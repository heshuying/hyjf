package com.hyjf.admin.manager.statis;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.hjhplan.accedelist.AccedeListDefine;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.customize.HjhAccountBalanceCustomize;


@Controller
@RequestMapping(value = AccountBalanceDefine.REQUEST_MAPPING)
public class AccountBalanceController extends BaseController{
	
	@Autowired
	private AccountBalanceService hjhAccountBalanceService;

    /**
     * 查询列表
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AccountBalanceDefine.INIT)
    @RequiresPermissions(AccountBalanceDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, AccountBalanceBean form) {
       String time = request.getParameter("time");
    	if(("month").equals(time)) {
    		LogUtil.startLog(AccountBalanceController.class.toString(),AccountBalanceDefine.INIT);
    		ModelAndView modeAndView = new ModelAndView(AccountBalanceDefine.MONTH_LIST_PATH);
    		// 创建分页
    		this.createPageByMonth(request, modeAndView, form);
    		LogUtil.endLog(AccountBalanceController.class.toString(), AccountBalanceDefine.INIT);
    		return modeAndView;
    	}else {
	    	LogUtil.startLog(AccountBalanceController.class.toString(),AccountBalanceDefine.INIT);
	        ModelAndView modeAndView = new ModelAndView(AccountBalanceDefine.LIST_PATH);
	        // 创建分页
	        this.createPage(request, modeAndView, form);
	        LogUtil.endLog(AccountBalanceController.class.toString(), AccountBalanceDefine.INIT);
	        return modeAndView;
    	}
    
    }
    
	/**
	 * 画面检索面板-检索按键画面请求
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AccountBalanceDefine.SEARCH_ACTION)
	@RequiresPermissions(AccountBalanceDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, AccountBalanceBean form) {
		LogUtil.startLog(AccountBalanceController.class.toString(), AccountBalanceDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(AccountBalanceDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(AccountBalanceController.class.toString(), AccountBalanceDefine.SEARCH_ACTION);
		return modelAndView;
	}
	
    
    /**
     * 分页
     *
     * @param request
     * @param modeAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modeAndView, AccountBalanceBean form) {
    	HjhAccountBalanceCustomize hjhAccountBalanceCustomize = new HjhAccountBalanceCustomize();
    	BeanUtils.copyProperties(form, hjhAccountBalanceCustomize);
        Integer count = this.hjhAccountBalanceService.getHjhAccountBalancecount(hjhAccountBalanceCustomize);
        if (count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            hjhAccountBalanceCustomize.setLimitStart(paginator.getOffset());
            hjhAccountBalanceCustomize.setLimitEnd(paginator.getLimit());

            List<HjhAccountBalanceCustomize> customers = this.hjhAccountBalanceService.getHjhAccountBalanceList(hjhAccountBalanceCustomize);
            HjhAccountBalanceCustomize sum = this.hjhAccountBalanceService.getHjhAccountBalanceSum(hjhAccountBalanceCustomize);
            form.setSumObj(sum);
            form.setPaginator(paginator);
            form.setRecordList(customers);
        }
        modeAndView.addObject(AccountBalanceDefine.HJHACCOUNTBALANCE_FORM, form);
    }
    
    /**
     * 月分页
     *
     * @param request
     * @param modeAndView
     * @param form
     */
    private void createPageByMonth(HttpServletRequest request, ModelAndView modeAndView, AccountBalanceBean form) {
    	HjhAccountBalanceCustomize hjhAccountBalanceCustomize = new HjhAccountBalanceCustomize();
    	BeanUtils.copyProperties(form, hjhAccountBalanceCustomize);
    	Integer count = this.hjhAccountBalanceService.getHjhAccountBalanceMonthCount(hjhAccountBalanceCustomize);
    	if (count > 0) {
    		Paginator paginator = new Paginator(form.getPaginatorPage(), count);
    		hjhAccountBalanceCustomize.setLimitStart(paginator.getOffset());
    		hjhAccountBalanceCustomize.setLimitEnd(paginator.getLimit());
    		
    		List<HjhAccountBalanceCustomize> customers = this.hjhAccountBalanceService.getHjhAccountBalanceMonthList(hjhAccountBalanceCustomize);
    		HjhAccountBalanceCustomize sum = this.hjhAccountBalanceService.getHjhAccountBalanceMonthSum(hjhAccountBalanceCustomize);
    		form.setSumObj(sum);
    		form.setPaginator(paginator);
    		form.setRecordList(customers);
    	}
    	modeAndView.addObject(AccountBalanceDefine.HJHACCOUNTBALANCE_FORM, form);
    }
	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
    
	@RequestMapping(AccountBalanceDefine.HJHACCOUNTBALANCE_EXPORT)
	@RequiresPermissions(AccountBalanceDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, HjhAccountBalanceCustomize form) throws Exception {
		LogUtil.startLog(AccountBalanceController.class.toString(),AccountBalanceDefine.HJHACCOUNTBALANCE_EXPORT);
		// 表格sheet名称
		String sheetName = "每日交易量";
		List<HjhAccountBalanceCustomize> resultList = this.hjhAccountBalanceService.getHjhAccountBalanceList(form);
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
		String[] titles = new String[] { "序号","日期", "原始资产交易额(元)","债转资产交易额(元)", "复出借金额(元)","新加入资金额(元)" };
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
					HjhAccountBalanceCustomize AccountBalanceCustomize = resultList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					Double investAccount = AccountBalanceCustomize.getInvestAccount().doubleValue();
					Double creditAccount = AccountBalanceCustomize.getCreditAccount().doubleValue();
					Double reinvestAccount = AccountBalanceCustomize.getReinvestAccount().doubleValue();
					Double addAccount = AccountBalanceCustomize.getAddAccount().doubleValue();
					Date date = AccountBalanceCustomize.getDate();
					String format = new SimpleDateFormat("yyyy-MM-dd ").format(date);
					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					
					// 日期
					else if (celLength == 1) {
							cell.setCellValue(StringUtils.isEmpty(format) ? StringUtils.EMPTY : format);
					}
					// 原始资产交易额(元)
					else if (celLength == 2) {
						cell.setCellValue((investAccount != null ? investAccount:0 ));
					}
//					债转资产交易额(元)
					else if (celLength == 3) {
						cell.setCellValue((creditAccount != null ? creditAccount:0 ));
					}
//					复出借金额(元)
					else if (celLength == 4) {
						cell.setCellValue((reinvestAccount != null ? reinvestAccount:0 ));
					}
//					新加入资金额(元)
					else if (celLength == 5) {
						cell.setCellValue((addAccount != null ? addAccount:0 ));
					}
				
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(AccountBalanceController.class.toString(), AccedeListDefine.EXPORTEXECL);
	}
	/**
	 * 按月导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	
	@RequestMapping(AccountBalanceDefine.HJHACCOUNTBALANCE_Month_EXPORT)
	@RequiresPermissions(AccountBalanceDefine.PERMISSIONS_EXPORT)
	public void exportActionMonth(HttpServletRequest request, HttpServletResponse response, HjhAccountBalanceCustomize form) throws Exception {
		LogUtil.startLog(AccountBalanceController.class.toString(),AccountBalanceDefine.HJHACCOUNTBALANCE_EXPORT);
		// 表格sheet名称
		String sheetName = "每月交易量";
		List<HjhAccountBalanceCustomize> resultList = this.hjhAccountBalanceService.getHjhAccountBalanceMonthList(form);
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
		String[] titles = new String[] { "序号","日期", "原始资产交易额(元)","债转资产交易额(元)", "复出借金额(元)","新加入资金额(元)" };
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
					HjhAccountBalanceCustomize AccountBalanceCustomize = resultList.get(i);
					
					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					
					
					Double investAccount = AccountBalanceCustomize.getInvestAccount().doubleValue();
					Double creditAccount = AccountBalanceCustomize.getCreditAccount().doubleValue();
					Double reinvestAccount = AccountBalanceCustomize.getReinvestAccount().doubleValue();
					Double addAccount = AccountBalanceCustomize.getAddAccount().doubleValue();
					String dataFormt = AccountBalanceCustomize.getDataFormt();
					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					
					// 日期
					else if (celLength == 1) {
						cell.setCellValue(StringUtils.isEmpty(dataFormt) ? StringUtils.EMPTY : dataFormt);
					}
					// 原始资产交易额(元)
					else if (celLength == 2) {
						cell.setCellValue((investAccount != null ? investAccount:0 ));
					}
//					债转资产交易额(元)
					else if (celLength == 3) {
						cell.setCellValue((creditAccount != null ? creditAccount:0 ));
					}
//					复出借金额(元)
					else if (celLength == 4) {
						cell.setCellValue((reinvestAccount != null ? reinvestAccount:0 ));
					}
//					新加入资金额(元)
					else if (celLength == 5) {
						cell.setCellValue((addAccount != null ? addAccount:0 ));
					}
					
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
		
		LogUtil.endLog(AccountBalanceController.class.toString(), AccedeListDefine.EXPORTEXECL);
	}

    
    
}
