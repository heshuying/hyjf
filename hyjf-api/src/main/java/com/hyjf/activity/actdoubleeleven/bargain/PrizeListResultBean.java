package com.hyjf.activity.actdoubleeleven.bargain;

import java.util.List;

import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.mybatis.model.customize.act.ActNovPrizeCustomize;

public class PrizeListResultBean extends BaseResultBean {
    
    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = -9174539916567994537L;
    
    private String actStartTime;
    
    private String actEndtime;
    
    private String nowTime;
    
    private List<ActNovPrizeCustomize> dataList;

	public String getActStartTime() {
		return actStartTime;
	}

	public void setActStartTime(String actStartTime) {
		this.actStartTime = actStartTime;
	}

	public String getActEndtime() {
		return actEndtime;
	}

	public void setActEndtime(String actEndtime) {
		this.actEndtime = actEndtime;
	}

	public String getNowTime() {
		return nowTime;
	}

	public void setNowTime(String nowTime) {
		this.nowTime = nowTime;
	}

	public List<ActNovPrizeCustomize> getDataList() {
		return dataList;
	}

	public void setDataList(List<ActNovPrizeCustomize> dataList) {
		this.dataList = dataList;
	}

	@Override
	public String toString() {
		return super.toString() + "PrizeListResultBean [actStartTime=" + actStartTime + ", actEndtime=" + actEndtime + ", nowTime="
				+ nowTime + ", dataList=" + dataList + "]";
	}
    
    
    
}
