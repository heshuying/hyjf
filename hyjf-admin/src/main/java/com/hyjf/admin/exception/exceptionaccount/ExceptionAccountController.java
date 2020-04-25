package com.hyjf.admin.exception.exceptionaccount;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.ExceptionAccount;

@Controller
@RequestMapping(value = ExceptionAccountDefine.REQUEST_MAPPING)
public class ExceptionAccountController extends BaseController {
	@Autowired
	private ExceptionAccountService exceptionAccountService;

	@RequestMapping(ExceptionAccountDefine.EXCEPTIONACCOUNT_LIST_ACTION)
	@RequiresPermissions(ExceptionAccountDefine.PERMISSIONS_VIEW)
	public ModelAndView searchExceptionAccountList(HttpServletRequest request,
			@ModelAttribute(ExceptionAccountDefine.EXCEPTIONACCOUNT_FORM) ExceptionAccountBean form) {
		LogUtil.startLog(ExceptionAccountDefine.THIS_CLASS, ExceptionAccountDefine.EXCEPTIONACCOUNT_LIST_ACTION);
		ModelAndView modelAndView = new ModelAndView(ExceptionAccountDefine.GOTO_LIST);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(ExceptionAccountDefine.THIS_CLASS, ExceptionAccountDefine.EXCEPTIONACCOUNT_LIST_ACTION);
		return modelAndView;
	}

	/**
	 * 查询异常账户
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, ExceptionAccountBean form) {
		int recordTotal = exceptionAccountService.countRecordTotal(form.getUsername(),form.getCustomId(),form.getMobile());
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
			List<ExceptionAccount> recordList = this.exceptionAccountService.searchRecord(form.getUsername(),form.getCustomId(),form.getMobile(),paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(ExceptionAccountDefine.EXCEPTIONACCOUNT_FORM, form);
		}
	}
	
	/**
	 * 账户信息同步
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ExceptionAccountDefine.SYNC)
	public String sync(HttpServletRequest request, ExceptionAccountBean form,RedirectAttributes redirectAttributes) {
		LogUtil.startLog(ExceptionAccountDefine.THIS_CLASS, ExceptionAccountDefine.SYNC);
		exceptionAccountService.syncAccount(form.getId());
		LogUtil.endLog(ExceptionAccountDefine.THIS_CLASS, ExceptionAccountDefine.SYNC);
		redirectAttributes.addAttribute("paginatorPage", form.getPaginatorPage());
		redirectAttributes.addAttribute("username", form.getUsername());
		redirectAttributes.addAttribute("customId", form.getCustomId());
		redirectAttributes.addAttribute("mobile", form.getMobile());
		return "redirect:init";
	}

	/**
	 * 导出Excel
	 * 
	 * @param form
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(ExceptionAccountDefine.EXPORT)
	public void exportUserLeaveExcel(@ModelAttribute ExceptionAccountBean form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		LogUtil.startLog(ExceptionAccountDefine.THIS_CLASS, ExceptionAccountDefine.EXPORT);
		// 表格sheet名称
		String sheetName = "注册信息";
		// 文件名称
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date())
				+ CustomConstants.EXCEL_EXT;

		List<ExceptionAccount> recordList = this.exceptionAccountService.searchRecord(form.getUsername(),form.getCustomId(),form.getMobile(),-1, -1);
		String[] titles = new String[] { "序号", "用户名", "客户号", "手机号","角色", "平台可用金额", "平台冻结金额", "汇付可用金额", "汇付冻结金额" };
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
					sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles,
							(sheetName + "_第" + sheetCount + "页"));
					rowNum = 1;
				}

				// 新建一行
				Row row = sheet.createRow(rowNum);
				// 循环数据
				for (int celLength = 0; celLength < titles.length; celLength++) {
					ExceptionAccount exceptionAccount = recordList.get(i);
					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					if (celLength == 0) {// 序号
						cell.setCellValue(i + 1);
					} else if (celLength == 1) {
						cell.setCellValue(exceptionAccount.getUsername());
					} else if (celLength == 2) {
						cell.setCellValue(exceptionAccount.getCustomId());
					} else if (celLength == 3) {
						cell.setCellValue(exceptionAccount.getMobile());
					} else if (celLength == 4) {
						cell.setCellValue(exceptionAccount.getRole());
					} else if (celLength == 5) {
						cell.setCellValue(exceptionAccount.getBalancePlat().toString());
					} else if (celLength == 6) {
						cell.setCellValue(exceptionAccount.getFrostPlat().toString());
					} else if (celLength == 7) {
						cell.setCellValue(exceptionAccount.getBalanceHuifu().toString());
					} else if (celLength == 8) {
						cell.setCellValue(exceptionAccount.getFrostHuifu().toString());
					}else {
						long time = Long.valueOf(exceptionAccount.getCreateTime()) * 1000;
						Date date = new Date(time);
						SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String s = dateformat.format(date);
						cell.setCellValue(s);
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
		LogUtil.endLog(ExceptionAccountDefine.THIS_CLASS, ExceptionAccountDefine.EXPORT);
	}
}
