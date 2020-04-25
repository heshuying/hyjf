package com.hyjf.bank.service.borrow.issue;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import com.alibaba.fastjson.JSON;
import com.hyjf.bank.service.borrow.AssetServiceImpl;
import com.hyjf.bank.service.borrow.send.RedisBorrow;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.RedisConstants;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.HjhDebtCredit;
import com.hyjf.mybatis.model.auto.HjhDebtCreditExample;
import com.hyjf.mybatis.model.auto.HjhPlan;
import com.hyjf.mybatis.model.auto.HjhPlanAsset;

@Service
public class AutoIssueServiceImpl extends AssetServiceImpl implements AutoIssueService {
	Logger _log = LoggerFactory.getLogger(AutoIssueServiceImpl.class);

	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;
	

	public static JedisPool pool = RedisUtils.getPool();

	/**
	 * 更新标的计划编号，redis计划
	 * @param borrow
	 * @return
	 */
	@Override
	public boolean updateIssueBorrow(BorrowWithBLOBs borrow, String planNid,HjhPlanAsset hjhPlanAsset){
		// 关联计划
		BorrowWithBLOBs updateBloBs = new BorrowWithBLOBs();
		updateBloBs.setId(borrow.getId());
		
		// 是否可以进行借款
		updateBloBs.setPlanNid(planNid);
		updateBloBs.setLabelId(borrow.getLabelId());
		
		// 更新时间
		int systemNowDateLong = GetDate.getNowTime10();
		Date systemNowDate = GetDate.getDate(systemNowDateLong);
		updateBloBs.setUpdatetime(systemNowDate);
		
		this.borrowMapper.updateByPrimaryKeySelective(updateBloBs);
		
		
		//增加redis相应计划可投金额
		//更新汇计划表
		HjhPlan hjhPlan = new HjhPlan();
		hjhPlan.setPlanNid(planNid);
		hjhPlan.setAvailableInvestAccount(borrow.getAccount());
		this.hjhPlanCustomizeMapper.updatePlanAccount(hjhPlan);
		_log.info(borrow.getBorrowNid()+" 成功更新计划池"+planNid+"总额 + "+borrow.getAccount());

		// 更新资产表
		if(hjhPlanAsset != null){
			HjhPlanAsset hjhPlanAssetnew = new HjhPlanAsset();
			hjhPlanAssetnew.setId(hjhPlanAsset.getId());
			hjhPlanAssetnew.setPlanNid(planNid);
			//获取当前时间
			int nowTime = GetDate.getNowTime10();
			hjhPlanAssetnew.setUpdateTime(nowTime);
			hjhPlanAssetnew.setUpdateUserId(1);
			boolean borrowFlag = this.hjhPlanAssetMapper.updateByPrimaryKeySelective(hjhPlanAssetnew)>0?true:false;
		}
					
		
		redisAdd(RedisConstants.HJH_PLAN+planNid,borrow.getAccount().toString());//增加redis相应计划可投金额
		if (!CustomConstants.INST_CODE_HYJF.equals(borrow.getInstCode())) {
			redisSubstrack(RedisConstants.CAPITAL_TOPLIMIT_+borrow.getInstCode(),borrow.getAccount().toString());//减少风险保证金可投金额
		}
		RedisBorrow redisBorrow = new RedisBorrow();
		redisBorrow.setBorrowNid(borrow.getBorrowNid());
		redisBorrow.setBorrowAccountWait(borrow.getAccount());
		
		RedisUtils.leftpush(RedisConstants.HJH_PLAN_LIST+RedisConstants.HJH_BORROW_INVEST+RedisConstants.HJH_SLASH+planNid, JSON.toJSONString(redisBorrow));//redis相应计划
		

		_log.info(borrow.getBorrowNid()+" 计划编号："+planNid+" 关联计划成功");
		
		return true;
	}

	/**
	 * 更新债转标的计划编号，redis计划
	 * @param credit
	 * @param planNid
	 * @return
	 */
	@Override
	public boolean updateIssueCredit(HjhDebtCredit credit, String planNid){
		// 关联计划
		HjhDebtCredit updateBloBs = new HjhDebtCredit();
		updateBloBs.setId(credit.getId());
		updateBloBs.setPlanNidNew(planNid);
		updateBloBs.setLabelId(credit.getLabelId());
		updateBloBs.setLabelName(credit.getLabelName());
		// 更新时间
		int systemNowDateLong = GetDate.getNowTime10();
		updateBloBs.setUpdateTime(systemNowDateLong);
		this.hjhDebtCreditMapper.updateByPrimaryKeySelective(updateBloBs);
		
		
		//增加redis相应计划可投金额
		//更新汇计划表
		HjhPlan hjhPlan = new HjhPlan();
		hjhPlan.setPlanNid(planNid);
		hjhPlan.setAvailableInvestAccount(credit.getLiquidationFairValue());
//		hjhPlan.setJoinTotal(credit.getLiquidationFairValue());
		this.hjhPlanCustomizeMapper.updatePlanAccount(hjhPlan);
		_log.info(credit.getCreditNid()+" 成功更新计划池"+planNid+"总额 + "+credit.getLiquidationFairValue());
		
		redisAdd(RedisConstants.HJH_PLAN+planNid,credit.getLiquidationFairValue().toString());//增加redis相应计划可投金额
//		redisSubstrack(RedisConstants.CAPITAL_TOPLIMIT_+credit.getInstCode(),credit.getLiquidationFairValue().toString());//减少风险保证金可投金额//TODO:待确认
		
		RedisBorrow redisBorrow = new RedisBorrow();
		redisBorrow.setBorrowNid(credit.getCreditNid());
		redisBorrow.setBorrowAccountWait(credit.getLiquidationFairValue());
		
		RedisUtils.leftpush(RedisConstants.HJH_PLAN_LIST+RedisConstants.HJH_BORROW_CREDIT+RedisConstants.HJH_SLASH+planNid, JSON.toJSONString(redisBorrow));//redis相应计划
		

		_log.info(credit.getCreditNid()+" 计划编号："+planNid+" 关联计划成功");
		
		return true;
	}
	
	/**
	 * 并发情况下保证设置一个值
	 * @param key
	 * @param value
	 */
	private void redisAdd(String key,String value){

		Jedis jedis = pool.getResource();
		
		while ("OK".equals(jedis.watch(key))) {
			List<Object> results = null;
			
			String balance = jedis.get(key);
			BigDecimal bal = new BigDecimal(0);
			if (balance != null) {
				bal =  new BigDecimal(balance);
			}
			BigDecimal val =  new BigDecimal(value);
			
			Transaction tx = jedis.multi();
			String valbeset = bal.add(val).toString();
			tx.set(key, valbeset);
			results = tx.exec();
			if (results == null || results.isEmpty()) {
				jedis.unwatch();
			} else {
				String ret = (String) results.get(0);
				if (ret != null && ret.equals("OK")) {
					// 成功后
					break;
				} else {
					jedis.unwatch();
				}
			}
		}
	}
	
	/**
	 * 并发情况下保证设置一个值
	 * @param key
	 * @param value
	 */
	private boolean redisSubstrack(String key,String value){

		Jedis jedis = pool.getResource();
		boolean result = false;
		
		while ("OK".equals(jedis.watch(key))) {
			List<Object> results = null;
			
			String balance = jedis.get(key);
			BigDecimal bal = new BigDecimal(balance);
			BigDecimal val = new BigDecimal(value);
			
			if(val.compareTo(bal)>0){
				return false;
			}
			
			Transaction tx = jedis.multi();
			String valbeset = bal.subtract(val).toString();
			tx.set(key, valbeset);
			results = tx.exec();
			if (results == null || results.isEmpty()) {
				jedis.unwatch();
			} else {
				String ret = (String) results.get(0);
				if (ret != null && ret.equals("OK")) {
					// 成功后
					result = true;
					break;
				} else {
					jedis.unwatch();
				}
			}
		}
		
		return result;
	}

	/**
	 * 获取一个债转标的
	 * @param creditNid
	 * @return
	 */
	@Override
	public HjhDebtCredit mainGetCreditByNid(String creditNid) {
		HjhDebtCreditExample example = new HjhDebtCreditExample();
		HjhDebtCreditExample.Criteria criteria = example.createCriteria();
		criteria.andCreditNidEqualTo(creditNid);
		List<HjhDebtCredit> list = this.hjhDebtCreditMapper.selectByExample(example);
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}
	
}
