package com.hyjf.admin.manager.config.poundageledger;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.poundageledger.PoundageLedgerCustomize;

@Service
public class PoundageLedgerServiceImpl extends BaseServiceImpl implements PoundageLedgerService {
	/**
	 * 查询数量
	 * 
	 * @param poundageLedgerCustomize
	 * @return
	 */
	@Override
	public Integer getPoundageLedgerCount(PoundageLedgerCustomize poundageLedgerCustomize) {
		Integer count = this.poundageLedgerCustomizeMapper.getPoundageLedgerCount(poundageLedgerCustomize);
		return count;
	}

	@Override
	public Integer getPoundageLedgerExSelfCount(PoundageLedgerCustomize poundageLedgerCustomize) {
		Integer count = this.poundageLedgerCustomizeMapper.getPoundageLedgerExSelfCount(poundageLedgerCustomize);
		return count;
	}

	/**
	 * 查询信息
	 * 
	 * @param poundageLedgerCustomize
	 * @return
	 */
	@Override
	public List<PoundageLedgerCustomize> getPoundageLedgerList(PoundageLedgerCustomize poundageLedgerCustomize) {
		List<PoundageLedgerCustomize> list = this.poundageLedgerCustomizeMapper.getPoundageLedgerList(poundageLedgerCustomize);
		return list;
	}
	
	/**
     * 新增信息
     * @param poundageLedgerCustomize
     * @return
     */
    public void insertPoundageLedger(PoundageLedgerCustomize poundageLedgerCustomize){
        this.poundageLedgerCustomizeMapper.insertPoundageLedger(poundageLedgerCustomize);
    }
    /**
     * 修改信息
     * @param poundageLedgerCustomize
     * @return
     */
    public void updatePoundageLedger(PoundageLedgerCustomize poundageLedgerCustomize){
        this.poundageLedgerCustomizeMapper.updatePoundageLedger(poundageLedgerCustomize);
    }
    /**
     * 删除信息
     * @param id
     * @return
     */
    public void deletePoundageLedger(int id){
        this.poundageLedgerCustomizeMapper.deletePoundageLedger(id);
    }
    /**
     * 获取单条信息
     * @param id
     * @return
     */
    public PoundageLedgerCustomize getPoundageLedgerById(int id){
        return this.poundageLedgerCustomizeMapper.getPoundageLedgerById(id);
    }

}
