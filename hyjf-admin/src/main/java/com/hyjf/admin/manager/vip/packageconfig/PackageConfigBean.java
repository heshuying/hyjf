package com.hyjf.admin.manager.vip.packageconfig;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.VipAuth;
import com.hyjf.mybatis.model.customize.admin.VipAuthCustomize;

/**
 * 文章管理实体类
 * 
 * @author 
 *
 */
public class PackageConfigBean extends VipAuth implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 3803722754627032581L;

    /**
     * 前台时间接收
     */
    private String ids;
    
    private List<VipAuth> vipAuths;

    private String couponType;
	private String startCreate;

    private String endCreate;


    public String getStartCreate() {
        return startCreate;
    }

    public void setStartCreate(String startCreate) {
        this.startCreate = startCreate;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getEndCreate() {
        return endCreate;
    }

    public void setEndCreate(String endCreate) {
        this.endCreate = endCreate;
    }

    private List<VipAuthCustomize> recordList;

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

    public List<VipAuth> getVipAuths() {
        return vipAuths;
    }

    public void setVipAuths(List<VipAuth> vipAuths) {
        this.vipAuths = vipAuths;
    }

    public List<VipAuthCustomize> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<VipAuthCustomize> recordList) {
        this.recordList = recordList;
    }

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }





}
