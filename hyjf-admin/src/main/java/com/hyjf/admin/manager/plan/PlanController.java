package com.hyjf.admin.manager.plan;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.DebtPlanConfig;
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
@RequestMapping(value = PlanDefine.REQUEST_MAPPING)
public class PlanController extends BaseController {

	@Autowired
	private PlanService planService;

	private static DecimalFormat DF_FOR_VIEW = new DecimalFormat("#,##0.00");

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PlanDefine.INIT)
	@RequiresPermissions(PlanDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute("PlanBean") PlanBean form) {
		LogUtil.startLog(PlanController.class.toString(), PlanDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(PlanDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(PlanController.class.toString(), PlanDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面迁移
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PlanDefine.PREVIEW_ACTION)
	@RequiresPermissions(PlanDefine.PERMISSIONS_PREVIEW)
	public ModelAndView previewAction(HttpServletRequest request, @ModelAttribute(PlanDefine.PLAN_FORM) PlanBean form) {
		LogUtil.startLog(PlanController.class.toString(), PlanDefine.PREVIEW_ACTION);
		ModelAndView modelAndView = new ModelAndView(PlanDefine.PREVIEW_PATH);
		modelAndView.addObject("previewUrl", CustomConstants.HOST + "/plan/getPlanPreview.do?planNid=" + form.getPlanNidSrch());
		LogUtil.endLog(PlanController.class.toString(), PlanDefine.INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PlanDefine.SEARCH_ACTION)
	@RequiresPermissions(PlanDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, PlanBean form) {
		LogUtil.startLog(PlanController.class.toString(), PlanDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(PlanDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(PlanController.class.toString(), PlanDefine.INIT);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, PlanBean form) {

		// 计划类型
		List<DebtPlanConfig> planTypeList = planService.getPlanTypeList();
		modelAndView.addObject("planTypeList", planTypeList);

		PlanCommonCustomize planCommonCustomize = new PlanCommonCustomize();
		// 计划编码
		planCommonCustomize.setPlanNidSrch(form.getPlanNidSrch());
		// 计划名称
		planCommonCustomize.setPlanNameSrch(form.getPlanNameSrch());
		// 计划类型 “配置中心-汇添金配置”中所有计划类型（不区分启用禁用）
		planCommonCustomize.setPlanTypeSrch(form.getPlanTypeSrch());
		// 计划状态 发起中；待审核；审核不通过；待开放；募集中；募集完成；锁定中；清算中；还款中；已还款；已流标
		planCommonCustomize.setPlanStatusSrch(form.getPlanStatusSrch());
		// 发起时间
		planCommonCustomize.setTimeStartSrch(form.getTimeStartSrch());
		planCommonCustomize.setTimeEndSrch(form.getTimeEndSrch());

		planCommonCustomize.setSort(form.getSort());
		planCommonCustomize.setCol(form.getCol());

		int count = planService.countPlan(planCommonCustomize);
		if (count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			planCommonCustomize.setLimitStart(paginator.getOffset());
			planCommonCustomize.setLimitEnd(paginator.getLimit());
			Map<String, Object> totalMap = planService.getPlanAccountTotal(planCommonCustomize);
			if (totalMap != null) {
				modelAndView.addObject("planMoneySum", totalMap.get("planMoneySum"));
				modelAndView.addObject("planJoinMoneySum", totalMap.get("planJoinMoneySum"));
				modelAndView.addObject("planMoneyWaitSum", totalMap.get("planMoneyWaitSum"));
			}
			List<DebtPlan> recordList = planService.selectPlanList(planCommonCustomize);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}
		modelAndView.addObject(PlanDefine.PLAN_FORM, form);
	}

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(PlanDefine.EXPORT_ACTION)
	@RequiresPermissions(PlanDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, PlanBean form) throws Exception {
		LogUtil.startLog(PlanController.class.toString(), PlanDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "计划列表";

		PlanCommonCustomize planCommonCustomize = new PlanCommonCustomize();
		// 计划编码
		planCommonCustomize.setPlanNidSrch(form.getPlanNidSrch());
		// 计划名称
		planCommonCustomize.setPlanNameSrch(form.getPlanNameSrch());
		// 计划类型 “配置中心-汇添金配置”中所有计划类型（不区分启用禁用）
		planCommonCustomize.setPlanTypeSrch(form.getPlanTypeSrch());
		// 计划状态 发起中；待审核；审核不通过；待开放；募集中；募集完成；锁定中；清算中；还款中；已还款；已流标
		planCommonCustomize.setPlanStatusSrch(form.getPlanStatusSrch());
		// 发起时间
		planCommonCustomize.setTimeStartSrch(form.getTimeStartSrch());
		planCommonCustomize.setTimeEndSrch(form.getTimeEndSrch());

		List<DebtPlan> resultList = planService.exportPlanList(planCommonCustomize);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "序号", "智投编号", "智投名称", "智投类型", "授权服务金额", "参考年回报率", "服务回报期限", "授权服务金额", "计划未募集金额", "可使用金额", "冻结金额", "状态", "发起时间" };
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
					DebtPlan debtPlan = resultList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 计划编号
					else if (celLength == 1) {
						cell.setCellValue(debtPlan.getDebtPlanNid());
					}
					// 计划名称
					else if (celLength == 2) {
						cell.setCellValue(debtPlan.getDebtPlanName());
					}
					// 计划类型
					else if (celLength == 3) {
						cell.setCellValue(debtPlan.getDebtPlanTypeName());
					}
					// 计划金额
					else if (celLength == 4) {
						cell.setCellValue(debtPlan.getDebtPlanMoney() == null ? "0" : DF_FOR_VIEW.format(debtPlan.getDebtPlanMoney()));
					}
					// 预期年化
					else if (celLength == 5) {
						cell.setCellValue(debtPlan.getExpectApr() == null ? "0" : DF_FOR_VIEW.format(debtPlan.getExpectApr()) + "%");
					}
					// 锁定期
					else if (celLength == 6) {
						cell.setCellValue(debtPlan.getDebtLockPeriod());
					}
					// 加入金额
					else if (celLength == 7) {
						cell.setCellValue(debtPlan.getDebtPlanMoneyYes() == null ? "0" : DF_FOR_VIEW.format(debtPlan.getDebtPlanMoneyYes()));
					}
					// 计划余额
					else if (celLength == 8) {
						cell.setCellValue(debtPlan.getDebtPlanMoneyWait() == null ? "0" : DF_FOR_VIEW.format(debtPlan.getDebtPlanMoneyWait()));
					}
					// 可使用金额
					else if (celLength == 9) {
						cell.setCellValue(debtPlan.getDebtPlanBalance() == null ? "0" : DF_FOR_VIEW.format(debtPlan.getDebtPlanBalance()));
					}
					// 冻结金额
					else if (celLength == 10) {
						cell.setCellValue(debtPlan.getDebtPlanFrost() == null ? "0" : DF_FOR_VIEW.format(debtPlan.getDebtPlanFrost()));
					}
					// 状态
					else if (celLength == 11) {
						if (debtPlan.getDebtPlanStatus() == 0) {
							cell.setCellValue("发起中");
						} else if (debtPlan.getDebtPlanStatus() == 1) {
							cell.setCellValue("待审核");
						} else if (debtPlan.getDebtPlanStatus() == 2) {
							cell.setCellValue("审核不通过");
						} else if (debtPlan.getDebtPlanStatus() == 3) {
							cell.setCellValue("待开放");
						} else if (debtPlan.getDebtPlanStatus() == 4) {
							cell.setCellValue("募集中");
						} else if (debtPlan.getDebtPlanStatus() == 5) {
							cell.setCellValue("锁定中");
						} else if (debtPlan.getDebtPlanStatus() == 6) {
							cell.setCellValue("清算中");
						}else if (debtPlan.getDebtPlanStatus() == 7) {
							cell.setCellValue("清算中");
						} else if (debtPlan.getDebtPlanStatus() == 8) {
							cell.setCellValue("清算完成");
						} else if (debtPlan.getDebtPlanStatus() == 9) {
							cell.setCellValue("未还款");
						} else if (debtPlan.getDebtPlanStatus() == 10) {
							cell.setCellValue("还款中");
						} else if (debtPlan.getDebtPlanStatus() == 11) {
							cell.setCellValue("还款完成");
						} else if( debtPlan.getDebtPlanStatus() == 12){
							cell.setCellValue("流标");
						}
					}
					// 发起时间
					else if (celLength == 12) {
						cell.setCellValue(GetDate.getDateTimeMyTime(debtPlan.getCreateTime()));
					}

				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(PlanController.class.toString(), PlanDefine.EXPORT_ACTION);
	}
}
