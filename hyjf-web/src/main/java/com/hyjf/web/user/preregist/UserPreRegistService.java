package com.hyjf.web.user.preregist;

import java.util.Map;

import com.hyjf.web.BaseService;

public interface UserPreRegistService extends BaseService {

	Map<String, Object> savePreregist(UserPreRegistBean userPreRegistBean);
}
