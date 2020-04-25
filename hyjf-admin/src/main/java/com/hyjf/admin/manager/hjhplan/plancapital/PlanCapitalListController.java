package com.hyjf.admin.manager.hjhplan.plancapital;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.HjhPlanCapital;
import com.hyjf.mybatis.model.customize.admin.AdminPlanAccedeListCustomize;
import org.apache.commons.lang.time.DateUtils;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 资金计划列表画面
 *
 * @author liubin
 */
@Controller
@RequestMapping(value = PlanCapitalListDefine.REQUEST_MAPPING)
public class PlanCapitalListController extends BaseController {

    //类名
    private static final String THIS_CLASS = PlanCapitalListController.class.toString();

    @Autowired
    private PlanCapitalListService planCapitalListService;

    /**
     * 画面初始化
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(PlanCapitalListDefine.INIT)
    @RequiresPermissions(PlanCapitalListDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, PlanCapitalListBean form) {
        LogUtil.startLog(THIS_CLASS, PlanCapitalListDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(PlanCapitalListDefine.LIST_PATH);
        // 日期默认当天到6天后
        Date newDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        form.setDateFromSrch(simpleDateFormat.format(newDate));
        form.setDateToSrch(simpleDateFormat.format(DateUtils.addDays(newDate, 9)));
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(THIS_CLASS, PlanCapitalListDefine.INIT);
        return modelAndView;
    }

    /**
     * 列表检索Action
     * @author liubin
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(PlanCapitalListDefine.SEARCH_ACTION)
    @RequiresPermissions(PlanCapitalListDefine.PERMISSIONS_SEARCH)
    public ModelAndView search(HttpServletRequest request, PlanCapitalListBean form) {
        LogUtil.startLog(THIS_CLASS, PlanCapitalListDefine.SEARCH_ACTION);
        ModelAndView modelAndView = new ModelAndView(PlanCapitalListDefine.LIST_PATH);
        // 分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(THIS_CLASS, PlanCapitalListDefine.SEARCH_ACTION);
        return modelAndView;
    }

    /**
     * 分页
     *
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, PlanCapitalListBean form) {
        int count = this.planCapitalListService.countRecord(form);
        if (count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            form.setLimitStart(paginator.getOffset());
            form.setLimitEnd(paginator.getLimit());
            List<HjhPlanCapital> recordList = this.planCapitalListService.getRecordList(form);
            form.setRecordList(recordList);
            form.setPaginator(paginator);
            // 合计
            Map<String , Object> sumRecord = this.planCapitalListService.sumRecord(form);
            modelAndView.addObject("sumRecord", sumRecord);
        }
        modelAndView.addObject(PlanCapitalListDefine.PLANCAPITALLIST_FORM, form);
    }

    /**
     * 复投详细画面
     * @param request
     * @param form
     * @param attr
     * @return
     */
    @RequestMapping(value = PlanCapitalListDefine.REINVEST_INFO_ACTION)
    @RequiresPermissions(PlanCapitalListDefine.PERMISSIONS_INFO)
    public ModelAndView reinvestInfoAction(HttpServletRequest request, PlanCapitalListBean form, RedirectAttributes attr) {
        LogUtil.startLog(THIS_CLASS, PlanCapitalListDefine.REINVEST_INFO_ACTION);
        ModelAndView modelAndView = new ModelAndView(PlanCapitalListDefine.REINVEST_DETAIL_ACITON);
        attr.addAttribute("date", form.getDateKey());
        attr.addAttribute("planNid", form.getPlanNid());
        LogUtil.endLog(THIS_CLASS, PlanCapitalListDefine.REINVEST_INFO_ACTION);
        return modelAndView;
    }

    /**
     * 债转详细画面
     * @param request
     * @param form
     * @param attr
     * @return
     */
    @RequestMapping(value = PlanCapitalListDefine.CREDIT_INFO_ACTION)
    @RequiresPermissions(PlanCapitalListDefine.PERMISSIONS_INFO)
    public ModelAndView creditInfoAction(HttpServletRequest request, PlanCapitalListBean form, RedirectAttributes attr) {
        LogUtil.startLog(THIS_CLASS, PlanCapitalListDefine.CREDIT_INFO_ACTION);
        ModelAndView modelAndView = new ModelAndView(PlanCapitalListDefine.CREDIT_DETAIL_ACITON);
        attr.addAttribute("date", form.getDateKey());
        attr.addAttribute("planNid", form.getPlanNid());
        LogUtil.endLog(THIS_CLASS, PlanCapitalListDefine.CREDIT_INFO_ACTION);
        return modelAndView;
    }

    /**
     * 根据业务需求导出相应的表格 此处暂时为可用情况 缺陷： 1.无法指定相应的列的顺序， 2.无法配置，excel文件名，excel sheet名称
     * 3.目前只能导出一个sheet 4.列的宽度的自适应，中文存在一定问题
     * 5.根据导出的业务需求最好可以在导出的时候输入起止页码，因为在大数据量的情况下容易造成卡顿
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(PlanCapitalListDefine.EXPORT_ACTION)
    @RequiresPermissions(PlanCapitalListDefine.PERMISSIONS_EXPORT)
    public void exportExcel(@ModelAttribute PlanCapitalListBean form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        LogUtil.startLog(THIS_CLASS, PlanCapitalListDefine.EXPORT_ACTION);
        // 表格sheet名称
        String sheetName = "资金计划";
        // 文件名称
        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
        // 需要输出的结果列表
        List<HjhPlanCapital> recordList = this.planCapitalListService.getRecordList(form);
        String[] titles = new String[] { "序号", "日期", "智投编号", "智投名称", "服务回报期限", "复投总额（元）", "债转总额（元）" };
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
                    HjhPlanCapital data = recordList.get(i);
                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);
                    if (celLength == 0) {// 序号
                        cell.setCellValue(i + 1);
                    } else if (celLength == 1) {// 日期
                        cell.setCellValue(GetDate.dateToString2(data.getDate()));
                    } else if (celLength == 2) {// 计划编号
                        cell.setCellValue(data.getPlanNid());
                    } else if (celLength == 3) {// 计划名称
                        cell.setCellValue(data.getPlanName());
                    } else if (celLength == 4) {// 锁定期
                        cell.setCellValue(data.getLockPeriod() + (data.getIsMonth()==0?"天":"个月"));
                    } else if (celLength == 5) {// 复投总额（元）
                        cell.setCellValue(CustomConstants.DF_FOR_VIEW.format(data.getReinvestAccount()));
                    } else if (celLength == 6) {// 债转总额（元）
                        cell.setCellValue(CustomConstants.DF_FOR_VIEW.format(data.getCreditAccount()));
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
        LogUtil.endLog(THIS_CLASS, PlanCapitalListDefine.EXPORT_ACTION);
    }
}
