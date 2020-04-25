package com.hyjf.admin.exception.authexception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.bank.service.user.autoup.AutoPlusService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.admin.AdminUserAuthExptionListCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;

@Controller
@RequestMapping(value = UserauthDefine.REQUEST_MAPPING)
public class UserauthExceptionController extends BaseController {

    @Autowired
    private AutoPlusService autoPlusService;
    
    @Autowired
    private UserauthExceptionService userauthService;
    
    private Logger logger = LoggerFactory.getLogger(UserauthExceptionController.class);

    /**
     * 权限维护画面初始化
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(UserauthDefine.USERAUTH_LIST_ACTION)
    @RequiresPermissions(UserauthDefine.PERMISSIONS_VIEW)
    public ModelAndView init(@ModelAttribute(UserauthDefine.USERAUTH_LIST_FORM) UserauthListCustomizeBean form) {
        LogUtil.startLog(UserauthDefine.THIS_CLASS, UserauthDefine.USERAUTH_LIST_ACTION);
        ModelAndView modelAndView = new ModelAndView(UserauthDefine.USER_AUTH_LIST_PATH);
        // 创建分页
        this.createPage(modelAndView, form);
        LogUtil.endLog(UserauthDefine.THIS_CLASS, UserauthDefine.USERAUTH_LIST_ACTION);
        return modelAndView;
    }

    /**
     * 创建权限维护分页机能
     *
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(ModelAndView modelAndView, UserauthListCustomizeBean form) {
        // 封装查询条件
        Map<String, Object> authUser = this.buildQueryCondition(form);
        int recordTotal = this.userauthService.countRecordTotal(authUser);
        if (recordTotal > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
            List<AdminUserAuthExptionListCustomize> recordList = this.userauthService.getExceptionRecordList(authUser,
                    paginator.getOffset(), paginator.getLimit());
            form.setPaginator(paginator);
            form.setRecordList(recordList);
            modelAndView.addObject(UserauthDefine.USERAUTH_LIST_FORM, form);
        }
    }


    /**
     * 构建查询条件
     *
     * @param form
     * @return
     */
    private Map<String, Object> buildQueryCondition(UserauthListCustomizeBean form) {
        // 封装查询条件
        Map<String, Object> authUser = new HashMap<String, Object>();
        authUser.put("userName", form.getUserName());
        authUser.put("recommendName", form.getRecommendName());
        // 出借授权状态
        authUser.put("autoInvesStatus", form.getAutoInvesStatus());
        // 债转授权状态
        authUser.put("autoCreditStatus", form.getAutoCreditStatus());
        // 授权时间
        authUser.put("invesAddTimeStart", form.getInvesAddTimeStart());
        authUser.put("invesAddTimeEnd", form.getInvesAddTimeEnd());
        // 签约到期日
        authUser.put("invesEndTimeStart", form.getInvestEndTimeStart());
        authUser.put("invesEndTimeEnd", form.getInvestEndTimeEnd());
        return authUser;
    }
    
    /**
     * 
     * 同步用户授权状态
     * @author sunss
     * @param userId
     * @param type 1自动出借授权  2债转授权
     * @return
     */
    @RequestMapping(value = UserauthDefine.USERAUTH_SYN_ACTION)
    @ResponseBody
    public JSONObject synUserAuth(@RequestParam Integer userId , @RequestParam Integer type ) {
        // 返回结果
        JSONObject result = new JSONObject();
        logger.info("同步用户授权状态，查询用户：{}", userId);
        BankCallBean retBean = autoPlusService.getUserAuthQUery(userId, type+"");

        try {
            if (retBean != null && BankCallConstant.RESPCODE_SUCCESS.equals(retBean.get(BankCallConstant.PARAM_RETCODE))) {
                this.autoPlusService.updateUserAuthState(userId, retBean);
                result.put("success", "0");
                result.put("msg", "查询成功！");
            } else {
                String retCode = retBean != null ? retBean.getRetCode() : "";
                String retMessage = this.autoPlusService.getBankRetMsg(retCode);
                result.put("success", "1");
                result.put("msg", StringUtils.isNotEmpty(retMessage) ? retMessage : "未知错误");
            }
        } catch (Exception e) {
            logger.error("授权查询出错", e);
            result.put("success", "1");
            result.put("msg", e.getMessage());
        }
        logger.info("queryUserAuth result is: {}", result.toJSONString());
        return result;
    }

}
