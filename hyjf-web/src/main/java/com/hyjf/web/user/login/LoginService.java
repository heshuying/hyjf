package com.hyjf.web.user.login;

import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.web.BaseService;
import com.hyjf.web.common.WebViewUser;

public interface LoginService extends BaseService {

    public boolean existUser(String userNameOrMobile);

    public boolean existEmail(String email);

    /**
     * 检查密码是否正确
     * 
     * @return -1:检查密码失败,用户不存在|-2:检查密码失败,存在多个相同用户|-3:检查密码失败,密码错误
     */
    public int queryPasswordAction(String username, String password);

    /**
     * 检查密码是否正确,传递的password加了密
     * 
     * @return -1:检查密码失败,用户不存在|-2:检查密码失败,存在多个相同用户|-3:检查密码失败,密码错误
     */
    public int queryPasswordAction2(String username, String password);

    /**
     * 登录
     * 
     * @return -1:登录失败,用户不存在|-2:登录失败,存在多个相同用户|-3:登录失败,密码错误
     */
    public int updateLoginInAction(String username, String password, String ip);

    /**
     * 登录,传递的password加了密
     * 
     * @return -1:登录失败,用户不存在|-2:登录失败,存在多个相同用户|-3:登录失败,密码错误
     */
    public int updateLoginInAction2(String username, String password, String ip);

    public void insertAccount() throws Exception;

    /**
     * 获取userid
     * 
     * @param userId
     * @return
     */
    public WebViewUser getWebViewUserByUserId(Integer userId);

    /**
     * 根据用户名获取用户id
     * @param userName
     * @return
     */
    public Integer getUserIdByUsername(String userName);

    /**
     * 
     * 根据计划编号查询计划
     * @author renxingchen
     * @param bid
     * @return
     */
    public DebtPlan queryPlanByNid(String bid);

    /**
     * 用户名或手机号查询用户信息
     * @param loginUserName
     * @return
     */
    Integer getUser(String loginUserName);

    /**
     * 发送神策数据统计MQ
     *
     * @param sensorsDataBean
     */
    void sendSensorsDataMQ(SensorsDataBean sensorsDataBean);
}
