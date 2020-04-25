package com.hyjf.admin.manager.config.versionconfig;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.auto.Version;

/**
 * 惠天利配置页面
 * 
 * @author qingbing
 *
 */
@Controller
@RequestMapping(value = VersionConfigDefine.REQUEST_MAPPING)
public class VersionConfigController extends BaseController {

	@Autowired
	private VersionConfigService versionConfigService;
    /**
     * 类名
     */
    private static final String THIS_CLASS = VersionConfigController.class.getName();

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(VersionConfigDefine.INIT)
	@RequiresPermissions(VersionConfigDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request,@ModelAttribute(VersionConfigDefine.VERSIONCONFIG_FORM) VersionConfigBean form) {
		// 日志开始
		LogUtil.startLog(THIS_CLASS, VersionConfigDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(VersionConfigDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		// 日志结束
		LogUtil.endLog(THIS_CLASS, VersionConfigDefine.INIT);
		return modelAndView;
	}
	/**
	 * 查询
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(VersionConfigDefine.SEARCH_ACTION)
	@RequiresPermissions(VersionConfigDefine.PERMISSIONS_SEARCH)
	public ModelAndView searchAction(HttpServletRequest request,@ModelAttribute(VersionConfigDefine.VERSIONCONFIG_FORM) VersionConfigBean form) {
		// 日志开始
		LogUtil.startLog(THIS_CLASS, VersionConfigDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(VersionConfigDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		// 日志结束
		LogUtil.endLog(THIS_CLASS, VersionConfigDefine.INIT);
		return modelAndView;
	}
	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, VersionConfigBean form) {
		VersionConfigBean version = new VersionConfigBean();
    	if(StringUtils.isNotEmpty(form.getNameSrh())){
    		version.setNameSrh(form.getNameSrh());
    	}
    	if(StringUtils.isNotEmpty(form.getVersionSrh())){
    		version.setVersionSrh(form.getVersionSrh());
    	}
    	Integer count = this.versionConfigService.countRecord(version);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			List<Version> recordList  = this.versionConfigService.getRecordList(version, paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
		}
		//数据字典
		List<ParamName> versionName = this.versionConfigService.getParamNameList("VERSION_NAME");
		List<ParamName> isUpdate = this.versionConfigService.getParamNameList("IS_UPDATE");
		modelAndView.addObject("versionName",versionName);
		modelAndView.addObject("isUpdate",isUpdate);
		modelAndView.addObject(VersionConfigDefine.VERSIONCONFIG_FORM, form);
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
    @RequestMapping(value = VersionConfigDefine.INFO_ACTION)
    @Token(save = true)
    @RequiresPermissions(value = { VersionConfigDefine.PERMISSIONS_ADD, VersionConfigDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
    public ModelAndView moveToInfoAction(HttpServletRequest request, VersionConfigBean form) throws Exception {
        LogUtil.startLog(THIS_CLASS, VersionConfigDefine.INFO_ACTION);
        ModelAndView modelAndView = new ModelAndView(VersionConfigDefine.INFO_PATH);
        String ids = form.getIds();
        VersionConfigBean bean = new VersionConfigBean();
        if (Validator.isNotNull(ids)) {
            Integer id = Integer.valueOf(ids);
            bean.setId(id);
            // 根据主键检索数据
            Version record = this.versionConfigService.getRecord(id);
            BeanUtils.copyProperties(record, bean);
        }
		//数据字典
		List<ParamName> versionName = this.versionConfigService.getParamNameList("VERSION_NAME");
		List<ParamName> isUpdate = this.versionConfigService.getParamNameList("IS_UPDATE");
		modelAndView.addObject("versionName",versionName);
		modelAndView.addObject("isUpdate",isUpdate);
        modelAndView.addObject(VersionConfigDefine.VERSIONCONFIG_FORM, bean);
        LogUtil.endLog(THIS_CLASS, VersionConfigDefine.INFO_ACTION);
        return modelAndView;
    }
    /**
     * 添加信息
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(value = VersionConfigDefine.INSERT_ACTION, method = RequestMethod.POST)
    @Token(check = true, forward = VersionConfigDefine.TOKEN_INIT_PATH)
    @RequiresPermissions(value = { VersionConfigDefine.PERMISSIONS_ADD })
    public ModelAndView insertAction(HttpServletRequest request, VersionConfigBean form) {
        LogUtil.startLog(THIS_CLASS, VersionConfigDefine.INSERT_ACTION);
        ModelAndView modelAndView = new ModelAndView(VersionConfigDefine.INFO_PATH);
        // 画面验证
		this.validatorFieldCheck(modelAndView, form);
		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			//数据字典
			List<ParamName> versionName = this.versionConfigService.getParamNameList("VERSION_NAME");
			List<ParamName> isUpdate = this.versionConfigService.getParamNameList("IS_UPDATE");
			modelAndView.addObject("versionName",versionName);
			modelAndView.addObject("isUpdate",isUpdate);
			modelAndView.addObject(VersionConfigDefine.VERSIONCONFIG_FORM, form);
			return modelAndView;
		}
        this.versionConfigService.insertRecord(form);
        this.createPage(request, modelAndView, form);
        modelAndView.addObject(VersionConfigDefine.SUCCESS, VersionConfigDefine.SUCCESS);
        LogUtil.endLog(THIS_CLASS, VersionConfigDefine.INSERT_ACTION);
        return modelAndView;
    }

    /**
     * 修改信息
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(value = VersionConfigDefine.UPDATE_ACTION, method = RequestMethod.POST)
    @Token(check = true, forward = VersionConfigDefine.TOKEN_INIT_PATH)
    @RequiresPermissions(value = { VersionConfigDefine.PERMISSIONS_MODIFY })
    public ModelAndView updateAction(HttpServletRequest request, VersionConfigBean form) {
        LogUtil.startLog(THIS_CLASS, VersionConfigDefine.UPDATE_ACTION);
        ModelAndView modelAndView = new ModelAndView(VersionConfigDefine.INFO_PATH);
        // 画面验证
		this.validatorFieldCheck(modelAndView, form);
		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			//数据字典
			List<ParamName> versionName = this.versionConfigService.getParamNameList("VERSION_NAME");
			List<ParamName> isUpdate = this.versionConfigService.getParamNameList("IS_UPDATE");
			modelAndView.addObject("versionName",versionName);
			modelAndView.addObject("isUpdate",isUpdate);
			modelAndView.addObject(VersionConfigDefine.VERSIONCONFIG_FORM, form);
			return modelAndView;
		}

        this.versionConfigService.updateRecord(form);
        // 创建分页
        this.createPage(request, modelAndView, form);
        modelAndView.addObject(VersionConfigDefine.SUCCESS, VersionConfigDefine.SUCCESS);
        LogUtil.endLog(THIS_CLASS, VersionConfigDefine.UPDATE_ACTION);
        return modelAndView;
    }

    /**
     * 删除
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(VersionConfigDefine.DELETE_ACTION)
    @RequiresPermissions(VersionConfigDefine.PERMISSIONS_DELETE)
    public ModelAndView deleteRecordAction(HttpServletRequest request, RedirectAttributes attr, VersionConfigBean form) {
        LogUtil.startLog(THIS_CLASS, VersionConfigDefine.DELETE_ACTION);
        ModelAndView modelAndView = new ModelAndView(VersionConfigDefine.LIST_PATH);
        List<Integer> recordList = JSONArray.parseArray(form.getIds(), Integer.class);
        this.versionConfigService.deleteRecord(recordList);
        LogUtil.endLog(THIS_CLASS, VersionConfigDefine.DELETE_ACTION);
        modelAndView.setViewName("redirect:" + VersionConfigDefine.REQUEST_MAPPING + "/" + VersionConfigDefine.INIT);
        return modelAndView;
    }
    
	/**
	 * 画面校验
	 * 
	 * @param modelAndView
	 * @param form
	 */
	private void validatorFieldCheck(ModelAndView modelAndView, VersionConfigBean form) {
		// 系统名称
		boolean typeFlag = ValidatorFieldCheckUtil.validateRequired(modelAndView, "type", String.valueOf(form.getType()));
		// 版本号
		boolean verFlag = ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "version", form.getVersion(), 12, false);
		// 地址
		ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "url", form.getUrl(), 255, false);
		// 版本描述
		ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "content", form.getContent(), 500, false);

		if (typeFlag && verFlag && StringUtils.isNotEmpty(form.getVersion())) {
			Version version = this.versionConfigService.getVersionByCode(form.getId(), form.getType(), form.getVersion());
			if (version != null) {
					ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "type-verdioncode", "exists.type.versioncode");
			}
		}
	}
}
