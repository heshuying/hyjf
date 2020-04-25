/**
 * VIP管理
 * */

package com.hyjf.admin.vip.vipmanage;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.admin.VIPManageListCustomize;

@Controller
@RequestMapping(value = VIPManageDefine.REQUEST_MAPPING)
public class VIPManageController extends BaseController {

	@Resource
	private VIPManageService vipManageService;
	/**
	 * VIP管理列表
	 *
	 */
	@RequestMapping(VIPManageDefine.VIP_MANAGE_ACTION)
	@RequiresPermissions(VIPManageDefine.PERMISSIONS_VIEW)
	public ModelAndView searchUserList(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(VIPManageDefine.VIP_MANAGE_FORM) VIPManageCustomizeBean searchUser) {
		LogUtil.startLog(this.getClass().getName(), VIPManageDefine.VIP_MANAGE_ACTION);
		ModelAndView modelAndView = new ModelAndView(VIPManageDefine.VIP_MANAGE_PATH);
		// 创建分页
		this.createPage(request, modelAndView, searchUser);
		LogUtil.endLog(this.getClass().getName(), VIPManageDefine.VIP_MANAGE_ACTION);
		return modelAndView;
	}

	/**
	 * 创建权限维护分页机能
	 *
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, VIPManageCustomizeBean form) {

		// 用户角色
		List<ParamName> userRoles = this.vipManageService.getParamNameList("USER_ROLE");
		modelAndView.addObject("userRoles", userRoles);
		// 用户属性
		List<ParamName> userPropertys = this.vipManageService.getParamNameList("USER_PROPERTY");
		modelAndView.addObject("userPropertys", userPropertys);
		// 开户状态
		List<ParamName> accountStatus = this.vipManageService.getParamNameList("ACCOUNT_STATUS");
		modelAndView.addObject("accountStatus", accountStatus);
		// 用户状态
		List<ParamName> userStatus = this.vipManageService.getParamNameList("USER_STATUS");
		modelAndView.addObject("userStatus", userStatus);
		// 注册平台
		List<ParamName> registPlat = this.vipManageService.getParamNameList("CLIENT");
		modelAndView.addObject("registPlat", registPlat);
		// 51老用户
		List<ParamName> is51 = this.vipManageService.getParamNameList("IS_51");
		modelAndView.addObject("is51", is51);
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
		// 封装查询条件
		Map<String, Object> user = new HashMap<String, Object>();
		String userName = StringUtils.isNotEmpty(form.getUserName()) ? form.getUserName() : null;
		String realName = StringUtils.isNotEmpty(form.getRealName()) ? form.getRealName() : null;
		String mobile = StringUtils.isNotEmpty(form.getMobile()) ? form.getMobile() : null;
		String recommendName = StringUtils.isNotEmpty(form.getRecommendName()) ? form.getRecommendName() : null;
		String userRole = StringUtils.isNotEmpty(form.getUserRole()) ? form.getUserRole() : null;
		String userProperty = StringUtils.isNotEmpty(form.getUserProperty()) ? form.getUserProperty() : null;
		String accountStatusStr = StringUtils.isNotEmpty(form.getAccountStatus()) ? form.getAccountStatus() : null;
		String userStatusStr = StringUtils.isNotEmpty(form.getUserStatus()) ? form.getUserStatus() : null;
		String registPlatStr = StringUtils.isNotEmpty(form.getRegistPlat()) ? form.getRegistPlat() : null;
		String is51Str = StringUtils.isNotEmpty(form.getIs51()) ? form.getIs51() : null;
		String regTimeStart = StringUtils.isNotEmpty(form.getRegTimeStart()) ? form.getRegTimeStart() : null;
		String regTimeEnd = StringUtils.isNotEmpty(form.getRegTimeEnd()) ? form.getRegTimeEnd() : null;
		String[] combotreeListSrchStr = form.getCombotreeListSrch();
		if (form.getRegTimeStart() != null) {
			user.put("regTimeStart", regTimeStart);
		}
		if (form.getRegTimeEnd() != null) {
			user.put("regTimeEnd", regTimeEnd);
		}

		
		user.put("userName", userName);
		user.put("realName", realName);
		user.put("mobile", mobile);
		user.put("recommendName", recommendName);
		user.put("userRole", userRole);
		user.put("userProperty", userProperty);
		user.put("accountStatus", accountStatusStr);
		user.put("userStatus", userStatusStr);
		user.put("registPlat", registPlatStr);
		user.put("is51", is51Str);
		user.put("combotreeListSrch", combotreeListSrchStr);
		int recordTotal = this.vipManageService.countRecordTotal(user);
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
			List<VIPManageListCustomize> recordList = this.vipManageService.getRecordList(user, paginator.getOffset(),
					paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(VIPManageDefine.VIP_MANAGE_FORM, form);
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
	@RequestMapping(VIPManageDefine.EXPORT_VIP_ACTION)
	@RequiresPermissions(VIPManageDefine.PERMISSIONS_EXPORT)
	public void exportExcel(@ModelAttribute VIPManageCustomizeBean form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		LogUtil.startLog(this.getClass().getName(), VIPManageDefine.EXPORT_VIP_ACTION);
		// 表格sheet名称
		String sheetName = "VIP列表";
		// 文件名称
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date())
				+ CustomConstants.EXCEL_EXT;
		;
		// 需要输出的结果列表
		// 封装查询条件
		Map<String, Object> userMap = new HashMap<String, Object>();
		String regionName = StringUtils.isNotEmpty(form.getRegionName()) ? form.getRegionName() : null;
		String branchName = StringUtils.isNotEmpty(form.getBranchName()) ? form.getBranchName() : null;
		String departmentName = StringUtils.isNotEmpty(form.getDepartmentName()) ? form.getDepartmentName() : null;
		String userName = StringUtils.isNotEmpty(form.getUserName()) ? form.getUserName() : null;
		String realName = StringUtils.isNotEmpty(form.getRealName()) ? form.getRealName() : null;
		String mobile = StringUtils.isNotEmpty(form.getMobile()) ? form.getMobile() : null;
		String recommendName = StringUtils.isNotEmpty(form.getRecommendName()) ? form.getRecommendName() : null;
		String userRole = StringUtils.isNotEmpty(form.getUserRole()) ? form.getUserRole() : null;
		String userProperty = StringUtils.isNotEmpty(form.getUserProperty()) ? form.getUserProperty() : null;
		String accountStatusStr = StringUtils.isNotEmpty(form.getAccountStatus()) ? form.getAccountStatus() : null;
		String userStatusStr = StringUtils.isNotEmpty(form.getUserStatus()) ? form.getUserStatus() : null;
		String registPlatStr = StringUtils.isNotEmpty(form.getRegistPlat()) ? form.getRegistPlat() : null;
		String is51Str = StringUtils.isNotEmpty(form.getIs51()) ? form.getIs51() : null;
		String regTimeStart = StringUtils.isNotEmpty(form.getRegTimeStart()) ? form.getRegTimeStart() : null;
		String regTimeEnd = StringUtils.isNotEmpty(form.getRegTimeEnd()) ? form.getRegTimeEnd() : null;
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
		String[] combotreeListSrchStr = form.getCombotreeListSrch();
		if (form.getRegTimeStart() != null) {
			userMap.put("regTimeStart", regTimeStart);
		}
		if (form.getRegTimeEnd() != null) {
			userMap.put("regTimeEnd", regTimeEnd);
		}
		userMap.put("regionName", regionName);
		userMap.put("branchName", branchName);
		userMap.put("departmentName", departmentName);
		userMap.put("userName", userName);
		userMap.put("realName", realName);
		userMap.put("mobile", mobile);
		userMap.put("recommendName", recommendName);
		userMap.put("userRole", userRole);
		userMap.put("userProperty", userProperty);
		userMap.put("accountStatus", accountStatusStr);
		userMap.put("userStatus", userStatusStr);
		userMap.put("registPlat", registPlatStr);
		userMap.put("is51", is51Str);
		userMap.put("combotreeListSrch", combotreeListSrchStr);
		List<VIPManageListCustomize> recordList = this.vipManageService.getRecordList(userMap, -1, -1);
		String[] titles = new String[] { "序号", "分公司", "分部", "团队", "用户名", "姓名", "手机号码","VIP等级","V值","VIP购买时间", "用户角色", "用户属性", "推荐人", "51老用户",
				"用户状态", "开户状态","会员开通渠道", "注册平台", "注册时间" };
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
					VIPManageListCustomize user = recordList.get(i);
					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					if (celLength == 0) {// 序号
						cell.setCellValue(i + 1);
					} else if (celLength == 1) { // 大区
						cell.setCellValue(user.getRegionName());
					} else if (celLength == 2) { // 分公司
						cell.setCellValue(user.getBranchName());
					} else if (celLength == 3) { // 团队
						cell.setCellValue(user.getDepartmentName());
					} else if (celLength == 4) {// 用户名
						cell.setCellValue(user.getUserName());
					} else if (celLength == 5) {// 姓名
						cell.setCellValue(user.getRealName());
					} else if (celLength == 6) {// 手机号码
						cell.setCellValue(user.getMobile());
					} else if (celLength == 7) {// VIP等级名称
						cell.setCellValue(user.getVipName());
					} else if (celLength == 8) {// 累计V值
						cell.setCellValue(user.getVipValue());
					} else if (celLength == 9) {// VIP购买时间
                        cell.setCellValue(user.getVipAddTime());
                    } else if (celLength == 10) {// 用户角色
                        cell.setCellValue(user.getUserRole());
                    } else if (celLength == 11) {// 用户属性
						cell.setCellValue(user.getUserProperty());
					} else if (celLength == 12) {// 推荐人
						cell.setCellValue(user.getRecommendName());
					} else if (celLength == 13) {// 51老用户
						cell.setCellValue(user.getIs51());
					} else if (celLength == 14) {// 用户状态
						cell.setCellValue(user.getUserStatus());
					} else if (celLength == 15) {// 开户状态
						cell.setCellValue(user.getAccountStatus());
					}else if (celLength == 16) {// 会员开通渠道
						cell.setCellValue(user.getVipPlatform());
					} else if (celLength == 17) {// 注册平台
						cell.setCellValue(user.getRegistPlat());
					} else if (celLength == 18) {// 注册时间
						cell.setCellValue(user.getRegTime());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
		LogUtil.endLog(this.getClass().getName(), VIPManageDefine.EXPORT_VIP_ACTION);
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
	public String getCrmDepartmentListAction(@RequestBody VIPManageCustomizeBean form) {
		// 部门
		String[] list = new String[] {};
		if (Validator.isNotNull(form.getIds())) {
			if (form.getIds().contains(StringPool.COMMA)) {
				list = form.getIds().split(StringPool.COMMA);
			} else {
				list = new String[] { form.getIds() };
			}
		}

		JSONArray ja = this.vipManageService.getCrmDepartmentList(list);
		if (ja != null) {
			return ja.toString();
		}

		return StringUtils.EMPTY;
	}

}
