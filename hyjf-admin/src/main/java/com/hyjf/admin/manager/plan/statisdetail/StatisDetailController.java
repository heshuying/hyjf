package com.hyjf.admin.manager.plan.statisdetail;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.customize.PlanStatisCustomize;

/**
 * 
 * @author: zhouxiaoshuai
 * @email: 287424494@qq.com
 * @description:计划所用控制器
 * @version: 1
 * @date: 2016年9月12日 下午2:50:16
 */
@Controller
@RequestMapping(value = StatisDetailDefine.REQUEST_MAPPING)
public class StatisDetailController extends BaseController {

	@Autowired
	private StatisDetailService statisDetailService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(StatisDetailDefine.INIT)
	@RequiresPermissions(StatisDetailDefine.STATIS_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute("StatisDetailBean") StatisDetailBean form) {
		LogUtil.startLog(StatisDetailController.class.toString(), StatisDetailDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(StatisDetailDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(StatisDetailController.class.toString(), StatisDetailDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(StatisDetailDefine.SEARCH)
	@RequiresPermissions(StatisDetailDefine.STATIS_VIEW)
	public ModelAndView search(HttpServletRequest request, StatisDetailBean form) {
		LogUtil.startLog(StatisDetailController.class.toString(), StatisDetailDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(StatisDetailDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(StatisDetailController.class.toString(), StatisDetailDefine.INIT);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, StatisDetailBean form) {

		PlanStatisCustomize planStatisCustomize = new PlanStatisCustomize();
		String timeStart = form.getTimeStart();
		String timeEnd = form.getTimeEnd();
		if (StringUtils.isNotEmpty(timeStart)) {
			planStatisCustomize.setTimeStart(timeStart);
		}
		if (StringUtils.isNotEmpty(timeEnd)) {
			planStatisCustomize.setTimeEnd(timeEnd);
		}
		planStatisCustomize.setSort("desc");
		Long count = statisDetailService.countPlanStatic(planStatisCustomize);
		if (count > 0) {
			if (form.getPaginatorPage() == 0) {
				form.setPaginatorPage(1);
			}
			Paginator paginator = new Paginator(form.getPaginatorPage(), count.intValue());
			planStatisCustomize.setLimitStart(paginator.getOffset());
			planStatisCustomize.setLimitEnd(paginator.getLimit());
			List<PlanStatisCustomize> planStatis = statisDetailService.selectPlanStaticList(planStatisCustomize);
			form.setPaginator(paginator);
			modelAndView.addObject("planStatis", planStatis);
		}
		modelAndView.addObject(StatisDetailDefine.HTLSTATIS_FORM, form);
	}

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(StatisDetailDefine.EXPORT_ACTION)
	@RequiresPermissions(StatisDetailDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, StatisDetailBean form)
			throws Exception {
		LogUtil.startLog(StatisDetailController.class.toString(), StatisDetailDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "数据中心明细";
		PlanStatisCustomize planStatisCustomize = new PlanStatisCustomize();
		String timeStart = form.getTimeStart();
		String timeEnd = form.getTimeEnd();
		if (StringUtils.isNotEmpty(timeStart)) {
			planStatisCustomize.setTimeStart(timeStart);
		}
		if (StringUtils.isNotEmpty(timeEnd)) {
			planStatisCustomize.setTimeEnd(timeEnd);
		}
		planStatisCustomize.setSort("desc");
		List<PlanStatisCustomize> resultList = statisDetailService.selectPlanStaticList(planStatisCustomize);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date())
				+ CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "序号", "日期", "待成交资产-专属资产", "待成交资产--债权转让", "计划持有债权数量-专属资产", "计划持有债权数量-债权数量",
				"计划持有债权待还总额", "计划持有债权已还总额" };
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
					sheet = ExportExcel
							.createHSSFWorkbookTitle(workbook, titles, (sheetName + "_第" + sheetCount + "页"));
					rowNum = 1;
				}

				// 新建一行
				Row row = sheet.createRow(rowNum);
				// 循环数据
				for (int celLength = 0; celLength < titles.length; celLength++) {
					PlanStatisCustomize planStatisCustomizeResult = resultList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 小时时间
					else if (celLength == 1) {
						cell.setCellValue(planStatisCustomizeResult.getDataHour());
					}
					// 待成交资产-专属资产
					else if (celLength == 2) {
						cell.setCellValue(planStatisCustomizeResult.getWaitInvest() + "");
					}
					// 待成交资产--债权转让
					else if (celLength == 3) {
						cell.setCellValue(planStatisCustomizeResult.getWaitCredit() + "");
					}
					// 计划持有债权数量-专属资产
					else if (celLength == 4) {
						cell.setCellValue(planStatisCustomizeResult.getYesInvest() + "");
					}
					// 计划持有债权数量-债权数量
					else if (celLength == 5) {
						cell.setCellValue(planStatisCustomizeResult.getYesCredit() + "");
					}
					// 计划持有债权待还总额
					else if (celLength == 6) {
						cell.setCellValue(planStatisCustomizeResult.getWaitRepay() + "");
					}
					// 计划持有债权已还总额
					else if (celLength == 7) {
						cell.setCellValue(planStatisCustomizeResult.getYesRepay() + "");
					}

				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(StatisDetailController.class.toString(), StatisDetailDefine.EXPORT_ACTION);
	}

}
