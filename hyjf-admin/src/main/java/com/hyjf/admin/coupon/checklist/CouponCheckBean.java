package com.hyjf.admin.coupon.checklist;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.CouponCheck;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author lisheng
 * @version CouponCheckBean, v0.1 2018/6/6 16:22
 */

public class CouponCheckBean implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 387630498860089653L;

    private String id ;
    /**
     * 翻页机能用的隐藏变量
     */
    private int paginatorPage = 1;
    private String filePath;
    /**
     * 列表画面自定义标签上的用翻页对象：paginator
     */
    private Paginator paginator;
    /**
     * 列表
     */
    private List<CouponCheck> recordList;

    /**
     * 添加时间
     */
    private Date createTime;
    /**
     * 发放状态
     */
    private String status;
    /**
     * 审核备注
     */
    private String mark;
    /**
     * 检索条件 添加开始时间
     */
    private String timeStartAddSrch;

    /**
     * 检索条件 添加结束时间
     */
    private String timeEndAddSrch;

    public String getId() {
        return id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public List<CouponCheck> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<CouponCheck> recordList) {
        this.recordList = recordList;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimeStartAddSrch() {
        return timeStartAddSrch;
    }

    public void setTimeStartAddSrch(String timeStartAddSrch) {
        this.timeStartAddSrch = timeStartAddSrch;
    }

    public String getTimeEndAddSrch() {
        return timeEndAddSrch;
    }

    public void setTimeEndAddSrch(String timeEndAddSrch) {
        this.timeEndAddSrch = timeEndAddSrch;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}
