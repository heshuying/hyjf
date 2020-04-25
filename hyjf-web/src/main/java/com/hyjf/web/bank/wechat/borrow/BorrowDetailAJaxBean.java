/**
 * Description:项目列表查询所用vo
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.web.bank.wechat.borrow;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.hyjf.bank.service.borrow.BorrowFileCustomBean;
import com.hyjf.bank.service.borrow.BorrowRepayPlanCustomBean;
import com.hyjf.mybatis.model.customize.web.WebHzcDisposalPlanCustomize;
import com.hyjf.mybatis.model.customize.web.WebHzcProjectDetailCustomize;
import com.hyjf.mybatis.model.customize.web.WebMortgageCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectAuthenInfoCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectCompanyDetailCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectDetailCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectPersonDetailCustomize;
import com.hyjf.mybatis.model.customize.web.WebRiskControlCustomize;
import com.hyjf.mybatis.model.customize.web.WebVehiclePledgeCustomize;
import com.hyjf.web.WeChatBaseAjaxResultBean;

public class BorrowDetailAJaxBean extends WeChatBaseAjaxResultBean implements Serializable {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = -7415746042913857105L;

	private WebProjectDetailCustomize projectDeatil;

	private WebHzcProjectDetailCustomize borrowInfo;

	private WebHzcDisposalPlanCustomize disposalPlan;

	private List<BorrowRepayPlanCustomBean> repayPlanList;

	private List<BorrowFileCustomBean> files;

	private WebProjectCompanyDetailCustomize companyDetail;

	private WebProjectPersonDetailCustomize personDetail;

	private WebRiskControlCustomize riskControl;

	private List<WebMortgageCustomize> mortgageList;

	private List<WebVehiclePledgeCustomize> vehiclePledgeList;

	private List<WebProjectAuthenInfoCustomize> authenList;
	
	private Map<String, Object> otherInfo;

	private String nowTime;

	public String getNowTime() {
		return nowTime;
	}

	public void setNowTime(String nowTime) {
		this.nowTime = nowTime;
	}

	public WebProjectDetailCustomize getProjectDeatil() {
		return projectDeatil;
	}

	public void setProjectDeatil(WebProjectDetailCustomize projectDeatil) {
		this.projectDeatil = projectDeatil;
	}

	public WebHzcProjectDetailCustomize getBorrowInfo() {
		return borrowInfo;
	}

	public void setBorrowInfo(WebHzcProjectDetailCustomize borrowInfo) {
		this.borrowInfo = borrowInfo;
	}

	public WebHzcDisposalPlanCustomize getDisposalPlan() {
		return disposalPlan;
	}

	public void setDisposalPlan(WebHzcDisposalPlanCustomize disposalPlan) {
		this.disposalPlan = disposalPlan;
	}

	public List<BorrowRepayPlanCustomBean> getRepayPlanList() {
		return repayPlanList;
	}

	public void setRepayPlanList(List<BorrowRepayPlanCustomBean> repayPlanList) {
		this.repayPlanList = repayPlanList;
	}

	public List<BorrowFileCustomBean> getFiles() {
		return files;
	}

	public void setFiles(List<BorrowFileCustomBean> files) {
		this.files = files;
	}

	public WebProjectCompanyDetailCustomize getCompanyDetail() {
		return companyDetail;
	}

	public void setCompanyDetail(WebProjectCompanyDetailCustomize companyDetail) {
		this.companyDetail = companyDetail;
	}

	public WebProjectPersonDetailCustomize getPersonDetail() {
		return personDetail;
	}

	public void setPersonDetail(WebProjectPersonDetailCustomize personDetail) {
		this.personDetail = personDetail;
	}

	public WebRiskControlCustomize getRiskControl() {
		return riskControl;
	}

	public void setRiskControl(WebRiskControlCustomize riskControl) {
		this.riskControl = riskControl;
	}

	public List<WebMortgageCustomize> getMortgageList() {
		return mortgageList;
	}

	public void setMortgageList(List<WebMortgageCustomize> mortgageList) {
		this.mortgageList = mortgageList;
	}

	public List<WebVehiclePledgeCustomize> getVehiclePledgeList() {
		return vehiclePledgeList;
	}

	public void setVehiclePledgeList(List<WebVehiclePledgeCustomize> vehiclePledgeList) {
		this.vehiclePledgeList = vehiclePledgeList;
	}

	public List<WebProjectAuthenInfoCustomize> getAuthenList() {
		return authenList;
	}

	public void setAuthenList(List<WebProjectAuthenInfoCustomize> authenList) {
		this.authenList = authenList;
	}

	public Map<String, Object> getOtherInfo() {
		return otherInfo;
	}

	public void setOtherInfo(Map<String, Object> otherInfo) {
		this.otherInfo = otherInfo;
	}

}
