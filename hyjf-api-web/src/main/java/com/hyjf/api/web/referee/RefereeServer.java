package com.hyjf.api.web.referee;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.web.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;

@Controller
@RequestMapping(value = RefereeDefine.REQUEST_MAPPING)
public class RefereeServer extends BaseController {

    @Autowired
    private RefereeService refereeService;

    /**
     * 更新推荐人信息,CRM平台调用
     * 
     * @param userId
     * @param SpreadsUserId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = RefereeDefine.UPDATE_REC_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public String updateRec(@RequestParam("userId") String userId, @RequestParam("spreadsUserId") String spreadsUserId,
        @RequestParam("operationName") String operationName, HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(RefereeDefine.THIS_CLASS, RefereeDefine.UPDATE_REC_ACTION);

        JSONObject ret = new JSONObject();
        // 根据userId检索用户是否存在
        int count = this.refereeService.countUserById(userId);
        if (count == 0) {
            String message = ValidatorFieldCheckUtil.getErrorMessage("null", "");
            message = message.replace("{label}", "用户id");
            ret.put(RefereeDefine.JSON_VALID_STATUS_KEY, "N");
            ret.put(RefereeDefine.JSON_VALID_INFO_KEY, message);
            return ret.toString();
        }
        // 根据推荐人ID检索推荐人是否存在
        count = this.refereeService.countUserById(spreadsUserId);
        if (count == 0) {
            String message = ValidatorFieldCheckUtil.getErrorMessage("null", "");
            message = message.replace("{label}", "推荐人ID");
            ret.put(RefereeDefine.JSON_VALID_STATUS_KEY, "N");
            ret.put(RefereeDefine.JSON_VALID_INFO_KEY, message);
            return ret.toString();
        }
        // 更新推荐人
        String ip = GetCilentIP.getIpAddr(request);
        int updateCount = this.refereeService.updateSpreadsUsers(userId, spreadsUserId, operationName, ip);
        if (updateCount == 0) {
            String message = "更新推荐人失败";
            ret.put(RefereeDefine.JSON_VALID_STATUS_KEY, "N");
            ret.put(RefereeDefine.JSON_VALID_INFO_KEY, message);
            return ret.toString();
        }
        // 没有错误时,返回y
        if (!ret.containsKey(RefereeDefine.JSON_VALID_INFO_KEY)) {
            ret.put(RefereeDefine.JSON_VALID_STATUS_KEY, RefereeDefine.JSON_VALID_STATUS_OK);
            // 查询推荐人信息
            UsersInfo referrer = this.refereeService.selectUserInfoByUserId(Integer.parseInt(spreadsUserId));
            Users referrerUsers = this.refereeService.selectUserByUserId(Integer.parseInt(spreadsUserId));
            ret.put(RefereeDefine.REFERRER, spreadsUserId);
            ret.put(RefereeDefine.REFERRER_USERNAME, referrerUsers.getUsername());
            ret.put(RefereeDefine.REFFER_ATTRIBUTE, referrer.getAttribute());
            ret.put(RefereeDefine.REFFER_MOBILE, referrerUsers.getMobile());
        }
        LogUtil.endLog(RefereeDefine.THIS_CLASS, RefereeDefine.UPDATE_REC_ACTION);
        return ret.toString();
    }

}
