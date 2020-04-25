package com.hyjf.admin.manager.activity.inviteact;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.ActivityInviteSeven;

public interface InviteSevenActivityService extends BaseService {

    Integer selectRecordCount(InviteSevenActivityBean form);

    List<ActivityInviteSeven> selectRecordList(InviteSevenActivityBean form, Paginator paginator);

}
