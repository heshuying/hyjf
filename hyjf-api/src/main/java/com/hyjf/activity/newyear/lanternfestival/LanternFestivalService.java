package com.hyjf.activity.newyear.lanternfestival;

import com.hyjf.base.service.BaseService;

public interface LanternFestivalService extends BaseService {

    void getPresentRiddles(PresentRiddlesResultBean resultBean);

    void getUserPresentCumulativeCoupon(Integer userId, UserPresentCumulativeResultBean resultBean);

    void getUserLanternIllumineList(Integer userId, UserLanternIllumineResultBean resultBean);

    int updateUserAnswerRecord(LanternFestivalBean lanternFestivalBean, UserAnswerResultBean resultBean);

    void check(LanternFestivalBean lanternFestivalBean, CheckResultBean resultBean);

    void getTodayUserAnswerFlag(LanternFestivalResultBean resultBean, LanternFestivalBean lanternFestivalBean);

    int insertUserAnswerRecordInit(LanternFestivalBean lanternFestivalBean);

}
