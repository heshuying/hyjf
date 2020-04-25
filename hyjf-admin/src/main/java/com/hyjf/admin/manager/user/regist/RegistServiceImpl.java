package com.hyjf.admin.manager.user.regist;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.UtmPlat;
import com.hyjf.mybatis.model.auto.UtmPlatExample;
import com.hyjf.mybatis.model.customize.admin.AdminRegistListCustomize;

@Service
public class RegistServiceImpl extends BaseServiceImpl implements RegistService {

	/**
	 * 获取权限列表
	 * 
	 * @return
	 */
	public List<AdminRegistListCustomize> getRecordList(Map<String, Object> registUser, int limitStart, int limitEnd) {
		// 封装查询条件
		if (limitStart == 0 || limitStart > 0) {
			registUser.put("limitStart", limitStart);
		}
		if (limitEnd > 0) {
			registUser.put("limitEnd", limitEnd);
		}
		// 查询用户列表
		List<AdminRegistListCustomize> users = adminRegistCustomizeMapper.selectRegistList(registUser);
		return users;
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * 
	 * @param form
	 * @return
	 * @author Administrator
	 */

	@Override
	public int countRecordTotal(Map<String, Object> registUser) {
		return adminRegistCustomizeMapper.countRecordTotal(registUser);

	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * 
	 * @return
	 * @author Administrator
	 */

	@Override
	public List<UtmPlat> getUtmPlagList() {
		List<UtmPlat> utmPlats = utmPlatMapper.selectByExample(new UtmPlatExample());
		return utmPlats;

	}

}
