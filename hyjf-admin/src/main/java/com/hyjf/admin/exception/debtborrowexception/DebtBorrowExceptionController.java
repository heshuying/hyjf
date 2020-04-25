/**
 * Description:可以删除异常项目的列表,只查看初审中和待发布项目信息
 * Copyright: Copyright (HYJF Corporation)2016
 * Company: HYJF Corporation
 * @author: zhuxiaodong
 * @version: 1.0
 * Created at: 2016年3月8日 上午9:06:32
 * Modification History:
 * Modified by : 
 */
package com.hyjf.admin.exception.debtborrowexception;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.debt.debtborrowcommon.DebtBorrowCommonService;
import com.hyjf.admin.promotion.utm.UtmController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowExceptionDeleteBean;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowExceptionDeleteSrchBean;

/**
 * @package com.hyjf.admin.exception.borrowexception
 * @author 朱晓东
 * @date 2016/03/08 9:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = DebtBorrowExceptionDefine.REQUEST_MAPPING)
public class DebtBorrowExceptionController extends BaseController {

	@Autowired
	private DebtBorrowExceptionService borrowExceptionService;

	@Autowired
	private DebtBorrowCommonService borrowCommonService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(DebtBorrowExceptionDefine.INIT)
	@RequiresPermissions(DebtBorrowExceptionDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, DebtBorrowExceptionBean form) {
		LogUtil.startLog(DebtBorrowExceptionController.class.toString(), DebtBorrowExceptionDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(DebtBorrowExceptionDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(DebtBorrowExceptionController.class.toString(), DebtBorrowExceptionDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(DebtBorrowExceptionDefine.SEARCH_ACTION)
	@RequiresPermissions(DebtBorrowExceptionDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, DebtBorrowExceptionBean form) {
		LogUtil.startLog(DebtBorrowExceptionController.class.toString(), DebtBorrowExceptionDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(DebtBorrowExceptionDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(DebtBorrowExceptionController.class.toString(), DebtBorrowExceptionDefine.INIT);
		return modelAndView;
	}
	
	/**
	 * 查看删除的项目画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(DebtBorrowExceptionDefine.DELETEINIT)
	@RequiresPermissions(DebtBorrowExceptionDefine.PERMISSIONSLOG_SEARCH)
	public ModelAndView borrowdeleteinit(HttpServletRequest request, DebtBorrowExceptionDeleteSrchBean form) {
		LogUtil.startLog(DebtBorrowExceptionController.class.toString(), DebtBorrowExceptionDefine.DELETEINIT);
		ModelAndView modelAndView = new ModelAndView(DebtBorrowExceptionDefine.DELETE_LIST_PATH);
		// 创建分页
		this.createBorrowDeletePage(request, modelAndView, form);
		LogUtil.endLog(DebtBorrowExceptionController.class.toString(), DebtBorrowExceptionDefine.DELETEINIT);
		return modelAndView;
	}

	/**
	 * 删除
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(DebtBorrowExceptionDefine.DELETE_ACTION)
	@RequiresPermissions(DebtBorrowExceptionDefine.PERMISSIONS_DELETE)
	public String deleteRecordAction(HttpServletRequest request, RedirectAttributes attr, DebtBorrowExceptionBean form) {
		LogUtil.startLog(UtmController.class.toString(), DebtBorrowExceptionDefine.DELETE_ACTION);
		if(StringUtils.isNotEmpty(form.getBorrowNid())){
			String nid = form.getBorrowNid();
			if(nid!=null && !"".equals(nid)){
				this.borrowExceptionService.deleteBorrowByNid(form.getBorrowNid());
			}else{
				attr.addFlashAttribute(DebtBorrowExceptionDefine.BORROW_FORM, form);
				LogUtil.endLog(UtmController.class.toString(), DebtBorrowExceptionDefine.DELETE_ACTION);
				return "redirect:" + DebtBorrowExceptionDefine.REQUEST_MAPPING + "/" + DebtBorrowExceptionDefine.INIT;
			}
		}
		attr.addFlashAttribute(DebtBorrowExceptionDefine.BORROW_FORM, form);
		LogUtil.endLog(UtmController.class.toString(), DebtBorrowExceptionDefine.DELETE_ACTION);
		return "redirect:" + DebtBorrowExceptionDefine.REQUEST_MAPPING + "/" + DebtBorrowExceptionDefine.INIT;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, DebtBorrowExceptionBean form) {

		// 项目类型
		List<BorrowProjectType> borrowProjectTypeList = this.borrowCommonService.borrowProjectTypeList("");
		modelAndView.addObject("borrowProjectTypeList", borrowProjectTypeList);

		// 还款方式
		List<BorrowStyle> borrowStyleList = this.borrowCommonService.borrowStyleList("");
		modelAndView.addObject("borrowStyleList", borrowStyleList);

		DebtBorrowCommonCustomize corrowCommonCustomize = new DebtBorrowCommonCustomize();
		// 借款编码
		corrowCommonCustomize.setBorrowNidSrch(form.getBorrowNidSrch());
		// 项目名称
		corrowCommonCustomize.setBorrowNameSrch(form.getBorrowNameSrch());
		// 借 款 人
		corrowCommonCustomize.setUsernameSrch(form.getUsernameSrch());
		// 项目状态
		if(form.getStatusSrch()!=null && StringUtils.isNotEmpty(form.getStatusSrch().trim())){
			if(form.getStatusSrch().trim().equals("0")||form.getStatusSrch().trim().equals("10")){
				corrowCommonCustomize.setStatusSrch(form.getStatusSrch());
			}else{
				corrowCommonCustomize.setStatusSrch(null);
			}
		}else{
			corrowCommonCustomize.setStatusSrch(null);
		}
		// 项目类型
		corrowCommonCustomize.setProjectTypeSrch(form.getProjectTypeSrch());
		// 还款方式
		corrowCommonCustomize.setBorrowStyleSrch(form.getBorrowStyleSrch());
		// 放款时间
		corrowCommonCustomize.setRecoverTimeStartSrch(form.getRecoverTimeStartSrch());

		// 添加时间
		corrowCommonCustomize.setTimeStartSrch(form.getTimeStartSrch());
		// 添加时间
		corrowCommonCustomize.setTimeEndSrch(form.getTimeEndSrch());

		corrowCommonCustomize.setSort(form.getSort());
		corrowCommonCustomize.setCol(form.getCol());

		Long count = this.borrowExceptionService.countBorrow(corrowCommonCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			corrowCommonCustomize.setLimitStart(paginator.getOffset());
			corrowCommonCustomize.setLimitEnd(paginator.getLimit());
			List<DebtBorrowCustomize> recordList = this.borrowExceptionService.selectBorrowList(corrowCommonCustomize);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
			BigDecimal sumAccount = this.borrowExceptionService.sumAccount(corrowCommonCustomize);
			modelAndView.addObject("sumAccount", sumAccount);
		}
		modelAndView.addObject(DebtBorrowExceptionDefine.BORROW_FORM, form);
	}
	
	/**
	 * 创建查看已删除borrow数据列表分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createBorrowDeletePage(HttpServletRequest request, ModelAndView modelAndView, DebtBorrowExceptionDeleteSrchBean form) {

		Long count = this.borrowExceptionService.countBorrowDelete(form);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			form.setLimitStart(paginator.getOffset());
			form.setLimitEnd(paginator.getLimit());
			List<DebtBorrowExceptionDeleteBean> recordList = this.borrowExceptionService.selectBorrowDeleteList(form);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}
		modelAndView.addObject(DebtBorrowExceptionDefine.BORROW_DELETE_FORM, form);
	}
}
