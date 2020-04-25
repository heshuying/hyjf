package com.hyjf.coupon.loans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.hyjf.base.service.BaseService;
import com.hyjf.common.calculate.CalculatesUtil;
import com.hyjf.common.calculate.InterestInfo;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.BorrowTenderCpn;
import com.hyjf.mybatis.model.auto.BorrowTenderCpnExample;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.CouponConfig;
import com.hyjf.mybatis.model.auto.CouponRecover;
import com.hyjf.mybatis.model.auto.HjhPlan;

public interface CouponLoansService extends BaseService {
    /**
     * 自动放款（优惠券）
     * @param apicron
     * @param borrowTender
     * @return
     * @throws Exception
     */
    List<Map<String, String>> updateCouponRecover(BorrowTenderCpn borrowTenderCpn) throws Exception;

    /**
     * 取得标的详情
     *
     * @return
     */
    public BorrowWithBLOBs getBorrow(String borrowNid);

    
    /**
     * 取得优惠券借款列表
     * @param borrowNid
     * @return
     */
    List<BorrowTenderCpn> getBorrowTenderCpnList(String borrowNid);
    
    /**
     * 更新放款状态(优惠券)
     *
     * @param accountList
     * @return
     */
    public int updateBorrowTenderCpn(BorrowTenderCpn borrowTenderCpn);
    
    /**
     * 发送短信(优惠券出借成功)
     *
     * @param userId
     */
    void sendSmsCoupon(List<Map<String, String>> msgList);
    
    /**
     * 根据订单编号，检查订单是否优惠券出借
     * @param tenderNid
     */
    boolean checkCouponTender(String tenderNid);
    
    /**
     * 计算vip用户的V值
     * @param borrowLoans
     */
    void updateVipValue(CouponLoansBean borrowLoans);
    
    /**
     * App消息推送（优惠券出借成功）
     * @param msgList
     */
    void sendAppMSCoupon(List<Map<String, String>> msgList);

    List<BorrowTenderCpn> getBorrowTenderCpnHjhList(String orderId);

	List<Map<String, String>> updateCouponRecoverHjh(BorrowTenderCpn borrowTenderCpn, String realOrderId) throws Exception;

	List<BorrowTenderCpn> getBorrowTenderCpnHjhCouponOnlyList(String couponOrderId);
}
