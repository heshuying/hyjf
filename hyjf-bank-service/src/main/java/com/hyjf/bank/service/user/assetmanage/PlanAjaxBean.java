package com.hyjf.bank.service.user.assetmanage;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.web.CurrentHoldPlanListCustomize;
import com.hyjf.mybatis.model.customize.web.RepayMentPlanListCustomize;

public class PlanAjaxBean implements Serializable {

	private static final long serialVersionUID = 3278149257478770256L;
	
	//当前持有计划列表
	private List<CurrentHoldPlanListCustomize> currentHoldPlanList;
	//已回款计划列表
	private List<RepayMentPlanListCustomize> repayMentPlanList;
	//当前持有计划数量
	private Integer currentHoldPlanCount=0;
	//已回款计划数量
	private Integer repayMentPlanCount=0;

	// 请求处理是否成功
    private boolean status = false;

    // 分页信息
    private Paginator paginator;

    // web服务地址
    private String host;

    // 返回信息
    private String message;

    // 错误码
    private String errorCode;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void success() {
        this.status = true;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Paginator getPaginator() {
        return paginator;
    }

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public List<CurrentHoldPlanListCustomize> getCurrentHoldPlanList() {
        return currentHoldPlanList;
    }

    public void setCurrentHoldPlanList(List<CurrentHoldPlanListCustomize> currentHoldPlanList) {
        this.currentHoldPlanList = currentHoldPlanList;
    }

    public Integer getCurrentHoldPlanCount() {
        return currentHoldPlanCount;
    }

    public void setCurrentHoldPlanCount(Integer currentHoldPlanCount) {
        this.currentHoldPlanCount = currentHoldPlanCount;
    }

    
    public Integer getRepayMentPlanCount() {
        return repayMentPlanCount;
    }

    public void setRepayMentPlanCount(Integer repayMentPlanCount) {
        this.repayMentPlanCount = repayMentPlanCount;
    }

    public List<RepayMentPlanListCustomize> getRepayMentPlanList() {
        return repayMentPlanList;
    }

    public void setRepayMentPlanList(List<RepayMentPlanListCustomize> repayMentPlanList) {
        this.repayMentPlanList = repayMentPlanList;
    }

    

}
