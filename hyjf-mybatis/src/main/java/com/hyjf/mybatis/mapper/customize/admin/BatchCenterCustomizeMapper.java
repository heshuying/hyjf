package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;

import com.hyjf.mybatis.model.customize.admin.BatchCenterCustomize;

/**
 * 
 * @author cwyang
 * add by 2017/4/7
 *  银行对账mybaties查询接口
 */
public interface BatchCenterCustomizeMapper {

    public Long countBatchCenter(BatchCenterCustomize batchCenterCustomize);

    public List<BatchCenterCustomize> selectBatchCenterList(BatchCenterCustomize batchCenterCustomize);

    public BatchCenterCustomize sumBatchCenter(BatchCenterCustomize batchCenterCustomize);
    
}
