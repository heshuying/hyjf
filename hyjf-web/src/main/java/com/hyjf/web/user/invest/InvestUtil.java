/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: b
 * @version: 1.0
 * Created at: 2015年12月10日 上午10:00:43
 * Modification History:
 * Modified by :
 */

package com.hyjf.web.user.invest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;

public class InvestUtil {

	// 调用出借接口
	public static ModelAndView callTender(String account, String orderId, String tenderUsrcustid,
			String borrowerUsrcustid, String userId, String borrowNid, String retUrl) {
		ModelAndView modelAndView = new ModelAndView();
		Properties properties = PropUtils.getSystemResourcesProperties(); // 商户后台应答地址(必须)
		String bgRetUrl = properties.getProperty("hyjf.chinapnr.callbackurl").trim();
		ChinapnrBean chinapnrBean = new ChinapnrBean();
		chinapnrBean.setVersion("10");// 接口版本号
		chinapnrBean.setCmdId("InitiativeTender"); // 消息类型(主动投标)
		chinapnrBean.setUsrCustId(tenderUsrcustid);// 用户客户号
		chinapnrBean.setOrdId(orderId); // 订单号(必须)
		chinapnrBean.setOrdDate(GetDate.getServerDateTime(1, new Date()));// 订单时间(必须)格式为yyyyMMdd，例如：20130307
		chinapnrBean.setTransAmt(CustomUtil.formatAmount(account));// 交易金额(必须)
		chinapnrBean.setMaxTenderRate("0.10");// 手续费率最高0.1
		chinapnrBean.setBorrowerCustId(borrowerUsrcustid);
		chinapnrBean.setBorrowerAmt(CustomUtil.formatAmount(account));
		chinapnrBean.setBorrowerRate("0.30");
		chinapnrBean.setMerPriv(borrowNid);
		chinapnrBean.setLogUserId(Integer.valueOf(userId));
		// chinapnrBean.setProId(borrowId);
		Map<String, String> map = new HashMap<String, String>();
		map.put("BorrowerCustId", borrowerUsrcustid);
		map.put("BorrowerAmt", CustomUtil.formatAmount(account));
		map.put("BorrowerRate", "0.30");// 云龙提示
		// map.put("ProId", borrow.getId()+"");// 云龙提示
		//map.put("ProId", borrowNid);// 项目id
		JSONArray array = new JSONArray();
		array.add(map);
		String BorrowerDetails = JSON.toJSONString(array);
		chinapnrBean.setBorrowerDetails(BorrowerDetails);
		chinapnrBean.setRetUrl(retUrl); // 页面返回
		chinapnrBean.setBgRetUrl(bgRetUrl); // 商户后台应答地址(必须)
		chinapnrBean.setType("user_tender"); // 日志类型
		// chinapnrBean.setIsFreeze("Y");
		// 跳转到汇付天下画面

		try {
			modelAndView = ChinapnrUtil.callApi(chinapnrBean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return modelAndView;
	}
}
