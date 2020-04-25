package com.hyjf.activity.invite7;

import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.paginator.Paginator;

public class InviteSevenResultBean extends BaseResultBean {
    
    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = -9174539916567994537L;
    
    //分页信息
    Paginator paginator;

    public Paginator getPaginator() {
        return paginator;
    }

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }
    
    
}
