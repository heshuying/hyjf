package com.hyjf.admin.promotion.utm;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hyjf.common.util.GetCilentIP;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.UtmPlat;

/**
 * 手续费配置
 * 
 * @author
 *
 */
@Controller
@RequestMapping(value = UtmDefine.REQUEST_MAPPING)
public class UtmController extends BaseController {
	private Logger logger = LoggerFactory.getLogger(UtmController.class);
	@Autowired
	private UtmService utmService;

	/**
	 * 列表维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(UtmDefine.INIT)
	@RequiresPermissions(UtmDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(UtmDefine.FORM) UtmBean form) {
		LogUtil.startLog(UtmController.class.toString(), UtmDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(UtmDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(UtmController.class.toString(), UtmDefine.INIT);
		return modelAndView;
	}

	/**
	 * 搜索按钮
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(UtmDefine.SEARCH_ACTION)
	@RequiresPermissions(UtmDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(UtmDefine.FORM) UtmBean form) {
		LogUtil.startLog(UtmController.class.toString(), UtmDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(UtmDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(UtmController.class.toString(), UtmDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 文章列表维护分页机能 页面初始化
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, UtmBean form) {
		boolean timeStartFlag = ValidatorFieldCheckUtil.validateDate(modelAndView, "", form.getTimeStartSrch(), false);
		boolean timeEndFlag = ValidatorFieldCheckUtil.validateDate(modelAndView, "", form.getTimeEndSrch(), false);
		if (timeStartFlag && timeEndFlag) {
			List<UtmPlat> recordList = this.utmService.getRecordList(form, -1, -1);
			if (recordList != null) {
				Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
				recordList = this.utmService.getRecordList(form, paginator.getOffset(), paginator.getLimit());
				form.setPaginator(paginator);
				modelAndView.addObject("recordList", recordList);
			}
		}
		modelAndView.addObject(UtmDefine.FORM, form);

		modelAndView.addObject("utmPlatList", utmService.getUtm());
	}

	/**
	 * 画面迁移(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(UtmDefine.INFO_ACTION)
	@RequiresPermissions(value = { UtmDefine.PERMISSIONS_ADD, UtmDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView info(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(UtmDefine.FORM) UtmBean form) {
		LogUtil.startLog(UtmController.class.toString(), UtmDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(UtmDefine.INFO_PATH);
		if (StringUtils.isNotEmpty(form.getId())) {
			UtmPlat record = utmService.getRecord(form.getId());
			modelAndView.addObject(UtmDefine.FORM, record);
			return modelAndView;
		}
		LogUtil.endLog(UtmController.class.toString(), UtmDefine.INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 添加信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = UtmDefine.INSERT_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(UtmDefine.PERMISSIONS_ADD)
	public ModelAndView insertAction(HttpServletRequest request, UtmBean form) {
		LogUtil.startLog(UtmController.class.toString(), UtmDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(UtmDefine.INFO_PATH);

		if (StringUtils.isNotEmpty(form.getId())) {
			UtmPlat record = utmService.getRecord(form.getId());
			if (record != null) {
				ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "sourceId", "repeat");
			}
		}

		// 画面验证
		this.validatorFieldCheck(modelAndView, form);

		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			modelAndView.addObject(UtmDefine.FORM, form);
			return modelAndView;
		}

		// 数据插入
		this.utmService.insertRecord(form);
		// 跳转页面用（info里面有）
		modelAndView.addObject(UtmDefine.SUCCESS, UtmDefine.SUCCESS);
		LogUtil.endLog(UtmController.class.toString(), UtmDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 修改维护信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = UtmDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(UtmDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateAction(HttpServletRequest request, UtmBean form) {
		LogUtil.startLog(UtmController.class.toString(), UtmDefine.UPDATE_ACTION);
		logger.info("修改渠道配置信息...form is :{}, ip is :{}", JSONObject.toJSONString(form), GetCilentIP.getIpAddr(request));
		ModelAndView modelAndView = new ModelAndView(UtmDefine.INFO_PATH);

		// 画面验证
		this.validatorFieldCheck(modelAndView, form);

		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			modelAndView.addObject(UtmDefine.FORM, form);
			return modelAndView;
		}

		// 更新
		this.utmService.updateRecord(form);
		// 跳转页面用（info里面有）
		modelAndView.addObject(UtmDefine.SUCCESS, UtmDefine.SUCCESS);
		LogUtil.endLog(UtmController.class.toString(), UtmDefine.UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 * 画面校验
	 * 
	 * @param modelAndView
	 * @param form
	 */
	private void validatorFieldCheck(ModelAndView modelAndView, UtmBean form) {
		// 渠道编号
		boolean flag1 = ValidatorFieldCheckUtil.validateSignlessNum(modelAndView, "sourceId", form.getSourceId(), 10, true);
		// 名称
		boolean flag2 = ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "sourceName", form.getSourceName(), 50, true);
		// 发标时间
		ValidatorFieldCheckUtil.validateRequired(modelAndView, "delFlag", form.getDelFlag());
		// 备注说明
		ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "remark", form.getRemark(), 225, false);

		if (flag1 && flag2) {
			int record = utmService.sourceNameIsExists(form.getSourceName(), form.getSourceId());
			if (record == 1) {
				ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "sourceName", "repeat");
			}
		}

	}

	/**
	 * 删除汇直投项目类型
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(UtmDefine.DELETE_ACTION)
	@RequiresPermissions(UtmDefine.PERMISSIONS_DELETE)
	public String deleteRecordAction(HttpServletRequest request, RedirectAttributes attr, UtmBean form) {
		LogUtil.startLog(UtmController.class.toString(), UtmDefine.DELETE_ACTION);
		this.utmService.deleteRecord(form.getId());
		attr.addFlashAttribute(UtmDefine.FORM, form);
		LogUtil.endLog(UtmController.class.toString(), UtmDefine.DELETE_ACTION);
		return "redirect:" + UtmDefine.REQUEST_MAPPING + "/" + UtmDefine.INIT;
	}

	/**
	 * 检查编号唯一性
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = UtmDefine.CHECK_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(value = { UtmDefine.PERMISSIONS_ADD, UtmDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public String checkAction(HttpServletRequest request) {
		LogUtil.startLog(UtmController.class.toString(), UtmDefine.CHECK_ACTION);

		String param = request.getParameter("param");

		JSONObject ret = new JSONObject();
		UtmBean form = new UtmBean();
		form.setSourceId(param);
		int record = utmService.sourceIdIsExists(form.getSourceId());
		if (record == 1) {
			String message = ValidatorFieldCheckUtil.getErrorMessage("repeat", "");
			message = message.replace("{label}", "渠道编号");
			ret.put(UtmDefine.JSON_VALID_INFO_KEY, message);
		}
		// 没有错误时,返回y
		if (!ret.containsKey(UtmDefine.JSON_VALID_INFO_KEY)) {
			ret.put(UtmDefine.JSON_VALID_STATUS_KEY, UtmDefine.JSON_VALID_STATUS_OK);
		}
		LogUtil.endLog(UtmController.class.toString(), UtmDefine.CHECK_ACTION);
		return ret.toString();
	}

}
