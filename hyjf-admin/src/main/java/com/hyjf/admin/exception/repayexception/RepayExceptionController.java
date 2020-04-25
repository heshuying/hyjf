package com.hyjf.admin.exception.repayexception;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.exception.openaccountexception.OpenAccountDefine;
import com.hyjf.admin.exception.repayexception.info.RepayExceptionInfoDefine;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonService;
import com.hyjf.admin.manager.borrow.borrowfull.BorrowFullController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.validator.Validator;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowRecover;
import com.hyjf.mybatis.model.auto.BorrowRecoverPlan;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.customize.RepayExceptionCustomize;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-T
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = RepayExceptionDefine.REQUEST_MAPPING)
public class RepayExceptionController extends BaseController {

	@Autowired
	private RepayExceptionService repaymentExceptionService;

	@Autowired
	private BorrowCommonService borrowCommonService;

	// ****************************************还款掉单****************************************
	/**
	 * 画面初始化
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(RepayExceptionDefine.INIT)
	@RequiresPermissions(RepayExceptionDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute(RepayExceptionDefine.REPAYMENT_FORM) RepayExceptionBean form) {
		LogUtil.startLog(RepayExceptionController.class.toString(), RepayExceptionDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(RepayExceptionDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(RepayExceptionController.class.toString(), RepayExceptionDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面初始化
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(RepayExceptionDefine.SEARCH_ACTION)
	@RequiresPermissions(RepayExceptionDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, RepayExceptionBean form) {
		LogUtil.startLog(RepayExceptionController.class.toString(), RepayExceptionDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(RepayExceptionDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(RepayExceptionController.class.toString(), RepayExceptionDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 创建分页
	 *
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, RepayExceptionBean form) {
		RepayExceptionCustomize repayExceptionCustomize = new RepayExceptionCustomize();
		// 还款方式
		List<BorrowStyle> repayTypeList = this.borrowCommonService.borrowStyleList("");
		modelAndView.addObject("repayTypeList", repayTypeList);
		BeanUtils.copyProperties(form, repayExceptionCustomize);

		Long count = this.repaymentExceptionService.countBorrowRepayment(repayExceptionCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			repayExceptionCustomize.setLimitStart(paginator.getOffset());
			repayExceptionCustomize.setLimitEnd(paginator.getLimit());
			List<RepayExceptionCustomize> recordList = this.repaymentExceptionService.selectBorrowRepaymentList(repayExceptionCustomize);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);

		}
		modelAndView.addObject(RepayExceptionDefine.REPAYMENT_FORM, form);
	}

	/**
	 * 重新还款按钮
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(RepayExceptionDefine.RESTART_REPAY_ACTION)
	public ModelAndView restartRepayAction(HttpServletRequest request, RedirectAttributes attr, RepayExceptionBean form) throws ParseException {
		LogUtil.startLog(BorrowFullController.class.toString(), RepayExceptionDefine.RESTART_REPAY_ACTION);
		ModelAndView modelAndView = new ModelAndView(RepayExceptionDefine.LIST_PATH);

		attr.addFlashAttribute(RepayExceptionDefine.REPAYMENT_FORM, form);
		modelAndView = new ModelAndView(RepayExceptionDefine.RE_LIST_PATH);
		if (form != null && Validator.isNotNull(form.getBorrowNidHidden()) && Validator.isNotNull(form.getPeriodNowHidden())) {
			this.repaymentExceptionService.updateBorrowApicronRecord(form.getBorrowNidHidden(), form.getPeriodNowHidden());
		}

		LogUtil.endLog(BorrowFullController.class.toString(), RepayExceptionDefine.RESTART_REPAY_ACTION);
		return modelAndView;
	}

	/**
	 * 跳转到详情
	 *
	 * @param request
	 * @param form
	 */
	@RequestMapping(RepayExceptionDefine.TO_RECOVER_ACTION)
	public ModelAndView toRecoverAction(HttpServletRequest request, RepayExceptionBean form, RedirectAttributes attr) {
		LogUtil.startLog(RepayExceptionController.class.toString(), RepayExceptionDefine.TO_RECOVER_ACTION);
		ModelAndView modelAndView = new ModelAndView(RepayExceptionInfoDefine.RE_LIST_PATH);
		attr.addAttribute("borrowNidSrch", form.getBorrowNidHidden());
		attr.addAttribute("borrowPeriodSrch", form.getPeriodNowHidden());
		attr.addAttribute("monthType", form.getMonthType());
		// 跳转到还款计划
		LogUtil.endLog(RepayExceptionController.class.toString(), RepayExceptionDefine.TO_RECOVER_ACTION);
		return modelAndView;
	}

	/**
	 * 重新还款按钮 TODO
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping("repayTemp")
	public ModelAndView repayTemp(HttpServletRequest request, RedirectAttributes attr, RepayExceptionBean form) throws ParseException {
		LogUtil.startLog(BorrowFullController.class.toString(), RepayExceptionDefine.RESTART_REPAY_ACTION);
		ModelAndView modelAndView = new ModelAndView(RepayExceptionDefine.LIST_PATH);

		attr.addFlashAttribute(RepayExceptionDefine.REPAYMENT_FORM, form);
		modelAndView = new ModelAndView(RepayExceptionDefine.RE_LIST_PATH);

		// String pwd = form.getData();
		// if ("88470C4AC0828B1B5090C1A6B12253A7".equals(pwd)) {
		// this.repaymentExceptionService.updateRrepayTemp(form.getBorrowNid(),
		// 1);
		// }

		LogUtil.endLog(BorrowFullController.class.toString(), RepayExceptionDefine.RESTART_REPAY_ACTION);
		return modelAndView;
	}

	/**
	 * 重新还款按钮 TODO
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(RepayExceptionDefine.INIT_REPAY_ACTION)
	@RequiresPermissions(RepayExceptionDefine.PERMISSIONS_ADD_REPAY)
	public ModelAndView initRepay(HttpServletRequest request) throws ParseException {

		LogUtil.startLog(RepayExceptionDefine.THIS_CLASS, RepayExceptionDefine.INIT_REPAY_ACTION);
		ModelAndView modelAndView = new ModelAndView(RepayExceptionDefine.INIT_REPAY_PATH);
		LogUtil.endLog(RepayExceptionDefine.THIS_CLASS, RepayExceptionDefine.INIT_REPAY_ACTION);
		return modelAndView;
	}

	/**
	 * 重新还款按钮 TODO
	 *
	 * @param request
	 * @param form
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(RepayExceptionDefine.ADD_REPAY_ACTION)
	@RequiresPermissions(RepayExceptionDefine.PERMISSIONS_ADD_REPAY)
	public ModelAndView addRepay(HttpServletRequest request) throws Exception {

		LogUtil.startLog(RepayExceptionDefine.THIS_CLASS, RepayExceptionDefine.ADD_REPAY_ACTION);
		ModelAndView modelAndView = new ModelAndView(RepayExceptionDefine.INIT_REPAY_PATH);
		String orderId = request.getParameter("orderId");
		String period = request.getParameter("period");
		this.checkRepayParam(modelAndView, orderId, period);
		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			modelAndView.addObject("orderId", orderId);
			modelAndView.addObject("period", period);
			LogUtil.errorLog(RepayExceptionDefine.THIS_CLASS, RepayExceptionDefine.ADD_REPAY_ACTION, "输入内容验证失败", null);
			return modelAndView;
		} else {
			boolean flag = this.repaymentExceptionService.updateBorrowRepay(orderId, period);
			if (flag) {
				modelAndView.addObject(RepayExceptionDefine.SUCCESS, RepayExceptionDefine.SUCCESS);
			} else {
				modelAndView.addObject("orderId", orderId);
				modelAndView.addObject("period", period);
				LogUtil.errorLog(OpenAccountDefine.THIS_CLASS, RepayExceptionDefine.ADD_REPAY_ACTION, "此笔还款失败", null);
			}
		}
		LogUtil.endLog(RepayExceptionDefine.THIS_CLASS, RepayExceptionDefine.ADD_REPAY_ACTION);
		return modelAndView;
	}

	private void checkRepayParam(ModelAndView modelAndView, String orderId, String period) {

		// 订单号校验
		ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "orderId", orderId, 30, true);
		// 还款期数校验
		ValidatorFieldCheckUtil.validateRequired(modelAndView, "period", period);
		if (!ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			BorrowRecover borrowRecover = this.repaymentExceptionService.selectBorrowRecoverByNid(orderId);
			if (borrowRecover == null) {
				// 还款记录
				ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "orderId", "repay.borrow.recover", "还款记录总表查询错误!");
			} else {
				String borrowNid = borrowRecover.getBorrowNid();
				Borrow borrow = this.repaymentExceptionService.selectBorrow(borrowNid);
				String borrowStyle = borrow.getBorrowStyle();
				// 是否月标(true:月标, false:天标)
				boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
						|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
				if (isMonth) {
					BorrowRecoverPlan borrowRecoverPlan = this.repaymentExceptionService.selectBorrowRecoverPlan(orderId, Integer.parseInt(period));
					if (borrowRecoverPlan == null) {
						// 还款记录
						ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "period", "repay.borrow.recover.plan", "还款记录分期表查询错误!");
					}
				}
			}
		}
	}

}
