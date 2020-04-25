package com.hyjf.admin.manager.plan.plancommon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.plan.release.PlanReleaseService;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.DebtPlanConfig;
import com.hyjf.mybatis.model.customize.DebtPlanBorrowCustomize;

/**
 * 
 * @ClassName PlanCommonController
 * @Description 计划发布
 * @author liuyang
 * @date 2016年9月18日 下午2:04:11
 */
@Controller
@RequestMapping(value = PlanCommonDefine.REQUEST_MAPPING)
public class PlanCommonController extends BaseController {

	@Autowired
	private PlanCommonService planCommonService;

	@Autowired
	private PlanReleaseService planReleaseService;

	/**
	 * 迁移到详细画面
	 * 
	 * @Title moveToInfoAction
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = PlanCommonDefine.INFO_ACTION)
	@RequiresPermissions(value = PlanCommonDefine.PERMISSIONS_ADD)
	public ModelAndView moveToInfoAction(HttpServletRequest request, PlanCommonBean form) {
		LogUtil.startLog(PlanCommonController.class.toString(), PlanCommonDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(PlanCommonDefine.INFO_PATH);

		// 获取计划配置信息
		List<DebtPlanConfig> debtPlanConfigList = this.planCommonService.getDebtPlanConfigList();
		modelAndView.addObject("debtPlanConfigList", debtPlanConfigList);

		// 还款方式
		List<BorrowStyle> borrowStyleList = this.planCommonService.getBorrowStyleList();
		modelAndView.addObject("borrowStyleList", borrowStyleList);
		//
		String webhost = UploadFileUtils.getDoPath(PropUtils.getSystem("hyjf.web.host"));
		webhost = webhost.substring(0, webhost.length() - 1);
		modelAndView.addObject("webhost", webhost);

		if (StringUtils.isEmpty(form.getDebtPlanStatus()) || "0".equals(form.getDebtPlanStatus()) || "1".equals(form.getDebtPlanStatus()) || "2".equals(form.getDebtPlanStatus())) {
			// 获取资产配置信息
			this.createPage(request, modelAndView, form);
		} else {
			// 只获取已关联的资产配置信息
			this.createPlanPage(request, modelAndView, form);
		}

		// 计划预编码
		form.setDebtPlanPreNid(this.planCommonService.getPlanPreNid());
		// 计划编码
		String debtPlanNid = form.getDebtPlanNid();

		if (StringUtils.isNotEmpty(debtPlanNid)) {
			// 计划编码是否存在
			boolean isExistsRecord = this.planCommonService.isExistsRecord(debtPlanNid, StringUtils.EMPTY);
			if (isExistsRecord) {
				// 获取计划数据
				this.planCommonService.getPlanInfo(form);
			}
		}
		modelAndView.addObject(PlanCommonDefine.PLAN_FORM, form);
		LogUtil.endLog(PlanCommonController.class.toString(), PlanCommonDefine.INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 画面详情
	 * 
	 * @Title planInfoAction
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = PlanCommonDefine.DETAIL_ACTION)
	public ModelAndView planInfoAction(HttpServletRequest request, PlanCommonBean form) {
		LogUtil.startLog(PlanCommonController.class.toString(), PlanCommonDefine.DETAIL_ACTION);
		ModelAndView modelAndView = new ModelAndView(PlanCommonDefine.DETAIL_PATH);

		// 获取计划配置信息
		List<DebtPlanConfig> debtPlanConfigList = this.planCommonService.getDebtPlanConfigList();
		modelAndView.addObject("debtPlanConfigList", debtPlanConfigList);

		// 还款方式
		List<BorrowStyle> borrowStyleList = this.planCommonService.getBorrowStyleList();
		modelAndView.addObject("borrowStyleList", borrowStyleList);

		//
		String webhost = UploadFileUtils.getDoPath(PropUtils.getSystem("hyjf.web.host"));
		webhost = webhost.substring(0, webhost.length() - 1);

		if ("0".equals(form.getDebtPlanStatus()) || "1".equals(form.getDebtPlanStatus()) || "2".equals(form.getDebtPlanStatus())) {
			// 获取资产配置信息
			this.createPage(request, modelAndView, form);
		} else {
			// 只获取已关联的资产配置信息
			this.createPlanPage(request, modelAndView, form);
		}

		// 计划预编码
		form.setDebtPlanPreNid(this.planCommonService.getPlanPreNid());
		// 计划编码
		String debtPlanNid = form.getDebtPlanNid();

		if (StringUtils.isNotEmpty(debtPlanNid)) {
			// 计划编码是否存在
			boolean isExistsRecord = this.planCommonService.isExistsRecord(debtPlanNid, StringUtils.EMPTY);
			if (isExistsRecord) {
				// 获取计划数据
				this.planCommonService.getPlanInfo(form);
			}
		}
		modelAndView.addObject("webhost", webhost);
		modelAndView.addObject(PlanCommonDefine.PLAN_FORM, form);
		LogUtil.endLog(PlanCommonController.class.toString(), PlanCommonDefine.DETAIL_PATH);
		return modelAndView;
	}

	/**
	 * 根据计划类型获取计划名称
	 * 
	 * @Title getPlanName
	 * @param request
	 * @param response
	 * @param debtPlanType
	 * @param debtPlanName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = PlanCommonDefine.GET_PLAN_NAME_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String getPlanName(HttpServletRequest request, HttpServletResponse response, @RequestParam String debtPlanType) {
		LogUtil.startLog(PlanCommonController.class.toString(), PlanCommonDefine.GET_PLAN_NAME_ACTION);
		JSONObject ret = new JSONObject();
		// 计划类型
		if (StringUtils.isEmpty(debtPlanType)) {
			ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
			ret.put(CustomConstants.DATA, null);
			ret.put(CustomConstants.MSG, "无法获取计划类型");
			LogUtil.endLog(PlanCommonController.class.toString(), PlanCommonDefine.GET_PLAN_NAME_ACTION);
			return JSONObject.toJSONString(ret, true);
		}
		// 根据计划类型获取已经发布的计划数量
		int planCount = this.planCommonService.getPlanByDebtPlanType(debtPlanType);
		// 根据计划类型获取计划配置信息
		DebtPlanConfig debtPlanConfig = this.planCommonService.getPlanConfigByDebtPlanType(debtPlanType);
		// 计划名称
		String debtPlanTypeName = StringUtils.EMPTY;
		if (debtPlanConfig != null) {
			// 根据计划类型获取计划名称
			debtPlanTypeName = debtPlanConfig.getDebtPlanTypeName();
		}
		String planName = debtPlanTypeName + "第" + (planCount + 1) + "期";
		if (StringUtils.isNotEmpty(planName)) {
			ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
			ret.put(CustomConstants.DATA, planName);
			ret.put(CustomConstants.MSG, "");
		}
		LogUtil.endLog(PlanCommonController.class.toString(), PlanCommonDefine.GET_PLAN_NAME_ACTION);
		return JSONObject.toJSONString(ret, true);
	}

	/**
	 * 计划名称是否已经存在
	 * 
	 * @Title isExistsApplicant
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = PlanCommonDefine.ISDEBTPLANNAMEEXIST_ACTION, method = RequestMethod.POST)
	public String isDebtPlanNameExist(HttpServletRequest request) {
		LogUtil.startLog(PlanCommonController.class.toString(), PlanCommonDefine.ISDEBTPLANNAMEEXIST_ACTION);
		String message = this.planCommonService.isDebtPlanNameExist(request);
		LogUtil.endLog(PlanCommonController.class.toString(), PlanCommonDefine.ISDEBTPLANNAMEEXIST_ACTION);
		return message;
	}

	/**
	 * 获取最新的计划预编码
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = PlanCommonDefine.GETPLANPRENID_ACTION, method = RequestMethod.POST)
	public String getPlanPreNid(HttpServletRequest request) {
		LogUtil.startLog(PlanCommonController.class.toString(), PlanCommonDefine.GETPLANPRENID_ACTION);
		String planPreNid = this.planCommonService.getPlanPreNid();
		LogUtil.endLog(PlanCommonController.class.toString(), PlanCommonDefine.GETPLANPRENID_ACTION);
		return planPreNid;
	}

	/**
	 * 计划预编号是否重复
	 * 
	 * @Title isExistsPlanPreNidRecord
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = PlanCommonDefine.ISEXISTSPLANPRENID_ACTION, method = RequestMethod.POST)
	public String isExistsPlanPreNidRecord(HttpServletRequest request) {
		LogUtil.startLog(PlanCommonController.class.toString(), PlanCommonDefine.ISEXISTSPLANPRENID_ACTION);
		String message = this.planCommonService.isExistsPlanPreNidRecord(request);
		LogUtil.startLog(PlanCommonController.class.toString(), PlanCommonDefine.ISEXISTSPLANPRENID_ACTION);
		return message;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PlanCommonDefine.SEARCH_ACTION)
	@RequiresPermissions(PlanCommonDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, @ModelAttribute(PlanCommonDefine.PLAN_FORM) PlanCommonBean form) {
		LogUtil.startLog(PlanCommonController.class.toString(), PlanCommonDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(PlanCommonDefine.INFO_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		form.setTabName("tab_glzc_5");
		ModelAndView modelAndView1 = this.moveToInfoAction(request, form);
		modelAndView.addAllObjects(modelAndView1.getModelMap());
		LogUtil.endLog(PlanCommonController.class.toString(), PlanCommonDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 详情关联资产的检索
	 * 
	 * @Title searchInfoAction
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PlanCommonDefine.SEARCH_INFO_ACTION)
	@RequiresPermissions(PlanCommonDefine.PERMISSIONS_SEARCH)
	public ModelAndView searchInfoAction(HttpServletRequest request, PlanCommonBean form) {
		LogUtil.startLog(PlanCommonController.class.toString(), PlanCommonDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(PlanCommonDefine.DETAIL_PATH);
		ModelAndView modelAndView1 = this.planInfoAction(request, form);
		modelAndView.addAllObjects(modelAndView1.getModelMap());
		// 创建分页
		this.createPlanPage(request, modelAndView, form);
		form.setTabName("tab_glzc_5");
		LogUtil.endLog(PlanCommonController.class.toString(), PlanCommonDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, PlanCommonBean form) {

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

		// 获取关联资产件数
		int count = planCommonService.countDebtPlanBorrowList(param);
		if (count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);

			param.put("limitStart", -1);
			// 获取总计
			Map<String, Object> debtPlanBorrowMap = planCommonService.countDebtPlanAmount(param);
			if (debtPlanBorrowMap != null) {
				modelAndView.addObject("accountSum", debtPlanBorrowMap.get("accountSum"));
				modelAndView.addObject("borrowAccountWaitSum", debtPlanBorrowMap.get("borrowAccountWaitSum"));
			}
			List<DebtPlanBorrowCustomize> recordList = planCommonService.getDebtPlanBorrowList(param);
			if (recordList != null && recordList.size() > 0) {
				for (DebtPlanBorrowCustomize debtPlanBorrowCustomize : recordList) {
					List<String> debtPlanNidList = planCommonService.getDebtPlanNidListByBorrowNid(debtPlanBorrowCustomize.getBorrowNid());
					debtPlanBorrowCustomize.setDebtPlanNidList(debtPlanNidList);
					// 根据项目编号查询计划是否有被选中
					if (StringUtils.isNotEmpty(form.getDebtPlanNid())) {
						String isSelected = planCommonService.getPlanIsSelected(form.getDebtPlanNid(), debtPlanBorrowCustomize.getBorrowNid());
						debtPlanBorrowCustomize.setIsSelected(isSelected);
					}
				}

			}
			form.setPaginator(paginator);
			form.setDebtPlanBorrowList(recordList);
		}

		modelAndView.addObject(PlanCommonDefine.PLAN_FORM, form);
	}

	/**
	 * 已关联资产的创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPlanPage(HttpServletRequest request, ModelAndView modelAndView, PlanCommonBean form) {

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
	 * 汇添金添加信息
	 * 
	 * @Title insertAction
	 * @param request
	 * @param attr
	 * @param form
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(PlanCommonDefine.INSERT_ACTION)
	@RequiresPermissions(PlanCommonDefine.PERMISSIONS_ADD)
	public ModelAndView insertAction(HttpServletRequest request, RedirectAttributes attr, PlanCommonBean form) throws Exception {

		ModelAndView modelAndView = new ModelAndView(PlanCommonDefine.INFO_PATH);
		// 计划预编码
		String debtPlanPreNid = form.getDebtPlanPreNid();
		// 计划编码
		String debtPlanNid = form.getDebtPlanNid();
		// 计划类型
		String debtPlanType = form.getDebtPlanType();
		
		// 可用券配置
		if(StringUtils.isEmpty(form.getCouponConfig())){
		    form.setCouponConfig("0");
		}
		
		// 根据计划类型检索计划配置信息
		if (StringUtils.isNotEmpty(debtPlanType)) {
			DebtPlanConfig debtPlanConfig = this.planCommonService.getPlanConfigByDebtPlanType(debtPlanType);
			// 计划配置不为空的情况
			if (debtPlanConfig != null && StringUtils.isEmpty(debtPlanNid)) {
				debtPlanNid = debtPlanConfig.getDebtPlanPrefix() + debtPlanPreNid;
				// 计划编号
				form.setDebtPlanNid(debtPlanNid);
			}
		}
		// 计划编号是否存在
		boolean isExistsRecord = StringUtils.isNotEmpty(debtPlanNid) && this.planCommonService.isExistsRecord(debtPlanNid, debtPlanPreNid);

		// 画面校验
		this.planCommonService.validatorFieldCheck(modelAndView, form, isExistsRecord);
		// 如果画面校验出错
		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			// 获取计划配置信息
			List<DebtPlanConfig> debtPlanConfigList = this.planCommonService.getDebtPlanConfigList();
			modelAndView.addObject("debtPlanConfigList", debtPlanConfigList);
			// 还款方式
			List<BorrowStyle> borrowStyleList = this.planCommonService.getBorrowStyleList();

			this.createPage(request, modelAndView, form);

			modelAndView.addObject("borrowStyleList", borrowStyleList);
			modelAndView.addObject(PlanCommonDefine.PLAN_FORM, form);
			return modelAndView;
		}

		if (isExistsRecord) {
			// 更新操作
			this.planCommonService.updateRecord(form);
		} else {
			// 插入操作
			this.planCommonService.insertRecord(form);
		}
		// 关联资产不为空的情况
		if (StringUtils.isNotEmpty(form.getDebtPlanBorrowNid())) {
			// 资产配置信息更新
			List<String> debtPlanBorrowNid = JSONArray.parseArray(form.getDebtPlanBorrowNid(), String.class);
			this.planCommonService.insertDebtPlanBorrowNidRecord(form, debtPlanBorrowNid);
		}
		modelAndView = new ModelAndView("redirect:/manager/borrow/plan/init");
		return modelAndView;
	}
}
