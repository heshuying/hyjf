package com.hyjf.admin.promotion.channel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.maintenance.admin.AdminDefine;
import com.hyjf.admin.manager.borrow.borrow.BorrowBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.Utm;
import com.hyjf.mybatis.model.auto.UtmPlat;
import com.hyjf.mybatis.model.customize.ChannelCustomize;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = ChannelDefine.REQUEST_MAPPING)
public class ChannelController extends BaseController {

	@Autowired
	private ChannelService channelService;

	/**
	 * 列表维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ChannelDefine.INIT)
	@RequiresPermissions(ChannelDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(ChannelDefine.FORM) ChannelBean form) {
		LogUtil.startLog(ChannelController.class.toString(), ChannelDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(ChannelDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(ChannelController.class.toString(), ChannelDefine.INIT);
		return modelAndView;
	}

	/**
	 * 列表维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ChannelDefine.SEARCH_ACTION)
	@RequiresPermissions(ChannelDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(ChannelDefine.FORM) ChannelBean form) {
		LogUtil.startLog(ChannelController.class.toString(), ChannelDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(ChannelDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(ChannelController.class.toString(), ChannelDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 文章列表维护分页机能 页面初始化
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, ChannelBean form) {

		List<UtmPlat> utmPlatList = this.channelService.getUtmPlat(StringUtils.EMPTY);
		modelAndView.addObject("utmPlatList", utmPlatList);

		ChannelCustomize channelCustomize = new ChannelCustomize();
		channelCustomize.setSourceIdSrch(form.getSourceIdSrch());
		channelCustomize.setUtmTermSrch(form.getUtmTermSrch());

		Integer count = this.channelService.countList(channelCustomize);
		if (count != null) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			channelCustomize.setLimitStart(paginator.getOffset());
			channelCustomize.setLimitEnd(paginator.getLimit());
			List<ChannelCustomize> recordList = this.channelService.getRecordList(channelCustomize);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}

		modelAndView.addObject(ChannelDefine.FORM, form);
	}

	/**
	 * 画面迁移(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(ChannelDefine.INFO_ACTION)
	@RequiresPermissions(value = { ChannelDefine.PERMISSIONS_ADD, ChannelDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView info(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(ChannelDefine.FORM) ChannelBean form) throws UnsupportedEncodingException {
		LogUtil.startLog(ChannelController.class.toString(), ChannelDefine.INFO_ACTION);

		ModelAndView modelAndView = new ModelAndView(ChannelDefine.INFO_PATH);
		List<UtmPlat> utmPlatList = this.channelService.getUtmPlat(StringUtils.EMPTY);
		modelAndView.addObject("utmPlatList", utmPlatList);

		if (StringUtils.isNotEmpty(form.getUtmId())) {
			Utm record = channelService.getRecord(form.getUtmId());
			if (record.getUtmReferrer() == null || record.getUtmReferrer() == 0) {
				modelAndView.addObject("utmReferrer", "");
			} else {
				Users user = this.channelService.getUser(StringUtils.EMPTY, String.valueOf(record.getUtmReferrer()));
				modelAndView.addObject("utmReferrer", user.getUsername());
			}
			modelAndView.addObject(ChannelDefine.FORM, record);
			if (record != null) {
				modelAndView.addObject("url", this.getUrl(record));
			}
			return modelAndView;
		}
		LogUtil.endLog(ChannelController.class.toString(), ChannelDefine.INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 获取URL
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String getUrl(Utm record) throws UnsupportedEncodingException {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append(record.getLinkAddress());
		strBuf.append("?utmId=");
		strBuf.append(record.getUtmId());
		if (StringUtils.isNotEmpty(record.getUtmSource())) {
			strBuf.append("&utm_source=");
			strBuf.append(toUnicode(record.getUtmSource()));
		}
		if (StringUtils.isNotEmpty(record.getUtmMedium())) {
			strBuf.append("&utm_medium=");
			strBuf.append(toUnicode(record.getUtmMedium()));

		}
		if (StringUtils.isNotEmpty(record.getUtmTerm())) {
			strBuf.append("&utm_term=");
			strBuf.append(toUnicode(record.getUtmTerm()));

		}
		if (StringUtils.isNotEmpty(record.getUtmContent())) {
			strBuf.append("&utm_content=");
			strBuf.append(toUnicode(record.getUtmContent()));

		}
		if (StringUtils.isNotEmpty(record.getUtmCampaign())) {
			strBuf.append("&utm_campaign=");
			strBuf.append(toUnicode(record.getUtmCampaign()));

		}
		if (record.getUtmReferrer() != null && record.getUtmReferrer() != 0) {
			// 统一推荐人字段,以便在着陆页操作
			strBuf.append("&refferUserId=");
			strBuf.append(record.getUtmReferrer());

		}
		return strBuf.toString();
	}

	/**
	 * Unicode
	 * 
	 * @param str
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String toUnicode(String str) throws UnsupportedEncodingException {
		return URLEncoder.encode(str, "UTF-8");
	}

	/**
	 * 添加信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = ChannelDefine.INSERT_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(ChannelDefine.PERMISSIONS_ADD)
	public ModelAndView insertAction(HttpServletRequest request, ChannelBean form) {
		LogUtil.startLog(ChannelController.class.toString(), ChannelDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(ChannelDefine.INFO_PATH);

		if (StringUtils.isNotEmpty(form.getUtmId())) {
			Utm record = channelService.getRecord(form.getUtmId());
			if (record != null) {
				ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "sourceId", "repeat");
			}
		}

		// 画面验证
		this.validatorFieldCheck(modelAndView, form);

		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			List<UtmPlat> utmPlatList = this.channelService.getUtmPlat(StringUtils.EMPTY);
			modelAndView.addObject("utmPlatList", utmPlatList);
			modelAndView.addObject("utmReferrer", form.getUtmReferrer());
			modelAndView.addObject(ChannelDefine.FORM, form);
			return modelAndView;
		}
		try {
			// 数据插入
			this.channelService.insertRecord(form);
		} catch (Exception e) {
			// 跳转页面用（info里面有）
			List<UtmPlat> utmPlatList = this.channelService.getUtmPlat(StringUtils.EMPTY);
			modelAndView.addObject("utmPlatList", utmPlatList);
			modelAndView.addObject("utmReferrer", form.getUtmReferrer());
			modelAndView.addObject(ChannelDefine.FORM, form);
			modelAndView.addObject(ChannelDefine.SUCCESS, "error");
			return modelAndView;
		}
		// 跳转页面用（info里面有）
		modelAndView.addObject(ChannelDefine.SUCCESS, ChannelDefine.SUCCESS);
		LogUtil.endLog(ChannelController.class.toString(), ChannelDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 修改维护信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = ChannelDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(ChannelDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateAction(HttpServletRequest request, ChannelBean form) {
		LogUtil.startLog(ChannelController.class.toString(), ChannelDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(ChannelDefine.INFO_PATH);

		// 画面验证
		this.validatorFieldCheck(modelAndView, form);

		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			List<UtmPlat> utmPlatList = this.channelService.getUtmPlat(StringUtils.EMPTY);
			modelAndView.addObject("utmPlatList", utmPlatList);
			modelAndView.addObject("utmReferrer", form.getUtmReferrer());
			modelAndView.addObject(ChannelDefine.FORM, form);
			return modelAndView;
		}

		// 更新
		this.channelService.updateRecord(form);
		// 跳转页面用（info里面有）
		modelAndView.addObject(ChannelDefine.SUCCESS, ChannelDefine.SUCCESS);
		LogUtil.endLog(ChannelController.class.toString(), ChannelDefine.UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 * 画面校验
	 * 
	 * @param modelAndView
	 * @param form
	 */
	private void validatorFieldCheck(ModelAndView modelAndView, ChannelBean form) {
		// 渠道
		boolean sourceIdFlag = ValidatorFieldCheckUtil.validateRequired(modelAndView, "sourceId", form.getSourceId());
		// 推广方式
		ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "utmMedium", form.getUtmMedium(), 50, false);
		// 推广单元
		ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "utmContent", form.getUtmContent(), 50, false);
		// 推广计划
		ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "utmCampaign", form.getUtmCampaign(), 50, false);
		// 关键字
		boolean utmTermFlag = ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "utmTerm", form.getUtmTerm(), 50, false);
		// 推荐人
//		boolean utmReferrerFlag = ValidatorFieldCheckUtil.validateRequired(modelAndView, "utmReferrer", form.getUtmReferrer());
//		if (utmReferrerFlag) {
//			int usersFlag = this.channelService.checkUtmReferrer(form.getUtmReferrer());
//			if (usersFlag == 1) {
//				ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "utmReferrer", "referrer_username.not.exists");
//			}
//		}
		// 链接地址
		ValidatorFieldCheckUtil.validateRequired(modelAndView, "linkAddress", form.getLinkAddress());
		ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "linkAddress", form.getLinkAddress(), 250, false);
		// 备注说明
		ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "remark", form.getRemark(), 100, false);

		if (sourceIdFlag && utmTermFlag && StringUtils.isNotEmpty(form.getUtmTerm())) {
			Utm utm = this.channelService.getRecord(form.getSourceId(), form.getUtmTerm());
			if (utm != null) {
				if(StringUtils.isNotEmpty(form.getUtmId())){
					if(utm.getUtmId() != Integer.parseInt(form.getUtmId())){
						ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "sourceId-utmTerm", "exists.sourceid.utmterm");
					}
				}else{
					ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "sourceId-utmTerm", "exists.sourceid.utmterm");
				}
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
	@RequestMapping(ChannelDefine.DELETE_ACTION)
	@RequiresPermissions(ChannelDefine.PERMISSIONS_DELETE)
	public String deleteRecordAction(HttpServletRequest request, RedirectAttributes attr, ChannelBean form) {
		LogUtil.startLog(ChannelController.class.toString(), ChannelDefine.DELETE_ACTION);
		this.channelService.deleteRecord(form.getUtmId());
		attr.addFlashAttribute(ChannelDefine.FORM, form);
		LogUtil.endLog(ChannelController.class.toString(), ChannelDefine.DELETE_ACTION);
		return "redirect:" + ChannelDefine.REQUEST_MAPPING + "/" + ChannelDefine.INIT;
	}

	/**
	 * 检查编号唯一性
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = ChannelDefine.CHECK_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(value = { ChannelDefine.PERMISSIONS_ADD, ChannelDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public String checkAction(HttpServletRequest request) {
		LogUtil.startLog(ChannelController.class.toString(), ChannelDefine.CHECK_ACTION);

		String param = request.getParameter("param");
		JSONObject ret = new JSONObject();

		int usersFlag = this.channelService.checkUtmReferrer(param);
		if (usersFlag == 1) {
			String message = ValidatorFieldCheckUtil.getErrorMessage("referrer_username.not.exists", "");
			ret.put(AdminDefine.JSON_VALID_INFO_KEY, message);
			return ret.toString();
		}

		ret.put(AdminDefine.JSON_VALID_STATUS_KEY, AdminDefine.JSON_VALID_STATUS_OK);

		return ret.toJSONString();
	}

	/**
	 * 资料上传
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = ChannelDefine.UPLOAD_FILE, method = RequestMethod.POST)
	@ResponseBody
	public JSONArray uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogUtil.startLog(ChannelController.class.toString(), ChannelDefine.UPLOAD_FILE);
		JSONArray files = this.channelService.uploadFile(request, response);
		LogUtil.endLog(ChannelController.class.toString(), ChannelDefine.UPLOAD_FILE);
		return files;
	}

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(ChannelDefine.DOWNLOAD_ACTION)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, BorrowBean form) throws Exception {
		LogUtil.startLog(ChannelController.class.toString(), ChannelDefine.DOWNLOAD_ACTION);
		// 表格sheet名称
		String sheetName = "推广管理模板";

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "渠道(utm_source)", "推广方式(utm_medium)", "推广单元(utm_content)", "推广计划(utm_campaign)", "关键字(utm_term)", "推荐人(utm_referrer)", "备注" };
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();

		// 生成一个表格
		ExportExcel.createHSSFWorkbookTitle(workbook, titles, sheetName);

		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(ChannelController.class.toString(), ChannelDefine.DOWNLOAD_ACTION);
	}

}
