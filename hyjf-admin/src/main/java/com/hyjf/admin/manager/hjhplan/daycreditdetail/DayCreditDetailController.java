package com.hyjf.admin.manager.hjhplan.daycreditdetail;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.customize.admin.HjhDebtCreditCustomize;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 汇计划按天转让记录
 * @author liubin
 */
@Controller
@RequestMapping(value = DayCreditDetailDefine.REQUEST_MAPPING)
public class DayCreditDetailController extends BaseController {

	@Autowired
	private DayCreditDetailService dayCreditDetailService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(DayCreditDetailDefine.INIT_ACTION)
	@RequiresPermissions(DayCreditDetailDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, HttpServletResponse response, @ModelAttribute(DayCreditDetailDefine.HJH_CREDIT_FORM) DayCreditDetailBean form) {
		LogUtil.startLog(DayCreditDetailDefine.CONTROLLER_NAME, DayCreditDetailDefine.INIT_ACTION);
		ModelAndView modeAndView = new ModelAndView(DayCreditDetailDefine.LIST_PATH);
		//* 前画面传入检索条件 出让计划编号
		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(DayCreditDetailDefine.CONTROLLER_NAME, DayCreditDetailDefine.INIT_ACTION);
		return modeAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(DayCreditDetailDefine.SEARCH_ACTION)
	@RequiresPermissions(DayCreditDetailDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, HttpServletResponse response, DayCreditDetailBean form) {
		LogUtil.startLog(DayCreditDetailDefine.CONTROLLER_NAME, DayCreditDetailDefine.SEARCH_ACTION);
		ModelAndView modeAndView = new ModelAndView(DayCreditDetailDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(DayCreditDetailDefine.CONTROLLER_NAME, DayCreditDetailDefine.SEARCH_ACTION);
		return modeAndView;
	}

	/**
	 * 创建分页机能
	 * @param request
	 * @param modelAndView
	 * @param planCreditBean
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, DayCreditDetailBean planCreditBean) {

		// 还款方式
		List<BorrowStyle> borrowStyleList = this.dayCreditDetailService.selectBorrowStyleList("");
		modelAndView.addObject("borrowStyleList", borrowStyleList);
		// 转让状态
		modelAndView.addObject("creditStatusList", this.dayCreditDetailService.getParamNameList(CustomConstants.HJH_DEBT_CREDIT_STATUS));
		// 债转还款状态
		modelAndView.addObject("repayStatusList", this.dayCreditDetailService.getParamNameList(CustomConstants.HJH_DEBT_REPAY_STATUS));

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("planNid", planCreditBean.getPlanNid());
		params.put("planOrderId", planCreditBean.getPlanOrderId());
		params.put("planNidNew", planCreditBean.getPlanNidNew());
		params.put("userName", planCreditBean.getUserName());
		params.put("creditNid", planCreditBean.getCreditNid());
		params.put("borrowNid", planCreditBean.getBorrowNid());
		params.put("repayStyle", planCreditBean.getRepayStyle());
		params.put("creditStatus", planCreditBean.getCreditStatus());
		params.put("repayStatus", planCreditBean.getRepayStatus());
		params.put("liquidatesTimeStart", StringUtils.isNotBlank(planCreditBean.getDate())?planCreditBean.getDate():null);
		params.put("liquidatesTimeEnd", StringUtils.isNotBlank(planCreditBean.getDate())?planCreditBean.getDate():null);
		params.put("repayNextTimeStart",StringUtils.isNotBlank(planCreditBean.getRepayNextTimeStart())?planCreditBean.getRepayNextTimeStart():null);
		params.put("repayNextTimeEnd", StringUtils.isNotBlank(planCreditBean.getRepayNextTimeEnd())?planCreditBean.getRepayNextTimeEnd():null);
		params.put("endTimeStart",StringUtils.isNotBlank(planCreditBean.getEndTimeStart())?planCreditBean.getEndTimeStart():null);
		params.put("endTimeEnd", StringUtils.isNotBlank(planCreditBean.getEndTimeEnd())?planCreditBean.getEndTimeEnd():null);
		Integer count = this.dayCreditDetailService.countDebtCredit(params);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(planCreditBean.getPaginatorPage(), count);
			params.put("limitStart", paginator.getOffset());
			params.put("limitEnd", paginator.getLimit());
			List<HjhDebtCreditCustomize> recordList = this.dayCreditDetailService.selectDebtCreditList(params);
			planCreditBean.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
			// 合计
			Map<String , Object> sumRecord = this.dayCreditDetailService.sumRecord(params);
			modelAndView.addObject("sumRecord", sumRecord);
		}
		modelAndView.addObject(DayCreditDetailDefine.HJH_CREDIT_FORM, planCreditBean);
	}

	/**
	 * 详细画面
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = DayCreditDetailDefine.INFO_ACTION)
	@RequiresPermissions(DayCreditDetailDefine.PERMISSIONS_INFO)
	public ModelAndView bailInfoAction(HttpServletRequest request, DayCreditDetailBean form, RedirectAttributes attr) {

		LogUtil.startLog(DayCreditDetailDefine.CONTROLLER_NAME, DayCreditDetailDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(DayCreditDetailDefine.CREDIT_DETAIL_ACITON);
		attr.addAttribute("creditNid", form.getCreditNid());
		LogUtil.endLog(DayCreditDetailDefine.CONTROLLER_NAME, DayCreditDetailDefine.INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 导出功能
	 * @param request
	 * @param response
	 * @param planCreditBean
	 * @throws Exception
	 */
	@RequestMapping(DayCreditDetailDefine.EXPORT_ACTION)
	@RequiresPermissions(DayCreditDetailDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, DayCreditDetailBean planCreditBean) throws Exception {
		LogUtil.startLog(DayCreditDetailDefine.CONTROLLER_NAME, DayCreditDetailDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "智投服务按日转让记录";

		// 转让状态
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("planNid", planCreditBean.getPlanNid());
		params.put("planOrderId", planCreditBean.getPlanOrderId());
		params.put("planNidNew", planCreditBean.getPlanNidNew());
		params.put("userName", planCreditBean.getUserName());
		params.put("creditNid", planCreditBean.getCreditNid());
		params.put("borrowNid", planCreditBean.getBorrowNid());
		params.put("repayStyle", planCreditBean.getRepayStyle());
		params.put("creditStatus", planCreditBean.getCreditStatus());
		params.put("repayStatus", planCreditBean.getRepayStatus());
		params.put("liquidatesTimeStart", StringUtils.isNotBlank(planCreditBean.getDate())?planCreditBean.getDate():null);
		params.put("liquidatesTimeEnd", StringUtils.isNotBlank(planCreditBean.getDate())?planCreditBean.getDate():null);
		params.put("repayNextTimeStart",StringUtils.isNotBlank(planCreditBean.getRepayNextTimeStart())?planCreditBean.getRepayNextTimeStart():null);
		params.put("repayNextTimeEnd", StringUtils.isNotBlank(planCreditBean.getRepayNextTimeEnd())?planCreditBean.getRepayNextTimeEnd():null);
		params.put("endTimeStart",StringUtils.isNotBlank(planCreditBean.getEndTimeStart())?planCreditBean.getEndTimeStart():null);
		params.put("endTimeEnd", StringUtils.isNotBlank(planCreditBean.getEndTimeEnd())?planCreditBean.getEndTimeEnd():null);
		List<HjhDebtCreditCustomize> resultList = this.dayCreditDetailService.selectDebtCreditList(params);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "序号", "出让人智投编号", "出让人智投订单号", "清算后智投编号", "出让人", "债转编号", "原项目编号", "还款方式", "债权本金（元）", "债权价值（元）", "已转让本金（元）", "垫付利息（元）", "转让状态", "项目期数", "实际清算时间", "债转结束时间"};
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
					HjhDebtCreditCustomize data = resultList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					} else if (celLength == 1) {// 出让人计划编号
						cell.setCellValue(data.getPlanNid());
					} else if (celLength == 2) {// 出让人计划订单号
						cell.setCellValue(data.getPlanOrderId());
					} else if (celLength == 3) {// 清算后计划编号
						cell.setCellValue(data.getPlanNidNew());
					} else if (celLength == 4) {// 出让人
						cell.setCellValue(data.getUserName());
					} else if (celLength == 5) {// 债转编号
						cell.setCellValue(data.getCreditNid());
					} else if (celLength == 6) {// 原项目编号
						cell.setCellValue(data.getBorrowNid());
					} else if (celLength == 7) {// 还款方式
						cell.setCellValue(data.getRepayStyleName());
					} else if (celLength == 8) {// 债权本金（元）
						cell.setCellValue(data.getCreditCapital());
					} else if (celLength == 9) {// 债权价值（元）
						cell.setCellValue(data.getLiquidationFairValue());
					} else if (celLength == 10) {// 已转让本金（元）
						cell.setCellValue(data.getAssignCapital());
					} else if (celLength == 11) {// 垫付利息（元）
						cell.setCellValue(data.getAssignAdvanceInterest());
					} else if (celLength == 12) {// 转让状态
						cell.setCellValue(data.getCreditStatusName());
					} else if (celLength == 13) {// 项目总期数
						cell.setCellValue(data.getLiquidatesPeriod()+"/"+data.getBorrowPeriod());
					} /*else if (celLength == 14) {// 清算时所在期数
						cell.setCellValue(data.getLiquidatesPeriod());
					}*/ else if (celLength == 14) {// 实际清算时间
						cell.setCellValue(data.getLiquidatesTime());
					} else if (celLength == 15) {// 债转结束时间
						cell.setCellValue(data.getEndTime());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(DayCreditDetailDefine.CONTROLLER_NAME, DayCreditDetailDefine.EXPORT_ACTION);
	}
}
