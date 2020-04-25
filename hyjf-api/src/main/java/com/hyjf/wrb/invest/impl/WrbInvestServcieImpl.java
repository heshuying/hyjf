package com.hyjf.wrb.invest.impl;

import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.wrb.*;
import com.hyjf.wrb.invest.WrbInvestServcie;
import com.hyjf.wrb.invest.request.WrbInvestRecordRequest;
import com.hyjf.wrb.invest.response.WrbInvestRecordResponse.WrbInvestRecord;
import com.hyjf.wrb.invest.response.WrbInvestSumResponse;
import com.hyjf.wrb.invest.response.wrbInvestRecoverPlanResponse.WrbRecoverRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

@Service
public class WrbInvestServcieImpl extends BaseServiceImpl implements WrbInvestServcie {


	/**
	 * 获取某天出借情况
	 *
	 * @param invest_date 出借日期 格式2015-10-10
	 * @param limit
	 * @param page
	 * @return
	 */
	@Override
	public List<BorrowTender> getInvestDetail(Date invest_date, Integer limit, Integer page) {
		BorrowTenderExample example = new BorrowTenderExample();
		BorrowTenderExample.Criteria criteria = example.createCriteria();
		// 查询开始条数
		Integer limitStart = (page-1) * limit;
		// 查询结束条数
		Integer limitEnd = limit;
		// 查询开始时间
		Integer timeStart = new Long(invest_date.getTime() / 1000).intValue();
		// 查询结束时间
		Integer timeEnd = new Long(invest_date.getTime() / 1000 + 24 * 60 * 60).intValue();
		criteria.andAddtimeBetween(timeStart, timeEnd);
		example.setLimitStart(limitStart);
		example.setLimitEnd(limitEnd);
		example.setOrderByClause("addtime desc");
		List<BorrowTender> borrowTenderList = borrowTenderMapper.selectByExample(example);

	    return borrowTenderList;

	}

	/**
	 * 获取某天汇总数据
	 *
	 * @param date
	 * @return
	 */
	@Override
	public WrbInvestSumResponse getDaySum(Date date) {

		// 查询开始时间
		Integer timeStart = new Long(date.getTime() / 1000).intValue();
		// 查询结束时间
		Integer timeEnd = new Long(date.getTime() / 1000 + 24 * 60 * 60).intValue();
		WrbDaySumCustomize wrbSumCustomize = wrbQueryCustomizeMapper.getDaySum(timeStart, timeEnd);
        WrbInvestSumResponse response = new WrbInvestSumResponse();
        BigDecimal bd=new BigDecimal(0);
        if(wrbSumCustomize.getAll_wait_back_money() == null){
        	wrbSumCustomize.setAll_wait_back_money(bd);
        }
        if(wrbSumCustomize.getFc_all_wait_back_money() == null){
        	wrbSumCustomize.setFc_all_wait_back_money(bd);
        }
        if(wrbSumCustomize.getInvest_all_money() == null){
        	wrbSumCustomize.setInvest_all_money(bd);
        }
        
        BeanUtils.copyProperties(wrbSumCustomize, response);
        return response;
    }

	/**
	 * 根据标的号和出借开始时间（秒）查询出借信息
	 * 
	 * @param borrowNid
	 * @return
	 */
	public List<WrbBorrowTenderCustomize> searchBorrowTenderByNidAndTime(String borrowNid, int investTime) {
        List<WrbBorrowTenderCustomize> list = wrbQueryCustomizeMapper.selectWrbBorrowTender(borrowNid, investTime);
        for (WrbBorrowTenderCustomize tenderCustomize : list) {
            String username = tenderCustomize.getUsername();
            if (username != null) {
                username = username.substring(0, 1).concat("**");
                tenderCustomize.setUsername(username);
            }
        }
        return list;
	}

	/**
	 * 根据标的号和出借开始时间查询汇总数据
	 * @param borrowNid
	 * @param investTime 秒
	 * @return
	 */
	public WrbBorrowTenderSumCustomize searchBorrowTenderSumByNidAndTime(String borrowNid, int investTime){
		return wrbQueryCustomizeMapper.selectWrbBorrowSumTender(borrowNid, investTime);
	}

	  /**
     * 根据平台用户id获取账户信息
     * @param userId
     * @return
     */
    @Override
    public Account getAccountInfo(String userId) {
        AccountExample example = new AccountExample();
        AccountExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(Integer.valueOf(userId));
        List<Account> accountList = accountMapper.selectByExample(example);
        if (accountList != null) {
            return accountList.get(0);
        }
        return null;
    }
    
	@Override
	public List<WrbBorrowListCustomize> searchBorrowListByNid(String borrowNid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("borrowClass", "");

		// 定向标过滤
		params.put("publishInstCode", "");
		if (StringUtils.isNotBlank(borrowNid)) {
			params.put("borrowNid", borrowNid);
		}else{
			params.put("projectType", "HZT");
			params.put("status", 2);// 获取 出借中
		}
        List<WrbBorrowListCustomize> customizeList = wrbQueryCustomizeMapper.searchBorrowListByNid(params);
		for (WrbBorrowListCustomize wrbBorrowListCustomize : customizeList) {
            String url = wrbBorrowListCustomize.getInvest_url();
            wrbBorrowListCustomize.setInvest_url(url);
        }
        return customizeList;
	}

    /**
     * 获取用户优惠券信息
     * @param userId
     * @return
     */
    @Override
    public List<CouponUser> getCouponInfo(String userId) {
        CouponUserExample example = new CouponUserExample();
        CouponUserExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(Integer.valueOf(userId));
        List<CouponUser> couponUserList = couponUserMapper.selectByExample(example);
        return couponUserList;
    }

    /**
     * 根据优惠券编号获取优惠券配置信息
     * @param couponCode
     * @return
     */
    @Override
    public CouponConfig getCouponByCouponCode(String couponCode) {
        CouponConfigExample example = new CouponConfigExample();
        CouponConfigExample.Criteria criteria = example.createCriteria();
        criteria.andCouponCodeEqualTo(couponCode);
        List<CouponConfig> couponConfigList = couponConfigMapper.selectByExample(example);
        if (couponConfigList.size() != 0) {
            return couponConfigList.get(0);
        }
        return null;
    }

    /**
     * 获取出借记录查询
     * @param request
     * @return
     */
    @Override
    public List<WrbInvestRecordCustomize> getInvestRecord(WrbInvestRecordRequest request) {

        String recordId = request.getInvest_record_id();
        String userId = request.getPf_user_id();

        Map<String, Object> params = new HashMap<>();
        if (request.getStart_time() != null) {
            long startTime = GetDate.stringToDate(request.getStart_time()).getTime() / 1000;
            params.put("startTime", startTime);
        }
        if (request.getEnd_time() != null) {
            long endTime = GetDate.stringToDate(request.getEnd_time()).getTime() / 1000;
            params.put("endTime", endTime);
        }
        params.put("userId", userId);
        params.put("limitStart", request.getOffset());
        params.put("limitEnd",  request.getLimit());
        params.put("nid", recordId);
        params.put("status", request.getInvest_status());
        List<WrbInvestRecordCustomize> investRecordCustomizeList = wrbQueryCustomizeMapper.getInvestRecord(params);

        for (WrbInvestRecordCustomize wrbRecoverCustomize : investRecordCustomizeList) {
            String nid = wrbRecoverCustomize.getInvest_record_id();
            List<WrbRecoverCustomize> recoverList = wrbQueryCustomizeMapper.getRecover(nid);
            List<WrbRecoverCustomize> recoverPlanList = wrbQueryCustomizeMapper.getRecoverPlan(nid);

            if (!CollectionUtils.isEmpty(recoverPlanList)) {
                BeanUtils.copyProperties(recoverPlanList.get(0), wrbRecoverCustomize);
                if (!CollectionUtils.isEmpty(recoverList)) {
                    WrbRecoverCustomize recoverCustomize = recoverList.get(0);
                    wrbRecoverCustomize.setBack_early_state(recoverCustomize.getBack_early_state());
                    wrbRecoverCustomize.setBack_early_time(recoverCustomize.getBack_early_time());
                }
            } else if (!CollectionUtils.isEmpty(recoverList)){
                BeanUtils.copyProperties(recoverList.get(0), wrbRecoverCustomize);
            }
        }
        return investRecordCustomizeList;
    }

    /**
     * 获取出借记录回款计划
     * @param userId
     * @param investRecordId
     * @param borrowNid
     * @return
     */
    @Override
    public List<WrbRecoverRecord> getRecoverPlan(String userId, String investRecordId, String borrowNid) {

        List<WrbRecoverRecord> recoverRecordList = new ArrayList<>();

        BorrowExample example = new BorrowExample();
        BorrowExample.Criteria criteria = example.createCriteria();
        criteria.andBorrowNidEqualTo(borrowNid);
        List<Borrow> borrowList = borrowMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(borrowList)) {
            Borrow borrow = borrowList.get(0);
            String borrowStyle = borrow.getBorrowStyle();
            if ("endday".equals(borrowStyle) || "end".equals(borrowStyle)) {
                // 不分期
                BorrowRecoverExample recoverExample = new BorrowRecoverExample();
                BorrowRecoverExample.Criteria criteria1 = recoverExample.createCriteria();
                criteria1.andBorrowNidEqualTo(borrowNid);
                criteria1.andUserIdEqualTo(Integer.valueOf(userId));
                criteria1.andNidEqualTo(investRecordId);
                List<BorrowRecover> borrowRecoverList = borrowRecoverMapper.selectByExample(recoverExample);
                if(!CollectionUtils.isEmpty(borrowRecoverList)) {
                    WrbRecoverRecord wrbRecoverRecord = new WrbRecoverRecord();
                    BorrowRecover borrowRecover = borrowRecoverList.get(0);
                    // 回款日期
                    wrbRecoverRecord.setBack_date(GetDate.timestamptoStrYYYYMMDD(Integer.valueOf(borrowRecover.getRecoverTime())));
                    // 回款本金
                    wrbRecoverRecord.setBack_principal(borrowRecover.getRecoverCapital());
                    // 回款利息
                    wrbRecoverRecord.setBack_interest(borrowRecover.getRecoverInterest());
                    // 回款金额
                    wrbRecoverRecord.setBack_money(borrowRecover.getRecoverAccount());
                    //0:未回款 1:已回款
                    wrbRecoverRecord.setBack_status(borrowRecover.getStatus());
                    // 服务费
                    wrbRecoverRecord.setService_fee(BigDecimal.ZERO);
                    recoverRecordList.add(wrbRecoverRecord);
                }
            } else {
                // 分期
                BorrowRecoverPlanExample borrowRecoverPlanExample = new BorrowRecoverPlanExample();
                BorrowRecoverPlanExample.Criteria borrowRecoverPlanCriteria = borrowRecoverPlanExample.createCriteria();
                borrowRecoverPlanCriteria.andUserIdEqualTo(Integer.parseInt(userId));
                borrowRecoverPlanCriteria.andBorrowNidEqualTo(borrowNid);
                borrowRecoverPlanCriteria.andNidEqualTo(investRecordId);
                List<BorrowRecoverPlan> borrowRecoverPlanList =  borrowRecoverPlanMapper.selectByExample(borrowRecoverPlanExample);
                if (!CollectionUtils.isEmpty(borrowRecoverPlanList)) {
                    for (BorrowRecoverPlan borrowRecoverPlan : borrowRecoverPlanList) {
                        WrbRecoverRecord wrbRecoverRecord = new WrbRecoverRecord();
                        // 回款日期
                        wrbRecoverRecord.setBack_date(GetDate.timestamptoStrYYYYMMDD(Integer.valueOf(borrowRecoverPlan.getRecoverTime())));
                        // 回款本金
                        wrbRecoverRecord.setBack_principal(borrowRecoverPlan.getRecoverCapital());
                        // 回款利息
                        wrbRecoverRecord.setBack_interest(borrowRecoverPlan.getRecoverInterest());
                        // 回款金额
                        wrbRecoverRecord.setBack_money(borrowRecoverPlan.getRecoverAccount());
                        //0:未回款 1:已回款
                        wrbRecoverRecord.setBack_status(borrowRecoverPlan.getRecoverStatus());
                        // 服务费
                        wrbRecoverRecord.setService_fee(BigDecimal.ZERO);
                        recoverRecordList.add(wrbRecoverRecord);
                    }
                }
            }
        }
        return recoverRecordList;
    }

    @Override
    public Borrow selectBorrowByBorrowNid(String borrowNid) {
        BorrowExample example = new BorrowExample();
        BorrowExample.Criteria criteria = example.createCriteria();
        criteria.andBorrowNidEqualTo(borrowNid);
        List<Borrow> borrows = borrowMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(borrows))  {
            return borrows.get(0);
        }
        return null;
    }

    /**
     * 获取出借记录信息
     * @param borrowTender
     */
    private WrbInvestRecord getInvestRecord(BorrowTender borrowTender,Integer investStatus) {
        WrbInvestRecord record = new WrbInvestRecord();
        record.setInvest_time(GetDate.timestamptoNUMStrYYYYMMDDHHMMSS(borrowTender.getAddtime()));//出借时间
        record.setInvest_money(borrowTender.getAccount());//出借金额
        record.setAll_back_principal(borrowTender.getRecoverAccountCapitalYes());//已回款本金
        record.setAll_back_interest(borrowTender.getRecoverAccountInterestYes());//已回款利息
        record.setAll_interest(borrowTender.getRecoverAccountInterest());//预期收益
        record.setInvest_reward(borrowTender.getTenderAwardAccount());//出借奖励

        String borrowNid = borrowTender.getBorrowNid();

        //查询标的
        BorrowExample borrowExample = new BorrowExample();
        borrowExample.createCriteria().andBorrowNidEqualTo(borrowNid);
        List<Borrow> borrowList = borrowMapper.selectByExample(borrowExample);
        if (!CollectionUtils.isEmpty(borrowList)) {
            Borrow borrow = borrowList.get(0);
            record.setProject_url("/borrow/" + borrow.getBorrowNid());
            //还款状态
            int status = borrow.getStatus();
            switch (status){
                case 2:
                    record.setInvest_status(-1);//出借中
                    break;
                case 4:
                    record.setInvest_status(0);//还款中
                    break;
                case 5:
                    record.setInvest_status(1);//已还款
                    break;
                default:
                    break;
            }

            record.setProject_id(borrow.getBorrowNid());
            record.setProject_rate(borrow.getBorrowApr().floatValue());//标的利率

            String borrowStyle = borrow.getBorrowStyle();

            BorrowRecoverPlanExample borrowRecoverPlanExample = new BorrowRecoverPlanExample();
            BorrowRecoverPlanExample.Criteria borrowRecoverPlanCriteria = borrowRecoverPlanExample.createCriteria();
            borrowRecoverPlanCriteria.andNidEqualTo(borrowTender.getNid());
            borrowRecoverPlanCriteria.andRecoverStatusEqualTo(0);
            borrowRecoverPlanExample.setOrderByClause("recover_time ASC");
            List<BorrowRecoverPlan> borrowRecoverPlanList = borrowRecoverPlanMapper.selectByExample(borrowRecoverPlanExample);

            BorrowRecoverExample borrowRecoverExample = new BorrowRecoverExample();
            BorrowRecoverExample.Criteria criteria = borrowRecoverExample.createCriteria();
            criteria.andNidEqualTo(borrowTender.getNid());
            criteria.andRecoverStatusEqualTo(0);
            borrowRecoverExample.setOrderByClause("recover_time ASC");
            List<BorrowRecover> borrowRecoverList = borrowRecoverMapper.selectByExample(borrowRecoverExample);


            if (!CollectionUtils.isEmpty(borrowRecoverPlanList)) {
                BorrowRecoverPlan borrowRecoverPlan = borrowRecoverPlanList.get(0);
                // 下一回款日
                record.setNext_back_date(GetDate.timestamptoStrYYYYMMDD(Integer.valueOf(borrowRecoverPlan.getRecoverTime())));
                // 下一回款金额
                record.setNext_back_money(borrowRecoverPlan.getRecoverAccount());
                // 下一回款本金
                record.setNext_back_principal(borrowRecoverPlan.getRecoverCapital());
                // 下一回款利息
                record.setNext_back_interest(borrowRecoverPlan.getRecoverInterest());
            } else if (!CollectionUtils.isEmpty(borrowRecoverList)){
                BorrowRecover borrowRecover = borrowRecoverList.get(0);
                // 下一回款日
                record.setNext_back_date(GetDate.timestamptoStrYYYYMMDD(Integer.valueOf(borrowRecover.getRecoverTime())));
                // 下一回款金额
                record.setNext_back_money(borrowRecover.getRecoverAccount());
                // 下一回款本金
                record.setNext_back_principal(borrowRecover.getRecoverCapital());
                // 下一回款利息
                record.setNext_back_interest(borrowRecover.getRecoverInterest());
            }

            if (!CollectionUtils.isEmpty(borrowRecoverList)) {
                BorrowRecover recover = borrowRecoverList.get(0);
                String recoverTime = GetDate.timestamptoStrYYYYMMDD(Integer.valueOf(recover.getRecoverTime()));
                if (!"endday".equals(borrowStyle) || "endmonth".equals(borrowStyle)) {
                    record.setMonthly_back_date(recoverTime.substring(recoverTime.lastIndexOf("-") + 1));
                }

                if (recover.getChargeDays() > 8 && recover.getAdvanceStatus() == 1) {
                    //提前还款
                    record.setInvest_status(3);
                    // 提前还款
                    record.setBack_early_state(1);
                    // 提前还款天数
                    Integer aheadDays = recover.getAheadDays();
                    String aheadTime = GetDate.timestamptoNUMStrYYYYMMDDHHMMSS(Integer.valueOf(recover.getRecoverTime()) - aheadDays * 24 * 60 * 60);
                    record.setBack_early_time(aheadTime);
                } else {
                    // 未提前还款
                    record.setBack_early_state(0);
                }
            }

            BorrowStyleExample borrowStyleExample = new BorrowStyleExample();
            BorrowStyleExample.Criteria criteria2 = borrowStyleExample.createCriteria();
            criteria2.andNidEqualTo(borrowStyle);
            List<BorrowStyle> borrowStyleList = borrowStyleMapper.selectByExample(borrowStyleExample);
            if (borrowStyleList != null) {
                record.setPayback_way(borrowStyleList.get(0).getName());
            }

            // 查询债转信息
            BorrowCreditExample creditExample = new BorrowCreditExample();
            BorrowCreditExample.Criteria criteria3 = creditExample.createCriteria();
            criteria3.andTenderNidEqualTo(borrowNid);
            List<BorrowCredit> borrowCreditList = borrowCreditMapper.selectByExample(creditExample);
            if ((borrowCreditList.size() > 0)) {
                BorrowCredit borrowCredit = borrowCreditList.get(0);
                // 转让
                record.setAttorn_state(1);
                record.setAttorn_time(GetDate.timestamptoStrYYYYMMDDHHMMSS(borrowCredit.getAddTime()));
            } else {
                // 未转让
                record.setAttorn_state(0);
            }

            Integer client = borrowTender.getClient();
            if (client == 0) {
                record.setSuorce(1);
            } else if(client == 2 || client == 3) {
                record.setSuorce(2);
            } else {
                record.setSuorce(0);
            }
        }
        return record;
    }

}
