/*
 * Copyright(c) 2012-2016 JD Pharma.Ltd. All Rights Reserved.
 * 
 * _ooOoo_
 * o8888888o
 * 88" . "88
 * (| -_- |)
 * O\ = /O
 * ____/`---'\____
 * .' \\| |// `.
 * / \\||| : |||// \
 * / _||||| -:- |||||- \
 * | | \\\ - /// | |
 * | \_| ''\---/'' | |
 * \ .-\__ `-` ___/-. /
 * ___`. .' /--.--\ `. . __
 * ."" '< `.___\_<|>_/___.' >'"".
 * | | : `- \`.;`\ _ /`;.`/ - ` : | |
 * \ \ `-. \_ __\ /__ _/ .-` / /
 * ======`-.____`-.___\_____/___.-`____.-'======
 * `=---='
 * ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 * 佛祖保佑 永无BUG
 */

package com.hyjf.web.plan.repay;

import java.math.BigDecimal;
import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.validator.ValidatorCheckUtil;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.DebtBorrow;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.web.BaseController;
import com.hyjf.web.user.repay.UserRepayDefine;
import com.hyjf.web.user.repay.UserRepayProjectBean;
import com.hyjf.web.user.repay.UserRepayService;
import com.hyjf.web.util.WebUtils;

/**
 * 用户前台还款实体类
 * 
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年10月9日
 * @see 上午9:45:41
 */
@Controller("planUserRepayController")
@RequestMapping(value = PlanUserRepayDefine.REQUEST_MAPPING)
public class PlanUserRepayContrllor extends BaseController {

	@Autowired
	private UserRepayService repayService;

	@Autowired
	private PlanUserRepayService planUserRepayService;

	/**
	 * 
	 * 用户还款详情页
	 * 
	 * @author renxingchen
	 * @param form
	 * @param request
	 * @return
	 * @throws ParseException
	 * @throws NumberFormatException
	 */
	@RequestMapping(value = PlanUserRepayDefine.REPAY_DETAIL_ACTION)
	public ModelAndView repayDetail(@ModelAttribute UserRepayProjectBean form, HttpServletRequest request)
			throws NumberFormatException, ParseException {
		ModelAndView modelAndView = new ModelAndView(PlanUserRepayDefine.REPAY_DETAIL_HTJ_PAGE);
		// 用户ID
		Integer userId = WebUtils.getUserId(request);
		form.setUserId(userId.toString());
		// 查询用户的出借详情
		UserRepayProjectBean repayProject = this.planUserRepayService.searchRepayProjectDetail(form);
		modelAndView.addObject("repayProject", repayProject);
		return modelAndView;
	}

	/**
	 * 
	 * 用户前台还款汇添金专属项目
	 * 
	 * @author renxingchen
	 * @param request
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = PlanUserRepayDefine.REPAY_ACTION)
	public ModelAndView repay(HttpServletRequest request) throws ParseException {
		ModelAndView modelAndView = new ModelAndView(UserRepayDefine.REPAY_ERROR_PAGE);
		JSONObject info = new JSONObject();
		Integer userId = WebUtils.getUserId(request);
		String password = request.getParameter("password");
		String borrowNid = request.getParameter("borrowNid");
		modelAndView.addObject("borrowNid", borrowNid);
		// 校验用户的还款
		DebtRepayByTermBean repay = this.validatorFieldCheckRepay(info, userId, password, borrowNid);
		if (!ValidatorCheckUtil.hasValidateError(info)) {// 如果校验成功
			String ip = GetCilentIP.getIpAddr(request);
			repay.setIp(ip);
			DebtBorrow borrow = this.planUserRepayService.searchDebtBorrowProject(userId, borrowNid);
			try {
				boolean flag = this.planUserRepayService.updateRepayMoney(repay);
				if (flag) {
					modelAndView.addObject("borrowName", borrow.getName());
					modelAndView.setViewName(UserRepayDefine.REPAY_SUCCESS_PAGE);
					return modelAndView;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			modelAndView.addObject("message", "还款失败，请稍后再试！");
			return modelAndView;
		} else {// 如果校验失败
			modelAndView.addObject("message", info.getString(UserRepayDefine.REPAY_ERROR));
			return modelAndView;
		}
	}

	/**
	 * 
	 * 校验用户的还款信息
	 * 
	 * @author renxingchen
	 * @param info
	 * @param userId
	 * @param password
	 * @param borrowNid
	 * @throws ParseException
	 */
	private DebtRepayByTermBean validatorFieldCheckRepay(JSONObject info, Integer userId, String password,
			String borrowNid) throws ParseException {
		// 获取当前用户
		Users user = this.repayService.searchRepayUser(userId);
		// 检查用户是否存在
		if (user != null) {
			String sort = user.getSalt();
			String mdPassword = MD5.toMD5Code(password + sort);
			// 检查用户输入的密码信息同当前的用户密码信息是否对应
			if (mdPassword.equals(user.getPassword())) {
				// 获取用户的账户余额信息
				Account account = this.repayService.searchRepayUserAccount(userId);
				// 查询用户的账户余额信息
				if (account != null) {
					// 获取用户的可用余额
					BigDecimal balance = account.getBalance();
					// 获取当前的用户还款的项目
					DebtBorrow borrow = this.planUserRepayService.searchDebtBorrowProject(userId, borrowNid);
					// 判断用户当前还款的项目是否存在
					if (borrow != null) {
						// 获取项目还款方式
						String borrowStyle = StringUtils.isNotEmpty(borrow.getBorrowStyle()) ? borrow.getBorrowStyle()
								: null;
						if (StringUtils.isNotEmpty(borrowStyle)) {
							// 获取借款用户的实际还款信息
							DebtRepayByTermBean repay = this.planUserRepayService.calculateRepay(userId, borrow);
							BigDecimal repayTotal = repay.getRepayTotal();
							if (repayTotal.compareTo(balance) == 0 || repayTotal.compareTo(balance) == -1) {// 如果用户的账户可用的余额大于等于应还总额
								// 获取用户在汇付的余额
								AccountChinapnr accountChinapnr = this.repayService.getChinapnrUserInfo(userId);
								BigDecimal userBalance = this.repayService
										.getUserBalance(accountChinapnr.getChinapnrUsrcustid());
								if (repayTotal.compareTo(userBalance) == 0 || repayTotal.compareTo(userBalance) == -1) {// 如果用户在汇付的余额大于等于应还总额
									return repay;
								} else {
									ValidatorCheckUtil.validateSpecialError(info, UserRepayDefine.REPAY_ERROR,
											UserRepayDefine.REPAY_ERROR10);
								}
							} else {
								ValidatorCheckUtil.validateSpecialError(info, UserRepayDefine.REPAY_ERROR,
										UserRepayDefine.REPAY_ERROR8);
							}
						} else {
							ValidatorCheckUtil.validateSpecialError(info, UserRepayDefine.REPAY_ERROR,
									UserRepayDefine.REPAY_ERROR5);
						}
					} else {
						ValidatorCheckUtil.validateSpecialError(info, UserRepayDefine.REPAY_ERROR,
								UserRepayDefine.REPAY_ERROR4);
					}
				} else {
					ValidatorCheckUtil.validateSpecialError(info, UserRepayDefine.REPAY_ERROR,
							UserRepayDefine.REPAY_ERROR3);
				}
			} else {
				ValidatorCheckUtil.validateSpecialError(info, UserRepayDefine.REPAY_ERROR,
						UserRepayDefine.REPAY_ERROR2);
			}
		} else {
			ValidatorCheckUtil.validateSpecialError(info, UserRepayDefine.REPAY_ERROR, UserRepayDefine.REPAY_ERROR1);
		}
		return null;
	}
}
