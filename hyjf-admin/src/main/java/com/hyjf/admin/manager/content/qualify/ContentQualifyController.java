package com.hyjf.admin.manager.content.qualify;

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
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonService;
import com.hyjf.admin.manager.content.links.ContentPartnerController;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.ContentQualify;

/**
 * 内容管理 -资质荣誉
 * 
 * @author yangx
 *
 */
@Controller
@RequestMapping(value = ContentQualifyDefine.REQUEST_MAPPING)
public class ContentQualifyController extends BaseController {

	@Autowired
	private ContentQualifyService contentQualifyService;
	@Autowired
	private BorrowCommonService borrowCommonService;

	/**
	 * 面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ContentQualifyDefine.INIT)
	@RequiresPermissions(ContentQualifyDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(ContentQualifyDefine.CONTENTQUALIFY_FORM) ContentQualifyBean form) {
		LogUtil.startLog(ContentQualifyController.class.toString(), ContentQualifyDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(ContentQualifyDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(ContentQualifyController.class.toString(), ContentQualifyDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面迁移(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ContentQualifyDefine.INFO_ACTION)
	@RequiresPermissions(value = { ContentQualifyDefine.PERMISSIONS_INFO, ContentQualifyDefine.PERMISSIONS_ADD,ContentQualifyDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView info(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(ContentQualifyDefine.CONTENTQUALIFY_FORM) ContentQualifyBean form) {
		LogUtil.startLog(ContentQualifyController.class.toString(), ContentQualifyDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(ContentQualifyDefine.INFO_PATH);
		if (StringUtils.isNotEmpty(form.getIds())) {
			Integer id = Integer.valueOf(form.getIds());
			ContentQualify record = contentQualifyService.getRecord(id);
			modelAndView.addObject(ContentQualifyDefine.CONTENTQUALIFY_FORM, record);
			String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
			modelAndView.addObject("fileDomainUrl",fileDomainUrl);
		}
		LogUtil.endLog(ContentQualifyController.class.toString(), ContentQualifyDefine.INIT);
		return modelAndView;
	}

	/**
	 * 条件查询
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ContentQualifyDefine.SEARCH_ACTION)
	@RequiresPermissions(ContentQualifyDefine.PERMISSIONS_SEARCH)
	public ModelAndView searchAction(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(ContentQualifyDefine.CONTENTQUALIFY_FORM) ContentQualifyBean form) {
		LogUtil.startLog(ContentQualifyController.class.toString(), ContentQualifyDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(ContentQualifyDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(ContentQualifyController.class.toString(), ContentQualifyDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 文章列表维护分页机能 根据条件
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, ContentQualifyBean form) {
		List<ContentQualify> recordList = this.contentQualifyService.selectRecordList(form, -1, -1);
		if (recordList != null) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			recordList = this.contentQualifyService.selectRecordList(form, paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(ContentQualifyDefine.CONTENTQUALIFY_FORM, form);
			String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
			modelAndView.addObject("fileDomainUrl",fileDomainUrl);
		}
	}

	/**
	 * 添加配置维护信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = ContentQualifyDefine.INSERT_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(ContentQualifyDefine.PERMISSIONS_ADD)
	public ModelAndView insertAction(HttpServletRequest request, ContentQualify form) {
		LogUtil.startLog(ContentQualifyController.class.toString(), ContentQualifyDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(ContentQualifyDefine.INFO_PATH);
		// 调用校验
		if (validatorFieldCheck(modelAndView, form) != null) {
			// 失败返回
			return validatorFieldCheck(modelAndView, form);
		}
		contentQualifyService.insertRecord(form);
		modelAndView.addObject(ContentQualifyDefine.SUCCESS, ContentQualifyDefine.SUCCESS);
		LogUtil.endLog(ContentQualifyController.class.toString(), ContentQualifyDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 修改配置维护信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = ContentQualifyDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(ContentQualifyDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateAction(HttpServletRequest request, ContentQualify form) {
		LogUtil.startLog(ContentQualifyController.class.toString(), ContentQualifyDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(ContentQualifyDefine.INFO_PATH);

		// 调用校验
		if (validatorFieldCheck(modelAndView, form) != null) {
			// 失败返回
			return validatorFieldCheck(modelAndView, form);
		}
		// 根据id更新
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "id", form.getId().toString())) {
			return modelAndView;
		}

		contentQualifyService.updateRecord(form);
		modelAndView.addObject(ContentQualifyDefine.SUCCESS, ContentQualifyDefine.SUCCESS);

		LogUtil.endLog(ContentQualifyController.class.toString(), ContentQualifyDefine.UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 * 删除配置信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ContentQualifyDefine.DELETE_ACTION)
	@RequiresPermissions(ContentQualifyDefine.PERMISSIONS_DELETE)
	public ModelAndView deleteRecordAction(HttpServletRequest request, String ids) {
		LogUtil.startLog(ContentQualifyController.class.toString(), ContentQualifyDefine.DELETE_ACTION);

		ModelAndView modelAndView = new ModelAndView(ContentQualifyDefine.RE_LIST_PATH);
		// 解析json字符串
		List<Integer> recordList = JSONArray.parseArray(ids, Integer.class);
		contentQualifyService.deleteRecord(recordList);
		LogUtil.endLog(ContentQualifyController.class.toString(), ContentQualifyDefine.DELETE_ACTION);
		return modelAndView;
	}

	/**
	 * 修改状态
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ContentQualifyDefine.STATUS_ACTION)
	@RequiresPermissions(ContentQualifyDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateStatus(HttpServletRequest request,RedirectAttributes attr,ContentQualifyBean form) {
		LogUtil.startLog(ContentPartnerController.class.toString(), ContentQualifyDefine.STATUS_ACTION);

		ModelAndView modelAndView = new ModelAndView(ContentQualifyDefine.RE_LIST_PATH);
		// 修改状态
		if (StringUtils.isNotEmpty(form.getIds())) {
			Integer id = Integer.valueOf(form.getIds());
			ContentQualify record = this.contentQualifyService.getRecord(id);
			if (record.getStatus() == 1) {
				record.setStatus(0);
			} else {
				record.setStatus(1);
			}
			this.contentQualifyService.updateRecord(record);
		}
		attr.addFlashAttribute(ContentQualifyDefine.CONTENTQUALIFY_FORM, form);
		LogUtil.endLog(ContentPartnerController.class.toString(), ContentQualifyDefine.STATUS_ACTION);
		return modelAndView;
	}

	/**
	 * 调用校验表单方法
	 * 
	 * @param modelAndView
	 * @param form
	 * @return
	 */
	private ModelAndView validatorFieldCheck(ModelAndView modelAndView, ContentQualify form) {
		// 字段校验(非空判断和长度判断)
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "name", form.getName())) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "imgurl", form.getImgurl())) {
			form.setImgurl("");
		}
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "orderNum", form.getOrderNum().toString())) {
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
	@RequestMapping(value = ContentQualifyDefine.UPLOAD_FILE, method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = { ContentQualifyDefine.PERMISSIONS_ADD,
			ContentQualifyDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogUtil.startLog(ContentQualifyController.class.toString(), ContentQualifyDefine.UPLOAD_FILE);
		String files = borrowCommonService.uploadFile(request, response);
		LogUtil.endLog(ContentQualifyController.class.toString(), ContentQualifyDefine.UPLOAD_FILE);
		return files;
	}

}
