package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.auto.HolidaysConfigNew;
import org.apache.ibatis.annotations.Param;


/**
 * @author xiasq
 * @version HolidaysConfigNewCustomizeMapper, v0.1 2018/7/16 17:52
 */
public interface HolidaysConfigNewCustomizeMapper {

    /**
     * 批量插入
     * @param list
     * @throws Exception
     */
    void batchInsert(List<HolidaysConfigNew> list);

    /**
     * 批量更新状态
     * @param list
     * @throws Exception
     */
    void batchUpdate(List<HolidaysConfigNew> updateList);

    /**
     * 查询指定月份配置
     * @param year
     * @param month
     * @return
     */
    List<HolidaysConfigNew> selectByYearMonth(@Param("year") int year, @Param("month") int month);

    /**
     * 查询某年某月第一个工作日的日
     * @param year
     * @param month
     * @return
     */
    int selectFirstWorkdayOnMonth(@Param("year") int year, @Param("month") int month);

    /**
     * 删除年度配置
     * @param year
     */
    void deleteByYear(int year);
}
