package com.hyjf.admin.manager.vip.gradeconfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.auto.VipInfo;

/**
 * 手续费配置
 * 
 * @author
 *
 */
@Controller
@RequestMapping(value = GradeConfigDefine.REQUEST_MAPPING)
public class GradeConfigController extends BaseController {

    @Autowired
    private GradeConfigService gradeConfigService;

	/**
	 * 列表维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(GradeConfigDefine.INIT)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(GradeConfigDefine.CONFIGFEE_FORM) GradeConfigBean form) {
		LogUtil.startLog(GradeConfigController.class.toString(), GradeConfigDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(GradeConfigDefine.LIST_PATH);
		
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(GradeConfigController.class.toString(), GradeConfigDefine.INIT);
		return modelAndView;
	}

	/**
	 * 文章列表维护分页机能 页面初始化
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, GradeConfigBean form) {
		List<VipInfo> recordList = this.gradeConfigService.getRecordList(form, -1, -1);
		if (recordList != null) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			recordList = this.gradeConfigService.getRecordList(form, paginator.getOffset(),
					paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(GradeConfigDefine.CONFIGFEE_FORM, form);
		}
	}

	/**
	 * 画面迁移(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(GradeConfigDefine.INFO_ACTION)
	@RequiresPermissions(value = { GradeConfigDefine.PERMISSIONS_INFO, GradeConfigDefine.PERMISSIONS_ADD,
	        GradeConfigDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView info(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(GradeConfigDefine.CONFIGFEE_FORM) GradeConfigBean form) {
		LogUtil.startLog(GradeConfigDefine.class.toString(), GradeConfigDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(GradeConfigDefine.INFO_PATH);

		if (StringUtils.isNotEmpty(form.getIds())) {
			Integer id = Integer.valueOf(form.getIds());
			VipInfo record = this.gradeConfigService.getRecord(id);
			modelAndView.addObject(GradeConfigDefine.CONFIGFEE_FORM, record);
		}
		// vip等级
        List<ParamName> grade = this.gradeConfigService
                .getParamNameList("VIP_GRADE");
        modelAndView.addObject("grade", grade);
		
		LogUtil.endLog(GradeConfigController.class.toString(), GradeConfigDefine.INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 添加活动信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = GradeConfigDefine.INSERT_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(GradeConfigDefine.PERMISSIONS_ADD)
	public ModelAndView insertAction(HttpServletRequest request, GradeConfigBean form) {
		LogUtil.startLog(GradeConfigDefine.class.toString(), GradeConfigDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(GradeConfigDefine.INFO_PATH);

		// 调用校验
		if (validatorFieldCheck(modelAndView, form) != null) {
			// 失败返回
			return validatorFieldCheck(modelAndView, form);
		}
		
		// 数据插入
		this.gradeConfigService.insertRecord(form);
		// 跳转页面用（info里面有）
		modelAndView.addObject(GradeConfigDefine.SUCCESS, GradeConfigDefine.SUCCESS);
		LogUtil.endLog(GradeConfigController.class.toString(), GradeConfigDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 修改活动维护信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	 
	@RequestMapping(value = GradeConfigDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(GradeConfigDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateAction(HttpServletRequest request, GradeConfigBean form) {
		LogUtil.startLog(GradeConfigDefine.class.toString(), GradeConfigDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(GradeConfigDefine.INFO_PATH);

		// 调用校验
		if (validatorFieldCheck(modelAndView, form) != null) {
			// 失败返回
			return validatorFieldCheck(modelAndView, form);
		}
		// // 根据id更新
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "id", form.getId().toString())) {
			return modelAndView;
		}

		// 更新
		this.gradeConfigService.updateRecord(form);
		// 跳转页面用（info里面有）
		modelAndView.addObject(GradeConfigDefine.SUCCESS, GradeConfigDefine.SUCCESS);
		LogUtil.endLog(GradeConfigController.class.toString(), GradeConfigDefine.UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 * 调用校验表单方法
	 * 
	 * @param modelAndView
	 * @param form
	 * @return
	 */
	private ModelAndView validatorFieldCheck(ModelAndView modelAndView, GradeConfigBean form) {
		// 字段校验(非空判断和长度判断)

	    if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "vipValue", form.getVipValue()+"")) {
	        return modelAndView;
        }
        if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "vipLevel", form.getVipLevel()+"")) {
            return modelAndView;
        }
		return null;
	}
	
	@ResponseBody
	@RequestMapping(value = GradeConfigDefine.VALIDATEBEFORE)
	@RequiresPermissions(GradeConfigDefine.PERMISSIONS_VIEW)
	public Map<String, Object> validateBeforeAction(HttpServletRequest request, GradeConfigBean form) {
		LogUtil.startLog(GradeConfigDefine.class.toString(), GradeConfigDefine.VALIDATEBEFORE);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ModelAndView modelAndView=new ModelAndView();
		// 字段校验(非空判断和长度判断)
        if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "vipValue", form.getVipValue()+"")) {
            resultMap.put("success", false);
            resultMap.put("msg", "请选择VIP等级");
            return resultMap;
        }
        if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "vipLevel", form.getVipLevel()+"")) {
            resultMap.put("success", false);
            resultMap.put("msg", "V值不能为空");
            return resultMap;
        }
        
        int count=gradeConfigService.getVipInfoByVipLevel(form);
        if(count>0){
            resultMap.put("success", false);
            resultMap.put("msg", "Vip等级已经创建不能重复创建");
            return resultMap;
        }
		resultMap.put("success", true);
		LogUtil.endLog(GradeConfigDefine.class.toString(), GradeConfigDefine.VALIDATEBEFORE);
		return resultMap;
	}
	/*
    private Date createTime(String dateString) {
        SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd");
        Date date=new Date();
        try {
            date = sim.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }*/
    
}
