/**
 * Description:用户信息管理业务处理类接口实现
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 上午11:05:56
 * Modification History:
 * Modified by : 
 */

package com.hyjf.admin.manager.user.evaluation;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.UserEvalationResultCustomize;
import com.hyjf.mybatis.model.customize.UserEvalationResultExampleCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminUserEvalationCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminUserEvalationResultCustomize;

/**
 * @author 逄成超
 */
@Service("evaluationService")
public class EvaluationServiceImpl extends BaseServiceImpl implements EvaluationService {

    @Override
    public int countRecordTotal(Map<String, Object> user) {
        // 查询用户列表
        int count = adminUserEvalationResultCustomizeMapper.countRecordTotal(user);
        return count;
    }

    @Override
    public List<AdminUserEvalationResultCustomize> getRecordList(Map<String, Object> user,  int limitStart, int limitEnd) {
        if (limitStart == 0 || limitStart > 0) {
            user.put("limitStart", limitStart);
        }
        if (limitEnd > 0) {
            user.put("limitEnd", limitEnd);
        }
        // 查询用户列表
        List<AdminUserEvalationResultCustomize> users = adminUserEvalationResultCustomizeMapper.selectUserEvalationResultList(user);
        return users;
    }

    @Override
    public UserEvalationResultCustomize selectUserEvalationResultByUserId(int userId) {
        UserEvalationResultExampleCustomize example = new UserEvalationResultExampleCustomize();
        example.createCriteria().andUserIdEqualTo(userId);
        List<UserEvalationResultCustomize> userEvalationResultCustomize = userEvalationResultCustomizeMapper.selectByExample(example);
        if (userEvalationResultCustomize != null && userEvalationResultCustomize.size() > 0) {
            return userEvalationResultCustomize.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<AdminUserEvalationCustomize> getUserEvalation(Integer id) {
        List<AdminUserEvalationCustomize> list=adminUserEvalationResultCustomizeMapper.getUserEvalation(id);
        return list;
    }

    @Override
    public List<AdminUserEvalationResultCustomize> exoportRecordList(Map<String, Object> user) {
     // 查询用户列表
        List<AdminUserEvalationResultCustomize> users = adminUserEvalationResultCustomizeMapper.exoportRecordList(user);
        return users;
    }


}