package com.hyjf.admin.manager.content.qualify;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.ContentQualify;

/**
 * 活动列表实体类
 * @author 
 *
 */
public class ContentQualifyBean extends ContentQualify implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3803722754627032581L;

	private List<ContentQualify> recordList;

    private String ids;

	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;
	
	/**
     * 前台时间接收
     */
    private String startCreate;

    private String endCreate;

    
    public String getStartCreate() {
        return startCreate;
    }

    public void setStartCreate(String startCreate) {
        this.startCreate = startCreate;
    }

    public String getEndCreate() {
        return endCreate;
    }

    public void setEndCreate(String endCreate) {
        this.endCreate = endCreate;
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

	public List<ContentQualify> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<ContentQualify> recordList) {
		this.recordList = recordList;
	}
	
	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

}
