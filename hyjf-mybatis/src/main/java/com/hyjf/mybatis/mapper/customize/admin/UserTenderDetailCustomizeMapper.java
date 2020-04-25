/**
 * 10月份活动   用户使用推荐星信息列表
 * zhangjinpeng
 */

package com.hyjf.mybatis.mapper.customize.admin;

import java.math.BigDecimal;
import java.util.List;

import com.hyjf.mybatis.model.customize.admin.UserTenderDetailCustomize;


public interface UserTenderDetailCustomizeMapper {
	
	/**
	 * 获取投之家渠道用户出借信息列表
	 */
	List<UserTenderDetailCustomize> selectUserTenderDetailList(UserTenderDetailCustomize userTenderDetailCustomize);
	
	/**
	 * 获取投之家渠道用户出借信息总数量
	 */
	Integer selectUserTenderDetailCount(UserTenderDetailCustomize userTenderDetailCustomize);
	
	/**
	 * 获取投之家渠道用户出借额累计
	 */
	BigDecimal selectUserTenderAccountTotal(UserTenderDetailCustomize userTenderDetailCustomize);

}
