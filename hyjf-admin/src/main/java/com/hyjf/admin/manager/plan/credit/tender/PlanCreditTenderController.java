package com.hyjf.admin.manager.plan.credit.tender;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.borrow.borrow.BorrowController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.customize.admin.htj.DebtCreditTenderCustomize;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = PlanCreditTenderDefine.REQUEST_MAPPING)
public class PlanCreditTenderController extends BaseController {

	@Autowired
	private PlanCreditTenderService planCreditTenderService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PlanCreditTenderDefine.INIT)
	@RequiresPermissions(PlanCreditTenderDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, HttpServletResponse response,@ModelAttribute PlanCreditTenderBean form) {
		LogUtil.startLog(PlanCreditTenderDefine.CONTROLLER_NAME, PlanCreditTenderDefine.INIT);
		ModelAndView modeAndView = new ModelAndView(PlanCreditTenderDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(PlanCreditTenderDefine.CONTROLLER_NAME, PlanCreditTenderDefine.INIT);
		return modeAndView;
	}

	/**
	 * 查询
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PlanCreditTenderDefine.SEARCH_ACTION)
	@RequiresPermissions(PlanCreditTenderDefine.PERMISSIONS_SEARCH)
	public ModelAndView searchAction(HttpServletRequest request, HttpServletResponse response,@ModelAttribute PlanCreditTenderBean form) {
		LogUtil.startLog(PlanCreditTenderDefine.CONTROLLER_NAME, PlanCreditTenderDefine.SEARCH_ACTION);
		ModelAndView modeAndView = new ModelAndView(PlanCreditTenderDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(PlanCreditTenderDefine.CONTROLLER_NAME, PlanCreditTenderDefine.SEARCH_ACTION);
		return modeAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, PlanCreditTenderBean form) {

		// 还款方式
		List<BorrowStyle> borrowStyleList = this.planCreditTenderService.selectBorrowStyleList("");
		modelAndView.addObject("borrowStyleList", borrowStyleList);
		// 承接方式
		modelAndView.addObject("assignTypeList",this.planCreditTenderService.getParamNameList(CustomConstants.CREDIT_ASSIGN_TYPE));
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("assignPlanNid", form.getAssignPlanNid());
		params.put("assignPlanOrderId", form.getAssignPlanOrderId());
		params.put("assignUserName", form.getAssignUserName());
		params.put("creditUserName", form.getCreditUserName());
		params.put("creditNid", form.getCreditNid());
		params.put("borrowNid", form.getBorrowNid());
		params.put("repayStyle", form.getRepayStyle());
		params.put("assignType", form.getAssignType());
		params.put("assignTimeStart", StringUtils.isNotBlank(form.getAssignTimeStart())?form.getAssignTimeStart():null);
		params.put("assignTimeEnd", StringUtils.isNotBlank(form.getAssignTimeEnd())?form.getAssignTimeEnd():null);
		Integer count = this.planCreditTenderService.countDebtCreditTenderList(params);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			params.put("limitStart", paginator.getOffset());
			params.put("limitEnd", paginator.getLimit());
			List<DebtCreditTenderCustomize> recordList = this.planCreditTenderService.selectDebtCreditTenderList(params);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}
		modelAndView.addObject(PlanCreditTenderDefine.FORM, form);
	}


	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(PlanCreditTenderDefine.EXPORT_ACTION)
	@RequiresPermissions(PlanCreditTenderDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, PlanCreditTenderBean form) throws Exception {
		LogUtil.startLog(BorrowController.class.toString(), PlanCreditTenderDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "智投服务承接记录";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("assignPlanNid", form.getAssignPlanNid());
		params.put("assignPlanOrderId", form.getAssignPlanOrderId());
		params.put("assignUserName", form.getAssignUserName());
		params.put("creditUserName", form.getCreditUserName());
		params.put("creditNid", form.getCreditNid());
		params.put("borrowNid", form.getBorrowNid());
		params.put("repayStyle", form.getRepayStyle());
		params.put("assignType", form.getAssignType());
		params.put("assignTimeStart", StringUtils.isNotBlank(form.getAssignTimeStart())?form.getAssignTimeStart():null);
		params.put("assignTimeEnd", StringUtils.isNotBlank(form.getAssignTimeEnd())?form.getAssignTimeEnd():null);
		List<DebtCreditTenderCustomize> resultList = this.planCreditTenderService.selectDebtCreditTenderList(params);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "序号","承接人","承接计划编号","承接计划订单号","出让人","债转编号","原项目编号","还款方式","承接本金","垫付利息","实际支付金额","服务费率","实际服务费"," 承接时间","承接方式","项目总期数","承接时所在期数"};
		
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
					DebtCreditTenderCustomize debtCreditTender = resultList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 承接人
					else if (celLength == 1) {
						cell.setCellValue(debtCreditTender.getAssignOrderId());
					}
					
					// 承接计划编号
					else if (celLength == 2) {
						cell.setCellValue(debtCreditTender.getAssignPlanNid());
					}
					// 承接计划订单号
					else if (celLength == 3) {
						cell.setCellValue(debtCreditTender.getAssignOrderId());
					}
					// 出让人
					else if (celLength == 4) {
						cell.setCellValue(debtCreditTender.getCreditUserName());
					}
					// 债转编号
					else if (celLength == 5) {
						cell.setCellValue(debtCreditTender.getCreditNid());
					}
					// 原项目编号
					else if (celLength == 6) {
						cell.setCellValue(debtCreditTender.getBorrowNid());
					}
					// 还款方式
					else if (celLength == 7) {
						cell.setCellValue(debtCreditTender.getRepayStyleName());
					}
					// 承接本金
					else if (celLength == 8) {
						cell.setCellValue(debtCreditTender.getAssignCapital());
					}
					// 垫付利息
					else if (celLength == 9) {
						cell.setCellValue(debtCreditTender.getAssignInterestAdvance());
					}
					// 实际支付金额
					else if (celLength == 10) {
						cell.setCellValue(debtCreditTender.getAssignPay());
					}
					// 服务费率
					else if (celLength == 11) {
						cell.setCellValue(debtCreditTender.getServiceFee()+"%");
					}
					// 实际服务费
					else if (celLength == 12) {
						cell.setCellValue(debtCreditTender.getServiceFeeRate());
					}
					// 承接时间
					else if (celLength == 13) {
						cell.setCellValue(debtCreditTender.getAssignTime());
					}
					// 承接方式
					else if (celLength == 14) {
						cell.setCellValue(debtCreditTender.getAssignTypeName());
					}
					// 项目总期数
					else if (celLength == 15) {
						cell.setCellValue(debtCreditTender.getBorrowPeriod());
					}
					// 承接时所在期数
					else if (celLength == 16) {
						cell.setCellValue(debtCreditTender.getAssignPeriod());
					}
					
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(BorrowController.class.toString(), PlanCreditTenderDefine.EXPORT_ACTION);
	}

}
