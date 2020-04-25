package com.hyjf.batch.synchronizeMeaage.account;

import com.hyjf.batch.synchronizeMeaage.mobile.MobileSynchronizeService;
import com.hyjf.mybatis.mapper.auto.AccountMobileAynchMapper;
import com.hyjf.mybatis.model.auto.AccountMobileAynch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author lisheng
 * @version AccountSynchronizeTask, v0.1 2018/5/10 16:56
 */

public class AccountSynchronizeTask {

    /** 类名 */
    private static final String THIS_CLASS = AccountSynchronizeTask.class.getName();
    Logger _log = LoggerFactory.getLogger(AccountSynchronizeTask.class);
    // 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
    private static Boolean isOver = true;
    @Autowired
    BankCardSynchronizeService bankCardSynchronizeService;
    @Autowired
    MobileSynchronizeService mobileSynchronizeService;
    @Autowired
    AccountMobileAynchMapper accountMobileAynchMapper;
    public void run() {
        String methodName = "run";
        if (isOver) {
            _log.info(THIS_CLASS + "==>" + methodName + "==>" + "开始同步银行卡号！");
            try {
                isOver = false;
                List<AccountMobileAynch> accountMobileAynches = mobileSynchronizeService.searchAccountMobileAynch("2");
                for (AccountMobileAynch accountMobileAynch : accountMobileAynches) {
                    // 同步银行号
                    bankCardSynchronize(accountMobileAynch);
                }
            }catch(Exception e){
                _log.info(THIS_CLASS + "==>" + methodName + "==>" + "查询同步银行卡号异常！，查询数据失败");
                e.printStackTrace();
            } finally {
                isOver = true;
            }
        }

    }

    public void  bankCardSynchronize(AccountMobileAynch accountMobileAynch){
        Integer userId = accountMobileAynch.getUserId();
        // 返回结果
        try {
            boolean flag = bankCardSynchronizeService.updateAccountBankByUserId(accountMobileAynch);
            if(flag){
                _log.info(THIS_CLASS + "==>" + userId + "==>" + "同步银行卡号成功！");
            }else{
                _log.info(THIS_CLASS + "==>" + userId + "==>" + "同步银行卡号失败！");
            }

        } catch (Exception e) {
            _log.info(THIS_CLASS + "==>" + userId + "==>" + "同步银行卡号异常！");
            e.printStackTrace();
        }
    }
}
