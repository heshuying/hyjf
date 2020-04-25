package com.hyjf.admin.htl.productinto;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.htl.common.HtlCommonService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.ProductExportIntoRecordCustomize;
import com.hyjf.mybatis.model.customize.ProductIntoRecordCustomize;

/**
 * @package com.hyjf.admin.maintenance.Product
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = ProductIntoRecordCustomizeDefine.REQUEST_MAPPING)
public class ProductIntoRecordCustomizeControllor extends BaseController {

	private static final String THIS_CLASS = ProductIntoRecordCustomizeControllor.class.getName();
	@Autowired
	private ProductIntoRecordCustomizeService htlIntoRecordCustomizeService;
	@Autowired
	private HtlCommonService htlCommonService;
	/**
	 * 转入记录画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ProductIntoRecordCustomizeDefine.INIT)
    @RequiresPermissions(ProductIntoRecordCustomizeDefine.PRODUCTINTORECORD_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute(ProductIntoRecordCustomizeDefine.PRODUCTINTORECORD_FORM) ProductIntoRecordCustomizeBean form) {
		LogUtil.startLog(THIS_CLASS, ProductIntoRecordCustomizeDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(ProductIntoRecordCustomizeDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, ProductIntoRecordCustomizeDefine.INIT);
		return modelAndView;
	}

	/**
	 * 转入记录画面查询
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ProductIntoRecordCustomizeDefine.SEARCH_ACTION)
    @RequiresPermissions(ProductIntoRecordCustomizeDefine.PRODUCTINTORECORD_SEARCH)
	public ModelAndView search(HttpServletRequest request, ProductIntoRecordCustomizeBean form) {
		LogUtil.startLog(THIS_CLASS, ProductIntoRecordCustomizeDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(ProductIntoRecordCustomizeDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, ProductIntoRecordCustomizeDefine.SEARCH_ACTION);
		return modelAndView;
	}
	/**
	 * 创建转入记录分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, ProductIntoRecordCustomizeBean form) {
		ProductIntoRecordCustomize productIntoRecordCustomize = new ProductIntoRecordCustomize();
		String userid = request.getParameter("userid");
		if(StringUtils.isNotEmpty(userid)){
			productIntoRecordCustomize.setUserId(Integer.parseInt(userid));
			request.setAttribute("userid", userid);
		}
		if(StringUtils.isNotEmpty(form.getUsernameSrh())){
			productIntoRecordCustomize.setUsername(form.getUsernameSrh());
		}
		if(StringUtils.isNotEmpty(form.getRefernameSrh())){
			productIntoRecordCustomize.setRefername(form.getRefernameSrh());;
		}
		if(StringUtils.isNotEmpty(form.getTimeStartSrch())){
			productIntoRecordCustomize.setTimeStartSrch(GetDate.get10Time(form.getTimeStartSrch()));
		}
		if(StringUtils.isNotEmpty(form.getTimeStartSrch())){
			productIntoRecordCustomize.setTimeEndSrch(GetDate.get10Time(form.getTimeEndSrch()));
		}
		if(StringUtils.isNotEmpty(form.getClientSrh())){
			productIntoRecordCustomize.setClient(Integer.parseInt(form.getClientSrh()));
		}
		if(StringUtils.isNotEmpty(form.getInvestStatusSrh())){
			productIntoRecordCustomize.setInvestStatus(Integer.parseInt(form.getInvestStatusSrh()));
		}
		//操作平台
		List<ParamName> clients=this.htlCommonService.getParamNameList("CLIENT");
		modelAndView.addObject("clients", clients);
		Integer count = this.htlIntoRecordCustomizeService.countHtlIntoRecord(productIntoRecordCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			productIntoRecordCustomize.setLimitStart(paginator.getOffset());
			productIntoRecordCustomize.setLimitEnd(paginator.getLimit());
			List<ProductIntoRecordCustomize> recordList  = this.htlIntoRecordCustomizeService.getRecordList(productIntoRecordCustomize);
			form.setPaginator(paginator);
			//modelAndView.addObject("recordList", recordList);
			form.setRecordList(recordList);
		}
		modelAndView.addObject(ProductIntoRecordCustomizeDefine.PRODUCTINTORECORD_FORM, form);
	}


	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(ProductIntoRecordCustomizeDefine.EXPORTEXECL)
	@RequiresPermissions(ProductIntoRecordCustomizeDefine.PRODUCTINTORECORD_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, ProductIntoRecordCustomizeBean form) throws Exception {
		LogUtil.startLog(THIS_CLASS, ProductIntoRecordCustomizeDefine.EXPORTEXECL);
		// 表格sheet名称
		String sheetName = "汇天利转入记录";
		ProductIntoRecordCustomize productIntoRecordCustomize = new ProductIntoRecordCustomize();
		String userid = request.getParameter("userid");
		if(StringUtils.isNotEmpty(userid)){
			productIntoRecordCustomize.setUserId(Integer.parseInt(userid));
			request.setAttribute("userid", userid);
		}
		if(StringUtils.isNotEmpty(form.getUsernameSrh())){
			productIntoRecordCustomize.setUsername(form.getUsernameSrh());
		}
		if(StringUtils.isNotEmpty(form.getRefernameSrh())){
			productIntoRecordCustomize.setRefername(form.getRefernameSrh());;
		}
		if(StringUtils.isNotEmpty(form.getTimeStartSrch())){
			productIntoRecordCustomize.setTimeStartSrch(GetDate.get10Time(form.getTimeStartSrch()));
		}
		if(StringUtils.isNotEmpty(form.getTimeStartSrch())){
			productIntoRecordCustomize.setTimeEndSrch(GetDate.get10Time(form.getTimeEndSrch()));
		}
		if(StringUtils.isNotEmpty(form.getClientSrh())){
			productIntoRecordCustomize.setClient(Integer.parseInt(form.getClientSrh()));
		}
		if(StringUtils.isNotEmpty(form.getInvestStatusSrh())){
			productIntoRecordCustomize.setInvestStatus(Integer.parseInt(form.getInvestStatusSrh()));
		}

		List<ProductExportIntoRecordCustomize> resultList  = this.htlIntoRecordCustomizeService.exportExcel(productIntoRecordCustomize);
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
		String[] titles = new String[] {"序号", "汇盈订单号", "用户名","推荐人(当前)","推荐人ID(当前)","推荐人(出借时)","推荐人ID(出借时)","一级部门","二级部门","团队","转入金额","实际到账","本金总额","操作平台","状态","转入时间" };
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
					ProductExportIntoRecordCustomize pInfo = resultList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					else if (celLength == 1) {
						cell.setCellValue(pInfo.getOrderId());
					}
					else if (celLength == 2) {
						if(pInfo.getUsername() == null){
							cell.setCellValue("");
						}else{
							cell.setCellValue(pInfo.getUsername());
						}
					}
					else if (celLength == 3) {
						if(pInfo.getCurReferName() == null){
							cell.setCellValue("");
						}else{
							cell.setCellValue(pInfo.getCurReferName());
						}
					}
					else if (celLength == 4) {
						if(pInfo.getCurReferId() == null || pInfo.getCurReferId() == 0){
							cell.setCellValue("");
						}else{
							cell.setCellValue(pInfo.getCurReferId());
						}
					}
					else if (celLength == 5) {
						if(pInfo.getIvtReferName() == null){
							cell.setCellValue("");
						}else{
							cell.setCellValue(pInfo.getIvtReferName());
						}
					}
					else if (celLength == 6) {
						if(pInfo.getIvtReferId() == null || pInfo.getIvtReferId() == 0){
							cell.setCellValue("");
						}else{
							cell.setCellValue(pInfo.getIvtReferId());
						}
					}
					else if (celLength == 7) {
						if(pInfo.getRegionName() == null){
							cell.setCellValue("");
						}else{
							cell.setCellValue(pInfo.getRegionName());
						}
					}
					else if (celLength == 8) {
						if(pInfo.getBranceName() == null){
							cell.setCellValue("");
						}else{
							cell.setCellValue(pInfo.getBranceName());
						}
					}
					else if (celLength == 9) {
						if(pInfo.getDepartmentName() == null){
							cell.setCellValue("");
						}else{
							cell.setCellValue(pInfo.getDepartmentName());
						}
					}
					else if (celLength == 10) {
						cell.setCellValue(pInfo.getAmount().doubleValue());
					}
					else if (celLength == 11) {
						if(pInfo.getRealAmount() == null){
							cell.setCellValue(0);
						}else{
							cell.setCellValue(pInfo.getRealAmount().doubleValue());
						}
					}
					else if (celLength == 12) {
						cell.setCellValue(pInfo.getBalance().doubleValue());
					}
					else if (celLength == 13) {
						cell.setCellValue(pInfo.getClient());
					}
					else if (celLength == 14) {
						cell.setCellValue(pInfo.getInvestStatus());
					}
					else if (celLength == 15) {
						cell.setCellValue(pInfo.getInvestTime());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(THIS_CLASS, ProductIntoRecordCustomizeDefine.EXPORTEXECL);
	}


}
