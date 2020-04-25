/**
 * Description:按照用户名/手机号查询会员资料用Service实现类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 刘彬
 * @version: 1.0
 *           Created at: 2017年7月15日 下午1:50:02
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.callcenter.userinfo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.callcenter.user.UserBean;
import com.hyjf.mybatis.customize.CustomizeMapper;
import com.hyjf.mybatis.model.auto.CallcenterServiceUsers;
import com.hyjf.mybatis.model.auto.CallcenterServiceUsersExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.callcenter.CallcenterUserBaseCustomize;
import com.hyjf.mybatis.model.customize.callcenter.CallcenterUserDetailCustomize;

@Service
public class UserInfoServiceImpl extends CustomizeMapper implements UserInfoService {

	/**
	 * 查询会员基本信息
	 * @param user
	 * @return List<CallcenterUserBaseCustomize>
	 * @author 刘彬
	 */
	@Override
	public List<CallcenterUserBaseCustomize> getUserBaseList(Users user) {
		// 封装查询条件
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("userId", user.getUserId());
		// 查询用户列表
		List<CallcenterUserBaseCustomize> users = this.callcenterUserInfoCustomizeMapper.selectUserList(conditionMap);
		return users;
	}
	
	/**
	 * 查询会员详细信息
	 * @param user
	 * @return List<CellcenterUserDetailCustomize>
	 * @author 刘彬
	 */
	public List<CallcenterUserDetailCustomize> getUserDetailList(Users user) {
		// 封装查询条件
		Integer userId = user.getUserId();
		List<CallcenterUserDetailCustomize> userDetails = this.callcenterUserInfoCustomizeMapper.selectUserDetailById(userId);
		return userDetails;
	}
	
	/**
	 * 查询会员基本信息
	 * @param user
	 * @return List<CallcenterUserBaseCustomize>
	 * @author 刘彬
	 */
	@Override
	public List<CallcenterUserBaseCustomize> getNoServiceUsersList(UserBean bean) {
		// 封装查询条件
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("limitStart", bean.getLimitStart());
		conditionMap.put("limitEnd", bean.getLimitSize());
		List<CallcenterUserBaseCustomize> users = null;
		if (bean.getFlag().equals("1")) {
			// 复投用户筛选
			users = this.callcenterUserInfoCustomizeMapper.selectNoServiceFuTouUsersList(conditionMap);
		}else if(bean.getFlag().equals("2")){
			// 流失用户筛选
			users = this.callcenterUserInfoCustomizeMapper.selectNoServiceLiuShiUsersList(conditionMap);
		} else {
			// 查询用户列表
			users = this.callcenterUserInfoCustomizeMapper.selectNoServiceUsersList(conditionMap);
		}

		return users;
	}

	/**
	 * 更新呼叫中心用户分配客服的状态
	 * @param user
	 * @return List<CallcenterUserBaseCustomize>
	 * @author 刘彬
	 * @throws Exception 
	 */
	@Override
	public Integer executeRecord(List<CallcenterServiceUsers> beanList) {
		//当前时间
		Date nowDate = new Date();
		//操作记录数
		int rowCound = 0;
    	for (CallcenterServiceUsers bean : beanList) {
    		//检索条件
    		CallcenterServiceUsersExample example = new CallcenterServiceUsersExample();
            example.createCriteria().andUsernameEqualTo(bean.getUsername());
            //检索
            List<CallcenterServiceUsers> list = callcenterServiceUsersMapper.selectByExample(example);
            //判空
            if (list == null) {
            	return null;
            }
    		if (list.size() > 0) {
				//更新
    			bean.setUpddate(nowDate);//更新时间
    			rowCound += this.callcenterServiceUsersMapper.updateByExampleSelective(bean, example);
			}else{
				//登陆
				bean.setInsdate(nowDate);//登陆时间
				rowCound += callcenterServiceUsersMapper.insertSelective(bean);
			}
		}
		return rowCound;
	}
}




