package com.hyjf.api.server.user.nonwithdraw;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.base.bean.BaseResultBean;

/**
 * 
 * 外部服务接口:用户免密提现结果Bean
 * @author hyjf
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年9月6日
 * @see 下午4:34:09
 */
public class NonCashWithdrawResultBean extends BaseResultBean {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -9106212219698443171L;
	// 交易金额
	private String amt;
	// 到账金额
	private String arrivalAmount;
	// 提现手续费
	private String fee;
	// orderId
    private String orderId;

	public String getAmt() {
		return amt;
	}

	public void setAmt(String amt) {
		this.amt = amt;
	}

	public String getArrivalAmount() {
		return arrivalAmount;
	}

	public void setArrivalAmount(String arrivalAmount) {
		this.arrivalAmount = arrivalAmount;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
	 * 
	 * 只返回了状态和描述
	 * @author sunss
	 * @return
	 */
    public JSONObject toJson() {
        JSONObject jso = new JSONObject();
        jso.put("status", this.getStatus());
        jso.put("statusDesc", this.getStatusDesc());
        jso.put("chkValue", this.getChkValue());
        return jso;
    }

    /**
     * 
     * 返回成功的信息
     * @author sunss
     * @return
     */
    public JSONObject toSuccessJson() {
        JSONObject jso = new JSONObject();
        jso.put("status", this.getStatus());
        jso.put("statusDesc", this.getStatusDesc());
        jso.put("amt", this.getAmt());// 交易金额
        jso.put("arrivalAmount", this.getArrivalAmount());// 到账金额
        jso.put("orderId", this.getOrderId());// orderId
        jso.put("fee", this.getFee());// 提现手续费
        jso.put("chkValue", this.getChkValue());
        return jso;
    }

}
