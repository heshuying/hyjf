/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.bank.service.sensorsdata.recharge;

import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.AccountRecharge;
import com.hyjf.mybatis.model.auto.AccountRechargeExample;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankCardExample;
import com.sensorsdata.analytics.javasdk.SensorsAnalytics;
import com.sensorsdata.analytics.javasdk.exceptions.InvalidArgumentException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 神策数据统计:充值业务相关Service实现类
 *
 * @author liuyang
 * @version SensorsDataRechargeServiceImpl, v0.1 2018/7/11 17:53
 */
@Service
public class SensorsDataRechargeServiceImpl extends BaseServiceImpl implements SensorsDataRechargeService {

    Logger _log = LoggerFactory.getLogger(SensorsDataRechargeServiceImpl.class);

    /**
     * 发送神策埋点数据
     *
     * @param sensorsDataBean
     * @throws IOException
     * @throws InvalidArgumentException
     */
    @Override
    public void sendSensorsData(SensorsDataBean sensorsDataBean) throws IOException, InvalidArgumentException {

        // log文件存放位置
        String logFilePath = PropUtils.getSystem("sensors.data.log.path");
        // 初始化神策SDK
        SensorsAnalytics sa = new SensorsAnalytics(new SensorsAnalytics.ConcurrentLoggingConsumer(logFilePath + "sensorsData.log"));
        // 充值订单号
        String rechageOrderId = sensorsDataBean.getOrderId();
        // 根据充值订单号查询充值记录
        AccountRecharge accountRecharge = this.selectAccountRechargeByOrderId(rechageOrderId);
        if (accountRecharge == null) {
            _log.error("根据充值订单号查询用户充值记录不存在,充值订单号:[" + rechageOrderId + "].");
            return;
        }
        Map<String, Object> properties = new HashMap<String, Object>();

        // 充值金额
        properties.put("recharge_amount", accountRecharge.getMoney());
        // 充值时间
        properties.put("recharge_time", GetDate.getDateTimeMyTime(accountRecharge.getCreateTime()));
        // 查询用户银行卡信息
        Integer userId = accountRecharge.getUserId();
        BankCard bankCard = this.getBankCardByUserId(userId);
        if (bankCard == null) {
            properties.put("bank_name", "");
            properties.put("bank_card_number", "");
        } else {
            properties.put("bank_name", StringUtils.isBlank(bankCard.getBank()) ? "" : bankCard.getBank());
            //  mod by liuyang 20181029 神策数据统计敏感数据删除 start
            // properties.put("bank_card_number", StringUtils.isBlank(bankCard.getCardNo()) ? "" : bankCard.getCardNo());
            properties.put("bank_card_number", "");
            //  mod by liuyang 20181029 神策数据统计敏感数据删除 end
        }
        // 充值类型
        properties.put("recharge_type", "快捷充值");
        // 是否是首次充值
        properties.put("$is_first_time", this.isFirstRecharge(userId));
        // 是否成功
        properties.put("success", true);
        // 错误信息
        properties.put("error_message", "");

        // 平台类型
        if (accountRecharge.getClient() == 0) {
            properties.put("PlatformType", "PC");
        } else if (accountRecharge.getClient() == 1) {
            properties.put("PlatformType", "wechat");
        } else if (accountRecharge.getClient() == 2) {
            properties.put("PlatformType", "Android");
        } else if (accountRecharge.getClient() == 3) {
            properties.put("PlatformType", "iOS");
        }
        // 调用神策track事件
        sa.track(String.valueOf(userId), true, "recharge_result", properties);
        sa.shutdown();

    }


    /**
     * 根据充值订单号查询用户充值记录
     *
     * @param rechageOrderId
     * @return
     */
    private AccountRecharge selectAccountRechargeByOrderId(String rechageOrderId) {
        AccountRechargeExample example = new AccountRechargeExample();
        AccountRechargeExample.Criteria cra = example.createCriteria();
        cra.andNidEqualTo(rechageOrderId);
        List<AccountRecharge> list = this.accountRechargeMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据用户ID 查询用户银行卡信息
     *
     * @param userId
     * @return
     */
    private BankCard getBankCardByUserId(Integer userId) {
        BankCardExample example = new BankCardExample();
        BankCardExample.Criteria cra = example.createCriteria();
        cra.andUserIdEqualTo(userId);
        List<BankCard> list = this.bankCardMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据用户ID查询是否是首次充值
     */
    private boolean isFirstRecharge(Integer userId) {
        AccountRechargeExample example = new AccountRechargeExample();
        AccountRechargeExample.Criteria cra = example.createCriteria();
        cra.andUserIdEqualTo(userId);
        cra.andStatusEqualTo(2);
        List<AccountRecharge> list = accountRechargeMapper.selectByExample(example);
        // 如果充值记录中只有一条 说明是首次充值
        if (list != null && list.size() == 1) {
            return true;
        }
        return false;
    }

}
