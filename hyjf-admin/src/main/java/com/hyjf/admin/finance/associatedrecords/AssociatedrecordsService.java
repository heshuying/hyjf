package com.hyjf.admin.finance.associatedrecords;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.DirectionalTransferAssociatedRecords;

public interface AssociatedrecordsService extends BaseService {
    /**
     * 检索关联记录件数
     * 
     * @param form
     * @return
     */
    public int countRecordTotal(AssociatedrecordsBean form);

    /**
     * 检索关联记录列表
     * 
     * @param form
     * @return
     */
    public List<DirectionalTransferAssociatedRecords> getRecordList(AssociatedrecordsBean form, int limitStart,
        int limitEnd);
}
