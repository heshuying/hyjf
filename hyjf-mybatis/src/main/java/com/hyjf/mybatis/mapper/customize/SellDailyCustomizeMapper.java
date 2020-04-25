package com.hyjf.mybatis.mapper.customize;

import com.hyjf.mybatis.model.auto.SellDaily;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author xiasq
 * @version SellDailyCustomizeMapper, v0.1 2018/7/25 9:16
 */
public interface SellDailyCustomizeMapper {
	/**
	 * 查询运营中心合计
	 * 
	 * @param dateStr
	 * @return
	 */
	SellDaily selectOCSum(@Param("dateStr") String dateStr);

	/**
	 * 查询总合计
	 * 
	 * @param dateStr
	 * @return
	 */
	SellDaily selectAllSum(@Param("dateStr") String dateStr);

	/**
	 * 查询一级分部合计
	 * 
	 * @param formatDateStr
	 * @param drawOrder
	 * @return
	 */
	SellDaily selectPrimaryDivisionSum(@Param("dateStr") String dateStr, @Param("drawOrder") int drawOrder);

	/**
	 * 查询当日待还（工作日计算当天， 如果工作日次日是节假日，那么计算当天到节假日过后第一个工作日）
	 * 
	 * @param startTime
	 * @param endTime
	 * @param type
	 *            1-查询所有部门 2-上海运营中心-网络运营部 3-查询app推广
	 * @return
	 */
	List<SellDaily> countNoneRepayToday(@Param("startTime") Date startTime, @Param("endTime") Date endTime,
			@Param("type") Integer type);

	/**
	 * 查询本月份规模业绩
	 * 
	 * @param startTime
	 * @param endTime
	 * @param type
	 *            1-查询所有部门 2-上海运营中心-网络运营部 3-查询app推广
	 * @return
	 */
	List<SellDaily> countTotalInvestOnMonth(@Param("startTime") Date startTime, @Param("endTime") Date endTime,
			@Param("type") Integer type);

	/**
	 * 查询上月份规模业绩
	 * 
	 * @param startTime
	 * @param endTime
	 * @param type
	 *            1-查询所有部门 2-上海运营中心-网络运营部 3-查询app推广
	 * @return
	 */
	List<SellDaily> countTotalInvestOnPreviousMonth(@Param("startTime") Date startTime, @Param("endTime") Date endTime,
			@Param("type") Integer type);

	/**
	 * 查询本月累计已还款
	 * 
	 * @param startTime
	 * @param endTime
	 * @param type
	 *            1-查询所有部门 2-上海运营中心-网络运营部 3-查询app推广
	 * @return
	 */
	List<SellDaily> countTotalRepayOnMonth(@Param("startTime") Date startTime, @Param("endTime") Date endTime,
			@Param("type") Integer type);

	/**
	 * 本月累计提现
	 * 
	 * @param startTime
	 * @param endTime
	 * @param type
	 *            1-查询所有部门 2-上海运营中心-网络运营部 3-查询app推广
	 * @return
	 */
	List<SellDaily> countTotalWithdrawOnMonth(@Param("startTime") Date startTime, @Param("endTime") Date endTime,
			@Param("type") Integer type);

	/**
	 * 本月累计充值
	 * 
	 * @param startTime
	 * @param endTime
	 * @param type
	 *            1-查询所有部门 2-上海运营中心-网络运营部 3-查询app推广
	 * @return
	 */
	List<SellDaily> countTotalRechargeOnMonth(@Param("startTime") Date startTime, @Param("endTime") Date endTime,
			@Param("type") Integer type);

	/**
	 * 本月累计年化业绩
	 * 
	 * @param startTime
	 * @param endTime
	 * @param type
	 *            1-查询所有部门 2-上海运营中心-网络运营部 3-查询app推广
	 * @return
	 */
	List<SellDaily> countTotalAnnualInvestOnMonth(@Param("startTime") Date startTime, @Param("endTime") Date endTime,
			@Param("type") Integer type);

	/**
	 * 上月累计年化业绩
	 * 
	 * @param startTime
	 * @param endTime
	 * @param type
	 *            1-查询所有部门 2-上海运营中心-网络运营部 3-查询app推广
	 * @return
	 */
	List<SellDaily> countTotalAnnualInvestOnPreviousMonth(@Param("startTime") Date startTime,
			@Param("endTime") Date endTime, @Param("type") Integer type);

	/**
	 * 昨日规模业绩
	 * 
	 * @param startTime
	 * @param endTime
	 * @param type
	 *            1-查询所有部门 2-上海运营中心-网络运营部 3-查询app推广
	 * @return
	 */
	List<SellDaily> countTotalTenderYesterday(@Param("startTime") Date startTime, @Param("endTime") Date endTime,
			@Param("type") Integer type);

	/**
	 * 昨日已还款
	 * 
	 * @param startTime
	 * @param endTime
	 * @param type
	 *            1-查询所有部门 2-上海运营中心-网络运营部 3-查询app推广
	 * @return
	 */
	List<SellDaily> countTotalRepayYesterday(@Param("startTime") Date startTime, @Param("endTime") Date endTime,
			@Param("type") Integer type);

	/**
	 * 昨日年化业绩
	 * 
	 * @param startTime
	 * @param endTime
	 * @param type
	 *            1-查询所有部门 2-上海运营中心-网络运营部 3-查询app推广
	 * @return
	 */
	List<SellDaily> countTotalAnnualInvestYesterday(@Param("startTime") Date startTime, @Param("endTime") Date endTime,
			@Param("type") Integer type);

	/**
	 * 昨日充值
	 * 
	 * @param startTime
	 * @param endTime
	 * @param type
	 *            1-查询所有部门 2-上海运营中心-网络运营部 3-查询app推广
	 * @return
	 */
	List<SellDaily> countTotalRechargeYesterday(@Param("startTime") Date startTime, @Param("endTime") Date endTime,
			@Param("type") Integer type);

	/**
	 * 昨日提现
	 * 
	 * @param startTime
	 * @param endTime
	 * @param type
	 *            1-查询所有部门 2-上海运营中心-网络运营部 3-查询app推广
	 * @return
	 */
	List<SellDaily> countTotalWithdrawYesterday(@Param("startTime") Date startTime, @Param("endTime") Date endTime,
			@Param("type") Integer type);

	/**
	 * 计算第四、六、十列速率,第十六列净资金流
	 */
	void calculateRate();

	/**
	 * 批量插入
	 * 
	 * @param currentMonthTotalTenderList
	 */
	void batchInsert(List<SellDaily> currentMonthTotalTenderList);

	/**
	 * 批量更新
	 * 
	 * @param currentMonthTotalTenderList
	 */
	void batchUpdate(List<SellDaily> currentMonthTotalTenderList);

	/**
	 * 根据一级部门查询二级部门
	 * 
	 * @param primaryDivision
	 * @return
	 */
	List<String> selectTwoDivisionByPrimaryDivision(String primaryDivision);

	List<SellDaily> countRegisterTotalYesterday(@Param("startTime") Date startTime, @Param("endTime") Date endTime,
			@Param("type") Integer type);

	List<SellDaily> countRechargeGt3000UserNum(@Param("startTime") Date startTime, @Param("endTime") Date endTime,
			@Param("type") Integer type);

	List<SellDaily> countInvestGt3000UserNum(@Param("startTime") Date startTime, @Param("endTime") Date endTime,
			@Param("type") Integer type);

	List<SellDaily> countInvestGt3000MonthUserNum(@Param("startTime") Date startTime, @Param("endTime") Date endTime,
			@Param("type") Integer type);
}
