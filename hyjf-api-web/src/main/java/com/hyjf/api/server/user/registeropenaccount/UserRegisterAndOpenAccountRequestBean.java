package com.hyjf.api.server.user.registeropenaccount;

import com.hyjf.base.bean.BaseBean;

/**
 * 外部服务接口:用户注册加开户请求Bean
 *
 * @author liuyang
 */
public class UserRegisterAndOpenAccountRequestBean extends BaseBean {
    // 手机号
    private String mobile;
    // 姓名
    private String trueName;
    // 身份证号
    private String idCard;
    // 银行卡号
    private String cardNo;

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTrueName() {
        return this.trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getIdCard() {
        return this.idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getCardNo() {
        return this.cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }


}
