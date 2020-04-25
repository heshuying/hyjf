package com.hyjf.api.surong.user.cashauth;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BankOpenAccountExample;
import com.hyjf.mybatis.model.auto.BankSmsAuthCode;
import com.hyjf.mybatis.model.auto.BankSmsAuthCodeExample;
import com.hyjf.mybatis.model.auto.HjhUserAuth;
import com.hyjf.mybatis.model.auto.HjhUserAuthExample;
import com.hyjf.mybatis.model.auto.HjhUserAuthLog;
import com.hyjf.mybatis.model.auto.HjhUserAuthLogExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

@Service
public class suCashAuthServiceImpl extends BaseServiceImpl implements suCashAuthService{

    /**
     * 根据电子账户号查询用户开户信息
     * 
     * @param accountId
     * @return
     */
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
    public String selectBankSmsLog(Integer userId, String srvTxCode) {
        BankSmsAuthCodeExample example = new BankSmsAuthCodeExample();
        example.createCriteria().andUserIdEqualTo(userId).andSrvTxCodeEqualTo(srvTxCode);
        List<BankSmsAuthCode> smsAuthCodeList = bankSmsAuthCodeMapper.selectByExample(example);
        if (smsAuthCodeList != null && smsAuthCodeList.size() == 1) {
            BankSmsAuthCode smsAuthCode = smsAuthCodeList.get(0);
            return smsAuthCode.getSmsSeq();
        }
        return null;
    }
    
    @Override
    public Map<String,Object> selectUserAuth(Integer userId){
        Map<String, Object> map = hjhPlanCustomizeMapper.selectUserAppointmentInfo(userId+"");
        return map;
    }

    @Override
    public boolean insertUserAuthInves(Integer userId, BankCallBean bean) {
        HjhUserAuth hjhUserAuth = new HjhUserAuth();
        Users users = this.getUsers(userId);
        hjhUserAuth.setUserId(userId);
        hjhUserAuth.setUserName(users.getUsername());
        hjhUserAuth.setCreateUser(userId);
        hjhUserAuth.setCreateTime(GetDate.getNowTime10());
        hjhUserAuth.setDelFlg(0);
        hjhUserAuth.setAutoOrderId(bean.getOrderId());
        hjhUserAuth.setAutoInvesStatus(Integer.parseInt(bean.getAutoBid()));
        hjhUserAuth.setAutoCreditStatus(Integer.parseInt(bean.getAutoTransfer()));
        hjhUserAuth.setAutoWithdrawStatus(Integer.parseInt(bean.getAgreeWithdraw()));
        hjhUserAuth.setAutoConsumeStatus(Integer.parseInt(bean.getDirectConsume()));
        hjhUserAuth.setAutoCreateTime(GetDate.getNowTime10());
        hjhUserAuth.setUpdateTime(GetDate.getNowTime10());
        hjhUserAuth.setUpdateUser(userId);
        boolean flag = hjhUserAuthMapper.insertSelective(hjhUserAuth) > 0 ? true : false;
        return flag;
    }
    
    @Override
    public boolean updateUserAuthInves(Integer userId, BankCallBean bean) {
        
        HjhUserAuth hjhUserAuth = new HjhUserAuth();
        HjhUserAuthExample example=new HjhUserAuthExample();
        example.createCriteria().andUserIdEqualTo(userId);
        Users users = this.getUsers(userId);
        hjhUserAuth.setUserId(userId);
        hjhUserAuth.setUserName(users.getUsername());
        hjhUserAuth.setCreateUser(userId);
        hjhUserAuth.setCreateTime(GetDate.getNowTime10());
        hjhUserAuth.setDelFlg(0);
        hjhUserAuth.setAutoOrderId(bean.getOrderId());
        hjhUserAuth.setAutoInvesStatus(Integer.parseInt(bean.getAutoBid()));
        hjhUserAuth.setAutoCreditStatus(Integer.parseInt(bean.getAutoTransfer()));
        hjhUserAuth.setAutoWithdrawStatus(Integer.parseInt(bean.getAgreeWithdraw()));
        hjhUserAuth.setAutoConsumeStatus(Integer.parseInt(bean.getDirectConsume()));
        hjhUserAuth.setAutoCreateTime(GetDate.getNowTime10());
        hjhUserAuth.setUpdateTime(GetDate.getNowTime10());
        hjhUserAuth.setUpdateUser(userId);
        
        boolean flag =  hjhUserAuthMapper.updateByExampleSelective(hjhUserAuth,example)  > 0 ? true : false;

        return flag;
    }

    @Override
    public boolean insertUserAuthLog(Integer userId,BankCallBean bean,String auth_type) {
        HjhUserAuthLog hjhUserAuthLog = new HjhUserAuthLog();
        Users users = this.getUsers(userId);
        hjhUserAuthLog.setUserId(userId);                     
        hjhUserAuthLog.setUserName(users.getUsername());                       
        hjhUserAuthLog.setOrderId(bean.getLogOrderId());                        
        hjhUserAuthLog.setOrderStatus(2);                        
        hjhUserAuthLog.setAuthType(Integer.parseInt(auth_type));                                         
        hjhUserAuthLog.setOperateEsb(4);                     
        hjhUserAuthLog.setAuthCreateTime(GetDate.getNowTime10());                        
        hjhUserAuthLog.setCreateTime(GetDate.getNowTime10());                     
        hjhUserAuthLog.setCreateUser(userId);                     
        hjhUserAuthLog.setUpdateTime(GetDate.getNowTime10());                     
        hjhUserAuthLog.setUpdateUser(userId);                     
        hjhUserAuthLog.setDelFlg(0);                     
        boolean flag = hjhUserAuthLogMapper.insertSelective(hjhUserAuthLog) > 0 ? true : false;
        return flag;
    }
    
    @Override
    public boolean updateUserAuthLog(Integer userId,String orderId){
        HjhUserAuthLog hjhUserAuthLog = new HjhUserAuthLog();
        HjhUserAuthLogExample example=new HjhUserAuthLogExample();
        example.createCriteria().andOrderIdEqualTo(orderId);
        hjhUserAuthLog.setOrderStatus(1);                        
        hjhUserAuthLog.setUpdateTime(GetDate.getNowTime10());                     
        hjhUserAuthLog.setUpdateUser(userId);                     
        hjhUserAuthLog.setDelFlg(0);   
        boolean flag = hjhUserAuthLogMapper.updateByExampleSelective(hjhUserAuthLog, example)>0?true : false;
        return flag;
    }
    
}
