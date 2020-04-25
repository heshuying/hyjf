package com.hyjf.wechat.controller.activity.activity0601;

import com.hyjf.common.security.utils.MD5Utils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import edu.emory.mathcs.backport.java.util.Arrays;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author xiasq
 * @version ExportExcelTool, v0.1 2018/5/28 9:46
 */
public class ExportExcelTool {
	private final static String fileName = "201806_exchage_code" + CustomConstants.EXCEL_EXT;
	private static Logger logger = LoggerFactory.getLogger(ExportExcelTool.class);

	private final static List<String> dateList = Arrays.asList(new String[] { "2018-05-30", "2018-05-31","2018-06-01", "2018-06-02", "2018-06-03",
			"2018-06-04", "2018-06-05", "2018-06-06", "2018-06-07", "2018-06-08", "2018-06-09", "2018-06-10",
			"2018-06-11", "2018-06-12", "2018-06-13", "2018-06-14", "2018-06-15", "2018-06-16", "2018-06-17",
			"2018-06-18", "2018-06-19", "2018-06-20", "2018-06-21", "2018-06-22", "2018-06-23", "2018-06-24",
			"2018-06-25", "2018-06-26", "2018-06-27", "2018-06-28", "2018-06-29", "2018-06-30" });

	public static void main(String[] args) throws IOException {
		String date = GetDate.formatDate();
		logger.info("date: " + date);
		generatorExchageCode(date);

		String[] titles = new String[] { "序号", "日期", "兑换码" };
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = createHSSFWorkbookTitle(workbook, titles, "第1页");
		// 构建excel主题
		buildWorkbook(titles, sheet);
		// 导出
		String filePath = "E:/" + fileName;
		writeExcelToDisk(filePath, workbook);
	}

	/**
	 * 构建excel主题
	 * 
	 * @param titles
	 * @param sheet
	 */
	private static void buildWorkbook(String[] titles, HSSFSheet sheet) {
		int rowNum = 0;
		for (int i = 0; i < dateList.size(); i++) {
			rowNum++;
			// 新建一行
			Row row = sheet.createRow(rowNum);
			// 循环数据
			for (int celLength = 0; celLength < titles.length; celLength++) {
				// 创建相应的单元格
				Cell cell = row.createCell(celLength);
				// 序号
				if (celLength == 0) {
					cell.setCellValue(i + 1);
				}
				// 日期
				else if (celLength == 1) {
					cell.setCellValue(dateList.get(i));
				}
				// 兑换码
				else if (celLength == 2) {
					cell.setCellValue(generatorExchageCode(dateList.get(i)));
				}
			}
		}
	}

	public static String generatorExchageCode(String initCode) {
		String cd5Code = MD5Utils.MD5(initCode);
		logger.info("cd5Code: " + cd5Code);
		String exchageCode = cd5Code.substring(0, 6);
		logger.info("exchageCode: " + exchageCode);
		return exchageCode;
	}

	/**
	 * 将excel文件存到指定位置
	 *
	 * @param filePath
	 * @param wb
	 */
	private static void writeExcelToDisk(String filePath, HSSFWorkbook wb) {
		try {
			FileOutputStream fout = new FileOutputStream(filePath);
			wb.write(fout);
			fout.close();
			logger.info("excel已经导出到:" + filePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建导出标题
	 * 
	 * @param workbook
	 * @param titles
	 * @param sheetName
	 * @return
	 */
	private static HSSFSheet createHSSFWorkbookTitle(HSSFWorkbook workbook, String[] titles, String sheetName) {

		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(sheetName);
		Row row = sheet.createRow(0);
		for (int celLength = 0; celLength < titles.length; celLength++) {
			// 创建相应的单元格
			Cell cell = row.createCell(celLength);
			cell.setCellValue(titles[celLength]);
		}
		return sheet;
	}
}
