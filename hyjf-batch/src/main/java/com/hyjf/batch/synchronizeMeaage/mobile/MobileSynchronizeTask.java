package com.hyjf.batch.synchronizeMeaage.mobile;

import com.hyjf.mybatis.model.auto.AccountMobileAynch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author lisheng
 * @version MobileSynchronizeTask, v0.1 2018/5/10 16:55
 */

public class MobileSynchronizeTask {

    /** 类名 */
    private static final String THIS_CLASS = MobileSynchronizeTask.class.getName();
    Logger _log = LoggerFactory.getLogger(MobileSynchronizeTask.class);
    // 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
    private static Boolean isOver = true;
    @Autowired
    MobileSynchronizeService mobileSynchronizeService;

    public void run() {
        String methodName = "run";
        if (isOver) {
            _log.info(THIS_CLASS + "==>" + methodName + "==>" + "开始同步手机号！");
            try {
                isOver = false;
                List<AccountMobileAynch> accountMobileAynches = mobileSynchronizeService.searchAccountMobileAynch("1");
                for (AccountMobileAynch accountMobileAynch : accountMobileAynches) {
                        // 同步手机号
                        mobileSynchronize(accountMobileAynch);
                }
            }catch(Exception e){
                _log.info(THIS_CLASS + "==>" + methodName + "==>" + "同步手机号异常！"+e.getMessage());
                e.printStackTrace();
            } finally {
                isOver = true;
            }
        }

    }

    public void mobileSynchronize(AccountMobileAynch accountMobileAynch){
        Integer userId = accountMobileAynch.getUserId();
        // 返回结果
        try {
            boolean isUpdateFlag = mobileSynchronizeService.updateMobile(accountMobileAynch);

            if (isUpdateFlag) {
                _log.info(THIS_CLASS + "==>" + userId + "==>" + "同步手机号成功！");
                // 同步手机后,重新发送CA认证客户信息修改MQ start
                this.mobileSynchronizeService.sendCAMQ(userId+"");
                // 同步手机后,重新发送CA认证客户信息修改MQ end
            }else{
                _log.info(THIS_CLASS + "==>" + userId + "==>" + "同步手机号失败！");
            }
        } catch (Exception e) {
            _log.info(THIS_CLASS + "==>" + userId + "==>" + "同步手机号异常！");
            e.printStackTrace();
        }
    }
}
