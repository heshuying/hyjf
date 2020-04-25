package com.hyjf.admin.manager.borrow.credit;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.admin.manager.hjhplan.planlist.PlanListDefine;
import com.hyjf.common.calculate.DateUtils;
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
import com.hyjf.mybatis.model.customize.admin.HjhDebtCreditCustomize;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author wangkun
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = HjhDebtCreditDefine.REQUEST_MAPPING)
public class HjhDebtCreditController extends BaseController {

	@Autowired
	private HjhDebtCreditService debtCreditService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(HjhDebtCreditDefine.INIT_ACTION)
	@RequiresPermissions(HjhDebtCreditDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, HttpServletResponse response, @ModelAttribute(HjhDebtCreditDefine.HJH_CREDIT_FORM) HjhDebtCreditBean form) {
		LogUtil.startLog(HjhDebtCreditDefine.CONTROLLER_NAME, HjhDebtCreditDefine.INIT_ACTION);
		ModelAndView modeAndView = new ModelAndView(HjhDebtCreditDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(HjhDebtCreditDefine.CONTROLLER_NAME, HjhDebtCreditDefine.INIT_ACTION);
		return modeAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(HjhDebtCreditDefine.SEARCH_ACTION)
	@RequiresPermissions(HjhDebtCreditDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, HttpServletResponse response, HjhDebtCreditBean form) {
		LogUtil.startLog(HjhDebtCreditDefine.CONTROLLER_NAME, HjhDebtCreditDefine.SEARCH_ACTION);
		ModelAndView modeAndView = new ModelAndView(HjhDebtCreditDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(HjhDebtCreditDefine.CONTROLLER_NAME, HjhDebtCreditDefine.SEARCH_ACTION);
		return modeAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, HjhDebtCreditBean planCreditBean) {

		// 还款方式
		List<BorrowStyle> borrowStyleList = this.debtCreditService.selectBorrowStyleList("");
		modelAndView.addObject("borrowStyleList", borrowStyleList);
		// 转让状态
		modelAndView.addObject("creditStatusList", this.debtCreditService.getParamNameList(CustomConstants.HJH_DEBT_CREDIT_STATUS));
		// 债转还款状态
		modelAndView.addObject("repayStatusList", this.debtCreditService.getParamNameList(CustomConstants.HJH_DEBT_REPAY_STATUS));
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
		params.put("liquidatesTimeStart", StringUtils.isNotBlank(planCreditBean.getLiquidatesTimeStart())?planCreditBean.getLiquidatesTimeStart():null);
		params.put("liquidatesTimeEnd", StringUtils.isNotBlank(planCreditBean.getLiquidatesTimeEnd())?planCreditBean.getLiquidatesTimeEnd():null);
		params.put("endDateStart",StringUtils.isNotBlank(planCreditBean.getEndDateStart())?planCreditBean.getEndDateStart():null);
		params.put("endDateEnd",StringUtils.isNotBlank(planCreditBean.getEndDateEnd())?planCreditBean.getEndDateEnd():null);
		params.put("repayNextTimeStart",StringUtils.isNotBlank(planCreditBean.getRepayNextTimeStart())?planCreditBean.getRepayNextTimeStart():null);
		params.put("repayNextTimeEnd", StringUtils.isNotBlank(planCreditBean.getRepayNextTimeEnd())?planCreditBean.getRepayNextTimeEnd():null);
		// add by  zhangyk  增加搜索条件 start
		params.put("actualAprStartSrch",planCreditBean.getActualAprStartSrch());
		params.put("actualAprEndSrch",planCreditBean.getActualAprEndSrch());
		params.put("labelName",planCreditBean.getLabelName());
		// add by  zhangyk  增加搜索条件 end
		Integer count = this.debtCreditService.countDebtCredit(params);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(planCreditBean.getPaginatorPage(), count);
			params.put("limitStart", paginator.getOffset());
			params.put("limitEnd", paginator.getLimit());
			List<HjhDebtCreditCustomize> recordList = this.debtCreditService.selectDebtCreditList(params);
			// add by zhangyk  增加查询显示列表数据总结列
			Map<String,Object> sum = this.debtCreditService.selectDebtCreditTotal(params);
			planCreditBean.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
			modelAndView.addObject("sum",sum);
		}
		modelAndView.addObject(HjhDebtCreditDefine.HJH_CREDIT_FORM, planCreditBean);
	}

	/**
	 * 详细画面
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = HjhDebtCreditDefine.INFO_ACTION)
	@RequiresPermissions(HjhDebtCreditDefine.PERMISSIONS_INFO)
	public ModelAndView bailInfoAction(HttpServletRequest request, HjhDebtCreditBean form, RedirectAttributes attr) {

		LogUtil.startLog(HjhDebtCreditDefine.CONTROLLER_NAME, HjhDebtCreditDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(HjhDebtCreditDefine.CREDIT_DETAIL_ACITON);
		attr.addAttribute("creditNid", form.getCreditNid());
		LogUtil.endLog(HjhDebtCreditDefine.CONTROLLER_NAME, HjhDebtCreditDefine.INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(HjhDebtCreditDefine.EXPORT_ACTION)
	@RequiresPermissions(HjhDebtCreditDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, HjhDebtCreditBean planCreditBean) throws Exception {
		LogUtil.startLog(HjhDebtCreditDefine.CONTROLLER_NAME, HjhDebtCreditDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "智投服务转让记录";

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
		params.put("liquidatesTimeStart", StringUtils.isNotBlank(planCreditBean.getLiquidatesTimeStart())?planCreditBean.getLiquidatesTimeStart():null);
		params.put("liquidatesTimeEnd", StringUtils.isNotBlank(planCreditBean.getLiquidatesTimeEnd())?planCreditBean.getLiquidatesTimeEnd():null);
		params.put("repayNextTimeStart",StringUtils.isNotBlank(planCreditBean.getRepayNextTimeStart())?planCreditBean.getRepayNextTimeStart():null);
		params.put("repayNextTimeEnd", StringUtils.isNotBlank(planCreditBean.getRepayNextTimeEnd())?planCreditBean.getRepayNextTimeEnd():null);
		List<HjhDebtCreditCustomize> resultList = this.debtCreditService.selectDebtCreditList(params);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "序号", "出让人智投编号", "出让人智投订单号", "清算后智投编号", "出让人", "债转编号", "原项目编号", "原项目出借利率", "还款方式", "债权本金","债权价值", "预计承接出借利率", "已转让本金", "垫付利息", /*"清算手续费率", "实际服务费",*/"剩余债权价值", "出让人实际到账金额", "最新清算时间", "预计开始退出时间","转让状态", "还款状态","项目期数 ","当期应还款时间" };
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
					HjhDebtCreditCustomize debtCredit = resultList.get(i);

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
					// 清算后计划编号
					else if (celLength == 3) {
						cell.setCellValue(debtCredit.getPlanNidNew());
					}
					// 出让人
					else if (celLength == 4) {
						cell.setCellValue(debtCredit.getUserName());
					}
					// 债转编号
					else if (celLength == 5) {
						cell.setCellValue(debtCredit.getCreditNid());
					}
					// 原项目编号
					else if (celLength == 6) {
						cell.setCellValue(debtCredit.getBorrowNid());
					}
					// 原项目出借利率
					else if (celLength == 7) {
						cell.setCellValue(debtCredit.getBorrowApr()+"%");
					}
					// 还款方式
					else if (celLength == 8) {
						cell.setCellValue(debtCredit.getRepayStyleName());
					}
					// 债权本金
					else if (celLength == 9) {
						cell.setCellValue(debtCredit.getCreditCapital());
					}
					// 债权价值
					else if (celLength == 10) {
						cell.setCellValue(debtCredit.getLiquidationFairValue());
					}
					// 预计承接收益率
					else if (celLength == 11) {
						cell.setCellValue(debtCredit.getActualApr()+"%");
					}
					// 已转让本金
					else if (celLength == 12) {
						cell.setCellValue(debtCredit.getAssignCapital());
					}
					// 垫付利息
					else if (celLength == 13) {
						cell.setCellValue(debtCredit.getAssignAdvanceInterest());
					}
					// 在途资金
					else if (celLength == 14) {
						cell.setCellValue(debtCredit.getRemainCredit());
					}
					// 出让人实际到账金额
					else if (celLength == 15) {
						cell.setCellValue(debtCredit.getAccountReceive());
					}
					// 最新清算时间
					else if (celLength == 16) {
						cell.setCellValue(debtCredit.getLiquidatesTime());
					}
					// 预计开始退出时间
					else if (celLength == 17){
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
						cell.setCellValue(dateFormat.format(debtCredit.getEndDate()));
					}
					// 转让状态
					else if (celLength == 18) {
						cell.setCellValue(debtCredit.getCreditStatusName());
					}
					// 还款状态
					else if (celLength == 19) {
						cell.setCellValue(debtCredit.getRepayStatusName());
					}
					// 项目期数
					else if (celLength == 20) {
						cell.setCellValue(debtCredit.getLiquidatesPeriod()+"/"+debtCredit.getBorrowPeriod());
					}
					/*// 清算时所在期数
					else if (celLength == 20) {
						cell.setCellValue(debtCredit.getLiquidatesPeriod());
					}*/
					// 当期应还款时间
					else if (celLength == 21) {
						cell.setCellValue(debtCredit.getRepayNextTime());
					}

				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(HjhDebtCreditDefine.CONTROLLER_NAME, HjhDebtCreditDefine.EXPORT_ACTION);
	}

	/**
	 * 运营记录-债转标的init
	 * @param request
	 * @param response
	 * @param form
     * @return
     */
	@RequestMapping(HjhDebtCreditDefine.OPT_CREDIT_ACTION_INIT)
	@RequiresPermissions(HjhDebtCreditDefine.PERMISSIONS_VIEW)
	public ModelAndView optAction(HttpServletRequest request, HttpServletResponse response, @ModelAttribute(HjhDebtCreditDefine.HJH_CREDIT_FORM) HjhDebtCreditBean form) {
		LogUtil.startLog(HjhDebtCreditDefine.CONTROLLER_NAME, HjhDebtCreditDefine.OPT_CREDIT_ACTION_INIT);
		ModelAndView modeAndView = new ModelAndView(PlanListDefine.OPT_CREDIT_LIST_PATH);
		// 创建分页
		/*if (!"1".equals(form.getIsSearch())){
			if (StringUtils.isBlank(form.getLiquidatesTimeStart() )){*/
		form.setPlanNid("");  // 初始化页面不在显示planNid
		form.setLiquidatesTimeStart(GetDate.date2Str(new Date(), new SimpleDateFormat("yyyy-MM-dd")));
		/*	}
		}*/
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(HjhDebtCreditDefine.CONTROLLER_NAME, HjhDebtCreditDefine.OPT_CREDIT_ACTION_INIT);
		return modeAndView;
	}


	/**
	 * 运营记录-债转标的search
	 * @param request
	 * @param response
	 * @param form
	 * @return
	 */
	@RequestMapping(HjhDebtCreditDefine.OPT_CREDIT_ACTION_SEARCH)
	@RequiresPermissions(HjhDebtCreditDefine.PERMISSIONS_VIEW)
	public ModelAndView optRecordSearch(HttpServletRequest request, HttpServletResponse response, @ModelAttribute(HjhDebtCreditDefine.HJH_CREDIT_FORM) HjhDebtCreditBean form) {
		LogUtil.startLog(HjhDebtCreditDefine.CONTROLLER_NAME, HjhDebtCreditDefine.OPT_CREDIT_ACTION_SEARCH);
		ModelAndView modeAndView = new ModelAndView(PlanListDefine.OPT_CREDIT_LIST_PATH);
		// 创建分页
		/*if (!"1".equals(form.getIsSearch())){
			if (StringUtils.isBlank(form.getLiquidatesTimeStart() )){
				form.setLiquidatesTimeStart(GetDate.date2Str(new Date(), new SimpleDateFormat("yyyy-MM-dd")));
			}
		}*/
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(HjhDebtCreditDefine.CONTROLLER_NAME, HjhDebtCreditDefine.OPT_CREDIT_ACTION_SEARCH);
		return modeAndView;
	}

}
