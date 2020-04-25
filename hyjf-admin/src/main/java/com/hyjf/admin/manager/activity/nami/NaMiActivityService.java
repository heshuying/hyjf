package com.hyjf.admin.manager.activity.nami;

import java.util.List;
import java.util.Map;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.admin.NaMiActivityUserListCustomize;

public interface NaMiActivityService extends BaseService {

    Integer selectRecordCount(Map<String, Object> paraMap);

    List<NaMiActivityUserListCustomize> selectRecordList(Map<String, Object> paraMap);

    String getFoldRatio(String userId);

    
}
