/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.manager.user.userportrait;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.UsersPortrait;
import com.hyjf.mybatis.model.customize.UserPortraitCustomize;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ${yaoy}
 * @version UserPortraitServiceImpl, v0.1 2018/5/11 15:04
 */
@Service
public class UserPortraitServiceImpl extends BaseServiceImpl implements UserPortraitService {

    @Override
    public int countRecordTotal(Map<String, Object> userPortrait) {
        int countTotal = userInfoCustomizeMapper.countRecordTotal(userPortrait);
        return countTotal;
    }


    @Override
    public List<UserPortraitCustomize> getRecordList(Map<String, Object> userPortrait, int limitStart, int limitEnd) {

        if (limitStart == 0 || limitStart > 0) {
            userPortrait.put("limitStart", limitStart);
        }
        if (limitEnd > 0) {
            userPortrait.put("limitEnd", limitEnd);
        }

        List<UserPortraitCustomize> usersPortraits = userInfoCustomizeMapper.selectPortraitList(userPortrait);
        return usersPortraits;
    }



    @Override
    public UsersPortrait getUsersPortraitByUserId(Integer userId) {
        UsersPortrait usersPortrait = usersPortraitMapper.selectByPrimaryKey(userId);
        return usersPortrait;
    }

    @Override
    public Map<String, Object> saveUserPortrait(UsersPortraitBean form) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            Integer userId = form.getUserId();
            if (userId != null) {
                UsersPortrait usersPortrait = new UsersPortrait();
                usersPortrait.setUserId(userId);
                usersPortrait.setUserName(form.getUserName());
                usersPortrait.setEducation(form.getEducation());
                usersPortrait.setOccupation(form.getOccupation());
                usersPortrait.setInterest(form.getInterest());
                usersPortrait.setLoginActive(form.getLoginActive());
                usersPortrait.setCustomerSource(form.getCustomerSource());
                usersPortrait.setInvestPlatform(form.getInvestPlatform());
                usersPortrait.setInvestAge(form.getInvestAge());
                usersPortrait.setCurrentOwner(form.getCurrentOwner());
                usersPortrait.setAddWechat(form.getAddWechat());
                usersPortrait.setCustomerComplaint(form.getCustomerComplaint());
                usersPortrait.setInviteCustomer(form.getInviteCustomer());
                usersPortrait.setRemark(form.getRemark());
                this.usersPortraitMapper.updateByPrimaryKeySelective(usersPortrait);
                resultMap.put("usersPortrait", usersPortrait);
                resultMap.put("success", true);
                resultMap.put("msg", "保存完成");
            }else{
                resultMap.put("success", false);
                resultMap.put("msg", "无需要编辑的信息");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultMap;
    }
}
