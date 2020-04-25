/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.manager.user.userportrait;

import com.hyjf.admin.BaseController;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.UsersPortrait;
import com.hyjf.mybatis.model.customize.UserPortraitCustomize;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;


/**
 * @author ${yaoy}
 * @version UserPortraitController, v0.1 2018/5/11 14:26
 */

@Controller
@RequestMapping(value = UserPortraitDefine.REQUEST_MAPPING)
public class UserPortraitController extends BaseController {

    @Autowired
    private UserPortraitService userPortraitService;
    private Logger logger = LoggerFactory.getLogger(UserPortraitController.class);

    /**
     * 画面初始化
     *
     * @param form
     * @return
     */
    @RequestMapping(value = UserPortraitDefine.USERPORTRAIT_LIST_ACTION)
    public ModelAndView init(@ModelAttribute(UserPortraitDefine.USERPORTRAIT_LIST_FORM) UsersPortraitBean form) {
        logger.info("用户画像页面初始化 form ：{}" + form);
        ModelAndView modelAndView = new ModelAndView(UserPortraitDefine.USER_PORTRAIT_LIST_PATH);
        // 创建分页
        this.createPage(modelAndView, form);
        return modelAndView;
    }

    /**
     * 用户画像编辑页面跳转
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(UserPortraitDefine.USERPORTRAIT_UPDATE_ACTION)
    public ModelAndView updateUserPortrait(HttpServletRequest request, UsersPortraitBean form) {
        logger.info("编辑页面跳转开始");
        ModelAndView modelAndView = new ModelAndView(UserPortraitDefine.USERPORTRAIT_UPDATE_PATH);
        if (form.getUserId() != null) {
            UsersPortrait usersPortrait = this.userPortraitService.getUsersPortraitByUserId(form.getUserId());
            modelAndView.addObject("usersPortrait", usersPortrait);
        } else {
            modelAndView.addObject("usersPortrait", null);
        }
        return modelAndView;
    }

    /**
     * 用户画像编辑保存
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(UserPortraitDefine.USERPORTRAIT_SAVE_ACTION)
    public ModelAndView saveUserPortrait(HttpServletRequest request, @ModelAttribute UsersPortraitBean form) {
        logger.info("用户画像编辑保存 form ：{}", form);
        ModelAndView modelAndView = new ModelAndView(UserPortraitDefine.USERPORTRAIT_UPDATE_PATH);
        try {
            if (form.getUserId() != null) {
                Map<String, Object> resultMap = this.userPortraitService.saveUserPortrait(form);
                if ((Boolean) resultMap.get("success")) {
                    modelAndView.addObject("success", "success");
                    modelAndView.addObject("usersPortrait", resultMap.get("usersPortrait"));
                } else {
                    modelAndView.addObject("success", "failed");
                    modelAndView.addObject("usersPortrait", form);
                    modelAndView.addObject("message", resultMap.get("msg"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modelAndView;
    }

    /**
     * 用户画像表格导出
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(UserPortraitDefine.EXPORT_USERPORTRAIT_ACTION)
    public void exportExcel(@ModelAttribute UsersPortraitBean form, HttpServletRequest request,
                            HttpServletResponse response) throws Exception {
        logger.info("表格导出");
        // 表格sheet名称
        String sheetName = "用户画像";
        // 文件名称
        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date())
                + CustomConstants.EXCEL_EXT;
        //解决IE浏览器导出列表中文乱码问题
        String userAgent = request.getHeader("user-agent").toLowerCase();
        if (userAgent.contains("msie") || userAgent.contains("like gecko")) {
            // win10 ie edge 浏览器 和其他系统的ie
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } else {
            fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
        }
        // 需要输出的结果列表

        // 封装查询条件
        Map<String, Object> param = this.buildQueryCondition(form);

//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DATE, -1);
//        String yesterday = GetDate.date_sdf.format(cal.getTime());
//        String yesterdayBegin = yesterday + " 00:00:00";
//        String yesterdayEnd = yesterday + " 23:59:59";
//        userPortrait.put("yesterdayBeginTime", GetDate.strYYYYMMDDHHMMSS2Timestamp2(yesterdayBegin));
//        userPortrait.put("yesterdayEndTime", GetDate.strYYYYMMDDHHMMSS2Timestamp2(yesterdayEnd));
        List<UserPortraitCustomize> recordList = userPortraitService.getRecordList(param, -1, -1);

        String[] titles = new String[]{"用户名", "手机号","年龄", "性别", "学历", "职业", "地域", "爱好","账户总资产（元）","账户可用金额（元）","账户待还金额（元）", "账户冻结金额（元）", "资金存留比（%）","客均收益率（%）", "累计收益（元）", "累计年化出借金额（元）", "累计充值金额（元）",
                "累计提取金额（元）", "登录活跃", "客户来源", "最后一次登录至今时长（天）", "最后一次充值至今时长（天）", "最后一次提现至今时长（天）","最后一笔回款时间", "同时出借平台数", "投龄",
                "交易笔数", "当前拥有人", "是否加微信", "出借进程", "客户投诉", "邀约客户数","邀约注册客户数","邀约充值客户数","邀约出借客户数","是否有主单","注册时间"};
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
                    sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles,
                            (sheetName + "_第" + sheetCount + "页"));
                    rowNum = 1;
                }

                // 新建一行
                Row row = sheet.createRow(rowNum);
                // 循环数据
                try {
                    for (int celLength = 0; celLength < titles.length; celLength++) {
                        UserPortraitCustomize usersPortrait = recordList.get(i);
                        // 创建相应的单元格
                        Cell cell = row.createCell(celLength);
                        if (celLength == 0) {// 用户名
                            cell.setCellValue(StringUtils.isNoneBlank(usersPortrait.getUserName()) ? usersPortrait.getUserName() : "");
                        } else if (celLength == 1) {//手机号
                            cell.setCellValue(StringUtils.isNoneBlank(usersPortrait.getMobile()) ? usersPortrait.getMobile() :  "");
                        } else if (celLength == 2) {//年龄
                            cell.setCellValue(usersPortrait.getAge() != null ? usersPortrait.getAge().toString() :  "");
                        } else if (celLength == 3) {//  "性别",
                            cell.setCellValue(StringUtils.isNoneBlank(usersPortrait.getSex()) ? usersPortrait.getSex() :  "");
                        } else if (celLength == 4) {// 学历
                            cell.setCellValue(StringUtils.isNoneBlank(usersPortrait.getEducation()) ? usersPortrait.getEducation() :  "");
                        } else if (celLength == 5) {// 职业" "
                            cell.setCellValue(StringUtils.isNoneBlank(usersPortrait.getOccupation()) ? usersPortrait.getOccupation() :  "");
                        } else if (celLength == 6) {// 地域
                            cell.setCellValue(StringUtils.isNoneBlank(usersPortrait.getCity()) ? usersPortrait.getCity() :  "");
                        } else if (celLength == 7) {// 爱好
                            cell.setCellValue(StringUtils.isNoneBlank(usersPortrait.getInterest()) ?usersPortrait.getInterest() :  "");
                        } else if (celLength == 8) {// 账户总资产（元）
                            cell.setCellValue(usersPortrait.getBankTotal()!=null ?usersPortrait.getBankTotal().toString() : "");
                        } else if (celLength == 9) {// 账户可用金额（元）
                            cell.setCellValue(usersPortrait.getBankBalance()!=null ?usersPortrait.getBankBalance().toString() :  "");
                        }else if (celLength == 10) {// "账户待还金额（元）
                            cell.setCellValue(usersPortrait.getAccountAwait()!=null ?usersPortrait.getAccountAwait().toString() :  "");
                        }else if (celLength == 11) {// "账户冻结金额（元）",
                            cell.setCellValue(usersPortrait.getBankFrost()!=null ?usersPortrait.getBankFrost().toString() :  "");
                        } else if (celLength == 12) {// "资金存留比（%）"
                            cell.setCellValue(usersPortrait.getFundRetention()!=null ?usersPortrait.getFundRetention().toString() :  "");
                        } else if (celLength == 13) {// ""客均收益率（%）",
                            cell.setCellValue(usersPortrait.getYield()!=null ?usersPortrait.getYield().toString() :  "");
                        } else if (celLength == 14) {// 累计收益
                            cell.setCellValue(usersPortrait.getInterestSum()!=null ?usersPortrait.getInterestSum().toString() :  "");
                        } else if (celLength == 15) {// 累计年化出借金额
                            cell.setCellValue(usersPortrait.getInvestSum()!=null ?usersPortrait.getInvestSum().toString() :  "");
                        } else if (celLength == 16) {// 累计充值金额
                            cell.setCellValue(usersPortrait.getRechargeSum()!=null ?usersPortrait.getRechargeSum().toString() :  "");
                        } else if (celLength == 17) {// 累计提取金额
                            cell.setCellValue(usersPortrait.getWithdrawSum()!=null ?usersPortrait.getWithdrawSum().toString() :  "");
                        } else if (celLength == 18) {// 登录活跃"
                            cell.setCellValue(StringUtils.isNoneBlank(usersPortrait.getLoginActive()) ?usersPortrait.getLoginActive() :  "");
                        } else if (celLength == 19) {// 客户来源
                            cell.setCellValue(StringUtils.isNoneBlank(usersPortrait.getCustomerSource()) ?usersPortrait.getCustomerSource() :  "");
                        } else if (celLength == 20) {// 最后一次登录至今时长
                            cell.setCellValue(usersPortrait.getLastLoginTime() != null ?usersPortrait.getLastLoginTime().toString() :  "");
                        } else if (celLength == 21) {// 最后一次充值至今时长
                            cell.setCellValue(usersPortrait.getLastRechargeTime() != null ?usersPortrait.getLastRechargeTime() .toString(): "");
                        } else if (celLength == 22) {// 最后一次提现至今时长
                            cell.setCellValue(usersPortrait.getLastWithdrawTime() != null ?usersPortrait.getLastWithdrawTime().toString() : "");
                        }else if (celLength == 23) {// 最后一笔回款时间
                            cell.setCellValue(StringUtils.isNoneBlank(usersPortrait.getLastRepayTime()) ?usersPortrait.getLastRepayTime() : "");
                        }else if (celLength == 24) {// 同时出借平台数
                            cell.setCellValue(usersPortrait.getInvestPlatform() != null ?usersPortrait.getInvestPlatform().toString(): "");
                        } else if (celLength == 25) {// 投龄
                            cell.setCellValue(usersPortrait.getInvestAge() != null ?usersPortrait.getInvestAge().toString() : "");
                        }else if (celLength == 26) {// 交易笔数
                            cell.setCellValue(usersPortrait.getTradeNumber() != null ?usersPortrait.getTradeNumber().toString() : "");
                        } else if (celLength == 27) {// 当前拥有人
                            cell.setCellValue(StringUtils.isNoneBlank(usersPortrait.getCurrentOwner()) ?usersPortrait.getCurrentOwner() : "");
                        } else if (celLength == 28) {// 是否加微信
                            cell.setCellValue(StringUtils.isNoneBlank(usersPortrait.getAddWechat())?usersPortrait.getAddWechat() : "");
                        } else if (celLength == 29) {// 出借进程
                            cell.setCellValue(StringUtils.isNoneBlank(usersPortrait.getInvestProcess())?usersPortrait.getInvestProcess() : "");
                        } else if (celLength == 30) {// 客户投诉
                            cell.setCellValue(StringUtils.isNoneBlank(usersPortrait.getCustomerComplaint())?usersPortrait.getCustomerComplaint() : "");
                        } else if (celLength == 31) {// 邀约客户数
                            cell.setCellValue(usersPortrait.getInviteCustomer() != null ?usersPortrait.getInviteCustomer().toString() : "");
                        }else if (celLength == 32) {//  "邀约注册客户数"
                            cell.setCellValue(usersPortrait.getInviteRegist() != null ?usersPortrait.getInviteRegist().toString() : "");
                        }else if (celLength == 33) {//  "邀约充值客户数"
                            cell.setCellValue(usersPortrait.getInviteRecharge() != null ?usersPortrait.getInviteRecharge().toString() :"");
                        }else if (celLength == 34) {//  ""邀约出借客户数
                            cell.setCellValue(usersPortrait.getInviteTender() != null ?usersPortrait.getInviteTender().toString() : "");
                        }else if (celLength == 35) {//  ""是否有主单"
                            cell.setCellValue(usersPortrait.getAttribute() != null ?usersPortrait.getAttribute().toString() : "");
                        }else if (celLength == 36) {//  "注册时间"
                            cell.setCellValue(StringUtils.isNoneBlank(usersPortrait.getRegTime())?usersPortrait.getRegTime() : "");
                        }
                    }
                } catch (Exception e) {
                    logger.error("错误 e :{}", e);
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
    }


    private void createPage(ModelAndView modelAndView, UsersPortraitBean form) {

        // 封装查询条件
        Map<String, Object> userPortrait = this.buildQueryCondition(form);

        int recordTotal = userPortraitService.countRecordTotal(userPortrait);
        if (recordTotal > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
            List<UserPortraitCustomize> recordList = userPortraitService.getRecordList(userPortrait,
                    paginator.getOffset(), paginator.getLimit());
            form.setPaginator(paginator);
            form.setRecordlist(recordList);
        }
        modelAndView.addObject("obj", form);
    }

    /**
     * 构建查询条件
     *
     * @param form
     * @return
     */
    private Map<String, Object> buildQueryCondition(UsersPortraitBean form) {
        // 封装查询条件
        Map<String, Object> userPortrait = new HashMap<String, Object>();

        String mobile= StringUtils.isNoneBlank(form.getMobile())?form.getMobile():null;
        String sex=StringUtils.isNoneBlank(form.getSex())?form.getSex():null;
        Integer ageStart=form.getAgeStart()!= null ?form.getAgeStart():null;
        Integer ageEnd=form.getAgeEnd()!= null ?form.getAgeEnd():null;
        BigDecimal bankTotalStart=form.getBankTotalStart()!= null?form.getBankTotalStart():null;
        BigDecimal bankTotalEnd = form.getBankTotalEnd() != null?form.getBankTotalEnd():null;
        BigDecimal interestSumStart=form.getInterestSumStart() != null? form.getInterestSumStart():null;
        BigDecimal interestSumEnd =form.getInterestSumEnd() != null?form.getInterestSumEnd():null;
        Integer tradeNumberStart=form.getTradeNumberStart() != null?form.getTradeNumberStart():null;
        Integer tradeNumberEnd=form.getTradeNumberEnd() != null?form.getTradeNumberEnd():null;
        String currentOwner = StringUtils.isNotBlank(form.getCurrentOwner())?form.getCurrentOwner():null;
        Integer attribute=form.getAttribute() != null?form.getAttribute():null;
        String investProcess=StringUtils.isNoneBlank(form.getInvestProcess())?form.getInvestProcess():null;
        String regTimeStart=StringUtils.isNoneBlank(form.getRegTimeStart())?form.getRegTimeStart()+" 00:00:00":null;
        String regTimeEnd=StringUtils.isNoneBlank(form.getRegTimeEnd())?form.getRegTimeEnd()+" 23:59:59":null;

        userPortrait.put("userName", form.getUserName());
        userPortrait.put("mobile", mobile);
        userPortrait.put("sex", sex);
        userPortrait.put("ageStart", ageStart);
        userPortrait.put("ageEnd", ageEnd);
        userPortrait.put("bankTotalStart", bankTotalStart);
        userPortrait.put("bankTotalEnd", bankTotalEnd);
        userPortrait.put("interestSumStart", interestSumStart);
        userPortrait.put("interestSumEnd", interestSumEnd);
        userPortrait.put("tradeNumberStart", tradeNumberStart);
        userPortrait.put("tradeNumberEnd", tradeNumberEnd);
        userPortrait.put("currentOwner", currentOwner);
        userPortrait.put("attribute", attribute);
        userPortrait.put("investProcess", investProcess);
        userPortrait.put("regTimeStart", regTimeStart);
        userPortrait.put("regTimeEnd", regTimeEnd);
        return userPortrait;
    }

}
