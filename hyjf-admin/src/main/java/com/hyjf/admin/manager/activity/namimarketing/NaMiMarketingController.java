package com.hyjf.admin.manager.activity.namimarketing;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.PerformanceReturnDetail;
import com.hyjf.mybatis.model.customize.NaMiMarketing.NaMiMarketingCustomize;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiehuili on 2018/11/8.
 */
@Controller
@RequestMapping(value = NaMiMarketingDefine.REQUEST_MAPPING)
public class NaMiMarketingController extends BaseController {


    @Autowired
    private NaMiMarketingService naMiMarketingService;
    /**
     * 邀请 明细 画面初始化
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(NaMiMarketingDefine.INIT)
    @RequiresPermissions(NaMiMarketingDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, NaMiMarketingBean form) {
        LogUtil.startLog(NaMiMarketingController.class.toString(), NaMiMarketingDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(NaMiMarketingDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(NaMiMarketingController.class.toString(), NaMiMarketingDefine.INIT);
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
        List<Integer> ids = this.naMiMarketingService.selectNaMiMarketingCount(paraMap);
        if (!CollectionUtils.isEmpty(ids)) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), ids.size());
            paraMap.put("limitStart",paginator.getOffset());
            paraMap.put("limitEnd",paginator.getLimit());
            paraMap.put("ids",ids);
            recordList = this.naMiMarketingService.selectNaMiMarketingList(paraMap);
            form.setPaginator(paginator);
            modelAndView.addObject("recordList", recordList);
        }
        modelAndView.addObject(NaMiMarketingDefine.PRIZECODE_FORM, form);
        return recordList;
    }

    /**
     * 业绩返现详情 画面初始化
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(NaMiMarketingDefine.PERFORMANCE_INIT)
    @RequiresPermissions(NaMiMarketingDefine.PERMISSIONS_VIEW)
    public ModelAndView performanceInit(HttpServletRequest request, NaMiMarketingBean form) {
        LogUtil.startLog(NaMiMarketingController.class.toString(), NaMiMarketingDefine.PERFORMANCE_INIT);
        String joinTimeStart =request.getParameter("joinTimeStart");
        String joinTimeEnd =request.getParameter("joinTimeEnd");
        if(StringUtils.isNotBlank(joinTimeStart)){
            form.setJoinTimeStart(joinTimeStart);
        }
        if(StringUtils.isNotBlank(joinTimeEnd)){
            form.setJoinTimeEnd(joinTimeEnd);
        }
        ModelAndView modelAndView = new ModelAndView(NaMiMarketingDefine.PERFORMANCE_LIST_PATH);
        // 创建分页
        this.performanceCreatePage(request, modelAndView, form);
        LogUtil.endLog(NaMiMarketingController.class.toString(), NaMiMarketingDefine.PERFORMANCE_INIT);
        return modelAndView;
    }
    /**
     * 创建分页机能
     * @param request
     * @param modelAndView
     * @param form
     */
    private List<NaMiMarketingCustomize> performanceCreatePage(HttpServletRequest request, ModelAndView modelAndView, NaMiMarketingBean form) {
        Map<String, Object> paraMap  = beanToMap(form);
        List<NaMiMarketingCustomize> recordList = null;
        int count = this.naMiMarketingService.selectNaMiMarketingPerfanceCount(paraMap);
        if (count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            paraMap.put("limitStart",paginator.getOffset());
            paraMap.put("limitEnd",paginator.getLimit());
            recordList = this.naMiMarketingService.selectNaMiMarketingPerfanceList(paraMap);
            form.setPaginator(paginator);
            modelAndView.addObject("recordList", recordList);
        }
        modelAndView.addObject(NaMiMarketingDefine.PRIZECODE_FORM, form);
        return recordList;
    }

    /**
     * 业绩返现详情
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(NaMiMarketingDefine.PERFORMANCE_INFO)
    @RequiresPermissions(NaMiMarketingDefine.PERMISSIONS_VIEW)
    public ModelAndView performanceInfo(HttpServletRequest request, NaMiMarketingBean form) {
        LogUtil.startLog(NaMiMarketingController.class.toString(), NaMiMarketingDefine.PERFORMANCE_INFO);
        ModelAndView modelAndView = new ModelAndView(NaMiMarketingDefine.PERFORMANCE_INFO_LIST_PATH);
        //id为空，不查询
        if(null != form.getId()&& form.getId().intValue() >0){
            //根据id查询上级推荐人
            List<PerformanceReturnDetail> recordList=this.naMiMarketingService.selectNaMiMarketingPerfanceInfo(form);
            if(!CollectionUtils.isEmpty(recordList)){
                BigDecimal returnAmount = recordList.get(0).getReturnAmount();
                if(recordList.size()==1){
                    modelAndView.addObject("A", recordList.get(0));
                    modelAndView.addObject("active",1);
                }else if(recordList.size()==2){
                    modelAndView.addObject("B", recordList.get(0));
                    recordList.get(1).setReturnAmount(returnAmount);
                    modelAndView.addObject("A", recordList.get(1));
                    modelAndView.addObject("active",2);
                } else if(recordList.size()==3){
                    BigDecimal ret=returnAmount.divide(new BigDecimal(2),2,BigDecimal.ROUND_DOWN);
                    modelAndView.addObject("C", recordList.get(0));
                    recordList.get(1).setReturnAmount(ret);
                    modelAndView.addObject("B", recordList.get(1));
                    recordList.get(2).setReturnAmount(ret);
                    modelAndView.addObject("A", recordList.get(2));
                    modelAndView.addObject("active",3);
                }else if(recordList.size()==4){
                    BigDecimal ret=returnAmount.divide(new BigDecimal(3),2,BigDecimal.ROUND_DOWN);
                    modelAndView.addObject("D", recordList.get(0));
                    recordList.get(1).setReturnAmount(ret);
                    modelAndView.addObject("C", recordList.get(1));
                    recordList.get(2).setReturnAmount(ret);
                    modelAndView.addObject("B", recordList.get(2));
                    recordList.get(3).setReturnAmount(ret);
                    modelAndView.addObject("A", recordList.get(3));
                    modelAndView.addObject("active",4);
                }
            }
        }
        LogUtil.endLog(NaMiMarketingController.class.toString(), NaMiMarketingDefine.PERFORMANCE_INFO);
        return modelAndView;
    }


    /**
     * 邀请 明细导出
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(NaMiMarketingDefine.EXPORT_ACTION)
    @RequiresPermissions(NaMiMarketingDefine.PERMISSIONS_EXPORT)
    public void exportExcel(HttpServletRequest request, HttpServletResponse response, NaMiMarketingBean form) throws Exception {
        // 表格sheet名称
        String sheetName = "邀请明细";
        // 取得数据
        List<NaMiMarketingCustomize> recordList = createPage(request, new ModelAndView(),form);
        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
        String[] titles = new String[]{ "序号","账户名", "姓名", "邀请人账户名","注册日期时间"};
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
                    //邀请人账户名
                    else if (celLength == 3) {
                        cell.setCellValue(bean.getRefferName()==null?"" : bean.getRefferName());
                    }
                    //  注册日期时间
                    else if (celLength == 4) {
                        cell.setCellValue(bean.getRegTime() == null? "": bean.getRegTime());
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
    }

    /**
     * 业绩返现详情 导出
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(NaMiMarketingDefine.EXPORT_PERFORMANCE_ACTION)
    @RequiresPermissions(NaMiMarketingDefine.PERMISSIONS_EXPORT)
    public void exportPerformanceExcel(HttpServletRequest request, HttpServletResponse response, NaMiMarketingBean form) throws Exception {
        // 表格sheet名称
        String sheetName = "年化业绩返现详情";
        // 取得数据
        List<NaMiMarketingCustomize> recordList = performanceCreatePage(request, new ModelAndView(),form);
        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
        String[] titles = new String[]{ "序号","账户名", "姓名", "邀请人账户名","投资订单号","单笔投资金额（元）","投资期限","产品类型","产品编号","单笔当月产生的业绩（元）","单笔返现金额（元）","放款时间/加入时间"};
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
                    //邀请人账户名
                    else if (celLength == 3) {
                        cell.setCellValue(bean.getRefferName()==null?"" : bean.getRefferName());
                    }
                    //  投资订单号
                    else if (celLength == 4) {
                        cell.setCellValue(bean.getTenderNo() == null? "": bean.getTenderNo());
                    }
                    //  单笔投资金额
                    else if (celLength == 5) {
                        cell.setCellValue(bean.getTenderAmount() == null? BigDecimal.ZERO+"": bean.getTenderAmount()+"");
                    }
                    //  投资期限
                    else if (celLength == 6) {
                        cell.setCellValue(bean.getTerm() == null? "": bean.getTerm());
                    }
                    //  产品类型
                    else if (celLength == 7) {
                        cell.setCellValue(bean.getProductType() == null? "": bean.getProductType());
                    }
                    //  产品编号
                    else if (celLength == 8) {
                        cell.setCellValue(bean.getTenderNo() == null? "": bean.getTenderNo());
                    }
                    //  单笔当月产生的业绩（元）
                    else if (celLength == 9) {
                        cell.setCellValue(bean.getReturnPerformance() == null? BigDecimal.ZERO+"": bean.getReturnPerformance()+"");
                    }
                    //  单笔返现金额（元）
                    else if (celLength == 10) {
                        cell.setCellValue(bean.getReturnAmount() == null? BigDecimal.ZERO+"": bean.getReturnAmount()+"");
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


    public Map<String, Object> beanToMap(NaMiMarketingBean form){
        Map<String, Object> paraMap = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(form.getUsername())){
            paraMap.put("username", form.getUsername().trim());
        }
        if(StringUtils.isNotBlank(form.getTruename())){
            paraMap.put("truename", form.getTruename().trim());
        }
        if(StringUtils.isNotBlank(form.getRefferName())){
            paraMap.put("refferName", form.getRefferName().trim());
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
        return paraMap;
    }
}
