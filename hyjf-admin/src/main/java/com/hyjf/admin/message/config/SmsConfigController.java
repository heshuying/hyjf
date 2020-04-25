package com.hyjf.admin.message.config;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.mybatis.model.auto.SmsConfigWithBLOBs;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = SmsConfigDefine.CONFIG)
public class SmsConfigController extends BaseController {

	@Autowired
	private SmsConfigService configService;

	/**
	 * 详情页(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(SmsConfigDefine.INIT)
	@RequiresPermissions(SmsConfigDefine.PERMISSIONS_VIEW)
	public ModelAndView info(HttpServletRequest request) {
		LogUtil.startLog(SmsConfigController.class.toString(), SmsConfigDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(SmsConfigDefine.INIT_PATH);

		SmsConfigWithBLOBs config = configService.queryConfig(new SmsConfigWithBLOBs());
		if (config != null) {
			modelAndView.addObject(SmsConfigDefine.INFO_FORM, config);
		} else {
			modelAndView.addObject(SmsConfigDefine.INFO_FORM, new SmsConfigWithBLOBs());
		}
		LogUtil.endLog(SmsConfigController.class.toString(), SmsConfigDefine.INIT);
		return modelAndView;
	}

	/**
	 * 添加模块
	 */
	@RequestMapping(value = SmsConfigDefine.ADD_ACTION)
	@RequiresPermissions(value = {SmsConfigDefine.PERMISSIONS_ADD,SmsConfigDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView addTemplate(HttpServletRequest request, SmsConfigWithBLOBs form) {
		LogUtil.startLog(SmsConfigController.class.toString(), SmsConfigDefine.ADD_ACTION);
		ModelAndView modelAndView = new ModelAndView(SmsConfigDefine.INIT_PATH);

		configService.addConfig(form);

		modelAndView.addObject(SmsConfigDefine.SUCCESS, SmsConfigDefine.SUCCESS);
		modelAndView.addObject(SmsConfigDefine.INFO_FORM, form);
		LogUtil.endLog(SmsConfigController.class.toString(), SmsConfigDefine.ADD_ACTION);
		return modelAndView;
	}

	/**
	 * 修改模板
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = SmsConfigDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(value = {SmsConfigDefine.PERMISSIONS_ADD,SmsConfigDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView updateAction(HttpServletRequest request, SmsConfigWithBLOBs form) {
		LogUtil.startLog(SmsConfigController.class.toString(), SmsConfigDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(SmsConfigDefine.INIT_PATH);

		configService.updateConfig(form);
		modelAndView.addObject(SmsConfigDefine.SUCCESS, SmsConfigDefine.SUCCESS);
		modelAndView.addObject(SmsConfigDefine.INFO_FORM, form);
		LogUtil.endLog(SmsConfigController.class.toString(), SmsConfigDefine.UPDATE_ACTION);
		return modelAndView;
	}

}
