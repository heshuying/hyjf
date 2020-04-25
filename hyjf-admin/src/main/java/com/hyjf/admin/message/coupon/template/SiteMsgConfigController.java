package com.hyjf.admin.message.coupon.template;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.SiteMsgConfig;
import com.hyjf.mybatis.model.auto.SiteMsgConfigExample;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = SiteMsgConfigDefine.TEMPLATE)
public class SiteMsgConfigController extends BaseController {

	@Autowired
	private SiteMsgConfigService tplService;

	/**
	 * 分页
	 * 
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modeAndView, SiteMsgConfigBean form) {
		SiteMsgConfigExample smsTem = new SiteMsgConfigExample();
		SiteMsgConfigExample.Criteria criteria = smsTem.createCriteria();
		if (form.getStatus() != null && form.getStatus() != 2) {
			criteria.andStatusEqualTo(form.getStatus());
		}

		if (StringUtils.isNotEmpty(form.getTplName())) {
			criteria.andTplNameEqualTo(form.getTplName());
		}
		Integer count = this.tplService.queryTemCount(smsTem);
		if (count >= 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			smsTem.setLimitStart(paginator.getOffset());
			smsTem.setLimitEnd(paginator.getLimit());

			List<SiteMsgConfig> accountInfos = this.tplService.queryTem(smsTem);
			form.setPaginator(paginator);
			form.setRecordList(accountInfos);
			modeAndView.addObject(SiteMsgConfigDefine.TEMPLATE_LIST_FORM, form);
		}

	}

	/**
	 * 模块列表
	 */
	@RequestMapping(value = SiteMsgConfigDefine.INIT)
	@RequiresPermissions(SiteMsgConfigDefine.PERMISSIONS_VIEW)
	public ModelAndView messageList(HttpServletRequest request, SiteMsgConfigBean form) {
		LogUtil.startLog(SiteMsgConfigController.class.toString(), SiteMsgConfigDefine.INIT);
		ModelAndView modeAndView = new ModelAndView(SiteMsgConfigDefine.LIST_VIEW);

		this.createPage(request, modeAndView, form);
		LogUtil.endLog(SiteMsgConfigController.class.toString(), SiteMsgConfigDefine.INIT);
		return modeAndView;
	}

	/**
	 * 添加模块
	 */
	@RequestMapping(value = SiteMsgConfigDefine.ADD_ACTION)
	@RequiresPermissions(SiteMsgConfigDefine.PERMISSIONS_ADD)
	public ModelAndView addTemplate(HttpServletRequest request, SiteMsgConfigBean form) {
		LogUtil.startLog(SiteMsgConfigController.class.toString(), SiteMsgConfigDefine.ADD_ACTION);
		ModelAndView modeAndView = new ModelAndView(SiteMsgConfigDefine.INFO_PATH);// 重定向回列表页面
		SiteMsgConfig smt = new SiteMsgConfig();
		smt.setAddTime(GetDate.getNowTime10());
		smt.setStatus(1);
		smt.setTplCode(form.getTemplateCode());
		smt.setTplContent(form.getTplContent());
		smt.setTplName(form.getTplName());
		smt.setUpdateTime(GetDate.getNowTime10());

		if (StringUtils.isNotEmpty(form.getTemplateCode())) {
			tplService.addTem(smt);
		}
		modeAndView.addObject(SiteMsgConfigDefine.SUCCESS, SiteMsgConfigDefine.SUCCESS);
		createPage(request, modeAndView, form);
		LogUtil.endLog(SiteMsgConfigController.class.toString(), SiteMsgConfigDefine.ADD_ACTION);
		return modeAndView;
	}

	/**
	 * 修改模板
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = SiteMsgConfigDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(SiteMsgConfigDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateAction(HttpServletRequest request, SiteMsgConfigBean form) {
		LogUtil.startLog(SiteMsgConfigController.class.toString(), SiteMsgConfigDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(SiteMsgConfigDefine.INFO_PATH);

		SiteMsgConfig smt = new SiteMsgConfig();
		// 根据id更新
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "id", form.getId().toString())) {
			return modelAndView;
		} else {
			smt.setId(form.getId());
		}

		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "tplCode", form.getTplCode().toString())) {
			return modelAndView;
		} else {
			smt.setTplCode(form.getTplCode());
		}

		if (StringUtils.isNotEmpty(form.getTplContent())) {
			smt.setTplContent(form.getTplContent());
		}

		if (StringUtils.isNotEmpty(form.getTplName())) {
			smt.setTplName(form.getTplName());
		}
		// 更新
		this.tplService.updateTem(smt);
		modelAndView.addObject(SiteMsgConfigDefine.SUCCESS, SiteMsgConfigDefine.SUCCESS);
		modelAndView.addObject(SiteMsgConfigDefine.INFO_FORM, form);
		LogUtil.endLog(SiteMsgConfigController.class.toString(), SiteMsgConfigDefine.UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 * 列表页跳转详情页(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(SiteMsgConfigDefine.INFO_ACTION)
	@RequiresPermissions(value = { SiteMsgConfigDefine.PERMISSIONS_INFO, SiteMsgConfigDefine.PERMISSIONS_ADD,
			SiteMsgConfigDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView info(HttpServletRequest request, SiteMsgConfigBean form) {
		LogUtil.startLog(SiteMsgConfigController.class.toString(), SiteMsgConfigDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(SiteMsgConfigDefine.INFO_PATH);

		if (form.getId() != null) {
			Integer id = Integer.valueOf(form.getId());
			SiteMsgConfig record = this.tplService.queryById(id);
			form.setId(record.getId());
			form.setTplContent(record.getTplContent());
			form.setTplName(record.getTplName());
			form.setTemplateCode(record.getTplCode());
			modelAndView.addObject(SiteMsgConfigDefine.INFO_FORM, form);
		}
		LogUtil.endLog(SiteMsgConfigController.class.toString(), SiteMsgConfigDefine.INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 关闭模板
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = SiteMsgConfigDefine.CLOSE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(SiteMsgConfigDefine.PERMISSIONS_MODIFY)
	public ModelAndView closeAction(HttpServletRequest request, SiteMsgConfigBean form) {
		LogUtil.startLog(SiteMsgConfigController.class.toString(), SiteMsgConfigDefine.CLOSE_ACTION);
		ModelAndView modelAndView = new ModelAndView(SiteMsgConfigDefine.LIST_VIEW);

		SiteMsgConfig smt = new SiteMsgConfig();
		smt.setTplCode(form.getTplCode());
		smt.setId(form.getId());
		smt.setStatus(0);
		// 更新
		this.tplService.updateTem(smt);
		modelAndView.addObject(SiteMsgConfigDefine.SUCCESS, SiteMsgConfigDefine.SUCCESS);
		this.createPage(request, modelAndView, new SiteMsgConfigBean());
		LogUtil.endLog(SiteMsgConfigController.class.toString(), SiteMsgConfigDefine.CLOSE_ACTION);
		return modelAndView;
	}

	/**
	 * 启用模板
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = SiteMsgConfigDefine.OPEN_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(SiteMsgConfigDefine.PERMISSIONS_MODIFY)
	public ModelAndView openAction(HttpServletRequest request, SiteMsgConfigBean form) {
		LogUtil.startLog(SiteMsgConfigController.class.toString(), SiteMsgConfigDefine.OPEN_ACTION);
		ModelAndView modelAndView = new ModelAndView(SiteMsgConfigDefine.LIST_VIEW);

		SiteMsgConfig smt = new SiteMsgConfig();
		smt.setTplCode(form.getTplCode());
		smt.setId(form.getId());
		smt.setStatus(1);
		// 更新
		this.tplService.updateTem(smt);
		modelAndView.addObject(SiteMsgConfigDefine.SUCCESS, SiteMsgConfigDefine.SUCCESS);
		this.createPage(request, modelAndView, new SiteMsgConfigBean());
		LogUtil.endLog(SiteMsgConfigController.class.toString(), SiteMsgConfigDefine.OPEN_ACTION);
		return modelAndView;
	}
}
