/**
 * Description:项目列表查询service接口
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.wechat.service.borrow;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowManinfo;
import com.hyjf.mybatis.model.auto.BorrowRepay;
import com.hyjf.mybatis.model.auto.BorrowRepayPlan;
import com.hyjf.mybatis.model.auto.BorrowUsers;
import com.hyjf.mybatis.model.customize.app.AppProjectDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectInvestListCustomize;
import com.hyjf.mybatis.model.customize.app.AppRiskControlCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigCustomizeV2;
import com.hyjf.wechat.base.BaseService;
import com.hyjf.wechat.model.borrow.WxTenderVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface WxBorrowService extends BaseService {

    /**
     * 根据项目id获取项目信息
     * 
     * @param borrowNid
     * @return
     */
    AppProjectDetailCustomize selectProjectDetail(String borrowNid);
    
    /**
     * 根据borrowNid查询用户对象
     * @param borrowNid
     * @return
     */
    BorrowUsers getBorrowUsersByNid(String borrowNid);

    /**
     * 根据borrowNid获取借款人信息
     * @param borrowNid
     * @return
     */
    BorrowManinfo getBorrowManinfoByNid(String borrowNid);
    
    
    /**
     * 根据borrowNid查询List
     * @param borrowNid
     * @return
     */
    List<BorrowRepayPlan> findRepayPlanByBorrowNid(String borrowNid);
    
    
    /**
     * 统计用户出借的项目总数
     * 
     * @param params
     * @return
     */
    int countProjectInvestRecordTotal(Map<String, Object> params);
    
    
    /**
     * 根据borrowNid查询所发标的出借金额
     * @param params
     * @return
     */
    String countMoneyByBorrowId(Map<String, Object> params);
    
    /**
     * 查询项目的用户出借列表
     * 
     * @param params
     * @return
     */
    List<AppProjectInvestListCustomize> searchProjectInvestList(Map<String, Object> params);
    /**
     * 
     * 获取优惠券信息
     * @author pcc
     * @param couponGrantId
     * @param userId
     * @return
     */
    CouponConfigCustomizeV2 getCouponUser(String couponGrantId, Integer userId);
    /**
     * 
     * 出借校验
     * @author pcc
     * @param borrowNid
     * @param account
     * @param valueOf
     * @param string
     * @param cuc
     * @return
     */
    JSONObject checkParam(String borrowNid, String account, String valueOf, CouponConfigCustomizeV2 cuc);
    /**
     * 
     * 校验优惠券信息
     * @author pcc
     * @param borrow
     * @param vo
     * @param valueOf
     * @param cuc
     * @param couponGrantId
     * @return
     */
    JSONObject checkParamForCoupon(Borrow borrow, WxTenderVo vo, String valueOf, CouponConfigCustomizeV2 cuc,
        String couponGrantId);
    /**
     * 调用汇付天下接口前操作,
     * 插入huiyingdai_borrow_tender_tmp和huiyingdai_borrow_tender_tmpinfo表
     *
     * @param borrowId
     *            借款id
     * @param userId
     *            用户id
     * @param account
     *            出借金额
     * @param ip
     *            出借人ip
     * @return 出借是否成功
     */
    Boolean updateBeforeChinaPnR(HttpServletRequest request, String borrowNid, String orderId, Integer valueOf, String account, String ipAddr,
                                 String couponGrantId, String tenderUserName);

    /**
     * 项目可投金额
     * @param borrowNid
     * @return
     */
    String getBorrowAccountWait(String borrowNid);

	AppRiskControlCustomize selectRiskControl(String borrowNid);
	
	/**
	 * 获取还款信息
	 * add by jijun 2018/04/27
	 * @param borrowNid
	 * @return
	 */
	BorrowRepay getBorrowRepay(String borrowNid);
}
