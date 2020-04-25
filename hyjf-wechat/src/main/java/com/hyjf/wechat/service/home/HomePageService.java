/**
 * 首页service接口
 * @package com.hyjf.app.home
 * @author 王坤
 * @date 2016/02/05 10:30
 * @version V1.0  
 */
package com.hyjf.wechat.service.home;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.auto.ContentArticle;
import com.hyjf.mybatis.model.auto.ContentQualify;
import com.hyjf.mybatis.model.customize.app.AppAdsCustomize;
import com.hyjf.mybatis.model.customize.app.AppBorrowImageCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectListCustomize;
import com.hyjf.mybatis.model.customize.web.WebHomePageStatisticsCustomize;
import com.hyjf.mybatis.model.customize.web.hjh.HjhPlanCustomize;
import com.hyjf.wechat.base.BaseService;
import com.hyjf.wechat.controller.home.HomePageResultVo;

public interface HomePageService extends BaseService {

	/**
	 * 查询首页banner(加缓存)
	 * @param ads
	 * @return
	 */
	List<AppAdsCustomize> searchBannerList(Map<String, Object> ads);

	/**
	 * 查询首页项目类型列表
	 * @return
	 */
	List<AppBorrowImageCustomize> searchProjectTypeList(Map<String, Object> projectType);

	/**
	 * 查询项目列表
	 * @param projectMap
	 * @return
	 */
	List<AppProjectListCustomize> searchProjectList(Map<String, Object> projectMap);
	
	/**
	 * 获取累计出借数据
	 * @return
	 */
	WebHomePageStatisticsCustomize searchTotalStatistics();

	/**
	 * 获取累计出借数据
	 * @return
	 */
	Map<String, Object> selectData();

	/**
	 * 
	 * @method: selectTenderListMap
	 * @description: 	搜索月出借金额		
	 * @param type 0出借  1债转出借
	 */
	List<Map<String, Object>> selectTenderListMap(int type);
	
	/**
	 * 获取资质文件数据
	 * @return
	 */
	List<ContentQualify> getCompanyQualify();
	
	
	/**
	 * 获取400电话
	 * @return
	 */
	String getServicePhoneNumber();

	/**
	 * 获取新手标数据(加缓存)
	 * @author hsy
	 * @param info
	 */
    List<AppProjectListCustomize> searchProjectNewList(Map<String, Object> projectMap);

//    Integer updateCurrentDayRequestTimes(Integer userId);

	/**
	 * 汇计划列表查询 - 首页显示 和按照类型查询在排序上略有区别
	 * @param params
	 * @return
	 */
	List<HjhPlanCustomize> searchIndexHjhPlanList(Map<String, Object> params);

	/**
	 * 判断用户是否开户
	 * 0.未开户	1.已开户
	 * @param userId
	 * @return
	 */
	Integer getUserTypeByUserId(Integer userId);


	String getTotalAssets(Integer userId);

	String getaVailableBalance(Integer userId);

	String getaccumulatedEarnings(Integer userId);

	String getCoupons(Integer userId);

	String getTotalInvestmentAmount(Integer userId);

	String getBorrowAccountWait(String borrowNid);

	/**
	 * 汇计划余额
	 * @param borrowNid
	 * @return
	 */
	String getHJHAccountWait(String borrowNid);
	
	//从 web应用拷贝的接口，用于展示发布的信息
    List<ContentArticle> searchHomeNoticeList(String noticeType, int offset, int limit);

    /**
     * 获取用户累计出借条数
     * @param userId
     * @return
     */
    Integer selectInvestCount(Integer userId);
    
    /**
     * 异步获取projectList add by jijun 20180412(加缓存)
     * @param currentPage
     * @param pageSize
     * @param showPlanFlag 
     * @return
     */
    HomePageResultVo getProjectListAsyn(HomePageResultVo vo,int currentPage, int pageSize, String showPlanFlag);

    
}
