/**
 * 首页service接口实现
 * @package com.hyjf.app.home
 * @author 王坤
 * @date 2016/02/05 10:30
 * @version V1.0  
 */
package com.hyjf.wechat.service.home;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.hyjf.common.util.CommonUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.ContentArticle;
import com.hyjf.mybatis.model.auto.ContentQualify;
import com.hyjf.mybatis.model.auto.ContentQualifyExample;
import com.hyjf.mybatis.model.auto.HjhPlan;
import com.hyjf.mybatis.model.auto.HjhPlanExample;
import com.hyjf.mybatis.model.auto.SiteSetting;
import com.hyjf.mybatis.model.auto.SiteSettingExample;
import com.hyjf.mybatis.model.customize.ContentArticleCustomize;
import com.hyjf.mybatis.model.customize.app.AppAdsCustomize;
import com.hyjf.mybatis.model.customize.app.AppBorrowImageCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectListCustomize;
import com.hyjf.mybatis.model.customize.web.WebHomePageStatisticsCustomize;
import com.hyjf.mybatis.model.customize.web.hjh.HjhPlanCustomize;
import com.hyjf.mybatis.model.customize.wechat.WechatHomeProjectListCustomize;
import com.hyjf.wechat.base.BaseServiceImpl;
import com.hyjf.wechat.controller.home.HomePageResultVo;

import com.alicp.jetcache.anno.CachePenetrationProtect;
import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.hyjf.common.util.CustomConstants;
import java.util.concurrent.TimeUnit;

@Service
public class HomePageServiceImpl extends BaseServiceImpl implements HomePageService {

	/**
	 * 查询首页的bannner列表
	 */
	@Override
	@Cached(name="wechatHomeBannerCache-", expire = CustomConstants.HOME_CACHE_LIVE_TIME, cacheType = CacheType.BOTH)
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

		List<AppBorrowImageCustomize> borrowImageList = appBorrowImageCustomizeMapper
				.selectBorrowImageList(projectType);
		for (AppBorrowImageCustomize appBorrowImageCustomize : borrowImageList) {
			if (appBorrowImageCustomize.getPageType().equals("0")) {
				appBorrowImageCustomize.setTypeType(appBorrowImageCustomize.getJumpName());
				appBorrowImageCustomize.setTypeUrl("");
			} else if (appBorrowImageCustomize.getPageType().equals("1")) {
				appBorrowImageCustomize.setTypeType("");
			} else if (appBorrowImageCustomize.getPageType().equals("2")) {
				appBorrowImageCustomize.setTypeType("");
				appBorrowImageCustomize.setTypeUrl("");
			}
		}
		return borrowImageList;
	}

	/***
	 * 查询首页项目列表
	 */
	@Override
	public List<AppProjectListCustomize> searchProjectList(Map<String, Object> projectMap) {

		List<AppProjectListCustomize> projectList = new ArrayList<AppProjectListCustomize>();

		// 查询首页定时发标,出借中,复审中的项目
		List<AppProjectListCustomize> list = appProjectListCustomizeMapper.selectHomeProjectList(projectMap);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size() && i < 5; i++) {
				AppProjectListCustomize appProjec = list.get(i);
				projectList.add(appProjec);
			}
		}
		// 查询还款中的项目
		if (projectList.size() < 5) {
			List<AppProjectListCustomize> listRepay = appProjectListCustomizeMapper
					.selectHomeRepayProjectList(projectMap);
			if (listRepay != null && listRepay.size() > 0) {
				for (int i = 0; i < 5 && projectList.size() < 5 && i < listRepay.size(); i++) {
					AppProjectListCustomize repay = listRepay.get(i);
					if (repay != null) {
						projectList.add(repay);
					}
				}
			}
		}
		// // add APP1.3.5改版 by zhangjp end
		//
		// String statusOnTime = "10";
		// projectMap.put("status", statusOnTime);
		// // 查询首页定时发标的项目
		// List<AppProjectListCustomize> listOnTime =
		// appProjectListCustomizeMapper.selectProjectList(projectMap);
		// String statusInvest = "11";
		// projectMap.put("status", statusInvest);
		// // 查询首页出借中的项目
		// List<AppProjectListCustomize> listInvest =
		// appProjectListCustomizeMapper.selectProjectList(projectMap);
		// if (listOnTime != null && listOnTime.size() > 0) {
		// for (int i = 0; i < listOnTime.size(); i++) {
		// AppProjectListCustomize onTime = listOnTime.get(i);
		// projectList.add(onTime);
		// }
		// }
		// if (listInvest != null && listInvest.size() > 0) {
		// for (int i = 0; i < listInvest.size(); i++) {
		// AppProjectListCustomize invest = listInvest.get(i);
		// if (invest != null) {
		// projectList.add(invest);
		// }
		// }
		// }
		// // 当定时发标和正在出借的项目小于4时，获取复审中的标
		// if (projectList.size() < 5) {
		// String statusRepay = "12";
		// projectMap.put("status", statusRepay);
		// List<AppProjectListCustomize> listVerify =
		// appProjectListCustomizeMapper.selectProjectList(projectMap);
		// if (listVerify != null && listVerify.size() > 0) {
		// for (int i = 0; i < 5 && projectList.size() < 5 && i < listVerify.size();
		// i++) {
		// AppProjectListCustomize verify = listVerify.get(i);
		// if (verify != null) {
		// projectList.add(verify);
		// }
		// }
		// }
		// }
		// // 当定时发标和正在出借的项目小于4时，获取还款中的标
		// if (projectList.size() < 5) {
		// String statusRepay = "13";
		// projectMap.put("status", statusRepay);
		// List<AppProjectListCustomize> listRepay =
		// appProjectListCustomizeMapper.selectProjectList(projectMap);
		// if (listRepay != null && listRepay.size() > 0) {
		// for (int i = 0; i < 5 && projectList.size() < 5 && i < listRepay.size(); i++)
		// {
		// AppProjectListCustomize repay = listRepay.get(i);
		// if (repay != null) {
		// projectList.add(repay);
		// }
		// }
		// }
		// }
		// // add APP1.3.5改版 by zhangjp start
		// // 首页项目列表显示不超过5条数据
		// if(projectList.size() > 5){
		// projectList = projectList.subList(0, 5);
		// }
		// // add APP1.3.5改版 by zhangjp end
		return projectList;
	}
	
	/**
	 *
	 * 获取新手标数据
	 * @author hsy
	 * @param info
	 */
	@Override
	@Cached(name="wechatHomeProjectNewListCache-", expire = CustomConstants.HOME_CACHE_LIVE_TIME, cacheType = CacheType.BOTH)
	@CacheRefresh(refresh = 5, stopRefreshAfterLastAccess = 600, timeUnit = TimeUnit.SECONDS)
	public List<AppProjectListCustomize> searchProjectNewList(Map<String, Object> projectMap) {
		List<AppProjectListCustomize> projectList = new ArrayList<AppProjectListCustomize>();

		// 取得新手汇项目（定时发标）
		Map<String, Object> projectNewMap = new HashMap<String, Object>();
		
		projectNewMap.put("planform", "wx");
		projectNewMap.put("type", "4");
		projectNewMap.put("host", projectMap.get("host"));
		// 查询首页定时发标的项目
		List<AppProjectListCustomize> listNewOnTime = appProjectListCustomizeMapper.selectProjectList(projectNewMap);
		
		
		// 取得新手汇项目（出借中）
		String statusNewInvest = "15";
		projectNewMap.put("status", statusNewInvest);
		// 查询首页定时发标的项目
		List<AppProjectListCustomize> listNewInvest = appProjectListCustomizeMapper
				.selectProjectList(projectNewMap);
		if (listNewInvest != null && listNewInvest.size() > 0) {
			for (int i = 0; i < listNewInvest.size(); i++) {
				AppProjectListCustomize newInvest = listNewInvest.get(i);
				projectList.add(newInvest);
			}
		}
		// 新手汇项目（出借中）为空
		if (projectList.size() == 0) {
			String statusNewOnTime = "14";
			projectNewMap.put("status", statusNewOnTime);
			if (listNewOnTime != null && listNewOnTime.size() > 0) {
				for (int i = 0; i < listNewOnTime.size(); i++) {
					AppProjectListCustomize newOnTime = listNewOnTime.get(i);
					projectList.add(newOnTime);
				}
			}
		}
		return projectList;
	}

	/**
	 * 获取累计出借数据
	 * 
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
	 * 
	 * @return
	 * @author Michael
	 */
	@Override
	public List<ContentQualify> getCompanyQualify() {
		ContentQualifyExample example = new ContentQualifyExample();
		ContentQualifyExample.Criteria cra = example.createCriteria();
		cra.andStatusEqualTo(1);// 启用状态
		return contentQualifyMapper.selectByExample(example);
	}

	/**
	 * 获取400电话
	 * 
	 * @return
	 * @author Michael
	 */
	@Override
	public String getServicePhoneNumber() {
		String phoneNumber = "";
		List<SiteSetting> list = siteSettingMapper.selectByExample(new SiteSettingExample());
		if (list != null && list.size() > 0) {
			if (StringUtils.isNotEmpty(list.get(0).getServicePhoneNumber())) {
				phoneNumber = list.get(0).getServicePhoneNumber();
			}
		}
		return phoneNumber;
	}

	/**
	 * 汇计划列表查询 - 首页显示
	 */
	@Override
	public List<HjhPlanCustomize> searchIndexHjhPlanList(Map<String, Object> params) {
		List<HjhPlanCustomize> hjhPlanList = this.hjhPlanCustomizeMapper.selectIndexHjhPlanList(params);
		return hjhPlanList;
	}

	@Override
	public Integer getUserTypeByUserId(Integer userId) {
		return usersMapper.selectByPrimaryKey(userId).getBankOpenAccount();
	}

	// 获取资产总额
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

	// 获取可用余额
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

	// 获取累计收益
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

	// 获取优惠券数量
	@Override
	public String getCoupons(Integer userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("usedFlag", "0");
		int total = couponUserListCustomizeMapper.countCouponUserList(params);
		return String.valueOf(total);
	}

	// 获取累计出借金额
	@Override
	public String getTotalInvestmentAmount(Integer userId) {
		return String.valueOf(calculateInvestInterestMapper.selectByPrimaryKey(1).getTenderSum());
	}

	// 获取项目可投金额
	@Override
	public String getBorrowAccountWait(String borrowNid) {
		BorrowExample borrowExample = new BorrowExample();
		BorrowExample.Criteria criteria = borrowExample.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		return String.valueOf(borrowMapper.selectByExample(borrowExample).get(0).getBorrowAccountWait());
	}

	/**
	 * 查询汇计划余额
	 * 
	 * @param borrowNid
	 * @return
	 */
	@Override
	public String getHJHAccountWait(String borrowNid) {
		HjhPlanExample example = new HjhPlanExample();
		HjhPlanExample.Criteria criteria = example.createCriteria();
		criteria.andPlanNidEqualTo(borrowNid);
		List<HjhPlan> hjhPlans = hjhPlanMapper.selectByExample(example);
		return String.valueOf(hjhPlans.get(0).getAvailableInvestAccount());
	}

	@Override
	public List<ContentArticle> searchHomeNoticeList(String noticeType, int limitStart, int limitEnd) {
		// TODO Auto-generated method stub
		// ContentArticleExample example = new ContentArticleExample();
		// if (offset != -1) {
		// example.setLimitStart(offset);
		// example.setLimitEnd(limit);
		// }
		// ContentArticleExample.Criteria crt = example.createCriteria();
		// crt.andTypeEqualTo(noticeType);
		// crt.andStatusEqualTo(1);
		// example.setOrderByClause("create_time Desc");

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
	 * 
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
	 * 分页获取首页数据
	 * 
	 * @param request
	 * @return
	 */
	@Override
	@Cached(name="wechatProjectListAsynCache-", expire = CustomConstants.HOME_CACHE_LIVE_TIME, cacheType = CacheType.BOTH)
	@CacheRefresh(refresh = 5, stopRefreshAfterLastAccess = 600, timeUnit = TimeUnit.SECONDS)
	public HomePageResultVo getProjectListAsyn(HomePageResultVo vo,int currentPage, int pageSize, String showPlanFlag) {

		List<WechatHomeProjectListCustomize> list=new ArrayList<WechatHomeProjectListCustomize>();
		Map<String, Object> projectMap = new HashMap<String, Object>();
		// 汇盈金服app首页定向标过滤
		projectMap.put("publishInstCode", CustomConstants.HYJF_INST_CODE);
		int offSet = (currentPage - 1) * pageSize;
		if (offSet == 0 || offSet > 0) {
			projectMap.put("limitStart", offSet);
		}
		if (pageSize > 0) {
			projectMap.put("limitEnd", pageSize + 1);
		}
		if(showPlanFlag!=null){
			projectMap.put("showPlanFlag", showPlanFlag);
		}
		list=appProjectListCustomizeMapper.selectHomeProjectListAsyn(projectMap);

		if(!CollectionUtils.isEmpty(list)) {
			if(list.size()==(pageSize+1)){
				list.remove(list.size() - 1);
				// 不是最后一页 每次在每页显示条数的基础上多查一条，然后根据查询结果判断是不是最后一页
				vo.setEndPage(0);
			}else{
				// 是最后一页
				vo.setEndPage(1);
			}
		}else{
			// 是最后一页
			vo.setEndPage(1);
		}
		
		
		if(showPlanFlag==null){
			if(currentPage==1){
				if(list.size()==0){
					//补两条
					List<WechatHomeProjectListCustomize> hjhList=appProjectListCustomizeMapper.selectHomeHjhOpenLaterList();
					hjhList.addAll(list);
					list=hjhList;
				}else if(list.size()>0&&!"HJH".equals(list.get(0).getBorrowType())){
					//补两条
					List<WechatHomeProjectListCustomize> hjhList=appProjectListCustomizeMapper.selectHomeHjhOpenLaterList();
					hjhList.addAll(list);
					list=hjhList;
				}else if(list.size()>1&&!"HJH".equals(list.get(1).getBorrowType())){
					//补一条
					List<WechatHomeProjectListCustomize> hjhList=appProjectListCustomizeMapper.selectHomeHjhOpenLaterList();
					list.add(1, hjhList.get(0));
				}
			}
		}
		if(vo.getEndPage()==1){
			if(list.size()>0&&"HJH".equals(list.get(list.size()-1).getBorrowType())){
				
				List<WechatHomeProjectListCustomize> hjhList=appProjectListCustomizeMapper.selectHomeRepaymentsProjectList();
				list.addAll(hjhList);
				//补两条
			}else if(list.size()>1&&"HJH".equals(list.get(list.size()-2).getBorrowType())){
				List<WechatHomeProjectListCustomize> hjhList=appProjectListCustomizeMapper.selectHomeRepaymentsProjectList();
				list.add(hjhList.get(0));
				//补一条
			}
		}
		
		
		DecimalFormat df = CustomConstants.DF_FOR_VIEW;
		for (WechatHomeProjectListCustomize wechatHomeProjectListCustomize : list) {
			if("HJH".equals(wechatHomeProjectListCustomize.getBorrowType())){
				if("1".equals(wechatHomeProjectListCustomize.getStatus())){
					wechatHomeProjectListCustomize.setStatus("20");
				}else{
					wechatHomeProjectListCustomize.setStatus("21");
				}
			}else{
				
				if("0".equals(wechatHomeProjectListCustomize.getOnTime())||"".equals(wechatHomeProjectListCustomize.getOnTime())){
					switch (wechatHomeProjectListCustomize.getStatus()) {
					case "10":
						wechatHomeProjectListCustomize.setOnTime(wechatHomeProjectListCustomize.getOnTime());
						break;
					case "11":
						wechatHomeProjectListCustomize.setOnTime("立即出借");
						break;
					case "12":
						wechatHomeProjectListCustomize.setOnTime("复审中");
						break;
					case "13":
						wechatHomeProjectListCustomize.setOnTime("还款中");
						break;
					case "14":
						wechatHomeProjectListCustomize.setOnTime("已退出");
						break;
					}
					
				}else{
					wechatHomeProjectListCustomize.setOnTime(wechatHomeProjectListCustomize.getOnTime());
				}
				
				
			}
			wechatHomeProjectListCustomize.setAccountWait(df.format(new BigDecimal(wechatHomeProjectListCustomize.getAccountWait())));
		}
		
		// 字段为null时，转为""
		CommonUtils.convertNullToEmptyString(list);
		vo.setHomeProjectList(list);
		return vo;
	}


}
