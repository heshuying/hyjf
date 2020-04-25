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
@RequestMapping(value = CouponBackMoneyHjhDefine.TY_REQUEST_MAPPING)
public class CouponBackMoneyTYHjhController extends BaseController {

	@Autowired
	private CouponBackMoneyHjhService couponBackMoneyHztService;

	/**
	 * 体验金画面初始化
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(CouponBackMoneyHjhDefine.BACK_MONEY_TY_INIT)
	@RequiresPermissions(CouponBackMoneyHjhDefine.PERMISSIONS_VIEW_TY)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(CouponBackMoneyHjhDefine.FORM) CouponBackMoneyHjhBean form) {
		LogUtil.startLog(CouponBackMoneyTYHjhController.class.toString(), CouponBackMoneyHjhDefine.BACK_MONEY_TY_INIT);
		ModelAndView modelAndView = new ModelAndView(CouponBackMoneyHjhDefine.BACK_MONEY_TY_LIST_PATH);
		form.setCouponType(1);
        form.setTimeStartSrch(GetDate.date2Str(GetDate.getTodayBeforeOrAfter(-30), new SimpleDateFormat("yyyy-MM-dd")));
        form.setTimeEndSrch(GetDate.date2Str(GetDate.getTodayBeforeOrAfter(0), new SimpleDateFormat("yyyy-MM-dd")));

		// 创建分页
		this.createTYPage(request, modelAndView, form);
		LogUtil.endLog(CouponBackMoneyTYHjhController.class.toString(), CouponBackMoneyHjhDefine.BACK_MONEY_TY_INIT);
		return modelAndView;
	}

	/**
	 * 体验金查询
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(CouponBackMoneyHjhDefine.SEARCH_TY_ACTION)
	@RequiresPermissions(CouponBackMoneyHjhDefine.PERMISSIONS_SEARCH_TY)
	public ModelAndView searchAction(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(CouponBackMoneyHjhDefine.FORM) CouponBackMoneyHjhBean form) {
		LogUtil.startLog(CouponBackMoneyTYHjhController.class.toString(), CouponBackMoneyHjhDefine.SEARCH_TY_ACTION);
		ModelAndView modelAndView = new ModelAndView(CouponBackMoneyHjhDefine.BACK_MONEY_TY_LIST_PATH);
		// 创建分页
		this.createTYPage(request, modelAndView, form);
		LogUtil.endLog(CouponBackMoneyTYHjhController.class.toString(), CouponBackMoneyHjhDefine.SEARCH_TY_ACTION);
		return modelAndView;
	}

	/**
	 * 体验金分页机能
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createTYPage(HttpServletRequest request, ModelAndView modelAndView, CouponBackMoneyHjhBean form) {
		
	    
	 // 项目状态
        List<ParamName> list = this.couponBackMoneyHztService.getParamNameList(CustomConstants.COUPON_RECIVE_STATUS);
        modelAndView.addObject("couponReciveStatusList", list);
	    
	    CouponBackMoneyCustomize couponBackMoneyCustomize =createCouponBackMoneyCustomize(form);
	    couponBackMoneyCustomize.setCouponType("1");
        Integer count = this.couponBackMoneyHztService.countRecordTY(couponBackMoneyCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			couponBackMoneyCustomize.setLimitStart(paginator.getOffset());
			couponBackMoneyCustomize.setLimitEnd(paginator.getLimit());
			List<CouponBackMoneyCustomize>  recordList = this.couponBackMoneyHztService.getRecordListTY(couponBackMoneyCustomize);
			String recoverInterest = this.couponBackMoneyHztService.queryRecoverInterestTotle(couponBackMoneyCustomize);
			String investTotal = this.couponBackMoneyHztService.queryInvestTotal(couponBackMoneyCustomize);
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
    @RequestMapping(CouponBackMoneyHjhDefine.EXPORT_TY_ACTION)
    @RequiresPermissions(CouponBackMoneyHjhDefine.PERMISSIONS_EXPORT_TY)
    public void exportTYAction(HttpServletRequest request, HttpServletResponse response, @ModelAttribute(CouponBackMoneyHjhDefine.FORM) CouponBackMoneyHjhBean form) throws Exception {
        LogUtil.startLog(CouponTenderHztController.class.toString(), CouponBackMoneyHjhDefine.EXPORT_TY_ACTION);
        // 表格sheet名称
        String sheetName = "体验金回款列表";
		//设置默认查询时间
		if(StringUtils.isEmpty(form.getTimeStartSrch())){
			form.setTimeStartSrch(GetDate.getDate("yyyy-MM-dd"));
		}
		if(StringUtils.isEmpty(form.getTimeEndSrch())){
			form.setTimeEndSrch(GetDate.getDate("yyyy-MM-dd"));
		}
        CouponBackMoneyCustomize couponBackMoneyCustomize =createCouponBackMoneyCustomize(form);
        couponBackMoneyCustomize.setCouponType("1");
        List<CouponBackMoneyCustomize> resultList  = this.couponBackMoneyHztService.exoportTYRecordList(couponBackMoneyCustomize);
        String recoverInterest = this.couponBackMoneyHztService.queryRecoverInterestTotle(couponBackMoneyCustomize);
        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
        String[] titles = new String[] { "序号", "用户名", "姓名", "手机号", "客户号", "出借订单号", "项目名称", "项目期限",
        		"项目年化", "智投编号", "体验金面值", "体验金收益期限", "体验金编号", "出借时间","放款时间", "应回款时间",
        		"回款金额", "回款状态", "转载订单号", "实际回款时间"};
        
       
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
                        cell.setCellValue(pInfo.getUsername());
                    }
                    else if (celLength == 2) {
                        cell.setCellValue(pInfo.getTruename());
                    }
                    else if (celLength == 3) {
                        cell.setCellValue(pInfo.getMobile());
                    }
                    else if (celLength == 4) {
                        cell.setCellValue(pInfo.getChinapnrUsrcustid());
                    }
                    else if (celLength == 5) {
                        cell.setCellValue(pInfo.getNid());
                    }
                    else if (celLength == 6) {
                        cell.setCellValue(pInfo.getBorrowName());
                    }
                    else if (celLength == 7) {
                        cell.setCellValue(pInfo.getBorrowPeriod()+"个月");
                    }
                    else if (celLength == 8) {
                        cell.setCellValue(pInfo.getBorrowApr());
                    }
                    else if (celLength == 9) {
                        cell.setCellValue(pInfo.getBorrowNid());
                    }
                    else if (celLength == 10) {
                        cell.setCellValue("￥"+pInfo.getCouponQuota());
                    }
                    else if (celLength == 11) {
                        cell.setCellValue(pInfo.getCouponProfitTime()+"天");
                    }
                    else if (celLength == 12) {
                        cell.setCellValue(pInfo.getCouponUserCode());
                    }
                    else if (celLength == 13) {
                        cell.setCellValue(pInfo.getAddTime());
                    }
                    else if (celLength == 14) {
                        cell.setCellValue(pInfo.getHcrAddTime());
                    }
                    else if (celLength == 15) {
                        cell.setCellValue(pInfo.getRecoverTime());
                    }
                    else if (celLength == 16) {
                        cell.setCellValue("￥"+pInfo.getRecoverInterest());
                    }
                    else if (celLength == 17) {
                        cell.setCellValue(pInfo.getReceivedFlg());
                    }
                    else if (celLength == 18) {
                        cell.setCellValue(pInfo.getTransferId());
                    }
                    else if (celLength == 19) {
                        cell.setCellValue(pInfo.getRecoverYestime());
                    }
                }
            }
            rowNum++;
            Row row = sheet.createRow(rowNum);
            Cell cell1 = row.createCell(0);
            cell1.setCellValue("合计");
            Cell cell2 = row.createCell(16);
            cell2.setCellValue("￥"+recoverInterest);
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);

        LogUtil.endLog(CouponConfigController.class.toString(), CouponBackMoneyHjhDefine.EXPORT_TY_ACTION);
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
        if(StringUtils.isNotEmpty(form.getCouponUserCode())){
            couponBackMoneyCustomize.setCouponUserCode(form.getCouponUserCode());
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
        return couponBackMoneyCustomize;
    }

}
