package com.hyjf.web.activity.activity;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.web.home.HomePageDefine;

/**
 * 活动列表页
 * 
 * @author qingbing
 *
 */
@Controller
@RequestMapping(value = NewActivityListDefine.REQUEST_MAPPING)
public class NewActivityListController {

	@Autowired
	private NewActivityListService activityListService;

	/**
	 * 活动列表维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(NewActivityListDefine.INIT)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(NewActivityListDefine.ACTIVITYLIST_FORM) NewActivityListBean form) {
		LogUtil.startLog(NewActivityListController.class.toString(), NewActivityListDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(NewActivityListDefine.LIST_PATH);
		LogUtil.endLog(NewActivityListController.class.toString(), NewActivityListDefine.INIT);
		return modelAndView;
	}

	/**
	 * 活动列表维护分页机能 页面初始化
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@ResponseBody
    @RequestMapping(NewActivityListDefine.ACTIVITY_LIST)
	private NewActivityListResultBean getActivityListPage(HttpServletRequest request, ModelAndView modelAndView,
	    NewActivityListBean form) {
		List<ActivityList> recordList = this.activityListService.getRecordList(new ActivityList(), -1, -1);
		NewActivityListResultBean result = new NewActivityListResultBean();
		if (recordList != null) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			recordList = this.activityListService.getRecordList(new ActivityList(), paginator.getOffset(), paginator.getLimit());
			result.setPaginator(paginator);
            result.setActivityListBeanList(recordList);
		}
        result.success();
        result.setHost(HomePageDefine.HOST);
        return result;
	}



}
