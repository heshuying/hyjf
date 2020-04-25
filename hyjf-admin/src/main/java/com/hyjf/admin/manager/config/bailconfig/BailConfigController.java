package com.hyjf.admin.manager.config.bailconfig;

import com.hyjf.admin.BaseController;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.*;
import com.hyjf.mybatis.model.auto.HjhInstConfig;
import com.hyjf.mybatis.model.customize.AdminSystem;
import com.hyjf.mybatis.model.customize.admin.HjhBailConfigCustomize;
import com.hyjf.mybatis.model.customize.admin.HjhBailConfigInfoCustomize;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 现金贷资产方配置页面
 *
 * @author liushouyi
 */
@Controller
@RequestMapping(value = BailConfigDefine.REQUEST_MAPPING)
public class BailConfigController extends BaseController {

    @Autowired
    private BailConfigService bailConfigService;

    /**
     * 现金贷资产方保证金配置画面初始化
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(BailConfigDefine.INIT)
    @RequiresPermissions(BailConfigDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, @ModelAttribute(BailConfigDefine.BAILCONFIG_FORM) BailConfigBean form) {
        ModelAndView modelAndView = new ModelAndView(BailConfigDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        return modelAndView;
    }

    /**
     * 创建分页机能
     *
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, BailConfigBean form) {
        HjhBailConfigCustomize hjhBailConfigCustomize = new HjhBailConfigCustomize();
        int count = this.bailConfigService.countBailConfig();
        if (count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            hjhBailConfigCustomize.setLimitStart(paginator.getOffset());
            hjhBailConfigCustomize.setLimitEnd(paginator.getLimit());
            List<HjhBailConfigInfoCustomize> record = bailConfigService.getRecordList(hjhBailConfigCustomize);
            form.setPaginator(paginator);
            form.setRecordList(record);
        }
        modelAndView.addObject(BailConfigDefine.BAILCONFIG_FORM, form);
    }

    /**
     * 画面迁移(含有id更新，不含有id添加)
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(BailConfigDefine.INFO_ACTION)
    @RequiresPermissions(value = {BailConfigDefine.PERMISSIONS_INFO, BailConfigDefine.PERMISSIONS_ADD, BailConfigDefine.PERMISSIONS_MODIFY}, logical = Logical.OR)
    public ModelAndView info(HttpServletRequest request, @ModelAttribute(BailConfigDefine.BAILCONFIG_FORM) BailConfigBean form) {
        ModelAndView modelAndView = new ModelAndView(BailConfigDefine.INFO_PATH);
        if (null != form.getId()) {
            Integer id = form.getId();
            HjhBailConfigInfoCustomize hjhBailConfigInfoCustomize = this.bailConfigService.selectHjhBailConfigInfo(id);
            if (hjhBailConfigInfoCustomize != null) {
                // 根据还款方式更新保证金还款方式验证的有效性
                this.bailConfigService.updateBailInfoDelFlg(hjhBailConfigInfoCustomize);
                // 点击修改按钮时先更新修改当前可用的还款方式再重新抽取数据加载到页面
                hjhBailConfigInfoCustomize = this.bailConfigService.selectHjhBailConfigInfo(id);
            }
            modelAndView.addObject(BailConfigDefine.BAILCONFIG_FORM, hjhBailConfigInfoCustomize);
        }
        // 资金来源
        List<HjhInstConfig> hjhInstConfigList = this.bailConfigService.hjhNoUsedInstConfigList();
        modelAndView.addObject("hjhInstConfigList", hjhInstConfigList);
        return modelAndView;
    }

    /**
     * 合作机构额度添加
     *
     * @param form
     * @return
     */
    @RequestMapping(BailConfigDefine.INSERT_ACTION)
    @RequiresPermissions(BailConfigDefine.PERMISSIONS_ADD)
    public ModelAndView add(HjhBailConfigInfoCustomize form) {
        ModelAndView modelAndView = new ModelAndView(BailConfigDefine.INFO_PATH);
        // 调用校验
        if (validatorFieldCheck(modelAndView, form) != null) {
            // 失败返回
            return validatorFieldCheck(modelAndView, form);
        }
        AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
        form.setCreateUserId(Integer.parseInt(adminSystem.getId()));
        form.setUpdateUserId(Integer.parseInt(adminSystem.getId()));
        String instCode = form.getInstCode();
        // 发标额度上限
//        form.setPushMarkLine(form.getBailTatol().multiply(new BigDecimal("100")).divide(new BigDecimal(form.getBailRate()),2, BigDecimal.ROUND_DOWN));
        // 发标额度余额（默认为上限）
//        form.setRemainMarkLine(form.getPushMarkLine());

        // 周期内发标已发额度
        BigDecimal sendedAccountByCycBD = BigDecimal.ZERO;
        String sendedAccountByCyc = this.bailConfigService.selectSendedAccountByCyc(form);
        if (StringUtils.isNotBlank(sendedAccountByCyc)) {
            sendedAccountByCycBD = new BigDecimal(sendedAccountByCyc);
        }
        form.setCycLoanTotal(sendedAccountByCycBD);

        // 保证金信息插入
        boolean result = this.bailConfigService.insertRecord(form);
        // 合规改造删除 2018-12-03
//        if (result) {
//            // 根据还款方式更新保证金还款方式验证的有效性
//            result = this.bailConfigService.updateBailInfoDelFlg(form);
//        }
        if (result) {
            // 日推标上限记录到redis
            RedisUtils.set(RedisConstants.DAY_MARK_LINE + instCode, form.getDayMarkLine().toString());
            // 月推标上限记录到redis
            RedisUtils.set(RedisConstants.MONTH_MARK_LINE + instCode, form.getMonthMarkLine().toString());
            // 日额度累计redis(日累计不存再的情况初始化)
            if (!RedisUtils.exists(RedisConstants.DAY_MARK_ACCUMULATE + instCode)) {
                RedisUtils.set(RedisConstants.DAY_MARK_ACCUMULATE + instCode, "0");
            }
        }
        modelAndView.addObject(BailConfigDefine.SUCCESS, BailConfigDefine.SUCCESS);
        return modelAndView;
    }

    /**
     * 合作机构额度修改
     *
     * @param form
     * @return
     */
    @RequestMapping(BailConfigDefine.UPDATE_ACTION)
    @RequiresPermissions(BailConfigDefine.PERMISSIONS_MODIFY)
    public ModelAndView update(HjhBailConfigInfoCustomize form) {
        ModelAndView modelAndView = new ModelAndView(BailConfigDefine.INFO_PATH);
        // 调用校验
        if (validatorFieldCheck(modelAndView, form) != null) {
            // 失败返回
            return validatorFieldCheck(modelAndView, form);
        }

        AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
        form.setCreateUserId(Integer.parseInt(adminSystem.getId()));

        // 保证金信息更新
        boolean result = this.bailConfigService.updateRecord(form);
//        if (result) {
//            // 根据还款方式更新保证金还款方式验证的有效性
//            result = this.bailConfigService.updateBailInfoDelFlg(form);
//        }
        if (result) {
            // 日推标上限记录到redis
            RedisUtils.set(RedisConstants.DAY_MARK_LINE + form.getInstCode(), form.getDayMarkLine().toString());
            // 月推标上限记录到redis
            RedisUtils.set(RedisConstants.MONTH_MARK_LINE + form.getInstCode(), form.getMonthMarkLine().toString());
        }
        modelAndView.addObject(BailConfigDefine.SUCCESS, BailConfigDefine.SUCCESS);
        return modelAndView;
    }

    /**
     * 删除配置信息
     *
     * @param request
     * @param id
     * @return
     */
    @RequestMapping(BailConfigDefine.DELETE_ACTION)
    @RequiresPermissions(BailConfigDefine.PERMISSIONS_DELETE)
    public ModelAndView deleteRecordAction(HttpServletRequest request, Integer id) {
        ModelAndView modelAndView = new ModelAndView(BailConfigDefine.RE_LIST_PATH);
        this.bailConfigService.deleteRecord(id);
        return modelAndView;
    }

    /**
     * 调用校验表单方法
     *
     * @param modelAndView
     * @param form
     * @return
     */
    private ModelAndView validatorFieldCheck(ModelAndView modelAndView, HjhBailConfigInfoCustomize form) {
        // 暂无后台校验字段
        return null;
    }

    /**
     * 合作机构额度导出
     *
     * @param request
     * @param response
     * @param form
     */
    @RequestMapping(BailConfigDefine.EXPORT_ACTION)
    @RequiresPermissions(BailConfigDefine.PERMISSIONS_EXPORT)
    public void exportBorrowExceptionExcel(HttpServletRequest request, HttpServletResponse response,
                                           HjhBailConfigInfoCustomize form) {
        // 表格sheet名称
        String sheetName = "合作机构额度列表";

        List<HjhBailConfigInfoCustomize> recordList = this.bailConfigService.getRecordList(form);
        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + ".xls";

        String[] titles = new String[]{"序号", "资产来源", "日推标额度", "月推标额度", "合作额度", "周期内发标额度"};

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
                    sheet = ExportExcel
                            .createHSSFWorkbookTitle(workbook, titles, (sheetName + "_第" + sheetCount + "页"));
                    rowNum = 1;
                }

                // 新建一行
                Row row = sheet.createRow(rowNum);
                // 循环数据
                for (int celLength = 0; celLength < titles.length; celLength++) {
                    HjhBailConfigInfoCustomize record = this.bailConfigService.selectHjhBailConfigInfo(recordList.get(i).getId());
                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);
                    // 序号
                    if (celLength == 0) {
                        cell.setCellValue(i + 1);
                    }
                    // 资产来源
                    else if (celLength == 1) {
                        cell.setCellValue(record.getInstName());
                    }
                    // 日推标额度
                    else if (celLength == 2) {
                        cell.setCellValue(record.getDayMarkLine().toString());
                    }
                    // 月推标额度
                    else if (celLength == 3) {
                        cell.setCellValue(record.getMonthMarkLine().toString());
                    }
                    // 新增授信额度
                    else if (celLength == 4) {
                        cell.setCellValue(record.getNewCreditLine().toString());
                    }
                    // 周期内发标额度
                    else if (celLength == 5) {
                        cell.setCellValue(String.valueOf(record.getCycLoanTotal()));
                    }

                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
    }

    /**
     * 下拉联动
     *
     * @param request
     * @return 进入资产列表页面
     */
    @RequestMapping(BailConfigDefine.INST_CODE_CHANGE_ACTION)
    @RequiresPermissions(BailConfigDefine.PERMISSIONS_VIEW)
    @ResponseBody
    public HjhBailConfigInfoCustomize instCodeChangeAction(HttpServletRequest request, RedirectAttributes attr, String instCode) {

        HjhBailConfigInfoCustomize hjhBailConfigInfoCustomize = new HjhBailConfigInfoCustomize();

        // 获取当前机构可用还款方式
        List<String> repayMethodList = this.bailConfigService.selectRepayMethod(instCode);
        if (repayMethodList != null && repayMethodList.size() > 0) {
            for (String repayMethod : repayMethodList) {
                if ("end".equals(repayMethod)) {
                    hjhBailConfigInfoCustomize.setEndDEL(0);
                }
                if ("endday".equals(repayMethod)) {
                    hjhBailConfigInfoCustomize.setEnddayDEL(0);
                }
                if ("month".equals(repayMethod)) {
                    hjhBailConfigInfoCustomize.setMonthDEL(0);
                }
                if ("endmonth".equals(repayMethod)) {
                    hjhBailConfigInfoCustomize.setEndmonthDEL(0);
                }
                if ("principal".equals(repayMethod)) {
                    hjhBailConfigInfoCustomize.setPrincipalDEL(0);
                }
                if ("season".equals(repayMethod)) {
                    hjhBailConfigInfoCustomize.setSeasonDEL(0);
                }
                if ("endmonths".equals(repayMethod)) {
                    hjhBailConfigInfoCustomize.setEndmonthsDEL(0);
                }
            }
        }
        return hjhBailConfigInfoCustomize;
    }
}
