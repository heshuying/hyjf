package com.hyjf.admin.finance.bankaleve;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.mapper.customize.AleveCustomizeMapper;
import com.hyjf.mybatis.model.customize.AleveLogCustomize;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 银行账务明细
 * Created by cuigq on 2018/1/22.
 */
@Controller
@RequestMapping(BankAleveDefine.REQUEST_MAPPING)
public class BankAleveController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BankAleveService aleveService;

    @Autowired
    private AleveCustomizeMapper aleveCustomizeMapper;

    @RequestMapping(BankAleveDefine.INIT)
    @RequiresPermissions(BankAleveDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, BankAleveBean form){
        ModelAndView modelAndView = new ModelAndView(BankAleveDefine.LIST_PATH);

        Date endDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        form.setStartInpdate(simpleDateFormat.format(DateUtils.addDays(endDate, -1)));
        form.setEndInpdate(simpleDateFormat.format(DateUtils.addDays(endDate, -1)));

        aleveService.queryList(form);
        modelAndView.addObject(BankAleveDefine.BANKALEVE_FORM, form);
        return modelAndView;
    }

    @RequestMapping(BankAleveDefine.BANKALEVE_LIST)
    @RequiresPermissions(BankAleveDefine.PERMISSIONS_VIEW)
    public ModelAndView search(HttpServletRequest request, BankAleveBean form) {
        ModelAndView modelAndView = new ModelAndView(BankAleveDefine.LIST_PATH);
        aleveService.queryList(form);
        modelAndView.addObject(BankAleveDefine.BANKALEVE_FORM, form);
        return modelAndView;
    }

    /**
     * 银行账务明细导出
     *
     * @param request
     * @param response
     * @param form
     * @throws Exception
     */
    @RequestMapping(BankAleveDefine.EXPORT_BANKALEVE_ACTION)
    @RequiresPermissions(BankAleveDefine.PERMISSIONS_EXPORT)
    public void exportBankAleveExcel(HttpServletRequest request, HttpServletResponse response, BankAleveBean form) throws Exception {
        String sheetName = "银行账务明细";

        //sheet默认最大行数
        int rowMaxCount = 1000;

        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + ".xls";

        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();

        int totalCount = aleveService.queryAleveLogCount(form);

        int sheetCount = (totalCount % rowMaxCount) == 0 ? totalCount / rowMaxCount : totalCount / rowMaxCount + 1;

        Map<String, String> beanPropertyColumnMap = buildMap();

        Map<String, IValueFormater> adapterMap = buildValueAdapter();

        for (int i = 0; i < sheetCount; i++) {
            if (i == (totalCount / rowMaxCount)) {
                form.setLimitStart(i * rowMaxCount);
                form.setLimitEnd(totalCount - (i * rowMaxCount) + 1);
            } else {
                form.setLimitStart(i * rowMaxCount);
                form.setLimitEnd(rowMaxCount);
            }

            List<AleveLogCustomize> lstBankAleve = aleveService.queryAleveLogList(form);
            String sheetNameTmp = sheetName + "_第" + (i + 1) + "页";

            Dataset2ExcelHelper helper = new Dataset2ExcelHelper();
            helper.export(workbook, sheetNameTmp, beanPropertyColumnMap, adapterMap, lstBankAleve);
        }

        Dataset2ExcelHelper.write2Response(request, response, fileName, workbook);
    }

    private Map<String, IValueFormater> buildValueAdapter() {
        Map<String, IValueFormater> mapAdapter = Maps.newHashMap();
        IValueFormater revindAdapter = new RevindValueFormater();
        mapAdapter.put("revind", revindAdapter);

        IValueFormater accchgAdapter = new IValueFormater() {
            @Override
            public String format(Object object) {
                if (object instanceof String) {
                    String accchg = (String) object;
                    return accchg.equals("1") ? "调账" : "";
                }
                return null;
            }
        };
        mapAdapter.put("accchg", accchgAdapter);

        IValueFormater inptimeFormatter = new IValueFormater() {
            @Override
            public String format(Object object) {
                if (object instanceof Integer) {
                    String inptimeStr = String.valueOf(object);
                    if (inptimeStr.length() == 8) {
                        String[] parts = Iterables.toArray(
                                Splitter
                                        .fixedLength(2)
                                        .split(inptimeStr),
                                String.class
                        );
                        return Joiner.on(":").join(parts);
                    } else {
                        return inptimeStr;
                    }
                }
                return "";
            }
        };
        mapAdapter.put("inptime", inptimeFormatter);

        return mapAdapter;
    }

    private Map<String, String> buildMap() {
        Map<String, String> map = Maps.newLinkedHashMap();
        map.put("bank", "银行号");
        map.put("cardnbr", "电子账号");
        map.put("amount", "交易金额");
        map.put("curNum","货币代码");
        map.put("crflag", "交易金额符号");
        map.put("valdate", "入帐日期");
        map.put("inpdate", "交易日期");
        map.put("reldate", "自然日期");
        map.put("inptime", "交易时间");
        map.put("tranno", "交易流水号");
        map.put("oriTranno", "关联交易流水号");
        map.put("transtype", "交易类型");
        map.put("desline", "交易描述");
        map.put("currBal", "交易后余额");
        map.put("forcardnbr", "对手交易账号");
        map.put("revind", "冲正撤销标志");
        map.put("accchg", "交易标识");
        map.put("seqno", "系统根踪号");
        map.put("oriNum", "原交易流水号");
        map.put("resv", "保留域");
        return map;
    }
}
