package com.hyjf.admin.invite.user;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.admin.InviteUserCustomize;

public interface InviteUserService extends BaseService {

	List<InviteUserCustomize> getRecordList(InviteUserCustomize inviteUserCustomize);
	
	Integer getRecordTotal(InviteUserCustomize inviteUserCustomize);
}
