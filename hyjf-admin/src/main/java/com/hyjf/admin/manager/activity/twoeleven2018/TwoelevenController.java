package com.hyjf.admin.manager.activity.twoeleven2018;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.TwoelevenReward;
import com.hyjf.mybatis.model.customize.admin.TwoelevenCustomize;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiehuili on 2018/10/10.
 */
@Controller
@RequestMapping(value = TwoelevenDefine.REQUEST_MAPPING)
public class TwoelevenController extends BaseController {

    @Autowired
    private TwoelevenService twoelevenService;
    /**
     * 秒杀 明细 画面初始化
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(TwoelevenDefine.INIT)
    @RequiresPermissions(TwoelevenDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, TwoelevenBean form) {
        LogUtil.startLog(TwoelevenController.class.toString(), TwoelevenDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(TwoelevenDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(TwoelevenController.class.toString(), TwoelevenDefine.INIT);
        return modelAndView;
    }
    /**
     * 创建分页机能
     * @param request
     * @param modelAndView
     * @param form
     */
    private List<TwoelevenCustomize> createPage(HttpServletRequest request, ModelAndView modelAndView, TwoelevenBean form) {
        List<TwoelevenCustomize> recordList = null;
        Map<String, Object> paraMap =beanToMap(form);
        Integer count = this.twoelevenService.selectTwoelevenSeckillCount(paraMap);
        if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
            paraMap.put("limitStart", paginator.getOffset());
            paraMap.put("limitEnd", paginator.getLimit());
            recordList = this.twoelevenService.selectTwoelevenSeckillList(paraMap);
            form.setPaginator(paginator);
            form.setRecordList(recordList);
        }
        modelAndView.addObject(TwoelevenDefine.PRIZECODE_FORM, form);
        return recordList;
    }

    /**
     *  年化出借金额 画面初始化
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(TwoelevenDefine.INVEST_INIT)
    @RequiresPermissions(TwoelevenDefine.PERMISSIONS_VIEW)
    public ModelAndView investInit(HttpServletRequest request, TwoelevenBean form) {
        LogUtil.startLog(TwoelevenController.class.toString(), TwoelevenDefine.INVEST_INIT);
        ModelAndView modelAndView = new ModelAndView(TwoelevenDefine.INVEST_LIST_PATH);
        // 创建分页
        this.createPageInvest(request, modelAndView, form);
        LogUtil.endLog(TwoelevenController.class.toString(), TwoelevenDefine.INVEST_INIT);
        return modelAndView;
    }
    /**
     * 创建分页机能
     * @param request
     * @param modelAndView
     * @param form
     */
    private List<TwoelevenCustomize> createPageInvest(HttpServletRequest request, ModelAndView modelAndView, TwoelevenBean form) {
        List<TwoelevenCustomize> recordList = null;
        Map<String, Object> paraMap =beanToMap(form);
        Integer count = this.twoelevenService.selectTwoelevenInvestCount(paraMap);
        if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
            paraMap.put("limitStart", paginator.getOffset());
            paraMap.put("limitEnd", paginator.getLimit());
            recordList = this.twoelevenService.selectTwoelevenInvestList(paraMap);
            form.setPaginator(paginator);
            form.setRecordList(recordList);
        }
        modelAndView.addObject(TwoelevenDefine.PRIZECODE_FORM, form);
        return recordList;
    }

    /**
     *  奖励（新款iPhone）明细画面初始化
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(TwoelevenDefine.REWARD_INIT)
    @RequiresPermissions(TwoelevenDefine.PERMISSIONS_VIEW)
    public ModelAndView rewardInit(HttpServletRequest request, TwoelevenBean form) {
        LogUtil.startLog(TwoelevenController.class.toString(), TwoelevenDefine.REWARD_INIT);
        ModelAndView modelAndView = new ModelAndView(TwoelevenDefine.REWARD_LIST_PATH);
        // 创建分页
        this.createPageReward(request, modelAndView, form);
        LogUtil.endLog(TwoelevenController.class.toString(), TwoelevenDefine.REWARD_INIT);
        return modelAndView;
    }
    /**
     * 创建分页机能
     * @param request
     * @param modelAndView
     * @param form
     */
    private List<TwoelevenCustomize> createPageReward(HttpServletRequest request, ModelAndView modelAndView, TwoelevenBean form) {
        List<TwoelevenCustomize> recordList = null;
        Map<String, Object> paraMap =beanToMap(form);
        Integer count = this.twoelevenService.selectTwoelevenRewardCount(paraMap);
        if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
            paraMap.put("limitStart", paginator.getOffset());
            paraMap.put("limitEnd", paginator.getLimit());
            recordList = this.twoelevenService.selectTwoelevenRewardList(paraMap);
            form.setPaginator(paginator);
            form.setRecordList(recordList);
        }
        modelAndView.addObject(TwoelevenDefine.PRIZECODE_FORM, form);
        return recordList;
    }

    /**
     * 奖励（新款iPhone）明细画面迁移(含有id更新，不含有id添加)
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(TwoelevenDefine.INFO_ACTION)
    public ModelAndView info(HttpServletRequest request, TwoelevenBean form) {
        LogUtil.startLog(TwoelevenController.class.toString(), TwoelevenDefine.INFO_ACTION);
        ModelAndView modelAndView = new ModelAndView(TwoelevenDefine.REWARD__INFO_LIST_PATH);
        //设置为未发放状态
        form.setStatus(0);
        modelAndView.addObject(TwoelevenDefine.PRIZECODE_FORM, form);
        LogUtil.endLog(TwoelevenController.class.toString(), TwoelevenDefine.INFO_ACTION);
        return modelAndView;
    }
    /**
     * 奖励（新款iPhone）明细修改
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(TwoelevenDefine.UPDATE_ACTION)
    public ModelAndView update(HttpServletRequest request, TwoelevenBean form) {
        LogUtil.startLog(TwoelevenController.class.toString(), TwoelevenDefine.UPDATE_ACTION);
        ModelAndView modelAndView = new ModelAndView(TwoelevenDefine.RE_LIST_PATH);
        if(form.getId() != null){
            TwoelevenReward record = new TwoelevenReward();
            record.setId(form.getId());
            record.setStatus(form.getStatus());
            twoelevenService.updateTwoelevenReward(record);
        }
        LogUtil.endLog(TwoelevenController.class.toString(), TwoelevenDefine.UPDATE_ACTION);
        return modelAndView;
    }

    /**
     * 秒杀导出
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(TwoelevenDefine.EXPORT_EXCEL_ACTION)
    @RequiresPermissions(TwoelevenDefine.PERMISSIONS_EXPORT)
    public void exportExcel(HttpServletRequest request, HttpServletResponse response, TwoelevenBean form) throws Exception {
        // 表格sheet名称
        String sheetName = "秒杀明细";
        //导出名称乱码
        String userAgent = request.getHeader("user-agent").toLowerCase();
        if (userAgent.contains("msie") || userAgent.contains("like gecko")) {
            // win10 ie edge 浏览器 和其他系统的ie
            sheetName = URLEncoder.encode(sheetName, "UTF-8");
        } else if(userAgent.contains("firefox")){
           // firefox
            sheetName=sheetName;
        } else{
            sheetName = new String(sheetName.getBytes("UTF-8"), "iso-8859-1");
        }
        // 取得数据
        List<TwoelevenCustomize> recordList = createPage(request, new ModelAndView(),form);
        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
        String[] titles = new String[]{ "序号","账户名", "姓名", "奖励名称","奖励类型","奖励批号","发放方式", "状态", "秒杀时间", "发放时间"};
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
                    TwoelevenCustomize bean = recordList.get(i);
                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);
                    // 账户名
                    if (celLength == 0) {
                        cell.setCellValue(rowNum);
                    }
                    // 账户名
                    else if (celLength == 1) {
                        cell.setCellValue(bean.getUsername() == null?"":bean.getUsername());
                    }
                    // 姓名
                    else if (celLength == 2) {
                        cell.setCellValue(bean.getTruename() == null?"" : bean.getTruename());
                    }
                    //奖励名称
                    else if (celLength == 3) {
                        cell.setCellValue(bean.getRewardName()==null?"" : bean.getRewardName());
                    }
                    //  奖励类型
                    else if (celLength == 4) {
                        cell.setCellValue(bean.getRewardType() == null? "": bean.getRewardType());
                    }
                    //"奖励批号
                    else if (celLength == 5) {
                        cell.setCellValue(bean.getRewardId() == null? " " : bean.getRewardId() );
                    }
                    //"发放方式",
                    else if (celLength == 6) {
                        cell.setCellValue(bean.getDistributionStatusName() == null? "" : bean.getDistributionStatusName());
                    }
                    // "状态
                    else if (celLength == 7) {
                        cell.setCellValue(bean.getStatusName() == null? "" : bean.getStatusName());
                    }
                    // "秒杀时间
                    else if (celLength == 8) {
                        cell.setCellValue(bean.getSecondsTime() == null? "" : bean.getSecondsTime());
                    }
                    // "发放时间
                    else if (celLength == 9) {
                        cell.setCellValue(bean.getSendTime() == null? "" : bean.getSendTime());
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
    }

    /**
     * 累计额年化出借金额导出
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(TwoelevenDefine.EXPORT_EXCEL_INVEST_ACTION)
    @RequiresPermissions(TwoelevenDefine.PERMISSIONS_EXPORT)
    public void exportInvestExcel(HttpServletRequest request, HttpServletResponse response, TwoelevenBean form) throws Exception {
        // 表格sheet名称
        String sheetName = "累计额年化出借金额";
        //导出名称乱码
        String userAgent = request.getHeader("user-agent").toLowerCase();
        if (userAgent.contains("msie") || userAgent.contains("like gecko")) {
            // win10 ie edge 浏览器 和其他系统的ie
            sheetName = URLEncoder.encode(sheetName, "UTF-8");
        } else if(userAgent.contains("firefox")){
            // firefox
            sheetName=sheetName;
        } else{
            sheetName = new String(sheetName.getBytes("UTF-8"), "iso-8859-1");
        }
        // 取得数据
        List<TwoelevenCustomize> recordList = createPageInvest(request, new ModelAndView(),form);
        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
        String[] titles = new String[]{ "序号","账户名", "姓名", "累计额年化出借金额(元)","奖励名称"};


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
                    TwoelevenCustomize bean = recordList.get(i);
                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);
                    // 账户名
                    if (celLength == 0) {
                        cell.setCellValue(rowNum);
                    }
                    // 账户名
                    else if (celLength == 1) {
                        cell.setCellValue(bean.getUsername() == null?"":bean.getUsername());
                    }
                    // 姓名
                    else if (celLength == 2) {
                        cell.setCellValue(bean.getTruename() == null?"" : bean.getTruename());
                    }
                    //累计额年化出借金额(
                    else if (celLength == 3) {
                        cell.setCellValue(bean.getYearAmountAll()==null?"" : bean.getYearAmountAll().toString());
                    }
                    //奖励名称
                    else if (celLength == 4) {
                        cell.setCellValue(bean.getRewardName()==null?"" : bean.getRewardName());
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
    }

    /**
     * 奖励（新款iPhone）明细导出
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(TwoelevenDefine.EXPORT_EXCEL_REWARD_ACTION)
    @RequiresPermissions(TwoelevenDefine.PERMISSIONS_EXPORT)
    public void exportRewardExcel(HttpServletRequest request, HttpServletResponse response, TwoelevenBean form) throws Exception {
        // 表格sheet名称
        String sheetName = "奖励（新款iPhone）明细";
        //导出名称乱码
        String userAgent = request.getHeader("user-agent").toLowerCase();
        if (userAgent.contains("msie") || userAgent.contains("like gecko")) {
            // win10 ie edge 浏览器 和其他系统的ie
            sheetName = URLEncoder.encode(sheetName, "UTF-8");
        } else if(userAgent.contains("firefox")){
            // firefox
            sheetName=sheetName;
        } else{
            sheetName = new String(sheetName.getBytes("UTF-8"), "iso-8859-1");
        }
        // 取得数据
        List<TwoelevenCustomize> recordList = createPageReward(request, new ModelAndView(),form);
        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
        String[] titles = new String[]{ "序号","奖励名称","发放方式", "账户名", "姓名", "手机号","状态", "获得时间", "发放时间"};
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
                    TwoelevenCustomize bean = recordList.get(i);
                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);
                    // 账户名
                    if (celLength == 0) {
                        cell.setCellValue(rowNum);
                    }else if (celLength == 1) {
                        //奖励名称
                        cell.setCellValue(bean.getRewardName()==null?"" : bean.getRewardName());
                    }else if (celLength == 2) {
                        //"发放方式",
                        cell.setCellValue(bean.getDistributionStatusName() == null? "" : bean.getDistributionStatusName());
                    }else if (celLength == 3) {
                        // 账户名
                        cell.setCellValue(bean.getUsername() == null?"":bean.getUsername());
                    }else if (celLength == 4) {
                         // 姓名
                        cell.setCellValue(bean.getTruename() == null ? "" : bean.getTruename());
                    } else if (celLength == 5) {
                         // 手机号
                        cell.setCellValue(bean.getMobile() == null ? "" : bean.getMobile());
                    }else if (celLength == 6) {
                        // "状态
                        cell.setCellValue(bean.getStatusName() == null? "" : bean.getStatusName());
                    }else if (celLength == 7) {
                         // "获得时间
                        cell.setCellValue(bean.getObtainTime() == null? "" : bean.getObtainTime());
                    }else if (celLength == 8) {
                         // "发放时间
                        cell.setCellValue(bean.getSendTime() == null? "" : bean.getSendTime());
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
    }

    public Map<String, Object> beanToMap(TwoelevenBean form){
        Map<String, Object> paraMap = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(form.getUsername())){
            paraMap.put("username", form.getUsername().trim());
        }
        if(StringUtils.isNotBlank(form.getTruename())){
            paraMap.put("truename", form.getTruename().trim());
        }
        if(StringUtils.isNotBlank(form.getRewardType())){
            paraMap.put("rewardType", form.getRewardType().trim());
        }
        if(StringUtils.isNotBlank(form.getRewardId())){
            paraMap.put("rewardId", form.getRewardId().trim());
        }
        if(StringUtils.isNotBlank(form.getCol())){
            paraMap.put("col", form.getCol().trim());
        }
        if(StringUtils.isNotBlank(form.getSort())){
            paraMap.put("sort", form.getSort().trim());
        }
        if(form.getStatus() != null){
            paraMap.put("status", form.getStatus());
        }
        if(StringUtils.isNotBlank(form.getMobile())){
            paraMap.put("mobile", form.getMobile().trim());
        }
        return paraMap;
    }
}
