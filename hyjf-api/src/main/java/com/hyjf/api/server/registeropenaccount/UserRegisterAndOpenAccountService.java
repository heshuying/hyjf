package com.hyjf.api.server.registeropenaccount;

import javax.servlet.http.HttpServletRequest;

import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.auto.HjhInstConfig;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

/**
 * 外部服务接口:用户注册加开户Service
 *
 * @author liuyang
 */
public interface UserRegisterAndOpenAccountService extends BaseService {
    /**
     * 根据手机号检索用户是否已在平台注册
     *
     * @param mobile
     * @return
     */
    Users selectUserByMobile(String mobile);

    /**
     * 根据机构编号检索机构信息
     *
     * @param instCode
     * @return
     */
    HjhInstConfig selectInstConfigByInstCode(String instCode);

    /**
     * 根据手机号注册用户
     *
     * @param mobile
     * @param instCode
     * @return
     */
    Integer insertUserAction(String mobile, String instCode, HttpServletRequest request);

    /**
     * 开户失败后,根据用户ID删除用户信息
     *
     * @param userId
     */
    void userDeleteByUserId(Integer userId);

    /**
     * 开户成功后保存
     *
     * @param openAccountResult
     * @return
     */
    boolean updateUserAccount(BankCallBean openAccountResult);

    /**
     * 更新开户日志表
     *
     * @param userId
     * @param logOrderId
     * @param status
     */
    void updateUserAccountLog(Integer userId, String logOrderId, int status);
}
