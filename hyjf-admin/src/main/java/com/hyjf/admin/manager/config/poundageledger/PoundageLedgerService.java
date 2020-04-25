package com.hyjf.admin.manager.config.poundageledger;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.poundageledger.PoundageLedgerCustomize;

public interface PoundageLedgerService extends BaseService {
	/**
	 * 查询数量
	 * @param poundageLedgerCustomize
	 * @return
	 */
    public Integer getPoundageLedgerCount(PoundageLedgerCustomize poundageLedgerCustomize);

	/**
	 * 查询出借人分公司数量
	 * @param poundageLedgerCustomize
	 * @return
	 */
	public Integer getPoundageLedgerExSelfCount(PoundageLedgerCustomize poundageLedgerCustomize);
	/**
	 * 查询信息
	 * @param poundageLedgerCustomize
	 * @return
	 */
	public List<PoundageLedgerCustomize> getPoundageLedgerList(PoundageLedgerCustomize poundageLedgerCustomize);
	/**
     * 新增信息
     * @param poundageLedgerCustomize
     * @return
     */
	public void insertPoundageLedger(PoundageLedgerCustomize poundageLedgerCustomize);
	/**
     * 修改信息
     * @param poundageLedgerCustomize
     * @return
     */
	public void updatePoundageLedger(PoundageLedgerCustomize poundageLedgerCustomize);
	/**
     * 删除信息
     * @param id
     * @return
     */
    public void deletePoundageLedger(int id);
    /**
     * 获取单条信息
     * @param id
     * @return
     */
    public PoundageLedgerCustomize getPoundageLedgerById(int id);
	
}
