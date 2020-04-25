package com.hyjf.mybatis.mapper.customize.poundageledger;

import java.util.List;

import com.hyjf.mybatis.model.customize.poundageledger.PoundageCustomize;

public interface PoundageCustomizeMapper {

    public void updatePoundage(PoundageCustomize entity);

    public Integer getPoundageCount(PoundageCustomize entity);

    public List<PoundageCustomize>  getPoundageList(PoundageCustomize entity);

    public PoundageCustomize getPoundageById(int id);

    public PoundageCustomize getPoundageSum(PoundageCustomize entity);
}
