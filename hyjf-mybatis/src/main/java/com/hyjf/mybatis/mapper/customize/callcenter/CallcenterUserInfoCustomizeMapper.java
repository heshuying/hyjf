/**
 * Description:按照用户名/手机号查询会员资料Mapper类
 * Copyright: (HYJF Corporation) 2017
 * Company: HYJF Corporation
 * @author: 刘彬
 * @version: 1.0
 * Created at: 2017年7月19日 上午11:01:57
 * Modification History:
 * Modified by : 
 * */

package com.hyjf.mybatis.mapper.customize.callcenter;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hyjf.mybatis.model.customize.callcenter.CallcenterUserBaseCustomize;
import com.hyjf.mybatis.model.customize.callcenter.CallcenterUserHuifuCustomize;
import com.hyjf.mybatis.model.customize.callcenter.CallcenterUserDetailCustomize;

public interface CallcenterUserInfoCustomizeMapper {
	/**
	 * 根据推荐人id查询相应的用户
	 * 同步另外文件AdminUsersCustomizeMapper
	 * @param user
	 * @return
	 */
	List<CallcenterUserBaseCustomize> selectUserList(Map<String, Object> user);

	/**
	 * 根据用户id查询用户详情
	 * 同步另外文件AdminUsersCustomizeMapper
	 * @param userId
	 * @return
	 */
	List<CallcenterUserDetailCustomize> selectUserDetailById(@Param("userId") Integer userId);

	/**
	 * 根据用户的查询条件查询用户汇付开户列表
	 * 同步另外文件AdminAccountCustomizeMapper
	 * @param accountUser
	 * @return
	 */
	List<CallcenterUserHuifuCustomize> selectAccountList(Map<String, Object> accountUser);
	
	/**
	 * 查询呼叫中心未分配客服的用户
	 * @param user
	 * @return
	 */
	List<CallcenterUserBaseCustomize> selectNoServiceUsersList(Map<String, Object> user);
	
	/**
	 * 查询呼叫中心未分配客服的用户（复投用户筛选）
	 * @param user
	 * @return
	 */
	List<CallcenterUserBaseCustomize> selectNoServiceFuTouUsersList(Map<String, Object> user);
	
	/**
	 * 查询呼叫中心未分配客服的用户（流失用户筛选）
	 * @param user
	 * @return
	 */
	List<CallcenterUserBaseCustomize> selectNoServiceLiuShiUsersList(Map<String, Object> user);
}
