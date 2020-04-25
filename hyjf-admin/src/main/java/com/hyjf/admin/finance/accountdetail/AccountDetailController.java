package com.hyjf.admin.finance.accountdetail;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.AccountTrade;
import com.hyjf.mybatis.model.customize.AccountDetailCustomize;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * 资金管理
 * 
 * @author HBZ
 */
@Controller
@RequestMapping(value = AccountDetailDefine.REQUEST_MAPPING)
public class AccountDetailController extends BaseController {

	@Autowired
	private AccountDetailService accountDetailService;

	/**
	 * 分页
	 * 
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modeAndView, AccountDetailBean form) {
		// 交易类型列表
		List<AccountTrade> trades = this.accountDetailService.selectTradeTypes();
		form.setTradeList(trades);
		AccountDetailCustomize accountDetailCustomize = new AccountDetailCustomize();
		accountDetailCustomize.setType(form.getType());
		accountDetailCustomize.setUsername(form.getUsername());
		accountDetailCustomize.setReferrerName(form.getReferrerName());
		accountDetailCustomize.setNid(form.getNid());
		accountDetailCustomize.setAccountId(form.getAccountId());
		accountDetailCustomize.setSeqNo(form.getSeqNo());
		accountDetailCustomize.setIsBank(form.getIsBank());
		accountDetailCustomize.setCheckStatus(form.getCheckStatus());
		accountDetailCustomize.setTradeStatus(form.getTradeStatus());
		accountDetailCustomize.setTypeSearch(form.getTypeSearch());
		accountDetailCustomize.setStartDate(form.getStartDate());
		accountDetailCustomize.setEndDate(form.getEndDate());
		accountDetailCustomize.setTradeTypeSearch(form.getTradeTypeSearch());
		
		accountDetailCustomize.setRemarkSrch(form.getRemarkSrch());// 备注查询
		
		Integer count = this.accountDetailService.queryAccountDetailCount(accountDetailCustomize);
		if (count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			accountDetailCustomize.setLimitStart(paginator.getOffset());
			accountDetailCustomize.setLimitEnd(paginator.getLimit());

			List<AccountDetailCustomize> accountDetails = this.accountDetailService.queryAccountDetails(accountDetailCustomize);
			form.setPaginator(paginator);
			form.setRecordList(accountDetails);
			modeAndView.addObject(AccountDetailDefine.ACCOUNTDETAIL_FORM, form);
		} else {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			accountDetailCustomize.setLimitStart(paginator.getOffset());
			accountDetailCustomize.setLimitEnd(paginator.getLimit());
			form.setPaginator(paginator);
			modeAndView.addObject(AccountDetailDefine.ACCOUNTDETAIL_FORM, form);
		}

	}

	/**
	 * 资金明细 列表
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AccountDetailDefine.ACCOUNTDETAIL_LIST)
	@RequiresPermissions(AccountDetailDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, AccountDetailBean form) {
		LogUtil.startLog(AccountDetailController.class.toString(), AccountDetailDefine.ACCOUNTDETAIL_LIST);
		ModelAndView modeAndView = new ModelAndView(AccountDetailDefine.LIST_PATH);
		// 创建分页
		// sql优化，只拉取10天的数据
		//form.setStartDate(GetDate.date2Str(GetDate.getTodayBeforeOrAfter(-10), new SimpleDateFormat("yyyy-MM-dd")));
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(AccountDetailController.class.toString(), AccountDetailDefine.ACCOUNTDETAIL_LIST);
		return modeAndView;
	}
	
	/**
	 * 资金明细过滤查询
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AccountDetailDefine.SEARCH_ACTION)
	@RequiresPermissions(AccountDetailDefine.PERMISSIONS_VIEW)
	public ModelAndView search(HttpServletRequest request, AccountDetailBean form) {
		LogUtil.startLog(AccountDetailController.class.toString(), AccountDetailDefine.SEARCH_ACTION);
		ModelAndView modeAndView = new ModelAndView(AccountDetailDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(AccountDetailController.class.toString(), AccountDetailDefine.SEARCH_ACTION);
		return modeAndView;
	}

	/**
	 * 导出资金明细列表
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(AccountDetailDefine.EXPORT_ACCOUNTDETAIL_ACTION)
	@RequiresPermissions(AccountDetailDefine.PERMISSIONS_ACCOUNTDETAIL_EXPORT)
	public void exportAccountsExcel(HttpServletRequest request, HttpServletResponse response, AccountDetailBean form) throws Exception {
		// 表格sheet名称
		String sheetName = "资金明细";

		// 取得数据
		form.setLimitStart(-1);
		form.setLimitEnd(-1);

		// 设置默认查询时间
		if (StringUtils.isEmpty(form.getStartDate())) {
			form.setStartDate(GetDate.getDate("yyyy-MM-dd"));
		}
		if (StringUtils.isEmpty(form.getEndDate())) {
			form.setEndDate(GetDate.getDate("yyyy-MM-dd"));
		}

		List<AccountDetailCustomize> recordList = this.accountDetailService.queryAccountDetails(form);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + ".xls";

		String[] titles = new String[] { "序号", "明细ID", "用户名", "电子账号", "推荐人", "推荐组", "资金托管平台", "流水号", "订单号", "操作类型", "交易类型", "操作金额", "银行总资产", "银行可用余额", "银行冻结金额", "汇付可用金额", "汇付冻结金额", "智投服务可用余额",
				"智投服务冻结金额", "交易状态", "对账状态", "备注说明", "时间" };
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
					AccountDetailCustomize accountDetailCustomize = recordList.get(i);

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
					// 电子账号
					else if (celLength == 3) {
						cell.setCellValue(accountDetailCustomize.getAccountId());
					}
					// 推荐人
					else if (celLength == 4) {
						cell.setCellValue(accountDetailCustomize.getReferrerName());
					}
					// 推荐组
					else if (celLength == 5) {
						cell.setCellValue(accountDetailCustomize.getReferrerGroup());
					}
					// 资金托管平台
					else if (celLength == 6) {
						cell.setCellValue(StringUtils.isEmpty(accountDetailCustomize.getIsBank()) ? "" : accountDetailCustomize.getIsBank().equals("1") ? "江西银行" : "汇付天下");
					}
					// 流水号
					else if (celLength == 7) {
						cell.setCellValue(accountDetailCustomize.getSeqNo());
					}
					// 订单号
					else if (celLength == 8) {
						cell.setCellValue(accountDetailCustomize.getNid());
					}
					// 操作类型
					else if (celLength == 9) {
						cell.setCellValue(accountDetailCustomize.getType());
					}
					// 交易类型
					else if (celLength == 10) {
						cell.setCellValue(accountDetailCustomize.getTradeType());
					}
					// 操作金额
					else if (celLength == 11) {
						cell.setCellValue(accountDetailCustomize.getAmount() + "");
					}
					// 银行总资产
					else if (celLength == 12) {
						cell.setCellValue(accountDetailCustomize.getBankTotal() + "");
					}
					// 银行可用余额
					else if (celLength == 13) {
						cell.setCellValue(accountDetailCustomize.getBankBalance() + "");
					}
					// 银行冻结金额
					else if (celLength == 14) {
						cell.setCellValue(accountDetailCustomize.getBankFrost() + "");
					}
					// 汇付可用金额
					else if (celLength == 15) {
						cell.setCellValue(accountDetailCustomize.getBalance() + "");
					}
					// 汇付冻结金额
					else if (celLength == 16) {
						cell.setCellValue(accountDetailCustomize.getFrost() + "");
					}
					// 汇添金可用金额
					else if (celLength == 17) {
						cell.setCellValue(accountDetailCustomize.getPlanBalance() + "");
					}
					// 汇添金冻结金额
					else if (celLength == 18) {
						cell.setCellValue(accountDetailCustomize.getPlanFrost() + "");
					}
					// 交易状态
					else if (celLength == 19) {
						cell.setCellValue(StringUtils.isEmpty(accountDetailCustomize.getTradeStatus()) ? "" : accountDetailCustomize.getTradeStatus().equals("0") ? "失败" : "成功" + "");
					}
					// 对账状态
					else if (celLength == 20) {
						cell.setCellValue(StringUtils.isEmpty(accountDetailCustomize.getCheckStatus()) ? "" : accountDetailCustomize.getCheckStatus().equals("0") ? "未对账" : "已对账" + "");
					}
					// 备注说明
					else if (celLength == 21) {
						cell.setCellValue(accountDetailCustomize.getRemark());
					}
					// 时间
					else if (celLength == 22) {
						cell.setCellValue(accountDetailCustomize.getCreateTime());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

	}

	/**
	 * 20170120还款后,交易明细修复
	 * 
	 * @param request
	 * @param modeAndView
	 * @param form
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(AccountDetailDefine.ACCOUNTDETAIL_DATA_REPAIR)
	@RequiresPermissions(AccountDetailDefine.PERMISSIONS_ACCOUNTDETAIL_DATA_REPAIR)
	public String repayDataRepair(HttpServletRequest request, ModelAndView modeAndView, AccountDetailBean form) throws Exception {
		LogUtil.startLog(AccountDetailController.class.toString(), AccountDetailDefine.ACCOUNTDETAIL_DATA_REPAIR);
		this.accountDetailService.repayDataRepair();
		return "redirect:" + AccountDetailDefine.REQUEST_MAPPING + "/" + AccountDetailDefine.ACCOUNTDETAIL_LIST;
	}

}
