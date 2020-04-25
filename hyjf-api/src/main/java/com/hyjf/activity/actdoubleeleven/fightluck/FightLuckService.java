package com.hyjf.activity.actdoubleeleven.fightluck;

import java.util.List;

import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.auto.ActRewardList;

public interface FightLuckService extends BaseService {

    String grabCoupons(Integer userId);

    List<ActRewardList> getFightLuckWinnersList();

    List<ActRewardList> getFightLuckWinnersListByUserId(Integer userId);

    
}
