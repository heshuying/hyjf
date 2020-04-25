/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.bank.service.sensorsdata.register;

import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.StringUtil;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.UserDepartmentInfoCustomize;
import com.sensorsdata.analytics.javasdk.SensorsAnalytics;
import com.sensorsdata.analytics.javasdk.exceptions.InvalidArgumentException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 神策数据统计:注册业务相关Service实现类
 *
 * @author liuyang
 * @version SensorsDataRegisterServiceImpl, v0.1 2018/7/16 17:13
 */
@Service
public class SensorsDataRegisterServiceImpl extends BaseServiceImpl implements SensorsDataRegisterService {


    Logger _log = LoggerFactory.getLogger(SensorsDataRegisterServiceImpl.class);

    /**
     * 发送神策埋点数据
     *
     * @param sensorsDataBean
     * @throws IOException
     * @throws InvalidArgumentException
     */
    @Override
    public void sendSensorsData(SensorsDataBean sensorsDataBean) throws IOException, InvalidArgumentException {

        // log文件存放位置
        String logFilePath = PropUtils.getSystem("sensors.data.log.path");
        // 初始化神策SDK
        SensorsAnalytics sa = new SensorsAnalytics(new SensorsAnalytics.ConcurrentLoggingConsumer(logFilePath + "sensorsData.log"));
        // 前端传递过来的预置属性
        Map<String, Object> presetProps = sensorsDataBean.getPresetProps();
        // 匿名ID
        String distinctId = "";
        if (presetProps.get("_distinct_id") != null) {
            distinctId = (String) presetProps.get("_distinct_id");
            _log.info("匿名用户ID:" + distinctId);
        }
        //  设备ID
        String deviceId = "";
        if (presetProps.get("$device_id") != null) {
            deviceId = (String) presetProps.get("$device_id");
            _log.info("手机设备ID:" + deviceId);
        }
        if (StringUtils.isBlank(distinctId) && StringUtils.isBlank(deviceId)) {
            _log.error("神策数据采集:注册相关,获取匿名ID,设备ID为空");
            return;
        }
        // 用户ID
        Integer userId = sensorsDataBean.getUserId();
        // 根据用户ID 查询用户信息
        Users users = this.getUsers(userId);
        if (users == null) {
            _log.error("根据用户ID查询用户信息失败,用户ID:" + userId + "].");
            return;
        }
        // 根据用户ID 查询用户详情
        UsersInfo usersInfo = this.getUsersInfoByUserId(userId);
        if (usersInfo == null) {
            _log.error("根据用户ID查询用户详情信息失败,用户ID:" + userId + "].");
            return;
        }

        // 调用神策
        // 2.1 通过，trackSignUP，把匿名ID和注册ID贯通起来
        sa.trackSignUp(String.valueOf(userId), StringUtils.isBlank(distinctId) ? deviceId : distinctId);
        // 构建用户属性
        // 2.2 用户注册时，填充了一些个人信息，可以用Profile接口记录下来
        Map<String, Object> profiles = new HashMap<String, Object>();
        // 不可变更用户属性
        Map<String, Object> userProfiles = new HashMap<String, Object>();

        // mod by liuyang 20181029 神策数据统计敏感数据删除 start
        // 手机号
        // profiles.put("phoneNumber", users.getMobile());
        profiles.put("phoneNumber", "");
        // mod by liuyang 20181029 神策数据统计敏感数据删除 end
        // 注册渠道
        // 根据用户ID 查询用户注册渠道
        UtmReg utmReg = this.selectUtmRegByUserId(userId);
        // 如果是pc渠道过来的用户
        if (utmReg != null) {
            // 根据utmId查询渠道信息
            Integer utmId = utmReg.getUtmId();
            Utm utm = this.getUtm(utmId);
            // 获取source_id
            Integer sourceId = utm.getSourceId();
            // 根据source_id 获取推过渠道信息
            UtmPlat utmPlat = this.getUtmPlatBySourceId(sourceId);
            // 注册渠道
            profiles.put("registerChannel", utmPlat == null ? "" : utmPlat.getSourceName());
        }

        // 如果 是用户app注册渠道过来的
        AppChannelStatisticsDetail appChannelStatisticsDetail = this.selectAppChannelStatisticsDetail(userId);
        if (appChannelStatisticsDetail != null) {
            // 注册渠道
            profiles.put("registerChannel", StringUtils.isBlank(appChannelStatisticsDetail.getSourceName()) ? "" : appChannelStatisticsDetail.getSourceName());
        }
        // 根据用户ID 查询用户推荐人信息
        SpreadsUsers spreadsUsers = this.getSpreadsUsersByUserId(userId);
        // 用户没有推荐人
        if (spreadsUsers == null) {
            // 当前邀请人
            profiles.put("newinviter", "");
            // 注册时邀请人
            userProfiles.put("inviter", "");
        } else {
            // 推荐人用户ID
            Integer spreadsUserId = spreadsUsers.getSpreadsUserid();
            // 推荐人用户名
            Users spreadsUser = this.getUsers(spreadsUserId);
            // 根据用户ID 查询 推荐人情况
            profiles.put("newinviter", spreadsUser == null ? "" : spreadsUser.getUsername());
            // 注册时邀请人
            userProfiles.put("inviter", spreadsUser == null ? "" : spreadsUser.getUsername());
        }
        // 用户类型
        if (users.getUserType() == 1) {
            profiles.put("Utype", "企业用户");
        } else {
            profiles.put("Utype", "个人用户");
        }
        // 用户角色
        if (usersInfo.getRoleId() == 1) {
            profiles.put("roleType", "出借人");
        } else if (usersInfo.getRoleId() == 2) {
            profiles.put("roleType", "借款人");
        } else {
            profiles.put("roleType", "担保机构");
        }
        // 用户属性
        if (usersInfo.getAttribute() == 0) {
            // 当前用户属性
            profiles.put("newattribute", "无主单");
            // 注册时用户属性
            userProfiles.put("attribute", "无主单");
        } else if (usersInfo.getAttribute() == 1) {
            // 当前用户属性
            profiles.put("newattribute", "有主单");
            // 注册时用户属性
            userProfiles.put("attribute", "有主单");
        } else if (usersInfo.getAttribute() == 2) {
            // 当前用户属性
            profiles.put("newattribute", "线下员工");
            // 注册时用户属性
            userProfiles.put("attribute", "线下员工");
        } else if (usersInfo.getAttribute() == 3) {
            // 当前用户属性
            profiles.put("newattribute", "线上员工");
            // 注册时用户属性
            userProfiles.put("attribute", "线上员工");
        }

        // 根据用户ID 查询用户部门信息
        UserDepartmentInfoCustomize userDepartmentInfoCustomize = this.userDepartmentInfoCustomizeMapper.selectUserDepartmentInfo(userId);
        if (userDepartmentInfoCustomize == null) {
            // 当前分公司
            profiles.put("newregionName", "");
            // 当前分部
            profiles.put("newbranchName", "");
            // 当前团队
            profiles.put("newdepartmentName", "");

            // 注册时分公司
            userProfiles.put("regionName", "");
            // 注册时分部
            userProfiles.put("branchName", "");
            // 注册时团队
            userProfiles.put("departmentName", "");

        } else {
            // 分公司
            profiles.put("newregionName", StringUtils.isBlank(userDepartmentInfoCustomize.getRegionName()) ? "" : userDepartmentInfoCustomize.getRegionName());
            // 分部
            profiles.put("newbranchName", StringUtils.isBlank(userDepartmentInfoCustomize.getBranchName()) ? "" : userDepartmentInfoCustomize.getBranchName());
            // 团队
            profiles.put("newdepartmentName", StringUtils.isBlank(userDepartmentInfoCustomize.getDepartmentName()) ? "" : userDepartmentInfoCustomize.getDepartmentName());

            // 注册时分公司
            userProfiles.put("regionName", StringUtils.isBlank(userDepartmentInfoCustomize.getRegionName()) ? "" : userDepartmentInfoCustomize.getRegionName());
            // 注册时分部
            userProfiles.put("branchName", StringUtils.isBlank(userDepartmentInfoCustomize.getBranchName()) ? "" : userDepartmentInfoCustomize.getBranchName());
            // 注册时团队
            userProfiles.put("departmentName", StringUtils.isBlank(userDepartmentInfoCustomize.getDepartmentName()) ? "" : userDepartmentInfoCustomize.getDepartmentName());
        }


        // 设置用户表预置属性
        // 城市
        profiles.put("$city", presetProps.get("$city") == null ? "" : presetProps.get("$city"));
        // 省份
        profiles.put("$province", presetProps.get("$province") == null ? "" : presetProps.get("$province"));
        // 用户名
        userProfiles.put("$name", users.getUsername());
        // 注册时间
        if (users.getRegTime() != 0) {
            userProfiles.put("$signup_time", GetDate.timestamptoNUMStrYYYYMMDDHHMMSS(users.getRegTime()));
        }
        //iOS渠道追踪匹配模式
        profiles.put("$utm_matching_type", presetProps.get("$utm_matching_type") == null ? "" : presetProps.get("$utm_matching_type"));
        if (presetProps.get("$first_visit_time") != null) {
            // 首次访问时间
            userProfiles.put("$first_visit_time", presetProps.get("$first_visit_time"));
        }
        // 首次前向地址
        userProfiles.put("$first_referrer", presetProps.get("$first_referrer") == null ? "" : presetProps.get("$first_referrer"));
        // 首次前向域名
        userProfiles.put("$first_referrer_host", presetProps.get("$first_referrer_host") == null ? "" : presetProps.get("$first_referrer_host"));
        // 首次使用的浏览器语言
        userProfiles.put("$first_browser_language", presetProps.get("$first_browser_language") == null ? "" : presetProps.get("$first_browser_language"));
        // 首次浏览器字符类型（1.8支持）
        userProfiles.put("$first_browser_charset", presetProps.get("$first_browser_charset") == null ? "" : presetProps.get("$first_browser_charset"));
        // 首次搜索引擎关键词（1.8支持）
        userProfiles.put("$first_search_keyword", presetProps.get("$first_browser_charset") == null ? "" : presetProps.get("$first_browser_charset"));
        // 首次流量来源类型（1.8支持）
        userProfiles.put("$first_traffic_source_type", presetProps.get("$first_traffic_source_type") == null ? "" : presetProps.get("$first_traffic_source_type"));
        // 首次广告系列来源
        userProfiles.put("$utm_source", presetProps.get("$utm_source") == null ? "" : presetProps.get("$utm_source"));
        // 首次广告系列媒介
        userProfiles.put("$utm_medium", presetProps.get("$utm_medium") == null ? "" : presetProps.get("$utm_medium"));
        // 首次广告系列字词
        userProfiles.put("$utm_term", presetProps.get("$utm_term") == null ? "" : presetProps.get("$utm_term"));
        // 首次广告系列内容
        userProfiles.put("$utm_content", presetProps.get("$utm_content") == null ? "" : presetProps.get("$utm_content"));
        // 首次广告系列名称
        userProfiles.put("$utm_campaign", presetProps.get("$utm_campaign") == null ? "" : presetProps.get("$utm_campaign"));
        // 此时传入的是注册ID了
        sa.profileSet(String.valueOf(userId), true, profiles);
        // 设置用户不可变更属性
        sa.profileSetOnce(String.valueOf(userId), true, userProfiles);
        // 2.3 立刻刷新一下，让数据传到SA中
        sa.flush();
        sa.shutdown();
    }


    /**
     * 根据用户ID查询用户注册渠道
     *
     * @param userId
     * @return
     */
    private UtmReg selectUtmRegByUserId(Integer userId) {
        UtmRegExample example = new UtmRegExample();
        UtmRegExample.Criteria cra = example.createCriteria();
        cra.andUserIdEqualTo(userId);
        List<UtmReg> list = this.utmRegMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据utmId查询
     *
     * @param utmId
     * @return
     */
    private Utm getUtm(Integer utmId) {
        UtmExample example = new UtmExample();
        UtmExample.Criteria cra = example.createCriteria();
        cra.andUtmIdEqualTo(utmId);
        List<Utm> list = this.utmMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }


    /**
     * 根据sourceId查询推广渠道平台
     *
     * @param sourceId
     * @return
     */
    private UtmPlat getUtmPlatBySourceId(Integer sourceId) {
        UtmPlatExample example = new UtmPlatExample();
        UtmPlatExample.Criteria cra = example.createCriteria();
        cra.andSourceIdEqualTo(sourceId);
        List<UtmPlat> list = this.utmPlatMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }


    /**
     * 根据用户ID 查询用户推广渠道
     *
     * @param userId
     * @return
     */
    private AppChannelStatisticsDetail selectAppChannelStatisticsDetail(Integer userId) {
        AppChannelStatisticsDetailExample example = new AppChannelStatisticsDetailExample();
        AppChannelStatisticsDetailExample.Criteria cra = example.createCriteria();
        cra.andUserIdEqualTo(userId);
        List<AppChannelStatisticsDetail> list = this.appChannelStatisticsDetailMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据用户ID 查询 用户推荐人
     *
     * @param userId
     * @return
     */
    private SpreadsUsers selectSpreadsUsersByUserId(Integer userId) {
        SpreadsUsersExample example = new SpreadsUsersExample();
        SpreadsUsersExample.Criteria cra = example.createCriteria();
        cra.andUserIdEqualTo(userId);

        List<SpreadsUsers> list = this.spreadsUsersMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
}
