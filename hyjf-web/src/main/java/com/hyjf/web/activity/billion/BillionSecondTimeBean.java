package com.hyjf.web.activity.billion;

import java.io.Serializable;

/**
 * 活动列表实体类
 * 
 * @author qingbing
 *
 */
public class BillionSecondTimeBean  implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 3803722754627032581L;

    /**
     * 100时间点
     */
    private Integer time100;
    /**
     * 101时间点
     */
    private Integer time101;
    /**
     * 102时间点
     */
    private Integer time102;
    /**
     * 103时间点
     */
    private Integer time103;
    /**
     * 104时间点
     */
    private Integer time104;
    /**
     * 105时间点
     */
    private Integer time105;
    /**
     * 进行到哪个阶段
     * 0、未开始
     * 1、 100-101
     * 2、 101-102
     * 3、 102-103
     * 4、 103-104
     * 5、 104-105
     */
    private Integer stage;
    /**
     * 最小阶段
     * 0、未开始
     * 1、 100-101
     * 2、 101-102
     * 3、 102-103
     * 4、 103-104
     * 5、 104-105
     */
    private Integer minStage;
    /**
     * 当前时间
     */
    private Integer nowTime;
    /**
     * 活动是否已结束
     * 1  结束
     * 0 未结束
     */
    private Integer isEnd;
    
    
    
	public Integer getTime100() {
		return time100;
	}
	public void setTime100(Integer time100) {
		this.time100 = time100;
	}
	public Integer getTime101() {
		return time101;
	}
	public void setTime101(Integer time101) {
		this.time101 = time101;
	}
	public Integer getTime102() {
		return time102;
	}
	public void setTime102(Integer time102) {
		this.time102 = time102;
	}
	public Integer getTime103() {
		return time103;
	}
	public void setTime103(Integer time103) {
		this.time103 = time103;
	}
	public Integer getTime104() {
		return time104;
	}
	public void setTime104(Integer time104) {
		this.time104 = time104;
	}
	public Integer getTime105() {
		return time105;
	}
	public void setTime105(Integer time105) {
		this.time105 = time105;
	}
	public Integer getStage() {
		return stage;
	}
	public void setStage(Integer stage) {
		this.stage = stage;
	}
	public Integer getNowTime() {
		return nowTime;
	}
	public void setNowTime(Integer nowTime) {
		this.nowTime = nowTime;
	}
	public Integer getIsEnd() {
		return isEnd;
	}
	public void setIsEnd(Integer isEnd) {
		this.isEnd = isEnd;
	}
	public Integer getMinStage() {
		return minStage;
	}
	public void setMinStage(Integer minStage) {
		this.minStage = minStage;
	}
 
}
