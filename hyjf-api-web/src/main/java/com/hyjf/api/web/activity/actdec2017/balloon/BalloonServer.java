package com.hyjf.api.web.activity.actdec2017.balloon;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.hyjf.activity.actdec2017.balloon.BalloonDefine;
import com.hyjf.activity.actdec2017.balloon.BalloonReceiveRequestBean;
import com.hyjf.activity.actdec2017.balloon.BalloonReceiveResultBean;
import com.hyjf.activity.actdec2017.balloon.BalloonService;
import com.hyjf.activity.actdec2017.balloon.InitBalloonResultBean;
import com.hyjf.api.web.BaseController;
import com.hyjf.base.bean.BaseDefine;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.ActdecTenderBalloon;
import com.hyjf.mybatis.model.auto.ActivityList;

/**
 * 砍价活动
 * @author hesy
 *
 */
@Controller
@RequestMapping(value = BalloonDefine.REQUEST_MAPPING)
public class BalloonServer extends BaseController{

    @Autowired
    private BalloonService balloonService;
    
	Logger _log = LoggerFactory.getLogger(BalloonServer.class);
    
    String actId = PropUtils.getSystem("hyjf.act.dec.2017.balloon.id");
    
    /**
     * 获取砍价活动奖品列表
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = BalloonDefine.GET_INIT_BALLOON_PAGE)
    public InitBalloonResultBean initBalloonPage(HttpServletRequest request, HttpServletResponse response) {
        InitBalloonResultBean resultBean = new InitBalloonResultBean();
        
        ActivityList act = balloonService.getActivityById(Integer.parseInt(actId));
        
        Integer actStartTime = act.getTimeStart();
        Integer actEndTime = act.getTimeEnd();
        
        resultBean.setActStartTime(String.valueOf(actStartTime));
        resultBean.setActEndtime(String.valueOf(actEndTime));
        resultBean.setNowTime(String.valueOf(GetDate.getNowTime10()));
        
        String userId = request.getParameter("userId");
        
        _log.info("initBalloonPage接口请求参数：userId=" + userId);
        
        //验证请求参数
        if (Validator.isNull(userId)) {
        	resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("请求参数非法");
            _log.info("initBalloonPage接口返回：" + JSON.toJSONString(resultBean));
            return resultBean;

        }
        
        ActdecTenderBalloon record = balloonService.getBalloonRecord(Integer.parseInt(userId));
        if(record == null){
        	resultBean.setCanReceiveCount(0);
        	resultBean.setReceivedCount(0);
        }else{
        	resultBean.setCanReceiveCount(record.getBallonCanReceive());
        	resultBean.setReceivedCount(record.getBallonReceived());
        }
        
        resultBean.setUserId(userId);
        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        
        _log.info("initBalloonPage接口返回：" + JSON.toJSONString(resultBean));
        
        return resultBean;
    }
    
  
    /**
     * 获取砍价活动奖品列表
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = BalloonDefine.GET_BALLOON_RECEIVE)
    public BalloonReceiveResultBean balloonReceive(@ModelAttribute BalloonReceiveRequestBean balloonReceiveRequestBean, HttpServletRequest request, HttpServletResponse response) {
        BalloonReceiveResultBean resultBean = new BalloonReceiveResultBean();
        
        ActivityList act = balloonService.getActivityById(Integer.parseInt(actId));
        
        Integer actStartTime = act.getTimeStart();
        Integer actEndTime = act.getTimeEnd();
        
        resultBean.setActStartTime(String.valueOf(actStartTime));
        resultBean.setActEndtime(String.valueOf(actEndTime));
        resultBean.setNowTime(String.valueOf(GetDate.getNowTime10()));
        
        String userId = request.getParameter("userId");
        
        _log.info("balloonReceive接口请求参数：userId=" + userId);
        
        if(actStartTime > GetDate.getNowTime10() || actEndTime < GetDate.getNowTime10()){
        	resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("活动未开始或已结束");
            _log.info("活动未开始或已结束，balloonReceive接口返回：" + resultBean);
            return resultBean;
        }
        
        //验证请求参数
        if (Validator.isNull(userId)) {
        	resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("请求参数非法");
            _log.info("请求参数非法，balloonReceive接口返回：" + JSON.toJSONString(resultBean));
            return resultBean;

        }
        
        //验签
        if(!this.checkSign(balloonReceiveRequestBean, BaseDefine.METHOD_DO_BALLOON_RECEIVE)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("验签失败！");
            _log.info("验签失败，balloonReceive接口返回：" + resultBean);
            return resultBean;
        }
        
        ActdecTenderBalloon record = balloonService.getBalloonRecord(Integer.parseInt(userId));
        if(record == null || record.getBallonCanReceive()<=0){
        	resultBean.setCanReceiveCount(0);
        	resultBean.setReceivedCount(0);
        	resultBean.setCurrentReceiveCount(0);
        	resultBean.setUserId(userId);
            resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
            resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
            _log.info("没有可领取的气球，balloonReceive接口返回：" + JSON.toJSONString(resultBean));
            return resultBean;
        }
        
        balloonService.updateBalloonReceive(Integer.parseInt(userId));
        resultBean.setCurrentReceiveCount(record.getBallonCanReceive());
        record = balloonService.getBalloonRecord(Integer.parseInt(userId));
    	resultBean.setCanReceiveCount(record.getBallonCanReceive());
    	resultBean.setReceivedCount(record.getBallonReceived());
        
        resultBean.setUserId(userId);
        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        
        _log.info("balloonReceive接口返回：" + JSON.toJSONString(resultBean));
        
        return resultBean;
    }
	

}
