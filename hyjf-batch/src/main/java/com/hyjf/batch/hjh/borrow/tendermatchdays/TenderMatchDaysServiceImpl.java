package com.hyjf.batch.hjh.borrow.tendermatchdays;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.HjhAccede;
import com.hyjf.mybatis.model.customize.HjhAccedeCustomize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 计算自动出借的匹配期(每日)
 * @author liubin
 * 汇计划三期
 */
@Service
public class TenderMatchDaysServiceImpl extends BaseServiceImpl implements TenderMatchDaysService {
	
	Logger _log = LoggerFactory.getLogger(TenderMatchDaysServiceImpl.class);

	@Override
	public Boolean updateMatchDays() {
		// 更新计算匹配期
		/**
		 * 1,先更新正常数据, order_status 状态为 0,1,2 的数据
		 * 2,读取 order_status 状态为 80,81,82,90,91,92 并且 count_interest_time 开始计息时间为空的数据进行更新.
		 */
		Boolean firstUpdate = this.batchHjhAccedeCustomizeMapper.updateMatchDates() >= 0 ? true : false;

		// 获取发生异常且开始计息时间为空的的数据并更新其匹配期 add by huanghui start
		// 投资异常
		this.batchHjhAccedeCustomizeMapper.updateMatchDatesTwo(80, 82);

		// 复投异常
		this.batchHjhAccedeCustomizeMapper.updateMatchDatesTwo(90, 92);

		// 获取发生异常且开始计息时间为空的的数据并更新其匹配期 add by huanghui end
		return firstUpdate;
	}
}