package com.hyjf.activity.actten.act2;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.mybatis.model.auto.ActQuestions;

public class ActQuestionTenResultBean extends BaseResultBean {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -9106212219698443171L;
	// 当前时间
	private String nowDate;
	// 答题状态
	private String answerState;
	// 当前问题
	private ActQuestions actQuestions;
	
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
    public ActQuestions getActQuestions() {
        return actQuestions;
    }
    public void setActQuestions(ActQuestions actQuestions) {
        this.actQuestions = actQuestions;
    }
    // 设置错误的返回值
    public JSONObject getFailMess(String statusDesc) {
        JSONObject result = new JSONObject();
        result.put("status", BaseResultBean.STATUS_FAIL);
        result.put("statusDesc", statusDesc);
        return result;
    }
    // 返回未答题信息
    public JSONObject getSuccessNotAnswered() {
        JSONObject result = new JSONObject();
        result.put("status", BaseResultBean.STATUS_SUCCESS);
        result.put("answerState", ActQuestionTenDefine.STATE_NOT_ANSWERED);
        result.put("statusDesc", "未答题");
        result.put("id", this.actQuestions.getId());// 这个标志可以用来表名第几周
        result.put("title", this.actQuestions.getTitle());
        result.put("answerA", this.actQuestions.getAnswerA());
        result.put("answerB", this.actQuestions.getAnswerB());
        result.put("answerC", this.actQuestions.getAnswerC());
        result.put("answerD", this.actQuestions.getAnswerD());
        result.put("timeStart", this.actQuestions.getTimeStart());// 开始时间
        result.put("timeEnd", this.actQuestions.getTimeEnd());// 结束时间
        
        return result;
    }
    // 已答题
    public JSONObject getSuccessAnswered() {
        JSONObject result = new JSONObject();
        result.put("status", BaseResultBean.STATUS_SUCCESS);
        result.put("answerState", ActQuestionTenDefine.STATE_ANSWERED);
        result.put("statusDesc", "已答题");
        result.put("timeStart", this.actQuestions.getTimeStart());// 开始时间
        result.put("timeEnd", this.actQuestions.getTimeEnd());// 结束时间
        result.put("id", this.actQuestions.getId());// 这个标志可以用来表名第几周
        return result;
    }
    
    public JSONObject getFailAnswered() {
        JSONObject result = new JSONObject();
        result.put("status", BaseResultBean.STATUS_FAIL);
        result.put("answerState", ActQuestionTenDefine.STATE_ANSWERED);
        result.put("statusDesc", "已答题");
        result.put("timeStart", this.actQuestions.getTimeStart());// 开始时间
        result.put("timeEnd", this.actQuestions.getTimeEnd());// 结束时间
        result.put("id", this.actQuestions.getId());// 这个标志可以用来表名第几周
        
        return result;
    }
    
    public JSONObject getSuccessMess(String mess) {
        JSONObject result = new JSONObject();
        result.put("status", BaseResultBean.STATUS_SUCCESS);
        result.put("statusDesc", mess);
        return result;
    }
    public JSONObject getFailMess(String mess, int status) {
        JSONObject result = new JSONObject();
        result.put("status", status);
        result.put("statusDesc", mess);
        return result;
    }
}
