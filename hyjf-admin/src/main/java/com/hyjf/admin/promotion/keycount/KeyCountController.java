package com.hyjf.admin.promotion.keycount;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.hyjf.mybatis.model.auto.UtmPlat;
import com.hyjf.mybatis.model.customize.KeyCountCustomize;

/**
 * @package com.hyjf.admin.promotion.keycount
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = KeyCountDefine.REQUEST_MAPPING)
public class KeyCountController extends BaseController {

	@Autowired
	private KeyCountService keyCountService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(KeyCountDefine.INIT)
	@RequiresPermissions(KeyCountDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute(KeyCountDefine.FORM) KeyCountBean form) {
		LogUtil.startLog(KeyCountController.class.toString(), KeyCountDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(KeyCountDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(KeyCountController.class.toString(), KeyCountDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(KeyCountDefine.SEARCH_ACTION)
	@RequiresPermissions(KeyCountDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, KeyCountBean form) {
		LogUtil.startLog(KeyCountController.class.toString(), KeyCountDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(KeyCountDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(KeyCountController.class.toString(), KeyCountDefine.INIT);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, KeyCountBean form) {

		List<UtmPlat> utmPlatList = this.keyCountService.getUtmPlat();
		modelAndView.addObject("utmPlatList", utmPlatList);

		KeyCountCustomize keyCountCustomize = new KeyCountCustomize();
		// 渠道
		keyCountCustomize.setSourceIdSrch(form.getSourceIdSrch());
		// 关键词
		keyCountCustomize.setKeyWordSrch(form.getKeyWordSrch());

		Integer count = this.keyCountService.countList(keyCountCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			keyCountCustomize.setLimitStart(paginator.getOffset());
			keyCountCustomize.setLimitEnd(paginator.getLimit());
			List<KeyCountCustomize> recordList = this.keyCountService.getRecordList(keyCountCustomize);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}
		modelAndView.addObject(KeyCountDefine.FORM, form);
	}

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(KeyCountDefine.EXPORT_ACTION)
	@RequiresPermissions(KeyCountDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, KeyCountBean form) throws Exception {
		LogUtil.startLog(KeyCountController.class.toString(), KeyCountDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "关键词统计";

		KeyCountCustomize keyCountCustomize = new KeyCountCustomize();
		// 渠道
		keyCountCustomize.setSourceIdSrch(form.getSourceIdSrch());
		// 关键词
		keyCountCustomize.setKeyWordSrch(form.getKeyWordSrch());

		List<KeyCountCustomize> recordList = this.keyCountService.exportList(keyCountCustomize);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "序号", "渠道", "关键词", "访问数", "注册数", "开户数" };
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
					KeyCountCustomize record = recordList.get(i);

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
					// 关键词
					else if (celLength == 2) {
						cell.setCellValue(record.getKeyWord());
					}
					// 访问数
					else if (celLength == 3) {
						cell.setCellValue(record.getAccessNumber());
					}
					// 注册数
					else if (celLength == 4) {
						cell.setCellValue(record.getRegistNumber());
					}
					// 开户数
					else if (celLength == 5) {
						cell.setCellValue(record.getAccountNumber());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
		LogUtil.endLog(KeyCountController.class.toString(), KeyCountDefine.EXPORT_ACTION);
	}
}
