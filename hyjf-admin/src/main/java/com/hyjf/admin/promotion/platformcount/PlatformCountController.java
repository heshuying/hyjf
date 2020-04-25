package com.hyjf.admin.promotion.platformcount;

import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.borrow.borrow.BorrowBean;
import com.hyjf.admin.promotion.channelcount.ChannelCountBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.customize.PlatformCountCustomize;

/**
 * @author GOGTZ-Z
 * @version V1.0  
 * @package com.hyjf.admin.promotion.platformcount
 * @date 2015/07/09 17:00
 */
@Controller
@RequestMapping(value = PlatformCountDefine.REQUEST_MAPPING)
public class PlatformCountController extends BaseController {

    @Autowired
    private PlatformCountService platformCountService;

    /**
     * 画面初始化
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(PlatformCountDefine.INIT)
    @RequiresPermissions(PlatformCountDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, @ModelAttribute(PlatformCountDefine.FORM) ChannelCountBean form) {
        LogUtil.startLog(PlatformCountController.class.toString(), PlatformCountDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(PlatformCountDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(PlatformCountController.class.toString(), PlatformCountDefine.INIT);
        return modelAndView;
    }

    /**
     * 画面初始化
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(PlatformCountDefine.SEARCH_ACTION)
    @RequiresPermissions(PlatformCountDefine.PERMISSIONS_SEARCH)
    public ModelAndView search(HttpServletRequest request, ChannelCountBean form) {
        LogUtil.startLog(PlatformCountController.class.toString(), PlatformCountDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(PlatformCountDefine.LIST_PATH);

        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(PlatformCountController.class.toString(), PlatformCountDefine.INIT);
        return modelAndView;
    }

    /**
     * 创建分页机能
     *
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, ChannelCountBean form) {

        PlatformCountCustomize platformCountCustomize = new PlatformCountCustomize();
        // 添加时间
        platformCountCustomize.setTimeStartSrch(form.getTimeStartSrch());
        // 添加时间
        platformCountCustomize.setTimeEndSrch(form.getTimeEndSrch());

        Integer count = this.platformCountService.countList(platformCountCustomize);
        if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
            platformCountCustomize.setLimitStart(paginator.getOffset());
            platformCountCustomize.setLimitEnd(paginator.getLimit());
            List<PlatformCountCustomize> recordList = this.platformCountService.getRecordList(platformCountCustomize);
            form.setPaginator(paginator);
            modelAndView.addObject("recordList", recordList);
        }
        modelAndView.addObject(PlatformCountDefine.FORM, form);
    }

    /**
     * 导出功能
     *
     * @param request
     * @param response
     * @param form
     */
    @RequestMapping(PlatformCountDefine.EXPORT_ACTION)
    @RequiresPermissions(PlatformCountDefine.PERMISSIONS_EXPORT)
    public void exportAction(HttpServletRequest request, HttpServletResponse response, BorrowBean form) throws Exception {
        LogUtil.startLog(PlatformCountController.class.toString(), PlatformCountDefine.EXPORT_ACTION);
        // 表格sheet名称
        String sheetName = "平台统计";

        PlatformCountCustomize platformCountCustomize = new PlatformCountCustomize();
        // 添加时间
        platformCountCustomize.setTimeStartSrch(form.getTimeStartSrch());
        // 添加时间
        platformCountCustomize.setTimeEndSrch(form.getTimeEndSrch());

        List<PlatformCountCustomize> recordList = this.platformCountService.exportList(platformCountCustomize);

        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

        //解决IE浏览器导出列表中文乱码问题
        String userAgent = request.getHeader("user-agent").toLowerCase();
        if (userAgent.contains("msie") || userAgent.contains("like gecko")) {
            // win10 ie edge 浏览器 和其他系统的ie
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } else {
            fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
        }

        String[] titles = new String[]{"序号", "平台", "访问数", "注册数", "开户数", "出借人数", "累计充值", "累计出借", "汇直投出借金额", "汇消费出借金额", "汇天利出借金额", "汇添金出借金额", "智投服务出借金额", "汇转让出借金额"};
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
                    PlatformCountCustomize record = recordList.get(i);

                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);

                    // 序号
                    if (celLength == 0) {
                        cell.setCellValue(i + 1);
                    }
                    // 平台
                    else if (celLength == 1) {
                        cell.setCellValue(record.getSourceName());
                    }
                    // 访问数
                    else if (celLength == 2) {
                        cell.setCellValue(record.getAccessNumber());
                    }
                    // 注册数
                    else if (celLength == 3) {
                        cell.setCellValue(record.getRegistNumber());
                    }
                    // 开户数
                    else if (celLength == 4) {
                        cell.setCellValue(record.getAccountNumber());
                    }
                    // 出借人数
                    else if (celLength == 5) {
                        cell.setCellValue(record.getTenderNumber());
                    }
                    // 累计充值
                    else if (celLength == 6) {
                        cell.setCellValue(record.getRechargePrice());
                    }
                    // 累计出借
                    else if (celLength == 7) {
                        cell.setCellValue(record.getTenderPrice());
                    }
                    // 汇直投出借金额
                    else if (celLength == 8) {
                        cell.setCellValue(record.getHztTenderPrice());
                    }
                    // 汇消费出借金额
                    else if (celLength == 9) {
                        cell.setCellValue(record.getHxfTenderPrice());
                    }
                    // 汇天利出借金额
                    else if (celLength == 10) {
                        cell.setCellValue(record.getHtlTenderPrice());
                    }
                    // 汇添金出借金额
                    else if (celLength == 11) {
                        cell.setCellValue(record.getHtjTenderPrice());
                    }
                    // 汇计划出借金额
                    else if (celLength == 12) {
                        cell.setCellValue(record.getHjhTenderPrice());
                    }
                    // 汇转让出借金额
                    else if (celLength == 13) {
                        cell.setCellValue(record.getHzrTenderPrice());
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);

        LogUtil.endLog(PlatformCountController.class.toString(), PlatformCountDefine.EXPORT_ACTION);
    }
}
