package com.hyjf.api.web.activity.actdoubleeleven.draw;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.activity.actdoubleeleven.draw.ActDrawGuessDefine;
import com.hyjf.activity.actdoubleeleven.draw.ActDrawGuessRequestBean;
import com.hyjf.activity.actdoubleeleven.draw.ActDrawGuessResultBean;
import com.hyjf.activity.actdoubleeleven.draw.ActDrawGuessService;
import com.hyjf.api.web.BaseController;
import com.hyjf.bank.service.coupon.UserCouponServiceImpl;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.ActJanAnswerList;
import com.hyjf.mybatis.model.auto.ActJanQuestions;
import com.hyjf.mybatis.model.auto.ActRewardList;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;

/**
 * 
 * 我画你猜活动
 * @author sss
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年10月24日
 * @see 下午2:16:59
 */
@Controller
@RequestMapping(value = ActDrawGuessDefine.REQUEST_MAPPING)
public class DrawGuessServer extends BaseController {

    @Autowired
    private ActDrawGuessService actDrawGuessService;

    @Autowired
    private UserCouponServiceImpl userCouponService;

    private static final String THIS_CLASS = DrawGuessServer.class.getName();

    /**
     * 
     * 根据用户ID加载答题信息
     * @author sunss
     * @param requestBean
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = ActDrawGuessDefine.GET_USER_QUESTION_ACTION)
    public JSONObject getUserQuestion( ActDrawGuessRequestBean requestBean, HttpServletRequest request,
        HttpServletResponse response) {
        LogUtil.startLog(THIS_CLASS, ActDrawGuessDefine.GET_USER_QUESTION_ACTION);
        ActDrawGuessResultBean resultBean = new ActDrawGuessResultBean();
        resultBean.setActTime(PropUtils.getSystem("hyjf.act.nov.2017.draw.time"));
        
        // 验签
        if (!this.checkSign(requestBean, ActDrawGuessDefine.GET_USER_QUESTION_ACTION)) {
            return resultBean.getFailMess("验签失败！");
        }

        // 获取当前时间的问题
        ActJanQuestions actQuestions = actDrawGuessService.getQuestionByNowDate();
        if (actQuestions == null) {
            return resultBean.getFailMess("活动时间为11月11日-11月30日");
        }

        Integer questionId = actQuestions.getId();
        resultBean.setActQuestions(actQuestions);
        Integer userId = requestBean.getUserId();

        if (!Validator.isNull(userId)) {
            // 检查用户是否存在
            Users user = actDrawGuessService.getUsers(userId);
            if (user == null) {
                return resultBean.getFailMess("用户不存在！");
            }

            // 根据问题ID查询用户答题状态
            ActJanAnswerList actQuestionsAnswerlist =
                    actDrawGuessService.getActQuestionsAnswerListByUserId(questionId, userId);
            if (actQuestionsAnswerlist == null) {
                // 未答题
                return resultBean.getSuccessNotAnswered();
            }
            return resultBean.getSuccessAnswered();
        }
        LogUtil.endLog(THIS_CLASS, ActDrawGuessDefine.GET_USER_QUESTION_ACTION);
        // 没登陆 未答题
        return resultBean.getSuccessNotAnswered();

    }

    /**
     * 用户答题
     * @author sunss
     * @param requestBean
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = ActDrawGuessDefine.ANSWER_CHECK_ACTION)
    public JSONObject answerCheck( ActDrawGuessRequestBean requestBean, HttpServletRequest request,
        HttpServletResponse response) {
        LogUtil.startLog(THIS_CLASS, ActDrawGuessDefine.ANSWER_CHECK_ACTION);
        ActDrawGuessResultBean resultBean = new ActDrawGuessResultBean();
        // 验签
       if (!this.checkSign(requestBean, ActDrawGuessDefine.ANSWER_CHECK_ACTION)) {
            return resultBean.getFailMess("验签失败！");
        }

        String questionId = requestBean.getQuestionId();
        Integer userId = requestBean.getUserId();
        String answer = requestBean.getAnswer();

        if (Validator.isNull(questionId) || Validator.isNull(userId) || Validator.isNull(answer)) {
            return resultBean.getFailMess("请求参数非法！");
        }
        // 查询问题的正确答案
        ActJanQuestions actQuestions = actDrawGuessService.getQuestionById(Integer.parseInt(questionId));
        if (actQuestions == null) {
            // 查询不到该问题
            return resultBean.getFailMess("活动时间为11月11日-11月30日");
        }

        resultBean.setActQuestions(actQuestions);
        
        // 检查用户是否存在
        Users user = actDrawGuessService.getUsers(userId);
        if (user == null) {
            return resultBean.getFailMess("用户不存在！");
        }

        // 检查用户是否已经答过题
        ActJanAnswerList actQuestionsAnswerlist =
                actDrawGuessService.getActQuestionsAnswerListByUserId(Integer.parseInt(questionId), userId);
        if (actQuestionsAnswerlist != null) {
            // 已经答过题了 返回错误信息
            return resultBean.getFailAnswered();
        }

        if (actQuestions.getAnswerRight().equals(answer)) {
            // 获得此人已经中了哪个优惠券
            List<ActRewardList> hasCoupArr = actDrawGuessService.getHasCoupArrByUserId(userId);
            // 答对了 记录日志
            String coupon = getCouponRandom(hasCoupArr);
            Integer activityId = Integer.parseInt(PropUtils.getSystem("hyjf.act.nov.2017.draw.id"));
            int couponSource = Integer.MIN_VALUE;
            List<String> couponCodeList = new ArrayList<String>();
            couponCodeList.add(coupon);
            boolean sendFlg = false;
            try {
                userCouponService.sendConponAction(couponCodeList, userId + "", 0, activityId, couponSource,
                        "双十一我画你猜活动发放优惠券");
                sendFlg = true;
            } catch (Exception e) {
                LogUtil.errorLog(this.getClass().getName(), ActDrawGuessDefine.ANSWER_CHECK_ACTION,
                        "1双十一我画你猜优惠券发送失败，UserID：" + userId, null);
            }

            // 获取用户真实姓名
            UsersInfo userInfo = actDrawGuessService.getUsersInfoByUserId(userId);

            requestBean.setSendFlg(sendFlg);
            requestBean.setMobile(user.getMobile());
            requestBean.setUserName(user.getUsername());
            requestBean.setTrueName(userInfo.getTruename());
            requestBean.setCoupon(coupon);

            actDrawGuessService.insertActRewardlist(requestBean);
            return resultBean.getSuccessMess("恭喜你答题成功！");
        }

        LogUtil.endLog(THIS_CLASS, ActDrawGuessDefine.ANSWER_CHECK_ACTION);
        return resultBean.getFailMess("抱歉，您答错了！",99);
    }

    /**
     * 获得此人中的加息券 中了的就不能在中了
     * @author sunss
     * @return
     */
    private String getCouponRandom(List<ActRewardList> rewardLists) {
        // 获取已经中的加息券
        List<String> hasCoupArr = new ArrayList<String>();
        for (ActRewardList rewar : rewardLists) {
            hasCoupArr.add(rewar.getCouponCode());
        }
        // 读取配置文件中的加息券配置
        String coupStr = PropUtils.getSystem("hyjf.act.nov.2017.draw.coupon");
        String[] coupArr = coupStr.split(",");
        // 如果已经中过了
        if(hasCoupArr.size()>0){
            List<String> noCoupList = new ArrayList<String>();
            for (String coup : coupArr) {
                if(!hasCoupArr.contains(coup)){
                    noCoupList.add(coup);
                }
            }
            coupArr = noCoupList.toArray(new String[noCoupList.size()]);
        }
        Random random = new Random();
        int result = random.nextInt(coupArr.length);
        return coupArr[result];
    }
    
}
