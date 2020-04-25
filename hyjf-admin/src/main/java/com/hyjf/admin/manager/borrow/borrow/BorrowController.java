package com.hyjf.admin.manager.borrow.borrow;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonService;
import com.hyjf.admin.manager.hjhplan.planlist.PlanListDefine;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.*;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.HjhInstConfig;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.BorrowCustomize;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = BorrowDefine.REQUEST_MAPPING)
public class BorrowController extends BaseController {

	@Autowired
	private BorrowService borrowService;

	@Autowired
	private BorrowCommonService borrowCommonService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowDefine.INIT)
	@RequiresPermissions(BorrowDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute("borrowBean") BorrowBean form) {
		LogUtil.startLog(BorrowController.class.toString(), BorrowDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(BorrowDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BorrowController.class.toString(), BorrowDefine.INIT);
		return modelAndView;
	}

	
	/**
	 * 画面迁移
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowDefine.PREVIEW_ACTION)
	@RequiresPermissions(BorrowDefine.PERMISSIONS_PREVIEW)
	public ModelAndView previewAction(HttpServletRequest request, @ModelAttribute(BorrowDefine.BORROW_FORM) BorrowBean form) {
		LogUtil.startLog(BorrowController.class.toString(), BorrowDefine.PREVIEW_ACTION);
		ModelAndView modelAndView = new ModelAndView(BorrowDefine.PREVIEW_PATH);
		modelAndView.addObject("previewUrl", "https://www.hyjf.com/bank/web/borrow/getBorrowDetailPreview.do?borrowNid="+form.getBorrowNid());
		LogUtil.endLog(BorrowController.class.toString(), BorrowDefine.INFO_ACTION);
		return modelAndView;
	}
	
	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowDefine.SEARCH_ACTION)
	@RequiresPermissions(BorrowDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, BorrowBean form) {
		LogUtil.startLog(BorrowController.class.toString(), BorrowDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(BorrowDefine.LIST_PATH);		
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BorrowController.class.toString(), BorrowDefine.INIT);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, BorrowBean form) {

		// 资金来源
		List<HjhInstConfig> hjhInstConfigList = this.borrowCommonService.hjhInstConfigList("");
		modelAndView.addObject("hjhInstConfigList", hjhInstConfigList);
		
		// 项目类型
		List<BorrowProjectType> borrowProjectTypeList = this.borrowCommonService.borrowProjectTypeList("");
		modelAndView.addObject("borrowProjectTypeList", borrowProjectTypeList);

		// 还款方式
		List<BorrowStyle> borrowStyleList = this.borrowCommonService.borrowStyleList("");
		modelAndView.addObject("borrowStyleList", borrowStyleList);

		// 项目状态
		List<ParamName> borrowStatusList = this.borrowCommonService.getParamNameList(CustomConstants.BORROW_STATUS);
		modelAndView.addObject("borrowStatusList", borrowStatusList);

		BorrowCommonCustomize corrowCommonCustomize = new BorrowCommonCustomize();
		// 借款编码
		corrowCommonCustomize.setBorrowNidSrch(form.getBorrowNidSrch());
		// 项目名称
		/* DEL BY LIUSHOUYI 合规检查 START
		corrowCommonCustomize.setBorrowNameSrch(form.getBorrowNameSrch());
		DEL BY LIUSHOUYI 合规检查 END*/
		// 借 款 人
		corrowCommonCustomize.setUsernameSrch(form.getUsernameSrch());
		// 项目状态
		corrowCommonCustomize.setStatusSrch(form.getStatusSrch());
		// 项目类型
		corrowCommonCustomize.setProjectTypeSrch(form.getProjectTypeSrch());
		// 还款方式
		corrowCommonCustomize.setBorrowStyleSrch(form.getBorrowStyleSrch());
		// 放款时间
		corrowCommonCustomize.setRecoverTimeStartSrch(form.getRecoverTimeStartSrch());
		// 放款时间
		corrowCommonCustomize.setRecoverTimeEndSrch(form.getRecoverTimeEndSrch());
		// 添加时间
		corrowCommonCustomize.setTimeStartSrch(form.getTimeStartSrch());
		// 添加时间
		corrowCommonCustomize.setTimeEndSrch(form.getTimeEndSrch());
		// 标签名称 new added
		corrowCommonCustomize.setLabelNameSrch(form.getLabelNameSrch());
		corrowCommonCustomize.setSort(form.getSort());
		corrowCommonCustomize.setCol(form.getCol());
		corrowCommonCustomize.setBorrowPeriod(form.getBorrowPeriod());
		// 计划编号
		corrowCommonCustomize.setPlanNidSrch(form.getPlanNidSrch());
		// 资金来源编号
		corrowCommonCustomize.setInstCodeSrch(form.getInstCodeSrch());
		// ADD BY zhangyunkai 增加初审时间查询条件 start
        corrowCommonCustomize.setVerifyTimeStartSrch(form.getVerifyTimeStartSrch());
		corrowCommonCustomize.setVerifyTimeEndSrch(form.getVerifyTimeEndSrch());
		// ADD BY zhangyunkai 增加初审时间查询条件 end
		Long count = this.borrowService.countBorrow(corrowCommonCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			corrowCommonCustomize.setLimitStart(paginator.getOffset());
			corrowCommonCustomize.setLimitEnd(paginator.getLimit());
			List<BorrowCustomize> recordList = this.borrowService.selectBorrowList(corrowCommonCustomize);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
			BigDecimal sumAccount = this.borrowService.sumAccount(corrowCommonCustomize);
			modelAndView.addObject("sumAccount", sumAccount);
		}
		String webUrl = PropUtils.getSystem("hyjf.web.host");
		modelAndView.addObject("webUrl", webUrl);
		modelAndView.addObject("pageUrl", request.getRequestURL()+"?"+request.getQueryString());
		modelAndView.addObject(BorrowDefine.BORROW_FORM, form);
	}

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(BorrowDefine.EXPORT_ACTION)
	@RequiresPermissions(BorrowDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, BorrowBean form) throws Exception {
		LogUtil.startLog(BorrowController.class.toString(), BorrowDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "借款列表";

		BorrowCommonCustomize corrowCommonCustomize = new BorrowCommonCustomize();
		// 借款编码
		corrowCommonCustomize.setBorrowNidSrch(form.getBorrowNidSrch());
		// 项目名称
		/*DEL BY LIUSHOUYI 合规检查 START
		corrowCommonCustomize.setBorrowNameSrch(form.getBorrowNameSrch());
		DEL BY LIUSHOUYI 合规检查 END*/
		// 借 款 人
		corrowCommonCustomize.setUsernameSrch(form.getUsernameSrch());
		// 项目状态
		corrowCommonCustomize.setStatusSrch(form.getStatusSrch());
		// 项目类型
		corrowCommonCustomize.setProjectTypeSrch(form.getProjectTypeSrch());
		// 还款方式
		corrowCommonCustomize.setBorrowStyleSrch(form.getBorrowStyleSrch());
		// 放款时间
		corrowCommonCustomize.setRecoverTimeStartSrch(StringUtils.isNotBlank(form.getRecoverTimeStartSrch())?form.getRecoverTimeStartSrch():null);
		// 放款时间
		corrowCommonCustomize.setRecoverTimeEndSrch(StringUtils.isNotBlank(form.getRecoverTimeEndSrch())?form.getRecoverTimeEndSrch():null);
		// 添加时间
		corrowCommonCustomize.setTimeStartSrch(StringUtils.isNotBlank(form.getTimeStartSrch())?form.getTimeStartSrch():null);
		// 添加时间
		corrowCommonCustomize.setTimeEndSrch(StringUtils.isNotBlank(form.getTimeEndSrch())?form.getTimeEndSrch():null);
		// 标签名称 查询
		corrowCommonCustomize.setLabelNameSrch(form.getLabelNameSrch());

		corrowCommonCustomize.setPlanNidSrch(form.getPlanNidSrch());
		// ADD BY zhangyunkai 增加初审时间查询条件 start
		corrowCommonCustomize.setVerifyTimeStartSrch(form.getVerifyTimeStartSrch());
		corrowCommonCustomize.setVerifyTimeEndSrch(form.getVerifyTimeEndSrch());
		// ADD BY zhangyunkai 增加初审时间查询条件 end
		List<BorrowCommonCustomize> resultList = this.borrowService.exportBorrowList(corrowCommonCustomize);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		//UPD BY LIUSHOUYI 合规检查 START
		/* 
		String[] titles = new String[] { "序号", "项目编号", "计划编号", "借款人ID", "借款人用户名", "项目申请人","项目名称", "项目名称", "项目类型", "资产来源", "借款金额（元）", "借款期限", "出借利率", "还款方式", "放款服务费率", "还款服务费率", "合作机构", "已借到金额", "剩余金额", "借款进度", "项目状态", "添加时间",
				"初审通过时间", "定时发标时间","预约开始时间","预约截止时间", "实际发标时间", "出借截止时间", "满标时间", "复审通过时间", "放款完成时间", "最后还款日","备案时间","担保机构用户名","复审人员","所在地区","借款人姓名","属性","是否受托支付","收款人用户名","标签名称","备注" };
		*/
		String[] titles = new String[] { "序号", "项目编号", "智投编号", "借款人ID", "借款人用户名", "项目名称", "项目类型", "资产来源", "借款金额（元）", "借款期限", "出借利率", "还款方式", "放款服务费率", "还款服务费率", "合作机构", "已借到金额", "剩余金额", "借款进度", "项目状态", "添加时间",
				"初审通过时间", "定时发标时间","预约开始时间","预约截止时间", "实际发标时间", "出借截止时间", "满标时间", "复审通过时间", "放款完成时间", "最后还款日","备案时间","所在地区","借款人姓名","属性","是否受托支付","收款人用户名","标签名称","备注" ,"添加标的人员","标的备案人员","担保机构用户名","加息收益率"};
	// UPD BY LIUSHOUYI 合规检查 END
		
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
					BorrowCommonCustomize borrowCommonCustomize = resultList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 项目编号
					else if (celLength == 1) {
						cell.setCellValue(borrowCommonCustomize.getBorrowNid());
					}
					// 计划编号
					else if (celLength == 2) {
						cell.setCellValue(borrowCommonCustomize.getPlanNid());
					}
					// 借款人ID
					else if (celLength == 3) {
						cell.setCellValue(borrowCommonCustomize.getUserId());
					}
					// 借款人用户名
					else if (celLength == 4) {
						cell.setCellValue(borrowCommonCustomize.getUsername());
					}
					// 项目申请人 (合规删除 modify by hesy 2018-12-05)
//					else if (celLength == 5) {
//						cell.setCellValue(borrowCommonCustomize.getApplicant());
//					}
					// 项目名称
					else if (celLength == 5) {
						cell.setCellValue(StringUtils.isEmpty(borrowCommonCustomize.getProjectName()) ? ""
								: borrowCommonCustomize.getProjectName());
					}
					// 项目类型
					else if (celLength == 6) {
						cell.setCellValue(borrowCommonCustomize.getBorrowProjectTypeName());
					}
					// 资产来源
					else if (celLength == 7) {
						cell.setCellValue(borrowCommonCustomize.getInstName());
					}
					// 借款金额（元）
					else if (celLength == 8) {
						cell.setCellValue(borrowCommonCustomize.getAccount());
					}
					// 借款期限
					else if (celLength == 9) {
						cell.setCellValue(borrowCommonCustomize.getBorrowPeriod());
					}
					// 出借利率
					else if (celLength == 10) {
						cell.setCellValue(borrowCommonCustomize.getBorrowApr());
					}
					// 还款方式
					else if (celLength == 11) {
						cell.setCellValue(borrowCommonCustomize.getBorrowStyle());
					}
					// 放款服务费率
					else if (celLength == 12) {
						cell.setCellValue(borrowCommonCustomize.getBorrowServiceScale());
					}
					// 还款服务费率
					else if (celLength == 13) {
						cell.setCellValue(borrowCommonCustomize.getBorrowManagerScale());
					}
					// 合作机构
					else if (celLength == 14) {
						cell.setCellValue(borrowCommonCustomize.getBorrowMeasuresInstit());
					}
					// 已借到金额
					else if (celLength == 15) {
						cell.setCellValue(borrowCommonCustomize.getBorrowAccountYes());
					}
					// 剩余金额
					else if (celLength == 16) {
						cell.setCellValue(borrowCommonCustomize.getBorrowAccountWait());
					}
					// 借款进度
					else if (celLength == 17) {
						cell.setCellValue(borrowCommonCustomize.getBorrowAccountScale());
					}
					// 项目状态
					else if (celLength == 18) {
						cell.setCellValue(borrowCommonCustomize.getStatus());
					}
					// 添加时间
					else if (celLength == 19) {
						cell.setCellValue(borrowCommonCustomize.getAddtime());
					}
					// 初审通过时间
					else if (celLength == 20) {
						cell.setCellValue(borrowCommonCustomize.getVerifyTime());
					}
					// 定时发标时间
					else if (celLength == 21) {
						cell.setCellValue(borrowCommonCustomize.getOntime());
					}
					// 预约开始时间
					else if (celLength == 22) {
						if (!"待发布".equals(borrowCommonCustomize.getStatus())) {
							cell.setCellValue(borrowCommonCustomize.getBookingBeginTime());
						}
					}
					// 预约截止时间
					else if (celLength == 23) {
						if (!"待发布".equals(borrowCommonCustomize.getStatus())) {
							cell.setCellValue(borrowCommonCustomize.getBookingEndTime());
						}
					}
					// 实际发标时间
					else if (celLength == 24) {
						cell.setCellValue(borrowCommonCustomize.getVerifyTime());
					}
					// 投稿截止时间
					else if (celLength == 25) {
						cell.setCellValue(borrowCommonCustomize.getBorrowValidTime());
					}
					// 满标时间
					else if (celLength == 26) {
						cell.setCellValue(borrowCommonCustomize.getBorrowFullTime());
					}
					// 复审通过时间
					else if (celLength == 27) {
						cell.setCellValue(borrowCommonCustomize.getReverifyTime());
					}
					// 放款完成时间
					else if (celLength == 28) {
						cell.setCellValue(borrowCommonCustomize.getRecoverLastTime());
					}
					// 最后还款日
					else if (celLength == 29) {
						cell.setCellValue(borrowCommonCustomize.getRepayLastTime());
					}
					// 备案时间
					else if (celLength == 30) {
						cell.setCellValue(borrowCommonCustomize.getRegistTime());
					}
//					// 复审人员
//					else if (celLength == 32) {
//						cell.setCellValue(borrowCommonCustomize.getReverifyUserName());
//					}
					// 所在地区
                    else if (celLength == 31) {
                        cell.setCellValue(borrowCommonCustomize.getLocation());
                    }
					// 借款人姓名
                    else if (celLength == 32) {
                        cell.setCellValue(borrowCommonCustomize.getBorrowerName());
                    }
					// 属性
                    else if (celLength == 33) {
                        cell.setCellValue(borrowCommonCustomize.getAttribute());
                    }
					// 是否受托支付
                    else if (celLength == 34) {
                    	if ("0".equals(borrowCommonCustomize.getEntrustedFlg())) {
                    		cell.setCellValue("否");
                    	} else {
                    		cell.setCellValue("是");
                    	}
                    }
					// 收款人用户名
                    else if (celLength == 35) {
                    	if ("0".equals(borrowCommonCustomize.getEntrustedFlg())) {
                    		cell.setCellValue("未开通受托支付");
                    	} else {
                    		cell.setCellValue(borrowCommonCustomize.getEntrustedUsername());
                    	}
                    }
//					// 账户操作人
//                    else if (celLength == 38) {
//                        cell.setCellValue(borrowCommonCustomize.getCreateUserName());
//                    }
//                    // 备案人员 
//                    else if (celLength == 39) {
//                        cell.setCellValue(borrowCommonCustomize.getRegistUserName());
//                    }
					// 标签名称 
                    else if (celLength == 36) {
                        cell.setCellValue(borrowCommonCustomize.getLabelNameSrch());
                    }
					// 备注 
                    else if (celLength == 37) {
                        cell.setCellValue(borrowCommonCustomize.getRemark());
                    }
					// 添加标的人员 
                    else if (celLength == 38) {
                        cell.setCellValue(borrowCommonCustomize.getCreatename());
                    }
					// 标的备案人员 
                    else if (celLength == 39) {
                        cell.setCellValue(borrowCommonCustomize.getRegistname());
                    }
					// 担保机构用户名
					else if (celLength == 40) {
						cell.setCellValue(borrowCommonCustomize.getRepayOrgUserName());
					}
                    // UPD BY LIUSHOUYI 合规检查 END
					else if (celLength == 41) {
						if (new BigDecimal(borrowCommonCustomize.getBorrowExtraYield()).compareTo(BigDecimal.ZERO) > 0) {
							cell.setCellValue(borrowCommonCustomize.getBorrowExtraYield() + "%");
						}
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(BorrowController.class.toString(), BorrowDefine.EXPORT_ACTION);
	}

	/**
	 * 运营记录-原始标的
	 * @param request
	 * @param form
     * @return
     */
	@RequestMapping(BorrowDefine.OPT_ACTION)
	@RequiresPermissions(BorrowDefine.PERMISSIONS_VIEW)
	public ModelAndView optRecord(HttpServletRequest request, @ModelAttribute("borrowBean") BorrowBean form) {
		LogUtil.startLog(BorrowController.class.toString(), BorrowDefine.OPT_ACTION);
		ModelAndView modelAndView = new ModelAndView(PlanListDefine.OPT_LOAD_LIST_PATH);
		// 创建分页
		if (StringUtils.isNotBlank(form.getPlanNidSrch())){
			form.setPlanNidTemp(form.getPlanNidSrch());
		}
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BorrowController.class.toString(), BorrowDefine.OPT_ACTION);
		modelAndView.addObject("borrowForm",form);
		return modelAndView;
	}
}
