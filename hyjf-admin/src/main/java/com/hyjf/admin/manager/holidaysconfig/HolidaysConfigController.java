package com.hyjf.admin.manager.holidaysconfig;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
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
import com.hyjf.mybatis.model.auto.HolidaysConfig;

/**
 * 手续费配置
 * 
 * @author
 *
 */
@Controller
@RequestMapping(value = HolidaysConfigDefine.REQUEST_MAPPING)
public class HolidaysConfigController extends BaseController {

    @Autowired
    private HolidaysConfigService holidaysConfigService;

	/**
	 * 列表维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(HolidaysConfigDefine.INIT)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(HolidaysConfigDefine.CONFIGFEE_FORM) HolidaysConfigBean form) {
		LogUtil.startLog(HolidaysConfigController.class.toString(), HolidaysConfigDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(HolidaysConfigDefine.LIST_PATH);
		
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(HolidaysConfigController.class.toString(), HolidaysConfigDefine.INIT);
		return modelAndView;
	}

	/**
	 * 文章列表维护分页机能 页面初始化
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, HolidaysConfigBean form) {
		List<HolidaysConfig> recordList = this.holidaysConfigService.getRecordList(new HolidaysConfig(), -1, -1);
		if (recordList != null) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			recordList = this.holidaysConfigService.getRecordList(new HolidaysConfig(), paginator.getOffset(),
					paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(HolidaysConfigDefine.CONFIGFEE_FORM, form);
		}
	}

	/**
	 * 画面迁移(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(HolidaysConfigDefine.INFO_ACTION)
/*	@RequiresPermissions(value = { WithdrawalsTimeConfigDefine.PERMISSIONS_INFO, WithdrawalsTimeConfigDefine.PERMISSIONS_ADD,
			WithdrawalsTimeConfigDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)*/
	public ModelAndView info(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(HolidaysConfigDefine.CONFIGFEE_FORM) HolidaysConfigBean form) {
		LogUtil.startLog(HolidaysConfigController.class.toString(), HolidaysConfigDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(HolidaysConfigDefine.INFO_PATH);

		if (StringUtils.isNotEmpty(form.getIds())) {
			Integer id = Integer.valueOf(form.getIds());
			HolidaysConfig record = this.holidaysConfigService.getRecord(id);
			modelAndView.addObject(HolidaysConfigDefine.CONFIGFEE_FORM, record);
		}else {
		    HolidaysConfig record = new HolidaysConfig();
		    String data=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		    record.setStatrTime(data);
		    record.setEndTime(data);
            modelAndView.addObject(HolidaysConfigDefine.CONFIGFEE_FORM, record);
		}
		LogUtil.endLog(HolidaysConfigController.class.toString(), HolidaysConfigDefine.INFO_ACTION);
		return modelAndView;
	}

    /**
	 * 添加活动信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = HolidaysConfigDefine.INSERT_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(HolidaysConfigDefine.PERMISSIONS_ADD)
	public ModelAndView insertAction(HttpServletRequest request, HolidaysConfigBean form) {
		LogUtil.startLog(HolidaysConfigController.class.toString(), HolidaysConfigDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(HolidaysConfigDefine.INFO_PATH);

		// 调用校验
		if (validatorFieldCheck(modelAndView, form) != null) {
			// 失败返回
			return validatorFieldCheck(modelAndView, form);
		}
		
		// 数据插入
		this.holidaysConfigService.insertRecord(form);
		// 跳转页面用（info里面有）
		modelAndView.addObject(HolidaysConfigDefine.SUCCESS, HolidaysConfigDefine.SUCCESS);
		LogUtil.endLog(HolidaysConfigController.class.toString(), HolidaysConfigDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 修改活动维护信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	 
	@RequestMapping(value = HolidaysConfigDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(HolidaysConfigDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateAction(HttpServletRequest request, HolidaysConfigBean form) {
		LogUtil.startLog(HolidaysConfigController.class.toString(), HolidaysConfigDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(HolidaysConfigDefine.INFO_PATH);

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
		this.holidaysConfigService.updateRecord(form);
		// 跳转页面用（info里面有）
		modelAndView.addObject(HolidaysConfigDefine.SUCCESS, HolidaysConfigDefine.SUCCESS);
		LogUtil.endLog(HolidaysConfigController.class.toString(), HolidaysConfigDefine.UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 * 调用校验表单方法
	 * 
	 * @param modelAndView
	 * @param form
	 * @return
	 */
	private ModelAndView validatorFieldCheck(ModelAndView modelAndView, HolidaysConfigBean form) {
		// 字段校验(非空判断和长度判断)

	    if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "eventsName", form.getEventsName())) {
	        return modelAndView;
        }
/*        if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "year", form.getYear())) {
            return modelAndView;
        }*/
        if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "statrTime", form.getStatrTime())) {
            return modelAndView;
        }
        if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "endTime", form.getEndTime())) {
            return modelAndView;
        }
		return null;
	}

	@ResponseBody
	@RequestMapping(value = HolidaysConfigDefine.VALIDATEBEFORE)
	@RequiresPermissions(HolidaysConfigDefine.PERMISSIONS_VIEW)
	public Map<String, Object> validateBeforeAction(HttpServletRequest request, HolidaysConfigBean form) {
		LogUtil.startLog(HolidaysConfigController.class.toString(), HolidaysConfigDefine.VALIDATEBEFORE);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ModelAndView modelAndView=new ModelAndView();
		// 字段校验(非空判断和长度判断)
        if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "eventsName", form.getEventsName())) {
            resultMap.put("success", false);
            resultMap.put("msg", "节日名称不能为空");
            return resultMap;
        }
/*        if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "year", form.getYear())) {
            resultMap.put("success", false);
            resultMap.put("msg", "年份不能为空");
            return resultMap;
        }*/
        if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "statrTime", form.getStatrTime())) {
            resultMap.put("success", false);
            resultMap.put("msg", "开始时间不能为空");
            return resultMap;
        }
        if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "endTime", form.getEndTime())) {
            resultMap.put("success", false);
            resultMap.put("msg", "结束时间不能为空");
            return resultMap;
        }
        
/*        if (form.getEndTime().indexOf(form.getYear())<0||form.getStatrTime().indexOf(form.getYear())<0) {
            resultMap.put("success", false);
            resultMap.put("msg", "请选择当前年份的日期");
            return resultMap;
        }*/
		Date endTime=createTime(form.getEndTime());
		Date statrTime=createTime(form.getStatrTime());
		if(endTime.getTime()<statrTime.getTime()){
		    resultMap.put("success", false);
            resultMap.put("msg", "开始时间不能小于结束时间");
            return resultMap;
		}
		resultMap.put("success", true);
		LogUtil.endLog(HolidaysConfigController.class.toString(), HolidaysConfigDefine.VALIDATEBEFORE);
		return resultMap;
	}

    private Date createTime(String dateString) {
        SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd");
        Date date=new Date();
        try {
            date = sim.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    
}
