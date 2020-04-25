package com.hyjf.app.report;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = ReportDefine.REQUEST_MAPPING)
public class OperationReportController extends BaseController {

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
}
