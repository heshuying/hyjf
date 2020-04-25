package com.hyjf.batch.pc.repairstatistics;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.PcChannelStatistics;
import com.hyjf.mybatis.model.customize.admin.PcChannelStatisticsCustomize;

/**
 * Pc渠道统计定时Service实现类
 * 
 * @author liuyang
 *
 */
@Service
public class PcChannelStatisticsRepairServiceImpl extends BaseServiceImpl implements PcChannelStatisticsRepairService {

	/**
	 * 检索当天PC统计数据
	 * 
	 * @param pcChannelStatisticsCustomize
	 * @return
	 */
	@Override
	public List<PcChannelStatisticsCustomize> selectPcChannelStatisticsList(PcChannelStatisticsCustomize pcChannelStatisticsCustomize) {
		return pcChannelStatisticsCustomizeMapper.selectPcChannelStatisticsList(pcChannelStatisticsCustomize);
	}

	/**
	 * 插入PC统计数据表
	 * 
	 * @param statisticsCustomize
	 * @param nowDate
	 * @return
	 */
	@Override
	public boolean insertRecord(PcChannelStatisticsCustomize statisticsCustomize, String nowDate) {
		PcChannelStatistics pcChannelStatistics = new PcChannelStatistics();
		// 渠道ID
		pcChannelStatistics.setSourceId(statisticsCustomize.getSourceId());
		// 渠道名称
		pcChannelStatistics.setSourceName(statisticsCustomize.getSourceName());
		// 访问数
		pcChannelStatistics.setAccessNumber(Integer.parseInt(statisticsCustomize.getAccessNumber().replace(",", "")));
		// 注册数
		pcChannelStatistics.setRegistNumber(Integer.parseInt(statisticsCustomize.getRegistNumber().replace(",", "")));
		// 开户数
		pcChannelStatistics.setOpenAccountNumber(Integer.parseInt(statisticsCustomize.getOpenAccountNumber().replace(",", "")));
		// 出借人数
		pcChannelStatistics.setTenderNumber(Integer.parseInt(statisticsCustomize.getTenderNumber().replace(",", "")));
		// 累计充值
		if (StringUtils.isNotEmpty(statisticsCustomize.getCumulativeRecharge())) {
			pcChannelStatistics.setCumulativeRecharge(new BigDecimal(statisticsCustomize.getCumulativeRecharge().replace(",", "")));
		} else {
			pcChannelStatistics.setCumulativeRecharge(BigDecimal.ZERO);
		}
		// 累计出借
		if (StringUtils.isNotEmpty(statisticsCustomize.getCumulativeInvestment())) {
			pcChannelStatistics.setCumulativeInvestment(new BigDecimal(statisticsCustomize.getCumulativeInvestment().replace(",", "")));
		} else {
			pcChannelStatistics.setCumulativeInvestment(BigDecimal.ZERO);
		}
		// 汇直投出借金额
		if (StringUtils.isNotEmpty(statisticsCustomize.getHztTenderPrice())) {
			pcChannelStatistics.setHztTenderPrice(new BigDecimal(statisticsCustomize.getHztTenderPrice().replace(",", "")));
		} else {
			pcChannelStatistics.setHztTenderPrice(BigDecimal.ZERO);
		}
		// 汇消费出借金额
		if (StringUtils.isNotEmpty(statisticsCustomize.getHxfTenderPrice())) {
			pcChannelStatistics.setHxfTenderPrice(new BigDecimal(statisticsCustomize.getHxfTenderPrice().replace(",", "")));
		} else {
			pcChannelStatistics.setHxfTenderPrice(BigDecimal.ZERO);
		}
		// 汇天利出借金额
		if (StringUtils.isNotEmpty(statisticsCustomize.getHtlTenderPrice())) {
			pcChannelStatistics.setHtlTenderPrice(new BigDecimal(statisticsCustomize.getHtlTenderPrice().replace(",", "")));
		} else {
			pcChannelStatistics.setHtlTenderPrice(BigDecimal.ZERO);
		}
		// 汇添金出借金额
		if (StringUtils.isNotEmpty(statisticsCustomize.getHtjTenderPrice())) {
			pcChannelStatistics.setHtjTenderPrice(new BigDecimal(statisticsCustomize.getHtjTenderPrice().replace(",", "")));
		} else {
			pcChannelStatistics.setHtjTenderPrice(BigDecimal.ZERO);
		}
		// 汇金理财出借金额
		if (StringUtils.isNotEmpty(statisticsCustomize.getRtbTenderPrice())) {
			pcChannelStatistics.setRtbTenderPrice(new BigDecimal(statisticsCustomize.getRtbTenderPrice().replace(",", "")));
		} else {
			pcChannelStatistics.setRtbTenderPrice(BigDecimal.ZERO);
		}
		// 汇转让出借金额
		if (StringUtils.isNotEmpty(statisticsCustomize.getHzrTenderPrice())) {
			pcChannelStatistics.setHzrTenderPrice(new BigDecimal(statisticsCustomize.getHzrTenderPrice().replace(",", "")));
		} else {
			pcChannelStatistics.setHzrTenderPrice(BigDecimal.ZERO);
		}
		// 插入时间
		pcChannelStatistics.setAddTime(GetDate.stringToDate2(nowDate));
		return pcChannelStatisticsMapper.insertSelective(pcChannelStatistics) > 0 ? true : false;
	}
}
