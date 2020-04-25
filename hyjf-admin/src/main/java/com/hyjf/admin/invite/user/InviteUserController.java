package com.hyjf.admin.invite.user;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import com.hyjf.mybatis.model.customize.admin.InviteUserCustomize;

/**
 * 10月份活动 用户邀请获得推荐星列表
 * 
 * @author zhangjinpeng
 * 
 */
@Controller
@RequestMapping(value = InviteUserDefine.REQUEST_MAPPING)
public class InviteUserController extends BaseController {
	/**
	 * 类名
	 */
	private static final String THIS_CLASS = InviteUserController.class.getName();

	@Autowired
	private InviteUserService inviteUserService;

	/**
	 * 账户设置画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(InviteUserDefine.INIT)
	@RequiresPermissions(InviteUserDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, InviteUserBean form) {
		LogUtil.startLog(THIS_CLASS, InviteUserDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(InviteUserDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, InviteUserDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面查询
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(InviteUserDefine.SEARCH_ACTION)
	@RequiresPermissions(InviteUserDefine.PERMISSIONS_VIEW)
	public ModelAndView search(HttpServletRequest request, InviteUserBean form) {
		LogUtil.startLog(THIS_CLASS, InviteUserDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(InviteUserDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, InviteUserDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 分页
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, InviteUserBean form) {
		// 用户属性
		List<ParamName> userPropertys = this.inviteUserService.getParamNameList("USER_PROPERTY");
		modelAndView.addObject("userPropertys", userPropertys);
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
		
		InviteUserCustomize inviteUserCustomize = new InviteUserCustomize();
		BeanUtils.copyProperties(form, inviteUserCustomize);
		Integer count = this.inviteUserService.getRecordTotal(inviteUserCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			inviteUserCustomize.setLimitStart(paginator.getOffset());
			inviteUserCustomize.setLimitEnd(paginator.getLimit());
			List<InviteUserCustomize> recordList = this.inviteUserService.getRecordList(inviteUserCustomize);
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(InviteUserDefine.INVITE_USER_FORM, form);
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
	@RequestMapping(InviteUserDefine.EXPORT_INVITE_USER_ACTION)
	@RequiresPermissions(InviteUserDefine.PERMISSIONS_EXPORT)
	public void exportInviteUser(@ModelAttribute InviteUserBean form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		LogUtil.startLog(InviteUserDefine.THIS_CLASS, InviteUserDefine.EXPORT_INVITE_USER_ACTION);
		// 表格sheet名称
		String sheetName = "参与活动账户信息";
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
		
		List<InviteUserCustomize> recordList = this.inviteUserService.getRecordList(form);
		String[] titles = new String[] { "序号", "用户名", "姓名", "手机号", "所属部门", "用户属性", "可用推荐星数量", "累计推荐星数量", "使用推荐星数量" };
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
					InviteUserCustomize user = recordList.get(i);
					String userName = StringUtils.isEmpty(user.getUserName())?StringUtils.EMPTY:user.getUserName();
					String trueName = StringUtils.isEmpty(user.getTrueName())?StringUtils.EMPTY:user.getTrueName();
					String mobile = StringUtils.isEmpty(user.getMobile())?StringUtils.EMPTY:user.getMobile();
					String departmentName = StringUtils.isEmpty(user.getDepartmentName())?StringUtils.EMPTY:user.getDepartmentName();
					String userAttrName = StringUtils.isEmpty(user.getUserAttrName())?StringUtils.EMPTY:user.getUserAttrName();
					Integer recommendValidCount = user.getRecommendValidCount()==null?0:user.getRecommendValidCount();
					Integer recommendAllCount = user.getRecommendAllCount()==null?0:user.getRecommendAllCount();
					Integer recommendUsedCount = user.getRecommendUsedCount()==null?0:user.getRecommendUsedCount();
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
					} else if (celLength == 5) {// 用户属性
						cell.setCellValue(userAttrName);
					} else if (celLength == 6) {// 可用推荐星数量
						cell.setCellValue(recommendValidCount);
					} else if (celLength == 7) {// 累计推荐星数量
						cell.setCellValue(recommendAllCount);
					} else if (celLength == 8) {// 使用推荐星数量
						cell.setCellValue(recommendUsedCount);
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
	public String getCrmDepartmentListAction(@RequestBody InviteUserBean form) {
		// 部门
		String[] list = new String[] {};
		if (Validator.isNotNull(form.getIds())) {
			if (form.getIds().contains(StringPool.COMMA)) {
				list = form.getIds().split(StringPool.COMMA);
			} else {
				list = new String[] { form.getIds() };
			}
		}

		JSONArray ja = this.inviteUserService.getCrmDepartmentList(list);
		if (ja != null) {
			return ja.toString();
		}

		return StringUtils.EMPTY;
	}

}
