package com.hyjf.api.wdzj.borrowdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.web.BaseController;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.customize.apiweb.wdzj.BorrowListCustomize;
import com.hyjf.mybatis.model.customize.apiweb.wdzj.PreapysListCustomize;

/**
 * 网贷之家标的数据接口
 * @author hesy
 *
 */
@Controller
@RequestMapping(value = BorrowDataDefine.REQUEST_MAPPING)
public class BorrowDataServer extends BaseController{

    @Autowired
    private BorrowDataService borrowDataService;
    
	Logger _log = LoggerFactory.getLogger(BorrowDataServer.class);
    
    
    /**
     * 标的放款数据接口
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = BorrowDataDefine.GET_BORROWLIST)
    public JSONObject getBorrowDataList(HttpServletRequest request, HttpServletResponse response) {
    	JSONObject result = new JSONObject();
    	String username = PropUtils.getSystem("hyjf.wdzj.username");
    	
    	String token = request.getParameter("token");
    	String date = request.getParameter("date");
    	String page = request.getParameter("page");
    	String pageSize = request.getParameter("pageSize");
    	_log.info("网贷之家 getBorrowDataList接口请求参数：token=" + token + " date=" + date + " page=" + page + " pageSize" + pageSize);
    	
    	// 请求参数校验
    	if(StringUtils.isEmpty(date) || StringUtils.isEmpty(token)){
    		 result = getFailMsg("1", "请求参数非法");
    		 _log.info("网贷之家 getBorrowDataList接口返回：" + result.toJSONString());
             return result;
    	}
    	if(StringUtils.isBlank(page)){
    		page = "1";
    	}
    	if(StringUtils.isBlank(pageSize)){
    		pageSize = "10";
    	}
    	
    	//token校验
    	if(!tokenCheck(token, username)){
    		result = getFailMsg("1", "token校验失败");
   		 	_log.info("网贷之家 getBorrowDataList接口返回：" + result.toJSONString());
            return result;
    	}
    	
    	// 得出开始结束时间
    	Integer timeStart;
		Integer timeEnd;
		try {
			timeStart = GetDate.getDayStart10(date);
			timeEnd = GetDate.getDayEnd10(date);
		} catch (Exception e) {
			e.printStackTrace();
			result = getFailMsg("1", "data格式不正确");
   		 	_log.info("网贷之家 getBorrowDataList接口返回：" + result.toJSONString());
            return result;
		}
    	
    	Map<String, Object> paraMap = new HashMap<String, Object>();
    	paraMap.put("timeStart", timeStart);
    	paraMap.put("timeEnd", timeEnd);
    	
    	String totalAmount = borrowDataService.selectBorrowAmountSum(paraMap);
    	Integer count = borrowDataService.countBorrowList(paraMap);
	    if (count != null && count > 0) {
            Paginator paginator = new Paginator(Integer.parseInt(page), count, Integer.parseInt(pageSize));
            paraMap.put("limitStart", paginator.getOffset());
            paraMap.put("limitEnd", paginator.getLimit());
            List<BorrowListCustomize> recordList  = borrowDataService.selectBorrowList(paraMap);
            
            result.put("status", "0");
    		result.put("statusDesc", "成功");
            result.put("totalPage", String.valueOf(paginator.getTotalPages()));
            result.put("currentPage", page);
            result.put("totalCount", String.valueOf(count));
            result.put("totalAmount", totalAmount);
            result.put("borrowList", JSON.toJSON(recordList));
        }else{
        	result.put("status", "0");
    		result.put("statusDesc", "成功");
        	result.put("totalPage", "0");
            result.put("currentPage", page);
            result.put("totalCount", String.valueOf(count));
            result.put("totalAmount", totalAmount);
            result.put("borrowList", JSON.toJSON(new ArrayList<BorrowListCustomize>()));
        }
    	
    	return result;
    }
    /**
     * 标的提前放款数据接口
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = BorrowDataDefine.GET_PREAPYSLIST)
    public JSONObject getPreapysList(HttpServletRequest request, HttpServletResponse response) {
    	JSONObject result = new JSONObject();
    	String username = PropUtils.getSystem("hyjf.wdzj.username");
    	
    	String token = request.getParameter("token");
    	String date = request.getParameter("date");
    	String page = request.getParameter("page");
    	String pageSize = request.getParameter("pageSize");
    	
    	// 请求参数校验
    	if(StringUtils.isEmpty(date) || StringUtils.isEmpty(token)){
    		 result = getFailMsg("1", "请求参数非法");
    		 _log.info("网贷之家 getPreapysList接口返回：" + result.toJSONString());
             return result;
    	}
    	if(StringUtils.isBlank(page)){
    		page = "1";
    	}
    	if(StringUtils.isBlank(pageSize)){
    		pageSize = "10";
    	}
    	
    	//token校验
    	if(!tokenCheck(token, username)){
    		result = getFailMsg("1", "token校验失败");
   		 	_log.info("网贷之家 getPreapysList接口返回：" + result.toJSONString());
            return result;
    	}
    	
    	// 得出开始结束时间
    	Integer timeStart;
		Integer timeEnd;
		try {
			timeStart = GetDate.getDayStart10(date);
			timeEnd = GetDate.getDayEnd10(date);
		} catch (Exception e) {
			e.printStackTrace();
			result = getFailMsg("1", "data格式不正确");
   		 	_log.info("网贷之家 getPreapysList接口返回：" + result.toJSONString());
            return result;
		}
    	
    	Map<String, Object> paraMap = new HashMap<String, Object>();
    	paraMap.put("timeStart", timeStart);
    	paraMap.put("timeEnd", timeEnd);
    	
    	Integer count = borrowDataService.countPreapysList(paraMap);
	    if (count != null && count > 0) {
            Paginator paginator = new Paginator(Integer.parseInt(page), count, Integer.parseInt(pageSize));
            paraMap.put("limitStart", paginator.getOffset());
            paraMap.put("limitEnd", paginator.getLimit());
            List<PreapysListCustomize> recordList  = borrowDataService.selectPreapysList(paraMap);
            result.put("status", 0);
    		result.put("statusDesc", "成功");
            result.put("totalPage", paginator.getTotalPages());
            result.put("currentPage", page);
            result.put("preapys", JSON.toJSON(recordList));
        }else{
        	result.put("status", 0);
    		result.put("statusDesc", "成功");
        	result.put("totalPage", "0");
            result.put("currentPage", page);
            result.put("preapys", JSON.toJSON(new ArrayList<PreapysListCustomize>()));
        }
    	
    	return result;
    }
    
    /**
     * token校验
     * @param token
     * @param userName
     * @return
     */
    private boolean tokenCheck(String token, String userName){
    	String key = "token_wdzj_" + userName;
    	if(!RedisUtils.exists(key)){
    		return false;
    	}else{
    		String tokenInRedis = RedisUtils.get(key);
    		return token.equals(tokenInRedis) ? true : false;
    	}
    }
    
    private JSONObject getFailMsg(String status, String desc) {
		JSONObject result = new JSONObject();
		
		result.put("status", status);
		result.put("statusDesc", desc);
		result.put("borrowList", new JSONArray());
		return result;
	}
	
    
  }
