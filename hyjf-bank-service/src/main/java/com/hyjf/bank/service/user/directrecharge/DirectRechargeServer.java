package com.hyjf.bank.service.user.directrecharge;


import java.util.Map;

import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.BaseService;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

public interface DirectRechargeServer  extends BaseService {

    /**
     * 
     * 获取页面充值接口
     * @author sunss
     * @param userId
     * @param TxCode
     * @return
     */
    public ModelAndView insertGetMV(UserDirectRechargeBean rechargeBean) throws Exception;

    /**
     * 
     * 用户充值后处理
     * @author sunss
     * @param bean
     * @param params
     * @return
     */
    public JSONObject handleRechargeInfo(BankCallBean bean, Map<String, String> params);

    public BankCard getBankCardByUserId(Integer userId);

    /**
     * 发送神策数据统计MQ
     *
     * @param sensorsDataBean
     */
    void sendSensorsDataMQ(SensorsDataBean sensorsDataBean);
}


