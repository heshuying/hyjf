package com.hyjf.mybatis.mapper.customize.poundageledger;

import java.util.List;

import com.hyjf.mybatis.model.customize.poundageledger.PoundageDetailCustomize;

public interface PoundageDetailCustomizeMapper {
    
	public void insertPoundageDetail(PoundageDetailCustomize entity);

    public void updatePoundageDetail(PoundageDetailCustomize entity);

    public void deletePoundageDetail(int id);

    public PoundageDetailCustomize getPoundageDetailById(int id);

    public Integer getPoundageDetailCount(PoundageDetailCustomize entity);

    public List<PoundageDetailCustomize>  getPoundageDetailList(PoundageDetailCustomize entity);

    public List<PoundageDetailCustomize>  getPoundageDetailPage(PoundageDetailCustomize entity);

    public void insertTimerPoundageDetailList();

    public void insertTimerPoundageList();
}
