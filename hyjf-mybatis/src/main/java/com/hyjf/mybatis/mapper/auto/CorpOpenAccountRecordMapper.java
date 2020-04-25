package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.CorpOpenAccountRecord;
import com.hyjf.mybatis.model.auto.CorpOpenAccountRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CorpOpenAccountRecordMapper {
    int countByExample(CorpOpenAccountRecordExample example);

    int deleteByExample(CorpOpenAccountRecordExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CorpOpenAccountRecord record);

    int insertSelective(CorpOpenAccountRecord record);

    List<CorpOpenAccountRecord> selectByExample(CorpOpenAccountRecordExample example);

    CorpOpenAccountRecord selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CorpOpenAccountRecord record, @Param("example") CorpOpenAccountRecordExample example);

    int updateByExample(@Param("record") CorpOpenAccountRecord record, @Param("example") CorpOpenAccountRecordExample example);

    int updateByPrimaryKeySelective(CorpOpenAccountRecord record);

    int updateByPrimaryKey(CorpOpenAccountRecord record);
}