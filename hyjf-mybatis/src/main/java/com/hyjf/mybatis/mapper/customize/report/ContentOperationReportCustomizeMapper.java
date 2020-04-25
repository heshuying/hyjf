package com.hyjf.mybatis.mapper.customize.report;

import com.hyjf.mybatis.model.auto.OperationReport;
import com.hyjf.mybatis.model.auto.OperationReportExample;
import com.hyjf.mybatis.model.customize.report.OperationReportCustomize;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ContentOperationReportCustomizeMapper {

    /**
     * 获取全部运营报表列表
     *
     * @return
     */
    List<OperationReportCustomize> getRecordList(Map<String, Object> record);

    /**
     * 获取（自定义）全部运营报表列表
     *
     * @return
     */
    List<OperationReportCustomize> getCustomizeRecordList(Map<String, Object> record);

    /**
     * 统计全部运营报表个数
     *
     * @return
     */
    Integer countRecord(Map<String, Object> record);

    /**
     * 按月度统计运营报表列表
     *
     * @return
     */
    List<OperationReportCustomize> getRecordListByMonth(Map<String, Object> record);

    /**
     * 按月度统计运营报表个数
     *
     * @return
     */
    Integer countRecordByMonth(Map<String, Object> record);
    /**
     * 按季度统计运营报表列表
     *
     * @return
     */
    List<OperationReportCustomize> getRecordListByQuarter(Map<String, Object> record);

    /**
     * 按季度统计运营报表个数
     *
     * @return
     */
    Integer countRecordByQuarter(Map<String, Object> record);

    /**
     * 按半年统计运营报表列表
     *
     * @return
     */
    List<OperationReportCustomize> getRecordListByHalfYear(Map<String, Object> record);
    /**
     * 按半年统计运营报表个数
     *
     * @return
     */
    Integer countRecordByHalfYear(Map<String, Object> record);
    /**
     * 按年统计运营报表列表
     *
     * @return
     */
    List<OperationReportCustomize> getRecordListByYear(Map<String, Object> record);
    /**
     * 按年统计运营报表个数
     *
     * @return
     */
    Integer countRecordByYear(Map<String, Object> record);
    /**
     * 通过主键查询运营报表
     *
     * @return
     */
    OperationReportCustomize selectByPrimaryKey(Integer id);
}