package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hyjf.mybatis.model.auto.Borrow;

/**
 * 定时发标
 * 
 * @author HBZ
 */
public interface OntimeTenderCustomizeMapper {

	/**
	 * 查询符合条件的定时投标 数量
	 * 
	 * @param ontime
	 * @return
	 */
	public Integer queryOntimeTenderCount(@Param("ontime") Integer ontime);

	/**
	 * 查询符合条件的定时投标 列表
	 * 
	 * @param ontime
	 * @return
	 */
	public List<Borrow> queryOntimeTenderList(@Param("ontime") Integer ontime);

	/**
	 * 查询符合条件的定时汇计划投标 列表
	 * 
	 * @param ontime
	 * @return
	 */
	public List<Borrow> queryHjhOntimeTenderList(@Param("ontime") Integer ontime);
	
	/**
	 * 查询所有未复审的标
	 * 
	 * @return
	 */
	public List<Borrow> queryAllunrecheckTenders();

	/**
	 * 查询分期发标的标的拆分总期数
	 * 
	 * @param borrowPreNid
	 * @return
	 */
	public Integer querySplitTenderCount(@Param("borrowPreNid") Integer borrowPreNid);

	/**
	 * 
	 * @param tplname
	 * @return
	 */
	public String queryMobiles(@Param("tplname") String tplname);

	/**
	 * 查询相应的定时预约开始标的
	 * @param onTime
	 * @return
	 */

	public List<Borrow> selectBorrowAppointStart(@Param("ontime") Integer ontime);

	/**
	 * 查询相应的定时预约结束标的
	 * @param onTime
	 * @return
	 */
		
	public List<Borrow> selectBorrowAppointEnd(@Param("ontime") Integer ontime);

	/**
	 * 查询标的是否达定时发标时间
	 * @param onTime
	 * @return
	 */
	public Integer queryOntimeIdByNid(@Param("borrowNid") String borrowNid, @Param("ontime") Integer ontime);
	
}
