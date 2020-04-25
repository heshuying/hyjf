package com.hyjf.admin.exception.hjhcreditendexception;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.borrow.credit.HjhDebtCreditBean;
import com.hyjf.bank.service.hjh.borrow.tender.BankAutoTenderService;
import com.hyjf.bank.service.user.credit.CreditService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.HjhDebtCredit;
import com.hyjf.mybatis.model.customize.admin.HjhDebtCreditCustomize;


/**
 * 汇计划债转结束债权异常处理Controller
 * 
 * @ClassName HjhCreditEndExceptionController
 * @author liubin
 * @date 2018年1月15日 上午9:45:20
 */
@Controller
@RequestMapping(value = HjhCreditEndExceptionDefine.REQUEST_MAPPING)
public class HjhCreditEndExceptionController extends BaseController {
	
	@Autowired
	private HjhCreditEndExceptionService hjhCreditEndExceptionService;

	@Autowired
	private BankAutoTenderService bankAutoTenderService;
	
	@Autowired
	private CreditService creditService;
	
	//类名 
	private static final String THIS_CLASS = HjhCreditEndExceptionController.class.toString();
	
	Logger _log = LoggerFactory.getLogger(HjhCreditEndExceptionController.class);
	
	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(HjhCreditEndExceptionDefine.INIT)
	@RequiresPermissions(HjhCreditEndExceptionDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, HjhDebtCreditBean form) {
		LogUtil.startLog(THIS_CLASS, HjhCreditEndExceptionDefine.INIT);
		ModelAndView modeAndView = new ModelAndView(HjhCreditEndExceptionDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(THIS_CLASS, HjhCreditEndExceptionDefine.INIT);
		return modeAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(HjhCreditEndExceptionDefine.SEARCH_ACTION)
	@RequiresPermissions(HjhCreditEndExceptionDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, HttpServletResponse response, HjhDebtCreditBean form) {
		LogUtil.startLog(THIS_CLASS, HjhCreditEndExceptionDefine.SEARCH_ACTION);
		ModelAndView modeAndView = new ModelAndView(HjhCreditEndExceptionDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(THIS_CLASS, HjhCreditEndExceptionDefine.SEARCH_ACTION);
		return modeAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, HjhDebtCreditBean planCreditBean) {

		// 还款方式
		List<BorrowStyle> borrowStyleList = this.hjhCreditEndExceptionService.selectBorrowStyleList("");
		modelAndView.addObject("borrowStyleList", borrowStyleList);
		// 转让状态
		modelAndView.addObject("creditStatusList", this.hjhCreditEndExceptionService.getParamNameList(CustomConstants.HJH_DEBT_CREDIT_STATUS));
		// 债转还款状态
		modelAndView.addObject("repayStatusList", this.hjhCreditEndExceptionService.getParamNameList(CustomConstants.HJH_DEBT_REPAY_STATUS));
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("planNid", planCreditBean.getPlanNid());
		params.put("planOrderId", planCreditBean.getPlanOrderId());
		params.put("userName", planCreditBean.getUserName());
		params.put("creditNid", planCreditBean.getCreditNid());
		params.put("borrowNid", planCreditBean.getBorrowNid());
		params.put("repayStyle", planCreditBean.getRepayStyle());
		params.put("liquidatesTimeStart", StringUtils.isNotBlank(planCreditBean.getLiquidatesTimeStart())?planCreditBean.getLiquidatesTimeStart():null);
		params.put("liquidatesTimeEnd", StringUtils.isNotBlank(planCreditBean.getLiquidatesTimeEnd())?planCreditBean.getLiquidatesTimeEnd():null);
		params.put("repayNextTimeStart",StringUtils.isNotBlank(planCreditBean.getRepayNextTimeStart())?planCreditBean.getRepayNextTimeStart():null);
		params.put("repayNextTimeEnd", StringUtils.isNotBlank(planCreditBean.getRepayNextTimeEnd())?planCreditBean.getRepayNextTimeEnd():null);
		params.put("creditStatus", "1");
		params.put("creditCapitalWait", "0");
		Integer count = this.hjhCreditEndExceptionService.countDebtCredit(params);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(planCreditBean.getPaginatorPage(), count);
			params.put("limitStart", paginator.getOffset());
			params.put("limitEnd", paginator.getLimit());
			List<HjhDebtCreditCustomize> recordList = this.hjhCreditEndExceptionService.selectDebtCreditList(params);
			planCreditBean.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}
		modelAndView.addObject(HjhCreditEndExceptionDefine.JOINDETAIL_FORM, planCreditBean);
	}
		
	/**
	 * 结束债权
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = HjhCreditEndExceptionDefine.MODIFY_ACTION, produces = "application/json; charset=UTF-8")
	@RequiresPermissions(HjhCreditEndExceptionDefine.PERMISSIONS_MODIFY)
	public String updateDebtEndAction(HttpServletRequest request, @ModelAttribute HjhDebtCreditBean form) {

		LogUtil.startLog(THIS_CLASS, HjhCreditEndExceptionDefine.MODIFY_ACTION);
		JSONObject ret = new JSONObject();
		String creditNid = form.getCreditNid();
		
		//取得债转信息
		HjhDebtCredit credit = this.bankAutoTenderService.selectCreditByNid(creditNid);
		
		//获取出让用户的江西银行电子账号
		BankOpenAccount sellerBankOpenAccount = this.bankAutoTenderService.getBankOpenAccount(credit.getUserId());
		if (sellerBankOpenAccount == null) {
			ret.put(HjhCreditEndExceptionDefine.JSON_STATUS_KEY, HjhCreditEndExceptionDefine.JSON_STATUS_NG);
			ret.put(HjhCreditEndExceptionDefine.JSON_RESULT_KEY, "未取得出让人的银行账号。");
			return ret.toString();
		}
		
		String sellerUsrcustid = sellerBankOpenAccount.getAccount();//出让用户的江西银行电子账号
		
		if (credit.getCreditCapitalWait().compareTo(BigDecimal.ZERO) == 0) {
			//获取出让人投标成功的授权号
			String sellerAuthCode = this.bankAutoTenderService.getSellerAuthCode(credit.getSellOrderId(), credit.getSourceType());
			if (sellerAuthCode == null) {
				ret.put(HjhCreditEndExceptionDefine.JSON_STATUS_KEY, HjhCreditEndExceptionDefine.JSON_STATUS_NG);
				ret.put(HjhCreditEndExceptionDefine.JSON_RESULT_KEY, "未取得出让人出借的授权码。");
				return ret.toString();
			}
			
			//调用银行结束债权接口
			boolean result = false;
			try {
				result = this.creditService.requestDebtEnd(credit, sellerUsrcustid, sellerAuthCode);
				if (!result) {
					_log.error("债转编号[" + creditNid + "]银行结束债权失败。");
					ret.put(HjhCreditEndExceptionDefine.JSON_STATUS_KEY, HjhCreditEndExceptionDefine.JSON_STATUS_NG);
					ret.put(HjhCreditEndExceptionDefine.JSON_RESULT_KEY, "该债权结束失败。");
					return ret.toString();
				}
			} catch (Exception e) {
				_log.error("债转编号[" + creditNid + "]银行结束债权异常。", e);
			}

			_log.info("债转编号[" + creditNid + "]银行结束债权成功。");
			
			//银行结束债权后，更新债权表为完全承接
			result = this.bankAutoTenderService.updateCreditForEnd(credit);
			if (!result) {
				_log.error("债转编号[" + creditNid + "]银行结束债权后，更新债权表为完全承接失败。");
				ret.put(HjhCreditEndExceptionDefine.JSON_STATUS_KEY, HjhCreditEndExceptionDefine.JSON_STATUS_NG);
				ret.put(HjhCreditEndExceptionDefine.JSON_RESULT_KEY, "银行结束债权成功，更新债权表失败。");
				return ret.toString();
			}
		}
		
		ret.put(HjhCreditEndExceptionDefine.JSON_STATUS_KEY, HjhCreditEndExceptionDefine.JSON_STATUS_OK);
		ret.put(HjhCreditEndExceptionDefine.JSON_RESULT_KEY, "债权结束成功");
		LogUtil.endLog(THIS_CLASS, HjhCreditEndExceptionDefine.MODIFY_ACTION);
		return ret.toJSONString();
	}
}
