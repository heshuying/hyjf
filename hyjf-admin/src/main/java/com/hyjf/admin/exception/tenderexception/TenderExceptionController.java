package com.hyjf.admin.exception.tenderexception;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.admin.AdminTenderExceptionCustomize;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = TenderExceptionDefine.REQUEST_MAPPING)
public class TenderExceptionController extends BaseController {

	@Autowired
	private TenderExceptionService tenderExceptionService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(TenderExceptionDefine.INIT)
	@RequiresPermissions(TenderExceptionDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, TenderExceptionBean form) {
		LogUtil.startLog(TenderExceptionController.class.toString(), TenderExceptionDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(TenderExceptionDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(TenderExceptionController.class.toString(), TenderExceptionDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(TenderExceptionDefine.SEARCH_ACTION)
	@RequiresPermissions(TenderExceptionDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, TenderExceptionBean form) {
		LogUtil.startLog(TenderExceptionController.class.toString(), TenderExceptionDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(TenderExceptionDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(TenderExceptionController.class.toString(), TenderExceptionDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, TenderExceptionBean form) {

		AdminTenderExceptionCustomize TenderExceptionCustomize = new AdminTenderExceptionCustomize();
		TenderExceptionCustomize.setBorrowNidSrch(form.getBorrowNidSrch());
		TenderExceptionCustomize.setTenderUserNameSrch(form.getTenderUserNameSrch());
		TenderExceptionCustomize.setNidSrch(form.getNidSrch());
		TenderExceptionCustomize.setTimeStartSrch(form.getTimeStartSrch());
		TenderExceptionCustomize.setTimeEndSrch(form.getTimeEndSrch());
		Integer count = this.tenderExceptionService.countTenderException(TenderExceptionCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			TenderExceptionCustomize.setLimitStart(paginator.getOffset());
			TenderExceptionCustomize.setLimitEnd(paginator.getLimit());
			List<AdminTenderExceptionCustomize> recordList = this.tenderExceptionService
					.selectTenderExceptionList(TenderExceptionCustomize);
			form.setPaginator(paginator);
			String sumAccount = this.tenderExceptionService.sumTenderExceptionAccount(TenderExceptionCustomize);
			modelAndView.addObject("sumAccount", sumAccount);
			modelAndView.addObject("recordList", recordList);
		}

		modelAndView.addObject(TenderExceptionDefine.FORM, form);
	}

	/**
	 * 解冻异常修复
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = TenderExceptionDefine.TENDER_BACK_ACTION)
	@ResponseBody
	public JSONObject tenderBackAction(HttpServletRequest request, TenderExceptionBean form) {
		LogUtil.startLog(TenderExceptionController.class.toString(), TenderExceptionDefine.TENDER_BACK_ACTION);
		JSONObject object = new JSONObject();

		boolean trxIdFlag = this.tenderExceptionService.selsectNidIsExists(form.getNid());

		if (!trxIdFlag) {
			object.put("message", "出借订单号在系统不存在！");
			return object;
		}

		String message = "";
		try {
			message = this.tenderExceptionService.updateBackTender(form);
		} catch (Exception e) {
			message = e.getMessage();
		}
		if (StringUtils.isNotEmpty(message)) {
			object.put("message", message);
			return object;
		}

		LogUtil.endLog(TenderExceptionController.class.toString(), TenderExceptionDefine.TENDER_BACK_ACTION);

		object.put("message", "");
		return object;

	}
}
