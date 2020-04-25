package com.hyjf.server.module.wkcd.borrow;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.http.HttpDeal;
import com.hyjf.common.security.utils.AESUtil;
import com.hyjf.common.util.DownloadPictureUtil;
import com.hyjf.common.util.PropUtils;
import com.hyjf.server.BaseController;

@Controller
@RequestMapping(value = WkcdBorrowDefine.REQUEST_MAPPING)
public class WkcdBorrowController extends BaseController {
	@Autowired
	private WkcdBorrowService wkcdBorrowService;
    
	@RequestMapping(value="getWkcdBorrowAction")
	@ResponseBody
	public JSONObject generateBorrow(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject ret = new JSONObject();
		//String secretKeyMingwen = request.getAttribute("secretKey").toString();
		String requestObjectMingwen = request.getAttribute("requestObject").toString();
		if(StringUtils.isEmpty(requestObjectMingwen)){
			ret.put("status", "1");
	        ret.put("statusDesc", "接口调用失败,参数为空");
	        return ret;
		} 
        Map<String, String> map = parseRequestJson(requestObjectMingwen);
        try {
        	map = parseRequestJson(requestObjectMingwen);
        	if(wkcdBorrowService.checkWkcdId(map.get("wkcd_id"))){
        		ret.put("status", "1");
    	        ret.put("statusDesc", "该资产包已经存在!");
    	        return ret;
        	}
			wkcdBorrowService.insertRecord(Integer.valueOf(map.get("userId")), map.get("wkcd_id"), map.get("user_name"), map.get("truename"), map.get("mobile"),map.get("wkcd_rate"), map.get("borrow_amount"), map.get("car_no"), map.get("car_type"), map.get("car_shop"), map.get("wkcd_status"),map.get("wkcd_repay_type"),Integer.valueOf(map.get("wkcd_borrow_period")));
		} catch (NumberFormatException e) {
			ret.put("status", "1");
	        ret.put("statusDesc", "接口调用失败,参数转换错误");
	        return ret;
		} catch (Exception e) {
			ret.put("status", "1");
	        ret.put("statusDesc", "接口调用失败,"+e.getMessage());
	        return ret;
		}
        ret.put("status", "0");
        ret.put("statusDesc", "接口请求成功");
        try {
        	//下载车贷图片至服务器
            downLoadPic(map.get("wkcd_id"));  
		} catch (Exception e) {
			System.out.println("---资产包图片下载有问题---");
		}
        return ret;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void downLoadPic(String wkcd_id) throws Exception{
		//下载车贷图片至服务器
        String token = RedisUtils.get("Third-Party-WKCD-Token");
        String url = PropUtils.getSystem("wkcd.host")+"/thirdparty/getEnclosureUrl/borrowInfo?_partnerId=hyjf"+"&_token="+token+"&_bid="+wkcd_id;
        String json_result = HttpDeal.get(url);
        Map<String, Object> map_pic = new HashMap<>();
        Map<String, Object> images = new HashMap<>();
        try {
        	 map_pic = JSON.parseObject(json_result, Map.class);
        	 images = JSON.parseObject(AESUtil.decryptAES(map_pic.get("response").toString(),PropUtils.getSystem("wkcd.aes.key")), Map.class);
		} catch (Exception e) {
			e.printStackTrace();
		}   
		Iterator iter = images.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			Object val = entry.getValue();
			if (val != null) {
				if (val instanceof String && StringUtils.isNotBlank(val.toString())) {
					DownloadPictureUtil.download(val.toString(), DownloadPictureUtil.convert(key.toString()), PropUtils.getSystem("file.download.wkcd.real.path")+File.separator + wkcd_id + File.separator);
				} else if (val instanceof List) {
					List list = (List) val;
					for (int i = 0; i < list.size(); i++) {
						if (StringUtils.isNotBlank(list.get(i).toString())) {
							DownloadPictureUtil.download(list.get(i).toString(), DownloadPictureUtil.convert(key.toString()) + (i + 1), PropUtils.getSystem("file.download.wkcd.real.path")+File.separator + wkcd_id + File.separator);
						}
					}
				}
				else if(key.toString().equals("carPhoto")){
					 Map<String, Object> carMap = (Map)val;
					 Iterator iterCar = carMap.entrySet().iterator();
					 while(iterCar.hasNext()){
						 Map.Entry entryCar = (Map.Entry) iterCar.next();
						 if(entryCar.getValue()!=null){
							 DownloadPictureUtil.download(entryCar.getValue().toString(), DownloadPictureUtil.convert(entryCar.getKey().toString()), PropUtils.getSystem("file.download.wkcd.real.path")+File.separator + wkcd_id + File.separator);
						 }
					 }
				}
			}
		}   
	}
	
	public static void main(String[] args) throws Exception {
	}
}
