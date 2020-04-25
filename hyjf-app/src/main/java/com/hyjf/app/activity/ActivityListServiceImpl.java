package com.hyjf.app.activity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.app.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.customize.ActivityListCustomize;
@Service
public class ActivityListServiceImpl extends BaseServiceImpl implements ActivityListService {

	/**
	 * 查询活动数量
	 * @return
	 */
	public Integer queryActivityCount(ActivityListCustomize activityListCustomize){
		Integer count= this.activityListCustomizeMapper.queryActivityCount(activityListCustomize);
		return count;
	}
	
	/**
	 * 查询活动列表
	 * @return
	 */
	public List<ActivityListBean> queryActivityList(ActivityListCustomize activityListCustomize){       
		List<ActivityList> activitys = this.activityListCustomizeMapper.queryActivityList(activityListCustomize);        
		
		List<ActivityListBean> beanList= new ArrayList<ActivityListBean>();
		if(activitys!=null && activitys.size()>0){
			for(ActivityList al:activitys){
				ActivityListBean bean =new ActivityListBean();
				bean.setImg(PropUtils.getSystem("hyjf.web.host")+al.getImg());//应前台要求，路径给绝对路径
				bean.setTitle(al.getTitle());
				bean.setTimeStart(al.getTimeStart());
				bean.setTimeEnd(al.getTimeEnd());
				bean.setUrlForeground(al.getUrlForeground());
				if (al.getTimeStart() >= GetDate.getNowTime10()) {
					bean.setStatus("未开始");
				}
				if (al.getTimeEnd() <= GetDate.getNowTime10()) {
					bean.setStatus("已完成");
				}
				if (al.getTimeEnd() >= GetDate.getNowTime10()
						&& al.getTimeStart() <= GetDate.getNowTime10()) {
					bean.setStatus("进行中");
				}
				beanList.add(bean);
			}
		}
		return beanList;
	}
	
}
