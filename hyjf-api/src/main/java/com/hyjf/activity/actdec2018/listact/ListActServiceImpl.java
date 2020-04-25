package com.hyjf.activity.actdec2018.listact;


import com.alibaba.fastjson.JSONObject;
import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.UserInfoCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Service
public class ListActServiceImpl extends BaseServiceImpl implements ListActService{
    
	SimpleDateFormat df = new SimpleDateFormat("dd");
	Logger _log = LoggerFactory.getLogger(ListActServiceImpl.class);
	private static final String THIS_CLASS = ListActServiceImpl.class.getName();
	
	
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
	public List<UsersInfo> getUserInfo(Integer userId) {
		//获取用户信息
		 UsersInfoExample example = new UsersInfoExample();
				 example.or().andUserIdEqualTo(userId);
		return usersInfoMapper.selectByExample(example);
	}

	

	@Override
	public List<Users> getUser(Integer userId) {
		//获取用户信息
		 UsersExample example = new UsersExample();
				 example.or().andUserIdEqualTo(userId);
		return usersMapper.selectByExample(example);
	}

	@Override
	public ActdecListedThree getAct3(Integer userId) {
		ActdecListedThreeExample example = new ActdecListedThreeExample();
		 example.or().andUserIdEqualTo(userId);
		 List<ActdecListedThree> alt = actdecListedThreeMapper.selectByExample(example);
		 if(alt.isEmpty()) {
			 return null;
		 }
		return alt.get(0);
	}

	@Override
	public ActdecListedFour getAct4(Integer userId) {
		ActdecListedFourExample example = new ActdecListedFourExample();
		 example.or().andUserIdEqualTo(userId);
		 List<ActdecListedFour> alf = actdecListedFourMapper.selectByExample(example);
		 if(alf.isEmpty()) {
			 return null;
		 }
		return alf.get(0);
	}

	@Override
	public int insertAct3(ActdecListedThree actdecListedThree) {
		
		return actdecListedThreeMapper.insert(actdecListedThree);
	}

	@Override
	public int insertAct4(ActdecListedFour actdecListedFour) {
		return actdecListedFourMapper.insert(actdecListedFour);
	}

	@Override
	public int updateAct3(ActdecListedThree actdecListedThree) {
		
		return actdecListedThreeMapper.updateByPrimaryKey(actdecListedThree);
	}

	@Override
	public int updateAct4(ActdecListedFour actdecListedFour) {
		return actdecListedFourMapper.updateByPrimaryKey(actdecListedFour);
	}

	@Override
	public int getSpreads(Integer userId) {
		SpreadsUsersExample example = new SpreadsUsersExample();
		 example.or().andUserIdEqualTo(userId);
		 List<SpreadsUsers> sum = spreadsUsersMapper.selectByExample(example);
		 if(sum.isEmpty()) {
			 return 0;
		 }
		return spreadsUsersMapper.selectByExample(example).get(0).getSpreadsUserid();
	}

	@Override
	public List<ActdecListedThree> getAct3List( int type) {
		ActdecListedThreeExample example = new ActdecListedThreeExample();
		if(type==1) {
			example.setOrderByClause(" single desc,create_time limit 100");
		}else {
			example.setOrderByClause(" cumulative desc,create_time limit 100");
		}
		
		return actdecListedThreeMapper.selectByExample(example);
	}

	@Override
	public List<ActdecListedFour> getAct4List( int type) {
		if(type==1) {
			ActdecListedFourExample example = new ActdecListedFourExample();
			example.setOrderByClause(" cumulative desc,create_time limit 100");
			return actdecListedFourMapper.selectByExample(example);
		}else if(type==2){
			return listActServiceCustomizeMapper.selectActdecListed();
		}else if(type==3) {
			return listActServiceCustomizeMapper.selectActdecListedTwo();
		}
			
		return null;
	}

	@Override
	public List<ActdecListedOne> getAct1List(Integer userId) {
		ActdecListedOneExample example = new ActdecListedOneExample();
		 ActdecListedOneExample.Criteria criteria = example.createCriteria();
		 criteria.andUserIdEqualTo(userId);
		 criteria.andWhetherEqualTo(1);
		 example.setOrderByClause("update_time asc ");
		return actdecListedOneMapper.selectByExample(example);
	}

	@Override
	public ActdecListedOne getAct1(Integer userId) {
		ActdecListedOneExample example = new ActdecListedOneExample();
		 ActdecListedOneExample.Criteria criteria = example.createCriteria();
		 criteria.andUserIdEqualTo(userId);
		 criteria.andWhetherEqualTo(0);
		 example.setOrderByClause(" update_time asc ");
		 List<ActdecListedOne> as = actdecListedOneMapper.selectByExample(example);
		 if(as.isEmpty()) {
			 return null; 
		 }
		return as.get(0);
	}

	@Override
	public int insertAct1(ActdecListedOne one) {
		return actdecListedOneMapper.insert(one);
	}

	@Override
	public List<BorrowTender> getBorrowTender(String orderId) {
		BorrowTenderExample example = new BorrowTenderExample();
		example.or().andBorrowNidEqualTo(orderId);
		example.setOrderByClause("id asc ");
		return borrowTenderMapper.selectByExample(example);
	}

	@Override
	public Borrow getBorrow(String borrowNid) {
		BorrowExample example = new BorrowExample();
		example.or().andBorrowNidEqualTo(borrowNid);
		return borrowMapper.selectByExample(example).get(0);
	}

	@Override
	public int updateAct1(ActdecListedOne one) {
		return actdecListedOneMapper.updateByPrimaryKey(one);
	}

	@Override
	public int getAct1Count(String borrowNid) {
		ActdecListedOneExample example = new ActdecListedOneExample();
		 ActdecListedOneExample.Criteria criteria = example.createCriteria();
		 criteria.andNumberEqualTo(borrowNid);
		 List<ActdecListedOne> alo = actdecListedOneMapper.selectByExample(example);
		 if(alo.isEmpty()) {
			 return 0;
		 }
		return alo.size();
	}

	@Override
	public Boolean insertMoney(Integer userId,String trueName) {
		String money="0";
		UtmRegExample example=new UtmRegExample();
		example.or().andUserIdEqualTo(userId);
		List<UtmReg> urm = utmRegMapper.selectByExample(example);
		if(!urm.isEmpty()) {
			if(urm.get(0).getUtmId()==2329) {
				money="6.6";
			}else if(urm.get(0).getUtmId()==2330) {
				money="8.8";
			}else if(urm.get(0).getUtmId()==2331) {
				money="18.8";
			}else {
				return false;
			}
		}else {
			return false;
		}
	    BankOpenAccount bankOpenAccountInfo = this.getBankOpenAccount(userId);
        if (bankOpenAccountInfo == null) {
            return false;
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
        bean.setTxAmount(CustomUtil.formatAmount(String.valueOf(money)));
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
                
                transferStatus = 2;
                // 插入转账异常
                this.insertTransferExceptionLog(bean, resultBean, userId,new BigDecimal(money), bankOpenAccountInfo.getAccount(), 0, transferStatus,
                        errorMsg, 2);
                LogUtil.errorLog(THIS_CLASS, "addActdecWinning", new Exception("转账失败！失败数据已插入转账异常表。errormsg:"+errorMsg));
                return false;
                
            }
            
        } catch (Exception e) {
            LogUtil.errorLog(THIS_CLASS, "addActdecWinning", "系统发生异常，更新异常转账表失败,事物回滚", e);
            throw new RuntimeException("系统发生异常，更新异常转账表失败,事物回滚!" + "[扫码送钱活动转账：" + userId + "]");
        }
        ///////////////////////////////////////////////////////////////////////转账结束
        Account newAccount = new Account();
		// 更新账户信息
		newAccount.setUserId(userId);// 用户Id
		newAccount.setBankTotal(new BigDecimal(money)); // 累加到账户总资产
		newAccount.setBankBalance(new BigDecimal(money)); // 累加可用余额
		newAccount.setBankBalanceCash(new BigDecimal(money));// 银行账户可用户
		boolean isAccountUpdateFlag = this.adminAccountCustomizeMapper.updateBankRechargeSuccess(newAccount) > 0 ? true : false;
		if (!isAccountUpdateFlag) {
			return false;
		}
		
		// 重新获取用户账户信息
		Account account = this.getAccount(userId);
		// 生成交易明细
		AccountList accountList = new AccountList();
		accountList.setNid(orderId);
		accountList.setUserId(userId);
		accountList.setAmount(new BigDecimal(money));
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
		accountList.setRemark("扫码开户送金钱");
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
        nowBankMerchantAccount.setAvailableBalance(nowBankMerchantAccount.getAvailableBalance().subtract(new BigDecimal(money)));
        nowBankMerchantAccount.setAccountBalance(nowBankMerchantAccount.getAccountBalance().subtract(new BigDecimal(money)));
        nowBankMerchantAccount.setUpdateTime(GetDate.getNowTime10());
		// 写入网站收支
		AccountWebList accountWebList = new AccountWebList();
		accountWebList.setOrdid(resultBean.getLogOrderId());
		accountWebList.setAmount(new BigDecimal(money));
		accountWebList.setType(2);// 1收入2支出
		accountWebList.setTrade("borrowactivity");
		accountWebList.setTradeType("活动奖励");
		accountWebList.setUserId(userId);
		accountWebList.setUsrcustid(merrpAccount);
		accountWebList.setTruename(trueName);
		accountWebList.setRemark("扫码开户送金钱");
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
            bankMerchantAccountList.setAmount(new BigDecimal(money));
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
            bankMerchantAccountList.setRemark("扫码开户送金钱");
            
             this.bankMerchantAccountListMapper.insertSelective(bankMerchantAccountList);
        }
		return null;
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
