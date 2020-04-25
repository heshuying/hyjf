package com.hyjf.admin.manager.config.htlconfig;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.config.hzrconfig.HzrConfigDefine;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.Product;

/**
 * 惠天利配置页面
 * 
 * @author qingbing
 *
 */
@Controller
@RequestMapping(value = HtlConfigDefine.REQUEST_MAPPING)
public class HtlConfigController extends BaseController {

	@Autowired
	private HtlConfigService htlConfigService;

	/**
	 * 惠天利配置画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(HtlConfigDefine.INIT)
	@RequiresPermissions(HtlConfigDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request,
			@ModelAttribute(HtlConfigDefine.HTLCONFIG_FORM) HtlConfigBean form) {
		// 日志开始
		LogUtil.startLog(HtlConfigController.class.toString(), HtlConfigDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(HtlConfigDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		// 日志结束
		LogUtil.endLog(HtlConfigController.class.toString(), HtlConfigDefine.INIT);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, HtlConfigBean form) {
		List<Product> recordList = htlConfigService.getRecordList(new Product(), -1, -1);
		if (recordList != null) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			recordList = this.htlConfigService.getRecordList(new Product(), paginator.getOffset(),
					paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(HtlConfigDefine.HTLCONFIG_FORM, form);
		}
	}

	/**
	 * 画面迁移(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(HzrConfigDefine.INFO_ACTION)
	@RequiresPermissions(value = { HtlConfigDefine.PERMISSIONS_INFO, HtlConfigDefine.PERMISSIONS_ADD,
			HtlConfigDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView info(HttpServletRequest request,
			@ModelAttribute(HtlConfigDefine.HTLCONFIG_FORM) HtlConfigBean form) {
		LogUtil.startLog(HtlConfigController.class.toString(), HtlConfigDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(HtlConfigDefine.INFO_PATH);
		LogUtil.endLog(HtlConfigController.class.toString(), HtlConfigDefine.INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 数据修改
	 * 
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(HtlConfigDefine.UPDATE_ACTION)
	@RequiresPermissions(HtlConfigDefine.PERMISSIONS_MODIFY)
	public ModelAndView update(HtlConfigBean form) {
		// 日志开始
		LogUtil.startLog(HtlConfigController.class.toString(), HtlConfigDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(HtlConfigDefine.INFO_PATH);
		// 成功修改
		htlConfigService.updateRecord(form);
		modelAndView.addObject(HtlConfigDefine.SUCCESS, HtlConfigDefine.SUCCESS);
		// 日志结束
		LogUtil.endLog(HtlConfigController.class.toString(), HtlConfigDefine.INSERT_ACTION);
		return modelAndView;
	}
}
