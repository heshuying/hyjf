/**
 * Description:预约管理
 * Copyright: (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2016年8月1日 上午11:01:57
 * Modification History:
 * Modified by :
 * */

package com.hyjf.admin.manager.borrow.appoint;

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
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.admin.AdminBorrowAppointCustomize;

@Controller
@RequestMapping(value = BorrowAppointDefine.REQUEST_MAPPING)
public class BorrowAppointController extends BaseController {

	@Autowired
	private BorrowAppointService appointService;

	/**
	 * 预约管理
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowAppointDefine.APPOINT_LIST_ACTION)
	@RequiresPermissions(BorrowAppointDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request,
			@ModelAttribute(BorrowAppointDefine.APPOINT_LIST_FORM) BorrowAppointListCustomizeBean form) {
		LogUtil.startLog(BorrowAppointDefine.THIS_CLASS, BorrowAppointDefine.APPOINT_LIST_ACTION);
		ModelAndView modelAndView = new ModelAndView(BorrowAppointDefine.APPOINT_LIST_PATH);
		// 创建分页
		this.createPage(modelAndView, form);
		LogUtil.endLog(BorrowAppointDefine.THIS_CLASS, BorrowAppointDefine.APPOINT_LIST_ACTION);
		return modelAndView;
	}

	/**
	 * 创建权限维护分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(ModelAndView modelAndView, BorrowAppointListCustomizeBean form) {

		// 预约状态
		List<ParamName> appointStatusList = this.appointService.getParamNameList("APPOINT_STATUS");
		modelAndView.addObject("appointStatusList", appointStatusList);
		// 出借状态
		List<ParamName> tenderStatusList = this.appointService.getParamNameList("TENDER_STATUS");
		modelAndView.addObject("tenderStatusList", tenderStatusList);
		// 获取查询参数
		String orderId = StringUtils.isNotBlank(form.getOrderId()) ? "%" + form.getOrderId() + "%" : null;
		String tenderNid = StringUtils.isNotBlank(form.getTenderNid()) ? "%" + form.getTenderNid() + "%" : null;
		String borrowNid = StringUtils.isNotBlank(form.getBorrowNid()) ? "%" + form.getBorrowNid() + "%" : null;
		String userName = StringUtils.isNotBlank(form.getUserName()) ? "%" + form.getUserName() + "%" : null;
		String tenderStatus = StringUtils.isNotBlank(form.getTenderStatus()) ? form.getTenderStatus() : null;
		String appointStatus = StringUtils.isNotBlank(form.getAppointStatus()) ? form.getAppointStatus() : null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderId", orderId);
		params.put("tenderNid", tenderNid);
		params.put("borrowNid", borrowNid);
		params.put("userName", userName);
		params.put("tenderStatus", tenderStatus);
		params.put("appointStatus", appointStatus);
		params.put("appointTimeStart", form.getAppointTimeStart());
		params.put("appointTimeEnd", form.getAppointTimeEnd());
		params.put("cancelTimeStart", form.getCancelTimeStart());
		params.put("cancelTimeEnd", form.getCancelTimeEnd());
		int recordTotal = this.appointService.countRecordTotal(params);
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
			List<AdminBorrowAppointCustomize> recordList = this.appointService.getRecordList(params,paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(BorrowAppointDefine.APPOINT_LIST_ACTION, form);
		}
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
	@RequestMapping(BorrowAppointDefine.EXPORT_APPOINT_ACTION)
	@RequiresPermissions(BorrowAppointDefine.PERMISSIONS_EXPORT)
	public void exportExcel(@ModelAttribute BorrowAppointListCustomizeBean form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		LogUtil.startLog(BorrowAppointDefine.THIS_CLASS, BorrowAppointDefine.EXPORT_APPOINT_ACTION);
		// 表格sheet名称
		String sheetName = "预约记录";
		// 文件名称
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date())
				+ CustomConstants.EXCEL_EXT;
		// 获取查询参数
		String orderId = StringUtils.isNotBlank(form.getOrderId()) ? "%" + form.getOrderId() + "%" : null;
		String tenderNid = StringUtils.isNotBlank(form.getTenderNid()) ? "%" + form.getTenderNid() + "%" : null;
		String borrowNid = StringUtils.isNotBlank(form.getBorrowNid()) ? "%" + form.getBorrowNid() + "%" : null;
		String userName = StringUtils.isNotBlank(form.getUserName()) ? "%" + form.getUserName() + "%" : null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderId", orderId);
		params.put("tenderNid", tenderNid);
		params.put("borrowNid", borrowNid);
		params.put("userName", userName);
		params.put("appointTimeStart", form.getAppointTimeStart());
		params.put("appointTimeEnd", form.getAppointTimeEnd());
		params.put("cancelTimeStart", form.getCancelTimeStart());
		params.put("cancelTimeEnd", form.getCancelTimeEnd());

		// 需要输出的结果列表
		List<AdminBorrowAppointCustomize> recordList = this.appointService.getRecordList(params, -1, -1);
		String[] titles = new String[] { "序号", "用户名", "预约订单号", "出借订单号", "项目编号", "项目期限", "项目金额", "年华收益", "预约金额", "预约时间",
				"预约状态", "预约撤销时间", "发标时间", "出借状态", "备注" };
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
					sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles,
							(sheetName + "_第" + sheetCount + "页"));
					rowNum = 1;
				}
				// 新建一行
				Row row = sheet.createRow(rowNum);
				// 循环数据
				for (int celLength = 0; celLength < titles.length; celLength++) {
					AdminBorrowAppointCustomize user = recordList.get(i);
					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					if (celLength == 0) {// 序号
						cell.setCellValue(i + 1);
					} else if (celLength == 1) {// 用户名
						cell.setCellValue(user.getUserName());
					} else if (celLength == 2) {// 预约订单号
						cell.setCellValue(user.getOrderId());
					} else if (celLength == 3) {// 出借订单号
						cell.setCellValue(user.getTenderNid());
					} else if (celLength == 4) {// 项目编号
						cell.setCellValue(user.getBorrowNid());
					} else if (celLength == 5) {// 项目期限
						cell.setCellValue(user.getBorrowPeriod());
					} else if (celLength == 6) {// 项目金额
						cell.setCellValue(user.getBorrowAccount());
					} else if (celLength == 7) {// 年华收益
						cell.setCellValue(user.getBorrowApr());
					} else if (celLength == 8) {// 预约金额
						cell.setCellValue(user.getBorrowAccount());
					} else if (celLength == 9) {// 预约时间
						cell.setCellValue(user.getAppointTime());
					} else if (celLength == 10) {// 预约状态
						cell.setCellValue(user.getAppointStatusInfo());
					} else if (celLength == 11) {// 预约撤销时间
						cell.setCellValue(user.getCancelTime());
					} else if (celLength == 12) {// 发标时间
						cell.setCellValue(user.getVerifyTime());
					} else if (celLength == 13) {// 出借状态
						cell.setCellValue(user.getVerifyTime());
					} else if (celLength == 14) {// 备注
						if (!user.getAppointStatus().equals("1")) {
							if (StringUtils.isNotBlank(user.getAppointRemark())) {
								cell.setCellValue(user.getAppointRemark());
							} else {
								cell.setCellValue("");
							}
						} else if (user.getAppointStatus().equals("1")) {
							if (StringUtils.isNotBlank(user.getTenderStatus())) {
								if (!user.getTenderStatus().equals("1")) {
									cell.setCellValue(user.getTenderRemark());
								}
							} else {
								cell.setCellValue("");
							}
						}
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
		LogUtil.endLog(BorrowAppointDefine.THIS_CLASS, BorrowAppointDefine.EXPORT_APPOINT_ACTION);
	}
}
