package com.hyjf.mybatis.mapper.customize.app;

import java.util.List;

import com.hyjf.mybatis.model.customize.app.AppChannelStatisticsCustomize;

public interface AppChannelStatisticsCustomizeMapper {

	/**
	 * 获取列表 定时插入统计表
	 * 
	 * @param channelCustomize
	 * @return
	 */
	List<AppChannelStatisticsCustomize> selectList(AppChannelStatisticsCustomize channelStatisticsCustomize);

	/**
	 * COUNT
	 * 
	 * @param channelCustomize
	 * @return
	 */
	Integer countList(AppChannelStatisticsCustomize channelStatisticsCustomize);

	/**
	 * 导出列表
	 * 
	 * @param channelCountCustomize
	 * @return
	 */
	List<AppChannelStatisticsCustomize> exportList(AppChannelStatisticsCustomize channelStatisticsCustomize);
	
	/**
	 * 后台展示列表
	 * 
	 * @param channelCustomize
	 * @return
	 */
	List<AppChannelStatisticsCustomize> selectSumList(AppChannelStatisticsCustomize channelStatisticsCustomize);

	/**
	 * 后台展示COUNT
	 * 
	 * @param channelCustomize
	 * @return
	 */
	Integer countSumList(AppChannelStatisticsCustomize channelStatisticsCustomize);

	/**
	 * 查询相应的app渠道访问记录
	 * @param channelStatisticsCustomize
	 * @return
	 */
	
	List<AppChannelStatisticsCustomize> selectVisitCount (AppChannelStatisticsCustomize channelStatisticsCustomize);
	/**
	 * 查询相应的app渠道注册数
	 * @param channelStatisticsCustomize
	 * @return
	 */
	List<AppChannelStatisticsCustomize> selectRegisterCount (AppChannelStatisticsCustomize channelStatisticsCustomize);
	/**
	 * 查询相应的app渠道主单注册数
	 * @param channelStatisticsCustomize
	 * @return
	 */
	List<AppChannelStatisticsCustomize> selectRegisterAttrCount (AppChannelStatisticsCustomize channelStatisticsCustomize);
	/**
	 * 查询相应的app渠道开户数 
	 * @param channelStatisticsCustomize
	 * @return
	 */
	List<AppChannelStatisticsCustomize> selectOpenAccountCount (AppChannelStatisticsCustomize channelStatisticsCustomize);
	/**
	 * 查询相应的app渠道无主单开户数  
	 * @param channelStatisticsCustomize
	 * @return
	 */
	List<AppChannelStatisticsCustomize> selectOpenAccountAttrCount (AppChannelStatisticsCustomize channelStatisticsCustomize);
	/**
	 * 查询相应的app渠道PC开户数 
	 * @param channelStatisticsCustomize
	 * @return
	 */
	List<AppChannelStatisticsCustomize> selectAccountNumberPC (AppChannelStatisticsCustomize channelStatisticsCustomize);
	/**
	 * 查询相应的app渠道微信开户数 
	 * @param channelStatisticsCustomize
	 * @return
	 */
	List<AppChannelStatisticsCustomize> selectAccountNumberWechat (AppChannelStatisticsCustomize channelStatisticsCustomize);
	/**
	 * 查询相应的app渠道Ios开户数 
	 * @param channelStatisticsCustomize
	 * @return
	 */
	List<AppChannelStatisticsCustomize> selectAccountNumberIos (AppChannelStatisticsCustomize channelStatisticsCustomize);
	/**
	 * 询相应的app渠道安卓开户数 
	 * @param channelStatisticsCustomize
	 * @return
	 */
	List<AppChannelStatisticsCustomize> selectAccountNumberAndroid (AppChannelStatisticsCustomize channelStatisticsCustomize);
	/**
	 * 查询相应的app渠道出借用户数 
	 * @param channelStatisticsCustomize
	 * @return
	 */
	List<AppChannelStatisticsCustomize> selectInvestNumber (AppChannelStatisticsCustomize channelStatisticsCustomize);
	/**
	 * 查询相应的app渠道出借无主单用户数 
	 * @param channelStatisticsCustomize
	 * @return
	 */
	List<AppChannelStatisticsCustomize> selectInvestAttrNumber (AppChannelStatisticsCustomize channelStatisticsCustomize);
	/**
	 * 查询相应的app渠道用户充值数 
	 * @param channelStatisticsCustomize
	 * @return
	 */
	List<AppChannelStatisticsCustomize> selectCumulativeCharge (AppChannelStatisticsCustomize channelStatisticsCustomize);
	/**
	 * 查询相应的app渠道无主单用户充值数 
	 * @param channelStatisticsCustomize
	 * @return
	 */
	List<AppChannelStatisticsCustomize> selectCumulativeAttrCharge (AppChannelStatisticsCustomize channelStatisticsCustomize);
	/**
	 * 查询相应的app渠道用户汇直投出借数 
	 * @param channelStatisticsCustomize
	 * @return
	 */
	List<AppChannelStatisticsCustomize> selectHztInvestSum (AppChannelStatisticsCustomize channelStatisticsCustomize);
	/**
	 * 查询相应的app渠道用户汇消费出借数 
	 * @param channelStatisticsCustomize
	 * @return
	 */
	List<AppChannelStatisticsCustomize> selectHxfInvestSum (AppChannelStatisticsCustomize channelStatisticsCustomize);
	/**
	 * 查询相应的app渠道用户汇天利出借数 
	 * @param channelStatisticsCustomize
	 * @return
	 */
	List<AppChannelStatisticsCustomize> selectHtlInvestSum (AppChannelStatisticsCustomize channelStatisticsCustomize);
	/**
	 * 查询相应的app渠道用户汇添金出借数 
	 * @param channelStatisticsCustomize
	 * @return
	 */
	List<AppChannelStatisticsCustomize> selectHtjInvestSum (AppChannelStatisticsCustomize channelStatisticsCustomize);
	/**
	 * 查询相应的app渠道用户融通宝出借数 
	 * @param channelStatisticsCustomize
	 * @return
	 */
	List<AppChannelStatisticsCustomize> selectRtbInvestSum (AppChannelStatisticsCustomize channelStatisticsCustomize);
	/**
	 * 查询相应的app渠道用户汇转让出借数 
	 * @param channelStatisticsCustomize
	 * @return
	 */
	List<AppChannelStatisticsCustomize> selectHzrInvestSum (AppChannelStatisticsCustomize channelStatisticsCustomize);
	/**
	 * 查询相应的app渠道用户PC出借数 
	 * @param channelStatisticsCustomize
	 * @return
	 */
	List<AppChannelStatisticsCustomize> selectTenderNumberPc (AppChannelStatisticsCustomize channelStatisticsCustomize);
	/**
	 * 查询相应的app渠道用户微信出借数 
	 * @param channelStatisticsCustomize
	 * @return
	 */
	List<AppChannelStatisticsCustomize> selectTenderNumberWechat (AppChannelStatisticsCustomize channelStatisticsCustomize);
	/**
	 * 查询相应的app渠道用户Android出借数 
	 * @param channelStatisticsCustomize
	 * @return
	 */
	List<AppChannelStatisticsCustomize> selectTenderNumberAndroid (AppChannelStatisticsCustomize channelStatisticsCustomize);
	/**
	 * 查询相应的app渠道用户IOS出借数 
	 * @param channelStatisticsCustomize
	 * @return
	 */
	List<AppChannelStatisticsCustomize> selectTenderNumberIos (AppChannelStatisticsCustomize channelStatisticsCustomize);
	/**
	 * 查询相应的app渠道用户出借总额 
	 * @param channelStatisticsCustomize
	 * @return
	 */
	List<AppChannelStatisticsCustomize> selectCumulativeAttrInvest(AppChannelStatisticsCustomize channelStatisticsCustomize);
	

}