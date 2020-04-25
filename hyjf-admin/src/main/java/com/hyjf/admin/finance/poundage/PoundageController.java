package com.hyjf.admin.finance.poundage;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.admin.finance.poundagedetail.PoundageDetailDefine;
import com.hyjf.admin.finance.poundagedetail.PoundageDetailService;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.poundageledger.PoundageCustomize;

/**
 * 每天上午8点系统统计前一天（0:00:00-23:59:59）需要手续费分账的数据，在此列表显示
 * 页面数据：
 * 1.列表数据类型
 * 每天统计出的前一天数据在此显示
 * 2.列表排序
 * 按分账统计时间倒序排序，即最新添加的在前面，同一时间添加的不同的收款账户不用区分先后
 * 3.查询字段
 * （1）分账状态：下拉列表，单选，选项：全部/未审核/审核通过//分账成功、分账失败，默认：全部；
 * （2）分账时间：时间控件，具体到天；结束值必须大于等于开始值，且时间段最大1个月；默认为空，即表示不限；
 * 4.列表字段
 * （1）总分账金额：一天所有需要分账的金额之和，保留小数点后两位，计算时取分账明细中已经去尾的金额累加，点击分账，将该天总分账金额转入收款方账户
 * （2）总分账笔数：一天所有需要分账的笔数
 * （3）收款方用户名和姓名：以收款方为单位，显示其一天内所需要分账的金额和笔数
 * （4）分账状态：
 * ① 未审核，操作栏按钮：审核、详情、下载；
 * ② 审核通过，已进行审核，但未进行分账；操作按钮：分账、详情、下载
 * ③ 分账成功，点击分账按钮后，银行返回成功；操作按钮：详情、下载
 * ④ 分账失败，点击分账，银行没有返回明确的成功信息，都纳入分账失败，进入异常中心
 * （5）分账时间，记录分账的时间点，精确到时分秒
 * 5.总计：统计总分账金额和总分账笔数；若进行了筛选，则统计筛选后的金额与笔数
 * 6.操作
 * （1）审核：点击审核后二次确认，点击确认后审核成功；
 * （2）分账：审核通过后，记录状态变成审核通过之后，审核按钮隐藏，显示分账按钮，点击后二次确认，点击确认后开始按照明细分账，调用江西银行手续费分账接口
 * （3）详情：点击详情后进入详情页面
 * 7.下载
 * 点击下载之后，导出对应一天的所有详细分账记录的excel表格
 * 字段包括：详情列表里的字段及对应信息
 * 8.导出列表
 * 导出字段：序号、收款方用户名、姓名、总分账金额、总分账笔数、分账时间段、分账状态、分账时间
 *
 * @author Albert
 */
@Controller
@RequestMapping(value = PoundageDefine.REQUEST_MAPPING)
public class PoundageController extends BaseController {

    @Autowired
    private PoundageService poundageService;
    @Autowired
    private PoundageDetailService poundageDetailService;

    Logger _log = LoggerFactory.getLogger(PoundageController.class);

    /**
     * 查询列表
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(PoundageDefine.POUNDAGE_LIST)
    @RequiresPermissions(PoundageDefine.PERMISSIONS_VIEW)
    public ModelAndView search(HttpServletRequest request, PoundageBean form) {
        LogUtil.startLog(PoundageController.class.toString(), PoundageDefine.POUNDAGE_LIST);
        ModelAndView modelAndView = new ModelAndView(PoundageDefine.LIST_PATH);
        PoundageCustomize poundageSum = this.poundageService.getPoundageSum(form);
        modelAndView.addObject("poundageSum", poundageSum);
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(PoundageController.class.toString(), PoundageDefine.POUNDAGE_LIST);
        return modelAndView;
    }

    /**
     * 分页
     *
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, PoundageBean form) {
        PoundageCustomize poundageCustomize = new PoundageCustomize();
        BeanUtils.copyProperties(form, poundageCustomize);
        Integer count = this.poundageService.getPoundageCount(poundageCustomize);
        if (count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            poundageCustomize.setLimitStart(paginator.getOffset());
            poundageCustomize.setLimitEnd(paginator.getLimit());
            List<PoundageCustomize> customers = this.poundageService.getPoundageList(poundageCustomize);
            form.setPaginator(paginator);
            form.setRecordList(customers);
        }
        modelAndView.addObject(PoundageDefine.POUNDAGE_FORM, form);
    }

    /**
     * 添加按钮,跳转添加详情画面
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(PoundageDefine.DETAIL_ACTION)
    @RequiresPermissions(PoundageDefine.PERMISSIONS_ADD)
    public ModelAndView detailAction(HttpServletRequest request, PoundageBean form) {
        LogUtil.startLog(PoundageController.class.getName(), PoundageDefine.DETAIL_ACTION);
        ModelAndView modelAndView = new ModelAndView(PoundageDefine.DETAIL_PATH);
        String id = request.getParameter("id");
        if (id != null && id != "") {
            PoundageCustomize poundage = this.poundageService.getPoundageById(Integer.valueOf(id));
            // 转出方用户电子账户号
            poundage.setAccountId(CustomConstants.HYJF_BANK_MERS_ACCOUNT);
            // 余额
            BigDecimal balance = this.poundageService.getBankBalance(Integer.parseInt(ShiroUtil.getLoginUserId()), CustomConstants.HYJF_BANK_MERS_ACCOUNT);
            if (balance == null) {
                modelAndView.addObject(PoundageDefine.SUCCESS, PoundageDefine.JSON_STATUS_NG);
            } else {
                poundage.setBalance(balance.toString());
            }
            modelAndView.addObject(PoundageDefine.POUNDAGE_FORM, poundage);
        }
        LogUtil.endLog(PoundageController.class.getName(), PoundageDefine.DETAIL_ACTION);
        return modelAndView;
    }

    /**
     * 审核
     *
     * @param request
     * @param form
     * @return
     * @author wgx
     */
    @RequestMapping(PoundageDefine.TRANSFER_AUDIT)
    @RequiresPermissions(PoundageDefine.PERMISSIONS_AUDIT)
    public ModelAndView updateAction(HttpServletRequest request, PoundageBean form) {
        LogUtil.startLog(PoundageDefine.class.getName(), PoundageDefine.PERMISSIONS_AUDIT);
        ModelAndView modelAndView = new ModelAndView(PoundageDefine.LIST_PATH);
        // 登陆用户ID
        Integer loginUserId = Integer.parseInt(ShiroUtil.getLoginUserId());
        form.setStatus(PoundageBean.STATUS_AUDIT);
        form.setUpdater(loginUserId);
        form.setUpdateTime(GetDate.getNowTime10());
        this.poundageService.updatePoundage(form);
        modelAndView.addObject(PoundageDefine.SUCCESS, PoundageDefine.SUCCESS);
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(PoundageDefine.class.getName(), PoundageDefine.PERMISSIONS_AUDIT);
        return modelAndView;
    }

    /**
     * 佣金分账
     * 1.查询银行平台还款服务费账户余额
     * 2.调用江西银行接口分佣
     * 成功:更新手续费分账信息--更新分账时间和分账状态（分账成功）;
     * 更新转入用户账户信息--更新转入用户江西银行总资产和江西银行可用余额;
     * 插入交易明细（资金中心-资金明细）--交易类型:分账,收支类型：收入;
     * 插入手续费账户明细（资金中心-银行平台用户-手续费账户明细）--交易类型:分账,收支类型:支出.
     * 失败:更新手续费分账信息--更新分账状态（分账失败）;
     * 插入手续费分账异常信息（异常中心-手续费异常处理）--可根据订单号和手续费账号查询状态.
     *
     * @param request
     * @param form
     * @return
     * @author wgx
     */
    @RequestMapping(value = PoundageDefine.TRANSFER_ACTION)
    @RequiresPermissions(PoundageDefine.PERMISSIONS_ADD)
    public ModelAndView transferAction(HttpServletRequest request, PoundageBean form) {
        LogUtil.startLog(PoundageController.class.getName(), PoundageDefine.TRANSFER_ACTION);
        ModelAndView modelAndView = new ModelAndView(PoundageDefine.DETAIL_PATH);
        // 登陆用户ID
        Integer loginUserId = Integer.parseInt(ShiroUtil.getLoginUserId());
        // 转出用户电子账户号
        String accountId = form.getAccountId();
        // 余额
        BigDecimal balance = this.poundageService.getBankBalance(loginUserId, accountId);
        form.setBalance(balance == null ? BigDecimal.ZERO.toString() : balance.toString());// 账户余额
        form.setUpdater(loginUserId);//分账人
        int nowTime = GetDate.getNowTime10();
        form.setUpdateTime(nowTime);//修改时间
        form.setAddTime(nowTime);//分账时间
        poundageService.checkTransferParam(modelAndView, form);
        if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
            modelAndView.addObject(PoundageDefine.POUNDAGE_FORM, form);
            return modelAndView;
        } else {
            // 调用江西银行接口分佣
            try {
                BankCallBean resultBean = this.poundageService.getLedgerResult(form);
                if (resultBean == null || resultBean.getRetCode() == null) {
                    String logOrderId = resultBean.getLogOrderId() == null ? "" : resultBean.getLogOrderId();
                    _log.info("调用银行接口失败,银行返回空.订单号:[" + logOrderId + "].");
                    return getModelAndView(form, modelAndView, resultBean, "feeshare.transfer.error", "调用银行接口失败");
                }
                form.setStatus(PoundageBean.STATUS_FAIL);
                // 银行返回响应代码
                String retCode = resultBean == null ? "" : resultBean.getRetCode();
                if ("CA51".equals(retCode)) {
                    return getModelAndView(form, modelAndView, resultBean, "feeshare.transfer.txamount.error", "账户余额不足");
                }
                // 调用银行接口失败
                if (!BankCallStatusConstant.RESPCODE_SUCCESS.equals(retCode)) {
                    return getModelAndView(form, modelAndView, resultBean, "feeshare.transfer.error", "调用银行接口失败(" + retCode + ")");
                }
                // 银行返回成功
                // 更新订单,用户账户等信息
                boolean updateFlag = this.poundageService.updateAfterLedgerSuccess(resultBean, form);
                if (!updateFlag) {
                    _log.info("调用银行成功后,更新数据失败");
                    // 转账成功，更新状态失败
                    return getModelAndView(form, modelAndView, resultBean, "feeshare.transfer.success", "调用银行成功后,更新数据失败");
                }
                poundageService.addBankCall(form, resultBean);
            } catch (Exception e) {
                _log.info("转账发生异常:异常信息:[" + e.getMessage() + "].");
                return getModelAndView(form, modelAndView, new BankCallBean(), "feeshare.transfer.exception", "转账发生异常");
            }
        }
        //分账成功
        return getSuccessModelAndView(form, modelAndView);
    }

    /**
     * 分账成功后返回前台页面
     *
     * @param form
     * @param modelAndView
     * @return
     */
    private ModelAndView getSuccessModelAndView(PoundageBean form, ModelAndView modelAndView) {
        form.setStatus(PoundageBean.STATUS_SUCCESS);
        modelAndView.addObject(PoundageDefine.POUNDAGE_FORM, form);
        modelAndView.addObject(PoundageDefine.SUCCESS, PoundageDefine.SUCCESS);
        poundageService.updatePoundage(form);
        LogUtil.endLog(PoundageController.class.getName(), PoundageDefine.TRANSFER_ACTION);
        return modelAndView;
    }

    /**
     * 根据错误信息返回前台页面
     *
     * @param form
     * @param modelAndView
     * @param resultBean
     * @param errorId
     * @param error
     * @return
     */
    private ModelAndView getModelAndView(PoundageBean form, ModelAndView modelAndView, BankCallBean resultBean, String errorId, String error) {
        ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "error", errorId, error);
        poundageService.addBankCall(form, resultBean);
        poundageService.updateAfterLedgerFail(resultBean, form);
        modelAndView.addObject(PoundageDefine.POUNDAGE_FORM, form);
        modelAndView.addObject(PoundageDefine.JSON_STATUS_KEY, PoundageDefine.JSON_STATUS_NG);
        return modelAndView;
    }

    /**
     * 导出手续费列表
     *
     * @param request
     * @param response
     * @throws Exception
     * @author wgx
     */
    @RequestMapping(PoundageDefine.POUNDAGE_EXPORT)
    @RequiresPermissions(PoundageDefine.PERMISSIONS_EXPORT)
    public void exportPoundageExcel(HttpServletRequest request, HttpServletResponse response, PoundageBean form)
            throws Exception {
        // 表格sheet名称
        String sheetName = "手续费分账";
        // 取得数据
        form.setLimitStart(-1);
        form.setLimitEnd(-1);
        PoundageCustomize poundageCustomize = new PoundageCustomize();
        BeanUtils.copyProperties(form, poundageCustomize);
        List<PoundageCustomize> recordList = this.poundageService.getPoundageList(poundageCustomize);
        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
        String[] titles = new String[]{"序号", "收款方用户名", "收款方姓名", "总分账金额", "总分账笔数", "分账时间段",
                "分账状态", "分账时间", "分账订单号", "分账流水号"};
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
                    PoundageCustomize bean = recordList.get(i);

                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);

                    // 序号
                    if (celLength == 0) {
                        cell.setCellValue(i + 1);
                    }
                    // 收款方用户名
                    else if (celLength == 1) {
                        cell.setCellValue(bean.getUserName());
                    }
                    // 姓名
                    else if (celLength == 2) {
                        cell.setCellValue(bean.getRealName());
                    }
                    // 总分账金额
                    else if (celLength == 3) {
                        cell.setCellValue(bean.getAmount().toString());
                    }
                    // 总分账笔数
                    else if (celLength == 4) {
                        cell.setCellValue(bean.getQuantity());
                    }
                    // 分账时间段
                    else if (celLength == 5) {
                        Integer poundageTime = Integer.parseInt(bean.getPoundageTime());
                        cell.setCellValue(poundageTime == null ? "" : GetDate.timestamptoStrYYYYMMDD(poundageTime));
                    }
                    // 分账状态
                    else if (celLength == 6) {
                        cell.setCellValue(PoundageCustomize.getStatusStr(bean.getStatus()));
                    }
                    // 分账时间
                    else if (celLength == 7) {
                        Integer addTime = bean.getAddTime();
                        cell.setCellValue(addTime == null ? "" : GetDate.timestamptoStrYYYYMMDDHHMMSS(addTime));
                    }
                    // 分账订单号
                    else if (celLength == 8) {
                        cell.setCellValue(bean.getNid());
                    }
                    // 分账流水号
                    else if (celLength == 9) {
                        cell.setCellValue(bean.getSeqNo());
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
    }

    /**
     * 导出手续费明细列表
     *
     * @param request
     * @param response
     * @throws Exception
     * @author wgx
     */
    @RequestMapping(PoundageDefine.POUNDAGE_DETAIL_EXPORT)
    @RequiresPermissions(PoundageDetailDefine.PERMISSIONS_EXPORT)
    public void exportPoundageDetailExcel(HttpServletRequest request, HttpServletResponse response, PoundageBean form)
            throws Exception {
        // 手续费分账信息
        PoundageCustomize poundageCustomize = poundageService.getPoundageById(form.getId());
        poundageDetailService.exportPoundageDetail(response, poundageCustomize);
    }
}
