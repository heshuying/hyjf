package com.hyjf.admin.exception.tenderback;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.TenderBackHistory;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = TenderBackDefine.REQUEST_MAPPING)
public class TenderBackController extends BaseController {

	@Autowired
	private TenderBackService tenderBackService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(TenderBackDefine.INIT)
	@RequiresPermissions(TenderBackDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, TenderBackBean form) {
		LogUtil.startLog(TenderBackController.class.toString(), TenderBackDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(TenderBackDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(TenderBackController.class.toString(), TenderBackDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(TenderBackDefine.SEARCH_ACTION)
	@RequiresPermissions(TenderBackDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, TenderBackBean form) {
		LogUtil.startLog(TenderBackController.class.toString(), TenderBackDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(TenderBackDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(TenderBackController.class.toString(), TenderBackDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, TenderBackBean form) {

		Integer count = this.tenderBackService.countTenderBackHistory(form);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			form.setLimitStart(paginator.getOffset());
			form.setLimitEnd(paginator.getLimit());
			List<TenderBackHistory> recordList = this.tenderBackService.selectTenderBackHistoryList(form);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}

		modelAndView.addObject(TenderBackDefine.FORM, form);
	}
}
