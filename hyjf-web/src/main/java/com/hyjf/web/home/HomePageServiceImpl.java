package com.hyjf.web.home;

import com.alicp.jetcache.anno.CachePenetrationProtect;
import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mongo.operationreport.dao.TotalInvestAndInterestMongoDao;
import com.hyjf.mongo.operationreport.entity.TotalInvestAndInterestEntity;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.web.WebHomePageStatisticsCustomize;
import com.hyjf.web.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class HomePageServiceImpl extends BaseServiceImpl implements HomePageService {

	@Autowired
	private TotalInvestAndInterestMongoDao totalInvestAndInterestMongoDao;

	@Override
	public WebHomePageStatisticsCustomize searchTotalStatistics() {
		WebHomePageStatisticsCustomize homeStatistics = webHomePageCustomizeMapper.countTotalStatistics();
		return homeStatistics;
	}

	@Override
	@Cached(name="webHomeCompanyCache-", expire = CustomConstants.HOME_CACHE_LIVE_TIME, cacheType = CacheType.BOTH)
	@CacheRefresh(refresh = 60, stopRefreshAfterLastAccess = 60, timeUnit = TimeUnit.SECONDS)
	public List<ContentArticle> searchHomeNoticeList(String noticeType, int offset, int limit) {
		ContentArticleExample example = new ContentArticleExample();
		if (offset != -1) {
			example.setLimitStart(offset);
			example.setLimitEnd(limit);
		}
		ContentArticleExample.Criteria crt = example.createCriteria();
		crt.andTypeEqualTo(noticeType);
		crt.andStatusEqualTo(1);
		example.setOrderByClause("create_time Desc");
		List<ContentArticle> contentArticles = contentArticleMapper.selectByExampleWithBLOBs(example);
		return contentArticles;
	}


	@Override
	@Cached(name="webHomeBannerCache-", expire = CustomConstants.HOME_CACHE_LIVE_TIME, cacheType = CacheType.BOTH)
	@CacheRefresh(refresh = 60, stopRefreshAfterLastAccess = 60, timeUnit = TimeUnit.SECONDS)
	public List<Ads> searchHomeAdsList(Integer typeId,Short isIndex, int offset, int limit) {
		AdsExample example = new AdsExample();
		if (offset != -1) {
			example.setLimitStart(offset);
			example.setLimitEnd(limit);
		}
		example.setOrderByClause("`order` Asc");
		AdsExample.Criteria crt = example.createCriteria();
		crt.andTypeidEqualTo(typeId);
		crt.andStatusEqualTo((short) 1);
		crt.andIsIndexEqualTo(isIndex);
		crt.andStartTimeLessThanOrEqualTo(GetDate.getDataString(GetDate.datetimeFormat));//活动开始时间要小于当前时间
		crt.andEndTimeGreaterThanOrEqualTo(GetDate.getDataString(GetDate.datetimeFormat));//活动结束时间要大于当前时间
		List<Ads> adss = adsMapper.selectByExample(example);
		return adss;
	}

	@Override
	public int saveUtmVisit(UtmVisit visit) {
		utmVisitMapper.insertSelective(visit);
		return 0;
	}

	/**
	 * 获取平台统计数据
	 * @return
	 * @author Michael
	 */
	@Override
	public CalculateInvestInterest getTenderSum() {
		List<CalculateInvestInterest> list = this.calculateInvestInterestMapper.selectByExample(new CalculateInvestInterestExample());
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;	
	}
	
	/**
	 * 查询最新更新地址(下载APP链接)
	 * @return
	 */
	@Override
	public Version getVersion() {
		VersionExample example = new VersionExample();
		VersionExample.Criteria cra = example.createCriteria();
		// 0 PC ,1 Android , 2 IOS , 3 wechat
		cra.andTypeEqualTo(1);
		// 版本号最高
		example.setOrderByClause("id desc");
		List<Version> versionList = versionMapper.selectByExample(example);
		return versionList.size() == 0 ? new Version() : versionList.get(0);
	}

	@Override
	public BigDecimal selectTenderSum() {
		TotalInvestAndInterestEntity entity = totalInvestAndInterestMongoDao.findOne(new Query());
		if (entity != null) {
			return entity.getTotalInvestAmount();
		}
		return BigDecimal.ZERO;
	}

	@Override
	public BigDecimal selectInterestSum() {
		TotalInvestAndInterestEntity entity = totalInvestAndInterestMongoDao.findOne(new Query());
		if (entity != null) {
			return entity.getTotalInterestAmount();
		}
		return BigDecimal.ZERO;
	}

	@Override
	public int selectTotalTenderSum() {
		TotalInvestAndInterestEntity entity = totalInvestAndInterestMongoDao.findOne(new Query());
		if (entity != null) {
			return entity.getTotalInvestNum();
		}
		return 0;
	}
}
