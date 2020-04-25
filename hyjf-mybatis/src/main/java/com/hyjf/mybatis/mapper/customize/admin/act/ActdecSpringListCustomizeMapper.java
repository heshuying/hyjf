/**
 * 10月份活动   获取推荐星明细列表
 * zhangjinpeng
 */

package com.hyjf.mybatis.mapper.customize.admin.act;

import java.util.List;

import com.hyjf.mybatis.model.customize.admin.act.ActdecSpringListCustomize;


public interface ActdecSpringListCustomizeMapper {
	
	/**
	 * 获取春节活动明细列表
	 */
	List<ActdecSpringListCustomize> selectInviteRecommendList(ActdecSpringListCustomize getRecommendCustomize);
	
	/**
	 * 获取春节活动明细列表总数量
	 */
	Integer selectInviteRecommendTotal(ActdecSpringListCustomize getRecommendCustomize);

}
