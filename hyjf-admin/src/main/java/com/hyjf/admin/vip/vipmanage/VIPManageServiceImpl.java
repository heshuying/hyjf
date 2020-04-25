/**
 * Description:用户信息管理业务处理类接口实现
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 上午11:05:56
 * Modification History:
 * Modified by : 
 */

package com.hyjf.admin.vip.vipmanage;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.admin.VIPManageListCustomize;

@Service("vipManageService")
public class VIPManageServiceImpl extends BaseServiceImpl implements VIPManageService {

	/**
	 * 获取权限列表
	 * 
	 * @return
	 */
	public List<VIPManageListCustomize> getRecordList(Map<String, Object> user, int limitStart, int limitEnd) {

		if (limitStart == 0 || limitStart > 0) {
			user.put("limitStart", limitStart);
		}
		if (limitEnd > 0) {
			user.put("limitEnd", limitEnd);
		}
		// 查询用户列表
		List<VIPManageListCustomize> users = vipManageCustomizeMapper.selectUserList(user);
		return users;
	}


	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * 
	 * @param userListCustomizeBean
	 * @return
	 * @author Administrator
	 */

	@Override
	public int countRecordTotal(Map<String, Object> user) {
		// 查询用户列表
		int count = vipManageCustomizeMapper.countRecordTotal(user);
		return count;
	}


}