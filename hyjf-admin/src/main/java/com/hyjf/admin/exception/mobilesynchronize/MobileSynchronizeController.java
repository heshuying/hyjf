package com.hyjf.admin.exception.mobilesynchronize;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.MobileSynchronizeCustomize;

/**
 * 同步手机号Controller
 * 
 * @author liuyang
 *
 */
@Controller
@RequestMapping(value = MobileSynchronizeDefine.REQUEST_MAPPING)
public class MobileSynchronizeController extends BaseController {

	@Autowired
	private MobileSynchronizeService mobileSynchronizeService;

	/** 类名 */
	private static final String THIS_CLASS = MobileSynchronizeController.class.getName();

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(MobileSynchronizeDefine.INIT)
	@RequiresPermissions(MobileSynchronizeDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, MobileSynchronizeBean form) {
		LogUtil.startLog(THIS_CLASS, MobileSynchronizeDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(MobileSynchronizeDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, MobileSynchronizeDefine.INIT);
		return modelAndView;
	}

	/**
	 * 分页技能维护
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, MobileSynchronizeBean form) {
		// 已开户用户数量
		int count = this.mobileSynchronizeService.countBankOpenAccountUsers(form);
		if (count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			List<MobileSynchronizeCustomize> record = mobileSynchronizeService.selectBankOpenAccountUsersList(form, paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(record);
		}
		modelAndView.addObject(MobileSynchronizeDefine.FORM, form);
	}

	/**
	 * 
	 * 列表检索Action
	 * 
	 * @author liuyang
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(MobileSynchronizeDefine.SEARCH_ACTION)
	@RequiresPermissions(MobileSynchronizeDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, MobileSynchronizeBean form) {
		LogUtil.startLog(THIS_CLASS, MobileSynchronizeDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(MobileSynchronizeDefine.LIST_PATH);
		// 分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, MobileSynchronizeDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 重新还款
	 * 
	 * @Title restartRepayAction
	 * @param request
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(MobileSynchronizeDefine.MODIFY_ACTION)
	@RequiresPermissions(MobileSynchronizeDefine.PERMISSIONS_MODIFY)
	public JSONObject modifyMobileAction(HttpServletRequest request, MobileSynchronizeBean form) {
		LogUtil.startLog(THIS_CLASS, MobileSynchronizeDefine.MODIFY_ACTION);
		// 返回结果
		JSONObject ret = new JSONObject();
		// 电子账户号
		String accountId = form.getAccountId();
		// 用户ID
		String userId = form.getUserId();
		// 用户ID为空
		if (StringUtils.isEmpty(userId)) {
			ret.put("status", "error");
			ret.put("result", "用户ID为空!");
			return ret;
		}
		// 用户电子账户号为空
		if (StringUtils.isEmpty(accountId)) {
			ret.put("status", "error");
			ret.put("result", "电子账户号为空!");
			return ret;
		}
		try {
			boolean isUpdateFlag = this.mobileSynchronizeService.updateMobile(accountId, userId);
			if (isUpdateFlag) {
				// add by liuyang 20180301  同步手机后,重新发送CA认证客户信息修改MQ start
				this.mobileSynchronizeService.sendCAMQ(userId);
				// add by liuyang 20180301  同步手机后,重新发送CA认证客户信息修改MQ end

				ret.put("status", "success");
				ret.put("result", "手机号同步成功");
				return ret;
			}else{
				ret.put("status", "error");
				ret.put("result", "手机号同步失败");
				return ret;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		LogUtil.endLog(THIS_CLASS, MobileSynchronizeDefine.MODIFY_ACTION);
		return null;

	}
}
