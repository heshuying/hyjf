package com.hyjf.admin.invite.GetRecommend;

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
import com.hyjf.mybatis.model.customize.admin.GetRecommendCustomize;

/**
 * 10月份活动 获取推荐星明细列表
 * 
 * @author zhangjinpeng
 * 
 */
@Controller
@RequestMapping(value = GetRecommendDefine.REQUEST_MAPPING)
public class GetRecommendController extends BaseController {
	/**
	 * 类名
	 */
	private static final String THIS_CLASS = GetRecommendController.class.getName();

	@Autowired
	private GetRecommendService getRecommendService;

	/**
	 * 获取推荐星明细列表 初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(GetRecommendDefine.INIT)
	@RequiresPermissions(GetRecommendDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, GetRecommendBean form) {
		LogUtil.startLog(THIS_CLASS, GetRecommendDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(GetRecommendDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, GetRecommendDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面查询
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(GetRecommendDefine.SEARCH_ACTION)
	@RequiresPermissions(GetRecommendDefine.PERMISSIONS_VIEW)
	public ModelAndView search(HttpServletRequest request, GetRecommendBean form) {
		LogUtil.startLog(THIS_CLASS, GetRecommendDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(GetRecommendDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, GetRecommendDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 分页
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, GetRecommendBean form) {
		// 推荐星类别
		List<ParamName> recommendSourceList = this.getRecommendService.getParamNameList("RECOMMEND_SOURCE");
		modelAndView.addObject("recommendSourceList", recommendSourceList);
		modelAndView.addObject("sendFlagSrch", form.getSendFlagSrch());
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
		
		GetRecommendCustomize getRecommendCustomize = new GetRecommendCustomize();
		BeanUtils.copyProperties(form, getRecommendCustomize);
		Integer count = this.getRecommendService.getRecordTotal(getRecommendCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			getRecommendCustomize.setLimitStart(paginator.getOffset());
			getRecommendCustomize.setLimitEnd(paginator.getLimit());
			List<GetRecommendCustomize> recordList = this.getRecommendService.getRecordList(getRecommendCustomize);
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(GetRecommendDefine.INVITE_USER_FORM, form);
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
	@RequestMapping(GetRecommendDefine.EXPORT_INVITE_USER_ACTION)
	@RequiresPermissions(GetRecommendDefine.PERMISSIONS_EXPORT)
	public void exportInviteUser(@ModelAttribute GetRecommendBean form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		LogUtil.startLog(GetRecommendDefine.THIS_CLASS, GetRecommendDefine.EXPORT_INVITE_USER_ACTION);
		// 表格sheet名称
		String sheetName = "推荐星获取明细";
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
		
		List<GetRecommendCustomize> recordList = this.getRecommendService.getRecordList(form);
		String[] titles = new String[] { "序号", "用户名", "姓名", "手机号", "所属部门", "获得推荐星类型", "获得推荐星数量", "推荐好友", "推荐好友姓名", "推荐好友手机", "获得时间","是否发放" };
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
					GetRecommendCustomize user = recordList.get(i);
					String userName = StringUtils.isEmpty(user.getUserName())?StringUtils.EMPTY:user.getUserName();
					String trueName = StringUtils.isEmpty(user.getTrueName())?StringUtils.EMPTY:user.getTrueName();
					String mobile = StringUtils.isEmpty(user.getMobile())?StringUtils.EMPTY:user.getMobile();
					String departmentName = StringUtils.isEmpty(user.getDepartmentName())?StringUtils.EMPTY:user.getDepartmentName();
					String recommendSource = StringUtils.isEmpty(user.getRecommendSource())?StringUtils.EMPTY:user.getRecommendSource();
					Integer recommendCount = user.getRecommendCount()==null?0:user.getRecommendCount();
					String inviteByUserName = StringUtils.isEmpty(user.getInviteByUserName())?StringUtils.EMPTY:user.getInviteByUserName();
					String inviteByTrueName = StringUtils.isEmpty(user.getInviteByTrueName())?StringUtils.EMPTY:user.getInviteByTrueName();
					String inviteByMobile = StringUtils.isEmpty(user.getInviteByMobile())?StringUtils.EMPTY:user.getInviteByMobile();
					String sendTime = StringUtils.isEmpty(user.getSendTime())?StringUtils.EMPTY:user.getSendTime();
					String sendFlag = StringUtils.isEmpty(user.getSendFlag())?StringUtils.EMPTY:user.getSendFlag();
					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					if (celLength == 0) {// 序号
						cell.setCellValue(i + 1);
					} else if (celLength == 1) { // 用户名
						cell.setCellValue(userName);
					} else if (celLength == 2) { // 姓名
                        cell.setCellValue(trueName);
                    } else if (celLength == 3) { // 手机号
						cell.setCellValue(mobile);
					} else if (celLength == 4) { // 所属部门
						cell.setCellValue(departmentName);
					} else if (celLength == 5) {// 获得推荐星类型
						cell.setCellValue(recommendSource);
					} else if (celLength == 6) {// 获得推荐星数量
						cell.setCellValue(recommendCount);
					} else if (celLength == 7) {// 推荐好友
						cell.setCellValue(inviteByUserName);
					} else if (celLength == 8) {// 推荐好友姓名
                        cell.setCellValue(inviteByTrueName);
                    } else if (celLength == 9) {// 推荐好友手机
						cell.setCellValue(inviteByMobile);
					} else if (celLength == 10) {// 获得时间
						cell.setCellValue(sendTime);
					} else if (celLength == 11) {// 是否发放
						cell.setCellValue(sendFlag);
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
	public String getCrmDepartmentListAction(@RequestBody GetRecommendBean form) {
		// 部门
		String[] list = new String[] {};
		if (Validator.isNotNull(form.getIds())) {
			if (form.getIds().contains(StringPool.COMMA)) {
				list = form.getIds().split(StringPool.COMMA);
			} else {
				list = new String[] { form.getIds() };
			}
		}

		JSONArray ja = this.getRecommendService.getCrmDepartmentList(list);
		if (ja != null) {
			return ja.toString();
		}

		return StringUtils.EMPTY;
	}

}
