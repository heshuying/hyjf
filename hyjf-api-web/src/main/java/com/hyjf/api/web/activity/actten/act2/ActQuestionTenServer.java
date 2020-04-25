package com.hyjf.api.web.activity.actten.act2;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.activity.actten.act2.ActQuestionTenDefine;
import com.hyjf.activity.actten.act2.ActQuestionTenRequestBean;
import com.hyjf.activity.actten.act2.ActQuestionTenResultBean;
import com.hyjf.activity.actten.act2.ActQuestionTenService;
import com.hyjf.api.web.BaseController;
import com.hyjf.bank.service.coupon.UserCouponService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.ActQuestions;
import com.hyjf.mybatis.model.auto.ActQuestionsAnswerlist;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 
 * 十月份活动2 - 答题活动
 * @author sunss
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年9月12日
 * @see 下午2:30:37
 */
@Controller
@RequestMapping(value = ActQuestionTenDefine.REQUEST_MAPPING)
public class ActQuestionTenServer extends BaseController {

    @Autowired
    private ActQuestionTenService actQuestionTenService;

   // @Autowired
   // private UserCouponService userCouponService;

    private static final String THIS_CLASS = ActQuestionTenServer.class.getName();

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
    @RequestMapping(value = ActQuestionTenDefine.GET_USER_QUESTION_ACTION)
    public JSONObject getUserQuestion( ActQuestionTenRequestBean requestBean, HttpServletRequest request,
        HttpServletResponse response) {
        LogUtil.startLog(THIS_CLASS, ActQuestionTenDefine.GET_USER_QUESTION_ACTION);
        ActQuestionTenResultBean resultBean = new ActQuestionTenResultBean();
        // 验签
        if (!this.checkSign(requestBean, ActQuestionTenDefine.GET_USER_QUESTION_ACTION)) {
            return resultBean.getFailMess("验签失败！");
        }

        // 获取当前时间的问题
        ActQuestions actQuestions = actQuestionTenService.getQuestionByNowDate();
        if (actQuestions == null) {
            return resultBean.getFailMess("活动时间为10月1日-10月28日");
        }

        Integer questionId = actQuestions.getId();
        resultBean.setActQuestions(actQuestions);
        Integer userId = requestBean.getUserId();

        if (!Validator.isNull(userId)) {
            // 检查用户是否存在
            Users user = actQuestionTenService.getUsers(userId);
            if (user == null) {
                return resultBean.getFailMess("用户不存在！");
            }

            // 根据问题ID查询用户答题状态
            ActQuestionsAnswerlist actQuestionsAnswerlist =
                    actQuestionTenService.getActQuestionsAnswerListByUserId(questionId, userId);
            if (actQuestionsAnswerlist == null) {
                // 未答题
                return resultBean.getSuccessNotAnswered();
            }
            return resultBean.getSuccessAnswered();
        }
        LogUtil.endLog(THIS_CLASS, ActQuestionTenDefine.GET_USER_QUESTION_ACTION);
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
    @RequestMapping(value = ActQuestionTenDefine.ANSWER_CHECK_ACTION)
    public JSONObject answerCheck( ActQuestionTenRequestBean requestBean, HttpServletRequest request,
        HttpServletResponse response) {
        LogUtil.startLog(THIS_CLASS, ActQuestionTenDefine.ANSWER_CHECK_ACTION);
        ActQuestionTenResultBean resultBean = new ActQuestionTenResultBean();
        // 验签
        if (!this.checkSign(requestBean, ActQuestionTenDefine.ANSWER_CHECK_ACTION)) {
            return resultBean.getFailMess("验签失败！");
        }

        String questionId = requestBean.getQuestionId();
        Integer userId = requestBean.getUserId();
        String answer = requestBean.getAnswer();

        if (Validator.isNull(questionId) || Validator.isNull(userId) || Validator.isNull(answer)) {
            return resultBean.getFailMess("请求参数非法！");
        }

        // 检查用户是否存在
        Users user = actQuestionTenService.getUsers(userId);
        if (user == null) {
            return resultBean.getFailMess("用户不存在！");
        }

        // 检查用户是否已经答过题
        ActQuestionsAnswerlist actQuestionsAnswerlist =
                actQuestionTenService.getActQuestionsAnswerListByUserId(Integer.parseInt(questionId), userId);
        if (actQuestionsAnswerlist != null) {
            // 已经答过题了 返回错误信息
            return resultBean.getFailAnswered();
        }

        // 查询问题的正确答案
        ActQuestions actQuestions = actQuestionTenService.getQuestionById(Integer.parseInt(questionId));
        if (actQuestions == null) {
            // 查询不到该问题
            return resultBean.getFailMess("活动时间为10月1日-10月28日");
        }

        if (actQuestions.getAnswerRight().equals(answer)) {
            // 答对了 记录日志
            String coupon = getCouponRandom();
            Integer activityId = Integer.parseInt(PropUtils.getSystem("hyjf.actten2017.id"));
            int couponSource = Integer.MIN_VALUE;
            List<String> couponCodeList = new ArrayList<String>();
            couponCodeList.add(coupon);
            boolean sendFlg = false;
            try {
//                userCouponService.sendConponAction(couponCodeList, userId + "", 0, activityId, couponSource,
//                        "十月份活动发放优惠券");
                sendFlg = true;
            } catch (Exception e) {
                LogUtil.errorLog(this.getClass().getName(), ActQuestionTenDefine.ANSWER_CHECK_ACTION,
                        "10月份答题活动优惠券发送失败，UserID：" + userId, null);
            }

            // 获取用户真实姓名
            UsersInfo userInfo = actQuestionTenService.getUsersInfoByUserId(userId);

            requestBean.setSendFlg(sendFlg);
            requestBean.setMobile(user.getMobile());
            requestBean.setUserName(user.getUsername());
            requestBean.setTrueName(userInfo.getTruename());
            requestBean.setCoupon(coupon);

            actQuestionTenService.insertActRewardlist(requestBean);
            return resultBean.getSuccessMess("恭喜你答题成功！");
        }

        LogUtil.endLog(THIS_CLASS, ActQuestionTenDefine.ANSWER_CHECK_ACTION);
        return resultBean.getFailMess("抱歉，您答错了！");
    }

    /**
     * 获得此人中的加息券
     * @author sunss
     * @return
     */
    private String getCouponRandom() {
        // 读取配置文件中的加息券配置
        String coupStr = PropUtils.getSystem("act.act2.coupon");
        String[] coupArr = coupStr.split(",");
        Random random = new Random();
        int result = random.nextInt(coupArr.length);
        return coupArr[result];
    }

}
