package com.hyjf.admin.promotion.app.reconciliation;

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
import com.hyjf.mybatis.model.auto.UtmPlat;
import com.hyjf.mybatis.model.customize.admin.AppChannelReconciliationCustomize;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * app渠道对账
 * 
 * @author Michael
 */
@Controller
@RequestMapping(value = AppChannelReconciliationDefine.REQUEST_MAPPING)
public class AppChannelReconciliationController extends BaseController {

	@Autowired
	private AppChannelReconciliationService channelReconciliationService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AppChannelReconciliationDefine.INIT)
	@RequiresPermissions(AppChannelReconciliationDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute(AppChannelReconciliationDefine.FORM) AppChannelReconciliationBean form) {
		LogUtil.startLog(AppChannelReconciliationController.class.toString(), AppChannelReconciliationDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(AppChannelReconciliationDefine.LIST_PATH);

		// 获取登录用户的userId
		Integer userId = Integer.parseInt(ShiroUtil.getLoginUserId());
		// 根据用户Id查询渠道账号管理
		AdminUtmReadPermissions adminUtmReadPermissions = this.channelReconciliationService.selectAdminUtmReadPermissions(userId);
		if (adminUtmReadPermissions != null) {
			form.setUtmIds(adminUtmReadPermissions.getUtmIds());// 封装到页面
		}

		// 默认前后一个月查询时间
		form.setTimeStartSrch(GetDate.date2Str(GetDate.getTodayBeforeOrAfter(-30), new SimpleDateFormat("yyyy-MM-dd")));
		form.setTimeEndSrch(GetDate.date2Str(new Date(), new SimpleDateFormat("yyyy-MM-dd")));

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(AppChannelReconciliationController.class.toString(), AppChannelReconciliationDefine.INIT);
		return modelAndView;
	}


	/**
	 * 画面初始化_汇计划
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AppChannelReconciliationDefine.HJH_INIT)
	@RequiresPermissions(AppChannelReconciliationDefine.PERMISSIONS_VIEW)
	public ModelAndView hjh_init(HttpServletRequest request, @ModelAttribute(AppChannelReconciliationDefine.HJH_FORM) AppChannelReconciliationBean form) {
		LogUtil.startLog(AppChannelReconciliationController.class.toString(), AppChannelReconciliationDefine.HJH_INIT);
		ModelAndView modelAndView = new ModelAndView(AppChannelReconciliationDefine.HJH_LIST_PATH);

		// 获取登录用户的userId
		Integer userId = Integer.parseInt(ShiroUtil.getLoginUserId());
		// 根据用户Id查询渠道账号管理
		AdminUtmReadPermissions adminUtmReadPermissions = this.channelReconciliationService.selectAdminUtmReadPermissions(userId);
		if (adminUtmReadPermissions != null) {
			form.setUtmIds(adminUtmReadPermissions.getUtmIds());// 封装到页面
		}

		// 默认前后一个月查询时间
		form.setTimeStartSrch(GetDate.date2Str(GetDate.getTodayBeforeOrAfter(-30), new SimpleDateFormat("yyyy-MM-dd")));
		form.setTimeEndSrch(GetDate.date2Str(new Date(), new SimpleDateFormat("yyyy-MM-dd")));

		// 创建分页
		this.createPageHjh(request, modelAndView, form);
		LogUtil.endLog(AppChannelReconciliationController.class.toString(), AppChannelReconciliationDefine.HJH_INIT);
		return modelAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AppChannelReconciliationDefine.SEARCH_ACTION)
	@RequiresPermissions(AppChannelReconciliationDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, AppChannelReconciliationBean form) {
		LogUtil.startLog(AppChannelReconciliationController.class.toString(), AppChannelReconciliationDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(AppChannelReconciliationDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(AppChannelReconciliationController.class.toString(), AppChannelReconciliationDefine.SEARCH_ACTION);
		return modelAndView;
	}


	/**
	 * 画面初始化_汇计划
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AppChannelReconciliationDefine.HJH_SEARCH_ACTION)
	@RequiresPermissions(AppChannelReconciliationDefine.PERMISSIONS_SEARCH)
	public ModelAndView searchHjh(HttpServletRequest request, AppChannelReconciliationBean form) {
		LogUtil.startLog(AppChannelReconciliationController.class.toString(), AppChannelReconciliationDefine.HJH_SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(AppChannelReconciliationDefine.HJH_LIST_PATH);

		// 创建分页
		this.createPageHjh(request, modelAndView, form);
		LogUtil.endLog(AppChannelReconciliationController.class.toString(), AppChannelReconciliationDefine.HJH_SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, AppChannelReconciliationBean form) {

		AppChannelReconciliationCustomize appChannelReconciliationCustomize = new AppChannelReconciliationCustomize();
		if (StringUtils.isNotEmpty(form.getTimeStartSrch())) {
			appChannelReconciliationCustomize.setTimeStartSrch(GetDate.getDayStart(form.getTimeStartSrch()));
		}
		if (StringUtils.isNotEmpty(form.getUtmIds())) {
			appChannelReconciliationCustomize.setUtmIds(form.getUtmIds());;
		}
		if (StringUtils.isNotEmpty(form.getTimeEndSrch())) {
			appChannelReconciliationCustomize.setTimeEndSrch(GetDate.getDayEnd(form.getTimeEndSrch()));
		}
		if (StringUtils.isNotEmpty(form.getUserNameSrch())) {
			appChannelReconciliationCustomize.setUserName(form.getUserNameSrch());
		}
		if (StringUtils.isNotEmpty(form.getOrderCodeSrch())) {
			appChannelReconciliationCustomize.setOrderCode(form.getOrderCodeSrch());
		}
		if (StringUtils.isNotEmpty(form.getBorrowNidSrch())) {
			appChannelReconciliationCustomize.setBorrowNid(form.getBorrowNidSrch());
		}
		if (form.getIsFirst() != null) {
			appChannelReconciliationCustomize.setIsFirst(form.getIsFirst());
		}
		if (StringUtils.isNotEmpty(form.getRegStartTime())) {
			appChannelReconciliationCustomize.setRegStartTime(GetDate.getDayStart(form.getRegStartTime()));
		}
		if (StringUtils.isNotEmpty(form.getRegEndTime())) {
			appChannelReconciliationCustomize.setRegEndTime(GetDate.getDayEnd(form.getRegEndTime()));
		}
		// 渠道
		String[] utmIds = new String[] {};
		if (Validator.isNotNull(form.getUtmIds())) {
			if (form.getUtmIds().contains(StringPool.COMMA)) {
				utmIds = form.getUtmIds().split(StringPool.COMMA);
				appChannelReconciliationCustomize.setUtmIdsSrch(utmIds);
			} else {
				utmIds = new String[] { form.getUtmIds() };
				appChannelReconciliationCustomize.setUtmIdsSrch(utmIds);
			}
		}
		//获取app渠道
		UtmPlat utmPlat=new UtmPlat();
		utmPlat.setSourceType(1);
		List<UtmPlat> utmtTypeList=this.channelReconciliationService.utmPlatListAppGet(utmPlat);
		form.setUtmtTypeList(utmtTypeList);
		modelAndView.addObject("utmtTypeList", utmtTypeList);
		Integer count = this.channelReconciliationService.countAppChannelReconciliationRecord(appChannelReconciliationCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			appChannelReconciliationCustomize.setLimitStart(paginator.getOffset());
			appChannelReconciliationCustomize.setLimitEnd(paginator.getLimit());
			List<AppChannelReconciliationCustomize> recordList = this.channelReconciliationService.selectAppChannelReconciliationRecord(appChannelReconciliationCustomize);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}
		modelAndView.addObject(AppChannelReconciliationDefine.FORM, form);
	}


	/**
	 * 创建分页机能_汇计划
	 *
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPageHjh(HttpServletRequest request, ModelAndView modelAndView, AppChannelReconciliationBean form) {

		AppChannelReconciliationCustomize appChannelReconciliationCustomize = new AppChannelReconciliationCustomize();
		if (StringUtils.isNotEmpty(form.getTimeStartSrch())) {
			appChannelReconciliationCustomize.setTimeStartSrch(GetDate.getDayStart(form.getTimeStartSrch()));
		}
		if (StringUtils.isNotEmpty(form.getUtmIds())) {
			appChannelReconciliationCustomize.setUtmIds(form.getUtmIds());;
		}
		if (StringUtils.isNotEmpty(form.getTimeEndSrch())) {
			appChannelReconciliationCustomize.setTimeEndSrch(GetDate.getDayEnd(form.getTimeEndSrch()));
		}
		if (StringUtils.isNotEmpty(form.getUserNameSrch())) {
			appChannelReconciliationCustomize.setUserName(form.getUserNameSrch());
		}
		if (StringUtils.isNotEmpty(form.getOrderCodeSrch())) {
			appChannelReconciliationCustomize.setOrderCode(form.getOrderCodeSrch());
		}
		if (StringUtils.isNotEmpty(form.getBorrowNidSrch())) {
			appChannelReconciliationCustomize.setBorrowNid(form.getBorrowNidSrch());
		}
		if (form.getIsFirst() != null) {
			appChannelReconciliationCustomize.setIsFirst(form.getIsFirst());
		}
		if (StringUtils.isNotEmpty(form.getRegStartTime())) {
			appChannelReconciliationCustomize.setRegStartTime(GetDate.getDayStart(form.getRegStartTime()));
		}
		if (StringUtils.isNotEmpty(form.getRegEndTime())) {
			appChannelReconciliationCustomize.setRegEndTime(GetDate.getDayEnd(form.getRegEndTime()));
		}
		// 渠道
		String[] utmIds = new String[] {};
		if (Validator.isNotNull(form.getUtmIds())) {
			if (form.getUtmIds().contains(StringPool.COMMA)) {
				utmIds = form.getUtmIds().split(StringPool.COMMA);
				appChannelReconciliationCustomize.setUtmIdsSrch(utmIds);
			} else {
				utmIds = new String[] { form.getUtmIds() };
				appChannelReconciliationCustomize.setUtmIdsSrch(utmIds);
			}
		}
		//获取app渠道
		UtmPlat utmPlat=new UtmPlat();
		utmPlat.setSourceType(1);
		List<UtmPlat> utmtTypeList=this.channelReconciliationService.utmPlatListAppGet(utmPlat);
		form.setUtmtTypeList(utmtTypeList);
		modelAndView.addObject("utmtTypeList", utmtTypeList);
		Integer count = this.channelReconciliationService.countAppChannelReconciliationRecordHjh(appChannelReconciliationCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			appChannelReconciliationCustomize.setLimitStart(paginator.getOffset());
			appChannelReconciliationCustomize.setLimitEnd(paginator.getLimit());
			List<AppChannelReconciliationCustomize> recordList = this.channelReconciliationService.selectAppChannelReconciliationRecordHjh(appChannelReconciliationCustomize);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}
		modelAndView.addObject(AppChannelReconciliationDefine.HJH_FORM, form);
	}

	/**
	 * 导出功能
	 * @param request
	 * @param response
	 * @param form
	 * @throws Exception
	 */
	@RequestMapping(AppChannelReconciliationDefine.EXPORT_ACTION)
	@RequiresPermissions(AppChannelReconciliationDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, AppChannelReconciliationBean form) throws Exception {
		LogUtil.startLog(AppChannelReconciliationController.class.toString(), AppChannelReconciliationDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "APP渠道对账_散标";

		AppChannelReconciliationCustomize appChannelReconciliationCustomize = new AppChannelReconciliationCustomize();

		if (StringUtils.isNotEmpty(form.getTimeStartSrch())) {
			appChannelReconciliationCustomize.setTimeStartSrch(GetDate.getDayStart(form.getTimeStartSrch()));
		}
		if (StringUtils.isNotEmpty(form.getTimeEndSrch())) {
			appChannelReconciliationCustomize.setTimeEndSrch(GetDate.getDayEnd(form.getTimeEndSrch()));
		}
		if (StringUtils.isNotEmpty(form.getUserNameSrch())) {
			appChannelReconciliationCustomize.setUserName(form.getUserNameSrch());
		}
		if (StringUtils.isNotEmpty(form.getOrderCodeSrch())) {
			appChannelReconciliationCustomize.setOrderCode(form.getOrderCodeSrch());
		}
		if (StringUtils.isNotEmpty(form.getBorrowNidSrch())) {
			appChannelReconciliationCustomize.setBorrowNid(form.getBorrowNidSrch());
		}
		if (form.getIsFirst() != null) {
			appChannelReconciliationCustomize.setIsFirst(form.getIsFirst());
		}
		if (StringUtils.isNotEmpty(form.getRegStartTime())) {
			appChannelReconciliationCustomize.setRegStartTime(GetDate.getDayStart(form.getRegStartTime()));
		}
		if (StringUtils.isNotEmpty(form.getRegEndTime())) {
			appChannelReconciliationCustomize.setRegEndTime(GetDate.getDayEnd(form.getRegEndTime()));
		}
		if (StringUtils.isNotEmpty(form.getUtmIds())) {
			String[] utmIds = new String[] {};
			if (form.getUtmIds().contains(StringPool.COMMA)) {
				utmIds = form.getUtmIds().split(StringPool.COMMA);
				appChannelReconciliationCustomize.setUtmIdsSrch(utmIds);
			} else {
				utmIds = new String[] { form.getUtmIds() };
				appChannelReconciliationCustomize.setUtmIdsSrch(utmIds);
			}
		}
		List<AppChannelReconciliationCustomize> recordList = this.channelReconciliationService.selectAppChannelReconciliationRecord(appChannelReconciliationCustomize);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "序号", "用户名", "渠道", "注册时间", "出借订单", "项目编号", "标的期限", "出借金额", "是否首投", "出借时间" };
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
					AppChannelReconciliationCustomize record = recordList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 用户名
					else if (celLength == 1) {
						cell.setCellValue(record.getUserName()==null?"":record.getUserName());
					}
					// 渠道
					else if (celLength == 2) {
						cell.setCellValue(record.getUtmName()==null?"":record.getUtmName());
					}
					// 注册时间
					else if (celLength == 3) {
						cell.setCellValue(record.getRegTime()==null?"":GetDate.timestamptoStrYYYYMMDDHHMM(record.getRegTime()));
					}
					// 出借订单
					else if (celLength == 4) {
						cell.setCellValue(record.getOrderCode()==null?"":record.getOrderCode());
					}
					// 项目编号
					else if (celLength == 5) {
						cell.setCellValue(record.getBorrowNid()==null?"":record.getBorrowNid());
					}
					// 标的期限
					else if (celLength == 6) {
						cell.setCellValue(record.getBorrowPeriod()==null?"":record.getBorrowPeriod());
					}
					// 出借金额
					else if (celLength == 7) {
						cell.setCellValue(record.getInvestAmount()==null?"":record.getInvestAmount());
					}
					// 出借金额
					else if (celLength == 8) {
						if(record.getIsFirst() != null&&record.getIsFirst().intValue()==1){
							cell.setCellValue("是");
						}else{
							cell.setCellValue("否");
						}
					}
					// 出借时间
					else if (celLength == 9) {
						cell.setCellValue(record.getInvestTime()==null?"":GetDate.timestamptoStrYYYYMMDDHHMM(String.valueOf(record.getInvestTime())));
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(AppChannelReconciliationController.class.toString(), AppChannelReconciliationDefine.EXPORT_ACTION);
	}



	/**
	 * 导出功能_汇计划
	 * @param request
	 * @param response
	 * @param form
	 * @throws Exception
	 */
	@RequestMapping(AppChannelReconciliationDefine.EXPORT_HJH_ACTION)
	@RequiresPermissions(AppChannelReconciliationDefine.PERMISSIONS_EXPORT)
	public void exportHjhAction(HttpServletRequest request, HttpServletResponse response, AppChannelReconciliationBean form) throws Exception {
		LogUtil.startLog(AppChannelReconciliationController.class.toString(), AppChannelReconciliationDefine.EXPORT_HJH_ACTION);
		// 表格sheet名称
		String sheetName = "APP渠道对账_智投服务";

		AppChannelReconciliationCustomize appChannelReconciliationCustomize = new AppChannelReconciliationCustomize();

		if (StringUtils.isNotEmpty(form.getTimeStartSrch())) {
			appChannelReconciliationCustomize.setTimeStartSrch(GetDate.getDayStart(form.getTimeStartSrch()));
		}
		if (StringUtils.isNotEmpty(form.getTimeEndSrch())) {
			appChannelReconciliationCustomize.setTimeEndSrch(GetDate.getDayEnd(form.getTimeEndSrch()));
		}
		if (StringUtils.isNotEmpty(form.getUserNameSrch())) {
			appChannelReconciliationCustomize.setUserName(form.getUserNameSrch());
		}
		if (StringUtils.isNotEmpty(form.getOrderCodeSrch())) {
			appChannelReconciliationCustomize.setOrderCode(form.getOrderCodeSrch());
		}
		if (StringUtils.isNotEmpty(form.getBorrowNidSrch())) {
			appChannelReconciliationCustomize.setBorrowNid(form.getBorrowNidSrch());
		}
		if (form.getIsFirst() != null) {
			appChannelReconciliationCustomize.setIsFirst(form.getIsFirst());
		}
		if (StringUtils.isNotEmpty(form.getRegStartTime())) {
			appChannelReconciliationCustomize.setRegStartTime(GetDate.getDayStart(form.getRegStartTime()));
		}
		if (StringUtils.isNotEmpty(form.getRegEndTime())) {
			appChannelReconciliationCustomize.setRegEndTime(GetDate.getDayEnd(form.getRegEndTime()));
		}
		if (StringUtils.isNotEmpty(form.getUtmIds())) {
			String[] utmIds = new String[] {};
			if (form.getUtmIds().contains(StringPool.COMMA)) {
				utmIds = form.getUtmIds().split(StringPool.COMMA);
				appChannelReconciliationCustomize.setUtmIdsSrch(utmIds);
			} else {
				utmIds = new String[] { form.getUtmIds() };
				appChannelReconciliationCustomize.setUtmIdsSrch(utmIds);
			}
		}
		List<AppChannelReconciliationCustomize> recordList = this.channelReconciliationService.selectAppChannelReconciliationRecordHjh(appChannelReconciliationCustomize);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "序号", "用户名", "渠道", "注册时间","智投订单号", "智投编号", "服务回报期限", "授权服务金额", "是否首投", "出借时间" };
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
					AppChannelReconciliationCustomize record = recordList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 用户名
					else if (celLength == 1) {
						cell.setCellValue(record.getUserName()==null?"":record.getUserName());
					}
					// 渠道
					else if (celLength == 2) {
						cell.setCellValue(record.getUtmName()==null?"":record.getUtmName());
					}
					// 注册时间
					else if (celLength == 3) {
						cell.setCellValue(record.getRegTime()==null?"":GetDate.timestamptoStrYYYYMMDDHHMM(record.getRegTime()));
					}
					// 出借订单
					else if (celLength == 4) {
						cell.setCellValue(record.getOrderCode()==null?"":record.getOrderCode());
					}
					// 项目编号
					else if (celLength == 5) {
						cell.setCellValue(record.getBorrowNid()==null?"":record.getBorrowNid());
					}
					// 标的期限
					else if (celLength == 6) {
						cell.setCellValue(record.getBorrowPeriod()==null?"":record.getBorrowPeriod());
					}
					// 出借金额
					else if (celLength == 7) {
						cell.setCellValue(record.getInvestAmount()==null?"":record.getInvestAmount());
					}
					// 是否首投
					else if (celLength == 8) {
						if(record.getIsFirst() != null&&record.getIsFirst().intValue()==1){
							cell.setCellValue("是");
						}else{
							cell.setCellValue("否");
						}
					}
					// 出借时间
					else if (celLength == 9) {
						cell.setCellValue(record.getInvestTime()==null?"":GetDate.timestamptoStrYYYYMMDDHHMM(String.valueOf(record.getInvestTime())));
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(AppChannelReconciliationController.class.toString(), AppChannelReconciliationDefine.EXPORT_HJH_ACTION);
	}
}
