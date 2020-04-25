package com.hyjf.admin.exception.increaseinterestexception;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.admin.AdminIncreaseInterestExceptionCustomize;

/**
 * 融通宝加息还款异常处理Controller
 * 
 * @ClassName IncreaseInterestExceptionController
 * @author liuyang
 * @date 2017年1月5日 下午5:01:48
 */
@Controller
@RequestMapping(value = IncreaseInterestExceptionDefine.REQUEST_MAPPING)
public class IncreaseInterestExceptionController extends BaseController {

	/** 类名 */
	private static final String THIS_CLASS = IncreaseInterestExceptionController.class.getName();

	@Autowired
	private IncreaseInterestExceptionService increaseInterestExceptionService;

	@RequestMapping(IncreaseInterestExceptionDefine.INIT)
	@RequiresPermissions(IncreaseInterestExceptionDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, IncreaseInterestExceptionBean form) {
		LogUtil.startLog(THIS_CLASS, IncreaseInterestExceptionDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(IncreaseInterestExceptionDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, IncreaseInterestExceptionDefine.INIT);
		return modelAndView;
	}

	/**
	 * 
	 * 列表检索Action
	 * 
	 * @author liuyang
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(IncreaseInterestExceptionDefine.SEARCH_ACTION)
	@RequiresPermissions(IncreaseInterestExceptionDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, IncreaseInterestExceptionBean form) {
		LogUtil.startLog(THIS_CLASS, IncreaseInterestExceptionDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(IncreaseInterestExceptionDefine.LIST_PATH);
		// 分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, IncreaseInterestExceptionDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 分页技能维护
	 * 
	 * @Title createPage
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, IncreaseInterestExceptionBean form) {
		int count = increaseInterestExceptionService.countRecordList(form);
		if (count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			List<AdminIncreaseInterestExceptionCustomize> recordList = increaseInterestExceptionService.selectRecordList(form, paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
		}
		modelAndView.addObject(IncreaseInterestExceptionDefine.FORM, form);
	}

	/**
	 * 重新还款
	 * 
	 * @Title restartRepayAction
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(IncreaseInterestExceptionDefine.RESTART_REPAY_ACTION)
	@RequiresPermissions(IncreaseInterestExceptionDefine.PERMISSIONS_CONFIRM)
	public String restartRepayAction(HttpServletRequest request, IncreaseInterestExceptionBean form) {
		LogUtil.startLog(THIS_CLASS, IncreaseInterestExceptionDefine.RESTART_REPAY_ACTION);
		// 更新borrowApi
		if (StringUtils.isNotEmpty(form.getId()) && StringUtils.isNotEmpty(form.getBorrowNid())) {
			this.increaseInterestExceptionService.updateBorrowApr(form);
		}
		LogUtil.endLog(THIS_CLASS, IncreaseInterestExceptionDefine.RESTART_REPAY_ACTION);
		return "redirect:" + IncreaseInterestExceptionDefine.REQUEST_MAPPING + "/" + IncreaseInterestExceptionDefine.INIT;

	}
}
