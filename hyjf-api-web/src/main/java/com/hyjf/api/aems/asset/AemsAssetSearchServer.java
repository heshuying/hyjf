/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2017
 * Company: HYJF Corporation
 * @author: LiuBin
 * @version: 1.0
 * Created at: 2017年8月18日 下午2:36:15
 * Modification History:
 * Modified by : 
 */
	
package com.hyjf.api.aems.asset;

import com.hyjf.api.server.util.ErrorCodeConstant;
import com.hyjf.api.web.BaseController;
import com.hyjf.base.bean.BaseDefine;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.customize.AssetDetailCustomize;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Aems资产查询接口
 * @author Zha Daojian
 * @date 2018/9/13 14:49
 * @param
 * @return
 **/
@Controller
@RequestMapping(AemsAssetSearchDefine.REQUEST_MAPPING)
public class AemsAssetSearchServer extends BaseController{

	Logger _log = LoggerFactory.getLogger(AemsAssetSearchServer.class);

	@Autowired
	private AemsAssetSearchService assetSearchService;

	@ResponseBody
	@RequestMapping(value = AemsAssetSearchDefine.STATUS_ACTION, method = RequestMethod.POST)
	public AemsAssetStatusResultBean assetStatusSearch(@RequestBody AemsAssetStatusRequestBean bean) {

		AemsAssetStatusResultBean resultBean = new AemsAssetStatusResultBean();
		resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
		resultBean.setStatusDesc("请求参数非法");

		//空验证
		if (Validator.isNull(bean) || StringUtils.isBlank(bean.getAssetId())
				|| StringUtils.isBlank(bean.getInstCode()) ||  StringUtils.isBlank(bean.getChkValue())){
			resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
			resultBean.setStatusDesc("资产编号不为空");
			_log.info("-------------------请求参数非法--------------------");
			return resultBean;
		}
		
        //验签
        if(!this.AEMSVerifyRequestSign(bean, AemsAssetSearchDefine.REQUEST_MAPPING+AemsAssetSearchDefine.STATUS_ACTION)){
			resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000002);
			resultBean.setStatusDesc("验签失败！");
			_log.info("-------------------验签失败！--------------------");
			return resultBean;
        }
        
        _log.info(bean.getInstCode()+"  ----资产查询接口开始");
        
        //查询
        String assetId = bean.getAssetId();
        String instCode = bean.getInstCode();
        AssetDetailCustomize assetStatus = this.assetSearchService.selectStatusById(assetId, instCode);
        if (assetStatus == null) {
			resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_ZT000009);
			resultBean.setStatusDesc("未查询到该资产编号（" + assetId + ", " + instCode + "）！");
			_log.info("---------------未查询到该资产编号（" + assetId + ", " + instCode + "）！---------------");
			return resultBean;
		}
     		
        resultBean.setStatusForResponse(ErrorCodeConstant.SUCCESS);
        resultBean.setStatusDesc("请求成功");
        resultBean.setAssetStatus(assetStatus.getStatus().toString());
        resultBean.setStatusDesc(assetStatus.getStatusDesc());
        resultBean.setBorrowNid(assetStatus.getBorrowNid());
		resultBean.setNid(assetStatus.getNid());
        _log.info(bean.getInstCode()+"  ----资产查询接口结束");
        
		return resultBean;
	}
}

	