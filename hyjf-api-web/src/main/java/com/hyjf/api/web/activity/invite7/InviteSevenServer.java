package com.hyjf.api.web.activity.invite7;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hyjf.activity.invite7.InviteSevenDefine;
import com.hyjf.activity.invite7.InviteSevenResultBean;
import com.hyjf.activity.invite7.InviteSevenService;
import com.hyjf.api.web.BaseController;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.ActivityInviteSeven;

@Controller
@RequestMapping(value = InviteSevenDefine.REQUEST_MAPPING)
public class InviteSevenServer extends BaseController{

    @Autowired
    private InviteSevenService inviteSevenService;
    
    /**
     * 
     * 获取七月份活动奖励列表
     * @author hsy
     * @param form
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = InviteSevenDefine.GET_INVITELIST)
    public InviteSevenResultBean getInviteList(HttpServletRequest request, HttpServletResponse response) {
        String methodName = "getPrizeDrawList";
        LogUtil.startLog(this.getClass().getName(), methodName);
        InviteSevenResultBean resultBean = new InviteSevenResultBean();
        
        String userId = request.getParameter("userId");
        String investType = request.getParameter("investType");
        String page = request.getParameter("page");
        String pageSize = request.getParameter("pageSize");
        
        //验证请求参数
        if (Validator.isNull(userId)) {
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("请求参数非法");
            return resultBean;
        }
        
        //验证请求参数 “出借类型 0：全部  1：已出借  2：未出借”
        if (Validator.isNull(request.getParameter("investType"))) {
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("请求参数非法");
            return resultBean;
        }
        
        if(Validator.isNull(page) || Integer.parseInt(page) <= 0){
            page = "1";
        }
        
        if(Validator.isNull(pageSize) || Integer.parseInt(pageSize) <= 0){
            pageSize = "10";
        }

        int countTotal = inviteSevenService.selectRecordCount(investType, Integer.parseInt(userId));
        if(countTotal > 0){
            Paginator paginator = new Paginator(Integer.parseInt(page), countTotal, Integer.parseInt(pageSize));
            List<ActivityInviteSeven> inviteList = inviteSevenService.selectRecordList(investType, Integer.parseInt(userId), paginator);
            resultBean.setPaginator(paginator);
            resultBean.setData(inviteList);
        }else{
            Paginator paginator = new Paginator(Integer.parseInt(page), countTotal, Integer.parseInt(pageSize));
            resultBean.setPaginator(paginator);
            resultBean.setData(new ArrayList<ActivityInviteSeven>());
        }
        
        
        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        
        LogUtil.endLog(this.getClass().getName(), methodName);
        return resultBean;
    }
    
    
}
