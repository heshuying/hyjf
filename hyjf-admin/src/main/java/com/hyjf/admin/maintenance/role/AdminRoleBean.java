package com.hyjf.admin.maintenance.role;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.AdminRole;
import com.hyjf.mybatis.model.auto.AdminRoleMenuPermissions;

/**
 * @package com.hyjf.admin.maintenance.AdminRole;
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
public class AdminRoleBean extends AdminRole implements Serializable {

    /**
     * serialVersionUID
     */

    private static final long serialVersionUID = 387630498860089653L;

    private String ids;

    private String roleId;

    private List<AdminRole> recordList;

    private List<AdminRoleMenuPermissions> permList;

    // 查询用变量
    /** 角色名称 */
    private String roleNameSrch;
    /** 角色状态 */
    private String stateSrch;
    private String stateSrchOn;
    private String stateSrchOff;

    /**
     * 翻页机能用的隐藏变量
     */
    private int paginatorPage = 1;

    /**
     * 列表画面自定义标签上的用翻页对象：paginator
     */
    private Paginator paginator;

    public int getPaginatorPage() {
        if (paginatorPage == 0) {
            paginatorPage = 1;
        }
        return paginatorPage;
    }

    public void setPaginatorPage(int paginatorPage) {
        this.paginatorPage = paginatorPage;
    }

    public Paginator getPaginator() {
        return paginator;
    }

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    public List<AdminRole> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<AdminRole> recordList) {
        this.recordList = recordList;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getRoleNameSrch() {
        return roleNameSrch;
    }

    public void setRoleNameSrch(String roleNameSrch) {
        this.roleNameSrch = roleNameSrch;
    }

    public String getStateSrch() {
        return stateSrch;
    }

    public void setStateSrch(String stateSrch) {
        this.stateSrch = stateSrch;
    }

    public String getStateSrchOn() {
        return stateSrchOn;
    }

    public void setStateSrchOn(String stateSrchOn) {
        this.stateSrchOn = stateSrchOn;
    }

    public String getStateSrchOff() {
        return stateSrchOff;
    }

    public void setStateSrchOff(String stateSrchOff) {
        this.stateSrchOff = stateSrchOff;
    }

    public List<AdminRoleMenuPermissions> getPermList() {
        return permList;
    }

    public void setPermList(List<AdminRoleMenuPermissions> permList) {
        this.permList = permList;
    }

}
