/*
 * @Copyright: 2005-2018 so_what. All rights reserved.
 */
package com.hyjf.mybatis.model.customize.cert;

import com.hyjf.mybatis.model.auto.CertBorrow;

import java.util.List;

/**
 * @Description 数据库批量操作
 * @Author sss
 * @Date 2018/12/12 9:34
 */
public class CertBorrowUpdate {

    /**
     * 要修改的对象的ID集合
     */
    private List<Integer> ids ;
    /**
     * 自己new一个要修改哪个字段set哪个字段
     */
    private CertBorrow certBorrow;

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public CertBorrow getCertBorrow() {
        return certBorrow;
    }

    public void setCertBorrow(CertBorrow certBorrow) {
        this.certBorrow = certBorrow;
    }
}
