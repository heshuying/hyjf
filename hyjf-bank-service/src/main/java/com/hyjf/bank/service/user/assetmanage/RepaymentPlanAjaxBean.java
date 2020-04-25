package com.hyjf.bank.service.user.assetmanage;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.web.CurrentHoldRepayMentPlanDetailsCustomize;
import com.hyjf.mybatis.model.customize.web.CurrentHoldRepayMentPlanListCustomize;

public class RepaymentPlanAjaxBean implements Serializable {

	private static final long serialVersionUID = 3278149257478770256L;
	
	//当前持有计划列表
	private List<CurrentHoldRepayMentPlanListCustomize> currentHoldRepayMentPlanList;
	//当前持有计划数量
	private CurrentHoldRepayMentPlanDetailsCustomize currentHoldRepayMentPlanDetails;

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

   

    public List<CurrentHoldRepayMentPlanListCustomize> getCurrentHoldRepayMentPlanList() {
        return currentHoldRepayMentPlanList;
    }

    public void setCurrentHoldRepayMentPlanList(List<CurrentHoldRepayMentPlanListCustomize> currentHoldRepayMentPlanList) {
        this.currentHoldRepayMentPlanList = currentHoldRepayMentPlanList;
    }

    public CurrentHoldRepayMentPlanDetailsCustomize getCurrentHoldRepayMentPlanDetails() {
        return currentHoldRepayMentPlanDetails;
    }

    public void setCurrentHoldRepayMentPlanDetails(CurrentHoldRepayMentPlanDetailsCustomize currentHoldRepayMentPlanDetails) {
        this.currentHoldRepayMentPlanDetails = currentHoldRepayMentPlanDetails;
    }




}
