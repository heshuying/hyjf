package com.hyjf.api.web.referee;

import com.hyjf.api.web.BaseService;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;

public interface RefereeService extends BaseService {

    /**
     * CRM平台调用修改推荐人用
     * 根据用户id检索用户是否存在
     * @author liuyang
     * @param userId
     * @return
     */
    public int countUserById(String userId);

    /**
     * CRM平台用,修改推荐人
     * @param userId
     * @param spreadsUserId
     * @param operationName
     * @return
     */
    public int updateSpreadsUsers(String userId, String spreadsUserId, String operationName, String ip);

    /**
     * 更新用户attribute属性
     * 
     * @param form
     */
    public void updateUserParam(Integer userId);
    
    /**
     * 
     * 根据用户id查询用户信息
     * @author liuyang
     * @param userId
     * @return
     */
    public UsersInfo selectUserInfoByUserId(Integer userId);
    
    /**
     * 
     * 获取用户信息
     * @author liuyang
     * @param userId
     * @return
     */
    public Users selectUserByUserId(Integer userId);
}
