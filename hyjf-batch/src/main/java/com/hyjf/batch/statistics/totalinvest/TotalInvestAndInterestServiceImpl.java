package com.hyjf.batch.statistics.totalinvest;

import com.hyjf.common.util.CustomConstants;
import com.hyjf.mongo.operationreport.dao.TotalInvestAndInterestMongoDao;
import com.hyjf.mongo.operationreport.entity.TotalInvestAndInterestEntity;
import com.hyjf.mybatis.mapper.customize.OperationReportCustomizeMapper;
import com.hyjf.mybatis.mapper.customize.web.hjh.HjhPlanCustomizeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author xiasq
 * @version TotalInvestAndInterestServiceImpl, v0.1 2018/5/16 10:18
 */
@Service
public class TotalInvestAndInterestServiceImpl implements TotalInvestAndInterestService {
	Logger _log = LoggerFactory.getLogger(TotalInvestAndInterestServiceImpl.class);
	@Autowired
	private TotalInvestAndInterestMongoDao totalInvestAndInterestMongoDao;

	@Autowired
	private OperationReportCustomizeMapper operationReportCustomizeMapper;

	@Autowired
	private HjhPlanCustomizeMapper hjhPlanCustomizeMapper;

	@Override
	public void execute() throws Exception {
		// 累计交易笔数(实时)
		int totalInvestNum = this.countTotalInvestNum();

		// 累计交易总额(实时)
		BigDecimal totalInvestAmount = this.countTotalInvestAmount();

		// 累计为用户赚取收益(实时)
		BigDecimal totalInterestAmount = this.countTotalInterestAmount();

		TotalInvestAndInterestEntity entity = totalInvestAndInterestMongoDao.findOne(new Query());
		// 第一次插入
		if (entity == null) {
			entity = new TotalInvestAndInterestEntity();
		}
		entity.setTotalInterestAmount(totalInterestAmount);
		entity.setTotalInvestAmount(totalInvestAmount);
		entity.setTotalInvestNum(totalInvestNum);
		List<Map<String, Object>> list = hjhPlanCustomizeMapper.searchPlanStatisticData();
		if (!CollectionUtils.isEmpty(list)) {
			Map<String, Object> map = list.get(0);
			BigDecimal interestTotal = (BigDecimal) map.get("interest_total");
			BigDecimal accountTotal = (BigDecimal) map.get("accede_account_total");
			Long accedeTimes = (Long) map.get("accede_times");
			entity.setHjhTotalInterestAmount(interestTotal);
			entity.setHjhTotalInvestAmount(accountTotal);
			//entity.setTotalInvestNum(accedeTimes.intValue());
			entity.setHjhTotalInvestNum(accedeTimes.intValue());
		}
		_log.info("待更新数据: {}", entity);
        // save没有插入，有则更新
		totalInvestAndInterestMongoDao.save(entity);
	}

	@Override
	public int countTotalInvestNum() {
		return operationReportCustomizeMapper.getTradeCount() + CustomConstants.HTJ_HTL_COUNT;
	}

	@Override
	public BigDecimal countTotalInterestAmount() {
		return operationReportCustomizeMapper.getTotalInterest();
	}

	@Override
	public BigDecimal countTotalInvestAmount() {
		return operationReportCustomizeMapper.getTotalCount();
	}

	@Override
	public List<Map<String, Object>> searchPlanStatisticData() {
		return hjhPlanCustomizeMapper.searchPlanStatisticData();
	}
}
