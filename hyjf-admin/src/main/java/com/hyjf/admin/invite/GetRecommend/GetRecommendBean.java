package com.hyjf.admin.invite.GetRecommend;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.admin.GetRecommendCustomize;

/**
 * 10月份活动  获取推荐星明细 表单类
 * zhangjinpeng
 */
public class GetRecommendBean extends GetRecommendCustomize implements Serializable {

	/**
     * serialVersionUID
     */
	private static final long serialVersionUID = 1790601499725213969L;

    private String ids;
    
    private List<GetRecommendCustomize> recordList;

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

    public List<GetRecommendCustomize> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<GetRecommendCustomize> recordList) {
        this.recordList = recordList;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }
}
