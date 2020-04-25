package com.hyjf.batch.borrow.splitTender;

import java.util.List;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowBail;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;

public interface OntimeSplitTenderService  extends BaseService {

	/**
	 * 查询符合条件的定时投标 数量
	 * @param ontime
	 * @return
	 */
	public Integer queryOntimeTenderCount(Integer ontime);
	/**
	 * 查询符合条件的定时投标 列表
	 * @param ontime
	 * @return
	 */
	public List<Borrow> queryOntimeTenderList(Integer ontime);
	/**
	 * 修改投标信息
	 * @param record
	 * @return
	 */
	int updateByPrimaryKey(Borrow record);

	/**
	 * 修改投标信息
	 * @param record
	 * @return
	 */
	public int updateByPrimaryKeySelective(BorrowWithBLOBs record);
	
	/**
	 * 查询所有未复审的标
	 * @return
	 */
	public List<Borrow> queryAllunrecheckTenders();
	
	/**
	 * 查询分期发标的标的拆分总期数
	 * @param borrowPreNid
	 * @return
	 */
	public Integer querySplitTenderCount(Integer borrowPreNid);
	/**
	 * 根据borrowNid查询Borrow
	 * @param borrowNid
	 * @return
	 */
	public Borrow queryBorrowByNextNid(String borrowNid);
	/**
	 * 查询是否交纳保证金
	 * @param borrowNid
	 * @return
	 */
	public BorrowBail queryBorrowBailByNid(String borrowNid);
	/**
	 * 
	 * @param tplname
	 * @return
	 */
	public String queryMobiles(String tplname);
}

	