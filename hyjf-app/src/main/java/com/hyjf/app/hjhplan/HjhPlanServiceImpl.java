/**
 * Description:汇计划service接口实现
 * Copyright: Copyright (HYJF Corporation) 2017
 * Company: HYJF Corporation
 * @author: LIBIN
 * @version: 1.0
 */
package com.hyjf.app.hjhplan;

import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.hyjf.app.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanAccedeCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanBorrowCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanDetailCustomize;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class HjhPlanServiceImpl extends BaseServiceImpl implements HjhPlanService {
	
	/**
	 * 根据计划nid查询相应的计划详情
	 * @param planNid
	 * @return
	 */
	@Override
	public DebtPlanDetailCustomize selectDebtPlanDetail(String planNid) {
		DebtPlanDetailCustomize planDetail = this.hjhPlanCustomizeMapper.selectDebtPlanDetail(planNid);
		return planDetail;
	}
	
	/**
	 * 统计相应的计划加入记录总数
	 * @param params
	 * @return
	 */
	@Override
	public int countPlanAccedeRecordTotal(Map<String, Object> params) {
		int count = this.hjhPlanCustomizeMapper.countPlanAccedeRecordTotal(params);
		return count;
	}

	/**
	 * 统计相应的计划总数
	 * @param params
	 * @return
	 */
	@Override
	public Long selectPlanAccedeSum(Map<String, Object> params) {
		return hjhPlanCustomizeMapper.selectPlanAccedeSum(params);
	}

	/**
	 * 查询计划的加入记录
	 * @param params
	 * @return
	 * @author Administrator
	 */
	@Override
	public List<DebtPlanAccedeCustomize> selectPlanAccedeList(Map<String, Object> params) {
		List<DebtPlanAccedeCustomize> planAccedeList = this.hjhPlanCustomizeMapper.selectPlanAccedeList(params);
		return planAccedeList;
	}

	@Override
	public Users searchLoginUser(Integer userId) {
		Users user = usersMapper.selectByPrimaryKey(userId);
		return user;
	}

	@Override
	public int countUserAccede(String planNid, Integer userId) {
		HjhAccedeExample example = new HjhAccedeExample();
		HjhAccedeExample.Criteria crt = example.createCriteria();
		crt.andPlanNidEqualTo(planNid);
		crt.andUserIdEqualTo(userId);
		int count = this.hjhAccedeMapper.countByExample(example);
		return count;
	}

	@Override
	public HjhUserAuth getHjhUserAuthByUserId(Integer userId) {
        HjhUserAuthExample example=new HjhUserAuthExample();
        example.createCriteria().andUserIdEqualTo(userId);
        List<HjhUserAuth> list=hjhUserAuthMapper.selectByExample(example);
        if(list!=null&& list.size()>0){
            return list.get(0);
        }else{
            return null;    
        }
	}
	
	/**
	 * 查询相应的计划标的记录总数
	 * 
	 * @param params
	 * @return
	 * @author Administrator
	 */
	@Override
	@Cached(name="appPlanDetailCountCache-", expire = CustomConstants.HOME_CACHE_LIVE_TIME, cacheType = CacheType.BOTH)
	@CacheRefresh(refresh = 60, stopRefreshAfterLastAccess = 300, timeUnit = TimeUnit.SECONDS)
	public int countPlanBorrowRecordTotal(Map<String, Object> params) {
		int count = this.hjhPlanCustomizeMapper.countPlanBorrowRecordTotal(params);
		return count;

	}
	
	/**
	 * 查询相应的计划标的列表
	 * 
	 * @param params
	 * @return
	 * @author Administrator
	 */
	@Override
	@Cached(name="appPlanDetailListCache-", expire = CustomConstants.HOME_CACHE_LIVE_TIME, cacheType = CacheType.BOTH)
	@CacheRefresh(refresh = 60, stopRefreshAfterLastAccess = 300, timeUnit = TimeUnit.SECONDS)
	public List<DebtPlanBorrowCustomize> selectPlanBorrowList(Map<String, Object> params) {
		List<DebtPlanBorrowCustomize> planAccedeList = this.hjhPlanCustomizeMapper.selectPlanBorrowList(params);
		// add 给借款人加密 wangxiaohui 20180425 start
		for (DebtPlanBorrowCustomize planAccede : planAccedeList) {
			String borrowNid = planAccede.getBorrowNid();
			if ("1".equals(planAccede.getCompanyOrPersonal())) {//如果类型是公司 huiyingdai_borrow_users
				BorrowUsersExample caExample = new BorrowUsersExample();
				BorrowUsersExample.Criteria caCra = caExample.createCriteria();
				caCra.andBorrowNidEqualTo(borrowNid);
				List<BorrowUsers> selectByExample = this.borrowUsersMapper.selectByExample(caExample);
				String tureName= selectByExample.get(0).getUsername();
				String str = "******";
				if (tureName != null &&tureName != "") {
					if (tureName.length() <= 2) {
						tureName = str + tureName;
					}else if (tureName.length() > 2) {
						String substring = tureName.substring(tureName.length()-2);
						tureName = str + substring;
					}
				}
				planAccede.setTrueName(tureName);
			}else if("2".equals(planAccede.getCompanyOrPersonal())){//类型是个人 huiyingdai_borrow_maninfo
				//根据borrowNid查询查询个人的真实姓名
				BorrowManinfoExample boExample = new BorrowManinfoExample();
				BorrowManinfoExample.Criteria caCra = boExample.createCriteria();
				caCra.andBorrowNidEqualTo(borrowNid);
				List<BorrowManinfo> selectByExample = this.borrowManinfoMapper.selectByExample(boExample);
				String trueName = selectByExample.get(0).getName();
				String str = "**";
				if (trueName != null &&trueName != "") {
					if (trueName.length() == 1) {
						trueName =  trueName + str;
					}else if (trueName.length() > 1) {
						String substring = trueName.substring(0,1);
						trueName =  substring + str;
					}
				}
				planAccede.setTrueName(trueName);
			}
		}
		// add 给借款人加密 wangxiaohui 20180425 end
		return planAccedeList;

	}

	@Override
	public Borrow getBorrowByBorrowNid(String borrowNid) {
		BorrowExample example = new BorrowExample();
		BorrowExample.Criteria crt = example.createCriteria();
		crt.andBorrowNidEqualTo(borrowNid);
		List<Borrow> borrow = borrowMapper.selectByExample(example);
		if(!CollectionUtils.isEmpty(borrow)){
			return borrow.get(0);
		}
		return null;
	}
}
