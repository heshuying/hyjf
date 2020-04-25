package com.hyjf.admin.manager.user.regist;

import java.util.List;
import java.util.Map;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.UtmPlat;
import com.hyjf.mybatis.model.customize.admin.AdminRegistListCustomize;

public interface RegistService extends BaseService {

	/**
	 * 获取权限列表
	 * 
	 * @return
	 */
	public List<AdminRegistListCustomize> getRecordList(Map<String, Object> registUser, int limitStart, int limitEnd);

	/**
	 * @param form
	 * @return
	 */

	public int countRecordTotal(Map<String, Object> registUser);

	/**
	 * @return
	 */
	public List<UtmPlat> getUtmPlagList();

}
