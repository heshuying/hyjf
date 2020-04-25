package com.hyjf.admin.invite.drawconf;

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
@RequestMapping(value = DrawConfDefine.REQUEST_MAPPING)
public class DrawConfController extends BaseController {

	@Autowired
	private DrawConfService confService;

	/**
	 * 奖品配置列表
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(DrawConfDefine.INIT)
	@RequiresPermissions(DrawConfDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(DrawConfDefine.CONF_FORM) DrawConfBean form) {
		LogUtil.startLog(DrawConfController.class.toString(), DrawConfDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(DrawConfDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(DrawConfController.class.toString(), DrawConfDefine.INIT);
		return modelAndView;
	}

	/**
	 * 奖品配置列表条件查询
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(DrawConfDefine.SEARCH_ACTION)
	@RequiresPermissions(DrawConfDefine.PERMISSIONS_SEARCH)
	public ModelAndView select(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(DrawConfDefine.CONF_FORM) DrawConfBean form) {
		LogUtil.startLog(DrawConfController.class.toString(), DrawConfDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(DrawConfDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(DrawConfController.class.toString(), DrawConfDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 奖品配置列表维护分页机能 页面初始化
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, DrawConfBean form) {
	    // 奖品类型
        List<ParamName> prizeTypes = this.confService.getParamNameList("PRIZE_TYPE");
        modelAndView.addObject("prizeType", prizeTypes);
        
		List<PrizeGetCustomize> recordList = this.confService.getRecordList(form, -1, -1);
		if (recordList != null) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			recordList = this.confService.getRecordList(form, paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
			modelAndView.addObject(DrawConfDefine.CONF_FORM, form);
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
    @RequestMapping(DrawConfDefine.INFO_ACTION)
    @RequiresPermissions(value = { DrawConfDefine.PERMISSIONS_INFO, DrawConfDefine.PERMISSIONS_ADD,
            AppBannerDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
    public ModelAndView info(HttpServletRequest request, RedirectAttributes attr,
            @ModelAttribute(DrawConfDefine.CONF_FORM) DrawConfBean form) {
        LogUtil.startLog(DrawConfController.class.toString(), DrawConfDefine.INFO_ACTION);
        ModelAndView modelAndView = new ModelAndView(DrawConfDefine.INFO_PATH);
        
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
            modelAndView.addObject(DrawConfDefine.CONF_FORM, mymap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtil.endLog(DrawConfController.class.toString(), DrawConfDefine.INFO_ACTION);
        return modelAndView;
    }

    /**
     * 添加奖品信息
     * 
     * @param request
     * @param form
     * @return
     */
    @RequiresPermissions(DrawConfDefine.PERMISSIONS_ADD)
    @RequestMapping(value = DrawConfDefine.INSERT_ACTION, method = RequestMethod.POST)
    public ModelAndView insertAction(HttpServletRequest request, DrawConfBean form) {
        LogUtil.startLog(DrawConfController.class.toString(), DrawConfDefine.INSERT_ACTION);
        ModelAndView modelAndView = new ModelAndView(DrawConfDefine.INFO_PATH);
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
        modelAndView.addObject(DrawConfDefine.SUCCESS, DrawConfDefine.SUCCESS);
        LogUtil.endLog(DrawConfController.class.toString(), DrawConfDefine.INSERT_ACTION);
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
    @RequiresPermissions(DrawConfDefine.PERMISSIONS_MODIFY)
    @RequestMapping(value = DrawConfDefine.UPDATE_ACTION, method = RequestMethod.POST)
    public ModelAndView updateAction(HttpServletRequest request, DrawConfBean form) {
        LogUtil.startLog(DrawConfController.class.toString(), DrawConfDefine.UPDATE_ACTION);
        ModelAndView modelAndView = new ModelAndView(DrawConfDefine.INFO_PATH);
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
        
        modelAndView.addObject(DrawConfDefine.SUCCESS, DrawConfDefine.SUCCESS);
        LogUtil.endLog(DrawConfController.class.toString(), DrawConfDefine.UPDATE_ACTION);
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
    @RequiresPermissions(DrawConfDefine.PERMISSIONS_MODIFY)
    @RequestMapping(value = DrawConfDefine.STATUS_ACTION, method = RequestMethod.POST)
    public ModelAndView statusAction(HttpServletRequest request, DrawConfBean form) {
        LogUtil.startLog(DrawConfController.class.toString(), DrawConfDefine.STATUS_ACTION);
        ModelAndView modelAndView = new ModelAndView("redirect:" + DrawConfDefine.REQUEST_MAPPING + "/init");
        
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
        
        modelAndView.addObject(DrawConfDefine.SUCCESS, DrawConfDefine.SUCCESS);
        LogUtil.endLog(DrawConfController.class.toString(), DrawConfDefine.STATUS_ACTION);
        return modelAndView;
    }
    

    /**
     * 调用校验表单方法
     * 
     * @param modelAndView
     * @param form
     * @return
     */
    private ModelAndView validatorFieldCheck(ModelAndView modelAndView, DrawConfBean form) {
        // 奖品名称必填
        ValidatorFieldCheckUtil.validateRequired(modelAndView, "prizeName", form.getPrizeName());
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
    @RequestMapping(value = DrawConfDefine.UPLOAD_FILE, method = RequestMethod.POST)
    @RequiresPermissions(value = { DrawConfDefine.PERMISSIONS_ADD, DrawConfDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
    public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LogUtil.startLog(ConfigController.class.toString(), DrawConfDefine.UPLOAD_FILE);
        String files = confService.uploadFile(request, response);
        LogUtil.endLog(ConfigController.class.toString(), DrawConfDefine.UPLOAD_FILE);
        return files;
    }
    
    /**
	 * 根据业务需求导出相应的表格 
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(DrawConfDefine.EXPORT_DRAW_CONF_ACTION)
	@RequiresPermissions(DrawConfDefine.PERMISSIONS_EXPORT)
	public void exportDrawConf(@ModelAttribute DrawConfBean form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		LogUtil.startLog(GetRecommendDefine.THIS_CLASS, GetRecommendDefine.EXPORT_INVITE_USER_ACTION);
		// 表格sheet名称
		String sheetName = "抽奖配置";
		// 文件名称
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date())
				+ CustomConstants.EXCEL_EXT;
		
		List<PrizeGetCustomize> recordList = this.confService.getRecordList(form, -1, -1);
		String[] titles = new String[] { "序号", "奖品名称", "奖品类型", "排序", "中奖几率", "数量上限","已抽奖数量" };
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
					// 奖品类型
					String prizeType = StringUtils.equals(prizeConf.getPrizeType(), "1") ? "实物奖品" : "优惠券";
					// 排序
					Integer prizeSort = prizeConf.getPrizeSort();
					// 中奖几率
					String prizeProbability = prizeConf.getPrizeProbability()+"%";
					// 数量上限
					Integer prizeQuantity = prizeConf.getPrizeQuantity();
					// 已抽取数量
					Integer drawedPrizeQuantity = prizeConf.getPrizeQuantity() - prizeConf.getPrizeReminderQuantity();
					
					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					if (celLength == 0) {// 序号
						cell.setCellValue(i + 1);
					} else if (celLength == 1) { // 奖品名称
						cell.setCellValue(prizeName);
					} else if (celLength == 2) { // 奖品类型
						cell.setCellValue(prizeType);
					} else if (celLength == 3) { // 排序
						cell.setCellValue(prizeSort);
					} else if (celLength == 4) {// 中奖几率
						cell.setCellValue(prizeProbability);
					} else if (celLength == 5) {// 数量上限
						cell.setCellValue(prizeQuantity);
					} else if (celLength == 6) {// 已抽取数量
						cell.setCellValue(drawedPrizeQuantity);
					} 
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
		LogUtil.endLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.EXPORT_USERS_ACTION);
	}

}
