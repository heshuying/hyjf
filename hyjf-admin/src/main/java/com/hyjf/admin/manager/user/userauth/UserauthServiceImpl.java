package com.hyjf.admin.manager.user.userauth;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.CreditTenderExample;
import com.hyjf.mybatis.model.customize.admin.AdminBankcardListCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminUserAuthListCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminUserAuthLogListCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminUserPayAuthCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminUserRePayAuthCustomize;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserauthServiceImpl extends BaseServiceImpl implements UserauthService {

	/**
	 * 获取权限列表
	 * 
	 * @return
	 */
	public List<AdminUserAuthListCustomize> getRecordList(Map<String, Object> authUser, int limitStart, int limitEnd) {

		if (limitStart == 0 || limitStart > 0) {
			authUser.put("limitStart", limitStart);
		}
		if (limitEnd > 0) {
			authUser.put("limitEnd", limitEnd);
		}
		// 查询用户列表
		List<AdminUserAuthListCustomize> users = adminUserAuthCustomizeMapper.selectUserAuthList(authUser);
		return users;
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * 
	 * @param authUser
	 * @return
	 * @author Administrator
	 */

	@Override
	public int countRecordTotal(Map<String, Object> authUser) {
		// 查询用户列表
		int countTotal = adminUserAuthCustomizeMapper.countRecordTotal(authUser);
		return countTotal;

	}


    @Override
    public int countRecordTotalLog(Map<String, Object> authUser) {
        // 查询用户列表
        int countTotal = adminUserAuthCustomizeMapper.countRecordTotalLog(authUser);
        return countTotal;
    }

    @Override
    public List<AdminUserAuthLogListCustomize> getRecordListLog(Map<String, Object> authUser, int limitStart, int limitEnd) {
        if (limitStart == 0 || limitStart > 0) {
			authUser.put("limitStart", limitStart);
        }
        if (limitEnd > 0) {
			authUser.put("limitEnd", limitEnd);
        }
        // 查询用户列表
        List<AdminUserAuthLogListCustomize> users = adminUserAuthCustomizeMapper.selectUserAuthLogList(authUser);
        return users;
    }

	@Override
	public int countRecordTotalPay(Map<String, Object> authUser) {
		
		int countTotal = adminUserAuthCustomizeMapper.countRecordTotalPay(authUser);
		return countTotal;
	}

	@Override
	public List<AdminUserPayAuthCustomize> getRecordListPay(Map<String, Object> authUser, int limitStart,
			int limitEnd) {
		if (limitStart == 0 || limitStart > 0) {
			authUser.put("limitStart", limitStart);
		}
		if (limitEnd > 0) {
			authUser.put("limitEnd", limitEnd);
		}
		// 查询用户列表
		List<AdminUserPayAuthCustomize> users = adminUserAuthCustomizeMapper.selectUserPayAuthList(authUser);
		return users;
	}

	@Override
	public void updatePayAuthRecord(int id, String signEndDate, int authtype) {
		adminUserAuthCustomizeMapper.updatePayAuthRecord(id,signEndDate,authtype);
	}
	

	@Override
	public int countRecordTotalRePay(Map<String, Object> authUser) {
		int countTotal = adminUserAuthCustomizeMapper.countRecordTotalRePay(authUser);
		return countTotal;
	}

	@Override
	public List<AdminUserRePayAuthCustomize> getRecordListRePay(Map<String, Object> authUser, int limitStart,
			int limitEnd) {
		if (limitStart == 0 || limitStart > 0) {
			authUser.put("limitStart", limitStart);
		}
		if (limitEnd > 0) {
			authUser.put("limitEnd", limitEnd);
		}
		// 查询用户列表
		List<AdminUserRePayAuthCustomize> users = adminUserAuthCustomizeMapper.selectUserRePayAuthList(authUser);
		return users;
	}

	@Override
	public void updateRePayAuthRecord(int id, String signEndDate, int authtype) {
		adminUserAuthCustomizeMapper.updateRePayAuthRecord(id,signEndDate,authtype);
	}

	@Override
	public int isDismissPay(int userid) {
//		return adminUserAuthCustomizeMapper.selectCanDismissPay(userid);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userid);
        params.put("roleId", 2);
        params.put("status", 0);
		int tenderCount = webUserRepayListCustomizeMapper.countUserPayProjectRecordTotal(params);
		CreditTenderExample example = new CreditTenderExample();
		example.createCriteria().andUserIdEqualTo(userid).andStatusEqualTo(0);
		int creditCount = creditTenderMapper.countByExample(example);
		return tenderCount + creditCount;
	}

	@Override
	public int isDismissRePay(int userid) {
		return adminUserAuthCustomizeMapper.selectCanDismissRePay(userid);
	}
	
	

}
