package com.hyjf.admin.manager.hjhplan.accedelist;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonService;
import com.hyjf.admin.manager.borrow.borrowinvest.BorrowInvestService;
import com.hyjf.bank.service.fdd.fddgeneratecontract.FddGenerateContractBean;
import com.hyjf.bank.service.fdd.fddgeneratecontract.FddGenerateContractConstant;
import com.hyjf.bank.service.fdd.fddgeneratecontract.FddGenerateContractService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.BorrowInvestCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminPlanAccedeListCustomize;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 汇计划加入明细Controller
 * 
 * @ClassName AccedeListController
 * @author LIBIN
 * @date 2017年8月16日 上午9:45:20
 */
@Controller
@RequestMapping(value = AccedeListDefine.REQUEST_MAPPING) 
public class AccedeListController extends BaseController {
	
	@Autowired
	private AccedeListService accedeListService;
	@Autowired
	private BorrowInvestService borrowInvestService;
	@Autowired
	private FddGenerateContractService fddGenerateContractService;
	@Autowired
	private BorrowCommonService  borrowCommonService;
	//类名 
	private static final String THIS_CLASS = AccedeListController.class.toString();


	@Autowired
	@Qualifier("myAmqpTemplate")
	private RabbitTemplate rabbitTemplate;
	/**
	 * 画面初期化
	 * 
	 * @Title init
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AccedeListDefine.INIT)
	@RequiresPermissions(AccedeListDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, AccedeListBean form) {
		LogUtil.startLog(THIS_CLASS, AccedeListDefine.INIT);
		ModelAndView modeAndView = new ModelAndView(AccedeListDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(THIS_CLASS, AccedeListDefine.INIT);
		return modeAndView;
	}
	
	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, AccedeListBean form) {

		int count = accedeListService.countAccedeRecord(form);
		java.math.BigDecimal num;
		java.math.BigDecimal hundred = new java.math.BigDecimal(100);
		if (count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			form.setLimitStart(paginator.getOffset());
			form.setLimitEnd(paginator.getLimit());
			// 汇计划加入明细列表
			List<AdminPlanAccedeListCustomize> recordList = this.accedeListService.selectAccedeRecordList(form);
			for (AdminPlanAccedeListCustomize adminPlanAccedeListCustomize : recordList) {
				// 自动投标进度=已出借金额/加入金额（不考虑复投）
				// 确定分母不为0
				if(adminPlanAccedeListCustomize.getJalreadyInvest() == null &&  adminPlanAccedeListCustomize.getjAccedeAccount() == null && adminPlanAccedeListCustomize.getjAccedeAccount().equals(BigDecimal.ZERO)){
					adminPlanAccedeListCustomize.setAutoBidProgress("0");
				} else {
					num = adminPlanAccedeListCustomize.getJalreadyInvest().divide(adminPlanAccedeListCustomize.getjAccedeAccount(), 2);
					adminPlanAccedeListCustomize.setAutoBidProgress(num.multiply(hundred).toString().replace(".00", ""));
				}
			}
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			/*-----add by LSY START------*/
			AdminPlanAccedeListCustomize sumAccedeRecord = this.accedeListService.sumAccedeRecord(form);
			modelAndView.addObject("sumAccedeRecord", sumAccedeRecord);
			/*-----add by LSY START------*/
		}
		modelAndView.addObject(AccedeListDefine.JOINDETAIL_FORM, form);
	}
	
	/**
	 * 检索Action
	 * 
	 * @Title searchAction
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AccedeListDefine.SEARCH_ACTION)
	@RequiresPermissions(AccedeListDefine.PERMISSIONS_SEARCH)
	public ModelAndView searchAction(HttpServletRequest request, AccedeListBean form) {
		LogUtil.startLog(THIS_CLASS, AccedeListDefine.SEARCH_ACTION);
		ModelAndView modeAndView = new ModelAndView(AccedeListDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(THIS_CLASS, AccedeListDefine.SEARCH_ACTION);
		return modeAndView;
	}
	
	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(AccedeListDefine.ENHANCE_EXPORT_ACTION)
	@RequiresPermissions(value = {AccedeListDefine.PERMISSIONS_EXPORT, AccedeListDefine.ENHANCE_PERMISSIONS_EXPORT}, logical = Logical.AND)
	public void enhanceExportAction(HttpServletRequest request, HttpServletResponse response, AccedeListBean form) throws Exception {
		LogUtil.startLog(THIS_CLASS, AccedeListDefine.ENHANCE_EXPORT_ACTION);
		java.math.BigDecimal num;
		java.math.BigDecimal hundred = new java.math.BigDecimal(100);
		// 表格sheet名称
		String sheetName = "智投订单";
		List<AdminPlanAccedeListCustomize> resultList = this.accedeListService.selectAccedeRecordList(form);
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
		String[] titles = new String[] { "序号","智投订单号", "智投编号",  "智投名称","服务回报期限", "参考年回报率","用户名（出借人）","出借人id","出借人用户属性（当前)", "分公司(当前)", "部门(当前)", "团队(当前)","推荐人（当前）","推荐人ID（当前）","推荐人姓名（当前）", "推荐人用户属性（当前）", "分公司(当前)", "部门(当前)", "团队(当前)", "出借人用户属性（出借时）","推荐人(出借时)", "推荐人ID（出借时）", "推荐人姓名（出借时）", "推荐人用户属性(出借时)", "分公司(出借时)", "部门(出借时)", "团队(出借时)", "授权服务金额",
				"自动投标进度","可用余额(元) ", "冻结金额(元) ","公允价值(元) ","实际出借利率率","操作平台","订单状态","匹配期", "开始计息时间", "授权服务时间" ,"预计开始退出时间","实际退出时间"};
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
					AdminPlanAccedeListCustomize planAccedeDetail = resultList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 计划订单号
					else if (celLength == 1) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getPlanOrderId()) ? StringUtils.EMPTY : planAccedeDetail.getPlanOrderId());
					}
					// 计划编号
					else if (celLength == 2) {
						cell.setCellValue(planAccedeDetail.getDebtPlanNid());
					}
					// 计划名称
					else if (celLength == 3) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getDebtPlanName()) ? StringUtils.EMPTY : planAccedeDetail.getDebtPlanName());
					}
					// 锁定期
					else if (celLength == 4) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getDebtLockPeriod()) ? StringUtils.EMPTY : planAccedeDetail.getDebtLockPeriod());
					}
					// 预期出借利率率
					else if (celLength == 5) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getExpectApr()) ? StringUtils.EMPTY : planAccedeDetail.getExpectApr() + "%");
					}
					// 用户名
					else if (celLength == 6) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getUserName()) ? StringUtils.EMPTY : planAccedeDetail.getUserName());
					}
					// 出借人id
					else if (celLength == 7) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getUserId()) ? StringUtils.EMPTY : planAccedeDetail.getUserId());
					}
					// 出借人当前属性
					else if (celLength == 8) {
						if ("0".equals(planAccedeDetail.getUserAttribute())) {
							cell.setCellValue("无主单");
						} else if ("1".equals(planAccedeDetail.getUserAttribute())) {
							cell.setCellValue("有主单");
						} else if ("2".equals(planAccedeDetail.getUserAttribute())) {
							cell.setCellValue("线下员工");
						} else if ("3".equals(planAccedeDetail.getUserAttribute())) {
							cell.setCellValue("线上员工");
						}else {
							cell.setCellValue(planAccedeDetail.getUserAttribute());
						}
					}
					// 出借人公司
					else if (celLength == 9) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getInviteUserRegionname2()) ? StringUtils.EMPTY : planAccedeDetail.getInviteUserRegionname2());
					}
					// 出借人公司
					else if (celLength == 10) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getInviteUserBranchname2()) ? StringUtils.EMPTY : planAccedeDetail.getInviteUserBranchname2());
					}
					// 出借人公司
					else if (celLength == 11) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getInviteUserDepartmentname2()) ? StringUtils.EMPTY : planAccedeDetail.getInviteUserDepartmentname2());
					}
					// 推荐人（当前）
					else if (celLength == 12) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getRefereeUserName()) ? StringUtils.EMPTY : planAccedeDetail.getRefereeUserName());
					}
					// 推荐人（当前）
					else if (celLength == 13) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getRefereeUserId()) ? StringUtils.EMPTY : planAccedeDetail.getRefereeUserId());
					}
					else if (celLength == 14) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getRefereeTrueName()) ? StringUtils.EMPTY : planAccedeDetail.getRefereeTrueName());
					}
					// 推荐人用户属性（当前）
					else if (celLength == 15) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getRecommendAttr()) ? StringUtils.EMPTY : planAccedeDetail.getRecommendAttr());
					}

					else if (celLength == 16) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getInviteUserRegionname1()) ? StringUtils.EMPTY : planAccedeDetail.getInviteUserRegionname1());
					}
					// 推荐人用户属性（当前）
					else if (celLength == 17) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getInviteUserBranchname1()) ? StringUtils.EMPTY : planAccedeDetail.getInviteUserBranchname1());
					}
					// 推荐人用户属性（当前）
					else if (celLength == 18) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getInviteUserDepartmentname1()) ? StringUtils.EMPTY : planAccedeDetail.getInviteUserDepartmentname1());
					}
					
					// 出借人当前属性
					else if (celLength == 19) {
						if ("0".equals(planAccedeDetail.getAttribute())) {
							cell.setCellValue("无主单");
						} else if ("1".equals(planAccedeDetail.getAttribute())) {
							cell.setCellValue("有主单");
						} else if ("2".equals(planAccedeDetail.getAttribute())) {
							cell.setCellValue("线下员工");
						} else if ("3".equals(planAccedeDetail.getAttribute())) {
							cell.setCellValue("线上员工");
						}else {
							cell.setCellValue(planAccedeDetail.getAttribute());
						}

					}
					// 推荐人用户属性（当前）
					else if (celLength == 20) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getInviteName()) ? StringUtils.EMPTY : planAccedeDetail.getInviteName());
					}
					else if (celLength == 21) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getInviteUserId()) ? StringUtils.EMPTY : planAccedeDetail.getInviteUserId());
					}
					else if (celLength == 22) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getInviteTrueName()) ? StringUtils.EMPTY : planAccedeDetail.getInviteTrueName());
					}
					// 推荐人用户属性（当前）
					else if (celLength == 23) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getInviteUserAttributeName()) ? StringUtils.EMPTY : planAccedeDetail.getInviteUserAttributeName());
					}
					// 推荐人用户属性（当前）
					else if (celLength == 24) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getInviteUserRegionname()) ? StringUtils.EMPTY : planAccedeDetail.getInviteUserRegionname());
					}
					// 推荐人用户属性（当前）
					else if (celLength == 25) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getInviteUserBranchname()) ? StringUtils.EMPTY : planAccedeDetail.getInviteUserBranchname());
					}
					// 推荐人用户属性（当前）
					else if (celLength == 26) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getInviteUserDepartmentname()) ? StringUtils.EMPTY : planAccedeDetail.getInviteUserDepartmentname());
					}
					// 加入金额
					else if (celLength == 27) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getAccedeAccount()) ? StringUtils.EMPTY : planAccedeDetail.getAccedeAccount());
					}
					// 自动投标进度
					else if (celLength == 28) {
						if(planAccedeDetail.getJalreadyInvest() == null &&  planAccedeDetail.getjAccedeAccount() == null && planAccedeDetail.getjAccedeAccount().equals(BigDecimal.ZERO)){
							cell.setCellValue("0%");
						} else {
							num = planAccedeDetail.getJalreadyInvest().divide(planAccedeDetail.getjAccedeAccount(), 2);
							cell.setCellValue(num.multiply(hundred).toString().replace(".00", "") + "%");
						}
					}
					// 可用余额
					else if (celLength == 29) {
						if(planAccedeDetail.getAvailableInvestAccount() != null){
							cell.setCellValue(planAccedeDetail.getAvailableInvestAccount());
						} else {
							cell.setCellValue("0.0");
						}
					}
					// 冻结金额
					else if (celLength == 30) {
						if(planAccedeDetail.getFrostAccount() != null){
							cell.setCellValue(planAccedeDetail.getFrostAccount());
						} else {
							cell.setCellValue("0.0");
						}
					}
					// 公允价值
					else if (celLength == 31) {
						if(planAccedeDetail.getFairValue()!= null){
						   cell.setCellValue(planAccedeDetail.getFairValue());
						} else {
							cell.setCellValue("0.0");
						}
					}
					// 实际出借利率率
					else if (celLength == 32) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getActualApr()) ? StringUtils.EMPTY : planAccedeDetail.getActualApr() + "%");
					}
					// 平台
					else if (celLength == 33) {
						if ("0".equals(planAccedeDetail.getPlatform())) {
							cell.setCellValue("PC");
						} else if ("1".equals(planAccedeDetail.getPlatform())) {
							cell.setCellValue("微官网");
						} else if ("2".equals(planAccedeDetail.getPlatform())) {
							cell.setCellValue("android");
						} else if ("3".equals(planAccedeDetail.getPlatform())) {
							cell.setCellValue("ios");
						}
					}
					// 订单状态
					else if (celLength == 34) {
						if (0 == Integer.parseInt(planAccedeDetail.getOrderStatus())) {
							cell.setCellValue("自动投标中");
						} else if (2 == Integer.parseInt(planAccedeDetail.getOrderStatus())) {
							cell.setCellValue("自动投标成功");
						} else if (3 == Integer.parseInt(planAccedeDetail.getOrderStatus())) {
							cell.setCellValue("锁定中");
						} else if (5 == Integer.parseInt(planAccedeDetail.getOrderStatus())) {
							cell.setCellValue("退出中");
						} else if (7 == Integer.parseInt(planAccedeDetail.getOrderStatus())) {
							cell.setCellValue("已退出");
						} else if (99 == Integer.parseInt(planAccedeDetail.getOrderStatus())) {
							cell.setCellValue("自动出借异常");
						}
					}
					// 匹配期
					else if (celLength == 35) {
						if (StringUtils.isNotEmpty(planAccedeDetail.getMatchDates())) {
							cell.setCellValue(planAccedeDetail.getMatchDates() + "天");
						}
					}
					// 锁定时间
					else if (celLength == 36) {
						if (StringUtils.isNotEmpty(planAccedeDetail.getCountInterestTime())) {
							cell.setCellValue(planAccedeDetail.getCountInterestTime());
						}
					}
					// 加入时间
					else if (celLength == 37) {
						if (StringUtils.isNotEmpty(planAccedeDetail.getCreateTime())) {
							cell.setCellValue(planAccedeDetail.getCreateTime());
						}
					}
					// 预计开始退出时间u
					else if (celLength == 38) {
						cell.setCellValue(planAccedeDetail.getEndDate()==null?"":GetDate.formatDate(planAccedeDetail.getEndDate()));
					}
					// 实际退出时间
					else if (celLength == 39){
						String acctualPaymentTimeStr = "";
						if (planAccedeDetail.getAcctualPaymentTime()!=0 && planAccedeDetail.getAcctualPaymentTime()!=null ){
							acctualPaymentTimeStr = GetDate.timestamptoNUMStrYYYYMMDDHHMMSS(planAccedeDetail.getAcctualPaymentTime());
						}
						cell.setCellValue(acctualPaymentTimeStr);
					}


/*					// 预期年化
					else if (celLength == 35) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getExpectApr()) ? StringUtils.EMPTY : planAccedeDetail.getExpectApr() + "%");
					}*/
					// 用户属性（当前）
/*					else if (celLength == 36) {
						if ("0".equals(planAccedeDetail.getUserAttribute())) {
							cell.setCellValue("无主单");
						} else if ("1".equals(planAccedeDetail.getUserAttribute())) {
							cell.setCellValue("有主单");
						} else if ("2".equals(planAccedeDetail.getUserAttribute())) {
							cell.setCellValue("线下员工");
						} else if ("3".equals(planAccedeDetail.getUserAttribute())) {
							cell.setCellValue("线上员工");
						}
					}*/
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(THIS_CLASS, AccedeListDefine.ENHANCE_EXPORT_ACTION);
	}

	@RequestMapping(AccedeListDefine.EXPORTEXECL)
	@RequiresPermissions(AccedeListDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, AccedeListBean form) throws Exception {
		LogUtil.startLog(THIS_CLASS, AccedeListDefine.EXPORTEXECL);
		java.math.BigDecimal num;
		java.math.BigDecimal hundred = new java.math.BigDecimal(100);
		// 表格sheet名称
		String sheetName = "智投订单";
		List<AdminPlanAccedeListCustomize> resultList = this.accedeListService.selectAccedeRecordList(form);
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "序号","智投订单号", "智投编号",  "智投名称","服务回报期限", "参考年回报率","用户名（出借人）", "出借人id","出借人用户属性（当前)", "推荐人（当前）","推荐人ID（当前）","推荐人姓名（当前）", "推荐人用户属性（当前）",
				"出借人用户属性（出借时）","推荐人(出借时)", "推荐人ID（出借时）", "推荐人姓名（出借时）", "推荐人用户属性(出借时)","授权服务金额",
				"自动投标进度","可用余额(元) ", "冻结金额(元) ","公允价值(元) ","实际出借利率率","操作平台","订单状态","匹配期", "开始计息时间", "授权服务时间", "预计开始退出时间", "实际退出时间"};
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
					AdminPlanAccedeListCustomize planAccedeDetail = resultList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 智投订单号
					else if (celLength == 1) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getPlanOrderId()) ? StringUtils.EMPTY : planAccedeDetail.getPlanOrderId());
					}
					// 智投编号
					else if (celLength == 2) {
						cell.setCellValue(planAccedeDetail.getDebtPlanNid());
					}
					// 智投名称
					else if (celLength == 3) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getDebtPlanName()) ? StringUtils.EMPTY : planAccedeDetail.getDebtPlanName());
					}
					// 服务回报期限
					else if (celLength == 4) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getDebtLockPeriod()) ? StringUtils.EMPTY : planAccedeDetail.getDebtLockPeriod());
					}
					// 参考年回报率
					else if (celLength == 5) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getExpectApr()) ? StringUtils.EMPTY : planAccedeDetail.getExpectApr() + "%");
					}
					// 用户名
					else if (celLength == 6) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getUserName()) ? StringUtils.EMPTY : planAccedeDetail.getUserName());
					}
					// 出借人id
					else if (celLength == 7) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getUserId()) ? StringUtils.EMPTY : planAccedeDetail.getUserId());
					}
					// 出借人当前属性
					else if (celLength == 8) {
						if ("0".equals(planAccedeDetail.getUserAttribute())) {
							cell.setCellValue("无主单");
						} else if ("1".equals(planAccedeDetail.getUserAttribute())) {
							cell.setCellValue("有主单");
						} else if ("2".equals(planAccedeDetail.getUserAttribute())) {
							cell.setCellValue("线下员工");
						} else if ("3".equals(planAccedeDetail.getUserAttribute())) {
							cell.setCellValue("线上员工");
						}else {
							cell.setCellValue(planAccedeDetail.getUserAttribute());
						}
					}
					// 推荐人（当前）
					else if (celLength == 9) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getRefereeUserName()) ? StringUtils.EMPTY : planAccedeDetail.getRefereeUserName());
					}
					// 推荐人（当前）
					else if (celLength == 10) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getRefereeUserId()) ? StringUtils.EMPTY : planAccedeDetail.getRefereeUserId());
					}
					else if (celLength == 11) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getRefereeTrueName()) ? StringUtils.EMPTY : planAccedeDetail.getRefereeTrueName());
					}
					// 推荐人用户属性（当前）
					else if (celLength == 12) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getRecommendAttr()) ? StringUtils.EMPTY : planAccedeDetail.getRecommendAttr());
					}
					// 出借人当前属性
					else if (celLength == 13) {
						if ("0".equals(planAccedeDetail.getAttribute())) {
							cell.setCellValue("无主单");
						} else if ("1".equals(planAccedeDetail.getAttribute())) {
							cell.setCellValue("有主单");
						} else if ("2".equals(planAccedeDetail.getAttribute())) {
							cell.setCellValue("线下员工");
						} else if ("3".equals(planAccedeDetail.getAttribute())) {
							cell.setCellValue("线上员工");
						}else {
							cell.setCellValue(planAccedeDetail.getAttribute());
						}

					}
					// 推荐人用户属性（当前）
					else if (celLength == 14) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getInviteName()) ? StringUtils.EMPTY : planAccedeDetail.getInviteName());
					}
					else if (celLength == 15) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getInviteUserId()) ? StringUtils.EMPTY : planAccedeDetail.getInviteUserId());
					}
					else if (celLength == 16) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getInviteTrueName()) ? StringUtils.EMPTY : planAccedeDetail.getInviteTrueName());
					}
					// 推荐人用户属性（当前）
					else if (celLength == 17) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getInviteUserAttributeName()) ? StringUtils.EMPTY : planAccedeDetail.getInviteUserAttributeName());
					}
					// 授权服务金额
					else if (celLength == 18) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getAccedeAccount()) ? StringUtils.EMPTY : planAccedeDetail.getAccedeAccount());
					}
					// 自动投标进度
					else if (celLength == 19) {
						if(planAccedeDetail.getJalreadyInvest() == null &&  planAccedeDetail.getjAccedeAccount() == null && planAccedeDetail.getjAccedeAccount().equals(BigDecimal.ZERO)){
							cell.setCellValue("0%");
						} else {
							num = planAccedeDetail.getJalreadyInvest().divide(planAccedeDetail.getjAccedeAccount(), 2);
							cell.setCellValue(num.multiply(hundred).toString().replace(".00", "") + "%");
						}
					}
					// 可用余额
					else if (celLength == 20) {
						if(planAccedeDetail.getAvailableInvestAccount() != null){
							cell.setCellValue(planAccedeDetail.getAvailableInvestAccount());
						} else {
							cell.setCellValue("0.0");
						}
					}
					// 冻结金额
					else if (celLength == 21) {
						if(planAccedeDetail.getFrostAccount() != null){
							cell.setCellValue(planAccedeDetail.getFrostAccount());
						} else {
							cell.setCellValue("0.0");
						}
					}
					// 公允价值
					else if (celLength == 22) {
						if(planAccedeDetail.getFairValue()!= null){
							cell.setCellValue(planAccedeDetail.getFairValue());
						} else {
							cell.setCellValue("0.0");
						}
					}
					// 实际出借利率率
					else if (celLength == 23) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getActualApr()) ? StringUtils.EMPTY : planAccedeDetail.getActualApr() + "%");
					}
					// 平台
					else if (celLength == 24) {
						if ("0".equals(planAccedeDetail.getPlatform())) {
							cell.setCellValue("PC");
						} else if ("1".equals(planAccedeDetail.getPlatform())) {
							cell.setCellValue("微官网");
						} else if ("2".equals(planAccedeDetail.getPlatform())) {
							cell.setCellValue("android");
						} else if ("3".equals(planAccedeDetail.getPlatform())) {
							cell.setCellValue("ios");
						}
					}
					// 订单状态
					else if (celLength == 25) {
						if (0 == Integer.parseInt(planAccedeDetail.getOrderStatus())) {
							cell.setCellValue("自动投标中");
						} else if (2 == Integer.parseInt(planAccedeDetail.getOrderStatus())) {
							cell.setCellValue("自动投标成功");
						} else if (3 == Integer.parseInt(planAccedeDetail.getOrderStatus())) {
							cell.setCellValue("锁定中");
						} else if (5 == Integer.parseInt(planAccedeDetail.getOrderStatus())) {
							cell.setCellValue("退出中");
						} else if (7 == Integer.parseInt(planAccedeDetail.getOrderStatus())) {
							cell.setCellValue("已退出");
						} else if (99 == Integer.parseInt(planAccedeDetail.getOrderStatus())) {
							cell.setCellValue("自动出借异常");
						}
					}
					// 匹配期
					else if (celLength == 26) {
						if (StringUtils.isNotEmpty(planAccedeDetail.getMatchDates())) {
							cell.setCellValue(planAccedeDetail.getMatchDates() + "天");
						}
					}
					// 开始计息时间
					else if (celLength == 27) {
						if (StringUtils.isNotEmpty(planAccedeDetail.getCountInterestTime())) {
							cell.setCellValue(planAccedeDetail.getCountInterestTime());
						}
					}
					// 授权服务时间
					else if (celLength == 28) {
						if (StringUtils.isNotEmpty(planAccedeDetail.getCreateTime())) {
							cell.setCellValue(planAccedeDetail.getCreateTime());
						}
					}
					// 预计开始退出时间
					else if (celLength == 29) {
						cell.setCellValue(planAccedeDetail.getEndDate()==null?"":GetDate.formatDate(planAccedeDetail.getEndDate()));
					}
					// 实际退出时间
					else if (celLength == 30){
						String acctualPaymentTimeStr = "";
						if (planAccedeDetail.getAcctualPaymentTime()!=0 && planAccedeDetail.getAcctualPaymentTime()!=null ){
							acctualPaymentTimeStr = GetDate.timestamptoNUMStrYYYYMMDDHHMMSS(planAccedeDetail.getAcctualPaymentTime());
						}
						cell.setCellValue(acctualPaymentTimeStr);
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(THIS_CLASS, AccedeListDefine.EXPORTEXECL);
	}

	/**
	 * 跳转到协议发送入力页面
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = AccedeListDefine.TOEXPORT_AGREEMENT_ACTION)
	@RequiresPermissions(AccedeListDefine.PERMISSIONS_EXPORT)
	public ModelAndView toExportAgreementAction(HttpServletRequest request, AccedeListBean form) {
		LogUtil.startLog(THIS_CLASS, AccedeListDefine.TOEXPORT_AGREEMENT_ACTION);
		ModelAndView modelAndView = new ModelAndView(AccedeListDefine.EXPORT_AGREEMENT_PATH);//输入Email的画面
		String planOrderId = request.getParameter("planOrderId");
		String planNid = request.getParameter("debtPlanNid");
		String userId = request.getParameter("userid");
		if(StringUtils.isNotEmpty(planNid)){
			HjhPlan hjhPlan = this.accedeListService.searchHjhPlanByPlanNid(planNid);
			if(hjhPlan !=null){
				modelAndView.addObject("borrowStyle", hjhPlan.getBorrowStyle());
			}
		}
		if(StringUtils.isNotEmpty(planOrderId)){
			HjhAccede accede = this.accedeListService.searchHjhAccedeByOrderId(planOrderId);
			if(accede != null){
				modelAndView.addObject("accedeOrderId", accede.getAccedeOrderId());
				modelAndView.addObject("planNid", accede.getPlanNid());
				modelAndView.addObject("lockPeriod", accede.getLockPeriod());
				modelAndView.addObject("expectApr", accede.getExpectApr());
				modelAndView.addObject("accedeAmount", accede.getAccedeAccount());
				modelAndView.addObject("orderStatus", accede.getOrderStatus());
				modelAndView.addObject("createTime", GetDate.timestamptoNUMStrYYYYMMDDHHMMSS(accede.getCreateTime()));
			}
		}
		if(StringUtils.isNotEmpty(userId)){
			Users users = this.accedeListService.getUsersByUserId(Integer.valueOf(userId));
			if(users != null){
				modelAndView.addObject("userid", users.getUserId());
				modelAndView.addObject("email", users.getEmail());
			}
		}
		LogUtil.endLog(THIS_CLASS, AccedeListDefine.TOEXPORT_AGREEMENT_ACTION);
		return modelAndView;
	}
	
	/**
	 * 点击发送协议 email入力后请求
	 * @param request
	 * @return
	 */
	@RequiresPermissions(AccedeListDefine.PERMISSIONS_EXPORT)
	@ResponseBody
	@RequestMapping(value = AccedeListDefine.EXPORT_AGREEMENT_ACTION, produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	public String exportAgreementAction(HttpServletRequest request) {
		LogUtil.startLog(THIS_CLASS, AccedeListDefine.EXPORT_AGREEMENT_ACTION);
		JSONObject ret = new JSONObject();
		String userid = request.getParameter("userid");
		String planOrderId = request.getParameter("planOrderId");
		String debtPlanNid = request.getParameter("debtPlanNid");
		String email = request.getParameter("email");
		if (StringUtils.isEmpty(email)) {
			ret.put(AccedeListDefine.JSON_STATUS_KEY, AccedeListDefine.JSON_STATUS_NG);
			ret.put(AccedeListDefine.JSON_RESULT_KEY, "请输入邮箱地址");
			return ret.toString();
		}
		if (!Validator.isEmailAddress(email)) {
			ret.put(AccedeListDefine.JSON_STATUS_KEY, AccedeListDefine.JSON_STATUS_NG);
			ret.put(AccedeListDefine.JSON_RESULT_KEY, "邮箱格式不正确!");
			return ret.toString();
		}
		if (StringUtils.isEmpty(planOrderId) || StringUtils.isEmpty(debtPlanNid)) {
			ret.put(AccedeListDefine.JSON_STATUS_KEY, AccedeListDefine.JSON_STATUS_NG);
			ret.put(AccedeListDefine.JSON_RESULT_KEY, "传递参数不正确");
			return ret.toString();
		}
		// 新旧协议发送区分
		// 通过汇计划订单号查询新协议
		String msg = new String();
		List<TenderAgreement> tenderAgreementsList= fddGenerateContractService.selectByExample(planOrderId);
		// 只有新法大大协议存DB
		if(tenderAgreementsList!=null && tenderAgreementsList.size()>0){
			// 新增发送新协议方法
			msg = accedeListService.sendFddHjhServiceAgrm(userid, planOrderId, debtPlanNid, email);
		} else {
			// 原发送旧协议不动
			msg = accedeListService.resendMessageAction(userid, planOrderId, debtPlanNid, email);
		}
		if (msg == null) {
			ret.put(AccedeListDefine.JSON_RESULT_KEY, "操作完成");
			ret.put(AccedeListDefine.JSON_STATUS_KEY, AccedeListDefine.JSON_STATUS_OK);
		} else if (!"系统异常".equals(msg)) {
			ret.put(AccedeListDefine.JSON_STATUS_KEY, AccedeListDefine.JSON_STATUS_NG);
			ret.put(AccedeListDefine.JSON_RESULT_KEY, msg);
		} else {
			ret.put(AccedeListDefine.JSON_STATUS_KEY, AccedeListDefine.JSON_STATUS_NG);
			ret.put(AccedeListDefine.JSON_RESULT_KEY, "异常纪录，请刷新后重试");
		}
		LogUtil.endLog(THIS_CLASS, AccedeListDefine.EXPORT_AGREEMENT_ACTION);
		return ret.toString();
	}
	
	/**
	 * 出借明细画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AccedeListDefine.TENDER_INFO)
	@RequiresPermissions(AccedeListDefine.PERMISSIONS_VIEW)
	public ModelAndView tenderInit(HttpServletRequest request, AccedeListBean form) {
		LogUtil.startLog(THIS_CLASS, AccedeListDefine.TENDER_INFO);
		ModelAndView modelAndView = new ModelAndView(AccedeListDefine.TENDER_INFO_LIST_PATH);
		// 创建分页
		this.createTenderPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, AccedeListDefine.TENDER_INFO);
		return modelAndView;
	}
	
	/**
	 * 出借明细创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createTenderPage(HttpServletRequest request, ModelAndView modelAndView, AccedeListBean form) {

		// 资金来源
		List<HjhInstConfig> hjhInstConfigList = this.borrowCommonService.hjhInstConfigList("");
		modelAndView.addObject("hjhInstConfigList", hjhInstConfigList);

		// 项目类型
		List<BorrowProjectType> borrowProjectTypeList = this.borrowCommonService.borrowProjectTypeList("");
		modelAndView.addObject("borrowProjectTypeList", borrowProjectTypeList);

		// 还款方式
		List<BorrowStyle> borrowStyleList = this.borrowCommonService.borrowStyleList("");
		modelAndView.addObject("borrowStyleList", borrowStyleList);

		//出借共通实体
		BorrowInvestCustomize borrowInvestCustomize = new BorrowInvestCustomize();
		// 传 汇计划加入订单号
		borrowInvestCustomize.setAccedeOrderIdSrch(form.getAccedeOrderIdSrch());
		// 传 计划编号
		borrowInvestCustomize.setPlanNidSrch(form.getDebtPlanNidSrch());
		// add by  zhangyk   出借明细-原始增加检索条件 start
		// 传 项目编号
		borrowInvestCustomize.setBorrowNidSrch(form.getBorrowNidSrch());
		// 传 推荐人
		borrowInvestCustomize.setReferrerNameSrch(form.getRefereeNameSrch());
		// 资产来源
		borrowInvestCustomize.setInstCodeSrch(form.getInstCodeSrch());
		// 传 项目类型
		borrowInvestCustomize.setProductType(form.getProjectTypeSrch());
		// 传 借款期限
		borrowInvestCustomize.setBorrowPeriod(form.getBorrowPeriod());
		// 还款方式
		borrowInvestCustomize.setBorrowStyleSrch(form.getBorrowStyleSrch());
		borrowInvestCustomize.setInstCodeSrch(form.getInstCodeSrch());
		// 传 出借时间
		borrowInvestCustomize.setTimeStartSrch(form.getTimeStartSrch());
		borrowInvestCustomize.setTimeEndSrch(form.getTimeEndSrch());
		// 传 复审通过时间
		borrowInvestCustomize.setReAuthPassStartTime(form.getReAuthPassStartSrch());
		borrowInvestCustomize.setReAuthPassEndTime(form.getReAuthPassEndSrch());
		borrowInvestCustomize.setProductType(form.getProjectTypeSrch());
		borrowInvestCustomize.setTenderType(form.getTenderType());
		// add by  zhangyk   出借明细-原始增加检索条件 end
		// 调用出借查询
		Long count = this.borrowInvestService.countBorrowInvest(borrowInvestCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			borrowInvestCustomize.setLimitStart(paginator.getOffset());
			borrowInvestCustomize.setLimitEnd(paginator.getLimit());
			List<BorrowInvestCustomize> recordList = this.borrowInvestService.selectBorrowInvestList(borrowInvestCustomize);
			form.setPaginator(paginator);
			form.setRecordLists(recordList);
			modelAndView.addObject("recordList",recordList);
			/*-----add by LSY START------*/
			String sumBorrowInvest = this.borrowInvestService.sumBorrowInvest(borrowInvestCustomize);
			modelAndView.addObject("sumBorrowInvest", sumBorrowInvest);
			/*-----add by LSY END------*/
		}
		modelAndView.addObject(AccedeListDefine.TENDER_INFO_FORM, form);
	}
	
	
	/**
	 * 发送协议(汇计划三期出借协议重发取消)
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
/*	@ResponseBody
	@RequiresPermissions(AccedeListDefine.PERMISSIONS_EXPORT)
	@RequestMapping(value = AccedeListDefine.RESEND_MESSAGE_ACTION, produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	public String resendMessageAction(HttpServletRequest request) {
		LogUtil.startLog(THIS_CLASS, AccedeListDefine.RESEND_MESSAGE_ACTION);
		JSONObject ret = new JSONObject();
		
		String userid = request.getParameter("userid");
		String planOrderId = request.getParameter("planOrderId");
		String debtPlanNid = request.getParameter("debtPlanNid");

		
		String msg = accedeListService.resendMessageAction(userid, planOrderId, debtPlanNid, null);
		
		if (msg == null) {
			ret.put(AccedeListDefine.JSON_RESULT_KEY, "操作完成");
			ret.put(AccedeListDefine.JSON_STATUS_KEY, AccedeListDefine.JSON_STATUS_OK);
		} else if (!"系统异常".equals(msg)) {
			ret.put(AccedeListDefine.JSON_STATUS_KEY, AccedeListDefine.JSON_STATUS_NG);
			ret.put(AccedeListDefine.JSON_RESULT_KEY, msg);
		} else {
			ret.put(AccedeListDefine.JSON_STATUS_KEY, AccedeListDefine.JSON_STATUS_NG);
			ret.put(AccedeListDefine.JSON_RESULT_KEY, "异常纪录，请刷新后后重试");
		}
		LogUtil.endLog(THIS_CLASS, AccedeListDefine.RESEND_MESSAGE_ACTION);
		return ret.toString();
	}*/

	/**
	 * PDF脱敏图片预览
	 * @param request
	 * @return
	 */
	@RequestMapping(AccedeListDefine.PDF_PREVIEW_ACTION)
	@RequiresPermissions(AccedeListDefine.PERMISSIONS_PDF_PREVIEW)
	public ModelAndView pdfPreviewAction(HttpServletRequest request) {
		LogUtil.startLog(THIS_CLASS, AccedeListDefine.PDF_PREVIEW_ACTION);
		ModelAndView modelAndView = new ModelAndView(AccedeListDefine.PDF_PREVIEW_PATH);
		String nid = request.getParameter("nid");
		// 根据订单号查询用户出借协议记录表
		TenderAgreement tenderAgreement = this.accedeListService.selectTenderAgreement(nid);
		if (tenderAgreement != null && StringUtils.isNotBlank(tenderAgreement.getImgUrl())) {
			String imgUrl = tenderAgreement.getImgUrl();
			String[] imgs = imgUrl.split(";");
			List<String> imgList = Arrays.asList(imgs);
			modelAndView.addObject("imgList",imgList);
			// 文件服务器
			String fileDomainUrl = PropUtils.getSystem("hyjf.ftp.url") + PropUtils.getSystem("hyjf.ftp.basepath.img");
			modelAndView.addObject("fileDomainUrl",fileDomainUrl);
		}
		LogUtil.endLog(THIS_CLASS, AccedeListDefine.PDF_PREVIEW_ACTION);
		return modelAndView;
	}


	/**
	 * PDF文件签署
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions(AccedeListDefine.PERMISSIONS_PDF_SIGN)
	@RequestMapping(value = AccedeListDefine.PDF_SIGN_ACTION, produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	public String pdfSignAction(HttpServletRequest request) {
		LogUtil.startLog(THIS_CLASS, AccedeListDefine.PDF_SIGN_ACTION);
		JSONObject ret = new JSONObject();
		// 用户ID
		String userId = request.getParameter("userId");
		// 计入加入订单号
		String planOrderId = request.getParameter("planOrderId");

		// 参数判断
		if(StringUtils.isBlank(userId) || StringUtils.isBlank(planOrderId)){
			ret.put(AccedeListDefine.JSON_RESULT_KEY, "请求参数为空");
			ret.put(AccedeListDefine.JSON_STATUS_KEY, AccedeListDefine.JSON_STATUS_NG);
			return ret.toString();
		}
		// 根据用户ID,加入计划订单号查询用户加入记录
		HjhAccede accede = this.accedeListService.selectAccedeRecord(userId,planOrderId);
		if (accede == null){
			ret.put(AccedeListDefine.JSON_RESULT_KEY, "用户加入记录不存在");
			ret.put(AccedeListDefine.JSON_STATUS_KEY, AccedeListDefine.JSON_STATUS_NG);
			return ret.toString();
		}
		Users users = this.accedeListService.getUsersByUserId(Integer.parseInt(userId));
		if(users == null ){
			ret.put(AccedeListDefine.JSON_RESULT_KEY, "用户不存在");
			ret.put(AccedeListDefine.JSON_STATUS_KEY, AccedeListDefine.JSON_STATUS_NG);
			return ret.toString();
		}
		// 查询出借协议记录表
		TenderAgreement tenderAgreement = this.borrowInvestService.selectTenderAgreement(planOrderId);
		if(tenderAgreement != null && tenderAgreement.getStatus() == 2){
			this.borrowInvestService.updateSaveSignInfo(tenderAgreement, "", FddGenerateContractConstant.PROTOCOL_TYPE_PLAN, accede.getPlanNid());
		}else {
			FddGenerateContractBean bean = new FddGenerateContractBean();
			bean.setOrdid(planOrderId);
			bean.setTenderUserId(users.getUserId());
			bean.setTenderUserName(users.getUsername());
			bean.setTransType(FddGenerateContractConstant.PROTOCOL_TYPE_PLAN);
			bean.setTenderType(2);
			bean.setSignDate(GetDate.getDateMyTimeInMillis(accede.getCountInterestTime()));
			bean.setPlanStartDate(GetDate.getDateMyTimeInMillis(accede.getCountInterestTime()));
			bean.setPlanEndDate(GetDate.getDateMyTimeInMillis(accede.getQuitTime()));
			bean.setTenderInterestFmt(String.valueOf(accede.getWaitTotal()));
			this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_GENERATE_CONTRACT, JSONObject.toJSONString(bean));
		}
		ret.put(AccedeListDefine.JSON_RESULT_KEY, "操作成功,签署MQ已发送");
		ret.put(AccedeListDefine.JSON_STATUS_KEY, AccedeListDefine.JSON_STATUS_OK);
		LogUtil.endLog(THIS_CLASS, AccedeListDefine.PDF_SIGN_ACTION);
		return ret.toString();
	}

}
