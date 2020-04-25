package com.hyjf.app.sharenews;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;

@Controller
@RequestMapping(value = ShareNewsDefine.REQUEST_MAPPING)
public class ShareNewsController extends BaseController {
    /** 类名 */
    public static final String THIS_CLASS = ShareNewsController.class.getName();

    private static String HOST_URL = PropUtils.getSystem("hyjf.web.host");

    @Autowired
    private ShareNewsService shareNewsService;

    /**
     * 获取分享信息
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = ShareNewsDefine.GETNEWS_ACTION, method = RequestMethod.POST)
    public JSONObject getMyAsset(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(THIS_CLASS, ShareNewsDefine.GETNEWS_ACTION);
        JSONObject ret = new JSONObject();
        ret.put("request", ShareNewsDefine.RETURN_REQUEST);

        // 版本号
        String version = request.getParameter("version");
        // 网络状态
        String netStatus = request.getParameter("netStatus");
        // 平台
        String platform = request.getParameter("platform");
        // 唯一标识
        String sign = request.getParameter("sign");

        // 检查参数正确性
        if (Validator.isNull(version) || Validator.isNull(netStatus) || Validator.isNull(platform)
                || Validator.isNull(sign)) {
            ret.put("status", "1");
            ret.put("statusDesc", "请求参数非法");
            return ret;
        }

        // 取得加密用的Key
        String key = SecretUtil.getKey(sign);
        if (Validator.isNull(key)) {
            ret.put("status", "1");
            ret.put("statusDesc", "请求参数非法");
            return ret;
        }

        ShareNewsBean shareNewsBean = this.shareNewsService.queryShareNews();
        if (shareNewsBean.getTitle() != null) {
            if (null != SecretUtil.getToken(sign)) {// 如果用户是登陆状态 拼接用户编号
                Integer userId = SecretUtil.getUserId(sign);
                try {
                    ret.put("linkUrl", PropUtils.getSystem("hyjf.app.qrcode.url") + "refferUserId=" + userId);
                } catch (Exception e) {
                    ret.put("status", "708");
                    ret.put("statusDesc", "获取分享信息失败!");
                    return ret;
                }
                
            } else {
                ret.put("status", "708");
                ret.put("statusDesc", "获取分享信息失败!");
                return ret;
            }
            ret.put("status", "0");
            ret.put("statusDesc", "成功");
            ret.put("title", shareNewsBean.getTitle());
            ret.put("content", shareNewsBean.getContent());
            ret.put("img", "https://www.hyjf.com" + shareNewsBean.getImg());// 提供绝对路径
        } else {
            ret.put("status", "2");
            ret.put("statusDesc", "获取分享信息失败！");
        }
        LogUtil.endLog(THIS_CLASS, ShareNewsDefine.GETNEWS_ACTION);
        return ret;

    }

}
