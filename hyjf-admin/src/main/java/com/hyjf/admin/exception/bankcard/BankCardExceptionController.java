package com.hyjf.admin.exception.bankcard;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.config.bankconfig.BankConfigService;
import com.hyjf.admin.manager.content.article.ContentArticleDefine;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.TimeCountTool;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.BankConfig;
import com.hyjf.mybatis.model.customize.admin.AdminBankCardExceptionCustomize;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;

/**
 * @package com.hyjf.admin.maintenance.Admin
 * @author GOGTZ-T
 * @date 2015/11/29 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = BankCardExceptionDefine.REQUEST_MAPPING)
public class BankCardExceptionController extends BaseController {
	@Autowired
	private BankConfigService bankConfigService;

	@Autowired
	private BankCardExceptionService bankCardService;

	/**
	 * 查询列表
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BankCardExceptionDefine.INIT_ACTION)
	@RequiresPermissions(BankCardExceptionDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute BankCardExceptionBean form) {
		LogUtil.startLog(BankCardExceptionDefine.THIS_CLASS, BankCardExceptionDefine.INIT_ACTION);
		ModelAndView modelAndView = new ModelAndView(BankCardExceptionDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BankCardExceptionDefine.THIS_CLASS, BankCardExceptionDefine.INIT_ACTION);
		return modelAndView;
	}

	/**
	 * 查询列表
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BankCardExceptionDefine.SEARCH_ACTION)
	@RequiresPermissions(BankCardExceptionDefine.PERMISSIONS_VIEW)
	public ModelAndView list(HttpServletRequest request, @ModelAttribute BankCardExceptionBean form) {
		LogUtil.startLog(BankCardExceptionDefine.THIS_CLASS, BankCardExceptionDefine.INIT_ACTION);
		ModelAndView modelAndView = new ModelAndView(BankCardExceptionDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BankCardExceptionDefine.THIS_CLASS, BankCardExceptionDefine.INIT_ACTION);
		return modelAndView;
	}

	/**
	 * 创建账户设置分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, BankCardExceptionBean form) {
		// 银行卡列表
		List<BankConfig> bankConfigList = this.bankConfigService.getRecordList(new BankConfig(), -1, -1);
		modelAndView.addObject("bankConfigList", bankConfigList);
		AdminBankCardExceptionCustomize bankCardExceptionCustomize = new AdminBankCardExceptionCustomize();
		BeanUtils.copyProperties(form, bankCardExceptionCustomize);
		// 查询个数
		int recordTotal = this.bankCardService.queryAccountBankCount(bankCardExceptionCustomize);
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
			List<AdminBankCardExceptionCustomize> recordList = this.bankCardService
					.queryAccountBankList(bankCardExceptionCustomize, paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}
		modelAndView.addObject(BankCardExceptionDefine.BANKCARDEXCEPTION_FORM, form);
	}

	/**
	 * 更新某个开户用户的银行卡信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(BankCardExceptionDefine.UPDATE_BANKCARDEXCEPTION_ACTION)
	@RequiresPermissions(BankCardExceptionDefine.PERMISSION_MODIFY)
	public Map<String, Object> updateBankCardExceptionAction(HttpServletRequest request,
			@ModelAttribute BankCardExceptionBean form) {
		LogUtil.startLog(BankCardExceptionDefine.THIS_CLASS, BankCardExceptionDefine.INIT_ACTION);
		ModelAndView modelAndView = new ModelAndView(BankCardExceptionDefine.LIST_PATH);
		// 验证
		if (Validator.isNull(form.getUserId())) {
			modelAndView.getModel().put(ContentArticleDefine.SUCCESS, false);
			modelAndView.getModel().put(ContentArticleDefine.JSON_VALID_INFO_KEY, "空的用户ID");
		}
		if (!Validator.isNumber(form.getUserId())) {
			modelAndView.getModel().put(ContentArticleDefine.SUCCESS, false);
			modelAndView.getModel().put(ContentArticleDefine.JSON_VALID_INFO_KEY, "无效的用户ID");
		}
		// 更新
		String result = this.bankCardService.updateAccountBankByUserId(Integer.parseInt(form.getUserId()));
		if (result.equals(ChinaPnrConstant.RESPCODE_SUCCESS)) {
			// 跳转页面用（info里面有）
			modelAndView.getModel().put(ContentArticleDefine.SUCCESS, true);
		} else {
			modelAndView.getModel().put(ContentArticleDefine.SUCCESS, false);
			modelAndView.getModel().put(ContentArticleDefine.JSON_VALID_INFO_KEY, result);
		}
		LogUtil.endLog(BankCardExceptionDefine.THIS_CLASS, BankCardExceptionDefine.INIT_ACTION);
		return modelAndView.getModel();
	}

	// 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
	private static Boolean isOver = true;
	private static Integer BANKCARDINDEX = 0;
	private static Integer BANKCARDSIZE = 0;

	/**
	 * 更新所有开户用户的银行卡信息,此方法暂不开放
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	// 注意:此方法暂不开放此方法暂不开放此方法暂不开放此方法暂不开放
	@ResponseBody
	@RequestMapping(BankCardExceptionDefine.UPDAYE_ALLBANKCARDEXCEPTION_ACTION)
	@RequiresPermissions(BankCardExceptionDefine.PERMISSION_MODIFYALL)
	public Map<String, Object> updateAllBankCardExceptionAction(HttpServletRequest request,
			@ModelAttribute BankCardExceptionBean form) {
		LogUtil.startLog(BankCardExceptionDefine.THIS_CLASS, BankCardExceptionDefine.INIT_ACTION);
		ModelAndView modelAndView = new ModelAndView(BankCardExceptionDefine.LIST_PATH);
		if (isOver) {
			isOver = false;
			{
				TimeCountTool timeCountTool = new TimeCountTool();
				timeCountTool.printBeginDate();
				List<AccountChinapnr> accountChinapnrList = bankCardService.queryAllAccountBankCount();
				timeCountTool.printYongshi("查询AccountChinapnr列表");
				int size = accountChinapnrList.size();
				BANKCARDSIZE=size;
				for (int i = 0; i < size; i++) {
					BANKCARDINDEX=i;
					AccountChinapnr accountChinapnr = accountChinapnrList.get(i);
					bankCardService.updateAccountBankByUserId(accountChinapnr.getUserId());
					// timeCountTool.printYongshi("更新第" + (i + 1) +
					// "个用户的银行卡信息,用户ID:" +
					// accountChinapnr.getUserId() + ",汇付号:"
					// + accountChinapnr.getChinapnrUsrcustid() + ",共" + size +
					// "个");
				}
				timeCountTool.printZongYongshi();
			}
			isOver = true;
			modelAndView.getModel().put(ContentArticleDefine.SUCCESS, true);
			modelAndView.getModel().put(ContentArticleDefine.JSON_VALID_INFO_KEY, "操作成功");
		} else {
			modelAndView.getModel().put(ContentArticleDefine.SUCCESS, false);
			modelAndView.getModel().put(ContentArticleDefine.JSON_VALID_INFO_KEY, "正在进行处理,进度("+(BANKCARDINDEX+1)+"/"+BANKCARDSIZE+"),请勿重复操作");
		}
		LogUtil.endLog(BankCardExceptionDefine.THIS_CLASS, BankCardExceptionDefine.INIT_ACTION);
		return modelAndView.getModel();
	}
}
