package com.hyjf.admin.manager.activity.activitylist;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.maintenance.config.ConfigDefine;
import com.hyjf.admin.maintenance.paramname.ParamNameService;
import com.hyjf.admin.manager.config.bankconfig.BankConfigDefine;
import com.hyjf.admin.manager.content.article.ContentArticleController;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.GetterUtil;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.ActivityF1;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.UserInfoCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;

/**
 * 活动列表页
 * 
 * @author qingbing
 *
 */
@Controller
@RequestMapping(value = ActivityListDefine.REQUEST_MAPPING)
public class ActivityListController extends BaseController {

    @Autowired
    private ActivityListService activityListService;

    @Autowired
    private ParamNameService paramNameService;

    /**
     * 类名
     */
    private static final String THIS_CLASS = ActivityListController.class.getName();

    /**
     * 活动列表维护画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(ActivityListDefine.INIT)
    @RequiresPermissions(ActivityListDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,
        @ModelAttribute(ActivityListDefine.ACTIVITYLIST_FORM) ActivityListBean form) {
        LogUtil.startLog(THIS_CLASS, ActivityListDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(ActivityListDefine.LIST_PATH);

        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(THIS_CLASS, ActivityListDefine.INIT);
        return modelAndView;
    }

    /**
     * 活动列表维护分页机能 页面初始化
     * 
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, ActivityListBean form) {
    	//六月份运营部活动ID
//    	String activityId = PropUtils.getSystem(CustomConstants.ACTIVITY_ID_SIX);
//    	modelAndView.addObject("activityId", activityId);
    	
        List<ActivityList> recordList = this.activityListService.getRecordList(new ActivityList(), -1, -1);
        if (recordList != null) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
            recordList =
                    this.activityListService.getRecordList(new ActivityList(), paginator.getOffset(),
                            paginator.getLimit());
            List<ActivityListBean> forBack = forBack(recordList);
            form.setPaginator(paginator);
            form.setForBack(forBack);
            modelAndView.addObject(ActivityListDefine.ACTIVITYLIST_FORM, form);
            String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
            modelAndView.addObject("fileDomainUrl", fileDomainUrl);
        }
    }

    /**
     * 画面迁移(含有id更新，不含有id添加)
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(ActivityListDefine.INFO_ACTION)
    @RequiresPermissions(value = { ActivityListDefine.PERMISSIONS_INFO, ActivityListDefine.PERMISSIONS_ADD,
            ActivityListDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
    public ModelAndView info(HttpServletRequest request, RedirectAttributes attr,
        @ModelAttribute(ActivityListDefine.ACTIVITYLIST_FORM) ActivityListBean form) {
        LogUtil.startLog(ContentArticleController.class.toString(), ActivityListDefine.INFO_ACTION);
        ModelAndView modelAndView = new ModelAndView(ActivityListDefine.INFO_PATH);
        // 操作复选框
        modelAndView.addObject("startTime", GetDate.formatDate(new Date()));
        modelAndView.addObject("endTime", GetDate.formatDate(new Date()));
        // 组装platformList
        List<ParamName> paramNames = paramNameService.getParamNameList("CLIENT");
        modelAndView.addObject("allPlatformList", paramNames);
        // 组装结束
        modelAndView.addObject("startTime", "");
        modelAndView.addObject("endTime", "");
        if (StringUtils.isNotEmpty(form.getIds())) {
            Integer id = Integer.valueOf(form.getIds());
            ActivityList record = this.activityListService.getRecord(id);
            modelAndView.addObject(ActivityListDefine.ACTIVITYLIST_FORM, record);
            // 拆分平台
            String[] split = record.getPlatform().split(",");
            modelAndView.addObject("platformList", split);
            // 封装回显时间格式化
            modelAndView.addObject("startTime", GetDate.getDateTimeMyTime(record.getTimeStart()));
            modelAndView.addObject("endTime", GetDate.getDateTimeMyTime(record.getTimeEnd()));
            String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
            modelAndView.addObject("fileDomainUrl", fileDomainUrl);
        }
        LogUtil.endLog(ContentArticleController.class.toString(), ActivityListDefine.INFO_ACTION);
        return modelAndView;
    }

    /**
     * 根据条件查询所需要数据
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(ActivityListDefine.SEARCH_ACTION)
    @RequiresPermissions(ActivityListDefine.PERMISSIONS_SEARCH)
    public ModelAndView selectActivityList(HttpServletRequest request, RedirectAttributes attr,
        @ModelAttribute(ActivityListDefine.ACTIVITYLIST_FORM) ActivityListBean form) {
        LogUtil.startLog(THIS_CLASS, ActivityListDefine.INFO_ACTION);
        ModelAndView modelAndView = new ModelAndView(ActivityListDefine.LIST_PATH);
        // 参数校验
        // if (StringUtils.isNotEmpty(form.getStartTime())
        // && StringUtils.isNotEmpty(form.getEndTime())
        // &&
        // Integer.valueOf(GetDate.strYYYYMMDDHHMMSS2Timestamp2(form.getStartTime()))
        // > Integer.valueOf(GetDate
        // .strYYYYMMDDHHMMSS2Timestamp2(form.getEndTime()))) {
        // return modelAndView.addObject("errorMsg", "活动开始日期不能小于结束日期");
        // }
        // if (StringUtils.isNotEmpty(form.getStartCreate())
        // && StringUtils.isNotEmpty(form.getEndCreate())
        // &&
        // Integer.valueOf(GetDate.strYYYYMMDDHHMMSS2Timestamp2(form.getStartCreate()))
        // > Integer.valueOf(GetDate
        // .strYYYYMMDDHHMMSS2Timestamp2(form.getEndCreate()))) {
        // return modelAndView.addObject("errorMsg", "活动创建开始日期不能小于结束日期");
        // }
        // 创建分页
        this.createPageBy(request, modelAndView, form);
        LogUtil.endLog(THIS_CLASS, ActivityListDefine.INFO_ACTION);
        return modelAndView;
    }

    /**
     * 活动列表维护分页机能 根据条件
     * 
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPageBy(HttpServletRequest request, ModelAndView modelAndView, ActivityListBean form) {
        List<ActivityList> recordList = this.activityListService.selectRecordList(form, -1, -1);
        if (recordList != null) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
            recordList = this.activityListService.selectRecordList(form, paginator.getOffset(), paginator.getLimit());
            List<ActivityListBean> forBack = forBack(recordList);
            form.setPaginator(paginator);
            form.setForBack(forBack);
            modelAndView.addObject(ActivityListDefine.ACTIVITYLIST_FORM, form);
            String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
            modelAndView.addObject("fileDomainUrl", fileDomainUrl);
        }
    }

    /**
     * 添加活动信息
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(value = ActivityListDefine.INSERT_ACTION, method = RequestMethod.POST)
    @RequiresPermissions(ActivityListDefine.PERMISSIONS_ADD)
    public ModelAndView insertAction(HttpServletRequest request, ActivityList form,
        @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime) {
        LogUtil.startLog(THIS_CLASS, ActivityListDefine.INSERT_ACTION);
        ModelAndView modelAndView = new ModelAndView(ActivityListDefine.INFO_PATH);
        // string转int操作
        form.setTimeStart(Integer.valueOf(GetDate.strYYYYMMDDHHMMSS2Timestamp2(startTime)));
        form.setTimeEnd(Integer.valueOf(GetDate.strYYYYMMDDHHMMSS2Timestamp2(endTime)));
        // 调用校验
        if (validatorFieldCheck(modelAndView, form) != null) {
            // 失败返回
            return validatorFieldCheck(modelAndView, form);
        }
        // 数据插入
        this.activityListService.insertRecord(form);
        // 跳转页面用（info里面有）
        modelAndView.addObject(ActivityListDefine.SUCCESS, ActivityListDefine.SUCCESS);
        LogUtil.endLog(THIS_CLASS, ActivityListDefine.INSERT_ACTION);
        return modelAndView;
    }

    /**
     * 修改活动维护信息
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(value = ActivityListDefine.UPDATE_ACTION, method = RequestMethod.POST)
    @RequiresPermissions(ActivityListDefine.PERMISSIONS_MODIFY)
    public ModelAndView updateAction(HttpServletRequest request, ActivityList form,
        @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime) {
        LogUtil.startLog(THIS_CLASS, ActivityListDefine.UPDATE_ACTION);
        ModelAndView modelAndView = new ModelAndView(ActivityListDefine.INFO_PATH);
        // 根据id更新
        if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "id", form.getId().toString())) {
            return modelAndView;
        }
        // string转int操作
        form.setTimeStart(Integer.valueOf(GetDate.strYYYYMMDDHHMMSS2Timestamp2(startTime)));
        form.setTimeEnd(Integer.valueOf(GetDate.strYYYYMMDDHHMMSS2Timestamp2(endTime)));
        // 调用校验
        if (validatorFieldCheck(modelAndView, form) != null) {
            // 失败返回
            return validatorFieldCheck(modelAndView, form);
        }
        // 更新
        this.activityListService.updateRecord(form);
        // 跳转页面用（info里面有）
        modelAndView.addObject(ActivityListDefine.SUCCESS, ActivityListDefine.SUCCESS);
        LogUtil.endLog(THIS_CLASS, ActivityListDefine.UPDATE_ACTION);
        return modelAndView;
    }

    /**
     * 删除配置信息
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(ConfigDefine.DELETE_ACTION)
    @RequiresPermissions(ActivityListDefine.PERMISSIONS_DELETE)
    public ModelAndView deleteRecordAction(HttpServletRequest request, String ids) {
        LogUtil.startLog(THIS_CLASS, ActivityListDefine.DELETE_ACTION);

        ModelAndView modelAndView = new ModelAndView(ActivityListDefine.RE_LIST_PATH);
        // 解析json字符串
        List<Integer> recordList = JSONArray.parseArray(ids, Integer.class);
        this.activityListService.deleteRecord(recordList);
        LogUtil.endLog(THIS_CLASS, ActivityListDefine.DELETE_ACTION);
        return modelAndView;
    }

    /**
     * 调用校验表单方法
     * 
     * @param modelAndView
     * @param form
     * @return
     */
    private ModelAndView validatorFieldCheck(ModelAndView modelAndView, ActivityList form) {
        // 字段校验(非空判断和长度判断)
        if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "title", form.getTitle())) {
            return modelAndView;
        }
        if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "qr", form.getQr())) {
            form.setQr("");
        }
        if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "title", form.getTitle(), 30, true)) {
            return modelAndView;
        }
        if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "description", form.getDescription(), 200, false)) {
            return modelAndView;
        }
        if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "timeStart", form.getTimeStart().toString())) {
            return modelAndView;
        }
        if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "timeEnd", form.getTimeEnd().toString())) {
            return modelAndView;
        }
        if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "platform", form.getPlatform())) {
            form.setPlatform("");
        }
        return null;
    }

    /**
     * 调用封装回显
     * 
     * @param recordList
     * @return
     */
    private List<ActivityListBean> forBack(List<ActivityList> recordList) {
        List<ActivityListBean> forBack = new ArrayList<ActivityListBean>();

        for (ActivityList activityList : recordList) {
            ActivityListBean bean = new ActivityListBean();
            BeanUtils.copyProperties(activityList, bean);
            bean.setId(activityList.getId());
            bean.setTitle(activityList.getTitle());
            bean.setUrlForeground(activityList.getUrlForeground());
            List<ParamName> paramNames = paramNameService.getParamNameList("CLIENT");
            String platform = activityList.getPlatform();
            for (ParamName param : paramNames) {
                platform = platform.replace(param.getNameCd(), param.getName());
            }
            bean.setPlatform(platform);
            bean.setStartTime(GetDate.getDateTimeMyTime(activityList.getTimeStart()));
            bean.setEndTime(GetDate.getDateTimeMyTime(activityList.getTimeEnd()));
            if (activityList.getTimeStart() >= GetDate.getNowTime10()) {
                bean.setStatus("未开始");
            }
            if (activityList.getTimeEnd() <= GetDate.getNowTime10()) {
                bean.setStatus("已完成");
            }
            if (activityList.getTimeEnd() >= GetDate.getNowTime10()
                    && activityList.getTimeStart() <= GetDate.getNowTime10()) {
                bean.setStatus("进行中");
            }
            bean.setStartCreate(GetDate.getDateTimeMyTime(activityList.getCreateTime()));
            if("汇盈F1大师赛".equals(activityList.getTitle())){
                bean.setIsMayActivity("1");
            }
                
            forBack.add(bean);
        }

        return forBack;
    }

    /**
     * 资料上传
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = BankConfigDefine.UPLOAD_FILE, method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = { ActivityListDefine.PERMISSIONS_ADD, ActivityListDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
    public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LogUtil.startLog(THIS_CLASS, ActivityListDefine.UPLOAD_FILE);
        String files = activityListService.uploadFile(request, response);
        LogUtil.endLog(THIS_CLASS, ActivityListDefine.UPLOAD_FILE);
        return files;
    }

    /**
     * 
     * 获取活动详情列表
     * @author liuyang
     * @param request
     * @param searchForm
     * @return
     */

    @RequestMapping(ActivityListDefine.ACTIVITY_INFO_ACTION)
    @RequiresPermissions(ActivityListDefine.PERMISSIONS_INFO)
    public ModelAndView getActivityF1(HttpServletRequest request, ActivityF1Bean searchForm) {
        LogUtil.startLog(THIS_CLASS, ActivityListDefine.ACTIVITY_INFO);

        ModelAndView modelAndView = new ModelAndView(ActivityListDefine.ACTIVITY_INFO_LIST_PATH);

        // 创建分页
        this.createActivityF1Page(request, modelAndView, searchForm);

        LogUtil.endLog(THIS_CLASS, ActivityListDefine.ACTIVITY_INFO);
        return modelAndView;
    }

    /**
     * 
     * 返现操作
     * @author liuyang
     * @param request
     * @param response
     * @param form
     * @return
     */
    @ResponseBody
    @RequestMapping(value = ActivityListDefine.RETURNCASH_ACTION)
    @RequiresPermissions(ActivityListDefine.PERMISSIONS_RETURNCASH_RETURNCASH)
    public String returncashAction(HttpServletRequest request, HttpServletResponse response,
        @RequestBody ActivityF1Bean form) {

        LogUtil.startLog(THIS_CLASS, ActivityListDefine.RETURNCASH_ACTION);

        JSONObject ret = new JSONObject();
        // 用户ID
        Integer userId = GetterUtil.getInteger(form.getUserId());
        // 用户ID为空
        if (Validator.isNull(userId)) {
            ret.put(ActivityListDefine.JSON_STATUS_KEY, ActivityListDefine.JSON_STATUS_NG);
            ret.put(ActivityListDefine.JSON_RESULT_KEY, "返现发生错误,请重新操作!");
            LogUtil.errorLog(THIS_CLASS, ActivityListDefine.RETURNCASH_ACTION, new Exception("参数不正确[userId=" + userId
                    + "]"));
            return ret.toString();
        }

        // 取得用户信息
        ActivityF1 activityF1 = activityListService.selectActivityF1ByUserId(userId);

        // 用户信息不存在时,返回错误信息
        if (activityF1 == null) {
            ret.put(ActivityListDefine.JSON_STATUS_KEY, ActivityListDefine.JSON_STATUS_NG);
            ret.put(ActivityListDefine.JSON_RESULT_KEY, "返现发生错误,请重新操作!");
            LogUtil.errorLog(THIS_CLASS, ActivityListDefine.RETURNCASH_ACTION, new Exception("参数不正确[userId=" + userId
                    + "]"));
            return ret.toString();
        }
       
        /**redis 锁 */
//        if(StringUtils.isNotEmpty(RedisUtils.get("F1_RETURNCASH:"+ activityF1.getUserId()))){
//             ret.put(ActivityListDefine.JSON_STATUS_KEY, ActivityListDefine.JSON_STATUS_NG);
//             ret.put(ActivityListDefine.JSON_RESULT_KEY, "用户数据已发生变化,请刷新页面!");
//             return ret.toString();
//        }else{
//            RedisUtils.set("F1_RETURNCASH:"+ activityF1.getUserId(),activityF1.getReturnAmount().toString() ,5);
//        }
        
        boolean reslut = RedisUtils.tranactionSet("F1_RETURNCASH:"+ activityF1.getUserId() ,5);
		// 如果没有设置成功，说明有请求来设置过
		if(!reslut){
			ret.put(ActivityListDefine.JSON_STATUS_KEY, ActivityListDefine.JSON_STATUS_NG);
            ret.put(ActivityListDefine.JSON_RESULT_KEY, "用户数据已发生变化,请刷新页面!");
            return ret.toString();
		}
        
        // 根据用户id查询用户部门信息
        UserInfoCustomize userInfoCustomize = activityListService.queryUserInfoByUserId(activityF1.getUserId());

        // 取得用户在汇付天下的账户信息
        AccountChinapnr accountChinapnr = activityListService.getChinapnrUserInfo(userId);
        // 用户未开户时,返回错误信息
        if (accountChinapnr == null) {
            ret.put(ActivityListDefine.JSON_STATUS_KEY, ActivityListDefine.JSON_STATUS_NG);
            ret.put(ActivityListDefine.JSON_RESULT_KEY, "返现发生错误,请重新操作!");
            LogUtil.errorLog(THIS_CLASS, ActivityListDefine.RETURNCASH_ACTION, new Exception("参数不正确[userId=" + userId
                    + "]"));
            return ret.toString();
        }

        // IP地址
        String ip = CustomUtil.getIpAddr(request);

        String ordId = GetOrderIdUtils.getOrderId2(userId);
        // 调用汇付接口(4.3.11. 自动扣款转账（商户用）)
        ChinapnrBean bean = new ChinapnrBean();
        bean.setVersion(ChinaPnrConstant.VERSION_10);
        bean.setCmdId(ChinaPnrConstant.CMDID_TRANSFER); // 消息类型(必须)
        bean.setOrdId(ordId); // 订单号(必须)
        bean.setTransAmt(String.valueOf(form.getReturnAmount())); // 交易金额(必须)
        bean.setInCustId(accountChinapnr.getChinapnrUsrcustid().toString()); // 入账客户号(必须)
        bean.setInAcctId(""); // 入账子账户(可选)
        bean.setBgRetUrl(ChinapnrUtil.getBgRetUrl());

        // 写log用参数
        bean.setLogUserId(userId); // 操作者ID
        bean.setLogRemark("5月份活动返现"); // 备注
        bean.setLogClient("0"); // PC
        bean.setLogIp(ip); // IP地址

        // 取得用户信息
        ActivityF1 activityInfo = activityListService.selectActivityF1ByUserId(userId);

        // 用户信息不存在时,返回错误信息
        if ("1".equals(activityInfo.getIsReturnFlg())) {
            ret.put(ActivityListDefine.JSON_STATUS_KEY, ActivityListDefine.JSON_STATUS_NG);
            ret.put(ActivityListDefine.JSON_RESULT_KEY, "用户数据已发生变化,请刷新页面!");
            return ret.toString();
        }
       
        // 调用汇付接口
        ChinapnrBean chinaPnrBean = ChinapnrUtil.callApiBg(bean);

        if (chinaPnrBean == null) {
            LogUtil.errorLog(THIS_CLASS, ActivityListDefine.RETURNCASH_ACTION, new Exception("调用汇付接口发生错误"));
            ret.put(ActivityListDefine.JSON_STATUS_KEY, ActivityListDefine.JSON_STATUS_NG);
            ret.put(ActivityListDefine.JSON_RESULT_KEY, "返现发生错误,请重新操作!");
            return ret.toString();
        }

        int cnt = 0;
        // 接口返回正常时,执行更新操作
        if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(chinaPnrBean.getRespCode())) {
            // 设置是否返现flg
            activityF1.setIsReturnFlg("1");
            // 取得用户信息
            ActivityF1 activityF1Info = activityListService.selectActivityF1ByUserId(userId);
            if("1".equals(activityF1Info.getIsReturnFlg())){
            	 ret.put(ActivityListDefine.JSON_STATUS_KEY, ActivityListDefine.JSON_STATUS_NG);
                 ret.put(ActivityListDefine.JSON_RESULT_KEY, "用户数据已发生变化,请刷新页面!");
                 return ret.toString();
            }
            try {
                // 返现处理
                cnt = this.activityListService.insertReturncashRecord(activityF1, bean, userInfoCustomize);
            } catch (Exception e) {
                LogUtil.errorLog(THIS_CLASS, ActivityListDefine.RETURNCASH_ACTION, e);
            }
        }

        // 返现成功
        if (cnt > 0) {
            ret.put(ActivityListDefine.JSON_STATUS_KEY, ActivityListDefine.JSON_STATUS_OK);
            ret.put(ActivityListDefine.JSON_RESULT_KEY, "返现操作成功!");
        } else if(cnt == -1){
             ret.put(ActivityListDefine.JSON_STATUS_KEY, ActivityListDefine.JSON_STATUS_NG);
             ret.put(ActivityListDefine.JSON_RESULT_KEY, "用户数据已发生变化,请刷新页面!");
             return ret.toString();
        }else {
            ret.put(ActivityListDefine.JSON_STATUS_KEY, ActivityListDefine.JSON_STATUS_NG);
            ret.put(ActivityListDefine.JSON_RESULT_KEY, "返现操作发生错误,请联系技术人员处理!");
        }

        LogUtil.endLog(THIS_CLASS, ActivityListDefine.RETURNCASH_ACTION);
        return ret.toString();
    }

    /**
     * 活动列表维护分页机能 页面初始化
     * 
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createActivityF1Page(HttpServletRequest request, ModelAndView modelAndView, ActivityF1Bean form) {

        // 用户角色
        List<ParamName> userNames = this.activityListService.getParamNameList("USER_NAME");
        modelAndView.addObject("userName", userNames);
        // 用户属性
        List<ParamName> realNames = this.activityListService.getParamNameList("REAL_NAME");
        modelAndView.addObject("realName", realNames);
        // 开户状态
        List<ParamName> mobiles = this.activityListService.getParamNameList("MOBILE");
        modelAndView.addObject("mobile", mobiles);

        // 封装查询条件
        Map<String, Object> activityF1 = new HashMap<String, Object>();
        String userName = StringUtils.isNotEmpty(form.getUserName()) ? form.getUserName() : null;
        String realName = StringUtils.isNotEmpty(form.getRealName()) ? form.getRealName() : null;
        String mobile = StringUtils.isNotEmpty(form.getMobile()) ? form.getMobile() : null;

        activityF1.put("userName", userName);
        activityF1.put("realName", realName);
        activityF1.put("mobile", mobile);

        int count = this.activityListService.countActivityInfo(activityF1);
        if (count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            List<ActivityF1> recordList =
                    this.activityListService.selectActivityInfoList(activityF1, paginator.getOffset(),
                            paginator.getLimit());
            List<ActivityF1Bean> forBack = activityF1ForBack(recordList);
            form.setPaginator(paginator);
            form.setForBack(forBack);
            modelAndView.addObject(ActivityListDefine.ACTIVITY_INFO_FORM, form);
        }
    }

    /**
     * 
     * 此处为方法说明
     * @author liuyang
     * @param recordList
     * @return
     */
    public List<ActivityF1Bean> activityF1ForBack(List<ActivityF1> recordList) {
        List<ActivityF1Bean> forBack = new ArrayList<ActivityF1Bean>();
        Date systemNowDate = new Date();
        Long systemNowDateLong = systemNowDate.getTime() / 1000;
        Integer time = Integer.valueOf(String.valueOf(systemNowDateLong));
        Date endDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            endDate = format.parse("2016-05-31 23:59:59");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Long endDateLong = endDate.getTime() / 1000;
        Integer endTime = Integer.valueOf(String.valueOf(endDateLong));
        for (ActivityF1 activityF1 : recordList) {
            ActivityF1Bean activityF1Bean = new ActivityF1Bean();
            BeanUtils.copyProperties(activityF1, activityF1Bean);
            activityF1Bean.setUserId(activityF1.getUserId());
            // 使用APP出借
            if ("1".equals(activityF1.getIsAppFlg())) {
                activityF1Bean.setIsAppFlg("是");
            } else {
                activityF1Bean.setIsAppFlg("否");
            }

            // 当前时间>20160531
            if (time > endTime) {
                activityF1Bean.setIsover("1");
            } else {
                activityF1Bean.setIsover("0");
            }
            // 根据用户id查询用户部门信息
            UserInfoCustomize userInfoCustomize = activityListService.queryUserInfoByUserId(activityF1.getUserId());
            // 一级部门
            activityF1Bean
                    .setFirst_level_department(userInfoCustomize == null ? "" : userInfoCustomize.getRegionName());
            // 二级部门
            activityF1Bean.setSecond_level_department(userInfoCustomize == null ? "" : userInfoCustomize
                    .getBranchName());
            // 三级部门
            activityF1Bean.setThird_level_department(userInfoCustomize == null ? "" : userInfoCustomize
                    .getDepartmentName());

            // 首投是否>5000
            if ("1".equals(activityF1.getIsFirstFlg())) {
                activityF1Bean.setIsFirstFlg("是");
            } else {
                activityF1Bean.setIsFirstFlg("否");
            }
            forBack.add(activityF1Bean);
        }
        return forBack;
    }
}
