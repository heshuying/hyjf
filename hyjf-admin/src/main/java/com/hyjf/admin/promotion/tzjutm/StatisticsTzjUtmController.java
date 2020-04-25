package com.hyjf.admin.promotion.tzjutm;

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
import com.hyjf.mybatis.model.customize.admin.StatisticsTzjUtmCustomize;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = StatisticsTzjUtmDefine.REQUEST_MAPPING)
public class StatisticsTzjUtmController extends BaseController {

	@Autowired
	private StatisticsTzjUtmService statisticsTzjUtmService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(StatisticsTzjUtmDefine.INIT)
	@RequiresPermissions(StatisticsTzjUtmDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute(StatisticsTzjUtmDefine.FORM) StatisticsTzjUtmBean form) {
		LogUtil.startLog(StatisticsTzjUtmController.class.toString(), StatisticsTzjUtmDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(StatisticsTzjUtmDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(StatisticsTzjUtmController.class.toString(), StatisticsTzjUtmDefine.INIT);
		return modelAndView;
	}

	/**
	 * 查询
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(StatisticsTzjUtmDefine.SEARCH_ACTION)
	@RequiresPermissions(StatisticsTzjUtmDefine.PERMISSIONS_SEARCH)
	public ModelAndView searchAction(HttpServletRequest request, StatisticsTzjUtmBean form) {
		LogUtil.startLog(StatisticsTzjUtmController.class.toString(), StatisticsTzjUtmDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(StatisticsTzjUtmDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(StatisticsTzjUtmController.class.toString(), StatisticsTzjUtmDefine.INIT);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, StatisticsTzjUtmBean form) {

		StatisticsTzjUtmCustomize statisticsTzjUtmCustomize = new StatisticsTzjUtmCustomize();
		if (StringUtils.isNotEmpty(form.getTimeStartSrch())) {
			statisticsTzjUtmCustomize.setTimeStartSrch(form.getTimeStartSrch());
		}
		if (StringUtils.isNotEmpty(form.getTimeEndSrch())) {
			statisticsTzjUtmCustomize.setTimeEndSrch(form.getTimeEndSrch());
		}
		
		// 默认显示昨天的数据
		if(StringUtils.isEmpty(form.getTimeStartSrch()) && StringUtils.isEmpty(form.getTimeEndSrch())){
		    statisticsTzjUtmCustomize.setTimeStartSrch(GetDate.formatDate(GetDate.getSomeDayBeforeOrAfter(new Date(), -1)));
		    statisticsTzjUtmCustomize.setTimeEndSrch(GetDate.formatDate(GetDate.getSomeDayBeforeOrAfter(new Date(), -1)));
		}
		
		Integer count = this.statisticsTzjUtmService.countSumList(statisticsTzjUtmCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			statisticsTzjUtmCustomize.setLimitStart(paginator.getOffset());
			statisticsTzjUtmCustomize.setLimitEnd(paginator.getLimit());
			List<StatisticsTzjUtmCustomize> recordList = this.statisticsTzjUtmService.getSumRecordList(statisticsTzjUtmCustomize);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}
		modelAndView.addObject(StatisticsTzjUtmDefine.FORM, form);
	}

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(StatisticsTzjUtmDefine.EXPORT_ACTION)
	@RequiresPermissions(StatisticsTzjUtmDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, StatisticsTzjUtmBean form) throws Exception {
		LogUtil.startLog(StatisticsTzjUtmController.class.toString(), StatisticsTzjUtmDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "投之家子渠道报表";

		StatisticsTzjUtmCustomize StatisticsTzjUtmCustomize = new StatisticsTzjUtmCustomize();

		if (StringUtils.isNotEmpty(form.getTimeStartSrch())) {
			StatisticsTzjUtmCustomize.setTimeStartSrch(form.getTimeStartSrch());
		}
		if (StringUtils.isNotEmpty(form.getTimeEndSrch())) {
			StatisticsTzjUtmCustomize.setTimeEndSrch(form.getTimeEndSrch());
		}
		
		List<StatisticsTzjUtmCustomize> recordList = this.statisticsTzjUtmService.getSumRecordList(StatisticsTzjUtmCustomize);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "序号", "渠道", "注册", "开户", "开户比", "绑卡", "绑卡比", "新充人数", "新投人数", "新投转化率", "充值人数", "出借人数", "出借额", "首投人数", "首投金额", "复投人数", "复投率"};
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
					StatisticsTzjUtmCustomize record = recordList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 渠道
					else if (celLength == 1) {
						cell.setCellValue(record.getChannelName());
					}
					// 注册
					else if (celLength == 2) {
						cell.setCellValue(record.getRegistCount());
					}
					// 开户
					else if (celLength == 3) {
						cell.setCellValue(record.getOpenCount());
					}
					// 开户比
					else if (celLength == 4) {
						cell.setCellValue(record.getOpenRate()+"%");
					}
					// 绑卡
					else if (celLength == 5) {
						cell.setCellValue(record.getCardbindCount());
					}
					// 绑卡比
					else if (celLength == 6) {
						cell.setCellValue(record.getCardbindRate()+"%");
					}
					// 新充人数
					else if (celLength == 7) {
						cell.setCellValue(record.getRechargenewCount());
					}
					// 新投人数
					else if (celLength == 8) {
						cell.setCellValue(record.getTendernewCount());
					}
					// 新投转化率
					else if (celLength == 9) {
						cell.setCellValue(record.getTendernewRate() +"%");
					}
					// 充值人数
					else if (celLength == 10) {
						cell.setCellValue(record.getRechargeCount());
					}
					// 出借人数
					else if (celLength == 11) {
						cell.setCellValue(record.getTenderCount());
					}
					// 出借额
					else if (celLength == 12) {
						cell.setCellValue(record.getTenderMoney().toString());
					}
					// 首投人数
					else if (celLength == 13) {
						cell.setCellValue(record.getTenderfirstCount());
					}
					// 首投金额                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              
					else if (celLength == 14) {
						cell.setCellValue(record.getTenderfirstMoney().toString());
					}
					// 复投人数
					else if (celLength == 15) {
						cell.setCellValue(record.getTenderAgainCount());
					}
					//复投率
					else if (celLength == 16) {
						cell.setCellValue(record.getTenderAgainRate()+"%");
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(StatisticsTzjUtmController.class.toString(), StatisticsTzjUtmDefine.EXPORT_ACTION);
	}
}
