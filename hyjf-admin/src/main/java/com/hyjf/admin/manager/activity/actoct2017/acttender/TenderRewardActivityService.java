package com.hyjf.admin.manager.activity.actoct2017.acttender;

import java.util.List;
import java.util.Map;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.customize.admin.act.ActTen2017Customize;

public interface TenderRewardActivityService extends BaseService {

	List<ActTen2017Customize> selectRecordList(Map<String,Object> paraMap);

	Integer selectRecordCount(Map<String,Object> paraMap);

	List<ActTen2017Customize> selectTenderReturnDetail(Map<String,Object> paraMap);

	Integer selectTenderReturnDetailCount(Map<String,Object> paraMap);

	ActivityList getActivityListById(Integer activityId);


}
