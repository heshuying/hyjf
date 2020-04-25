package com.hyjf.wechat.controller.hjh.vo;

import com.hyjf.wechat.base.BaseResultBean;

public class HjhTenderResultBean extends BaseResultBean{
    
	private static final long serialVersionUID = 8712206946181379696L;
	
	private String sign;
	
	private String url;
	//用户测评等级
	private String evalType;

	private String revaluationMoney;
	//项目测评等级
	private String evalFlagType;

	private String revaluationMoneyPrincipal;

	public String getRevaluationMoneyPrincipal() {
		return revaluationMoneyPrincipal;
	}

	public void setRevaluationMoneyPrincipal(String revaluationMoneyPrincipal) {
		this.revaluationMoneyPrincipal = revaluationMoneyPrincipal;
	}

	public String getEvalType() {
		return evalType;
	}

	public void setEvalType(String evalType) {
		this.evalType = evalType;
	}

	public String getRevaluationMoney() {
		return revaluationMoney;
	}

	public void setRevaluationMoney(String revaluationMoney) {
		this.revaluationMoney = revaluationMoney;
	}

	public String getSign() {
        return sign;
    }
    public void setSign(String sign) {
        this.sign = sign;
    }
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public String getEvalFlagType() {
		return evalFlagType;
	}

	public void setEvalFlagType(String evalFlagType) {
		this.evalFlagType = evalFlagType;
	}
	
}
