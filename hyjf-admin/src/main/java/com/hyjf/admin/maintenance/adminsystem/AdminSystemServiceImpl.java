package com.hyjf.admin.maintenance.adminsystem;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.Admin;
import com.hyjf.mybatis.model.auto.AdminExample;
import com.hyjf.mybatis.model.customize.AdminSystem;

@Service
public class AdminSystemServiceImpl extends BaseServiceImpl implements AdminSystemService {

	private static final String TOP_MEN_UID = "0";

	/**
	 * 
	 * 获取菜单列表
	 * 
	 * @param adminSystemParam
	 * @return
	 * @author Administrator
	 */
	@Override
	public List<AdminSystem> selectLeftMenuTree(AdminSystem adminSystemParam) {
		List<AdminSystem> leftMenuList = this.adminSystemMapper.selectLeftMenuTree(adminSystemParam);
		return this.treeMenuList(leftMenuList, TOP_MEN_UID);
	}

	/**
	 * 菜单树形结构
	 * 
	 * @param menuTreeDBList
	 * @param topParentMenuCd
	 * @return
	 */
	private List<AdminSystem> treeMenuList(List<AdminSystem> menuTreeDBList, String topParentMenuCd) {
		List<AdminSystem> childMenu = new ArrayList<AdminSystem>();
		if (menuTreeDBList != null && menuTreeDBList.size() > 0) {
			for (AdminSystem menuTreeRecord : menuTreeDBList) {
				AdminSystem menuTree = menuTreeRecord;
				String menuCd = menuTree.getMenuUuid();
				String parentMenuCd = menuTree.getMenuPuuid();
				if (topParentMenuCd.equals(parentMenuCd)) {
					List<AdminSystem> MenuTreeList = treeMenuList(menuTreeDBList, menuCd);
					menuTree.setMenuTreeClild(MenuTreeList);
					childMenu.add(menuTree);
				}
			}
		}
		return childMenu;
	}

	/**
	 * 获取用户的角色
	 * 
	 * @param adminParam
	 * @return
	 * @author Administrator
	 */
	@Override
	public List<Integer> selectAdminGroup(AdminSystem adminSystem) {

		AdminExample example = new AdminExample();
		AdminExample.Criteria cra = example.createCriteria();
		cra.andIdEqualTo(Integer.valueOf(adminSystem.getId()));

		List<Admin> adminList = adminMapper.selectByExample(example);

		List<Integer> groupIdList = new ArrayList<Integer>();
		if (adminList != null && adminList.size() > 0) {
			for (Admin admin : adminList) {
				groupIdList.add(admin.getGroupId());
			}
		}

		return groupIdList;
	}

	/**
	 * 获取用户的基本信息用于保存于session或者缓存中
	 * 
	 * @param adminSystem
	 * @return
	 * @author Administrator
	 */

	@Override
	public AdminSystem getUserInfo(AdminSystem adminSystem) {
		return this.adminSystemMapper.getUserInfo(adminSystem);
	}

	/**
	 * 获取用户的菜单权限
	 * 
	 * @param adminSystem
	 * @return
	 * @author Administrator
	 */

	@Override
	public List<AdminSystem> getUserPermission(AdminSystem adminSystem) {
		return this.adminSystemMapper.getUserPermission(adminSystem);
	}
}
