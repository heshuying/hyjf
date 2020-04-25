package com.hyjf.admin.message.mail.template;

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
import com.hyjf.mybatis.model.auto.SmsMailTemplate;
import com.hyjf.mybatis.model.auto.SmsMailTemplateExample;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = SmsMailTemplateDefine.TEMPLATE)
public class SmsMailTemplateController extends BaseController {

	@Autowired
	private SmsMailTemplateService tplService;

	/**
	 * 分页
	 * 
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modeAndView, SmsMailTemplateBean form) {
		SmsMailTemplateExample smsTem = new SmsMailTemplateExample();
		SmsMailTemplateExample.Criteria criteria = smsTem.createCriteria();
		if (form.getMailStatus() != null && form.getMailStatus() != 2) {
			criteria.andMailStatusEqualTo(form.getMailStatus());
		}

		if (StringUtils.isNotEmpty(form.getMailName())) {
			criteria.andMailNameEqualTo(form.getMailName());
		}
		Integer count = this.tplService.queryMailTemCount(smsTem);
		if (count >= 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			smsTem.setLimitStart(paginator.getOffset());
			smsTem.setLimitEnd(paginator.getLimit());

			List<SmsMailTemplate> accountInfos = this.tplService.queryMailTem(smsTem);
			form.setPaginator(paginator);
			form.setRecordList(accountInfos);
			modeAndView.addObject(SmsMailTemplateDefine.TEMPLATE_LIST_FORM, form);
		}

	}

	/**
	 * 模块列表
	 */
	@RequestMapping(value = SmsMailTemplateDefine.INIT)
	@RequiresPermissions(SmsMailTemplateDefine.PERMISSIONS_VIEW)
	public ModelAndView messageList(HttpServletRequest request, SmsMailTemplateBean form) {
		LogUtil.startLog(SmsMailTemplateController.class.toString(), SmsMailTemplateDefine.INIT);
		ModelAndView modeAndView = new ModelAndView(SmsMailTemplateDefine.LIST_VIEW);

		this.createPage(request, modeAndView, form);
		LogUtil.endLog(SmsMailTemplateController.class.toString(), SmsMailTemplateDefine.INIT);
		return modeAndView;
	}

	/**
	 * 添加模块
	 */
	@RequestMapping(value = SmsMailTemplateDefine.ADD_ACTION)
	@RequiresPermissions(SmsMailTemplateDefine.PERMISSIONS_ADD)
	public ModelAndView addTemplate(HttpServletRequest request, SmsMailTemplateBean form) {
		LogUtil.startLog(SmsMailTemplateController.class.toString(), SmsMailTemplateDefine.ADD_ACTION);
		ModelAndView modeAndView = new ModelAndView(SmsMailTemplateDefine.INFO_PATH);// 重定向回列表页面
		SmsMailTemplate smt = new SmsMailTemplate();
		smt.setCreateTime(GetDate.getNowTime10());
		smt.setMailStatus(1);
		;
		smt.setMailValue(form.getTemplateCode());
		smt.setMailContent(form.getMailContent());
		smt.setMailName(form.getMailName());
		smt.setUpdateTime(GetDate.getNowTime10());

		if (StringUtils.isNotEmpty(form.getTemplateCode())) {
			tplService.addMailTem(smt);
		}
		modeAndView.addObject(SmsMailTemplateDefine.SUCCESS, SmsMailTemplateDefine.SUCCESS);
		createPage(request, modeAndView, form);
		LogUtil.endLog(SmsMailTemplateController.class.toString(), SmsMailTemplateDefine.ADD_ACTION);
		return modeAndView;
	}

	/**
	 * 修改模板
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = SmsMailTemplateDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(SmsMailTemplateDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateAction(HttpServletRequest request, SmsMailTemplateBean form) {
		LogUtil.startLog(SmsMailTemplateController.class.toString(), SmsMailTemplateDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(SmsMailTemplateDefine.INFO_PATH);

		SmsMailTemplate smt = new SmsMailTemplate();
		// 根据id更新
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "id", form.getId().toString())) {
			return modelAndView;
		} else {
			smt.setId(form.getId());
		}

		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "mailValue", form.getMailValue().toString())) {
			return modelAndView;
		} else {
			smt.setMailValue(form.getMailValue());
		}

		if (StringUtils.isNotEmpty(form.getMailContent())) {
			smt.setMailContent(form.getMailContent());
		}

		if (StringUtils.isNotEmpty(form.getMailName())) {
			smt.setMailName(form.getMailName());
		}
		// 更新
		this.tplService.updateMailTem(smt);
		modelAndView.addObject(SmsMailTemplateDefine.SUCCESS, SmsMailTemplateDefine.SUCCESS);
		modelAndView.addObject(SmsMailTemplateDefine.INFO_FORM, form);
		LogUtil.endLog(SmsMailTemplateController.class.toString(), SmsMailTemplateDefine.UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 * 列表页跳转详情页(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(SmsMailTemplateDefine.INFO_ACTION)
	@RequiresPermissions(value = { SmsMailTemplateDefine.PERMISSIONS_INFO,

			SmsMailTemplateDefine.PERMISSIONS_ADD, SmsMailTemplateDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView info(HttpServletRequest request, SmsMailTemplateBean form) {
		LogUtil.startLog(SmsMailTemplateController.class.toString(), SmsMailTemplateDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(SmsMailTemplateDefine.INFO_PATH);

		if (form.getId() != null) {
			Integer id = Integer.valueOf(form.getId());
			SmsMailTemplate record = this.tplService.queryById(id, form.getMailValue());
			form.setId(record.getId());
			form.setMailContent(record.getMailContent());
			form.setMailName(record.getMailName());
			form.setTemplateCode(record.getMailValue());
			modelAndView.addObject(SmsMailTemplateDefine.INFO_FORM, form);
		}
		LogUtil.endLog(SmsMailTemplateController.class.toString(), SmsMailTemplateDefine.INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 关闭模板
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = SmsMailTemplateDefine.CLOSE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(SmsMailTemplateDefine.PERMISSIONS_MODIFY)
	public ModelAndView closeAction(HttpServletRequest request, SmsMailTemplateBean form) {
		LogUtil.startLog(SmsMailTemplateController.class.toString(), SmsMailTemplateDefine.CLOSE_ACTION);
		ModelAndView modelAndView = new ModelAndView(SmsMailTemplateDefine.LIST_VIEW);

		SmsMailTemplate smt = new SmsMailTemplate();
		smt.setMailValue(form.getMailValue());
		smt.setId(form.getId());
		smt.setMailStatus(0);
		// 更新
		this.tplService.updateMailTem(smt);
		modelAndView.addObject(SmsMailTemplateDefine.SUCCESS, SmsMailTemplateDefine.SUCCESS);
		this.createPage(request, modelAndView, new SmsMailTemplateBean());
		LogUtil.endLog(SmsMailTemplateController.class.toString(), SmsMailTemplateDefine.CLOSE_ACTION);
		return modelAndView;
	}

	/**
	 * 启用模板
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = SmsMailTemplateDefine.OPEN_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(SmsMailTemplateDefine.PERMISSIONS_MODIFY)
	public ModelAndView openAction(HttpServletRequest request, SmsMailTemplateBean form) {
		LogUtil.startLog(SmsMailTemplateController.class.toString(), SmsMailTemplateDefine.OPEN_ACTION);
		ModelAndView modelAndView = new ModelAndView(SmsMailTemplateDefine.LIST_VIEW);

		SmsMailTemplate smt = new SmsMailTemplate();
		smt.setMailValue(form.getMailValue());
		smt.setId(form.getId());
		smt.setMailStatus(1);
		// 更新
		this.tplService.updateMailTem(smt);
		modelAndView.addObject(SmsMailTemplateDefine.SUCCESS, SmsMailTemplateDefine.SUCCESS);
		this.createPage(request, modelAndView, new SmsMailTemplateBean());
		LogUtil.endLog(SmsMailTemplateController.class.toString(), SmsMailTemplateDefine.OPEN_ACTION);
		return modelAndView;
	}
}
