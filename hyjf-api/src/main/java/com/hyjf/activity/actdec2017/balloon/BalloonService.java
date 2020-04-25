package com.hyjf.activity.actdec2017.balloon;

import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.auto.ActdecTenderBalloon;

public interface BalloonService extends BaseService {

	int updateBalloonReceive(Integer userId);

	ActdecTenderBalloon getBalloonRecord(Integer userId);
	
}
