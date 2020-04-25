package com.hyjf.wrb.invest;


import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.wrb.WrbBorrowListCustomize;
import com.hyjf.mybatis.model.customize.wrb.WrbBorrowTenderCustomize;
import com.hyjf.mybatis.model.customize.wrb.WrbBorrowTenderSumCustomize;
import com.hyjf.mybatis.model.customize.wrb.WrbInvestRecordCustomize;
import com.hyjf.wrb.invest.request.WrbInvestRecordRequest;
import com.hyjf.wrb.invest.response.WrbInvestSumResponse;
import com.hyjf.wrb.invest.response.wrbInvestRecoverPlanResponse;

import java.util.Date;
import java.util.List;

public interface WrbInvestServcie {

    /**
     * 获取某天出借情况
     *
     * @param invest_date 出借日期 格式2015-10-10
     * @param limit
     * @param page
     * @return
     */
    List<BorrowTender> getInvestDetail(Date invest_date, Integer limit, Integer page);

    /**
     * 获取某天汇总数据
     *
     * @param date
     * @return
     */
    WrbInvestSumResponse getDaySum(Date date);

    /**
     * 根据标的号和出借开始时间查询出借信息
     * @param borrowNid
     * @param investTime 秒
     * @return
     */
    List<WrbBorrowTenderCustomize> searchBorrowTenderByNidAndTime(String borrowNid, int investTime);

    /**
     * 根据标的号和出借开始时间查询汇总数据
     * @param borrowNid
     * @param investTime 秒
     * @return
     */
    WrbBorrowTenderSumCustomize searchBorrowTenderSumByNidAndTime(String borrowNid, int investTime);

    /**
     * 根据平台用户id获取账户信息
     * @param userId
     * @return
     */
    Account getAccountInfo(String userId);

    /**
     * 根据标的ID查询可出借标的信息
     * @param borrowNid 标的ID
     * @return
     */
	List<WrbBorrowListCustomize> searchBorrowListByNid(String borrowNid);

    /**
     * 获取用户优惠券信息
     * @param userId
     * @return
     */
    List<CouponUser> getCouponInfo(String userId);

    /**
     * 根据优惠券编号获取优惠券配置信息
     * @param couponCode
     * @return
     */
    CouponConfig getCouponByCouponCode(String couponCode);

    /**
     * 出借记录查询
     * @param request
     * @return 出借记录
     * @throws Exception
     */
    List<WrbInvestRecordCustomize> getInvestRecord(WrbInvestRecordRequest request) throws Exception;

    /**
     * 获取出借记录回款计划
     * @param userId
     * @param investRecordId 流水号
     * @param borrowNid
     * @return
     */
    List<wrbInvestRecoverPlanResponse.WrbRecoverRecord> getRecoverPlan(String userId, String investRecordId, String borrowNid);

    /**
     * 根据borrowNid查询borrow
     * @param borrowNid
     * @return
     */
    Borrow selectBorrowByBorrowNid(String borrowNid);
}
