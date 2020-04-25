package com.hyjf.admin.finance.pushMoney;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.finance.returncash.ReturncashService;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.cache.RedisUtils;
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
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BorrowApicron;
import com.hyjf.mybatis.model.auto.TenderCommission;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.PushMoneyCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

/**
 * 提成管理
 *
 * @author HBZ
 */
@Controller
@RequestMapping(value = PushMoneyManageDefine.REQUEST_MAPPING)
public class PushMoneyManageController extends BaseController {
    /**
     * 类名
     */
    private static final String THIS_CLASS = PushMoneyManageController.class.getName();

    @Autowired
    private PushMoneyManageService pushMoneyService;

    @Autowired
    private ReturncashService returncashService;

    // ***********************提成列表*****************************
    /**
     * 分页
     *
     * @param request
     * @param modeAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modeAndView, PushMoneyManageBean form) {

        PushMoneyCustomize pushMoneyCustomize = new PushMoneyCustomize();
        BeanUtils.copyProperties(form, pushMoneyCustomize);

        Integer count = this.pushMoneyService.queryPushMoneyCount(pushMoneyCustomize);
        if (count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            pushMoneyCustomize.setLimitStart(paginator.getOffset());
            pushMoneyCustomize.setLimitEnd(paginator.getLimit());

            List<PushMoneyCustomize> pushMoneyCustomizes = this.pushMoneyService.queryPushMoneyList(pushMoneyCustomize);
            form.setPaginator(paginator);
            form.setRecordList(pushMoneyCustomizes);
            modeAndView.addObject(PushMoneyManageDefine.PUSHMONEY_FORM, form);
        } else {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            pushMoneyCustomize.setLimitStart(paginator.getOffset());
            pushMoneyCustomize.setLimitEnd(paginator.getLimit());
            form.setPaginator(paginator);
            modeAndView.addObject(PushMoneyManageDefine.PUSHMONEY_FORM, form);
        }

    }

    /**
     * 提成管理 列表
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(PushMoneyManageDefine.PUSHMONEY_LIST)
    @RequiresPermissions(PushMoneyManageDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, PushMoneyManageBean form) {
        LogUtil.startLog(PushMoneyManageController.class.toString(), PushMoneyManageDefine.PUSHMONEY_LIST);
        ModelAndView modeAndView = new ModelAndView(PushMoneyManageDefine.LIST_PATH);

        // 创建分页
        this.createPage(request, modeAndView, form);
        LogUtil.endLog(PushMoneyManageController.class.toString(), PushMoneyManageDefine.PUSHMONEY_LIST);
        return modeAndView;
    }

    /**
     * 提成管理 查询条件
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(PushMoneyManageDefine.PUSHMONEY_LIST_WITHQ)
    @RequiresPermissions(PushMoneyManageDefine.PERMISSIONS_VIEW)
    public ModelAndView initWithQ(HttpServletRequest request, PushMoneyManageBean form) {
        LogUtil.startLog(PushMoneyManageController.class.toString(), PushMoneyManageDefine.PUSHMONEY_LIST);
        ModelAndView modeAndView = new ModelAndView(PushMoneyManageDefine.LIST_PATH);

        // 创建分页
        this.createPage(request, modeAndView, form);
        LogUtil.endLog(PushMoneyManageController.class.toString(), PushMoneyManageDefine.PUSHMONEY_LIST);
        return modeAndView;
    }

    /**
     * 计算提成
     *
     * @param request
     * @param form
     * @return
     */
    @ResponseBody
    @RequestMapping(PushMoneyManageDefine.CALCULATE_PUSHMONEY)
    @RequiresPermissions(PushMoneyManageDefine.PERMISSIONS_PUSHMONEY_CALCULATE)
    public String calculatePushMoney(HttpServletRequest request, @RequestBody PushMoneyManageBean form) {
        LogUtil.startLog(this.getClass().getName(), PushMoneyManageDefine.CALCULATE_PUSHMONEY);
        JSONObject ret = new JSONObject();

        // 提成ID
        String borrowNid = form.getBorrowNid();

        // 取得借款API表
        BorrowApicron apicron = this.pushMoneyService.getBorrowApicronBorrowNid(borrowNid);
        if (apicron == null) {
            ret.put(PushMoneyManageDefine.JSON_STATUS_KEY, PushMoneyManageDefine.JSON_STATUS_NG);
            ret.put(PushMoneyManageDefine.JSON_RESULT_KEY, "该项目不存在!");
            return ret.toString();
        }
        if (GetterUtil.getInteger(apicron.getWebStatus()) == 1) {
            ret.put(PushMoneyManageDefine.JSON_STATUS_KEY, PushMoneyManageDefine.JSON_STATUS_OK);
            ret.put(PushMoneyManageDefine.JSON_RESULT_KEY, "该标的提成已经计算完成!");
            return ret.toString();
        }

        int cnt = -1;
        try {
            // 发提成处理
            cnt = this.pushMoneyService.insertTenderCommissionRecord(apicron.getId(), form);
        } catch (Exception e) {
            LogUtil.errorLog(this.getClass().getName(), PushMoneyManageDefine.CALCULATE_PUSHMONEY, e);
        }

        if (cnt >= 0) {
            ret.put("status", "success");
            ret.put("result", "提成计算成功！");
        } else {
            ret.put("status", "error");
            ret.put("result", "提成计算失败,请重新操作!");
        }
        LogUtil.endLog(this.getClass().getName(), PushMoneyManageDefine.CALCULATE_PUSHMONEY);
        return ret.toString();
    }

    /**
     * 根据业务需求导出相应的表格 此处暂时为可用情况 缺陷： 1.无法指定相应的列的顺序， 2.无法配置，excel文件名，excel sheet名称
     * 3.目前只能导出一个sheet 4.列的宽度的自适应，中文存在一定问题
     * 5.根据导出的业务需求最好可以在导出的时候输入起止页码，因为在大数据量的情况下容易造成卡顿
     *
     * 导出推广提成列表
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(PushMoneyManageDefine.EXPORT_PUSHMONEY_ACTION)
    @RequiresPermissions(PushMoneyManageDefine.PERMISSIONS_PUSHMONEY_EXPORT)
    public void exportPushMoneyExcel(HttpServletRequest request, HttpServletResponse response, PushMoneyManageBean form)
        throws Exception {

        // 表格sheet名称
        String sheetName = "推广提成列表";

        // 取得数据
        form.setLimitStart(-1);
        form.setLimitEnd(-1);
        
		//设置默认查询时间
		if(StringUtils.isEmpty(form.getRecoverDateStart())){
			form.setRecoverDateStart(GetDate.getDate("yyyy-MM-dd"));
		}
		if(StringUtils.isEmpty(form.getRecoverDateEnd())){
			form.setRecoverDateEnd(GetDate.getDate("yyyy-MM-dd"));
		}

        PushMoneyCustomize pushMoneyCustomize = new PushMoneyCustomize();
        BeanUtils.copyProperties(form, pushMoneyCustomize);
        List<PushMoneyCustomize> recordList = this.pushMoneyService.queryPushMoneyList(pushMoneyCustomize);

        String fileName =
                sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

        String[] titles = new String[] { "序号", "项目编号", "项目名称", "借款期限", "融资金额", "提成总额", "放款时间" };
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
                    PushMoneyCustomize bean = recordList.get(i);

                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);

                    // 序号
                    if (celLength == 0) {
                        cell.setCellValue(i + 1);
                    }
                    // 项目编号
                    else if (celLength == 1) {
                        cell.setCellValue(bean.getBorrowNid());
                    }
                    // 项目名称
                    else if (celLength == 2) {
                        cell.setCellValue(bean.getBorrowName());
                    }
                    // 借款期限
                    else if (celLength == 3) {
                        cell.setCellValue(bean.getRzqx());
                    }
                    // 融资金额
                    else if (celLength == 4) {
                        cell.setCellValue(bean.getAccount().toString());
                    }
                    // 提成总额
                    else if (celLength == 5) {
                        cell.setCellValue(bean.getCommission().toString());
                    }
                    // 放款时间
                    else if (celLength == 6) {
                        cell.setCellValue(bean.getRecoverLastTime());
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
    }

    // ***********************提现明细*****************************
    /**
     * 提成明细分页
     *
     * @param request
     * @param modeAndView
     * @param form
     */
    private void createPageDetail(HttpServletRequest request, ModelAndView modeAndView, PushMoneyManageBean form) {
    	PushMoneyCustomize pushMoneyCustomize = new PushMoneyCustomize();
        BeanUtils.copyProperties(form, pushMoneyCustomize);
        pushMoneyCustomize.setTenderType(1);
        
        Integer count = this.pushMoneyService.queryPushMoneyDetailCount(pushMoneyCustomize);
        if (count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            pushMoneyCustomize.setLimitStart(paginator.getOffset());
            pushMoneyCustomize.setLimitEnd(paginator.getLimit());

            List<PushMoneyCustomize> pushMoneyCustomizes =
                    this.pushMoneyService.queryPushMoneyDetail(pushMoneyCustomize);
            Map<String , Object> pushMoneyTotle = this.pushMoneyService.queryPushMoneyTotle(pushMoneyCustomize);
            form.setPaginator(paginator);
            form.setRecordList(pushMoneyCustomizes);
            modeAndView.addObject(PushMoneyManageDefine.PUSHMONEY_FORM, form);
            modeAndView.addObject("pushMoneyTotle", pushMoneyTotle);
        } else {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            pushMoneyCustomize.setLimitStart(paginator.getOffset());
            pushMoneyCustomize.setLimitEnd(paginator.getLimit());
            form.setPaginator(paginator);
            modeAndView.addObject(PushMoneyManageDefine.PUSHMONEY_FORM, form);
        }

    }


    /**
     * 资金管理-提成列表
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(PushMoneyManageDefine.PUSHMONEYLIST)
    @RequiresPermissions(PushMoneyManageDefine.PERMISSIONS_PUSHMONEYLIST_VIEW)
    public ModelAndView queryPushMoneyList(HttpServletRequest request, PushMoneyManageBean form) {
        LogUtil.startLog(PushMoneyManageController.class.toString(), PushMoneyManageDefine.PUSHMONEYLIST);
        ModelAndView modeAndView = new ModelAndView(PushMoneyManageDefine.PUSHMONEYLIST_PATH);

        // 创建分页
        this.createPageDetail(request, modeAndView, form);
        LogUtil.endLog(PushMoneyManageController.class.toString(), PushMoneyManageDefine.PUSHMONEYLIST);
        return modeAndView;
    }

    /**
     * 发提成
     *
     * @param request
     * @param form
     * @return
     */
    @ResponseBody
    @RequestMapping(PushMoneyManageDefine.CONFIRM_PUSHMONEY)
    @RequiresPermissions(PushMoneyManageDefine.PERMISSIONS_DETAIL_PUSHMONEY_CONFIRM)
    public String confirmPushMoneyAction(HttpServletRequest request, @RequestBody PushMoneyManageBean form) {
        LogUtil.startLog(this.getClass().getName(), PushMoneyManageDefine.CONFIRM_PUSHMONEY);
        JSONObject ret = new JSONObject();
        
        // 提成ID
        Integer id = form.getIds();

        TenderCommission tenderCommission = this.pushMoneyService.queryTenderCommissionByPrimaryKey(id);
        // 如果 未发放 //且 提成>0
        if (tenderCommission != null && tenderCommission.getStatus() == 0
                && tenderCommission.getAccountTender().compareTo(BigDecimal.ZERO) > 0) {
            Integer userId = tenderCommission.getUserId();

            /** 验证员工在平台的身份属性是否和crm的一致 如果不一致则不发提成 begin */

			/** redis 锁 */
			boolean reslut = RedisUtils.tranactionSet("PUSH_MONEY:" + id, 60);
			// 如果没有设置成功，说明有请求来设置过
			if(!reslut){
				ret.put(PushMoneyManageDefine.JSON_STATUS_KEY, PushMoneyManageDefine.JSON_STATUS_NG);
				ret.put(PushMoneyManageDefine.JSON_RESULT_KEY, "数据已发生变化,请一分钟后再操作!");
				return ret.toString();
			}
			
            UsersInfo usersInfo = this.pushMoneyService.getUsersInfoByUserId(userId);
            // cuttype 提成发放方式（3线上2线下）
            Integer cuttype = this.pushMoneyService.queryCrmCuttype(userId);

            if (usersInfo.getAttribute() != null && usersInfo.getAttribute() > 1) {
                if (usersInfo.getAttribute() != cuttype) {
                    ret.put(PushMoneyManageDefine.JSON_STATUS_KEY, PushMoneyManageDefine.JSON_STATUS_NG);
                    ret.put(PushMoneyManageDefine.JSON_RESULT_KEY, "该用户属性异常！");
                    LogUtil.errorLog(THIS_CLASS, PushMoneyManageDefine.CONFIRM_PUSHMONEY, new Exception(
                            "该用户平台属性与CRM 不符！[userId=" + userId + "]"));
                    return ret.toString();
                }
            }
            /** 验证员工在平台的身份属性是否和crm的一致 如果不一致则不发提成 end */

            BankOpenAccount bankOpenAccountInfo = returncashService.getBankOpenAccount(userId);

            if (bankOpenAccountInfo != null && !Validator.isNull(bankOpenAccountInfo.getAccount())) {
                // 查询商户子账户余额
                String merrpAccount = PropUtils.getSystem(BankCallConstant.BANK_MERRP_ACCOUNT);
                BigDecimal bankBalance = pushMoneyService.getBankBalance(Integer.parseInt(ShiroUtil.getLoginUserId()), merrpAccount);
                
                // 如果接口调用成功
                if (bankBalance != null) {
                    // 检查余额是否充足
                    if (bankBalance.compareTo(tenderCommission.getCommission()) < 0) {
                        LogUtil.errorLog(THIS_CLASS, PushMoneyManageDefine.CONFIRM_PUSHMONEY, new Exception("推广提成子账户余额不足,请先充值或向该子账户转账"));
                        ret.put(PushMoneyManageDefine.JSON_STATUS_KEY, PushMoneyManageDefine.JSON_STATUS_NG);
                        ret.put(PushMoneyManageDefine.JSON_RESULT_KEY, "推广提成子账户余额不足,请先充值或向该子账户转账");
                        return ret.toString();
                    }
                } else {
                    System.out.println("没有查询到商户可用余额");
                    LogUtil.errorLog(THIS_CLASS, PushMoneyManageDefine.CONFIRM_PUSHMONEY, new Exception("调用银行接口发生错误"));
                    ret.put(PushMoneyManageDefine.JSON_STATUS_KEY, PushMoneyManageDefine.JSON_STATUS_NG);
                    ret.put(PushMoneyManageDefine.JSON_RESULT_KEY, "没有查询到商户可用余额");
                    return ret.toString();
                }

                
                // IP地址
                String ip = CustomUtil.getIpAddr(request);
                String orderId = GetOrderIdUtils.getOrderId2(Integer.valueOf(userId));
                
                BankCallBean bean = new BankCallBean();
                bean.setVersion(BankCallConstant.VERSION_10);// 版本号
                bean.setTxCode(BankCallMethodConstant.TXCODE_VOUCHER_PAY);// 交易代码
                bean.setTxDate(GetOrderIdUtils.getTxDate()); // 交易日期
                bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
                bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
                bean.setChannel(BankCallConstant.CHANNEL_PC); // 交易渠道
                bean.setAccountId(merrpAccount);// 电子账号
                bean.setTxAmount(tenderCommission.getCommission().toString());
                bean.setForAccountId(bankOpenAccountInfo.getAccount());
                bean.setDesLineFlag("1");
                bean.setDesLine(tenderCommission.getOrdid());
                bean.setLogOrderId(orderId);// 订单号
                bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
                bean.setLogUserId(String.valueOf(userId));
                bean.setLogClient(0);// 平台
                bean.setLogIp(ip);

                BankCallBean resultBean;
                try {
                    resultBean = BankCallUtils.callApiBg(bean);
                } catch (Exception e) {
                    e.printStackTrace();
                    ret.put(PushMoneyManageDefine.JSON_STATUS_KEY, PushMoneyManageDefine.JSON_STATUS_NG);
                    ret.put(PushMoneyManageDefine.JSON_RESULT_KEY, "请求红包接口失败");
                    return ret.toString();
                }
                
                if (resultBean == null || !BankCallConstant.RESPCODE_SUCCESS.equals(resultBean.getRetCode())) {
                    LogUtil.errorLog(THIS_CLASS, PushMoneyManageDefine.CONFIRM_PUSHMONEY, new Exception("调用红包接口发生错误"));
                    ret.put(PushMoneyManageDefine.JSON_STATUS_KEY, PushMoneyManageDefine.JSON_STATUS_NG);
                    ret.put(PushMoneyManageDefine.JSON_RESULT_KEY, "调用红包接口发生错误");
                    return ret.toString();
                }

                int cnt = 0;
                // 接口返回正常时,执行更新操作
                try {
                    // 发提成处理
                    cnt = this.pushMoneyService.updateTenderCommissionRecord(tenderCommission, resultBean);
                } catch (Exception e) {
                    LogUtil.errorLog(THIS_CLASS, PushMoneyManageDefine.CONFIRM_PUSHMONEY, e);
                }

                // 返现成功
                if (cnt > 0) {
                    ret.put(PushMoneyManageDefine.JSON_STATUS_KEY, PushMoneyManageDefine.JSON_STATUS_OK);
                    ret.put(PushMoneyManageDefine.JSON_RESULT_KEY, "发提成操作成功!");
                    System.out.println("提成发放成功，用户id：" + userId + " 金额:"  + tenderCommission.getCommission().toString());
                } else {
                    ret.put(PushMoneyManageDefine.JSON_STATUS_KEY, PushMoneyManageDefine.JSON_STATUS_NG);
                    ret.put(PushMoneyManageDefine.JSON_RESULT_KEY, "发提成时发生错误,请重新操作!");
                    LogUtil.errorLog(THIS_CLASS, PushMoneyManageDefine.CONFIRM_PUSHMONEY, new Exception("发提成时发生错误,请重新操作!"));
                }
                
                LogUtil.endLog(this.getClass().getName(), PushMoneyManageDefine.CONFIRM_PUSHMONEY);
                return ret.toString();
                
            }else {
                ret.put(PushMoneyManageDefine.JSON_STATUS_KEY, PushMoneyManageDefine.JSON_STATUS_NG);
                ret.put(PushMoneyManageDefine.JSON_RESULT_KEY, "该用户未开户");
                LogUtil.errorLog(THIS_CLASS, PushMoneyManageDefine.CONFIRM_PUSHMONEY, new Exception("参数不正确[userId="
                        + userId + "]"));
                return ret.toString();
            }

        }else{
            ret.put(PushMoneyManageDefine.JSON_STATUS_KEY, PushMoneyManageDefine.JSON_STATUS_NG);
            ret.put(PushMoneyManageDefine.JSON_RESULT_KEY, "提成已经发过啦");
        }
        return ret.toString();
       
    }

    /**
     * 根据业务需求导出相应的表格 此处暂时为可用情况 缺陷： 1.无法指定相应的列的顺序， 2.无法配置，excel文件名，excel sheet名称
     * 3.目前只能导出一个sheet 4.列的宽度的自适应，中文存在一定问题
     * 5.根据导出的业务需求最好可以在导出的时候输入起止页码，因为在大数据量的情况下容易造成卡顿
     *
     * 导出推广提成列表
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(PushMoneyManageDefine.ENHANCE_EXPORT_ACTION)
    @RequiresPermissions(value = {PushMoneyManageDefine.PERMISSIONS_PUSHMONEYLIST_PUSHMONEY_EXPORT, PushMoneyManageDefine.ENHANCE_PERMISSIONS_EXPORT}, logical = Logical.AND)
    public void enhanceExportPushMoneyDetailExcel(HttpServletRequest request, HttpServletResponse response,
        PushMoneyManageBean form) throws Exception {

        // 表格sheet名称
        String sheetName = "推广提成发放列表";

        // 取得数据
        form.setLimitStart(-1);
        form.setLimitEnd(-1);
        
        PushMoneyCustomize pushMoneyCustomize = new PushMoneyCustomize();
        BeanUtils.copyProperties(form, pushMoneyCustomize);
        pushMoneyCustomize.setTenderType(1);
        List<PushMoneyCustomize> recordList = this.pushMoneyService.queryPushMoneyDetail(pushMoneyCustomize);

        String fileName =
                sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

        String[] titles =
                new String[] { "序号", "项目编号", "借款期限", "分公司", "分部", "团队", "提成人", "电子账号", "用户属性", "51老用户", "出借人", "授权服务金额", "提成金额",
                        "状态", "出借时间", "发放时间" };
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
                    PushMoneyCustomize bean = recordList.get(i);

                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);

                    // 序号
                    if (celLength == 0) {
                        cell.setCellValue(i + 1);
                    }
                    // 项目编号
                    else if (celLength == 1) {
                        cell.setCellValue(bean.getBorrowNid());
                    }
                    // 借款期限
                    else if (celLength == 2) {
                        cell.setCellValue(bean.getRzqx());
                    }
                    // 分公司
                    else if (celLength == 3) {
                        cell.setCellValue(bean.getRegionName());
                    }
                    // 分部
                    else if (celLength == 4) {
                        cell.setCellValue(bean.getBranchName());
                    }
                    // 团队
                    else if (celLength == 5) {
                        cell.setCellValue(HtmlUtil.unescape(bean.getDepartmentName()));
                    }
                    // 提成人
                    else if (celLength == 6) {
                        cell.setCellValue(bean.getUsername());
                    }
                    else if (celLength == 7) {
                        cell.setCellValue(bean.getAccountId());
                    }
                    // 提成人用户属性
                    else if (celLength == 8) {
                        // cell.setCellValue(bean.getAttributeName());
                        String attribute = "";
                        if ("0".equals(bean.getAttribute())) {
                            attribute = "无主单";
                        } else if ("1".equals(bean.getAttribute())) {
                            attribute = "有主单";
                        } else if ("2".equals(bean.getAttribute())) {
                            attribute = "线下员工";
                        } else if ("3".equals(bean.getAttribute())) {
                            attribute = "线上员工";
                        }
                        cell.setCellValue(attribute);
                    }
                    // 51老用户
                    else if (celLength == 9) {
                        cell.setCellValue(bean.getIs51Name());
                    }
                    // 出借人
                    else if (celLength == 10) {
                        cell.setCellValue(bean.getUsernameTender());
                    }
                    // 出借金额
                    else if (celLength == 11) {
                        cell.setCellValue(bean.getAccountTender().toString());
                    }
                    // 提成金额
                    else if (celLength == 12) {
                        cell.setCellValue(bean.getCommission().toString());
                    }
                    // 状态
                    else if (celLength == 13) {
                        cell.setCellValue(bean.getStatusName());
                    }
                    // 出借时间
                    else if (celLength == 14) {
                        cell.setCellValue(bean.getTenderTimeView());
                    }
                    // 发放时间
                    else if (celLength == 15) {
                        cell.setCellValue(bean.getSendTimeView());
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
    }


    @RequestMapping(PushMoneyManageDefine.EXPORT_PUSHMONEY_DETAIL_ACTION)
    @RequiresPermissions(PushMoneyManageDefine.PERMISSIONS_PUSHMONEYLIST_PUSHMONEY_EXPORT)
    public void exportPushMoneyDetailExcel(HttpServletRequest request, HttpServletResponse response,
                                           PushMoneyManageBean form) throws Exception {

        // 表格sheet名称
        String sheetName = "推广提成发放列表";

        // 取得数据
        form.setLimitStart(-1);
        form.setLimitEnd(-1);

        PushMoneyCustomize pushMoneyCustomize = new PushMoneyCustomize();
        BeanUtils.copyProperties(form, pushMoneyCustomize);
        pushMoneyCustomize.setTenderType(1);
        List<PushMoneyCustomize> recordList = this.pushMoneyService.queryPushMoneyDetail(pushMoneyCustomize);

        String fileName =
                sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

        String[] titles =
                new String[] { "序号", "项目编号", "借款期限", "提成人", "电子账号", "用户属性", "51老用户", "出借人", "授权服务金额", "提成金额",
                        "状态", "出借时间", "发放时间" };
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
                    PushMoneyCustomize bean = recordList.get(i);

                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);

                    // 序号
                    if (celLength == 0) {
                        cell.setCellValue(i + 1);
                    }
                    // 项目编号
                    else if (celLength == 1) {
                        cell.setCellValue(bean.getBorrowNid());
                    }
                    // 借款期限
                    else if (celLength == 2) {
                        cell.setCellValue(bean.getRzqx());
                    }
                    // 提成人
                    else if (celLength == 3) {
                        cell.setCellValue(bean.getUsername());
                    }
                    else if (celLength == 4) {
                        cell.setCellValue(bean.getAccountId());
                    }
                    // 提成人用户属性
                    else if (celLength == 5) {
                        // cell.setCellValue(bean.getAttributeName());
                        String attribute = "";
                        if ("0".equals(bean.getAttribute())) {
                            attribute = "无主单";
                        } else if ("1".equals(bean.getAttribute())) {
                            attribute = "有主单";
                        } else if ("2".equals(bean.getAttribute())) {
                            attribute = "线下员工";
                        } else if ("3".equals(bean.getAttribute())) {
                            attribute = "线上员工";
                        }
                        cell.setCellValue(attribute);
                    }
                    // 51老用户
                    else if (celLength == 6) {
                        cell.setCellValue(bean.getIs51Name());
                    }
                    // 出借人
                    else if (celLength == 7) {
                        cell.setCellValue(bean.getUsernameTender());
                    }
                    // 授权服务金额
                    else if (celLength == 8) {
                        cell.setCellValue(bean.getAccountTender().toString());
                    }
                    // 提成金额
                    else if (celLength == 9) {
                        cell.setCellValue(bean.getCommission().toString());
                    }
                    // 状态
                    else if (celLength == 10) {
                        cell.setCellValue(bean.getStatusName());
                    }
                    // 出借时间
                    else if (celLength == 11) {
                        cell.setCellValue(bean.getTenderTimeView());
                    }
                    // 发放时间
                    else if (celLength == 12) {
                        cell.setCellValue(bean.getSendTimeView());
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
    }

    /**
     * 取得部门信息
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping("getCrmDepartmentList")
    @ResponseBody
    public String getCrmDepartmentListAction(@RequestBody PushMoneyManageBean form) {
        // 部门
        String[] list = new String[] {};
        if (Validator.isNotNull(form.getDepIds())) {
            if (form.getDepIds().contains(StringPool.COMMA)) {
                list = form.getDepIds().split(StringPool.COMMA);
            } else {
                list = new String[] { form.getDepIds() };
            }
        }

        JSONArray ja = this.pushMoneyService.getCrmDepartmentList(list);
        if (ja != null) {
            return ja.toString();
        }

        return StringUtils.EMPTY;
    }
}
