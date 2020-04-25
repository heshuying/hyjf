package com.hyjf.mybatis.mapper.customize.web;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hyjf.mybatis.model.customize.web.WebInviteRecordCustomize;
import com.hyjf.mybatis.model.customize.web.WebRewardRecordCustomize;

public interface WebInviteCustomizeMapper {
	/**
	 * 查询邀请记录
	 * @param userId
	 * @return
	 */
	public List<WebInviteRecordCustomize> queryInviteRecords(@Param("userId") Integer userId,@Param("limitStart") int offset,@Param("limitEnd") int limit);	
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
	public List<WebRewardRecordCustomize> queryRewardRecords(@Param("userId") Integer userId,@Param("limitStart") int offset,@Param("limitEnd") int limit);
	/**
	 * 查询奖励记录数量
	 * @param userId
	 * @return
	 */
	public Integer queryRewardRecordsCount(Integer userId);
	/**
	 * 统计用户邀请奖励分红总额
	 * @author pcc
	 * @param userId
	 * @return
	 */
    public BigDecimal queryRewardRecordsSum(Integer userId);
}
