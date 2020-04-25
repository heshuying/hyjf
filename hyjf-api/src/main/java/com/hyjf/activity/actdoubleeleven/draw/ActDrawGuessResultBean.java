package com.hyjf.activity.actdoubleeleven.draw;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.ActJanQuestions;

public class ActDrawGuessResultBean extends BaseResultBean {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -9106212219698443171L;
	// 当前时间
	private String nowDate;
	// 答题状态
	private String answerState;
	// 当前问题
	private ActJanQuestions actQuestions;
	// 活动开始时间结束时间
	private String actTime;
	
    public String getNowDate() {
        return nowDate;
    }
    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }
    public String getAnswerState() {
        return answerState;
    }
    public void setAnswerState(String answerState) {
        this.answerState = answerState;
    }
    public ActJanQuestions getActQuestions() {
        return actQuestions;
    }
    public void setActQuestions(ActJanQuestions actQuestions) {
        this.actQuestions = actQuestions;
    }
    public String getActTime() {
        return actTime;
    }
    public void setActTime(String actTime) {
        this.actTime = actTime;
    }
    // 设置错误的返回值
    public JSONObject getFailMess(String statusDesc) {
        JSONObject result = new JSONObject();
        result.put("status", BaseResultBean.STATUS_FAIL);
        result.put("statusDesc", statusDesc);
        result.put("actTime", this.actTime); // 活动时间
        result.put("nowTime",GetDate.getNowTime10()); // 活动时间
        
        return result;
    }
    // 返回未答题信息
    public JSONObject getSuccessNotAnswered() {
        JSONObject result = new JSONObject();
        result.put("status", BaseResultBean.STATUS_SUCCESS);
        result.put("answerState", ActDrawGuessDefine.STATE_NOT_ANSWERED);
        result.put("statusDesc", "未答题");
        result.put("id", this.actQuestions==null?"":this.actQuestions.getId());// 这个标志可以用来表名第几周
        result.put("title", this.actQuestions==null?"":this.actQuestions.getTitle());
        result.put("timeStart", this.actQuestions==null?"":this.actQuestions.getTimeStart());// 开始时间
        result.put("timeEnd", this.actQuestions==null?"":this.actQuestions.getTimeEnd());// 结束时间
        result.put("actTime", this.actTime); // 活动时间
        result.put("nowTime",GetDate.getNowTime10()); // 活动时间
        
        return result;
    }
    // 已答题
    public JSONObject getSuccessAnswered() {
        JSONObject result = new JSONObject();
        result.put("status", BaseResultBean.STATUS_SUCCESS);
        result.put("answerState", ActDrawGuessDefine.STATE_ANSWERED);
        result.put("statusDesc", "已答题");
        result.put("timeStart", this.actQuestions==null?"":this.actQuestions.getTimeStart());// 开始时间
        result.put("timeEnd", this.actQuestions==null?"":this.actQuestions.getTimeEnd());// 结束时间
        result.put("id", this.actQuestions==null?"":this.actQuestions.getId());// 这个标志可以用来表名第几周
        result.put("actTime", this.actTime); // 活动时间
        result.put("nowTime",GetDate.getNowTime10()); // 活动时间
        
        return result;
    }
    
    public JSONObject getFailAnswered() {
        JSONObject result = new JSONObject();
        result.put("status", BaseResultBean.STATUS_FAIL);
        result.put("answerState", ActDrawGuessDefine.STATE_ANSWERED);
        result.put("statusDesc", "已答题");
        result.put("timeStart", this.actQuestions==null?"":this.actQuestions.getTimeStart());// 开始时间
        result.put("timeEnd", this.actQuestions==null?"":this.actQuestions.getTimeEnd());// 结束时间
        result.put("id", this.actQuestions==null?"":this.actQuestions.getId());// 这个标志可以用来表名第几周
        result.put("actTime", this.actTime); // 活动时间
        result.put("nowTime",GetDate.getNowTime10()); // 活动时间
        
        return result;
    }
    
    public JSONObject getSuccessMess(String mess) {
        JSONObject result = new JSONObject();
        result.put("status", BaseResultBean.STATUS_SUCCESS);
        result.put("statusDesc", mess);
        result.put("actTime", this.actTime); // 活动时间
        result.put("nowTime",GetDate.getNowTime10()); // 活动时间
        
        return result;
    }
     public JSONObject getFailMess(String mess, int status) {
        JSONObject result = new JSONObject();
        result.put("status", status);
        result.put("statusDesc", mess);
        result.put("nowTime",GetDate.getNowTime10()); // 活动时间
        
        return result;
    }
     
     public static void main(String[] args) {
        System.out.println(GetDate.getNowTime10());
    }
}
