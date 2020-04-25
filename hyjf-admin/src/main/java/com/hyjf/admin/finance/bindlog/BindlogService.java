package com.hyjf.admin.finance.bindlog;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.DirectionalTransferAssociatedLog;

public interface BindlogService extends BaseService {
    /**
     * 检索关联记录log表件数
     * 
     * @param form
     * @return
     */
    public int countRecordTotal(BindlogBean form);

    /**
     * 检索关联记录log列表
     * 
     * @param form
     * @return
     */
    public List<DirectionalTransferAssociatedLog> getRecordList(BindlogBean form, int limitStart, int limitEnd);
}
