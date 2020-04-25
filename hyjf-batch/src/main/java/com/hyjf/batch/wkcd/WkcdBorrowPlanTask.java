package com.hyjf.batch.wkcd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.http.HttpRequest;
import com.hyjf.common.security.utils.MD5Utils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.mapper.auto.WkcdBorrowMapper;
import com.hyjf.mybatis.mapper.customize.WkcdBorrowCustomizeMapper;
import com.hyjf.mybatis.model.auto.WkcdBorrow;
import com.hyjf.mybatis.model.auto.WkcdBorrowExample;
import com.hyjf.mybatis.model.customize.WkcdBorrowCustomize;
import com.hyjf.mybatis.model.customize.WkcdBorrowDTO;

public class WkcdBorrowPlanTask {
	@Autowired
	private WkcdBorrowCustomizeMapper wkcdBorrowCustomizeMapper;
	@Autowired
	private WkcdBorrowMapper wkcdBorrowMapper;
	/** 运行状态 */
	private static int isRun = 0;

	public void run() {
		sendBorrowPlan();
	}
	
	public  void sendBorrowPlan() {
		if (isRun == 0) {
			isRun = 1;
			List<WkcdBorrowCustomize> result = wkcdBorrowCustomizeMapper.selectToSend();
			Set<String> bids = new HashSet<>();
			List<WkcdBorrowDTO> dtos = new ArrayList<>();
			if(result == null || result.size() == 0){
				isRun = 0;
				return;
			}
			System.out.println("---发送微可车贷还款计划");
            for (WkcdBorrowCustomize item : result) {
				bids.add(item.getBid());
			}
            for (String string : bids) {
            	WkcdBorrowDTO dto = new WkcdBorrowDTO();
            	dto.setBid(string);
            	for (WkcdBorrowCustomize item : result) {
					if(item.getBid().equals(string)){
						dto.setWkcd_id(item.getWkcd_id());
						dto.setRecoverLastTime(item.getRecoverLastTime());
						dto.getPlans().add(item);
					}
				}
            	dtos.add(dto);
			}
	        String url = PropUtils.getSystem("wkcd.host") + "/thirdparty/confirmLoan";
	        Map<String, Object> param = new HashMap<>();
	        param.put("_token", RedisUtils.get("Third-Party-WKCD-Token"));
	        param.put("_partnerId", "hyjf");
	        param.put("borrowList", JSONObject.toJSON(dtos));
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
	        String json_result = HttpRequest.sendPost(url, params);
	        if(StringUtils.isNotEmpty(json_result)){
	        	JSONObject json = JSONObject.parseObject(json_result);
	        	if(json.getString("code").equals("10000")){   //微可成功保存
	        		for (String string : bids) {
	    	        	WkcdBorrowExample example = new WkcdBorrowExample();
	    	        	WkcdBorrowExample.Criteria cra = example.createCriteria();
	    	        	cra.andBorrowNidEqualTo(string);
	    	        	List<WkcdBorrow> wkcdBorrows = wkcdBorrowMapper.selectByExample(example);
	    	        	if(!wkcdBorrows.isEmpty()){
	    	        		WkcdBorrow record = wkcdBorrows.get(0);
	    	        		record.setSync(1);
	    	        		wkcdBorrowMapper.updateByPrimaryKey(record);
	    	        	}
	    	        }
	        	}
	        }
		}
		isRun = 0;
	}
}
