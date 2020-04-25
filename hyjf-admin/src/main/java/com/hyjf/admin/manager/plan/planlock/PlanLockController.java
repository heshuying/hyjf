package com.hyjf.admin.manager.plan.planlock;

import java.math.BigDecimal;
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

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.debt.debtborrow.DebtBorrowService;
import com.hyjf.admin.manager.debt.debtborrowcommon.DebtBorrowCommonService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.DebtCredit;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.DebtPlanAccede;
import com.hyjf.mybatis.model.customize.PlanCommonCustomize;
import com.hyjf.mybatis.model.customize.PlanInvestCustomize;
import com.hyjf.mybatis.model.customize.PlanLockCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtAdminCreditCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCustomize;

import cn.jpush.api.utils.StringUtils;

/**
 * 
 * @author: zhouxiaoshuai
 * @email: 287424494@qq.com
 * @description:计划所用控制器
 * @version: 1
 * @date: 2016年9月12日 下午2:50:16
 */
@Controller
@RequestMapping(value = PlanLockDefine.REQUEST_MAPPING)
public class PlanLockController extends BaseController {

	@Autowired
	private PlanLockService planLockService;

	@Autowired
	private DebtBorrowCommonService debtborrowCommonService;

	@Autowired
	private DebtBorrowService debtBorrowService;

	@Autowired
	private PlanAssignCreditService planCreditTenderService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PlanLockDefine.INIT)
	@RequiresPermissions(PlanLockDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute("PlanLockBean") PlanLockBean form) {
		LogUtil.startLog(PlanLockController.class.toString(), PlanLockDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(PlanLockDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(PlanLockController.class.toString(), PlanLockDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面迁移
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PlanLockDefine.PREVIEW_ACTION)
	@RequiresPermissions(PlanLockDefine.PERMISSIONS_PREVIEW)
	public ModelAndView previewAction(HttpServletRequest request,
			@ModelAttribute(PlanLockDefine.PLAN_FORM) PlanLockBean form) {
		LogUtil.startLog(PlanLockController.class.toString(), PlanLockDefine.PREVIEW_ACTION);
		ModelAndView modelAndView = new ModelAndView(PlanLockDefine.PREVIEW_PATH);
		modelAndView.addObject("previewUrl", CustomConstants.HOST + "/plan/getPlanPreview.do?planNid=" + form.getPlanNidSrch());
		LogUtil.endLog(PlanLockController.class.toString(), PlanLockDefine.INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PlanLockDefine.SEARCH_ACTION)
	@RequiresPermissions(PlanLockDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, PlanLockBean form) {
		LogUtil.startLog(PlanLockController.class.toString(), PlanLockDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(PlanLockDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(PlanLockController.class.toString(), PlanLockDefine.INIT);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, PlanLockBean form) {

		PlanCommonCustomize planCommonCustomize = new PlanCommonCustomize();
		// 计划编码
		planCommonCustomize.setPlanNidSrch(form.getPlanNidSrch());
		// 计划状态 0 发起中；1 待审核；2审核不通过；3待开放；4募集中；5锁定中；6清算中；7清算完成，8未还款，9还款中，10还款完成
		planCommonCustomize.setPlanStatusSrch("5");
		// 满标/到期时间
		planCommonCustomize.setFullExpireTime(form.getFullExpireTime());
		planCommonCustomize.setFullExpireTimeEnd(form.getFullExpireTimeEnd());
		// 应清算时间
		planCommonCustomize.setLiquidateShouldTime(form.getLiquidateShouldTime());
		planCommonCustomize.setLiquidateShouldTimeEnd(form.getLiquidateShouldTimeEnd());
		planCommonCustomize.setSort(form.getSort());
		planCommonCustomize.setCol(form.getCol());

		int count = planLockService.countPlan(planCommonCustomize);
		if (count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			planCommonCustomize.setLimitStart(paginator.getOffset());
			planCommonCustomize.setLimitEnd(paginator.getLimit());
			List<DebtPlan> recordList = planLockService.selectPlanList(planCommonCustomize);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}
		modelAndView.addObject(PlanLockDefine.PLAN_FORM, form);
	}

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(PlanLockDefine.EXPORT_ACTION)
	@RequiresPermissions(PlanLockDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, PlanLockBean form)
			throws Exception {
		LogUtil.startLog(PlanLockController.class.toString(), PlanLockDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "锁定中计划列表";

		PlanCommonCustomize planCommonCustomize = new PlanCommonCustomize();
		// 计划编码
		planCommonCustomize.setPlanNidSrch(form.getPlanNidSrch());
		// 计划状态 发起中；待审核；审核不通过；待开放；募集中；募集完成；锁定中；清算中；还款中；已还款；已流标
		planCommonCustomize.setPlanStatusSrch("5");
		// 满标/到期时间
		planCommonCustomize.setFullExpireTime(form.getFullExpireTime());
		planCommonCustomize.setFullExpireTimeEnd(form.getFullExpireTimeEnd());
		// 应清算时间
		planCommonCustomize.setLiquidateShouldTime(form.getLiquidateShouldTime());
		planCommonCustomize.setLiquidateShouldTimeEnd(form.getLiquidateShouldTimeEnd());

		List<DebtPlan> resultList = planLockService.exportPlanList(planCommonCustomize);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date())
				+ CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "序号", "智投编号", "智投名称", "智投类型", "授权服务金额", "服务回报期限", "参考年回报率", "当前年化", "授权服务金额", "可用金额", "冻结金额", "满标/到期时间", "清算日期" };
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
					sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles,
							(sheetName + "_第" + sheetCount + "页"));
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
						cell.setCellValue(debtPlan.getDebtPlanMoney().toString());
					}
					// 锁定期
					else if (celLength == 5) {
						cell.setCellValue(debtPlan.getDebtLockPeriod());
					}
					// 预期年化
					else if (celLength == 6) {
						cell.setCellValue(debtPlan.getExpectApr() == null ? "0" : CustomConstants.DF_FOR_VIEW.format(debtPlan.getExpectApr()) + "%");
					}
					// 当前年化
					else if (celLength == 7) {
						cell.setCellValue(debtPlan.getActualApr() == null ? "0" : CustomConstants.DF_FOR_VIEW.format(debtPlan.getActualApr()) + "%");
					}
					// 加入金额
					else if (celLength == 8) {
						cell.setCellValue(debtPlan.getDebtPlanMoneyYes() == null ? "0"
								: CustomConstants.DF_FOR_VIEW.format(debtPlan.getDebtPlanMoneyYes()));
					}
					// 可使用金额
					else if (celLength == 9) {
						cell.setCellValue(debtPlan.getDebtPlanBalance() == null ? "0"
								: CustomConstants.DF_FOR_VIEW.format(debtPlan.getDebtPlanBalance()));
					}
					// 冻结金额
					else if (celLength == 10) {
						cell.setCellValue(debtPlan.getDebtPlanFrost() == null ? "0"
								: CustomConstants.DF_FOR_VIEW.format(debtPlan.getDebtPlanFrost()));
					}
					// 满标/到期时间
					else if (celLength == 11) {
						if (debtPlan.getFullExpireTime() == 0) {
							cell.setCellValue(GetDate.timestamptoStrYYYYMMDDHHMMSS(debtPlan.getBuyEndTime()));
						} else {
							cell.setCellValue(GetDate.timestamptoStrYYYYMMDDHHMMSS(debtPlan.getFullExpireTime()));
						}

					}
					// 清算日期
					else if (celLength == 12) {
						cell.setCellValue(GetDate.timestamptoStrYYYYMMDDHHMMSS(debtPlan.getLiquidateShouldTime()));
					}

				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(PlanLockController.class.toString(), PlanLockDefine.EXPORT_ACTION);
	}

	/**
	 * 详情页面
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PlanLockDefine.INFO_ACTION)
	@RequiresPermissions(PlanLockDefine.PERMISSIONS_VIEW)
	public ModelAndView infoAction(HttpServletRequest request, @ModelAttribute("PlanLockBean") PlanLockBean form) {
		LogUtil.startLog(PlanLockController.class.toString(), PlanLockDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(PlanLockDefine.INFO_PATH);
		// 创建分页
		this.createInfoPage(request, modelAndView, form);
		LogUtil.endLog(PlanLockController.class.toString(), PlanLockDefine.INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PlanLockDefine.INFO_SEARCH_ACTION)
	@RequiresPermissions(PlanLockDefine.PERMISSIONS_SEARCH)
	public ModelAndView infoSearch(HttpServletRequest request, PlanLockBean form) {
		LogUtil.startLog(PlanLockController.class.toString(), PlanLockDefine.INFO_SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(PlanLockDefine.INFO_PATH);

		// 创建分页
		this.createInfoPage(request, modelAndView, form);
		LogUtil.endLog(PlanLockController.class.toString(), PlanLockDefine.INFO_SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createInfoPage(HttpServletRequest request, ModelAndView modelAndView, PlanLockBean form) {
		// 区分是否是加入明细里的手动出借列表
		String accedeOrderId = form.getAccedeOrderId();
		// 区分加入明细，债权明细，回款明细
		String type = form.getType();
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
		// 项目编号
		planCommonCustomize.setBorrowNid(form.getBorrowNidSrch());
		// 项目订单号
		planCommonCustomize.setPlanOrderId(form.getPlanOrderId());
		// 项目类型
		planCommonCustomize.setProjectType(form.getProjectTypeSrch());
		// 还款方式
		planCommonCustomize.setRepayType(form.getBorrowStyleSrch());
		// 回款状态
		planCommonCustomize.setRepayStatus(form.getRepayStatus());
		// 应回款日期开始
		planCommonCustomize.setRepayTimeStart(form.getRepayTimeStart());
		// 应回款日期结束
		planCommonCustomize.setRepayTimeEnd(form.getRepayTimeEnd());
		planCommonCustomize.setSort(form.getSort());
		planCommonCustomize.setCol(form.getCol());
		// 计划基本信息
		List<DebtPlan> planList = planLockService.selectPlanList(planCommonCustomize);
		// 加入明细外面页面
		if (planList != null && planList.size() > 0) {
			modelAndView.addObject("plan", planList.get(0));

			HashMap<String, Object> planCountMap = planLockService.selectPlanCountMap(form.getPlanNidSrch());
			if (planCountMap != null) {
				// 加入订单数
				modelAndView.addObject("accedeCount", planCountMap.get("accedeCount"));
				// 持有专属资产笔数
				modelAndView.addObject("investCount", planCountMap.get("investCount"));
				// 持有专属资产本金
				modelAndView.addObject("account", planCountMap.get("account"));
				// 持有债权笔数
				modelAndView.addObject("creditTenderCount", planCountMap.get("creditTenderCount"));
				// 持有债权本金
				modelAndView.addObject("creditCapital", planCountMap.get("creditCapital"));
				// 实际支付
				modelAndView.addObject("assignPay", planCountMap.get("assignPay"));
			}

			DebtPlan plan = planList.get(0);
			// 预期到期出借利率率
			String expireApr = this.planLockService.calculationPlanExpectApr(plan);
			modelAndView.addObject("expireApr", expireApr);
			// 项目类型
			List<BorrowProjectType> borrowProjectTypeList = this.debtborrowCommonService.borrowProjectTypeList("");
			modelAndView.addObject("borrowProjectTypeList", borrowProjectTypeList);
			// 还款方式
			List<BorrowStyle> borrowStyleList = this.debtborrowCommonService.borrowStyleList("");
			modelAndView.addObject("borrowStyleList", borrowStyleList);
			if ((StringUtils.isEmpty(type) || type.equals("0"))) {
				if (StringUtils.isNotEmpty(accedeOrderId)) {
					// 加入明细内页(手动出借)
					planCommonCustomize.setAccedeOrderId(accedeOrderId);
					planCommonCustomize.setCol(null);
					planCommonCustomize.setSort(null);
					List<PlanLockCustomize> recordList = planLockService.selectPlanAccedeList(planCommonCustomize);
					if (recordList != null && recordList.size() > 0) {
						PlanLockCustomize planLockCustomize = recordList.get(0);
						modelAndView.addObject("planLockCustomize", planLockCustomize);
					}
					DebtBorrowCommonCustomize corrowCommonCustomize = new DebtBorrowCommonCustomize();
					// 项目状态 出借中12
					corrowCommonCustomize.setStatusSrch("12");
					corrowCommonCustomize.setBorrowNidSrch(form.getBorrowNidSrch());
					// 项目类型
					corrowCommonCustomize.setProjectTypeSrch(form.getProjectTypeSrch());
					// 还款方式
					corrowCommonCustomize.setBorrowStyleSrch(form.getBorrowStyleSrch());
					if (StringUtils.isNotEmpty(form.getCol()) && form.getCol().contains("dpa.")) {
						corrowCommonCustomize.setSort(null);
						corrowCommonCustomize.setCol(null);
					} else {
						corrowCommonCustomize.setSort(form.getSort());
						corrowCommonCustomize.setCol(form.getCol());
					}

					Long count = this.debtBorrowService.countBorrow(corrowCommonCustomize);
					if (count != null && count > 0) {
						Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
						corrowCommonCustomize.setLimitStart(paginator.getOffset());
						corrowCommonCustomize.setLimitEnd(paginator.getLimit());
						List<DebtBorrowCustomize> borrowList = this.debtBorrowService.selectBorrowList(corrowCommonCustomize);
						form.setPaginator(paginator);
						modelAndView.addObject("borrowList", borrowList);
					}

				} else {
					// 加入明细
					Long count = planLockService.countPlanAccede(planCommonCustomize);
					if (count > 0) {
						// 计划总额
						HashMap<String, Object> planLockSumMap = planLockService.planLockSumMap(planCommonCustomize);
						modelAndView.addObject("joinMoney", planLockSumMap.get("joinMoney"));
						modelAndView.addObject("orderMoney", planLockSumMap.get("orderMoney"));
						modelAndView.addObject("frostMoney", planLockSumMap.get("frostMoney"));
						modelAndView.addObject("ardMoney", planLockSumMap.get("ardMoney"));
						Paginator paginator = new Paginator(form.getPaginatorPage(), count.intValue());
						planCommonCustomize.setLimitStart(paginator.getOffset());
						planCommonCustomize.setLimitEnd(paginator.getLimit());
						List<PlanLockCustomize> recordList = planLockService.selectPlanAccedeList(planCommonCustomize);
						form.setPaginator(paginator);
						modelAndView.addObject("recordList", recordList);
					}
				}

			} else if ("1".equals(type) && StringUtils.isEmpty(accedeOrderId)) {
				// 债权明细
				Long count = planLockService.countPlanInvestNew(planCommonCustomize);
				if (count > 0) {
					// 债权总额
					HashMap<String, Object> planInvestSumMap = planLockService.planInvestSumMapNew(planCommonCustomize);
					modelAndView.addObject("accountSum", planInvestSumMap.get("accountSum"));
					modelAndView.addObject("fairValueSum", planInvestSumMap.get("fairValueSum"));
					// 到期公允价值求和
					modelAndView.addObject("expireFairValueSum", planInvestSumMap.get("expireFairValueSum"));
					Paginator paginator = new Paginator(form.getPaginatorPage(), count.intValue());
					planCommonCustomize.setLimitStart(paginator.getOffset());
					planCommonCustomize.setLimitEnd(paginator.getLimit());
					List<PlanInvestCustomize> debtInvestList = planLockService.selectPlanInvestListNew(planCommonCustomize);
					form.setPaginator(paginator);
					modelAndView.addObject("debtInvestList", debtInvestList);
				}

			} else if ("2".equals(type) && StringUtils.isEmpty(accedeOrderId)) {
				// 回款明细
				Long count = planLockService.countLoanDetailNew(planCommonCustomize);
				if (count > 0) {
					// 债权总额
					HashMap<String, Object> planLoanSumMap = planLockService.LoanDeailSumMapNew(planCommonCustomize);
					modelAndView.addObject("loanCapitalSum", planLoanSumMap.get("loanCapitalSum"));
					modelAndView.addObject("loanInterestSum", planLoanSumMap.get("loanInterestSum"));
					modelAndView.addObject("loanAccountSum", planLoanSumMap.get("loanAccountSum"));
					modelAndView.addObject("repayAccountSum", planLoanSumMap.get("repayAccountSum"));
					Paginator paginator = new Paginator(form.getPaginatorPage(), count.intValue());
					planCommonCustomize.setLimitStart(paginator.getOffset());
					planCommonCustomize.setLimitEnd(paginator.getLimit());
					List<Map<String, Object>> debtLoanList = planLockService.selectLoanDetailListNew(planCommonCustomize);
					form.setPaginator(paginator);
					modelAndView.addObject("debtLoanList", debtLoanList);
				}
			} else if ("3".equals(type)) {
				if (StringUtils.isNotEmpty(accedeOrderId)) {
					// 加入明细内页(手动出借)
					planCommonCustomize.setAccedeOrderId(accedeOrderId);
					planCommonCustomize.setCol(null);
					planCommonCustomize.setSort(null);
					List<PlanLockCustomize> recordList = planLockService.selectPlanAccedeList(planCommonCustomize);
					if (recordList != null && recordList.size() > 0) {
						PlanLockCustomize planLockCustomize = recordList.get(0);
						modelAndView.addObject("planLockCustomize", planLockCustomize);
					}
					DebtBorrowCommonCustomize corrowCommonCustomize = new DebtBorrowCommonCustomize();
					// 项目状态 出借中12
					corrowCommonCustomize.setStatusSrch("12");
					corrowCommonCustomize.setBorrowNidSrch(form.getBorrowNidSrch());
					// 项目类型
					corrowCommonCustomize.setProjectTypeSrch(form.getProjectTypeSrch());
					// 还款方式
					corrowCommonCustomize.setBorrowStyleSrch(form.getBorrowStyleSrch());
					corrowCommonCustomize.setSort(form.getSort());
					corrowCommonCustomize.setCol(form.getCol());

					Map<String, Object> param = new HashMap<String, Object>();
					// 还款方式
					String borrowStyle = form.getBorrowStyleSrch();
					if (StringUtils.isNotEmpty(borrowStyle)) {
						param.put("borrowStyleSrch", borrowStyle);
					}
					// 排序
					if (StringUtils.isNotEmpty(form.getSort())) {
						param.put("sort", form.getSort());
					}
					if (StringUtils.isNotEmpty(form.getCol())) {
						param.put("col", form.getCol());
					}
					// 检索债转项目的件数
					int count = this.planLockService.countCreditProject(param);
					if (count > 0) {
						Paginator paginator = new Paginator(form.getPaginatorPage(), count);
						param.put("limitStart", paginator.getOffset());
						param.put("limitEnd", paginator.getLimit());
						List<DebtAdminCreditCustomize> creditProjectList = this.planLockService.selectDebtCreditProject(param);
						form.setPaginator(paginator);
						modelAndView.addObject("creditProjectList", creditProjectList);
					}

				} else {
					// 加入明细
					Long count = planLockService.countPlanAccede(planCommonCustomize);
					if (count > 0) {
						// 计划总额
						HashMap<String, Object> planLockSumMap = planLockService.planLockSumMap(planCommonCustomize);
						modelAndView.addObject("joinMoney", planLockSumMap.get("joinMoney"));
						modelAndView.addObject("orderMoney", planLockSumMap.get("orderMoney"));
						modelAndView.addObject("frostMoney", planLockSumMap.get("frostMoney"));
						modelAndView.addObject("ardMoney", planLockSumMap.get("ardMoney"));
						Paginator paginator = new Paginator(form.getPaginatorPage(), count.intValue());
						planCommonCustomize.setLimitStart(paginator.getOffset());
						planCommonCustomize.setLimitEnd(paginator.getLimit());
						List<PlanLockCustomize> recordList = planLockService.selectPlanAccedeList(planCommonCustomize);
						form.setPaginator(paginator);
						modelAndView.addObject("recordList", recordList);
					}
				}

			}
		}
		modelAndView.addObject(PlanLockDefine.PLAN_FORM, form);
	}

	/**
	 * 出借跳转画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PlanLockDefine.PLANINVEST)
	@RequiresPermissions(PlanLockDefine.PERMISSIONS_VIEW)
	public ModelAndView planInvest(HttpServletRequest request, String borrowNid, String accedeOrderId) {
		LogUtil.startLog(PlanLockController.class.toString(), PlanLockDefine.PLANINVEST);
		ModelAndView modelAndView = new ModelAndView(PlanLockDefine.INVEST_PATH);
		modelAndView.addObject("borrowNid", borrowNid);
		modelAndView.addObject("accedeOrderId", accedeOrderId);
		if (borrowNid != null) {
			DebtBorrowCommonCustomize corrowCommonCustomize = new DebtBorrowCommonCustomize();
			// 借款编码
			corrowCommonCustomize.setBorrowNidSrch(borrowNid);
			List<DebtBorrowCustomize> borrowList = this.debtBorrowService.selectBorrowList(corrowCommonCustomize);
			if (borrowList != null && borrowList.size() > 0) {
				DebtBorrowCustomize borrowCustomize = borrowList.get(0);
				modelAndView.addObject("borrowCustomize", borrowCustomize);
			}
		}
		if (accedeOrderId != null) {
			PlanCommonCustomize planCommonCustomize = new PlanCommonCustomize();
			// 加入明细内页(手动出借)
			planCommonCustomize.setAccedeOrderId(accedeOrderId);
			List<PlanLockCustomize> recordList = planLockService.selectPlanAccedeList(planCommonCustomize);
			if (recordList != null && recordList.size() > 0) {
				PlanLockCustomize planLockCustomize = recordList.get(0);
				modelAndView.addObject("planLockCustomize", planLockCustomize);
			}
		}
		LogUtil.endLog(PlanLockController.class.toString(), PlanLockDefine.PLANINVEST);
		return modelAndView;
	}

	/**
	 * 出借
	 * 
	 * @param request
	 * @param form
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(PlanLockDefine.INVESTNOW)
	@RequiresPermissions(PlanLockDefine.PERMISSIONS_VIEW)
	public ModelAndView investNow(HttpServletRequest request, String borrowNid, String accedeOrderId, String money) throws Exception {
		LogUtil.startLog(PlanLockController.class.toString(), PlanLockDefine.PLANINVEST);
		// 回跳详情页面
		ModelAndView modelAndView = new ModelAndView();
		if (StringUtils.isEmpty(borrowNid) || StringUtils.isEmpty(accedeOrderId)) {
			modelAndView.setViewName(PlanLockDefine.INVEST_FALSE_PATH);
			modelAndView.addObject("error", "参数不能为空");
			return modelAndView;
		}
		modelAndView.addObject("borrowNid", borrowNid);
		modelAndView.addObject("accedeOrderId", accedeOrderId);
		if (StringUtils.isEmpty(money) && !Validator.isNumber(money)) {
			modelAndView.setViewName(PlanLockDefine.INVEST_FALSE_PATH);
			modelAndView.addObject("error", "请填写正确的授权服务金额");
			return modelAndView;
		}
		BigDecimal investNum = new BigDecimal(money);
		if (investNum.compareTo(BigDecimal.ZERO) < 1) {
			modelAndView.setViewName(PlanLockDefine.INVEST_FALSE_PATH);
			modelAndView.addObject("error", "授权服务金额需大于0");
			return modelAndView;
		}
		String userId = null;
		PlanCommonCustomize planCommonCustomize = new PlanCommonCustomize();
		// 加入明细内页(手动出借)
		planCommonCustomize.setAccedeOrderId(accedeOrderId);
		List<PlanLockCustomize> recordList = planLockService.selectPlanAccedeList(planCommonCustomize);
		if (recordList != null && recordList.size() > 0) {
			PlanLockCustomize planLockCustomize = recordList.get(0);
			modelAndView.addObject("planLockCustomize", planLockCustomize);
			userId = planLockCustomize.getUserId();
		}
		JSONObject result = planLockService.checkParamAppointment(borrowNid, money, Integer.parseInt(userId));
		if (result == null) {
			modelAndView.setViewName(PlanLockDefine.INVEST_FALSE_PATH);
			modelAndView.addObject("error", "出借验证失败");
			return modelAndView;
		} else if (result.get("error") != null && result.get("error").equals("1")) {
			modelAndView.setViewName(PlanLockDefine.INVEST_FALSE_PATH);
			modelAndView.addObject("error", result.get("data") + "");
			return modelAndView;
		}
		// TODO 调用出借接口进行自动投标
		List<DebtPlanAccede> debtPlanAccedes = planLockService.getDebtPlanAccedes(accedeOrderId);
		if (debtPlanAccedes != null && debtPlanAccedes.size() > 0) {
			DebtPlanAccede debtPlanAccede = debtPlanAccedes.get(0);
			if (debtPlanAccede.getAccedeBalance().compareTo(investNum) == -1) {
				modelAndView.setViewName(PlanLockDefine.INVEST_FALSE_PATH);
				modelAndView.addObject("error", "此笔加入可用余额不足");
				return modelAndView;
			}
			DebtPlan debtPlan = this.planLockService.selectDebtPlanInfoByPlanNid(debtPlanAccede.getPlanNid());
			// 计划最小余额
			BigDecimal minSurplusInvestAccount = debtPlan.getMinSurplusInvestAccount();
			// 调用出借方法进行出借
			boolean investFlag = this.planLockService.tender(debtPlanAccede, borrowNid, investNum,minSurplusInvestAccount);
			if (investFlag) {
				modelAndView.setViewName(PlanLockDefine.INVEST_SUCCESS_PATH);
				modelAndView.addObject("success", "出借成功，智投订单号：" + accedeOrderId + ",项目编号：" + borrowNid);
			} else {
				modelAndView.setViewName(PlanLockDefine.INVEST_FALSE_PATH);
				modelAndView.addObject("error", "出借失败，智投订单号：" + accedeOrderId + ",项目编号：" + borrowNid);
			}
		}

		LogUtil.endLog(PlanLockController.class.toString(), PlanLockDefine.PLANINVEST);
		return modelAndView;
	}

	/**
	 * 债转出借跳转
	 * 
	 * @Title creditTenderInfoAction
	 * @param request
	 * @param creditNid
	 * @param accedeOrderId
	 * @return
	 */
	@RequestMapping(PlanLockDefine.CREDIT_TENDER_INFO_ACTION)
	@RequiresPermissions(PlanLockDefine.PERMISSIONS_VIEW)
	public ModelAndView creditTenderInfoAction(HttpServletRequest request, String creditNid, String accedeOrderId) {
		LogUtil.startLog(PlanLockController.class.toString(), PlanLockDefine.CREDIT_TENDER_INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(PlanLockDefine.CREDIT_TENDER_PATH);
		modelAndView.addObject("creditNid", creditNid);
		modelAndView.addObject("accedeOrderId", accedeOrderId);
		if (creditNid != null) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("creditNidSrch", creditNid);
			List<DebtAdminCreditCustomize> creditProjectList = this.planLockService.selectDebtCreditProject(param);
			if (creditProjectList != null && creditProjectList.size() > 0) {
				DebtAdminCreditCustomize creditProjectInfo = creditProjectList.get(0);
				modelAndView.addObject("creditProjectInfo", creditProjectInfo);
			}
		}
		if (accedeOrderId != null) {
			PlanCommonCustomize planCommonCustomize = new PlanCommonCustomize();
			// 加入明细内页(手动出借)
			planCommonCustomize.setAccedeOrderId(accedeOrderId);
			List<PlanLockCustomize> recordList = planLockService.selectPlanAccedeList(planCommonCustomize);
			if (recordList != null && recordList.size() > 0) {
				PlanLockCustomize planLockCustomize = recordList.get(0);
				modelAndView.addObject("planLockCustomize", planLockCustomize);
			}
		}
		LogUtil.endLog(PlanLockController.class.toString(), PlanLockDefine.CREDIT_TENDER_INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 购买债权
	 * 
	 * @Title creditTenderAction
	 * @param request
	 * @param creditNid
	 * @param accedeOrderId
	 * @param money
	 * @return
	 */
	@RequestMapping(PlanLockDefine.CREDIT_TENDER_ACTION)
	@RequiresPermissions(PlanLockDefine.PERMISSIONS_VIEW)
	public ModelAndView creditTenderAction(HttpServletRequest request, String creditNid, String accedeOrderId, String money) {
		// 回跳详情页面
		ModelAndView modelAndView = new ModelAndView();
		if (StringUtils.isEmpty(creditNid) || StringUtils.isEmpty(accedeOrderId)) {
			modelAndView.setViewName(PlanLockDefine.INVEST_FALSE_PATH);
			modelAndView.addObject("error", "参数不能为空");
			return modelAndView;
		}
		modelAndView.addObject("creditNid", creditNid);
		modelAndView.addObject("accedeOrderId", accedeOrderId);
		if (StringUtils.isEmpty(money) && !Validator.isNumber(money)) {
			modelAndView.setViewName(PlanLockDefine.INVEST_FALSE_PATH);
			modelAndView.addObject("error", "请填写正确的授权服务金额");
			return modelAndView;
		}
		BigDecimal investNum = new BigDecimal(money);
		if (investNum.compareTo(BigDecimal.ZERO) < 1) {
			modelAndView.setViewName(PlanLockDefine.INVEST_FALSE_PATH);
			modelAndView.addObject("error", "授权服务金额需大于0");
			return modelAndView;
		}

		List<DebtPlanAccede> debtPlanAccedes = planLockService.getDebtPlanAccedes(accedeOrderId);
		if (debtPlanAccedes != null && debtPlanAccedes.size() > 0) {
			DebtPlanAccede debtPlanAccede = debtPlanAccedes.get(0);
			if (debtPlanAccede.getAccedeBalance().compareTo(investNum) == -1) {
				modelAndView.setViewName(PlanLockDefine.INVEST_FALSE_PATH);
				modelAndView.addObject("error", "此笔加入可用余额不足");
				return modelAndView;
			}
			// 根据债转编号检索债转
			DebtCredit debtCredit = this.planCreditTenderService.selectCreditListByCreditNid(creditNid);
			if (debtCredit == null) {
				modelAndView.setViewName(PlanLockDefine.INVEST_FALSE_PATH);
				modelAndView.addObject("error", "查询不到债转项目!");
				return modelAndView;
			}
			// TODO调用债转承接
			boolean investFlag = false;
			// 检索计划详情
			DebtPlan planInfo = this.planLockService.selectDebtPlanInfoByPlanNid(debtPlanAccede.getPlanNid());
			investFlag = this.planCreditTenderService.assignCredit(debtCredit, debtPlanAccede, investNum, planInfo.getExpectApr(),planInfo.getMinSurplusInvestAccount());
			if (investFlag) {
				modelAndView.setViewName(PlanLockDefine.INVEST_SUCCESS_PATH);
				modelAndView.addObject("success", "出借成功，智投订单号：" + accedeOrderId + ",债转编号：" + debtCredit.getCreditNid());
			} else {
				modelAndView.setViewName(PlanLockDefine.INVEST_FALSE_PATH);
				modelAndView.addObject("error", "出借失败，智投订单号：" + accedeOrderId + ",债转编号：" + debtCredit.getCreditNid());
			}

		}
		return modelAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PlanLockDefine.UPDATE_ACTION)
	@RequiresPermissions(PlanLockDefine.PERMISSIONS_MODIFY)
	@ResponseBody
	public Map<String, String> updateAction(HttpServletRequest request, String accedeorderid) {
		LogUtil.startLog(PlanLockController.class.toString(), PlanLockDefine.UPDATE_ACTION);
		Map<String, String> map = new HashMap<String, String>();
		if (StringUtils.isNotEmpty(accedeorderid)) {
			int status = planLockService.updateCycleTimesZero(accedeorderid);
			if (status > 0) {
				map.put("status", "success");
				map.put("result", "清零成功");
			} else {
				map.put("status", "false");
				map.put("result", "更新失败 影响条数" + status);
			}

		} else {
			map.put("status", "false");
			map.put("result", "accedeorderId参数不能为null");
		}

		LogUtil.endLog(PlanLockController.class.toString(), PlanLockDefine.UPDATE_ACTION);
		return map;
	}

}
