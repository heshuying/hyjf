package com.hyjf.mybatis.mapper.customize.batch;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hyjf.mybatis.model.auto.DebtBorrow;

/**
 * 定时发标
 * 
 * @author HBZ
 */
public interface BatchDebtOntimeBorrowCustomizeMapper {

	/**
	 * 查询符合条件的定时投标 列表
	 * 
	 * @param ontime
	 * @return
	 */
	public List<DebtBorrow> queryOntimeTenderList(@Param("ontime") Integer ontime);

	/**
	 * 
	 * @param tplname
	 * @return
	 */
	public String queryMobiles(@Param("tplname") String tplname);

	/**
	 * 查询相应的定时预约开始标的
	 * 
	 * @param onTime
	 * @return
	 */

	public List<DebtBorrow> selectBorrowAppointStart(@Param("ontime") Integer ontime);

	/**
	 * 查询相应的定时预约结束标的
	 * 
	 * @param onTime
	 * @return
	 */

	public List<DebtBorrow> selectBorrowAppointEnd(@Param("ontime") Integer ontime);

}
