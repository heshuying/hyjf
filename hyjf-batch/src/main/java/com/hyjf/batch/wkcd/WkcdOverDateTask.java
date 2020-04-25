package com.hyjf.batch.wkcd;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.http.HttpRequest;
import com.hyjf.common.security.utils.MD5Utils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.mapper.customize.WkcdBorrowCustomizeMapper;
import com.hyjf.mybatis.model.customize.WkcdOverDateCustomize;

public class WkcdOverDateTask {
	@Autowired
	private WkcdBorrowCustomizeMapper wkcdBorrowCustomizeMapper;
	/** 运行状态 */
	private static int isRun = 0;

	public void run() {
		sendOverDate();
	}
	
	public void sendOverDate(){
		if(isRun != 0){
			return;
		}
		isRun = 1;
		List<WkcdOverDateCustomize> overDates = wkcdBorrowCustomizeMapper.selectOverDate();
		if(overDates == null || overDates.size() == 0){
			isRun = 0;
			return;
		}
		System.out.println("------发送微可逾期还款信息");
		String url = PropUtils.getSystem("wkcd.host") + "/thirdparty/overdueNotification";
        Map<String, Object> param = new HashMap<>();
        param.put("_token", RedisUtils.get("Third-Party-WKCD-Token"));
        param.put("_partnerId", "hyjf");
        param.put("overdueList", JSONObject.toJSON(overDates));
        //参数加密
        String miwen = "";
        String requestBody = JSON.toJSONString(param);
		try {
			miwen = MD5Utils.MD5(requestBody+PropUtils.getSystem("wkcd.aes.key"));
		}  catch (Exception e) {
			e.printStackTrace();
		}
        Map<String, Object> params = new HashMap<>();
        params.put("_request_body", requestBody);
        params.put("_digest", miwen);
        HttpRequest.sendPost(url, params);
        isRun = 0;
	}
}
