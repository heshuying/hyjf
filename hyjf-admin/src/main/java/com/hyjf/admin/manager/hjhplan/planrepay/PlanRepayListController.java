package com.hyjf.admin.manager.hjhplan.planrepay;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.borrow.borrowrepayment.plan.BorrowRepaymentPlanBean;
import com.hyjf.admin.manager.borrow.borrowrepayment.plan.BorrowRepaymentPlanController;
import com.hyjf.admin.manager.borrow.borrowrepaymentinfo.infolist.BorrowRepaymentInfoListDefine;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.HjhPlan;
import com.hyjf.mybatis.model.auto.HjhRepay;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.BorrowRepaymentCustomize;
import com.hyjf.mybatis.model.customize.HjhCreditTenderCustomize;
import com.hyjf.mybatis.model.customize.PlanCommonCustomize;
import com.hyjf.mybatis.model.customize.web.hjh.HjhPlanRepayCustomize;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author: LIBIN
 * @description:汇计划计划还款所用控制器
 * @version: 1
 * @date: 2017年8月11日
 */
@Controller
@RequestMapping(value = PlanRepayListDefine.REQUEST_MAPPING)
public class PlanRepayListController extends BaseController {
	
	@Autowired
	private PlanRepayListService planRepayListService;
	
	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PlanRepayListDefine.INIT)
	@RequiresPermissions(PlanRepayListDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute("PlanRepayListBean") PlanRepayListBean form) {
		LogUtil.startLog(PlanRepayListController.class.toString(), PlanRepayListDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(PlanRepayListDefine.LIST_PATH);
		// 创建分页,时间默认当天
		Date endDate = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		form.setRepayTimeStart(simpleDateFormat.format(DateUtils.addDays(endDate, 0)));
		form.setRepayTimeEnd(simpleDateFormat.format(DateUtils.addDays(endDate, 0)));
		
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(PlanRepayListController.class.toString(), PlanRepayListDefine.INIT);
		return modelAndView;
	}
	
	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PlanRepayListDefine.SEARCH_ACTION)
	@RequiresPermissions(PlanRepayListDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, PlanRepayListBean form) {
		LogUtil.startLog(PlanRepayListController.class.toString(), PlanRepayListDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(PlanRepayListDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(PlanRepayListController.class.toString(), PlanRepayListDefine.INIT);
		return modelAndView;
	}
	
	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, PlanRepayListBean form) {
		// 初始化总计数据
		BigDecimal sumAccedeAccount = BigDecimal.ZERO;
		BigDecimal sumRepayInterest = BigDecimal.ZERO;
		BigDecimal sumRepayWait = BigDecimal.ZERO;
		// 汇计划三期 实际收益 总计
		BigDecimal sumActualRevenue = BigDecimal.ZERO;
		// 汇计划三期 实际回款 总计
		BigDecimal sumActualPayTotal = BigDecimal.ZERO;
		// 汇计划三期 清算服务费 总计
		BigDecimal sumLqdServiceFee = BigDecimal.ZERO;
	
		PlanCommonCustomize PlanCommonCustomize = new PlanCommonCustomize();
		// 取查询加入订单号
		PlanCommonCustomize.setPlanOrderId(form.getAccedeOrderIdSrch());
		// 取查询计划编码
		PlanCommonCustomize.setPlanNidSrch(form.getPlanNidSrch());
		// 取查询用户名
		PlanCommonCustomize.setUserName(form.getUserNameSrch());
		// 取锁定期
		PlanCommonCustomize.setDebtLockPeriodSrch(form.getDebtLockPeriodSrch());
		// 取回款状态 ：0 未回款，1 部分回款 2 已回款'
		PlanCommonCustomize.setRepayStatus(form.getRepayStatusSrch());
		// 取订单状态 ：0 自动投标中 1锁定中 2退出中 3已退出'
		PlanCommonCustomize.setOrderStatusSrch(form.getOrderStatusSrch());
		// 取还款方式
		PlanCommonCustomize.setBorrowStyleSrch(form.getBorrowStyleSrch());
		//应还款日期开始
		PlanCommonCustomize.setRepayTimeStart(form.getRepayTimeStart());
		//应还款日期结束
		PlanCommonCustomize.setRepayTimeEnd(form.getRepayTimeEnd());
		//计划实际还款时间开始
		PlanCommonCustomize.setActulRepayTimeStart(form.getActulRepayTimeStart());
		//计划实际还款时间结束
		PlanCommonCustomize.setActulRepayTimeEnd(form.getActulRepayTimeEnd());
		// 取排序
		PlanCommonCustomize.setSort(form.getSort());
		// 取排序列
		PlanCommonCustomize.setCol(form.getCol());
		// 汇计划三期增加 推荐人检索
		PlanCommonCustomize.setRefereeNameSrch(form.getRefereeNameSrch());
//		// 查询总条数
//		int count = planRepayListService.countRepayPlan(PlanCommonCustomize);
		HashMap<String,Object> paraMap = new HashMap<String,Object>();
		if(StringUtils.isNotEmpty(form.getAccedeOrderIdSrch())){
			paraMap.put("planOrderId", form.getAccedeOrderIdSrch());
		}
		if(StringUtils.isNotEmpty(form.getPlanNidSrch())){
			paraMap.put("planNidSrch", form.getPlanNidSrch());
		}
		if(StringUtils.isNotEmpty(form.getUserNameSrch())){
			paraMap.put("userName", form.getUserNameSrch());
		}
		if(StringUtils.isNotEmpty(form.getDebtLockPeriodSrch())){
			paraMap.put("debtLockPeriodSrch", form.getDebtLockPeriodSrch());
		}
		if(StringUtils.isNotEmpty(form.getRepayStatusSrch())){
			paraMap.put("repayStatus", Integer.valueOf(form.getRepayStatusSrch()));
		}
		if(StringUtils.isNotEmpty(form.getOrderStatusSrch())){
			paraMap.put("orderStatusSrch", Integer.valueOf(form.getOrderStatusSrch()));
		}
		if(StringUtils.isNotEmpty(form.getBorrowStyleSrch())){
			paraMap.put("borrowStyleSrch", form.getBorrowStyleSrch());
		}
		if(StringUtils.isNotEmpty(form.getRepayTimeStart())){
			paraMap.put("repayTimeStart", GetDate.getDayStart10(form.getRepayTimeStart()));
		}
		if(StringUtils.isNotEmpty(form.getRepayTimeEnd())){
			paraMap.put("repayTimeEnd", GetDate.getDayEnd10(form.getRepayTimeEnd()));
		}
		if(StringUtils.isNotEmpty(form.getActulRepayTimeStart())){
			paraMap.put("actulRepayTimeStart", GetDate.getDayStart10(form.getActulRepayTimeStart()));
		}
		if(StringUtils.isNotEmpty(form.getActulRepayTimeEnd())){
			paraMap.put("actulRepayTimeEnd", GetDate.getDayEnd10(form.getActulRepayTimeEnd()));
		}
		// 汇计划三期新增 推荐人查询
		if(StringUtils.isNotEmpty(form.getRefereeNameSrch())){
			paraMap.put("refereeNameSrch", form.getRefereeNameSrch());
		}
		List<HjhPlanRepayCustomize> resultList = planRepayListService.exportPlanRepayList(paraMap);
		Paginator paginator = new Paginator(form.getPaginatorPage(), resultList.size());
		if (resultList.size() > 0) {
			if(form.getPaginatorPage()==0){
				form.setPaginatorPage(1);
			}
            // 未带分页查询(因为总结是计算全部)
			List<HjhPlanRepayCustomize> recordList1 = planRepayListService.exportPlanRepayList(paraMap);
			for(int i = 0; i < recordList1.size(); i++){
				/*------add by LSY START-------*/
				sumAccedeAccount = sumAccedeAccount.add(recordList1.get(i).getAccedeAccount());
				sumRepayInterest = sumRepayInterest.add(recordList1.get(i).getRepayInterest());
				sumRepayWait = sumRepayWait.add(recordList1.get(i).getRepayWait());
				/*------add by LSY END-------*/
				sumActualRevenue = sumActualRevenue.add(recordList1.get(i).getActualRevenue());
				sumActualPayTotal = sumActualPayTotal.add(recordList1.get(i).getActualPayTotal());
				sumLqdServiceFee = sumLqdServiceFee.add(new BigDecimal(recordList1.get(i).getLqdServiceFee()));
			}
			// 带分页检索
			paraMap.put("limitStart", paginator.getOffset());
			paraMap.put("limitEnd", paginator.getLimit());
			List<HjhPlanRepayCustomize> recordList = planRepayListService.exportPlanRepayList(paraMap);
			List<HjhRepayBean> list=new ArrayList();
			//根据计划编号获取计划锁定期天月和还款方式
			for(int i = 0; i < recordList.size(); i++){
				if(recordList.get(i).getPlanNid()!=null){
					String planNid=recordList.get(i).getPlanNid();
					HjhPlan hjhplan=planRepayListService.getPlan(planNid);
					HjhRepay hrBean=new HjhRepayBean();
					//父类
					hrBean=recordList.get(i);
					//子类
					HjhRepayBean hrBean2=new HjhRepayBean();
					BeanUtils.copyProperties(hrBean, hrBean2);
					if(hjhplan!=null){
						if(hjhplan.getIsMonth()!=null){
							hrBean2.setIsMonth(hjhplan.getIsMonth());
						}
						if(hjhplan.getBorrowStyle()!=null){
							hrBean2.setBorrowStyle(hjhplan.getBorrowStyle());
						}
					}
					list.add(hrBean2);
				}
			}
			form.setPaginator(paginator);
			modelAndView.addObject("accedeOrderId", form.getAccedeOrderIdSrch());
			modelAndView.addObject("recordList", list);
			modelAndView.addObject("sumAccedeAccount", sumAccedeAccount);
			modelAndView.addObject("sumRepayInterest", sumRepayInterest);
			modelAndView.addObject("sumRepayWait", sumRepayWait);
			modelAndView.addObject("sumActualRevenue", sumActualRevenue);
			modelAndView.addObject("sumActualPayTotal", sumActualPayTotal);
			modelAndView.addObject("sumLqdServiceFee", sumLqdServiceFee);
		}
		modelAndView.addObject(PlanRepayListDefine.PLAN_LIST_FORM, form);
	}
	
	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(PlanRepayListDefine.ENHANCE_EXPORT_ACTION)
	@RequiresPermissions(value = {PlanRepayListDefine.PERMISSIONS_EXPORT, PlanRepayListDefine.ENHANCE_PERMISSIONS_EXPORT}, logical = Logical.AND)
	public void enhanceExportAction(HttpServletRequest request, HttpServletResponse response, PlanRepayListBean form) throws Exception {
		LogUtil.startLog(PlanRepayListController.class.toString(), PlanRepayListDefine.ENHANCE_EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "智投退出";
		
		HashMap<String,Object> paraMap = new HashMap<String,Object>();
		if(StringUtils.isNotEmpty(form.getAccedeOrderIdSrch())){
			paraMap.put("planOrderId", form.getAccedeOrderIdSrch());
		}
		if(StringUtils.isNotEmpty(form.getPlanNidSrch())){
			paraMap.put("planNidSrch", form.getPlanNidSrch());
		}
		if(StringUtils.isNotEmpty(form.getUserNameSrch())){
			paraMap.put("userName", form.getUserNameSrch());
		}
		if(StringUtils.isNotEmpty(form.getDebtLockPeriodSrch())){
			paraMap.put("debtLockPeriodSrch", form.getDebtLockPeriodSrch());
		}
		if(StringUtils.isNotEmpty(form.getRepayStatusSrch())){
			paraMap.put("repayStatus", Integer.valueOf(form.getRepayStatusSrch()));
		}
		if(StringUtils.isNotEmpty(form.getOrderStatusSrch())){
			paraMap.put("orderStatusSrch", Integer.valueOf(form.getOrderStatusSrch()));
		}
		if(StringUtils.isNotEmpty(form.getBorrowStyleSrch())){
			paraMap.put("borrowStyleSrch", form.getBorrowStyleSrch());
		}
		if(StringUtils.isNotEmpty(form.getRepayTimeStart())){
			paraMap.put("repayTimeStart", GetDate.getDayStart10(form.getRepayTimeStart()));
		}
		if(StringUtils.isNotEmpty(form.getRepayTimeEnd())){
			paraMap.put("repayTimeEnd", GetDate.getDayEnd10(form.getRepayTimeEnd()));
		}
		if(StringUtils.isNotEmpty(form.getActulRepayTimeStart())){
			paraMap.put("actulRepayTimeStart", GetDate.getDayStart10(form.getActulRepayTimeStart()));
		}
		if(StringUtils.isNotEmpty(form.getActulRepayTimeEnd())){
			paraMap.put("actulRepayTimeEnd", GetDate.getDayEnd10(form.getActulRepayTimeEnd()));
		}
		// 汇计划三期新增 推荐人查询
		if(StringUtils.isNotEmpty(form.getRefereeNameSrch())){
			paraMap.put("refereeNameSrch", form.getRefereeNameSrch());
		}
		List<HjhPlanRepayCustomize> resultList = planRepayListService.exportPlanRepayList(paraMap);
		//根据计划编号获取计划锁定期天月和还款方式
		for(int i = 0; i < resultList.size(); i++){
			if(resultList.get(i).getPlanNid()!=null){
				String planNid=resultList.get(i).getPlanNid();
				HjhPlan hjhplan=planRepayListService.getPlan(planNid);
				if(hjhplan.getIsMonth()!=null){
					resultList.get(i).setIsMonth(hjhplan.getIsMonth());
				}
				if(hjhplan.getBorrowStyle()!=null){
					resultList.get(i).setBorrowStyle(hjhplan.getBorrowStyle());
				}
			}
			
		}
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
		String[] titles = new String[] { 
				"序号","智投订单号", "智投编号", "智投名称", "服务回报期限","参考年回报率","用户名（出借人）",
				"出借人用户属性（当前）", 
				"推荐人(当前)", "分公司(当前)", "部门(当前)", "团队(当前)",
				"授权服务金额(元)", "参考回报(元)",
				"实际收益(元)", "已退出金额(元)",
				"还款方式","订单状态",  "实际退出时间", "预计开始退出时间","清算服务费" ,"清算服务费率" ,
				"出借服务费率" ,"清算进度","最晚退出时间","授权服务时间","开始计息时间"};
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
					HjhPlanRepayCustomize planAccedeDetail = resultList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 计划订单号
					else if (celLength == 1) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getAccedeOrderId()) ? StringUtils.EMPTY : planAccedeDetail.getAccedeOrderId());
					}
					// 计划编号
					else if (celLength == 2) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getPlanNid()) ? StringUtils.EMPTY : planAccedeDetail.getPlanNid());
					}
					// 计划名称
					else if (celLength == 3) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getPlanName()) ? StringUtils.EMPTY : planAccedeDetail.getPlanName());
					}
					// 锁定期
					else if (celLength == 4) {
						if (planAccedeDetail.getLockPeriod()  == null) {
							cell.setCellValue(0);
						} else {
							if(planAccedeDetail.getIsMonth()==0){
								cell.setCellValue(planAccedeDetail.getLockPeriod()+ "天");
							} 
							if(planAccedeDetail.getIsMonth()==1){
								cell.setCellValue(planAccedeDetail.getLockPeriod()+ "个月");
							}
							
						}
					}
					// 预期出借利率率
					else if (celLength == 5) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getExpectApr()) ? StringUtils.EMPTY : planAccedeDetail.getExpectApr() + "%");
					}
					// 用户名
					else if (celLength == 6) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getUserName()) ? StringUtils.EMPTY : planAccedeDetail.getUserName());
					}
					// 出借人用户属性（当前）
					else if (celLength == 7) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getRecommendAttr()) ? StringUtils.EMPTY : planAccedeDetail.getRecommendAttr());
					}
					//推荐人用户属性
/*					else if (celLength == 7) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getInviteUserAttributeName()) ? StringUtils.EMPTY : planAccedeDetail.getInviteUserAttributeName());
					}*/
					// 推荐人用户名（当前）
					else if (celLength == 8) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getInviteUserName()) ? StringUtils.EMPTY : planAccedeDetail.getInviteUserName());
					}
					// 推荐人用户部门信息（当前）
					else if (celLength == 9) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getInviteUserRegionName()) ? StringUtils.EMPTY : planAccedeDetail.getInviteUserRegionName());
					}
					// 推荐人用户部门信息（当前）
					else if (celLength == 10) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getInviteUserBranchName()) ? StringUtils.EMPTY : planAccedeDetail.getInviteUserBranchName());
					}
					// 推荐人用户部门信息（当前）
					else if (celLength == 11) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getInviteUserDepartmentName()) ? StringUtils.EMPTY : planAccedeDetail.getInviteUserDepartmentName());
					}
/*					// 推荐人用户属性（退出时）
					else if (celLength == 12) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getRecommendAttr()) ? StringUtils.EMPTY : planAccedeDetail.getRecommendAttr());
					}*/
					// 加入金额
					else if (celLength == 12) {
						if (planAccedeDetail.getAccedeAccount()  == null) {
							cell.setCellValue(0);
						} else {
							cell.setCellValue(planAccedeDetail.getAccedeAccount().toString());
						}
					}
					// 预期收益
					else if (celLength == 13) {
						if (planAccedeDetail.getRepayInterest()  == null) {
							cell.setCellValue(0);
						} else {
							cell.setCellValue(planAccedeDetail.getRepayInterest().toString());
						}
					}
					//实际收益(元) 
					else if (celLength == 14) {
						if (planAccedeDetail.getActualRevenue()  == null) {
							cell.setCellValue(0);
						} else {
							cell.setCellValue(planAccedeDetail.getActualRevenue().toString());
						}
					}
					//实际回款总额(元)
					else if (celLength == 15) {
						if (planAccedeDetail.getActualPayTotal()  == null) {
							cell.setCellValue(0);
						} else {
							cell.setCellValue(planAccedeDetail.getActualPayTotal().toString());
						}
					}
	/*				// 回款状态
					else if (celLength == 16) {
						if (0 == planAccedeDetail.getRepayStatus()) {
							cell.setCellValue("未回款");
						} else if (1 == planAccedeDetail.getRepayStatus()) {
							cell.setCellValue("部分回款");
						} else if (2 == planAccedeDetail.getRepayStatus()) {
							cell.setCellValue("已回款");
						}
					}*/
					//还款方式
					else if(celLength == 16){
						if (planAccedeDetail.getBorrowStyle()  == null) {
							cell.setCellValue("");
						} else {
							if(planAccedeDetail.getBorrowStyle().equals("month")){
								cell.setCellValue("等额本息");
							}if(planAccedeDetail.getBorrowStyle().equals("season")){
								cell.setCellValue("按季还款");
							}if(planAccedeDetail.getBorrowStyle().equals("end")){
								cell.setCellValue("按月计息，到期还本还息");
							}if(planAccedeDetail.getBorrowStyle().equals("endmonth")){
								cell.setCellValue("先息后本");
							}if(planAccedeDetail.getBorrowStyle().equals("endday")){
								cell.setCellValue("按天计息，到期还本还息");
							}if(planAccedeDetail.getBorrowStyle().equals("endmonths")){
								cell.setCellValue("按月付息到期还本");
							}if(planAccedeDetail.getBorrowStyle().equals("principal")){
								cell.setCellValue("等额本金");
							}
						}
					}
					// 已回款
/*					else if (celLength == 15) {
						if (planAccedeDetail.getRepayAlready()  == null) {
							cell.setCellValue(0);
						} else {
							cell.setCellValue(planAccedeDetail.getRepayAlready().toString());
						}
					}*/
					// 待回款
/*					else if (celLength == 16) {
						if (planAccedeDetail.getRepayWait()  == null) {
							cell.setCellValue(0);
						} else {
							cell.setCellValue(planAccedeDetail.getRepayWait().toString());
						}
					}*/
					// 已还本金
/*					else if (celLength == 17) {
						if (planAccedeDetail.getPlanRepayCapital()  == null) {
							cell.setCellValue(0);
						} else {
							cell.setCellValue(planAccedeDetail.getPlanRepayCapital().toString());
						}
					}*/
					// 已还利息
/*					else if (celLength == 18) {
						if (planAccedeDetail.getPlanRepayInterest()  == null) {
							cell.setCellValue(0);
						} else {
							cell.setCellValue(planAccedeDetail.getPlanRepayInterest().toString());
						}
					}*/
					// 订单状态
					else if (celLength == 17) {
						if (0 == planAccedeDetail.getOrderStatus()) {
							cell.setCellValue("自动投标中");
						} else if (2 == planAccedeDetail.getOrderStatus()) {
							cell.setCellValue("自动投标成功");
						} else if (3 == planAccedeDetail.getOrderStatus()) {
							cell.setCellValue("锁定中");
						} else if (5 == planAccedeDetail.getOrderStatus()) {
							cell.setCellValue("退出中");
						} else if (7 == planAccedeDetail.getOrderStatus()) {
							cell.setCellValue("已退出");
						} else if (99 == planAccedeDetail.getOrderStatus()) {
							cell.setCellValue("自动出借异常");
						}
					}
					// 计划实际还款时间 (实际退出时间) 
					else if (celLength == 18) {
						if (planAccedeDetail.getRepayActualTime()  == null) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(GetDate.timestamptoNUMStrYYYYMMDDHHMMSS(planAccedeDetail.getRepayActualTime()));
						}
					}
					// 清算时间  
					else if (celLength == 19) {
						if (planAccedeDetail.getRepayShouldTime()  == null) {
							cell.setCellValue(0);
						} else {
							cell.setCellValue(GetDate.timestamptoStrYYYYMMDDHHMMSS(planAccedeDetail.getRepayShouldTime()));
						}
					}
					// 清算服务费
					else if (celLength == 20) {
						if (planAccedeDetail.getLqdServiceFee()  == null) {
							cell.setCellValue(0);
						} else {
							cell.setCellValue(planAccedeDetail.getLqdServiceFee().toString());
						}
					}
					// 清算服务费率
					else if (celLength == 21) {
						if (planAccedeDetail.getLqdServiceApr()  == null) {
							cell.setCellValue(0);
						} else {
							cell.setCellValue(planAccedeDetail.getLqdServiceApr().toString());
						}
					}
					// 出借服务费率
					else if (celLength == 22) {
						if (planAccedeDetail.getInvestServiceApr()  == null) {
							cell.setCellValue(0);
						} else {
							cell.setCellValue(planAccedeDetail.getInvestServiceApr());
						}
					}
					// 清算进度
					else if (celLength == 23) {
						if (planAccedeDetail.getLqdProgress()  == null) {
							cell.setCellValue(0);
						} else {
							cell.setCellValue(planAccedeDetail.getLqdProgress());
						}
					}
					// 最晚退出时间
					else if (celLength == 24) {
						if (StringUtils.isEmpty(planAccedeDetail.getLastQuitTime())) {
							cell.setCellValue(0);
						} else {
							cell.setCellValue(planAccedeDetail.getLastQuitTime());
						}
					}
					// 汇计划加入时间
					else if (celLength == 25) {
						if (planAccedeDetail.getJoinTime()  == 0) {
							cell.setCellValue(0);
						} else {
							cell.setCellValue(GetDate.timestamptoStrYYYYMMDDHHMMSS(planAccedeDetail.getJoinTime()));
						}
					}
					// 订单锁定时间 = 加入计划的计息时间
					else if (celLength == 26) {
						if (planAccedeDetail.getOrderLockTime() == 0) {
							cell.setCellValue(0);
						} else {
							cell.setCellValue(GetDate.timestamptoStrYYYYMMDDHHMMSS(planAccedeDetail.getOrderLockTime()));
						}
					}	
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(PlanRepayListController.class.toString(), PlanRepayListDefine.ENHANCE_EXPORT_ACTION);
	}

	@RequestMapping(PlanRepayListDefine.EXPORTEXECL)
	@RequiresPermissions(PlanRepayListDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, PlanRepayListBean form) throws Exception {
		LogUtil.startLog(PlanRepayListController.class.toString(), PlanRepayListDefine.EXPORTEXECL);
		// 表格sheet名称
		String sheetName = "智投退出";

		HashMap<String,Object> paraMap = new HashMap<String,Object>();
		if(StringUtils.isNotEmpty(form.getAccedeOrderIdSrch())){
			paraMap.put("planOrderId", form.getAccedeOrderIdSrch());
		}
		if(StringUtils.isNotEmpty(form.getPlanNidSrch())){
			paraMap.put("planNidSrch", form.getPlanNidSrch());
		}
		if(StringUtils.isNotEmpty(form.getUserNameSrch())){
			paraMap.put("userName", form.getUserNameSrch());
		}
		if(StringUtils.isNotEmpty(form.getDebtLockPeriodSrch())){
			paraMap.put("debtLockPeriodSrch", form.getDebtLockPeriodSrch());
		}
		if(StringUtils.isNotEmpty(form.getRepayStatusSrch())){
			paraMap.put("repayStatus", Integer.valueOf(form.getRepayStatusSrch()));
		}
		if(StringUtils.isNotEmpty(form.getOrderStatusSrch())){
			paraMap.put("orderStatusSrch", Integer.valueOf(form.getOrderStatusSrch()));
		}
		if(StringUtils.isNotEmpty(form.getBorrowStyleSrch())){
			paraMap.put("borrowStyleSrch", form.getBorrowStyleSrch());
		}
		if(StringUtils.isNotEmpty(form.getRepayTimeStart())){
			paraMap.put("repayTimeStart", GetDate.getDayStart10(form.getRepayTimeStart()));
		}
		if(StringUtils.isNotEmpty(form.getRepayTimeEnd())){
			paraMap.put("repayTimeEnd", GetDate.getDayEnd10(form.getRepayTimeEnd()));
		}
		if(StringUtils.isNotEmpty(form.getActulRepayTimeStart())){
			paraMap.put("actulRepayTimeStart", GetDate.getDayStart10(form.getActulRepayTimeStart()));
		}
		if(StringUtils.isNotEmpty(form.getActulRepayTimeEnd())){
			paraMap.put("actulRepayTimeEnd", GetDate.getDayEnd10(form.getActulRepayTimeEnd()));
		}
		// 汇计划三期新增 推荐人查询
		if(StringUtils.isNotEmpty(form.getRefereeNameSrch())){
			paraMap.put("refereeNameSrch", form.getRefereeNameSrch());
		}
		List<HjhPlanRepayCustomize> resultList = planRepayListService.exportPlanRepayList(paraMap);
		//根据计划编号获取计划锁定期天月和还款方式
		for(int i = 0; i < resultList.size(); i++){
			if(resultList.get(i).getPlanNid()!=null){
				String planNid=resultList.get(i).getPlanNid();
				HjhPlan hjhplan=planRepayListService.getPlan(planNid);
				if(hjhplan.getIsMonth()!=null){
					resultList.get(i).setIsMonth(hjhplan.getIsMonth());
				}
				if(hjhplan.getBorrowStyle()!=null){
					resultList.get(i).setBorrowStyle(hjhplan.getBorrowStyle());
				}
			}

		}
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		String[] titles = new String[] {
				"序号","智投订单号", "智投编号", "智投名称", "服务回报期限","参考年回报率","用户名（出借人）",
				"出借人用户属性（当前）",
				"推荐人(当前)",
				"授权服务金额(元)", "参考回报(元)",
				"实际收益(元)", "已退出金额(元)",
				"还款方式","订单状态",  "实际退出时间", "预计开始退出时间","清算服务费" ,"清算服务费率" ,
				"出借服务费率" ,"清算进度","最晚退出时间","授权服务时间","开始计息时间"};
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
					HjhPlanRepayCustomize planAccedeDetail = resultList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 智投订单号
					else if (celLength == 1) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getAccedeOrderId()) ? StringUtils.EMPTY : planAccedeDetail.getAccedeOrderId());
					}
					// 智投编号
					else if (celLength == 2) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getPlanNid()) ? StringUtils.EMPTY : planAccedeDetail.getPlanNid());
					}
					// 智投名称
					else if (celLength == 3) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getPlanName()) ? StringUtils.EMPTY : planAccedeDetail.getPlanName());
					}
					// 服务回报期限
					else if (celLength == 4) {
						if (planAccedeDetail.getLockPeriod()  == null) {
							cell.setCellValue(0);
						} else {
							if(planAccedeDetail.getIsMonth()==0){
								cell.setCellValue(planAccedeDetail.getLockPeriod()+ "天");
							}
							if(planAccedeDetail.getIsMonth()==1){
								cell.setCellValue(planAccedeDetail.getLockPeriod()+ "个月");
							}

						}
					}
					// 参考年回报率
					else if (celLength == 5) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getExpectApr()) ? StringUtils.EMPTY : planAccedeDetail.getExpectApr() + "%");
					}
					// 用户名
					else if (celLength == 6) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getUserName()) ? StringUtils.EMPTY : planAccedeDetail.getUserName());
					}
					// 出借人用户属性（当前）
					else if (celLength == 7) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getRecommendAttr()) ? StringUtils.EMPTY : planAccedeDetail.getRecommendAttr());
					}
					// 推荐人用户名（当前）
					else if (celLength == 8) {
						cell.setCellValue(StringUtils.isEmpty(planAccedeDetail.getInviteUserName()) ? StringUtils.EMPTY : planAccedeDetail.getInviteUserName());
					}
					// 授权服务金额
					else if (celLength == 9) {
						if (planAccedeDetail.getAccedeAccount()  == null) {
							cell.setCellValue(0);
						} else {
							cell.setCellValue(planAccedeDetail.getAccedeAccount().toString());
						}
					}
					// 参考回报
					else if (celLength == 10) {
						if (planAccedeDetail.getRepayInterest()  == null) {
							cell.setCellValue(0);
						} else {
							cell.setCellValue(planAccedeDetail.getRepayInterest().toString());
						}
					}
					//实际收益(元)
					else if (celLength == 11) {
						if (planAccedeDetail.getActualRevenue()  == null) {
							cell.setCellValue(0);
						} else {
							cell.setCellValue(planAccedeDetail.getActualRevenue().toString());
						}
					}
					//已退出金额(元)
					else if (celLength == 12) {
						if (planAccedeDetail.getActualPayTotal()  == null) {
							cell.setCellValue(0);
						} else {
							cell.setCellValue(planAccedeDetail.getActualPayTotal().toString());
						}
					}
					//还款方式
					else if(celLength == 13){
						if (planAccedeDetail.getBorrowStyle()  == null) {
							cell.setCellValue("");
						} else {
							if(planAccedeDetail.getBorrowStyle().equals("month")){
								cell.setCellValue("等额本息");
							}if(planAccedeDetail.getBorrowStyle().equals("season")){
								cell.setCellValue("按季还款");
							}if(planAccedeDetail.getBorrowStyle().equals("end")){
								cell.setCellValue("按月计息，到期还本还息");
							}if(planAccedeDetail.getBorrowStyle().equals("endmonth")){
								cell.setCellValue("先息后本");
							}if(planAccedeDetail.getBorrowStyle().equals("endday")){
								cell.setCellValue("按天计息，到期还本还息");
							}if(planAccedeDetail.getBorrowStyle().equals("endmonths")){
								cell.setCellValue("按月付息到期还本");
							}if(planAccedeDetail.getBorrowStyle().equals("principal")){
								cell.setCellValue("等额本金");
							}
						}
					}
					// 订单状态
					else if (celLength == 14) {
						if (0 == planAccedeDetail.getOrderStatus()) {
							cell.setCellValue("自动投标中");
						} else if (2 == planAccedeDetail.getOrderStatus()) {
							cell.setCellValue("自动投标成功");
						} else if (3 == planAccedeDetail.getOrderStatus()) {
							cell.setCellValue("锁定中");
						} else if (5 == planAccedeDetail.getOrderStatus()) {
							cell.setCellValue("退出中");
						} else if (7 == planAccedeDetail.getOrderStatus()) {
							cell.setCellValue("已退出");
						} else if (99 == planAccedeDetail.getOrderStatus()) {
							cell.setCellValue("自动出借异常");
						}
					}
					// 计划实际还款时间 (实际退出时间)
					else if (celLength == 15) {
						if (planAccedeDetail.getRepayActualTime()  == null) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(GetDate.timestamptoStrYYYYMMDDHHMMSS(planAccedeDetail.getRepayActualTime()));
						}
					}
					// 预计开始退出时间
					else if (celLength == 16) {
						if (planAccedeDetail.getRepayShouldTime()  == null) {
							cell.setCellValue(0);
						} else {
							cell.setCellValue(GetDate.timestamptoStrYYYYMMDDHHMMSS(planAccedeDetail.getRepayShouldTime()));
						}
					}
					// 清算服务费
					else if (celLength == 17) {
						if (planAccedeDetail.getLqdServiceFee()  == null) {
							cell.setCellValue(0);
						} else {
							cell.setCellValue(planAccedeDetail.getLqdServiceFee().toString());
						}
					}
					// 清算服务费率
					else if (celLength == 18) {
						if (planAccedeDetail.getLqdServiceApr()  == null) {
							cell.setCellValue(0);
						} else {
							cell.setCellValue(planAccedeDetail.getLqdServiceApr().toString());
						}
					}
					// 出借服务费率
					else if (celLength == 19) {
						if (planAccedeDetail.getInvestServiceApr()  == null) {
							cell.setCellValue(0);
						} else {
							cell.setCellValue(planAccedeDetail.getInvestServiceApr());
						}
					}
					// 清算进度
					else if (celLength == 20) {
						if (planAccedeDetail.getLqdProgress()  == null) {
							cell.setCellValue(0);
						} else {
							cell.setCellValue(planAccedeDetail.getLqdProgress());
						}
					}
					// 最晚退出时间
					else if (celLength == 21) {
						if (StringUtils.isEmpty(planAccedeDetail.getLastQuitTime())) {
							cell.setCellValue(0);
						} else {
							cell.setCellValue(planAccedeDetail.getLastQuitTime());
						}
					}
					// 授权服务时间
					else if (celLength == 22) {
						if (planAccedeDetail.getJoinTime()  == 0) {
							cell.setCellValue(0);
						} else {
							cell.setCellValue(GetDate.timestamptoStrYYYYMMDDHHMMSS(planAccedeDetail.getJoinTime()));
						}
					}
					// 开始计息时间 (订单锁定时间 = 加入计划的计息时间)
					else if (celLength == 23) {
						if (planAccedeDetail.getOrderLockTime() == 0) {
							cell.setCellValue(0);
						} else {
							cell.setCellValue(GetDate.timestamptoStrYYYYMMDDHHMMSS(planAccedeDetail.getOrderLockTime()));
						}
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(PlanRepayListController.class.toString(), PlanRepayListDefine.EXPORTEXECL);
	}
	
	/**
	 * 还款明细画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PlanRepayListDefine.REPAY_INFO)
	@RequiresPermissions(PlanRepayListDefine.PERMISSIONS_VIEW)
	public ModelAndView repayInit(HttpServletRequest request, PlanRepayListBean form) {
		LogUtil.startLog(PlanRepayListController.class.toString(), PlanRepayListDefine.REPAY_INFO);
		ModelAndView modelAndView = new ModelAndView(PlanRepayListDefine.REPAY_INFO_LIST_PATH);
		// 创建分页
		this.createRepayPage(request, modelAndView, form);
		LogUtil.endLog(PlanRepayListController.class.toString(), PlanRepayListDefine.REPAY_INFO);
		return modelAndView;
	}
	
	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createRepayPage(HttpServletRequest request, ModelAndView modelAndView, PlanRepayListBean form) {
		//汇直投还款信息 --- 共通实体
		BorrowRepaymentCustomize borrowRepaymentCustomize = new BorrowRepaymentCustomize();
		// 传 汇计划加入订单号
		borrowRepaymentCustomize.setAccedeOrderIdSrch(form.getAccedeOrderIdSrch());
		// tab专用
		modelAndView.addObject("orderId", form.getAccedeOrderIdSrch());
		// 查询总数
		Long count = this.planRepayListService.countBorrowRepayment(borrowRepaymentCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			borrowRepaymentCustomize.setLimitStart(paginator.getOffset());
			borrowRepaymentCustomize.setLimitEnd(paginator.getLimit());
			List<BorrowRepaymentCustomize> recordList = this.planRepayListService.selectBorrowRepaymentList(borrowRepaymentCustomize);
			for(BorrowRepaymentCustomize object : recordList){
			    String borrowStyle = object.getBorrowStyle();// 项目还款方式
			 // 是否月标(true:月标, false:天标)
		        boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
		                || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
		         object.setIsMonth(isMonth);
				 String userId = object.getBorrowUserId();//取borrow表userID(借款人ID)
				 if(userId!=null && userId!=""){
					 Users users = this.planRepayListService.getUsersByUserId(Integer.valueOf(userId));
					 object.setBorrowUserName(users.getUsername());//将借款人用户名set进
			     } else {
			    	 object.setBorrowUserName("");//将借款人用户名set进
			     }
			 }
			form.setPaginator(paginator);
			modelAndView.addObject("accedeOrderId", form.getAccedeOrderIdSrch());
			modelAndView.addObject("recordList", recordList);
			/*-------add by LSY START-----------*/
			BorrowRepaymentCustomize sumObject = this.planRepayListService.sumBorrowRepayment(borrowRepaymentCustomize);
			modelAndView.addObject("sumObject", sumObject);
			/*-------add by LSY END-----------*/
		}
		modelAndView.addObject(PlanRepayListDefine.REPAY_INFO_FORM, form);
	}
	/**
     * 跳转到还款明细
     * 
     * @param request
     * @param form
     */
    @RequestMapping(PlanRepayListDefine.REPAY_PLAN_DETAIL_ACTION)
    @RequiresPermissions(PlanRepayListDefine.PERMISSIONS_INFO)
    public ModelAndView toHuankuanjihuaAction(HttpServletRequest request, BorrowRepaymentPlanBean form, RedirectAttributes attr) {
        LogUtil.startLog(PlanRepayListController.class.toString(), PlanRepayListDefine.REPAY_PLAN_DETAIL_ACTION);
        ModelAndView modelAndView = new ModelAndView(BorrowRepaymentInfoListDefine.RE_LIST_PATH);
        attr.addAttribute("borrowNid", form.getBorrowNid());
        attr.addAttribute("recoverPeriod", form.getRepayPeriod());// 此处的repayPeriod就等于目的地的recoverPeriod
        attr.addAttribute("accedeOrderId", form.getAccedeOrderId());
        attr.addAttribute("nid", form.getNid());
        attr.addAttribute("isMonth", form.getIsMonth());
        attr.addAttribute(BorrowRepaymentInfoListDefine.ACTFROM, BorrowRepaymentInfoListDefine.ACTFROMPLAN);
        // infoForm.setbo(form.getBorrowNid());
        // modelAndView.addObject("form", infoForm);
        // 跳转到还款计划
        LogUtil.endLog(BorrowRepaymentPlanController.class.toString(), PlanRepayListDefine.REPAY_PLAN_DETAIL_ACTION);
        return modelAndView;
    }
    
	/**
	 * (债转)还款明细画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PlanRepayListDefine.CREDIT_REPAY_INFO)
	@RequiresPermissions(PlanRepayListDefine.PERMISSIONS_VIEW)
	public ModelAndView creditRepayInit(HttpServletRequest request, PlanRepayListBean form) {
		LogUtil.startLog(PlanRepayListController.class.toString(), PlanRepayListDefine.CREDIT_REPAY_INFO);
		ModelAndView modelAndView = new ModelAndView(PlanRepayListDefine.CREDIT_REPAY_INFO_LIST_PATH);//债转还款详细画面
		this.createCreditRepayPage(request, modelAndView, form);
		LogUtil.endLog(PlanRepayListController.class.toString(), PlanRepayListDefine.CREDIT_REPAY_INFO);
		return modelAndView;
	}
    
	
	/**
     * (债转汇总-hyjf_hjh_debt_credit_tender)还款明细画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(PlanRepayListDefine.CREDIT_TENDER_INFO)
    @RequiresPermissions(PlanRepayListDefine.PERMISSIONS_VIEW)
    public ModelAndView creditTenderInit(HttpServletRequest request, PlanRepayListBean form) {
        LogUtil.startLog(PlanRepayListController.class.toString(), PlanRepayListDefine.CREDIT_TENDER_INFO);
        ModelAndView modelAndView = new ModelAndView(PlanRepayListDefine.CREDIT_TENDER_INFO_LIST_PATH);//债转还款详细画面
        this.createCreditTenderPage(request, modelAndView, form);
        LogUtil.endLog(PlanRepayListController.class.toString(), PlanRepayListDefine.CREDIT_TENDER_INFO);
        return modelAndView;
    }
    
	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createCreditRepayPage(HttpServletRequest request, ModelAndView modelAndView, PlanRepayListBean form) {
		//还款信息 --- 共通实体
		BorrowRepaymentCustomize borrowRepaymentCustomize = new BorrowRepaymentCustomize();
		borrowRepaymentCustomize.setAccedeOrderIdSrch(form.getAccedeOrderIdSrch());
		
		// tab专用
		modelAndView.addObject("orderId", form.getAccedeOrderIdSrch());
		// 查询债转还款信息总数
		Long count = this.planRepayListService.countCreditBorrowRepayment(borrowRepaymentCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			borrowRepaymentCustomize.setLimitStart(paginator.getOffset());
			borrowRepaymentCustomize.setLimitEnd(paginator.getLimit());
			// 查询债转承接
			List<BorrowRepaymentCustomize> recordList = this.planRepayListService.selectCreditBorrowRepaymentList(borrowRepaymentCustomize);
			for(BorrowRepaymentCustomize object : recordList){
			    String borrowStyle = object.getBorrowStyle();// 项目还款方式
			 // 是否月标(true:月标, false:天标)
		        boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
		                || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
		         object.setIsMonth(isMonth);
			 }
			form.setPaginator(paginator);
			modelAndView.addObject("accedeOrderId", form.getAccedeOrderIdSrch());
			modelAndView.addObject("recordList", recordList);
		}
		modelAndView.addObject(PlanRepayListDefine.REPAY_INFO_FORM, form);
		
	}
	
	/**
     * 创建分页机能
     * 
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createCreditTenderPage(HttpServletRequest request, ModelAndView modelAndView, PlanRepayListBean form) {
        //还款信息 --- 共通实体
        HjhCreditTenderCustomize borrowRepaymentCustomize = new HjhCreditTenderCustomize();
        borrowRepaymentCustomize.setAccedeOrderIdSrch(form.getAccedeOrderIdSrch());
        
        // tab专用
        modelAndView.addObject("orderId", form.getAccedeOrderIdSrch());
        // 查询债转还款信息总数
        Long count = this.planRepayListService.countCreditBorrowTender(borrowRepaymentCustomize);
        if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
            borrowRepaymentCustomize.setLimitStart(paginator.getOffset());
            borrowRepaymentCustomize.setLimitEnd(paginator.getLimit());
            // 查询债转承接
            List<HjhCreditTenderCustomize> recordList = this.planRepayListService.selectCreditBorrowTendertList(borrowRepaymentCustomize);
            for(HjhCreditTenderCustomize object : recordList){
                String borrowStyle = object.getBorrowStyle();// 项目还款方式
             // 是否月标(true:月标, false:天标)
                boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
                        || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
                 object.setIsMonth(isMonth);
             }
            form.setPaginator(paginator);
            modelAndView.addObject("accedeOrderId", form.getAccedeOrderIdSrch());
            modelAndView.addObject("recordList", recordList);
        }
        modelAndView.addObject(PlanRepayListDefine.REPAY_INFO_FORM, form);
        
    }
   
}
