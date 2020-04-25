package com.hyjf.mybatis.model.customize;

import java.io.Serializable;
import java.util.Date;


/**
 * 
 * 传入参数的时间内累
 * @author Administrator
 *
 */
public class TimeToolsBean implements Serializable{
	private Date yuechu;
	private Date yuemo;
	public Date getYuechu() {
		return yuechu;
	}
	public void setYuechu(Date yuechu) {
		this.yuechu = yuechu;
	}
	public Date getYuemo() {
		return yuemo;
	}
	public void setYuemo(Date yuemo) {
		this.yuemo = yuemo;
	}
	
}
