/**
 * 首页service接口
 * @package com.hyjf.app.home
 * @author 王坤
 * @date 2016/02/05 10:30
 * @version V1.0  
 */
package com.hyjf.app.home;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.hyjf.app.BaseService;
import com.hyjf.mybatis.model.auto.AppPushManage;
import com.hyjf.mybatis.model.auto.ContentArticle;
import com.hyjf.mybatis.model.auto.ContentQualify;
import com.hyjf.mybatis.model.customize.EvalationCustomize;
import com.hyjf.mybatis.model.customize.app.AppAdsCustomize;
import com.hyjf.mybatis.model.customize.app.AppBorrowImageCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectListCustomize;
import com.hyjf.mybatis.model.customize.web.WebHomePageStatisticsCustomize;
import com.hyjf.mybatis.model.customize.web.hjh.HjhPlanCustomize;

public interface HomePageService extends BaseService {

	/**
	 * 查询首页banner(加缓存)
	 * @param ads
	 * @return
	 */
	List<AppAdsCustomize> searchBannerList(Map<String, Object> ads);

	/**
	 * 查询首页项目类型列表(controller里面的调用已经被注掉，此查询暂时没用)
	 * @return
	 */
	List<AppBorrowImageCustomize> searchProjectTypeList(Map<String, Object> projectType);

	/**
	 * 查询项目列表(加缓存)
	 * @param projectMap
	 * @return
	 */
	List<AppProjectListCustomize> searchProjectList(Map<String, Object> projectMap);

	/**
	 * 查询项目列表_app3.0.9(加缓存)
	 * @param projectMap
	 * @return
	 */
	List<AppProjectListCustomize> searchProjectList_new(Map<String, Object> projectMap);
	
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

    boolean checkNewUser(Object userId);

	/**
	 *
	 * 获取新手标数据(加缓存)
	 * @author hsy
	 * @param info
	 */
    List<AppProjectListCustomize> searchProjectNewList(Map<String, Object> projectMap);

    Integer updateCurrentDayRequestTimes(String uniqueIdentifier, Integer userId);

	/**
	 * 汇计划列表查询 - 首页显示 和按照类型查询在排序上略有区别(加缓存)
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
     * 
     * 首页汇计划推广计划列表
     * @author pcc
     * @param params
     * @return
     */
    List<HjhPlanCustomize> searchIndexHjhExtensionPlanList(Map<String, Object> params);

	public List<HjhPlanCustomize> selectIndexHjhExtensionPlanListByLockTime(Map<String, Object> params);

	/**
	 * 查询累计出借
	 * @return
	 */
	BigDecimal selectTotalInvest();

	/**
	 * 获取有效公告信息
	 * @return
	 */
    public List<AppPushManage> getAnnouncenments();

    /**
	 * 测评说明
	 * @return
	 */
	List<EvalationCustomize> getEvalationRecord();

}
