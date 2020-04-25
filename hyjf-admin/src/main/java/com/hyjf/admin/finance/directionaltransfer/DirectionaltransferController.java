package com.hyjf.admin.finance.directionaltransfer;

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
import com.hyjf.mybatis.model.auto.AccountDirectionalTransfer;

@Controller
@RequestMapping(value = DirectionaltransferDefine.REQUEST_MAPPING)
public class DirectionaltransferController extends BaseController {

    @Autowired
    private DirectionaltransferService directionaltransferService;

    /**
     * 定向转账列表
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(DirectionaltransferDefine.DIRECTIONAL_TRANSFER_LIST)
    @RequiresPermissions(value = { DirectionaltransferDefine.PERMISSIONS_VIEW,
            DirectionaltransferDefine.PERMISSIONS_SEARCH }, logical = Logical.OR)
    public ModelAndView init(HttpServletRequest request, DirectionaltransferBean form) {
        LogUtil.startLog(DirectionaltransferDefine.THIS_CLASS, DirectionaltransferDefine.DIRECTIONAL_TRANSFER_LIST);
        ModelAndView modelAndView = new ModelAndView(DirectionaltransferDefine.DIRECTIONAL_TRANSFER_LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(DirectionaltransferDefine.THIS_CLASS, DirectionaltransferDefine.DIRECTIONAL_TRANSFER_LIST);
        return modelAndView;
    }

    /**
     * 定向转账分页
     * 
     * @param request
     * @param modeAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modeAndView, DirectionaltransferBean form) {
        int total = this.directionaltransferService.countRecordTotal(form);
        if (total > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), total);
            List<AccountDirectionalTransfer> resultList =
                    this.directionaltransferService.getRecordList(form, paginator.getOffset(), paginator.getLimit());
            form.setAccountDirectionalTransferList(resultList);
            form.setPaginator(paginator);
        }

        modeAndView.addObject(DirectionaltransferDefine.DIRECTIONAL_TRANSFER_FORM, form);
    }

    /**
     * 定向转账列表导出
     * 
     * @param form
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(DirectionaltransferDefine.DIRECTIONAL_TRANSFER_EXPORT)
    @RequiresPermissions(DirectionaltransferDefine.PERMISSIONS_EXPORT)
    public void exportDirectionalTransferListExcel(@ModelAttribute DirectionaltransferBean form,
        HttpServletRequest request, HttpServletResponse response) throws Exception {
        LogUtil.startLog(DirectionaltransferDefine.THIS_CLASS, DirectionaltransferDefine.DIRECTIONAL_TRANSFER_EXPORT);
        // 表格sheet名称
        String sheetName = "定向转账列表";
        // 文件名称
        String fileName =
                sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

        // 检索列表
        List<AccountDirectionalTransfer> resultList = this.directionaltransferService.getRecordList(form, -1, -1);
        String[] titles = new String[] { "序号", "转出账户", "转入账户", "转账订单号", "转账金额", "转账状态", "转账时间", "说明" };
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
                    AccountDirectionalTransfer accountDirectionalTransfer = resultList.get(i);
                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);
                    if (celLength == 0) {// 序号
                        cell.setCellValue(i + 1);
                    } else if (celLength == 1) { // 转出账户
                        cell.setCellValue(accountDirectionalTransfer.getTurnOutUsername());
                    } else if (celLength == 2) { // 转入账户
                        cell.setCellValue(accountDirectionalTransfer.getShiftToUsername());
                    } else if (celLength == 3) { // 转账订单号
                        cell.setCellValue(accountDirectionalTransfer.getOrderId());
                    } else if (celLength == 4) {// 转账金额
                        cell.setCellValue(String.valueOf(accountDirectionalTransfer.getTransferAccountsMoney()));
                    } else if (celLength == 5) {// 转账状态
                        if (accountDirectionalTransfer.getTransferAccountsState() == 0) {
                            cell.setCellValue("转账中");
                        } else if (accountDirectionalTransfer.getTransferAccountsState() == 1) {
                            cell.setCellValue("成功");
                        } else if (accountDirectionalTransfer.getTransferAccountsState() == 2) {
                            cell.setCellValue("失败");
                        }
                    } else if (celLength == 6) {// 转账时间
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        cell.setCellValue(df.format(accountDirectionalTransfer.getTransferAccountsTime()));
                    } else if (celLength == 7) {// 说明
                        cell.setCellValue(accountDirectionalTransfer.getRemark());
                    }
                }
            }
        }

        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);

        LogUtil.endLog(DirectionaltransferDefine.THIS_CLASS, DirectionaltransferDefine.DIRECTIONAL_TRANSFER_EXPORT);
    }
}
