package com.hyjf.batch.holidayconfig;

/**
 * @author xiasq
 * @version HolidayConfigNewService, v0.1 2018/7/26 17:57
 */
public interface HolidayConfigNewService {

    /**
     * 更新节假日配置表
     *
     * @param year
     * @return
     * @throws ReturnMessageException
     */
    boolean updateHolidaysConfig(int year);

    /**
     * 初始化年度配置
     *
     * @param year
     */
    void initCurrentYearConfig(int year);

    /**
     * 查询某年日历是否存在
     * @param currentYear
     * @return
     */
    boolean hasHolidayConfig(int currentYear);
}
