package com.hyjf.admin.manager.borrow.increaseinterest.repayplan;

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
import com.hyjf.admin.manager.borrow.borrowrepaymentinfo.infolist.BorrowRepaymentInfoListDefine;
import com.hyjf.admin.manager.borrow.increaseinterest.repay.IncreaseInterestRepayDefine;
import com.hyjf.admin.manager.borrow.increaseinterest.repaydetail.IncreaseInterestRepayDetailDefine;
import com.hyjf.admin.manager.borrow.increaseinterest.repaydetail.infolist.IncreaseInterestRepayInfoListDefine;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.IncreaseInterestRepayDetail;

/**
 * 融通宝加息还款计划Controller
 *
 * @ClassName IncreaseInterestRepayPlanController
 * @author liuyang
 * @date 2016年12月29日 上午9:02:52
 */
@Controller
@RequestMapping(value = IncreaseInterestRepayPlanDefine.REQUEST_MAPPING)
public class IncreaseInterestRepayPlanController extends BaseController {

	@Autowired
	private IncreaseInterestRepayPlanService increaseInterestRepayPlanService;

	/**
	 * 画面初始化
	 *
	 * @Title init
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(IncreaseInterestRepayPlanDefine.INIT)
	@RequiresPermissions(IncreaseInterestRepayPlanDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, IncreaseInterestRepayPlanBean form) {
		LogUtil.startLog(IncreaseInterestRepayPlanDefine.THIS_CLASS, IncreaseInterestRepayPlanDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(IncreaseInterestRepayPlanDefine.LIST_PATH);
		// 分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(IncreaseInterestRepayPlanDefine.THIS_CLASS, IncreaseInterestRepayPlanDefine.INIT);
		return modelAndView;
	}

	/**
	 * 融通宝加息还款计划分页
	 *
	 * @Title createPage
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	public void createPage(HttpServletRequest request, ModelAndView modelAndView, IncreaseInterestRepayPlanBean form) {
		int total = this.increaseInterestRepayPlanService.countRecordList(form);
		if (total > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), total);
			List<IncreaseInterestRepayDetail> recordList = this.increaseInterestRepayPlanService.selectRecordList(form, paginator.getOffset(), paginator.getLimit());
			form.setRecordList(recordList);
			form.setPaginator(paginator);
		}
		modelAndView.addObject(IncreaseInterestRepayPlanDefine.REPAY_FORM, form);
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
	@RequestMapping(IncreaseInterestRepayPlanDefine.SEARCH_ACTION)
	@RequiresPermissions(IncreaseInterestRepayPlanDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, IncreaseInterestRepayPlanBean form) {
		LogUtil.startLog(IncreaseInterestRepayPlanDefine.THIS_CLASS, IncreaseInterestRepayPlanDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(IncreaseInterestRepayPlanDefine.LIST_PATH);
		// 分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(IncreaseInterestRepayPlanDefine.THIS_CLASS, IncreaseInterestRepayPlanDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 跳转到还款详情画面
	 *
	 * @Title repayPlanDetailAction
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(IncreaseInterestRepayPlanDefine.REPAY_PLAN_INFO_ACTION)
	public ModelAndView repayPlanDetailAction(HttpServletRequest request, IncreaseInterestRepayPlanBean form, RedirectAttributes attr) {
		LogUtil.startLog(IncreaseInterestRepayPlanDefine.THIS_CLASS, IncreaseInterestRepayPlanDefine.REPAY_PLAN_INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(IncreaseInterestRepayInfoListDefine.RE_LIST_PATH);
		attr.addAttribute("borrowNidSrch", form.getBorrowNid());
		attr.addAttribute("repayPeriodSrch", form.getRepayPeriod());
		LogUtil.endLog(IncreaseInterestRepayPlanDefine.THIS_CLASS, IncreaseInterestRepayPlanDefine.REPAY_PLAN_INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 跳转到还款明细
	 *
	 * @param request
	 * @param form
	 */
	@RequestMapping(IncreaseInterestRepayPlanDefine.REPAY_DETAIL_ACTION)
	@RequiresPermissions(IncreaseInterestRepayPlanDefine.PERMISSIONS_INFO)
	public ModelAndView toHuankuanjihuaAction(HttpServletRequest request, IncreaseInterestRepayPlanBean form, RedirectAttributes attr) {
		LogUtil.startLog(IncreaseInterestRepayPlanDefine.THIS_CLASS, IncreaseInterestRepayPlanDefine.REPAY_DETAIL_ACTION);
		ModelAndView modelAndView = new ModelAndView(IncreaseInterestRepayDefine.RE_LIST_PATH);
		attr.addAttribute("borrowNid", form.getBorrowNid());
		attr.addAttribute("repayPeriod", form.getRepayPeriod());// 此处的repayPeriod就等于目的地的recoverPeriod
		attr.addAttribute(IncreaseInterestRepayDetailDefine.REPAY_FORM, BorrowRepaymentInfoListDefine.ACTFROMPLAN);
		// 跳转到还款计划
		LogUtil.endLog(IncreaseInterestRepayPlanDefine.THIS_CLASS, IncreaseInterestRepayPlanDefine.REPAY_DETAIL_ACTION);
		return modelAndView;
	}

	/**
	 * 导出功能
	 *
	 * @param request
	 * @param form
	 */
	@RequestMapping(IncreaseInterestRepayPlanDefine.EXPORT_ACTION)
	@RequiresPermissions(IncreaseInterestRepayPlanDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, IncreaseInterestRepayPlanBean form) throws Exception {
		LogUtil.startLog(IncreaseInterestRepayPlanDefine.THIS_CLASS, IncreaseInterestRepayPlanDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "加息还款计划";

		List<IncreaseInterestRepayDetail> resultList = increaseInterestRepayPlanService.selectRecordList(form, -1, -1);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
		//序号/项目编号/借款人/借款期限/还款期数/还款方式/出借利率/加息收益率/应还加息收益/应还时间/转账状态/实际还款时间
		String[] titles = new String[] { "序号", "项目编号", "借款人" ,"借款期限", "还款期数", "还款方式","出借利率", "加息收益率", "应还加息收益", "应还时间", "转账状态", "实际还款时间" };
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
					IncreaseInterestRepayDetail increaseInterestRepayDetail = resultList.get(i);
					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 项目编号
					else if (celLength == 1) {
						cell.setCellValue(StringUtils.isEmpty(increaseInterestRepayDetail.getBorrowNid()) ? "" : increaseInterestRepayDetail.getBorrowNid());
					}
					// 借款人
					else if (celLength == 2) {
						cell.setCellValue(StringUtils.isEmpty(increaseInterestRepayDetail.getUserName()) ? "" : increaseInterestRepayDetail.getUserName());
					}
					// 借款期限
					else if (celLength == 3) {
						if ("endday".equals(increaseInterestRepayDetail.getBorrowStyle())) {
							cell.setCellValue(increaseInterestRepayDetail.getBorrowPeriod() + "天");
						} else {
							cell.setCellValue(increaseInterestRepayDetail.getBorrowPeriod() + "个月");
						}
					}
					// 还款期数
					else if (celLength == 4) {
						cell.setCellValue("第" + increaseInterestRepayDetail.getRepayPeriod() + "期");
					}
					// 还款方式
					else if (celLength == 5) {
						cell.setCellValue(increaseInterestRepayDetail.getBorrowStyleName());
					}

					// 出借利率
					else if (celLength == 6) {
						cell.setCellValue(increaseInterestRepayDetail.getBorrowApr() + "%");
					}
					// 产品加息收益率
					else if (celLength == 7) {
						cell.setCellValue(increaseInterestRepayDetail.getBorrowExtraYield() + "%");
					}
					// 应还加息收益
					else if (celLength == 8) {
						cell.setCellValue(increaseInterestRepayDetail.getRepayInterest()==null ? "" : increaseInterestRepayDetail.getRepayInterest().toString());
					}
					// 应还时间
					else if (celLength == 9) {
						cell.setCellValue(increaseInterestRepayDetail.getRepayTime()==null ? "" : GetDate.getDateMyTimeInMillis(Integer.parseInt(increaseInterestRepayDetail.getRepayTime())));
					}
					// 转账状态
					else if (celLength == 10) {
						if (increaseInterestRepayDetail.getRepayStatus() == 0) {
							cell.setCellValue("未回款");
						} else if (increaseInterestRepayDetail.getRepayStatus() == 1) {
							cell.setCellValue("已回款");
						}
					}
					// 实际还款时间
					else if (celLength == 11) {
						cell.setCellValue(increaseInterestRepayDetail.getRepayActionTime()==null ? "" :  GetDate.getDateMyTimeInMillis(Integer.parseInt(increaseInterestRepayDetail.getRepayActionTime())));
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(IncreaseInterestRepayPlanDefine.THIS_CLASS, IncreaseInterestRepayPlanDefine.EXPORT_ACTION);
	}
}
