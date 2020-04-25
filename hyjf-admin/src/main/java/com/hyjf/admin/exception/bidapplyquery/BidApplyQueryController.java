/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
/**
 * 
 */
package com.hyjf.admin.exception.bidapplyquery;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;

/**
 * @author libin
 * 出借人投标申请查询Controller
 * @version BidApplyQueryController.java, v0.1 2018年8月16日 上午10:04:35
 */
@Controller
@RequestMapping(value = BidApplyQueryDefine.REQUEST_MAPPING)
public class BidApplyQueryController extends BaseController{
	
	@Autowired
	private BidApplyQueryService bidApplyQueryService;
	
	/**
	 * 画面初始化
	 * @param request
	 * @param form
	 * @return
	 * @author libin
	 * @date 2018年8月16日 上午10:04:35
	 */
	@RequestMapping(BidApplyQueryDefine.INIT_ACTION)
	@RequiresPermissions(BidApplyQueryDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, BidApplyQueryBean form) {
		LogUtil.startLog(BidApplyQueryController.class.toString(), BidApplyQueryDefine.INIT_ACTION);
		ModelAndView modelAndView = new ModelAndView(BidApplyQueryDefine.LIST_PATH);
		LogUtil.endLog(BidApplyQueryController.class.toString(), BidApplyQueryDefine.INIT_ACTION);
		return modelAndView;
	}
	
	/**
	 * 查询按钮
	 * @param request
	 * @param form
	 * @return
	 * @author libin
	 * @date 2018年8月16日 上午10:04:35
	 */
	@RequestMapping(BidApplyQueryDefine.SEARCH_ACTION)
	@RequiresPermissions(BidApplyQueryDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, BidApplyQueryBean form) {
		LogUtil.startLog(BidApplyQueryController.class.toString(), BidApplyQueryDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(BidApplyQueryDefine.LIST_PATH);
		//单笔资金类业务交易查询
		this.bidApplyQuerySearch(request, modelAndView, form);
		LogUtil.endLog(BidApplyQueryController.class.toString(), BidApplyQueryDefine.SEARCH_ACTION);
		return modelAndView;
	}
	
	/**
	 * 出借人投标申请查询
	 * @param request
	 * @param modelAndView
	 * @param form
	 * @author libin
	 * @date 2018年8月16日 上午10:04:35
	 */
	private void bidApplyQuerySearch(HttpServletRequest request, ModelAndView modelAndView, BidApplyQueryBean form) {
		JSONObject result = this.bidApplyQueryService.bidApplyQuerySearch(form);
		form.setResult(result);
		modelAndView.addObject(BidApplyQueryDefine.BID_APPLY_QUERY_FORM, form);
	}
}
