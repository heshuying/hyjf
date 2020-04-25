package com.hyjf.admin.manager.config.evaluationconfig.eveluationmoney;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.EvaluationConfig;

import java.io.Serializable;
import java.util.List;

/**
 * 文章管理实体类
 * 
 * @author 
 *
 */
public class EvaluationMoneyBean extends EvaluationConfig implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 3803722754627032581L;

    /**
     * 前台时间接收
     */
    private String ids;

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

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
