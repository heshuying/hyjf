package com.hyjf.api.web.jixin.chinapnr;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.web.BaseController;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.PropUtils;
import com.hyjf.jixin.chinapnr.JixinChinapnrBean;
import com.hyjf.jixin.chinapnr.JixinChinapnrDefine;
import com.hyjf.jixin.chinapnr.JixinChinapnrService;

@Controller
@RequestMapping(value = JixinChinapnrDefine.REQUEST_MAPPING)
public class ChinapnrServer extends BaseController {
	Logger _log = LoggerFactory.getLogger(ChinapnrServer.class);
	@Autowired
	private JixinChinapnrService jixinChinapnrService;
	
	@ResponseBody
	@RequestMapping(value = JixinChinapnrDefine.GET_CONTENT_INFO_MAPPING, method = RequestMethod.POST)
	public JSONObject getContentOfChinapnr(HttpServletRequest request, HttpServletResponse response,
			JixinChinapnrBean bean) {
		_log.info("查看即信发送的报文信息接口调用："+JSON.toJSONString(bean));
		JSONObject resultJson = new JSONObject();
		try {
			String result = "";
			
			//验签
			if (!this.checkSign(bean)) {
				_log.info("查看即信发送的报文信息接口调用：验签失败！");
				resultJson.put("status", 1);
				resultJson.put("statusDesc", "验签失败！");
				return resultJson;
			}
			
			//查询报文信息
			result = jixinChinapnrService.getContentOfChinapnr(bean);
			if (result==null||result.equals("")){
				_log.info("查看即信发送的报文信息接口调用：该报文不存在！");
				resultJson.put("status", 1);
				resultJson.put("statusDesc", "该报文不存在！");
				return resultJson;
			}
			resultJson.put("result", result);
			resultJson.put("status", 0);
			resultJson.put("statusDesc", "报文取得成功！");
			_log.info("查看即信发送的报文信息接口调用：报文取得成功！");
			return resultJson;
		} catch (Exception e) {
			_log.info("查看即信发送的报文信息接口调用：报文取得失败！");
			resultJson.put("status", 2);
			resultJson.put("statusDesc", "报文取得失败！");
			return resultJson;
		}
	}
	
	/**
	 * 验证签名
	 * 
	 * @param paramBean
	 * @return
	 */
	private boolean checkSign(JixinChinapnrBean paramBean) {

		Integer txDate = paramBean.getTxDate();
		Integer txTime = paramBean.getTxTime();
		String seqNo = paramBean.getSeqNo();
		String timestamp = paramBean.getTimestamp();
		
		String accessKey = PropUtils
				.getSystem(JixinChinapnrDefine.RELEASE_JIXIN_MSG_ACCESSKEY);
		
		String sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + txDate + txTime + seqNo + timestamp + accessKey));
		
		return StringUtils.equals(sign, paramBean.getSign()) ? true : false;
	}
}