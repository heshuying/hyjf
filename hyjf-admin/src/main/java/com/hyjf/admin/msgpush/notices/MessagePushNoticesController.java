package com.hyjf.admin.msgpush.notices;

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
import com.hyjf.admin.msgpush.messages.MessagePushMessagesController;
import com.hyjf.admin.msgpush.messages.MessagePushMessagesDefine;
import com.hyjf.admin.msgpush.tag.MessagePushTagService;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetMessageIdUtil;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.MessagePushMsg;
import com.hyjf.mybatis.model.auto.MessagePushTag;
import com.hyjf.mybatis.model.auto.ParamName;

/**
 * 活动列表页
 * 
 * @author 李深强
 *
 */
@Controller
@RequestMapping(value = MessagePushNoticesDefine.REQUEST_MAPPING)
public class MessagePushNoticesController extends BaseController {

	@Autowired
	private MessagePushNoticesService messagePushNoticesService;

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
	@RequestMapping(MessagePushNoticesDefine.INIT)
	@RequiresPermissions(MessagePushNoticesDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(MessagePushNoticesDefine.FORM) MessagePushNoticesBean form) {
		LogUtil.startLog(MessagePushNoticesController.class.toString(), MessagePushNoticesDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(MessagePushNoticesDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(MessagePushNoticesController.class.toString(), MessagePushNoticesDefine.INIT);
		return modelAndView;
	}

	/**
	 * 条件查询
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(MessagePushNoticesDefine.SEARCH_ACTION)
	@RequiresPermissions(MessagePushNoticesDefine.PERMISSIONS_SEARCH)
	public ModelAndView searchAction(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(MessagePushNoticesDefine.FORM) MessagePushNoticesBean form) {
		LogUtil.startLog(MessagePushNoticesController.class.toString(), MessagePushNoticesDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(MessagePushNoticesDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(MessagePushNoticesController.class.toString(), MessagePushNoticesDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 分页
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, MessagePushNoticesBean form) {
		Integer count = this.messagePushNoticesService.getRecordCount(form);
		if (count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			form.setPaginator(paginator);
			List<MessagePushMsg> recordList = this.messagePushNoticesService.getRecordList(form, paginator.getOffset(), paginator.getLimit());
			form.setRecordList(recordList);
			String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
			modelAndView.addObject("fileDomainUrl", fileDomainUrl);
		}
		// 标签类型
		List<MessagePushTag> noticesPushTags = this.messagePushTagService.getAllPushTagList();
		modelAndView.addObject("noticesPushTags", noticesPushTags);
		prepareDatas(modelAndView);
		modelAndView.addObject(MessagePushNoticesDefine.FORM, form);
	}

	/**
	 * 准备各种枚举
	 * 
	 * @param modelAndView
	 */
	private void prepareDatas(ModelAndView modelAndView) {
		{
			// ======================拼接枚举======================
			// 发送状态
			List<ParamName> noticesSendStatus = this.messagePushNoticesService.getParamNameList("MSG_PUSH_MSG_STATUS");
			modelAndView.addObject("noticesSendStatus", noticesSendStatus);
			// 后续动作
			List<ParamName> noticesActions = this.messagePushNoticesService.getParamNameList("MSG_PUSH_TEMP_ACT");
			modelAndView.addObject("noticesActions", noticesActions);
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
			List<ParamName> naturePages = this.messagePushNoticesService.getParamNameList("MSG_PUSH_NATUREURLS");
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
	@RequestMapping(MessagePushNoticesDefine.INFO_ACTION)
	@RequiresPermissions(value = { MessagePushNoticesDefine.PERMISSIONS_ADD, MessagePushNoticesDefine.PERMISSIONS_MODIFY, MessagePushNoticesDefine.PERMISSIONS_RESEND }, logical = Logical.OR)
	public ModelAndView info(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(MessagePushNoticesDefine.FORM) MessagePushNoticesBean form) {
		LogUtil.startLog(MessagePushNoticesController.class.toString(), MessagePushNoticesDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(MessagePushNoticesDefine.INFO_PATH);
		try {
			if (StringUtils.isNotEmpty(form.getIds())) {
				Integer id = Integer.valueOf(form.getIds());
				MessagePushMsg record = this.messagePushNoticesService.getRecord(id);
				BeanUtils.copyProperties(record, form);
				String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
				modelAndView.addObject("fileDomainUrl", fileDomainUrl);
				if (record.getMsgAction().intValue() == CustomConstants.MSG_PUSH_TEMP_ACT_0) {
					form.setNoticesActionUrl1("");
					form.setNoticesActionUrl2("");
				}
				if (record.getMsgAction().intValue() == CustomConstants.MSG_PUSH_TEMP_ACT_1) {
					form.setNoticesActionUrl1(record.getMsgActionUrl());
					form.setNoticesActionUrl2("");
				}
				if (record.getMsgAction().intValue() == CustomConstants.MSG_PUSH_TEMP_ACT_3) {
					form.setNoticesActionUrl3(record.getMsgActionUrl());
					form.setNoticesActionUrl2("");
				}
				if (record.getMsgAction().intValue() == CustomConstants.MSG_PUSH_TEMP_ACT_2) {
					form.setNoticesActionUrl1("");
					form.setNoticesActionUrl2(record.getMsgActionUrl());
				}
				// 如果是转发,则form的id应置为空
				if (StringUtils.isNotEmpty(form.getUpdateOrReSend()) && form.getUpdateOrReSend().equals("2")) {
					form.setId(null);
				}
				if (form.getMsgSendType().intValue() == CustomConstants.MSG_PUSH_SEND_TYPE_1) {
					if (form.getPreSendTime() != null) {
						form.setNoticesPreSendTimeStr(GetDate.timestamptoStrYYYYMMDDHHMMSS(form.getPreSendTime()));
					}
				}
			}
			modelAndView.addObject(MessagePushNoticesDefine.FORM, form);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 标签类型
		List<MessagePushTag> noticesPushTags = this.messagePushCommonService.getTagList();
		modelAndView.addObject("noticesPushTags", noticesPushTags);
		prepareDatas(modelAndView);
		LogUtil.endLog(MessagePushNoticesController.class.toString(), MessagePushNoticesDefine.INIT);
		return modelAndView;
	}

	/**
	 * 添加信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = MessagePushNoticesDefine.INSERT_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(MessagePushNoticesDefine.PERMISSIONS_ADD)
	public ModelAndView insertAction(HttpServletRequest request, MessagePushNoticesBean form) {
		LogUtil.startLog(MessagePushNoticesController.class.toString(), MessagePushNoticesDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(MessagePushNoticesDefine.INFO_PATH);
		// 调用校验
		if (validatorFieldCheck(modelAndView, form) != null) {
			// 标签类型
			List<MessagePushTag> noticesPushTags = this.messagePushCommonService.getTagList();
			modelAndView.addObject("noticesPushTags", noticesPushTags);
			prepareDatas(modelAndView);
			modelAndView.addObject(MessagePushNoticesDefine.FORM, form);
			return modelAndView;
		}
		modelAndView = new ModelAndView(MessagePushNoticesDefine.RE_LIST_PATH);
		// 数据插入
		MessagePushMsg record = new MessagePushMsg();
		BeanUtils.copyProperties(form, record);
		if (form.getMsgAction().intValue() == CustomConstants.MSG_PUSH_TEMP_ACT_0) {
			record.setMsgActionUrl("");
		}
		if (form.getMsgAction().intValue() == CustomConstants.MSG_PUSH_TEMP_ACT_1||form.getMsgAction().intValue() == CustomConstants.MSG_PUSH_TEMP_ACT_3) {
			record.setMsgActionUrl(form.getNoticesActionUrl1());
		}
		if (form.getMsgAction().intValue() == CustomConstants.MSG_PUSH_TEMP_ACT_2) {
			record.setMsgActionUrl(form.getNoticesActionUrl2());
		}
		if (form.getMsgSendType().intValue() == CustomConstants.MSG_PUSH_SEND_TYPE_1) {
			record.setSendTime(GetDate.getMyTimeInMillis());
			if (StringUtils.isNotEmpty(form.getNoticesPreSendTimeStr())) {
				try {
					Integer time = GetDate.strYYYYMMDDHHMMSS2Timestamp2(form.getNoticesPreSendTimeStr());
					if (time != 0) {
						record.setPreSendTime(time);
						record.setSendTime(time);
					}
				} catch (Exception e) {
				}
			}
		} else {
			record.setPreSendTime(null);
			record.setSendTime(GetDate.getNowTime10());
		}
		String msgCode = GetMessageIdUtil.getNewMsgCode(record.getTagCode());
		record.setMsgCode(msgCode);// 设置ID
		record.setMsgSendStatus(CustomConstants.MSG_PUSH_MSG_STATUS_0);// 设置默认状态
		record.setMsgDestinationType(CustomConstants.MSG_PUSH_DESTINATION_TYPE_0);
		this.messagePushNoticesService.insertRecord(record);
		modelAndView.addObject(MessagePushNoticesDefine.SUCCESS, MessagePushNoticesDefine.SUCCESS);
		LogUtil.endLog(MessagePushNoticesController.class.toString(), MessagePushNoticesDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 修改信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = MessagePushNoticesDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(MessagePushNoticesDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateAction(HttpServletRequest request, MessagePushNoticesBean form) {
		LogUtil.startLog(MessagePushNoticesController.class.toString(), MessagePushNoticesDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(MessagePushNoticesDefine.INFO_PATH);
		// 调用校验
		if (validatorFieldCheck(modelAndView, form) != null) {
			// 标签类型
			List<MessagePushTag> noticesPushTags = this.messagePushCommonService.getTagList();
			modelAndView.addObject("noticesPushTags", noticesPushTags);
			prepareDatas(modelAndView);
			modelAndView.addObject(MessagePushNoticesDefine.FORM, form);
			return modelAndView;
		}
		// 根据id更新
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "id", form.getId().toString())) {
			modelAndView = new ModelAndView(MessagePushNoticesDefine.RE_LIST_PATH);
			return modelAndView;
		}
		modelAndView = new ModelAndView(MessagePushNoticesDefine.RE_LIST_PATH);
		// 更新
		MessagePushMsg record = new MessagePushMsg();
		BeanUtils.copyProperties(form, record);
		if (form.getMsgAction().intValue() == CustomConstants.MSG_PUSH_TEMP_ACT_0) {
			record.setMsgActionUrl("");
		}
		if (form.getMsgAction().intValue() == CustomConstants.MSG_PUSH_TEMP_ACT_1) {
			record.setMsgActionUrl(form.getNoticesActionUrl1());
		}
		if (form.getMsgAction().intValue() == CustomConstants.MSG_PUSH_TEMP_ACT_3) {
			record.setMsgActionUrl(form.getNoticesActionUrl3());
		}
		if (form.getMsgAction().intValue() == CustomConstants.MSG_PUSH_TEMP_ACT_2) {
			record.setMsgActionUrl(form.getNoticesActionUrl2());
		}
		if (form.getMsgSendType().intValue() == CustomConstants.MSG_PUSH_SEND_TYPE_1) {
			record.setSendTime(GetDate.getMyTimeInMillis());
			if (StringUtils.isNotEmpty(form.getNoticesPreSendTimeStr())) {
				try {
					Integer time = GetDate.strYYYYMMDDHHMMSS2Timestamp2(form.getNoticesPreSendTimeStr());
					if (time != 0) {
						record.setPreSendTime(time);
						record.setSendTime(time);
					}
				} catch (Exception e) {
				}
			}
		} else {
			record.setPreSendTime(null);
			record.setSendTime(GetDate.getNowTime10());
		}
		this.messagePushNoticesService.updateRecord(record);
		modelAndView.addObject(MessagePushNoticesDefine.SUCCESS, MessagePushNoticesDefine.SUCCESS);
		LogUtil.endLog(MessagePushNoticesController.class.toString(), MessagePushNoticesDefine.UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 * 删除信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(MessagePushNoticesDefine.DELETE_ACTION)
	@RequiresPermissions(MessagePushNoticesDefine.PERMISSIONS_DELETE)
	public ModelAndView deleteRecordAction(HttpServletRequest request, String ids) {
		LogUtil.startLog(MessagePushNoticesController.class.toString(), MessagePushNoticesDefine.DELETE_ACTION);

		ModelAndView modelAndView = new ModelAndView(MessagePushNoticesDefine.RE_LIST_PATH);
		// 解析json字符串
		List<Integer> recordList = JSONArray.parseArray(ids, Integer.class);
		this.messagePushNoticesService.deleteRecord(recordList);
		LogUtil.endLog(MessagePushNoticesController.class.toString(), MessagePushNoticesDefine.DELETE_ACTION);
		return modelAndView;
	}

	/**
	 * 调用校验表单方法
	 * 
	 * @param modelAndView
	 * @param form
	 * @return
	 */
	private ModelAndView validatorFieldCheck(ModelAndView modelAndView, MessagePushNoticesBean form) {
		// 字段校验(非空判断和长度判断)
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "tagId", form.getTagId() + "")) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "msgTitle", form.getMsgTitle(), 20, true)) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "msgImageUrl", form.getMsgImageUrl(), 100, false)) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "msgContent", form.getMsgContent(), 4000, true)) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "msgTerminal", form.getMsgTerminal(), 20, true)) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "msgAction", form.getMsgAction() + "")) {
			return modelAndView;
		}
		if (form.getMsgAction().intValue() == CustomConstants.MSG_PUSH_TEMP_ACT_1) {
			if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "msgActionUrl1", form.getNoticesActionUrl1(), 100, true)) {
				return modelAndView;
			}
		}
		if(form.getId()!=null) {
			if (form.getMsgAction().intValue() == CustomConstants.MSG_PUSH_TEMP_ACT_3) {
				if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "msgActionUrl3", form.getNoticesActionUrl3(), 100, true)) {
					return modelAndView;
				}
			}
		}
		if (form.getMsgAction().intValue() == CustomConstants.MSG_PUSH_TEMP_ACT_2) {
			if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "msgActionUrl2", form.getNoticesActionUrl2(), 100, true)) {
				return modelAndView;
			}
		}
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "msgSendType", form.getMsgSendType() + "")) {
			return modelAndView;
		}
		if (form.getMsgSendType().intValue() == CustomConstants.MSG_PUSH_SEND_TYPE_1) {
			if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "noticesPreSendTimeStr", form.getNoticesPreSendTimeStr(), 100, true)) {
				return modelAndView;
			}
		}
		return null;
	}

//	/**
//	 * 检查名称唯一
//	 * 
//	 * @param request
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value = MessagePushNoticesDefine.CHECK_ACTION, method = RequestMethod.POST)
//	@RequiresPermissions(value = { MessagePushNoticesDefine.PERMISSIONS_ADD, MessagePushNoticesDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
//	public String checkAction(HttpServletRequest request) {
//		LogUtil.startLog(MessagePushNoticesController.class.toString(), MessagePushNoticesDefine.CHECK_ACTION);
//		String id = request.getParameter("id");
//		String tagId = request.getParameter("tagId");
//		String templateCode = request.getParameter("param");
//		JSONObject ret = new JSONObject();
//		// 检查着名称唯一性
//		int cnt = messagePushNoticesService.countByTemplateCode(GetterUtil.getInteger(id), GetterUtil.getInteger(tagId), templateCode);
//		if (cnt > 0) {
//			String message = ValidatorFieldCheckUtil.getErrorMessage("repeat", "");
//			message = message.replace("{label}", "标签编码");
//			ret.put(MessagePushNoticesDefine.JSON_VALID_INFO_KEY, message);
//		}
//		// 没有错误时,返回y
//		if (!ret.containsKey(MessagePushNoticesDefine.JSON_VALID_INFO_KEY)) {
//			ret.put(MessagePushNoticesDefine.JSON_VALID_STATUS_KEY, MessagePushNoticesDefine.JSON_VALID_STATUS_OK);
//		}
//		LogUtil.endLog(MessagePushNoticesController.class.toString(), MessagePushNoticesDefine.CHECK_ACTION);
//		return ret.toString();
//	}

	/**
	 * 检查是否是url
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = MessagePushMessagesDefine.CHECKURL_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(value = { MessagePushMessagesDefine.PERMISSIONS_ADD, MessagePushMessagesDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public String checkUrlAction(HttpServletRequest request) {
		LogUtil.startLog(MessagePushMessagesController.class.toString(), MessagePushMessagesDefine.CHECKURL_ACTION);
		// String templateActionUrl1 = request.getParameter("param");
		JSONObject ret = new JSONObject();
		// 检查是否是url
		Boolean isUrl = true;// TODO Validator.isUrl2(templateActionUrl1);
		if (!isUrl) {
			ret.put(MessagePushMessagesDefine.JSON_VALID_INFO_KEY, "请输入正确的http地址（全路径）!");
		}
		// 没有错误时,返回y
		if (!ret.containsKey(MessagePushMessagesDefine.JSON_VALID_INFO_KEY)) {
			ret.put(MessagePushMessagesDefine.JSON_VALID_STATUS_KEY, MessagePushMessagesDefine.JSON_VALID_STATUS_OK);
		}
		LogUtil.endLog(MessagePushMessagesController.class.toString(), MessagePushMessagesDefine.CHECKURL_ACTION);
		return ret.toString();
	}

	/**
	 * 检查是否是电话号码s
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = MessagePushMessagesDefine.CHECKMOBILES_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(value = { MessagePushMessagesDefine.PERMISSIONS_ADD, MessagePushMessagesDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public String checkMobilesAction(ModelAndView modelAndView,HttpServletRequest request) {
		LogUtil.startLog(MessagePushMessagesController.class.toString(), MessagePushMessagesDefine.CHECKMOBILES_ACTION);
		String mobiles = request.getParameter("param");
		JSONObject ret = new JSONObject();
		// 检查是否是电话号码
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "msgDestination", mobiles)) {
			ret.put(MessagePushMessagesDefine.JSON_VALID_INFO_KEY, "电话号码不能为空.");
			LogUtil.endLog(MessagePushMessagesController.class.toString(), MessagePushMessagesDefine.CHECKMOBILES_ACTION);
			return ret.toString();
		}
		String[] mobileStrs = mobiles.split(",");
		for (int i = 0; i < mobileStrs.length; i++) {
			if (!ValidatorFieldCheckUtil.validateMobile(modelAndView, "msgDestination", mobileStrs[i], true)) {
				ret.put(MessagePushMessagesDefine.JSON_VALID_INFO_KEY, mobileStrs[i]+"不是正确的电话号码");
				LogUtil.endLog(MessagePushMessagesController.class.toString(), MessagePushMessagesDefine.CHECKMOBILES_ACTION);
				return ret.toString();
			}
		}
		// 没有错误时,返回y
		if (!ret.containsKey(MessagePushMessagesDefine.JSON_VALID_INFO_KEY)) {
			ret.put(MessagePushMessagesDefine.JSON_VALID_STATUS_KEY, MessagePushMessagesDefine.JSON_VALID_STATUS_OK);
		}
		LogUtil.endLog(MessagePushMessagesController.class.toString(), MessagePushMessagesDefine.CHECKMOBILES_ACTION);
		return ret.toString();
	}

	/**
	 * 资料上传
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = MessagePushNoticesDefine.UPLOAD_FILE, method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = { MessagePushNoticesDefine.PERMISSIONS_ADD, MessagePushNoticesDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogUtil.startLog(MessagePushNoticesController.class.toString(), MessagePushNoticesDefine.UPLOAD_FILE);
		String files = messagePushNoticesService.uploadFile(request, response);
		LogUtil.endLog(MessagePushNoticesController.class.toString(), MessagePushNoticesDefine.UPLOAD_FILE);
		return files;
	}
}
