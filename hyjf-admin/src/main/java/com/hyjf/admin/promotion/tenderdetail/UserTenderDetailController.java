package com.hyjf.admin.promotion.tenderdetail;

import java.math.BigDecimal;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.user.manageruser.ManageUsersDefine;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.customize.admin.UserTenderDetailCustomize;

/**
 * 投之家用户出借明细
 * 
 * @author zhangjinpeng
 * 
 */
@Controller
@RequestMapping(value = UserTenderDetailDefine.REQUEST_MAPPING)
public class UserTenderDetailController extends BaseController {
	/**
	 * 类名
	 */
	private static final String THIS_CLASS = UserTenderDetailController.class.getName();

	@Autowired
	private UserTenderDetailService userTenderDetailService;

	/**
	 * 投之家用户出借明细列表初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(UserTenderDetailDefine.INIT)
	@RequiresPermissions(UserTenderDetailDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, UserTenderDetailBean form) {
		LogUtil.startLog(THIS_CLASS, UserTenderDetailDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(UserTenderDetailDefine.LIST_PATH);
		//当前月份减一
		//页面初始化的时候，默认查询当前时间往前推一个月内的数据　update by jijun 2018/03/15　
		form.setRegTime(String.valueOf(GetDate.countDate(GetDate.getNowTime10(),2,-1)));
		
		//回显注册时间regTimeStartSrch
		String todaySub1Month=GetDate.getCountDate(2, -1);
		form.setRegTimeStartSrch(todaySub1Month);
		//回显注册时间regTimeEndSrch
		String regTimeEndSrch=GetDate.date2Str(GetDate.date_sdf);
		form.setRegTimeEndSrch(regTimeEndSrch);
		
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, UserTenderDetailDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面查询
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(UserTenderDetailDefine.SEARCH_ACTION)
	@RequiresPermissions(UserTenderDetailDefine.PERMISSIONS_VIEW)
	public ModelAndView search(HttpServletRequest request, UserTenderDetailBean form) {
		LogUtil.startLog(THIS_CLASS, UserTenderDetailDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(UserTenderDetailDefine.LIST_PATH);
		if (validatorFieldValueCheck(modelAndView, form) != null) {
			// 失败返回
			return validatorFieldValueCheck(modelAndView, form);
		}
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, UserTenderDetailDefine.SEARCH_ACTION);
		return modelAndView;
	}

	
	/**
	 * 属性值的校验 add by jijun 20180316
	 * @param modelAndView
	 * @param form
	 * @return
	 */
	private ModelAndView validatorFieldValueCheck(ModelAndView modelAndView, UserTenderDetailBean form) {
		// 字段校验(非空判断和长度判断)
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "regTimeStartSrch", form.getRegTimeStartSrch())) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "regTimeEndSrch", form.getRegTimeEndSrch())) {
			return modelAndView;
		}
		//校验差值是否越过阈值 add by jijun 2018/03/15
		if(!ValidatorFieldCheckUtil.validateExceedThreshold(modelAndView,form.getRegTimeStartSrch(),form.getRegTimeEndSrch(),UserTenderDetailDefine.THRESHOLD_MONTH)){
			return modelAndView;
		}
		
		return null;
	}
	
	
	/**
	 * 分页
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, UserTenderDetailBean form) {
		
		UserTenderDetailCustomize userTenderDetailCustomize = new UserTenderDetailCustomize();
		BeanUtils.copyProperties(form, userTenderDetailCustomize);
		Integer count = this.userTenderDetailService.getRecordTotal(userTenderDetailCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			userTenderDetailCustomize.setLimitStart(paginator.getOffset());
			userTenderDetailCustomize.setLimitEnd(paginator.getLimit());
			List<UserTenderDetailCustomize> recordList = this.userTenderDetailService.getRecordList(userTenderDetailCustomize);
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			BigDecimal tenderAccountTotal = this.userTenderDetailService.getTenderAccountTotal(userTenderDetailCustomize);
			form.setTenderAccountTotal(tenderAccountTotal);
			modelAndView.addObject(UserTenderDetailDefine.USER_TENDER_DETAIL_FORM, form);
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
	@RequestMapping(UserTenderDetailDefine.EXPORT_USER_TENDER_DETAIL_ACTION)
	@RequiresPermissions(UserTenderDetailDefine.PERMISSIONS_EXPORT)
	public void exportInviteUser(@ModelAttribute UserTenderDetailBean form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		LogUtil.startLog(UserTenderDetailDefine.THIS_CLASS, UserTenderDetailDefine.EXPORT_USER_TENDER_DETAIL_ACTION);
		// 表格sheet名称
		String sheetName = "投之家用户出借明细";
		// 文件名称
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date())
				+ CustomConstants.EXCEL_EXT;
		
		List<UserTenderDetailCustomize> recordList = this.userTenderDetailService.getRecordList(form);
		String[] titles = new String[] { "序号", "用户名", "渠道", "注册时间", "开户时间", "出借金额", "项目编号", "标的期限", "出借时间" };
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
					UserTenderDetailCustomize userTenderDetail = recordList.get(i);
					String userName = StringUtils.isEmpty(userTenderDetail.getUserName())?StringUtils.EMPTY:userTenderDetail.getUserName();
					String pcUtmSource = StringUtils.isEmpty(userTenderDetail.getPcUtmSource())?StringUtils.EMPTY:userTenderDetail.getPcUtmSource();
					String appUtmSource = StringUtils.isEmpty(userTenderDetail.getAppUtmSource())?StringUtils.EMPTY:userTenderDetail.getAppUtmSource();
					String regTime = StringUtils.isEmpty(userTenderDetail.getRegTime())?StringUtils.EMPTY:userTenderDetail.getRegTime();
					String openAccountTime = StringUtils.isEmpty(userTenderDetail.getOpenAccountTime())?StringUtils.EMPTY:userTenderDetail.getOpenAccountTime();
					BigDecimal tenderAccount = userTenderDetail.getTenderAccount()==null?BigDecimal.ZERO:userTenderDetail.getTenderAccount();
					String borrowNid = StringUtils.isEmpty(userTenderDetail.getBorrowNid())?StringUtils.EMPTY:userTenderDetail.getBorrowNid();
					String borrowPeriod = StringUtils.isEmpty(userTenderDetail.getBorrowPeriod())?StringUtils.EMPTY:userTenderDetail.getBorrowPeriod();
					String tenderTime = StringUtils.isEmpty(userTenderDetail.getTenderTime())?StringUtils.EMPTY:userTenderDetail.getTenderTime();
					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					if (celLength == 0) {// 序号
						cell.setCellValue(i + 1);
					} else if (celLength == 1) { // 用户名
						cell.setCellValue(userName);
					} else if (celLength == 2) { // 渠道名称
						if(StringUtils.isNotEmpty(appUtmSource)){
							cell.setCellValue(appUtmSource);
						}else{
							cell.setCellValue(pcUtmSource);
						}
                    } else if (celLength == 3) { // 注册时间
						cell.setCellValue(regTime);
					} else if (celLength == 4) { // 开户时间
						cell.setCellValue(openAccountTime);
					} else if (celLength == 5) {// 出借金额
						cell.setCellValue(tenderAccount.toString());
					} else if (celLength == 6) {// 项目编号
						cell.setCellValue(borrowNid);
					} else if (celLength == 7) {// 项目编号
                        cell.setCellValue(borrowPeriod);
                    } else if (celLength == 8) {// 出借时间
						cell.setCellValue(tenderTime);
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
		LogUtil.endLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.EXPORT_USERS_ACTION);
	}

}
