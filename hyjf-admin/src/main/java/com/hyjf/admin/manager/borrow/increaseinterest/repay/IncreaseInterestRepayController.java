package com.hyjf.admin.manager.borrow.increaseinterest.repay;

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
import com.hyjf.admin.manager.borrow.increaseinterest.repaydetail.IncreaseInterestRepayDetailDefine;
import com.hyjf.admin.manager.borrow.increaseinterest.repayplan.IncreaseInterestRepayPlanDefine;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.IncreaseInterestRepay;

/**
 * 融通宝加息还款信息Controller
 *
 * @ClassName IncreaseInterestRepayController
 * @author liuyang
 * @date 2016年12月28日 下午4:16:33
 */
@Controller
@RequestMapping(value = IncreaseInterestRepayDefine.REQUEST_MAPPING)
public class IncreaseInterestRepayController extends BaseController {

	@Autowired
	private IncreaseInterestRepayService increaseInterestRepayService;

	/**
	 * 画面初始化
	 *
	 * @Title init
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(IncreaseInterestRepayDefine.INIT)
	@RequiresPermissions(IncreaseInterestRepayDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, IncreaseInterestRepayBean form) {
		LogUtil.startLog(IncreaseInterestRepayDefine.THIS_CLASS, IncreaseInterestRepayDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(IncreaseInterestRepayDefine.LIST_PATH);
		// 分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(IncreaseInterestRepayDefine.THIS_CLASS, IncreaseInterestRepayDefine.INIT);
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
	public void createPage(HttpServletRequest request, ModelAndView modelAndView, IncreaseInterestRepayBean form) {
		int total = this.increaseInterestRepayService.countRecordList(form);
		if (total > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), total);
			List<IncreaseInterestRepay> recordList = this.increaseInterestRepayService.selectRecordList(form, paginator.getOffset(), paginator.getLimit());
			form.setRecordList(recordList);
			form.setPaginator(paginator);
			/*-------add by LSY START------------------*/
			String sumAccount = this.increaseInterestRepayService.sumAccount(form);
			modelAndView.addObject("sumAccount", sumAccount);
			/*-------add by LSY END--------------------*/
		}
		modelAndView.addObject(IncreaseInterestRepayDefine.REPAY_FORM, form);
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
	@RequestMapping(IncreaseInterestRepayDefine.SEARCH_ACTION)
	@RequiresPermissions(IncreaseInterestRepayDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, IncreaseInterestRepayBean form) {
		LogUtil.startLog(IncreaseInterestRepayDefine.THIS_CLASS, IncreaseInterestRepayDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(IncreaseInterestRepayDefine.LIST_PATH);
		// 分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(IncreaseInterestRepayDefine.THIS_CLASS, IncreaseInterestRepayDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 跳转到还款计划
	 *
	 * @param request
	 * @param form
	 */
	@RequestMapping(IncreaseInterestRepayDefine.REPAY_PLAN_ACTION)
	public ModelAndView replayPlanAction(HttpServletRequest request, IncreaseInterestRepayBean form, RedirectAttributes attr) {
		LogUtil.startLog(IncreaseInterestRepayDefine.THIS_CLASS, IncreaseInterestRepayDefine.REPAY_PLAN_ACTION);
		ModelAndView modelAndView = new ModelAndView(IncreaseInterestRepayPlanDefine.RE_LIST_PATH);
		attr.addAttribute("borrowNidSrch", form.getBorrowNid());
		// 跳转到还款计划
		LogUtil.endLog(IncreaseInterestRepayDefine.THIS_CLASS, IncreaseInterestRepayDefine.REPAY_PLAN_ACTION);
		return modelAndView;
	}

	/**
	 * 跳转到还款明细
	 *
	 * @param request
	 * @param form
	 */
	@RequestMapping(IncreaseInterestRepayDefine.TO_RECOVER_ACTION)
	public ModelAndView toRecoverAction(HttpServletRequest request, IncreaseInterestRepayBean form, RedirectAttributes attr) {
		LogUtil.startLog(IncreaseInterestRepayDefine.THIS_CLASS, IncreaseInterestRepayDefine.TO_RECOVER_ACTION);
		ModelAndView modelAndView = new ModelAndView(IncreaseInterestRepayDetailDefine.RE_LIST_PATH);
		attr.addAttribute("borrowNidSrch", form.getBorrowNid());
		// 跳转到还款计划
		LogUtil.endLog(IncreaseInterestRepayDefine.THIS_CLASS, IncreaseInterestRepayDefine.TO_RECOVER_ACTION);
		return modelAndView;
	}

	/**
	 * 导出功能
	 *
	 * @param request
	 * @param form
	 */
	@RequestMapping(IncreaseInterestRepayDefine.EXPORT_ACTION)
	@RequiresPermissions(IncreaseInterestRepayDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, IncreaseInterestRepayBean form) throws Exception {
		LogUtil.startLog(IncreaseInterestRepayDefine.THIS_CLASS, IncreaseInterestRepayDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "加息还款信息";

		List<IncreaseInterestRepay> resultList = increaseInterestRepayService.selectRecordList(form, -1, -1);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
		//20180730导出增加字段：借款金额/放款时间
		//序号/项目编号/借款人/项目期限/借款金额/还款方式/出借利率/加息收益率/应加息收益/应还日期/转账状态/放款时间/实际还款时间
		String[] titles = new String[] { "序号","项目编号", "借款人用户名" , "项目期限","借款金额", "还款方式", "出借利率", "加息收益率", "应还加息收益", "应还日期", "转账状态","放款时间","实际还款时间" };
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
					IncreaseInterestRepay increaseInterestRepay = resultList.get(i);
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
					// 借款人
					else if (celLength == 2) {
						cell.setCellValue(StringUtils.isEmpty(increaseInterestRepay.getUserName()) ? "" : increaseInterestRepay.getUserName());
					}
					// 借款期限
					else if (celLength == 3) {
						if ("endday".equals(increaseInterestRepay.getBorrowStyle())) {
							cell.setCellValue(increaseInterestRepay.getBorrowPeriod() + "天");
						} else {
							cell.setCellValue(increaseInterestRepay.getBorrowPeriod() + "个月");
						}
					}
					// 借款金额
					else if (celLength == 4) {
						cell.setCellValue(increaseInterestRepay.getBorrowAccount()==null ? "" : increaseInterestRepay.getBorrowAccount().toString());
					}
					// 还款方式
					else if (celLength == 5) {
						cell.setCellValue(increaseInterestRepay.getBorrowStyleName());
					}
					// 出借利率
					else if (celLength == 6) {
						cell.setCellValue(increaseInterestRepay.getBorrowApr() + "%");
					}
					// 产品加息收益率
					else if (celLength == 7) {
						cell.setCellValue(increaseInterestRepay.getBorrowExtraYield() + "%");
					}
					// 应还加息收益
					else if (celLength == 8) {
						cell.setCellValue(increaseInterestRepay.getRepayInterest().toString());
					}
					// 应还时间
					else if (celLength == 9) {
						cell.setCellValue(StringUtils.isEmpty(increaseInterestRepay.getRepayTime()) ? "" : GetDate.getDateMyTimeInMillis(Integer.parseInt(increaseInterestRepay.getRepayTime())));
					}
					// 转账状态
					else if (celLength == 10) {
						if (increaseInterestRepay.getRepayStatus() == 0) {
							cell.setCellValue("未转账");
						} else if (increaseInterestRepay.getRepayStatus() == 1) {
							cell.setCellValue("已转账");
						}
					}
					// 放款时间
					else if (celLength == 11) {
						cell.setCellValue(increaseInterestRepay.getLoanActionTime()==null  ? "" : GetDate.getDateMyTimeInMillis(increaseInterestRepay.getLoanActionTime()));
					}
					// 实际还款时间
					else if (celLength == 12) {
						cell.setCellValue(increaseInterestRepay.getRepayActionTime()==null ? "" : GetDate.getDateTimeMyTimeInMillis(Integer.parseInt(increaseInterestRepay.getRepayActionTime())));
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(IncreaseInterestRepayDefine.THIS_CLASS, IncreaseInterestRepayDefine.EXPORT_ACTION);
	}

}
