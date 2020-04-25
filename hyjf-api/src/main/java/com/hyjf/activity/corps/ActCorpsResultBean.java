package com.hyjf.activity.corps;


import java.util.List;

import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.mybatis.model.auto.ActdecCorps;
import com.hyjf.mybatis.model.auto.ActdecWinning;

public class ActCorpsResultBean extends BaseResultBean{

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 1L;
    int corpsId;
    public int getCorpsId() {
		return corpsId;
	}
	public void setCorpsId(int corpsId) {
		this.corpsId = corpsId;
	}
	 String captainOpid;

     public String getCaptainOpid() {
		return captainOpid;
	}
	public void setCaptainOpid(String captainOpid) {
		this.captainOpid = captainOpid;
	}
	public String getCaptainName() {
		return captainName;
	}
	public void setCaptainName(String captainName) {
		this.captainName = captainName;
	}
	String captainName;
	int amount;

	//战队列表
    List<ActdecCorps> actdecCorps;
    //加入战队
    List<ActdecCorps> actdecCorps2;
    //红包多多
    List<ActdecCorps> actdecCorps3;

	//苹果多多
    List<ActdecCorps> actdecCorps4;
    //领取红包列表
    List<ActdecWinning> actdecWinning;
    
    
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public List<ActdecCorps> getActdecCorps() {
		return actdecCorps;
	}
	public void setActdecCorps(List<ActdecCorps> actdecCorps) {
		this.actdecCorps = actdecCorps;
	}
	public List<ActdecWinning> getActdecWinning() {
		return actdecWinning;
	}
	public void setActdecWinning(List<ActdecWinning> actdecWinning) {
		this.actdecWinning = actdecWinning;
	}
    public List<ActdecCorps> getActdecCorps2() {
		return actdecCorps2;
	}
	public void setActdecCorps2(List<ActdecCorps> actdecCorps2) {
		this.actdecCorps2 = actdecCorps2;
	}
	public List<ActdecCorps> getActdecCorps3() {
		return actdecCorps3;
	}
	public void setActdecCorps3(List<ActdecCorps> actdecCorps3) {
		this.actdecCorps3 = actdecCorps3;
	}
	public List<ActdecCorps> getActdecCorps4() {
		return actdecCorps4;
	}
	public void setActdecCorps4(List<ActdecCorps> actdecCorps4) {
		this.actdecCorps4 = actdecCorps4;
	}

}
