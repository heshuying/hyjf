package com.hyjf.admin.maintenance.menu;

import java.io.Serializable;
import java.util.List;

import com.hyjf.mybatis.model.auto.AdminMenu;
import com.hyjf.mybatis.model.customize.AdminSystem;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions;
 * @author GOGTZ-T
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
public class AdminMenuBean extends AdminMenu implements Serializable {

    /**
     * serialVersionUID
     */

    private static final long serialVersionUID = 387630498860089653L;

    private String ids;

    private String selectedNode;

    private String[] permissionsUuid;

    private List<AdminSystem> recordList;

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public List<AdminSystem> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<AdminSystem> recordList) {
        this.recordList = recordList;
    }

    public String getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(String selectedNode) {
        this.selectedNode = selectedNode;
    }

    public String[] getPermissionsUuid() {
        return permissionsUuid;
    }

    public void setPermissionsUuid(String[] permissionsUuid) {
        this.permissionsUuid = permissionsUuid;
    }


}
