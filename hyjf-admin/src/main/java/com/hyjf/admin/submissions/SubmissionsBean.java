package com.hyjf.admin.submissions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.SubmissionsCustomize;

/**
 * @package com.hyjf.admin.submissions
 * @author zhangjinpeng
 * @date 2016/3/7 14:00
 * @version V1.0  
 */
public class SubmissionsBean extends SubmissionsCustomize implements
		Serializable {

	/**
	 * serialVersionUID
	 */

	private static final long serialVersionUID = 387630498860089653L;

	/**
	 * 画面一览列表
	 */
	private List<SubmissionsCustomize> submissionsList = new ArrayList<SubmissionsCustomize>();

	/************************* 以下字段画面检索用 ************************/

	/**
	 * 被选中的意见反馈
	 */
	private String selectedSubId;

	/**
	 * 开始时间
	 */
	private String addTimeStart;

	/**
	 * 结束时间
	 */
	private String addTimeEnd;

	/**
	 * 处理状态
	 */
	private List<ParamName> subStateList = new ArrayList<ParamName>();

	/**
	 * 系统类别
	 */
	private List<ParamName> sysTypeList = new ArrayList<ParamName>();

	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;

	public List<SubmissionsCustomize> getSubmissionsList() {
		return submissionsList;
	}

	public void setSubmissionsList(List<SubmissionsCustomize> submissionsList) {
		this.submissionsList = submissionsList;
	}

	public String getAddTimeStart() {
		return addTimeStart;
	}

	public void setAddTimeStart(String addTimeStart) {
		this.addTimeStart = addTimeStart;
	}

	public String getAddTimeEnd() {
		return addTimeEnd;
	}

	public void setAddTimeEnd(String addTimeEnd) {
		this.addTimeEnd = addTimeEnd;
	}

	public List<ParamName> getSubStateList() {
		return subStateList;
	}

	public void setSubStateList(List<ParamName> subStateList) {
		this.subStateList = subStateList;
	}

	public List<ParamName> getSysTypeList() {
		return sysTypeList;
	}

	public void setSysTypeList(List<ParamName> sysTypeList) {
		this.sysTypeList = sysTypeList;
	}

	public int getPaginatorPage() {
		if (this.paginatorPage == 0) {
			this.paginatorPage = 1;
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

	public String getSelectedSubId() {
		return selectedSubId;
	}

	public void setSelectedSubId(String selectedSubId) {
		this.selectedSubId = selectedSubId;
	}

}
