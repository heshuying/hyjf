package com.hyjf.admin.manager.activity.billion.one;

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
import com.hyjf.mybatis.model.auto.ActivityBillionOne;

/**
 * 满心满亿活动
 * @author Michael
 */
@Controller
@RequestMapping(value = BillionOneDefine.REQUEST_MAPPING)
public class BillionOneController extends BaseController {

	@Autowired
	private BillionOneService billionOneService;

	 /**
     * 画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(BillionOneDefine.INIT)
    @RequiresPermissions(BillionOneDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, BillionOneBean form) {
        LogUtil.startLog(BillionOneController.class.toString(), BillionOneDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(BillionOneDefine.LIST_PATH);

        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(BillionOneController.class.toString(), BillionOneDefine.INIT);
        return modelAndView;
    }

    /**
     * 查询画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(BillionOneDefine.SEARCH_ACTION)
    @RequiresPermissions(BillionOneDefine.PERMISSIONS_SEARCH)
    public ModelAndView searchAction(HttpServletRequest request, BillionOneBean form) {
        LogUtil.startLog(BillionOneController.class.toString(), BillionOneDefine.SEARCH_ACTION);
        ModelAndView modelAndView = new ModelAndView(BillionOneDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(BillionOneController.class.toString(), BillionOneDefine.SEARCH_ACTION);
        return modelAndView;
    }

    /**
     * 创建分页机能
     * 
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, BillionOneBean form) {
        Integer count = this.billionOneService.selectRecordCount(form);
        if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
            List<ActivityBillionOne> recordList = this.billionOneService.getRecordList(form,paginator.getOffset(),paginator.getLimit());
            form.setPaginator(paginator);
            form.setRecordList(recordList);
        }
        modelAndView.addObject(BillionOneDefine.FORM, form);
    }


	/**
     * 数据导出
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(BillionOneDefine.EXPORT_ACTION)
    @RequiresPermissions(BillionOneDefine.PERMISSIONS_EXPORT)
    public void exportAction(HttpServletRequest request, HttpServletResponse response, BillionOneBean form) throws Exception {
        LogUtil.startLog(BillionOneController.class.toString(), BillionOneDefine.EXPORT_ACTION);
        // 表格sheet名称
        String sheetName = "十一月份满心满亿活动";
        
        List<ActivityBillionOne> resultList = this.billionOneService.getRecordList(form,-1,-1);

        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

        String[] titles = new String[] { "序号", "满百金额", "用户名", "姓名", "手机号", "出借时间", "出借金额", "出借类型", "项目编号", "奖励名称"};
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
                	ActivityBillionOne record = resultList.get(i);

                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);

                    // 序号
                    if (celLength == 0) {
                        cell.setCellValue(i + 1);
                    }
                    else if (celLength == 1) {
                        cell.setCellValue(record.getAccordMoney()+"亿");
                    }
                    else if (celLength == 2) {
                    	 cell.setCellValue(StringUtils.isEmpty(record.getUserName()) ? "" : record.getUserName());
                    }
                    else if (celLength == 3) {
                        cell.setCellValue(StringUtils.isEmpty(record.getUserName()) ? "" : record.getUserName());
                    }
                    else if (celLength == 4) {
                        cell.setCellValue(StringUtils.isEmpty(record.getMobile()) ? "" : record.getMobile());
                    }
                    else if (celLength == 5) {
                        cell.setCellValue(record.getTenderTime() == null ? "": GetDate.timestamptoStrYYYYMMDDHHMMSS(record.getTenderTime()));
                    }
                    else if (celLength == 6) {
                        cell.setCellValue(record.getTenderMoney() == null ? "" :record.getTenderMoney().toString());
                    }
                    else if (celLength == 7) {
                        cell.setCellValue(record.getProjectTypeName());
                    } 
                    else if (celLength == 8) {
                        cell.setCellValue(record.getBorrowNid());
                    } 
                    else if (celLength == 9) {
                        cell.setCellValue(record.getPrizeName());
                    } 
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
        LogUtil.endLog(BillionOneController.class.toString(), BillionOneDefine.EXPORT_ACTION);
    }
    
    
}
