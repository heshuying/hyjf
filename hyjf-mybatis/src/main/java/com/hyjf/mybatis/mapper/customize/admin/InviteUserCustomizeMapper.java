/**
 * 10月份活动   邀请会员用户信息列表
 * zhangjinpeng
 */

package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;

import com.hyjf.mybatis.model.customize.admin.InviteUserCustomize;


public interface InviteUserCustomizeMapper {

	/**
	 * 获取参与10月份邀请活动的用户信息列表
	 */
	List<InviteUserCustomize> selectInviteUserList(InviteUserCustomize inviteUserCustomize);
	
	/**
	 * 获取参与10月份邀请活动的用户信息总数量
	 */
	Integer selectInviteUserTotal(InviteUserCustomize inviteUserCustomize);

}
