package com.hyjf.admin.promotion.message;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.mybatis.messageprocesser.MessageDefine;

/**
 * 手续费配置
 * 
 * @author
 *
 */
@Controller(value = "message2Controller")
@RequestMapping(value = com.hyjf.admin.promotion.message.MessageDefine.REQUEST_MAPPING)
public class MessageController extends BaseController {

    /**
     * 列表维护画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(com.hyjf.admin.promotion.message.MessageDefine.INIT)
    @RequiresPermissions(com.hyjf.admin.promotion.message.MessageDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("/promotion/message/message");
        List<MessageRecord> recordList = new ArrayList<>();
        MessageRecord smsQuenem = new MessageRecord("短信主队列", RedisUtils.llen(MessageDefine.SMSQUENEM) + "");
        MessageRecord smsQuenes = new MessageRecord("短信副队列", RedisUtils.llen(MessageDefine.SMSQUENES) + "");
        MessageRecord mailQuenem = new MessageRecord("邮件主队列", RedisUtils.llen(MessageDefine.MAILQUENEM) + "");
        MessageRecord mailQuenes = new MessageRecord("邮件副队列", RedisUtils.llen(MessageDefine.MAILQUENES) + "");
        MessageRecord appMsQuenem = new MessageRecord("app消息主队列", RedisUtils.llen(MessageDefine.APPMSQUENEM) + "");
        MessageRecord appMsQuenes = new MessageRecord("app消息副队列", RedisUtils.llen(MessageDefine.APPMSQUENES) + "");
        recordList.add(smsQuenem);
        recordList.add(smsQuenes);
        recordList.add(mailQuenem);
        recordList.add(mailQuenes);
        recordList.add(appMsQuenem);
        recordList.add(appMsQuenes);
        modelAndView.addObject("recordList", recordList);
        return modelAndView;
    }

}
