package com.hyjf.admin.manager.user.changelog;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.UsersChangeLog;
import com.hyjf.mybatis.model.auto.UsersChangeLogExample;
import com.hyjf.mybatis.model.customize.ChangeLogCustomize;

@Service
public class ChangeLogServiceImpl extends BaseServiceImpl implements ChangeLogService {

    /**
     * 获取用户信息修改列表
     * 
     * @return
     */
    public List<UsersChangeLog> getRecordList(ChangeLogBean userChangeLog, int limitStart, int limitEnd) {
        UsersChangeLogExample example = new UsersChangeLogExample();
        example.setOrderByClause(" change_time desc ");
        if (limitStart != -1) {
            example.setLimitStart(limitStart);
            example.setLimitEnd(limitEnd);
        }
        com.hyjf.mybatis.model.auto.UsersChangeLogExample.Criteria criteria = example.createCriteria();
        // 条件查询
        if (StringUtils.isNotEmpty(userChangeLog.getUsername())) {
            criteria.andUsernameEqualTo(userChangeLog.getUsername());
        }
        if (StringUtils.isNotEmpty(userChangeLog.getRealName())) {
            criteria.andRealNameEqualTo(userChangeLog.getRealName());
        }
        if (StringUtils.isNotEmpty(userChangeLog.getMobile())) {
            criteria.andMobileEqualTo(userChangeLog.getMobile());
        }
        if (StringUtils.isNotEmpty(userChangeLog.getRecommendUser())) {
            criteria.andRecommendUserEqualTo(userChangeLog.getRecommendUser());
        }
        if (userChangeLog.getAttribute() != null) {
            criteria.andAttributeEqualTo(Integer.parseInt(userChangeLog.getAttribute()));
        }
        if (userChangeLog.getIs51() != null) {
            criteria.andIs51EqualTo(userChangeLog.getIs51());
        }
        return usersChangeLogMapper.selectByExample(example);
    }

    /**
     * 获取用户信息修改列表
     * @param userChangeLog
     * @return
     */
    public List<ChangeLogCustomize> getChangeLogList(ChangeLogBean userChangeLog) {
        List<ChangeLogCustomize> changeLogs = changeLogCustomizeMapper.queryChangeLogList(userChangeLog);
        return changeLogs;
    }
    
    /**
     * 
     * 获取某一用户的信息修改列表
     * @param userChangeLog
     * @param limitStart
     * @param limitEnd
     * @return
     */
    public List<ChangeLogCustomize> getUserRecordList(ChangeLogBean userChangeLog, int limitStart, int limitEnd) {
        if (limitStart != -1) {
            userChangeLog.setLimitStart(limitStart);
            userChangeLog.setLimitEnd(limitEnd);
        }
        return changeLogCustomizeMapper.queryChangeLogList(userChangeLog);
    }
    
    /**
     * 获取用户信息修改记录数
     * 
     * @param form
     * @return
     */

    @Override
    public int countRecordTotal(ChangeLogCustomize userChangeLog) {
        return changeLogCustomizeMapper.queryChangeLogCount(userChangeLog);
    }

    /**
     * 合规四期(手机号加密显示)
     * @param userChangeLog
     * @return
     * add by nxl
     */
    @Override
    public List<ChangeLogCustomize> selectChangeLogList(ChangeLogBean userChangeLog) {
        List<ChangeLogCustomize> changeLogs = changeLogCustomizeMapper.selectChangeLogList(userChangeLog);
        return changeLogs;
    }
}
