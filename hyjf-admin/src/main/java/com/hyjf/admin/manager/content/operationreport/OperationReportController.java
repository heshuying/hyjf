package com.hyjf.admin.manager.content.operationreport;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.content.ads.ContentAdsController;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.*;
import com.hyjf.mybatis.model.auto.MonthlyOperationReport;
import com.hyjf.mybatis.model.auto.OperationReport;
import com.hyjf.mybatis.model.auto.QuarterOperationReport;
import com.hyjf.mybatis.model.customize.AdminSystem;
import com.hyjf.mybatis.model.customize.report.OperationReportCustomize;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author xiehuili
 * @version 2.0
 */
@Controller
@RequestMapping(value = ContentOperationreportDefine.REQUEST_MAPPING)
public class OperationReportController extends BaseController {

    @Autowired
    private OperationReportService operationReportService;

    /**
     * 列表初始化
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(ContentOperationreportDefine.INIT)
    @RequiresPermissions(ContentOperationreportDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,
                             @ModelAttribute(ContentOperationreportDefine.FORM) OperationReportBean form) {
        LogUtil.startLog(OperationReportController.class.toString(), ContentOperationreportDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(ContentOperationreportDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(OperationReportController.class.toString(), ContentOperationreportDefine.INIT);
        return modelAndView;
    }

    /**
     * 进入季度报告页面
     *
     * @param request
     * @return
     */
    @RequestMapping(ContentOperationreportDefine.QUARTER_INIT)
    @RequiresPermissions(ContentOperationreportDefine.PERMISSIONS_VIEW)
    public ModelAndView initQuarter(HttpServletRequest request) {
        LogUtil.startLog(OperationReportController.class.toString(), ContentOperationreportDefine.QUARTER_INIT);
        ModelAndView modelAndView = new ModelAndView(ContentOperationreportDefine.QUARTER_LIST_PATH);
        OperationreportCommonBean form = new OperationreportCommonBean();
        OperationReport operationReport = new OperationReport();
        QuarterOperationReport quarterOperationReport = new QuarterOperationReport();
        String type = request.getParameter("operationReportType");
        Integer operationReportType = null;
        if (type != null && type != "") {
            operationReportType = Integer.parseInt(type);
            operationReport.setOperationReportType(operationReportType);
            if (operationReportType == 5) {
                quarterOperationReport.setQuarterType(1);
            } else if (operationReportType == 6) {
                quarterOperationReport.setQuarterType(3);
            }
        }
        String year = request.getParameter("year");
        if (year != null && year != "") {
            operationReport.setYear(year);
        } else {
            operationReport.setYear(String.valueOf(GetDate.getYear()));
        }
        form.setOperationReport(operationReport);
        form.setQuarterOperationReport(quarterOperationReport);
        modelAndView.addObject(ContentOperationreportDefine.FORM_COOMMON, form);
        LogUtil.endLog(OperationReportController.class.toString(), ContentOperationreportDefine.QUARTER_INIT);
        return modelAndView;
    }

    /**
     * 进入月度报告页面，修改頁面
     *
     * @param request
     * @return
     */
    @RequestMapping(ContentOperationreportDefine.MONTH_INIT)
    @RequiresPermissions(ContentOperationreportDefine.PERMISSIONS_VIEW)
    public ModelAndView initMonth(HttpServletRequest request) {
        LogUtil.startLog(OperationReportController.class.toString(), ContentOperationreportDefine.MONTH_INIT);
        ModelAndView modelAndView = new ModelAndView(ContentOperationreportDefine.MONTH_LIST_PATH);
        OperationreportCommonBean form = new OperationreportCommonBean();
        MonthlyOperationReport monthlyOperationReport = new MonthlyOperationReport();
        OperationReport operationReport = new OperationReport();
        String id = request.getParameter("id");
        if (StringUtils.isNotEmpty(id)) {
            OperationreportCommonBean perationreportCommonBean = operationReportService.selectOperationreportCommon(id);
            int type = perationreportCommonBean.getOperationReport().getOperationReportType().intValue();
            if (type == 5 || type == 6) {
                modelAndView = new ModelAndView(ContentOperationreportDefine.QUARTER_LIST_PATH);
            }
            if (type == 3) {
                modelAndView = new ModelAndView(ContentOperationreportDefine.HALFYEAR_LIST_PATH);
            }
            if (type == 4) {
                modelAndView = new ModelAndView(ContentOperationreportDefine.YEAR_LIST_PATH);
            }
            modelAndView.addObject(ContentOperationreportDefine.FORM_COOMMON, perationreportCommonBean);
        } else {
            String mon = request.getParameter("month");
            String year = request.getParameter("year");
            if (StringUtils.isNotEmpty(mon)) {
                monthlyOperationReport.setMonth(Integer.parseInt(mon));
            } else {
                monthlyOperationReport.setMonth(1);
            }
            if (StringUtils.isNotEmpty(year)) {
                operationReport.setYear(year);
            } else {
                int nowYear = Calendar.getInstance().get(Calendar.YEAR);
                operationReport.setYear(String.valueOf(nowYear));
            }
            operationReport.setOperationReportType(1);
            form.setOperationReport(operationReport);
            form.setMonthlyOperationReport(monthlyOperationReport);
            modelAndView.addObject(ContentOperationreportDefine.FORM_COOMMON, form);
        }
        //修改页面图片回显
        String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
        modelAndView.addObject("imgSrc" ,fileDomainUrl);
        LogUtil.endLog(OperationReportController.class.toString(), ContentOperationreportDefine.MONTH_INIT);
        return modelAndView;
    }

    /**
     * 进入上半年度报告页面
     *
     * @param request
     * @return
     */
    @RequestMapping(ContentOperationreportDefine.HALFYEAR_INIT)
    @RequiresPermissions(ContentOperationreportDefine.PERMISSIONS_VIEW)
    public ModelAndView initHalfYear(HttpServletRequest request) {
        LogUtil.startLog(OperationReportController.class.toString(), ContentOperationreportDefine.HALFYEAR_INIT);
        ModelAndView modelAndView = new ModelAndView(ContentOperationreportDefine.HALFYEAR_LIST_PATH);
        OperationreportCommonBean form = new OperationreportCommonBean();
        OperationReport operationReport = new OperationReport();
        operationReport.setOperationReportType(3);
        String year = request.getParameter("year");
        if (year != null && year != "") {
            operationReport.setYear(year);
        } else {
            operationReport.setYear(String.valueOf(GetDate.getYear()));
        }
        form.setOperationReport(operationReport);
        modelAndView.addObject(ContentOperationreportDefine.FORM_COOMMON, form);
        LogUtil.endLog(OperationReportController.class.toString(), ContentOperationreportDefine.HALFYEAR_INIT);
        return modelAndView;
    }

    /**
     * 进入年度报告页面
     *
     * @param request
     * @return
     */
    @RequestMapping(ContentOperationreportDefine.YEAR_INIT)
    @RequiresPermissions(ContentOperationreportDefine.PERMISSIONS_VIEW)
    public ModelAndView initYear(HttpServletRequest request) {
        LogUtil.startLog(OperationReportController.class.toString(), ContentOperationreportDefine.YEAR_INIT);
        ModelAndView modelAndView = new ModelAndView(ContentOperationreportDefine.YEAR_LIST_PATH);
        OperationreportCommonBean form = new OperationreportCommonBean();
        OperationReport operationReport = new OperationReport();
        operationReport.setOperationReportType(4);
        String year = request.getParameter("year");
        if (year != null && year != "") {
            operationReport.setYear(year);
        } else {
            operationReport.setYear(String.valueOf(GetDate.getYear()));
        }
        form.setOperationReport(operationReport);
        modelAndView.addObject(ContentOperationreportDefine.FORM_COOMMON, form);
        LogUtil.endLog(OperationReportController.class.toString(), ContentOperationreportDefine.YEAR_INIT);
        return modelAndView;
    }

    /**
     * 删除信息
     *
     * @param request
     * @param ids
     * @return
     */
    @RequestMapping(ContentOperationreportDefine.DELETE_ACTION)
    @RequiresPermissions(ContentOperationreportDefine.PERMISSIONS_DELETE)
    public ModelAndView deleteRecordAction(HttpServletRequest request, String ids) {
        LogUtil.startLog(OperationReportController.class.toString(), ContentOperationreportDefine.DELETE_ACTION);

        ModelAndView modelAndView = new ModelAndView(ContentOperationreportDefine.RE_LIST_PATH);
        // 解析json字符串
        if (StringUtils.isNotEmpty(ids)) {
            List<String> recordList = JSONArray.parseArray(ids, String.class);
            OperationReport record = new OperationReport();
            record.setIsDelete(1);
            record.setId(recordList.get(0));
            operationReportService.updateByPrimaryKeySelective(record);
        }
        LogUtil.endLog(OperationReportController.class.toString(), ContentOperationreportDefine.DELETE_ACTION);
        return modelAndView;
    }

    /**
     * 迁移到修改详情
     *
     * @param request
     * @return
     */
    @RequestMapping(ContentOperationreportDefine.UPDATEACTION)
    @RequiresPermissions(ContentOperationreportDefine.PERMISSIONS_VIEW)
    public ModelAndView modifyMonthOperation(HttpServletRequest request, OperationReportBean from) {
        LogUtil.startLog(OperationReportController.class.toString(), ContentOperationreportDefine.UPDATEACTION);
        String ids = from.getIds();
        ModelAndView modelAndView = null;
        String str = "manager/content/contentoperationreport";
        OperationreportCommonBean form = operationReportService.selectOperationreportCommon(ids);
        if (form != null) {
            modelAndView = new ModelAndView(ContentOperationreportDefine.INFO_PATH);
        }
        modelAndView.addObject(ContentOperationreportDefine.FORM_COOMMON, form);
        LogUtil.endLog(OperationReportController.class.toString(), ContentOperationreportDefine.UPDATEACTION);
        return modelAndView;

    }

    /**
     * 根据条件查询所需要数据
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(ContentOperationreportDefine.SEARCH_ACTION)
    @RequiresPermissions(ContentOperationreportDefine.PERMISSIONS_SEARCH)
    public ModelAndView searchAction(HttpServletRequest request, RedirectAttributes attr,
                                     @ModelAttribute(ContentOperationreportDefine.FORM) OperationReportBean form) {
        LogUtil.startLog(OperationReportController.class.toString(), ContentOperationreportDefine.SEARCH_ACTION);
        ModelAndView modelAndView = new ModelAndView(ContentOperationreportDefine.LIST_PATH);

        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(OperationReportController.class.toString(), ContentOperationreportDefine.SEARCH_ACTION);
        return modelAndView;
    }

    private void createPage(HttpServletRequest request, ModelAndView modelAndView, OperationReportBean form) {
        Map<String, Object> map = new HashMap<String, Object>();
        Integer count = 0;
        map.put("typeSearch", form.getTypeSearch());
        if(StringUtils.isNotEmpty(form.getStartCreate())){
            map.put("timeStar",form.getStartCreate()+" 00:00:00");
        }
        if(StringUtils.isNotEmpty(form.getEndCreate())){
            map.put("timeEnd",form.getEndCreate()+" 23:59:59");
        }
        //根据类型切换查询逻辑
        if (StringUtils.isNotEmpty(form.getTypeSearch())) {
            String type = form.getTypeSearch();
            //1到12月份为month类型
            int typeInt = Integer.valueOf(type).intValue();
            if (typeInt <= 12) {
                map.put("monthType", typeInt);
                count = operationReportService.countRecordByMonth(map);
                if (count != null && count > 0) {
                    Paginator paginator = new Paginator(form.getPaginatorPage(), count);
                    map.put("limitStart", paginator.getOffset());
                    map.put("limitEnd", paginator.getLimit());
                    List<OperationReportCustomize> recordList = this.operationReportService
                            .getRecordListByMonth(map);
                    form.setPaginator(paginator);
                    form.setRecordList(recordList);
                }
            } else if (typeInt == 13 || typeInt == 14) { //季度
                if (typeInt == 13) {
                    map.put("quarterType", 1);
                } else if (typeInt == 14) {
                    map.put("quarterType", 3);
                }
                count = operationReportService.countRecordByQuarter(map);
                if (count != null && count > 0) {
                    Paginator paginator = new Paginator(form.getPaginatorPage(), count);
                    map.put("limitStart", paginator.getOffset());
                    map.put("limitEnd", paginator.getLimit());
                    List<OperationReportCustomize> recordList = this.operationReportService
                            .getRecordListByQuarter(map);
                    form.setPaginator(paginator);
                    form.setRecordList(recordList);
                }
            } else if (typeInt == 15) { //半年
                count = operationReportService.countRecordByHalfYear(map);
                if (count != null && count > 0) {
                    Paginator paginator = new Paginator(form.getPaginatorPage(), count);
                    map.put("limitStart", paginator.getOffset());
                    map.put("limitEnd", paginator.getLimit());
                    List<OperationReportCustomize> recordList = this.operationReportService
                            .getRecordListByHalfYear(map);
                    form.setPaginator(paginator);
                    form.setRecordList(recordList);
                }
            } else if (typeInt == 16) {  //全年
                count = operationReportService.countRecordByYear(map);
                if (count != null && count > 0) {
                    Paginator paginator = new Paginator(form.getPaginatorPage(), count);
                    map.put("limitStart", paginator.getOffset());
                    map.put("limitEnd", paginator.getLimit());
                    List<OperationReportCustomize> recordList = this.operationReportService
                            .getRecordListByYear(map);
                    form.setPaginator(paginator);
                    form.setRecordList(recordList);
                }
            }

        } else {
            //查询所有数据
            count = operationReportService.countRecord(map);
            if (count != null && count > 0) {
                Paginator paginator = new Paginator(form.getPaginatorPage(), count);
                map.put("limitStart", paginator.getOffset());
                map.put("limitEnd", paginator.getLimit());
                List<OperationReportCustomize> recordList = this.operationReportService
                        .getRecordList(map);
                form.setPaginator(paginator);
                form.setRecordList(recordList);
            }
        }
        //返回页面数据
        modelAndView.addObject(ContentOperationreportDefine.FORM, form);
        String webUrl = PropUtils.getSystem("hyjf.web.host");
        modelAndView.addObject("webUrl", webUrl);
    }

    /**
     * 发布
     *
     * @param request
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(ContentOperationreportDefine.PUBLISH_ACTION)
    @RequiresPermissions(ContentOperationreportDefine.PERMISSIONS_MODIFY)
    public String publishAction(HttpServletRequest request, @RequestBody OperationReportBean form) {
        LogUtil.startLog(OperationReportController.class.toString(), ContentOperationreportDefine.PUBLISH_ACTION);
        JSONObject ret = new JSONObject();
        try {
            AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
            OperationReport record = new OperationReport();
            int nowTime = GetDate.getMyTimeInMillis();
            record.setId(form.getId());
            if (form.getIsRelease() == 1) {
                record.setIsRelease(0);//发布切换为未发布
            } else {
                record.setIsRelease(1);//未发布切换为发布
            }
            record.setUpdateTime(nowTime);
            record.setReleaseTime(nowTime);
            record.setUpdateUserId(Integer.parseInt(adminSystem.getId()));
            Integer result = operationReportService.updateByPrimaryKeySelective(record);
            if (result == 1) {
                ret.put(ContentOperationreportDefine.JSON_STATUS_KEY, ContentOperationreportDefine.JSON_STATUS_OK);
            }
        } catch (Exception e) {
            ret.put(ContentOperationreportDefine.JSON_STATUS_NG, "系统正在维护，请稍后再试");
        }

        LogUtil.endLog(OperationReportController.class.toString(), ContentOperationreportDefine.PUBLISH_ACTION);

        return ret.toString();
    }


    /**
     * 导出列表
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(ContentOperationreportDefine.EXPORT_EXCEL_ACTION)
    @RequiresPermissions(ContentOperationreportDefine.PERMISSIONS_EXPORT)
    public void exportAccountsExcel(HttpServletRequest request, HttpServletResponse response, OperationReportBean form) throws Exception {
        // 表格sheet名称
        String sheetName = "运营报告数据";
        // 取得数据
        List<OperationReportCustomize> recordList = createExcelPage(request, form);
        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

        String[] titles = new String[]{"标题名称", "累计交易额", "累计赚取收益", "平台注册人数", "本月（本季/本年）成交笔数", "本月（本季/本年）成交金额", "本月（本季/本年）为用户赚取收益", "状态", "发布时间"};

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
                    OperationReportCustomize bean = recordList.get(i);

                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);
                    // 标题名称
                    if (celLength == 0) {
                        cell.setCellValue(bean.getCnName());
                    }
                    // 累计交易额
                    else if (celLength == 1) {
                        cell.setCellValue(bean.getAllAmount() == null ? "" : String.valueOf(bean.getAllAmount()));
                    }
                    // 累计赚取收益
                    else if (celLength == 2) {
                        cell.setCellValue(bean.getAllProfit() == null ? "" : String.valueOf(bean.getAllProfit()));
                    }
                    // 平台注册人数
                    else if (celLength == 3) {
                        cell.setCellValue(bean.getRegistNum() == null ? "" : String.valueOf(bean.getRegistNum()));
                    }
                    // 本月（本季/本年）成交笔数
                    else if (celLength == 4) {
                        cell.setCellValue(bean.getSuccessDealNum() == null ? "" : String.valueOf(bean.getSuccessDealNum()));
                    }
                    // 本月（本季/本年）成交金额
                    else if (celLength == 5) {
                        cell.setCellValue(bean.getOperationAmount() == null ? "" : String.valueOf(bean.getOperationAmount()));
                    }
                    // 本月（本季/本年）为用户赚取收益
                    else if (celLength == 6) {
                        cell.setCellValue(bean.getOperationProfit() == null ? "" : String.valueOf(bean.getOperationProfit()));
                    }
                    // 状态
                    else if (celLength == 7) {
                        if (1 == bean.getIsRelease().intValue()) {
                            cell.setCellValue("发布");
                        } else {
                            cell.setCellValue("未发布");
                        }
                    }
                    // 发布时间
                    else if (celLength == 8) {
                        cell.setCellValue(bean.getReleaseTimeStr());
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
    }

    private List<OperationReportCustomize> createExcelPage(HttpServletRequest request, OperationReportBean form) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<OperationReportCustomize> list = null;
        Integer count = 0;
        map.put("typeSearch", form.getTypeSearch());
        map.put("startCreate", form.getStartCreate());
        map.put("endCreate", form.getEndCreate());

        if(StringUtils.isNotEmpty(form.getStartCreate())){
            map.put("timeStar",form.getStartCreate()+" 00:00:00");
        }
        if(StringUtils.isNotEmpty(form.getEndCreate())){
            map.put("timeEnd",form.getEndCreate()+" 23:59:59");
        }

        //根据类型切换查询逻辑
        if (StringUtils.isNotEmpty(form.getTypeSearch())) {
            String type = form.getTypeSearch();
            int typeInt = Integer.valueOf(type).intValue();
            //1到12月份为month类型
            if (typeInt<=12) {
                map.put("monthType", typeInt);
                count = operationReportService.countRecordByMonth(map);
                if (count != null && count > 0) {
                    Paginator paginator = new Paginator(form.getPaginatorPage(), count);
                    map.put("limitStart", paginator.getOffset());
                    map.put("limitEnd", paginator.getLimit());
                    list = this.operationReportService
                            .getRecordListByMonth(map);
                    form.setPaginator(paginator);
                    form.setRecordList(list);
                }
            } else if ("13".equals(type) || "14".equals(type)) { //季度
                if ("13".equals(type)) {
                    map.put("quarterType", 1);
                } else if ("14".equals(type)) {
                    map.put("quarterType", 3);
                }
                count = operationReportService.countRecordByQuarter(map);
                if (count != null && count > 0) {
                    Paginator paginator = new Paginator(form.getPaginatorPage(), count);
                    map.put("limitStart", paginator.getOffset());
                    map.put("limitEnd", paginator.getLimit());
                    list = this.operationReportService
                            .getRecordListByQuarter(map);
                    form.setPaginator(paginator);
                    form.setRecordList(list);
                }
            } else if ("15".equals(type)) { //半年
                count = operationReportService.countRecordByHalfYear(map);
                if (count != null && count > 0) {
                    Paginator paginator = new Paginator(form.getPaginatorPage(), count);
                    map.put("limitStart", paginator.getOffset());
                    map.put("limitEnd", paginator.getLimit());
                    list = this.operationReportService
                            .getRecordListByHalfYear(map);
                    form.setPaginator(paginator);
                    form.setRecordList(list);
                }
            } else if ("16".equals(type)) {  //全年
                count = operationReportService.countRecordByYear(map);
                if (count != null && count > 0) {
                    Paginator paginator = new Paginator(form.getPaginatorPage(), count);
                    map.put("limitStart", paginator.getOffset());
                    map.put("limitEnd", paginator.getLimit());
                    list = this.operationReportService
                            .getRecordListByYear(map);
                    form.setPaginator(paginator);
                    form.setRecordList(list);
                }
            }

        } else {
            //查询所有数据
            count = operationReportService.countRecord(map);
            if (count != null && count > 0) {
//                Paginator paginator = new Paginator(form.getPaginatorPage(), count);
//                map.put("limitStart", paginator.getOffset());
//                map.put("limitEnd", paginator.getLimit());
                list = this.operationReportService
                        .getRecordList(map);
//                form.setPaginator(paginator);
                form.setRecordList(list);
            }
        }
        return list;
    }
    /**
     * infoAction跳转到新增页面
     *
     * @param request
     * @return
     */
    @RequestMapping(ContentOperationreportDefine.INFO_ACTION)
    @RequiresPermissions(ContentOperationreportDefine.PERMISSIONS_VIEW)
    public ModelAndView infoMonthOperation(HttpServletRequest request, String ids,String pageStatus) {
        LogUtil.startLog(OperationReportController.class.toString(), ContentOperationreportDefine.INFO_ACTION);
        ModelAndView modelAndView = new ModelAndView(ContentOperationreportDefine.INFO_PATH);
        //下拉框初始化
        List<OperationSelectBean> selectList = initSelect();
        modelAndView.addObject("selectList", selectList);
        // 解析json字符串
        if (StringUtils.isNotEmpty(ids)) {
            OperationreportCommonBean form = operationReportService.selectOperationreportCommon(ids);
            if(StringUtils.isNotEmpty(pageStatus)){//再次发布也走这个逻辑
                OperationReport operationReport = form.getOperationReport();
                operationReport.setIsRelease(Integer.valueOf(pageStatus));
                form.setOperationReport(operationReport);
            }
            modelAndView.addObject(ContentOperationreportDefine.FORM_COOMMON, form);
            Integer type = form.getOperationReport().getOperationReportType();
            modelAndView.addObject("reportType", type);

        }
        LogUtil.endLog(OperationReportController.class.toString(), ContentOperationreportDefine.INFO_ACTION);
        return modelAndView;
    }

    /**
     * 月度运营报告新增修改
     *
     * @param request
     * @return
     */
    @RequestMapping(ContentOperationreportDefine.MONTHACTION)
    @RequiresPermissions(ContentOperationreportDefine.PERMISSIONS_VIEW)
    public ModelAndView insertMonthAction(HttpServletRequest request, OperationreportCommonBean from) {
        LogUtil.startLog(OperationReportController.class.toString(), ContentOperationreportDefine.MONTHACTION);
        ModelAndView modelAndView = new ModelAndView(ContentOperationreportDefine.RE_LIST_PATH);
        //运营报告的id
        if (from == null) {
            modelAndView = new ModelAndView(ContentOperationreportDefine.MONTH_LIST_PATH);
        }
        OperationReport operationReport = from.getOperationReport();
        if (operationReport == null) {
            modelAndView = new ModelAndView(ContentOperationreportDefine.MONTH_LIST_PATH);
        }
        String year = operationReport.getYear();
        if(StringUtils.isBlank(year)){
            operationReport.setYear(String.valueOf(GetDate.getYear()));
        }
        if (StringUtils.isBlank(operationReport.getId())) {
            //月度新增
            int result = operationReportService.insertMonthlyOperationReport(from);
            if (result != 1) {
                modelAndView = new ModelAndView(ContentOperationreportDefine.MONTH_LIST_PATH);
            }
        } else {
            //修改月度报告
            int result = operationReportService.updateMonthOperationReport(from);
            if (result != 1) {
                modelAndView = new ModelAndView(ContentOperationreportDefine.MONTH_LIST_PATH);
            }
        }
        LogUtil.endLog(OperationReportController.class.toString(), ContentOperationreportDefine.MONTHACTION);
        //todo  跳转
        return modelAndView;
    }

    /**
     * 季度运营报告新增
     *
     * @param request
     * @return
     */
    @RequestMapping(ContentOperationreportDefine.QUARTERACTION)
    @RequiresPermissions(ContentOperationreportDefine.PERMISSIONS_VIEW)
    public ModelAndView insertQuarterAction(HttpServletRequest request, OperationreportCommonBean from) {
        LogUtil.startLog(OperationReportController.class.toString(), ContentOperationreportDefine.QUARTERACTION);
        ModelAndView modelAndView = new ModelAndView(ContentOperationreportDefine.RE_LIST_PATH);
        OperationReport operationReport = from.getOperationReport();
        QuarterOperationReport quarterOperationReport = from.getQuarterOperationReport();
        if (operationReport == null) {
            return new ModelAndView(ContentOperationreportDefine.QUARTER_LIST_PATH);
        }
        String year = operationReport.getYear();
        if(StringUtils.isBlank(year)){
            operationReport.setYear(String.valueOf(GetDate.getYear()));
        }
        String id = operationReport.getId();
        Integer operationReportType = operationReport.getOperationReportType();
        if (StringUtils.isBlank(id)) {
            //第一季度5，第三季度6对应的季度运营报告类型1,3
            if (operationReportType == 5 || operationReportType == 6) {
                //设置运营报告中的运营报告类型，1月度，2季度，3半年，4年
                operationReport.setOperationReportType(2);
                from.setOperationReport(operationReport);
                //新增季度报告,
                if (operationReportType == 5) {
                    quarterOperationReport.setQuarterType(1);
                    from.setQuarterOperationReport(quarterOperationReport);
                }
                if (operationReportType == 6) {
                    quarterOperationReport.setQuarterType(3);
                    from.setQuarterOperationReport(quarterOperationReport);
                }
            }
            int result = operationReportService.insertQuarterOperationReport(from);
            if (result != 1) {
                modelAndView = new ModelAndView(ContentOperationreportDefine.QUARTER_LIST_PATH);
            }
        } else {
            //修改季度报告
            int result = operationReportService.updateQuarterOperationReport(from);
            if (result != 1) {
                modelAndView = new ModelAndView(ContentOperationreportDefine.QUARTER_LIST_PATH);
            }
        }
        LogUtil.endLog(OperationReportController.class.toString(), ContentOperationreportDefine.QUARTERACTION);
        return modelAndView;

    }

    /**
     * 上半年度运营报告新增修改
     *
     * @param request
     * @return
     */
    @RequestMapping(ContentOperationreportDefine.HALFYEARACTION)
    @RequiresPermissions(ContentOperationreportDefine.PERMISSIONS_VIEW)
    public ModelAndView insertHalfYearAction(HttpServletRequest request, OperationreportCommonBean from) {
        LogUtil.startLog(OperationReportController.class.toString(), ContentOperationreportDefine.HALFYEARACTION);
        ModelAndView modelAndView = new ModelAndView(ContentOperationreportDefine.RE_LIST_PATH);
        OperationReport operationReport = from.getOperationReport();
        if (operationReport == null) {
            return new ModelAndView(ContentOperationreportDefine.HALFYEAR_LIST_PATH);
        }
        String year = operationReport.getYear();
        if(StringUtils.isBlank(year)){
            operationReport.setYear(String.valueOf(GetDate.getYear()));
        }
        String id = operationReport.getId();
        if (StringUtils.isBlank(id)) {
            operationReport.setOperationReportType(3);
            //新增上半年报告,
           operationReportService.insertHalfYearOperationReport(from);
        } else {
            //修改上半年度报告
             operationReportService.updateHalfYearOperationReport(from);
        }
        LogUtil.endLog(OperationReportController.class.toString(), ContentOperationreportDefine.HALFYEARACTION);
        return modelAndView;

    }


    /**
     * 年度运营报告新增
     *
     * @param request
     * @return
     */
    @RequestMapping(ContentOperationreportDefine.YEARACTION)
    @RequiresPermissions(ContentOperationreportDefine.PERMISSIONS_VIEW)
    public ModelAndView insertYearAction(HttpServletRequest request, OperationreportCommonBean from) {
        LogUtil.startLog(OperationReportController.class.toString(), ContentOperationreportDefine.YEARACTION);
        ModelAndView modelAndView = new ModelAndView(ContentOperationreportDefine.RE_LIST_PATH);
        OperationReport operationReport = from.getOperationReport();
        if (operationReport == null) {
            return new ModelAndView(ContentOperationreportDefine.YEAR_LIST_PATH);
        }
        String year = operationReport.getYear();
        if(StringUtils.isBlank(year)){
            operationReport.setYear(String.valueOf(GetDate.getYear()));
        }
        String id = operationReport.getId();
        if (StringUtils.isBlank(id)) {
            //新增年度报告
            int result = operationReportService.insertYearOperationReport(from);
            if (result != 1) {
                modelAndView = new ModelAndView(ContentOperationreportDefine.YEAR_LIST_PATH);
            }
        } else {
            //修改年度报告
            int result = operationReportService.updateYearOperationReport(from);
            if (result != 1) {
                modelAndView = new ModelAndView(ContentOperationreportDefine.YEAR_LIST_PATH);
            }
        }

        LogUtil.endLog(OperationReportController.class.toString(), ContentOperationreportDefine.YEARACTION);
        return modelAndView;

    }

    /**
     * 月度新增修改页面预览
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = ContentOperationreportDefine.PREVIEW, method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = {ContentOperationreportDefine.PERMISSIONS_ADD, ContentOperationreportDefine.PERMISSIONS_MODIFY}, logical = Logical.OR)
    public JSONObject preview(HttpServletRequest request,HttpServletResponse response, OperationreportCommonBean from) throws Exception {
        LogUtil.startLog(ContentAdsController.class.toString(), ContentOperationreportDefine.PREVIEW);
        JSONObject json = new JSONObject();
        String webUrl = PropUtils.getSystem("hyjf.web.host");
        String path=null;
        if(from != null) {
            OperationReport operationReport = from.getOperationReport();
            if(operationReport != null){
                String year = operationReport.getYear();
                if(StringUtils.isBlank(year)){
                    operationReport.setYear(String.valueOf(GetDate.getYear()));
                }
                String id = operationReport.getId();
                if (StringUtils.isBlank(id)) {
                    //月度新增预览
                    json = operationReportService.insertMonthlyOperationReportPreview(from);
                    if (json != null) {
                        String operationId = json.get("operationId").toString();
                        path=webUrl+"/report/initMonthReport.do?id="+operationId;
                        json.put("path",path);
                    }
                } else {
                    //修改月度报告预览
                    json = operationReportService.updateMonthOperationReportPreview(from);
                    if (json != null) {
                        String operationId = json.get("operationId").toString();
                        path=webUrl+"/report/initMonthReport.do?id="+operationId;
                        json.put("path",path);
                    }
                }
            }
        }
        LogUtil.endLog(OperationReportController.class.toString(), ContentOperationreportDefine.PREVIEW);
        return json;
    }

    /**
     * 年度新增修改页面预览
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = ContentOperationreportDefine.YEAR_PREVIEW, method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = {ContentOperationreportDefine.PERMISSIONS_ADD, ContentOperationreportDefine.PERMISSIONS_MODIFY}, logical = Logical.OR)
    public JSONObject yearpreview(HttpServletRequest request,HttpServletResponse response, OperationreportCommonBean from) throws Exception {
        LogUtil.startLog(ContentAdsController.class.toString(), ContentOperationreportDefine.PREVIEW);
        JSONObject json = new JSONObject();
        String webUrl = PropUtils.getSystem("hyjf.web.host");
        String path=null;
        if(from != null) {
            OperationReport operationReport = from.getOperationReport();
            if(operationReport != null){
                String year = operationReport.getYear();
                if(StringUtils.isBlank(year)){
                    operationReport.setYear(String.valueOf(GetDate.getYear()));
                }
                if (StringUtils.isBlank(operationReport.getId())) {
                    //年度新增
                    json = operationReportService.insertYearlyOperationReportPreview(from);
                    if (json != null) {
                        String operationId = json.get("operationId").toString();
                        path=webUrl+"/report/initMonthReport.do?id="+operationId;
                        json.put("path",path);
                    }
                } else {
                    //修改年度报告
                    json = operationReportService.updateYearOperationReportPreview(from);
                    if (json != null) {
                        String operationId = json.get("operationId").toString();
                        path=webUrl+"/report/initMonthReport.do?id="+operationId;
                        json.put("path",path);
                    }
                }

            }
        }
        LogUtil.endLog(OperationReportController.class.toString(), ContentOperationreportDefine.PREVIEW);
        return json;
    }


    /**
     * 季度新增修改页面预览
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = ContentOperationreportDefine.PREVIEWQUARTERACTION, method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = {ContentOperationreportDefine.PERMISSIONS_ADD, ContentOperationreportDefine.PERMISSIONS_MODIFY}, logical = Logical.OR)
    public JSONObject previewQuarter(HttpServletRequest request,HttpServletResponse response, OperationreportCommonBean from) throws Exception {
        LogUtil.startLog(ContentAdsController.class.toString(), ContentOperationreportDefine.PREVIEW);
        JSONObject json = new JSONObject();
        String webUrl = PropUtils.getSystem("hyjf.web.host");
        String path=null;
        if(from != null) {
            OperationReport operationReport = from.getOperationReport();
            if(operationReport != null){
                String year = operationReport.getYear();
                if(StringUtils.isBlank(year)){
                    operationReport.setYear(String.valueOf(GetDate.getYear()));
                }
                String id = operationReport.getId();
                if (StringUtils.isBlank(id)) {
                    //季度新增页面预览
                    Integer type= operationReport.getOperationReportType();
                    json = operationReportService.insertQuarterOperationReportPreview(from,type);
                    if (json != null) {
                        String operationId = json.get("operationId").toString();
                        path=webUrl+"/report/initMonthReport.do?id="+operationId;
                        json.put("path",path);
                    }
                } else {
                    //季度报告修改页面预览
                    json = operationReportService.updateQuarterOperationReportPreview(from);
                    if (json != null) {
                        String operationId = json.get("operationId").toString();
                        path=webUrl+"/report/initMonthReport.do?id="+operationId;
                        json.put("path",path);
                    }
                }
            }
        }
        LogUtil.endLog(OperationReportController.class.toString(), ContentOperationreportDefine.PREVIEW);
        return json;
    }
    /**
     * 半年度新增修改页面预览
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = ContentOperationreportDefine.PREVIEW_HALF, method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = {ContentOperationreportDefine.PERMISSIONS_ADD, ContentOperationreportDefine.PERMISSIONS_MODIFY}, logical = Logical.OR)
    public JSONObject previewHalf(HttpServletRequest request,HttpServletResponse response, OperationreportCommonBean from) throws Exception {
        LogUtil.startLog(ContentAdsController.class.toString(), ContentOperationreportDefine.PREVIEW);
        JSONObject json = new JSONObject();
        String webUrl = PropUtils.getSystem("hyjf.web.host");
        String path=null;
        if(from != null) {
            OperationReport operationReport = from.getOperationReport();
            if(operationReport != null){
                String year = operationReport.getYear();
                if(StringUtils.isBlank(year)){
                    operationReport.setYear(String.valueOf(GetDate.getYear()));
                }
                String id = operationReport.getId();
                if (StringUtils.isBlank(id)) {
                    //半年预览新增
                    json = operationReportService.insertHalfYearOperationReportPreview(from);
                    if (json != null) {
                        String operationId = json.get("operationId").toString();
                        path=webUrl+"/report/initMonthReport.do?id="+operationId;
                        json.put("path",path);
                    }
                } else {
                    //半年预览修改
                    json = operationReportService.updateHalfYearOperationReportPreview(from);
                    if (json != null) {
                        String operationId = json.get("operationId").toString();
                        path=webUrl+"/report/initMonthReport.do?id="+operationId;
                        json.put("path",path);
                    }
                }
            }
        }
        LogUtil.endLog(OperationReportController.class.toString(), ContentOperationreportDefine.PREVIEW);
        return json;
    }
    /**
     * 资料上传
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = ContentOperationreportDefine.UPLOAD_FILE, method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = {ContentOperationreportDefine.PERMISSIONS_ADD, ContentOperationreportDefine.PERMISSIONS_MODIFY}, logical = Logical.OR)
    public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LogUtil.startLog(ContentAdsController.class.toString(), ContentOperationreportDefine.UPLOAD_FILE);
        String files = operationReportService.uploadFile(request, response);
        LogUtil.endLog(OperationReportController.class.toString(), ContentOperationreportDefine.UPLOAD_FILE);
        return files;
    }


    private List<OperationSelectBean> initSelect() {
        List<OperationSelectBean> selectList = new ArrayList<OperationSelectBean>();
        OperationSelectBean bean1 = new OperationSelectBean();
        bean1.setCode("1");
        bean1.setName("月度运营报告");
        selectList.add(bean1);
        OperationSelectBean bean5 = new OperationSelectBean();
        bean5.setCode("5");
        bean5.setName("第一季度运营报告");
        selectList.add(bean5);
        OperationSelectBean bean3 = new OperationSelectBean();
        bean3.setCode("3");
        bean3.setName("半年度运营报告");
        selectList.add(bean3);
        OperationSelectBean bean6 = new OperationSelectBean();
        bean6.setCode("6");
        bean6.setName("第三季度运营报告");
        selectList.add(bean6);
        OperationSelectBean bean4 = new OperationSelectBean();
        bean4.setCode("4");
        bean4.setName("年度运营报告");
        selectList.add(bean4);
        return selectList;
    }

}
