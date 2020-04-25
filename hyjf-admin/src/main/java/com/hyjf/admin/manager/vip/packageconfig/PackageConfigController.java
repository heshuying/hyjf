package com.hyjf.admin.manager.vip.packageconfig;

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

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.coupon.config.CouponConfigService;
import com.hyjf.admin.coupon.user.CouponUserBean;
import com.hyjf.admin.coupon.user.CouponUserService;
import com.hyjf.admin.manager.vip.gradeconfig.GradeConfigService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.CouponConfig;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.auto.VipInfo;
import com.hyjf.mybatis.model.customize.admin.VipAuthCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigCustomize;

/**
 * 手续费配置
 * 
 * @author
 *
 */
@Controller
@RequestMapping(value = PackageConfigDefine.REQUEST_MAPPING)
public class PackageConfigController extends BaseController {

    @Autowired
    private PackageConfigService packageConfigService;
    
    @Autowired
    private CouponConfigService couponConfigService;

    @Autowired
    private CouponUserService couponUserService;
    @Autowired
    private GradeConfigService gradeConfigService;
	/**
	 * 列表维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PackageConfigDefine.INIT)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(PackageConfigDefine.CONFIGFEE_FORM) PackageConfigBean form) {
		LogUtil.startLog(PackageConfigController.class.toString(), PackageConfigDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(PackageConfigDefine.LIST_PATH);
		
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(PackageConfigController.class.toString(), PackageConfigDefine.INIT);
		return modelAndView;
	}

	/**
	 * 文章列表维护分页机能 页面初始化
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, PackageConfigBean form) {
	    Integer count  = this.packageConfigService.countRecord(form);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			VipAuthCustomize vipAuthCustomize=new VipAuthCustomize();
	        vipAuthCustomize.setLimitStart(paginator.getOffset());
	        vipAuthCustomize.setLimitEnd(paginator.getLimit());
			List<VipAuthCustomize> recordList = this.packageConfigService.getRecordList(vipAuthCustomize);
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(PackageConfigDefine.CONFIGFEE_FORM, form);
		}
	}

	/**
	 * 画面迁移(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PackageConfigDefine.INFO_ACTION)
	@RequiresPermissions(value = { PackageConfigDefine.PERMISSIONS_INFO, PackageConfigDefine.PERMISSIONS_ADD,
	        PackageConfigDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView info(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(PackageConfigDefine.CONFIGFEE_FORM) PackageConfigBean form) {
		LogUtil.startLog(PackageConfigDefine.class.toString(), PackageConfigDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(PackageConfigDefine.INFO_PATH);

		if (StringUtils.isNotEmpty(form.getIds())) {
			Integer id = Integer.valueOf(form.getIds());
			VipAuthCustomize record = this.packageConfigService.getRecord(id);
			modelAndView.addObject(PackageConfigDefine.CONFIGFEE_FORM, record);
			
			CouponConfigCustomize couponConfigCustomize = new CouponConfigCustomize();
	        couponConfigCustomize.setLimitStart(-1);
	        couponConfigCustomize.setLimitEnd(-1);
	        couponConfigCustomize.setStatus(CustomConstants.COUPON_STATUS_PUBLISHED);
	        couponConfigCustomize.setCouponType(record.getCouponType()+"");
	        List<CouponConfigCustomize> recordConfigList  = couponConfigService.getRecordList(couponConfigCustomize);
			modelAndView.addObject("configList", recordConfigList);
		}else{
		    modelAndView.addObject(PackageConfigDefine.CONFIGFEE_FORM, new VipAuthCustomize());
		}
		// 优惠券类别
        List<ParamName> couponType = this.packageConfigService
                .getParamNameList("COUPON_TYPE");
        modelAndView.addObject("couponType", couponType);
		
        // vip等级
        List<VipInfo> vipInfos = this.gradeConfigService.getRecordList(null, -1, -1);
        modelAndView.addObject("vipInfos", vipInfos);
		LogUtil.endLog(PackageConfigController.class.toString(), PackageConfigDefine.INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 添加活动信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = PackageConfigDefine.INSERT_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(PackageConfigDefine.PERMISSIONS_ADD)
	public ModelAndView insertAction(HttpServletRequest request, PackageConfigBean form) {
		LogUtil.startLog(PackageConfigDefine.class.toString(), PackageConfigDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(PackageConfigDefine.INFO_PATH);

		// 调用校验
		if (validatorFieldCheck(modelAndView, form) != null) {
			// 失败返回
			return validatorFieldCheck(modelAndView, form);
		}
		
		// 数据插入
		this.packageConfigService.insertRecord(form);
		// 跳转页面用（info里面有）
		modelAndView.addObject(PackageConfigDefine.SUCCESS, PackageConfigDefine.SUCCESS);
		LogUtil.endLog(PackageConfigController.class.toString(), PackageConfigDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 修改活动维护信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	 
	@RequestMapping(value = PackageConfigDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(PackageConfigDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateAction(HttpServletRequest request, PackageConfigBean form) {
		LogUtil.startLog(PackageConfigDefine.class.toString(), PackageConfigDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(PackageConfigDefine.INFO_PATH);

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
		this.packageConfigService.updateRecord(form);
		// 跳转页面用（info里面有）
		modelAndView.addObject(PackageConfigDefine.SUCCESS, PackageConfigDefine.SUCCESS);
		LogUtil.endLog(PackageConfigController.class.toString(), PackageConfigDefine.UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 * 调用校验表单方法
	 * 
	 * @param modelAndView
	 * @param form
	 * @return
	 */
	private ModelAndView validatorFieldCheck(ModelAndView modelAndView, PackageConfigBean form) {
		// 字段校验(非空判断和长度判断)

	    if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "vipId", form.getVipId()+"")) {
	        return modelAndView;
        }
        if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "couponCode", form.getCouponCode()+"")) {
            return modelAndView;
        }
		return null;
	}
	
	@ResponseBody
	@RequestMapping(value = PackageConfigDefine.VALIDATEBEFORE)
	@RequiresPermissions(PackageConfigDefine.PERMISSIONS_VIEW)
	public Map<String, Object> validateBeforeAction(HttpServletRequest request, PackageConfigBean form) {
		LogUtil.startLog(PackageConfigDefine.class.toString(), PackageConfigDefine.VALIDATEBEFORE);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ModelAndView modelAndView=new ModelAndView();
		// 字段校验(非空判断和长度判断)
        if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "vipId", form.getVipId()+"")) {
            resultMap.put("success", false);
            resultMap.put("msg", "请选择VIP等级");
            return resultMap;
        }
        if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "couponCode", form.getCouponCode()+"")) {
            resultMap.put("success", false);
            resultMap.put("msg", "请选择发放的优惠券");
            return resultMap;
        }
        
        if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "couponQuantity", form.getCouponQuantity()+"")) {
            resultMap.put("success", false);
            resultMap.put("msg", "请输入发放张数");
            return resultMap;
        }
        
        
        
        int count=packageConfigService.getVipAuthByIdAndCode(form.getId(),form.getVipId(),form.getCouponCode());
        if(count>0){
            resultMap.put("success", false);
            resultMap.put("msg", "该优惠券已创建请进行修改操作");
            return resultMap;
        }
		resultMap.put("success", true);
		LogUtil.endLog(PackageConfigDefine.class.toString(), PackageConfigDefine.VALIDATEBEFORE);
		return resultMap;
	}
	
	
	/**
     * 
     * 返回测评问卷
     * @author pcc
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value=PackageConfigDefine.SELECT_COUPON_LIST_ACTION, produces = "text/html;charset=UTF-8",method = RequestMethod.POST)
    // @RequiresRoles(ShiroConstants.ROLE_NORMAL_USER)
    public String selectCouponListAction(HttpServletRequest request, RedirectAttributes attr,
        @ModelAttribute(PackageConfigDefine.CONFIGFEE_FORM) PackageConfigBean form) {
        LogUtil.startLog(PackageConfigController.class.toString(), PackageConfigDefine.REQUEST_MAPPING);
//      ModelAndView modelAndView = new ModelAndView(FinancialAdvisorDefine.QUESTIONNAIRE_PATH);
        JSONObject ret=new JSONObject();

      //加载优惠券配置列表
        CouponConfigCustomize couponConfigCustomize = new CouponConfigCustomize();
        couponConfigCustomize.setLimitStart(-1);
        couponConfigCustomize.setLimitEnd(-1);
        couponConfigCustomize.setStatus(CustomConstants.COUPON_STATUS_PUBLISHED);
        couponConfigCustomize.setCouponType(form.getCouponType());
        List<CouponConfigCustomize> recordConfigList  = couponConfigService.getRecordList(couponConfigCustomize);
        //List<String> typeList=financialAdvisorService.getTypeList();
        ret.put("list",recordConfigList);
        
        LogUtil.endLog(PackageConfigDefine.class.toString(), PackageConfigDefine.SELECT_COUPON_LIST_ACTION);
        return JSONObject.toJSONString(ret);
    }

    
    /**
     * 加载优惠券配置信息
     */
    @ResponseBody
    @RequestMapping(value = PackageConfigDefine.LOAD_COUPONCONFIG_ACTION,produces="text/html;charset=UTF-8")
    public String loadCouponConfig(HttpServletRequest request, RedirectAttributes attr, CouponUserBean form) {
        LogUtil.startLog(PackageConfigController.class.toString(), PackageConfigDefine.LOAD_COUPONCONFIG_ACTION);
        if(StringUtils.isEmpty(form.getCouponCode())){
            return StringUtils.EMPTY;
        }
        CouponConfig config = couponUserService.selectConfigByCode(form.getCouponCode());
        
        String configString = JSONObject.toJSONString(config);
        LogUtil.endLog(PackageConfigController.class.toString(), PackageConfigDefine.LOAD_COUPONCONFIG_ACTION);
        return configString;
    }
    
}
