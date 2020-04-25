package com.hyjf.bank.service.user.auto;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.common.util.CommonUtils;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.HjhUserAuth;
import com.hyjf.mybatis.model.auto.HjhUserAuthExample;
import com.hyjf.mybatis.model.auto.HjhUserAuthLog;
import com.hyjf.mybatis.model.auto.HjhUserAuthLogExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

@Service
public class AutoServiceImpl extends BaseServiceImpl implements AutoService {
    /**
     * 
     * 根据用户id更新用户签约授权信息
     * @author pcc
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
            hjhUserAuth.setUserId(user.getUserId());
            hjhUserAuth.setUserName(user.getUsername());
            hjhUserAuth.setAutoInvesStatus(1);
            hjhUserAuth.setAutoCreateTime(nowTime);
            hjhUserAuth.setAutoCreditStatus(1);
            hjhUserAuth.setAutoOrderId(bean.getOrderId());
            hjhUserAuth.setCreateUser(user.getUserId());
            hjhUserAuth.setCreateTime(nowTime);
            hjhUserAuth.setUpdateTime(nowTime);
            hjhUserAuth.setUpdateUser(userId);
            hjhUserAuth.setAutoWithdrawStatus(0);
            hjhUserAuth.setAutoConsumeStatus(0);
            hjhUserAuth.setDelFlg(0);
            hjhUserAuthMapper.insertSelective(hjhUserAuth);
        }else{
            HjhUserAuth updateHjhUserAuth=new HjhUserAuth();
            updateHjhUserAuth.setId(hjhUserAuth.getId());
            updateHjhUserAuth.setAutoInvesStatus(1);
            updateHjhUserAuth.setAutoCreditStatus(1);
            updateHjhUserAuth.setAutoCreateTime(nowTime);
            updateHjhUserAuth.setUpdateTime(nowTime);
            updateHjhUserAuth.setUpdateUser(userId);
            updateHjhUserAuth.setAutoOrderId(bean.getOrderId());
            hjhUserAuthMapper.updateByPrimaryKeySelective(hjhUserAuth);
        }
        
        
        
    }
    /**
     * 
     * 根据用户id查询用户签约授权信息
     * @author pcc
     * @param userId
     * @return
     */
    @Override
    public HjhUserAuth getHjhUserAuthByUserId(Integer userId) {
        HjhUserAuthExample example=new HjhUserAuthExample();
        example.createCriteria().andUserIdEqualTo(userId);
        List<HjhUserAuth> list=hjhUserAuthMapper.selectByExample(example);
        if(list!=null&& list.size()>0){
            return list.get(0);
        }else{
            return null;    
        }
        
    }
    /**
     * 
     * 插入用户签约授权log
     * @author pcc
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
        hjhUserAuthLog.setOrderId(bean.getOrderId());
        hjhUserAuthLog.setOrderStatus(2);
        hjhUserAuthLog.setAuthType(Integer.valueOf(authType));
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
     * 检查服务费授权状态
     * @author sunss
     * @param userId
     * @return
     * @see com.hyjf.bank.service.user.auto.AutoService#checkPaymentAuthStatus(java.lang.Integer)
     */
    @Override
    public boolean checkPaymentAuthStatus(Integer userId) {
        // 如果用户ID没有 直接成功吧 不会出现这种
        if (userId == null) {
            return true;
        }
        // 检查开关是否打开 没打开 不用校验
        if (CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_PAYMENT_AUTH).getEnabledStatus() - 1 != 0) {
            return true;
        }
        HjhUserAuth auth = getHjhUserAuthByUserId(userId);
        if (auth == null || auth.getAutoPaymentStatus() - 1 != 0) {
            return false;
        }
        return true;
    }
    
    /**
     * 检查还款授权状态
     * 此处为实现/覆载说明
     * @author sunss
     * @param userId
     * @return
     * @see com.hyjf.bank.service.user.auto.AutoService#checkRepayAuthStatus(java.lang.Integer)
     */
    @Override
    public boolean checkRepayAuthStatus(Integer userId) {
     // 如果用户ID没有 直接成功吧 不会出现这种
        if (userId == null) {
            return true;
        }
        // 检查开关是否打开 没打开 不用校验
        if (CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_REPAYMENT_AUTH).getEnabledStatus() - 1 != 0) {
            return true;
        }
        HjhUserAuth auth = getHjhUserAuthByUserId(userId);
        if (auth == null || auth.getAutoRepayStatus() - 1 != 0) {
            return false;
        }
        return true;
    }


}
