package com.hyjf.admin.manager.activity.actnov2017.fightluck;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.ActRewardList;

public interface ActFightLuckService extends BaseService {


	public List<ActRewardList> getRecordList();

}
