package com.hyjf.admin.finance.returncash;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.finance.pushMoney.PushMoneyManageDefine;
import com.hyjf.common.http.HtmlUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.GetterUtil;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.customize.ReturncashCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;

/**
 * @package com.hyjf.admin.finance.returncash
 * @author GOGTZ-T
 * @date 2015/11/29 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = ReturncashDefine.REQUEST_MAPPING)
public class ReturncashController extends BaseController {
    /**
     * 类名
     */
    private static final String THIS_CLASS = ReturncashController.class.getName();

    @Autowired
    private ReturncashService returncashService;

    /**
     * 返现管理画面初始化
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(ReturncashDefine.INIT)
    @RequiresPermissions(value = { ReturncashDefine.PERMISSIONS_RETURNCASH_VIEW,
            ReturncashDefine.PERMISSIONS_RETURNEDCASH_VIEW }, logical = Logical.OR)
    public ModelAndView init(HttpServletRequest request, ReturncashBean form) {
        LogUtil.startLog(THIS_CLASS, ReturncashDefine.RETURNCASH);
        ModelAndView modelAndView = new ModelAndView(ReturncashDefine.RETURNCASH_LIST_PATH);

        Subject currentUser = SecurityUtils.getSubject();
        // 有待返现列表权限时,显示待返现列表
        if (currentUser.isPermitted(ReturncashDefine.PERMISSIONS_RETURNCASH_VIEW)) {
            modelAndView = new ModelAndView(ReturncashDefine.RETURNCASH_LIST_PATH);
            // 创建分页
            this.createReturncashPage(request, modelAndView, form);
            // 无待返现列表权限,有已返现列表权限时,显示已返现列表
        } else if (currentUser.isPermitted(ReturncashDefine.PERMISSIONS_RETURNEDCASH_VIEW)) {
            modelAndView = new ModelAndView(ReturncashDefine.RETURNEDCASH_LIST_PATH);
            // 创建分页
            this.createReturnedcashPage(request, modelAndView, form);
        }

        LogUtil.endLog(THIS_CLASS, ReturncashDefine.RETURNCASH);
        return modelAndView;
    }

    // ***********************************************待返现画面Start****************************************************
    /**
     * 待返现管理画面初始化
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(ReturncashDefine.RETURNCASH)
    @RequiresPermissions(ReturncashDefine.PERMISSIONS_RETURNCASH_VIEW)
    public ModelAndView initReturncash(HttpServletRequest request, ReturncashBean form) {
        LogUtil.startLog(THIS_CLASS, ReturncashDefine.RETURNCASH);
        ModelAndView modelAndView = new ModelAndView(ReturncashDefine.RETURNCASH_LIST_PATH);

        // 创建分页
        this.createReturncashPage(request, modelAndView, form);
        LogUtil.endLog(THIS_CLASS, ReturncashDefine.RETURNCASH);
        return modelAndView;
    }

    /**
     * 待返现管理画面查询
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(ReturncashDefine.SEARCH_RETURNCASH_ACTION)
    @RequiresPermissions(ReturncashDefine.PERMISSIONS_RETURNCASH_VIEW)
    public ModelAndView searchReturncash(HttpServletRequest request, ReturncashBean form) {
        LogUtil.startLog(THIS_CLASS, ReturncashDefine.SEARCH_RETURNCASH_ACTION);
        ModelAndView modelAndView = new ModelAndView(ReturncashDefine.RETURNCASH_LIST_PATH);

        // 创建分页
        this.createReturncashPage(request, modelAndView, form);
        LogUtil.endLog(THIS_CLASS, ReturncashDefine.SEARCH_RETURNCASH_ACTION);
        return modelAndView;
    }

    /**
     * 创建待返现管理分页机能
     *
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createReturncashPage(HttpServletRequest request, ModelAndView modelAndView, ReturncashBean form) {
        int cnt = this.returncashService.getReturncashRecordCount(form);
        if (cnt > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), cnt);
            form.setLimitStart(paginator.getOffset());
            form.setLimitEnd(paginator.getLimit());
            List<ReturncashCustomize> recordList = this.returncashService.getReturncashRecordList(form);
            form.setPaginator(paginator);
            form.setRecordList(recordList);
        }
        modelAndView.addObject(ReturncashDefine.RETURNCASH_FORM, form);
    }

    /**
     * 返手续费
     *
     * @param request
     * @param form
     * @return
     */
    @ResponseBody
    @RequestMapping(ReturncashDefine.RETURNCASH_ACTION)
    @RequiresPermissions(ReturncashDefine.PERMISSIONS_RETURNCASH_RETURNCASH)
    public String returncashAction(HttpServletRequest request, @RequestBody ReturncashBean form) {
        LogUtil.startLog(THIS_CLASS, ReturncashDefine.RETURNCASH_ACTION);

        JSONObject ret = new JSONObject();

        // 用户ID
        Integer userId = GetterUtil.getInteger(form.getIds());
        if (Validator.isNull(userId)) {
            ret.put(ReturncashDefine.JSON_STATUS_KEY, ReturncashDefine.JSON_STATUS_NG);
            ret.put(ReturncashDefine.JSON_RESULT_KEY, "返手续费发生错误,请重新操作!");
            LogUtil.errorLog(THIS_CLASS, ReturncashDefine.RETURNCASH_ACTION, new Exception("参数不正确[userId=" + userId
                    + "]"));
            return ret.toString();
        }

        // 取得用户信息
        ReturncashBean param = new ReturncashBean();
        param.setUserId(userId);
        List<ReturncashCustomize> userList = returncashService.getReturncashRecordList(param);
        ReturncashCustomize returncash = null;
        // 用户信息不存在时,返回错误信息
        if (userList == null || userList.size() == 0) {
            ret.put(ReturncashDefine.JSON_STATUS_KEY, ReturncashDefine.JSON_STATUS_NG);
            ret.put(ReturncashDefine.JSON_RESULT_KEY, "返手续费发生错误,请重新操作!");
            LogUtil.errorLog(THIS_CLASS, ReturncashDefine.RETURNCASH_ACTION, new Exception("参数不正确[userId=" + userId
                    + "]"));
            return ret.toString();
        } else {
            returncash = userList.get(0);
        }

        // 取得用户在汇付天下的账户信息
        AccountChinapnr accountChinapnr = returncashService.getChinapnrUserInfo(userId);
        // 用户未开户时,返回错误信息
        if (accountChinapnr == null) {
            ret.put(ReturncashDefine.JSON_STATUS_KEY, ReturncashDefine.JSON_STATUS_NG);
            ret.put(ReturncashDefine.JSON_RESULT_KEY, "返手续费发生错误,请重新操作!");
            LogUtil.errorLog(THIS_CLASS, ReturncashDefine.RETURNCASH_ACTION, new Exception("参数不正确[userId=" + userId
                    + "]"));
            return ret.toString();
        }

        // IP地址
        String ip = CustomUtil.getIpAddr(request);

        // 查询商户子账户余额
        ChinapnrBean accountBean = new ChinapnrBean();
        // 构建请求参数
        accountBean.setVersion(ChinaPnrConstant.VERSION_10);
        accountBean.setCmdId(ChinaPnrConstant.CMDID_QUERY_ACCTS);// 消息类型(必须)
        accountBean.setMerCustId(PropUtils.getSystem(ChinaPnrConstant.PROP_MERCUSTID)); // 商户客户号
        // 发送请求获取结果
        ChinapnrBean resultBean = ChinapnrUtil.callApiBg(accountBean);
        String respCode = resultBean == null ? "" : resultBean.getRespCode();
        // 如果接口调用成功
        if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode)) {
            // 如果接口返回的字符串不为空
            if (StringUtils.isNotBlank(resultBean.getAcctDetails())) {
                JSONArray acctDetailsList = JSONArray.parseArray(resultBean.getAcctDetails());
                for (Object object : acctDetailsList) {
                    JSONObject acctObject = (JSONObject) object;
                    // 查询充值手续费返现子账户的余额
                    if (PropUtils.getSystem(ChinaPnrConstant.PROP_MERACCT08).equals(acctObject.getString("SubAcctId"))) {
                        BigDecimal avlBalance = acctObject.getBigDecimal("AvlBal");
                        if (avlBalance.compareTo(new BigDecimal(returncash.getMaybackmoney())) < 0) {
                            LogUtil.errorLog(THIS_CLASS, PushMoneyManageDefine.CONFIRM_PUSHMONEY, new Exception(
                                    "调用汇付接口发生错误"));
                            ret.put(PushMoneyManageDefine.JSON_STATUS_KEY, PushMoneyManageDefine.JSON_STATUS_NG);
                            ret.put(PushMoneyManageDefine.JSON_RESULT_KEY, "充值手续费返现子账户余额不足,请先充值或向该子账户转账");
                            return ret.toString();
                        }
                    }
                }
            }
        }

        // 调用汇付接口(4.3.11. 自动扣款转账（商户用）)
        ChinapnrBean bean = new ChinapnrBean();
        bean.setVersion(ChinaPnrConstant.VERSION_10);
        bean.setCmdId(ChinaPnrConstant.CMDID_TRANSFER); // 消息类型(必须)
        bean.setOrdId(GetOrderIdUtils.getOrderId2(userId)); // 订单号(必须)
        bean.setOutCustId(PropUtils.getSystem(ChinaPnrConstant.PROP_MERCUSTID));// 出账客户号(必须)
        bean.setOutAcctId(PropUtils.getSystem(ChinaPnrConstant.PROP_MERACCT08));// 出账子账户(必须)
        bean.setTransAmt(returncash.getMaybackmoney()); // 交易金额(必须)
        bean.setInCustId(accountChinapnr.getChinapnrUsrcustid().toString()); // 入账客户号(必须)
        bean.setInAcctId(""); // 入账子账户(可选)
        bean.setBgRetUrl(ChinapnrUtil.getBgRetUrl());

        // 写log用参数
        bean.setLogUserId(userId); // 操作者ID
        bean.setLogRemark("充值手续费返还"); // 备注
        bean.setLogClient("0"); // PC
        bean.setLogIp(ip); // IP地址

        // 调用汇付接口
        ChinapnrBean chinaPnrBean = ChinapnrUtil.callApiBg(bean);

        if (chinaPnrBean == null) {
            LogUtil.errorLog(THIS_CLASS, ReturncashDefine.RETURNCASH_ACTION, new Exception("调用汇付接口发生错误"));
            ret.put(ReturncashDefine.JSON_STATUS_KEY, ReturncashDefine.JSON_STATUS_NG);
            ret.put(ReturncashDefine.JSON_RESULT_KEY, "返手续费发生错误,请重新操作!");
            return ret.toString();
        }

        int cnt = 0;
        // 接口返回正常时,执行更新操作
        if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(chinaPnrBean.getRespCode())) {
            // 设置IP地址
            returncash.setIp(ip);

            try {
                // 返现处理
                cnt = this.returncashService.insertReturncashRecord(returncash, bean);
            } catch (Exception e) {
                LogUtil.errorLog(THIS_CLASS, ReturncashDefine.RETURNCASH_ACTION, e);
            }
        }

        // 返现成功
        if (cnt > 0) {
            ret.put(ReturncashDefine.JSON_STATUS_KEY, ReturncashDefine.JSON_STATUS_OK);
            ret.put(ReturncashDefine.JSON_RESULT_KEY, "返手续费操作成功!");
        } else {
            ret.put(ReturncashDefine.JSON_STATUS_KEY, ReturncashDefine.JSON_STATUS_NG);
            ret.put(ReturncashDefine.JSON_RESULT_KEY, "返手续费发生错误,请联系技术人员处理!");
        }

        LogUtil.endLog(THIS_CLASS, ReturncashDefine.RETURNCASH_ACTION);
        return ret.toString();
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
    @RequestMapping(ReturncashDefine.EXPORT_RETURNCASH_ACTION)
    @RequiresPermissions(ReturncashDefine.PERMISSIONS_RETURNCASH_EXPORT)
    public void exportReturncashExcel(HttpServletRequest request, HttpServletResponse response, ReturncashBean form)
        throws Exception {
        LogUtil.startLog(THIS_CLASS, ReturncashDefine.EXPORT_RETURNCASH_ACTION);
        // 表格sheet名称
        String sheetName = "待返现列表";

        // 取得数据
        form.setLimitStart(-1);
        form.setLimitEnd(-1);
        List<ReturncashCustomize> recordList = this.returncashService.getReturncashRecordList(form);

        String fileName =
                sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

        String[] titles = new String[] { "序号", "用户名", "分公司", "分部", "团队", "待返现充值金额", "待返手续费", "待返现出借金额", "返现金额" };
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
                    sheet =
                            ExportExcel
                                    .createHSSFWorkbookTitle(workbook, titles, (sheetName + "_第" + sheetCount + "页"));
                    rowNum = 1;
                }

                // 新建一行
                Row row = sheet.createRow(rowNum);
                // 循环数据
                for (int celLength = 0; celLength < titles.length; celLength++) {
                    ReturncashCustomize bean = recordList.get(i);

                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);

                    // 序号
                    if (celLength == 0) {
                        cell.setCellValue(i + 1);
                    }
                    // 用户名
                    else if (celLength == 1) {
                        cell.setCellValue(bean.getUsername());
                    }
                    // 分公司
                    else if (celLength == 2) {
                        cell.setCellValue(bean.getRegionName());
                    }
                    // 分部
                    else if (celLength == 3) {
                        cell.setCellValue(bean.getBranchName());
                    }
                    // 团队
                    else if (celLength == 4) {
                        cell.setCellValue(HtmlUtil.unescape(bean.getDepartmentName()));
                    }
                    // 待返现充值金额
                    else if (celLength == 5) {
                        cell.setCellValue(bean.getRecMoney().toString());
                    }
                    // 待返手续费
                    else if (celLength == 6) {
                        cell.setCellValue(bean.getFee().toString());
                    }
                    // 待返现出借金额
                    else if (celLength == 7) {
                        cell.setCellValue(bean.getInMoney().toString());
                    }
                    // 返现金额
                    else if (celLength == 8) {
                        cell.setCellValue(bean.getMaybackmoney());
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);

        LogUtil.endLog(THIS_CLASS, ReturncashDefine.EXPORT_RETURNCASH_ACTION);
    }

    // ***********************************************待返现画面End****************************************************

    // ***********************************************已返现画面Start****************************************************
    /**
     * 已现管理画面初始化
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(ReturncashDefine.RETURNEDCASH)
    @RequiresPermissions(ReturncashDefine.PERMISSIONS_RETURNEDCASH_VIEW)
    public ModelAndView initReturnedcash(HttpServletRequest request, ReturncashBean form) {
        LogUtil.startLog(THIS_CLASS, ReturncashDefine.RETURNEDCASH);
        ModelAndView modelAndView = new ModelAndView(ReturncashDefine.RETURNEDCASH_LIST_PATH);

        // 创建分页
        this.createReturnedcashPage(request, modelAndView, form);
        LogUtil.endLog(THIS_CLASS, ReturncashDefine.RETURNEDCASH);
        return modelAndView;
    }

    /**
     * 已返现管理画面查询
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(ReturncashDefine.SEARCH_RETURNEDCASH_ACTION)
    @RequiresPermissions(ReturncashDefine.PERMISSIONS_RETURNEDCASH_VIEW)
    public ModelAndView searchReturnedcash(HttpServletRequest request, ReturncashBean form) {
        LogUtil.startLog(THIS_CLASS, ReturncashDefine.SEARCH_RETURNEDCASH_ACTION);
        ModelAndView modelAndView = new ModelAndView(ReturncashDefine.RETURNEDCASH_LIST_PATH);

        // 创建分页
        this.createReturnedcashPage(request, modelAndView, form);
        LogUtil.endLog(THIS_CLASS, ReturncashDefine.SEARCH_RETURNEDCASH_ACTION);
        return modelAndView;
    }

    /**
     * 创建已待返现管理分页机能
     *
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createReturnedcashPage(HttpServletRequest request, ModelAndView modelAndView, ReturncashBean form) {
    	
        long cnt = this.returncashService.getReturnedcashRecordCount(form);
        if (cnt > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), (int) cnt);
            form.setLimitStart(paginator.getOffset());
            form.setLimitEnd(paginator.getLimit());
            List<ReturncashCustomize> recordList = this.returncashService.getReturnedcashRecordList(form);
            form.setPaginator(paginator);
            form.setRecordList(recordList);
        }
        modelAndView.addObject(ReturncashDefine.RETURNCASH_FORM, form);
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
    @RequestMapping(ReturncashDefine.EXPORT_RETURNEDCASH_ACTION)
    @RequiresPermissions(ReturncashDefine.PERMISSIONS_RETURNEDCASH_EXPORT)
    public void exportReturnedcashExcel(HttpServletRequest request, HttpServletResponse response, ReturncashBean form)
        throws Exception {
        LogUtil.startLog(THIS_CLASS, ReturncashDefine.EXPORT_RETURNCASH_ACTION);
        // 表格sheet名称
        String sheetName = "已返现列表";

        // 取得数据
        form.setLimitStart(-1);
        form.setLimitEnd(-1);
    	//设置默认查询时间
		if(StringUtils.isEmpty(form.getAddtimeStartSrch())){
			form.setAddtimeStartSrch(GetDate.getDate("yyyy-MM-dd"));
		}
		if(StringUtils.isEmpty(form.getAddtimeEndSrch())){
			form.setAddtimeEndSrch(GetDate.getDate("yyyy-MM-dd"));
		}
        List<ReturncashCustomize> recordList = this.returncashService.getReturnedcashRecordList(form);

        String fileName =
                sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

        String[] titles = new String[] { "序号", "用户名", "分公司", "分部", "团队", "返现金额", "返现时间", "状态", "操作员" };
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
                    sheet =
                            ExportExcel
                                    .createHSSFWorkbookTitle(workbook, titles, (sheetName + "_第" + sheetCount + "页"));
                    rowNum = 1;
                }

                // 新建一行
                Row row = sheet.createRow(rowNum);
                // 循环数据
                for (int celLength = 0; celLength < titles.length; celLength++) {
                    ReturncashCustomize bean = recordList.get(i);

                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);

                    // 序号
                    if (celLength == 0) {
                        cell.setCellValue(i + 1);
                    }
                    // 用户名
                    else if (celLength == 1) {
                        cell.setCellValue(bean.getUsername());
                    }
                    // 分公司
                    else if (celLength == 2) {
                        cell.setCellValue(bean.getRegionName());
                    }
                    // 分部
                    else if (celLength == 3) {
                        cell.setCellValue(bean.getBranchName());
                    }
                    // 团队
                    else if (celLength == 4) {
                        cell.setCellValue(HtmlUtil.unescape(bean.getDepartmentName()));
                    }
                    // 返现金额
                    else if (celLength == 5) {
                        cell.setCellValue(bean.getMoney().toString());
                    }
                    // 返现时间
                    else if (celLength == 6) {
                        cell.setCellValue(bean.getAddtime());
                    }
                    // 状态
                    else if (celLength == 7) {
                        cell.setCellValue(bean.getStatus());
                    }
                    // 操作员
                    else if (celLength == 8) {
                        cell.setCellValue(bean.getOperator());
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);

        LogUtil.endLog(THIS_CLASS, ReturncashDefine.EXPORT_RETURNCASH_ACTION);
    }

    // ***********************************************已返现画面End****************************************************

    /**
     * 取得部门信息
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping("getCrmDepartmentList")
    @ResponseBody
    public String getCrmDepartmentListAction(@RequestBody ReturncashBean form) {
        // 部门
        String[] list = new String[] {};
        if (Validator.isNotNull(form.getIds())) {
            if (form.getIds().contains(StringPool.COMMA)) {
                list = form.getIds().split(StringPool.COMMA);
            } else {
                list = new String[] { form.getIds() };
            }
        }

        JSONArray ja = this.returncashService.getCrmDepartmentList(list);
        if (ja != null) {
            return ja.toString();
        }

        return StringUtils.EMPTY;
    }
}
