package com.hyjf.admin.htl.product;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.annotate.Token;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.Product;

/**
 * @package com.hyjf.admin.maintenance.Product
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = HtlProductDefine.REQUEST_MAPPING)
public class HtlProductControllor extends BaseController {

	@Autowired
	private HtlProductService HtlProductService;

	/**
	 * 权限维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(HtlProductDefine.INIT)
	// @RequiresPermissions(ProductDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(HtlProductDefine.PRODUCT_FORM) HtlProductBean form) {
		LogUtil.startLog(HtlProductControllor.class.toString(), HtlProductDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(HtlProductDefine.LIST_PATH);

		System.out.println(form.getId());

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(HtlProductControllor.class.toString(), HtlProductDefine.INIT);
		return modelAndView;
	}

	/**
	 * 创建权限维护分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, HtlProductBean form) {
		List<Product> recordList = this.HtlProductService.getRecordList(new Product(), -1, -1);
		if (recordList != null) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			recordList = this.HtlProductService.getRecordList(new Product(), paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(HtlProductDefine.PRODUCT_FORM, form);
		}
	}

	/**
	 * 迁移到权限维护详细画面
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = HtlProductDefine.INFO_ACTION)
	@Token(save = true)
	@RequiresPermissions(value = { HtlProductDefine.PRODUCT_ADD, HtlProductDefine.PRODUCT_MODIFY }, logical = Logical.OR)
	public ModelAndView moveToInfoAction(HttpServletRequest request, HtlProductBean form) {
		LogUtil.startLog(HtlProductControllor.class.toString(), HtlProductDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(HtlProductDefine.INFO_PATH);
		Product record = new Product();
		record.setId(record.getId());
		if (record.getId() != null) {
			// 根据主键判断该条数据在数据库中是否存在
			boolean isExists = this.HtlProductService.isExistsRecord(record);

			// 没有添加权限 同时 也没能检索出数据的时候异常
			if (!isExists) {
				Subject currentUser = SecurityUtils.getSubject();
				currentUser.checkPermission(HtlProductDefine.PRODUCT_ADD);
			}

			// 根据主键检索数据
			record = this.HtlProductService.getRecord(record);
		}

		modelAndView.addObject(HtlProductDefine.PRODUCT_FORM, record);
		LogUtil.endLog(HtlProductControllor.class.toString(), HtlProductDefine.INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 添加权限维护信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = HtlProductDefine.INSERT_ACTION, method = RequestMethod.POST)
	@Token(check = true, forward = HtlProductDefine.TOKEN_INIT_PATH)
	@RequiresPermissions(value = { HtlProductDefine.PRODUCT_ADD })
	public ModelAndView insertAction(HttpServletRequest request, HtlProductBean form) {
		LogUtil.startLog(HtlProductControllor.class.toString(), HtlProductDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(HtlProductDefine.LIST_PATH);
		// 权限检查用字段的校验
		//ValidatorFieldCheckUtil.validateAlphaAndMaxLength(modelAndView, "permission", form.getPermission(), 20, true);
		// 权限名字s
		//ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "permissionName", form.getPermissionName(), 20, true);

		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			modelAndView.setViewName(HtlProductDefine.INFO_PATH);
			modelAndView.addObject(HtlProductDefine.PRODUCT_FORM, form);
			return modelAndView;
		}

		Product Product = new Product();
	//	Product.setPermission(form.getPermission());
	//	Product.setPermissionName(form.getPermissionName());
	//	Product.setDescription(form.getDescription());

		if (form.getId() == null) {
			this.HtlProductService.insertRecord(Product);
		}

		LogUtil.endLog(HtlProductControllor.class.toString(), HtlProductDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 修改权限维护信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = HtlProductDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@Token(check = true, forward = HtlProductDefine.TOKEN_INIT_PATH)
	@RequiresPermissions(value = { HtlProductDefine.PRODUCT_MODIFY })
	public ModelAndView updateAction(HttpServletRequest request, HtlProductBean form) {
		LogUtil.startLog(HtlProductControllor.class.toString(), HtlProductDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(HtlProductDefine.INFO_PATH);

		Subject currentUser = SecurityUtils.getSubject();
		currentUser.checkPermission(HtlProductDefine.PRODUCT_MODIFY);

		if (form.getId() != null) {
			Product product = new Product();

			this.HtlProductService.updateRecord(product);
		}

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(HtlProductControllor.class.toString(), HtlProductDefine.UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 * 删除权限维护
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(HtlProductDefine.DELETE_ACTION)
	@RequiresPermissions(HtlProductDefine.PRODUCT_DELETE)
	public String deleteRecordAction(HttpServletRequest request, RedirectAttributes attr, HtlProductBean form) {
		LogUtil.startLog(HtlProductControllor.class.toString(), HtlProductDefine.DELETE_ACTION);
		List<String> recordList = JSONArray.parseArray(form.getId().toString(), String.class);
		this.HtlProductService.deleteRecord(recordList);
		attr.addFlashAttribute(HtlProductDefine.PRODUCT_FORM, form);
		LogUtil.endLog(HtlProductControllor.class.toString(), HtlProductDefine.DELETE_ACTION);
		return "redirect:" + HtlProductDefine.REQUEST_MAPPING + "/" + HtlProductDefine.INIT;
	}
}
