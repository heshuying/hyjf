package com.hyjf.admin.manager.activity.actdec2017.balloon;

import java.util.List;
import java.util.Map;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.ActdecTenderBalloon;

public interface BalloonListService extends BaseService {

	int countBalloonList(Map<String,Object> paraMap);

	List<ActdecTenderBalloon> selectRecordList(Map<String,Object> paraMap);

	int countRecordListDetail(Integer userId);

	List<ActdecTenderBalloon> selectRecordListDetail(Integer userId, int limitStart, int limitEnd);
	
}
