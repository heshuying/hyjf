package com.hyjf.activity.mgm10.regist;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.base.service.BaseService;

public interface RegistRecommendService extends BaseService {
    
	JSONObject validateService(String id,Integer inviteUser,Integer inviteByUser);
	
	void insertInviteInfo(Integer inviteUser,Integer inviteByUser) throws Exception;
}
