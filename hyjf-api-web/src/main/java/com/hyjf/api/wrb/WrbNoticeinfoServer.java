package com.hyjf.api.wrb;

import java.util.ArrayList;
import java.util.List;

import com.hyjf.mybatis.model.auto.MessagePushTemplate;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.WrbParseParamUtil;
import com.hyjf.mybatis.model.auto.MessagePushMsgHistory;
import com.hyjf.wrb.WrbResponse;
import com.hyjf.wrb.noticeinfo.WrbNoticeinfoDefine;
import com.hyjf.wrb.noticeinfo.WrbNoticeinfoSerice;
import com.hyjf.wrb.noticeinfo.request.WrbNoticeinfoRequest;
import com.hyjf.wrb.noticeinfo.response.WrbNoticeinfoResponse;
import com.hyjf.wrb.noticeinfo.response.WrbNoticeinfoResponse.NoticeinfoDetail;



@RestController
@RequestMapping(WrbNoticeinfoDefine.REQUEST_MAPPING)
public class WrbNoticeinfoServer {
	private Logger logger = LoggerFactory.getLogger(WrbInvestServer.class);
	
	@Autowired
	private WrbNoticeinfoSerice wrbNoticeinfoSerice;
	
	@RequestMapping(WrbNoticeinfoDefine.NOTICE_INFO)
	public WrbNoticeinfoResponse getNoticeinfoDetail(@RequestParam String param,
											 @RequestParam(value = "sign", required = false) String sign){
		logger.info("获取平台的公告信息, param is :{}, sign is :{}", param, sign);
		WrbNoticeinfoResponse response=new WrbNoticeinfoResponse();
		
		WrbNoticeinfoRequest request= null;
		try{
			request = WrbParseParamUtil.mapToBean(WrbParseParamUtil.parseParam(param), WrbNoticeinfoRequest.class);
		}catch(Exception e){
			logger.error("参数解析失败....", e);
			response.setRetcode(WrbResponse.FAIL_RETCODE);
			response.setRetmsg(WrbResponse.FAIL_RETMESSAGE);
			return response;
		}finally {
            if (request == null) {
                response.setRetcode(WrbResponse.FAIL_RETCODE);
                response.setRetmsg(WrbResponse.FAIL_RETMESSAGE);
                return response;
            }
        }

		Integer limit = request.getLimit();
		Integer page = request.getPage();
		if(null == limit || null  == page ){
			  response.setRetcode(WrbResponse.FAIL_RETCODE);
              response.setRetmsg(WrbResponse.FAIL_RETMESSAGE);
              return response;
		}

		//查询平台的公告信息-新
		List<MessagePushTemplate> noticeinfoDetailList = wrbNoticeinfoSerice.getNoticeinfoDetailNew(limit, page);
		WrbNoticeinfoResponse wrbNoticeinfoResponse=new WrbNoticeinfoResponse();
		List<NoticeinfoDetail> detailList = new ArrayList<NoticeinfoDetail>();
		for (MessagePushTemplate messagePushTemplate:noticeinfoDetailList){
			NoticeinfoDetail noticeinfoDetail = new NoticeinfoDetail();
			noticeinfoDetail.setId(String.valueOf(messagePushTemplate.getId()));
			noticeinfoDetail.setContent(messagePushTemplate.getTemplateContent());
			noticeinfoDetail.setTitle(messagePushTemplate.getTemplateTitle());
			noticeinfoDetail.setUrl(messagePushTemplate.getTemplateActionUrl());
			if(messagePushTemplate.getLastupdateTime()!=null){
				noticeinfoDetail.setRelease_time(GetDate.timestamptoNUMStrYYYYMMDDHHMMSS(messagePushTemplate.getLastupdateTime()));
			}else{
				noticeinfoDetail.setRelease_time("");
			}
			detailList.add(noticeinfoDetail);
		}
		wrbNoticeinfoResponse.setAll_notices(detailList);
		return wrbNoticeinfoResponse;
		
	}
}
