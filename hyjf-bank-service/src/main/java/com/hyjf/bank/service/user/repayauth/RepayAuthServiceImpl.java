package com.hyjf.bank.service.user.repayauth;

import java.text.SimpleDateFormat;
import java.util.List;

import com.hyjf.common.util.CommonUtils;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.HjhUserAuthConfig;
import com.hyjf.common.util.PropUtils;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

import org.springframework.stereotype.Service;

import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BankOpenAccountExample;
import com.hyjf.mybatis.model.auto.BankSmsAuthCode;
import com.hyjf.mybatis.model.auto.BankSmsAuthCodeExample;
import com.hyjf.mybatis.model.auto.HjhUserAuth;
import com.hyjf.mybatis.model.auto.HjhUserAuthExample;
import com.hyjf.mybatis.model.auto.HjhUserAuthLog;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

import org.springframework.web.servlet.ModelAndView;

@Service
public class RepayAuthServiceImpl extends BaseServiceImpl implements RepayAuthService {
    
    
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
     */
    @Override
    public void insertUserAuthLog(int userId, BankCallBean bean, Integer client) {
        Integer nowTime=GetDate.getNowTime10();
        Users user=this.getUsers(userId);
        HjhUserAuthLog hjhUserAuthLog=new HjhUserAuthLog();
        hjhUserAuthLog.setUserId(user.getUserId());
        hjhUserAuthLog.setUserName(user.getUsername());
        hjhUserAuthLog.setOrderId(bean.getLogOrderId());
        hjhUserAuthLog.setOrderStatus(2);
        hjhUserAuthLog.setAuthType(6);
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
    public ModelAndView getCallbankMV(RepayAuthBean openBean) {
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
        openAccoutBean.setTxCode(BankCallConstant.TXCODE_REPAY_AUTH_PAGE);// 消息类型(用户开户)
        openAccoutBean.setInstCode(bankInstCode);// 机构代码
        openAccoutBean.setBankCode(bankCode);
        openAccoutBean.setTxDate(txDate);
        openAccoutBean.setTxTime(txTime);
        openAccoutBean.setSeqNo(seqNo);
        openAccoutBean.setChannel(openBean.getChannel());

        openAccoutBean.setAccountId(openBean.getAccountId());
        
        HjhUserAuthConfig config=CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_REPAYMENT_AUTH);
        openAccoutBean.setMaxAmt(config.getPersonalMaxAmount()+"");
        // 需要修改
        openAccoutBean.setDeadline(GetDate.date2Str(GetDate.countDate(1,config.getAuthPeriod()),new SimpleDateFormat("yyyyMMdd")));
        // 生产商应该是五年
        //openAccoutBean.setDeadline(GetDate.date2Str(GetDate.countDate(1,5),new SimpleDateFormat("yyyyMMdd")));
        openAccoutBean.setRemark("");
        openAccoutBean.setRetUrl(openBean.getRetUrl());
        openAccoutBean.setNotifyUrl(openBean.getNotifyUrl());
        openAccoutBean.setForgotPwdUrl(openBean.getForgotPwdUrl());

        // 页面调用必须传的

        openAccoutBean.setLogBankDetailUrl(BankCallConstant.BANK_URL_REPAY_AUTH_PAGE);
        openAccoutBean.setLogOrderId(openBean.getOrderId());
        openAccoutBean.setLogOrderDate(orderDate);
        openAccoutBean.setLogUserId(String.valueOf(openBean.getUserId()));
        openAccoutBean.setLogRemark("还款授权");
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



}
