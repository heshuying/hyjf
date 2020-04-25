package com.hyjf.admin.invite.ActdecList;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.user.manageruser.ManageUsersDefine;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.admin.act.ActdecSpringListCustomize;

/**
 * 10月份活动 获取推荐星明细列表
 * 
 * @author zhangjinpeng
 * 
 */
@Controller
@RequestMapping(value = ActdecListDefine.REQUEST_MAPPING)
public class ActdecListController extends BaseController {
	/**
	 * 类名
	 */
	private static final String THIS_CLASS = ActdecListController.class.getName();

	@Autowired
	private ActdecListService getRecommendService;

	/**
	 * 获取推荐星明细列表 初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ActdecListDefine.INIT)
	@RequiresPermissions(ActdecListDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, ActdecListBean form) {
		LogUtil.startLog(THIS_CLASS, ActdecListDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(ActdecListDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, ActdecListDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面查询
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ActdecListDefine.SEARCH_ACTION)
	@RequiresPermissions(ActdecListDefine.PERMISSIONS_VIEW)
	public ModelAndView search(HttpServletRequest request, ActdecListBean form) {
		LogUtil.startLog(THIS_CLASS, ActdecListDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(ActdecListDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, ActdecListDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 分页
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, ActdecListBean form) {
		
		ActdecSpringListCustomize getRecommendCustomize = new ActdecSpringListCustomize();
		BeanUtils.copyProperties(form, getRecommendCustomize);
		Integer count = this.getRecommendService.getRecordTotal(getRecommendCustomize);
		if (count != null && count > 0) {
			if(form.getPaginatorPage()==0){
				form.setPaginatorPage(1);
			}
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			getRecommendCustomize.setLimitStart(paginator.getOffset());
			getRecommendCustomize.setLimitEnd(paginator.getLimit());
			List<ActdecSpringListCustomize> recordList = this.getRecommendService.getRecordList(getRecommendCustomize);
			form.setPaginator(paginator);
			form.setRecordList(recordList);
		}
		modelAndView.addObject(ActdecListDefine.INVITE_USER_FORM, form);
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
	@RequestMapping(ActdecListDefine.EXPORT_INVITE_USER_ACTION)
	@RequiresPermissions(ActdecListDefine.PERMISSIONS_EXPORT)
	public void exportInviteUser(@ModelAttribute ActdecListBean form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		LogUtil.startLog(ActdecListDefine.THIS_CLASS, ActdecListDefine.EXPORT_INVITE_USER_ACTION);
		// 表格sheet名称
		String sheetName = "春节活动明细";
		// 文件名称
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date())
				+ CustomConstants.EXCEL_EXT;
		List<ActdecSpringListCustomize> recordList = this.getRecommendService.getRecordList(form);
		String[] titles = new String[] { "序号", "用户名", "手机号", "操作", "操作金额", "当前新增金额", "新增出借余额", "碎片变更", "获得奖励", "累计获得碎片", "可使用碎片","时间" };
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
					ActdecSpringListCustomize user = recordList.get(i);
					String userName = StringUtils.isEmpty(user.getUserName())?StringUtils.EMPTY:user.getUserName();
					String userMobile = StringUtils.isEmpty(user.getUserMobile())?StringUtils.EMPTY:user.getUserMobile();
					String operType = StringUtils.isEmpty(user.getOperType())?StringUtils.EMPTY:user.getOperType();
					Integer operAmount = user.getOperAmount();
					Integer newRecharge = user.getNewRecharge();
					Integer newInvestment = user.getNewInvestment()==null?0:user.getNewInvestment();
					Integer number = user.getNumber();
					String reward = user.getReward();
					Integer totalNumber = user.getTotalNumber();
					Integer availableNumber = user.getAvailableNumber();
					String createTime = StringUtils.isEmpty(user.getCreateTime())?StringUtils.EMPTY:user.getCreateTime();
					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					if (celLength == 0) {// 序号
						cell.setCellValue(i + 1);
					} else if (celLength == 1) { // 用户名
						cell.setCellValue(userName);
					} else if (celLength == 2) { // 手机号
                        cell.setCellValue(userMobile);
                    } else if (celLength == 3) { // 操作
						cell.setCellValue(operType);
					} else if (celLength == 4) { // 操作金额
						cell.setCellValue(operAmount);
					} else if (celLength == 5) {// 当前新增金额
						cell.setCellValue(newRecharge);
					} else if (celLength == 6) {// 新增出借余额
						cell.setCellValue(newInvestment);
					} else if (celLength == 7) {// 碎片变更
						cell.setCellValue(number);
					} else if (celLength == 8) {// 获得奖励
                        cell.setCellValue(reward);
                    } else if (celLength == 9) {// 累计获得碎片
						cell.setCellValue(totalNumber);
					} else if (celLength == 10) {// 可使用碎片
						cell.setCellValue(availableNumber);
					} else if (celLength == 11) {// 时间
						cell.setCellValue(createTime);
					} 
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
		LogUtil.endLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.EXPORT_USERS_ACTION);
	}

}
