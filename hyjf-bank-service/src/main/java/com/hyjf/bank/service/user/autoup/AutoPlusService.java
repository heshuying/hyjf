package com.hyjf.bank.service.user.autoup;

import com.hyjf.bank.service.BaseService;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.HjhUserAuth;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

public interface AutoPlusService extends BaseService {

    /**
     * 
     * 根据用户id更新用户签约授权信息
     * @param userId
     * @param bean 
     */
    void updateUserAuthInves(Integer userId, BankCallBean bean);
    /**
     * 
     * 插入用户签约授权log
     * @param userId
     * @param bean
     * @param client
     * @param authType
     */
    void insertUserAuthLog(int userId, BankCallBean bean, Integer client, String authType);
    
    /**
     * 
     * 根据手机号查询用户
     * @author sss
     * @param mobile
     * @return
     */
    Users selectUserByMobile(String mobile);
    
    /**
     * 
     * 根据电子账户号查询帐号
     * @author sss
     * @param accountId
     * @return
     */
    BankOpenAccount selectBankOpenAccountByAccountId(String accountId);

    /**
     * 
     * 调用银行的查询接口
     * @author sss
     * @param userId
     * @param type
     * @return
     */
    public BankCallBean getUserAuthQUery(Integer userId,String type);
    
    /**
     * 
     * 根据订单号查询授权码
     * @author sss
     * @param userId
     * @param txcodeDirectRechargeOnline
     * @return
     */
    String selectBankSmsSeq(Integer userId, String txcodeDirectRechargeOnline);
    
    /**
     * 
     * 缴费授权  还款授权
     * @author sunss
     * @return
     */
    BankCallBean getTermsAuthQuery(int userId, String channel);
    
    /**
     * 缴费授权
     * @param userId
     * @param channel
     * @return
     */
    BankCallBean getPayAuthQuery(HjhUserAuth userAuth, String url,String channel);
    BankCallBean getRePayAuthQuery(HjhUserAuth userAuth, String url,String channel);
    /**
     * 
     * 缴费授权  还款授权 异步处理
     * @author sunss
     * @param userId
     * @param retBean
     */
    void updateUserAuth(int userId, BankCallBean retBean);
    
    /**
     * 
     * 检查是否授权过了
     * @author sunss
     * @param userId
     * @return
     */
    boolean checkIsAuth(Integer userId,String txcode);


    /**
     *
     * 出借授权解约
     * @author sunss
     * @return
     */
    BankCallBean cancelInvestAuth(int userId, String channel);

    /**
     *
     * 债转授权解约
     * @author sunss
     * @return
     */
    BankCallBean cancelCreditAuth(int userId, String channel);

    /**
     * 出借授权解约更新表
     * @param userId
     */
    void updateCancelInvestAuth(int userId);

    /**
     * 债转授权解约更新表
     * @param userId
     */
    void updateCancelCreditAuth(int userId);

    /**
     * 判断用户是否有持有中的计划。如果有，则不能解除出借授权和债转授权
     * @param userId
     * @return
     */
    boolean canCancelAuth(int userId);
    
    /**
    *
    * 缴费授权解约
    * @author sunss
    * @return
    */
   BankCallBean cancelPayAuth(int userId,String channel);
   public void updateCancelPayAuth(int userId) ;
   /**
    * 还款授权解约
    * @param userId
    * @param channel
    * @return
    */
   BankCallBean cancelRePayAuth(int userId,String channel);
   public void updateCancelRePayAuth(int userId) ;
   
   /**
    * 
    * 获得用户授权状态信息  
    * 自动投标状态          缴费授权状态      还款授权状态    
    * @author sunss
    * @return
    */
   public HjhUserAuth getUserAuthState(HjhUserAuth auth) ;

    void insertUserAuthLog2(int userId,BankCallBean retBean,String authType);
    
    /**
     * 
     * 自动出借 自动债转修改状态、到期时间、金额等信息
     * @author sunss
     * @param userId
     * @param retBean
     */
    HjhUserAuth updateUserAuthState(Integer userId, BankCallBean retBean);
}
