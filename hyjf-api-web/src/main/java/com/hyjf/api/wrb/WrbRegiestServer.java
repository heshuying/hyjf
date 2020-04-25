package com.hyjf.api.wrb;

import com.hyjf.api.server.register.UserRegisterService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.WrbCommonDateUtil;
import com.hyjf.common.util.WrbParseParamUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.wrb.WrbResponse;
import com.hyjf.wrb.regiester.WrbRegiesterServcie;
import com.hyjf.wrb.regiester.WrbRegisterDefine;
import com.hyjf.wrb.regiester.WrbRegisterRequestBean;
import com.hyjf.wrb.regiester.WrbRegisterResultBean;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 接口用于风车理财平台注册
 * @author Walter
 * 2018-03-09 09：45
 */
@Controller
@RequestMapping(value = WrbRegisterDefine.REQUEST_MAPPING)
public class WrbRegiestServer {
	Logger _log = LoggerFactory.getLogger(WrbRegiestServer.class);

	@Autowired
	private WrbRegiesterServcie wrbRegiesterServcie;
	
	@Autowired
    private UserRegisterService userRegisterService;
	/**
     * 用户注册接口
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = WrbRegisterDefine.REGISTER_ACTION)
    public WrbRegisterResultBean userRegister(@RequestParam String param,
			 @RequestParam(value = "sign", required = false) String sign,HttpServletRequest request) {
        _log.info("风车理财注册, param is :{}, sign is :{}", param, sign);
        Map<String, String> paramMap = WrbParseParamUtil.parseParam(param);
        WrbRegisterResultBean resultBean = new WrbRegisterResultBean();
        
        if (CollectionUtils.isEmpty(paramMap)) {
        	resultBean.setRetcode(WrbResponse.FAIL_RETCODE);
        	resultBean.setRetmsg(WrbResponse.FAIL_RETMESSAGE);
			return resultBean;
		}
        WrbRegisterRequestBean wrbRegisterRequestBean = null;
		try {
			wrbRegisterRequestBean = WrbParseParamUtil.mapToBean(paramMap, WrbRegisterRequestBean.class);
		} catch (Exception e) {
			_log.error("参数解析失败, paramMap is :"+ paramMap, e);
            resultBean.setRetcode("99");
            resultBean.setRetmsg("注册平台异常，用户注册失败！");
            return resultBean;
		} finally {
            if (wrbRegisterRequestBean == null) {
                resultBean.setRetcode("99");
                resultBean.setRetmsg("注册平台异常，用户注册失败！");
                return resultBean;
            }
        }
        
        // 手机号
        String mobile = wrbRegisterRequestBean.getMobile();
        // 注册平台
        String from = wrbRegisterRequestBean.getFrom();
        if(!(StringUtils.isNotBlank(from) && "wrb".equals(from))){
        	resultBean.setRetcode("3");
        	resultBean.setRetmsg("注册平台异常，用户注册失败！");
        	return resultBean;
        }
        // 机构编号
        String instCode = WrbCommonDateUtil.FCLC_INSTCODE+"";
        // 注册平台
        String platform = WrbCommonDateUtil.FCLC_PLATFORM;
        // 注册渠道
        String utmId = WrbCommonDateUtil.FCLC_UTMID;
        try {
            // 验证请求参数
            // 手机号
            if (Validator.isNull(mobile)) {
            	resultBean.setRetcode("3");
                resultBean.setRetmsg("手机号不能为空");
                return resultBean;
            }
            // 手机号合法性校验
            if (!Validator.isMobile(mobile)) {
            	resultBean.setRetcode("3");
                resultBean.setRetmsg("请输入您的真实手机号码");
                return resultBean;
            }
            // 根据机构编号检索机构信息
            HjhInstConfig instConfig = this.userRegisterService.selectInstConfigByInstCode(instCode);
            // 机构编号
            if (instConfig == null) {
                _log.info("获取机构信息为空,机构编号:[" + instCode + "].");
                resultBean.setRetcode("3");
                resultBean.setRetmsg("机构编号错误");
                return resultBean;
            }
            // 根据渠道号检索推广渠道是否存在
            UtmPlat utmPlat = this.userRegisterService.selectUtmPlatByUtmId(utmId);
            if (utmPlat == null) {
                _log.info("根据渠道号获取推广渠道为空,渠道号:[" + utmId + "].");
                resultBean.setRetcode("3");
                resultBean.setRetmsg("推广渠道号非法");
                return resultBean;
            }
            // 根据手机号检索用户是否存在
            Users user = this.userRegisterService.selectUserByMobile(mobile);
            // 如果用户已经存在,表示该手机号已经注册
            if (user != null) {
                BindUsers bindUsers = wrbRegiesterServcie.selectByUserId(user.getUserId(), instCode);
                if (bindUsers == null) {
                    _log.info("用户手机号已在平台注册:用户名:[" + user.getUsername() + ",用户手机号:[" + mobile + "]");
                    // 合作平台的老用户
                    resultBean.setRetcode("1");
                    resultBean.setRetmsg("手机号已在平台注册");
                    resultBean.setPf_user_id(user.getUserId()+"");
                    return resultBean;
                } else {
                    if (bindUsers.getBindUniqueId().equals(wrbRegisterRequestBean.getWrb_user_id())) {
                        resultBean.setRetcode("0");
                        resultBean.setRetmsg("注册成功");
                        // 用户Id
                        resultBean.setPf_user_id(String.valueOf(bindUsers.getUserId()));
                        resultBean.setPf_user_name(user.getUsername());
                        return resultBean;
                    } else {
                        resultBean.setRetcode("3");
                        resultBean.setRetmsg("注册失败, 风车理财id已被绑定");
                        // 用户Id
                        resultBean.setPf_user_id(String.valueOf(bindUsers.getUserId()));
                        resultBean.setPf_user_name(user.getUsername());
                        return resultBean;
                    }
                }
/*
                resultBean.setRetcode("1");
                resultBean.setRetmsg("手机号已在平台注册");
                resultBean.setPf_user_id(user.getUserId()+"");
                return resultBean;*/
            } else {
                // 手机号未注册
                _log.info("手机号在平台未注册,手机号:[" + mobile + "]");
                Integer userId = this.userRegisterService.insertUserAction(mobile, instCode, request, instConfig.getInstType(), utmPlat,platform);
                if (userId == null || userId == 0) {
                    _log.info("用户注册失败,手机号:[" + mobile + "]");
                    resultBean.setRetcode("3");
                    resultBean.setRetmsg("注册失败");
                    return resultBean;
                } else {
                    Users users = this.userRegisterService.checkUserByUserId(userId);
                    if (users == null) {
                        _log.info("根据用户ID获取用户信息表失败,用户ID:[" + userId + "]");
                        resultBean.setRetcode("3");
                        resultBean.setRetmsg("注册失败");
                        return resultBean;
                    }
                    
                    //处理用户基本信息
                    UsersInfo userInfo = wrbRegiesterServcie.getUserInfoByUserId(users.getUserId());
                    userInfo.setIdcard(wrbRegisterRequestBean.getId_no());
                    userInfo.setTruename(wrbRegisterRequestBean.getTrue_name());
                    wrbRegiesterServcie.updateUserInfo(userInfo);

                    //插入用户绑定表
                    BindUsers bindUser = new BindUsers();
                    int nowTime = GetDate.getNowTime10();
                    bindUser.setUserId(userId);
                    bindUser.setBindUniqueId(wrbRegisterRequestBean.getWrb_user_id());
                    bindUser.setBindPlatformId(Integer.valueOf(instCode));
                    bindUser.setCreateTime(nowTime);
                    wrbRegiesterServcie.insertSelective(bindUser);
                    _log.info("汇盈金福用户："+userId+"跟风车理财用户："+userId+"已经绑定！");

                    String userName = users.getUsername();
                    // 用户注册成功
                    _log.info("用户注册成功,手机号:[" + mobile + "],用户ID:[" + userId + ",用户名:[" + userName + "].");
                    resultBean.setRetcode("0");
                    resultBean.setRetmsg("注册成功");
                    resultBean.setPf_user_id(String.valueOf(users.getUserId()));
                    resultBean.setPf_user_name(userName);
                    return resultBean;
                }
            }
        } catch (Exception e) {
            _log.info("用户注册失败~,手机号:[" + mobile + "],失败原因:[" + e.getMessage() + "]");
            resultBean.setRetcode("3");
            resultBean.setRetmsg("注册失败");
            return resultBean;
        }
    }
}
