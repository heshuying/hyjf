package com.hyjf.admin.manager.plan.repaydetail;

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
import com.hyjf.admin.manager.plan.planlock.PlanLockService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.customize.PlanCommonCustomize;
import com.hyjf.mybatis.model.customize.PlanLockCustomize;
/**
 * 
 * @author: zhouxiaoshuai
 * @email: 	287424494@qq.com		
* @description:计划所用控制器
 * @version:     1 
 * @date:   2016年9月12日 下午2:50:16
 */
@Controller
@RequestMapping(value = RepayDetailDefine.REQUEST_MAPPING)
public class RepayDetailController extends BaseController {


	@Autowired
	private PlanLockService planLockService;
	


	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(RepayDetailDefine.INIT)
	@RequiresPermissions(RepayDetailDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute("RepayDetailBean") RepayDetailBean form) {
		LogUtil.startLog(RepayDetailController.class.toString(), RepayDetailDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(RepayDetailDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(RepayDetailController.class.toString(), RepayDetailDefine.INIT);
		return modelAndView;
	}

	
	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(RepayDetailDefine.SEARCH_ACTION)
	@RequiresPermissions(RepayDetailDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, RepayDetailBean form) {
		LogUtil.startLog(RepayDetailController.class.toString(), RepayDetailDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(RepayDetailDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(RepayDetailController.class.toString(), RepayDetailDefine.INIT);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, RepayDetailBean form) {
		PlanCommonCustomize planCommonCustomize = new PlanCommonCustomize();
		// 计划编码
		planCommonCustomize.setPlanNidSrch(form.getPlanNidSrch());
		//  计划订单号
		planCommonCustomize.setPlanOrderId(form.getPlanOrderId());
		// 用户名
		planCommonCustomize.setUserName(form.getUserName());
		//清算开始时间
		planCommonCustomize.setLiquidateShouldTime(form.getLiquidateShouldTime());
		//清算结束时间
		planCommonCustomize.setLiquidateShouldTimeEnd(form.getLiquidateShouldTimeEnd());
		if (form.getRepayStatus()==null||form.getRepayStatus().equals("")) {
			//还款状态 0出借中 1出借完成 2清算中 3清算完成 4还款中 5还款完成 
			planCommonCustomize.setRepayStatus("20");
		}else {
			//还款状态 
			planCommonCustomize.setRepayStatus(form.getRepayStatus());
		}
	
		//应还款日期开始
		planCommonCustomize.setRepayTimeStart(form.getRepayTimeStart());
		//应还款日期结束
		planCommonCustomize.setRepayTimeEnd(form.getRepayTimeEnd());
		//还款明细
		Long count = planLockService.countPlanAccede(planCommonCustomize);
		if (count > 0) {
			//计划总额
			HashMap<String , Object> planLockSumMap=planLockService.planLockSumMap(planCommonCustomize);
			/* sum(dpa.repay_capital) repayCapital,
			 sum(dpa.repay_interest) repayInterest,
			 sum(dpa.repay_interest_wait) repayInterestWait,
			 sum(dpa.repay_account) repayAccount,*/
			modelAndView.addObject("joinMoney", planLockSumMap.get("joinMoney"));
			modelAndView.addObject("orderMoney", planLockSumMap.get("orderMoney"));
			modelAndView.addObject("frostMoney", planLockSumMap.get("frostMoney"));
			modelAndView.addObject("ardMoney", planLockSumMap.get("ardMoney"));//yes
			modelAndView.addObject("repayCapital", planLockSumMap.get("repayCapital"));
			modelAndView.addObject("repayCapitalYes", planLockSumMap.get("repayCapitalYes"));
			modelAndView.addObject("repayInterest", planLockSumMap.get("repayInterest"));
			modelAndView.addObject("repayInterestWait", planLockSumMap.get("repayInterestWait"));
			modelAndView.addObject("repayAccount", planLockSumMap.get("repayAccount"));
			modelAndView.addObject("repayInterestFact", planLockSumMap.get("repayInterestFact"));
			modelAndView.addObject("repayAccountFact", planLockSumMap.get("repayAccountFact"));
			if (form.getPaginatorPage()==0) {
				form.setPaginatorPage(1);
			}
			Paginator paginator = new Paginator(form.getPaginatorPage(), count.intValue());
			planCommonCustomize.setLimitStart(paginator.getOffset());
			planCommonCustomize.setLimitEnd(paginator.getLimit());
			List<PlanLockCustomize> recordList = planLockService.selectPlanAccedeList(planCommonCustomize);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}	
		modelAndView.addObject(RepayDetailDefine.CREDIT_FORM, form);
	}

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(RepayDetailDefine.EXPORT_ACTION)
	@RequiresPermissions(RepayDetailDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, RepayDetailBean form) throws Exception {
		LogUtil.startLog(RepayDetailController.class.toString(), RepayDetailDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "还款明细";
		PlanCommonCustomize planCommonCustomize = new PlanCommonCustomize();
		// 计划编码
		planCommonCustomize.setPlanNidSrch(form.getPlanNidSrch());
		//  计划订单号
		planCommonCustomize.setPlanOrderId(form.getPlanOrderId());
		// 用户名
		planCommonCustomize.setUserName(form.getUserName());
		//清算开始时间
		planCommonCustomize.setLiquidateShouldTime(form.getLiquidateShouldTime());
		//清算结束时间
		planCommonCustomize.setLiquidateShouldTimeEnd(form.getLiquidateShouldTimeEnd());
		if (form.getRepayStatus()==null||form.getRepayStatus().equals("")) {
			//还款状态 0出借中 1出借完成 2还款中 3还款完成 不是3  就是未还款
			planCommonCustomize.setRepayStatus("20");
		}else {
			//还款状态 0出借中 1出借完成 2还款中 3还款完成 不是3  就是未还款
			planCommonCustomize.setRepayStatus(form.getRepayStatus());
		}
		//应还款日期开始
		planCommonCustomize.setRepayTimeStart(form.getRepayTimeStart());
		//应还款日期结束
		planCommonCustomize.setRepayTimeEnd(form.getRepayTimeEnd());
		List<PlanLockCustomize> recordList = planLockService.selectPlanAccedeList(planCommonCustomize);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "序号", "智投编号","智投订单号", "冻结订单号",  "用户名", "授权服务金额",
											"应还本金", "应还总利息", "已派利息", "最后应还利息", 
										"应还总额", "实际还款本金", "实际还款利息", "实际还款总额", "还款状态", "清算时间","最晚应还时间"};
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
					// 计划编号
					else if (celLength == 1) {
						cell.setCellValue(planLockCustomize.getDebtPlanNid());
					}
					// 计划订单号
					else if (celLength == 2) {
						cell.setCellValue(planLockCustomize.getAccedeOrderId());
					}
					// 冻结订单号
					else if (celLength == 3) {
						cell.setCellValue(planLockCustomize.getFreezeOrderId());
					}
					// 用户名
					else if (celLength == 4) {
						cell.setCellValue(planLockCustomize.getUserName());
					}
					// 出借/承接订单号
					else if (celLength == 5) {
						cell.setCellValue(planLockCustomize.getAccedeAccount());
					}
					// 应还款本金
					else if (celLength == 6) {
						cell.setCellValue(planLockCustomize.getRepayCapital() );
					}
					// 应还总利息
					else if (celLength == 7) {
						cell.setCellValue(planLockCustomize.getRepayInterest());
					}
					// 已派利息
					else if (celLength == 8) {
						cell.setCellValue(planLockCustomize.getRepayInterestYes() );
					}
					// 最后应还利息
					else if (celLength == 9) {
						cell.setCellValue(planLockCustomize.getRepayInterestFact());
					}
					// 应还款总额
					else if (celLength == 10) {
						cell.setCellValue(planLockCustomize.getRepayAccount());
					}
					// 实际还款本金
					else if (celLength == 11) {
						cell.setCellValue(planLockCustomize.getRepayCapitalYes() );
					}
					// 实际回款利息
					else if (celLength == 12) {
						cell.setCellValue(planLockCustomize.getRepayInterestYes());
					}
					// 实际回款总额
					else if (celLength == 13) {
						cell.setCellValue(planLockCustomize.getRepayAccountFact());
					}
					// 此笔加入是否已经完成 0出借中 1出借完成 2还款中 3还款完成
					else if (celLength == 14) {
						if (planLockCustomize.getStatus().equals("0")||planLockCustomize.getStatus().equals("1")) {
							cell.setCellValue("未还款");
						}else if (planLockCustomize.getStatus().equals("2")) {
							cell.setCellValue("还款中");
						}else if (planLockCustomize.getStatus().equals("3")) {
							cell.setCellValue("已还款");
						}
					}
					//清算时间
					else if (celLength == 15) {
						cell.setCellValue(planLockCustomize.getLiquidateShouldTime());
					}
					//最晚应还时间
					else if (celLength == 16) {
						cell.setCellValue(planLockCustomize.getLastRepayTime());
					}
					
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(RepayDetailController.class.toString(), RepayDetailDefine.EXPORT_ACTION);
	}
	

}
