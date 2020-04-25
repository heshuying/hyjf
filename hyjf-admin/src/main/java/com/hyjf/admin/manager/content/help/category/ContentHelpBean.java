package com.hyjf.admin.manager.content.help.category;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.Category;
import com.hyjf.mybatis.model.auto.ContentHelp;
import com.hyjf.mybatis.model.customize.ContentHelpCustomize;

public class ContentHelpBean extends ContentHelp implements Serializable {

	/**
	 * serialVersionUID
	 */

	private static final long serialVersionUID = 387630498860089653L;

	private Integer delType;

	/**
	 * 给文章替换的新的id
	 */
	private Integer newid;
	/**
	 * 给文章替换的新的pid
	 */
	private Integer newpid;

	// 区间查询添加时间开始时间
	private String post_time_begin;

	// 区间查询添加时间结束时间
	private String post_time_end;

	private List<Category> recordList;

	private List<ContentHelpCustomize> helpList;

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

	public Integer getDelType() {
		return delType;
	}

	public void setDelType(Integer delType) {
		this.delType = delType;
	}

	public Integer getNewid() {
		return newid;
	}

	public void setNewid(Integer newid) {
		this.newid = newid;
	}

	public Integer getNewpid() {
		return newpid;
	}

	public void setNewpid(Integer newpid) {
		this.newpid = newpid;
	}

	public String getPost_time_begin() {
		return post_time_begin;
	}

	public void setPost_time_begin(String post_time_begin) {
		this.post_time_begin = post_time_begin;
	}

	public String getPost_time_end() {
		return post_time_end;
	}

	public void setPost_time_end(String post_time_end) {
		this.post_time_end = post_time_end;
	}

	public List<Category> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<Category> recordList) {
		this.recordList = recordList;
	}

	public List<ContentHelpCustomize> getHelpList() {
		return helpList;
	}

	public void setHelpList(List<ContentHelpCustomize> helpList) {
		this.helpList = helpList;
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
}
