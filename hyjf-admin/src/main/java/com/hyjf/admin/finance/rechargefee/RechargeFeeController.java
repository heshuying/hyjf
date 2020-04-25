package com.hyjf.admin.finance.rechargefee;

import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.RechargeFeeReconciliation;

/**
 * 充值管理
 *
 * @author HBZ
 */
@Controller
@RequestMapping(value = RechargeFeeDefine.REQUEST_MAPPING)
public class RechargeFeeController extends BaseController {
	@Autowired
	private RechargeFeeService rechargeFeeService;
	/**
	 * 分页
	 *
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modeAndView, RechargeFeeBean form) {
		Integer count = this.rechargeFeeService.queryRechargeFeeCount(form);
		if (count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			form.setPaginator(paginator);
			List<RechargeFeeReconciliation> rechargeFeeList = this.rechargeFeeService.queryRechargeFeeList(form,paginator.getOffset(),paginator.getLimit());
			form.setRecordList(rechargeFeeList);
		}
		modeAndView.addObject(RechargeFeeDefine.RECHARGE_FEE_FORM, form);
	}

	/**
	 * 充值手续费对账 列表
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(RechargeFeeDefine.INIT)
	@RequiresPermissions(RechargeFeeDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RechargeFeeBean form) {
		LogUtil.startLog(RechargeFeeController.class.toString(), RechargeFeeDefine.INIT);
		ModelAndView modeAndView = new ModelAndView(RechargeFeeDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(RechargeFeeController.class.toString(), RechargeFeeDefine.INIT);
		return modeAndView;
	}

	/**
	 * 充值手续费对账 查询条件
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(RechargeFeeDefine.SEARCH_ACTION)
	@RequiresPermissions(RechargeFeeDefine.PERMISSIONS_VIEW)
	public ModelAndView searchAction(HttpServletRequest request, RechargeFeeBean form) {
		LogUtil.startLog(RechargeFeeController.class.toString(), RechargeFeeDefine.SEARCH_ACTION);
		ModelAndView modeAndView = new ModelAndView(RechargeFeeDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modeAndView, form);

		LogUtil.endLog(RechargeFeeController.class.toString(), RechargeFeeDefine.SEARCH_ACTION);
		return modeAndView;
	}


	/**
	 * 数据导出
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(RechargeFeeDefine.EXPORT_ACTION)
	@RequiresPermissions(RechargeFeeDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, RechargeFeeBean form) throws Exception {
		LogUtil.startLog(RechargeFeeController.class.toString(), RechargeFeeDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "充值手续费对账";
		//设置默认查询时间
		if(StringUtils.isEmpty(form.getStartDateSrch())){
			form.setStartDateSrch(GetDate.getDate("yyyy-MM-dd"));
		}
		if(StringUtils.isEmpty(form.getEndDateSrch())){
			form.setEndDateSrch(GetDate.getDate("yyyy-MM-dd"));
		}
		List<RechargeFeeReconciliation> rechargeFeeList = this.rechargeFeeService.exportRechargeFeeList(form);
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "序号", "用户名", "账单编号", "账单周期", "累计充值金额", "平台累计垫付手续费", "应付款", "转账订单号", "状态", "生成时间" };
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
					RechargeFeeReconciliation record = rechargeFeeList.get(i);
					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					} else if (celLength == 1) {
						cell.setCellValue(record.getUserName());
					} else if (celLength == 2) {
						cell.setCellValue(record.getRechargeNid());
					} else if (celLength == 3) {
						cell.setCellValue(GetDate.getDateMyTimeInMillis(record.getStartTime())+" 至  "+GetDate.getDateMyTimeInMillis(record.getEndTime()));// 充值渠道
					} else if (celLength == 4) {
						cell.setCellValue(String.valueOf(record.getRechargeAmount())); 
					} else if (celLength == 5) {
						cell.setCellValue(String.valueOf(record.getRechargeFee()));
					} else if (celLength == 6) {
						cell.setCellValue(String.valueOf(record.getRechargeFee()));
					} else if (celLength == 7) {
						cell.setCellValue(record.getOrderId());
					} else if (celLength == 8) {
						if(record.getStatus() == 0){
							cell.setCellValue("待付款");
						}else{
							cell.setCellValue("已付款");
						}
					} else if (celLength == 9) {
						cell.setCellValue(GetDate.timestamptoStrYYYYMMDDHHMMSS(record.getAddTime()));
					} 
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
		LogUtil.endLog(RechargeFeeController.class.toString(), RechargeFeeDefine.EXPORT_ACTION);

	}


}
