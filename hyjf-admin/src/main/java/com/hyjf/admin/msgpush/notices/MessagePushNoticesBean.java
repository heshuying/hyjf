package com.hyjf.admin.msgpush.notices;

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
public class MessagePushNoticesBean extends MessagePushMsg implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3803722754627032581L;

	/**
	 * 标签类型查询
	 */
	private String noticesTagIdSrch;
	/**
	 * 标题查询
	 */
	private String noticesTitleSrch;
	/**
	 * 消息编码查询
	 */
	private String noticesCodeSrch;
	/**
	 * 作者查询
	 */
	private String noticesCreateUserNameSrch;
	/**
	 * 推送平台查询
	 */
	private String noticesTerminalSrch;
	/**
	 * 状态查询
	 */
	private Integer noticesSendStatusSrch;
	/**
	 * 指定的原生界面
	 */
	private String noticesActionUrl1;
	/**
	 * 指定的原生界面
	 */
	private String noticesActionUrl2;

	private String noticesActionUrl3;
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
	private String noticesPreSendTimeStr;

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

	public String getNoticesTagIdSrch() {
		return noticesTagIdSrch;
	}

	public void setNoticesTagIdSrch(String noticesTagIdSrch) {
		this.noticesTagIdSrch = noticesTagIdSrch;
	}

	public String getNoticesTitleSrch() {
		return noticesTitleSrch;
	}

	public void setNoticesTitleSrch(String noticesTitleSrch) {
		this.noticesTitleSrch = noticesTitleSrch;
	}

	public String getNoticesCodeSrch() {
		return noticesCodeSrch;
	}

	public void setNoticesCodeSrch(String noticesCodeSrch) {
		this.noticesCodeSrch = noticesCodeSrch;
	}

	public String getNoticesCreateUserNameSrch() {
		return noticesCreateUserNameSrch;
	}

	public void setNoticesCreateUserNameSrch(String noticesCreateUserNameSrch) {
		this.noticesCreateUserNameSrch = noticesCreateUserNameSrch;
	}

	public String getNoticesTerminalSrch() {
		return noticesTerminalSrch;
	}

	public void setNoticesTerminalSrch(String noticesTerminalSrch) {
		this.noticesTerminalSrch = noticesTerminalSrch;
	}

	public Integer getNoticesSendStatusSrch() {
		return noticesSendStatusSrch;
	}

	public void setNoticesSendStatusSrch(Integer noticesSendStatusSrch) {
		this.noticesSendStatusSrch = noticesSendStatusSrch;
	}

	public String getNoticesActionUrl1() {
		return noticesActionUrl1;
	}

	public void setNoticesActionUrl1(String noticesActionUrl1) {
		this.noticesActionUrl1 = noticesActionUrl1;
	}

	public String getNoticesActionUrl2() {
		return noticesActionUrl2;
	}

	public void setNoticesActionUrl2(String noticesActionUrl2) {
		this.noticesActionUrl2 = noticesActionUrl2;
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

	public String getNoticesPreSendTimeStr() {
		return noticesPreSendTimeStr;
	}

	public void setNoticesPreSendTimeStr(String noticesPreSendTimeStr) {
		this.noticesPreSendTimeStr = noticesPreSendTimeStr;
	}

	public String getNoticesActionUrl3() {
		return noticesActionUrl3;
	}

	public void setNoticesActionUrl3(String noticesActionUrl3) {
		this.noticesActionUrl3 = noticesActionUrl3;
	}
}
