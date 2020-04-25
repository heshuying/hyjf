package com.hyjf.mybatis.mapper.customize;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.BorrowFullCustomize;

public interface BorrowFullCustomizeMapper {

	/**
	 * 复审记录 总数COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	Long countBorrowFull(BorrowCommonCustomize borrowCommonCustomize);

	/**
	 * 复审记录
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	List<BorrowFullCustomize> selectBorrowFullList(BorrowCommonCustomize borrowCommonCustomize);

	/**
	 * 复审中的列表
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	List<BorrowFullCustomize> selectFullList(@Param("borrowNid") String borrowNid, @Param("limitStart") int limitStart, @Param("limitEnd") int limitEnd);

	/**
	 * 复审详细
	 * 
	 * @param borrowFullCustomize
	 * @return
	 */
	BorrowFullCustomize selectFullInfo(@Param("borrowNid") String borrowNid);

	/**
	 * 合计
	 * 
	 * @param borrowFullCustomize
	 * @return
	 */
	BorrowFullCustomize sumAmount(@Param("borrowNid") String borrowNid);

	/**
	 * 获取自动复审的记录
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	List<BorrowWithBLOBs> selectAutoFullList(BorrowCommonCustomize borrowCommonCustomize);
    /**
     * 
     * @method: selectFeeMapByParams
     * @description: 	查看新的管理费率和收益差率	
     * 	
     *  @param params  包括:
     *  @param borrowPeriod  项目期限
     *  @param borrowStyle 类型 例如 endday 
     *  @param projectType 项目类型
     *  @return 
     * @return: Map<String,Object>
    * @mender: zhouxiaoshuai
     * @date:   2016年6月20日 下午1:16:41
     */
    Map<String, Object> selectFeeMapByParams(Map<String , Object> params);

	/**
	 * 检索项目的服务费率
	 * @param params
	 * @return
	 */
	String selectServiceRateByParams(Map<String, Object> params);

	/**
	 * 检索项目的管理费率
	 * @param params
	 * @return
	 */
	String selectManChargeRateByParams(Map<String, Object> params);

	/**
	 * 检索项目的收益差率
	 * @param params
	 * @return
	 */
	String selectReturnRateByParams(Map<String, Object> params);

	/**
	 * 取得金额合计
	 * @param borrowCommonCustomize
	 * @return
	 */
		
	BorrowFullCustomize sumAccount(BorrowCommonCustomize borrowCommonCustomize);
	
	
	/**
	 * 检索项目的逾期费率
	 * @param params
	 * @return
	 */
	String selectLateInterestRateByParams(Map<String, Object> params);
	
	/**
	 * 检索项目的逾期天数
	 * @param params
	 * @return
	 */
	String selectLateFreeDaysByParams(Map<String, Object> params);
}