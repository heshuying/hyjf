package com.hyjf.admin.htl.productinfo;

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
import com.hyjf.mybatis.model.auto.ProductInfo;

/**
 * @package com.hyjf.admin.maintenance.Product
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = ProductInfoDefine.REQUEST_MAPPING)
public class ProductInfoControllor extends BaseController {

	private static final String THIS_CLASS = ProductInfoControllor.class.getName();
	@Autowired
	private ProductInfoService productInfoService;


	/**
	 * 每日报表画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ProductInfoDefine.INIT)
    @RequiresPermissions(ProductInfoDefine.PRODUCTINFO_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(ProductInfoDefine.PRODUCTINFO_FORM) ProductInfoBean form) {
		LogUtil.startLog(THIS_CLASS, ProductInfoDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(ProductInfoDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(ProductInfoControllor.class.toString(), ProductInfoDefine.INIT);
		return modelAndView;
	}

	/**
	 * 每日报表画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ProductInfoDefine.SEARCH_ACTION)
    @RequiresPermissions(ProductInfoDefine.PRODUCTINFO_SEARCH)
	public ModelAndView search(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(ProductInfoDefine.PRODUCTINFO_FORM) ProductInfoBean form) {
		LogUtil.startLog(THIS_CLASS, ProductInfoDefine.PRODUCTINFO_SEARCH);
		ModelAndView modelAndView = new ModelAndView(ProductInfoDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(ProductInfoControllor.class.toString(), ProductInfoDefine.PRODUCTINFO_SEARCH);
		return modelAndView;
	}

	
	/**
	 * 每日报表分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, ProductInfoBean form) {
		ProductInfoBean productInfo=new ProductInfoBean();
		if(StringUtils.isNotEmpty(form.getTimeStartSrch())){
			productInfo.setTimeStartSrch(form.getTimeStartSrch());
		}
		if(StringUtils.isNotEmpty(form.getTimeEndSrch())){
			productInfo.setTimeEndSrch(form.getTimeEndSrch());
		}
		
		Integer count = this.productInfoService.countRecord(productInfo);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			List<ProductInfo> recordList  = this.productInfoService.getRecordList(productInfo, paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
		}
		modelAndView.addObject(ProductInfoDefine.PRODUCTINFO_FORM, form);
	}


	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(ProductInfoDefine.EXPORTEXECL)
	@RequiresPermissions(ProductInfoDefine.PRODUCTINFO_EXPORTEXECL)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, ProductInfoBean form) throws Exception {
		LogUtil.startLog(THIS_CLASS, ProductInfoDefine.EXPORTEXECL);
		// 表格sheet名称
		String sheetName = "汇天利每日报表";
		ProductInfoBean productInfo=new ProductInfoBean();
		//时间检索
		if(StringUtils.isNotEmpty(form.getTimeStartSrch())){
			productInfo.setTimeStartSrch(form.getTimeStartSrch());
		}
		if(StringUtils.isNotEmpty(form.getTimeEndSrch())){
			productInfo.setTimeEndSrch(form.getTimeEndSrch());
		}		
		List<ProductInfo> resultList = this.productInfoService.getRecordList(productInfo, -1, -1);
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
		String[] titles = new String[] { "序号", "日期","转入用户数","转出用户数","转入金额","转出金额","转出收益","本金总额","资管公司账户余额" };
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
					ProductInfo pInfo = resultList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					else if (celLength == 1) {
						cell.setCellValue(pInfo.getDataDate());
					}
					else if (celLength == 2) {
						cell.setCellValue(pInfo.getInCount());
					}
					else if (celLength == 3) {
						cell.setCellValue(pInfo.getOutCount());
					}
					else if (celLength == 4) {
						cell.setCellValue(pInfo.getInAmount().doubleValue());
					}
					else if (celLength == 5) {
						cell.setCellValue(pInfo.getOutAmount().doubleValue());
					}
					else if (celLength == 6) {
						if(pInfo.getOutInterest() == null){
							cell.setCellValue("0.00");
						}else{
							cell.setCellValue(pInfo.getOutInterest().doubleValue());
						}
					}
					else if (celLength == 7) {
						cell.setCellValue(pInfo.getInvestAmount().doubleValue());
					}
					else if(celLength == 8){
						cell.setCellValue(pInfo.getLoanBalance().doubleValue());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(THIS_CLASS, ProductInfoDefine.EXPORTEXECL);
	}


}
