package com.hyjf.admin.manager.activity.actoct2017.actsignin;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
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
import com.hyjf.admin.manager.config.bankrecharge.BankRechargeBean;
import com.hyjf.admin.manager.config.bankrecharge.BankRechargeController;
import com.hyjf.admin.manager.config.bankrecharge.BankRechargeDefine;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.ActSignin;
import com.hyjf.mybatis.model.auto.BanksConfig;


/**
 * 
 * 
 * @author
 *
 */
@Controller
@RequestMapping(value = ActSigninDefine.REQUEST_MAPPING)
public class ActSigninController extends BaseController {

	@Autowired
	private ActSigninService actSigninService;


	/**
	 * 列表维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ActSigninDefine.INIT)
	@RequiresPermissions(ActSigninDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(ActSigninDefine.CONFIGBANK_FORM) ActSigninBean form) {
		LogUtil.startLog(ActSigninController.class.toString(), ActSigninDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(ActSigninDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(ActSigninController.class.toString(), ActSigninDefine.INIT);
		return modelAndView;
	}

	/**
	 * 文章列表维护分页机能 页面初始化
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, ActSigninBean form) {
		List<ActSignin> recordList = this.actSigninService.getRecordList(new ActSignin(), -1, -1);
		if (recordList != null) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			ActSignin bc=new ActSignin();
			bc.setUserName(form.getUserName());
			bc.setMobile(form.getMobile());
			bc.setRemark(form.getRemark());
			
			recordList = this.actSigninService.getRecordList(bc, paginator.getOffset(),
					paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(ActSigninDefine.CONFIGBANK_FORM, form);
			String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
			modelAndView.addObject("fileDomainUrl",fileDomainUrl);
		}
	}

	/**
	 * 查看详细页面
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ActSigninDefine.INFO_ACTION)
	@RequiresPermissions( ActSigninDefine.PERMISSIONS_VIEW)
	public ModelAndView info(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(ActSigninDefine.CONFIGBANK_FORM) ActSigninBean form) {
		LogUtil.startLog(ActSigninController.class.toString(), ActSigninDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(ActSigninDefine.LIST_INFO_PATH);

		this.listCreatePage(request, modelAndView, form);
		LogUtil.endLog(ActSigninController.class.toString(), ActSigninDefine.INIT);
		return modelAndView;
	}

	/**
	 * 分页机能 页面初始化详细页面
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void listCreatePage(HttpServletRequest request, ModelAndView modelAndView, ActSigninBean form) {
		Integer uid;
		if(""!=form.getUserId()+""){
			uid=form.getUserId();
		}else{
			uid=Integer.parseInt(request.getParameter("userId").toString()) ;
		}
		List<ActSignin> recordList = this.actSigninService.getRecord(uid , -1, -1);
		if (recordList != null) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			
			recordList = this.actSigninService.getRecord(uid, paginator.getOffset(),
					paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(ActSigninDefine.CONFIGBANK_FORM, form);
			String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
			modelAndView.addObject("fileDomainUrl",fileDomainUrl);
		}
	}


	/**
	 * 调用校验表单方法
	 * 
	 * @param modelAndView
	 * @param form
	 * @return
	 */
	private ModelAndView validatorFieldCheck(ModelAndView modelAndView, BanksConfig form) {
		// 字段校验(非空判断和长度判断)
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "name", form.getBankName())) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "name", form.getBankName(), 50, true)) {
			return modelAndView;
		}
//		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "code", form.getBankCode())) {
//			return modelAndView;
//		}
//		if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "code", form.getBankCode(), 10, true)) {
//			return modelAndView;
//		}
		return null;
	}


}
