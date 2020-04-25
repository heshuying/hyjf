package com.hyjf.admin.finance.web;


import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.*;
import com.hyjf.mybatis.model.auto.AccountTrade;
import com.hyjf.mybatis.model.customize.WebCustomize;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * 网站收支
 * @author HBZ
 */
@Controller
@RequestMapping(value = WebDefine.REQUEST_MAPPING)
public class WebController extends BaseController  {

	@Autowired
	private WebService webService;
	
	/**
	 * 分页
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modeAndView, WebBean form){
		WebCustomize webCustomize = new WebCustomize();
		BeanUtils.copyProperties(form, webCustomize);
		
		//交易类型列表
		List<AccountTrade> trades= this.webService.selectTradeTypes();
		form.setTradeList(trades);
		
		Integer count = this.webService.queryWebCount(webCustomize);
		if(count>0){
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			webCustomize.setLimitStart(paginator.getOffset());
			webCustomize.setLimitEnd(paginator.getLimit());
			List<WebCustomize> webCustomizes = this.webService.queryWebList(webCustomize); 
			form.setPaginator(paginator);
			form.setRecordList(webCustomizes);
			modeAndView.addObject(WebDefine.WEB_FORM, form);
			//交易金额总计
			String sumAccount = this.webService.selectBorrowInvestAccount(webCustomize);
			modeAndView.addObject("sumAccount", sumAccount);
		}else{
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			webCustomize.setLimitStart(paginator.getOffset());
			webCustomize.setLimitEnd(paginator.getLimit());
			form.setPaginator(paginator);
			modeAndView.addObject(WebDefine.WEB_FORM, form);
		}
		
	}
	/**
	 * 网站收支 列表
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(WebDefine.WEB_LIST)
	@RequiresPermissions(WebDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, WebBean form) {
		LogUtil.startLog(WebController.class.toString(), WebDefine.WEB_LIST);
		ModelAndView modeAndView = new ModelAndView(WebDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(WebController.class.toString(), WebDefine.WEB_LIST);
		return modeAndView;
	}
	
	/**
	 * 网站收支  查询条件
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(WebDefine.WEB_LIST_WITHQ)
	@RequiresPermissions(WebDefine.PERMISSIONS_VIEW)
	public ModelAndView initWithQ(HttpServletRequest request, WebBean form) {
		LogUtil.startLog(WebController.class.toString(), WebDefine.WEB_LIST);
		ModelAndView modeAndView = new ModelAndView(WebDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(WebController.class.toString(), WebDefine.WEB_LIST);
		return modeAndView;
	}


    /**
     * 根据业务需求导出相应的表格 此处暂时为可用情况 缺陷： 1.无法指定相应的列的顺序， 2.无法配置，excel文件名，excel sheet名称
     * 3.目前只能导出一个sheet 4.列的宽度的自适应，中文存在一定问题
     * 5.根据导出的业务需求最好可以在导出的时候输入起止页码，因为在大数据量的情况下容易造成卡顿
     * 
     * 导出网站收支列表
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(WebDefine.ENHANCE_EXPORT_ACTION)
	@RequiresPermissions(value = {WebDefine.PERMISSIONS_WEB_EXPORT, WebDefine.ENHANCE_PERMISSIONS_EXPORT}, logical = Logical.AND)
    public void enhanceExportWeblistExcel(HttpServletRequest request, HttpServletResponse response, WebBean form) throws Exception {
        // 从session中获取相应的登录用户名
//        String userName = ShiroUtil.getLoginUsername();
        // 表格sheet名称
        String sheetName = "网站收支";
         		 	 	 	 	 	 	
        // 取得数据
        form.setLimitStart(-1);
        form.setLimitEnd(-1);
		//设置默认查询时间
		if(StringUtils.isEmpty(form.getStartDate())){
			form.setStartDate(GetDate.getDate("yyyy-MM-dd"));
		}
		if(StringUtils.isEmpty(form.getEndDate())){
			form.setEndDate(GetDate.getDate("yyyy-MM-dd"));
		}

        List<WebCustomize> recordList = this.webService.queryWebList(form);

        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

        String[] titles = new String[] { "序号", "订单号", "分公司", "分部", "团队", "用户名", "姓名", "收支类型", "交易金额", "交易类型", "说明", "发生时间" };  //, "摘要"      
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
                	WebCustomize bean = recordList.get(i);

                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);

                    // 序号
                    if (celLength == 0) {
                        cell.setCellValue(i + 1);
                    }
                    // 订单号
                    else if (celLength == 1) {
                        cell.setCellValue(bean.getOrdid());
                    }
                    // 大区
                    else if (celLength == 2) {
                        cell.setCellValue(bean.getRegionName());
                    }
                    // 分公司
                    else if (celLength == 3) {
                        cell.setCellValue(bean.getBranchName());
                    }
                    // 部门
                    else if (celLength == 4) {
                        cell.setCellValue(bean.getDepartmentName());
                    }
                    // 用户名
                    else if (celLength == 5) {
                        cell.setCellValue(bean.getUsername());
                    }
                    // 姓名
                    else if (celLength == 6) {
                        cell.setCellValue(bean.getTruename());
                    }
                    // 收支类型
                    else if (celLength == 7) {
                        cell.setCellValue(bean.getTypeName());
                    }
                    // 交易金额
                    else if (celLength == 8) {
                        cell.setCellValue(bean.getAmount() == null ? "0.00" : bean.getAmount().toString());
                    }
                    // 交易类型
                    else if (celLength == 9) {
                        cell.setCellValue(bean.getTradeType());
                    }
                    // 说明
                    else if (celLength == 10) {
                        cell.setCellValue(bean.getRemark());
                    }
                    // 摘要
//                    else if (celLength == 11) {
//                        cell.setCellValue(bean.getNote());
//                    }
                    // 发生时间
                    else if (celLength == 11) {
                        cell.setCellValue(bean.getCreateTime());
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
    }

	@RequestMapping(WebDefine.EXPORT_WEB_ACTION)
	@RequiresPermissions(WebDefine.PERMISSIONS_WEB_EXPORT)
	public void exportWeblistExcel(HttpServletRequest request, HttpServletResponse response, WebBean form) throws Exception {
		// 从session中获取相应的登录用户名
//        String userName = ShiroUtil.getLoginUsername();
		// 表格sheet名称
		String sheetName = "网站收支";

		// 取得数据
		form.setLimitStart(-1);
		form.setLimitEnd(-1);
		//设置默认查询时间
		if(StringUtils.isEmpty(form.getStartDate())){
			form.setStartDate(GetDate.getDate("yyyy-MM-dd"));
		}
		if(StringUtils.isEmpty(form.getEndDate())){
			form.setEndDate(GetDate.getDate("yyyy-MM-dd"));
		}

		List<WebCustomize> recordList = this.webService.queryWebList(form);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "序号", "订单号", "用户名", "姓名", "收支类型", "交易金额", "交易类型", "说明", "发生时间" };  //, "摘要"
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
					WebCustomize bean = recordList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 订单号
					else if (celLength == 1) {
						cell.setCellValue(bean.getOrdid());
					}
					// 用户名
					else if (celLength == 2) {
						cell.setCellValue(bean.getUsername());
					}
					// 姓名
					else if (celLength == 3) {
						cell.setCellValue(bean.getTruename());
					}
					// 收支类型
					else if (celLength == 4) {
						cell.setCellValue(bean.getTypeName());
					}
					// 交易金额
					else if (celLength == 5) {
						cell.setCellValue(bean.getAmount() == null ? "0.00" : bean.getAmount().toString());
					}
					// 交易类型
					else if (celLength == 6) {
						cell.setCellValue(bean.getTradeType());
					}
					// 说明
					else if (celLength == 7) {
						cell.setCellValue(bean.getRemark());
					}
					// 发生时间
					else if (celLength == 8) {
						cell.setCellValue(bean.getCreateTime());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
	}
    
    /**
     * 余额查询
     * @param request
     * @return
     */
	@RequestMapping(WebDefine.YUE_SEARCH_ACTION)
	@RequiresPermissions(WebDefine.PERMISSIONS_VIEW)
	public ModelAndView yueSearch(HttpServletRequest request) {
		LogUtil.startLog(WebController.class.toString(), WebDefine.YUE_SEARCH_ACTION);
		ModelAndView modeAndView = new ModelAndView(WebDefine.YUE_DETAILS_PATH);
		// 取得客户编号
		String companyId = PropUtils.getSystem(ChinaPnrConstant.PROP_MERCUSTID);
		// 取得公司账户余额
		double companyYue = webService.getCompanyYuE(companyId);
		modeAndView.addObject("companyYue", companyYue);
		LogUtil.endLog(WebController.class.toString(), WebDefine.YUE_SEARCH_ACTION);
		return modeAndView;
	}
    
}

	

