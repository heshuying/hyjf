package com.hyjf.app.user.reward;

import java.math.BigDecimal;


import java.util.List;

import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.admin.UserInfoForLogCustomize;
import com.hyjf.mybatis.model.customize.web.WebInviteRecordCustomize;
import com.hyjf.mybatis.model.customize.web.WebRewardRecordCustomize;
import com.hyjf.app.BaseService;

public interface InviteService extends BaseService {

	/**
	 * 查询邀请记录
	 * @param userId
	 * @return
	 */
	public List<WebInviteRecordCustomize> queryInviteRecords(Integer userId,int offset,int limit);
	/**
	 * 查询邀请好友数量
	 * @param userId
	 * @return
	 */
	public Integer queryInviteCount(Integer userId);
	/**
	 * 查询奖励记录
	 * @param userId
	 * @return
	 */
	public List<WebRewardRecordCustomize> queryRewardRecords(Integer userId,int offset,int limit);
	/**
	 * 查询奖励记录数量
	 * @param userId
	 * @return
	 */
	public Integer queryRewardRecordsCount(Integer userId);
	
	/**
     * 统计用户邀请奖励分红总额
     * @param userId
     * @return
     */
    public BigDecimal queryRewardRecordsSum(Integer userId);
    /**
     * 重写了获取所有推荐人的信息
     * @param referId
     * @return
     */
    public List<UserInfoForLogCustomize> queryInviteList(int referId,int limitStart,int limitEnd);
    
    public int queryInviteCount(int referId);
}


