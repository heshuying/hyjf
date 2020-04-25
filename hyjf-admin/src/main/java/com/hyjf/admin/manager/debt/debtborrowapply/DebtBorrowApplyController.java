package com.hyjf.admin.manager.debt.debtborrowapply;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.GenericValidator;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.Loan;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowApplyCustomize;

/**
 * @package com.hyjf.admin.manager.borrow.borrowapply
 * @author GOGTZ-Z
 * @date 2015/12/03 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = DebtBorrowApplyDefine.REQUEST_MAPPING)
public class DebtBorrowApplyController extends BaseController {

	@Autowired
	private DebtBorrowApplyService debtBborrowApplyService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(DebtBorrowApplyDefine.INIT)
	@RequiresPermissions(DebtBorrowApplyDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, DebtBorrowApplyBean form) {
		LogUtil.startLog(DebtBorrowApplyController.class.toString(), DebtBorrowApplyDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(DebtBorrowApplyDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(DebtBorrowApplyController.class.toString(), DebtBorrowApplyDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(DebtBorrowApplyDefine.SEARCH_ACTION)
	@RequiresPermissions(DebtBorrowApplyDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, DebtBorrowApplyBean form) {
		LogUtil.startLog(DebtBorrowApplyController.class.toString(), DebtBorrowApplyDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(DebtBorrowApplyDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(DebtBorrowApplyController.class.toString(), DebtBorrowApplyDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, DebtBorrowApplyBean form) {

		DebtBorrowApplyCustomize borrowApplyCustomize = new DebtBorrowApplyCustomize();

		// 姓名 检索条件
		borrowApplyCustomize.setNameSrch(form.getNameSrch());
		// 审核状态 检索条件
		borrowApplyCustomize.setStateSrch(form.getStateSrch());
		// 申请时间 检索条件
		borrowApplyCustomize.setTimeStartSrch(form.getTimeStartSrch());
		// 申请时间 检索条件
		borrowApplyCustomize.setTimeEndSrch(form.getTimeEndSrch());

		// 审核状态
		List<ParamName> applyStatusList = this.debtBborrowApplyService.getParamNameList(CustomConstants.BORROW_APPLY_STATUS);
		modelAndView.addObject("applyStatusList", applyStatusList);

		Long count = this.debtBborrowApplyService.countBorrowApply(borrowApplyCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			borrowApplyCustomize.setLimitStart(paginator.getOffset());
			borrowApplyCustomize.setLimitEnd(paginator.getLimit());
			List<DebtBorrowApplyCustomize> recordList = this.debtBborrowApplyService.selectBorrowApplyList(borrowApplyCustomize);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}

		modelAndView.addObject(DebtBorrowApplyDefine.BORROW_FORM, form);
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(DebtBorrowApplyDefine.INFO_ACTION)
	@RequiresPermissions(value = { DebtBorrowApplyDefine.PERMISSIONS_INFO, DebtBorrowApplyDefine.PERMISSION_AUDIT }, logical = Logical.OR)
	public ModelAndView infoAction(HttpServletRequest request, DebtBorrowApplyBean form) {
		LogUtil.startLog(DebtBorrowApplyController.class.toString(), DebtBorrowApplyDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(DebtBorrowApplyDefine.INFO_PATH);

		String id = form.getId();
		if (StringUtils.isNotEmpty(id) && GenericValidator.isInt(id)) {
			Loan loan = this.debtBborrowApplyService.getLoan(id);
			form.setLoan(loan);
			form.setAddtime(GetDate.timestamptoStrYYYYMMDDHHMMSS(String.valueOf(loan.getAddtime())));
			modelAndView.addObject("status", loan.getState());
			// 审核状态
			List<ParamName> applyStatusList = this.debtBborrowApplyService.getParamNameList(CustomConstants.BORROW_APPLY_STATUS);
			modelAndView.addObject("applyStatusList", applyStatusList);
		}

		modelAndView.addObject(DebtBorrowApplyDefine.BORROW_FORM, form);

		LogUtil.endLog(DebtBorrowApplyController.class.toString(), DebtBorrowApplyDefine.INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 审核
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(DebtBorrowApplyDefine.UPDATE_ACTION)
	@RequiresPermissions(DebtBorrowApplyDefine.PERMISSION_AUDIT)
	public ModelAndView updateAction(HttpServletRequest request, DebtBorrowApplyBean form) {
		LogUtil.startLog(DebtBorrowApplyController.class.toString(), DebtBorrowApplyDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(DebtBorrowApplyDefine.INFO_PATH);

		// 验证
		this.validatorFieldCheck(modelAndView, form);
		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			String id = form.getId();
			Loan loan = this.debtBborrowApplyService.getLoan(id);
			form.setLoan(loan);
			form.setAddtime(GetDate.timestamptoStrYYYYMMDDHHMMSS(String.valueOf(loan.getAddtime())));
			modelAndView.addObject("status", loan.getState());
			// 审核状态
			List<ParamName> applyStatusList = this.debtBborrowApplyService.getParamNameList(CustomConstants.BORROW_APPLY_STATUS);
			modelAndView.addObject("applyStatusList", applyStatusList);

			loan.setState(Integer.valueOf(form.getState()));
			loan.setRemark(form.getRemark());

			modelAndView.addObject(DebtBorrowApplyDefine.BORROW_FORM, form);
			return modelAndView;
		}
		Loan loan = new Loan();
		loan.setId(Integer.valueOf(form.getId()));
		if (StringUtils.isNotEmpty(form.getState())) {
			loan.setState(Integer.valueOf(form.getState()));
		}
		loan.setRemark(form.getRemark());
		this.debtBborrowApplyService.updateRecord(loan);

		modelAndView.addObject(DebtBorrowApplyDefine.BORROW_FORM, form);
		modelAndView.addObject(DebtBorrowApplyDefine.SUCCESS, DebtBorrowApplyDefine.SUCCESS);
		LogUtil.endLog(DebtBorrowApplyController.class.toString(), DebtBorrowApplyDefine.INFO_PATH);
		return modelAndView;
	}

	/**
	 * 验证
	 * 
	 * @param mav
	 * @param form
	 */
	private void validatorFieldCheck(ModelAndView mav, DebtBorrowApplyBean form) {
		// 审核状态
		ValidatorFieldCheckUtil.validateRequired(mav, "state", form.getState());
		// 审核备注
		ValidatorFieldCheckUtil.validateMaxLength(mav, "remark", form.getRemark(), 255, true);
	}
}
