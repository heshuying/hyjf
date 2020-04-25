package com.hyjf.admin.exception.bankaccountcheck;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.finance.accountdetail.AccountDetailService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.AccountTrade;
import com.hyjf.mybatis.model.customize.admin.AdminBankAccountCheckCustomize;

/**
 * 异常中心银行对账控制层
 * @author yangchangwei
 *
 */
@Controller
@RequestMapping(value = BankAccountCheckDefine.REQUEST_MAPPING)
public class BankAccountCheckController extends BaseController{

	@Autowired
	private BankAccountCheckService bankAccountCheckService;
	@Autowired
	private AccountDetailService accountDetailService;
	/**
	 * 查询列表
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BankAccountCheckDefine.INIT_ACTION)
	@RequiresPermissions(BankAccountCheckDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute BankAccountCheckBean form) {
		LogUtil.startLog(BankAccountCheckDefine.THIS_CLASS, BankAccountCheckDefine.INIT_ACTION);
		ModelAndView modelAndView = new ModelAndView(BankAccountCheckDefine.BANKACCOUNTCHECK_PATH);
		
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BankAccountCheckDefine.THIS_CLASS, BankAccountCheckDefine.INIT_ACTION);
		return modelAndView;
	}

	/**
	 * 分页
	 * 
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modeAndView, BankAccountCheckBean form) {
		// 交易类型列表
		List<AccountTrade> trades = this.accountDetailService.selectTradeTypes();
		form.setTradeList(trades);
		AdminBankAccountCheckCustomize customize = new AdminBankAccountCheckCustomize();
		customize.setUserId(form.getUserId());
		customize.setUserName(form.getUserName());
		customize.setTradeTypeSearch(form.getTradeTypeSearch());
		customize.setnId(form.getnId());
		customize.setTypeSearch(form.getTypeSearch());
		customize.setCheckStatus(form.getCheckStatus());
		customize.setTradeStatus(form.getTradeStatus());
		customize.setAccountId(form.getAccountId());
		customize.setBankSeqNo(form.getBankSeqNo());
		customize.setStartDate(form.getStartDate());
		customize.setEndDate(form.getEndDate());
		
		Integer count = this.bankAccountCheckService.queryAccountCheckListCount(customize);
		if (count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			if (paginator.getOffset()<0) {
				customize.setLimitStart(0);
				customize.setLimitEnd(10);
			}else{
				customize.setLimitStart(paginator.getOffset());
				customize.setLimitEnd(paginator.getLimit());
			}
			List<AdminBankAccountCheckCustomize> resultList =  bankAccountCheckService.queryAccountCheckList(customize);
			form.setPaginator(paginator);
			form.setAccountCheckList(resultList);
			modeAndView.addObject(BankAccountCheckDefine.ACCOUNTCHECK_FORM, form);
		} else {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			customize.setLimitStart(paginator.getOffset());
			customize.setLimitEnd(paginator.getLimit());
			form.setPaginator(paginator);
			modeAndView.addObject(BankAccountCheckDefine.ACCOUNTCHECK_FORM, form);
		}

	}
}
