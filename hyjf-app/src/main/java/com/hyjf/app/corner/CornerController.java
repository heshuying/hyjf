/**
 * Description:出借控制器
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: b
 * @version: 1.0
 * Created at: 2015年12月4日 下午2:32:36
 * Modification History:
 * Modified by :
 */
package com.hyjf.app.corner;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.MobileCode;
import com.hyjf.mybatis.model.auto.Version;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 汇天利资金中心接口(APP)
 * @author Michael
 */
@Controller
@RequestMapping(value = AppCommonDefine.REQUEST_MAPPING)
public class CornerController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(CornerController.class);
    /** THIS_CLASS */
    private static final String THIS_CLASS = CornerController.class.getName();
    private static String WEB_URL = "/hyjf-app";
	@Autowired
	private AppCommonService appCommonService;

    /**
     * 获取版本号
     * @param request
     * @param form
     * @return
     */
    @ResponseBody
    @RequestMapping(value = AppCommonDefine.VERSION_MAPPING)
    public JSONObject getVersion(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(THIS_CLASS, AppCommonDefine.VERSION_MAPPING);

		JSONObject map = new JSONObject();
		map.put("request", WEB_URL + AppCommonDefine.REQUEST_MAPPING + AppCommonDefine.VERSION_MAPPING);
        // 唯一标识
        String sign = request.getParameter("sign");
        // 平台
		String clientStr = request.getParameter("platform");
		//版本号
		String versionStr = request.getParameter("version");
        // 取得加密用的Key
        String key = SecretUtil.getKey(sign);
        if (Validator.isNull(key)) {
        	map.put("status", "1");
        	map.put("statusDesc", "请求参数非法");
            return map;
        }
        map.put("status", "0");
        map.put("statusDesc", "请求成功");
        int type = 0;
        if(StringUtils.isNotEmpty(clientStr)){
        	if(clientStr.equals(CustomConstants.CLIENT_ANDROID)){
        		type = 1;
        	}else if(clientStr.equals(CustomConstants.CLIENT_IOS)){
        		type = 2;
        	}
        }

        //版本更新 逻辑修改  pcc   20180130  stats
        if(StringUtils.isEmpty(versionStr) ){
            //没有版本号
            map.put("newVersion",versionStr);
            map.put("action","2");
            map.put("url", "");
            map.put("content", "");
            return map;
        }
        
        //版本号为  1.0.0.16  16为渠道号 不记版本
        String vers[] = versionStr.split("\\."); // 取前三位版本号
        if(vers != null && vers.length > 0){
            versionStr = vers[0] +"." + vers[1] +"." + vers[2];
        }
        Version version = appCommonService.getNewVersionByType(type);
        
        Version forceVersion=appCommonService.getVersionByType(type,0,null);
        
        Version currentVersion=appCommonService.getVersionByType(type,null,versionStr);
        if(currentVersion == null){
            map.put("status","1");
            map.put("statusDesc","当前版本号未在hyjf_version中配置");
            logger.error("当前版本号未在hyjf_version中配置....");
            return map;
        }

        if(version == null){
            map.put("status","1");
            map.put("statusDesc","获取最新版本号异常");
            return map;
        }
        if(forceVersion == null){
            map.put("status","1");
            map.put("statusDesc","获取强制更新版本号异常");
            return map;
        }
        
        if(versionStr.equals(forceVersion.getVersion())){
            // 当前版本号等于强制更新版本号操作
            if(versionStr.equals(version.getVersion())){
                //当前版本号等于最新版本号
                map.put("newVersion",versionStr);
                map.put("action","2");
                map.put("url", "");
                map.put("content", "");
            }else{
                //当前版本号不等于最新版本号
                map.put("newVersion",version.getVersion());
                map.put("action",String.valueOf(version.getIsupdate()));
                map.put("url", version.getUrl());
                map.put("content", version.getContent());
            }
        } else {
            if(forceVersion.getId()<currentVersion.getId()&&currentVersion.getId()<version.getId()){
                //强制更新和最新版本中间版本
                map.put("newVersion",version.getVersion());
                map.put("action",String.valueOf(version.getIsupdate()));
                map.put("url", version.getUrl());
                map.put("content", version.getContent());
            } else {
            	
                if(versionStr.equals(version.getVersion())){
                    map.put("newVersion",versionStr);
                    map.put("action","2");
                    map.put("url", "");
                    map.put("content", "");
                }else{
                  //强制更新和最新版本中间版本
                    map.put("newVersion",version.getVersion());
                    map.put("action",String.valueOf(forceVersion.getIsupdate()));
                    map.put("url", version.getUrl());
                    map.put("content", version.getContent());
                }
            }
        }
        
        //版本更新 逻辑修改  pcc   20180130  end
        LogUtil.endLog(THIS_CLASS, AppCommonDefine.VERSION_MAPPING);
        return map;
    }
    /**
     * 接收设备唯一标识
     * （数据库没有 存储，有 更新）
     * @param request
     * @param form
     * @return
     */
    @ResponseBody
    @RequestMapping(value = AppCommonDefine.MOBILE_CODE_MAPPING, method = RequestMethod.POST)
    public JSONObject mobileCode(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(THIS_CLASS, AppCommonDefine.MOBILE_CODE_MAPPING);
		JSONObject map = new JSONObject();
		map.put("request", WEB_URL + AppCommonDefine.REQUEST_MAPPING + AppCommonDefine.MOBILE_CODE_MAPPING);
        // 唯一标识
        String sign = request.getParameter("sign");
        // 平台
		String clientStr = request.getParameter("platform");
		//设备标识码
        String mobileCodeStr = request.getParameter("mobileCode");
        //版本号
        String versionStr = request.getParameter("version");
        // 取得加密用的Key
        String key = SecretUtil.getKey(sign);
        if (Validator.isNull(key)) {
        	map.put("status", "1");
        	map.put("statusDesc", "请求参数非法");
            return map;
        }
        if(StringUtils.isEmpty(mobileCodeStr)){
        	map.put("status","1");
        	map.put("statusDesc","请求参数非法");
        	return map;
        }

        int userid = SecretUtil.getUserId(sign);//用户id
        map.put("status","0");
        map.put("statusDesc","请求成功");
        
        //版本号为  1.0.0.16  16为渠道号 不记版本
        if(StringUtils.isNotEmpty(versionStr) ){
        		String vers[] = versionStr.split("\\."); // 取渠道号
        		if(vers != null && vers.length > 0){
        			versionStr = vers[3] ;
        		}
        }
        try {
             MobileCode mobileCode = appCommonService.getMobileCodeByUserId(userid);
             if(mobileCode != null){
            	 if(!mobileCode.getMobileCode().equals(mobileCodeStr)){
            		 mobileCode.setMobileCode(mobileCodeStr);
            		 mobileCode.setClient(clientStr);
            		 mobileCode.setPackageCode(versionStr);
            		 mobileCode.setSign(sign);
            		 appCommonService.updateMobileCode(mobileCode);
            	 }
             }else{
            	 mobileCode = new MobileCode();
            	 mobileCode.setMobileCode(mobileCodeStr);
            	 mobileCode.setUserId(userid);
            	 mobileCode.setSign(sign);
            	 mobileCode.setClient(clientStr);
            	 mobileCode.setPackageCode(versionStr);
            	 appCommonService.insertMobileCode(mobileCode);
             }
		} catch (Exception e) {
			map.put("status","1");
        	map.put("statusDesc","更新设备唯一标识异常");
		}
        LogUtil.endLog(THIS_CLASS, AppCommonDefine.MOBILE_CODE_MAPPING);
        return map;
    }
    /**
     * 设置角标
     * @param request
     * @param form
     * @return
     */
    @ResponseBody
    @RequestMapping(value = AppCommonDefine.CORNER_MAPPING,method = RequestMethod.POST)
    public JSONObject setCorner(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(THIS_CLASS, AppCommonDefine.CORNER_MAPPING);

		JSONObject map = new JSONObject();
		map.put("request", WEB_URL + AppCommonDefine.REQUEST_MAPPING + AppCommonDefine.CORNER_MAPPING);
        // 唯一标识
//        String sign = request.getParameter("sign");
//        //角标
//        String cornerStr = request.getParameter("corner");
        // 取得加密用的Key
//        String key = SecretUtil.getKey(sign);
//        if (Validator.isNull(key)) {
//        	map.put("status", "1");
//        	map.put("statusDesc", "请求参数非法");
//            return map;
//        }
//        if(StringUtils.isEmpty(cornerStr)){
//        	map.put("status","1");
//        	map.put("statusDesc","请求参数非法");
//        }
        map.put("status","0");
        map.put("statusDesc","设置成功");
//        try {
//        	int corner = Integer.parseInt(cornerStr);
//        	UserCorner cornerInfo = appCommonService.getUserCornerBySign(sign);
//        	if(cornerInfo != null){
//        		cornerInfo.setCorner(corner);
//        		appCommonService.updateUserCorner(cornerInfo);
//        	}else{
//        		cornerInfo = new UserCorner();
//        		cornerInfo.setCorner(corner);
//        		cornerInfo.setSign(sign);
//        		appCommonService.insertUserCorner(cornerInfo);
//        	}
//		} catch (Exception e) {
//        	map.put("status","1");
//        	map.put("statusDesc","设置角标异常");
//		}
        LogUtil.endLog(THIS_CLASS, AppCommonDefine.CORNER_MAPPING);
        return map;
    }

    
    /**
     * 获取最新版本号下载地址
     * @param request
     * @param form
     * @return
     */
    @ResponseBody
    @RequestMapping(value = AppCommonDefine.NEW_VERSION_URL_MAPPING)
    public JSONObject getNewVersionURL(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(THIS_CLASS, AppCommonDefine.VERSION_MAPPING);

        JSONObject map = new JSONObject();
        map.put("status", "000");
        map.put("request", WEB_URL + AppCommonDefine.REQUEST_MAPPING + AppCommonDefine.VERSION_MAPPING);
        map.put("statusDesc", "请求成功");
        Version version = appCommonService.getNewVersionByType(1);

        map.put("url", version.getUrl());
        //版本更新 逻辑修改  pcc   20180130  end
        LogUtil.endLog(THIS_CLASS, AppCommonDefine.VERSION_MAPPING);
        return map;
    }

}
