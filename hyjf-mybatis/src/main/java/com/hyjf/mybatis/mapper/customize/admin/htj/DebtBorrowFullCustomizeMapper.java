package com.hyjf.mybatis.mapper.customize.admin.htj;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hyjf.mybatis.model.auto.DebtBorrowWithBLOBs;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowFullCustomize;

public interface DebtBorrowFullCustomizeMapper {

	/**
	 * 复审记录 总数COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	Long countBorrowFull(DebtBorrowCommonCustomize debtBorrowCommonCustomize);

	/**
	 * 复审记录
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	List<DebtBorrowFullCustomize> selectBorrowFullList(DebtBorrowCommonCustomize debtBorrowCommonCustomize);

	/**
	 * 复审中的列表
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	List<DebtBorrowFullCustomize> selectFullList(@Param("borrowNid") String borrowNid, @Param("limitStart") int limitStart, @Param("limitEnd") int limitEnd);

	/**
	 * 复审详细
	 * 
	 * @param borrowFullCustomize
	 * @return
	 */
	DebtBorrowFullCustomize selectFullInfo(@Param("borrowNid") String borrowNid);

	/**
	 * 合计
	 * 
	 * @param borrowFullCustomize
	 * @return
	 */
	DebtBorrowFullCustomize sumAmount(@Param("borrowNid") String borrowNid);

	/**
	 * 获取自动复审的记录
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	List<DebtBorrowWithBLOBs> selectAutoFullList(DebtBorrowCommonCustomize debtBorrowCommonCustomize);

	/**
	 * 
	 * @method: selectFeeMapByParams
	 * @description: 查看新的管理费率和收益差率
	 * 
	 * @param params
	 *            包括:
	 * @param borrowPeriod
	 *            项目期限
	 * @param borrowStyle
	 *            类型 例如 endday
	 * @param projectType
	 *            项目类型
	 * @return
	 * @return: Map<String,Object>
	 * @mender: zhouxiaoshuai
	 * @date: 2016年6月20日 下午1:16:41
	 */
	Map<String, Object> selectFeeMapByParams(Map<String, Object> params);
	
	/**
	 * 根据计划编号检索是否有正在清算中的计划
	 * @param params
	 * @return
	 */
	Integer countDebtInvestListByBorrowNid(Map<String,Object> params);

}