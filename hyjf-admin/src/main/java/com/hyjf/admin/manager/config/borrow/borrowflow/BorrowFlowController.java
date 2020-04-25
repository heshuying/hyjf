package com.hyjf.admin.manager.config.borrow.borrowflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.auto.HjhAssetBorrowType;
import com.hyjf.mybatis.model.auto.HjhAssetType;
import com.hyjf.mybatis.model.auto.HjhInstConfig;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.HjhAssetBorrowTypeCustomize;

/**
 * 
 * 标的流程配置新
 * @author hyjf
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年8月24日
 * @see 上午10:36:14
 */
@Controller
@RequestMapping(BorrowFlowDefine.REQUEST_MAPPING)
public class BorrowFlowController extends BaseController {

    @Autowired
    private BorrowFlowService borrowFlowService;

    /**
     * init
     * @author liubin
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(BorrowFlowDefine.INIT)
    @RequiresPermissions(BorrowFlowDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, BorrowFlowBean form) {
        LogUtil.startLog(BorrowFlowDefine.THIS_CLASS, BorrowFlowDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(BorrowFlowDefine.LIST_PATH);
        // 分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(BorrowFlowDefine.THIS_CLASS, BorrowFlowDefine.INIT);
        return modelAndView;
    }

    /**
     * 
     * 列表检索Action
     * @author liubin
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(BorrowFlowDefine.SEARCH_ACTION)
    @RequiresPermissions(BorrowFlowDefine.PERMISSIONS_SEARCH)
    public ModelAndView search(HttpServletRequest request, BorrowFlowBean form) {
        LogUtil.startLog(BorrowFlowDefine.THIS_CLASS, BorrowFlowDefine.SEARCH_ACTION);
        ModelAndView modelAndView = new ModelAndView(BorrowFlowDefine.LIST_PATH);
        // 分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(BorrowFlowDefine.THIS_CLASS, BorrowFlowDefine.SEARCH_ACTION);
        return modelAndView;
    }

    /**
     * 标的流程分页技能
     * @author liubin
     * @param request
     * @param modeAndView
     * @param form
     */
    public void createPage(HttpServletRequest request, ModelAndView modelAndView, BorrowFlowBean form) {
        // 项目列表
        List<BorrowProjectType> borrowProjectTypeList = this.borrowFlowService.borrowProjectTypeList("HZT");
        modelAndView.addObject("borrowProjectTypeList", borrowProjectTypeList);
		// 资金来源
		List<HjhInstConfig> hjhInstConfigList = this.borrowFlowService.hjhInstConfigList("");
		modelAndView.addObject("hjhInstConfigList", hjhInstConfigList);
		// 产品类型
		List<HjhAssetType> assetTypeList = this.borrowFlowService.hjhAssetTypeList(form.getInstCodeSrch());
		modelAndView.addObject("assetTypeList", assetTypeList);  
		// 状态
		List<ParamName> statusList = this.borrowFlowService.getParamNameList("FLOW_STATUS");
		modelAndView.addObject("statusList", statusList);
		
        int total = this.borrowFlowService.countRecord(form);
        if (total > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), total);
            List<HjhAssetBorrowTypeCustomize> recordList =
                    this.borrowFlowService.getRecordList(form, paginator.getOffset(), paginator.getLimit());
            form.setRecordList(recordList);
            form.setPaginator(paginator);
        }
        modelAndView.addObject(BorrowFlowDefine.BORROW_FLOW_FORM, form);
    }

    /**
     * 
     * 画面迁移(含有id更新，不含有id添加)
     * @author liubin
     * @param request
     * @param modelAndView
     * @param form
     * @return
     */
    @RequestMapping(BorrowFlowDefine.INFO_ACTION)
    public ModelAndView info(HttpServletRequest request, BorrowFlowBean form) {
        LogUtil.startLog(BorrowFlowDefine.THIS_CLASS, BorrowFlowDefine.INFO_ACTION);
        ModelAndView modelAndView = new ModelAndView(BorrowFlowDefine.INFO_PATH);
        HjhAssetBorrowType record = new HjhAssetBorrowType();
        if (form.getId() != null) {
            record = this.borrowFlowService.getRecordInfo(form.getId());
        }
        modelAndView.addObject(BorrowFlowDefine.BORROW_FLOW_FORM, record);
        
        // 项目列表
        List<BorrowProjectType> borrowProjectTypeList = this.borrowFlowService.borrowProjectTypeList("HZT");
        modelAndView.addObject("borrowProjectTypeList", borrowProjectTypeList);
		// 资金来源
		List<HjhInstConfig> hjhInstConfigList = this.borrowFlowService.hjhInstConfigList("");
		modelAndView.addObject("hjhInstConfigList", hjhInstConfigList);
		// 产品类型
		List<HjhAssetType> assetTypeList = this.borrowFlowService.hjhAssetTypeList(record.getInstCode());
		modelAndView.addObject("assetTypeList", assetTypeList); 
        
        LogUtil.endLog(BorrowFlowDefine.THIS_CLASS, BorrowFlowDefine.INFO_ACTION);
        return modelAndView;
    }

    /**
     * 
     * 插入操作
     * @author liubin
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(BorrowFlowDefine.INSERT_ACTION)
    @RequiresPermissions(BorrowFlowDefine.PERMISSIONS_ADD)
    public ModelAndView insertAction(HttpServletRequest request, BorrowFlowBean form) {
        LogUtil.startLog(BorrowFlowDefine.THIS_CLASS, BorrowFlowDefine.INSERT_ACTION);
        ModelAndView modelAndView = new ModelAndView(BorrowFlowDefine.INFO_PATH);
        // 表单校验
        this.validatorFieldCheck(modelAndView, form);
        if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {//TODO
            // 项目列表
            List<BorrowProjectType> borrowProjectTypeList = this.borrowFlowService.borrowProjectTypeList("HZT");
            modelAndView.addObject("borrowProjectTypeList", borrowProjectTypeList);
    		// 资金来源
    		List<HjhInstConfig> hjhInstConfigList = this.borrowFlowService.hjhInstConfigList("");
    		modelAndView.addObject("hjhInstConfigList", hjhInstConfigList);
    		// 产品类型
    		List<HjhAssetType> assetTypeList = this.borrowFlowService.hjhAssetTypeList(form.getInstCode());
    		modelAndView.addObject("assetTypeList", assetTypeList); 
    		
            modelAndView.addObject(BorrowFlowDefine.BORROW_FLOW_FORM, form);
            LogUtil.errorLog(BorrowFlowDefine.THIS_CLASS, BorrowFlowDefine.INSERT_ACTION, "输入内容验证失败", null);
            return modelAndView;
        }

        // 插入
        this.borrowFlowService.insertRecord(form);
        modelAndView.addObject(BorrowFlowDefine.SUCCESS, BorrowFlowDefine.SUCCESS);
        LogUtil.endLog(BorrowFlowDefine.THIS_CLASS, BorrowFlowDefine.INSERT_ACTION);
        return modelAndView;
    }

    /**
     * 
     * 数据更新
     * @author liubin
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(BorrowFlowDefine.UPDATE_ACTION)
    @RequiresPermissions(BorrowFlowDefine.PERMISSIONS_MODIFY)
    public ModelAndView updateAction(HttpServletRequest request, BorrowFlowBean form) {
        LogUtil.startLog(BorrowFlowDefine.THIS_CLASS, BorrowFlowDefine.UPDATE_ACTION);
        ModelAndView modelAndView = new ModelAndView(BorrowFlowDefine.INFO_PATH);
        // 数据更新
        this.borrowFlowService.updateRecord(form);
        modelAndView.addObject(BorrowFlowDefine.SUCCESS, BorrowFlowDefine.SUCCESS);
        LogUtil.endLog(BorrowFlowDefine.THIS_CLASS, BorrowFlowDefine.UPDATE_ACTION);
        return modelAndView;
    }

    /**
     * 
     * 数据删除
     * @author liubin
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(BorrowFlowDefine.DELETE_ACTION)
    @RequiresPermissions(BorrowFlowDefine.PERMISSIONS_DELETE)
    public String deleteAction(HttpServletRequest request, RedirectAttributes attr, BorrowFlowBean form) {
        LogUtil.startLog(BorrowFlowDefine.THIS_CLASS, BorrowFlowDefine.DELETE_ACTION);
        this.borrowFlowService.deleteRecord(form);
        LogUtil.endLog(BorrowFlowDefine.THIS_CLASS, BorrowFlowDefine.DELETE_ACTION);
        return "redirect:" + BorrowFlowDefine.REQUEST_MAPPING + "/" + BorrowFlowDefine.INIT;
    }

    /**
     * 画面校验
     * 
     * @param modelAndView
     * @param form
     */
    private void validatorFieldCheck(ModelAndView modelAndView, BorrowFlowBean form) {
    	//标的类型
    	ValidatorFieldCheckUtil.validateRequired(modelAndView, "borrowCd", form.getBorrowCd().toString());
    	//机构编号
    	ValidatorFieldCheckUtil.validateRequired(modelAndView, "instCode", form.getInstCode());
    	//资产类型
    	ValidatorFieldCheckUtil.validateRequired(modelAndView, "assetType", form.getAssetType().toString());
//    	是否关联计划
//    	ValidatorFieldCheckUtil.validateRequired(modelAndView, "isAssociatePlan", form.getIsAssociatePlan().toString());
    	//自动录标
    	ValidatorFieldCheckUtil.validateRequired(modelAndView, "autoAdd", form.getAutoAdd().toString());
    	//自动备案
    	ValidatorFieldCheckUtil.validateRequired(modelAndView, "autoRecord", form.getAutoRecord().toString());
    	//自动保证金
//    	ValidatorFieldCheckUtil.validateRequired(modelAndView, "autoBail", form.getAutoBail().toString());
    	//自动初审
    	ValidatorFieldCheckUtil.validateRequired(modelAndView, "autoAudit", form.getAutoAudit().toString());
    	//自动复审
    	ValidatorFieldCheckUtil.validateRequired(modelAndView, "autoReview", form.getAutoReview().toString());
    	//是否开启
    	ValidatorFieldCheckUtil.validateRequired(modelAndView, "isOpen", form.getIsOpen().toString());

        // 检查唯一性
        int cnt = 
                this.borrowFlowService.countRecordByPK(form.getInstCode(),
                		form.getAssetType());
        if (cnt > 0) {	
            ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "instCode", "borrowflow.pk.repeat");
        }

    }
    
	/**
	 * 下拉联动
	 *
	 * @param request
	 * @return 进入资产列表页面
	 */
	@RequestMapping(BorrowFlowDefine.ASSETTYPE_ACTION)
	@RequiresPermissions(BorrowFlowDefine.PERMISSIONS_VIEW)
	@ResponseBody
	public List<Map<String, Object>> assetTypeAction(HttpServletRequest request, RedirectAttributes attr,
			String instCode) {
		LogUtil.startLog(BorrowFlowController.class.toString(), BorrowFlowDefine.ASSETTYPE_ACTION);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		// 根据资金来源取得产品类型
		List<HjhAssetType> hjhAssetTypeList = this.borrowFlowService.hjhAssetTypeList(instCode);
		if (hjhAssetTypeList != null && hjhAssetTypeList.size() > 0) {
			for (HjhAssetType hjhAssetBorrowType : hjhAssetTypeList) {
				Map<String, Object> mapTemp = new HashMap<String, Object>();
				mapTemp.put("id", hjhAssetBorrowType.getAssetType());
				mapTemp.put("text", hjhAssetBorrowType.getAssetTypeName());
				resultList.add(mapTemp);
			}
		}
		LogUtil.endLog(BorrowFlowController.class.toString(), BorrowFlowDefine.ASSETTYPE_ACTION);
		return resultList;
	}
}
