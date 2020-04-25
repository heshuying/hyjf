package com.hyjf.admin.datacenter.coupon;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.customize.datacenter.DataCenterCouponCustomize;

/**
 * 优惠券发行
 * 
 * @author
 *
 */
@Controller
@RequestMapping(value = DataCenterCouponDefine.REQUEST_DJ_MAPPING)
public class DataCenterCouponDJController extends BaseController {

	@Autowired
	private DataCenterCouponService dataCenterCouponService;

	
	
	/**
     * 加息卷画面初始化
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(DataCenterCouponDefine.DATA_CENTER_COUPON_DJ_INIT)
    @RequiresPermissions(DataCenterCouponDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(DataCenterCouponDefine.FORM) DataCenterCouponBean form) {
        LogUtil.startLog(DataCenterCouponDJController.class.toString(), DataCenterCouponDefine.DATA_CENTER_COUPON_DJ_INIT);
        ModelAndView modelAndView = new ModelAndView(DataCenterCouponDefine.DATA_CENTER_COUPON_DJ_LIST_PATH);
        // 创建分页
        this.createDJPage(request, modelAndView, form);
        LogUtil.endLog(DataCenterCouponDJController.class.toString(), DataCenterCouponDefine.DATA_CENTER_COUPON_DJ_INIT);
        return modelAndView;
    }


    /**
     * 加息卷分页机能
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createDJPage(HttpServletRequest request, ModelAndView modelAndView, DataCenterCouponBean form) {
        
        
    /* // 项目状态
        List<ParamName> list = this.dataCenterCouponService.getParamNameList(CustomConstants.COUPON_RECIVE_STATUS);
        modelAndView.addObject("couponReciveStatusList", list);*/
        
        
        DataCenterCouponCustomize dataCenterCouponCustomize =createDataCenterCouponCustomize(form);
        Integer count = this.dataCenterCouponService.countRecordDJ(dataCenterCouponCustomize);
        if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            dataCenterCouponCustomize.setLimitStart(paginator.getOffset());
            dataCenterCouponCustomize.setLimitEnd(paginator.getLimit());
            List<DataCenterCouponCustomize>  recordList = this.dataCenterCouponService.getRecordListDJ(dataCenterCouponCustomize);
            form.setPaginator(paginator);
            modelAndView.addObject("recordList",recordList);
        }
        modelAndView.addObject(DataCenterCouponDefine.FORM, form);
    }
    

    /**
     * 导出加息券功能
     * 
     * @param request
     * @param modelAndView
     * @param form
     */
    @RequestMapping(DataCenterCouponDefine.EXPORT_DJ_ACTION)
    @RequiresPermissions(DataCenterCouponDefine.PERMISSIONS_EXPORT)
    public void exportDJAction(HttpServletRequest request, HttpServletResponse response, @ModelAttribute(DataCenterCouponDefine.FORM) DataCenterCouponBean form) throws Exception {
        LogUtil.startLog(DataCenterCouponDefine.class.toString(), DataCenterCouponDefine.EXPORT_DJ_ACTION);
        // 表格sheet名称
        String sheetName = "代金券列表";
        DataCenterCouponCustomize dataCenterCouponCustomize =createDataCenterCouponCustomize(form);
        List<DataCenterCouponCustomize> resultList  = this.dataCenterCouponService.getRecordListDJ(dataCenterCouponCustomize);
        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
        String[] titles = new String[] {"序号", "来源", "已发放数量","已使用数量","已失效数量","使用率","失效率","总收益","已发放收益" ,"待发放收益" ,"累计真实出借金额" };
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
                    DataCenterCouponCustomize pInfo = resultList.get(i);
                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);
                    // 序号
                    if (celLength == 0) {
                        cell.setCellValue(i + 1);
                    }
                    else if (celLength == 1) {
                        cell.setCellValue(pInfo.getTitle());
                    }
                    else if (celLength == 2) {
                        cell.setCellValue(pInfo.getGrantNum());
                    }
                    else if (celLength == 3) {
                        cell.setCellValue(pInfo.getUsedNum());
                    }
                    else if (celLength == 4) {
                        cell.setCellValue(pInfo.getExpireNum());
                    }
                    else if (celLength == 5) {
                        cell.setCellValue(pInfo.getUtilizationRate());
                    }
                    else if (celLength == 6) {
                        cell.setCellValue(pInfo.getFailureRate());
                    }
                    else if (celLength == 7) {
                        cell.setCellValue(pInfo.getRecoverInterest());
                    }
                    else if (celLength == 8) {
                        cell.setCellValue(pInfo.getRecivedMoney());
                    }
                    else if (celLength == 9) {
                        cell.setCellValue(pInfo.getNorecivedMoney());
                    }
                    else if (celLength == 10) {
                        cell.setCellValue(pInfo.getRealTenderMoney());
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);

        LogUtil.endLog(CouponConfigController.class.toString(), DataCenterCouponDefine.EXPORT_DJ_ACTION);
    }
    
    
    
   

	private DataCenterCouponCustomize createDataCenterCouponCustomize(DataCenterCouponBean form) {
	    DataCenterCouponCustomize dataCenterCouponCustomize = new DataCenterCouponCustomize();

       /* if(StringUtils.isNotEmpty(form.getOrderId())){
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
            couponBackMoneyCustomize.setTimeStartSrch(form.getTimeStartSrch());
        }
        if(StringUtils.isNotEmpty(form.getTimeEndSrch())){
            couponBackMoneyCustomize.setTimeEndSrch(form.getTimeEndSrch());
        }*/
        return dataCenterCouponCustomize;
    }

}
