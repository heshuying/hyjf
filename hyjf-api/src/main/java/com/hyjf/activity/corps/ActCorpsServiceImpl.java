package com.hyjf.activity.corps;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CreateUUID;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AccountWebList;
import com.hyjf.mybatis.model.auto.ActdecCorps;
import com.hyjf.mybatis.model.auto.ActdecCorpsExample;
import com.hyjf.mybatis.model.auto.ActdecWinning;
import com.hyjf.mybatis.model.auto.ActdecWinningExample;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.auto.BankMerchantAccount;
import com.hyjf.mybatis.model.auto.BankMerchantAccountExample;
import com.hyjf.mybatis.model.auto.BankMerchantAccountList;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.TransferExceptionLog;
import com.hyjf.mybatis.model.auto.TransferExceptionLogExample;
import com.hyjf.mybatis.model.auto.TransferExceptionLogWithBLOBs;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.mybatis.model.customize.UserInfoCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;


@Service
public class ActCorpsServiceImpl extends BaseServiceImpl implements ActCorpsService{
	Logger _log = LoggerFactory.getLogger(ActCorpsServiceImpl.class);
	private static final String THIS_CLASS = ActCorpsServiceImpl.class.getName();
    /**
     * 获取活动时间
     * 
     * @return
     */
	@Override
    public ActivityList getActivityDate(int id) {
    	
        return activityListMapper.selectByPrimaryKey(id);
    }

	@Override
	public List<ActdecCorps> getActdecCorps(String opId) {
		List<ActdecCorps> ac;
		ActdecCorpsExample  example = new ActdecCorpsExample();
		example.or().andCaptainOpidEqualTo(opId);
		example.setOrderByClause(" corps_name asc ");
		ac=actdecCorpsMapper.selectByExample(example);
		ActdecCorpsExample  example1 = new ActdecCorpsExample();
		example1.or().andMember1OpidEqualTo(opId);
		ac.addAll(actdecCorpsMapper.selectByExample(example1));
		ActdecCorpsExample  example2 = new ActdecCorpsExample();
		example2.or().andMember2OpidEqualTo(opId);
		ac.addAll(actdecCorpsMapper.selectByExample(example2));
		return ac;
	}

	@Override
	public int addActdecCorps(String opId, String name, String head) {
		ActdecCorpsExample  example = new ActdecCorpsExample();
		example.or().andCaptainOpidEqualTo(opId);
		example.setOrderByClause(" corps_name desc ");
		//查询本人战队列表
		List<ActdecCorps> ac=actdecCorpsMapper.selectByExample(example);
		ActdecCorps record = new ActdecCorps();
		record.setPrizeType(0);
		record.setTeamType(1);
		//插入一条战队
		if(ac.isEmpty()) {
			record.setCaptainHead(head);
			record.setCaptainName(name);
			record.setCaptainOpid(opId);
			record.setCorpsName(1);
			record.setCreateTime(GetDate.getNowTime10());
			actdecCorpsMapper.insert(record);
			List<ActdecCorps> acc=actdecCorpsMapper.selectByExample(example);
			return acc.get(0).getId();
		}else if(ac.size()==10) {
			return -1;
		}else {
			record.setCaptainHead(head);
			record.setCaptainName(name);
			record.setCaptainOpid(opId);
			record.setCorpsName((ac.get(0).getCorpsName()+1));
			record.setCreateTime(GetDate.getNowTime10());
			actdecCorpsMapper.insert(record);
			List<ActdecCorps> acc=actdecCorpsMapper.selectByExample(example);
			return acc.get(0).getId();
		}

	}

	@Override
	public ActdecCorps joinActdecCorps(String id, String opId, String name, String head) {
		
		ActdecCorpsExample  example1 = new ActdecCorpsExample();
		example1.or().andMember1OpidEqualTo(opId);
		ActdecCorps acc=new ActdecCorps();
		
		if(!actdecCorpsMapper.selectByExample(example1).isEmpty()) {
			acc.setId(-1);
			return acc;
					//"每个人只能参加1个战队";
		}
		ActdecCorpsExample  example2 = new ActdecCorpsExample();
		example2.or().andMember2OpidEqualTo(opId);
		if(!actdecCorpsMapper.selectByExample(example2).isEmpty()) {
			acc.setId(-1);
			return acc;
					//"每个人只能参加1个战队";
		}
		
		ActdecCorps ac=new ActdecCorps();
		ac = actdecCorpsMapper.selectByPrimaryKey(Integer.valueOf(id));
		if(ac.getTeamType()==1) {//战队1个人
			ac.setMember1Head(head);
			ac.setMember1Name(name);
			ac.setMember1Opid(opId);
			ac.setTeamType(2);
			ac.setUpdateTime(GetDate.getNowTime10());
			actdecCorpsMapper.updateByPrimaryKey(ac);
			return ac;
		}else if(ac.getTeamType()==2) {//战队2个人
			ac.setMember2Head(head);
			ac.setMember2Name(name);
			ac.setMember2Opid(opId);
			ac.setTeamType(3);
			ac.setUpdateTime(GetDate.getNowTime10());
			actdecCorpsMapper.updateByPrimaryKey(ac);
			return ac;
		}else {//战队满员
			acc.setId(-2);
			return acc;
		}
	}
	@Override
	public List<ActdecWinning> getActdecWinning(String corpsId) {
		//获取该战队领奖历史
		ActdecWinningExample  example = new ActdecWinningExample();
		example.or().andCorpsIdEqualTo(Integer.valueOf(corpsId));
		return actdecWinningMapper.selectByExample(example);
	}

	@Override
	public synchronized String addActdecWinning(ActdecWinning aw,int userId,UsersInfo uio) {
		if (!RedisUtils.exists("ACTTOTALmoney")) {
			RedisUtils.set("ACTTOTALmoney", String.valueOf(aw.getAmount()));
		}else {
			int tot=Integer.valueOf(RedisUtils.get("ACTTOTALmoney"))+aw.getAmount();
			if(tot>Integer.valueOf(RedisUtils.get("ACTMAXmoney"))) {
				return "红包发放失败,请联系客服";
			}
			RedisUtils.set("ACTTOTALmoney", String.valueOf(tot));
		}
		ActdecWinningExample example=new ActdecWinningExample();
		com.hyjf.mybatis.model.auto.ActdecWinningExample.Criteria criteria = example.createCriteria();
		criteria.andCorpsIdEqualTo(aw.getCorpsId());
		criteria.andUserIdEqualTo(aw.getUserId());
		
		if(actdecWinningMapper.selectByExample(example).isEmpty()) {
			actdecWinningMapper.insert(aw);
		}else {
			return "不可重复领奖";
		}
		if(aw.getWinningType()==2) {
			return "";
		}

	    BankOpenAccount bankOpenAccountInfo = this.getBankOpenAccount(userId);
        if (bankOpenAccountInfo == null) {
            return "未开户";
        }
        

        
        ///////////////////////////////////////////////////////////////////////转账开始
        String orderId = GetOrderIdUtils.getOrderId2(Integer.valueOf(userId));
		String seqNo = GetOrderIdUtils.getSeqNo(6);
		BankCallBean bean = new BankCallBean();
        String merrpAccount = PropUtils.getSystem(BankCallConstant.BANK_MERRP_ACCOUNT);
	            
        bean.setVersion(BankCallConstant.VERSION_10);// 版本号
        bean.setTxCode(BankCallMethodConstant.TXCODE_VOUCHER_PAY);// 交易代码
        bean.setTxDate(GetOrderIdUtils.getTxDate()); // 交易日期
        bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
        bean.setSeqNo(seqNo);// 交易流水号
        bean.setChannel(BankCallConstant.CHANNEL_PC); // 交易渠道
        bean.setAccountId(merrpAccount);// 电子账号
        bean.setTxAmount(CustomUtil.formatAmount(String.valueOf(aw.getAmount())));
        bean.setForAccountId(bankOpenAccountInfo.getAccount());
        bean.setDesLineFlag("1");
        bean.setDesLine(orderId);
        bean.setLogOrderId(orderId);// 订单号
        bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
        bean.setLogUserId(String.valueOf(userId));
        bean.setLogClient(0);// 平台
        int transferStatus = Integer.MIN_VALUE;
        BankCallBean resultBean = null;
        try {
            resultBean = BankCallUtils.callApiBg(bean);
            if (resultBean == null || !BankCallConstant.RESPCODE_SUCCESS.equals(resultBean.getRetCode())) {
                // 转账失败
                String errorMsg = StringUtils.EMPTY;
 
                

                if(resultBean != null && Validator.isNotNull(resultBean.getRetMsg())){
                    errorMsg = resultBean.getRetMsg();
                }
                
                if(resultBean != null && (BankCallConstant.RETCODE_BIDAPPLY_YUE_FAIL.equals(resultBean.getRetCode()) || BankCallConstant.RETCODE_YUE_FAIL.equals(resultBean.getRetCode()))){
                    errorMsg = "商户子账户余额不足，请先充值或向该子账户转账";
                }
                
                transferStatus = 1;
                // 插入转账异常
                this.insertTransferExceptionLog(bean, resultBean, userId,new BigDecimal(aw.getAmount()), bankOpenAccountInfo.getAccount(), 0, transferStatus,
                        errorMsg, 2);
                LogUtil.errorLog(THIS_CLASS, "addActdecWinning", new Exception("转账失败！失败数据已插入转账异常表。errormsg:"+errorMsg));
                return "转账失败";
                
            }
            
        } catch (Exception e) {
            LogUtil.errorLog(THIS_CLASS, "addActdecWinning", "系统发生异常，更新异常转账表失败,事物回滚", e);
            throw new RuntimeException("系统发生异常，更新异常转账表失败,事物回滚!" + "[双十二活动转账：" + userId + "]");
        }
        ///////////////////////////////////////////////////////////////////////转账结束
        Account newAccount = new Account();
		// 更新账户信息
		newAccount.setUserId(userId);// 用户Id
		newAccount.setBankTotal(new BigDecimal(aw.getAmount())); // 累加到账户总资产
		newAccount.setBankBalance(new BigDecimal(aw.getAmount())); // 累加可用余额
		newAccount.setBankBalanceCash(new BigDecimal(aw.getAmount()));// 银行账户可用户
		boolean isAccountUpdateFlag = this.adminAccountCustomizeMapper.updateBankRechargeSuccess(newAccount) > 0 ? true : false;
		if (!isAccountUpdateFlag) {
			return "发放红包异常";
		}
		
		// 重新获取用户账户信息
		Account account = this.getAccount(userId);
		// 生成交易明细
		AccountList accountList = new AccountList();
		accountList.setNid(orderId);
		accountList.setUserId(userId);
		accountList.setAmount(new BigDecimal(aw.getAmount()));
		accountList.setTxDate(Integer.parseInt(bean.getTxDate()));// 交易日期
		accountList.setTxTime(Integer.parseInt(bean.getTxTime()));// 交易时间
		accountList.setSeqNo(bean.getSeqNo());// 交易流水号
		accountList.setBankSeqNo((bean.getTxDate() + bean.getTxTime() + bean.getSeqNo()));
		accountList.setType(1);
		accountList.setTrade("borrowactivity");
		accountList.setTradeCode("balance");
		accountList.setAccountId(bankOpenAccountInfo.getAccount());
		accountList.setBankTotal(account.getBankTotal()); // 银行总资产
		accountList.setBankBalance(account.getBankBalance()); // 银行可用余额
		accountList.setBankFrost(account.getBankFrost());// 银行冻结金额
		accountList.setBankWaitCapital(account.getBankWaitCapital());// 银行待还本金
		accountList.setBankWaitInterest(account.getBankWaitInterest());// 银行待还利息
		accountList.setBankAwaitCapital(account.getBankAwaitCapital());// 银行待收本金
		accountList.setBankAwaitInterest(account.getBankAwaitInterest());// 银行待收利息
		accountList.setBankAwait(account.getBankAwait());// 银行待收总额
		accountList.setBankInterestSum(account.getBankInterestSum()); // 银行累计收益
		accountList.setBankInvestSum(account.getBankInvestSum());// 银行累计出借
		accountList.setBankWaitRepay(account.getBankWaitRepay());// 银行待还金额
		accountList.setPlanBalance(account.getPlanBalance());//汇计划账户可用余额
		accountList.setPlanFrost(account.getPlanFrost());
		accountList.setTotal(account.getTotal());
		accountList.setBalance(account.getBalance());
		accountList.setFrost(account.getFrost());
		accountList.setAwait(account.getAwait());
		accountList.setRepay(account.getRepay());
		accountList.setRemark("双12活动红包发放");
		accountList.setCreateTime(GetDate.getNowTime10());
		accountList.setBaseUpdate(GetDate.getNowTime10());
		accountList.setOperator(userId + "");
		accountList.setIsUpdate(0);
		accountList.setBaseUpdate(0);
		accountList.setInterest(null);
		accountList.setWeb(0);
		accountList.setIsBank(1);// 是否是银行的交易记录 0:否 ,1:是
		accountList.setCheckStatus(0);// 对账状态0：未对账 1：已对账
		accountList.setTradeStatus(1);// 成功状态
		// 插入交易明细
		 this.accountListMapper.insertSelective(accountList);

		
		// 添加红包账户明细
        BankMerchantAccount nowBankMerchantAccount = this.getBankMerchantAccount(resultBean.getAccountId());
        nowBankMerchantAccount.setAvailableBalance(nowBankMerchantAccount.getAvailableBalance().subtract(new BigDecimal(aw.getAmount())));
        nowBankMerchantAccount.setAccountBalance(nowBankMerchantAccount.getAccountBalance().subtract(new BigDecimal(aw.getAmount())));
        nowBankMerchantAccount.setUpdateTime(GetDate.getNowTime10());
		// 写入网站收支
		AccountWebList accountWebList = new AccountWebList();
		accountWebList.setOrdid(resultBean.getLogOrderId());
		accountWebList.setAmount(new BigDecimal(aw.getAmount()));
		accountWebList.setType(2);// 1收入2支出
		accountWebList.setTrade("borrowactivity");
		accountWebList.setTradeType("活动奖励");
		accountWebList.setUserId(userId);
		accountWebList.setUsrcustid(merrpAccount);
		accountWebList.setTruename(uio.getTruename());
		accountWebList.setRemark("双十二战队活动奖励");
		accountWebList.setCreateTime(GetDate.getNowTime10());
		accountWebList.setOperator("admin");
		accountWebList.setFlag(1);
		this.accountWebListMapper.insertSelective(accountWebList);
        // 更新红包账户信息
        int updateCount = this.updateBankMerchantAccount(nowBankMerchantAccount);
        if(updateCount > 0){
            UserInfoCustomize userInfoCustomize = this.queryUserInfoByUserId(userId);
            
            // 添加红包明细
            BankMerchantAccountList bankMerchantAccountList = new BankMerchantAccountList();
            bankMerchantAccountList.setOrderId(resultBean.getLogOrderId());
            bankMerchantAccountList.setUserId(userId);
            bankMerchantAccountList.setAccountId(bankOpenAccountInfo.getAccount());
            bankMerchantAccountList.setAmount(new BigDecimal(aw.getAmount()));
            bankMerchantAccountList.setBankAccountCode(resultBean.getAccountId());
            bankMerchantAccountList.setBankAccountBalance(nowBankMerchantAccount.getAccountBalance());
            bankMerchantAccountList.setBankAccountFrost(nowBankMerchantAccount.getFrost());
            bankMerchantAccountList.setTransType(CustomConstants.BANK_MER_TRANS_TYPE_AUTOMATIC);
            bankMerchantAccountList.setType(CustomConstants.BANK_MER_TYPE_EXPENDITURE);
            bankMerchantAccountList.setStatus(CustomConstants.BANK_MER_TRANS_STATUS_SUCCESS);
            bankMerchantAccountList.setTxDate(Integer.parseInt(resultBean.getTxDate()));
            bankMerchantAccountList.setTxTime(Integer.parseInt(resultBean.getTxTime()));
            bankMerchantAccountList.setSeqNo(resultBean.getSeqNo());
            bankMerchantAccountList.setCreateTime(new Date());
            bankMerchantAccountList.setUpdateTime(new Date());
            bankMerchantAccountList.setCreateUserId(userId);
            bankMerchantAccountList.setUpdateUserId(userId);
            bankMerchantAccountList.setRegionName(userInfoCustomize.getRegionName());
            bankMerchantAccountList.setBranchName(userInfoCustomize.getBranchName());
            bankMerchantAccountList.setDepartmentName(userInfoCustomize.getDepartmentName());
            bankMerchantAccountList.setCreateUserName(userInfoCustomize.getUserName());
            bankMerchantAccountList.setUpdateUserName(userInfoCustomize.getUserName());
            bankMerchantAccountList.setRemark("双十二战队活动奖励");
            
             this.bankMerchantAccountListMapper.insertSelective(bankMerchantAccountList);
        }
        return "";
	}

	@Override
	public List<Users> getuser(String mob) {
		UsersExample example=new UsersExample();
		example.or().andMobileEqualTo(mob);
		return usersMapper.selectByExample(example);
		//获取用户信息
	}


	/**
	 * 作成转账异常记录
	 * 
	 * @param fromBean
	 * @param resultBean
	 * @param userId
	 * @param transAmt
	 * @param tenderUserCustId
	 */
	private void insertTransferExceptionLog(BankCallBean fromBean, BankCallBean bankCallBean, int userId, BigDecimal transAmt, String accountId,
			int recoverId, int transferStatus, String errorMsg, int type) throws Exception {
		TransferExceptionLogExample example = new TransferExceptionLogExample();
		example.createCriteria().andRecoverIdEqualTo(recoverId);
		List<TransferExceptionLog> listLog = this.transferExceptionLogMapper.selectByExample(example);
		if (listLog != null && listLog.size() > 0) {
			// 异常转账记录已经存在，不再执行插入操作
			_log.info("异常转账记录已经存在，不再执行插入操作");
			return;
		}
		int nowTime = GetDate.getNowTime10();
		TransferExceptionLogWithBLOBs transferExceptionLog = new TransferExceptionLogWithBLOBs();
		transferExceptionLog.setUuid(CreateUUID.createUUID());
		// 订单编号
		transferExceptionLog.setSeqNo(fromBean.getSeqNo());
		// 发送内容
		transferExceptionLog.setContent(JSONObject.toJSONString(fromBean, true));
		// 返回内容
		transferExceptionLog.setResult(bankCallBean == null ? null : JSONObject.toJSONString(bankCallBean, true));
		// 加息券
		transferExceptionLog.setType(type);
		// 状态 失败
		transferExceptionLog.setStatus(transferStatus);
		// 返回码
		transferExceptionLog.setRespcode(bankCallBean == null ? null : bankCallBean.getRetCode());
		// 交易金额
		transferExceptionLog.setTransAmt(transAmt);
		// 出借人客户号
		transferExceptionLog.setAccountId(accountId);
		// 出借人编号
		transferExceptionLog.setUserId(userId);
		// 还款表（coupon_recover）id
		transferExceptionLog.setRecoverId(recoverId);
		// 转账订单号
		transferExceptionLog.setOrderId(fromBean.getLogOrderId());
		// 备注
		transferExceptionLog.setRemark(errorMsg);
		transferExceptionLog.setAddTime(nowTime);
		transferExceptionLog.setAddUser(CustomConstants.OPERATOR_AUTO_REPAY);
		transferExceptionLog.setUpdateTime(nowTime);
		transferExceptionLog.setUpdateUser(CustomConstants.OPERATOR_AUTO_REPAY);
		transferExceptionLog.setDelFlg(0);
		// 转账失败记录
		this.transferExceptionLogMapper.insertSelective(transferExceptionLog);
	}

	@Override
	public ActdecCorps getActdecCorpsOne(String id) {
		//获取一个战队信息
		return actdecCorpsMapper.selectByPrimaryKey((Integer.valueOf(id)));
	}

	@Override
	public List<UsersInfo> getUserInfo(int userId) {
	//获取用户信息
		 UsersInfoExample example = new UsersInfoExample();
				 example.or().andUserIdEqualTo(userId);
		return usersInfoMapper.selectByExample(example);
	}
    /**
     * 
     * 加载红包账户
     * @param accountCode
     * @return
     */
    public BankMerchantAccount getBankMerchantAccount(String accountCode) {
         BankMerchantAccountExample bankMerchantAccountExample = new BankMerchantAccountExample();
         bankMerchantAccountExample.createCriteria().andAccountCodeEqualTo(accountCode);
         List<BankMerchantAccount> bankMerchantAccounts = bankMerchantAccountMapper.selectByExample(bankMerchantAccountExample);
         if (bankMerchantAccounts != null && bankMerchantAccounts.size() != 0) {
             return bankMerchantAccounts.get(0);
         } else {
             return null;
         }
     }
    /**
     * 
     * 更新红包账户
     * @param account
     * @return
     */
    public int updateBankMerchantAccount(BankMerchantAccount account){
        return bankMerchantAccountMapper.updateByPrimaryKeySelective(account);
    }
    public UserInfoCustomize queryUserInfoByUserId(Integer userId) {
        return userInfoCustomizeMapper.queryUserInfoByUserId(userId);
    }

}
