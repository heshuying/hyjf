package com.hyjf.admin.manager.activity.returncash;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
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

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.user.manageruser.ManageUsersService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.GetterUtil;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.admin.AdminActivityReturncashCustomize;
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
@RequestMapping(value = ActivityReturncashDefine.REQUEST_MAPPING)
public class ActivityReturncashController extends BaseController {
    /**
     * 类名
     */
    private static final String THIS_CLASS = ActivityReturncashController.class.getName();

    @Autowired
    private ActivityReturncashService returncashService;
    @Resource
	private ManageUsersService usersService;

    /**
     * 返现管理画面初始化
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(ActivityReturncashDefine.INIT)
    @RequiresPermissions(value = {ActivityReturncashDefine.PERMISSIONS_RETURNCASH_VIEW, ActivityReturncashDefine.PERMISSIONS_RETURNEDCASH_VIEW }, logical = Logical.OR)
    public ModelAndView init(HttpServletRequest request, ActivicyReturncashBean form) {
        LogUtil.startLog(THIS_CLASS, ActivityReturncashDefine.RETURNCASH);
        ModelAndView modelAndView = new ModelAndView(ActivityReturncashDefine.RETURNCASH_LIST_PATH);

        Subject currentUser = SecurityUtils.getSubject();
        // 有待返现列表权限时,显示待返现列表
        if (currentUser.isPermitted(ActivityReturncashDefine.PERMISSIONS_RETURNCASH_VIEW)) {
            modelAndView = new ModelAndView(ActivityReturncashDefine.RETURNCASH_LIST_PATH);
            // 创建分页
            this.createReturncashPage(request, modelAndView, form);
            // 无待返现列表权限,有已返现列表权限时,显示已返现列表
        } 

        LogUtil.endLog(THIS_CLASS, ActivityReturncashDefine.RETURNCASH);
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
    @RequestMapping(ActivityReturncashDefine.RETURNCASH)
    @RequiresPermissions(ActivityReturncashDefine.PERMISSIONS_RETURNCASH_VIEW)
    public ModelAndView initReturncash(HttpServletRequest request, ActivicyReturncashBean form) {
        LogUtil.startLog(THIS_CLASS, ActivityReturncashDefine.RETURNCASH);
        ModelAndView modelAndView = new ModelAndView(ActivityReturncashDefine.RETURNCASH_LIST_PATH);

        // 创建分页
        this.createReturncashPage(request, modelAndView, form);
        LogUtil.endLog(THIS_CLASS, ActivityReturncashDefine.RETURNCASH);
        return modelAndView;
    }

    /**
     * 待返现管理画面查询
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(ActivityReturncashDefine.SEARCH_RETURNCASH_ACTION)
    @RequiresPermissions(ActivityReturncashDefine.PERMISSIONS_RETURNCASH_VIEW)
    public ModelAndView searchReturncash(HttpServletRequest request, ActivicyReturncashBean form) {
        LogUtil.startLog(THIS_CLASS, ActivityReturncashDefine.SEARCH_RETURNCASH_ACTION);
        ModelAndView modelAndView = new ModelAndView(ActivityReturncashDefine.RETURNCASH_LIST_PATH);

        // 创建分页
        this.createReturncashPage(request, modelAndView, form);
        LogUtil.endLog(THIS_CLASS, ActivityReturncashDefine.SEARCH_RETURNCASH_ACTION);
        return modelAndView;
    }

    /**
     * 创建待返现管理分页机能
     *
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createReturncashPage(HttpServletRequest request, ModelAndView modelAndView, ActivicyReturncashBean form) {
    	// 用户属性
		List<ParamName> userPropertys = this.usersService.getParamNameList("USER_PROPERTY");
		modelAndView.addObject("userPropertys", userPropertys);
    	
    	Map<String, Object> paraMap = new HashMap<String, Object>();
    	if(StringUtils.isNotEmpty(form.getUsernameSrch())){
    		paraMap.put("username", form.getUsernameSrch());
    	}
    	if(StringUtils.isNotEmpty(form.getAttributeSrch())){
    		paraMap.put("attribute", form.getAttributeSrch());
    	}
    	
        int cnt = this.returncashService.getReturncashRecordCount(paraMap);
        if (cnt > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), cnt);
            paraMap.put("limitStart", paginator.getOffset());
            paraMap.put("limitEnd", paginator.getLimit());
            List<AdminActivityReturncashCustomize> recordList = this.returncashService.getReturncashRecordList(paraMap);
            form.setPaginator(paginator);
            form.setRecordList(recordList);
        }
        
        BigDecimal rewardTotal = this.returncashService.getReturncashAmountTotal(new HashMap<String, Object>());
        modelAndView.addObject("rewardTotal", rewardTotal);
        
        //是否显示返现按钮
        modelAndView.addObject("showButton", false);
        modelAndView.addObject(ActivityReturncashDefine.RETURNCASH_FORM, form);
    }

    /**
     * 已现管理画面初始化
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(ActivityReturncashDefine.RETURNEDCASH)
    @RequiresPermissions(ActivityReturncashDefine.PERMISSIONS_RETURNEDCASH_VIEW)
    public ModelAndView initReturnedcash(HttpServletRequest request, ActivicyReturncashBean form) {
        LogUtil.startLog(THIS_CLASS, ActivityReturncashDefine.RETURNEDCASH);
        ModelAndView modelAndView = new ModelAndView(ActivityReturncashDefine.RETURNEDCASH_LIST_PATH);

        // 创建分页
        this.createReturnedcashPage(request, modelAndView, form);
        LogUtil.endLog(THIS_CLASS, ActivityReturncashDefine.RETURNEDCASH);
        return modelAndView;
    }

    /**
     * 已返现管理画面查询
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(ActivityReturncashDefine.SEARCH_RETURNEDCASH_ACTION)
    @RequiresPermissions(ActivityReturncashDefine.PERMISSIONS_RETURNEDCASH_VIEW)
    public ModelAndView searchReturnedcash(HttpServletRequest request, ActivicyReturncashBean form) {
        LogUtil.startLog(THIS_CLASS, ActivityReturncashDefine.SEARCH_RETURNEDCASH_ACTION);
        ModelAndView modelAndView = new ModelAndView(ActivityReturncashDefine.RETURNEDCASH_LIST_PATH);

        // 创建分页
        this.createReturnedcashPage(request, modelAndView, form);
        LogUtil.endLog(THIS_CLASS, ActivityReturncashDefine.SEARCH_RETURNEDCASH_ACTION);
        return modelAndView;
    }

    /**
     * 创建已待返现管理分页机能
     *
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createReturnedcashPage(HttpServletRequest request, ModelAndView modelAndView, ActivicyReturncashBean form) {
    	// 用户属性
		List<ParamName> userPropertys = this.usersService.getParamNameList("USER_PROPERTY");
		modelAndView.addObject("userPropertys", userPropertys);
    	
    	Map<String, Object> paraMap = new HashMap<String, Object>();
    	if(StringUtils.isNotEmpty(form.getUsernameSrch())){
    		paraMap.put("username", form.getUsernameSrch());
    	}
    	if(StringUtils.isNotEmpty(form.getAttributeSrch())){
    		paraMap.put("attribute", form.getAttributeSrch());
    	}
    	long cnt = this.returncashService.getReturnedcashRecordCount(paraMap);
        if (cnt > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), (int) cnt);
            paraMap.put("limitStart", paginator.getOffset());
            paraMap.put("limitEnd", paginator.getLimit());
            List<AdminActivityReturncashCustomize> recordList = this.returncashService.getReturnedcashRecordList(paraMap);
            form.setPaginator(paginator);
            form.setRecordList(recordList);
        }
        modelAndView.addObject(ActivityReturncashDefine.RETURNCASH_FORM, form);
    }
    
    /**
     * 返手续费
     *
     * @param request
     * @param form
     * @return
     * @throws InterruptedException 
     */
    @ResponseBody
    @RequestMapping(ActivityReturncashDefine.RETURNCASH_ACTION)
    @RequiresPermissions(ActivityReturncashDefine.PERMISSIONS_RETURNCASH_RETURNCASH)
    public synchronized String returncashAction(HttpServletRequest request, @RequestBody ActivicyReturncashBean form) throws InterruptedException {
        LogUtil.startLog(THIS_CLASS, ActivityReturncashDefine.RETURNCASH_ACTION);

        JSONObject ret = new JSONObject();

        // 用户ID
        Integer userId = GetterUtil.getInteger(form.getIds());
        if (Validator.isNull(userId)) {
            ret.put(ActivityReturncashDefine.JSON_STATUS_KEY, ActivityReturncashDefine.JSON_STATUS_NG);
            ret.put(ActivityReturncashDefine.JSON_RESULT_KEY, "活动返现发生错误,请重新操作!");
            LogUtil.errorLog(THIS_CLASS, ActivityReturncashDefine.RETURNCASH_ACTION, new Exception("参数不正确[userId="+userId+"]"));
            return ret.toString();
        }
        
        
        
        //活动奖励金额
        String rewardTotal = form.getRewardTotalStr();
        if (StringUtils.isEmpty(rewardTotal) || Double.parseDouble(rewardTotal) <= 0) {
            ret.put(ActivityReturncashDefine.JSON_STATUS_KEY, ActivityReturncashDefine.JSON_STATUS_NG);
            ret.put(ActivityReturncashDefine.JSON_RESULT_KEY, "活动返现发生错误,返现金额不能为0!");
            LogUtil.errorLog(THIS_CLASS, ActivityReturncashDefine.RETURNCASH_ACTION, new Exception("参数不正确[rewardTotal="+rewardTotal+"]"));
            return ret.toString();
        }
        
        //检查用户是否已返过钱
        if(returncashService.checkReturnCashStatus(userId)){
        	ret.put(ActivityReturncashDefine.JSON_STATUS_KEY, ActivityReturncashDefine.JSON_STATUS_NG);
            ret.put(ActivityReturncashDefine.JSON_RESULT_KEY, "活动返现发生错误,用户已返现!");
            LogUtil.errorLog(THIS_CLASS, ActivityReturncashDefine.RETURNCASH_ACTION, new Exception("用户已返现[userId="+userId+"]"));
            return ret.toString();
        }


        // 取得用户在汇付天下的账户信息
        AccountChinapnr accountChinapnr = returncashService.getChinapnrUserInfo(userId);
        // 用户未开户时,返回错误信息
        if (accountChinapnr == null) {
            ret.put(ActivityReturncashDefine.JSON_STATUS_KEY, ActivityReturncashDefine.JSON_STATUS_NG);
            ret.put(ActivityReturncashDefine.JSON_RESULT_KEY, "活动返现发生错误,用户未开户!");
            LogUtil.errorLog(THIS_CLASS, ActivityReturncashDefine.RETURNCASH_ACTION, new Exception("用户未开户[userId="+userId+"]"));
            return ret.toString();
        }

        // IP地址
        String ip = CustomUtil.getIpAddr(request);

        // 调用汇付接口(4.3.11. 自动扣款转账（商户用）)
        ChinapnrBean bean = new ChinapnrBean();
        bean.setVersion(ChinaPnrConstant.VERSION_10);
        bean.setCmdId(ChinaPnrConstant.CMDID_TRANSFER); // 消息类型(必须)
        bean.setOrdId(GetOrderIdUtils.getOrderId2(userId)); // 订单号(必须)
        bean.setTransAmt(CustomUtil.formatAmount(rewardTotal)); // 交易金额(必须)
        bean.setInCustId(accountChinapnr.getChinapnrUsrcustid().toString()); // 入账客户号(必须)
        bean.setInAcctId(""); // 入账子账户(可选)
        bean.setBgRetUrl(ChinapnrUtil.getBgRetUrl());

        // 写log用参数
        bean.setLogUserId(userId); // 操作者ID
        bean.setLogRemark("活动返现"); // 备注
        bean.setLogClient("0"); // PC
        bean.setLogIp(ip); // IP地址
        System.out.println("调用转账接口开始");
        // 调用汇付接口
        ChinapnrBean chinaPnrBean = ChinapnrUtil.callApiBg(bean);
        
        System.out.println("调用转账接口结束");

        if (chinaPnrBean == null) {
            LogUtil.errorLog(THIS_CLASS, ActivityReturncashDefine.RETURNCASH_ACTION, new Exception("调用汇付接口发生错误"));
            ret.put(ActivityReturncashDefine.JSON_STATUS_KEY, ActivityReturncashDefine.JSON_STATUS_NG);
            ret.put(ActivityReturncashDefine.JSON_RESULT_KEY, "活动返现发生错误,请重新操作!");
            return ret.toString();
        }

        int cnt = 0;
        // 接口返回正常时,执行更新操作
        if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(chinaPnrBean.getRespCode())) {

            try {
                // 返现处理
                cnt = this.returncashService.insertReturncashRecord(userId, ip, bean);
            } catch(Exception e) {
                LogUtil.errorLog(THIS_CLASS, ActivityReturncashDefine.RETURNCASH_ACTION, e);
            }
        }

        // 返现成功
        if(cnt > 0) {
            ret.put(ActivityReturncashDefine.JSON_STATUS_KEY, ActivityReturncashDefine.JSON_STATUS_OK);
            ret.put(ActivityReturncashDefine.JSON_RESULT_KEY, "活动返现操作成功!");
        } else {
            ret.put(ActivityReturncashDefine.JSON_STATUS_KEY, ActivityReturncashDefine.JSON_STATUS_NG);
            ret.put(ActivityReturncashDefine.JSON_RESULT_KEY, "活动返现发生错误,请联系技术人员处理!");
        }

        LogUtil.endLog(THIS_CLASS, ActivityReturncashDefine.RETURNCASH_ACTION);
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
    @RequestMapping(ActivityReturncashDefine.EXPORT_RETURNCASH_ACTION)
    @RequiresPermissions(ActivityReturncashDefine.PERMISSIONS_RETURNCASH_EXPORT)
    public void exportReturncashExcel(HttpServletRequest request, HttpServletResponse response, ActivicyReturncashBean form) throws Exception {
        LogUtil.startLog(THIS_CLASS, ActivityReturncashDefine.EXPORT_RETURNCASH_ACTION);
        // 表格sheet名称
        String sheetName = "待返现列表";
        Map<String, Object> paraMap = new HashMap<String, Object>();
        // 取得数据
        paraMap.put("limitStart", -1);
        paraMap.put("limitEnd", -1);
        if(StringUtils.isNotEmpty(form.getUsernameSrch())){
    		paraMap.put("username", form.getUsernameSrch());
    	}
    	if(StringUtils.isNotEmpty(form.getAttributeSrch())){
    		paraMap.put("attribute", form.getAttributeSrch());
    	}
        List<AdminActivityReturncashCustomize> recordList = this.returncashService.getReturncashRecordList(paraMap);

        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

        String[] titles = new String[] { "序号", "用户名", "真实姓名", "手机号", "推荐人", "用户属性", "活动期内累计出借额", "是否有流失用户奖励", "转账订单号", "返现金额" };
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
                	AdminActivityReturncashCustomize bean = recordList.get(i);

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
                    // 真实用户名
                    else if (celLength == 2) {
                        cell.setCellValue(bean.getTruename());
                    }
                    // 手机号
                    else if (celLength == 3) {
                        cell.setCellValue(bean.getMobile());
                    }
                    // 推荐人
                    else if (celLength == 4) {
                        cell.setCellValue(bean.getReferrerUserName());
                    }
                    // 用户属性
                    else if (celLength == 5) {
                        cell.setCellValue(bean.getAttribute());
                    }
                    // 活动期间累计出借额
                    else if (celLength == 6) {
                        cell.setCellValue(String.valueOf(bean.getInvestTotalActivity()));
                    }
                    // 是否有流失用户奖励
                    else if (celLength == 7) {
                    	cell.setCellValue(bean.getHasLostreward().equals("1")?"有":"无");
                    }
                    // 订单号
                    else if (celLength == 8) {
                        cell.setCellValue(bean.getOrderId());
                    }
                    //返现金额
                    else if (celLength == 9) {
                        cell.setCellValue(String.valueOf(bean.getRewardTotal()));
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);

        LogUtil.endLog(THIS_CLASS, ActivityReturncashDefine.EXPORT_RETURNCASH_ACTION);
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
    @RequestMapping(ActivityReturncashDefine.EXPORT_RETURNEDCASH_ACTION)
    @RequiresPermissions(ActivityReturncashDefine.PERMISSIONS_RETURNEDCASH_EXPORT)
    public void exportReturnedcashExcel(HttpServletRequest request, HttpServletResponse response, ActivicyReturncashBean form) throws Exception {
        LogUtil.startLog(THIS_CLASS, ActivityReturncashDefine.EXPORT_RETURNEDCASH_ACTION);
        // 表格sheet名称
        String sheetName = "待返现列表";
        Map<String, Object> paraMap = new HashMap<String, Object>();
        // 取得数据
        paraMap.put("limitStart", -1);
        paraMap.put("limitEnd", -1);
        if(StringUtils.isNotEmpty(form.getUsernameSrch())){
    		paraMap.put("username", form.getUsernameSrch());
    	}
    	if(StringUtils.isNotEmpty(form.getAttributeSrch())){
    		paraMap.put("attribute", form.getAttributeSrch());
    	}
        List<AdminActivityReturncashCustomize> recordList = this.returncashService.getReturnedcashRecordList(paraMap);

        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

        String[] titles = new String[] { "序号", "用户名", "真实姓名", "手机号", "推荐人", "用户属性", "活动期内累计出借额", "是否有流失用户奖励", "转账订单号", "返现金额" };
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
                	AdminActivityReturncashCustomize bean = recordList.get(i);

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
                    // 真实用户名
                    else if (celLength == 2) {
                        cell.setCellValue(bean.getTruename());
                    }
                    // 手机号
                    else if (celLength == 3) {
                        cell.setCellValue(bean.getMobile());
                    }
                    // 推荐人
                    else if (celLength == 4) {
                        cell.setCellValue(bean.getReferrerUserName());
                    }
                    // 用户属性
                    else if (celLength == 5) {
                        cell.setCellValue(bean.getAttribute());
                    }
                    // 活动期间累计出借额
                    else if (celLength == 6) {
                        cell.setCellValue(String.valueOf(bean.getInvestTotalActivity()));
                    }
                    // 是否有流失用户奖励
                    else if (celLength == 7) {
                    	cell.setCellValue(bean.getHasLostreward().equals("1")?"有":"无");
                    }
                    // 订单号
                    else if (celLength == 8) {
                        cell.setCellValue(bean.getOrderId());
                    }
                    //返现金额
                    else if (celLength == 9) {
                        cell.setCellValue(String.valueOf(bean.getRewardTotal()));
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);

        LogUtil.endLog(THIS_CLASS, ActivityReturncashDefine.EXPORT_RETURNEDCASH_ACTION);
    }

    
}
