package com.hyjf.admin.promotion.utmadmin;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.Admin;
import com.hyjf.mybatis.model.auto.AdminExample;
import com.hyjf.mybatis.model.auto.AdminUtmReadPermissions;
import com.hyjf.mybatis.model.auto.AdminUtmReadPermissionsExample;
import com.hyjf.mybatis.model.auto.UtmPlat;
import com.hyjf.mybatis.model.auto.UtmPlatExample;
import com.hyjf.mybatis.model.customize.admin.AdminUtmReadPermissionsCustomize;

@Service
public class AdminUtmReadPermissionsServiceImpl extends BaseServiceImpl implements AdminUtmReadPermissionsService {


	/**
	 * 获取列表数
	 * @param pcChannelReconciliationCustomize
	 * @return
	 * @author Michael
	 */
		
	@Override
	public Integer countAdminUtmReadPermissionsRecord(AdminUtmReadPermissionsCustomize adminUtmReadPermissionsCustomize) {
		return adminUtmReadPermissionsCustomizeMapper.countAdminUtmReadPermissionsRecord(adminUtmReadPermissionsCustomize);
			
	}

	/**
	 * 获取列表
	 * @param pcChannelReconciliationCustomize
	 * @return
	 * @author Michael
	 */
		
	@Override
	public List<AdminUtmReadPermissionsCustomize> selectAdminUtmReadPermissionsRecord(
			AdminUtmReadPermissionsCustomize adminUtmReadPermissionsCustomize) {
		return adminUtmReadPermissionsCustomizeMapper.selectAdminUtmReadPermissionsRecord(adminUtmReadPermissionsCustomize);
			
	}

	/**
	 * 获取单表
	 * @param id
	 * @return
	 * @author Michael
	 */
		
	@Override
	public AdminUtmReadPermissions getRecord(Integer id) {
		return adminUtmReadPermissionsMapper.selectByPrimaryKey(id);
	}

	/**
	 * 插入
	 * @param record
	 * @author Michael
	 */
		
	@Override
	public void insertRecord(AdminUtmReadPermissionsBean form) {
		AdminUtmReadPermissions record = new AdminUtmReadPermissions();
		Admin adminUser = this.getAdminByUserName(form.getAdminUserName());
		if(adminUser != null){
			record.setAdminUserId(adminUser.getId());
			record.setAdminUserName(adminUser.getUsername());
		}
		record.setKeyCode(form.getKeyCode());
		record.setCreateTime(GetDate.getNowTime10());
		record.setUtmIds(form.getUtmIds());
		adminUtmReadPermissionsMapper.insertSelective(record);
	}

	/**
	 * 更新
	 * @param record
	 * @author Michael
	 */
		
	@Override
	public void updateRecord(AdminUtmReadPermissionsBean form) {
		AdminUtmReadPermissions record = new AdminUtmReadPermissions();
		record.setId(Integer.valueOf(form.getId()));
		record.setKeyCode(form.getKeyCode());
		record.setUtmIds(form.getUtmIds());
		adminUtmReadPermissionsMapper.updateByPrimaryKeySelective(record);
	}

	/**
	 * 删除
	 * @param id
	 * @author Michael
	 */
		
	@Override
	public void deleteRecord(Integer id) {
		adminUtmReadPermissionsMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 用户是否存在
	 * @param userName
	 * @return
	 * @author Michael
	 */
		
	@Override
	public int isExistsAdminUser(String userName) {
		int usersFlag = 0;
		AdminExample example = new AdminExample();
		AdminExample.Criteria cra = example.createCriteria();
		cra.andUsernameEqualTo(userName);
		List<Admin> userList = this.adminMapper.selectByExample(example);
		// 用户名不存在
		if (userList == null || userList.size() == 0) {
			usersFlag = 1;
			return usersFlag;
		}
		Admin users = userList.get(0);
		//用户已经被禁用
		if(users.getState().equals("1")){
			usersFlag = 2;
			return usersFlag;
		}
		AdminUtmReadPermissionsExample utmexample = new AdminUtmReadPermissionsExample();
		AdminUtmReadPermissionsExample.Criteria utmcra = utmexample.createCriteria();
		utmcra.andAdminUserNameEqualTo(userName);
		List<AdminUtmReadPermissions> utmuserList = this.adminUtmReadPermissionsMapper.selectByExample(utmexample);
		//该记录已存在
		if(utmuserList != null && utmuserList.size() > 0){
			usersFlag = 3;
		}
		return usersFlag;
	}

	/**
	 * 获取渠道信息
	 * @return
	 * @author Michael
	 */
	@Override
	public List<UtmPlat> getUtmPlatList() {
		UtmPlatExample example = new UtmPlatExample();
		UtmPlatExample.Criteria cra = example.createCriteria();
		cra.andDelFlagEqualTo("0");//启用状态
		return utmPlatMapper.selectByExample(example);
	}

	/**
	 * 通过用户名获取用户信息
	 * @param username
	 * @return
	 */
	public Admin getAdminByUserName(String username){
		AdminExample example = new AdminExample();
		AdminExample.Criteria cra = example.createCriteria();
		cra.andUsernameEqualTo(username);
		List<Admin> userList = this.adminMapper.selectByExample(example);
		if (userList != null && userList.size() > 0) {
			return userList.get(0);
		}
		return null;
	}
}
