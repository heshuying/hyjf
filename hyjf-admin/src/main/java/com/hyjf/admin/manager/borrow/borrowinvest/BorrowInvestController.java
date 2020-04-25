package com.hyjf.admin.manager.borrow.borrowinvest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonService;
import com.hyjf.admin.manager.borrow.borrowfirst.BorrowFirstBean;
import com.hyjf.admin.manager.hjhplan.planlist.PlanListDefine;
import com.hyjf.bank.service.fdd.fddgeneratecontract.FddGenerateContractBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.BorrowInvestCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = BorrowInvestDefine.REQUEST_MAPPING)
public class BorrowInvestController extends BaseController {

	@Autowired
	private BorrowInvestService borrowInvestService;

	@Autowired
	private BorrowCommonService borrowCommonService;

	@Autowired
	@Qualifier("myAmqpTemplate")
	private RabbitTemplate rabbitTemplate;

	/**
	 * 画面初始化
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowInvestDefine.INIT)
	@RequiresPermissions(BorrowInvestDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, BorrowInvestBean form) {
		LogUtil.startLog(BorrowInvestController.class.toString(), BorrowInvestDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(BorrowInvestDefine.LIST_PATH);
		// 创建分页
		// sql优化，只拉取10天的数据
		form.setTimeStartSrch(GetDate.date2Str(GetDate.getTodayBeforeOrAfter(-10), new SimpleDateFormat("yyyy-MM-dd")));
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BorrowInvestController.class.toString(), BorrowInvestDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面初始化
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowInvestDefine.SEARCH_ACTION)
	@RequiresPermissions(BorrowInvestDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, BorrowInvestBean form) {
		LogUtil.startLog(BorrowInvestController.class.toString(), BorrowInvestDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(BorrowInvestDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BorrowInvestController.class.toString(), BorrowInvestDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 *
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, BorrowInvestBean form) {

		BorrowInvestCustomize borrowInvestCustomize = new BorrowInvestCustomize();

		// 操作平台
		List<ParamName> clientList = this.borrowInvestService.getParamNameList(CustomConstants.CLIENT);
		modelAndView.addObject("clientList", clientList);

		// 还款方式
		List<BorrowStyle> borrowStyleList = this.borrowCommonService.borrowStyleList("");
		modelAndView.addObject("borrowStyleList", borrowStyleList);

		// 投标方式
		List<ParamName> investTypeList = this.borrowInvestService.getParamNameList("INVEST_TYPE");
		modelAndView.addObject("investTypeList", investTypeList);

		// 还款方式
		List<UtmPlat> utmPlatList = this.borrowInvestService.getUtmPlatList();
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
		borrowInvestCustomize.setTimeStartSrch(form.getTimeStartSrch());
		// 出借时间 检索条件
		borrowInvestCustomize.setTimeEndSrch(form.getTimeEndSrch());
		// 项目期限
		borrowInvestCustomize.setBorrowPeriod(form.getBorrowPeriod());
		// 出借类型
		borrowInvestCustomize.setInvestType(form.getInvestType());
		// 计划编号
		borrowInvestCustomize.setPlanNidSrch(form.getPlanNidSrch());
        // 是否复投投标  1：是    0：否
		borrowInvestCustomize.setTenderType(form.getTenderType());

		Long count = this.borrowInvestService.countBorrowInvest(borrowInvestCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			borrowInvestCustomize.setLimitStart(paginator.getOffset());
			borrowInvestCustomize.setLimitEnd(paginator.getLimit());
			List<BorrowInvestCustomize> recordList = this.borrowInvestService.selectBorrowInvestList(borrowInvestCustomize);
			form.setPaginator(paginator);
			String sumAccount = this.borrowInvestService.selectBorrowInvestAccount(borrowInvestCustomize);
			modelAndView.addObject("sumAccount", sumAccount);
			modelAndView.addObject("recordList", recordList);
		}

		modelAndView.addObject(BorrowInvestDefine.BORROW_FORM, form);
	}

	/**
	 *  具有   组织机构查看权限  的导出, 可以导出更多的字段
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowInvestDefine.ENHANCE_EXPORT_USERS_ACTION)
	@RequiresPermissions(value = {BorrowInvestDefine.PERMISSIONS_EXPORT, BorrowInvestDefine.ENHANCE_PERMISSIONS_EXPORT}, logical = Logical.AND)
	public void enhanceExportAction(HttpServletRequest request, HttpServletResponse response, BorrowInvestBean form) throws Exception {
		LogUtil.startLog(BorrowInvestController.class.toString(), BorrowInvestDefine.ENHANCE_EXPORT_USERS_ACTION);
		// 表格sheet名称
		String sheetName = "出借明细";

		BorrowInvestCustomize borrowInvestCustomize = new BorrowInvestCustomize();
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
		borrowInvestCustomize.setTimeStartSrch(form.getTimeStartSrch());
		// 出借时间 检索条件
		borrowInvestCustomize.setTimeEndSrch(form.getTimeEndSrch());
		// 出借类型
		borrowInvestCustomize.setInvestType(form.getInvestType());
		// 是否复投投标
		borrowInvestCustomize.setTenderType(form.getTenderType());

		borrowInvestCustomize.setPlanNidSrch(form.getPlanNidSrch());

		List<BorrowInvestCustomize> resultList = new ArrayList<BorrowInvestCustomize>();

		if (StringUtils.isNotBlank(form.getTimeEndSrch()) || StringUtils.isNotBlank(form.getTimeStartSrch())) {
			resultList = this.borrowInvestService.selectExportBorrowInvestList(borrowInvestCustomize);
		}

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

        String[] titles = new String[] { "序号", "项目编号", "智投编号", "借款人ID", "借款人用户名", "项目类型", "项目期限", "出借利率", "还款方式", "出借订单号", "冻结订单号", "出借人用户名", "出借人ID", "出借人用户属性（当前）", "出借人所属一级分部（当前）", "出借人所属二级分部（当前）", "出借人所属团队（当前）", "推荐人（当前）", "推荐人ID（当前）", "推荐人姓名（当前）", "推荐人所属一级分部（当前）", "推荐人所属二级分部（当前）", "推荐人所属团队（当前）",
                "出借人用户属性（出借时）", "推荐人用户属性（出借时）", "推荐人（出借时）", "推荐人ID（出借时）", "一级分部（出借时）", "二级分部（出借时）", "团队（出借时）", "出借金额", "操作平台", "投标方式", "出借时间","合同编号","合同状态","合同名称","模版编号", "合同生成时间","合同签署时间","循环出借"};		// 声明一个工作薄
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
                    BorrowInvestCustomize record = resultList.get(i);

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
					// 计划编号
					else if (celLength == 2) {
						cell.setCellValue(record.getPlanNid());
					}
					// 借款人ID
					else if (celLength == 3) {
						cell.setCellValue(record.getUserId());
					}
					// 借款人用户名
					else if (celLength == 4) {
						cell.setCellValue(record.getUsername());
					}
					// 项目名称
//					else if (celLength == 5) {
//						cell.setCellValue(record.getBorrowName());
//					}
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
						cell.setCellValue(
								"0".equals(record.getTenderReferrerUserId()) ? "" : record.getInviteUserAttribute());
					}
					// 推荐人（出借时）
					else if (celLength == 25) {
						cell.setCellValue(record.getTenderReferrerUsername());
					}
					// 推荐人ID（出借时）
					else if (celLength == 26) {
						cell.setCellValue(
								"0".equals(record.getTenderReferrerUserId()) ? "" : record.getTenderReferrerUserId());
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
					// 合同编号
					else if (celLength == 34) {
						cell.setCellValue(StringUtils.isBlank(record.getContractNumber()) ? "" : record.getContractNumber());
					}
					// 合同状态
					else if (celLength == 35) {
						if (StringUtils.isBlank(record.getContractStatus())) {
								cell.setCellValue("初始");
						} else if (StringUtils.isNotBlank(record.getContractStatus()) && "0".equals(record.getContractStatus())) {
							cell.setCellValue("初始");
						} else if (StringUtils.isNotBlank(record.getContractStatus()) && "1".equals(record.getContractStatus())) {
							cell.setCellValue("生成成功");
						} else if (StringUtils.isNotBlank(record.getContractStatus()) && "2".equals(record.getContractStatus())) {
							cell.setCellValue("签署成功");
						} else if (StringUtils.isNotBlank(record.getContractStatus()) && "3".equals(record.getContractStatus())) {
							cell.setCellValue("下载成功");
						}
					}
					// 合同名称
					else if (celLength == 36) {
						cell.setCellValue(StringUtils.isBlank(record.getContractName()) ? "" : record.getContractName());
					}
					// 模版编号
					else if (celLength == 37) {
						cell.setCellValue(StringUtils.isBlank(record.getTempletId()) ? "" : record.getTempletId());
					}
					// 合同生成时间
					else if (celLength == 38) {
						cell.setCellValue(StringUtils.isBlank(record.getContractCreateTime()) ? "" : record.getContractCreateTime());
					}
					// 合同签署时间
					else if (celLength == 39) {
						cell.setCellValue(StringUtils.isBlank(record.getContractSignTime()) ? "" : record.getContractSignTime());
					}
					else if(celLength == 40) {
						cell.setCellValue(record.getTenderType());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
		LogUtil.endLog(BorrowInvestController.class.toString(), BorrowInvestDefine.EXPORT_ACTION);
	}

	/**
	 * 类表导出
	 * @param request
	 * @param response
	 * @param form
	 * @throws Exception
	 */
	@RequestMapping(BorrowInvestDefine.EXPORT_ACTION)
	@RequiresPermissions(BorrowInvestDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, BorrowInvestBean form) throws Exception {
		LogUtil.startLog(BorrowInvestController.class.toString(), BorrowInvestDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "出借明细";

		BorrowInvestCustomize borrowInvestCustomize = new BorrowInvestCustomize();
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
		borrowInvestCustomize.setTimeStartSrch(form.getTimeStartSrch());
		// 出借时间 检索条件
		borrowInvestCustomize.setTimeEndSrch(form.getTimeEndSrch());
		// 出借类型
		borrowInvestCustomize.setInvestType(form.getInvestType());
		// 是否复投投标
		borrowInvestCustomize.setTenderType(form.getTenderType());

		borrowInvestCustomize.setPlanNidSrch(form.getPlanNidSrch());

		List<BorrowInvestCustomize> resultList = new ArrayList<BorrowInvestCustomize>();

		if (StringUtils.isNotBlank(form.getTimeEndSrch()) || StringUtils.isNotBlank(form.getTimeStartSrch())) {
			resultList = this.borrowInvestService.selectExportBorrowInvestList(borrowInvestCustomize);
		}

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

        String[] titles = new String[] { "序号", "项目编号", "智投编号", "借款人ID", "借款人用户名", "项目名称", "项目类型", "项目期限", "出借利率", "还款方式", "出借订单号", "冻结订单号", "出借人用户名", "出借人ID", "出借人用户属性（当前）", "推荐人（当前）", "推荐人ID（当前）", "推荐人姓名（当前）",
                "出借人用户属性（出借时）", "推荐人用户属性（出借时）", "推荐人（出借时）", "推荐人ID（出借时）", "出借金额", "操作平台", "投标方式", "出借时间","合同编号","合同状态","合同名称","模版编号", "合同生成时间","合同签署时间","循环出借"};		// 声明一个工作薄
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
					BorrowInvestCustomize record = resultList.get(i);

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
					// 智投编号
					else if (celLength == 2) {
						cell.setCellValue(record.getPlanNid());
					}
					// 借款人ID
					else if (celLength == 3) {
						cell.setCellValue(record.getUserId());
					}
					// 借款人用户名
					else if (celLength == 4) {
						cell.setCellValue(record.getUsername());
					}
					// 项目名称
					else if (celLength == 5) {
						cell.setCellValue(record.getBorrowName());
					}
					// 项目类型
					else if (celLength == 6) {
						cell.setCellValue(record.getBorrowProjectTypeName());
					}
					// 借款期限
					else if (celLength == 7) {
						cell.setCellValue(record.getBorrowPeriod());
					}
					// 出借利率
					else if (celLength == 8) {
						cell.setCellValue(record.getBorrowApr());
					}
					// 还款方式
					else if (celLength == 9) {
						cell.setCellValue(record.getBorrowStyleName());
					}

					// 出借订单号
					else if (celLength == 10) {
						cell.setCellValue(record.getTenderOrderNum());
					}
					// 冻结订单号
					else if (celLength == 11) {
						cell.setCellValue(record.getFreezeOrderNum());
					}
					// 出借人用户名
					else if (celLength == 12) {
						cell.setCellValue(record.getTenderUsername());
					}
					// 出借人ID
					else if (celLength == 13) {
						cell.setCellValue(record.getTenderUserId());
					}
					// 出借人用户属性（当前）
					else if (celLength == 14) {
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
					// 推荐人（当前）
					else if (celLength == 15) {
						cell.setCellValue(record.getReferrerName());
					}
					// 推荐人ID（当前）
					else if (celLength == 16) {
						cell.setCellValue("0".equals(record.getReferrerUserId()) ? "" : record.getReferrerUserId());
					}
					// 推荐人姓名（当前）
					else if (celLength == 17) {
						cell.setCellValue(record.getReferrerTrueName());
					}
					// 出借人用户属性（出借时）
					else if (celLength == 18) {
						cell.setCellValue(record.getTenderUserAttribute());
					}
					// 推荐人用户属性（出借时）
					else if (celLength == 19) {
						cell.setCellValue(
								"0".equals(record.getTenderReferrerUserId()) ? "" : record.getInviteUserAttribute());
					}
					// 推荐人（出借时）
					else if (celLength == 20) {
						cell.setCellValue(record.getTenderReferrerUsername());
					}
					// 推荐人ID（出借时）
					else if (celLength == 21) {
						cell.setCellValue(
								"0".equals(record.getTenderReferrerUserId()) ? "" : record.getTenderReferrerUserId());
					}
					// 授权服务金额
					else if (celLength == 22) {
						cell.setCellValue(record.getAccount());
					}
					// 操作平台
					else if (celLength == 23) {
						cell.setCellValue(record.getOperatingDeck());
					}
					// 投标方式
					else if (celLength == 24) {
						cell.setCellValue(record.getInvestType());
					}
					// 出借时间
					else if (celLength == 25) {
						cell.setCellValue(record.getAddtime());
					}
					// 合同编号
					else if (celLength == 26) {
						cell.setCellValue(StringUtils.isBlank(record.getContractNumber()) ? "" : record.getContractNumber());
					}
					// 合同状态
					else if (celLength == 27) {
						if (StringUtils.isBlank(record.getContractStatus())) {
							cell.setCellValue("初始");
						} else if (StringUtils.isNotBlank(record.getContractStatus()) && "0".equals(record.getContractStatus())) {
							cell.setCellValue("初始");
						} else if (StringUtils.isNotBlank(record.getContractStatus()) && "1".equals(record.getContractStatus())) {
							cell.setCellValue("生成成功");
						} else if (StringUtils.isNotBlank(record.getContractStatus()) && "2".equals(record.getContractStatus())) {
							cell.setCellValue("签署成功");
						} else if (StringUtils.isNotBlank(record.getContractStatus()) && "3".equals(record.getContractStatus())) {
							cell.setCellValue("下载成功");
						}
					}
					// 合同名称
					else if (celLength == 28) {
						cell.setCellValue(StringUtils.isBlank(record.getContractName()) ? "" : record.getContractName());
					}
					// 模版编号
					else if (celLength == 29) {
						cell.setCellValue(StringUtils.isBlank(record.getTempletId()) ? "" : record.getTempletId());
					}
					// 合同生成时间
					else if (celLength == 30) {
						cell.setCellValue(StringUtils.isBlank(record.getContractCreateTime()) ? "" : record.getContractCreateTime());
					}
					// 合同签署时间
					else if (celLength == 31) {
						cell.setCellValue(StringUtils.isBlank(record.getContractSignTime()) ? "" : record.getContractSignTime());
					}
					else if(celLength == 32) {
						cell.setCellValue(record.getTenderType());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
		LogUtil.endLog(BorrowInvestController.class.toString(), BorrowInvestDefine.EXPORT_ACTION);
	}

	@RequiresPermissions(BorrowInvestDefine.PERMISSIONS_EXPORT)
	@ResponseBody
	@RequestMapping(value = BorrowInvestDefine.RESEND_MESSAGE_ACTION, produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	public String resendMessageAction(HttpServletRequest request) {
		LogUtil.startLog(BorrowInvestDefine.class.toString(), BorrowInvestDefine.RESEND_MESSAGE_ACTION);
		JSONObject ret = new JSONObject();
		String userid = request.getParameter("userid");
		String nid = request.getParameter("nid");
		String borrownid = request.getParameter("borrownid");
		String msg = "";
		if(borrownid != null){
			msg = borrowInvestService.resendMessageAction(userid, nid, borrownid, null);
		}
		if (msg == null) {
			ret.put(BorrowInvestDefine.JSON_RESULT_KEY, "操作完成");
			ret.put(BorrowInvestDefine.JSON_STATUS_KEY, BorrowInvestDefine.JSON_STATUS_OK);
		} else if (!"系统异常".equals(msg)) {
			ret.put(BorrowInvestDefine.JSON_STATUS_KEY, BorrowInvestDefine.JSON_STATUS_NG);
			ret.put(BorrowInvestDefine.JSON_RESULT_KEY, msg);
		} else {
			ret.put(BorrowInvestDefine.JSON_STATUS_KEY, BorrowInvestDefine.JSON_STATUS_NG);
			ret.put(BorrowInvestDefine.JSON_RESULT_KEY, "异常纪录，请刷新后重试");
		}

		LogUtil.endLog(BorrowInvestDefine.class.toString(), BorrowInvestDefine.RESEND_MESSAGE_ACTION);
		return ret.toString();
	}

	/**
	 * 跳转到导出页面
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = BorrowInvestDefine.TOEXPORT_AGREEMENT_ACTION)
	@RequiresPermissions(BorrowInvestDefine.PERMISSIONS_EXPORT_AGREEMENT)
	public ModelAndView toExportAgreementAction(HttpServletRequest request, BorrowFirstBean form) {
		LogUtil.startLog(BorrowInvestController.class.toString(), BorrowInvestDefine.TOEXPORT_AGREEMENT_ACTION);
		ModelAndView modelAndView = new ModelAndView(BorrowInvestDefine.EXPORT_AGREEMENT_PATH);
		String userid = request.getParameter("userid");
		String nid = request.getParameter("nid");
		String borrownid = request.getParameter("borrownid");
		// TODO
		modelAndView.addObject("userid", userid);
		modelAndView.addObject("nid", nid);
		modelAndView.addObject("borrownid", borrownid);

		LogUtil.endLog(BorrowInvestController.class.toString(), BorrowInvestDefine.TOEXPORT_AGREEMENT_ACTION);
		return modelAndView;
	}

	/**
	 * 导出协议
	 *
	 * @param request
	 * @return
	 */
	@RequiresPermissions(BorrowInvestDefine.PERMISSIONS_EXPORT_AGREEMENT)
	@ResponseBody
	@RequestMapping(value = BorrowInvestDefine.EXPORT_AGREEMENT_ACTION, produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	public String exportAgreementAction(HttpServletRequest request) {
		LogUtil.startLog(BorrowInvestDefine.class.toString(), BorrowInvestDefine.EXPORT_AGREEMENT_ACTION);
		JSONObject ret = new JSONObject();
		String userid = request.getParameter("userid");
		String nid = request.getParameter("nid");
		String borrownid = request.getParameter("borrownid");
		String email = request.getParameter("email");
		String msg = "";
		if (StringUtils.isEmpty(email)) {
			ret.put(BorrowInvestDefine.JSON_STATUS_KEY, BorrowInvestDefine.JSON_STATUS_NG);
			ret.put(BorrowInvestDefine.JSON_RESULT_KEY, "请输入邮箱地址");
			return ret.toString();
		}
		if (!Validator.isEmailAddress(email)) {
			ret.put(BorrowInvestDefine.JSON_STATUS_KEY, BorrowInvestDefine.JSON_STATUS_NG);
			ret.put(BorrowInvestDefine.JSON_RESULT_KEY, "邮箱格式不正确!");
			return ret.toString();
		}
		if (StringUtils.isEmpty(nid) || StringUtils.isEmpty(borrownid)) {
			ret.put(BorrowInvestDefine.JSON_STATUS_KEY, BorrowInvestDefine.JSON_STATUS_NG);
			ret.put(BorrowInvestDefine.JSON_RESULT_KEY, "传递参数不正确");
			return ret.toString();
		}
		if(borrownid != null){
			msg = borrowInvestService.resendMessageAction(userid, nid, borrownid, email);
		}
		if (msg == null) {
			ret.put(BorrowInvestDefine.JSON_RESULT_KEY, "操作完成");
			ret.put(BorrowInvestDefine.JSON_STATUS_KEY, BorrowInvestDefine.JSON_STATUS_OK);
		} else if (!"系统异常".equals(msg)) {
			ret.put(BorrowInvestDefine.JSON_STATUS_KEY, BorrowInvestDefine.JSON_STATUS_NG);
			ret.put(BorrowInvestDefine.JSON_RESULT_KEY, msg);
		} else {
			ret.put(BorrowInvestDefine.JSON_STATUS_KEY, BorrowInvestDefine.JSON_STATUS_NG);
			ret.put(BorrowInvestDefine.JSON_RESULT_KEY, "异常纪录，请刷新后重试");
		}
		LogUtil.endLog(BorrowInvestDefine.class.toString(), BorrowInvestDefine.EXPORT_AGREEMENT_ACTION);
		return ret.toString();
	}

	/**
	 * 出借人债权明细查询跳转页面 (对话框)
	 * @author LIBIN
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowInvestDefine.DEBT_CHECK_ACTION)
	@RequiresPermissions(BorrowInvestDefine.PERMISSIONS_DEBTCHECK)
	public ModelAndView investorDebtChkAction(HttpServletRequest request, @ModelAttribute InvestorDebtBean form) {
		LogUtil.startLog(BorrowInvestController.class.toString(), BorrowInvestDefine.DEBT_CHECK_ACTION);
		ModelAndView modeAndView = new ModelAndView(BorrowInvestDefine.BANK_ACCOUNT_CHECK_PATH);
		modeAndView.addObject(BorrowInvestDefine.DEBT_CHECK_FORM, form);
		LogUtil.endLog(BorrowInvestController.class.toString(), BorrowInvestDefine.DEBT_CHECK_ACTION);
		return modeAndView;
	}

	/**
	 *出借人债权明细查询
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowInvestDefine.DEBT_CHECK_INFO_ACTION)
	@RequiresPermissions(BorrowInvestDefine.PERMISSIONS_DEBTCHECK)
	public ModelAndView queryInvestorDebtAction(HttpServletRequest request, @ModelAttribute InvestorDebtBean form) {
		LogUtil.startLog(BorrowInvestController.class.toString(), BorrowInvestDefine.DEBT_CHECK_INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(BorrowInvestDefine.INVESTOR_INFO);
		String borowNid = form.getBorrownid();
		if (StringUtils.isNotBlank(borowNid)) {
			//调用银行接口查询出借人债权明细
			List<BankCallBean> resultBeans = borrowInvestService.queryInvestorDebtDetails(form);
			//转换页面展示列表
			List<InvestorDebtBean> detailList = getDetailList(resultBeans);
			modelAndView.addObject("detailList", detailList);//画面数据源一
		}else{
			logger.info("==========borrowNid is null!");
		}
		LogUtil.endLog(BorrowInvestController.class.toString(), BorrowInvestDefine.DEBT_CHECK_INFO_ACTION);
		modelAndView.addObject(BorrowInvestDefine.BORROW_NID, form.getBorrownid());//画面数据源二
		return modelAndView;
	}

	/**
	 * 获得页面需要的展示列表
	 * @param resultBeans
	 * @return
	 */
	private List<InvestorDebtBean> getDetailList(List<BankCallBean> resultBeans) {
		//展示用LIST
		List<InvestorDebtBean> detailList = new ArrayList<>();

		if (Validator.isNotNull(resultBeans) && resultBeans.size() > 0) {
			for (int i = 0; i < resultBeans.size(); i++) {
				BankCallBean resultBean = resultBeans.get(i);
				//获取bean里的JOSN
				String subPacks = resultBean.getSubPacks();
				if (StringUtils.isNotBlank(subPacks)) {
					JSONArray loanDetails = JSONObject.parseArray(subPacks);
					/*--------add by LSY START--------------*/
					BigDecimal sumTxAmount = BigDecimal.ZERO;
					BigDecimal sumForIncome = BigDecimal.ZERO;
					BigDecimal sumIntTotal = BigDecimal.ZERO;
					BigDecimal sumIncome = BigDecimal.ZERO;
					/*--------add by LSY END--------------*/
					for (int j = 0; j < loanDetails.size(); j++) {
						JSONObject loanDetail = loanDetails.getJSONObject(j);
						InvestorDebtBean info = new InvestorDebtBean();
						info.setBorrownid(loanDetail.getString(BankCallConstant.PARAM_PRODUCTID));//标的号
						info.setBuyDate(loanDetail.getString(BankCallConstant.PARAM_BUYDATE));//投标日期
						info.setOrderId(loanDetail.getString(BankCallConstant.PARAM_ORDERID));//订单号
						info.setTxAmount(loanDetail.getBigDecimal(BankCallConstant.PARAM_TXAMOUNT));//交易金额
						info.setYield(loanDetail.getBigDecimal(BankCallConstant.PARAM_YIELD));//预期出借利率率
						info.setForIncome(loanDetail.getBigDecimal(BankCallConstant.PARAM_FORINCOME));//预期收益
						info.setIntTotal(loanDetail.getBigDecimal(BankCallConstant.PARAM_INTTOTAL));//预期本息收益
						info.setIncome(loanDetail.getBigDecimal(BankCallConstant.PARAM_INCOME));//实际收益
						info.setIncFlag(loanDetail.getString(BankCallConstant.PARAM_INCFLAG));//实际收益符号
						info.setEndDate(loanDetail.getString(BankCallConstant.PARAM_ENDDATE));//到期日
						info.setState(loanDetail.getString(BankCallConstant.PARAM_STATE));//状态
						detailList.add(info);
						/*-------add by LSY START-----------*/
						sumTxAmount = sumTxAmount.add(loanDetail.getBigDecimal(BankCallConstant.PARAM_TXAMOUNT));
						sumForIncome = sumForIncome.add(loanDetail.getBigDecimal(BankCallConstant.PARAM_FORINCOME));
						sumIntTotal = sumIntTotal.add(loanDetail.getBigDecimal(BankCallConstant.PARAM_INTTOTAL));
						sumIncome = sumIncome.add(loanDetail.getBigDecimal(BankCallConstant.PARAM_INCOME));
						/*-------add by LSY END-----------*/
					}
					/*--------add by LSY START--------------*/
					if (detailList != null && !detailList.isEmpty()) {
					    InvestorDebtBean temp = detailList.get(0);
					    temp.setSumTxAmount(sumTxAmount);
					    temp.setSumForIncome(sumForIncome);
					    temp.setSumIntTotal(sumIntTotal);
					    temp.setSumIncome(sumIncome);
					}
					/*--------add by LSY END--------------*/
				}
			}
		}
		return detailList;
	}

	/**
	 * PDF文件签署
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions(BorrowInvestDefine.PERMISSIONS_PDF_SIGN)
	@RequestMapping(value = BorrowInvestDefine.PDF_SIGN_ACTION, produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	public String pdfSignAction(HttpServletRequest request) {
		LogUtil.startLog(BorrowInvestDefine.class.toString(), BorrowInvestDefine.PDF_SIGN_ACTION);
		JSONObject ret = new JSONObject();
		Integer userId = Integer.parseInt(request.getParameter("userId"));
		// 标的编号
		String borrowNid = request.getParameter("borrowNid");

		String nid = request.getParameter("nid");

		BorrowRecover br = this.borrowInvestService.selectBorrowRecover(userId, borrowNid, nid);

		if (br == null) {
			ret.put(BorrowInvestDefine.JSON_RESULT_KEY, "操作异常");
			ret.put(BorrowInvestDefine.JSON_STATUS_KEY, BorrowInvestDefine.JSON_STATUS_NG);
			return ret.toString();
		}
		// 查询标的详情
		Borrow borrow = this.borrowInvestService.getBorrowByNid(borrowNid);
		if (borrow == null) {
			ret.put(BorrowInvestDefine.JSON_RESULT_KEY, "标的不存在");
			ret.put(BorrowInvestDefine.JSON_STATUS_KEY, BorrowInvestDefine.JSON_STATUS_NG);
			return ret.toString();
		}
		Users users = this.borrowInvestService.getUsersByUserId(userId);
		if (users == null) {
			ret.put(BorrowInvestDefine.JSON_RESULT_KEY, "获取用户信息异常");
			ret.put(BorrowInvestDefine.JSON_STATUS_KEY, BorrowInvestDefine.JSON_STATUS_NG);
			return ret.toString();
		}
		// 查询出借协议记录表
		TenderAgreement tenderAgreement = this.borrowInvestService.selectTenderAgreement(nid);
		if (tenderAgreement != null && tenderAgreement.getStatus() == 2) {
			this.borrowInvestService.updateSaveSignInfo(tenderAgreement, borrowNid, 1, borrow.getInstCode());
		} else {
			FddGenerateContractBean bean = new FddGenerateContractBean();
			bean.setTenderUserId(userId);
			bean.setOrdid(nid);
			bean.setTransType(1);
			bean.setBorrowNid(borrowNid);
			bean.setSignDate(GetDate.getDateMyTimeInMillis(br.getCreateTime()));
			bean.setTenderUserName(users.getUsername());
			bean.setTenderInterest(br.getRecoverInterest());
			bean.setTenderType(0);
			rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_GENERATE_CONTRACT, JSONObject.toJSONString(bean));
		}
		ret.put(BorrowInvestDefine.JSON_RESULT_KEY, "操作成功,签署MQ已发送");
		ret.put(BorrowInvestDefine.JSON_STATUS_KEY, BorrowInvestDefine.JSON_STATUS_OK);
		LogUtil.endLog(BorrowInvestDefine.class.toString(), BorrowInvestDefine.PDF_SIGN_ACTION);
		return ret.toString();
	}

	/**
	 * PDF脱敏图片预览
	 * @param request
	 * @return
	 */
	@RequestMapping(BorrowInvestDefine.PDF_PREVIEW_ACTION)
	@RequiresPermissions(BorrowInvestDefine.PERMISSIONS_PDF_PREVIEW)
	public ModelAndView pdfPreviewAction(HttpServletRequest request) {
		LogUtil.startLog(BorrowInvestDefine.class.toString(), BorrowInvestDefine.PDF_PREVIEW_ACTION);
		ModelAndView modelAndView = new ModelAndView(BorrowInvestDefine.PDF_PREVIEW_PATH);
		String nid = request.getParameter("nid");
		// 根据订单号查询用户出借协议记录表
		TenderAgreement tenderAgreement = this.borrowInvestService.selectTenderAgreement(nid);
		if (tenderAgreement != null && StringUtils.isNotBlank(tenderAgreement.getImgUrl())) {
			String imgUrl = tenderAgreement.getImgUrl();
			String[] imgs = imgUrl.split(";");
			List<String> imgList = Arrays.asList(imgs);
			modelAndView.addObject("imgList",imgList);
			// 文件服务器
			String fileDomainUrl = PropUtils.getSystem("hyjf.ftp.url") + PropUtils.getSystem("hyjf.ftp.basepath.img");
			modelAndView.addObject("fileDomainUrl",fileDomainUrl);
		}
		LogUtil.endLog(BorrowInvestDefine.class.toString(), BorrowInvestDefine.PDF_PREVIEW_ACTION);
		return modelAndView;
	}

	/**
	 * 运营记录-出借明细
	 * @param request
	 * @param form
	 * @return
     */
	@RequestMapping(BorrowInvestDefine.OPT_ACTION_INIT)
	@RequiresPermissions(BorrowInvestDefine.PERMISSIONS_VIEW)
	public ModelAndView optRecordTender(HttpServletRequest request, BorrowInvestBean form) {
		LogUtil.startLog(BorrowInvestController.class.toString(), BorrowInvestDefine.OPT_ACTION_INIT);
		ModelAndView modelAndView = new ModelAndView(PlanListDefine.OPT_TENDER_LIST_PATH);
		// 创建分页
		// sql优化，只拉取10天的数据 ZYK
		// 如果是从原始标的跳转过来，不默认时间，否则默认最近10天
		if (!"1".equals(form.getIsOptFlag())){
			form.setTimeStartSrch(GetDate.date2Str(GetDate.getTodayBeforeOrAfter(-10), new SimpleDateFormat("yyyy-MM-dd")));
		}
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BorrowInvestController.class.toString(), BorrowInvestDefine.OPT_ACTION_INIT);
		return modelAndView;
	}

	/**
	 * 运营记录-出借明细
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowInvestDefine.OPT_ACTION_SEARCH)
	@RequiresPermissions(BorrowInvestDefine.PERMISSIONS_VIEW)
	public ModelAndView optRecordTenderSearch(HttpServletRequest request, BorrowInvestBean form) {
		LogUtil.startLog(BorrowInvestController.class.toString(), BorrowInvestDefine.OPT_ACTION_SEARCH);
		ModelAndView modelAndView = new ModelAndView(PlanListDefine.OPT_TENDER_LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BorrowInvestController.class.toString(), BorrowInvestDefine.OPT_ACTION_SEARCH);
		return modelAndView;
	}

}
