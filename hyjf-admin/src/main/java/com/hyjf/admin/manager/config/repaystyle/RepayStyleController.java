package com.hyjf.admin.manager.config.repaystyle;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.BorrowStyleWithBLOBs;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = RepayStyleDefine.REQUEST_MAPPING)
public class RepayStyleController extends BaseController {

	@Autowired
	private RepayStyleService borrowStyleService;

	/**
	 * 权限维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(RepayStyleDefine.INIT)
	@RequiresPermissions(RepayStyleDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(RepayStyleDefine.BORROWSTYLE_FORM) RepayStyleBean form) {
		LogUtil.startLog(RepayStyleController.class.toString(), RepayStyleDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(RepayStyleDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(RepayStyleController.class.toString(), RepayStyleDefine.INIT);
		return modelAndView;
	}

	/**
	 * 创建权限维护分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, RepayStyleBean form) {

		int total = this.borrowStyleService.countRecordTotal(form);
		if (total > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), total);
			List<BorrowStyleWithBLOBs> recordList = this.borrowStyleService.getRecordList(form, paginator.getOffset(),
					paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(RepayStyleDefine.BORROWSTYLE_FORM, form);
		}
	}

	/**
	 * 迁移到权限维护详细画面
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = RepayStyleDefine.INFO_ACTION)
	@RequiresPermissions(value = { RepayStyleDefine.PERMISSIONS_ADD,
			RepayStyleDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView info(HttpServletRequest request, RepayStyleBean form) {
		LogUtil.startLog(RepayStyleController.class.toString(), RepayStyleDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(RepayStyleDefine.INFO_PATH);
		if (form.getId() != null) {
			Integer id = Integer.valueOf(form.getId());
			BorrowStyleWithBLOBs record = borrowStyleService.getRecordById(id);
			modelAndView.addObject(RepayStyleDefine.BORROWSTYLE_FORM, record);
		}
		LogUtil.endLog(RepayStyleController.class.toString(), RepayStyleDefine.INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 添加权限维护信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = RepayStyleDefine.INSERT_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(RepayStyleDefine.PERMISSIONS_ADD)
	public ModelAndView insertAction(HttpServletRequest request, RepayStyleBean form) {
		LogUtil.startLog(RepayStyleController.class.toString(), RepayStyleDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(RepayStyleDefine.INFO_PATH);
		// 画面验证
		this.validatorFieldCheck(modelAndView, form);

		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			modelAndView.setViewName(RepayStyleDefine.INFO_PATH);
			modelAndView.addObject(RepayStyleDefine.BORROWSTYLE_FORM, form);
			return modelAndView;
		}

		// borrowStyle.setChargeType(form.getChargeType());
		// borrowStyle.setChargeTime(form.getChargeTime());
		// borrowStyle.setChargeTimeType(form.getChargeTimeType());

		// if (StringUtils.isEmpty(form.getChargeCd())) {
		this.borrowStyleService.insertRecord(form);
		// }

		modelAndView.addObject(RepayStyleDefine.SUCCESS,RepayStyleDefine.SUCCESS);
		createPage(request, modelAndView, form);

		LogUtil.endLog(RepayStyleController.class.toString(), RepayStyleDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 修改权限维护信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = RepayStyleDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(RepayStyleDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateAction(HttpServletRequest request, RepayStyleBean form) {
		LogUtil.startLog(RepayStyleController.class.toString(), RepayStyleDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(RepayStyleDefine.INFO_PATH);

		// 画面验证
		this.validatorFieldCheck(modelAndView, form);

		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			modelAndView.setViewName(RepayStyleDefine.INFO_PATH);
			modelAndView.addObject(RepayStyleDefine.BORROWSTYLE_FORM, form);
			return modelAndView;
		}

		this.borrowStyleService.updateRecord(form);
		// }

		modelAndView.addObject(RepayStyleDefine.SUCCESS,RepayStyleDefine.SUCCESS);
		// 创建分页
		createPage(request, modelAndView, form);

		LogUtil.endLog(RepayStyleController.class.toString(), RepayStyleDefine.UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 * 画面校验
	 * 
	 * @param modelAndView
	 * @param form
	 */
	private void validatorFieldCheck(ModelAndView modelAndView, RepayStyleBean form) {
		// 权限检查用字段的校验
		// ValidatorFieldCheckUtil.validateAlphaAndMaxLength(modelAndView,
		// "permission", form.getChargeType(), 20, true);
		// 权限名字
//		ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "permissionName", form.getNid(), 20, true);
		// 权限功能按钮是否重复
		if (this.borrowStyleService.isExistsPermission(form)) {
			// String message = ValidatorFieldCheckUtil.getErrorMessage("",
			// "权限");
			// ValidatorFieldCheckUtil.validateSpecialError(modelAndView,
			// "permission-repeat", form.getChargeType(), message);
		}
	}

	/**
	 * 删除权限维护
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(RepayStyleDefine.DELETE_ACTION)
	@RequiresPermissions(RepayStyleDefine.PERMISSIONS_DELETE)
	public String deleteRecordAction(HttpServletRequest request, RedirectAttributes attr, RepayStyleBean form) {
		LogUtil.startLog(RepayStyleController.class.toString(), RepayStyleDefine.DELETE_ACTION);
		List<String> recordList = JSONArray.parseArray(form.getNid(), String.class);
		this.borrowStyleService.deleteRecord(recordList);
		attr.addFlashAttribute(RepayStyleDefine.BORROWSTYLE_FORM, form);
		LogUtil.endLog(RepayStyleController.class.toString(), RepayStyleDefine.DELETE_ACTION);
		return "redirect:" + RepayStyleDefine.REQUEST_MAPPING + "/" + RepayStyleDefine.INIT;
	}

	/**
	 * 修改状态
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(RepayStyleDefine.STATUS_ACTION)
	@RequiresPermissions(RepayStyleDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateStatus(HttpServletRequest request, String id) {
		LogUtil.startLog(RepayStyleController.class.toString(), RepayStyleDefine.STATUS_ACTION);

		ModelAndView modelAndView = new ModelAndView(RepayStyleDefine.RE_LIST_PATH);
		if (id != null) {
			// 修改状态
			BorrowStyleWithBLOBs record = this.borrowStyleService.getRecordById(Integer.parseInt(id));
			if (record.getStatus() == 1) {
				record.setStatus(0);
			} else {
				record.setStatus(1);
			}
			this.borrowStyleService.updateRecord(record);
		}
		LogUtil.endLog(RepayStyleController.class.toString(), RepayStyleDefine.STATUS_ACTION);
		return modelAndView;
	}
}
