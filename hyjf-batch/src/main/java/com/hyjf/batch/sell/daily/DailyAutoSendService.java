package com.hyjf.batch.sell.daily;

import com.hyjf.mybatis.model.auto.SellDailyDistribution;

import java.util.Date;
import java.util.List;

/**
 * @author xiasq
 * @version DailyAutoSendService, v0.1 2018/8/3 10:00
 */
public interface DailyAutoSendService {

    /**
     * 获取后台发送邮件配置
     * @return
     */
    List<SellDailyDistribution> listSellDailyDistribution();
    /**
     * 判断某天是否是工作日
     * @param date
     * @return
     */
    boolean isWorkdateOnSomeDay(Date date);

    /**
     * 判断当天是不是当月第一个工作日
     * @return
     */
    boolean isTodayFirstWorkdayOnMonth();

    /**
     * 发送销售日报邮件
     * @param sellDailyDistribution
     */
    void sendMail(SellDailyDistribution sellDailyDistribution);
}
