package com.hyjf.admin.msgpush.template;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.msgpush.MessagePushCommonService;
import com.hyjf.admin.msgpush.messages.MessagePushMessagesDefine;
import com.hyjf.admin.msgpush.notices.MessagePushNoticesController;
import com.hyjf.admin.msgpush.tag.MessagePushTagService;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetterUtil;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.MessagePushTag;
import com.hyjf.mybatis.model.auto.MessagePushTemplate;
import com.hyjf.mybatis.model.auto.ParamName;

/**
 * 活动列表页
 *
 * @author 李深强
 *
 */
@Controller
@RequestMapping(value = MessagePushTemplateDefine.REQUEST_MAPPING)
public class MessagePushTemplateController extends BaseController {

	@Autowired
	private MessagePushTemplateService messagePushTemplateService;

	@Autowired
	private MessagePushTagService messagePushTagService;

	@Autowired
	private MessagePushCommonService messagePushCommonService;

	/**
	 * 画面初始化
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(MessagePushTemplateDefine.INIT)
	@RequiresPermissions(MessagePushTemplateDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(MessagePushTemplateDefine.FORM) MessagePushTemplateBean form) {
		LogUtil.startLog(MessagePushTemplateController.class.toString(), MessagePushTemplateDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(MessagePushTemplateDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(MessagePushTemplateController.class.toString(), MessagePushTemplateDefine.INIT);
		return modelAndView;
	}

	/**
	 * 条件查询
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(MessagePushTemplateDefine.SEARCH_ACTION)
	@RequiresPermissions(MessagePushTemplateDefine.PERMISSIONS_SEARCH)
	public ModelAndView searchAction(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(MessagePushTemplateDefine.FORM) MessagePushTemplateBean form) {
		LogUtil.startLog(MessagePushTemplateController.class.toString(), MessagePushTemplateDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(MessagePushTemplateDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(MessagePushTemplateController.class.toString(), MessagePushTemplateDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 分页
	 *
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, MessagePushTemplateBean form) {
		Integer count = this.messagePushTemplateService.getRecordCount(form);
		if (count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			form.setPaginator(paginator);
			List<MessagePushTemplate> recordList = this.messagePushTemplateService.getRecordList(form, paginator.getOffset(), paginator.getLimit());
			form.setRecordList(recordList);
			String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
			modelAndView.addObject("fileDomainUrl", fileDomainUrl);
		}
		// 标签类型
		List<MessagePushTag> templatePushTags = this.messagePushTagService.getAllPushTagList();
		modelAndView.addObject("templatePushTags", templatePushTags);
		prepareDatas(modelAndView);
		modelAndView.addObject(MessagePushTemplateDefine.FORM, form);
	}

	/**
	 * 准备各种枚举
	 *
	 * @param modelAndView
	 */
	private void prepareDatas(ModelAndView modelAndView) {
		{
			// ======================拼接枚举======================
			// 标签状态
			List<ParamName> templateStatus = this.messagePushTemplateService.getParamNameList("MSG_PUSH_STATUS");
			modelAndView.addObject("templateStatus", templateStatus);
			// 后续动作
			List<ParamName> templateActions = this.messagePushTemplateService.getParamNameList("MSG_PUSH_TEMP_ACT");
			modelAndView.addObject("templateActions", templateActions);
			// 推送终端
			List<ParamName> plats = new ArrayList<ParamName>();
			ParamName paramName1 = new ParamName();
			paramName1.setNameCd(CustomConstants.CLIENT_ANDROID);
			paramName1.setName("AndroidAPP");
			ParamName paramName2 = new ParamName();
			paramName2.setNameCd(CustomConstants.CLIENT_IOS);
			paramName2.setName("IOSAPP");
			plats.add(paramName1);
			plats.add(paramName2);
			modelAndView.addObject("plats", plats);
			// 原生页面
			List<ParamName> naturePages = this.messagePushTemplateService.getParamNameList("MSG_PUSH_NATUREURLS");
			modelAndView.addObject("naturePages", naturePages);
		}
	}

	/**
	 * 画面迁移(含有id更新，不含有id添加)
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(MessagePushTemplateDefine.INFO_ACTION)
	@RequiresPermissions(value = { MessagePushTemplateDefine.PERMISSIONS_ADD, MessagePushTemplateDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView info(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(MessagePushTemplateDefine.FORM) MessagePushTemplateBean form) {
		LogUtil.startLog(MessagePushTemplateController.class.toString(), MessagePushTemplateDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(MessagePushTemplateDefine.INFO_PATH);
		try {
			if (StringUtils.isNotEmpty(form.getIds())) {
				Integer id = Integer.valueOf(form.getIds());
				MessagePushTemplate record = this.messagePushTemplateService.getRecord(id);
				BeanUtils.copyProperties(record, form);
				String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
				modelAndView.addObject("fileDomainUrl", fileDomainUrl);
				if (record.getTemplateAction().intValue() == CustomConstants.MSG_PUSH_TEMP_ACT_0) {
					form.setTemplateActionUrl1("");
					form.setTemplateActionUrl2("");
				}
				if (record.getTemplateAction().intValue() == CustomConstants.MSG_PUSH_TEMP_ACT_1) {
					form.setTemplateActionUrl1(record.getTemplateActionUrl());
					form.setTemplateActionUrl2("");
				}
				if (record.getTemplateAction().intValue() == CustomConstants.MSG_PUSH_TEMP_ACT_3) {
					form.setTemplateActionUrl3(record.getTemplateActionUrl());
					form.setTemplateActionUrl2("");
				}
				if (record.getTemplateAction().intValue() == CustomConstants.MSG_PUSH_TEMP_ACT_2) {
					form.setTemplateActionUrl1("");
					form.setTemplateActionUrl2(record.getTemplateActionUrl());
				}
				if (StringUtils.isNotEmpty(record.getTemplateCode()) && record.getTemplateCode().contains("_")) {
					form.setTemplateCode(record.getTemplateCode().substring(record.getTemplateCode().indexOf("_")+1, record.getTemplateCode().length()));
				}
			}
			modelAndView.addObject(MessagePushTemplateDefine.FORM, form);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 标签类型
		List<MessagePushTag> templatePushTags = this.messagePushCommonService.getTagList();
		modelAndView.addObject("templatePushTags", templatePushTags);
		prepareDatas(modelAndView);
		LogUtil.endLog(MessagePushTemplateController.class.toString(), MessagePushTemplateDefine.INIT);
		return modelAndView;
	}

	/**
	 * 添加信息
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = MessagePushTemplateDefine.INSERT_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(MessagePushTemplateDefine.PERMISSIONS_ADD)
	public ModelAndView insertAction(HttpServletRequest request, MessagePushTemplateBean form) {
		LogUtil.startLog(MessagePushTemplateController.class.toString(), MessagePushTemplateDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(MessagePushTemplateDefine.INFO_PATH);
		// 调用校验
		if (validatorFieldCheck(modelAndView, form) != null) {
			// 标签类型
			List<MessagePushTag> templatePushTags = this.messagePushCommonService.getTagList();
			modelAndView.addObject("templatePushTags", templatePushTags);
			prepareDatas(modelAndView);
			modelAndView.addObject(MessagePushMessagesDefine.FORM, form);
			return modelAndView;
		}
		modelAndView = new ModelAndView(MessagePushTemplateDefine.RE_LIST_PATH);
		// 数据插入
		MessagePushTemplate record = new MessagePushTemplate();
		BeanUtils.copyProperties(form, record);
		if (form.getTemplateAction().intValue() == CustomConstants.MSG_PUSH_TEMP_ACT_0) {
			record.setTemplateActionUrl("");
		}
		if (form.getTemplateAction().intValue() == CustomConstants.MSG_PUSH_TEMP_ACT_1||form.getTemplateAction().intValue() == CustomConstants.MSG_PUSH_TEMP_ACT_3) {
			record.setTemplateActionUrl(form.getTemplateActionUrl1());
		}
		if (form.getTemplateAction().intValue() == CustomConstants.MSG_PUSH_TEMP_ACT_2) {
			record.setTemplateActionUrl(form.getTemplateActionUrl2());
		}
		record.setTemplateCode(form.getTagCode() + "_" + form.getTemplateCode());
		this.messagePushTemplateService.insertRecord(record);
		modelAndView.addObject(MessagePushTemplateDefine.SUCCESS, MessagePushTemplateDefine.SUCCESS);
		LogUtil.endLog(MessagePushTemplateController.class.toString(), MessagePushTemplateDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 修改信息
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = MessagePushTemplateDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(MessagePushTemplateDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateAction(HttpServletRequest request, MessagePushTemplateBean form) {
		LogUtil.startLog(MessagePushTemplateController.class.toString(), MessagePushTemplateDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(MessagePushTemplateDefine.INFO_PATH);
		// 调用校验
		if (validatorFieldCheck(modelAndView, form) != null) {
			// 标签类型
			List<MessagePushTag> templatePushTags = this.messagePushCommonService.getTagList();
			modelAndView.addObject("templatePushTags", templatePushTags);
			prepareDatas(modelAndView);
			modelAndView.addObject(MessagePushMessagesDefine.FORM, form);
			return modelAndView;
		}
		// 根据id更新
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "id", form.getId().toString())) {
			modelAndView = new ModelAndView(MessagePushMessagesDefine.RE_LIST_PATH);
			return modelAndView;
		}
		modelAndView = new ModelAndView(MessagePushTemplateDefine.RE_LIST_PATH);
		// 跟新
		MessagePushTemplate record = new MessagePushTemplate();
		BeanUtils.copyProperties(form, record);
		if (form.getTemplateAction().intValue() == CustomConstants.MSG_PUSH_TEMP_ACT_0) {
			record.setTemplateActionUrl("");
		}
		if (form.getTemplateAction().intValue() == CustomConstants.MSG_PUSH_TEMP_ACT_1) {
			record.setTemplateActionUrl(form.getTemplateActionUrl1());
		}
		if (form.getTemplateAction().intValue() == CustomConstants.MSG_PUSH_TEMP_ACT_3) {
			record.setTemplateActionUrl(form.getTemplateActionUrl3());
		}
		if (form.getTemplateAction().intValue() == CustomConstants.MSG_PUSH_TEMP_ACT_2) {
			record.setTemplateActionUrl(form.getTemplateActionUrl2());
		}
		record.setTemplateCode(form.getTagCode() + "_" + form.getTemplateCode());
		this.messagePushTemplateService.updateRecord(record);
		modelAndView.addObject(MessagePushTemplateDefine.SUCCESS, MessagePushTemplateDefine.SUCCESS);
		LogUtil.endLog(MessagePushTemplateController.class.toString(), MessagePushTemplateDefine.UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 * 删除信息
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(MessagePushTemplateDefine.DELETE_ACTION)
	@RequiresPermissions(MessagePushTemplateDefine.PERMISSIONS_DELETE)
	public ModelAndView deleteRecordAction(HttpServletRequest request, String ids) {
		LogUtil.startLog(MessagePushTemplateController.class.toString(), MessagePushTemplateDefine.DELETE_ACTION);

		ModelAndView modelAndView = new ModelAndView(MessagePushTemplateDefine.RE_LIST_PATH);
		// 解析json字符串
		List<Integer> recordList = JSONArray.parseArray(ids, Integer.class);
		this.messagePushTemplateService.deleteRecord(recordList);
		LogUtil.endLog(MessagePushTemplateController.class.toString(), MessagePushTemplateDefine.DELETE_ACTION);
		return modelAndView;
	}

	/**
	 * 调用校验表单方法
	 *
	 * @param modelAndView
	 * @param form
	 * @return
	 */
	private ModelAndView validatorFieldCheck(ModelAndView modelAndView, MessagePushTemplateBean form) {
		// 字段校验(非空判断和长度判断)
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "tagId", form.getTagId() + "")) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "templateCode", form.getTemplateCode(), 40, true)) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "templateTitle", form.getTemplateTitle(), 20, true)) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "templateImageUrl", form.getTemplateImageUrl(), 100, false)) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "templateContent", form.getTemplateContent(), 4000, true)) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "templateTerminal", form.getTemplateTerminal(), 20, true)) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "templateAction", form.getTemplateAction() + "")) {
			return modelAndView;
		}
		if (form.getTemplateAction().intValue() == CustomConstants.MSG_PUSH_TEMP_ACT_1) {
			if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "templateActionUrl1", form.getTemplateActionUrl1(), 100, true)) {
				return modelAndView;
			}
		}
		if(form.getId()!=null) {
			if (form.getTemplateAction().intValue() == CustomConstants.MSG_PUSH_TEMP_ACT_3) {
				if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "templateActionUrl3", form.getTemplateActionUrl3(), 100, true)) {
					return modelAndView;
				}
			}
		}
		if (form.getTemplateAction().intValue() == CustomConstants.MSG_PUSH_TEMP_ACT_2) {
			if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "templateActionUrl2", form.getTemplateActionUrl2(), 100, true)) {
				return modelAndView;
			}
		}
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "status", form.getStatus() + "")) {
			return modelAndView;
		}
		return null;
	}

	/**
	 * 修改状态
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(MessagePushTemplateDefine.STATUS_ACTION)
	@RequiresPermissions(MessagePushTemplateDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateStatus(HttpServletRequest request, String ids) {
		LogUtil.startLog(MessagePushTemplateController.class.toString(), MessagePushTemplateDefine.STATUS_ACTION);

		ModelAndView modelAndView = new ModelAndView(MessagePushTemplateDefine.RE_LIST_PATH);
		// 修改状态
		if (ids != null) {
			Integer id = Integer.valueOf(ids);
			MessagePushTemplate record = this.messagePushTemplateService.getRecord(id);
			// 新建状态只能启用，启用只能禁用，禁用只能启用
			if (record.getStatus().intValue() == CustomConstants.MSG_PUSH_STATUS_0) {
				record.setStatus(CustomConstants.MSG_PUSH_STATUS_1);
			} else if (record.getStatus() == CustomConstants.MSG_PUSH_STATUS_1) {
				record.setStatus(CustomConstants.MSG_PUSH_STATUS_2);
			} else if (record.getStatus() == CustomConstants.MSG_PUSH_STATUS_2) {
				record.setStatus(CustomConstants.MSG_PUSH_STATUS_1);
			}
			this.messagePushTemplateService.updateRecord(record);
		}
		LogUtil.endLog(MessagePushTemplateController.class.toString(), MessagePushTemplateDefine.STATUS_ACTION);
		return modelAndView;
	}

	/**
	 * 检查名称唯一
	 *
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = MessagePushTemplateDefine.CHECK_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(value = { MessagePushTemplateDefine.PERMISSIONS_ADD, MessagePushTemplateDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public String checkAction(HttpServletRequest request) {
		LogUtil.startLog(MessagePushTemplateController.class.toString(), MessagePushTemplateDefine.CHECK_ACTION);
		String id = request.getParameter("id");
		String templateCode = request.getParameter("templateCode");
		// String templateCode = request.getParameter("param");
		JSONObject ret = new JSONObject();
		// 检查着名称唯一性
		int cnt = messagePushTemplateService.countByTemplateCode(GetterUtil.getInteger(id), templateCode);
		if (cnt > 0) {
			String message = ValidatorFieldCheckUtil.getErrorMessage("repeat", "");
			message = message.replace("{label}", "标签编码");
			ret.put(MessagePushTemplateDefine.JSON_VALID_INFO_KEY, message);
		}
		// 没有错误时,返回y
		if (!ret.containsKey(MessagePushTemplateDefine.JSON_VALID_INFO_KEY)) {
			ret.put(MessagePushTemplateDefine.JSON_VALID_STATUS_KEY, MessagePushTemplateDefine.JSON_VALID_STATUS_OK);
		}
		LogUtil.endLog(MessagePushNoticesController.class.toString(), MessagePushTemplateDefine.CHECK_ACTION);
		return ret.toString();
	}

	/**
	 * 检查是否是url
	 *
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = MessagePushTemplateDefine.CHECKURL_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(value = { MessagePushTemplateDefine.PERMISSIONS_ADD, MessagePushTemplateDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public String checkUrlAction(HttpServletRequest request) {
		LogUtil.startLog(MessagePushTemplateController.class.toString(), MessagePushTemplateDefine.CHECKURL_ACTION);
		// String templateActionUrl1 = request.getParameter("param");
		JSONObject ret = new JSONObject();
		// 检查着名称唯一性
		Boolean isUrl = true;// TODO Validator.isUrl2(templateActionUrl1);
		if (!isUrl) {
			ret.put(MessagePushTemplateDefine.JSON_VALID_INFO_KEY, "请输入正确的http地址（全路径）!");
		}
		// 没有错误时,返回y
		if (!ret.containsKey(MessagePushTemplateDefine.JSON_VALID_INFO_KEY)) {
			ret.put(MessagePushTemplateDefine.JSON_VALID_STATUS_KEY, MessagePushTemplateDefine.JSON_VALID_STATUS_OK);
		}
		LogUtil.endLog(MessagePushTemplateController.class.toString(), MessagePushTemplateDefine.CHECKURL_ACTION);
		return ret.toString();
	}

	/**
	 * 资料上传
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = MessagePushTemplateDefine.UPLOAD_FILE, method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = { MessagePushTemplateDefine.PERMISSIONS_ADD, MessagePushTemplateDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogUtil.startLog(MessagePushTemplateController.class.toString(), MessagePushTemplateDefine.UPLOAD_FILE);
		String files = messagePushTemplateService.uploadFile(request, response);
		LogUtil.endLog(MessagePushTemplateController.class.toString(), MessagePushTemplateDefine.UPLOAD_FILE);
		return files;
	}
}
