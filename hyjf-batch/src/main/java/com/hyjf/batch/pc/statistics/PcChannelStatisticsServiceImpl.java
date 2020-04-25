package com.hyjf.batch.pc.statistics;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.PcChannelStatistics;
import com.hyjf.mybatis.model.auto.UtmPlat;
import com.hyjf.mybatis.model.auto.UtmPlatExample;
import com.hyjf.mybatis.model.customize.admin.PcChannelStatisticsCustomize;

/**
 * Pc渠道统计定时Service实现类
 * 
 * @author liuyang
 *
 */
@Service
public class PcChannelStatisticsServiceImpl extends BaseServiceImpl implements PcChannelStatisticsService {

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
	 * @return
	 */
	@Override
	public boolean insertRecord(PcChannelStatisticsCustomize statisticsCustomize, String nowDate, UtmPlat utmPlat) {

		// 访问数
		PcChannelStatisticsCustomize visitCustomizeBean = this.pcChannelStatisticsCustomizeMapper.selectVisit(statisticsCustomize);
		// 注册数
		PcChannelStatisticsCustomize registNumberBean = this.pcChannelStatisticsCustomizeMapper.selectRegistNumber(statisticsCustomize);
		// 开户数
		PcChannelStatisticsCustomize accountNumberBean = this.pcChannelStatisticsCustomizeMapper.selectAccountNumber(statisticsCustomize);
		// 出借数
		PcChannelStatisticsCustomize tenderNumberBean = this.pcChannelStatisticsCustomizeMapper.selectTenderNumber(statisticsCustomize);
		// 累计充值
		PcChannelStatisticsCustomize rechargePriceBean = this.pcChannelStatisticsCustomizeMapper.selectRechargePrice(statisticsCustomize);
		// 汇直投出借金额
		PcChannelStatisticsCustomize hztTenderPriceBean = this.pcChannelStatisticsCustomizeMapper.selectHztTenderPrice(statisticsCustomize);
		// 汇消费出借金额
		PcChannelStatisticsCustomize hxfTenderPriceBean = this.pcChannelStatisticsCustomizeMapper.selectHxfTenderPrice(statisticsCustomize);
		// 汇天利出借金额
		PcChannelStatisticsCustomize htlTenderPriceBean = this.pcChannelStatisticsCustomizeMapper.selectHtlTenderPrice(statisticsCustomize);
		// 汇添金出借金额
		PcChannelStatisticsCustomize htjTenderPriceBean = this.pcChannelStatisticsCustomizeMapper.selectHtjTenderPrice(statisticsCustomize);
		// 融通宝出借金额
		PcChannelStatisticsCustomize rtbTenderPriceBean = this.pcChannelStatisticsCustomizeMapper.selectRtbTenderPrice(statisticsCustomize);
		// 汇转让出借金额
		PcChannelStatisticsCustomize hzrTenderPriceBean = this.pcChannelStatisticsCustomizeMapper.selectHzrTenderPrice(statisticsCustomize);
		// 汇直投出借金额
		BigDecimal hztTenderPrice = hztTenderPriceBean == null ? BigDecimal.ZERO : new BigDecimal(hztTenderPriceBean.getHztTenderPrice());
		// 汇消费出借金额
		BigDecimal hxfTenderPrice = hxfTenderPriceBean == null ? BigDecimal.ZERO : new BigDecimal(hxfTenderPriceBean.getHxfTenderPrice());
		// 汇天利出借金额
		BigDecimal htlTenderPrice = htlTenderPriceBean == null ? BigDecimal.ZERO : new BigDecimal(htlTenderPriceBean.getHtlTenderPrice());
		// 汇添金出借金额
		BigDecimal htjTenderPrice = htjTenderPriceBean == null ? BigDecimal.ZERO : new BigDecimal(htjTenderPriceBean.getHtjTenderPrice());
		// 融通宝出借金额
		BigDecimal rtbTenderPrice = rtbTenderPriceBean == null ? BigDecimal.ZERO : new BigDecimal(rtbTenderPriceBean.getRtbTenderPrice());
		// 汇转让出借金额
		BigDecimal hzrTenderPrice = hzrTenderPriceBean == null ? BigDecimal.ZERO : new BigDecimal(hzrTenderPriceBean.getHzrTenderPrice());
		// 插入
		PcChannelStatistics pcChannelStatistics = new PcChannelStatistics();
		// 渠道ID
		pcChannelStatistics.setSourceId(utmPlat.getSourceId());
		// 渠道名称
		pcChannelStatistics.setSourceName(utmPlat.getSourceName());
		// 访问数
		pcChannelStatistics.setAccessNumber(visitCustomizeBean == null ? 0 : Integer.parseInt(visitCustomizeBean.getAccessNumber()));
		// 注册数
		pcChannelStatistics.setRegistNumber(registNumberBean == null ? 0 : Integer.parseInt(registNumberBean.getRegistNumber()));
		// 开户数
		pcChannelStatistics.setOpenAccountNumber(accountNumberBean == null ? 0 : Integer.parseInt(accountNumberBean.getOpenAccountNumber()));
		// 出借人数
		pcChannelStatistics.setTenderNumber(tenderNumberBean == null ? 0 : Integer.parseInt(tenderNumberBean.getTenderNumber()));
		// 累计充值
		pcChannelStatistics.setCumulativeRecharge(rechargePriceBean == null ? BigDecimal.ZERO : new BigDecimal(rechargePriceBean.getCumulativeRecharge()));
		// 汇直投出借金额
		pcChannelStatistics.setHztTenderPrice(hztTenderPrice);
		// 汇消费出借金额
		pcChannelStatistics.setHxfTenderPrice(hxfTenderPrice);
		// 汇天利出借金额
		pcChannelStatistics.setHtlTenderPrice(htlTenderPrice);
		// 汇添金出借金额
		pcChannelStatistics.setHtjTenderPrice(htjTenderPrice);
		// 汇金理财出借金额
		pcChannelStatistics.setRtbTenderPrice(rtbTenderPrice);
		// 汇转让出借金额
		pcChannelStatistics.setHzrTenderPrice(hzrTenderPrice);
		// 累计出借金额
		pcChannelStatistics.setCumulativeInvestment(hztTenderPrice.add(hxfTenderPrice).add(htlTenderPrice).add(htjTenderPrice).add(rtbTenderPrice).add(hzrTenderPrice));
		// 插入时间
		pcChannelStatistics.setAddTime(GetDate.stringToDate2(nowDate));
		return pcChannelStatisticsMapper.insertSelective(pcChannelStatistics) > 0 ? true : false;
	}

	/**
	 * 获取所有渠道号
	 * 
	 * @return
	 */
	@Override
	public List<UtmPlat> selectUtmPlatList() {
		UtmPlatExample utmPlatExample = new UtmPlatExample();
		UtmPlatExample.Criteria cra = utmPlatExample.createCriteria();
		cra.andSourceTypeEqualTo(0); // 渠道 0PC
		cra.andDelFlagEqualTo("0");// 未删除
		return this.utmPlatMapper.selectByExample(utmPlatExample);
	}
}
