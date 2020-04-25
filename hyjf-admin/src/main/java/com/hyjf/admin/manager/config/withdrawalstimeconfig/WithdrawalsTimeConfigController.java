package com.hyjf.admin.manager.config.withdrawalstimeconfig;

import java.util.ArrayList;
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
import com.hyjf.mybatis.model.auto.WithdrawalsTimeConfig;

/**
 * 手续费配置
 * 
 * @author
 *
 */
@Controller
@RequestMapping(value = WithdrawalsTimeConfigDefine.REQUEST_MAPPING)
public class WithdrawalsTimeConfigController extends BaseController {

	@Autowired
	private WithdrawalsTimeConfigService withdrawalsTimeConfigService;

	/**
	 * 列表维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(WithdrawalsTimeConfigDefine.INIT)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(WithdrawalsTimeConfigDefine.CONFIGFEE_FORM) WithdrawalsTimeConfigBean form) {
		LogUtil.startLog(WithdrawalsTimeConfigController.class.toString(), WithdrawalsTimeConfigDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(WithdrawalsTimeConfigDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(WithdrawalsTimeConfigController.class.toString(), WithdrawalsTimeConfigDefine.INIT);
		return modelAndView;
	}

	/**
	 * 文章列表维护分页机能 页面初始化
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, WithdrawalsTimeConfigBean form) {
		List<WithdrawalsTimeConfig> recordList = this.withdrawalsTimeConfigService.getRecordList(new WithdrawalsTimeConfig(), -1, -1);
		if (recordList != null) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			recordList = this.withdrawalsTimeConfigService.getRecordList(new WithdrawalsTimeConfig(), paginator.getOffset(),
					paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(WithdrawalsTimeConfigDefine.CONFIGFEE_FORM, form);
		}
	}

	/**
	 * 画面迁移(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(WithdrawalsTimeConfigDefine.INFO_ACTION)
/*	@RequiresPermissions(value = { WithdrawalsTimeConfigDefine.PERMISSIONS_INFO, WithdrawalsTimeConfigDefine.PERMISSIONS_ADD,
			WithdrawalsTimeConfigDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)*/
	public ModelAndView info(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(WithdrawalsTimeConfigDefine.CONFIGFEE_FORM) WithdrawalsTimeConfigBean form) {
		LogUtil.startLog(WithdrawalsTimeConfigController.class.toString(), WithdrawalsTimeConfigDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(WithdrawalsTimeConfigDefine.INFO_PATH);

		if (StringUtils.isNotEmpty(form.getIds())) {
			Integer id = Integer.valueOf(form.getIds());
			WithdrawalsTimeConfig record = this.withdrawalsTimeConfigService.getRecord(id);
			modelAndView.addObject(WithdrawalsTimeConfigDefine.CONFIGFEE_FORM, record);
		}
		// 设置银行列表
		modelAndView.addObject("times", createTimes());
		LogUtil.endLog(WithdrawalsTimeConfigController.class.toString(), WithdrawalsTimeConfigDefine.INFO_ACTION);
		return modelAndView;
	}

	private List<String> createTimes() {
        List<String> times=new ArrayList<String>();
        for (int i = 0; i < 25; i++) {
            for (int j = 0; j <2; j++) {
                String timeStr="";
                if(j==0){
                    timeStr=i+":"+"00";
                }else{
                    timeStr=i+":"+"30"; 
                }
                times.add(timeStr);
            }
            
        }
        return times;
    }

    /**
	 * 添加活动信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = WithdrawalsTimeConfigDefine.INSERT_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(WithdrawalsTimeConfigDefine.PERMISSIONS_ADD)
	public ModelAndView insertAction(HttpServletRequest request, WithdrawalsTimeConfigBean form) {
		LogUtil.startLog(WithdrawalsTimeConfigController.class.toString(), WithdrawalsTimeConfigDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(WithdrawalsTimeConfigDefine.INFO_PATH);

		// 调用校验
		if (validatorFieldCheck(modelAndView, form) != null) {
			// 失败返回
			return validatorFieldCheck(modelAndView, form);
		}
		// 数据插入
		this.withdrawalsTimeConfigService.insertRecord(form);
		// 跳转页面用（info里面有）
		modelAndView.addObject(WithdrawalsTimeConfigDefine.SUCCESS, WithdrawalsTimeConfigDefine.SUCCESS);
		LogUtil.endLog(WithdrawalsTimeConfigController.class.toString(), WithdrawalsTimeConfigDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 修改活动维护信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	 
	@RequestMapping(value = WithdrawalsTimeConfigDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(WithdrawalsTimeConfigDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateAction(HttpServletRequest request, WithdrawalsTimeConfigBean form) {
		LogUtil.startLog(WithdrawalsTimeConfigController.class.toString(), WithdrawalsTimeConfigDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(WithdrawalsTimeConfigDefine.INFO_PATH);

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
		this.withdrawalsTimeConfigService.updateRecord(form);
		// 跳转页面用（info里面有）
		modelAndView.addObject(WithdrawalsTimeConfigDefine.SUCCESS, WithdrawalsTimeConfigDefine.SUCCESS);
		LogUtil.endLog(WithdrawalsTimeConfigController.class.toString(), WithdrawalsTimeConfigDefine.UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 * 调用校验表单方法
	 * 
	 * @param modelAndView
	 * @param form
	 * @return
	 */
	private ModelAndView validatorFieldCheck(ModelAndView modelAndView, WithdrawalsTimeConfigBean form) {
		// 字段校验(非空判断和长度判断)
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "ifWorkingday", form.getIfWorkingday())) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "withdrawalsStart", form.getWithdrawalsStart())) {
            return modelAndView;
        }
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "withdrawalsEnd", form.getWithdrawalsEnd())) {
            return modelAndView;
        }
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "immediatelyWithdraw", form.getImmediatelyWithdraw())) {
            return modelAndView;
        }
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "quickWithdraw", form.getQuickWithdraw())) {
            return modelAndView;
        }
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "normalWithdraw", form.getNormalWithdraw())) {
            return modelAndView;
        }
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "status", form.getStatus()+"")) {
            return modelAndView;
        }
		return null;
	}

	@ResponseBody
	@RequestMapping(value = WithdrawalsTimeConfigDefine.VALIDATEBEFORE)
	@RequiresPermissions(WithdrawalsTimeConfigDefine.PERMISSIONS_VIEW)
	public Map<String, Object> validateBeforeAction(HttpServletRequest request, WithdrawalsTimeConfigBean form) {
		LogUtil.startLog(WithdrawalsTimeConfigController.class.toString(), WithdrawalsTimeConfigDefine.VALIDATEBEFORE);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ModelAndView modelAndView=new ModelAndView();
		// 字段校验(非空判断和长度判断)
        if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "ifWorkingday", form.getIfWorkingday())) {
            resultMap.put("success", false);
            resultMap.put("msg", "工作日不能为空");
            return resultMap;
        }
        if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "withdrawalsStart", form.getWithdrawalsStart())) {
            resultMap.put("success", false);
            resultMap.put("msg", "开始时间不能为空");
            return resultMap;
        }
        if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "withdrawalsEnd", form.getWithdrawalsEnd())) {
            resultMap.put("success", false);
            resultMap.put("msg", "结束时间不能为空");
            return resultMap;
        }
        String start=form.getWithdrawalsStart();
        String end=form.getWithdrawalsEnd();
        if (new Integer(start.replace(":", ""))>=new Integer(end.replace(":", ""))) {
            resultMap.put("success", false);
            resultMap.put("msg", "开始时间不能小于等于结束时间");
            return resultMap;
        }
        
        if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "immediatelyWithdraw", form.getImmediatelyWithdraw())) {
            resultMap.put("success", false);
            resultMap.put("msg", "即时提现不能为空");
            return resultMap;
        }
        if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "quickWithdraw", form.getQuickWithdraw())) {
            resultMap.put("success", false);
            resultMap.put("msg", "快速提现不能为空");
            return resultMap;
        }
        if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "normalWithdraw", form.getNormalWithdraw())) {
            resultMap.put("success", false);
            resultMap.put("msg", "一般提现不能为空");
            return resultMap;
        }
        if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "status", form.getStatus()+"")) {
            resultMap.put("success", false);
            resultMap.put("msg", "状态不能为空");
            return resultMap;
        }
		

		resultMap.put("success", true);
		LogUtil.endLog(WithdrawalsTimeConfigController.class.toString(), WithdrawalsTimeConfigDefine.VALIDATEBEFORE);
		return resultMap;
	}
}
