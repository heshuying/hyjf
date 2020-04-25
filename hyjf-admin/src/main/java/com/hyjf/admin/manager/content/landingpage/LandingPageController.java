package com.hyjf.admin.manager.content.landingpage;

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
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonService;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.GetterUtil;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.LandingPage;

/**
 * 活动列表页
 * 
 * @author 李深强
 *
 */
@Controller
@RequestMapping(value = LandingPageDefine.REQUEST_MAPPING)
public class LandingPageController extends BaseController {

	@Autowired
	private LandingPageService landingPageService;
	@Autowired
	private BorrowCommonService borrowCommonService;

	/**
	 * 活动列表维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(LandingPageDefine.INIT)
	@RequiresPermissions(LandingPageDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(LandingPageDefine.LANDING_PAGE_FORM) LandingPageBean form) {
		LogUtil.startLog(LandingPageController.class.toString(), LandingPageDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(LandingPageDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(LandingPageController.class.toString(), LandingPageDefine.INIT);
		return modelAndView;
	}

	/**
	 * 条件查询
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(LandingPageDefine.SEARCH_ACTION)
	@RequiresPermissions(LandingPageDefine.PERMISSIONS_SEARCH)
	public ModelAndView select(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(LandingPageDefine.LANDING_PAGE_FORM) LandingPageBean form) {
		LogUtil.startLog(LandingPageController.class.toString(), LandingPageDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(LandingPageDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(LandingPageController.class.toString(), LandingPageDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 活动列表维护分页机能 页面初始化
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, LandingPageBean form) {
		Integer count = this.landingPageService.getRecordCount(form);
		if(count > 0){
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			form.setPaginator(paginator);
			List<LandingPage> recordList = this.landingPageService.getRecordList(form, paginator.getOffset(), paginator.getLimit());
			form.setRecordList(recordList);
			String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
			modelAndView.addObject("fileDomainUrl", fileDomainUrl);
		}
		modelAndView.addObject(LandingPageDefine.LANDING_PAGE_FORM, form);
	}

	/**
	 * 画面迁移(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(LandingPageDefine.INFO_ACTION)
	@RequiresPermissions(value = {LandingPageDefine.PERMISSIONS_ADD,LandingPageDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView info(HttpServletRequest request, RedirectAttributes attr,@ModelAttribute(LandingPageDefine.LANDING_PAGE_FORM) LandingPageBean form) {
		LogUtil.startLog(LandingPageController.class.toString(), LandingPageDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(LandingPageDefine.INFO_PATH);
		try {
			if (StringUtils.isNotEmpty(form.getIds())) {
				Integer id = Integer.valueOf(form.getIds());
				LandingPage record = this.landingPageService.getRecord(id);
				BeanUtils.copyProperties(record, form);
				String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
				modelAndView.addObject("fileDomainUrl", fileDomainUrl);
			}
			modelAndView.addObject(LandingPageDefine.LANDING_PAGE_FORM, form);
		} catch (Exception e) {
			e.printStackTrace();
		}
		LogUtil.endLog(LandingPageController.class.toString(), LandingPageDefine.INIT);
		return modelAndView;
	}

	/**
	 * 添加信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = LandingPageDefine.INSERT_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(LandingPageDefine.PERMISSIONS_ADD)
	public ModelAndView insertAction(HttpServletRequest request, LandingPage form) {
		LogUtil.startLog(LandingPageController.class.toString(), LandingPageDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(LandingPageDefine.INFO_PATH);
		// 调用校验
		if (validatorFieldCheck(modelAndView, form) != null) {
			// 失败返回
			return validatorFieldCheck(modelAndView, form);
		}
		// 数据插入
		this.landingPageService.insertRecord(form);
		modelAndView.addObject(LandingPageDefine.SUCCESS, LandingPageDefine.SUCCESS);
		LogUtil.endLog(LandingPageController.class.toString(), LandingPageDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 修改信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = LandingPageDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(LandingPageDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateAction(HttpServletRequest request, LandingPage form) {
		LogUtil.startLog(LandingPageController.class.toString(), LandingPageDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(LandingPageDefine.INFO_PATH);
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
		this.landingPageService.updateRecord(form);
		modelAndView.addObject(LandingPageDefine.SUCCESS, LandingPageDefine.SUCCESS);
		LogUtil.endLog(LandingPageController.class.toString(), LandingPageDefine.UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 * 删除信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(LandingPageDefine.DELETE_ACTION)
	@RequiresPermissions(LandingPageDefine.PERMISSIONS_DELETE)
	public ModelAndView deleteRecordAction(HttpServletRequest request, String ids) {
		LogUtil.startLog(LandingPageController.class.toString(), LandingPageDefine.DELETE_ACTION);

		ModelAndView modelAndView = new ModelAndView(LandingPageDefine.RE_LIST_PATH);
		// 解析json字符串
		List<Integer> recordList = JSONArray.parseArray(ids, Integer.class);
		this.landingPageService.deleteRecord(recordList);
		LogUtil.endLog(LandingPageController.class.toString(), LandingPageDefine.DELETE_ACTION);
		return modelAndView;
	}

	/**
	 * 调用校验表单方法
	 * 
	 * @param modelAndView
	 * @param form
	 * @return
	 */
	private ModelAndView validatorFieldCheck(ModelAndView modelAndView, LandingPage form) {
		// 字段校验(非空判断和长度判断)
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "channelName", form.getChannelName())) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "pageName", form.getPageName())) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "pageUrl", form.getPageUrl())) {
			return modelAndView;
		}
		return null;
	}

    /**
     * 检查着落页名称唯一
     * 
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = LandingPageDefine.CHECK_ACTION, method = RequestMethod.POST)
    @RequiresPermissions(value = { LandingPageDefine.PERMISSIONS_ADD, LandingPageDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
    public String checkAction(HttpServletRequest request) {
        LogUtil.startLog(LandingPageController.class.toString(), LandingPageDefine.CHECK_ACTION);
        String id = request.getParameter("id");
        String param = request.getParameter("param");
        JSONObject ret = new JSONObject();
        // 检查着落页名称唯一性
        int cnt = landingPageService.countByPageName(GetterUtil.getInteger(id), param);
        if (cnt > 0) {
            String message = ValidatorFieldCheckUtil.getErrorMessage("repeat", "");
            message = message.replace("{label}", "着落页名称");
            ret.put(LandingPageDefine.JSON_VALID_INFO_KEY, message);
        }
        // 没有错误时,返回y
        if (!ret.containsKey(LandingPageDefine.JSON_VALID_INFO_KEY)) {
            ret.put(LandingPageDefine.JSON_VALID_STATUS_KEY, LandingPageDefine.JSON_VALID_STATUS_OK);
        }
        LogUtil.endLog(LandingPageController.class.toString(), LandingPageDefine.CHECK_ACTION);
        return ret.toString();
    }

	
	
	/**
	 * 资料上传
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = LandingPageDefine.UPLOAD_FILE, method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = { LandingPageDefine.PERMISSIONS_ADD,LandingPageDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogUtil.startLog(LandingPageController.class.toString(), LandingPageDefine.UPLOAD_FILE);
		String files = borrowCommonService.uploadFile(request, response);
		LogUtil.endLog(LandingPageController.class.toString(), LandingPageDefine.UPLOAD_FILE);
		return files;
	}

}
