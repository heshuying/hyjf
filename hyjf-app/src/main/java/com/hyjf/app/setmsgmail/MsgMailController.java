package com.hyjf.app.setmsgmail;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.BaseResultBeanFrontEnd;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by yaoyong on 2017/12/7.
 * <p>
 * 修改短信与邮件是否开启
 */

@Controller
@RequestMapping(value = MsgMailDefine.REQUEST_MAPPING)
public class MsgMailController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(MsgMailController.class);
    //类名
    public static final String THIS_CLASS = MsgMailController.class.getName();

    private final String TOKEN_ISINVALID_STATUS = "Token失效，请重新登录";
    @Autowired
    private MsgMailService msgMailService;

    @ResponseBody
    @RequestMapping(value = MsgMailDefine.SET_STATUS)
    public JSONObject setStatus(HttpServletRequest request, MsgMailRequest msgMailRequest) {
        LogUtil.startLog(THIS_CLASS, MsgMailDefine.SET_STATUS);
        JSONObject ret = new JSONObject();
        ret.put("request", MsgMailDefine.REQUEST_MAPPING + MsgMailDefine.SET_STATUS);
        String sign = request.getParameter("sign");

        // 取得加密用的Key
        String key = SecretUtil.getKey(sign);
        if (Validator.isNull(key)) {
            ret.put("status", "1");
            ret.put("statusDesc", "请求参数非法");
            return ret;
        }
        Integer userId = null;
        try {
            userId = SecretUtil.getUserId(sign);
        } catch (Exception e) { // token失效
            ret.put("status", BaseResultBeanFrontEnd.FAIL);
            ret.put("statusDesc", TOKEN_ISINVALID_STATUS);
            return ret;
        }
        if (userId == null) {
            ret.put("status", CustomConstants.APP_STATUS_FAIL);
            ret.put("statusDesc", TOKEN_ISINVALID_STATUS);
            return ret;
        }

        String smsOpenStatus = msgMailRequest.getSmsOpenStatus();
        String emailOpenStatus = msgMailRequest.getEmailOpenStatus();
        logger.info("短信开启状态:{}, 邮件开启状态:{}", smsOpenStatus, emailOpenStatus);
        try {
            msgMailService.updateStatusByUserId(userId, smsOpenStatus, emailOpenStatus);
            ret.put("status", "0");
            ret.put("statusDesc", "设置成功");
            return ret;
        } catch (Exception e) {
            logger.error("失败...", e);
            ret.put("status", "2");
            ret.put("statusDesc", "设置失败");
            return ret;
        }
    }
}
