package com.hyjf.web.user.preregistcea;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.plexus.util.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.mybatis.model.customize.admin.AdminPreRegistChannelExclusiveActivityCustomize;
import com.hyjf.web.BaseServiceImpl;

@Service
public class PreRegistChannelExclusiveActivityServiceImpl extends BaseServiceImpl implements PreRegistChannelExclusiveActivityService {
    
	@Override
	public List<Map<String, Object>> getRecordList(Map<String, Object> map) {
	    List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
	    List<AdminPreRegistChannelExclusiveActivityCustomize> preRegistChannelExclusiveActivityList = adminPreRegistChannelExclusiveActivityCustomizeMapper.selectRecordList(map);
	    Map<String, Object> resultMap = null;
	    if(preRegistChannelExclusiveActivityList!=null && preRegistChannelExclusiveActivityList.size()>0){
            for(AdminPreRegistChannelExclusiveActivityCustomize preRegistChannelExclusiveActivity : preRegistChannelExclusiveActivityList){
                resultMap = new HashMap<String, Object>();
                if(StringUtils.isNotEmpty(preRegistChannelExclusiveActivity.getUserId())){
                    UsersInfoExample example = new UsersInfoExample();
                    UsersInfoExample.Criteria criteria = example.createCriteria();
                    criteria.andUserIdEqualTo(Integer.parseInt(preRegistChannelExclusiveActivity.getUserId()));
                    List<UsersInfo> usersList = usersInfoMapper.selectByExample(example);
                    if(usersList!=null && usersList.size()>0){
                        resultMap.put("username", usersList.get(0).getTruename()!=null?(usersList.get(0).getTruename().substring(0, 1)+"**"):"***");
                    }
                }else{
                    resultMap.put("username", "***");
                }
                if(StringUtils.isNotEmpty(preRegistChannelExclusiveActivity.getMobile())){
                    resultMap.put("mobile", preRegistChannelExclusiveActivity.getMobile().substring(0, 3)+"********");
                }else{
                    resultMap.put("mobile", "特服号码段");
                }
                resultList.add(resultMap);
            }
        }
	    return resultList;
	}
}
