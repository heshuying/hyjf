package com.hyjf.admin.manager.config.protocol;

import com.hyjf.common.paginator.Paginator;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xiehuili on 2018/5/25.
 */
public class ProtocolTemplateBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private String ids;

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    private List<ProtocolTemplateCommon> recordList;

    private String   pageStatus;
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

    public List<ProtocolTemplateCommon> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<ProtocolTemplateCommon> recordList) {
        this.recordList = recordList;
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


    public String getPageStatus() {
        return pageStatus;
    }

    public void setPageStatus(String pageStatus) {
        this.pageStatus = pageStatus;
    }


}
