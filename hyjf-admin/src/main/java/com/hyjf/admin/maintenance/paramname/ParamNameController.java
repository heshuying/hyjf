package com.hyjf.admin.maintenance.paramname;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.ParamName;

/**
 * 手续费配置
 * 
 * @author
 *
 */
@Controller
@RequestMapping(value = ParamNameDefine.REQUEST_MAPPING)
public class ParamNameController extends BaseController {

	@Autowired
	private ParamNameService paramNameService;

	/**
	 * 列表维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ParamNameDefine.INIT)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(ParamNameDefine.FORM) ParamNameBean form) {
		LogUtil.startLog(ParamNameController.class.toString(), ParamNameDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(ParamNameDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(ParamNameController.class.toString(), ParamNameDefine.INIT);
		return modelAndView;
	}

	/**
	 * 维护分页机能 页面初始化
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, ParamNameBean form) {
		
		Integer count= this.paramNameService.getRecordListSize(form);
		if(count>0){
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			List<ParamName> recordList = this.paramNameService.getRecordList(form, paginator.getOffset(), paginator.getLimit());
			modelAndView.addObject("paginator", paginator);
			modelAndView.addObject("recordList", recordList);
		}
		modelAndView.addObject(ParamNameDefine.FORM, form);
		
//		
//		List<ParamName> recordList = this.paramNameService.getRecordList(new ParamNameBean(), -1, -1);
//		if (recordList != null) {
//			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
//			recordList = this.paramNameService.getRecordList(new ParamNameBean(), paginator.getOffset(), paginator.getLimit());
//			modelAndView.addObject("paginator", paginator);
//			modelAndView.addObject("recordList", recordList);
//			modelAndView.addObject(ParamNameDefine.FORM, form);
//		}
	}

	/**
	 * 画面迁移(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ParamNameDefine.INFO_ACTION)
	public ModelAndView info(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(ParamNameDefine.FORM) ParamNameBean form) {
		LogUtil.startLog(ParamNameController.class.toString(), ParamNameDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(ParamNameDefine.INFO_PATH);

		ParamName record = new ParamName();
		record.setNameClass(form.getNameClass());
		record.setNameCd(form.getNameCd());
		record.setName(form.getName());
		record = paramNameService.getRecord(record);
		if (record != null) {
			modelAndView.addObject("modifyFlag", "E");
		} else {
			modelAndView.addObject("modifyFlag", "N");
		}
		modelAndView.addObject(ParamNameDefine.FORM, record);

		LogUtil.endLog(ParamNameController.class.toString(), ParamNameDefine.INIT);
		return modelAndView;
	}

	/**
	 * 添加数据字典信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = ParamNameDefine.INSERT_ACTION, method = RequestMethod.POST)
	public ModelAndView insertAction(HttpServletRequest request, ParamNameBean form) {
		LogUtil.startLog(ParamNameController.class.toString(), ParamNameDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(ParamNameDefine.INFO_PATH);
		// 画面验证
		this.validatorFieldCheck(modelAndView, form);

		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			modelAndView.addObject("modifyFlag", form.getModifyFlag());
			modelAndView.addObject(ParamNameDefine.FORM, form);
			return modelAndView;
		}

		// 数据插入
		this.paramNameService.insertRecord(form);

		// 跳转页面用（info里面有）
		modelAndView.addObject(ParamNameDefine.SUCCESS, ParamNameDefine.SUCCESS);
		LogUtil.endLog(ParamNameController.class.toString(), ParamNameDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 修改活动维护信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = ParamNameDefine.UPDATE_ACTION, method = RequestMethod.POST)
	public ModelAndView updateAction(HttpServletRequest request, ParamNameBean form) {
		LogUtil.startLog(ParamNameController.class.toString(), ParamNameDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(ParamNameDefine.INFO_PATH);
		// 画面验证
		this.validatorFieldCheck(modelAndView, form);

		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			modelAndView.addObject("modifyFlag", form.getModifyFlag());
			modelAndView.addObject(ParamNameDefine.FORM, form);
			return modelAndView;
		}

		// 更新
		this.paramNameService.updateRecord(form);

		modelAndView.addObject(ParamNameDefine.SUCCESS, ParamNameDefine.SUCCESS);
		LogUtil.endLog(ParamNameController.class.toString(), ParamNameDefine.UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 * 画面校验
	 * 
	 * @param modelAndView
	 * @param form
	 */
	private void validatorFieldCheck(ModelAndView modelAndView, ParamNameBean form) {
		// 名称分类
		boolean nameClassFlag = ValidatorFieldCheckUtil.validateAlphaNumericAndMaxLength(modelAndView, "nameClass", form.getNameClass(), 20, true);
		// 名称编号
		boolean nameCdFlag = ValidatorFieldCheckUtil.validateAlphaNumericAndMaxLength(modelAndView, "nameCd", form.getNameCd(), 6, true);
		// 名称
		ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "name", form.getName(), 100, true);
		// 说明
		ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "other1", form.getOther1(), 255, false);
		// 说明
		ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "other2", form.getOther2(), 255, false);
		// 说明
		ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "other3", form.getOther3(), 255, false);
		// 排序
		ValidatorFieldCheckUtil.validateSignlessNum(modelAndView, "sort", form.getSort(), 2, false);

		if (nameClassFlag && nameCdFlag && "N".equals(form.getModifyFlag())) {
			boolean flag = this.paramNameService.isExists(form);
			if (flag) {
				ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "nameClass", "repay.namecd.nameclass");
			}
		}
	}

	/**
	 * 删除项目类型
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ParamNameDefine.DELETE_ACTION)
	@RequiresPermissions(ParamNameDefine.PERMISSIONS_DELETE)
	public String deleteRecordAction(HttpServletRequest request, RedirectAttributes attr, ParamNameBean form) {
		LogUtil.startLog(ParamNameController.class.toString(), ParamNameDefine.DELETE_ACTION);

		ParamName record = new ParamName();
		record.setNameClass(form.getNameClass());
		record.setNameCd(form.getNameCd());
		this.paramNameService.deleteRecord(record);

		attr.addFlashAttribute(ParamNameDefine.FORM, form);
		LogUtil.endLog(ParamNameController.class.toString(), ParamNameDefine.DELETE_ACTION);
		return "redirect:" + ParamNameDefine.REQUEST_MAPPING + "/" + ParamNameDefine.INIT;
	}
}
