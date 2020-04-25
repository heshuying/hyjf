package com.hyjf.admin.manager.content.links;

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
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.Links;

/**
 * 友情连接
 * 
 * @author qingbing
 *
 */
@Controller
@RequestMapping(value = ContentLinksDefine.REQUEST_MAPPING)
public class ContentLinksController extends BaseController {

	@Autowired
	private ContentLinksService contentLinksService;
	@Autowired
	private BorrowCommonService borrowCommonService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ContentLinksDefine.INIT)
	@RequiresPermissions(ContentLinksDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(ContentLinksDefine.CONTENTLINKS_FORM) ContentLinksBean form) {
		LogUtil.startLog(ContentLinksController.class.toString(), ContentLinksDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(ContentLinksDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(ContentLinksController.class.toString(), ContentLinksDefine.INIT);
		return modelAndView;
	}

	/**
	 * 分页
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, ContentLinksBean form) {
		// 友情连接为1
		form.setType(1);
		List<Links> recordList = this.contentLinksService.getRecordList(form, -1, -1);
		if (recordList != null) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			recordList = contentLinksService.getRecordList(form, paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(ContentLinksDefine.CONTENTLINKS_FORM, form);
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
	@RequestMapping(ContentLinksDefine.INFO_ACTION)
	@RequiresPermissions(value = { ContentLinksDefine.PERMISSIONS_INFO, ContentLinksDefine.PERMISSIONS_ADD,
			ContentLinksDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView info(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(ContentLinksDefine.CONTENTLINKS_FORM) ContentLinksBean form) {
		LogUtil.startLog(ContentLinksController.class.toString(), ContentLinksDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(ContentLinksDefine.INFO_PATH);

		if (StringUtils.isNotEmpty(form.getIds())) {
			Integer id = Integer.valueOf(form.getIds());
			Links record = this.contentLinksService.getRecord(id.shortValue());
			modelAndView.addObject(ContentLinksDefine.CONTENTLINKS_FORM, record);
			String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
			modelAndView.addObject("fileDomainUrl",fileDomainUrl);
		}
		LogUtil.endLog(ContentLinksController.class.toString(), ContentLinksDefine.INIT);
		return modelAndView;
	}

	/**
	 * 根据条件查询所需要数据
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ContentLinksDefine.SEARCH_ACTION)
	@RequiresPermissions(ContentLinksDefine.PERMISSIONS_SEARCH)
	public ModelAndView selectContentLinks(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(ContentLinksDefine.CONTENTLINKS_FORM) ContentLinksBean form) {
		LogUtil.startLog(ContentLinksController.class.toString(), ContentLinksDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(ContentLinksDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(ContentLinksController.class.toString(), ContentLinksDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 添加活动信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = ContentLinksDefine.INSERT_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(ContentLinksDefine.PERMISSIONS_ADD)
	public ModelAndView insertAction(HttpServletRequest request, Links form) {
		LogUtil.startLog(ContentLinksController.class.toString(), ContentLinksDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(ContentLinksDefine.INFO_PATH);

		// 调用校验
		if (validatorFieldCheck(modelAndView, form) != null) {
			// 失败返回
			return validatorFieldCheck(modelAndView, form);
		}
		// 数据插入(友情连接 为1)
		form.setType(1);
		// 数据插入
		this.contentLinksService.insertRecord(form);
		// 跳转页面用（info里面有）
		modelAndView.addObject(ContentLinksDefine.SUCCESS, ContentLinksDefine.SUCCESS);
		LogUtil.endLog(ContentLinksController.class.toString(), ContentLinksDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 修改活动维护信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = ContentLinksDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(ContentLinksDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateAction(HttpServletRequest request, Links form) {
		LogUtil.startLog(ContentLinksController.class.toString(), ContentLinksDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(ContentLinksDefine.INFO_PATH);

		// 调用校验
		if (validatorFieldCheck(modelAndView, form) != null) {
			// 失败返回
			return validatorFieldCheck(modelAndView, form);
		}
		// 根据id更新
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "id", form.getId().toString())) {
			return modelAndView;
		}
		// 数据插入(友情连接 为1)
		form.setType(1);
		// 更新
		this.contentLinksService.updateRecord(form);
		// 跳转页面用（info里面有）
		modelAndView.addObject(ContentLinksDefine.SUCCESS, ContentLinksDefine.SUCCESS);
		LogUtil.endLog(ContentLinksController.class.toString(), ContentLinksDefine.UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 * 删除配置信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ContentLinksDefine.DELETE_ACTION)
	@RequiresPermissions(ContentLinksDefine.PERMISSIONS_DELETE)
	public ModelAndView deleteRecordAction(HttpServletRequest request, String ids) {
		LogUtil.startLog(ContentLinksController.class.toString(), ContentLinksDefine.DELETE_ACTION);

		ModelAndView modelAndView = new ModelAndView(ContentLinksDefine.RE_LIST_PATH);
		// 解析json字符串
		List<Integer> recordList = JSONArray.parseArray(ids, Integer.class);
		this.contentLinksService.deleteRecord(recordList);
		LogUtil.endLog(ContentLinksController.class.toString(), ContentLinksDefine.DELETE_ACTION);
		return modelAndView;
	}

	/**
	 * 修改状态
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ContentLinksDefine.STATUS_ACTION)
	@RequiresPermissions(ContentLinksDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateStatus(HttpServletRequest request, RedirectAttributes attr,ContentLinksBean form) {
		LogUtil.startLog(ContentPartnerController.class.toString(), ContentLinksDefine.STATUS_ACTION);

		ModelAndView modelAndView = new ModelAndView(ContentLinksDefine.RE_LIST_PATH);
		// 修改状态
		if (StringUtils.isNotEmpty(form.getIds())) {
			Integer id = Integer.valueOf(form.getIds());
			Links record = this.contentLinksService.getRecord(id.shortValue());
			if (record.getStatus() == 1) {
				record.setStatus((short) 0);
			} else {
				record.setStatus((short) 1);
			}
			this.contentLinksService.updateRecord(record);
		}
		attr.addFlashAttribute(ContentLinksDefine.CONTENTLINKS_FORM, form);
		LogUtil.endLog(ContentPartnerController.class.toString(), ContentLinksDefine.STATUS_ACTION);
		return modelAndView;
	}

	/**
	 * 调用校验表单方法
	 * 
	 * @param modelAndView
	 * @param form
	 * @return
	 */
	private ModelAndView validatorFieldCheck(ModelAndView modelAndView, Links form) {
		// 字段校验(非空判断和长度判断)
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "webName", form.getWebname())) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "url", form.getUrl())) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "webName", form.getWebname(), 30, true)) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "webName", form.getUrl(), 60, true)) {
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
	@RequestMapping(value = ContentLinksDefine.UPLOAD_FILE, method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = { ContentLinksDefine.PERMISSIONS_ADD,
			ContentLinksDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogUtil.startLog(ContentLinksController.class.toString(), ContentLinksDefine.UPLOAD_FILE);
		String files = borrowCommonService.uploadFile(request, response);
		LogUtil.endLog(ContentLinksController.class.toString(), ContentLinksDefine.UPLOAD_FILE);
		return files;
	}
}
