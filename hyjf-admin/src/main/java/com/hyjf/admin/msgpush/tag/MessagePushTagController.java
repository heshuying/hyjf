package com.hyjf.admin.msgpush.tag;

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
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetterUtil;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.MessagePushTag;
import com.hyjf.mybatis.model.auto.ParamName;

/**
 * 活动列表页
 * 
 * @author 李深强
 *
 */
@Controller
@RequestMapping(value = MessagePushTagDefine.REQUEST_MAPPING)
public class MessagePushTagController extends BaseController {

	@Autowired
	private MessagePushTagService messagePushTagService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(MessagePushTagDefine.INIT)
	@RequiresPermissions(MessagePushTagDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(MessagePushTagDefine.FORM) MessagePushTagBean form) {
		LogUtil.startLog(MessagePushTagController.class.toString(), MessagePushTagDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(MessagePushTagDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(MessagePushTagController.class.toString(), MessagePushTagDefine.INIT);
		return modelAndView;
	}

	/**
	 * 条件查询
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(MessagePushTagDefine.SEARCH_ACTION)
	@RequiresPermissions(MessagePushTagDefine.PERMISSIONS_SEARCH)
	public ModelAndView select(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(MessagePushTagDefine.FORM) MessagePushTagBean form) {
		LogUtil.startLog(MessagePushTagController.class.toString(), MessagePushTagDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(MessagePushTagDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(MessagePushTagController.class.toString(), MessagePushTagDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 分页
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, MessagePushTagBean form) {
		Integer count = this.messagePushTagService.getRecordCount(form);
		if (count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			form.setPaginator(paginator);
			List<MessagePushTag> recordList = this.messagePushTagService.getRecordList(form, paginator.getOffset(), paginator.getLimit());
			form.setRecordList(recordList);
			String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
			modelAndView.addObject("fileDomainUrl", fileDomainUrl);
		}
		// 标签状态
		List<ParamName> tagStatus = this.messagePushTagService.getParamNameList("MSG_PUSH_STATUS");
		modelAndView.addObject("tagStatus", tagStatus);

		modelAndView.addObject(MessagePushTagDefine.FORM, form);
	}

	/**
	 * 画面迁移(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(MessagePushTagDefine.INFO_ACTION)
	@RequiresPermissions(value = { MessagePushTagDefine.PERMISSIONS_ADD, MessagePushTagDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView info(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(MessagePushTagDefine.FORM) MessagePushTagBean form) {
		LogUtil.startLog(MessagePushTagController.class.toString(), MessagePushTagDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(MessagePushTagDefine.INFO_PATH);
		try {
			if (StringUtils.isNotEmpty(form.getIds())) {
				Integer id = Integer.valueOf(form.getIds());
				MessagePushTag record = this.messagePushTagService.getRecord(id);
				BeanUtils.copyProperties(record, form);
				String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
				modelAndView.addObject("fileDomainUrl", fileDomainUrl);
			}
			modelAndView.addObject(MessagePushTagDefine.FORM, form);
		} catch (Exception e) {
			e.printStackTrace();
		}
		LogUtil.endLog(MessagePushTagController.class.toString(), MessagePushTagDefine.INIT);
		return modelAndView;
	}

	/**
	 * 添加信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = MessagePushTagDefine.INSERT_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(MessagePushTagDefine.PERMISSIONS_ADD)
	public ModelAndView insertAction(HttpServletRequest request, MessagePushTag form) {
		LogUtil.startLog(MessagePushTagController.class.toString(), MessagePushTagDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(MessagePushTagDefine.INFO_PATH);
		// 调用校验
		if (validatorFieldCheck(modelAndView, form) != null) {
			// 失败返回
			return validatorFieldCheck(modelAndView, form);
		}
		// 数据插入
		this.messagePushTagService.insertRecord(form);
		modelAndView.addObject(MessagePushTagDefine.SUCCESS, MessagePushTagDefine.SUCCESS);
		LogUtil.endLog(MessagePushTagController.class.toString(), MessagePushTagDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 修改信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = MessagePushTagDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(MessagePushTagDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateAction(HttpServletRequest request, MessagePushTag form) {
		LogUtil.startLog(MessagePushTagController.class.toString(), MessagePushTagDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(MessagePushTagDefine.INFO_PATH);
		// 调用校验
		if (validatorFieldCheck(modelAndView, form) != null) {
			// 失败返回
			return validatorFieldCheck(modelAndView, form);
		}
		// 根据id更新
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "id", form.getId().toString())) {
			return modelAndView;
		}
		// 跟新
		this.messagePushTagService.updateRecord(form);
		modelAndView.addObject(MessagePushTagDefine.SUCCESS, MessagePushTagDefine.SUCCESS);
		LogUtil.endLog(MessagePushTagController.class.toString(), MessagePushTagDefine.UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 * 删除信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(MessagePushTagDefine.DELETE_ACTION)
	@RequiresPermissions(MessagePushTagDefine.PERMISSIONS_DELETE)
	public ModelAndView deleteRecordAction(HttpServletRequest request, String ids) {
		LogUtil.startLog(MessagePushTagController.class.toString(), MessagePushTagDefine.DELETE_ACTION);

		ModelAndView modelAndView = new ModelAndView(MessagePushTagDefine.RE_LIST_PATH);
		// 解析json字符串
		List<Integer> recordList = JSONArray.parseArray(ids, Integer.class);
		this.messagePushTagService.deleteRecord(recordList);
		LogUtil.endLog(MessagePushTagController.class.toString(), MessagePushTagDefine.DELETE_ACTION);
		return modelAndView;
	}

	/**
	 * 调用校验表单方法
	 * 
	 * @param modelAndView
	 * @param form
	 * @return
	 */
	private ModelAndView validatorFieldCheck(ModelAndView modelAndView, MessagePushTag form) {
		// 字段校验(非空判断和长度判断)
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "tagName", form.getTagName())) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "tagCode", form.getTagCode())) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "introduction", form.getIntroduction())) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "iconUrl", form.getIconUrl())) {
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
	@RequestMapping(MessagePushTagDefine.STATUS_ACTION)
	@RequiresPermissions(MessagePushTagDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateStatus(HttpServletRequest request, String ids) {
		LogUtil.startLog(MessagePushTagController.class.toString(), MessagePushTagDefine.STATUS_ACTION);

		ModelAndView modelAndView = new ModelAndView(MessagePushTagDefine.RE_LIST_PATH);
		// 修改状态
		if (ids != null) {
			Integer id = Integer.valueOf(ids);
			MessagePushTag record = this.messagePushTagService.getRecord(id);
			// 新建状态只能启用，启用只能禁用，禁用只能启用
			if (record.getStatus().intValue() == CustomConstants.MSG_PUSH_STATUS_0) {
				record.setStatus(CustomConstants.MSG_PUSH_STATUS_1);
			} else if (record.getStatus() == CustomConstants.MSG_PUSH_STATUS_1) {
				record.setStatus(CustomConstants.MSG_PUSH_STATUS_2);
			} else if (record.getStatus() == CustomConstants.MSG_PUSH_STATUS_2) {
				record.setStatus(CustomConstants.MSG_PUSH_STATUS_1);
			}
			this.messagePushTagService.updateRecord(record);
		}
		LogUtil.endLog(MessagePushTagController.class.toString(), MessagePushTagDefine.STATUS_ACTION);
		return modelAndView;
	}

	/**
	 * 检查名称唯一
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = MessagePushTagDefine.CHECK_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(value = { MessagePushTagDefine.PERMISSIONS_ADD, MessagePushTagDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public String checkAction(HttpServletRequest request) {
		LogUtil.startLog(MessagePushTagController.class.toString(), MessagePushTagDefine.CHECK_ACTION);
		String id = request.getParameter("id");
		String param = request.getParameter("param");
		JSONObject ret = new JSONObject();
		// 检查着名称唯一性
		int cnt = messagePushTagService.countByTagCode(GetterUtil.getInteger(id), param);
		if (cnt > 0) {
			String message = ValidatorFieldCheckUtil.getErrorMessage("repeat", "");
			message = message.replace("{label}", "标签编码");
			ret.put(MessagePushTagDefine.JSON_VALID_INFO_KEY, message);
		}
		// 没有错误时,返回y
		if (!ret.containsKey(MessagePushTagDefine.JSON_VALID_INFO_KEY)) {
			ret.put(MessagePushTagDefine.JSON_VALID_STATUS_KEY, MessagePushTagDefine.JSON_VALID_STATUS_OK);
		}
		LogUtil.endLog(MessagePushTagController.class.toString(), MessagePushTagDefine.CHECK_ACTION);
		return ret.toString();
	}

	/**
	 * 资料上传
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = MessagePushTagDefine.UPLOAD_FILE, method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = { MessagePushTagDefine.PERMISSIONS_ADD, MessagePushTagDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogUtil.startLog(MessagePushTagController.class.toString(), MessagePushTagDefine.UPLOAD_FILE);
		String files = messagePushTagService.uploadFile(request, response);
		LogUtil.endLog(MessagePushTagController.class.toString(), MessagePushTagDefine.UPLOAD_FILE);
		return files;
	}

}
