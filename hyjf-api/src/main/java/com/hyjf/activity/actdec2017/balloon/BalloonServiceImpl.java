package com.hyjf.activity.actdec2017.balloon;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.ActdecTenderBalloon;
import com.hyjf.mybatis.model.auto.ActdecTenderBalloonExample;

@Service
public class BalloonServiceImpl extends BaseServiceImpl implements BalloonService {
	
	/**
	 * 加载数据
	 * @param userId
	 * @return
	 */
	@Override
	public ActdecTenderBalloon getBalloonRecord(Integer userId){
		ActdecTenderBalloonExample example = new ActdecTenderBalloonExample();
		example.createCriteria().andUserIdEqualTo(userId);
		example.setOrderByClause("id asc");
		
		List<ActdecTenderBalloon> balloonList = actdecTenderBalloonMapper.selectByExample(example);
		
		if(balloonList != null && !balloonList.isEmpty()){
			return balloonList.get(0);
		}
		
		return null;

	}
	
	/**
	 * 一键领取操作
	 */
	@Override
	public int updateBalloonReceive(Integer userId){
		ActdecTenderBalloon record = getBalloonRecord(userId);
		if(record != null){
			ActdecTenderBalloon bean = new ActdecTenderBalloon();
			bean.setBallonReceived(record.getBallonReceived() + record.getBallonCanReceive());
			bean.setBallonCanReceive(0);
			bean.setRewardName(getRewardName(bean.getBallonReceived() + bean.getBallonCanReceive()));
			bean.setUpdateTime(GetDate.getNowTime10());
			
			ActdecTenderBalloonExample example = new ActdecTenderBalloonExample();
			example.createCriteria().andUserIdEqualTo(userId);
			return actdecTenderBalloonMapper.updateByExampleSelective(bean, example);
		}
		
		return 0;
	}
	
	/**
	 * 计算奖励名称
	 * @param balloonCount
	 * @return
	 */
	private String getRewardName(Integer balloonCount){
		if(balloonCount>=1 && balloonCount<3){
			return "60元代金券一张";
		}else if(balloonCount>=3 && balloonCount<5){
			return "100元代金券一张";
		}else if(balloonCount>=5 && balloonCount<10){
			return "200元代金券一张";
		}else if(balloonCount>=10 && balloonCount<30){
			return "500元红包";
		}else if(balloonCount>=30 && balloonCount<60){
			return "1200元红包";
		}else if(balloonCount>=60 && balloonCount<100){
			return "3000元红包";
		}else if(balloonCount>=100){
			return "6000元红包";
		}else{
			return "";
		}
		
	}
}
