package com.hyjf.admin.manager.content.statisticsway;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.StatisticsWayConfigure;
import com.hyjf.mybatis.model.customize.statisticsway.StatisticsWayConfigureCustomize;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 运营报告---统计方式配置
 * @author  xiehuili on 2018/6/20.
 */
@Controller
@RequestMapping(value = StatisticsWayDefine.REQUEST_MAPPING)
public class StatisticsWayController extends BaseController {

    @Autowired
    private StatisticsWayService statisticsWayService;

    /**
     * 列表初始化
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(StatisticsWayDefine.INIT)
    @RequiresPermissions(StatisticsWayDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,
                             @ModelAttribute(StatisticsWayDefine.FORM) StatisticsWayBean form) {
        LogUtil.startLog(StatisticsWayDefine.class.toString(), StatisticsWayDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(StatisticsWayDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(StatisticsWayDefine.class.toString(), StatisticsWayDefine.INIT);
        return modelAndView;
    }

    /**
     * infoAction跳转到新增页面
     *
     * @param request
     * @return
     */
    @RequestMapping(StatisticsWayDefine.INFO_ACTION)
    @RequiresPermissions(StatisticsWayDefine.PERMISSIONS_VIEW)
    public ModelAndView infoMonthOperation(HttpServletRequest request, String ids) {
        LogUtil.startLog(StatisticsWayDefine.class.toString(), StatisticsWayDefine.INFO_ACTION);
        ModelAndView modelAndView = new ModelAndView(StatisticsWayDefine.INFO_PATH);
        // 解析json字符串
        if (StringUtils.isNotEmpty(ids)) {
            StatisticsWayConfigureCustomize form = statisticsWayService.selectstatisticsWayById(Integer.valueOf(ids));
            modelAndView.addObject("recode", form);
        }
        LogUtil.endLog(StatisticsWayDefine.class.toString(), StatisticsWayDefine.INFO_ACTION);
        return modelAndView;
    }

    /**
     * 校验表单字段
     *
     * @return
     */
    @RequestMapping(value = StatisticsWayDefine.VALIDATE_ACTION, method = RequestMethod.POST)
    @ResponseBody
    public JSONObject validatFieldCheck(HttpServletRequest request, StatisticsWayConfigure form) {
        //获取校验参数
        return  statisticsWayService.validatFieldCheck(form);
    }

    /**
     * 新增统计方式配置
     *
     * @param request
     * @return
     */
    @RequestMapping(value = StatisticsWayDefine.INSERT_ACTION, method = RequestMethod.POST)
    @RequiresPermissions(StatisticsWayDefine.PERMISSIONS_ADD)
    public ModelAndView insertStaticsWay(HttpServletRequest request, StatisticsWayConfigure form) {
        LogUtil.startLog(StatisticsWayDefine.class.toString(), StatisticsWayDefine.INFO_ACTION);
        ModelAndView modelAndView = new ModelAndView(StatisticsWayDefine.RE_LIST_PATH);
        if(form != null){
             statisticsWayService.updateStaticsWay(form);
        }
        LogUtil.endLog(StatisticsWayDefine.class.toString(), StatisticsWayDefine.INFO_ACTION);
        return modelAndView;
    }

    /**
     * 修改统计方式配置
     *
     * @param form
     * @return
     */
    @RequestMapping(value = StatisticsWayDefine.UPDATE_ACTION)
    @RequiresPermissions(StatisticsWayDefine.PERMISSIONS_MODIFY)
    public ModelAndView updateStaticsWay(HttpServletRequest request, StatisticsWayConfigure form) {
        LogUtil.startLog(StatisticsWayDefine.class.toString(), StatisticsWayDefine.INFO_ACTION);
        ModelAndView modelAndView = new ModelAndView(StatisticsWayDefine.RE_LIST_PATH);
        if(form != null){
            statisticsWayService.updateStaticsWay(form);
        }
        LogUtil.endLog(StatisticsWayDefine.class.toString(), StatisticsWayDefine.INFO_ACTION);
        return modelAndView;
    }

    /**
     * 删除统计方式配置
     *
     * @param request
     * @param ids
     * @return
     */
    @RequestMapping(StatisticsWayDefine.DELETE_ACTION)
    @RequiresPermissions(StatisticsWayDefine.PERMISSIONS_DELETE)
    public ModelAndView deleteStaticsWay(HttpServletRequest request, String ids) {
        LogUtil.startLog(StatisticsWayDefine.class.toString(), StatisticsWayDefine.DELETE_ACTION);
        ModelAndView modelAndView = new ModelAndView(StatisticsWayDefine.RE_LIST_PATH);
        if (StringUtils.isNotBlank(ids)) {
            statisticsWayService.deleteStaticsWay(Integer.valueOf(ids));
        }
        LogUtil.endLog(StatisticsWayDefine.class.toString(), StatisticsWayDefine.DELETE_ACTION);
        return modelAndView;
    }




    /**
     * 导出列表
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(StatisticsWayDefine.EXPORT_EXCEL_ACTION)
    @RequiresPermissions(StatisticsWayDefine.PERMISSIONS_EXPORT)
    public void exportAccountsExcel(HttpServletRequest request, HttpServletResponse response, StatisticsWayBean form) throws Exception {
        // 表格sheet名称
        String sheetName = "统计方式配置";
        // 取得数据
        List<StatisticsWayConfigureCustomize> recordList = createExcelPage(request, form);
        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

        String[] titles = new String[]{"标题名称", "唯一标识", "统计方式", "时间"};

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
                    StatisticsWayConfigureCustomize bean = recordList.get(i);
                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);
                    // 标题名称
                    if (celLength == 0) {
                        cell.setCellValue(bean.getTitleName());
                    }
                    // 唯一标识
                    else if (celLength == 1) {
                        cell.setCellValue(bean.getUniqueIdentifier());
                    }
                    //统计方式
                    else if (celLength == 2) {
                        cell.setCellValue(bean.getStatisticalMethod());
                    }
                    // 时间
                    else if (celLength == 3) {
                        cell.setCellValue(bean.getTime());
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
    }


    private List<StatisticsWayConfigureCustomize> createExcelPage(HttpServletRequest request,StatisticsWayBean form) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("titleName", form.getTitleName());
        map.put("uniqueIdentifier", form.getUniqueIdentifier());
        if(StringUtils.isNotEmpty(form.getStartTime())){
            map.put("timeStar",form.getStartTime()+" 00:00:00");
        }
        if(StringUtils.isNotEmpty(form.getEndTime())){
            map.put("timeEnd",form.getEndTime()+" 23:59:59");
        }
        Integer count = statisticsWayService.countRecordList(map);
        List<StatisticsWayConfigureCustomize> recordList =new ArrayList<StatisticsWayConfigureCustomize>();
        if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            map.put("limitStart", paginator.getOffset());
            map.put("limitEnd", paginator.getLimit());
            recordList = statisticsWayService.selectRecordList(map);
            form.setPaginator(paginator);
            form.setRecordList(recordList);
        }
        return recordList;
    }


    private void createPage(HttpServletRequest request, ModelAndView modelAndView, StatisticsWayBean form) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("titleName", form.getTitleName());
        map.put("uniqueIdentifier", form.getUniqueIdentifier());
        if(StringUtils.isNotEmpty(form.getStartTime())){
            map.put("timeStar",form.getStartTime()+" 00:00:00");
        }
        if(StringUtils.isNotEmpty(form.getEndTime())){
            map.put("timeEnd",form.getEndTime()+" 23:59:59");
        }
        Integer count = statisticsWayService.countRecordList(map);
        if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            map.put("limitStart", paginator.getOffset());
            map.put("limitEnd", paginator.getLimit());
            List<StatisticsWayConfigureCustomize> recordList = statisticsWayService.selectRecordList(map);
            form.setPaginator(paginator);
            form.setRecordList(recordList);
            modelAndView.addObject(StatisticsWayDefine.FORM, form);
        }
    }

}
