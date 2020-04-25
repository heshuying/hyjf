package com.hyjf.admin.invite.UsedRecommend;

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
import com.hyjf.mybatis.model.customize.admin.UsedRecommendCustomize;

/**
 * 10月份活动 使用推荐星明细列表
 * 
 * @author zhangjinpeng
 * 
 */
@Controller
@RequestMapping(value = UsedRecommendDefine.REQUEST_MAPPING)
public class UsedRecommendController extends BaseController {
	/**
	 * 类名
	 */
	private static final String THIS_CLASS = UsedRecommendController.class.getName();

	@Autowired
	private UsedRecommendService usedRecommendService;

	/**
	 * 使用推荐星明细列表初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(UsedRecommendDefine.INIT)
	@RequiresPermissions(UsedRecommendDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, UsedRecommendBean form) {
		LogUtil.startLog(THIS_CLASS, UsedRecommendDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(UsedRecommendDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, UsedRecommendDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面查询
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(UsedRecommendDefine.SEARCH_ACTION)
	@RequiresPermissions(UsedRecommendDefine.PERMISSIONS_VIEW)
	public ModelAndView search(HttpServletRequest request, UsedRecommendBean form) {
		LogUtil.startLog(THIS_CLASS, UsedRecommendDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(UsedRecommendDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, UsedRecommendDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 分页
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, UsedRecommendBean form) {
		// 推荐星类别
		List<ParamName> prizeKindList = this.usedRecommendService.getParamNameList("PRIZE_KIND");
		modelAndView.addObject("prizeKindList", prizeKindList);
		// 奖品发放状态
		modelAndView.addObject("prizeSendFlag", form.getPrizeSendFlagSrch());
		// 部门
		String[] list = new String[] {};
		if (Validator.isNotNull(form.getCombotreeSrch())) {
			if (form.getCombotreeSrch().contains(StringPool.COMMA)) {
				list = form.getCombotreeSrch().split(StringPool.COMMA);
				form.setCombotreeListSrch(list);
			} else {
				list = new String[] { form.getCombotreeSrch() };
				form.setCombotreeListSrch(list);
			}
		}
		
		UsedRecommendCustomize UsedRecommendCustomize = new UsedRecommendCustomize();
		BeanUtils.copyProperties(form, UsedRecommendCustomize);
		Integer count = this.usedRecommendService.getRecordTotal(UsedRecommendCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			UsedRecommendCustomize.setLimitStart(paginator.getOffset());
			UsedRecommendCustomize.setLimitEnd(paginator.getLimit());
			List<UsedRecommendCustomize> recordList = this.usedRecommendService.getRecordList(UsedRecommendCustomize);
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(UsedRecommendDefine.INVITE_USER_FORM, form);
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
	@RequestMapping(UsedRecommendDefine.EXPORT_INVITE_USER_ACTION)
	@RequiresPermissions(UsedRecommendDefine.PERMISSIONS_EXPORT)
	public void exportInviteUser(@ModelAttribute UsedRecommendBean form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		LogUtil.startLog(UsedRecommendDefine.THIS_CLASS, UsedRecommendDefine.EXPORT_INVITE_USER_ACTION);
		// 表格sheet名称
		String sheetName = "推荐星使用明细";
		// 文件名称
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date())
				+ CustomConstants.EXCEL_EXT;
		// 部门
		String[] list = new String[] {};
		if (Validator.isNotNull(form.getCombotreeSrch())) {
			if (form.getCombotreeSrch().contains(StringPool.COMMA)) {
				list = form.getCombotreeSrch().split(StringPool.COMMA);
				form.setCombotreeListSrch(list);
			} else {
				list = new String[] { form.getCombotreeSrch() };
				form.setCombotreeListSrch(list);
			}
		}
		
		List<UsedRecommendCustomize> recordList = this.usedRecommendService.getRecordList(form);
		String[] titles = new String[] { "序号", "用户名", "姓名", "手机号", "所属部门", "参与活动", "兑换数量", "使用推荐星", "获得奖品", "备注", "参与时间", "是否发放" };
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
					UsedRecommendCustomize user = recordList.get(i);
					String userName = StringUtils.isEmpty(user.getUserName())?StringUtils.EMPTY:user.getUserName();
					String trueName = StringUtils.isEmpty(user.getTrueName())?StringUtils.EMPTY:user.getTrueName();
					String mobile = StringUtils.isEmpty(user.getMobile())?StringUtils.EMPTY:user.getMobile();
					String departmentName = StringUtils.isEmpty(user.getDepartmentName())?StringUtils.EMPTY:user.getDepartmentName();
					String prizeKindName = StringUtils.isEmpty(user.getPrizeKindName())?StringUtils.EMPTY:user.getPrizeKindName();
					Integer prizeCount = user.getPrizeCount()==null?0:user.getPrizeCount();
					Integer usedRecommendCount = user.getUsedRecommendCount() == null?0:user.getUsedRecommendCount();
					String prizeName = StringUtils.isEmpty(user.getPrizeName())?StringUtils.EMPTY:user.getPrizeName();
					String remark = StringUtils.isEmpty(user.getRemark())?StringUtils.EMPTY:user.getRemark();
					String addTime = StringUtils.isEmpty(user.getAddTime())?StringUtils.EMPTY:user.getAddTime();
					String prizeSendFlag = StringUtils.isEmpty(user.getPrizeSendFlag())?StringUtils.EMPTY:user.getPrizeSendFlag();
					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					if (celLength == 0) {// 序号
						cell.setCellValue(i + 1);
					} else if (celLength == 1) { // 用户名
						cell.setCellValue(userName);
					} else if (celLength == 2) { // 用户名
                        cell.setCellValue(trueName);
                    } else if (celLength == 3) { // 手机号
						cell.setCellValue(mobile);
					} else if (celLength == 4) { // 所属部门
						cell.setCellValue(departmentName);
					} else if (celLength == 5) {// 参与活动
						cell.setCellValue(prizeKindName);
					} else if (celLength == 6) {// 兑换数量
						cell.setCellValue(prizeCount);
					} else if (celLength == 7) {// 使用推荐星
						cell.setCellValue(usedRecommendCount);
					} else if (celLength == 8) {// 获得奖品
						cell.setCellValue(prizeName);
					} else if (celLength == 9) {// 备注
						cell.setCellValue(remark);
					} else if (celLength == 10) {// 参与时间
						cell.setCellValue(addTime);
					} else if (celLength == 11) {// 是否发放
						cell.setCellValue(prizeSendFlag);
						
					} 
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
		LogUtil.endLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.EXPORT_USERS_ACTION);
	}
	
	/**
	 * 取得部门信息
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping("getCrmDepartmentList")
	@ResponseBody
	public String getCrmDepartmentListAction(@RequestBody UsedRecommendBean form) {
		// 部门
		String[] list = new String[] {};
		if (Validator.isNotNull(form.getIds())) {
			if (form.getIds().contains(StringPool.COMMA)) {
				list = form.getIds().split(StringPool.COMMA);
			} else {
				list = new String[] { form.getIds() };
			}
		}

		JSONArray ja = this.usedRecommendService.getCrmDepartmentList(list);
		if (ja != null) {
			return ja.toString();
		}

		return StringUtils.EMPTY;
	}

}
