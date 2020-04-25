package com.hyjf.admin.manager.user.userauth;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.user.auth.AuthBean;
import com.hyjf.bank.service.user.auth.AuthService;
import com.hyjf.bank.service.user.autoup.AutoPlusService;
import com.hyjf.bank.service.user.paymentauthpage.PaymentAuthService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.HjhUserAuth;
import com.hyjf.mybatis.model.customize.admin.AdminUserRePayAuthCustomize;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 付款的页面
 *
 * @author zx
 */
@Controller
@RequestMapping(value = UserauthDefine.REQUEST_MAPPING)
public class UserRePayAuthController {
    @Autowired
    private UserauthService userauthService;
    @Autowired
    private PaymentAuthService paymentAuthService;
    @Autowired
    private AutoPlusService autoPlusService;
    @Autowired
    private AuthService authService;


    @RequestMapping(UserPayDefine.USERREPAYAUTH_LIST_ACTION)
    @RequiresPermissions(UserauthLogDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,
                             @ModelAttribute(UserauthLogDefine.USERAUTH_LIST_FORM) UserRePayAuthListBean form) {
        LogUtil.startLog(UserRePayAuthController.class.getName(), UserPayDefine.USERREPAYAUTH_LIST_ACTION);

        ModelAndView modelAndView = new ModelAndView(UserPayDefine.USER_REPAY_AUTH_LIST_PATH);
        // 创建分页8
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(UserRePayAuthController.class.getName(), UserPayDefine.USERREPAYAUTH_LIST_ACTION);
        return modelAndView;
    }


//	@RequestMapping(UserPayDefine.USER_REPAY_AUTH_QUERY)
////	@RequiresPermissions(HjhLabelDefine.PERMISSIONS_MODIFY)
//	public ModelAndView queryStatus(HttpServletRequest request, int id) {
//		LogUtil.startLog(UserPayAuthController.class.getName(), UserPayDefine.USERPAYAUTH_LIST_ACTION);
//		
//		ModelAndView modelAndView = new ModelAndView("redirect:/manager/userauth/userrepayauthlist");
//		if (id >0) {
//			HjhUserAuth payauth=paymentAuthService.getHjhUserAuthByUserId(id);
//			userauthService.updateRePayAuthRecord(id,payauth.getAutoRepayEndTime(),payauth.getAutoRepayStatus());
//			
//		}
//		LogUtil.startLog(UserPayAuthController.class.getName(), UserPayDefine.USERPAYAUTH_LIST_ACTION);
//		return modelAndView;
//	}

    @RequestMapping(UserPayDefine.USER_REPAY_AUTH_QUERY)
    @ResponseBody
    public JSONObject queryStatus(@RequestParam Integer userId) {
        JSONObject result = new JSONObject();
        logger.info("查询开始，查询用户：{}", userId);

        HjhUserAuth payauth = paymentAuthService.getHjhUserAuthByUserId(userId);

        if (payauth == null) {
            logger.error("查询出错", "用户信息不存在");
            result.put("success", "1");
            result.put("msg", "用户信息不存在");
            return result;
        }
        //String url = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + UserauthDefine.REQUEST_MAPPING + "/" + UserPayDefine.USERREPAYAUTH_LIST_ACTION;
        BankCallBean retBean = authService.getTermsAuthQuery(userId, BankCallConstant.CHANNEL_PC);
        //BankCallBean retBean = autoPlusService.getRePayAuthQuery(payauth, url, BankCallConstant.CHANNEL_PC);
        try {
            if(authService.checkDefaultConfig(retBean,AuthBean.AUTH_TYPE_REPAY_AUTH)){
                logger.info("checkDefaultConfig return");
                result.put("success", "0");
                result.put("msg", "查询成功！");
                return result;
            }

            if (retBean != null
                    && BankCallConstant.RESPCODE_SUCCESS.equals(retBean.get(BankCallConstant.PARAM_RETCODE))) {

                //HjhUserAuth payauth = paymentAuthService.getHjhUserAuthByUserId(userId);
                //userauthService.updateRePayAuthRecord(userId, payauth.getAutoRepayEndTime(), payauth.getAutoRepayStatus());
                this.authService.updateUserAuth(userId, retBean,AuthBean.AUTH_TYPE_REPAY_AUTH);
                result.put("success", "0");
                result.put("msg", "查询成功！");
            } else {
                String retCode = retBean != null ? retBean.getRetCode() : "";
                String retMessage = this.autoPlusService.getBankRetMsg(retCode);
                String retMsg = retBean.getRetMsg();
                if("请联系客服！".equals(retMessage) && StringUtils.isNotBlank(retMsg)){
                    retMessage = retMsg;
                }
                result.put("success", "1");
                result.put("msg", StringUtils.isNotEmpty(retMessage) ? retMessage : "未知错误");
            }
        } catch (Exception e) {
            logger.error("查询出错", e);
            result.put("success", "1");
            result.put("msg", e.getMessage());
        }
        logger.info("queryStatus result is: {}", result.toJSONString());
        return result;
    }

    private void createPage(HttpServletRequest request, ModelAndView modelAndView, UserRePayAuthListBean form) {

        // 封装查询条件
        Map<String, Object> authUser = new HashMap<String, Object>();
        authUser.put("userName", form.getUserName());
        authUser.put("bankid", form.getBankid());
        authUser.put("authType", form.getAuthType());
 //       Integer authTimeStart = GetDate.strYYYYMMDD2Timestamp2(form.getAuthTimeStart());
 //       Integer authTimeEnd = GetDate.strYYYYMMDD2Timestamp2(form.getAuthTimeEnd());
        authUser.put("authTimeStart", form.getAuthTimeStart());
        authUser.put("authTimeEnd", form.getAuthTimeEnd());
        authUser.put("signTimeStart", form.getSignTimeStart());
        authUser.put("signTimeEnd", form.getSignTimeEnd());

        int recordTotal = userauthService.countRecordTotalRePay(authUser);
        if (recordTotal > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
            List<AdminUserRePayAuthCustomize> recordList = userauthService.getRecordListRePay(authUser,
                    paginator.getOffset(), paginator.getLimit());
            form.setPaginator(paginator);
            form.setRecordList(recordList);
        }
        modelAndView.addObject("obj", form);
    }

    private Logger logger = LoggerFactory.getLogger(UserauthController.class);

    @RequestMapping(UserPayDefine.USER_REPAY_CANCEL_ACTION)
    @ResponseBody
    public JSONObject payCancel(@RequestParam int userId) {
        // 返回结果
        JSONObject result = new JSONObject();
        logger.info("还款授权解约开始，用户：{}", userId);

        if (userauthService.isDismissRePay(userId) > 0) {
            result.put("success", "1");
            result.put("msg", "当前用户存在未还款标的，不能解约！");
            return result;
        }
        String authType = "10";
        BankCallBean retBean = this.autoPlusService.cancelRePayAuth(userId, BankCallConstant.CHANNEL_PC);
        try {
            if (retBean != null) {
                if (BankCallConstant.RESPCODE_SUCCESS.equals(retBean.getRetCode())) {
                    // 关闭授权操作
                    autoPlusService.updateCancelRePayAuth(userId);
                    //在auth_log表中插入解约记录
                    autoPlusService.insertUserAuthLog2(userId,retBean,authType);
                    result.put("success", "0");
                    result.put("msg", "还款授权解约成功！");
                } else {
                    String retCode = retBean != null ? retBean.getRetCode() : "";
                    String retMessage = this.autoPlusService.getBankRetMsg(retCode);
                    String retMsg = retBean.getRetMsg();
                    if("请联系客服！".equals(retMessage) && StringUtils.isNotBlank(retMsg)){
                        retMessage = retMsg;
                    }
                    result.put("success", "1");
                    result.put("msg", StringUtils.isNotEmpty(retMessage) ? retMessage : "未知错误");
                }
            } else {
                result.put("success", "1");
                result.put("msg", "调用银行接口失败");
            }
        } catch (Exception e) {
            logger.error("还款授权解约出错", e);
            result.put("success", "1");
            result.put("msg", e.getMessage());
        }
        return result;
    }

    @RequestMapping(UserPayDefine.EXPORT_REPAYAUTH_ACTION)
	@RequiresPermissions(UserauthDefine.PERMISSIONS_EXPORT)
    public void exportExcel(@ModelAttribute UserPayAuthListBean form, HttpServletRequest request,
                            HttpServletResponse response) throws Exception {

        LogUtil.startLog(UserRePayAuthController.class.getName(), UserPayDefine.EXPORT_REPAYAUTH_ACTION);
        // 表格sheet名称
        String sheetName = "还款授权";
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

//		List<AdminUserAuthListCustomize> recordList = this.userauthService.getRecordList(authUser, -1, -1);
//		List<AdminUserPayAuthCustomize> recordList = userauthService.getRecordListPay(authUser,-1,-1);
        List<AdminUserRePayAuthCustomize> recordList = userauthService.getRecordListRePay(authUser, -1, -1);

        String[] titles = new String[]{"序号", "用户名", "授权金额", "签约到期日", "授权状态", "银行电子账户", "授权时间"};
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
                    AdminUserRePayAuthCustomize user = recordList.get(i);
                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);
                    if (celLength == 0) {// 序号
                        cell.setCellValue(i + 1);
                    } else if (celLength == 1) {// 用户名
                        cell.setCellValue(user.getUserName());
                    } else if (celLength == 2) {//授权金额
                        cell.setCellValue("1350000");
                    }else if (celLength == 3) {// 签约到期日
                        cell.setCellValue(user.getSignEndDate());
                    } else if (celLength == 4) {// 授权状态
                        if (Integer.valueOf(user.getAuthType()) == 1) {
                            cell.setCellValue("已授权");
                        } else if (Integer.valueOf(user.getAuthType()) == 0) {
                            cell.setCellValue("未授权");
                        } else if (Integer.valueOf(user.getAuthType()) == 2) {
                            cell.setCellValue("已解约");
                        }

                    } else if (celLength == 5) {// 银行电子账户
                        cell.setCellValue(user.getBankid());
                    } else if (celLength == 6) {// 授权时间
                        cell.setCellValue(user.getSignDate());
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
        LogUtil.endLog(UserauthDefine.THIS_CLASS, UserauthDefine.EXPORT_USERAUTH_ACTION);
    }

    /**
     * 构建查询条件
     *
     * @param form
     * @return
     */
    private Map<String, Object> buildQueryCondition(UserPayAuthListBean form) {
        // 封装查询条件
        Map<String, Object> authUser = new HashMap<String, Object>();
        authUser.put("userName", form.getUserName());
        authUser.put("bankid", form.getBankid());
        authUser.put("authType", form.getAuthType());
        authUser.put("authTimeStart", form.getAuthTimeStart());
        authUser.put("authTimeEnd", form.getAuthTimeEnd());

        authUser.put("signTimeStart", form.getSignTimeStart());
        authUser.put("signTimeEnd", form.getSignTimeEnd());
        return authUser;
    }
}
