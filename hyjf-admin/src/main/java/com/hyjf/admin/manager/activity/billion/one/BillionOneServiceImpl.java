package com.hyjf.admin.manager.activity.billion.one;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.ActivityBillionOne;
import com.hyjf.mybatis.model.auto.ActivityBillionOneExample;

@Service
public class BillionOneServiceImpl extends BaseServiceImpl implements BillionOneService {

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param form
	 * @return
	 * @author Michael
	 */
		
	@Override
	public Integer selectRecordCount(BillionOneBean form) {
		return activityBillionOneMapper.countByExample(new ActivityBillionOneExample());
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param form
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 * @author Michael
	 */
		
	@Override
	public List<ActivityBillionOne> getRecordList(BillionOneBean form, int limitStart, int limitEnd) {
		return activityBillionOneMapper.selectByExample(new ActivityBillionOneExample());
	}

}
