package com.hyjf.batch.datarecover.couponrepay;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.mybatis.model.customize.coupon.CouponTenderCustomize;

/**
 * 体验金按照收益期限还款
 * @author zhangjp
 *
 */
public class RepayDataRecoverTask {

    Logger _log = LoggerFactory.getLogger(RepayDataRecoverTask.class);

    @Autowired
    RepayDataRecoverServiceImpl repayDataRecoverService;

    public void run() {
        dataRecover();
    }

    /**
     * 调用还款接口
     *
     * @return
     */
    private void dataRecover() {
        _log.info("优惠券还款调单数据修复开始...............................");
        try {
            List<String> recoverNidList = new ArrayList<String>();
            recoverNidList.add("HDD161205301");
            recoverNidList.add("HBD170309217");
            recoverNidList.add("GYD170206301");
            recoverNidList.add("HDD17040167");
            recoverNidList.add("HBD170309218");
            recoverNidList.add("HBD170309219");
            recoverNidList.add("GYD170206302");
            recoverNidList.add("HDD161205302");
            recoverNidList.add("HBD170309220");
            recoverNidList.add("HFD17040169");
            recoverNidList.add("NEW17050388");
            recoverNidList.add("HDD161205303");
            recoverNidList.add("HFD17040170");
            recoverNidList.add("HDD161205304");
            recoverNidList.add("GYD170206303");
            recoverNidList.add("HDD161205305");
            recoverNidList.add("HFD17040181");
            recoverNidList.add("HFD170309901");
            recoverNidList.add("HXD17040194");
            recoverNidList.add("HDD161205306");
            recoverNidList.add("HBD170310101");
            recoverNidList.add("HXD17040195");
            recoverNidList.add("NEW17050389");
            recoverNidList.add("HXD17040196");
            recoverNidList.add("HXD17040197");
            recoverNidList.add("HDD161205307");
            for (String borrowNid : recoverNidList) {
                List<CouponTenderCustomize> couponTenderList = repayDataRecoverService.getCouponTenderList(borrowNid);

                for (CouponTenderCustomize ct : couponTenderList) {
                    try {
                        System.out.println("优惠券还款掉单数据修复, 项目编号：" + borrowNid + " 优惠券出借编号：" + ct.getOrderId());
                        repayDataRecoverService.couponRepayDataRecover(borrowNid, 1, ct);
                    } catch (Exception e) {
                        _log.info("直投类优惠券还款数据修复失败，优惠券出借编号：" + ct.getOrderId() + " 项目编号：" + borrowNid);
                        e.printStackTrace();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        _log.info("优惠券还款掉单数据修复结束............................................");
    }
}
