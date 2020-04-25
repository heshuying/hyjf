/**
 * 10月份活动   获取推荐星明细列表
 * zhangjinpeng
 */

package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;

import com.hyjf.mybatis.model.customize.admin.GetRecommendCustomize;


public interface GetRecommendCustomizeMapper {
	
	/**
	 * 获取推荐星明细列表
	 */
	List<GetRecommendCustomize> selectInviteRecommendList(GetRecommendCustomize getRecommendCustomize);
	
	/**
	 * 获取推荐星明细列表总数量
	 */
	Integer selectInviteRecommendTotal(GetRecommendCustomize getRecommendCustomize);

}
