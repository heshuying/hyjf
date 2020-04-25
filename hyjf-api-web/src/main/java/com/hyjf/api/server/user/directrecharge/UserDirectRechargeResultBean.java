package com.hyjf.api.server.user.directrecharge;

import com.hyjf.base.bean.BaseResultBean;

/**
 * 外部服务接口:用户充值
 *
 * @author liuyang
 */
public class UserDirectRechargeResultBean extends BaseResultBean {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 8988592724638111679L;

    /**
     * 交易金额
     */
    private String txAmount;
    // 充值订单号
    private String orderId;

    public String getTxAmount() {
        return txAmount;
    }

    public void setTxAmount(String txAmount) {
        this.txAmount = txAmount;
    }

    public String getOrderId() {
        return this.orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
