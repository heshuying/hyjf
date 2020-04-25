package com.hyjf.mybatis.mapper.customize;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hyjf.mybatis.model.customize.SubCommissionListConfigCustomize;
import com.hyjf.mybatis.model.customize.SubCommissionListConfigCustomizeExample;

public interface SubCommissionListConfigCustomizeMapper {
    int countByExample(SubCommissionListConfigCustomizeExample example);

    int deleteByExample(SubCommissionListConfigCustomizeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SubCommissionListConfigCustomize record);

    int insertSelective(SubCommissionListConfigCustomize record);

    List<SubCommissionListConfigCustomize> selectByExample(SubCommissionListConfigCustomizeExample example);
    
    List<SubCommissionListConfigCustomize> selectByExampleUsername(SubCommissionListConfigCustomize subCommissionListConfigCustomize);

    SubCommissionListConfigCustomize selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SubCommissionListConfigCustomize record, @Param("example") SubCommissionListConfigCustomizeExample example);

    int updateByExample(@Param("record") SubCommissionListConfigCustomize record, @Param("example") SubCommissionListConfigCustomizeExample example);

    int updateByPrimaryKeySelective(SubCommissionListConfigCustomize record);

    int updateByPrimaryKey(SubCommissionListConfigCustomize record);
    /**
	 * 获取列表
	 * 
	 * @param conditionMap
	 * @return
	 */
	List<SubCommissionListConfigCustomize> getRecordList(Map<String, Object> conditionMap);
	/**
	 * 
	 * 
	 * @param conditionMap
	 * @return
	 */
	Integer getRecordCount(Map<String, Object> conditionMap);
	
	Map<String, Object> getUserInfo(String username);
	
	Map<String, Object> getUserIdInfo(Integer userid);
	
    List<SubCommissionListConfigCustomize> selectByNameExample(SubCommissionListConfigCustomizeExample example);

	List<SubCommissionListConfigCustomize> getSimpleList();
    
}