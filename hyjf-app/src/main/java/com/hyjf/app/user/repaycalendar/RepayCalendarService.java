package com.hyjf.app.user.repaycalendar;

import java.util.List;
import java.util.Map;

import com.hyjf.app.BaseService;

/**
 * @author xiasq
 * @version RepayCalendarService, v0.1 2018/1/30 18:29
 */
public interface RepayCalendarService extends BaseService {
    int countRepaymentCalendar(Map<String, Object> params);

    List<ReapyCalendarResult> searchRepaymentCalendar(Map<String, Object> params);

    int searchNearlyRepaymentTime(Map<String, Object> params);
}
