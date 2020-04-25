/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.manager.user.userportraitscore;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.admin.UserPortraitScoreCustomize;

import java.io.Serializable;
import java.util.List;

/**
 * @author yaoyong
 * @version UserPortraitScoreBean, v0.1 2018/7/9 17:53
 */
public class UserPortraitScoreBean extends UserPortraitScoreCustomize implements Serializable {
    private List<UserPortraitScoreCustomize> recordList;

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

    public List<UserPortraitScoreCustomize> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<UserPortraitScoreCustomize> recordList) {
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
}
