package com.hyjf.admin.manager.activity.actnov2017.draw;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.ActJanAnswerList;
import com.hyjf.mybatis.model.auto.ActivityBillionThirdConfig;

/**
 * 
 * @author sunss
 */
public class ActDrawGuessBean extends ActivityBillionThirdConfig implements Serializable {

	/**
	 * serialVersionUID:
	 */
	private static final long serialVersionUID = 1L;
	
	
	private List<ActJanAnswerList> recordList;
	
	/**
	 * 用户名 检索条件
	 */
	private String userName;
	/**
	 * 手机号 检索条件
	 */
	private String mobile;
	/**
	 * 题目序号 检索条件
	 */
	private Integer questionId;
	
	
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

	public List<ActJanAnswerList> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<ActJanAnswerList> recordList) {
		this.recordList = recordList;
	}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

}
