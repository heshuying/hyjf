package com.hyjf.admin.manager.activity.billion.second;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.ActivityBillionSecond;
import com.hyjf.mybatis.model.auto.ParamName;

/**
 * 助力百亿活动
 * @author Michael
 */
@Controller
@RequestMapping(value = BillionSecondDefine.REQUEST_MAPPING)
public class BillionSecondController extends BaseController {

	@Autowired
	private BillionSecondService billionSecondService;

	 /**
     * 画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(BillionSecondDefine.INIT)
    @RequiresPermissions(BillionSecondDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, BillionSecondBean form) {
        LogUtil.startLog(BillionSecondController.class.toString(), BillionSecondDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(BillionSecondDefine.LIST_PATH);

        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(BillionSecondController.class.toString(), BillionSecondDefine.INIT);
        return modelAndView;
    }

    /**
     * 查询画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(BillionSecondDefine.SEARCH_ACTION)
    @RequiresPermissions(BillionSecondDefine.PERMISSIONS_SEARCH)
    public ModelAndView searchAction(HttpServletRequest request, BillionSecondBean form) {
        LogUtil.startLog(BillionSecondController.class.toString(), BillionSecondDefine.SEARCH_ACTION);
        ModelAndView modelAndView = new ModelAndView(BillionSecondDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(BillionSecondController.class.toString(), BillionSecondDefine.SEARCH_ACTION);
        return modelAndView;
    }

    /**
     * 创建分页机能
     * 
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, BillionSecondBean form) {
        Integer count = this.billionSecondService.selectRecordCount(form);
        if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
            List<ActivityBillionSecond> recordList = this.billionSecondService.getRecordList(form,paginator.getOffset(),paginator.getLimit());
            form.setPaginator(paginator);
            form.setRecordList(recordList);
        }
        // 奖励类型
        modelAndView.addObject("billionPrizeTypes",this.billionSecondService.getParamNameList("BILLION_PRIZE_TYPE"));
        
        modelAndView.addObject(BillionSecondDefine.FORM, form);
    }


	/**
     * 数据导出
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(BillionSecondDefine.EXPORT_ACTION)
    @RequiresPermissions(BillionSecondDefine.PERMISSIONS_EXPORT)
    public void exportAction(HttpServletRequest request, HttpServletResponse response, BillionSecondBean form) throws Exception {
        LogUtil.startLog(BillionSecondController.class.toString(), BillionSecondDefine.EXPORT_ACTION);
        // 表格sheet名称
        String sheetName = "十一月份助力百亿活动";
        //记录列表
        List<ActivityBillionSecond> resultList = this.billionSecondService.getRecordList(form,-1,-1);
        //数据字典
    	List<ParamName> list = this.billionSecondService.getParamNameList("BILLION_PRIZE_TYPE");
        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

        String[] titles = new String[] { "序号", "用户名", "姓名", "手机号", "出借直投金额", "获得奖励", "优惠券ID", "是否发放"};
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
                	ActivityBillionSecond record = resultList.get(i);

                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);

                    // 序号
                    if (celLength == 0) {
                        cell.setCellValue(i + 1);
                    }
                    else if (celLength == 1) {
                    	 cell.setCellValue(StringUtils.isEmpty(record.getUserName()) ? "" : record.getUserName());
                    }
                    else if (celLength == 2) {
                        cell.setCellValue(StringUtils.isEmpty(record.getUserName()) ? "" : record.getUserName());
                    }
                    else if (celLength == 3) {
                        cell.setCellValue(StringUtils.isEmpty(record.getMobile()) ? "" : record.getMobile());
                    }
                    else if (celLength == 4) {
                    	 cell.setCellValue(record.getTenderMoney() == null ? "" :record.getTenderMoney().toString());
                    }
                    else if (celLength == 5) {
                    	if(record.getPrizeId() != null){
                    		for(int j = 0;j<list.size(); j++){
                    			if(String.valueOf(record.getPrizeId()).equals(list.get(j).getNameCd())){
                    				 cell.setCellValue(list.get(j).getName());
                    			}
                    		}
                    	}else{
                    		 cell.setCellValue("");
                    	}
                    }
                    else if (celLength == 6) {
                        cell.setCellValue(record.getCouponCode());
                    } 
                    else if (celLength == 7) {
                    	if(record.getIsSend() == 0){
                    		 cell.setCellValue("未发放");
                    	}else{
                    		 cell.setCellValue("已发放");
                    	}
                       
                    } 
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
        LogUtil.endLog(BillionSecondController.class.toString(), BillionSecondDefine.EXPORT_ACTION);
    }
    
    
}
