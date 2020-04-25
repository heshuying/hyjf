package com.hyjf.wechat.service.withdraw;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.mybatis.model.auto.Accountwithdraw;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BanksConfig;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.wechat.base.BaseService;

import java.util.List;
import java.util.Map;

/**
 * Created by cuigq on 2018/2/13.
 */
public interface WxBankWithdrawService extends BaseService {

    List<BankCard> getBankCardByUserId(Integer userId);

    BanksConfig getBanksConfigByBankId(Integer bankId);

    String getWithdrawFee(Integer userId, String cardNo);

    Accountwithdraw getAccountWithdrawByOrdId(String ordId);

    int updateBeforeCash(BankCallBean bean, Map<String, String> params);

    JSONObject handlerAfterCash(BankCallBean bean, Map<String, String> params) throws Exception;

    /**
     * 神策数据统计发送MQ
     *
     * @param sensorsDataBean
     */
    void sendSensorsDataMQ(SensorsDataBean sensorsDataBean);
}
