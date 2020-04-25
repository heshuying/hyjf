package com.hyjf.admin.manager.plan.accountdetail;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.AccountTrade;
import com.hyjf.mybatis.model.customize.admin.htj.DebtAccountDetailCustomize;

/**
 * 资金管理
 * 
 * @author HBZ
 */
@Controller
@RequestMapping(value = HtjAccountDetailDefine.REQUEST_MAPPING)
public class HtjAccountDetailController extends BaseController {

	@Autowired
	private HtjAccountDetailService accountDetailService;

	/**
	 * 分页
	 * 
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modeAndView, HtjAccountDetailBean form) {
		// 交易类型列表
		List<AccountTrade> trades = this.accountDetailService.selectTradeTypes();
		form.setTradeList(trades);

		DebtAccountDetailCustomize accountDetailCustomize = new DebtAccountDetailCustomize();
		accountDetailCustomize.setType(form.getType());
		accountDetailCustomize.setUsername(form.getUsername());
		accountDetailCustomize.setReferrerName(form.getReferrerName());
		accountDetailCustomize.setNid(form.getNid());
		accountDetailCustomize.setStartDate(StringUtils.isNotBlank(form.getStartDate())?form.getStartDate():null);
		accountDetailCustomize.setEndDate(StringUtils.isNotBlank(form.getEndDate())?form.getEndDate():null);
		accountDetailCustomize.setTradeTypeSearch(form.getTradeTypeSearch());

		Integer count = this.accountDetailService.queryAccountDetailCount(accountDetailCustomize);
		if (count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			accountDetailCustomize.setLimitStart(paginator.getOffset());
			accountDetailCustomize.setLimitEnd(paginator.getLimit());

			List<DebtAccountDetailCustomize> accountDetails = this.accountDetailService.queryAccountDetails(accountDetailCustomize);
			form.setPaginator(paginator);
			form.setRecordList(accountDetails);
			modeAndView.addObject(HtjAccountDetailDefine.ACCOUNTDETAIL_FORM, form);
		} else {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			accountDetailCustomize.setLimitStart(paginator.getOffset());
			accountDetailCustomize.setLimitEnd(paginator.getLimit());
			form.setPaginator(paginator);
			modeAndView.addObject(HtjAccountDetailDefine.ACCOUNTDETAIL_FORM, form);
		}

	}

	/**
	 * 资金明细 列表
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(HtjAccountDetailDefine.ACCOUNTDETAIL_LIST)
	@RequiresPermissions(HtjAccountDetailDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, HtjAccountDetailBean form) {
		LogUtil.startLog(HtjAccountDetailController.class.toString(), HtjAccountDetailDefine.ACCOUNTDETAIL_LIST);
		ModelAndView modeAndView = new ModelAndView(HtjAccountDetailDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(HtjAccountDetailController.class.toString(), HtjAccountDetailDefine.ACCOUNTDETAIL_LIST);
		return modeAndView;
	}

	/**
	 * 导出资金明细列表
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(HtjAccountDetailDefine.EXPORT_ACCOUNTDETAIL_ACTION)
	@RequiresPermissions(HtjAccountDetailDefine.PERMISSIONS_ACCOUNTDETAIL_EXPORT)
	public void exportAccountsExcel(HttpServletRequest request, HttpServletResponse response, HtjAccountDetailBean form) throws Exception {
		// 表格sheet名称
		String sheetName = "资金明细";

		// 取得数据
		form.setLimitStart(-1);
		form.setLimitEnd(-1);
		List<DebtAccountDetailCustomize> recordList = this.accountDetailService.queryAccountDetails(form);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + ".xls";

		String[] titles = new String[] { "序号", "明细ID", "用户名", "推荐人", "推荐组", "订单号", "操作类型", "交易类型", "操作金额", "可用余额", "冻结金额", "智投服务可用余额", "智投服务冻结金额", "计划订单可用余额", "计划订单冻结金额", "备注说明", "时间" };
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
					DebtAccountDetailCustomize accountDetailCustomize = recordList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}

					// 明细ID
					else if (celLength == 1) {
						cell.setCellValue(accountDetailCustomize.getId());
					}
					// 用户名
					else if (celLength == 2) {
						cell.setCellValue(accountDetailCustomize.getUsername());
					}
					// 推荐人
					else if (celLength == 3) {
						cell.setCellValue(accountDetailCustomize.getReferrerName());
					}
					// 推荐组
					else if (celLength == 4) {
						cell.setCellValue(accountDetailCustomize.getReferrerGroup());
					}
					// 订单号
					else if (celLength == 5) {
						cell.setCellValue(accountDetailCustomize.getNid());
					}
					// 操作类型
					else if (celLength == 6) {
						cell.setCellValue(accountDetailCustomize.getType());
					}
					// 交易类型
					else if (celLength == 7) {
						cell.setCellValue(accountDetailCustomize.getTradeType());
					}
					// 操作金额
					else if (celLength == 8) {
						cell.setCellValue(accountDetailCustomize.getAmount() + "");
					}
					// 可用余额
					else if (celLength == 9) {
						cell.setCellValue(accountDetailCustomize.getBalance() + "");
					}
					// 冻结金额
					else if (celLength == 10) {
						cell.setCellValue(accountDetailCustomize.getFrost() + "");
					}
					// 智投服务可用余额
					else if (celLength == 11) {
						cell.setCellValue(accountDetailCustomize.getPlanBalance() == null ? BigDecimal.ZERO.toString() : accountDetailCustomize.getPlanBalance().toString());
					}
					// 智投服务冻结金额
					else if (celLength == 12) {
						cell.setCellValue(accountDetailCustomize.getPlanFrost() == null ? BigDecimal.ZERO.toString() : accountDetailCustomize.getPlanFrost().toString());
					}
					// 计划订单可用余额
					else if (celLength == 13) {
						cell.setCellValue(accountDetailCustomize.getPlanOrderBalance() == null ? BigDecimal.ZERO.toString() : accountDetailCustomize.getPlanOrderBalance().toString());
					}
					// 计划订单冻结金额
					else if (celLength == 14) {
						cell.setCellValue(accountDetailCustomize.getPlanOrderFrost() == null ? BigDecimal.ZERO.toString() : accountDetailCustomize.getPlanOrderFrost().toString());
					}
					// 备注说明
					else if (celLength == 15) {
						cell.setCellValue(accountDetailCustomize.getRemark());
					}
					// 时间
					else if (celLength == 16) {
						cell.setCellValue(accountDetailCustomize.getCreateTime());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

	}
}
