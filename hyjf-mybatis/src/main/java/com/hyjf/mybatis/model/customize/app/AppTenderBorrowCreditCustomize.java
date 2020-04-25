package com.hyjf.mybatis.model.customize.app;

/**
 * 
 * 债权转让提交实体类
 * @author hyjf
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年8月8日
 * @see 下午2:18:29
 */
public class AppTenderBorrowCreditCustomize extends AppTenderCreditCustomize {

    /**
     * serialVersionUID:
     */

    private static final long serialVersionUID = 1L;

    /**
     * 折价率
     */
    private String creditDiscount;

    /**
     * 转让价格
     */
    private String creditPrice;

    /**
     * 服务费
     */
    private String creditFee;

    /**
     * 手机验证码
     */
    private String telcode;

    public String getCreditDiscount() {
        return creditDiscount;
    }

    public void setCreditDiscount(String creditDiscount) {
        this.creditDiscount = creditDiscount;
    }

    public String getCreditPrice() {
        return creditPrice;
    }

    public void setCreditPrice(String creditPrice) {
        this.creditPrice = creditPrice;
    }

    public String getCreditFee() {
        return creditFee;
    }

    public void setCreditFee(String creditFee) {
        this.creditFee = creditFee;
    }

    public String getTelcode() {
        return telcode;
    }

    public void setTelcode(String telcode) {
        this.telcode = telcode;
    }
}
