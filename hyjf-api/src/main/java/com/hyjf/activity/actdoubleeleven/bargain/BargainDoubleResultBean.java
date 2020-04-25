package com.hyjf.activity.actdoubleeleven.bargain;

import java.math.BigDecimal;

import com.hyjf.base.bean.BaseResultBean;

public class BargainDoubleResultBean extends BaseResultBean {
    
    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = -9174539916567994537L;
    
    //砍价记录id
	private Integer idBargain;

	private Integer prizeId;

	private String prizeName;
	
	//砍掉金额
	private BigDecimal prizeBargain;

	public Integer getIdBargain() {
		return idBargain;
	}

	public void setIdBargain(Integer idBargain) {
		this.idBargain = idBargain;
	}

	public Integer getPrizeId() {
		return prizeId;
	}

	public void setPrizeId(Integer prizeId) {
		this.prizeId = prizeId;
	}

	public String getPrizeName() {
		return prizeName;
	}

	public void setPrizeName(String prizeName) {
		this.prizeName = prizeName;
	}

	public BigDecimal getPrizeBargain() {
		return prizeBargain;
	}

	public void setPrizeBargain(BigDecimal prizeBargain) {
		this.prizeBargain = prizeBargain;
	}

	@Override
	public String toString() {
		return super.toString() + "BargainDoubleResultBean [idBargain=" + idBargain + ", prizeId=" + prizeId + ", prizeName=" + prizeName
				+ ", prizeBargain=" + prizeBargain + "]";
	}


    
    
}
