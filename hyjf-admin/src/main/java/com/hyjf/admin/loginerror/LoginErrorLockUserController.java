/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.loginerror;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hyjf.admin.loginerror.config.LoginErrorConfig;
import com.hyjf.admin.loginerror.config.LoginErrorConfigManager;
import com.hyjf.common.cache.RedisConstants;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mybatis.model.auto.LoginErrorLockUser;
import com.hyjf.mybatis.model.customize.AdminSystem;
import com.hyjf.mybatis.model.customize.loginerror.LoginErrorLockUserExtend;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * 登录失败锁定用户Controller
 *
 * @author cui
 * @version LoginErrorLockUserController, v0.1 2018/7/13 16:34
 */
@Controller
@RequestMapping(value = LoginErrorLockUserDefine.REQUEST_MAPPING)
public class LoginErrorLockUserController {

    @Autowired
    private LoginErrorLockUserService loginErrorLockUserService;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 前台锁定用户列表
     *
     * @param request
     * @param qo
     * @return
     */
    @RequestMapping(value = LoginErrorLockUserDefine.FRONT_INIT_ACTION)
    @RequiresPermissions(LoginErrorLockUserDefine.PERMISSIONS_VIEW)
    public ModelAndView frontLockUserList(HttpServletRequest request, LoginErrorLockUserQO qo) {

        ModelAndView modelAndView = new ModelAndView(LoginErrorLockUserDefine.FRONT_LOCK_USER_LIST_PATH);

        createPage(modelAndView, qo, true);

        return modelAndView;

    }

    /**
     * 后台锁定用户列表
     *
     * @param request
     * @param qo
     * @return
     */
    @RequestMapping(value = LoginErrorLockUserDefine.BACK_INIT_ACTION)
    @RequiresPermissions(LoginErrorLockUserDefine.PERMISSIONS_VIEW)
    public ModelAndView backLockUserList(HttpServletRequest request, LoginErrorLockUserQO qo) {

        ModelAndView modelAndView = new ModelAndView(LoginErrorLockUserDefine.BACK_LOCK_USER_LIST_PATH);

        createPage(modelAndView, qo, false);

        return modelAndView;

    }

    private void createPage(ModelAndView modelAndView, LoginErrorLockUserQO qo, boolean isFront) {

        Map<String, Object> parameterMap = Maps.newHashMap();
        parameterMap.put("lockTimeStartStr", qo.getLockTimeStartStr());
        parameterMap.put("lockTimeEndStr", qo.getLockTimeEndStr());
        parameterMap.put("username", qo.getUsername());
        parameterMap.put("mobile", qo.getMobile());
        parameterMap.put("isFront", isFront ? 1 : 0);

        int recordTotal = loginErrorLockUserService.countRecordTotal(parameterMap);
        if (recordTotal > 0) {
            Paginator paginator = new Paginator(qo.getPaginatorPage(), recordTotal);

            parameterMap.put("limitStart", paginator.getOffset());
            parameterMap.put("limitEnd", paginator.getLimit());

            List<LoginErrorLockUser> recordList = loginErrorLockUserService.getRecordList(parameterMap);
            List<LoginErrorLockUserExtend> lstRecord = parseRecord(recordList, isFront);
            qo.setPaginator(paginator);
            qo.setLstLockUser(lstRecord);
            modelAndView.addObject(LoginErrorLockUserDefine.LOCKUSER_LIST_FORM, qo);
        }

    }

    private List<LoginErrorLockUserExtend> parseRecord(List<LoginErrorLockUser> recordList, final boolean isFront) {
        return Lists.transform(recordList, new Function<LoginErrorLockUser, LoginErrorLockUserExtend>() {
            @Nullable
            @Override
            public LoginErrorLockUserExtend apply(@Nullable LoginErrorLockUser loginErrorLockUser) {
                LoginErrorLockUserExtend record = new LoginErrorLockUserExtend();
                BeanUtils.copyProperties(loginErrorLockUser, record);
                if (loginErrorLockUser.getLockTime() != null) {
                    String lockTimeString = sdf.format(loginErrorLockUser.getLockTime());
                    record.setLockTimeStr(lockTimeString);
                }

                //是否被锁定
                String redisKeyPrefix = isFront ? RedisConstants.PASSWORD_ERR_COUNT_ALL : RedisConstants.PASSWORD_ERR_COUNT_ADMIN;

                String key = record.getUsername();

                if (isFront) {
                    key = String.valueOf(loginErrorLockUserService.getLockedUserId(record.getUsername()));
                }

                boolean isLocked = RedisUtils.exists(redisKeyPrefix + key);

                record.setUnlocked(isLocked ? 0 : 1);

                return record;
            }
        });
    }

    @RequestMapping(value = LoginErrorLockUserDefine.BACK_UNLOCK_ACTION)
    @RequiresPermissions(LoginErrorLockUserDefine.PERMISSIONS_UNLOCK)
    @ResponseBody
    public JSONObject backUnlock(@RequestParam int id) {
        JSONObject result = new JSONObject();

        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        if (session.getAttribute(CustomConstants.LOGIN_USER_INFO) != null) {
            AdminSystem users = (AdminSystem) session.getAttribute(CustomConstants.LOGIN_USER_INFO);
            try {
                loginErrorLockUserService.updateUnlockBackUser(id, users.getId());
                result.put("success", "0");
                result.put("msg", "解锁操作成功！");
            } catch (Exception e) {
                logger.error("解除用户【{}】锁定失败", id, e);
                result.put("success", "1");
                result.put("msg", "解锁操作失败！");
            }
        }
        return result;
    }

    @RequestMapping(value = LoginErrorLockUserDefine.FRONT_UNLOCK_ACTION)
    @RequiresPermissions(LoginErrorLockUserDefine.PERMISSIONS_UNLOCK)
    @ResponseBody
    public JSONObject frontUnlock(@RequestParam int id) {
        JSONObject result = new JSONObject();

        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        if (session.getAttribute(CustomConstants.LOGIN_USER_INFO) != null) {
            AdminSystem users = (AdminSystem) session.getAttribute(CustomConstants.LOGIN_USER_INFO);
            try {
                loginErrorLockUserService.updateUnlockFrontUser(id, users.getId());
                result.put("success", "0");
                result.put("msg", "解锁操作成功！");
            } catch (Exception e) {
                logger.error("解除用户【{}】锁定失败", id);
                result.put("success", "1");
                result.put("msg", "解锁操作失败！");
            }
        }
        return result;
    }

    @RequestMapping(value = LoginErrorLockUserDefine.BACK_LOGINERRORCONFIG_ACTION)
    public ModelAndView backLoginErrorCfg(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView(LoginErrorLockUserDefine.BACK_LOGINERRORCFG_PATH);
        LoginErrorConfig.Config config = LoginErrorConfigManager.getInstance().getAdminConfig();
        modelAndView.addObject("config", config);
        return modelAndView;
    }

    @RequestMapping(value = LoginErrorLockUserDefine.FRONT_LOGINERRORCONFIG_ACTION)
    public ModelAndView frontLoginErrorCfg(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView(LoginErrorLockUserDefine.FRONT_LOGINERRORCFG_PATH);
        LoginErrorConfig.Config config = LoginErrorConfigManager.getInstance().getWebConfig();
        modelAndView.addObject("config", config);
        return modelAndView;
    }
}
