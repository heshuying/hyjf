package com.hyjf.admin.finance.bankjournal;

import com.google.common.collect.Maps;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.finance.bankaleve.Dataset2ExcelHelper;
import com.hyjf.admin.finance.bankaleve.IValueFormater;
import com.hyjf.admin.finance.bankaleve.RevindValueFormater;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.mapper.customize.EveLogCustomizeMapper;
import com.hyjf.mybatis.model.customize.EveLogCustomize;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 银行交易明细
 * Created by cuigq on 2018/1/18.
 */
@Controller
@RequestMapping(BankJournalDefine.REQUEST_MAPPING)
public class BankJournalController extends BaseController {

    @Autowired
    private BankJournalService bankJournalService;

    @Autowired
    private EveLogCustomizeMapper eveLogCustomizeMapper;

    /**
     * 初始化页面
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(BankJournalDefine.INIT)
    @RequiresPermissions(BankJournalDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, BankJournalBean form) {
        ModelAndView modeAndView = new ModelAndView(BankJournalDefine.LIST_PATH);

        Date endDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        form.setStartDate(simpleDateFormat.format(DateUtils.addDays(endDate, -1)));
        form.setEndDate(simpleDateFormat.format(DateUtils.addDays(endDate, -1)));

        bankJournalService.queryList(form);

        modeAndView.addObject(BankJournalDefine.BANKJOURNAL_FORM, form);
        return modeAndView;
    }


    /**
     * 银行交易明细 列表
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(BankJournalDefine.BANKJOURNAL_LIST)
    @RequiresPermissions(BankJournalDefine.PERMISSIONS_VIEW)
    public ModelAndView search(HttpServletRequest request, BankJournalBean form) {
        ModelAndView modeAndView = new ModelAndView(BankJournalDefine.LIST_PATH);
        bankJournalService.queryList(form);
        modeAndView.addObject(BankJournalDefine.BANKJOURNAL_FORM, form);
        return modeAndView;
    }


    /**
     * 导出银行交易明细
     *
     * @param request
     * @param response
     * @param form
     * @throws Exception
     */
    @RequestMapping(BankJournalDefine.EXPORT_BANKJOURNAL_ACTION)
    @RequiresPermissions(BankJournalDefine.PERMISSIONS_EXPORT)
    public void exportBankJournalExcel(HttpServletRequest request, HttpServletResponse response, BankJournalBean form) throws Exception {
        String sheetName = "银行交易明细";

        //sheet默认最大行数
        int defaultRowMaxCount = 1000;

        // 设置默认查询时间
        if (StringUtils.isEmpty(form.getStartDate())) {
            form.setStartDate(GetDate.getDate("yyyy-MM-dd"));
        }
        if (StringUtils.isEmpty(form.getEndDate())) {
            form.setEndDate(GetDate.getDate("yyyy-MM-dd"));
        }

        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + ".xls";

        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();

        int totalCount = eveLogCustomizeMapper.queryEveLogCount(form);

        int sheetCount = (totalCount % defaultRowMaxCount) == 0 ? totalCount / defaultRowMaxCount : totalCount / defaultRowMaxCount + 1;

        Map<String, String> beanPropertyColumnMap = buildMap();

        Map<String, IValueFormater> mapValueAdapter = buildValueAdapter();

        for (int i = 0; i < sheetCount; i++) {
            if (i == (totalCount / defaultRowMaxCount)) {
                form.setLimitStart(i * defaultRowMaxCount);
                form.setLimitEnd(totalCount - (i * defaultRowMaxCount) + 1);
            } else {
                form.setLimitStart(i * defaultRowMaxCount);
                form.setLimitEnd(defaultRowMaxCount);
            }

            List<EveLogCustomize> lstBankAleve = eveLogCustomizeMapper.queryEveLogList(form);

            String sheetNameTmp = sheetName + "_第" + (i + 1) + "页";

            Dataset2ExcelHelper helper = new Dataset2ExcelHelper();
            helper.export(workbook, sheetNameTmp, beanPropertyColumnMap, mapValueAdapter, lstBankAleve);
        }

        Dataset2ExcelHelper.write2Response(request, response, fileName, workbook);

    }


    private Map<String, IValueFormater> buildValueAdapter() {
        Map<String, IValueFormater> mapAdapter = Maps.newHashMap();
        IValueFormater revindValueAdapter = new RevindValueFormater();
        mapAdapter.put("revind", revindValueAdapter);
        return mapAdapter;
    }

    private Map<String, String> buildMap() {
        Map<String, String> map = Maps.newLinkedHashMap();
        map.put("forcode", "发送方标识码");
        map.put("seqno", "系统跟踪号");
        map.put("cendtString", "交易传输时间");
        map.put("cardnbr", "主账号");
        map.put("amount", "交易金额");
        map.put("crflag", "交易金额符号");
        map.put("msgtype", "消息类型");
        map.put("proccode", "交易类型码");
        map.put("orderno", "订单号");
        map.put("tranno", "内部交易流水号");
        map.put("reserved", "内部保留域");
        map.put("revind", "冲正撤销标志");
        map.put("transtype", "主机交易类型");
        return map;
    }

}
