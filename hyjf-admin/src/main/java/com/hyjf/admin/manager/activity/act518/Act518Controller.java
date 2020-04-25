package com.hyjf.admin.manager.activity.act518;

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
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.servlet.ModelAndView;


import com.hyjf.admin.BaseController;

import com.hyjf.admin.manager.borrow.borrow.BorrowController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.ActdecFinancing;


/**
 * 
 * 518理财活动
 * @author dddzs
 * @version hyjf 1.0
 * @since hyjf 1.0 2018年4月26日
 * @see 上午9:56:26
 */
@Controller
@RequestMapping(value = Act518Define.REQUEST_MAPPING)
public class Act518Controller extends BaseController {

	@Autowired
	private Act518Service act518Service;

	 /**
     * 画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(Act518Define.INIT)
    @RequiresPermissions(Act518Define.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, Act518Bean form) {
        LogUtil.startLog(Act518Controller.class.toString(), Act518Define.INIT);
        ModelAndView modelAndView = new ModelAndView(Act518Define.LIST_PATH);

        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(Act518Controller.class.toString(), Act518Define.INIT);
        return modelAndView;
    }

    /**
     * 画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(Act518Define.SEARCH_ACTION)
    @RequiresPermissions(Act518Define.PERMISSIONS_SEARCH)
    public ModelAndView search(HttpServletRequest request, Act518Bean form) {
        LogUtil.startLog(Act518Controller.class.toString(), Act518Define.SEARCH_ACTION);
        ModelAndView modelAndView = new ModelAndView(Act518Define.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(Act518Controller.class.toString(), Act518Define.SEARCH_ACTION);
        return modelAndView;
    }

    /**
     * 创建分页机能
     * 
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, Act518Bean form) {

		Integer count =act518Service.countRecordListDetail(form);
        
        if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));

             List<ActdecFinancing> recordList = act518Service.selectRecordListDetail(form,paginator.getOffset(), paginator.getLimit());
            form.setPaginator(paginator);
            modelAndView.addObject("recordList", recordList);
        }

        modelAndView.addObject(Act518Define.ACTDEC_BALLOON_FORM, form);
    }
    
    
    /**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(Act518Define.EXPORT_ACTION)
	@RequiresPermissions(Act518Define.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, Act518Bean form)
			throws Exception {
		LogUtil.startLog(BorrowController.class.toString(), Act518Define.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "518活动列表";

		

		List<ActdecFinancing> resultList = act518Service.selectRecordListDetail(form,-1, -1);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date())
				+ CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "序号", "用户名", "手机号", "优惠券面值", "出借门槛", "状态", "生成时间"};
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
					sheet = ExportExcel
							.createHSSFWorkbookTitle(workbook, titles, (sheetName + "_第" + sheetCount + "页"));
					rowNum = 1;
				}

				// 新建一行
				Row row = sheet.createRow(rowNum);
				// 循环数据
				for (int celLength = 0; celLength < titles.length; celLength++) {
					ActdecFinancing actdecFinancing = resultList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 用户id
					else if (celLength == 1) {
						cell.setCellValue(actdecFinancing.getUserName() + "");
					}
					// 用户手机号
					else if (celLength == 2) {
						cell.setCellValue(actdecFinancing.getMobile() + "");
					}
					// 用户名
					else if (celLength == 3) {
						cell.setCellValue(actdecFinancing.getFaceValue() + "");
					}
					// 真实姓名
					else if (celLength == 4) {
						cell.setCellValue(actdecFinancing.getThreshold() + "");
					}
					// 出借金额
					else if (celLength == 5) {
						if(actdecFinancing.getType().equals(0)) {
							cell.setCellValue("未使用");
						}else if(actdecFinancing.getType().equals(1)) {
							cell.setCellValue("已使用");
						}else {
							cell.setCellValue("已过期");
						}
						
					}
					// 年化金额
					else if (celLength == 6) {
						
						cell.setCellValue(GetDate.timestamptoNUMStrYYYYMMDDHHMMSS(actdecFinancing.getCreateTime()) + "");
					}
					
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(BorrowController.class.toString(), Act518Define.EXPORT_ACTION);
	}
   
}
