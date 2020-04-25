package com.hyjf.bank.service.user.paymentauthpage;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.common.util.CommonUtils;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.HjhUserAuthConfig;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.HjhUserAuth;
import com.hyjf.mybatis.model.auto.HjhUserAuthExample;
import com.hyjf.mybatis.model.auto.HjhUserAuthLog;
import com.hyjf.mybatis.model.auto.HjhUserAuthLogExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

@Service
public class PaymentAuthPageServiceImpl extends BaseServiceImpl implements PaymentAuthService {
    /**
     * 
     * 根据用户id更新用户签约授权信息
     * @param userId
     * @param bean 
     */
    @Override
    public void updateUserAuthInves(Integer userId,BankCallBean bean) {
        HjhUserAuth hjhUserAuth=this.getHjhUserAuthByUserId(userId);
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
        
        //更新用户签约授权状态信息表
        if(hjhUserAuth==null){
            Users user=this.getUsers(userId);
            hjhUserAuth=new HjhUserAuth();
            // 设置状态
            setAuthType(hjhUserAuth,bean);
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
        }else{
            HjhUserAuth updateHjhUserAuth=new HjhUserAuth();
            // 设置状态
            setAuthType(hjhUserAuth,bean);
            updateHjhUserAuth.setId(hjhUserAuth.getId());
            updateHjhUserAuth.setUpdateTime(nowTime);
            updateHjhUserAuth.setUpdateUser(userId);
            hjhUserAuthMapper.updateByPrimaryKeySelective(hjhUserAuth);
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
            hjhUserAuth.setAutoBidEndTime(bean.getBidDeadline());
        }else if(BankCallConstant.TXCODE_AUTO_CREDIT_INVEST_AUTH_PLUSS.equals(txcode)){
            hjhUserAuth.setAutoCreditStatus(1);
            hjhUserAuth.setAutoCreditOrderId(bean.getOrderId());
            hjhUserAuth.setAutoCreditTime(GetDate.getNowTime10());
            hjhUserAuth.setAutoCreateTime(GetDate.getNowTime10());
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
     * @param orderId
     * @param client
     * @param authType
     */
    @Override
    public void insertUserAuthLog(int userId, String orderId, Integer client, String authType) {
        Integer nowTime=GetDate.getNowTime10();
        Users user=this.getUsers(userId);
        HjhUserAuthLog hjhUserAuthLog=new HjhUserAuthLog();
        hjhUserAuthLog.setUserId(user.getUserId());
        hjhUserAuthLog.setUserName(user.getUsername());
        hjhUserAuthLog.setOrderId(orderId);
        hjhUserAuthLog.setOrderStatus(2);
        if(authType!=null&&authType.equals(BankCallConstant.QUERY_TYPE_2)){
            hjhUserAuthLog.setAuthType(5);
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

    @Override
    public ModelAndView getCallbankMV(PaymentAuthPageBean openBean) {
        ModelAndView mv = new ModelAndView();
        
        // 获取共同参数
        String bankCode = PropUtils.getSystem(BankCallConstant.BANK_BANKCODE);
        String bankInstCode = PropUtils.getSystem(BankCallConstant.BANK_INSTCODE);
        String orderDate = GetOrderIdUtils.getOrderDate();
        String txDate = GetOrderIdUtils.getTxDate();
        String txTime = GetOrderIdUtils.getTxTime();
        String seqNo = GetOrderIdUtils.getSeqNo(6);
        // 调用开户接口
        BankCallBean openAccoutBean = new BankCallBean();
        openAccoutBean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        openAccoutBean.setTxCode(BankCallConstant.TXCODE_PAYMENT_AUTH_PAGE);// 消息类型(用户开户)
        openAccoutBean.setInstCode(bankInstCode);// 机构代码
        openAccoutBean.setBankCode(bankCode);
        openAccoutBean.setTxDate(txDate);
        openAccoutBean.setTxTime(txTime);
        openAccoutBean.setSeqNo(seqNo);
        openAccoutBean.setChannel(openBean.getChannel());
        
        openAccoutBean.setAccountId(openBean.getAccountId());
        HjhUserAuthConfig config=CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_PAYMENT_AUTH);
        openAccoutBean.setMaxAmt(config.getPersonalMaxAmount()+"");
        openAccoutBean.setDeadline(GetDate.date2Str(GetDate.countDate(1,config.getAuthPeriod()),new SimpleDateFormat("yyyyMMdd")));
        openAccoutBean.setRemark("");
        openAccoutBean.setRetUrl(openBean.getRetUrl());
        openAccoutBean.setNotifyUrl(openBean.getNotifyUrl());
        openAccoutBean.setForgotPwdUrl(openBean.getForgotPwdUrl());
        
        // 页面调用必须传的
        
        openAccoutBean.setLogBankDetailUrl(BankCallConstant.BANK_URL_PAYMENT_AUTH_PAGE);
        openAccoutBean.setLogOrderId(openBean.getOrderId());
        openAccoutBean.setLogOrderDate(orderDate);
        openAccoutBean.setLogUserId(String.valueOf(openBean.getUserId()));
        openAccoutBean.setLogRemark("缴费授权");
        openAccoutBean.setLogIp(openBean.getIp());
        openAccoutBean.setLogClient(Integer.parseInt(openBean.getPlatform()));
        openBean.setOrderId(openBean.getOrderId());
        try {
            
            mv = BankCallUtils.callApi(openAccoutBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }


}
