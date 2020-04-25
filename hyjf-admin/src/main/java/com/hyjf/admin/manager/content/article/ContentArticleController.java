package com.hyjf.admin.manager.content.article;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.alibaba.fastjson.JSONArray;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.maintenance.config.ConfigController;
import com.hyjf.admin.maintenance.config.ConfigDefine;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonService;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.Category;
import com.hyjf.mybatis.model.auto.ContentArticle;

/**
 * 文章列表页
 * 
 * @author qingbing
 *
 */
@Controller
@RequestMapping(value = ContentArticleDefine.REQUEST_MAPPING)
public class ContentArticleController extends BaseController {

	@Autowired
	private ContentArticleService contentArticleService;
	@Autowired
	private BorrowCommonService borrowCommonService;

	/**
	 * 文章列表维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ContentArticleDefine.INIT)
	@RequiresPermissions(ContentArticleDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(ContentArticleDefine.CONTENTARTICLE_FORM) ContentArticleBean form) {
		LogUtil.startLog(ContentArticleController.class.toString(), ContentArticleDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(ContentArticleDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(ContentArticleController.class.toString(), ContentArticleDefine.INIT);
		return modelAndView;
	}

	/**
	 * 文章列表维护分页机能 页面初始化
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, ContentArticleBean form) {
		List<ContentArticle> recordList = this.contentArticleService.selectRecordList(form, -1, -1);
		putCategory(modelAndView);
		if (recordList != null) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			recordList = this.contentArticleService.selectRecordList(form, paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(ContentArticleDefine.CONTENTARTICLE_FORM, form);
			String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
			modelAndView.addObject("fileDomainUrl", fileDomainUrl);
		}
		putCategory(modelAndView);
	}

	/**
	 * 画面迁移(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ContentArticleDefine.INFO_ACTION)
	@RequiresPermissions(value = { ContentArticleDefine.PERMISSIONS_INFO, ContentArticleDefine.PERMISSIONS_ADD,
			ContentArticleDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView info(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(ContentArticleDefine.CONTENTARTICLE_FORM) ContentArticleBean form) {

		LogUtil.startLog(ContentArticleController.class.toString(), ContentArticleDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(ContentArticleDefine.INFO_PATH);

		// CategoryExample categoryExample = new CategoryExample();
		// CategoryExample.Criteria categoryCriteria =
		// categoryExample.createCriteria();
		// categoryCriteria.andGroupEqualTo("news");
		// List<Category> categoryList =
		// this.categoryService.getCategory(categoryExample);
		putCategory(modelAndView);
		if (StringUtils.isNotEmpty(form.getIds())) {
			Integer id = Integer.valueOf(form.getIds());
			ContentArticle record = this.contentArticleService.getRecord(id);
			modelAndView.addObject(ContentArticleDefine.CONTENTARTICLE_FORM, record);
			String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
			modelAndView.addObject("fileDomainUrl", fileDomainUrl);
		}
		LogUtil.endLog(ContentArticleController.class.toString(), ContentArticleDefine.INIT);
		return modelAndView;
	}

	/**
	 * 设置Category
	 * 
	 * @param modelAndView
	 */
	private void putCategory(ModelAndView modelAndView) {
		List<Category> categoryList = new ArrayList<Category>();
		{
			Category category = new Category();
			category.setId(2);
			category.setTitle("网站公告");
			categoryList.add(category);
		}
		{
			Category category = new Category();
			category.setId(3);
			category.setTitle("网贷知识");
			categoryList.add(category);
		}
		{
			Category category = new Category();
			category.setId(5);
			category.setTitle("关于我们");
			categoryList.add(category);
		}
		{
			Category category = new Category();
			category.setId(101);
			category.setTitle("风险教育");
			categoryList.add(category);
		}
		// {
		// Category category = new Category();
		// category.setId(6);
		// category.setTitle("创始人");
		// categoryList.add(category);
		// }
		{
			Category category = new Category();
			category.setId(8);
			category.setTitle("联系我们");
			categoryList.add(category);
		}
		{
			Category category = new Category();
			category.setId(20);
			category.setTitle("公司动态");
			categoryList.add(category);
		}
		modelAndView.addObject("categoryList", categoryList);
	}

	/**
	 * 根据条件查询所需要数据
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ContentArticleDefine.SEARCH_ACTION)
	@RequiresPermissions(ContentArticleDefine.PERMISSIONS_SEARCH)
	public ModelAndView selectContentArticle(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(ContentArticleDefine.CONTENTARTICLE_FORM) ContentArticleBean form) {
		LogUtil.startLog(ContentArticleController.class.toString(), ContentArticleDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(ContentArticleDefine.LIST_PATH);
		// 参数校验
		if (StringUtils.isNotEmpty(form.getStartCreate())
				&& StringUtils.isNotEmpty(form.getEndCreate())
				&& GetDate.getMillis(GetDate.str2Timestamp(form.getStartCreate())) > GetDate.getMillis(GetDate
						.str2Timestamp(form.getEndCreate()))) {
			return modelAndView.addObject("errorMsg", "活动创建开始日期不能小于结束日期");
		}
		// 创建分页
		this.createPageBy(request, modelAndView, form);
		LogUtil.endLog(ContentArticleController.class.toString(), ContentArticleDefine.INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 文章列表维护分页机能 根据条件
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPageBy(HttpServletRequest request, ModelAndView modelAndView, ContentArticleBean form) {
		List<ContentArticle> recordList = this.contentArticleService.selectRecordList(form, -1, -1);
		if (recordList != null) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			recordList = this.contentArticleService.selectRecordList(form, paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(ContentArticleDefine.CONTENTARTICLE_FORM, form);
		}
		putCategory(modelAndView);
	}

	/**
	 * 添加活动信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = ContentArticleDefine.INSERT_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(ContentArticleDefine.PERMISSIONS_ADD)
	public ModelAndView insertAction(HttpServletRequest request, ContentArticle form) {
		LogUtil.startLog(ContentArticleController.class.toString(), ContentArticleDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(ContentArticleDefine.INFO_PATH);

		// 调用校验
		if (validatorFieldCheck(modelAndView, form) != null) {
			// 失败返回
			return validatorFieldCheck(modelAndView, form);
		}
		// 数据插入
		this.contentArticleService.insertRecord(form);
		// 跳转页面用（info里面有）
		modelAndView.addObject(ContentArticleDefine.SUCCESS, ContentArticleDefine.SUCCESS);
		modelAndView.addObject(ContentArticleDefine.CONTENTARTICLE_FORM, new ContentArticle());
		LogUtil.endLog(ContentArticleController.class.toString(), ContentArticleDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 修改活动维护信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = ContentArticleDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(ContentArticleDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateAction(HttpServletRequest request, ContentArticle form) {
		LogUtil.startLog(ContentArticleController.class.toString(), ContentArticleDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(ContentArticleDefine.INFO_PATH);

		// 调用校验
		if (validatorFieldCheck(modelAndView, form) != null) {
			// 失败返回
			return validatorFieldCheck(modelAndView, form);
		}
		// 根据id更新
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "id", form.getId().toString())) {
			return modelAndView;
		}
		// 更新
		this.contentArticleService.updateRecord(form);
		// 跳转页面用（info里面有）
		modelAndView.addObject(ContentArticleDefine.SUCCESS, ContentArticleDefine.SUCCESS);
		modelAndView.addObject(ContentArticleDefine.CONTENTARTICLE_FORM, new ContentArticle());
		LogUtil.endLog(ContentArticleController.class.toString(), ContentArticleDefine.UPDATE_ACTION);
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
	@RequiresPermissions(ContentArticleDefine.PERMISSIONS_DELETE)
	public ModelAndView deleteRecordAction(HttpServletRequest request, String ids) {
		LogUtil.startLog(ConfigController.class.toString(), ConfigDefine.DELETE_ACTION);

		ModelAndView modelAndView = new ModelAndView(ContentArticleDefine.RE_LIST_PATH);
		// 解析json字符串
		List<Integer> recordList = JSONArray.parseArray(ids, Integer.class);
		this.contentArticleService.deleteRecord(recordList);
		LogUtil.endLog(ConfigController.class.toString(), ConfigDefine.DELETE_ACTION);
		return modelAndView;
	}

	/**
	 * 修改状态
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ContentArticleDefine.STATUS_ACTION)
	@RequiresPermissions(ContentArticleDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateStatus(HttpServletRequest request, RedirectAttributes attr, ContentArticleBean form) {
		LogUtil.startLog(ConfigController.class.toString(), ContentArticleDefine.STATUS_ACTION);

		ModelAndView modelAndView = new ModelAndView(ContentArticleDefine.RE_LIST_PATH);
		// 修改状态
		if (StringUtils.isNotEmpty(form.getIds())) {
			Integer id = Integer.valueOf(form.getIds());
			ContentArticle record = this.contentArticleService.getRecord(id);
			if (record.getStatus() == 1) {
				record.setStatus(0);
			} else {
				record.setStatus(1);
			}
			this.contentArticleService.updateRecord(record);
		}
		attr.addFlashAttribute(ContentArticleDefine.CONTENTARTICLE_FORM, form);
		LogUtil.endLog(ConfigController.class.toString(), ContentArticleDefine.STATUS_ACTION);
		return modelAndView;
	}

	/**
	 * 调用校验表单方法
	 * 
	 * @param modelAndView
	 * @param form
	 * @return
	 */
	private ModelAndView validatorFieldCheck(ModelAndView modelAndView, ContentArticle form) {
		// 字段校验(非空判断和长度判断)
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "title", form.getTitle())) {
			return modelAndView;
		}
		;
		if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "title", form.getTitle(), 50, true)) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "type", form.getType())) {
			return modelAndView;
		}
		;
		if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "type", form.getType(), 50, true)) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "status", form.getStatus().toString())) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "author", form.getAuthor())) {
			return modelAndView;
		}
		;
		if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "author", form.getAuthor(), 50, true)) {
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
	@RequestMapping(value = ContentArticleDefine.UPLOAD_FILE, method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = { ContentArticleDefine.PERMISSIONS_ADD, ContentArticleDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogUtil.startLog(ConfigController.class.toString(), ContentArticleDefine.UPLOAD_FILE);
		String files = borrowCommonService.uploadFile(request, response);
		LogUtil.endLog(ConfigController.class.toString(), ContentArticleDefine.UPLOAD_FILE);
		return files;
	}
}
