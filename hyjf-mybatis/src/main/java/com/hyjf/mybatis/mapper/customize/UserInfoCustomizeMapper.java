package com.hyjf.mybatis.mapper.customize;

import com.hyjf.mybatis.model.auto.UsersPortrait;
import com.hyjf.mybatis.model.customize.OADepartmentCustomize;
import com.hyjf.mybatis.model.customize.UserInfoCustomize;
import com.hyjf.mybatis.model.customize.UserPortraitCustomize;
import com.hyjf.mybatis.model.customize.admin.UserPortraitScoreCustomize;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface UserInfoCustomizeMapper {

    /**
     * 查询非员工用户信息
     *
     * @param username
     * @return
     */
    public UserInfoCustomize queryUserInfoByName(@Param("userName") String username);

    /**
     * 查询员工信息
     *
     * @param employeeName
     * @return
     */
    public UserInfoCustomize queryUserInfoByEmployeeName(@Param("employeeName") String employeeName);

    /**
     * 查询员工部门信息
     *
     * @param userId
     * @return
     */
    public List<UserInfoCustomize> queryDepartmentInfoByUserId(@Param("userId") Integer userId);

    /**
     * 查询所有部门信息
     *
     * @param oaDepartmentCustomize
     * @return
     */
    public List<OADepartmentCustomize> queryDepartmentInfo(OADepartmentCustomize oaDepartmentCustomize);
    
    /**
     * 
     * 根据用户id查询用户情报
     * @author liuyang
     * @param user_id
     * @return
     */
    public UserInfoCustomize queryUserInfoByUserId(@Param("user_id") Integer user_id);
    
    /**
     * 
     * 根据用户id查询用户情报
     * @author liuyang
     * @param user_id
     * @return
     */
    public UserInfoCustomize selectUserInfoByUserId(@Param("user_id") Integer user_id);
    
    /**
     * 根据用户名查询用户部门信息
     * @param username
     * @return
     */
    public UserInfoCustomize queryUserDepartmentInfoByUserName(@Param("userName") String username);

    /**
     * 根据用户画像查询用户记录数
     * @param userPortrait
     * @return
     */
    int countRecordTotal(Map<String, Object> userPortrait);

    /**
     * 根据用户画像查询用户记录
     * @param userPortrait
     * @return
     */
    List<UserPortraitCustomize> selectPortraitList(Map<String, Object> userPortrait);

    /**
     * 根据用户画像评分查询用户记录
     * @param userPortrait
     * @return
     */
    List<UsersPortrait> selectUserPortraitList(Map<String, Object> userPortrait);

    /**
     * 查询用户画像评分信息
     * @param userPortraitScore
     * @return
     */
    List<UserPortraitScoreCustomize> selectUserPortraitListScore(Map<String, Object> userPortraitScore);

    /**
     * 根据性别和年龄查询用户年化出借金额
     * @param map
     * @return
     */
    List<BigDecimal> selectInvest(Map<String, Object> map);
}
