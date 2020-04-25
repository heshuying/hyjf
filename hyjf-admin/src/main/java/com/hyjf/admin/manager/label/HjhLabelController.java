package com.hyjf.admin.manager.label;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.allocationengine.AllocationEngineBean;
import com.hyjf.admin.manager.allocationengine.AllocationEngineService;
import com.hyjf.admin.manager.asset.assetlist.AssetListController;
import com.hyjf.admin.manager.asset.assetlist.AssetListDefine;
import com.hyjf.admin.manager.config.operationlog.OperationLogService;
import com.hyjf.admin.manager.config.repaystyle.RepayStyleController;
import com.hyjf.admin.manager.config.repaystyle.RepayStyleDefine;
import com.hyjf.common.calculate.DateUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.HjhAllocationEngine;
import com.hyjf.mybatis.model.auto.HjhAssetType;
import com.hyjf.mybatis.model.auto.HjhInstConfig;
import com.hyjf.mybatis.model.auto.HjhLabel;
import com.hyjf.mybatis.model.customize.HjhLabelCustomize;

/**
 * 标签页面
 * 
 * @author zhadaojian
 *
 */
@Controller
@RequestMapping(value = HjhLabelDefine.REQUEST_MAPPING)
public class HjhLabelController extends BaseController {

	@Autowired
	private HjhLabelService labelService;
	@Autowired
    private OperationLogService operationlogService;


	/**
	 * 标签配置画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(HjhLabelDefine.INIT)
	@RequiresPermissions(HjhLabelDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request,
			@ModelAttribute(HjhLabelDefine.PUSHMONEY_FORM) HjhLabelBean form) {
		// 日志开始
		LogUtil.startLog(HjhLabelController.class.toString(), HjhLabelDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(HjhLabelDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		// 日志结束
		LogUtil.endLog(HjhLabelController.class.toString(), HjhLabelDefine.INIT);
		return modelAndView;
	}

	/**
     * 画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(HjhLabelDefine.SEARCH_ACTION)
    @RequiresPermissions(HjhLabelDefine.PERMISSIONS_SEARCH)
    public ModelAndView search(HttpServletRequest request, HjhLabelBean form) {
        LogUtil.startLog(HjhLabelController.class.toString(), HjhLabelDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(HjhLabelDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(HjhLabelController.class.toString(), HjhLabelDefine.INIT);
        return modelAndView;
    }
	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, HjhLabelBean form) {
		List<HjhLabelCustomize> recordList = labelService.getRecordList(form, -1, -1);
		// 资产来源
        List<HjhInstConfig> hjhInstConfigs=this.labelService.hjhInstConfigList("");
        modelAndView.addObject("hjhInstConfigList", hjhInstConfigs);
        
        //产品类型 
        List<HjhAssetType> hjhAssetTypes = this.labelService.hjhAssetTypeList(form.getInstCodeSrch());
        modelAndView.addObject("assetTypeList", hjhAssetTypes);
        // 项目类型
        List<BorrowProjectType> borrowProjectTypeList = this.labelService.borrowProjectTypeList("");
        modelAndView.addObject("borrowProjectTypeList", borrowProjectTypeList);
        // 还款方式
        List<BorrowStyle> borrowStyleList = this.labelService.borrowStyleList("");
        modelAndView.addObject("borrowStyleList", borrowStyleList);
		if (recordList != null) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			recordList = this.labelService.getRecordList(form, paginator.getOffset(),
					paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(HjhLabelDefine.PUSHMONEY_FORM, form);
		}
	}
	
	/**
     * 下拉联动
     *
     * @param request
     * @return 进入资产列表页面
     */
    @RequestMapping(HjhLabelDefine.ASSETTYPE_ACTION)
    @RequiresPermissions(HjhLabelDefine.PERMISSIONS_VIEW)
    @ResponseBody
    public List<Map<String, Object>> assetTypeAction(HttpServletRequest request, RedirectAttributes attr,
            String instCode) {
        LogUtil.startLog(AssetListController.class.toString(), AssetListDefine.ASSETTYPE_ACTION);
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        // 根据资金来源取得产品类型
        List<HjhAssetType> hjhAssetTypeList = this.labelService.hjhAssetTypeList(instCode);
        if (hjhAssetTypeList != null && hjhAssetTypeList.size() > 0) {
            for (HjhAssetType hjhAssetType : hjhAssetTypeList) {
                Map<String, Object> mapTemp = new HashMap<String, Object>();
                mapTemp.put("id", hjhAssetType.getAssetType());
                mapTemp.put("text", hjhAssetType.getAssetTypeName());
                resultList.add(mapTemp);
            }
        }
        LogUtil.endLog(AssetListController.class.toString(), AssetListDefine.ASSETTYPE_ACTION);
        return resultList;
    }

	/**
	 * 画面迁移(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(HjhLabelDefine.INFO_ACTION)
	@RequiresPermissions(value = { HjhLabelDefine.PERMISSIONS_INFO, HjhLabelDefine.PERMISSIONS_ADD,
			HjhLabelDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView info(HttpServletRequest request,
			@ModelAttribute(HjhLabelDefine.PUSHMONEY_FORM) HjhLabelBean form) {
		LogUtil.startLog(HjhLabelController.class.toString(), HjhLabelDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(HjhLabelDefine.INFO_PATH);
		// 资产来源
        List<HjhInstConfig> hjhInstConfigs=this.labelService.hjhInstConfigList("");
        modelAndView.addObject("hjhInstConfigList", hjhInstConfigs);
        // 项目类型
        List<BorrowProjectType> borrowProjectTypeList = this.labelService.borrowProjectTypeList("");
        modelAndView.addObject("borrowProjectTypeList", borrowProjectTypeList);
        // 还款方式
        List<BorrowStyle> borrowStyleList = this.labelService.borrowStyleList("");
        modelAndView.addObject("borrowStyleList", borrowStyleList);
		if (StringUtils.isNotEmpty(form.getIds())) {
			Integer id = Integer.valueOf(form.getIds());
			HjhLabelCustomize record = this.labelService.getRecord(id);
            BeanUtils.copyProperties(record, form);
            if(record.getPushTimeStart()!=null){
                form.setPushTimeStartString(DateUtils.getNowDateByHH(record.getPushTimeStart()));
            }
            if(record.getPushTimeEnd()!=null){
                form.setPushTimeEndString(DateUtils.getNowDateByHH(record.getPushTimeEnd()));
            }
			//产品类型 
	        List<HjhAssetType> hjhAssetTypes = this.labelService.hjhAssetTypeList(record.getInstCode());
	        modelAndView.addObject("assetTypeList", hjhAssetTypes);
			modelAndView.addObject(HjhLabelDefine.PUSHMONEY_FORM, form);
		}
		LogUtil.endLog(HjhLabelController.class.toString(), HjhLabelDefine.INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 数据添加
	 * 
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(HjhLabelDefine.INSERT_ACTION)
	@RequiresPermissions(HjhLabelDefine.PERMISSIONS_ADD)
	public ModelAndView add(HjhLabelBean form) {
		// 日志开始
		LogUtil.startLog(HjhLabelController.class.toString(), HjhLabelDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(HjhLabelDefine.INFO_PATH);
		
		// 画面验证
        this.validatorFieldCheck(modelAndView, form, true);
        if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
            modelAndView.addObject(HjhLabelDefine.PUSHMONEY_FORM, form);
         // 资产来源
            List<HjhInstConfig> hjhInstConfigs=this.labelService.hjhInstConfigList("");
            modelAndView.addObject("hjhInstConfigList", hjhInstConfigs);
            // 项目类型
            List<BorrowProjectType> borrowProjectTypeList = this.labelService.borrowProjectTypeList("");
            modelAndView.addObject("borrowProjectTypeList", borrowProjectTypeList);
            // 还款方式
            List<BorrowStyle> borrowStyleList = this.labelService.borrowStyleList("");
            modelAndView.addObject("borrowStyleList", borrowStyleList);
          //产品类型 
            List<HjhAssetType> hjhAssetTypes = this.labelService.hjhAssetTypeList(form.getInstCode());
            modelAndView.addObject("assetTypeList", hjhAssetTypes);
            return modelAndView;
        }
		// 成功插入
        HjhLabel label = new HjhLabel();
        BeanUtils.copyProperties(form, label);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        try {
            label.setPushTimeStart(sdf.parse(form.getPushTimeStartString()));
            label.setPushTimeEnd(sdf.parse(form.getPushTimeEndString()));
        } catch (ParseException e) {
            ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "pushTimeStart", "pushTimeStart.error");
        }
		this.labelService.insertRecord(label);
		modelAndView.addObject(HjhLabelDefine.SUCCESS, HjhLabelDefine.SUCCESS);
		// 日志结束
		LogUtil.endLog(HjhLabelController.class.toString(), HjhLabelDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 数据修改
	 * 
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(HjhLabelDefine.UPDATE_ACTION)
	@RequiresPermissions(HjhLabelDefine.PERMISSIONS_MODIFY)
	public ModelAndView update(HjhLabelBean form) {
		// 日志开始
		LogUtil.startLog(HjhLabelController.class.toString(), HjhLabelDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(HjhLabelDefine.INFO_PATH);
		
		// 画面验证
        this.validatorFieldCheck(modelAndView, form, true);
        if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
         // 资产来源
            List<HjhInstConfig> hjhInstConfigs=this.labelService.hjhInstConfigList("");
            modelAndView.addObject("hjhInstConfigList", hjhInstConfigs);
            // 项目类型
            List<BorrowProjectType> borrowProjectTypeList = this.labelService.borrowProjectTypeList("");
            modelAndView.addObject("borrowProjectTypeList", borrowProjectTypeList);
            // 还款方式
            List<BorrowStyle> borrowStyleList = this.labelService.borrowStyleList("");
            modelAndView.addObject("borrowStyleList", borrowStyleList);
          //产品类型 
            List<HjhAssetType> hjhAssetTypes = this.labelService.hjhAssetTypeList(form.getInstCode());
            modelAndView.addObject("assetTypeList", hjhAssetTypes);
            modelAndView.addObject(HjhLabelDefine.PUSHMONEY_FORM, form);
            return modelAndView;
        }
		// 成功修改
        HjhLabel label = new HjhLabel();
        BeanUtils.copyProperties(form, label);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        try {
            label.setPushTimeStart(sdf.parse(form.getPushTimeStartString()));
            label.setPushTimeEnd(sdf.parse(form.getPushTimeEndString()));
        } catch (ParseException e) {
            ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "pushTimeStart", "pushTimeStartError.error");
        }
        HjhLabelCustomize record = this.labelService.getRecord(label.getId());
        if(StringUtils.isNotEmpty(label.getLabelName())){
        	if(record!=null){
        		//判断标签名称是否重复
            	if(!record.getLabelName().equals(label.getLabelName())){
            		if(this.labelService.isExistsRecordByName(label)){
            			ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "labelName", "labelNameError.error");
            		}else{
            			AllocationEngineBean bean=new AllocationEngineBean();
                		bean.setLabelName(record.getLabelName());
                		//修改标签名称后更改标的分配规则引擎中标签名称
                		List<HjhAllocationEngine> recordList = this.labelService.getAllocationEngineRecordList(bean);
                		if(recordList.size()>0){
                			for(int i=0;i<recordList.size();i++){
                				HjhAllocationEngine engine=new HjhAllocationEngine();
                				engine.setId(recordList.get(i).getId());
                				engine.setLabelName(label.getLabelName());
                				this.labelService.updatePlanConfigRecord(engine);
                			}
                		}
            		}          	
            	}
        	}
        }
		this.labelService.updateRecord(label);
		modelAndView.addObject(HjhLabelDefine.SUCCESS, HjhLabelDefine.SUCCESS);
		// 日志结束
		LogUtil.endLog(HjhLabelController.class.toString(), HjhLabelDefine.INSERT_ACTION);
		return modelAndView;
	}
	/**
     * 修改状态
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(HjhLabelDefine.STATUS_ACTION)
    @RequiresPermissions(HjhLabelDefine.PERMISSIONS_MODIFY)
    public ModelAndView updateStatus(HttpServletRequest request, String ids) {
        LogUtil.startLog(HjhLabelController.class.toString(), HjhLabelDefine.STATUS_ACTION);

        ModelAndView modelAndView = new ModelAndView(HjhLabelDefine.RE_LIST_PATH);
        if (ids != null) {
            // 修改状态
            HjhLabel record = this.labelService.getRecordById(Integer.parseInt(ids));
            if (record.getLabelState() == 1) {
                record.setLabelState(0);;
            } else {
                record.setLabelState(1);
            }
            this.labelService.updateRecord(record);
        }
        LogUtil.endLog(RepayStyleController.class.toString(), RepayStyleDefine.STATUS_ACTION);
        return modelAndView;
    }

	/**
	 * 调用校验表单方法
	 * 
	 * @param modelAndView
	 * @param form
	 * @return
	 */
    private void validatorFieldCheck(ModelAndView modelAndView, HjhLabelBean form, boolean isUpdate) {
        // 标的期限
        /*if (form.getLabelTermEnd() == null || form.getLabelTermStart() == null ) {
            ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "labelTermStart", "labelTerm.error");
        }*/
        if (form.getLabelTermEnd() != null && form.getLabelTermStart() != null ) {
            if (form.getLabelTermEnd() < form.getLabelTermStart()) {
                ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "labelTermStart", "labelTermEnd.error");
            }
        }
        // 标的实际利率
        if (form.getLabelAprEnd() != null && form.getLabelAprStart() !=null) {
            if (form.getLabelAprEnd().compareTo(form.getLabelAprStart()) < 0) {
                ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "labelAprStart", "labelAprStart.error");
            }
        }
        // 标的实际支付金额 
        if (form.getLabelPaymentAccountEnd() != null && form.getLabelPaymentAccountStart()!=null) {
            if (form.getLabelPaymentAccountEnd().compareTo(form.getLabelPaymentAccountStart()) < 0) {
                ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "labelPaymentAccountEnd", "labelPaymentAccountEnd.error");
            }
        }
        // 推送时间节点
        if (StringUtils.isNotEmpty(form.getPushTimeEndString()) && StringUtils.isNotEmpty(form.getPushTimeStartString())) {
           
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            try {
              Date pushTimeStart = sdf.parse(form.getPushTimeStartString());
              Date pushTimeEnd =  sdf.parse(form.getPushTimeEndString());
              if (pushTimeEnd.compareTo(pushTimeStart) < 0) {
                  ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "pushTimeStart", "pushTimeStart.error");
              }
            } catch (ParseException e) {
                ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "pushTimeStart", "pushTimeStartError.error");
            }
            
        }
       
        // 剩余天数remainingDaysStart
        if (form.getRemainingDaysEnd() != null && form.getRemainingDaysStart() != null ) {
            if (form.getRemainingDaysEnd() < form.getRemainingDaysStart()) {
                ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "remainingDaysStart", "remainingDaysStart.error");
            }
        }
        //标签名称重复检验
        if(form.getLabelName() != null && labelService.isExistsRecordByName(form)){
            ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "labelName", "labelName.error");
        }
	}
}
