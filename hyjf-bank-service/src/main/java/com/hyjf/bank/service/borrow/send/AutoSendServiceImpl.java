package com.hyjf.bank.service.borrow.send;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.borrow.AssetServiceImpl;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.*;
import com.hyjf.mybatis.messageprocesser.MailMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.AdminSystem;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AutoSendServiceImpl extends AssetServiceImpl implements AutoSendService {
	
	Logger _log = LoggerFactory.getLogger(AutoSendServiceImpl.class);

	public static JedisPool pool = RedisUtils.getPool();
	
	@Autowired
	@Qualifier("mailProcesser")
	private MessageProcesser mailMessageProcesser;
	
	/**
	 * 汇消费的项目类型编号
	 */
	public static String PROJECT_TYPE_HXF = "8";
	
	/**
	 * 邮件发送key
	 */
	public static String LABEL_MAIL_KEY = "labelmailkey";
	/**
	 * 查询已经审核的初始状态的资产
	 * 
	 * @return
	 */
	@Override
	public List<HjhPlanAsset> selectAutoSendBorrowList() {
		
		HjhPlanAssetExample example = new HjhPlanAssetExample();
		HjhPlanAssetExample.Criteria crt = example.createCriteria();
        crt.andVerifyStatusEqualTo(1);
        crt.andStatusEqualTo(0);
        
        List<HjhPlanAsset> list = this.hjhPlanAssetMapper.selectByExample(example);
        
		
		return list;
	}
	
	/**
	 * 查询单个资产根据资产ID
	 * 
	 * @return
	 */
	@Override
	public HjhPlanAsset selectPlanAsset(String assetId, String instCode) {
		HjhPlanAsset resultAsset = null;
		HjhPlanAssetExample example = new HjhPlanAssetExample();
		HjhPlanAssetExample.Criteria crt = example.createCriteria();
		crt.andAssetIdEqualTo(assetId);
		crt.andInstCodeEqualTo(instCode);
        
        List<HjhPlanAsset> list = this.hjhPlanAssetMapper.selectByExample(example);
        
        if(list != null && list.size() > 0){
        	resultAsset = list.get(0);
        }
		
		return resultAsset;
	}
	
	/**
	 * 资产自动录标
	 * 
	 * @param hjhPlanAsset
	 * @return
	 * @throws Exception 
	 */

	@Override
	public boolean insertSendBorrow(HjhPlanAsset hjhPlanAsset,HjhAssetBorrowType hjhAssetBorrowType) throws Exception {
		
		// 验证资产风险保证金是否足够（redis）
		Integer checkResult = checkAssetCanSend(hjhPlanAsset);
		if(checkResult ==  -1){
			return false;
		}
		if (checkResult > 0) {
			_log.info("资产编号："+hjhPlanAsset.getAssetId()+" 录标校验未通过:" + checkResult);
			//add by cwyang 20180420 增加待补缴状态
			HjhPlanAsset planAsset = new HjhPlanAsset();
			planAsset.setId(hjhPlanAsset.getId());
			planAsset.setStatus(checkResult);//待补缴保证金
			this.hjhPlanAssetMapper.updateByPrimaryKeySelective(planAsset);
			//end
			return false;
		}
		
		// 获取管理费率，服务费率，自动发标费率
		// 项目类型(code):从hyjf_hjh_asset_borrowtype 取code 现金贷
		String projectCd = hjhAssetBorrowType.getBorrowCd()+"";
		String borrowClass = this.getBorrowProjectClass(projectCd);
		String queryBorrowStyle = null;
		// 费率配置表有点尴尬，还款方式只区分了天和月
		if ("endday".equals(hjhPlanAsset.getBorrowStyle())) {//天标
			queryBorrowStyle  = "endday";
			
		}else {
			queryBorrowStyle = "month";
		}
		BorrowFinmanNewCharge borrowFinmanNewCharge = this.selectBorrowApr(borrowClass,hjhPlanAsset.getInstCode(),
				hjhPlanAsset.getAssetType(),queryBorrowStyle,hjhPlanAsset.getBorrowPeriod());
		if(borrowFinmanNewCharge == null || borrowFinmanNewCharge.getAutoBorrowApr() == null){
			_log.info("资产编号："+hjhPlanAsset.getAssetId()+" 录标失败 ,没有取到项目费率");
			return false;
		}
		
		// 录标 a. 根据表配置判断发标项目类型
		if(!insertRecord(hjhPlanAsset,hjhAssetBorrowType,borrowFinmanNewCharge)){
			_log.info("资产编号："+hjhPlanAsset.getAssetId()+" 录标失败");
			return false;
		}

		// 更新额度
		updateForSend(hjhPlanAsset.getInstCode(), new BigDecimal(hjhPlanAsset.getAccount()));
		// 累加日已用额度
		String dayUsedKey = RedisConstants.DAY_USED + hjhPlanAsset.getInstCode() + "_" + GetDate.getDate("yyyyMMdd");
		RedisUtils.redisAdd(dayUsedKey, String.valueOf(hjhPlanAsset.getAccount()));
		// 累加月已用额度
		String monthKey = RedisConstants.MONTH_USED + hjhPlanAsset.getInstCode() + "_" + GetDate.getDate("yyyyMM");
		RedisUtils.redisAdd(monthKey, String.valueOf(hjhPlanAsset.getAccount()));

		return true;
	}

	/**
	 * 验证资产风险保证金是否足够（redis）
	 * @param hjhPlanAsset
	 * @return
	 */
	private Integer checkAssetCanSend(HjhPlanAsset hjhPlanAsset) {
		String instCode = hjhPlanAsset.getInstCode();
		HjhBailConfig bailConfig = this.getBailConfig(instCode);
//		BigDecimal availableBalance = bailConfig.getRemainMarkLine();
		BigDecimal assetAcount = new BigDecimal(hjhPlanAsset.getAccount());

		if(bailConfig == null){
			_log.error("没有添加保证金配置");
			return -1;
		}

		// 可用发标额度余额校验
		/*if (BigDecimal.ZERO.compareTo(availableBalance) >= 0) {
			_log.info("资产编号："+hjhPlanAsset.getAssetId()+" 可用发标额度余额小于等于零 " + availableBalance);
			// 可用发标额度余额小于等于0不能发标
			return 1;
		}
		if(assetAcount.compareTo(availableBalance) > 0){
			// 可用发标额度余额不够不能发标
			return 1;
		}*/

		// 日推标额度校验
		BigDecimal dayAvailable = BigDecimal.ZERO;
		// 今日已用额度
		String dayUsedKey = RedisConstants.DAY_USED + instCode + "_" + GetDate.getDate("yyyyMMdd");
		BigDecimal dayUsed = getValueInRedis(dayUsedKey);
		_log.info("dayUsedKey: " + dayUsedKey + " day userd in redis: " + dayUsed);
		// 累积可用额度
		String accumulateKey = RedisConstants.DAY_MARK_ACCUMULATE + instCode;
		BigDecimal accumulate = getValueInRedis(accumulateKey);
		_log.info("accumulateKey: " + accumulateKey + " accumulate in redis: " + accumulate);
		dayAvailable = dayAvailable.add(bailConfig.getDayMarkLine()).subtract(dayUsed);
		if(bailConfig.getIsAccumulate() == 1){
            dayAvailable = dayAvailable.add(accumulate);
			_log.info("已开启日累计额度，当前可用额度：" + dayAvailable);
		}
		if(dayAvailable.compareTo(assetAcount) < 0){
			_log.info("日推标可用额度不足，资产编号：" + instCode + " 当前可用额度：{}，推送额度:{}", dayAvailable, assetAcount);
			return 23;
		}

		// 月推标额度校验
		BigDecimal monthAvailable = BigDecimal.ZERO;
		String monthKey = RedisConstants.MONTH_USED + instCode + "_" + GetDate.getDate("yyyyMM");
		BigDecimal monthUsed = getValueInRedis(monthKey);
		_log.info("monthKey: " + monthKey + " month userd in redis: " + monthUsed);
		monthAvailable = monthAvailable.add(bailConfig.getMonthMarkLine()).subtract(monthUsed);
		if(monthAvailable.compareTo(assetAcount) < 0){
			_log.info("月推标可用额度不足，资产编号：" + instCode + " 当前可用额度：{}，推送额度:{}", monthAvailable, assetAcount);
			return 24;
		}

		// 授信额度校验 （合规改造删除 modify by hesy 2018-12-04）
//		HjhBailConfigInfo configInfo = getConfigInfo(hjhPlanAsset.getBorrowStyle(), hjhPlanAsset.getInstCode());
//		if(configInfo == null){
//			_log.info("HjhBailConfigInfo不存在，机构编号：{}, 还款方式：{}", hjhPlanAsset.getInstCode(), hjhPlanAsset.getBorrowStyle());
//			return -1;
//		}
//
//		if(configInfo.getIsNewCredit() != 1 ){
//			_log.error("因还款方式未配置合作额度授信方式，推标失败");
//			return -1;
//		}

//		if(configInfo.getIsNewCredit() == 1){
			// 合作额度校验
			if(!checkNewCredit(bailConfig, assetAcount)){
				return 21;
			}

//		}
		// 合规改造删除 2018-12-03
		/*else if(configInfo.getIsNewCredit() != 1 && configInfo.getIsLoanCredit() == 1){
			// 在贷余额授信校验
			if(!checkLoanCredit(bailConfig, assetAcount)){
				return 22;
			}

		}else if(configInfo.getIsNewCredit() == 1 && configInfo.getIsLoanCredit() == 1){
			// 新增授信校验或在贷余额校验一个校验通过就可以
			if(!checkNewCredit(bailConfig, assetAcount) && !checkLoanCredit(bailConfig, assetAcount)){
				return 21;
			}
		}*/
		// 新增授信校验或在贷余额校验都没有配置
//		if(configInfo.getIsNewCredit() != 1 && configInfo.getIsLoanCredit() != 1){
//			_log.error("因还款方式未配置保证金授信方式，推标失败");
//			return -1;
//		}

		return 0;
	}

	private BigDecimal getValueInRedis(String key){
		_log.info("get value in redis, key:{}", key);
		String value = RedisUtils.get(key);
		_log.info("value in redis, key:{}, value:{}", key, value);

		if (StringUtils.isBlank(value)){
			RedisUtils.set(key, "0");
			return BigDecimal.ZERO;
		}
		return new BigDecimal(value);
	}

	/**
	 * 新增授信额度校验
	 * @return
	 */
	private boolean checkNewCredit(HjhBailConfig bailConfig, BigDecimal account){
		// 新增授信额度
		BigDecimal newCreditLine = bailConfig.getNewCreditLine();
		// 周期内发标已发额度
		BigDecimal cycLoanTotal = bailConfig.getCycLoanTotal().add(account);
		_log.info("周期内发标已发额度+本次推标额度：" + cycLoanTotal);

		if(newCreditLine.compareTo(cycLoanTotal) < 0){
			_log.info("周期内发标已发额度超过授信额度");
			return false;
		}
		return true;
	}

	/**
	 * 在贷余额额度校验
	 * @return
	 */
	private boolean checkLoanCredit(HjhBailConfig bailConfig, BigDecimal account){
		// 在贷授信额度
		BigDecimal loanCreditLine = bailConfig.getLoanCreditLine();
		// 历史标的发标总额
		BigDecimal hisLoanTotal = bailConfig.getLoanMarkLine().add(account);
		_log.info("机构编号：{}, 发标已发额度+本次推标额度：{}", bailConfig.getInstCode(), hisLoanTotal);
		// 已还本金
		BigDecimal repayedCapital = bailConfig.getRepayedCapital();
		_log.info("机构编号：{}, 已还本金：{}", bailConfig.getInstCode(), repayedCapital);

		if(loanCreditLine.compareTo(hisLoanTotal.subtract(repayedCapital)) < 0){
			_log.info("机构编号：{}, 在贷余额额度已超过授信额度", bailConfig.getInstCode());
			return false;
		}
		return true;
	}

	/**
	 * 发标完成更新相应额度
	 * @param instCode
	 * @param assetAccount
	 * @return
	 */
	private Integer updateForSend(String instCode, BigDecimal assetAccount){
		Map<String,Object> paraMap = new HashMap<>();
		paraMap.put("amount", assetAccount);
		paraMap.put("instCode", instCode);

		return  hjhBailConfigCustomizeMapper.updateForSendBorrow(paraMap);
	}




	private HjhBailConfigInfo getConfigInfo(String borrowStyle, String instCode){
		HjhBailConfigInfoExample example = new HjhBailConfigInfoExample();
		example.createCriteria().andInstCodeEqualTo(instCode).andBorrowStyleEqualTo(borrowStyle).andDelFlgEqualTo(0);

		List<HjhBailConfigInfo> list = hjhBailConfigInfoMapper.selectByExample(example);
		if(list != null && !list.isEmpty()){
			return list.get(0);
		}
		return null;
	}

	/**
	 * 录标
	 * 
	 * @param hjhPlanAsset
	 * @throws Exception
	 */
	private boolean insertRecord(HjhPlanAsset hjhPlanAsset,HjhAssetBorrowType hjhAssetBorrowType,BorrowFinmanNewCharge borrowFinmanNewCharge) throws Exception {
		
		boolean result = false;
		
		
		// borrow_class
		String beforeFix = borrowFinmanNewCharge.getProjectType();
		
		BorrowWithBLOBs borrow = this.setBorrowCommonData(hjhPlanAsset, hjhAssetBorrowType, borrowFinmanNewCharge);
		
		// 获取标签ID
		HjhLabel label = this.getLabelId(borrow,hjhPlanAsset);
		if(label == null || label.getId() ==null){
			_log.info(hjhPlanAsset.getAssetId()+" 没有获取到标签");
			
			/**汇计划三期邮件预警 BY LIBIN start*/
			// 如果redis不存在这个KEY(一天有效期)，那么可以发邮件
			if(!RedisUtils.exists(LABEL_MAIL_KEY + hjhPlanAsset.getAssetId())){
				StringBuffer msg = new StringBuffer();
				msg.append("资产ID：").append(hjhPlanAsset.getAssetId()).append("<br/>");
				msg.append("当前时间：").append(GetDate.formatTime()).append("<br/>");
				msg.append("错误信息：").append("该资产在自动录标时未打上标签！").append("<br/>");
				// 邮箱集合
				Boolean env_test = "true".equals(PropUtils.getSystem("hyjf.env.test")) ? true : false;
				_log.info("自动录标时未打上标签环境 evn_test is test ? " + env_test);
				String emailList= "";
				if (env_test){
					emailList = PropUtils.getSystem("hyjf.alerm.email.test");
				}else{
					emailList = PropUtils.getSystem("hyjf.alerm.email");
				}
				String [] toMail = emailList.split(",");
				MailMessage message = new MailMessage(null, null, "资产ID为：" + hjhPlanAsset.getAssetId(), msg.toString(), null, toMail, null,
						MessageDefine.MAILSENDFORMAILINGADDRESSMSG);
				int num = mailMessageProcesser.gather(message);
				if( num > 0){
					// String key, String value, int expireSeconds 
					RedisUtils.set(LABEL_MAIL_KEY + hjhPlanAsset.getAssetId(), hjhPlanAsset.getAssetId(), 24 * 60 * 60);
				}
			} else {
				_log.info("此邮件key值还未过期(一天)");
			}
			/**汇计划三期邮件预警 BY LIBIN end*/
			return result;
		}
		
		// 获取下一个标的编号
		String borrowPreNidNew = getNextBorrowNid();
		
		// 标签ID
		borrow.setLabelId(label.getId());
		/*--------add by liushouyi HJH3 Start---------*/
		// 默认使用引擎
		borrow.setIsEngineUsed(1);
		/*--------add by liushouyi HJH3 End---------*/
		
		String borrowNid = beforeFix + borrowPreNidNew;
		//项目标题
		borrow.setProjectName(borrowNid);
		// 借款编号
		borrow.setBorrowNid(borrowNid);
		// 借款预编码
		borrow.setBorrowPreNid(borrowPreNidNew);
		// 新借款预编码
		borrow.setBorrowPreNidNew(borrowPreNidNew);
		
		
		// 借款表插入
		this.borrowMapper.insertSelective(borrow);
		if(hjhPlanAsset.getBorrowCompanyName() == null || hjhPlanAsset.getBorrowCompanyName().equals("")){
			// 个人信息
			this.insertBorrowManinfo(borrowNid, hjhPlanAsset, borrow);
		}else{
			//企业信息
			this.insertBorrowCompanyManinfo(borrowNid, hjhPlanAsset, borrow);
		}

		// 更新资产表
		HjhPlanAsset hjhPlanAssetnew = new HjhPlanAsset();
		hjhPlanAssetnew.setId(hjhPlanAsset.getId());
		// 标的编号，计划编号在关联资产更新
		hjhPlanAssetnew.setBorrowNid(borrowNid);
		hjhPlanAssetnew.setLabelId(label.getId());
		hjhPlanAssetnew.setLabelName(label.getLabelName());
		
		hjhPlanAssetnew.setStatus(3);//备案中
		//获取当前时间
		int nowTime = GetDate.getNowTime10();
		hjhPlanAssetnew.setUpdateTime(nowTime);
		hjhPlanAssetnew.setUpdateUserId(1);
		boolean borrowFlag = this.hjhPlanAssetMapper.updateByPrimaryKeySelective(hjhPlanAssetnew)>0?true:false;
		if(borrowFlag){
			result = true;
		}
		
		return result;
	}
	
	
	private String getNextBorrowNid(){
		// 操作redis
		// 拿取实际的项目编号
		String borrowPreNidNew = "";
		Jedis jedis = pool.getResource();
		
		String borrowPreNid = getBorrowPreNid();//初始标的号码 -->根据年月放当月最初值
		while ("OK".equals(jedis.watch(RedisConstants.GEN_HJH_BORROW_NID))) {
			List<Object> results = null;
			Transaction tx = jedis.multi();
			borrowPreNidNew = RedisUtils.get(RedisConstants.GEN_HJH_BORROW_NID);
			if (StringUtils.isBlank(borrowPreNidNew)) {
				tx.set(RedisConstants.GEN_HJH_BORROW_NID, borrowPreNid);
				borrowPreNidNew = borrowPreNid;
				results = tx.exec();
			} else if (borrowPreNidNew != null) {
				if(Long.parseLong(borrowPreNid)>Long.parseLong(borrowPreNidNew)){
					borrowPreNidNew = (String.valueOf(borrowPreNid));
				}else{
					borrowPreNidNew = (String.valueOf(Long.valueOf(borrowPreNidNew) + 1));
				}
				tx.set(RedisConstants.GEN_HJH_BORROW_NID, borrowPreNidNew);
				results = tx.exec();
			}
			if (results == null || results.isEmpty()) {
				jedis.unwatch();
			} else {
				String ret = (String) results.get(0);
				if (ret != null && ret.equals("OK")) {
					borrowPreNid = borrowPreNidNew;
					break;
				} else {
					jedis.unwatch();
				}
			}
		}
		
		return borrowPreNidNew;
		
	}


	/**
	 * 借款表插入
	 * 
	 * @param hjhPlanAsset
	 * @param hjhPlanAsset
	 * @return
	 * @throws Exception
	 */
	private BorrowWithBLOBs setBorrowCommonData(HjhPlanAsset hjhPlanAsset, HjhAssetBorrowType hjhAssetBorrowType, BorrowFinmanNewCharge borrowFinmanNewCharge)
			throws Exception {

		// 插入huiyingdai_borrow
		BorrowWithBLOBs borrow = new BorrowWithBLOBs();
		
		// 关联计划
		borrow.setIsShow(1); // 默认不展示
		
		borrow.setInstCode(hjhPlanAsset.getInstCode());
		borrow.setAssetType(hjhAssetBorrowType.getAssetType());
		
		// 受托支付
		if (hjhPlanAsset.getEntrustedFlg() != null && hjhPlanAsset.getEntrustedFlg().intValue() ==1) {
			borrow.setEntrustedFlg(1);
			borrow.setEntrustedUserId(hjhPlanAsset.getEntrustedUserId());
			borrow.setEntrustedUserName(hjhPlanAsset.getEntrustedUserName());
		}
		
		// 插入时间
		Date systemNowDate = GetDate.getDate();
		// 添加时间
		String addtime = String.valueOf(GetDate.getNowTime10());
		// 根据项目类型设置下列
		BorrowProjectType borrowProjectType = getProjectType(hjhAssetBorrowType.getBorrowCd()+"");
		borrow.setBorrowIncreaseMoney(borrowProjectType.getIncreaseMoney()); //递增出借金额
		borrow.setBorrowInterestCoupon(borrowProjectType.getInterestCoupon()); 
		borrow.setBorrowTasteMoney(borrowProjectType.getTasteMoney());//体验金

		borrow.setUserId(hjhPlanAsset.getUserId());
		// 借款人用户名
		borrow.setBorrowUserName(hjhPlanAsset.getUserName());
		
		// 项目申请人
//		String applicant = hjhAssetBorrowType.getApplicant();
		String repayOrgName = hjhAssetBorrowType.getRepayOrgName();
		borrow.setApplicant(hjhPlanAsset.getIdcard());
		
		// 垫付机构用户名不为空的情况
		if (StringUtils.isNotEmpty(repayOrgName)) {
			// 根据垫付机构用户名检索垫付机构用户ID
			UsersExample usersExample = new UsersExample();
			UsersExample.Criteria userCri = usersExample.createCriteria();
			userCri.andUsernameEqualTo(repayOrgName);
			userCri.andBankOpenAccountEqualTo(1);// 汇付已开户
			List<Users> ulist = this.usersMapper.selectByExample(usersExample);
			// 如果用户名不存在，返回错误信息。
			if (ulist == null || ulist.size() == 0) {
				throw new Exception("获取垫付机构失败,垫付机构名称:" + repayOrgName);
			}
			Integer userId = ulist.get(0).getUserId();
			borrow.setRepayOrgUserId(userId);
			borrow.setIsRepayOrgFlag(1);
			// 垫付机构用户名
			borrow.setRepayOrgName(repayOrgName);
		} else {
			borrow.setRepayOrgUserId(0);
			borrow.setIsRepayOrgFlag(0);
		}
		
		// 借款标题
		borrow.setName("个人短期借款");
		// 状态
		borrow.setStatus(0);
		// 图片信息
		borrow.setBorrowPic("");
		// 点击次数
		borrow.setHits(0);
		borrow.setCommentCount(0);// 插入时不用的字段
		//新标（20170612改版后都为新标）
		borrow.setIsNew(1);
		
		// 借款方式
		// 车辆抵押:2 房产抵押:1
//		if (StringUtils.equals("2", borrowBean.getTypeCar())) {
//			borrow.setType(borrowBean.getTypeCar());
//		}
//
//		if (StringUtils.equals("1", borrowBean.getTypeHouse())) {
//			borrow.setType(borrowBean.getTypeHouse());
//		}
//
//		if (StringUtils.equals("2", borrowBean.getTypeCar()) && StringUtils.equals("1", borrowBean.getTypeHouse())) {
//			borrow.setType("3");
//		}

		if (StringUtils.isEmpty(borrow.getType())) {
			borrow.setType("0");
		}

		borrow.setViewType("");// 插入时不用的字段
		// 添加时间
		borrow.setAddtime(addtime);
		// 添加IP
		borrow.setAddip("localhost");
		// 冻结额度
		borrow.setAmountAccount(new BigDecimal(hjhPlanAsset.getAccount()));
		borrow.setAmountType("credit");// 插入时不用的字段
		borrow.setCashStatus(0);// 插入时不用的字段
		// 借款总金额
		borrow.setAccount(new BigDecimal(hjhPlanAsset.getAccount()));
		borrow.setBorrowAccountWait(new BigDecimal(hjhPlanAsset.getAccount()));
		borrow.setBorrowAccountWaitAppoint(new BigDecimal(hjhPlanAsset.getAccount()));
		borrow.setOtherWebStatus(0);// 插入时不用的字段
		
		// 财务状况
//		if (StringUtils.isEmpty(hjhPlanAsset.getAccountContents())) {
//			borrow.setAccountContents(StringUtils.EMPTY);
//		} else {
//			borrow.setAccountContents(hjhPlanAsset.getAccountContents());
//		}
		borrow.setAccountContents(StringUtils.EMPTY);
		borrow.setBorrowType("credit");// 插入时不用的字段
		borrow.setBorrowPassword("");// 插入时不用的字段
		borrow.setBorrowFlag("");// 插入时不用的字段
		// 是否可以进行借款
		borrow.setBorrowStatus(0);
		// 满表审核状态
		borrow.setBorrowFullStatus(0);
		// 已经募集的金额
		borrow.setBorrowAccountYes(BigDecimal.ZERO);
		// // 剩余的金额
		// borrow.setBorrowAccountWait(BigDecimal.ZERO);
		// 募集完成率
		borrow.setBorrowAccountScale(BigDecimal.ZERO);
		// 还款方式
		borrow.setBorrowStyle(hjhPlanAsset.getBorrowStyle());
		// 借款期限
		borrow.setBorrowPeriod(hjhPlanAsset.getBorrowPeriod());
		borrow.setBorrowPeriodRoam(0);// 插入时不用的字段
		borrow.setBorrowDay(0);// 插入时不用的字段
		// 借款利率
		borrow.setBorrowApr(new BigDecimal(borrowFinmanNewCharge.getAutoBorrowApr()).multiply(new BigDecimal(100)));
		borrow.setLateInterestRate(borrowFinmanNewCharge.getLateInterest()); //逾期利率(汇计划用)late_interest_rate
		borrow.setLateFreeDays(borrowFinmanNewCharge.getLateFreeDays()); // 逾期免息天数(汇计划用)late_free_days
		
		// 项目描述
		borrow.setBorrowContents(hjhPlanAsset.getAssetInfo());
		// 新增协议期限字段
//		if (StringUtils.isNotEmpty(borrowBean.getContractPeriod())) {
//			borrow.setContractPeriod(Integer.parseInt(borrowBean.getContractPeriod()));
//		}
		// 信用评级
		if (StringUtils.isNotEmpty(hjhPlanAsset.getCreditLevel())) {
			borrow.setBorrowLevel(hjhPlanAsset.getCreditLevel());
		}
		// ----------风险缓释金添加-------
		// 资产编号
//		borrow.setBorrowAssetNumber(borrowBean.getBorrowAssetNumber());
//		// 项目来源
//		borrow.setBorrowProjectSource(borrowBean.getBorrowProjectSource());
//		// 起息时间
//		borrow.setBorrowInterestTime(borrowBean.getBorrowInterestTime());
//		// 到期时间
//		borrow.setBorrowDueTime(borrowBean.getBorrowDueTime());
//		// 保障方式
//		borrow.setBorrowSafeguardWay(borrowBean.getBorrowSafeguardWay());
//		// 收益说明
//		borrow.setBorrowIncomeDescription(borrowBean.getBorrowIncomeDescription());
//		// 发行人
//		borrow.setBorrowPublisher(borrowBean.getBorrowPublisher());
		// 产品加息收益率
//		borrow.setBorrowExtraYield(new BigDecimal(StringUtils.isNotEmpty(borrowBean.getBorrowExtraYield()) ? borrowBean.getBorrowExtraYield() : "0"));
		// ----------风险缓释金添加 end-------

		/**************网站改版添加 ******************/
		//融资用途
		borrow.setFinancePurpose(hjhPlanAsset.getUseage());
		// 平台直接默认填写：借款用途
		borrow.setBorrowUse(hjhPlanAsset.getUseage());
		
		//月薪收入
		borrow.setMonthlyIncome(hjhPlanAsset.getMonthlyIncome());
		//还款来源
//		borrow.setPayment(hjhPlanAsset.getPayment());
		//第一还款来源
		borrow.setFirstPayment(hjhPlanAsset.getFirstPayment());
		//第二还款来源
		borrow.setSecondPayment(hjhPlanAsset.getSecondPayment());
		//费用说明
		borrow.setCostIntrodution(hjhPlanAsset.getCostIntrodution());
		//财务状况
//		borrow.setFianceCondition(hjhPlanAsset.getFianceCondition());
		/**************网站改版添加end ******************/
		
		borrow.setBorrowFrostAccount(BigDecimal.ZERO);// 插入时不用的字段
		borrow.setBorrowFrostScale("");// 插入时不用的字段
		borrow.setBorrowFrostSecond(BigDecimal.ZERO);// 插入时不用的字段
					
		// 借款有效时间
		borrow.setBorrowValidTime(Integer.parseInt(getBorrowConfig("BORROW_VALID_TIME")));
		borrow.setRegistStatus(0);// 银行备案状态
		// 银行备案时间
		borrow.setBankRegistDays(Integer.parseInt(getBorrowConfig("BORROW_REGIST_DAYS")));
		borrow.setRepayStatus(0);// 标的还款状态
		// 银行募集开始时间
		String rasieStartDate = GetOrderIdUtils.getOrderDate();
		borrow.setBankRaiseStartDate(rasieStartDate);
		// 银行募集结束时间
		String raiseEndDate = this.getBankRaiseEndDate(rasieStartDate, borrow.getBankRegistDays(), borrow.getBorrowValidTime());
		borrow.setBankRaiseEndDate(raiseEndDate);
		// 银行用借款期限
		if (borrow.getBorrowStyle().equals(CustomConstants.BORROW_STYLE_ENDDAY)) {
			borrow.setBankBorrowDays(borrow.getBorrowPeriod());
		}
		/** 月标直接写死每月30天，银行不校验.计算过于麻烦 */
		else {
			borrow.setBankBorrowDays(borrow.getBorrowPeriod() * 30);
		}
		// 借款成功时间
		borrow.setBorrowSuccessTime(0);
		// 借款到期时间
		borrow.setBorrowEndTime("");
		borrow.setBorrowPartStatus(0);// 插入时不用的字段
		borrow.setBorrowUpfiles("");// 插入时不用的字段
		borrow.setCancelUserid(0);// 插入时不用的字段
		borrow.setCancelStatus(0);// 插入时不用的字段
		borrow.setCancelTime("");// 插入时不用的字段
		borrow.setCancelRemark("");// 插入时不用的字段
		borrow.setCancelContents("");// 插入时不用的字段
		
		// 最低投标金额
		if (StringUtils.isNotEmpty(borrowProjectType.getInvestStart())) {
			borrow.setTenderAccountMin(Integer.valueOf(borrowProjectType.getInvestStart()));
		} else {
			borrow.setTenderAccountMin(0);
		}

		// 最高投标金额
		if (StringUtils.isNotEmpty(borrowProjectType.getInvestEnd())) {
			borrow.setTenderAccountMax(Integer.valueOf(borrowProjectType.getInvestEnd()));
		} else {
			borrow.setTenderAccountMax(0);
		}
		// 投标次数
		borrow.setTenderTimes(0);
		// 最后出借时间
		borrow.setTenderLastTime("");
		borrow.setRepayAdvanceStatus(0);// 插入时不用的字段
		borrow.setRepayAdvanceTime("");// 插入时不用的字段
		borrow.setRepayAdvanceStep(0);// 插入时不用的字段
		// 应还款总额
		borrow.setRepayAccountAll(BigDecimal.ZERO);
		// 总还款利息
		borrow.setRepayAccountInterest(BigDecimal.ZERO);
		// 总还款本金
		borrow.setRepayAccountCapital(BigDecimal.ZERO);
		// 已还款总额
		borrow.setRepayAccountYes(BigDecimal.ZERO);
		// 已还款利息
		borrow.setRepayAccountInterestYes(BigDecimal.ZERO);
		// 已还款本金
		borrow.setRepayAccountCapitalYes(BigDecimal.ZERO);
		// 未还款总额
		borrow.setRepayAccountWait(BigDecimal.ZERO);
		// 未还款利息
		borrow.setRepayAccountInterestWait(BigDecimal.ZERO);
		// 未还款本金
		borrow.setRepayAccountCapitalWait(BigDecimal.ZERO);
		borrow.setRepayAccountTimes(0);// 插入时不用的字段
		borrow.setRepayMonthAccount(0);// 插入时不用的字段
		// 最后还款时间
		borrow.setRepayLastTime("");// 插入时不用的字段
		borrow.setRepayEachTime("");// 插入时不用的字段
		borrow.setRepayNextTime(0);// 插入时不用的字段
		borrow.setRepayNextAccount(BigDecimal.ZERO);// 插入时不用的字段
		// 还款次数
		borrow.setRepayTimes(0);
		borrow.setRepayFullStatus(0);// 插入时不用的字段
		borrow.setRepayFeeNormal(BigDecimal.ZERO); // 插入时不用的字段
													// 正常还款费用
		borrow.setRepayFeeAdvance(BigDecimal.ZERO); // 插入时不用的字段
													// 提前还款费用
		borrow.setRepayFeeLate(BigDecimal.ZERO); // 插入时不用的字段
													// 逾期还款费用
		borrow.setLateInterest(BigDecimal.ZERO); // 插入时不用的字段
													// 逾期利息
		borrow.setLateForfeit(BigDecimal.ZERO); // 插入时不用的字段
												// 逾期催缴费
		borrow.setVouchStatus(0); // 插入时不用的字段 是否是担保
		borrow.setVouchAdvanceStatus(0); // 插入时不用的字段
		borrow.setVouchUserStatus(0); // 插入时不用的字段 担保人担保状态
		borrow.setVouchUsers(""); // 插入时不用的字段 担保人列表
		borrow.setVouchAccount(BigDecimal.ZERO); // 插入时不用的字段
													// 总担保的金额
		borrow.setVouchAccountYes(BigDecimal.ZERO); // 插入时不用的字段
													// 已担保的金额
		borrow.setVouchAccountWait(BigDecimal.ZERO); // 插入时不用的字段
		borrow.setVouchAccountScale(0L); // 插入时不用的字段 已担保的比例
		borrow.setVouchTimes(0); // 插入时不用的字段 担保次数
		borrow.setVouchAwardStatus(0); // 插入时不用的字段 是否设置担保奖励
		borrow.setVouchAwardScale(BigDecimal.ZERO); // 插入时不用的字段
													// 担保比例
		borrow.setVouchAwardAccount(BigDecimal.ZERO); // 插入时不用的字段
														// 总付出的担保奖励

		borrow.setVoucherName("");// 插入时不用的字段
		borrow.setVoucherLianxi("");// 插入时不用的字段
		borrow.setVoucherAtt("");// 插入时不用的字段
		borrow.setVouchjgName("");// 插入时不用的字段
		borrow.setVouchjgLianxi("");// 插入时不用的字段
		borrow.setVouchjgJs("");// 插入时不用的字段
		borrow.setVouchjgXy("");// 插入时不用的字段
		borrow.setFastStatus(0);// 插入时不用的字段
		borrow.setVouchstatus(0);// 插入时不用的字段
		borrow.setGroupStatus(0);// 插入时不用的字段
		borrow.setGroupId(0);// 插入时不用的字段

		borrow.setAwardStatus(0); // 插入时不用的字段 是否奖励
		borrow.setAwardFalse(0); // 插入时不用的字段 出借失败是否也奖励
		// 插入时不用的字段 奖励金额
		borrow.setAwardAccount(BigDecimal.ZERO);
		// 插入时不用的字段 按比例奖励
		borrow.setAwardScale(BigDecimal.ZERO);
		// 插入时不用的字段 投标奖励总额
		borrow.setAwardAccountAll(BigDecimal.ZERO);

		borrow.setOpenAccount(0); // 插入时不用的字段 公开我的帐户资金情况
		borrow.setOpenBorrow(0); // 插入时不用的字段 公开我的借款资金情况
		borrow.setOpenTender(0); // 插入时不用的字段 公开我的投标资金情况
		borrow.setOpenCredit(0); // 插入时不用的字段 公开我的信用额度情况
		// 是否可以评论
		borrow.setCommentStaus(0);
		borrow.setCommentTimes(0); // 插入时不用的字段 评论次数
		borrow.setCommentUsertype(""); // 插入时不用的字段 可评论的用户
		borrow.setDiyaContents(""); // 插入时不用的字段
		borrow.setBorrowPawnApp(""); // 插入时不用的字段
		borrow.setBorrowPawnAppUrl(""); // 插入时不用的字段
		borrow.setBorrowPawnAuth(""); // 插入时不用的字段
		borrow.setBorrowPawnAuthUrl(""); // 插入时不用的字段
		borrow.setBorrowPawnFormalities(""); // 插入时不用的字段
		borrow.setBorrowPawnFormalitiesUrl(""); // 插入时不用的字段
		borrow.setBorrowPawnType(""); // 插入时不用的字段
		borrow.setBorrowPawnTime(""); // 插入时不用的字段
		borrow.setBorrowPawnDescription(""); // 插入时不用的字段
		borrow.setBorrowPawnValue(""); // 插入时不用的字段
		borrow.setBorrowPawnXin(""); // 插入时不用的字段
		borrow.setOrderTop(""); // 插入时不用的字段 置顶时间
		// 初审核人
		borrow.setVerifyUserid("0");
		// 正式发标时间
		borrow.setVerifyTime("0");
		// 初审通过时间
		borrow.setVerifyOverTime(0);
		// 初审核备注
		borrow.setVerifyRemark("");
		borrow.setVerifyContents(""); // 插入时不用的字段 审核备注
		borrow.setVerifyStatus(0); // 插入时不用的字段
		// 复审核人
		borrow.setReverifyUserid("0");
		// 复审核时间
		borrow.setReverifyTime("0");
		// 复审核备注
		borrow.setReverifyRemark("");
		borrow.setReverifyStatus(0); // 插入时不用的字段
		borrow.setReverifyContents(""); // 插入时不用的字段 审核复审标注
		borrow.setUpfilesId(""); // 插入时不用的字段 发标上传图片
		borrow.setBorrowRunningUse(""); // 插入时不用的字段 资金运转-用途
		borrow.setBorrowRunningSoruce(""); // 插入时不用的字段 资金运转-来源

//		// 担保机构 风险控制措施-机构
//		borrow.setBorrowMeasuresInstit(hjhPlanAsset.getBorrowMeasuresInstit());
//		// 机构介绍
//		borrow.setBorrowCompanyInstruction(hjhPlanAsset.getBorrowCompanyInstruction());
//		// 操作流程
//		borrow.setBorrowOperatingProcess(hjhPlanAsset.getBorrowOperatingProcess());
//		// 抵押物信息 风险控制措施-抵押物
//		borrow.setBorrowMeasuresMort(hjhPlanAsset.getBorrowMeasuresMort());
//		// 本息保障 险控制措施-措施  风控措施  现在默认写死
		String measureMea = "1、汇盈金服已对该项目进行了严格的审核，最大程度的确保借款方信息的真实性，但不保证审核信息完全无误。<br>2、汇盈金服仅为信息发布平台，不对出借人提供担保或承诺保本保息，出借人应根据自身的出借偏好和风险承受能力进行独立判断和作出决策。市场有风险，出借需谨慎。<br>";
		borrow.setBorrowMeasuresMea(measureMea);
		borrow.setBorrowAnalysisPolicy(""); // 插入时不用的字段 政策及市场分析-政策支持
		borrow.setBorrowAnalysisMarket(""); // 插入时不用的字段 政策及市场分析-市场分析
		borrow.setBorrowCompany(""); // 插入时不用的字段 企业背景
		borrow.setBorrowCompanyScope(""); // 插入时不用的字段 企业信息-营业范围
		borrow.setBorrowCompanyBusiness(""); // 插入时不用的字段 企业信息-经营状况
		borrow.setXmupfilesId(""); // 插入时不用的字段
		borrow.setDyupfilesId(""); // 插入时不用的字段
		// 项目资料
//		borrow.setFiles(this.getUploadImage(borrowBean, "", borrowNid));
		// 担保方式
		borrow.setGuaranteeType(0);
		// 项目类型
		borrow.setProjectType(hjhAssetBorrowType.getBorrowCd());

		// 融资服务费
		borrow.setServiceFeeRate(borrowFinmanNewCharge.getChargeRate());
		// 账户管理费率
		borrow.setManageFeeRate(borrowFinmanNewCharge.getManChargeRate());
		// 收益差率
		borrow.setDifferentialRate(borrowFinmanNewCharge.getReturnRate());

		//默认全选
		// 可出借平台_PC
		borrow.setCanTransactionPc("1");

		// 可出借平台_微网站
		borrow.setCanTransactionWei("1");

		// 可出借平台_IOS
		borrow.setCanTransactionIos("1");

		// 可出借平台_Android
		borrow.setCanTransactionAndroid("1");

		// 运营标签->hjh 默认不填
		borrow.setOperationLabel("0");
		// 判断是企业还是个人
		if(hjhPlanAsset.getBorrowCompanyName() == null || hjhPlanAsset.getBorrowCompanyName().equals("")){
			// 个人信息
			borrow.setCompanyOrPersonal("2");
		}else{
			//企业信息
			borrow.setCompanyOrPersonal("1");
		}
		// 定时发标
		borrow.setOntime(0);
		borrow.setBookingBeginTime(0);
		borrow.setBookingEndTime(0);
		borrow.setBookingStatus(0);
		borrow.setBorrowAccountScaleAppoint(BigDecimal.ZERO);
		// 汇资管的内容设置
//		this.setHZGInfo(borrowBean, borrow);
		// 更新时间
		borrow.setUpdatetime(systemNowDate);
		// 银行存管标识 0未进行银行存管 1已进行银行存管
		borrow.setBankInputFlag(0);
		// 标的还款后的回滚方式 (合规改造删除 2018-12-03)
//		HjhBailConfigInfoExample example = new HjhBailConfigInfoExample();
//		example.createCriteria().andInstCodeEqualTo(hjhPlanAsset.getInstCode()).andBorrowStyleEqualTo(hjhPlanAsset.getBorrowStyle());
//		List<HjhBailConfigInfo> hjhBailConfigInfoList = this.hjhBailConfigInfoMapper.selectByExample(example);
		// 推送资产的时候校验回滚方式是否配置、若未配置不得推送资产
//		if(null!=hjhBailConfigInfoList && hjhBailConfigInfoList.size()>0) {
//			borrow.setRepayCapitalType(hjhBailConfigInfoList.get(0).getRepayCapitalType());
//		}

		return borrow;

	}

	/**
	 * 个人信息
	 * 
	 * @param borrowNid
	 * @param hjhPlanAsset
	 * @param borrow
	 * @return
	 */
	public int insertBorrowManinfo(String borrowNid, HjhPlanAsset hjhPlanAsset, BorrowWithBLOBs borrow) {
		
		BorrowManinfo borrowManinfo = new BorrowManinfo();

		borrowManinfo.setBorrowNid(borrowNid);
		borrowManinfo.setBorrowPreNid(borrow.getBorrowPreNid());
		// 姓名
		if (StringUtils.isNotEmpty(hjhPlanAsset.getTruename())) {
			borrowManinfo.setName(hjhPlanAsset.getTruename());
		} else {
			borrowManinfo.setName(StringUtils.EMPTY);
		}
		// 性别
		if (hjhPlanAsset.getSex() != null) {
			borrowManinfo.setSex(hjhPlanAsset.getSex());
		} else {
			borrowManinfo.setSex(0);
		}
		// 年龄
		if (hjhPlanAsset.getAge() != null) {
			borrowManinfo.setOld(hjhPlanAsset.getAge());
		} else {
			borrowManinfo.setOld(0);
		}
		// 婚姻
		if (hjhPlanAsset.getMarriage() != null) {
			borrowManinfo.setMerry(hjhPlanAsset.getMarriage());
		} else {
			borrowManinfo.setMerry(0);
		}
		// 岗位职业
		if (StringUtils.isNotEmpty(hjhPlanAsset.getPosition())) {
			borrowManinfo.setPosition(hjhPlanAsset.getPosition());
		}
		// 省
//		if (StringUtils.isNotEmpty(hjhPlanAsset.getLocation_p())) {
//			borrowManinfo.setPro(hjhPlanAsset.getLocation_p());
//		} else {
//			borrowManinfo.setPro(StringUtils.EMPTY);
//		}
		// 市
		if (StringUtils.isNotEmpty(hjhPlanAsset.getWorkCity())) {
			borrowManinfo.setCity(hjhPlanAsset.getWorkCity());
		} else {
			borrowManinfo.setCity(StringUtils.EMPTY);
		}

		// 公司规模
		borrowManinfo.setSize(StringUtils.EMPTY);

		// 公司月营业额
		borrowManinfo.setBusiness(BigDecimal.ZERO);

		// 行业
//		if (StringUtils.isNotEmpty(borrowBean.getIndustry())) {
//			borrowManinfo.setIndustry(borrowBean.getIndustry());
//		} else {
//			borrowManinfo.setIndustry(StringUtils.EMPTY);
//		}

		// 现单位工作时间
//		if (StringUtils.isNotEmpty(borrowBean.getWtime())) {
//			borrowManinfo.setWtime(borrowBean.getWtime());
//		} else {
//			borrowManinfo.setWtime(StringUtils.EMPTY);
//		}

		// 授信额度
//		if (StringUtils.isNotEmpty(borrowBean.getUserCredit())) {
//			borrowManinfo.setCredit(Integer.valueOf((borrowBean.getUserCredit())));
//		} else {
//			borrowManinfo.setCredit(0);
//		}
		//身份证号
		if(StringUtils.isNotEmpty(hjhPlanAsset.getIdcard())){
			borrowManinfo.setCardNo(hjhPlanAsset.getIdcard());
		}else{
			borrowManinfo.setCardNo(StringUtils.EMPTY);
		}
		//户籍地
		if(StringUtils.isNotEmpty(hjhPlanAsset.getDomicile())){
			borrowManinfo.setDomicile(hjhPlanAsset.getDomicile());
		}else{
			borrowManinfo.setDomicile(StringUtils.EMPTY);
		}
		//在平台逾期次数
		if(StringUtils.isNotEmpty(hjhPlanAsset.getOverdueTimes())){
			borrowManinfo.setOverdueTimes(hjhPlanAsset.getOverdueTimes());
		}else{
			borrowManinfo.setOverdueTimes(StringUtils.EMPTY);
		}
		//在平台逾期金额
		if(StringUtils.isNotEmpty(hjhPlanAsset.getOverdueAmount())){
			borrowManinfo.setOverdueAmount(hjhPlanAsset.getOverdueAmount());
		}else{
			borrowManinfo.setOverdueAmount(StringUtils.EMPTY);
		}
		//涉诉情况
		if(StringUtils.isNotEmpty(hjhPlanAsset.getLitigation())){
			borrowManinfo.setLitigation(hjhPlanAsset.getLitigation());
		}else{
			borrowManinfo.setLitigation(StringUtils.EMPTY);
		}
		
		
		//个贷审核信息 身份证 0未审核 1已审核
		borrowManinfo.setIsCard(1);
		//个贷审核信息 收入状况 0未审核 1已审核
		borrowManinfo.setIsIncome(1);
		//个贷审核信息 信用状况 0未审核 1已审核
		borrowManinfo.setIsCredit(1);
		//个贷审核信息 婚姻状况 0未审核 1已审核
		borrowManinfo.setIsMerry(1);
		//个贷审核信息 工作状况 0未审核 1已审核
		borrowManinfo.setIsWork(1);
		
		//个贷审核信息 资产状况 0未审核 1已审核
		borrowManinfo.setIsAsset(0);
		//个贷审核信息 车辆状况0未审核 1已审核
		borrowManinfo.setIsVehicle(0);
		//个贷审核信息 行驶证 0未审核 1已审核
		borrowManinfo.setIsDrivingLicense(0);
		//个贷审核信息 车辆登记证 0未审核 1已审核
		borrowManinfo.setIsVehicleRegistration(0);
		//个贷审核信息 车辆登记证 0未审核 1已审核
		borrowManinfo.setIsVehicleRegistration(0);
		//个贷审核信息 户口本 0未审核 1已审核
		borrowManinfo.setIsAccountBook(0);
		
		//(个人)年收入
		if(StringUtils.isNotEmpty(hjhPlanAsset.getAnnualIncome())){
			borrowManinfo.setAnnualIncome(hjhPlanAsset.getAnnualIncome());
		}else{
			borrowManinfo.setAnnualIncome("10万以内");
		}
		//(个人)征信报告逾期情况
		if(StringUtils.isNotEmpty(hjhPlanAsset.getOverdueReport())){
			borrowManinfo.setOverdueReport(hjhPlanAsset.getOverdueReport());
		}else{
			borrowManinfo.setOverdueReport("暂无数据");
		}
		//(个人)重大负债状况
		if(StringUtils.isNotEmpty(hjhPlanAsset.getDebtSituation())){
			borrowManinfo.setDebtSituation(hjhPlanAsset.getDebtSituation());
		}else{
			borrowManinfo.setDebtSituation("无");
		}
		//(个人)其他平台借款情况
		if(StringUtils.isNotEmpty(hjhPlanAsset.getOtherBorrowed())){
			borrowManinfo.setOtherBorrowed(hjhPlanAsset.getOtherBorrowed());
		}else{
			borrowManinfo.setOtherBorrowed("暂无数据");
		}
		//(个人)借款资金运用情况
		if(StringUtils.isNotEmpty(hjhPlanAsset.getIsFunds())){
			borrowManinfo.setIsFunds(hjhPlanAsset.getIsFunds());
		}else{
			borrowManinfo.setIsFunds("正常");
		}
		//(个人)借款方经营状况及财务状况
		if(StringUtils.isNotEmpty(hjhPlanAsset.getIsManaged())){
			borrowManinfo.setIsManaged(hjhPlanAsset.getIsManaged());
		}else{
			borrowManinfo.setIsManaged("正常");
		}
		//(个人)借款方还款能力变化情况
		if(StringUtils.isNotEmpty(hjhPlanAsset.getIsAbility())){
			borrowManinfo.setIsAbility(hjhPlanAsset.getIsAbility());
		}else{
			borrowManinfo.setIsAbility("正常");
		}
		//(个人)借款方逾期情况
		if(StringUtils.isNotEmpty(hjhPlanAsset.getIsOverdue())){
			borrowManinfo.setIsOverdue(hjhPlanAsset.getIsOverdue());
		}else{
			borrowManinfo.setIsOverdue("暂无");
		}
		//(个人)借款方涉诉情况
		if(StringUtils.isNotEmpty(hjhPlanAsset.getIsComplaint())){
			borrowManinfo.setIsComplaint(hjhPlanAsset.getIsComplaint());
		}else{
			borrowManinfo.setIsComplaint("暂无");
		}
		//(个人)借款方受行政处罚情况
		if(StringUtils.isNotEmpty(hjhPlanAsset.getIsPunished())){
			borrowManinfo.setIsPunished(hjhPlanAsset.getIsPunished());
		}else{
			borrowManinfo.setIsPunished("暂无");
		}
		// add by nxl 20180723 互金系统添加借款人地址 Start
		if(StringUtils.isNotBlank(hjhPlanAsset.getAddress())){
			borrowManinfo.setAddress(hjhPlanAsset.getAddress());
		}
		// add by nxl 20180723 互金系统添加借款人地址 End
		this.borrowManinfoMapper.insertSelective(borrowManinfo);
		
		return 0;
	}

	/**
	 * 企业信息
	 * @param borrowNid
	 * @param hjhPlanAsset
	 * @param borrow
	 * @return
	 */
	public int insertBorrowCompanyManinfo(String borrowNid, HjhPlanAsset hjhPlanAsset, BorrowWithBLOBs borrow) {
			_log.info("插入企业信息",JSONObject.toJSON(hjhPlanAsset));
			// 公司信息
			BorrowUsers borrowUsers = new BorrowUsers();

			borrowUsers.setBorrowNid(borrowNid);
			borrowUsers.setBorrowPreNid(borrow.getBorrowPreNid());

			if (StringUtils.isNotEmpty(hjhPlanAsset.getBorrowCompanyName())) {
				borrowUsers.setUsername(hjhPlanAsset.getBorrowCompanyName());
			} else {
				borrowUsers.setUsername(StringUtils.EMPTY);
			}
			//注册资本货币单位 	元-美元
			borrowUsers.setCurrencyName("元");

			if (StringUtils.isNotEmpty(hjhPlanAsset.getRegisteredCapital())) {
				borrowUsers.setRegCaptial(hjhPlanAsset.getRegisteredCapital());
			} else {
				borrowUsers.setRegCaptial(StringUtils.EMPTY);
			}

			if (StringUtils.isNotEmpty(hjhPlanAsset.getIndustryInvolved())) {
				borrowUsers.setIndustry(hjhPlanAsset.getIndustryInvolved());
			} else {
				borrowUsers.setIndustry(StringUtils.EMPTY);
			}

			if (StringUtils.isNotEmpty(hjhPlanAsset.getIsComplaint())) {
				borrowUsers.setLitigation(hjhPlanAsset.getIsComplaint());
			} else {
				borrowUsers.setLitigation(StringUtils.EMPTY);
			}

			if (StringUtils.isNotEmpty(hjhPlanAsset.getOverdueReport())) {
				borrowUsers.setCreReport(hjhPlanAsset.getOverdueReport());
			} else {
				borrowUsers.setCreReport(StringUtils.EMPTY);
			}

			if (StringUtils.isNotEmpty(hjhPlanAsset.getRegistrationDate())) {
				borrowUsers.setComRegTime(hjhPlanAsset.getRegistrationDate());
			} else {
				borrowUsers.setComRegTime(StringUtils.EMPTY);
			}
			//统一社会信用代码
			if (StringUtils.isNotEmpty(hjhPlanAsset.getUnifiedSocialCreditCode())) {
				borrowUsers.setSocialCreditCode(hjhPlanAsset.getUnifiedSocialCreditCode());
			} else {
				borrowUsers.setSocialCreditCode(StringUtils.EMPTY);
			}
			//法人
			if (StringUtils.isNotEmpty(hjhPlanAsset.getLegalPerson())) {
				borrowUsers.setLegalPerson(hjhPlanAsset.getLegalPerson());
			} else {
				borrowUsers.setLegalPerson(StringUtils.EMPTY);
			}
			//主营业务
			if (StringUtils.isNotEmpty(hjhPlanAsset.getMainBusiness())) {
				borrowUsers.setMainBusiness(hjhPlanAsset.getMainBusiness());
			} else {
				borrowUsers.setMainBusiness(StringUtils.EMPTY);
			}
			//在平台逾期次数
			if (StringUtils.isNotEmpty(hjhPlanAsset.getOverdueTimes())) {
				borrowUsers.setOverdueTimes(hjhPlanAsset.getOverdueTimes());
			} else {
				borrowUsers.setOverdueTimes(StringUtils.EMPTY);
			}
			//在平台逾期金额
			if (StringUtils.isNotEmpty(hjhPlanAsset.getOverdueAmount())) {
				borrowUsers.setOverdueAmount(hjhPlanAsset.getOverdueAmount());
			} else {
				borrowUsers.setOverdueAmount(StringUtils.EMPTY);
			}

			//企贷审核信息  0未审核 1已审核
			//企业证件
			borrowUsers.setIsCertificate(1);

			if (StringUtils.isNotEmpty(hjhPlanAsset.getIsManaged())) {
				borrowUsers.setIsOperation(Integer.valueOf(1));
			} else {
				borrowUsers.setIsOperation(0);
			}
			if (StringUtils.isNotEmpty(hjhPlanAsset.getFinancialSituation())) {
				borrowUsers.setIsFinance(1);
			} else {
				borrowUsers.setIsFinance(0);
			}
			//企业信用
			borrowUsers.setIsEnterpriseCreidt(1);
			//法人信息
			borrowUsers.setIsLegalPerson(1);
			//资产状况
			borrowUsers.setIsAsset(1);
			//购销合同
			borrowUsers.setIsPurchaseContract(1);
			//供销合同
			borrowUsers.setIsSupplyContract(1);
			//征信报告逾期情况
			borrowUsers.setOverdueReport("暂未提供");
			//重大负债情况
			borrowUsers.setDebtSituation("无");
			//其他平台借款情况
			borrowUsers.setOtherBorrowed("无");
			//借款资金运用情况
			borrowUsers.setIsFunds("正常");
			//借款人经营状况及财务状况
			borrowUsers.setIsManaged("正常");
			//借款人还款能力变化情况
			borrowUsers.setIsAbility("正常");
			//借款人逾期情况
			borrowUsers.setIsOverdue("暂无");
			//借款人涉诉情况
			borrowUsers.setIsComplaint("暂无");
			//借款人受行政处罚情况
			borrowUsers.setIsPunished("暂无");
            // add by nxl 20180808 互金系统添加企业注册地址地址和企业注册编码 Start
            if (StringUtils.isNotBlank(hjhPlanAsset.getRegistrationAddress())) {
				borrowUsers.setRegistrationAddress(hjhPlanAsset.getRegistrationAddress());
            }
            if (StringUtils.isNotBlank(hjhPlanAsset.getCorporateCode())) {
				borrowUsers.setCorporateCode(hjhPlanAsset.getCorporateCode());
            }
            // add by nxl 20180808 互金系统添加企业注册地址地址和企业注册编码 End
			//添加操作
			return this.borrowUsersMapper.insertSelective(borrowUsers);
	}

	/**
	 * 公司信息
	 * 
	 * @param borrowNid
	 * @param borrowBean
	 * @param borrow
	 * @return
	 *//*
	public int insertBorrowUsers(String borrowNid, HjhPlanAsset hjhPlanAsset, BorrowWithBLOBs borrow) {
		BorrowUsersExample borrowUsersExample = new BorrowUsersExample();
		BorrowUsersExample.Criteria borrowUsersCra = borrowUsersExample.createCriteria();
		borrowUsersCra.andBorrowNidEqualTo(borrowNid);
		this.borrowUsersMapper.deleteByExample(borrowUsersExample);

		if (StringUtils.equals("1", hjhPlanAsset.getCompanyOrPersonal())) {
			// 公司信息
			BorrowUsers borrowUsers = new BorrowUsers();

			borrowUsers.setBorrowNid(borrowNid);
			borrowUsers.setBorrowPreNid(borrow.getBorrowPreNid());

			if (StringUtils.isNotEmpty(borrowBean.getComName())) {
				borrowUsers.setUsername(borrowBean.getComName());
			} else {
				borrowUsers.setUsername(StringUtils.EMPTY);
			}

			if (StringUtils.isNotEmpty(borrowBean.getComLocationProvince())) {
				borrowUsers.setProvince(borrowBean.getComLocationProvince());
			} else {
				borrowUsers.setProvince(StringUtils.EMPTY);
			}

			if (StringUtils.isNotEmpty(borrowBean.getComLocationCity())) {
				borrowUsers.setCity(borrowBean.getComLocationCity());
			} else {
				borrowUsers.setCity(StringUtils.EMPTY);
			}

			if (StringUtils.isNotEmpty(borrowBean.getComLocationArea())) {
				borrowUsers.setArea(borrowBean.getComLocationArea());
			} else {
				borrowUsers.setArea(StringUtils.EMPTY);
			}

			if (StringUtils.isNotEmpty(borrowBean.getComRegCaptial())) {
				borrowUsers.setRegCaptial(borrowBean.getComRegCaptial());
			} else {
				borrowUsers.setRegCaptial(StringUtils.EMPTY);
			}

			if (StringUtils.isNotEmpty(borrowBean.getComUserIndustry())) {
				borrowUsers.setIndustry(borrowBean.getComUserIndustry());
			} else {
				borrowUsers.setIndustry(StringUtils.EMPTY);
			}

			if (StringUtils.isNotEmpty(borrowBean.getComLitigation())) {
				borrowUsers.setLitigation(borrowBean.getComLitigation());
			} else {
				borrowUsers.setLitigation(StringUtils.EMPTY);
			}

			if (StringUtils.isNotEmpty(borrowBean.getComCreReport())) {
				borrowUsers.setCreReport(borrowBean.getComCreReport());
			} else {
				borrowUsers.setCreReport(StringUtils.EMPTY);
			}

			if (StringUtils.isNotEmpty(borrowBean.getComCredit())) {
				borrowUsers.setCredit(Integer.valueOf(borrowBean.getComCredit()));
			} else {
				borrowUsers.setCredit(0);
			}

			if (StringUtils.isNotEmpty(borrowBean.getComStaff())) {
				borrowUsers.setStaff(Integer.valueOf(borrowBean.getComStaff()));
			} else {
				borrowUsers.setStaff(0);
			}

			if (StringUtils.isNotEmpty(borrowBean.getComOtherInfo())) {
				borrowUsers.setOtherInfo(borrowBean.getComOtherInfo());
			} else {
				borrowUsers.setOtherInfo(StringUtils.EMPTY);
			}

			if (StringUtils.isNotEmpty(borrowBean.getComRegTime())) {
				borrowUsers.setComRegTime(borrowBean.getComRegTime());
			} else {
				borrowUsers.setComRegTime(StringUtils.EMPTY);
			}
			//统一社会信用代码
			if (StringUtils.isNotEmpty(borrowBean.getComSocialCreditCode())) {
				borrowUsers.setSocialCreditCode(borrowBean.getComSocialCreditCode());
			} else {
				borrowUsers.setSocialCreditCode(StringUtils.EMPTY);
			}
			//法人
			if (StringUtils.isNotEmpty(borrowBean.getComLegalPerson())) {
				borrowUsers.setLegalPerson(borrowBean.getComLegalPerson());
			} else {
				borrowUsers.setLegalPerson(StringUtils.EMPTY);
			}
			//注册号
			if (StringUtils.isNotEmpty(borrowBean.getComRegistCode())) {
				borrowUsers.setRegistCode(borrowBean.getComRegistCode());
			} else {
				borrowUsers.setRegistCode(StringUtils.EMPTY);
			}
			//主营业务
			if (StringUtils.isNotEmpty(borrowBean.getComMainBusiness())) {
				borrowUsers.setMainBusiness(borrowBean.getComMainBusiness());
			} else {
				borrowUsers.setMainBusiness(StringUtils.EMPTY);
			}
			//在平台逾期次数
			if (StringUtils.isNotEmpty(borrowBean.getComOverdueTimes())) {
				borrowUsers.setOverdueTimes(borrowBean.getComOverdueTimes());
			} else {
				borrowUsers.setOverdueTimes(StringUtils.EMPTY);
			}
			//在平台逾期金额
			if (StringUtils.isNotEmpty(borrowBean.getComOverdueAmount())) {
				borrowUsers.setOverdueAmount(borrowBean.getComOverdueAmount());
			} else {
				borrowUsers.setOverdueAmount(StringUtils.EMPTY);
			}
			//企贷审核信息  0未审核 1已审核
			if (StringUtils.isNotEmpty(borrowBean.getComIsCertificate())) {
				borrowUsers.setIsCertificate(Integer.valueOf(borrowBean.getComIsCertificate()));
			} else {
				borrowUsers.setIsCertificate(0);
			}

			if (StringUtils.isNotEmpty(borrowBean.getComIsOperation())) {
				borrowUsers.setIsOperation(Integer.valueOf(borrowBean.getComIsOperation()));
			} else {
				borrowUsers.setIsOperation(0);
			}
			if (StringUtils.isNotEmpty(borrowBean.getComIsFinance())) {
				borrowUsers.setIsFinance(Integer.valueOf(borrowBean.getComIsFinance()));
			} else {
				borrowUsers.setIsFinance(0);
			}
			if (StringUtils.isNotEmpty(borrowBean.getComIsEnterpriseCreidt())) {
				borrowUsers.setIsEnterpriseCreidt(Integer.valueOf(borrowBean.getComIsEnterpriseCreidt()));
			} else {
				borrowUsers.setIsEnterpriseCreidt(0);
			}
			if (StringUtils.isNotEmpty(borrowBean.getComIsLegalPerson())) {
				borrowUsers.setIsLegalPerson(Integer.valueOf(borrowBean.getComIsLegalPerson()));
			} else {
				borrowUsers.setIsLegalPerson(0);
			}
			if (StringUtils.isNotEmpty(borrowBean.getComIsAsset())) {
				borrowUsers.setIsAsset(Integer.valueOf(borrowBean.getComIsAsset()));
			} else {
				borrowUsers.setIsAsset(0);
			}
			if (StringUtils.isNotEmpty(borrowBean.getComIsPurchaseContract())) {
				borrowUsers.setIsPurchaseContract(Integer.valueOf(borrowBean.getComIsPurchaseContract()));
			} else {
				borrowUsers.setIsPurchaseContract(0);
			}
			if (StringUtils.isNotEmpty(borrowBean.getComIsSupplyContract())) {
				borrowUsers.setIsSupplyContract(Integer.valueOf(borrowBean.getComIsSupplyContract()));
			} else {
				borrowUsers.setIsSupplyContract(0);
			}
			this.borrowUsersMapper.insertSelective(borrowUsers);
		}

		return 0;
	}

	*//**
	 * 车辆信息
	 * 
	 * @param borrowNid
	 * @param borrowBean
	 * @param borrow
	 * @return
	 *//*
	private int insertBorrowCarinfo(String borrowNid, HjhPlanAsset hjhPlanAsset, BorrowWithBLOBs borrow) {
		BorrowCarinfoExample borrowCarinfoExample = new BorrowCarinfoExample();
		BorrowCarinfoExample.Criteria borrowCarinfoCra = borrowCarinfoExample.createCriteria();
		borrowCarinfoCra.andBorrowNidEqualTo(borrowNid);
		this.borrowCarinfoMapper.deleteByExample(borrowCarinfoExample);

		if (StringUtils.equals("2", borrowBean.getTypeCar())) {
			List<BorrowCommonCar> borrowCommonCarList = borrowBean.getBorrowCarinfoList();
			if (borrowCommonCarList != null && borrowCommonCarList.size() > 0) {
				for (BorrowCommonCar borrowCommonCar : borrowCommonCarList) {
					BorrowCarinfo borrowCarinfo = new BorrowCarinfo();
					// 品牌
					if (StringUtils.isNotEmpty(borrowCommonCar.getBrand())) {
						borrowCarinfo.setBrand(borrowCommonCar.getBrand());
					} else {
						borrowCarinfo.setBrand(StringUtils.EMPTY);
					}

					// 型号
					if (StringUtils.isNotEmpty(borrowCommonCar.getModel())) {
						borrowCarinfo.setModel(borrowCommonCar.getModel());
					} else {
						borrowCarinfo.setModel(StringUtils.EMPTY);
					}

					// 车系
					if (StringUtils.isNotEmpty(borrowCommonCar.getCarseries())) {
						borrowCarinfo.setCarseries(borrowCommonCar.getCarseries());
					} else {
						borrowCarinfo.setCarseries(StringUtils.EMPTY);
					}

					// 颜色
					if (StringUtils.isNotEmpty(borrowCommonCar.getColor())) {
						borrowCarinfo.setColor(borrowCommonCar.getColor());
					} else {
						borrowCarinfo.setColor(StringUtils.EMPTY);
					}

					// 出厂年份
					if (StringUtils.isNotEmpty(borrowCommonCar.getYear())) {
						borrowCarinfo.setYear(borrowCommonCar.getYear());
					} else {
						borrowCarinfo.setYear(StringUtils.EMPTY);
					}

					// 产地
					if (StringUtils.isNotEmpty(borrowCommonCar.getPlace())) {
						borrowCarinfo.setPlace(borrowCommonCar.getPlace());
					} else {
						borrowCarinfo.setPlace(StringUtils.EMPTY);
					}

					// 购买日期
					if (StringUtils.isNotEmpty(borrowCommonCar.getBuytime())) {
						borrowCarinfo.setBuytime(Integer.valueOf(String.valueOf(GetDate.str2Timestamp(borrowCommonCar.getBuytime()).getTime() / 1000)));
					} else {
						borrowCarinfo.setBuytime(0);
					}
					// 1有保险2无保险
					if (StringUtils.isNotEmpty(borrowCommonCar.getIsSafe())) {
						borrowCarinfo.setIsSafe(Integer.valueOf(borrowCommonCar.getIsSafe()));
					} else {
						borrowCarinfo.setIsSafe(0);
					}

					// 购买价
					if (StringUtils.isNotEmpty(borrowCommonCar.getPrice())) {
						borrowCarinfo.setPrice(new BigDecimal(borrowCommonCar.getPrice()));
					} else {
						borrowCarinfo.setPrice(BigDecimal.ZERO);
					}
					// 评估价
					if (StringUtils.isNotEmpty(borrowCommonCar.getToprice())) {
						borrowCarinfo.setToprice(new BigDecimal(borrowCommonCar.getToprice()));
					} else {
						borrowCarinfo.setToprice(BigDecimal.ZERO);
					}

					// 车牌号
					if (StringUtils.isNotEmpty(borrowCommonCar.getNumber())) {
						borrowCarinfo.setNumber(borrowCommonCar.getNumber());
					} else {
						borrowCarinfo.setNumber(StringUtils.EMPTY);
					}
					// 车辆登记地
					if (StringUtils.isNotEmpty(borrowCommonCar.getRegistration())) {
						borrowCarinfo.setRegistration(borrowCommonCar.getRegistration());
					} else {
						borrowCarinfo.setRegistration(StringUtils.EMPTY);
					}
					// 车架号
					if (StringUtils.isNotEmpty(borrowCommonCar.getVin())) {
						borrowCarinfo.setVin(borrowCommonCar.getVin());
					} else {
						borrowCarinfo.setVin(StringUtils.EMPTY);
					}
					
					borrowCarinfo.setBorrowNid(borrowNid);
					borrowCarinfo.setBorrowPreNid(borrow.getBorrowPreNid());

					this.borrowCarinfoMapper.insertSelective(borrowCarinfo);
				}
			}
		}

		return 0;
	}
	*//**
	 * 房产信息
	 * 
	 * @param borrowNid
	 * @param borrowBean
	 * @param borrow
	 * @return
	 *//*
	private int insertBorrowHouses(String borrowNid, HjhPlanAsset hjhPlanAsset, BorrowWithBLOBs borrow) {
		if (StringUtils.equals("1", borrowBean.getTypeHouse())) {
			BorrowHousesExample borrowHousesExample = new BorrowHousesExample();
			BorrowHousesExample.Criteria borrowHousesCra = borrowHousesExample.createCriteria();
			borrowHousesCra.andBorrowNidEqualTo(borrowNid);
			this.borrowHousesMapper.deleteByExample(borrowHousesExample);
			List<BorrowHouses> borrowHousesList = borrowBean.getBorrowHousesList();
			if (borrowHousesList != null && borrowHousesList.size() > 0) {
				for (BorrowHouses borrowHouses : borrowHousesList) {
					borrowHouses.setBorrowNid(borrowNid);
					borrowHouses.setBorrowPreNid(borrow.getBorrowPreNid());
					this.borrowHousesMapper.insertSelective(borrowHouses);
				}
			}
		}
		return 0;
	}

	*//**
	 * 认证信息
	 * 
	 * @param borrowNid
	 * @param borrowBean
	 * @param borrow
	 * @return
	 *//*
	private int insertBorrowCompanyAuthen(String borrowNid, HjhPlanAsset hjhPlanAsset, BorrowWithBLOBs borrow) {
		BorrowCompanyAuthenExample borrowCompanyAuthenExample = new BorrowCompanyAuthenExample();
		BorrowCompanyAuthenExample.Criteria borrowCompanyAuthenCra = borrowCompanyAuthenExample.createCriteria();
		borrowCompanyAuthenCra.andBorrowNidEqualTo(borrowNid);
		this.borrowCompanyAuthenMapper.deleteByExample(borrowCompanyAuthenExample);
		List<BorrowCommonCompanyAuthen> borrowCommonCompanyAuthenList = hjhPlanAsset.getBorrowCommonCompanyAuthenList();
		if (borrowCommonCompanyAuthenList != null && borrowCommonCompanyAuthenList.size() > 0) {
			for (BorrowCommonCompanyAuthen borrowCommonCompanyAuthen : borrowCommonCompanyAuthenList) {
				BorrowCompanyAuthen borrowCompanyAuthen = new BorrowCompanyAuthen();
				borrowCompanyAuthen.setAuthenName(borrowCommonCompanyAuthen.getAuthenName());
				borrowCompanyAuthen.setAuthenSortKey(Integer.valueOf(borrowCommonCompanyAuthen.getAuthenSortKey()));
				borrowCompanyAuthen.setAuthenTime(borrowCommonCompanyAuthen.getAuthenTime());
				borrowCompanyAuthen.setBorrowNid(borrowNid);
				borrowCompanyAuthen.setBorrowPreNid(borrow.getBorrowPreNid());
				this.borrowCompanyAuthenMapper.insertSelective(borrowCompanyAuthen);
			}
		}
		return 0;
	}*/

	

	/**
	 * 发标，更新状态
	 * 
	 * @param record
	 */
	/*private void updateOntimeRecord(HjhPlanAsset hjhPlanAsset) {
		// 插入时间
		int systemNowDateLong = GetDate.getNowTime10();
		Date systemNowDate = GetDate.getDate(systemNowDateLong);
		String borrowNid = hjhPlanAsset.getBorrowNid();
		if (StringUtils.isNotEmpty(borrowNid)) {
			BorrowExample borrowExample = new BorrowExample();
			BorrowExample.Criteria borrowCra = borrowExample.createCriteria();
			borrowCra.andBorrowNidEqualTo(borrowNid);
			List<BorrowWithBLOBs> borrowList = this.borrowMapper.selectByExampleWithBLOBs(borrowExample);
			if (borrowList != null && borrowList.size() == 1) {
				BorrowWithBLOBs borrow = borrowList.get(0);
				// 剩余的金额
				borrow.setBorrowAccountWait(borrow.getAccount());
				int time = systemNowDateLong;
				// 当发标状态为立即发标时插入系统时间
				if (borrowBean.getVerifyStatus() != null && StringUtils.isNotEmpty(borrowBean.getVerifyStatus())) {
					// 发标方式为”暂不发标2“或者”定时发标 3“时，项目状态变为”待发布“
					if (Integer.valueOf(borrowBean.getVerifyStatus()) == 2 || Integer.valueOf(borrowBean.getVerifyStatus()) == 3) {
						// 定时发标
						if (Integer.valueOf(borrowBean.getVerifyStatus()) == 3) {
							// 发标时间
							borrow.setOntime(GetDate.strYYYYMMDDHHMMSS2Timestamp(borrowBean.getOntime()));
						} else if (Integer.valueOf(borrowBean.getVerifyStatus()) == 3) {
							// 发标时间
							borrow.setOntime(0);
						}
						// 状态
						borrow.setStatus(1);
						// 初审状态
						borrow.setVerifyStatus(Integer.valueOf(borrowBean.getVerifyStatus()));
					}
					// 发标方式为”立即发标 2“时，项目状态变为”出借中
					else if (Integer.valueOf(borrowBean.getVerifyStatus()) == 4) {
						// 是否可以进行借款
						borrow.setBorrowStatus(1);
						// 初审时间
						borrow.setVerifyTime(String.valueOf(GetDate.getNowTime10()));
						// 发标的状态
						borrow.setVerifyStatus(Integer.valueOf(borrowBean.getVerifyStatus()));
						// 状态
						borrow.setStatus(2);
						// 借款到期时间
						borrow.setBorrowEndTime(String.valueOf(time + borrow.getBorrowValidTime() * 86400));
						// borrowNid，借款的borrowNid,account借款总额
						RedisUtils.set(borrowNid, borrow.getAccount().toString());
					}
					// 更新时间
					borrow.setUpdatetime(systemNowDate);
					this.borrowMapper.updateByExampleSelective(borrow, borrowExample);
				}
			}
		}
	}*/

	/**
	 * 获取平台项目编号信息
	 * @param borrowCd
	 */
	private BorrowProjectType getProjectType(String borrowCd) {
		BorrowProjectType borrowProjectType = null;
		BorrowProjectTypeExample example = new BorrowProjectTypeExample();
		BorrowProjectTypeExample.Criteria cra = example.createCriteria();
		cra.andBorrowCdEqualTo(borrowCd);
		cra.andStatusEqualTo("0");
		
		
		List<BorrowProjectType> projectTypes = this.borrowProjectTypeMapper.selectByExample(example);
		if(projectTypes != null && projectTypes.size() ==1){
			borrowProjectType = projectTypes.get(0);
		}
		
		return borrowProjectType;
	}


	/**
	 * 项目类型
	 * 
	 * @return
	 * @author Administrator
	 */
	private String getBorrowProjectClass(String borrowCd) {
		BorrowProjectTypeExample example = new BorrowProjectTypeExample();
		BorrowProjectTypeExample.Criteria cra = example.createCriteria();
		cra.andStatusEqualTo(CustomConstants.FLAG_NORMAL);
		cra.andBorrowCdEqualTo(borrowCd);

		List<BorrowProjectType> list = this.borrowProjectTypeMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0).getBorrowClass();
		}
		return "";
	}

	/**
	 * 获取融资服务费率
	 * 
	 * @param projectType
	 * 
	 * @param borrowStyle
	 * @param borrowPeriod
	 * @return
	 */
	private String getBorrowServiceScale(String projectType, String borrowStyle, Integer borrowPeriod) {
		BorrowFinserChargeExample example = new BorrowFinserChargeExample();
		BorrowFinserChargeExample.Criteria cra = example.createCriteria();
		if ("endday".equals(borrowStyle)) {
			cra.andChargeTimeTypeEqualTo("endday");
			cra.andChargeTimeEqualTo(borrowPeriod);
		} else {
			cra.andChargeTimeEqualTo(borrowPeriod);
			cra.andChargeTimeTypeNotEqualTo("endday");
		}
		cra.andProjectTypeEqualTo(Integer.valueOf(projectType));
		cra.andStatusEqualTo(0);

		List<BorrowFinserCharge> borrowFinserChargeList = borrowFinserChargeMapper.selectByExample(example);

		if (borrowFinserChargeList != null && borrowFinserChargeList.size() > 0) {
			return borrowFinserChargeList.get(0).getChargeRate();
		}

		return "";

	}

	/**
	 * 账户管理费率
	 * 
	 * @param projectType
	 * @param projectType
	 * @return
	 */
	private String getBorrowManagerScale(String projectType, String borrowStyle, Integer borrowPeriod) {
		BorrowFinmanChargeExample example = new BorrowFinmanChargeExample();
		BorrowFinmanChargeExample.Criteria cra = example.createCriteria();
		if ("endday".equals(borrowStyle)) {
			cra.andChargeTimeTypeEqualTo("endday");
		} else {
			cra.andChargeTimeTypeNotEqualTo("endday");
		}
		cra.andStatusEqualTo(0);

		List<BorrowFinmanCharge> borrowFinserChargeList = borrowFinmanChargeMapper.selectByExample(example);
		// TODO check fee table
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("borrowPeriod", borrowPeriod);
		params.put("borrowStyle", borrowStyle);
		params.put("projectType", projectType);
		Map<String, Object> feemap = borrowFullCustomizeMapper.selectFeeMapByParams(params);
		String man_charge_rate = "";
		if (feemap != null && !feemap.isEmpty()) {
			man_charge_rate = feemap.get("man_charge_rate") + "";
		}

		if (projectType != null && !projectType.equals("8")) {
			return man_charge_rate;
		} else {
			if (borrowFinserChargeList != null && borrowFinserChargeList.size() > 0) {
				return borrowFinserChargeList.get(0).getManChargePer();
			} else {
				return "";
			}
		}
	}

	/**
	 * 账户管理费率(最低，最高)
	 * 
	 * @param projectType
	 * @param borrowStyle
	 * @return
	 */
	private JSONObject getBorrowManagerScale(String projectType, String borrowStyle, Integer borrowPeriod, JSONObject jsonObject) {
		BorrowFinhxfmanChargeExample example = new BorrowFinhxfmanChargeExample();
		BorrowFinhxfmanChargeExample.Criteria cra = example.createCriteria();
		if ("endday".equals(borrowStyle)) {
			cra.andChargeTimeTypeEqualTo("endday");
		} else {
			cra.andChargeTimeEqualTo(borrowPeriod);
			cra.andChargeTimeTypeNotEqualTo("endday");
		}
		cra.andStatusEqualTo(0);

		List<BorrowFinhxfmanCharge> borrowFinhxfmanChargeList = borrowFinhxfmanChargeMapper.selectByExample(example);
		if (borrowFinhxfmanChargeList != null && borrowFinhxfmanChargeList.size() > 0) {
			jsonObject.put("borrowManagerScale", borrowFinhxfmanChargeList.get(0).getManChargePer());
			jsonObject.put("borrowManagerScaleEnd", borrowFinhxfmanChargeList.get(0).getManChargePerEnd());
		} else {
			jsonObject.put("borrowManagerScale", "");
			jsonObject.put("borrowManagerScaleEnd", "");
		}

		return jsonObject;
	}

	/**
	 * 收益差率
	 * 
	 * @param projectType
	 * @param borrowStyle
	 * @return
	 */
	private String getBorrowReturnScale(String projectType, String borrowStyle, Integer borrowPeriod) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("borrowPeriod", borrowPeriod);
		params.put("borrowStyle", borrowStyle);
		params.put("projectType", projectType);
		Map<String, Object> feemap = borrowFullCustomizeMapper.selectFeeMapByParams(params);
		String differential_rate = "";
		if (feemap != null && !feemap.isEmpty()) {
			differential_rate = feemap.get("return_rate") + "";
		}
		if (projectType != null && !projectType.equals("8")) {
			return differential_rate;
		} else {
			return "";
		}
	}

	private boolean updateBorrowRegist(BorrowWithBLOBs borrow, int status, int registStatus) {
		Date nowDate = new Date();
		AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
		BorrowExample example = new BorrowExample();
		example.createCriteria().andIdEqualTo(borrow.getId()).andStatusEqualTo(borrow.getStatus()).andRegistStatusEqualTo(borrow.getRegistStatus());
		borrow.setRegistStatus(registStatus);
		borrow.setStatus(status);
		borrow.setRegistUserId(Integer.parseInt(adminSystem.getId()));
		borrow.setRegistUserName(adminSystem.getUsername());
		borrow.setRegistTime(nowDate);
		boolean flag = this.borrowMapper.updateByExampleSelective(borrow, example) > 0 ? true : false;
		return flag;
	}

	/**
	 * 算募集时间
	 * @param rasieStartDate
	 * @param bankRegistDays
	 * @param borrowValidTime
	 * @return
	 */
	private String getBankRaiseEndDate(String rasieStartDate, Integer bankRegistDays, Integer borrowValidTime) {
		Integer raiseStartdate = GetDate.strYYYYMMDD3Timestamp3(rasieStartDate);
		int raistEndDate = raiseStartdate + bankRegistDays * 60 * 60 * 24 + borrowValidTime * 60 * 60 * 24;
		return GetDate.getDateMyTimeInMillisYYYYMMDD(raistEndDate);
	}

	/**
	 * 借款预编码，八位
	 * 
	 * @return
	 */
	private String getBorrowPreNid() {
		String yyyymm = GetDate.getServerDateTime(13, new Date());
		String mmdd = yyyymm.substring(2);
//		String borrowPreNid = this.borrowCustomizeMapper.getBorrowPreNid(mmdd);
		String borrowPreNid = StringUtils.EMPTY;
		if (StringUtils.isEmpty(borrowPreNid)) {
			return mmdd + "00000001";
		}
		if (borrowPreNid.length() == 8) {
			return mmdd + "00000001";
		}
		return String.valueOf(Long.valueOf(borrowPreNid) + 1);
	}

	/**
	 * 获取对应计划
	 * 
	 * @return
	 */
	private HjhPlan selectPlanByPeriod(int period) {
		HjhPlanExample example = new HjhPlanExample();
		HjhPlanExample.Criteria cra = example.createCriteria();
		cra.andLockPeriodEqualTo(period);
		cra.andDelFlgEqualTo(0);

		List<HjhPlan> list = this.hjhPlanMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
		
	}

	/**
	 * 获取资产项目类型
	 * 
	 * @return
	 */
	public HjhAssetBorrowType selectAssetBorrowType(HjhPlanAsset hjhPlanAsset) {
		HjhAssetBorrowTypeExample example = new HjhAssetBorrowTypeExample();
		HjhAssetBorrowTypeExample.Criteria cra = example.createCriteria();
		cra.andInstCodeEqualTo(hjhPlanAsset.getInstCode());
		cra.andAssetTypeEqualTo(hjhPlanAsset.getAssetType());
		cra.andIsOpenEqualTo(1);

		List<HjhAssetBorrowType> list = this.hjhAssetBorrowTypeMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		
		return null;
		
	}

	/**
	 * 根据项目类型，期限，获取借款利率
	 * 
	 * @return
	 */
	private BorrowFinmanNewCharge selectBorrowApr(String projectType,String instCode, Integer instProjectType, String borrowStyle,Integer chargetime) {
		BorrowFinmanNewChargeExample example = new BorrowFinmanNewChargeExample();
		BorrowFinmanNewChargeExample.Criteria cra = example.createCriteria();
		cra.andProjectTypeEqualTo(projectType);
		cra.andInstCodeEqualTo(instCode);
		cra.andAssetTypeEqualTo(instProjectType);
		cra.andManChargeTimeTypeEqualTo(borrowStyle);
		cra.andManChargeTimeEqualTo(chargetime);
		cra.andStatusEqualTo(0);

		_log.info("===========根据条件获取项目费率：projectType="+projectType+",instCode="+instCode+",assetType="
				+instProjectType+",manChargeTimeType="+borrowStyle+",manChargeTime="+chargetime);

		List<BorrowFinmanNewCharge> list = this.borrowFinmanNewChargeMapper.selectByExample(example);
		if (CollectionUtils.isNotEmpty(list)) {
			return list.get(0);
		}
		
		return null;
		
	}
	
}
