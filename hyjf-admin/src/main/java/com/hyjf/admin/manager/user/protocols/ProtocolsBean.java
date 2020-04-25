package com.hyjf.admin.manager.user.protocols;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.FddTemplet;
import com.hyjf.mybatis.model.customize.FddTempletCustomize;

import java.io.Serializable;
import java.util.List;

/**
 * 文章管理实体类
 * 
 * @author
 *
 */
public class ProtocolsBean extends FddTemplet implements Serializable {

    private String ids;

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 3803722754627032581L;

    /** 列表list */
    private List<FddTempletCustomize> recordList;

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

    public List<FddTempletCustomize> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<FddTempletCustomize> recordList) {
        this.recordList = recordList;
    }

}
