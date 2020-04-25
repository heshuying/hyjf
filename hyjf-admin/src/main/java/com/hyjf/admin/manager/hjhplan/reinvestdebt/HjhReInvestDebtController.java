package com.hyjf.admin.manager.hjhplan.reinvestdebt;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.customize.web.hjh.HjhReInvestDebtCustomize;

/**
 * 复投详情
 * 
 * @author HJH
 */
@Controller
@RequestMapping(value = HjhReInvestDebtDefine.REQUEST_MAPPING)
public class HjhReInvestDebtController extends BaseController {

	@Autowired
	private HjhReInvestDebtService hjhReInvestDebtService;

	/**
	 * 分页
	 * 
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modeAndView, HjhReInvestDebtBean form) {

		HjhReInvestDebtCustomize hjhReInvestDebtCustomize = new HjhReInvestDebtCustomize();
		hjhReInvestDebtCustomize.setAssignPlanOrderIdSrch(form.getAssignPlanOrderIdSrch());
		hjhReInvestDebtCustomize.setAssignPlanNidSrch(form.getAssignPlanNidSrch());
		hjhReInvestDebtCustomize.setAssignOrderIdSrch(form.getAssignOrderIdSrch());
		hjhReInvestDebtCustomize.setUserNameSrch(form.getUserNameSrch());
		hjhReInvestDebtCustomize.setCreditUserNameSrch(form.getCreditUserNameSrch());
		hjhReInvestDebtCustomize.setCreditNidSrch(form.getCreditNidSrch());
		hjhReInvestDebtCustomize.setBorrowNidSrch(form.getBorrowNidSrch());
		hjhReInvestDebtCustomize.setAssignTypeSrch(form.getAssignTypeSrch());
		hjhReInvestDebtCustomize.setBorrowStyleSrch(form.getBorrowStyleSrch());
		hjhReInvestDebtCustomize.setPlanNid(form.getPlanNid());
		hjhReInvestDebtCustomize.setDate(form.getDate());

		Integer count = this.hjhReInvestDebtService.queryHjhReInvestDebtCount(hjhReInvestDebtCustomize);
		if (count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			hjhReInvestDebtCustomize.setLimitStart(paginator.getOffset());
			hjhReInvestDebtCustomize.setLimitEnd(paginator.getLimit());

			List<HjhReInvestDebtCustomize> accountDebts = this.hjhReInvestDebtService.queryHjhReInvestDebts(hjhReInvestDebtCustomize);
			form.setPaginator(paginator);
			modeAndView.addObject("recordList",accountDebts);
			modeAndView.addObject(HjhReInvestDebtDefine.REINVESTDEBT_FORM, form);
			//求合计值
			HjhReInvestDebtCustomize total = this.hjhReInvestDebtService.queryReInvestDebtTotal(hjhReInvestDebtCustomize);
			modeAndView.addObject("total", total);
		} else {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			hjhReInvestDebtCustomize.setLimitStart(paginator.getOffset());
			hjhReInvestDebtCustomize.setLimitEnd(paginator.getLimit());
			form.setPaginator(paginator);
			modeAndView.addObject(HjhReInvestDebtDefine.REINVESTDEBT_FORM, form);
		}
	}

	/**
	 * 资金明细 列表
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(HjhReInvestDebtDefine.INIT)
	@RequiresPermissions(HjhReInvestDebtDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, HjhReInvestDebtBean form) {
		LogUtil.startLog(HjhReInvestDebtController.class.toString(), HjhReInvestDebtDefine.INIT);
		ModelAndView modeAndView = new ModelAndView(HjhReInvestDebtDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(HjhReInvestDebtController.class.toString(), HjhReInvestDebtDefine.INIT);
		return modeAndView;
	}
	
	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(HjhReInvestDebtDefine.SEARCH_ACTION)
	@RequiresPermissions(HjhReInvestDebtDefine.PERMISSIONS_SEARCH)
	public ModelAndView searchAction(HttpServletRequest request, HttpServletResponse response, HjhReInvestDebtBean form) {
		LogUtil.startLog(HjhReInvestDebtDefine.class.toString(), HjhReInvestDebtDefine.SEARCH_ACTION);
		ModelAndView modeAndView = new ModelAndView(HjhReInvestDebtDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(HjhReInvestDebtDefine.class.toString(), HjhReInvestDebtDefine.SEARCH_ACTION);
		return modeAndView;
	}

	/**
	 * 导出资金明细列表
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(HjhReInvestDebtDefine.EXPORT_ACTION)
	@RequiresPermissions(HjhReInvestDebtDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, HjhReInvestDebtBean form) throws Exception {
		// 表格sheet名称
		String sheetName = "资金明细";

		HjhReInvestDebtCustomize hjhReInvestDebtCustomize = new HjhReInvestDebtCustomize();
		hjhReInvestDebtCustomize.setAssignPlanOrderIdSrch(form.getAssignPlanOrderIdSrch());
		hjhReInvestDebtCustomize.setAssignPlanNidSrch(form.getAssignPlanNidSrch());
		hjhReInvestDebtCustomize.setAssignOrderIdSrch(form.getAssignOrderIdSrch());
		hjhReInvestDebtCustomize.setUserNameSrch(form.getUserNameSrch());
		hjhReInvestDebtCustomize.setCreditUserNameSrch(form.getCreditUserNameSrch());
		hjhReInvestDebtCustomize.setCreditNidSrch(form.getCreditNidSrch());
		hjhReInvestDebtCustomize.setBorrowNidSrch(form.getBorrowNidSrch());
		hjhReInvestDebtCustomize.setAssignTypeSrch(form.getAssignTypeSrch());
		hjhReInvestDebtCustomize.setBorrowStyleSrch(form.getBorrowStyleSrch());
		hjhReInvestDebtCustomize.setPlanNid(form.getPlanNid());
		hjhReInvestDebtCustomize.setDate(form.getDate());
		// 取得数据
		List<HjhReInvestDebtCustomize> recordList = this.hjhReInvestDebtService.exportReInvestDebts(hjhReInvestDebtCustomize);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + ".xls";

		String[] titles = new String[] { "序号","智投订单号","承接智投编号","承接订单号","承接人","出让人","债权编号","原项目编号","还款方式","承接本金","垫付利息","实际支付金额","承接方式","项目总期数","承接时所在期数","承接时间" };
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();

		// 生成一个表格
		HSSFSheet sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, sheetName + "_第1页");

		if (recordList != null && recordList.size() > 0) {

			int sheetCount = 1;
			int rowNum = 0;

			for (int i = 0; i < recordList.size(); i++) {
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
					HjhReInvestDebtCustomize hjhReInvestDebt = recordList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 计划订单号
					else if (celLength == 1) {
						cell.setCellValue(hjhReInvestDebt.getAssignPlanOrderId());
					}
					// 承接计划编号
					else if (celLength == 2) {
						cell.setCellValue(hjhReInvestDebt.getAssignPlanNid());
					}
					// 承接订单号
					else if (celLength == 3) {
						cell.setCellValue(hjhReInvestDebt.getAssignOrderId());
					}
					// 承接人
					else if (celLength == 4) {
						cell.setCellValue(hjhReInvestDebt.getUserName());
					}
					// 出让人
					else if (celLength == 5) {
						cell.setCellValue(hjhReInvestDebt.getCreditUserName());
					}
					// 债权编号
					else if (celLength == 6) {
						cell.setCellValue(hjhReInvestDebt.getCreditNid());
					}
					// 原项目编号
					else if (celLength == 7) {
						cell.setCellValue(hjhReInvestDebt.getBorrowNid());
					}
					// 还款方式
					else if (celLength == 8) {
						cell.setCellValue(hjhReInvestDebt.getBorrowStyle());
					}
					// 承接本金
					else if (celLength == 9) {
						cell.setCellValue(hjhReInvestDebt.getAssignCapital());
					}
					// 垫付利息
					else if (celLength == 10) {
						cell.setCellValue(hjhReInvestDebt.getAssignInterestAdvance());
					}
					// 实际支付金额
					else if (celLength == 11) {
						cell.setCellValue(hjhReInvestDebt.getAssignPay());
					}
					// 承接方式
					else if (celLength == 12) {
						cell.setCellValue(hjhReInvestDebt.getAssignType());
					}
					// 项目总期数
					else if (celLength == 13) {
						cell.setCellValue(hjhReInvestDebt.getBorrowPeriod());
					}
					// 承接时所在期数
					else if (celLength == 14) {
						cell.setCellValue(hjhReInvestDebt.getAssignPeriod());
					}
					// 承接时间
					else if (celLength == 15) {
						cell.setCellValue(hjhReInvestDebt.getAssignTime());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

	}
}
