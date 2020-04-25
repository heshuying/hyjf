/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.wechat.controller.server;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.util.PropUtils;
import com.hyjf.wechat.base.BaseController;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 获取神策服务器地址URL
 *
 * @author liuyang
 * @version SensorDataController, v0.1 2018/9/17 16:46
 */
@Controller
@RequestMapping(ServerDefine.REQUEST_MAPPING)
public class ServerController extends BaseController {
    /**
     * 取的神策服务器地址,区分测试,线上环境
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(ServerDefine.GET_SENSOR_DATA_URL)
    public JSONObject getSensorDataURL(HttpServletRequest request, HttpServletResponse response) {
        JSONObject ret = new JSONObject();
        String sensorsDataUrl = PropUtils.getSystem("sensors.data.url.path");
        // 如果取的为空,默认设置测试环境地址
        if (StringUtils.isBlank(sensorsDataUrl)) {
            sensorsDataUrl = "https://sa.hyjf.com:8106/sa?project=default";
        }
        ret.put("sensorsDataUrl", sensorsDataUrl);
        ret.put("status", "000");
        return ret;
    }
}
