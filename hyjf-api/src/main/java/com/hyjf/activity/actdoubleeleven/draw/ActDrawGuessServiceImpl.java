package com.hyjf.activity.actdoubleeleven.draw;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.mapper.auto.ActJanAnswerListMapper;
import com.hyjf.mybatis.mapper.auto.ActJanQuestionsMapper;
import com.hyjf.mybatis.mapper.auto.ActRewardListMapper;
import com.hyjf.mybatis.model.auto.ActJanAnswerList;
import com.hyjf.mybatis.model.auto.ActJanAnswerListExample;
import com.hyjf.mybatis.model.auto.ActJanQuestions;
import com.hyjf.mybatis.model.auto.ActJanQuestionsExample;
import com.hyjf.mybatis.model.auto.ActJanQuestionsExample.Criteria;
import com.hyjf.mybatis.model.auto.ActRewardList;
import com.hyjf.mybatis.model.auto.ActRewardListExample;
import com.hyjf.mybatis.model.auto.CouponConfig;
import com.hyjf.mybatis.model.auto.CouponConfigExample;

@Service("actDrawGuessService")
public class ActDrawGuessServiceImpl extends BaseServiceImpl implements ActDrawGuessService {

    @Autowired
    protected ActJanQuestionsMapper actQuestionsMapper;
    @Autowired
    protected ActJanAnswerListMapper actJanAnswerListMapper;
    @Autowired
    protected ActRewardListMapper actRewardListMapper;
    
    /**
     * 根据当前时间查询问题
     * @author sunss
     * @param resultBean
     */
    @Override
    public ActJanQuestions getQuestionByNowDate() {
        // 获取当前时间
        Integer nowDate = Integer.parseInt(GetDate.getDate("yyyyMMdd"));
        ActJanQuestionsExample example = new ActJanQuestionsExample();
        Criteria criteria = example.createCriteria();
        
        criteria.andTimeStartLessThanOrEqualTo(nowDate);
        criteria.andTimeEndGreaterThanOrEqualTo(nowDate);
        criteria.andDelFlgEqualTo(0);
        
        List<ActJanQuestions> list = actQuestionsMapper.selectByExample(example);
        
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
    public ActJanAnswerList getActQuestionsAnswerListByUserId(Integer questionId, Integer userId) {
        ActJanAnswerListExample example = new ActJanAnswerListExample();
        
        example.createCriteria().andQuestionIdEqualTo(questionId).andUserIdEqualTo(userId).andDelFlgEqualTo(0);
        
        List<ActJanAnswerList> list = actJanAnswerListMapper.selectByExample(example);
        
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
    public ActJanQuestions getQuestionById(Integer questionId) {
        // 获取当前时间
        Integer nowDate = Integer.parseInt(GetDate.getDate("yyyyMMdd"));
        
        ActJanQuestionsExample example = new ActJanQuestionsExample();
        
        example.createCriteria().andTimeStartLessThanOrEqualTo(nowDate).andTimeEndGreaterThanOrEqualTo(nowDate).andDelFlgEqualTo(0).andIdEqualTo(questionId);
        
        List<ActJanQuestions> list = actQuestionsMapper.selectByExample(example);
        
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
    public void insertActRewardlist(ActDrawGuessRequestBean requestBean) {
        //插入答题记录表
        // 查询优惠券名字
        CouponConfigExample emConfig = new CouponConfigExample();
        CouponConfigExample.Criteria caConfig = emConfig.createCriteria();
        caConfig.andCouponCodeEqualTo(requestBean.getCoupon());
        List<CouponConfig> configList = couponConfigMapper.selectByExample(emConfig);
        if (configList == null || configList.isEmpty()) {
            return;
        }
        CouponConfig config = configList.get(0);
        
        ActJanAnswerList answerlist = new ActJanAnswerList();
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
        answerlist.setCouponCode(requestBean.getCoupon());
        answerlist.setCouponName(config.getCouponName());
        actJanAnswerListMapper.insert(answerlist);
        //插入奖励发送表
        ActRewardList actRewardList = new ActRewardList();
        actRewardList.setUserId(requestBean.getUserId());
        actRewardList.setUserName(requestBean.getUserName());
        actRewardList.setActType(ActDrawGuessDefine.ACT_TYPE);
        actRewardList.setCouponCode(requestBean.getCoupon());
        actRewardList.setSendFlg(requestBean.getSendFlg()?1:0);
        actRewardList.setCreateTime(GetDate.getNowTime10());
        actRewardList.setDelFlg(0);
        actRewardListMapper.insert(actRewardList);
    }

    @Override
    public List<ActRewardList> getHasCoupArrByUserId(Integer userId) {
        ActRewardListExample example = new ActRewardListExample();
        example.createCriteria().andUserIdEqualTo(userId).andActTypeEqualTo(ActDrawGuessDefine.ACT_TYPE).andDelFlgEqualTo(0).andSendFlgEqualTo(1);
        
        return actRewardListMapper.selectByExample(example);
    }

}
