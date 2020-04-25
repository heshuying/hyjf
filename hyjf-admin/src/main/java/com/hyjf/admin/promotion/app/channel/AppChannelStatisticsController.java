package com.hyjf.admin.promotion.app.channel;

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
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.AdminUtmReadPermissions;
import com.hyjf.mybatis.model.customize.app.AppChannelStatisticsCustomize;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = AppChannelStatisticsDefine.REQUEST_MAPPING)
public class AppChannelStatisticsController extends BaseController {

	@Autowired
	private AppChannelStatisticsService appChannelStatisticsService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AppChannelStatisticsDefine.INIT)
	@RequiresPermissions(AppChannelStatisticsDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute(AppChannelStatisticsDefine.FORM) AppChannelStatisticsBean form) {
		LogUtil.startLog(AppChannelStatisticsController.class.toString(), AppChannelStatisticsDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(AppChannelStatisticsDefine.LIST_PATH);

		// 获取登录用户的userId
		Integer userId = Integer.parseInt(ShiroUtil.getLoginUserId());
		// 根据用户Id查询渠道账号管理
		AdminUtmReadPermissions adminUtmReadPermissions = this.appChannelStatisticsService.selectAdminUtmReadPermissions(userId);
		if (adminUtmReadPermissions != null) {
			form.setUtmIds(adminUtmReadPermissions.getUtmIds());// 封装到页面
		}
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(AppChannelStatisticsController.class.toString(), AppChannelStatisticsDefine.INIT);
		return modelAndView;
	}

	/**
	 * 查询
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AppChannelStatisticsDefine.SEARCH_ACTION)
	@RequiresPermissions(AppChannelStatisticsDefine.PERMISSIONS_SEARCH)
	public ModelAndView searchAction(HttpServletRequest request, AppChannelStatisticsBean form) {
		LogUtil.startLog(AppChannelStatisticsController.class.toString(), AppChannelStatisticsDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(AppChannelStatisticsDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(AppChannelStatisticsController.class.toString(), AppChannelStatisticsDefine.INIT);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, AppChannelStatisticsBean form) {

		AppChannelStatisticsCustomize appChannelStatisticsCustomize = new AppChannelStatisticsCustomize();
		if (StringUtils.isNotEmpty(form.getTimeStartSrch())) {
			appChannelStatisticsCustomize.setTimeStartSrch(GetDate.getDayStart(form.getTimeStartSrch()));
		}
		if (StringUtils.isNotEmpty(form.getTimeEndSrch())) {
			appChannelStatisticsCustomize.setTimeEndSrch(GetDate.getDayEnd(form.getTimeEndSrch()));
		}
		// 渠道
		String[] utmIds = new String[] {};
		if (Validator.isNotNull(form.getUtmIds())) {
			if (form.getUtmIds().contains(StringPool.COMMA)) {
				utmIds = form.getUtmIds().split(StringPool.COMMA);
				appChannelStatisticsCustomize.setUtmIdsSrch(utmIds);
			} else {
				utmIds = new String[] { form.getUtmIds() };
				appChannelStatisticsCustomize.setUtmIdsSrch(utmIds);
			}
		}
		Integer count = this.appChannelStatisticsService.countSumList(appChannelStatisticsCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			appChannelStatisticsCustomize.setLimitStart(paginator.getOffset());
			appChannelStatisticsCustomize.setLimitEnd(paginator.getLimit());
			List<AppChannelStatisticsCustomize> recordList = this.appChannelStatisticsService.getSumRecordList(appChannelStatisticsCustomize);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}
		modelAndView.addObject(AppChannelStatisticsDefine.FORM, form);
	}

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(AppChannelStatisticsDefine.EXPORT_ACTION)
	@RequiresPermissions(AppChannelStatisticsDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, AppChannelStatisticsBean form) throws Exception {
		LogUtil.startLog(AppChannelStatisticsController.class.toString(), AppChannelStatisticsDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "app渠道统计";

		AppChannelStatisticsCustomize appChannelStatisticsCustomize = new AppChannelStatisticsCustomize();

		if (StringUtils.isNotEmpty(form.getTimeStartSrch())) {
			appChannelStatisticsCustomize.setTimeStartSrch(GetDate.getDayStart(form.getTimeStartSrch()));
		}
		if (StringUtils.isNotEmpty(form.getTimeEndSrch())) {
			appChannelStatisticsCustomize.setTimeEndSrch(GetDate.getDayEnd(form.getTimeEndSrch()));
		}
		if (StringUtils.isNotEmpty(form.getUtmIds())) {
			String[] utmIds = new String[] {};
			if (form.getUtmIds().contains(StringPool.COMMA)) {
				utmIds = form.getUtmIds().split(StringPool.COMMA);
				appChannelStatisticsCustomize.setUtmIdsSrch(utmIds);
			} else {
				utmIds = new String[] { form.getUtmIds() };
				appChannelStatisticsCustomize.setUtmIdsSrch(utmIds);
			}
		}
		List<AppChannelStatisticsCustomize> recordList = this.appChannelStatisticsService.exportList(appChannelStatisticsCustomize);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "序号", "渠道", "访问数", "注册数", "注册数(无主单)", "开户数", "开户数(无主单)", "开户数(PC)", "开户数(iOS)", "开户数(Android)", "开户数(微官网)", "出借人数", "出借人数(无主单)", "出借人数(PC)", "出借人数(iOS)", "出借人数(Android)", "出借人数(微官网)", "累计充值", "累计充值(无主单)", "累计出借", "累计出借(无主单)", "汇直投出借金额", "汇消费出借金额", "汇天利出借金额",
				"汇添金出借金额", "汇金理财出借金额", "汇转让出借金额" };
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
					AppChannelStatisticsCustomize record = recordList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 平台
					else if (celLength == 1) {
						cell.setCellValue(record.getChannelName());
					}
					// 访问数
					else if (celLength == 2) {
						cell.setCellValue(record.getVisitCount());
					}
					// 注册数
					else if (celLength == 3) {
						cell.setCellValue(record.getRegisterCount());
					}
					// 注册数（无主单）
					else if (celLength == 4) {
						cell.setCellValue(record.getRegisterAttrCount());
					}
					// 开户数
					else if (celLength == 5) {
						cell.setCellValue(record.getOpenAccountCount());
					}
					// 开户数（无主单）
					else if (celLength == 6) {
						cell.setCellValue(record.getOpenAccountAttrCount());
					}
					// 开户数(PC)
					else if (celLength == 7) {
						cell.setCellValue(record.getAccountNumberPc());
					}
					// 开户数(iOS)
					else if (celLength == 8) {
						cell.setCellValue(record.getAccountNumberIos());
					}
					// 开户数(Android)
					else if (celLength == 9) {
						cell.setCellValue(record.getAccountNumberAndroid());
					}
					// 开户数(微信)
					else if (celLength == 10) {
						cell.setCellValue(record.getAccountNumberWechat());
					}

					// 出借人数
					else if (celLength == 11) {
						cell.setCellValue(record.getInvestNumber());
					}
					// 出借人数(无主单)
					else if (celLength == 12) {
						cell.setCellValue(record.getInvestAttrNumber());
					}
					// 出借人数（PC）
					else if (celLength == 13) {
						cell.setCellValue(record.getTenderNumberPc());
					}
					// 出借人数(iOS)
					else if (celLength == 14) {
						cell.setCellValue(record.getTenderNumberIos());
					}
					// 出借人数(Android)
					else if (celLength == 15) {
						cell.setCellValue(record.getTenderNumberAndroid());
					}
					// 出借人数(微官网)
					else if (celLength == 16) {
						cell.setCellValue(record.getTenderNumberWechat());
					}
					// 累计充值
					else if (celLength == 17) {
						cell.setCellValue(record.getCumulativeCharge());
					}
					// 累计充值(无主单)
					else if (celLength == 18) {
						cell.setCellValue(record.getCumulativeAttrCharge());
					}
					// 累计出借
					else if (celLength == 19) {
						cell.setCellValue(record.getCumulativeInvest());
					}
					// 累计出借(无主单)
					else if (celLength == 20) {
						cell.setCellValue(record.getCumulativeAttrInvest());
					}
					// 汇直投出借金额
					else if (celLength == 21) {
						cell.setCellValue(record.getHztInvestSum());
					}
					// 汇消费出借金额
					else if (celLength == 22) {
						cell.setCellValue(record.getHxfInvestSum());
					}
					// 汇天利出借金额
					else if (celLength == 23) {
						cell.setCellValue(record.getHtlInvestSum());
					}
					// 汇添金出借金额
					else if (celLength == 24) {
						cell.setCellValue(record.getHtjInvestSum());
					}
					// 汇金理财出借金额
					else if (celLength == 25) {
						cell.setCellValue(record.getRtbInvestSum());
					}
					// 汇转让出借金额
					else if (celLength == 26) {
						cell.setCellValue(record.getHzrInvestSum());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(AppChannelStatisticsController.class.toString(), AppChannelStatisticsDefine.EXPORT_ACTION);
	}
}
