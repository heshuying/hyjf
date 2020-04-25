package com.hyjf.admin.www.login;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.loginerror.LoginErrorLockUserService;
import com.hyjf.admin.loginerror.config.LoginErrorConfigManager;
import com.hyjf.admin.manager.user.manageruser.ManageUsersDefine;
import com.hyjf.admin.shiro.AuthorizingHYJFRealm;
import com.hyjf.common.cache.RedisConstants;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.calculate.DateUtils;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.SessionUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.mapper.auto.LoginErrorLockUserMapper;
import com.hyjf.mybatis.model.auto.Admin;
import com.hyjf.mybatis.model.auto.LoginErrorLockUser;
import com.hyjf.mybatis.model.customize.AdminSystem;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(LoginDefine.CONTROLLOR_REQUEST_MAPPING)
public class LoginController extends BaseController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private AuthorizingHYJFRealm authorizingHYJFRealm;

    @Autowired
    private LoginErrorLockUserService loginErrorLockUserService;

    @RequestMapping(LoginDefine.INIT)
    public ModelAndView init(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView(LoginDefine.INIT_PATH);
        // Subject currentUser = SecurityUtils.getSubject();
        // if (currentUser.isAuthenticated()) {
        // SessionUtils.setSession(CustomConstants.FUNCTION_ID, "");
        // modelAndView = new ModelAndView(new
        // RedirectView(request.getContextPath() + "/manager/desktop/init"));
        // }
        return modelAndView;
    }

    @RequestMapping(LoginDefine.LOGIN)
    public ModelAndView login(HttpServletRequest request, LoginBean loginBean) {
        ModelAndView modelAndView = new ModelAndView("www/login/login");
        ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "username", loginBean.getUsername(), 20, true);
        ValidatorFieldCheckUtil.validateRequired(modelAndView, "password", loginBean.getPassword());

        if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
            return modelAndView;
        }
        /**逻辑漏洞修改 add by zhangqingqing */

        //2.判断是否错误超过错误次数
        //获取Redis配置的额登录最大错误次数
        Integer maxLoginErrorNum = LoginErrorConfigManager.getInstance().getAdminConfig().getMaxLoginErrorNum();
        //1.获取该用户密码错误次数
        String passwordErrorNum = RedisUtils.get(RedisConstants.PASSWORD_ERR_COUNT_ADMIN + loginBean.getUsername());
        //判断密码错误次数是否超限
        if (!StringUtils.isEmpty(passwordErrorNum) && Integer.parseInt(passwordErrorNum) >= maxLoginErrorNum) {
            long retTime = RedisUtils.ttl(RedisConstants.PASSWORD_ERR_COUNT_ADMIN + loginBean.getUsername());
            modelAndView.addObject("retTime", DateUtils.SToHMSStr(retTime));
            //密码错误次数已达上限
            modelAndView.setViewName("www/login/login");
            return modelAndView;
        }
        /** end */
        Subject currentUser = SecurityUtils.getSubject();
        String password = MD5.toMD5Code(loginBean.getPassword());
        UsernamePasswordToken token = new UsernamePasswordToken(loginBean.getUsername(), password);

        try {
            // if (StringUtils.equals("1", loginBean.getRemember())) {
            // token.setRememberMe(true);
            // }
            currentUser.login(token);
            SessionUtils.setSession(CustomConstants.FUNCTION_ID, "");
        } catch (AuthenticationException e) {
            if ("user_disabled".equals(e.getMessage())) {
                modelAndView.setViewName("/error/user_disabled");
            } else {
                modelAndView.setViewName("www/login/login");
            }
            return modelAndView;
        } catch (UnknownSessionException e) {
            return modelAndView;
        }
        // 断言用户已经登录
        Assert.isTrue(currentUser.isAuthenticated());
        //登录成功就将登陆密码错误次数的key删除
        RedisUtils.del(RedisConstants.PASSWORD_ERR_COUNT_ADMIN + loginBean.getUsername());

        authorizingHYJFRealm.clearCachedAuthorizationInfo(loginBean.getUsername());// 清除权限缓存
        authorizingHYJFRealm.isPermitted(SecurityUtils.getSubject().getPrincipals(),
                String.valueOf(System.currentTimeMillis()));

        modelAndView = new ModelAndView(new RedirectView(request.getContextPath() + "/manager/desktop/init"));
        return modelAndView;
    }

    @RequestMapping(LoginDefine.LOGIN_OUT)
    public ModelAndView loginOut() {
        ModelAndView modelAndView = new ModelAndView(LoginDefine.INIT_PATH);
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
        return modelAndView;
    }

    @RequestMapping(LoginDefine.TO_UPDATEPASSWORD)
    public ModelAndView toUpdatePassword() {
        ModelAndView modelAndView = new ModelAndView(LoginDefine.UPDATEOPASSWORD_PATH);
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(LoginDefine.UPDATEPASSWORD)
    public Map<String, Object> updatePasswordAction(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        // 更新密码
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        String newPassword1 = request.getParameter("newPassword1");//用户输入的新密码

        String oldPassword = request.getParameter("oldPassword");//用户输入的旧密码

        if (Validator.isNull(newPassword1) && Validator.isNull(oldPassword)) {
            modelAndView.getModel().put(ManageUsersDefine.JSON_VALID_INFO_KEY, "新密码和旧密码都不能为空！");
        } else {
            if (Validator.isNumber(newPassword1) || newPassword1.length() < 8 || newPassword1.length() > 16) {
                modelAndView.getModel().put(ManageUsersDefine.JSON_VALID_INFO_KEY, "密码8-16位，必须有数字字母组成，区分大小写");
            } else {
                if (session.getAttribute(CustomConstants.LOGIN_USER_INFO) != null) {
                    AdminSystem users = (AdminSystem) session.getAttribute(CustomConstants.LOGIN_USER_INFO);

                    // add by libin 漏洞修复 5.1.5 20181012 start
                    //原代码 获取 users后，也没有做空判断直接给前端返回 “不存在该用户”,逻辑不正确，故先注掉
                    /*modelAndView.getModel().put(ManageUsersDefine.JSON_VALID_INFO_KEY, "不存在该用户");*/
                    // 在保存时，再次 使用用户名 以及 旧密码查看存在性
                    AdminSystem u = loginService.getUsersByUserNameAndPassword(users.getUsername(), oldPassword);
                    if (u == null) {
                        modelAndView.getModel().put(ManageUsersDefine.JSON_VALID_INFO_KEY, "旧密码不正确");
                        return modelAndView.getModel();
                    }
                    // add by libin 漏洞修复 5.1.5 20181012 end

                    try {
                        loginService.updatePassword(users.getUsername(), newPassword1);
                        modelAndView.getModel().put(ManageUsersDefine.JSON_VALID_INFO_KEY, "修改成功");
                    } catch (Exception e) {
                        modelAndView.getModel().put(ManageUsersDefine.JSON_VALID_INFO_KEY, e.getMessage());
                    }
                } else {
                    modelAndView.getModel().put(ManageUsersDefine.JSON_VALID_INFO_KEY, "不存在登录用户,请重新登录");
                }
            }
        }
        return modelAndView.getModel();
    }

    @ResponseBody
    @RequestMapping(LoginDefine.CHECKPASSWORD)
    public String checkPasswordAction(ModelAndView modelAndView, HttpServletRequest request) {
        // admin修改密码，输入旧密码后异步跳入此方法
        String name = request.getParameter("name");//这个获取的是JSP的控件名称 oldPassword
        String param = request.getParameter("param");//这个获取的是 输入的 旧密码 的内容
        JSONObject ret = new JSONObject();
        if (Validator.isNull(name)) {
            ret.put(ManageUsersDefine.JSON_VALID_INFO_KEY, "无效的控件名称");
        } else {
            if (Validator.equals(name, "oldPassword")) {
                if (Validator.isNull(param)) {
                    ret.put(ManageUsersDefine.JSON_VALID_INFO_KEY, "旧密码不能为空");
                } else {
                    // 验证旧密码是否正确
                    Subject currentUser = SecurityUtils.getSubject();//获取当前登录用户
                    Session session = currentUser.getSession();//获取用的session
                    if (session.getAttribute(CustomConstants.LOGIN_USER_INFO) != null) {
                        AdminSystem users = (AdminSystem) session.getAttribute(CustomConstants.LOGIN_USER_INFO);
                        // 使用用户名 以及 未加密过的密码查看存在性
                        AdminSystem u = loginService.getUsersByUserNameAndPassword(users.getUsername(), param);
                        if (u == null) {
                            ret.put(ManageUsersDefine.JSON_VALID_INFO_KEY, "旧密码不正确");
                        }
                    } else {
                        ret.put(ManageUsersDefine.JSON_VALID_INFO_KEY, "不存在登录用户,请重新登录");
                    }
                }
            }
//			if (Validator.equals(name, "newPassword1")) {
//				if (Validator.isNull(param)) {
//					ret.put(ManageUsersDefine.JSON_VALID_INFO_KEY, "新密码不能为空");
//				} else {
//					if (Validator.isNumber(param) || param.length() < 8 || param.length() > 16) {
//						ret.put(ManageUsersDefine.JSON_VALID_INFO_KEY, "密码8-16位，必须有数字字母组成，区分大小写");
//					}
//				}
//			}

        }
        // 没有错误时,返回y
        if (!ret.containsKey(ManageUsersDefine.JSON_VALID_INFO_KEY)) {
            ret.put(ManageUsersDefine.JSON_VALID_STATUS_KEY, ManageUsersDefine.JSON_VALID_STATUS_OK);
        }
        return ret.toString();
    }

    /**
     * 验证登录密码是否正确
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(LoginDefine.CHECKPWDISOK)
    public JSONObject checkAction(HttpServletRequest request, @RequestBody LoginBean form) {
        JSONObject resultJson = new JSONObject();
        String username = form.getUsername();
        String password = form.getPassword();
        //新增验证码逻辑begin
        //此代码为配合测试进行压力测试修改，无需合并主干 update by limeng
        String validateCode = form.getValidateCode();
        if (StringUtils.isEmpty(validateCode)) {
            resultJson.put(LoginDefine.JSON_VALID_STATUS_KEY, "4");
            return resultJson;//验证码为空
        } else {
            HttpSession session = request.getSession();
            String sessionCode = String.valueOf(session.getAttribute("validateCode"));
            if (!sessionCode.equals(validateCode)) {
                resultJson.put(LoginDefine.JSON_VALID_STATUS_KEY, "5");
                return resultJson;//验证码不相等
            }
        }
        //新增验证码逻辑end
        // 查询是否在数据库存在
        List<Admin> adminSystems = loginService.getUsersByUsername(username);

        if (adminSystems != null && adminSystems.size() == 1) {
            //2.判断是否错误超过错误次数
            Integer maxLoginErrorNum = LoginErrorConfigManager.getInstance().getAdminConfig().getMaxLoginErrorNum();//获取Redis配置的额登录最大错误次数
            //1.获取该用户密码错误次数
            String passwordErrorNum = RedisUtils.get(RedisConstants.PASSWORD_ERR_COUNT_ADMIN + username);
            String pwd_md5 = MD5.toMD5Code(password);
            //判断密码错误次数是否超限
            if (!StringUtils.isEmpty(passwordErrorNum) && Integer.parseInt(passwordErrorNum) >= maxLoginErrorNum) {
                resultJson.put(LoginDefine.JSON_VALID_STATUS_KEY, "6");
                long retTime = RedisUtils.ttl(RedisConstants.PASSWORD_ERR_COUNT_ADMIN + username);
                resultJson.put("retTime", DateUtils.SToHMSStr(retTime));
                return resultJson;//密码错误次数已达上限
            }
            if (!adminSystems.get(0).getPassword().equals(pwd_md5)) {
                //增加密码错误次数校验
                long value = loginService.insertPassWordCount(RedisConstants.PASSWORD_ERR_COUNT_ADMIN + username);//以用户手机号为key
                Integer loginLockTime = LoginErrorConfigManager.getInstance().getAdminConfig().getLockLong();
                if (value < maxLoginErrorNum + 1) {
                    if (maxLoginErrorNum - value == 3) {
                        resultJson.put(LoginDefine.JSON_VALID_STATUS_KEY, "7");
                        return resultJson;
                    }
                    if (maxLoginErrorNum - value == 2) {
                        resultJson.put(LoginDefine.JSON_VALID_STATUS_KEY, "8");
                        return resultJson;
                    }
                    if (maxLoginErrorNum - value == 1) {
                        resultJson.put(LoginDefine.JSON_VALID_STATUS_KEY, "9");
                        return resultJson;
                    }
                    if (maxLoginErrorNum - value == 0) {
                        LoginErrorLockUser loginErrorLockUser = new LoginErrorLockUser();
                        loginErrorLockUser.setUserid(adminSystems.get(0).getId());
                        loginErrorLockUser.setUsername(adminSystems.get(0).getUsername());
                        loginErrorLockUser.setMobile(adminSystems.get(0).getMobile());
                        loginErrorLockUser.setLockTime(new Date());
                        loginErrorLockUser.setUnlockTime(DateUtils.nowDateAddDate(loginLockTime));
                        loginErrorLockUser.setFront(0);
                        loginErrorLockUserService.insertLockUser(loginErrorLockUser);
                        resultJson.put(LoginDefine.JSON_VALID_STATUS_KEY, "6");
                        long retTime = RedisUtils.ttl(RedisConstants.PASSWORD_ERR_COUNT_ADMIN + username);
                        resultJson.put("retTime", DateUtils.SToHMSStr(retTime));
                        return resultJson;//密码错误次数已达上限
                    }
                    resultJson.put(LoginDefine.JSON_VALID_STATUS_KEY, "2");
                    return resultJson; // 密码错误，提示 用户名或密码错误
                } else {
                    throw new IllegalArgumentException("");
                }
            } else {
                resultJson.put(LoginDefine.JSON_VALID_STATUS_KEY, "0");
                return resultJson;
            }
        } else {
            resultJson.put(LoginDefine.JSON_VALID_STATUS_KEY, "1");
            return resultJson; // 用户名错误，提示 用户名或密码错误
        }
    }

}
