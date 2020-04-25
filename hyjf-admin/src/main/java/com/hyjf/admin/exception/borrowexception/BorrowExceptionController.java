/**
 * Description:可以删除异常项目的列表,只查看初审中和待发布项目信息
 * Copyright: Copyright (HYJF Corporation)2016
 * Company: HYJF Corporation
 * @author: zhuxiaodong
 * @version: 1.0
 * Created at: 2016年3月8日 上午9:06:32
 * Modification History:
 * Modified by : 
 */
package com.hyjf.admin.exception.borrowexception;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.BorrowCustomize;
import com.hyjf.mybatis.model.customize.BorrowExceptionDeleteBean;
import com.hyjf.mybatis.model.customize.BorrowExceptionDeleteSrchBean;

/**
 * @package com.hyjf.admin.exception.borrowexception
 * @author 朱晓东
 * @date 2016/03/08 9:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = BorrowExceptionDefine.REQUEST_MAPPING)
public class BorrowExceptionController extends BaseController {

	@Autowired
	private BorrowExceptionService borrowExceptionService;

	@Autowired
	private BorrowCommonService borrowCommonService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowExceptionDefine.INIT)
	@RequiresPermissions(BorrowExceptionDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, BorrowExceptionBean form) {
		LogUtil.startLog(BorrowExceptionController.class.toString(), BorrowExceptionDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(BorrowExceptionDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BorrowExceptionController.class.toString(), BorrowExceptionDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowExceptionDefine.SEARCH_ACTION)
	@RequiresPermissions(BorrowExceptionDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, BorrowExceptionBean form) {
		LogUtil.startLog(BorrowExceptionController.class.toString(), BorrowExceptionDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(BorrowExceptionDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BorrowExceptionController.class.toString(), BorrowExceptionDefine.INIT);
		return modelAndView;
	}

	/**
	 * 查看删除的项目画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowExceptionDefine.DELETEINIT)
	@RequiresPermissions(BorrowExceptionDefine.PERMISSIONSLOG_SEARCH)
	public ModelAndView borrowdeleteinit(HttpServletRequest request, BorrowExceptionDeleteSrchBean form) {
		LogUtil.startLog(BorrowExceptionController.class.toString(), BorrowExceptionDefine.DELETEINIT);
		ModelAndView modelAndView = new ModelAndView(BorrowExceptionDefine.DELETE_LIST_PATH);
		// 创建分页
		this.createBorrowDeletePage(request, modelAndView, form);
		LogUtil.endLog(BorrowExceptionController.class.toString(), BorrowExceptionDefine.DELETEINIT);
		return modelAndView;
	}

	/**
	 * 删除
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowExceptionDefine.DELETE_ACTION)
	@RequiresPermissions(BorrowExceptionDefine.PERMISSIONS_DELETE)
	public String deleteRecordAction(HttpServletRequest request, RedirectAttributes attr, BorrowExceptionBean form) {
		LogUtil.startLog(BorrowExceptionController.class.toString(), BorrowExceptionDefine.DELETE_ACTION);
		if (StringUtils.isNotEmpty(form.getBorrowNid())) {
			String nid = form.getBorrowNid();
			if (nid != null && !"".equals(nid)) {
				this.borrowExceptionService.deleteBorrowByNid(form.getBorrowNid());
			} else {
				attr.addFlashAttribute(BorrowExceptionDefine.BORROW_FORM, form);
				LogUtil.endLog(BorrowExceptionController.class.toString(), BorrowExceptionDefine.DELETE_ACTION);
				return "redirect:" + BorrowExceptionDefine.REQUEST_MAPPING + "/" + BorrowExceptionDefine.INIT;
			}
		}
		attr.addFlashAttribute(BorrowExceptionDefine.BORROW_FORM, form);
		LogUtil.endLog(BorrowExceptionController.class.toString(), BorrowExceptionDefine.DELETE_ACTION);
		return "redirect:" + BorrowExceptionDefine.REQUEST_MAPPING + "/" + BorrowExceptionDefine.INIT;
	}

	/**
	 * 标的撤销
	 * 
	 * @param request
	 * @param attr
	 * @return
	 */
	@RequestMapping(BorrowExceptionDefine.REVOKE_ACTION)
	@RequiresPermissions(BorrowExceptionDefine.PERMISSIONS_REVOKE)
	public String revokeAction(HttpServletRequest request, RedirectAttributes attr, BorrowExceptionBean form) {
		// 标的号
		String borrowNid = request.getParameter("borrowNid");
		if (StringUtils.isNotEmpty(borrowNid)) {
			try {
				this.borrowExceptionService.updateBorrowByNid(form.getBorrowNid());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			attr.addFlashAttribute(BorrowExceptionDefine.BORROW_FORM, form);
			LogUtil.endLog(BorrowExceptionController.class.toString(), BorrowExceptionDefine.REVOKE_ACTION);
			return "redirect:" + BorrowExceptionDefine.REQUEST_MAPPING + "/" + BorrowExceptionDefine.INIT;
		}
		attr.addFlashAttribute(BorrowExceptionDefine.BORROW_FORM, form);
		LogUtil.endLog(BorrowExceptionController.class.toString(), BorrowExceptionDefine.DELETE_ACTION);
		return "redirect:" + BorrowExceptionDefine.REQUEST_MAPPING + "/" + BorrowExceptionDefine.INIT;
	}

	/**
	 * 项目异常页导出
	 * 
	 * @param request
	 * @param response
	 * @param form
	 */
	@RequestMapping(BorrowExceptionDefine.EXPORT_ACTION)
	@RequiresPermissions(BorrowExceptionDefine.PERMISSIONS_EXPORT)
	public void exportBorrowExceptionExcel(HttpServletRequest request, HttpServletResponse response,
			BorrowExceptionBean form) {
		// 表格sheet名称
		String sheetName = "项目异常列表";

		BorrowCommonCustomize corrowCommonCustomize = new BorrowCommonCustomize();
		// 借款编码
		corrowCommonCustomize.setBorrowNidSrch(form.getBorrowNidSrch());
		// 项目名称
		corrowCommonCustomize.setBorrowNameSrch(form.getBorrowNameSrch());
		// 借 款 人
		corrowCommonCustomize.setUsernameSrch(form.getUsernameSrch());
		// 项目状态
		if (form.getStatusSrch() != null && StringUtils.isNotEmpty(form.getStatusSrch().trim())) {
			if (form.getStatusSrch().trim().equals("0") || form.getStatusSrch().trim().equals("10")) {
				corrowCommonCustomize.setStatusSrch(form.getStatusSrch());
			} else {
				corrowCommonCustomize.setStatusSrch(null);
			}
		} else {
			corrowCommonCustomize.setStatusSrch(null);
		}
		// 项目类型
		corrowCommonCustomize.setProjectTypeSrch(form.getProjectTypeSrch());
		// 还款方式
		corrowCommonCustomize.setBorrowStyleSrch(form.getBorrowStyleSrch());
		// 放款时间
		corrowCommonCustomize.setRecoverTimeStartSrch(form.getRecoverTimeStartSrch());

		// 添加时间
		corrowCommonCustomize.setTimeStartSrch(form.getTimeStartSrch());
		// 添加时间
		corrowCommonCustomize.setTimeEndSrch(form.getTimeEndSrch());
		// 取得数据
		corrowCommonCustomize.setLimitStart(-1);
		corrowCommonCustomize.setLimitEnd(-1);
		List<BorrowCustomize> recordList = this.borrowExceptionService.selectBorrowList(corrowCommonCustomize);
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + ".xls";

		String[] titles = new String[] { "序号", "项目编号", "项目名称", "借款人", "担保机构名", "项目类型", "借款金额", "借款期限", "出借利率", "还款方式",
				"项目状态", "复审人员", "添加时间" };

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
					sheet = ExportExcel
							.createHSSFWorkbookTitle(workbook, titles, (sheetName + "_第" + sheetCount + "页"));
					rowNum = 1;
				}

				// 新建一行
				Row row = sheet.createRow(rowNum);
				// 循环数据
				for (int celLength = 0; celLength < titles.length; celLength++) {
					BorrowCustomize record = recordList.get(i);
					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 项目编号
					else if (celLength == 1) {
						cell.setCellValue(record.getBorrowNid());
					}
					// 项目名称
					else if (celLength == 2) {
						cell.setCellValue(record.getBorrowName());
					}
					// 借款人
					else if (celLength == 3) {
						cell.setCellValue(record.getUsername());
					}
					// 担保机构
					else if (celLength == 4) {
						cell.setCellValue(record.getRepay_org_name());
					}
					// 项目类型
					else if (celLength == 5) {
						cell.setCellValue(record.getProjectTypeName());
					}
					// 借款金额
					else if (celLength == 6) {
						cell.setCellValue(record.getAccount());
					}
					// 借款期限
					else if (celLength == 7) {
						cell.setCellValue(record.getBorrowPeriod());
					}
					// 出借利率
					else if (celLength == 8) {
						cell.setCellValue(record.getBorrowApr());
					}
					// 还款方式
					else if (celLength == 9) {
						cell.setCellValue(record.getBorrowStyleName());
					}
					// 项目状态
					else if (celLength == 10) {
						cell.setCellValue(record.getStatus());
					}
					// 复审人员
					else if (celLength == 11) {
						cell.setCellValue("");
					}
					// 添加时间
					else if (celLength == 12) {
						cell.setCellValue(record.getAddtime());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, BorrowExceptionBean form) {

		// 项目类型
		List<BorrowProjectType> borrowProjectTypeList = this.borrowCommonService.borrowProjectTypeList("");
		modelAndView.addObject("borrowProjectTypeList", borrowProjectTypeList);

		// 还款方式
		List<BorrowStyle> borrowStyleList = this.borrowCommonService.borrowStyleList("");
		modelAndView.addObject("borrowStyleList", borrowStyleList);

		BorrowCommonCustomize corrowCommonCustomize = new BorrowCommonCustomize();
		// 借款编码
		corrowCommonCustomize.setBorrowNidSrch(form.getBorrowNidSrch());
		// 项目名称
		corrowCommonCustomize.setBorrowNameSrch(form.getBorrowNameSrch());
		// 借 款 人
		corrowCommonCustomize.setUsernameSrch(form.getUsernameSrch());
		// 项目状态
		if (form.getStatusSrch() != null && StringUtils.isNotEmpty(form.getStatusSrch().trim())) {
			if (form.getStatusSrch().trim().equals("0") || form.getStatusSrch().trim().equals("10")) {
				corrowCommonCustomize.setStatusSrch(form.getStatusSrch());
			} else {
				corrowCommonCustomize.setStatusSrch(null);
			}
		} else {
			corrowCommonCustomize.setStatusSrch(null);
		}
		// 项目类型
		corrowCommonCustomize.setProjectTypeSrch(form.getProjectTypeSrch());
		// 还款方式
		corrowCommonCustomize.setBorrowStyleSrch(form.getBorrowStyleSrch());
		// 放款时间
		corrowCommonCustomize.setRecoverTimeStartSrch(form.getRecoverTimeStartSrch());

		// 添加时间
		corrowCommonCustomize.setTimeStartSrch(form.getTimeStartSrch());
		// 添加时间
		corrowCommonCustomize.setTimeEndSrch(form.getTimeEndSrch());

		corrowCommonCustomize.setSort(form.getSort());
		corrowCommonCustomize.setCol(form.getCol());

		Long count = this.borrowExceptionService.countBorrow(corrowCommonCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			corrowCommonCustomize.setLimitStart(paginator.getOffset());
			corrowCommonCustomize.setLimitEnd(paginator.getLimit());
			List<BorrowCustomize> recordList = this.borrowExceptionService.selectBorrowList(corrowCommonCustomize);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
			BigDecimal sumAccount = this.borrowExceptionService.sumAccount(corrowCommonCustomize);
			modelAndView.addObject("sumAccount", sumAccount);
		}
		modelAndView.addObject(BorrowExceptionDefine.BORROW_FORM, form);
	}

	/**
	 * 创建查看已删除borrow数据列表分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createBorrowDeletePage(HttpServletRequest request, ModelAndView modelAndView,
			BorrowExceptionDeleteSrchBean form) {

		Long count = this.borrowExceptionService.countBorrowDelete(form);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			form.setLimitStart(paginator.getOffset());
			form.setLimitEnd(paginator.getLimit());
			List<BorrowExceptionDeleteBean> recordList = this.borrowExceptionService.selectBorrowDeleteList(form);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}
		modelAndView.addObject(BorrowExceptionDefine.BORROW_DELETE_FORM, form);
	}

	/**
	 * 项目异常删除页导出
	 * 
	 * @param request
	 * @param response
	 * @param form
	 */
	@RequestMapping(BorrowExceptionDefine.EXPORT_BORROWDEL_EXCEL_ACTION)
	@RequiresPermissions(BorrowExceptionDefine.PERMISSIONS_EXPORT)
	public void exportBorrowDelExcel(HttpServletRequest request, HttpServletResponse response,
			BorrowExceptionDeleteSrchBean form) {
		// 表格sheet名称
		String sheetName = "项目异常删除列表";
		form.setLimitStart(-1);
		form.setLimitEnd(-1);
		List<BorrowExceptionDeleteBean> recordList = this.borrowExceptionService.selectBorrowDeleteList(form);
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + ".xls";

		String[] titles = new String[] { "序号", "项目编号", "项目名称", "借款人", "借款金额", "借款期限", "保证金", "项目状态", "添加时间", "操作类型",
				"操作时间", "操作人" };

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
					sheet = ExportExcel
							.createHSSFWorkbookTitle(workbook, titles, (sheetName + "_第" + sheetCount + "页"));
					rowNum = 1;
				}
				// 新建一行
				Row row = sheet.createRow(rowNum);
				// 循环数据
				for (int celLength = 0; celLength < titles.length; celLength++) {
					BorrowExceptionDeleteBean record = recordList.get(i);
					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 项目编号
					else if (celLength == 1) {
						cell.setCellValue(record.getBorrow_nid());
					}
					// 项目名称
					else if (celLength == 2) {
						cell.setCellValue(record.getBorrow_name());
					}
					// 借款人
					else if (celLength == 3) {
						cell.setCellValue(record.getUsername());
					}
					// 借款金额
					else if (celLength == 4) {
						cell.setCellValue(record.getAccount());
					}
					// 借款期限
					else if (celLength == 5) {
						cell.setCellValue(record.getBorrow_period());
					}
					// 保证金
					else if (celLength == 6) {
						cell.setCellValue(record.getBail_num() == null ? "0.00" : String.valueOf(record.getBail_num()));
					}
					// 项目状态
					else if (celLength == 7) {
						cell.setCellValue(record.getStatus());
					}
					// 添加时间
					else if (celLength == 8) {
						cell.setCellValue(record.getAddtime());
					}
					// 操作类型
					else if (celLength == 9) {
						cell.setCellValue(record.getOperater_type() == 0 ? "删除" : "撤销");
					}
					// 操作时间
					else if (celLength == 10) {
						cell.setCellValue(record.getOperater_time_str());
					}
					// 操作人
					else if (celLength == 11) {
						cell.setCellValue(record.getOperater_user());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
	}
}
