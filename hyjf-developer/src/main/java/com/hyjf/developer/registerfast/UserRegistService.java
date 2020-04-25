package com.hyjf.developer.registerfast;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.developer.BaseService;


public interface UserRegistService  extends BaseService {
    int insertUserActionNew(String mobile, String password, String verificationCode, String reffer, String loginIp,String platform, String utm_id, String utm_source, String utm_medium, String utm_content, HttpServletRequest request,HttpServletResponse response);

    boolean existUser(String userName);
}
