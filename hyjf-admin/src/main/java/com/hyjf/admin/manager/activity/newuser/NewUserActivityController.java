package com.hyjf.admin.manager.activity.newuser;

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
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.admin.AdminNewUserActivityCustomize;

/**
 * 
 * 九月份运营新手活动
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年7月12日
 * @see 下午4:58:59
 */
@Controller
@RequestMapping(value = NewUserActivityDefine.REQUEST_MAPPING)
public class NewUserActivityController extends BaseController {

	@Autowired
	private NewUserActivityService newUserActivityService;

	 /**
     * 画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(NewUserActivityDefine.INIT)
    @RequiresPermissions(NewUserActivityDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, NewUserActivityBean form) {
        LogUtil.startLog(NewUserActivityController.class.toString(), NewUserActivityDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(NewUserActivityDefine.LIST_PATH);

        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(NewUserActivityController.class.toString(), NewUserActivityDefine.INIT);
        return modelAndView;
    }

    /**
     * 画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(NewUserActivityDefine.SEARCH_ACTION)
    @RequiresPermissions(NewUserActivityDefine.PERMISSIONS_SEARCH)
    public ModelAndView search(HttpServletRequest request, NewUserActivityBean form) {
        LogUtil.startLog(NewUserActivityController.class.toString(), NewUserActivityDefine.SEARCH_ACTION);
        ModelAndView modelAndView = new ModelAndView(NewUserActivityDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(NewUserActivityController.class.toString(), NewUserActivityDefine.SEARCH_ACTION);
        return modelAndView;
    }

    /**
     * 创建分页机能
     * 
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, NewUserActivityBean form) {

        Map<String, Object> paraMap = new HashMap<String, Object>();
        if(StringUtils.isNotEmpty(form.getUsernameSrch())){
            paraMap.put("username", form.getUsernameSrch());
        }
        if(StringUtils.isNotEmpty(form.getMobileSrch())){
            paraMap.put("mobile", form.getMobileSrch());
        }
        if(StringUtils.isNotEmpty(form.getRecommendNameSrch())){
            paraMap.put("recommendName", form.getRecommendNameSrch());
        }
        if(StringUtils.isNotEmpty(form.getActivitySrch())){
            paraMap.put("activity", form.getActivitySrch());
        }
        if(StringUtils.isNotEmpty(form.getSendStatusSrch())){
            paraMap.put("sendStatus", form.getSendStatusSrch());
        }
        if(StringUtils.isNotEmpty(form.getRegistPlatSrch())){
            paraMap.put("registPlat", form.getRegistPlatSrch());
        }
        
        //取得活动配置id
        String activityId = PropUtils.getSystem(CustomConstants.REGISTER_SEND_COUPON_ACTIVITY_ID);
        if(StringUtils.isEmpty(activityId)){
            LogUtil.errorLog(NewUserActivityController.class.toString(), "createOpportunityPage", "注册送代金券活动id没有配置", null);
        }
        ActivityList activity = newUserActivityService.getActivityListById(Integer.parseInt(activityId));
        if(activity == null){
            LogUtil.errorLog(NewUserActivityController.class.toString(), "createOpportunityPage", "注册送代金券活动不存在，活动ID：" + activityId, null);
        }
        
        String activityId2 = PropUtils.getSystem(CustomConstants.TENDER_SEND_COUPON_ACTIVITY_ID);
        if(StringUtils.isEmpty(activityId2)){
            LogUtil.errorLog(NewUserActivityController.class.toString(), "createOpportunityPage", "出借送加息券活动id没有配置", null);
        }
        ActivityList activity2 = newUserActivityService.getActivityListById(Integer.parseInt(activityId2));
        if(activity2 == null){
            LogUtil.errorLog(NewUserActivityController.class.toString(), "createOpportunityPage", "出借送加息券活动不存在，活动ID：" + activityId2, null);
        }
        
        paraMap.put("activityId1", activityId);
        paraMap.put("startTime1", activity.getTimeStart());
        paraMap.put("endTime1", activity.getTimeEnd());
        
        paraMap.put("activityId2", activityId2);
        paraMap.put("startTime2", activity2.getTimeStart());
        paraMap.put("endTime2", activity2.getTimeEnd());
        
        // 注册平台
        List<ParamName> registPlat = this.newUserActivityService.getParamNameList("CLIENT");
        modelAndView.addObject("registPlat", registPlat);
        
        Integer count = this.newUserActivityService.selectRecordCount(paraMap);
        if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
            paraMap.put("limitStart", paginator.getOffset());
            paraMap.put("limitEnd", paginator.getLimit());
            List<AdminNewUserActivityCustomize> recordList = this.newUserActivityService.selectRecordList(paraMap);
            form.setPaginator(paginator);
            
            modelAndView.addObject("recordList", recordList);
        }

        modelAndView.addObject(NewUserActivityDefine.NEWUSER_FORM, form);
    }

    /**
     * 画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(NewUserActivityDefine.INIT_REGISTALL)
    @RequiresPermissions(NewUserActivityDefine.PERMISSIONS_VIEW)
    public ModelAndView initRegistAll(HttpServletRequest request, NewUserActivityBean form) {
        LogUtil.startLog(NewUserActivityController.class.toString(), NewUserActivityDefine.INIT_REGISTALL);
        ModelAndView modelAndView = new ModelAndView(NewUserActivityDefine.LIST_PATH_REGISTALL);

        // 创建分页
        this.createPageRegistAll(request, modelAndView, form);
        LogUtil.endLog(NewUserActivityController.class.toString(), NewUserActivityDefine.INIT_REGISTALL);
        return modelAndView;
    }

    /**
     * 画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(NewUserActivityDefine.SEARCH_ACTION_REGISTALL)
    @RequiresPermissions(NewUserActivityDefine.PERMISSIONS_SEARCH)
    public ModelAndView searchRegistAll(HttpServletRequest request, NewUserActivityBean form) {
        LogUtil.startLog(NewUserActivityController.class.toString(), NewUserActivityDefine.SEARCH_ACTION_REGISTALL);
        ModelAndView modelAndView = new ModelAndView(NewUserActivityDefine.LIST_PATH_REGISTALL);
        // 创建分页
        this.createPageRegistAll(request, modelAndView, form);
        LogUtil.endLog(NewUserActivityController.class.toString(), NewUserActivityDefine.SEARCH_ACTION_REGISTALL);
        return modelAndView;
    }
    
    /**
     * 创建分页机能
     * 
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPageRegistAll(HttpServletRequest request, ModelAndView modelAndView, NewUserActivityBean form) {

        Map<String, Object> paraMap = new HashMap<String, Object>();
        if(StringUtils.isNotEmpty(form.getUsernameSrch())){
            paraMap.put("username", form.getUsernameSrch());
        }
        if(StringUtils.isNotEmpty(form.getMobileSrch())){
            paraMap.put("mobile", form.getMobileSrch());
        }
        if(StringUtils.isNotEmpty(form.getRecommendNameSrch())){
            paraMap.put("recommendName", form.getRecommendNameSrch());
        }
        if(StringUtils.isNotEmpty(form.getSendStatusSrch())){
            paraMap.put("sendStatus", form.getSendStatusSrch());
        }
        if(StringUtils.isNotEmpty(form.getRegistPlatSrch())){
            paraMap.put("registPlat", form.getRegistPlatSrch());
        }
        
        //取得活动配置id
        String activityId = PropUtils.getSystem(CustomConstants.REGISTER_ALL_SEND_COUPON_ACTIVITY_ID);
        if(StringUtils.isEmpty(activityId)){
            LogUtil.errorLog(NewUserActivityController.class.toString(), "createOpportunityPage", "注册送代金券活动id没有配置", null);
        }
        ActivityList activity = newUserActivityService.getActivityListById(Integer.parseInt(activityId));
        if(activity == null){
            LogUtil.errorLog(NewUserActivityController.class.toString(), "createOpportunityPage", "注册送代金券活动不存在，活动ID：" + activityId, null);
        }
        
        paraMap.put("activityId", activityId);
        paraMap.put("startTime", activity.getTimeStart());
        paraMap.put("endTime", activity.getTimeEnd());
        
        
        // 注册平台
        List<ParamName> registPlat = this.newUserActivityService.getParamNameList("CLIENT");
        modelAndView.addObject("registPlat", registPlat);
        
        Integer count = this.newUserActivityService.selectRegistAllCount(paraMap);
        if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
            paraMap.put("limitStart", paginator.getOffset());
            paraMap.put("limitEnd", paginator.getLimit());
            List<AdminNewUserActivityCustomize> recordList = this.newUserActivityService.selectRegistAllList(paraMap);
            form.setPaginator(paginator);
            
            modelAndView.addObject("recordList", recordList);
        }

        modelAndView.addObject(NewUserActivityDefine.NEWUSER_FORM, form);
    }

	/**
     * 数据导出
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(NewUserActivityDefine.EXPORT_ACTION)
    @RequiresPermissions(NewUserActivityDefine.PERMISSIONS_EXPORT)
    public void exportAction(HttpServletRequest request, HttpServletResponse response, NewUserActivityBean form) throws Exception {
        LogUtil.startLog(NewUserActivityController.class.toString(), NewUserActivityDefine.EXPORT_ACTION);
        // 表格sheet名称
        String sheetName = "九月新手活动";

        Map<String, Object> paraMap = new HashMap<String, Object>();
        if(StringUtils.isNotEmpty(form.getUsernameSrch())){
            paraMap.put("username", form.getUsernameSrch());
        }
        if(StringUtils.isNotEmpty(form.getMobileSrch())){
            paraMap.put("mobile", form.getMobileSrch());
        }
        if(StringUtils.isNotEmpty(form.getRecommendNameSrch())){
            paraMap.put("recommendName", form.getRecommendNameSrch());
        }
        if(StringUtils.isNotEmpty(form.getActivitySrch())){
            paraMap.put("activity", form.getActivitySrch());
        }
        if(StringUtils.isNotEmpty(form.getSendStatusSrch())){
            paraMap.put("sendStatus", form.getSendStatusSrch());
        }
        if(StringUtils.isNotEmpty(form.getRegistPlatSrch())){
            paraMap.put("registPlat", form.getRegistPlatSrch());
        }
        
        //取得活动配置id
        String activityId = PropUtils.getSystem(CustomConstants.REGISTER_SEND_COUPON_ACTIVITY_ID);
        if(StringUtils.isEmpty(activityId)){
            LogUtil.errorLog(NewUserActivityController.class.toString(), "createOpportunityPage", "注册送代金券活动id没有配置", null);
        }
        ActivityList activity = newUserActivityService.getActivityListById(Integer.parseInt(activityId));
        if(activity == null){
            LogUtil.errorLog(NewUserActivityController.class.toString(), "createOpportunityPage", "注册送代金券活动不存在，活动ID：" + activityId, null);
        }
        
        String activityId2 = PropUtils.getSystem(CustomConstants.TENDER_SEND_COUPON_ACTIVITY_ID);
        if(StringUtils.isEmpty(activityId2)){
            LogUtil.errorLog(NewUserActivityController.class.toString(), "createOpportunityPage", "出借送加息券活动id没有配置", null);
        }
        ActivityList activity2 = newUserActivityService.getActivityListById(Integer.parseInt(activityId2));
        if(activity2 == null){
            LogUtil.errorLog(NewUserActivityController.class.toString(), "createOpportunityPage", "出借送加息券活动不存在，活动ID：" + activityId2, null);
        }
        
        paraMap.put("activityId1", activityId);
        paraMap.put("startTime1", activity.getTimeStart());
        paraMap.put("endTime1", activity.getTimeEnd());
        
        paraMap.put("activityId2", activityId2);
        paraMap.put("startTime2", activity2.getTimeStart());
        paraMap.put("endTime2", activity2.getTimeEnd());
        
        List<AdminNewUserActivityCustomize> resultList = this.newUserActivityService.selectRecordList(paraMap);

        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

        String[] titles = new String[] { "序号", "用户名", "手机号", "推荐人", "渠道", "注册平台", "注册时间", "单笔出借", "出借时间", "活动奖励", "发放状态"};
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
                    AdminNewUserActivityCustomize record = resultList.get(i);

                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);

                    // 序号
                    if (celLength == 0) {
                        cell.setCellValue(i + 1);
                    }
                    else if (celLength == 1) {
                        cell.setCellValue(record.getUserName());
                    }
                    else if (celLength == 2) {
                        cell.setCellValue(record.getMobile()==null?"":record.getMobile());
                    }
                    else if (celLength == 3) {
                        cell.setCellValue(StringUtils.isEmpty(record.getRecommendName()) ? "" : record.getRecommendName());
                    }
                    else if (celLength == 4) {
                        cell.setCellValue(StringUtils.isEmpty(record.getSourceName()) ? "" : record.getSourceName());
                    }
                    else if (celLength == 5) {
                        cell.setCellValue(record.getRegistPlat());
                    }
                    else if (celLength == 6) {
                        cell.setCellValue(record.getRegTime());
                    }
                    else if (celLength == 7) {
                        cell.setCellValue(record.getAccount());
                    } 
                    else if (celLength == 8) {
                        cell.setCellValue(record.getInvestTime());
                    } 
                    else if (celLength == 9) {
                        cell.setCellValue(record.getActivity().equals("0") ? "68元代金券" : "1.5%加息券");
                    } 
                    else if (celLength == 10) {
                        cell.setCellValue(record.getCouponSendAlready());
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
        LogUtil.endLog(NewUserActivityController.class.toString(), NewUserActivityDefine.EXPORT_ACTION);
    }
    
    /**
     * 数据导出
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(NewUserActivityDefine.EXPORT_ALL_ACTION)
    @RequiresPermissions(NewUserActivityDefine.PERMISSIONS_EXPORT)
    public void exportAllAction(HttpServletRequest request, HttpServletResponse response, NewUserActivityBean form) throws Exception {
        LogUtil.startLog(NewUserActivityController.class.toString(), NewUserActivityDefine.EXPORT_ALL_ACTION);
        // 表格sheet名称
        String sheetName = "九月新手活动";

        Map<String, Object> paraMap = new HashMap<String, Object>();
        if(StringUtils.isNotEmpty(form.getUsernameSrch())){
            paraMap.put("username", form.getUsernameSrch());
        }
        if(StringUtils.isNotEmpty(form.getMobileSrch())){
            paraMap.put("mobile", form.getMobileSrch());
        }
        if(StringUtils.isNotEmpty(form.getRecommendNameSrch())){
            paraMap.put("recommendName", form.getRecommendNameSrch());
        }
        if(StringUtils.isNotEmpty(form.getSendStatusSrch())){
            paraMap.put("sendStatus", form.getSendStatusSrch());
        }
        if(StringUtils.isNotEmpty(form.getRegistPlatSrch())){
            paraMap.put("registPlat", form.getRegistPlatSrch());
        }
        
        //取得活动配置id
        String activityId = PropUtils.getSystem(CustomConstants.REGISTER_ALL_SEND_COUPON_ACTIVITY_ID);
        if(StringUtils.isEmpty(activityId)){
            LogUtil.errorLog(NewUserActivityController.class.toString(), "createOpportunityPage", "注册送代金券活动id没有配置", null);
        }
        ActivityList activity = newUserActivityService.getActivityListById(Integer.parseInt(activityId));
        if(activity == null){
            LogUtil.errorLog(NewUserActivityController.class.toString(), "createOpportunityPage", "注册送代金券活动不存在，活动ID：" + activityId, null);
        }
        
        paraMap.put("activityId", activityId);
        paraMap.put("startTime", activity.getTimeStart());
        paraMap.put("endTime", activity.getTimeEnd());
        
        List<AdminNewUserActivityCustomize> resultList = this.newUserActivityService.selectRegistAllList(paraMap);

        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

        String[] titles = new String[] { "序号", "用户名", "手机号", "推荐人", "渠道", "注册平台", "注册时间", "活动奖励", "发放状态"};
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
                    AdminNewUserActivityCustomize record = resultList.get(i);

                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);

                    // 序号
                    if (celLength == 0) {
                        cell.setCellValue(i + 1);
                    }
                    else if (celLength == 1) {
                        cell.setCellValue(record.getUserName());
                    }
                    else if (celLength == 2) {
                        cell.setCellValue(record.getMobile()==null?"":record.getMobile());
                    }
                    else if (celLength == 3) {
                        cell.setCellValue(StringUtils.isEmpty(record.getRecommendName()) ? "" : record.getRecommendName());
                    }
                    else if (celLength == 4) {
                        cell.setCellValue(StringUtils.isEmpty(record.getSourceName()) ? "" : record.getSourceName());
                    }
                    else if (celLength == 5) {
                        cell.setCellValue(record.getRegistPlat());
                    }
                    else if (celLength == 6) {
                        cell.setCellValue(record.getRegTime());
                    }
                    else if (celLength == 7) {
                        cell.setCellValue("68元代金券");
                    } 
                    else if (celLength == 8) {
                        cell.setCellValue(record.getCouponSendAlready());
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
        LogUtil.endLog(NewUserActivityController.class.toString(), NewUserActivityDefine.EXPORT_ALL_ACTION);
    }
    
}
