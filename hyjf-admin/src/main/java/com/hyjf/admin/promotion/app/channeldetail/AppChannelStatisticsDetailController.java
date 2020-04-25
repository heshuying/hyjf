package com.hyjf.admin.promotion.app.channeldetail;

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
import com.hyjf.mybatis.model.customize.app.AppChannelStatisticsDetailCustomize;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = AppChannelStatisticsDetailDefine.REQUEST_MAPPING)
public class AppChannelStatisticsDetailController extends BaseController {

	@Autowired
	private AppChannelStatisticsDetailService channelStatisticsDetailService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AppChannelStatisticsDetailDefine.INIT)
	@RequiresPermissions(AppChannelStatisticsDetailDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute(AppChannelStatisticsDetailDefine.FORM) AppChannelStatisticsDetailBean form) {
		LogUtil.startLog(AppChannelStatisticsDetailController.class.toString(), AppChannelStatisticsDetailDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(AppChannelStatisticsDetailDefine.LIST_PATH);
		// 获取登录用户的userId
		Integer userId = Integer.parseInt(ShiroUtil.getLoginUserId());
		// 根据用户Id查询渠道账号管理
		AdminUtmReadPermissions adminUtmReadPermissions = this.channelStatisticsDetailService.selectAdminUtmReadPermissions(userId);
		if (adminUtmReadPermissions != null) {
			form.setUtmIds(adminUtmReadPermissions.getUtmIds());// 封装到页面
		}
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(AppChannelStatisticsDetailController.class.toString(), AppChannelStatisticsDetailDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AppChannelStatisticsDetailDefine.SEARCH_ACTION)
	@RequiresPermissions(AppChannelStatisticsDetailDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, AppChannelStatisticsDetailBean form) {
		LogUtil.startLog(AppChannelStatisticsDetailController.class.toString(), AppChannelStatisticsDetailDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(AppChannelStatisticsDetailDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(AppChannelStatisticsDetailController.class.toString(), AppChannelStatisticsDetailDefine.INIT);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, AppChannelStatisticsDetailBean form) {
		AppChannelStatisticsDetailCustomize appChannelStatisticsCustomize = new AppChannelStatisticsDetailCustomize();
		// 页面是否展示渠道查询框
		int flag = 0;
		// 查询条件
		if (StringUtils.isNotEmpty(form.getUserNameSrch())) {
			appChannelStatisticsCustomize.setUserName(form.getUserNameSrch());
		}
		if (StringUtils.isNotEmpty(form.getSourceIdSrch())) {
			appChannelStatisticsCustomize.setSourceId(Integer.valueOf(form.getSourceIdSrch()));
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
			flag = 1;
		}
		Integer count = this.channelStatisticsDetailService.countList(appChannelStatisticsCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			appChannelStatisticsCustomize.setLimitStart(paginator.getOffset());
			appChannelStatisticsCustomize.setLimitEnd(paginator.getLimit());
			List<AppChannelStatisticsDetailCustomize> recordList = this.channelStatisticsDetailService.getRecordList(appChannelStatisticsCustomize);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}
		modelAndView.addObject("flag", flag);
		modelAndView.addObject("UtmPlatList", this.channelStatisticsDetailService.getAppUtm());
		modelAndView.addObject(AppChannelStatisticsDetailDefine.FORM, form);
	}

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(AppChannelStatisticsDetailDefine.EXPORT_ACTION)
	@RequiresPermissions(AppChannelStatisticsDetailDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, AppChannelStatisticsDetailBean form) throws Exception {
		LogUtil.startLog(AppChannelStatisticsDetailController.class.toString(), AppChannelStatisticsDetailDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "app渠道统计明细";

		AppChannelStatisticsDetailCustomize appChannelStatisticsCustomize = new AppChannelStatisticsDetailCustomize();
		// 查询条件
		if (StringUtils.isNotEmpty(form.getUserNameSrch())) {
			appChannelStatisticsCustomize.setUserName(form.getUserNameSrch());
		}
		if (StringUtils.isNotEmpty(form.getSourceIdSrch())) {
			appChannelStatisticsCustomize.setSourceId(Integer.valueOf(form.getSourceIdSrch()));
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
		List<AppChannelStatisticsDetailCustomize> recordList = this.channelStatisticsDetailService.exportList(appChannelStatisticsCustomize);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "序号", "渠道", "用户ID", "用户名", "性别", "注册时间", "开户时间", "首次出借时间", "首投项目类型", "首投项目期限", "首投金额", "累计出借金额" };
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
					AppChannelStatisticsDetailCustomize record = recordList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 渠道
					else if (celLength == 1) {
						cell.setCellValue(record.getSourceName());
					}
					// 用户ID
					else if (celLength == 2) {
						cell.setCellValue(record.getUserId());
					}
					// 用户名
					else if (celLength == 3) {
						cell.setCellValue(record.getUserName());
					}
					// 性别
					else if (celLength == 4) {
						cell.setCellValue(record.getSex());
					}
					// 注册时间
					else if (celLength == 5) {
						if (record.getRegisterTime() == null) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(GetDate.date2Str(record.getRegisterTime(), GetDate.datetimeFormat));
						}
					}
					// 开户时间
					else if (celLength == 6) {
						if (record.getOpenAccountTime() == null) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(GetDate.date2Str(record.getOpenAccountTime(), GetDate.datetimeFormat));
						}
					}
					// 首次出借时间
					else if (celLength == 7) {
						if (record.getFirstInvestTime() == null) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(GetDate.formatDateTime(record.getFirstInvestTime()));
						}
					}
					// 首投项目类型
					else if (celLength == 8) {
						if (StringUtils.isNotEmpty(record.getInvestProjectType())) {
							cell.setCellValue(record.getInvestProjectType());
						} else {
							cell.setCellValue("");
						}
					}
					// 首投项目期限
					else if (celLength == 9) {
						if (StringUtils.isNotEmpty(record.getInvestProjectPeriod())) {
							cell.setCellValue(record.getInvestProjectPeriod());
						} else {
							cell.setCellValue("");
						}
					}
					// 首投金额
					else if (celLength == 10) {
						cell.setCellValue(record.getInvestAmount() == null ? "0.00" : record.getInvestAmount().toString());
					}
					// 累计出借金额
					else if (celLength == 11) {
						cell.setCellValue(record.getCumulativeInvest().toString());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(AppChannelStatisticsDetailController.class.toString(), AppChannelStatisticsDetailDefine.EXPORT_ACTION);
	}
}
