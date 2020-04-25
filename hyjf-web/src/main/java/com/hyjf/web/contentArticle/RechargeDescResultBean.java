package com.hyjf.web.contentArticle;

import java.util.ArrayList;
import java.util.List;

import com.hyjf.web.WebBaseAjaxResultBean;


/**
 * @author jjj
 * @version AppRechargeDescResultBean, v0.1 2017/12/13 20:53 充值限额说明
 */
public class RechargeDescResultBean extends WebBaseAjaxResultBean {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 充值限额说明
	private List<RechargeLimitAmountDesc> list;

	public RechargeDescResultBean(){
		list = new ArrayList<>();
	}

	static class RechargeLimitAmountDesc {
		private String bankName;
		private String once;
		private String day;
		private String month;

		public String getBankName() {
			return bankName;
		}

		public void setBankName(String bankName) {
			this.bankName = bankName;
		}

		public String getOnce() {
			return once;
		}

		public void setOnce(String once) {
			this.once = once;
		}

		public String getDay() {
			return day;
		}

		public void setDay(String day) {
			this.day = day;
		}

		public String getMonth() {
			return month;
		}

		public void setMonth(String month) {
			this.month = month;
		}
	}

	public List<RechargeLimitAmountDesc> getList() {
		return list;
	}

	public void setList(List<RechargeLimitAmountDesc> list) {
		this.list = list;
	}
}
