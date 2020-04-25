package com.hyjf.admin.manager.debt.debtborrowinvest;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.borrow.borrowfirst.BorrowFirstBean;
import com.hyjf.admin.manager.debt.debtborrowcommon.DebtBorrowCommonService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.auto.UtmPlat;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowInvestCustomize;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = DebtBorrowInvestDefine.REQUEST_MAPPING)
public class DebtBorrowInvestController extends BaseController {

	@Autowired
	private DebtBorrowInvestService debtBorrowInvestService;

	@Autowired
	private DebtBorrowCommonService debtBorrowCommonService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(DebtBorrowInvestDefine.INIT)
	@RequiresPermissions(DebtBorrowInvestDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, DebtBorrowInvestBean form) {
		LogUtil.startLog(DebtBorrowInvestController.class.toString(), DebtBorrowInvestDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(DebtBorrowInvestDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(DebtBorrowInvestController.class.toString(), DebtBorrowInvestDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(DebtBorrowInvestDefine.SEARCH_ACTION)
	@RequiresPermissions(DebtBorrowInvestDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, DebtBorrowInvestBean form) {
		LogUtil.startLog(DebtBorrowInvestController.class.toString(), DebtBorrowInvestDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(DebtBorrowInvestDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(DebtBorrowInvestController.class.toString(), DebtBorrowInvestDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, DebtBorrowInvestBean form) {

		DebtBorrowInvestCustomize borrowInvestCustomize = new DebtBorrowInvestCustomize();

		// 操作平台
		List<ParamName> clientList = this.debtBorrowInvestService.getParamNameList(CustomConstants.CLIENT);
		modelAndView.addObject("clientList", clientList);

		// 还款方式
		List<BorrowStyle> borrowStyleList = this.debtBorrowCommonService.borrowStyleList("");
		modelAndView.addObject("borrowStyleList", borrowStyleList);

		// 投标方式
		List<ParamName> investTypeList = this.debtBorrowInvestService.getParamNameList("INVEST_TYPE");
		modelAndView.addObject("investTypeList", investTypeList);

		// 还款方式
		List<UtmPlat> utmPlatList = this.debtBorrowInvestService.getUtmPlatList();
		modelAndView.addObject("utmList", utmPlatList);

		// 项目编号 检索条件
		borrowInvestCustomize.setBorrowNidSrch(form.getBorrowNidSrch());
		// 项目名称 检索条件
		borrowInvestCustomize.setBorrowNameSrch(form.getBorrowNameSrch());
		// 用户名 检索条件
		borrowInvestCustomize.setUsernameSrch(form.getUsernameSrch());
		// 推荐人 检索条件
		borrowInvestCustomize.setReferrerNameSrch(form.getReferrerNameSrch());
		// 还款方式 检索条件
		borrowInvestCustomize.setBorrowStyleSrch(form.getBorrowStyleSrch());
		// 操作平台 检索条件
		borrowInvestCustomize.setClientSrch(form.getClientSrch());
		// 渠道 检索条件
		borrowInvestCustomize.setUtmIdSrch(form.getUtmIdSrch());
		// 出借时间 检索条件
		borrowInvestCustomize.setTimeStartSrch(StringUtils.isNotBlank(form.getTimeStartSrch())?form.getTimeStartSrch():null);
		// 出借时间 检索条件
		borrowInvestCustomize.setTimeEndSrch(StringUtils.isNotBlank(form.getTimeEndSrch())?form.getTimeEndSrch():null);
		// 项目期限
		borrowInvestCustomize.setBorrowPeriod(form.getBorrowPeriod());
		// 出借类型
		borrowInvestCustomize.setInvestType(form.getInvestType());

		Long count = this.debtBorrowInvestService.countBorrowInvest(borrowInvestCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			borrowInvestCustomize.setLimitStart(paginator.getOffset());
			borrowInvestCustomize.setLimitEnd(paginator.getLimit());
			List<DebtBorrowInvestCustomize> recordList = this.debtBorrowInvestService.selectBorrowInvestList(borrowInvestCustomize);
			form.setPaginator(paginator);
			String sumAccount = this.debtBorrowInvestService.selectBorrowInvestAccount(borrowInvestCustomize);
			modelAndView.addObject("sumAccount", sumAccount);
			modelAndView.addObject("recordList", recordList);
		}

		modelAndView.addObject(DebtBorrowInvestDefine.BORROW_FORM, form);
	}

	/**
	 * 数据导出
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(DebtBorrowInvestDefine.EXPORT_ACTION)
	@RequiresPermissions(DebtBorrowInvestDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, DebtBorrowInvestBean form) throws Exception {
		LogUtil.startLog(DebtBorrowInvestController.class.toString(), DebtBorrowInvestDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "出借明细";

		DebtBorrowInvestCustomize debtBorrowInvestCustomize = new DebtBorrowInvestCustomize();
		// 项目编号 检索条件
		debtBorrowInvestCustomize.setBorrowNidSrch(form.getBorrowNidSrch());
		// 项目名称 检索条件
		debtBorrowInvestCustomize.setBorrowNameSrch(form.getBorrowNameSrch());
		// 用户名 检索条件
		debtBorrowInvestCustomize.setUsernameSrch(form.getUsernameSrch());
		// 推荐人 检索条件
		debtBorrowInvestCustomize.setReferrerNameSrch(form.getReferrerNameSrch());
		// 还款方式 检索条件
		debtBorrowInvestCustomize.setBorrowStyleSrch(form.getBorrowStyleSrch());
		// 操作平台 检索条件
		debtBorrowInvestCustomize.setClientSrch(form.getClientSrch());
		// 渠道 检索条件
		debtBorrowInvestCustomize.setUtmIdSrch(form.getUtmIdSrch());
		// 出借时间 检索条件
		debtBorrowInvestCustomize.setTimeStartSrch(StringUtils.isNotBlank(form.getTimeStartSrch())?form.getTimeStartSrch():null);
		// 出借时间 检索条件
		debtBorrowInvestCustomize.setTimeEndSrch(StringUtils.isNotBlank(form.getTimeEndSrch())?form.getTimeEndSrch():null);
		// 出借类型
		debtBorrowInvestCustomize.setInvestType(form.getInvestType());

		List<DebtBorrowInvestCustomize> resultList = this.debtBorrowInvestService.exportBorrowInvestList(debtBorrowInvestCustomize);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "序号", "项目编号", "借款人ID", "借款人用户名", "项目名称", "项目类型", "借款期限", "出借利率", "还款方式", "出借订单号", "冻结订单号", "出借人用户名", "出借人ID", "出借人用户属性（当前）", "出借人所属一级分部（当前）", "出借人所属二级分部（当前）",
				"出借人所属团队（当前）", "推荐人（当前）", "推荐人ID（当前）", "推荐人姓名（当前）", "推荐人所属一级分部（当前）", "推荐人所属二级分部（当前）", "推荐人所属团队（当前）", "出借人用户属性（出借时）", "推荐人用户属性（出借时）", "推荐人（出借时）", "推荐人ID（出借时）", "一级分部（出借时）",
				"二级分部（出借时）", "团队（出借时）", "出借金额", "操作平台", "投标方式", "出借时间" };
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
					DebtBorrowInvestCustomize record = resultList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 项目编号
					else if (celLength == 1) {
						cell.setCellValue(record.getBorrowNid());
					}
					// 借款人ID
					else if (celLength == 2) {
						cell.setCellValue(record.getUserId());
					}
					// 借款人用户名
					else if (celLength == 3) {
						cell.setCellValue(record.getUsername());
					}
					// 项目名称
					else if (celLength == 4) {
						cell.setCellValue(record.getBorrowName());
					}
					// 项目类型
					else if (celLength == 5) {
						cell.setCellValue(record.getBorrowProjectTypeName());
					}
					// 借款期限
					else if (celLength == 6) {
						cell.setCellValue(record.getBorrowPeriod());
					}
					// 出借利率
					else if (celLength == 7) {
						cell.setCellValue(record.getBorrowApr());
					}
					// 还款方式
					else if (celLength == 8) {
						cell.setCellValue(record.getBorrowStyleName());
					}

					// 出借订单号
					else if (celLength == 9) {
						cell.setCellValue(record.getTenderOrderNum());
					}
					// 冻结订单号
					else if (celLength == 10) {
						cell.setCellValue(record.getFreezeOrderNum());
					}
					// 出借人用户名
					else if (celLength == 11) {
						cell.setCellValue(record.getTenderUsername());
					}
					// 出借人ID
					else if (celLength == 12) {
						cell.setCellValue(record.getTenderUserId());
					}
					// 出借人用户属性（当前）
					else if (celLength == 13) {
						if ("0".equals(record.getTenderUserAttributeNow())) {
							cell.setCellValue("无主单");
						} else if ("1".equals(record.getTenderUserAttributeNow())) {
							cell.setCellValue("有主单");
						} else if ("2".equals(record.getTenderUserAttributeNow())) {
							cell.setCellValue("线下员工");
						} else if ("3".equals(record.getTenderUserAttributeNow())) {
							cell.setCellValue("线上员工");
						}
					}
					// 出借人所属一级分部（当前）
					else if (celLength == 14) {
						cell.setCellValue(record.getTenderRegionName());
					}
					// 出借人所属二级分部（当前）
					else if (celLength == 15) {
						cell.setCellValue(record.getTenderBranchName());
					}
					// 出借人所属团队（当前）
					else if (celLength == 16) {
						cell.setCellValue(record.getTenderDepartmentName());
					}
					// 推荐人（当前）
					else if (celLength == 17) {
						cell.setCellValue(record.getReferrerName());
					}
					// 推荐人ID（当前）
					else if (celLength == 18) {
						cell.setCellValue("0".equals(record.getReferrerUserId()) ? "" : record.getReferrerUserId());
					}
					// 推荐人姓名（当前）
					else if (celLength == 19) {
						cell.setCellValue(record.getReferrerTrueName());
					}
					// 推荐人所属一级分部（当前）
					else if (celLength == 20) {
						cell.setCellValue(record.getReferrerRegionName());
					}
					// 推荐人所属二级分部（当前）
					else if (celLength == 21) {
						cell.setCellValue(record.getReferrerBranchName());
					}
					// 推荐人所属团队（当前）
					else if (celLength == 22) {
						cell.setCellValue(record.getReferrerDepartmentName());
					}
					// 出借人用户属性（出借时）
					else if (celLength == 23) {
						cell.setCellValue(record.getTenderUserAttribute());
					}
					// 推荐人用户属性（出借时）
					else if (celLength == 24) {
						cell.setCellValue("0".equals(record.getTenderReferrerUserId()) ? "" : record.getInviteUserAttribute());
					}
					// 推荐人（出借时）
					else if (celLength == 25) {
						cell.setCellValue(record.getTenderReferrerUsername());
					}
					// 推荐人ID（出借时）
					else if (celLength == 26) {
						cell.setCellValue("0".equals(record.getTenderReferrerUserId()) ? "" : record.getTenderReferrerUserId());
					}
					// 一级分部（出借时）
					else if (celLength == 27) {
						cell.setCellValue(record.getDepartmentLevel1Name());
					}
					// 二级分部（出借时）
					else if (celLength == 28) {
						cell.setCellValue(record.getDepartmentLevel2Name());
					}
					// 团队（出借时）
					else if (celLength == 29) {
						cell.setCellValue(record.getTeamName());
					}
					// 出借金额
					else if (celLength == 30) {
						cell.setCellValue(record.getAccount());
					}
					// 操作平台
					else if (celLength == 31) {
						cell.setCellValue(record.getOperatingDeck());
					}
					// 投标方式
					else if (celLength == 32) {
						cell.setCellValue(record.getInvestType());
					}
					// 出借时间
					else if (celLength == 33) {
						cell.setCellValue(record.getAddtime());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
		LogUtil.endLog(DebtBorrowInvestController.class.toString(), DebtBorrowInvestDefine.EXPORT_ACTION);
	}

	@RequiresPermissions(DebtBorrowInvestDefine.PERMISSIONS_EXPORT)
	@ResponseBody
	@RequestMapping(value = DebtBorrowInvestDefine.RESEND_MESSAGE_ACTION, produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	public String resendMessageAction(HttpServletRequest request) {
		LogUtil.startLog(DebtBorrowInvestDefine.class.toString(), DebtBorrowInvestDefine.RESEND_MESSAGE_ACTION);
		JSONObject ret = new JSONObject();
		String userid = request.getParameter("userid");
		String nid = request.getParameter("nid");
		String borrownid = request.getParameter("borrownid");
		String msg = debtBorrowInvestService.resendMessageAction(userid, nid, borrownid, null);
		if (msg == null) {
			ret.put(DebtBorrowInvestDefine.JSON_RESULT_KEY, "操作完成");
			ret.put(DebtBorrowInvestDefine.JSON_STATUS_KEY, DebtBorrowInvestDefine.JSON_STATUS_OK);
		} else if (!"系统异常".equals(msg)) {
			ret.put(DebtBorrowInvestDefine.JSON_STATUS_KEY, DebtBorrowInvestDefine.JSON_STATUS_NG);
			ret.put(DebtBorrowInvestDefine.JSON_RESULT_KEY, msg);
		} else {
			ret.put(DebtBorrowInvestDefine.JSON_STATUS_KEY, DebtBorrowInvestDefine.JSON_STATUS_NG);
			ret.put(DebtBorrowInvestDefine.JSON_RESULT_KEY, "异常纪录，请刷新后重试");
		}

		LogUtil.endLog(DebtBorrowInvestDefine.class.toString(), DebtBorrowInvestDefine.RESEND_MESSAGE_ACTION);
		return ret.toString();
	}

	/**
	 * 跳转到导出页面
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = DebtBorrowInvestDefine.TOEXPORT_AGREEMENT_ACTION)
	@RequiresPermissions(DebtBorrowInvestDefine.PERMISSIONS_EXPORT_AGREEMENT)
	public ModelAndView toExportAgreementAction(HttpServletRequest request, BorrowFirstBean form) {
		LogUtil.startLog(DebtBorrowInvestController.class.toString(), DebtBorrowInvestDefine.TOEXPORT_AGREEMENT_ACTION);
		ModelAndView modelAndView = new ModelAndView(DebtBorrowInvestDefine.EXPORT_AGREEMENT_PATH);
		String userid = request.getParameter("userid");
		String nid = request.getParameter("nid");
		String borrownid = request.getParameter("borrownid");
		// TODO
		modelAndView.addObject("userid", userid);
		modelAndView.addObject("nid", nid);
		modelAndView.addObject("borrownid", borrownid);

		LogUtil.endLog(DebtBorrowInvestController.class.toString(), DebtBorrowInvestDefine.TOEXPORT_AGREEMENT_ACTION);
		return modelAndView;
	}

	/**
	 * 导出协议
	 * 
	 * @param request
	 * @return
	 */
	@RequiresPermissions(DebtBorrowInvestDefine.PERMISSIONS_EXPORT_AGREEMENT)
	@ResponseBody
	@RequestMapping(value = DebtBorrowInvestDefine.EXPORT_AGREEMENT_ACTION, produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	public String exportAgreementAction(HttpServletRequest request) {
		LogUtil.startLog(DebtBorrowInvestDefine.class.toString(), DebtBorrowInvestDefine.EXPORT_AGREEMENT_ACTION);
		JSONObject ret = new JSONObject();
		String userid = request.getParameter("userid");
		String nid = request.getParameter("nid");
		String borrownid = request.getParameter("borrownid");
		String email = request.getParameter("email");
		if (StringUtils.isEmpty(email)) {
			ret.put(DebtBorrowInvestDefine.JSON_STATUS_KEY, DebtBorrowInvestDefine.JSON_STATUS_NG);
			ret.put(DebtBorrowInvestDefine.JSON_RESULT_KEY, "请输入邮箱地址");
			return ret.toString();
		}
		if (!Validator.isEmailAddress(email)) {
			ret.put(DebtBorrowInvestDefine.JSON_STATUS_KEY, DebtBorrowInvestDefine.JSON_STATUS_NG);
			ret.put(DebtBorrowInvestDefine.JSON_RESULT_KEY, "邮箱格式不正确!");
			return ret.toString();
		}
		if (StringUtils.isEmpty(nid) || StringUtils.isEmpty(borrownid)) {
			ret.put(DebtBorrowInvestDefine.JSON_STATUS_KEY, DebtBorrowInvestDefine.JSON_STATUS_NG);
			ret.put(DebtBorrowInvestDefine.JSON_RESULT_KEY, "传递参数不正确");
			return ret.toString();
		}
		String msg = debtBorrowInvestService.resendMessageAction(userid, nid, borrownid, email);
		if (msg == null) {
			ret.put(DebtBorrowInvestDefine.JSON_RESULT_KEY, "操作完成");
			ret.put(DebtBorrowInvestDefine.JSON_STATUS_KEY, DebtBorrowInvestDefine.JSON_STATUS_OK);
		} else if (!"系统异常".equals(msg)) {
			ret.put(DebtBorrowInvestDefine.JSON_STATUS_KEY, DebtBorrowInvestDefine.JSON_STATUS_NG);
			ret.put(DebtBorrowInvestDefine.JSON_RESULT_KEY, msg);
		} else {
			ret.put(DebtBorrowInvestDefine.JSON_STATUS_KEY, DebtBorrowInvestDefine.JSON_STATUS_NG);
			ret.put(DebtBorrowInvestDefine.JSON_RESULT_KEY, "异常纪录，请刷新后重试");
		}
		LogUtil.endLog(DebtBorrowInvestDefine.class.toString(), DebtBorrowInvestDefine.EXPORT_AGREEMENT_ACTION);
		return ret.toString();
	}

}
