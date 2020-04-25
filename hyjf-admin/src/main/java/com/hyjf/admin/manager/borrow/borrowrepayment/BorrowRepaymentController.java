package com.hyjf.admin.manager.borrow.borrowrepayment;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonService;
import com.hyjf.admin.manager.borrow.borrowfirst.BorrowFirstController;
import com.hyjf.admin.manager.borrow.borrowfull.BorrowFullController;
import com.hyjf.admin.manager.borrow.borrowrepayment.plan.BorrowRepaymentPlanBean;
import com.hyjf.admin.manager.borrow.borrowrepayment.plan.BorrowRepaymentPlanController;
import com.hyjf.admin.manager.borrow.borrowrepayment.plan.BorrowRepaymentPlanDefine;
import com.hyjf.admin.manager.borrow.borrowrepayment.plan.BorrowRepaymentPlanService;
import com.hyjf.admin.manager.borrow.borrowrepaymentinfo.BorrowRepaymentInfoDefine;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BorrowRepay;
import com.hyjf.mybatis.model.auto.BorrowRepayPlan;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.HjhInstConfig;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.BorrowRepaymentCustomize;
import com.hyjf.mybatis.model.customize.BorrowRepaymentPlanCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminRepayDelayCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = BorrowRepaymentDefine.REQUEST_MAPPING)
public class BorrowRepaymentController extends BaseController {

	@Autowired
	private BorrowRepaymentService borrowRepaymentService;

	@Autowired
	private BorrowCommonService borrowCommonService;
	
	@Autowired
    private BorrowRepaymentPlanService borrowRepaymentPlanService;
	

	/**
	 * 画面初始化
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowRepaymentDefine.INIT)
	@RequiresPermissions(BorrowRepaymentDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute(BorrowRepaymentDefine.REPAYMENT_FORM) BorrowRepaymentBean form) {
		LogUtil.startLog(BorrowRepaymentController.class.toString(), BorrowRepaymentDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(BorrowRepaymentDefine.LIST_PATH);
		// 创建分页,时间默认当天
		Date endDate = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		form.setActulRepayTimeStartSrch(simpleDateFormat.format(DateUtils.addDays(endDate, 0)));
		form.setActulRepayTimeEndSrch(simpleDateFormat.format(DateUtils.addDays(endDate, 0)));
		
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BorrowRepaymentController.class.toString(), BorrowRepaymentDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面初始化
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowRepaymentDefine.SEARCH_ACTION)
	@RequiresPermissions(BorrowRepaymentDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, BorrowRepaymentBean form) {
		LogUtil.startLog(BorrowRepaymentController.class.toString(), BorrowRepaymentDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(BorrowRepaymentDefine.LIST_PATH);
		// 创建分页
		
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BorrowRepaymentController.class.toString(), BorrowRepaymentDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 创建分页
	 *
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, BorrowRepaymentBean form) {
		BorrowRepaymentCustomize borrowRepaymentCustomize = new BorrowRepaymentCustomize();
		// 还款方式
		List<BorrowStyle> repayTypeList = this.borrowCommonService.borrowStyleList("");
		modelAndView.addObject("repayTypeList", repayTypeList);
		BeanUtils.copyProperties(form, borrowRepaymentCustomize);
		/*--------------add by LSY START-------------------*/
		// 资金来源
		List<HjhInstConfig> hjhInstConfigList = this.borrowCommonService.hjhInstConfigList("");
		modelAndView.addObject("hjhInstConfigList", hjhInstConfigList);
		/*--------------add by LSY END-------------------*/

		Long count = this.borrowRepaymentService.countBorrowRepayment(borrowRepaymentCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			borrowRepaymentCustomize.setLimitStart(paginator.getOffset());
			borrowRepaymentCustomize.setLimitEnd(paginator.getLimit());
			List<BorrowRepaymentCustomize> recordList = this.borrowRepaymentService.selectBorrowRepaymentList(borrowRepaymentCustomize);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
			BorrowRepaymentCustomize sumObject = this.borrowRepaymentService.sumBorrowRepaymentInfo(borrowRepaymentCustomize);
			modelAndView.addObject("sumObject", sumObject);
		}
		modelAndView.addObject(BorrowRepaymentDefine.REPAYMENT_FORM, form);
	}

	/**
     * 数据导出--还款计划
     * 
     * @param request
     * @param form
     * @return
     */
	@RequestMapping(BorrowRepaymentDefine.EXPORT_REPAY_ACTION)
    @RequiresPermissions(BorrowRepaymentDefine.PERMISSIONS_EXPORT)
    public void exportRepayClkAct(HttpServletRequest request, HttpServletResponse response, BorrowRepaymentPlanBean form) {
        LogUtil.startLog(BorrowRepaymentPlanController.class.toString(), BorrowRepaymentDefine.EXPORT_REPAY_ACTION);
        // 表格sheet名称
        String sheetName = "还款计划导出数据";

        BorrowRepaymentPlanCustomize borrowRepaymentPlanCustomize = new BorrowRepaymentPlanCustomize();

        // 项目编号
        if (StringUtils.isNotBlank(form.getBorrowNid())){
            borrowRepaymentPlanCustomize.setBorrowNid(form.getBorrowNid());
        }
        // 标的发布时间
        if (StringUtils.isNotBlank(form.getVerifyTimeStartSrch())){
            String date;
            date = form.getVerifyTimeStartSrch();              
            borrowRepaymentPlanCustomize.setVerifyTimeStartSrch(date);
        }
        if (StringUtils.isNotBlank(form.getVerifyTimeEndSrch())){
            String date;
            date = form.getVerifyTimeEndSrch();                
            borrowRepaymentPlanCustomize.setVerifyTimeEndSrch(date);
        }
        // 实际还款时间
        if (StringUtils.isNotBlank(form.getActulRepayTimeStartSrch())){
            String actulDate;
            actulDate = form.getActulRepayTimeStartSrch();
            borrowRepaymentPlanCustomize.setActulRepayTimeStartSrch(actulDate);
        }
        if (StringUtils.isNotBlank(form.getActulRepayTimeEndSrch())){
            String actulDateEnd;
            actulDateEnd = form.getActulRepayTimeEndSrch();
            borrowRepaymentPlanCustomize.setActulRepayTimeEndSrch(actulDateEnd);
        }
        // 下期还款日
        if (StringUtils.isNotBlank(form.getRepayLastTimeStartSrch())){
            String repayLastDate;
            repayLastDate = form.getRepayLastTimeStartSrch();
            borrowRepaymentPlanCustomize.setRepayLastTimeStartSrch(repayLastDate);
        }
        if (StringUtils.isNotBlank(form.getRepayLastTimeEndSrch())){
            String repayLastDate;
            repayLastDate = form.getRepayLastTimeEndSrch();
            borrowRepaymentPlanCustomize.setRepayLastTimeEndSrch(repayLastDate);
        }
        if (StringUtils.isNotBlank(form.getInstCodeSrch())){
            borrowRepaymentPlanCustomize.setInstCodeSrch(form.getInstCodeSrch());
        }
        if (StringUtils.isNotBlank(form.getPlanNidSrch())){
            borrowRepaymentPlanCustomize.setPlanNidSrch(form.getPlanNidSrch());
        }
        if (StringUtils.isNotBlank(form.getBorrowPeriod())){
            borrowRepaymentPlanCustomize.setBorrowPeriod(form.getBorrowPeriod());
        }

        if (StringUtils.isNotBlank(form.getBorrowUserName())){
            borrowRepaymentPlanCustomize.setBorrowUserName(form.getBorrowUserName());
        }
        if (StringUtils.isNotBlank(form.getRepayStyleType())){
            borrowRepaymentPlanCustomize.setRepayStyleType(form.getRepayStyleType());
        }

        List<BorrowRepaymentPlanCustomize> recordList = this.borrowRepaymentPlanService.exportRepayClkActBorrowRepaymentInfoList(borrowRepaymentPlanCustomize);
        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

        /*---------------upd by LSY START-------------*/
        //String[] titles = new String[] { "项目编号", "借款人ID", "借款人用户名", "项目名称", "项目类型", "借款期限", "出借利率", "借款金额", "借到金额", "还款方式", "还款期数", "应还本金", "应还利息", "应还本息", "应收管理费", "提前天数", "少还利息", "延期天数", "延期利息", "逾期天数", "逾期利息", "应还总额", "实还总额", "还款状态", "实际还款日期", "应还日期","还款来源" };
        String[] titles = new String[] { "项目编号","资产来源" , "借款人ID", "借款人用户名", "项目类型", "借款期限", "出借利率", "借款金额", "借到金额", "还款方式", "还款期数", "应还本金", "应还利息", "应还本息", "还款服务费", "提前天数", "少还利息", "延期天数", "延期利息", "逾期天数", "逾期利息", "应还总额", "实还总额", "还款状态", "实际还款日期", "应还日期","剩余待还本金","剩余待还利息","还款来源","初审时间"};
        /*---------------upd by LSY END-------------*/
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
                    BorrowRepaymentPlanCustomize record = recordList.get(i);

                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);

                    // 项目编号
                    if (celLength == 0) {
                        cell.setCellValue(record.getBorrowNid());
                    }
                    // 资产来源
                    else if(celLength == 1) {
                        cell.setCellValue(record.getInstName());
                    }
                    // 借款人ID
                    else if (celLength == 2) {
                        cell.setCellValue(record.getUserId());
                    }
                    // 借款人用户名
                    else if (celLength == 3) {
                        cell.setCellValue(record.getBorrowUserName());
                    }
                    // 项目名称
//                    else if (celLength == 4) {
//                        cell.setCellValue(record.getBorrowName());
//                    }
                    // 项目类型
                    else if (celLength == 4) {
                        cell.setCellValue(record.getProjectTypeName());
                    }
                    // 借款期限
                    else if (celLength == 5) {
                        cell.setCellValue(record.getBorrowPeriod());
                    }
                    // 出借利率
                    else if (celLength == 6) {
                        cell.setCellValue(record.getBorrowApr() + "%");
                    }
                    // 借款金额
                    else if (celLength == 7) {
                        cell.setCellValue(record.getBorrowAccount().equals("") ? 0 : Double.valueOf(record.getBorrowAccount()));
                    }
                    // 借到金额
                    else if (celLength == 8) {
                        cell.setCellValue(record.getBorrowAccountYes().equals("") ? 0 : Double.valueOf(record.getBorrowAccountYes()));
                    }
                    // 还款方式
                    else if (celLength == 9) {
                        cell.setCellValue(record.getRepayType());
                    }
                    // 还款期数
                    else if (celLength == 10) {
                        cell.setCellValue(StringUtils.isBlank(record.getRepayPeriod()) ? " " : "第" + record.getRepayPeriod() + "期");
                    }
                    // 应还本金
                    else if (celLength == 11) {
                        Double repayCapital;
                        if (StringUtils.isNotBlank(record.getRepayCapital())){
                            repayCapital = Double.valueOf(record.getRepayCapital());
                        }else {
                            repayCapital = 0.0;
                        }
                        cell.setCellValue(repayCapital);
                    }
                    // 应还利息
                    else if (celLength == 12) {
                        cell.setCellValue(StringUtils.isNotBlank(record.getRepayInterest()) ? Double.valueOf(record.getRepayInterest()) : 0);
                    }
                    // 应还本息
                    else if (celLength == 13) {
                        cell.setCellValue(StringUtils.isBlank(record.getRepayAccount()) ? 0 : Double.valueOf(record.getRepayAccount()));
                    }
                    // 应收管理费
                    else if (celLength == 14) {
                        cell.setCellValue(StringUtils.isBlank(record.getRepayFee()) ? 0 : Double.valueOf(record.getRepayFee()));
                    }
                    // 提前天数
                    else if (celLength == 15) {
                        cell.setCellValue(record.getTiqiantianshu());
                    }
                    // 少还利息
                    else if (celLength == 16) {
                        cell.setCellValue(StringUtils.isBlank(record.getShaohuanlixi()) ? 0 : Double.valueOf(record.getShaohuanlixi()));
                    }
                    // 延期天数
                    else if (celLength == 17) {
                        cell.setCellValue(record.getYanqitianshu());
                    }
                    // 延期利息
                    else if (celLength == 18) {
                        cell.setCellValue(StringUtils.isBlank(record.getYanqilixi()) ? 0 : Double.valueOf(record.getYanqilixi()));
                    }
                    // 逾期天数
                    else if (celLength == 19) {
                        cell.setCellValue(record.getYuqitianshu());
                    }
                    // 逾期利息
                    else if (celLength == 20) {
                        cell.setCellValue(StringUtils.isBlank(record.getYuqilixi()) ? 0 : Double.valueOf(record.getYuqilixi()));
                    }
                    // 应还总额
                    else if (celLength == 21) {
                        cell.setCellValue(StringUtils.isBlank(record.getYinghuanzonge()) ? 0 : Double.valueOf(record.getYinghuanzonge()));
                    }
                    // 实还总额
                    else if (celLength == 22) {
                        cell.setCellValue(StringUtils.isBlank(record.getShihuanzonge()) ? 0 : Double.valueOf(record.getShihuanzonge()));
                    }
                    // 还款状态
                    else if (celLength == 23) {
                        if (StringUtils.isNotEmpty(record.getStatus())) {
                            cell.setCellValue(record.getStatus().equals("0") ? "还款中" : "已还款");
                        }
                    }
                    // 实际还款日
                    else if (celLength == 24) {
                        cell.setCellValue(record.getRepayActionTime());
                    }
                    // 应还日期
                    else if (celLength == 25) {
                        cell.setCellValue(record.getRepayLastTime());
                    }
                    // 剩余待还本金
                    else if (celLength == 26){
                        cell.setCellValue(record.getRepayAccountCapitalWait());
                    }
                    // 剩余待还利息
                    else if (celLength == 27){
                        cell.setCellValue(record.getRepayAccountInterestWait());
                    }
                    // 还款来源
                    else if(celLength == 28) {
                        cell.setCellValue(record.getRepayMoneySource());
                    }
                 // 应还日期
                    else if (celLength == 29) {
                        cell.setCellValue(record.getVerifyTime());
                    }
                    

                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);

        LogUtil.endLog(BorrowRepaymentPlanController.class.toString(),BorrowRepaymentDefine.EXPORT_REPAY_ACTION);
    }

	/**
     * 数据导出
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(BorrowRepaymentDefine.EXPORT_ACTION)
    @RequiresPermissions(BorrowRepaymentDefine.PERMISSIONS_EXPORT)
    public void exportAction(HttpServletRequest request, HttpServletResponse response, BorrowRepaymentBean form) {
        LogUtil.startLog(BorrowRepaymentController.class.toString(), BorrowRepaymentDefine.EXPORT_ACTION);
        // 表格sheet名称
        String sheetName = "还款信息导出数据";

        BorrowRepaymentCustomize borrowRepaymentCustomize = new BorrowRepaymentCustomize();
        BeanUtils.copyProperties(form, borrowRepaymentCustomize);

        List<BorrowRepaymentCustomize> recordList = this.borrowRepaymentService.selectBorrowRepaymentList(borrowRepaymentCustomize);
        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

        /*-----upd by LSY START-------------------------------------*/
        //String[] titles = new String[] { "项目编号", "计划编号", "借款人ID", "借款人用户名", "项目名称", "项目类型","合作机构", "借款期限", "出借利率", "借款金额", "借到金额", "还款方式", "应还本金", "应还利息", "应还本息", "管理费", "已还本金", "已还利息", "已还本息", "未还本金", "未还利息",
        String[] titles = new String[] { "项目编号", "资产来源", "智投编号", "借款人ID", "借款人用户名", "项目名称", "项目类型","合作机构", "借款期限", "出借利率", "借款金额", "借到金额", "还款方式", "应还本金", "应还利息", "应还本息", "还款服务费", "已还本金", "已还利息", "已还本息", "剩余待还本金", "剩余待还利息",
        /*-----upd by LSY END-------------------------------------*/
                "未还本息", "还款状态", "到期日", "下期还款日", "还款来源", "实际还款时间" ,"担保机构用户名","添加标的人员","备案人员","还款冻结订单号"};
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
                    BorrowRepaymentCustomize record = recordList.get(i);

                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);
                    // 项目编号
                    if (celLength == 0) {
                        cell.setCellValue(record.getBorrowNid());
                    }
                    /*-----add by LSY START-------------------------*/
                    // 资产来源
                    else if (celLength == 1) {
                        cell.setCellValue(record.getInstName());
                    }
                    /*-----add by LSY END-------------------------*/
                    // 计划编号
                    else if (celLength == 2) {
                        cell.setCellValue(record.getPlanNid());
                    }
                    // 借款人ID
                    else if (celLength == 3) {
                        cell.setCellValue(record.getUserId());
                    }
                    // 借款人用户名
                    else if (celLength == 4) {
                        cell.setCellValue(record.getBorrowUserName());
                    }
                    // 项目名称
                    else if (celLength == 5) {
                        cell.setCellValue(record.getBorrowName());
                    }
                    // 项目类型
                    else if (celLength == 6) {
                        cell.setCellValue(record.getProjectTypeName());
                    }
                    // 合作机构
                    else if(celLength == 7){
                        cell.setCellValue(record.getPartner());
                    }
                    // 借款期限
                    else if (celLength == 8) {
                        cell.setCellValue(record.getBorrowPeriod());
                    }
                    // 出借利率
                    else if (celLength == 9) {
                        cell.setCellValue(record.getBorrowApr() + "%");
                    }
                    // 借款金额
                    else if (celLength == 10) {
                        cell.setCellValue(
                                record.getBorrowAccount().equals("") ? 0 : Double.valueOf(record.getBorrowAccount()));
                    }
                    // 借到金额
                    else if (celLength == 11) {
                        cell.setCellValue(record.getBorrowAccountYes().equals("") ? 0
                                : Double.valueOf(record.getBorrowAccountYes()));
                    }
                    // 还款方式
                    else if (celLength == 12) {
                        cell.setCellValue(record.getRepayType());
                    }
                    // 应还本金
                    else if (celLength == 13) {
                        cell.setCellValue(record.getRepayAccountCapital().equals("") ? 0
                                : Double.valueOf(record.getRepayAccountCapital()));
                    }
                    // 应还利息
                    else if (celLength == 14) {
                        cell.setCellValue(record.getRepayAccountInterest().equals("") ? 0
                                : Double.valueOf(record.getRepayAccountInterest()));
                    }
                    // 应还本息
                    else if (celLength == 15) {
                        cell.setCellValue(record.getRepayAccountAll().equals("") ? 0
                                : Double.valueOf(record.getRepayAccountAll()));
                    }
                    // 管理费
                    else if (celLength == 16) {
                        cell.setCellValue(record.getRepayFee().equals("") ? 0 : Double.valueOf(record.getRepayFee()));
                    }
                    // 已还本金
                    else if (celLength == 17) {
                        cell.setCellValue(record.getRepayAccountCapitalYes().equals("") ? 0
                                : Double.valueOf(record.getRepayAccountCapitalYes()));
                    }
                    // 已还利息
                    else if (celLength == 18) {
                        cell.setCellValue(record.getRepayAccountInterestYes().equals("") ? 0
                                : Double.valueOf(record.getRepayAccountInterestYes()));
                    }
                    // 已还本息
                    else if (celLength == 19) {
                        cell.setCellValue(record.getRepayAccountYes().equals("") ? 0
                                : Double.valueOf(record.getRepayAccountYes()));
                    }
                    // 未还本金
                    else if (celLength == 20) {
                        cell.setCellValue(record.getRepayAccountCapitalWait().equals("") ? 0
                                : Double.valueOf(record.getRepayAccountCapitalWait()));
                    }
                    // 未还利息
                    else if (celLength == 21) {
                        cell.setCellValue(record.getRepayAccountInterestWait().equals("") ? 0
                                : Double.valueOf(record.getRepayAccountInterestWait()));
                    }
                    // 未还本息
                    else if (celLength == 22) {
                        cell.setCellValue(record.getRepayAccountWait().equals("") ? 0
                                : Double.valueOf(record.getRepayAccountWait()));
                    }
                    // 还款状态
                    else if (celLength == 23) {
                        if (StringUtils.isNotEmpty(record.getStatus())) {
                            cell.setCellValue(record.getStatus().equals("0") ? "还款中" : "已还款");
                        }
                    }
                    // 最后还款日
                    else if (celLength == 24) {
                        cell.setCellValue(record.getRepayLastTime());
                    }
                    // 下期还款日
                    else if (celLength == 25) {
                        cell.setCellValue(record.getRepayNextTime());
                    }
                    // 还款来源
                    else if (celLength == 26) {
                        cell.setCellValue(record.getRepayMoneySource());
                    }
                    // 实际还款时间
                    else if (celLength == 27) {
                        cell.setCellValue(record.getRepayActionTime());
                    }
                    // 担保机构用户名
                    else if (celLength == 28) {
                        cell.setCellValue(record.getRepayOrgUserName());
                    }
                    // 账户操作人
                    else if (celLength == 29) {
                        cell.setCellValue(record.getCreateUserName());
                    }
                    // 备案人员 
                    else if (celLength == 30) {
                        cell.setCellValue(record.getRegistUserName());
                    }
                    // 还款冻结订单号
                    else if (celLength == 31) {
                        cell.setCellValue(StringUtils.isBlank(record.getFreezeOrderId())?"":record.getFreezeOrderId());
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);

        LogUtil.endLog(BorrowRepaymentController.class.toString(), BorrowRepaymentDefine.EXPORT_ACTION);
    }
	/**
	 * 跳转到还款计划
	 *
	 * @param request
	 * @param form
	 */
	@RequestMapping(BorrowRepaymentDefine.REPAY_PLAN_ACTION)
	public ModelAndView replayPlanAction(HttpServletRequest request, BorrowRepaymentBean form, RedirectAttributes attr) {
		LogUtil.startLog(BorrowRepaymentController.class.toString(), BorrowRepaymentDefine.REPAY_PLAN_ACTION);
		ModelAndView modelAndView = new ModelAndView(BorrowRepaymentPlanDefine.RE_LIST_PATH);
		attr.addAttribute("borrowNid", form.getBorrowNid());
		// 跳转到还款计划
		LogUtil.endLog(BorrowRepaymentController.class.toString(), BorrowRepaymentDefine.REPAY_PLAN_ACTION);
		return modelAndView;
	}

	/**
	 * 跳转到详情
	 *
	 * @param request
	 * @param form
	 */
	@RequestMapping(BorrowRepaymentDefine.TO_RECOVER_ACTION)
	public ModelAndView toRecoverAction(HttpServletRequest request, BorrowRepaymentBean form, RedirectAttributes attr) {
		LogUtil.startLog(BorrowRepaymentController.class.toString(), BorrowRepaymentDefine.TO_RECOVER_ACTION);
		ModelAndView modelAndView = new ModelAndView(BorrowRepaymentInfoDefine.RE_LIST_PATH);
//		attr.addAttribute("borrowNid", form.getBorrowNid());
//		modelAndView.addObject(attr);
		modelAndView.addObject("borrowNid",form.getBorrowNid());
		// 跳转到还款计划
		LogUtil.endLog(BorrowRepaymentController.class.toString(), BorrowRepaymentDefine.TO_RECOVER_ACTION);
		return modelAndView;
	}

	// -------------------------------------------------------

	/**
	 * 迁移到延期画面
	 *
	 * @param request
	 * @param form
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(BorrowRepaymentDefine.INIT_DELAY_REPAY_ACTION)
	public ModelAndView moveAfterRepayAction(HttpServletRequest request, RepayDelayCustomizeBean form) throws ParseException {
		LogUtil.startLog(BorrowRepaymentController.class.toString(), BorrowRepaymentDefine.INIT_DELAY_REPAY_ACTION);
		ModelAndView modelAndView = new ModelAndView(BorrowRepaymentDefine.DELAY_REPAY_PATH);
		modelAndView = this.getDelayRepayInfo(modelAndView, form.getNid());
		LogUtil.endLog(BorrowFirstController.class.toString(), BorrowRepaymentDefine.INIT_DELAY_REPAY_ACTION);
		return modelAndView;
	}

	/**
	 * 获取延期数据
	 *
	 * @param modelAndView
	 * @param form
	 * @return
	 * @throws ParseException
	 */
	private ModelAndView getDelayRepayInfo(ModelAndView modelAndView, String borrowNid) throws ParseException {
		AdminRepayDelayCustomize repayDelay = this.borrowRepaymentService.selectBorrowInfo(borrowNid);
		modelAndView.addObject("borrowRepayInfo", repayDelay);
		// 单期标
		if (CustomConstants.BORROW_STYLE_ENDDAY.equals(repayDelay.getBorrowStyle()) || CustomConstants.BORROW_STYLE_END.equals(repayDelay.getBorrowStyle())) {
			BorrowRepay borrowRepay = this.borrowRepaymentService.getBorrowRepayDelay(borrowNid, repayDelay.getBorrowApr(), repayDelay.getBorrowStyle());
			modelAndView.addObject("repayInfo", borrowRepay);
			modelAndView.addObject("repayTime", GetDate.formatDate(Long.valueOf(borrowRepay.getRepayTime()) * 1000L));
			modelAndView.addObject("delayDays", borrowRepay.getDelayDays());
		} else {
			BorrowRepayPlan borrowRepayPlan = this.borrowRepaymentService.getBorrowRepayPlanDelay(borrowNid, repayDelay.getBorrowApr(), repayDelay.getBorrowStyle());
			modelAndView.addObject("repayInfo", borrowRepayPlan);
			modelAndView.addObject("repayTime", GetDate.formatDate(Long.valueOf(borrowRepayPlan.getRepayTime()) * 1000L));
			modelAndView.addObject("delayDays", borrowRepayPlan.getDelayDays());
		}
		return modelAndView;
	}

	/**
	 * 延期
	 *
	 * @param request
	 * @param form
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(BorrowRepaymentDefine.DELAY_REPAY_ACTION)
	public ModelAndView afterRepayAction(HttpServletRequest request, RepayDelayCustomizeBean form) throws ParseException {
		LogUtil.startLog(BorrowRepaymentController.class.toString(), BorrowRepaymentDefine.DELAY_REPAY_ACTION);
		ModelAndView modelAndView = new ModelAndView(BorrowRepaymentDefine.DELAY_REPAY_PATH);

		String borrowNid = form.getBorrowNid();

		boolean afterDayFlag = ValidatorFieldCheckUtil.validateSignlessNum(modelAndView, "delayDays", form.getDelayDays(), 1, true);

		if (afterDayFlag) {

			if (!(Integer.valueOf(form.getDelayDays()) >= 1 && Integer.valueOf(form.getDelayDays()) <= 8)) {
				ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "delayDays", "after.repay.day");
			} else {
				// 延期日8天后、应该还款的日期
				Integer lastReapyTime = Integer.valueOf(form.getRepayTime()) + (8 * 24 * 3600);
				// 延期日加上输入的天数后、应该还款的日期
				Integer nowTimePlusDelayDays = Integer.valueOf(form.getRepayTime()) + (Integer.valueOf(form.getDelayDays()) * 24 * 3600);
				if (nowTimePlusDelayDays > lastReapyTime) {
					String repayTimeForMsg = GetDate.formatDate(Long.valueOf(lastReapyTime) * 1000);
					modelAndView.addObject("repayTimeForMsg", repayTimeForMsg);
					ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "delayDays", "delay.day.lt.repaytime");
				}
			}
		}

		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			modelAndView = this.getDelayRepayInfo(modelAndView, borrowNid);
			modelAndView.addObject("delayDays", form.getDelayDays());
			return modelAndView;
		}

		this.borrowRepaymentService.updateBorrowRepayDelayDays(borrowNid, form.getDelayDays());

		modelAndView.addObject(BorrowRepaymentDefine.SUCCESS, BorrowRepaymentDefine.SUCCESS);
		LogUtil.endLog(BorrowFirstController.class.toString(), BorrowRepaymentDefine.DELAY_REPAY_ACTION);
		return modelAndView;
	}

	/**
	 * 迁移到还款画面
	 *
	 * @param request
	 * @param form
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(BorrowRepaymentDefine.INIT_REPAY_ACTION)
	public ModelAndView moveRepayAction(HttpServletRequest request, RepayCustomizeBean form) throws ParseException {
		LogUtil.startLog(BorrowRepaymentController.class.toString(), BorrowRepaymentDefine.INIT_REPAY_ACTION);
		ModelAndView modelAndView = new ModelAndView(BorrowRepaymentDefine.REPAY_PATH);
		modelAndView = this.getRepayInfo(modelAndView, form.getNid());
		LogUtil.endLog(BorrowFirstController.class.toString(), BorrowRepaymentDefine.INIT_REPAY_ACTION);
		return modelAndView;
	}

	/**
	 * 获取还款数据
	 *
	 * @param modelAndView
	 * @param form
	 * @return
	 * @throws ParseException
	 */
	private ModelAndView getRepayInfo(ModelAndView modelAndView, String borrowNid) throws ParseException {

		AdminRepayDelayCustomize repayDelay = this.borrowRepaymentService.selectBorrowInfo(borrowNid);
		modelAndView.addObject("borrowRepayInfo", repayDelay);
		String userId = repayDelay.getUserId();
		Account account = this.borrowRepaymentService.getUserAccount(userId);
		// 借款人账户余额
		BigDecimal balance = account.getBankBalance();
		modelAndView.addObject("balance", balance);
		// 单期标
		if (CustomConstants.BORROW_STYLE_ENDDAY.equals(repayDelay.getBorrowStyle()) || CustomConstants.BORROW_STYLE_END.equals(repayDelay.getBorrowStyle())) {
			BorrowRepayBean borrowRepay = this.borrowRepaymentService.getBorrowRepayInfo(borrowNid, repayDelay.getBorrowApr(), repayDelay.getBorrowStyle());
			modelAndView.addObject("repayInfo", borrowRepay);
		} else {// 多期标
			BorrowRepayPlanBean borrowRepayPlan = this.borrowRepaymentService.getBorrowRepayPlanInfo(borrowNid, repayDelay.getBorrowApr(), repayDelay.getBorrowStyle());
			modelAndView.addObject("repayInfo", borrowRepayPlan);
		}
		return modelAndView;
	}

	/**
	 * 还款
	 *
	 * @param request
	 * @param form
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(BorrowRepaymentDefine.REPAY_ACTION)
	public ModelAndView repayAction(HttpServletRequest request, RepayCustomizeBean form) throws ParseException {

		LogUtil.startLog(BorrowRepaymentController.class.toString(), BorrowRepaymentDefine.REPAY_ACTION);
		ModelAndView modelAndView = new ModelAndView(BorrowRepaymentDefine.REPAY_PATH);
		// 还款密码
		String password = form.getPassword();
		// 还款密码加密处理
		String mdPassword = "123456";

		String borrowNid = form.getBorrowNid();
		String userId = form.getUserId();
		// 根据用户Id 检索用户信息
		Users repayUser = this.borrowRepaymentService.getUsersByUserId(Integer.parseInt(userId));
		String borrowApr = form.getBorrowApr();
		String borrowStyle = form.getBorrowStyle();
		Account account = this.borrowRepaymentService.getUserAccount(userId);
		BigDecimal balance = account.getBankBalance();
		// Ip地址
		String ip = GetCilentIP.getIpAddr(request);
		balance = new BigDecimal(2000);
		// 加密后的密码与数据库的密码进行比较
		if (!StringUtils.equals(mdPassword, password)) {
			ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "password", "repay.password");
		}
		if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle) || CustomConstants.BORROW_STYLE_END.equals(borrowStyle)) {
			BorrowRepayBean borrowRepay = this.borrowRepaymentService.getBorrowRepayInfo(borrowNid, borrowApr, borrowStyle);
			BigDecimal repayTotal = borrowRepay.getRepayAccountAll();
			if (repayTotal.compareTo(balance) == 0 || repayTotal.compareTo(balance) == -1) {
				userId = "47";
				BankOpenAccount accountChinapnr = this.borrowRepaymentService.getBankOpenAccount(Integer.parseInt(userId));
				BigDecimal userBalance = this.borrowRepaymentService.getBankBalance(Integer.parseInt(userId), accountChinapnr.getAccount());
				if (repayTotal.compareTo(userBalance) == 0 || repayTotal.compareTo(userBalance) == -1) {
					// 申请还款冻结资金
					// 调用江西银行还款申请冻结资金
					BankCallBean bean = new BankCallBean();
					bean.setVersion(BankCallConstant.VERSION_10);// 版本号
					bean.setTxCode(BankCallMethodConstant.TXCODE_BALANCE_FREEZE);// 交易代码
					bean.setTxDate(GetOrderIdUtils.getTxDate()); // 交易日期
					bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
					bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
					bean.setChannel(BankCallConstant.CHANNEL_PC); // 交易渠道
					bean.setAccountId(accountChinapnr.getAccount());// 电子账号
					bean.setOrderId(GetOrderIdUtils.getOrderId2(Integer.parseInt(userId))); // 订单号
					bean.setTxAmount(String.valueOf(repayTotal));// 交易金额
					bean.setProductId(borrowNid);
					bean.setFrzType("0");
					bean.setLogOrderId(GetOrderIdUtils.getOrderId2(Integer.parseInt(userId)));// 订单号
					bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
					bean.setLogUserId(String.valueOf(userId));
					bean.setLogUserName(repayUser == null ? "" : repayUser.getUsername());
					bean.setLogClient(0);
					bean.setLogIp(ip);
					BankCallBean callBackBean = BankCallUtils.callApiBg(bean);
					String respCode = callBackBean == null ? "" : callBackBean.getRetCode();
					// 申请冻结资金失败
					if (!BankCallConstant.RESPCODE_SUCCESS.equals(respCode)) {
						System.out.println("调用还款申请冻结资金接口失败:" + callBackBean.getRetMsg() + "订单号:" + callBackBean.getOrderId());
						modelAndView.addObject("message", "还款失败，请稍后再试！");
						return modelAndView;
					}
				} else {
					ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "password", "repay.balance.huifu");
				}
			} else {
				ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "password", "repay.balance");
			}
			if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
				modelAndView = this.getRepayInfo(modelAndView, borrowNid);
				return modelAndView;
			}

			borrowRepay.setIp(ip);
			this.borrowRepaymentService.updateBorrowRecover(borrowRepay);
		} else {
			BorrowRepayPlanBean borrowRepayPlan = this.borrowRepaymentService.getBorrowRepayPlanInfo(borrowNid, borrowApr, borrowStyle);
			BigDecimal repayTotal = borrowRepayPlan.getRepayAccountAll();
			if (repayTotal.compareTo(balance) == 0 || repayTotal.compareTo(balance) == -1) {
				BankOpenAccount accountChinapnr = this.borrowRepaymentService.getBankOpenAccount(Integer.parseInt(userId));
				BigDecimal userBalance = this.borrowRepaymentService.getBankBalance(Integer.parseInt(userId), accountChinapnr.getAccount());
				if (repayTotal.compareTo(userBalance) == 0 || repayTotal.compareTo(userBalance) == -1) {
					// 申请还款冻结资金
					// 调用江西银行还款申请冻结资金
					BankCallBean bean = new BankCallBean();
					bean.setVersion(BankCallConstant.VERSION_10);// 版本号
					bean.setTxCode(BankCallMethodConstant.TXCODE_BALANCE_FREEZE);// 交易代码
					bean.setTxDate(GetOrderIdUtils.getTxDate()); // 交易日期
					bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
					bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
					bean.setChannel(BankCallConstant.CHANNEL_PC); // 交易渠道
					bean.setAccountId(accountChinapnr.getAccount());// 电子账号
					bean.setOrderId(GetOrderIdUtils.getOrderId2(Integer.parseInt(userId))); // 订单号
					bean.setTxAmount(String.valueOf(repayTotal));// 交易金额
					bean.setProductId(borrowNid);
                    bean.setFrzType("0");
					bean.setLogOrderId(GetOrderIdUtils.getOrderId2(Integer.parseInt(userId)));// 订单号
					bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
					bean.setLogUserId(String.valueOf(userId));
					bean.setLogUserName(repayUser == null ? "" : repayUser.getUsername());
					bean.setLogClient(0);
					bean.setLogIp(ip);
					BankCallBean callBackBean = BankCallUtils.callApiBg(bean);
					String respCode = callBackBean == null ? "" : callBackBean.getRetCode();
					// 申请冻结资金失败
					if (!BankCallConstant.RESPCODE_SUCCESS.equals(respCode)) {
						System.out.println("调用还款申请冻结资金接口失败:" + callBackBean.getRetMsg() + "订单号:" + callBackBean.getOrderId());
						modelAndView.addObject("message", "还款失败，请稍后再试！");
						return modelAndView;
					}
				} else {
					ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "password", "repay.balance.huifu");
				}
			} else {
				ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "password", "repay.balance");
			}
			if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
				modelAndView = this.getRepayInfo(modelAndView, borrowNid);
				return modelAndView;
			}
			borrowRepayPlan.setIp(ip);
			this.borrowRepaymentService.updateBorrowRecoverPlan(borrowRepayPlan);
		}

		modelAndView.addObject(BorrowRepaymentDefine.SUCCESS, BorrowRepaymentDefine.SUCCESS);
		LogUtil.endLog(BorrowFirstController.class.toString(), BorrowRepaymentDefine.REPAY_ACTION);
		return modelAndView;
	}

	/**
	 * 重新还款按钮
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowRepaymentDefine.RESTART_REPAY_ACTION)
	public ModelAndView restartRepayAction(HttpServletRequest request, RedirectAttributes attr, BorrowRepaymentBean form) throws ParseException {
		LogUtil.startLog(BorrowFullController.class.toString(), BorrowRepaymentDefine.RESTART_REPAY_ACTION);
		ModelAndView modelAndView = new ModelAndView(BorrowRepaymentDefine.LIST_PATH);

		attr.addFlashAttribute(BorrowRepaymentDefine.REPAYMENT_FORM, form);
		modelAndView = new ModelAndView(BorrowRepaymentDefine.RE_LIST_PATH);
		this.borrowRepaymentService.updateBorrowApicronRecord(form == null ? "" : form.getBorrowNidHidden());

		LogUtil.endLog(BorrowFullController.class.toString(), BorrowRepaymentDefine.RESTART_REPAY_ACTION);
		return modelAndView;
	}

	/**
	 * 更新管理费
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequiresPermissions(BorrowRepaymentDefine.PERMISSIONS_ADD)
	@RequestMapping(BorrowRepaymentDefine.UPDATE_RECOVER_FEE_ACTION)
	public ModelAndView updateRecoverFeeAction(HttpServletRequest request, RedirectAttributes attr, BorrowRepaymentBean form) throws ParseException {
		LogUtil.startLog(BorrowFullController.class.toString(), BorrowRepaymentDefine.RESTART_REPAY_ACTION);
		ModelAndView modelAndView = new ModelAndView(BorrowRepaymentDefine.LIST_PATH);

		attr.addFlashAttribute(BorrowRepaymentDefine.REPAYMENT_FORM, form);
		modelAndView = new ModelAndView(BorrowRepaymentDefine.RE_LIST_PATH);
		try {
			this.borrowRepaymentService.incomeFeeService();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		LogUtil.endLog(BorrowFullController.class.toString(), BorrowRepaymentDefine.RESTART_REPAY_ACTION);
		return modelAndView;
	}
}
