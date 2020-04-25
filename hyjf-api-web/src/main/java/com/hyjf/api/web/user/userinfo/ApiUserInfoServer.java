package com.hyjf.api.web.user.userinfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hyjf.api.web.BaseController;
import com.hyjf.common.enums.utils.MsgEnum;
import com.hyjf.common.result.ResultBean;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.validator.CheckUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.user.ApiUserPostBean;
import com.hyjf.user.common.ApiCommonService;
import com.hyjf.user.userinfo.ApiUserInfoService;

/**
 * @author liubin
 */
@RestController
@RequestMapping(value = ApiUserInfoDefine.REQUEST_MAPPING)
public class ApiUserInfoServer extends BaseController {
	@Autowired
	private ApiCommonService ApiCommonService;
	@Autowired
	private ApiUserInfoService ApiUserInfoService;
	
	/**
	 * 获取用户资产总额
	 * @param form
	 * @return
	 */
    @RequestMapping(value = ApiUserInfoDefine.TOTALASSETS_MAPPING)
    public ResultBean<String> totalAssets(@RequestBody ApiUserPostBean bean){
		// 验证
    	//传入信息验证 
		//(适用 信息码和信息在枚举中)
		CheckUtil.check(Validator.isNotNull(bean.getBindUniqueIdScy()), "Object.required", "bindUniqueIdScy");
		// (适用 信息码和信息在枚举中)
		CheckUtil.check(Validator.isNotNull(bean.getTimestamp()), MsgEnum.ERR_OBJECT_REQUIRED, "Timestamp");
		
		// 验签
		ApiCommonService.checkSign(bean);
		// 解密
		Long bindUniqueId = ApiCommonService.decrypt(bean);
		// 查询Userid
		Integer userId = ApiCommonService.getUserIdByBind(bindUniqueId, 1);
    	// 返回查询结果
    	return new ResultBean<String>(ApiUserInfoService.getTotleAssetsByUserId(userId));
    }
}
