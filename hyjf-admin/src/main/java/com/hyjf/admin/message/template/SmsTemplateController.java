package com.hyjf.admin.message.template;

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
import com.hyjf.mybatis.model.auto.SmsTemplate;
import com.hyjf.mybatis.model.auto.SmsTemplateExample;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = SmsTemplateDefine.TEMPLATE)
public class SmsTemplateController extends BaseController {

	@Autowired
	private SmsTemplateService tplService;

	/**
	 * 分页
	 * 
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modeAndView, SmsTemplateBean form) {
		SmsTemplateExample smsTem = new SmsTemplateExample();
		SmsTemplateExample.Criteria criteria = smsTem.createCriteria();
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

			List<SmsTemplate> accountInfos = this.tplService.queryTem(smsTem);
			form.setPaginator(paginator);
			form.setRecordList(accountInfos);
			modeAndView.addObject(SmsTemplateDefine.TEMPLATE_LIST_FORM, form);
		}

	}

	/**
	 * 模块列表
	 */
	@RequestMapping(value = SmsTemplateDefine.INIT)
	@RequiresPermissions(SmsTemplateDefine.PERMISSIONS_VIEW)
	public ModelAndView messageList(HttpServletRequest request, SmsTemplateBean form) {
		LogUtil.startLog(SmsTemplateController.class.toString(), SmsTemplateDefine.INIT);
		ModelAndView modeAndView = new ModelAndView(SmsTemplateDefine.LIST_VIEW);

		this.createPage(request, modeAndView, form);
		LogUtil.endLog(SmsTemplateController.class.toString(), SmsTemplateDefine.INIT);
		return modeAndView;
	}

	/**
	 * 添加模块
	 */
	@RequestMapping(value = SmsTemplateDefine.ADD_ACTION)
	@RequiresPermissions(SmsTemplateDefine.PERMISSIONS_ADD)
	public ModelAndView addTemplate(HttpServletRequest request, SmsTemplateBean form) {
		LogUtil.startLog(SmsTemplateController.class.toString(), SmsTemplateDefine.ADD_ACTION);
		ModelAndView modeAndView = new ModelAndView(SmsTemplateDefine.INFO_PATH);// 重定向回列表页面
		SmsTemplate smt = new SmsTemplate();
		smt.setCreateTime(GetDate.getNowTime10());
		smt.setStatus(1);
		smt.setTplCode(form.getTemplateCode());
		smt.setTplContent(form.getTplContent());
		smt.setTplName(form.getTplName());
		smt.setUpdateTime(GetDate.getNowTime10());

		if (StringUtils.isNotEmpty(form.getTemplateCode())) {
			tplService.addTem(smt);
		}
		modeAndView.addObject(SmsTemplateDefine.SUCCESS, SmsTemplateDefine.SUCCESS);
		createPage(request, modeAndView, form);
		LogUtil.endLog(SmsTemplateController.class.toString(), SmsTemplateDefine.ADD_ACTION);
		return modeAndView;
	}

	/**
	 * 修改模板
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = SmsTemplateDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(SmsTemplateDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateAction(HttpServletRequest request, SmsTemplateBean form) {
		LogUtil.startLog(SmsTemplateController.class.toString(), SmsTemplateDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(SmsTemplateDefine.INFO_PATH);

		SmsTemplate smt = new SmsTemplate();
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
		modelAndView.addObject(SmsTemplateDefine.SUCCESS, SmsTemplateDefine.SUCCESS);
		modelAndView.addObject(SmsTemplateDefine.INFO_FORM, form);
		LogUtil.endLog(SmsTemplateController.class.toString(), SmsTemplateDefine.UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 * 列表页跳转详情页(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(SmsTemplateDefine.INFO_ACTION)
	@RequiresPermissions(value = { SmsTemplateDefine.PERMISSIONS_INFO, SmsTemplateDefine.PERMISSIONS_ADD,
			SmsTemplateDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView info(HttpServletRequest request, SmsTemplateBean form) {
		LogUtil.startLog(SmsTemplateController.class.toString(), SmsTemplateDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(SmsTemplateDefine.INFO_PATH);

		if (form.getId() != null) {
			Integer id = Integer.valueOf(form.getId());
			SmsTemplate record = this.tplService.queryById(id, form.getTplCode());
			form.setId(record.getId());
			form.setTplContent(record.getTplContent());
			form.setTplName(record.getTplName());
			form.setTemplateCode(record.getTplCode());
			modelAndView.addObject(SmsTemplateDefine.INFO_FORM, form);
		}
		LogUtil.endLog(SmsTemplateController.class.toString(), SmsTemplateDefine.INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 关闭模板
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = SmsTemplateDefine.CLOSE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(SmsTemplateDefine.PERMISSIONS_MODIFY)
	public ModelAndView closeAction(HttpServletRequest request, SmsTemplateBean form) {
		LogUtil.startLog(SmsTemplateController.class.toString(), SmsTemplateDefine.CLOSE_ACTION);
		ModelAndView modelAndView = new ModelAndView(SmsTemplateDefine.LIST_VIEW);

		SmsTemplate smt = new SmsTemplate();
		smt.setTplCode(form.getTplCode());
		smt.setId(form.getId());
		smt.setStatus(0);
		// 更新
		this.tplService.updateTem(smt);
		modelAndView.addObject(SmsTemplateDefine.SUCCESS, SmsTemplateDefine.SUCCESS);
		this.createPage(request, modelAndView, new SmsTemplateBean());
		LogUtil.endLog(SmsTemplateController.class.toString(), SmsTemplateDefine.CLOSE_ACTION);
		return modelAndView;
	}

	/**
	 * 启用模板
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = SmsTemplateDefine.OPEN_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(SmsTemplateDefine.PERMISSIONS_MODIFY)
	public ModelAndView openAction(HttpServletRequest request, SmsTemplateBean form) {
		LogUtil.startLog(SmsTemplateController.class.toString(), SmsTemplateDefine.OPEN_ACTION);
		ModelAndView modelAndView = new ModelAndView(SmsTemplateDefine.LIST_VIEW);

		SmsTemplate smt = new SmsTemplate();
		smt.setTplCode(form.getTplCode());
		smt.setId(form.getId());
		smt.setStatus(1);
		// 更新
		this.tplService.updateTem(smt);
		modelAndView.addObject(SmsTemplateDefine.SUCCESS, SmsTemplateDefine.SUCCESS);
		this.createPage(request, modelAndView, new SmsTemplateBean());
		LogUtil.endLog(SmsTemplateController.class.toString(), SmsTemplateDefine.OPEN_ACTION);
		return modelAndView;
	}
}
