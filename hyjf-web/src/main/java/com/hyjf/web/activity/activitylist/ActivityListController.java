package com.hyjf.web.activity.activitylist;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.Ads;
import com.hyjf.mybatis.model.auto.AdsType;
import com.hyjf.web.BaseController;
import com.hyjf.web.home.HomePageDefine;

/**
 * 
 * 活动列表
 * 
 * @author hyjf
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年4月19日
 * @see 上午9:58:53
 */
@Controller("activityListController")
@RequestMapping(value = ActivityListDefine.REQUEST_MAPPING)
public class ActivityListController extends BaseController {

	@Autowired
	private ActivityListService activityListService;

	/**
	 * 
	 * 活动列表初期化
	 * 
	 * @param request
	 * @param attr
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = ActivityListDefine.ACTIVITY_LISTPAGE_ACTION)
	public AdsResultBean getActivityListPage(AdsBean form) {
		// 返回结果
		AdsResultBean result = new AdsResultBean();
		// 获取活动类型
		AdsType adsType = this.activityListService.getAdsTypeByCode("activity");
		if (adsType == null) {
			result.isStatus();
			result.setHost(HomePageDefine.HOST);
			return result;
		}
		// 获取活动件数
		int totalPage = this.activityListService.getRecordListCountByTypeid(adsType.getTypeid());

		if (totalPage > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), totalPage, form.getPageSize());
			// 检索活动列表数据
			List<Ads> recordList = this.activityListService.getRecordList(adsType.getTypeid(), paginator.getOffset(), paginator.getLimit());
			List<AdsBean> resultRecordList = new ArrayList<AdsBean>();
			for (int i = 0; i < recordList.size(); i++) {
				AdsBean resultBean = new AdsBean();
				BeanUtils.copyProperties(recordList.get(i), resultBean);
				resultBean.setActivitiDesc(recordList.get(i).getActivitiDesc() == null ? "" : recordList.get(i).getActivitiDesc());
				resultRecordList.add(resultBean);
			}

			result.setPaginator(paginator);
			result.setAdsList(resultRecordList);
		}
		result.success();
		result.setHost(HomePageDefine.HOST);
		return result;
	}

	/**
	 * 
	 * 公告列表页面
	 * 
	 * @author liuyang
	 * @param form
	 * @return
	 */
	@RequestMapping(value = ActivityListDefine.ACTIVITY_LIST_ACTION)
	public ModelAndView init(@ModelAttribute(ActivityListDefine.ACTIVITYLIST_FORM) AdsBean form) {
		ModelAndView modelAndView = new ModelAndView(ActivityListDefine.LIST_PATH);
		return modelAndView;
	}

}
