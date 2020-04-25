package com.hyjf.admin.manager.vip.clubconfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.vip.gradeconfig.GradeConfigDefine;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.log.LogUtil;

import cn.jpush.api.utils.StringUtils;

/**
 * 会员CLUB通到配置
 * 
 * @author
 *
 */
@Controller
@RequestMapping(value = ClubConfigDefine.REQUEST_MAPPING)
public class ClubConfigController extends BaseController {

	@ResponseBody
	@RequestMapping(value = ClubConfigDefine.DISPLAY_VIP)
	@RequiresPermissions(ClubConfigDefine.PERMISSIONS_UPDATE)
	public JSONObject validateBeforeAction(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(ClubConfigDefine.class.toString(), ClubConfigDefine.DISPLAY_VIP);
		JSONObject ret = new JSONObject();
		String displayvip = request.getParameter("displayvip");
        if (StringUtils.isNotEmpty(displayvip)) {
            RedisUtils.set("HYJF-CLUB-DisplayVIP",displayvip);
            ret.put("status", "0");
            ret.put("statusDesc", "操作完成,当前内存中会员显示标示为:"+RedisUtils.get("HYJF-CLUB-DisplayVIP"));
        }else{
            ret.put("status", "1");
            ret.put("statusDesc", "displayvip参数为空");
        }
		LogUtil.endLog(GradeConfigDefine.class.toString(), ClubConfigDefine.DISPLAY_VIP);
		return ret;
	}
}
