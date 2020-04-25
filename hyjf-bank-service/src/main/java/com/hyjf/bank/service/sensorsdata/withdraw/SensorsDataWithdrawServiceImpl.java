/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.bank.service.sensorsdata.withdraw;

import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.Accountwithdraw;
import com.hyjf.mybatis.model.auto.AccountwithdrawExample;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankCardExample;
import com.sensorsdata.analytics.javasdk.SensorsAnalytics;
import com.sensorsdata.analytics.javasdk.exceptions.InvalidArgumentException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 神策数据统计:提现业务相关Service实现类
 *
 * @author liuyang
 * @version SensorsDataWithdrawServiceImpl, v0.1 2018/7/13 15:13
 */
@Service
public class SensorsDataWithdrawServiceImpl extends BaseServiceImpl implements SensorsDataWithdrawService {

    Logger _log = LoggerFactory.getLogger(SensorsDataWithdrawServiceImpl.class);



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
        // 提现订单号
        String withdrawOrderId = sensorsDataBean.getOrderId();
        if (StringUtils.isBlank(withdrawOrderId)) {
            _log.error("提现订单号为空");
            return;
        }
        // 根据提现订单号查询提现订单
        Accountwithdraw accountwithdraw = this.selectAccountWithdraw(withdrawOrderId);
        if (accountwithdraw == null) {
            _log.error("根据提现订单号查询提现订单失败,提现订单号:[" + accountwithdraw);
            return;
        }
        // 用户ID
        Integer userId = accountwithdraw.getUserId();
        Map<String, Object> properties = new HashMap<String, Object>();
        // 提现金额
        properties.put("withdraw_amount", accountwithdraw.getTotal());
        // 提现手续费
        properties.put("fee_amount", accountwithdraw.getFee());
        // 实际到账金额
        properties.put("arrive_money", accountwithdraw.getCredited());
        // 根据用户ID 查询用户银行卡信息
        BankCard bankCard = this.getBankCardByUserId(userId);
        if (bankCard == null) {
            properties.put("bank_name", "");
            properties.put("bank_card_number", "");
        } else {
            // 银行名称
            properties.put("bank_name", StringUtils.isBlank(bankCard.getBank()) ? "" : bankCard.getBank());
            // 银行卡号
            // mod by liuyang 20181029 神策数据统计敏感数据删除 start
            // properties.put("bank_card_number", StringUtils.isBlank(bankCard.getCardNo()) ? "" : bankCard.getCardNo());
            properties.put("bank_card_number", "");
            //  mod by liuyang 20181029 神策数据统计敏感数据删除 end

        }
        // 提现金额大于 50001,设置提现方式
        if (accountwithdraw.getTotal().compareTo(new BigDecimal(50001)) >= 0) {
            properties.put("withdraw_type", "大额提现");
        } else {
            properties.put("withdraw_type", "普通提现");
        }
        // 提现成功时间
        properties.put("success_time",GetDate.getDateTimeMyTime(Integer.parseInt(accountwithdraw.getAddtime())));
        // 是否成功
        properties.put("success",true);
        // 是否是首次
        properties.put("$is_first_time", this.isFirstWithdarw(userId));
        // 错误信息
        properties.put("error_message", "");
        // 平台类型
        if (accountwithdraw.getClient() == 0) {
            properties.put("PlatformType", "PC");
        } else if (accountwithdraw.getClient() == 1) {
            properties.put("PlatformType", "wechat");
        } else if (accountwithdraw.getClient() == 2) {
            properties.put("PlatformType", "Android");
        } else if (accountwithdraw.getClient() == 3) {
            properties.put("PlatformType", "iOS");
        }
        // 调用神策track事件
        sa.track(String.valueOf(userId), true, "withdraw_result", properties);
        sa.shutdown();
    }

    /**
     * 根据提现订单号查询提现记录
     *
     * @param withdrawOrderId
     * @return
     */
    private Accountwithdraw selectAccountWithdraw(String withdrawOrderId) {
        AccountwithdrawExample example = new AccountwithdrawExample();
        AccountwithdrawExample.Criteria cra = example.createCriteria();
        cra.andNidEqualTo(withdrawOrderId);
        //提现成功
        cra.andStatusEqualTo(2);
        List<Accountwithdraw> list = this.accountwithdrawMapper.selectByExample(example);
        if (list != null && list.size() == 1) {
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
     * 根据用户Id查询用户是否是首次提现
     *
     * @param userId
     * @return
     */
    private boolean isFirstWithdarw(Integer userId) {
        AccountwithdrawExample example = new AccountwithdrawExample();
        AccountwithdrawExample.Criteria cra = example.createCriteria();
        cra.andUserIdEqualTo(userId);
        cra.andStatusEqualTo(2);
        List<Accountwithdraw> list = this.accountwithdrawMapper.selectByExample(example);
        if (list != null && list.size() == 1) {
            return true;
        }
        return false;
    }

}
