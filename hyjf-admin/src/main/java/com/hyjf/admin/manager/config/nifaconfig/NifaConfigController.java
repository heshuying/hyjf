/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.manager.config.nifaconfig;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.maintenance.admin.AdminService;
import com.hyjf.admin.manager.config.instconfig.InstConfigController;
import com.hyjf.admin.manager.config.instconfig.InstConfigDefine;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.SessionUtils;
import com.hyjf.mybatis.model.auto.NifaContractTemplate;
import com.hyjf.mybatis.model.auto.NifaFieldDefinition;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.AdminCustomize;
import com.hyjf.mybatis.model.customize.AdminSystem;
import com.hyjf.mybatis.model.customize.FddTempletCustomize;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author nixiaoling
 * @version NifaConfigBean, v0.1 2018/7/4 11:46
 */
@Controller
@RequestMapping(value = NifaConfigDefine.REQUEST_MAPPING)
public class NifaConfigController  extends BaseController {

    @Autowired
    private NifaConfigService nifaConfigService;
    @Autowired
    private AdminService adminService;

    /**
     * 权限维护画面初始化
     *
     * @param request
     * @param form
     * @return
     */
    //互金字段定义列表显示
    @RequestMapping(NifaConfigDefine.INIT)
    @RequiresPermissions(NifaConfigDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(NifaConfigDefine.HZRCONFIG_FORM) FieldDefinitionBean form) {
        LogUtil.startLog(NifaConfigController.class.toString(), NifaConfigDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(NifaConfigDefine.LIST_PATH_FIELD);
        // 创建分页
        this.filedCreatePage(request, modelAndView, form);
        LogUtil.endLog(NifaConfigController.class.toString(), NifaConfigDefine.INIT);
        return modelAndView;
    }

    /**
     * 创建分页机能(互金字段)
     *
     * @param request
     * @param modelAndView
     * @param form
     */
    private void filedCreatePage(HttpServletRequest request, ModelAndView modelAndView, FieldDefinitionBean form) {
        List<NifaFieldDefinition> recordList = nifaConfigService.getNifaFieldDefinitionList(-1, -1);
        if (recordList != null) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
            recordList = this.nifaConfigService.getNifaFieldDefinitionList(paginator.getOffset(), paginator.getLimit());
            List<NifaFieldDefinitionResponseBean> listShow = new ArrayList<NifaFieldDefinitionResponseBean>();
            SimpleDateFormat smp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for(NifaFieldDefinition nn:recordList){
                NifaFieldDefinitionResponseBean responseBean = new NifaFieldDefinitionResponseBean();
                BeanUtils.copyProperties(nn,responseBean);
                String updTime = smp.format(nn.getUpdateTime());
                responseBean.setUpdateDate(updTime);
                //
//                Users user = nifaConfigService.getUsersByUserId(nn.getUpdateUserId());
                AdminCustomize adminCustomize = adminService.getRecord(nn.getUpdateUserId());
                if(null!=adminCustomize){
                    responseBean.setUpdateUserName(adminCustomize.getUsername());
                }
                listShow.add(responseBean);
            }
            form.setPaginator(paginator);
            form.setRecordList(listShow);
            modelAndView.addObject(NifaConfigDefine.HZRCONFIG_FORM, form);
        }
    }

    //跳转到互金字段定义的添加或修改页面
    @RequestMapping(NifaConfigDefine.INFO_ACTION_FIELD)
    @RequiresPermissions(value = {NifaConfigDefine.PERMISSIONS_ADD, NifaConfigDefine.PERMISSIONS_MODIFY, NifaConfigDefine.PERMISSIONS_VIEW}, logical = Logical.OR)
    public ModelAndView info(HttpServletRequest request, @ModelAttribute(NifaConfigDefine.HZRCONFIG_FORM) FieldDefinitionBean form) {
        LogUtil.startLog(NifaConfigController.class.toString(), NifaConfigDefine.INFO_ACTION_FIELD);
        ModelAndView modelAndView = new ModelAndView(NifaConfigDefine.INFO_PATH_FIELD);
        NifaFieldDefinition record = null;
        if (StringUtils.isNotEmpty(form.getId())) {
            record = this.nifaConfigService.selectNifaFieldDefinitionInterfaceById(Integer.parseInt(form.getId()));
        }
        modelAndView.addObject(NifaConfigDefine.HZRCONFIG_FORM, record);
        LogUtil.endLog(NifaConfigController.class.toString(), NifaConfigDefine.INFO_ACTION_FIELD);
        return modelAndView;
    }
    /**
     * 跳转到互金字段定义的详细页面
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(NifaConfigDefine.DETAIL_ACTION_FIELD)
    @RequiresPermissions(value = {NifaConfigDefine.PERMISSIONS_VIEW}, logical = Logical.OR)
    public ModelAndView detailAction(HttpServletRequest request, @ModelAttribute(NifaConfigDefine.HZRCONFIG_FORM) FieldDefinitionBean form) {
        LogUtil.startLog(NifaConfigController.class.toString(), NifaConfigDefine.DETAIL_ACTION_FIELD);
        ModelAndView modelAndView = new ModelAndView(NifaConfigDefine.DETAIL_PATH_FIELD);
        NifaFieldDefinition record = null;
        if (StringUtils.isNotEmpty(form.getId())) {
            record = this.nifaConfigService.selectNifaFieldDefinitionInterfaceById(Integer.parseInt(form.getId()));
        }
        modelAndView.addObject(NifaConfigDefine.HZRCONFIG_FORM, record);
        LogUtil.endLog(NifaConfigController.class.toString(), NifaConfigDefine.DETAIL_PATH_FIELD);
        return modelAndView;
    }

    /**
     * 修改字段定义添加
     *
     * @param form
     * @return
     */
    @RequestMapping(NifaConfigDefine.UPDATE_ACTION_FIELD)
    @RequiresPermissions(NifaConfigDefine.PERMISSIONS_MODIFY)
    public ModelAndView updateField(NifaFieldDefinition form) {
        // 日志开始
        LogUtil.startLog(NifaConfigController.class.toString(), NifaConfigDefine.UPDATE_ACTION_FIELD);
        ModelAndView modelAndView = new ModelAndView(NifaConfigDefine.INFO_PATH_FIELD);
        //根据id查找字段信息
        NifaFieldDefinition nifaFieldDefinitionInterface = nifaConfigService.selectNifaFieldDefinitionInterfaceById(form.getId());
        AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
        nifaFieldDefinitionInterface.setUpdateUserId(Integer.parseInt(adminSystem.getId()));
        nifaFieldDefinitionInterface.setUpdateTime(new Date());
        //
        nifaFieldDefinitionInterface.setBorrowingRestrictions(form.getBorrowingRestrictions());
        nifaFieldDefinitionInterface.setJudgmentsBased(form.getJudgmentsBased());
        nifaFieldDefinitionInterface.setOverdueDefinition(form.getOverdueDefinition());
        nifaFieldDefinitionInterface.setOverdueProcess(form.getOverdueProcess());
        nifaFieldDefinitionInterface.setOverdueResponsibility(form.getOverdueResponsibility());
        nifaFieldDefinitionInterface.setRepayDateRule(form.getRepayDateRule());
        // 修改成功
        int result = nifaConfigService.updateRecordFieldDefinition(nifaFieldDefinitionInterface);
        modelAndView.addObject(NifaConfigDefine.SUCCESS, NifaConfigDefine.SUCCESS);
        // 日志结束
        LogUtil.endLog(NifaConfigController.class.toString(), NifaConfigDefine.UPDATE_ACTION_FIELD);
        return modelAndView;
    }

    /**
     * 插入字段表
     *
     * @param form
     * @return
     */
    @RequestMapping(NifaConfigDefine.INSERT_ACTION_FIELD)
    @RequiresPermissions(NifaConfigDefine.PERMISSIONS_ADD)
    public ModelAndView add(NifaFieldDefinition form) {
        // 日志开始
        LogUtil.startLog(NifaConfigController.class.toString(), NifaConfigDefine.INSERT_ACTION_FIELD);
        ModelAndView modelAndView = new ModelAndView(NifaConfigDefine.INFO_PATH_FIELD);

        AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
        form.setCreateUserId(Integer.parseInt(adminSystem.getId()));
        form.setCreateTime(new Date());
        form.setUpdateUserId(Integer.parseInt(adminSystem.getId()));
        form.setUpdateTime(new Date());
        // 成功插入
        int result = nifaConfigService.insertRecordFieldDefinition(form);
        modelAndView.addObject(NifaConfigDefine.SUCCESS, NifaConfigDefine.SUCCESS);
        // 日志结束
        LogUtil.endLog(NifaConfigController.class.toString(), NifaConfigDefine.UPDATE_ACTION_FIELD);
        return modelAndView;
    }

    //合同模版约列表
    @RequestMapping(NifaConfigDefine.INITCONTRACT)
    @RequiresPermissions(NifaConfigDefine.PERMISSIONS_VIEW)
    public ModelAndView initContract(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(NifaConfigDefine.HZRCONFIG_FORM) ContractTemplateBean form) {
        LogUtil.startLog(NifaConfigController.class.toString(), NifaConfigDefine.INITCONTRACT);
        ModelAndView modelAndView = new ModelAndView(NifaConfigDefine.LIST_PATH_CONTRACT);
        // 创建分页
        this.createPageContract(request, modelAndView, form);
        LogUtil.endLog(NifaConfigController.class.toString(), NifaConfigDefine.INIT);
        return modelAndView;
    }

    public void createPageContract(HttpServletRequest request, ModelAndView modelAndView, ContractTemplateBean form) {
        List<NifaContractTemplate> recordList = nifaConfigService.selectNifaContractTemplateInterface(-1, -1);
        List<NifaContractTemplateResponseBean> listResponseBean = new ArrayList<NifaContractTemplateResponseBean>();
        SimpleDateFormat smp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (recordList != null) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
            recordList = this.nifaConfigService.selectNifaContractTemplateInterface(paginator.getOffset(), paginator.getLimit());
            for(NifaContractTemplate cc:recordList){
                NifaContractTemplateResponseBean responseBean = new NifaContractTemplateResponseBean();
                BeanUtils.copyProperties(cc,responseBean);
                responseBean.setUpdateDate(smp.format(cc.getUpdateTime()));
//                Users users = nifaConfigService.getUsersByUserId(cc.getUpdateUserId());
                AdminCustomize adminCustomize = adminService.getRecord(cc.getUpdateUserId());
                if(null!=adminCustomize){
                    responseBean.setUpdateUserName(adminCustomize.getUsername());
                }
                listResponseBean.add(responseBean);
            }
            form.setPaginator(paginator);
            form.setRecordList(listResponseBean);
            modelAndView.addObject(NifaConfigDefine.HZRCONFIG_FORM, form);
        }
    }

    /**
     * 画面迁移(含有id更新，不含有id添加)
     *
     * @param request
     * @param form
     * @return
     */
    //跳转到合同模板条款的添加或修改页面
    @RequestMapping(NifaConfigDefine.INFO_ACTION_CONTRACT)
    @RequiresPermissions(value = {NifaConfigDefine.PERMISSIONS_ADD, NifaConfigDefine.PERMISSIONS_MODIFY}, logical = Logical.OR)
    public ModelAndView infoContract(HttpServletRequest request, @ModelAttribute(NifaConfigDefine.HZRCONFIG_FORM) ContractTemplateBean form) {
        LogUtil.startLog(NifaConfigController.class.toString(), NifaConfigDefine.INFO_ACTION_CONTRACT);
        ModelAndView modelAndView = new ModelAndView(NifaConfigDefine.INFO_PATH_CONTRACT);

        NifaContractTemplate record = null;
        if (StringUtils.isNotEmpty(form.getIds())) {
            record = this.nifaConfigService.selectNifaContractTemplateInterfaceById(Integer.parseInt(form.getIds()));
        }
        List<FddTempletCustomize> fddTempletCustomizeList = nifaConfigService.selectContractTempId();
        modelAndView.addObject(NifaConfigDefine.HZRCONFIG_FORM, record);
        modelAndView.addObject("tempIds", fddTempletCustomizeList);
        LogUtil.endLog(NifaConfigController.class.toString(), NifaConfigDefine.INFO_ACTION_CONTRACT);
        return modelAndView;
    }

    /**
     * 修改合同模板条款
     *
     * @param form
     * @return
     */
    @RequestMapping(NifaConfigDefine.UPDATE_ACTION_CONTRACT)
    @RequiresPermissions(NifaConfigDefine.PERMISSIONS_MODIFY)
    public ModelAndView updateContract(ContractTemplateBean form) {
        // 日志开始
        LogUtil.startLog(NifaConfigController.class.toString(), NifaConfigDefine.UPDATE_ACTION_CONTRACT);
        ModelAndView modelAndView = new ModelAndView(NifaConfigDefine.INFO_PATH_CONTRACT);
        //根据id查找字段信息
        NifaContractTemplate nifaContractTemplateInterface = nifaConfigService.selectNifaContractTemplateInterfaceById(Integer.parseInt(form.getIds()));
        AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
        nifaContractTemplateInterface.setUpdateUserId(Integer.parseInt(adminSystem.getId()));
        nifaContractTemplateInterface.setUpdateTime(new Date());
        //
        nifaContractTemplateInterface.setNormalDefinition(form.getNormalDefinition());
        nifaContractTemplateInterface.setPrepaymentDefinition(form.getPrepaymentDefinition());
        nifaContractTemplateInterface.setBorrowerPromises(form.getBorrowerPromises());
        nifaContractTemplateInterface.setLenderPromises(form.getLenderPromises());
        nifaContractTemplateInterface.setBorrowerObligation(form.getBorrowerObligation());
        nifaContractTemplateInterface.setConfidentiality(form.getConfidentiality());
        nifaContractTemplateInterface.setBreachContract(form.getBreachContract());
        nifaContractTemplateInterface.setApplicableLaw(form.getApplicableLaw());
        nifaContractTemplateInterface.setDisputeResolution(form.getDisputeResolution());
        nifaContractTemplateInterface.setOtherConditions(form.getOtherConditions());
        // 修改成功
        int result = nifaConfigService.updateNifaContractTemplateInterface(nifaContractTemplateInterface);
        modelAndView.addObject(NifaConfigDefine.SUCCESS, NifaConfigDefine.SUCCESS);
        // 日志结束
        LogUtil.endLog(NifaConfigController.class.toString(), NifaConfigDefine.UPDATE_ACTION_FIELD);
        return modelAndView;
    }

    /**
     * 插入合同模板条款
     *
     * @param form
     * @return
     */
    @RequestMapping(NifaConfigDefine.INSERT_ACTION_CONTRACT)
    @RequiresPermissions(NifaConfigDefine.PERMISSIONS_ADD)
    public ModelAndView addContract(ContractTemplateBean form) {
        // 日志开始
        LogUtil.startLog(NifaConfigController.class.toString(), NifaConfigDefine.INSERT_ACTION_FIELD);
        ModelAndView modelAndView = new ModelAndView(NifaConfigDefine.INFO_PATH_CONTRACT);
        AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
        NifaContractTemplate nifaContractTemplateInterface = new NifaContractTemplate();
        nifaContractTemplateInterface.setCreateUserId(Integer.parseInt(adminSystem.getId()));
        nifaContractTemplateInterface.setCreateTime(new Date());
        nifaContractTemplateInterface.setUpdateUserId(Integer.parseInt(adminSystem.getId()));
        nifaContractTemplateInterface.setUpdateTime(new Date());
        BeanUtils.copyProperties(form, nifaContractTemplateInterface);
        // 成功插入
        int result = nifaConfigService.insertNifaContractTemplateInterface(nifaContractTemplateInterface);
        modelAndView.addObject(NifaConfigDefine.SUCCESS, NifaConfigDefine.SUCCESS);
        // 日志结束
        LogUtil.endLog(NifaConfigController.class.toString(), NifaConfigDefine.UPDATE_ACTION_FIELD);
        return modelAndView;
    }

    /**
     * 删除合同模板条款
     *
     * @param request
     * @param ids
     * @return
     */
    @RequestMapping(NifaConfigDefine.DELETE_ACTION_CONTRACT)
    @RequiresPermissions(NifaConfigDefine.PERMISSIONS_DELETE)
    public ModelAndView contractDeleteAction(HttpServletRequest request, String ids) {
        LogUtil.startLog(InstConfigController.class.toString(), NifaConfigDefine.DELETE_ACTION_CONTRACT);
        ModelAndView modelAndView = new ModelAndView(NifaConfigDefine.RE_CONTRACT_PATH);
        // 解析json字符串
        List<Integer> recordList = JSONArray.parseArray(ids, Integer.class);
        nifaConfigService.deleteRecordContractTemplate(recordList);
        LogUtil.endLog(InstConfigController.class.toString(), InstConfigDefine.DELETE_ACTION);
        return modelAndView;
    }
}
