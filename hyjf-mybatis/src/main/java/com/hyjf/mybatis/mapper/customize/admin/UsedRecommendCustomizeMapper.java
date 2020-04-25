/**
 * 10月份活动   用户使用推荐星信息列表
 * zhangjinpeng
 */

package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;

import com.hyjf.mybatis.model.customize.admin.UsedRecommendCustomize;


public interface UsedRecommendCustomizeMapper {
	
	/**
	 * 获取用户使用推荐星信息列表
	 */
	List<UsedRecommendCustomize> selectInviteRecommendList(UsedRecommendCustomize usedRecommendCustomize);
	
	/**
	 * 获取用户使用推荐星信息总数量
	 */
	Integer selectInviteRecommendTotal(UsedRecommendCustomize usedRecommendCustomize);

}
