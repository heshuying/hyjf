package com.hyjf.admin.invite.user;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.admin.InviteUserCustomize;

/**
 * 10月份邀请活动-邀请用户列表
 * @author zhangjinpeng
 *
 */
@Service
public class InviteUserServiceImpl extends BaseServiceImpl implements InviteUserService {
	
    /**
     * 获取参与活动的邀请用户列表
     *
     * @return
     */
	@Override
    public List<InviteUserCustomize> getRecordList(InviteUserCustomize inviteUserCustomize) {
        return this.inviteUserCustomizeMapper.selectInviteUserList(inviteUserCustomize);
    }

	/**
	 * 获取邀请用户的总数量
	 */
	@Override
	public Integer getRecordTotal(InviteUserCustomize inviteUserCustomize) {
		return this.inviteUserCustomizeMapper.selectInviteUserTotal(inviteUserCustomize);
	}

    
}
