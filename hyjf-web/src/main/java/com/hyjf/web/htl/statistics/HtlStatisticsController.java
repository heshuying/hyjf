/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
package com.hyjf.web.htl.statistics;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mybatis.model.customize.web.WebHtlStatisticsCustomize;
import com.hyjf.web.BaseController;

@Controller("htlStatisticsController")
@RequestMapping(value = HtlStatisticsDefine.REQUEST_MAPPING)
public class HtlStatisticsController extends BaseController {

	@Autowired
	private HtlStatisticsService htlStatisticsService;

	/**
	 * htl用户出借数据统计
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = HtlStatisticsDefine.TOTAL_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String searchTotalStatistics(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(HtlStatisticsDefine.THIS_CLASS, HtlStatisticsDefine.TOTAL_ACTION);
		JSONObject ret = new JSONObject();
		JSONObject info = new JSONObject();
		WebHtlStatisticsCustomize htl = htlStatisticsService.searchTotalStatistics();
		info.put("htlStatistics", htl);
		ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
		ret.put(CustomConstants.DATA, info);
		ret.put(CustomConstants.MSG, "");
		LogUtil.endLog(HtlStatisticsDefine.THIS_CLASS, HtlStatisticsDefine.TOTAL_ACTION);
		return JSONObject.toJSONString(ret, true);
	}

}
