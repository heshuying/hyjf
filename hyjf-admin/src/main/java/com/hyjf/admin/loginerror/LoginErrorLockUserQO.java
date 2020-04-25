/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.loginerror;

import com.google.common.collect.Lists;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.LoginErrorLockUser;
import com.hyjf.mybatis.model.customize.loginerror.LoginErrorLockUserExtend;

import java.util.List;

/**
 * @author cui
 * @version LoginErrorLockUserQO, v0.1 2018/7/13 16:57
 */
public class LoginErrorLockUserQO extends LoginErrorLockUser {

    //最后一次登录失败时间-开始时间
    private String lockTimeStartStr;

    //最后一次登录失败时间-结束时间
    private String lockTimeEndStr;

    //是否是前台锁定用户
    private Integer isFront;

    /**
     * 翻页机能用的隐藏变量
     */
    private int paginatorPage = 1;

    /**
     * 列表画面自定义标签上的用翻页对象：paginator
     */
    private Paginator paginator;


    private List<LoginErrorLockUserExtend> lstLockUser;


    public String getLockTimeStartStr() {
        return lockTimeStartStr;
    }

    public void setLockTimeStartStr(String lockTimeStartStr) {
        this.lockTimeStartStr = lockTimeStartStr;
    }

    public String getLockTimeEndStr() {
        return lockTimeEndStr;
    }

    public void setLockTimeEndStr(String lockTimeEndStr) {
        this.lockTimeEndStr = lockTimeEndStr;
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

    public Paginator getPaginator() {
        return paginator;
    }

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    public List<LoginErrorLockUserExtend> getLstLockUser() {
        if (lstLockUser == null) {
            lstLockUser = Lists.newArrayList();
        }
        return lstLockUser;
    }

    public void setLstLockUser(List<LoginErrorLockUserExtend> lstLockUser) {
        this.lstLockUser = lstLockUser;
    }

    public Integer getIsFront() {
        return isFront;
    }

    public void setIsFront(Integer isFront) {
        this.isFront = isFront;
    }
}
