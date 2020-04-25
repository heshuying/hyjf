package com.hyjf.admin.finance.recharge;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.finance.transfer.TransferDefine;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.RechargeCustomize;
import com.hyjf.mybatis.model.customize.UserInfoCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 充值管理
 *
 * @author HBZ
 */
@Controller
@RequestMapping(value = RechargeDefine.REQUEST_MAPPING)
public class RechargeController extends BaseController {
	@Autowired
	private RechargeService rechargeService;

	/**
	 * 分页
	 *
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modeAndView, RechargeBean form) {
		// 用户属性
		List<ParamName> userPropertys = this.rechargeService.getParamNameList("USER_PROPERTY");
		modeAndView.addObject("userPropertys", userPropertys);
		// 充值状态
		List<ParamName> rechargeStatus = this.rechargeService.getParamNameList("RECHARGE_STATUS");
		modeAndView.addObject("rechargeStatus", rechargeStatus);
		// 资金托管平台
		List<ParamName> bankTypeList = this.rechargeService.getParamNameList("BANK_TYPE");
		modeAndView.addObject("bankTypeList", bankTypeList);
		// 银行列表
		List<BanksConfig> banks = this.rechargeService.getBankcardList();
		form.setBanksList(banks);
		RechargeCustomize rechargeCustomize = new RechargeCustomize();
		BeanUtils.copyProperties(form, rechargeCustomize);
		Integer count = this.rechargeService.queryRechargeCount(rechargeCustomize);
		if (count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			form.setPaginator(paginator);
			rechargeCustomize.setLimitStart(paginator.getOffset());
			rechargeCustomize.setLimitEnd(paginator.getLimit());
			List<RechargeCustomize> rechargeCustomizes = this.rechargeService.queryRechargeList(rechargeCustomize);
			form.setRecordList(rechargeCustomizes);
		}
		modeAndView.addObject(RechargeDefine.RECHARGE_FORM, form);
	}

	/**
	 * 账户管理 列表
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(RechargeDefine.RECHARGE_LIST)
	@RequiresPermissions(RechargeDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RechargeBean form) {
		ModelAndView modeAndView = new ModelAndView(RechargeDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modeAndView, form);
		return modeAndView;
	}

	/**
	 * 账户管理 查询条件
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(RechargeDefine.RECHARGE_LIST_WITHQ)
	@RequiresPermissions(RechargeDefine.PERMISSIONS_VIEW)
	public ModelAndView initWithQ(HttpServletRequest request, RechargeBean form) {
		LogUtil.startLog(RechargeController.class.toString(), RechargeDefine.RECHARGE_LIST);
		ModelAndView modeAndView = new ModelAndView(RechargeDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modeAndView, form);

		LogUtil.endLog(RechargeController.class.toString(), RechargeDefine.RECHARGE_LIST);
		return modeAndView;
	}

	/**
	 * 确认充值(FIX) 操作
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(RechargeDefine.RECHARGE_FIX)
	@RequiresPermissions(RechargeDefine.PERMISSIONS_FIX)
	public String rechargeFix(HttpServletRequest request, @RequestBody RechargeBean form) {
		LogUtil.startLog(RechargeController.class.toString(), RechargeDefine.RECHARGE_FIX);
		JSONObject ret = new JSONObject();
		Integer userId = form.getUserId();
		String nid = form.getNid();
		String status = form.getStatus();// 0 失败； 1成功
		if (Validator.isNull(userId) || Validator.isNull(nid) || Validator.isNull(status)) {
			ret.put("status", "error");
			ret.put("result", "确认发生错误,请重新操作!");
			LogUtil.errorLog(RechargeController.class.toString(), RechargeDefine.RECHARGE_FIX, new Exception("参数不正确[userId=" + userId + "]"));
			return ret.toString();
		}
		// 取出账户信息
		Account account = this.rechargeService.getAccountByUserId(userId);
		if (Validator.isNull(account)) {
			ret.put("status", "error");
			ret.put("result", "确认发生错误,请重新操作!");
			LogUtil.errorLog(RechargeController.class.toString(), RechargeDefine.RECHARGE_FIX, new Exception("[userId=" + userId + "]下账户异常！"));
			return ret.toString();
		}
		// 获取充值信息
		AccountRecharge accountRecharge = this.rechargeService.queryRechargeByNid(nid);
		if (Validator.isNull(accountRecharge)) {
			ret.put("status", "error");
			ret.put("result", "确认发生错误,请重新操作!");
			LogUtil.errorLog(RechargeController.class.toString(), RechargeDefine.RECHARGE_FIX, new Exception("[nid=" + nid + "]不存在！"));
			return ret.toString();
		}
		// 确认充值 ; 0表示充值失败
		boolean isAccountUpdate = false;
		if ("1".equals(status)) {
			try {
				isAccountUpdate = this.rechargeService.updateAccountAfterRecharge(userId, nid);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// 充值失败,更新充值订单
			try {
				isAccountUpdate = this.rechargeService.updateAccountAfterRechargeFail(userId, nid);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 提现确认成功
		if (isAccountUpdate) {
			ret.put(RechargeDefine.JSON_STATUS_KEY, RechargeDefine.JSON_STATUS_OK);
			ret.put(RechargeDefine.JSON_RESULT_KEY, "充值确认操作成功!");
		} else {
			ret.put(RechargeDefine.JSON_STATUS_KEY, RechargeDefine.JSON_STATUS_NG);
			ret.put(RechargeDefine.JSON_RESULT_KEY, "充值确认发生错误,请重新操作!");
		}

		LogUtil.endLog(RechargeController.class.toString(), RechargeDefine.RECHARGE_FIX);
		return ret.toString();
	}

	/**
	 * 更新充值状态
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(RechargeDefine.MODIFY_ACTION)
	@RequiresPermissions(RechargeDefine.PERMISSIONS_MODIFY)
	public String modifyRechargeStatus(HttpServletRequest request, @RequestBody RechargeBean form) {
		LogUtil.startLog(RechargeController.class.toString(), RechargeDefine.MODIFY_ACTION);
		JSONObject ret = new JSONObject();
		Integer userId = form.getUserId();
		String nid = form.getNid();
		if (Validator.isNull(userId) || Validator.isNull(nid)) {
			ret.put("status", "error");
			ret.put("result", "确认发生错误,请重新操作!");
			LogUtil.errorLog(RechargeController.class.toString(), RechargeDefine.MODIFY_ACTION, new Exception("参数不正确[userId=" + userId + "]"));
			return ret.toString();
		}
		// 更新充值状态
		boolean isAccountUpdate = false;
		try {
			isAccountUpdate = this.rechargeService.updateRechargeStatus(userId, nid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 提现确认成功
		if (isAccountUpdate) {
			ret.put(RechargeDefine.JSON_STATUS_KEY, RechargeDefine.JSON_STATUS_OK);
			ret.put(RechargeDefine.JSON_RESULT_KEY, "充值状态修改操作成功!");
		} else {
			ret.put(RechargeDefine.JSON_STATUS_KEY, RechargeDefine.JSON_STATUS_NG);
			ret.put(RechargeDefine.JSON_RESULT_KEY, "充值状态修改发生错误,请重新操作!");
		}

		LogUtil.endLog(RechargeController.class.toString(), RechargeDefine.MODIFY_ACTION);
		return ret.toString();
	}

	/**
	 * 数据导出
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(RechargeDefine.ENHANCE_EXPORT_ACTION)
	@RequiresPermissions(value = {RechargeDefine.PERMISSIONS_EXPORT, RechargeDefine.ENHANCE_PERMISSIONS_EXPORT}, logical = Logical.AND)
	public void enhanceExportAction(HttpServletRequest request, HttpServletResponse response, RechargeBean form) throws Exception {
		LogUtil.startLog(RechargeController.class.toString(), RechargeDefine.ENHANCE_EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "充值管理详情数据";
		// 设置默认查询时间
		if (StringUtils.isEmpty(form.getStartDate())) {
			form.setStartDate(GetDate.getDate("yyyy-MM-dd"));
		}
		if (StringUtils.isEmpty(form.getEndDate())) {
			form.setEndDate(GetDate.getDate("yyyy-MM-dd"));
		}
		RechargeCustomize rechargeCustomize = new RechargeCustomize();
		BeanUtils.copyProperties(form, rechargeCustomize);
		List<RechargeCustomize> rechargeCustomizes = this.rechargeService.queryRechargeList(rechargeCustomize);
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
        
		// String[] titles = new String[] { "用户名", "订单号", "充值渠道", "充值银行",
		// "银行卡号", "充值金额", "手续费", "垫付手续费" , "到账金额", "充值状态", "充值平台", "充值时间" };
		String[] titles = new String[] { "序号", "订单号", "用户名", "电子账号", "手机号", "流水号", "资金托管平台", "用户角色", "用户属性（当前）", "用户所属一级分部（当前）", "用户所属二级分部（当前）", "用户所属团队（当前）", "推荐人用户名（当前）", "推荐人姓名（当前）",
				"推荐人所属一级分部（当前）", "推荐人所属二级分部（当前）", "推荐人所属团队（当前）", "充值渠道", "充值类型", "充值银行", "银行卡号", "充值金额", "手续费", "垫付手续费", "到账金额", "充值状态", "充值平台", "充值时间" ,"发送日期" ,"发送时间" ,"系统跟踪号" };
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();

		// 生成一个表格
		HSSFSheet sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, sheetName + "_第1页");

		if (rechargeCustomizes != null && rechargeCustomizes.size() > 0) {

			int sheetCount = 1;
			int rowNum = 0;

			for (int i = 0; i < rechargeCustomizes.size(); i++) {
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
					RechargeCustomize record = rechargeCustomizes.get(i);
					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					} else if (celLength == 1) {
						cell.setCellValue(record.getNid());
					} else if (celLength == 2) {
						cell.setCellValue(record.getUsername());
					} else if (celLength == 3) {
						cell.setCellValue(record.getAccountId());
					}
					// 手机号
					else if (celLength == 4) {
						cell.setCellValue(record.getMobile());
					}
					// 流水号
					else if (celLength == 5) {
						cell.setCellValue(record.getSeqNo());
					}
					// 资金托管平台
					else if (celLength == 6) {
						cell.setCellValue(record.getIsBank());
					}
					// 用户角色
					else if (celLength == 7) {
						cell.setCellValue(record.getRoleId());
					}
					// 用户属性（当前）
					else if (celLength == 8) {
						if ("0".equals(record.getUserAttribute())) {
							cell.setCellValue("无主单");
						} else if ("1".equals(record.getUserAttribute())) {
							cell.setCellValue("有主单");
						} else if ("2".equals(record.getUserAttribute())) {
							cell.setCellValue("线下员工");
						} else if ("3".equals(record.getUserAttribute())) {
							cell.setCellValue("线上员工");
						}
					}
					// 用户所属一级分部（当前）
					else if (celLength == 9) {
						cell.setCellValue(record.getUserRegionName());
					}
					// 用户所属二级分部（当前）
					else if (celLength == 10) {
						cell.setCellValue(record.getUserBranchName());
					}
					// 用户所属团队（当前）
					else if (celLength == 11) {
						cell.setCellValue(record.getUserDepartmentName());
					}
					// 推荐人用户名（当前）
					else if (celLength == 12) {
						cell.setCellValue(record.getReferrerName());
					}
					// 推荐人姓名（当前）
					else if (celLength == 13) {
						cell.setCellValue(record.getReferrerTrueName());
					}
					// 推荐人所属一级分部（当前）
					else if (celLength == 14) {
						cell.setCellValue(record.getReferrerRegionName());
					}
					// 推荐人所属二级分部（当前）
					else if (celLength == 15) {
						cell.setCellValue(record.getReferrerBranchName());
					}
					// 推荐人所属团队（当前）
					else if (celLength == 16) {
						cell.setCellValue(record.getReferrerDepartmentName());
					} else if (celLength == 17) {
						cell.setCellValue(record.getType());// 充值渠道
					} else if (celLength == 18) {
						if (record.getGateType() != null && "B2C".equals(record.getGateType())) {
							cell.setCellValue("个人网银充值"); // 充值类型
						} else if (record.getGateType() != null && "B2B".equals(record.getGateType())) {
							cell.setCellValue("企业网银充值"); // 充值类型
						} else if (record.getGateType() != null && "QP".equals(record.getGateType())) {
							cell.setCellValue("快捷充值"); // 充值类型
						} else {
							cell.setCellValue(record.getGateType()); // 充值类型
						}
					} else if (celLength == 19) {
						cell.setCellValue(record.getBankName());
					} else if (celLength == 20) {
						cell.setCellValue(record.getCardid());
					} else if (celLength == 21) {
						cell.setCellValue(record.getMoney() + "");
					} else if (celLength == 22) {
						cell.setCellValue(record.getFee() != null ? (record.getFee() + "") : (0 + ""));
					} else if (celLength == 23) {
						cell.setCellValue(record.getDianfuFee() != null ? (record.getDianfuFee() + "") : (0 + ""));
					} else if (celLength == 24) {
						cell.setCellValue(record.getBalance() + "");
					} else if (celLength == 25) {
						cell.setCellValue(record.getStatus());
					} else if (celLength == 26) {
						cell.setCellValue(record.getClient());
					} else if (celLength == 27) {
						cell.setCellValue(record.getCreateTime());
					} else if (celLength == 28) {
						cell.setCellValue(record.getTxDate());
					} else if (celLength == 29) {
						cell.setCellValue(record.getTxTime());
					} else if (celLength == 30) {
						cell.setCellValue(record.getBankSeqNo());
					}
					// 以下都是空
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(request, response, workbook, titles, fileName);
		LogUtil.endLog(RechargeController.class.toString(), RechargeDefine.ENHANCE_EXPORT_ACTION);

	}

	@RequestMapping(RechargeDefine.EXPORT_ACTION)
	@RequiresPermissions(RechargeDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, RechargeBean form) throws Exception {
		LogUtil.startLog(RechargeController.class.toString(), RechargeDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "充值管理详情数据";
		// 设置默认查询时间
		if (StringUtils.isEmpty(form.getStartDate())) {
			form.setStartDate(GetDate.getDate("yyyy-MM-dd"));
		}
		if (StringUtils.isEmpty(form.getEndDate())) {
			form.setEndDate(GetDate.getDate("yyyy-MM-dd"));
		}
		RechargeCustomize rechargeCustomize = new RechargeCustomize();
		BeanUtils.copyProperties(form, rechargeCustomize);
		List<RechargeCustomize> rechargeCustomizes = this.rechargeService.queryRechargeList(rechargeCustomize);
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		// String[] titles = new String[] { "用户名", "订单号", "充值渠道", "充值银行",
		// "银行卡号", "充值金额", "手续费", "垫付手续费" , "到账金额", "充值状态", "充值平台", "充值时间" };
		String[] titles = new String[] { "序号", "订单号", "用户名", "电子账号", "手机号", "流水号", "资金托管平台", "用户角色", "用户属性（当前）",  "推荐人用户名（当前）", "推荐人姓名（当前）",
				 "充值渠道", "充值类型", "充值银行", "银行卡号", "充值金额", "手续费", "垫付手续费", "到账金额", "充值状态", "充值平台", "充值时间" ,"发送日期" ,"发送时间" ,"系统跟踪号" };
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();

		// 生成一个表格
		HSSFSheet sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, sheetName + "_第1页");

		if (rechargeCustomizes != null && rechargeCustomizes.size() > 0) {

			int sheetCount = 1;
			int rowNum = 0;

			for (int i = 0; i < rechargeCustomizes.size(); i++) {
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
					RechargeCustomize record = rechargeCustomizes.get(i);
					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					} else if (celLength == 1) {
						cell.setCellValue(record.getNid());
					} else if (celLength == 2) {
						cell.setCellValue(record.getUsername());
					} else if (celLength == 3) {
						cell.setCellValue(record.getAccountId());
					}
					// 手机号
					else if (celLength == 4) {
						cell.setCellValue(record.getMobile());
					}
					// 流水号
					else if (celLength == 5) {
						cell.setCellValue(record.getSeqNo());
					}
					// 资金托管平台
					else if (celLength == 6) {
						cell.setCellValue(record.getIsBank());
					}
					// 用户角色
					else if (celLength == 7) {
						cell.setCellValue(record.getRoleId());
					}
					// 用户属性（当前）
					else if (celLength == 8) {
						if ("0".equals(record.getUserAttribute())) {
							cell.setCellValue("无主单");
						} else if ("1".equals(record.getUserAttribute())) {
							cell.setCellValue("有主单");
						} else if ("2".equals(record.getUserAttribute())) {
							cell.setCellValue("线下员工");
						} else if ("3".equals(record.getUserAttribute())) {
							cell.setCellValue("线上员工");
						}
					}
					// 推荐人用户名（当前）
					else if (celLength == 9) {
						cell.setCellValue(record.getReferrerName());
					}
					// 推荐人姓名（当前）
					else if (celLength == 10) {
						cell.setCellValue(record.getReferrerTrueName());
					}
					else if (celLength == 11) {
						cell.setCellValue(record.getType());// 充值渠道
					} else if (celLength == 12) {
						if (record.getGateType() != null && "B2C".equals(record.getGateType())) {
							cell.setCellValue("个人网银充值"); // 充值类型
						} else if (record.getGateType() != null && "B2B".equals(record.getGateType())) {
							cell.setCellValue("企业网银充值"); // 充值类型
						} else if (record.getGateType() != null && "QP".equals(record.getGateType())) {
							cell.setCellValue("快捷充值"); // 充值类型
						} else {
							cell.setCellValue(record.getGateType()); // 充值类型
						}
					} else if (celLength == 13) {
						cell.setCellValue(record.getBankName());
					} else if (celLength == 14) {
						cell.setCellValue(record.getCardid());
					} else if (celLength == 15) {
						cell.setCellValue(record.getMoney() + "");
					} else if (celLength == 16) {
						cell.setCellValue(record.getFee() != null ? (record.getFee() + "") : (0 + ""));
					} else if (celLength == 17) {
						cell.setCellValue(record.getDianfuFee() != null ? (record.getDianfuFee() + "") : (0 + ""));
					} else if (celLength == 18) {
						cell.setCellValue(record.getBalance() + "");
					} else if (celLength == 19) {
						cell.setCellValue(record.getStatus());
					} else if (celLength == 20) {
						cell.setCellValue(record.getClient());
					} else if (celLength == 21) {
						cell.setCellValue(record.getCreateTime());
					} else if (celLength == 22) {
						cell.setCellValue(record.getTxDate());
					} else if (celLength == 23) {
						cell.setCellValue(record.getTxTime());
					} else if (celLength == 24) {
						cell.setCellValue(record.getBankSeqNo());
					}
					// 以下都是空
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(request, response, workbook, titles, fileName);
		LogUtil.endLog(RechargeController.class.toString(), RechargeDefine.EXPORT_ACTION);

	}

	/**
	 * 弹出 平台转账框
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(RechargeDefine.TO_HANDRECHARGE)
	@RequiresPermissions(RechargeDefine.PERMISSIONS_TOHANDRECHARGE)
	public ModelAndView toHandRechargePage(HttpServletRequest request, RechargeBean form) {
		LogUtil.startLog(RechargeController.class.toString(), RechargeDefine.TO_HANDRECHARGE);
		ModelAndView modeAndView = new ModelAndView(RechargeDefine.RECHARGE_HAND_PATH);

        // 查询商户子账户余额
        String merrpAccount = PropUtils.getSystem(BankCallConstant.BANK_MERRP_ACCOUNT);
        BigDecimal bankBalance = rechargeService.getBankBalance(Integer.parseInt(ShiroUtil.getLoginUserId()), merrpAccount);


		modeAndView.addObject("avlBal", bankBalance);

		LogUtil.endLog(RechargeController.class.toString(), RechargeDefine.TO_HANDRECHARGE);
		return modeAndView;
	}

	/**
	 * 校验用户转账的结果
	 *
	 * @param request
	 * @return 进入用户详情页面
	 */
	@ResponseBody
	@RequiresPermissions(RechargeDefine.PERMISSIONS_ADD)
	@RequestMapping(value = RechargeDefine.CHECK_TRANSFER_ACTION, method = RequestMethod.POST)
	public String checkTransfer(HttpServletRequest request) {

		LogUtil.startLog(RechargeController.class.toString(), TransferDefine.CHECK_TRANSFER_ACTION);
		String outUserName = request.getParameter("param");
		JSONObject ret = new JSONObject();
		if (StringUtils.isNotBlank(outUserName)) {
			this.rechargeService.checkTransfer(outUserName, ret);
		} else {
			ret.put(TransferDefine.JSON_VALID_INFO_KEY, "用户账号不能为空!");
		}
		LogUtil.endLog(RechargeController.class.toString(), TransferDefine.CHECK_TRANSFER_ACTION);
		return ret.toString();
	}

	/**
	 * 平台转账
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	// @ResponseBody
	@RequestMapping(value = RechargeDefine.HANDRECHARGE)
	// @RequiresPermissions(RechargeDefine.PERMISSIONS_ADD)
	public ModelAndView handRecharge(HttpServletRequest request, RechargeBean form) {
		LogUtil.startLog(RechargeController.class.toString(), RechargeDefine.HANDRECHARGE);

		ModelAndView modelAndView = new ModelAndView(RechargeDefine.RECHARGE_HAND_PATH);

		String password = form.getPassword();
		if (password != null && password.equals(PropUtils.getSystem(PropertiesConstants.HYJF_HANDRECHARGE_PASSWORD))) {

			// 获取客户userId
			// Integer userId = Integer.valueOf(ShiroUtil.getLoginUserId());-
			// Users cusUser =
			// rechargeService.queryUserInfoByUserName(form.getUsername());
			UserInfoCustomize userInfo = rechargeService.queryUserInfoByName(form.getUsername());
			Integer userId = userInfo == null ? 0 : userInfo.getUserId();
			if (userId == 0) {
				modelAndView.addObject("status", "error");
				modelAndView.addObject("result", "该用户不存在");
				LogUtil.errorLog(RechargeController.class.toString(), RechargeDefine.HANDRECHARGE, new Exception("参数不正确[userName=" + form.getUsername() + "]"));
				return modelAndView;
			}
			form.setUserId(userId);

			BankOpenAccount bankAccount = rechargeService.getBankOpenAccount(userId);
			// 用户未开户时,返回错误信息
			if (bankAccount == null || Validator.isNull(bankAccount.getAccount())) {
				modelAndView.addObject("status", "error");
				modelAndView.addObject("result", "该用户未开户");
				LogUtil.errorLog(RechargeController.class.toString(), RechargeDefine.HANDRECHARGE, new Exception("参数不正确[userId=" + userId + "]"));
				return modelAndView;
			}

	        // 查询商户子账户余额
	        String merrpAccount = PropUtils.getSystem(BankCallConstant.BANK_MERRP_ACCOUNT);
	        BigDecimal bankBalance = rechargeService.getBankBalance(Integer.parseInt(ShiroUtil.getLoginUserId()), merrpAccount);

	        if (bankBalance.compareTo(form.getMoney()) <= 0) {
                LogUtil.errorLog(this.getClass().getName(), "handRecharge", "红包账户余额不足,请先充值或向该子账户转账!", null);
                modelAndView.addObject("status", "error");
                modelAndView.addObject("result", "红包账户余额不足,请先充值或向该子账户转账!");
                return modelAndView;
            }


            // IP地址
            String ip = CustomUtil.getIpAddr(request);
            String orderId = GetOrderIdUtils.getOrderId2(Integer.valueOf(userId));
            
            BankCallBean bean = new BankCallBean();
            bean.setVersion(BankCallConstant.VERSION_10);// 版本号
            bean.setTxCode(BankCallMethodConstant.TXCODE_VOUCHER_PAY);// 交易代码
            bean.setTxDate(GetOrderIdUtils.getTxDate()); // 交易日期
            bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
            bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
            bean.setChannel(BankCallConstant.CHANNEL_PC); // 交易渠道
            bean.setAccountId(merrpAccount);// 电子账号
            bean.setTxAmount(form.getMoney().toString());
            bean.setForAccountId(bankAccount.getAccount());
            bean.setDesLineFlag("1");
            bean.setLogOrderId(orderId);// 订单号
            bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
            bean.setLogUserId(String.valueOf(userId));
            bean.setLogClient(0);// 平台
            bean.setLogIp(ip);

            BankCallBean resultBean;
            try {
                resultBean = BankCallUtils.callApiBg(bean);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.errorLog(RechargeController.class.toString(), RechargeDefine.HANDRECHARGE, e);
                modelAndView.addObject("status", "error");
                modelAndView.addObject("result", "平台转账发生错误,请重新操作!");
                return modelAndView;
            }
            
            if (resultBean == null || !BankCallConstant.RESPCODE_SUCCESS.equals(resultBean.getRetCode())) {
                LogUtil.errorLog(RechargeController.class.toString(), RechargeDefine.HANDRECHARGE, new Exception("调用汇付接口发生错误"));
                modelAndView.addObject("status", "error");
                modelAndView.addObject("result", "平台转账发生错误,请重新操作!");
                return modelAndView;
            }

			int cnt = 0;
			try {
				// 平台转账处理
				cnt = this.rechargeService.updateHandRechargeRecord(form, resultBean, userInfo, bankAccount.getAccount());
			} catch (Exception e) {
				LogUtil.errorLog(RechargeController.class.toString(), RechargeDefine.HANDRECHARGE, e);
			}

			// 返现成功
			if (cnt > 0) {
				modelAndView.addObject("status", "success");
				modelAndView.addObject("success", "success");
				modelAndView.addObject("result", "平台转账操作成功!");
			} else {
				modelAndView.addObject("status", "error");
				modelAndView.addObject("result", "平台转账发生错误,请重新操作!");
			}
		} else {
			modelAndView.addObject("status", "error");
			modelAndView.addObject("result", "密码错误,请重新操作!");
		}

		LogUtil.endLog(RechargeController.class.toString(), RechargeDefine.HANDRECHARGE);
		return modelAndView;
	}

	/**
	 * 分页 转账管理 平台转账
	 *
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	private void createPagePT(HttpServletRequest request, ModelAndView modeAndView, RechargeBean form) {
		RechargeCustomize rechargeCustomize = new RechargeCustomize();
		BeanUtils.copyProperties(form, rechargeCustomize);
		rechargeCustomize.setTypeSearch("ADMIN");
		Integer count = this.rechargeService.queryRechargeCount(rechargeCustomize);
		if (count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			form.setPaginator(paginator);
			rechargeCustomize.setLimitStart(paginator.getOffset());
			rechargeCustomize.setLimitEnd(paginator.getLimit());
			List<RechargeCustomize> rechargeCustomizes = this.rechargeService.queryRechargeList(rechargeCustomize);
			form.setRecordList(rechargeCustomizes);
			modeAndView.addObject(RechargeDefine.RECHARGE_FORM, form);
		} else {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			rechargeCustomize.setLimitStart(paginator.getOffset());
			rechargeCustomize.setLimitEnd(paginator.getLimit());
			form.setPaginator(paginator);
			modeAndView.addObject(RechargeDefine.RECHARGE_FORM, form);
		}

	}

	/**
	 * 账户管理 列表
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(RechargeDefine.RECHARGEPT_LIST)
	@RequiresPermissions(RechargeDefine.PERMISSIONS_VIEW)
	public ModelAndView initPT(HttpServletRequest request, RechargeBean form) {
		ModelAndView modeAndView = new ModelAndView(RechargeDefine.LISTPT_PATH);
		// 创建分页
		this.createPagePT(request, modeAndView, form);
		return modeAndView;
	}

	/**
	 * 账户管理 查询条件
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(RechargeDefine.RECHARGEPT_LIST_WITHQ)
	@RequiresPermissions(RechargeDefine.PERMISSIONS_VIEW)
	public ModelAndView initWithPT(HttpServletRequest request, RechargeBean form) {
		LogUtil.startLog(RechargeController.class.toString(), RechargeDefine.RECHARGEPT_LIST_WITHQ);
		ModelAndView modeAndView = new ModelAndView(RechargeDefine.LISTPT_PATH);

		// 创建分页
		this.createPagePT(request, modeAndView, form);

		LogUtil.endLog(RechargeController.class.toString(), RechargeDefine.RECHARGEPT_LIST_WITHQ);
		return modeAndView;
	}

	/**
	 * 数据导出平台转账
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(RechargeDefine.EXPORTPT_ACTION)
	@RequiresPermissions(RechargeDefine.PERMISSIONS_EXPORT)
	public void exportPTAction(HttpServletRequest request, HttpServletResponse response, RechargeBean form) throws Exception {
		LogUtil.startLog(RechargeController.class.toString(), RechargeDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "平台转账详情数据";
		// 设置默认查询时间
		if (StringUtils.isEmpty(form.getStartDate())) {
			form.setStartDate(GetDate.getDate("yyyy-MM-dd"));
		}
		if (StringUtils.isEmpty(form.getEndDate())) {
			form.setEndDate(GetDate.getDate("yyyy-MM-dd"));
		}
		RechargeCustomize rechargeCustomize = new RechargeCustomize();
		BeanUtils.copyProperties(form, rechargeCustomize);
		rechargeCustomize.setTypeSearch("ADMIN");
		List<RechargeCustomize> rechargeCustomizes = this.rechargeService.queryRechargeList(rechargeCustomize);
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		// String[] titles = new String[] { "用户名", "订单号", "充值渠道", "充值银行",
		// "银行卡号", "充值金额", "手续费", "垫付手续费" , "到账金额", "充值状态", "充值平台", "充值时间" };
		String[] titles = new String[] { "序号", "订单号", "用户名", "手机号", "转账金额", "可用余额", "转账状态", "转账时间", "备注" ,"发送日期" ,"发送时间" ,"系统跟踪号" };
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();

		// 生成一个表格
		HSSFSheet sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, sheetName + "_第1页");

		if (rechargeCustomizes != null && rechargeCustomizes.size() > 0) {

			int sheetCount = 1;
			int rowNum = 0;

			for (int i = 0; i < rechargeCustomizes.size(); i++) {
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
					RechargeCustomize record = rechargeCustomizes.get(i);
					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					} else if (celLength == 1) {
						cell.setCellValue(record.getNid());
					} else if (celLength == 2) {
						cell.setCellValue(record.getUsername());
					}
					// 手机号
					else if (celLength == 3) {
						cell.setCellValue(record.getMobile());
					} // 转账金额
					else if (celLength == 4) {
						cell.setCellValue(record.getMoney() + "");
					}
					// 可用余额
					else if (celLength == 5) {
						cell.setCellValue(record.getBalance() + "");
					}
					// 转账状态
					else if (celLength == 6) {
						cell.setCellValue(record.getStatusName());
					}
					// 转账时间
					else if (celLength == 7) {
						cell.setCellValue(record.getCreateTime());
					}
					// 备注
					else if (celLength == 8) {
						cell.setCellValue(record.getRemark());
					}
					else if (celLength == 9) {
						cell.setCellValue(record.getTxDate());
					}
					else if (celLength == 10) {
						cell.setCellValue(record.getTxTime());
					}
					else if (celLength == 11) {
						cell.setCellValue(record.getBankSeqNo());
					}
					// 以下都是空
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(request, response, workbook, titles, fileName);
		LogUtil.endLog(RechargeController.class.toString(), RechargeDefine.EXPORTPT_ACTION);

	}

}
