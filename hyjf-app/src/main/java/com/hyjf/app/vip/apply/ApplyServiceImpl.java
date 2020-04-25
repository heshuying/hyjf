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

package com.hyjf.app.vip.apply;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseServiceImpl;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.AppMsMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.AccountChinapnrExample;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AccountWebList;
import com.hyjf.mybatis.model.auto.AccountWebListExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.mybatis.model.auto.VipInfo;
import com.hyjf.mybatis.model.auto.VipInfoExample;
import com.hyjf.mybatis.model.auto.VipTransferLog;
import com.hyjf.mybatis.model.auto.VipUserUpgrade;
import com.hyjf.mybatis.model.auto.VipUserUpgradeExample;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;

@Service
public class ApplyServiceImpl extends BaseServiceImpl implements ApplyService {
    
    @Autowired
    @Qualifier("appMsProcesser")
    private MessageProcesser appMsProcesser;
    	@Autowired
	DataSourceTransactionManager transactionManager;	/**
	 * 用户购买vip
	 */
	@Override
	public synchronized JSONObject updateUserVip(Integer userId,BigDecimal transAmt,HttpServletRequest request,String ordId,String usrCustId,String merCustId, String platform) throws Exception {
		LogUtil.startLog(ApplyServiceImpl.class.toString(), "updateUserVip", "用户购买vip");
		// 手动事务，排他校验
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		// 事物隔离级别，开启新事务
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); 
		// 获得事务状态
		TransactionStatus status = transactionManager.getTransaction(def); 
		int nowTime = GetDate.getNowTime10();
		JSONObject result = null;
		UsersInfoExample example = new UsersInfoExample();
		UsersInfoExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		List<UsersInfo> listUsersInfo = this.usersInfoMapper.selectByExample(example);
		UsersInfo usersInfo = listUsersInfo != null && listUsersInfo.size() > 0 ? listUsersInfo.get(0) : null;
		int vipId = Integer.MIN_VALUE;
		String vipName = "";
		if (usersInfo != null) {
			VipInfoExample vipInfoExample = new VipInfoExample();
			VipInfoExample.Criteria criteriaVipInfo = vipInfoExample.createCriteria();
			// VIP1
			criteriaVipInfo.andVipLevelEqualTo(ApplyDefine.INT_ONE);
			criteriaVipInfo.andDelFlgEqualTo(ApplyDefine.INT_ZERO);
			List<VipInfo> listVipInfo = this.vipInfoMapper.selectByExample(vipInfoExample);
			if (listVipInfo != null && listVipInfo.size() > 0) {
				VipInfo vipInfo = listVipInfo.get(0);
				// VIP1的编号
				vipId = vipInfo.getId();
				vipName = vipInfo.getVipName();
				usersInfo.setVipId(vipId);
			}
			// V值0
			usersInfo.setVipValue(ApplyDefine.INT_ZERO);
			
			// 计算vip有效期  当前时间+1年
			Date date = GetDate.countDate(1, 1);
			String startDateTime = GetDate.get10Time(GetDate.dateToString2(date),24*3600-1);
			// vip有效期
			usersInfo.setVipExpDate(Integer.valueOf(startDateTime));
			System.out.println(StringUtils.isNotEmpty(platform)+"     "+platform);
			//记录vip购买平台
			if(StringUtils.isNotEmpty(platform)){
			    
                usersInfo.setVipPlatform(new Integer(platform));
            }
			this.usersInfoMapper.updateByPrimaryKeySelective(usersInfo);
			result = new JSONObject();
			// VIP编号
			result.put("vipId", vipId);
			
			// 插入转账记录
			VipTransferLog vipTransferLog = new VipTransferLog();
			// 用户编号
			vipTransferLog.setUserId(userId);
			// vip编号
			vipTransferLog.setVipId(vipId);
			// 订单编号
			vipTransferLog.setOrdId(ordId);
			// 转入账号
			vipTransferLog.setTransferInCustid(merCustId);
			// 转出账号
			vipTransferLog.setTransferOutCustid(usrCustId);
			// 转账金额
			vipTransferLog.setTransAmt(transAmt);
			// 转账类型
			vipTransferLog.setTransType(1);
			// 转账状态
			vipTransferLog.setTransStatus(0);
			// 转账时间
			vipTransferLog.setTransTime(nowTime);
			vipTransferLog.setAddTime(nowTime);
			vipTransferLog.setAddUser(String.valueOf(userId));
			vipTransferLog.setUpdateTime(nowTime);
			vipTransferLog.setUpdateUser(String.valueOf(userId));
			vipTransferLog.setDelFlag(0);
			this.vipTransferLogMapper.insertSelective(vipTransferLog);
			
			// 账户信息表
			AccountExample accountExample = new AccountExample();
			AccountExample.Criteria accountCriteria = accountExample.createCriteria();
			accountCriteria.andUserIdEqualTo(userId);
			List<Account> list = accountMapper.selectByExample(accountExample);
			if (list != null && list.size() == 1) {
				Account accountObject = list.get(0);
				// 总金额
				BigDecimal total = accountObject.getTotal();
				total = total == null ? BigDecimal.ZERO : total;
				// 可用金额
				BigDecimal balance = accountObject.getBalance();
				balance = balance == null ? BigDecimal.ZERO : balance;
				// 累加到总支出
				BigDecimal expand = accountObject.getExpend().add(transAmt);
				expand = expand == null ? BigDecimal.ZERO : expand;
				expand = expand.add(transAmt);
				// 更新用户账户余额表
				accountObject.setTotal(total.subtract(transAmt));
				// 出借人可用余额
				accountObject.setBalance(balance.subtract(transAmt));
				// 出借人待收金额
				accountObject.setExpend(expand);
				AccountExample updateAccountExample = new AccountExample();
				updateAccountExample.createCriteria().andUserIdEqualTo(userId);
				// 更新用户账户信息
				this.accountMapper.updateByExampleSelective(accountObject, updateAccountExample);
				// 账户明细表
				AccountList accountList = new AccountList();
				accountList.setAmount(transAmt);
				accountList.setAwait(BigDecimal.ZERO);
				accountList.setBalance(balance.subtract(transAmt));
				accountList.setBaseUpdate(0);
				accountList.setCreateTime(Integer.valueOf((new Date().getTime() / 1000) + ""));
				accountList.setFrost(accountObject.getFrost());
				accountList.setInterest(BigDecimal.ZERO);
				accountList.setIp(GetCilentIP.getIpAddr(request));
				accountList.setIsUpdate(0);
				accountList.setNid(ordId);
				accountList.setOperator(userId + "");
				accountList.setRemark("购买会员资格");
				accountList.setRepay(BigDecimal.ZERO);
				accountList.setTotal(total.subtract(transAmt));
				// 购买VIP
				accountList.setTrade("apply_vip");
				// 冻结操作
				accountList.setTradeCode("balance");
				// 收支类型1收入2支出3冻结
				accountList.setType(2);
				accountList.setUserId(userId);
				accountList.setWeb(0);
				this.accountListMapper.insertSelective(accountList);
				
				// 插入网站收支明细记录
		        AccountWebList accountWebList = new AccountWebList();
		        accountWebList.setOrdid(ordId);// 订单号
		        accountWebList.setUserId(userId); // 出借者
		        accountWebList.setTruename(usersInfo.getTruename());
		        accountWebList.setAmount(transAmt); // 优惠券出借受益
		        accountWebList.setType(CustomConstants.TYPE_IN); // 类型1收入,2支出
		        accountWebList.setTrade(CustomConstants.APPLY_VIP); // 优惠券收益费
		        accountWebList.setTradeType(CustomConstants.APPLY_VIP_REMARK); // 购买vip
		        accountWebList.setRemark("会员购买"); // 出借编号
		        accountWebList.setCreateTime(nowTime);
		        insertAccountWebList(accountWebList);

		        // 排他校验，判断用户是否已经购买过会员
		        VipUserUpgradeExample exampleCheck = new VipUserUpgradeExample();
		        VipUserUpgradeExample.Criteria criteriaCheck = exampleCheck.createCriteria();
		        criteriaCheck.andUserIdEqualTo(userId);
		        int vip = this.vipUserUpgradeMapper.countByExample(exampleCheck);
		        if(vip > 0){
		        	// 用户已经是会员,事务回滚
		        	transactionManager.rollback(status);
		        	result.put("upgradeId", -1);
		        	return result;
		        }
		        // vip成长表
		        VipUserUpgrade vipUserUpgrade = new VipUserUpgrade();
		        vipUserUpgrade.setUserId(userId);
		        vipUserUpgrade.setVipId(vipId);
		        vipUserUpgrade.setUpgradeVipValue(0);
		        // 1：购买，2：V值升级
		        vipUserUpgrade.setUpgradeVipType(1);
		        // 0：未发放，1：已发放
		        vipUserUpgrade.setGiftFlg(0);
		        vipUserUpgrade.setRemark("会员购买");
		        vipUserUpgrade.setAddTime(nowTime);
		        vipUserUpgrade.setAddUser(String.valueOf(userId));
		        vipUserUpgrade.setUpdateTime(nowTime);
		        vipUserUpgrade.setUpdateUser(String.valueOf(userId));
		        vipUserUpgrade.setDelFlg(0);
		        // 插入vip成长表
		        this.vipUserUpgradeMapper.insertSelective(vipUserUpgrade);
		        transactionManager.commit(status);
		        result.put("upgradeId", vipUserUpgrade.getId());
		        
			}
		//发送push消息
			System.out.println("----------------购买vip成功push消息开始-----------------");			Map<String, String> param = new HashMap<String, String>();
			if(StringUtils.isEmpty(usersInfo.getTruename())){
			    param.put("val_name", "");
			}else if(usersInfo.getTruename().length() > 1){
			    param.put("val_name", usersInfo.getTruename().substring(0, 1));
			}else{
			    param.put("val_name", usersInfo.getTruename());
			}
			
			if(usersInfo.getSex() == 1){
			    param.put("val_sex", "先生");
			}else if(usersInfo.getSex() == 2){
			    param.put("val_sex", "女士");
			}else{
			    param.put("val_sex", "");
			}
            param.put("val_vip_grade", vipName);
            System.out.println("请求参数userid：" + userId + " param: " + param);
            AppMsMessage appMsMessage = new AppMsMessage(userId, param, null, MessageDefine.APPMSSENDFORUSER, CustomConstants.JYTZ_VIP_BECOMEVIP);
            appMsProcesser.gather(appMsMessage);
            System.out.println("----------------购买vip成功push消息结束-----------------");

			return result;
		}
		LogUtil.endLog(ApplyServiceImpl.class.toString(), "updateUserVip");
		return result;
	}
	
	/**
     * 插入网站收支记录
     *
     * @param nid
     * @return
     */
    private int insertAccountWebList(AccountWebList accountWebList) {
        if (countAccountWebList(accountWebList.getOrdid(), accountWebList.getTrade()) == 0) {
            // 插入
            return this.accountWebListMapper.insertSelective(accountWebList);
        }
        return 0;
    }
    
    /**
     * 判断网站收支是否存在
     *
     * @param nid
     * @return
     */
    private int countAccountWebList(String nid, String trade) {
        AccountWebListExample example = new AccountWebListExample();
        example.createCriteria().andOrdidEqualTo(nid).andTradeEqualTo(trade);
        return this.accountWebListMapper.countByExample(example);
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
	 * 检查参数的正确性
	 * 
	 * @param userId
	 * @param transAmt
	 *            交易金额
	 * @param flag
	 *            交易类型，1购买 2赎回
	 * @return
	 */
	public JSONObject checkParam(String account, Integer userIdInt) throws Exception {

		if (userIdInt == null) {
			return jsonMessage("您未登陆，请先登录", "1");
		}
		AccountChinapnr accountChinapnrBorrower = this.getAccountChinapnr(userIdInt);
		if (accountChinapnrBorrower == null) {
			return jsonMessage("借款人未开户", "2");
		}
		// 如果验证没问题，则返回用户的汇付账号
		Long usrcustid = accountChinapnrBorrower.getChinapnrUsrcustid();
		ChinapnrBean bean = new ChinapnrBean();
		bean.setVersion("10");
		bean.setCmdId("QueryBalanceBg");
		bean.setMerCustId(PropUtils.getSystem(ChinaPnrConstant.PROP_MERCUSTID));
		bean.setUsrCustId(String.valueOf(usrcustid));
		ChinapnrBean result = ChinapnrUtil.callApiBg(bean);
		System.out.println("查询用户账户余额返回结果："+result);
		if (result != null && StringUtils.equals("000", result.RespCode)) {
			String acctBalStr = result.getAvlBal() != null ? result.getAvlBal() : "0.00";
			acctBalStr = StringUtils.replace(acctBalStr,",",StringUtils.EMPTY);
			// 账户余额
			BigDecimal acctBal = new BigDecimal(acctBalStr);
			System.out.println("查询用户账户余额："+acctBal);
			// 购买vip金额
			BigDecimal accountVip = new BigDecimal(account);
			if (acctBal.compareTo(accountVip) < 0) {
				// 账户余额小于购买金额
				return jsonMessage("账户余额不足", "3");
			}

		}
		UsersInfo usersInfo = this.getUsersInfoByUserId(userIdInt);
		if(usersInfo.getVipId() != null){
			// 用户已经是vip
			return jsonMessage("用户已经购买过VIP", "4");
		}
		JSONObject jsonMessage = new JSONObject();
		jsonMessage.put("error", "0");
		jsonMessage.put("usrcustid", usrcustid);
		return jsonMessage;
	}

	/**
	 * 组成返回信息
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
	 * 更新礼包发放状态
	 */
	@Override
	public void updateGiftStatus(int userId,int upgradeId) throws Exception {
		int nowTime = GetDate.getNowTime10();
		VipUserUpgrade upgrade = new VipUserUpgrade();
		upgrade.setId(upgradeId);
		// 0：未发放，1：已发放
		upgrade.setGiftFlg(1);
		upgrade.setUpdateTime(nowTime);
		upgrade.setUpdateUser(CustomConstants.OPERATOR_AUTO_GIFT);
		upgrade.setDelFlg(0);
		this.vipUserUpgradeMapper.updateByPrimaryKeySelective(upgrade);
	}

	/**
	 * 取得vip信息
	 */
	@Override
	public VipInfo getVipInfo(int vipId) {
		VipInfo vipInfo = this.vipInfoMapper.selectByPrimaryKey(vipId);
		return vipInfo;
	}

}
