package com.hyjf.web.other;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.user.credit.CreditService;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.ValidatorCheckUtil;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.SmsConfig;
import com.hyjf.mybatis.model.customize.DataSearchCustomize;
import com.hyjf.web.bank.web.user.credit.CreditController;
import com.hyjf.web.bank.web.user.credit.CreditDefine;
import com.hyjf.web.util.WebUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.cookie.Cookie;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lisheng
 * @version DataSearchController, v0.1 2018/7/4 11:48
 */
@Controller
@RequestMapping(DataSearchDefine.REQUEST_MAPPING)
public class DataSearchController {
    @Autowired
    DataSearchService dataSearchService;

    @Autowired
    @Qualifier("smsProcesser")
    private MessageProcesser smsProcesser;
    @Autowired
    private CreditService tenderCreditService;
    static Logger _log = LoggerFactory.getLogger(DataSearchController.class);


    @RequestMapping(value =DataSearchDefine.INIT,method = RequestMethod.GET )
    public ModelAndView init(HttpServletRequest request) {
        _log.info("进入千乐数据查询");
        ModelAndView modelAndView=null;
        String userlogin = WebUtils.getCookie(request,"userlogin");
        if(StringUtils.isNotBlank(userlogin)){
            modelAndView= new ModelAndView(DataSearchDefine.LOGIN_PATH);
        }else{
            modelAndView= new ModelAndView(DataSearchDefine.LOGIN_PASSWORD);
        }
        _log.info("转跳数据界面");
        return modelAndView;
    }


    /**
     *退出登录
     * @param request
     * @return
     */
    @RequestMapping(value =DataSearchDefine.CANCLE,method = RequestMethod.GET )
    public ModelAndView cancle(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView=null;

        //String userlogin = WebUtils.getCookie(request,"userlogin");

        WebUtils.removeCookie(request, response, "userlogin","/","");
        modelAndView= new ModelAndView(DataSearchDefine.LOGIN_PASSWORD);
        return modelAndView;
    }

    /**
     *查询数据
     * @author
     * @param form
     * @return
     */
    @ResponseBody
    @RequestMapping(DataSearchDefine.DATA_SEARCH)
    public DataSearchBean init(DataSearchBean form) {
        _log.info("开始查询数据");
        int dataTotal = dataSearchService.findDataTotal(form);
        Paginator paginator = new Paginator(form.getPaginatorPage(), dataTotal);
        if(dataTotal>0){
            int offset = paginator.getOffset();
            int limit = paginator.getLimit();
            form.setLimitStart(offset);
            form.setLimitEnd(limit);
            List<DataSearchCustomize> dataList = dataSearchService.findDataList(form);
            form.setList(dataList);
        }
        form.setPaginator(paginator);
        Map<String, Object> money = dataSearchService.findMoney(form);
        form.setMoney(money);
        form.success();
        _log.info("查询数据成功");
        return form;
    }


    /**
     * 验证登录
     * @return
     */
    @ResponseBody
    @RequestMapping(value = DataSearchDefine.LOGIN)
    public ModelAndView login(HttpServletRequest request,HttpServletResponse response, DataSearchBean form) {
        ModelAndView modelAndView = new ModelAndView(DataSearchDefine.LOGIN_PASSWORD);
        String mobile = form.getMobile();
        String code = form.getCode();
        JSONObject ret = new JSONObject();
        JSONObject info = new JSONObject();
        JSONObject jo = new JSONObject();
        // 手机号码(必须,数字,最大长度)
        if (!ValidatorCheckUtil.validateRequired(info, null, null, mobile)) {
            jo.put(CreditDefine.MOBILE_ERROR, CreditDefine.ERROR_TYPE_REQUIRED);
        } else if (!ValidatorCheckUtil.validateMobile(info, null, null, mobile, 11, false)) {
            jo.put(CreditDefine.MOBILE_ERROR, CreditDefine.ERROR_TYPE_OTHERS);
        }
        // 短信验证码
        if (!ValidatorCheckUtil.validateRequired(info, null, null, code)) {
            jo.put(CreditDefine.CODE_ERROR, CreditDefine.ERROR_TYPE_REQUIRED);
        }

        if(!dataSearchService.checkMobile(mobile)){
            modelAndView.addObject("msg","手机号验证失败");
            return modelAndView;
        }
        String logincode = WebUtils.getCookie(request,"userlogin");
        if(StringUtils.equals(logincode,mobile+code)){
            modelAndView= new ModelAndView(DataSearchDefine.LOGIN_PATH);
            return modelAndView;
        }
        if (jo == null || jo.isEmpty()) {
            int cnt = this.tenderCreditService.checkMobileCode(mobile, code);
            if (cnt > 0) {
                WebUtils.addCookie(request,response,"userlogin",mobile+code,60*60);
                modelAndView= new ModelAndView(DataSearchDefine.LOGIN_PATH);
                return modelAndView;
            } else {
                modelAndView.addObject("msg","验证码错误或超过有效期");
            }
        } else {
            modelAndView.addObject("msg","手机验证码格式错误");
        }
        modelAndView.addObject(ret);
        return modelAndView;
    }

    /**
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(value = DataSearchDefine.SEND_SMS)
    @ResponseBody
    public JSONObject sendSmsCode(HttpServletRequest request,DataSearchBean form) {
        LogUtil.startLog(CreditController.class.toString(), CreditDefine.TENDER_TO_CREDIT_SEND_CODE);
        String mobile = form.getMobile();
        JSONObject ret = new JSONObject();
        JSONObject info = new JSONObject();
        JSONObject jo = new JSONObject();

        if(!dataSearchService.checkMobile(mobile)){
            ret.put(CustomConstants.MSG,"手机号验证失败");
            return ret;
        }
        SmsConfig smsConfig = tenderCreditService.getSmsConfig();
        String ip = GetCilentIP.getIpAddr(request);
        String ipCount = RedisUtils.get(ip + ":MaxIpCount");
        if (org.apache.commons.lang.StringUtils.isEmpty(ipCount)) {
            ipCount = "0";
            RedisUtils.set(ip + ":MaxIpCount", "0");
        }
        if (Integer.valueOf(ipCount) >= smsConfig.getMaxIpCount()) {
            if (Integer.valueOf(ipCount) == smsConfig.getMaxIpCount()) {

                RedisUtils.set(ip + ":MaxIpCount", (Integer.valueOf(ipCount) + 1) + "", 24 * 60 * 60);
            }

            jo.put(CreditDefine.CODE_ERROR, CreditDefine.ERROR_MAXCOUNT_OTHERS);
            ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
            ret.put(CustomConstants.DATA, jo);
            ret.put(CustomConstants.MSG, "IP访问次数超限");
            LogUtil.errorLog(CreditController.class.toString(), CreditDefine.TENDER_TO_CREDIT_SEND_CODE, "短信验证码发送失败", null);
            return ret;
        }
        // 判断最大发送数max_phone_count
        String count = RedisUtils.get(mobile + ":MaxPhoneCount");
        if (org.apache.commons.lang.StringUtils.isEmpty(count)) {
            count = "0";
            RedisUtils.set(mobile + ":MaxPhoneCount", "0");
        }
        if (Integer.valueOf(count) >= smsConfig.getMaxPhoneCount()) {
            if (Integer.valueOf(count) == smsConfig.getMaxPhoneCount()) {
                try {
                    tenderCreditService.sendSms(mobile, "手机发送次数超限");
                } catch (Exception e) {
                    LogUtil.errorLog(CreditController.class.toString(), CreditDefine.TENDER_TO_CREDIT_SEND_CODE, e);
                }
                RedisUtils.set(mobile + ":MaxPhoneCount", (Integer.valueOf(count) + 1) + "", 24 * 60 * 60);
            }
            try {
                tenderCreditService.sendEmail(mobile, "手机发送次数超限");
            } catch (Exception e) {
                LogUtil.errorLog(CreditController.class.toString(), CreditDefine.TENDER_TO_CREDIT_SEND_CODE, e);
            }
            jo.put(CreditDefine.CODE_ERROR, CreditDefine.ERROR_MAXCOUNT_OTHERS);
            ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
            ret.put(CustomConstants.DATA, jo);
            ret.put(CustomConstants.MSG, "手机发送次数超限");
            return ret;
        }
        // 判断发送间隔时间
        String intervalTime = RedisUtils.get(mobile + ":IntervalTime");
        if (!org.apache.commons.lang.StringUtils.isEmpty(intervalTime)) {
            jo.put(CreditDefine.CODE_ERROR, CreditDefine.ERROR_INTERVAL_TIME_OTHERS);
            ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
            ret.put(CustomConstants.DATA, jo);
            ret.put(CustomConstants.MSG, "发送时间间隔太短");
            return ret;
        }

        if (!ValidatorCheckUtil.validateRequired(info, null, null, mobile)) {
            jo.put(CreditDefine.MOBILE_ERROR, CreditDefine.ERROR_TYPE_REQUIRED);
        } else if (!ValidatorCheckUtil.validateMobile(info, null, null, mobile, 11, false)) {
            jo.put(CreditDefine.MOBILE_ERROR, CreditDefine.ERROR_TYPE_OTHERS);
        }

        // 手机验证码
        if (jo == null || jo.isEmpty()) {
            // 生成验证码
            String checkCode = GetCode.getRandomSMSCode(6);
            Map<String, String> param = new HashMap<String, String>();
            param.put("val_code", checkCode);
            // 发送短信验证码
            SmsMessage smsMessage = new SmsMessage(null, param, mobile, null, MessageDefine.SMSSENDFORMOBILE, null, CustomConstants.PARAM_TPL_ZHUCE, CustomConstants.CHANNEL_TYPE_NORMAL);
            Integer result = smsProcesser.gather(smsMessage);
            // checkCode过期时间，默认120秒
            RedisUtils.set(mobile + ":MaxValidTime", checkCode, smsConfig.getMaxValidTime() == null ? 120 : smsConfig.getMaxValidTime() * 60);
            // 发送checkCode最大时间间隔，默认60秒
            RedisUtils.set(mobile + ":IntervalTime", mobile, smsConfig.getMaxIntervalTime() == null ? 60 : smsConfig.getMaxIntervalTime());

            // 短信发送成功后处理
            if (result != null && result == 1) {
                // 累计IP次数
                String currentMaxIpCount = RedisUtils.get(ip + ":MaxIpCount");
                if (org.apache.commons.lang.StringUtils.isEmpty(currentMaxIpCount)) {
                    currentMaxIpCount = "0";
                }
                // 累加手机次数
                String currentMaxPhoneCount = RedisUtils.get(mobile + ":MaxPhoneCount");
                if (org.apache.commons.lang.StringUtils.isEmpty(currentMaxPhoneCount)) {
                    currentMaxPhoneCount = "0";
                }
                RedisUtils.set(ip + ":MaxIpCount", (Integer.valueOf(currentMaxIpCount) + 1) + "", 24 * 60 * 60);
                RedisUtils.set(mobile + ":MaxPhoneCount", (Integer.valueOf(currentMaxPhoneCount) + 1) + "", 24 * 60 * 60);
            }

            // 保存短信验证码
            this.tenderCreditService.saveSmsCode(mobile, checkCode);
            jo.put("status", 0);
            ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
            ret.put(CustomConstants.DATA, jo);
            String maxValidTime = "60";
            Integer time = smsConfig.getMaxIntervalTime();
            if (time != null) {
                maxValidTime = time + "";
            }
            ret.put(CustomConstants.MAX_VALIDTIME, maxValidTime);
            ret.put(CustomConstants.MSG, "");
            return ret;
        } else {
            ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
            ret.put(CustomConstants.DATA, jo);
            ret.put(CustomConstants.MSG, "系统错误");
            return ret;
        }
    }


    /**
     * 数据导出
     *
     * @param form
     * @return
     */
    @RequestMapping(DataSearchDefine.EXPORT_DATA)
    public void exportAction(DataSearchBean form,HttpServletResponse response) throws Exception {
        form.setReffername(URLDecoder.decode(form.getReffername(), "UTF-8"));
        form.setUsername(URLDecoder.decode(form.getUsername(), "UTF-8"));
        form.setTruename(URLDecoder.decode(form.getTruename(), "UTF-8"));
        // 表格sheet名称
        String sheetName = "数据导出";
        List<DataSearchCustomize> resultList=null;
        int dataTotal = dataSearchService.findDataTotal(form);
        if(dataTotal>0){
            resultList= dataSearchService.findExportDataList(form);
        }

        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

        String[] titles = new String[]
                { "序号", "注册时间", "用户名", "姓名", "手机号","推荐人姓名", "出借类型",
                "项目/智投编号",  "出借金额", "出借期限","年化金额"
                        ,"佣金7%","出借时间"
                };
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();

        // 生成一个表格
        HSSFSheet sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, sheetName + "_第1页");

        if (resultList != null && resultList.size() > 0) {

            int sheetCount = 1;
            int rowNum = 0;

            for (int i = 0; i<10000&&i < resultList.size(); i++) {
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
                    DataSearchCustomize record = resultList.get(i);
                    Cell cell = row.createCell(celLength);

                    // 序号
                    if (celLength == 0) {
                        cell.setCellValue(i + 1);
                    }
                    else if (celLength == 1) {
                        cell.setCellValue(record.getReg_time());//注册日期
                    }
                    else if (celLength == 2) {
                        cell.setCellValue(record.getUsername());//用户名
                    }
                    else if (celLength == 3) {
                        cell.setCellValue(record.getTruename());//姓名
                    }
                    else if (celLength == 4) {
                        cell.setCellValue(record.getMobile());//手机号
                    }else if (celLength == 5) {
                        cell.setCellValue(record.getReffername());//推荐人姓名
                    }
                    else if (celLength == 6) {
                        cell.setCellValue(record.getType());//出借类型
                    }

                    else if (celLength == 7) {
                        cell.setCellValue(record.getPlannid());//项目/计划编号
                    }
                    else if (celLength == 8) {
                        cell.setCellValue(record.getAccount());//出借金额
                    }
                    else if (celLength == 9) {
                        cell.setCellValue(record.getBorrow_period());//出借期限
                    }else if (celLength == 10) {
                        cell.setCellValue(record.getYearAccount());//年化金额
                    }else if (celLength == 11) {
                        cell.setCellValue(record.getMoney());//佣金7%
                    }else if (celLength == 12) {
                        cell.setCellValue(record.getAddtimes());//出借日期
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
    }

}
