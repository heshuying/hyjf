package com.hyjf.admin.htl.productinterest;

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

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.customize.ProductInterestCustomize;
/**
 * @package com.hyjf.admin.htl.productredeem
 * @author Michael
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = ProductInterestDefine.REQUEST_MAPPING)
public class ProductInterestControllor extends BaseController {

	private static final String THIS_CLASS = ProductInterestControllor.class.getName();
	@Autowired
	private ProductInterestService  productInterestService;

	/**
	 * 汇天利转入画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ProductInterestDefine.INIT)
    @RequiresPermissions(ProductInterestDefine.PRODUCTINTERESTCUSTOMIZE_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute(ProductInterestDefine.PRODUCTINTERESTCUSTOMIZE_FORM) ProductInterestBean form) {
		LogUtil.startLog(THIS_CLASS, ProductInterestDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(ProductInterestDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, ProductInterestDefine.INIT);
		return modelAndView;
	}

	/**
	 * 汇天利转入画面查询
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ProductInterestDefine.SEARCH_ACTION)
    @RequiresPermissions(ProductInterestDefine.PRODUCTINTERESTCUSTOMIZE_SEARCH)
	public ModelAndView search(HttpServletRequest request, ProductInterestBean form) {
		LogUtil.startLog(THIS_CLASS, ProductInterestDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(ProductInterestDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, ProductInterestDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 汇天利转入列表分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, ProductInterestBean form) {
		ProductInterestCustomize productInterestCustomize = new ProductInterestCustomize();
		String userid = request.getParameter("userid");
		if(StringUtils.isNotEmpty(userid)){
			productInterestCustomize.setUserId(Integer.parseInt(userid));
			request.setAttribute("userid", userid);
		}
		if(StringUtils.isNotEmpty(form.getUsername())){
			productInterestCustomize.setUsername(form.getUsername());
		}
		if(StringUtils.isNotEmpty(form.getRefername())){
			productInterestCustomize.setRefername(form.getRefername());
		}
		if(StringUtils.isNotEmpty(form.getTimeStartSrch())){
			productInterestCustomize.setTimeStartSrch(GetDate.get10Time(form.getTimeStartSrch()));
		}
		if(StringUtils.isNotEmpty(form.getTimeStartSrch())){
			productInterestCustomize.setTimeEndSrch(GetDate.get10Time(form.getTimeEndSrch()));
		}
		
		Integer count = this.productInterestService.countRecord(productInterestCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			productInterestCustomize.setLimitStart(paginator.getOffset());
			productInterestCustomize.setLimitEnd(paginator.getLimit());
			List<ProductInterestCustomize> recordList  = this.productInterestService.getRecordList(productInterestCustomize);
			form.setPaginator(paginator);
			form.setRecordList(recordList);
		}
		modelAndView.addObject(ProductInterestDefine.PRODUCTINTERESTCUSTOMIZE_FORM, form);
		
	}

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(ProductInterestDefine.EXPORTEXECL)
	@RequiresPermissions(ProductInterestDefine.PRODUCTINTERESTCUSTOMIZE_EXEPORTEXECL)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, ProductInterestBean form) throws Exception {
		LogUtil.startLog(THIS_CLASS, ProductInterestDefine.EXPORTEXECL);
		// 表格sheet名称
		String sheetName = "汇天利收益明细";
		ProductInterestCustomize productInterestCustomize = new ProductInterestCustomize();
		String userid = request.getParameter("userid");
		if(StringUtils.isNotEmpty(userid)){
			productInterestCustomize.setUserId(Integer.parseInt(userid));
			request.setAttribute("userid", userid);
		}
		if(StringUtils.isNotEmpty(form.getUsername())){
			productInterestCustomize.setUsername(form.getUsername());
		}
		if(StringUtils.isNotEmpty(form.getRefername())){
			productInterestCustomize.setRefername(form.getRefername());
		}
		if(StringUtils.isNotEmpty(form.getTimeStartSrch())){
			productInterestCustomize.setTimeStartSrch(GetDate.get10Time(form.getTimeStartSrch()));
		}
		if(StringUtils.isNotEmpty(form.getTimeStartSrch())){
			productInterestCustomize.setTimeEndSrch(GetDate.get10Time(form.getTimeEndSrch()));
		}
		List<ProductInterestCustomize> resultList  = this.productInterestService.getRecordList(productInterestCustomize);
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
		String[] titles = new String[] { "序号", "日期","用户名","推荐人","本金总额","收益金额" };
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
					ProductInterestCustomize pInfo = resultList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					else if (celLength == 1) {
						cell.setCellValue(pInfo.getInterestTime());
					}
					else if (celLength == 2) {
						cell.setCellValue(pInfo.getUsername());
					}
					else if (celLength == 3) {
						if(pInfo.getRefername() == null){
							cell.setCellValue("");
						}else{
							cell.setCellValue(pInfo.getRefername());
						}
					}
					else if (celLength == 4) {
						cell.setCellValue(pInfo.getAmount().doubleValue());
					}
					else if (celLength == 5) {
						if(pInfo.getInterest() == null){
							cell.setCellValue(0);
						}else{
							cell.setCellValue(pInfo.getInterest().doubleValue());
						}
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(THIS_CLASS, ProductInterestDefine.EXPORTEXECL);
	}

}
