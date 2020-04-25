package com.hyjf.api.web.referee;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.api.web.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.SpreadsUsersExample;
import com.hyjf.mybatis.model.auto.SpreadsUsersLog;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersChangeLog;
import com.hyjf.mybatis.model.auto.UsersChangeLogExample;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.mybatis.model.customize.UserUpdateParamCustomize;
import com.hyjf.mybatis.model.customize.admin.UserInfoForLogCustomize;

@Service
public class RefereeServiceImpl extends BaseServiceImpl implements RefereeService {

    @Override
    public int countUserById(String userId) {
        int ret = 0;
        if (StringUtils.isNotEmpty(userId)) {
            UsersExample example = new UsersExample();
            UsersExample.Criteria cra = example.createCriteria();
            cra.andUserIdEqualTo(Integer.parseInt(userId));
            ret = this.usersMapper.countByExample(example);
        }
        return ret;
    }

    @Override
    public int updateSpreadsUsers(String userId, String spreadsUserId, String operation, String ip) {
        int ret = 0;
        int currentTime = GetDate.getNowTime10();
        // 旧推荐人名称
        String oldRecommendUser = "";
        // 新推荐人用户名称
        String newRecommendUser = "";
        // 更新推荐人
        Users users = this.usersMapper.selectByPrimaryKey(Integer.parseInt(userId));
        if (users != null) {
            // 旧推荐人名称
            oldRecommendUser = users.getReferrerUserName();
            // 推荐人id
            users.setReferrer(Integer.parseInt(spreadsUserId));
            // 推荐人用户名获取
            Users spreadsUser = this.usersMapper.selectByPrimaryKey(Integer.parseInt(spreadsUserId));
            // 新推荐人用户名
            newRecommendUser = spreadsUser.getUsername();
            // 推荐人用户名
            users.setReferrerUserName(newRecommendUser);
            // 更新Users表
            ret += usersMapper.updateByPrimaryKeySelective(users);
        }

        // 更新userInfo的主单与非主单信息
        updateUserParam(Integer.parseInt(userId));
        // 更新推荐人关系表
        SpreadsUsersExample example = new SpreadsUsersExample();
        SpreadsUsersExample.Criteria cra = example.createCriteria();
        cra.andUserIdEqualTo(Integer.parseInt(userId));
        // 根据用户id检索推荐人关系表
        List<SpreadsUsers> spreadUsers = spreadsUsersMapper.selectByExample(example);
        // 旧推荐人ID
        Integer oldSpreadUserId = null;

        if (spreadUsers != null && spreadUsers.size() > 0) {
            // 更新推荐人关系表
            SpreadsUsers spreadUser = spreadUsers.get(0);
            oldSpreadUserId = spreadUser.getSpreadsUserid();
            // 设置新的推荐人id
            spreadUser.setSpreadsUserid(Integer.parseInt(spreadsUserId));
            // 操作者
            spreadUser.setOperation(operation);
            // 保存推荐人信息
            ret += spreadsUsersMapper.updateByPrimaryKeyWithBLOBs(spreadUser);

            SpreadsUsersLog spreadsUsersLog = new SpreadsUsersLog();
            spreadsUsersLog.setOldSpreadsUserid(oldSpreadUserId);
            spreadsUsersLog.setUserId(Integer.parseInt(userId));
            spreadsUsersLog.setSpreadsUserid(Integer.parseInt(spreadsUserId));
            spreadsUsersLog.setType("crm");
            spreadsUsersLog.setOpernote("crm");
            spreadsUsersLog.setOperation(operation);
            spreadsUsersLog.setAddtime(String.valueOf(currentTime));
            spreadsUsersLog.setAddip(ip);
            // 保存相应的更新日志信息
            ret += spreadsUsersLogMapper.insertSelective(spreadsUsersLog);
        } else {
            SpreadsUsers spreadsUsers = new SpreadsUsers();
            // 用户id
            spreadsUsers.setUserId(Integer.parseInt(userId));
            // 推荐人id
            spreadsUsers.setSpreadsUserid(Integer.parseInt(spreadsUserId));
            spreadsUsers.setType("crm");
            spreadsUsers.setOpernote("crm");
            spreadsUsers.setAddtime(String.valueOf(currentTime));
            spreadsUsers.setOperation(operation);
            spreadsUsers.setAddip(ip);

            // 插入推荐人
            ret += spreadsUsersMapper.insertSelective(spreadsUsers);

            SpreadsUsersLog spreadsUsersLog = new SpreadsUsersLog();
            spreadsUsersLog.setOldSpreadsUserid(null);
            spreadsUsersLog.setUserId(Integer.parseInt(userId));
            spreadsUsersLog.setSpreadsUserid(Integer.parseInt(spreadsUserId));
            spreadsUsersLog.setType("crm");
            spreadsUsersLog.setOpernote("crm");
            spreadsUsersLog.setOperation(operation);
            spreadsUsersLog.setAddtime(String.valueOf(currentTime));
            spreadsUsersLog.setAddip(ip);
            // 保存相应的更新日志信息
            ret += spreadsUsersLogMapper.insertSelective(spreadsUsersLog);
        }

        // 保存用户信息修改日志
        List<UserInfoForLogCustomize> usersLog = usersCustomizeMapper.selectUserByUserId(Integer.parseInt(userId));
        if (usersLog != null && !usersLog.isEmpty()) {
            UserInfoForLogCustomize customize = usersLog.get(0);
            UsersChangeLog changeLog = new UsersChangeLog();
            changeLog.setUserId(customize.getUserId());
            changeLog.setUsername(customize.getUserName());
            changeLog.setAttribute(customize.getAttribute());
            changeLog.setIs51(customize.getIs51());
            changeLog.setRole(customize.getUserRole());
            changeLog.setMobile(customize.getMobile());
            changeLog.setRealName(customize.getRealName());
            changeLog.setRecommendUser(oldRecommendUser);
            changeLog.setStatus(customize.getUserStatus());
            changeLog.setChangeType(1);

            UsersChangeLogExample logExample = new UsersChangeLogExample();
            UsersChangeLogExample.Criteria logCriteria = logExample.createCriteria();
            logCriteria.andUserIdEqualTo(Integer.parseInt(userId));
            int count = usersChangeLogMapper.countByExample(logExample);
            if (count <= 0) {
                // 如果从来没有添加过操作日志，则将原始信息插入修改日志中
                changeLog.setRemark("初始注册");
                changeLog.setChangeUser("system");
                changeLog.setChangeTime(customize.getRegTime());
                usersChangeLogMapper.insertSelective(changeLog);
            }

            // 获取操作者id
            Integer operationId = 0;
            UsersExample usersExample = new UsersExample();
            UsersExample.Criteria usersCra = usersExample.createCriteria();
            usersCra.andUsernameEqualTo(operation);
            List<Users> operationUsers = this.usersMapper.selectByExample(usersExample);
            if (operationUsers != null && operationUsers.size() > 0) {
                Users operationUser = operationUsers.get(0);
                operationId = operationUser.getUserId();
            }
            // 插入一条用户信息修改日志
            changeLog.setChangeUser(operation);
            // 操作人
            changeLog.setChangeUserid(operationId);
            changeLog.setRecommendUser(newRecommendUser);
            changeLog.setRemark("crm");
            changeLog.setChangeTime(currentTime);
            ret += usersChangeLogMapper.insertSelective(changeLog);
        }
        return ret;
    }

    /**
     * 更新userInfo的主单与非主单信息
     * 
     * @param userId
     *            用户ID
     * @author 孙亮
     * @since 2015年12月31日 上午9:15:34
     */
    @Override
    public void updateUserParam(Integer userId) {

        Users users = usersMapper.selectByPrimaryKey(userId);
        if (users != null) {
            UsersInfoExample uie = new UsersInfoExample();
            UsersInfoExample.Criteria uipec = uie.createCriteria();
            uipec.andUserIdEqualTo(userId);
            List<UsersInfo> usersInfoList = usersInfoMapper.selectByExample(uie);
            if (usersInfoList != null && usersInfoList.size() == 1) {
                // 如果userinfo不为空
                UsersInfo userInfo = usersInfoList.get(0);
                userInfo.setAttribute(0);// 默认为无主单
                {
                    // 从oa表中查询线上线下部门属性
                    List<UserUpdateParamCustomize> userUpdateParamList =
                            ontimeUserLeaveCustomizeMapper.queryUserAndDepartment(userInfo.getUserId());
                    if (userUpdateParamList != null && userUpdateParamList.size() > 0) {
                        if (userUpdateParamList.get(0).getCuttype() != null) {
                            if (userUpdateParamList.get(0).getCuttype().equals("1")) {
                                // 线上
                                userInfo.setAttribute(3);
                            }
                            if (userUpdateParamList.get(0).getCuttype().equals("2")) {
                                // 线下
                                userInfo.setAttribute(2);
                            }
                        }
                    }
                }
                {
                    // 更新attribute
                    if (userInfo.getAttribute() != 2 && userInfo.getAttribute() != 3) {
                        if (Validator.isNotNull(users.getReferrer())) {
                            UsersInfoExample puie = new UsersInfoExample();
                            UsersInfoExample.Criteria puipec = puie.createCriteria();
                            puipec.andUserIdEqualTo(users.getReferrer());
                            List<UsersInfo> pUsersInfoList = usersInfoMapper.selectByExample(puie);
                            if (pUsersInfoList != null && pUsersInfoList.size() == 1) {
                                // 如果该用户的上级不为空
                                UsersInfo parentInfo = pUsersInfoList.get(0);
                                if (Validator.isNotNull(parentInfo) && Validator.isNotNull(parentInfo.getAttribute())) {
                                    if (Validator.equals(parentInfo.getAttribute(), new Integer(2))
                                            || Validator.equals(parentInfo.getAttribute(), new Integer(3))) {
                                        // 有推荐人且推荐人为员工(Attribute=2或3)时才设置为有主单
                                        userInfo.setAttribute(1);
                                    }
                                }
                            }
                        }
                    }
                }
                usersInfoMapper.updateByPrimaryKey(userInfo);
            }
        }
    }

    @Override
    public UsersInfo selectUserInfoByUserId(Integer userId) {
        UsersInfo usersInfo = new UsersInfo();
        UsersInfoExample example = new UsersInfoExample();
        UsersInfoExample.Criteria cra = example.createCriteria();
        cra.andUserIdEqualTo(userId);
        List<UsersInfo> usersInfoList = usersInfoMapper.selectByExample(example);
        if (usersInfoList != null && usersInfoList.size() > 0) {
            usersInfo = usersInfoList.get(0);
        }
        return usersInfo;
    }

    @Override
    public Users selectUserByUserId(Integer userId) {

        return usersMapper.selectByPrimaryKey(userId);
    }

}
