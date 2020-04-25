package com.hyjf.admin.finance.couponrepaymonitor;

import java.math.BigDecimal;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.customize.admin.AdminCouponRepayMonitorCustomize;

/**
 * 
 * 此处为类说明
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年7月12日
 * @see 下午4:58:59
 */
@Controller
@RequestMapping(value = CouponRepayDefine.REQUEST_MAPPING)
public class CouponRepayController extends BaseController {

	@Autowired
	private CouponRepayService couponRepayService;

    @RequestMapping(CouponRepayDefine.CHART)
    @RequiresPermissions(CouponRepayDefine.PERMISSIONS_VIEW)
    public ModelAndView chart(HttpServletRequest request, CouponRepayBean form) {
        LogUtil.startLog(CouponRepayController.class.toString(), CouponRepayDefine.CHART);
        ModelAndView modelAndView = new ModelAndView(CouponRepayDefine.CHART_PATH);
        Map<String,Object> paraMap = new HashMap<String,Object>();
        paraMap.put("limitStart", 0);
        paraMap.put("limitEnd", 1);
        List<AdminCouponRepayMonitorCustomize> recordList = this.couponRepayService.selectRecordList(paraMap);
        if(recordList != null && !recordList.isEmpty()){
            modelAndView.addObject("latestUpdateTime", recordList.get(0).getUpdateTime());
        }else {
            modelAndView.addObject("latestUpdateTime", "");
        }
        LogUtil.endLog(CouponRepayController.class.toString(), CouponRepayDefine.CHART);
        return modelAndView;
    }
    
    @ResponseBody
    @RequestMapping(value = CouponRepayDefine.REPAY_STATISTIC_ACTION, produces = "application/json; charset=UTF-8")
    public String repayStatisticAction(HttpServletRequest request, HttpServletResponse response){
        LogUtil.startLog(CouponRepayController.class.toString(), CouponRepayDefine.REPAY_STATISTIC_ACTION);
        List<AdminCouponRepayMonitorCustomize> recordList = this.couponRepayService.selectRecordList(new HashMap<String,Object>());
        String[] days;
        BigDecimal[] moneyWaitSum;
        BigDecimal[] moneyYesSum;
        if(recordList != null && !recordList.isEmpty()){
            days = new String[recordList.size()];
            moneyWaitSum = new BigDecimal[recordList.size()];
            moneyYesSum = new BigDecimal[recordList.size()];
            int j = 0;
            for(int i=recordList.size()-1; i>= 0; i--){
                days[j] = recordList.get(i).getDay();
                moneyWaitSum[j] = recordList.get(i).getInterestWaitTotal();
                moneyYesSum[j] = recordList.get(i).getInterestYesTotal();
                j++;
            }
        }else {
            days = new String[0];
            moneyWaitSum = new BigDecimal[0];
            moneyYesSum = new BigDecimal[0];
        }
        
      //返回结果
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("days", days);
        map.put("moneyWaitSum", moneyWaitSum);
        map.put("moneyYesSum", moneyYesSum);
        String ret = JSON.toJSONString(map);
        LogUtil.endLog(CouponRepayController.class.toString(), CouponRepayDefine.REPAY_STATISTIC_ACTION);
        return ret;
    }
	   
	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(CouponRepayDefine.INIT)
	@RequiresPermissions(CouponRepayDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, CouponRepayBean form) {
		LogUtil.startLog(CouponRepayController.class.toString(), CouponRepayDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(CouponRepayDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(CouponRepayController.class.toString(), CouponRepayDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(CouponRepayDefine.SEARCH_ACTION)
	@RequiresPermissions(CouponRepayDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, CouponRepayBean form) {
		LogUtil.startLog(CouponRepayController.class.toString(), CouponRepayDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(CouponRepayDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(CouponRepayController.class.toString(), CouponRepayDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, CouponRepayBean form) {

	    Map<String, Object> paraMap = new HashMap<String, Object>();
	    if(StringUtils.isNotEmpty(form.getTimeStartSrch())){
	        paraMap.put("timeStartSrch", form.getTimeStartSrch());
	    }
	    if(StringUtils.isNotEmpty(form.getTimeEndSrch())){
            paraMap.put("timeEndSrch", form.getTimeEndSrch());
        }
		Integer count = this.couponRepayService.countRecordTotal(paraMap);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			paraMap.put("limitStart", paginator.getOffset());
			paraMap.put("limitEnd", paginator.getLimit());
			List<AdminCouponRepayMonitorCustomize> recordList = this.couponRepayService.selectRecordList(paraMap);
			form.setPaginator(paginator);
			List<AdminCouponRepayMonitorCustomize> recordListSum = this.couponRepayService.selectInterestSum(paraMap);
			if(recordListSum != null && !recordListSum.isEmpty() && recordListSum.get(0) != null){
		        modelAndView.addObject("interestWaitSum", recordListSum.get(0).getInterestWaitTotalAll());
		        modelAndView.addObject("interestYesSum", recordListSum.get(0).getInterestYesTotalAll());
			    
			}
			
			//计算最后一次更新时间
	        modelAndView.addObject("latestUpdateTime", recordList.get(0).getUpdateTime());
			modelAndView.addObject("recordList", recordList);
		}else {
		    modelAndView.addObject("latestUpdateTime", "");
		}

		modelAndView.addObject(CouponRepayDefine.COUPON_FORM, form);
	}

	/**
     * 数据导出
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(CouponRepayDefine.EXPORT_ACTION)
    @RequiresPermissions(CouponRepayDefine.PERMISSIONS_EXPORT)
    public void exportAction(HttpServletRequest request, HttpServletResponse response, CouponRepayBean form) throws Exception {
        LogUtil.startLog(CouponRepayController.class.toString(), CouponRepayDefine.EXPORT_ACTION);
        // 表格sheet名称
        String sheetName = "优惠券还款监测";

        Map<String, Object> paraMap = new HashMap<String, Object>();
        if(StringUtils.isNotEmpty(form.getTimeStartSrch())){
            paraMap.put("timeStartSrch", form.getTimeStartSrch());
        }
        if(StringUtils.isNotEmpty(form.getTimeEndSrch())){
            paraMap.put("timeEndSrch", form.getTimeEndSrch());
        }
        
        List<AdminCouponRepayMonitorCustomize> resultList = this.couponRepayService.selectRecordList(paraMap);

        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

        String[] titles = new String[] { "序号", "日期", "星期", "加息券待还统计", "加息券实际还款", "差额（实际-预测）"};
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
                    AdminCouponRepayMonitorCustomize record = resultList.get(i);

                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);

                    // 序号
                    if (celLength == 0) {
                        cell.setCellValue(i + 1);
                    }
                    // 日期
                    else if (celLength == 1) {
                        cell.setCellValue(record.getDay());
                    }
                    // 星期
                    else if (celLength == 2) {
                        cell.setCellValue(record.getWeek());
                    }
                    // 加息券待还收益
                    else if (celLength == 3) {
                        cell.setCellValue(String.valueOf(record.getInterestWaitTotal()));
                    }
                    // 加息券已还收益
                    else if (celLength == 4) {
                        cell.setCellValue(String.valueOf(record.getInterestYesTotal()));
                    }
                    // 差额
                    else if (celLength == 5) {
                        cell.setCellValue(String.valueOf(record.getRepayGap()));
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
        LogUtil.endLog(CouponRepayController.class.toString(), CouponRepayDefine.EXPORT_ACTION);
    }
	
}
