package com.hyjf.admin.msgpush.history;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.msgpush.tag.MessagePushTagService;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.MessagePushMsgHistory;
import com.hyjf.mybatis.model.auto.MessagePushTag;
import com.hyjf.mybatis.model.auto.ParamName;

/**
 * 活动列表页
 * 
 * @author 李深强
 *
 */
@Controller
@RequestMapping(value = MessagePushHistoryDefine.REQUEST_MAPPING)
public class MessagePushHistoryController extends BaseController {

	@Autowired
	private MessagePushHistoryService messagePushHistoryService;

	@Autowired
	private MessagePushTagService messagePushTagService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(MessagePushHistoryDefine.INIT)
	@RequiresPermissions(MessagePushHistoryDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(MessagePushHistoryDefine.FORM) MessagePushHistoryBean form) {
		LogUtil.startLog(MessagePushHistoryController.class.toString(), MessagePushHistoryDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(MessagePushHistoryDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(MessagePushHistoryController.class.toString(), MessagePushHistoryDefine.INIT);
		return modelAndView;
	}

	/**
	 * 条件查询
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(MessagePushHistoryDefine.SEARCH_ACTION)
	@RequiresPermissions(MessagePushHistoryDefine.PERMISSIONS_SEARCH)
	public ModelAndView searchAction(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(MessagePushHistoryDefine.FORM) MessagePushHistoryBean form) {
		LogUtil.startLog(MessagePushHistoryController.class.toString(), MessagePushHistoryDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(MessagePushHistoryDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(MessagePushHistoryController.class.toString(), MessagePushHistoryDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 分页
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, MessagePushHistoryBean form) {
		Integer count = this.messagePushHistoryService.getRecordCount(form);
		if (count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			form.setPaginator(paginator);
			List<MessagePushMsgHistory> recordList = this.messagePushHistoryService.getRecordList(form, paginator.getOffset(), paginator.getLimit());
			form.setRecordList(recordList);
			String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
			modelAndView.addObject("fileDomainUrl", fileDomainUrl);
		}
		// 标签类型
		List<MessagePushTag> historyPushTags = this.messagePushTagService.getAllPushTagList();
		modelAndView.addObject("historyPushTags", historyPushTags);
		prepareDatas(modelAndView);
		modelAndView.addObject(MessagePushHistoryDefine.FORM, form);
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
			List<ParamName> historySendStatus = this.messagePushHistoryService.getParamNameList("MSG_PUSH_SEND_STATUS");
			modelAndView.addObject("historySendStatus", historySendStatus);
			// 后续动作
			List<ParamName> historyActions = this.messagePushHistoryService.getParamNameList("MSG_PUSH_TEMP_ACT");
			modelAndView.addObject("historyActions", historyActions);
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
			List<ParamName> naturePages = this.messagePushHistoryService.getParamNameList("MSG_PUSH_NATUREURLS");
			modelAndView.addObject("naturePages", naturePages);
		}
	}

}
