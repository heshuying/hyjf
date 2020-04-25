package com.hyjf.web.stock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.log.LogUtil;
import com.hyjf.mybatis.model.auto.StockInfo;
import com.hyjf.mybatis.model.customize.web.StockInfo2Customize;
import com.hyjf.web.BaseController;

@Controller("stockInfoController")
@RequestMapping(value = StockInfoDefine.REQUEST_MAPPING)
public class StockInfoController extends BaseController {

	@Autowired
	private StockInfoService stockInfoService;

	/**
	 * 查询股票信息
	 * @param request
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(StockInfoDefine.QUERY_STOCKINFO_ACTION)
	public  String queryStockinfoAction(HttpServletRequest request,StockInfoBean form){
		LogUtil.startLog(StockInfoController.class.toString(),StockInfoDefine.QUERY_STOCKINFO_ACTION);
//		ModelAndView modeAndView = new ModelAndView(StockInfoDefine.QUERY_STOCKINFO_PATH);
		JSONObject ret = new JSONObject();
		String type= form.getType();
		if(form==null || form.getType()==null || "".equals(form.getType())){
			ret.put("statusDesc", "参数为空。");
			return ret.toString();
		}
		//查询规定时间段股票信息
		List<StockInfo2Customize> result = this.stockInfoService.queryStockInfoList2(type);
		List<Object> resultList= new ArrayList<Object>();
		for(StockInfo2Customize r: result){
			Map<Object,Object> info= new HashMap<Object,Object>();
			long milltime= r.getTime();
			info.put("name", milltime*1000);
			info.put("value", new String[]{r.getDate(),r.getNowPrice().toString()});
			resultList.add(info);
		}
		ret.put("StockInfoList", resultList);
		//查询当前股票信息
		StockInfo stockinfo = this.stockInfoService.queryStockInfo();
		ret.put("stockinfo", stockinfo);
		ret.put("StockInfoForm", form);
		return ret.toString();
	}
}
