package com.hyjf.admin.maintenance.menu;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.AdminExample;
import com.hyjf.mybatis.model.auto.AdminMenu;
import com.hyjf.mybatis.model.auto.AdminMenuExample;
import com.hyjf.mybatis.model.auto.AdminMenuPermissions;
import com.hyjf.mybatis.model.auto.AdminMenuPermissionsExample;
import com.hyjf.mybatis.model.auto.AdminPermissions;
import com.hyjf.mybatis.model.auto.AdminPermissionsExample;
import com.hyjf.mybatis.model.customize.AdminCustomize;
import com.hyjf.mybatis.model.customize.AdminSystem;

@Service
public class AdminMenuServiceImpl extends BaseServiceImpl implements AdminMenuService {

    private static final String TOP_MEN_UID = "0";

    /**
     * 获取菜单列表
     *
     * @return
     */
    public JSONArray getRecordList(AdminMenuBean form) {
        AdminMenuExample example = new AdminMenuExample();
        example.createCriteria().andDelFlagEqualTo(CustomConstants.FLAG_NORMAL);
        example.setOrderByClause(" menu_puuid, menu_sort ");
        List<AdminMenu> leftMenuList = this.adminMenuMapper.selectByExample(example);

        Map<String, String> map = new HashMap<String, String>();
        if (leftMenuList != null && leftMenuList.size() > 0) {
            for (AdminMenu adminMenu : leftMenuList) {
                map.put(adminMenu.getMenuUuid(), adminMenu.getMenuName());
            }
        }

        String selectedNode = form.getSelectedNode();
        return this.treeMenuList(leftMenuList, map, selectedNode, TOP_MEN_UID);
    }

    /**
     * 菜单树形结构
     *
     * @param menuTreeDBList
     * @param topParentMenuCd
     * @return
     */
    private JSONArray treeMenuList(List<AdminMenu> menuTreeDBList, Map<String, String> map, String selectedNode, String topParentMenuCd) {
        JSONArray ja = new JSONArray();
        JSONObject joAttr = new JSONObject();
        if (menuTreeDBList != null && menuTreeDBList.size() > 0) {
            JSONObject jo = null;
            for (AdminMenu menuTreeRecord : menuTreeDBList) {
                jo = new JSONObject();

                jo.put("id", menuTreeRecord.getMenuUuid());
                jo.put("text", menuTreeRecord.getMenuName());
                jo.put("icon", menuTreeRecord.getMenuIcon());
                joAttr = new JSONObject();
                joAttr.put("menuUuid", menuTreeRecord.getMenuUuid());
                joAttr.put("menuPuuid", menuTreeRecord.getMenuPuuid());
                joAttr.put("menuPName", map.get(menuTreeRecord.getMenuPuuid()));
                joAttr.put("menuCtrl", menuTreeRecord.getMenuCtrl());
                joAttr.put("menuIcon", menuTreeRecord.getMenuIcon());
                joAttr.put("menuName", menuTreeRecord.getMenuName());
                joAttr.put("menuSort", menuTreeRecord.getMenuSort());
                joAttr.put("menuUrl", menuTreeRecord.getMenuUrl());
                joAttr.put("menuHide", menuTreeRecord.getMenuHide());
                joAttr.put("menuTip", menuTreeRecord.getMenuTip());
                jo.put("li_attr", joAttr);
                if (Validator.isNotNull(selectedNode) && selectedNode.equals(menuTreeRecord.getMenuUuid())) {
                    JSONObject selectObj = new JSONObject();
                    selectObj.put("selected", true);
                    // selectObj.put("opened", true);
                    jo.put("state", selectObj);
                }

                String menuCd = menuTreeRecord.getMenuUuid();
                String parentMenuCd = menuTreeRecord.getMenuPuuid();
                if (topParentMenuCd.equals(parentMenuCd)) {
                    JSONArray array = treeMenuList(menuTreeDBList, map, selectedNode, menuCd);
                    jo.put("children", array);
                    ja.add(jo);
                }
            }
        }
        return ja;
    }

    /**
     * 获取单个菜单
     *
     * @return
     */
    public AdminCustomize getRecord(Integer id) {
        AdminCustomize adminCustomize = new AdminCustomize();
        adminCustomize.setId(id);
        List<AdminCustomize> adminList = adminCustomizeMapper.selectAdminList(adminCustomize);
        if (adminList != null && adminList.size() > 0) {
            return adminList.get(0);
        }
        return new AdminCustomize();
    }

    /**
     * 根据主键判断菜单中数据是否存在
     *
     * @return
     */
    public boolean isExistsRecord(Integer id) {
        if (Validator.isNull(id)) {
            return false;
        }
        AdminExample example = new AdminExample();
        AdminExample.Criteria cra = example.createCriteria();
        cra.andIdEqualTo(id);
        int cnt = adminMapper.countByExample(example);
        return cnt > 0;
    }

    /**
     * 菜单插入
     *
     * @param record
     */
    public void insertRecord(AdminMenuBean record) {
        String nowTime = GetDate.getServerDateTime(9, new Date());
        String userId = ShiroUtil.getLoginUserId();
        record.setMenuUuid(UUID.randomUUID().toString());
        record.setDelFlag(CustomConstants.FLAG_NORMAL);
        record.setCreatetime(nowTime);
        record.setCreateuser(userId);
        record.setUpdatetime(nowTime);
        record.setUpdateuser(userId);
        adminMenuMapper.insertSelective(record);
    }

    /**
     * 菜单更新
     *
     * @param record
     */
    public void updateRecord(AdminMenuBean record) {
        String nowTime = GetDate.getServerDateTime(9, new Date());
        String userId = ShiroUtil.getLoginUserId();
        record.setUpdatetime(nowTime);
        record.setUpdateuser(userId);
        adminMenuMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 菜单删除
     *
     * @param ids
     */
    public void deleteRecord(List<String> ids) {
        String nowTime = GetDate.getServerDateTime(9, new Date());
        String userId = ShiroUtil.getLoginUserId();
        AdminMenu record = new AdminMenu();
        record.setDelFlag(CustomConstants.FLAG_DELETE);
        record.setUpdatetime(nowTime);
        record.setUpdateuser(userId);

        AdminMenuExample example = new AdminMenuExample();
        AdminMenuExample.Criteria criteria = example.createCriteria();
        criteria.andMenuUuidIn(ids);
        adminMenuMapper.updateByExampleSelective(record, example);
    }

    /**
     * 取得菜单权限列表
     *
     * @param menuUuid
     */
    public List<AdminSystem> getMenuPermissionsList(String menuUuid) {

        AdminMenuPermissionsExample menuPermissionsExample = new AdminMenuPermissionsExample();
        menuPermissionsExample.createCriteria().andMenuIdEqualTo(menuUuid);
        List<AdminMenuPermissions> listMenuPermissions = this.adminMenuPermissionsMapper.selectByExample(menuPermissionsExample);
        Map<String, String> menuPermissionsMap = new HashMap<String, String>();
        if (listMenuPermissions != null) {
            for (AdminMenuPermissions menuPermissions : listMenuPermissions) {
                menuPermissionsMap.put(menuPermissions.getPermissionId(), menuPermissions.getPermissionId());
            }
        }

        AdminPermissionsExample permissionsExample = new AdminPermissionsExample();
        permissionsExample.createCriteria().andDelFlagEqualTo(CustomConstants.FLAG_NORMAL);
        permissionsExample.setOrderByClause(" permission_uuid ");
        List<AdminPermissions> listPermission = this.adminPermissionsMapper.selectByExample(permissionsExample);
        List<AdminSystem> list = new ArrayList<AdminSystem>();
        if (listPermission != null) {
            for (AdminPermissions permissions : listPermission) {
                AdminSystem bean = new AdminSystem();
                bean.setMenuUuid(menuUuid);
                bean.setPermissionUuid(permissions.getPermissionUuid());
                bean.setPermission(permissions.getPermission());
                bean.setPermissionName(permissions.getPermissionName());
                bean.setSelected(menuPermissionsMap.containsKey(permissions.getPermissionUuid()));
                list.add(bean);
            }
        }

        return list;
    }

    /**
     * 菜单权限更新
     *
     * @param record
     */
    public void updateMenuPermissions(AdminMenuBean record) {
        String menuUuid = record.getMenuUuid();
        String[] permissions = record.getPermissionsUuid();
        if (Validator.isNotNull(menuUuid) && permissions != null) {
            AdminMenuPermissionsExample menuPermissionsExample = new AdminMenuPermissionsExample();
            menuPermissionsExample.createCriteria().andMenuIdEqualTo(menuUuid);
            this.adminMenuPermissionsMapper.deleteByExample(menuPermissionsExample);

            AdminMenuPermissions adminMenuPermissions = null;
            for (String permissionId : permissions) {
                adminMenuPermissions = new AdminMenuPermissions();
                adminMenuPermissions.setMenuId(menuUuid);
                adminMenuPermissions.setPermissionId(permissionId);
                this.adminMenuPermissionsMapper.insertSelective(adminMenuPermissions);
            }
        }

    }
}
