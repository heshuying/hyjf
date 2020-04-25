package com.hyjf.admin.manager.user.certificateauthority;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.CertificateAuthority;
import org.apache.poi.hssf.record.formula.functions.T;

import java.io.Serializable;
import java.util.List;

/**
 * CA认证记录Bean
 *
 * @author liuyang
 */
public class FddCertificateAuthorityBean implements Serializable {

    private static final long serialVersionUID = -2585206898072754969L;

    // 用户名(检索用)
    private String userNameSrch;
    // 手机号(检索用)
    private String mobileSrch;
    // 姓名(检索用)
    private String trueNameSrch;
    // 用户类型(检索用)
    private String idTypeSrch;
    // 状态(检索用)
    private String statusSrch;
    // 检索开始时间
    private String startTimeSrch;
    // 检索结束时间
    private String endTimeSrch;
    // 客户编号(检索用)
    private String customerIdSrch;
    // 检索结果
    private List<CertificateAuthority> recordList;
    /**
     * 翻页机能用的隐藏变量
     */
    private int paginatorPage = 1;

    /**
     * 列表画面自定义标签上的用翻页对象：paginator
     */
    private Paginator paginator;

    /**
     * 检索条件 limitStart
     */
    private int limitStart = -1;

    /**
     * 检索条件 limitEnd
     */
    private int limitEnd = -1;


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
    public String getUserNameSrch() {
        return userNameSrch;
    }

    public void setUserNameSrch(String userNameSrch) {
        this.userNameSrch = userNameSrch;
    }

    public String getMobileSrch() {
        return mobileSrch;
    }

    public void setMobileSrch(String mobileSrch) {
        this.mobileSrch = mobileSrch;
    }

    public String getTrueNameSrch() {
        return trueNameSrch;
    }

    public void setTrueNameSrch(String trueNameSrch) {
        this.trueNameSrch = trueNameSrch;
    }

    public String getStatusSrch() {
        return statusSrch;
    }

    public void setStatusSrch(String statusSrch) {
        this.statusSrch = statusSrch;
    }

    public String getStartTimeSrch() {
        return startTimeSrch;
    }

    public void setStartTimeSrch(String startTimeSrch) {
        this.startTimeSrch = startTimeSrch;
    }

    public String getEndTimeSrch() {
        return endTimeSrch;
    }

    public void setEndTimeSrch(String endTimeSrch) {
        this.endTimeSrch = endTimeSrch;
    }

    public String getCustomerIdSrch() {
        return customerIdSrch;
    }

    public void setCustomerIdSrch(String customerIdSrch) {
        this.customerIdSrch = customerIdSrch;
    }

    public List<CertificateAuthority> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<CertificateAuthority> recordList) {
        this.recordList = recordList;
    }

    public String getIdTypeSrch() {
        return idTypeSrch;
    }

    public void setIdTypeSrch(String idTypeSrch) {
        this.idTypeSrch = idTypeSrch;
    }
}
