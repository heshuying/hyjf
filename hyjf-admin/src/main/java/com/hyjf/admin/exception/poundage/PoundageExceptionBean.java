package com.hyjf.admin.exception.poundage;

import java.util.List;
import java.io.Serializable;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.poundageledger.PoundageExceptionCustomize;

/**
 * com.hyjf.admin.exception.poundage
 *
 * @author wgx
 * @date 2017/12/15
 */
public class PoundageExceptionBean extends PoundageExceptionCustomize implements Serializable {

    private static final long serialVersionUID = -1277747628400943073L;

    private List<PoundageExceptionCustomize> recordList;
    /**
     * 翻页机能用的隐藏变量
     */
    private int paginatorPage = 1;

    /**
     * 列表画面自定义标签上的用翻页对象：paginator
     */
    private Paginator paginator;

    private String fid; //功能菜单id

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
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

    public List<PoundageExceptionCustomize> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<PoundageExceptionCustomize> recordList) {
        this.recordList = recordList;
    }

    public Paginator getPaginator() {
        return paginator;
    }

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }


}
