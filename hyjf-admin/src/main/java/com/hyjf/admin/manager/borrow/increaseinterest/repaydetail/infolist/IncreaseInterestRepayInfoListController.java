package com.hyjf.admin.manager.borrow.increaseinterest.repaydetail.infolist;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.admin.AdminIncreaseInterestRepayCustomize;

/**
 * 融通宝加息还款明细详情Controller
 *
 * @ClassName IncreaseInterestRepayInfoListController
 * @author liuyang
 * @date 2017年1月4日 下午5:05:45
 */
@Controller
@RequestMapping(IncreaseInterestRepayInfoListDefine.REQUEST_MAPPING)
public class IncreaseInterestRepayInfoListController extends BaseController {

	@Autowired
	private IncreaseInterestRepayInfoListService increaseInterestRepayInfoListService;

	/**
	 * 画面初始化
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(IncreaseInterestRepayInfoListDefine.INIT)
	public ModelAndView init(HttpServletRequest request, IncreaseInterestRepayInfoListBean form) {
		LogUtil.startLog(IncreaseInterestRepayInfoListDefine.THIS_CLASS, IncreaseInterestRepayInfoListDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(IncreaseInterestRepayInfoListDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(IncreaseInterestRepayInfoListDefine.THIS_CLASS, IncreaseInterestRepayInfoListDefine.INIT);
		return modelAndView;
	}

	/**
	 *
	 * 列表检索Action
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(IncreaseInterestRepayInfoListDefine.SEARCH_ACTION)
	public ModelAndView search(HttpServletRequest request, IncreaseInterestRepayInfoListBean form) {
		LogUtil.startLog(IncreaseInterestRepayInfoListDefine.THIS_CLASS, IncreaseInterestRepayInfoListDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(IncreaseInterestRepayInfoListDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(IncreaseInterestRepayInfoListDefine.THIS_CLASS, IncreaseInterestRepayInfoListDefine.INIT);
		return modelAndView;
	}

	/**
	 * 创建分页技能
	 *
	 * @Title createPage
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, IncreaseInterestRepayInfoListBean form) {
		if (StringUtils.isNotEmpty(form.getStatus())) {
			form.setStatus(null);
		}
		Long count = this.increaseInterestRepayInfoListService.countBorrowRepaymentInfoList(form);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			List<AdminIncreaseInterestRepayCustomize> recordList = this.increaseInterestRepayInfoListService.selectBorrowRepaymentInfoListList(form, paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			/*---add by LSY START--------*/
			AdminIncreaseInterestRepayCustomize sumBorrowRepay = this.increaseInterestRepayInfoListService.sumBorrowLoanmentInfo(form);
			modelAndView.addObject("sumBorrowRepay", sumBorrowRepay);
			/*---add by LSY END--------*/
		}
		modelAndView.addObject(IncreaseInterestRepayInfoListDefine.FORM, form);
	}

	/**
	 * 导出功能
	 *
	 * @param request
	 * @param form
	 */
	@RequestMapping(IncreaseInterestRepayInfoListDefine.EXPORT_ACTION)
	/*@RequiresPermissions(IncreaseInterestRepayInfoListDefine.PERMISSIONS_EXPORT)*/
	public void exportAction(HttpServletRequest request, HttpServletResponse response, IncreaseInterestRepayInfoListBean form) throws Exception {
		LogUtil.startLog(IncreaseInterestRepayInfoListDefine.THIS_CLASS, IncreaseInterestRepayInfoListDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "加息还款明细";

		List<AdminIncreaseInterestRepayCustomize> resultList = increaseInterestRepayInfoListService.selectRecordList(form, -1, -1);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
		//序号/项目编号/出借人/借款期限/还款期数/还款方式/出借利率/加息收益率/应还本金/应还加息收益/应还时间/转账状态/实还加息收益/实际还款时间
		String[] titles = new String[] { "序号", "项目编号", "出借人用户名", "借款期限",  "还款期数", "还款方式", "出借利率", "加息收益率", "出借本金", "应还加息收益", "应回时间", "转账状态","实还加息收益","实际回款时间"};
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
					AdminIncreaseInterestRepayCustomize increaseInterestRepay = resultList.get(i);
					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 项目编号
					else if (celLength == 1) {
						cell.setCellValue(StringUtils.isEmpty(increaseInterestRepay.getBorrowNid()) ? "" : increaseInterestRepay.getBorrowNid());
					}
					// 出借人用户名
					else if (celLength == 2) {
						cell.setCellValue(increaseInterestRepay.getInvestUserName());
					}
					// 借款期限
					else if (celLength == 3) {
						if ("endday".equals(increaseInterestRepay.getBorrowStyle())) {
							cell.setCellValue(increaseInterestRepay.getBorrowPeriod() + "天");
						} else {
							cell.setCellValue(increaseInterestRepay.getBorrowPeriod() + "个月");
						}
					}
					// 还款期数
					else if (celLength == 4) {
						cell.setCellValue("第" + increaseInterestRepay.getRepayPeriod() + "期");
					}
					// 还款方式
					else if (celLength == 5) {
						cell.setCellValue(increaseInterestRepay.getRepayStyleName());
					}

					// 出借利率
					else if (celLength == 6) {
						cell.setCellValue(increaseInterestRepay.getBorrowApr() + "%");
					}
					// 产品加息收益率
					else if (celLength == 7) {
						cell.setCellValue(increaseInterestRepay.getBorrowExtraYield() + "%");
					}
					// 应还本金
					else if (celLength == 8) {
						cell.setCellValue(increaseInterestRepay.getRepayCapital());
					}
					// 应还加息收益
					else if (celLength == 9) {
						cell.setCellValue(increaseInterestRepay.getRepayInterest());
					}
					// 应还时间
					else if (celLength == 10) {
						cell.setCellValue(increaseInterestRepay.getRepayTime()==null ? "" : GetDate.getDateMyTimeInMillis(Integer.valueOf(increaseInterestRepay.getRepayTime())));
					}
					// 转账状态
					else if (celLength == 11) {
						if ("0".equals(increaseInterestRepay.getRepayStatus())) {
							cell.setCellValue("未回款");
						} else if ("1".equals(increaseInterestRepay.getRepayStatus())) {
							cell.setCellValue("已回款");
						}
					}
					//实还加息收益
					else if (celLength == 12) {
						cell.setCellValue(increaseInterestRepay.getRepayInterestYes()==null ? "" : increaseInterestRepay.getRepayInterestYes().toString());
					}
					// 实际还款时间
					else if (celLength == 13) {
						cell.setCellValue(increaseInterestRepay.getRepayActionTime()==null ? "" : GetDate.getDateMyTimeInMillis(Integer.valueOf(increaseInterestRepay.getRepayActionTime())));
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(IncreaseInterestRepayInfoListDefine.THIS_CLASS, IncreaseInterestRepayInfoListDefine.EXPORT_ACTION);
	}

}
