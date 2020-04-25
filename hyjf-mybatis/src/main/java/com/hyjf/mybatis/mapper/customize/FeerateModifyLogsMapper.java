package com.hyjf.mybatis.mapper.customize;

import com.hyjf.mybatis.model.customize.FeerateModifyLog;
import com.hyjf.mybatis.model.auto.FeerateModifyLogExample;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface FeerateModifyLogsMapper {
    /**
     * 获取列表
     * @param example
     * @return
     */
    List<FeerateModifyLog> selectByExample(FeerateModifyLogExample example);
    
	int countByExample(FeerateModifyLogExample example);

    FeerateModifyLog selectByPrimaryKey(Integer id);
    
    /**
	 * COUNT
	 * 
	 * @param map
	 * @return
	 */
	Integer countAssetList(Map<String, Object> conditionMap);
	/**
	 * 获取列表
	 * 
	 * @param map
	 * @return
	 */
	List<FeerateModifyLog> selectAssetListList(Map<String, Object> conditionMap);

}