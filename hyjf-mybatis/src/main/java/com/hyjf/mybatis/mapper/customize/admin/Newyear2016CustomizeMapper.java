/**
 * 2016新年活动
 * zhangjinpeng
 */

package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;

import com.hyjf.mybatis.model.customize.admin.Newyear2016UserCardCustomize;
import com.hyjf.mybatis.model.customize.admin.Newyear2016UserPrizeCustomize;
import com.hyjf.mybatis.model.customize.admin.Newyear2016UserYuanXiaoCustomize;


public interface Newyear2016CustomizeMapper {
	
	/**
	 * 获取用户奖品发放列表
	 * @param paramMap
	 * @return
	 */
	List<Newyear2016UserPrizeCustomize> selectUserPrizeList(Newyear2016UserPrizeCustomize paramBean);
	
	/**
	 * 获取用户奖品总数量
	 * @param paramMap
	 * @return
	 */
	Integer selectUserPrizeTotal(Newyear2016UserPrizeCustomize paramBean);
	
	/**
	 * 获取用户财神卡列表
	 * @param paramMap
	 * @return
	 */
	List<Newyear2016UserCardCustomize> selectUserCardList(Newyear2016UserCardCustomize paramBean);
	
	/**
	 * 获取用户财神卡总数量
	 * @param paramMap
	 * @return
	 */
	Integer selectUserCardTotal(Newyear2016UserCardCustomize paramBean);

	/**
	 * 获取用户元宵节猜灯谜列表
	 * @param paramMap
	 * @return
	 */
	List<Newyear2016UserYuanXiaoCustomize> selectUserYuanXiaoList(Newyear2016UserYuanXiaoCustomize paramBean);
	
	/**
	 * 获取用户元宵节猜灯谜总数量
	 * @param paramMap
	 * @return
	 */
	Integer selectUserYuanXiaoTotal(Newyear2016UserYuanXiaoCustomize paramBean);
}
