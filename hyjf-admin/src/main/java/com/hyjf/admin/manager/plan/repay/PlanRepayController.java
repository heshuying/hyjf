package com.hyjf.admin.manager.plan.repay;

import java.util.Date;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.customize.PlanCommonCustomize;
import com.hyjf.mybatis.model.customize.PlanLockCustomize;

import cn.jpush.api.utils.StringUtils;
/**
 * 
 * @author: zhouxiaoshuai
 * @email: 	287424494@qq.com		
* @description:计划所用控制器
 * @version:     1 
 * @date:   2016年9月12日 下午2:50:16
 */
@Controller
@RequestMapping(value = PlanRepayDefine.REQUEST_MAPPING)
public class PlanRepayController extends BaseController {

	@Autowired
	private PlanRepayService planRepayService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PlanRepayDefine.INIT)
	@RequiresPermissions(PlanRepayDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute("PlanRepayBean") PlanRepayBean form) {
		LogUtil.startLog(PlanRepayController.class.toString(), PlanRepayDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(PlanRepayDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(PlanRepayController.class.toString(), PlanRepayDefine.INIT);
		return modelAndView;
	}

	
	/**
	 * 画面迁移
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PlanRepayDefine.PREVIEW_ACTION)
	@RequiresPermissions(PlanRepayDefine.PERMISSIONS_PREVIEW)
	public ModelAndView previewAction(HttpServletRequest request, @ModelAttribute(PlanRepayDefine.PLAN_FORM) PlanRepayBean form) {
		LogUtil.startLog(PlanRepayController.class.toString(), PlanRepayDefine.PREVIEW_ACTION);
		ModelAndView modelAndView = new ModelAndView(PlanRepayDefine.PREVIEW_PATH);
		modelAndView.addObject("previewUrl", CustomConstants.HOST+"/plan/getPlanPreview.do?planNid="+form.getPlanNidSrch());
		LogUtil.endLog(PlanRepayController.class.toString(), PlanRepayDefine.INFO_ACTION);
		return modelAndView;
	}
	
	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PlanRepayDefine.SEARCH_ACTION)
	@RequiresPermissions(PlanRepayDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, PlanRepayBean form) {
		LogUtil.startLog(PlanRepayController.class.toString(), PlanRepayDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(PlanRepayDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(PlanRepayController.class.toString(), PlanRepayDefine.INIT);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, PlanRepayBean form) {

		PlanCommonCustomize planCommonCustomize = new PlanCommonCustomize();
		// 计划编码
		planCommonCustomize.setPlanNidSrch(form.getPlanNidSrch());
		// 计划状态 0 发起中；1 待审核；2审核不通过；3待开放；4募集中；5锁定中；6清算中；7清算完成，8未还款，9还款中，10还款完成
		planCommonCustomize.setPlanStatusSrch(form.getPlanStatusSrch());
		// 最迟还款日
		planCommonCustomize.setRepayTimeLastStart(form.getRepayTimeLastStart());
		planCommonCustomize.setRepayTimeLastEnd(form.getRepayTimeLastEnd());
		// 实际还款日期
		planCommonCustomize.setRepayTimeStart(form.getRepayTimeStart());
		planCommonCustomize.setRepayTimeEnd(form.getRepayTimeEnd());
		planCommonCustomize.setSort(form.getSort());
		planCommonCustomize.setCol(form.getCol());

		int count = planRepayService.countPlan(planCommonCustomize);
		if (count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			planCommonCustomize.setLimitStart(paginator.getOffset());
			planCommonCustomize.setLimitEnd(paginator.getLimit());
			List<DebtPlan> recordList = planRepayService.selectPlanList(planCommonCustomize);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}
		modelAndView.addObject(PlanRepayDefine.PLAN_FORM, form);
	}

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(PlanRepayDefine.EXPORT_ACTION)
	@RequiresPermissions(PlanRepayDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, PlanRepayBean form) throws Exception {
		LogUtil.startLog(PlanRepayController.class.toString(), PlanRepayDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "还款列表";
		PlanCommonCustomize planCommonCustomize = new PlanCommonCustomize();
		// 计划编码
		planCommonCustomize.setPlanNidSrch(form.getPlanNidSrch());
		// 计划状态 0 发起中；1 待审核；2审核不通过；3待开放；4募集中；5锁定中；6清算中；7清算完成，8未还款，9还款中，10还款完成
		planCommonCustomize.setPlanStatusSrch(form.getPlanStatusSrch());
		// 最迟还款日
		planCommonCustomize.setRepayTimeLastStart(form.getRepayTimeLastStart());
		planCommonCustomize.setRepayTimeLastEnd(form.getRepayTimeLastEnd());
		// 实际还款日期
		planCommonCustomize.setRepayTimeStart(form.getRepayTimeStart());
		planCommonCustomize.setRepayTimeEnd(form.getRepayTimeEnd());
		
		List<DebtPlan> resultList = planRepayService.exportPlanList(planCommonCustomize);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "序号", "智投编号", "服务回报期限","参考年回报率","授权服务金额", "应还利息", "应还总额", "计划余额",
				"清算到账", "转让进度", "收服务费", "应清算日期", "实际清算日期", "最迟还款日", "实还本金", "实还利息", "实还总额",
				"实际还款时间", "智投状态"};
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
					// 锁定期
					else if (celLength == 2) {
						cell.setCellValue(debtPlan.getDebtLockPeriod());
					}
					// 出借利率
					else if (celLength == 3) {
						cell.setCellValue(debtPlan.getExpectApr() == null ? "0" : String.valueOf(debtPlan.getExpectApr()) + "%");
					}
					// 加入金额
					else if (celLength == 4) {
						cell.setCellValue(debtPlan.getDebtPlanMoneyYes() == null ? "0" : CustomConstants.DF_FOR_VIEW.format(debtPlan.getDebtPlanMoneyYes()));
					}
					// 应还利息
					else if (celLength == 5) {
						cell.setCellValue(debtPlan.getRepayAccountInterest() == null ? "0" : CustomConstants.DF_FOR_VIEW.format(debtPlan.getRepayAccountInterest()));
					}
					// 应还总额
					else if (celLength == 6) {
						cell.setCellValue(debtPlan.getRepayAccountAll() == null ? "0" : CustomConstants.DF_FOR_VIEW.format(debtPlan.getRepayAccountAll()));
					}
					// 计划余额
					else if (celLength == 7) {
						cell.setCellValue(debtPlan.getDebtPlanBalance() == null ? "0" : CustomConstants.DF_FOR_VIEW.format(debtPlan.getDebtPlanBalance()));
					}
					// 清算到账
					else if (celLength == 8) {
						cell.setCellValue(debtPlan.getLiquidateArrivalAmount() == null ? "0" : CustomConstants.DF_FOR_VIEW.format(debtPlan.getLiquidateArrivalAmount()));
					}
					// 转让进度
					else if (celLength == 9) {
						cell.setCellValue(debtPlan.getLiquidateApr() == null ? "0" : CustomConstants.DF_FOR_VIEW.format(debtPlan.getLiquidateApr()));
					}
					// 收服务费
					else if (celLength == 10) {
						cell.setCellValue(debtPlan.getServiceFee() == null ? "" : CustomConstants.DF_FOR_VIEW.format(debtPlan.getServiceFee()));
					}
					// 应清算日期
					else if (celLength == 11) {
						cell.setCellValue(debtPlan.getLiquidateShouldTime() == null ? "" : GetDate.times10toStrYYYYMMDD(debtPlan.getLiquidateShouldTime()));
					}
					// 实际清算日期
					else if (celLength == 12) {
						cell.setCellValue(debtPlan.getLiquidateFactTime() == null ? "" : GetDate.times10toStrYYYYMMDD(debtPlan.getLiquidateFactTime()));
					}
					// 最迟还款日
					else if (celLength == 13) {
						cell.setCellValue(debtPlan.getRepayTimeLast() == null ? "" : GetDate.times10toStrYYYYMMDD(debtPlan.getRepayTimeLast()));
					}
					// 实还本金
					else if (celLength == 14) {
						cell.setCellValue(debtPlan.getRepayAccountCapitalYes() == null ? "0" : CustomConstants.DF_FOR_VIEW.format(debtPlan.getRepayAccountCapitalYes()));
					}
					// 实还利息
					else if (celLength == 15) {
						cell.setCellValue(debtPlan.getRepayAccountInterestYes() == null ? "0" : CustomConstants.DF_FOR_VIEW.format(debtPlan.getRepayAccountInterestYes()));
					}
					// 实还总额
					else if (celLength == 16) {
						cell.setCellValue(debtPlan.getRepayAccountYes() == null ? "0" : CustomConstants.DF_FOR_VIEW.format(debtPlan.getRepayAccountYes()));
					}
					// 实际还款时间
					else if (celLength == 17) {
						cell.setCellValue(debtPlan.getRepayTime() == null ? "" : GetDate.times10toStrYYYYMMDD(debtPlan.getRepayTime()));
						}
					// 计划状态
					else if (celLength == 18) {
						if (debtPlan.getDebtPlanStatus()==8) {
							cell.setCellValue("待还款");
						}else if (debtPlan.getDebtPlanStatus()==9) {
							cell.setCellValue("待还款");
						}else if (debtPlan.getDebtPlanStatus()==10) {
							cell.setCellValue("还款中");
						}else if (debtPlan.getDebtPlanStatus()==11) {
							cell.setCellValue("已还款");
						}
						
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(PlanRepayController.class.toString(), PlanRepayDefine.EXPORT_ACTION);
	}
	
	
	
	
	


	/**
	 * 详情页面
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PlanRepayDefine.INFO_ACTION)
	@RequiresPermissions(PlanRepayDefine.PERMISSIONS_VIEW)
	public ModelAndView infoAction(HttpServletRequest request, @ModelAttribute("PlanRepayBean") PlanRepayBean form) {
		LogUtil.startLog(PlanRepayController.class.toString(), PlanRepayDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(PlanRepayDefine.INFO_PATH);
		// 创建分页
		this.createInfoPage(request, modelAndView, form);
		LogUtil.endLog(PlanRepayController.class.toString(), PlanRepayDefine.INFO_ACTION);
		return modelAndView;
	}

	
	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PlanRepayDefine.INFO_SEARCH_ACTION)
	@RequiresPermissions(PlanRepayDefine.PERMISSIONS_SEARCH)
	public ModelAndView infoSearch(HttpServletRequest request, PlanRepayBean form) {
		LogUtil.startLog(PlanRepayController.class.toString(), PlanRepayDefine.INFO_SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(PlanRepayDefine.INFO_PATH);

		// 创建分页
		this.createInfoPage(request, modelAndView, form);
		LogUtil.endLog(PlanRepayController.class.toString(), PlanRepayDefine.INFO_SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createInfoPage(HttpServletRequest request, ModelAndView modelAndView, PlanRepayBean form) {
		PlanCommonCustomize planCommonCustomize = new PlanCommonCustomize();
		// 计划编码
		planCommonCustomize.setPlanNidSrch(form.getPlanNidSrch());
		// 用户名
		planCommonCustomize.setUserName(form.getUserName());
		// 计划余额最小
		planCommonCustomize.setPlanWaitMoneyMin(form.getPlanWaitMoneyMin());
		// 计划余额最大
		planCommonCustomize.setPlanWaitMoneyMax(form.getPlanWaitMoneyMax());
		// 加入时间开始
		planCommonCustomize.setJoinTimeStart(form.getJoinTimeStart());
		// 加入时间结束
		planCommonCustomize.setJoinTimeEnd(form.getJoinTimeEnd());
		//项目编号
		planCommonCustomize.setBorrowNid(form.getBorrowNidSrch());
		//项目订单号
		planCommonCustomize.setPlanOrderId(form.getPlanOrderId());
		if (form.getPlanStatusSrch()==null||form.getPlanStatusSrch().equals("")) {
			//还款状态
			planCommonCustomize.setStatus("30");
		}else {
			//还款状态
			planCommonCustomize.setStatus(form.getPlanStatusSrch());
		}
		//应回款日期开始
		planCommonCustomize.setRepayTimeStart(form.getRepayTimeStart());
		//应回款日期结束
		planCommonCustomize.setRepayTimeEnd(form.getRepayTimeEnd());
		planCommonCustomize.setSort(form.getSort());
		planCommonCustomize.setCol(form.getCol());
		//计划基本信息
		List<DebtPlan> planList = planRepayService.selectPlanList(planCommonCustomize);
		//加入明细外面页面
		if (planList!=null&&planList.size()>0) {
			modelAndView.addObject("plan", planList.get(0));
	
		HashMap<String , Object> planCreditCountMap=planRepayService.selectPlanCreditCountMap(form.getPlanNidSrch());
		if (planCreditCountMap!=null) {
			//加入订单数
			modelAndView.addObject("planCredit", planCreditCountMap);
		}
					//加入明细
					Long count = planRepayService.countPlanAccede(planCommonCustomize);
					if (count > 0) {
						//计划总额
						HashMap<String , Object> planLockSumMap=planRepayService.planLockSumMap(planCommonCustomize);
						modelAndView.addObject("repayAccount", planLockSumMap.get("repayAccount"));
						modelAndView.addObject("orderMoney", planLockSumMap.get("orderMoney"));//可用余额
						modelAndView.addObject("repayAccountFact", planLockSumMap.get("repayAccountFact"));
						modelAndView.addObject("repayInterestFact", planLockSumMap.get("repayInterestFact"));//实还利息
						modelAndView.addObject("serviceFeeRate", planLockSumMap.get("serviceFeeRate"));//服务费
						Paginator paginator = new Paginator(form.getPaginatorPage(), count.intValue());
						planCommonCustomize.setLimitStart(paginator.getOffset());
						planCommonCustomize.setLimitEnd(paginator.getLimit());
						List<PlanLockCustomize> recordList = planRepayService.selectPlanAccedeList(planCommonCustomize);
						form.setPaginator(paginator);
						modelAndView.addObject("recordList", recordList);
					}	
		
		}
		modelAndView.addObject(PlanRepayDefine.PLAN_FORM, form);
	}
	
	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(PlanRepayDefine.INFO_EXPORT_ACTION)
	@RequiresPermissions(PlanRepayDefine.PERMISSIONS_EXPORT)
	public void infoexportAction(HttpServletRequest request, HttpServletResponse response, PlanRepayBean form) throws Exception {
		LogUtil.startLog(PlanRepayController.class.toString(), PlanRepayDefine.INFO_EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "还款详情列表";
		PlanCommonCustomize planCommonCustomize = new PlanCommonCustomize();
		// 计划编码
		planCommonCustomize.setPlanNidSrch(form.getPlanNidSrch());
		// 用户名
		planCommonCustomize.setUserName(form.getUserName());
		// 计划余额最小
		planCommonCustomize.setPlanWaitMoneyMin(form.getPlanWaitMoneyMin());
		// 计划余额最大
		planCommonCustomize.setPlanWaitMoneyMax(form.getPlanWaitMoneyMax());
		// 加入时间开始
		planCommonCustomize.setJoinTimeStart(form.getJoinTimeStart());
		// 加入时间结束
		planCommonCustomize.setJoinTimeEnd(form.getJoinTimeEnd());
		//项目编号
		planCommonCustomize.setBorrowNid(form.getBorrowNidSrch());
		//项目订单号
		planCommonCustomize.setPlanOrderId(form.getPlanOrderId());
		if (form.getPlanStatusSrch()==null||form.getPlanStatusSrch().equals("")) {
			//还款状态
			planCommonCustomize.setStatus("30");
		}else {
			//还款状态
			planCommonCustomize.setStatus(form.getPlanStatusSrch());
		}
		//应回款日期开始
		planCommonCustomize.setRepayTimeStart(form.getRepayTimeStart());
		//应回款日期结束
		planCommonCustomize.setRepayTimeEnd(form.getRepayTimeEnd());
		planCommonCustomize.setSort(form.getSort());
		planCommonCustomize.setCol(form.getCol());
		List<PlanLockCustomize> recordList = planRepayService.selectPlanAccedeList(planCommonCustomize);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "序号", "智投订单号", "用户名","授权服务金额","应还利息", "应还总额", "可用余额", "本期实还利息",
				"实际还款总额", "服务费", "还款状态"};
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
					PlanLockCustomize planLockCustomize = recordList.get(i);
					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 订单号
					else if (celLength == 1) {
						cell.setCellValue(planLockCustomize.getAccedeOrderId());
					}
					// 用户名
					else if (celLength == 2) {
						cell.setCellValue(planLockCustomize.getUserName());
					}
					// 加入金额
					else if (celLength == 3) {
						cell.setCellValue(planLockCustomize.getAccedeAccount());
					}
					// 应还利息
					else if (celLength == 4) {
						cell.setCellValue(planLockCustomize.getRepayInterest());
					}
					// 应还总额
					else if (celLength == 5) {
						cell.setCellValue(planLockCustomize.getRepayAccount());
					}
					// 可用余额
					else if (celLength == 6) {
						cell.setCellValue(planLockCustomize.getAccedeBalance());
					}
					// 本期实还利息
					else if (celLength == 7) {
						cell.setCellValue(planLockCustomize.getRepayInterestYes());
					}
					// 实际还款总额
					else if (celLength == 8) {
						cell.setCellValue(planLockCustomize.getRepayAccountYes());
					}
					//服务费
					else if (celLength == 9) {
						cell.setCellValue(planLockCustomize.getServiceFee());
					}
					//还款状态
					else if (celLength == 10) {
						if (planLockCustomize.getStatus().equals("3")) {
							cell.setCellValue("未还款");
						}
						if (planLockCustomize.getStatus().equals("4")) {
							cell.setCellValue("还款中");
						}
						if (planLockCustomize.getStatus().equals("5")) {
							cell.setCellValue("已还款");
						}
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(PlanRepayController.class.toString(), PlanRepayDefine.EXPORT_ACTION);
	}
	
	
	
	
	/**
	 * 还款 画面初始化 
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PlanRepayDefine.UPDATE_ACTION)
	@RequiresPermissions(PlanRepayDefine.PERMISSIONS_MODIFY)
	@ResponseBody
	public Map<String, String>  repayNow(HttpServletRequest request, String  plannid) {
		LogUtil.startLog(PlanRepayController.class.toString(), PlanRepayDefine.UPDATE_ACTION);
		Map<String , String> map= new HashMap<String, String>();
		if (StringUtils.isNotEmpty(plannid)) {
			boolean status=planRepayService.repayNow(plannid);
			if (status) {
				map.put("status", "success");
				map.put("result", "更新成功 ，计划还款正在处理中");
			}else {
				map.put("status", "false");
				map.put("result", "更新失败 ");
			}
			
		}else {
			map.put("status", "false");
			map.put("result", "参数不能为空");
		}
		
		LogUtil.endLog(PlanRepayController.class.toString(), PlanRepayDefine.UPDATE_ACTION);
		return map;
	}

}
