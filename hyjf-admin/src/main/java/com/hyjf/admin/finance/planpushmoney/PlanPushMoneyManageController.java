package com.hyjf.admin.finance.planpushmoney;

import java.math.BigDecimal;
import java.text.DecimalFormat;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.http.HtmlUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.DebtAccedeCommission;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.admin.AdminPlanPushMoneyDetailCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;

@Controller
@RequestMapping(PlanPushMoneyManageDefine.REQUEST_MAPPING)
public class PlanPushMoneyManageController extends BaseController {

	/** 类名 */
	private static final String THIS_CLASS = PlanPushMoneyManageController.class.getName();

	@Autowired
	private PlanPushMoneyManageService planPushMoneyManageService;

	private static DecimalFormat DF_FOR_VIEW = new DecimalFormat("#,##0.00");

	/**
	 * 初期化
	 * 
	 * @Title init
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PlanPushMoneyManageDefine.INIT)
	@RequiresPermissions(PlanPushMoneyManageDefine.PERMISSIONS_PUSHMONEY_VIEW)
	public ModelAndView init(HttpServletRequest request, PlanPushMoneyManageBean form) {
		LogUtil.startLog(THIS_CLASS, PlanPushMoneyManageDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(PlanPushMoneyManageDefine.LIST_PATH);
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, PlanPushMoneyManageDefine.INIT);
		return modelAndView;
	}

	/**
	 * 检索
	 * 
	 * @Title search
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PlanPushMoneyManageDefine.SEARCH_ACTION)
	@RequiresPermissions(PlanPushMoneyManageDefine.PERMISSIONS_PUSHMONEY_VIEW)
	public ModelAndView search(HttpServletRequest request, PlanPushMoneyManageBean form) {
		LogUtil.startLog(THIS_CLASS, PlanPushMoneyManageDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(PlanPushMoneyManageDefine.LIST_PATH);
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, PlanPushMoneyManageDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 分页
	 * 
	 * @Title createPage
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, PlanPushMoneyManageBean form) {
		int count = this.planPushMoneyManageService.countLockPlanList(form);
		if (count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			form.setLimitStart(paginator.getOffset());
			form.setLimitEnd(paginator.getLimit());
			List<DebtPlan> recordList = this.planPushMoneyManageService.selectLockPlanList(form);
			form.setRecordList(recordList);
			form.setPaginator(paginator);
		}
		modelAndView.addObject(PlanPushMoneyManageDefine.FORM, form);
	}

	/**
	 * 计算提成
	 * 
	 * @Title calculatePushMoney
	 * @param request
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(PlanPushMoneyManageDefine.CALCULATE_PUSHMONEY)
	@RequiresPermissions(PlanPushMoneyManageDefine.PERMISSIONS_PUSHMONEY_CALCULATE)
	public String calculatePushMoney(HttpServletRequest request, @RequestBody PlanPushMoneyManageBean form) {
		LogUtil.startLog(THIS_CLASS, PlanPushMoneyManageDefine.CALCULATE_PUSHMONEY);
		JSONObject ret = new JSONObject();
		// 计划Nid
		String debtPlanNid = form.getDebtPlanNid();

		// 根据计划编号检索计划
		DebtPlan plan = this.planPushMoneyManageService.selectDebtPlanByDebtPlanNid(debtPlanNid);
		if (plan == null) {
			ret.put(PlanPushMoneyManageDefine.JSON_STATUS_KEY, PlanPushMoneyManageDefine.JSON_STATUS_NG);
			ret.put(PlanPushMoneyManageDefine.JSON_RESULT_KEY, "该计划不存在!");
			return ret.toString();
		}
		if (plan.getCommissionStatus() == 1) {
			ret.put(PlanPushMoneyManageDefine.JSON_STATUS_KEY, PlanPushMoneyManageDefine.JSON_STATUS_OK);
			ret.put(PlanPushMoneyManageDefine.JSON_RESULT_KEY, "该计划的提成已经计算完成!");
			return ret.toString();
		}

		int cnt = -1;

		try {
			// 发提成处理
			cnt = this.planPushMoneyManageService.insertAccedeCommissionRecord(form);
		} catch (Exception e) {
			LogUtil.errorLog(THIS_CLASS, PlanPushMoneyManageDefine.CALCULATE_PUSHMONEY, e);
		}

		if (cnt >= 0) {
			ret.put("status", "success");
			ret.put("result", "提成计算成功！");
		} else {
			ret.put("status", "error");
			ret.put("result", "提成计算失败,请重新操作!");
		}

		LogUtil.endLog(THIS_CLASS, PlanPushMoneyManageDefine.CALCULATE_PUSHMONEY);

		return ret.toString();
	}

	/**
	 * 提成列表导出
	 * 
	 * @Title exportPushMoneyExcel
	 * @param request
	 * @param response
	 * @param form
	 */
	@RequestMapping(PlanPushMoneyManageDefine.EXPORT_PUSHMONEY_ACTION)
	@RequiresPermissions(PlanPushMoneyManageDefine.PERMISSIONS_PUSHMONEY_EXPORT)
	public void exportPushMoneyExcel(HttpServletRequest request, HttpServletResponse response, PlanPushMoneyManageBean form) throws Exception {
		// 表格sheet名称
		String sheetName = "汇添金推广提成列表";

		//设置默认查询时间
		if(StringUtils.isEmpty(form.getPlanLockStartTimeSrch())){
			form.setPlanLockStartTimeSrch(GetDate.getDate("yyyy-MM-dd"));
		}
		if(StringUtils.isEmpty(form.getPlanLockEndTimeSrch())){
			form.setPlanLockEndTimeSrch(GetDate.getDate("yyyy-MM-dd"));
		}
		
		List<DebtPlan> recordList = this.planPushMoneyManageService.selectLockPlanList(form);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "序号", "智投编号", "智投名称", "服务回报期限", "授权服务金额", "实际募集金额", "提成总额", "计划进入锁定期时间" };

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
					DebtPlan bean = recordList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 计划编号
					else if (celLength == 1) {
						cell.setCellValue(bean.getDebtPlanNid());
					}
					// 计划名称
					else if (celLength == 2) {
						cell.setCellValue(bean.getDebtPlanName());
					}
					// 计划期限
					else if (celLength == 3) {
						cell.setCellValue(bean.getDebtLockPeriod() + "个月");
					}
					// 计划金额
					else if (celLength == 4) {
						cell.setCellValue(bean.getDebtPlanMoney() == null ? "0" : DF_FOR_VIEW.format(bean.getDebtPlanMoney()));
					}
					// 实际募集金额
					else if (celLength == 5) {
						cell.setCellValue(bean.getDebtPlanMoneyYes() == null ? "0" : DF_FOR_VIEW.format(bean.getDebtPlanMoneyYes()));
					}
					// 提成总额
					else if (celLength == 6) {
						cell.setCellValue(bean.getCommissionTotal() == null ? "0" : DF_FOR_VIEW.format(bean.getCommissionTotal()));
					}
					// 计划进入所定期时间
					else if (celLength == 7) {
						if (bean.getPlanLockTime() != null && bean.getPlanLockTime() != 0) {
							cell.setCellValue(GetDate.getDateTimeMyTime(bean.getPlanLockTime()));
						} else {
							cell.setCellValue(StringUtils.EMPTY);
						}
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
	}

	/**
	 * 提成明细初始化
	 * 
	 * @Title initDetail
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PlanPushMoneyManageDefine.PLAN_PUSHMONEY_DETAIL_ACTION)
	@RequiresPermissions(PlanPushMoneyManageDefine.PERMISSIONS_PUSHMONEYDETAIL_VIEW)
	public ModelAndView initDetail(HttpServletRequest request, PlanPushMoneyManageBean form) {
		LogUtil.startLog(THIS_CLASS, PlanPushMoneyManageDefine.PLAN_PUSHMONEY_DETAIL_ACTION);
		ModelAndView modelAndView = new ModelAndView(PlanPushMoneyManageDefine.PUSH_MONEY_DETAIL_LIST_PATH);
		this.createPushMoneyDetail(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, PlanPushMoneyManageDefine.PLAN_PUSHMONEY_DETAIL_ACTION);
		return modelAndView;
	}

	/**
	 * 提成明细检索
	 * 
	 * @Title searchPushMoneyDetail
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PlanPushMoneyManageDefine.PLAN_PUSHMONEY_DETAIL_SEARCH_ACTION)
	@RequiresPermissions(PlanPushMoneyManageDefine.PERMISSIONS_PUSHMONEYDETAIL_VIEW)
	public ModelAndView searchPushMoneyDetail(HttpServletRequest request, PlanPushMoneyManageBean form) {
		LogUtil.startLog(THIS_CLASS, PlanPushMoneyManageDefine.PLAN_PUSHMONEY_DETAIL_SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(PlanPushMoneyManageDefine.PUSH_MONEY_DETAIL_LIST_PATH);
		this.createPushMoneyDetail(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, PlanPushMoneyManageDefine.PLAN_PUSHMONEY_DETAIL_SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 提成明细分页
	 * 
	 * @Title createPushMoneyDetail
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPushMoneyDetail(HttpServletRequest request, ModelAndView modelAndView, PlanPushMoneyManageBean form) {
		
		int count = this.planPushMoneyManageService.countRecordTotal(form);
		if (count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			form.setLimitStart(paginator.getOffset());
			form.setLimitEnd(paginator.getLimit());
			List<AdminPlanPushMoneyDetailCustomize> recordList = this.planPushMoneyManageService.selectDebtAccedeCommission(form);
			form.setPushMoneyDetailList(recordList);
			form.setPaginator(paginator);
		}
		modelAndView.addObject(PlanPushMoneyManageDefine.FORM, form);
	}

	/**
	 * 发提成
	 * 
	 * @Title confirmPushMoneyAction
	 * @param request
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(PlanPushMoneyManageDefine.CONFIRM_PUSHMONEY)
	@RequiresPermissions(PlanPushMoneyManageDefine.PERMISSIONS_DETAIL_PUSHMONEY_CONFIRM)
	public String confirmPushMoneyAction(HttpServletRequest request, @RequestBody PlanPushMoneyManageBean form) {
		LogUtil.startLog(THIS_CLASS, PlanPushMoneyManageDefine.CONFIRM_PUSHMONEY);
		JSONObject ret = new JSONObject();
		// 加入订单ID
		String accedeOrderId = form.getOrderId();
		// 提成ID
		String id = form.getIds();

		DebtAccedeCommission accedeCommission = this.planPushMoneyManageService.selectAccedeCommissionByIdAndAccedeOrderId(id, accedeOrderId);
		// 提成未发放且提成>0
		if (accedeCommission != null && accedeCommission.getStatus() == 0 && accedeCommission.getCommission().compareTo(BigDecimal.ZERO) > 0) {
			Integer userId = accedeCommission.getUserId();
			/** 验证员工在平台的身份属性是否和crm的一致 如果不一致则不发提成 begin */
			UsersInfo usersInfo = this.planPushMoneyManageService.getUsersInfoByUserId(userId);
			// cuttype 提成发放方式（3线上2线下）
			Integer cuttype = this.planPushMoneyManageService.queryCrmCuttype(userId);

			if (usersInfo.getAttribute() != null && usersInfo.getAttribute() > 1) {
				if (usersInfo.getAttribute() != cuttype) {
					ret.put(PlanPushMoneyManageDefine.JSON_STATUS_KEY, PlanPushMoneyManageDefine.JSON_STATUS_NG);
					ret.put(PlanPushMoneyManageDefine.JSON_RESULT_KEY, "该用户属性异常！");
					LogUtil.errorLog(THIS_CLASS, PlanPushMoneyManageDefine.CONFIRM_PUSHMONEY, new Exception("该用户平台属性与CRM 不符！[userId=" + userId + "]"));
					return ret.toString();
				}
			}
			/** 验证员工在平台的身份属性是否和crm的一致 如果不一致则不发提成 end */

			/** redis 锁 */
//			if (StringUtils.isNotEmpty(RedisUtils.get("HTJ_PUSH_MONEY:" + id))) {
//				ret.put(PlanPushMoneyManageDefine.JSON_STATUS_KEY, PlanPushMoneyManageDefine.JSON_STATUS_NG);
//				ret.put(PlanPushMoneyManageDefine.JSON_RESULT_KEY, "数据已发生变化,请刷新页面!");
//				return ret.toString();
//			} else {
//				RedisUtils.set("HTJ_PUSH_MONEY:" + id, accedeCommission.getCommission().toString(), 5);
//			}
			
			boolean reslut = RedisUtils.tranactionSet("HTJ_PUSH_MONEY:" + id, 5);
			// 如果没有设置成功，说明有请求来设置过
			if(!reslut){
				ret.put(PlanPushMoneyManageDefine.JSON_STATUS_KEY, PlanPushMoneyManageDefine.JSON_STATUS_NG);
				ret.put(PlanPushMoneyManageDefine.JSON_RESULT_KEY, "数据已发生变化,请刷新页面!");
				return ret.toString();
			}
			
			// 查询商户子账户余额
			ChinapnrBean accountBean = new ChinapnrBean();

			// 构建请求参数
			accountBean.setVersion(ChinaPnrConstant.VERSION_10); // 版本号(必须)
			accountBean.setCmdId(ChinaPnrConstant.CMDID_QUERY_ACCTS); // 消息类型(必须)
			accountBean.setMerCustId(PropUtils.getSystem(ChinaPnrConstant.PROP_MERCUSTID)); // 商户客户号
			// 发送请求获取结果
			ChinapnrBean resultBean = ChinapnrUtil.callApiBg(accountBean);
			String respCode = resultBean == null ? "" : resultBean.getRespCode();
			// 如果接口调用成功
			if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode)) {
				// 如果接口返回的字符串不为空
				if (StringUtils.isNotBlank(resultBean.getAcctDetails())) {
					JSONArray acctDetailsList = JSONArray.parseArray(resultBean.getAcctDetails());
					for (Object object : acctDetailsList) {
						JSONObject acctObject = (JSONObject) object;
						// 查询推广提成子账户的余额
						if (PropUtils.getSystem(ChinaPnrConstant.PROP_MERACCT06).equals(acctObject.getString("SubAcctId"))) {
							BigDecimal avlBalance = acctObject.getBigDecimal("AvlBal");
							if (avlBalance.compareTo(accedeCommission.getCommission()) < 0) {
								LogUtil.errorLog(THIS_CLASS, PlanPushMoneyManageDefine.CONFIRM_PUSHMONEY, new Exception("调用汇付接口发生错误"));
								ret.put(PlanPushMoneyManageDefine.JSON_STATUS_KEY, PlanPushMoneyManageDefine.JSON_STATUS_NG);
								ret.put(PlanPushMoneyManageDefine.JSON_RESULT_KEY, "推广提成子账户余额不足,请先充值或向该子账户转账");
								return ret.toString();
							}
						}
					}
				}
			} else {
				System.out.println("没有查询到商户可用余额");
				LogUtil.errorLog(THIS_CLASS, PlanPushMoneyManageDefine.CONFIRM_PUSHMONEY, new Exception("调用汇付接口发生错误"));
				ret.put(PlanPushMoneyManageDefine.JSON_STATUS_KEY, PlanPushMoneyManageDefine.JSON_STATUS_NG);
				ret.put(PlanPushMoneyManageDefine.JSON_RESULT_KEY, "没有查询到商户可用余额");
				return ret.toString();
			}

			// 取得用户在汇付天下的账户信息
			AccountChinapnr accountChinapnr = this.planPushMoneyManageService.getChinapnrUserInfo(userId);

			// 用户未开户时,返回错误信息
			if (accountChinapnr == null) {
				ret.put(PlanPushMoneyManageDefine.JSON_STATUS_KEY, PlanPushMoneyManageDefine.JSON_STATUS_NG);
				ret.put(PlanPushMoneyManageDefine.JSON_RESULT_KEY, "该用户未开户");
				LogUtil.errorLog(THIS_CLASS, PlanPushMoneyManageDefine.CONFIRM_PUSHMONEY, new Exception("参数不正确[userId=" + userId + "]"));
				return ret.toString();
			}
			// IP地址
			String ip = CustomUtil.getIpAddr(request);
			// 调用汇付接口(4.3.11.3 自动扣款转账（商户用）)
			ChinapnrBean bean = new ChinapnrBean();
			bean.setVersion(ChinaPnrConstant.VERSION_10);
			bean.setCmdId(ChinaPnrConstant.CMDID_TRANSFER); // 消息类型(必须)
			bean.setOrdId(GetOrderIdUtils.getOrderId2(userId)); // 订单号(必须)
			bean.setOutCustId(PropUtils.getSystem(ChinaPnrConstant.PROP_MERCUSTID));// 出账客户号(必须)
			bean.setOutAcctId(PropUtils.getSystem(ChinaPnrConstant.PROP_MERACCT06));// 出账子账户(必须)
			bean.setTransAmt(accedeCommission.getCommission().toString()); // 交易金额(必须)
			bean.setInCustId(accountChinapnr.getChinapnrUsrcustid().toString()); // 入账客户号(必须)
			bean.setInAcctId(""); // 入账子账户(可选)
			bean.setBgRetUrl(ChinapnrUtil.getBgRetUrl());

			// 写log用参数
			bean.setLogUserId(userId); // 操作者ID
			bean.setLogRemark("出借推广提成"); // 备注
			bean.setLogClient("0"); // PC
			bean.setLogIp(ip); // IP地址

			// 调用汇付接口
			ChinapnrBean chinaPnrBean = ChinapnrUtil.callApiBg(bean);

			if (chinaPnrBean == null) {
				LogUtil.errorLog(THIS_CLASS, PlanPushMoneyManageDefine.CONFIRM_PUSHMONEY, new Exception("调用汇付接口发生错误"));
				ret.put(PlanPushMoneyManageDefine.JSON_STATUS_KEY, PlanPushMoneyManageDefine.JSON_STATUS_NG);
				ret.put(PlanPushMoneyManageDefine.JSON_RESULT_KEY, "发提成时发生错误,请重新操作!");
				// 更新失败状态
				this.planPushMoneyManageService.updateAccedeCommissoinRecordError(accedeCommission, bean);
				return ret.toString();
			}

			int cnt = 0;
			// 接口返回正常时,执行更新操作
			if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(chinaPnrBean.getRespCode())) {
				chinaPnrBean.setLogUserId(userId); // 操作者ID
				chinaPnrBean.setLogRemark("汇添金计划提成"); // 备注
				chinaPnrBean.setLogClient("0"); // PC
				chinaPnrBean.setLogIp(ip); // IP地址
				try {
					// 发提成处理
					cnt = this.planPushMoneyManageService.updateAccedeCommissionRecord(accedeCommission, chinaPnrBean);
				} catch (Exception e) {
					LogUtil.errorLog(THIS_CLASS, PlanPushMoneyManageDefine.CONFIRM_PUSHMONEY, e);
				}
			} else {
				LogUtil.errorLog(THIS_CLASS, PlanPushMoneyManageDefine.CONFIRM_PUSHMONEY, new Exception("调用汇付接口发生错误"));
				ret.put(PlanPushMoneyManageDefine.JSON_STATUS_KEY, PlanPushMoneyManageDefine.JSON_STATUS_NG);
				ret.put(PlanPushMoneyManageDefine.JSON_RESULT_KEY, "发提成时发生错误,请重新操作!");
				// 更新失败状态
				this.planPushMoneyManageService.updateAccedeCommissoinRecordError(accedeCommission, bean);
				return ret.toString();
			}

			// 返现成功
			if (cnt > 0) {
				ret.put(PlanPushMoneyManageDefine.JSON_STATUS_KEY, PlanPushMoneyManageDefine.JSON_STATUS_OK);
				ret.put(PlanPushMoneyManageDefine.JSON_RESULT_KEY, "发提成操作成功!");
			} else {
				ret.put(PlanPushMoneyManageDefine.JSON_STATUS_KEY, PlanPushMoneyManageDefine.JSON_STATUS_NG);
				ret.put(PlanPushMoneyManageDefine.JSON_RESULT_KEY, "发提成时发生错误,请重新操作!");
			}
		}

		LogUtil.endLog(THIS_CLASS, PlanPushMoneyManageDefine.CONFIRM_PUSHMONEY);
		return ret.toString();

	}

	/**
	 * 根据业务需求导出相应的表格 此处暂时为可用情况 缺陷： 1.无法指定相应的列的顺序， 2.无法配置，excel文件名，excel sheet名称
	 * 3.目前只能导出一个sheet 4.列的宽度的自适应，中文存在一定问题
	 * 5.根据导出的业务需求最好可以在导出的时候输入起止页码，因为在大数据量的情况下容易造成卡顿
	 *
	 * 导出推广提成列表
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(PlanPushMoneyManageDefine.EXPORT_PUSHMONEY_DETAIL_ACTION)
	@RequiresPermissions(PlanPushMoneyManageDefine.PERMISSIONS_DETAIL_PUSHMONEY_EXPORT)
	public void exportPushMoneyDetailExcel(HttpServletRequest request, HttpServletResponse response, PlanPushMoneyManageBean form) throws Exception {

		// 表格sheet名称
		String sheetName = "汇添金提成发放列表";

		// 取得数据
		form.setLimitStart(-1);
		form.setLimitEnd(-1);
		
		List<AdminPlanPushMoneyDetailCustomize> recordList = this.planPushMoneyManageService.selectDebtAccedeCommission(form);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "序号", "提成人", "提成金额", "提成转账订单号", "提成发放时间", "分公司", "分部", "团队", "提成人属性", "51老用户", "出借人", "授权服务金额", "智投编号", "智投订单号", "服务回报期限", "授权服务时间", "提成发放状态" };
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
					AdminPlanPushMoneyDetailCustomize bean = recordList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 提成人
					else if (celLength == 1) {
						cell.setCellValue(bean.getUserName());
					}
					// 提成金额
					else if (celLength == 2) {
						cell.setCellValue(bean.getCommission().toString());
					}
					// 提成转账订单号
					else if (celLength == 3) {
						cell.setCellValue(bean.getOrderId());
					}
					// 提成发放时间
					else if (celLength == 4) {
						if (StringUtils.isNotEmpty(bean.getReturnTime())) {
							cell.setCellValue(GetDate.getDateTimeMyTime(Integer.parseInt(bean.getReturnTime())));
						} else {
							cell.setCellValue(StringUtils.EMPTY);
						}
					}
					// 分公司
					else if (celLength == 5) {
						cell.setCellValue(bean.getRegionName());
					}
					// 分部
					else if (celLength == 6) {
						cell.setCellValue(bean.getBranchName());
					}
					// 团队
					else if (celLength == 7) {
						cell.setCellValue(HtmlUtil.unescape(bean.getDepartmentName()));
					}
					// 提成人属性
					else if (celLength == 8) {
						cell.setCellValue(bean.getAttribute());
					}
					// 51老用户
					else if (celLength == 9) {
						if ("1".equals(bean.getIs51())) {
							cell.setCellValue("是");
						} else {
							cell.setCellValue("否");
						}
					}
					// 出借人
					else if (celLength == 10) {
						cell.setCellValue(bean.getAccedeUserName());
					}
					// 授权服务金额
					else if (celLength == 11) {
						cell.setCellValue(bean.getAccedeAccount().toString());
					}
					// 智投编号
					else if (celLength == 12) {
						cell.setCellValue(bean.getDebtPlanNid());
					}

					// 智投订单号
					else if(celLength ==13){
						cell.setCellValue(bean.getAccedeOrderId());
					}

					// 服务回报期限
					else if (celLength == 14) {
						cell.setCellValue(bean.getDebtLockPeriod() + "个月");
					}

					// 出借时间
					else if (celLength == 15) {
						cell.setCellValue(GetDate.getDateTimeMyTime(bean.getAccedeTime()));
					}
					// 状态
					else if (celLength == 16) {
						if ("1".equals(bean.getStatus())) {
							cell.setCellValue("已发放");
						} else if ("0".equals(bean.getStatus())) {
							cell.setCellValue("未发放");
						} else if ("2".equals(bean.getStatus())) {
							cell.setCellValue("发放失败");
						}
					}

				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
	}

	/**
	 * 取得部门信息
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getCrmDepartmentList")
	public String getCrmDepartmentListAction(@RequestBody PlanPushMoneyManageBean form) {
		// 部门
		String[] list = new String[] {};
		if (Validator.isNotNull(form.getDepIds())) {
			if (form.getDepIds().contains(StringPool.COMMA)) {
				list = form.getDepIds().split(StringPool.COMMA);
			} else {
				list = new String[] { form.getDepIds() };
			}
		}

		JSONArray ja = this.planPushMoneyManageService.getCrmDepartmentList(list);
		if (ja != null) {
			return ja.toString();
		}

		return StringUtils.EMPTY;
	}
}
