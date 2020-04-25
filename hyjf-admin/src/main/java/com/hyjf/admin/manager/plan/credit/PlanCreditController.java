package com.hyjf.admin.manager.plan.credit;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.customize.admin.htj.DebtCreditCustomize;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author wangkun
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = PlanCreditDefine.REQUEST_MAPPING)
public class PlanCreditController extends BaseController {

	@Autowired
	private PlanCreditService debtCreditService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PlanCreditDefine.INIT_ACTION)
	@RequiresPermissions(PlanCreditDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, HttpServletResponse response, @ModelAttribute(PlanCreditDefine.PLAN_CREDIT_FORM) PlanCreditBean form) {
		LogUtil.startLog(PlanCreditDefine.CONTROLLER_NAME, PlanCreditDefine.INIT_ACTION);
		ModelAndView modeAndView = new ModelAndView(PlanCreditDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(PlanCreditDefine.CONTROLLER_NAME, PlanCreditDefine.INIT_ACTION);
		return modeAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PlanCreditDefine.SEARCH_ACTION)
	@RequiresPermissions(PlanCreditDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, HttpServletResponse response, PlanCreditBean form) {
		LogUtil.startLog(PlanCreditDefine.CONTROLLER_NAME, PlanCreditDefine.SEARCH_ACTION);
		ModelAndView modeAndView = new ModelAndView(PlanCreditDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(PlanCreditDefine.CONTROLLER_NAME, PlanCreditDefine.SEARCH_ACTION);
		return modeAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, PlanCreditBean planCreditBean) {

		// 还款方式
		List<BorrowStyle> borrowStyleList = this.debtCreditService.selectBorrowStyleList("");
		modelAndView.addObject("borrowStyleList", borrowStyleList);
		// 转让状态
		modelAndView.addObject("creditStatusList", this.debtCreditService.getParamNameList(CustomConstants.DEBT_CREDIT_STATUS));
		// 债转还款状态
		modelAndView.addObject("repayStatusList", this.debtCreditService.getParamNameList(CustomConstants.DEBT_REPAY_STATUS));
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("planNid", planCreditBean.getPlanNid());
		params.put("planOrderId", planCreditBean.getPlanOrderId());
		params.put("userName", planCreditBean.getUserName());
		params.put("creditNid", planCreditBean.getCreditNid());
		params.put("borrowNid", planCreditBean.getBorrowNid());
		params.put("repayStyle", planCreditBean.getRepayStyle());
		params.put("creditStatus", planCreditBean.getCreditStatus());
		params.put("liquidatesTimeStart", StringUtils.isNotBlank(planCreditBean.getLiquidatesTimeStart())?planCreditBean.getLiquidatesTimeStart():null);
		params.put("liquidatesTimeEnd", StringUtils.isNotBlank(planCreditBean.getLiquidatesTimeEnd())?planCreditBean.getLiquidatesTimeEnd():null);
		params.put("repayNextTimeStart",StringUtils.isNotBlank(planCreditBean.getRepayNextTimeStart())?planCreditBean.getRepayNextTimeStart():null);
		params.put("repayNextTimeEnd", StringUtils.isNotBlank(planCreditBean.getRepayNextTimeEnd())?planCreditBean.getRepayNextTimeEnd():null);
		Integer count = this.debtCreditService.countDebtCredit(params);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(planCreditBean.getPaginatorPage(), count);
			params.put("limitStart", paginator.getOffset());
			params.put("limitEnd", paginator.getLimit());
			List<DebtCreditCustomize> recordList = this.debtCreditService.selectDebtCreditList(params);
			planCreditBean.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}
		modelAndView.addObject(PlanCreditDefine.PLAN_CREDIT_FORM, planCreditBean);
	}

	/**
	 * 详细画面
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = PlanCreditDefine.INFO_ACTION)
	@RequiresPermissions(PlanCreditDefine.PERMISSIONS_INFO)
	public ModelAndView bailInfoAction(HttpServletRequest request, PlanCreditBean form, RedirectAttributes attr) {

		LogUtil.startLog(PlanCreditDefine.CONTROLLER_NAME, PlanCreditDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(PlanCreditDefine.CREDIT_DETAIL_ACITON);
		attr.addAttribute("creditNid", form.getCreditNid());
		LogUtil.endLog(PlanCreditDefine.CONTROLLER_NAME, PlanCreditDefine.INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(PlanCreditDefine.EXPORT_ACTION)
	@RequiresPermissions(PlanCreditDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, PlanCreditBean planCreditBean) throws Exception {
		LogUtil.startLog(PlanCreditDefine.CONTROLLER_NAME, PlanCreditDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "汇添金计划转让记录";

		// 转让状态
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("planNid", planCreditBean.getPlanNid());
		params.put("planOrderId", planCreditBean.getPlanOrderId());
		params.put("userName", planCreditBean.getUserName());
		params.put("creditNid", planCreditBean.getCreditNid());
		params.put("borrowNid", planCreditBean.getBorrowNid());
		params.put("repayStyle", planCreditBean.getRepayStyle());
		params.put("creditStatus", planCreditBean.getCreditStatus());
		params.put("liquidatesTimeStart", StringUtils.isNotBlank(planCreditBean.getLiquidatesTimeStart())?planCreditBean.getLiquidatesTimeStart():null);
		params.put("liquidatesTimeEnd", StringUtils.isNotBlank(planCreditBean.getLiquidatesTimeEnd())?planCreditBean.getLiquidatesTimeEnd():null);
		params.put("repayNextTimeStart",StringUtils.isNotBlank(planCreditBean.getRepayNextTimeStart())?planCreditBean.getRepayNextTimeStart():null);
		params.put("repayNextTimeEnd", StringUtils.isNotBlank(planCreditBean.getRepayNextTimeEnd())?planCreditBean.getRepayNextTimeEnd():null);
		List<DebtCreditCustomize> resultList = this.debtCreditService.selectDebtCreditList(params);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "序号", "出让人智投编号", "出让人智投订单号", "出让人", "债转编号", "原项目编号", "原项目出借利率", "还款方式", "债权本金", "预计实际收益率", "收到转让本金", "收到垫付利息", "清算手续费率", "实际服务费", "出让人实际到账金额", "实际清算时间", "转让状态", "项目总期数 " };
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
					DebtCreditCustomize debtCredit = resultList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 出让人计划编号
					else if (celLength == 1) {
						cell.setCellValue(debtCredit.getPlanNid());
					}
					// 出让人计划订单号
					else if (celLength == 2) {
						cell.setCellValue(debtCredit.getPlanOrderId());
					}
					// 出让人
					else if (celLength == 3) {
						cell.setCellValue(debtCredit.getUserName());
					}
					// 债转编号
					else if (celLength == 4) {
						cell.setCellValue(debtCredit.getCreditNid());
					}
					// 原项目编号
					else if (celLength == 5) {
						cell.setCellValue(debtCredit.getBorrowNid());
					}
					// 原项目出借利率
					else if (celLength == 6) {
						cell.setCellValue(debtCredit.getBorrowApr()+"%");
					}
					// 还款方式
					else if (celLength == 7) {
						cell.setCellValue(debtCredit.getRepayStyleName());
					}
					// 债权本金
					else if (celLength == 8) {
						cell.setCellValue(debtCredit.getCreditCapital());
					}
					// 预计实际收益率
					else if (celLength == 9) {
						cell.setCellValue(debtCredit.getActualApr()+"%");
					}
					// 收到转让本金
					else if (celLength == 10) {
						cell.setCellValue(debtCredit.getAssignCapital());
					}
					// 收到垫付利息
					else if (celLength == 11) {
						cell.setCellValue(debtCredit.getAssignAdvanceInterest());
					}
					// 清算手续费率
					else if (celLength == 12) {
						cell.setCellValue(debtCredit.getServiceFeeRate()+"%");
					}
					// 实际服务费
					else if (celLength == 13) {
						cell.setCellValue(debtCredit.getServiceFee());
					}
					// 出让人实际到账金额
					else if (celLength == 14) {
						cell.setCellValue(debtCredit.getAccountReceive());
					}
					// 实际清算时间
					else if (celLength == 15) {
						cell.setCellValue(debtCredit.getLiquidatesTime());
					}
					// 转让状态
					else if (celLength == 16) {
						cell.setCellValue(debtCredit.getCreditStatusName());
					}
					// 项目总期数
					else if (celLength == 17) {
						cell.setCellValue(debtCredit.getBorrowPeriod());
					}

				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(PlanCreditDefine.CONTROLLER_NAME, PlanCreditDefine.EXPORT_ACTION);
	}
}
