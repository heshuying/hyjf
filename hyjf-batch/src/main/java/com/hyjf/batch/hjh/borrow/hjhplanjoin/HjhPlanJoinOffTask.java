/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.batch.hjh.borrow.hjhplanjoin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 定时关闭计划任务
 * @author liubin
 * @version HjhPlanJoinOffTask, v0.1 2018/7/30 22:21
 */
public class HjhPlanJoinOffTask {
    Logger _log = LoggerFactory.getLogger(HjhPlanJoinOffTask.class);

    /** 运行状态 */
    private static int isRun = 0;

    @Autowired
    HjhPlanJoinService hjhPlanJoinService;

    /**
     * 调用任务实际方法接口
     */
    public void run() {
        process();
    }

    /**
     * 关闭计划
     *
     * @return
     */
    private boolean process() {
        if (isRun == 0) {
            _log.info("定时关闭计划任务 开始... ");

            isRun = 1;
            try {
                // 关闭计划
                int result = this.hjhPlanJoinService.updateHjhPlanForJoin(2);
                if (result == 0) {
                    _log.info("没有关闭的计划。");
                }else{
                    _log.info("关闭了 " + result + " 个计划。");
                }
            } catch (Exception e) {
                _log.error("定时关闭计划任务 异常... ");
                e.printStackTrace();
            } finally {
                isRun = 0;
            }
            _log.info("定时关闭计划任务 结束... ");
        }else{
            _log.info("定时关闭计划任务 正在运行... ");
        }
        return true;
    }
}
