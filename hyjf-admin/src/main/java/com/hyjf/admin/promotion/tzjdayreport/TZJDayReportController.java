package com.hyjf.admin.promotion.tzjdayreport;

import java.text.DecimalFormat;
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

import com.alibaba.fastjson.JSON;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.coupon.config.CouponConfigController;
import com.hyjf.admin.coupon.config.CouponConfigDefine;
import com.hyjf.admin.coupon.user.CouponUserBean;
import com.hyjf.admin.coupon.user.CouponUserController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.StatisticsTzj;
import com.hyjf.mybatis.model.auto.StatisticsTzjHour;

/**
 * 
 * 投之家日统计报表
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年7月12日
 * @see 下午4:58:59
 */
@Controller
@RequestMapping(value = TZJDayReportDefine.REQUEST_MAPPING)
public class TZJDayReportController extends BaseController {
    // 统计开始时间 2017-03-27 00:00:00
    public static final String timeStart = "1490544000";
    // 统计结束时间
    public static final String timeEnd = String.valueOf(GetDate.getDayStart10(new Date()));
    
	@Autowired
	private TZJDayReportService tzjDayReportService;

    @RequestMapping(TZJDayReportDefine.CHART)
    @RequiresPermissions(TZJDayReportDefine.PERMISSIONS_VIEW)
    public ModelAndView initChart(HttpServletRequest request, TZJDayReportBean form) {
        LogUtil.startLog(TZJDayReportController.class.toString(), TZJDayReportDefine.CHART);
        ModelAndView modelAndView = new ModelAndView(TZJDayReportDefine.CHART_PATH);

        // 创建分页
        this.getChartData(request, modelAndView, form);
        LogUtil.endLog(TZJDayReportController.class.toString(), TZJDayReportDefine.CHART);
        return modelAndView;
    }
    
    public void getChartData(HttpServletRequest request, ModelAndView modelAndView, TZJDayReportBean form){
        LogUtil.startLog(TZJDayReportController.class.toString(), TZJDayReportDefine.STATISTICS_CHART_DATA_ACTION);
        
        String[] days;
        int[] registCount;
        int[] tenderFirstCount;
        
        Map<String, Object> dayMap = new HashMap<String, Object>();
        dayMap.put("timeStartSrch", form.getTimeStartSrch());
        dayMap.put("timeEndSrch", form.getTimeEndSrch());
        List<StatisticsTzj> dayList = tzjDayReportService.selectDayList(dayMap);
        
        Map<String, Object> hourMap = new HashMap<String, Object>();
        hourMap.put("timeStartSrch", form.getTimeStartSrch());
        hourMap.put("timeEndSrch", form.getTimeEndSrch());
        List<StatisticsTzjHour> hourList = tzjDayReportService.selectHourList(hourMap);
        
        String dayUpdateTime = tzjDayReportService.selectDayUpdateTime();
        String hourUpdateTime = tzjDayReportService.selectHourUpdateTime();
        
        if(StringUtils.isEmpty(form.getTimeStartSrch()) || StringUtils.isEmpty(form.getTimeEndSrch())){
            days = new String[dayList.size()];
            registCount = new int[dayList.size()];
            tenderFirstCount = new int[dayList.size()];
            
            for(int i=0; i<dayList.size(); i++){
                days[i] = dayList.get(i).getDay();
                registCount[i] = dayList.get(i).getRegistCount();
                tenderFirstCount[i] = dayList.get(i).getTenderfirstCount();
            }
            
            modelAndView.addObject("chartTitle", "日统计图表");
            modelAndView.addObject("latestUpdateTime", dayUpdateTime);
        }else if(form.getTimeStartSrch().equals(form.getTimeEndSrch())){
            days = new String[hourList.size()];
            registCount = new int[hourList.size()];
            tenderFirstCount = new int[hourList.size()];
            
            for(int i=0; i<hourList.size(); i++){
                days[i] = hourList.get(i).getHour();
                registCount[i] = hourList.get(i).getRegistCount();
                tenderFirstCount[i] = hourList.get(i).getTenderfirstCount();
            }
            
            modelAndView.addObject("chartTitle", "小时统计图表（" + form.getTimeStartSrch() + "）");
            modelAndView.addObject("latestUpdateTime", hourUpdateTime);
        }else {
            days = new String[dayList.size()];
            registCount = new int[dayList.size()];
            tenderFirstCount = new int[dayList.size()];
            
            for(int i=0; i<dayList.size(); i++){
                days[i] = dayList.get(i).getDay();
                registCount[i] = dayList.get(i).getRegistCount();
                tenderFirstCount[i] = dayList.get(i).getTenderfirstCount();
            }
            
            modelAndView.addObject("chartTitle", "日统计图表");
            modelAndView.addObject("chartTitle", "日统计图表");
            modelAndView.addObject("latestUpdateTime", dayUpdateTime);
        }
        
        modelAndView.addObject("days", JSON.toJSONString(days));
        modelAndView.addObject("registCount", JSON.toJSONString(registCount));
        modelAndView.addObject("tenderFirstCount", JSON.toJSONString(tenderFirstCount));
        LogUtil.endLog(TZJDayReportController.class.toString(), TZJDayReportDefine.STATISTICS_CHART_DATA_ACTION);
    }

	   
	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(TZJDayReportDefine.INIT)
	@RequiresPermissions(TZJDayReportDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, TZJDayReportBean form) {
		LogUtil.startLog(TZJDayReportController.class.toString(), TZJDayReportDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(TZJDayReportDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(TZJDayReportController.class.toString(), TZJDayReportDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(TZJDayReportDefine.SEARCH_ACTION)
	@RequiresPermissions(TZJDayReportDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, TZJDayReportBean form) {
		LogUtil.startLog(TZJDayReportController.class.toString(), TZJDayReportDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(TZJDayReportDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(TZJDayReportController.class.toString(), TZJDayReportDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, TZJDayReportBean form) {
		DecimalFormat dataFormat = new DecimalFormat("##0.00");
		
        Map<String, Object> paraMap = new HashMap<String, Object>();
        if(StringUtils.isNotEmpty(form.getTimeStartSrch())){
            paraMap.put("timeStartSrch", form.getTimeStartSrch());
        }
        if(StringUtils.isNotEmpty(form.getTimeEndSrch())){
            paraMap.put("timeEndSrch", form.getTimeEndSrch());
        }
        
        Map<String, Object> paraMapTotal = new HashMap<String, Object>();
        paraMapTotal.put("timeStartSrch", timeStart);
        paraMapTotal.put("timeEndSrch", timeEnd);
        
        // 出借总人数
        Integer tenderCountTotal = tzjDayReportService.getTenderCountTotal(paraMapTotal);
        // 出借总额
        double tenderMoneyTotal = tzjDayReportService.getTenderMoneyTotal(paraMapTotal);
        
        Integer count = tzjDayReportService.countRecordTotal(paraMap);
        if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
            paraMap.put("limitStart", paginator.getOffset());
            paraMap.put("limitEnd", paginator.getLimit());
            List<StatisticsTzj> recordList = tzjDayReportService.selectRecordList(paraMap);
            form.setPaginator(paginator);
            
            //计算最后一次更新时间
            modelAndView.addObject("latestUpdateTime", recordList.get(0).getUpdateTime());
            modelAndView.addObject("recordList", recordList);
        }else {
            modelAndView.addObject("latestUpdateTime", "");
        }

        modelAndView.addObject("tenderCountTotal", tenderCountTotal);
        modelAndView.addObject("tenderMoneyTotal", dataFormat.format(tenderMoneyTotal));
        modelAndView.addObject(TZJDayReportDefine.TZJ_DAYREPORT_FORM, form);

	}

    /**
     * 导出EXCEL
     * @param request
     * @param modelAndView
     * @param form
     */
    @RequestMapping(TZJDayReportDefine.EXPORT_ACTION)
    @RequiresPermissions(TZJDayReportDefine.PERMISSIONS_EXPORT)
    public void exportAction(HttpServletRequest request, HttpServletResponse response, CouponUserBean form) throws Exception {
        LogUtil.startLog(CouponUserController.class.toString(), TZJDayReportDefine.EXPORT_ACTION);
        // 表格sheet名称
        String sheetName = "投之家日报表";
        
        Map<String, Object> paraMap = new HashMap<String, Object>();
        if(StringUtils.isNotEmpty(form.getTimeStartSrch())){
            paraMap.put("timeStartSrch", form.getTimeStartSrch());
        }
        if(StringUtils.isNotEmpty(form.getTimeEndSrch())){
            paraMap.put("timeEndSrch", form.getTimeEndSrch());
        }
        
        List<StatisticsTzj> resultList  = tzjDayReportService.selectRecordList(paraMap);
        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
        String[] titles = new String[] {"序号", "日期","注册","开户","开户比", "绑卡","绑卡比","新充人数","新投人数","新投转化率", "充值人数", "出借人数", "出借额", "首投人数","首投金额", "复投人数", "复投率" };
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
                    StatisticsTzj record = resultList.get(i);

                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);
                    // 序号
                    if (celLength == 0) {
                        cell.setCellValue(i + 1);
                    }
                    else if (celLength == 1) {
                        cell.setCellValue(record.getDay());
                    }
                    else if (celLength == 2) {
                        cell.setCellValue(record.getRegistCount());
                    }
                    else if (celLength == 3) {
                        cell.setCellValue(record.getOpenCount());
                    }
                    else if (celLength == 4) {
                        cell.setCellValue(record.getOpenRate().doubleValue() + "%");
                    }
                    else if (celLength == 5) {
                        cell.setCellValue(record.getCardbindCount());
                    }
                    else if (celLength == 6) {
                        cell.setCellValue(record.getCardbindRate().doubleValue() + "%");
                    }
                    else if (celLength == 7) {
                        cell.setCellValue(record.getRechargenewCount());
                    }
                    else if (celLength == 8) {
                        cell.setCellValue(record.getTendernewCount());
                    }
                    else if (celLength == 9) {
                        cell.setCellValue(record.getTendernewRate().doubleValue() + "%");
                    }
                    else if (celLength == 10) {
                        cell.setCellValue(record.getRechargeCount());
                    }
                    else if (celLength == 11) {
                        cell.setCellValue(record.getTenderCount());
                    }
                    else if (celLength == 12) {
                        cell.setCellValue(record.getTenderMoney().doubleValue());
                    }
                    else if (celLength == 13) {
                        cell.setCellValue(record.getTenderfirstCount());
                    }
                    else if (celLength == 14) {
                        cell.setCellValue(record.getTenderfirstMoney().doubleValue());
                    }
                    else if (celLength == 15) {
                        cell.setCellValue(record.getTenderAgainCount());
                    }
                    else if (celLength == 16) {
                        cell.setCellValue(record.getTenderAgainRate().doubleValue() + "%");
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);

        LogUtil.endLog(CouponConfigController.class.toString(), CouponConfigDefine.EXPORT_ACTION);
    }

	
}
