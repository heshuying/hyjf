package com.hyjf.web.user.appoint;

import java.io.Serializable;
import java.util.List;

import com.hyjf.mybatis.model.customize.web.WebBorrowAppointCustomize;
import com.hyjf.web.WebBaseAjaxResultBean;

public class AppointListAjaxBean extends WebBaseAjaxResultBean implements Serializable {

	private static final long serialVersionUID = 3278149257478770256L;
	
	private String appointStatus;
	
	private String recordTotal;

	private List<WebBorrowAppointCustomize> appointlist;

	public String getAppointStatus() {
		return appointStatus;
	}

	public void setAppointStatus(String appointStatus) {
		this.appointStatus = appointStatus;
	}

	public String getRecordTotal() {
		return recordTotal;
	}

	public void setRecordTotal(String recordTotal) {
		this.recordTotal = recordTotal;
	}

	public List<WebBorrowAppointCustomize> getAppointlist() {
		return appointlist;
	}

	public void setAppointlist(List<WebBorrowAppointCustomize> appointlist) {
		this.appointlist = appointlist;
	}


}
