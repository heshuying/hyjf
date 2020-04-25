package com.hyjf.mybatis.mapper.customize.poundageledger;

import org.apache.ibatis.annotations.Param;

import java.util.List;

import com.hyjf.mybatis.model.customize.poundageledger.PoundageExceptionCustomize;

/**
 * com.hyjf.mybatis.mapper.customize.poundage
 *
 * @author wgx
 * @date 2017/12/15
 */
public interface PoundageExceptionCustomizeMapper {

    public void insertPoundageException(PoundageExceptionCustomize entity);

    public void updatePoundageException(PoundageExceptionCustomize entity);

    public void updatePoundageExceptionStatus(PoundageExceptionCustomize entity);

    public void deletePoundageException(int id);

    public PoundageExceptionCustomize getPoundageExceptionById(int id);

    public Integer getPoundageExceptionCount(PoundageExceptionCustomize entity);

    public List<PoundageExceptionCustomize> getPoundageExceptionList(PoundageExceptionCustomize entity);
}
