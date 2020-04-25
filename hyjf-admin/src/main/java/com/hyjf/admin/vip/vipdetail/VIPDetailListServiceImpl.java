package com.hyjf.admin.vip.vipdetail;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.admin.VIPDetailListCustomize;

@Service
public class VIPDetailListServiceImpl extends BaseServiceImpl implements VIPDetailListService {

	/**
	 * 获取权限列表
	 * 
	 * @return
	 */
	public List<VIPDetailListCustomize> getRecordList(Map<String, Object> param, int limitStart, int limitEnd) {

		if (limitStart == 0 || limitStart > 0) {
			param.put("limitStart", limitStart);
		}
		if (limitEnd > 0) {
			param.put("limitEnd", limitEnd);
		}
		// 查询用户列表
		List<VIPDetailListCustomize> users = vipDetailListCustomizeMapper.selectRecordList(param);
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
	public int countRecordTotal(Map<String, Object> param) {
		// 查询用户列表
		int count = vipDetailListCustomizeMapper.countRecordTotal(param);
		return count;
	}


}
