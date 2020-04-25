package com.hyjf.admin.manager.borrow.borrowrecover;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.HjhInstConfig;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.BorrowRecoverCustomize;
import org.apache.commons.lang.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = BorrowRecoverDefine.REQUEST_MAPPING)
public class BorrowRecoverController extends BaseController {

	@Autowired
	private BorrowRecoverService borrowRecoverService;

	@Autowired
	private BorrowCommonService borrowCommonService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowRecoverDefine.INIT)
	@RequiresPermissions(BorrowRecoverDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, BorrowRecoverBean form) {
		LogUtil.startLog(BorrowRecoverController.class.toString(), BorrowRecoverDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(BorrowRecoverDefine.LIST_PATH);
		// 创建分页,放款时间默认当天
		Date endDate = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		form.setTimeStartSrch(simpleDateFormat.format(DateUtils.addDays(endDate, 0)));
		form.setTimeEndSrch(simpleDateFormat.format(DateUtils.addDays(endDate, 0)));
		
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BorrowRecoverController.class.toString(), BorrowRecoverDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowRecoverDefine.SEARCH_ACTION)
	@RequiresPermissions(BorrowRecoverDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, BorrowRecoverBean form) {
		LogUtil.startLog(BorrowRecoverController.class.toString(), BorrowRecoverDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(BorrowRecoverDefine.LIST_PATH);
		// 创建分页
		
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BorrowRecoverController.class.toString(), BorrowRecoverDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, BorrowRecoverBean form) {

		BorrowRecoverCustomize borrowRecoverCustomize = new BorrowRecoverCustomize();

		// 项目编号 检索条件
		borrowRecoverCustomize.setBorrowNidSrch(form.getBorrowNidSrch());
		// 项目名称 检索条件
		borrowRecoverCustomize.setBorrowNameSrch(form.getBorrowNameSrch());
		// 计划编号 检索条件
		borrowRecoverCustomize.setPlanNidSrch(form.getPlanNidSrch());
		// 出借人 检索条件
		borrowRecoverCustomize.setUsernameSrch(form.getUsernameSrch());
		// 出借订单号 检索条件
		borrowRecoverCustomize.setOrderNumSrch(form.getOrderNumSrch());
		// 合作机构编号
//		borrowRecoverCustomize.setInstCodeOrgSrch(form.getInstCodeOrgSrch());
		// 放款状态 检索条件
		borrowRecoverCustomize.setIsRecoverSrch(form.getIsRecoverSrch());
		// 出借时间 检索条件
		borrowRecoverCustomize.setTimeRecoverStartSrch(form.getTimeRecoverStartSrch());
		// 出借时间 检索条件
		borrowRecoverCustomize.setTimeRecoverEndSrch(form.getTimeRecoverEndSrch());
		// 出借时间 检索条件
		borrowRecoverCustomize.setTimeStartSrch(form.getTimeStartSrch());
		// 出借时间 检索条件
		borrowRecoverCustomize.setTimeEndSrch(form.getTimeEndSrch());
		// 放款订单号
		borrowRecoverCustomize.setLoanOrdid(form.getLoanOrdid());
		// 放款批次号
        borrowRecoverCustomize.setLoanBatchNo(form.getLoanBatchNo());
		// 放款状态
		List<ParamName> recoverList = this.borrowCommonService.getParamNameList(CustomConstants.LOAN_STATUS);
		modelAndView.addObject("recoverList", recoverList);
		/*--------------add by LSY START-------------------*/
		// 资金来源 检索条件
		// 合规删除
//		borrowRecoverCustomize.setInstCodeSrch(form.getInstCodeSrch());
		// 资金来源
//		List<HjhInstConfig> hjhInstConfigList = this.borrowRecoverService.hjhInstConfigList("");
//		modelAndView.addObject("hjhInstConfigList", hjhInstConfigList);
		/*--------------add by LSY END-------------------*/

		Long count = this.borrowRecoverService.countBorrowRecover(borrowRecoverCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			borrowRecoverCustomize.setLimitStart(paginator.getOffset());
			borrowRecoverCustomize.setLimitEnd(paginator.getLimit());
			List<BorrowRecoverCustomize> recordList = this.borrowRecoverService.selectBorrowRecoverList(borrowRecoverCustomize);
			BorrowRecoverCustomize sumAccount = this.borrowRecoverService.sumBorrowRecoverList(borrowRecoverCustomize);
			modelAndView.addObject("sumAccount", sumAccount);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}

		modelAndView.addObject(BorrowRecoverDefine.BORROW_FORM, form);
	}

	/**
	 * 具有   组织机构查看权限  的导出, 可以导出更多的字段
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowRecoverDefine.ENHANCE_EXPORT_ACTION)
	@RequiresPermissions(value = {BorrowRecoverDefine.PERMISSIONS_EXPORT, BorrowRecoverDefine.ENHANCE_PERMISSIONS_EXPORT}, logical = Logical.AND)
	public void enhanceExportAction(HttpServletRequest request, HttpServletResponse response, BorrowRecoverBean form) {
		LogUtil.startLog(BorrowRecoverController.class.toString(), BorrowRecoverDefine.ENHANCE_EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "放款明细";

		BorrowRecoverCustomize borrowRecoverCustomize = new BorrowRecoverCustomize();

		// 项目编号 检索条件
		borrowRecoverCustomize.setBorrowNidSrch(form.getBorrowNidSrch());
		/*--------------add by LSY START-------------------*/
		// 资金来源 检索条件
		borrowRecoverCustomize.setInstCodeSrch(form.getInstCodeSrch());
		// 计划编号 检索条件
		borrowRecoverCustomize.setPlanNidSrch(form.getPlanNidSrch());
		/*--------------add by LSY END-------------------*/
		// 项目名称 检索条件
		borrowRecoverCustomize.setBorrowNameSrch(form.getBorrowNameSrch());
		// 出借人 检索条件
		borrowRecoverCustomize.setUsernameSrch(form.getUsernameSrch());
		// 出借订单号 检索条件
		borrowRecoverCustomize.setOrderNumSrch(form.getOrderNumSrch());
		// 合作机构编号
		borrowRecoverCustomize.setInstCodeOrgSrch(form.getInstCodeOrgSrch());;
		// 放款状态 检索条件
		borrowRecoverCustomize.setIsRecoverSrch(form.getIsRecoverSrch());
		// 出借时间 检索条件
		borrowRecoverCustomize.setTimeRecoverStartSrch(form.getTimeRecoverStartSrch());
		// 出借时间 检索条件
		borrowRecoverCustomize.setTimeRecoverEndSrch(form.getTimeRecoverEndSrch());
		// 出借时间 检索条件
		borrowRecoverCustomize.setTimeStartSrch(form.getTimeStartSrch());
		// 出借时间 检索条件
		borrowRecoverCustomize.setTimeEndSrch(form.getTimeEndSrch());
		// 放款订单号
		borrowRecoverCustomize.setLoanOrdid(form.getLoanOrdid());
		// 放款批次号
        borrowRecoverCustomize.setLoanBatchNo(form.getLoanBatchNo());
		List<BorrowRecoverCustomize> resultList = this.borrowRecoverService.selectExportBorrowRecoverList(borrowRecoverCustomize);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
		
		/*--------------add by LSY START-------------------*/
        //String[] titles = new String[] { "序号", "项目编号", "计划编号", "借款人ID", "借款人用户名", "是否受托支付", "受托支付用户名", "项目名称", "项目类型", "借款期限", "出借利率", "还款方式", "出借订单号", "放款订单号",
        //"出借人用户名", "出借人ID", "出借时间", "出借金额", "应放款金额", "应收服务费", "实际放款金额", "实收服务费", "放款状态", "放款时间" ,
        String[] titles = new String[] { "序号", "项目编号", "资产来源", "智投编号", "借款人ID", "借款人用户名", "是否受托支付", "受托支付用户名", "项目类型", "借款期限", "出借利率", "还款方式", "出借订单号", "放款订单号","合作机构编号",
                "出借人用户名", "出借人ID", "出借时间", "出借金额", "应放款金额", "放款服务费", "实际放款金额", "实收服务费", "放款状态", "放款时间" ,
                "出借人用户属性（出借时）", "推荐人用户属性（出借时）", "推荐人（出借时）", "推荐人ID（出借时）", "一级分部（出借时）", "二级分部（出借时）", "团队（出借时）"};
		/*--------------add by LSY END-------------------*/
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
					BorrowRecoverCustomize record = resultList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 项目编号
					else if (celLength == 1) {
						cell.setCellValue(record.getBorrowNid());
					}
					/*--------------add by LSY START-------------------*/
					else if (celLength == 2) {
						cell.setCellValue(record.getInstName());
					}
					/*--------------add by LSY END-------------------*/
					// 计划编号
					else if (celLength == 3) {
						cell.setCellValue(record.getPlanNid());
					}
					// 借款人ID
					else if (celLength == 4) {
						cell.setCellValue(record.getUserId());
					}
					// 借款人用户名
					else if (celLength == 5) {
						cell.setCellValue(record.getUsername());
					}
					// 是否受托支付 2017.11.7新增
                    else if (celLength == 6) {
                        Integer isStzf = record.getEntrustedFlg();
                        String isStzf_str="否";
                        if (isStzf != null && isStzf - 1 == 0) {
                            isStzf_str = "是";
                        }
                        cell.setCellValue(isStzf_str);
                    }
					// 受托支付用户名 2017.11.7新增
                    else if (celLength == 7) {
                        cell.setCellValue(record.getEntrustedUserName());
                    }
					// 项目名称
//					else if (celLength == 8) {
//						cell.setCellValue(record.getBorrowName());
//					}
					// 项目类型
					else if (celLength == 8) {
						cell.setCellValue(record.getBorrowProjectTypeName());
					}
					// 借款期限
					else if (celLength == 9) {
						cell.setCellValue(record.getBorrowPeriod());
					}
					// 出借利率
					else if (celLength == 10) {
						cell.setCellValue(record.getBorrowApr());
					}
					// 还款方式
					else if (celLength == 11) {
						cell.setCellValue(record.getBorrowStyleName());
					}
					// 出借订单号
					else if (celLength == 12) {
						cell.setCellValue(record.getOrderNum());
					}
					// 出借订单号
					else if (celLength == 13) {
						cell.setCellValue(record.getLoanOrdid());
					}
					// 合作机构编号
					else if (celLength == 14) {
						cell.setCellValue(record.getInstCode());
					}
					// 出借人用户名
					else if (celLength == 15) {
						cell.setCellValue(record.getTenderUsername());
					}
					// 出借人ID
					else if (celLength == 16) {
						cell.setCellValue(record.getTenderUserId());
					}
					// 出借时间
					else if (celLength == 17) {
						cell.setCellValue(record.getAddtime());
					}
					// 出借金额
					else if (celLength == 18) {
						cell.setCellValue(record.getAccount());
					}
					// 应放款金额
					else if (celLength == 19) {
						cell.setCellValue(record.getAccountYes());
					}
					// 应收服务费
					else if (celLength == 20) {
						cell.setCellValue(record.getLoanFee());
					}
					// 实际放款金额
					else if (celLength == 21) {
						cell.setCellValue(record.getRecoverPrice());
					}
					// 实收服务费
					else if (celLength == 22) {
						cell.setCellValue(record.getServicePrice());
					}
					// 放款状态
					else if (celLength == 23) {
						cell.setCellValue(record.getIsRecover());
					}
					// 放款时间
					else if (celLength == 24) {
						cell.setCellValue(record.getTimeRecover());
					}
					// 出借人用户属性（出借时）
					else if (celLength == 25) {
						cell.setCellValue(record.getTenderUserAttribute());
					}
					// 推荐人用户属性（出借时）
                    else if (celLength == 26) {
                        cell.setCellValue(record.getInviteUserAttribute());
                    }
					// 推荐人（出借时）
                    else if (celLength == 27) {
                        cell.setCellValue(record.getTenderReferrerUsername());
                    }
					// 推荐人ID（出借时）
                    else if (celLength == 28) {
                        cell.setCellValue(record.getTenderReferrerUserId());
                    }
					// 一级分部（出借时）
                    else if (celLength == 29) {
                        cell.setCellValue(record.getDepartmentLevel1Name());
                    }
					// 二级分部（出借时）
                    else if (celLength == 30) {
                        cell.setCellValue(record.getDepartmentLevel2Name());
                    }
					// 团队（出借时）
                    else if (celLength == 31) {
                        cell.setCellValue(record.getTeamName());
                    }
					
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
		LogUtil.endLog(BorrowRecoverController.class.toString(), BorrowRecoverDefine.EXPORT_ACTION);
	}

	/**
	 * 数据导出
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowRecoverDefine.EXPORT_ACTION)
	@RequiresPermissions(BorrowRecoverDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, BorrowRecoverBean form) {
		LogUtil.startLog(BorrowRecoverController.class.toString(), BorrowRecoverDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "放款明细";

		BorrowRecoverCustomize borrowRecoverCustomize = new BorrowRecoverCustomize();

		// 项目编号 检索条件
		borrowRecoverCustomize.setBorrowNidSrch(form.getBorrowNidSrch());
		/*--------------add by LSY START-------------------*/
		// 资金来源 检索条件
		borrowRecoverCustomize.setInstCodeSrch(form.getInstCodeSrch());
		// 计划编号 检索条件
		borrowRecoverCustomize.setPlanNidSrch(form.getPlanNidSrch());
		/*--------------add by LSY END-------------------*/
		// 项目名称 检索条件
		borrowRecoverCustomize.setBorrowNameSrch(form.getBorrowNameSrch());
		// 出借人 检索条件
		borrowRecoverCustomize.setUsernameSrch(form.getUsernameSrch());
		// 出借订单号 检索条件
		borrowRecoverCustomize.setOrderNumSrch(form.getOrderNumSrch());
		// 合作机构编号
		borrowRecoverCustomize.setInstCodeOrgSrch(form.getInstCodeOrgSrch());;
		// 放款状态 检索条件
		borrowRecoverCustomize.setIsRecoverSrch(form.getIsRecoverSrch());
		// 出借时间 检索条件
		borrowRecoverCustomize.setTimeRecoverStartSrch(form.getTimeRecoverStartSrch());
		// 出借时间 检索条件
		borrowRecoverCustomize.setTimeRecoverEndSrch(form.getTimeRecoverEndSrch());
		// 出借时间 检索条件
		borrowRecoverCustomize.setTimeStartSrch(form.getTimeStartSrch());
		// 出借时间 检索条件
		borrowRecoverCustomize.setTimeEndSrch(form.getTimeEndSrch());
		// 放款订单号
		borrowRecoverCustomize.setLoanOrdid(form.getLoanOrdid());
		// 放款批次号
		borrowRecoverCustomize.setLoanBatchNo(form.getLoanBatchNo());
		List<BorrowRecoverCustomize> resultList = this.borrowRecoverService.selectExportBorrowRecoverList(borrowRecoverCustomize);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		/*--------------add by LSY START-------------------*/
        String[] titles = new String[] { "序号", "项目编号", "资产来源", "智投编号", "借款人ID", "借款人用户名", "是否受托支付", "受托支付用户名", "项目类型", "借款期限", "出借利率", "还款方式", "出借订单号", "放款订单号","合作机构编号",
                "出借人用户名", "出借人ID", "出借时间", "出借金额", "应放款金额", "放款服务费", "实际放款金额", "实收服务费", "放款状态", "放款时间" ,
                "出借人用户属性（出借时）", "推荐人用户属性（出借时）", "推荐人（出借时）", "推荐人ID（出借时）"};
		/*--------------add by LSY END-------------------*/
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
					BorrowRecoverCustomize record = resultList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 项目编号
					else if (celLength == 1) {
						cell.setCellValue(record.getBorrowNid());
					}
					/*--------------add by LSY START-------------------*/
					else if (celLength == 2) {
						cell.setCellValue(record.getInstName());
					}
					/*--------------add by LSY END-------------------*/
					// 智投编号
					else if (celLength == 3) {
						cell.setCellValue(record.getPlanNid());
					}
					// 借款人ID
					else if (celLength == 4) {
						cell.setCellValue(record.getUserId());
					}
					// 借款人用户名
					else if (celLength == 5) {
						cell.setCellValue(record.getUsername());
					}
					// 是否受托支付 2017.11.7新增
					else if (celLength == 6) {
						Integer isStzf = record.getEntrustedFlg();
						String isStzf_str="否";
						if (isStzf != null && isStzf - 1 == 0) {
							isStzf_str = "是";
						}
						cell.setCellValue(isStzf_str);
					}
					// 受托支付用户名 2017.11.7新增
					else if (celLength == 7) {
						cell.setCellValue(record.getEntrustedUserName());
					}
					// 项目名称
//					else if (celLength == 8) {
//						cell.setCellValue(record.getBorrowName());
//					}
					// 项目类型
					else if (celLength == 8) {
						cell.setCellValue(record.getBorrowProjectTypeName());
					}
					// 借款期限
					else if (celLength == 9) {
						cell.setCellValue(record.getBorrowPeriod());
					}
					// 出借利率
					else if (celLength == 10) {
						cell.setCellValue(record.getBorrowApr());
					}
					// 还款方式
					else if (celLength == 11) {
						cell.setCellValue(record.getBorrowStyleName());
					}
					// 出借订单号
					else if (celLength == 12) {
						cell.setCellValue(record.getOrderNum());
					}
					// 出借订单号
					else if (celLength == 13) {
						cell.setCellValue(record.getLoanOrdid());
					}
					// 合作机构编号
					else if (celLength == 14) {
						cell.setCellValue(record.getInstCode());
					}
					// 出借人用户名
					else if (celLength == 15) {
						cell.setCellValue(record.getTenderUsername());
					}
					// 出借人ID
					else if (celLength == 16) {
						cell.setCellValue(record.getTenderUserId());
					}
					// 出借时间
					else if (celLength == 17) {
						cell.setCellValue(record.getAddtime());
					}
					// 授权服务金额
					else if (celLength == 18) {
						cell.setCellValue(record.getAccount());
					}
					// 应放款金额
					else if (celLength == 19) {
						cell.setCellValue(record.getAccountYes());
					}
					// 应收服务费
					else if (celLength == 20) {
						cell.setCellValue(record.getLoanFee());
					}
					// 实际放款金额
					else if (celLength == 21) {
						cell.setCellValue(record.getRecoverPrice());
					}
					// 实收服务费
					else if (celLength == 22) {
						cell.setCellValue(record.getServicePrice());
					}
					// 放款状态
					else if (celLength == 23) {
						cell.setCellValue(record.getIsRecover());
					}
					// 放款时间
					else if (celLength == 24) {
						cell.setCellValue(record.getTimeRecover());
					}
					// 出借人用户属性（出借时）
					else if (celLength == 25) {
						cell.setCellValue(record.getTenderUserAttribute());
					}
					// 推荐人用户属性（出借时）
					else if (celLength == 26) {
						cell.setCellValue(record.getInviteUserAttribute());
					}
					// 推荐人（出借时）
					else if (celLength == 27) {
						cell.setCellValue(record.getTenderReferrerUsername());
					}
					// 推荐人ID（出借时）
					else if (celLength == 28) {
						cell.setCellValue(record.getTenderReferrerUserId());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
		LogUtil.endLog(BorrowRecoverController.class.toString(), BorrowRecoverDefine.EXPORT_ACTION);
	}
}
