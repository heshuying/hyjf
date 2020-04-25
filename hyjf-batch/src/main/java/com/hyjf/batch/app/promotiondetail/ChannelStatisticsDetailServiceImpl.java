package com.hyjf.batch.app.promotiondetail;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.AppChannelStatistics;
import com.hyjf.mybatis.model.auto.AppChannelStatisticsDetail;
import com.hyjf.mybatis.model.auto.AppChannelStatisticsDetailExample;
import com.hyjf.mybatis.model.auto.AppChannelStatisticsExample;
import com.hyjf.mybatis.model.customize.batch.BatchChannelStatisticsOldCustomize;
import com.hyjf.mybatis.model.customize.batch.BatchPcPromotionCustomize;

/**
 * APP渠道统计明细老数据修复定时Service实现类
 * 
 * @author liuyang
 *
 */
@Service
public class ChannelStatisticsDetailServiceImpl extends BaseServiceImpl implements ChannelStatisticsDetailService {

	@Override
	public List<AppChannelStatisticsDetail> selectChannelStatisticsDetailList() {

		AppChannelStatisticsDetailExample example = new AppChannelStatisticsDetailExample();
		AppChannelStatisticsDetailExample.Criteria cra = example.createCriteria();
		// 首投时间为空
		cra.andFirstInvestTimeIsNull();
		return this.appChannelStatisticsDetailMapper.selectByExample(example);
	}

	@Override
	public BatchPcPromotionCustomize selectAppUserInvestByUserId(Integer userId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userId", userId);
		List<BatchPcPromotionCustomize> list = this.batchPcPromotionCustomizeMapper.selectAppPromotionCustomizeList(param);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 更新hyjf_app_channel_statistics_detail表数据
	 */
	@Override
	public boolean updateAppChannelStatisticsDetail(AppChannelStatisticsDetail appChannelStatisticsDetail, BatchPcPromotionCustomize batchAppPromotionCustomize) {
		// 首投时间
		appChannelStatisticsDetail.setFirstInvestTime(Integer.parseInt(batchAppPromotionCustomize.getInvestTime()));
		// 首投项目类型
		appChannelStatisticsDetail.setInvestProjectType(batchAppPromotionCustomize.getInvestProjectType());
		// 首投项目期限
		appChannelStatisticsDetail.setInvestProjectPeriod(batchAppPromotionCustomize.getInvestProjectPeriod());
		// 首投金额
		appChannelStatisticsDetail.setInvestAmount(batchAppPromotionCustomize.getInvestAmount());
		return this.appChannelStatisticsDetailMapper.updateByPrimaryKeySelective(appChannelStatisticsDetail) > 0 ? true : false;
	}

	/**
	 * 检索所有数据
	 * @return
	 * @author Michael
	 */
		
	@Override
	public List<AppChannelStatistics> selectChannelStatisticsList() {
		return appChannelStatisticsMapper.selectByExample(new AppChannelStatisticsExample());
	}

	/**
	 * 根据渠道号查询出借数据
	 * @param batchChannelStatisticsOldCustomize
	 * @return
	 * @author Michael
	 */
		
	@Override
	public BatchChannelStatisticsOldCustomize selectChannelStatisticsOldList(
			BatchChannelStatisticsOldCustomize batchChannelStatisticsOldCustomize) {
		List<BatchChannelStatisticsOldCustomize> list = batchChannelStatisticsOldCustomizeMapper.selectChannelStatisticsOldList(batchChannelStatisticsOldCustomize);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}

	/**
	 * 更新统计表
	 * @param appChannelStatistics
	 * @param batchChannelStatisticsOldCustomize
	 * @return
	 * @author Michael
	 */
	@Override
	public boolean updateAppChannelStatistics(AppChannelStatistics appChannelStatistics,
			BatchChannelStatisticsOldCustomize batchChannelStatisticsOldCustomize) {
		//出借总额
		appChannelStatistics.setCumulativeInvest(new BigDecimal(batchChannelStatisticsOldCustomize.getCumulativeInvest()));
		// 汇添金出借金额
		if (StringUtils.isNotEmpty(batchChannelStatisticsOldCustomize.getHtjInvestSum())) {
			appChannelStatistics.setHtjInvestSum(new BigDecimal(batchChannelStatisticsOldCustomize.getHtjInvestSum()));
		} else {
			appChannelStatistics.setHtjInvestSum(BigDecimal.ZERO);
		}
		// 汇金理财出借金额
		if (StringUtils.isNotEmpty(batchChannelStatisticsOldCustomize.getRtbInvestSum())) {
			appChannelStatistics.setRtbInvestSum(new BigDecimal(batchChannelStatisticsOldCustomize.getRtbInvestSum()));
		} else {
			appChannelStatistics.setRtbInvestSum(BigDecimal.ZERO);
		}
		// 汇转让出借金额
		if (StringUtils.isNotEmpty(batchChannelStatisticsOldCustomize.getHzrInvestSum())) {
			appChannelStatistics.setHzrInvestSum(new BigDecimal(batchChannelStatisticsOldCustomize.getHzrInvestSum()));
		} else {
			appChannelStatistics.setHzrInvestSum(BigDecimal.ZERO);
		}
		
		return appChannelStatisticsMapper.updateByPrimaryKeySelective(appChannelStatistics) > 0 ? true :false;
	}

}
