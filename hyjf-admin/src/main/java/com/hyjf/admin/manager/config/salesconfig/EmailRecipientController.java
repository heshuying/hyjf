package com.hyjf.admin.manager.config.salesconfig;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.customize.AdminSystem;
import com.hyjf.mybatis.model.customize.SellDailyDistributionCustomize;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author lisheng
 * @version EmailRecipientController, v0.1 2018/7/23 16:41
 * 报表邮件收件人配置
 */
@Controller
@RequestMapping(value = EmailRecipientDefine.REQUEST_MAPPING)
public class EmailRecipientController {
    @Autowired
    EmailRecipientService emailRecipientService;

    /**
     * 画面初始化
     * @param form
     * @return
     */
    @RequestMapping(EmailRecipientDefine.INIT)
    public ModelAndView init(EmailRecipientBean form) {
        LogUtil.startLog(EmailRecipientController.class.toString(), EmailRecipientDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(EmailRecipientDefine.LIST_PATH);
        // 创建分页
        this.createPage(modelAndView, form);
        LogUtil.endLog(EmailRecipientController.class.toString(), EmailRecipientDefine.INIT);
        return modelAndView;
    }

    /**
     * 创建权限维护分页机能
     * @param modelAndView
     * @param form
     */
    private void createPage(ModelAndView modelAndView, EmailRecipientBean form) {
        int total = emailRecipientService.countRecordTotal(form);
        if (total > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), total);

            List<SellDailyDistributionCustomize> recordList = emailRecipientService.getRecordList(form, paginator.getOffset(),paginator.getLimit());
            form.setPaginator(paginator);
            form.setRecordList(recordList);
            modelAndView.addObject(EmailRecipientDefine.EMAIL_FORM, form);
        }
    }

    /**
     * 转跳到添加界面
     */
    @RequestMapping(EmailRecipientDefine.ADD_ACTION)
    private ModelAndView addAction() {
        ModelAndView modelAndView = new ModelAndView(EmailRecipientDefine.ADD_LIST_PATH);
        return modelAndView;
    }

    /**
     * 修改信息转跳
     * @param form
     */
    @RequestMapping(EmailRecipientDefine.JUMP_UPDATE_ACTION)
    private ModelAndView jumpUpdate(EmailRecipientBean form) {
        ModelAndView modelAndView = new ModelAndView(EmailRecipientDefine.ADD_LIST_PATH);
        SellDailyDistributionCustomize record = emailRecipientService.getRecordById(form.getId());
        modelAndView.addObject(EmailRecipientDefine.EMAIL_FORM, record);
        return modelAndView;
    }


    /**
     * 修改信息
     * @param form
     */
    @RequestMapping(EmailRecipientDefine.UPDATE_ACTION)
    private ModelAndView updateAction(EmailRecipientBean form) {
        ModelAndView modelAndView = new ModelAndView("redirect:"+EmailRecipientDefine.REQUEST_MAPPING+EmailRecipientDefine.INIT);
        String userName = this.getUserName();
        form.setUpdateName(userName);

        boolean b = emailRecipientService.updateRecord(form);
        return  modelAndView;
    }

    /**
     * 添加信息
     * @param form
     */
    @RequestMapping(EmailRecipientDefine.INSERT_ACTION)
    private ModelAndView insertAction(HttpServletRequest request,EmailRecipientBean form) {
        ModelAndView modelAndView = new ModelAndView("redirect:"+EmailRecipientDefine.REQUEST_MAPPING+EmailRecipientDefine.INIT);
        String userName = this.getUserName();
        form.setCreateName(userName);

        boolean b = emailRecipientService.insertRecord(form);
        return modelAndView;
    }
    /**
     * 禁用
     * @param form
     * @return
     */
    @RequestMapping(EmailRecipientDefine.FORBIDDEN_ACTION)
    public ModelAndView updateMessage( EmailRecipientBean form) {
        ModelAndView modelAndView = new ModelAndView("redirect:"+EmailRecipientDefine.REQUEST_MAPPING+EmailRecipientDefine.INIT);
        String userName = this.getUserName();
        form.setUpdateName(userName);
        form.setStatus(2);
        emailRecipientService.updateForbidden(form);
        return modelAndView;
    }

    /**
     * 启用
     * @param form
     * @return
     */
    @RequestMapping(EmailRecipientDefine.START_ACTION)
    public ModelAndView startMessage( EmailRecipientBean form) {
        ModelAndView modelAndView = new ModelAndView("redirect:"+EmailRecipientDefine.REQUEST_MAPPING+EmailRecipientDefine.INIT);
        String userName = this.getUserName();
        form.setUpdateName(userName);
        form.setStatus(1);
        emailRecipientService.updateForbidden(form);
        return modelAndView;
    }

    /**
     * 查看详情
     * @param form
     */
    @RequestMapping(EmailRecipientDefine.INFO_ACTION)
    private ModelAndView infoAction(EmailRecipientBean form) {
        ModelAndView modelAndView = new ModelAndView(EmailRecipientDefine.INFO_PATH);
        SellDailyDistributionCustomize record = emailRecipientService.getRecordById(form.getId());
        String email = record.getEmail();
        if (StringUtils.isNotBlank(email)) {
            List<String> strings = Arrays.asList(email.split(";"));
            record.setEmails(strings);
        }
        modelAndView.addObject(EmailRecipientDefine.EMAIL_FORM, record);
        return modelAndView;
    }

    /**
     * 校验邮箱地址
     * @param form
     */
    @RequestMapping(EmailRecipientDefine.CHECK_EMAIL_ACTION)
    @ResponseBody
    private JSONObject checkEmailAction(EmailRecipientBean form) {
       JSONObject jsonObject = new JSONObject();
        String[] array = form.getEmail().split(";");
        Set<String> set = new HashSet<String>();
        for(String str : array){
            set.add(str);
        }
        if(set.size() != array.length){
            jsonObject.put("result",false);//有重复
        }else{
            jsonObject.put("result",true);//不重复
        }
        return jsonObject;
    }



            
    /**
     * 获取当前登录用户
     * @return
     */
    public String getUserName() {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        AdminSystem users = (AdminSystem) session.getAttribute(CustomConstants.LOGIN_USER_INFO);
        String username = users.getUsername();
        return username;
    }




}
