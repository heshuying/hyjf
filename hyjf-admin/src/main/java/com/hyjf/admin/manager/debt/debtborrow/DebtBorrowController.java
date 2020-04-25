package com.hyjf.admin.manager.debt.debtborrow;

import java.math.BigDecimal;
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
import com.hyjf.admin.manager.debt.debtborrowcommon.DebtBorrowCommonService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCustomize;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = DebtBorrowDefine.REQUEST_MAPPING)
public class DebtBorrowController extends BaseController {

	@Autowired
	private DebtBorrowService borrowService;

	@Autowired
	private DebtBorrowCommonService debtBorrowCommonService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(DebtBorrowDefine.INIT)
	@RequiresPermissions(DebtBorrowDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute("borrowBean") DebtBorrowBean form) {
		LogUtil.startLog(DebtBorrowController.class.toString(), DebtBorrowDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(DebtBorrowDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(DebtBorrowController.class.toString(), DebtBorrowDefine.INIT);
		return modelAndView;
	}

	
	/**
	 * 画面迁移
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(DebtBorrowDefine.PREVIEW_ACTION)
	@RequiresPermissions(DebtBorrowDefine.PERMISSIONS_PREVIEW)
	public ModelAndView previewAction(HttpServletRequest request, @ModelAttribute(DebtBorrowDefine.BORROW_FORM) DebtBorrowBean form) {
		LogUtil.startLog(DebtBorrowController.class.toString(), DebtBorrowDefine.PREVIEW_ACTION);
		ModelAndView modelAndView = new ModelAndView(DebtBorrowDefine.PREVIEW_PATH);
		modelAndView.addObject("previewUrl", "https://www.hyjf.com/project/getProjectPreview.do?borrowNid="+form.getBorrowNid());
		LogUtil.endLog(DebtBorrowController.class.toString(), DebtBorrowDefine.INFO_ACTION);
		return modelAndView;
	}
	
	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(DebtBorrowDefine.SEARCH_ACTION)
	@RequiresPermissions(DebtBorrowDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, DebtBorrowBean form) {
		LogUtil.startLog(DebtBorrowController.class.toString(), DebtBorrowDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(DebtBorrowDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(DebtBorrowController.class.toString(), DebtBorrowDefine.INIT);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, DebtBorrowBean form) {

		// 项目类型
		List<BorrowProjectType> borrowProjectTypeList = this.debtBorrowCommonService.borrowProjectTypeList("");
		modelAndView.addObject("borrowProjectTypeList", borrowProjectTypeList);

		// 还款方式
		List<BorrowStyle> borrowStyleList = this.debtBorrowCommonService.borrowStyleList("");
		modelAndView.addObject("borrowStyleList", borrowStyleList);

		// 项目状态
		List<ParamName> borrowStatusList = this.debtBorrowCommonService.getParamNameList(CustomConstants.BORROW_STATUS);
		modelAndView.addObject("borrowStatusList", borrowStatusList);

		DebtBorrowCommonCustomize corrowCommonCustomize = new DebtBorrowCommonCustomize();
		// 借款编码
		corrowCommonCustomize.setBorrowNidSrch(form.getBorrowNidSrch());
		// 项目名称
		corrowCommonCustomize.setBorrowNameSrch(form.getBorrowNameSrch());
		// 借 款 人
		corrowCommonCustomize.setUsernameSrch(form.getUsernameSrch());
		// 项目状态
		corrowCommonCustomize.setStatusSrch(form.getStatusSrch());
		// 项目类型
		corrowCommonCustomize.setProjectTypeSrch(form.getProjectTypeSrch());
		// 还款方式
		corrowCommonCustomize.setBorrowStyleSrch(form.getBorrowStyleSrch());
		// 放款时间
		corrowCommonCustomize.setRecoverTimeStartSrch(StringUtils.isNotBlank(form.getRecoverTimeStartSrch())?form.getRecoverTimeStartSrch():null);
		// 放款时间
		corrowCommonCustomize.setRecoverTimeEndSrch(StringUtils.isNotBlank(form.getRecoverTimeEndSrch())?form.getRecoverTimeEndSrch():null);
		// 添加时间
		corrowCommonCustomize.setTimeStartSrch(StringUtils.isNotBlank(form.getTimeStartSrch())?form.getTimeStartSrch():null);
		// 添加时间
		corrowCommonCustomize.setTimeEndSrch(StringUtils.isNotBlank(form.getTimeEndSrch())?form.getTimeEndSrch():null);

		corrowCommonCustomize.setSort(form.getSort());
		corrowCommonCustomize.setCol(form.getCol());
		corrowCommonCustomize.setBorrowPeriod(form.getBorrowPeriod());;

		Long count = this.borrowService.countBorrow(corrowCommonCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			corrowCommonCustomize.setLimitStart(paginator.getOffset());
			corrowCommonCustomize.setLimitEnd(paginator.getLimit());
			List<DebtBorrowCustomize> recordList = this.borrowService.selectBorrowList(corrowCommonCustomize);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
			BigDecimal sumAccount = this.borrowService.sumAccount(corrowCommonCustomize);
			modelAndView.addObject("sumAccount", sumAccount);
		}
		modelAndView.addObject(DebtBorrowDefine.BORROW_FORM, form);
	}

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(DebtBorrowDefine.EXPORT_ACTION)
	@RequiresPermissions(DebtBorrowDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, DebtBorrowBean form) throws Exception {
		LogUtil.startLog(DebtBorrowController.class.toString(), DebtBorrowDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "借款列表";

		DebtBorrowCommonCustomize corrowCommonCustomize = new DebtBorrowCommonCustomize();
		// 借款编码
		corrowCommonCustomize.setBorrowNidSrch(form.getBorrowNidSrch());
		// 项目名称
		corrowCommonCustomize.setBorrowNameSrch(form.getBorrowNameSrch());
		// 借 款 人
		corrowCommonCustomize.setUsernameSrch(form.getUsernameSrch());
		// 项目状态
		corrowCommonCustomize.setStatusSrch(form.getStatusSrch());
		// 项目类型
		corrowCommonCustomize.setProjectTypeSrch(form.getProjectTypeSrch());
		// 还款方式
		corrowCommonCustomize.setBorrowStyleSrch(form.getBorrowStyleSrch());
		// 放款时间
		corrowCommonCustomize.setRecoverTimeStartSrch(StringUtils.isNotBlank(form.getRecoverTimeStartSrch())?form.getRecoverTimeStartSrch():null);
		// 放款时间
		corrowCommonCustomize.setRecoverTimeEndSrch(StringUtils.isNotBlank(form.getRecoverTimeEndSrch())?form.getRecoverTimeEndSrch():null);
		// 添加时间
		corrowCommonCustomize.setTimeStartSrch(StringUtils.isNotBlank(form.getTimeStartSrch())?form.getTimeStartSrch():null);
		// 添加时间
		corrowCommonCustomize.setTimeEndSrch(StringUtils.isNotBlank(form.getTimeEndSrch())?form.getTimeEndSrch():null);

		List<DebtBorrowCommonCustomize> resultList = this.borrowService.exportBorrowList(corrowCommonCustomize);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "序号", "项目编号", "借款人ID", "借款人用户名", "项目申请人", "项目名称", "项目类型", "借款金额（元）", "借款期限", "出借利率", "还款方式", "放款服务费率", "还款服务费率", "合作机构", "已借到金额", "剩余金额", "借款进度", "项目状态", "添加时间",
				"初审通过时间", "定时发标时间","预约开始时间","预约截止时间", "实际发标时间", "出借截止时间", "满标时间", "复审通过时间", "放款完成时间", "最后还款日" };
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
					DebtBorrowCommonCustomize borrowCommonCustomize = resultList.get(i);

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
					// 借款人ID
					else if (celLength == 2) {
						cell.setCellValue(borrowCommonCustomize.getUserId());
					}
					// 借款人用户名
					else if (celLength == 3) {
						cell.setCellValue(borrowCommonCustomize.getUsername());
					}
					// 项目申请人
					else if (celLength == 4) {
						cell.setCellValue(borrowCommonCustomize.getApplicant());
					}
					// 项目名称
					else if (celLength == 5) {
						cell.setCellValue(borrowCommonCustomize.getBorrowName());
					}
					// 项目类型
					else if (celLength == 6) {
						cell.setCellValue(borrowCommonCustomize.getBorrowProjectTypeName());
					}
					// 借款金额（元）
					else if (celLength == 7) {
						cell.setCellValue(borrowCommonCustomize.getAccount());
					}
					// 借款期限
					else if (celLength == 8) {
						cell.setCellValue(borrowCommonCustomize.getBorrowPeriod());
					}
					// 出借利率
					else if (celLength == 9) {
						cell.setCellValue(borrowCommonCustomize.getBorrowApr());
					}
					// 还款方式
					else if (celLength == 10) {
						cell.setCellValue(borrowCommonCustomize.getBorrowStyle());
					}
					// 放款服务费率
					else if (celLength == 11) {
						cell.setCellValue(borrowCommonCustomize.getBorrowServiceScale());
					}
					// 还款服务费率
					else if (celLength == 12) {
						cell.setCellValue(borrowCommonCustomize.getBorrowManagerScale());
					}
					// 合作机构
					else if (celLength == 13) {
						cell.setCellValue(borrowCommonCustomize.getBorrowMeasuresInstit());
					}
					// 已借到金额
					else if (celLength == 14) {
						cell.setCellValue(borrowCommonCustomize.getBorrowAccountYes());
					}
					// 剩余金额
					else if (celLength == 15) {
						cell.setCellValue(borrowCommonCustomize.getBorrowAccountWait());
					}
					// 借款进度
					else if (celLength == 16) {
						cell.setCellValue(borrowCommonCustomize.getBorrowAccountScale());
					}
					// 项目状态
					else if (celLength == 17) {
						cell.setCellValue(borrowCommonCustomize.getStatus());
					}
					// 添加时间
					else if (celLength == 18) {
						cell.setCellValue(borrowCommonCustomize.getAddtime());
					}
					// 初审通过时间
					else if (celLength == 19) {
						cell.setCellValue(borrowCommonCustomize.getVerifyOverTime());
					}
					// 定时发标时间
					else if (celLength == 20) {
						cell.setCellValue(borrowCommonCustomize.getOntime());
					}
					// 预约开始时间
					else if (celLength == 21) {
						if(!borrowCommonCustomize.getStatus().equals("待发布")){
							cell.setCellValue(borrowCommonCustomize.getBookingBeginTime());
						}
					}
					// 预约截止时间
					else if (celLength == 22) {
						if(!borrowCommonCustomize.getStatus().equals("待发布")){
							cell.setCellValue(borrowCommonCustomize.getBookingEndTime());
						}
					}
					// 实际发标时间
					else if (celLength == 23) {
						cell.setCellValue(borrowCommonCustomize.getVerifyTime());
					}
					// 投稿截止时间
					else if (celLength == 24) {
						cell.setCellValue(borrowCommonCustomize.getBorrowValidTime());
					}
					// 满标时间
					else if (celLength == 25) {
						cell.setCellValue(borrowCommonCustomize.getBorrowFullTime());
					}
					// 复审通过时间
					else if (celLength == 26) {
						cell.setCellValue(borrowCommonCustomize.getReverifyTime());
					}
					// 放款完成时间
					else if (celLength == 27) {
						cell.setCellValue(borrowCommonCustomize.getRecoverLastTime());
					}
					// 最后还款日
					else if (celLength == 28) {
						cell.setCellValue(borrowCommonCustomize.getRepayLastTime());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(DebtBorrowController.class.toString(), DebtBorrowDefine.EXPORT_ACTION);
	}
}
