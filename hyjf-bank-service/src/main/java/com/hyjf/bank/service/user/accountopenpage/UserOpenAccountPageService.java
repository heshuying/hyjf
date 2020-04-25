package com.hyjf.bank.service.user.accountopenpage;


import java.util.Map;

import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.mybatis.model.auto.BankCard;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.bank.service.BaseService;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

public interface UserOpenAccountPageService  extends BaseService {

    /**
     * 
     * 获取开户接口请求参数
     * @author sunss
     * @param userId
     * @param TxCode
     * @return
     */
    public ModelAndView getCallbankMV(OpenAccountPageBean openBean);

    /**
     * 
     * 调用江西银行查询银行卡信息  并在本地进行绑定
     * @author sunss
     * @param bankCallBean
     * @return
     */
    public boolean updateCardNoToBank(BankCallBean bankCallBean);

    /**
     * 
     * 修改用户属性为借款人
     * @author sunss
     * @param userId
     * @param i
     * @return
     */
    public boolean updateUserRoleId(int userId, int roleId);

    /**
     * 
     * 检查参数有效性
     * @author sunss
     * @param userId
     * @param mobile
     * @param name
     * @param idNo
     * @param cardNo 
     * @return
     */
    public Map<String, String> checkParm(Integer userId, String mobile, String name);
    
    /**
     * 开户成功后,更新用户账户信息
     * 
     * @param openAccountResult
     * @return
     */
    boolean updateUserAccount(BankCallBean openAccountResult);
    
    /**
     * 更新开户日志表
     * 
     * @param userId
     * @param logOrderId
     * @param status
     */
    void updateUserAccountLog(Integer userId, String logOrderId, int status);

    /**
     * 
     * 开户+设置交易密码的接口
     * @author sunss
     * @param openBean
     * @return
     */
    public ModelAndView getCallbankEncryptPageMV(OpenAccountPageBean openBean);

    /**
     * 根据用户ID查询银行卡
     * @param userid
     * @return
     */
    BankCard getBankCardByUserId(String userid);


    /**
     * 开户成功后,发送神策数据统计MQ
     *
     * @param sensorsDataBean
     */
    void sendSensorsDataMQ(SensorsDataBean sensorsDataBean);
}


