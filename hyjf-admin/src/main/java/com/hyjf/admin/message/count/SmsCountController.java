/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.message.count;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.user.manageruser.ManageUsersService;
import com.hyjf.admin.message.log.SmsLogController;
import com.hyjf.admin.msgpush.notices.MessagePushNoticesService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.customize.SmsCountCustomize;
import com.hyjf.mybatis.model.customize.SmsLogCustomize;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 短信统计显示
 *
 * @author yinhui
 */
@Controller
@RequestMapping(value = SmsCountDefine.COUNT)
public class SmsCountController extends BaseController {

    Logger _log = LoggerFactory.getLogger(SmsCountController.class);
    @Resource
    private ManageUsersService usersService;

    @Autowired
    private SmsCountService smsCountService;
    @Autowired
    private MessagePushNoticesService messagePushNoticesServiceImpl;

    @RequestMapping(value = SmsCountDefine.INIT)
    public ModelAndView countList(HttpServletRequest request, SmsCountBean form) {
        LogUtil.startLog(SmsCountController.class.toString(), SmsCountDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(SmsCountDefine.MESSAGE_LIST_VIEW);
        this.createPage(request, modelAndView, form);

        LogUtil.endLog(SmsCountController.class.toString(), SmsCountDefine.INIT);
        return modelAndView;
    }

    private void createPage(HttpServletRequest request, ModelAndView modelandview, SmsCountBean form) {
        SmsCountCustomize smsCountCustomize = new SmsCountCustomize();
        //查询短信单价配置
        String configMoney = messagePushNoticesServiceImpl.getParamName("SMS_COUNT_PRICE", "PRICE");
        if (StringUtils.isEmpty(configMoney)) {
            configMoney = "0.042";//短信单价（0.042元/条）
        }
        DecimalFormat decimalFormat = new DecimalFormat("0.000");
        if (StringUtils.isNotEmpty(form.getPost_time_begin())) {
            //int begin = GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayStart(form.getPost_time_begin()));
            smsCountCustomize.setPost_time_begin(form.getPost_time_begin());
        }
        if (StringUtils.isNotEmpty(form.getPost_time_end())) {
            //int end = GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayEnd(form.getPost_time_end()));
            smsCountCustomize.setPost_time_end(form.getPost_time_end());
        }

        // 部门
        String[] combotreeListSrchStr = new String[]{};
        if (Validator.isNotNull(form.getCombotreeSrch())) {
            if (form.getCombotreeSrch().contains(StringPool.COMMA)) {
                combotreeListSrchStr = form.getCombotreeSrch().split(StringPool.COMMA);

            } else {
                combotreeListSrchStr = new String[]{form.getCombotreeSrch()};

            }

            if (Arrays.asList(combotreeListSrchStr).contains("-10086")) {

                //将-10086转换为 0 , 0=部门为 ‘其他’
                for (int i = 0; i < combotreeListSrchStr.length; i++) {
                    String st = combotreeListSrchStr[i];
                    if (("-10086").equals(st)) {
                        combotreeListSrchStr[i] = "0";
                    }
                }
            }
            smsCountCustomize.setCombotreeListSrch(combotreeListSrchStr);
        }

        List<SmsCountCustomize> listSmsCount = smsCountService.querySmsCountList(smsCountCustomize);
        if (listSmsCount.size() >= 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), listSmsCount.size());
            smsCountCustomize.setLimitStart(paginator.getOffset());
            smsCountCustomize.setLimitEnd(paginator.getLimit());

            List<SmsCountCustomize> listSms = smsCountService.querySmsCountList(smsCountCustomize);
            for (SmsCountCustomize sms : listSms) {
                sms.setSmsMoney(decimalFormat.format(new BigDecimal(configMoney).multiply(new BigDecimal(sms.getSmsNumber()))));
            }
            _log.info("===============================开始计算总数");
            Integer smsNumber = smsCountService.querySmsCountNumberTotal(smsCountCustomize);
            _log.info("===============================总数" + smsNumber);
            form.setSmsNumber(Integer.valueOf(smsNumber));
            form.setSmsNumberMoney(decimalFormat.format(new BigDecimal(configMoney).multiply(new BigDecimal(smsNumber))));
            _log.info("===============================总金额" + form.getSmsNumberMoney());
            modelandview.addObject(SmsCountDefine.LIST_SMS, listSms);
            form.setPaginator(paginator);
            modelandview.addObject(SmsCountDefine.MESSAGE_FORM, form);
        }
    }

    /**
     * 初始化接口
     *
     * @param request
     * @return
     */
    @RequestMapping(value = SmsCountDefine.INIT_SMSCOUNT)
    @ResponseBody
    public Map<String, Object> initSmsCount(HttpServletRequest request) {
        LogUtil.startLog(SmsLogController.class.toString(), "initSmsCount");
        Map<String, Object> map = new HashMap<String, Object>();
        SmsLogCustomize smsLogCustomize = new SmsLogCustomize();
        String initStar = "";
        String initEnd = "";
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        //获取月份，0表示1月份
        int month = c.get(Calendar.MONTH) + 1;
        //获取当前天数
        int day = c.get(Calendar.DAY_OF_MONTH);
        initStar = year + "-" + month + "-01 00:00:00";
        initEnd = year + "-" + month + "-" + day + " 23:59:59";
        smsLogCustomize.setInitStar(initStar);
        smsLogCustomize.setInitEnd(initEnd);
        //smsCountService.insertBatch(smsLogCustomize);
        map.put("success", "success");
        map.put("200", "执行成功");
        return map;
    }

    /**
     * 取得部门信息
     *
     * @param form
     * @return
     */
    @RequestMapping(value = SmsCountDefine.GET_CRMDEPARTMENT_LIST)
    @ResponseBody
    public String getCrmDepartmentListAction(@RequestBody SmsCountBean form) {
        // 部门
        String[] list = new String[]{};
        if (Validator.isNotNull(form.getIds())) {
            if (form.getIds().contains(StringPool.COMMA)) {
                list = form.getIds().split(StringPool.COMMA);
            } else {
                list = new String[]{form.getIds()};
            }
        }

        JSONArray ja = this.usersService.getCrmDepartmentList(list);
        if (ja != null) {

            //在部门树中加入 0=部门（其他）,因为前端不能显示id=0,就在后台将0=其他转换为-10086=其他
            JSONObject jo = new JSONObject();

            jo.put("id", -10086);
            jo.put("text", "其他");
            JSONObject joAttr = new JSONObject();
            joAttr.put("id", -10086);
            joAttr.put("parentid", 0);
            joAttr.put("parentname", "");
            joAttr.put("name", "其他");
            joAttr.put("listorder", 0);
            jo.put("li_attr", joAttr);
            JSONArray array = new JSONArray();
            jo.put("children", array);
            if (Validator.isNotNull(list) && ArrayUtils.contains(list, String.valueOf(-10086))) {
                JSONObject selectObj = new JSONObject();
                selectObj.put("selected", true);
                // selectObj.put("opened", true);
                jo.put("state", selectObj);
            }

            ja.add(jo);

            return ja.toString();
        }

        return StringUtils.EMPTY;
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
    @RequestMapping(value = SmsCountDefine.EXPORT_SMS)
    @RequiresPermissions(SmsCountDefine.PERMISSIONS_EXPORT)
    public void exportExcel(@ModelAttribute SmsCountBean form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        LogUtil.startLog(SmsCountController.class.getName(), "exportSms");
        //查询短信单价配置
        String configMoney = messagePushNoticesServiceImpl.getParamName("SMS_COUNT_PRICE", "PRICE");
        DecimalFormat decimalFormat = new DecimalFormat("0.000");
        // 表格sheet名称
        String sheetName = "短信统计列表";
        // 文件名称
        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
        // 需要输出的结果列表
        SmsCountCustomize smsCountCustomize = new SmsCountCustomize();
        if (StringUtils.isNotEmpty(form.getPost_time_begin())) {
            //int begin = GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayStart(form.getPost_time_begin()));
            smsCountCustomize.setPost_time_begin(form.getPost_time_begin());
        }
        if (StringUtils.isNotEmpty(form.getPost_time_end())) {
            //int end = GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayEnd(form.getPost_time_end()));
            smsCountCustomize.setPost_time_end(form.getPost_time_end());
        }

        // 部门
        String[] combotreeListSrchStr = new String[]{};
        if (Validator.isNotNull(form.getCombotreeSrch())) {
            if (form.getCombotreeSrch().contains(StringPool.COMMA)) {
                combotreeListSrchStr = form.getCombotreeSrch().split(StringPool.COMMA);
            } else {
                combotreeListSrchStr = new String[]{form.getCombotreeSrch()};
            }

            if (Arrays.asList(combotreeListSrchStr).contains("-10086")) {

                //将-10086转换为 0 , 0=部门为 ‘其他’
                for (int i = 0; i < combotreeListSrchStr.length; i++) {
                    String st = combotreeListSrchStr[i];
                    if (("-10086").equals(st)) {
                        combotreeListSrchStr[i] = "0";
                    }
                }
            }
            smsCountCustomize.setCombotreeListSrch(combotreeListSrchStr);

        }
        List<SmsCountCustomize> listSms = smsCountService.querySmsCountList(smsCountCustomize);
        //短信总条数+总费用
        Integer smsNumber = smsCountService.querySmsCountNumberTotal(smsCountCustomize);

        String[] titles = new String[]{"序号", "分公司", "数量(条)", "费用(元)", "时间"};
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, sheetName + "_第1页");

        if (listSms != null && listSms.size() > 0) {

            int sheetCount = 1;
            int rowNum = 0;

            for (int i = 0; i < listSms.size(); i++) {
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
                    SmsCountCustomize sms = listSms.get(i);
                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);
                    if (celLength == 0) {// 序号
                        cell.setCellValue(i + 1);
                    } else if (celLength == 1) { // 分公司
                        cell.setCellValue(sms.getDepartmentName());
                    } else if (celLength == 2) { // 数量(条)
                        cell.setCellValue(sms.getSmsNumber());
                    } else if (celLength == 3) { // 费用(元)
                        sms.setSmsMoney(decimalFormat.format(new BigDecimal(configMoney).multiply(new BigDecimal(sms.getSmsNumber()))));
                        cell.setCellValue(sms.getSmsMoney());
                    } else if (celLength == 4) {// 时间
                        cell.setCellValue(sms.getPosttime());
                    }
                }
            }

            //总条数
            String[] sumSmsCount = new String[]{"总条数", "", String.valueOf(smsNumber), decimalFormat.format(new BigDecimal(configMoney).multiply(new BigDecimal(smsNumber))), ""};
            Row rowTow = sheet.createRow(rowNum + 1);
            for (int celLength = 0; celLength < sumSmsCount.length; celLength++) {
                // 创建相应的单元格
                Cell cell = rowTow.createCell(celLength);
                cell.setCellValue(sumSmsCount[celLength]);
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
        LogUtil.endLog(SmsCountController.class.getName(), "exportSms");
    }
}
