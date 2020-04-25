package com.hyjf.admin.manager.plan.creditdetail;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import com.hyjf.admin.manager.debt.debtborrowcommon.DebtBorrowCommonService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.customize.CreditDetailCustomize;
import com.hyjf.mybatis.model.customize.PlanInvestCustomize;

/**
 * 
 * @author: zhouxiaoshuai
 * @email: 287424494@qq.com
 * @description:计划所用控制器
 * @version: 1
 * @date: 2016年9月12日 下午2:50:16
 */
@Controller
@RequestMapping(value = CreditDetailDefine.REQUEST_MAPPING)
public class CreditDetailController extends BaseController {

	@Autowired
	private CreditDetailService creditDetailService;

	@Autowired
	private DebtBorrowCommonService debtBorrowCommonService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(CreditDetailDefine.INIT)
	@RequiresPermissions(CreditDetailDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute("CreditDetailBean") CreditDetailBean form) {
		LogUtil.startLog(CreditDetailController.class.toString(), CreditDetailDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(CreditDetailDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(CreditDetailController.class.toString(), CreditDetailDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(CreditDetailDefine.SEARCH_ACTION)
	@RequiresPermissions(CreditDetailDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, CreditDetailBean form) {
		LogUtil.startLog(CreditDetailController.class.toString(), CreditDetailDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(CreditDetailDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(CreditDetailController.class.toString(), CreditDetailDefine.INIT);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, CreditDetailBean form) {

		CreditDetailCustomize creditDetailCustomize = new CreditDetailCustomize();
		// 计划编码
		creditDetailCustomize.setPlanNidSrch(form.getPlanNidSrch());
		// 计划订单号
		creditDetailCustomize.setPlanOrderId(form.getPlanOrderId());
		// 出借或者承接订单号
		creditDetailCustomize.setOrderId(form.getOrderId());
		// 用户名
		creditDetailCustomize.setUserName(form.getUserName());
		// 项目编号
		creditDetailCustomize.setBorrowNid(form.getBorrowNidSrch());
		// 原始项目类型
		creditDetailCustomize.setProjectType(form.getProjectTypeSrch());
		// 原始项目类型
		creditDetailCustomize.setProjectType(form.getProjectTypeSrch());
		// 出借/承接时间开始
		creditDetailCustomize.setInvestTimeStart(StringUtils.isNotBlank(form.getInvestTimeStart()) ? form
				.getInvestTimeStart() : null);
		// 出借/承接时间结束
		creditDetailCustomize.setInvestTimeEnd(StringUtils.isNotBlank(form.getInvestTimeEnd()) ? form
				.getInvestTimeEnd() : null);
		// 项目类型
		List<BorrowProjectType> borrowProjectTypeList = this.debtBorrowCommonService.borrowProjectTypeList("");
		modelAndView.addObject("borrowProjectTypeList", borrowProjectTypeList);
		// 债权明细
		Long count = creditDetailService.countPlanInvestNew(creditDetailCustomize);
		if (count > 0) {
			// 债权总额
			HashMap<String, Object> planInvestSumMap = creditDetailService.creditInvestSumMapNew(creditDetailCustomize);
			if (planInvestSumMap != null) {
				modelAndView.addObject("fairValueSum", planInvestSumMap.get("fairValueSum"));
			}
			if (form.getPaginatorPage() == 0) {
				form.setPaginatorPage(1);
			}
			Paginator paginator = new Paginator(form.getPaginatorPage(), count.intValue());
			creditDetailCustomize.setLimitStart(paginator.getOffset());
			creditDetailCustomize.setLimitEnd(paginator.getLimit());
			List<PlanInvestCustomize> debtInvestList = creditDetailService
					.selectPlanInvestListNew(creditDetailCustomize);
			form.setPaginator(paginator);
			modelAndView.addObject("debtInvestList", debtInvestList);
		}
		modelAndView.addObject(CreditDetailDefine.CREDIT_FORM, form);
	}

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(CreditDetailDefine.EXPORT_ACTION)
	@RequiresPermissions(CreditDetailDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, CreditDetailBean form)
			throws Exception {
		LogUtil.startLog(CreditDetailController.class.toString(), CreditDetailDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "债权明细";

		CreditDetailCustomize creditDetailCustomize = new CreditDetailCustomize();
		// 计划编码
		creditDetailCustomize.setPlanNidSrch(form.getPlanNidSrch());
		// 计划订单号
		creditDetailCustomize.setPlanOrderId(form.getPlanOrderId());
		// 出借或者承接订单号
		creditDetailCustomize.setOrderId(form.getOrderId());
		// 用户名
		creditDetailCustomize.setUserName(form.getUserName());
		// 项目编号
		creditDetailCustomize.setBorrowNid(form.getBorrowNidSrch());
		// 原始项目类型
		creditDetailCustomize.setProjectType(form.getProjectTypeSrch());
		// 原始项目类型
		creditDetailCustomize.setProjectType(form.getProjectTypeSrch());
		// 出借/承接时间开始
		creditDetailCustomize.setInvestTimeStart(StringUtils.isNotBlank(form.getInvestTimeStart()) ? form
				.getInvestTimeStart() : null);
		// 出借/承接时间结束
		creditDetailCustomize.setInvestTimeEnd(StringUtils.isNotBlank(form.getInvestTimeEnd()) ? form
				.getInvestTimeEnd() : null);

		List<PlanInvestCustomize> resultList = creditDetailService.selectPlanInvestListNew(creditDetailCustomize);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date())
				+ CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "序号", "智投编号", "智投订单号", "出借/承接订单号", "用户名", "债转编号", "项目编号", "原始项目类型", "出借利率",
				"持有本金", "持有期限", "剩余期限", "公允价值", "出借/承接时间", "延期天数", "逾期天数" };
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
					sheet = ExportExcel
							.createHSSFWorkbookTitle(workbook, titles, (sheetName + "_第" + sheetCount + "页"));
					rowNum = 1;
				}

				// 新建一行
				Row row = sheet.createRow(rowNum);
				// 循环数据
				for (int celLength = 0; celLength < titles.length; celLength++) {
					PlanInvestCustomize planInvestCustomize = resultList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 计划编号
					else if (celLength == 1) {
						cell.setCellValue(planInvestCustomize.getPlanNid());
					}
					// 计划订单号
					else if (celLength == 2) {
						cell.setCellValue(planInvestCustomize.getPlanOrderId());
					}
					// 出借/承接订单号
					else if (celLength == 3) {
						cell.setCellValue(planInvestCustomize.getOrderId());
					}
					// 用户名
					else if (celLength == 4) {
						cell.setCellValue(planInvestCustomize.getUserName());
					}
					// 债转编号
					else if (celLength == 5) {
						cell.setCellValue(planInvestCustomize.getCreditNid());
					}
					// 项目编号
					else if (celLength == 6) {
						cell.setCellValue(planInvestCustomize.getBorrowNid());
					}
					// 原始项目类型
					else if (celLength == 7) {
						cell.setCellValue(planInvestCustomize.getBorrowTypeName());
					}
					// 出借利率
					else if (celLength == 8) {
						cell.setCellValue(planInvestCustomize.getBorrowApr() + "%");
					}
					// 持有本金
					else if (celLength == 9) {
						cell.setCellValue(planInvestCustomize.getAccount());
					}
					// 持有期限
					else if (celLength == 10) {
						cell.setCellValue(planInvestCustomize.getHoldDays());
					}
					// 剩余期限
					else if (celLength == 11) {
						if (Integer.parseInt(planInvestCustomize.getSurplusDays()) >= 0) {
							cell.setCellValue(planInvestCustomize.getSurplusDays());
						}
					}
					// 公允价值
					else if (celLength == 12) {
						cell.setCellValue(planInvestCustomize.getFairValue());
					}
					// 出借/承接时间
					else if (celLength == 13) {
						cell.setCellValue(planInvestCustomize.getCreateTime());
					}
					// 延期天数
					else if (celLength == 14) {
						cell.setCellValue(planInvestCustomize.getDelayDays());
					}
					// 逾期天数
					else if (celLength == 15) {
						cell.setCellValue(planInvestCustomize.getLateDays());
					}

				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(CreditDetailController.class.toString(), CreditDetailDefine.EXPORT_ACTION);
	}

}
