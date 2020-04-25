package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.AppPushManage;
import com.hyjf.mybatis.model.auto.AppPushManageExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AppPushManageMapper {
    int countByExample(AppPushManageExample example);

    int deleteByExample(AppPushManageExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AppPushManage record);

    int insertSelective(AppPushManage record);

    List<AppPushManage> selectByExampleWithBLOBs(AppPushManageExample example);

    List<AppPushManage> selectByExample(AppPushManageExample example);

    AppPushManage selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AppPushManage record, @Param("example") AppPushManageExample example);

    int updateByExampleWithBLOBs(@Param("record") AppPushManage record, @Param("example") AppPushManageExample example);

    int updateByExample(@Param("record") AppPushManage record, @Param("example") AppPushManageExample example);

    int updateByPrimaryKeySelective(AppPushManage record);

    int updateByPrimaryKeyWithBLOBs(AppPushManage record);

    int updateByPrimaryKey(AppPushManage record);
}