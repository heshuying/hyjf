/**
 * Description:按照用户名/手机号查询会员资料用Service接口
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 刘彬
 * @version: 1.0
 *           Created at: 2017年7月15日 下午1:50:02
 *           Modification History:
 *           Modified by :
 */
package com.hyjf.callcenter.userinfo;

import java.util.List;

import com.hyjf.callcenter.user.UserBean;
import com.hyjf.mybatis.model.auto.CallcenterServiceUsers;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.callcenter.CallcenterUserBaseCustomize;
import com.hyjf.mybatis.model.customize.callcenter.CallcenterUserDetailCustomize;

public interface UserInfoService {

	/**
	 * 查询会员基本信息
	 * @param user
	 * @return List<CallcenterUserBaseCustomize>
	 * @author 刘彬
	 */
	public List<CallcenterUserBaseCustomize> getUserBaseList(Users user);
	/**
	 * 查询会员详细信息
	 * @param user
	 * @return List<CellcenterUserDetailCustomize>
	 * @author 刘彬
	 */
	public List<CallcenterUserDetailCustomize> getUserDetailList(Users user);
	/**
	 * 查询呼叫中心未分配客服的用户
	 * @param user
	 * @return List<CallcenterUserBaseCustomize>
	 * @author 刘彬
	 */
	public List<CallcenterUserBaseCustomize> getNoServiceUsersList(UserBean bean);
	
	/**
	 * 更新呼叫中心用户分配客服的状态
	 * @param List<CallcenterServiceUsers>
	 * @return ResultListBean
	 * @author 刘彬
	 */
	public Integer executeRecord(List<CallcenterServiceUsers> userList); 
}
