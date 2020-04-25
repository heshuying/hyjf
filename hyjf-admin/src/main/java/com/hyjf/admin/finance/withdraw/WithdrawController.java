package com.hyjf.admin.finance.withdraw;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.Accountwithdraw;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.WithdrawCustomize;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @package com.hyjf.admin.finance.Withdraw
 * @author GOGTZ-T
 * @date 2015/11/29 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = WithdrawDefine.REQUEST_MAPPING)
public class WithdrawController extends BaseController {
	/**
	 * 类名
	 */
	private static final String THIS_CLASS = WithdrawController.class.getName();

	@Autowired
	private WithdrawService withdrawService;

	/**
	 * 返现管理画面初始化
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(WithdrawDefine.INIT)
	@RequiresPermissions(WithdrawDefine.PERMISSIONS_WITHDRAW_VIEW)
	public ModelAndView init(HttpServletRequest request, WithdrawBean form) {
		LogUtil.startLog(THIS_CLASS, WithdrawDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(WithdrawDefine.WITHDRAW_LIST_PATH);

		modelAndView = new ModelAndView(WithdrawDefine.WITHDRAW_LIST_PATH);
		// 创建分页
		this.createWithdrawPage(request, modelAndView, form);

		LogUtil.endLog(THIS_CLASS, WithdrawDefine.INIT);
		return modelAndView;
	}

	// ***********************************************提现画面Start****************************************************

	/**
	 * 提现管理画面查询
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(WithdrawDefine.SEARCH_WITHDRAW_ACTION)
	@RequiresPermissions(WithdrawDefine.PERMISSIONS_WITHDRAW_VIEW)
	public ModelAndView searchWithdraw(HttpServletRequest request, WithdrawBean form) {
		LogUtil.startLog(THIS_CLASS, WithdrawDefine.SEARCH_WITHDRAW_ACTION);
		ModelAndView modelAndView = new ModelAndView(WithdrawDefine.WITHDRAW_LIST_PATH);

		// 创建分页
		this.createWithdrawPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, WithdrawDefine.SEARCH_WITHDRAW_ACTION);
		return modelAndView;
	}

	/**
	 * 创建提现管理分页机能
	 *
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createWithdrawPage(HttpServletRequest request, ModelAndView modelAndView, WithdrawBean form) {
		// 用户属性
		List<ParamName> userPropertys = this.withdrawService.getParamNameList("USER_PROPERTY");
		modelAndView.addObject("userPropertys", userPropertys);
		int cnt = this.withdrawService.getWithdrawRecordCount(form);
		if (cnt > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), cnt);
			form.setLimitStart(paginator.getOffset());
			form.setLimitEnd(paginator.getLimit());
			List<WithdrawCustomize> recordList = this.withdrawService.getWithdrawRecordList(form);
			form.setPaginator(paginator);
			form.setRecordList(recordList);
		}
		// 提现平台列表
		modelAndView.addObject(WithdrawDefine.CLIENT_LIST, withdrawService.getParamNameList(CustomConstants.CLIENT));
		// 提现状态列表
		modelAndView.addObject(WithdrawDefine.STATUS_LIST, withdrawService.getParamNameList(CustomConstants.WITHDRAW_STATUS));

		modelAndView.addObject(WithdrawDefine.WITHDRAW_FORM, form);
	}

	/**
	 * 提现确认
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(WithdrawDefine.CONFIRM_WITHDRAW_ACTION)
	@RequiresPermissions(WithdrawDefine.PERMISSIONS_WITHDRAW_CONFIRM)
	public String confirmWithdrawAction(HttpServletRequest request, @RequestBody WithdrawBean form) {
		LogUtil.startLog(THIS_CLASS, WithdrawDefine.CONFIRM_WITHDRAW_ACTION);
		JSONObject ret = new JSONObject();
		Integer userId = form.getUserId();
		String nid = form.getNid();
		Integer status = form.getStatus();// 0 失败； 1成功

		if (Validator.isNull(userId) || Validator.isNull(nid) || Validator.isNull(status)) {
			ret.put("status", "error");
			ret.put("result", "确认发生错误,请重新操作!");
			LogUtil.errorLog(THIS_CLASS, WithdrawDefine.CONFIRM_WITHDRAW_ACTION, new Exception("参数不正确[userId=" + userId + "]"));
			return ret.toString();
		}
		// 取出账户信息
		Account account = this.withdrawService.getAccountByUserId(userId);
		if (Validator.isNull(account)) {
			ret.put("status", "error");
			ret.put("result", "确认发生错误,请重新操作!");
			LogUtil.errorLog(THIS_CLASS, WithdrawDefine.CONFIRM_WITHDRAW_ACTION, new Exception("[userId=" + userId + "]下账户异常！"));
			return ret.toString();
		}
		// 获取充值信息
		Accountwithdraw accountwithdraw = this.withdrawService.queryAccountwithdrawByNid(nid, userId);
		if (Validator.isNull(accountwithdraw)) {
			ret.put("status", "error");
			ret.put("result", "确认发生错误,请重新操作!");
			LogUtil.errorLog(THIS_CLASS, WithdrawDefine.CONFIRM_WITHDRAW_ACTION, new Exception("[nid=" + nid + "]不存在！"));
			return ret.toString();
		}
		// 设置IP地址
		String ip = CustomUtil.getIpAddr(request);
		// 登陆用户名
		String loginUser = ShiroUtil.getLoginUsername();
		Map<String, String> param = new HashMap<String, String>();
		param.put("userName", loginUser);
		param.put("ip", ip);
		// 确认充值 ; 0表示充值失败
		boolean isAccountUpdate = false;
		if (status == 1) {
			try {
				isAccountUpdate = this.withdrawService.updateAccountAfterWithdraw(userId, nid, param);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// 充值失败,更新充值订单
			try {
				isAccountUpdate = this.withdrawService.updateAccountAfterWithdrawFail(userId, nid);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 提现确认成功
		if (isAccountUpdate) {
			ret.put(WithdrawDefine.JSON_STATUS_KEY, WithdrawDefine.JSON_STATUS_OK);
			ret.put(WithdrawDefine.JSON_RESULT_KEY, "提现确认操作成功!");
		} else {
			ret.put(WithdrawDefine.JSON_STATUS_KEY, WithdrawDefine.JSON_STATUS_NG);
			ret.put(WithdrawDefine.JSON_RESULT_KEY, "提现确认发生错误,请重新操作!");
		}

		LogUtil.endLog(THIS_CLASS, WithdrawDefine.CONFIRM_WITHDRAW_ACTION);
		return ret.toString();
	}

	/**
	 * 根据业务需求导出相应的表格 此处暂时为可用情况 缺陷： 1.无法指定相应的列的顺序， 2.无法配置，excel文件名，excel sheet名称
	 * 3.目前只能导出一个sheet 4.列的宽度的自适应，中文存在一定问题
	 * 5.根据导出的业务需求最好可以在导出的时候输入起止页码，因为在大数据量的情况下容易造成卡顿
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(WithdrawDefine.ENHANCE_EXPORT_ACTION)
	@RequiresPermissions(value = {WithdrawDefine.PERMISSIONS_WITHDRAW_EXPORT, WithdrawDefine.ENHANCE_PERMISSIONS_EXPORT}, logical = Logical.AND)
	public void enhanceExportWithdrawExcel(HttpServletRequest request, HttpServletResponse response, WithdrawBean form) throws Exception {
		LogUtil.startLog(THIS_CLASS, WithdrawDefine.ENHANCE_EXPORT_ACTION);

		// 表格sheet名称
		String sheetName = "提现列表";

		// 取得数据
		form.setLimitStart(-1);
		form.setLimitEnd(-1);
		// 设置默认查询时间
		if (StringUtils.isEmpty(form.getAddtimeStartSrch())) {
			form.setAddtimeStartSrch(GetDate.getDate("yyyy-MM-dd"));
		}
		if (StringUtils.isEmpty(form.getAddtimeEndSrch())) {
			form.setAddtimeEndSrch(GetDate.getDate("yyyy-MM-dd"));
		}
		List<WithdrawCustomize> recordList = this.withdrawService.getWithdrawRecordList(form);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "序号", "用户名", "电子帐号", "资金托管平台", "流水号", "手机号", "用户角色", "用户属性（当前）", "用户所属一级分部（当前）", "用户所属二级分部（当前）", "用户所属团队（当前）", "推荐人用户名（当前）", "推荐人姓名（当前）", "推荐人所属一级分部（当前）",
				"推荐人所属二级分部（当前）", "推荐人所属团队（当前）", "订单号", "提现金额", "手续费", "到账金额", "实际出账金额", "提现银行", "提现方式", "提现银行卡号", "提交时间", "提现平台", "处理状态" ,"发送日期" ,"发送时间" ,"系统跟踪号" };
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
					WithdrawCustomize bean = recordList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 用户名
					else if (celLength == 1) {
						cell.setCellValue(bean.getUsername());
					}
					// 电子帐号
					else if (celLength == 2) {
						cell.setCellValue(bean.getAccountId());
					}
					// 资金托管平台
					else if (celLength == 3) {
						if (bean.getBankFlag() != null) {
							if (bean.getBankFlag() == 1) {
								cell.setCellValue("江西银行");
							} else {
								cell.setCellValue("汇付天下");
							}
						} else {
							cell.setCellValue("汇付天下");
						}

					}
					// 流水号
					else if (celLength == 4) {
						cell.setCellValue(bean.getSeqNo()==null ? "" : String.valueOf(bean.getSeqNo()));
					}

					// 手机号
					else if (celLength == 5) {
						cell.setCellValue(bean.getMobile());
					}
					// 用户角色
					else if (celLength == 6) {
						cell.setCellValue(bean.getRoleid());
					}

					// 用户属性（当前）
					else if (celLength == 7) {
						cell.setCellValue(bean.getUserAttribute());
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
					// 用户所属一级分部（当前）
					else if (celLength == 8) {
						cell.setCellValue(bean.getUserRegionName());
					}
					// 用户所属二级分部（当前）
					else if (celLength == 9) {
						cell.setCellValue(bean.getUserBranchName());
					}
					// 用户所属团队（当前）
					else if (celLength == 10) {
						cell.setCellValue(bean.getUserDepartmentName());
					}
					// 推荐人用户名（当前）
					else if (celLength == 11) {
						cell.setCellValue(bean.getReferrerName());
					}
					// 推荐人姓名（当前）
					else if (celLength == 12) {
						cell.setCellValue(bean.getReferrerTrueName());
					}
					// 推荐人所属一级分部（当前）
					else if (celLength == 13) {
						cell.setCellValue(bean.getReferrerRegionName());
					}
					// 推荐人所属二级分部（当前）
					else if (celLength == 14) {
						cell.setCellValue(bean.getReferrerBranchName());
					}
					// 推荐人所属团队（当前）
					else if (celLength == 15) {
						cell.setCellValue(bean.getReferrerDepartmentName());
					}
					// 订单号
					else if (celLength == 16) {
						cell.setCellValue(bean.getOrdid());
					}
					// 提现金额
					else if (celLength == 17) {
						cell.setCellValue(bean.getTotal().toString());
					}
					// 手续费
					else if (celLength == 18) {
						cell.setCellValue(bean.getFee());
					}
					// 到账金额
					else if (celLength == 19) {
						cell.setCellValue(bean.getCredited().toString());
					}
					// 实际出账金额
					else if (celLength == 20) {
						cell.setCellValue(bean.getTotal().toString());
					}
					// 提现银行
					else if (celLength == 21) {
						cell.setCellValue(bean.getBank());
					}
					// 提现方式
					else if (celLength == 22) {
					    if ("0".equals(bean.getWithdrawType()+"")) {
					        cell.setCellValue("主动提现");
                        } else if ("1".equals(bean.getWithdrawType()+"")) {
                            cell.setCellValue("代提现");
                        }
                    }
					// 提现银行卡号
					else if (celLength == 23) {
						cell.setCellValue(bean.getAccount());
					}
					// 提交时间
					else if (celLength == 24) {
						cell.setCellValue(bean.getAddtimeStr());
					}
					// 提现平台
					else if (celLength == 25) {
						cell.setCellValue(bean.getClientStr());
					}
					// 处理状态
					else if (celLength == 26) {
						cell.setCellValue(bean.getStatusStr());
					}
					else if (celLength == 27) {
						cell.setCellValue(bean.getTxDateS());
					} 
					else if (celLength == 28) {
						cell.setCellValue(bean.getTxTimeS());
					} 
					else if (celLength == 29) {
						cell.setCellValue(bean.getSeqNo()==null ? "" : String.valueOf(bean.getSeqNo()));
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(request, response, workbook, titles, fileName);

		LogUtil.endLog(THIS_CLASS, WithdrawDefine.ENHANCE_EXPORT_ACTION);
	}

	@RequestMapping(WithdrawDefine.EXPORT_WITHDRAW_ACTION)
	@RequiresPermissions(WithdrawDefine.PERMISSIONS_WITHDRAW_EXPORT)
	public void exportWithdrawExcel(HttpServletRequest request, HttpServletResponse response, WithdrawBean form) throws Exception {
		LogUtil.startLog(THIS_CLASS, WithdrawDefine.EXPORT_WITHDRAW_ACTION);

		// 表格sheet名称
		String sheetName = "提现列表";

		// 取得数据
		form.setLimitStart(-1);
		form.setLimitEnd(-1);
		// 设置默认查询时间
		if (StringUtils.isEmpty(form.getAddtimeStartSrch())) {
			form.setAddtimeStartSrch(GetDate.getDate("yyyy-MM-dd"));
		}
		if (StringUtils.isEmpty(form.getAddtimeEndSrch())) {
			form.setAddtimeEndSrch(GetDate.getDate("yyyy-MM-dd"));
		}
		List<WithdrawCustomize> recordList = this.withdrawService.getWithdrawRecordList(form);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "序号", "用户名", "电子帐号", "资金托管平台", "流水号", "手机号", "用户角色", "用户属性（当前）",  "推荐人用户名（当前）", "推荐人姓名（当前）", "订单号", "提现金额", "手续费", "到账金额", "实际出账金额", "提现银行", "提现方式", "提现银行卡号", "提交时间", "提现平台", "处理状态" ,"发送日期" ,"发送时间" ,"系统跟踪号" };
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
					WithdrawCustomize bean = recordList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 用户名
					else if (celLength == 1) {
						cell.setCellValue(bean.getUsername());
					}
					// 电子帐号
					else if (celLength == 2) {
						cell.setCellValue(bean.getAccountId());
					}
					// 资金托管平台
					else if (celLength == 3) {
						if (bean.getBankFlag() != null) {
							if (bean.getBankFlag() == 1) {
								cell.setCellValue("江西银行");
							} else {
								cell.setCellValue("汇付天下");
							}
						} else {
							cell.setCellValue("汇付天下");
						}

					}
					// 流水号
					else if (celLength == 4) {
						cell.setCellValue(bean.getSeqNo()==null ? "" : String.valueOf(bean.getSeqNo()));
					}

					// 手机号
					else if (celLength == 5) {
						cell.setCellValue(bean.getMobile());
					}
					// 用户角色
					else if (celLength == 6) {
						cell.setCellValue(bean.getRoleid());
					}

					// 用户属性（当前）
					else if (celLength == 7) {
						cell.setCellValue(bean.getUserAttribute());
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
					// 推荐人用户名（当前）
					else if (celLength == 8) {
						cell.setCellValue(bean.getReferrerName());
					}
					// 推荐人姓名（当前）
					else if (celLength == 9) {
						cell.setCellValue(bean.getReferrerTrueName());
					}
					// 订单号
					else if (celLength == 10) {
						cell.setCellValue(bean.getOrdid());
					}
					// 提现金额
					else if (celLength == 11) {
						cell.setCellValue(bean.getTotal().toString());
					}
					// 手续费
					else if (celLength == 12) {
						cell.setCellValue(bean.getFee());
					}
					// 到账金额
					else if (celLength == 13) {
						cell.setCellValue(bean.getCredited().toString());
					}
					// 实际出账金额
					else if (celLength == 14) {
						cell.setCellValue(bean.getTotal().toString());
					}
					// 提现银行
					else if (celLength == 15) {
						cell.setCellValue(bean.getBank());
					}
					// 提现方式
					else if (celLength == 16) {
						if ("0".equals(bean.getWithdrawType()+"")) {
							cell.setCellValue("主动提现");
						} else if ("1".equals(bean.getWithdrawType()+"")) {
							cell.setCellValue("代提现");
						}
					}
					// 提现银行卡号
					else if (celLength == 17) {
						cell.setCellValue(bean.getAccount());
					}
					// 提交时间
					else if (celLength == 18) {
						cell.setCellValue(bean.getAddtimeStr());
					}
					// 提现平台
					else if (celLength == 19) {
						cell.setCellValue(bean.getClientStr());
					}
					// 处理状态
					else if (celLength == 20) {
						cell.setCellValue(bean.getStatusStr());
					}
					else if (celLength == 21) {
						cell.setCellValue(bean.getTxDateS());
					}
					else if (celLength == 22) {
						cell.setCellValue(bean.getTxTimeS());
					}
					else if (celLength == 23) {
						cell.setCellValue(bean.getSeqNo()==null ? "" : String.valueOf(bean.getSeqNo()));
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(request, response, workbook, titles, fileName);

		LogUtil.endLog(THIS_CLASS, WithdrawDefine.EXPORT_WITHDRAW_ACTION);
	}
	// ***********************************************提现画面End****************************************************

}
