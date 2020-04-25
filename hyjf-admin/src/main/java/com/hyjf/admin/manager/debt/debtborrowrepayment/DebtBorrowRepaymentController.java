package com.hyjf.admin.manager.debt.debtborrowrepayment;

import java.math.BigDecimal;
import java.text.ParseException;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.debt.debtborrowcommon.DebtBorrowCommonService;
import com.hyjf.admin.manager.debt.debtborrowrepayment.plan.DebtBorrowRepaymentPlanDefine;
import com.hyjf.admin.manager.debt.debtborrowrepaymentinfo.DebtBorrowRepaymentInfoDefine;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.DebtRepay;
import com.hyjf.mybatis.model.auto.DebtRepayDetail;
import com.hyjf.mybatis.model.customize.admin.htj.DebtAdminRepayDelayCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowRepaymentCustomize;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = DebtBorrowRepaymentDefine.REQUEST_MAPPING)
public class DebtBorrowRepaymentController extends BaseController {

	@Autowired
	private DebtBorrowRepaymentService borrowRepaymentService;

	@Autowired
	private DebtBorrowCommonService borrowCommonService;

	/**
	 * 画面初始化
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(DebtBorrowRepaymentDefine.INIT)
	@RequiresPermissions(DebtBorrowRepaymentDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute(DebtBorrowRepaymentDefine.REPAYMENT_FORM) DebtBorrowRepaymentBean form) {
		LogUtil.startLog(DebtBorrowRepaymentController.class.toString(), DebtBorrowRepaymentDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(DebtBorrowRepaymentDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(DebtBorrowRepaymentController.class.toString(), DebtBorrowRepaymentDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面初始化
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(DebtBorrowRepaymentDefine.SEARCH_ACTION)
	@RequiresPermissions(DebtBorrowRepaymentDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, DebtBorrowRepaymentBean form) {
		LogUtil.startLog(DebtBorrowRepaymentController.class.toString(), DebtBorrowRepaymentDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(DebtBorrowRepaymentDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(DebtBorrowRepaymentController.class.toString(), DebtBorrowRepaymentDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 创建分页
	 *
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, DebtBorrowRepaymentBean form) {
		DebtBorrowRepaymentCustomize borrowRepaymentCustomize = new DebtBorrowRepaymentCustomize();
		// 还款方式
		List<BorrowStyle> repayTypeList = this.borrowCommonService.borrowStyleList("");
		modelAndView.addObject("repayTypeList", repayTypeList);
		BeanUtils.copyProperties(form, borrowRepaymentCustomize);
		Long count = this.borrowRepaymentService.countBorrowRepayment(borrowRepaymentCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			borrowRepaymentCustomize.setLimitStart(paginator.getOffset());
			borrowRepaymentCustomize.setLimitEnd(paginator.getLimit());
			List<DebtBorrowRepaymentCustomize> recordList = this.borrowRepaymentService.selectBorrowRepaymentList(borrowRepaymentCustomize);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
			DebtBorrowRepaymentCustomize sumObject = this.borrowRepaymentService.sumBorrowRepaymentInfo(borrowRepaymentCustomize);
			modelAndView.addObject("sumObject", sumObject);
		}
		modelAndView.addObject(DebtBorrowRepaymentDefine.REPAYMENT_FORM, form);
	}

	/**
	 * 数据导出
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(DebtBorrowRepaymentDefine.EXPORT_ACTION)
	@RequiresPermissions(DebtBorrowRepaymentDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, DebtBorrowRepaymentBean form) {
		LogUtil.startLog(DebtBorrowRepaymentController.class.toString(), DebtBorrowRepaymentDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "还款信息导出数据";

		DebtBorrowRepaymentCustomize borrowRepaymentCustomize = new DebtBorrowRepaymentCustomize();
		BeanUtils.copyProperties(form, borrowRepaymentCustomize);

		List<DebtBorrowRepaymentCustomize> recordList = this.borrowRepaymentService.selectBorrowRepaymentList(borrowRepaymentCustomize);
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "项目编号", "借款人ID", "借款人用户名", "项目名称", "项目类型", "借款期限", "出借利率", "借款金额", "借到金额", "还款方式", "应还本金", "应还利息", "应还本息", "管理费", "已还本金", "已还利息", "已还本息", "未还本金", "未还利息", "未还本息", "还款状态", "最后还款日", "下期还款日" };
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
					DebtBorrowRepaymentCustomize record = recordList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					// 项目编号
					if (celLength == 0) {
						cell.setCellValue(record.getBorrowNid());
					}
					// 借款人ID
					else if (celLength == 1) {
						cell.setCellValue(record.getUserId());
					}
					// 借款人用户名
					else if (celLength == 2) {
						cell.setCellValue(record.getBorrowUserName());
					}
					// 项目名称
					else if (celLength == 3) {
						cell.setCellValue(record.getBorrowName());
					}
					// 项目类型
					else if (celLength == 4) {
						cell.setCellValue(record.getProjectTypeName());
					}
					// 借款期限
					else if (celLength == 5) {
						cell.setCellValue(record.getBorrowPeriod());
					}
					// 出借利率
					else if (celLength == 6) {
						cell.setCellValue(record.getBorrowApr() + "%");
					}
					// 借款金额
					else if (celLength == 7) {
						cell.setCellValue(record.getBorrowAccount().equals("") ? 0 : Double.valueOf(record.getBorrowAccount()));
					}
					// 借到金额
					else if (celLength == 8) {
						cell.setCellValue(record.getBorrowAccountYes().equals("") ? 0 : Double.valueOf(record.getBorrowAccountYes()));
					}
					// 还款方式
					else if (celLength == 9) {
						cell.setCellValue(record.getRepayType());
					}
					// 应还本金
					else if (celLength == 10) {
						cell.setCellValue(record.getRepayAccountCapital().equals("") ? 0 : Double.valueOf(record.getRepayAccountCapital()));
					}
					// 应还利息
					else if (celLength == 11) {
						cell.setCellValue(record.getRepayAccountInterest().equals("") ? 0 : Double.valueOf(record.getRepayAccountInterest()));
					}
					// 应还本息
					else if (celLength == 12) {
						cell.setCellValue(record.getRepayAccountAll().equals("") ? 0 : Double.valueOf(record.getRepayAccountAll()));
					}
					// 管理费
					else if (celLength == 13) {
						cell.setCellValue(record.getRepayFee().equals("") ? 0 : Double.valueOf(record.getRepayFee()));
					}
					// 已还本金
					else if (celLength == 14) {
						cell.setCellValue(record.getRepayAccountCapitalYes().equals("") ? 0 : Double.valueOf(record.getRepayAccountCapitalYes()));
					}
					// 已还利息
					else if (celLength == 15) {
						cell.setCellValue(record.getRepayAccountInterestYes().equals("") ? 0 : Double.valueOf(record.getRepayAccountInterestYes()));
					}
					// 已还本息
					else if (celLength == 16) {
						cell.setCellValue(record.getRepayAccountAll().equals("") ? 0 : Double.valueOf(record.getRepayAccountAll()));
					}
					// 未还本金
					else if (celLength == 17) {
						cell.setCellValue(record.getRepayAccountCapitalWait().equals("") ? 0 : Double.valueOf(record.getRepayAccountCapitalWait()));
					}
					// 未还利息
					else if (celLength == 18) {
						cell.setCellValue(record.getRepayAccountInterestWait().equals("") ? 0 : Double.valueOf(record.getRepayAccountInterestWait()));
					}
					// 未还本息
					else if (celLength == 19) {
						cell.setCellValue(record.getRepayAccountWait().equals("") ? 0 : Double.valueOf(record.getRepayAccountWait()));
					}
					// 还款状态
					else if (celLength == 20) {
						if (StringUtils.isNotEmpty(record.getStatus())) {
							cell.setCellValue(record.getStatus().equals("0") ? "还款中" : "已还款");
						}
					}
					// 最后还款日
					else if (celLength == 21) {
						cell.setCellValue(record.getRepayLastTime());
					}
					// 下期还款日
					else if (celLength == 22) {
						cell.setCellValue(record.getRepayNextTime());
					}

				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
		LogUtil.endLog(DebtBorrowRepaymentController.class.toString(), DebtBorrowRepaymentDefine.EXPORT_ACTION);
	}

	/**
	 * 跳转到还款计划
	 *
	 * @param request
	 * @param form
	 */
	@RequestMapping(DebtBorrowRepaymentDefine.REPAY_PLAN_ACTION)
	public ModelAndView replayPlanAction(HttpServletRequest request, DebtBorrowRepaymentBean form, RedirectAttributes attr) {
		LogUtil.startLog(DebtBorrowRepaymentController.class.toString(), DebtBorrowRepaymentDefine.REPAY_PLAN_ACTION);
		ModelAndView modelAndView = new ModelAndView(DebtBorrowRepaymentPlanDefine.RE_LIST_PATH);
		attr.addAttribute("borrowNid", form.getBorrowNid());
		// 跳转到还款计划
		LogUtil.endLog(DebtBorrowRepaymentController.class.toString(), DebtBorrowRepaymentDefine.REPAY_PLAN_ACTION);
		return modelAndView;
	}

	/**
	 * 跳转到详情
	 *
	 * @param request
	 * @param form
	 */
	@RequestMapping(DebtBorrowRepaymentDefine.TO_RECOVER_ACTION)
	public ModelAndView toRecoverAction(HttpServletRequest request, DebtBorrowRepaymentBean form, RedirectAttributes attr) {
		LogUtil.startLog(DebtBorrowRepaymentController.class.toString(), DebtBorrowRepaymentDefine.TO_RECOVER_ACTION);
		ModelAndView modelAndView = new ModelAndView(DebtBorrowRepaymentInfoDefine.RE_LIST_PATH);
		attr.addAttribute("borrowNid", form.getBorrowNid());
		// 跳转到还款计划
		LogUtil.endLog(DebtBorrowRepaymentController.class.toString(), DebtBorrowRepaymentDefine.TO_RECOVER_ACTION);
		return modelAndView;
	}

	// -------------------------------------------------------

	/**
	 * 迁移到延期画面
	 *
	 * @param request
	 * @param form
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(DebtBorrowRepaymentDefine.INIT_DELAY_REPAY_ACTION)
	public ModelAndView moveAfterRepayAction(HttpServletRequest request, DebtRepayDelayCustomizeBean form) throws ParseException {
		LogUtil.startLog(DebtBorrowRepaymentController.class.toString(), DebtBorrowRepaymentDefine.INIT_DELAY_REPAY_ACTION);
		ModelAndView modelAndView = new ModelAndView(DebtBorrowRepaymentDefine.DELAY_REPAY_PATH);
		modelAndView = this.getDelayRepayInfo(modelAndView, form.getNid());
		LogUtil.endLog(DebtBorrowRepaymentController.class.toString(), DebtBorrowRepaymentDefine.INIT_DELAY_REPAY_ACTION);
		return modelAndView;
	}

	/**
	 * 获取延期数据
	 *
	 * @param modelAndView
	 * @param form
	 * @return
	 * @throws ParseException
	 */
	private ModelAndView getDelayRepayInfo(ModelAndView modelAndView, String borrowNid) throws ParseException {
		DebtAdminRepayDelayCustomize repayDelay = this.borrowRepaymentService.selectBorrowInfo(borrowNid);
		modelAndView.addObject("borrowRepayInfo", repayDelay);
		// 单期标
		if (CustomConstants.BORROW_STYLE_ENDDAY.equals(repayDelay.getBorrowStyle()) || CustomConstants.BORROW_STYLE_END.equals(repayDelay.getBorrowStyle())) {
			DebtRepay borrowRepay = this.borrowRepaymentService.getBorrowRepayDelay(borrowNid, repayDelay.getBorrowApr(), repayDelay.getBorrowStyle());
			modelAndView.addObject("repayInfo", borrowRepay);
			modelAndView.addObject("repayTime", GetDate.formatDate(Long.valueOf(borrowRepay.getRepayTime()) * 1000L));
			modelAndView.addObject("delayDays", borrowRepay.getDelayDays());
		} else {
			DebtRepayDetail borrowRepayPlan = this.borrowRepaymentService.getBorrowRepayPlanDelay(borrowNid, repayDelay.getBorrowApr(), repayDelay.getBorrowStyle());
			modelAndView.addObject("repayInfo", borrowRepayPlan);
			modelAndView.addObject("repayTime", GetDate.formatDate(Long.valueOf(borrowRepayPlan.getRepayTime()) * 1000L));
			modelAndView.addObject("delayDays", borrowRepayPlan.getDelayDays());
		}
		return modelAndView;
	}

	/**
	 * 延期
	 *
	 * @param request
	 * @param form
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(DebtBorrowRepaymentDefine.DELAY_REPAY_ACTION)
	public ModelAndView afterRepayAction(HttpServletRequest request, DebtRepayDelayCustomizeBean form) throws ParseException {
		LogUtil.startLog(DebtBorrowRepaymentController.class.toString(), DebtBorrowRepaymentDefine.DELAY_REPAY_ACTION);
		ModelAndView modelAndView = new ModelAndView(DebtBorrowRepaymentDefine.DELAY_REPAY_PATH);

		String borrowNid = form.getBorrowNid();

		boolean afterDayFlag = ValidatorFieldCheckUtil.validateSignlessNum(modelAndView, "delayDays", form.getDelayDays(), 1, true);

		if (afterDayFlag) {

			if (!(Integer.valueOf(form.getDelayDays()) >= 1 && Integer.valueOf(form.getDelayDays()) <= 8)) {
				ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "delayDays", "after.repay.day");
			} else {
				// 延期日8天后、应该还款的日期
				Integer lastReapyTime = Integer.valueOf(form.getRepayTime()) + (8 * 24 * 3600);
				// 延期日加上输入的天数后、应该还款的日期
				Integer nowTimePlusDelayDays = Integer.valueOf(form.getRepayTime()) + (Integer.valueOf(form.getDelayDays()) * 24 * 3600);
				if (nowTimePlusDelayDays > lastReapyTime) {
					String repayTimeForMsg = GetDate.formatDate(Long.valueOf(lastReapyTime) * 1000);
					modelAndView.addObject("repayTimeForMsg", repayTimeForMsg);
					ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "delayDays", "delay.day.lt.repaytime");
				}
			}
		}

		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			modelAndView = this.getDelayRepayInfo(modelAndView, borrowNid);
			modelAndView.addObject("delayDays", form.getDelayDays());
			return modelAndView;
		}

		this.borrowRepaymentService.updateBorrowRepayDelayDays(borrowNid, form.getDelayDays());

		modelAndView.addObject(DebtBorrowRepaymentDefine.SUCCESS, DebtBorrowRepaymentDefine.SUCCESS);
		LogUtil.endLog(DebtBorrowRepaymentController.class.toString(), DebtBorrowRepaymentDefine.DELAY_REPAY_ACTION);
		return modelAndView;
	}

	/**
	 * 迁移到还款画面
	 *
	 * @param request
	 * @param form
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(DebtBorrowRepaymentDefine.INIT_REPAY_ACTION)
	public ModelAndView moveRepayAction(HttpServletRequest request, DebtRepayCustomizeBean form) throws ParseException {
		LogUtil.startLog(DebtBorrowRepaymentController.class.toString(), DebtBorrowRepaymentDefine.INIT_REPAY_ACTION);
		ModelAndView modelAndView = new ModelAndView(DebtBorrowRepaymentDefine.REPAY_PATH);
		modelAndView = this.getRepayInfo(modelAndView, form.getNid());
		LogUtil.endLog(DebtBorrowRepaymentController.class.toString(), DebtBorrowRepaymentDefine.INIT_REPAY_ACTION);
		return modelAndView;
	}

	/**
	 * 获取还款数据
	 *
	 * @param modelAndView
	 * @param form
	 * @return
	 * @throws ParseException
	 */
	private ModelAndView getRepayInfo(ModelAndView modelAndView, String borrowNid) throws ParseException {

		DebtAdminRepayDelayCustomize repayDelay = this.borrowRepaymentService.selectBorrowInfo(borrowNid);
		modelAndView.addObject("borrowRepayInfo", repayDelay);
		String userId = repayDelay.getUserId();
		Account account = this.borrowRepaymentService.getUserAccount(userId);
		BigDecimal balance = account.getBalance();
		modelAndView.addObject("balance", balance);
		// 单期标
		if (CustomConstants.BORROW_STYLE_ENDDAY.equals(repayDelay.getBorrowStyle()) || CustomConstants.BORROW_STYLE_END.equals(repayDelay.getBorrowStyle())) {
			DebtBorrowRepayBean borrowRepay = this.borrowRepaymentService.getBorrowRepayInfo(borrowNid, repayDelay.getBorrowApr(), repayDelay.getBorrowStyle());
			modelAndView.addObject("repayInfo", borrowRepay);
		} else {// 多期标
			DebtBorrowRepayPlanBean borrowRepayPlan = this.borrowRepaymentService.getBorrowRepayPlanInfo(borrowNid, repayDelay.getBorrowApr(), repayDelay.getBorrowStyle());
			modelAndView.addObject("repayInfo", borrowRepayPlan);
		}
		return modelAndView;
	}

	/**
	 * 还款
	 *
	 * @param request
	 * @param form
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(DebtBorrowRepaymentDefine.REPAY_ACTION)
	public ModelAndView repayAction(HttpServletRequest request, DebtRepayCustomizeBean form) throws ParseException {

		LogUtil.startLog(DebtBorrowRepaymentController.class.toString(), DebtBorrowRepaymentDefine.REPAY_ACTION);
		ModelAndView modelAndView = new ModelAndView(DebtBorrowRepaymentDefine.REPAY_PATH);
		// 还款密码
		String password = form.getPassword();
		// 还款密码加密处理
		String mdPassword = "123456";

		String borrowNid = form.getBorrowNid();
		String userId = form.getUserId();
		String borrowApr = form.getBorrowApr();
		String borrowStyle = form.getBorrowStyle();
		Account account = this.borrowRepaymentService.getUserAccount(userId);
		BigDecimal balance = account.getBalance();
		// 加密后的密码与数据库的密码进行比较
		if (!StringUtils.equals(mdPassword, password)) {
			ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "password", "repay.password");
		}
		if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle) || CustomConstants.BORROW_STYLE_END.equals(borrowStyle)) {
			DebtBorrowRepayBean borrowRepay = this.borrowRepaymentService.getBorrowRepayInfo(borrowNid, borrowApr, borrowStyle);
			BigDecimal repayTotal = borrowRepay.getRepayAccountAll();
			if (repayTotal.compareTo(balance) == 0 || repayTotal.compareTo(balance) == -1) {
				AccountChinapnr accountChinapnr = this.borrowRepaymentService.getChinapnrUserInfo(Integer.parseInt(userId));
				BigDecimal userBalance = this.borrowRepaymentService.getUserBalance(accountChinapnr.getChinapnrUsrcustid());
				if (repayTotal.compareTo(userBalance) == 0 || repayTotal.compareTo(userBalance) == -1) {

				} else {
					ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "password", "repay.balance.huifu");
				}
			} else {
				ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "password", "repay.balance");
			}
			if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
				modelAndView = this.getRepayInfo(modelAndView, borrowNid);
				return modelAndView;
			}
			String ip = GetCilentIP.getIpAddr(request);
			borrowRepay.setIp(ip);
			this.borrowRepaymentService.updateBorrowRecover(borrowRepay);
		} else {
			DebtBorrowRepayPlanBean borrowRepayPlan = this.borrowRepaymentService.getBorrowRepayPlanInfo(borrowNid, borrowApr, borrowStyle);
			BigDecimal repayTotal = borrowRepayPlan.getRepayAccountAll();
			if (repayTotal.compareTo(balance) == 0 || repayTotal.compareTo(balance) == -1) {
				AccountChinapnr accountChinapnr = this.borrowRepaymentService.getChinapnrUserInfo(Integer.parseInt(userId));
				BigDecimal userBalance = this.borrowRepaymentService.getUserBalance(accountChinapnr.getChinapnrUsrcustid());
				if (repayTotal.compareTo(userBalance) == 0 || repayTotal.compareTo(userBalance) == -1) {

				} else {
					ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "password", "repay.balance.huifu");
				}
			} else {
				ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "password", "repay.balance");
			}
			if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
				modelAndView = this.getRepayInfo(modelAndView, borrowNid);
				return modelAndView;
			}
			String ip = GetCilentIP.getIpAddr(request);
			borrowRepayPlan.setIp(ip);
			this.borrowRepaymentService.updateBorrowRecoverPlan(borrowRepayPlan);
		}

		modelAndView.addObject(DebtBorrowRepaymentDefine.SUCCESS, DebtBorrowRepaymentDefine.SUCCESS);
		LogUtil.endLog(DebtBorrowRepaymentController.class.toString(), DebtBorrowRepaymentDefine.REPAY_ACTION);
		return modelAndView;
	}

	/**
	 * 重新还款按钮
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(DebtBorrowRepaymentDefine.RESTART_REPAY_ACTION)
	public ModelAndView restartRepayAction(HttpServletRequest request, RedirectAttributes attr, DebtBorrowRepaymentBean form) throws ParseException {
		LogUtil.startLog(DebtBorrowRepaymentController.class.toString(), DebtBorrowRepaymentDefine.RESTART_REPAY_ACTION);
		ModelAndView modelAndView = new ModelAndView(DebtBorrowRepaymentDefine.LIST_PATH);

		attr.addFlashAttribute(DebtBorrowRepaymentDefine.REPAYMENT_FORM, form);
		modelAndView = new ModelAndView(DebtBorrowRepaymentDefine.RE_LIST_PATH);
		this.borrowRepaymentService.updateBorrowApicronRecord(form == null ? "" : form.getBorrowNidHidden());

		LogUtil.endLog(DebtBorrowRepaymentController.class.toString(), DebtBorrowRepaymentDefine.RESTART_REPAY_ACTION);
		return modelAndView;
	}

	/**
	 * 更新管理费
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequiresPermissions(DebtBorrowRepaymentDefine.PERMISSIONS_ADD)
	@RequestMapping(DebtBorrowRepaymentDefine.UPDATE_RECOVER_FEE_ACTION)
	public ModelAndView updateRecoverFeeAction(HttpServletRequest request, RedirectAttributes attr, DebtBorrowRepaymentBean form) throws ParseException {
		LogUtil.startLog(DebtBorrowRepaymentController.class.toString(), DebtBorrowRepaymentDefine.RESTART_REPAY_ACTION);
		ModelAndView modelAndView = new ModelAndView(DebtBorrowRepaymentDefine.LIST_PATH);

		attr.addFlashAttribute(DebtBorrowRepaymentDefine.REPAYMENT_FORM, form);
		modelAndView = new ModelAndView(DebtBorrowRepaymentDefine.RE_LIST_PATH);
		try {
			this.borrowRepaymentService.incomeFeeService();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		LogUtil.endLog(DebtBorrowRepaymentController.class.toString(), DebtBorrowRepaymentDefine.RESTART_REPAY_ACTION);
		return modelAndView;
	}
}
