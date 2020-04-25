package com.hyjf.admin.manager.borrow.borrowlog;

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
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.BorrowLogCommonCustomize;
import com.hyjf.mybatis.model.customize.BorrowLogCustomize;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = BorrowDefine.REQUEST_MAPPING)
public class BorrowLogController extends BaseController {

	@Autowired
	private BorrowLogService borrowLogService;

	@Autowired
	private BorrowCommonService borrowCommonService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowDefine.INIT)
	@RequiresPermissions(BorrowDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute("borrowLoBean") BorrowLoBean form) {
		LogUtil.startLog(BorrowLogController.class.toString(), BorrowDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(BorrowDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BorrowLogController.class.toString(), BorrowDefine.INIT);
		return modelAndView;
	}


	
	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowDefine.SEARCH_ACTION)
	@RequiresPermissions(BorrowDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, BorrowLoBean form) {
		LogUtil.startLog(BorrowLogController.class.toString(), BorrowDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(BorrowDefine.LIST_PATH);		
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BorrowLogController.class.toString(), BorrowDefine.INIT);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, BorrowLoBean form) {



		// 项目状态
		List<ParamName> borrowStatusList = this.borrowCommonService.getParamNameList(CustomConstants.BORROW_STATUS);
		modelAndView.addObject("borrowStatusList", borrowStatusList);

		BorrowLogCommonCustomize borrowLogCustomize = new BorrowLogCommonCustomize();
		// 借款编码
		borrowLogCustomize.setBorrowNidSrch(form.getBorrowNidSrch());
		// 项目状态
		borrowLogCustomize.setBorrowStatusSrch(form.getStatusSrch());
		// 项目状态
		borrowLogCustomize.setTypeSrch(form.getTypeSrch());
		//操作人
		borrowLogCustomize.setCreateUserNameSrch(form.getUsernameSrch());

		// 添加时间
		borrowLogCustomize.setTimeStartSrch(form.getTimeStartSrch());
		// 添加时间
		borrowLogCustomize.setTimeEndSrch(form.getTimeEndSrch());
		borrowLogCustomize.setSort(form.getSort());
		borrowLogCustomize.setCol(form.getCol());


		Long count = this.borrowLogService.countBorrowLog(borrowLogCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			borrowLogCustomize.setLimitStart(paginator.getOffset());
			borrowLogCustomize.setLimitEnd(paginator.getLimit());
			List<BorrowLogCustomize> recordList = this.borrowLogService.selectBorrowLogList(borrowLogCustomize);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}
		String webUrl = PropUtils.getSystem("hyjf.web.host");
		modelAndView.addObject("webUrl", webUrl);
		modelAndView.addObject(BorrowDefine.BORROW_LOG_FORM, form);
	}

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(BorrowDefine.EXPORT_ACTION)
	@RequiresPermissions(BorrowDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, BorrowLoBean form) throws Exception {
		LogUtil.startLog(BorrowLogController.class.toString(), BorrowDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "借款操作日志列表";

		BorrowLogCommonCustomize borrowLogCustomize = new BorrowLogCommonCustomize();
		// 借款编码
        borrowLogCustomize.setBorrowNidSrch(form.getBorrowNidSrch());
        // 项目状态
        borrowLogCustomize.setBorrowStatusSrch(form.getStatusSrch());
        // 修改类型
        borrowLogCustomize.setTypeSrch(form.getTypeSrch());

        // 添加时间
        borrowLogCustomize.setTimeStartSrch(form.getTimeStartSrch());
        // 添加时间
        borrowLogCustomize.setTimeEndSrch(form.getTimeEndSrch());

		List<BorrowLogCustomize> resultList = this.borrowLogService.exportBorrowLogList(borrowLogCustomize);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "序号", "项目编号", "项目状态", "修改类型","操作人", "操作时间", "备注" };
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
				    BorrowLogCustomize borrowCommonCustomize = resultList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 项目编号
					else if (celLength == 1) {
						cell.setCellValue(borrowCommonCustomize.getBorrowNid());
					}
					// 项目状态
					else if (celLength == 2) {
						cell.setCellValue(borrowCommonCustomize.getBorrowStatus());
					}
					// 修改类型
                    else if (celLength == 3) {
                        cell.setCellValue(borrowCommonCustomize.getType());
                    }
					// 操作人
					else if (celLength == 4) {
						cell.setCellValue(borrowCommonCustomize.getCreateUserName());
					}
					// 操作时间
					else if (celLength == 5) {
						cell.setCellValue(borrowCommonCustomize.getCreateTime());
					}
					// 备注
					else if (celLength == 6) {
						cell.setCellValue(borrowCommonCustomize.getRemark());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(BorrowLogController.class.toString(), BorrowDefine.EXPORT_ACTION);
	}
}
