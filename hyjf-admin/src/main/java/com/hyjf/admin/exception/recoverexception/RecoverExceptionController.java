package com.hyjf.admin.exception.recoverexception;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonService;
import com.hyjf.admin.manager.borrow.borrowrecover.BorrowRecoverController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.admin.AdminRecoverExceptionCustomize;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = RecoverExceptionDefine.REQUEST_MAPPING)
public class RecoverExceptionController extends BaseController {

	@Autowired
	private RecoverExceptionService recoverExceptionService;

	@Autowired
	private BorrowCommonService borrowCommonService;

	/**
	 * 复审记录
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(RecoverExceptionDefine.INIT)
	@RequiresPermissions(RecoverExceptionDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute(RecoverExceptionDefine.FORM) RecoverExceptionBean form) {
		LogUtil.startLog(RecoverExceptionController.class.toString(), RecoverExceptionDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(RecoverExceptionDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(RecoverExceptionController.class.toString(), RecoverExceptionDefine.INIT);
		return modelAndView;
	}

	/**
	 * 查找
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(RecoverExceptionDefine.SEARCH_ACTION)
	@RequiresPermissions(RecoverExceptionDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, @ModelAttribute(RecoverExceptionDefine.FORM) RecoverExceptionBean form) {
		LogUtil.startLog(RecoverExceptionController.class.toString(), RecoverExceptionDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(RecoverExceptionDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(RecoverExceptionController.class.toString(), RecoverExceptionDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, RecoverExceptionBean form) {

		AdminRecoverExceptionCustomize searchCriteria = new AdminRecoverExceptionCustomize();
		// 借款编码
		searchCriteria.setBorrowNidSrch(form.getBorrowNidSrch());
		// 项目名称
		searchCriteria.setBorrowNameSrch(form.getBorrowNameSrch());
		// 用户名
		searchCriteria.setUsernameSrch(form.getUsernameSrch());
		// 满标时间
		searchCriteria.setTimeStartSrch(form.getTimeStartSrch());
		// 满标时间
		searchCriteria.setTimeEndSrch(form.getTimeEndSrch());

		Long count = this.recoverExceptionService.queryCount(searchCriteria);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			searchCriteria.setLimitStart(paginator.getOffset());
			searchCriteria.setLimitEnd(paginator.getLimit());
			List<AdminRecoverExceptionCustomize> recordList = this.recoverExceptionService.queryRecordList(searchCriteria);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}
		modelAndView.addObject(RecoverExceptionDefine.FORM, form);
	}

	/**
	 * 放款异常修复
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(RecoverExceptionDefine.REPEAT_ACTION)
	@RequiresPermissions(RecoverExceptionDefine.PERMISSIONS_RECOVER_EXCEPTION)
	public ModelAndView repeatAction(HttpServletRequest request, RedirectAttributes attr, RecoverExceptionBean form) {
		LogUtil.startLog(RecoverExceptionController.class.toString(), RecoverExceptionDefine.REPEAT_ACTION);
		ModelAndView modelAndView = new ModelAndView("redirect:/exception/recoverexception/init");

		attr.addFlashAttribute(RecoverExceptionDefine.FORM, form);

		this.recoverExceptionService.updateBorrowApicronRecord(form);

		LogUtil.endLog(RecoverExceptionController.class.toString(), RecoverExceptionDefine.LIST_PATH);
		return modelAndView;
	}

	/**
	 * 还款详细画面
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(RecoverExceptionDefine.INFO_ACTION)
	public ModelAndView infoAction(HttpServletRequest request, RecoverExceptionBean form) {
		LogUtil.startLog(RecoverExceptionController.class.toString(), RecoverExceptionDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(RecoverExceptionDefine.INFO_PATH);

		// 创建分页
		this.createPageRecover(request, modelAndView, form);
		LogUtil.endLog(RecoverExceptionController.class.toString(), RecoverExceptionDefine.INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(RecoverExceptionDefine.SEARCH_INFO_ACTION)
	public ModelAndView searchInfoAction(HttpServletRequest request, RecoverExceptionBean form) {
		LogUtil.startLog(BorrowRecoverController.class.toString(), RecoverExceptionDefine.SEARCH_INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(RecoverExceptionDefine.INFO_PATH);
		// 创建分页
		this.createPageRecover(request, modelAndView, form);
		LogUtil.endLog(BorrowRecoverController.class.toString(), RecoverExceptionDefine.SEARCH_INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPageRecover(HttpServletRequest request, ModelAndView modelAndView, RecoverExceptionBean form) {

		AdminRecoverExceptionCustomize searchCriteria = new AdminRecoverExceptionCustomize();

		// 项目编号 检索条件
		searchCriteria.setBorrowNidSrch(form.getBorrowNidSrch());
		// 项目名称 检索条件
		searchCriteria.setBorrowNameSrch(form.getBorrowNameSrch());
		// 出借人 检索条件
		searchCriteria.setUsernameSrch(form.getUsernameSrch());
		// 出借订单号 检索条件
		searchCriteria.setOrderNumSrch(form.getOrderNumSrch());
		// 放款状态 检索条件
		searchCriteria.setIsRecoverSrch(form.getIsRecoverSrch());
		// 出借时间 检索条件
		searchCriteria.setTimeRecoverStartSrch(GetDate.getSearchStartTime(form.getTimeRecoverStartSrch()));
		// 出借时间 检索条件
		searchCriteria.setTimeRecoverEndSrch(GetDate.getSearchStartTime(form.getTimeRecoverEndSrch()));
		// 放款时间 检索条件
		searchCriteria.setTimeStartSrch(GetDate.getSearchStartTime(form.getTimeStartSrch()));
		// 放款时间 检索条件
		searchCriteria.setTimeEndSrch(GetDate.getSearchEndTime(form.getTimeEndSrch()));

		// 放款状态
		List<ParamName> recoverList = this.borrowCommonService.getParamNameList(CustomConstants.BORROW_RECOVER);
		modelAndView.addObject("recoverList", recoverList);

		Long count = this.recoverExceptionService.queryCountBorrowRecover(searchCriteria);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			searchCriteria.setLimitStart(paginator.getOffset());
			searchCriteria.setLimitEnd(paginator.getLimit());
			List<AdminRecoverExceptionCustomize> recordList = this.recoverExceptionService.queryBorrowRecoverList(searchCriteria);
			AdminRecoverExceptionCustomize sumAccount = this.recoverExceptionService.querySumBorrowRecoverList(searchCriteria);
			modelAndView.addObject("sumAccount", sumAccount);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}

		modelAndView.addObject(RecoverExceptionDefine.FORM, form);
	}
}
