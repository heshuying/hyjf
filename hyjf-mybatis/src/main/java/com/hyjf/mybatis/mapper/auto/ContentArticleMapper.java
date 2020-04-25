package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.ContentArticle;
import com.hyjf.mybatis.model.auto.ContentArticleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ContentArticleMapper {
    int countByExample(ContentArticleExample example);

    int deleteByExample(ContentArticleExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ContentArticle record);

    int insertSelective(ContentArticle record);

    List<ContentArticle> selectByExampleWithBLOBs(ContentArticleExample example);

    List<ContentArticle> selectByExample(ContentArticleExample example);

    ContentArticle selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ContentArticle record, @Param("example") ContentArticleExample example);

    int updateByExampleWithBLOBs(@Param("record") ContentArticle record, @Param("example") ContentArticleExample example);

    int updateByExample(@Param("record") ContentArticle record, @Param("example") ContentArticleExample example);

    int updateByPrimaryKeySelective(ContentArticle record);

    int updateByPrimaryKeyWithBLOBs(ContentArticle record);

    int updateByPrimaryKey(ContentArticle record);
}