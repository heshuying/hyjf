package com.hyjf.admin.manager.activity.actoct2017.acttender;

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
import com.hyjf.mybatis.model.customize.admin.act.ActTen2017Customize;

/**
 * 
 * 十月份活动出借返现
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年7月12日
 * @see 下午4:58:59
 */
@Controller
@RequestMapping(value = TenderRewardActivityDefine.REQUEST_MAPPING)
public class TenderRewardActivityController extends BaseController {

	@Autowired
	private TenderRewardActivityService tenderRewardActivityService;

	 /**
     * 画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(TenderRewardActivityDefine.INIT)
    @RequiresPermissions(TenderRewardActivityDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, TenderRewardActivityBean form) {
        LogUtil.startLog(TenderRewardActivityController.class.toString(), TenderRewardActivityDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(TenderRewardActivityDefine.LIST_PATH);

        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(TenderRewardActivityController.class.toString(), TenderRewardActivityDefine.INIT);
        return modelAndView;
    }

    /**
     * 画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(TenderRewardActivityDefine.SEARCH_ACTION)
    @RequiresPermissions(TenderRewardActivityDefine.PERMISSIONS_SEARCH)
    public ModelAndView search(HttpServletRequest request, TenderRewardActivityBean form) {
        LogUtil.startLog(TenderRewardActivityController.class.toString(), TenderRewardActivityDefine.SEARCH_ACTION);
        ModelAndView modelAndView = new ModelAndView(TenderRewardActivityDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(TenderRewardActivityController.class.toString(), TenderRewardActivityDefine.SEARCH_ACTION);
        return modelAndView;
    }

    /**
     * 创建分页机能
     * 
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, TenderRewardActivityBean form) {
    	String activityId = PropUtils.getSystem("hyjf.actten2017.id");
    	if(StringUtils.isEmpty(activityId)){
            LogUtil.errorLog(TenderRewardActivityController.class.toString(), "createPage", "2017十月份活动活动id没有配置", null);
            return;
    	}
    	ActivityList activity = tenderRewardActivityService.getActivityListById(Integer.parseInt(activityId));
        if(activity == null){
            LogUtil.errorLog(TenderRewardActivityController.class.toString(), "createPage", "2017十月份活动不存在，活动ID：" + activityId, null);
            return;
        }
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("startTime", activity.getTimeStart());
        paraMap.put("endTime", activity.getTimeEnd());
        
        if(StringUtils.isNotEmpty(form.getUsernameSrch())){
            paraMap.put("usernameSrch", form.getUsernameSrch());
        }
        if(StringUtils.isNotEmpty(form.getMobileSrch())){
            paraMap.put("mobileSrch", form.getMobileSrch());
        }
        if(StringUtils.isNotEmpty(form.getCouponNameSrch())){
            paraMap.put("couponNameSrch", form.getCouponNameSrch());
        }
        
        Integer count = this.tenderRewardActivityService.selectRecordCount(paraMap);
        if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
            paraMap.put("limitStart", paginator.getOffset());
            paraMap.put("limitEnd", paginator.getLimit());
            List<ActTen2017Customize> recordList = this.tenderRewardActivityService.selectRecordList(paraMap);
            form.setPaginator(paginator);
            
            modelAndView.addObject("recordList", recordList);
        }

        modelAndView.addObject(TenderRewardActivityDefine.ACTTEN_TENDER_REWARD_FORM, form);
    }

	 /**
     * 画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(TenderRewardActivityDefine.INIT_DETAIL)
    @RequiresPermissions(TenderRewardActivityDefine.PERMISSIONS_VIEW)
    public ModelAndView initDetail(HttpServletRequest request, TenderRewardActivityBean form) {
        LogUtil.startLog(TenderRewardActivityController.class.toString(), TenderRewardActivityDefine.INIT_DETAIL);
        ModelAndView modelAndView = new ModelAndView(TenderRewardActivityDefine.LIST_PATH_DETAIL);

        // 创建分页
        this.createPageDetail(request, modelAndView, form);
        LogUtil.endLog(TenderRewardActivityController.class.toString(), TenderRewardActivityDefine.INIT_DETAIL);
        return modelAndView;
    }

    /**
     * 画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(TenderRewardActivityDefine.SEARCH_ACTION_DETAIL)
    @RequiresPermissions(TenderRewardActivityDefine.PERMISSIONS_SEARCH)
    public ModelAndView searchDetail(HttpServletRequest request, TenderRewardActivityBean form) {
        LogUtil.startLog(TenderRewardActivityController.class.toString(), TenderRewardActivityDefine.SEARCH_ACTION_DETAIL);
        ModelAndView modelAndView = new ModelAndView(TenderRewardActivityDefine.LIST_PATH_DETAIL);
        // 创建分页
        this.createPageDetail(request, modelAndView, form);
        LogUtil.endLog(TenderRewardActivityController.class.toString(), TenderRewardActivityDefine.SEARCH_ACTION_DETAIL);
        return modelAndView;
    }

    /**
     * 创建分页机能
     * 
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPageDetail(HttpServletRequest request, ModelAndView modelAndView, TenderRewardActivityBean form) {

    	String activityId = PropUtils.getSystem("hyjf.actten2017.id");
    	if(StringUtils.isEmpty(activityId)){
            LogUtil.errorLog(TenderRewardActivityController.class.toString(), "createPageDetail", "2017十月份活动活动id没有配置", null);
            return;
    	}
    	ActivityList activity = tenderRewardActivityService.getActivityListById(Integer.parseInt(activityId));
        if(activity == null){
            LogUtil.errorLog(TenderRewardActivityController.class.toString(), "createPageDetail", "2017十月份活动不存在，活动ID：" + activityId, null);
            return;
        }
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("startTime", activity.getTimeStart());
        paraMap.put("endTime", activity.getTimeEnd());
        
    	if(form.getUserId() != null){
            paraMap.put("userId", form.getUserId());
        }
        if(StringUtils.isNotEmpty(form.getUsernameSrch())){
            paraMap.put("usernameSrch", form.getUsernameSrch());
        }
        if(StringUtils.isNotEmpty(form.getMobileSrch())){
            paraMap.put("mobileSrch", form.getMobileSrch());
        }
        
        Integer count = this.tenderRewardActivityService.selectTenderReturnDetailCount(paraMap);
        if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
            paraMap.put("limitStart", paginator.getOffset());
            paraMap.put("limitEnd", paginator.getLimit());
            List<ActTen2017Customize> recordList = this.tenderRewardActivityService.selectTenderReturnDetail(paraMap);
            form.setPaginator(paginator);
            
            modelAndView.addObject("recordList", recordList);
        }

        modelAndView.addObject(TenderRewardActivityDefine.ACTTEN_TENDER_REWARD_FORM, form);
    }
    
    @RequestMapping(TenderRewardActivityDefine.EXPORT_ACTION)
    @RequiresPermissions(TenderRewardActivityDefine.PERMISSIONS_EXPORT)
    public void exportAction(HttpServletRequest request,  HttpServletResponse response, TenderRewardActivityBean form) throws Exception {
        LogUtil.startLog(this.getClass().getName(), TenderRewardActivityDefine.EXPORT_ACTION);
        // 表格sheet名称
        String sheetName = "优惠券用户列表";
		
        String activityId = PropUtils.getSystem("hyjf.actten2017.id");
    	if(StringUtils.isEmpty(activityId)){
            LogUtil.errorLog(TenderRewardActivityController.class.toString(), "createPage", "2017十月份活动活动id没有配置", null);
            return;
    	}
    	ActivityList activity = tenderRewardActivityService.getActivityListById(Integer.parseInt(activityId));
        if(activity == null){
            LogUtil.errorLog(TenderRewardActivityController.class.toString(), "createPage", "2017十月份活动不存在，活动ID：" + activityId, null);
            return;
        }
        
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("startTime", activity.getTimeStart());
        paraMap.put("endTime", activity.getTimeEnd());
        
        if(StringUtils.isNotEmpty(form.getUsernameSrch())){
            paraMap.put("usernameSrch", form.getUsernameSrch());
        }
        if(StringUtils.isNotEmpty(form.getMobileSrch())){
            paraMap.put("mobileSrch", form.getMobileSrch());
        }
        if(StringUtils.isNotEmpty(form.getCouponNameSrch())){
            paraMap.put("couponNameSrch", form.getCouponNameSrch());
        }

        List<ActTen2017Customize> resultList = this.tenderRewardActivityService.selectRecordList(paraMap);
        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
        String[] titles = new String[] {"序号", "用户名", "姓名", "手机号", "最高单笔年化出借", "最后出借时间 ", "奖励"};
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
                	ActTen2017Customize actten = resultList.get(i);

                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);
                    // 序号
                    if (celLength == 0) {
                        cell.setCellValue(i + 1);
                    }
                    else if (celLength == 1) {
                        cell.setCellValue(actten.getUserName());
                    }
                    else if (celLength == 2) {
                        cell.setCellValue(actten.getTrueName());
                    }
                    else if (celLength == 3) {
                        cell.setCellValue(actten.getMobile());
                    }
                    else if (celLength == 4) {
                    	cell.setCellValue(actten.getTenderAccount());
                    }
                    else if(celLength == 5) {
                        cell.setCellValue(actten.getTenderTime());
                    }
                    else if (celLength == 6) {
                        cell.setCellValue(actten.getRewardName());
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);

        LogUtil.endLog(this.getClass().getName(), TenderRewardActivityDefine.EXPORT_ACTION);
    }


}
