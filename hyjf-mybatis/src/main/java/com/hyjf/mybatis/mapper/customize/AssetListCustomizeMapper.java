package com.hyjf.mybatis.mapper.customize;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hyjf.mybatis.model.customize.AssetDetailCustomize;
import com.hyjf.mybatis.model.customize.AssetListCustomize;

public interface AssetListCustomizeMapper {

	/**
	 * 获取列表
	 * 
	 * @param map
	 * @return
	 */
	List<AssetListCustomize> selectAssetListList(Map<String, Object> conditionMap);

	/**
	 * COUNT
	 * 
	 * @param map
	 * @return
	 */
	Integer countAssetList(Map<String, Object> conditionMap);
	
	/**
	 * 取得详细
	 * 
	 * @param map
	 * @return
	 */
	AssetDetailCustomize selectAssetDetail(@Param("assetId") String assetId, @Param("instCode") String instCode);
	
	/**
	 * 取得状态
	 * 
	 * @param map
	 * @return
	 */
	AssetDetailCustomize selectAssetStatus(@Param("assetId") String assetId, @Param("instCode") String instCode);
	
	/**
	 * 金额合计
	 * 
	 * @param conditionMap
	 * @return
	 */
	BigDecimal sumAccount(Map<String, Object> conditionMap);
}

