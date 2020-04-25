package com.hyjf.admin.finance.bindlog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.DirectionalTransferAssociatedLog;

@Controller
@RequestMapping(BindlogDefine.REQUEST_MAPPING)
public class BindlogController extends BaseController {
    @Autowired
    private BindlogService bindlogService;

    /**
     * 关联日志列表
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(BindlogDefine.BIND_LOG_LIST)
    @RequiresPermissions(value = { BindlogDefine.PERMISSIONS_VIEW, BindlogDefine.PERMISSIONS_SEARCH }, logical = Logical.OR)
    public ModelAndView init(HttpServletRequest request, BindlogBean form) {
        LogUtil.startLog(BindlogDefine.THIS_CLASS, BindlogDefine.BIND_LOG_LIST);
        ModelAndView modelAndView = new ModelAndView(BindlogDefine.BIND_LOG_LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(BindlogDefine.THIS_CLASS, BindlogDefine.BIND_LOG_LIST);
        return modelAndView;
    }

    /**
     * 关联日志分页
     * 
     * @param request
     * @param modeAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modeAndView, BindlogBean form) {
        int total = this.bindlogService.countRecordTotal(form);
        if (total > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), total);
            List<DirectionalTransferAssociatedLog> resultList =
                    this.bindlogService.getRecordList(form, paginator.getOffset(), paginator.getLimit());
            form.setRecordList(resultList);
            form.setPaginator(paginator);
        }

        modeAndView.addObject(BindlogDefine.BIND_LOG_FORM, form);
    }

    /**
     * 关联日志列表导出
     * 
     * @param form
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(BindlogDefine.BIND_LOG_EXPORT)
    @RequiresPermissions(BindlogDefine.PERMISSIONS_EXPORT)
    public void exportDirectionalTransferListExcel(@ModelAttribute BindlogBean form, HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        LogUtil.startLog(BindlogDefine.THIS_CLASS, BindlogDefine.BIND_LOG_EXPORT);
        // 表格sheet名称
        String sheetName = "关联日志列表";
        // 文件名称
        String fileName =
                sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

        // 检索列表
        List<DirectionalTransferAssociatedLog> resultList = this.bindlogService.getRecordList(form, -1, -1);
        String[] titles =
                new String[] { "序号", "转出账户", "转出账户手机", "转出账户客户号", "转入账户", "转入账户手机", "转入账户客户号", "关联状态", "关联时间", "说明" };
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
                    sheet =
                            ExportExcel
                                    .createHSSFWorkbookTitle(workbook, titles, (sheetName + "_第" + sheetCount + "页"));
                    rowNum = 1;
                }
                // 新建一行
                Row row = sheet.createRow(rowNum);

                // 循环数据
                for (int celLength = 0; celLength < titles.length; celLength++) {
                    DirectionalTransferAssociatedLog directionalTransferAssociatedLog = resultList.get(i);
                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);
                    if (celLength == 0) {// 序号
                        cell.setCellValue(i + 1);
                    } else if (celLength == 1) { // 转出账户
                        cell.setCellValue(directionalTransferAssociatedLog.getTurnOutUsername());
                    } else if (celLength == 2) { // 转出账户手机
                        cell.setCellValue(directionalTransferAssociatedLog.getTurnOutMobile());
                    } else if (celLength == 3) { // 转出账户客户号
                        cell.setCellValue(String.valueOf(directionalTransferAssociatedLog.getTurnOutChinapnrUsrcustid()));
                    } else if (celLength == 4) {// 转入账户
                        cell.setCellValue(directionalTransferAssociatedLog.getShiftToUsername());
                    } else if (celLength == 5) {// 转入账户手机
                        cell.setCellValue(directionalTransferAssociatedLog.getShiftToMobile());
                    } else if (celLength == 6) {// 转入账户客户号
                        cell.setCellValue(String.valueOf(directionalTransferAssociatedLog.getShiftToChinapnrUsrcustid()));
                    } else if (celLength == 7) {// 关联状态
                        if (directionalTransferAssociatedLog.getAssociatedState() == 0) {
                            cell.setCellValue("未授权");
                        } else if (directionalTransferAssociatedLog.getAssociatedState() == 1) {
                            cell.setCellValue("成功");
                        } else if (directionalTransferAssociatedLog.getAssociatedState() == 2) {
                            cell.setCellValue("失败");
                        }
                    } else if (celLength == 8) {// 关联时间
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        cell.setCellValue(df.format(directionalTransferAssociatedLog.getAssociatedTime()));
                    } else if (celLength == 9) {// 说明
                        cell.setCellValue(directionalTransferAssociatedLog.getRemark());
                    }
                }
            }
        }

        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);

        LogUtil.endLog(BindlogDefine.THIS_CLASS, BindlogDefine.BIND_LOG_EXPORT);
    }
}
