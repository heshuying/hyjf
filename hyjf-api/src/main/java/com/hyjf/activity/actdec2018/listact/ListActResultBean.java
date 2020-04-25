package com.hyjf.activity.actdec2018.listact;

import java.util.List;

import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.mybatis.model.auto.ActdecListedFour;
import com.hyjf.mybatis.model.auto.ActdecListedOne;

public class ListActResultBean extends BaseResultBean {

	/**
	 * 此处为属性说明
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 累计数量
	 */
	List<ActdecListedFour> alf;
	List<ActdecListedOne> alo;
	String num;
	String reward;
	int timeStart;
	int timeEnd;
	public int getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(int timeStart) {
		this.timeStart = timeStart;
	}

	public int getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(int timeEnd) {
		this.timeEnd = timeEnd;
	}

	
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public List<ActdecListedOne> getAlo() {
		return alo;
	}

	public void setAlo(List<ActdecListedOne> alo) {
		this.alo = alo;
	}

	public List<ActdecListedFour> getAlf() {
		return alf;
	}

	public void setAlf(List<ActdecListedFour> alf) {
		this.alf = alf;
	}

}
