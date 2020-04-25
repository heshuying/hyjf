/**
 * 江西银行商户子账户
 */
package com.hyjf.admin.finance.bank.merchant.poundage;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.admin.BankMerchantAccountListCustomize;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = BankPoundageAccountDefine.REQUEST_MAPPING)
public class BankPoundageAccountController extends BaseController {

	@Autowired
	private BankPoundageAccountService  bankPoundageAccountService;

	/**
	 * 商户子账户列表
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BankPoundageAccountDefine.INIT)
	@RequiresPermissions(BankPoundageAccountDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,@ModelAttribute(BankPoundageAccountDefine.ACCOUNT_LIST_FORM) BankPoundageAccountListBean form) {
		
		LogUtil.startLog(BankPoundageAccountDefine.THIS_CLASS, BankPoundageAccountDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(BankPoundageAccountDefine.ACCOUNT_LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BankPoundageAccountDefine.THIS_CLASS, BankPoundageAccountDefine.INIT);
		return modelAndView;
	}

	
	/**
	 * 转账列表分页
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, BankPoundageAccountListBean form) {
	    /** 服务费子账户号 */
	    String bankAccountCode=PropUtils.getSystem(BankCallConstant.BANK_MERS_ACCOUNT);
	    /** 红包子账户号 */
//	    String bankAccountCode=PropUtils.getSystem(BankCallConstant.BANK_MERRP_ACCOUNT);
	    form.setBankAccountCode(bankAccountCode);
	    // 收支类型
        List<ParamName> bankMerType = this.bankPoundageAccountService.getParamNameList("BANK_MER_TYPE");
        modelAndView.addObject("bankMerType", bankMerType);
        // 交易类型
        List<ParamName> transTypes = this.bankPoundageAccountService.getParamNameList("BANK_MER_TRANS_TYPE");
        modelAndView.addObject("transTypes", transTypes);
        // 交易状态
        List<ParamName> transStatus = this.bankPoundageAccountService.getParamNameList("BANK_MER_TRANS_STATUS");
        modelAndView.addObject("transStatus", transStatus);
		int recordTotal = this.bankPoundageAccountService.queryRecordTotal(form);
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
			form.setLimitStart(paginator.getOffset());
			form.setLimitEnd(paginator.getLimit());
			List<BankMerchantAccountListCustomize> recordList = this.bankPoundageAccountService.selectRecordList(form);
			//返回页面
			form.setPaginator(paginator);
			form.setRecordList(recordList);
		}
		modelAndView.addObject(BankPoundageAccountDefine.ACCOUNT_LIST_FORM, form);
	}
    
}
