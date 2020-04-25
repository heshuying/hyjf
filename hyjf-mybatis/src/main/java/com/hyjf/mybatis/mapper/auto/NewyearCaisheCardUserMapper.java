package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.NewyearCaisheCardUser;
import com.hyjf.mybatis.model.auto.NewyearCaisheCardUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface NewyearCaisheCardUserMapper {
    int countByExample(NewyearCaisheCardUserExample example);

    int deleteByExample(NewyearCaisheCardUserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(NewyearCaisheCardUser record);

    int insertSelective(NewyearCaisheCardUser record);

    List<NewyearCaisheCardUser> selectByExample(NewyearCaisheCardUserExample example);

    NewyearCaisheCardUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") NewyearCaisheCardUser record, @Param("example") NewyearCaisheCardUserExample example);

    int updateByExample(@Param("record") NewyearCaisheCardUser record, @Param("example") NewyearCaisheCardUserExample example);

    int updateByPrimaryKeySelective(NewyearCaisheCardUser record);

    int updateByPrimaryKey(NewyearCaisheCardUser record);
}