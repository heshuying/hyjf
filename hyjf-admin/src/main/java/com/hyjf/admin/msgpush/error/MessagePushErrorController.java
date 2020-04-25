package com.hyjf.admin.msgpush.error;

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
import com.hyjf.admin.msgpush.MessagePushCommonService;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.MessagePushMsgHistory;
import com.hyjf.mybatis.model.auto.MessagePushTag;
import com.hyjf.mybatis.model.auto.ParamName;

/**
 * 推送异常列表页
 * 
 * @author 李深强
 *
 */
@Controller
@RequestMapping(value = MessagePushErrorDefine.REQUEST_MAPPING)
public class MessagePushErrorController extends BaseController {

	@Autowired
	private MessagePushErrorService messagePushErrorService;
	@Autowired
	private MessagePushCommonService messagePushCommonService;
 
	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(MessagePushErrorDefine.INIT)
	@RequiresPermissions(MessagePushErrorDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(MessagePushErrorDefine.FORM) MessagePushErrorBean form) {
		LogUtil.startLog(MessagePushErrorController.class.toString(), MessagePushErrorDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(MessagePushErrorDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(MessagePushErrorController.class.toString(), MessagePushErrorDefine.INIT);
		return modelAndView;
	}

	/**
	 * 条件查询
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(MessagePushErrorDefine.SEARCH_ACTION)
	@RequiresPermissions(MessagePushErrorDefine.PERMISSIONS_SEARCH)
	public ModelAndView select(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(MessagePushErrorDefine.FORM) MessagePushErrorBean form) {
		LogUtil.startLog(MessagePushErrorController.class.toString(), MessagePushErrorDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(MessagePushErrorDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(MessagePushErrorController.class.toString(), MessagePushErrorDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 分页 
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, MessagePushErrorBean form) {
		Integer count = this.messagePushErrorService.getRecordCount(form);
		if(count > 0){
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			form.setPaginator(paginator);
			List<MessagePushMsgHistory> recordList = this.messagePushErrorService.getRecordList(form, paginator.getOffset(), paginator.getLimit());
			form.setRecordList(recordList);
			String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
			modelAndView.addObject("fileDomainUrl", fileDomainUrl);
		}
		// 标签
		List<MessagePushTag> tagList = this.messagePushErrorService.getTagList();
		modelAndView.addObject("tagList", tagList);
		// 发送状态
		List<ParamName> messagesSendStatus = this.messagePushErrorService.getParamNameList("MSG_PUSH_SEND_STATUS");
		modelAndView.addObject("messagesSendStatus", messagesSendStatus);
		
		modelAndView.addObject(MessagePushErrorDefine.FORM, form);
	}
	
	/**
	 * 修改状态
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(MessagePushErrorDefine.RESEND_ACTION)
	@RequiresPermissions(MessagePushErrorDefine.PERMISSIONS_MODIFY)
	public ModelAndView resendAction(HttpServletRequest request, String ids) {
		LogUtil.startLog(MessagePushErrorController.class.toString(), MessagePushErrorDefine.RESEND_ACTION);

		ModelAndView modelAndView = new ModelAndView(MessagePushErrorDefine.RE_LIST_PATH);
		// 重发此消息
		if (ids != null) {
			Integer id = Integer.valueOf(ids);
			MessagePushMsgHistory msg = this.messagePushErrorService.getRecord(id);
			this.messagePushCommonService.sendMessage(msg);
		}
		LogUtil.endLog(MessagePushErrorController.class.toString(), MessagePushErrorDefine.RESEND_ACTION);
		return modelAndView;
	}

	
	
}
