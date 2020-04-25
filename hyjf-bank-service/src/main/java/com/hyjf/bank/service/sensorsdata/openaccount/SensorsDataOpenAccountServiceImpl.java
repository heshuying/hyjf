/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.bank.service.sensorsdata.openaccount;

import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.*;
import com.sensorsdata.analytics.javasdk.SensorsAnalytics;
import com.sensorsdata.analytics.javasdk.exceptions.InvalidArgumentException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 神策数据统计:用户开户Service实现类
 *
 * @author liuyang
 * @version SensorsDataOpenAccountServiceImpl, v0.1 2018/9/27 9:15
 */
@Service
public class SensorsDataOpenAccountServiceImpl extends BaseServiceImpl implements SensorsDataOpenAccountService {

    Logger _log = LoggerFactory.getLogger(SensorsDataOpenAccountServiceImpl.class);

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
        // 用户ID
        Integer userId = sensorsDataBean.getUserId();

        if (userId == null || userId <= 0) {
            _log.info("神策数据统计:用户开户相关,用户ID错误");
            return;
        }
        // 根据用户ID 查询用户
        Users users = this.getUsers(userId);
        if (users == null) {
            _log.info("神策数据统计:用户开户相关,根据用户ID查询用户信息失败");
            return;
        }
        // 根据用户ID查询用户详情信息
        UsersInfo usersInfo = this.getUsersInfoByUserId(userId);
        if (usersInfo == null) {
            _log.info("神策数据统计:用户开户相关,根据用户ID查询用户详情信息失败");
            return;
        }

        // 根据用户ID查询开户信息
        BankOpenAccount bankOpenAccount = this.getBankOpenAccount(userId);
        if (bankOpenAccount == null) {
            _log.info("神策数据统计:用户开户相关,根据用户ID查询用户开户信息失败");
            return;
        }

        Map<String, Object> properties = new HashMap<String, Object>();
        // 平台类型
        if (users.getBankAccountEsb() == 0) {
            properties.put("PlatformType", "PC");
        } else if (users.getBankAccountEsb() == 1) {
            properties.put("PlatformType", "wechat");
        } else if (users.getBankAccountEsb() == 2) {
            properties.put("PlatformType", "Android");
        } else if (users.getBankAccountEsb() == 3) {
            properties.put("PlatformType", "iOS");
        }
        // 根据用户ID查询用户银行卡信息
        BankCard bankCard = this.getBankCardByUserId(userId);
        if (bankCard == null) {
            _log.info("未获取到用户银行卡信息:用户ID:[" + userId + "].");
            properties.put("bank_name", "");
        } else {
            properties.put("bank_name", StringUtils.isNotBlank(bankCard.getBank()) ? bankCard.getBank() : "");
        }
        // mod by liuyang 20181029 神策数据统计敏感数据删除 start
        // 身份证号
        // properties.put("id_card", StringUtils.isNotBlank(usersInfo.getIdcard()) ? usersInfo.getIdcard() : "");
        properties.put("id_card", "");
        // 电子账户号
        // properties.put("account_id", StringUtils.isNotBlank(bankOpenAccount.getAccount()) ? bankOpenAccount.getAccount() : "");
        properties.put("account_id", "");
        // 姓名
        // properties.put("true_name", StringUtils.isNotBlank(usersInfo.getTruename()) ? usersInfo.getTruename() : "");
        properties.put("true_name", "");
        // mod by liuyang 20181029 神策数据统计敏感数据删除 end
        // 开户时间
        properties.put("open_time", bankOpenAccount.getCreateTime());
        // 调用神策track事件
        sa.track(String.valueOf(userId), true, "open_success", properties);
        sa.shutdown();
        // 如果redis里存在的话,将redis里的值删除
        if (RedisUtils.exists("SENSORS_DATA_OPEN_ACCOUNT:" + userId)) {
            RedisUtils.del("SENSORS_DATA_OPEN_ACCOUNT:" + userId);
        }
    }

    /**
     * 根据用户ID查询用户银行卡信息
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

}
