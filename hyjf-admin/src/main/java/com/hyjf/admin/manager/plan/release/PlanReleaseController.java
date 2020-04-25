package com.hyjf.admin.manager.plan.release;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.plan.PlanService;
import com.hyjf.admin.manager.plan.plancommon.PlanCommonDefine;
import com.hyjf.admin.manager.plan.plancommon.PlanCommonService;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.DebtPlanConfig;
import com.hyjf.mybatis.model.auto.DebtPlanWithBLOBs;
import com.hyjf.mybatis.model.customize.DebtPlanBorrowCustomize;
import com.hyjf.mybatis.model.customize.PlanCommonCustomize;

/**
 * 计划发布Controller
 * 
 * @ClassName PlanReleaseController
 * @author liuyang
 * @date 2016年9月23日 上午11:41:31
 */
@Controller
@RequestMapping(PlanReleaseDefine.REQUEST_MAPPING)
public class PlanReleaseController extends BaseController {

	@Autowired
	private PlanReleaseService planReleaseService;

	@Autowired
	private PlanService planService;

	@Autowired
	private PlanCommonService planCommonService;

	/** 类型 */
	public static String THIS_CLASS = PlanReleaseController.class.toString();

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PlanReleaseDefine.INIT)
	@RequiresPermissions(PlanReleaseDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute("PlanReleaseBean") PlanReleaseBean form) {
		LogUtil.startLog(THIS_CLASS, PlanReleaseDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(PlanReleaseDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, PlanReleaseDefine.INIT);
		return modelAndView;
	}

	/**
	 * 检索Action
	 * 
	 * @Title searchAction
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PlanReleaseDefine.SEARCH_ACTION)
	@RequiresPermissions(PlanReleaseDefine.PERMISSIONS_SEARCH)
	public ModelAndView searchAction(HttpServletRequest request, @ModelAttribute("PlanReleaseBean") PlanReleaseBean form) {
		LogUtil.startLog(THIS_CLASS, PlanReleaseDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(PlanReleaseDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, PlanReleaseDefine.INIT);
		return modelAndView;

	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, PlanReleaseBean form) {

		// 计划类型
		List<DebtPlanConfig> planTypeList = planService.getPlanTypeList();
		modelAndView.addObject("planTypeList", planTypeList);

		PlanCommonCustomize planCommonCustomize = new PlanCommonCustomize();
		// 计划编码
		planCommonCustomize.setPlanNidSrch(form.getPlanNidSrch());
		// 计划名称
		planCommonCustomize.setPlanNameSrch(form.getPlanNameSrch());
		// 计划类型 “配置中心-汇添金配置”中所有计划类型（不区分启用禁用）
		planCommonCustomize.setPlanTypeSrch(form.getPlanTypeSrch());
		// 计划状态 发起中；待审核；审核不通过；待开放；募集中；募集完成；锁定中；清算中；还款中；已还款；已流标
		planCommonCustomize.setPlanStatusSrch(form.getPlanStatusSrch());
		// 发起时间
		planCommonCustomize.setTimeStartSrch(form.getTimeStartSrch());
		planCommonCustomize.setTimeEndSrch(form.getTimeEndSrch());
		// 检索计划发布列表件数
		int count = planReleaseService.countPlanRelease(planCommonCustomize);
		if (count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			planCommonCustomize.setLimitStart(paginator.getOffset());
			planCommonCustomize.setLimitEnd(paginator.getLimit());
			// 计划发布列表
			List<DebtPlan> recordList = planReleaseService.selectPlanReleaseList(planCommonCustomize);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}
		modelAndView.addObject(PlanReleaseDefine.PLAN_FORM, form);
	}

	/**
	 * 提审详细画面
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PlanReleaseDefine.ARRAIGNMENT_ACTION)
	@RequiresPermissions(PlanReleaseDefine.PERMISSION_TISHEN)
	public ModelAndView arraignmentAction(HttpServletRequest request, PlanReleaseBean form) {
		LogUtil.startLog(THIS_CLASS, PlanReleaseDefine.ARRAIGNMENT_ACTION);
		ModelAndView modelAndView = new ModelAndView(PlanReleaseDefine.ARRAIGNMENT_PATH);
		// 计划类型
		List<DebtPlanConfig> planTypeList = planService.getPlanTypeList();
		modelAndView.addObject("planTypeList", planTypeList);
		// 计划编号
		String debtPlanNid = form.getDebtPlanNid();
		// 计划编号不为空
		if (StringUtils.isNotEmpty(debtPlanNid)) {
			// 根据计划编号检索计划详情
			DebtPlan planInfo = this.planReleaseService.selectPlanReleaseInfoByDebtPlanNid(debtPlanNid);
			modelAndView.addObject("planInfo", planInfo);
		}
		LogUtil.endLog(THIS_CLASS, PlanReleaseDefine.ARRAIGNMENT_ACTION);
		return modelAndView;
	}

	/**
	 * 确认提审
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = PlanReleaseDefine.ARRAIGNMENT_OK_ACTION)
	@RequiresPermissions(PlanReleaseDefine.PERMISSION_TISHEN)
	public ModelAndView arraignmentOkAction(HttpServletRequest request, PlanReleaseBean form) {
		LogUtil.startLog(THIS_CLASS, PlanReleaseDefine.ARRAIGNMENT_OK_ACTION);
		ModelAndView modelAndView = new ModelAndView(PlanReleaseDefine.ARRAIGNMENT_PATH);
		String debtPlanNid = form.getDebtPlanNid();
		if (StringUtils.isNotEmpty(debtPlanNid)) {
			// 确认提审
			this.planReleaseService.updatePlanRecord(debtPlanNid);
		}
		modelAndView.addObject(PlanReleaseDefine.SUCCESS, PlanReleaseDefine.SUCCESS);
		modelAndView.addObject(PlanReleaseDefine.PLAN_FORM, form);
		LogUtil.endLog(THIS_CLASS, PlanReleaseDefine.ARRAIGNMENT_OK_ACTION);
		return modelAndView;
	}

	/**
	 * 计划审核
	 * 
	 * @Title auditPlanAction
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = PlanReleaseDefine.AUDIT_PLAN_ACTION)
	@RequiresPermissions(PlanReleaseDefine.PERMISSION_AUDIT)
	public ModelAndView auditPlanAction(HttpServletRequest request, PlanReleaseBean form) {
		LogUtil.startLog(THIS_CLASS, PlanReleaseDefine.AUDIT_PLAN_ACTION);
		ModelAndView modelAndView = new ModelAndView(PlanReleaseDefine.AUDIT_PATH);
		// 获取计划配置信息
		List<DebtPlanConfig> debtPlanConfigList = this.planCommonService.getDebtPlanConfigList();
		modelAndView.addObject("debtPlanConfigList", debtPlanConfigList);
		// 计划类型
		List<DebtPlanConfig> planTypeList = planService.getPlanTypeList();
		modelAndView.addObject("planTypeList", planTypeList);

		// 还款方式
		List<BorrowStyle> borrowStyleList = this.planCommonService.getBorrowStyleList();
		modelAndView.addObject("borrowStyleList", borrowStyleList);
		//
		String webhost = UploadFileUtils.getDoPath(PropUtils.getSystem("hyjf.web.host"));
		webhost = webhost.substring(0, webhost.length() - 1);
		modelAndView.addObject("webhost",webhost);
		// 计划编号
		String debtPlanNid = form.getDebtPlanNid();
		if (StringUtils.isNotEmpty(debtPlanNid)) {
			// 计划详情
			DebtPlanWithBLOBs planInfo = this.planReleaseService.selectPlanInfoWithBLOBsByDebtPlanNid(debtPlanNid);
			// 画面详情设置
			this.setPlanInfo(planInfo, form);
			// 检索该计划选中的关联资产
			Map<String, Object> param = new HashMap<String, Object>();
			// 项目编号不为空
			if (StringUtils.isNotEmpty(form.getBorrowNidSrch())) {
				param.put("borrowNidSrch", form.getBorrowNidSrch());
			}
			// 还款方式
			if (StringUtils.isNotEmpty(form.getBorrowStyleSrch())) {
				param.put("borrowStyleSrch", form.getBorrowStyleSrch());
			}
			// 计划编号
			if (StringUtils.isNotEmpty(form.getDebtPlanNid())) {
				param.put("debtPlanNidSrch", form.getDebtPlanNid());
			}
			// 计划已关联的专属资产件数
			int count = this.planReleaseService.countDebtPlanBorrowListByDebtPlanNid(param);
			if (count > 0) {
				Paginator paginator = new Paginator(form.getPaginatorPage(), count);
				param.put("limitStart", paginator.getOffset());
				param.put("limitEnd", paginator.getLimit());
				// 获取总计
				Map<String, Object> debtPlanBorrowMap = planReleaseService.countDebtPlanBorrowListAmount(param);
				if (debtPlanBorrowMap != null) {
					modelAndView.addObject("accountSum", debtPlanBorrowMap.get("accountSum"));
					modelAndView.addObject("borrowAccountWaitSum", debtPlanBorrowMap.get("borrowAccountWaitSum"));
				}
				// 计划已关联的专属资产
				List<DebtPlanBorrowCustomize> debtPlanBorrowList = this.planReleaseService.selectDebtPlanBorrowListByDebtPlanNid(param);
				// 关联专属资产不为空
				if (debtPlanBorrowList != null && debtPlanBorrowList.size() > 0) {
					for (DebtPlanBorrowCustomize debtPlanBorrowCustomize : debtPlanBorrowList) {
						// 检索专属资产已关联的计划编号
						List<String> debtPlanNidList = planCommonService.getDebtPlanNidListByBorrowNid(debtPlanBorrowCustomize.getBorrowNid());
						debtPlanBorrowCustomize.setDebtPlanNidList(debtPlanNidList);
					}
				}
				form.setDebtPlanBorrowList(debtPlanBorrowList);
				form.setPaginator(paginator);
			}
			modelAndView.addObject(PlanReleaseDefine.PLAN_FORM, form);
		}
		LogUtil.endLog(THIS_CLASS, PlanReleaseDefine.AUDIT_PLAN_ACTION);
		return modelAndView;
	}

	/**
	 * 关联资产查询
	 * 
	 * @Title infoSearchAction
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = PlanReleaseDefine.SEARCH_INFO_ACTION)
	@RequiresPermissions(value = PlanReleaseDefine.PERMISSIONS_SEARCH)
	public ModelAndView infoSearchAction(HttpServletRequest request, PlanReleaseBean form) {
		LogUtil.startLog(THIS_CLASS, PlanReleaseDefine.SEARCH_INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(PlanReleaseDefine.AUDIT_PATH);
		// 创建分页
		this.createPlanPage(request, modelAndView, form);
		form.setTabName("tab_glzc_5");
		ModelAndView modelAndView1 = this.auditPlanAction(request, form);
		modelAndView.addAllObjects(modelAndView1.getModelMap());
		LogUtil.endLog(THIS_CLASS, PlanReleaseDefine.SEARCH_INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPlanPage(HttpServletRequest request, ModelAndView modelAndView, PlanReleaseBean form) {

		Map<String, Object> param = new HashMap<String, Object>();
		// 项目编号不为空
		if (StringUtils.isNotEmpty(form.getBorrowNidSrch())) {
			param.put("borrowNidSrch", form.getBorrowNidSrch());
		}
		// 还款方式
		if (StringUtils.isNotEmpty(form.getBorrowStyleSrch())) {
			param.put("borrowStyleSrch", form.getBorrowStyleSrch());
		}
		// 计划编号
		if (StringUtils.isNotEmpty(form.getDebtPlanNidSrch())) {
			param.put("debtPlanNidSrch", form.getDebtPlanNidSrch());
		}
		// 计划已关联的专属资产件数
		int count = this.planReleaseService.countDebtPlanBorrowListByDebtPlanNid(param);
		if (count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			param.put("limitStart", paginator.getOffset());
			param.put("limitEnd", paginator.getLimit());
			// 获取总计
			Map<String, Object> debtPlanBorrowMap = planReleaseService.countDebtPlanBorrowListAmount(param);
			if (debtPlanBorrowMap != null) {
				modelAndView.addObject("accountSum", debtPlanBorrowMap.get("accountSum"));
				modelAndView.addObject("borrowAccountWaitSum", debtPlanBorrowMap.get("borrowAccountWaitSum"));
			}
			// 计划已关联的专属资产
			List<DebtPlanBorrowCustomize> debtPlanBorrowList = this.planReleaseService.selectDebtPlanBorrowListByDebtPlanNid(param);
			// 关联专属资产不为空
			if (debtPlanBorrowList != null && debtPlanBorrowList.size() > 0) {
				for (DebtPlanBorrowCustomize debtPlanBorrowCustomize : debtPlanBorrowList) {
					// 检索专属资产已关联的计划编号
					List<String> debtPlanNidList = planCommonService.getDebtPlanNidListByBorrowNid(debtPlanBorrowCustomize.getBorrowNid());
					debtPlanBorrowCustomize.setDebtPlanNidList(debtPlanNidList);

					// 根据项目编号查询计划是否有被选中
					if (StringUtils.isNotEmpty(form.getDebtPlanNid())) {
						String isSelected = planCommonService.getPlanIsSelected(form.getDebtPlanNid(), debtPlanBorrowCustomize.getBorrowNid());
						debtPlanBorrowCustomize.setIsSelected(isSelected);
					}
				}

			}
			form.setDebtPlanBorrowList(debtPlanBorrowList);
			form.setPaginator(paginator);
		}
		modelAndView.addObject(PlanCommonDefine.PLAN_FORM, form);
	}

	/**
	 * 审核后,计划的更新
	 * 
	 * @Title updatePlanAction
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = PlanReleaseDefine.UPDATE_PLAN_ACTION)
	@RequiresPermissions(PlanReleaseDefine.PERMISSION_AUDIT)
	public ModelAndView updatePlanAction(HttpServletRequest request, PlanReleaseBean form) {
		LogUtil.startLog(THIS_CLASS, PlanReleaseDefine.UPDATE_PLAN_ACTION);
		ModelAndView modelAndView = new ModelAndView(PlanReleaseDefine.AUDIT_PATH);
		// 计划编号
		String debtPlanNid = form.getDebtPlanNid();
		if (StringUtils.isNotEmpty(debtPlanNid)) {
			this.planReleaseService.updatePlanInfo(form);
		}
		modelAndView = new ModelAndView("redirect:/manager/borrow/planrelease/init");
		LogUtil.endLog(THIS_CLASS, PlanReleaseDefine.UPDATE_PLAN_ACTION);
		return modelAndView;
	}

	/**
	 * 详情页面内容设置
	 * 
	 * @Title setPlanInfo
	 * @param planInfo
	 * @param form
	 */
	private void setPlanInfo(DebtPlanWithBLOBs planInfo, PlanReleaseBean form) {
		// 计划编号
		form.setDebtPlanNid(StringUtils.isEmpty(planInfo.getDebtPlanNid()) ? StringUtils.EMPTY : planInfo.getDebtPlanNid());
		// 计划类型
		form.setDebtPlanType(planInfo.getDebtPlanType() == null ? StringUtils.EMPTY : String.valueOf(planInfo.getDebtPlanType()));
		// 计划名称
		form.setDebtPlanName(StringUtils.isEmpty(planInfo.getDebtPlanName()) ? StringUtils.EMPTY : planInfo.getDebtPlanName());
		// 计划金额
		form.setDebtPlanMoney(planInfo.getDebtPlanMoney() == null ? StringUtils.EMPTY : String.valueOf(planInfo.getDebtPlanMoney().intValue()));
		// 锁定期
		form.setDebtLockPeriod(planInfo.getDebtLockPeriod() == null ? StringUtils.EMPTY : String.valueOf(planInfo.getDebtLockPeriod()));
		// 预期年化
		form.setExpectApr(planInfo.getExpectApr() == null ? StringUtils.EMPTY : String.valueOf(planInfo.getExpectApr()));
		// 退出方式
		form.setDebtQuitStyle(planInfo.getDebtQuitStyle() == null ? StringUtils.EMPTY : String.valueOf(planInfo.getDebtQuitStyle()));
		// 退出所需天数
		form.setDebtQuitPeriod(planInfo.getDebtQuitPeriod() == null ? StringUtils.EMPTY : String.valueOf(planInfo.getDebtQuitPeriod()));
		// 计划概念
		form.setPlanConcept(StringUtils.isEmpty(planInfo.getPlanConcept()) ? StringUtils.EMPTY : planInfo.getPlanConcept());
		// 计划原理
		form.setPlanPrinciple(StringUtils.isEmpty(planInfo.getPlanPrinciple()) ? StringUtils.EMPTY : planInfo.getPlanPrinciple());
		// 风控保障措施
		form.setSafeguardMeasures(StringUtils.isEmpty(planInfo.getSafeguardMeasures()) ? StringUtils.EMPTY : planInfo.getSafeguardMeasures());
		// 风险保证金措施
		form.setMarginMeasures(StringUtils.isEmpty(planInfo.getMarginMeasures()) ? StringUtils.EMPTY : planInfo.getMarginMeasures());
		// 计划状态
		form.setDebtPlanStatus(planInfo.getDebtPlanStatus() == null ? StringUtils.EMPTY : String.valueOf(planInfo.getDebtPlanStatus()));
		// 申购开始时间
		form.setBuyBeginTime(planInfo.getBuyBeginTime() == null ? StringUtils.EMPTY : GetDate.timestamptoStrYYYYMMDDHHMM(String.valueOf(planInfo.getBuyBeginTime())));
		// 申购期限(天)
		form.setBuyPeriodDay(planInfo.getBuyPeriodDay() == null ? StringUtils.EMPTY : String.valueOf(planInfo.getBuyPeriodDay()));
		// 申购期限(小时)
		form.setBuyPeriodHour(planInfo.getBuyPeriodHour() == null ? StringUtils.EMPTY : String.valueOf(planInfo.getBuyPeriodHour()));
		// 最低加入金额
		form.setDebtMinInvestment(planInfo.getDebtMinInvestment() == null ? StringUtils.EMPTY : String.valueOf(planInfo.getDebtMinInvestment()));
		// 递增金额
		form.setDebtInvestmentIncrement(planInfo.getDebtInvestmentIncrement() == null ? StringUtils.EMPTY : String.valueOf(planInfo.getDebtInvestmentIncrement()));
		// 最高加入金额
		form.setDebtMaxInvestment(planInfo.getDebtMaxInvestment() == null ? StringUtils.EMPTY : String.valueOf(planInfo.getDebtMaxInvestment()));
		// 审核意见
		form.setIsAudits(CustomConstants.PLAN_ISAUDITS_YES);
		// 可用券信息
		form.setCouponConfig(planInfo.getCouponConfig() == null ? StringUtils.EMPTY : String.valueOf(planInfo.getCouponConfig()));
	}
}
