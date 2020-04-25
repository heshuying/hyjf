
package com.hyjf.admin.exception.singletradeinfo;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;

/**
* 接口：单笔资金类业务交易查询 Controller类
* @author LiuBin
* @date 2017年7月31日 上午9:31:11
* 
*/ 
@Controller
@RequestMapping(value = SingleTradeInfoDefine.REQUEST_MAPPING)
public class SingleTradeInfoController extends BaseController {

	@Autowired
	private SingleTradeInfoService singleTradeInfoService;


	/**
	 * 画面初始化
	 * @param request
	 * @param form
	 * @return
	 * @author LiuBin
	 * @date 2017年7月31日 上午9:32:49
	 */
	@RequestMapping(SingleTradeInfoDefine.INIT_ACTION)
	@RequiresPermissions(SingleTradeInfoDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, SingleTradeInfoBean form) {
		LogUtil.startLog(SingleTradeInfoController.class.toString(), SingleTradeInfoDefine.INIT_ACTION);
		ModelAndView modelAndView = new ModelAndView(SingleTradeInfoDefine.LIST_PATH);
		LogUtil.endLog(SingleTradeInfoController.class.toString(), SingleTradeInfoDefine.INIT_ACTION);
		return modelAndView;
	}
	

	/**
	 * 查询按钮
	 * @param request
	 * @param form
	 * @return
	 * @author LiuBin
	 * @date 2017年7月31日 上午9:35:36
	 */
	@RequestMapping(SingleTradeInfoDefine.SEARCH_ACTION)
	@RequiresPermissions(SingleTradeInfoDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, SingleTradeInfoBean form) {
		LogUtil.startLog(SingleTradeInfoController.class.toString(), SingleTradeInfoDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(SingleTradeInfoDefine.LIST_PATH);
		//单笔资金类业务交易查询
		this.singleTradeInfoSearch(request, modelAndView, form);
		LogUtil.endLog(SingleTradeInfoController.class.toString(), SingleTradeInfoDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 单笔资金类业务交易查询
	 * @param request
	 * @param modelAndView
	 * @param form
	 * @author LiuBin
	 * @date 2017年7月31日 上午9:36:33
	 */
	private void singleTradeInfoSearch(HttpServletRequest request, ModelAndView modelAndView, SingleTradeInfoBean form) {
		JSONObject result = this.singleTradeInfoService.singleTradeInfoSearch(form);
		form.setResult(result);
		modelAndView.addObject(SingleTradeInfoDefine.SINGLE_TRADE_INFO_FORM, form);
	}
}
