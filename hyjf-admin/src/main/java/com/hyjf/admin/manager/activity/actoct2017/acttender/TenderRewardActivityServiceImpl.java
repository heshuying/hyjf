package com.hyjf.admin.manager.activity.actoct2017.acttender;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.customize.admin.act.ActTen2017Customize;

@Service
public class TenderRewardActivityServiceImpl extends BaseServiceImpl implements TenderRewardActivityService {

    @Override
	public Integer selectRecordCount(Map<String,Object> paraMap) {
        return actTen2017CustomizeMapper.selectTenderReturnListCount(paraMap);
    }

    @Override
	public List<ActTen2017Customize> selectRecordList(Map<String,Object> paraMap) {
    	return actTen2017CustomizeMapper.selectTenderReturnList(paraMap);
        
    }
    
   	@Override
	public Integer selectTenderReturnDetailCount(Map<String,Object> paraMap) {
        return actTen2017CustomizeMapper.selectTenderReturnDetailCount(paraMap);
    }

   	@Override
	public List<ActTen2017Customize> selectTenderReturnDetail(Map<String,Object> paraMap) {
       	return actTen2017CustomizeMapper.selectTenderReturnDetail(paraMap);
           
    }
   	
    @Override
	public ActivityList getActivityListById(Integer activityId){
        ActivityList activity = this.activityListMapper
                .selectByPrimaryKey(Integer.valueOf(activityId));
        return activity;
    }
    

}
