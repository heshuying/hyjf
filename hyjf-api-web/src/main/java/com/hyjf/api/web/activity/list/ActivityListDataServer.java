package com.hyjf.api.web.activity.list;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hyjf.activity.list.ActivityListDataBean;
import com.hyjf.activity.list.ActivityListDataDefine;
import com.hyjf.activity.list.ActivityListDataResultBean;
import com.hyjf.activity.list.ActivityListDataService;
import com.hyjf.api.web.BaseController;
import com.hyjf.base.bean.BaseDefine;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.mybatis.model.auto.ActivityList;

@Controller
@RequestMapping(value=ActivityListDataDefine.REQUEST_MAPPING)
public class ActivityListDataServer extends BaseController{

    @Autowired
    private ActivityListDataService activityListDataService;
    
    /**
     * 
     * 获取活动列表数据
     * @author hsy
     * @param form
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = ActivityListDataDefine.GET_ACTIVITY_LIST)
    public ActivityListDataResultBean getActListData(@ModelAttribute ActivityListDataBean form, HttpServletRequest request, HttpServletResponse response) {
        String methodName = "getActListData";
        LogUtil.startLog(this.getClass().getName(), methodName);
        ActivityListDataResultBean resultBean = new ActivityListDataResultBean();
        
        if(StringUtils.isEmpty(form.getPlatform())){
            form.setPlatform("");
        }
        
        if(!this.checkSign(form, BaseDefine.METHOD_GET_ACTLIST)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("验签失败！");
            LogUtil.errorLog(this.getClass().getName(), methodName, "验签失败！", null);
            return resultBean;
        }

        List<ActivityList> listStarting = activityListDataService.getActivityListStarting(form.getPlatform().split(","));
        List<ActivityList> listWaiting = activityListDataService.getActivityListWaitingStart(form.getPlatform().split(","));
        List<ActivityList> listEnded = activityListDataService.getActivityListEnded(3, form.getPlatform().split(","));
        
        resultBean.setActListStarting(listStarting);
        resultBean.setActListWaitingStart(listWaiting);
        resultBean.setActListEnded(listEnded);
        
        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        
        LogUtil.endLog(this.getClass().getName(), methodName);
        return resultBean;
    }

}
