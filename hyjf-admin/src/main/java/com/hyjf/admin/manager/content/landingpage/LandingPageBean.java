package com.hyjf.admin.manager.content.landingpage;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.LandingPage;

/**
 * 活动列表实体类
 * 
 * @author qingbing
 *
 */
public class LandingPageBean extends LandingPage implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3803722754627032581L;

	/**
	 * 前台时间接收
	 */
	private String startTime;

	private String endTime;
	
	private String pageNameSrch;
	private String channelNameSrch;

	private String ids;

	private List<LandingPage> recordList;

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

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getPageNameSrch() {
		return pageNameSrch;
	}

	public void setPageNameSrch(String pageNameSrch) {
		this.pageNameSrch = pageNameSrch;
	}

	public String getChannelNameSrch() {
		return channelNameSrch;
	}

	public void setChannelNameSrch(String channelNameSrch) {
		this.channelNameSrch = channelNameSrch;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public List<LandingPage> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<LandingPage> recordList) {
		this.recordList = recordList;
	}


}
