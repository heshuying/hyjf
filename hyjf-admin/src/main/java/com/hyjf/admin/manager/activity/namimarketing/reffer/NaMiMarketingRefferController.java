package com.hyjf.admin.manager.activity.namimarketing.reffer;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.activity.namimarketing.NaMiMarketingBean;
import com.hyjf.admin.manager.activity.namimarketing.NaMiMarketingDefine;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.customize.NaMiMarketing.NaMiMarketingCustomize;
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
import java.math.BigDecimal;
import java.util.*;

/**
 * @author xiehuili on 2018/11/8.
 */
@Controller
@RequestMapping(value = NaMiMarketingDefine.REQUEST_MAPPING)
public class NaMiMarketingRefferController extends BaseController {


    @Autowired
    private NaMiMarketingRefferService naMiMarketingService;

    /**
     * 邀请人返现明细 画面初始化
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(NaMiMarketingDefine.REFFER_DETAIL_INIT)
    @RequiresPermissions(NaMiMarketingDefine.PERMISSIONS_VIEW)
    public ModelAndView refferInit(HttpServletRequest request, NaMiMarketingBean form) {
        LogUtil.startLog(NaMiMarketingRefferController.class.toString(), NaMiMarketingDefine.REFFER_DETAIL_INIT);
        ModelAndView modelAndView = new ModelAndView(NaMiMarketingDefine.REFFER_DETAIL_LIST_PATH);
        String joinTimeStart =request.getParameter("joinTimeStart");
        String joinTimeEnd =request.getParameter("joinTimeEnd");
        if(StringUtils.isNotBlank(joinTimeStart)){
            form.setJoinTimeStart(joinTimeStart);
        }
        if(StringUtils.isNotBlank(joinTimeEnd)){
            form.setJoinTimeEnd(joinTimeEnd);
        }
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(NaMiMarketingRefferController.class.toString(), NaMiMarketingDefine.REFFER_DETAIL_INIT);
        return modelAndView;
    }
    /**
     * 创建分页机能
     * @param request
     * @param modelAndView
     * @param form
     */
    private List<NaMiMarketingCustomize> createPage(HttpServletRequest request, ModelAndView modelAndView, NaMiMarketingBean form) {
        Map<String, Object> paraMap  = beanToMap(form);
        List<NaMiMarketingCustomize> recordList = null;
        int count = this.naMiMarketingService.selectNaMiMarketingRefferCount(paraMap);
        if (count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            paraMap.put("limitStart",paginator.getOffset());
            paraMap.put("limitEnd",paginator.getLimit());
            recordList = this.naMiMarketingService.selectNaMiMarketingRefferList(paraMap);
            form.setPaginator(paginator);
            modelAndView.addObject("recordList", recordList);
        }
        modelAndView.addObject(NaMiMarketingDefine.PRIZECODE_FORM, form);
        return recordList;
    }
    /**
     * 邀请人返现统计 画面初始化
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(NaMiMarketingDefine.REFFER_TOTAL_INIT)
    @RequiresPermissions(NaMiMarketingDefine.PERMISSIONS_VIEW)
    public ModelAndView refferTotalInit(HttpServletRequest request, NaMiMarketingBean form) {
        LogUtil.startLog(NaMiMarketingRefferController.class.toString(), NaMiMarketingDefine.REFFER_TOTAL_INIT);
        ModelAndView modelAndView = new ModelAndView(NaMiMarketingDefine.REFFER_TOTAL_LIST_PATH);
        // 创建分页
        this.refferTotalCreatePage(request, modelAndView, form);
        LogUtil.endLog(NaMiMarketingRefferController.class.toString(), NaMiMarketingDefine.REFFER_TOTAL_INIT);
        return modelAndView;
    }
    /**
     * 创建分页机能
     * @param request
     * @param modelAndView
     * @param form
     */
    private List<NaMiMarketingCustomize> refferTotalCreatePage(HttpServletRequest request, ModelAndView modelAndView, NaMiMarketingBean form) {
        //月份初始化
        List<String> monthList = initMonth();
        modelAndView.addObject("monthList", monthList);
        Map<String, Object> paraMap  = beanToMap(form);
        List<NaMiMarketingCustomize> recordList = null;
        int count = this.naMiMarketingService.selectNaMiMarketingRefferTotalCount(paraMap);
        BigDecimal totalAmount = new BigDecimal(0.00);
        if (count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            paraMap.put("limitStart",paginator.getOffset());
            paraMap.put("limitEnd",paginator.getLimit());
            recordList = this.naMiMarketingService.selectNaMiMarketingRefferTotalList(paraMap);
            form.setPaginator(paginator);
            modelAndView.addObject("recordList", recordList);
            //合计
            totalAmount = naMiMarketingService.selectNaMiMarketingRefferTotalAmount(paraMap);
        }
        modelAndView.addObject("totalAmount", totalAmount);
        modelAndView.addObject(NaMiMarketingDefine.PRIZECODE_FORM, form);
        return recordList;
    }



    /**
     * 邀请人返现明细 导出
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(NaMiMarketingDefine.EXPORT_REFFER_DETAIL_ACTION)
    @RequiresPermissions(NaMiMarketingDefine.PERMISSIONS_EXPORT)
    public void exportRefferDetailExcel(HttpServletRequest request, HttpServletResponse response, NaMiMarketingBean form) throws Exception {
        // 表格sheet名称
        String sheetName = "邀请人返现明细";
        // 取得数据
        List<NaMiMarketingCustomize> recordList = createPage(request, new ModelAndView(),form);
        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
        String[] titles = new String[]{ "序号","账户名", "姓名","单笔当月产生的业绩（元）","获得返现金额（元）", "投资人账户名","投资订单号","单笔投资金额（元）","投资期限","产品类型","产品编号","放款时间/加入时间"};
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
                    NaMiMarketingCustomize bean = recordList.get(i);
                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);
                    // 账户名
                    if (celLength == 0) {
                        cell.setCellValue(rowNum);
                    }
                    // 账户名
                    else if (celLength == 1) {
                        cell.setCellValue(bean.getUsername() == null?"":bean.getUsername());
                    }
                    // 姓名
                    else if (celLength == 2) {
                        cell.setCellValue(bean.getTruename() == null?"" : bean.getTruename());
                    }
                    //单笔当月产生的业绩
                    else if (celLength == 3) {
                        cell.setCellValue(bean.getReturnPerformance()==null? BigDecimal.ZERO+"" : bean.getReturnPerformance()+"");
                    }
                    //  获得返现金额
                    else if (celLength == 4) {
                        cell.setCellValue(bean.getReturnAmount() == null?  BigDecimal.ZERO+"": bean.getReturnAmount()+"");
                    }
                    //  投资人账户名
                    else if (celLength == 5) {
                        cell.setCellValue(bean.getRefferName() == null? "": bean.getRefferName()+"");
                    }
                    //  投资 订单号
                    else if (celLength == 6) {
                        cell.setCellValue(bean.getTenderNo() == null? "": bean.getTenderNo());
                    }
                    //  单笔投资金额
                    else if (celLength == 7) {
                        cell.setCellValue(bean.getTenderAmount() == null? BigDecimal.ZERO+"": bean.getTenderAmount()+"");
                    }
                    //  投资期限
                    else if (celLength == 8) {
                        cell.setCellValue(bean.getTerm() == null? "": bean.getTerm());
                    }
                    //  产品类型
                    else if (celLength == 9) {
                        cell.setCellValue(bean.getProductType() == null? "": bean.getProductType());
                    }
                    //  产品编号
                    else if (celLength == 10) {
                        cell.setCellValue(bean.getProductNo() == null? "": bean.getProductNo());
                    }
                    //  放款时间/加入时间
                    else if (celLength == 11) {
                        cell.setCellValue(bean.getRegTime() == null? "": bean.getRegTime());
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
    }


    /**
     * 邀请人返现统计 导出
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(NaMiMarketingDefine.EXPORT_REFFER_TOTAL_ACTION)
    @RequiresPermissions(NaMiMarketingDefine.PERMISSIONS_EXPORT)
    public void exportRefferTotalExcel(HttpServletRequest request, HttpServletResponse response, NaMiMarketingBean form) throws Exception {
        // 表格sheet名称
        String sheetName = "邀请人返现统计";
        // 取得数据
        List<NaMiMarketingCustomize> recordList = refferTotalCreatePage(request, new ModelAndView(),form);
        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
        String[] titles = new String[]{ "序号","月份","账户名", "姓名","返现合计（元）"};
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
                    NaMiMarketingCustomize bean = recordList.get(i);
                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);
                    // 账户名
                    if (celLength == 0) {
                        cell.setCellValue(rowNum);
                    }
                    // 月份
                    else if (celLength == 1) {
                        cell.setCellValue(bean.getRegTime() == null?"":bean.getRegTime());
                    }
                    // 账户名
                    else if (celLength == 2) {
                        cell.setCellValue(bean.getUsername() == null?"":bean.getUsername());
                    }
                    // 姓名
                    else if (celLength == 3) {
                        cell.setCellValue(bean.getTruename() == null?"" : bean.getTruename());
                    }
                    //返现合计
                    else if (celLength == 4) {
                        cell.setCellValue(bean.getReturnAmount() == null?  BigDecimal.ZERO+"": bean.getReturnAmount()+"");
                    }
//                    //  返现合计
//                    else if (celLength == 5) {
//                        cell.setCellValue(bean.getReturnAmount() == null?  BigDecimal.ZERO+"": bean.getReturnAmount()+"");
//                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
    }

    public List<String>  initMonth(){
        List monthList =new ArrayList<String>();
//        monthList.add("2018-05");
//        monthList.add("2018-06");
//        monthList.add("2018-07");
//        monthList.add("2018-08");
//        monthList.add("2018-09");
//        monthList.add("2018-10");
//        monthList.add("2018-11");
//        monthList.add("2018-12");
//        monthList.add("2019-01");
//        monthList.add("2019-02");
//        monthList.add("2019-03");
        monthList = this.naMiMarketingService.selectMonthList();
        return monthList;
    }

    public Map<String, Object> beanToMap(NaMiMarketingBean form){
        Map<String, Object> paraMap = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(form.getUsername())){
            paraMap.put("username", form.getUsername().trim());
        }
        if(StringUtils.isNotBlank(form.getTruename())){
            paraMap.put("truename", form.getTruename().trim());
        }
        if(StringUtils.isNotBlank(form.getRefferName())){
            paraMap.put("tenderName", form.getRefferName().trim());
        }
        if(StringUtils.isNotBlank(form.getCol())){
            paraMap.put("col", form.getCol().trim());
        }
        if(StringUtils.isNotBlank(form.getSort())){
            paraMap.put("sort", form.getSort().trim());
        }
        if(StringUtils.isNotBlank(form.getProductType())){
            paraMap.put("productType", form.getProductType().trim());
        }
        if(StringUtils.isNotBlank(form.getProductNo())){
            paraMap.put("productNo", form.getProductNo().trim());
        }
        if(StringUtils.isNotBlank(form.getJoinTimeStart())){
            paraMap.put("joinTimeStart", form.getJoinTimeStart().trim()+" 00:00:00");
        }
        if(StringUtils.isNotBlank(form.getJoinTimeEnd())){
            paraMap.put("joinTimeEnd", form.getJoinTimeEnd().trim()+" 23:59:59");
        }
        if(StringUtils.isNotBlank(form.getMonth())){
            form.setJoinTimeStart(form.getMonth()+"-01");
            form.setJoinTimeEnd(getLastMinuTime(form.getMonth()));
            paraMap.put("joinTimeStart", form.getMonth()+"-01 00:00:00");
            paraMap.put("joinTimeEnd", getLastMinuTime(form.getMonth())+" 23:59:59");
        }
        return paraMap;
    }

    public String getLastMinuTime(String oldTime){
        //String转 date
        Date d=GetDate.stringToDate2(oldTime+"-01");
        //最后一天日期
        Date lastT=GetDate.getLastDayOnMonth(d);
        return GetDate.formatDate(lastT,"yyyy-MM-dd");
    }

}
