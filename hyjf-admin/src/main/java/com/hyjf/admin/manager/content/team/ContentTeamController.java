package com.hyjf.admin.manager.content.team;

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
import com.hyjf.admin.manager.config.bankconfig.BankConfigController;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.Team;

/**
 * 团队介绍
 * 
 * @author
 *
 */
@Controller
@RequestMapping(value = ContentTeamDefine.REQUEST_MAPPING)
public class ContentTeamController extends BaseController {

	@Autowired
	private ContentTeamService contentTeamService;
	@Autowired
	private BorrowCommonService borrowCommonService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ContentTeamDefine.INIT)
	@RequiresPermissions(ContentTeamDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,@ModelAttribute(ContentTeamDefine.CONTENTTEAMS_FORM) ContentTeamBean form) {
		LogUtil.startLog(ContentTeamController.class.toString(), ContentTeamDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(ContentTeamDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(ContentTeamController.class.toString(), ContentTeamDefine.INIT);
		return modelAndView;
	}

	/**
	 * 根据条件查询所需要数据
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ContentTeamDefine.SEARCH_ACTION)
	@RequiresPermissions(ContentTeamDefine.PERMISSIONS_SEARCH)
	public ModelAndView searchAction(HttpServletRequest request, RedirectAttributes attr,@ModelAttribute(ContentTeamDefine.CONTENTTEAMS_FORM) ContentTeamBean form) {
		LogUtil.startLog(ContentTeamController.class.toString(), ContentTeamDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(ContentTeamDefine.LIST_PATH);
		// 参数校验
		if(StringUtils.isNotEmpty(form.getStartCreate()) && StringUtils.isNotEmpty(form.getEndCreate())){
			if(GetDate.getMillis(GetDate.str2Timestamp(form.getStartCreate())) > GetDate.getMillis(GetDate.str2Timestamp(form.getEndCreate())) ){
				return modelAndView.addObject("errorMsg", "活动创建开始日期不能小于结束日期");
			}
		}
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(ContentTeamController.class.toString(), ContentTeamDefine.INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 分页
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, ContentTeamBean form) {
		List<Team> recordList = this.contentTeamService.selectRecordList(form, -1, -1);
		if (recordList != null) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			recordList = this.contentTeamService.selectRecordList(form, paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(ContentTeamDefine.CONTENTTEAMS_FORM, form);
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
	@RequestMapping(ContentTeamDefine.INFO_ACTION)
	@RequiresPermissions(value = { ContentTeamDefine.PERMISSIONS_INFO, ContentTeamDefine.PERMISSIONS_ADD,ContentTeamDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView info(HttpServletRequest request, RedirectAttributes attr,@ModelAttribute(ContentTeamDefine.CONTENTTEAMS_FORM) ContentTeamBean form) {
		LogUtil.startLog(ContentTeamController.class.toString(), ContentTeamDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(ContentTeamDefine.INFO_PATH);
		if (StringUtils.isNotEmpty(form.getIds())) {
			Integer id = Integer.valueOf(form.getIds());
			Team record = this.contentTeamService.getRecord(id);
			modelAndView.addObject(ContentTeamDefine.CONTENTTEAMS_FORM, record);
			String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
			modelAndView.addObject("fileDomainUrl",fileDomainUrl);
		}
		LogUtil.endLog(ContentTeamController.class.toString(), ContentTeamDefine.INIT);
		return modelAndView;
	}

	
	
	/**
	 * 添加
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = ContentTeamDefine.INSERT_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(ContentTeamDefine.PERMISSIONS_ADD)
	public ModelAndView insertAction(HttpServletRequest request, Team form) {
		LogUtil.startLog(ContentTeamController.class.toString(), ContentTeamDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(ContentTeamDefine.INFO_PATH);
		// form.setImgurl("1");
		// form.setImgappurl(form.getImgurl());
		// 调用校验
		if (validatorFieldCheck(modelAndView, form) != null) {
			// 失败返回
			return validatorFieldCheck(modelAndView, form);
		}
		// 数据插入
		this.contentTeamService.insertRecord(form);
		// 跳转页面用（info里面有）
		modelAndView.addObject(ContentTeamDefine.SUCCESS, ContentTeamDefine.SUCCESS);
		LogUtil.endLog(ContentTeamController.class.toString(), ContentTeamDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 修改
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = ContentTeamDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(ContentTeamDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateAction(HttpServletRequest request, Team form) {
		LogUtil.startLog(ContentTeamController.class.toString(), ContentTeamDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(ContentTeamDefine.INFO_PATH);

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
		this.contentTeamService.updateRecord(form);
		// 跳转页面用（info里面有）
		modelAndView.addObject(ContentTeamDefine.SUCCESS, ContentTeamDefine.SUCCESS);
		LogUtil.endLog(ContentTeamController.class.toString(), ContentTeamDefine.UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 * 删除
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ContentTeamDefine.DELETE_ACTION)
	@RequiresPermissions(ContentTeamDefine.PERMISSIONS_DELETE)
	public ModelAndView deleteRecordAction(HttpServletRequest request, String ids) {
		LogUtil.startLog(ContentTeamController.class.toString(), ContentTeamDefine.DELETE_ACTION);

		ModelAndView modelAndView = new ModelAndView(ContentTeamDefine.RE_LIST_PATH);
		// 解析json字符串
		List<Integer> recordList = JSONArray.parseArray(ids, Integer.class);
		this.contentTeamService.deleteRecord(recordList);
		LogUtil.endLog(ContentTeamController.class.toString(), ContentTeamDefine.DELETE_ACTION);
		return modelAndView;
	}

	/**
	 * 修改状态
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ContentTeamDefine.STATUS_ACTION)
	@RequiresPermissions(ContentTeamDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateStatus(HttpServletRequest request, RedirectAttributes attr,ContentTeamBean form) {
		LogUtil.startLog(ContentTeamController.class.toString(), ContentTeamDefine.STATUS_ACTION);

		ModelAndView modelAndView = new ModelAndView(ContentTeamDefine.RE_LIST_PATH);
		// 修改状态
		if (StringUtils.isNotEmpty(form.getIds())) {
			Integer id = Integer.valueOf(form.getIds());
			Team record = this.contentTeamService.getRecord(id);
			if (record.getStatus() == 1) {
				record.setStatus(0);
			} else {
				record.setStatus(1);
			}
			this.contentTeamService.updateRecord(record);
		}
		attr.addFlashAttribute(ContentTeamDefine.CONTENTTEAMS_FORM, form);
		LogUtil.endLog(ContentTeamController.class.toString(), ContentTeamDefine.STATUS_ACTION);
		return modelAndView;
	}

	/**
	 * 调用校验表单方法
	 * 
	 * @param modelAndView
	 * @param form
	 * @return
	 */
	private ModelAndView validatorFieldCheck(ModelAndView modelAndView, Team form) {
		// 字段校验(非空判断和长度判断)
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "name", form.getName())) {
			return modelAndView;
		}
		;
		if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "name", form.getName(), 60, true)) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "status", form.getStatus().toString())) {
			return modelAndView;
		}
		;
		if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "status", form.getStatus().toString(), 1, true)) {
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
	@RequestMapping(value = ContentTeamDefine.UPLOAD_FILE, method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = { ContentTeamDefine.PERMISSIONS_ADD,
			ContentTeamDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogUtil.startLog(BankConfigController.class.toString(), ContentTeamDefine.UPLOAD_FILE);
		String files = borrowCommonService.uploadFile(request, response);
		LogUtil.endLog(BankConfigController.class.toString(), ContentTeamDefine.UPLOAD_FILE);
		return files;
	}

}
