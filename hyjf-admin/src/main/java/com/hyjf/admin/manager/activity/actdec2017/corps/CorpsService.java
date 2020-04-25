package com.hyjf.admin.manager.activity.actdec2017.corps;

import java.util.List;
import java.util.Map;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.ActdecCorps;
import com.hyjf.mybatis.model.auto.ActdecCorpsExample;
import com.hyjf.mybatis.model.auto.ActdecTenderBalloon;
import com.hyjf.mybatis.model.auto.ActdecWinning;

public interface CorpsService extends BaseService {

	int countRecordListDetail(CorpsBean cb);

	List<ActdecWinning> selectRecordListDetail(CorpsBean cb, int limitStart, int limitEnd);
	//中奖列表
	int updateRecordListD(int type);
	
}
