package com.hyjf.admin.manager.content.ads;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
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
import com.hyjf.admin.app.maintenance.banner.AppBannerDefine;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonService;
import com.hyjf.admin.manager.content.links.ContentPartnerController;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.Ads;
import com.hyjf.mybatis.model.auto.AdsType;
import com.hyjf.mybatis.model.auto.AdsWithBLOBs;

/**
 * 广告管理
 * 
 * @author qingbing
 *
 */
@Controller
@RequestMapping(value = ContentAdsDefine.REQUEST_MAPPING)
public class ContentAdsController extends BaseController {

	@Autowired
	private ContentAdsService contentAdsService;
	@Autowired
	private BorrowCommonService borrowCommonService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ContentAdsDefine.INIT)
	@RequiresPermissions(ContentAdsDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request,@ModelAttribute(ContentAdsDefine.ADS_FORM) ContentAdsBean form) {
		LogUtil.startLog(ContentAdsController.class.toString(), ContentAdsDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(ContentAdsDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(ContentAdsController.class.toString(), ContentAdsDefine.INIT);
		return modelAndView;
	}

	/**
	 *查询
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ContentAdsDefine.SEARCH_ACTION)
	@RequiresPermissions(ContentAdsDefine.PERMISSIONS_SEARCH)
	public ModelAndView select(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(ContentAdsDefine.ADS_FORM) ContentAdsBean form) {
		LogUtil.startLog(ContentPartnerController.class.toString(), ContentAdsDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(ContentAdsDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(ContentPartnerController.class.toString(), ContentAdsDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 分页
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, ContentAdsBean form) {
		
		int count = this.contentAdsService.countRecordList(form);
		if (count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			List<Ads> recordList = this.contentAdsService.getRecordList(form, paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(AppBannerDefine.ADS_FORM, form);
		}
		// 获取广告类型列表
		List<AdsType> adsTypeList = this.contentAdsService.getAdsTypeList();
		modelAndView.addObject("adsTypeList", adsTypeList);
		// 文件根目录
		String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
		modelAndView.addObject("fileDomainUrl", fileDomainUrl);
	}

	/**
	 * 画面迁移(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ContentAdsDefine.INFO_ACTION)
	@RequiresPermissions(value = { ContentAdsDefine.PERMISSIONS_INFO, ContentAdsDefine.PERMISSIONS_ADD,ContentAdsDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView info(HttpServletRequest request, RedirectAttributes attr,@ModelAttribute(ContentAdsDefine.ADS_FORM) ContentAdsBean form) {
		LogUtil.startLog(ContentAdsController.class.toString(), ContentAdsDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(ContentAdsDefine.INFO_PATH);
		try {
			if (StringUtils.isNotEmpty(form.getIds())) {
				Integer id = Integer.valueOf(form.getIds());
				Ads record = this.contentAdsService.getRecord(id);
				BeanUtils.copyProperties(record, form);
				String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
				modelAndView.addObject("fileDomainUrl", fileDomainUrl);
			} 
			//类型
			List<AdsType> adsTypeList = this.contentAdsService.getAdsTypeList();
			modelAndView.addObject("adsTypeList", adsTypeList);
			modelAndView.addObject(ContentAdsDefine.ADS_FORM, form);
		} catch (Exception e) {
			e.printStackTrace();
		}
		LogUtil.endLog(ContentAdsController.class.toString(), ContentAdsDefine.INIT);
		return modelAndView;
	}

	/**
	 * 添加
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = ContentAdsDefine.INSERT_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(ContentAdsDefine.PERMISSIONS_ADD)
	public ModelAndView insertAction(HttpServletRequest request, AdsWithBLOBs form) {
		LogUtil.startLog(ContentAdsController.class.toString(), ContentAdsDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(ContentAdsDefine.INFO_PATH);
		// 调用校验
		if (validatorFieldCheck(modelAndView, form) != null) {
			// 失败返回
			return validatorFieldCheck(modelAndView, form);
		}
		// 数据插入
		this.contentAdsService.insertRecord(form);
		modelAndView.addObject(ContentAdsDefine.SUCCESS, ContentAdsDefine.SUCCESS);
		LogUtil.endLog(ContentAdsController.class.toString(), ContentAdsDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 修改
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = ContentAdsDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(ContentAdsDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateAction(HttpServletRequest request, AdsWithBLOBs form) {
		LogUtil.startLog(ContentAdsController.class.toString(), ContentAdsDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(ContentAdsDefine.INFO_PATH);
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
		this.contentAdsService.updateRecord(form);
		modelAndView.addObject(ContentAdsDefine.SUCCESS, ContentAdsDefine.SUCCESS);
		LogUtil.endLog(ContentAdsController.class.toString(), ContentAdsDefine.UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 * 修改状态
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ContentAdsDefine.STATUS_ACTION)
	@RequiresPermissions(ContentAdsDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateStatus(HttpServletRequest request,RedirectAttributes attr, ContentAdsBean form) {
		LogUtil.startLog(ContentAdsController.class.toString(), ContentAdsDefine.STATUS_ACTION);

		ModelAndView modelAndView = new ModelAndView(ContentAdsDefine.RE_LIST_PATH);
		// 修改状态
		if (StringUtils.isNotEmpty(form.getIds()) ) {
			Integer id = Integer.valueOf(form.getIds());
			AdsWithBLOBs record = this.contentAdsService.getRecord(id);
			if (record.getStatus() == 1) {
				record.setStatus((short) 0);
			} else {
				record.setStatus((short) 1);
			}
			this.contentAdsService.updateRecord(record);
		}
		attr.addFlashAttribute(ContentAdsDefine.ADS_FORM, form);
		LogUtil.endLog(ContentAdsController.class.toString(), ContentAdsDefine.STATUS_ACTION);
		return modelAndView;
	}

	/**
	 * 删除
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ContentAdsDefine.DELETE_ACTION)
	@RequiresPermissions(ContentAdsDefine.PERMISSIONS_DELETE)
	public ModelAndView deleteRecordAction(HttpServletRequest request, String ids) {
		LogUtil.startLog(ContentAdsController.class.toString(), ContentAdsDefine.DELETE_ACTION);

		ModelAndView modelAndView = new ModelAndView(ContentAdsDefine.RE_LIST_PATH);
		// 解析json字符串
		List<Integer> recordList = JSONArray.parseArray(ids, Integer.class);
		this.contentAdsService.deleteRecord(recordList);
		LogUtil.endLog(ContentAdsController.class.toString(), ContentAdsDefine.DELETE_ACTION);
		return modelAndView;
	}

	/**
	 * 调用校验表单方法
	 * 
	 * @param modelAndView
	 * @param form
	 * @return
	 */
	private ModelAndView validatorFieldCheck(ModelAndView modelAndView, AdsWithBLOBs form) {
		// 字段校验(非空判断和长度判断)
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "name", form.getName())) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "code", form.getCode())) {
			form.setCode("");
		}
		if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "name", form.getName(), 50, true)) {
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
	@RequestMapping(value = ContentAdsDefine.UPLOAD_FILE, method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = { ContentAdsDefine.PERMISSIONS_ADD,ContentAdsDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogUtil.startLog(ContentAdsController.class.toString(), ContentAdsDefine.UPLOAD_FILE);
		String files = borrowCommonService.uploadFile(request, response);
		LogUtil.endLog(ContentAdsController.class.toString(), ContentAdsDefine.UPLOAD_FILE);
		return files;
	}

}
