package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.ActJanPrizewinList;
import com.hyjf.mybatis.model.auto.ActJanPrizewinListExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ActJanPrizewinListMapper {
    int countByExample(ActJanPrizewinListExample example);

    int deleteByExample(ActJanPrizewinListExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ActJanPrizewinList record);

    int insertSelective(ActJanPrizewinList record);

    List<ActJanPrizewinList> selectByExample(ActJanPrizewinListExample example);

    ActJanPrizewinList selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ActJanPrizewinList record, @Param("example") ActJanPrizewinListExample example);

    int updateByExample(@Param("record") ActJanPrizewinList record, @Param("example") ActJanPrizewinListExample example);

    int updateByPrimaryKeySelective(ActJanPrizewinList record);

    int updateByPrimaryKey(ActJanPrizewinList record);
}