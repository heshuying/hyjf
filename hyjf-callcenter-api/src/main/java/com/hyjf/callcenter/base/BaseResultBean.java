package com.hyjf.callcenter.base;

/**
 * 呼叫中心用接口返回数据的基类
 * @author 刘彬
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年07月15日 
 * @see 下午2:23:01
 */
public class BaseResultBean {
    
	public static final String STATUS_SUCCESS = "0";
	
	public static final String STATUS_FAIL = "1";
	
	public static final String STATUS_DESC_SUCCESS = "成功";
	
	public static final String STATUS_DESC_FAIL = "失败";

    /**
     * 此处为属性说明
     */
    //private static final long serialVersionUID = -3589570872364671096L;

	/**
	 * 状态值
	 */
    private String status;
	/**
	 * 状态描述
	 */
    private String statusDesc;
	/**
	 * 总记录数
	 */
    private String totalRecords;
    
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusDesc() {
		return statusDesc;
	}
	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
	public String getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(String totalRecords) {
		this.totalRecords = totalRecords;
	}
    /**
     * 设定返回状态和状态信息
     * @param status
     * @param statusDesc
     */
	public void statusMessage(String status,String statusDesc) {
		this.status = status;
		this.statusDesc = statusDesc;
	}
}
