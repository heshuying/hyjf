package com.hyjf.admin.manager.activity.actnov2017.fightluck;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.ActRewardList;
import com.hyjf.mybatis.model.auto.ActRewardListExample;

@Service
public class ActFightLuckServiceImpl extends BaseServiceImpl implements ActFightLuckService {
	
    @Override
    public List<ActRewardList> getRecordList() {
        ActRewardListExample example=new ActRewardListExample();
        example.createCriteria().andAct1RewardTypeEqualTo(4).andActTypeEqualTo(4);
        return actRewardListMapper.selectByExample(example);
    }

    
}
