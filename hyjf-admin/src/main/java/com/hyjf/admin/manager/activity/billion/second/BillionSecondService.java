package com.hyjf.admin.manager.activity.billion.second;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.ActivityBillionSecond;

public interface BillionSecondService extends BaseService {

	public Integer selectRecordCount(BillionSecondBean form);

	public List<ActivityBillionSecond> getRecordList(BillionSecondBean form, int limitStart, int limitEnd);

    
}
