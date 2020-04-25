package com.hyjf.admin.manager.content.environment;

import java.util.List;

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
import com.hyjf.admin.manager.content.links.ContentPartnerController;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.ContentEnvironment;

/**
 * 内容管理-办公环境
 * 
 * @author qingbing
 *
 */
@Controller
@RequestMapping(value = ContentEnvironmentDefine.REQUEST_MAPPING)
public class ContentEnvironmentController extends BaseController {

	@Autowired
	private ContentEnvironmentService contentEnvironmentService;
	@Autowired
	private BorrowCommonService borrowCommonService;

	/**
	 *画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ContentEnvironmentDefine.INIT)
	@RequiresPermissions(ContentEnvironmentDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(ContentEnvironmentDefine.ENVIRONMENT_FORM) ContentEnvironmentBean form) {
		LogUtil.startLog(ContentEnvironmentController.class.toString(), ContentEnvironmentDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(ContentEnvironmentDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(ContentEnvironmentController.class.toString(), ContentEnvironmentDefine.INIT);
		return modelAndView;
	}

	/**
	 * 条件查询
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ContentEnvironmentDefine.SEARCH_ACTION)
	@RequiresPermissions(ContentEnvironmentDefine.PERMISSIONS_SEARCH)
	public ModelAndView searchAction(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(ContentEnvironmentDefine.ENVIRONMENT_FORM) ContentEnvironmentBean form) {
		LogUtil.startLog(ContentPartnerController.class.toString(), ContentEnvironmentDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(ContentEnvironmentDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(ContentPartnerController.class.toString(), ContentEnvironmentDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 分页
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, ContentEnvironmentBean form) {
		List<ContentEnvironment> recordList = this.contentEnvironmentService.getRecordList(form, -1, -1);
		if (recordList != null) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			recordList = this.contentEnvironmentService.getRecordList(form, paginator.getOffset(),paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(ContentEnvironmentDefine.ENVIRONMENT_FORM, form);
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
	@RequestMapping(ContentEnvironmentDefine.INFO_ACTION)
	@RequiresPermissions(value = { ContentEnvironmentDefine.PERMISSIONS_INFO, ContentEnvironmentDefine.PERMISSIONS_ADD,ContentEnvironmentDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView info(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(ContentEnvironmentDefine.ENVIRONMENT_FORM) ContentEnvironmentBean form) {
		LogUtil.startLog(ContentPartnerController.class.toString(), ContentEnvironmentDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(ContentEnvironmentDefine.INFO_PATH);

		if (StringUtils.isNotEmpty(form.getIds())) {
			Integer id = Integer.valueOf(form.getIds());
			ContentEnvironment record = this.contentEnvironmentService.getRecord(id);
			modelAndView.addObject(ContentEnvironmentDefine.ENVIRONMENT_FORM, record);
			String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
			modelAndView.addObject("fileDomainUrl",fileDomainUrl);
		}
		LogUtil.endLog(ContentPartnerController.class.toString(), ContentEnvironmentDefine.INIT);
		return modelAndView;
	}

	/**
	 * 添加
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = ContentEnvironmentDefine.INSERT_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(ContentEnvironmentDefine.PERMISSIONS_ADD)
	public ModelAndView insertAction(HttpServletRequest request, ContentEnvironment form) {
		LogUtil.startLog(ContentEnvironmentController.class.toString(), ContentEnvironmentDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(ContentEnvironmentDefine.INFO_PATH);
		// 调用校验
		if (validatorFieldCheck(modelAndView, form) != null) {
			// 失败返回
			return validatorFieldCheck(modelAndView, form);
		}
		// 数据插入
		this.contentEnvironmentService.insertRecord(form);
		modelAndView.addObject(ContentEnvironmentDefine.SUCCESS, ContentEnvironmentDefine.SUCCESS);
		LogUtil.endLog(ContentEnvironmentController.class.toString(), ContentEnvironmentDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 修改活动维护信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = ContentEnvironmentDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(ContentEnvironmentDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateAction(HttpServletRequest request, ContentEnvironment form) {
		LogUtil.startLog(ContentEnvironmentController.class.toString(), ContentEnvironmentDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(ContentEnvironmentDefine.INFO_PATH);
		// 调用校验
		if (validatorFieldCheck(modelAndView, form) != null) {
			// 失败返回
			return validatorFieldCheck(modelAndView, form);
		}
		// 根据id更新
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "id", form.getId().toString())) {
			return modelAndView;
		}
		// 跟新
		this.contentEnvironmentService.updateRecord(form);
		modelAndView.addObject(ContentEnvironmentDefine.SUCCESS, ContentEnvironmentDefine.SUCCESS);
		LogUtil.endLog(ContentEnvironmentController.class.toString(), ContentEnvironmentDefine.UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 * 删除环境信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ConfigDefine.DELETE_ACTION)
	@RequiresPermissions(ContentEnvironmentDefine.PERMISSIONS_DELETE)
	public ModelAndView deleteRecordAction(HttpServletRequest request, String ids) {
		LogUtil.startLog(ConfigController.class.toString(), ContentEnvironmentDefine.DELETE_ACTION);

		ModelAndView modelAndView = new ModelAndView(ContentEnvironmentDefine.RE_LIST_PATH);
		// 解析json字符串
		List<Integer> recordList = JSONArray.parseArray(ids, Integer.class);
		this.contentEnvironmentService.deleteRecord(recordList);
		LogUtil.endLog(ConfigController.class.toString(), ContentEnvironmentDefine.DELETE_ACTION);
		return modelAndView;
	}

	/**
	 * 修改状态
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ContentEnvironmentDefine.STATUS_ACTION)
	@RequiresPermissions(ContentEnvironmentDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateStatus(HttpServletRequest request, RedirectAttributes attr,ContentEnvironmentBean form) {
		LogUtil.startLog(ConfigController.class.toString(), ContentEnvironmentDefine.STATUS_ACTION);

		ModelAndView modelAndView = new ModelAndView(ContentEnvironmentDefine.RE_LIST_PATH);
		// 修改状态
		if (StringUtils.isNotEmpty(form.getIds())) {
			Integer id = Integer.valueOf(form.getIds());
			ContentEnvironment record = this.contentEnvironmentService.getRecord(id);
			if (record.getStatus() == 1) {
				record.setStatus(0);
			} else if (record.getStatus() == 0) {
				record.setStatus(1);
			}
			this.contentEnvironmentService.updateRecord(record);
		}
		attr.addFlashAttribute(ContentEnvironmentDefine.ENVIRONMENT_FORM, form);
		LogUtil.endLog(ConfigController.class.toString(), ContentEnvironmentDefine.STATUS_ACTION);
		return modelAndView;
	}

	/**
	 * 调用校验表单方法
	 * 
	 * @param modelAndView
	 * @param form
	 * @return
	 */
	private ModelAndView validatorFieldCheck(ModelAndView modelAndView, ContentEnvironment form) {
		// 字段校验(非空判断和长度判断)
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "name", form.getName())) {
			return modelAndView;
		}
		;
		if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "name", form.getName(), 50, true)) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "imgurl", form.getImgurl())) {
			form.setImgurl("");
		}
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "imgtype", form.getImgtype().toString())) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "status", form.getStatus().toString())) {
			return modelAndView;
		}
		return null;
	}

	/**
	 * 资料上传
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = ContentEnvironmentDefine.UPLOAD_FILE, method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = { ContentEnvironmentDefine.PERMISSIONS_ADD,
			ContentEnvironmentDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogUtil.startLog(ContentEnvironmentController.class.toString(), ContentEnvironmentDefine.UPLOAD_FILE);
		String files = borrowCommonService.uploadFile(request, response);
		LogUtil.endLog(ContentEnvironmentController.class.toString(), ContentEnvironmentDefine.UPLOAD_FILE);
		return files;
	}
}
