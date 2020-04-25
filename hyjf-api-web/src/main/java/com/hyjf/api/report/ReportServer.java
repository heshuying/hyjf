package com.hyjf.api.report;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.report.ReportService;

/**
 *
 * 运营报告接口
 * @author
 * @version hyjf 1.0
 * @since hyjf 1.0 2018年4月10日
 * @see 10:08:00
 */
@RestController
@RequestMapping(value = ReportDefine.REQUEST_MAPPING)
public class ReportServer {
	private Logger logger = LoggerFactory.getLogger(ReportServer.class);
	
	@Autowired
	private ReportService reportService;

	/**
	 * 根据Id获取运营报告详情
	 * @param
	 * @return
	 */
	@RequestMapping(value = ReportDefine.REPORT_INFO)
	@ResponseBody
	public JSONObject getReportInfo(String id){
		logger.info("获取运营报告信息, id is :{}", id);
		Map<String, Object> map = new HashMap<String ,Object>();
		map.put("id", id);
		return reportService.getOperationReportInfo(map);
	
	}
	/**
	 * 获取已发布的运营列表
	 * @param
	 * @return
	 */
	@RequestMapping(value = ReportDefine.REPORT_LIST)
	@ResponseBody
	public JSONObject getReportList(String isRelease,Integer paginatorPage){
		Map<String, Object> map = new HashMap<String ,Object>();
		map.put("isRelease", isRelease);
		map.put("paginatorPage", paginatorPage);
		return reportService.getRecordListByRelease(map);
	}
	
	
}