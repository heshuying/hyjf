/**
 * Description:用户出借实现类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 郭勇
 * @version: 1.0
 * Created at: 2015年12月4日 下午1:50:02
 * Modification History:
 * Modified by :
 */

package com.hyjf.web.user.invest;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.user.pandect.PandectService;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.calculate.DateUtils;
import com.hyjf.common.calculate.FinancingServiceChargeUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.AccountChinapnrExample;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AccountLog;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.auto.AppChannelStatisticsDetail;
import com.hyjf.mybatis.model.auto.AppChannelStatisticsDetailExample;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowAppoint;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.auto.BorrowProjectTypeExample;
import com.hyjf.mybatis.model.auto.BorrowSendType;
import com.hyjf.mybatis.model.auto.BorrowSendTypeExample;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.BorrowStyleExample;
import com.hyjf.mybatis.model.auto.BorrowTender;
import com.hyjf.mybatis.model.auto.BorrowTenderExample;
import com.hyjf.mybatis.model.auto.BorrowTenderTmp;
import com.hyjf.mybatis.model.auto.BorrowTenderTmpExample;
import com.hyjf.mybatis.model.auto.BorrowTenderTmpInfo;
import com.hyjf.mybatis.model.auto.CalculateInvestInterest;
import com.hyjf.mybatis.model.auto.CalculateInvestInterestExample;
import com.hyjf.mybatis.model.auto.CouponRealTenderExample;
import com.hyjf.mybatis.model.auto.CouponUser;
import com.hyjf.mybatis.model.auto.CouponUserExample;
import com.hyjf.mybatis.model.auto.FreezeList;
import com.hyjf.mybatis.model.auto.IncreaseInterestInvest;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.SpreadsUsersExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.mybatis.model.auto.UtmReg;
import com.hyjf.mybatis.model.auto.UtmRegExample;
import com.hyjf.mybatis.model.auto.VipUserTender;
import com.hyjf.mybatis.model.customize.EmployeeCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigCustomizeV2;
import com.hyjf.mybatis.model.customize.web.WebProjectDetailCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.web.BaseServiceImpl;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

@Service
public class InvestServiceImpl extends BaseServiceImpl implements InvestService {

	private static String HOST = PropUtils.getSystem("hyjf.web.host").trim();
	@Autowired
	PandectService pandectService;
	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;
	@Autowired
	private PlatformTransactionManager transactionManager;

	@Autowired
	private TransactionDefinition transactionDefinition;

	public static JedisPool pool = RedisUtils.getPool();

	/**
	 * 
	 * 出借预插入
	 * 
	 * @param borrowNid
	 * @param OrdId
	 * @param userId
	 * @param account
	 * @param ip
	 * @return
	 * @author Administrator
	 */
	@Override
	public Boolean updateBeforeChinaPnR(String borrowNid, String OrdId, Integer userId, String account, String ip) {

		BorrowTenderTmp borrowTender = new BorrowTenderTmp();
		borrowTender.setUserId(userId);
		borrowTender.setBorrowNid(borrowNid);
		String nid = OrdId;
		borrowTender.setNid(nid);
		borrowTender.setAccount(new BigDecimal(account));
		borrowTender.setAddip(ip);
		borrowTender.setChangeStatus(0);
		borrowTender.setChangeUserid(0);
		borrowTender.setChangePeriod(0);
		borrowTender.setTenderStatus(0);
		borrowTender.setTenderNid(borrowNid);
		borrowTender.setTenderAwardAccount(new BigDecimal(0));
		borrowTender.setRecoverFullStatus(0);
		borrowTender.setRecoverFee(new BigDecimal(0));
		borrowTender.setRecoverType("");
		borrowTender.setRecoverAdvanceFee(new BigDecimal(0));
		borrowTender.setRecoverLateFee(new BigDecimal(0));
		borrowTender.setTenderAwardFee(new BigDecimal(0));
		borrowTender.setContents("");
		borrowTender.setAutoStatus(0);
		borrowTender.setWebStatus(0);
		borrowTender.setPeriodStatus(0);
		borrowTender.setWeb(0);
		Boolean flag1 = borrowTenderTmpMapper.insertSelective(borrowTender) > 0 ? true : false;

		BorrowTenderTmpInfo info = new BorrowTenderTmpInfo();
		info.setOrdid(nid);
		Map<String, String> map = new HashMap<String, String>();
		map.put("borrow_nid", borrowNid);
		map.put("user_id", userId + "");
		map.put("account", account + "");
		map.put("status", "0");
		map.put("nid", nid);
		map.put("addtime", (new Date().getTime() / 1000) + "");
		map.put("addip", ip);
		String array = JSON.toJSONString(map);
		info.setTmpArray(array);
		info.setAddtime((new Date().getTime() / 1000) + "");
		Boolean flag2 = borrowTenderTmpInfoMapper.insertSelective(info) > 0 ? true : false;
		return flag1 && flag2;
	}

	/**
	 * 
	 * 用户出借
	 * 
	 * @param borrow
	 * @param bean
	 * @return
	 * @author Administrator
	 */
	@Override
	public synchronized JSONObject userTender(Borrow borrow, ChinapnrBean bean) {

		JSONObject info = new JSONObject();
		// 操作ip
		String ip = bean.getLogIp();
		// 操作平台
		int client = StringUtils.isNotBlank(bean.getLogClient()) ? Integer.parseInt(bean.getLogClient()) : 0;
		// 获取出借用户的出借客户号
		String tenderUsrcustid = bean.getUsrCustId();
		// 出借人id
		Integer userId = bean.getLogUserId();
		// 冻结标示
		String freezeTrxId = bean.getFreezeTrxId();
		// 借款金额
		String account = bean.getTransAmt();
		// 订单id
		String orderId = bean.getOrdId();
		// 订单日期
		String orderDate = bean.getOrdDate();
		// 出借结果返回码
		String respCode = bean.getRespCode();
		// 项目编号
		String borrowNid = borrow.getBorrowNid();
		// 项目的还款方式
		String borrowStyle = borrow.getBorrowStyle();
		// 项目类型
		int projectType = borrow.getProjectType();
		// 服务费率
		BigDecimal serviceFeeRate = Validator.isNull(borrow.getServiceFeeRate()) ? BigDecimal.ZERO : new BigDecimal(
				borrow.getServiceFeeRate());
		// 借款期数
		Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();
		// 借款项目主键
		Integer borrowId = borrow.getId();
		// 剩余出借金额
		BigDecimal borrowAccountWait = borrow.getBorrowAccountWait();
		// 手动控制事务
		TransactionStatus txStatus = null;
		// 发送状态
		String status = ChinaPnrConstant.STATUS_FAIL;
		// 冻结前验证
		BigDecimal accountBigDecimal = new BigDecimal(account);
		Jedis jedis = pool.getResource();
		String accountRedisWait = RedisUtils.get(borrowNid);
		if (StringUtils.isNotBlank(accountRedisWait)) {
			// 操作redis
			while ("OK".equals(jedis.watch(borrowNid))) {
				accountRedisWait = RedisUtils.get(borrowNid);
				if (StringUtils.isNotBlank(accountRedisWait)) {
					System.out.println("PC用户:" + userId + "***冻结前可投金额：" + accountRedisWait);
					if (new BigDecimal(accountRedisWait).compareTo(BigDecimal.ZERO) == 0) {
						info.put("message", "您来晚了，下次再来抢吧！");
						info.put("status", status);
						break;
					} else {
						if (new BigDecimal(accountRedisWait).compareTo(accountBigDecimal) < 0) {
							info.put("message", "可投剩余金额为" + accountRedisWait + "元！");
							info.put("status", status);
							break;
						} else {
							Transaction tx = jedis.multi();
							BigDecimal lastAccount = new BigDecimal(accountRedisWait).subtract(accountBigDecimal);
							tx.set(borrowNid, lastAccount + "");
							List<Object> result = tx.exec();
							if (result == null || result.isEmpty()) {
								jedis.unwatch();
							} else {
								status = ChinaPnrConstant.STATUS_VERTIFY_OK;
								info.put("message", "redis操作成功！");
								info.put("status", status);
								System.out.println("PC用户:" + userId + "***冻结前减redis：" + accountBigDecimal);
								break;
							}
						}
					}
				} else {
					info.put("message", "您来晚了，下次再来抢吧！");
					info.put("status", status);
					break;
				}
			}
		} else {
			info.put("message", "您来晚了，下次再来抢吧！");
			info.put("status", status);
		}
		if (ChinaPnrConstant.STATUS_VERTIFY_OK.equals(status)) {
			try {
				// 开启事务
				txStatus = this.transactionManager.getTransaction(transactionDefinition);
				int nowTime = GetDate.getNowTime10();
				// 删除临时表
				BorrowTenderTmpExample borrowTenderTmpExample = new BorrowTenderTmpExample();
				BorrowTenderTmpExample.Criteria criteria1 = borrowTenderTmpExample.createCriteria();
				criteria1.andNidEqualTo(orderId);
				criteria1.andUserIdEqualTo(userId);
				criteria1.andBorrowNidEqualTo(borrowNid);
				boolean tenderTempFlag = borrowTenderTmpMapper.deleteByExample(borrowTenderTmpExample) > 0 ? true
						: false;
				if (tenderTempFlag) {
					System.out.println("用户:" + userId + "***********************************删除borrowTenderTmp，订单号："
							+ orderId);
					// 插入冻结表
					FreezeList record = new FreezeList();
					record.setAmount(new BigDecimal(account));
					record.setBorrowNid(borrowNid);
					record.setCreateTime(nowTime);
					record.setOrdid(orderId);
					record.setUserId(userId);
					record.setRespcode(respCode);
					record.setTrxid(freezeTrxId);
					record.setOrdid(orderId);
					record.setUsrcustid(tenderUsrcustid);
					record.setXfrom(1);
					record.setStatus(0);
					record.setUnfreezeManual(0);
					boolean freezeFlag = freezeListMapper.insertSelective(record) > 0 ? true : false;
					if (freezeFlag) {
						System.out.println("用户:" + userId + "***********************************插入FreezeList，订单号："
								+ orderId);
						BigDecimal accountDecimal = new BigDecimal(account);
						BigDecimal perService = new BigDecimal(0);
						if (StringUtils.isNotEmpty(borrowStyle)) {
							BigDecimal serviceScale = serviceFeeRate;
							// 到期还本还息end/先息后本endmonth/等额本息month/等额本金principal
							if (CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle)
									|| CustomConstants.BORROW_STYLE_END.equals(borrowStyle)
									|| CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
									|| CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle)) {
								perService = FinancingServiceChargeUtils.getMonthsPrincipalServiceCharge(
										accountDecimal, serviceScale);
							}
							// 按天计息到期还本还息
							else if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle)) {
								perService = FinancingServiceChargeUtils.getDaysPrincipalServiceCharge(accountDecimal,
										serviceScale, borrowPeriod);
							}
						}
						BorrowTender borrowTender = new BorrowTender();
						borrowTender.setAccount(accountDecimal);
						borrowTender.setAccountTender(new BigDecimal(0));
						borrowTender.setActivityFlag(0);//
						borrowTender.setAddip(ip);
						borrowTender.setAddtime(nowTime);
						borrowTender.setApiStatus(0);//
						borrowTender.setAutoStatus(0);//
						borrowTender.setBorrowNid(borrowNid);
						borrowTender.setChangePeriod(0);//
						borrowTender.setChangeUserid(0);
						borrowTender.setClient(0);
						borrowTender.setContents("");//
						borrowTender.setFlag(0);//
						borrowTender.setIsok(0);
						borrowTender.setIsReport(0);
						borrowTender.setChangeStatus(0);
						borrowTender.setLoanAmount(accountDecimal.subtract(perService));
						borrowTender.setNid(orderId);
						borrowTender.setOrderDate(orderDate);
						borrowTender.setPeriodStatus(0);//
						borrowTender.setRecoverAccountAll(new BigDecimal(0));//
						borrowTender.setRecoverAccountCapitalWait(new BigDecimal(0));//
						borrowTender.setRecoverAccountCapitalYes(new BigDecimal(0));
						borrowTender.setRecoverAccountInterest(new BigDecimal(0));
						borrowTender.setRecoverAccountInterestWait(new BigDecimal(0));
						borrowTender.setRecoverAccountInterestYes(new BigDecimal(0));
						borrowTender.setRecoverAccountWait(new BigDecimal(0));
						borrowTender.setRecoverAccountYes(new BigDecimal(0));
						borrowTender.setRecoverAdvanceFee(new BigDecimal(0));
						borrowTender.setRecoverFee(new BigDecimal(0));
						borrowTender.setRecoverFullStatus(0);
						borrowTender.setRecoverLateFee(new BigDecimal(0));
						borrowTender.setRecoverTimes(0);
						borrowTender.setRecoverType("");
						borrowTender.setStatus(0);
						borrowTender.setTenderAwardAccount(new BigDecimal(0));
						borrowTender.setTenderAwardFee(new BigDecimal(0));
						borrowTender.setTenderNid(borrowNid);
						borrowTender.setTenderStatus(0);
						borrowTender.setUserId(userId);
						// 出借人信息
						Users users = getUsers(userId);
						// add by zhangjp vip出借记录 start
						UsersInfo userInfo = null;
						// add by zhangjp vip出借记录 end
						if (users!=null&&projectType != 8) {
							// 更新渠道统计用户累计出借
							AppChannelStatisticsDetailExample channelExample = new AppChannelStatisticsDetailExample();
							AppChannelStatisticsDetailExample.Criteria crt = channelExample
									.createCriteria();
							crt.andUserIdEqualTo(userId);
							List<AppChannelStatisticsDetail> appChannelStatisticsDetails = this.appChannelStatisticsDetailMapper
									.selectByExample(channelExample);
							if (appChannelStatisticsDetails != null
									&& appChannelStatisticsDetails.size() == 1) {
								AppChannelStatisticsDetail channelDetail = appChannelStatisticsDetails
										.get(0);
								Map<String, Object> params = new HashMap<String, Object>();
								params.put("id", channelDetail.getId());
								params.put("accountDecimal", accountDecimal);
								// 出借时间
								params.put("investTime", nowTime);
								// 项目类型
								if(borrow.getProjectType()== 13){
									params.put("projectType","汇金理财");
								}else{
									params.put("projectType","汇直投");
								}
								// 首次投标项目期限
								String investProjectPeriod = "";
								if(borrow.getBorrowStyle().equals("endday")){
									investProjectPeriod =borrow.getBorrowPeriod() +"天";
								}else{
									investProjectPeriod =borrow.getBorrowPeriod() +"个月";
								}
								params.put("investProjectPeriod", investProjectPeriod);
								// 更新渠道统计用户累计出借
								if(users.getInvestflag() == 1){
									this.appChannelStatisticsDetailCustomizeMapper
											.updateAppChannelStatisticsDetail(params);
								}else if(users.getInvestflag() == 0){
									// 更新首投出借
									this.appChannelStatisticsDetailCustomizeMapper.updateFirstAppChannelStatisticsDetail(params);
								}
								System.out
								.println("用户:"
										+ userId
										+ "***********************************预更新渠道统计表AppChannelStatisticsDetail，订单号："
										+ orderId);
							}else{
								// 更新huiyingdai_utm_reg的首投信息
								UtmRegExample utmRegExample = new UtmRegExample();
								UtmRegExample.Criteria utmRegCra = utmRegExample.createCriteria();
								utmRegCra.andUserIdEqualTo(userId);
								List<UtmReg> utmRegList = this.utmRegMapper.selectByExample(utmRegExample);
								if(utmRegList!=null&&utmRegList.size()>0){
									UtmReg utmReg = utmRegList.get(0);
									Map<String, Object> params = new HashMap<String, Object>();
									params.put("id", utmReg.getId());
									params.put("accountDecimal", accountDecimal);
									// 出借时间
									params.put("investTime", nowTime);
									// 项目类型
									if (borrow.getProjectType() == 13) {
										params.put("projectType", "汇金理财");
									} else {
										params.put("projectType", "汇直投");
									}
									// 首次投标项目期限
									String investProjectPeriod = "";
									if(borrow.getBorrowStyle().equals("endday")){
										investProjectPeriod =borrow.getBorrowPeriod() +"天";
									}else{
										investProjectPeriod =borrow.getBorrowPeriod() +"个月";
									}
									params.put("investProjectPeriod", investProjectPeriod);
									
									// 更新渠道统计用户累计出借
									if(users.getInvestflag() == 0){
										// 更新huiyingdai_utm_reg的首投信息
										this.appChannelStatisticsDetailCustomizeMapper.updateFirstUtmReg(params);
									}
								}
							}
						}
						if (users != null) {
							if (users.getInvestflag() == 0) {
								users.setInvestflag(1);
								this.usersMapper.updateByPrimaryKeySelective(users);
							} else {
								if (projectType == 4) {
									status = ChinaPnrConstant.STATUS_FAIL;
									info.put("message", "该项目限投一笔！");
									info.put("status", status);
									this.recoverRedis(borrowNid, userId, account);
									// 回滚事务
									this.transactionManager.rollback(txStatus);
									return info;
								}
							}
							// 出借用户名
							borrowTender.setTenderUserName(users.getUsername());
							// 获取出借人属性
							// modify by zhangjp vip出借记录 start
							// UsersInfo userInfo = getUserInfo(userId);
							userInfo = getUserInfo(userId);
							// modify by zhangjp vip出借记录 end
							// 用户属性 0=>无主单 1=>有主单 2=>线下员工 3=>线上员工
							Integer attribute = null;
							if (userInfo != null) {
								// 获取出借用户的用户属性
								attribute = userInfo.getAttribute();
								if (attribute != null) {
									// 出借人用户属性
									borrowTender.setTenderUserAttribute(attribute);
									// 如果是线上员工或线下员工，推荐人的userId和username不插
									if (attribute == 2 || attribute == 3) {
										EmployeeCustomize employeeCustomize = employeeCustomizeMapper
												.selectEmployeeByUserId(userId);
										if (employeeCustomize != null) {
											borrowTender.setInviteRegionId(employeeCustomize.getRegionId());
											borrowTender.setInviteRegionName(employeeCustomize.getRegionName());
											borrowTender.setInviteBranchId(employeeCustomize.getBranchId());
											borrowTender.setInviteBranchName(employeeCustomize.getBranchName());
											borrowTender.setInviteDepartmentId(employeeCustomize.getDepartmentId());
											borrowTender.setInviteDepartmentName(employeeCustomize.getDepartmentName());
										}
									} else if (attribute == 1) {
										SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
										SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample
												.createCriteria();
										spreadsUsersExampleCriteria.andUserIdEqualTo(userId);
										List<SpreadsUsers> sList = spreadsUsersMapper
												.selectByExample(spreadsUsersExample);
										if (sList != null && sList.size() == 1) {
											int refUserId = sList.get(0).getSpreadsUserid();
											// 查找用户推荐人
											Users userss = getUsers(refUserId);
											if (userss != null) {
												borrowTender.setInviteUserId(userss.getUserId());
												borrowTender.setInviteUserName(userss.getUsername());
											}
											// 推荐人信息
											UsersInfo refUsers = getUserInfo(refUserId);
											// 推荐人用户属性
											if (refUsers != null) {
												borrowTender.setInviteUserAttribute(refUsers.getAttribute());
											}
											// 查找用户推荐人部门
											EmployeeCustomize employeeCustomize = employeeCustomizeMapper
													.selectEmployeeByUserId(refUserId);
											if (employeeCustomize != null) {
												borrowTender.setInviteRegionId(employeeCustomize.getRegionId());
												borrowTender.setInviteRegionName(employeeCustomize.getRegionName());
												borrowTender.setInviteBranchId(employeeCustomize.getBranchId());
												borrowTender.setInviteBranchName(employeeCustomize.getBranchName());
												borrowTender.setInviteDepartmentId(employeeCustomize.getDepartmentId());
												borrowTender.setInviteDepartmentName(employeeCustomize
														.getDepartmentName());
											}
										}
									} else if (attribute == 0) {
										SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
										SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample
												.createCriteria();
										spreadsUsersExampleCriteria.andUserIdEqualTo(userId);
										List<SpreadsUsers> sList = spreadsUsersMapper
												.selectByExample(spreadsUsersExample);
										if (sList != null && sList.size() == 1) {
											int refUserId = sList.get(0).getSpreadsUserid();
											// 查找推荐人
											Users userss = getUsers(refUserId);
											if (userss != null) {
												borrowTender.setInviteUserId(userss.getUserId());
												borrowTender.setInviteUserName(userss.getUsername());
											}
											// 推荐人信息
											UsersInfo refUsers = getUserInfo(refUserId);
											// 推荐人用户属性
											if (refUsers != null) {
												borrowTender.setInviteUserAttribute(refUsers.getAttribute());
											}
										}
									}
								}
							}
						}
						borrowTender.setWeb(0);
						borrowTender.setWebStatus(0);
						borrowTender.setClient(client);
						borrowTender.setInvestType(0);
						// 单笔出借的融资服务费
						borrowTender.setLoanFee(perService);
						// add by zhangjp vip出借记录 start
						borrowTender.setRemark("现金出借");
						// add by zhangjp vip出借记录 end
						boolean trenderFlag = borrowTenderMapper.insertSelective(borrowTender) > 0 ? true : false;
						if (trenderFlag) {
							System.out.println("用户:" + userId
									+ "***********************************插入borrowTender，订单号：" + orderId);
							// add by zhangjp vip出借记录 start
							if (userInfo != null && userInfo.getVipId() != null) {
								VipUserTender vt = new VipUserTender();
								// 出借用户编号
								vt.setUserId(userId);
								// 出借用户vip编号
								vt.setVipId(userInfo.getVipId());
								// 出借编号
								vt.setTenderNid(orderId);
								vt.setSumVipValue(userInfo.getVipValue());
								vt.setAddTime(nowTime);
								vt.setAddUser(String.valueOf(userId));
								vt.setUpdateTime(nowTime);
								vt.setUpdateUser(String.valueOf(userId));
								vt.setDelFlg(0);
								this.vipUserTenderMapper.insertSelective(vt);
							}
							// add by zhangjp vip出借记录 end

							// 新出借金额汇总
							AccountExample example = new AccountExample();
							AccountExample.Criteria criteria = example.createCriteria();
							criteria.andUserIdEqualTo(userId);
							List<Account> list = accountMapper.selectByExample(example);
							if (list != null && list.size() == 1) {
								// 总金额
								BigDecimal total = list.get(0).getTotal();
								// 冻结金额
								BigDecimal frost = list.get(0).getFrost();
								// 可用金额
								BigDecimal balance = list.get(0).getBalance();
								// 更新用户账户余额表
								Account accountBean = new Account();
								accountBean.setUserId(userId);
								accountBean.setFrost(accountDecimal);
								// 出借人可用余额
								accountBean.setBalance(accountDecimal);
								// 出借人待收金额
								accountBean.setInMoney(accountDecimal);
								Boolean accountFlag = this.adminAccountCustomizeMapper.updateOfTender(accountBean) > 0 ? true
										: false;
								// 插入account_list表
								if (accountFlag) {
									System.out.println("用户:" + userId
											+ "***********************************更新account，订单号：" + orderId);
									AccountList accountList = new AccountList();
									accountList.setAmount(accountDecimal);
									accountList.setAwait(new BigDecimal(0));
									accountList.setBalance(balance.subtract(accountDecimal));
									accountList.setBaseUpdate(0);
									accountList.setCreateTime(nowTime);
									accountList.setFrost(frost.add(accountDecimal));
									accountList.setInterest(new BigDecimal(0));
									accountList.setIp(ip);
									accountList.setIsUpdate(0);
									accountList.setNid(orderId);
									accountList.setOperator(userId + "");
									accountList.setRemark(borrowNid);
									accountList.setRepay(new BigDecimal(0));
									accountList.setTotal(total);
									accountList.setTrade("tender");// 出借
									accountList.setTradeCode("frost");// 投标冻结后为frost
									accountList.setType(3);// 收支类型1收入2支出3冻结
									accountList.setUserId(userId);
									accountList.setWeb(0);
									boolean accountListFlag = accountListMapper.insertSelective(accountList) > 0 ? true : false;
									if (accountListFlag) {
										System.out.println("用户:" + userId
												+ "***********************************插入accountList，订单号：" + orderId);
										// 更新borrow表
										Map<String, Object> borrowParam = new HashMap<String, Object>();
										borrowParam.put("borrowAccountYes", accountDecimal);
										borrowParam.put("borrowService", perService);
										borrowParam.put("borrowId", borrowId);
										boolean updateBorrowAccountFlag = borrowCustomizeMapper
												.updateOfBorrow(borrowParam) > 0 ? true : false;
										if (updateBorrowAccountFlag) {
											System.out.println("用户:" + userId
													+ "***********************************更新borrow表，订单号：" + orderId);
											List<CalculateInvestInterest> calculates = this.calculateInvestInterestMapper
													.selectByExample(new CalculateInvestInterestExample());
											if (calculates != null && calculates.size() > 0) {
												CalculateInvestInterest calculateNew = new CalculateInvestInterest();
												calculateNew.setTenderSum(accountDecimal);
												calculateNew.setId(calculates.get(0).getId());
												calculateNew.setCreateTime(GetDate.getDate(nowTime));
												this.webCalculateInvestInterestCustomizeMapper
														.updateCalculateInvestByPrimaryKey(calculateNew);
											}
											
											// 计算此时的剩余可出借金额
											BigDecimal accountWait = borrowAccountWait.subtract(accountDecimal);
											// 满标处理
											if (accountWait.compareTo(new BigDecimal(0)) == 0) {
												System.out.println("用户:" + userId
														+ "***********************************项目满标，订单号：" + orderId);
												Map<String, Object> borrowFull = new HashMap<String, Object>();
												borrowFull.put("borrowId", borrowId);
												boolean fullFlag = borrowCustomizeMapper.updateOfFullBorrow(borrowFull) > 0 ? true
														: false;
												if (fullFlag) {
													// 清除标总额的缓存
													RedisUtils.del(borrowNid);
													// 纯发短信接口
													Map<String, String> replaceMap = new HashMap<String, String>();
													replaceMap.put("val_title", borrowNid);
													replaceMap.put("val_date", DateUtils.getNowDate());
													BorrowSendTypeExample sendTypeExample = new BorrowSendTypeExample();
													BorrowSendTypeExample.Criteria sendTypeCriteria = sendTypeExample
															.createCriteria();
													sendTypeCriteria.andSendCdEqualTo("AUTO_FULL");
													List<BorrowSendType> sendTypeList = borrowSendTypeMapper
															.selectByExample(sendTypeExample);
													if (sendTypeList == null || sendTypeList.size() == 0) {
														System.out
																.println("用户:"
																		+ userId
																		+ "***********************************冻结成功后处理afterChinaPnR："
																		+ "数据库查不到 sendTypeList == null");
														throw new Exception("数据库查不到" + BorrowSendType.class);
													}
													BorrowSendType sendType = sendTypeList.get(0);
													if (sendType.getAfterTime() == null) {
														System.out
																.println("用户:"
																		+ userId
																		+ "***********************************冻结成功后处理afterChinaPnR："
																		+ "sendType.getAfterTime()==null");
														throw new Exception("sendType.getAfterTime()==null");
													}
													replaceMap.put("val_times", sendType.getAfterTime() + "");
													// 发送短信验证码
													SmsMessage smsMessage = new SmsMessage(null, replaceMap, null,
															null, MessageDefine.SMSSENDFORMANAGER, null,
															CustomConstants.PARAM_TPL_XMMB,
															CustomConstants.CHANNEL_TYPE_NORMAL);
													smsProcesser.gather(smsMessage);
												} else {
													throw new Exception("满标更新borrow表失败");
												}
											} else if (accountWait.compareTo(new BigDecimal(0)) < 0) {
												System.out.println("用户:" + userId + "项目编号:" + borrowNid
														+ "***********************************项目暴标");
												throw new RuntimeException("没有可投金额");
											}
											// 扣除可用金额
											AccountLog accountLog = new AccountLog();
											accountLog.setUserId(userId);// 操作用户id
											accountLog.setNid("tender_frost_" + userId + "_" + borrowNid + "_"
													+ orderId);
											accountLog.setTotalOld(BigDecimal.ZERO);
											accountLog.setCode("borrow");
											accountLog.setCodeType("tender");
											accountLog.setCodeNid(orderId);
											accountLog.setBorrowNid(borrowNid);// 收入
											accountLog.setIncomeOld(BigDecimal.ZERO);
											accountLog.setIncomeNew(BigDecimal.ZERO);
											accountLog.setAccountWebStatus(0);
											accountLog.setAccountUserStatus(0);
											accountLog.setAccountType("");
											accountLog.setMoney(accountDecimal);// 操作金额
											accountLog.setIncome(BigDecimal.ZERO);// 收入
											accountLog.setExpend(BigDecimal.ZERO);// 支出
											accountLog.setExpendNew(BigDecimal.ZERO);
											accountLog.setBalanceOld(BigDecimal.ZERO);
											accountLog.setBalanceNew(BigDecimal.ZERO);
											accountLog.setBalanceCash(BigDecimal.ZERO);
											accountLog.setBalanceCashNew(BigDecimal.ZERO);
											accountLog.setBalanceCashOld(BigDecimal.ZERO);
											accountLog.setExpendOld(BigDecimal.ZERO);
											accountLog.setBalanceCash(BigDecimal.ZERO);// 可提现金额
											accountLog.setBalanceFrost(accountDecimal.multiply(new BigDecimal(-1)));// 不可提现金额
											accountLog.setFrost(frost);// 冻结金额
											accountLog.setFrostOld(BigDecimal.ZERO);
											accountLog.setFrostNew(BigDecimal.ZERO);
											accountLog.setAwait(BigDecimal.ZERO);// 待收金额
											accountLog.setRepay(BigDecimal.ZERO);// 待还金额
											accountLog.setRepayOld(BigDecimal.ZERO);
											accountLog.setRepayNew(BigDecimal.ZERO);
											accountLog.setAwait(BigDecimal.ZERO);
											accountLog.setAwaitNew(BigDecimal.ZERO);
											accountLog.setAwaitOld(BigDecimal.ZERO);
											accountLog.setType("tender");// 类型
											accountLog.setToUserid(borrowPeriod); // 付给谁
											accountLog.setRemark("投标[" + borrowNid + "]所冻结资金");// 备注
											accountLog.setAddtime(nowTime + "");
											accountLog.setAddip(ip);
											accountLog.setBalanceFrostNew(BigDecimal.ZERO);
											accountLog.setBalanceFrostOld(BigDecimal.ZERO);
											boolean accountLogFlag = this.accountLogMapper.insertSelective(accountLog) > 0 ? true
													: false;
											if (accountLogFlag) {
												System.out.println("用户:" + userId
														+ "***********************************插入accountLog，订单号："
														+ orderId);
												// 提交事务
												this.transactionManager.commit(txStatus);
												info.put("isExcute", "1");
												return info;
											} else {
												throw new Exception("accountLog表更新失败");
											}
										} else {
											throw new Exception("borrow表更新失败");
										}
									} else {
										throw new Exception("用户账户交易明细表更新失败");
									}
								} else {
									throw new Exception("用户账户信息表更新失败");
								}
							} else {
								throw new Exception("用户账户信息查询失败！");
							}

						} else {
							throw new Exception("插入出借表失败！");
						}
					} else {
						throw new Exception("插入冻结表失败！");
					}
				} else {
					throw new Exception("删除borrowTenderTmp表失败");
				}
			} catch (Exception e) {
				// 回滚事务
				this.transactionManager.rollback(txStatus);
				this.recoverRedis(borrowNid, userId, account);
				status = ChinaPnrConstant.STATUS_FAIL;
				info.put("message", "出借失败！");
				info.put("status", status);
			}
		}
		return info;
	}

	/**
	 * 优惠券出借
	 */
	@Override
	public synchronized Boolean updateCouponTender(String couponGrantId, String borrowNid, String ordDate, Integer userId, String account, String ip, int couponOldTime, String mainTenderNid, Map<String, Object> retMap) {
		LogUtil.infoLog(InvestServiceImpl.class.toString(), "updateCouponTender", "优惠券出借开始。。。。。。");
		JSONObject jsonObject = CommonSoaUtils.CouponInvestForPC(userId + "", borrowNid, account,
				CustomConstants.CLIENT_PC, couponGrantId, mainTenderNid, ip, couponOldTime + "");
		if (jsonObject.getIntValue("status") == 0) {
			// 优惠券出借额度
			retMap.put("couponAccount", jsonObject.getString("accountDecimal"));
			// 优惠券面值
			retMap.put("couponQuota", jsonObject.getString("couponQuota"));
			// 优惠券类别
			retMap.put("couponType", jsonObject.getString("couponTypeInt"));
			LogUtil.infoLog(InvestServiceImpl.class.toString(), "updateCouponTender", "优惠券出借结束。。。。。。");
			return true;
		} else {
			LogUtil.infoLog(InvestServiceImpl.class.toString(), "updateCouponTender", "优惠券出借结束。。。。。。");
			return false;
		}

	}

	/**
	 * 获取用户的汇付信息
	 *
	 * @param userId
	 * @return 用户的汇付信息
	 */

	@Override
	public AccountChinapnr getAccountChinapnr(Integer userId) {
		AccountChinapnr accountChinapnr = null;
		if (userId == null) {
			return null;
		}
		AccountChinapnrExample example = new AccountChinapnrExample();
		AccountChinapnrExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		List<AccountChinapnr> list = accountChinapnrMapper.selectByExample(example);
		if (list != null && !list.isEmpty()) {
			accountChinapnr = list.get(0);
		}
		return accountChinapnr;
	}

	/**
	 * 获取借款信息
	 *
	 * @param borrowId
	 * @return 借款信息
	 */

	@Override
	public Borrow getBorrowByNid(String borrowNid) {
		Borrow borrow = null;
		BorrowExample example = new BorrowExample();
		BorrowExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		List<Borrow> list = borrowMapper.selectByExample(example);
		if (list != null && !list.isEmpty()) {
			borrow = list.get(0);
		}
		return borrow;
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 *
	 * @param account
	 * @param tenderUsrcustid
	 * @param borrowerUsrcustid
	 * @param OrdId
	 * @return
	 * @author b
	 */

	@Override
	public FreezeDefine freeze(Integer userId, String account, String tenderUsrcustid, String borrowerUsrcustid, String OrdId) {
		FreezeDefine freezeDefine = new FreezeDefine();
		Properties properties = PropUtils.getSystemResourcesProperties(); // 商户后台应答地址(必须)
		String bgRetUrl = properties.getProperty("hyjf.chinapnr.callbackurl").trim();
		ChinapnrBean chinapnrBean = new ChinapnrBean();
		chinapnrBean.setVersion("10");// 接口版本号
		chinapnrBean.setCmdId("UsrFreezeBg"); // 消息类型(冻结)
		chinapnrBean.setUsrCustId(tenderUsrcustid);// 出借用户客户号
		chinapnrBean.setOrdId(OrdId); // 订单号(必须)
		chinapnrBean.setOrdDate(GetDate.getServerDateTime(1, new Date()));// 订单时间(必须)格式为yyyyMMdd，例如：20130307
		chinapnrBean.setTransAmt(CustomUtil.formatAmount(account));// 交易金额(必须)
		chinapnrBean.setRetUrl(""); // 页面返回
		chinapnrBean.setBgRetUrl(bgRetUrl); // 商户后台应答地址(必须)
		chinapnrBean.setType("user_freeze"); // 日志类型
		chinapnrBean.setLogUserId(userId);
		// chinapnrBean.setIsFreeze("N");
		ChinapnrBean bean = ChinapnrUtil.callApiBg(chinapnrBean);
		// 处理冻结返回信息
		if (bean != null) {
			String respCode = bean.getRespCode();
			freezeDefine.setFreezeTrxId(bean.getTrxId());
			System.out.println("用户:" + userId + "***********************************冻结订单号：" + bean.getTrxId());
			if (StringUtils.isNotEmpty(respCode) && respCode.equals(ChinaPnrConstant.RESPCODE_SUCCESS)) {
				freezeDefine.setFreezeFlag(true);
			} else {
				System.out.println("用户:" + userId + "***********************************冻结失败错误码：" + respCode);
				freezeDefine.setFreezeFlag(false);
			}
			return freezeDefine;
		} else {
			return null;
		}
	}

	/**
	 * 更新标的信息
	 *
	 * @param record
	 * @return
	 * @author b
	 */

	@Override
	public boolean updateBorrow(Borrow record) {
		borrowMapper.updateByPrimaryKey(record);
		return true;

	}

	/**
	 * 新用户新手标验证，标如果是新用户标，查看此用户是否有过出借记录，如果有返回true，提示不能投标了
	 *
	 * @param userId
	 * @param projectType
	 * @return
	 */

	@Override
	public boolean checkIsNewUserCanInvest(Integer userId) {
		// 新的判断是否为新用户方法
		int total = webUserInvestListCustomizeMapper.countNewUserTotal(userId + "");
		if (total == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断是否51老用户,如果是则返回true，否则返回false
	 *
	 * @param userId
	 * @param projectType
	 * @return
	 * @author b
	 */

	@Override
	public boolean checkIs51UserCanInvest(Integer userId) {
		UsersInfoExample usersInfoExample = new UsersInfoExample();
		UsersInfoExample.Criteria borrowCriteria = usersInfoExample.createCriteria();
		borrowCriteria.andUserIdEqualTo(userId);
		List<UsersInfo> list = usersInfoMapper.selectByExample(usersInfoExample);
		if (list != null && !list.isEmpty()) {
			UsersInfo usersInfo = list.get(0);
			if (usersInfo != null) {
				Integer is51 = usersInfo.getIs51();// 1是51，0不是
				if (is51 != null && is51 == 1) {
					return true;
				}
			}
		}
		return false;

	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 *
	 * @param userId
	 * @return
	 * @author b
	 */

	@Override
	public UsersInfo getUserInfo(Integer userId) {
		UsersInfoExample usersInfoExample = new UsersInfoExample();
		UsersInfoExample.Criteria usersInfoExampleCriteria = usersInfoExample.createCriteria();
		usersInfoExampleCriteria.andUserIdEqualTo(userId);
		List<UsersInfo> userInfoList = usersInfoMapper.selectByExample(usersInfoExample);
		UsersInfo usersInfo = null;
		if (userInfoList != null && !userInfoList.isEmpty()) {
			usersInfo = userInfoList.get(0);
		}
		return usersInfo;

	}

	/**
	 * 获取用户信息
	 *
	 * @param userId
	 * @return
	 * @author b
	 */

	@Override
	public Users getUsers(Integer userId) {
		if (userId == null) {
			return null;
		}
		// 查找用户
		UsersExample usersExample = new UsersExample();
		UsersExample.Criteria criteria2 = usersExample.createCriteria();
		criteria2.andUserIdEqualTo(userId);
		List<Users> userList = usersMapper.selectByExample(usersExample);
		Users users = null;
		if (userList != null && !userList.isEmpty()) {
			users = userList.get(0);

		}
		return users;

	}

	/**
	 * 获取项目类型
	 * 
	 * @param projectType
	 * @return
	 * @author b
	 */

	@Override
	public BorrowProjectType getBorrowProjectType(String projectType) {
		if (StringUtils.isBlank(projectType)) {
			return null;
		}
		// 查找用户
		BorrowProjectTypeExample borrowProjectTypeExample = new BorrowProjectTypeExample();
		BorrowProjectTypeExample.Criteria criteria2 = borrowProjectTypeExample.createCriteria();
		criteria2.andBorrowCdEqualTo(projectType);
		List<BorrowProjectType> list = borrowProjectTypeMapper.selectByExample(borrowProjectTypeExample);
		BorrowProjectType borrowProjectType = null;
		if (list != null && !list.isEmpty()) {
			borrowProjectType = list.get(0);

		}
		return borrowProjectType;
	}

	/**
	 * 
	 * 回滚redis
	 * 
	 * @param borrowNid
	 * @param userId
	 * @param account
	 * @author Administrator
	 */
	public void recoverRedis(String borrowNid, Integer userId, String account) {
		JedisPool pool = RedisUtils.getPool();
		Jedis jedis = pool.getResource();
		BigDecimal accountBigDecimal = new BigDecimal(account);
		String balanceLast = RedisUtils.get(borrowNid);
		if (StringUtils.isNotBlank(balanceLast)) {
			while ("OK".equals(jedis.watch(borrowNid))) {
				BigDecimal recoverAccount = accountBigDecimal.add(new BigDecimal(balanceLast));
				Transaction tx = jedis.multi();
				tx.set(borrowNid, recoverAccount + "");
				List<Object> result = tx.exec();
				if (result == null || result.isEmpty()) {
					jedis.unwatch();
				} else {
					System.out.println("用户:" + userId + "***********************************from redis恢复redis：" + account);
					break;
				}
			}
		}
	}

	/**
	 * 根据汇付客户号查询用户信息
	 * 
	 * @param usrCustId
	 * @return
	 */

	@Override
	public Integer getUserIdByUsrcustId(String usrCustId) {
		if (StringUtils.isBlank(usrCustId)) {
			return null;
		}
		// 查找用户userId
		AccountChinapnrExample borrowProjectTypeExample = new AccountChinapnrExample();
		AccountChinapnrExample.Criteria criteria2 = borrowProjectTypeExample.createCriteria();
		criteria2.andChinapnrUsrcustidEqualTo(Long.valueOf(usrCustId));
		List<AccountChinapnr> list = accountChinapnrMapper.selectByExample(borrowProjectTypeExample);
		Integer userId = null;
		if (list != null && !list.isEmpty()) {
			userId = list.get(0).getUserId();
		}
		return userId;
	}

	/**
	 * 
	 * 出借校验
	 * 
	 * @param borrowNid
	 * @param account
	 * @param userIdInt
	 * @param platform
	 * @param couponGrantId
	 * @return
	 * @author Administrator
	 */
	public JSONObject checkParam(String borrowNid, String account, Integer userIdInt, String platform, String couponGrantId) {

		CouponConfigCustomizeV2 cuc = null;
		// -1:有可用的优惠券，但是出借时不选择优惠券 空或null：用户没有可用优惠券
		/*
		 * if(StringUtils.isNotEmpty(couponGrantId) && !StringUtils.equals("-1",
		 * couponGrantId)){ cuc = this.getCouponUser(couponGrantId); }
		 */
		UsersInfoExample usersInfoExample = new UsersInfoExample();
		usersInfoExample.createCriteria().andUserIdEqualTo(userIdInt);
		List<UsersInfo> usersInfos = this.usersInfoMapper.selectByExample(usersInfoExample);
		if (null != usersInfos && usersInfos.size() == 1) {
			String roleIsOpen = PropUtils.getSystem(CustomConstants.HYJF_ROLE_ISOPEN);
			if(StringUtils.isNotBlank(roleIsOpen) && roleIsOpen.equals("true")){
				if (usersInfos.get(0).getRoleId() != 1) {// 担保机构用户
					return jsonMessage("仅限出借人进行出借", "1");
				}
			}

		} else {
			return jsonMessage("账户信息异常", "1");
		}
		String userId = "";
		if (userIdInt == null) {
			return jsonMessage("您未登陆，请先登录", "1");
		} else {
			userId = String.valueOf(userIdInt.intValue());
		}
		// 判断用户userId是否存在
		if (StringUtils.isEmpty(userId)) {
			return jsonMessage("您未登陆，请先登录", "1");
		}
		Users user = this.getUsers(Integer.parseInt(userId));
		if (StringUtils.isNotEmpty(couponGrantId) && !StringUtils.equals("-1", couponGrantId)) {
			cuc = this.getCouponUser(couponGrantId, userIdInt);
		}
		// 判断用户信息是否存在
		if (user == null) {
			return jsonMessage("用户信息不存在", "1");
		}
		// 判断用户是否禁用
		if (user.getStatus() == 1) {// 0启用，1禁用
			return jsonMessage("该用户已被禁用", "1");
		}
		// 判断借款编号是否存在
		if (StringUtils.isEmpty(borrowNid)) {
			return jsonMessage("借款项目不存在", "1");
		}
		Borrow borrow = this.getBorrowByNid(borrowNid);
		// 判断借款信息是否存在
		if (borrow == null || borrow.getId() == null) {
			return jsonMessage("借款项目不存在", "1");
		}
		if (borrow.getUserId() == null) {
			return jsonMessage("借款人不存在", "1");
		}

		Integer projectType = borrow.getProjectType();// 0，51老用户；1，新用户；2，全部用户
		if (projectType == null) {
			return jsonMessage("未设置该出借项目的项目类型", "1");
		}
		// 出借项目的配置信息
		BorrowProjectType borrowProjectType = this.getBorrowProjectType(String.valueOf(projectType));
		if (borrowProjectType == null) {
			return jsonMessage("未查询到该出借项目的配置信息", "1");
		}
		// 51老用户标
		if (borrowProjectType.getInvestUserType().equals("0")) {
			boolean is51User = this.checkIs51UserCanInvest(Integer.parseInt(userId));
			if (!is51User) {
				return jsonMessage("该项目只能51老用户出借", "1");
			}
		}
		if (borrowProjectType.getInvestUserType().equals("1")) {
			boolean newUser = this.checkIsNewUserCanInvest(Integer.parseInt(userId));
			if (!newUser) {
				return jsonMessage("该项目只能新手出借", "1");
			}
		}
		AccountChinapnr accountChinapnrTender = this.getAccountChinapnr(Integer.parseInt(userId));
		// 用户未在平台开户
		if (accountChinapnrTender == null) {
			return jsonMessage("用户开户信息不存在", "1");
		}
		// 判断借款人开户信息是否存在
		if (accountChinapnrTender.getChinapnrUsrcustid() == null) {
			return jsonMessage("用户汇付客户号不存在", "1");
		}

		// 项目出借客户端
		if (platform.equals("0") && borrow.getCanTransactionPc().equals("0")) {
			String tmpInfo = "";
			if (borrow.getCanTransactionAndroid().equals("1")) {
				tmpInfo += " Android端  ";
			}
			if (borrow.getCanTransactionIos().equals("1")) {
				tmpInfo += " Ios端  ";
			}
			if (borrow.getCanTransactionWei().equals("1")) {
				tmpInfo += " 微信端  ";
			}
			return jsonMessage("此项目只能在" + tmpInfo + "出借", "1");
		} else if (platform.equals("1") && borrow.getCanTransactionWei().equals("0")) {
			String tmpInfo = "";
			if (borrow.getCanTransactionAndroid().equals("1")) {
				tmpInfo += " Android端  ";
			}
			if (borrow.getCanTransactionIos().equals("1")) {
				tmpInfo += " Ios端  ";
			}
			if (borrow.getCanTransactionPc().equals("1")) {
				tmpInfo += " Pc端  ";
			}
			return jsonMessage("此项目只能在" + tmpInfo + "出借", "1");
		} else if (platform.equals("2") && borrow.getCanTransactionAndroid().equals("0")) {
			String tmpInfo = "";
			if (borrow.getCanTransactionPc().equals("1")) {
				tmpInfo += " PC端  ";
			}
			if (borrow.getCanTransactionIos().equals("1")) {
				tmpInfo += " Ios端  ";
			}
			if (borrow.getCanTransactionWei().equals("1")) {
				tmpInfo += " 微信端  ";
			}
			return jsonMessage("此项目只能在" + tmpInfo + "出借", "1");
		} else if (platform.equals("3") && borrow.getCanTransactionIos().equals("0")) {
			String tmpInfo = "";
			if (borrow.getCanTransactionPc().equals("1")) {
				tmpInfo += " PC端  ";
			}
			if (borrow.getCanTransactionAndroid().equals("1")) {
				tmpInfo += " Android端  ";
			}
			if (borrow.getCanTransactionWei().equals("1")) {
				tmpInfo += " 微信端  ";
			}
			return jsonMessage("此项目只能在" + tmpInfo + "出借", "1");
		}
		AccountChinapnr accountChinapnrBorrower = this.getAccountChinapnr(borrow.getUserId());
		if (accountChinapnrBorrower == null) {
			return jsonMessage("借款人未开户", "1");
		}
		if (accountChinapnrBorrower.getChinapnrUsrcustid() == null) {
			return jsonMessage("借款人汇付客户号不存在", "1");
		}
		if (userId.equals(String.valueOf(borrow.getUserId()))) {
			return jsonMessage("借款人不可以自己出借项目", "1");
		}
		// 判断借款是否流标
		if (borrow.getStatus() == 2) { // 流标
			return jsonMessage("此项目已经流标", "1");
		}
		// 已满标
		if (borrow.getBorrowFullStatus() == 1) {
			return jsonMessage("此项目已经满标", "1");
		}
		// 判断用户出借金额是否为空
		if (!(StringUtils.isNotEmpty(account) || (StringUtils.isEmpty(account) && cuc != null && cuc.getCouponType() == 3) || (StringUtils.isEmpty(account) && cuc != null && cuc.getCouponType() == 1 && cuc.getAddFlg() == 1))) {
			return jsonMessage("请输入出借金额", "1");
		}
		// 还款金额是否数值
		try {
			// 出借金额必须是整数
			Long accountInt = Long.parseLong(account);
			if ((accountInt == 0 && cuc == null) || (accountInt == 0 && cuc != null && cuc.getCouponType() == 2)) {
				return jsonMessage("出借金额不能为0元", "1");
			}
			if (accountInt != 0 && cuc != null && cuc.getCouponType() == 1 && cuc.getAddFlg() == 1) {
				return jsonMessage("该优惠券只能单独使用", "1");
			}
			if (accountInt < 0) {
				return jsonMessage("出借金额不能为负数", "1");
			}
			// 新需求判断顺序变化
			// 将出借金额转化为BigDecimal
			BigDecimal accountBigDecimal = new BigDecimal(account);
			String balance = RedisUtils.get(borrowNid);
			if (StringUtils.isEmpty(balance)) {
				return jsonMessage("您来晚了，下次再来抢吧", "1");
			}
			// 剩余可投金额
			Integer min = borrow.getTenderAccountMin();
			// 当剩余可投金额小于最低起投金额，不做最低起投金额的限制
			if (min != null && min != 0 && new BigDecimal(balance).compareTo(new BigDecimal(min)) == -1) {
				if (accountBigDecimal.compareTo(new BigDecimal(balance)) == 1) {
					return jsonMessage("剩余可投金额为" + balance + "元", "1");
				}
				if (accountBigDecimal.compareTo(new BigDecimal(balance)) != 0) {
					return jsonMessage("剩余可投只剩" + balance + "元，须全部购买", "1");
				}
			} else {
				// 项目的剩余金额大于最低起投金额
				if (accountBigDecimal.compareTo(new BigDecimal(min)) == -1) {
					if (accountBigDecimal.compareTo(BigDecimal.ZERO) == 0) {
						if (cuc != null && cuc.getCouponType() != 3 && cuc.getCouponType() != 1) {
							return jsonMessage(borrow.getTenderAccountMin() + "元起投", "1");
						}
					} else {
						return jsonMessage(borrow.getTenderAccountMin() + "元起投", "1");
					}
				} else {
					Integer max = borrow.getTenderAccountMax();
					if (max != null && max != 0 && accountBigDecimal.compareTo(new BigDecimal(max)) == 1) {
						return jsonMessage("项目最大出借额为" + max + "元", "1");
					}
				}
			}
			if (accountBigDecimal.compareTo(borrow.getAccount()) > 0) {
				return jsonMessage("出借金额不能大于项目总额", "1");
			}
			// 出借人记录
			Account tenderAccount = this.getAccount(Integer.parseInt(userId));
			if (tenderAccount.getBalance().compareTo(accountBigDecimal) < 0) {
				return jsonMessage("可用金额不足，请充值！", "1");
			}

			// redis剩余金额不足
			if (accountBigDecimal.compareTo(new BigDecimal(balance)) == 1) {
				return jsonMessage("剩余可投金额为" + balance + "元", "1");
			}
			// 根据项目标号获取相应的项目信息
			WebProjectDetailCustomize borrowDetail = webProjectListCustomizeMapper.selectProjectDetail(borrowNid);
			if (borrowDetail.getIncreaseMoney() != null && (accountInt - min) % Integer.parseInt(borrowDetail.getIncreaseMoney()) != 0 && accountBigDecimal.compareTo(new BigDecimal(balance)) == -1) {
				return jsonMessage("出借递增金额须为" + borrowDetail.getIncreaseMoney() + " 元的整数倍", "1");
			}
			// 如果验证没问题，则返回出借人借款人的汇付账号
			Long borrowerUsrcustid = accountChinapnrBorrower.getChinapnrUsrcustid();
			Long tenderUsrcustid = accountChinapnrTender.getChinapnrUsrcustid();
			JSONObject jsonMessage = new JSONObject();
			jsonMessage.put("error", "0");
			jsonMessage.put("borrowerUsrcustid", borrowerUsrcustid);
			jsonMessage.put("tenderUsrcustid", tenderUsrcustid);
			jsonMessage.put("borrowId", borrow.getId());
			jsonMessage.put("bankInputFlag", borrow.getBankInputFlag() + "");
			return jsonMessage;

		} catch (Exception e) {
			return jsonMessage("出借金额必须为整数", "1");
		}

	}

	/**
	 * 拼装返回信息
	 * 
	 * @param message
	 * @param status
	 * @return
	 */
	public JSONObject jsonMessage(String data, String error) {
		JSONObject jo = null;
		if (Validator.isNotNull(data)) {
			jo = new JSONObject();
			jo.put("error", error);
			jo.put("data", data);
		}
		return jo;
	}

	/**
	 * 
	 * 解冻用户出借资金
	 * 
	 * @param borrowUserId
	 * @param investUserId
	 * @param orderId
	 * @param trxId
	 * @param ordDate
	 * @return
	 * @throws Exception
	 * @author Administrator
	 */
	@Override
	public boolean unFreezeOrder(int borrowUserId, int investUserId, String orderId, String trxId, String ordDate) throws Exception {

		// 借款人在汇付的账户信息
		AccountChinapnr inCust = this.getAccountChinapnr(borrowUserId);
		if (inCust == null) {
			throw new Exception("借款人未开户。[借款人ID：" + borrowUserId + "]，" + "[出借订单号：" + orderId + "]");
		}
		// 出借人的账户信息
		AccountChinapnr outCust = this.getAccountChinapnr(investUserId);
		if (outCust == null) {
			throw new Exception("出借人未开户。[出借人ID：" + investUserId + "]，" + "[出借订单号：" + orderId + "]");
		}
		// 调用交易查询接口(解冻)
		ChinapnrBean queryTransStatBean = queryTransStat(orderId, ordDate, "FREEZE");
		if (queryTransStatBean == null) {
			throw new Exception("调用交易查询接口(解冻)失败。" + ",[出借订单号：" + orderId + "]");
		} else {
			String queryRespCode = queryTransStatBean.getRespCode();
			System.out.print("出借失败交易接口查询接口返回码：" + queryRespCode);
			// 调用接口失败时(000以外)
			if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(queryRespCode)) {
				String message = queryTransStatBean == null ? "" : queryTransStatBean.getRespDesc();
				LogUtil.errorLog(this.getClass().getName(), "unFreezeOrder", "调用交易查询接口(解冻)失败。" + message + ",[出借订单号：" + orderId + "]", null);
				throw new Exception("调用交易查询接口(解冻)失败。" + queryRespCode + "：" + message + ",[出借订单号：" + orderId + "]");
			} else {
				// 汇付交易状态
				String transStat = queryTransStatBean == null ? "" : queryTransStatBean.getTransStat();
				// 冻结请求已经被解冻 U:已解冻 N:无需解冻，对于解冻交易
				if (!"U".equals(transStat) && !"N".equals(transStat)) {
					/** 解冻订单 */
					ChinapnrBean unFreezeBean = usrUnFreeze(trxId);
					String respCode = unFreezeBean == null ? "" : unFreezeBean.getRespCode();
					System.out.print("出借失败自动解冻接口返回码：" + respCode);
					// 调用接口失败时(000 或 107 以外)
					if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode) && !ChinaPnrConstant.RESPCODE_REPEAT_DEAL.equals(respCode)) {
						String message = unFreezeBean == null ? "" : unFreezeBean.getRespDesc();
						message = "调用解冻接口失败。" + respCode + "：" + message + "，出借订单号[" + orderId + "]";
						LogUtil.errorLog(this.getClass().getName(), "unFreezeOrder", message, null);
						return false;
					} else {
						return true;
					}
				} else {
					return true;
				}
			}
		}
	}

	/**
	 * 查询交易状态
	 * 
	 * @param ordId
	 * @param ordDate
	 * @param queryTransType
	 * @return
	 */
	private ChinapnrBean queryTransStat(String ordId, String ordDate, String queryTransType) {
		String methodName = "queryTransStat";

		// 调用汇付接口(交易状态查询)
		ChinapnrBean bean = new ChinapnrBean();
		bean.setVersion(ChinaPnrConstant.VERSION_10); // 版本号(必须)
		bean.setCmdId(ChinaPnrConstant.CMDID_QUERYTRANSSTAT); // 消息类型(必须)
		bean.setOrdId(ordId); // 订单号(必须)
		bean.setOrdDate(ordDate); // 订单日期(必须)
		bean.setQueryTransType(queryTransType); // 交易查询类型
		// 写log用参数
		bean.setLogUserId(0);
		bean.setLogRemark("交易状态查询"); // 备注
		bean.setLogClient("0"); // PC
		// 调用汇付接口
		ChinapnrBean chinapnrBean = ChinapnrUtil.callApiBg(bean);
		if (chinapnrBean == null) {
			LogUtil.errorLog(this.getClass().getName(), methodName, new Exception("调用交易状态查询接口失败![参数：" + bean.getAllParams() + "]"));
			return null;
		}
		return chinapnrBean;
	}

	/**
	 * 资金（货款）解冻(调用汇付天下接口)
	 *
	 * @param trxId
	 * @return
	 */
	private ChinapnrBean usrUnFreeze(String trxId) {
		String methodName = "usrUnFreeze";

		// 调用汇付接口
		ChinapnrBean bean = new ChinapnrBean();
		bean.setVersion(ChinaPnrConstant.VERSION_10); // 版本号(必须)
		bean.setCmdId(ChinaPnrConstant.CMDID_USR_UN_FREEZE); // 消息类型(必须)
		bean.setOrdId(GetOrderIdUtils.getOrderId2(0)); // 订单号(必须)
		bean.setOrdDate(GetOrderIdUtils.getOrderDate()); // 订单日期(必须)
		bean.setTrxId(trxId); // 本平台交易唯一标识(必须)
		bean.setBgRetUrl(ChinapnrUtil.getBgRetUrl()); // 商户后台应答地址(必须)
		// 写log用参数
		bean.setLogUserId(0);
		bean.setLogRemark("资金（货款）解冻"); // 备注
		bean.setLogClient("0"); // PC
		ChinapnrBean chinapnrBean = ChinapnrUtil.callApiBg(bean);
		if (chinapnrBean == null) {
			LogUtil.errorLog(this.getClass().getName(), methodName, new Exception("调用解冻接口失败![参数：" + bean.getAllParams() + "]"));
			return null;
		}
		return chinapnrBean;
	}

	// add by zhangjp 优惠券相关 start
	/**
	 * 取得用户优惠券信息
	 * 
	 * @param couponGrantId
	 * @return
	 */
	public CouponConfigCustomizeV2 getCouponUser(String couponGrantId, Integer userId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("couponGrantId", couponGrantId);
		paramMap.put("userId", userId);
		CouponConfigCustomizeV2 ccTemp = this.couponUserCustomizeMapper.selectCouponConfigByGrantId(paramMap);
		return ccTemp;
	}

	// add by zhangjp 优惠券相关 end

	/**
	 * 
	 * 更新新手出借标志位
	 * 
	 * @param userId
	 * @return
	 * @author Administrator
	 */
	@Override
	public boolean updateUserInvestFlagById(Integer userId) {

		Integer newFlag = webUserInvestListCustomizeMapper.selectUserInvestFlag(userId);
		if (newFlag != null && newFlag == 0) {
			Integer investFlag = webUserInvestListCustomizeMapper.updateUserInvestFlag(userId);
			if (investFlag > 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	/**
	 * 
	 * 获取项目类型名称
	 * 
	 * @param borrowProjectType
	 * @return
	 * @author Administrator
	 */
	@Override
	public String getProjectTypeName(BorrowProjectType borrowProjectType) {
		List<BorrowProjectType> list = getProjectTypeList();
		for (BorrowProjectType projectType : list) {
			if (projectType.getBorrowCd().equals(borrowProjectType.getBorrowCd())) {
				if ("ZXH".equals(borrowProjectType.getBorrowClass())) {
					return borrowProjectType.getBorrowName();
				} else if ("NEW".equals(borrowProjectType.getBorrowClass())) {
					return borrowProjectType.getBorrowName();
				} else if ("HXF".equals(borrowProjectType.getBorrowClass())) {
					return borrowProjectType.getBorrowName();
				} else {
					return "汇直投";
				}
			}
		}
		return "汇直投";
	}

	/**
	 * 获取项目类型列表
	 * 
	 * @return
	 */
	public List<BorrowProjectType> getProjectTypeList() {
		BorrowProjectTypeExample example = new BorrowProjectTypeExample();
		BorrowProjectTypeExample.Criteria cra = example.createCriteria();
		cra.andStatusEqualTo(CustomConstants.FLAG_NORMAL);
		return this.borrowProjectTypeMapper.selectByExample(example);
	}

	/**
	 * 
	 * 校验优惠券是否可用
	 * 
	 * @param projectTypeList
	 * @param projectTypeCd
	 * @return
	 * @author Administrator
	 */
	// pcc20160715
	@Override
	public String validateCouponProjectType(String projectTypeList, String projectTypeCd) {
		boolean ifprojectType = true;
		BorrowProjectType borrowProjectType = new BorrowProjectType();
		BorrowProjectTypeExample example = new BorrowProjectTypeExample();
		example.createCriteria().andBorrowCdEqualTo(projectTypeCd);
		List<BorrowProjectType> list = borrowProjectTypeMapper.selectByExample(example);
		borrowProjectType = list.get(0);
		if (projectTypeList.indexOf("-1") != -1) {
			if (!"RTB".equals(borrowProjectType.getBorrowClass())) {
				ifprojectType = false;
			}
		} else {

			if ("HXF".equals(borrowProjectType.getBorrowClass())) {
				if (projectTypeList.indexOf("2") != -1) {
					ifprojectType = false;
				}
			} else if ("NEW".equals(borrowProjectType.getBorrowClass())) {
				if (projectTypeList.indexOf("3") != -1) {
					ifprojectType = false;
				}
			} else if ("ZXH".equals(borrowProjectType.getBorrowClass())) {
				if (projectTypeList.indexOf("4") != -1) {
					ifprojectType = false;
				}
			} else {
				if (projectTypeList.indexOf("1") != -1) {
					if (!"RTB".equals(borrowProjectType.getBorrowClass())) {
						ifprojectType = false;
					}
				}
			}
		}
		if (!ifprojectType) {
			return null;
		}
		return "对不起，您选择的优惠券不能用于当前类别标的";
	}

	/**
	 * 
	 * 查询用户的出借记录
	 * 
	 * @param userId
	 * @param borrowNid
	 * @param nid
	 * @return
	 * @author Administrator
	 */
	@Override
	public int countBorrowTenderNum(Integer userId, String borrowNid, String nid) {
		BorrowTenderExample example = new BorrowTenderExample();
		BorrowTenderExample.Criteria crt = example.createCriteria();
		crt.andUserIdEqualTo(userId);
		crt.andNidEqualTo(nid);
		crt.andBorrowNidEqualTo(borrowNid);
		return borrowTenderMapper.countByExample(example);
	}

	/**
	 * 更新出借记录临时表
	 * 
	 * @param userId
	 * @param ordId
	 * @return
	 * @author Administrator
	 */
	public int updateBorrowTenderTmp(Integer userId, String borrowNid, String ordId) {
		BorrowTenderTmpExample example = new BorrowTenderTmpExample();
		BorrowTenderTmpExample.Criteria crt = example.createCriteria();
		crt.andUserIdEqualTo(userId);
		crt.andNidEqualTo(ordId);
		crt.andBorrowNidEqualTo(borrowNid);
		BorrowTenderTmp tenderTmp = new BorrowTenderTmp();
		tenderTmp.setStatus(1);
		return this.borrowTenderTmpMapper.updateByExampleSelective(tenderTmp, example);

	}

	/**
	 * 
	 * 查询优惠券数据
	 * 
	 * @param couponGrantId
	 * @param borrow
	 * @param userId
	 * @param account
	 * @param couponOldTime
	 * @return
	 * @author Administrator
	 */
	@Override
	public Map<String, Object> queryCouponData(String couponGrantId, Borrow borrow, Integer userId, String account, int couponOldTime) {

		Map<String, Object> retMap = new HashMap<String, Object>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("couponGrantId", couponGrantId);
		paramMap.put("userId", userId);
		CouponConfigCustomizeV2 ccTemp = this.couponUserCustomizeMapper.selectCouponConfigByGrantId(paramMap);
		// 出借金额
		BigDecimal accountDecimal = null;
		if (Validator.isNotNull(ccTemp)) {
			// 优惠券类别
			int couponType = ccTemp.getCouponType();
			// 面值
			BigDecimal couponQuota = ccTemp.getCouponQuota();
			// 年华收益率
			BigDecimal couponRate = null;
			if (couponType == 1) {
				// 体验金 出借资金=体验金面值
				accountDecimal = couponQuota;
				couponRate = borrow.getBorrowApr();
			} else if (couponType == 2) {
				// 加息券 出借资金=真实出借资金
				accountDecimal = new BigDecimal(account);
				couponRate = couponQuota;
			} else if (couponType == 3) {
				// 代金券 出借资金=体验金面值
				accountDecimal = couponQuota;
				couponRate = borrow.getBorrowApr();
			}
			// 优惠券出借额度
			retMap.put("couponAccount", accountDecimal);
			// 优惠券面值
			retMap.put("couponQuota", couponQuota);
			// 优惠券类别
			retMap.put("couponType", couponType);
			// 年华收益率
			retMap.put("couponRate", couponRate);
			return retMap;
		} else {
			return null;
		}

	}

	/**
	 * 
	 * 查询优惠券是否使用
	 * 
	 * @param orderId
	 * @param couponGrantId
	 * @param userId
	 * @return
	 * @author Administrator
	 */

	@Override
	public boolean updateCouponTenderStatus(String orderId, String couponGrantId, Integer userId) {

		/** 为了看到事务提交结果，不得已进行线程sleep ，不要删除 王坤 */
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int nowTime = GetDate.getNowTime10();
		// 优惠券用户表状态
		CouponUser cu = new CouponUser();
		cu.setId(Integer.valueOf(couponGrantId));
		cu.setUpdateTime(nowTime);
		boolean flag = this.couponUserMapper.updateByPrimaryKeySelective(cu) > 0 ? true : false;
		if (flag) {
			CouponRealTenderExample realExample = new CouponRealTenderExample();
			CouponRealTenderExample.Criteria realCrt = realExample.createCriteria();
			realCrt.andRealTenderIdEqualTo(orderId);
			int count = this.couponRealTenderMapper.countByExample(realExample);
			if (count > 0) {
				return true;
			} else {
				return false;
			}

		} else {
			return false;
		}

	}

	/**
	 * 
	 * 查询用户出借记录
	 * 
	 * @param userId
	 * @param nid
	 * @return
	 * @author Administrator
	 */
	public int countBorrowTenderNum(Integer userId, String nid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("nid", nid);
		params.put("userId", userId);
		return webUserInvestListCustomizeMapper.countUserInvestNumBynid(params);
	}

	/**
	 * 
	 * 校验用户出借-用户预约
	 * 
	 * @param borrowNid
	 * @param account
	 * @param userIdInt
	 * @param platform
	 * @param appointCheck
	 * @return
	 * @author Administrator
	 */
	public JSONObject checkParamAppointment(String borrowNid, String account, Integer userIdInt, String platform, String appointCheck) {

		String userId = "";
		if (userIdInt == null) {
			return jsonMessage("您未登陆，请先登录", "1");
		} else {
			userId = String.valueOf(userIdInt.intValue());
		}
		// 判断用户userId是否存在
		if (StringUtils.isEmpty(userId)) {
			return jsonMessage("您未登陆，请先登录", "1");
		}
		UsersInfoExample usersInfoExample = new UsersInfoExample();
		usersInfoExample.createCriteria().andUserIdEqualTo(userIdInt);
		List<UsersInfo> usersInfos = this.usersInfoMapper.selectByExample(usersInfoExample);
		if (null != usersInfos && usersInfos.size() == 1) {
			String roleIsOpen = PropUtils.getSystem(CustomConstants.HYJF_ROLE_ISOPEN);
			if(StringUtils.isNotBlank(roleIsOpen) && roleIsOpen.equals("true")){
				if (usersInfos.get(0).getRoleId() != 1) {// 担保机构用户
					return jsonMessage("仅限出借人进行出借", "1");
				}
			}

		} else {
			return jsonMessage("账户信息异常", "1");
		}
		Users user = this.getUsers(Integer.parseInt(userId));
		// 判断用户信息是否存在
		if (user == null) {
			return jsonMessage("用户信息不存在", "1");
		}
		// 判断用户是否禁用
		if (user.getStatus() == 1) {// 0启用，1禁用
			return jsonMessage("该用户已被禁用", "1");
		}
		/**
		 * authType 授权方式：0完全授权，1部分授权 authStatus 预约授权状态：0：未授权1：已授权 authTime
		 * 授权操作时间 recodTotal 违约累计积分 recodTime 违约更新时间 recodTruncateTime 积分清空时间
		 */
		Map<String, Object> map = webUserInvestListCustomizeMapper.selectUserAppointmentInfo(userId);
		if (map == null || map.isEmpty()) {
			return jsonMessage("您未进行预约授权  <a href='" + HOST + "/user/pandect/appointment.do?appointment=1'>去授权 </a> ", "1");
		}
		if (map.get("authStatus") == null) {
			return jsonMessage("您未进行预约授权  <a href='" + HOST + "/user/pandect/appointment.do?appointment=1'>去授权 </a> ", "1");
		}
		// 预约接口查询
		Map<String, Object> appointmentMap = pandectService.checkAppointmentStatus(Integer.parseInt(userId), map.get("authStatus") + "");
		boolean appointmentFlag = (boolean) appointmentMap.get("appointmentFlag");
		if (appointmentFlag) {
			String AuthType = "W";
			if (user.getAuthType() != null && user.getAuthType() == 1) {
				AuthType = "P";
			}
			// 开启授权同步操作
			pandectService.updateUserAuthStatus(AuthType, (1 - Integer.parseInt(map.get("authStatus") + "")) + "", userId + "");
		}
		Map<String, Object> map1 = webUserInvestListCustomizeMapper.selectUserAppointmentInfo(userId);
		if ((map1.get("authStatus") + "").equals("0")) {
			return jsonMessage("您未进行预约授权  <a href='" + HOST + "/user/pandect/appointment.do?appointment=1'>去授权 </a> ", "1");
		}
		if (map.get("recodTotal") != null && Integer.parseInt(map.get("recodTotal") + "") >= 12) {
			if (map.get("recodTime") != null) {
				// 违约天数计算
				int recodTime = Integer.parseInt(GetDate.get10Time(map.get("recodTime") + ""));
				int WYtime = 0;
				try {
					WYtime = 60 - GetDate.daysBetween(recodTime, GetDate.getNowTime10());
				} catch (ParseException e) {
					return jsonMessage("处理违约时间失败，请联系客服", "1");
				}
				return jsonMessage("您已列入黑名单，请" + WYtime + "天后再试", "1");
			} else {
				return jsonMessage("您的违约时间数据异常，请联系客服", "1");
			}
		}
		// 判断借款编号是否存在
		if (StringUtils.isEmpty(borrowNid)) {
			return jsonMessage("借款项目不存在", "1");
		}
		// 借款信息
		Borrow borrow = this.getBorrowByNid(borrowNid);
		if (borrow == null || borrow.getId() == null) {
			return jsonMessage("借款项目不存在", "1");
		}
		if (borrow.getUserId() == null) {
			return jsonMessage("借款人不存在", "1");
		}
		if (borrow.getBookingStatus() != 1) {
			return jsonMessage("此项目已经预约完成", "1");
		}
		if (borrow.getBookingEndTime() < GetDate.getNowTime10()) {
			return jsonMessage("此项目已经预约已结束", "1");
		}
		Integer projectType = borrow.getProjectType();// 0，51老用户；1，新用户；2，全部用户
		if (projectType == null) {
			return jsonMessage("未设置该出借项目的项目类型", "1");
		}
		BorrowProjectType borrowProjectType = this.getBorrowProjectType(String.valueOf(projectType));
		if (borrowProjectType == null) {
			return jsonMessage("未查询到该出借项目的配置信息", "1");
		}
		// 51老用户标
		if (borrowProjectType.getInvestUserType().equals("0")) {
			boolean is51User = this.checkIs51UserCanInvest(Integer.parseInt(userId));
			if (!is51User) {
				return jsonMessage("该项目只能51老用户预约", "1");
			}
		}
		if (borrowProjectType.getInvestUserType().equals("1")) {
			boolean newUser = this.checkIsNewUserCanInvest(Integer.parseInt(userId));
			if (!newUser) {
				return jsonMessage("该项目只能新手预约", "1");
			}
		}
		// 用户开户信息
		AccountChinapnr accountChinapnrTender = this.getAccountChinapnr(Integer.parseInt(userId));
		if (accountChinapnrTender == null) {
			return jsonMessage("用户开户信息不存在", "1");
		}
		// 判断借款人开户信息是否存在
		if (accountChinapnrTender.getChinapnrUsrcustid() == null) {
			return jsonMessage("用户汇付客户号不存在", "1");
		}
		if (platform.equals("0") && borrow.getCanTransactionPc().equals("0")) {
			String tmpInfo = "";
			if (borrow.getCanTransactionAndroid().equals("1")) {
				tmpInfo += " Android端  ";
			}
			if (borrow.getCanTransactionIos().equals("1")) {
				tmpInfo += " Ios端  ";
			}
			if (borrow.getCanTransactionWei().equals("1")) {
				tmpInfo += " 微信端  ";
			}
			return jsonMessage("此项目只能在" + tmpInfo + "预约", "1");
		} else if (platform.equals("1") && borrow.getCanTransactionWei().equals("0")) {
			String tmpInfo = "";
			if (borrow.getCanTransactionAndroid().equals("1")) {
				tmpInfo += " Android端  ";
			}
			if (borrow.getCanTransactionIos().equals("1")) {
				tmpInfo += " Ios端  ";
			}
			if (borrow.getCanTransactionPc().equals("1")) {
				tmpInfo += " Pc端  ";
			}
			return jsonMessage("此项目只能在" + tmpInfo + "预约", "1");
		} else if (platform.equals("2") && borrow.getCanTransactionAndroid().equals("0")) {
			String tmpInfo = "";
			if (borrow.getCanTransactionPc().equals("1")) {
				tmpInfo += " PC端  ";
			}
			if (borrow.getCanTransactionIos().equals("1")) {
				tmpInfo += " Ios端  ";
			}
			if (borrow.getCanTransactionWei().equals("1")) {
				tmpInfo += " 微信端  ";
			}
			return jsonMessage("此项目只能在" + tmpInfo + "预约", "1");
		} else if (platform.equals("3") && borrow.getCanTransactionIos().equals("0")) {
			String tmpInfo = "";
			if (borrow.getCanTransactionPc().equals("1")) {
				tmpInfo += " PC端  ";
			}
			if (borrow.getCanTransactionAndroid().equals("1")) {
				tmpInfo += " Android端  ";
			}
			if (borrow.getCanTransactionWei().equals("1")) {
				tmpInfo += " 微信端  ";
			}
			return jsonMessage("此项目只能在" + tmpInfo + "预约", "1");
		}
		// 借款人开户信息
		AccountChinapnr accountChinapnrBorrower = this.getAccountChinapnr(borrow.getUserId());
		if (accountChinapnrBorrower == null) {
			return jsonMessage("借款人未开户", "1");
		}
		if (accountChinapnrBorrower.getChinapnrUsrcustid() == null) {
			return jsonMessage("借款人汇付客户号不存在", "1");
		}
		if (userId.equals(String.valueOf(borrow.getUserId()))) {
			return jsonMessage("借款人不可以自己预约项目", "1");
		}
		// 判断借款是否流标
		if (borrow.getStatus() == 2) { // 流标
			return jsonMessage("此项目已经流标", "1");
		}
		// 已满标
		if (borrow.getBorrowFullStatus() == 1) {
			return jsonMessage("此项目已经满标", "1");
		}
		// 判断用户出借金额是否为空
		if (StringUtils.isEmpty(account)) {
			return jsonMessage("请输入预约金额", "1");
		}
		// 还款金额是否数值
		try {
			// 出借金额必须是整数
			Long accountInt = Long.parseLong(account);
			if (accountInt == 0) {
				return jsonMessage("预约金额不能为0元", "1");
			}
			if (accountInt < 0) {
				return jsonMessage("预约金额不能为负数", "1");
			}
			// 新需求判断顺序变化
			// 将出借金额转化为BigDecimal
			BigDecimal accountBigDecimal = new BigDecimal(account);
			String balance = RedisUtils.get(borrowNid + CustomConstants.APPOINT);
			if (StringUtils.isEmpty(balance)) {
				return jsonMessage("您来晚了，下次再来抢吧", "1");
			}
			// 剩余可投金额
			Integer min = borrow.getTenderAccountMin();
			// 当剩余可投金额小于最低起投金额，不做最低起投金额的限制
			if (min != null && min != 0 && new BigDecimal(balance).compareTo(new BigDecimal(min)) == -1) {
				if (accountBigDecimal.compareTo(new BigDecimal(balance)) == 1) {
					return jsonMessage("剩余可预约金额为" + balance + "元", "1");
				}
				if (accountBigDecimal.compareTo(new BigDecimal(balance)) != 0) {
					return jsonMessage("剩余可预约只剩" + balance + "元，须全部购买", "1");
				}
			} else {// 项目的剩余金额大于最低起投金额
				if (accountBigDecimal.compareTo(new BigDecimal(min)) == -1) {
					return jsonMessage(borrow.getTenderAccountMin() + "元起投", "1");
				} else {
					Integer max = borrow.getTenderAccountMax();
					if (max != null && max != 0 && accountBigDecimal.compareTo(new BigDecimal(max)) == 1) {
						return jsonMessage("项目最大预约额为" + max + "元", "1");
					}
				}
			}
			if (accountBigDecimal.compareTo(borrow.getAccount()) > 0) {
				return jsonMessage("预约金额不能大于项目总额", "1");
			}
			Account tenderAccount = this.getAccount(Integer.parseInt(userId));
			if (tenderAccount.getBalance().compareTo(accountBigDecimal) < 0) {
				return jsonMessage("余额不足，请充值！", "1");
			}
			if (StringUtils.isNotEmpty(balance)) {
				// redis剩余金额不足
				if (accountBigDecimal.compareTo(new BigDecimal(balance)) == 1) {
					return jsonMessage("项目太抢手了！剩余可预约金额只有" + balance + "元", "1");
				} else {
					if (appointCheck == null || !appointCheck.equals("on")) {
						return jsonMessage("请勾选预约需知", "1");
					}
					// 2.根据项目标号获取相应的项目信息
					WebProjectDetailCustomize borrowDetail = webProjectListCustomizeMapper.selectProjectDetail(borrowNid);
					if (borrowDetail.getIncreaseMoney() != null && (accountInt - min) % Integer.parseInt(borrowDetail.getIncreaseMoney()) != 0 && accountBigDecimal.compareTo(new BigDecimal(balance)) == -1) {
						return jsonMessage("预约递增金额须为" + borrowDetail.getIncreaseMoney() + " 元的整数倍", "1");
					}
					// 如果验证没问题，则返回出借人借款人的汇付账号
					Long borrowerUsrcustid = accountChinapnrBorrower.getChinapnrUsrcustid();
					Long tenderUsrcustid = accountChinapnrTender.getChinapnrUsrcustid();
					JSONObject jsonMessage = new JSONObject();
					jsonMessage.put("error", "0");
					jsonMessage.put("borrowerUsrcustid", borrowerUsrcustid);
					jsonMessage.put("tenderUsrcustid", tenderUsrcustid);
					jsonMessage.put("borrowId", borrow.getId());
					jsonMessage.put("bankInputFlag", borrow.getBankInputFlag() + "");
					return jsonMessage;
				}
			} else {
				return jsonMessage("您来晚了，下次再来抢吧", "1");
			}
		} catch (Exception e) {
			return jsonMessage("预约金额必须为整数", "1");
		}
	}

	/**
	 * 用户预约 执行前每个方法前需要添加BusinessDesc描述
	 * 
	 * @param borrowId
	 * @param borrowNid
	 * @param projectType
	 * @param borrowStyle
	 * @param feeRate
	 * @param borrowPeriod
	 * @param borrowAccountWait
	 * @param orderId
	 * @param userId
	 * @param account
	 * @param Usrcustid
	 * @param borrowAccount
	 * @param client
	 * @param ip
	 * @param freezeTrxId
	 * @return
	 * @throws Exception
	 * @author Administrator
	 */
	@Override
	public synchronized Boolean updateAfterAppointRedis(int borrowId, String borrowNid, int projectType, String borrowStyle, BigDecimal feeRate, int borrowPeriod, BigDecimal borrowAccountWait, String orderId, Integer userId, String account, String Usrcustid, BigDecimal borrowAccount,
			Integer client, String ip, String freezeTrxId) throws Exception {

		// 插入冻结表
		FreezeList record = new FreezeList();
		record.setAmount(new BigDecimal(account));
		record.setBorrowNid(borrowNid);
		record.setCreateTime(Integer.valueOf((new Date().getTime() / 1000) + ""));
		record.setOrdid(orderId);
		record.setUserId(userId);
		record.setRespcode("000");
		record.setTrxid(freezeTrxId);
		record.setOrdid(orderId);
		record.setUsrcustid(Usrcustid);
		record.setXfrom(1);
		record.setStatus(0);
		record.setUnfreezeManual(0);
		System.out.println("预约用户:" + userId + "***********************************预插入FreezeList：" + JSON.toJSONString(record));
		boolean freezeFlag = freezeListMapper.insertSelective(record) > 0 ? true : false;
		if (freezeFlag) {
			BigDecimal accountDecimal = new BigDecimal(account);
			BorrowAppoint borrowAppoint = new BorrowAppoint();
			borrowAppoint.setAccount(accountDecimal);
			borrowAppoint.setUserId(userId);
			borrowAppoint.setOrderId(orderId);
			borrowAppoint.setBorrowNid(borrowNid);
			borrowAppoint.setBorrowPeriod(borrowPeriod);
			borrowAppoint.setBorrowApr(feeRate);
			borrowAppoint.setBorrowAccount(borrowAccount);
			borrowAppoint.setAppointStatus(1);
			borrowAppoint.setAppointTime(new Date());
			borrowAppoint.setCreateTime(new Date());
			borrowAppoint.setCreateUserId(userId);
			System.out.println("用户:" + userId + "***********************************预插入borrowAppoint：" + JSON.toJSONString(borrowAppoint));
			boolean trenderFlag = borrowAppointMapper.insertSelective(borrowAppoint) > 0 ? true : false;
			if (trenderFlag) {
				// 新出借金额汇总
				AccountExample example = new AccountExample();
				AccountExample.Criteria criteria = example.createCriteria();
				criteria.andUserIdEqualTo(userId);
				List<Account> list = accountMapper.selectByExample(example);
				if (list != null && list.size() == 1) {
					// 总金额
					BigDecimal total = list.get(0).getTotal();
					// 冻结金额
					BigDecimal frost = list.get(0).getFrost();
					// 可用金额
					BigDecimal balance = list.get(0).getBalance();
					// 更新用户账户余额表
					Account accountBean = new Account();
					accountBean.setUserId(userId);
					accountBean.setFrost(accountDecimal);
					// 出借人可用余额
					accountBean.setBalance(accountDecimal);
					// 出借人待收金额
					accountBean.setInMoney(new BigDecimal(0));
					Boolean accountFlag = this.adminAccountCustomizeMapper.updateOfTender(accountBean) > 0 ? true : false;
					// 插入account_list表
					if (accountFlag) {
						AccountList accountList = new AccountList();
						accountList.setAmount(accountDecimal);
						accountList.setAwait(new BigDecimal(0));
						accountList.setBalance(balance.subtract(accountDecimal));
						accountList.setBaseUpdate(0);
						accountList.setCreateTime(Integer.valueOf((new Date().getTime() / 1000) + ""));
						accountList.setFrost(frost.add(accountDecimal));
						accountList.setInterest(new BigDecimal(0));
						accountList.setIp(ip);
						accountList.setIsUpdate(0);
						accountList.setNid(orderId);
						accountList.setOperator(userId + "");
						accountList.setRemark(borrowNid);
						accountList.setRepay(new BigDecimal(0));
						accountList.setTotal(total);
						accountList.setTrade("appoint_freeze");// 出借
						accountList.setTradeCode("frost");//
						accountList.setType(2);// 收支类型1收入2支出3冻结
						accountList.setUserId(userId);
						accountList.setWeb(0);
						System.out.println("预约用户:" + userId + "***********************************预插入accountList：" + JSON.toJSONString(accountList));
						boolean accountListFlag = accountListMapper.insertSelective(accountList) > 0 ? true : false;
						if (accountListFlag) {
							// 更新borrow表
							Map<String, Object> borrow = new HashMap<String, Object>();
							borrow.put("borrowAccountWaitAppoint", accountDecimal);
							borrow.put("borrowId", borrowId);
							boolean updateBorrowAccountFlag = borrowCustomizeMapper.updateOfBorrowAppoint(borrow) > 0 ? true : false;
							if (updateBorrowAccountFlag) {
								// 计算此时的剩余可出借金额
								BigDecimal accountWait = borrowAccountWait.subtract(accountDecimal);
								// 满标处理
								if (accountWait.compareTo(new BigDecimal(0)) == 0) {
									System.out.println("预约用户:" + userId + "***********************************项目满标");
									Map<String, Object> borrowFull = new HashMap<String, Object>();
									borrowFull.put("borrowId", borrowId);
									borrowCustomizeMapper.updateOfFullBorrowAppoint(borrowFull);
								} else if (accountWait.compareTo(new BigDecimal(0)) < 0) {
									System.out.println("预约用户:" + userId + "项目编号:" + borrowNid + "***********************************项目预约暴标");
									throw new RuntimeException("没有可预约金额");
								}
								String tenderId = orderId.substring(10);
								// 扣除可用金额
								AccountLog accountLog = new AccountLog();
								accountLog.setUserId(userId);// 操作用户id
								accountLog.setNid("tender_frost_" + userId + "_" + borrowNid + "_" + tenderId);
								accountLog.setTotalOld(BigDecimal.ZERO);
								accountLog.setCode("tender");
								accountLog.setCodeType("appoint");
								accountLog.setCodeNid(tenderId);
								accountLog.setBorrowNid(borrowNid);// 收入
								accountLog.setIncomeOld(BigDecimal.ZERO);
								accountLog.setIncomeNew(BigDecimal.ZERO);
								accountLog.setAccountWebStatus(0);
								accountLog.setAccountUserStatus(0);
								accountLog.setAccountType("");
								accountLog.setMoney(accountDecimal);// 操作金额
								accountLog.setIncome(BigDecimal.ZERO);// 收入
								accountLog.setExpend(BigDecimal.ZERO);// 支出
								accountLog.setExpendNew(BigDecimal.ZERO);
								accountLog.setBalanceOld(BigDecimal.ZERO);
								accountLog.setBalanceNew(BigDecimal.ZERO);
								accountLog.setBalanceCash(BigDecimal.ZERO);
								accountLog.setBalanceCashNew(BigDecimal.ZERO);
								accountLog.setBalanceCashOld(BigDecimal.ZERO);
								accountLog.setExpendOld(BigDecimal.ZERO);
								accountLog.setBalanceCash(BigDecimal.ZERO);// 可提现金额
								accountLog.setBalanceFrost(accountDecimal.multiply(new BigDecimal(-1)));// 不可提现金额
								accountLog.setFrost(frost);// 冻结金额
								accountLog.setFrostOld(BigDecimal.ZERO);
								accountLog.setFrostNew(BigDecimal.ZERO);
								accountLog.setAwait(BigDecimal.ZERO);// 待收金额
								accountLog.setRepay(BigDecimal.ZERO);// 待还金额
								accountLog.setRepayOld(BigDecimal.ZERO);
								accountLog.setRepayNew(BigDecimal.ZERO);
								accountLog.setAwait(BigDecimal.ZERO);
								accountLog.setAwaitNew(BigDecimal.ZERO);
								accountLog.setAwaitOld(BigDecimal.ZERO);
								accountLog.setType("appoint");// 类型
								accountLog.setToUserid(borrowPeriod); // 付给谁
								accountLog.setRemark("预约[" + borrowNid + "]所冻结资金");// 备注
								accountLog.setAddtime(String.valueOf(GetDate.getNowTime10()));
								accountLog.setAddip(ip);
								accountLog.setBalanceFrostNew(BigDecimal.ZERO);
								accountLog.setBalanceFrostOld(BigDecimal.ZERO);
								System.out.println("预约用户:" + userId + "***********************************预插入accountLog：" + JSON.toJSONString(accountLog));
								boolean accountLogFlag = this.accountLogMapper.insertSelective(accountLog) > 0 ? true : false;
								if (accountLogFlag) {
									return true;
								} else {
									throw new RuntimeException("accountLog表更新失败");
								}
							} else {
								throw new RuntimeException("borrow表更新失败");
							}
						} else {
							throw new RuntimeException("用户账户交易明细表更新失败");
						}
					} else {
						throw new RuntimeException("用户账户信息表更新失败");
					}
				} else {
					throw new RuntimeException("用户账户信息查询失败！");
				}
			} else {
				throw new RuntimeException("插入预约表失败！");
			}
		} else {
			throw new RuntimeException("插入冻结表失败！");
		}
	}

	@Override
	public boolean checkIfSendCoupon(Users user, String account) {
		// TODO 预留活动id
		int activityId = Integer.parseInt(CustomConstants.INVEST_ACTIVITY_ID);
		ActivityList activityList = activityListMapper.selectByPrimaryKey(activityId);
		if (activityList == null) {
			return false;
		}
		Integer timeStart = activityList.getTimeStart();
		Integer timeEnd = activityList.getTimeEnd();
		if (timeStart > GetDate.getNowTime10()) {
			return false;
		}
		if (timeEnd != null && timeEnd < GetDate.getNowTime10()) {
			return false;
		}
		BigDecimal accountBigDecimal = BigDecimal.ZERO;
		if (account == null) {
			return false;
		} else {
			accountBigDecimal = new BigDecimal(account);
		}
		if (accountBigDecimal.compareTo(new BigDecimal("1000")) != -1) {
			// 判断推荐人
			SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
			SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
			spreadsUsersExampleCriteria.andUserIdEqualTo(user.getUserId());
			List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
			if (sList != null && sList.size() == 1) {
				int refUserId = sList.get(0).getSpreadsUserid();
				// 判断是否是投之家来的
				if (refUserId != 111734) {
					return false;
				} else {
					CouponUserExample example = new CouponUserExample();
					CouponUserExample.Criteria cra = example.createCriteria();
					cra.andUserIdEqualTo(user.getUserId());
					cra.andActivityIdEqualTo(activityId);
					cra.andDelFlagEqualTo(CustomConstants.FALG_NOR);
					int couponCount = couponUserMapper.countByExample(example);
					if (couponCount > 0) {
						return false;
					} else {
						return true;
					}
				}

			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public void extraUeldInvest(Borrow borrow, ChinapnrBean bean) {
		// 操作ip
		String ip = bean.getLogIp();
		// 操作平台
		int client = StringUtils.isNotBlank(bean.getLogClient()) ? Integer.parseInt(bean.getLogClient()) : 0;
		// 出借人id
		Integer userId = bean.getLogUserId();
		// 借款金额
		String account = bean.getTransAmt();
		// 订单id
		String tenderOrderId = bean.getOrdId();
		// 项目编号
		String borrowNid = borrow.getBorrowNid();
		// 项目的还款方式
		String borrowStyle = borrow.getBorrowStyle();
		BorrowStyle borrowStyleMain = this.getborrowStyleByNid(borrowStyle);
		String borrowStyleName = borrowStyleMain.getName();
		// 借款期数
		Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();
		Users users = this.getUsers(userId);
		// 生成额外利息订单
		String orderId = GetOrderIdUtils.getOrderId2(Integer.valueOf(userId));
		// 根据orderid查询出借信息tender
		BorrowTender tender = this.getBorrowTenderByNidUserIdBorrowNid(tenderOrderId, userId, borrowNid);
		if (tender != null) {

			IncreaseInterestInvest increaseInterestInvest = new IncreaseInterestInvest();
			increaseInterestInvest.setUserId(userId);
			increaseInterestInvest.setInvestUserName(users.getUsername());
			increaseInterestInvest.setTenderId(tender.getId());
			increaseInterestInvest.setTenderNid(tenderOrderId);
			increaseInterestInvest.setBorrowNid(borrowNid);
			increaseInterestInvest.setBorrowApr(borrow.getBorrowApr());
			increaseInterestInvest.setBorrowExtraYield(borrow.getBorrowExtraYield());
			increaseInterestInvest.setBorrowPeriod(borrowPeriod);
			increaseInterestInvest.setBorrowStyle(borrowStyle);
			increaseInterestInvest.setBorrowStyleName(borrowStyleName);
			increaseInterestInvest.setOrderId(orderId);
			increaseInterestInvest.setOrderDate(GetDate.getServerDateTime(10, new Date()));
			increaseInterestInvest.setAccount(new BigDecimal(account));
			increaseInterestInvest.setStatus(0);
			increaseInterestInvest.setWeb(0);
			increaseInterestInvest.setClient(client);
			increaseInterestInvest.setAddip(ip);
			increaseInterestInvest.setRemark("产品加息");
			increaseInterestInvest.setInvestType(0);
			increaseInterestInvest.setCreateTime(GetDate.getNowTime10());
			increaseInterestInvest.setCreateUserId(userId);
			increaseInterestInvest.setCreateUserName(users.getUsername());
			boolean incinvflag = increaseInterestInvestMapper.insertSelective(increaseInterestInvest) > 0 ? true : false;
			if (!incinvflag) {
				System.err.println("融通宝出借额外利息出借失败，插入额外出借信息失败,出借订单号:" + tenderOrderId);
			}
		} else {
			System.err.println("融通宝出借额外利息出借失败，获取出借信息失败,出借订单号:" + tenderOrderId);
		}

	}

	private BorrowStyle getborrowStyleByNid(String borrowStyle) {
		BorrowStyleExample example = new BorrowStyleExample();
		BorrowStyleExample.Criteria cri = example.createCriteria();
		cri.andNidEqualTo(borrowStyle);
		List<BorrowStyle> style = borrowStyleMapper.selectByExample(example);
		return style.get(0);
	}

	private BorrowTender getBorrowTenderByNidUserIdBorrowNid(String orderId, Integer userId, String borrowNid) { // 删除临时表
		BorrowTenderExample borrowTenderExample = new BorrowTenderExample();
		BorrowTenderExample.Criteria criteria1 = borrowTenderExample.createCriteria();
		criteria1.andNidEqualTo(orderId);
		criteria1.andUserIdEqualTo(userId);
		criteria1.andBorrowNidEqualTo(borrowNid);
		List<BorrowTender> tenderList = borrowTenderMapper.selectByExample(borrowTenderExample);
		if (tenderList != null && tenderList.size() > 0) {
			return tenderList.get(0);
		} else {
			return null;
		}
	}

    @Override
    public BorrowProjectType getProjectTypeByBorrowNid(String borrowNid) {
        BorrowProjectTypeExample example = new BorrowProjectTypeExample();
        BorrowProjectTypeExample.Criteria cra = example.createCriteria();
        cra.andStatusEqualTo(CustomConstants.FLAG_NORMAL).andBorrowClassEqualTo(borrowNid.substring(0, 3));
        List<BorrowProjectType> borrowProjectTypes = this.borrowProjectTypeMapper.selectByExample(example);
        BorrowProjectType borrowProjectType = new BorrowProjectType();
        if (borrowProjectTypes != null && borrowProjectTypes.size() > 0) {
            borrowProjectType = borrowProjectTypes.get(0);
        }
        return borrowProjectType;
    }

}