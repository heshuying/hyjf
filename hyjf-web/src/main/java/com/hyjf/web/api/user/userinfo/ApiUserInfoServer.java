package com.hyjf.web.api.user.userinfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hyjf.common.result.ResultBean;
import com.hyjf.common.security.utils.SignUtil;
import com.hyjf.common.validator.CheckUtil;
import com.hyjf.web.api.base.ApiBaseController;
import com.hyjf.web.api.common.ApiCommonService;
import com.hyjf.web.api.user.ApiUserPostBean;

/**
 * @author liubin
 */
@Controller
@RequestMapping(value = ApiUserInfoDefine.REQUEST_MAPPING)
public class ApiUserInfoServer extends ApiBaseController {
	@Autowired
	private ApiCommonService ApiCommonService;
	@Autowired
	private ApiUserInfoService ApiUserInfoService;
	
	/**
	 * 获取用户资产总额
	 * @param form
	 * @return
	 */
    @ResponseBody
    @RequestMapping(value = ApiUserInfoDefine.TOTALASSETS_MAPPING)
    public ResultBean<String> totalAssets(@RequestBody ApiUserPostBean bean){
		// 验证
		ApiCommonService.checkPostBeanOfInfo(bean);
		// 验签
		CheckUtil.check(SignUtil.checkSignDefaultKey(bean.getChkValue(), bean.getBindUniqueIdScy(),bean.getPid(),bean.getTimestamp()), "sign");
		// 解密
		Long bindUniqueId = ApiCommonService.decrypt(bean);
		// 查询Userid
		Integer userId = ApiCommonService.getUserIdByBind(bindUniqueId, bean.getPid());
    	// 返回查询结果
    	return new ResultBean<String>(ApiUserInfoService.getTotleAssetsByUserId(userId));
    }
}
