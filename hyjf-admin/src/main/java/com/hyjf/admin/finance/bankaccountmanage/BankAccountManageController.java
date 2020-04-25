package com.hyjf.admin.finance.bankaccountmanage;

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
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.annotate.Token;
import com.hyjf.admin.finance.accountdetail.AccountDetailBean;
import com.hyjf.admin.finance.accountdetail.AccountDetailController;
import com.hyjf.admin.finance.accountdetail.AccountDetailDefine;
import com.hyjf.admin.finance.accountdetail.AccountDetailService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.GetterUtil;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.AccountTrade;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.customize.AccountDetailCustomize;
import com.hyjf.mybatis.model.customize.BankAccountManageCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

/**
 * 账户管理(银行)
 * 
 * @author liuyang
 *
 */
@Controller
@RequestMapping(value = BankAccountManageDefine.REQUEST_MAPPING)
public class BankAccountManageController extends BaseController {
	/**
	 * 类名
	 */
	private static final String THIS_CLASS = BankAccountManageController.class.getName();
	@Autowired
	private BankAccountManageService accountManageService;
	@Autowired
	private AccountDetailService accountDetailService;

	/**
	 * 分页
	 *
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modeAndView, BankAccountManageBean form) {
		BankAccountManageCustomize accountInfoCustomize = new BankAccountManageCustomize();
		BeanUtils.copyProperties(form, accountInfoCustomize);

		Integer count = this.accountManageService.queryAccountCount(accountInfoCustomize);
		if (count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			accountInfoCustomize.setLimitStart(paginator.getOffset());
			accountInfoCustomize.setLimitEnd(paginator.getLimit());

			List<BankAccountManageCustomize> accountInfos = this.accountManageService.queryAccountInfos(accountInfoCustomize);
			form.setPaginator(paginator);
			form.setRecordList(accountInfos);
		}
		modeAndView.addObject(BankAccountManageDefine.ACCOUNTMANAGE_FORM, form);

	}

	/**
	 * 账户管理 列表
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BankAccountManageDefine.ACCOUNTMANAGE_LIST)
	@RequiresPermissions(BankAccountManageDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, BankAccountManageBean form) {
		LogUtil.startLog(BankAccountManageController.class.toString(), BankAccountManageDefine.ACCOUNTMANAGE_LIST);
		ModelAndView modeAndView = new ModelAndView(BankAccountManageDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(BankAccountManageController.class.toString(), BankAccountManageDefine.ACCOUNTMANAGE_LIST);
		return modeAndView;
	}

	/**
	 * 根据业务需求导出相应的表格 此处暂时为可用情况 缺陷： 1.无法指定相应的列的顺序， 2.无法配置，excel文件名，excel sheet名称
	 * 3.目前只能导出一个sheet 4.列的宽度的自适应，中文存在一定问题
	 * 5.根据导出的业务需求最好可以在导出的时候输入起止页码，因为在大数据量的情况下容易造成卡顿
	 *
	 * 导出账户列表
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(BankAccountManageDefine.ENHANCE_EXPORT_ACTION)
	@RequiresPermissions(value = {BankAccountManageDefine.PERMISSIONS_ACCOUNTMANAGE_EXPORT, BankAccountManageDefine.ENHANCE_PERMISSIONS_EXPORT}, logical = Logical.AND)
	public void enhanceExportAccountsExcel(HttpServletRequest request, HttpServletResponse response, BankAccountManageBean form) throws Exception {
		// 表格sheet名称
		String sheetName = "账户数据";

		// 取得数据
		form.setLimitStart(-1);
		form.setLimitEnd(-1);
		// 部门
		if (Validator.isNotNull(form.getCombotreeSrch())) {
			if (form.getCombotreeSrch().contains(StringPool.COMMA)) {
				String[] list = form.getCombotreeSrch().split(StringPool.COMMA);
				form.setCombotreeListSrch(list);
			} else {
				form.setCombotreeListSrch(new String[] { form.getCombotreeSrch() });
			}
		}
		List<BankAccountManageCustomize> recordList = this.accountManageService.queryAccountInfos(form);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "用户ID", "用户名", "分公司", "分部", "团队", "资产总额", "电子账号", "可用金额", "冻结金额", "银行待收", "银行待还", "银行账户", "用户手机号", "用户属性（当前）", "用户角色",
				"推荐人用户名（当前）", "推荐人姓名（当前）", "推荐人所属一级分部（当前）", "推荐人所属二级分部（当前）", "推荐人所属三级分部（当前）" };

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
					BankAccountManageCustomize bean = recordList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					// 用户ID
					if (celLength == 0) {
						cell.setCellValue(bean.getUserId());
					}
					// 用户名
					else if (celLength == 1) {
						cell.setCellValue(bean.getUsername());
					}
					// 分公司
					else if (celLength == 2) {
						cell.setCellValue(bean.getRegionName());
					}
					// 分部
					else if (celLength == 3) {
						cell.setCellValue(bean.getBranchName());
					}
					// 团队
					else if (celLength == 4) {
						cell.setCellValue(bean.getDepartmentName());
					}
					// 资产总额
					else if (celLength == 5) {
						if (bean.getBankTotal() != null) {
							cell.setCellValue(String.valueOf(bean.getBankTotal()));
						} else {
							cell.setCellValue("0.00");
						}
					}
					// 电子账号
					else if (celLength == 6) {
						cell.setCellValue(bean.getAccount() == null ? "" : bean.getAccount());
					}
					// 可用金额
					else if (celLength == 7) {
						cell.setCellValue(bean.getBankBalance() == null ? "0.00" : bean.getBankBalance().toString());
					}
					// 冻结金额
					else if (celLength == 8) {
						cell.setCellValue(bean.getBankFrost() == null ? "0.00" : bean.getBankFrost().toString());
					}
					// 待收金额
					else if (celLength == 9) {
						cell.setCellValue(bean.getBankAwait() == null ? "0.00" : bean.getBankAwait().toString());
					}
					// 待还金额
					else if (celLength == 10) {
						cell.setCellValue(bean.getBankWaitRepay() == null ? "0.00" : bean.getBankWaitRepay().toString());
					}
					// 银行账户
					else if (celLength == 11) {
						cell.setCellValue((bean.getBankBalanceCash() == null ? "0.00" : bean.getBankBalanceCash().toString()) + "/"
								+ (bean.getBankFrostCash() == null ? "0.00" : bean.getBankFrostCash().toString()));
					}
					// 手机号
					else if (celLength == 12) {
						cell.setCellValue(bean.getMobile());
					}
					// 用户属性（当前）
					else if (celLength == 13) {
						if ("0".equals(bean.getUserAttribute())) {
							cell.setCellValue("无主单");
						} else if ("1".equals(bean.getUserAttribute())) {
							cell.setCellValue("有主单");
						} else if ("2".equals(bean.getUserAttribute())) {
							cell.setCellValue("线下员工");
						} else if ("3".equals(bean.getUserAttribute())) {
							cell.setCellValue("线上员工");
						}
					}
					// 角色
					else if (celLength == 14) {
						cell.setCellValue(bean.getRoleid());
					}
					// 推荐人用户名（当前）
					else if (celLength == 15) {
						cell.setCellValue(bean.getReferrerName());
					}
					// 推荐人姓名（当前）
					else if (celLength == 16) {
						cell.setCellValue(bean.getReferrerTrueName());
					}
					// 推荐人所属一级分部（当前）
					else if (celLength == 17) {
						cell.setCellValue(bean.getReferrerRegionName());
					}
					// 推荐人所属二级分部（当前）
					else if (celLength == 18) {
						cell.setCellValue(bean.getReferrerBranchName());
					}
					// 推荐人所属三级分部（当前）
					else if (celLength == 19) {
						cell.setCellValue(bean.getReferrerDepartmentName());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
	}

	@RequestMapping(BankAccountManageDefine.EXPORT_ACCOUNTMANAGE_ACTION)
	@RequiresPermissions(BankAccountManageDefine.PERMISSIONS_ACCOUNTMANAGE_EXPORT)
	public void exportAccountsExcel(HttpServletRequest request, HttpServletResponse response, BankAccountManageBean form) throws Exception {
		// 表格sheet名称
		String sheetName = "账户数据";

		// 取得数据
		form.setLimitStart(-1);
		form.setLimitEnd(-1);
		// 部门
		if (Validator.isNotNull(form.getCombotreeSrch())) {
			if (form.getCombotreeSrch().contains(StringPool.COMMA)) {
				String[] list = form.getCombotreeSrch().split(StringPool.COMMA);
				form.setCombotreeListSrch(list);
			} else {
				form.setCombotreeListSrch(new String[] { form.getCombotreeSrch() });
			}
		}
		List<BankAccountManageCustomize> recordList = this.accountManageService.queryAccountInfos(form);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "用户ID", "用户名", "资产总额", "电子账号", "可用金额", "冻结金额", "银行待收", "银行待还", "银行账户", "用户手机号", "用户属性（当前）", "用户角色", "推荐人用户名（当前）", "推荐人姓名（当前）" };

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
					BankAccountManageCustomize bean = recordList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					// 用户ID
					if (celLength == 0) {
						cell.setCellValue(bean.getUserId());
					}
					// 用户名
					else if (celLength == 1) {
						cell.setCellValue(bean.getUsername());
					}
					// 资产总额
					else if (celLength == 2) {
						if (bean.getBankTotal() != null) {
							cell.setCellValue(String.valueOf(bean.getBankTotal()));
						} else {
							cell.setCellValue("0.00");
						}
					}
					// 电子账号
					else if (celLength == 3) {
						cell.setCellValue(bean.getAccount() == null ? "" : bean.getAccount());
					}
					// 可用金额
					else if (celLength == 4) {
						cell.setCellValue(bean.getBankBalance() == null ? "0.00" : bean.getBankBalance().toString());
					}
					// 冻结金额
					else if (celLength == 5) {
						cell.setCellValue(bean.getBankFrost() == null ? "0.00" : bean.getBankFrost().toString());
					}
					// 待收金额
					else if (celLength == 6) {
						cell.setCellValue(bean.getBankAwait() == null ? "0.00" : bean.getBankAwait().toString());
					}
					// 待还金额
					else if (celLength == 7) {
						cell.setCellValue(bean.getBankWaitRepay() == null ? "0.00" : bean.getBankWaitRepay().toString());
					}
					// 银行账户
					else if (celLength == 8) {
						cell.setCellValue((bean.getBankBalanceCash() == null ? "0.00" : bean.getBankBalanceCash().toString()) + "/"
								+ (bean.getBankFrostCash() == null ? "0.00" : bean.getBankFrostCash().toString()));
					}
					// 手机号
					else if (celLength == 9) {
						cell.setCellValue(bean.getMobile());
					}
					// 用户属性（当前）
					else if (celLength == 10) {
						if ("0".equals(bean.getUserAttribute())) {
							cell.setCellValue("无主单");
						} else if ("1".equals(bean.getUserAttribute())) {
							cell.setCellValue("有主单");
						} else if ("2".equals(bean.getUserAttribute())) {
							cell.setCellValue("线下员工");
						} else if ("3".equals(bean.getUserAttribute())) {
							cell.setCellValue("线上员工");
						}
					}
					// 角色
					else if (celLength == 11) {
						cell.setCellValue(bean.getRoleid());
					}
					// 推荐人用户名（当前）
					else if (celLength == 12) {
						cell.setCellValue(bean.getReferrerName());
					}
					// 推荐人姓名（当前）
					else if (celLength == 13) {
						cell.setCellValue(bean.getReferrerTrueName());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
	}

	/**
	 * 资金明细 列表 分页
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
		accountDetailCustomize.setUserId(form.getUserId());
		accountDetailCustomize.setUsername(form.getUsername());
		accountDetailCustomize.setStartDate(form.getStartDate());
		accountDetailCustomize.setEndDate(form.getEndDate());
		accountDetailCustomize.setReferrerName(form.getReferrerName());
		accountDetailCustomize.setNid(form.getNid());
		accountDetailCustomize.setTradeTypeSearch(form.getTradeTypeSearch());

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
	public ModelAndView DetailInit(HttpServletRequest request, AccountDetailBean form) {
		LogUtil.startLog(AccountDetailController.class.toString(), AccountDetailDefine.ACCOUNTDETAIL_LIST);
		ModelAndView modeAndView = new ModelAndView(AccountDetailDefine.LIST_PATH);
		// TODO
		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(AccountDetailController.class.toString(), AccountDetailDefine.ACCOUNTDETAIL_LIST);
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
	public void exportAccountDetailExcel(HttpServletRequest request, HttpServletResponse response, AccountDetailBean form) throws Exception {
		// 表格sheet名称
		String sheetName = "资金明细";

		// 取得数据
		form.setLimitStart(-1);
		form.setLimitEnd(-1);
		List<AccountDetailCustomize> recordList = this.accountDetailService.queryAccountDetails(form);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + ".xls";

		String[] titles = new String[] { "序号", "明细ID", "用户名", "推荐人", "推荐组", "订单号", "操作类型", "交易类型", "操作金额", "可用余额", "冻结金额", "备注说明", "时间" };
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
					// 备注说明
					else if (celLength == 11) {
						cell.setCellValue(accountDetailCustomize.getRemark());
					}
					// 时间
					else if (celLength == 12) {
						cell.setCellValue(accountDetailCustomize.getCreateTime());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

	}

	/**
	 * 更新
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(BankAccountManageDefine.UPDATE_BALANCE_ACTION)
	@RequiresPermissions(BankAccountManageDefine.PERMISSIONS_UPDATE_BALANCE)
	public String updateBalanceAction(HttpServletRequest request, @RequestBody BankAccountManageBean form) {
		LogUtil.startLog(THIS_CLASS, BankAccountManageDefine.UPDATE_BALANCE_ACTION);

		JSONObject ret = new JSONObject();

		// 用户ID
		Integer userId = GetterUtil.getInteger(form.getUserId());
		if (Validator.isNull(userId)) {
			ret.put(BankAccountManageDefine.JSON_STATUS_KEY, BankAccountManageDefine.JSON_STATUS_NG);
			ret.put(BankAccountManageDefine.JSON_RESULT_KEY, "更新发生错误,请重新操作!");
			LogUtil.errorLog(THIS_CLASS, BankAccountManageDefine.UPDATE_BALANCE_ACTION, new Exception("参数不正确[userId=" + userId + "]"));
			return ret.toString();
		}

		// 取得用户在银行的账户信息
		BankOpenAccount accountChinapnr = accountManageService.getBankOpenAccount(userId);
		// 用户未开户时,返回错误信息
		if (accountChinapnr == null) {
			ret.put(BankAccountManageDefine.JSON_STATUS_KEY, BankAccountManageDefine.JSON_STATUS_NG);
			ret.put(BankAccountManageDefine.JSON_RESULT_KEY, "用户未开户!");
			LogUtil.errorLog(THIS_CLASS, BankAccountManageDefine.UPDATE_BALANCE_ACTION, new Exception("参数不正确[userId=" + userId + "]"));
			return ret.toString();
		}

		// 账户可用余额
		BigDecimal balance = BigDecimal.ZERO;
		// 账户冻结金额
		BigDecimal frost = BigDecimal.ZERO;
		// 账面余额
		BigDecimal currBalance = BigDecimal.ZERO;

		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 版本号
		bean.setTxCode(BankCallMethodConstant.TXCODE_BALANCE_QUERY);// 交易代码
		bean.setTxDate(GetOrderIdUtils.getTxDate()); // 交易日期
		bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
		bean.setChannel(BankCallConstant.CHANNEL_PC); // 交易渠道
		bean.setAccountId(accountChinapnr.getAccount());// 电子账号
		bean.setLogOrderId(GetOrderIdUtils.getOrderId2(Integer.valueOf(userId)));// 订单号
		bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
		bean.setLogUserId(String.valueOf(userId));
		bean.setLogClient(0);// 平台
		try {
			BankCallBean resultBean = BankCallUtils.callApiBg(bean);
			if (resultBean == null) {
				LogUtil.errorLog(THIS_CLASS, BankAccountManageDefine.UPDATE_BALANCE_ACTION, new Exception("调用银行接口发生错误"));
				ret.put(BankAccountManageDefine.JSON_STATUS_KEY, BankAccountManageDefine.JSON_STATUS_NG);
				ret.put(BankAccountManageDefine.JSON_RESULT_KEY, "更新发生错误,请重新操作!");
				return ret.toString();
			}
			if (resultBean != null && BankCallStatusConstant.RESPCODE_SUCCESS.equals(resultBean.getRetCode())) {
				// 账户余额
				balance = new BigDecimal(resultBean.getAvailBal().replace(",", ""));
				// 账面余额
				currBalance = new BigDecimal(resultBean.getCurrBal().replace(",", ""));
				// 账户冻结金额
				frost = currBalance.subtract(balance);
			} else {
				LogUtil.errorLog(THIS_CLASS, BankAccountManageDefine.UPDATE_BALANCE_ACTION, new Exception("调用银行接口发生错误"));
				ret.put(BankAccountManageDefine.JSON_STATUS_KEY, BankAccountManageDefine.JSON_STATUS_NG);
				ret.put(BankAccountManageDefine.JSON_RESULT_KEY, "更新发生错误,请重新操作!");
				return ret.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		int cnt = 0;

		try {
			// 更新处理
			cnt = this.accountManageService.updateAccount(userId, balance, frost);
		} catch (Exception e) {
			LogUtil.errorLog(THIS_CLASS, BankAccountManageDefine.UPDATE_BALANCE_ACTION, e);
		}

		// 返现成功
		if (cnt > 0) {
			ret.put(BankAccountManageDefine.JSON_STATUS_KEY, BankAccountManageDefine.JSON_STATUS_OK);
			ret.put(BankAccountManageDefine.JSON_RESULT_KEY, "更新操作成功!");
		} else {
			ret.put(BankAccountManageDefine.JSON_STATUS_KEY, BankAccountManageDefine.JSON_STATUS_NG);
			ret.put(BankAccountManageDefine.JSON_RESULT_KEY, "更新发生错误,请重新操作!");
		}

		LogUtil.endLog(THIS_CLASS, BankAccountManageDefine.UPDATE_BALANCE_ACTION);
		return ret.toString();
	}

	/**
	 * 取得部门信息
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping("getCrmDepartmentList")
	@ResponseBody
	public String getCrmDepartmentListAction(@RequestBody BankAccountManageBean form) {
		// 部门
		String[] list = new String[] {};
		if (Validator.isNotNull(form.getUserId())) {
			if (form.getUserId().contains(StringPool.COMMA)) {
				list = form.getUserId().split(StringPool.COMMA);
			} else {
				list = new String[] { form.getUserId() };
			}
		}

		JSONArray ja = this.accountManageService.getCrmDepartmentList(list);
		if (ja != null) {
			return ja.toString();
		}

		return StringUtils.EMPTY;
	}

	/**
	 * 银行对账跳转页面 add by cwyang 2017/4/18
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BankAccountManageDefine.BANK_ACCOUNT_CHECK_ACTION)
	@Token(save = true)
	@RequiresPermissions(BankAccountManageDefine.PERMISSIONS_ACCOUNTCHECK)
	public ModelAndView bankAccountCheeckAction(HttpServletRequest request, @ModelAttribute BankAccountManageBean form) {

		ModelAndView modeAndView = new ModelAndView(BankAccountManageDefine.BANK_ACCOUNT_CHECK_PATH);
		modeAndView.addObject(BankAccountManageDefine.ACCOUNTMANAGE_FORM, form);
		return modeAndView;
	}

	/**
	 * 开始线下充值对账 add by cwyang 2017/4/18
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = BankAccountManageDefine.BANK_ACCOUNT_CHECK_START_ACTION, method = RequestMethod.POST)
	@Token(save = true)
	@RequiresPermissions(BankAccountManageDefine.PERMISSIONS_ACCOUNTCHECK)
	public String bankAccountCheckAction(HttpServletRequest request, @RequestBody BankAccountManageBean form) {
		int userId = Integer.parseInt(form.getUserId());
		JSONObject ret = new JSONObject();
		String startTime = form.getStartTime();
		String endTime = form.getEndTime();
		if (StringUtils.isBlank(startTime)||StringUtils.isBlank(endTime)) {
			ret.put(BankAccountManageDefine.JSON_MSG_KEY, "开始时间或结束时间不得为空!");
			ret.put(BankAccountManageDefine.JSON_STATUS_KEY, BankAccountManageDefine.JSON_STATUS_NG);
			return ret.toString();
		}
		String result = this.accountManageService.updateAccountCheck(userId, startTime, endTime);
		
		if ("success".equals(result)) {
			ret.put(BankAccountManageDefine.JSON_STATUS_KEY, BankAccountManageDefine.JSON_STATUS_OK);
		} else {
			ret.put(BankAccountManageDefine.JSON_MSG_KEY, result);
			ret.put(BankAccountManageDefine.JSON_STATUS_KEY, BankAccountManageDefine.JSON_STATUS_NG);
		}

		return ret.toString();
	}
}
