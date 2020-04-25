package com.hyjf.admin.message.log;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.SmsLog;
import com.hyjf.mybatis.model.customize.SmsLogCustomize;

/**
 * @package com.hyjf.admin.message
 * @author Gaolang
 * @date 2015/11/26 17:00
 * @version V1.0  
 */
public class SmsLogBean extends SmsLog implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 手机号码
	 */
	private String mobile;

	/**
	 * 发送状态 0 成功 1 失败 2 全部
	 */
	private Integer status;

	/**
	 * 提交时间区间查询开始时间
	 */
	private String post_time_begin;

	/**
	 * 提交时间区间查询结束时间
	 */
	private String post_time_end;

	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;
	private int limitStart = -1;
	private int limitEnd = -1;

	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;

	private List<SmsLogCustomize> recordList;

	/**
	 * @return the recordList
	 */
	public List<SmsLogCustomize> getRecordList() {
		return recordList;
	}

	/**
	 * @param recordList
	 *            the recordList to set
	 */
	public void setRecordList(List<SmsLogCustomize> recordList) {
		this.recordList = recordList;
	}

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

	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile
	 *            the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return the post_time_begin
	 */
	public String getPost_time_begin() {
		return post_time_begin;
	}

	/**
	 * @param post_time_begin
	 *            the post_time_begin to set
	 */
	public void setPost_time_begin(String post_time_begin) {
		this.post_time_begin = post_time_begin;
	}

	/**
	 * @return the post_time_end
	 */
	public String getPost_time_end() {
		return post_time_end;
	}

	/**
	 * @param post_time_end
	 *            the post_time_end to set
	 */
	public void setPost_time_end(String post_time_end) {
		this.post_time_end = post_time_end;
	}

	public int getLimitStart() {
		return limitStart;
	}

	public void setLimitStart(int limitStart) {
		this.limitStart = limitStart;
	}

	public int getLimitEnd() {
		return limitEnd;
	}

	public void setLimitEnd(int limitEnd) {
		this.limitEnd = limitEnd;
	}

}
