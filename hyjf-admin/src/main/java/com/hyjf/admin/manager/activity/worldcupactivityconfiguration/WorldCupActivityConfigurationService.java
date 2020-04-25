package com.hyjf.admin.manager.activity.worldcupactivityconfiguration;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.mybatis.model.auto.WorldCupTeam;
import com.hyjf.mybatis.model.customize.worldcup.WorldTeamCustomize;
import com.hyjf.mybatis.model.customize.worldcup.WorldTeamMatchCustomize;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * @author xiehuili on 2018/6/14.
 * 世界杯活动配置
 */
public interface WorldCupActivityConfigurationService {


    /**
     * 获取世界杯活动配置球队
     *
     * @return
     */
    public List<WorldCupTeam> getWorldCupTeamSelect();

    /**
     * 查询已经选择的球队对阵
     *
     * @return
     */
    public List<WorldTeamCustomize> getWorldCupTeamSelected();

    /**
     * 资料上传
     *
     * @param request
     * @return
     * @throws Exception
     */
    public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception;

    /**
     * 获取世界杯活动决战赛比赛配置
     *
     * @return
     */
    public  List<WorldTeamMatchCustomize> getWorldTeamMatchList();


    /**
     * 决战赛比赛配置提交
     * @param request
     */
    public JSONObject updateWorldCupMatch(HttpServletRequest request) ;

    /**
     * 决战赛比赛配置校验球队是否唯一选中 @RequestMapping值
     */
    public JSONObject validationWorldCupTeam(List<WorldTeamCustomize> list);

    /**
     * 决战赛球队配置提交
     */
    public void updateWorldCupTeam(List<WorldTeamCustomize> list) ;
    /**
     * 判断16进8球队结果是否已经有，
     */
    public Integer getWorldCupTeamResultCount();

}
