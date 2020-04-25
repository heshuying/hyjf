/**
 * 首页service接口实现
 * @package com.hyjf.app.home
 * @author 王坤
 * @date 2016/02/05 10:30
 * @version V1.0  
 */
package com.hyjf.app.home;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.hyjf.common.cache.RedisConstants;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.StringUtil;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.EvalationCustomize;
import com.hyjf.mybatis.model.customize.EvalationExampleCustomize;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.hyjf.app.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.Validator;
import com.hyjf.mongo.operationreport.dao.TotalInvestAndInterestMongoDao;
import com.hyjf.mongo.operationreport.entity.TotalInvestAndInterestEntity;
import com.hyjf.mybatis.model.customize.ContentArticleCustomize;
import com.hyjf.mybatis.model.customize.app.AppAdsCustomize;
import com.hyjf.mybatis.model.customize.app.AppBorrowImageCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectListCustomize;
import com.hyjf.mybatis.model.customize.web.WebHomePageStatisticsCustomize;
import com.hyjf.mybatis.model.customize.web.hjh.HjhPlanCustomize;

@Service
public class HomePageServiceImpl extends BaseServiceImpl implements HomePageService {

	@Autowired
	private TotalInvestAndInterestMongoDao mongoDao;

	/**
	 * 查询首页的bannner列表(加缓存)
	 */
	@Override
	@Cached(name="appHomeBannerCache-", expire = CustomConstants.HOME_CACHE_LIVE_TIME, cacheType = CacheType.BOTH)
	@CacheRefresh(refresh = 60, stopRefreshAfterLastAccess = 60, timeUnit = TimeUnit.SECONDS)
	public List<AppAdsCustomize> searchBannerList(Map<String, Object> ads) {

		List<AppAdsCustomize> adsList = adsCustomizeMapper.selectAdsList(ads);
		return adsList;
	}

	/**
	 * 查询项目类型的列表
	 */
	@Override
	public List<AppBorrowImageCustomize> searchProjectTypeList(Map<String, Object> projectType) {

		List<AppBorrowImageCustomize> borrowImageList = appBorrowImageCustomizeMapper.selectBorrowImageList(projectType);
		for (AppBorrowImageCustomize appBorrowImageCustomize : borrowImageList) {
            if (appBorrowImageCustomize.getPageType().equals("0")) {
                appBorrowImageCustomize.setTypeType(appBorrowImageCustomize.getJumpName());
                appBorrowImageCustomize.setTypeUrl("");
            }else if (appBorrowImageCustomize.getPageType().equals("1")) {
                appBorrowImageCustomize.setTypeType("");
            }else if (appBorrowImageCustomize.getPageType().equals("2")) {
                appBorrowImageCustomize.setTypeType("");
                appBorrowImageCustomize.setTypeUrl("");
            }
        }
		return borrowImageList;
	}

	/***
	 * TODO 查询首页项目列表
	 */
	@Override
	
	@Cached(name="appHomeProjectListCache-", expire = CustomConstants.HOME_CACHE_LIVE_TIME, cacheType = CacheType.BOTH)
	@CacheRefresh(refresh = 5, stopRefreshAfterLastAccess = 600, timeUnit = TimeUnit.SECONDS)
	
	public List<AppProjectListCustomize> searchProjectList(Map<String, Object> projectMap) {

		List<AppProjectListCustomize> projectList = new ArrayList<AppProjectListCustomize>();

		//查询首页定时发标,出借中,复审中的项目
		List<AppProjectListCustomize> list = appProjectListCustomizeMapper.selectHomeProjectList(projectMap);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size() && i < 5; i++) {
				AppProjectListCustomize appProjec = list.get(i);
				projectList.add(appProjec);
			}
		}
		//查询还款中的项目
		if (projectList.size() < 5) {
			List<AppProjectListCustomize> listRepay = appProjectListCustomizeMapper.selectHomeRepayProjectList(projectMap);
			if (listRepay != null && listRepay.size() > 0) {
				for (int i = 0; i < 5 && projectList.size() < 5 && i < listRepay.size(); i++) {
					AppProjectListCustomize repay = listRepay.get(i);
					if (repay != null) {
						projectList.add(repay);
					}
				}
			}
		}
//		// add APP1.3.5改版 by zhangjp end
//		
//		String statusOnTime = "10";
//		projectMap.put("status", statusOnTime);
//		// 查询首页定时发标的项目
//		List<AppProjectListCustomize> listOnTime = appProjectListCustomizeMapper.selectProjectList(projectMap);
//		String statusInvest = "11";
//		projectMap.put("status", statusInvest);
//		// 查询首页出借中的项目
//		List<AppProjectListCustomize> listInvest = appProjectListCustomizeMapper.selectProjectList(projectMap);
//		if (listOnTime != null && listOnTime.size() > 0) {
//			for (int i = 0; i < listOnTime.size(); i++) {
//				AppProjectListCustomize onTime = listOnTime.get(i);
//				projectList.add(onTime);
//			}
//		}
//		if (listInvest != null && listInvest.size() > 0) {
//			for (int i = 0; i < listInvest.size(); i++) {
//				AppProjectListCustomize invest = listInvest.get(i);
//				if (invest != null) {
//					projectList.add(invest);
//				}
//			}
//		}
//		// 当定时发标和正在出借的项目小于4时，获取复审中的标
//		if (projectList.size() < 5) {
//			String statusRepay = "12";
//			projectMap.put("status", statusRepay);
//			List<AppProjectListCustomize> listVerify = appProjectListCustomizeMapper.selectProjectList(projectMap);
//			if (listVerify != null && listVerify.size() > 0) {
//				for (int i = 0; i < 5 && projectList.size() < 5 && i < listVerify.size(); i++) {
//					AppProjectListCustomize verify = listVerify.get(i);
//					if (verify != null) {
//						projectList.add(verify);
//					}
//				}
//			}
//		}
//		// 当定时发标和正在出借的项目小于4时，获取还款中的标
//		if (projectList.size() < 5) {
//			String statusRepay = "13";
//			projectMap.put("status", statusRepay);
//			List<AppProjectListCustomize> listRepay = appProjectListCustomizeMapper.selectProjectList(projectMap);
//			if (listRepay != null && listRepay.size() > 0) {
//				for (int i = 0; i < 5 && projectList.size() < 5 && i < listRepay.size(); i++) {
//					AppProjectListCustomize repay = listRepay.get(i);
//					if (repay != null) {
//						projectList.add(repay);
//					}
//				}
//			}
//		}
//		// add APP1.3.5改版 by zhangjp start
//		// 首页项目列表显示不超过5条数据
//		if(projectList.size() > 5){
//			projectList = projectList.subList(0, 5);
//		}
//		// add APP1.3.5改版 by zhangjp end
		return projectList;
	}

	/***
	 * 查询首页项目列表 3.0.9
	 */
	@Override
	@Cached(name="appHomeProjectList_newCache-", expire = CustomConstants.HOME_CACHE_LIVE_TIME, cacheType = CacheType.BOTH)
	@CacheRefresh(refresh = 5, stopRefreshAfterLastAccess = 600, timeUnit = TimeUnit.SECONDS)
	public List<AppProjectListCustomize> searchProjectList_new(Map<String, Object> projectMap) {
		List<AppProjectListCustomize> projectList = new ArrayList<AppProjectListCustomize>();

		//查询首页定时发标,出借中,复审中的项目
		List<AppProjectListCustomize> list = appProjectListCustomizeMapper.selectHomeProjectList(projectMap);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				AppProjectListCustomize appProjec = list.get(i);
				String status = appProjec.getStatus();
				if("10".equals(status) || "11".equals(status)){
					projectList.add(appProjec);
				}
			}
		}
		return projectList;
	}

	@Override
	@Cached(name="appHomeProjectNewListCache-", expire = CustomConstants.HOME_CACHE_LIVE_TIME, cacheType = CacheType.BOTH)
	@CacheRefresh(refresh = 5, stopRefreshAfterLastAccess = 600, timeUnit = TimeUnit.SECONDS)
    public List<AppProjectListCustomize> searchProjectNewList(Map<String, Object> projectMap) {
	    List<AppProjectListCustomize> projectList = new ArrayList<AppProjectListCustomize>();
		Map<String, Object> projectNewMap = new HashMap<String, Object>();
		// 取得新手汇项目（出借中）
		String statusNewInvest = "15";
		projectNewMap.put("status", statusNewInvest);
		projectNewMap.put("type", "4");
		projectNewMap.put("host", projectMap.get("host"));
		// 查询首页定时发标的项目
		List<AppProjectListCustomize> listNewInvest = appProjectListCustomizeMapper.selectProjectList(projectNewMap);
		if (listNewInvest != null && listNewInvest.size() > 0) {
			for (int i = 0; i < listNewInvest.size(); i++) {
				AppProjectListCustomize newInvest = listNewInvest.get(i);
				if(!Validator.isIncrease(newInvest.getIncreaseInterestFlag(), newInvest.getBorrowExtraYieldOld())){
				    newInvest.setBorrowExtraYield("");
                }
				projectList.add(newInvest);
			}
		}
        // 新手汇项目（出借中）为空
       if(projectList.size() == 0 ){
		   // 取得新手汇项目（定时发标）
		   String statusNewOnTime = "14";
		   projectNewMap.put("status", statusNewOnTime);
		   // 查询首页定时发标的项目
		   List<AppProjectListCustomize> listNewOnTime = appProjectListCustomizeMapper.selectProjectList(projectNewMap);
		   if (listNewOnTime != null && listNewOnTime.size() > 0) {
			   for (int i = 0; i < listNewOnTime.size(); i++) {
				   AppProjectListCustomize newOnTime = listNewOnTime.get(i);
				   if(!Validator.isIncrease(newOnTime.getIncreaseInterestFlag(), newOnTime.getBorrowExtraYieldOld())){
                       newOnTime.setBorrowExtraYield("");
                   }
				   projectList.add(newOnTime);
			   }
		   }
        }

		//复审
		if(projectList.size()==0){
			String status = "16";
			projectNewMap.put("status", status);
			List<AppProjectListCustomize> reviewList = appProjectListCustomizeMapper.selectProjectList(projectNewMap);
			if (reviewList != null && reviewList.size() > 0) {
				for (int i = 0; i < reviewList.size(); i++) {
					AppProjectListCustomize newOnTime = reviewList.get(i);
					if(!Validator.isIncrease(newOnTime.getIncreaseInterestFlag(), newOnTime.getBorrowExtraYieldOld())){
                        newOnTime.setBorrowExtraYield("");
                    }
					projectList.add(newOnTime);
				}
			}
		}
		//还款
		if(projectList.size()==0){
			String status = "17";
			projectNewMap.put("status", status);
			List<AppProjectListCustomize> repaymentList = appProjectListCustomizeMapper.selectProjectList(projectNewMap);
			if (repaymentList != null && repaymentList.size() > 0) {
				for (int i = 0; i < repaymentList.size(); i++) {
					AppProjectListCustomize newOnTime = repaymentList.get(i);
					if(!Validator.isIncrease(newOnTime.getIncreaseInterestFlag(), newOnTime.getBorrowExtraYieldOld())){
					    newOnTime.setBorrowExtraYield("");
					}
					projectList.add(newOnTime);
				}
			}
		}

	    return projectList;
	}

	/**
	 * 获取累计出借数据
	 * @return
	 * @author Michael
	 */

	public WebHomePageStatisticsCustomize searchTotalStatistics() {
		WebHomePageStatisticsCustomize homeStatistics = webHomePageCustomizeMapper.countTotalStatistics();
		return homeStatistics;
	}

	@Override
	public Map<String, Object> selectData() {
		 return dataCustomizeMapper.selectData();
	}

	@Override
	public List<Map<String, Object>> selectTenderListMap(int type) {
		return dataCustomizeMapper.selectTenderListMap(type);
	}

	/**
	 * 获取资质文件数据
	 * @return
	 * @author Michael
	 */
	@Override
	public List<ContentQualify> getCompanyQualify() {
		ContentQualifyExample example = new ContentQualifyExample();
		ContentQualifyExample.Criteria cra = example.createCriteria();
		cra.andStatusEqualTo(1);//启用状态
		return contentQualifyMapper.selectByExample(example);
	}

	/**
	 * 获取400电话
	 * @return
	 * @author Michael
	 */
	@Override
	public String getServicePhoneNumber() {
		String phoneNumber = "";
		List<SiteSetting> list = siteSettingMapper.selectByExample(new SiteSettingExample());
		if(list != null && list.size() > 0){
			if(StringUtils.isNotEmpty(list.get(0).getServicePhoneNumber())){
				phoneNumber = list.get(0).getServicePhoneNumber();
			}
		}
		return phoneNumber;
	}


	/**
	 *
	 * 查询当前设备当天请求次数
	 * @author hsy
	 * @param uniqueIdentifier
	 * @param userId
	 * @return
	 */
	@Override
    public Integer updateCurrentDayRequestTimes(String uniqueIdentifier, Integer userId){
	    // 如果没有收到设备唯一码在返回0次
	    if(StringUtils.isEmpty(uniqueIdentifier)){
	        return 0;
	    }

	    UserDeviceUniqueCodeExample example = new UserDeviceUniqueCodeExample();
	    example.createCriteria().andUniqueIdentifierEqualTo(uniqueIdentifier);

	    List<UserDeviceUniqueCode> recordList = userDeviceUniqueCodeMapper.selectByExample(example);

	    if(recordList == null || recordList.isEmpty()){
	        // 没有对应的记录则插入
	        UserDeviceUniqueCode record = new UserDeviceUniqueCode();
	        record.setCurrentDay(GetDate.formatDate());
	        record.setRequestTimesDay(1);
	        record.setUniqueIdentifier(uniqueIdentifier);
	        record.setUserId(userId);
	        record.setAddTime(GetDate.getNowTime10());
	        record.setUpdateTime(GetDate.getNowTime10());
	        userDeviceUniqueCodeMapper.insertSelective(record);

	        return 0;
	    }else if(!recordList.get(0).getCurrentDay().equals(GetDate.formatDate())){
	        // 如果不是当前日期则更新
	        UserDeviceUniqueCode record = recordList.get(0);
	        record.setCurrentDay(GetDate.formatDate());
            record.setRequestTimesDay(1);
            record.setUserId(userId);
            record.setUpdateTime(GetDate.getNowTime10());
            userDeviceUniqueCodeMapper.updateByPrimaryKeySelective(record);

            return 0;
	    }else{
	        // 返回当前已访问次数并更新
	        UserDeviceUniqueCode record = recordList.get(0);
	        int currentTimes = record.getRequestTimesDay() + 1;
            record.setRequestTimesDay(currentTimes);
            record.setUserId(userId);
            record.setUpdateTime(GetDate.getNowTime10());
            userDeviceUniqueCodeMapper.updateByPrimaryKeySelective(record);

	        return currentTimes;
	    }
	}

	/**
	 * 汇计划列表查询 - 首页显示
	 */
	@Override
	@Cached(name="appHomeIndexHjhPlanListCache-", expire = CustomConstants.HOME_CACHE_LIVE_TIME, cacheType = CacheType.BOTH)
	@CacheRefresh(refresh = 5, stopRefreshAfterLastAccess = 600, timeUnit = TimeUnit.SECONDS)
	public List<HjhPlanCustomize> searchIndexHjhPlanList(Map<String, Object> params) {
		List<HjhPlanCustomize> hjhPlanList = this.hjhPlanCustomizeMapper.selectIndexHjhPlanList(params);
		return hjhPlanList;
	}
	
	/**
     * 首页汇计划推广计划列表 - 首页显示
     */
    @Override
    public List<HjhPlanCustomize> searchIndexHjhExtensionPlanList(Map<String, Object> params) {
        List<HjhPlanCustomize> hjhPlanList = this.hjhPlanCustomizeMapper.selectIndexHjhExtensionPlanList(params);
        return hjhPlanList;
    }

	/**
	 * 首页汇计划推广计划列表 - 首页显示 ②	若没有可投计划，则显示锁定期限短的
	 */
	@Override
	public List<HjhPlanCustomize> selectIndexHjhExtensionPlanListByLockTime(Map<String, Object> params) {
		List<HjhPlanCustomize> hjhPlanList = this.hjhPlanCustomizeMapper.selectIndexHjhExtensionPlanListByLockTime(params);
		return hjhPlanList;
	}

	@Override
	public BigDecimal selectTotalInvest() {
		TotalInvestAndInterestEntity entity = mongoDao.findOne(new Query());
		if (entity != null) {
			return entity.getTotalInvestAmount();
		}
		return BigDecimal.ZERO;
	}

	/**
	 * 获得有效的公告内容
	 * @return
	 */
	@Override
	public List<AppPushManage> getAnnouncenments() {
		AppPushManageExample example = new AppPushManageExample();
		AppPushManageExample.Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(1);
		criteria.andTimeStartLessThanOrEqualTo(GetDate.getNowTime10());
		criteria.andTimeEndGreaterThanOrEqualTo(GetDate.getNowTime10());
		example.setOrderByClause(" `order` asc ,create_time desc");
		List<AppPushManage> pushManageList = this.appPushManageMapper.selectByExample(example);
		if(pushManageList != null && pushManageList.size() > 0){
			return pushManageList;
		}
		return null;
	}

	@Override
	public Integer getUserTypeByUserId(Integer userId) {
		return usersMapper.selectByPrimaryKey(userId).getBankOpenAccount();
	}

	//获取资产总额
	@Override
	public String getTotalAssets(Integer userId) {
	    AccountExample accountExample = new AccountExample();
        AccountExample.Criteria criteria = accountExample.createCriteria();
        criteria.andUserIdEqualTo(userId);
        List<Account> accounts = accountMapper.selectByExample(accountExample);
        if (accounts != null && accounts.size() > 0) {
             return String.valueOf(accounts.get(0).getBankTotal());
        }
        return null;
	}

	//获取可用余额
	@Override
	public String getaVailableBalance(Integer userId) {
        AccountExample accountExample = new AccountExample();
        AccountExample.Criteria criteria = accountExample.createCriteria();
        criteria.andUserIdEqualTo(userId);
        List<Account> accounts = accountMapper.selectByExample(accountExample);
        if (accounts != null && accounts.size() > 0) {
            return String.valueOf(accounts.get(0).getBankBalance());
        }
        return null;
	}

	//获取累计收益
	@Override
	public String getaccumulatedEarnings(Integer userId) {
	    AccountExample accountExample = new AccountExample();
        AccountExample.Criteria criteria = accountExample.createCriteria();
        criteria.andUserIdEqualTo(userId);
        List<Account> accounts = accountMapper.selectByExample(accountExample);
		if (accounts != null && accounts.size() > 0) {
            return String.valueOf(accounts.get(0).getBankInterestSum());
        }
		return null;
	}

	//获取优惠券数量
	@Override
	public String getCoupons(Integer userId) {
        Map<String ,Object> params=new HashMap<String ,Object>();
        params.put("userId", userId);
        params.put("usedFlag", "0");
        int total=couponUserListCustomizeMapper.countCouponUserList(params);
        return String.valueOf(total);
	}

	//获取累计出借金额
	@Override
	public String getTotalInvestmentAmount(Integer userId) {
		return String.valueOf(calculateInvestInterestMapper.selectByPrimaryKey(1).getTenderSum());
	}

	//获取项目可投金额
    @Override
    public String getBorrowAccountWait(String borrowNid) {
	    BorrowExample borrowExample = new BorrowExample();
        BorrowExample.Criteria criteria = borrowExample.createCriteria();
        criteria.andBorrowNidEqualTo(borrowNid);
        return String.valueOf(borrowMapper.selectByExample(borrowExample).get(0).getBorrowAccountWait());
    }

	/**
	 * 查询汇计划余额
	 * @param borrowNid
	 * @return
	 */
	@Override
	public String getHJHAccountWait(String borrowNid) {
		HjhPlanExample example = new HjhPlanExample();
		HjhPlanExample.Criteria criteria = example.createCriteria();
		criteria.andPlanNidEqualTo(borrowNid);
		List<HjhPlan> hjhPlans = hjhPlanMapper.selectByExample(example);
		if(hjhPlans != null && hjhPlans.size() > 0){
			return String.valueOf(hjhPlans.get(0).getAvailableInvestAccount());
		}else{
			return null;
		}
	}


    @Override
/*	@Cached(name="appFindNoticeCache-", expire = CustomConstants.HOME_CACHE_LIVE_TIME, cacheType = CacheType.BOTH)
	@CacheRefresh(refresh = 60, stopRefreshAfterLastAccess = 60, timeUnit = TimeUnit.SECONDS)*/
    public List<ContentArticle> searchHomeNoticeList(String noticeType,int limitStart, int limitEnd) {
        // TODO Auto-generated method stub
//        ContentArticleExample example = new ContentArticleExample();
//        if (offset != -1) {
//            example.setLimitStart(offset);
//            example.setLimitEnd(limit);
//        }
//        ContentArticleExample.Criteria crt = example.createCriteria();
//        crt.andTypeEqualTo(noticeType);
//        crt.andStatusEqualTo(1);
//        example.setOrderByClause("create_time Desc");
    	
    	ContentArticleCustomize example = new ContentArticleCustomize();
		if (limitStart != -1) {
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}
		example.setStatus(1);
		example.setType(noticeType);
        List<ContentArticle> contentArticles = contentArticleCustomizeMapper.selectContentArticleCustomize(example);
        return contentArticles;
    }

    /**
     * 获取用户累计出借条数
     * @param userId
     * @return
     *
     *
     */
    @Override
    public Integer selectInvestCount(Integer userId) {
        Integer count = borrowCustomizeMapper.countInvest(userId);
        return count;
    }

	/**
	 * 获取评分标准列表
	 * @return
	 * @author Michael
	 */
	@Override
	public List<EvalationCustomize> getEvalationRecord() {
		EvalationExampleCustomize example = new EvalationExampleCustomize();
		example.createCriteria().andStatusEqualTo(0);
		// 获取评分标准对应的上限金额并拼接
		List<EvalationCustomize> eval = evalationCustomizeMapper.selectByExample(example);
		for(EvalationCustomize evalStr : eval){
			switch (evalStr.getType()){
				case "保守型":
					evalStr.setRevaluationMoney(StringUtil.getTenThousandOfANumber(
							Double.valueOf(RedisUtils.get(RedisConstants.REVALUATION_CONSERVATIVE)).intValue() == 0.0D ? 0 :
									Double.valueOf(RedisUtils.get(RedisConstants.REVALUATION_CONSERVATIVE)).intValue()));
					break;
				case "稳健型":
					evalStr.setRevaluationMoney(StringUtil.getTenThousandOfANumber(
							Double.valueOf(RedisUtils.get(RedisConstants.REVALUATION_ROBUSTNESS)).intValue() == 0.0D ? 0:
									Double.valueOf(RedisUtils.get(RedisConstants.REVALUATION_ROBUSTNESS)).intValue()));
					break;
				case "成长型":
					evalStr.setRevaluationMoney(StringUtil.getTenThousandOfANumber(
							Double.valueOf(RedisUtils.get(RedisConstants.REVALUATION_GROWTH)).intValue() == 0.0D ? 0:
									Double.valueOf(RedisUtils.get(RedisConstants.REVALUATION_GROWTH)).intValue()));
					break;
				case "进取型":
					evalStr.setRevaluationMoney(StringUtil.getTenThousandOfANumber(
							Double.valueOf(RedisUtils.get(RedisConstants.REVALUATION_AGGRESSIVE)).intValue() == 0.0D ? 0:
									Double.valueOf(RedisUtils.get(RedisConstants.REVALUATION_AGGRESSIVE)).intValue()));
					break;
				default:
					evalStr.setRevaluationMoney("0");
			}
		}
		return eval;
	}

}
