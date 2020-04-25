package com.hyjf.admin.coupon.backmoney.hjh;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.coupon.config.CouponConfigController;
import com.hyjf.admin.coupon.tender.hzt.CouponTenderHztController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.coupon.CouponBackMoneyCustomize;

/**
 * 优惠券发行
 * 
 * @author
 *
 */
@Controller
@RequestMapping(value = CouponBackMoneyHjhDefine.JX_REQUEST_MAPPING)
public class CouponBackMoneyJXHjhController extends BaseController {

	@Autowired
	private CouponBackMoneyHjhService couponBackMoneyHjhService;



	/**
     * 加息卷画面初始化
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(CouponBackMoneyHjhDefine.BACK_MONEY_JX_INIT)
    @RequiresPermissions(CouponBackMoneyHjhDefine.PERMISSIONS_VIEW_JX)
    public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(CouponBackMoneyHjhDefine.FORM) CouponBackMoneyHjhBean form) {
        LogUtil.startLog(CouponBackMoneyJXHjhController.class.toString(), CouponBackMoneyHjhDefine.BACK_MONEY_JX_INIT);
        ModelAndView modelAndView = new ModelAndView(CouponBackMoneyHjhDefine.BACK_MONEY_JX_LIST_PATH);
        form.setCouponType(2);
        form.setTimeStartSrch(GetDate.date2Str(GetDate.getTodayBeforeOrAfter(-30), new SimpleDateFormat("yyyy-MM-dd")));
        form.setTimeEndSrch(GetDate.date2Str(GetDate.getTodayBeforeOrAfter(0), new SimpleDateFormat("yyyy-MM-dd")));
        // 创建分页
        this.createJXPage(request, modelAndView, form);
        LogUtil.endLog(CouponBackMoneyJXHjhController.class.toString(), CouponBackMoneyHjhDefine.BACK_MONEY_JX_INIT);
        return modelAndView;
    }

    /**
     * 加息卷查询
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(CouponBackMoneyHjhDefine.SEARCH_JX_ACTION)
    @RequiresPermissions(CouponBackMoneyHjhDefine.PERMISSIONS_SEARCH_JX)
    public ModelAndView searchAction(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(CouponBackMoneyHjhDefine.FORM) CouponBackMoneyHjhBean form) {
        LogUtil.startLog(CouponBackMoneyJXHjhController.class.toString(), CouponBackMoneyHjhDefine.SEARCH_JX_ACTION);
        ModelAndView modelAndView = new ModelAndView(CouponBackMoneyHjhDefine.BACK_MONEY_JX_LIST_PATH);
        // 创建分页
        this.createJXPage(request, modelAndView, form);
        LogUtil.endLog(CouponBackMoneyJXHjhController.class.toString(), CouponBackMoneyHjhDefine.SEARCH_JX_ACTION);
        return modelAndView;
    }

    /**
     * 加息卷分页机能
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createJXPage(HttpServletRequest request, ModelAndView modelAndView, CouponBackMoneyHjhBean form) {
        
        
     // 项目状态
        List<ParamName> list = this.couponBackMoneyHjhService.getParamNameList(CustomConstants.COUPON_RECIVE_STATUS);
        modelAndView.addObject("couponReciveStatusList", list);
        
        CouponBackMoneyCustomize couponBackMoneyCustomize =createCouponBackMoneyCustomize(form);
        couponBackMoneyCustomize.setCouponType("2");
        Integer count = this.couponBackMoneyHjhService.countRecordJX(couponBackMoneyCustomize);
        if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            couponBackMoneyCustomize.setLimitStart(paginator.getOffset());
            couponBackMoneyCustomize.setLimitEnd(paginator.getLimit());
            List<CouponBackMoneyCustomize>  recordList = this.couponBackMoneyHjhService.getRecordListJX(couponBackMoneyCustomize);
            String recoverInterest = this.couponBackMoneyHjhService.queryRecoverInterestTotle(couponBackMoneyCustomize);
            String investTotal = this.couponBackMoneyHjhService.queryInvestTotal(couponBackMoneyCustomize);
            form.setPaginator(paginator);
            modelAndView.addObject("recoverInterest",recoverInterest);
            modelAndView.addObject("investTotal",investTotal);
            modelAndView.addObject("recordList",recordList);
        }
        modelAndView.addObject(CouponBackMoneyHjhDefine.FORM, form);
    }
    
    
    
    /**
     * 导出功能
     * 
     * @param request
     * @param modelAndView
     * @param form
     */
    @RequestMapping(CouponBackMoneyHjhDefine.EXPORT_JX_ACTION)
    @RequiresPermissions(CouponBackMoneyHjhDefine.PERMISSIONS_EXPORT_JX)
    public void exportJXAction(HttpServletRequest request, HttpServletResponse response, @ModelAttribute(CouponBackMoneyHjhDefine.FORM) CouponBackMoneyHjhBean form) throws Exception {
        LogUtil.startLog(CouponTenderHztController.class.toString(), CouponBackMoneyHjhDefine.EXPORT_JX_ACTION);
        // 表格sheet名称
        String sheetName = "加息券回款列表";
		//设置默认查询时间
		if(StringUtils.isEmpty(form.getTimeStartSrch())){
			form.setTimeStartSrch(GetDate.getDate("yyyy-MM-dd"));
		}
		if(StringUtils.isEmpty(form.getTimeEndSrch())){
			form.setTimeEndSrch(GetDate.getDate("yyyy-MM-dd"));
		}
        CouponBackMoneyCustomize couponBackMoneyCustomize =createCouponBackMoneyCustomize(form);
        couponBackMoneyCustomize.setCouponType("2");
        List<CouponBackMoneyCustomize> resultList  = this.couponBackMoneyHjhService.exoportJXRecordList(couponBackMoneyCustomize);
        String recoverInterest = this.couponBackMoneyHjhService.queryRecoverInterestTotle(couponBackMoneyCustomize);
        String investTotal = this.couponBackMoneyHjhService.queryInvestTotal(couponBackMoneyCustomize);
        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
        String[] titles = new String[] {"序号", "订单号", "用户名","优惠券id","优惠券类型编号",
                "智投编号","回款期数","应回款（元）","转账订单号","状态" ,"应回款日期","使用时间" ,"授权服务金额" ,"来源" ,"内容" };
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        
        // 生成一个表格
        HSSFSheet sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, sheetName + "_第1页");

        if (resultList != null && resultList.size() > 0) {

            int sheetCount = 1;
            int rowNum = 0;

            for (int i = 0; i < resultList.size(); i++) {
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
                    CouponBackMoneyCustomize pInfo = resultList.get(i);
                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);
                    // 序号
                    if (celLength == 0) {
                        cell.setCellValue(i + 1);
                    }
                    else if (celLength == 1) {
                        cell.setCellValue(pInfo.getNid());
                    }
                    else if (celLength == 2) {
                        cell.setCellValue(pInfo.getUsername());
                    }
                    else if (celLength == 3) {
                        cell.setCellValue(pInfo.getCouponUserCode());
                    }
                    else if (celLength == 4) {
                        cell.setCellValue(pInfo.getCouponCode());
                    }
                    else if (celLength == 5) {
                        cell.setCellValue(pInfo.getBorrowNid());
                    }
                    else if (celLength == 6) {
                        cell.setCellValue(pInfo.getRecoverPeriod());
                    }
                    else if (celLength == 7) {
                        cell.setCellValue(pInfo.getRecoverInterest());
                    }
                    else if (celLength == 8) {
                        cell.setCellValue(pInfo.getTransferId());
                    }
                    else if (celLength == 9) {
                        cell.setCellValue(pInfo.getReceivedFlg());
                    }
                    else if (celLength == 10) {
                        cell.setCellValue(pInfo.getRecoverTime());
                    }
                    else if (celLength == 11) {
                        cell.setCellValue(pInfo.getAddTime());
                    }
                    else if (celLength == 12) {
                        cell.setCellValue("￥"+pInfo.getRecoverCapital());
                    }
                    else if (celLength == 13) {
                        cell.setCellValue(pInfo.getCouponSource());
                    }
                    else if (celLength == 14) {
                        cell.setCellValue(pInfo.getCouponContent());
                    }
                }
                
                
            }
            rowNum++;
            Row row = sheet.createRow(rowNum);
            Cell cell1 = row.createCell(0);
            cell1.setCellValue("合计");
            Cell cell2 = row.createCell(7);
            cell2.setCellValue("￥"+recoverInterest);
            Cell cell3 = row.createCell(12);
            cell3.setCellValue("￥"+investTotal);
            
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);

        LogUtil.endLog(CouponConfigController.class.toString(), CouponBackMoneyHjhDefine.EXPORT_JX_ACTION);
    }

	private CouponBackMoneyCustomize createCouponBackMoneyCustomize(CouponBackMoneyHjhBean form) {
	    CouponBackMoneyCustomize couponBackMoneyCustomize = new CouponBackMoneyCustomize();

        if(StringUtils.isNotEmpty(form.getOrderId())){
            couponBackMoneyCustomize.setNid(form.getOrderId());
        }
        if(StringUtils.isNotEmpty(form.getUsername())){
            couponBackMoneyCustomize.setUsername(form.getUsername());
        }
        if(StringUtils.isNotEmpty(form.getCouponCode())){
            couponBackMoneyCustomize.setCouponCode(form.getCouponCode());
        }
        if(StringUtils.isNotEmpty(form.getBorrowNid())){
            couponBackMoneyCustomize.setBorrowNid(form.getBorrowNid());
        }
        if(StringUtils.isNotEmpty(form.getCouponReciveStatus())){
            couponBackMoneyCustomize.setReceivedFlg(form.getCouponReciveStatus());
        }
        if(StringUtils.isNotEmpty(form.getTimeStartSrch())){
            couponBackMoneyCustomize.setTimeStartSrch(String.valueOf(GetDate.getDayStart10(form.getTimeStartSrch())));
        }
        if(StringUtils.isNotEmpty(form.getTimeEndSrch())){
            couponBackMoneyCustomize.setTimeEndSrch(String.valueOf(GetDate.getDayEnd10(form.getTimeEndSrch())));
        }
        if(StringUtils.isNotEmpty(form.getCouponType()+"")){
            couponBackMoneyCustomize.setCouponType(form.getCouponType()+"");
        }
        if(StringUtils.isNotEmpty(form.getCouponUserCode())){
            couponBackMoneyCustomize.setCouponUserCode(form.getCouponUserCode());
        }
        return couponBackMoneyCustomize;
    }

}
