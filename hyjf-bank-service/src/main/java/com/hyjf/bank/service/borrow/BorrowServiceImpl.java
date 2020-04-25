/**
 * Description:汇直投查询service接口实现
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.bank.service.borrow;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.hyjf.bank.service.mq.MqService;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mybatis.model.auto.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alicp.jetcache.anno.CachePenetrationProtect;
import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.calculate.CalculatesUtil;
import com.hyjf.common.calculate.InterestInfo;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.customize.coupon.UserCouponConfigCustomize;
import com.hyjf.mybatis.model.customize.web.WebHzcDisposalPlanCustomize;
import com.hyjf.mybatis.model.customize.web.WebHzcProjectDetailCustomize;
import com.hyjf.mybatis.model.customize.web.WebMortgageCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectAuthenInfoCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectCompanyDetailCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectConsumeCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectDetailCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectInvestListCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectListCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectPersonDetailCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectUndertakeListCustomize;
import com.hyjf.mybatis.model.customize.web.WebRiskControlCustomize;
import com.hyjf.mybatis.model.customize.web.WebVehiclePledgeCustomize;

@Service
public class BorrowServiceImpl extends BaseServiceImpl implements BorrowService {
	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	@Autowired
	private MqService mqService;

	Logger _log = LoggerFactory.getLogger(BorrowServiceImpl.class);
	/**
	 * 查询指定项目类型的项目列表
	 */
	@Override
	public List<WebProjectListCustomize> searchProjectList(Map<String, Object> params) {
		List<WebProjectListCustomize> list = webProjectListCustomizeMapper.selectProjectList(params);
		return list;
	}

	/**
	 * 查询指定项目类型的项目数目
	 */
	@Override
	public int countProjectListRecordTotal(Map<String, Object> params) {
		int total = webProjectListCustomizeMapper.countProjectListRecordTotal(params);
		return total;
	}

	/**
	 * 查询个人借款项目的项目详情
	 */
	@Override
	public WebProjectPersonDetailCustomize searchProjectPersonDetail(String borrowNid) {
		WebProjectPersonDetailCustomize htlDetail = webProjectListCustomizeMapper.selectProjectPersonDetail(borrowNid);
		return htlDetail;
	}

	/**
	 * 查询企业项目的项目详情
	 */
	@Override
	public WebProjectCompanyDetailCustomize searchProjectCompanyDetail(String borrowNid) {
		WebProjectCompanyDetailCustomize htlDetail = webProjectListCustomizeMapper.selectProjectCompanyDetail(borrowNid);
		return htlDetail;
	}

	/**
	 * 查询指定项目的项目出借详情
	 */
	@Override
	public List<WebProjectInvestListCustomize> searchProjectInvestList(Map<String, Object> params) {
		List<WebProjectInvestListCustomize> list = webProjectListCustomizeMapper.selectProjectInvestList(params);
		return list;
	}

	/**
	 * 统计指定项目的项目出借总数
	 */
	@Override
	public int countProjectInvestRecordTotal(Map<String, Object> params) {
		int hztInvestTotal = webProjectListCustomizeMapper.countProjectInvestRecordTotal(params);
		return hztInvestTotal;
	}
	
	/**
	 * 查询汇消费项目的打包信息列表
	 */
	@Override
	public List<WebProjectConsumeCustomize> searchProjectConsumeList(Map<String, Object> params) {
		List<WebProjectConsumeCustomize> list = webProjectListCustomizeMapper.selectProjectConsumeList(params);
		return list;
	}

	/**
	 * 统计汇消费项目的打包信息列表总数
	 */
	@Override
	public int countProjectConsumeRecordTotal(Map<String, Object> params) {
		int total = webProjectListCustomizeMapper.countProjectConsumeListRecordTotal(params);
		return total;
	}

	/**
	 * 根据borrowNid获取借款项目
	 * 
	 * @param borrowNid
	 * @return
	 */
	@Override
	public WebProjectDetailCustomize selectProjectDetail(String borrowNid) {
		WebProjectDetailCustomize borrow = webProjectListCustomizeMapper.selectProjectDetail(borrowNid);
		return borrow;
	}

	/**
	 * 获取资产项目类型
	 * 
	 * @return
	 */
	@Override
	public HjhAssetBorrowType selectAssetBorrowType(String instCode,Integer assetType) {
		HjhAssetBorrowTypeExample example = new HjhAssetBorrowTypeExample();
		HjhAssetBorrowTypeExample.Criteria cra = example.createCriteria();
		cra.andInstCodeEqualTo(instCode);
		cra.andAssetTypeEqualTo(assetType);
		cra.andIsOpenEqualTo(1);

		List<HjhAssetBorrowType> list = this.hjhAssetBorrowTypeMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		
		return null;
		
	}

	/**
	 * 根据borrowNid获取借款项目
	 * 
	 * @param borrowNid
	 * @return
	 */
	@Override
	public WebProjectDetailCustomize selectProjectPreview(String borrowNid) {
		WebProjectDetailCustomize borrow = webProjectListCustomizeMapper.selectProjectPreview(borrowNid);
		return borrow;
	}

	/**
	 * 查询相应的汇资产信息
	 */
	@Override
	public WebHzcProjectDetailCustomize searchHzcProjectDetail(String borrowNid) {
		WebHzcProjectDetailCustomize hzcInfo = webProjectListCustomizeMapper.searchHzcProjectDetail(borrowNid);
		return hzcInfo;
	}

	/**
	 * 查询认证信息
	 */
	@Override
	public List<WebProjectAuthenInfoCustomize> searchProjectAuthenInfos(String borrowNid) {
		List<WebProjectAuthenInfoCustomize> authenInfoList = webProjectListCustomizeMapper.searchProjectAuthenInfos(borrowNid);
		return authenInfoList;
	}

	@Override
	public WebHzcDisposalPlanCustomize searchDisposalPlan(String borrowNid) {
		WebHzcDisposalPlanCustomize disposalPlan = webProjectListCustomizeMapper.searchDisposalPlan(borrowNid);
		return disposalPlan;
	}

	@Override
	public WebRiskControlCustomize selectRiskControl(String borrowNid) {
		WebRiskControlCustomize riskControl = webProjectListCustomizeMapper.selectRiskControl(borrowNid);
		return riskControl;
	}

	@Override
	public List<WebMortgageCustomize> selectMortgageList(String borrowNid) {
		List<WebMortgageCustomize> mortgageList = webProjectListCustomizeMapper.selectMortgageList(borrowNid);
		return mortgageList;
	}

	@Override
	public List<WebVehiclePledgeCustomize> selectVehiclePledgeList(String borrowNid) {
		List<WebVehiclePledgeCustomize> vehiclePledgeList = webProjectListCustomizeMapper.selectVehiclePledgeList(borrowNid);
		return vehiclePledgeList;
	}

	@Override
	public List<BorrowFileCustomBean> searchProjectFiles(String borrowNid, String url) {
		String files = appProjectListCustomizeMapper.searchProjectFiles(borrowNid);
		List<BorrowFileCustomBean> fileList = new ArrayList<BorrowFileCustomBean>();
		if (StringUtils.isNotEmpty(files)) {
			JSONArray jsonFiles = JSONArray.parseArray(files);
			for (int i = 0; i < jsonFiles.size(); i++) {
				JSONObject urlFile = jsonFiles.getJSONObject(i);
				JSONArray urls = urlFile.getJSONArray("data");
				for (int j = 0; j < urls.size(); j++) {
					JSONObject file = urls.getJSONObject(j);
					BorrowFileCustomBean fileBean = new BorrowFileCustomBean();
					fileBean.setFileName(file.getString("name"));
					fileBean.setFileUrl(url + file.getString("fileurl"));
					if (StringUtils.isNotEmpty(file.getString("imageSort"))) {
						fileBean.setSort(file.getString("imageSort"));
					} else {
						fileBean.setSort(String.valueOf(j));
					}
					fileList.add(fileBean);
				}
			}
		}
		if (fileList != null && fileList.size() > 1) {
			Collections.sort(fileList, new Comparator<BorrowFileCustomBean>() {
				public int compare(BorrowFileCustomBean arg0, BorrowFileCustomBean arg1) {
					return Integer.parseInt(arg0.getSort()) > Integer.parseInt(arg1.getSort()) ? 1 : 0;
				}
			});
		}
		return fileList;
	}

	/**
	 * 计算还款计划
	 * 
	 * @param borrowNid
	 * @return
	 */
	@Override
	public List<BorrowRepayPlanCustomBean> getRepayPlan(String borrowNid) {
		Borrow borrow = this.getBorrowByNid(borrowNid);
		String borrowStyle = borrow.getBorrowStyle();
		Integer projectType = borrow.getProjectType();
		BigDecimal yearRate = borrow.getBorrowApr();
		Integer totalMonth = borrow.getBorrowPeriod();
		BigDecimal account = borrow.getAccount();
		Integer time = borrow.getBorrowSuccessTime();
		if (time == null) {
			time = (int) (System.currentTimeMillis() / 1000);
		}
		List<BorrowRepayPlanCustomBean> repayPlans = new ArrayList<BorrowRepayPlanCustomBean>();
		// 差异费率
		BigDecimal differentialRate = Validator.isNull(borrow.getDifferentialRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getDifferentialRate());
		// 初审时间
		int borrowVerifyTime = Validator.isNull(borrow.getVerifyTime()) ? 0 : Integer.parseInt(borrow.getVerifyTime());
		// 月利率(算出管理费用[上限])
		BigDecimal borrowMonthRate = Validator.isNull(borrow.getManageFeeRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getManageFeeRate());
		// 月利率(算出管理费用[下限])
		BigDecimal borrowManagerScaleEnd = Validator.isNull(borrow.getBorrowManagerScaleEnd()) ? BigDecimal.ZERO : new BigDecimal(borrow.getBorrowManagerScaleEnd());
		// 按月计息到期还本还息和按天计息，到期还本还息
		if (borrowStyle.equals(CalculatesUtil.STYLE_END) || borrowStyle.equals(CalculatesUtil.STYLE_ENDDAY)) {
			InterestInfo info = CalculatesUtil.getInterestInfo(account, totalMonth, yearRate, borrowStyle, time, borrowMonthRate, borrowManagerScaleEnd, projectType, differentialRate,
					borrowVerifyTime);
			if (info != null) {
				String repayTime = "-";
				// 通过复审
				if (borrow.getStatus() >= 3 && borrow.getReverifyStatus() > 0) {
					repayTime = GetDate.formatDate(GetDate.getDate(info.getRepayTime() * 1000L));
				}
				BorrowRepayPlanCustomBean plan = new BorrowRepayPlanCustomBean();
				plan.setRepayPeriod("1");
				plan.setRepayTime(repayTime);
				plan.setRepayType("本息");
				plan.setRepayTotal(info.getRepayAccount().toString());
				repayPlans.add(plan);
			}
		} else {// 等额本息和等额本金和先息后本
			InterestInfo info = CalculatesUtil.getInterestInfo(account, totalMonth, yearRate, borrowStyle, time, borrowMonthRate, borrowManagerScaleEnd, projectType, differentialRate,
					borrowVerifyTime);
			if (info.getListMonthly() != null) {
				String repayTime = "-";
				for (int i = 0; i < info.getListMonthly().size(); i++) {
					InterestInfo sub = info.getListMonthly().get(i);
					// 通过复审
					if (borrow.getStatus() >= 3 && borrow.getReverifyStatus() > 0) {
						repayTime = GetDate.formatDate(GetDate.getDate(sub.getRepayTime() * 1000L));
					}
					String repayType = "本息";
					if (i < info.getListMonthly().size() - 1 && borrowStyle.equals(CalculatesUtil.STYLE_ENDMONTH)) {
						repayType = "利息";
					}
					BorrowRepayPlanCustomBean plan = new BorrowRepayPlanCustomBean();
					plan.setRepayPeriod(sub.getMontyNo() + "");
					plan.setRepayTime(repayTime);
					plan.setRepayType(repayType);
					plan.setRepayTotal(sub.getRepayAccount().toString());
					repayPlans.add(plan);
				}
			}
		}
		return repayPlans;
	}

	@Override
	public int countUserInvest(Integer userId, String borrowNid) {
		BorrowTenderExample example = new BorrowTenderExample();
		BorrowTenderExample.Criteria crt = example.createCriteria();
		crt.andUserIdEqualTo(userId);
		crt.andBorrowNidEqualTo(borrowNid);
		int count = this.borrowTenderMapper.countByExample(example);
		return count;
	}

	@Override
	public int countUserDebtInvest(Integer userId, String borrowNid) {
		DebtInvestExample example = new DebtInvestExample();
		DebtInvestExample.Criteria crt = example.createCriteria();
		crt.andUserIdEqualTo(userId);
		crt.andBorrowNidEqualTo(borrowNid);
		int count = this.debtInvestMapper.countByExample(example);
		return count;
	}

	@Override
	public UserCouponConfigCustomize getBestCoupon(String borrowNid, Integer userId, String money, String platform) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		// 查询项目信息
		Borrow borrow = this.getBorrowByNid(borrowNid);

		BorrowProjectType borrowProjectType = getProjectTypeByBorrowNid(borrowNid);
		String style = borrow.getBorrowStyle();
		// 加息券是否启用 0禁用 1启用
		Integer couponFlg = borrow.getBorrowInterestCoupon();
		// 体验金是否启用 0禁用 1启用
		Integer moneyFlg = borrow.getBorrowTasteMoney();
		List<UserCouponConfigCustomize> couponConfigs = couponConfigCustomizeMapper.getCouponConfigList(map);
		// 排序
		Collections.sort(couponConfigs, new ComparatorCouponBean());
		for (UserCouponConfigCustomize userCouponConfigCustomize : couponConfigs) {
			// 验证项目加息券或体验金是否可用
			if (couponFlg != null && couponFlg == 0) {
				if (userCouponConfigCustomize.getCouponType() == 2) {
					continue;
				}
			}
			if (moneyFlg != null && moneyFlg == 0) {
				if (userCouponConfigCustomize.getCouponType() == 1) {
					continue;
				}
			}
			// 验证项目期限、
			Integer type = userCouponConfigCustomize.getProjectExpirationType();
			if ("endday".equals(style)) {
				if (type == 1) {
					if ((userCouponConfigCustomize.getProjectExpirationLength() * 30) != borrow.getBorrowPeriod()) {
						continue;
					}
				} else if (type == 3) {
					if ((userCouponConfigCustomize.getProjectExpirationLength() * 30) > borrow.getBorrowPeriod()) {
						continue;
					}
				} else if (type == 4) {
					if ((userCouponConfigCustomize.getProjectExpirationLength() * 30) < borrow.getBorrowPeriod()) {
						continue;
					}
				} else if (type == 2) {
					if ((userCouponConfigCustomize.getProjectExpirationLengthMin() * 30) > borrow.getBorrowPeriod()
							|| (userCouponConfigCustomize.getProjectExpirationLengthMax() * 30) < borrow.getBorrowPeriod()) {
						continue;
					}
				}
			} else {
				if (type == 1) {
					if (userCouponConfigCustomize.getProjectExpirationLength() != borrow.getBorrowPeriod()) {
						continue;
					}
				} else if (type == 3) {
					if (userCouponConfigCustomize.getProjectExpirationLength() > borrow.getBorrowPeriod()) {
						continue;
					}
				} else if (type == 4) {
					if (userCouponConfigCustomize.getProjectExpirationLength() < borrow.getBorrowPeriod()) {
						continue;
					}
				} else if (type == 2) {
					if (userCouponConfigCustomize.getProjectExpirationLengthMin() > borrow.getBorrowPeriod() || userCouponConfigCustomize.getProjectExpirationLengthMax() < borrow.getBorrowPeriod()) {
						continue;
					}
				}
			}

			// 验证项目金额
			Integer tenderQuota = userCouponConfigCustomize.getTenderQuotaType();
			if (tenderQuota == 1) {
				if (userCouponConfigCustomize.getTenderQuotaMin() > new Double(money) || userCouponConfigCustomize.getTenderQuotaMax() < new Double(money)) {
					continue;
				}
			} else if (tenderQuota == 2) {
				if (userCouponConfigCustomize.getTenderQuota() > new Double(money)) {
					continue;
				}
			}
			// 验证优惠券适用的项目 新逻辑 pcc20160715
			String projectType = userCouponConfigCustomize.getProjectType();
			boolean ifprojectType = true;
			if (projectType.indexOf("-1") != -1) {
				if (!"RTB".equals(borrowProjectType.getBorrowClass())) {
					ifprojectType = false;
				}
			} else {
				if ("HXF".equals(borrowProjectType.getBorrowClass())) {
					if (projectType.indexOf("2") != -1) {
						ifprojectType = false;
					}
				} else if ("NEW".equals(borrowProjectType.getBorrowClass())) {
					if (projectType.indexOf("3") != -1) {
						ifprojectType = false;
					}
				} else if ("ZXH".equals(borrowProjectType.getBorrowClass())) {
					if (projectType.indexOf("4") != -1) {
						ifprojectType = false;
					}
				} else {
					if (projectType.indexOf("1") != -1) {
						if (!"RTB".equals(borrowProjectType.getBorrowClass())) {
							ifprojectType = false;
						}
					}
				}
			}
			if (ifprojectType) {
				continue;
			}
			// 验证使用平台
			String couponSystem = userCouponConfigCustomize.getCouponSystem();
			String[] couponSystemArr = couponSystem.split(",");
			for (String couponSystemString : couponSystemArr) {
				if ("-1".equals(couponSystemString)) {
					return userCouponConfigCustomize;
				}
				if (platform.equals(couponSystemString)) {
					return userCouponConfigCustomize;
				}
			}
		}
		return null;
	}

	@Override
	@Cached(name="webBidProjectTypeCache-", expire = CustomConstants.HOME_CACHE_LIVE_TIME, cacheType = CacheType.BOTH)
	@CacheRefresh(refresh = 300, stopRefreshAfterLastAccess = 1800, timeUnit = TimeUnit.SECONDS)
	public List<BorrowProjectType> getProjectTypeList() {
		BorrowProjectTypeExample example = new BorrowProjectTypeExample();
		BorrowProjectTypeExample.Criteria cra = example.createCriteria();
		cra.andStatusEqualTo(CustomConstants.FLAG_NORMAL);
		return this.borrowProjectTypeMapper.selectByExample(example);
	}

	@Override
	public DebtBorrow selectDebtBorrowByNid(String borrowNid) {
		DebtBorrowExample example = new DebtBorrowExample();
		DebtBorrowExample.Criteria crt = example.createCriteria();
		crt.andBorrowNidEqualTo(borrowNid);
		List<DebtBorrow> borrowList = this.debtBorrowMapper.selectByExample(example);
		if (borrowList != null && borrowList.size() == 1) {
			return borrowList.get(0);
		}
		return null;
	}

	@Override
	public int countDebtPlanProjectInvestRecordTotal(Map<String, Object> params) {
		return debtPlanBorrowCustomizeMapper.countDebtPlanBorrowInvestRecordTotal(params);
	}

	@Override
	public List<WebProjectInvestListCustomize> searchDeptPlanProjectInvestList(Map<String, Object> params) {
		return debtPlanBorrowCustomizeMapper.selectDebtPlanBorrowInvestList(params);
	}

    @Override
    @Cached(name="webBidCntCache-", expire = CustomConstants.HOME_CACHE_LIVE_TIME, cacheType = CacheType.BOTH)
	@CacheRefresh(refresh = 5, stopRefreshAfterLastAccess = 600, timeUnit = TimeUnit.SECONDS)
    public int countProjectListRecordTotalNew(Map<String, Object> params) {
        int total = webProjectListCustomizeMapper.countProjectListRecordTotalNew(params);
        return total;
    }

    @Override
    /**
     * 这里含着新手标和散标
     */
    @Cached(name="webHomeProjectListCache-", expire = CustomConstants.HOME_CACHE_LIVE_TIME, cacheType = CacheType.BOTH)
	@CacheRefresh(refresh = 5, stopRefreshAfterLastAccess = 600, timeUnit = TimeUnit.SECONDS)
    public List<WebProjectListCustomize> searchProjectListNew(Map<String, Object> params) {
        List<WebProjectListCustomize> list = webProjectListCustomizeMapper.selectProjectListNew(params);
        return list;
    }

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param borrowNid
	 * @return
	 * @author Michael
	 */
		
	@Override
	public BorrowUsers getBorrowUsersByNid(String borrowNid) {
		if(StringUtils.isBlank(borrowNid)){
			return null;
		}
		BorrowUsersExample example = new BorrowUsersExample();
		BorrowUsersExample.Criteria cra = example.createCriteria();
		cra.andBorrowNidEqualTo(borrowNid);
		List<BorrowUsers> list = this.borrowUsersMapper.selectByExample(example);
		if(list != null && list.size() > 0){
		    // add by liushouyi nifa2 20181206 start
			// 处理企业注册时间（数据库存放格式YYYY-MM-DD）
			if (StringUtils.isNotBlank(list.get(0).getComRegTime()) && list.get(0).getComRegTime().length() >= 4){
				String comRegTime = list.get(0).getComRegTime().substring(0,4).concat("年");
				list.get(0).setComRegTime(comRegTime);
			}
			// add by liushouyi nifa2 20181206 end
			return list.get(0);
		}
		return null;
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param borrowNid
	 * @return
	 * @author Michael
	 */
		
	@Override
	public BorrowManinfo getBorrowManinfoByNid(String borrowNid) {
		if(StringUtils.isBlank(borrowNid)){
			return null;
		}
		// upd by liushouyi nifa2 20181206 start
		List<BorrowManinfo> list = this.borrowManinfoCustomizeMapper.selectBorrowManInfoByBorrowNid(borrowNid);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
//		BorrowManinfoExample example = new BorrowManinfoExample();
//		BorrowManinfoExample.Criteria cra = example.createCriteria();
//		cra.andBorrowNidEqualTo(borrowNid);
//		List<BorrowManinfo> list = this.borrowManinfoMapper.selectByExample(example);
//		if(list != null && list.size() > 0){
//			// 处理借款人职业类型
//			ParamNameExample examplePN = new ParamNameExample();
//			examplePN.createCriteria().andNameClassEqualTo("POSITION").andNameCdEqualTo(list.get(0).getPosition());
//			List<ParamName> paramNames = this.paramNameMapper.selectByExample(examplePN);
//			if(null != paramNames && paramNames.size() > 0){
//				list.get(0).setPosition(paramNames.get(0).getName());
//			}
//			return list.get(0);
//		}
        // upd by liushouyi nifa2 20181206 end
		return null;
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param borrowNid
	 * @return
	 * @author Michael
	 */
		
	@Override
	public List<BorrowHouses> getBorrowHousesByNid(String borrowNid) {
		if(StringUtils.isBlank(borrowNid)){
			return null;
		}
		BorrowHousesExample example = new BorrowHousesExample();
		BorrowHousesExample.Criteria cra = example.createCriteria();
		cra.andBorrowNidEqualTo(borrowNid);
		List<BorrowHouses> list = this.borrowHousesMapper.selectByExample(example);
		if(list != null && list.size() > 0){
			return list;
		}
		return null;
			
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param borrowNid
	 * @return
	 * @author Michael
	 */
		
	@Override
	public List<BorrowCarinfo> getBorrowCarinfoByNid(String borrowNid) {
		if(StringUtils.isBlank(borrowNid)){
			return null;
		}
		BorrowCarinfoExample example = new BorrowCarinfoExample();
		BorrowCarinfoExample.Criteria cra = example.createCriteria();
		cra.andBorrowNidEqualTo(borrowNid);
		List<BorrowCarinfo> list = this.borrowCarinfoMapper.selectByExample(example);
		if(list != null && list.size() > 0){
			return list;
		}
		return null;
			
	}

	
	
	/**
     * 查询标的到达定时发标时间的标的编号
     * @param borrowNid 
     * @param ontime
     * @return
	 */
	@Override
	public Integer getOntimeIdByNid(String borrowNid, Integer ontime) {
		return this.ontimeTenderCustomizeMapper.queryOntimeIdByNid(borrowNid, ontime);
	}
	
	/**
	 * 发标
	 * 
	 * @param borrow
	 * @return
	 * @author Administrator
	 */

	@Override
	public boolean updateOntimeSendBorrow(int borrowId) {

		// 当前时间
		int nowTime = GetDate.getNowTime10();
		BorrowWithBLOBs borrow = this.borrowMapper.selectByPrimaryKey(borrowId);
        // DB验证
        // 有出借金额发生异常
        BigDecimal zero = new BigDecimal("0");
        BigDecimal borrowAccountYes = borrow.getBorrowAccountYes();
        String borrowNid = borrow.getBorrowNid();
        if (!(borrowAccountYes == null || borrowAccountYes.compareTo(zero) == 0)) {
            _log.error(borrowNid + " 定时发标异常：标的已有出借人出借");
            return false;
        }
		
		borrow.setBorrowEndTime(String.valueOf(nowTime + borrow.getBorrowValidTime() * 86400));
		// 是否可以进行借款
		borrow.setBorrowStatus(1);
		// 是否可以进行借款
		borrow.setBorrowFullStatus(0);
		// 状态
		borrow.setStatus(2);
		// 初审时间
		borrow.setVerifyTime(String.valueOf(nowTime));
		// 剩余可出借金额
		borrow.setBorrowAccountWait(borrow.getAccount());
		boolean flag = this.borrowMapper.updateByPrimaryKeySelective(borrow) > 0 ? true : false;
		if (flag) {
			// 写入redis
			RedisUtils.set(borrow.getBorrowNid(), borrow.getBorrowAccountWait().toString());
			// add by liushouyi nifa2 20181204 start
			// 发送发标成功的消息队列到互金上报数据
			Map<String, String> param = new HashMap<String, String>();
			param.put("borrowNid", borrowNid);
			param.put("userId",borrow.getUserId() + "");
			this.mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ISSUE_INVESTING_DELAY_KEY, JSONObject.toJSONString(param));
			// add by liushouyi nifa2 20181204 end
			// 发送发标短信
			Map<String, String> params = new HashMap<String, String>();
			params.put("val_title", borrow.getBorrowNid());
			SmsMessage smsMessage = new SmsMessage(null, params, null, null, MessageDefine.SMSSENDFORMANAGER, "【汇盈金服】", CustomConstants.PARAM_TPL_DSFB, CustomConstants.CHANNEL_TYPE_NORMAL);
			smsProcesser.gather(smsMessage);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<ActdecListedOne> getActdecList(String borrowNid) {
		ActdecListedOneExample example = new ActdecListedOneExample();
		 com.hyjf.mybatis.model.auto.ActdecListedOneExample.Criteria criteria = example.createCriteria();
		 criteria.andNumberEqualTo(borrowNid);
		return actdecListedOneMapper.selectByExample(example);
	}

	/**
	 * 取得借款信息
	 *
	 * @return
	 */
	@Override
	public BorrowRepay getBorrowRepay(String borrowNid) {
		BorrowRepayExample example = new BorrowRepayExample();
		BorrowRepayExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		List<BorrowRepay> list = this.borrowRepayMapper.selectByExample(example);

		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 根据borrowNid，userId查找承接记录
	 */
	@Override
	public int countCreditTender(Map<String, Object> params) {
		return webProjectListCustomizeMapper.countCreditTender(params);
	}
	/**
	 * 根据borrowNid查找债转记录列表
	 * @param params
	 * @return
	 */
	@Override
	public List<WebProjectUndertakeListCustomize> selectProjectUndertakeList(Map<String, Object> params) {
		return webProjectListCustomizeMapper.selectProjectUndertakeList(params);
	}

	/**
	 * 根据borrowNid获取债转记录个数
	 */
	@Override
	public int countCreditTenderByBorrowNid(String borrowNid) {
		return webProjectListCustomizeMapper.countCreditTenderByBorrowNid(borrowNid);
	}

	/**
	 * 根据borrowNid获取债转总金额
	 */
	@Override
	public String sumUndertakeAccount(String borrowNid) {
		return webProjectListCustomizeMapper.sumUndertakeAccount(borrowNid);
	}
	
}
