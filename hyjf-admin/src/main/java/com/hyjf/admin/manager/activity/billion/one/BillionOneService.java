package com.hyjf.admin.manager.activity.billion.one;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.ActivityBillionOne;

public interface BillionOneService extends BaseService {

	public Integer selectRecordCount(BillionOneBean form);

	public List<ActivityBillionOne> getRecordList(BillionOneBean form, int limitStart, int limitEnd);

    
}
