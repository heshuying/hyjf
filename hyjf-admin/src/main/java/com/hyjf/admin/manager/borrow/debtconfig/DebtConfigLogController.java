package com.hyjf.admin.manager.borrow.debtconfig;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.finance.bindlog.BindlogDefine;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.DebtConfig;
import com.hyjf.mybatis.model.auto.DebtConfigLog;
import com.hyjf.mybatis.model.auto.DirectionalTransferAssociatedLog;
import com.hyjf.mybatis.model.customize.AdminSystem;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * @author tanyy
 * @date 2018/08/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = DebtConfigDefine.PAGE_MAPPING)
public class DebtConfigLogController extends BaseController {


	@Autowired
	private DebtConfigService debtConfigService;

	/**
	 * 画面初始化
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(DebtConfigDefine.PAGE_INIT)
	@RequiresPermissions(DebtConfigDefine.PERMISSIONS_LOG_VIEW)
	public ModelAndView pageInit(HttpServletRequest request, DebtConfigBean form) {
		LogUtil.startLog(DebtConfigLogController.class.toString(), DebtConfigDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(DebtConfigDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(DebtConfigDefine.class.toString(), DebtConfigDefine.INIT);
		return modelAndView;
	}
	/**
	 * 创建分页机能
	 *
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, DebtConfigBean form) {
		Integer total = debtConfigService.countDebtConfigLogTotal();
		if (total > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), total);
			List<DebtConfigLog> list = debtConfigService.getDebtConfigLogList(form, paginator.getOffset(), paginator.getLimit());
			form.setRecordList(list);
			form.setPaginator(paginator);
		}
		modelAndView.addObject("form", form);
	}

	/**
	 * 导出
	 *
	 * @param
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(DebtConfigDefine.DEBTCONFIGLOG_EXPORT)
	public void debtconfiglogExport( HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogUtil.startLog(DebtConfigLogController.class.toString(), DebtConfigDefine.DEBTCONFIGLOG_EXPORT);
		// 表格sheet名称
		String sheetName = "汇转让配置记录";
		// 文件名称
		String fileName =
				sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		// 检索列表
		List<DebtConfigLog> resultList = debtConfigService.getDebtConfigLogList(new DebtConfigBean(), -1, -1);
		String[] titles =
				new String[] { "序号", "转让服务费率", "折让率下限", "折让率上限", "债转开关", "修改人", "修改时间", "IP地址"};
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
					sheet =
							ExportExcel
									.createHSSFWorkbookTitle(workbook, titles, (sheetName + "_第" + sheetCount + "页"));
					rowNum = 1;
				}
				// 新建一行
				Row row = sheet.createRow(rowNum);

				// 循环数据
				for (int celLength = 0; celLength < titles.length; celLength++) {
					DebtConfigLog log = resultList.get(i);
					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					if (celLength == 0) {// 序号
						cell.setCellValue(i + 1);
					} else if (celLength == 1) {
						cell.setCellValue(String.valueOf(log.getAttornRate()));
					} else if (celLength == 2) {
						cell.setCellValue(String.valueOf(log.getConcessionRateDown()));
					} else if (celLength == 3) {
						cell.setCellValue(String.valueOf(log.getConcessionRateUp()));
					} else if (celLength == 4) {
						cell.setCellValue(log.getToggle()==1?"开":"关");
					} else if (celLength == 5) {
						cell.setCellValue(log.getUpdateUserName());
					} else if (celLength == 6) {
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						cell.setCellValue(df.format(log.getUpdateTime()));
					} else if (celLength == 7) {
						cell.setCellValue(log.getIpAddress());
					}
				}
			}
		}

		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(BindlogDefine.THIS_CLASS, BindlogDefine.BIND_LOG_EXPORT);
	}
}
