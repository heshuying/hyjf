package com.hyjf.web.home;

import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.web.WebHomePageStatisticsCustomize;
import com.hyjf.web.BaseService;

import java.math.BigDecimal;
import java.util.List;

public interface HomePageService extends BaseService {
	/**
	 * 获取系统累计出借金额、累计收益、风险保证金（计算）
	 * @return
	 */
	WebHomePageStatisticsCustomize searchTotalStatistics();
	/**
	 * 取首页公告列表
	 * @return
	 */
	List<ContentArticle> searchHomeNoticeList(String noticeType, int offset, int limit);

	/**
	 * 获取广告
	 * @param typeId
	 * @param isIndex
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<Ads> searchHomeAdsList(Integer typeId,Short isIndex, int offset, int limit);

	/**
	 * 保存访问记录
	 * @param visit
	 * @return
	 */
	public int saveUtmVisit(UtmVisit visit);

	/**
	 * 获取平台统计数据
	 * @return
	 * @author Michael
	 */
	public CalculateInvestInterest getTenderSum();
	
	/**
	 * 查询最新更新地址(下载APP链接)
	 * @return
	 */
	Version getVersion();

	/**
	 * 累计出借总额
	 * @return
	 */
	BigDecimal selectTenderSum();

	/**
	 * 累计收益
	 * @return
	 */
	BigDecimal selectInterestSum();

	/**
	 * 累计出借笔数
	 * @return
	 */
	int selectTotalTenderSum();
	
}
