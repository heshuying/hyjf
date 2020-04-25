package com.hyjf.admin.invite.ActdecList;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.ActdecSpringList;
import com.hyjf.mybatis.model.customize.admin.GetRecommendCustomize;
import com.hyjf.mybatis.model.customize.admin.act.ActdecSpringListCustomize;

/**
 * 春节活动
 * kdl
 */
public class ActdecListBean extends ActdecSpringListCustomize implements Serializable {

	/**
     * serialVersionUID
     */
	private static final long serialVersionUID = 1790601499725213969L;

    private String ids;
    
    private List<ActdecSpringListCustomize> recordList;
    

	

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }
    
	public List<ActdecSpringListCustomize> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<ActdecSpringListCustomize> recordList) {
		this.recordList = recordList;
	}
    
}
