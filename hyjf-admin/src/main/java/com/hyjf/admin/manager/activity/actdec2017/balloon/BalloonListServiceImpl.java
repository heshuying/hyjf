package com.hyjf.admin.manager.activity.actdec2017.balloon;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.ActdecTenderBalloon;
import com.hyjf.mybatis.model.auto.ActdecTenderBalloonExample;

@Service
public class BalloonListServiceImpl extends BaseServiceImpl implements BalloonListService {
	
	@Override
	public List<ActdecTenderBalloon> selectRecordList(Map<String,Object> paraMap) {
		return actCustomizeMapper.selectBalloonList(paraMap);

	}
	
	@Override
	public int countBalloonList(Map<String,Object> paraMap){
		return actCustomizeMapper.countBalloonList(paraMap);
	}
	
	@Override
	public List<ActdecTenderBalloon> selectRecordListDetail(Integer userId, int limitStart, int limitEnd) {
		ActdecTenderBalloonExample example = new ActdecTenderBalloonExample();
		example.createCriteria().andUserIdEqualTo(userId);
		example.setOrderByClause("id desc");
		example.setLimitStart(limitStart);
		example.setLimitEnd(limitEnd);
		List<ActdecTenderBalloon> resultList = actdecTenderBalloonMapper.selectByExample(example);
		if(resultList ==null){
			return new ArrayList<ActdecTenderBalloon>();
		}
		
		return resultList;

	}
	
	@Override
	public int countRecordListDetail(Integer userId) {
		ActdecTenderBalloonExample example = new ActdecTenderBalloonExample();
		example.createCriteria().andUserIdEqualTo(userId);
		return actdecTenderBalloonMapper.countByExample(example);

	}

}
