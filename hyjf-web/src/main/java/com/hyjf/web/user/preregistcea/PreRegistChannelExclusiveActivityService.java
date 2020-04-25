package com.hyjf.web.user.preregistcea;

import java.util.List;
import java.util.Map;

import com.hyjf.web.BaseService;

public interface PreRegistChannelExclusiveActivityService extends BaseService {

    List<Map<String, Object>> getRecordList(Map<String, Object> map);
}
