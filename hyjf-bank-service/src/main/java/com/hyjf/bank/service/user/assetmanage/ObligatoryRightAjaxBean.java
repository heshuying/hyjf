package com.hyjf.bank.service.user.assetmanage;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.web.CurrentHoldObligatoryRightListCustomize;
import com.hyjf.mybatis.model.customize.web.RepayMentListCustomize;
import com.hyjf.mybatis.model.customize.web.TenderCreditDetailCustomize;

public class ObligatoryRightAjaxBean implements Serializable {

	private static final long serialVersionUID = 3278149257478770256L;
	
	//当前持有债权列表
	private List<CurrentHoldObligatoryRightListCustomize> currentHoldObligatoryRightList;
	//已回款债权列表
	private List<RepayMentListCustomize> repayMentList;
	//转让记录列表
	private List<TenderCreditDetailCustomize> creditRecordList;
	
	//当前持有债权数量
	private Integer currentHoldObligatoryRightCount=0;
	//已回款债权数量
	private Integer repayMentCount=0;
	//转让债权数量
	private Integer tenderCreditDetailCount=0;
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

    public List<CurrentHoldObligatoryRightListCustomize> getCurrentHoldObligatoryRightList() {
        return currentHoldObligatoryRightList;
    }

    public void setCurrentHoldObligatoryRightList(
        List<CurrentHoldObligatoryRightListCustomize> currentHoldObligatoryRightList) {
        this.currentHoldObligatoryRightList = currentHoldObligatoryRightList;
    }

    public Integer getCurrentHoldObligatoryRightCount() {
        return currentHoldObligatoryRightCount;
    }

    public void setCurrentHoldObligatoryRightCount(Integer currentHoldObligatoryRightCount) {
        this.currentHoldObligatoryRightCount = currentHoldObligatoryRightCount;
    }

    public Integer getRepayMentCount() {
        return repayMentCount;
    }

    public void setRepayMentCount(Integer repayMentCount) {
        this.repayMentCount = repayMentCount;
    }

    public Integer getTenderCreditDetailCount() {
        return tenderCreditDetailCount;
    }

    public void setTenderCreditDetailCount(Integer tenderCreditDetailCount) {
        this.tenderCreditDetailCount = tenderCreditDetailCount;
    }

    public List<RepayMentListCustomize> getRepayMentList() {
        return repayMentList;
    }

    public void setRepayMentList(List<RepayMentListCustomize> repayMentList) {
        this.repayMentList = repayMentList;
    }

	public List<TenderCreditDetailCustomize> getCreditRecordList() {
		return creditRecordList;
	}

	public void setCreditRecordList(List<TenderCreditDetailCustomize> creditRecordList) {
		this.creditRecordList = creditRecordList;
	}


	

}
