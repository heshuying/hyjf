package com.hyjf.activity.actten.act2;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.ActQuestions;
import com.hyjf.mybatis.model.auto.ActQuestionsAnswerlist;
import com.hyjf.mybatis.model.auto.ActQuestionsAnswerlistExample;
import com.hyjf.mybatis.model.auto.ActQuestionsExample;
import com.hyjf.mybatis.model.auto.ActQuestionsExample.Criteria;
import com.hyjf.mybatis.model.auto.ActRewardList;

@Service("actQuestionTenService")
public class ActQuestionTenServiceImpl extends BaseServiceImpl implements ActQuestionTenService {

    /**
     * 根据当前时间查询问题
     * @author sunss
     * @param resultBean
     */
    @Override
    public ActQuestions getQuestionByNowDate() {
        // 获取当前时间
        Integer nowDate = Integer.parseInt(GetDate.getDate("yyyyMMdd"));
        ActQuestionsExample example = new ActQuestionsExample();
        Criteria criteria = example.createCriteria();
        criteria.andTimeStartLessThanOrEqualTo(nowDate);
        criteria.andTimeEndGreaterThanOrEqualTo(nowDate);
        criteria.andDelFlgEqualTo(0);
        
        List<ActQuestions> list = actQuestionsMapper.selectByExample(example);
        
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据用户ID查询用户这道题是不是答过了
     * @author sunss
     * @param questionId
     * @param userId
     * @return
     * @see com.hyjf.activity.actten.act2.ActQuestionTenService#getActQuestionsAnswerListByUserId(java.lang.Integer, java.lang.Integer)
     */
    @Override
    public ActQuestionsAnswerlist getActQuestionsAnswerListByUserId(Integer questionId, Integer userId) {
        ActQuestionsAnswerlistExample example = new ActQuestionsAnswerlistExample();
        
        example.createCriteria().andQuestionIdEqualTo(questionId).andUserIdEqualTo(userId).andDelFlgEqualTo(0);
        
        List<ActQuestionsAnswerlist> list = actQuestionsAnswerlistMapper.selectByExample(example);
        
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据ID查询问题
     * @author sunss
     * @param questionId
     * @return
     * @see com.hyjf.activity.actten.act2.ActQuestionTenService#getQuestionById(java.lang.Integer)
     */
    @Override
    public ActQuestions getQuestionById(Integer questionId) {
        // 获取当前时间
        Integer nowDate = Integer.parseInt(GetDate.getDate("yyyyMMdd"));
        
        ActQuestionsExample example = new ActQuestionsExample();
        Criteria criteria = example.createCriteria();
        criteria.andTimeStartLessThanOrEqualTo(nowDate);
        criteria.andTimeEndGreaterThanOrEqualTo(nowDate);
        criteria.andDelFlgEqualTo(0);
        criteria.andIdEqualTo(questionId);
        
        List<ActQuestions> list = actQuestionsMapper.selectByExample(example);
        
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 答对了的逻辑
     * @author sunss
     * @param requestBean
     * @see com.hyjf.activity.actten.act2.ActQuestionTenService#insertActRewardlist(com.hyjf.activity.actten.act2.ActQuestionTenRequestBean)
     */
    @Override
    public void insertActRewardlist(ActQuestionTenRequestBean requestBean) {
        //插入答题记录表
        ActQuestionsAnswerlist answerlist = new ActQuestionsAnswerlist();
        answerlist.setQuestionId(Integer.parseInt(requestBean.getQuestionId()));
        answerlist.setUserId(requestBean.getUserId());
        answerlist.setUserName(requestBean.getUserName());
        answerlist.setTruename(requestBean.getTrueName());
        answerlist.setMobile(requestBean.getMobile());
        answerlist.setAnswerTime(GetDate.getNowTime10());
        answerlist.setAnswer(requestBean.getAnswer());
        answerlist.setIsCorrect(1);
        answerlist.setCreateTime(GetDate.getNowTime10());
        answerlist.setDelFlg(0);
        actQuestionsAnswerlistMapper.insert(answerlist);
        //插入奖励发送表
        ActRewardList actRewardList = new ActRewardList();
        actRewardList.setUserId(requestBean.getUserId());
        actRewardList.setUserName(requestBean.getUserName());
        actRewardList.setActType(ActQuestionTenDefine.ACT_TYPE);
        actRewardList.setCouponCode(requestBean.getCoupon());
        actRewardList.setSendFlg(requestBean.getSendFlg()?1:0);
        actRewardList.setCreateTime(GetDate.getNowTime10());
        actRewardList.setDelFlg(0);
        actRewardListMapper.insert(actRewardList);
    }

}
