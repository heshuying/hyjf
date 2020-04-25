package com.hyjf.mybatis.mapper.customize.batch;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.auto.HjhAccede;
import com.hyjf.mybatis.model.customize.HjhAccedeCustomize;
import org.apache.ibatis.annotations.Param;

public interface BatchHjhAccedeCustomizeMapper {
	/**
	 * 取得汇计划自动出借的计划订单列表
	 * @Title updatePlanCredit
	 * @param plan
	 * @return
	 */
	List<HjhAccedeCustomize> selectHjhAutoTenderList(Map<String,Object> paramMap);

	/**
	 *
	 * 计划放款后计划订单冻结金额变化
	 * @param accede
	 * @return
	 */
	int updateOfPlanLoansTender(HjhAccede accede);

	/**
	 *
	 * 更新待收收益
	 * @param hjhAccede
	 * @return
	 */
	int updateInterest(HjhAccede hjhAccede);

	// add 汇计划三期 计算匹配期处理 liubin 20180515 start
	/**
	 * 更新计算匹配期
	 * @return
	 */
	int updateMatchDates();
	// add 汇计划三期 计算匹配期处理 liubin 20180515 end

	int updateMatchDatesTwo(@Param("start") Integer start, @Param("end") Integer end);
	// add 出让人没有缴费授权临时对应（不收取服务费） liubin 20181113 start
	int countNoAutoPayment(@Param("creditNid") String creditNid);
	// add 出让人没有缴费授权临时对应（不收取服务费） liubin 20181113 end


	List<HjhAccede> selectHjhAutoTenderAbnormalList(int start, int end);

	int updateMatchDatesTwo(int id);
}