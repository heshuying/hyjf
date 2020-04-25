package com.hyjf.admin.manager.content.operationreport;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.mybatis.model.auto.OperationReport;
import com.hyjf.mybatis.model.customize.report.OperationReportCustomize;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
/**
 * @author xiehuili
 * @version 2.0
 */
public interface OperationReportService  {
    /**
     * 获取全部列表
     *
     * @return
     */
    public List<OperationReportCustomize> getRecordList(Map<String, Object> record);

    /**
     * 统计全部个数
     *
     * @return
     */
    public Integer countRecord(Map<String, Object> record);

    /**
     * 按月度统计列表
     *
     * @return
     */
    List<OperationReportCustomize> getRecordListByMonth(Map<String, Object> record);
    /**
     * 按月度统计个数
     *
     * @return
     */
    Integer countRecordByMonth(Map<String, Object> record);

    /**
     * 按季度统计列表
     *
     * @return
     */
    List<OperationReportCustomize> getRecordListByQuarter(Map<String, Object> record);

    /**
     * 按季度统计个数
     *
     * @return
     */
    Integer countRecordByQuarter(Map<String, Object> record);
    /**
     * 按半年统计列表
     *
     * @return
     */
    List<OperationReportCustomize> getRecordListByHalfYear(Map<String, Object> record);
    /**
     * 按半年统计个数
     *
     * @return
     */
    Integer countRecordByHalfYear(Map<String, Object> record);
    /**
     * 按年统计列表
     *
     * @return
     */
    List<OperationReportCustomize> getRecordListByYear(Map<String, Object> record);
    /**
     * 按年统计个数
     *
     * @return
     */
    Integer countRecordByYear(Map<String, Object> record);


    int updateByPrimaryKeySelective(OperationReport record);
    /**
     * 添加或修改
     *
     * @return
     */
//	public Integer insertOperationReportCommon(OperationreportCommonBean form);

	/**根据id查询运营报告
	 * @param id
	 * @return
	 */
	public OperationreportCommonBean selectOperationreportCommon(String id);

	/**图片上传
	 * @param request
	 * @param response
	 * @return
	 */
	public String uploadFile(HttpServletRequest request, HttpServletResponse response);


	/**季度运营报告插入
	 * @param form
	 * @return
	 */
	public int insertQuarterOperationReport(OperationreportCommonBean form);

	/**季度运营报告插入预览
	 * @param form
	 * @return
	 */
	public JSONObject insertQuarterOperationReportPreview(OperationreportCommonBean form,Integer type);


	/**季度运营报告修改
	 * @param form
	 * @return
	 */
	public int updateQuarterOperationReport(OperationreportCommonBean form);
	/**季度运营报告修改预览
	 * @param form
	 * @return
	 */
	public JSONObject updateQuarterOperationReportPreview(OperationreportCommonBean form);

	/**月度运营报告插入
	 * @param form
	 * @return
	 */
	public int insertMonthlyOperationReport(OperationreportCommonBean form);
	/**月度运营报告插入预览功能
	 * @param form
	 * @return
	 */
	public JSONObject insertMonthlyOperationReportPreview(OperationreportCommonBean form) ;
	/**月度运营报告修改
	 * @param form
	 * @return
	 */
	public int updateMonthOperationReport(OperationreportCommonBean form);

	/**月度运营报告修改预览
	 * @param form
	 * @return
	 */
	public JSONObject updateMonthOperationReportPreview(OperationreportCommonBean form);

	/**上半年度运营报告插入
	 * @param form
	 * @return
	 */
	public int insertHalfYearOperationReport(OperationreportCommonBean form);

    /**上半年度运营报告预览功能
     * @param form
     * @return
     */
    public JSONObject insertHalfYearOperationReportPreview(OperationreportCommonBean form) ;
	/**上半年度运营报告修改
	 * @param form
	 * @return
	 */
	public int updateHalfYearOperationReport(OperationreportCommonBean form);
    /**上半年度运营报告修改预览
     * @param form
     * @return
     */
    public JSONObject updateHalfYearOperationReportPreview(OperationreportCommonBean form);
	/**年度运营报告插入
	 * @param form
	 * @return
	 */
	public int insertYearOperationReport(OperationreportCommonBean form);

	/**年度运营报告修改
	 * @param form
	 * @return
	 */
	public int updateYearOperationReport(OperationreportCommonBean form);


	/**年度运营报告新增预览
	 * @param form
	 * @return
	 */
	public JSONObject insertYearlyOperationReportPreview(OperationreportCommonBean form);

	/**年度运营报告修改预览
	 * @param form
	 * @return
	 */
	public JSONObject updateYearOperationReportPreview(OperationreportCommonBean form);

}
