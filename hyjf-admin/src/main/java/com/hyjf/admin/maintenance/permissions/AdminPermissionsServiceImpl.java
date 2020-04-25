package com.hyjf.admin.maintenance.permissions;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.CreateUUID;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.AdminPermissions;
import com.hyjf.mybatis.model.auto.AdminPermissionsExample;

@Service
public class AdminPermissionsServiceImpl extends BaseServiceImpl implements AdminPermissionsService {

	/**
	 * 获取权限列表
	 * 
	 * @return
	 */
	public List<AdminPermissions> getRecordList(AdminPermissionsBean adminPermissions, int limitStart, int limitEnd) {
		AdminPermissionsExample example = new AdminPermissionsExample();
		AdminPermissionsExample.Criteria cra = example.createCriteria();
		cra.andDelFlagEqualTo(CustomConstants.FLAG_NORMAL);
		// 权限
        if (Validator.isNotNull(adminPermissions.getPermissionSrch())) {
            cra.andPermissionEqualTo(adminPermissions.getPermissionSrch());
        }
        // 权限名称
        if (Validator.isNotNull(adminPermissions.getPermissionNameSrch())) {
            cra.andPermissionNameLike("%" + adminPermissions.getPermissionNameSrch() + "%");
        }
		if (limitStart != -1) {
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}
		return adminPermissionsMapper.selectByExample(example);
	}

	/**
	 * 获取单个权限维护
	 * 
	 * @return
	 */
	public AdminPermissions getRecord(AdminPermissions record) {
		AdminPermissionsExample example = new AdminPermissionsExample();
		AdminPermissionsExample.Criteria cra = example.createCriteria();
		cra.andPermissionUuidEqualTo(record.getPermissionUuid());
		List<AdminPermissions> AdminPermissionsList = adminPermissionsMapper.selectByExample(example);
		if (AdminPermissionsList != null && AdminPermissionsList.size() > 0) {
			return AdminPermissionsList.get(0);
		}
		return new AdminPermissions();
	}

	/**
	 * 根据主键判断权限维护中数据是否存在
	 * 
	 * @return
	 */
	public boolean isExistsRecord(AdminPermissions record) {
		if (StringUtils.isEmpty(record.getPermissionUuid())) {
			return false;
		}
		AdminPermissionsExample example = new AdminPermissionsExample();
		AdminPermissionsExample.Criteria cra = example.createCriteria();
		cra.andPermissionUuidEqualTo(record.getPermissionUuid());
		List<AdminPermissions> AdminPermissionsList = adminPermissionsMapper.selectByExample(example);
		if (AdminPermissionsList != null && AdminPermissionsList.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 根据主键判断权限维护中权限是否存在
	 * 
	 * @return
	 */
	public boolean isExistsPermission(AdminPermissions record) {
		AdminPermissionsExample example = new AdminPermissionsExample();
		AdminPermissionsExample.Criteria cra = example.createCriteria();
		cra.andPermissionEqualTo(record.getPermission());
		if (StringUtils.isNotEmpty(record.getPermissionUuid())) {
			cra.andPermissionUuidNotEqualTo(record.getPermissionUuid());
		}
		List<AdminPermissions> AdminPermissionsList = adminPermissionsMapper.selectByExample(example);
		if (AdminPermissionsList != null && AdminPermissionsList.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 权限维护插入
	 * 
	 * @param record
	 */
	public void insertRecord(AdminPermissions record) {
		String nowTime = GetDate.getServerDateTime(9, new Date());
		record.setPermissionUuid(CreateUUID.createUUID());
		record.setDelFlag(CustomConstants.FLAG_NORMAL);
		record.setCreatetime(nowTime);
		record.setUpdatetime(nowTime);
		adminPermissionsMapper.insertSelective(record);
	}

	/**
	 * 权限维护更新
	 * 
	 * @param record
	 */
	public void updateRecord(AdminPermissions record) {
	    String nowTime = GetDate.getServerDateTime(9, new Date());
		record.setUpdatetime(nowTime);
		adminPermissionsMapper.updateByPrimaryKeySelective(record);
	}

	/**
	 * 权限维护删除
	 * 
	 * @param record
	 */
	public void deleteRecord(List<String> recordList) {
	    String nowTime = GetDate.getServerDateTime(9, new Date());
		for (String permissionUuid : recordList) {
			AdminPermissions record = new AdminPermissions();
			record.setPermissionUuid(permissionUuid);
			record.setDelFlag(CustomConstants.FLAG_DELETE);
			record.setUpdatetime(nowTime);
			adminPermissionsMapper.updateByPrimaryKeySelective(record);
		}
	}
}
