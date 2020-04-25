package com.hyjf.batch.datarecover.couponrepay.bank;

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
            recoverNidList.add("HFD17050404");
            recoverNidList.add("NEW17060480");
            recoverNidList.add("NEW17060479");
            recoverNidList.add("HDD17050314");
            recoverNidList.add("NEW17060389");
            recoverNidList.add("HDD17050316");
            recoverNidList.add("HDD17050315");
            recoverNidList.add("HDD17050318");
            recoverNidList.add("NEW17060387");
            recoverNidList.add("HDD17050313");
            recoverNidList.add("NEW17060388");
            recoverNidList.add("HXD160706701");
            recoverNidList.add("HXD160706702");
            
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
