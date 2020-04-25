package com.hyjf.admin.manager.debt.debtborrowfull;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowFullCustomize;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = DebtBorrowFullDefine.REQUEST_MAPPING)
public class DebtBorrowFullController extends BaseController {

	@Autowired
	private DebtBorrowFullService debtBorrowFullService;

	/**
	 * 复审记录
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(DebtBorrowFullDefine.INIT)
	@RequiresPermissions(DebtBorrowFullDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute("borrowFullBean") DebtBorrowFullBean form) {
		LogUtil.startLog(DebtBorrowFullController.class.toString(), DebtBorrowFullDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(DebtBorrowFullDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(DebtBorrowFullController.class.toString(), DebtBorrowFullDefine.INIT);
		return modelAndView;
	}

	/**
	 * 查找
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(DebtBorrowFullDefine.SEARCH_ACTION)
	@RequiresPermissions(DebtBorrowFullDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, @ModelAttribute("borrowFullBean") DebtBorrowFullBean form) {
		LogUtil.startLog(DebtBorrowFullController.class.toString(), DebtBorrowFullDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(DebtBorrowFullDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(DebtBorrowFullController.class.toString(), DebtBorrowFullDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, DebtBorrowFullBean form) {

		DebtBorrowCommonCustomize debtBorrowCustomize = new DebtBorrowCommonCustomize();
		// 借款编码
		debtBorrowCustomize.setBorrowNidSrch(form.getBorrowNidSrch());
		// 项目名称
		debtBorrowCustomize.setBorrowNameSrch(form.getBorrowNameSrch());
		// 用户名
		debtBorrowCustomize.setUsernameSrch(form.getUsernameSrch());
		// 满标时间
		debtBorrowCustomize.setTimeStartSrch(form.getTimeStartSrch());
		// 满标时间
		debtBorrowCustomize.setTimeEndSrch(form.getTimeEndSrch());

		Long count = this.debtBorrowFullService.countBorrowFull(debtBorrowCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			debtBorrowCustomize.setLimitStart(paginator.getOffset());
			debtBorrowCustomize.setLimitEnd(paginator.getLimit());
			List<DebtBorrowFullCustomize> recordList = this.debtBorrowFullService.selectBorrowFullList(debtBorrowCustomize);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}
		modelAndView.addObject(DebtBorrowFullDefine.BORROW_FORM, form);
	}

	/**
	 * 复审的详细画面
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(DebtBorrowFullDefine.FULL_INFO_ACTION)
	@RequiresPermissions(DebtBorrowFullDefine.PERMISSIONS_BORROW_FULL)
	public ModelAndView fullAction(HttpServletRequest request, DebtBorrowFullBean form) {
		LogUtil.startLog(DebtBorrowFullController.class.toString(), DebtBorrowFullDefine.FULL_INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(DebtBorrowFullDefine.REVERIFY_PATH);

		// 复审的详细
		this.getFullInfo(modelAndView, form);

		modelAndView.addObject(DebtBorrowFullDefine.BORROW_FORM, form);

		LogUtil.endLog(DebtBorrowFullController.class.toString(), DebtBorrowFullDefine.FULL_INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 复审的详细
	 * 
	 * @param modelAndView
	 * @param form
	 */
	private void getFullInfo(ModelAndView modelAndView, DebtBorrowFullBean form) {
		String borrowNid = form.getBorrowNid();

		if (StringUtils.isNotEmpty(borrowNid)) {
			DebtBorrowFullCustomize reverifyInfo = this.debtBorrowFullService.selectFullInfo(borrowNid);
			if (reverifyInfo != null) {
				modelAndView.addObject("reverifyInfo", reverifyInfo);

				List<DebtBorrowFullCustomize> fullList = this.debtBorrowFullService.selectFullList(borrowNid, -1, -1);
				if (fullList != null && fullList.size() > 0) {
					Paginator paginator = new Paginator(form.getPaginatorPage(), fullList.size());
					fullList = this.debtBorrowFullService.selectFullList(borrowNid, paginator.getOffset(), paginator.getLimit());
					form.setPaginator(paginator);
					modelAndView.addObject("fullList", fullList);

					DebtBorrowFullCustomize sumAmount = this.debtBorrowFullService.sumAmount(borrowNid);
					modelAndView.addObject("sumAmount", sumAmount);
				}
			}
		}
	}

	/**
	 * 复审的详细画面确定按钮
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(DebtBorrowFullDefine.FULL_ACTION)
	@RequiresPermissions(DebtBorrowFullDefine.PERMISSIONS_BORROW_FULL)
	public ModelAndView fullInfoAction(HttpServletRequest request, RedirectAttributes attr, DebtBorrowFullBean form) {
		LogUtil.startLog(DebtBorrowFullController.class.toString(), DebtBorrowFullDefine.FULL_ACTION);
		ModelAndView modelAndView = new ModelAndView(DebtBorrowFullDefine.REVERIFY_PATH);

		this.validatorFieldCheck(modelAndView, form);

		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			// 复审的详细
			this.getFullInfo(modelAndView, form);
			modelAndView.addObject("reverifyRemark", form.getReverifyRemark());
			modelAndView.addObject(DebtBorrowFullDefine.BORROW_FORM, form);
			return modelAndView;
		}
		// 检索项目关联计划是否有正在清算中的计划
		int count = debtBorrowFullService.countDebtInvestListByBorrowNid(form.getBorrowNid());
		if (count > 0) {
			// 标的有清算中计划,跳回原画面
			// 复审的详细
			this.getFullInfo(modelAndView, form);
			modelAndView.addObject("reverifyRemark", form.getReverifyRemark());
			modelAndView.addObject(DebtBorrowFullDefine.BORROW_FORM, form);
			return modelAndView;
		}

		attr.addFlashAttribute("borrowFullBean", form);
		modelAndView = new ModelAndView("redirect:/manager/debt/debtborrowfull/init");
		this.debtBorrowFullService.updateReverifyRecord(form);

		LogUtil.endLog(DebtBorrowFullController.class.toString(), DebtBorrowFullDefine.FULL_ACTION);
		return modelAndView;
	}

	/**
	 * 复审的详细画面确定按钮
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(DebtBorrowFullDefine.REPEAT_ACTION)
	public ModelAndView repeatAction(HttpServletRequest request, RedirectAttributes attr, DebtBorrowFullBean form) {
		LogUtil.startLog(DebtBorrowFullController.class.toString(), DebtBorrowFullDefine.REPEAT_ACTION);
		ModelAndView modelAndView = new ModelAndView(DebtBorrowFullDefine.REVERIFY_PATH);

		attr.addFlashAttribute("borrowFullBean", form);
		modelAndView = new ModelAndView("redirect:/manager/debt/debtborrowfull/init");
		this.debtBorrowFullService.updateBorrowApicronRecord(form);

		LogUtil.endLog(DebtBorrowFullController.class.toString(), DebtBorrowFullDefine.REPEAT_ACTION);
		return modelAndView;
	}

	/**
	 * 验证
	 * 
	 * @param mav
	 * @param form
	 */
	private void validatorFieldCheck(ModelAndView mav, DebtBorrowFullBean form) {
		// 审核备注
		ValidatorFieldCheckUtil.validateMaxLength(mav, "reverifyRemark", form.getReverifyRemark(), 255, true);
		// 是否已经被审核
		boolean flag = this.debtBorrowFullService.isBorrowStatus16(form);
		if (flag) {
			ValidatorFieldCheckUtil.validateSpecialError(mav, "statusError", "borrow.status.not.16");
		}
	}

	/**
	 * 流标
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(DebtBorrowFullDefine.OVER_ACTION)
	@RequiresPermissions(DebtBorrowFullDefine.PERMISSIONS_BORROW_OVER)
	public ModelAndView overAction(HttpServletRequest request, RedirectAttributes attr, DebtBorrowFullBean form) {
		LogUtil.startLog(DebtBorrowFullController.class.toString(), DebtBorrowFullDefine.OVER_ACTION);
		ModelAndView modelAndView = new ModelAndView("redirect:/manager/debt/debtborrowfull/init");
		attr.addFlashAttribute("borrowFullBean", form);
		modelAndView = new ModelAndView("redirect:/manager/debt/debtborrowfull/init");
		this.debtBorrowFullService.updateBorrowRecordOver(form);

		LogUtil.endLog(DebtBorrowFullController.class.toString(), DebtBorrowFullDefine.OVER_ACTION);
		return modelAndView;
	}
}
