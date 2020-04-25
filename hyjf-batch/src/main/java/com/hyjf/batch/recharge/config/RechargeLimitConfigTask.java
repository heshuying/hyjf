/*
 * Copyright(c) 2012-2016 JD Pharma.Ltd. All Rights Reserved.
 */
package com.hyjf.batch.recharge.config;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.log.LogUtil;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;

/**
 * 快捷卡充值限额查询接口
 * @author 李深强
 */
public class RechargeLimitConfigTask {
   
	
   @Autowired
   private RechargeConfigService rechargeConfigService;
   
    /**
     * 更新快捷卡充值限额
     * @author 李深强
     */
    public void updateRechargeLimitConfig() {
        LogUtil.startLog(RechargeLimitConfigTask.class.toString(), "updateRechargeLimitConfig");
        System.out.println("--------------更新快捷卡充值限额-------------------");
		//查询汇付信息
		ChinapnrBean retBean = new ChinapnrBean();
		try {
			ChinapnrBean querybean = new ChinapnrBean();
			querybean.setVersion(ChinaPnrConstant.VERSION_10);// 接口版本号
			querybean.setCmdId(ChinaPnrConstant.CMDID_QUERY_PAY_QUOTA); // 消息类型
//			querybean.setOpenBankId("");//银行卡
			querybean.setGateBusiId(ChinaPnrConstant.PARAM_QP);
			querybean.setBgRetUrl(ChinapnrUtil.getBgRetUrl());
			//调用接口
		    retBean =  ChinapnrUtil.callApiBg(querybean);
		    //调用成功
		    if(ChinaPnrConstant.RESPCODE_SUCCESS.equals(retBean.getRespCode())){
		    	String payQutaList = retBean.getPayQuotaDetails();
		    	JSONArray array = JSONObject.parseArray(payQutaList);
		    	if(array != null && array.size() > 0){
		    		//更新数据
		    		for(int i = 0;i < array.size();i++){
			    		JSONObject obj = array.getJSONObject(i);
						this.rechargeConfigService.updateBankRechargeConfig(obj.getString("OpenBankId"), obj.getString("SingleTransQuota"), obj.getString("CardDailyTransQuota"));
		    		}
		    	}
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
        LogUtil.endLog(RechargeLimitConfigTask.class.toString(), "updateRechargeLimitConfig");
    }
 
 
}
