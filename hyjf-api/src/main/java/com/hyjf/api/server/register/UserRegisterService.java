package com.hyjf.api.server.register;

import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UtmPlat;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 外部服务接口:用户注册Service
 *
 * @author liuyang
 */
public interface UserRegisterService extends BaseService {
    /**
     * 根据手机号密码注册用户
     *
     * @param mobile
     * @param instCode
     * @param request
     * @param instType
     * @param utmPlat
     * @param platform
     * @return
     */
    Integer insertUserAction(String mobile, String instCode, HttpServletRequest request, Integer instType, UtmPlat utmPlat,String platform);

    /**
     * 根据手机号检索该手机号是否已注册
     *
     * @param mobile
     * @return
     */
    Users selectUserByMobile(String mobile);

    /**
     * 根据用户ID检索用户信息
     *
     * @param userId
     * @return
     */
    Users checkUserByUserId(Integer userId);

    /**
     * 根据用户ID查询用户开户信息
     *
     * @param userId
     * @return
     */
    BankOpenAccount selectBankOpenAccountByUserId(Integer userId);

    /**
     * 根据渠道号检索渠道是否存在
     * @param utmId
     * @return
     */
    UtmPlat selectUtmPlatByUtmId(String utmId);

    List<Users> selectUserByRecommendName(String referee);

    void inserSpreadUser(Integer userId, Users users);

    /**
     * 根据手机号码发送短信
     * @param userId 用户ID
     * @param mobile 手机号码
     * @param content 短信内容
     * return 
     */
	String sendSmsMessage(Integer userId, String mobile, String content,String password);

    void updateByPrimaryKeySelective(Users users);
}
