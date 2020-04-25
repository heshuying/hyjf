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

import org.apache.ibatis.annotations.Param;

import com.hyjf.mybatis.model.customize.admin.AdminUserBankOpenAccountCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminUserDetailCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminUserListCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminUserRecommendCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminUserUpdateCustomize;

public interface AdminUsersCustomizeMapper {
	/**
	 * 根据推荐人id查询相应的用户
	 * 
	 * @param user
	 * @return
	 */
	List<AdminUserListCustomize> selectUserList(Map<String, Object> user);

	/**
	 * 当分账类型选择按出借人分账时，需选择出借人分公司，从会员中心-会员管理-分公司字段读取所有分公司
	 * @return
	 */
	List<Map<String,String>> selectRegionNameList();

	/**
	 * 根据用户id查询用户详情
	 * 
	 * @param userId
	 * @return
	 */
	List<AdminUserDetailCustomize> selectUserDetailById(@Param("userId") Integer userId);

	/**
	 * 通过用户ID 关联用户所在的渠道
	 * @param userId
	 * @return
	 */
	AdminUserDetailCustomize selectUserUtmInfo(@Param("userId") Integer userId);
	/**
	 * 根据用户id查询用户更新信息
	 * 
	 * @param userId
	 * @return
	 */
	List<AdminUserUpdateCustomize> selectUserUpdateById(@Param("userId") Integer userId);

	/**
	 * @param user
	 * @return
	 */

	int countRecordTotal(Map<String, Object> user);

	/**
	 * @param userId
	 * @return
	 */

	List<AdminUserRecommendCustomize> searchUserRecommend(int userId);

	/**
	 * 获取指定时间内修改的推荐人信息
	 * 
	 * @param userRecommendCustomize
	 * @return
	 */
	List<AdminUserRecommendCustomize> querySpreadsUsersLog(AdminUserRecommendCustomize userRecommendCustomize);
	/**
	 * 
	 * 查询用户银行开户信息
	 * @author pcc
	 * @param userId
	 * @return
	 */
    List<AdminUserBankOpenAccountCustomize> selectBankOpenAccountById(int userId);

}
