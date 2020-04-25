package com.hyjf.batch.app.promotion;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.AppChannelStatistics;
import com.hyjf.mybatis.model.auto.UtmPlat;
import com.hyjf.mybatis.model.auto.UtmPlatExample;
import com.hyjf.mybatis.model.customize.app.AppChannelStatisticsCustomize;

@Service
public class ChannelStatisticsServiceImpl extends BaseServiceImpl implements ChannelStatisticsService {

	/**
	 * 查询相应的app渠道统计数据
	 * 
	 * @param channelStatisticsCustomize
	 * @return
	 * @author Michael
	 */
	public AppChannelStatisticsCustomize selectAppChannelStatistics(AppChannelStatisticsCustomize channelStatisticsCustomize) {

		AppChannelStatisticsCustomize statistic = new AppChannelStatisticsCustomize();
		List<AppChannelStatisticsCustomize> visitCount = appChannelStatisticsCustomizeMapper.selectVisitCount(channelStatisticsCustomize);
		if (visitCount != null && visitCount.size() == 1) {
			statistic.setVisitCount(StringUtils.isNotBlank(visitCount.get(0).getVisitCount()) ? visitCount.get(0).getVisitCount() : "0");
		}
		List<AppChannelStatisticsCustomize> registerCount = appChannelStatisticsCustomizeMapper.selectRegisterCount(channelStatisticsCustomize);
		if (registerCount != null && registerCount.size() == 1) {
			statistic.setRegisterCount(StringUtils.isNotBlank(registerCount.get(0).getRegisterCount()) ? registerCount.get(0).getRegisterCount() : "0");
		}
		List<AppChannelStatisticsCustomize> registerAttrCount = appChannelStatisticsCustomizeMapper.selectRegisterAttrCount(channelStatisticsCustomize);
		if (registerAttrCount != null && registerAttrCount.size() == 1) {
			statistic.setRegisterAttrCount(StringUtils.isNotBlank(registerAttrCount.get(0).getRegisterAttrCount()) ? registerAttrCount.get(0).getRegisterAttrCount() : "0");
		}
		List<AppChannelStatisticsCustomize> openAccountCount = appChannelStatisticsCustomizeMapper.selectOpenAccountCount(channelStatisticsCustomize);
		if (openAccountCount != null && openAccountCount.size() == 1) {
			statistic.setOpenAccountCount(StringUtils.isNotBlank(openAccountCount.get(0).getOpenAccountCount()) ? openAccountCount.get(0).getOpenAccountCount() : "0");
		}
		List<AppChannelStatisticsCustomize> openAccountAttrCount = appChannelStatisticsCustomizeMapper.selectOpenAccountAttrCount(channelStatisticsCustomize);
		if (openAccountAttrCount != null && openAccountAttrCount.size() == 1) {
			statistic.setOpenAccountAttrCount(StringUtils.isNotBlank(openAccountAttrCount.get(0).getOpenAccountAttrCount()) ? openAccountAttrCount.get(0).getOpenAccountAttrCount() : "0");
		}
		List<AppChannelStatisticsCustomize> accountNumberPc = appChannelStatisticsCustomizeMapper.selectAccountNumberPC(channelStatisticsCustomize);
		if (accountNumberPc != null && accountNumberPc.size() == 1) {
			statistic.setAccountNumberPc(StringUtils.isNotBlank(accountNumberPc.get(0).getAccountNumberPc()) ? accountNumberPc.get(0).getAccountNumberPc() : "0");
		}
		List<AppChannelStatisticsCustomize> accountNumberWechat = appChannelStatisticsCustomizeMapper.selectAccountNumberWechat(channelStatisticsCustomize);
		if (accountNumberWechat != null && accountNumberWechat.size() == 1) {
			statistic.setAccountNumberWechat(StringUtils.isNotBlank(accountNumberWechat.get(0).getAccountNumberWechat()) ? accountNumberWechat.get(0).getAccountNumberWechat() : "0");
		}
		List<AppChannelStatisticsCustomize> accountNumberIos = appChannelStatisticsCustomizeMapper.selectAccountNumberIos(channelStatisticsCustomize);
		if (accountNumberIos != null && accountNumberIos.size() == 1) {
			statistic.setAccountNumberIos(StringUtils.isNotBlank(accountNumberIos.get(0).getAccountNumberIos()) ? accountNumberIos.get(0).getAccountNumberIos() : "0");
		}
		List<AppChannelStatisticsCustomize> accountNumberAndroid = appChannelStatisticsCustomizeMapper.selectAccountNumberAndroid(channelStatisticsCustomize);
		if (accountNumberAndroid != null && accountNumberAndroid.size() == 1) {
			statistic.setAccountNumberAndroid(StringUtils.isNotBlank(accountNumberAndroid.get(0).getAccountNumberAndroid()) ? accountNumberAndroid.get(0).getAccountNumberAndroid() : "0");
		}
		List<AppChannelStatisticsCustomize> investNumber = appChannelStatisticsCustomizeMapper.selectInvestNumber(channelStatisticsCustomize);
		if (investNumber != null && investNumber.size() == 1) {
			statistic.setInvestNumber(StringUtils.isNotBlank(investNumber.get(0).getInvestNumber()) ? investNumber.get(0).getInvestNumber() : "0");
		}
		List<AppChannelStatisticsCustomize> investAttrNumber = appChannelStatisticsCustomizeMapper.selectInvestAttrNumber(channelStatisticsCustomize);
		if (investAttrNumber != null && investAttrNumber.size() == 1) {
			statistic.setInvestAttrNumber(StringUtils.isNotBlank(investAttrNumber.get(0).getInvestAttrNumber()) ? investAttrNumber.get(0).getInvestAttrNumber() : "0");
		}
		List<AppChannelStatisticsCustomize> cumulativeCharge = appChannelStatisticsCustomizeMapper.selectCumulativeCharge(channelStatisticsCustomize);
		if (cumulativeCharge != null && cumulativeCharge.size() == 1) {
			statistic.setCumulativeCharge(StringUtils.isNotBlank(cumulativeCharge.get(0).getCumulativeCharge()) ? cumulativeCharge.get(0).getCumulativeCharge() : "0");
		}
		List<AppChannelStatisticsCustomize> cumulativeAttrCharge = appChannelStatisticsCustomizeMapper.selectCumulativeAttrCharge(channelStatisticsCustomize);
		if (cumulativeAttrCharge != null && cumulativeAttrCharge.size() == 1) {
			statistic.setCumulativeAttrCharge(StringUtils.isNotBlank(cumulativeAttrCharge.get(0).getCumulativeAttrCharge()) ? cumulativeAttrCharge.get(0).getCumulativeAttrCharge() : "0");
		}
		// 统计相应的数据
		// 汇直投出借总额
		BigDecimal hztInvestSumTotal = BigDecimal.ZERO;
		// 汇直投出借总额
		BigDecimal hxfInvestSumTotal = BigDecimal.ZERO;
		// 汇直投出借总额
		BigDecimal htlInvestSumTotal = BigDecimal.ZERO;
		// 汇直投出借总额
		BigDecimal htjInvestSumTotal = BigDecimal.ZERO;
		// 汇直投出借总额
		BigDecimal rtbInvestSumTotal = BigDecimal.ZERO;
		// 汇直投出借总额
		BigDecimal hzrInvestSumTotal = BigDecimal.ZERO;
		List<AppChannelStatisticsCustomize> hztInvestSum = appChannelStatisticsCustomizeMapper.selectHztInvestSum(channelStatisticsCustomize);
		if (hztInvestSum != null && hztInvestSum.size() == 1) {
			String investSum = StringUtils.isNotBlank(hztInvestSum.get(0).getHztInvestSum()) ? hztInvestSum.get(0).getHztInvestSum() : "0";
			hztInvestSumTotal = new BigDecimal(investSum);
			statistic.setHztInvestSum(investSum);
		}
		List<AppChannelStatisticsCustomize> hxfInvestSum = appChannelStatisticsCustomizeMapper.selectHxfInvestSum(channelStatisticsCustomize);
		if (hxfInvestSum != null && hxfInvestSum.size() == 1) {
			String investSum = StringUtils.isNotBlank(hxfInvestSum.get(0).getHxfInvestSum()) ? hxfInvestSum.get(0).getHxfInvestSum() : "0";
			hxfInvestSumTotal = new BigDecimal(investSum);
			statistic.setHxfInvestSum(investSum);
		}
		List<AppChannelStatisticsCustomize> htlInvestSum = appChannelStatisticsCustomizeMapper.selectHtlInvestSum(channelStatisticsCustomize);
		if (htlInvestSum != null && htlInvestSum.size() == 1) {
			String investSum = StringUtils.isNotBlank(htlInvestSum.get(0).getHtlInvestSum()) ? htlInvestSum.get(0).getHtlInvestSum() : "0";
			htlInvestSumTotal = new BigDecimal(investSum);
			statistic.setHtlInvestSum(investSum);
		}
		List<AppChannelStatisticsCustomize> htjInvestSum = appChannelStatisticsCustomizeMapper.selectHtjInvestSum(channelStatisticsCustomize);
		if (htjInvestSum != null && htjInvestSum.size() == 1) {
			String investSum = StringUtils.isNotBlank(htjInvestSum.get(0).getHtjInvestSum()) ? htjInvestSum.get(0).getHtjInvestSum() : "0";
			htjInvestSumTotal = new BigDecimal(investSum);
			statistic.setHtjInvestSum(investSum);
		}
		List<AppChannelStatisticsCustomize> rtbInvestSum = appChannelStatisticsCustomizeMapper.selectRtbInvestSum(channelStatisticsCustomize);
		if (rtbInvestSum != null && rtbInvestSum.size() == 1) {
			String investSum = StringUtils.isNotBlank(rtbInvestSum.get(0).getRtbInvestSum()) ? rtbInvestSum.get(0).getRtbInvestSum() : "0";
			rtbInvestSumTotal = new BigDecimal(investSum);
			statistic.setRtbInvestSum(investSum);
		}
		List<AppChannelStatisticsCustomize> hzrInvestSum = appChannelStatisticsCustomizeMapper.selectHzrInvestSum(channelStatisticsCustomize);
		if (hzrInvestSum != null && hzrInvestSum.size() == 1) {
			String investSum = StringUtils.isNotBlank(hzrInvestSum.get(0).getHzrInvestSum()) ? hzrInvestSum.get(0).getHzrInvestSum() : "0";
			hzrInvestSumTotal = new BigDecimal(investSum);
			statistic.setHzrInvestSum(investSum);
		}
		BigDecimal cumulativeInvest = hztInvestSumTotal.add(hxfInvestSumTotal).add(htlInvestSumTotal).add(htjInvestSumTotal).add(rtbInvestSumTotal).add(hzrInvestSumTotal);
		statistic.setCumulativeInvest(cumulativeInvest.toString());
		List<AppChannelStatisticsCustomize> cumulativeAttrInvest = appChannelStatisticsCustomizeMapper.selectCumulativeAttrInvest(channelStatisticsCustomize);
		if (cumulativeAttrInvest != null && cumulativeAttrInvest.size() == 1) {
			statistic.setCumulativeAttrInvest(StringUtils.isNotBlank(cumulativeAttrInvest.get(0).getCumulativeAttrInvest()) ? cumulativeAttrInvest.get(0).getCumulativeAttrInvest() : "0");
		}
		List<AppChannelStatisticsCustomize> tenderNumberPc = appChannelStatisticsCustomizeMapper.selectTenderNumberPc(channelStatisticsCustomize);
		if (tenderNumberPc != null && tenderNumberPc.size() == 1) {
			statistic.setTenderNumberPc(StringUtils.isNotBlank(tenderNumberPc.get(0).getTenderNumberPc()) ? tenderNumberPc.get(0).getTenderNumberPc() : "0");
		}
		List<AppChannelStatisticsCustomize> tenderNumberWechat = appChannelStatisticsCustomizeMapper.selectTenderNumberWechat(channelStatisticsCustomize);
		if (tenderNumberWechat != null && tenderNumberWechat.size() == 1) {
			statistic.setTenderNumberWechat(StringUtils.isNotBlank(tenderNumberWechat.get(0).getTenderNumberWechat()) ? tenderNumberWechat.get(0).getTenderNumberWechat() : "0");
		}
		List<AppChannelStatisticsCustomize> tenderNumberAndroid = appChannelStatisticsCustomizeMapper.selectTenderNumberAndroid(channelStatisticsCustomize);
		if (tenderNumberAndroid != null && tenderNumberAndroid.size() == 1) {
			statistic.setTenderNumberAndroid(StringUtils.isNotBlank(tenderNumberAndroid.get(0).getTenderNumberAndroid()) ? tenderNumberAndroid.get(0).getTenderNumberAndroid() : "0");
		}
		List<AppChannelStatisticsCustomize> tenderNumberIos = appChannelStatisticsCustomizeMapper.selectTenderNumberIos(channelStatisticsCustomize);
		if (tenderNumberIos != null && tenderNumberIos.size() == 1) {
			statistic.setTenderNumberIos(StringUtils.isNotBlank(tenderNumberIos.get(0).getTenderNumberIos()) ? tenderNumberIos.get(0).getTenderNumberIos() : "0");
		}
		return statistic;
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * 
	 * @param channelStatisticsCustomize
	 * @author Michael
	 */

	public void insertRecord(AppChannelStatisticsCustomize record) {
		AppChannelStatistics appChannelStatistics = new AppChannelStatistics();
		appChannelStatistics.setChannelName(record.getChannelName());
		appChannelStatistics.setSourceId(Integer.valueOf(record.getSourceId()));
		appChannelStatistics.setVisitCount(new BigDecimal(record.getVisitCount()));
		appChannelStatistics.setRegisterCount(new BigDecimal(record.getRegisterCount()));
		appChannelStatistics.setRegisterAttrCount(new BigDecimal(record.getRegisterAttrCount()));
		appChannelStatistics.setOpenAccountCount(new BigDecimal(record.getOpenAccountCount()));
		appChannelStatistics.setInvestNumber(new BigDecimal(record.getInvestNumber()));
		appChannelStatistics.setCumulativeCharge(new BigDecimal(record.getCumulativeCharge()));
		appChannelStatistics.setCumulativeInvest(new BigDecimal(record.getCumulativeInvest()));
		if (record.getHtlInvestSum() != null && record.getHtlInvestSum() != "") {
			appChannelStatistics.setHtlInvestSum(new BigDecimal(record.getHtlInvestSum()));
		} else {
			appChannelStatistics.setHtlInvestSum(new BigDecimal("0"));
		}
		if (record.getHztInvestSum() != null && record.getHztInvestSum() != "") {
			appChannelStatistics.setHztInvestSum(new BigDecimal(record.getHztInvestSum()));
		} else {
			appChannelStatistics.setHztInvestSum(new BigDecimal("0"));
		}
		if (record.getHxfInvestSum() != null && record.getHxfInvestSum() != "") {
			appChannelStatistics.setHxfInvestSum(new BigDecimal(record.getHxfInvestSum()));
		} else {
			appChannelStatistics.setHxfInvestSum(new BigDecimal("0"));
		}
		// 汇添金出借金额
		if (StringUtils.isNotEmpty(record.getHtjInvestSum())) {
			appChannelStatistics.setHtjInvestSum(new BigDecimal(record.getHtjInvestSum()));
		} else {
			appChannelStatistics.setHtjInvestSum(BigDecimal.ZERO);
		}
		// 汇金理财出借金额
		if (StringUtils.isNotEmpty(record.getRtbInvestSum())) {
			appChannelStatistics.setRtbInvestSum(new BigDecimal(record.getRtbInvestSum()));
		} else {
			appChannelStatistics.setRtbInvestSum(BigDecimal.ZERO);
		}
		// 汇转让出借金额
		if (StringUtils.isNotEmpty(record.getHzrInvestSum())) {
			appChannelStatistics.setHzrInvestSum(new BigDecimal(record.getHzrInvestSum()));
		} else {
			appChannelStatistics.setHzrInvestSum(BigDecimal.ZERO);
		}
		appChannelStatistics.setUpdateTime(GetDate.getDate());
		// 导出报表新增字段
		appChannelStatistics.setAccountNumberAndroid(Integer.parseInt(record.getAccountNumberAndroid()));
		appChannelStatistics.setAccountNumberIos(Integer.parseInt(record.getAccountNumberIos()));
		appChannelStatistics.setAccountNumberPc(Integer.parseInt(record.getAccountNumberPc()));
		appChannelStatistics.setAccountNumberWechat(Integer.parseInt(record.getAccountNumberWechat()));
		appChannelStatistics.setTenderNumberAndroid(Integer.parseInt(record.getTenderNumberAndroid()));
		appChannelStatistics.setTenderNumberIos(Integer.parseInt(record.getTenderNumberIos()));
		appChannelStatistics.setTenderNumberPc(Integer.parseInt(record.getTenderNumberPc()));
		appChannelStatistics.setTenderNumberWechat(Integer.parseInt(record.getTenderNumberWechat()));
		// 2016-05-10报表新增字段
		appChannelStatistics.setInvestAttrNumber(Integer.parseInt(record.getInvestAttrNumber()));
		appChannelStatistics.setCumulativeAttrCharge(new BigDecimal(record.getCumulativeAttrCharge()));
		appChannelStatistics.setCumulativeAttrInvest(new BigDecimal(record.getCumulativeAttrInvest()));
		appChannelStatistics.setOpenAccountAttrCount(Integer.parseInt(record.getOpenAccountAttrCount()));
		appChannelStatisticsMapper.insertSelective(appChannelStatistics);
	}

	@Override
	public List<UtmPlat> selectUtmPlat() {
		UtmPlatExample utmPlatExample = new UtmPlatExample();
		UtmPlatExample.Criteria cra = utmPlatExample.createCriteria();
		cra.andSourceTypeEqualTo(1); // 渠道 1App
		cra.andDelFlagEqualTo("0");// 未删除
		return this.utmPlatMapper.selectByExample(utmPlatExample);
	}
}
