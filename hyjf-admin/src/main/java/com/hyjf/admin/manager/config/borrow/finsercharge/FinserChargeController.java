package com.hyjf.admin.manager.config.borrow.finsercharge;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
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
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.BorrowFinserCharge;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.customize.FinserChargeCustomize;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = FinserChargeDefine.REQUEST_MAPPING)
public class FinserChargeController extends BaseController {

	@Autowired
	private FinserChargeService finserChargeService;
	@Autowired
	private BorrowCommonService borrowCommonService;

	/**
	 * 维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(FinserChargeDefine.INIT)
	@RequiresPermissions(FinserChargeDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(FinserChargeDefine.FINSERCHARGE_FORM) FinserChargeBean form) {
		LogUtil.startLog(FinserChargeController.class.toString(), FinserChargeDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(FinserChargeDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(FinserChargeController.class.toString(), FinserChargeDefine.INIT);
		return modelAndView;
	}

	/**
	 * 创建维护分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, FinserChargeBean form) {
		int total = this.finserChargeService.countRecordTotal(form);
		if (total > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), total);
			List<FinserChargeCustomize> recordList = this.finserChargeService.getRecordList(form, paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
			modelAndView.addObject(FinserChargeDefine.FINSERCHARGE_FORM, form);
		}
	}

	/**
	 * 迁移到维护详细画面
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = FinserChargeDefine.INFO_ACTION)
	@RequiresPermissions(value = { FinserChargeDefine.PERMISSIONS_ADD, FinserChargeDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView moveToInfoAction(HttpServletRequest request, FinserChargeBean form) {
		LogUtil.startLog(FinserChargeController.class.toString(), FinserChargeDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(FinserChargeDefine.INFO_PATH);
		BorrowFinserCharge record = new BorrowFinserCharge();
		// 前台传递id
		record.setChargeCd(form.getChargeCd());
		if (StringUtils.isNotEmpty(record.getChargeCd())) {
			// 根据主键判断该条数据在数据库中是否存在
			boolean isExists = this.finserChargeService.isExistsRecord(record);

			// 没有添加 同时 也没能检索出数据的时候异常
			if (!isExists) {
				Subject currentUser = SecurityUtils.getSubject();
				currentUser.checkPermission(FinserChargeDefine.PERMISSIONS_ADD);
			}
			// 根据主键检索数据
			record = this.finserChargeService.getRecord(record);
		}
		// 项目类型
		List<BorrowProjectType> borrowProjectTypeList = this.borrowCommonService.borrowProjectTypeList(CustomConstants.HZT);
		modelAndView.addObject("enddayMonthList", this.finserChargeService.getParamNameList(CustomConstants.ENDDAY_MONTH));
		modelAndView.addObject(FinserChargeDefine.FINSERCHARGE_FORM, record);
		modelAndView.addObject("borrowProjectTypeList",borrowProjectTypeList);
		LogUtil.endLog(FinserChargeController.class.toString(), FinserChargeDefine.INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 添加维护信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = FinserChargeDefine.INSERT_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(value = { FinserChargeDefine.PERMISSIONS_ADD })
	public ModelAndView insertAction(HttpServletRequest request, FinserChargeBean form) {
		LogUtil.startLog(FinserChargeController.class.toString(), FinserChargeDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(FinserChargeDefine.INFO_PATH);
		// 画面验证
		this.validatorFieldCheck(modelAndView, form);

		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			modelAndView.addObject("enddayMonthList", this.finserChargeService.getParamNameList(CustomConstants.ENDDAY_MONTH));
			modelAndView.addObject(FinserChargeDefine.FINSERCHARGE_FORM, form);
			return modelAndView;
		}

		this.finserChargeService.insertRecord(form);
		modelAndView.addObject(FinserChargeDefine.SUCCESS, FinserChargeDefine.SUCCESS);
		LogUtil.endLog(FinserChargeController.class.toString(), FinserChargeDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 修改维护信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = FinserChargeDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(value = { FinserChargeDefine.PERMISSIONS_MODIFY })
	public ModelAndView updateAction(HttpServletRequest request, FinserChargeBean form) {
		LogUtil.startLog(FinserChargeController.class.toString(), FinserChargeDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(FinserChargeDefine.INFO_PATH);
		// 画面验证
		this.validatorFieldCheck(modelAndView, form);

		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			modelAndView.addObject("enddayMonthList", this.finserChargeService.getParamNameList(CustomConstants.ENDDAY_MONTH));
			modelAndView.addObject(FinserChargeDefine.FINSERCHARGE_FORM, form);
			return modelAndView;
		}

		this.finserChargeService.updateRecord(form);
		modelAndView.addObject(FinserChargeDefine.SUCCESS, FinserChargeDefine.SUCCESS);
		LogUtil.endLog(FinserChargeController.class.toString(), FinserChargeDefine.UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 * 画面校验
	 * 
	 * @param modelAndView
	 * @param form
	 */
	private void validatorFieldCheck(ModelAndView modelAndView, FinserChargeBean form) {
		// 类型
		boolean chargeTimeTypeFlag = ValidatorFieldCheckUtil.validateRequired(modelAndView, "chargeTimeType", form.getChargeTimeType());
		// 期限
		boolean chargeTimeFlag = true;
		if (chargeTimeTypeFlag && !"endday".equals(form.getChargeTimeType())) {
			chargeTimeFlag = ValidatorFieldCheckUtil.validateSignlessNum(modelAndView, "chargeTime", form.getChargeTime(), 10, true);
		}
		// 服务费率
		ValidatorFieldCheckUtil.validateSignlessNumLength(modelAndView, "chargeRate", form.getChargeRate(), 2, 10, true);
		// 说明
		ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "remark", form.getRemark(), 255, false);
		// 状态
		ValidatorFieldCheckUtil.validateRequired(modelAndView, "status", form.getStatus());

		String chargeCd = form.getChargeCd();
		
		Integer projectType = form.getProjectType();
		
		projectType = projectType == null ? 0:projectType;

		if (chargeTimeTypeFlag && "endday".equals(form.getChargeTimeType())) {
			boolean flag = finserChargeService.onlyOneDay(chargeCd, form.getChargeTime(),projectType);
			if (flag) {
				ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "chargeTimeType-repeat", "charge.time.type.repeat");
			}
		}

		if (chargeTimeFlag && "month".equals(form.getChargeTimeType())) {
			boolean onlyOneMonthFlag = finserChargeService.onlyOneMonth(chargeCd, form.getChargeTime(),projectType);
			if (onlyOneMonthFlag) {
				ValidatorFieldCheckUtil.validateRequired(modelAndView, "onlyOneMonth", "only.one.month.repeat");
			}
		}
	}

	/**
	 * 删除权限汇直投项目类型
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(FinserChargeDefine.DELETE_ACTION)
	@RequiresPermissions(FinserChargeDefine.PERMISSIONS_DELETE)
	public String deleteRecordAction(HttpServletRequest request, RedirectAttributes attr, FinserChargeBean form) {
		LogUtil.startLog(FinserChargeController.class.toString(), FinserChargeDefine.DELETE_ACTION);
		this.finserChargeService.deleteRecord(form.getChargeCd());
		attr.addFlashAttribute(FinserChargeDefine.FINSERCHARGE_FORM, form);
		LogUtil.endLog(FinserChargeController.class.toString(), FinserChargeDefine.DELETE_ACTION);
		return "redirect:" + FinserChargeDefine.REQUEST_MAPPING + "/" + FinserChargeDefine.INIT;
	}

	/**
	 * 检查月数唯一
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = FinserChargeDefine.CHECK_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(value = { FinserChargeDefine.PERMISSIONS_ADD, FinserChargeDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public String checkAction(HttpServletRequest request) {
		LogUtil.startLog(FinserChargeController.class.toString(), FinserChargeDefine.CHECK_ACTION);
		String chargeCd = request.getParameter("chargeCd");
		String borrowStyle = request.getParameter("borrowStyle");
		String name = request.getParameter("name");
		String param = request.getParameter("param");
		Integer projectType = 0;
		if(StringUtils.isNotBlank(request.getParameter("projectType"))){
			projectType = Integer.valueOf(request.getParameter("projectType"));
		}
		JSONObject ret = new JSONObject();
		// 检查天标是否是唯一
//		if ("chargeTimeType".equals(name) && "endday".equals(param)) {
//			boolean flag = finserChargeService.enddayIsExists(chargeCd,projectType);
//			if (flag) {
//				String message = ValidatorFieldCheckUtil.getErrorMessage("repeat", "");
//				message = message.replace("{label}", "类型：天标");
//				ret.put(FinserChargeDefine.JSON_VALID_INFO_KEY, message);
//			}
//		}
		// 检查月数是否是唯一
		if ("chargeTime".equals(name)) {
			boolean flag = false;
			if (StringUtils.isNotEmpty(borrowStyle) && "endday".equals(borrowStyle)) {
				flag = finserChargeService.onlyOneDay(chargeCd, param, projectType);
				if (flag) {
					String message = ValidatorFieldCheckUtil.getErrorMessage("repeat", "");
					message = message.replace("{label}", "天数");
					ret.put(FinserChargeDefine.JSON_VALID_INFO_KEY, message);
					return ret.toString();
				}
			} else if (StringUtils.isNotEmpty(borrowStyle) && "month".equals(borrowStyle)) {
				flag = finserChargeService.onlyOneMonth(chargeCd, param, projectType);
				if (flag) {
					String message = ValidatorFieldCheckUtil.getErrorMessage("repeat", "");
					message = message.replace("{label}", "月数");
					ret.put(FinserChargeDefine.JSON_VALID_INFO_KEY, message);
				}
			}
		}
		// 没有错误时,返回y
		if (!ret.containsKey(FinserChargeDefine.JSON_VALID_INFO_KEY)) {
			ret.put(FinserChargeDefine.JSON_VALID_STATUS_KEY, FinserChargeDefine.JSON_VALID_STATUS_OK);
		}
		LogUtil.endLog(FinserChargeController.class.toString(), FinserChargeDefine.CHECK_ACTION);
		return ret.toString();
	}

}
