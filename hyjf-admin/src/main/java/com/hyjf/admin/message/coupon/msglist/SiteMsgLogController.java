package com.hyjf.admin.message.coupon.msglist;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.message.coupon.SiteMsgLogCustomize;

/**
 * 站内信列表
 * 
 * @author
 *
 */
@Controller
@RequestMapping(value = SiteMsgLogDefine.REQUEST_MAPPING)
public class SiteMsgLogController extends BaseController {

	@Autowired
	private SiteMsgLogService SiteMsgLogService;

	/**
	 * 画面初始化
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(SiteMsgLogDefine.INIT)
	@RequiresPermissions(SiteMsgLogDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(SiteMsgLogDefine.FORM) SiteMsgLogBean form) {
		LogUtil.startLog(SiteMsgLogController.class.toString(), SiteMsgLogDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(SiteMsgLogDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(SiteMsgLogController.class.toString(), SiteMsgLogDefine.INIT);
		return modelAndView;
	}

	/**
	 * 分页机能
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, SiteMsgLogBean form) {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		if(StringUtils.isNotEmpty(form.getToUsername())){
			paraMap.put("toUsername", form.getToUsername());
		}
		if(StringUtils.isNotEmpty(form.getTimeStartSrch())){
			paraMap.put("timeStartSrch", form.getTimeStartSrch());
		}
		if(StringUtils.isNotEmpty(form.getTimeEndSrch())){
		    paraMap.put("timeEndSrch", form.getTimeEndSrch());
		}	
		
		Integer count = this.SiteMsgLogService.countMsgLog(paraMap);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			paraMap.put("limitStart", paginator.getOffset());
			paraMap.put("limitEnd", paginator.getLimit());
			List<SiteMsgLogCustomize> recordList  = this.SiteMsgLogService.selectMsgLogList(paraMap);
			form.setPaginator(paginator);
			form.setRecordList(recordList);
		}
		modelAndView.addObject(SiteMsgLogDefine.FORM, form);
	}

	
}
