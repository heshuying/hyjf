package com.hyjf.admin.manager.activity.doubleSection;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.ActivityMidauInfo;
import com.hyjf.mybatis.model.customize.admin.DoubleSectionActivityCustomize;
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
 * @Auther: walter.limeng
 * @Date: 2018/9/11 09:50
 * @Description: DoubleSectionController
 */
@Controller
@RequestMapping(value = DoubleSectionActivityDefine.REQUEST_MAPPING)
public class DoubleSectionController extends BaseController {

    @Autowired
    private DoubleSectionService doubleSectionService;

    /**
     * @Author walter.limeng
     * @Description  单笔出借明细
     * @Date 9:54 2018/9/11
     * @Param
     * @return
     */
    @RequestMapping(DoubleSectionActivityDefine.INIT)
//    @RequiresPermissions(DoubleSectionActivityDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, DoubleSectionActivityBean form) {
        LogUtil.startLog(DoubleSectionActivityDefine.class.toString(), DoubleSectionActivityDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(DoubleSectionActivityDefine.LIST_PATH);
        // 创建分页
        if(form.getSort() == null){
            form.setSort("DESC");
        }
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(DoubleSectionActivityDefine.class.toString(), DoubleSectionActivityDefine.INIT);
        return modelAndView;
    }

    /**
     * 创建分页机能
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, DoubleSectionActivityBean form) {
        Map<String, Object> paraMap =beanToMap(form);
        Integer count = this.doubleSectionService.selectDouSectionActivityCount(paraMap);
        if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
            paraMap.put("limitStart", paginator.getOffset());
            paraMap.put("limitEnd", paginator.getLimit());
            List<DoubleSectionActivityCustomize> recordList = this.doubleSectionService.selectDouSectionActivityList(paraMap);
            form.setPaginator(paginator);
            form.setRecordList(recordList);
        }
        modelAndView.addObject(DoubleSectionActivityDefine.PRIZECODE_FORM, form);
    }

    /**
     * @Author walter.limeng
     * @Description  奖励明细页面初始化
     * @Date 11:36 2018/9/11
     * @Param DoubleSectionActivityBean
     * @return
     */
    @RequestMapping(DoubleSectionActivityDefine.AWARD_INIT)
//    @RequiresPermissions(DoubleSectionActivityDefine.PERMISSIONS_VIEW)
    public ModelAndView awardInit(HttpServletRequest request, DoubleSectionActivityBean form) {
        LogUtil.startLog(DoubleSectionActivityDefine.class.toString(), DoubleSectionActivityDefine.AWARD_INIT);
        ModelAndView modelAndView = new ModelAndView(DoubleSectionActivityDefine.AWARD_LIST_PATH);
        // 创建分页
        if(form.getSort() == null){
            form.setSort("DESC");
        }
        this.createAwardPage(request, modelAndView, form);
        LogUtil.endLog(DoubleSectionActivityDefine.class.toString(), DoubleSectionActivityDefine.AWARD_INIT);
        return modelAndView;
    }

    /**
     * 创建分页机能
     *
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createAwardPage(HttpServletRequest request, ModelAndView modelAndView, DoubleSectionActivityBean form) {
        Map<String, Object> paraMap =beanToMap(form);
//        paraMap.put("sortTwo", form.getSortTwo());
        Integer count = this.doubleSectionService.selectSectionActivityAwardCount(paraMap);
        if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
            paraMap.put("limitStart", paginator.getOffset());
            paraMap.put("limitEnd", paginator.getLimit());

            List<DoubleSectionActivityCustomize> recordList = this.doubleSectionService.selectSectionActivityAwardList(paraMap);
            form.setPaginator(paginator);
            form.setRecordList(recordList);
        }
        modelAndView.addObject(DoubleSectionActivityDefine.PRIZECODE_FORM, form);
    }

    @RequestMapping(DoubleSectionActivityDefine.INFO_ACTION)
//    @RequiresPermissions(QixiActivityDefine.PERMISSIONS_INFO)
    public ModelAndView info(HttpServletRequest request, String ids) {
        LogUtil.startLog(DoubleSectionController.class.toString(), DoubleSectionActivityDefine.INFO_ACTION);
        ModelAndView modelAndView = new ModelAndView(DoubleSectionActivityDefine.AWARD_INFO_PATH);
        if(StringUtils.isNotBlank(ids)){
            DoubleSectionActivityCustomize doubleSectionActivityCustomize = new DoubleSectionActivityCustomize();
            doubleSectionActivityCustomize.setId(Integer.valueOf(ids));
            doubleSectionActivityCustomize.setRewardStatus("0");
            modelAndView.addObject(DoubleSectionActivityDefine.PRIZECODE_FORM, doubleSectionActivityCustomize);
        }
        LogUtil.endLog(DoubleSectionController.class.toString(), DoubleSectionActivityDefine.INFO_ACTION);
        return modelAndView;
    }

    /**
     *  奖励明细 修改状态
     * @param request
     * @return
     */
    @RequestMapping(DoubleSectionActivityDefine.UPDATE_ACTION)
//    @RequiresPermissions(QixiActivityDefine.PERMISSIONS_INFO)
    public ModelAndView update(HttpServletRequest request, ActivityMidauInfo from) {
        LogUtil.startLog(DoubleSectionController.class.toString(), DoubleSectionActivityDefine.UPDATE_ACTION);
        ModelAndView modelAndView = new ModelAndView(DoubleSectionActivityDefine.RE_LIST_PATH);
        if(from.getId() != null){
            from.setUpdateTime(new Date());
            doubleSectionService.updateSectionActivityAward(from);
        }
        LogUtil.endLog(DoubleSectionController.class.toString(), DoubleSectionActivityDefine.UPDATE_ACTION);
        return modelAndView;
    }

    /**
     * 单笔出借导出
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(DoubleSectionActivityDefine.EXPORT_EXCEL_ACTION)
    @RequiresPermissions(DoubleSectionActivityDefine.PERMISSIONS_EXPORT)
    public void exportExcel(HttpServletRequest request, HttpServletResponse response, DoubleSectionActivityBean form) throws Exception {
        // 表格sheet名称
        String sheetName = "单笔出借导出";
        // 取得数据
        List<DoubleSectionActivityCustomize> recordList = createExcelPage(request, form);
        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
        String[] titles = new String[]{ "账户名", "姓名", "手机号","出借订单号", "单笔出借金额（元）", "产品类型", "产品编号","产品期限","奖励名称","出借时间"};
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
                    DoubleSectionActivityCustomize bean = recordList.get(i);
                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);
                    // 账户名
                    if (celLength == 0) {
                        cell.setCellValue(bean.getUserName() == null?"":bean.getUserName());
                    }
                    // 姓名
                    else if (celLength == 1) {
                        cell.setCellValue(bean.getTrueName() == null?"" : bean.getTrueName());
                    }
                    //手机号
                    else if (celLength == 2) {
                        cell.setCellValue(bean.getMobile()==null?"" : bean.getMobile());
                    }
                    //出借订单号
                    else if (celLength == 3) {
                        cell.setCellValue(bean.getOrderId()==null?"" : bean.getOrderId());
                    }
                    //  "单笔出借金额（元）", "产品类型", "产品编号","出借时间"
                    else if (celLength == 4) {
                        cell.setCellValue(bean.getInvestMoney() == null? "0.00": bean.getInvestMoney().toString());
                    }
                    //"产品类型",
                    else if (celLength == 5) {
                        cell.setCellValue(bean.getProductType() == null? "" : bean.getProductType());
                    }
                    //"产品编号",
                    else if (celLength == 6) {
                        cell.setCellValue(bean.getBorrowNid() == null? "" : bean.getBorrowNid());
                    }
                    // "产品期限"
                    else if (celLength == 7) {
                        cell.setCellValue(bean.getProductStyle() == null? "" : bean.getProductStyle());
                    }
                    // "奖励名称"
                    else if (celLength == 8) {
                        cell.setCellValue(bean.getRewardName() == null? "" : bean.getRewardName());
                    }
                    // "出借时间"
                    else if (celLength == 9) {
                        cell.setCellValue(bean.getInvestTime() == null? "" : bean.getInvestTime().substring(0,19));
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
    private List<DoubleSectionActivityCustomize> createExcelPage(HttpServletRequest request,  DoubleSectionActivityBean form) {
        Map<String, Object> paraMap =beanToMap(form);
        List<DoubleSectionActivityCustomize> recordList=null;
        Integer count = this.doubleSectionService.selectDouSectionActivityCount(paraMap);
        if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
            paraMap.put("limitStart", paginator.getOffset());
            paraMap.put("limitEnd", paginator.getLimit());
            recordList = this.doubleSectionService.selectDouSectionActivityList(paraMap);
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
    @RequestMapping(DoubleSectionActivityDefine.EXPORT_EXCEL_AWARD_ACTION)
    @RequiresPermissions(DoubleSectionActivityDefine.PERMISSIONS_EXPORT)
    public void exportAwardExcel(HttpServletRequest request, HttpServletResponse response,DoubleSectionActivityBean form) throws Exception {
        // 表格sheet名称
        String sheetName = "奖励明细导出";
        // 取得数据
        List<DoubleSectionActivityCustomize> recordList = createAwardExcelPage(request, form);
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
                    DoubleSectionActivityCustomize bean = recordList.get(i);
                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);
                    // 奖励名称
                    if (celLength == 0) {
                        cell.setCellValue(bean.getRewardName() == null?"":bean.getRewardName());
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
                            if("0".equals(bean.getDistributionStatus())){
                                cell.setCellValue("系统发放");
                            }else{
                                cell.setCellValue("手动发放");
                            }
                        }
                    }
                    // 账户名
                    if (celLength == 4) {
                        cell.setCellValue(bean.getUserName() == null?"":bean.getUserName());
                    }
                    // 姓名
                    else if (celLength == 5) {
                        cell.setCellValue(bean.getTrueName() == null?"" : bean.getTrueName());
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
                            if("0".equals(bean.getRewardStatus())){
                                cell.setCellValue("待发放");
                            }else{
                                cell.setCellValue("已发放");
                            }
                        }
                    }
                    //  "获得时间",
                    else if (celLength == 8) {
                        cell.setCellValue(bean.getCreateTime() == null? "": bean.getCreateTime().substring(0,19));
                    }
                    //""发放时间"
                    else if (celLength == 9) {
                        if(null != bean.getRewardStatus() && "1".equals(bean.getRewardStatus())){
                            cell.setCellValue(bean.getUpdateTime() == null? "": bean.getUpdateTime().substring(0,19));
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
    private  List<DoubleSectionActivityCustomize> createAwardExcelPage(HttpServletRequest request,DoubleSectionActivityBean form) {
        List<DoubleSectionActivityCustomize> recordList = null;
        Map<String, Object> paraMap =beanToMap(form);
        Integer count = this.doubleSectionService.selectSectionActivityAwardCount(paraMap);
        if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
            paraMap.put("limitStart", paginator.getOffset());
            paraMap.put("limitEnd", paginator.getLimit());
            recordList = this.doubleSectionService.selectSectionActivityAwardList(paraMap);
        }
        return recordList;
    }

    /**
     * @Author walter.limeng
     * @Description  封装参数到Map
     * @Date 10:34 2018/9/11
     * @Param DoubleSectionActivityBean
     * @return
     */
    public   Map<String, Object> beanToMap(DoubleSectionActivityBean form){
        Map<String, Object> paraMap = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(form.getUserName())){
            paraMap.put("username", form.getUserName().trim());
        }
        if(StringUtils.isNotBlank(form.getTrueName())){
            paraMap.put("truename", form.getTrueName().trim());
        }
        if(StringUtils.isNotBlank(form.getMobile())){
            paraMap.put("mobile", form.getMobile().trim());
        }
        if(StringUtils.isNotBlank(form.getProductType())){
            paraMap.put("projectType", form.getProductType().trim());
        }
        if(StringUtils.isNotBlank(form.getDistributionStatus())){
            paraMap.put("distributionStatus", form.getDistributionStatus().trim());
        }
        if(StringUtils.isNotBlank(form.getRewardStatus())){
            paraMap.put("rewardStatus", form.getRewardStatus().trim());
        }
        if(StringUtils.isNotBlank(form.getRewardId())){
            paraMap.put("rewardId", form.getRewardId().trim());
        }
        if(StringUtils.isNotBlank(form.getRewardType())){
            paraMap.put("rewardType", form.getRewardType().trim());
        }
        paraMap.put("sortTwo", form.getSortTwo());
        paraMap.put("colTwo", form.getColTwo());
        paraMap.put("sort", form.getSort());
        paraMap.put("col", form.getCol());
        return paraMap;
    }
}
