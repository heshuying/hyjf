package com.hyjf.web.other;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.DataSearchCustomize;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lisheng
 * @version DataSearchBean, v0.1 2018/7/4 11:50
 */

public class DataSearchBean  implements Serializable {
    private String mobile;
    private String addTimeStart;
    private String addTimeEnd;
    private String regTimeStart;
    private String regTimeEnd;
    private String Type;
    private String code;
    private String truename;
    private String username;
    private String reffername;
    private Integer limitStart;
    private Integer limitEnd;
    // 请求处理是否成功
    private boolean status = false;
    private List<DataSearchCustomize> list;

    private Map<String,Object> money;

    public void success() {
        this.status = true;
    }
    /**
     * 翻页机能用的隐藏变量
     */
    private int paginatorPage = 1;
    /**
     * 翻页功能所用分页大小
     */
    private int pageSize = 10;
    /**
     * 列表画面自定义标签上的用翻页对象：paginator
     */
    private Paginator paginator;

    public DataSearchBean() {
        super();
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

    public int getPageSize() {
        if (pageSize == 0) {
            pageSize = 10;
        }
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }



    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getMobile() {
        return mobile;
    }


    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddTimeStart() {
        return addTimeStart;
    }

    public void setAddTimeStart(String addTimeStart) {
        this.addTimeStart = addTimeStart;
    }

    public String getAddTimeEnd() {
        return addTimeEnd;
    }

    public void setAddTimeEnd(String addTimeEnd) {
        this.addTimeEnd = addTimeEnd;
    }

    public String getRegTimeStart() {
        return regTimeStart;
    }

    public void setRegTimeStart(String regTimeStart) {
        this.regTimeStart = regTimeStart;
    }

    public String getRegTimeEnd() {
        return regTimeEnd;
    }

    public void setRegTimeEnd(String regTimeEnd) {
        this.regTimeEnd = regTimeEnd;
    }


    public String getType() {
        return Type;
    }


    public void setType(String type) {
        Type = type;
    }

    public Integer getLimitStart() {
        return limitStart;
    }

    public void setLimitStart(Integer limitStart) {
        this.limitStart = limitStart;
    }

    public Integer getLimitEnd() {
        return limitEnd;
    }

    public void setLimitEnd(Integer limitEnd) {
        this.limitEnd = limitEnd;
    }

    public List<DataSearchCustomize> getList() {
        return list;
    }

    public void setList(List<DataSearchCustomize> list) {
        this.list = list;
    }

    public Map<String, Object> getMoney() {
        return money;
    }

    public void setMoney(Map<String, Object> money) {
        this.money = money;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getReffername() {
        return reffername;
    }

    public void setReffername(String reffername) {
        this.reffername = reffername;
    }
}
