package com.hyjf.admin.finance.statistics.rechargefee;

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

import com.alibaba.fastjson.JSON;
import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.RechargeFeeBalanceStatistics;
import com.hyjf.mybatis.model.auto.RechargeFeeStatistics;
import com.hyjf.mybatis.model.customize.RechargeFeeStatisticsCustomize;

/**
 * 充值手续费垫付统计
 *
 * @author 李深强
 */
@Controller
@RequestMapping(value = RechargeFeeStatisticsDefine.REQUEST_MAPPING)
public class RechargeFeeStatisticsController extends BaseController {
	@Autowired
	private RechargeFeeStatisticsService rechargeFeeStatisticsService;
	
	
	/**
	 * 充值手续费垫付统计 页面
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(RechargeFeeStatisticsDefine.STATISTICS)
	@RequiresPermissions(RechargeFeeStatisticsDefine.PERMISSIONS_VIEW)
	public ModelAndView statistics(HttpServletRequest request, RechargeFeeStatisticsBean form) {
		LogUtil.startLog(RechargeFeeStatisticsController.class.toString(), RechargeFeeStatisticsDefine.STATISTICS);
		ModelAndView modeAndView = new ModelAndView(RechargeFeeStatisticsDefine.STATIS_PATH);
		RechargeFeeStatistics record =this.rechargeFeeStatisticsService.selectNewRechargeFeeStatistics();
		if(record != null ){
			form.setUpdateTimeView(GetDate.timestamptoStrYYYYMMDDHHMMSS(record.getUpdateTime()));
		}else{
			form.setUpdateTimeView("");
		}
		modeAndView.addObject(RechargeFeeStatisticsDefine.RECHARGE_FEE_STATISTICS_FORM, form);
		LogUtil.endLog(RechargeFeeStatisticsController.class.toString(), RechargeFeeStatisticsDefine.STATISTICS);
		return modeAndView;
	}

	
	
	/**
	 * 分页
	 *
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modeAndView, RechargeFeeStatisticsBean form) {

		Integer count = this.rechargeFeeStatisticsService.queryRechargeFeeStatisticsCount(form);
		if (count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			form.setPaginator(paginator);
			List<RechargeFeeStatistics> rechargeFeeList = this.rechargeFeeStatisticsService.queryRechargeFeeStatisticsList(form,paginator.getOffset(),paginator.getLimit());
			form.setRecordList(rechargeFeeList);
			//总计数据
			RechargeFeeStatisticsCustomize sumRecord = this.rechargeFeeStatisticsService.selectRechargeFeeStatisticsSum(form);
			form.setComAmountSum(sumRecord.getCompAmountSum());
			form.setRechargeAmountSum(sumRecord.getRechargeAmountSum());
			form.setQuickAmountSum(sumRecord.getQuickAmountSum());
			form.setPersonalAmountSum(sumRecord.getPersonalAmountSum());
			form.setFeeSum(sumRecord.getFeeSum());
			//获取更新时间
			RechargeFeeStatistics record =this.rechargeFeeStatisticsService.selectNewRechargeFeeStatistics();
			form.setUpdateTimeView(GetDate.timestamptoStrYYYYMMDDHHMMSS(record.getUpdateTime()));
		}else{
			form.setUpdateTimeView("");
		}
		modeAndView.addObject(RechargeFeeStatisticsDefine.RECHARGE_FEE_STATISTICS_FORM, form);
	}

	/**
	 * 充值手续费垫付统计 列表
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(RechargeFeeStatisticsDefine.INIT)
	@RequiresPermissions(RechargeFeeStatisticsDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RechargeFeeStatisticsBean form) {
		LogUtil.startLog(RechargeFeeStatisticsController.class.toString(), RechargeFeeStatisticsDefine.INIT);
		ModelAndView modeAndView = new ModelAndView(RechargeFeeStatisticsDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(RechargeFeeStatisticsController.class.toString(), RechargeFeeStatisticsDefine.INIT);
		return modeAndView;
	}

	/**
	 *  充值手续费垫付统计 查询条件
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(RechargeFeeStatisticsDefine.SEARCH_ACTION)
	@RequiresPermissions(RechargeFeeStatisticsDefine.PERMISSIONS_VIEW)
	public ModelAndView searchAction(HttpServletRequest request, RechargeFeeStatisticsBean form) {
		LogUtil.startLog(RechargeFeeStatisticsController.class.toString(), RechargeFeeStatisticsDefine.SEARCH_ACTION);
		ModelAndView modeAndView = new ModelAndView(RechargeFeeStatisticsDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modeAndView, form);

		LogUtil.endLog(RechargeFeeStatisticsController.class.toString(), RechargeFeeStatisticsDefine.SEARCH_ACTION);
		return modeAndView;
	}


	/**
	 * 数据导出
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(RechargeFeeStatisticsDefine.EXPORT_ACTION)
	@RequiresPermissions(RechargeFeeStatisticsDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, RechargeFeeStatisticsBean form) throws Exception {
		LogUtil.startLog(RechargeFeeStatisticsController.class.toString(), RechargeFeeStatisticsDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "充值手续费垫付统计明细";
		
		List<RechargeFeeStatistics> rechargeFeeList = this.rechargeFeeStatisticsService.exportRechargeFeeStatisticsList(form);
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "序号", "日期", "充值金额", "快捷充值", "个人网银", "企业网银", "平台垫付手续费" };
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();

		// 生成一个表格
		HSSFSheet sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, sheetName + "_第1页");

		if (rechargeFeeList != null && rechargeFeeList.size() > 0) {

			int sheetCount = 1;
			int rowNum = 0;

			for (int i = 0; i < rechargeFeeList.size(); i++) {
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
					RechargeFeeStatistics record = rechargeFeeList.get(i);
					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					} else if (celLength == 1) {
						cell.setCellValue(record.getStatDate());
					} else if (celLength == 2) {
						cell.setCellValue(String.valueOf(record.getRechargeAmount()));
					} else if (celLength == 3) {
						cell.setCellValue(String.valueOf(record.getQuickAmount()));
					} else if (celLength == 4) {
						cell.setCellValue(String.valueOf(record.getPersonalAmount())); 
					} else if (celLength == 5) {
						cell.setCellValue(String.valueOf(record.getComAmount()));
					} else if (celLength == 6) {
						cell.setCellValue(String.valueOf(record.getFee()));
					} 
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
		LogUtil.endLog(RechargeFeeStatisticsController.class.toString(), RechargeFeeStatisticsDefine.EXPORT_ACTION);

	}

	
	/**
	 * 出借总览
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = RechargeFeeStatisticsDefine.STATISTICS_SEARCH_ACTION, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String statisSearchAction(HttpServletRequest request, HttpServletResponse response,@ModelAttribute(RechargeFeeStatisticsDefine.RECHARGE_FEE_STATISTICS_FORM) RechargeFeeStatisticsBean form) {
		LogUtil.startLog(RechargeFeeStatisticsController.class.toString(), RechargeFeeStatisticsDefine.STATISTICS_SEARCH_ACTION);
		String viewFlag  = request.getParameter("viewFlag");
		String staDate = null;
		//查看今天/昨天数据
		if("2".equals(viewFlag)){
			staDate = GetDate.dateToString2(GetDate.countDate(5, -1), "yyyy-MM-dd");
		}else{
			staDate = GetDate.date2Str(GetDate.date_sdf);
		}
		//返回结果
		Map<String, Object> map = new HashMap<String, Object>();
		//垫付金额
		List<RechargeFeeStatistics> rechargeFeeList = this.rechargeFeeStatisticsService.selectAllRechargeFeeStatisticsList();
		
		int size = rechargeFeeList.size();
		String days[] = new String[size];//时间
		BigDecimal feeAmount[] = new BigDecimal[size];//垫付金额
		
		if(rechargeFeeList.size() > 0){
			for(int i = 0;i<rechargeFeeList.size();i++){
				RechargeFeeStatistics rechargeFee = rechargeFeeList.get(i);
				days[i] = rechargeFee.getStatDate();
				if(rechargeFee.getFee().compareTo(BigDecimal.ZERO) > 0){
					feeAmount[i] = rechargeFee.getFee().divide(new BigDecimal(10000), 2,BigDecimal.ROUND_DOWN);
				}else{
					feeAmount[i] = BigDecimal.ZERO;
				}
			}
		}
		map.put("days", days);
		map.put("data",feeAmount);
		
		//账户余额
		List<RechargeFeeBalanceStatistics> balanceList = this.rechargeFeeStatisticsService.selectFeeBalanceStatistics(staDate);
		int balanceSize = 0;
		if(balanceList != null && balanceList.size() > 0){
			balanceSize = balanceList.size();
		}

		String timepoint[] = new String[balanceSize];//时间节点
		BigDecimal balanceAmount[] = new BigDecimal[balanceSize];//账户余额
		if(balanceList.size() > 0){
			for(int i = 0;i<balanceList.size();i++){
				RechargeFeeBalanceStatistics balanceInfo = balanceList.get(i);
				timepoint[i] = balanceInfo.getTimePoint();
				if(balanceInfo.getBalance().compareTo(BigDecimal.ZERO) > 0){
					balanceAmount[i] = balanceInfo.getBalance();
				}else{
					feeAmount[i] = BigDecimal.ZERO;
				}
			}
		}
		map.put("timepoint", timepoint);
		map.put("data2",balanceAmount);
		String rep = JSON.toJSONString(map);
		LogUtil.startLog(RechargeFeeStatisticsController.class.toString(), RechargeFeeStatisticsDefine.STATISTICS_SEARCH_ACTION);
		return rep;
	}
	
}
