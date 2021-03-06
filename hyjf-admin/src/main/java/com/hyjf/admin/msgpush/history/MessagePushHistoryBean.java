package com.hyjf.admin.msgpush.history;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.MessagePushMsgHistory;

/**
 * 推送消息标签
 * 
 * @author lishenqiang
 *
 */
public class MessagePushHistoryBean extends MessagePushMsgHistory implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3803722754627032581L;

	/**
	 * 标签类型查询
	 */
	private String historyTagIdSrch;
	/**
	 * 标题查询
	 */
	private String historyTitleSrch;
	/**
	 * 消息编码查询
	 */
	private String historyCodeSrch;
	/**
	 * 作者查询
	 */
	private String historyCreateUserNameSrch;
	/**
	 * 推送平台查询
	 */
	private String historyTerminalSrch;
	/**
	 * 首次阅读终端查询
	 */
	private String historyFirstReadTerminalSrch;
	/**
	 * 状态查询
	 */
	private Integer historySendStatusSrch;
	/**
	 * 指定的原生界面
	 */
	private String historyActionUrl1;
	/**
	 * 指定的原生界面
	 */
	private String historyActionUrl2;
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
	private String historyPreSendTimeStr;

	private String ids;

	private List<MessagePushMsgHistory> recordList;

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

	public String getHistoryTagIdSrch() {
		return historyTagIdSrch;
	}

	public void setHistoryTagIdSrch(String historyTagIdSrch) {
		this.historyTagIdSrch = historyTagIdSrch;
	}

	public String getHistoryTitleSrch() {
		return historyTitleSrch;
	}

	public void setHistoryTitleSrch(String historyTitleSrch) {
		this.historyTitleSrch = historyTitleSrch;
	}

	public String getHistoryCodeSrch() {
		return historyCodeSrch;
	}

	public void setHistoryCodeSrch(String historyCodeSrch) {
		this.historyCodeSrch = historyCodeSrch;
	}

	public String getHistoryCreateUserNameSrch() {
		return historyCreateUserNameSrch;
	}

	public void setHistoryCreateUserNameSrch(String historyCreateUserNameSrch) {
		this.historyCreateUserNameSrch = historyCreateUserNameSrch;
	}

	public String getHistoryTerminalSrch() {
		return historyTerminalSrch;
	}

	public void setHistoryTerminalSrch(String historyTerminalSrch) {
		this.historyTerminalSrch = historyTerminalSrch;
	}

	public Integer getHistorySendStatusSrch() {
		return historySendStatusSrch;
	}

	public void setHistorySendStatusSrch(Integer historySendStatusSrch) {
		this.historySendStatusSrch = historySendStatusSrch;
	}

	public String getHistoryActionUrl1() {
		return historyActionUrl1;
	}

	public void setHistoryActionUrl1(String historyActionUrl1) {
		this.historyActionUrl1 = historyActionUrl1;
	}

	public String getHistoryActionUrl2() {
		return historyActionUrl2;
	}

	public void setHistoryActionUrl2(String historyActionUrl2) {
		this.historyActionUrl2 = historyActionUrl2;
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

	public List<MessagePushMsgHistory> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<MessagePushMsgHistory> recordList) {
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

	public String getHistoryPreSendTimeStr() {
		return historyPreSendTimeStr;
	}

	public void setHistoryPreSendTimeStr(String historyPreSendTimeStr) {
		this.historyPreSendTimeStr = historyPreSendTimeStr;
	}

	public String getHistoryFirstReadTerminalSrch() {
		return historyFirstReadTerminalSrch;
	}

	public void setHistoryFirstReadTerminalSrch(String historyFirstReadTerminalSrch) {
		this.historyFirstReadTerminalSrch = historyFirstReadTerminalSrch;
	}

}
