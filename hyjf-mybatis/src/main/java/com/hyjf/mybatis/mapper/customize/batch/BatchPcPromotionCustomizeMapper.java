package com.hyjf.mybatis.mapper.customize.batch;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.batch.BatchPcPromotionCustomize;

/**
 * PC渠道统计明细更新Mapper
 * 
 * @author liuyang
 *
 */
public interface BatchPcPromotionCustomizeMapper {

	/**
	 * 根据用户Id查询用户首投信息
	 * 
	 * @param param
	 * @return
	 */
	public List<BatchPcPromotionCustomize> selectPcPromotionCustomizeList(Map<String, Object> param);

	/**
	 * 根据用户Id查询App用户首投信息
	 * 
	 * @param param
	 * @return
	 */
	public List<BatchPcPromotionCustomize> selectAppPromotionCustomizeList(Map<String, Object> param);

}
