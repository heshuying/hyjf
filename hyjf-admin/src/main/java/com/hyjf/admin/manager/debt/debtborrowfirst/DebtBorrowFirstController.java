package com.hyjf.admin.manager.debt.debtborrowfirst;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.codehaus.plexus.util.StringUtils;
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
import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.DebtBorrowWithBLOBs;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowFirstCustomize;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = DebtBorrowFirstDefine.REQUEST_MAPPING)
public class DebtBorrowFirstController extends BaseController {

	@Autowired
	private DebtBorrowFirstService debtBorrowFirstService;

	@Autowired
	private DebtBorrowCommonService debtBorrowCommonService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(DebtBorrowFirstDefine.INIT)
	@RequiresPermissions(DebtBorrowFirstDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute("borrowfirstForm") DebtBorrowFirstBean form) {
		LogUtil.startLog(DebtBorrowFirstController.class.toString(), DebtBorrowFirstDefine.INIT);
		ModelAndView modeAndView = new ModelAndView(DebtBorrowFirstDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(DebtBorrowFirstController.class.toString(), DebtBorrowFirstDefine.INIT);
		return modeAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(DebtBorrowFirstDefine.SEARCH_ACTION)
	@RequiresPermissions(DebtBorrowFirstDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, DebtBorrowFirstBean form) {
		LogUtil.startLog(DebtBorrowFirstController.class.toString(), DebtBorrowFirstDefine.SEARCH_ACTION);
		ModelAndView modeAndView = new ModelAndView(DebtBorrowFirstDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(DebtBorrowFirstController.class.toString(), DebtBorrowFirstDefine.SEARCH_ACTION);
		return modeAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, DebtBorrowFirstBean debtBorrowFirstBean) {

		DebtBorrowCommonCustomize debtBorrowCommonCustomize = new DebtBorrowCommonCustomize();
		// 借款编码
		debtBorrowCommonCustomize.setBorrowNidSrch(debtBorrowFirstBean.getBorrowNidSrch());
		// 项目名称
		debtBorrowCommonCustomize.setBorrowNameSrch(debtBorrowFirstBean.getBorrowNameSrch());
		// 用户名
		debtBorrowCommonCustomize.setUsernameSrch(debtBorrowFirstBean.getUsernameSrch());
		// 保证金状态
		debtBorrowCommonCustomize.setIsBailSrch(debtBorrowFirstBean.getIsBailSrch());
		// 添加时间
		debtBorrowCommonCustomize.setTimeStartSrch(debtBorrowFirstBean.getTimeStartSrch());
		// 添加时间
		debtBorrowCommonCustomize.setTimeEndSrch(debtBorrowFirstBean.getTimeEndSrch());

		debtBorrowCommonCustomize.setBorrowPeriod(debtBorrowFirstBean.getBorrowPeriod());

		Integer count = this.debtBorrowFirstService.countBorrowFirst(debtBorrowCommonCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(debtBorrowFirstBean.getPaginatorPage(), count);
			debtBorrowCommonCustomize.setLimitStart(paginator.getOffset());
			debtBorrowCommonCustomize.setLimitEnd(paginator.getLimit());
			List<DebtBorrowFirstCustomize> recordList = this.debtBorrowFirstService.selectBorrowFirstList(debtBorrowCommonCustomize);
			debtBorrowFirstBean.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
			String sumAccount = this.debtBorrowFirstService.sumBorrowFirstAccount(debtBorrowCommonCustomize);
			modelAndView.addObject("sumAccount", sumAccount);
		}
		modelAndView.addObject(DebtBorrowFirstDefine.BORROW_FORM, debtBorrowFirstBean);
	}

	/**
	 * 已交保证金详细画面
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = DebtBorrowFirstDefine.BAIL_INFO_ACTION)
	@RequiresPermissions(DebtBorrowFirstDefine.PERMISSIONS_BORROW_BAIL)
	public ModelAndView bailInfoAction(HttpServletRequest request, DebtBorrowFirstBean form) {
		LogUtil.startLog(DebtBorrowFirstController.class.toString(), DebtBorrowFirstDefine.BAIL_INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(DebtBorrowFirstDefine.BAIL_PATH);

		String borrowNid = form.getBorrowNid();

		if (StringUtils.isNotEmpty(borrowNid)) {
			DebtBorrowWithBLOBs debtBorrowWithBLOBs = this.debtBorrowCommonService.getBorrowWithBLOBs(borrowNid);
			if (debtBorrowWithBLOBs != null) {
				modelAndView.addObject("fireInfo", debtBorrowWithBLOBs);
				BigDecimal account = debtBorrowWithBLOBs.getAccount();
				modelAndView.addObject("userName", this.debtBorrowFirstService.getUserName(debtBorrowWithBLOBs.getUserId()));
				BigDecimal bailPercent = new BigDecimal(this.debtBorrowFirstService.getBorrowConfig(CustomConstants.BORROW_BAIL_RATE));// 计算公式：保证金金额=借款金额×3％
				double accountBail = (account.multiply(bailPercent)).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
				modelAndView.addObject("accountBail", accountBail);
			}
		}

		LogUtil.endLog(DebtBorrowFirstController.class.toString(), DebtBorrowFirstDefine.BAIL_INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 已交保证金
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = DebtBorrowFirstDefine.BAIL_ACTION)
	@RequiresPermissions(DebtBorrowFirstDefine.PERMISSIONS_BORROW_BAIL)
	public ModelAndView bailAction(HttpServletRequest request, DebtBorrowFirstBean form) {
		LogUtil.startLog(DebtBorrowFirstController.class.toString(), DebtBorrowFirstDefine.BAIL_ACTION);
		ModelAndView modelAndView = new ModelAndView(DebtBorrowFirstDefine.BAIL_PATH);

		String borrowPreNid = form.getBorrowPreNid();
		// 已交保证金
		this.debtBorrowFirstService.borrowBailRecord(borrowPreNid);

		modelAndView.addObject(DebtBorrowFirstDefine.SUCCESS, DebtBorrowFirstDefine.SUCCESS);
		modelAndView.addObject(DebtBorrowFirstDefine.BORROW_FORM, form);
		LogUtil.endLog(DebtBorrowFirstController.class.toString(), DebtBorrowFirstDefine.BAIL_PATH);
		return modelAndView;
	}

	/**
	 * 发标
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = DebtBorrowFirstDefine.FIRE_ACTION)
	@RequiresPermissions(DebtBorrowFirstDefine.PERMISSIONS_BORROW_FIRE)
	public ModelAndView fireAction(HttpServletRequest request, DebtBorrowFirstBean form) {
		LogUtil.startLog(DebtBorrowFirstController.class.toString(), DebtBorrowFirstDefine.FIRE_ACTION);
		ModelAndView modelAndView = new ModelAndView(DebtBorrowFirstDefine.FIRE_PATH);

		String borrowNid = form.getBorrowNid();

		if (StringUtils.isNotEmpty(borrowNid)) {
			DebtBorrowWithBLOBs borrowWithBLOBs = debtBorrowCommonService.getBorrowWithBLOBs(borrowNid);
			if (borrowWithBLOBs != null) {
				if (borrowWithBLOBs.getBookingBeginTime() != null && borrowWithBLOBs.getBookingBeginTime() != 0) {
					modelAndView.addObject("canUpdate", true);
				} else {
					modelAndView.addObject("canUpdate", false);
				}
				if (borrowWithBLOBs.getBookingStatus() != null && borrowWithBLOBs.getBookingStatus() != 0) {
					modelAndView.addObject("canUpdateTime", true);
				} else {
					modelAndView.addObject("canUpdateTime", false);
				}
				modelAndView.addObject("projectType", borrowWithBLOBs.getProjectType());
				modelAndView.addObject("fireInfo", borrowWithBLOBs);
				if (borrowWithBLOBs.getOntime() != null && borrowWithBLOBs.getOntime() != 0) {
					modelAndView.addObject("ontime", GetDate.timestamptoStrYYYYMMDDHHMM(String.valueOf(borrowWithBLOBs.getOntime())));
				}
				if (borrowWithBLOBs.getBookingBeginTime() != null && borrowWithBLOBs.getBookingBeginTime() != 0) {
					modelAndView.addObject("bookingBeginTime", GetDate.timestamptoStrYYYYMMDDHHMM(String.valueOf(borrowWithBLOBs.getBookingBeginTime())));
				}
				if (borrowWithBLOBs.getBookingEndTime() != null && borrowWithBLOBs.getBookingEndTime() != 0) {
					modelAndView.addObject("bookingEndTime", GetDate.timestamptoStrYYYYMMDDHHMM(String.valueOf(borrowWithBLOBs.getBookingEndTime())));
				}
				modelAndView.addObject("addtime", GetDate.timestamptoStrYYYYMMDDHHMMSS(borrowWithBLOBs.getAddtime()));
				boolean borrowBailFlag = this.debtBorrowFirstService.getBorrowBail(borrowNid);
				modelAndView.addObject("borrowBailFlag", borrowBailFlag);
			}
		}

		LogUtil.endLog(DebtBorrowFirstController.class.toString(), DebtBorrowFirstDefine.FIRE_ACTION);
		return modelAndView;
	}

	/**
	 * 发标
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = DebtBorrowFirstDefine.FIRE_UPDATE_ACTION)
	@RequiresPermissions(DebtBorrowFirstDefine.PERMISSIONS_BORROW_FIRE)
	public ModelAndView fireUpdateAction(HttpServletRequest request, DebtBorrowFirstBean form) {
		LogUtil.startLog(DebtBorrowFirstController.class.toString(), DebtBorrowFirstDefine.FIRE_UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(DebtBorrowFirstDefine.FIRE_PATH);

		String borrowNid = form.getBorrowNid();
		String verifyStatus = form.getVerifyStatus();
		String ontime = form.getOntime();
		String bookingBeginTime = form.getBookingBeginTime();
		String bookingEndTime = form.getBookingEndTime();

		// 发标方式
		boolean verifyStatusFlag = ValidatorFieldCheckUtil.validateRequired(modelAndView, "verifyStatus", verifyStatus);
		ValidatorFieldCheckUtil.validateRequired(modelAndView, "borrowNid", borrowNid);
		if (verifyStatusFlag) {
			// 定时发标
			if (StringUtils.equals(verifyStatus, "1")) {
				verifyStatusFlag = ValidatorFieldCheckUtil.validateDateYYYMMDDHH24MI(modelAndView, "ontime", ontime, true);
				if (verifyStatusFlag) {
					String systeDate = GetDate.getServerDateTime(14, new Date());
					if (systeDate.compareTo(ontime) >= 0) {
						ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "ontime", "ontimeltsystemdate");
					}
					if (StringUtils.isNotEmpty(bookingBeginTime) || StringUtils.isNotEmpty(bookingEndTime)) {
						// 如果开始预约和截止预约时间中有一个时间有值,则进行以下验证
						// 截止预约时间必填
						verifyStatusFlag = ValidatorFieldCheckUtil.validateDateYYYMMDDHH24MI(modelAndView, "bookingEndTime", bookingEndTime, true);
						// 截止预约时间必须小于定时发标时间
						if (verifyStatusFlag && bookingEndTime.compareTo(ontime) >= 0) {
							ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "bookingEndTime", "bookingEndTimeltontime");
						}
						// 开始预约时间必填
						verifyStatusFlag = ValidatorFieldCheckUtil.validateDateYYYMMDDHH24MI(modelAndView, "bookingBeginTime", bookingBeginTime, true);
						// 开始预约时间必须小于截止预约时间
						if (verifyStatusFlag && bookingBeginTime.compareTo(bookingEndTime) >= 0) {
							ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "bookingEndTime", "bookingBeginTimeltbookingEndTime");
						}
						// 当前时间必须小于开始预约时间
						if (systeDate.compareTo(bookingBeginTime) >= 0) {
							ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "bookingBeginTime", "bookingBeginTimeltsystemdate");
						}
					}
				}
			}
		}

		if (StringUtils.isNotEmpty(borrowNid)) {
			DebtBorrowWithBLOBs debtBorrowWithBLOBs = debtBorrowCommonService.getBorrowWithBLOBs(borrowNid);
			if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
				if (debtBorrowWithBLOBs != null) {
					if (debtBorrowWithBLOBs.getBookingBeginTime() != null && debtBorrowWithBLOBs.getBookingBeginTime() != 0) {
						modelAndView.addObject("canUpdate", true);
					} else {
						modelAndView.addObject("canUpdate", false);
					}
					if (debtBorrowWithBLOBs.getBookingStatus() != null && debtBorrowWithBLOBs.getBookingStatus() != 0) {
						modelAndView.addObject("canUpdateTime", true);
					} else {
						modelAndView.addObject("canUpdateTime", false);
					}
					modelAndView.addObject("projectType", debtBorrowWithBLOBs.getProjectType());
					debtBorrowWithBLOBs.setVerifyStatus(Integer.parseInt(verifyStatus));
					modelAndView.addObject("fireInfo", debtBorrowWithBLOBs);
					modelAndView.addObject("addtime", GetDate.timestamptoStrYYYYMMDDHHMMSS(debtBorrowWithBLOBs.getAddtime()));
					boolean borrowBailFlag = this.debtBorrowFirstService.getBorrowBail(borrowNid);
					modelAndView.addObject("ontime", ontime);
					modelAndView.addObject("bookingBeginTime", bookingBeginTime);
					modelAndView.addObject("bookingEndTime", bookingEndTime);
					modelAndView.addObject("borrowBailFlag", borrowBailFlag);
					modelAndView.addObject(DebtBorrowFirstDefine.BORROW_FORM, form);
					return modelAndView;
				}
				modelAndView.addObject(DebtBorrowFirstDefine.BORROW_FORM, form);
				return modelAndView;
			}
		}

		if (StringUtils.isNotEmpty(borrowNid)) {
			this.debtBorrowFirstService.updateOntimeRecord(form);
		}

		modelAndView.addObject(DebtBorrowFirstDefine.SUCCESS, DebtBorrowFirstDefine.SUCCESS);
		modelAndView.addObject(DebtBorrowFirstDefine.BORROW_FORM, form);
		LogUtil.endLog(DebtBorrowFirstController.class.toString(), DebtBorrowFirstDefine.FIRE_UPDATE_ACTION);
		return modelAndView;

	}
}
