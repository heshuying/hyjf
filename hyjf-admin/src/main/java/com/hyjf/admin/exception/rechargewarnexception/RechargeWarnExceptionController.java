package com.hyjf.admin.exception.rechargewarnexception;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.admin.AdminRechargeWarnExceptionCustomize;

/**
 * 充值管理
 *
 * @author HBZ
 */
@Controller
@RequestMapping(value = RechargeWarnExceptionDefine.REQUEST_MAPPING)
public class RechargeWarnExceptionController extends BaseController {
	@Autowired
	private RechargeWarnExceptionService rechargeWarnExceptionService;

	/**
	 * 账户管理 列表
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(RechargeWarnExceptionDefine.INIT)
	@RequiresPermissions(RechargeWarnExceptionDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RechargeWarnExceptionBean form) {
		LogUtil.startLog(RechargeWarnExceptionController.class.toString(), RechargeWarnExceptionDefine.INIT);
		ModelAndView modeAndView = new ModelAndView(RechargeWarnExceptionDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(RechargeWarnExceptionController.class.toString(), RechargeWarnExceptionDefine.INIT);
		return modeAndView;
	}

	/**
	 * 账户管理 列表
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(RechargeWarnExceptionDefine.RECHARGE_LIST_WITHQ)
	@RequiresPermissions(RechargeWarnExceptionDefine.PERMISSIONS_VIEW)
	public ModelAndView search(HttpServletRequest request, RechargeWarnExceptionBean form) {
		LogUtil.startLog(RechargeWarnExceptionController.class.toString(), RechargeWarnExceptionDefine.RECHARGE_LIST_WITHQ);
		ModelAndView modeAndView = new ModelAndView(RechargeWarnExceptionDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(RechargeWarnExceptionController.class.toString(), RechargeWarnExceptionDefine.RECHARGE_LIST_WITHQ);
		return modeAndView;
	}

	/**
	 * 分页
	 *
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modeAndView, RechargeWarnExceptionBean form) {
		AdminRechargeWarnExceptionCustomize rechargeCustomize = new AdminRechargeWarnExceptionCustomize();
		BeanUtils.copyProperties(form, rechargeCustomize);

		Integer count = this.rechargeWarnExceptionService.queryRechargeWarnCount(form);
		if (count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			form.setPaginator(paginator);

			rechargeCustomize.setLimitStart(paginator.getOffset());
			rechargeCustomize.setLimitEnd(paginator.getLimit());
			List<AdminRechargeWarnExceptionCustomize> rechargeCustomizes = this.rechargeWarnExceptionService.queryRechargeWarnList(rechargeCustomize);
			modeAndView.addObject("rechargeWarnExceptionList", rechargeCustomizes);
		}
		modeAndView.addObject(RechargeWarnExceptionDefine.RECHARGE_FORM, form);
	}

}
