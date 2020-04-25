package com.hyjf.admin.manager.activity.prize;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.customize.app.AppUserPrizeCodeCustomize;
import com.hyjf.mybatis.model.customize.app.AppUserPrizeOpportunityCustomize;

/**
 * 
 * 此处为类说明
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年7月12日
 * @see 下午4:58:59
 */
@Controller
@RequestMapping(value = PrizeCodeListDefine.REQUEST_MAPPING)
public class PrizeCodeListController extends BaseController {

	@Autowired
	private PrizeCodeListService prizeCodeListService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PrizeCodeListDefine.INIT)
	@RequiresPermissions(PrizeCodeListDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, PrizeCodeListBean form) {
		LogUtil.startLog(PrizeCodeListController.class.toString(), PrizeCodeListDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(PrizeCodeListDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(PrizeCodeListController.class.toString(), PrizeCodeListDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PrizeCodeListDefine.SEARCH_ACTION)
	@RequiresPermissions(PrizeCodeListDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, PrizeCodeListBean form) {
		LogUtil.startLog(PrizeCodeListController.class.toString(), PrizeCodeListDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(PrizeCodeListDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(PrizeCodeListController.class.toString(), PrizeCodeListDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, PrizeCodeListBean form) {

	    Map<String, Object> paraMap = new HashMap<String, Object>();
	    if(StringUtils.isNotEmpty(form.getUserid())){
            paraMap.put("userid", form.getUserid());
        }
	    if(StringUtils.isNotEmpty(form.getUsernameSrch())){
	        paraMap.put("username", form.getUsernameSrch());
	    }
	    if(StringUtils.isNotEmpty(form.getPrizeCodeSrch())){
            paraMap.put("prizeCode", form.getPrizeCodeSrch());
        }
	    if(StringUtils.isNotEmpty(form.getPrizeFlgSrch())){
            paraMap.put("prizeFlg", form.getPrizeFlgSrch());
        }
		Integer count = this.prizeCodeListService.selectRecordCount(paraMap);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			paraMap.put("limitStart", paginator.getOffset());
			paraMap.put("limitEnd", paginator.getLimit());
			List<AppUserPrizeCodeCustomize> recordList = this.prizeCodeListService.selectRecordList(paraMap);
			form.setPaginator(paginator);
			
			modelAndView.addObject("recordList", recordList);
		}

		modelAndView.addObject(PrizeCodeListDefine.PRIZECODE_FORM, form);
	}
	
	
	 /**
     * 画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(PrizeCodeListDefine.INIT_OPPORTUNITY)
    @RequiresPermissions(PrizeCodeListDefine.PERMISSIONS_VIEW)
    public ModelAndView initOpportunity(HttpServletRequest request, PrizeCodeListBean form) {
        LogUtil.startLog(PrizeCodeListController.class.toString(), PrizeCodeListDefine.INIT_OPPORTUNITY);
        ModelAndView modelAndView = new ModelAndView(PrizeCodeListDefine.LIST_OPPORTUNITY_PATH);

        // 创建分页
        this.createOpportunityPage(request, modelAndView, form);
        LogUtil.endLog(PrizeCodeListController.class.toString(), PrizeCodeListDefine.INIT_OPPORTUNITY);
        return modelAndView;
    }

    /**
     * 画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(PrizeCodeListDefine.SEARCH_OPPORTUNITY_ACTION)
    @RequiresPermissions(PrizeCodeListDefine.PERMISSIONS_SEARCH)
    public ModelAndView searchOpportunity(HttpServletRequest request, PrizeCodeListBean form) {
        LogUtil.startLog(PrizeCodeListController.class.toString(), PrizeCodeListDefine.SEARCH_OPPORTUNITY_ACTION);
        ModelAndView modelAndView = new ModelAndView(PrizeCodeListDefine.LIST_OPPORTUNITY_PATH);
        // 创建分页
        this.createOpportunityPage(request, modelAndView, form);
        LogUtil.endLog(PrizeCodeListController.class.toString(), PrizeCodeListDefine.SEARCH_OPPORTUNITY_ACTION);
        return modelAndView;
    }

    /**
     * 创建分页机能
     * 
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createOpportunityPage(HttpServletRequest request, ModelAndView modelAndView, PrizeCodeListBean form) {

        Map<String, Object> paraMap = new HashMap<String, Object>();
        if(StringUtils.isNotEmpty(form.getUsernameSrch())){
            paraMap.put("username", form.getUsernameSrch());
        }
        if(StringUtils.isNotEmpty(form.getOpportunitySrch())){
            paraMap.put("opportunity", form.getOpportunitySrch());
        }
        if(StringUtils.isNotEmpty(form.getPrizeValidSrch())){
            paraMap.put("prizeValid", form.getPrizeValidSrch());
        }
        
        //取得一元夺宝活动配置id
        String activityId = PropUtils.getSystem(CustomConstants.TENDER_PRIZE_ACTIVITY_ID);
        if(StringUtils.isEmpty(activityId)){
            LogUtil.errorLog(PrizeCodeListController.class.toString(), "createOpportunityPage", "活动id没有配置", null);
        }
        ActivityList activity = prizeCodeListService.getActivityListById(Integer.parseInt(activityId));
        if(activity == null){
            LogUtil.errorLog(PrizeCodeListController.class.toString(), "createOpportunityPage", "活动不存在，活动ID：" + activityId, null);
        }
        paraMap.put("activityId", activityId);
        paraMap.put("startTime", activity.getTimeStart());
        paraMap.put("endTime", activity.getTimeEnd());
        
        Integer count = this.prizeCodeListService.selectPrizeOpportunityCount(paraMap);
        if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
            paraMap.put("limitStart", paginator.getOffset());
            paraMap.put("limitEnd", paginator.getLimit());
            List<AppUserPrizeOpportunityCustomize> recordList = this.prizeCodeListService.selectPrizeOpportunityList(paraMap);
            form.setPaginator(paginator);
            
            modelAndView.addObject("recordList", recordList);
        }

        modelAndView.addObject(PrizeCodeListDefine.PRIZECODE_FORM, form);
    }

    

	/**
     * 数据导出
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(PrizeCodeListDefine.EXPORT_ACTION)
    @RequiresPermissions(PrizeCodeListDefine.PERMISSIONS_EXPORT)
    public void exportAction(HttpServletRequest request, HttpServletResponse response, PrizeCodeListBean form) throws Exception {
        LogUtil.startLog(PrizeCodeListController.class.toString(), PrizeCodeListDefine.EXPORT_ACTION);
        // 表格sheet名称
        String sheetName = "兑奖码列表";

        Map<String, Object> paraMap = new HashMap<String, Object>();
        if(StringUtils.isNotEmpty(form.getUsernameSrch())){
            paraMap.put("username", form.getUsernameSrch());
        }
        if(StringUtils.isNotEmpty(form.getPrizeCodeSrch())){
            paraMap.put("prizeCode", form.getPrizeCodeSrch());
        }
        if(StringUtils.isNotEmpty(form.getPrizeFlgSrch())){
            paraMap.put("prizeFlg", form.getPrizeFlgSrch());
        }
        
        List<AppUserPrizeCodeCustomize> resultList = this.prizeCodeListService.selectRecordList(paraMap);

        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

        String[] titles = new String[] { "序号", "用户名", "真实姓名", "手机号", "推荐人", "夺宝奖品", "幸运吗", "夺宝时间", "备注"};
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();

        // 生成一个表格
        HSSFSheet sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, sheetName + "_第1页");

        if (resultList != null && resultList.size() > 0) {

            int sheetCount = 1;
            int rowNum = 0;

            for (int i = 0; i < resultList.size(); i++) {
                rowNum++;
                if (i != 0 && i % 60000 == 0) {
                    sheetCount++;
                    sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, (sheetName + "_第" + sheetCount + "页"));
                    rowNum = 1;
                }

                // 新建一行
                Row row = sheet.createRow(rowNum);
                // 循环数据
                for (int celLength = 0; celLength < titles.length; celLength++) {
                    AppUserPrizeCodeCustomize record = resultList.get(i);

                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);

                    // 序号
                    if (celLength == 0) {
                        cell.setCellValue(i + 1);
                    }
                    else if (celLength == 1) {
                        cell.setCellValue(record.getUsername());
                    }
                    else if (celLength == 2) {
                        cell.setCellValue(record.getTruename());
                    }
                    else if (celLength == 3) {
                        cell.setCellValue(String.valueOf(record.getMobile()==null?"":record.getMobile()));
                    }
                    else if (celLength == 4) {
                        cell.setCellValue(String.valueOf(record.getReferrerUserName()));
                    }
                    else if (celLength == 5) {
                        cell.setCellValue(String.valueOf(record.getPrizeName()));
                    }
                    else if (celLength == 6) {
                        cell.setCellValue(String.valueOf(record.getPrizeCode()));
                    }
                    else if (celLength == 7) {
                        cell.setCellValue(String.valueOf(record.getAddTime()));
                    }
                    else if (celLength == 8) {
                        cell.setCellValue(String.valueOf(record.getPrizeFlg()));
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
        LogUtil.endLog(PrizeCodeListController.class.toString(), PrizeCodeListDefine.EXPORT_ACTION);
    }
    
    /**
     * 数据导出
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(PrizeCodeListDefine.EXPORT_OPPORTUNITY_ACTION)
    @RequiresPermissions(PrizeCodeListDefine.PERMISSIONS_EXPORT)
    public void exportOpportunityAction(HttpServletRequest request, HttpServletResponse response, PrizeCodeListBean form) throws Exception {
        LogUtil.startLog(PrizeCodeListController.class.toString(), PrizeCodeListDefine.EXPORT_OPPORTUNITY_ACTION);
        // 表格sheet名称
        String sheetName = "夺宝次数列表";

        Map<String, Object> paraMap = new HashMap<String, Object>();
        if(StringUtils.isNotEmpty(form.getUsernameSrch())){
            paraMap.put("username", form.getUsernameSrch());
        }
        
        List<AppUserPrizeOpportunityCustomize> resultList = this.prizeCodeListService.selectPrizeOpportunityList(paraMap);

        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

        String[] titles = new String[] { "序号", "用户名", "真实姓名", "手机号", "推荐人", "活动期内累计出借额", "当前可用夺宝机会", "注册时间"};
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();

        // 生成一个表格
        HSSFSheet sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, sheetName + "_第1页");

        if (resultList != null && resultList.size() > 0) {

            int sheetCount = 1;
            int rowNum = 0;

            for (int i = 0; i < resultList.size(); i++) {
                rowNum++;
                if (i != 0 && i % 60000 == 0) {
                    sheetCount++;
                    sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, (sheetName + "_第" + sheetCount + "页"));
                    rowNum = 1;
                }

                // 新建一行
                Row row = sheet.createRow(rowNum);
                // 循环数据
                for (int celLength = 0; celLength < titles.length; celLength++) {
                    AppUserPrizeOpportunityCustomize record = resultList.get(i);

                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);

                    // 序号
                    if (celLength == 0) {
                        cell.setCellValue(i + 1);
                    }
                    else if (celLength == 1) {
                        cell.setCellValue(record.getUsername());
                    }
                    else if (celLength == 2) {
                        cell.setCellValue(record.getTruename());
                    }
                    else if (celLength == 3) {
                        cell.setCellValue(String.valueOf(record.getMobile()==null?"":record.getMobile()));
                    }
                    else if (celLength == 4) {
                        cell.setCellValue(String.valueOf(record.getReferrerUserName()));
                    }
                    else if (celLength == 5) {
                        cell.setCellValue(String.valueOf(record.getInvestTotal()));
                    }
                    else if (celLength == 6) {
                        cell.setCellValue(String.valueOf(record.getPrizeCountRemain()));
                    }
                    else if (celLength == 7) {
                        cell.setCellValue(String.valueOf(record.getRegTimeFormat()));
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
        LogUtil.endLog(PrizeCodeListController.class.toString(), PrizeCodeListDefine.EXPORT_ACTION);
    }
	
}
