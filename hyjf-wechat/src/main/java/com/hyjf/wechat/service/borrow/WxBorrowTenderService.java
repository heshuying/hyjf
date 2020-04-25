/**
 * Description:用户出借服务
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 郭勇
 * @version: 1.0
 *           Created at: 2015年12月4日 下午1:45:13
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.wechat.service.borrow;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.wechat.base.BaseService;

public interface WxBorrowTenderService extends BaseService {

    /**
     * 保存用戶的投資数据
     * @param borrow
     * @param bean
     * @return
     * @throws Exception 
     */
    JSONObject userTender(Borrow borrow, BankCallBean bean) throws Exception;
    
    /**
     * 操作redis
     * @param userId 
     * @param borrowNid
     * @param account
     * @return
     */
    JSONObject redisTender(Integer userId, String borrowNid, String txAmount);
    
    void extraUeldInvest(Borrow borrow, BankCallBean bean);
    
    /**
     * 更新出借记录临时表
     * 
     * @param userId
     * @param ordId
     * @return
     * @author Administrator
     */
    boolean updateBorrowTenderTmp(Integer userId, String borrowNid, String ordId);
    
    /**
     * 
     * 删除临时表记录
     * @author pcc
     * @param userId
     * @param borrowNid
     * @param orderId
     * @param account
     * @return
     * @throws Exception
     */
    boolean bidCancel(Integer userId, String borrowNid, String orderId, String account) throws Exception;
    
    /**
     * 根据优惠券id判断优惠券是否被使用
     * add by cwyang 2017-5-11
     * @param couponGrantId
     * @return
     */
    public JSONObject getCouponIsUsed(String orderID,String couponGrantId,int userId);

    boolean updateTender(Integer userId, String borrowNid, String orderId, BankCallBean bean);

    /**
     * 发送神策数据统计MQ
     * @param sensorsDataBean
     */
    void sendSensorsDataMQ(SensorsDataBean sensorsDataBean);

    /**
     * 获取还款方式配置
     * @param borrowStyle
     * @return
     */
    BorrowStyle getProjectBorrowStyle(String borrowStyle);
}
