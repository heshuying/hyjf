package com.hyjf.wechat.controller.user.bankwithdraw;

import com.google.common.base.Objects;
import com.hyjf.wechat.base.BaseMapBean;

/**
 * 微信端提现请求参数封装对象
 * Created by cuigq on 2018/2/27.
 */
public class WxBankWithdrawQO extends BaseMapBean{

    // 订单号
    private String order;

    //原始交易金额
    private String transAmt;

    //银行卡号
    private String cardNo;

    // 银行联号
    private String openCardBankCode;

    //短信验证码
    private String srvAuthCode;
    
    private String verificationType;
    
    private String mobile;
    

    public String getVerificationType() {
		return verificationType;
	}

	public void setVerificationType(String verificationType) {
		this.verificationType = verificationType;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getTransAmt() {
        return transAmt;
    }

    public void setTransAmt(String transAmt) {
        this.transAmt = transAmt;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getOpenCardBankCode() {
        return openCardBankCode;
    }

    public void setOpenCardBankCode(String openCardBankCode) {
        this.openCardBankCode = openCardBankCode;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("order", order).add("transAmt", transAmt).add("cardNo", cardNo).add("openCardBankCode", openCardBankCode).toString();
    }

	public String getSrvAuthCode() {
		return srvAuthCode;
	}

	public void setSrvAuthCode(String srvAuthCode) {
		this.srvAuthCode = srvAuthCode;
	}
}
