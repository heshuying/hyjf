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
package com.hyjf.web.plan.borrow;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.calculate.CalculatesUtil;
import com.hyjf.common.calculate.InterestInfo;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.auto.BorrowProjectTypeExample;
import com.hyjf.mybatis.model.auto.BorrowTenderExample;
import com.hyjf.mybatis.model.auto.Users;
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
import com.hyjf.mybatis.model.customize.web.WebRiskControlCustomize;
import com.hyjf.mybatis.model.customize.web.WebVehiclePledgeCustomize;
import com.hyjf.web.BaseServiceImpl;

@Service
public class PlanBorrowServiceImpl extends BaseServiceImpl implements PlanBorrowService {

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
		WebProjectCompanyDetailCustomize htlDetail = webProjectListCustomizeMapper
				.selectProjectCompanyDetail(borrowNid);
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
		List<WebProjectAuthenInfoCustomize> authenInfoList = webProjectListCustomizeMapper
				.searchProjectAuthenInfos(borrowNid);
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
		List<WebVehiclePledgeCustomize> vehiclePledgeList = webProjectListCustomizeMapper
				.selectVehiclePledgeList(borrowNid);
		return vehiclePledgeList;
	}

	@Override
	public List<WebProjectConsumeCustomize> selectProjectConsumeList(Map<String, Object> params) {
		List<WebProjectConsumeCustomize> consumeList = webProjectListCustomizeMapper.selectProjectConsumeList(params);
		return consumeList;
	}

	@Override
	public int countProjectConsumeListRecordTotal(Map<String, Object> params) {
		int total = appProjectListCustomizeMapper.countProjectConsumeListRecordTotal(params);
		return total;
	}

	@Override
	public List<PlanBorrowFileBean> searchProjectFiles(String borrowNid, String url) {
		String files = appProjectListCustomizeMapper.searchProjectFiles(borrowNid);
		List<PlanBorrowFileBean> fileList = new ArrayList<PlanBorrowFileBean>();
		if (StringUtils.isNotEmpty(files)) {
			JSONArray jsonFiles = JSONArray.parseArray(files);
			for (int i = 0; i < jsonFiles.size(); i++) {
				JSONObject urlFile = jsonFiles.getJSONObject(i);
				JSONArray urls = urlFile.getJSONArray("data");
				for (int j = 0; j < urls.size(); j++) {
					JSONObject file = urls.getJSONObject(j);
					PlanBorrowFileBean fileBean = new PlanBorrowFileBean();
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
			Collections.sort(fileList, new Comparator<PlanBorrowFileBean>() {
				public int compare(PlanBorrowFileBean arg0, PlanBorrowFileBean arg1) {
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
	public List<PlanBorrowRepayPlanBean> getRepayPlan(String borrowNid) {
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
		List<PlanBorrowRepayPlanBean> repayPlans = new ArrayList<PlanBorrowRepayPlanBean>();
		// 差异费率
		BigDecimal differentialRate = Validator.isNull(borrow.getDifferentialRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getDifferentialRate());
		//初审时间
		int borrowVerifyTime =Validator.isNull(borrow.getVerifyTime()) ? 0 : Integer.parseInt(borrow.getVerifyTime());
    	// 月利率(算出管理费用[上限])
		BigDecimal borrowMonthRate = Validator.isNull(borrow.getManageFeeRate()) ? BigDecimal.ZERO: new BigDecimal(borrow.getManageFeeRate());
		// 月利率(算出管理费用[下限])
		BigDecimal borrowManagerScaleEnd = Validator.isNull(borrow.getBorrowManagerScaleEnd()) ? BigDecimal.ZERO: new BigDecimal(borrow.getBorrowManagerScaleEnd());
		// 按月计息到期还本还息和按天计息，到期还本还息
		if (borrowStyle.equals(CalculatesUtil.STYLE_END) || borrowStyle.equals(CalculatesUtil.STYLE_ENDDAY)) {
			InterestInfo info = CalculatesUtil.getInterestInfo(account, totalMonth, yearRate, borrowStyle, time,borrowMonthRate, borrowManagerScaleEnd, projectType,differentialRate,borrowVerifyTime);
			if (info != null) {
				String repayTime = "-";
				// 通过复审
				if (borrow.getReverifyStatus() == 3) {
					repayTime = GetDate.formatDate(GetDate.getDate(info.getRepayTime() * 1000L));
				}
				PlanBorrowRepayPlanBean planIntrest = new PlanBorrowRepayPlanBean();
				planIntrest.setRepayPeriod("1");
				planIntrest.setRepayTime(repayTime);
				planIntrest.setRepayType("利息");
				planIntrest.setRepayTotal(info.getRepayAccountInterest().toString());
				PlanBorrowRepayPlanBean plan = new PlanBorrowRepayPlanBean();
				plan.setRepayPeriod("1");
				plan.setRepayTime(repayTime);
				plan.setRepayType("本金");
				plan.setRepayTotal(info.getRepayAccountCapital().toString());
				repayPlans.add(planIntrest);
				repayPlans.add(plan);
			}
		} else {// 等额本息和等额本金和先息后本
			InterestInfo info = CalculatesUtil.getInterestInfo(account, totalMonth, yearRate, borrowStyle, time,
					borrowMonthRate, borrowManagerScaleEnd, projectType,differentialRate,borrowVerifyTime);
			if (info.getListMonthly() != null) {
				String repayTime = "-";
				for (int i = 0; i < info.getListMonthly().size(); i++) {
					InterestInfo sub = info.getListMonthly().get(i);
					// 通过复审
					if (borrow.getReverifyStatus() == 3) {
						repayTime = GetDate.formatDate(GetDate.getDate(sub.getRepayTime() * 1000L));
					}
					String repayType = "本息";
					if (i < info.getListMonthly().size() - 1 && borrowStyle.equals(CalculatesUtil.STYLE_ENDMONTH)) {
						repayType = "利息";
					}
					PlanBorrowRepayPlanBean plan = new PlanBorrowRepayPlanBean();
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

	private Borrow getBorrowByNid(String borrowNid) {
		BorrowExample example = new BorrowExample();
		BorrowExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		List<Borrow> list = borrowMapper.selectByExample(example);
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public Users searchLoginUser(Integer userId) {
		Users user = usersMapper.selectByPrimaryKey(userId);
		return user;
	}

	@Override
	public Borrow selectBorrowByNid(String borrowNid) {
		BorrowExample example = new BorrowExample();
		BorrowExample.Criteria crt = example.createCriteria();
		crt.andBorrowNidEqualTo(borrowNid);
		List<Borrow> borrowList = this.borrowMapper.selectByExample(example);
		if(borrowList!=null&&borrowList.size()==1){
			return borrowList.get(0);
		}
		return null;
	}

	@Override
	public int countUserInvest(Integer userId, String borrowNid) {
		BorrowTenderExample example = new BorrowTenderExample();
		BorrowTenderExample.Criteria crt=example.createCriteria();
		crt.andUserIdEqualTo(userId);
		crt.andBorrowNidEqualTo(borrowNid);
		int count = this.borrowTenderMapper.countByExample(example);
		return count;
	}
	
	
	
	 @Override
	    public UserCouponConfigCustomize getBestCoupon(String borrowNid, Integer userId, String money, String platform) {
	        Map<String,Object> map=new HashMap<String,Object>();
	        map.put("userId", userId);
	        // 查询项目信息
	        Borrow borrow = this.getBorrowByNid(borrowNid);
	        
//	        System.out.println("~~~~~~~~~~出借页面加载获得标编号~~~~~~~~~~~~~borrowNid:"+borrowNid);
	        BorrowProjectType borrowProjectType=getProjectTypeByBorrowNid(borrowNid);
//	        System.out.println("~~~~~~~~~~出借页面加载获得标编号~~~~~~~~~~~~~borrowNid的getBorrowInterestCoupon():"+borrow.getBorrowInterestCoupon());
//	        System.out.println("~~~~~~~~~~出借页面加载获得标编号~~~~~~~~~~~~~borrowNid的getBorrowTasteMoney():"+borrow.getBorrowTasteMoney());
	        String style=borrow.getBorrowStyle();
	        //加息券是否启用  0禁用  1启用
	        Integer couponFlg = borrow.getBorrowInterestCoupon();
	        //体验金是否启用  0禁用  1启用
	        Integer moneyFlg=borrow.getBorrowTasteMoney();
	        /*int couponType = 0;
	        if(couponFlg==1&&moneyFlg!=1){
	            couponType = 2;
	        }else if(couponFlg!=1&&moneyFlg==1){
	            couponType = 1;
	        }
	        map.put("couponType", couponType);*/
	        List<UserCouponConfigCustomize> couponConfigs=couponConfigCustomizeMapper.getCouponConfigList(map);
//	        List<UserCouponConfigCustomize> availableCouponConfigs=new ArrayList<UserCouponConfigCustomize>();
	        for (UserCouponConfigCustomize userCouponConfigCustomize : couponConfigs) {
	            
	            
	            //验证项目加息券或体验金是否可用
	            if(couponFlg!=null&&couponFlg==0){
	                if(userCouponConfigCustomize.getCouponType()==2){
	                    continue;
	                }
	            }
	            if(moneyFlg!=null&&moneyFlg==0){
	                if(userCouponConfigCustomize.getCouponType()==1){
	                    continue;
	                }
	            }
	            //验证项目期限、
	            Integer type=userCouponConfigCustomize.getProjectExpirationType();
	            if("endday".equals(style)){
	                if(type==1){
	                    if((userCouponConfigCustomize.getProjectExpirationLength()*30)!= borrow.getBorrowPeriod()){
	                        continue;
	                    }
	                }else if(type==3){
	                    if((userCouponConfigCustomize.getProjectExpirationLength()*30)> borrow.getBorrowPeriod()){
	                        continue;
	                    }
	                }else if(type==4){
	                    if((userCouponConfigCustomize.getProjectExpirationLength()*30)< borrow.getBorrowPeriod()){
	                        continue;
	                    }
	                }else if(type==2){
	                    if((userCouponConfigCustomize.getProjectExpirationLengthMin()*30)> borrow.getBorrowPeriod()||
	                            (userCouponConfigCustomize.getProjectExpirationLengthMax()*30)< borrow.getBorrowPeriod()){
	                        continue;
	                    }
	                } 
	            }else{
	                if(type==1){
	                    if(userCouponConfigCustomize.getProjectExpirationLength()!= borrow.getBorrowPeriod()){
	                        continue;
	                    }
	                }else if(type==3){
	                    if(userCouponConfigCustomize.getProjectExpirationLength()> borrow.getBorrowPeriod()){
	                        continue;
	                    }
	                }else if(type==4){
	                    if(userCouponConfigCustomize.getProjectExpirationLength()< borrow.getBorrowPeriod()){
	                        continue;
	                    }
	                }else if(type==2){
	                    if(userCouponConfigCustomize.getProjectExpirationLengthMin()> borrow.getBorrowPeriod()||userCouponConfigCustomize.getProjectExpirationLengthMax()< borrow.getBorrowPeriod()){
	                        continue;
	                    }
	                }
	            }
	            
	            //验证项目金额
	            Integer tenderQuota=userCouponConfigCustomize.getTenderQuotaType();
	            if(tenderQuota==1){
	                if(userCouponConfigCustomize.getTenderQuotaMin()> new Double(money)||userCouponConfigCustomize.getTenderQuotaMax()< new Double(money)){
	                    continue;
	                }
	            }else if(tenderQuota==2){
	                if(userCouponConfigCustomize.getTenderQuota()> new Double(money)){
	                    continue;
	                }
	            }
	            //验证优惠券适用的项目   新逻辑 pcc20160715
	            String projectType=userCouponConfigCustomize.getProjectType();
	            boolean ifprojectType=true;
	            if(projectType.indexOf("-1")!=-1){
	            	if(!"RTB".equals(borrowProjectType.getBorrowClass())){
                		ifprojectType = false;
                	}
	            }else{
	                if("HXF".equals(borrowProjectType.getBorrowClass())){
	                    if(projectType.indexOf("2")!=-1){
	                        ifprojectType=false; 
	                    }
	                } else if("NEW".equals(borrowProjectType.getBorrowClass())){
	                    if(projectType.indexOf("3")!=-1){
	                        ifprojectType=false; 
	                    }
	                } else if("ZXH".equals(borrowProjectType.getBorrowClass())){
	                    if(projectType.indexOf("4")!=-1){
	                        ifprojectType=false; 
	                    }
	                } else {
	                	if(projectType.indexOf("1")!=-1){
	                		if(!"RTB".equals(borrowProjectType.getBorrowClass())){
	                    		ifprojectType=false; 
	                    	}
	                    }
	                }
	            }
	            if(ifprojectType){
	                continue;
	            }
	  
	            
	            //验证使用平台
	            String couponSystem=userCouponConfigCustomize.getCouponSystem();
	            String[] couponSystemArr=couponSystem.split(",");
	            for (String couponSystemString : couponSystemArr) {
	                if("-1".equals(couponSystemString)){
	                    return userCouponConfigCustomize;
	                }
	                if(platform.equals(couponSystemString)){
	                    return userCouponConfigCustomize;
	                }
	            }
	        }
	        return null;
	    }
	    
	    @Override
	    public UserCouponConfigCustomize getBestCouponById(String couponId) {
	        UserCouponConfigCustomize couponConfig=couponConfigCustomizeMapper.getBestCouponById(couponId);
	        return couponConfig;
	    }
	    
	    public BorrowProjectType getProjectTypeByBorrowNid(String borrowNid) {
	        BorrowProjectTypeExample example = new BorrowProjectTypeExample();
	        BorrowProjectTypeExample.Criteria cra = example.createCriteria();
	        cra.andStatusEqualTo(CustomConstants.FLAG_NORMAL).andBorrowClassEqualTo(borrowNid.substring(0,3));
	        List<BorrowProjectType> borrowProjectTypes=this.borrowProjectTypeMapper.selectByExample(example);
	        BorrowProjectType borrowProjectType=new BorrowProjectType();
	        if(borrowProjectTypes!=null&&borrowProjectTypes.size()>0){
	            borrowProjectType=borrowProjectTypes.get(0);
	        }
	        return borrowProjectType;
	    }

	    
	    public List<BorrowProjectType> getProjectTypeList() {
	        BorrowProjectTypeExample example = new BorrowProjectTypeExample();
	        BorrowProjectTypeExample.Criteria cra = example.createCriteria();
	        cra.andStatusEqualTo(CustomConstants.FLAG_NORMAL);
	        return this.borrowProjectTypeMapper.selectByExample(example);
	    }

}
