/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.manager.user.operationlog;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;

import java.io.Serializable;
import java.util.List;

/**
 * @author yaoyong
 * @version UserOperationLogBean, v0.1 2018/10/10 10:29
 */
public class UserOperationLogBean extends UserOperationLogEntity implements Serializable {

    //检索条件
    /**
     * 操作时间开始
     */
    private String operationTimeStart;

    /**
     * 操作时间结束
     */
    private String operationTimeEnd;

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

    private List<UserOperationLogEntity> recordList;

    public String getOperationTimeStart() {
        return operationTimeStart;
    }

    public void setOperationTimeStart(String operationTimeStart) {
        this.operationTimeStart = operationTimeStart;
    }

    public String getOperationTimeEnd() {
        return operationTimeEnd;
    }

    public void setOperationTimeEnd(String operationTimeEnd) {
        this.operationTimeEnd = operationTimeEnd;
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

    public List<UserOperationLogEntity> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<UserOperationLogEntity> recordList) {
        this.recordList = recordList;
    }
}
