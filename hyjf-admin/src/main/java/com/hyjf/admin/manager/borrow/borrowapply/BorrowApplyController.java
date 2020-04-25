package com.hyjf.admin.manager.borrow.borrowapply;

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
import com.hyjf.mybatis.model.customize.BorrowApplyCustomize;

/**
 * @package com.hyjf.admin.manager.borrow.borrowapply
 * @author GOGTZ-Z
 * @date 2015/12/03 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = BorrowApplyDefine.REQUEST_MAPPING)
public class BorrowApplyController extends BaseController {

	@Autowired
	private BorrowApplyService borrowApplyService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowApplyDefine.INIT)
	@RequiresPermissions(BorrowApplyDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, BorrowApplyBean form) {
		LogUtil.startLog(BorrowApplyController.class.toString(), BorrowApplyDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(BorrowApplyDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BorrowApplyController.class.toString(), BorrowApplyDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowApplyDefine.SEARCH_ACTION)
	@RequiresPermissions(BorrowApplyDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, BorrowApplyBean form) {
		LogUtil.startLog(BorrowApplyController.class.toString(), BorrowApplyDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(BorrowApplyDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BorrowApplyController.class.toString(), BorrowApplyDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, BorrowApplyBean form) {

		BorrowApplyCustomize borrowApplyCustomize = new BorrowApplyCustomize();

		// 姓名 检索条件
		borrowApplyCustomize.setNameSrch(form.getNameSrch());
		// 审核状态 检索条件
		borrowApplyCustomize.setStateSrch(form.getStateSrch());
		// 申请时间 检索条件
		borrowApplyCustomize.setTimeStartSrch(form.getTimeStartSrch());
		// 申请时间 检索条件
		borrowApplyCustomize.setTimeEndSrch(form.getTimeEndSrch());

		// 审核状态
		List<ParamName> applyStatusList = this.borrowApplyService.getParamNameList(CustomConstants.BORROW_APPLY_STATUS);
		modelAndView.addObject("applyStatusList", applyStatusList);

		Long count = this.borrowApplyService.countBorrowApply(borrowApplyCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			borrowApplyCustomize.setLimitStart(paginator.getOffset());
			borrowApplyCustomize.setLimitEnd(paginator.getLimit());
			List<BorrowApplyCustomize> recordList = this.borrowApplyService.selectBorrowApplyList(borrowApplyCustomize);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}

		modelAndView.addObject(BorrowApplyDefine.BORROW_FORM, form);
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowApplyDefine.INFO_ACTION)
	@RequiresPermissions(value = { BorrowApplyDefine.PERMISSIONS_INFO, BorrowApplyDefine.PERMISSION_AUDIT }, logical = Logical.OR)
	public ModelAndView infoAction(HttpServletRequest request, BorrowApplyBean form) {
		LogUtil.startLog(BorrowApplyController.class.toString(), BorrowApplyDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(BorrowApplyDefine.INFO_PATH);

		String id = form.getId();
		if (StringUtils.isNotEmpty(id) && GenericValidator.isInt(id)) {
			Loan loan = this.borrowApplyService.getLoan(id);
			form.setLoan(loan);
			form.setAddtime(GetDate.timestamptoStrYYYYMMDDHHMMSS(String.valueOf(loan.getAddtime())));
			modelAndView.addObject("status", loan.getState());
			// 审核状态
			List<ParamName> applyStatusList = this.borrowApplyService.getParamNameList(CustomConstants.BORROW_APPLY_STATUS);
			modelAndView.addObject("applyStatusList", applyStatusList);
		}

		modelAndView.addObject(BorrowApplyDefine.BORROW_FORM, form);

		LogUtil.endLog(BorrowApplyController.class.toString(), BorrowApplyDefine.INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 审核
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowApplyDefine.UPDATE_ACTION)
	@RequiresPermissions(BorrowApplyDefine.PERMISSION_AUDIT)
	public ModelAndView updateAction(HttpServletRequest request, BorrowApplyBean form) {
		LogUtil.startLog(BorrowApplyController.class.toString(), BorrowApplyDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(BorrowApplyDefine.INFO_PATH);

		// 验证
		this.validatorFieldCheck(modelAndView, form);
		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			String id = form.getId();
			Loan loan = this.borrowApplyService.getLoan(id);
			form.setLoan(loan);
			form.setAddtime(GetDate.timestamptoStrYYYYMMDDHHMMSS(String.valueOf(loan.getAddtime())));
			modelAndView.addObject("status", loan.getState());
			// 审核状态
			List<ParamName> applyStatusList = this.borrowApplyService.getParamNameList(CustomConstants.BORROW_APPLY_STATUS);
			modelAndView.addObject("applyStatusList", applyStatusList);

			loan.setState(Integer.valueOf(form.getState()));
			loan.setRemark(form.getRemark());

			modelAndView.addObject(BorrowApplyDefine.BORROW_FORM, form);
			return modelAndView;
		}
		Loan loan = new Loan();
		loan.setId(Integer.valueOf(form.getId()));
		if (StringUtils.isNotEmpty(form.getState())) {
			loan.setState(Integer.valueOf(form.getState()));
		}
		loan.setRemark(form.getRemark());
		this.borrowApplyService.updateRecord(loan);

		modelAndView.addObject(BorrowApplyDefine.BORROW_FORM, form);
		modelAndView.addObject(BorrowApplyDefine.SUCCESS, BorrowApplyDefine.SUCCESS);
		LogUtil.endLog(BorrowApplyController.class.toString(), BorrowApplyDefine.INFO_PATH);
		return modelAndView;
	}

	/**
	 * 验证
	 * 
	 * @param mav
	 * @param form
	 */
	private void validatorFieldCheck(ModelAndView mav, BorrowApplyBean form) {
		// 审核状态
		ValidatorFieldCheckUtil.validateRequired(mav, "state", form.getState());
		// 审核备注
		ValidatorFieldCheckUtil.validateMaxLength(mav, "remark", form.getRemark(), 255, true);
	}
}
