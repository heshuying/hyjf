package com.hyjf.admin.manager.config.account.accountconfig;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.MerchantAccount;

/**
 * 
 * 账户配置Controller
 * @author liuyang
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年7月7日
 * @see 上午10:51:17
 */
@Controller
@RequestMapping(value = AccountConfigDefine.REQUEST_MAPPING)
public class AccountConfigController extends BaseController {

    @Autowired
    private AccountConfigService accountConfigService;

    /**
     * 
     * 账户配置初期化
     * @author liuyang
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AccountConfigDefine.INIT)
    @RequiresPermissions(AccountConfigDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, AccountConfigBean form) {
        LogUtil.startLog(AccountConfigDefine.THIS_CLASS, AccountConfigDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(AccountConfigDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        // 子账户类型
        modelAndView.addObject("subaccountType",
                this.accountConfigService.getParamNameList(CustomConstants.SUB_ACCOUNT_CLASS));
        LogUtil.endLog(AccountConfigDefine.THIS_CLASS, AccountConfigDefine.INIT);
        return modelAndView;
    }

    /**
     * 
     * 账户配置列表检索
     * @author liuyang
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AccountConfigDefine.SEARCH_ACTION)
    @RequiresPermissions(AccountConfigDefine.PERMISSIONS_SEARCH)
    public ModelAndView search(HttpServletRequest request, AccountConfigBean form) {
        LogUtil.startLog(AccountConfigDefine.THIS_CLASS, AccountConfigDefine.SEARCH_ACTION);
        ModelAndView modelAndView = new ModelAndView(AccountConfigDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        // 子账户类型
        modelAndView.addObject("subaccountType",
                this.accountConfigService.getParamNameList(CustomConstants.SUB_ACCOUNT_CLASS));
        LogUtil.endLog(AccountConfigDefine.THIS_CLASS, AccountConfigDefine.SEARCH_ACTION);
        return modelAndView;
    }

    /**
     * 
     * 账户配置分页维护技能
     * @author liuyang
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, AccountConfigBean form) {
        int count = this.accountConfigService.countSubAccountList(form);
        if (count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            List<MerchantAccount> recordList =
                    this.accountConfigService.searchSubAccountList(form, paginator.getOffset(), paginator.getLimit());
            form.setSubAccountList(recordList);
            form.setPaginator(paginator);
        }
        modelAndView.addObject(AccountConfigDefine.ACCOUNT_CONFIG_FORM, form);
    }

    /**
     * 
     * 画面迁移(含有id更新，不含有id添加)
     * @author liuyang
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AccountConfigDefine.INFO_ACTION)
    public ModelAndView info(HttpServletRequest request, AccountConfigBean form) {
        LogUtil.startLog(AccountConfigDefine.THIS_CLASS, AccountConfigDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(AccountConfigDefine.INFO_PATH);
        MerchantAccount record = new MerchantAccount();
        // 设置子账户自动转入 默认值:支持
        record.setTransferIntoFlg(1);
        // 设置子账户转出 默认值:支持
        record.setTransferOutFlg(1);
        if (StringUtils.isNotEmpty(form.getIds())) {
            record = this.accountConfigService.getAccountListInfo(form.getIds());
        }
        BeanUtils.copyProperties(record, form);
        modelAndView.addObject(AccountConfigDefine.ACCOUNT_CONFIG_FORM, form);
        // 子账户类型
        modelAndView.addObject("subaccountType",
                this.accountConfigService.getParamNameList(CustomConstants.SUB_ACCOUNT_CLASS));
        LogUtil.endLog(AccountConfigDefine.THIS_CLASS, AccountConfigDefine.INIT);
        return modelAndView;
    }

    /**
     * 
     * 子账户添加
     * @author liuyang
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AccountConfigDefine.INSERT_ACTION)
    @RequiresPermissions(AccountConfigDefine.PERMISSIONS_ADD)
    public ModelAndView insertAction(HttpServletRequest request, AccountConfigBean form) {
        LogUtil.startLog(AccountConfigDefine.THIS_CLASS, AccountConfigDefine.INSERT_ACTION);
        ModelAndView modelAndView = new ModelAndView(AccountConfigDefine.INFO_PATH);
        // 表单校验
        this.validatorFieldCheck(modelAndView, form);
        if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
            // 子账户类型
            modelAndView.addObject("subaccountType",
                    this.accountConfigService.getParamNameList(CustomConstants.SUB_ACCOUNT_CLASS));
            modelAndView.addObject(AccountConfigDefine.ACCOUNT_CONFIG_FORM, form);
            LogUtil.errorLog(AccountConfigDefine.THIS_CLASS, AccountConfigDefine.INSERT_ACTION, "输入内容验证失败", null);
            return modelAndView;
        }
        // 插入
        this.accountConfigService.insertAccountListInfo(form);
        modelAndView.addObject(AccountConfigDefine.SUCCESS, AccountConfigDefine.SUCCESS);
        LogUtil.endLog(AccountConfigDefine.THIS_CLASS, AccountConfigDefine.INSERT_ACTION);
        return modelAndView;
    }

    /**
     * 
     * 子账户编辑
     * @author liuyang
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AccountConfigDefine.UPDATE_ACTION)
    @RequiresPermissions(AccountConfigDefine.PERMISSIONS_MODIFY)
    public ModelAndView updateAction(HttpServletRequest request, AccountConfigBean form) {
        LogUtil.startLog(AccountConfigDefine.THIS_CLASS, AccountConfigDefine.UPDATE_ACTION);
        ModelAndView modelAndView = new ModelAndView(AccountConfigDefine.INFO_PATH);
        // 表单校验
        this.validatorFieldCheck(modelAndView, form);
        if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
            // 子账户类型
            modelAndView.addObject("subaccountType",
                    this.accountConfigService.getParamNameList(CustomConstants.SUB_ACCOUNT_CLASS));
            modelAndView.addObject(AccountConfigDefine.ACCOUNT_CONFIG_FORM, form);
            LogUtil.errorLog(AccountConfigDefine.THIS_CLASS, AccountConfigDefine.UPDATE_ACTION, "输入内容验证失败", null);
            return modelAndView;
        }
        this.accountConfigService.updateAccountListInfo(form);
        modelAndView.addObject(AccountConfigDefine.SUCCESS, AccountConfigDefine.SUCCESS);
        LogUtil.endLog(AccountConfigDefine.THIS_CLASS, AccountConfigDefine.UPDATE_ACTION);
        return modelAndView;
    }

    /**
     * 
     * CheckAction
     * @author liuyang
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = AccountConfigDefine.CHECK_ACTION, method = RequestMethod.POST)
    @RequiresPermissions(value = { AccountConfigDefine.PERMISSIONS_ADD, AccountConfigDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
    public String checkAction(HttpServletRequest request) {
        LogUtil.startLog(AccountConfigDefine.THIS_CLASS, AccountConfigDefine.CHECK_ACTION);
        String name = request.getParameter("name");
        String param = request.getParameter("param");
        String ids = request.getParameter("ids");
        JSONObject ret = new JSONObject();
        // 子账户名称
        if ("subAccountName".equals(name)) {
            // 根据子账户名称检索,是否重复
            int count = this.accountConfigService.countAccountListInfoBySubAccountName(ids, param);
            if (count > 0) {
                String message = ValidatorFieldCheckUtil.getErrorMessage("repeat", "");
                message = message.replace("{label}", "子账户名称");
                ret.put(AccountConfigDefine.JSON_VALID_INFO_KEY, message);
            }
        }
        // 子账户代号
        if ("subAccountCode".equals(name)) {
            // 根据子账户代号检索,是否重复
            int result = this.accountConfigService.countAccountListInfoBySubAccountCode(ids, param);
            if (result > 0) {
                String message = ValidatorFieldCheckUtil.getErrorMessage("repeat", "");
                message = message.replace("{label}", "子账户代号");
                ret.put(AccountConfigDefine.JSON_VALID_INFO_KEY, message);
            }
        }
        // 没有错误时,返回y
        if (!ret.containsKey(AccountConfigDefine.JSON_VALID_INFO_KEY)) {
            ret.put(AccountConfigDefine.JSON_VALID_STATUS_KEY, AccountConfigDefine.JSON_VALID_STATUS_OK);
        }
        LogUtil.endLog(AccountConfigDefine.THIS_CLASS, AccountConfigDefine.CHECK_ACTION);
        return ret.toString();
    }

    /**
     * 画面校验
     * 
     * @param modelAndView
     * @param form
     */
    private void validatorFieldCheck(ModelAndView modelAndView, AccountConfigBean form) {
        // 子账户名称
        ValidatorFieldCheckUtil.validateRequired(modelAndView, "subAccountName", form.getSubAccountName());

        // 子账户类型
        ValidatorFieldCheckUtil.validateRequired(modelAndView, "subAccountType", form.getSubAccountType());

        // 子账户代码
        ValidatorFieldCheckUtil.validateRequired(modelAndView, "subAccountCode", form.getSubAccountCode());

        // 排序
        ValidatorFieldCheckUtil.validateRequired(modelAndView, "order", String.valueOf(form.getSort()));
    }
}
