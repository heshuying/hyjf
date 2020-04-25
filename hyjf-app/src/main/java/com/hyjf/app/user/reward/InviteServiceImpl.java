package com.hyjf.app.user.reward;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.admin.UserInfoForLogCustomize;
import com.hyjf.mybatis.model.customize.web.WebInviteRecordCustomize;
import com.hyjf.mybatis.model.customize.web.WebRewardRecordCustomize;
import com.hyjf.app.BaseServiceImpl;

@Service
public class InviteServiceImpl extends BaseServiceImpl implements InviteService {

	/**
	 * 查询邀请记录
	 * @param userId
	 * @return
	 */
	public List<WebInviteRecordCustomize> queryInviteRecords(Integer userId,int offset,int limit){
		return webInviteCustomizeMapper.queryInviteRecords(userId,offset,limit);
	}
	/**
	 * 查询邀请好友数量
	 * @param userId
	 * @return
	 */
	public Integer queryInviteCount(Integer userId){
		return webInviteCustomizeMapper.queryInviteCount(userId);
	}
	/**
	 * 统计用户邀请奖励分红总额
	 * @param userId
	 * @return
	 */
	public BigDecimal queryRewardRecordsSum(Integer userId){
		return webInviteCustomizeMapper.queryRewardRecordsSum(userId);
	}
	
	/**
     * 查询奖励记录
     * @param userId
     * @return
     */
    public List<WebRewardRecordCustomize> queryRewardRecords(Integer userId,int offset,int limit){
        return webInviteCustomizeMapper.queryRewardRecords(userId,offset,limit);
    }
	/**
	 * 查询奖励记录数量
	 * @param userId
	 * @return
	 */
	public Integer queryRewardRecordsCount(Integer userId){
		return webInviteCustomizeMapper.queryRewardRecordsCount(userId);
	}
	@Override
	public List<UserInfoForLogCustomize> queryInviteList(int referId,int limitStart,int limitEnd) {
		HashMap<String,Object> params = new HashMap<String,Object>();
        params.put("limitStart", limitStart);
        params.put("limitEnd", limitEnd);
        params.put("referId", referId);
		return usersCustomizeMapper.selectInviteUserList(params);
	}
	@Override
	public int queryInviteCount(int referId) {
		return usersCustomizeMapper.selectInviteUserCount(referId);
	}
	
	
}


