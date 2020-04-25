package com.hyjf.admin.message.notice.config;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.SmsNoticeConfigExample;
import com.hyjf.mybatis.model.auto.SmsNoticeConfigWithBLOBs;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = SmsNoticeConfigDefine.NOTICECONFIG)
public class SmsNoticeConfigController extends BaseController {

	@Autowired
	private SmsNoticeConfigService configService;

	/**
	 * 配置列表
	 */
	@RequestMapping(value = SmsNoticeConfigDefine.INIT)
	@RequiresPermissions(SmsNoticeConfigDefine.PERMISSIONS_VIEW)
	public ModelAndView messageList(HttpServletRequest request) {
		LogUtil.startLog(SmsNoticeConfigController.class.toString(), SmsNoticeConfigDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(SmsNoticeConfigDefine.LIST_VIEW);
		List<SmsNoticeConfigWithBLOBs> con = configService.queryConfig(new SmsNoticeConfigExample());

		modelAndView.addObject(SmsNoticeConfigDefine.LIST_FORM, con);

		LogUtil.endLog(SmsNoticeConfigController.class.toString(), SmsNoticeConfigDefine.INIT);
		return modelAndView;
	}

	/**
	 * 添加配置
	 */
	@RequestMapping(value = SmsNoticeConfigDefine.ADD_ACTION)
	@RequiresPermissions(SmsNoticeConfigDefine.PERMISSIONS_ADD)
	public ModelAndView addTemplate(HttpServletRequest request, SmsNoticeConfigBean form) {
		LogUtil.startLog(SmsNoticeConfigController.class.toString(), SmsNoticeConfigDefine.ADD_ACTION);
		ModelAndView modeAndView = new ModelAndView(SmsNoticeConfigDefine.INFO_PATH);
		form.setName(form.getConfigName());
		form.setStatus(1);
		form.setCreateTime(GetDate.getNowTime10());
		form.setUpdateTime(GetDate.getNowTime10());

		configService.addConfig(form);
		modeAndView.addObject(SmsNoticeConfigDefine.SUCCESS, SmsNoticeConfigDefine.SUCCESS);
		LogUtil.endLog(SmsNoticeConfigController.class.toString(), SmsNoticeConfigDefine.ADD_ACTION);
		return modeAndView;
	}

	/**
	 * 修改模板
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = SmsNoticeConfigDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(SmsNoticeConfigDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateAction(HttpServletRequest request, SmsNoticeConfigBean form) {
		LogUtil.startLog(SmsNoticeConfigController.class.toString(), SmsNoticeConfigDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(SmsNoticeConfigDefine.INFO_PATH);

		form.setUpdateTime(GetDate.getNowTime10());
		configService.updateConfig(form);
		modelAndView.addObject(SmsNoticeConfigDefine.SUCCESS, SmsNoticeConfigDefine.SUCCESS);
		modelAndView.addObject(SmsNoticeConfigDefine.INFO_FORM, form);
		LogUtil.endLog(SmsNoticeConfigController.class.toString(), SmsNoticeConfigDefine.UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 * 列表页跳转详情页(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(SmsNoticeConfigDefine.INFO_ACTION)
	@RequiresPermissions(value = { SmsNoticeConfigDefine.PERMISSIONS_INFO, SmsNoticeConfigDefine.PERMISSIONS_ADD,
			SmsNoticeConfigDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView info(HttpServletRequest request, SmsNoticeConfigWithBLOBs form) {
		LogUtil.startLog(SmsNoticeConfigController.class.toString(), SmsNoticeConfigDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(SmsNoticeConfigDefine.INFO_PATH);

		if (form.getId() != null) {
			Integer id = Integer.valueOf(form.getId());
			modelAndView.addObject(SmsNoticeConfigDefine.INFO_FORM, configService.queryById(id, form.getName()));
		}
		LogUtil.endLog(SmsNoticeConfigController.class.toString(), SmsNoticeConfigDefine.INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 关闭模板
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = SmsNoticeConfigDefine.CLOSE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(SmsNoticeConfigDefine.PERMISSIONS_MODIFY)
	public ModelAndView closeAction(HttpServletRequest request, SmsNoticeConfigWithBLOBs form) {
		LogUtil.startLog(SmsNoticeConfigController.class.toString(), SmsNoticeConfigDefine.CLOSE_ACTION);
		ModelAndView modelAndView = new ModelAndView(SmsNoticeConfigDefine.LIST_VIEW);

		form.setStatus(0);
		// 更新
		this.configService.updateConfig(form);
		List<SmsNoticeConfigWithBLOBs> con = configService.queryConfig(new SmsNoticeConfigExample());
		modelAndView.addObject(SmsNoticeConfigDefine.LIST_FORM, con);
		modelAndView.addObject(SmsNoticeConfigDefine.SUCCESS, SmsNoticeConfigDefine.SUCCESS);
		LogUtil.endLog(SmsNoticeConfigController.class.toString(), SmsNoticeConfigDefine.CLOSE_ACTION);
		return modelAndView;
	}

	/**
	 * 启用模板
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = SmsNoticeConfigDefine.OPEN_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(SmsNoticeConfigDefine.PERMISSIONS_MODIFY)
	public ModelAndView openAction(HttpServletRequest request, SmsNoticeConfigWithBLOBs form) {
		LogUtil.startLog(SmsNoticeConfigController.class.toString(), SmsNoticeConfigDefine.OPEN_ACTION);
		ModelAndView modelAndView = new ModelAndView(SmsNoticeConfigDefine.LIST_VIEW);

		form.setStatus(1);
		// 更新
		this.configService.updateConfig(form);
		List<SmsNoticeConfigWithBLOBs> con = configService.queryConfig(new SmsNoticeConfigExample());
		modelAndView.addObject(SmsNoticeConfigDefine.LIST_FORM, con);
		modelAndView.addObject(SmsNoticeConfigDefine.SUCCESS, SmsNoticeConfigDefine.SUCCESS);
		LogUtil.endLog(SmsNoticeConfigController.class.toString(), SmsNoticeConfigDefine.OPEN_ACTION);
		return modelAndView;
	}

	/**
	 * 检索唯一标识
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = SmsNoticeConfigDefine.CHECK_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(SmsNoticeConfigDefine.PERMISSIONS_MODIFY)
	public String checkAction(HttpServletRequest request, SmsNoticeConfigBean form) {
		LogUtil.startLog(SmsNoticeConfigController.class.toString(), SmsNoticeConfigDefine.CHECK_ACTION);

		String name = request.getParameter("name");
		String param = request.getParameter("param");
		JSONObject ret = new JSONObject();
		// 检查月数唯一性
		if ("configName".equals(name)) {
			int cnt = configService.onlyName(param);
			if (cnt > 0) {
				String message = ValidatorFieldCheckUtil.getErrorMessage("repeat", "");
				message = message.replace("{label}", "标识已存在，请勿重复输入");
				ret.put(SmsNoticeConfigDefine.JSON_VALID_INFO_KEY, message);
			}
		}
		// 没有错误时,返回y
		if (!ret.containsKey(SmsNoticeConfigDefine.JSON_VALID_INFO_KEY)) {
			ret.put(SmsNoticeConfigDefine.JSON_VALID_STATUS_KEY, SmsNoticeConfigDefine.JSON_VALID_STATUS_OK);
		}
		LogUtil.endLog(SmsNoticeConfigController.class.toString(), SmsNoticeConfigDefine.CHECK_ACTION);
		return ret.toString();
	}
}
