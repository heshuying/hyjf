/**
 * Description:我的出借service接口
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 *           Created at: 2015年11月11日 下午2:17:31
 *           Modification History:
 *           Modified by :
 */
package com.hyjf.web.user.mytender;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowRecover;
import com.hyjf.mybatis.model.auto.BorrowRecoverExample;
import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.BorrowCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectRepayListCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserInvestListCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserProjectListCustomize;
import com.hyjf.web.BaseService;

public interface MyTenderService extends BaseService {

    /**
     * 我的出借列表查询
     * @param hzt
     * @param i
     * @param pageSize
     * @return
     */
    List<WebUserProjectListCustomize> selectUserProjectList(Map<String, Object> params);

    /**
     * 我的出借数据总数
     * @param params
     * @return
     */
    int countUserProjectRecordTotal(Map<String, Object> params);

    /**
     * 用户协议相应的用户出借列表
     * @param borrowNid
     * @param offset
     * @param limit
     * @return
     */
    List<WebUserInvestListCustomize> selectUserInvestList(UserInvestListBean form, int offset, int limit);

    /**
     * 用户协议想用的用户出借总数
     * @param borrowNid
     * @return
     */
    int countUserInvestRecordTotal(UserInvestListBean form);

    /**
     * 用户出借为分期时的用户还款总数
     * @param form
     * @return
     */
    int countProjectRepayPlanRecordTotal(ProjectRepayListBean form);

    /**
     * 用户出借为分期时用户还款信息
     * @param form
     * @param offset
     * @param limit
     * @return
     */
    List<WebProjectRepayListCustomize> selectProjectRepayPlanList(ProjectRepayListBean form, int offset, int limit);

    /**
     * 借款列表
     * 
     * @return
     */
    public List<BorrowCustomize> selectBorrowList(BorrowCommonCustomize borrowCommonCustomize);
    
    public List<BorrowRecover> selectBorrowRecover(BorrowRecoverExample example);

    /**
     * 下载PDF文件（平台居间服务协议）
     * @param userid
     * @param nid
     * @param borrownid
     */
    public void createAgreementPDF(String userId, String nid, String borrowNid);

    /**
     * 
     * 优惠券还款计划记录数
     * @author hsy
     * @param form
     * @return
     */
    public int countCouponProjectRepayPlanRecordTotal(ProjectRepayListBean form);

    /**
     * 
     * 优惠券还款计划记录列表
     * @author hsy
     * @param form
     * @param offset
     * @param limit
     * @return
     */
    public List<WebProjectRepayListCustomize> selectCouponProjectRepayPlanList(ProjectRepayListBean form, int offset,
        int limit);

    /**
     * 
     * 优惠券总收益
     * @author hsy
     * @param userId
     * @return
     */
    public String queryCouponInterestTotal(Integer userId);

    /**
     * 
     * 汇添金借款列表
     * @author renxingchen
     * @param borrowCommonCustomize
     * @return
     */
    List<DebtBorrowCustomize> selectDebtBorrowList(DebtBorrowCommonCustomize debtBorrowCommonCustomize);
    
	/**
	 * 根据借款id获取借款信息
	 * 
	 * @param borrowId
	 * @return
	 */
	public Borrow getBorrowByNid(String borrowNid);

}
