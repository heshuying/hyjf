package com.hyjf.web.contentArticle;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.ContentArticle;

/**
 * 文章管理实体类
 * 
 * @author qingbing
 *
 */
public class ContentArticleBean extends ContentArticle implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 3803722754627032581L;

    /**
     * 前台时间接收
     */
    private String ids;
    
    private String startCreate;

    private String endCreate;

    public String getStartCreate() {
        return startCreate;
    }

    public void setStartCreate(String startCreate) {
        this.startCreate = startCreate;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getEndCreate() {
        return endCreate;
    }

    public void setEndCreate(String endCreate) {
        this.endCreate = endCreate;
    }

    private List<ContentArticle> recordList;
    

    /**
     * 翻页机能用的隐藏变量
     */
    private int paginatorPage = 1;
    
    private int pageSize = 10;


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

    public List<ContentArticle> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<ContentArticle> recordList) {
        this.recordList = recordList;
    }

    public int getPageSize() {
        if (pageSize == 0) {
            pageSize = 10;
        }
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

}
