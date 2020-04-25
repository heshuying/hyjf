package com.hyjf.mybatis.mapper.customize.poundageledger;

import java.util.List;

import com.hyjf.mybatis.model.customize.poundageledger.PoundageLedgerCustomize;

public interface PoundageLedgerCustomizeMapper {
    
	public void insertPoundageLedger(PoundageLedgerCustomize entity);

    public void updatePoundageLedger(PoundageLedgerCustomize entity);

    public void deletePoundageLedger(int id);

    public PoundageLedgerCustomize getPoundageLedgerById(int id);

    public Integer getPoundageLedgerCount(PoundageLedgerCustomize entity);

    public Integer getPoundageLedgerExSelfCount (PoundageLedgerCustomize entity);

    public List<PoundageLedgerCustomize>  getPoundageLedgerList(PoundageLedgerCustomize entity);

    public List<PoundageLedgerCustomize>  getPoundageLedgerPage(PoundageLedgerCustomize entity);
}
