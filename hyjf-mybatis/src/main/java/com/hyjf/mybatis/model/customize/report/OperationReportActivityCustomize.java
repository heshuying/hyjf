package com.hyjf.mybatis.model.customize.report;

import java.io.Serializable;

import com.hyjf.mybatis.model.auto.OperationReportActivity;

public class OperationReportActivityCustomize extends OperationReportActivity implements Serializable{

		private String time;

	    private String startTime;
	
	    private String endTime;

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public String getStartTime() {
			return startTime;
		}

		public void setStartTime(String startTime) {
			this.startTime = startTime;
		}

		public String getEndTime() {
			return endTime;
		}

		public void setEndTime(String endTime) {
			this.endTime = endTime;
		}


	
}
