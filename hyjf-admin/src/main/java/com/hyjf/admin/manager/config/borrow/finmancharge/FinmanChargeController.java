package com.hyjf.admin.manager.config.borrow.finmancharge;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.config.borrow.finsercharge.FinserChargeController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.BorrowFinmanCharge;

/**
 * 手续费配置
 * 
 * @author
 *
 */
@Controller
@RequestMapping(value = FinmanChargeDefine.REQUEST_MAPPING)
public class FinmanChargeController extends BaseController {

	@Autowired
	private FinmanChargeService finmanChargeService;

	/**
	 * 列表维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(FinmanChargeDefine.INIT)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(FinmanChargeDefine.FINMANCONFIGFORM) FinmanChargeBean form) {
		LogUtil.startLog(FinmanChargeController.class.toString(), FinmanChargeDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(FinmanChargeDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(FinmanChargeController.class.toString(), FinmanChargeDefine.INIT);
		return modelAndView;
	}

	/**
	 * 维护分页机能 页面初始化
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, FinmanChargeBean form) {
		List<BorrowFinmanCharge> recordList = this.finmanChargeService.getRecordList(new FinmanChargeBean(), -1, -1);
		if (recordList != null) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			recordList = this.finmanChargeService.getRecordList(new FinmanChargeBean(), paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(FinmanChargeDefine.FINMANCONFIGFORM, form);
		}
	}

	/**
	 * 画面迁移(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(FinmanChargeDefine.INFO_ACTION)
	public ModelAndView info(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(FinmanChargeDefine.FINMANCONFIGFORM) FinmanChargeBean form) {
		LogUtil.startLog(FinmanChargeController.class.toString(), FinmanChargeDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(FinmanChargeDefine.INFO_PATH);

		BorrowFinmanCharge record = new BorrowFinmanCharge();
		record.setManChargeCd(form.getManChargeCd());

		if (StringUtils.isNotEmpty(form.getManChargeCd())) {
			record = finmanChargeService.getRecord(record.getManChargeCd());
			modelAndView.addObject(FinmanChargeDefine.FINMANCONFIGFORM, record);
		}
		modelAndView.addObject("enddayMonthList", this.finmanChargeService.getParamNameList(CustomConstants.ENDDAY_MONTH));
		LogUtil.endLog(FinmanChargeController.class.toString(), FinmanChargeDefine.INIT);
		return modelAndView;
	}

	/**
	 * 添加活动信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = FinmanChargeDefine.INSERT_ACTION, method = RequestMethod.POST)
	public ModelAndView insertAction(HttpServletRequest request, FinmanChargeBean form) {
		LogUtil.startLog(FinmanChargeController.class.toString(), FinmanChargeDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(FinmanChargeDefine.INFO_PATH);
		// 画面验证
		this.validatorFieldCheck(modelAndView, form);

		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			modelAndView.addObject("enddayMonthList", this.finmanChargeService.getParamNameList(CustomConstants.ENDDAY_MONTH));
			modelAndView.addObject(FinmanChargeDefine.FINMANCONFIGFORM, form);
			return modelAndView;
		}

		// 数据插入
		this.finmanChargeService.insertRecord(form);

		// 跳转页面用（info里面有）
		modelAndView.addObject(FinmanChargeDefine.SUCCESS, FinmanChargeDefine.SUCCESS);
		LogUtil.endLog(FinmanChargeController.class.toString(), FinmanChargeDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 修改活动维护信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = FinmanChargeDefine.UPDATE_ACTION, method = RequestMethod.POST)
	public ModelAndView updateAction(HttpServletRequest request, FinmanChargeBean form) {
		LogUtil.startLog(FinmanChargeController.class.toString(), FinmanChargeDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(FinmanChargeDefine.INFO_PATH);
		// 画面验证
		this.validatorFieldCheck(modelAndView, form);

		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			modelAndView.addObject("enddayMonthList", this.finmanChargeService.getParamNameList(CustomConstants.ENDDAY_MONTH));
			modelAndView.addObject(FinmanChargeDefine.FINMANCONFIGFORM, form);
			return modelAndView;
		}

		// 更新
		this.finmanChargeService.updateRecord(form);

		modelAndView.addObject(FinmanChargeDefine.SUCCESS, FinmanChargeDefine.SUCCESS);
		LogUtil.endLog(FinmanChargeController.class.toString(), FinmanChargeDefine.UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 * 画面校验
	 * 
	 * @param modelAndView
	 * @param form
	 */
	private void validatorFieldCheck(ModelAndView modelAndView, FinmanChargeBean form) {
		// 类型
		ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "chargeTimeType", form.getChargeTimeType(), 10, true);
		// 状态
		ValidatorFieldCheckUtil.validateRequired(modelAndView, "status", form.getStatus());
		// 服务费率
		ValidatorFieldCheckUtil.validateSignlessNumLength(modelAndView, "manChargePer", form.getManChargePer(), 2, 4, true);
		// 说明
		ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "remark", form.getRemark(), 255, false);
	}

	/**
	 * 删除项目类型
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(FinmanChargeDefine.DELETE_ACTION)
	@RequiresPermissions(FinmanChargeDefine.PERMISSIONS_DELETE)
	public String deleteRecordAction(HttpServletRequest request, RedirectAttributes attr, FinmanChargeBean form) {
		LogUtil.startLog(FinmanChargeController.class.toString(), FinmanChargeDefine.DELETE_ACTION);
		this.finmanChargeService.deleteRecord(form.getManChargeCd());
		attr.addFlashAttribute(FinmanChargeDefine.FINMANCONFIGFORM, form);
		LogUtil.endLog(FinmanChargeController.class.toString(), FinmanChargeDefine.DELETE_ACTION);
		return "redirect:" + FinmanChargeDefine.REQUEST_MAPPING + "/" + FinmanChargeDefine.INIT;
	}

	/**
	 * 检查月数唯一
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = FinmanChargeDefine.CHECK_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(value = { FinmanChargeDefine.PERMISSIONS_ADD, FinmanChargeDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public String checkAction(HttpServletRequest request) {
		LogUtil.startLog(FinserChargeController.class.toString(), FinmanChargeDefine.CHECK_ACTION);

		String chargeTimeType = request.getParameter("param");

		JSONObject ret = new JSONObject();
		boolean flag = this.finmanChargeService.isManChargeTypeExists(chargeTimeType);
		if (flag) {
			String message = ValidatorFieldCheckUtil.getErrorMessage("repeat", "");
			message = message.replace("{label}", "类型");
			ret.put(FinmanChargeDefine.JSON_VALID_INFO_KEY, message);
		}

		// 没有错误时,返回y
		if (!ret.containsKey(FinmanChargeDefine.JSON_VALID_INFO_KEY)) {
			ret.put(FinmanChargeDefine.JSON_VALID_STATUS_KEY, FinmanChargeDefine.JSON_VALID_STATUS_OK);
		}
		LogUtil.endLog(FinserChargeController.class.toString(), FinmanChargeDefine.CHECK_ACTION);
		return ret.toString();
	}
}
