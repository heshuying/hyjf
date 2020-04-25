package com.hyjf.admin.htl.user;

import java.math.BigDecimal;
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
import com.hyjf.mybatis.model.customize.HtlUserInfoCustomize;

/**
 * 汇天利账户信息
 * @author Michael
 */
@Controller
@RequestMapping(value = HtlUserInfoDefine.REQUEST_MAPPING)
public class HtlUserInfoControllor extends BaseController {

	private static final String THIS_CLASS = HtlUserInfoControllor.class.getName();
	@Autowired
	private HtlUserInfoService htlUserInfoService;
	/**汇天利利率 */
	private static BigDecimal INTEREST_RATE = BigDecimal.ZERO;

	/**
	 * 汇天利账户信息初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(HtlUserInfoDefine.INIT)
    @RequiresPermissions(HtlUserInfoDefine.HTLUSERINFO_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute(HtlUserInfoDefine.HTLUSERINFO_FORM) HtlUserInfoBean form) {
		LogUtil.startLog(THIS_CLASS, HtlUserInfoDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(HtlUserInfoDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, HtlUserInfoDefine.INIT);
		return modelAndView;
	}

	/**
	 * 汇天利账户信息查询
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(HtlUserInfoDefine.SEARCH_ACTION)
    @RequiresPermissions(HtlUserInfoDefine.HTLUSERINFO_SEARCH)
	public ModelAndView search(HttpServletRequest request, HtlUserInfoBean form) {
		LogUtil.startLog(THIS_CLASS, HtlUserInfoDefine.HTLUSERINFO_SEARCH);
		ModelAndView modelAndView = new ModelAndView(HtlUserInfoDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, HtlUserInfoDefine.HTLUSERINFO_SEARCH);
		return modelAndView;
	}
	/**
	 * 创建汇天利用户信息分页机能
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, HtlUserInfoBean form) {
		HtlUserInfoCustomize htlUserInfoCustomize = new HtlUserInfoCustomize();
		if(StringUtils.isNotEmpty(form.getUsername())){
			htlUserInfoCustomize.setUsername(form.getUsername());
		}
		if(StringUtils.isNotEmpty(form.getRefername())){
			htlUserInfoCustomize.setRefername(form.getRefername());
		}
		Integer count = this.htlUserInfoService.countRecord(htlUserInfoCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			htlUserInfoCustomize.setLimitStart(paginator.getOffset());
			htlUserInfoCustomize.setLimitEnd(paginator.getLimit());
			List<HtlUserInfoCustomize> recordList  = this.htlUserInfoService.getRecordList(htlUserInfoCustomize);
			if(recordList != null && recordList.size() > 0){
				for(int i=0 ; i< recordList.size();i++){
					recordList.get(i).setTodayEarnings(getInterest(recordList.get(i).getPrincipal()));
				}
			}
			form.setPaginator(paginator);
			form.setRecordList(recordList);
		}
		modelAndView.addObject(HtlUserInfoDefine.HTLUSERINFO_FORM, form);
	}

	
	

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(HtlUserInfoDefine.EXEPORTEXECL)
	@RequiresPermissions(HtlUserInfoDefine.HTLUSERINFO_EXEPORTEXECL)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, HtlUserInfoBean form) throws Exception {
		LogUtil.startLog(THIS_CLASS, HtlUserInfoDefine.EXEPORTEXECL);
		// 表格sheet名称
		String sheetName = "汇天利账户信息";

		HtlUserInfoCustomize htlUserInfoCustomize = new HtlUserInfoCustomize();
		//用户名
		if(StringUtils.isNotEmpty(form.getUsername())){
			htlUserInfoCustomize.setUsername(form.getUsername());
		}
		//推荐人
		if(StringUtils.isNotEmpty(form.getRefername())){
			htlUserInfoCustomize.setRefername(form.getRefername());
		}
		List<HtlUserInfoCustomize> resultList = this.htlUserInfoService.getRecordList(htlUserInfoCustomize);
		if(resultList != null && resultList.size() > 0){
			for(int i=0 ; i< resultList.size();i++){
				resultList.get(i).setTodayEarnings(getInterest(resultList.get(i).getPrincipal()));
			}
		}
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
		String[] titles = new String[] { "序号", "用户名","姓名","手机号","推荐人","本金总额（元）","今日预计收益（元）","历史累计收益" };
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
					HtlUserInfoCustomize htlUserInfo = resultList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					else if (celLength == 1) {
						cell.setCellValue(htlUserInfo.getUsername());
					}
					else if (celLength == 2) {
						cell.setCellValue(htlUserInfo.getTruename());
					}
					else if (celLength == 3) {
						cell.setCellValue(htlUserInfo.getMobile());
					}
					else if (celLength == 4) {
						if(htlUserInfo.getRefername() == null){
							cell.setCellValue("");
						}else{
							cell.setCellValue(htlUserInfo.getRefername());
						}
					}
					else if (celLength == 5) {
						cell.setCellValue(htlUserInfo.getPrincipal().doubleValue());
					}
					else if (celLength == 6) {
						cell.setCellValue(htlUserInfo.getTodayEarnings().doubleValue());
					}
					else if (celLength == 7) {
						if(htlUserInfo.getHistoryEarnings() == null){
							cell.setCellValue("0.00");
						}else{
							cell.setCellValue(htlUserInfo.getHistoryEarnings().doubleValue());
						}
						
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(THIS_CLASS, HtlUserInfoDefine.EXEPORTEXECL);
	}
	
	
    /**
     * 新版汇天利利息算法 2016-01-27 上线
     * 汇天利利息计算    {本金 * (0.06 / 360)} 保留两位小数
     * @param amount 本金
     * @param investDate 出借时间 yyyy-MM-dd
     */
    public  BigDecimal getInterest(BigDecimal amount){
    	BigDecimal interestAmount = BigDecimal.ZERO;
    	if(amount == null || amount.compareTo(BigDecimal.ZERO) == 0 ){
    		return interestAmount;
    	}

    	if(INTEREST_RATE.compareTo(BigDecimal.ZERO) == 0){
    		INTEREST_RATE = htlUserInfoService.getProduct().getInterestRate();
    	}
    	try {
    		interestAmount = amount.multiply(INTEREST_RATE.divide(new BigDecimal(360),10,BigDecimal.ROUND_DOWN));
    		interestAmount = interestAmount.divide(BigDecimal.ONE,2,BigDecimal.ROUND_DOWN);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return interestAmount;
    }


	
}
