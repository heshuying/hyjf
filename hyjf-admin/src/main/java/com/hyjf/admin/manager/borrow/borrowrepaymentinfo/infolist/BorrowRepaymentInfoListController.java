package com.hyjf.admin.manager.borrow.borrowrepaymentinfo.infolist;

import com.hyjf.admin.manager.borrow.borrowrepaymentinfo.BorrowRepaymentInfoService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.HjhInstConfig;
import com.hyjf.mybatis.model.customize.BorrowRepaymentInfoListCustomize;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
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

@Controller
@RequestMapping(value = BorrowRepaymentInfoListDefine.REQUEST_MAPPING)
public class BorrowRepaymentInfoListController {

	@Autowired
	private BorrowRepaymentInfoListService borrowRepaymentInfoListService;
	@Autowired
    private BorrowRepaymentInfoService borrowRepaymentInfoService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowRepaymentInfoListDefine.INIT)
	@RequiresPermissions(BorrowRepaymentInfoListDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, BorrowRepaymentInfoListBean form) {
		LogUtil.startLog(BorrowRepaymentInfoListController.class.toString(), BorrowRepaymentInfoListDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(BorrowRepaymentInfoListDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BorrowRepaymentInfoListController.class.toString(), BorrowRepaymentInfoListDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowRepaymentInfoListDefine.SEARCH_ACTION)
	@RequiresPermissions(BorrowRepaymentInfoListDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, BorrowRepaymentInfoListBean form) {
		LogUtil.startLog(BorrowRepaymentInfoListController.class.toString(), BorrowRepaymentInfoListDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(BorrowRepaymentInfoListDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BorrowRepaymentInfoListController.class.toString(), BorrowRepaymentInfoListDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, BorrowRepaymentInfoListBean form) {
		/*if (StringUtils.isNotBlank(form.getStatus())) {
			form.setStatus(null);
		}*/
		/*--------------add by LSY START-------------------*/
        // 资金来源
        List<HjhInstConfig> hjhInstConfigList = this.borrowRepaymentInfoService.hjhInstConfigList("");
        modelAndView.addObject("hjhInstConfigList", hjhInstConfigList);
        /*--------------add by LSY END-------------------*/
		// 判断是从哪个Controller跳过来的
		// 0代表从Plan来,1代表从Info来
		BorrowRepaymentInfoListCustomize borrowRepaymentInfoListCustomize = new BorrowRepaymentInfoListCustomize();
		BeanUtils.copyProperties(form, borrowRepaymentInfoListCustomize);
		Long count = this.borrowRepaymentInfoListService.countBorrowRepaymentInfoList(borrowRepaymentInfoListCustomize);
	    if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
            borrowRepaymentInfoListCustomize.setLimitStart(paginator.getOffset());
            borrowRepaymentInfoListCustomize.setLimitEnd(paginator.getLimit());
            List<BorrowRepaymentInfoListCustomize> recordList = this.borrowRepaymentInfoListService.selectBorrowRepaymentInfoListList(borrowRepaymentInfoListCustomize);
            form.setPaginator(paginator);
            modelAndView.addObject("recordList", recordList);
            BorrowRepaymentInfoListCustomize sumObject = this.borrowRepaymentInfoListService.sumBorrowRepaymentInfoList(borrowRepaymentInfoListCustomize);
            modelAndView.addObject("sumObject", sumObject);

        }
		modelAndView.addObject(BorrowRepaymentInfoListDefine.REPAYMENTINFOLIST_FORM, form);
	}

	/**
	 * 数据导出
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowRepaymentInfoListDefine.EXPORT_ACTION)
	@RequiresPermissions(BorrowRepaymentInfoListDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, BorrowRepaymentInfoListBean form) {
		LogUtil.startLog(BorrowRepaymentInfoListController.class.toString(), BorrowRepaymentInfoListDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "还款详情导出数据";

		BorrowRepaymentInfoListCustomize borrowRepaymentInfoListCustomize = new BorrowRepaymentInfoListCustomize();
		BeanUtils.copyProperties(form, borrowRepaymentInfoListCustomize);

		List<BorrowRepaymentInfoListCustomize> recordList = this.borrowRepaymentInfoListService.selectBorrowRepaymentInfoListList(borrowRepaymentInfoListCustomize);
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		/*---------------upa by LSY START--------------*/
		//String[] titles = new String[] { "项目编号", "借款人ID", "借款人用户名", "项目名称", "项目类型", "借款期限", "出借利率", "借款金额", "借到金额", "还款方式", "还款期数", "出借人用户名", "出借人ID", "出借金额", "应还本金", "应还利息", "应还本息", "管理费", "提前天数", "少还利息", "延期天数", "延期利息", "逾期天数", "逾期利息", "应还总额", "还款订单号", "实还总额", "还款状态", "实际还款日期", "应还日期" };
		String[] titles = new String[] { "项目编号", "资产来源", "借款人ID","借款人用户名", "项目名称", "项目类型", "借款期限", "出借利率", "借款金额", "借到金额", "还款方式", "还款期数", "出借人用户名", "出借人ID", "授权服务金额", "应还本金", "应还利息", "应还本息", "还款服务费", "提前天数", "少还利息", "延期天数", "延期利息", "逾期天数", "逾期利息", "应还总额", "还款订单号", "实还总额", "还款状态", "实际还款日期", "应还日期" };
		/*---------------upa by LSY START--------------*/
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
					BorrowRepaymentInfoListCustomize record = recordList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 项目编号
					if (celLength == 0) {
						cell.setCellValue(record.getBorrowNid());
					}
					// 资产来源
                    if (celLength == 1) {
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
					else if (celLength == 4) {
						cell.setCellValue(record.getBorrowName());
					}
					// 项目类型
					else if (celLength == 5) {
						cell.setCellValue(record.getProjectTypeName());
					}
					// 借款期限
					else if (celLength == 6) {
						cell.setCellValue(record.getBorrowPeriod() + "个月");
					}
					// 出借利率
					else if (celLength == 7) {
						cell.setCellValue(record.getBorrowApr() + "%");
					}
					// 借款金额
					else if (celLength == 8) {
						cell.setCellValue(record.getBorrowAccount().equals("") ? 0 : Double.valueOf(record.getBorrowAccount()));
					}
					// 借到金额
					else if (celLength == 9) {
						cell.setCellValue(record.getBorrowAccountYes().equals("") ? 0 : Double.valueOf(record.getBorrowAccountYes()));
					}
					// 还款方式
					else if (celLength == 10) {
						cell.setCellValue(record.getRepayType());
					}
					// 还款期数
					else if (celLength == 11) {
						cell.setCellValue("第" + record.getRecoverPeriod() + "期");
					}
					// 出借人用户名
					else if (celLength == 12) {
						cell.setCellValue(record.getRecoverUserName());
					}
					// 出借人ID
					else if (celLength == 13) {
						cell.setCellValue(record.getRecoverUserId());
					}
					// 出借金额
					else if (celLength == 14) {
						cell.setCellValue(record.getRecoverTotal().equals("") ? 0 : Double.valueOf(record.getRecoverTotal()));
					}
					// 应还本金
					else if (celLength == 15) {
						cell.setCellValue(record.getRecoverCapital().equals("") ? 0 : Double.valueOf(record.getRecoverCapital()));
					}
					// 应还利息
					else if (celLength == 16) {
						cell.setCellValue(record.getRecoverInterest().equals("") ? 0 : Double.valueOf(record.getRecoverInterest()));
					}
					// 应还本息
					else if (celLength == 17) {
						cell.setCellValue(record.getRecoverAccount().equals("") ? 0 : Double.valueOf(record.getRecoverAccount()));
					}
					// 管理费
					else if (celLength == 18) {
						cell.setCellValue(record.getRecoverFee().equals("") ? 0 : Double.valueOf(record.getRecoverFee()));
					}
					// 提前天数
					else if (celLength == 19) {
						cell.setCellValue(record.getChargeDays());
					}
					// 少还利息
					else if (celLength == 20) {
						cell.setCellValue(StringUtils.isNotBlank(record.getChargeInterest()) ? record.getChargeInterest() : "0");
					}
					// 延期天数
					else if (celLength == 21) {
						cell.setCellValue(record.getDelayDays());
					}
					// 延期利息
					else if (celLength == 22) {
						cell.setCellValue(StringUtils.isNotBlank(record.getDelayInterest()) ? record.getDelayInterest() : "0");
					}
					// 逾期天数
					else if (celLength == 23) {
						cell.setCellValue(record.getLateDays());
					}
					// 逾期利息
					else if (celLength == 24) {
						cell.setCellValue(StringUtils.isNotBlank(record.getLateInterest()) ? record.getLateInterest() : "0");
					}
					// 应还总额
					else if (celLength == 25) {
						cell.setCellValue(StringUtils.isNotBlank(record.getRecoverAccount()) ? record.getRecoverAccount() : "0");
					}
					// 还款订单号
					else if (celLength == 26) {
						cell.setCellValue(record.getNid());
					}
					// 实还总额
					else if (celLength == 27) {
						cell.setCellValue(StringUtils.isNotBlank(record.getRecoverAccountYes()) ? record.getRecoverAccountYes() : "0");
					}
					// 还款状态
					else if (celLength == 28) {
						cell.setCellValue(record.getRepayType());
					}
					// 实际还款日期
					else if (celLength == 29) {
						cell.setCellValue(record.getRecoverActionTime());
					}
					// 应还日期
					else if (celLength == 30) {
						cell.setCellValue(record.getRecoverLastTime());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
		LogUtil.endLog(BorrowRepaymentInfoListController.class.toString(), BorrowRepaymentInfoListDefine.EXPORT_ACTION);
	}
}
