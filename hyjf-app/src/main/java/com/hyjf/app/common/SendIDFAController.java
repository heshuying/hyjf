/**
 * 发送客户端IDFA
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: b
 * @version: 1.0
 * Created at: 2016年06月21日 下午2:32:36
 * Modification History:
 * Modified by :
 */
package com.hyjf.app.common;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.common.http.HttpDeal;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.I4;


/**
 * 发送客户端IDFA接口(APP)
 * @author Michael
 */
@Controller
@RequestMapping(value = SendIDFADefine.REQUEST_MAPPING)
public class SendIDFAController extends BaseController {
    /** THIS_CLASS */
    private static final String THIS_CLASS = SendIDFAController.class.getName();
    private static String WEB_URL = "/hyjf-app";
	@Autowired
	private SendIDFAService sendIDFAService;
//
//    /**
//     * 发送客户端IDFA（广告标识码）
//     * @param request
//     * @param form
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping(value = SendIDFADefine.SEND_IDFA_MAPPING, method = RequestMethod.POST)
//    public JSONObject sendIDFA(HttpServletRequest request, HttpServletResponse response) {
//        LogUtil.startLog(THIS_CLASS, SendIDFADefine.SEND_IDFA_MAPPING);
//		JSONObject map = new JSONObject();
//		map.put("request", WEB_URL + SendIDFADefine.REQUEST_MAPPING + SendIDFADefine.SEND_IDFA_MAPPING);
//        // 唯一标识
//        String sign = request.getParameter("sign");
//        // 平台
//		String clientStr = request.getParameter("platform");
//		//版本号
//		String versionStr = request.getParameter("version");
//        // 取得加密用的Key
//        String key = SecretUtil.getKey(sign);
//        if (Validator.isNull(key)) {
//        	map.put("status", "1");
//        	map.put("statusDesc", "请求参数非法");
//            return map;
//        }
//        map.put("status", "0");
//        map.put("statusDesc", "请求成功");
//        int type = 0;
//        if(StringUtils.isNotEmpty(clientStr)){
//        	if(clientStr.equals(CustomConstants.CLIENT_ANDROID)){
//        		type = 1;
//        	}else if(clientStr.equals(CustomConstants.CLIENT_IOS)){
//        		type = 2;
//        	}
//        }
//        Version version = sendIDFAService.getNewVersionByType(type);
//        //获取IDFA
//        String idfa = request.getParameter("IDFA");
//        if(StringUtils.isNotEmpty(idfa)){
//            //检查该idfa是否存在
//            List<Idfa> idfaList = sendIDFAService.selectIDFA(idfa);
//            if(idfaList!=null && idfaList.size()>0){
//                map.put("status","0");
//                map.put("statusDesc","IDFA已经存在");
//            }else{//判断不存在后
//                Idfa idfaEntity = new Idfa();
//                idfaEntity.setIdfa(idfa);
//                idfaEntity.setPlatform(clientStr);
//                idfaEntity.setVersion(version!=null?version.getVersion():versionStr);
//                idfaEntity.setCreateTime(GetDate.getNowTime10());
//                Integer result = sendIDFAService.insertIDFA(idfaEntity);
//                //删除一年以上的数据
//                sendIDFAService.deleteIDFAByOneYear(GetDate.getNowTime10()-3600*24*365);
//                if(result.intValue()==1){
//                    map.put("status","0");
//                    map.put("statusDesc","发送客户端IDFA保存成功");
//                }else{
//                    map.put("status","1");
//                    map.put("statusDesc","发送客户端IDFA异常-IDFA未保存");
//                }
//            } 
//        }else{
//            map.put("status","1");
//            map.put("statusDesc","发送客户端IDFA异常-IDFA为空");
//        }
//        LogUtil.endLog(THIS_CLASS, SendIDFADefine.SEND_IDFA_MAPPING);
//        return map;
//    }
	

    /**
     * 发送客户端IDFA（广告标识码）
     * @param request
     * @param form
     * @return
     */
    @ResponseBody
    @RequestMapping(value = SendIDFADefine.SEND_IDFA_MAPPING, method = RequestMethod.POST)
    public JSONObject sendIDFA(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(THIS_CLASS, SendIDFADefine.SEND_IDFA_MAPPING);
		JSONObject map = new JSONObject();
		map.put("request", WEB_URL + SendIDFADefine.REQUEST_MAPPING + SendIDFADefine.SEND_IDFA_MAPPING);
        // 唯一标识
        String sign = request.getParameter("sign");
        // 平台
//		String clientStr = request.getParameter("platform");
		//版本号
//		String versionStr = request.getParameter("version");
        // 取得加密用的Key
        String key = SecretUtil.getKey(sign);
        if (Validator.isNull(key)) {
        	map.put("status", "1");
        	map.put("statusDesc", "请求参数非法");
            return map;
        }
        map.put("status", "0");
        map.put("statusDesc", "请求成功");
        //获取IDFA
        String idfa = request.getParameter("IDFA");
        System.out.println("*********idfa:"+idfa);
        if(StringUtils.isNotEmpty(idfa)){
            //检查该idfa是否存在
            List<I4> i4List = sendIDFAService.selectI4List(idfa);
            //如果已经存在，调用爱思助手激活回调接口
            if(i4List!=null && i4List.size()>0){
            	I4 i4= i4List.get(0);
            	String url= PropUtils.getSystem("i4.url")+"&mac="+i4.getMac()+"&idfa="+i4.getIdfa()+"&os="+i4.getOs()+"&rt=1";
            	System.out.println("url:"+url);
            	//请求 get接口
            	String content= HttpDeal.get(url);
            	
            	if(StringUtils.isEmpty(content)){
                    map.put("status","1");
                    map.put("statusDesc","调用爱思接口没有返回结果！");
            	}else{
            		JSONObject contentJson = JSON.parseObject(content);
            		System.out.println("=====内容：\n"+contentJson.get("message"));
            		if(StringUtils.isNotEmpty(String.valueOf(contentJson.get("success")))){
            			if("true".equals(contentJson.get("success"))){
                            map.put("status","0");
                            map.put("statusDesc","调用爱思助手激活回调接口，返回值成功！");
//                            System.out.println("调用爱思助手激活回调接口，返回值成功！");
            			}else{
            				 map.put("status","1");
                             map.put("statusDesc","调用爱思助手激活回调接口，返回值失败！");
//                             System.out.println("调用爱思助手激活回调接口，返回值失败！");
            			}
            		}
            	}
            }else{//如果不存在， 不调用爱思助手激活接口
                map.put("status","1");
                map.put("statusDesc","客户端IDFA不存在！");
            } 
        }else{
            map.put("status","1");
            map.put("statusDesc","客户端IDFA为空");
        }
        LogUtil.endLog(THIS_CLASS, SendIDFADefine.SEND_IDFA_MAPPING);
        return map;
    }
    
    
    
}


