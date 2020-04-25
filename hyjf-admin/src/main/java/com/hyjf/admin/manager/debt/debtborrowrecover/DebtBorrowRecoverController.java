package com.hyjf.admin.manager.debt.debtborrowrecover;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowRecoverCustomize;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = DebtBorrowRecoverDefine.REQUEST_MAPPING)
public class DebtBorrowRecoverController extends BaseController {

	@Autowired
	private DebtBorrowRecoverService borrowRecoverService;

	@Autowired
	private BorrowCommonService borrowCommonService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(DebtBorrowRecoverDefine.INIT)
	@RequiresPermissions(DebtBorrowRecoverDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, DebtBorrowRecoverBean form) {
		LogUtil.startLog(DebtBorrowRecoverController.class.toString(), DebtBorrowRecoverDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(DebtBorrowRecoverDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(DebtBorrowRecoverController.class.toString(), DebtBorrowRecoverDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(DebtBorrowRecoverDefine.SEARCH_ACTION)
	@RequiresPermissions(DebtBorrowRecoverDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, DebtBorrowRecoverBean form) {
		LogUtil.startLog(DebtBorrowRecoverController.class.toString(), DebtBorrowRecoverDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(DebtBorrowRecoverDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(DebtBorrowRecoverController.class.toString(), DebtBorrowRecoverDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, DebtBorrowRecoverBean form) {

		DebtBorrowRecoverCustomize debtBorrowRecoverCustomize = new DebtBorrowRecoverCustomize();

		// 项目编号 检索条件
		debtBorrowRecoverCustomize.setBorrowNidSrch(form.getBorrowNidSrch());
		// 项目名称 检索条件
		debtBorrowRecoverCustomize.setBorrowNameSrch(form.getBorrowNameSrch());
		// 出借人 检索条件
		debtBorrowRecoverCustomize.setUsernameSrch(form.getUsernameSrch());
		// 出借订单号 检索条件
		debtBorrowRecoverCustomize.setOrderNumSrch(form.getOrderNumSrch());
		// 放款状态 检索条件
		debtBorrowRecoverCustomize.setIsRecoverSrch(form.getIsRecoverSrch());
		// 出借时间 检索条件
		debtBorrowRecoverCustomize.setTimeRecoverStartSrch(StringUtils.isNotBlank(form.getTimeRecoverStartSrch())?form.getTimeRecoverStartSrch():null);
		// 出借时间 检索条件
		debtBorrowRecoverCustomize.setTimeRecoverEndSrch(StringUtils.isNotBlank(form.getTimeRecoverEndSrch())?form.getTimeRecoverEndSrch():null);
		// 出借时间 检索条件
		debtBorrowRecoverCustomize.setTimeStartSrch(StringUtils.isNotBlank(form.getTimeStartSrch())?form.getTimeStartSrch():null);
		// 出借时间 检索条件
		debtBorrowRecoverCustomize.setTimeEndSrch(StringUtils.isNotBlank(form.getTimeEndSrch())?form.getTimeEndSrch():null);
		// 放款订单号
		debtBorrowRecoverCustomize.setLoanOrdid(form.getLoanOrdid());

		// 放款状态
		List<ParamName> recoverList = this.borrowCommonService.getParamNameList(CustomConstants.BORROW_RECOVER);
		modelAndView.addObject("recoverList", recoverList);

		Long count = this.borrowRecoverService.countBorrowRecover(debtBorrowRecoverCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			debtBorrowRecoverCustomize.setLimitStart(paginator.getOffset());
			debtBorrowRecoverCustomize.setLimitEnd(paginator.getLimit());
			List<DebtBorrowRecoverCustomize> recordList = this.borrowRecoverService.selectBorrowRecoverList(debtBorrowRecoverCustomize);
			DebtBorrowRecoverCustomize sumAccount = this.borrowRecoverService.sumBorrowRecoverList(debtBorrowRecoverCustomize);
			modelAndView.addObject("sumAccount", sumAccount);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}

		modelAndView.addObject(DebtBorrowRecoverDefine.BORROW_FORM, form);
	}

	/**
	 * 数据导出
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(DebtBorrowRecoverDefine.EXPORT_ACTION)
	@RequiresPermissions(DebtBorrowRecoverDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, DebtBorrowRecoverBean form) {
		LogUtil.startLog(DebtBorrowRecoverController.class.toString(), DebtBorrowRecoverDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "放款明细";

		DebtBorrowRecoverCustomize debtBorrowRecoverCustomize = new DebtBorrowRecoverCustomize();

		// 项目编号 检索条件
		debtBorrowRecoverCustomize.setBorrowNidSrch(form.getBorrowNidSrch());
		// 项目名称 检索条件
		debtBorrowRecoverCustomize.setBorrowNameSrch(form.getBorrowNameSrch());
		// 出借人 检索条件
		debtBorrowRecoverCustomize.setUsernameSrch(form.getUsernameSrch());
		// 出借订单号 检索条件
		debtBorrowRecoverCustomize.setOrderNumSrch(form.getOrderNumSrch());
		// 放款状态 检索条件
		debtBorrowRecoverCustomize.setIsRecoverSrch(form.getIsRecoverSrch());
		// 出借时间 检索条件
		debtBorrowRecoverCustomize.setTimeRecoverStartSrch(StringUtils.isNotBlank(form.getTimeRecoverStartSrch())?form.getTimeRecoverStartSrch():null);
		// 出借时间 检索条件
		debtBorrowRecoverCustomize.setTimeRecoverEndSrch(StringUtils.isNotBlank(form.getTimeRecoverEndSrch())?form.getTimeRecoverEndSrch():null);
		// 出借时间 检索条件
		debtBorrowRecoverCustomize.setTimeStartSrch(StringUtils.isNotBlank(form.getTimeStartSrch())?form.getTimeStartSrch():null);
		// 出借时间 检索条件
		debtBorrowRecoverCustomize.setTimeEndSrch(StringUtils.isNotBlank(form.getTimeEndSrch())?form.getTimeEndSrch():null);
		// 放款订单号
		debtBorrowRecoverCustomize.setLoanOrdid(form.getLoanOrdid());

		List<DebtBorrowRecoverCustomize> resultList = this.borrowRecoverService.exportBorrowRecoverList(debtBorrowRecoverCustomize);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

        String[] titles = new String[] { "序号", "项目编号", "借款人ID", "借款人用户名", "项目名称", "项目类型", "借款期限", "出借利率", "还款方式", "出借订单号","放款订单号", "出借人用户名", "出借人ID", "出借时间", "授权服务金额", "应放款金额", "应收服务费", "实际放款金额", "实收服务费",
                "放款状态", "放款时间" };
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
					DebtBorrowRecoverCustomize record = resultList.get(i);

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
					// 借款人ID
					else if (celLength == 2) {
						cell.setCellValue(record.getUserId());
					}
					// 借款人用户名
					else if (celLength == 3) {
						cell.setCellValue(record.getUsername());
					}
					// 项目名称
					else if (celLength == 4) {
						cell.setCellValue(record.getBorrowName());
					}
					// 项目类型
					else if (celLength == 5) {
						cell.setCellValue(record.getBorrowProjectTypeName());
					}
					// 借款期限
					else if (celLength == 6) {
						cell.setCellValue(record.getBorrowPeriod());
					}
					// 出借利率
					else if (celLength == 7) {
						cell.setCellValue(record.getBorrowApr());
					}
					// 还款方式
					else if (celLength == 8) {
						cell.setCellValue(record.getBorrowStyleName());
					}
					// 出借订单号
					else if (celLength == 9) {
						cell.setCellValue(record.getOrderNum());
					}
					// 出借订单号
					else if (celLength == 10) {
						cell.setCellValue(record.getLoanOrdid());
					}
					// 出借人用户名
					else if (celLength == 11) {
						cell.setCellValue(record.getTenderUsername());
					}
					// 出借人ID
					else if (celLength == 12) {
						cell.setCellValue(record.getTenderUserId());
					}
					// 出借时间
					else if (celLength == 13) {
						cell.setCellValue(record.getAddtime());
					}
					// 出借金额
					else if (celLength == 14) {
						cell.setCellValue(record.getAccount());
					}
					// 应放款金额
					else if (celLength == 15) {
						cell.setCellValue(record.getAccountYes());
					}
					// 应收服务费
					else if (celLength == 16) {
						cell.setCellValue(record.getLoanFee());
					}
					// 实际放款金额
					else if (celLength == 17) {
						cell.setCellValue(record.getRecoverPrice());
					}
					// 实收服务费
					else if (celLength == 18) {
						cell.setCellValue(record.getServicePrice());
					}
					// 放款状态
					else if (celLength == 19) {
						cell.setCellValue(record.getIsRecover());
					}
					// 放款时间
					else if (celLength == 20) {
						cell.setCellValue(record.getTimeRecover());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
		LogUtil.endLog(DebtBorrowRecoverController.class.toString(), DebtBorrowRecoverDefine.EXPORT_ACTION);
	}
}
