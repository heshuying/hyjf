package com.hyjf.mybatis.mapper.customize;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hyjf.mybatis.model.customize.MspConfigure;
import com.hyjf.mybatis.model.customize.MspConfigureExample;

public interface MspConfigureCustomizeMapper {
    int countByExample(MspConfigureExample example);

    int deleteByExample(MspConfigureExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MspConfigure record);

    int insertSelective(MspConfigure record);

    List<MspConfigure> selectByExample(MspConfigureExample example);
    
    List<MspConfigure> sourceNameIsExists(MspConfigure mspConfigure);

    MspConfigure selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MspConfigure record, @Param("example") MspConfigureExample example);

    int updateByExample(@Param("record") MspConfigure record, @Param("example") MspConfigureExample example);

    int updateByPrimaryKeySelective(MspConfigure record);

    int updateByPrimaryKey(MspConfigure record);
    //自定义开始
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
	List<MspConfigure> selectAssetListList(Map<String, Object> conditionMap);
	
	
}