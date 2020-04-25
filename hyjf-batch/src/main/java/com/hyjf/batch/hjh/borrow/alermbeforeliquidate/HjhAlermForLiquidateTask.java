package com.hyjf.batch.hjh.borrow.alermbeforeliquidate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2018/5/25.
 */
public class HjhAlermForLiquidateTask {

    Logger logger = LoggerFactory.getLogger(HjhAlarmBeforeLiquidateServiceImpl.class);

    @Autowired
    private HjhAlarmBeforeLiquidateService hjhAlarmBeforeLiquidateService;

    public void run() {
        alerm4UndealBorrow();
    }
    private boolean alerm4UndealBorrow() {
        logger.info(">>>>>>>查询计划清算日前是否存在处于出借或复审中的原始标的 start<<<<<<<");
        try {
            hjhAlarmBeforeLiquidateService.selectAlermBeformLiquidation();
        } catch (Exception e) {
            logger.error("=======查询计划清算日前是否存在处于出借或复审中的原始标的发送预警邮件异常!" +e.getMessage());
        }
        logger.info(">>>>>>>查询计划清算日前是否存在处于出借或复审中的原始标的 end<<<<<<<");
        return true;
    }
}
