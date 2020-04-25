package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.ActJanBargain;
import com.hyjf.mybatis.model.auto.ActJanBargainExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ActJanBargainMapper {
    int countByExample(ActJanBargainExample example);

    int deleteByExample(ActJanBargainExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ActJanBargain record);

    int insertSelective(ActJanBargain record);

    List<ActJanBargain> selectByExample(ActJanBargainExample example);

    ActJanBargain selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ActJanBargain record, @Param("example") ActJanBargainExample example);

    int updateByExample(@Param("record") ActJanBargain record, @Param("example") ActJanBargainExample example);

    int updateByPrimaryKeySelective(ActJanBargain record);

    int updateByPrimaryKey(ActJanBargain record);
}