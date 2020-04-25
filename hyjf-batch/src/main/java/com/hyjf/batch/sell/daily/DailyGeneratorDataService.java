package com.hyjf.batch.sell.daily;

import java.util.Date;

/**
 * @author xiasq
 * @version DailyGeneratorDataService, v0.1 2018/8/2 9:54
 */
public interface DailyGeneratorDataService {
    /**
     * 初始化部门信息
     */
    void initDepartment();

    /**
     * 生成销售日报数据
     * @param date
     */
    void generatorSellDaily(Date date);

    /**
     * 当前日期是否已经生成销售日报
     * @return
     */
    boolean hasGeneratorDataToday();

}
