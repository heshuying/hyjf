package com.hyjf.admin.manager.activity.billion.config;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
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

import com.hyjf.admin.BaseController;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.CustomErrors;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.ActivityBillionThirdConfig;
import com.hyjf.mybatis.model.auto.CouponConfig;

/**
 * 秒杀配置
 * @author Michael
 */
@Controller
@RequestMapping(value = BillionThirdConfigDefine.REQUEST_MAPPING)
public class BillionThirdConfigController extends BaseController {

	@Autowired
	private BillionThirdConfigService billionThirdConfigService;

	 /**
     * 画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(BillionThirdConfigDefine.INIT)
    @RequiresPermissions(BillionThirdConfigDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, BillionThirdConfigBean form) {
        LogUtil.startLog(BillionThirdConfigController.class.toString(), BillionThirdConfigDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(BillionThirdConfigDefine.LIST_PATH);

        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(BillionThirdConfigController.class.toString(), BillionThirdConfigDefine.INIT);
        return modelAndView;
    }

    /**
     * 查询画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(BillionThirdConfigDefine.SEARCH_ACTION)
    @RequiresPermissions(BillionThirdConfigDefine.PERMISSIONS_SEARCH)
    public ModelAndView searchAction(HttpServletRequest request, BillionThirdConfigBean form) {
        LogUtil.startLog(BillionThirdConfigController.class.toString(), BillionThirdConfigDefine.SEARCH_ACTION);
        ModelAndView modelAndView = new ModelAndView(BillionThirdConfigDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(BillionThirdConfigController.class.toString(), BillionThirdConfigDefine.SEARCH_ACTION);
        return modelAndView;
    }

    /**
     * 创建分页机能
     * 
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, BillionThirdConfigBean form) {
        Integer count = this.billionThirdConfigService.selectRecordCount(form);
        if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
            List<ActivityBillionThirdConfig> recordList = this.billionThirdConfigService.getRecordList(form,paginator.getOffset(),paginator.getLimit());
            form.setPaginator(paginator);
            form.setRecordList(recordList);
        }
        modelAndView.addObject(BillionThirdConfigDefine.FORM, form);
    }

	/**
	 * 画面迁移(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BillionThirdConfigDefine.INFO_ACTION)
	@RequiresPermissions(value = { BillionThirdConfigDefine.PERMISSIONS_INFO, BillionThirdConfigDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView infoAction(HttpServletRequest request, RedirectAttributes attr,@ModelAttribute(BillionThirdConfigDefine.FORM) BillionThirdConfigBean form) {
		LogUtil.startLog(BillionThirdConfigController.class.toString(), BillionThirdConfigDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(BillionThirdConfigDefine.INFO_PATH);
		try {
			if (StringUtils.isNotEmpty(form.getIds())) {
				Integer id = Integer.valueOf(form.getIds());
				ActivityBillionThirdConfig record = this.billionThirdConfigService.getRecord(id);
				BeanUtils.copyProperties(record, form);
				String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
				modelAndView.addObject("fileDomainUrl", fileDomainUrl);
			} 
			modelAndView.addObject(BillionThirdConfigDefine.FORM, form);
		} catch (Exception e) {
			e.printStackTrace();
		}
		LogUtil.endLog(BillionThirdConfigController.class.toString(), BillionThirdConfigDefine.INIT);
		return modelAndView;
	}


	/**
	 * 修改
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = BillionThirdConfigDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(BillionThirdConfigDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateAction(HttpServletRequest request, ActivityBillionThirdConfig form) {
		LogUtil.startLog(BillionThirdConfigController.class.toString(), BillionThirdConfigDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(BillionThirdConfigDefine.INFO_PATH);
		// 调用校验
		if (validatorFieldCheck(modelAndView, form) != null) {
		    modelAndView.addObject(BillionThirdConfigDefine.FORM, form);
			// 失败返回
			return modelAndView;
		}
		// 根据id更新
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "id", form.getId().toString())) {
		    modelAndView.addObject(BillionThirdConfigDefine.FORM, form);
			return modelAndView;
		}
		// 更新
		this.billionThirdConfigService.updateRecord(form);
		modelAndView.addObject(BillionThirdConfigDefine.SUCCESS, BillionThirdConfigDefine.SUCCESS);
		LogUtil.endLog(BillionThirdConfigController.class.toString(), BillionThirdConfigDefine.UPDATE_ACTION);
		return modelAndView;
	}


	/**
	 * 调用校验表单方法
	 * 
	 * @param modelAndView
	 * @param form
	 * @return
	 */
	private ModelAndView validatorFieldCheck(ModelAndView modelAndView, ActivityBillionThirdConfig form) {
		// 字段校验(非空判断和长度判断)
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "prizeName", form.getPrizeName())) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "couponCode", form.getCouponCode())) {
			return modelAndView;
		}
		
		
		CouponConfig couponConfig =billionThirdConfigService.selectConfigByCode(form.getCouponCode());
		if(couponConfig==null||!form.getCouponCode().equals(couponConfig.getCouponCode())){
            CustomErrors.add(modelAndView, "couponCode", "same", "优惠券不存在或非可用状态");
            return modelAndView;
        }
		
		ActivityBillionThirdConfig record = this.billionThirdConfigService.getRecord(form.getId());
		if(record.getKillStatus()==1){
		    CustomErrors.add(modelAndView, "couponCode", "same", "秒杀已开始不能修改");
		    CustomErrors.add(modelAndView, "couponNum", "same", "秒杀已开始不能修改");
            return modelAndView;
		}
/*		if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "picUrl", form.getPicUrl(), 200, true)) {
			return modelAndView;
		}*/
		return null;
	}

	/**
	 * 资料上传
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping(value = BillionThirdConfigDefine.UPLOAD_FILE, method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(BillionThirdConfigDefine.PERMISSIONS_MODIFY)
	public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogUtil.startLog(BillionThirdConfigController.class.toString(), BillionThirdConfigDefine.UPLOAD_FILE);
		String files = borrowCommonService.uploadFile(request, response);
		LogUtil.endLog(BillionThirdConfigController.class.toString(), BillionThirdConfigDefine.UPLOAD_FILE);
		return files;
	}*/
    
}
