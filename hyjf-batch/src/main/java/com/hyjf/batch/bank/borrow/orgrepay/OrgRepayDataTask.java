package com.hyjf.batch.bank.borrow.orgrepay;

import com.hyjf.mybatis.model.auto.Borrow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * 批量还款展示数据计算
 */
public class OrgRepayDataTask {

    Logger _log = LoggerFactory.getLogger(OrgRepayDataTask.class);

    @Autowired
    private OrgRepayDataService service;

    /** 运行状态 */
    private static int isRun = 0;

    public void run() {

        if (isRun == 1) {
            return;
        }
        isRun = 1;
        _log.info("-----------------批量还款展示数据开始计算--------------------------------");
        try {

            //获取所有属于垫付机构还款的还款中标的
            List<Borrow> list = service.getOrgRepayBorrowList();
            if (list == null){
                _log.info("=================没有需要计算的批量还款展示数据=========================");
            }else{
                for (Borrow borrow: list) {
                    try{
                        service.updateOrgRepayData(borrow);
                    }catch (Exception e){
                        _log.info("=======批量还款展示数据计算异常，标的号：" + borrow.getBorrowNid() + ",错误信息：" + e.getMessage());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            _log.info("=======批量还款展示数据计算异常：" + e.getMessage());
        } finally {
            isRun = 0;

        }
        _log.info("=================批量还款展示数据计算结束！=========================");



    }


}
