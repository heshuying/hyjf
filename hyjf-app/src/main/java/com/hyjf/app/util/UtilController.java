/**
 * Description:获取指定的项目类型的项目列表
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 *           Created at: 2015年11月11日 下午2:17:31
 *           Modification History:
 *           Modified by :
 */
package com.hyjf.app.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hyjf.app.BaseController;
import com.hyjf.app.BaseResultBean;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mybatis.mapper.auto.UsersMapper;
import com.hyjf.mybatis.mapper.customize.UtilMapper;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;

/**
 * 
 * App充值控制器
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年2月19日
 * @see 下午2:14:02
 */
@Controller
@RequestMapping(value = "/util")
public class UtilController extends BaseController {

    @Autowired
    private UtilMapper utilMapper;

    @Autowired
    private UsersMapper usersMapper;

    /**
     * 
     * 清除手机号注册信息
     * @author renxingchen
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/clearMobile")
    public Object clearMobile(String mobile) {
        BaseResultBean resultBean = new BaseResultBean("/clearMobile");
        UsersExample example = new UsersExample();
        example.createCriteria().andMobileEqualTo(mobile);
        List<Users> users = usersMapper.selectByExample(example);
        Integer userId;
        if (null != users && !users.isEmpty()) {
            userId = users.get(0).getUserId();
            int temp = 0;
            // 查询用户信息
            temp = temp + utilMapper.deleteHuiyingdaiUsers(userId);
            temp = temp + utilMapper.deleteHuiyingdaiUsersInfo(userId);
            temp = temp + utilMapper.deleteHuiyingdaiAccount(userId);
            temp = temp + utilMapper.deleteHuiyingdaiAccountBank(userId);
            temp = temp + utilMapper.deleteHuiyingdaiUsersLog(userId);
            temp = temp + utilMapper.deleteHuiyingdaiUsersContact(userId);
            temp = temp + utilMapper.deleteHuiyingdaiSpreadsLog(userId);
            temp = temp + utilMapper.deleteHuiyingdaiSpreadsUsers(userId);
            temp = temp + utilMapper.deleteHuiyingdaiSpreadsUsersLog(userId);
            System.err.println(temp);
            resultBean.setStatus(CustomConstants.APP_STATUS_SUCCESS);
            resultBean.setStatusDesc(CustomConstants.APP_STATUS_DESC_SUCCESS);
        } else {
            resultBean.setStatusDesc(CustomConstants.APP_STATUS_DESC_FAIL);
        }
        return resultBean;
    }

}
