package com.hyjf.admin.manager.borrow.borrowrepaymentdetails;

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
import org.springframework.beans.BeanUtils;
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
import com.hyjf.mybatis.model.customize.BorrowRepaymentDetailsCustomize;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = BorrowRepaymentDetailsDefine.REQUEST_MAPPING)
public class BorrowRepaymentDetailsController extends BaseController {

	@Autowired
	private BorrowRepaymentDetailsService borrowRepaymentDetailsService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowRepaymentDetailsDefine.INIT)
	@RequiresPermissions(BorrowRepaymentDetailsDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, BorrowRepaymentDetailsBean form) {
		LogUtil.startLog(BorrowRepaymentDetailsController.class.toString(), BorrowRepaymentDetailsDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(BorrowRepaymentDetailsDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BorrowRepaymentDetailsController.class.toString(), BorrowRepaymentDetailsDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowRepaymentDetailsDefine.SEARCH_ACTION)
	@RequiresPermissions(BorrowRepaymentDetailsDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, BorrowRepaymentDetailsBean form) {
		LogUtil.startLog(BorrowRepaymentDetailsController.class.toString(), BorrowRepaymentDetailsDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(BorrowRepaymentDetailsDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BorrowRepaymentDetailsController.class.toString(), BorrowRepaymentDetailsDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, BorrowRepaymentDetailsBean form) {

		BorrowRepaymentDetailsCustomize BorrowRepaymentDetailsCustomize = new BorrowRepaymentDetailsCustomize();
		BeanUtils.copyProperties(form, BorrowRepaymentDetailsCustomize);

		Long count = this.borrowRepaymentDetailsService.countBorrowRepaymentDetails(BorrowRepaymentDetailsCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			BorrowRepaymentDetailsCustomize.setLimitStart(paginator.getOffset());
			BorrowRepaymentDetailsCustomize.setLimitEnd(paginator.getLimit());
			List<BorrowRepaymentDetailsCustomize> recordList = this.borrowRepaymentDetailsService.selectBorrowRepaymentDetailsList(BorrowRepaymentDetailsCustomize);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
			BorrowRepaymentDetailsCustomize sumObject = this.borrowRepaymentDetailsService.sumBorrowRepaymentDetails(BorrowRepaymentDetailsCustomize);
			modelAndView.addObject("sumObject", sumObject);
		}
		modelAndView.addObject(BorrowRepaymentDetailsDefine.REPAYMENTINFO_FORM, form);
	}



	/**
	 * 数据导出
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowRepaymentDetailsDefine.EXPORT_ACTION)
	@RequiresPermissions(BorrowRepaymentDetailsDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, BorrowRepaymentDetailsBean form) {
		LogUtil.startLog(BorrowRepaymentDetailsController.class.toString(), BorrowRepaymentDetailsDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "还款详细导出数据";

		BorrowRepaymentDetailsCustomize BorrowRepaymentDetailsCustomize = new BorrowRepaymentDetailsCustomize();
		BeanUtils.copyProperties(form, BorrowRepaymentDetailsCustomize);

		List<BorrowRepaymentDetailsCustomize> recordList = this.borrowRepaymentDetailsService.selectBorrowRepaymentDetailsList(BorrowRepaymentDetailsCustomize);
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "项目编号","借款人ID","借款人用户名","项目类型","借款期限",
		        "出借利率","借款金额","借到金额","还款方式","出借人用户名","出借人ID","出借人用户属性（出借时）",
                "出借人所属一级分部（当前）","出借人所属二级分部（当前）","出借人所属团队（当前）",
                "推荐人用户名（出借时）","所属一级分部（出借时）",
                "所属二级分部（出借时）","所属团队（出借时）","还款期数","应还本金",
		        "应还利息","应还本息","管理费","还款状态","实际还款时间","应还款日期"};
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
					BorrowRepaymentDetailsCustomize record = recordList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					// 项目编号
					if (celLength == 0) {
						cell.setCellValue(record.getBorrowNid());
					}
					// 借款人ID
					else if (celLength == 1) {
						cell.setCellValue(record.getBorrowUserId());
					}
					// 借款人用户名
					else if (celLength == 2) {
						cell.setCellValue(record.getBorrowUserName());
					}
					// 项目类型
					else if (celLength == 3) {
					    cell.setCellValue(record.getProjectTypeName());
					}
					// 借款期限
					else if (celLength == 4) {
						cell.setCellValue(record.getBorrowPeriod());
					}
					// 出借利率
					else if (celLength == 5) {
					    cell.setCellValue(record.getBorrowApr() + "%");
					}
					// 借款金额
					else if (celLength == 6) {
					    cell.setCellValue(record.getBorrowAccount().equals("") ? 0 : Double.valueOf(record.getBorrowAccount()));
					}
					// 借到金额
					else if (celLength == 7) {
						cell.setCellValue(record.getBorrowAccountYes().equals("") ? 0 : Double.valueOf(record.getBorrowAccountYes()));
					}
					// 还款方式
					else if (celLength == 8) {
						cell.setCellValue(record.getRepayType());
					}
					// 出借人用户名
					else if (celLength == 9) {
						cell.setCellValue(record.getRecoverUserName());
					}
					// 出借人ID
					else if (celLength == 10) {
						cell.setCellValue(record.getRecoverUserId());
					}
					// 出借人用户属性（出借时）
					else if (celLength == 11) {
						if ("0".equals(record.getTenderUserAttribute())) {
							cell.setCellValue("无主单");
						} else if ("1".equals(record.getTenderUserAttribute())) {
							cell.setCellValue("有主单");
						} else if ("2".equals(record.getTenderUserAttribute())) {
							cell.setCellValue("线下员工");
						} else if ("3".equals(record.getTenderUserAttribute())) {
							cell.setCellValue("线上员工");
						}
					}
					// 出借人所属一级分部（当前）
					else if (celLength == 12) {
						cell.setCellValue(record.getRecoverRegionName());
					}
					// 出借人所属二级分部（当前）
					else if (celLength == 13) {
						cell.setCellValue(record.getRecoverBranchName());
					}
					// 出借人所属团队（出借时）
					else if (celLength == 14) {
						cell.setCellValue(record.getRecoverDepartmentName());
					}
					// 推荐人用户名（出借时）
					else if (celLength == 15) {
						cell.setCellValue(record.getInviteUserName());
					}
					// 推荐人所属一级分部（出借时）
					else if (celLength == 16) {
						cell.setCellValue(record.getInviteRegionName());
					}
					// 推荐人所属二级分部（出借时）
					else if (celLength == 17) {
						cell.setCellValue(record.getInviteBranchName());
					}
					// 推荐人所属团队（出借时）
					else if (celLength == 18) {
						cell.setCellValue(record.getInviteDepartmentName());
					}
					// 分期还款期数
                    else if (celLength == 19) {
                        cell.setCellValue("第"+record.getRecoverPeriod()+"期");
                    }
					// 应还本金
					else if (celLength == 20) {
						cell.setCellValue(record.getRecoverCapital().equals("") ? 0 : Double.valueOf(record.getRecoverCapital()));
					}
					// 应还利息
					else if (celLength == 21) {
						cell.setCellValue(record.getRecoverInterest().equals("") ? 0 : Double.valueOf(record.getRecoverInterest()));
					}
					// 应还本息
					else if (celLength == 22) {
						cell.setCellValue(record.getRecoverAccount().equals("") ? 0 : Double.valueOf(record.getRecoverAccount()));
					}
					// 管理费
					else if (celLength == 23) {
						cell.setCellValue(record.getRecoverFee().equals("") ? 0 : Double.valueOf(record.getRecoverFee()));
					}
					// 还款状态
					else if (celLength == 24) {
						if (StringUtils.isNotEmpty(record.getStatus())) {
							cell.setCellValue(record.getStatus().equals("0") ? "还款中" : "已还款");
						}
					}
					
					// 实际回款时间
                    else if (celLength == 25) {
                        cell.setCellValue(record.getRecoverYestime());
                    }
					// 应回款时间
                    else if (celLength == 26) {
                        cell.setCellValue(record.getRecoverTime());
                    }
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
		LogUtil.endLog(BorrowRepaymentDetailsController.class.toString(), BorrowRepaymentDetailsDefine.EXPORT_ACTION);
	}
}
