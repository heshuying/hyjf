package com.hyjf.admin.manager.config.bankconfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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

import com.alibaba.fastjson.JSONArray;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.maintenance.config.ConfigController;
import com.hyjf.admin.maintenance.config.ConfigDefine;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonService;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.BankConfig;

/**
 * 手续费配置
 * 
 * @author
 *
 */
@Controller
@RequestMapping(value = BankConfigDefine.REQUEST_MAPPING)
public class BankConfigController extends BaseController {

	@Autowired
	private BankConfigService bankConfigService;
	@Autowired
	private BorrowCommonService borrowCommonService;

	/**
	 * 列表维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BankConfigDefine.INIT)
	@RequiresPermissions(BankConfigDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(BankConfigDefine.CONFIGBANK_FORM) BankConfigBean form) {
		LogUtil.startLog(BankConfigController.class.toString(), BankConfigDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(BankConfigDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BankConfigController.class.toString(), BankConfigDefine.INIT);
		return modelAndView;
	}

	/**
	 * 文章列表维护分页机能 页面初始化
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, BankConfigBean form) {
		List<BankConfig> recordList = this.bankConfigService.getRecordList(new BankConfig(), -1, -1);
		if (recordList != null) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			recordList = this.bankConfigService.getRecordList(new BankConfig(), paginator.getOffset(),
					paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(BankConfigDefine.CONFIGBANK_FORM, form);
			String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
			modelAndView.addObject("fileDomainUrl",fileDomainUrl);
		}
	}

	/**
	 * 画面迁移(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BankConfigDefine.INFO_ACTION)
	@RequiresPermissions(value = { BankConfigDefine.PERMISSIONS_INFO, BankConfigDefine.PERMISSIONS_ADD,
			BankConfigDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView info(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(BankConfigDefine.CONFIGBANK_FORM) BankConfigBean form) {
		LogUtil.startLog(BankConfigController.class.toString(), BankConfigDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(BankConfigDefine.INFO_PATH);

		if (StringUtils.isNotEmpty(form.getIds())) {
			Integer id = Integer.valueOf(form.getIds());
			BankConfig record = this.bankConfigService.getRecord(id);
			modelAndView.addObject(BankConfigDefine.CONFIGBANK_FORM, record);
			String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
			modelAndView.addObject("fileDomainUrl",fileDomainUrl);
		}
		LogUtil.endLog(BankConfigController.class.toString(), BankConfigDefine.INIT);
		return modelAndView;
	}

	/**
	 * 添加活动信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = BankConfigDefine.INSERT_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(BankConfigDefine.PERMISSIONS_ADD)
	public ModelAndView insertAction(HttpServletRequest request, BankConfig form) {
		LogUtil.startLog(BankConfigController.class.toString(), BankConfigDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(BankConfigDefine.INFO_PATH);

		// 调用校验
		if (validatorFieldCheck(modelAndView, form) != null) {
			// 失败返回
			return validatorFieldCheck(modelAndView, form);
		}
		// form.setLogo("1");
		BankConfig bank = new BankConfig();
		bank.setName(form.getName());
		List<BankConfig> banks = bankConfigService.getRecordList(bank, -1, -1);
		if (banks.size() == 0) {
			// 数据插入
			this.bankConfigService.insertRecord(form);
		}
		// 跳转页面用（info里面有）
		modelAndView.addObject(BankConfigDefine.SUCCESS, BankConfigDefine.SUCCESS);
		LogUtil.endLog(BankConfigController.class.toString(), BankConfigDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 修改活动维护信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = BankConfigDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(BankConfigDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateAction(HttpServletRequest request, BankConfig form) {
		LogUtil.startLog(BankConfigController.class.toString(), BankConfigDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(BankConfigDefine.INFO_PATH);

		// 调用校验
		if (validatorFieldCheck(modelAndView, form) != null) {
			// 失败返回
			return validatorFieldCheck(modelAndView, form);
		}
		// // 根据id更新
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "id", form.getId().toString())) {
			return modelAndView;
		}
		// form.setLogo("1");
		// 更新
		this.bankConfigService.updateRecord(form);
		// 跳转页面用（info里面有）
		modelAndView.addObject(BankConfigDefine.SUCCESS, BankConfigDefine.SUCCESS);
		LogUtil.endLog(BankConfigController.class.toString(), BankConfigDefine.UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 * 删除配置信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ConfigDefine.DELETE_ACTION)
	@RequiresPermissions(BankConfigDefine.PERMISSIONS_DELETE)
	public ModelAndView deleteRecordAction(HttpServletRequest request, String ids) {
		LogUtil.startLog(ConfigController.class.toString(), ConfigDefine.DELETE_ACTION);

		ModelAndView modelAndView = new ModelAndView(BankConfigDefine.RE_LIST_PATH);
		// 解析json字符串
		List<Integer> recordList = JSONArray.parseArray(ids, Integer.class);
		this.bankConfigService.deleteRecord(recordList);
		LogUtil.endLog(ConfigController.class.toString(), ConfigDefine.DELETE_ACTION);
		return modelAndView;
	}

	/**
	 * 调用校验表单方法
	 * 
	 * @param modelAndView
	 * @param form
	 * @return
	 */
	private ModelAndView validatorFieldCheck(ModelAndView modelAndView, BankConfig form) {
		// 字段校验(非空判断和长度判断)
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "name", form.getName())) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "name", form.getName(), 50, true)) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "code", form.getCode())) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "code", form.getCode(), 10, true)) {
			return modelAndView;
		}
		return null;
	}

	@ResponseBody
	@RequestMapping(value = BankConfigDefine.VALIDATEBEFORE)
	@RequiresPermissions(BankConfigDefine.PERMISSIONS_VIEW)
	public Map<String, Object> validateBeforeAction(HttpServletRequest request, BankConfig form) {
		LogUtil.startLog(BankConfigController.class.toString(), BankConfigDefine.VALIDATEBEFORE);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<BankConfig> list = bankConfigService.getRecordList(form, -1, -1);
		if (list != null && list.size() != 0) {
			if (form.getId() != null) {
				Boolean hasnot = true;
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getId() == form.getId()) {
						hasnot = false;
						break;
					}
				}
				if (hasnot) {
					resultMap.put("success", false);
					resultMap.put("msg", "银行名称或银行代码不可重复添加");
				} else {
					resultMap.put("success", true);
				}
			} else {
				resultMap.put("success", false);
				resultMap.put("msg", "银行名称或银行代码不可重复添加");
			}
		} else {
			resultMap.put("success", true);
		}
		LogUtil.endLog(BankConfigController.class.toString(), BankConfigDefine.VALIDATEBEFORE);
		return resultMap;
	}

	/**
	 * 资料上传
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = BankConfigDefine.UPLOAD_FILE, method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = { BankConfigDefine.PERMISSIONS_ADD,
			BankConfigDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogUtil.startLog(BankConfigController.class.toString(), BankConfigDefine.UPLOAD_FILE);
		String files = borrowCommonService.uploadFile(request, response);
		LogUtil.endLog(BankConfigController.class.toString(), BankConfigDefine.UPLOAD_FILE);
		return files;
	}
}
