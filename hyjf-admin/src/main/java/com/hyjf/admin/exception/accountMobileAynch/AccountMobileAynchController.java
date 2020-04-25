package com.hyjf.admin.exception.accountMobileAynch;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.AccountMobileAynch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author lisheng
 * @version AccountMobileAynchController, v0.1 2018/5/9 15:16
 */
@Controller
@RequestMapping(value = AccountMobileAynchDefine.REQUEST_MAPPING)
public class AccountMobileAynchController {

    @Autowired
    private  AccountMobileAynchService accountMobileAynchService;
    /** 类名 */
    private static final String THIS_CLASS = AccountMobileAynchController.class.getName();
    /**
     * 画面初始化
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AccountMobileAynchDefine.INIT)
    public ModelAndView init(HttpServletRequest request, AccountMobileAynchBean form) {
        LogUtil.startLog(THIS_CLASS, AccountMobileAynchDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(AccountMobileAynchDefine.MOBILE_LIST);
        // 创建分页
        this.createPage(request, modelAndView, form,AccountMobileAynchDefine.MOBILE_FLAG);
        LogUtil.endLog(THIS_CLASS, AccountMobileAynchDefine.INIT);
        return modelAndView;
    }

    /**
     * 分页技能维护
     *
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, AccountMobileAynchBean form,int flag) {
        // 数量
        int count = accountMobileAynchService.countMobileAccountAynch(form,flag);
        if (count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            List<AccountMobileAynch> accountMobileAynches = accountMobileAynchService.searchAccountMobileAynch(form, paginator.getOffset(), paginator.getLimit(),flag);
            form.setPaginator(paginator);
            form.setRecordList(accountMobileAynches);
        }
        modelAndView.addObject(AccountMobileAynchDefine.FORM, form);
    }



    /**
     *
     * 手机号列表检索Action
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AccountMobileAynchDefine.SEARCH_MOBILE_ACTION)
    public ModelAndView search(HttpServletRequest request, AccountMobileAynchBean form) {
        LogUtil.startLog(THIS_CLASS, AccountMobileAynchDefine.SEARCH_MOBILE_ACTION);
        ModelAndView modelAndView = new ModelAndView(AccountMobileAynchDefine.MOBILE_LIST);
        // 分页
        this.createPage(request, modelAndView, form,AccountMobileAynchDefine.MOBILE_FLAG);
        LogUtil.endLog(THIS_CLASS, AccountMobileAynchDefine.SEARCH_MOBILE_ACTION);
        return modelAndView;
    }

    /**
     *
     * 银行卡列表检索Action
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AccountMobileAynchDefine.SEARCH_ACCOUNT_ACTION)
    public ModelAndView searchAccount(HttpServletRequest request, AccountMobileAynchBean form) {
        LogUtil.startLog(THIS_CLASS, AccountMobileAynchDefine.SEARCH_ACCOUNT_ACTION);
        ModelAndView modelAndView = new ModelAndView(AccountMobileAynchDefine.ACCOUNT_LIST);
        // 分页updateBankCardExceptionAction
        this.createPage(request, modelAndView, form,AccountMobileAynchDefine.ACCOUNT_FLAG);
        LogUtil.endLog(THIS_CLASS, AccountMobileAynchDefine.SEARCH_ACCOUNT_ACTION);
        return modelAndView;
    }

    /**
     * 添加同步手机号用户
     * @param request
     * @param form 用户名
     */
    @RequestMapping(AccountMobileAynchDefine.ADD_ACTION)
    @ResponseBody
    public JSONObject addMobileSynch(HttpServletRequest request, AccountMobileAynchBean form) {
        String username = form.getUsername();
        Integer flag = accountMobileAynchService.insertAccountMobileAynch(username,form.getFlag()+"");
        JSONObject result = new JSONObject();
         if(flag==0){
             result.put("success", true);
             result.put("msg", "添加成功");
         }else if(flag==1){
             result.put("success", false);
             result.put("msg", "当前用户已有未同步数据，无法重复添加");
         }else if(flag==2){
             result.put("success", false);
             result.put("msg", "添加失败");
         }

        return  result;
    }
    /**
     * 手机号添加界面转跳
     * @param request
     * @param form 用户名
     */
    @RequestMapping(AccountMobileAynchDefine.TO_ADD_MOBILE_ACTION)
    public ModelAndView toAddMobileSynch(HttpServletRequest request, AccountMobileAynchBean form){
        ModelAndView modelAndView = new ModelAndView(AccountMobileAynchDefine.TO_ADD_ACTION_LIST);
        form.setFlag(AccountMobileAynchDefine.MOBILE_FLAG);
        modelAndView.addObject(AccountMobileAynchDefine.FORM, form);
        return modelAndView;
    }

    /**
     * 银行卡添加界面转跳
     * @param request
     * @param form 用户名
     */
    @RequestMapping(AccountMobileAynchDefine.TO_ADD_ACCOUNT_ACTION)
    public ModelAndView toAddAccounSynch(HttpServletRequest request, AccountMobileAynchBean form){
        ModelAndView modelAndView = new ModelAndView(AccountMobileAynchDefine.TO_ADD_ACTION_LIST);
        form.setFlag(AccountMobileAynchDefine.ACCOUNT_FLAG);
        modelAndView.addObject(AccountMobileAynchDefine.FORM, form);
        return modelAndView;
    }
   /**
     * 手机号修改界面  查询
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AccountMobileAynchDefine.MOBILE)
    public ModelAndView mobileList(HttpServletRequest request, AccountMobileAynchBean form) {
        LogUtil.startLog(THIS_CLASS, AccountMobileAynchDefine.MOBILE);
        ModelAndView modelAndView = new ModelAndView(AccountMobileAynchDefine.MOBILE_LIST);
        // 创建分页
        this.createPage(request, modelAndView, form,AccountMobileAynchDefine.MOBILE_FLAG);
        LogUtil.endLog(THIS_CLASS, AccountMobileAynchDefine.MOBILE);
        return modelAndView;
    }

    /**
     * 银行卡修改界面  查询
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AccountMobileAynchDefine.ACCOUTN)
    public ModelAndView accountList(HttpServletRequest request, AccountMobileAynchBean form) {
        LogUtil.startLog(THIS_CLASS, AccountMobileAynchDefine.ACCOUTN);
        ModelAndView modelAndView = new ModelAndView(AccountMobileAynchDefine.ACCOUNT_LIST);
        // 创建分页
        this.createPage(request, modelAndView, form,AccountMobileAynchDefine.ACCOUNT_FLAG);
        LogUtil.endLog(THIS_CLASS, AccountMobileAynchDefine.ACCOUTN);
        return modelAndView;
    }

    /**
     * 删除信息
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AccountMobileAynchDefine.DELETE_ACTION)
    public ModelAndView deleteMessage(HttpServletRequest request, AccountMobileAynchBean form){
        ModelAndView modelAndView;
        if(form.getFlag()==AccountMobileAynchDefine.MOBILE_FLAG){
            modelAndView= new ModelAndView("redirect:"+AccountMobileAynchDefine.REQUEST_MAPPING+"/"+AccountMobileAynchDefine.MOBILE);
            this.createPage(request, modelAndView, form,AccountMobileAynchDefine.MOBILE_FLAG);
        }else{
            modelAndView= new ModelAndView("redirect:"+AccountMobileAynchDefine.REQUEST_MAPPING+"/"+AccountMobileAynchDefine.ACCOUTN);
            this.createPage(request, modelAndView, form,AccountMobileAynchDefine.ACCOUNT_FLAG);
        }
        accountMobileAynchService.deleteMessage(form.getId());
        return modelAndView;
    }
}
