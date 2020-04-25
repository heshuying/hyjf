package com.hyjf.app.activity.actlist;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseService;
import com.hyjf.common.http.HttpClientUtils;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.PropertiesConstants;

public interface ActListService extends BaseService {

    JSONObject getActListData();


}
