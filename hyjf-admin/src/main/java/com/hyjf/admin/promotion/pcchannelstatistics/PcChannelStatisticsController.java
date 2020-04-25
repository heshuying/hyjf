package com.hyjf.admin.promotion.pcchannelstatistics;

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
import com.hyjf.mybatis.model.customize.admin.PcChannelStatisticsCustomize;

/**
 * PC渠道统计Controller
 * 
 * @author liuyang
 *
 */
@Controller
@RequestMapping(PcChannelStatisticsDefine.REQUEST_MAPPING)
public class PcChannelStatisticsController extends BaseController {

	@Autowired
	private PcChannelStatisticsService pcChannelStatisticsService;
	/** 类名 */
	private static final String THIS_CLASS = PcChannelStatisticsController.class.getName();

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PcChannelStatisticsDefine.INIT)
	@RequiresPermissions(PcChannelStatisticsDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute(PcChannelStatisticsDefine.FORM) PcChannelStatisticsBean form) {
		LogUtil.startLog(THIS_CLASS, PcChannelStatisticsDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(PcChannelStatisticsDefine.LIST_PATH);
		// 获取登录用户的userId
		Integer userId = Integer.parseInt(ShiroUtil.getLoginUserId());
		// 根据用户Id查询渠道账号管理
		AdminUtmReadPermissions adminUtmReadPermissions = this.pcChannelStatisticsService.selectAdminUtmReadPermissions(userId);
		if (adminUtmReadPermissions != null) {
			form.setUtmIds(adminUtmReadPermissions.getUtmIds());
		}
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, PcChannelStatisticsDefine.INIT);
		return modelAndView;
	}

	/**
	 * 分页技能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, PcChannelStatisticsBean form) {
		PcChannelStatisticsCustomize pcChannelStatisticsCustomize = new PcChannelStatisticsCustomize();
		// 渠道
		String[] utmIds = new String[] {};
		if (Validator.isNotNull(form.getUtmIds())) {
			if (form.getUtmIds().contains(StringPool.COMMA)) {
				utmIds = form.getUtmIds().split(StringPool.COMMA);
				pcChannelStatisticsCustomize.setUtmIdsSrch(utmIds);
			} else {
				utmIds = new String[] { form.getUtmIds() };
				pcChannelStatisticsCustomize.setUtmIdsSrch(utmIds);
			}
		}
		if (StringUtils.isNotEmpty(form.getTimeStartSrch())) {
			pcChannelStatisticsCustomize.setTimeStartSrch(form.getTimeStartSrch());
		}
		if (StringUtils.isNotEmpty(form.getTimeEndSrch())) {
			pcChannelStatisticsCustomize.setTimeEndSrch(form.getTimeEndSrch());
		}
		// 件数
		Integer count = this.pcChannelStatisticsService.countList(pcChannelStatisticsCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			pcChannelStatisticsCustomize.setLimitStart(paginator.getOffset());
			pcChannelStatisticsCustomize.setLimitEnd(paginator.getLimit());
			List<PcChannelStatisticsCustomize> recordList = this.pcChannelStatisticsService.selectSumRecordList(pcChannelStatisticsCustomize);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}
		modelAndView.addObject(PcChannelStatisticsDefine.FORM, form);
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PcChannelStatisticsDefine.SEARCH_ACTION)
	@RequiresPermissions(PcChannelStatisticsDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, PcChannelStatisticsBean form) {
		LogUtil.startLog(THIS_CLASS, PcChannelStatisticsDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(PcChannelStatisticsDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, PcChannelStatisticsDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(PcChannelStatisticsDefine.EXPORT_ACTION)
	@RequiresPermissions(PcChannelStatisticsDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, PcChannelStatisticsBean form) throws Exception {
		LogUtil.startLog(THIS_CLASS, PcChannelStatisticsDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "渠道统计";

		PcChannelStatisticsCustomize pcChannelStatisticsCustomize = new PcChannelStatisticsCustomize();
		// 查询
		if (StringUtils.isNotEmpty(form.getUtmIds())) {
			String[] utmIds = new String[] {};
			if (form.getUtmIds().contains(StringPool.COMMA)) {
				utmIds = form.getUtmIds().split(StringPool.COMMA);
				pcChannelStatisticsCustomize.setUtmIdsSrch(utmIds);
			} else {
				utmIds = new String[] { form.getUtmIds() };
				pcChannelStatisticsCustomize.setUtmIdsSrch(utmIds);
			}
		}
		if (StringUtils.isNotEmpty(form.getTimeStartSrch())) {
			pcChannelStatisticsCustomize.setTimeStartSrch(form.getTimeStartSrch());
		}
		if (StringUtils.isNotEmpty(form.getTimeEndSrch())) {
			pcChannelStatisticsCustomize.setTimeEndSrch(form.getTimeEndSrch());
		}

		List<PcChannelStatisticsCustomize> recordList = this.pcChannelStatisticsService.exportList(pcChannelStatisticsCustomize);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "序号", "渠道", "访问数", "注册数", "开户数", "出借人数", "累计充值", "累计出借", "汇直投出借金额", "汇消费出借金额", "汇天利出借金额", "汇添金出借金额", "汇金理财出借金额", "汇转让出借金额" };
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
					PcChannelStatisticsCustomize record = recordList.get(i);
					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 平台
					else if (celLength == 1) {
						cell.setCellValue(record.getSourceName());
					}
					// 访问数
					else if (celLength == 2) {
						cell.setCellValue(record.getAccessNumber());
					}
					// 注册数
					else if (celLength == 3) {
						cell.setCellValue(record.getRegistNumber());
					}
					// 开户数
					else if (celLength == 4) {
						cell.setCellValue(record.getOpenAccountNumber());
					}
					// 出借人数
					else if (celLength == 5) {
						cell.setCellValue(record.getTenderNumber());
					}
					// 累计充值
					else if (celLength == 6) {
						cell.setCellValue(record.getCumulativeRecharge());
					}
					// 累计出借
					else if (celLength == 7) {
						cell.setCellValue(record.getCumulativeInvestment());
					}
					// 汇直投出借金额
					else if (celLength == 8) {
						cell.setCellValue(record.getHztTenderPrice());
					}
					// 汇消费出借金额
					else if (celLength == 9) {
						cell.setCellValue(record.getHxfTenderPrice());
					}
					// 汇天利出借金额
					else if (celLength == 10) {
						cell.setCellValue(record.getHtlTenderPrice());
					}
					// 汇添金出借金额
					else if (celLength == 11) {
						cell.setCellValue(record.getHtjTenderPrice());
					}
					// 汇金理财出借金额
					else if (celLength == 12) {
						cell.setCellValue(record.getRtbTenderPrice());
					}
					// 汇转让出借金额
					else if (celLength == 13) {
						cell.setCellValue(record.getHzrTenderPrice());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(THIS_CLASS, PcChannelStatisticsDefine.EXPORT_ACTION);
	}
}
