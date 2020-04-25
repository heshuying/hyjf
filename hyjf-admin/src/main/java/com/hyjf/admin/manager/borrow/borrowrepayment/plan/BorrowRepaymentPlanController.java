package com.hyjf.admin.manager.borrow.borrowrepayment.plan;

import com.hyjf.admin.manager.borrow.borrowrepaymentinfo.BorrowRepaymentInfoService;
import com.hyjf.admin.manager.borrow.borrowrepaymentinfo.infolist.BorrowRepaymentInfoListDefine;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.HjhInstConfig;
import com.hyjf.mybatis.model.customize.BorrowRepaymentPlanCustomize;
import org.apache.commons.lang.StringUtils;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = BorrowRepaymentPlanDefine.REQUEST_MAPPING)
public class BorrowRepaymentPlanController {

	@Autowired
	private BorrowRepaymentPlanService borrowRepaymentPlanService;
	@Autowired
    private BorrowRepaymentInfoService borrowRepaymentInfoService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowRepaymentPlanDefine.INIT)
	@RequiresPermissions(BorrowRepaymentPlanDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, BorrowRepaymentPlanBean form) {
		LogUtil.startLog(BorrowRepaymentPlanController.class.toString(), BorrowRepaymentPlanDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(BorrowRepaymentPlanDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BorrowRepaymentPlanController.class.toString(), BorrowRepaymentPlanDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowRepaymentPlanDefine.SEARCH_ACTION)
	@RequiresPermissions(BorrowRepaymentPlanDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, BorrowRepaymentPlanBean form) {
		LogUtil.startLog(BorrowRepaymentPlanController.class.toString(), BorrowRepaymentPlanDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(BorrowRepaymentPlanDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BorrowRepaymentPlanController.class.toString(), BorrowRepaymentPlanDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, BorrowRepaymentPlanBean form) {
		if (form.getStatus() != null && form.getStatus().equals("")) {
			form.setStatus(null);
		}
		BorrowRepaymentPlanCustomize borrowRepaymentPlanCustomize = new BorrowRepaymentPlanCustomize();
		// // 还款状态
		// List<ParamName> repayStatusList =
		// this.borrowRepaymentService.getParamNameList("REPAYMENT_STATUS");
		// modelAndView.addObject("repayStatusList", repayStatusList);
		BeanUtils.copyProperties(form, borrowRepaymentPlanCustomize);
		// 资金来源
        List<HjhInstConfig> hjhInstConfigList = this.borrowRepaymentInfoService.hjhInstConfigList("");
        modelAndView.addObject("hjhInstConfigList", hjhInstConfigList);
		Long count = this.borrowRepaymentPlanService.countBorrowRepaymentPlan(borrowRepaymentPlanCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			borrowRepaymentPlanCustomize.setLimitStart(paginator.getOffset());
			borrowRepaymentPlanCustomize.setLimitEnd(paginator.getLimit());
			List<BorrowRepaymentPlanCustomize> recordList = this.borrowRepaymentPlanService.selectBorrowRepaymentPlanList(borrowRepaymentPlanCustomize);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
			BorrowRepaymentPlanCustomize sumObject = this.borrowRepaymentPlanService.sumBorrowRepaymentPlan(borrowRepaymentPlanCustomize);
			modelAndView.addObject("sumObject", sumObject);
		}
		modelAndView.addObject(BorrowRepaymentPlanDefine.REPAYMENTPLAN_FORM, form);
	}

	/**
	 * 跳转到还款明细
	 * 
	 * @param request
	 * @param form
	 */
	@RequestMapping(BorrowRepaymentPlanDefine.REPAY_PLAN_DETAIL_ACTION)
	@RequiresPermissions(BorrowRepaymentPlanDefine.PERMISSIONS_INFO)
	public ModelAndView toHuankuanjihuaAction(HttpServletRequest request, BorrowRepaymentPlanBean form, RedirectAttributes attr) {
		LogUtil.startLog(BorrowRepaymentPlanController.class.toString(), BorrowRepaymentPlanDefine.REPAY_PLAN_DETAIL_ACTION);
		ModelAndView modelAndView = new ModelAndView(BorrowRepaymentInfoListDefine.RE_LIST_PATH);
		attr.addAttribute("borrowNid", form.getBorrowNid());
		attr.addAttribute("recoverPeriod", form.getRepayPeriod());// 此处的repayPeriod就等于目的地的recoverPeriod
		attr.addAttribute("isMonth","true");
		attr.addAttribute(BorrowRepaymentInfoListDefine.ACTFROM, BorrowRepaymentInfoListDefine.ACTFROMPLAN);
		// infoForm.setbo(form.getBorrowNid());
		// modelAndView.addObject("form", infoForm);
		// 跳转到还款计划
		LogUtil.endLog(BorrowRepaymentPlanController.class.toString(), BorrowRepaymentPlanDefine.REPAY_PLAN_DETAIL_ACTION);
		return modelAndView;
	}

	/**
	 * 数据导出
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowRepaymentPlanDefine.EXPORT_ACTION)
	@RequiresPermissions(BorrowRepaymentPlanDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, BorrowRepaymentPlanBean form) {
		LogUtil.startLog(BorrowRepaymentPlanController.class.toString(), BorrowRepaymentPlanDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "还款计划导出数据";

		BorrowRepaymentPlanCustomize borrowRepaymentPlanCustomize = new BorrowRepaymentPlanCustomize();
		BeanUtils.copyProperties(form, borrowRepaymentPlanCustomize);

		List<BorrowRepaymentPlanCustomize> recordList = this.borrowRepaymentPlanService.selectBorrowRepaymentPlanList(borrowRepaymentPlanCustomize);
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		/*---------------upd by LSY START-------------*/
		//String[] titles = new String[] { "项目编号", "借款人ID", "借款人用户名", "项目名称", "项目类型", "借款期限", "出借利率", "借款金额", "借到金额", "还款方式", "还款期数", "应还本金", "应还利息", "应还本息", "应收管理费", "提前天数", "少还利息", "延期天数", "延期利息", "逾期天数", "逾期利息", "应还总额", "实还总额", "还款状态", "实际还款日期", "应还日期","还款来源" };
		String[] titles = new String[] { "项目编号","资产来源" , "借款人ID", "借款人用户名", "项目类型", "借款期限", "出借利率", "借款金额", "借到金额", "还款方式", "还款期数", "应还本金", "应还利息", "应还本息", "还款服务费", "提前天数", "少还利息", "延期天数", "延期利息", "逾期天数", "逾期利息", "应还总额", "实还总额", "还款状态", "实际还款日期", "应还日期","还款来源","剩余待还本金","剩余待还利息"};
		/*---------------upd by LSY END-------------*/
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
					BorrowRepaymentPlanCustomize record = recordList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 项目编号
					if (celLength == 0) {
						cell.setCellValue(record.getBorrowNid());
					}
					// 资产来源
                    else if(celLength == 1) {
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
					//else if (celLength == 4) {
					//	cell.setCellValue(record.getBorrowName());
					//}
					// 项目类型
					else if (celLength == 4) {
						cell.setCellValue(record.getProjectTypeName());
					}
					// 借款期限
					else if (celLength == 5) {
						cell.setCellValue(record.getBorrowPeriod() + "个月");
					}
					// 出借利率
					else if (celLength == 6) {
						cell.setCellValue(record.getBorrowApr() + "%");
					}
					// 借款金额
					else if (celLength == 7) {
						cell.setCellValue(record.getBorrowAccount().equals("") ? 0 : Double.valueOf(record.getBorrowAccount()));
					}
					// 借到金额
					else if (celLength == 8) {
						cell.setCellValue(record.getBorrowAccountYes().equals("") ? 0 : Double.valueOf(record.getBorrowAccountYes()));
					}
					// 还款方式
					else if (celLength == 9) {
						cell.setCellValue(record.getRepayType());
					}
					// 还款期数
					else if (celLength == 10) {
						cell.setCellValue("第" + record.getRepayPeriod() + "期");
					}
					// 应还本金
					else if (celLength == 11) {
						cell.setCellValue(record.getRepayCapital().equals("") ? 0 : Double.valueOf(record.getRepayCapital()));
					}
					// 应还利息
					else if (celLength == 12) {
						cell.setCellValue(record.getRepayInterest().equals("") ? 0 : Double.valueOf(record.getRepayInterest()));
					}
					// 应还本息
					else if (celLength == 13) {
						cell.setCellValue(record.getRepayAccount().equals("") ? 0 : Double.valueOf(record.getRepayAccount()));
					}
					// 应收管理费
					else if (celLength == 14) {
						cell.setCellValue(record.getRepayFee().equals("") ? 0 : Double.valueOf(record.getRepayFee()));
					}
					// 提前天数
					else if (celLength == 15) {
						cell.setCellValue(record.getTiqiantianshu());
					}
					// 少还利息
					else if (celLength == 16) {
						cell.setCellValue(record.getShaohuanlixi().equals("") ? 0 : Double.valueOf(record.getShaohuanlixi()));
					}
					// 延期天数
					else if (celLength == 17) {
						cell.setCellValue(record.getYanqitianshu());
					}
					// 延期利息
					else if (celLength == 18) {
						cell.setCellValue(record.getYanqilixi().equals("") ? 0 : Double.valueOf(record.getYanqilixi()));
					}
					// 逾期天数
					else if (celLength == 19) {
						cell.setCellValue(record.getYuqitianshu());
					}
					// 逾期利息
					else if (celLength == 20) {
						cell.setCellValue(record.getYuqilixi().equals("") ? 0 : Double.valueOf(record.getYuqilixi()));
					}
					// 应还总额
					else if (celLength == 21) {
						cell.setCellValue(record.getYinghuanzonge().equals("") ? 0 : Double.valueOf(record.getYinghuanzonge()));
					}
					// 实还总额
					else if (celLength == 22) {
						cell.setCellValue(record.getShihuanzonge().equals("") ? 0 : Double.valueOf(record.getShihuanzonge()));
					}
					// 还款状态
					else if (celLength == 23) {
						if (StringUtils.isNotEmpty(record.getStatus())) {
							cell.setCellValue(record.getStatus().equals("0") ? "还款中" : "已还款");
						}
					}
					// 实际还款日
					else if (celLength == 24) {
						cell.setCellValue(record.getRepayActionTime());
					}
					// 应还日期
					else if (celLength == 25) {
						cell.setCellValue(record.getRepayLastTime());
					}
					// 还款来源
					else if(celLength == 26) {
						cell.setCellValue(record.getRepayMoneySource());
					}
					// 剩余待还本金
					else if (celLength == 27){
						cell.setCellValue(record.getRepayAccountCapitalWait());
					}
					// 剩余待还利息
					else if (celLength == 28){
						cell.setCellValue(record.getRepayAccountInterestWait());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(BorrowRepaymentPlanController.class.toString(), BorrowRepaymentPlanDefine.EXPORT_ACTION);
	}
}
