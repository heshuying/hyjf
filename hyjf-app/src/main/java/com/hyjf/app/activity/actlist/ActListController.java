package com.hyjf.app.activity.actlist;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.ActivityList;

@Controller("actListController")
@RequestMapping(value = ActListDefine.REQUEST_MAPPING)
public class ActListController extends BaseController {
    @Autowired
    private ActListService actListService;
    
    /**
     * 
     * 活动列表
     * @author hsy
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = ActListDefine.GET_ACTLIST, method = RequestMethod.GET)
    public ModelAndView actList(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(this.getClass().getName(), ActListDefine.GET_ACTLIST);
        ModelAndView modelAndView = new ModelAndView(ActListDefine.ACTLIST_PAGE_PATH);
        
        String actDetailUrl = "/activity/activityDetail?";
        
        // 平台
        String platform = request.getParameter("platform");
        // 唯一标识
        String sign = request.getParameter("sign");
        // token
        String token = request.getParameter("token");
 
        
        //请求参数校验
        if(Validator.isNull(sign)){
            modelAndView = new ModelAndView(ActListDefine.ERROR_PTAH);
            return modelAndView;
        }
        
        if(StringUtils.isNotEmpty(sign)){
            actDetailUrl = actDetailUrl + "sign=" + sign;
        }
        if(StringUtils.isNotEmpty(token)){
            actDetailUrl = actDetailUrl + "&token=" + token; 
        }
        if(StringUtils.isNotEmpty(platform)){
            actDetailUrl = actDetailUrl + "&platform=" + platform;
        }
        actDetailUrl = actDetailUrl + "&url=";
        
        JSONObject jsonResult = actListService.getActListData();
        List<ActivityList> listStarting = JSONArray.parseArray(JSONObject.toJSONString(jsonResult.get("actListStarting")), ActivityList.class);
        List<ActivityList> listWaiting = JSONArray.parseArray(JSONObject.toJSONString(jsonResult.get("actListWaitingStart")), ActivityList.class);
        List<ActivityList> listEnded = JSONArray.parseArray(JSONObject.toJSONString(jsonResult.get("actListEnded")), ActivityList.class);
     
        modelAndView.addObject("listStarting", listStarting);
        modelAndView.addObject("listWaiting", listWaiting);
        modelAndView.addObject("listEnded", listEnded);
        modelAndView.addObject("actDetailUrl", actDetailUrl);
        
        String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
        modelAndView.addObject("fileDomainUrl", fileDomainUrl);
        LogUtil.endLog(this.getClass().getName(), ActListDefine.GET_ACTLIST);
        return modelAndView;
    }
    

}
