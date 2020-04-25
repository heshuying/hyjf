/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.batch.hjh.hjhcheck;

import com.alibaba.fastjson.JSON;
import com.hyjf.batch.hjh.borrow.tender.AutoTenderTask;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.chatbot.DDChatbot;
import com.hyjf.common.chatbot.DDChatbotBean;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.RedisConstants;
import com.hyjf.mybatis.model.auto.HjhPlan;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

/**
 * 汇计划各计划开放额度校验预警
 * @author liubin
 * @version HjhOpenAccountCheckTask, v0.1 2018/7/25 10:38
 */
public class HjhOpenAccountCheckTask {
    Logger _log = LoggerFactory.getLogger(AutoTenderTask.class);

    @Autowired
    private HjhOpenAccountCheckTaskService hjhOpenAccountCheckTaskService;

    // 运行状态h
    private static int isRun = 0;

    private static final String ENV_TEST = PropUtils.getSystem("hyjf.env.test").trim();
    /**
     * 调用任务实际方法接口
     */
    public void run() {
        process();
    }

    /**
     * 汇计划各计划开放额度校验预警
     *
     * @return
     */
    private boolean process() {
        if (isRun == 0) {
            _log.info("汇计划各计划开放额度校验预警任务 开始... ");

            isRun = 1;
            if("true".equals(ENV_TEST)){
                return true;
            }
            try {
                // 取得整个汇计划列表
                List<HjhPlan> hjhPlanList = this.hjhOpenAccountCheckTaskService.selectHjhPlanList();
                _log.info("校验汇计划计划个数：" + hjhPlanList.size());
                // 校验每个计划的开放额度，输出结果
                for (HjhPlan hjhPlan : hjhPlanList) {
                    // 取得校验的计划编号
                    String planNid = hjhPlan.getPlanNid();
                    // 取得redis的开放额度
                    String accountRedis = RedisUtils.get(RedisConstants.HJH_PLAN + planNid);
                    // 取得db的开放额度
                    BigDecimal accountDB = hjhPlan.getAvailableInvestAccount();
                    // 校验开放额度是否一致
                    if (StringUtils.isEmpty(accountRedis) || accountDB == null || accountDB.compareTo(new BigDecimal(accountRedis)) != 0) {
                        // 不一致
                        RedisUtils.incr(RedisConstants.CONT_WARN_OF_HJH_ACCOUNT + planNid);
                        _log.warn("计划【" + planNid + "】的Redis和DB的开放额度不同(连续次数:" + RedisUtils.get(RedisConstants.CONT_WARN_OF_HJH_ACCOUNT + planNid) + "),"
                                + " Redis:" + accountRedis + ",  DB:" + accountDB + " ！");
                    } else {
                        // 一致
                        // 初期化
                        RedisUtils.set(RedisConstants.CONT_WARN_OF_HJH_ACCOUNT + planNid, "0");
                        _log.info("计划【" + planNid + "】的Redis和db的开放额度一致,"
                                + " Redis:" + accountRedis + ",  DB:" + accountDB + "。");
                    }
                    // 划连续开放额度不同次数 每5次时，非同步问题导致的不一致，开始预警
                    if (Integer.parseInt(RedisUtils.get(RedisConstants.CONT_WARN_OF_HJH_ACCOUNT + planNid)) > 0
                            && Integer.parseInt(RedisUtils.get(RedisConstants.CONT_WARN_OF_HJH_ACCOUNT + planNid)) % 5 == 0) {
                        // 消息内容
                        String content = "计划【" + planNid + "】的Redis和DB的开放额度不同(连续次数:" + RedisUtils.get(RedisConstants.CONT_WARN_OF_HJH_ACCOUNT + planNid) + "),"
                                + " Redis:" + accountRedis + ",  DB:" + accountDB + " ！！！";
                        // 输出日志
                        _log.error(content);
                        // 输出到钉钉群
                        DDChatbotBean chatbotBean = new DDChatbotBean(content);
                        DDChatbot.chatbotSend(DDChatbot.JAVA_PRODUCTION_ERR, JSON.toJSONString(chatbotBean));
                    }
                }
            } catch (Exception e) {
                _log.error("汇计划各计划开放额度校验预警任务 异常！！！ ");
                e.printStackTrace();
            } finally {
                isRun = 0;
            }
            _log.info("汇计划各计划开放额度校验预警任务 结束... ");
        } else {
            _log.info("汇计划各计划开放额度校验预警任务 正在运行... ");
        }
        return true;
    }

}
