/**
 * Description:用户信息管理业务处理类接口实现
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 *           Created at: 2015年11月11日 上午11:05:56
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.admin.manager.asset.assetlist;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.AssetDetailCustomize;
import com.hyjf.mybatis.model.customize.AssetListCustomize;

/**
 * @author liubin
 */
@Service
public class AssetListServiceImpl extends BaseServiceImpl implements AssetListService {

	/**
	 * 获取权限列表
	 * 
	 * @return
	 */
	public List<AssetListCustomize> getRecordList(Map<String, Object> conditionMap, int limitStart, int limitEnd) {
		if (limitStart == 0 || limitStart > 0) {
			conditionMap.put("limitStart", limitStart);
		}
		if (limitEnd > 0) {
			conditionMap.put("limitEnd", limitEnd);
		}
		List<AssetListCustomize> list = assetListCustomizeMapper.selectAssetListList(conditionMap);
		return list;
	}

	/**
	 * 获取记录数
	 * @param form
	 * @return
	 * @author LiuBin
	 */
	@Override
	public Integer getRecordCount(Map<String, Object> conditionMap) {
		return assetListCustomizeMapper.countAssetList(conditionMap);
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param assetId
	 * @return
	 * @author LiuBin
	 */
	@Override
	public AssetDetailCustomize getDetailById(String assetId, String instCode) {
		return assetListCustomizeMapper.selectAssetDetail(assetId, instCode);
	}
	
	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param conditionMap
	 * @return
	 * @author PC-LIUSHOUYI
	 */
		
	@Override
	public BigDecimal sumAccount(Map<String, Object> conditionMap) {
		return assetListCustomizeMapper.sumAccount(conditionMap);
	}

}
