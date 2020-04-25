package com.hyjf.activity.invite7;

import java.util.List;

import com.hyjf.base.service.BaseService;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.ActivityInviteSeven;

public interface InviteSevenService extends BaseService {

    List<ActivityInviteSeven> selectRecordList(String investType, Integer userId, Paginator paginator);

    Integer selectRecordCount(String investType, Integer userId);
    
    

}
