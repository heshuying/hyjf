package com.hyjf.admin.manager.activity.billion.third;

import java.util.Date;
import java.util.List;

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
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.ActivityBillionThird;

/**
 * 助力百亿活动
 * @author Michael
 */
@Controller
@RequestMapping(value = BillionThirdDefine.REQUEST_MAPPING)
public class BillionThirdController extends BaseController {

	@Autowired
	private BillionThirdService billionThirdService;

	 /**
     * 画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(BillionThirdDefine.INIT)
    @RequiresPermissions(BillionThirdDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, BillionThirdBean form) {
        LogUtil.startLog(BillionThirdController.class.toString(), BillionThirdDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(BillionThirdDefine.LIST_PATH);

        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(BillionThirdController.class.toString(), BillionThirdDefine.INIT);
        return modelAndView;
    }

    /**
     * 查询画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(BillionThirdDefine.SEARCH_ACTION)
    @RequiresPermissions(BillionThirdDefine.PERMISSIONS_SEARCH)
    public ModelAndView search(HttpServletRequest request, BillionThirdBean form) {
        LogUtil.startLog(BillionThirdController.class.toString(), BillionThirdDefine.SEARCH_ACTION);
        ModelAndView modelAndView = new ModelAndView(BillionThirdDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(BillionThirdController.class.toString(), BillionThirdDefine.SEARCH_ACTION);
        return modelAndView;
    }

    /**
     * 创建分页机能
     * 
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, BillionThirdBean form) {
        Integer count = this.billionThirdService.selectRecordCount(form);
        if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
            List<ActivityBillionThird> recordList = this.billionThirdService.getRecordList(form,paginator.getOffset(),paginator.getLimit());
            form.setPaginator(paginator);
            form.setRecordList(recordList);
        }
        // 奖励类型
        modelAndView.addObject("billionPrizeTypes",this.billionThirdService.getParamNameList("BILLION_PRIZE_TYPE"));
        
        modelAndView.addObject(BillionThirdDefine.FORM, form);
    }


	/**
     * 数据导出
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(BillionThirdDefine.EXPORT_ACTION)
    @RequiresPermissions(BillionThirdDefine.PERMISSIONS_EXPORT)
    public void exportAction(HttpServletRequest request, HttpServletResponse response, BillionThirdBean form) throws Exception {
        LogUtil.startLog(BillionThirdController.class.toString(), BillionThirdDefine.EXPORT_ACTION);
        // 表格sheet名称
        String sheetName = "十一月份百亿狂欢活动";
        
        List<ActivityBillionThird> resultList = this.billionThirdService.getRecordList(form,-1,-1);

        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

        String[] titles = new String[] { "序号", "用户名", "姓名", "手机号", "获得奖励", "抢券时间", "优惠券编号", "用户优惠券编号"};
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
                	ActivityBillionThird record = resultList.get(i);

                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);

                    // 序号
                    if (celLength == 0) {
                        cell.setCellValue(i + 1);
                    }
                    else if (celLength == 1) {
                    	 cell.setCellValue(StringUtils.isEmpty(record.getUserName()) ? "" : record.getUserName());
                    }
                    else if (celLength == 2) {
                        cell.setCellValue(StringUtils.isEmpty(record.getTrueName()) ? "" : record.getTrueName());
                    }
                    else if (celLength == 3) {
                        cell.setCellValue(StringUtils.isEmpty(record.getMobile()) ? "" : record.getMobile());
                    }
                    else if (celLength == 4) {
                    	 cell.setCellValue(StringUtils.isEmpty(record.getPrizeName()) ? "" : record.getPrizeName());
                    }
                    else if (celLength == 5) {
                    	 cell.setCellValue(record.getCreateTime() == null ? "" : GetDate.timestamptoStrYYYYMMDDHHMMSS(record.getCreateTime()));
                    }
                    else if (celLength == 6) {
                        cell.setCellValue(record.getCouponCode());
                    } 
                    else if (celLength == 7) {
                        cell.setCellValue(record.getCouponId());
                    } 
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
        LogUtil.endLog(BillionThirdController.class.toString(), BillionThirdDefine.EXPORT_ACTION);
    }
    
    
}
