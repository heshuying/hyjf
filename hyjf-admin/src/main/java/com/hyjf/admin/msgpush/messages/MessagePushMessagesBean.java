package com.hyjf.admin.msgpush.messages;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.MessagePushMsg;

/**
 * 推送消息标签
 * 
 * @author lishenqiang
 *
 */
public class MessagePushMessagesBean extends MessagePushMsg implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3803722754627032581L;

	/**
	 * 标签类型查询
	 */
	private String messagesTagIdSrch;
	/**
	 * 标题查询
	 */
	private String messagesTitleSrch;
	/**
	 * 消息编码查询
	 */
	private String messagesCodeSrch;
	/**
	 * 作者查询
	 */
	private String messagesCreateUserNameSrch;
	/**
	 * 推送平台查询
	 */
	private String messagesTerminalSrch;
	/**
	 * 状态查询
	 */
	private Integer messagesSendStatusSrch;
	/**
	 * 指定的原生界面
	 */
	private String messagesActionUrl1;
	/**
	 * 指定的原生界面
	 */
	private String messagesActionUrl2;
	/**
	 * 指定的原生界面
	 */
	private String messagesActionUrl3;
	/**
	 * 发送开始时间查询
	 */
	private String startSendTimeSrch;
	/**
	 * 发送截止时间查询
	 */
	private String endSendTimeSrch;
	/**
	 * 发送时间
	 */
	private String messagesPreSendTimeStr;

	private String ids;

	private List<MessagePushMsg> recordList;

	/**
	 * 更新或是转发,0为更新1为转发
	 */
	private String updateOrReSend;

	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;

	public int getPaginatorPage() {
		if (paginatorPage == 0) {
			paginatorPage = 1;
		}
		return paginatorPage;
	}

	public String getMessagesTagIdSrch() {
		return messagesTagIdSrch;
	}

	public void setMessagesTagIdSrch(String messagesTagIdSrch) {
		this.messagesTagIdSrch = messagesTagIdSrch;
	}

	public String getMessagesTitleSrch() {
		return messagesTitleSrch;
	}

	public void setMessagesTitleSrch(String messagesTitleSrch) {
		this.messagesTitleSrch = messagesTitleSrch;
	}

	public String getMessagesCodeSrch() {
		return messagesCodeSrch;
	}

	public void setMessagesCodeSrch(String messagesCodeSrch) {
		this.messagesCodeSrch = messagesCodeSrch;
	}

	public String getMessagesCreateUserNameSrch() {
		return messagesCreateUserNameSrch;
	}

	public void setMessagesCreateUserNameSrch(String messagesCreateUserNameSrch) {
		this.messagesCreateUserNameSrch = messagesCreateUserNameSrch;
	}

	public String getMessagesTerminalSrch() {
		return messagesTerminalSrch;
	}

	public void setMessagesTerminalSrch(String messagesTerminalSrch) {
		this.messagesTerminalSrch = messagesTerminalSrch;
	}

	public Integer getMessagesSendStatusSrch() {
		return messagesSendStatusSrch;
	}

	public void setMessagesSendStatusSrch(Integer messagesSendStatusSrch) {
		this.messagesSendStatusSrch = messagesSendStatusSrch;
	}

	public String getMessagesActionUrl1() {
		return messagesActionUrl1;
	}

	public void setMessagesActionUrl1(String messagesActionUrl1) {
		this.messagesActionUrl1 = messagesActionUrl1;
	}

	public String getMessagesActionUrl2() {
		return messagesActionUrl2;
	}

	public void setMessagesActionUrl2(String messagesActionUrl2) {
		this.messagesActionUrl2 = messagesActionUrl2;
	}

	public String getStartSendTimeSrch() {
		return startSendTimeSrch;
	}

	public void setStartSendTimeSrch(String startSendTimeSrch) {
		this.startSendTimeSrch = startSendTimeSrch;
	}

	public String getEndSendTimeSrch() {
		return endSendTimeSrch;
	}

	public void setEndSendTimeSrch(String endSendTimeSrch) {
		this.endSendTimeSrch = endSendTimeSrch;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public List<MessagePushMsg> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<MessagePushMsg> recordList) {
		this.recordList = recordList;
	}

	public String getUpdateOrReSend() {
		return updateOrReSend;
	}

	public void setUpdateOrReSend(String updateOrReSend) {
		this.updateOrReSend = updateOrReSend;
	}

	public Paginator getPaginator() {
		return paginator;
	}

	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}

	public void setPaginatorPage(int paginatorPage) {
		this.paginatorPage = paginatorPage;
	}

	public String getMessagesPreSendTimeStr() {
		return messagesPreSendTimeStr;
	}

	public void setMessagesPreSendTimeStr(String messagesPreSendTimeStr) {
		this.messagesPreSendTimeStr = messagesPreSendTimeStr;
	}

	public String getMessagesActionUrl3() {
		return messagesActionUrl3;
	}

	public void setMessagesActionUrl3(String messagesActionUrl3) {
		this.messagesActionUrl3 = messagesActionUrl3;
	}
}
