package com.hyjf.admin.manager.plan.loandetail;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.hyjf.admin.manager.plan.planlock.PlanLockService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.customize.PlanCommonCustomize;

/**
 * 
 * @author: zhouxiaoshuai
 * @email: 287424494@qq.com
 * @description:计划所用控制器
 * @version: 1
 * @date: 2016年9月12日 下午2:50:16
 */
@Controller
@RequestMapping(value = LoanDetailDefine.REQUEST_MAPPING)
public class LoanDetailController extends BaseController {

	@Autowired
	private PlanLockService planLockService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(LoanDetailDefine.INIT)
	@RequiresPermissions(LoanDetailDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute("LoanDetailBean") LoanDetailBean form) {
		LogUtil.startLog(LoanDetailController.class.toString(), LoanDetailDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(LoanDetailDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(LoanDetailController.class.toString(), LoanDetailDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(LoanDetailDefine.SEARCH_ACTION)
	@RequiresPermissions(LoanDetailDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, LoanDetailBean form) {
		LogUtil.startLog(LoanDetailController.class.toString(), LoanDetailDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(LoanDetailDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(LoanDetailController.class.toString(), LoanDetailDefine.INIT);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, LoanDetailBean form) {
		PlanCommonCustomize planCommonCustomize = new PlanCommonCustomize();
		// 计划编码
		planCommonCustomize.setPlanNidSrch(form.getPlanNidSrch());
		// 计划订单号
		planCommonCustomize.setPlanOrderId(form.getPlanOrderId());
		// 出借/承接订单号
		planCommonCustomize.setOrderId(form.getOrderId());
		// 用户名
		planCommonCustomize.setUserName(form.getUserName());
		// 项目编号
		planCommonCustomize.setBorrowNid(form.getBorrowNidSrch());
		;
		// 回款状态
		planCommonCustomize.setRepayStatus(form.getRepayStatus());
		// 应回款日期开始
		planCommonCustomize.setRepayTimeStart(StringUtils.isNotBlank(form.getRepayTimeStart()) ? form
				.getRepayTimeStart() : null);
		// 应回款日期结束
		planCommonCustomize.setRepayTimeEnd(StringUtils.isNotBlank(form.getRepayTimeEnd()) ? form.getRepayTimeEnd()
				: null);
		// 回款明细
		Long count = planLockService.countLoanDetailNew(planCommonCustomize);
		if (count > 0) {
			// 债权总额
			HashMap<String, Object> planLoanSumMap = planLockService.LoanDeailSumMapNew(planCommonCustomize);
			modelAndView.addObject("loanCapitalSum", planLoanSumMap.get("loanCapitalSum"));
			modelAndView.addObject("loanInterestSum", planLoanSumMap.get("loanInterestSum"));
			modelAndView.addObject("loanAccountSum", planLoanSumMap.get("loanAccountSum"));
			modelAndView.addObject("repayAccountSum", planLoanSumMap.get("repayAccountSum"));
			modelAndView.addObject("serviceFeeSum", planLoanSumMap.get("serviceFeeSum"));
			modelAndView.addObject("fairValueSum", planLoanSumMap.get("fairValueSum"));
			if (form.getPaginatorPage() == 0) {
				form.setPaginatorPage(1);
			}
			Paginator paginator = new Paginator(form.getPaginatorPage(), count.intValue());
			planCommonCustomize.setLimitStart(paginator.getOffset());
			planCommonCustomize.setLimitEnd(paginator.getLimit());
			List<Map<String, Object>> debtLoanList = planLockService.selectLoanDetailListNew(planCommonCustomize);
			form.setPaginator(paginator);
			modelAndView.addObject("debtLoanList", debtLoanList);
		}
		modelAndView.addObject(LoanDetailDefine.CREDIT_FORM, form);
	}

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(LoanDetailDefine.EXPORT_ACTION)
	@RequiresPermissions(LoanDetailDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, LoanDetailBean form)
			throws Exception {
		LogUtil.startLog(LoanDetailController.class.toString(), LoanDetailDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "回款明细";
		PlanCommonCustomize planCommonCustomize = new PlanCommonCustomize();
		// 计划编码
		planCommonCustomize.setPlanNidSrch(form.getPlanNidSrch());
		// 计划订单号
		planCommonCustomize.setPlanOrderId(form.getPlanOrderId());
		// 出借/承接订单号
		planCommonCustomize.setOrderId(form.getOrderId());
		// 用户名
		planCommonCustomize.setUserName(form.getUserName());
		// 项目编号
		planCommonCustomize.setBorrowNid(form.getBorrowNidSrch());
		;
		// 回款状态
		planCommonCustomize.setRepayStatus(form.getRepayStatus());
		// 应回款日期开始
		planCommonCustomize.setRepayTimeStart(StringUtils.isNotBlank(form.getRepayTimeStart()) ? form
				.getRepayTimeStart() : null);
		// 应回款日期结束
		planCommonCustomize.setRepayTimeEnd(StringUtils.isNotBlank(form.getRepayTimeEnd()) ? form.getRepayTimeEnd()
				: null);
		List<Map<String, Object>> debtLoanList = planLockService.selectLoanDetailListNew(planCommonCustomize);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date())
				+ CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "序号", "智投编号", "智投订单号", "用户名", "出借/承接订单号", "项目编号", "回款期次", "应回款本金", "应回款利息",
				"应回款总额", "实际回款本金", "实际回款利息", "实际回款总额", "回款状态", "实际回款时间", "应回款时间", "服务费", "到期公允价值字段的值", "延期天数", "延期利息",
				"逾期天数", "逾期利息" };
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();

		// 生成一个表格
		HSSFSheet sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, sheetName + "_第1页");

		if (debtLoanList != null && debtLoanList.size() > 0) {

			int sheetCount = 1;
			int rowNum = 0;

			for (int i = 0; i < debtLoanList.size(); i++) {
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
					Map<String, Object> debtLoan = debtLoanList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 计划编号
					else if (celLength == 1) {
						cell.setCellValue(debtLoan.get("planNid") + "");
					}
					// 计划订单号
					else if (celLength == 2) {
						cell.setCellValue(debtLoan.get("planOrderId") + "");
					}
					// 用户名
					else if (celLength == 3) {
						cell.setCellValue(debtLoan.get("userName") + "");
					}
					// 出借/承接订单号
					else if (celLength == 4) {
						cell.setCellValue(debtLoan.get("investOrderId") + "");
					}
					// 项目编号
					else if (celLength == 5) {
						cell.setCellValue(debtLoan.get("borrowNid") + "");
					}
					// 回款期次
					else if (celLength == 6) {
						cell.setCellValue(debtLoan.get("repayPeriod") + "");
					}
					// 应回款本金
					else if (celLength == 7) {
						cell.setCellValue(debtLoan.get("loanCapital") + "");
					}
					// 应回款利息
					else if (celLength == 8) {
						cell.setCellValue(debtLoan.get("loanInterest") + "");
					}
					// 应回款总额
					else if (celLength == 9) {
						cell.setCellValue(debtLoan.get("loanAccount") + "");
					}
					// 实际回款本金
					else if (celLength == 10) {
						if (debtLoan.get("repayCapitalYes") == null) {
							cell.setCellValue("0");
						} else {
							cell.setCellValue(debtLoan.get("repayCapitalYes") + "");
						}

					}
					// 实际回款利息
					else if (celLength == 11) {
						if (debtLoan.get("repayInterestYes") == null) {
							cell.setCellValue("0");
						} else {
							cell.setCellValue(debtLoan.get("repayInterestYes") + "");
						}
					}
					// 实际回款总额
					else if (celLength == 12) {
						if (debtLoan.get("repayAccountYes") == null) {
							cell.setCellValue("0");
						} else {
							cell.setCellValue(debtLoan.get("repayAccountYes") + "");
						}
					}
					// 回款状态 还款状态 0未还款 1还款中 2已还款
					else if (celLength == 13) {
						if ((debtLoan.get("repayStatus") + "").equals("0")) {
							cell.setCellValue("未还款");
						} else if ((debtLoan.get("repayStatus") + "").equals("1")) {
							cell.setCellValue("已还款");
						}
					}
					// 实际回款时间
					else if (celLength == 14) {
						if (debtLoan.get("repayActionTime") == null
								|| debtLoan.get("repayActionTime").equals("1970-01-01")) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(debtLoan.get("repayActionTime") + "");
						}
					}
					// 应回款时间
					else if (celLength == 15) {
						cell.setCellValue(debtLoan.get("repayTime") + "");
					}
					// 服务费
					else if (celLength == 16) {
						if (debtLoan.get("serviceFee") == null) {
							cell.setCellValue("0");
						} else {
							cell.setCellValue(debtLoan.get("serviceFee") + "");
						}
					}
					// 到期公允价值
					else if (celLength == 17) {
						if (debtLoan.get("expireFairValue") == null) {
							cell.setCellValue("0");
						} else {
							cell.setCellValue(debtLoan.get("expireFairValue") + "");
						}
					}
					// "延期天数", "延期利息",
					// "逾期天数", "逾期利息"
					else if (celLength == 18) {
						cell.setCellValue(debtLoan.get("delay_days") + "");
					} else if (celLength == 19) {
						cell.setCellValue(debtLoan.get("delay_interest") + "");
					} else if (celLength == 20) {
						cell.setCellValue(debtLoan.get("late_days") + "");
					} else if (celLength == 21) {
						cell.setCellValue(debtLoan.get("late_interest") + "");
					}

				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(LoanDetailController.class.toString(), LoanDetailDefine.EXPORT_ACTION);
	}

}
