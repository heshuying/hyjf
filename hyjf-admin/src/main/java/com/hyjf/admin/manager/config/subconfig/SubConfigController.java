package com.hyjf.admin.manager.config.subconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.coupon.config.CouponConfigDefine;
import com.hyjf.admin.manager.config.operationlog.OperationLogBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.SubCommissionListConfig;
import com.hyjf.mybatis.model.customize.SubCommissionListConfigCustomize;

/**
 * 
 * 分账名单配置
 * @author hyjf
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年11月28日
 * @see 10:36:14
 */
@Controller
@RequestMapping(SubConfigDefine.REQUEST_MAPPING)
public class SubConfigController extends BaseController {

    @Autowired
    private SubConfigService subConfigService;

    /**
     * init
     * @author koudeli
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(SubConfigDefine.INIT)
    @RequiresPermissions(SubConfigDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, SubConfigBean form) {
        LogUtil.startLog(SubConfigDefine.THIS_CLASS, SubConfigDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(SubConfigDefine.LIST_PATH);
        // 分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(SubConfigDefine.THIS_CLASS, SubConfigDefine.INIT);
        return modelAndView;
    }

    /**
     * 
     * 列表检索Action
     * @author koudeli
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(SubConfigDefine.SEARCH_ACTION)
    @RequiresPermissions(SubConfigDefine.PERMISSIONS_SEARCH)
    public ModelAndView search(HttpServletRequest request, SubConfigBean form) {
        LogUtil.startLog(SubConfigDefine.THIS_CLASS, SubConfigDefine.SEARCH_ACTION);
        ModelAndView modelAndView = new ModelAndView(SubConfigDefine.LIST_PATH);
        // 分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(SubConfigDefine.THIS_CLASS, SubConfigDefine.SEARCH_ACTION);
        return modelAndView;
    }

    /**
     * 分账配置分页技能
     * @author koudeli
     * @param request
     * @param modelAndView
     * @param form
     */
    public void createPage(HttpServletRequest request, ModelAndView modelAndView, SubConfigBean form) {
    	// 封装查询条件
		Map<String, Object> conditionMap = setCondition(form);
		
		Integer count = this.subConfigService.getRecordCount(conditionMap);
        if (count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            List<SubCommissionListConfigCustomize> recordList =
                    this.subConfigService.getRecordList(conditionMap, paginator.getOffset(), paginator.getLimit());
            form.setRecordList(recordList);
            form.setPaginator(paginator);
        }
        modelAndView.addObject(SubConfigDefine.FINMAN_CHARGE_FORM, form);
    }

    /**
     * 
     * 画面迁移(含有id更新，不含有id添加)
     * @author liuyang
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(SubConfigDefine.INFO_ACTION)
    @RequiresPermissions(value = { SubConfigDefine.PERMISSIONS_ADD,
    		SubConfigDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
    public ModelAndView info(HttpServletRequest request, @ModelAttribute(SubConfigDefine.FINMAN_CHARGE_FORM)SubConfigBean form) {
        LogUtil.startLog(SubConfigDefine.THIS_CLASS, SubConfigDefine.INFO_ACTION);
        ModelAndView modelAndView = new ModelAndView(SubConfigDefine.INFO_PATH);
        SubCommissionListConfigCustomize record = new SubCommissionListConfigCustomize();
        if (form.getId()!=null) {
            record = this.subConfigService.getRecordInfo(form.getId());
        }
        modelAndView.addObject(SubConfigDefine.FINMAN_CHARGE_FORM, record);
        
        LogUtil.endLog(SubConfigDefine.THIS_CLASS, SubConfigDefine.INFO_ACTION);
        return modelAndView;
    }

    /**
     * 
     * 插入操作
     * @author koudeli
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(SubConfigDefine.INSERT_ACTION)
    @RequiresPermissions(SubConfigDefine.PERMISSIONS_ADD)
    public ModelAndView insertAction(HttpServletRequest request, SubConfigBean form) {
        LogUtil.startLog(SubConfigDefine.THIS_CLASS, SubConfigDefine.INSERT_ACTION);
        ModelAndView modelAndView = new ModelAndView(SubConfigDefine.INFO_PATH);
        // 表单校验(双表校验)
        this.validatorFieldCheck(modelAndView, form);
        if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
            modelAndView.addObject(SubConfigDefine.FINMAN_CHARGE_FORM, form);
            LogUtil.errorLog(SubConfigDefine.THIS_CLASS, SubConfigDefine.INSERT_ACTION, "输入内容验证失败", null);
            return modelAndView;
        }
		Map<String, Object> userMap=subConfigService.userMap(form);
		if (userMap.get("user_id")!=null) {
			form.setUserId(Integer.parseInt(String.valueOf(userMap.get("user_id"))));
		}
        // 插入
        int cot = this.subConfigService.insertRecord(form);
        modelAndView.addObject(SubConfigDefine.SUCCESS, SubConfigDefine.SUCCESS);
        LogUtil.endLog(SubConfigDefine.THIS_CLASS, SubConfigDefine.INSERT_ACTION);
        return modelAndView;
    }

    /**
     * 
     * 数据更新
     * @author koudeli
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(SubConfigDefine.UPDATE_ACTION)
    @RequiresPermissions(SubConfigDefine.PERMISSIONS_MODIFY)
    public ModelAndView updateAction(HttpServletRequest request, SubConfigBean form) {
        LogUtil.startLog(SubConfigDefine.THIS_CLASS, SubConfigDefine.UPDATE_ACTION);
        ModelAndView modelAndView = new ModelAndView(SubConfigDefine.INFO_PATH);
        // 数据更新(双表)
        int cnt = this.subConfigService.updateRecord(form);
        modelAndView.addObject(SubConfigDefine.SUCCESS, SubConfigDefine.SUCCESS);
        LogUtil.endLog(SubConfigDefine.THIS_CLASS, SubConfigDefine.UPDATE_ACTION);
        return modelAndView;
    }

    /**
     * 
     * 数据删除
     * @author koudeli
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(SubConfigDefine.DELETE_ACTION)
    @RequiresPermissions(SubConfigDefine.PERMISSIONS_DELETE)
    public String deleteAction(HttpServletRequest request, RedirectAttributes attr, SubConfigBean form) {
        LogUtil.startLog(SubConfigDefine.THIS_CLASS, SubConfigDefine.DELETE_ACTION);
        this.subConfigService.deleteRecord(form);//
        LogUtil.endLog(SubConfigDefine.THIS_CLASS, SubConfigDefine.DELETE_ACTION);
        return "redirect:" + SubConfigDefine.REQUEST_MAPPING + "/" + SubConfigDefine.INIT;
    }
    /**
	 * 封装查询条件
	 * 
	 * @param form
	 * @return
	 */
	private Map<String, Object> setCondition(SubConfigBean form) {
		String userNameSrch = StringUtils.isNotEmpty(form.getUserNameSrch()) ? form.getUserNameSrch() : null;
		String trueNameSrch = StringUtils.isNotEmpty(form.getTrueNameSrch()) ? form.getTrueNameSrch() : null;
		String roleNameSrch = StringUtils.isNotEmpty(form.getRoleNameSrch()) ? form.getRoleNameSrch() : null;
		String userTypeSrch = StringUtils.isNotEmpty(form.getUserTypeSrch()) ? form.getUserTypeSrch() : null;
		String accountSrch = StringUtils.isNotEmpty(form.getAccountSrch()) ? form.getAccountSrch() : null;
		String statusSrch = StringUtils.isNotEmpty(form.getStatusSrch()) ? form.getStatusSrch() : null;
		String recieveTimeStartSrch = StringUtils.isNotEmpty(form.getRecieveTimeStartSrch()) ? form.getRecieveTimeStartSrch() : null;
		String recieveTimeEndSrch = StringUtils.isNotEmpty(form.getRecieveTimeEndSrch()) ? form.getRecieveTimeEndSrch() : null;

		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("userNameSrch", userNameSrch);
		conditionMap.put("trueNameSrch", trueNameSrch);
		conditionMap.put("roleNameSrch", roleNameSrch);
		conditionMap.put("userTypeSrch", userTypeSrch);
		conditionMap.put("accountSrch", accountSrch);
		conditionMap.put("statusSrch", statusSrch);
		conditionMap.put("recieveTimeStartSrch", recieveTimeStartSrch);
		conditionMap.put("recieveTimeEndSrch", recieveTimeEndSrch);
		return conditionMap;
	}
    /**
     * 画面校验
     * 
     * @param modelAndView
     * @param form
     */
    private void validatorFieldCheck(ModelAndView modelAndView, SubConfigBean form) {
        // 用户名
        ValidatorFieldCheckUtil.validateRequired(modelAndView, "username", form.getUsername());
        // 姓名
        ValidatorFieldCheckUtil
                .validateRequired(modelAndView, "truename", String.valueOf(form.getTruename()));
        //用户角色
        ValidatorFieldCheckUtil.validateRequired(modelAndView, "roleName", form.getRoleName());
        //用户类型
        ValidatorFieldCheckUtil.validateRequired(modelAndView, "userType", form.getUserType());
        //银行开户状态
        ValidatorFieldCheckUtil.validateRequired(modelAndView, "bankOpenAccount", form.getBankOpenAccount());
        //江西银行电子账号
        ValidatorFieldCheckUtil.validateRequired(modelAndView, "account", form.getAccount());
        // 用户状态
        ValidatorFieldCheckUtil.validateRequired(modelAndView, "status", String.valueOf(form.getStatus()));
        
    }
    /**
	 * 用户名信息
	 *
	 * @param request
	 * @return 
	 */
	@RequestMapping(SubConfigDefine.SEARCHUPDATE_ACTION)
	@RequiresPermissions(SubConfigDefine.PERMISSIONS_VIEW)
	@ResponseBody
	public Map<String, Object> searchUpdateAction(HttpServletRequest request, RedirectAttributes attr,SubConfigBean form
			) {
		LogUtil.startLog(SubConfigController.class.toString(), SubConfigDefine.ASSETTYPE_ACTION);
		Map<String, Object> userMapNullMap=new HashMap();
		Map<String, Object> userMap=subConfigService.userMap(form);
//		List<SubCommissionListConfigCustomize> list=subConfigService.subconfig(form.getUsername());
		LogUtil.endLog(SubConfigController.class.toString(), SubConfigDefine.ASSETTYPE_ACTION);
		if (userMap!=null) {
				if (userMap.get("OPEN")!=null) {
					if ("未开户".equals(userMap.get("OPEN").toString().trim())) {
						userMapNullMap.put("info", "无法获取用户信息");
						userMapNullMap.put("status", "n");
						return userMapNullMap;
					}
				}
				if (userMap.get("status")!=null) {
					if (userMap.get("status").equals("1")) {
						userMapNullMap.put("info", "该用户已被禁用");
						userMapNullMap.put("status", "n");
						return userMapNullMap;
					}
				}
				userMap.put("status", "y");
				return userMap;
			
			
		}else {
			userMapNullMap.put("info", "您输入的用户名无对应信息，请重新输入");
			userMapNullMap.put("status", "n");
			return userMapNullMap;
		}
		
		
	}
	/**
	 * 用户名信息
	 *
	 * @param request
	 * @return 
	 */
	@RequestMapping(SubConfigDefine.ASSETTYPE_ACTION)
	@RequiresPermissions(SubConfigDefine.PERMISSIONS_VIEW)
	@ResponseBody
	public Map<String, Object> assetTypeAction(HttpServletRequest request, RedirectAttributes attr,SubConfigBean form
			) {
		LogUtil.startLog(SubConfigController.class.toString(), SubConfigDefine.ASSETTYPE_ACTION);
		Map<String, Object> userMapNullMap=new HashMap();
		Map<String, Object> userMap=subConfigService.userMap(form);
		List<SubCommissionListConfigCustomize> list=subConfigService.subconfig(form.getUsername());
		LogUtil.endLog(SubConfigController.class.toString(), SubConfigDefine.ASSETTYPE_ACTION);
		if (userMap!=null) {
			if (list.size()>0) {
				userMapNullMap.put("info", "该用户白名单信息已经存在");
				userMapNullMap.put("status", "n");
				return userMapNullMap;
			}else {
				if (userMap.get("OPEN")!=null) {
					if ("未开户".equals((String)userMap.get("OPEN").toString().trim())) {
						userMapNullMap.put("info", "无法获取用户信息");
						userMapNullMap.put("status", "n");
						return userMapNullMap;
					}
				}
				if (userMap.get("status")!=null) {
					if ((Boolean)userMap.get("status")) {
						userMapNullMap.put("info", "该用户已被禁用");
						userMapNullMap.put("status", "n");
						return userMapNullMap;
					}
				}
				userMap.put("status", "y");
				return userMap;
			}
			
		}else {
			userMapNullMap.put("info", "您输入的用户名无对应信息，请重新输入");
			userMapNullMap.put("status", "n");
			return userMapNullMap;
		}
		
		
	}
}
