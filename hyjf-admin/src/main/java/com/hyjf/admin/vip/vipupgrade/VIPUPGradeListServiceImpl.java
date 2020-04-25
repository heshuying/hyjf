package com.hyjf.admin.vip.vipupgrade;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.admin.VIPUpgradeListCustomize;

@Service
public class VIPUPGradeListServiceImpl extends BaseServiceImpl implements VIPUPGradeListService {

	/**
	 * 获取升级记录列表
	 * 
	 * @return
	 */
	public List<VIPUpgradeListCustomize> getRecordList(Map<String, Object> param, int limitStart, int limitEnd) {

		if (limitStart == 0 || limitStart > 0) {
			param.put("limitStart", limitStart);
		}
		if (limitEnd > 0) {
			param.put("limitEnd", limitEnd);
		}
		// 查询用户列表
		List<VIPUpgradeListCustomize> users = vipUPGradeListCustomizeMapper.selectRecordList(param);
		return users;
	}


	/**
	 * 获取总的记录数
	 * 
	 * @param userListCustomizeBean
	 * @return
	 * @author Administrator
	 */

	@Override
	public int countRecordTotal(Map<String, Object> param) {
		// 查询用户列表
		int count = vipUPGradeListCustomizeMapper.countRecordTotal(param);
		return count;
	}


}
