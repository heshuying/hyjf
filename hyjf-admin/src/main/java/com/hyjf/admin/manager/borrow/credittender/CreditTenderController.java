package com.hyjf.admin.manager.borrow.credittender;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.manager.borrow.borrowinvest.BorrowInvestController;
import com.hyjf.admin.manager.borrow.borrowinvest.BorrowInvestDefine;
import com.hyjf.admin.manager.borrow.borrowinvest.InvestorDebtBean;
import com.hyjf.bank.service.fdd.fddgeneratecontract.FddGenerateContractBean;
import com.hyjf.bank.service.fdd.fddgeneratecontract.FddGenerateContractConstant;
import com.hyjf.common.util.*;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.borrow.borrow.BorrowController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.BorrowCreditCustomize;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = CreditTenderDefine.REQUEST_MAPPING)
public class CreditTenderController extends BaseController {

	@Autowired
	private CreditTenderService creditTenderService;

	@Autowired
	@Qualifier("myAmqpTemplate")
	private RabbitTemplate rabbitTemplate;


	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(CreditTenderDefine.INIT)
	@RequiresPermissions(CreditTenderDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, HttpServletResponse response,@ModelAttribute CreditTenderBean form) {
		LogUtil.startLog(CreditTenderController.class.toString(), CreditTenderDefine.INIT);
		ModelAndView modeAndView = new ModelAndView(CreditTenderDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(CreditTenderController.class.toString(), CreditTenderDefine.INIT);
		return modeAndView;
	}

	/**
	 * 查询
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(CreditTenderDefine.SEARCH_ACTION)
	@RequiresPermissions(CreditTenderDefine.PERMISSIONS_SEARCH)
	public ModelAndView searchAction(HttpServletRequest request, HttpServletResponse response,@ModelAttribute CreditTenderBean form) {
		LogUtil.startLog(CreditTenderController.class.toString(), CreditTenderDefine.SEARCH_ACTION);
		ModelAndView modeAndView = new ModelAndView(CreditTenderDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(CreditTenderController.class.toString(), CreditTenderDefine.SEARCH_ACTION);
		return modeAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modeAndView, CreditTenderBean form) {

		BorrowCreditCustomize borrowCreditCustomize = new BorrowCreditCustomize();
		// 出让人
		borrowCreditCustomize.setCreditUsername(form.getCreditUsernameSrch());
		// 承接人
		borrowCreditCustomize.setUsernameSrch(form.getUsernameSrch());
		// 债转编号
		borrowCreditCustomize.setCreditNidSrch(form.getCreditNidSrch());
		// 项目编号
		borrowCreditCustomize.setBidNidSrch(form.getBidNidSrch());
		// 订单编号
		borrowCreditCustomize.setAssignNid(form.getAssignNidSrch());
		// 时间
		borrowCreditCustomize.setTimeStartSrch(StringUtils.isNotBlank(form.getTimeStartSrch())?form.getTimeStartSrch():null);
		// 时间
		borrowCreditCustomize.setTimeEndSrch(StringUtils.isNotBlank(form.getTimeEndSrch())?form.getTimeEndSrch():null);
		// 客户端
		borrowCreditCustomize.setClient(form.getClient());

		Integer count = this.creditTenderService.countBorrowCreditTenderList(borrowCreditCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			borrowCreditCustomize.setLimitStart(paginator.getOffset());
			borrowCreditCustomize.setLimitEnd(paginator.getLimit());
			List<BorrowCreditCustomize> recordList = this.creditTenderService.selectBorrowCreditTenderList(borrowCreditCustomize);
			form.setPaginator(paginator);
			modeAndView.addObject("recordList", recordList);
			/*-------add by LSY START---------*/
			BorrowCreditCustomize sumBorrowCreditInfo = this.creditTenderService.sumBorrowCreditInfo(borrowCreditCustomize);
			modeAndView.addObject("sumBorrowCreditInfo", sumBorrowCreditInfo);
			/*-------add by LSY END---------*/
		}
		modeAndView.addObject(CreditTenderDefine.FORM, form);
	}


	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(CreditTenderDefine.ENHANCE_EXPORT_ACTION)
	@RequiresPermissions(value = {CreditTenderDefine.PERMISSIONS_EXPORT, CreditTenderDefine.ENHANCE_PERMISSIONS_EXPORT}, logical = Logical.AND)
	public void enhanceExportAction(HttpServletRequest request, HttpServletResponse response, CreditTenderBean form) throws Exception {
		LogUtil.startLog(CreditTenderController.class.toString(), CreditTenderDefine.ENHANCE_EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "汇转让-承接信息";

		BorrowCreditCustomize borrowCreditCustomize = new BorrowCreditCustomize();
		// 出让人
		borrowCreditCustomize.setCreditUsername(form.getCreditUsernameSrch());
		// 承接人
		borrowCreditCustomize.setUsernameSrch(form.getUsernameSrch());
		// 债转编号
		borrowCreditCustomize.setCreditNidSrch(form.getCreditNidSrch());
		// 项目编号
		borrowCreditCustomize.setBidNidSrch(form.getBidNidSrch());
		// 订单编号
		borrowCreditCustomize.setAssignNid(form.getAssignNidSrch());
		// 时间
		borrowCreditCustomize.setTimeStartSrch(StringUtils.isNotBlank(form.getTimeStartSrch())?form.getTimeStartSrch():null);
		// 时间
		borrowCreditCustomize.setTimeEndSrch(StringUtils.isNotBlank(form.getTimeEndSrch())?form.getTimeEndSrch():null);
		// 客户端
		borrowCreditCustomize.setClient(form.getClient());
		
		List<BorrowCreditCustomize> resultList = this.creditTenderService.selectBorrowCreditTenderList(borrowCreditCustomize);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		/*----------upd by LSY START-----------------*/
		//String[] titles = new String[] { "序号","订单号","债转编号","项目编号","出让人","出让人当前的推荐人的用户名","出让人当前的推荐人的用户属性","出让人当前的推荐人的分公司","出让人当前的推荐人的部门","出让人当前的推荐人的团队","出让人承接时的推荐人的用户名", "出让人承接时的推荐人的用户属性", "出让人承接时的推荐人的分公司", "出让人承接时的推荐人的部门", "出让人承接时的推荐人的团队", "承接人","承接人当前的推荐人的用户名","承接人当前的推荐人的用户属性","承接人当前的推荐人的分公司","承接人当前的推荐人的部门","承接人当前的推荐人的团队","承接人承接时的推荐人的用户名", "承接人承接时的推荐人的用户属性", "承接人承接时的推荐人的分公司", "承接人承接时的推荐人的部门", "承接人承接时的推荐人的团队", "承接本金","折让率","认购价格","垫付利息", "服务费", "实付金额","承接平台", "承接时间" };
		String[] titles = new String[] { "序号","订单号","债转编号","项目编号","出让人","出让人当前的推荐人的用户名","出让人当前的推荐人的用户属性","出让人当前的推荐人的分公司","出让人当前的推荐人的部门","出让人当前的推荐人的团队","出让人承接时的推荐人的用户名", "出让人承接时的推荐人的用户属性", "出让人承接时的推荐人的分公司", "出让人承接时的推荐人的部门", "出让人承接时的推荐人的团队", "承接人","承接人当前的推荐人的用户名","承接人当前的推荐人的用户属性","承接人当前的推荐人的分公司","承接人当前的推荐人的部门","承接人当前的推荐人的团队","承接人承接时的推荐人的用户名", "承接人承接时的推荐人的用户属性", "承接人承接时的推荐人的分公司", "承接人承接时的推荐人的部门", "承接人承接时的推荐人的团队", "承接本金","折让率","认购价格","垫付利息", "债转服务费", "实付金额","承接平台", "承接时间" };
		/*----------upd by LSY END-----------------*/
		
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();

		// 生成一个表格
		HSSFSheet sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, sheetName + "_第1页");

		if (resultList != null && resultList.size() > 0) {
			int sheetCount = 1;
			int rowNum = 0;
			for (int i = 0; i < resultList.size(); i++) {
				rowNum++;
				if (i != 0 && i % 60000 == 0) {
					sheetCount++;
					sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, (sheetName + "_第" + sheetCount + "页"));
					rowNum = 1;
				}
				// 新建一行
				Row row = sheet.createRow(rowNum);
				// 循环数据
				for (int celLength = 0; celLength < titles.length; celLength++) {
					BorrowCreditCustomize borrowCommonCustomize = resultList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 订单号
					else if (celLength == 1) {
						cell.setCellValue(borrowCommonCustomize.getAssignNid());
					}
					// 债转编号
					else if (celLength == 2) {
						cell.setCellValue(borrowCommonCustomize.getCreditNid());
					}
					// 项目编号
					else if (celLength == 3) {
						cell.setCellValue(borrowCommonCustomize.getBidNid());
					}
					// 出让人用户名
					else if (celLength == 4) {
						cell.setCellValue(borrowCommonCustomize.getCreditUsername());
					}
					// 出让人推荐人信息---start
					// 出让人当前
					else if (celLength == 5) {
						if(StringUtils.isNotBlank(borrowCommonCustomize.getRecommendAttrCreditSelf()) && (borrowCommonCustomize.getRecommendAttrCreditSelf().equals("线上员工") || borrowCommonCustomize.getRecommendAttrCreditSelf().equals("线下员工"))){
							cell.setCellValue(borrowCommonCustomize.getCreditUsername());
						}else{
							cell.setCellValue(borrowCommonCustomize.getRecommendNameCredit());
						}

					}
					else if (celLength == 6) {
						if(StringUtils.isNotBlank(borrowCommonCustomize.getRecommendAttrCreditSelf()) && (borrowCommonCustomize.getRecommendAttrCreditSelf().equals("线上员工") || borrowCommonCustomize.getRecommendAttrCreditSelf().equals("线下员工"))){
							cell.setCellValue(borrowCommonCustomize.getRecommendAttrCreditSelf());
						}else{
							cell.setCellValue(borrowCommonCustomize.getRecommendAttrCredit());
						}

					}
					else if (celLength == 7) {
						if(StringUtils.isNotBlank(borrowCommonCustomize.getRecommendAttrCreditSelf()) && (borrowCommonCustomize.getRecommendAttrCreditSelf().equals("线上员工") || borrowCommonCustomize.getRecommendAttrCreditSelf().equals("线下员工"))){
							cell.setCellValue(borrowCommonCustomize.getRegionNameCreditSelf());
						}else{
							cell.setCellValue(borrowCommonCustomize.getRegionNameCredit());
						}

					}
					else if (celLength == 8) {
						if(StringUtils.isNotBlank(borrowCommonCustomize.getRecommendAttrCreditSelf()) && (borrowCommonCustomize.getRecommendAttrCreditSelf().equals("线上员工") || borrowCommonCustomize.getRecommendAttrCreditSelf().equals("线下员工"))){
							cell.setCellValue(borrowCommonCustomize.getBranchNameCreditSelf());
						}else{
							cell.setCellValue(borrowCommonCustomize.getBranchNameCredit());
						}

					}
					else if (celLength == 9) {
						if(StringUtils.isNotBlank(borrowCommonCustomize.getRecommendAttrCreditSelf()) && (borrowCommonCustomize.getRecommendAttrCreditSelf().equals("线上员工") || borrowCommonCustomize.getRecommendAttrCreditSelf().equals("线下员工"))){
							cell.setCellValue(borrowCommonCustomize.getDepartmentNameCreditSelf());
						}else{
							cell.setCellValue(borrowCommonCustomize.getDepartmentNameCredit());
						}

					}

					//出让人承接
					else if (celLength == 10) {
						cell.setCellValue(borrowCommonCustomize.getInviteUserCreditName());
					}
					else if (celLength == 11) {
						cell.setCellValue(borrowCommonCustomize.getInviteUserCreditAttribute());
					}
					else if (celLength == 12) {
						cell.setCellValue(borrowCommonCustomize.getInviteUserCreditRegionName());
					}
					else if (celLength == 13) {
						cell.setCellValue(borrowCommonCustomize.getInviteUserCreditBranchName());
					}
					else if (celLength == 14) {
						cell.setCellValue(borrowCommonCustomize.getInviteUserCreditDepartmentName());
					}
					// 出让人推荐人信息---end
					// 承接人用户名
					else if (celLength == 15) {
						cell.setCellValue(borrowCommonCustomize.getUsername());
					}
					// 承接人推荐人信息---start
					// 承接人当前
					else if (celLength == 16) {
						if(StringUtils.isNotBlank(borrowCommonCustomize.getRecommendAttrSelf()) && (borrowCommonCustomize.getRecommendAttrSelf().equals("线上员工") || borrowCommonCustomize.getRecommendAttrSelf().equals("线下员工"))){
							cell.setCellValue(borrowCommonCustomize.getUsername());
						}else{
							cell.setCellValue(borrowCommonCustomize.getRecommendName());
						}

					}
					else if (celLength == 17) {
						if(StringUtils.isNotBlank(borrowCommonCustomize.getRecommendAttrSelf()) && (borrowCommonCustomize.getRecommendAttrSelf().equals("线上员工") || borrowCommonCustomize.getRecommendAttrSelf().equals("线下员工"))){
							cell.setCellValue(borrowCommonCustomize.getRecommendAttrSelf());
						}else{
							cell.setCellValue(borrowCommonCustomize.getRecommendAttr());
						}

					}
					else if (celLength == 18) {
						if(StringUtils.isNotBlank(borrowCommonCustomize.getRecommendAttrSelf()) && (borrowCommonCustomize.getRecommendAttrSelf().equals("线上员工") || borrowCommonCustomize.getRecommendAttrSelf().equals("线下员工"))){
							cell.setCellValue(borrowCommonCustomize.getRegionNameSelf());
						}else{
							cell.setCellValue(borrowCommonCustomize.getRegionName());
						}

					}
					else if (celLength == 19) {
						if(StringUtils.isNotBlank(borrowCommonCustomize.getRecommendAttrSelf()) && (borrowCommonCustomize.getRecommendAttrSelf().equals("线上员工") || borrowCommonCustomize.getRecommendAttrSelf().equals("线下员工"))){
							cell.setCellValue(borrowCommonCustomize.getBranchNameSelf());
						}else{
							cell.setCellValue(borrowCommonCustomize.getBranchName());
						}

					}
					else if (celLength == 20) {
						if(StringUtils.isNotBlank(borrowCommonCustomize.getRecommendAttrSelf()) && (borrowCommonCustomize.getRecommendAttrSelf().equals("线上员工") || borrowCommonCustomize.getRecommendAttrSelf().equals("线下员工"))){
							cell.setCellValue(borrowCommonCustomize.getDepartmentNameSelf());
						}else{
							cell.setCellValue(borrowCommonCustomize.getDepartmentName());
						}

					}

					// 承接人承接时
					else if (celLength == 21) {
						cell.setCellValue(borrowCommonCustomize.getInviteUserName());
					}
					else if (celLength == 22) {
						cell.setCellValue(borrowCommonCustomize.getInviteUserAttribute());
					}
					else if (celLength == 23) {
						cell.setCellValue(borrowCommonCustomize.getInviteUseRegionname());
					}
					else if (celLength == 24) {
						cell.setCellValue(borrowCommonCustomize.getInviteUserBranchname());
					}
					else if (celLength == 25) {
						cell.setCellValue(borrowCommonCustomize.getInviteUserDepartmentName());
					}
					// 承接人承接时推荐人信息---end
					// 承接本金
					else if (celLength == 26) {
						cell.setCellValue(borrowCommonCustomize.getAssignCapital());
					}
					// 折让率
					else if (celLength == 27) {
						cell.setCellValue(borrowCommonCustomize.getCreditDiscount());
					}
					// 认购价格
					else if (celLength == 28) {
						cell.setCellValue(borrowCommonCustomize.getAssignPrice());
					}
					// 垫付利息
					else if (celLength == 29) {
						cell.setCellValue(borrowCommonCustomize.getAssignInterestAdvance());
					}
					// 服务费
					else if (celLength == 30) {
						cell.setCellValue(borrowCommonCustomize.getCreditFee());
					}
					// 实付金额
					else if (celLength == 31) {
						cell.setCellValue(borrowCommonCustomize.getAssignPay());
					}
					// 客户端
					else if (celLength == 32) {
						 if (borrowCommonCustomize.getClient()!=null&&!borrowCommonCustomize.getClient().equals("")
								&&borrowCommonCustomize.getClient().equals("0")) {
							cell.setCellValue("pc");
						}else if (borrowCommonCustomize.getClient()!=null&&!borrowCommonCustomize.getClient().equals("")
								&&borrowCommonCustomize.getClient().equals("1")) {
							cell.setCellValue("微信");
						}else if (borrowCommonCustomize.getClient()!=null&&!borrowCommonCustomize.getClient().equals("")
								&&borrowCommonCustomize.getClient().equals("2")) {
							cell.setCellValue("android");
						}else if (borrowCommonCustomize.getClient()!=null&&!borrowCommonCustomize.getClient().equals("")
								&&borrowCommonCustomize.getClient().equals("3")) {
							cell.setCellValue("ios");
						}
					}
					// 承接时间
					else if (celLength == 33) {
						cell.setCellValue(borrowCommonCustomize.getAddTime());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(CreditTenderController.class.toString(), CreditTenderDefine.ENHANCE_EXPORT_ACTION);
	}

	@RequestMapping(CreditTenderDefine.EXPORT_ACTION)
	@RequiresPermissions(CreditTenderDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, CreditTenderBean form) throws Exception {
		LogUtil.startLog(CreditTenderController.class.toString(), CreditTenderDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "汇转让-承接信息";

		BorrowCreditCustomize borrowCreditCustomize = new BorrowCreditCustomize();
		// 出让人
		borrowCreditCustomize.setCreditUsername(form.getCreditUsernameSrch());
		// 承接人
		borrowCreditCustomize.setUsernameSrch(form.getUsernameSrch());
		// 债转编号
		borrowCreditCustomize.setCreditNidSrch(form.getCreditNidSrch());
		// 项目编号
		borrowCreditCustomize.setBidNidSrch(form.getBidNidSrch());
		// 订单编号
		borrowCreditCustomize.setAssignNid(form.getAssignNidSrch());
		// 时间
		borrowCreditCustomize.setTimeStartSrch(StringUtils.isNotBlank(form.getTimeStartSrch())?form.getTimeStartSrch():null);
		// 时间
		borrowCreditCustomize.setTimeEndSrch(StringUtils.isNotBlank(form.getTimeEndSrch())?form.getTimeEndSrch():null);
		// 客户端
		borrowCreditCustomize.setClient(form.getClient());

		List<BorrowCreditCustomize> resultList = this.creditTenderService.selectBorrowCreditTenderList(borrowCreditCustomize);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		/*----------upd by LSY START-----------------*/
		String[] titles = new String[] { "序号","订单号","债转编号","项目编号","出让人","出让人当前的推荐人的用户名","出让人当前的推荐人的用户属性","出让人承接时的推荐人的用户名", "出让人承接时的推荐人的用户属性", "承接人","承接人当前的推荐人的用户名","承接人当前的推荐人的用户属性",
				"承接人承接时的推荐人的用户名", "承接人承接时的推荐人的用户属性", "承接本金","折让率","认购价格","垫付利息", "债转服务费", "实付金额","承接平台", "承接时间" };
		/*----------upd by LSY END-----------------*/

		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();

		// 生成一个表格
		HSSFSheet sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, sheetName + "_第1页");

		if (resultList != null && resultList.size() > 0) {
			int sheetCount = 1;
			int rowNum = 0;
			for (int i = 0; i < resultList.size(); i++) {
				rowNum++;
				if (i != 0 && i % 60000 == 0) {
					sheetCount++;
					sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, (sheetName + "_第" + sheetCount + "页"));
					rowNum = 1;
				}
				// 新建一行
				Row row = sheet.createRow(rowNum);
				// 循环数据
				for (int celLength = 0; celLength < titles.length; celLength++) {
					BorrowCreditCustomize borrowCommonCustomize = resultList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 订单号
					else if (celLength == 1) {
						cell.setCellValue(borrowCommonCustomize.getAssignNid());
					}
					// 债转编号
					else if (celLength == 2) {
						cell.setCellValue(borrowCommonCustomize.getCreditNid());
					}
					// 项目编号
					else if (celLength == 3) {
						cell.setCellValue(borrowCommonCustomize.getBidNid());
					}
					// 出让人用户名
					else if (celLength == 4) {
						cell.setCellValue(borrowCommonCustomize.getCreditUsername());
					}
					// 出让人推荐人信息---start
					// 出让人当前
					else if (celLength == 5) {
						if(StringUtils.isNotBlank(borrowCommonCustomize.getRecommendAttrCreditSelf()) && (borrowCommonCustomize.getRecommendAttrCreditSelf().equals("线上员工") || borrowCommonCustomize.getRecommendAttrCreditSelf().equals("线下员工"))){
							cell.setCellValue(borrowCommonCustomize.getCreditUsername());
						}else{
							cell.setCellValue(borrowCommonCustomize.getRecommendNameCredit());
						}

					}
					else if (celLength == 6) {
						if(StringUtils.isNotBlank(borrowCommonCustomize.getRecommendAttrCreditSelf()) && (borrowCommonCustomize.getRecommendAttrCreditSelf().equals("线上员工") || borrowCommonCustomize.getRecommendAttrCreditSelf().equals("线下员工"))){
							cell.setCellValue(borrowCommonCustomize.getRecommendAttrCreditSelf());
						}else{
							cell.setCellValue(borrowCommonCustomize.getRecommendAttrCredit());
						}

					}
					//出让人承接
					else if (celLength == 7) {
						cell.setCellValue(borrowCommonCustomize.getInviteUserCreditName());
					}
					else if (celLength == 8) {
						cell.setCellValue(borrowCommonCustomize.getInviteUserCreditAttribute());
					}
					// 出让人推荐人信息---end
					// 承接人用户名
					else if (celLength == 9) {
						cell.setCellValue(borrowCommonCustomize.getUsername());
					}
					// 承接人推荐人信息---start
					// 承接人当前
					else if (celLength == 10) {
						if(StringUtils.isNotBlank(borrowCommonCustomize.getRecommendAttrSelf()) && (borrowCommonCustomize.getRecommendAttrSelf().equals("线上员工") || borrowCommonCustomize.getRecommendAttrSelf().equals("线下员工"))){
							cell.setCellValue(borrowCommonCustomize.getUsername());
						}else{
							cell.setCellValue(borrowCommonCustomize.getRecommendName());
						}

					}
					else if (celLength == 11) {
						if(StringUtils.isNotBlank(borrowCommonCustomize.getRecommendAttrSelf()) && (borrowCommonCustomize.getRecommendAttrSelf().equals("线上员工") || borrowCommonCustomize.getRecommendAttrSelf().equals("线下员工"))){
							cell.setCellValue(borrowCommonCustomize.getRecommendAttrSelf());
						}else{
							cell.setCellValue(borrowCommonCustomize.getRecommendAttr());
						}

					}

					// 承接人承接时
					else if (celLength == 12) {
						cell.setCellValue(borrowCommonCustomize.getInviteUserName());
					}
					else if (celLength == 13) {
						cell.setCellValue(borrowCommonCustomize.getInviteUserAttribute());
					}
					// 承接人承接时推荐人信息---end
					// 承接本金
					else if (celLength == 14) {
						cell.setCellValue(borrowCommonCustomize.getAssignCapital());
					}
					// 折让率
					else if (celLength == 15) {
						cell.setCellValue(borrowCommonCustomize.getCreditDiscount());
					}
					// 认购价格
					else if (celLength == 16) {
						cell.setCellValue(borrowCommonCustomize.getAssignPrice());
					}
					// 垫付利息
					else if (celLength == 17) {
						cell.setCellValue(borrowCommonCustomize.getAssignInterestAdvance());
					}
					// 服务费
					else if (celLength == 18) {
						cell.setCellValue(borrowCommonCustomize.getCreditFee());
					}
					// 实付金额
					else if (celLength == 19) {
						cell.setCellValue(borrowCommonCustomize.getAssignPay());
					}
					// 客户端
					else if (celLength == 20) {
						if (borrowCommonCustomize.getClient()!=null&&!borrowCommonCustomize.getClient().equals("")
								&&borrowCommonCustomize.getClient().equals("0")) {
							cell.setCellValue("pc");
						}else if (borrowCommonCustomize.getClient()!=null&&!borrowCommonCustomize.getClient().equals("")
								&&borrowCommonCustomize.getClient().equals("1")) {
							cell.setCellValue("微信");
						}else if (borrowCommonCustomize.getClient()!=null&&!borrowCommonCustomize.getClient().equals("")
								&&borrowCommonCustomize.getClient().equals("2")) {
							cell.setCellValue("android");
						}else if (borrowCommonCustomize.getClient()!=null&&!borrowCommonCustomize.getClient().equals("")
								&&borrowCommonCustomize.getClient().equals("3")) {
							cell.setCellValue("ios");
						}
					}
					// 承接时间
					else if (celLength == 21) {
						cell.setCellValue(borrowCommonCustomize.getAddTime());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(CreditTenderController.class.toString(), CreditTenderDefine.EXPORT_ACTION);
	}

	/**
	 * PDF脱敏图片预览
	 * @param request
	 * @return
	 */
	@RequestMapping(CreditTenderDefine.PDF_PREVIEW_ACTION)
	@RequiresPermissions(CreditTenderDefine.PERMISSIONS_PDF_PREVIEW)
	public ModelAndView pdfPreviewAction(HttpServletRequest request) {
		LogUtil.startLog(CreditTenderController.class.toString(), CreditTenderDefine.PDF_PREVIEW_ACTION);
		ModelAndView modelAndView = new ModelAndView(CreditTenderDefine.PDF_PREVIEW_PATH);
		String nid = request.getParameter("nid");
		// 根据订单号查询用户出借协议记录表
		TenderAgreement tenderAgreement = this.creditTenderService.selectTenderAgreement(nid);
		if (tenderAgreement != null && StringUtils.isNotBlank(tenderAgreement.getImgUrl())) {
			String imgUrl = tenderAgreement.getImgUrl();
			String[] imgs = imgUrl.split(";");
			List<String> imgList = Arrays.asList(imgs);
			modelAndView.addObject("imgList",imgList);
			// 文件服务器
			String fileDomainUrl = PropUtils.getSystem("hyjf.ftp.url") + PropUtils.getSystem("hyjf.ftp.basepath.img");
			modelAndView.addObject("fileDomainUrl",fileDomainUrl);
		}
		LogUtil.endLog(CreditTenderController.class.toString(), CreditTenderDefine.PDF_PREVIEW_ACTION);
		return modelAndView;
	}


	/**
	 * PDF文件签署
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions(CreditTenderDefine.PERMISSIONS_PDF_SIGN)
	@RequestMapping(value = CreditTenderDefine.PDF_SIGN_ACTION, produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	public String pdfSignAction(HttpServletRequest request) {
		LogUtil.startLog(CreditTenderDefine.class.toString(), CreditTenderDefine.PDF_SIGN_ACTION);
		JSONObject ret = new JSONObject();
		// 用户ID
		String userId = request.getParameter("userId");
		// 标的编号
		String borrowNid = request.getParameter("borrowNid");

		// 承接订单号
		String assignNid = request.getParameter("assignNid");

		// 原始出借订单号
		String creditTenderNid = request.getParameter("creditTenderNid");
		// 债转编号
		String creditNid = request.getParameter("creditNid");

		if (StringUtils.isBlank(userId) || StringUtils.isBlank(borrowNid) || StringUtils.isBlank(assignNid) || StringUtils.isBlank(creditTenderNid) || StringUtils.isBlank(creditNid)) {
			ret.put(CreditTenderDefine.JSON_RESULT_KEY, "参数非法");
			ret.put(CreditTenderDefine.JSON_STATUS_KEY, CreditTenderDefine.JSON_STATUS_NG);
			return ret.toString();
		}
		// 标的信息
		Borrow borrow = this.creditTenderService.getBorrowByNid(borrowNid);
		if (borrow == null){
			ret.put(CreditTenderDefine.JSON_RESULT_KEY, "标的不存在");
			ret.put(CreditTenderDefine.JSON_STATUS_KEY, CreditTenderDefine.JSON_STATUS_NG);
			return ret.toString();
		}
		// 获取承接记录
		CreditTender ct = this.creditTenderService.selectCreditTenderRecord(userId, borrowNid, assignNid, creditTenderNid, creditNid);
		if (ct == null) {
			ret.put(CreditTenderDefine.JSON_RESULT_KEY, "获取承接记录失败");
			ret.put(CreditTenderDefine.JSON_STATUS_KEY, CreditTenderDefine.JSON_STATUS_NG);
			return ret.toString();
		}
		// 承接人
		Users users = this.creditTenderService.getUsersByUserId(Integer.parseInt(userId));
		if (users == null) {
			ret.put(CreditTenderDefine.JSON_RESULT_KEY, "获取用户信息异常");
			ret.put(CreditTenderDefine.JSON_STATUS_KEY, CreditTenderDefine.JSON_STATUS_NG);
			return ret.toString();
		}
		// 根据
		TenderAgreement tenderAgreement = this.creditTenderService.selectTenderAgreement(assignNid);
		if (tenderAgreement != null && tenderAgreement.getStatus() == 2) {
			// PDF脱敏加下载处理发送MQ
			this.creditTenderService.updateSaveSignInfo(tenderAgreement, tenderAgreement.getBorrowNid(), FddGenerateContractConstant.FDD_TRANSTYPE_PLAN_CRIDET, borrow.getInstCode());
		} else {
			FddGenerateContractBean bean = new FddGenerateContractBean();
			bean.setOrdid(assignNid);
			bean.setAssignOrderId(assignNid);
			bean.setCreditNid(creditNid);
			bean.setCreditTenderNid(creditTenderNid);
			bean.setTenderUserId(Integer.parseInt(userId));
			bean.setBorrowNid(borrowNid);
			bean.setTransType(3);
			bean.setTenderType(1);
			this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_GENERATE_CONTRACT, JSONObject.toJSONString(bean));
		}
		ret.put(CreditTenderDefine.JSON_RESULT_KEY, "操作成功,签署MQ已发送");
		ret.put(CreditTenderDefine.JSON_STATUS_KEY, CreditTenderDefine.JSON_STATUS_OK);
		LogUtil.endLog(CreditTenderDefine.class.toString(), CreditTenderDefine.PDF_SIGN_ACTION);
		return ret.toString();
	}


	/**
	 * 出借人债权明细查询
	 * @param request
	 * @return
	 */
	@RequestMapping(CreditTenderDefine.QUERY_INVEST_DEBT_ACTION)
	@RequiresPermissions(CreditTenderDefine.PERMISSIONS_QUERY_INVEST_DEBT_VIEW)
	public ModelAndView queryInvestorDebtAction(HttpServletRequest request) {
		LogUtil.startLog(CreditTenderController.class.toString(), CreditTenderDefine.QUERY_INVEST_DEBT_ACTION);
		ModelAndView modelAndView = new ModelAndView(CreditTenderDefine.QUERY_INVEST_DEBT_PATH);
		// 用户ID
		String userId = request.getParameter("userId");
		// 出借订单号
		String assignNid = request.getParameter("assignNid");
		if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(assignNid)) {
			BankOpenAccount bankOpenAccount = this.creditTenderService.getBankOpenAccount(Integer.parseInt(userId));
			// 电子账户号
			String accountId = bankOpenAccount.getAccount();
			// 调用江西银行查询单笔出借人投标申请接口
			List<BankCallBean> bankCallBean = this.creditTenderService.bidApplyQuery(userId, assignNid, accountId);
			modelAndView.addObject("bankCallBean", bankCallBean);

		}
		LogUtil.endLog(CreditTenderController.class.toString(), CreditTenderDefine.QUERY_INVEST_DEBT_ACTION);
		return modelAndView;
	}
}
