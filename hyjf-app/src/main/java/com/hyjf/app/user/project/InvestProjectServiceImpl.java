package com.hyjf.app.user.project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.app.BaseServiceImpl;
import com.hyjf.common.http.HttpClientUtils;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.PropertiesConstants;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.app.*;
import com.hyjf.mybatis.model.customize.web.WebUserInvestListCustomize;

@Service
public class InvestProjectServiceImpl extends BaseServiceImpl implements InvestProjectService {

	@Override
	public List<AppAlreadyRepayListCustomize> selectAlreadyRepayList(Map<String, Object> params) {
		List<AppAlreadyRepayListCustomize> alreadyRepayList = appUserInvestCustomizeMapper
				.selectAlreadyRepayList(params);
		return alreadyRepayList;
	}

	@Override
	public int countAlreadyRepayListRecordTotal(Map<String, Object> params) {
		int total = appUserInvestCustomizeMapper.countAlreadyRepayListRecordTotal(params);
		return total;
	}

	@Override
	public List<AppRepayPlanListCustomize> selectRepayPlanList(Map<String, Object> params) {
		List<AppRepayPlanListCustomize> reapyPlanList = appUserInvestCustomizeMapper.selectRepayRecoverPlanList(params);
		return reapyPlanList;
	}

	@Override
	public int countRepayPlanListRecordTotal(Map<String, Object> params) {
		int total = appUserInvestCustomizeMapper.countRepayRecoverPlanListRecordTotal(params);
		return total;
	}

	@Override
	public Borrow selectBorrowByBorrowNid(String borrowNid) {
		BorrowExample example = new BorrowExample();
		BorrowExample.Criteria crt = example.createCriteria();
		crt.andBorrowNidEqualTo(borrowNid);
		List<Borrow> borrows = borrowMapper.selectByExample(example);
		if (borrows != null && borrows.size() > 0) {
			return borrows.get(0);
		}
		return null;
	}

	@Override
	public BorrowStyle selectBorrowStyleByStyle(String borrowStyle) {

		BorrowStyleExample example = new BorrowStyleExample();
		BorrowStyleExample.Criteria crt = example.createCriteria();
		crt.andNidEqualTo(borrowStyle);
		crt.andStatusEqualTo(0);
		List<BorrowStyle> borrowStyles = borrowStyleMapper.selectByExample(example);
		if (borrowStyles != null && borrowStyles.size() > 0) {
			return borrowStyles.get(0);
		}
		return null;
	}

	@Override
	public int countRepayRecoverListRecordTotal(Map<String, Object> params) {
		int total = appUserInvestCustomizeMapper.countRepayRecoverListRecordTotal(params);
		return total;
	}

	@Override
	public List<AppRepayPlanListCustomize> selectRepayRecoverList(Map<String, Object> params) {
		List<AppRepayPlanListCustomize> reapyPlanList = appUserInvestCustomizeMapper.selectRepayRecoverList(params);
		return reapyPlanList;
	}

	@Override
	public List<AppProjectContractRecoverPlanCustomize> selectProjectContractRecoverPlan(Map<String, Object> params) {
		List<AppProjectContractRecoverPlanCustomize> reapyPlans = appUserInvestCustomizeMapper
				.selectProjectContractRecoverPlan(params);
		return reapyPlans;
	}

	@Override
	public AppProjectContractDetailCustomize selectProjectContractDetail(Map<String, Object> params) {
		AppProjectContractDetailCustomize contractDetail = appUserInvestCustomizeMapper
				.selectProjectContractDetail(params);
		return contractDetail;
	}

	/**
	 * 优惠券还款计划列表总记录数统计
	 */
	@Override
	public int countCouponRepayRecoverListRecordTotal(Map<String, Object> params) {
		int total = appUserInvestCustomizeMapper.countCouponRepayRecoverListRecordTotal(params);
		return total;
	}

	/**
	 * 优惠券还款计划列表
	 */
	@Override
	public List<AppRepayPlanListCustomize> selectCouponRepayRecoverList(Map<String, Object> params) {
		List<AppRepayPlanListCustomize> reapyPlanList = appUserInvestCustomizeMapper
				.selectCouponRepayRecoverList(params);
		return reapyPlanList;
	}

	/**
	 * 优惠券还款计划已得收益
	 * 
	 * @author hsy
	 * @param params
	 * @return
	 */
	@Override
	public String selectReceivedInterest(Map<String, Object> params) {
		String receivedInterest = appUserInvestCustomizeMapper.selectReceivedInterest(params);
		if (receivedInterest == null) {
			return "";
		}

		return receivedInterest;
	}

	/**
	 * 
	 * 回款中项目详情
	 * 
	 * @author liuyang
	 * @param params
	 * @return
	 * @see com.hyjf.app.user.project.InvestProjectService#getRepayDetail(java.util.Map)
	 */
	@Override
	public AppRepayDetailCustomize selectRepayDetail(Map<String, Object> params) {
		AppRepayDetailCustomize appRepayDetailCustomize = this.appUserInvestCustomizeMapper.selectRepayDetail(params);
		return appRepayDetailCustomize;
	}

	/**
	 * 
	 * 优惠券出借项目详情
	 * 
	 * @author liuyang
	 * @param params
	 * @return
	 * @see com.hyjf.app.user.project.InvestProjectService#selectCouponRepayDetail(java.util.Map)
	 */
	@Override
	public AppRepayDetailCustomize selectCouponRepayDetail(Map<String, Object> params) {
		AppRepayDetailCustomize appRepayDetailCustomize = this.appUserInvestCustomizeMapper
				.selectCouponRepayDetail(params);
		return appRepayDetailCustomize;
	}

	/**
	 * 
	 * 获取出借中的项目详情
	 * 
	 * @author liuyang
	 * @param params
	 * @return
	 * @see com.hyjf.app.user.project.InvestProjectService#selectInvestProjectDetail(java.util.Map)
	 */
	@Override
	public AppRepayDetailCustomize selectInvestProjectDetail(Map<String, Object> params) {
		AppRepayDetailCustomize appRepayDetailCustomize = this.appUserInvestCustomizeMapper
				.selectInvestProjectDetail(params);
		return appRepayDetailCustomize;
	}

	/**
	 * 
	 * 获取出借中的优惠券项目详情
	 * 
	 * @author liuyang
	 * @param params
	 * @return
	 * @see com.hyjf.app.user.project.InvestProjectService#selectCouponInvestProjectDetail(java.util.Map)
	 */
	@Override
	public AppRepayDetailCustomize selectCouponInvestProjectDetail(Map<String, Object> params) {
		AppRepayDetailCustomize appRepayDetailCustomize = this.appUserInvestCustomizeMapper
				.selectCouponInvestProjectDetail(params);
		return appRepayDetailCustomize;
	}

	/**
	 * 
	 * 获取回款中的项目详情
	 * 
	 * @author liuyang
	 * @param params
	 * @return
	 * @see com.hyjf.app.user.project.InvestProjectService#selectRepayedProjectDetail(java.util.Map)
	 */
	@Override
	public AppRepayDetailCustomize selectRepayedProjectDetail(Map<String, Object> params) {
		AppRepayDetailCustomize appRepayDetailCustomize = this.appUserInvestCustomizeMapper
				.selectRepayedProjectDetail(params);
		return appRepayDetailCustomize;
	}

	/**
	 * 
	 * 获取回款中的优惠券出借项目详情
	 * 
	 * @author liuyang
	 * @param params
	 * @return
	 * @see com.hyjf.app.user.project.InvestProjectService#selectCouponRepayedProjectDetail(java.util.Map)
	 */
	@Override
	public AppRepayDetailCustomize selectCouponRepayedProjectDetail(Map<String, Object> params) {
		AppRepayDetailCustomize appRepayDetailCustomize = this.appUserInvestCustomizeMapper
				.selectCouponRepayedProjectDetail(params);
		return appRepayDetailCustomize;
	}

	/**
	 * 
	 * 获取出借中的项目列表
	 * 
	 * @author hsy
	 * @param userId
	 * @param host
	 * @return
	 */
	@Override
	public String getInvestingData(String userId, String host, String page, String pageSize) {
		String SOA_INTERFACE_KEY = PropUtils.getSystem("aop.interface.accesskey");
		String GET_INVESTINGDATA = "user/invest/getInvestList.json";

		String timestamp = String.valueOf(GetDate.getNowTime10());
		String chkValue = StringUtils.lowerCase(MD5.toMD5Code(SOA_INTERFACE_KEY + userId + page + pageSize + timestamp
				+ SOA_INTERFACE_KEY));

		Map<String, String> params = new HashMap<String, String>();
		// 时间戳
		params.put("timestamp", timestamp);
		// 签名
		params.put("chkValue", chkValue);
		// userid
		params.put("userId", userId);
		params.put("host", host);
		params.put("page", page);
		params.put("pageSize", pageSize);

		// 请求路径
		String requestUrl = PropUtils.getSystem(PropertiesConstants.HYJF_API_WEB_URL) + GET_INVESTINGDATA;

		// 0:成功，1：失败
		return HttpClientUtils.post(requestUrl, params);

	}

	/**
	 * 
	 * 获取还款中的项目列表
	 * 
	 * @author hsy
	 * @param userId
	 * @param host
	 * @param hostContact
	 * @return
	 */
	@Override
	public String getRepayData(String userId, String host, String hostContact, String page, String pageSize) {
		String SOA_INTERFACE_KEY = PropUtils.getSystem("aop.interface.accesskey");
		String GET_REPAYDATA = "user/invest/getRepayList.json";

		String timestamp = String.valueOf(GetDate.getNowTime10());
		String chkValue = StringUtils.lowerCase(MD5.toMD5Code(SOA_INTERFACE_KEY + userId + page + pageSize + timestamp
				+ SOA_INTERFACE_KEY));

		Map<String, String> params = new HashMap<String, String>();
		// 时间戳
		params.put("timestamp", timestamp);
		// 签名
		params.put("chkValue", chkValue);
		// userid
		params.put("userId", userId);
		params.put("host", host);
		params.put("hostContact", hostContact);
		params.put("page", page);
		params.put("pageSize", pageSize);

		// 请求路径
		String requestUrl = PropUtils.getSystem(PropertiesConstants.HYJF_API_WEB_URL) + GET_REPAYDATA;

		// 0:成功，1：失败
		return HttpClientUtils.post(requestUrl, params);

	}

	/**
	 * 
	 * 获取已还款的项目列表
	 * 
	 * @author hsy
	 * @param userId
	 * @param host
	 * @return
	 */
	@Override
	public String getRepayedData(String userId, String host, String page, String pageSize) {
		String SOA_INTERFACE_KEY = PropUtils.getSystem("aop.interface.accesskey");
		String GET_REPAYEDDATA = "user/invest/getAlreadyRepayList.json";

		String timestamp = String.valueOf(GetDate.getNowTime10());
		String chkValue = StringUtils.lowerCase(MD5.toMD5Code(SOA_INTERFACE_KEY + userId + page + pageSize + timestamp
				+ SOA_INTERFACE_KEY));

		Map<String, String> params = new HashMap<String, String>();
		// 时间戳
		params.put("timestamp", timestamp);
		// 签名
		params.put("chkValue", chkValue);
		// userid
		params.put("userId", userId);
		params.put("host", host);
		params.put("page", page);
		params.put("pageSize", pageSize);

		// 请求路径
		String requestUrl = PropUtils.getSystem(PropertiesConstants.HYJF_API_WEB_URL) + GET_REPAYEDDATA;

		// 0:成功，1：失败
		return HttpClientUtils.post(requestUrl, params);

	}

	/**
	 * 
	 * 根据出借订单号nid获取优惠券配置
	 * 
	 * @author hsy
	 * @param nid
	 * @return
	 */
	@Override
	public CouponConfig getCouponConfigByOrderId(String nid) {
		if (StringUtils.isEmpty(nid)) {
			return null;
		}

		CouponTenderExample tenderExample = new CouponTenderExample();
		tenderExample.createCriteria().andOrderIdEqualTo(nid);
		List<CouponTender> couponTenders = couponTenderMapper.selectByExample(tenderExample);
		if (couponTenders == null || couponTenders.isEmpty()) {
			return null;
		}

		CouponUser couponUser = couponUserMapper.selectByPrimaryKey(couponTenders.get(0).getCouponGrantId());

		if (couponUser == null) {
			return null;
		}

		CouponConfigExample configExample = new CouponConfigExample();
		configExample.createCriteria().andCouponCodeEqualTo(couponUser.getCouponCode());
		List<CouponConfig> configs = couponConfigMapper.selectByExample(configExample);

		if (configs == null || configs.isEmpty()) {
			return null;
		}

		return configs.get(0);
	}

	@Override
	public List<WebUserInvestListCustomize> selectUserInvestList(String borrowNid, Integer userId, String nid,
			int limitStart, int limitEnd) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("borrowNid", borrowNid);
		params.put("userId", userId);
		params.put("nid", nid);
		if (limitStart == 0 || limitStart > 0) {
			params.put("limitStart", limitStart);
		}
		if (limitEnd > 0) {
			params.put("limitEnd", limitEnd);
		}
		List<WebUserInvestListCustomize> list = webUserInvestListCustomizeMapper.selectUserInvestList(params);
		return list;
	}

	/**
	 * 判断用户所在渠道是否允许债转
	 * @param userId
	 * @return
	 */
	@Override
	public boolean isAllowChannelAttorn(Integer userId) {
		// 根据userId获取用户注册推广渠道
		UtmPlat utmPlat = utmPlatCustomizeMapper.selectUtmPlatByUserId(userId);
		if (utmPlat != null && utmPlat.getAttornFlag() == 0)
			return false;
		return true;
	}

}
