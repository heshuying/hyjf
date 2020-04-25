package com.hyjf.admin.manager.plan.raise;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.DebtPlan;

/**
 * 计划募集Controller
 * 
 * @ClassName PlanRaiseController
 * @author liuyang
 * @date 2016年9月26日 下午2:59:37
 */
@Controller
@RequestMapping(value = PlanRaiseDefine.REQUEST_MAPPING)
public class PlanRaiseController extends BaseController {

	@Autowired
	private PlanRaiseService planRaiseService;

	/** 类名 */
	public static String THIS_CLASS = PlanRaiseController.class.toString();

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PlanRaiseDefine.INIT)
	@RequiresPermissions(PlanRaiseDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute("PlanRaiseBean") PlanRaiseBean form) {
		LogUtil.startLog(THIS_CLASS, PlanRaiseDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(PlanRaiseDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, PlanRaiseDefine.INIT);
		return modelAndView;
	}

	/**
	 * 检索Action
	 * 
	 * @Title searchAction
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PlanRaiseDefine.SEARCH_ACTION)
	@RequiresPermissions(PlanRaiseDefine.PERMISSIONS_SEARCH)
	public ModelAndView searchAction(HttpServletRequest request, @ModelAttribute("PlanRaiseBean") PlanRaiseBean form) {
		LogUtil.startLog(THIS_CLASS, PlanRaiseDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(PlanRaiseDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, PlanRaiseDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, PlanRaiseBean form) {

		// 检索计划发布列表件数
		int count = planRaiseService.countPlanRaise(form);
		if (count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			form.setLimitStart(paginator.getOffset());
			form.setLimitEnd(paginator.getLimit());
			// 计划发布列表
			List<DebtPlan> recordList = planRaiseService.selectPlanRaiseList(form);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}
		modelAndView.addObject(PlanRaiseDefine.PLAN_FORM, form);
	}
}
