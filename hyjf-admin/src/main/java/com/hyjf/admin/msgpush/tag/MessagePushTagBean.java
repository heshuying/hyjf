package com.hyjf.admin.msgpush.tag;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.MessagePushTag;

/**
 * 推送消息标签
 * 
 * @author lishenqiang
 *
 */
public class MessagePushTagBean extends MessagePushTag implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3803722754627032581L;

	/**
	 * 标签名称查询
	 */
	private String tagNameSrch;

	private String ids;

	private List<MessagePushTag> recordList;

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

	public List<MessagePushTag> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<MessagePushTag> recordList) {
		this.recordList = recordList;
	}

	public String getTagNameSrch() {
		return tagNameSrch;
	}

	public void setTagNameSrch(String tagNameSrch) {
		this.tagNameSrch = tagNameSrch;
	}


}
