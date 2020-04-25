package com.hyjf.admin.manager.config.htjconfig;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.DebtPlanConfig;

/**
 * 汇添金配置Controller
 * 
 * @ClassName HtjConfigController
 * @author liuyang
 * @date 2016年9月27日 上午9:08:43
 */
@Controller
@RequestMapping(value = HtjConfigDefine.REQUEST_MAPPING)
public class HtjConfigController extends BaseController {

	@Autowired
	private HtjConfigService htjConfigService;

	// 类名
	private static final String THIS_CLASS = HtjConfigController.class.toString();

	/**
	 * 列表维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(HtjConfigDefine.INIT)
	@RequiresPermissions(HtjConfigDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, HtjConfigBean form) {
		LogUtil.startLog(THIS_CLASS, HtjConfigDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(HtjConfigDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, HtjConfigDefine.INIT);
		return modelAndView;
	}

	/**
	 * 列表维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(HtjConfigDefine.SEARCH_ACTION)
	@RequiresPermissions(HtjConfigDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, HtjConfigBean form) {
		LogUtil.startLog(THIS_CLASS, HtjConfigDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(HtjConfigDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, HtjConfigDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, HtjConfigBean form) {
		int count = htjConfigService.countDebtPlanConfig(form);
		if (count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			form.setLimitStart(paginator.getOffset());
			form.setLimitEnd(paginator.getLimit());
			// 汇添金配置信息
			List<DebtPlanConfig> recordList = this.htjConfigService.selectDebtPlanConfigList(form);
			form.setPaginator(paginator);
			form.setRecordList(recordList);
		}
		modelAndView.addObject(HtjConfigDefine.HTJCONFIG_FORM, form);
	}

	/**
	 * 画面迁移(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(HtjConfigDefine.INFO_ACTION)
	@RequiresPermissions(value = { HtjConfigDefine.PERMISSIONS_ADD, HtjConfigDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView info(HttpServletRequest request, @ModelAttribute(HtjConfigDefine.HTJCONFIG_FORM) HtjConfigBean form) {
		LogUtil.startLog(THIS_CLASS, HtjConfigDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(HtjConfigDefine.INFO_PATH);
		HtjConfigBean debtPlanConfigInfo = new HtjConfigBean();
		debtPlanConfigInfo.setStatus(1);
		if (StringUtils.isNotEmpty(form.getIds())) {
			DebtPlanConfig record = this.htjConfigService.getDebtPlanConfigById(form.getIds());
			BeanUtils.copyProperties(record, debtPlanConfigInfo);
			debtPlanConfigInfo.setIds(form.getIds());
		}
		modelAndView.addObject(HtjConfigDefine.HTJCONFIG_FORM, debtPlanConfigInfo);
		LogUtil.endLog(THIS_CLASS, HtjConfigDefine.INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 更新操作
	 * 
	 * @Title updateAction
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(HtjConfigDefine.UPDATE_ACTION)
	@RequiresPermissions(value = HtjConfigDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateAction(HttpServletRequest request, HtjConfigBean form) {
		LogUtil.startLog(THIS_CLASS, HtjConfigDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(HtjConfigDefine.INFO_PATH);

		// 画面验证
		this.validatorFieldCheck(modelAndView, form, true);
		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			modelAndView.addObject(HtjConfigDefine.HTJCONFIG_FORM, form);
			return modelAndView;
		}

		this.htjConfigService.updateRecord(form);
		modelAndView.addObject(HtjConfigDefine.SUCCESS, HtjConfigDefine.SUCCESS);
		LogUtil.endLog(THIS_CLASS, HtjConfigDefine.UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 * 插入操作
	 * 
	 * @Title insertAction
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(HtjConfigDefine.INSERT_ACTION)
	@RequiresPermissions(HtjConfigDefine.PERMISSIONS_ADD)
	public ModelAndView insertAction(HttpServletRequest request, HtjConfigBean form) {
		LogUtil.startLog(THIS_CLASS, HtjConfigDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(HtjConfigDefine.INFO_PATH);

		// 画面验证
		this.validatorFieldCheck(modelAndView, form, false);

		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			modelAndView.addObject(HtjConfigDefine.HTJCONFIG_FORM, form);
			return modelAndView;
		}

		this.htjConfigService.insertRecord(form);
		modelAndView.addObject(HtjConfigDefine.SUCCESS, HtjConfigDefine.SUCCESS);

		LogUtil.endLog(THIS_CLASS, HtjConfigDefine.INSERT_ACTION);
		return modelAndView;

	}

	/**
	 * 删除汇添金配置信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(HtjConfigDefine.DELETE_ACTION)
	@RequiresPermissions(HtjConfigDefine.PERMISSIONS_DELETE)
	public ModelAndView deleteRecordAction(HttpServletRequest request, String ids) {
		LogUtil.startLog(THIS_CLASS, HtjConfigDefine.DELETE_ACTION);

		ModelAndView modelAndView = new ModelAndView(HtjConfigDefine.RE_LIST_PATH);
		// 解析json字符串
		List<Integer> recordList = JSONArray.parseArray(ids, Integer.class);
		this.htjConfigService.deleteRecord(recordList);
		LogUtil.endLog(THIS_CLASS, HtjConfigDefine.DELETE_ACTION);
		return modelAndView;
	}

	/**
	 * 产品ID,产品名称重复check
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = HtjConfigDefine.CHECK_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(HtjConfigDefine.PERMISSIONS_VIEW)
	public String checkAction(HttpServletRequest request) {
		LogUtil.startLog(THIS_CLASS, HtjConfigDefine.CHECK_ACTION);
		String name = request.getParameter("name");
		String param = request.getParameter("param");
		boolean flag = false;
		JSONObject ret = new JSONObject();
		if ("debtPlanType".equals(name)) {
			flag = this.htjConfigService.isExistsDebtPlanType(param);
			if (flag) {
				String message = ValidatorFieldCheckUtil.getErrorMessage("repeat", "");
				message = message.replace("{label}", "产品ID不能重复!");
				ret.put(HtjConfigDefine.JSON_VALID_INFO_KEY, message);
			}
		} else if ("debtPlanTypeName".equals(name)) {
			flag = this.htjConfigService.isExistsDebtPlanTypeName(param);
			if (flag) {
				String message = ValidatorFieldCheckUtil.getErrorMessage("repeat", "");
				message = message.replace("{label}", "产品名称不能重复!");
				ret.put(HtjConfigDefine.JSON_VALID_INFO_KEY, message);
			}
		} else if ("debtLockPeriod".equals(name)) {
			flag = this.htjConfigService.isExistsDebtLockPeriod(param);
			if (flag) {
				String message = ValidatorFieldCheckUtil.getErrorMessage("repeat", "");
				message = message.replace("{label}", "服务回报期限不能重复!");
				ret.put(HtjConfigDefine.JSON_VALID_INFO_KEY, message);
			}
		}
		// 没有错误时,返回y
		if (!ret.containsKey(HtjConfigDefine.JSON_VALID_INFO_KEY)) {
			ret.put(HtjConfigDefine.JSON_VALID_STATUS_KEY, HtjConfigDefine.JSON_VALID_STATUS_OK);
		}
		LogUtil.endLog(THIS_CLASS, HtjConfigDefine.CHECK_ACTION);
		return ret.toString();
	}

	/**
	 * 画面校验
	 * 
	 * @Title validatorFieldCheck
	 * @param modelAndView
	 * @param form
	 * @param isUpdate
	 */
	private void validatorFieldCheck(ModelAndView modelAndView, HtjConfigBean form, boolean isUpdate) {
		if (!isUpdate) {
			// 计划类型
			ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "debtPlanType", String.valueOf(form.getDebtPlanType()), 10, true);
			// 计划名称
			ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "debtPlanTypeName", form.getDebtPlanTypeName(), 50, true);
			// 计划前缀
			ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "debtPlanPrefix", form.getDebtPlanPrefix(), 20, true);
		}
		// 最低出借金额
		ValidatorFieldCheckUtil.validateRequired(modelAndView, "debtMinInvestment", String.valueOf(form.getDebtMinInvestment()));
		// 出借增量
		ValidatorFieldCheckUtil.validateRequired(modelAndView, "debtInvestmentIncrement", String.valueOf(form.getDebtInvestmentIncrement()));
		// 锁定期数
		ValidatorFieldCheckUtil.validateRequired(modelAndView, "debtLockPeriod", String.valueOf(form.getDebtLockPeriod()));

		// 最高出借金额
		ValidatorFieldCheckUtil.validateRequired(modelAndView, "debtMaxInvestment", String.valueOf(form.getDebtMaxInvestment()));
		// 最高出借金额
		if (form.getDebtMaxInvestment() != null && form.getDebtMaxInvestment().compareTo(BigDecimal.ZERO) > 0) {
			if (form.getDebtMaxInvestment().compareTo(form.getDebtMinInvestment()) < 0) {
				ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "debtMaxInvestment", "debtMaxInvestment.error");
			}
		}
		// 退出所需天数
		ValidatorFieldCheckUtil.validateRequired(modelAndView, "debtQuitPeriod", String.valueOf(form.getDebtQuitPeriod()));
		// 汇添金专属资产最后一笔出借金额
		ValidatorFieldCheckUtil.validateRequired(modelAndView, "investAccountLimit", String.valueOf(form.getInvestAccountLimit()));
		// 整体成交下限
		ValidatorFieldCheckUtil.validateRequired(modelAndView, "minSurplusInvestAccount", String.valueOf(form.getMinSurplusInvestAccount()));
		// 承接债转本金拆分笔数下限
		ValidatorFieldCheckUtil.validateRequired(modelAndView, "minInvestNumber", String.valueOf(form.getMinInvestNumber()));
		// 承接债转本金拆分笔数上限
		ValidatorFieldCheckUtil.validateRequired(modelAndView, "maxInvestNumber", String.valueOf(form.getMaxInvestNumber()));
		// 遍历次数
		ValidatorFieldCheckUtil.validateRequired(modelAndView, "cycleTimes", String.valueOf(form.getCycleTimes()));
		// 状态
		ValidatorFieldCheckUtil.validateRequired(modelAndView, "status", String.valueOf(form.getStatus()));
		// 常见问题
		ValidatorFieldCheckUtil.validateRequired(modelAndView, "remark", form.getRemark());
	}

}
