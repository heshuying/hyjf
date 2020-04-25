package com.hyjf.admin.submissions;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.auto.SubmissionsWithBLOBs;
import com.hyjf.mybatis.model.customize.SubmissionsCustomize;

/**
 * 意见反馈
 * @package com.hyjf.admin.submissions
 * @author zhangjinpeng
 * @date 2016/03/07 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = SubmissionsDefine.SUBMISSIONS)
public class SubmissionsController extends BaseController {

	@Autowired
	private SubmissionsService submissionsService;

	/**
	 * 意见反馈一览
	 * 
	 * @param request
	 * @param form
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = SubmissionsDefine.QUERY_SUBMISSIONS_ACTION)
	@RequiresPermissions(SubmissionsDefine.PERMISSIONS_VIEW)
	public ModelAndView querySubmissions(HttpServletRequest request, SubmissionsBean form) {
		LogUtil.startLog(SubmissionsController.class.toString(), SubmissionsDefine.QUERY_SUBMISSIONS_ACTION);
		ModelAndView modelAndView = new ModelAndView(SubmissionsDefine.SUBMISSIONS_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(SubmissionsDefine.THIS_CLASS, SubmissionsDefine.QUERY_SUBMISSIONS_ACTION);
		return modelAndView;
		
	}
	
	/**
	 * 创建权限维护分页机能
	 *
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, SubmissionsBean form) {

		/*********************画面初期用****************/
		
		// 来源系统类别
		// 用户属性
		List<ParamName> clientPropertys = this.submissionsService.getParamNameList("CLIENT");
		//modelAndView.addObject("clientPropertys", clientPropertys);
		form.setSysTypeList(clientPropertys);
		
		/******************查询用**************/

		// 封装查询条件
		Map<String, Object> searchCon = new HashMap<String, Object>();
		// 用户名
		String userName = StringUtils.isNotEmpty(form.getUserName()) ? form.getUserName() : null;
		searchCon.put("userName", userName);
		form.setUserName(userName);
		// 系统类别
		String sysType = form.getSysType();
		searchCon.put("sysType", sysType);
		// 操作系统版本号
		/*String sysVersion = form.getSysVersion();
		searchCon.put("sysVersion",sysVersion);*/
		// 平台版本号
		String platformVersion = form.getPlatformVersion();
		searchCon.put("platformVersion", platformVersion);
		// 手机型号
		String phoneType = form.getPhoneType();
		searchCon.put("phoneType", phoneType);
		// 反馈内容
		/*String content = form.getContent();
		searchCon.put("content", content);*/
		// 添加时间-开始
		String addTimeStart = form.getAddTimeStart();
		if(StringUtils.isNotEmpty(addTimeStart)){
			searchCon.put("addTimeStart", addTimeStart);
		}
		// 添加时间-结束
		String addTimeEnd = form.getAddTimeEnd();
		if(StringUtils.isNotEmpty(addTimeEnd)){
			searchCon.put("addTimeEnd", addTimeEnd);
		}
		// 取得数据总数 分页用
		int recordTotal = this.submissionsService.countRecordTotal(searchCon);
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
			// 根据分页取得数据
			List<SubmissionsCustomize> submissionsList = this.submissionsService.queryRecordList(searchCon, paginator.getOffset(),
					paginator.getLimit());
			form.setPaginator(paginator);
			form.setSubmissionsList(submissionsList);
			
		}
		modelAndView.addObject(SubmissionsDefine.SUBMISSIONS_LIST_FORM, form);
	}
	
	/**
	 * 根据业务需求导出相应的表格 此处暂时为可用情况 缺陷： 1.无法指定相应的列的顺序， 2.无法配置，excel文件名，excel sheet名称
	 * 3.目前只能导出一个sheet 4.列的宽度的自适应，中文存在一定问题
	 * 5.根据导出的业务需求最好可以在导出的时候输入起止页码，因为在大数据量的情况下容易造成卡顿
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(SubmissionsDefine.EXPORT_LIST_ACTION)
	@RequiresPermissions(SubmissionsDefine.PERMISSIONS_EXPORT)
	public void exportListAction(HttpServletRequest request, SubmissionsBean form,
			HttpServletResponse response) throws Exception {

		LogUtil.startLog(SubmissionsDefine.THIS_CLASS, SubmissionsDefine.EXPORT_LIST_ACTION);
		// 表格sheet名称
		String sheetName = "意见反馈列表";
		// 文件名称
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date())
				+ CustomConstants.EXCEL_EXT;
		;
		// 需要输出的结果列表
		// 封装查询条件
		Map<String, Object> searchCon = new HashMap<String, Object>();
		// 用户名
		String userName = StringUtils.isNotEmpty(form.getUserName()) ? form.getUserName() : null;
		searchCon.put("userName", userName);
		form.setUserName(userName);
		// 系统类别
		String sysType = form.getSysType();
		searchCon.put("sysType", sysType);
		// 操作系统版本号
		String sysVersion = form.getSysVersion();
		searchCon.put("sysVersion",sysVersion);
		// 平台版本号
		String platformVersion = form.getPlatformVersion();
		searchCon.put("platformVersion", platformVersion);
		// 手机型号
		String phoneType = form.getPhoneType();
		searchCon.put("phoneType", phoneType);
		// 反馈内容
		String content = form.getContent();
		searchCon.put("content", content);
		// 添加时间-开始
		String addTimeStart = form.getAddTimeStart();
		if(StringUtils.isNotEmpty(addTimeStart)){
			searchCon.put("addTimeStart", addTimeStart);
		}
		// 添加时间-结束
		String addTimeEnd = form.getAddTimeEnd();
		if(StringUtils.isNotEmpty(addTimeEnd)){
			searchCon.put("addTimeEnd", addTimeEnd);
		}
		List<SubmissionsCustomize> submissionsList = this.submissionsService.queryRecordList(searchCon, -1, -1);
		String[] titles = new String[] { "序号", "用户名", "操作系统", "版本号", "手机型号", "反馈内容", "时间" };
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, sheetName + "_第1页");

		if (submissionsList != null && submissionsList.size() > 0) {

			int sheetCount = 1;
			int rowNum = 0;

			for (int i = 0; i < submissionsList.size(); i++) {
				rowNum++;
				if (i != 0 && i % 60000 == 0) {
					sheetCount++;
					sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles,
							(sheetName + "_第" + sheetCount + "页"));
					rowNum = 1;
				}

				// 新建一行
				Row row = sheet.createRow(rowNum);
				// 循环数据
				for (int celLength = 0; celLength < titles.length; celLength++) {
					SubmissionsCustomize submissions = submissionsList.get(i);
					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					if (celLength == 0) {// 序号
						cell.setCellValue(i + 1);
					} else if (celLength == 1) { // 用户名
						cell.setCellValue(submissions.getUserName());
					} else if (celLength == 2) { // 系统
						cell.setCellValue(submissions.getSysType());
					} else if (celLength == 3) { // 平台版本号
						cell.setCellValue(submissions.getPlatformVersion());
					} else if (celLength == 4) {// 手机型号
						cell.setCellValue(submissions.getPhoneType());
					} else if (celLength == 5) {// 反馈内容
						cell.setCellValue(submissions.getContent());
					} else if (celLength == 6) {// 时间
						cell.setCellValue(submissions.getAddTime());
					} 
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
		LogUtil.endLog(SubmissionsDefine.THIS_CLASS, SubmissionsDefine.EXPORT_LIST_ACTION);
	}
	
	/**
	 * 意见反馈编辑画面跳转
	 * 
	 * @param request
	 * @param form
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = SubmissionsDefine.EDIT_SUBMISSIONS_ACTION)
	@RequiresPermissions(SubmissionsDefine.PERMISSIONS_MODIFY)
	public ModelAndView editSubmissions(HttpServletRequest request, SubmissionsBean form) {
		LogUtil.startLog(SubmissionsController.class.toString(), SubmissionsDefine.QUERY_SUBMISSIONS_ACTION);
		ModelAndView modelAndView = new ModelAndView(SubmissionsDefine.SUBMISSIONS_REPLY_PATH);
		// 取得意见反馈编号
		String subMissionsId = form.getSubmissionsId();
		if(StringUtils.isEmpty(subMissionsId)){
			return null;
		}
		// 根据意见反馈编号取得意见反馈
		SubmissionsWithBLOBs submissionsAuto = submissionsService.querySelectedSubmissions(subMissionsId);
		modelAndView.addObject("submissionsAuto", submissionsAuto);
		LogUtil.endLog(SubmissionsDefine.THIS_CLASS, SubmissionsDefine.QUERY_SUBMISSIONS_ACTION);
		return modelAndView;
		
	}
	
	/**
	 * 意见反馈更新保存
	 * 
	 * @param request
	 * @param form
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = SubmissionsDefine.UPDATE_SUBMISSIONS_ACTION)
	@RequiresPermissions(SubmissionsDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateSubmissions(HttpServletRequest request) {
		LogUtil.startLog(SubmissionsController.class.toString(), SubmissionsDefine.UPDATE_SUBMISSIONS_ACTION);
		ModelAndView modelAndView = new ModelAndView(SubmissionsDefine.SUBMISSIONS_REPLY_PATH);
		// 取得处理状态
		String status = request.getParameter("status");
		// 取得意见反馈编号
		String submissionsId = request.getParameter("submissionsId");
		if(StringUtils.isEmpty(submissionsId)||StringUtils.isEmpty(status)){
			return null;
		}
		// 更新意见反馈
		SubmissionsWithBLOBs submissions = new SubmissionsWithBLOBs();
		submissions.setId(Integer.valueOf(submissionsId));
		submissions.setState(Integer.valueOf(status));
		submissionsService.updateSubmissions(submissions);
		// 往画面传递success，使当前dialog画面关闭
		modelAndView.addObject(SubmissionsDefine.SUCCESS, SubmissionsDefine.SUCCESS);
		LogUtil.endLog(SubmissionsDefine.THIS_CLASS, SubmissionsDefine.UPDATE_SUBMISSIONS_ACTION);
		return modelAndView;
		
	}

}
