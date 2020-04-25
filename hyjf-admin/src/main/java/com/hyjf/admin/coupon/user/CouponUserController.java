package com.hyjf.admin.coupon.user;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.annotate.Token;
import com.hyjf.admin.coupon.config.CouponConfigController;
import com.hyjf.admin.coupon.config.CouponConfigDefine;
import com.hyjf.admin.coupon.config.CouponConfigService;
import com.hyjf.admin.coupon.tender.hzt.CouponTenderHztService;
import com.hyjf.admin.manager.activity.activitylist.ActivityListBean;
import com.hyjf.admin.manager.activity.activitylist.ActivityListService;
import com.hyjf.admin.manager.config.bankconfig.BankConfigController;
import com.hyjf.admin.manager.user.manageruser.ManageUsersService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.CustomErrors;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.CouponConfig;
import com.hyjf.mybatis.model.auto.CouponUser;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.ActivityListCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponRecoverCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponTenderDetailCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponUserCustomize;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 优惠券用户列表
 * 
 * @author
 *
 */
@Controller
@RequestMapping(value = CouponUserDefine.REQUEST_MAPPING)
public class CouponUserController extends BaseController {

    @Autowired
    private CouponUserService couponUserService;
    @Autowired
    private CouponConfigService couponConfigService;
    @Autowired
    private CouponTenderHztService couponTenderService;
    @Autowired
    private ActivityListService activityListService;
    @Resource
    private ManageUsersService usersService;
    
	/**
	 * 画面初始化
	 * @param request
	 * @param form
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(CouponUserDefine.INIT)
	@RequiresPermissions(CouponUserDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(CouponUserDefine.FORM) CouponUserBean form) throws UnsupportedEncodingException {
		LogUtil.startLog(CouponUserController.class.toString(), CouponUserDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(CouponUserDefine.LIST_PATH);
		// 创建分页
		form.setTimeStartAddSrch(GetDate.date2Str(GetDate.getTodayBeforeOrAfter(-30), new SimpleDateFormat("yyyy-MM-dd")));
        form.setTimeEndAddSrch(GetDate.date2Str(new Date(), new SimpleDateFormat("yyyy-MM-dd")));
		createPage(request, modelAndView, form);
		LogUtil.endLog(CouponUserController.class.toString(), CouponUserDefine.INIT);
		return modelAndView;
	}
	
	/**
	 * 
	 * 检索
	 * @author hsy
	 * @param request
	 * @param attr
	 * @param form
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(CouponUserDefine.SEARCH_ACTION)
    @RequiresPermissions(CouponUserDefine.PERMISSIONS_VIEW)
    public ModelAndView searchAction(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(CouponUserDefine.FORM) CouponUserBean form) throws UnsupportedEncodingException {
        LogUtil.startLog(CouponUserController.class.toString(), CouponUserDefine.SEARCH_ACTION);
        ModelAndView modelAndView = new ModelAndView(CouponUserDefine.LIST_PATH);
        // 创建分页
        createPage(request, modelAndView, form);
        LogUtil.endLog(CouponUserController.class.toString(), CouponUserDefine.SEARCH_ACTION);
        return modelAndView;
    }
	

	/**
	 * 分页机能
	 * @param request
	 * @param modelAndView
	 * @param form
	 * @throws UnsupportedEncodingException 
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, CouponUserBean form) throws UnsupportedEncodingException {
	    Map<String, Object> paraMap = new HashMap<String, Object>();
	    String couponFrom = form.getCouponFrom();
	    if(request.getMethod().equalsIgnoreCase("GET") && StringUtils.isNotEmpty(couponFrom)){
	        couponFrom = new String(couponFrom.getBytes("iso8859-1"),"utf-8");
	        form.setCouponFrom(couponFrom);
	    }
	    
	    paraMap.put("userId", form.getUserId());
	    paraMap.put("couponCode", form.getCouponCode());
	    paraMap.put("couponUserCode", form.getCouponUserCode());
	    paraMap.put("username", form.getUsername());
	    paraMap.put("couponType", form.getCouponType());
	    paraMap.put("usedFlag", form.getUsedFlag());
	    paraMap.put("couponFrom", couponFrom);
	    paraMap.put("couponSource", form.getCouponSource());
        if(StringUtils.isNotEmpty(form.getTimeStartAddSrch())){
            paraMap.put("timeStartAddSrch", GetDate.getDayStart10(form.getTimeStartAddSrch()));
        }
        if(StringUtils.isNotEmpty(form.getTimeEndAddSrch())){
            paraMap.put("timeEndAddSrch", GetDate.getDayEnd10(form.getTimeEndAddSrch()));
        }
        if(StringUtils.isNotEmpty(form.getTimeStartSrch())){
            paraMap.put("timeStartSrch", GetDate.getDayStart10(form.getTimeStartSrch()));
        }
        if(StringUtils.isNotEmpty(form.getTimeEndSrch())){
            paraMap.put("timeEndSrch", GetDate.getDayEnd10(form.getTimeEndSrch()));
        }
        
	    Integer count = couponUserService.countRecord(paraMap);
	    if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            paraMap.put("limitStart", paginator.getOffset());
            paraMap.put("limitEnd", paginator.getLimit());
            List<CouponUserCustomize> recordList  = couponUserService.getRecordList(paraMap);
            form.setPaginator(paginator);
            form.setRecordList(recordList);
        }
        modelAndView.addObject(CouponUserDefine.FORM, form);
	}
	
	/**
	 * 迁移到删除画面
	 */
	@RequestMapping(CouponUserDefine.DELETEVIEW_ACTION)
	@RequiresPermissions(CouponUserDefine.PERMISSIONS_DELETE)
    public ModelAndView deleteViewAction(HttpServletRequest request, RedirectAttributes attr, CouponUserBean form) {
        LogUtil.startLog(CouponUserController.class.toString(), CouponUserDefine.DELETEVIEW_ACTION);
        ModelAndView modelAndView = new ModelAndView(CouponUserDefine.DELETE_PATH);
        modelAndView.addObject("id", form.getId());
        LogUtil.endLog(CouponUserController.class.toString(), CouponUserDefine.DELETEVIEW_ACTION);
        return modelAndView;
    }
	/**
	 * 
	 * 删除一条优惠券
	 * @author hsy
	 * @param request
	 * @param attr
	 * @param form
	 * @return
	 */
    @RequestMapping(CouponUserDefine.DELETE_ACTION)
    @RequiresPermissions(CouponUserDefine.PERMISSIONS_DELETE)
    public ModelAndView deleteAction(HttpServletRequest request, RedirectAttributes attr, CouponUserBean form) {
        LogUtil.startLog(CouponUserController.class.toString(), CouponUserDefine.DELETE_ACTION);
        ModelAndView modelAndView = new ModelAndView(CouponUserDefine.DELETE_PATH);
        
        ValidatorFieldCheckUtil.validateRequired(modelAndView, "id", ""+form.getId());
        if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
            modelAndView.addObject(CouponUserDefine.FORM, form);
            return modelAndView;
        }
        couponUserService.deleteRecord(form.getId(), form.getContent());
        modelAndView.addObject(CouponUserDefine.SUCCESS, CouponUserDefine.SUCCESS);
        LogUtil.endLog(CouponUserController.class.toString(), CouponUserDefine.DELETE_ACTION);
        return modelAndView;
    }
    
    /**
     * 画面迁移到手动发布页面
     */
    @Token(save = true)
    @RequestMapping(CouponUserDefine.DISTRIBUTEVIEW_ACTION)
    @RequiresPermissions(CouponUserDefine.PERMISSIONS_ADD)
    public ModelAndView distributeViewAction(HttpServletRequest request, RedirectAttributes attr, CouponUserBean form) {
        LogUtil.startLog(CouponUserController.class.toString(), CouponUserDefine.DISTRIBUTEVIEW_ACTION);
        ModelAndView modelAndView = new ModelAndView(CouponUserDefine.DISTRIBUTE_PATH);
        //加载优惠券配置列表
        CouponConfigCustomize couponConfigCustomize = new CouponConfigCustomize();
        couponConfigCustomize.setLimitStart(-1);
        couponConfigCustomize.setLimitEnd(-1);
        couponConfigCustomize.setStatus(CustomConstants.COUPON_STATUS_PUBLISHED);
        List<CouponConfigCustomize> recordConfigList  = couponConfigService.getRecordList(couponConfigCustomize);
        //加载有效的活动列表
        List<ActivityListCustomize> recordList = activityListService.selectRecordListValid(new ActivityListBean(), -1, -1);
        
        modelAndView.addObject(CouponUserDefine.CONFIG_FORM, recordConfigList);
        modelAndView.addObject(CouponUserDefine.ACTIVELIST_FORM, recordList);
        LogUtil.endLog(CouponUserController.class.toString(), CouponUserDefine.DISTRIBUTEVIEW_ACTION);
        return modelAndView;
    }
    
    /**
     * 画面迁移到批量手动发布页面
     */
    @Token(save = true)
    @RequestMapping(CouponUserDefine.IMP_DISTRIBUTEVIEW_ACTION)
    @RequiresPermissions(CouponUserDefine.PERMISSIONS_IMPORT)
    public ModelAndView impDistributeViewAction(HttpServletRequest request, RedirectAttributes attr, CouponUserBean form) {
        LogUtil.startLog(CouponUserController.class.toString(), CouponUserDefine.IMP_DISTRIBUTEVIEW_ACTION);
        ModelAndView modelAndView = new ModelAndView(CouponUserDefine.IMP_DISTRIBUTE_PATH);
        
        LogUtil.endLog(CouponUserController.class.toString(), CouponUserDefine.IMP_DISTRIBUTEVIEW_ACTION);
        return modelAndView;
    }
    
    /**
     * 发布一条优惠券给用户
     */
    @Token(check = true, forward = CouponUserDefine.TOKEN_INIT_PATH)
    @RequestMapping(value = CouponUserDefine.DISTRIBUTE_ACTION, method = RequestMethod.POST)
    @RequiresPermissions(CouponUserDefine.PERMISSIONS_ADD)
    public ModelAndView distributeAction(HttpServletRequest request, RedirectAttributes attr, CouponUserBean form) {
        LogUtil.startLog(CouponUserController.class.toString(), CouponUserDefine.DISTRIBUTE_ACTION);
        ModelAndView modelAndView = new ModelAndView(CouponUserDefine.DISTRIBUTE_PATH);
        //校验请求参数
        this.validatorFieldCheck(modelAndView, form);
        if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
            modelAndView.addObject(CouponUserDefine.FORM, form);
            return modelAndView;
        }
        
        //校验数量
        if(form.getAmount() == null || form.getAmount() < 0){
        	form.setAmount(1);
        }
        
        for(int i=0; i< form.getAmount(); i++){
        	couponUserService.insertRecord(form);
        }
        modelAndView.addObject(CouponUserDefine.SUCCESS, CouponUserDefine.SUCCESS);
        LogUtil.endLog(CouponUserController.class.toString(), CouponUserDefine.DISTRIBUTE_ACTION);
        return modelAndView;
    }
    
    /**
     * 用户有效性校验
     * @author hsy
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = CouponUserDefine.CHECK_USER_ACTION, method = RequestMethod.POST)
    public String checkUserAction(HttpServletRequest request) {
        LogUtil.startLog(this.getClass().getName(), CouponUserDefine.CHECK_USER_ACTION);
        JSONObject ret = new JSONObject();

        //String name = request.getParameter("name");
        String param = request.getParameter("param");
        if(StringUtils.isEmpty(param)){
            String message = "用户名不能为空";
            ret.put(CouponUserDefine.JSON_VALID_INFO_KEY, message);
            return ret.toString();
        }

        com.hyjf.mybatis.model.auto.Users user = usersService.getUsersByUserName(param);
        if (user == null) {
            String message = "用户名不存在";
            ret.put(CouponUserDefine.JSON_VALID_INFO_KEY, message);
            return ret.toString();
        } else if (user.getStatus() != null && user.getStatus() == 1) {
            String message = "用户已锁定";
            ret.put(CouponUserDefine.JSON_VALID_INFO_KEY, message);
            return ret.toString();
        }
        
        // 没有错误时,返回y
        if (!ret.containsKey(CouponUserDefine.JSON_VALID_INFO_KEY)) {
            ret.put(CouponUserDefine.JSON_VALID_STATUS_KEY, CouponUserDefine.JSON_VALID_STATUS_OK);
        }
        LogUtil.endLog(this.getClass().getName(), CouponUserDefine.CHECK_USER_ACTION);
        return ret.toString();
    }


    /**
     * 
     * 校验优惠券配置是否可用
     * @author hsy
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = CouponUserDefine.CHECK_COUPON_VALID_ACTION,produces="text/html;charset=UTF-8")
    public String checkCouponValid(HttpServletRequest request) {
        String couponCode = request.getParameter("couponCode");
        String amount = request.getParameter("amount");
        if(StringUtils.isEmpty(couponCode)){
            return "请求参数不正确";
        }
        
        boolean result = couponConfigService.checkSendNum(couponCode,amount);
        
        if(result){
            return "success";
        }else{
            return "优惠券发行数量已用完";
        }
    }

        
    /**
     * 加载优惠券配置信息
     */
    @ResponseBody
    @RequestMapping(value = CouponUserDefine.LOAD_COUPONCONFIG_ACTION,produces="text/html;charset=UTF-8")
    public String loadCouponConfig(HttpServletRequest request, RedirectAttributes attr, CouponUserBean form) {
        LogUtil.startLog(CouponUserController.class.toString(), CouponUserDefine.LOAD_COUPONCONFIG_ACTION);
        if(StringUtils.isEmpty(form.getCouponCode())){
            return StringUtils.EMPTY;
        }
        CouponConfig config = couponUserService.selectConfigByCode(form.getCouponCode());
        
        //操作平台
        List<ParamName> clients=this.couponConfigService.getParamNameList("CLIENT");
        //modelAndView.addObject("clients", clients);
        //modelAndView.addObject("projectTypes",projectTypes);
        //被选中操作平台
        String clientString = "";

        //被选中操作平台
        String clientSed[] = StringUtils.split(config.getCouponSystem(), ",");
        for(int i=0 ; i< clientSed.length;i++){
            if("-1".equals(clientSed[i])){
                clientString=clientString+"所有平台";
                break;
            }else{
                for (ParamName paramName : clients) {
                    if(clientSed[i].equals(paramName.getNameCd())){
                        if(i!=0&&clientString.length()!=0){
                            clientString=clientString+"/";
                        }
                        clientString=clientString+paramName.getName();
                        
                    }
                }
            }
        }
        config.setCouponSystem(clientString);
        
        
        //modelAndView.addObject("selectedClientList", selectedClientList);
        //被选中项目类型   新逻辑 pcc20160715
        String projectString = "";
        //被选中项目类型
        String projectSed[] = StringUtils.split(config.getProjectType(), ",");
        if(config.getProjectType().indexOf("-1")!=-1){
            projectString="所有散标/新手/智投项目";
          }else{
              projectString=projectString+"所有";
              for (String project : projectSed) {
                   if("1".equals(project)){
                       projectString=projectString+"散标/";
                   }  
                   if("2".equals(project)){
                       projectString=projectString+"";
                   } 
                   if("3".equals(project)){
                       projectString=projectString+"新手/";
                   } 
                   if("4".equals(project)){
                       projectString=projectString+"";
                   } 
                   if("5".equals(project)){
                       projectString=projectString+"";
                   }
                   if("6".equals(project)){
                       projectString=projectString+"智投/";
                   }
              }
              projectString = StringUtils.removeEnd(
                      projectString, "/");
              projectString=projectString+"项目";
       }
        config.setProjectType(projectString);
        
        String configString = JSONObject.toJSONString(config);
        LogUtil.endLog(CouponUserController.class.toString(), CouponUserDefine.LOAD_COUPONCONFIG_ACTION);
        return configString;
    }
    
    /**
     * 迁移到详情画面
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(CouponUserDefine.INFO_ACTION)
    public ModelAndView infoAction(HttpServletRequest request, RedirectAttributes attr,@ModelAttribute(CouponUserDefine.FORM) CouponUserBean form) {
        LogUtil.startLog(CouponUserController.class.toString(), CouponUserDefine.INFO_ACTION);
        ModelAndView modelAndView = new ModelAndView(CouponUserDefine.INFO_PATH);
        String couponUserId = form.getId()+"";
        Map<String,Object> paramMap = new HashMap<String,Object>();

        paramMap.put("couponUserId", couponUserId);
        CouponTenderDetailCustomize detail=new CouponTenderDetailCustomize();
        detail=couponTenderService.getCouponTenderDetailCustomize(paramMap);
        
        List<CouponRecoverCustomize> list=
                couponTenderService.getCouponRecoverCustomize(paramMap);
        //操作平台
        List<ParamName> clients=this.couponConfigService.getParamNameList("CLIENT");
        
        //被选中操作平台
        String clientString = "";
        String clientSed[] = StringUtils.split(detail.getCouponSystem(), ",");
        for(int i=0 ; i< clientSed.length;i++){
            if("-1".equals(clientSed[i])){
                clientString=clientString+"所有平台";
                break;
            }else{
                for (ParamName paramName : clients) {
                    if(clientSed[i].equals(paramName.getNameCd())){
                        if(i!=0&&clientString.length()!=0){
                            clientString=clientString+"/";
                        }
                        clientString=clientString+paramName.getName();
                        
                    }
                }
            }
        }
        detail.setCouponSystem(clientString);
        
        
        //被选中项目类型    新逻辑 pcc20160715
        String projectString = "";
        //被选中项目类型
        String projectSed[] = StringUtils.split(detail.getProjectType(), ",");
        if(detail.getProjectType().indexOf("-1")!=-1){
            projectString="所有散标/新手/智投项目";
          }else{
              projectString=projectString+"所有";
              for (String project : projectSed) {
                   if("1".equals(project)){
                       projectString=projectString+"散标/";
                   }  
                   if("2".equals(project)){
                       projectString=projectString+"";
                   } 
                   if("3".equals(project)){
                       projectString=projectString+"新手/";
                   } 
                   if("4".equals(project)){
                       projectString=projectString+"";
                   } 
                   if("5".equals(project)){
                       projectString=projectString+"";
                   } 
                   if("6".equals(project)){
                       projectString=projectString+"智投/";
                   } 
                   
              }
              projectString = StringUtils.removeEnd(
                      projectString, "/");
              projectString=projectString+"项目";
       }
        detail.setProjectType("适用"+projectString);
        
        modelAndView.addObject("detail",detail);
        
        modelAndView.addObject("couponRecoverlist",list);
        
        LogUtil.endLog(CouponUserController.class.toString(), CouponUserDefine.INFO_ACTION);
        return modelAndView;
    }    
    
    
    /**
     * 画面迁移(含有id更新，不含有id添加)
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(CouponUserDefine.AUDIT_INIT_ACTION)

    public ModelAndView auditInitAction(HttpServletRequest request, RedirectAttributes attr,@ModelAttribute(CouponUserDefine.FORM) CouponUserBean form) {
        LogUtil.startLog(CouponUserController.class.toString(), CouponUserDefine.AUDIT_INIT_ACTION);
        ModelAndView modelAndView = new ModelAndView(CouponUserDefine.AUDIT_INIT_PATH);
        String couponUserId = form.getId()+"";
        Map<String,Object> paramMap = new HashMap<String,Object>();
        // 优惠券发放编号
        // couponTenderDetailCustomize.setTenderId(couponId);
        paramMap.put("couponUserId", couponUserId);
        //userCouponTenderDetail
        CouponTenderDetailCustomize detail=new CouponTenderDetailCustomize();
        detail=couponTenderService.getCouponTenderDetailCustomize(paramMap);
        //操作平台
        List<ParamName> clients=this.couponConfigService.getParamNameList("CLIENT");
        //modelAndView.addObject("clients", clients);
        //被选中操作平台
        String clientString = "";

      //被选中操作平台
        String clientSed[] = StringUtils.split(detail.getCouponSystem(), ",");
        for(int i=0 ; i< clientSed.length;i++){
            if("-1".equals(clientSed[i])){
                clientString=clientString+"不限";
                break;
            }else{
                for (ParamName paramName : clients) {
                    if(clientSed[i].equals(paramName.getNameCd())){
                        if(i!=0&&clientString.length()!=0){
                            clientString=clientString+"/";
                        }
                        clientString=clientString+paramName.getName();
                        
                    }
                }
            }
        }
        detail.setCouponSystem(clientString);
        
        
        //被选中项目类型    新逻辑 pcc20160715
        String projectString = "";
        //被选中项目类型
        String projectSed[] = StringUtils.split(detail.getProjectType(), ",");
        if(detail.getProjectType().indexOf("-1")!=-1){
            projectString="所有散标/新手/智投项目";
          }else{
              projectString=projectString+"所有";
              for (String project : projectSed) {
                   if("1".equals(project)){
                       projectString=projectString+"散标/";
                   }  
                   if("2".equals(project)){
                       projectString=projectString+"";
                   } 
                   if("3".equals(project)){
                       projectString=projectString+"新手/";
                   } 
                   if("4".equals(project)){
                       projectString=projectString+"";
                   } 
                   if("5".equals(project)){
                       projectString=projectString+"";
                   }
                   if("6".equals(project)){
                       projectString=projectString+"智投/";
                   }
                   
              }
              projectString = StringUtils.removeEnd(
                      projectString, "/");
              projectString=projectString+"项目";
       }
        detail.setProjectType("适用"+projectString);
        
        CouponUser couponUser=couponUserService.selectCouponUserById(couponUserId);
        
//        modelAndView.addObject("selectedProjectList", selectedProjectList);
        modelAndView.addObject("detail",detail);
        modelAndView.addObject("couponUser",couponUser);
        modelAndView.addObject("id",couponUserId);
        LogUtil.endLog(CouponUserController.class.toString(), CouponUserDefine.AUDIT_INIT_ACTION);
        return modelAndView;
    }
    
    
    /**
     * 画面迁移(含有id更新，不含有id添加)
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(CouponUserDefine.AUDIT_ACTION)

    public ModelAndView auditAction(HttpServletRequest request, RedirectAttributes attr,@ModelAttribute(CouponUserDefine.FORM) CouponUserBean form) {
        LogUtil.startLog(CouponUserController.class.toString(), CouponUserDefine.AUDIT_ACTION);
        ModelAndView modelAndView = new ModelAndView(CouponUserDefine.AUDIT_INIT_PATH);
        
        CouponUser record=couponUserService.selectCouponUserById(form.getId()+"");
        
        if(record == null || StringUtils.isEmpty(record.getCouponCode())){
            modelAndView.addObject(CouponConfigDefine.FORM, form);
            return modelAndView;
        }
        
        CouponConfig config = couponUserService.selectConfigByCode(record.getCouponCode());
        if(config == null){
            modelAndView.addObject(CouponConfigDefine.FORM, form);
            return modelAndView;
        }
        
        this.validatorFieldCheckAudit(modelAndView, form, record);

        if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
            
            
            String couponUserId = form.getId()+"";
            Map<String,Object> paramMap = new HashMap<String,Object>();
            // 优惠券发放编号
            // couponTenderDetailCustomize.setTenderId(couponId);
            paramMap.put("couponUserId", couponUserId);
            //userCouponTenderDetail
            CouponTenderDetailCustomize detail=new CouponTenderDetailCustomize();
            detail=couponTenderService.getCouponTenderDetailCustomize(paramMap);
            //操作平台
            List<ParamName> clients=this.couponConfigService.getParamNameList("CLIENT");
            //modelAndView.addObject("clients", clients);
            //被选中操作平台
            String clientString = "";

          //被选中操作平台
            String clientSed[] = StringUtils.split(detail.getCouponSystem(), ",");
            for(int i=0 ; i< clientSed.length;i++){
                if("-1".equals(clientSed[i])){
                    clientString=clientString+"不限";
                    break;
                }else{
                    for (ParamName paramName : clients) {
                        if(clientSed[i].equals(paramName.getNameCd())){
                            if(i!=0&&clientString.length()!=0){
                                clientString=clientString+"/";
                            }
                            clientString=clientString+paramName.getName();
                            
                        }
                    }
                }
            }
            detail.setCouponSystem(clientString);
            
            
            //被选中项目类型    新逻辑 pcc20160715
            String projectString = "";
            //被选中项目类型
            String projectSed[] = StringUtils.split(detail.getProjectType(), ",");
            if(detail.getProjectType().indexOf("-1")!=-1){
                projectString="所有散标/新手/智投项目";
              }else{
                  projectString=projectString+"所有";
                  for (String project : projectSed) {
                       if("1".equals(project)){
                           projectString=projectString+"散标/";
                       }  
                       if("2".equals(project)){
                           projectString=projectString+"";
                       } 
                       if("3".equals(project)){
                           projectString=projectString+"新手/";
                       } 
                       if("4".equals(project)){
                           projectString=projectString+"";
                       } 
                       if("5".equals(project)){
                           projectString=projectString+"";
                       } 
                       if("6".equals(project)){
                           projectString=projectString+"智投/";
                       }
                       
                  }
                  projectString = StringUtils.removeEnd(
                          projectString, "/");
                  projectString=projectString+"项目";
           }
            detail.setProjectType("适用"+projectString);
            
            CouponUser couponUser=couponUserService.selectCouponUserById(couponUserId);
            
//            modelAndView.addObject("selectedProjectList", selectedProjectList);
            modelAndView.addObject("detail",detail);
            modelAndView.addObject("couponUser",couponUser);
            modelAndView.addObject("id",couponUserId);
            
            
            modelAndView.addObject(CouponConfigDefine.FORM, form);
            return modelAndView;
        } else {
            // 更新
            this.couponUserService.auditRecord(form, config, record.getUserId());
        }
        
        
        modelAndView.addObject(CouponUserDefine.SUCCESS, CouponUserDefine.SUCCESS);
        LogUtil.endLog(CouponUserController.class.toString(), CouponUserDefine.AUDIT_ACTION);
        return modelAndView;
    }
    
    
    /**
     * 画面校验
     * 
     * @param modelAndView
     * @param form
     */
    private void validatorFieldCheckAudit(ModelAndView modelAndView,
        CouponUserBean form, CouponUser record) {
        // 排他校验
        ValidatorFieldCheckUtil.validateSynOperation(modelAndView,
                "synOperation", form.getUpdateTime(), record.getUpdateTime());
        if(!PropUtils.getSystem("coupon.audit.pwd").equals(MD5.toMD5Code(form.getCouponAuditPwd()))){
            CustomErrors.add(modelAndView, "couponAuditPwd", "same", "审核口令错误");
        } 
        
    }
    
    /**
     * 导出EXCEL
     * @param request
     * @param form
     */
    @RequestMapping(CouponUserDefine.EXPORT_ACTION)
    @RequiresPermissions(CouponUserDefine.PERMISSIONS_EXPORT)
    public void exportAction(HttpServletRequest request, HttpServletResponse response, CouponUserBean form) throws Exception {
        LogUtil.startLog(CouponUserController.class.toString(), CouponUserDefine.EXPORT_ACTION);
        // 表格sheet名称
        String sheetName = "优惠券用户列表";
		
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("userId", form.getUserId());
        paraMap.put("couponCode", form.getCouponCode());
        paraMap.put("couponUserCode", form.getCouponUserCode());
        paraMap.put("username", form.getUsername());
        paraMap.put("couponType", form.getCouponType());
        paraMap.put("usedFlag", form.getUsedFlag());
        paraMap.put("couponSource", form.getCouponSource());
        if(StringUtils.isNotEmpty(form.getTimeStartAddSrch())){
            paraMap.put("timeStartAddSrch", GetDate.getDayStart10(form.getTimeStartAddSrch()));
        }
        if(StringUtils.isNotEmpty(form.getTimeEndAddSrch())){
            paraMap.put("timeEndAddSrch", GetDate.getDayEnd10(form.getTimeEndAddSrch()));
        }
        if(StringUtils.isNotEmpty(form.getTimeStartSrch())){
            paraMap.put("timeStartSrch", GetDate.getDayStart10(form.getTimeStartSrch()));
        }
        if(StringUtils.isNotEmpty(form.getTimeEndSrch())){
            paraMap.put("timeEndSrch", GetDate.getDayEnd10(form.getTimeEndSrch()));
        }

        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
        String[] titles = new String[] {"序号", "优惠券ID","优惠券类别编号","用户名","发券时属性","注册渠道", "优惠券类型","面值","出借限额","有效期","来源", "操作平台", "项目类型", "项目期限", "使用状态","获得时间" };
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        
        // 生成一个表格
        HSSFSheet sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, sheetName + "_第1页");

        // 如果未加时间条件则只返回空excel
        if(!(StringUtils.isNotBlank(form.getTimeStartAddSrch()) && StringUtils.isNotBlank(form.getTimeEndAddSrch())) || (StringUtils.isNotBlank(form.getTimeStartSrch()) && StringUtils.isNotBlank(form.getTimeEndSrch()))){
            // 导出
            ExportExcel.writeExcelFile(response, workbook, titles, fileName);
            return;
        }

        List<CouponUserCustomize> resultList  = couponUserService.getRecordList(paraMap);
        if (resultList != null && resultList.size() > 0) {
            
            //操作平台
            List<ParamName> clients=this.couponConfigService.getParamNameList("CLIENT");
            
            int sheetCount = 1;
            int rowNum = 0;

            for (int i = 0; i < resultList.size(); i++) {
                rowNum++;
                if (i != 0 && i % 60000 == 0) {
                    sheetCount++;
                    sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, (sheetName + "_第" + sheetCount + "页"));
                    rowNum = 1;
                }
                // 新建一行
                Row row = sheet.createRow(rowNum);
                // 循环数据
                for (int celLength = 0; celLength < titles.length; celLength++) {
                    CouponUserCustomize couponUser = resultList.get(i);

                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);
                    // 序号
                    if (celLength == 0) {
                        cell.setCellValue(i + 1);
                    }
                    else if (celLength == 1) {
                        cell.setCellValue(couponUser.getCouponUserCode());
                    }
                    else if (celLength == 2) {
                        cell.setCellValue(couponUser.getCouponCode());
                    }
                    else if (celLength == 3) {
                        cell.setCellValue(couponUser.getUsername());
                    }
                    else if (celLength == 4) {
                    	cell.setCellValue(couponUser.getAttribute() == null ? "" : couponUser.getAttribute().equals("0") ? "无主单" : couponUser.getAttribute().equals("1") ? "有主单" : couponUser.getAttribute().equals("2") ? "线下员工" : "线上员工");
                    }
                    else if(celLength == 5) {
                        cell.setCellValue(couponUser.getChannel());
                    }
                    else if (celLength == 6) {
                        cell.setCellValue(couponUser.getCouponType());
                    }
                    else if (celLength == 7) {
                        cell.setCellValue(couponUser.getCouponQuota());
                    }
                    else if (celLength == 8) {
                        cell.setCellValue(couponUser.getTenderQuota());
                    }
                    else if (celLength == 9) {
                        cell.setCellValue(couponUser.getEndTime());
                    }
                    else if (celLength == 10) {
                        cell.setCellValue(couponUser.getCouponSource());
                    }
                    else if (celLength == 11) {
                        //被选中操作平台
                        String clientString = "";
                        String clientSed[] = StringUtils.split(couponUser.getCouponSystem(), ",");
                        for(int k=0 ; k< clientSed.length;k++){
                            if("-1".equals(clientSed[k])){
                                clientString=clientString+"不限";
                                break;
                            }else{
                                for (ParamName paramName : clients) {
                                    if(clientSed[k].equals(paramName.getNameCd())){
                                        if(k!=0&&clientString.length()!=0){
                                            clientString=clientString+"/";
                                        }
                                        clientString=clientString+paramName.getName();
                                        
                                    }
                                }
                            }
                        }
                        cell.setCellValue(clientString);
                    }
                    else if (celLength == 12) {
                        //被选中项目类型    新逻辑 pcc20160715
                        String projectString = "";
                        //被选中项目类型
                        String projectSed[] = StringUtils.split(couponUser.getProjectType(), ",");
                        if(couponUser.getProjectType().indexOf("-1")!=-1){
                            projectString="所有散标/新手/智投项目";
                          }else{
                              projectString=projectString+"所有";
                              for (String project : projectSed) {
                                   if("1".equals(project)){
                                       projectString=projectString+"散标/";
                                   }  
                                   if("2".equals(project)){
                                       projectString=projectString+"";
                                   } 
                                   if("3".equals(project)){
                                       projectString=projectString+"新手/";
                                   } 
                                   if("4".equals(project)){
                                       projectString=projectString+"";
                                   } 
                                   if("5".equals(project)){
                                       projectString=projectString+"";
                                   } 
                                   if("6".equals(project)){
                                       projectString=projectString+"智投/";
                                   } 
                                   
                              }
                              projectString = StringUtils.removeEnd(
                                      projectString, "/");
                              projectString=projectString+"项目";
                       }
                        cell.setCellValue("适用"+projectString);
                    }
                    else if (celLength == 13) {
                        cell.setCellValue(couponUser.getProjectExpirationType());
                    }
                    else if (celLength == 14) {
                        cell.setCellValue(couponUser.getUsedFlagView());
                    }
                    else if (celLength == 15) {
                        cell.setCellValue(couponUser.getAddTime());
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);

        LogUtil.endLog(CouponConfigController.class.toString(), CouponConfigDefine.EXPORT_ACTION);
    }

    /**
     * 画面校验
     * 
     * @param modelAndView
     * @param form
     */
    private void validatorFieldCheck(ModelAndView modelAndView, CouponUserBean form) {
        // 优惠券编码
        ValidatorFieldCheckUtil.validateRequired(modelAndView, "couponCode", form.getCouponCode());
        // 用户名
        ValidatorFieldCheckUtil.validateRequired(modelAndView, "username", form.getUsername());
    }

	/**
	 * 手动批量发券上传
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = CouponUserDefine.UPLOAD_ACTION, method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(CouponUserDefine.PERMISSIONS_IMPORT)
	public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogUtil.startLog(BankConfigController.class.toString(), CouponUserDefine.UPLOAD_ACTION);
		String files = couponUserService.uploadFile(request, response);
		LogUtil.endLog(BankConfigController.class.toString(), CouponUserDefine.UPLOAD_ACTION);
		return files;
	}
}
