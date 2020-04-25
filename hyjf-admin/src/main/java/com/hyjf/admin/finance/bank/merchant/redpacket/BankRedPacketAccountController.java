/**
 * 江西银行商户子账户
 */
package com.hyjf.admin.finance.bank.merchant.redpacket;

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
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.admin.BankMerchantAccountListCustomize;
import com.hyjf.pay.lib.bank.util.BankCallConstant;

@Controller
@RequestMapping(value = BankRedPacketAccountDefine.REQUEST_MAPPING)
public class BankRedPacketAccountController extends BaseController {

	@Autowired
	private BankRedPacketAccountService bankMerchantAccountService;

	/**
	 * 商户子账户列表
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BankRedPacketAccountDefine.INIT)
	@RequiresPermissions(BankRedPacketAccountDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,@ModelAttribute(BankRedPacketAccountDefine.ACCOUNT_LIST_FORM) BankRedPacketAccounttListBean form) {
		
		LogUtil.startLog(BankRedPacketAccountDefine.THIS_CLASS, BankRedPacketAccountDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(BankRedPacketAccountDefine.ACCOUNT_LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BankRedPacketAccountDefine.THIS_CLASS, BankRedPacketAccountDefine.INIT);
		return modelAndView;
	}

	
	/**
	 * 转账列表分页
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, BankRedPacketAccounttListBean form) {
	    /** 服务费子账户号 */
//	    String bankAccountCode=PropUtils.getSystem(BankCallConstant.BANK_MERS_ACCOUNT);
	    /** 红包子账户号 */
	    String bankAccountCode=PropUtils.getSystem(BankCallConstant.BANK_MERRP_ACCOUNT);
	    form.setBankAccountCode(bankAccountCode);
	    // 收支类型
        List<ParamName> bankMerType = this.bankMerchantAccountService.getParamNameList("BANK_MER_TYPE");
        modelAndView.addObject("bankMerType", bankMerType);
        // 交易类型
        List<ParamName> transTypes = this.bankMerchantAccountService.getParamNameList("BANK_MER_TRANS_TYPE");
        modelAndView.addObject("transTypes", transTypes);
        // 交易状态
        List<ParamName> transStatus = this.bankMerchantAccountService.getParamNameList("BANK_MER_TRANS_STATUS");
        modelAndView.addObject("transStatus", transStatus);
		int recordTotal = this.bankMerchantAccountService.queryRecordTotal(form);
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
			form.setLimitStart(paginator.getOffset());
			form.setLimitEnd(paginator.getLimit());
			List<BankMerchantAccountListCustomize> recordList = this.bankMerchantAccountService.selectRecordList(form);
			//返回页面
			form.setPaginator(paginator);
			form.setRecordList(recordList);
		}
		modelAndView.addObject(BankRedPacketAccountDefine.ACCOUNT_LIST_FORM, form);
	}
    
}
