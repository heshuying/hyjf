package com.hyjf.admin.exception.bankrepayfreezeexception;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.exception.bankaccountcheck.BankAccountCheckDefine;
import com.hyjf.admin.exception.tendercancelexception.TenderCancelExceptionDefine;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BankRepayFreezeLog;

/**
 * 账户管理(银行)
 * 
 * @author liuyang
 *
 */
@Controller
@RequestMapping(value = BankRepayFreezeDefine.REQUEST_MAPPING)
public class BankRepayFreezeController extends BaseController {

	@Autowired
	private BankRepayFreezeService bankRepayFreezeService;

	/**
	 * 还款冻结资金撤销跳转页面 add by cwyang 2017/4/18
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BankRepayFreezeDefine.INIT_ACTION)
	@RequiresPermissions(BankRepayFreezeDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute(BankRepayFreezeDefine.BANKREPAY_FORM) BankRepayFreezeBean form) {
		ModelAndView modeAndView = new ModelAndView(BankRepayFreezeDefine.LIST_PATH);
		this.createPage(request, modeAndView, form);
		return modeAndView;
	}

	@RequestMapping(BankRepayFreezeDefine.SEARCH_ACTION)
	@RequiresPermissions(BankRepayFreezeDefine.PERMISSION_SEARCH)
	public ModelAndView search(HttpServletRequest request, @ModelAttribute(BankRepayFreezeDefine.BANKREPAY_FORM) BankRepayFreezeBean form) {
		ModelAndView modeAndView = new ModelAndView(BankRepayFreezeDefine.LIST_PATH);
		this.createPage(request, modeAndView, form);
		return modeAndView;
	}

	/**
	 * 分页
	 * 
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, BankRepayFreezeBean form) {

		Integer count = this.bankRepayFreezeService.selectCountRepayFreezeList();
		if (count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			form.setLimitStart(paginator.getOffset());
			form.setLimitEnd(paginator.getLimit());
			List<BankRepayFreezeLog> recordList = this.bankRepayFreezeService.selectBankFreezeList(form);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}
		modelAndView.addObject(BankAccountCheckDefine.ACCOUNTCHECK_FORM, form);
	}

	/**
	 * 开始还款冻结资金撤销 add by cwyang 2017/4/18
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = BankRepayFreezeDefine.REPAYUNFREEZE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(BankRepayFreezeDefine.PERMISSIONS_MODIFY)
	public String bankAccountCheckAction(HttpServletRequest request, @RequestBody BankRepayFreezeBean form) {
		JSONObject ret = new JSONObject();
		String orderId = form.getOrderId();
		if (StringUtils.isBlank(orderId)) {
			ret.put(TenderCancelExceptionDefine.JSON_STATUS_KEY, TenderCancelExceptionDefine.JSON_STATUS_NG);
			ret.put(TenderCancelExceptionDefine.JSON_RESULT_KEY, "参数错误，请稍后再试！");
			return ret.toString();
		}
		BankRepayFreezeLog repayFreezeFlog = this.bankRepayFreezeService.selectBankFreezeLog(orderId);
		if (Validator.isNull(repayFreezeFlog)) {
			ret.put(TenderCancelExceptionDefine.JSON_STATUS_KEY, TenderCancelExceptionDefine.JSON_STATUS_NG);
			ret.put(TenderCancelExceptionDefine.JSON_RESULT_KEY, "还款解冻失败，还款冻结可能已经解冻！");
			return ret.toString();
		}
		boolean repayUnFreezeFlag = this.bankRepayFreezeService.repayUnfreeze(repayFreezeFlog);
		if (repayUnFreezeFlag) {
			boolean unfreezeFlag = this.bankRepayFreezeService.updateBankRepayFreeze(repayFreezeFlog);
			if (unfreezeFlag) {
				ret.put(TenderCancelExceptionDefine.JSON_STATUS_KEY, TenderCancelExceptionDefine.JSON_STATUS_OK);
				ret.put(TenderCancelExceptionDefine.JSON_RESULT_KEY, "还款解冻成功!");
				return ret.toString();
			}
		}
		ret.put(TenderCancelExceptionDefine.JSON_STATUS_KEY, TenderCancelExceptionDefine.JSON_STATUS_NG);
		ret.put(TenderCancelExceptionDefine.JSON_RESULT_KEY, "还款解冻失败，请联系客服！");
		return ret.toString();
	}
}
