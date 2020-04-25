package com.hyjf.batch.data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.CalculateInvestInterest;
import com.hyjf.mybatis.model.auto.CalculateInvestInterestExample;
import com.hyjf.mybatis.model.customize.web.WebHomePageStatisticsCustomize;

/**
 * 平台数据
 *
 * @author Administrator
 *
 */
@Service
@Transactional(isolation = Isolation.REPEATABLE_READ)
public class DataInfoServiceImpl extends BaseServiceImpl implements DataInfoService {

	@Override
	public int insertDataInfo() {
		int resultCount=0;
		List<CalculateInvestInterest> calculates = this.calculateInvestInterestMapper.selectByExample(new CalculateInvestInterestExample());
		if(calculates != null && calculates.size() >0 ){
			//七天出借数据
			Map<String, Object> map7=dataCustomizeMapper.selectDataInfo("7");
			//融资期限分布
			Map<String, Object> mapPeriod=dataCustomizeMapper.selectPeriodInfo();
			//保证金统计
			WebHomePageStatisticsCustomize homeStatistics = webHomePageCustomizeMapper.countTotalStatistics();
			CalculateInvestInterest calculateNew = new CalculateInvestInterest();
			calculateNew.setId(calculates.get(0).getId());
			calculateNew.setSevenDayTenderSum(new BigDecimal(map7.get("tenderMoney")+""));
			calculateNew.setSevenDayInterestSum(new BigDecimal(map7.get("recoverInterest")+""));
			calculateNew.setBorrowZeroOne(Integer.parseInt(mapPeriod.get("zeroone")+""));
			calculateNew.setBorrowOneThree(Integer.parseInt(mapPeriod.get("onethree")+""));
			calculateNew.setBorrowThreeSix(Integer.parseInt(mapPeriod.get("threesex")+""));
			calculateNew.setBorrowSixTwelve(Integer.parseInt(mapPeriod.get("sextw")+""));
			calculateNew.setBorrowTwelveUp(Integer.parseInt(mapPeriod.get("tw")+""));
			//出借金额分布
			Map<String, Object> mapTenderMoney=dataCustomizeMapper.selectTendMoneyInfo();
			calculateNew.setInvestOneDown(Integer.parseInt(mapTenderMoney.get("zeroone")+""));
			calculateNew.setInvestOneFive(Integer.parseInt(mapTenderMoney.get("onefive")+""));
			calculateNew.setInvestFiveTen(Integer.parseInt(mapTenderMoney.get("fiveten")+""));
			calculateNew.setInvestTenFifth(Integer.parseInt(mapTenderMoney.get("tenfive")+""));
			calculateNew.setInvestFifthUp(Integer.parseInt(mapTenderMoney.get("five")+""));
			calculateNew.setUpdateTime(new Date());
			//保证金
			calculateNew.setBailMoney(new BigDecimal(homeStatistics.getBailTotal().replaceAll(",", "")));
			resultCount=this.webCalculateInvestInterestCustomizeMapper.updateCalculateInvestByPrimaryKey(calculateNew);
		}
		return resultCount;
	}

	@Override
	public void insertAYearTenderInfo() {
		dataCustomizeMapper.insertHyjfTenderMonthInfo();
	}

}
