package com.hyjf.admin.manager.config.borrow.finmanchargenew;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.BorrowFinmanNewCharge;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.auto.HjhAssetType;
import com.hyjf.mybatis.model.auto.HjhInstConfig;
import com.hyjf.mybatis.model.customize.BorrowFinmanNewChargeCustomize;

/**
 * 
 * 融资管理费配置新
 * @author hyjf
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年6月13日
 * @see 上午10:36:14
 */
@Controller
@RequestMapping(FinmanChargeNewDefine.REQUEST_MAPPING)
public class FinmanChargeNewController extends BaseController {

    @Autowired
    private FinmanChargeNewService finmanChargeNewService;

    /**
     * init
     * @author liuyang
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(FinmanChargeNewDefine.INIT)
    @RequiresPermissions(FinmanChargeNewDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, FinmanChargeNewBean form) {
        LogUtil.startLog(FinmanChargeNewDefine.THIS_CLASS, FinmanChargeNewDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(FinmanChargeNewDefine.LIST_PATH);
        // 汇直投项目列表
        List<BorrowProjectType> borrowProjectTypeList = this.finmanChargeNewService.borrowProjectTypeList("HZT");
        modelAndView.addObject("borrowProjectTypeList", borrowProjectTypeList);
        // 天标还是月标
        modelAndView.addObject("enddayMonthList",
                this.finmanChargeNewService.getParamNameList(CustomConstants.ENDDAY_MONTH));
        // 分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(FinmanChargeNewDefine.THIS_CLASS, FinmanChargeNewDefine.INIT);
        return modelAndView;
    }

    /**
     * 
     * 列表检索Action
     * @author liuyang
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(FinmanChargeNewDefine.SEARCH_ACTION)
    @RequiresPermissions(FinmanChargeNewDefine.PERMISSIONS_SEARCH)
    public ModelAndView search(HttpServletRequest request, FinmanChargeNewBean form) {
        LogUtil.startLog(FinmanChargeNewDefine.THIS_CLASS, FinmanChargeNewDefine.SEARCH_ACTION);
        ModelAndView modelAndView = new ModelAndView(FinmanChargeNewDefine.LIST_PATH);
        // 汇直投项目列表
        List<BorrowProjectType> borrowProjectTypeList = this.finmanChargeNewService.borrowProjectTypeList("HZT");
        modelAndView.addObject("borrowProjectTypeList", borrowProjectTypeList);
        // 天标还是月标
        modelAndView.addObject("enddayMonthList",
                this.finmanChargeNewService.getParamNameList(CustomConstants.ENDDAY_MONTH));
        // 分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(FinmanChargeNewDefine.THIS_CLASS, FinmanChargeNewDefine.SEARCH_ACTION);
        return modelAndView;
    }

    /**
     * 融资管理费分页技能
     * @author liuyang
     * @param request
     * @param modeAndView
     * @param form
     */
    public void createPage(HttpServletRequest request, ModelAndView modelAndView, FinmanChargeNewBean form) {
		// 资金来源
		List<HjhInstConfig> hjhInstConfigList = this.finmanChargeNewService.hjhInstConfigList("");
		modelAndView.addObject("hjhInstConfigList", hjhInstConfigList);
		// 产品类型
		List<HjhAssetType> assetTypeList = this.finmanChargeNewService.hjhAssetTypeList(form.getInstCodeSrch());
		modelAndView.addObject("assetTypeList", assetTypeList);  
		
        int total = this.finmanChargeNewService.countRecordTotal(form);
        if (total > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), total);
            List<BorrowFinmanNewChargeCustomize> recordList =
                    this.finmanChargeNewService.getRecordList(form, paginator.getOffset(), paginator.getLimit());
            form.setRecordList(recordList);
            form.setPaginator(paginator);
        }
        modelAndView.addObject(FinmanChargeNewDefine.FINMAN_CHARGE_FORM, form);
    }

    /**
     * 
     * 画面迁移(含有id更新，不含有id添加)
     * @author liuyang
     * @param request
     * @param modelAndView
     * @param form
     * @return
     */
    @RequestMapping(FinmanChargeNewDefine.INFO_ACTION)
    public ModelAndView info(HttpServletRequest request, FinmanChargeNewBean form) {
        LogUtil.startLog(FinmanChargeNewDefine.THIS_CLASS, FinmanChargeNewDefine.INFO_ACTION);
        ModelAndView modelAndView = new ModelAndView(FinmanChargeNewDefine.INFO_PATH);
        BorrowFinmanNewCharge record = new BorrowFinmanNewCharge();
        record.setManChargeTimeType("month");
        if (StringUtils.isNotEmpty(form.getManChargeCd())) {
            record = this.finmanChargeNewService.getRecordInfo(form.getManChargeCd());
        }
        modelAndView.addObject(FinmanChargeNewDefine.FINMAN_CHARGE_FORM, record);
        // 汇直投项目列表
        List<BorrowProjectType> borrowProjectTypeList = this.finmanChargeNewService.borrowProjectTypeList("HZT");
        modelAndView.addObject("borrowProjectTypeList", borrowProjectTypeList);
        modelAndView.addObject("enddayMonthList",
                this.finmanChargeNewService.getParamNameList(CustomConstants.ENDDAY_MONTH));
		// 资金来源
		List<HjhInstConfig> hjhInstConfigList = this.finmanChargeNewService.hjhInstConfigList("");
		modelAndView.addObject("hjhInstConfigList", hjhInstConfigList);
		// 产品类型
		List<HjhAssetType> assetTypeList = this.finmanChargeNewService.hjhAssetTypeList(record.getInstCode());
		modelAndView.addObject("assetTypeList", assetTypeList);
        
        LogUtil.endLog(FinmanChargeNewDefine.THIS_CLASS, FinmanChargeNewDefine.INFO_ACTION);
        return modelAndView;
    }

    /**
     * 
     * 插入操作
     * @author liuyang
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(FinmanChargeNewDefine.INSERT_ACTION)
    @RequiresPermissions(FinmanChargeNewDefine.PERMISSIONS_ADD)
    public ModelAndView insertAction(HttpServletRequest request, FinmanChargeNewBean form) {
        LogUtil.startLog(FinmanChargeNewDefine.THIS_CLASS, FinmanChargeNewDefine.INSERT_ACTION);
        ModelAndView modelAndView = new ModelAndView(FinmanChargeNewDefine.INFO_PATH);
        // 表单校验(双表校验)
        this.validatorFieldCheck(modelAndView, form);
        if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
            // 汇直投项目列表
            List<BorrowProjectType> borrowProjectTypeList = this.finmanChargeNewService.borrowProjectTypeList("HZT");
            modelAndView.addObject("borrowProjectTypeList", borrowProjectTypeList);
            modelAndView.addObject("enddayMonthList",
                    this.finmanChargeNewService.getParamNameList(CustomConstants.ENDDAY_MONTH));
    		// 资金来源
    		List<HjhInstConfig> hjhInstConfigList = this.finmanChargeNewService.hjhInstConfigList("");
    		modelAndView.addObject("hjhInstConfigList", hjhInstConfigList);
    		// 产品类型
    		List<HjhAssetType> assetTypeList = this.finmanChargeNewService.hjhAssetTypeList(form.getInstCode());
    		modelAndView.addObject("assetTypeList", assetTypeList);
            modelAndView.addObject(FinmanChargeNewDefine.FINMAN_CHARGE_FORM, form);
            LogUtil.errorLog(FinmanChargeNewDefine.THIS_CLASS, FinmanChargeNewDefine.INSERT_ACTION, "输入内容验证失败", null);
            return modelAndView;
        }
        // 插入(双表插入)
        int cot = this.finmanChargeNewService.insertRecord(form);
        // added by libin 当变更成功后(插入记录数>0)，操作日志表
        if( cot > 0){
        	this.finmanChargeNewService.insertLogRecord(form);
        }
        
        modelAndView.addObject(FinmanChargeNewDefine.SUCCESS, FinmanChargeNewDefine.SUCCESS);
        LogUtil.endLog(FinmanChargeNewDefine.THIS_CLASS, FinmanChargeNewDefine.INSERT_ACTION);
        return modelAndView;
    }

    /**
     * 
     * 数据更新
     * @author liuyang
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(FinmanChargeNewDefine.UPDATE_ACTION)
    @RequiresPermissions(FinmanChargeNewDefine.PERMISSIONS_MODIFY)
    public ModelAndView updateAction(HttpServletRequest request, FinmanChargeNewBean form) {
        LogUtil.startLog(FinmanChargeNewDefine.THIS_CLASS, FinmanChargeNewDefine.UPDATE_ACTION);
        ModelAndView modelAndView = new ModelAndView(FinmanChargeNewDefine.INFO_PATH);
        // 数据更新(双表)
        int cnt = this.finmanChargeNewService.updateRecord(form);
        if(cnt > 0){
        	this.finmanChargeNewService.updateLogRecord(form);//此处更新也是插入，记录新动作而不是修改原动作
        }
        modelAndView.addObject(FinmanChargeNewDefine.SUCCESS, FinmanChargeNewDefine.SUCCESS);
        LogUtil.endLog(FinmanChargeNewDefine.THIS_CLASS, FinmanChargeNewDefine.UPDATE_ACTION);
        return modelAndView;
    }

    /**
     * 
     * 数据删除
     * @author liuyang
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(FinmanChargeNewDefine.DELETE_ACTION)
    @RequiresPermissions(FinmanChargeNewDefine.PERMISSIONS_DELETE)
    public String deleteAction(HttpServletRequest request, RedirectAttributes attr, FinmanChargeNewBean form) {
        LogUtil.startLog(FinmanChargeNewDefine.THIS_CLASS, FinmanChargeNewDefine.DELETE_ACTION);
        int cnt = this.finmanChargeNewService.deleteLogRecord(form);//此处删除是插入日志，记录新动作而不是修改原动作
        if(cnt > 0){
        	this.finmanChargeNewService.deleteRecord(form);//物理删除
        }
        LogUtil.endLog(FinmanChargeNewDefine.THIS_CLASS, FinmanChargeNewDefine.DELETE_ACTION);
        return "redirect:" + FinmanChargeNewDefine.REQUEST_MAPPING + "/" + FinmanChargeNewDefine.INIT;
    }

    /**
     * 画面校验
     * 
     * @param modelAndView
     * @param form
     */
    private void validatorFieldCheck(ModelAndView modelAndView, FinmanChargeNewBean form) {
        // 类型
        ValidatorFieldCheckUtil.validateRequired(modelAndView, "chargeTimeType", form.getManChargeTimeType());
        // 期限
        ValidatorFieldCheckUtil
                .validateRequired(modelAndView, "manChargeTime", String.valueOf(form.getManChargeTime()));
        //项目类型
        ValidatorFieldCheckUtil.validateRequired(modelAndView, "projectType", form.getProjectType());
        //资产来源
        ValidatorFieldCheckUtil.validateRequired(modelAndView, "instCode", form.getInstCode());
        //产品类型
        ValidatorFieldCheckUtil.validateRequired(modelAndView, "assetType", String.valueOf(form.getAssetType()));
        //服务费率
        ValidatorFieldCheckUtil.validateRequired(modelAndView, "chargeRate", form.getChargeRate());
        // 管理费率
        ValidatorFieldCheckUtil.validateRequired(modelAndView, "manChargeRate", form.getManChargeRate());
        // 收益差率
        ValidatorFieldCheckUtil.validateRequired(modelAndView, "returnRate", form.getReturnRate());
        // 逾期利率
        ValidatorFieldCheckUtil.validateRequired(modelAndView, "lateInterest", form.getLateInterest());
        // 逾期免息天数
        ValidatorFieldCheckUtil.validateRequired(modelAndView, "lateFreeDays", String.valueOf(form.getLateFreeDays()));
        // 状态
        ValidatorFieldCheckUtil.validateRequired(modelAndView, "status", String.valueOf(form.getStatus()));
        // 检查唯一性
        int cnt =
                this.finmanChargeNewService.countRecordByProjectType(form.getManChargeTimeType(),
                        form.getManChargeTime(), form.getInstCode(),form.getAssetType());
        if (cnt > 0) {	
            ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "instCode", "mancharge.time.type.repeat");
        }
        // added by libin
        // 检查费率配置Log表的唯一性(by: inst_code资产来源, asset_type产品类型,borrow_period借款期限 定位一条记录)
/*        int count = this.finmanChargeNewService.countRecordByItems(form.getManChargeTime(), form.getInstCode(),form.getAssetType());
        if (count > 0) {	
            ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "instCode", "mancharge.time.type.repeat");
        }*/
    }
    
	/**
	 * 下拉联动
	 *
	 * @param request
	 * @return 进入资产列表页面
	 */
	@RequestMapping(FinmanChargeNewDefine.ASSETTYPE_ACTION)
	@RequiresPermissions(FinmanChargeNewDefine.PERMISSIONS_VIEW)
	@ResponseBody
	public List<Map<String, Object>> assetTypeAction(HttpServletRequest request, RedirectAttributes attr,
			String instCode) {
		LogUtil.startLog(FinmanChargeNewController.class.toString(), FinmanChargeNewDefine.ASSETTYPE_ACTION);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		// 根据资金来源取得产品类型
		List<HjhAssetType> hjhAssetTypeList = this.finmanChargeNewService.hjhAssetTypeList(instCode);
		if (hjhAssetTypeList != null && hjhAssetTypeList.size() > 0) {
			for (HjhAssetType hjhAssetType : hjhAssetTypeList) {
				Map<String, Object> mapTemp = new HashMap<String, Object>();
				mapTemp.put("id", hjhAssetType.getAssetType());
				mapTemp.put("text", hjhAssetType.getAssetTypeName());
				resultList.add(mapTemp);
			}
		}
		LogUtil.endLog(FinmanChargeNewController.class.toString(), FinmanChargeNewDefine.ASSETTYPE_ACTION);
		return resultList;
	}
}
