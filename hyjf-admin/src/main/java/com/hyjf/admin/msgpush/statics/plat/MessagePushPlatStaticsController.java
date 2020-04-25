package com.hyjf.admin.msgpush.statics.plat;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hyjf.admin.BaseController;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.MessagePushPlatStatics;
import com.hyjf.mybatis.model.auto.MessagePushTag;

/**
 * 推送异常列表页
 * 
 * @author 李深强
 *
 */
@Controller
@RequestMapping(value = MessagePushPlatStaticsDefine.REQUEST_MAPPING)
public class MessagePushPlatStaticsController extends BaseController {

	@Autowired
	private MessagePushPlatStaticsService messagePushPlatStaticsService;
 
	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(MessagePushPlatStaticsDefine.INIT)
	@RequiresPermissions(MessagePushPlatStaticsDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(MessagePushPlatStaticsDefine.FORM) MessagePushPlatStaticsBean form) {
		LogUtil.startLog(MessagePushPlatStaticsController.class.toString(), MessagePushPlatStaticsDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(MessagePushPlatStaticsDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(MessagePushPlatStaticsController.class.toString(), MessagePushPlatStaticsDefine.INIT);
		return modelAndView;
	}

	/**
	 * 条件查询
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(MessagePushPlatStaticsDefine.SEARCH_ACTION)
	@RequiresPermissions(MessagePushPlatStaticsDefine.PERMISSIONS_SEARCH)
	public ModelAndView select(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(MessagePushPlatStaticsDefine.FORM) MessagePushPlatStaticsBean form) {
		LogUtil.startLog(MessagePushPlatStaticsController.class.toString(), MessagePushPlatStaticsDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(MessagePushPlatStaticsDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(MessagePushPlatStaticsController.class.toString(), MessagePushPlatStaticsDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 分页 
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, MessagePushPlatStaticsBean form) {
		Integer count = this.messagePushPlatStaticsService.getRecordCount(form);
		if(count > 0){
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			form.setPaginator(paginator);
			List<MessagePushPlatStatics> recordList = this.messagePushPlatStaticsService.getRecordList(form, paginator.getOffset(), paginator.getLimit());
			form.setRecordList(recordList);
			String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
			modelAndView.addObject("fileDomainUrl", fileDomainUrl);
		}
		// 标签
		List<MessagePushTag> tagList = this.messagePushPlatStaticsService.getTagList();
		modelAndView.addObject("tagList", tagList);
		modelAndView.addObject(MessagePushPlatStaticsDefine.FORM, form);
	}
	
	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(MessagePushPlatStaticsDefine.EXPORT_ACTION)
	@RequiresPermissions(MessagePushPlatStaticsDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, MessagePushPlatStaticsBean form) throws Exception {
		LogUtil.startLog(MessagePushPlatStaticsController.class.toString(), MessagePushPlatStaticsDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "平台消息统计报表";
		List<MessagePushPlatStatics> resultList = this.messagePushPlatStaticsService.getRecordList(form, -1, -1);
		// 标签
		List<MessagePushTag> tagList = this.messagePushPlatStaticsService.getTagList();
		
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
//		String[] titles = new String[] {"序号", "标签", "标题","消息编码","发送时间","iOS目标用户|送达数|阅读数","iOS送达百分比 | 阅读百分比","Android目标用户|送达数|阅读数","Android送达百分比|阅读百分比" };
		
		String[] titles = new String[] {"序号", "标签", "日期","目标推送数","送达数","阅读数","送达百分比","iOS目标推送数","iOS送达数","iOS阅读数","iOS送达百分比","Android目标推送数","Android送达数","Android阅读数","Android送达百分比" };
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
					sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, (sheetName + "_第" + sheetCount + "页"));
					rowNum = 1;
				}

				// 新建一行
				Row row = sheet.createRow(rowNum);
				// 循环数据
				for (int celLength = 0; celLength < titles.length; celLength++) {
					MessagePushPlatStatics platInfo = resultList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					//标签
					else if (celLength == 1) {
						for(int j = 0; j < tagList.size(); j++){
							if(platInfo.getTagId() == tagList.get(j).getId()){
								cell.setCellValue(tagList.get(j).getTagName());
							}
						}
					}
					else if (celLength == 2) {
						cell.setCellValue(GetDate.times10toStrYYYYMMDD(platInfo.getStaDate()));
					}
					else if (celLength == 3) {
						cell.setCellValue(platInfo.getDestinationCount());
					}
					else if (celLength == 4) {
						cell.setCellValue(platInfo.getSendCount());
					}
					else if (celLength == 5) {
						cell.setCellValue(platInfo.getReadCount());
					}
					else if (celLength == 6) {
						if(platInfo.getDestinationCount() == 0){
							cell.setCellValue("0.0%");
						}else{
							cell.setCellValue(platInfo.getSendCount()/platInfo.getDestinationCount() * 100 + "%");
						}
					}
					else if (celLength == 7) {
						cell.setCellValue(platInfo.getIosDestinationCount());
					}
					else if (celLength == 8) {
						cell.setCellValue(platInfo.getIosSendCount());
					}
					else if (celLength == 9) {
						cell.setCellValue(platInfo.getIosReadCount());
					}
					else if (celLength == 10) {
						if(platInfo.getIosDestinationCount() == 0){
							cell.setCellValue("0.0%");
						}else{
							cell.setCellValue(platInfo.getIosSendCount()/platInfo.getIosDestinationCount() * 100 + "%");
						}
					}
					else if (celLength == 11) {
						cell.setCellValue(platInfo.getAndroidDestinationCount());
					}
					else if (celLength == 12) {
						cell.setCellValue(platInfo.getAndroidSendCount());
					}
					else if (celLength == 13) {
						cell.setCellValue(platInfo.getAndroidReadCount());
					}
					else if (celLength == 14) {
						if(platInfo.getAndroidDestinationCount() == 0){
							cell.setCellValue("0.0%");
						}else{
							cell.setCellValue(platInfo.getAndroidSendCount()/platInfo.getAndroidDestinationCount() * 100 + "%");
						}
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
		LogUtil.endLog(MessagePushPlatStaticsController.class.toString(), MessagePushPlatStaticsDefine.EXPORT_ACTION);
	}

	
	
}
