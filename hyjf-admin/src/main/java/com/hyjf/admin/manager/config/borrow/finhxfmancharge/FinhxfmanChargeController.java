package com.hyjf.admin.manager.config.borrow.finhxfmancharge;

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
import com.hyjf.admin.manager.config.borrow.finsercharge.FinserChargeDefine;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.BorrowFinhxfmanCharge;

/**
 * 手续费配置
 * 
 * @author
 *
 */
@Controller
@RequestMapping(value = FinhxfmanChargeDefine.REQUEST_MAPPING)
public class FinhxfmanChargeController extends BaseController {

	@Autowired
	private FinhxfmanChargeService finhxfmanChargeService;

	/**
	 * 列表维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(FinhxfmanChargeDefine.INIT)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(FinhxfmanChargeDefine.FINHXFMANCONFIGFORM) FinhxfmanChargeBean form) {
		LogUtil.startLog(FinhxfmanChargeController.class.toString(), FinhxfmanChargeDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(FinhxfmanChargeDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(FinhxfmanChargeController.class.toString(), FinhxfmanChargeDefine.INIT);
		return modelAndView;
	}

	/**
	 * 维护分页机能 页面初始化
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, FinhxfmanChargeBean form) {
		List<BorrowFinhxfmanCharge> recordList = this.finhxfmanChargeService.getRecordList(new FinhxfmanChargeBean(), -1, -1);
		if (recordList != null) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			recordList = this.finhxfmanChargeService.getRecordList(new FinhxfmanChargeBean(), paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(FinhxfmanChargeDefine.FINHXFMANCONFIGFORM, form);
		}
	}

	/**
	 * 画面迁移(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(FinhxfmanChargeDefine.INFO_ACTION)
	public ModelAndView info(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(FinhxfmanChargeDefine.FINHXFMANCONFIGFORM) FinhxfmanChargeBean form) {
		LogUtil.startLog(FinhxfmanChargeController.class.toString(), FinhxfmanChargeDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(FinhxfmanChargeDefine.INFO_PATH);

		BorrowFinhxfmanCharge record = new BorrowFinhxfmanCharge();
		record.setManChargeCd(form.getManChargeCd());

		if (StringUtils.isNotEmpty(form.getManChargeCd())) {
			record = finhxfmanChargeService.getRecord(record.getManChargeCd());
			modelAndView.addObject(FinhxfmanChargeDefine.FINHXFMANCONFIGFORM, record);
		}
		modelAndView.addObject("enddayMonthList", this.finhxfmanChargeService.getParamNameList(CustomConstants.ENDDAY_MONTH));
		LogUtil.endLog(FinhxfmanChargeController.class.toString(), FinhxfmanChargeDefine.INIT);
		return modelAndView;
	}

	/**
	 * 添加活动信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = FinhxfmanChargeDefine.INSERT_ACTION, method = RequestMethod.POST)
	public ModelAndView insertAction(HttpServletRequest request, FinhxfmanChargeBean form) {
		LogUtil.startLog(FinhxfmanChargeController.class.toString(), FinhxfmanChargeDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(FinhxfmanChargeDefine.INFO_PATH);
		// 画面验证
		this.validatorFieldCheck(modelAndView, form);

		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			modelAndView.addObject("enddayMonthList", this.finhxfmanChargeService.getParamNameList(CustomConstants.ENDDAY_MONTH));
			modelAndView.addObject(FinhxfmanChargeDefine.FINHXFMANCONFIGFORM, form);
			return modelAndView;
		}

		// 数据插入
		this.finhxfmanChargeService.insertRecord(form);

		// 跳转页面用（info里面有）
		modelAndView.addObject(FinhxfmanChargeDefine.SUCCESS, FinhxfmanChargeDefine.SUCCESS);
		LogUtil.endLog(FinhxfmanChargeController.class.toString(), FinhxfmanChargeDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 修改活动维护信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = FinhxfmanChargeDefine.UPDATE_ACTION, method = RequestMethod.POST)
	public ModelAndView updateAction(HttpServletRequest request, FinhxfmanChargeBean form) {
		LogUtil.startLog(FinhxfmanChargeController.class.toString(), FinhxfmanChargeDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(FinhxfmanChargeDefine.INFO_PATH);
		// 画面验证
		this.validatorFieldCheck(modelAndView, form);

		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			modelAndView.addObject("enddayMonthList", this.finhxfmanChargeService.getParamNameList(CustomConstants.ENDDAY_MONTH));
			modelAndView.addObject(FinhxfmanChargeDefine.FINHXFMANCONFIGFORM, form);
			return modelAndView;
		}

		// 更新
		this.finhxfmanChargeService.updateRecord(form);

		modelAndView.addObject(FinhxfmanChargeDefine.SUCCESS, FinhxfmanChargeDefine.SUCCESS);
		LogUtil.endLog(FinhxfmanChargeController.class.toString(), FinhxfmanChargeDefine.UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 * 画面校验
	 * 
	 * @param modelAndView
	 * @param form
	 */
	private void validatorFieldCheck(ModelAndView modelAndView, FinhxfmanChargeBean form) {
		// 类型
		ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "chargeTimeType", form.getChargeTimeType(), 10, true);
		// 状态
		ValidatorFieldCheckUtil.validateRequired(modelAndView, "status", form.getStatus());
		// 服务费率
		boolean manChargePerFlag = ValidatorFieldCheckUtil.validateSignlessNumLength(modelAndView, "manChargePer", form.getManChargePer(), 2, 4, true);
		// 服务费率
		boolean manChargePerEndFlag = ValidatorFieldCheckUtil.validateSignlessNumLength(modelAndView, "manChargePerEnd", form.getManChargePerEnd(), 2, 4, true);

		if (manChargePerFlag && manChargePerEndFlag) {
			if ((form.getManChargePer()).compareTo(form.getManChargePerEnd()) < 0) {
				ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "manChargePer", "manager.up.down");
			}
		}

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
	@RequestMapping(FinhxfmanChargeDefine.DELETE_ACTION)
	@RequiresPermissions(FinhxfmanChargeDefine.PERMISSIONS_DELETE)
	public String deleteRecordAction(HttpServletRequest request, RedirectAttributes attr, FinhxfmanChargeBean form) {
		LogUtil.startLog(FinhxfmanChargeController.class.toString(), FinhxfmanChargeDefine.DELETE_ACTION);
		this.finhxfmanChargeService.deleteRecord(form.getManChargeCd());
		attr.addFlashAttribute(FinhxfmanChargeDefine.FINHXFMANCONFIGFORM, form);
		LogUtil.endLog(FinhxfmanChargeController.class.toString(), FinhxfmanChargeDefine.DELETE_ACTION);
		return "redirect:" + FinhxfmanChargeDefine.REQUEST_MAPPING + "/" + FinhxfmanChargeDefine.INIT;
	}

	/**
	 * 检查月数唯一
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = FinhxfmanChargeDefine.CHECK_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(value = { FinhxfmanChargeDefine.PERMISSIONS_ADD, FinhxfmanChargeDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public String checkAction(HttpServletRequest request) {
		LogUtil.startLog(FinserChargeController.class.toString(), FinhxfmanChargeDefine.CHECK_ACTION);
		String chargeCd = request.getParameter("chargeCd");
		String name = request.getParameter("name");
		String param = request.getParameter("param");

		JSONObject ret = new JSONObject();
		if (name.equals("chargeTimeType")) {
			boolean flag = this.finhxfmanChargeService.isManChargeTypeExists(param);
			if (flag) {
				String message = ValidatorFieldCheckUtil.getErrorMessage("repeat", "");
				message = message.replace("{label}", "类型");
				ret.put(FinhxfmanChargeDefine.JSON_VALID_INFO_KEY, message);
			}
		}
		// 检查月数是否是唯一
		if ("chargeTime".equals(name)) {
			boolean flag = finhxfmanChargeService.onlyOneMonth(chargeCd, param);
			if (flag) {
				String message = ValidatorFieldCheckUtil.getErrorMessage("repeat", "");
				message = message.replace("{label}", "月数");
				ret.put(FinserChargeDefine.JSON_VALID_INFO_KEY, message);
			}
		}

		// 没有错误时,返回y
		if (!ret.containsKey(FinhxfmanChargeDefine.JSON_VALID_INFO_KEY)) {
			ret.put(FinhxfmanChargeDefine.JSON_VALID_STATUS_KEY, FinhxfmanChargeDefine.JSON_VALID_STATUS_OK);
		}
		LogUtil.endLog(FinserChargeController.class.toString(), FinhxfmanChargeDefine.CHECK_ACTION);
		return ret.toString();
	}
}
