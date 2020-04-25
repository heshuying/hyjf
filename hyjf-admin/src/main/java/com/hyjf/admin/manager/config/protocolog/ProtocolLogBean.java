package com.hyjf.admin.manager.config.protocolog;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.ProtocolLog;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DELL on 2018/6/4.
 */
public class ProtocolLogBean implements Serializable {
    private static final long serialVersionUID = 1L;

    //    //协议模板
//    private ProtocolTemplate protocolTemplate;
//    //协议日志
//    private ProtocolLog protocolLog;
//    //协议版本
//    private ProtocolVersion protocolVersion;
//    private String ids;
//
//    public String getIds() {
//        return ids;
//    }
//
//    public void setIds(String ids) {
//        this.ids = ids;
//    }

    private List<ProtocolLog> recordList;

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

    public List<ProtocolLog> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<ProtocolLog> recordList) {
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
