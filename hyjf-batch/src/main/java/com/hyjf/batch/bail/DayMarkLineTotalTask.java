package com.hyjf.batch.bail;

import com.hyjf.batch.bank.borrow.apicron.BorrowRepayToRedisTask;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.calculate.DateUtils;
import com.hyjf.common.util.RedisConstants;
import com.hyjf.mybatis.model.auto.HjhBailConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 前一日未用日发标额度累计
 *
 * @author liushouyi
 */
public class DayMarkLineTotalTask {

    Logger _log = LoggerFactory.getLogger(BorrowRepayToRedisTask.class);

    /**
     * 运行状态
     */
    private static int isRun = 0;

    @Autowired
    DayMarkLineTotalService dayMarkLineTotalService;

    public void run() {
        // 调用还款接口
        taskAssign();
    }

    /**
     * 前一日未用日发表额度累计
     *
     * @return
     */
    private void taskAssign() {
        if (isRun == 1) {
            return;
        }
        isRun = 1;
        _log.info("【日发标额度累计】处理开始。");
        try {

            // 获取当天日期yyyyMMdd
            SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
            String nowDay = yyyyMMdd.format(new Date());
            //获取前一天时间返回时间类型 yyyyMMdd
            String beforeDay = DateUtils.getBeforeDateOfDay();

            // 判断是否当月第一天
            boolean startDay = "01".equals(nowDay.substring(6, 8));
            if (startDay) {
                // 拉取所有保证金配置数据循环
                List<HjhBailConfig> hjhBailConfigList = this.dayMarkLineTotalService.selectAccumulateListAll();
                if (null == hjhBailConfigList || hjhBailConfigList.size() <= 0) {
                    _log.info("【日发标额度累计】月初拉取所有保证金配置的数据。");
                    return;
                }
                for (HjhBailConfig hjhBailConfig : hjhBailConfigList) {
                    // 获取当前机构的日累计额度key值
                    String redisKeyDMA = RedisConstants.DAY_MARK_ACCUMULATE + hjhBailConfig.getInstCode();
                    // 设定当月累加redis为0
                    RedisUtils.set(redisKeyDMA, "0");
                    // 删除前一天redis
                    redisDeal(startDay,nowDay,beforeDay,hjhBailConfig.getInstCode());
                }
            } else {
                // 拉取所有保证金配置数据循环
                List<HjhBailConfig> hjhBailConfigList = this.dayMarkLineTotalService.selectAccumulateList();
                if (null == hjhBailConfigList || hjhBailConfigList.size() <= 0) {
                    _log.info("【日发标额度累计】非月初拉取当前在用保证金配置的数据。");
                    return;
                }
                for (HjhBailConfig hjhBailConfig : hjhBailConfigList) {
                    // 获取当前机构的日累计额度key值
                    String redisKeyDMA = RedisConstants.DAY_MARK_ACCUMULATE + hjhBailConfig.getInstCode();
                    // 前一天已用额度
                    String redisKeyDU = RedisConstants.DAY_USED + hjhBailConfig.getInstCode() + "_" + beforeDay;
                    // 日推标额度上限
                    String redisKeyDML = RedisConstants.DAY_MARK_LINE + hjhBailConfig.getInstCode();
                    // 打开累计额度开关的
                    if (hjhBailConfig.getIsAccumulate() == 1) {
                        try {
                            // 累加前日剩余额度计入redis
                            BigDecimal redisValueDMA = BigDecimal.ZERO;
                            if (RedisUtils.exists(redisKeyDMA)) {
                                redisValueDMA = new BigDecimal(RedisUtils.get(redisKeyDMA));
                            }
                            // 日额度上限(redis未记录的话取数据库的值)
                            BigDecimal redisValueDML = hjhBailConfig.getDayMarkLine();
                            if (RedisUtils.exists(redisKeyDML)) {
                                redisValueDML = new BigDecimal(RedisUtils.get(redisKeyDML));
                            }
                            // 日使用额度(没有的话说明前一天没使用过)
                            BigDecimal redisValueDU = BigDecimal.ZERO;
                            if (RedisUtils.exists(redisKeyDU)){
                                redisValueDU = new BigDecimal(RedisUtils.get(redisKeyDU));
                            }
                            // 日剩余累加+日推标额度-日使用额度(正常的情况这里最小算出为0)
                            redisValueDMA = redisValueDMA.add(redisValueDML.subtract(redisValueDU));
                            if (redisValueDMA.compareTo(BigDecimal.ZERO) >  0){
                                RedisUtils.set(redisKeyDMA, redisValueDMA.toString());
                            } else {
                                RedisUtils.set(redisKeyDMA, "0");
                            }
                        } catch (Exception e) {
                            _log.error("【日发标额度累计】计算日累计额度出错！");
                        }
                    }
                    // 设定当天已用日额度为0、删除前一天redis
                    redisDeal(startDay,nowDay,beforeDay,hjhBailConfig.getInstCode());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            _log.info("【日发标额度累计】处理结束。");
            isRun = 0;
        }
    }

    private void redisDeal(boolean startDay,String nowDay,String beforeDay,String instCode){
        if (RedisUtils.exists(RedisConstants.DAY_USED + instCode + "_" + beforeDay)){
            // 删除前一天已用额度
            RedisUtils.del(RedisConstants.DAY_USED + instCode + "_" + beforeDay);
        }
        // 当月第一天设置当月已用额度为0、删除上月累计redis
        if (startDay) {
            // 上月redis存在的话删除
            if (RedisUtils.exists(RedisConstants.MONTH_USED + instCode + "_" + beforeDay.substring(0, 6))) {
                RedisUtils.del(RedisConstants.MONTH_USED + instCode + "_" + beforeDay.substring(0, 6));
            }
        }
    }
}
