/**
 * Description:风险测评
 * Copyright: (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 逄成超
 * @version: 1.0
 * Created at: 2016年03月22日 上午11:01:57
 * Modification History:
 * Modified by :
 * */

package com.hyjf.admin.manager.user.evaluation;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.user.manageruser.ManageUsersService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.UserEvalationResultCustomize;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.admin.AdminUserEvalationCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminUserEvalationResultCustomize;

@Controller
@RequestMapping(value = EvaluationDefine.REQUEST_MAPPING)
public class EvaluationController extends BaseController {

	@Resource
	private EvaluationService evaluationService;

	@Resource
	private ManageUsersService usersService;

	@RequestMapping(EvaluationDefine.EVALUATION_LIST_ACTION)
	public ModelAndView searchUserList(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(EvaluationDefine.USERS_LIST_FORM) EvaluationListCustomizeBean form) {
		LogUtil.startLog(EvaluationDefine.THIS_CLASS, EvaluationDefine.EVALUATION_LIST_ACTION);
		ModelAndView modelAndView = new ModelAndView(EvaluationDefine.EVALUATION_LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(EvaluationDefine.THIS_CLASS, EvaluationDefine.EVALUATION_LIST_ACTION);
		return modelAndView;
	}

	/*
	 * 创建权限维护分页机能
	 *
	 * @param request
	 * 
	 * @param modelAndView
	 * 
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, EvaluationListCustomizeBean form) {

		// 用户属性
		List<ParamName> userPropertys = this.evaluationService.getParamNameList("USER_PROPERTY");
		modelAndView.addObject("userPropertys", userPropertys);
		// 开户状态
		List<ParamName> accountStatus = this.evaluationService.getParamNameList("ACCOUNT_STATUS");
		modelAndView.addObject("accountStatus", accountStatus);
		// 51老用户
		List<ParamName> is51 = this.evaluationService.getParamNameList("IS_51");
		modelAndView.addObject("is51", is51);

		// 测评状态
		List<ParamName> evaluationStatus = createEvaluationStatus();
		modelAndView.addObject("evaluationStatus", evaluationStatus);

		// 测评等级
		List<ParamName> evaluationType = createEvaluationType();
		modelAndView.addObject("evaluationType", evaluationType);

		// 封装查询条件
		Map<String, Object> user = new HashMap<String, Object>();
		String userName = StringUtils.isNotEmpty(form.getUserName()) ? form.getUserName() : null;
		String realName = StringUtils.isNotEmpty(form.getRealName()) ? form.getRealName() : null;
		String mobile = StringUtils.isNotEmpty(form.getMobile()) ? form.getMobile() : null;
		String userProperty = StringUtils.isNotEmpty(form.getUserProperty()) ? form.getUserProperty() : null;
		String accountStatusStr = StringUtils.isNotEmpty(form.getAccountStatus()) ? form.getAccountStatus() : null;
		String is51Str = StringUtils.isNotEmpty(form.getIs51()) ? form.getIs51() : null;
		String evaluationStatusStr = StringUtils.isNotEmpty(form.getEvaluationStatus()) ? form.getEvaluationStatus() : null;
		String evaluationTypeStr = StringUtils.isNotEmpty(form.getEvaluationType()) ? form.getEvaluationType() : null;

//		if (request.getMethod().equalsIgnoreCase("GET")) {
//			try {
//				if (userName != null) {
//					userName = new String(userName.getBytes("iso8859-1"), "utf-8");
//					form.setUserName(userName);
//				}
//				if (evaluationStatusStr != null) {
//					evaluationStatusStr = new String(evaluationStatusStr.getBytes("iso8859-1"), "utf-8");
//					form.setEvaluationStatus(evaluationStatusStr);
//				}
//				if (realName != null) {
//					realName = new String(realName.getBytes("iso8859-1"), "utf-8");
//					form.setRealName(realName);
//				}
//				if (evaluationTypeStr != null) {
//					evaluationTypeStr = new String(evaluationTypeStr.getBytes("iso8859-1"), "utf-8");
//					form.setEvaluationType(evaluationTypeStr);
//				}
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
//		}

		user.put("userName", userName);
		user.put("realName", realName);
		user.put("mobile", mobile);
		user.put("userProperty", userProperty);
		user.put("accountStatus", accountStatusStr);
		user.put("is51", is51Str);
		user.put("evaluationStatus", evaluationStatusStr);
		user.put("evaluationType", evaluationTypeStr);
		int recordTotal = this.evaluationService.countRecordTotal(user);
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
			List<AdminUserEvalationResultCustomize> recordList = this.evaluationService.getRecordList(user, paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(EvaluationDefine.USERS_LIST_FORM, form);
		}

		modelAndView.addObject("userName", userName);
		modelAndView.addObject("realName", realName);
		modelAndView.addObject("mobile", mobile);
		modelAndView.addObject("userProperty", userProperty);
		modelAndView.addObject("accountStatus", accountStatus);
		modelAndView.addObject("is51", is51Str);
		modelAndView.addObject("evaluationStatus", evaluationStatus);
		modelAndView.addObject("evaluationType", evaluationType);
	}

	private List<ParamName> createEvaluationType() {
		List<ParamName> list = new ArrayList<ParamName>();
		ParamName paramName1 = new ParamName();
		paramName1.setNameCd("保守型");
		list.add(paramName1);
		ParamName paramName3 = new ParamName();
		paramName3.setNameCd("稳健型");
		list.add(paramName3);
		ParamName paramName5 = new ParamName();
		paramName5.setNameCd("激进型");
		list.add(paramName5);
		return list;
	}

	private List<ParamName> createEvaluationStatus() {
		List<ParamName> list = new ArrayList<ParamName>();
		ParamName paramName1 = new ParamName();
		paramName1.setNameCd("已测评");
		list.add(paramName1);
		ParamName paramName2 = new ParamName();
		paramName2.setNameCd("未测评");
		list.add(paramName2);
		ParamName paramName3 = new ParamName();
		paramName3.setNameCd("已过期");
		list.add(paramName3);
		return list;
	}

	/**
	 * 用户详情
	 *
	 * @param request
	 * @return 进入用户详情页面
	 */
	@RequestMapping(EvaluationDefine.EVALUATION_DETAIL_ACTION)
	public ModelAndView searchUserDetail(HttpServletRequest request) {
		LogUtil.startLog(EvaluationDefine.THIS_CLASS, EvaluationDefine.EVALUATION_DETAIL_ACTION);
		ModelAndView modelAndView = new ModelAndView(EvaluationDefine.EVALUATION_DETAIL_PATH);
		// 获取用户id
		String userIdStr = request.getParameter("userId");
		int userId = Integer.parseInt(userIdStr);
		// 根据用户id查询用户详情信息
		UserEvalationResultCustomize userEvalationResultCustomize = evaluationService.selectUserEvalationResultByUserId(userId);
		if (userEvalationResultCustomize != null && userEvalationResultCustomize.getUserId() == userId) {
			modelAndView.addObject(EvaluationDefine.JSON_IF_EVALUATION_KEY, EvaluationDefine.FLG_AVTIVE);
			modelAndView.addObject(EvaluationDefine.JSON_USER_EVALATION_RESULT_KEY, userEvalationResultCustomize);
			List<AdminUserEvalationCustomize> list = evaluationService.getUserEvalation(userEvalationResultCustomize.getId());
			modelAndView.addObject("list", list);
			// modelAndView.addObject("listSize", list.size());
			Users users = evaluationService.getUsersByUserId(userId);
			modelAndView.addObject("user", users);
		} else {

			modelAndView.addObject(EvaluationDefine.JSON_IF_EVALUATION_KEY, EvaluationDefine.FLG_DISABLE);

		}
		// modelAndView.addObject(EvaluationDefine.USERS_DETAIL_FORM, user);
		LogUtil.endLog(EvaluationDefine.THIS_CLASS, EvaluationDefine.EVALUATION_DETAIL_ACTION);
		return modelAndView;

	}

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(EvaluationDefine.EXPORT_ACTION)
	@RequiresPermissions(EvaluationDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, EvaluationListCustomizeBean form) throws Exception {
		LogUtil.startLog(EvaluationController.class.toString(), EvaluationDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "用户测评结果列表";
		// 封装查询条件
		Map<String, Object> user = new HashMap<String, Object>();
		String userName = StringUtils.isNotEmpty(form.getUserName()) ? form.getUserName() : null;
		String realName = StringUtils.isNotEmpty(form.getRealName()) ? form.getRealName() : null;
		String mobile = StringUtils.isNotEmpty(form.getMobile()) ? form.getMobile() : null;
		String userProperty = StringUtils.isNotEmpty(form.getUserProperty()) ? form.getUserProperty() : null;
		String accountStatusStr = StringUtils.isNotEmpty(form.getAccountStatus()) ? form.getAccountStatus() : null;
		String is51Str = StringUtils.isNotEmpty(form.getIs51()) ? form.getIs51() : null;
		String evaluationStatusStr = StringUtils.isNotEmpty(form.getEvaluationStatus()) ? form.getEvaluationStatus() : null;
		String evaluationTypeStr = StringUtils.isNotEmpty(form.getEvaluationType()) ? form.getEvaluationType() : null;

		user.put("userName", userName);
		user.put("realName", realName);
		user.put("mobile", mobile);
		user.put("userProperty", userProperty);
		user.put("accountStatus", accountStatusStr);
		user.put("is51", is51Str);
		user.put("evaluationStatus", evaluationStatusStr);
		user.put("evaluationType", evaluationTypeStr);
		List<AdminUserEvalationResultCustomize> resultList = this.evaluationService.exoportRecordList(user);

		/*
		 * List<CouponConfigCustomize> resultList = this.couponConfigService
		 * .exoportRecordList(couponConfigCustomize);
		 */
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
		String[] titles = new String[] { "序号", "用户名", "姓名", "手机", "用户属性", "开户状态", "51老用户", "测评状态", "风险测评分", "风险等级", "测评时间", "上次测评时间" };

		// 序号、优惠券名称、优惠券类型、优惠券面值、发行数量、已发放数量、有效期、使用范围-操作平台、使用范围-项目类型、使用范围-项目期限、使用范围-出借金额
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
					AdminUserEvalationResultCustomize pInfo = resultList.get(i);
					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					} else if (celLength == 1) {
						cell.setCellValue(pInfo.getUserName());
					} else if (celLength == 2) {
						cell.setCellValue(pInfo.getRealName());
					} else if (celLength == 3) {
						cell.setCellValue(pInfo.getMobile());
					} else if (celLength == 4) {
						cell.setCellValue(pInfo.getUserProperty());
					} else if (celLength == 5) {
						cell.setCellValue(pInfo.getAccountStatus());
					} else if (celLength == 6) {
						cell.setCellValue("1".equals(pInfo.getIs51()) ? "是" : "否");
					} else if (celLength == 7) {
						cell.setCellValue(pInfo.getEvaluationStatus());
					} else if (celLength == 8) {
						cell.setCellValue(pInfo.getEvaluationScore());
					} else if (celLength == 9) {
						cell.setCellValue(pInfo.getEvaluationType());
					} else if (celLength == 10) {
						cell.setCellValue(pInfo.getCreatetime());
					} else if (celLength == 11) {
						cell.setCellValue(pInfo.getLasttime());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.startLog(EvaluationController.class.toString(), EvaluationDefine.EXPORT_ACTION);
	}

}
