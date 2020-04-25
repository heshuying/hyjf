package com.hyjf.web.report;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mongo.operationreport.entity.OperationReportColumnEntity;
import com.hyjf.mybatis.model.auto.OperationReport;
import com.hyjf.web.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = ReportDefine.REQUEST_MAPPING)
public class OperationReportController extends BaseController {
	Logger _log = LoggerFactory.getLogger(OperationReportController.class);
	@Autowired
	private OperationReportService operationReportService;

	/**
	 * 获取已发布报告的列表
	 *
	 * @param isRelease or paginatorPage
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = ReportDefine.REPORT_LIST)
	@ResponseBody
	public JSONObject getReportList(HttpServletRequest request,HttpServletResponse response,String isRelease,String paginatorPage) {
		Map<String, Object> map = new HashMap<String ,Object>();
		map.put("isRelease", isRelease);
		map.put("paginatorPage", paginatorPage);
		return operationReportService.getRecordListByRelease(map);
	}

	/**
	 * 获取报表明细
	 *
	 * @param
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = ReportDefine.REPORT_INFO)
	@ResponseBody
	public JSONObject reportInfo(HttpServletRequest request,HttpServletResponse response,String id) {
		Map<String, Object> map = new HashMap<String ,Object>();
		map.put("id", id);
		return operationReportService.getOperationReportInfo(map);
	}

	/**
	 * 进入月度报表预览
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = ReportDefine.INIT_MONTH_REPORT)
	public ModelAndView initMonthReport(HttpServletRequest request, HttpServletResponse response,String id) {
		ModelAndView modelAndView = null;
		if(StringUtils.isNotEmpty(id)){
			OperationReportColumnEntity report = operationReportService.selectByPrimaryKey(id);
			if(report.getOperationReportType().intValue()==1){
				modelAndView = new ModelAndView(ReportDefine.MONTH_LIST_PTAH);
				_log.info("进入月度页面===="+ReportDefine.MONTH_LIST_PTAH);
			}else if(report.getOperationReportType().intValue()==2){
				modelAndView = new ModelAndView(ReportDefine.QUARTER_LIST_PTAH);
				_log.info("进入季度页面===="+ReportDefine.QUARTER_LIST_PTAH);
			}else if(report.getOperationReportType().intValue()==3){
				modelAndView = new ModelAndView(ReportDefine.HALF_LIST_PTAH);
				_log.info("进入半年页面===="+ReportDefine.HALF_LIST_PTAH);
			}else if(report.getOperationReportType().intValue()==4){
				modelAndView = new ModelAndView(ReportDefine.YEAR_LIST_PTAH);
				_log.info("进入年度页面===="+ReportDefine.YEAR_LIST_PTAH);
			}
			modelAndView.addObject("webUrl", PropUtils.getSystem("hyjf.web.host"));
			modelAndView.addObject("id",id);
		}
		return modelAndView;
	}
}
