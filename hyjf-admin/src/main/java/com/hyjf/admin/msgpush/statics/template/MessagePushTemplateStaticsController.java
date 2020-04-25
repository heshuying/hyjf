package com.hyjf.admin.msgpush.statics.template;

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
import com.hyjf.mybatis.model.auto.MessagePushTag;
import com.hyjf.mybatis.model.auto.MessagePushTemplateStatics;

/**
 * 推送异常列表页
 * 
 * @author 李深强
 *
 */
@Controller
@RequestMapping(value = MessagePushTemplateStaticsDefine.REQUEST_MAPPING)
public class MessagePushTemplateStaticsController extends BaseController {

	@Autowired
	private MessagePushTemplateStaticsService messagePushTemplateStaticsService ;
 
	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(MessagePushTemplateStaticsDefine.INIT)
	@RequiresPermissions(MessagePushTemplateStaticsDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(MessagePushTemplateStaticsDefine.FORM) MessagePushTemplateStaticsBean form) {
		LogUtil.startLog(MessagePushTemplateStaticsController.class.toString(), MessagePushTemplateStaticsDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(MessagePushTemplateStaticsDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(MessagePushTemplateStaticsController.class.toString(), MessagePushTemplateStaticsDefine.INIT);
		return modelAndView;
	}

	/**
	 * 条件查询
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(MessagePushTemplateStaticsDefine.SEARCH_ACTION)
	@RequiresPermissions(MessagePushTemplateStaticsDefine.PERMISSIONS_SEARCH)
	public ModelAndView select(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(MessagePushTemplateStaticsDefine.FORM) MessagePushTemplateStaticsBean form) {
		LogUtil.startLog(MessagePushTemplateStaticsController.class.toString(), MessagePushTemplateStaticsDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(MessagePushTemplateStaticsDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(MessagePushTemplateStaticsController.class.toString(), MessagePushTemplateStaticsDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 分页 
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, MessagePushTemplateStaticsBean form) {
		Integer count = this.messagePushTemplateStaticsService.getRecordCount(form);
		if(count > 0){
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			form.setPaginator(paginator);
			List<MessagePushTemplateStatics> recordList = this.messagePushTemplateStaticsService.getRecordList(form, paginator.getOffset(), paginator.getLimit());
			form.setRecordList(recordList);
			String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
			modelAndView.addObject("fileDomainUrl", fileDomainUrl);
		}
		// 标签
		List<MessagePushTag> tagList = this.messagePushTemplateStaticsService.getTagList();
		modelAndView.addObject("tagList", tagList);
		modelAndView.addObject(MessagePushTemplateStaticsDefine.FORM, form);
	}
	
	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(MessagePushTemplateStaticsDefine.EXPORT_ACTION)
	@RequiresPermissions(MessagePushTemplateStaticsDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, MessagePushTemplateStaticsBean form) throws Exception {
		LogUtil.startLog(MessagePushTemplateStaticsController.class.toString(), MessagePushTemplateStaticsDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "平台消息统计报表";
		List<MessagePushTemplateStatics> resultList = this.messagePushTemplateStaticsService.getRecordList(form, -1, -1);
		// 标签
		List<MessagePushTag> tagList = this.messagePushTemplateStaticsService.getTagList();
		
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
		String[] titles = new String[] {"序号", "标签", "标题","消息编码","统计时间","iOS目标用户|送达数|阅读数","iOS送达百分比 | 阅读百分比","Android目标用户|送达数|阅读数","Android送达百分比|阅读百分比" };
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
					MessagePushTemplateStatics tempInfo = resultList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					//标签
					else if (celLength == 1) {
						for(int j = 0; j < tagList.size(); j++){
							if(tempInfo.getTagId() == tagList.get(j).getId()){
								cell.setCellValue(tagList.get(j).getTagName());
							}
						}
					}
					else if (celLength == 2) {
						cell.setCellValue(tempInfo.getMsgTitle());
					}
					else if (celLength == 3) {
						cell.setCellValue(tempInfo.getMsgCode());
					}
					else if (celLength == 4) {
						cell.setCellValue(GetDate.timestamptoStrYYYYMMDDHHMMSS(tempInfo.getSendTime()));
					}
					else if (celLength == 5) {
						cell.setCellValue(tempInfo.getIosDestinationCount()+"|"+tempInfo.getIosSendCount()+"|"+tempInfo.getIosReadCount());
					}
					else if (celLength == 6) {
						if(tempInfo.getIosDestinationCount() == 0){
							cell.setCellValue("0.0% | 0.0%");
						}else{
							cell.setCellValue(tempInfo.getIosSendCount()/tempInfo.getIosDestinationCount() * 100 + "% |"+tempInfo.getIosReadCount()/tempInfo.getIosDestinationCount() * 100 + "%");
						}
					}
					else if (celLength == 7) {
						cell.setCellValue(tempInfo.getAndroidDestinationCount()+"|"+tempInfo.getAndroidSendCount()+"|"+tempInfo.getAndroidReadCount());
					}
					else if (celLength == 8) {
						if(tempInfo.getAndroidDestinationCount() == 0){
							cell.setCellValue("0.0% | 0.0%");
						}else{
							cell.setCellValue(tempInfo.getAndroidSendCount()/tempInfo.getAndroidDestinationCount() * 100 + "% |"+tempInfo.getAndroidReadCount()/tempInfo.getAndroidDestinationCount() * 100 + "%");
						}
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
		LogUtil.endLog(MessagePushTemplateStaticsController.class.toString(), MessagePushTemplateStaticsDefine.EXPORT_ACTION);
	}

	
}
