package com.hyjf.admin.manager.config.applicant;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.annotate.Token;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.ConfigApplicant;

/**
 * 项目申请人配置页面
 * 
 * @author hbz
 *
 */
@Controller
@RequestMapping(value = ApplicantConfigDefine.REQUEST_MAPPING)
public class ApplicantConfigController extends BaseController {

	@Autowired
	private ApplicantConfigService applicantConfigService;
    /**
     * 类名
     */
    private static final String THIS_CLASS = ApplicantConfigController.class.getName();

	/**
	 * 惠天利配置画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ApplicantConfigDefine.INIT)
	@RequiresPermissions(ApplicantConfigDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request,@ModelAttribute(ApplicantConfigDefine.VERSIONCONFIG_FORM) ApplicantConfigBean form) {
		// 日志开始
		LogUtil.startLog(THIS_CLASS, ApplicantConfigDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(ApplicantConfigDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		// 日志结束
		LogUtil.endLog(THIS_CLASS, ApplicantConfigDefine.INIT);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, ApplicantConfigBean form) {
		ApplicantConfigBean applicantConfig = new ApplicantConfigBean();
		//查询条件
		if(form!=null && form.getApplicant()!=null && !"".equals(form.getApplicant())){
			applicantConfig.setApplicant(form.getApplicant());
		}
    	Integer count = this.applicantConfigService.countRecord(applicantConfig);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			List<ConfigApplicant> recordList  = this.applicantConfigService.getRecordList(applicantConfig, paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
		}
		//数据字典
//		List<ParamName> versionName = this.applicantConfigService.getParamNameList("VERSION_NAME");
//		List<ParamName> isUpdate = this.applicantConfigService.getParamNameList("IS_UPDATE");
//		modelAndView.addObject("versionName",versionName);
//		modelAndView.addObject("isUpdate",isUpdate);
		modelAndView.addObject(ApplicantConfigDefine.VERSIONCONFIG_FORM, form);
	}

    /**
     * 迁移到详细画面
     * 
     * @param request
     * @param form
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @RequestMapping(value = ApplicantConfigDefine.INFO_ACTION)
    @Token(save = true)
    @RequiresPermissions(value = { ApplicantConfigDefine.PERMISSIONS_ADD, ApplicantConfigDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
    public ModelAndView moveToInfoAction(HttpServletRequest request, ApplicantConfigBean form) throws Exception {
        LogUtil.startLog(THIS_CLASS, ApplicantConfigDefine.INFO_ACTION);
        ModelAndView modelAndView = new ModelAndView(ApplicantConfigDefine.INFO_PATH);
        String ids = form.getIds();
        ApplicantConfigBean bean = new ApplicantConfigBean();
        if (Validator.isNotNull(ids)) {
            Integer id = Integer.valueOf(ids);
            bean.setId(id);
            // 根据主键检索数据
            ConfigApplicant record = this.applicantConfigService.getRecord(id);
            BeanUtils.copyProperties(record, bean);
        }
		//数据字典
//		List<ParamName> versionName = this.applicantConfigService.getParamNameList("VERSION_NAME");
//		List<ParamName> isUpdate = this.applicantConfigService.getParamNameList("IS_UPDATE");
//		modelAndView.addObject("versionName",versionName);
//		modelAndView.addObject("isUpdate",isUpdate);
        modelAndView.addObject(ApplicantConfigDefine.VERSIONCONFIG_FORM, bean);
        LogUtil.endLog(THIS_CLASS, ApplicantConfigDefine.INFO_ACTION);
        return modelAndView;
    }
    /**
     * 添加信息
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(value = ApplicantConfigDefine.INSERT_ACTION, method = RequestMethod.POST)
    @Token(check = true, forward = ApplicantConfigDefine.TOKEN_INIT_PATH)
    @RequiresPermissions(value = { ApplicantConfigDefine.PERMISSIONS_ADD })
    public ModelAndView insertAction(HttpServletRequest request, ApplicantConfigBean form) {
        LogUtil.startLog(THIS_CLASS, ApplicantConfigDefine.INSERT_ACTION);
        ModelAndView modelAndView = new ModelAndView(ApplicantConfigDefine.INFO_PATH);
        
        this.applicantConfigService.insertRecord(form);
        this.createPage(request, modelAndView, form);
        modelAndView.addObject(ApplicantConfigDefine.SUCCESS, ApplicantConfigDefine.SUCCESS);
        LogUtil.endLog(THIS_CLASS, ApplicantConfigDefine.INSERT_ACTION);
        return modelAndView;
    }

    /**
     * 修改信息
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(value = ApplicantConfigDefine.UPDATE_ACTION, method = RequestMethod.POST)
    @Token(check = true, forward = ApplicantConfigDefine.TOKEN_INIT_PATH)
    @RequiresPermissions(value = { ApplicantConfigDefine.PERMISSIONS_MODIFY })
    public ModelAndView updateAction(HttpServletRequest request, ApplicantConfigBean form) {
        LogUtil.startLog(THIS_CLASS, ApplicantConfigDefine.UPDATE_ACTION);
        ModelAndView modelAndView = new ModelAndView(ApplicantConfigDefine.INFO_PATH);
        // 画面验证
        this.applicantConfigService.updateRecord(form);
        // 创建分页
        this.createPage(request, modelAndView, form);
        modelAndView.addObject(ApplicantConfigDefine.SUCCESS, ApplicantConfigDefine.SUCCESS);
        LogUtil.endLog(THIS_CLASS, ApplicantConfigDefine.UPDATE_ACTION);
        return modelAndView;
    }

    /**
     * 删除
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(ApplicantConfigDefine.DELETE_ACTION)
    @RequiresPermissions(ApplicantConfigDefine.PERMISSIONS_DELETE)
    public ModelAndView deleteRecordAction(HttpServletRequest request, RedirectAttributes attr, ApplicantConfigBean form) {
        LogUtil.startLog(THIS_CLASS, ApplicantConfigDefine.DELETE_ACTION);
        ModelAndView modelAndView = new ModelAndView(ApplicantConfigDefine.LIST_PATH);
        List<Integer> recordList = JSONArray.parseArray(form.getIds(), Integer.class);
        this.applicantConfigService.deleteRecord(recordList);
        LogUtil.endLog(THIS_CLASS, ApplicantConfigDefine.DELETE_ACTION);
        modelAndView.setViewName("redirect:" + ApplicantConfigDefine.REQUEST_MAPPING + "/" + ApplicantConfigDefine.INIT);
        return modelAndView;
    }
}
