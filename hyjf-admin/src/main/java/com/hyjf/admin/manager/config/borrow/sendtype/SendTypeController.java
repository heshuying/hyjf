package com.hyjf.admin.manager.config.borrow.sendtype;

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
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.BorrowSendType;

/**
 * 手续费配置
 * 
 * @author
 *
 */
@Controller
@RequestMapping(value = SendTypeDefine.REQUEST_MAPPING)
public class SendTypeController extends BaseController {

	@Autowired
	private SendTypeService sendTypeService;

	/**
	 * 列表维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(SendTypeDefine.INIT)
	@RequiresPermissions(SendTypeDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(SendTypeDefine.SENDTYPE_FORM) SendTypeBean form) {
		LogUtil.startLog(SendTypeController.class.toString(), SendTypeDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(SendTypeDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(SendTypeController.class.toString(), SendTypeDefine.INIT);
		return modelAndView;
	}

	/**
	 * 文章列表维护分页机能 页面初始化
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, SendTypeBean form) {
		List<BorrowSendType> recordList = this.sendTypeService.getRecordList(new SendTypeBean(), -1, -1);
		if (recordList != null) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			recordList = this.sendTypeService.getRecordList(new SendTypeBean(), paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(SendTypeDefine.SENDTYPE_FORM, form);
		}
	}

	/**
	 * 画面迁移(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(SendTypeDefine.INFO_ACTION)
	@RequiresPermissions(value = { SendTypeDefine.PERMISSIONS_ADD, SendTypeDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView info(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(SendTypeDefine.SENDTYPE_FORM) SendTypeBean form) {
		LogUtil.startLog(SendTypeController.class.toString(), SendTypeDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(SendTypeDefine.INFO_PATH);
		if (StringUtils.isNotEmpty(form.getSendCd())) {
			BorrowSendType record = sendTypeService.getRecord(form.getSendCd());
			modelAndView.addObject("modifyFlag", "M");
			modelAndView.addObject(SendTypeDefine.SENDTYPE_FORM, record);
			return modelAndView;
		}
		modelAndView.addObject("modifyFlag", "A");
		LogUtil.endLog(SendTypeController.class.toString(), SendTypeDefine.INIT);
		return modelAndView;
	}

	/**
	 * 添加信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = SendTypeDefine.INSERT_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(SendTypeDefine.PERMISSIONS_ADD)
	public ModelAndView insertAction(HttpServletRequest request, SendTypeBean form) {
		LogUtil.startLog(SendTypeController.class.toString(), SendTypeDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(SendTypeDefine.INFO_PATH);

		// 编号
		boolean sendCdFlag = ValidatorFieldCheckUtil.validateAlphaAndMaxLength(modelAndView, "sendCd", form.getSendCd(), 50, true);
		if (sendCdFlag) {
			BorrowSendType record = sendTypeService.getRecord(form.getSendCd());
			if (record != null && StringUtils.isNotEmpty(record.getSendCd())) {
				ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "sendCd", "repeat");
			}
		}

		// 画面验证
		this.validatorFieldCheck(modelAndView, form);

		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			modelAndView.addObject("enddayMonthList", this.sendTypeService.getParamNameList(CustomConstants.ENDDAY_MONTH));
			modelAndView.addObject(SendTypeDefine.SENDTYPE_FORM, form);
			return modelAndView;
		}

		// 数据插入
		this.sendTypeService.insertRecord(form);
		// 跳转页面用（info里面有）
		modelAndView.addObject(SendTypeDefine.SUCCESS, SendTypeDefine.SUCCESS);
		LogUtil.endLog(SendTypeController.class.toString(), SendTypeDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 修改维护信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = SendTypeDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(SendTypeDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateAction(HttpServletRequest request, SendTypeBean form) {
		LogUtil.startLog(SendTypeController.class.toString(), SendTypeDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(SendTypeDefine.INFO_PATH);

		ValidatorFieldCheckUtil.validateRequired(modelAndView, "sendCd", form.getSendCd());

		// 画面验证
		this.validatorFieldCheck(modelAndView, form);

		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			modelAndView.addObject("enddayMonthList", this.sendTypeService.getParamNameList(CustomConstants.ENDDAY_MONTH));
			modelAndView.addObject(SendTypeDefine.SENDTYPE_FORM, form);
			return modelAndView;
		}

		// 更新
		this.sendTypeService.updateRecord(form);
		// 跳转页面用（info里面有）
		modelAndView.addObject(SendTypeDefine.SUCCESS, SendTypeDefine.SUCCESS);
		LogUtil.endLog(SendTypeController.class.toString(), SendTypeDefine.UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 * 画面校验
	 * 
	 * @param modelAndView
	 * @param form
	 */
	private void validatorFieldCheck(ModelAndView modelAndView, SendTypeBean form) {
		// 名称
		ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "sendName", form.getSendName(), 50, true);
		// 发标时间
		ValidatorFieldCheckUtil.validateSignlessNum(modelAndView, "afterTime", form.getAfterTime(), 4, true);
		// 备注说明
		ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "remark", form.getRemark(), 50, true);

	}

	/**
	 * 删除汇直投项目类型
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(SendTypeDefine.DELETE_ACTION)
	@RequiresPermissions(SendTypeDefine.PERMISSIONS_DELETE)
	public String deleteRecordAction(HttpServletRequest request, RedirectAttributes attr, SendTypeBean form) {
		LogUtil.startLog(SendTypeController.class.toString(), SendTypeDefine.DELETE_ACTION);
		this.sendTypeService.deleteRecord(form.getSendCd());
		LogUtil.endLog(SendTypeController.class.toString(), SendTypeDefine.DELETE_ACTION);
		return "redirect:" + SendTypeDefine.REQUEST_MAPPING + "/" + SendTypeDefine.INIT;
	}

	/**
	 * 检查编号唯一性
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = SendTypeDefine.CHECK_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(value = { SendTypeDefine.PERMISSIONS_ADD, SendTypeDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public String checkAction(HttpServletRequest request) {
		LogUtil.startLog(SendTypeController.class.toString(), SendTypeDefine.CHECK_ACTION);

		String param = request.getParameter("param");

		JSONObject ret = new JSONObject();
		SendTypeBean form = new SendTypeBean();
		form.setSendCd(param);
		BorrowSendType record = sendTypeService.getRecord(form.getSendCd());
		if (record != null && StringUtils.isNotEmpty(record.getSendCd())) {
			String message = ValidatorFieldCheckUtil.getErrorMessage("repeat", "");
			message = message.replace("{label}", "编号");
			ret.put(SendTypeDefine.JSON_VALID_INFO_KEY, message);
		}
		// 没有错误时,返回y
		if (!ret.containsKey(SendTypeDefine.JSON_VALID_INFO_KEY)) {
			ret.put(SendTypeDefine.JSON_VALID_STATUS_KEY, SendTypeDefine.JSON_VALID_STATUS_OK);
		}
		LogUtil.endLog(SendTypeController.class.toString(), SendTypeDefine.CHECK_ACTION);
		return ret.toString();
	}

}
