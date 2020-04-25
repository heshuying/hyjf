package com.hyjf.web.activity.activityinfo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.common.log.LogUtil;
import com.hyjf.mybatis.model.auto.ActivityF1;
import com.hyjf.web.BaseController;
import com.hyjf.web.util.WebUtils;

@Controller("activityF1Controller")
@RequestMapping(value = ActivityF1Define.REQUEST_MAPPING)
public class ActivityF1Controller extends BaseController {
    @Autowired
    private ActivityF1Service activityF1Service;

    /**
     * 
     * 获取F1大师赛活动详情列表
     * @author yyc
     * @return
     */
    @RequestMapping(value = ActivityF1Define.ACTIVITYF1_LIST_ACTION)
    public ModelAndView getActivityF1List(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(ActivityF1Define.THIS_CLASS, ActivityF1Define.ACTIVITYF1_LIST_ACTION);

        ModelAndView modelAndView = new ModelAndView(ActivityF1Define.ACTIVITYF1_LIST_PATH);
        // 从session中获取相应的用户id
        Integer userId = WebUtils.getUserId(request);

        List<ActivityF1ResultBean> activityListResultBeans = new ArrayList<ActivityF1ResultBean>();
        // F1大师赛活动参与列表
        List<ActivityF1> activityList =
                activityF1Service.getActivityF1ListByActivityType(ActivityF1Define.ACTIVITY_TYPE);
        if (activityList != null && activityList.size() > 0) {
            for (int i = 0; i < activityList.size(); i++) {
                ActivityF1 activityF1 = activityList.get(i);
                ActivityF1ResultBean activityF1ResultBean = new ActivityF1ResultBean();
                activityF1ResultBean.setSort(i + 1);
                activityF1.setUserName(activityF1.getUserName().substring(0, 1)
                        + activityF1.getUserName().substring(1).replaceAll("[\u4e00-\u9fa5a-zA-Z0-9_]", "x"));

                if (i == 0 && (activityF1.getSpeed().intValue() >= 200)) {
                    // 第一名
                    activityF1.setReturnAmount(activityF1.getReturnAmountActivity().add(new BigDecimal(3000)));
                    BeanUtils.copyProperties(activityF1, activityF1ResultBean);
                    activityListResultBeans.add(activityF1ResultBean);
                    continue;
                }
                if (i == 1 && (activityF1.getSpeed().intValue() >= 200)) {
                    // 第二名
                    activityF1.setReturnAmount(activityF1.getReturnAmountActivity().add(new BigDecimal(2000)));
                    BeanUtils.copyProperties(activityF1, activityF1ResultBean);
                    activityListResultBeans.add(activityF1ResultBean);
                    continue;
                }

                if (i == 2 && (activityF1.getSpeed().intValue() >= 200)) {
                    // 第三名
                    activityF1.setReturnAmount(activityF1.getReturnAmountActivity().add(new BigDecimal(1000)));
                    BeanUtils.copyProperties(activityF1, activityF1ResultBean);
                    activityListResultBeans.add(activityF1ResultBean);
                    continue;
                }
                if ((i >= 3 && i <= 9 && (activityF1.getSpeed().intValue() >= 200))) {
                    // 4-10名
                    activityF1.setReturnAmount(activityF1.getReturnAmountActivity().add(new BigDecimal(800)));
                    BeanUtils.copyProperties(activityF1, activityF1ResultBean);
                    activityListResultBeans.add(activityF1ResultBean);
                    continue;
                }
                if ((i > 9 && i <= 19 && (activityF1.getSpeed().intValue() >= 200))) {
                    // 11-20名
                    activityF1.setReturnAmount(activityF1.getReturnAmountActivity().add(new BigDecimal(500)));
                    BeanUtils.copyProperties(activityF1, activityF1ResultBean);
                    activityListResultBeans.add(activityF1ResultBean);
                    continue;
                }

                if (activityF1.getReturnAmountActivity() != null) {
                    activityF1.setReturnAmount(activityF1.getReturnAmountActivity());
                } else {
                    activityF1.setReturnAmount(new BigDecimal(0));
                    activityF1.setReturnAmountActivity(new BigDecimal(0));
                }

                BeanUtils.copyProperties(activityF1, activityF1ResultBean);

                activityListResultBeans.add(activityF1ResultBean);
            }
        }

        ActivityF1 activityF1 = null;
        if (userId != null) {
            // 当前登陆用户参加活动详情
            activityF1 = activityF1Service.getActivityF1ByUserId(userId);
            modelAndView.addObject("activityF1", activityF1);
            modelAndView.addObject("islogin", true);
        } else {
            modelAndView.addObject("islogin", false);
        }

        modelAndView.addObject("activityList", activityList);

        return modelAndView;
    }
}
