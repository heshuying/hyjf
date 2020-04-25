package com.hyjf.batch.app.promotiondetail;

import java.util.List;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.AppChannelStatistics;
import com.hyjf.mybatis.model.auto.AppChannelStatisticsDetail;
import com.hyjf.mybatis.model.customize.batch.BatchChannelStatisticsOldCustomize;
import com.hyjf.mybatis.model.customize.batch.BatchPcPromotionCustomize;

/**
 * APP渠道统计明细老数据修复定时Service
 * 
 * @author liuyang
 *
 */
public interface ChannelStatisticsDetailService extends BaseService {
	/**
	 * 检索APP渠道统计明细
	 * 
	 * @return
	 */
	public List<AppChannelStatisticsDetail> selectChannelStatisticsDetailList();

	/**
	 * 根据用户ID检索用户出借信息
	 * 
	 * @param userId
	 * @return
	 */
	public BatchPcPromotionCustomize selectAppUserInvestByUserId(Integer userId);

	/**
	 * 更新hyjf_app_channel_statistics_detail
	 * 
	 * @param appChannelStatisticsDetail
	 * @param batchAppPromotionCustomize
	 * @return
	 */
	public boolean updateAppChannelStatisticsDetail(AppChannelStatisticsDetail appChannelStatisticsDetail, BatchPcPromotionCustomize batchAppPromotionCustomize);
	
	/*---------------------------------渠道统计主表------------------------------------------*/
	/**
	 * 检索APP渠道统计表
	 * 
	 * @return
	 */
	public List<AppChannelStatistics> selectChannelStatisticsList();
	
	
	/**
	 * 根据渠道号及时间检索 出借金额信息
	 * 
	 * @return
	 */
	public BatchChannelStatisticsOldCustomize selectChannelStatisticsOldList(BatchChannelStatisticsOldCustomize batchChannelStatisticsOldCustomize);
	
	
	/**
	 * 更新渠道统计表
	 * 
	 * @param appChannelStatisticsDetail
	 * @param batchAppPromotionCustomize
	 * @return
	 */
	public boolean updateAppChannelStatistics(AppChannelStatistics appChannelStatistics, BatchChannelStatisticsOldCustomize batchChannelStatisticsOldCustomize);

}
