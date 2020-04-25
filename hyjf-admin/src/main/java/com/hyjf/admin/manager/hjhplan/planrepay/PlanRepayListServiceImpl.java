package com.hyjf.admin.manager.hjhplan.planrepay;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import cn.jpush.api.utils.StringUtils;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.HjhPlan;
import com.hyjf.mybatis.model.auto.HjhPlanExample;
import com.hyjf.mybatis.model.auto.HjhRepay;
import com.hyjf.mybatis.model.auto.HjhRepayExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.customize.BorrowRepaymentCustomize;
import com.hyjf.mybatis.model.customize.HjhCreditTenderCustomize;
import com.hyjf.mybatis.model.customize.PlanCommonCustomize;
import com.hyjf.mybatis.model.customize.web.hjh.HjhPlanRepayCustomize;

@Service
public class PlanRepayListServiceImpl extends BaseServiceImpl implements PlanRepayListService {
	
	/**
	 * @method: countPlan
	 * @description: 还款计划数量查询
	 * @return: int
	 * @mender: LIBIN
	 * @date: 2017年8月11日
	 */
	@Override
	public int countRepayPlan(PlanCommonCustomize planCommonCustomize) {
		int ret = 0;
		HjhRepayExample example = new HjhRepayExample(); 
		HjhRepayExample.Criteria cra = example.createCriteria();
		// 传入查询加入订单号
		if (StringUtils.isNotEmpty(planCommonCustomize.getPlanOrderId())) {
			cra.andAccedeOrderIdLike("%" + planCommonCustomize.getPlanOrderId() + "%");
		}
		// 传入查询计划编号
		if (StringUtils.isNotEmpty(planCommonCustomize.getPlanNidSrch())) {
			cra.andPlanNidLike("%" + planCommonCustomize.getPlanNidSrch() + "%");
		}
		// 传入查询用户名
		if (StringUtils.isNotEmpty(planCommonCustomize.getUserName())) {
			cra.andUserNameLike("%" + planCommonCustomize.getUserName() + "%");
		}
		//锁定期
		if(StringUtils.isNotEmpty(planCommonCustomize.getDebtLockPeriodSrch())){
			cra.andLockPeriodEqualTo(Integer.parseInt(planCommonCustomize.getDebtLockPeriodSrch()));
		}
		// 传入查询回款状态
		if (StringUtils.isNotEmpty(planCommonCustomize.getRepayStatus())) {	
			cra.andRepayStatusEqualTo(Integer.valueOf(planCommonCustomize.getRepayStatus()));
		}
		// 传入查询订单状态
		if (StringUtils.isNotEmpty(planCommonCustomize.getOrderStatusSrch())) {	
			cra.andOrderStatusEqualTo(Integer.valueOf(planCommonCustomize.getOrderStatusSrch()));
		}
		// 传入查询应还款日期开始
		if (StringUtils.isNotEmpty(planCommonCustomize.getRepayTimeStart())) {	
			cra.andRepayShouldTimeGreaterThanOrEqualTo(GetDate.getDayStart10(planCommonCustomize.getRepayTimeStart()));
		}
		// 传入查询应还款日期结束
		if (StringUtils.isNotEmpty(planCommonCustomize.getRepayTimeEnd())) {	
			cra.andRepayShouldTimeLessThanOrEqualTo(GetDate.getDayEnd10(planCommonCustomize.getRepayTimeEnd()));
		}
		// 传入查询计划实际还款时间开始
		if (StringUtils.isNotEmpty(planCommonCustomize.getActulRepayTimeStart())) {
			cra.andRepayActualTimeGreaterThanOrEqualTo(GetDate.getDayStart10(planCommonCustomize.getActulRepayTimeStart()));
		}
		// 传入查询计划实际还款时间结束
		if (StringUtils.isNotEmpty(planCommonCustomize.getActulRepayTimeEnd())) {
			cra.andRepayActualTimeLessThanOrEqualTo(GetDate.getDayEnd10(planCommonCustomize.getActulRepayTimeEnd()));
		}
		// 排序(暂时按计划应还款时间倒序排)
		example.setOrderByClause("repay_should_time Desc");
		ret = this.hjhRepayMapper.countByExample(example);
		return ret;
	}

	/**
	 * 
	 * @method: selectPlanList
	 * @description: 还款计划列表查询
	 * @return: List
	 * @mender: LIBIN
	 * @date: 2017年8月11日
	 */
	@Override
	public List<HjhRepay> selectPlanList(PlanCommonCustomize planCommonCustomize) {
		HjhRepayExample example = new HjhRepayExample(); 
		HjhRepayExample.Criteria cra = example.createCriteria();
		// 传入查询加入订单号
		if (StringUtils.isNotEmpty(planCommonCustomize.getPlanOrderId())) {
			cra.andAccedeOrderIdEqualTo(planCommonCustomize.getPlanOrderId());
		}
		// 传入查询计划编号
		if (StringUtils.isNotEmpty(planCommonCustomize.getPlanNidSrch())) {
			cra.andPlanNidEqualTo(planCommonCustomize.getPlanNidSrch());
		}
		// 传入查询用户名
		if (StringUtils.isNotEmpty(planCommonCustomize.getUserName())) {
			cra.andUserNameEqualTo(planCommonCustomize.getUserName());
		}
		//锁定期
		if(StringUtils.isNotEmpty(planCommonCustomize.getDebtLockPeriodSrch())){
			cra.andLockPeriodEqualTo(Integer.parseInt(planCommonCustomize.getDebtLockPeriodSrch()));
		}
		// 传入查询回款状态
		if (StringUtils.isNotEmpty(planCommonCustomize.getRepayStatus())) {	
			cra.andRepayStatusEqualTo(Integer.valueOf(planCommonCustomize.getRepayStatus()));
		}
		// 传入查询订单状态
		if (StringUtils.isNotEmpty(planCommonCustomize.getOrderStatusSrch())) {	
			cra.andOrderStatusEqualTo(Integer.valueOf(planCommonCustomize.getOrderStatusSrch()));
		}
		// 传入查询应还款日期开始
		if (StringUtils.isNotEmpty(planCommonCustomize.getRepayTimeStart())) {	
			cra.andRepayShouldTimeGreaterThanOrEqualTo(GetDate.getDayStart10(planCommonCustomize.getRepayTimeStart()));
		}
		// 传入查询应还款日期结束
		if (StringUtils.isNotEmpty(planCommonCustomize.getRepayTimeEnd())) {	
			cra.andRepayShouldTimeLessThanOrEqualTo(GetDate.getDayEnd10(planCommonCustomize.getRepayTimeEnd()));
		}
		// 传入查询计划实际还款时间开始
		if (StringUtils.isNotEmpty(planCommonCustomize.getActulRepayTimeStart())) {
			cra.andRepayActualTimeGreaterThanOrEqualTo(GetDate.getDayStart10(planCommonCustomize.getActulRepayTimeStart()));
		}
		// 传入查询计划实际还款时间结束
		if (StringUtils.isNotEmpty(planCommonCustomize.getActulRepayTimeEnd())) {
			cra.andRepayActualTimeLessThanOrEqualTo(GetDate.getDayStart10(planCommonCustomize.getActulRepayTimeEnd()));
		}
		// 排序(暂时按计划应还款时间倒序排)
		example.setOrderByClause("repay_should_time Asc");
		// 传入分页
		if (planCommonCustomize.getLimitStart() >= 0) {
			example.setLimitStart(planCommonCustomize.getLimitStart());
			example.setLimitEnd(planCommonCustomize.getLimitEnd());
		}
		List<HjhRepay> result = this.hjhRepayMapper.selectByExample(example);
		return result;
	}
	
	/**
	 * 导出汇计划还款列表
	 * @param paraMap
	 * @return
	 */
	@Override
	public List<HjhPlanRepayCustomize> exportPlanRepayList(Map<String,Object> paraMap){
		return hjhPlanCustomizeMapper.exportPlanRepayList(paraMap);
	}

	/**
	 * 统计还款信息合计
	 *
	 * @param borrowRepaymentCustomize
	 * @return
	 */
	@Override
	public Long countBorrowRepayment(BorrowRepaymentCustomize borrowRepaymentCustomize) {
		return this.borrowRepaymentCustomizeMapper.countHjhBorrowRepayment(borrowRepaymentCustomize);
	}
	
	/**
	 * 还款信息列表
	 *
	 * @param borrowCommonCustomize
	 * @return
	 * @author Administrator
	 */
	@Override
	public List<BorrowRepaymentCustomize> selectBorrowRepaymentList(BorrowRepaymentCustomize borrowRepaymentCustomize) {
		return this.borrowRepaymentCustomizeMapper.selectHjhBorrowRepaymentList(borrowRepaymentCustomize);
	}
	
	/**
	 * 根据用户ID取得用户信息
	 *
	 * @param userId
	 * @return
	 */
	public Users getUsersByUserId(Integer userId) {
		if (userId != null) {
			UsersExample example = new UsersExample();
			example.createCriteria().andUserIdEqualTo(userId);
			List<Users> usersList = this.usersMapper.selectByExample(example);
			if (usersList != null && usersList.size() > 0) {
				return usersList.get(0);
			}
		}
		return null;
	}
	/**
	 * 根据计划编号获取计划信息
	 *
	 * @param planNid
	 * @return
	 */
	public HjhPlan getPlan(String planNid) {
		if (planNid != null) {
			HjhPlanExample example = new HjhPlanExample();
			example.createCriteria().andPlanNidEqualTo(planNid);
			List<HjhPlan> usersList = this.hjhPlanMapper.selectByExample(example);
			if (usersList != null && usersList.size() > 0) {
				return usersList.get(0);
			}
		}
		return null;
	}

	/**
	 * (债转)统计还款信息合计
	 *
	 * @param borrowRepaymentCustomize
	 * @return  
	 */
	@Override
	public Long countCreditBorrowRepayment(BorrowRepaymentCustomize borrowRepaymentCustomize) {
		return this.borrowRepaymentCustomizeMapper.countHjhCreditBorrowRepayment(borrowRepaymentCustomize);
	}
	
	@Override
    public Long countCreditBorrowTender(HjhCreditTenderCustomize borrowRepaymentCustomize) {
        return this.borrowRepaymentCustomizeMapper.countHjhCreditBorrowTender(borrowRepaymentCustomize);
    }

	/**
	 * (债转)还款信息列表
	 *
	 * @param borrowCommonCustomize
	 * @return
	 * @author Administrator
	 */
	@Override
	public List<BorrowRepaymentCustomize> selectCreditBorrowRepaymentList(BorrowRepaymentCustomize borrowCommonCustomize) {
		return this.borrowRepaymentCustomizeMapper.selectHjhCreditBorrowRepaymentList(borrowCommonCustomize);
	}
	@Override
    public List<HjhCreditTenderCustomize> selectCreditBorrowTendertList(HjhCreditTenderCustomize borrowCommonCustomize) {
        return this.borrowRepaymentCustomizeMapper.selectHjhCreditBorrowTendertList(borrowCommonCustomize);
    }

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param planCommonCustomize
	 * @return
	 * @author PC-LIUSHOUYI
	 */
		
	@Override
	public PlanCommonCustomize sumHjhRepay(PlanCommonCustomize planCommonCustomize) {
		HjhRepayExample example = new HjhRepayExample(); 
		HjhRepayExample.Criteria cra = example.createCriteria();
		// 传入查询加入订单号
		if (StringUtils.isNotEmpty(planCommonCustomize.getPlanOrderId())) {
			cra.andAccedeOrderIdEqualTo(planCommonCustomize.getPlanOrderId());
		}
		// 传入查询计划编号
		if (StringUtils.isNotEmpty(planCommonCustomize.getPlanNidSrch())) {
			cra.andPlanNidEqualTo(planCommonCustomize.getPlanNidSrch());
		}
		// 传入查询用户名
		if (StringUtils.isNotEmpty(planCommonCustomize.getUserName())) {
			cra.andUserNameEqualTo(planCommonCustomize.getUserName());
		}
		// 传入查询回款状态
		if (StringUtils.isNotEmpty(planCommonCustomize.getRepayStatus())) {	
			cra.andRepayStatusEqualTo(Integer.valueOf(planCommonCustomize.getRepayStatus()));
		}
		// 传入查询订单状态
		if (StringUtils.isNotEmpty(planCommonCustomize.getOrderStatusSrch())) {	
			cra.andOrderStatusEqualTo(Integer.valueOf(planCommonCustomize.getOrderStatusSrch()));
		}
		// 传入查询应还款日期开始
		if (StringUtils.isNotEmpty(planCommonCustomize.getRepayTimeStart())) {	
			cra.andRepayShouldTimeGreaterThanOrEqualTo(GetDate.getDayStart10(planCommonCustomize.getRepayTimeStart()));
		}
		// 传入查询应还款日期结束
		if (StringUtils.isNotEmpty(planCommonCustomize.getRepayTimeEnd())) {	
			cra.andRepayShouldTimeLessThanOrEqualTo(GetDate.getDayEnd10(planCommonCustomize.getRepayTimeEnd()));
		}
		// 传入查询计划实际还款时间开始
		if (StringUtils.isNotEmpty(planCommonCustomize.getActulRepayTimeStart())) {
			cra.andRepayActualTimeGreaterThanOrEqualTo(GetDate.getDayStart10(planCommonCustomize.getActulRepayTimeStart()));
		}
		// 传入查询计划实际还款时间结束
		if (StringUtils.isNotEmpty(planCommonCustomize.getActulRepayTimeEnd())) {
			cra.andRepayActualTimeLessThanOrEqualTo(GetDate.getDayStart10(planCommonCustomize.getActulRepayTimeEnd()));
		}
		// 订单状态为7的场合、合计已还款、已还本金、已还利息
		cra.andOrderStatusEqualTo(7);
		PlanCommonCustomize result = this.hjhRepayCustomizeMapper.sumHjhRepay(example);
		return result;
	}
	
	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param borrowCommonCustomize
	 * @return
	 * @author PC-LIUSHOUYI
	 */
		
	@Override
	public BorrowRepaymentCustomize sumBorrowRepayment(BorrowRepaymentCustomize borrowCommonCustomize) {
		return this.borrowRepaymentCustomizeMapper.sumHjhBorrowRepayment(borrowCommonCustomize);			
	}

}
