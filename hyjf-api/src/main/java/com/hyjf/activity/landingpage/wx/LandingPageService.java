package com.hyjf.activity.landingpage.wx;

import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.auto.WhereaboutsPageConfig;

public interface LandingPageService extends BaseService {

    WhereaboutsPageConfig getLandingConfig(Integer id);


}
