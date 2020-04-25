package com.hyjf.admin.manager.config.borrow.projecttype;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.BorrowProjectRepay;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.ProjectTypeCustomize;

/**
 * @package com.hyjf.admin.maintenance.BorrowType
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = ProjectTypeDefine.REQUEST_MAPPING)
public class ProjectTypeController extends BaseController {

    @Autowired
    private ProjectTypeService projectTypeService;

    /**
     * 权限汇直投项目类型画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(ProjectTypeDefine.INIT)
    @RequiresPermissions(ProjectTypeDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,
        @ModelAttribute(ProjectTypeDefine.PROJECTTYPE_FORM) ProjectTypeBean form) {

        LogUtil.startLog(ProjectTypeController.class.toString(), ProjectTypeDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(ProjectTypeDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(ProjectTypeDefine.THIS_CLASS, ProjectTypeDefine.INIT);
        return modelAndView;
    }

    /**
     * 创建权限汇直投项目类型分页机能
     * 
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, ProjectTypeBean form) {

        List<ProjectTypeCustomize> list = this.projectTypeService.getProjectTypeList(new ProjectTypeCustomize());
        if (list != null && list.size() > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), list.size());
            ProjectTypeCustomize projectTypeCustomize = new ProjectTypeCustomize();
            projectTypeCustomize.setLimitStart(paginator.getOffset());
            projectTypeCustomize.setLimitEnd(paginator.getLimit());
            list = this.projectTypeService.getProjectTypeList(projectTypeCustomize);
            form.setPaginator(paginator);
            modelAndView.addObject("list", list);
        }
        modelAndView.addObject(ProjectTypeDefine.PROJECTTYPE_FORM, form);
    }

    /**
     * 迁移到权限汇直投项目类型详细画面
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(value = ProjectTypeDefine.INFO_ACTION)
    @RequiresPermissions(value = { ProjectTypeDefine.PERMISSIONS_ADD, ProjectTypeDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
    public ModelAndView moveToInfoAction(HttpServletRequest request, ProjectTypeBean form) {
        LogUtil.startLog(ProjectTypeDefine.THIS_CLASS, ProjectTypeDefine.INFO_ACTION);
        ModelAndView modelAndView = new ModelAndView(ProjectTypeDefine.INFO_PATH);
        BorrowProjectType record = new BorrowProjectType();
        List<BorrowProjectRepay> selectRepay = null;
        record.setBorrowCd(form.getBorrowCd());
        boolean isExists = false;
        if (StringUtils.isNotEmpty(record.getBorrowCd())) {
            // 根据主键判断该条数据在数据库中是否存在
            isExists = this.projectTypeService.isExistsRecord(record);
        }

        if (isExists) {
            modelAndView.addObject("modifyFlag", "E");
            // 根据主键检索数据
            record = this.projectTypeService.getRecord(record);
            // 根据项目编号查询
            selectRepay = this.projectTypeService.selectRepay(record.getBorrowClass());
        } else {
            modelAndView.addObject("modifyFlag", "N");
        }

        modelAndView.addObject(ProjectTypeDefine.PROJECTTYPE_FORM, record);
        modelAndView.addObject("repayNames", selectRepay);
        // 回显checkbox标签
        List<BorrowStyle> selectStyles = this.projectTypeService.selectStyles();
        modelAndView.addObject("repayStyles", selectStyles);
        // 用户角色
        List<ParamName> investUsers = this.projectTypeService.getParamNameList("INVEST_USER");
        modelAndView.addObject("investUsers", investUsers);

        modelAndView.addObject("projectTypeList",
                this.projectTypeService.getParamNameList(CustomConstants.BORROW_PROJTCT));

        LogUtil.endLog(ProjectTypeDefine.THIS_CLASS, ProjectTypeDefine.INFO_ACTION);
        return modelAndView;
    }

    /**
     * 添加权限汇直投项目类型信息
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(value = ProjectTypeDefine.INSERT_ACTION, method = RequestMethod.POST)
    @RequiresPermissions(value = { ProjectTypeDefine.PERMISSIONS_ADD })
    public ModelAndView insertAction(HttpServletRequest request, ProjectTypeBean form) {

        LogUtil.startLog(ProjectTypeDefine.THIS_CLASS, ProjectTypeDefine.INSERT_ACTION);
        ModelAndView modelAndView = new ModelAndView(ProjectTypeDefine.INFO_PATH);

        // 画面验证
        this.validatorFieldCheck(modelAndView, form);

        if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {

            List<BorrowProjectRepay> selectRepay = new ArrayList<BorrowProjectRepay>();

            if (StringUtils.isNotEmpty(form.getMethodName())) {
                String name[] = form.getMethodName().split(",");
                if (name != null && name.length > 0) {
                    for (int i = 0; i < name.length; i++) {
                        BorrowProjectRepay borrowProjectRepay = new BorrowProjectRepay();
                        borrowProjectRepay.setRepayMethod(name[i]);
                        selectRepay.add(borrowProjectRepay);
                    }
                }
            }
            modelAndView.addObject("repayNames", selectRepay);

            // 用户角色
            List<ParamName> investUsers = this.projectTypeService.getParamNameList("INVEST_USER");
            modelAndView.addObject("investUsers", investUsers);

            // 回显checkbox标签
            List<BorrowStyle> selectStyles = this.projectTypeService.selectStyles();
            modelAndView.addObject("repayStyles", selectStyles);

            modelAndView.addObject(ProjectTypeDefine.PROJECTTYPE_FORM, form);
            LogUtil.errorLog(ProjectTypeDefine.THIS_CLASS, ProjectTypeDefine.UPDATE_ACTION, "输入内容验证失败", null);
            return modelAndView;
        }

        if (StringUtils.equals("N", form.getModifyFlag())) {
            this.projectTypeService.insertRecord(form);
        }
        modelAndView.addObject(ProjectTypeDefine.SUCCESS, ProjectTypeDefine.SUCCESS);
        LogUtil.endLog(ProjectTypeDefine.THIS_CLASS, ProjectTypeDefine.INSERT_ACTION);
        return modelAndView;
    }

    /**
     * 修改权限汇直投项目类型信息
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(value = ProjectTypeDefine.UPDATE_ACTION, method = RequestMethod.POST)
    @RequiresPermissions(value = { ProjectTypeDefine.PERMISSIONS_MODIFY })
    public ModelAndView updateAction(HttpServletRequest request, ProjectTypeBean form) {

        LogUtil.startLog(ProjectTypeDefine.THIS_CLASS, ProjectTypeDefine.UPDATE_ACTION);
        ModelAndView modelAndView = new ModelAndView(ProjectTypeDefine.INFO_PATH);
        // 画面验证
        this.validatorFieldCheck(modelAndView, form);

        if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {

            List<BorrowProjectRepay> selectRepay = new ArrayList<BorrowProjectRepay>();

            if (StringUtils.isNotEmpty(form.getMethodName())) {
                String name[] = form.getMethodName().split(",");
                if (name != null && name.length > 0) {
                    for (int i = 0; i < name.length; i++) {
                        BorrowProjectRepay borrowProjectRepay = new BorrowProjectRepay();
                        borrowProjectRepay.setRepayMethod(name[i]);
                        selectRepay.add(borrowProjectRepay);
                    }
                }
            }
            modelAndView.addObject("repayNames", selectRepay);

            // 用户角色
            List<ParamName> investUsers = this.projectTypeService.getParamNameList("INVEST_USER");
            modelAndView.addObject("investUsers", investUsers);

            // 回显checkbox标签
            List<BorrowStyle> selectStyles = this.projectTypeService.selectStyles();
            modelAndView.addObject("repayStyles", selectStyles);

            modelAndView.addObject(ProjectTypeDefine.PROJECTTYPE_FORM, form);
            LogUtil.errorLog(ProjectTypeDefine.THIS_CLASS, ProjectTypeDefine.UPDATE_ACTION, "输入内容验证失败", null);
            return modelAndView;
        }
        if (StringUtils.isNotEmpty(form.getBorrowCd())) {
            this.projectTypeService.updateRecord(form);
        }
        // 创建分页
        this.createPage(request, modelAndView, form);
        modelAndView.addObject(ProjectTypeDefine.SUCCESS, ProjectTypeDefine.SUCCESS);
        LogUtil.endLog(ProjectTypeDefine.THIS_CLASS, ProjectTypeDefine.UPDATE_ACTION);
        return modelAndView;
    }

    /**
     * 删除权限汇直投项目类型
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(ProjectTypeDefine.DELETE_ACTION)
    @RequiresPermissions(ProjectTypeDefine.PERMISSIONS_DELETE)
    public String deleteRecordAction(HttpServletRequest request, RedirectAttributes attr, ProjectTypeBean form) {
        LogUtil.startLog(ProjectTypeDefine.THIS_CLASS, ProjectTypeDefine.DELETE_ACTION);
        this.projectTypeService.deleteRecord(form.getBorrowCd());
		/*--------------------------add by LSY START-----------------------------------*/
        //删除asset表相应数据
        this.projectTypeService.deleteAsset(Integer.parseInt(form.getBorrowCd()));
		/*--------------------------add by LSY END-----------------------------------*/
        attr.addFlashAttribute(ProjectTypeDefine.PROJECTTYPE_FORM, form);
        LogUtil.endLog(ProjectTypeDefine.THIS_CLASS, ProjectTypeDefine.DELETE_ACTION);
        return "redirect:" + ProjectTypeDefine.REQUEST_MAPPING + "/" + ProjectTypeDefine.INIT;
    }

    /**
     * 检查手机号码或用户名唯一性
     * 
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = ProjectTypeDefine.CHECK_ACTION, method = RequestMethod.POST)
    @RequiresPermissions(value = { ProjectTypeDefine.PERMISSIONS_ADD, ProjectTypeDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
    public String checkAction(HttpServletRequest request) {
        LogUtil.startLog(ProjectTypeDefine.THIS_CLASS, ProjectTypeDefine.CHECK_ACTION);

        String borrowCd = request.getParameter("param");

        JSONObject ret = new JSONObject();
        // 检查项目名称唯一性
        int cnt = this.projectTypeService.borrowCdIsExists(borrowCd);
        if (cnt > 0) {
            String message = ValidatorFieldCheckUtil.getErrorMessage("repeat", "");
            message = message.replace("{label}", "项目编号");
            ret.put(ProjectTypeDefine.JSON_VALID_INFO_KEY, message);
        }
        // 没有错误时,返回y
        if (!ret.containsKey(ProjectTypeDefine.JSON_VALID_INFO_KEY)) {
            ret.put(ProjectTypeDefine.JSON_VALID_STATUS_KEY, ProjectTypeDefine.JSON_VALID_STATUS_OK);
        }
        LogUtil.endLog(ProjectTypeDefine.THIS_CLASS, ProjectTypeDefine.CHECK_ACTION);
        return ret.toString();
    }

    /**
     * 画面校验
     * 
     * @param modelAndView
     * @param form
     */
    private void validatorFieldCheck(ModelAndView modelAndView, ProjectTypeBean form) {
        // 项目类型
        boolean borrowCdFlag = ValidatorFieldCheckUtil.validateRequired(modelAndView, "borrowCd", form.getBorrowCd());
        // 项目编号
        ValidatorFieldCheckUtil.validateAlphaAndMaxLength(modelAndView, "borrowClass", form.getBorrowClass(), 20, true);
        // 权限名字
        ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "borrowName", form.getBorrowName(), 20, true);
        // 用户类型
        ValidatorFieldCheckUtil.validateRequired(modelAndView, "investUserType", form.getInvestUserType());
        // 状态
        ValidatorFieldCheckUtil.validateRequired(modelAndView, "status", form.getStatus());
        // 还款方式
        ValidatorFieldCheckUtil.validateRequired(modelAndView, "methodName", form.getMethodName());
        // 出借最小范围
        ValidatorFieldCheckUtil.validateSignlessNum(modelAndView, "investStart", form.getInvestStart(), 10, true);
        // 出借最大范围
        ValidatorFieldCheckUtil.validateSignlessNum(modelAndView, "investEnd", form.getInvestEnd(), 10, true);

        if ("N".equals(form.getModifyFlag()) && borrowCdFlag) {
            // 检查唯一性
            int cnt = this.projectTypeService.borrowCdIsExists(form.getBorrowCd());
            if (cnt > 0) {
                ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "borrowCd", "repeat");
            }
        }
    }
}
