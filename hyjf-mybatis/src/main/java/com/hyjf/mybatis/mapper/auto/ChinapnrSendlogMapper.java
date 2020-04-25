package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.ChinapnrSendlog;
import com.hyjf.mybatis.model.auto.ChinapnrSendlogExample;
import com.hyjf.mybatis.model.auto.ChinapnrSendlogWithBLOBs;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ChinapnrSendlogMapper {
    int countByExample(ChinapnrSendlogExample example);

    int deleteByExample(ChinapnrSendlogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ChinapnrSendlogWithBLOBs record);

    int insertSelective(ChinapnrSendlogWithBLOBs record);

    List<ChinapnrSendlogWithBLOBs> selectByExampleWithBLOBs(ChinapnrSendlogExample example);

    List<ChinapnrSendlog> selectByExample(ChinapnrSendlogExample example);

    ChinapnrSendlogWithBLOBs selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ChinapnrSendlogWithBLOBs record, @Param("example") ChinapnrSendlogExample example);

    int updateByExampleWithBLOBs(@Param("record") ChinapnrSendlogWithBLOBs record, @Param("example") ChinapnrSendlogExample example);

    int updateByExample(@Param("record") ChinapnrSendlog record, @Param("example") ChinapnrSendlogExample example);

    int updateByPrimaryKeySelective(ChinapnrSendlogWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(ChinapnrSendlogWithBLOBs record);

    int updateByPrimaryKey(ChinapnrSendlog record);
}