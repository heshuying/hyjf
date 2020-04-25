package com.hyjf.admin.manager.borrow.increaseinterest.repaydetail;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.jpush.api.utils.StringUtils;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.borrow.increaseinterest.repay.IncreaseInterestRepayBean;
import com.hyjf.admin.manager.borrow.increaseinterest.repaydetail.infolist.IncreaseInterestRepayInfoListDefine;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.customize.admin.AdminIncreaseInterestRepayCustomize;

/**
 * 融通宝加息还款明细
 *
 * @ClassName IncreaseInterestRepayDetailController
 * @author liuyang
 * @date 2016年12月29日 上午10:59:04
 */
@Controller
@RequestMapping(value = IncreaseInterestRepayDetailDefine.REQUEST_MAPPING)
public class IncreaseInterestRepayDetailController extends BaseController {

	@Autowired
	private IncreaseInterestRepayDetailService increaseInterestRepayDetailService;

	/**
	 * 画面初始化
	 *
	 * @Title init
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(IncreaseInterestRepayDetailDefine.INIT)
	@RequiresPermissions(IncreaseInterestRepayDetailDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, IncreaseInterestRepayDetailBean form) {
		LogUtil.startLog(IncreaseInterestRepayDetailDefine.THIS_CLASS, IncreaseInterestRepayDetailDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(IncreaseInterestRepayDetailDefine.LIST_PATH);
		// 分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(IncreaseInterestRepayDetailDefine.THIS_CLASS, IncreaseInterestRepayDetailDefine.INIT);
		return modelAndView;
	}

	/**
	 * 融通宝加息还款信息分页
	 *
	 * @Title createPage
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	public void createPage(HttpServletRequest request, ModelAndView modelAndView, IncreaseInterestRepayDetailBean form) {
		int total = this.increaseInterestRepayDetailService.countRecordList(form);
		if (total > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), total);
			List<AdminIncreaseInterestRepayCustomize> recordList = this.increaseInterestRepayDetailService.selectRecordList(form, paginator.getOffset(), paginator.getLimit());
			form.setRecordList(recordList);
			form.setPaginator(paginator);
			/*-----add by LSY START----------*/
			AdminIncreaseInterestRepayCustomize sumBorrowRepay = this.increaseInterestRepayDetailService.sumBorrowRepaymentInfo(form);
			modelAndView.addObject("sumBorrowRepay",sumBorrowRepay);
			/*-----add by LSY END----------*/
		}
		modelAndView.addObject(IncreaseInterestRepayDetailDefine.REPAY_FORM, form);
	}

	/**
	 *
	 * 列表检索Action
	 *
	 * @author liuyang
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(IncreaseInterestRepayDetailDefine.SEARCH_ACTION)
	@RequiresPermissions(IncreaseInterestRepayDetailDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, IncreaseInterestRepayDetailBean form) {
		LogUtil.startLog(IncreaseInterestRepayDetailDefine.THIS_CLASS, IncreaseInterestRepayDetailDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(IncreaseInterestRepayDetailDefine.LIST_PATH);
		// 分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(IncreaseInterestRepayDetailDefine.THIS_CLASS, IncreaseInterestRepayDetailDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 跳转到还款明细详情
	 *
	 * @param request
	 * @param form
	 */
	@RequestMapping(IncreaseInterestRepayDetailDefine.INFO_LIST_ACTION)
	public ModelAndView toRecoverAction(HttpServletRequest request, IncreaseInterestRepayBean form, RedirectAttributes attr) {
		LogUtil.startLog(IncreaseInterestRepayDetailDefine.THIS_CLASS, IncreaseInterestRepayDetailDefine.INFO_LIST_ACTION);
		ModelAndView modelAndView = new ModelAndView(IncreaseInterestRepayInfoListDefine.RE_LIST_PATH);
		attr.addAttribute("borrowNidSrch", form.getBorrowNid());
		attr.addAttribute("userNameSrch", form.getUserName());
		attr.addAttribute("investIdSrch", form.getInvestId());
		// 跳转到还款计划
		LogUtil.endLog(IncreaseInterestRepayDetailDefine.THIS_CLASS, IncreaseInterestRepayDetailDefine.INFO_LIST_ACTION);
		return modelAndView;
	}

	/**
	 * 导出功能
	 *
	 * @param request
	 * @param form
	 */
	@RequestMapping(IncreaseInterestRepayDetailDefine.EXPORT_ACTION)
	@RequiresPermissions(IncreaseInterestRepayDetailDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, IncreaseInterestRepayDetailBean form) throws Exception {
		LogUtil.startLog(IncreaseInterestRepayDetailDefine.THIS_CLASS, IncreaseInterestRepayDetailDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "加息还款明细";

		List<AdminIncreaseInterestRepayCustomize> resultList = increaseInterestRepayDetailService.selectRecordList(form, -1, -1);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
		//序号/项目编号/出借人/借款期限/还款方式/出借利率/加息收益率/应还加息收益/应还时间/转账状态/实际还款时间
		//String[] titles = new String[] { "序号", "项目编号", "借款期限", "还款方式", "出借人用户名", "出借利率", "产品加息收益率", "应还本金", "应还加息收益", "应还时间", "转账状态" };
		String[] titles = new String[] { "序号", "项目编号", "出借人用户名", "借款期限", "还款方式", "出借利率", "加息收益率", "应还加息收益", "应回时间", "转账状态"  , "实际回款时间"};
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
					// 还款方式
					else if (celLength == 4) {
						cell.setCellValue(increaseInterestRepay.getRepayStyleName());
					}

					// 出借利率
					else if (celLength == 5) {
						cell.setCellValue(increaseInterestRepay.getBorrowApr() + "%");
					}
					// 产品加息收益率
					else if (celLength == 6) {
						cell.setCellValue(increaseInterestRepay.getBorrowExtraYield() + "%");
					}
					// 应还加息收益
					else if (celLength == 7) {
						cell.setCellValue(increaseInterestRepay.getRepayInterest().toString());
					}
					// 应还时间
					else if (celLength == 8) {
						cell.setCellValue(increaseInterestRepay.getRepayTime()==null ? "" : GetDate.getDateMyTimeInMillisYYYYMMDD(Integer.valueOf(increaseInterestRepay.getRepayTime())));
					}
					// 转账状态
					else if (celLength == 9) {
						if ("0".equals(increaseInterestRepay.getRepayStatus())) {
							cell.setCellValue("未回款");
						} else if ("1".equals(increaseInterestRepay.getRepayStatus())) {
							cell.setCellValue("已回款");
						}
					}
					// 应还本金（20180731去除）
					/*else if (celLength == 7) {
						cell.setCellValue(increaseInterestRepay.getRepayCapital());
					}*/
					// 实还加息收益（停用）
					/*else if (celLength == 10) {
						cell.setCellValue(increaseInterestRepay.getRepayInterestYes().toString());
					}*/
					// 实际还款时间（20180731新增）
					else if (celLength == 10) {
						cell.setCellValue(increaseInterestRepay.getRepayActionTime()==null ? "" : GetDate.getDateTimeMyTimeInMillis(Integer.valueOf(increaseInterestRepay.getRepayActionTime())));
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(IncreaseInterestRepayDetailDefine.THIS_CLASS, IncreaseInterestRepayDetailDefine.EXPORT_ACTION);
	}

}
