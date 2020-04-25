package com.hyjf.admin.msgpush.error;

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
public class MessagePushErrorBean extends MessagePushMsgHistory implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3803722754627032581L;

	/**
	 * 标题查询
	 */
	private String msgTitleSrch;
	/**
	 * 标签查询
	 */
	private String tagIdSrch;
	/**
	 * 消息编码查询
	 */
	private String msgCodeSrch;
	/**
	 * 时间查询
	 */
	private String startDateSrch;
	/**
	 * 时间查询
	 */
	private String endDateSrch;
	
	
	private String ids;

	private List<MessagePushMsgHistory> recordList;

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

	public void setPaginatorPage(int paginatorPage) {
		this.paginatorPage = paginatorPage;
	}

	public Paginator getPaginator() {
		return paginator;
	}

	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getMsgTitleSrch() {
		return msgTitleSrch;
	}

	public void setMsgTitleSrch(String msgTitleSrch) {
		this.msgTitleSrch = msgTitleSrch;
	}

	public List<MessagePushMsgHistory> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<MessagePushMsgHistory> recordList) {
		this.recordList = recordList;
	}

	public String getTagIdSrch() {
		return tagIdSrch;
	}

	public void setTagIdSrch(String tagIdSrch) {
		this.tagIdSrch = tagIdSrch;
	}

	public String getMsgCodeSrch() {
		return msgCodeSrch;
	}

	public void setMsgCodeSrch(String msgCodeSrch) {
		this.msgCodeSrch = msgCodeSrch;
	}

	public String getStartDateSrch() {
		return startDateSrch;
	}

	public void setStartDateSrch(String startDateSrch) {
		this.startDateSrch = startDateSrch;
	}

	public String getEndDateSrch() {
		return endDateSrch;
	}

	public void setEndDateSrch(String endDateSrch) {
		this.endDateSrch = endDateSrch;
	}

}
