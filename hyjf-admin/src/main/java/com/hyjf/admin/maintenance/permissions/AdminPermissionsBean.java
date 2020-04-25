package com.hyjf.admin.maintenance.permissions;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.AdminPermissions;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions;
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
public class AdminPermissionsBean extends AdminPermissions implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 387630498860089653L;

    private List<AdminPermissions> recordList;

    private String permissionSrch;

    private String permissionNameSrch;

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

    public List<AdminPermissions> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<AdminPermissions> recordList) {
        this.recordList = recordList;
    }

    public String getPermissionSrch() {
        return permissionSrch;
    }

    public void setPermissionSrch(String permissionSrch) {
        this.permissionSrch = permissionSrch;
    }

    public String getPermissionNameSrch() {
        return permissionNameSrch;
    }

    public void setPermissionNameSrch(String permissionNameSrch) {
        this.permissionNameSrch = permissionNameSrch;
    }

}
