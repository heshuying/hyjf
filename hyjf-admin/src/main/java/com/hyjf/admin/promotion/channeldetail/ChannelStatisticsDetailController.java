package com.hyjf.admin.promotion.channeldetail;

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
import com.hyjf.mybatis.model.customize.ChannelStatisticsDetailCustomize;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = ChannelStatisticsDetailDefine.REQUEST_MAPPING)
public class ChannelStatisticsDetailController extends BaseController {

	@Autowired
	private ChannelStatisticsDetailService channelStatisticsDetailService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ChannelStatisticsDetailDefine.INIT)
	@RequiresPermissions(ChannelStatisticsDetailDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute(ChannelStatisticsDetailDefine.FORM) ChannelStatisticsDetailBean form) {
		LogUtil.startLog(ChannelStatisticsDetailController.class.toString(), ChannelStatisticsDetailDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(ChannelStatisticsDetailDefine.LIST_PATH);
		// 获取登录用户的userId
		Integer userId = Integer.parseInt(ShiroUtil.getLoginUserId());
		// 根据用户Id查询渠道账号管理
		AdminUtmReadPermissions adminUtmReadPermissions = this.channelStatisticsDetailService.selectAdminUtmReadPermissions(userId);
		if (adminUtmReadPermissions != null) {
			form.setUtmIds(adminUtmReadPermissions.getUtmIds());// 封装到页面
		}
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(ChannelStatisticsDetailController.class.toString(), ChannelStatisticsDetailDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ChannelStatisticsDetailDefine.SEARCH_ACTION)
	@RequiresPermissions(ChannelStatisticsDetailDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, ChannelStatisticsDetailBean form) {
		LogUtil.startLog(ChannelStatisticsDetailController.class.toString(), ChannelStatisticsDetailDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(ChannelStatisticsDetailDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(ChannelStatisticsDetailController.class.toString(), ChannelStatisticsDetailDefine.INIT);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, ChannelStatisticsDetailBean form) {
		ChannelStatisticsDetailCustomize channelStatisticsCustomize = new ChannelStatisticsDetailCustomize();
		int flag = 0;
		// 查询条件
		// 渠道
		String[] utmIds = new String[] {};
		if (Validator.isNotNull(form.getUtmIds())) {
			if (form.getUtmIds().contains(StringPool.COMMA)) {
				utmIds = form.getUtmIds().split(StringPool.COMMA);
				channelStatisticsCustomize.setUtmIdsSrch(utmIds);
			} else {
				utmIds = new String[] { form.getUtmIds() };
				channelStatisticsCustomize.setUtmIdsSrch(utmIds);
			}
			flag = 1;
		}
		if (StringUtils.isNotEmpty(form.getUserNameSrch())) {
			channelStatisticsCustomize.setUserName(form.getUserNameSrch());
		}
		if (StringUtils.isNotEmpty(form.getSourceIdSrch())) {
			channelStatisticsCustomize.setSourceId(Integer.valueOf(form.getSourceIdSrch()));
		}
		if (StringUtils.isNotEmpty(form.getKeySrch())) {
			channelStatisticsCustomize.setKeyName(form.getKeySrch());
		}
		Integer count = this.channelStatisticsDetailService.countList(channelStatisticsCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			channelStatisticsCustomize.setLimitStart(paginator.getOffset());
			channelStatisticsCustomize.setLimitEnd(paginator.getLimit());
			List<ChannelStatisticsDetailCustomize> recordList = this.channelStatisticsDetailService.getRecordList(channelStatisticsCustomize);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}
		modelAndView.addObject("flag", flag);
		modelAndView.addObject("UtmPlatList", this.channelStatisticsDetailService.getPCUtm());
		modelAndView.addObject(ChannelStatisticsDetailDefine.FORM, form);
	}

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(ChannelStatisticsDetailDefine.EXPORT_ACTION)
	@RequiresPermissions(ChannelStatisticsDetailDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, ChannelStatisticsDetailBean form) throws Exception {
		LogUtil.startLog(ChannelStatisticsDetailController.class.toString(), ChannelStatisticsDetailDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "PC渠道统计明细";

		ChannelStatisticsDetailCustomize channelStatisticsCustomize = new ChannelStatisticsDetailCustomize();
		// 查询条件
		if (StringUtils.isNotEmpty(form.getUtmIds())) {
			String[] utmIds = new String[] {};
			if (form.getUtmIds().contains(StringPool.COMMA)) {
				utmIds = form.getUtmIds().split(StringPool.COMMA);
				channelStatisticsCustomize.setUtmIdsSrch(utmIds);
			} else {
				utmIds = new String[] { form.getUtmIds() };
				channelStatisticsCustomize.setUtmIdsSrch(utmIds);
			}
		}
		if (StringUtils.isNotEmpty(form.getUserNameSrch())) {
			channelStatisticsCustomize.setUserName(form.getUserNameSrch());
		}
		if (StringUtils.isNotEmpty(form.getSourceIdSrch())) {
			channelStatisticsCustomize.setSourceId(Integer.valueOf(form.getSourceIdSrch()));
		}
		if (StringUtils.isNotEmpty(form.getKeySrch())) {
			channelStatisticsCustomize.setKeyName(form.getKeySrch());
		}
		List<ChannelStatisticsDetailCustomize> recordList = this.channelStatisticsDetailService.exportList(channelStatisticsCustomize);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "序号", "推广链接ID", "渠道", "用户ID", "用户名", "性别", "注册时间", "开户时间", "首次出借时间", "首投项目类型", "首投项目期限", "首投金额", "累计出借金额" };
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
					ChannelStatisticsDetailCustomize record = recordList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 推广链接ID
					else if (celLength == 1) {
						cell.setCellValue(record.getUtmId());
					}
					// 渠道
					else if (celLength == 2) {
						cell.setCellValue(record.getSourceName());
					}
					// 用户ID
					else if (celLength == 3) {
						cell.setCellValue(record.getUserId());
					}
					// 用户名
					else if (celLength == 4) {
						cell.setCellValue(record.getUserName());
					}
					// 性别
					else if (celLength == 5) {
						cell.setCellValue(record.getSex());
					}
					// 注册时间
					else if (celLength == 6) {
						if (record.getRegisterTime() == null) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(record.getRegisterTime());
						}
					}
					// 开户时间
					else if (celLength == 7) {
						if (record.getOpenAccountTime() == null) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(record.getOpenAccountTime());
						}
					}
					// 首次出借时间
					else if (celLength == 8) {
						if (record.getFirstInvestTime() == null) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(record.getFirstInvestTime());
						}
					}
					// 首投项目类型
					else if (celLength == 9) {
						if (StringUtils.isNotEmpty(record.getInvestProjectType())) {
							cell.setCellValue(record.getInvestProjectType());
						} else {
							cell.setCellValue("");
						}
					}
					// 首投项目期限
					else if (celLength == 10) {
						if (StringUtils.isNotEmpty(record.getInvestProjectPeriod())) {
							cell.setCellValue(record.getInvestProjectPeriod());
						} else {
							cell.setCellValue("");
						}
					}
					// 首投金额
					else if (celLength == 11) {
						cell.setCellValue(record.getInvestAmount() == null ? "0.00" : record.getInvestAmount().toString());
					}
					// 累计出借金额
					else if (celLength == 12) {
						cell.setCellValue(record.getCumulativeInvest().add(record.getHtjInvest()).add(record.getHzrInvest()).toString());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(ChannelStatisticsDetailController.class.toString(), ChannelStatisticsDetailDefine.EXPORT_ACTION);
	}
}
