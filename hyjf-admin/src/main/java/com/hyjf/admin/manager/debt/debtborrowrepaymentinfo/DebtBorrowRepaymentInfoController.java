package com.hyjf.admin.manager.debt.debtborrowrepaymentinfo;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.debt.debtborrowrepaymentinfo.debtinfolist.DebtBorrowRepaymentInfoListDefine;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowRepaymentInfoCustomize;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = DebtBorrowRepaymentInfoDefine.REQUEST_MAPPING)
public class DebtBorrowRepaymentInfoController extends BaseController {

	@Autowired
	private DebtBorrowRepaymentInfoService borrowRepaymentInfoService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(DebtBorrowRepaymentInfoDefine.INIT)
	@RequiresPermissions(DebtBorrowRepaymentInfoDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, DebtBorrowRepaymentInfoBean form) {
		LogUtil.startLog(DebtBorrowRepaymentInfoController.class.toString(), DebtBorrowRepaymentInfoDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(DebtBorrowRepaymentInfoDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(DebtBorrowRepaymentInfoController.class.toString(), DebtBorrowRepaymentInfoDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(DebtBorrowRepaymentInfoDefine.SEARCH_ACTION)
	@RequiresPermissions(DebtBorrowRepaymentInfoDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, DebtBorrowRepaymentInfoBean form) {
		LogUtil.startLog(DebtBorrowRepaymentInfoController.class.toString(), DebtBorrowRepaymentInfoDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(DebtBorrowRepaymentInfoDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(DebtBorrowRepaymentInfoController.class.toString(), DebtBorrowRepaymentInfoDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, DebtBorrowRepaymentInfoBean form) {

		DebtBorrowRepaymentInfoCustomize borrowRepaymentInfoCustomize = new DebtBorrowRepaymentInfoCustomize();
		BeanUtils.copyProperties(form, borrowRepaymentInfoCustomize);
		Long count = this.borrowRepaymentInfoService.countBorrowRepaymentInfo(borrowRepaymentInfoCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			borrowRepaymentInfoCustomize.setLimitStart(paginator.getOffset());
			borrowRepaymentInfoCustomize.setLimitEnd(paginator.getLimit());
			List<DebtBorrowRepaymentInfoCustomize> recordList = this.borrowRepaymentInfoService.selectBorrowRepaymentInfoList(borrowRepaymentInfoCustomize);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
			DebtBorrowRepaymentInfoCustomize sumObject = this.borrowRepaymentInfoService.sumBorrowRepaymentInfo(borrowRepaymentInfoCustomize);
			modelAndView.addObject("sumObject", sumObject);
		}
		modelAndView.addObject(DebtBorrowRepaymentInfoDefine.REPAYMENTINFO_FORM, form);
	}

	/**
	 * 跳转到详细列表
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(DebtBorrowRepaymentInfoDefine.TO_XIANGQING_ACTION)
	@RequiresPermissions(DebtBorrowRepaymentInfoDefine.PERMISSIONS_INFO)
	public ModelAndView toSearchInfoListAction(HttpServletRequest request, DebtBorrowRepaymentInfoBean form, RedirectAttributes attr) {
		LogUtil.startLog(DebtBorrowRepaymentInfoController.class.toString(), DebtBorrowRepaymentInfoDefine.TO_XIANGQING_ACTION);
		ModelAndView modelAndView = new ModelAndView(DebtBorrowRepaymentInfoListDefine.RE_LIST_PATH);
		attr.addAttribute("borrowNid", form.getBorrowNid());
		attr.addAttribute("nid", form.getNid());
		attr.addAttribute(DebtBorrowRepaymentInfoListDefine.ACTFROM, DebtBorrowRepaymentInfoListDefine.ACTFROMINFO);

		LogUtil.endLog(DebtBorrowRepaymentInfoController.class.toString(), DebtBorrowRepaymentInfoDefine.TO_XIANGQING_ACTION);
		return modelAndView;
	}

	/**
	 * 数据导出
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(DebtBorrowRepaymentInfoDefine.EXPORT_ACTION)
	@RequiresPermissions(DebtBorrowRepaymentInfoDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, DebtBorrowRepaymentInfoBean form) {
		LogUtil.startLog(DebtBorrowRepaymentInfoController.class.toString(), DebtBorrowRepaymentInfoDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "还款明细导出数据";

		DebtBorrowRepaymentInfoCustomize borrowRepaymentInfoCustomize = new DebtBorrowRepaymentInfoCustomize();
		BeanUtils.copyProperties(form, borrowRepaymentInfoCustomize);

		List<DebtBorrowRepaymentInfoCustomize> recordList = this.borrowRepaymentInfoService.selectBorrowRepaymentInfoList(borrowRepaymentInfoCustomize);
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "项目编号", "借款人ID", "借款人用户名", "项目名称", "项目类型", "借款期限", "出借利率", "借款金额", "借到金额", "还款方式", "出借人用户名", "出借人ID", "出借人用户属性（当前）", "出借人所属一级分部（当前）", "出借人所属二级分部（当前）", "出借人所属团队（当前）", "推荐人用户名（当前）", "推荐人姓名（当前）", "推荐人所属一级分部（当前）", "推荐人所属二级分部（当前）", "推荐人所属团队（当前）", "出借金额", "应还本金",
				"应还利息", "应还本息", "管理费", "已还本金", "已还利息", "已还本息", "未还本金", "未还利息", "未还本息", "还款状态", "最后还款日" };
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
					DebtBorrowRepaymentInfoCustomize record = recordList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					// 项目编号
					if (celLength == 0) {
						cell.setCellValue(record.getBorrowNid());
					}
					// 借款人ID
					else if (celLength == 1) {
						cell.setCellValue(record.getUserId());
					}
					// 借款人用户名
					else if (celLength == 2) {
						cell.setCellValue(record.getBorrowUserName());
					}
					// 项目名称
					else if (celLength == 3) {
						cell.setCellValue(record.getBorrowName());
					}
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
					// 出借人用户名
					else if (celLength == 10) {
						cell.setCellValue(record.getRecoverUserName());
					}
					// 出借人ID
					else if (celLength == 11) {
						cell.setCellValue(record.getRecoverUserId());
					}
					// 出借人用户属性（当前）
					else if (celLength == 12) {
						if ("0".equals(record.getRecoverUserAttribute())) {
							cell.setCellValue("无主单");
						} else if ("1".equals(record.getRecoverUserAttribute())) {
							cell.setCellValue("有主单");
						} else if ("2".equals(record.getRecoverUserAttribute())) {
							cell.setCellValue("线下员工");
						} else if ("3".equals(record.getRecoverUserAttribute())) {
							cell.setCellValue("线上员工");
						}
					}
					// 出借人所属一级分部（当前）
					else if (celLength == 13) {
						cell.setCellValue(record.getRecoverRegionName());
					}
					// 出借人所属二级分部（当前）
					else if (celLength == 14) {
						cell.setCellValue(record.getRecoverBranchName());
					}
					// 出借人所属团队（当前）
					else if (celLength == 15) {
						cell.setCellValue(record.getRecoverDepartmentName());
					}
					// 推荐人用户名（当前）
					else if (celLength == 16) {
						cell.setCellValue(record.getReferrerName());
					}
					// 推荐人姓名（当前）
					else if (celLength == 17) {
						cell.setCellValue(record.getReferrerTrueName());
					}
					// 推荐人所属一级分部（当前）
					else if (celLength == 18) {
						cell.setCellValue(record.getReferrerRegionName());
					}
					// 推荐人所属二级分部（当前）
					else if (celLength == 19) {
						cell.setCellValue(record.getReferrerBranchName());
					}
					// 推荐人所属团队（当前）
					else if (celLength == 20) {
						cell.setCellValue(record.getReferrerDepartmentName());
					}
					// 出借金额
					else if (celLength == 21) {
						cell.setCellValue(record.getRecoverTotal().equals("") ? 0 : Double.valueOf(record.getRecoverTotal()));
					}
					// 应还本金
					else if (celLength == 22) {
						cell.setCellValue(record.getRecoverCapital().equals("") ? 0 : Double.valueOf(record.getRecoverCapital()));
					}
					// 应还利息
					else if (celLength == 23) {
						cell.setCellValue(record.getRecoverInterest().equals("") ? 0 : Double.valueOf(record.getRecoverInterest()));
					}
					// 应还本息
					else if (celLength == 24) {
						cell.setCellValue(record.getRecoverAccount().equals("") ? 0 : Double.valueOf(record.getRecoverAccount()));
					}
					// 管理费
					else if (celLength == 25) {
						cell.setCellValue(record.getRecoverFee().equals("") ? 0 : Double.valueOf(record.getRecoverFee()));
					}
					// 已还本金
					else if (celLength == 26) {
						cell.setCellValue(record.getRecoverCapitalYes().equals("") ? 0 : Double.valueOf(record.getRecoverCapitalYes()));
					}
					// 已还利息
					else if (celLength == 27) {
						cell.setCellValue(record.getRecoverInterestYes().equals("") ? 0 : Double.valueOf(record.getRecoverInterestYes()));
					}
					// 已还本息
					else if (celLength == 28) {
						cell.setCellValue(record.getRecoverAccountYes().equals("") ? 0 : Double.valueOf(record.getRecoverAccountYes()));
					}
					// 未还本金
					else if (celLength == 29) {
						cell.setCellValue(record.getRecoverCapitalWait().equals("") ? 0 : Double.valueOf(record.getRecoverCapitalWait()));
					}
					// 未还利息
					else if (celLength == 30) {
						cell.setCellValue(record.getRecoverInterestWait().equals("") ? 0 : Double.valueOf(record.getRecoverInterestWait()));
					}
					// 未还本息
					else if (celLength == 31) {
						cell.setCellValue(record.getRecoverAccountWait().equals("") ? 0 : Double.valueOf(record.getRecoverAccountWait()));
					}
					// 还款状态
					else if (celLength == 32) {
						if (StringUtils.isNotEmpty(record.getStatus())) {
							cell.setCellValue(record.getStatus().equals("0") ? "还款中" : "已还款");
						}
					}
					// 最后还款日
					else if (celLength == 33) {
						cell.setCellValue(record.getRecoverLastTime());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
		LogUtil.endLog(DebtBorrowRepaymentInfoController.class.toString(), DebtBorrowRepaymentInfoDefine.EXPORT_ACTION);
	}
}
