package com.hyjf.admin.manager.plan.joindetail;

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
import com.hyjf.mybatis.model.customize.admin.AdminPlanAccedeDetailCustomize;

/**
 * 汇添金加入明细Controller
 * 
 * @ClassName HtjJoinDetailController
 * @author liuyang
 * @date 2016年9月29日 上午9:45:20
 */
@Controller
@RequestMapping(value = HtjJoinDetailDefine.REQUEST_MAPPING)
public class HtjJoinDetailController extends BaseController {

	@Autowired
	private HtjJoinDetailService htjJoinDetailService;

	/** 类名 */
	private static final String THIS_CLASS = HtjJoinDetailController.class.toString();

	/**
	 * 画面初期化
	 * 
	 * @Title init
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(HtjJoinDetailDefine.INIT)
	@RequiresPermissions(HtjJoinDetailDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, HtjJoinDetailBean form) {
		LogUtil.startLog(THIS_CLASS, HtjJoinDetailDefine.LIST_PATH);
		ModelAndView modeAndView = new ModelAndView(HtjJoinDetailDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(THIS_CLASS, HtjJoinDetailDefine.LIST_PATH);
		return modeAndView;
	}

	/**
	 * 检索Action
	 * 
	 * @Title searchAction
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(HtjJoinDetailDefine.SEARCH_ACTION)
	@RequiresPermissions(HtjJoinDetailDefine.PERMISSIONS_SEARCH)
	public ModelAndView searchAction(HttpServletRequest request, HtjJoinDetailBean form) {
		LogUtil.startLog(THIS_CLASS, HtjJoinDetailDefine.SEARCH_ACTION);
		ModelAndView modeAndView = new ModelAndView(HtjJoinDetailDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(THIS_CLASS, HtjJoinDetailDefine.SEARCH_ACTION);
		return modeAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, HtjJoinDetailBean form) {
		int count = htjJoinDetailService.countAccedeRecord(form);
		if (count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			form.setLimitStart(paginator.getOffset());
			form.setLimitEnd(paginator.getLimit());
			// 汇添金加入明细列表
			List<AdminPlanAccedeDetailCustomize> recordList = this.htjJoinDetailService.selectAccedeRecordList(form);
			// 总计
			String total = this.htjJoinDetailService.sumJoinAccount(form);
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			form.setTotal(total);
		}
		modelAndView.addObject(HtjJoinDetailDefine.JOINDETAIL_FORM, form);
	}

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(HtjJoinDetailDefine.EXPORTEXECL)
	@RequiresPermissions(HtjJoinDetailDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, HtjJoinDetailBean form) throws Exception {
		LogUtil.startLog(THIS_CLASS, HtjJoinDetailDefine.EXPORTEXECL);
		// 表格sheet名称
		String sheetName = "加入明细";
		List<AdminPlanAccedeDetailCustomize> resultList = this.htjJoinDetailService.selectAccedeRecordList(form);
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
		String[] titles = new String[] { "序号", "智投编号", "智投名称", "智投类型", "预期年化", "锁定期", "智投订单号", "冻结订单号", "用户名", "推荐人(当前)", "用户属性(当前)", "推荐人(出借时)", "分公司(出借时)", "部门(出借时)", "团队(出借时)", "授权服务金额", "状态", "操作平台", "授权服务时间" };
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
					AdminPlanAccedeDetailCustomize planAccedeDetail = resultList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 计划编号
					else if (celLength == 1) {
						cell.setCellValue(planAccedeDetail.getDebtPlanNid());
					}
					// 计划名称
					else if (celLength == 2) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getDebtPlanName()) ? StringUtils.EMPTY : planAccedeDetail.getDebtPlanName());
					}
					// 计划类型
					else if (celLength == 3) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getDebtPlanTypeName()) ? StringUtils.EMPTY : planAccedeDetail.getDebtPlanTypeName());
					}
					// 预期年化
					else if (celLength == 4) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getExpectApr()) ? StringUtils.EMPTY : planAccedeDetail.getExpectApr() + "%");
					}
					// 锁定期
					else if (celLength == 5) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getDebtLockPeriod()) ? StringUtils.EMPTY : planAccedeDetail.getDebtLockPeriod() + "个月");
					}
					// 计划订单号
					else if (celLength == 6) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getPlanOrderId()) ? StringUtils.EMPTY : planAccedeDetail.getPlanOrderId());
					}
					// 冻结订单号
					else if (celLength == 7) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getFreezeOrderId()) ? StringUtils.EMPTY : planAccedeDetail.getFreezeOrderId());
					}
					// 用户名
					else if (celLength == 8) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getUserName()) ? StringUtils.EMPTY : planAccedeDetail.getUserName());
					}
					// 推荐人（当前）
					else if (celLength == 9) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getRefereeUserName()) ? StringUtils.EMPTY : planAccedeDetail.getRefereeUserName());
					}
					// 用户属性（当前）
					else if (celLength == 10) {
						if ("0".equals(planAccedeDetail.getUserAttribute())) {
							cell.setCellValue("无主单");
						} else if ("1".equals(planAccedeDetail.getUserAttribute())) {
							cell.setCellValue("有主单");
						} else if ("2".equals(planAccedeDetail.getUserAttribute())) {
							cell.setCellValue("线下员工");
						} else if ("3".equals(planAccedeDetail.getUserAttribute())) {
							cell.setCellValue("线上员工");
						}
					}
					// 推荐人（出借时）
					else if (celLength == 11) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getInviteUserName()) ? StringUtils.EMPTY : planAccedeDetail.getInviteUserName());
					}
					// 分公司（出借时）
					else if (celLength == 12) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getInviteRegionName()) ? StringUtils.EMPTY : planAccedeDetail.getInviteRegionName());
					}
					// 部门（出借时）
					else if (celLength == 13) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getInviteBranchName()) ? StringUtils.EMPTY : planAccedeDetail.getInviteBranchName());
					}
					// 团队（出借时）
					else if (celLength == 14) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getInviteDepartmentName()) ? StringUtils.EMPTY : planAccedeDetail.getInviteDepartmentName());
					}
					// 加入金额
					else if (celLength == 15) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getAccedeAccount()) ? StringUtils.EMPTY : planAccedeDetail.getAccedeAccount());
					}
					// 状态
					else if (celLength == 16) {
						if (CustomConstants.DEBT_PLAN_STATUS_4 == Integer.parseInt(planAccedeDetail.getDebtPlanStatus())) {
							cell.setCellValue("申购中");
						} else if (CustomConstants.DEBT_PLAN_STATUS_5 == Integer.parseInt(planAccedeDetail.getDebtPlanStatus())) {
							cell.setCellValue("锁定中");
						} else if (CustomConstants.DEBT_PLAN_STATUS_11 == Integer.parseInt(planAccedeDetail.getDebtPlanStatus())) {
							cell.setCellValue("已退出");
						} else {
							cell.setCellValue("锁定中");
						}
					}
					// 平台
					else if (celLength == 17) {
						if ("0".equals(planAccedeDetail.getPlatform())) {
							cell.setCellValue("PC");
						} else if ("1".equals(planAccedeDetail.getPlatform())) {
							cell.setCellValue("微官网");
						} else if ("2".equals(planAccedeDetail.getPlatform())) {
							cell.setCellValue("android");
						} else if ("3".equals(planAccedeDetail.getPlatform())) {
							cell.setCellValue("ios");
						}
					}
					// 加入时间
					else if (celLength == 18) {
						if (StringUtils.isNotEmpty(planAccedeDetail.getCreateTime())) {
							cell.setCellValue(planAccedeDetail.getCreateTime());
						}
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(THIS_CLASS, HtjJoinDetailDefine.EXPORTEXECL);
	}
}
