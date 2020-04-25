package com.hyjf.admin.manager.config.banksetting.bankinterface;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonService;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.BankInterface;
import com.hyjf.mybatis.model.customize.admin.AdminBankInterfaceCustomize;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 手续费配置
 *
 * @author
 *
 */
@Controller
@RequestMapping(value = BankInterfaceDefine.REQUEST_MAPPING)
public class BankInterfaceController extends BaseController {

    @Autowired
    private BankInterfaceService bankInterfaceService;
    @Autowired
    private BorrowCommonService borrowCommonService;

    /**
     * 列表维护画面初始化
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(BankInterfaceDefine.INIT)
    @RequiresPermissions(BankInterfaceDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,
                             @ModelAttribute(BankInterfaceDefine.BANKINTERFACE_FORM) BankInterfaceBean form) {
        LogUtil.startLog(BankInterfaceController.class.toString(), BankInterfaceDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(BankInterfaceDefine.LIST_PATH);

        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(BankInterfaceController.class.toString(), BankInterfaceDefine.INIT);
        return modelAndView;
    }

    /**
     * 列表维护分页机能 页面初始化
     *
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, BankInterfaceBean form) {
        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("interfaceName",form.getInterfaceName());
        Integer recordCount = this.bankInterfaceService.getRecordCount(paraMap);
        if (recordCount != null && recordCount >0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), recordCount);
            paraMap.put("limitStart",paginator.getOffset());
            paraMap.put("limitEnd",paginator.getLimit());
            List<AdminBankInterfaceCustomize> recordList = this.bankInterfaceService.selectRecordList(paraMap);
            form.setPaginator(paginator);
            modelAndView.addObject("recordList",recordList);
        }
        modelAndView.addObject("pageUrl", request.getRequestURL()+"?"+request.getQueryString());
        modelAndView.addObject(BankInterfaceDefine.BANKINTERFACE_FORM, form);
    }

    /**
     * 禁用/启用
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(BankInterfaceDefine.USE_ACTION)
    @RequiresPermissions(BankInterfaceDefine.PERMISSIONS_MODIFY)
    public ModelAndView updateUseAction(HttpServletRequest request, BankInterfaceBean form) {
        LogUtil.startLog(BankInterfaceController.class.toString(), BankInterfaceDefine.USE_ACTION);

        ModelAndView modelAndView = new ModelAndView(BankInterfaceDefine.RE_LIST_PATH);
        BankInterface bankInterface = new BankInterface();
        bankInterface.setId(form.getId());

        if(form.getIsUsable() == 0){
            bankInterface.setIsUsable(1);
        }else {
            bankInterface.setIsUsable(0);
        }
        bankInterface.setUpdateTime(new Date());
        bankInterface.setUpdateUserName(ShiroUtil.getLoginUsername());
        bankInterface.setUpdateUserId(Integer.valueOf(ShiroUtil.getLoginUserId()));
        this.bankInterfaceService.updateRecord(bankInterface);
        LogUtil.endLog(BankInterfaceController.class.toString(), BankInterfaceDefine.USE_ACTION);
        return modelAndView;
    }

    /**
     * 删除接口配置信息
     *
     * @param request
     * @param id
     * @return
     */
    @RequestMapping(BankInterfaceDefine.DELETE_ACTION)
    @RequiresPermissions(BankInterfaceDefine.PERMISSIONS_DELETE)
    public ModelAndView deleteRecordAction(HttpServletRequest request, String id) {
        LogUtil.startLog(BankInterfaceController.class.toString(), BankInterfaceDefine.DELETE_ACTION);

        ModelAndView modelAndView = new ModelAndView(BankInterfaceDefine.RE_LIST_PATH);
        BankInterface bankInterface = new BankInterface();
        bankInterface.setIsDelete(1);
        bankInterface.setId(Integer.valueOf(id));
        bankInterface.setUpdateTime(new Date());
        bankInterface.setUpdateUserName(ShiroUtil.getLoginUsername());
        bankInterface.setUpdateUserId(Integer.valueOf(ShiroUtil.getLoginUserId()));
        this.bankInterfaceService.deleteRecord(bankInterface);
        LogUtil.endLog(BankInterfaceController.class.toString(), BankInterfaceDefine.DELETE_ACTION);
        return modelAndView;
    }

    /**
     * 画面迁移(含有id更新，不含有id添加)
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(BankInterfaceDefine.INFO_ACTION)
    @RequiresPermissions(value = { BankInterfaceDefine.PERMISSIONS_INFO, BankInterfaceDefine.PERMISSIONS_ADD,
            BankInterfaceDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
    public ModelAndView info(HttpServletRequest request, RedirectAttributes attr,
                             @ModelAttribute(BankInterfaceDefine.BANKINTERFACE_FORM) BankInterfaceBean form) {
        LogUtil.startLog(BankInterfaceController.class.toString(), BankInterfaceDefine.INFO_ACTION);
        ModelAndView modelAndView = new ModelAndView(BankInterfaceDefine.INFO_PATH);

        if (form.getId() != null) {
            BankInterface record = this.bankInterfaceService.getRecord(form.getId());
            modelAndView.addObject(BankInterfaceDefine.BANKINTERFACE_FORM, record);
        }
        LogUtil.endLog(BankInterfaceController.class.toString(), BankInterfaceDefine.INFO_ACTION);
        return modelAndView;
    }

    /**
     * 修改接口配置信息
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(value = BankInterfaceDefine.UPDATE_ACTION, method = RequestMethod.POST)
    @RequiresPermissions(BankInterfaceDefine.PERMISSIONS_MODIFY)
    public ModelAndView updateAction(HttpServletRequest request, BankInterfaceBean form) {
        LogUtil.startLog(BankInterfaceController.class.toString(), BankInterfaceDefine.UPDATE_ACTION);
        ModelAndView modelAndView = new ModelAndView(BankInterfaceDefine.INFO_PATH);
        form.setUpdateTime(new Date());
        form.setUpdateUserName(ShiroUtil.getLoginUsername());
        form.setUpdateUserId(Integer.valueOf(ShiroUtil.getLoginUserId()));
        // 更新
        this.bankInterfaceService.updateRecord(form);
        // 跳转页面用（info里面有）
        modelAndView.addObject(BankInterfaceDefine.SUCCESS, BankInterfaceDefine.SUCCESS);
        LogUtil.endLog(BankInterfaceController.class.toString(), BankInterfaceDefine.UPDATE_ACTION);
        return modelAndView;
    }

}
