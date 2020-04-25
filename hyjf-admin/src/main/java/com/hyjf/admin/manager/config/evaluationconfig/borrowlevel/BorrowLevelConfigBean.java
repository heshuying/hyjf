/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.manager.config.evaluationconfig.borrowlevel;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.EvaluationConfig;

import java.io.Serializable;
import java.util.List;

/**
 * 风险测评配置-信用等级配置Bean
 *
 * @author liuyang
 * @version BorrowLevelConfigBean, v0.1 2018/11/28 17:30
 */
public class BorrowLevelConfigBean extends EvaluationConfig implements Serializable {


    private List<EvaluationConfig> recordList;

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

    public List<EvaluationConfig> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<EvaluationConfig> recordList) {
        this.recordList = recordList;
    }


}
