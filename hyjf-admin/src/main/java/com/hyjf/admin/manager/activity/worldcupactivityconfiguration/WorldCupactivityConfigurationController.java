package com.hyjf.admin.manager.activity.worldcupactivityconfiguration;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.mybatis.model.auto.WorldCupTeam;
import com.hyjf.mybatis.model.customize.worldcup.WorldTeamCustomize;
import com.hyjf.mybatis.model.customize.worldcup.WorldTeamMatchCustomize;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiehuili on 2018/6/13.
 * 世界杯活动配置
 */
@Controller
@RequestMapping(value = WorldCupactivityConfigurationDefine.REQUEST_MAPPING)
public class WorldCupactivityConfigurationController extends BaseController {

    @Autowired
    private WorldCupActivityConfigurationService worldCupActivityConfigurationService;

    /**
     * 列表初始化
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(WorldCupactivityConfigurationDefine.INIT)
    @RequiresPermissions(WorldCupactivityConfigurationDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,
                             @ModelAttribute(WorldCupactivityConfigurationDefine.FORM) WorldCupactivityConfigurationBean form) {
        LogUtil.startLog(WorldCupactivityConfigurationDefine.class.toString(), WorldCupactivityConfigurationDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(WorldCupactivityConfigurationDefine.LIST_PATH);
        //查询select下拉框
        List<WorldCupTeam> record = worldCupActivityConfigurationService.getWorldCupTeamSelect();
        //查询球队对阵
        List<WorldTeamCustomize> recordSelected =worldCupActivityConfigurationService.getWorldCupTeamSelected();
        //判断16进8球队结果是否已经有，
        int count=worldCupActivityConfigurationService.getWorldCupTeamResultCount();
//        String webUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
//        modelAndView.addObject("webUrl", webUrl);
        modelAndView.addObject("mathResult", count);
        modelAndView.addObject("record", record);
        modelAndView.addObject("recordSelected", recordSelected);
        LogUtil.endLog(WorldCupactivityConfigurationDefine.class.toString(), WorldCupactivityConfigurationDefine.INIT);
        return modelAndView;
    }


    /**
     * 资料上传
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = WorldCupactivityConfigurationDefine.UPLOAD_FILE, method = RequestMethod.POST)
    @ResponseBody
    public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LogUtil.startLog(WorldCupactivityConfigurationDefine.class.toString(), WorldCupactivityConfigurationDefine.UPLOAD_FILE);
        String files = worldCupActivityConfigurationService.uploadFile(request, response);
        LogUtil.endLog(WorldCupactivityConfigurationDefine.class.toString(), WorldCupactivityConfigurationDefine.UPLOAD_FILE);
        return files;
    }

    /**
     * 决战赛比赛列表初始化
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(WorldCupactivityConfigurationDefine.MATCH_INIT)
    @RequiresPermissions(WorldCupactivityConfigurationDefine.PERMISSIONS_VIEW)
    public ModelAndView matchInit(HttpServletRequest request, RedirectAttributes attr,
                                  @ModelAttribute(WorldCupactivityConfigurationDefine.FORM) WorldCupactivityConfigurationBean form) {
        LogUtil.startLog(WorldCupactivityConfigurationDefine.class.toString(), WorldCupactivityConfigurationDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(WorldCupactivityConfigurationDefine.MATCH_LIST_PATH);
        //查询数据
        List<WorldTeamMatchCustomize> recordList = worldCupActivityConfigurationService.getWorldTeamMatchList();
        modelAndView.addObject("recordList", recordList);
        LogUtil.endLog(WorldCupactivityConfigurationDefine.class.toString(), WorldCupactivityConfigurationDefine.INIT);
        return modelAndView;
    }

    /**
     * 决战赛比赛配置提交
     * @param request
     */
    @RequestMapping(WorldCupactivityConfigurationDefine.UPDATE_WORLD_CUP_MATCH)
    @ResponseBody
    @RequiresPermissions(WorldCupactivityConfigurationDefine.PERMISSIONS_ADD)
    public JSONObject updateWorldCupMatch(HttpServletRequest request) {
        LogUtil.startLog(WorldCupactivityConfigurationDefine.class.toString(), WorldCupactivityConfigurationDefine.UPDATE_WORLD_CUP_MATCH);
        JSONObject json = worldCupActivityConfigurationService.updateWorldCupMatch(request);
        LogUtil.endLog(WorldCupactivityConfigurationDefine.class.toString(), WorldCupactivityConfigurationDefine.UPDATE_WORLD_CUP_MATCH);
        return  json;
    }

    /**
     * 决战赛比赛配置校验球队非空和是否唯一选中 @RequestMapping值
     */
    @RequestMapping(WorldCupactivityConfigurationDefine.VALIDDATION_TEAM)
    @ResponseBody
    public JSONObject validationWorldCupTeam(HttpServletRequest request, @ModelAttribute(WorldCupactivityConfigurationDefine.MATCH_FORM) WorldCupactivityConfigurationBean form) {
        LogUtil.startLog(WorldCupactivityConfigurationDefine.class.toString(), WorldCupactivityConfigurationDefine.UPDATE_WORLD_CUP_MATCH);
        List<WorldTeamCustomize> list = new ArrayList<WorldTeamCustomize>();
        JSONObject json = new JSONObject();
        if (form != null) {
            list = handleFormToList(form, list);
            if(!CollectionUtils.isEmpty(list)){
                json = worldCupActivityConfigurationService.validationWorldCupTeam(list);
            }else{
                json.put("error","请选择球队或球队LOGO！");
            }
        }
        LogUtil.endLog(WorldCupactivityConfigurationDefine.class.toString(), WorldCupactivityConfigurationDefine.UPDATE_WORLD_CUP_MATCH);
        return json;
    }

    /**
     * 决战赛球队配置提交 @RequestMapping值
     */
    @RequestMapping(WorldCupactivityConfigurationDefine.SUBMIT_TEAM_ACTION)
    @RequiresPermissions(WorldCupactivityConfigurationDefine.PERMISSIONS_ADD)
    public ModelAndView submitWorldTeam(HttpServletRequest request, @ModelAttribute(WorldCupactivityConfigurationDefine.MATCH_FORM) WorldCupactivityConfigurationBean form) {
        LogUtil.startLog(WorldCupactivityConfigurationDefine.class.toString(), WorldCupactivityConfigurationDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(WorldCupactivityConfigurationDefine.RE_LIST_PATH);
        List<WorldTeamCustomize> list = new ArrayList<WorldTeamCustomize>();
        if (form != null) {
            list = handleFormToList(form, list);
            if(!CollectionUtils.isEmpty(list)){
                worldCupActivityConfigurationService.updateWorldCupTeam(list);
            }
        }
        LogUtil.endLog(WorldCupactivityConfigurationDefine.class.toString(), WorldCupactivityConfigurationDefine.INIT);
        return modelAndView;

    }

    //对接收到的数据进行处理
    public List<WorldTeamCustomize> handleFormToList(WorldCupactivityConfigurationBean form, List<WorldTeamCustomize> list) {
        String s="";
        if (form != null) {
            WorldTeamCustomize team1c2d = form.getTeam1c2d();
            if (team1c2d != null) {
                list.add(team1c2d);
            }
            WorldTeamCustomize team1a2b = form.getTeam1a2b();
            if (team1a2b != null) {
                list.add(team1a2b);
            }
            WorldTeamCustomize team1f2e = form.getTeam1f2e();
            if (team1f2e != null) {
            	list.add(team1f2e);
            }
            WorldTeamCustomize team1h2g = form.getTeam1h2g();
            if (team1h2g != null) {
            	list.add(team1h2g);
            }
            WorldTeamCustomize team1e2f = form.getTeam1e2f();
            if (team1e2f != null) {
            	list.add(team1e2f);
            }
            WorldTeamCustomize team1g2h = form.getTeam1g2h();
            if (team1g2h != null) {
            	list.add(team1g2h);
            }
            
            WorldTeamCustomize team1b2a = form.getTeam1b2a();
            if (team1b2a != null) {
                list.add(team1b2a);
            }
            WorldTeamCustomize team1d2c = form.getTeam1d2c();
            if (team1d2c != null) {
                list.add(team1d2c);
            }
            //对球队和球队logo进行校验
            for(int i=0;i<list.size();i++){
                Integer homeId = list.get(i).getHomeId();
                Integer visiId = list.get(i).getVisitedId();
                if(homeId ==null||visiId==null){
                    s+="请选择主队或客队";
                }
                if (StringUtils.isBlank(list.get(i).getHomeLogo())||StringUtils.isBlank(list.get(i).getVisitedLogo()) ) {
                    s +="请选择主队或客队LOGO";
                }
                break;
            }
        }
        return s == ""? list : null;
    }

}