package com.hyjf.admin.htl.productredeem;

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
import com.hyjf.mybatis.model.customize.ProductExportOutRecordCustomize;
import com.hyjf.mybatis.model.customize.ProductRedeemCustomize;
/**
 * @package com.hyjf.admin.htl.productredeem
 * @author Michael
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = ProductRedeemDefine.REQUEST_MAPPING)
public class ProductRedeemControllor extends BaseController {

	private static final String THIS_CLASS = ProductRedeemControllor.class.getName();
	@Autowired
	private ProductRedeemService  productRedeemService;
	@Autowired
	private HtlCommonService htlCommonService;
	/**
	 * 汇天利转出画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ProductRedeemDefine.INIT)
    @RequiresPermissions(ProductRedeemDefine.PRODUCTREDEEMCUSTOMIZE_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute(ProductRedeemDefine.PRODUCTREDEEMCUSTOMIZE_FORM) ProductRedeemBean form) {
		LogUtil.startLog(THIS_CLASS, ProductRedeemDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(ProductRedeemDefine.LIST_PATH);
		String userid = request.getParameter("userid");
		if(StringUtils.isNotEmpty(userid)){
			form.setUserId(Integer.parseInt(userid));
		}
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, ProductRedeemDefine.INIT);
		return modelAndView;
	}

	/**
	 * 汇天利转出查询
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ProductRedeemDefine.SEARCH_ACTION)
    @RequiresPermissions(ProductRedeemDefine.PRODUCTREDEEMCUSTOMIZE_SEARCH)
	public ModelAndView search(HttpServletRequest request, ProductRedeemBean form) {
		LogUtil.startLog(THIS_CLASS, ProductRedeemDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(ProductRedeemDefine.LIST_PATH);
		String userid = request.getParameter("userid");
		if(StringUtils.isNotEmpty(userid)){
			form.setUserId(Integer.parseInt(userid));
		}
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, ProductRedeemDefine.INIT);
		return modelAndView;
	}
	
	/**
	 * 汇天利转出列表分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, ProductRedeemBean form) {
		ProductRedeemCustomize productRedeemCustomize = new ProductRedeemCustomize();
		if(form.getUserId() != null){
			productRedeemCustomize.setUserId(form.getUserId());
		}
		if(StringUtils.isNotEmpty(form.getOrderIdSrh())){
			productRedeemCustomize.setOrderId(form.getOrderIdSrh());
		}
		if(StringUtils.isNotEmpty(form.getUsernameSrh())){
			productRedeemCustomize.setUsername(form.getUsernameSrh());
		}
		if(StringUtils.isNotEmpty(form.getRefernameSrh())){
			productRedeemCustomize.setRefername(form.getRefernameSrh());
		}
		if(StringUtils.isNotEmpty(form.getClientSrh())){
			productRedeemCustomize.setClient(Integer.parseInt(form.getClientSrh()));
		}
		if(StringUtils.isNotEmpty(form.getStatusSrh())){
			productRedeemCustomize.setStatus(Integer.parseInt(form.getStatusSrh()));
		}
		if(StringUtils.isNotEmpty(form.getTimeStartSrch())){
			productRedeemCustomize.setTimeStartSrch(GetDate.get10Time(form.getTimeStartSrch()));
		}
		if(StringUtils.isNotEmpty(form.getTimeStartSrch())){
			productRedeemCustomize.setTimeEndSrch(GetDate.get10Time(form.getTimeEndSrch()));
		}
		//操作平台
		List<ParamName> clients=this.htlCommonService.getParamNameList("CLIENT");
		modelAndView.addObject("clients", clients);
		Integer count = this.productRedeemService.countProductRedeemRecord(productRedeemCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			productRedeemCustomize.setLimitStart(paginator.getOffset());
			productRedeemCustomize.setLimitEnd(paginator.getLimit());
			List<ProductRedeemCustomize> recordList  = this.productRedeemService.getRecordList(productRedeemCustomize);
			form.setPaginator(paginator);
			form.setRecordList(recordList);
		}
		modelAndView.addObject(ProductRedeemDefine.PRODUCTREDEEMCUSTOMIZE_FORM, form);
		
	}

	
	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(ProductRedeemDefine.EXPORTEXECL)
	@RequiresPermissions(ProductRedeemDefine.PRODUCTREDEEMCUSTOMIZE_EXEPORTEXECL)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, ProductRedeemBean form) throws Exception {
		LogUtil.startLog(THIS_CLASS, ProductRedeemDefine.EXPORTEXECL);
		// 表格sheet名称
		String sheetName = "汇天利转出记录";
		ProductRedeemCustomize productRedeemCustomize = new ProductRedeemCustomize();
		if(form.getUserId() != null){
			productRedeemCustomize.setUserId(form.getUserId());
		}
		if(StringUtils.isNotEmpty(form.getOrderIdSrh())){
			productRedeemCustomize.setOrderId(form.getOrderIdSrh());
		}
		if(StringUtils.isNotEmpty(form.getUsernameSrh())){
			productRedeemCustomize.setUsername(form.getUsernameSrh());
		}
		if(StringUtils.isNotEmpty(form.getRefernameSrh())){
			productRedeemCustomize.setRefername(form.getRefernameSrh());
		}
		if(StringUtils.isNotEmpty(form.getClientSrh())){
			productRedeemCustomize.setClient(Integer.parseInt(form.getClientSrh()));
		}
		if(StringUtils.isNotEmpty(form.getStatusSrh())){
			productRedeemCustomize.setStatus(Integer.parseInt(form.getStatusSrh()));
		}
		if(StringUtils.isNotEmpty(form.getTimeStartSrch())){
			productRedeemCustomize.setTimeStartSrch(GetDate.get10Time(form.getTimeStartSrch()));
		}
		if(StringUtils.isNotEmpty(form.getTimeStartSrch())){
			productRedeemCustomize.setTimeEndSrch(GetDate.get10Time(form.getTimeEndSrch()));
		}
		List<ProductExportOutRecordCustomize> resultList  = this.productRedeemService.exportExcel(productRedeemCustomize);
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
		String[] titles = new String[] { "序号", "汇盈订单号","用户名","推荐人(当前)","推荐人ID(当前)","推荐人(出借时)","推荐人ID(出借时)","一级部门","二级部门","团队","转出金额","实际到账","操作平台","状态","转出时间" };
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
					ProductExportOutRecordCustomize pInfo = resultList.get(i);

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
						cell.setCellValue(pInfo.getRealAmount().doubleValue());
					}
					else if (celLength == 12) {
						cell.setCellValue(pInfo.getClient());
					}
					else if (celLength == 13) {
						cell.setCellValue(pInfo.getStatus());
					}
					else if (celLength == 14) {
						cell.setCellValue(pInfo.getRedeemTime());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(THIS_CLASS, ProductRedeemDefine.EXPORTEXECL);
	}

	

}
