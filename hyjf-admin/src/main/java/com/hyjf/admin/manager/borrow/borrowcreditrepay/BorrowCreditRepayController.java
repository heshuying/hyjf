package com.hyjf.admin.manager.borrow.borrowcreditrepay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.borrow.borrowcredit.BorrowCreditBean;
import com.hyjf.admin.manager.borrow.borrowcredit.BorrowCreditDefine;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.JxlExcelUtils;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.CreditRepay;
import com.hyjf.mybatis.model.customize.admin.AdminBorrowCreditRepayCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminBorrowCreditTenderCustomize;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = BorrowCreditRepayDefine.REQUEST_MAPPING)
public class BorrowCreditRepayController extends BaseController {

	@Autowired
	private BorrowCreditRepayService borrowCreditRepayService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowCreditRepayDefine.INIT)
	@RequiresPermissions(BorrowCreditRepayDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, HttpServletResponse response, 
			@ModelAttribute BorrowCreditRepayBean form) {
		LogUtil.startLog(BorrowCreditRepayController.class.toString(), BorrowCreditRepayDefine.INIT);
		ModelAndView modeAndView = new ModelAndView(BorrowCreditRepayDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modeAndView, form, null);
		LogUtil.endLog(BorrowCreditRepayController.class.toString(), BorrowCreditRepayDefine.INIT);
		return modeAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowCreditRepayDefine.SEARCH_ACTION)
	@RequiresPermissions(BorrowCreditRepayDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, HttpServletResponse response, BorrowCreditRepayBean form) {
		LogUtil.startLog(BorrowCreditRepayController.class.toString(), BorrowCreditRepayDefine.SEARCH_ACTION);
		ModelAndView modeAndView = new ModelAndView(BorrowCreditRepayDefine.LIST_PATH);
		String export = request.getParameter("export");
		// 创建分页
		this.createPage(request, modeAndView, form, export);
		if (export != null && !export.equals("") && !export.equals("undefind")) {
			List<AdminBorrowCreditTenderCustomize> recordList = (List<AdminBorrowCreditTenderCustomize>) modeAndView.getModel().get("recordList");
			if(recordList!=null&&recordList.size()>0){
				for (int i = 0; i < recordList.size(); i++) {
					if (recordList.get(i).getStatus().equals("0") || recordList.get(i).getStatus().equals("00")) {
						recordList.get(i).setStatus("还款中");
					} else {
						recordList.get(i).setStatus("已还款");
					}
					recordList.get(i).setCreditNid("HZR" + recordList.get(i).getCreditNid());
				}
			}
			// 导出列表
			/*----------------upd by LSY START------------*/
			String[] keys = new String[] { "承接人", "债转编号", "出让人", "项目编号", "订单号", "应收本金", "应收利息", "应收本息", "已收本息", "还款服务费", "还款状态", "下次还款时间", "债权承接时间" };
			/*----------------upd by LSY END------------*/
			String tmarray = ",userName,creditNid,creditUserName,bidNid,assignNid," + "assignCapital,assignInterest,assignAccount,assignRepayAccount,creditFee,status,addTime,assignRepayNextTime,";
			JxlExcelUtils.exportexcle(response, "还款信息列表", recordList, "work", keys, AdminBorrowCreditTenderCustomize.class, null, tmarray);
			return null;
		}
		LogUtil.endLog(BorrowCreditRepayController.class.toString(), BorrowCreditRepayDefine.SEARCH_ACTION);
		return modeAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modeAndView, BorrowCreditRepayBean borrowCreditRepayBean, String export) {

		/*--
		Map<String, Object> params = new HashMap<String, Object>();
		// 用户名
		params.put("userName", request.getParameter("userName"));// 承接人ID
		params.put("creditUserName", request.getParameter("creditUserName"));// 出让人ID
		params.put("creditNid", StringUtils.isNotEmpty(request.getParameter("creditNid")) ? request.getParameter("creditNid").replace("HZR", "") : null);// 债转编号
		params.put("status", StringUtils.isNotEmpty(request.getParameter("status")) ? request.getParameter("status") : null);// 还款状态
		params.put("bidNid", request.getParameter("bidNid"));// 项目编号
		params.put("assignNid", request.getParameter("assignNid"));// 订单号
		params.put("assignRepayNextTimeStart",StringUtils.isNotBlank(request.getParameter("assignRepayNextTimeStart"))?request.getParameter("assignRepayNextTimeStart"):null );//下次还款时间
		params.put("assignRepayNextTimeEnd",StringUtils.isNotBlank(request.getParameter("assignRepayNextTimeEnd"))?request.getParameter("assignRepayNextTimeEnd"):null  );//下次还款时间
		params.put("paginatorPage", StringUtils.isNotEmpty(request.getParameter("paginatorPage")) ? request.getParameter("paginatorPage") : "1");// 债权承接时间
		params.put("addTimeStart", StringUtils.isNotBlank(request.getParameter("addTimeStart"))?request.getParameter("addTimeStart"):null);// 债权承接时间
		params.put("addTimeEnd",StringUtils.isNotBlank(request.getParameter("addTimeEnd"))?request.getParameter("addTimeEnd"):null);// 债权承接时间
		--*/
		AdminBorrowCreditTenderCustomize adminBorrowCreditTenderCustomize = new AdminBorrowCreditTenderCustomize();
		// 承接人ID
		adminBorrowCreditTenderCustomize.setUserNameSrch(borrowCreditRepayBean.getUsernameSrch());
		// 出让人ID
		adminBorrowCreditTenderCustomize.setCreditUserNameSrch(borrowCreditRepayBean.getCreditUserNameSrch());
		// 债转编号
		adminBorrowCreditTenderCustomize.setCreditNidSrch(StringUtils.isNotEmpty(borrowCreditRepayBean.getCreditNidSrch()) ? borrowCreditRepayBean.getCreditNidSrch().replace("HZR", "") : null);
		// 还款状态
		adminBorrowCreditTenderCustomize.setStatusSrch(StringUtils.isNotEmpty(borrowCreditRepayBean.getStatusSrch()) ? borrowCreditRepayBean.getStatusSrch() : null);
		// 项目编号
		adminBorrowCreditTenderCustomize.setBidNidSrch(borrowCreditRepayBean.getBidNidSrch());
		// 订单号
		adminBorrowCreditTenderCustomize.setAssignNidSrch(borrowCreditRepayBean.getAssignNidSrch());
		//下次还款时间
		adminBorrowCreditTenderCustomize.setAssignRepayNextTimeStartSrch(StringUtils.isNotBlank(borrowCreditRepayBean.getAssignRepayNextTimeStartSrch())?borrowCreditRepayBean.getAssignRepayNextTimeStartSrch():null);
		//下次还款时间
		adminBorrowCreditTenderCustomize.setAssignRepayNextTimeEndSrch(StringUtils.isNotBlank(borrowCreditRepayBean.getAssignRepayNextTimeEndSrch())?borrowCreditRepayBean.getAssignRepayNextTimeEndSrch():null);
		// 债权承接时间
		adminBorrowCreditTenderCustomize.setAddTimeStartSrch(StringUtils.isNotBlank(borrowCreditRepayBean.getAddTimeStartSrch())?borrowCreditRepayBean.getAddTimeStartSrch():null);
		// 债权承接时间
		adminBorrowCreditTenderCustomize.setAddTimeEndSrch(StringUtils.isNotBlank(borrowCreditRepayBean.getAddTimeEndSrch())?borrowCreditRepayBean.getAddTimeEndSrch():null);
		
		Integer count = this.borrowCreditRepayService.countCreditTender(adminBorrowCreditTenderCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(borrowCreditRepayBean.getPaginatorPage(), count);
			//params.put("limitStart", paginator.getOffset());
			adminBorrowCreditTenderCustomize.setLimitStart(paginator.getOffset());
			if (export != null && !export.equals("") && !export.equals("undefind")) {
				//params.put("limitEnd", 9999999);
				adminBorrowCreditTenderCustomize.setLimitEnd(9999999);
			} else {
				//params.put("limitEnd", paginator.getLimit());
				adminBorrowCreditTenderCustomize.setLimitEnd(paginator.getLimit());
			}
			List<AdminBorrowCreditTenderCustomize> recordList = this.borrowCreditRepayService.selectCreditTender(adminBorrowCreditTenderCustomize);
			//params.put("paginator", paginator);
			borrowCreditRepayBean.setPaginator(paginator);
			modeAndView.addObject("recordList", recordList);
			/*--------add by LSY START----------*/
			AdminBorrowCreditTenderCustomize sumCreditTender = this.borrowCreditRepayService.sumCreditTender(adminBorrowCreditTenderCustomize);
			modeAndView.addObject("sumCreditTender", sumCreditTender);
			/*--------add by LSY END----------*/
		}
		modeAndView.addObject(BorrowCreditRepayDefine.BORROW_FORM, borrowCreditRepayBean);
	}

	/**
	 * 债转还款计划 详细画面
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = BorrowCreditRepayDefine.INFO_ACTION)
	@RequiresPermissions(BorrowCreditRepayDefine.PERMISSIONS_INFO)
	public ModelAndView repayInfoAction(HttpServletRequest request, HttpServletResponse response, BorrowCreditRepayBean form) {
		LogUtil.startLog(BorrowCreditRepayController.class.toString(), BorrowCreditRepayDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(BorrowCreditRepayDefine.INFO_PATH);
		//String export = request.getParameter("export");
		// 创建分页
		//this.createInfoPage(request, modelAndView, export);
		form.setAssignNidSrch(form.getAssignNid());
		this.createInfoPage(request, modelAndView, form);
		/* 分页没有导出功能
		if (export != null && !export.equals("") && !export.equals("undefind")) {
			List<AdminBorrowCreditRepayCustomize> recordList = (List<AdminBorrowCreditRepayCustomize>) modelAndView.getModel().get("recordList");
			// 导出列表
			//----------upd by LSY START------------------//
			//String[] keys = new String[] { "项目编号", "还款方式", "项目名称", "还款期数", "待收本金", "待收利息", "待收本息", "管理费", "还款状态0还款中1已还款", "应还日期" };
			String[] keys = new String[] { "项目编号", "还款方式", "项目名称", "还款期数", "待收本金", "待收利息", "待收本息", "还款服务费", "还款状态0还款中1已还款", "应还日期" };
			//----------upd by LSY END------------------//
			// 包含的内部实体
			Class[] contantsClass = new Class[] { CreditRepay.class, Borrow.class };
			// 想要的字段 全要 是null
			String tmarray = "borrowNid,borrowStyle,name,recoverPeriod,assignCapital,assignInterest,assignAccount,manageFee,status,addip";
			JxlExcelUtils.exportexcle(response, "还款信息详情列表", recordList, "work", keys, AdminBorrowCreditRepayCustomize.class, contantsClass, tmarray);
			return null;
		}
		*/
		LogUtil.endLog(BorrowCreditRepayController.class.toString(), BorrowCreditRepayDefine.INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 债转还款计划 创建分页机能
	 * 
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	private void createInfoPage(HttpServletRequest request, ModelAndView modeAndView, BorrowCreditRepayBean borrowCreditRepayBean) {

		/*
		Map<String, Object> params = new HashMap<String, Object>();
		// 用户名
		params.put("assignNid", request.getParameter("assignNid"));// 承接人ID
		params.put("paginatorPage", request.getParameter("paginatorPage"));
		*/
		
		AdminBorrowCreditTenderCustomize adminBorrowCreditTenderCustomize = new AdminBorrowCreditTenderCustomize();
		adminBorrowCreditTenderCustomize.setAssignNidSrch(borrowCreditRepayBean.getAssignNidSrch());
		
		Integer count = this.borrowCreditRepayService.countBorrowCreditRepayInfoList(adminBorrowCreditTenderCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(borrowCreditRepayBean.getInfopaginatorPage(), count);
			/*
			params.put("limitStart", paginator.getOffset());
			if (export != null && !export.equals("") && !export.equals("undefind")) {
				params.put("limitEnd", 9999999);
			} else {
				params.put("limitEnd", paginator.getLimit());
			}
			*/
			adminBorrowCreditTenderCustomize.setLimitStart(paginator.getOffset());
			adminBorrowCreditTenderCustomize.setLimitEnd(paginator.getLimit());
			List<AdminBorrowCreditRepayCustomize> recordList = this.borrowCreditRepayService.selectBorrowCreditRepayInfoList(adminBorrowCreditTenderCustomize);
			//params.put("paginator", paginator);
			borrowCreditRepayBean.setInfopaginator(paginator);
			modeAndView.addObject("recordList", recordList);
			/*--------add by LSY START-------------*/
			AdminBorrowCreditRepayCustomize sumObject = this.borrowCreditRepayService.sumCreditRepay(adminBorrowCreditTenderCustomize);
			modeAndView.addObject("sumObject", sumObject);
			/*--------add by LSY END-------------*/
		}
		modeAndView.addObject(BorrowCreditRepayDefine.BORROW_FORM, borrowCreditRepayBean);

	}

}
