/**
 * Description：用户交易明细控制器
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.web.bank.web.user.trade;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.user.trade.RechargeListBankBean;
import com.hyjf.bank.service.user.trade.TradeDetailBankService;
import com.hyjf.bank.service.user.trade.TradeListBankBean;
import com.hyjf.bank.service.user.trade.WithdrawListBankBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.AccountTrade;
import com.hyjf.mybatis.model.customize.web.WebUserRechargeListCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserTradeListCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserWithdrawListCustomize;
import com.hyjf.web.BaseController;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.util.WebUtils;

@Controller("tradeDetailBankController")
@RequestMapping(value = TradeDetailBankDefine.REQUEST_MAPPING)
public class TradeDetailBankController extends BaseController {

	@Autowired
	private TradeDetailBankService tradeListService;

	/**
	 * 初始化项目列表画面
	 * 
	 * @param request
	 * @param responsef
	 * @return
	 */
	@RequestMapping(value = TradeDetailBankDefine.INIT_TRADE_LIST_ACTION)
	public ModelAndView initHztList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView(TradeDetailBankDefine.TRADE_LIST_PTAH);
		// 交易类型  下拉列表
		List<AccountTrade> trades = this.tradeListService.selectTradeTypes();
		modelAndView.addObject("trades", trades);
		WebViewUser user = WebUtils.getUser(request);
		if (user == null) {
			modelAndView.addObject("message", "登录超时，请重新登录！");
			return modelAndView;
		}
		// 用户角色1出借人2借款人3担保机构
		String roleId = user.getRoleId();
		modelAndView.addObject("roleId", roleId);
		return modelAndView;
	}

	/**
	 * 用户收支明细
	 * 
	 * @param trade
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = TradeDetailBankDefine.TRADE_LIST_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	
	public TradeListBankBean searchUserTradeList(@ModelAttribute TradeListBankBean trade, HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(TradeDetailBankDefine.THIS_CLASS, TradeDetailBankDefine.TRADE_LIST_ACTION);
		Integer userId = WebUtils.getUserId(request);
		// ShiroUtil.getLoginUserId(request);
		// Integer userId = 500180;
		trade.setUserId(userId.toString());
		this.createUserTradeListPage(request, trade);
		trade.setListType("trade");
		trade.success();
		LogUtil.endLog(TradeDetailBankDefine.THIS_CLASS, TradeDetailBankDefine.TRADE_LIST_ACTION);
		return trade;
	}

	/**
	 * 创建用户收支明细列表分页数据
	 * 
	 * @param request
	 * @param info
	 * @param form
	 */
	private void createUserTradeListPage(HttpServletRequest request, TradeListBankBean form) {
		// 统计相应的用户出借项目总数
	    AccountChinapnr accountChinapnr = tradeListService.getAccountChinapnr(Integer.parseInt(form.getUserId()));
	    if (form.getStartDate() != null && !"".equals(form.getStartDate().trim())) {
	    	form.setStartDate(GetDate.getDayStart(form.getStartDate()));
	    }
	    if(form.getEndDate()!= null && !"".equals(form.getEndDate().trim())){
	    	form.setEndDate(GetDate.getDayEnd(form.getEndDate()));
	    }
		int recordTotal = this.tradeListService.countUserTradeRecordTotal(form);
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
			// 查询相应的用户出借项目列表
			List<WebUserTradeListCustomize> userTrades = tradeListService.searchUserTradeList(form,
					paginator.getOffset(), paginator.getLimit());
			form.setIsChinapnrOpen(accountChinapnr==null?"false" : "true");//是否开通汇付天下
			form.setTradeList(userTrades);
			form.setPaginator(paginator);
		} else {
		    form.setIsChinapnrOpen(accountChinapnr==null?"false" : "true");
			form.setTradeList(new ArrayList<WebUserTradeListCustomize>());
			form.setPaginator(new Paginator(form.getPaginatorPage(), 0));
		}
	}

	/**
	 * 用户注册初始化画面数据保存（保存到session）
	 */
	@ResponseBody
	@RequestMapping(value = TradeDetailBankDefine.TRADE_TYPES_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String searchTradeTypes(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(TradeDetailBankDefine.THIS_CLASS, TradeDetailBankDefine.TRADE_TYPES_ACTION);
		JSONObject ret = new JSONObject();
		JSONObject info = new JSONObject();
		List<AccountTrade> tradeTypes = tradeListService.searchTradeTypes();
		info.put("tradeTypes", tradeTypes);
		ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
		ret.put(CustomConstants.DATA, info);
		ret.put(CustomConstants.MSG, "");
		LogUtil.endLog(TradeDetailBankDefine.THIS_CLASS, TradeDetailBankDefine.TRADE_TYPES_ACTION);
		return JSONObject.toJSONString(ret, true);

	}

	/**
	 * 用户充值记录
	 * 
	 * @param recharge
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = TradeDetailBankDefine.RECHARGE_LIST_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public RechargeListBankBean searchUserRechargeList(@ModelAttribute RechargeListBankBean recharge,
			HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(TradeDetailBankDefine.THIS_CLASS, TradeDetailBankDefine.TRADE_LIST_ACTION);
		// 用户ID
		Integer userId = WebUtils.getUserId(request);
		// ShiroUtil.getLoginUserId(request);
		// Integer userId = 500180;
		recharge.setUserId(userId.toString());
		this.createUserRechargeListPage(request, recharge);
		recharge.setListType("recharge");
		recharge.success();
		LogUtil.endLog(TradeDetailBankDefine.THIS_CLASS, TradeDetailBankDefine.TRADE_LIST_ACTION);
		return recharge;
	}

	/**
	 * 创建用户充值记录分页数据
	 * 
	 * @param request
	 * @param info
	 * @param form
	 */
	private void createUserRechargeListPage(HttpServletRequest request, RechargeListBankBean form) {
	    if (form.getStartDate() != null && !"".equals(form.getStartDate().trim())) {
	    	form.setStartDate(GetDate.getDayStart(form.getStartDate()));
	    }
	    if(form.getEndDate()!= null && !"".equals(form.getEndDate().trim())){
	    	form.setEndDate(GetDate.getDayStart(form.getEndDate()));
	    }
		// 统计相应的用户出借项目总数
		int recordTotal = this.tradeListService.countUserRechargeRecordTotal(form);
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
			// 查询相应的用户出借项目列表
			List<WebUserRechargeListCustomize> rechargeList = tradeListService.searchUserRechargeList(form,
					paginator.getOffset(), paginator.getLimit());
			form.setRechargeList(rechargeList);
			form.setPaginator(paginator);
		} else {
			form.setRechargeList(new ArrayList<WebUserRechargeListCustomize>());
			form.setPaginator(new Paginator(form.getPaginatorPage(), 0));
		}
	}

	/**
	 * 用户取现记录列表查询
	 * 
	 * @param withdraw
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = TradeDetailBankDefine.WITHDRAW_LIST_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public WithdrawListBankBean searchUserWithdrawList(@ModelAttribute WithdrawListBankBean withdraw,
			HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(TradeDetailBankDefine.THIS_CLASS, TradeDetailBankDefine.TRADE_LIST_ACTION);
		// 用户ID
		Integer userId = WebUtils.getUserId(request);
		// ShiroUtil.getLoginUserId(request);
		// Integer userId = 500252;
		withdraw.setUserId(userId.toString());
		this.createUserWithdrawListPage(request, withdraw);
		withdraw.setListType("withdraw");
		withdraw.success();
		LogUtil.endLog(TradeDetailBankDefine.THIS_CLASS, TradeDetailBankDefine.TRADE_LIST_ACTION);
		return withdraw;
	}

	/**
	 * 创建用户取现记录分页数据
	 * 
	 * @param request
	 * @param info
	 * @param form
	 */
	private void createUserWithdrawListPage(HttpServletRequest request, WithdrawListBankBean form) {
	    if (form.getStartDate() != null && !"".equals(form.getStartDate().trim())) {
	    	form.setStartDate(GetDate.getDayStart(form.getStartDate()));
	    }
	    if(form.getEndDate()!= null && !"".equals(form.getEndDate().trim())){
	    	form.setEndDate(GetDate.getDayStart(form.getEndDate()));
	    }
		// 统计相应的用户出借项目总数
		int recordTotal = this.tradeListService.countUserWithdrawRecordTotal(form);
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
			// 查询相应的用户出借项目列表
			List<WebUserWithdrawListCustomize> withdrawList = tradeListService.searchUserWithdrawList(form,
					paginator.getOffset(), paginator.getLimit());
			form.setWithdrawList(withdrawList);
			form.setPaginator(paginator);
		} else {
			form.setWithdrawList(new ArrayList<WebUserWithdrawListCustomize>());
			form.setPaginator(new Paginator(form.getPaginatorPage(), 0));
		}
	}

}
