package com.hyjf.admin.manager.activity.worldcupactivity;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.customize.worldcup.GuessingActivitieCustomize;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author xiehuili on 2018/6/13.
 * 世界杯活动
 */
@Controller
@RequestMapping(value = WorldCupActivityDefine.REQUEST_MAPPING)
public class WorldCupActivityController extends BaseController {

    @Autowired
    private WorldCupActivityService worldCupActivityService;

    /**
     * （竞猜输赢）列表初始化
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(WorldCupActivityDefine.INIT)
    @RequiresPermissions(WorldCupActivityDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,
                             @ModelAttribute(WorldCupActivityDefine.FORM) WorldCupActivityBean form) {
        LogUtil.startLog(WorldCupActivityDefine.class.toString(), WorldCupActivityDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(WorldCupActivityDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(WorldCupActivityDefine.class.toString(), WorldCupActivityDefine.INIT);
        return modelAndView;
    }


    /**
     * （竞猜输赢）分页查询
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, WorldCupActivityBean form) {
        Integer count = this.worldCupActivityService.countRecordBySearchCon(form);
        if (count.intValue() > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            List<GuessingActivitieCustomize>  recordList = this.worldCupActivityService.getRecordList(form, paginator.getOffset(),paginator.getLimit());
            form.setPaginator(paginator);
            form.setRecordList(recordList);
            //返回页面数据
            modelAndView.addObject(WorldCupActivityDefine.FORM, form);
        }

    }

    /**
     * 竞猜冠军----列表初始化
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(WorldCupActivityDefine.WINNING_INIT)
    @RequiresPermissions(WorldCupActivityDefine.PERMISSIONS_VIEW)
    public ModelAndView championInit(HttpServletRequest request, RedirectAttributes attr,
                             @ModelAttribute(WorldCupActivityDefine.FORM) WorldCupActivityBean form) {
        LogUtil.startLog(WorldCupActivityDefine.class.toString(), WorldCupActivityDefine.WINNING_INIT);
        ModelAndView modelAndView = new ModelAndView(WorldCupActivityDefine.WINNING_LIST_PATH);
        // 创建分页
        this.championCreatePage(request, modelAndView, form);
        LogUtil.endLog(WorldCupActivityDefine.class.toString(), WorldCupActivityDefine.WINNING_INIT);
        return modelAndView;
    }
    /**
     * （竞猜冠军）分页查询
     */
    private void championCreatePage(HttpServletRequest request, ModelAndView modelAndView, WorldCupActivityBean form) {
        Integer count = this.worldCupActivityService.countChampionRecordBySearchCon(form);
        if (count.intValue() > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            List<GuessingActivitieCustomize>  recordList = this.worldCupActivityService.getChampionRecordList(form, paginator.getOffset(),paginator.getLimit());
            form.setPaginator(paginator);
            form.setRecordList(recordList);
            //返回页面数据
            modelAndView.addObject(WorldCupActivityDefine.FORM, form);
        }

    }
    /**
     * 导出列表竞猜输赢
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(WorldCupActivityDefine.EXPORT_EXCEL_ACTION)
    @RequiresPermissions(WorldCupActivityDefine.PERMISSIONS_EXPORT)
    public void exportAccountsExcel(HttpServletRequest request, HttpServletResponse response, @ModelAttribute(WorldCupActivityDefine.FORM) WorldCupActivityBean form) throws Exception {
        // 表格sheet名称
        String sheetName = "竞猜输赢";
        // 取得数据
        List<GuessingActivitieCustomize>  recordList = createExcelPage(request, form);
        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
        String[] titles = new String[]{ "账户名称", "姓名", "手机号", "竞猜场次", "用户的竞猜",  "比赛结果","竞猜结果", "第几次竞猜", "猜中场数", "猜中场数排行","竞猜时间"};
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
                    GuessingActivitieCustomize bean = recordList.get(i);
                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);
                    // 账户名称
                    if (celLength == 0) {
                        cell.setCellValue(bean.getUsername());
                    }
                    // 姓名
                    else if (celLength == 1) {
                        cell.setCellValue(bean.getTrueName());
                    }
                    // 手机号
                    else if (celLength == 2) {
                        cell.setCellValue(bean.getMobile());
                    }
                    // 竞猜场次
                    else if (celLength == 3) {
                        cell.setCellValue(bean.getBatchName());
                    }
                    // 用户的竞猜
                    else if (celLength == 4) {
                        cell.setCellValue(bean.getTeamName());
                    }
                    // 比赛结果
                    else if (celLength == 5) {
                        cell.setCellValue(bean.getMatchResult());
                    }
                    // 竞猜结果
                    else if (celLength == 6) {
                        cell.setCellValue(bean.getGuessingMatchResult() == null ? "" : String.valueOf(bean.getGuessingMatchResult()));
                    }
                    // 第几次竞猜
                    else if (celLength == 7) {
                        cell.setCellValue(bean.getGuessingMatchNum());
                    }
                    // 猜中场数排行
                    else if (celLength == 8) {
                        cell.setCellValue(bean.getGuessingFieldNum());
                    }
                    // 猜中场数
                    else if (celLength == 9) {
                        cell.setCellValue(bean.getGuessingRankings());
                    }

                    // 竞猜时间
                    else if (celLength == 10) {
                         cell.setCellValue(bean.getTime());
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
    }
    /**
     * 导出列表竞猜输赢
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(WorldCupActivityDefine.EXPORT_WINNING_EXCEL_ACTION)
    @RequiresPermissions(WorldCupActivityDefine.PERMISSIONS_EXPORT)
    public void exportWinningExcel(HttpServletRequest request, HttpServletResponse response, @ModelAttribute(WorldCupActivityDefine.FORM) WorldCupActivityBean form) throws Exception {
        // 表格sheet名称
        String sheetName = "竞猜冠军";
        // 取得数据
        List<GuessingActivitieCustomize>  recordList = createWinExcelPage(request, form);
        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
        String[] titles = new String[]{ "账户名称", "姓名", "手机号", "支持的球队", "投票后胜场数", "投票时间"};
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
                    GuessingActivitieCustomize bean = recordList.get(i);
                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);
                    // 账户名称
                    if (celLength == 0) {
                        cell.setCellValue(bean.getUsername());
                    }
                    // 姓名
                    else if (celLength == 1) {
                        cell.setCellValue(bean.getTrueName());
                    }
                    // 手机号
                    else if (celLength == 2) {
                        cell.setCellValue(bean.getMobile());
                    }
                    // 支持的球队
                    else if (celLength == 3) {
                        cell.setCellValue(bean.getTeamName());
                    }
                    // 投票后胜场数
                    else if (celLength == 4) {
                        cell.setCellValue(bean.getGuessingMatchNum());
                    }
                    // 投票时间
                    else if (celLength == 5) {
                        cell.setCellValue(bean.getTime());
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
    }

    //获取导出的数据
    public List<GuessingActivitieCustomize>  createExcelPage(HttpServletRequest request, WorldCupActivityBean form) {
       Integer count = this.worldCupActivityService.countRecordBySearchCon(form);
        List<GuessingActivitieCustomize>  recordList =new ArrayList<>();
        if (count.intValue() > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            recordList = this.worldCupActivityService.getRecordList(form, paginator.getOffset(),paginator.getLimit());
            form.setPaginator(paginator);
            form.setRecordList(recordList);
        }
        return recordList;
    }
    /**
     * （竞猜冠军）获取导出的数据
     */
    private List<GuessingActivitieCustomize>  createWinExcelPage(HttpServletRequest request,  WorldCupActivityBean form) {
        Integer count = this.worldCupActivityService.countChampionRecordBySearchCon(form);
        List<GuessingActivitieCustomize>  recordList=new ArrayList<>();
        if (count.intValue() > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            recordList = this.worldCupActivityService.getChampionRecordList(form, paginator.getOffset(),paginator.getLimit());
            form.setPaginator(paginator);
            form.setRecordList(recordList);
        }
        return recordList;
    }


}
