package com.hyjf.admin.manager.user.bankcardlog;

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

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.BankAccountLog;
import com.hyjf.mybatis.model.auto.BankConfig;
import com.hyjf.mybatis.model.auto.ParamName;

/**
 * 用户绑卡操作记录
 * 
 * @author 李深强
 *
 */
@Controller
@RequestMapping(value = BankAccountLogDefine.REQUEST_MAPPING)
public class BankAccountLogController extends BaseController {

	@Autowired
	private BankAccountLogService bankAccountLogService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BankAccountLogDefine.INIT)
	@RequiresPermissions(BankAccountLogDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute(BankAccountLogDefine.FORM) BankAccountLogBean form) {
		LogUtil.startLog(BankAccountLogController.class.toString(), BankAccountLogDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(BankAccountLogDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BankAccountLogController.class.toString(), BankAccountLogDefine.INIT);
		return modelAndView;
	}

	/**
	 * 条件查询
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BankAccountLogDefine.SEARCH_ACTION)
	@RequiresPermissions(BankAccountLogDefine.PERMISSIONS_SEARCH)
	public ModelAndView searchAction(HttpServletRequest request, @ModelAttribute(BankAccountLogDefine.FORM) BankAccountLogBean form) {
		LogUtil.startLog(BankAccountLogController.class.toString(), BankAccountLogDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(BankAccountLogDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BankAccountLogController.class.toString(), BankAccountLogDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 分页机能 页面初始化
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, BankAccountLogBean form) {
		
		// 所属银行
		List<BankConfig> banks = this.bankAccountLogService.getBankcardList();
		modelAndView.addObject("banks", banks);
		
		// 银行卡属性
		List<ParamName> bankcardProperty = this.bankAccountLogService.getParamNameList("BANKCARD_PROPERTY");
		modelAndView.addObject("bankcardProperty", bankcardProperty);

		Integer count = this.bankAccountLogService.getRecordCount(form);
		if(count > 0){
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			form.setPaginator(paginator);
			List<BankAccountLog> recordList = this.bankAccountLogService.getRecordList(form, paginator.getOffset(), paginator.getLimit());
			form.setRecordList(recordList);
		}
		modelAndView.addObject(BankAccountLogDefine.FORM, form);
	}

	
	
	/**
	 * 导出报表
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(BankAccountLogDefine.EXPORT_ACTION)
	@RequiresPermissions(BankAccountLogDefine.PERMISSIONS_EXPORT)
	public void exportExcel(@ModelAttribute BankAccountLogBean form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		LogUtil.startLog(BankAccountLogController.class.toString(), BankAccountLogDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "用户绑卡操作记录";
		// 文件名称
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date())
				+ CustomConstants.EXCEL_EXT;
				// 需要输出的结果列表
		//列表
		List<BankAccountLog> recordList = this.bankAccountLogService.getRecordList(form, -1, -1);
		// 银行卡属性
		List<ParamName> bankcardProperty = this.bankAccountLogService.getParamNameList("BANKCARD_PROPERTY");
		
		String[] titles = new String[] { "序号", "用户名", "银行账号", "所属银行", "操作", "银行卡属性", "操作时间" };
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
					sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, (sheetName + "_第" + sheetCount + "页"));
					rowNum = 1;
				}

				// 新建一行
				Row row = sheet.createRow(rowNum);
				// 循环数据
				for (int celLength = 0; celLength < titles.length; celLength++) {
					BankAccountLog user = recordList.get(i);
					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					if (celLength == 0) {// 序号
						cell.setCellValue(i + 1);
					} else if (celLength == 1) {// 用户名
						cell.setCellValue(user.getUserName());
					} else if (celLength == 2) {// 银行账号
						cell.setCellValue(user.getBankCode());
					} else if (celLength == 3) {// 所属银行
						cell.setCellValue(user.getBankName());
					} else if (celLength == 4) {// 操作
						if(user.getOperationType() == 0){
							cell.setCellValue("绑定");
						}else{
							cell.setCellValue("删除");
						}
					} else if (celLength == 5) {// 银行卡属性
						for(int p = 0;p < bankcardProperty.size(); p++){
							if(user.getCardType() == Integer.valueOf(bankcardProperty.get(p).getNameCd())){
								cell.setCellValue(bankcardProperty.get(p).getName());
							}
						}
					} else if (celLength == 6) {// 添加时间
						cell.setCellValue(GetDate.timestamptoStrYYYYMMDDHHMMSS(user.getCreateTime()));
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
		LogUtil.endLog(BankAccountLogController.class.toString(), BankAccountLogDefine.EXPORT_ACTION);
	}
	
	
}
