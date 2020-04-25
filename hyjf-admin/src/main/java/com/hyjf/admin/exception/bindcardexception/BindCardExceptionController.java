package com.hyjf.admin.exception.bindcardexception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.finance.recharge.RechargeDefine;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.BindCardExceptionCustomize;

/**
 * 江西银行绑卡掉单处理Controller
 * 
 * @author liuyang
 *
 */
@Controller
@RequestMapping(BindCardExceptionDefine.REQUEST_MAPPING)
public class BindCardExceptionController extends BaseController {

	@Autowired
	private BindCardExceptionService bindCardExceptionService;
	/** 类名 */
	private String THIS_CLASS = BindCardExceptionController.class.toString();

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BindCardExceptionDefine.INIT)
	@RequiresPermissions(BindCardExceptionDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, BindCardExceptionBean form) {
		LogUtil.startLog(THIS_CLASS, BindCardExceptionDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(BindCardExceptionDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, BindCardExceptionDefine.INIT);
		return modelAndView;
	}

	@RequestMapping(BindCardExceptionDefine.SEARCH_ACTION)
	@RequiresPermissions(BindCardExceptionDefine.PERMISSION_SEARCH)
	public ModelAndView search(HttpServletRequest request, @ModelAttribute(BindCardExceptionDefine.FORM) BindCardExceptionBean form) {
		LogUtil.startLog(THIS_CLASS, BindCardExceptionDefine.SEARCH_ACTION);
		ModelAndView modeAndView = new ModelAndView(BindCardExceptionDefine.LIST_PATH);
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(THIS_CLASS, BindCardExceptionDefine.SEARCH_ACTION);
		return modeAndView;
	}

	/**
	 * 创建分页
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, BindCardExceptionBean form) {
		Map<String, Object> param = new HashMap<String, Object>();
		// 用户名
		if (StringUtils.isNotEmpty(form.getUserNameSrch())) {
			param.put("userNameSrch", form.getUserNameSrch());
		}
		// 电子账户号
		if (StringUtils.isNotEmpty(form.getAccountIdSrch())) {
			param.put("accountIdSrch", form.getAccountIdSrch());
		}
		// 查询用户银行卡列表
		int total = this.bindCardExceptionService.countBankCardList(param);
		if (total > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), total);
			param.put("limitStart", paginator.getOffset());
			param.put("limitEnd", paginator.getLimit());
			List<BindCardExceptionCustomize> result = this.bindCardExceptionService.selectBankCardList(param);
			form.setRecordList(result);
			form.setPaginator(paginator);
		}
		modelAndView.addObject(BindCardExceptionDefine.FORM, form);
	}

	/**
	 * 更新银行卡信息
	 * 
	 * @param request
	 * @param attr
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(BindCardExceptionDefine.MODIFY_ACTION)
	@RequiresPermissions(BindCardExceptionDefine.PERMISSION_MODIFY)
	public JSONObject modifyBindCard(HttpServletRequest request, RedirectAttributes attr, BindCardExceptionBean form) {
		LogUtil.startLog(THIS_CLASS, BindCardExceptionDefine.MODIFY_ACTION);
		// 返回结果
		JSONObject ret = new JSONObject();
		// 用户ID
		Integer userId = form.getUserId();
		// 电子账户号
		String accountId = form.getAccountId();
		if (userId == null || userId == 0) {
			ret.put("status", "error");
			ret.put("result", "确认发生错误,请重新操作!");
			LogUtil.errorLog(THIS_CLASS, BindCardExceptionDefine.MODIFY_ACTION, new Exception("参数不正确[userId=" + userId + "]"));
			return ret;
		}
		// 电子账户号为空
		if (StringUtils.isEmpty(accountId)) {
			ret.put("status", "error");
			ret.put("result", "确认发生错误,请重新操作!");
			LogUtil.errorLog(THIS_CLASS, BindCardExceptionDefine.MODIFY_ACTION, new Exception("参数不正确[userId=" + userId + "]"));
			return ret;
		}
		try {
			this.bindCardExceptionService.updateBindCard(userId, accountId);
			ret.put(RechargeDefine.JSON_STATUS_KEY, BindCardExceptionDefine.JSON_STATUS_OK);
			ret.put(RechargeDefine.JSON_RESULT_KEY, "银行卡同步操作成功!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		LogUtil.endLog(THIS_CLASS, BindCardExceptionDefine.MODIFY_ACTION);
		return ret;

	}

}
