package com.hyjf.admin.invite.exchangeconf;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
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
import com.hyjf.admin.app.maintenance.banner.AppBannerDefine;
import com.hyjf.admin.invite.GetRecommend.GetRecommendDefine;
import com.hyjf.admin.maintenance.config.ConfigController;
import com.hyjf.admin.manager.user.manageruser.ManageUsersDefine;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.Validator;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.admin.PrizeGetCustomize;

/**
 * 兑换奖品配置表
 * 
 * @author qingbing
 *
 */
@Controller
@RequestMapping(value = ExchangeConfDefine.REQUEST_MAPPING)
public class ExchangeConfController extends BaseController {

	@Autowired
	private ExchangeConfService confService;

	/**
	 * 奖品配置列表
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ExchangeConfDefine.INIT)
	@RequiresPermissions(ExchangeConfDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(ExchangeConfDefine.CONF_FORM) ExchangeConfBean form) {
		LogUtil.startLog(ExchangeConfController.class.toString(), ExchangeConfDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(ExchangeConfDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(ExchangeConfController.class.toString(), ExchangeConfDefine.INIT);
		return modelAndView;
	}

	/**
	 * 奖品配置列表条件查询
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ExchangeConfDefine.SEARCH_ACTION)
	@RequiresPermissions(ExchangeConfDefine.PERMISSIONS_SEARCH)
	public ModelAndView select(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(ExchangeConfDefine.CONF_FORM) ExchangeConfBean form) {
		LogUtil.startLog(ExchangeConfController.class.toString(), ExchangeConfDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(ExchangeConfDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(ExchangeConfController.class.toString(), ExchangeConfDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 奖品配置列表维护分页机能 页面初始化
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, ExchangeConfBean form) {
	    // 奖品类型
        List<ParamName> prizeTypes = this.confService.getParamNameList("PRIZE_TYPE");
        modelAndView.addObject("prizeType", prizeTypes);
        
		List<PrizeGetCustomize> recordList = this.confService.getRecordList(form, -1, -1);
		if (recordList != null) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			recordList = this.confService.getRecordList(form, paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
			modelAndView.addObject(ExchangeConfDefine.CONF_FORM, form);
			String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
			modelAndView.addObject("fileDomainUrl", fileDomainUrl);
		}
	}
	
	/**
     * 画面迁移(含有id更新，不含有id添加)
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(ExchangeConfDefine.INFO_ACTION)
    @RequiresPermissions(value = { ExchangeConfDefine.PERMISSIONS_INFO, ExchangeConfDefine.PERMISSIONS_ADD,
            AppBannerDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
    public ModelAndView info(HttpServletRequest request, RedirectAttributes attr,
            @ModelAttribute(ExchangeConfDefine.CONF_FORM) ExchangeConfBean form) {
        LogUtil.startLog(ExchangeConfController.class.toString(), ExchangeConfDefine.INFO_ACTION);
        ModelAndView modelAndView = new ModelAndView(ExchangeConfDefine.INFO_PATH);
        
        // 奖品类型
        List<ParamName> prizeTypes = this.confService.getParamNameList("PRIZE_TYPE");
        modelAndView.addObject("prizeType", prizeTypes);
        
        Map<String, Object> mymap = new HashMap<String, Object>();
        try {
            if (StringUtils.isNotEmpty(form.getIds())) {
                InvitePrizeConfCustom record = this.confService.getPrizeByGroupCode(form.getIds());
                // 添加回显
                mymap = convertBean(record);
                String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
                modelAndView.addObject("fileDomainUrl", fileDomainUrl);
            }else {
                //初始化
                mymap.put("prizeType", CustomConstants.CONF_PRIZE_TYPE_COUPON);
                mymap.put("prizeStatus", CustomConstants.CONF_PRIZE_STATUS_ON);
            }
            modelAndView.addObject(ExchangeConfDefine.CONF_FORM, mymap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtil.endLog(ExchangeConfController.class.toString(), ExchangeConfDefine.INFO_ACTION);
        return modelAndView;
    }

    /**
     * 添加奖品信息
     * 
     * @param request
     * @param form
     * @return
     */
    @RequiresPermissions(ExchangeConfDefine.PERMISSIONS_ADD)
    @RequestMapping(value = ExchangeConfDefine.INSERT_ACTION, method = RequestMethod.POST)
    public ModelAndView insertAction(HttpServletRequest request, ExchangeConfBean form) {
        LogUtil.startLog(ExchangeConfController.class.toString(), ExchangeConfDefine.INSERT_ACTION);
        ModelAndView modelAndView = new ModelAndView(ExchangeConfDefine.INFO_PATH);
        // 调用校验
        if (ValidatorFieldCheckUtil.hasValidateError(validatorFieldCheck(modelAndView, form))) {
            // 失败返回
            return modelAndView;
        }
        
        // 数据插入
        String[] couponCodes;
        if(form.getCouponCode().contains(",")){
            couponCodes = form.getCouponCode().split(",");
        }else{
            couponCodes = new String[]{form.getCouponCode()};
        }
        form.setPrizeGroupCode(GetCode.generatePrizeGroupCode());
        for(String code : couponCodes){
            form.setCouponCode(code);
            confService.insertRecord(form);
        }
        modelAndView.addObject(ExchangeConfDefine.SUCCESS, ExchangeConfDefine.SUCCESS);
        LogUtil.endLog(ExchangeConfController.class.toString(), ExchangeConfDefine.INSERT_ACTION);
        return modelAndView;
    }
    
    /**
     * 
     * 更新奖品信息
     * @author hsy
     * @param request
     * @param form
     * @return
     */
    @RequiresPermissions(ExchangeConfDefine.PERMISSIONS_MODIFY)
    @RequestMapping(value = ExchangeConfDefine.UPDATE_ACTION, method = RequestMethod.POST)
    public ModelAndView updateAction(HttpServletRequest request, ExchangeConfBean form) {
        LogUtil.startLog(ExchangeConfController.class.toString(), ExchangeConfDefine.UPDATE_ACTION);
        
        ModelAndView modelAndView = new ModelAndView(ExchangeConfDefine.INFO_PATH);
        
        if(Validator.isNull(form.getPrizeGroupCode())){
            return modelAndView;
        }
        // 调用校验
        if (ValidatorFieldCheckUtil.hasValidateError(validatorFieldCheck(modelAndView, form))) {
            // 失败返回
            return modelAndView;
        }
        
        // 更新奖品信息
        confService.updatePrizeConfig(form);
        
        modelAndView.addObject(ExchangeConfDefine.SUCCESS, ExchangeConfDefine.SUCCESS);
        LogUtil.endLog(ExchangeConfController.class.toString(), ExchangeConfDefine.UPDATE_ACTION);
        return modelAndView;
    }
    
    /**
     * 
     * 可用状态修改
     * @author hsy
     * @param request
     * @param form
     * @return
     */
    @RequiresPermissions(ExchangeConfDefine.PERMISSIONS_MODIFY)
    @RequestMapping(value = ExchangeConfDefine.STATUS_ACTION, method = RequestMethod.POST)
    public ModelAndView statusAction(HttpServletRequest request, ExchangeConfBean form) {
        LogUtil.startLog(ExchangeConfController.class.toString(), ExchangeConfDefine.STATUS_ACTION);
        ModelAndView modelAndView = new ModelAndView("redirect:" + ExchangeConfDefine.REQUEST_MAPPING + "/init");
        // 调用校验
        if (Validator.isNull(form.getIds())) {
            // 失败返回
            return modelAndView;
        }
        
        String[] paramArray = form.getIds().split(":");
        if(paramArray.length != 2){
            return modelAndView;
        }
        
        String groupCode = paramArray[0];
        String prizeStatus = CustomConstants.CONF_PRIZE_STATUS_ON;
        if(paramArray[1].equals(CustomConstants.CONF_PRIZE_STATUS_ON)){
            prizeStatus = CustomConstants.CONF_PRIZE_STATUS_OFF;
        }
        else if(paramArray[1].equals(CustomConstants.CONF_PRIZE_STATUS_OFF)){
            prizeStatus = CustomConstants.CONF_PRIZE_STATUS_ON;
        }
        
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("prizeGroupCode", groupCode);
        paraMap.put("prizeStatus", prizeStatus);
        
        confService.updatePrizeStatus(paraMap);
        
        modelAndView.addObject(ExchangeConfDefine.SUCCESS, ExchangeConfDefine.SUCCESS);
        LogUtil.endLog(ExchangeConfController.class.toString(), ExchangeConfDefine.STATUS_ACTION);
        return modelAndView;
    }
    

    /**
     * 调用校验表单方法
     * 
     * @param modelAndView
     * @param form
     * @return
     */
    private ModelAndView validatorFieldCheck(ModelAndView modelAndView, ExchangeConfBean form) {
        // 奖品名称必填
        ValidatorFieldCheckUtil.validateRequired(modelAndView, "prizeName", form.getPrizeName());
        //奖品数量必填
        if(Validator.isNull(form.getPrizeQuantity())){
            ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "prizeQuantity", "required");
        }
        //所需推荐星必填
        if(Validator.isNull(form.getRecommendQuantity())){
            ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "recommendQuantity", "required");
        }
        //如果是优惠券则校验优惠券编号
        if(form.getPrizeType().equals(2) || form.getPrizeType().equals(3) || form.getPrizeType().equals(4)){
            ValidatorFieldCheckUtil.validateRequired(modelAndView, "couponCode", form.getCouponCode());
        }
        return modelAndView;
    }
    
    /**
     * 资料上传
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = ExchangeConfDefine.UPLOAD_FILE, method = RequestMethod.POST)
    @RequiresPermissions(value = { ExchangeConfDefine.PERMISSIONS_ADD, ExchangeConfDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
    public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LogUtil.startLog(ConfigController.class.toString(), ExchangeConfDefine.UPLOAD_FILE);
        String files = confService.uploadFile(request, response);
        LogUtil.endLog(ConfigController.class.toString(), ExchangeConfDefine.UPLOAD_FILE);
        return files;
    }
    
    /**
	 * 根据业务需求导出相应的表格 
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(ExchangeConfDefine.EXPORT_EXCHANGE_CONF_ACTION)
	@RequiresPermissions(ExchangeConfDefine.PERMISSIONS_EXPORT)
	public void exportExchangeConf(@ModelAttribute ExchangeConfBean form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		LogUtil.startLog(GetRecommendDefine.THIS_CLASS, GetRecommendDefine.EXPORT_INVITE_USER_ACTION);
		// 表格sheet名称
		String sheetName = "兑换配置";
		// 文件名称
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date())
				+ CustomConstants.EXCEL_EXT;
		
		// List<GetRecommendCustomize> recordList = this.getRecommendService.getRecordList(form);
		List<PrizeGetCustomize> recordList = this.confService.getRecordList(form, -1, -1);
		String[] titles = new String[] { "序号", "奖品名称", "所需推荐星", "奖品类型", "排序", "奖品总数量", "已兑换数量" };
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, sheetName + "_第1页");

		if (recordList != null && recordList.size() > 0) {

			int sheetCount = 1;
			int rowNum = 0;

			for (int i = 0; i < recordList.size(); i++) {
				rowNum++;
				if (i != 0 && i % 60000 == 0) {
					sheetCount++;
					sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles,
							(sheetName + "_第" + sheetCount + "页"));
					rowNum = 1;
				}

				// 新建一行
				Row row = sheet.createRow(rowNum);
				// 循环数据
				for (int celLength = 0; celLength < titles.length; celLength++) {
					PrizeGetCustomize prizeConf = recordList.get(i);
					// 奖品名称
					String prizeName = prizeConf.getPrizeName();
					// 所需推荐星
					Integer recommendQuantity = prizeConf.getRecommendQuantity();
					// 奖品类型
					String prizeType = StringUtils.equals(prizeConf.getPrizeType(), "1") ? "实物奖品" : "优惠券";
					// 排序
					Integer prizeSort = prizeConf.getPrizeSort();
					// 奖品总数量
					Integer prizeQuantity = prizeConf.getPrizeQuantity();
					// 已兑换数量
					Integer exchangePrizeQuantity = prizeConf.getPrizeQuantity() - prizeConf.getPrizeReminderQuantity();
					
					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					if (celLength == 0) {// 序号
						cell.setCellValue(i + 1);
					} else if (celLength == 1) { // 奖品名称
						cell.setCellValue(prizeName);
					} else if (celLength == 2) { // 所需推荐星数量
						cell.setCellValue(recommendQuantity);
					} else if (celLength == 3) { // 奖品类型
						cell.setCellValue(prizeType);
					} else if (celLength == 4) {// 排序
						cell.setCellValue(prizeSort);
					} else if (celLength == 5) {// 奖品总数量
						cell.setCellValue(prizeQuantity);
					} else if (celLength == 6) {// 已兑换数量
						cell.setCellValue(exchangePrizeQuantity);
					} 
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
		LogUtil.endLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.EXPORT_USERS_ACTION);
	}
    

}
