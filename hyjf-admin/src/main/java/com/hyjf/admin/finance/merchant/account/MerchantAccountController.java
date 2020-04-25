/**
 * Description:商户子账户转账
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */

package com.hyjf.admin.finance.merchant.account;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.MerchantAccount;
import com.hyjf.mybatis.model.customize.admin.AdminMerchantAccountSumCustomize;

@Controller
@RequestMapping(value = MerchantAccountDefine.REQUEST_MAPPING)
public class MerchantAccountController extends BaseController {

	@Autowired
	private MerchantAccountService accountService;

	/**
	 * 商户子账户列表
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(MerchantAccountDefine.ACCOUNT_LIST_ACTION)
	@RequiresPermissions(MerchantAccountDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,@ModelAttribute(MerchantAccountDefine.ACCOUNT_LIST_FORM) MerchantAccountListBean form) {
		
		LogUtil.startLog(MerchantAccountDefine.THIS_CLASS, MerchantAccountDefine.ACCOUNT_LIST_ACTION);
		ModelAndView modelAndView = new ModelAndView(MerchantAccountDefine.ACCOUNT_LIST_PATH);
		//更新商户子账户的金额信息
		boolean flag = this.accountService.updateMerchantAccount();
		if(flag){
			modelAndView.addObject("message", "商户子账户金额已经更新！");
		}else{
			modelAndView.addObject("message", "商户子账户金额更新失败！");
		}
		// 创建分页
		this.createPage(request, modelAndView, form);
		AdminMerchantAccountSumCustomize accoutSum = this.accountService.searchAccountSum();
		form.setAccountBalanceSum(accoutSum.getAccountBalanceSum());
		form.setAvailableBalanceSum(accoutSum.getAvailableBalanceSum());
		form.setFrostSum(accoutSum.getFrostSum());
		LogUtil.endLog(MerchantAccountDefine.THIS_CLASS, MerchantAccountDefine.ACCOUNT_LIST_ACTION);
		return modelAndView;
	}

	/**
	 * 转账列表分页
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, MerchantAccountListBean form) {

		int recordTotal = this.accountService.queryRecordTotal(form);
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
			List<MerchantAccount> recordList = this.accountService.selectRecordList(form, paginator.getOffset(),paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
		}
		modelAndView.addObject(MerchantAccountDefine.ACCOUNT_LIST_FORM, form);
	}

}
