package com.hyjf.bank.service.user.autoup;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.common.util.CommonUtils;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.HjhUserAuthConfig;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BankOpenAccountExample;
import com.hyjf.mybatis.model.auto.BankSmsAuthCode;
import com.hyjf.mybatis.model.auto.BankSmsAuthCodeExample;
import com.hyjf.mybatis.model.auto.HjhAccede;
import com.hyjf.mybatis.model.auto.HjhAccedeExample;
import com.hyjf.mybatis.model.auto.HjhUserAuth;
import com.hyjf.mybatis.model.auto.HjhUserAuthExample;
import com.hyjf.mybatis.model.auto.HjhUserAuthLog;
import com.hyjf.mybatis.model.auto.HjhUserAuthLogExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

@Service
public class AutoPlusServiceImpl extends BaseServiceImpl implements AutoPlusService {
    /**
     * 
     * 根据用户id更新用户签约授权信息
     * @param userId
     * @param bean 
     */
    @Override
    public void updateUserAuthInves(Integer userId,BankCallBean bean) {

        Integer nowTime=GetDate.getNowTime10();
        HjhUserAuthLogExample example=new HjhUserAuthLogExample();
        example.createCriteria().andOrderIdEqualTo(bean.getOrderId());
        List<HjhUserAuthLog> list=hjhUserAuthLogMapper.selectByExample(example);
        HjhUserAuthLog hjhUserAuthLog=null;
        //更新用户签约授权日志表
        if(list!=null&&list.size()>0){
            hjhUserAuthLog=list.get(0);
            hjhUserAuthLog.setUpdateTime(nowTime);
            hjhUserAuthLog.setUpdateUser(userId);
            hjhUserAuthLog.setOrderStatus(1);
            hjhUserAuthLog.setAuthCreateTime(nowTime);
            hjhUserAuthLogMapper.updateByPrimaryKeySelective(hjhUserAuthLog);
        }
        
        // 这里同步异步一起进来会导致重复插入的异常，加一个同步锁
		synchronized (this) {
            HjhUserAuth hjhUserAuth=this.getHjhUserAuthByUserId(userId);
			// 更新用户签约授权状态信息表
			if (hjhUserAuth == null) {
				Users user = this.getUsers(userId);
				hjhUserAuth = new HjhUserAuth();
				// 设置状态
				setAuthType(hjhUserAuth, bean);
				hjhUserAuth.setAutoWithdrawStatus(0);
				hjhUserAuth.setAutoConsumeStatus(0);
				hjhUserAuth.setUserId(user.getUserId());
				hjhUserAuth.setUserName(user.getUsername());
				hjhUserAuth.setCreateUser(user.getUserId());
				hjhUserAuth.setCreateTime(nowTime);
				hjhUserAuth.setUpdateTime(nowTime);
				hjhUserAuth.setUpdateUser(userId);
				hjhUserAuth.setDelFlg(0);
				hjhUserAuthMapper.insertSelective(hjhUserAuth);
			} else {
				HjhUserAuth updateHjhUserAuth = new HjhUserAuth();
				// 设置状态
				setAuthType(hjhUserAuth, bean);
				updateHjhUserAuth.setId(hjhUserAuth.getId());
				updateHjhUserAuth.setUpdateTime(nowTime);
				updateHjhUserAuth.setUpdateUser(userId);
				hjhUserAuthMapper.updateByPrimaryKeySelective(hjhUserAuth);
			}
		}
        
    }
    
    // 设置状态
    private void setAuthType(HjhUserAuth hjhUserAuth, BankCallBean bean) {
        // 授权类型
        String txcode = bean.getTxCode();
        if(BankCallConstant.TXCODE_AUTO_BID_AUTH_PLUS.equals(txcode)){
            hjhUserAuth.setAutoInvesStatus(1);
            hjhUserAuth.setAutoOrderId(bean.getOrderId());
            hjhUserAuth.setAutoBidTime(GetDate.getNowTime10());
            hjhUserAuth.setAutoCreateTime(GetDate.getNowTime10());
            hjhUserAuth.setAutoBidEndTime(bean.getDeadline());
            HjhUserAuthConfig config=CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_AUTO_TENDER_AUTH);
            hjhUserAuth.setInvesMaxAmt(config.getEnterpriseMaxAmount()+"");
        }else if(BankCallConstant.TXCODE_AUTO_CREDIT_INVEST_AUTH_PLUSS.equals(txcode)){
            hjhUserAuth.setAutoCreditStatus(1);
            hjhUserAuth.setAutoCreditOrderId(bean.getOrderId());
            hjhUserAuth.setAutoCreditTime(GetDate.getNowTime10());
            hjhUserAuth.setAutoCreateTime(GetDate.getNowTime10());
            HjhUserAuthConfig config=CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_AUTO_CREDIT_AUTH);
            hjhUserAuth.setCreditMaxAmt(config.getEnterpriseMaxAmount()+"");
        }else if(BankCallConstant.TXCODE_CREDIT_AUTH_QUERY.equals(txcode)){
            //根据银行查询出借人签约状态
            if(BankCallConstant.QUERY_TYPE_1.equals(bean.getType())){
                hjhUserAuth.setAutoInvesStatus(1);
                hjhUserAuth.setAutoOrderId(bean.getOrderId());
                hjhUserAuth.setAutoBidTime(GetDate.getNowTime10());
                hjhUserAuth.setAutoBidEndTime(bean.getBidDeadline());
            }else if(BankCallConstant.QUERY_TYPE_2.equals(bean.getType())){
                hjhUserAuth.setAutoCreditStatus(1);
                hjhUserAuth.setAutoCreditOrderId(bean.getOrderId());
                hjhUserAuth.setAutoCreditTime(GetDate.getNowTime10());
            }
        }
        // 新增缴费授权和还款授权
        else if(BankCallConstant.TXCODE_PAYMENT_AUTH_PAGE.equals(txcode)){
            hjhUserAuth.setAutoPaymentStatus(1);
            hjhUserAuth.setAutoPaymentEndTime(bean.getDeadline());
            hjhUserAuth.setAutoPaymentTime(GetDate.getNowTime10());
            hjhUserAuth.setPaymentMaxAmt(bean.getPaymentMaxAmt());
        }else if(BankCallConstant.TXCODE_REPAY_AUTH_PAGE.equals(txcode)){
            hjhUserAuth.setAutoRepayStatus(1);
            hjhUserAuth.setAutoRepayEndTime(bean.getDeadline());
            hjhUserAuth.setAutoRepayTime(GetDate.getNowTime10());
            hjhUserAuth.setRepayMaxAmt(bean.getRepayMaxAmt());
        }

        // 客户授权功能查询接口
        else if(BankCallConstant.TXCODE_TERMS_AUTH_QUERY.equals(txcode)){
            //自动投标功能开通标志
            String autoBidStatus = bean.getAutoBid();
            //自动债转功能开通标志
            String autoTransfer = bean.getAutoTransfer();
            //缴费授权
            String paymentAuth = bean.getPaymentAuth();
            //还款授权
            String repayAuth = bean.getRepayAuth();
            if(StringUtils.isNotBlank(autoBidStatus)){
                hjhUserAuth.setAutoInvesStatus(Integer.parseInt(autoBidStatus));
            }
            if(StringUtils.isNotBlank(autoTransfer)){
                hjhUserAuth.setAutoCreditStatus(Integer.parseInt(autoTransfer));
            }
            if(StringUtils.isNotBlank(paymentAuth)){
                hjhUserAuth.setAutoPaymentStatus(Integer.parseInt(paymentAuth));
                hjhUserAuth.setAutoPaymentEndTime(bean.getPaymentDeadline());
               
                hjhUserAuth.setPaymentMaxAmt(bean.getPaymentMaxAmt());
            }
            if(StringUtils.isNotBlank(repayAuth)){
                hjhUserAuth.setAutoRepayStatus(Integer.parseInt(repayAuth));
                hjhUserAuth.setAutoRepayEndTime(bean.getRepayDeadline());
                
                hjhUserAuth.setRepayMaxAmt(bean.getRepayMaxAmt());
            }
            //自动投标到期日
            if(StringUtils.isNotBlank(bean.getAutoBidDeadline())) {
            	hjhUserAuth.setAutoBidEndTime(bean.getAutoBidDeadline());
            }
        }

    }
    
    /**
     * 
     * 根据用户id查询用户签约授权信息
     * @param userId
     * @return
     */
    @Override
	public HjhUserAuth getHjhUserAuthByUserId(Integer userId) {
		HjhUserAuthExample example = new HjhUserAuthExample();
		example.createCriteria().andUserIdEqualTo(userId);
		List<HjhUserAuth> list = hjhUserAuthMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}

	}
    /**
     * 
     * 插入用户签约授权log
     * @param userId
     * @param bean
     * @param queryType 
     * @param i 
     */
    @Override
    public void insertUserAuthLog(int userId, BankCallBean bean, Integer client, String authType) {
        Integer nowTime=GetDate.getNowTime10();
        Users user=this.getUsers(userId);
        HjhUserAuthLog hjhUserAuthLog=new HjhUserAuthLog();
        hjhUserAuthLog.setUserId(user.getUserId());
        hjhUserAuthLog.setUserName(user.getUsername());
        hjhUserAuthLog.setOrderId(bean.getLogOrderId());
        hjhUserAuthLog.setOrderStatus(2);
        if(authType!=null&&authType.equals(BankCallConstant.QUERY_TYPE_2)){
            hjhUserAuthLog.setAuthType(4);
        }else{
            hjhUserAuthLog.setAuthType(Integer.valueOf(authType));
        }
        
        hjhUserAuthLog.setOperateEsb(client);
        hjhUserAuthLog.setCreateUser(user.getUserId());
        hjhUserAuthLog.setCreateTime(nowTime);
        hjhUserAuthLog.setUpdateTime(nowTime);                     
        hjhUserAuthLog.setUpdateUser(userId);
        hjhUserAuthLog.setDelFlg(0);
        hjhUserAuthLogMapper.insertSelective(hjhUserAuthLog);
    }

    /**
     * 
     * 根据手机号查询用户
     * @author sss
     * @param mobile
     * @return
     */
    @Override
    public Users selectUserByMobile(String mobile) {
        UsersExample example = new UsersExample();
        UsersExample.Criteria cra = example.createCriteria();
        cra.andMobileEqualTo(mobile);
        List<Users> list = this.usersMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public BankOpenAccount selectBankOpenAccountByAccountId(String accountId) {
        BankOpenAccountExample example = new BankOpenAccountExample();
        BankOpenAccountExample.Criteria cra = example.createCriteria();
        cra.andAccountEqualTo(accountId);
        List<BankOpenAccount> list = this.bankOpenAccountMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
    
    @Override
    public BankCallBean getUserAuthQUery(Integer userId,String type) {
        BankOpenAccount bankOpenAccount = getBankOpenAccount(userId);
        // 调用查询出借人签约状态查询
        BankCallBean selectbean = new BankCallBean();
        selectbean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        selectbean.setTxCode(BankCallConstant.TXCODE_CREDIT_AUTH_QUERY);
        selectbean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
        selectbean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
        selectbean.setTxDate(GetOrderIdUtils.getTxDate());
        selectbean.setTxTime(GetOrderIdUtils.getTxTime());
        selectbean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
        selectbean.setChannel(BankCallConstant.CHANNEL_PC);
        selectbean.setType(type);
        selectbean.setAccountId(String.valueOf(bankOpenAccount.getAccount()));// 电子账号
        
        // 操作者ID
        selectbean.setLogUserId(String.valueOf(userId));
        selectbean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));
        //根据银行查询出借人签约状态
        if(BankCallConstant.QUERY_TYPE_1.equals(type)){
            selectbean.setLogRemark("用户授权自动出借");
        }else if(BankCallConstant.QUERY_TYPE_2.equals(type)){
            selectbean.setLogRemark("用户授权自动债转");
        }
        selectbean.setLogClient(0);
        // 返回参数
        BankCallBean retBean = null;
        // 调用接口
        retBean = BankCallUtils.callApiBg(selectbean);
        return retBean;
    }
    
    @Override
    public String selectBankSmsSeq(Integer userId, String txcodeDirectRechargeOnline) {
        BankSmsAuthCodeExample example = new BankSmsAuthCodeExample();
        example.createCriteria().andUserIdEqualTo(userId).andSrvTxCodeEqualTo(txcodeDirectRechargeOnline);
        List<BankSmsAuthCode> smsAuthCodeList = bankSmsAuthCodeMapper.selectByExample(example);
        if (smsAuthCodeList != null && smsAuthCodeList.size() == 1) {
            BankSmsAuthCode smsAuthCode = smsAuthCodeList.get(0);
            return smsAuthCode.getSrvAuthCode();
        }
        return null;
    }

    @Override
    public BankCallBean getTermsAuthQuery(int userId,String channel) {
        BankOpenAccount bankOpenAccount = getBankOpenAccount(userId);
        // 调用查询出借人签约状态查询
        BankCallBean selectbean = new BankCallBean();
        selectbean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        selectbean.setTxCode(BankCallConstant.TXCODE_TERMS_AUTH_QUERY);
        selectbean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
        selectbean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
        selectbean.setTxDate(GetOrderIdUtils.getTxDate());
        selectbean.setTxTime(GetOrderIdUtils.getTxTime());
        selectbean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
        selectbean.setChannel(channel);
        selectbean.setAccountId(String.valueOf(bankOpenAccount.getAccount()));// 电子账号
        selectbean.setLogOrderDate(GetOrderIdUtils.getOrderDate());
        // 操作者ID
        selectbean.setLogUserId(String.valueOf(userId));
        selectbean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));
        selectbean.setLogClient(0);
        // 返回参数
        BankCallBean retBean = null;
        // 调用接口
        retBean = BankCallUtils.callApiBg(selectbean);
        return retBean;
    }

    @Override
    public void updateUserAuth(int userId, BankCallBean retBean) {
		HjhUserAuth hjhUserAuth = this.getHjhUserAuthByUserId(userId);
		Integer nowTime = GetDate.getNowTime10();
		String orderId = retBean.getOrderId();
		if (StringUtils.isNotBlank(orderId)) {
			HjhUserAuthLogExample example = new HjhUserAuthLogExample();
			example.createCriteria().andOrderIdEqualTo(orderId);
			List<HjhUserAuthLog> list = hjhUserAuthLogMapper.selectByExample(example);
			HjhUserAuthLog hjhUserAuthLog = null;
			// 更新用户签约授权日志表
			if (list != null && list.size() > 0) {
				hjhUserAuthLog = list.get(0);
				hjhUserAuthLog.setUpdateTime(nowTime);
				hjhUserAuthLog.setUpdateUser(userId);
				hjhUserAuthLog.setOrderStatus(1);
				hjhUserAuthLog.setAuthCreateTime(nowTime);
				hjhUserAuthLogMapper.updateByPrimaryKeySelective(hjhUserAuthLog);
			}
		}
        
        if (retBean != null && BankCallConstant.RESPCODE_SUCCESS.equals(retBean.get(BankCallConstant.PARAM_RETCODE))) {
            // 更新user表状态为授权成功
            Users user=this.getUsers(userId);
            if(BankCallConstant.TXCODE_PAYMENT_AUTH_PAGE.equals(retBean.get(BankCallConstant.PARAM_TXCODE))){
                user.setPaymentAuthStatus(1);
                this.usersMapper.updateByPrimaryKeySelective(user);
            }
            //更新用户签约授权状态信息表
            if(hjhUserAuth==null){
                hjhUserAuth=new HjhUserAuth();
                // 设置状态
                setAuthType(hjhUserAuth,retBean);
                hjhUserAuth.setUserId(user.getUserId());
                hjhUserAuth.setUserName(user.getUsername());
                hjhUserAuth.setCreateUser(user.getUserId());
                hjhUserAuth.setCreateTime(nowTime);
                hjhUserAuth.setUpdateTime(nowTime);
                hjhUserAuth.setUpdateUser(userId);
                hjhUserAuth.setDelFlg(0);
                hjhUserAuthMapper.insertSelective(hjhUserAuth);
            }else{
                HjhUserAuth updateHjhUserAuth=new HjhUserAuth();
                // 设置状态
                setAuthType(hjhUserAuth,retBean);
                updateHjhUserAuth.setId(hjhUserAuth.getId());
                updateHjhUserAuth.setUpdateTime(nowTime);
                updateHjhUserAuth.setUpdateUser(userId);
                hjhUserAuthMapper.updateByPrimaryKeySelective(hjhUserAuth);
            }
        }
        
    }

    // 检查是否已经授权过了
    @Override
    public boolean checkIsAuth(Integer userId, String txcode) {
        HjhUserAuth hjhUserAuth = getHjhUserAuthByUserId(userId);
        String endTime = "";
        int status = 0;
        String nowTime = GetDate.date2Str(new Date(),GetDate.yyyyMMdd);
        // 缴费授权
        if(hjhUserAuth==null){
            return false;
        }
        if (BankCallConstant.TXCODE_PAYMENT_AUTH_PAGE.equals(txcode)) {
            endTime = hjhUserAuth.getAutoPaymentEndTime();
            status = hjhUserAuth.getAutoPaymentStatus();
        }else if(BankCallConstant.TXCODE_REPAY_AUTH_PAGE.equals(txcode)){
            endTime = hjhUserAuth.getAutoRepayEndTime();
            status = hjhUserAuth.getAutoRepayStatus();
        }else if(BankCallConstant.TXCODE_AUTO_BID_AUTH_PLUS.equals(txcode)){
            endTime = hjhUserAuth.getAutoBidEndTime();
            status = hjhUserAuth.getAutoInvesStatus();
        }
        // 0是未授权
        if (status - 0 == 0 || Integer.parseInt(endTime) - Integer.parseInt(nowTime) < 0) {
            return false;
        }
        return true;
    }


	@Override
	public BankCallBean cancelInvestAuth(int userId, String channel) {
		BankCallBean selectbean = new BankCallBean();
		selectbean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		selectbean.setTxCode(BankCallConstant.TXCODE_AUTOBID_AUTH_CANCEL);
		selectbean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
		selectbean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
		selectbean.setTxDate(GetOrderIdUtils.getTxDate());
		selectbean.setTxTime(GetOrderIdUtils.getTxTime());
		selectbean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
		selectbean.setChannel(channel);

		BankOpenAccount bankOpenAccount = this.getBankOpenAccount(userId);
		if (bankOpenAccount != null) {
			selectbean.setAccountId(bankOpenAccount.getAccount());// 电子账号
		}
		selectbean.setOrderId(GetOrderIdUtils.getUsrId(userId));// 订单号

		HjhUserAuth hjhUserAuth = this.getHjhUserAuthByUserId(userId);
		if (hjhUserAuth != null) {
			selectbean.setOrgOrderId(hjhUserAuth.getAutoOrderId());// 原订单号
		}

		// 操作者ID
		selectbean.setLogUserId(String.valueOf(userId));
		selectbean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));
		selectbean.setLogClient(0);
		// 调用接口
		BankCallBean retBean = BankCallUtils.callApiBg(selectbean);
		return retBean;
	}

	@Override
	public BankCallBean cancelCreditAuth(int userId, String channel) {
		BankCallBean selectbean = new BankCallBean();
		selectbean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		selectbean.setTxCode(BankCallConstant.TXCODE_AUTO_CREDIT_INVEST_AUTH_CANCEL);
		selectbean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
		selectbean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
		selectbean.setTxDate(GetOrderIdUtils.getTxDate());
		selectbean.setTxTime(GetOrderIdUtils.getTxTime());
		selectbean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
		selectbean.setChannel(channel);

		BankOpenAccount bankOpenAccount = this.getBankOpenAccount(userId);
		if (bankOpenAccount != null) {
			selectbean.setAccountId(bankOpenAccount.getAccount());// 电子账号
		}
		selectbean.setOrderId(GetOrderIdUtils.getUsrId(userId));// 订单号

		HjhUserAuth hjhUserAuth = this.getHjhUserAuthByUserId(userId);
		if (hjhUserAuth != null) {
			selectbean.setOrgOrderId(hjhUserAuth.getAutoCreditOrderId());// 原订单号
		}
		// 操作者ID
		selectbean.setLogUserId(String.valueOf(userId));
		selectbean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));
		selectbean.setLogClient(0);
		// 调用接口
		BankCallBean retBean = BankCallUtils.callApiBg(selectbean);
		return retBean;
	}

	/**
	 * 出借授权解约更新
	 * 
	 * @param userId
	 */
	@Override
	public void updateCancelInvestAuth(int userId) {
		HjhUserAuth hjhUserAuth = this.getHjhUserAuthByUserId(userId);
		hjhUserAuth.setAutoInvesStatus(0);
		hjhUserAuth.setInvesCancelTime(GetDate.date2Str(new Date(),GetDate.yyyyMMdd));
		hjhUserAuthMapper.updateByPrimaryKeySelective(hjhUserAuth);
	}

	/**
	 * 债转授权解约更新
	 * 
	 * @param userId
	 */
	@Override
	public void updateCancelCreditAuth(int userId) {
		HjhUserAuth hjhUserAuth = this.getHjhUserAuthByUserId(userId);
		hjhUserAuth.setAutoCreditStatus(0);
		hjhUserAuth.setCreditCancelTime(GetDate.date2Str(new Date(),GetDate.yyyyMMdd));
		hjhUserAuthMapper.updateByPrimaryKeySelective(hjhUserAuth);
	}

    /**
     * 判断用户是否有持有中的计划。如果有，则不能解除出借授权和债转授权
     * @param userId
     * @return
     */
    @Override
	public boolean canCancelAuth(int userId) {
		HjhAccedeExample example = new HjhAccedeExample();
		HjhAccedeExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		criteria.andOrderStatusNotEqualTo(7);
		List<HjhAccede> list = hjhAccedeMapper.selectByExample(example);
		if (!CollectionUtils.isEmpty(list)) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * 获得用户授权状态信息  
	 * 自动投标状态          缴费授权状态      还款授权状态    
	 * @author sunss
	 * @return
	 */
    @Override
	public HjhUserAuth getUserAuthState(HjhUserAuth auth) {
	    // 缴费授权
	    int paymentAuth = valdateAuthState(auth.getAutoPaymentStatus(),auth.getAutoPaymentEndTime());
	    auth.setAutoPaymentStatus(paymentAuth);
	    // 还款授权 
	    int repayAuth = valdateAuthState(auth.getAutoRepayStatus(),auth.getAutoRepayEndTime());
	    auth.setAutoRepayStatus(repayAuth);
	    // 自动出借授权
	    int invesAuth = valdateAuthState(auth.getAutoInvesStatus(),auth.getAutoBidEndTime());
	    auth.setAutoInvesStatus(invesAuth);
	    return auth;
	}

    /**
     * 解约插入授权记录表
     * @param userId
     * @param retBean
     */
    @Override
    public void insertUserAuthLog2(int userId,BankCallBean retBean,String authType) {
        Integer nowTime=GetDate.getNowTime10();
        Users user=this.getUsers(userId);
        HjhUserAuthLog hjhUserAuthLog=new HjhUserAuthLog();
        hjhUserAuthLog.setUserId(user.getUserId());
        hjhUserAuthLog.setUserName(user.getUsername());
        hjhUserAuthLog.setOrderId(retBean.getLogOrderId());
        hjhUserAuthLog.setOrderStatus(1);
        hjhUserAuthLog.setAuthType(Integer.parseInt(authType));
        hjhUserAuthLog.setOperateEsb(0);
        hjhUserAuthLog.setCreateUser(user.getUserId());
        hjhUserAuthLog.setCreateTime(nowTime);
        hjhUserAuthLog.setUpdateTime(nowTime);
        hjhUserAuthLog.setUpdateUser(userId);
        hjhUserAuthLog.setDelFlg(0);
        hjhUserAuthLogMapper.insertSelective(hjhUserAuthLog);
    }

    @Override
	public BankCallBean cancelPayAuth(int userId,String channel) {
		BankCallBean selectbean = new BankCallBean();
		selectbean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		selectbean.setTxCode(BankCallConstant.TXCODE_AUTO_PAY_AUTH_CANCEL);
		selectbean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
		selectbean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
		selectbean.setTxDate(GetOrderIdUtils.getTxDate());
		selectbean.setTxTime(GetOrderIdUtils.getTxTime());
		selectbean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
		selectbean.setChannel(channel);

		BankOpenAccount bankOpenAccount = this.getBankOpenAccount(userId);
		if (bankOpenAccount != null) {
			selectbean.setAccountId(bankOpenAccount.getAccount());// 电子账号
		}
		selectbean.setOrderId(GetOrderIdUtils.getUsrId(userId));// 订单号
		
		selectbean.setMaxAmt("2000000");
		selectbean.setTxType("2");
//		HjhUserAuth hjhUserAuth = this.getHjhUserAuthByUserId(userId);
//		if (hjhUserAuth != null) {
//			selectbean.setOrgOrderId(hjhUserAuth.getAutoOrderId());// 原订单号
//		}
		// 操作者ID
		selectbean.setLogUserId(String.valueOf(userId));
		selectbean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));
		selectbean.setLogClient(0);
		// 调用接口
		BankCallBean retBean = BankCallUtils.callApiBg(selectbean);
		return retBean;	
	}
	
	@Override
	public void updateCancelPayAuth(int userId) {
		HjhUserAuth hjhUserAuth = this.getHjhUserAuthByUserId(userId);
		hjhUserAuth.setAutoPaymentStatus(0);
		hjhUserAuth.setPaymentCancelTime(GetDate.date2Str(new Date(),GetDate.yyyyMMdd));
		hjhUserAuthMapper.updateByPrimaryKeySelective(hjhUserAuth);
        // 更新user表状态为授权成功
        Users user=this.getUsers(userId);
        user.setPaymentAuthStatus(0);
        this.usersMapper.updateByPrimaryKeySelective(user);
	}

	@Override
	public BankCallBean cancelRePayAuth(int userId, String channel) {
	    String orderId=GetOrderIdUtils.getOrderId2(userId);
		BankCallBean selectbean = new BankCallBean();
		selectbean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		selectbean.setTxCode(BankCallConstant.TXCODE_AUTO_REPAY_AUTH_CANCEL);
		selectbean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
		selectbean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
		selectbean.setTxDate(GetOrderIdUtils.getTxDate());
		selectbean.setTxTime(GetOrderIdUtils.getTxTime());
		selectbean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
		selectbean.setChannel(channel);

		BankOpenAccount bankOpenAccount = this.getBankOpenAccount(userId);
		if (bankOpenAccount != null) {
			selectbean.setAccountId(bankOpenAccount.getAccount());// 电子账号
		}
		selectbean.setOrderId(orderId);// 订单号
		
		selectbean.setMaxAmt("2000000");
		selectbean.setTxType("2");
		HjhUserAuth hjhUserAuth = this.getHjhUserAuthByUserId(userId);
		if (hjhUserAuth != null) {
		    selectbean.setDeadline(hjhUserAuth.getAutoRepayEndTime());
		}
		// 操作者IDX
		selectbean.setLogUserId(String.valueOf(userId));
		selectbean.setLogOrderId(orderId);
		// 调用接口
		BankCallBean retBean = BankCallUtils.callApiBg(selectbean);
		return retBean;	
	}

	@Override
	public void updateCancelRePayAuth(int userId) {
		HjhUserAuth hjhUserAuth = this.getHjhUserAuthByUserId(userId);
		hjhUserAuth.setAutoRepayStatus(0);
		hjhUserAuth.setRepayCancelTime(GetDate.date2Str(new Date(),GetDate.yyyyMMdd));
		hjhUserAuthMapper.updateByPrimaryKeySelective(hjhUserAuth);
	}
	
	// 检查是否授权  0未授权  1已授权
    private int valdateAuthState(Integer status, String endTime) {
        String nowTime = GetDate.date2Str(new Date(),GetDate.yyyyMMdd);
        if(endTime==null || status==null){
            return 0;
        }
        // 结束时间为空的情况下 直接返回状态
        if("".equals(endTime)){
            return status;
        }
        if (status - 0 == 0 || Integer.parseInt(endTime) - Integer.parseInt(nowTime) < 0) {
            return 0;
        }
        return 1;
    }
    
    @Override
    public BankCallBean getPayAuthQuery(HjhUserAuth userAuth,String url,String channel) {
        BankOpenAccount bankOpenAccount = getBankOpenAccount(userAuth.getUserId());
        // 调用查询出借人签约状态查询
        BankCallBean selectbean = new BankCallBean();
        selectbean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        selectbean.setTxCode(BankCallConstant.TXCODE_PAYMENT_AUTH_PAGE);
        selectbean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
        selectbean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
        selectbean.setTxDate(GetOrderIdUtils.getTxDate());
        selectbean.setTxTime(GetOrderIdUtils.getTxTime());
        selectbean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
        selectbean.setChannel(channel);
        selectbean.setMaxAmt("250000");
        selectbean.setDeadline(userAuth.getAutoPaymentEndTime());
        selectbean.setAccountId(String.valueOf(bankOpenAccount.getAccount()));// 电子账号
        selectbean.setLogOrderDate(GetOrderIdUtils.getOrderDate());
        
        selectbean.setRetUrl(url);
        // 操作者ID
        selectbean.setLogUserId(String.valueOf(userAuth.getUserId()));
        selectbean.setLogOrderId(GetOrderIdUtils.getOrderId2(userAuth.getUserId()));
        selectbean.setLogClient(0);
        // 返回参数
        BankCallBean retBean = null;
        // 调用接口
        retBean = BankCallUtils.callApiBg(selectbean);
        return retBean;
    }
    @Override
    public BankCallBean getRePayAuthQuery(HjhUserAuth userAuth,String url,String channel) {
        BankOpenAccount bankOpenAccount = getBankOpenAccount(userAuth.getUserId());
        // 调用查询出借人签约状态查询
        BankCallBean selectbean = new BankCallBean();
        selectbean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        selectbean.setTxCode(BankCallConstant.TXCODE_REPAY_AUTH_PAGE);
        selectbean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
        selectbean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
        selectbean.setTxDate(GetOrderIdUtils.getTxDate());
        selectbean.setTxTime(GetOrderIdUtils.getTxTime());
        selectbean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
        selectbean.setChannel(channel);
        selectbean.setMaxAmt("1350000");
        selectbean.setDeadline(userAuth.getAutoRepayEndTime());
        selectbean.setAccountId(String.valueOf(bankOpenAccount.getAccount()));// 电子账号
        selectbean.setLogOrderDate(GetOrderIdUtils.getOrderDate());
        
        selectbean.setRetUrl(url);
        // 操作者ID
        selectbean.setLogUserId(String.valueOf(userAuth.getUserId()));
        selectbean.setLogOrderId(GetOrderIdUtils.getOrderId2(userAuth.getUserId()));
        selectbean.setLogClient(0);
        // 返回参数
        BankCallBean retBean = null;
        // 调用接口
        retBean = BankCallUtils.callApiBg(selectbean);
        return retBean;
    }
    
    @Override
    public HjhUserAuth updateUserAuthState(Integer userId, BankCallBean retBean) {
        HjhUserAuth hjhUserAuth = this.getHjhUserAuthByUserId(userId);
        if (retBean != null && BankCallConstant.RESPCODE_SUCCESS.equals(retBean.get(BankCallConstant.PARAM_RETCODE))) {
         // 自动出借授权
            if("1".equals(retBean.getType())){
                hjhUserAuth.setAutoInvesStatus(Integer.parseInt(retBean.getState()));
                hjhUserAuth.setAutoOrderId(retBean.getOrderId());
                // 如果不为空的情况下才能更新
                if(!StringUtils.isEmpty(retBean.getBidDeadline())){
                    hjhUserAuth.setAutoBidEndTime(retBean.getBidDeadline());
                }
            }else{
                // 自动债转授权
                hjhUserAuth.setAutoCreditStatus(Integer.parseInt(retBean.getState()));
                hjhUserAuth.setAutoCreditOrderId(retBean.getOrderId());
            }
            hjhUserAuthMapper.updateByPrimaryKeySelective(hjhUserAuth);
        }
        return hjhUserAuth;
    }
}
