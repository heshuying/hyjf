package com.hyjf.batch.activity.billion.third;

import com.hyjf.batch.BaseService;

public interface BillionThirdService extends BaseService {

  

    public void startHourActivity(String hour);

    public void activityReset();

    public void startHourlyActivity();
	
}
