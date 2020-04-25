package com.hyjf.admin.exception.rechargeexception;

import java.util.HashMap;
import java.util.List;

import com.hyjf.mybatis.model.auto.AccountRecharge;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.RechargeCustomize;
import com.hyjf.mybatis.model.customize.UserInfoCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;

public interface RechargeExceptionService {

    /**
     * 充值管理 （账户数量）
     * @param accountManageBean
     * @return
     */
    public Integer queryRechargeCount(RechargeCustomize rechargeCustomize);

    /**
     * 充值管理 （列表）
     * @param accountManageBean
     * @return
     */
    public List<RechargeCustomize> queryRechargeList(RechargeCustomize rechargeCustomize);

    /**
     * 根据订单号nid获取充值信息
     * @param nid
     * @return
     */
    public AccountRecharge queryRechargeByNid(String nid);

    /**
     * 
     * 根据主键id查询单挑充值信息
     * @author renxingchen
     * @param id
     * @return
     */
    public RechargeCustomize queryRechargeById(Integer id);

    /**
     * 
     * @param accountRecharge
     * @return
     */
    public int updateRecharge(AccountRecharge accountRecharge);

    /**
     * 手动充值处理
     * @param form
     * @param chinapnrBean
     * @return
     */
    public int updateHandRechargeRecord(RechargeExceptionBean form, ChinapnrBean chinapnrBean,
        UserInfoCustomize userInfoCustomize);

    /**
     * 根据用户名查询用户ID
     * @param username
     * @return
     */
    public Users queryUserInfoByUserName(String username);

    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    public UserInfoCustomize queryUserInfoByName(String username);

    /**
     * 
     * 检查更新状态是充值中的数据状态
     * @author renxingchen
     * @param bean
     * @param bigDIntegerecimal
     * @param integer
     * @return
     */
    public HashMap<String, String> handleRechargeStatus(ChinapnrBean bean, Integer userId, String feeFrom);

}
