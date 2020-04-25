package com.hyjf.admin.manager.user.userauth;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.bank.service.user.auth.AuthBean;
import com.hyjf.bank.service.user.auth.AuthService;
import com.hyjf.bank.service.user.autoup.AutoPlusService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.customize.admin.AdminUserAuthListCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangqi
 * @version V1.0  
 * @package com.hyjf.admin.manager.user.userauth.UserauthController
 * @date 2017/8/14
 */
@Controller
@RequestMapping(value = UserauthDefine.REQUEST_MAPPING)
public class UserauthController extends BaseController {

    @Autowired
    private UserauthService userauthService;
    @Autowired
    private AuthService authService;

    /**
     * 权限维护画面初始化
     */
    @RequestMapping(UserauthDefine.USERAUTH_LIST_ACTION)
    @RequiresPermissions(UserauthDefine.PERMISSIONS_VIEW)
    public ModelAndView init(@ModelAttribute(UserauthDefine.USERAUTH_LIST_FORM) UserauthListCustomizeBean form) {
        LogUtil.startLog(UserauthDefine.THIS_CLASS, UserauthDefine.USERAUTH_LIST_ACTION);
        ModelAndView modelAndView = new ModelAndView(UserauthDefine.USER_AUTH_LIST_PATH);
        // 创建分页
        this.createPage(modelAndView, form);
        LogUtil.endLog(UserauthDefine.THIS_CLASS, UserauthDefine.USERAUTH_LIST_ACTION);
        return modelAndView;
    }

    /**
     * 创建权限维护分页机能
     */
    private void createPage(ModelAndView modelAndView, UserauthListCustomizeBean form) {
        // 封装查询条件
        Map<String, Object> authUser = this.buildQueryCondition(form);
        int recordTotal = this.userauthService.countRecordTotal(authUser);
        if (recordTotal > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
            List<AdminUserAuthListCustomize> recordList = this.userauthService.getRecordList(authUser,
                    paginator.getOffset(), paginator.getLimit());
            form.setPaginator(paginator);
            form.setRecordList(recordList);
            modelAndView.addObject(UserauthDefine.USERAUTH_LIST_FORM, form);
        }
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
    @RequestMapping(UserauthDefine.EXPORT_USERAUTH_ACTION)
    @RequiresPermissions(UserauthDefine.PERMISSIONS_EXPORT)
    public void exportExcel(@ModelAttribute UserauthListCustomizeBean form, HttpServletRequest request,
                            HttpServletResponse response) throws Exception {

        LogUtil.startLog(UserauthDefine.THIS_CLASS, UserauthDefine.EXPORT_USERAUTH_ACTION);
        // 表格sheet名称
        String sheetName = "授权状态";
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
        Map<String, Object> authUser = this.buildQueryCondition(form);

        List<AdminUserAuthListCustomize> recordList = this.userauthService.getRecordList(authUser, -1, -1);
        String[] titles = new String[]{"序号", "用户名", "手机号", "自动投标交易金额", "自动投标总金额", "自动投标到期日", "自动投标授权状态", "自动债转授权状态", "授权时间"};
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
                for (int celLength = 0; celLength < titles.length; celLength++) {
                    AdminUserAuthListCustomize user = recordList.get(i);
                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);
                    if (celLength == 0) {// 序号
                        cell.setCellValue(i + 1);
                    } else if (celLength == 1) {// 用户名
                        cell.setCellValue(user.getUserName());
                    } else if (celLength == 2) {// 手机号
                        cell.setCellValue(user.getMobile());
                    } else if (celLength == 3) {// 自动投标交易金额
                        cell.setCellValue("2000000");
                    } else if (celLength == 4) {// 自动投标总金额
                        cell.setCellValue("1000000000");
                    } else if (celLength == 5) {// 自动投标到期日
                        cell.setCellValue(user.getAutoInvesEndTime());
                    } else if (celLength == 6) {// 自动投标授权状态
                        cell.setCellValue(user.getAutoInvesStatus());
                    } else if (celLength == 7) {// 自动债转授权状态
                        cell.setCellValue(user.getAutoCreditStatus());
                    } else if (celLength == 8) {// 债转授权时间
                        cell.setCellValue(user.getAutoCreateTime());
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
        LogUtil.endLog(UserauthDefine.THIS_CLASS, UserauthDefine.EXPORT_USERAUTH_ACTION);
    }

    @Autowired
    private AutoPlusService autoPlusService;
    private Logger logger = LoggerFactory.getLogger(UserauthController.class);

    /**
     * 自动授权查询 - 调用江西银行接口查询
     *
     * @param userId
     */
    @RequestMapping(value = UserauthDefine.USERAUTH_QUERY_ACTION)
    @ResponseBody
    public JSONObject queryUserAuth(@RequestParam Integer userId) {
        // 返回结果
        JSONObject result = new JSONObject();
        logger.info("授权查询开始，查询用户：{}", userId);
        BankCallBean retBean = authService.getTermsAuthQuery(userId, BankCallConstant.CHANNEL_PC);
        try {
            if(authService.checkDefaultConfig(retBean,AuthBean.AUTH_TYPE_AUTO_BID)){
                logger.info("checkDefaultConfig return");
                result.put("success", "0");
                result.put("msg", "自动授权查询成功！");
                return result;
            }

            if (retBean != null && BankCallConstant.RESPCODE_SUCCESS.equals(retBean.get(BankCallConstant.PARAM_RETCODE))) {
                this.authService.updateUserAuth(userId, retBean,AuthBean.AUTH_TYPE_AUTO_BID);
                result.put("success", "0");
                result.put("msg", "自动授权查询成功！");
            } else {
                String retCode = retBean != null ? retBean.getRetCode() : "";
                String retMessage = this.authService.getBankRetMsg(retCode);
                result.put("success", "1");
                result.put("msg", StringUtils.isNotEmpty(retMessage) ? retMessage : "未知错误");
            }
        } catch (Exception e) {
            logger.error("授权查询出错", e);
            result.put("success", "1");
            result.put("msg", e.getMessage());
        }
        logger.info("queryUserAuth result is: {}", result.toJSONString());
        return result;
    }

    /**
     * 自动出借解约
     *
     * @param userId
     */
    @RequestMapping(UserauthDefine.USER_INVEST_CANCEL_ACTION)
    @ResponseBody
    public JSONObject cancelInvestAuth(@RequestParam int userId) {
        // 返回结果
        JSONObject result = new JSONObject();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
//            e.printStackTrace();
        }
        logger.info("自动出借解约开始，用户：{}", userId);
        if (!this.autoPlusService.canCancelAuth(userId)) {
            result.put("success", "1");
            result.put("msg", "当前用户存在持有中计划，不能解约！");
            return result;
        }
        String authType = "7";
        BankCallBean retBean = this.autoPlusService.cancelInvestAuth(userId, BankCallConstant.CHANNEL_PC);
        try {
            if (retBean != null) {
                if (BankCallConstant.RESPCODE_SUCCESS.equals(retBean.getRetCode())) {
                    // 关闭授权操作
                    autoPlusService.updateCancelInvestAuth(userId);
                    //在auth_log表中插入解约记录
                    autoPlusService.insertUserAuthLog2(userId,retBean,authType);
                    result.put("success", "0");
                    result.put("msg", "自动出借解约成功！");
                } else {
                    String retCode = retBean != null ? retBean.getRetCode() : "";
                    String retMessage = this.autoPlusService.getBankRetMsg(retCode);
                    result.put("success", "1");
                    result.put("msg", StringUtils.isNotEmpty(retMessage) ? retMessage : "未知错误");
                }
            } else {
                result.put("success", "1");
                result.put("msg", "调用银行接口失败");
            }
        } catch (Exception e) {
            logger.error("自动出借解约出错", e);
            result.put("success", "1");
            result.put("msg", e.getMessage());
        }
        return result;
    }

    /**
     * 自动债转解约
     *
     * @param userId
     */
    @RequestMapping(UserauthDefine.USER_CREDIT_CANCEL_ACTION)
    @ResponseBody
    public JSONObject cancelCreditAuth(@RequestParam int userId) {
        // 返回结果
        JSONObject result = new JSONObject();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
//            e.printStackTrace();
        }
        logger.info("自动债转授权开始，用户：{}", userId);
        if (!this.autoPlusService.canCancelAuth(userId)) {
            result.put("success", "1");
            result.put("msg", "当前用户存在持有中计划，不能解约！");
            return result;
        }
        String authType = "8";
        BankCallBean retBean = this.autoPlusService.cancelCreditAuth(userId, BankCallConstant.CHANNEL_PC);
        try {
            if (retBean != null) {
                if (BankCallConstant.RESPCODE_SUCCESS.equals(retBean.getRetCode())) {
                    // 关闭授权操作
                    autoPlusService.updateCancelCreditAuth(userId);
                    //在auth_log表中插入解约记录
                    autoPlusService.insertUserAuthLog2(userId,retBean,authType);
                    result.put("success", "0");
                    result.put("msg", "自动债转解约成功！");
                } else {
                    String retCode = retBean != null ? retBean.getRetCode() : "";
                    String retMessage = this.autoPlusService.getBankRetMsg(retCode);
                    result.put("success", "1");
                    result.put("msg", StringUtils.isNotEmpty(retMessage) ? retMessage : "未知错误");
                }
            } else {
                result.put("success", "1");
                result.put("msg", "调用银行接口失败");
            }
        } catch (Exception e) {
            logger.error("自动债转解约出错", e);
            result.put("success", "1");
            result.put("msg", e.getMessage());
        }
        return result;
    }

    /**
     * 构建查询条件
     *
     * @param form
     * @return
     */
    private Map<String, Object> buildQueryCondition(UserauthListCustomizeBean form) {
        // 封装查询条件
        Map<String, Object> authUser = new HashMap<String, Object>();
        authUser.put("userName", form.getUserName());
        authUser.put("recommendName", form.getRecommendName());
        // 出借授权状态
        authUser.put("autoInvesStatus", form.getAutoInvesStatus());
        // 债转授权状态
        authUser.put("autoCreditStatus", form.getAutoCreditStatus());
        // 授权时间
        authUser.put("invesAddTimeStart", form.getInvesAddTimeStart());
        authUser.put("invesAddTimeEnd", form.getInvesAddTimeEnd());
        // 签约到期日
        authUser.put("invesEndTimeStart", form.getInvestEndTimeStart());
        authUser.put("invesEndTimeEnd", form.getInvestEndTimeEnd());
        return authUser;
    }

    /**
     * 
     * 同步用户授权状态
     * @author sunss
     * @param userId
     * @param type 1自动出借授权  2债转授权
     * @return
     */
    @RequestMapping(value = UserauthDefine.USERAUTH_SYN_ACTION)
    @ResponseBody
    public JSONObject synUserAuth(@RequestParam Integer userId , @RequestParam Integer type ) {
        // 返回结果
        JSONObject result = new JSONObject();
        logger.info("同步用户授权状态，查询用户：{}", userId);
        BankCallBean retBean = autoPlusService.getUserAuthQUery(userId, type+"");

        try {
            if (retBean != null && BankCallConstant.RESPCODE_SUCCESS.equals(retBean.get(BankCallConstant.PARAM_RETCODE))) {
                this.autoPlusService.updateUserAuthState(userId, retBean);
                result.put("success", "0");
                result.put("msg", "查询成功！");
            } else {
                String retCode = retBean != null ? retBean.getRetCode() : "";
                String retMessage = this.autoPlusService.getBankRetMsg(retCode);
                result.put("success", "1");
                result.put("msg", StringUtils.isNotEmpty(retMessage) ? retMessage : "未知错误");
            }
        } catch (Exception e) {
            logger.error("授权查询出错", e);
            result.put("success", "1");
            result.put("msg", e.getMessage());
        }
        logger.info("queryUserAuth result is: {}", result.toJSONString());
        return result;
    }
}
