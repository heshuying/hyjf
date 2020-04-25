/**
 * Description:会员管理初始化列表查询
 * Copyright: (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 上午11:01:57
 * Modification History:
 * Modified by : 
 * */

package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.auto.CertMobileHash;
import com.hyjf.mybatis.model.auto.CertUser;
import com.hyjf.mybatis.model.auto.CertUserTransact;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.CertSendUser;
import com.hyjf.mybatis.model.customize.UserVO;
import com.hyjf.mybatis.model.customize.bifa.BifaIndexUserInfoBean;

import org.apache.ibatis.annotations.Param;

import com.hyjf.mybatis.model.customize.admin.UserInfoForLogCustomize;

public interface UsersCustomizeMapper {

	 /**
     * 根据用户id查询用户一条用户信息（添加用户更新日志用）
     * 
     * @param userId
     * @return
     */
    List<UserInfoForLogCustomize> selectUserByUserId(@Param("userId") Integer userId);
    
    /**
     * 根据推荐id，获取该id所有推荐的用户
     * @param referId
     * @return
     */
    List<UserInfoForLogCustomize> selectInviteUserList(Map<String, Object> paraMap);
    
    int selectInviteUserCount(int referId);

    List<UserVO> selectUserListByMobile(List<String> list);
    /**
     * 获取北互金索引上报信息
     * @param userId
     * @return
     */
    BifaIndexUserInfoBean selectUserCorpInfoByUserId(Integer userId);

    /**
     * 获取最近七天的银行开户用户
     * @param daySubSeven
     * @return
     */
    List<BifaIndexUserInfoBean> getBankOpenedAccountUsers(@Param("startDate") Integer startDate, @Param("endDate") Integer endDate);
    /**
     * 查询未上报的数据  投资人
     * @return
     */
    List<CertSendUser> selectCertUserNotSend();

    /**
     * 查询未上报的数据  投资人 数量
     * @return
     */
    Integer getCertUserNotSendCount();

    /**
     * 查询需要上报的用户信息
     * @param userId
     * @return
     */
    CertSendUser getCertSendUserByUserId(int userId);

    /**
     * 批量插入上报用户表
     * @param list
     */
    int insertCertUserByList(List<CertUser> list);

	int insertCertUserTransactByList(List<CertUserTransact> list);

    /**
     * 查询未进行哈希的用户
     * @return
     */
    List<Users> getNotHashUsers();

    /**
     * 批量插入手机号哈希值
     * @param mobileHashes
     */
    void insertMobileHashBatch(List<CertMobileHash> mobileHashes);
}
