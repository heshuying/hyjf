package com.hyjf.api.web.activity.newyear.lanternfestival;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hyjf.activity.newyear.lanternfestival.CheckResultBean;
import com.hyjf.activity.newyear.lanternfestival.LanternFestivalBean;
import com.hyjf.activity.newyear.lanternfestival.LanternFestivalDefine;
import com.hyjf.activity.newyear.lanternfestival.LanternFestivalResultBean;
import com.hyjf.activity.newyear.lanternfestival.LanternFestivalService;
import com.hyjf.activity.newyear.lanternfestival.PresentRiddlesResultBean;
import com.hyjf.activity.newyear.lanternfestival.UserAnswerResultBean;
import com.hyjf.activity.newyear.lanternfestival.UserLanternIllumineResultBean;
import com.hyjf.activity.newyear.lanternfestival.UserPresentCumulativeResultBean;
import com.hyjf.api.web.BaseController;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.log.LogUtil;

@Controller
@RequestMapping(value=LanternFestivalDefine.REQUEST_MAPPING)
public class LanternFestivalServer extends BaseController{
    @Autowired
    private LanternFestivalService lanternFestivalService;

    /**
     * 
     * 获取当天谜题信息
     * @author pcc
     * @param lanternFestivalBean
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = LanternFestivalDefine.GET_PRESENT_RIDDLES)
    public PresentRiddlesResultBean getPresentRiddles(@ModelAttribute LanternFestivalBean lanternFestivalBean , HttpServletRequest request, HttpServletResponse response) {

        LogUtil.startLog(this.getClass().getName(), LanternFestivalDefine.GET_PRESENT_RIDDLES);
        PresentRiddlesResultBean resultBean = new PresentRiddlesResultBean();

        if(!this.checkSign(lanternFestivalBean, LanternFestivalDefine.GET_PRESENT_RIDDLES)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("验签失败！");
            return resultBean;
        }
        lanternFestivalService.getPresentRiddles(resultBean);
        
        LogUtil.endLog(this.getClass().getName(), LanternFestivalDefine.GET_PRESENT_RIDDLES);
        return resultBean;
    }
    
    
    
    /**
     * 
     * 获取当前用户今天是否答过题
     * @author pcc
     * @param lanternFestivalBean
     * @param request
     * @param response
     * @return
     */
    @ResponseBody   
    @RequestMapping(value = LanternFestivalDefine.GET_TODAY_USER_ANSWER_FLAG)
    public LanternFestivalResultBean getTodayUserAnswerFlag(@ModelAttribute LanternFestivalBean lanternFestivalBean , HttpServletRequest request, HttpServletResponse response) {

        LogUtil.startLog(this.getClass().getName(), LanternFestivalDefine.GET_TODAY_USER_ANSWER_FLAG);
        LanternFestivalResultBean resultBean = new LanternFestivalResultBean();
        if(!this.checkSign(lanternFestivalBean, LanternFestivalDefine.GET_TODAY_USER_ANSWER_FLAG)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("验签失败！");
            return resultBean;
        }
        lanternFestivalService.getTodayUserAnswerFlag(resultBean,lanternFestivalBean);
        
        LogUtil.endLog(this.getClass().getName(), LanternFestivalDefine.GET_TODAY_USER_ANSWER_FLAG);
        return resultBean;
    }
    
    
    
    
    

    /**
     * 
     * 获取用户累计获得优惠券信息
     * @author pcc
     * @param lanternFestivalBean
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = LanternFestivalDefine.GET_USER_PRESENT_CUMULATIVE_COUPON)
    public UserPresentCumulativeResultBean getUserPresentCumulativeCoupon(@ModelAttribute LanternFestivalBean lanternFestivalBean , HttpServletRequest request, HttpServletResponse response) {

        LogUtil.startLog(this.getClass().getName(), LanternFestivalDefine.GET_USER_PRESENT_CUMULATIVE_COUPON);
        UserPresentCumulativeResultBean resultBean = new UserPresentCumulativeResultBean();

        if(!this.checkSign(lanternFestivalBean, LanternFestivalDefine.GET_USER_PRESENT_CUMULATIVE_COUPON)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("验签失败！");
            return resultBean;
        }
        lanternFestivalService.getUserPresentCumulativeCoupon(lanternFestivalBean.getUserId(),resultBean);
        
        LogUtil.endLog(this.getClass().getName(), LanternFestivalDefine.GET_USER_PRESENT_CUMULATIVE_COUPON);
        return resultBean;
    }
    /**
     *     
     * 获取用户灯笼点亮列表
     * @author pcc
     * @param lanternFestivalBean
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = LanternFestivalDefine.GET_USER_LANTERN_ILLUMINE_LIST)
    public UserLanternIllumineResultBean getUserLanternIllumineList(@ModelAttribute LanternFestivalBean lanternFestivalBean , HttpServletRequest request, HttpServletResponse response) {

        LogUtil.startLog(this.getClass().getName(), LanternFestivalDefine.GET_USER_LANTERN_ILLUMINE_LIST);
        UserLanternIllumineResultBean resultBean = new UserLanternIllumineResultBean();
        if(!this.checkSign(lanternFestivalBean, LanternFestivalDefine.GET_USER_LANTERN_ILLUMINE_LIST)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("验签失败！");
            return resultBean;
        }
        lanternFestivalService.getUserLanternIllumineList(lanternFestivalBean.getUserId(),resultBean);
        
        LogUtil.endLog(this.getClass().getName(), LanternFestivalDefine.GET_USER_LANTERN_ILLUMINE_LIST);
        return resultBean;
    }
    
    
    /**
     * 
     * 初始化记录用户答题信息
     * @author pcc
     * @param lanternFestivalBean
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = LanternFestivalDefine.INSERT_USER_ANSWER_RECORD_INIT)
    public LanternFestivalResultBean insertUserAnswerRecordInit(@ModelAttribute LanternFestivalBean lanternFestivalBean , HttpServletRequest request, HttpServletResponse response) {

        LogUtil.startLog(this.getClass().getName(), LanternFestivalDefine.INSERT_USER_ANSWER_RECORD_INIT);
        LanternFestivalResultBean resultBean = new LanternFestivalResultBean();
        if(!this.checkSign(lanternFestivalBean, LanternFestivalDefine.INSERT_USER_ANSWER_RECORD_INIT)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("验签失败！");
            return resultBean;
        }
        int count=0;
        try {
            count=lanternFestivalService.insertUserAnswerRecordInit(lanternFestivalBean);
        } catch (Exception e) {
            LogUtil.startLog(this.getClass().getName(),"系统异常");
            count=0;
        }
        
        
        if(count>0){
            resultBean.setStatus(BaseResultBean.STATUS_SUCCESS); 
            resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS); 
        }else{
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("系统异常,请稍后重试！");
        }
        
        LogUtil.endLog(this.getClass().getName(), LanternFestivalDefine.INSERT_USER_ANSWER_RECORD_INIT);
        return resultBean;
    }
    
    
    /**
     * 
     * 修改用户答题记录
     * @author pcc
     * @param lanternFestivalBean
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = LanternFestivalDefine.UPDATE_USER_ANSWER_RECORD)
    public UserAnswerResultBean updateUserAnswerRecord(@ModelAttribute LanternFestivalBean lanternFestivalBean , HttpServletRequest request, HttpServletResponse response) {

        LogUtil.startLog(this.getClass().getName(), LanternFestivalDefine.UPDATE_USER_ANSWER_RECORD);
        UserAnswerResultBean resultBean = new UserAnswerResultBean();
        if(!this.checkSign(lanternFestivalBean, LanternFestivalDefine.UPDATE_USER_ANSWER_RECORD)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("验签失败！");
            return resultBean;
        }
        int count=lanternFestivalService.updateUserAnswerRecord(lanternFestivalBean,resultBean);
        
        if(count>0){
            resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
            resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS); 
        }else{
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("系统异常,请稍后重试！");
        }
        
        LogUtil.endLog(this.getClass().getName(), LanternFestivalDefine.UPDATE_USER_ANSWER_RECORD);
        return resultBean;
    }
    
    
    /**
     * 
     * 校验用户答题
     * @author pcc
     * @param lanternFestivalBean
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = LanternFestivalDefine.CHECK)
    public CheckResultBean check(@ModelAttribute LanternFestivalBean lanternFestivalBean , HttpServletRequest request, HttpServletResponse response) {

        LogUtil.startLog(this.getClass().getName(), LanternFestivalDefine.CHECK);
        CheckResultBean resultBean = new CheckResultBean();
        if(!this.checkSign(lanternFestivalBean, LanternFestivalDefine.CHECK)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("验签失败！");
            return resultBean;
        }
        lanternFestivalService.check(lanternFestivalBean,resultBean);
        
        
        LogUtil.endLog(this.getClass().getName(), LanternFestivalDefine.CHECK);
        return resultBean;
    }
    
    
}
