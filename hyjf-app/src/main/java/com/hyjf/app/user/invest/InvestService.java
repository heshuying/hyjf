/**
 * Description:用户出借服务
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 *           Created at: 2015年12月4日 下午1:45:13
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.app.user.invest;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseService;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigCustomizeV2;
import com.hyjf.mybatis.model.customize.coupon.UserCouponConfigCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;

public interface InvestService extends BaseService {

    /**
     * 根据用户汇付客户号获取用户id
     * 
     * @param usrCustId
     * @return
     */
    public Integer getUserIdByUsrcustId(String usrCustId);

    /**
     * 根据项目类型获取项目类别对象
     * 
     * @param projectType
     * @return
     */
    public BorrowProjectType getBorrowProjectType(String projectType);

    /**
     * 获取用户对象
     * 
     * @param userId
     * @return
     */
    public Users getUsers(Integer userId);

    /**
     * 获取用户信息对象
     * 
     * @param userId
     * @return
     */
    public UsersInfo getUserInfo(Integer userId);

    /**
     * 判断用户是否可以投新用户标
     * 
     * @param userId
     * @param projectType
     * @return
     */
    public boolean checkIsNewUserCanInvest(Integer userId, Integer projectType);

    /**
     * 判断用户是否可以投51老用户标
     * 
     * @param userId
     * @param projectType
     * @return
     */
    public boolean checkIs51UserCanInvest(Integer userId);

    /**
     * 更新借款
     * 
     * @param record
     * @return
     */
    public boolean updateBorrow(Borrow record);

    /**
     * 冻结
     * 
     * @param userId
     * @param account
     * @param tenderUsrcustid
     * @param borrowerUsrcustid
     * @param OrdId
     * @param uuid
     * @return
     */
    public FreezeDefine freeze(Integer userId, String account, String tenderUsrcustid, String borrowerUsrcustid,
        String OrdId);

    /**
     * 获取用户的汇付信息
     * 
     * @param userId
     * @return 用户的汇付信息
     */
    public AccountChinapnr getAccountChinapnr(Integer userId);

    /**
     * 调用汇付天下接口前操作
     * 
     * @param borrowId
     *            借款id
     * @param userId
     *            用户id
     * @param account
     *            出借金额
     * @param ip
     *            出借人ip
     * @return 操作是否成功
     */
    public Boolean updateBeforeChinaPnR(String borrowNid, String OrdId, Integer userId, String account, String ip, String couponGrantId, String userName);

    /**
     * 调用汇付天下接口成功后操作
     * 
     * @param borrowId
     *            借款id
     * @param userId
     *            用户id
     * @param account
     *            出借金额
     * @param ip
     *            出借人ip
     * @param client
     *            客户端0PC，1微信2安卓APP，3IOS APP，4其他
     * @return 操作是否成功
     */
    public JSONObject userTender(Borrow borrow, ChinapnrBean bean);

    /**
     * 优惠券出借
     * 
     * @param borrowId
     *            借款id
     * @param userId
     *            用户id
     * @param account
     *            出借金额
     * @param ip
     *            出借人ip
     * @param client
     *            客户端0PC，1微信2安卓APP，3IOS APP，4其他
     * @return 操作是否成功
     */
    Boolean updateCouponTender(String couponGrantId, String borrowNid, String ordDate, Integer userId, String account,
        String ip, Integer client, int orderTime, String mainTenderNid, Map<String, Object> retMap);

    /**
     * 取得用户优惠券信息
     * 
     * @param couponGrantId
     * @param userId
     * @return
     */
    CouponConfigCustomizeV2 getCouponUser(String couponGrantId, int userId);

    /**
     * 恢复redis
     * 
     * @param borrowNid
     * @param userId
     * @param account
     * @return
     */
    public boolean recoverRedis(String borrowNid, Integer userId, String account);

    /**
     * @param borrowNid
     * @param account
     * @param userId
     * @param platform
     * @param cuc
     * @return
     */

    public JSONObject checkParam(String borrowNid, String account, String userId, String platform,
        CouponConfigCustomizeV2 cuc);

    /**
     * 优惠券出借校验
     * 
     * @param borrowNid
     * @param account
     * @param userId
     * @return
     */
    JSONObject checkParamForCoupon(Borrow borrow, AppTenderVo vo, String userId, CouponConfigCustomizeV2 cuc,String couponGrantId);

    /**
     * 解冻用户出借冻结
     * 
     * @param borrowUserId
     * @param investUserId
     * @param borrowNid
     * @param orderId
     * @param trxId
     * @param ordDate
     * @return
     * @throws Exception
     */
    boolean unFreezeOrder(int borrowUserId, int investUserId, String orderId, String trxId, String ordDate)
        throws Exception;

    /**
     * 
     * 获取用户最优优惠券
     * 
     * @author pcc
     * @param borrowNid
     * @param money
     * @param userId
     * @param platform
     * @return
     */
    public UserCouponConfigCustomize getBestCoupon(String borrowNid, Integer userId, String money, String platform);

    /**
     * 
     * 获取用户优惠券id获得优惠券信息
     * 
     * @author pcc
     * @param couponId
     * @return
     */
    public UserCouponConfigCustomize getCouponById(String couponId);

    /**
     * 
     * @method: updateUserInvestFlagById
     * @description: 新手同时投标判断1
     * @param userId
     * @return
     * @return: boolean
     * @mender: zhouxiaoshuai
     * @date: 2016年5月30日 下午5:53:59
     */
    public boolean updateUserInvestFlagById(Integer userId);

    /**
     * 
     * 校验出借时优惠券是否适用当前项目类别
     * 
     * @author pcc
     * @param projectType
     * @param projectType2
     * @return
     */
    public String validateCouponProjectType(String projectTypeList, String projectTypeCd);

    /**
     * 
     * 根据用户id查询tender表数量
     * 
     * @author pcc
     * @param userId
     * @param borrowNid
     * @param borrowProjectType
     * @return
     */
    public int countBorrowTenderNum(Integer userId, String borrowNid, String ordId);

    /**
     * 更新用户的出借临时表
     * @param userId
     * @param borrowNid
     * @param ordId
     * @return
     */
    public int updateBorrowTenderTmp(Integer userId, String borrowNid, String ordId);

    /**
     * 查询用户优惠券数据
     * @param couponGrantId
     * @param borrow
     * @param userId
     * @param account
     * @param couponUpdateTime
     * @return
     */
    public Map<String, Object> queryCouponData(String couponGrantId, Borrow borrow, Integer userId, String account,
        int couponUpdateTime);

    /**
     * 查询用户是否使用优惠券成功
     * @param orderId
     * @param couponGrantId
     * @param userId
     * @return
     */
    public boolean updateCouponTenderStatus(String orderId, String couponGrantId, Integer userId);

    /**
     * 
     * @method: checkIfSendCoupon
     * @description: 判断是否可以发送出借1000加息券
     * @param user
     * @param account
     * @return
     * @return: boolean
     * @mender: zhouxiaoshuai
     * @date: 2016年8月25日 下午4:34:06
     */
    public boolean checkIfSendCoupon(Users user, String account);
    
    public BorrowProjectType getProjectTypeByBorrowNid(String borrowNid);

	public void extraUeldInvest(Borrow borrow, ChinapnrBean bean);

	/**
	 * 根据出借唯一标识获取对象
	 * @param projectType 出借唯一标识
	 * @return
	 */
	public BorrowProjectType getProjectType(Integer projectType);

	
	/**
	 * 判断是否是债转标
	 */
	public int getCountByBorrowId(String borrowId);

    /**
     * 按照出借订单号查询出借记录
     * @param logOrderId
     * @return
     */
    BorrowTender getTenderByNid(String logOrderId);
}
