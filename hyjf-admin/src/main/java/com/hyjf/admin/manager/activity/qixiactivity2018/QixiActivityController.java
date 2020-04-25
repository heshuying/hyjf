package com.hyjf.admin.manager.activity.qixiactivity2018;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.ActivityQixi;
import com.hyjf.mybatis.model.customize.admin.QixiActivityCustomize;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author by xiehuili on 2018/7/23.
 */
@Controller
@RequestMapping(value = QixiActivityDefine.REQUEST_MAPPING)
public class QixiActivityController extends BaseController {
    @Autowired
    private QixiActivityService qixiActivityService;
    private final static String startTime = "2018-08-10 00:00:00";
    private final static String endTime = "2018-08-24 23:59:59";
    /**
     * 单笔 出借明细 画面初始化
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(QixiActivityDefine.INIT)
    @RequiresPermissions(QixiActivityDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, QixiActivityBean form) {
        LogUtil.startLog(QixiActivityController.class.toString(), QixiActivityDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(QixiActivityDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(QixiActivityController.class.toString(), QixiActivityDefine.INIT);
        return modelAndView;
    }
    /**
     * 创建分页机能
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, QixiActivityBean form) {
        Map<String, Object> paraMap =beanToMap(form);
        Integer count = this.qixiActivityService.selectQixiActivityCount(paraMap);
        if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
            paraMap.put("limitStart", paginator.getOffset());
            paraMap.put("limitEnd", paginator.getLimit());
            List<QixiActivityCustomize> recordList = this.qixiActivityService.selectQixiActivityList(paraMap);
            form.setPaginator(paginator);
            form.setRecordList(recordList);
        }
        modelAndView.addObject(QixiActivityDefine.PRIZECODE_FORM, form);
    }
    /**
     * 累计出借 出借明细 画面初始化
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(QixiActivityDefine.TOTAL_INIT)
    @RequiresPermissions(QixiActivityDefine.PERMISSIONS_VIEW)
    public ModelAndView totalInit(HttpServletRequest request, QixiActivityBean form) {
        LogUtil.startLog(QixiActivityController.class.toString(), QixiActivityDefine.TOTAL_INIT);
        ModelAndView modelAndView = new ModelAndView(QixiActivityDefine.TOTAL_LIST_PATH);
        // 创建分页
        this.createTotalPage(request, modelAndView, form);
        LogUtil.endLog(QixiActivityController.class.toString(), QixiActivityDefine.TOTAL_INIT);
        return modelAndView;
    }
    /**
     * 创建分页机能
     *
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createTotalPage(HttpServletRequest request, ModelAndView modelAndView, QixiActivityBean form) {
        Map<String, Object> paraMap =beanToMap(form);
        Integer count = this.qixiActivityService.selectQixiActivityTotalCount(paraMap);
        if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
            paraMap.put("limitStart", paginator.getOffset());
            paraMap.put("limitEnd", paginator.getLimit());
            List<QixiActivityCustomize> recordList = this.qixiActivityService.selectQixiActivityTotalList(paraMap);
            form.setPaginator(paginator);
            form.setRecordList(recordList);
        }
        modelAndView.addObject(QixiActivityDefine.PRIZECODE_FORM, form);
    }
    /**
     *  奖励明细 画面初始化
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(QixiActivityDefine.AWARD_INIT)
    @RequiresPermissions(QixiActivityDefine.PERMISSIONS_VIEW)
    public ModelAndView awardInit(HttpServletRequest request, QixiActivityBean form) {
        LogUtil.startLog(QixiActivityController.class.toString(), QixiActivityDefine.AWARD_INIT);
        ModelAndView modelAndView = new ModelAndView(QixiActivityDefine.AWARD_LIST_PATH);
        // 创建分页
        this.createAwardPage(request, modelAndView, form);
        LogUtil.endLog(QixiActivityController.class.toString(), QixiActivityDefine.AWARD_INIT);
        return modelAndView;
    }
    /**
     * 创建分页机能
     *
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createAwardPage(HttpServletRequest request, ModelAndView modelAndView, QixiActivityBean form) {
        Map<String, Object> paraMap =beanToMap(form);
//        paraMap.put("sortTwo", form.getSortTwo());
        Integer count = this.qixiActivityService.selectQixiActivityAwardCount(paraMap);
        if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
            paraMap.put("limitStart", paginator.getOffset());
            paraMap.put("limitEnd", paginator.getLimit());
            List<QixiActivityCustomize> recordList = this.qixiActivityService.selectQixiActivityAwardList(paraMap);
            form.setPaginator(paginator);
            form.setRecordList(recordList);
        }
        modelAndView.addObject(QixiActivityDefine.PRIZECODE_FORM, form);
    }

    /**
     *  奖励明细 画面初始化
     * @param request
     * @param ids
     * @return
     */
    @RequestMapping(QixiActivityDefine.INFO_ACTION)
//    @RequiresPermissions(QixiActivityDefine.PERMISSIONS_INFO)
    public ModelAndView info(HttpServletRequest request, String ids) {
        LogUtil.startLog(QixiActivityController.class.toString(), QixiActivityDefine.INFO_ACTION);
        ModelAndView modelAndView = new ModelAndView(QixiActivityDefine.AWARD_INFO_PATH);
        if(StringUtils.isNotBlank(ids)){
            QixiActivityCustomize qixiActivityCustomize = new QixiActivityCustomize();
            qixiActivityCustomize.setId(Integer.valueOf(ids));
            qixiActivityCustomize.setRewardStatus(0);
            modelAndView.addObject(QixiActivityDefine.PRIZECODE_FORM, qixiActivityCustomize);
        }
        LogUtil.endLog(QixiActivityController.class.toString(), QixiActivityDefine.INFO_ACTION);
        return modelAndView;
    }

    /**
     *  奖励明细 修改状态
     * @param request
     * @return
     */
    @RequestMapping(QixiActivityDefine.UPDATE_ACTION)
//    @RequiresPermissions(QixiActivityDefine.PERMISSIONS_INFO)
    public ModelAndView update(HttpServletRequest request, ActivityQixi from) {
        LogUtil.startLog(QixiActivityController.class.toString(), QixiActivityDefine.UPDATE_ACTION);
        ModelAndView modelAndView = new ModelAndView(QixiActivityDefine.RE_LIST_PATH);
        if(from.getId() != null){
            qixiActivityService.updateQixiActivityAward(from);
        }
        LogUtil.endLog(QixiActivityController.class.toString(), QixiActivityDefine.UPDATE_ACTION);
        return modelAndView;
    }
    /**
     * 单笔出借导出
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(QixiActivityDefine.EXPORT_EXCEL_ACTION)
    @RequiresPermissions(QixiActivityDefine.PERMISSIONS_EXPORT)
    public void exportExcel(HttpServletRequest request, HttpServletResponse response,QixiActivityBean form) throws Exception {
        // 表格sheet名称
        String sheetName = "单笔出借导出";
        // 取得数据
        List<QixiActivityCustomize> recordList = createExcelPage(request, form);
        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
        String[] titles = new String[]{ "账户名", "姓名", "手机号", "单笔出借金额（元）", "产品类型", "产品编号","出借时间"};
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
                    sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, (sheetName + "_第" + sheetCount + "页"));
                    rowNum = 1;
                }

                // 新建一行
                Row row = sheet.createRow(rowNum);
                // 循环数据
                for (int celLength = 0; celLength < titles.length; celLength++) {
                    QixiActivityCustomize bean = recordList.get(i);
                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);
                    // 账户名
                    if (celLength == 0) {
                        cell.setCellValue(bean.getUserName() == null?"":bean.getUserName());
                    }
                    // 姓名
                    else if (celLength == 1) {
                        cell.setCellValue(bean.getRealName() == null?"" : bean.getRealName());
                    }
                    //手机号
                    else if (celLength == 2) {
                        cell.setCellValue(bean.getMobile()==null?"" : bean.getMobile());
                    }
                    //  "单笔出借金额（元）", "产品类型", "产品编号","出借时间"
                    else if (celLength == 3) {
                        cell.setCellValue(bean.getSingleMoney() == null? "0.00": bean.getSingleMoney().toString());
                    }
                    //"产品类型", "产品编号","出借时间"
                    else if (celLength == 4) {
                        cell.setCellValue(bean.getBorrowType() == null? "汇直投" : "汇计划");
                    }
                    //"产品编号",
                    else if (celLength == 5) {
                        cell.setCellValue(bean.getBorrowNid() == null? "" : bean.getBorrowNid());
                    }
                    // "出借时间"
                    else if (celLength == 6) {
                        cell.setCellValue(bean.getInvestTime() == null? "" : bean.getInvestTime());
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
    }

    /**
     * 单笔出借导出
     *
     * @param request
     * @param form
     */
    private List<QixiActivityCustomize> createExcelPage(HttpServletRequest request,  QixiActivityBean form) {
        Map<String, Object> paraMap =beanToMap(form);
        List<QixiActivityCustomize> recordList=null;
        Integer count = this.qixiActivityService.selectQixiActivityCount(paraMap);
        if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
            paraMap.put("limitStart", paginator.getOffset());
            paraMap.put("limitEnd", paginator.getLimit());
            recordList = this.qixiActivityService.selectQixiActivityList(paraMap);
        }
        return recordList;
    }
    /**
     * 累计出借导出
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(QixiActivityDefine.EXPORT_EXCEL_TOTAL_ACTION)
    @RequiresPermissions(QixiActivityDefine.PERMISSIONS_EXPORT)
    public void exportTotalExcel(HttpServletRequest request, HttpServletResponse response,QixiActivityBean form) throws Exception {
        // 表格sheet名称
        String sheetName = "累计出借导出";
        // 取得数据
        List<QixiActivityCustomize> recordList = createTotalExcelPage(request, form);
        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
        String[] titles = new String[]{ "账户名", "姓名", "手机号", "累计出借金额（元）","奖励名称","获得时间"};
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
                    sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, (sheetName + "_第" + sheetCount + "页"));
                    rowNum = 1;
                }

                // 新建一行
                Row row = sheet.createRow(rowNum);
                // 循环数据
                for (int celLength = 0; celLength < titles.length; celLength++) {
                    QixiActivityCustomize bean = recordList.get(i);
                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);
                    // 账户名
                    if (celLength == 0) {
                        cell.setCellValue(bean.getUserName() == null?"":bean.getUserName());
                    }
                    // 姓名
                    else if (celLength == 1) {
                        cell.setCellValue(bean.getRealName() == null?"" : bean.getRealName());
                    }
                    //手机号
                    else if (celLength == 2) {
                        cell.setCellValue(bean.getMobile()==null?"" : bean.getMobile());
                    }
                    //  "累计出借金额（元）",
                    else if (celLength == 3) {
                        cell.setCellValue(bean.getTotalMoney() == null? "0.00": bean.getTotalMoney().toString());
                    }
                    //"奖励名称",
                    else if (celLength == 4) {
                        cell.setCellValue(bean.getAwardName() == null? "" : bean.getAwardName() );
                    }
                    //"获得时间",
                    else if (celLength == 5) {
                        if(StringUtils.isNotBlank(bean.getAwardName())){
                            cell.setCellValue(bean.getAwardTime() == null? "": bean.getAwardTime().substring(0,19));
                        }else{
                            cell.setCellValue("");
                        }
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
    }

    /**
     * 累计出借导出分页机能
     *
     * @param request
     * @param form
     */
    private List<QixiActivityCustomize> createTotalExcelPage(HttpServletRequest request, QixiActivityBean form) {
        List<QixiActivityCustomize> recordList =null;
        Map<String, Object> paraMap =beanToMap(form);
        Integer count = this.qixiActivityService.selectQixiActivityTotalCount(paraMap);
        if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
            paraMap.put("limitStart", paginator.getOffset());
            paraMap.put("limitEnd", paginator.getLimit());
            recordList = this.qixiActivityService.selectQixiActivityTotalList(paraMap);
        }
        return recordList;
    }

    /**
     * 奖励明细导出
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(QixiActivityDefine.EXPORT_EXCEL_AWARD_ACTION)
    @RequiresPermissions(QixiActivityDefine.PERMISSIONS_EXPORT)
    public void exportAwardExcel(HttpServletRequest request, HttpServletResponse response,QixiActivityBean form) throws Exception {
        // 表格sheet名称
        String sheetName = "奖励明细导出";
        // 取得数据
        List<QixiActivityCustomize> recordList = createAwardExcelPage(request, form);
        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
        String[] titles = new String[]{ "奖励名称", "奖励类型", "奖励批号", "发放方式","账户名", "姓名", "手机号", "状态", "获得时间","发放时间"};
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
                    sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, (sheetName + "_第" + sheetCount + "页"));
                    rowNum = 1;
                }

                // 新建一行
                Row row = sheet.createRow(rowNum);
                // 循环数据
                for (int celLength = 0; celLength < titles.length; celLength++) {
                    QixiActivityCustomize bean = recordList.get(i);
                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);
                    // 奖励名称
                    if (celLength == 0) {
                        cell.setCellValue(bean.getAwardName() == null?"":bean.getAwardName());
                    }
                    //奖励类型
                    else if (celLength == 1) {
                        cell.setCellValue(bean.getRewardType() == null?"" : bean.getRewardType());
                    }
                    //奖励批号
                    else if (celLength == 2) {
                        cell.setCellValue(bean.getRewardId()==null?"" : bean.getRewardId());
                    }
                    //  发放方式
                    else if (celLength == 3) {
                        if(bean.getDistributionStatus() == null){
                            cell.setCellValue("");
                        }else {
                            cell.setCellValue(bean.getDistributionStatus() == 0? "系统发放": "手动发放");
                        }
                    }
                    // 账户名
                    if (celLength == 4) {
                        cell.setCellValue(bean.getUserName() == null?"":bean.getUserName());
                    }
                    // 姓名
                    else if (celLength == 5) {
                        cell.setCellValue(bean.getRealName() == null?"" : bean.getRealName());
                    }
                    //手机号
                    else if (celLength == 6) {
                        cell.setCellValue(bean.getMobile()==null?"" : bean.getMobile());
                    }
                    // "状态", "获得时间","发放时间"
                    else if (celLength == 7) {
                        if(bean.getRewardStatus() == null){
                            cell.setCellValue("");
                        }else {
                            cell.setCellValue(bean.getRewardStatus() == 0? "待发放": "已发放");
                        }
                    }
                    //  "获得时间",
                    else if (celLength == 8) {
                        cell.setCellValue(bean.getAwardTime() == null? "": bean.getAwardTime().substring(0,19));
                    }
                    //""发放时间"
                    else if (celLength == 9) {
                        if(bean.getRewardStatus() == 1){
                            cell.setCellValue(bean.getDistributionTime() == null? "": bean.getDistributionTime().substring(0,19));
                        }else{
                            //待发放状态，没有获得时间
                            cell.setCellValue("");
                        }
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
    }
    /**
     * 奖励明细导出
     *
     * @param request
     * @param form
     */
    private  List<QixiActivityCustomize> createAwardExcelPage(HttpServletRequest request,QixiActivityBean form) {
        List<QixiActivityCustomize> recordList = null;
        Map<String, Object> paraMap =beanToMap(form);
//        paraMap.put("sortTwo", form.getSortTwo());
        Integer count = this.qixiActivityService.selectQixiActivityAwardCount(paraMap);
        if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
            paraMap.put("limitStart", paginator.getOffset());
            paraMap.put("limitEnd", paginator.getLimit());
            recordList = this.qixiActivityService.selectQixiActivityAwardList(paraMap);
        }
       return recordList;
    }

    public   Map<String, Object> beanToMap(QixiActivityBean form){
        Map<String, Object> paraMap = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(form.getUsername())){
            paraMap.put("username", form.getUsername().trim());
        }
        if(StringUtils.isNotBlank(form.getTruename())){
            paraMap.put("truename", form.getTruename().trim());
        }
        if(StringUtils.isNotBlank(form.getMobile())){
            paraMap.put("mobile", form.getMobile().trim());
        }
        if(StringUtils.isNotBlank(form.getType())){
            paraMap.put("type", form.getType().trim());
        }
        if(StringUtils.isNotBlank(form.getAwardType())){
            paraMap.put("awardType", form.getAwardType().trim());
        }
        if(StringUtils.isNotBlank(form.getAwardNum())){
            paraMap.put("awardId", form.getAwardNum().trim());
        }
        if(form.getGrandWay() != null){
            paraMap.put("grandWay", form.getGrandWay());
        }
        if(form.getStatus() != null){
            paraMap.put("status", form.getStatus());
        }
        paraMap.put("startTime", QixiActivityController.startTime);
        paraMap.put("endTime", QixiActivityController.endTime);
        paraMap.put("sort", form.getSort());
        paraMap.put("col", form.getCol());
        return paraMap;
    }


}
