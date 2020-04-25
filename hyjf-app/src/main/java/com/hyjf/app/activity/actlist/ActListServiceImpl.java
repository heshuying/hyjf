package com.hyjf.app.activity.actlist;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseServiceImpl;
import com.hyjf.common.http.HttpClientUtils;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.PropertiesConstants;

/**
 * 
 * 活动列表
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年12月9日
 * @see 下午2:11:00
 */
@Service
public class ActListServiceImpl extends BaseServiceImpl implements ActListService {
    
    public static final String GET_ACTDATA_LIST = "activity/actlist.json";
    
    public static final String accessKey  = PropUtils.getSystem("aop.interface.accesskey");
    
    /**
     * 
     * 活动列表数据
     * @author hsy
     * @return
     */
    @Override
    public JSONObject getActListData(){
        Map<String, String> params = new HashMap<String, String>();
        String timestamp=GetDate.getNowTime10()+"";
        // 时间戳
        params.put("timestamp", timestamp);
        params.put("platform", "2,3");
        // 获取抽奖页面奖品列表url
        String actListUrl = PropUtils.getSystem(PropertiesConstants.HYJF_API_WEB_URL)+GET_ACTDATA_LIST;
        
        String sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + timestamp +  accessKey));
        params.put("chkValue", sign);
        String result=HttpClientUtils.post(actListUrl, params);
        JSONObject resultObj=JSONObject.parseObject(result);
        
        return resultObj;
    }
}
