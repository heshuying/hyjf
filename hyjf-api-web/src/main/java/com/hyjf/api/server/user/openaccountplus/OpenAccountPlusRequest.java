package com.hyjf.api.server.user.openaccountplus;

import com.hyjf.base.bean.BaseBean;

/**
 * 用户开户Bean
 *
 * Created by yaoyong on 2017/11/29.
 */
public class OpenAccountPlusRequest extends BaseBean {

    //手机号
    private String mobile;
    //姓名
    private String trueName;
    //身份证号
    private String idNo;
    //推荐人
    private String referee;
    //第三方平台用户id
    private Long bindUniqueId;
    // 第三方操作平台
    private String utmId;

    public String getUtmId() {
        return utmId;
    }

    public void setUtmId(String utmId) {
        this.utmId = utmId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getReferee() {
        return referee;
    }

    public void setReferee(String referee) {
        this.referee = referee;
    }

    public Long getBindUniqueId() {
        return bindUniqueId;
    }

    public void setBindUniqueId(Long bindUniqueId) {
        this.bindUniqueId = bindUniqueId;
    }

}
