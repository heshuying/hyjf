package com.hyjf.admin.message.log;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.customize.SmsLogCustomize;
import com.hyjf.mybatis.model.customize.SmsOntimeCustomize;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * @author GOGTZ-Z
 * @version V1.0  
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @date 2015/07/09 17:00
 */
@Controller
@RequestMapping(value = SmsLogDefine.MESSAGE_LIST)
public class SmsLogController extends BaseController {

    @Autowired
    private SmsLogService logService;

    /**
     * 分页
     *
     * @param request
     * @param modeAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modeAndView, SmsLogBean form) {
        SmsLogCustomize smlogCustomize = new SmsLogCustomize();
        if (StringUtils.isNotEmpty(form.getMobile())) {
            smlogCustomize.setMobile(form.getMobile());
        }
        if (StringUtils.isNotEmpty(form.getType())) {
            smlogCustomize.setType(form.getType());
        }
        if (StringUtils.isNotEmpty(form.getPost_time_begin())) {
            int begin = GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayStart(form.getPost_time_begin()));
            smlogCustomize.setPost_time_begin(begin);
        }
        if (StringUtils.isNotEmpty(form.getPost_time_end())) {
            int end = GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayEnd(form.getPost_time_end()));
            smlogCustomize.setPost_time_end(end);
        }
        if (form.getStatus() != null) {
            smlogCustomize.setStatus(form.getStatus());
        } else {
            smlogCustomize.setStatus(2);
        }
        Integer count = this.logService.queryLogCount(smlogCustomize);
        if (count >= 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            smlogCustomize.setLimitStart(paginator.getOffset());
            smlogCustomize.setLimitEnd(paginator.getLimit());

            List<String> types = Arrays.asList("提现成功", "还款成功", "投标成功", "充值成功", "平台转账-收到推广提成", "平台转账-收到活动奖励", "平台转账-收到手续费返现", "资金相关-充值成功", "资金相关-提现成功", "债转部分转让成功", "债转全部转让成功", "优惠券收益到账", "优惠券出借成功", "红包短信提醒", "智投订单进入服务回报期限", "智投订单已退出", "加息还款", "计划已还款", "优惠券还款成功");
            List<String> types2 = Arrays.asList("资金相关-投资成功", "提现失败", "分期出借成功", "加息放款", "资金相关-收到还款", "收到借款", "计划自动投标完成", "资金相关收到还款", "资金相关-出借成功");
            List<String> types3 = Arrays.asList("用户注册验证码", "找回密码验证码", "绑定手机验证码", "解绑手机验证码", "注册", "找回密码", "更换手机号-验证原手机号", "更换手机号-绑定新手机号", "一键注册", "定向转账", "静默注册密码", "第三方用户注册密码发送", "微官网注册", "微官网找回密码");

            List<SmsLogCustomize> logs = this.logService.queryLog(smlogCustomize);
            if (logs != null && logs.size() != 0) {
                //Integer sumContentSize = this.logService.sumContent(smlogCustomize);
                for (SmsLogCustomize sm : logs) {
                    if (sm.getIsDisplay() == 1) {
                        sm.setAllContent("********************");
                    } else {
                        if (types.contains(sm.getType())) {
                            String regex = "[0-9]{1,14}\\.{0,1}[0-9]{0,2}";
                            sm.setAllContent(sm.getContent().replaceAll(regex, "********"));
                        } else if (types2.contains(sm.getType())) {
                            String regex = "(([1-9]{1}\\d*)|(0{1}))(\\.\\d{2})";
                            sm.setAllContent(sm.getContent().replaceAll(regex, "********"));
                        } else if (types3.contains(sm.getType())) {
                            String regex = "[0-9a-zA-Z]{6,16}";
                            sm.setAllContent(sm.getContent().replaceAll(regex, "********"));
                        } else if ("新增管理员密码提醒".equals(sm.getType())) {
                            StringBuffer content = new StringBuffer(sm.getContent());
                            StringBuffer s = content.replace(sm.getContent().indexOf("为", 21) + 1, sm.getContent().indexOf("，", 21), "********");
                            sm.setAllContent(content.replace(s.indexOf("为", 23) + 1, s.lastIndexOf("，"), "********").toString());
                        } else if ("后台重置密码".equals(sm.getType())) {
                            StringBuffer content = new StringBuffer(sm.getContent());
                            StringBuffer s = content.replace(sm.getContent().indexOf("号") + 1, sm.getContent().indexOf("密") - 1, "********");
                            sm.setAllContent(content.replace(s.indexOf("：") + 1, s.indexOf("如") -1,"********").toString());
                        } else if ("提现验证码".equals(sm.getType())) {
                            StringBuffer content = new StringBuffer(sm.getContent());
                            StringBuffer s = content.replace(sm.getContent().indexOf("：", 9) + 1, sm.getContent().indexOf("。", 10), "********");
                            StringBuffer s1 = s.replace(content.indexOf("：", 11) + 1, content.indexOf("元"), "********");
                            sm.setAllContent(s1.toString());
                        } else {
                            sm.setAllContent(sm.getContent());
                        }
                    }

//					sm.setAllContent(sm.getContent().);
                    // sm.setAllMobile(sm.getMobile());
//                    sm.setAllContent(sm.getContent());
                    /*
                     * if(null!=form.getMobile()){//将搜索手机号码独立出来,但是只输入一半手机号码，无法处理
                     * sm.setMobile(form.getMobile()); }else{
                     */
                    // if(sm.getMobile().length()>60){
                    // sm.setMobile(sm.getMobile().substring(0,30)+"...");
                    // }
                    // //}
                    // if(sm.getContent().length()>60){
                    // sm.setContent(sm.getContent().substring(0,30)+"...");
                    // }
                }
            }

            modeAndView.addObject(SmsLogDefine.LIST_FORM, logs);
            form.setPaginator(paginator);
            modeAndView.addObject(SmsLogDefine.MESSAGE_FORM, form);
        }

    }

    /**
     * 消息记录列表
     */
    @RequestMapping(value = SmsLogDefine.INIT)
    @RequiresPermissions(SmsLogDefine.PERMISSIONS_VIEW)
    public ModelAndView messageList(HttpServletRequest request, SmsLogBean form) {
        LogUtil.startLog(SmsLogController.class.toString(), SmsLogDefine.INIT);
        ModelAndView modeAndView = new ModelAndView(SmsLogDefine.MESSAGE_LIST_VIEW);

        this.createPage(request, modeAndView, form);
        LogUtil.endLog(SmsLogController.class.toString(), SmsLogDefine.INIT);
        return modeAndView;
    }

    /**
     * 分页
     *
     * @param request
     * @param modeAndView
     * @param form
     */
    private void timecreatePage(HttpServletRequest request, ModelAndView modeAndView, SmsLogBean form) {
        SmsOntimeCustomize smsOntimeCustomize = new SmsOntimeCustomize();
        if (StringUtils.isNotEmpty(form.getMobile())) {
            smsOntimeCustomize.setMobile(form.getMobile());
        }
        if (StringUtils.isNotEmpty(form.getPost_time_begin())) {
            int begin = GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayStart(form.getPost_time_begin()));
            smsOntimeCustomize.setStarttime(begin);
        }
        if (StringUtils.isNotEmpty(form.getPost_time_end())) {
            int end = GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayEnd(form.getPost_time_end()));
            smsOntimeCustomize.setEndtime(end);
        }
        if (form.getStatus() != null) {
            smsOntimeCustomize.setStatus(form.getStatus());
        }
        Integer count = this.logService.queryTimeCount(smsOntimeCustomize);
        if (count >= 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            smsOntimeCustomize.setLimitStart(paginator.getOffset());
            smsOntimeCustomize.setLimitEnd(paginator.getLimit());

            List<SmsOntimeCustomize> logs = this.logService.queryTime(smsOntimeCustomize);
            for (int i = 0; i < logs.size(); i++) {
                if (logs.get(i).getMobile() == null || logs.get(i).getMobile().equals("")) {
                    String member = "";
                    String money = "";
                    String add_time_end = "";
                    String add_time_begin = "";
                    String re_time_end = "";
                    String re_time_begin = "";
                    if (logs.get(i).getOpenAccount() != null && logs.get(i).getOpenAccount() == 0) {
                        member = "所有未开户用户";
                    } else if (logs.get(i).getOpenAccount() != null && logs.get(i).getOpenAccount() == 1) {
                        member = "所有已开户用户";
                    } else if (logs.get(i).getOpenAccount() != null && logs.get(i).getOpenAccount() == 3) {
                        member = "所有用户";
                    }

                    if (logs.get(i).getAddMoneyCount() != null
                            && logs.get(i).getAddMoneyCount().compareTo(new BigDecimal(0)) != 0) {
                        money = logs.get(i).getAddMoneyCount() + "";
                    }
                    if (logs.get(i).getAddTimeBegin() != null && !logs.get(i).getAddTimeBegin().equals("")) {
                        add_time_begin = logs.get(i).getAddTimeBegin();
                    }
                    if (logs.get(i).getAddTimeEnd() != null && !logs.get(i).getAddTimeEnd().equals("")) {
                        add_time_end = logs.get(i).getAddTimeEnd();
                    }
                    if (logs.get(i).getReTimeBegin() != null && !logs.get(i).getReTimeBegin().equals("")) {
                        re_time_begin = logs.get(i).getReTimeBegin();
                    }
                    if (logs.get(i).getReTimeEnd() != null && !logs.get(i).getReTimeEnd().equals("")) {
                        re_time_end = logs.get(i).getReTimeEnd();
                    }

                    logs.get(i).setMobile(
                            "筛选会员:" + member + ",累计出借金额：" + money + ",用户出借日期段：" + add_time_begin + "-" + add_time_end
                                    + ",用户注册日期段：" + re_time_begin + "-" + re_time_end);
                }
            }
            modeAndView.addObject(SmsLogDefine.LIST_FORM, logs);
            form.setPaginator(paginator);
            modeAndView.addObject(SmsLogDefine.MESSAGE_FORM, form);
        }

    }

    /**
     * 消息记录列表
     */
    @RequestMapping(value = SmsLogDefine.TIME_INIT)
    @RequiresPermissions(SmsLogDefine.PERMISSIONS_VIEW)
    public ModelAndView timemessageList(HttpServletRequest request, SmsLogBean form) {
        LogUtil.startLog(SmsLogController.class.toString(), SmsLogDefine.TIME_INIT);
        ModelAndView modeAndView = new ModelAndView(SmsLogDefine.TIME_MESSAGE_LIST_VIEW);

        this.timecreatePage(request, modeAndView, form);
        LogUtil.endLog(SmsLogController.class.toString(), SmsLogDefine.TIME_INIT);
        return modeAndView;
    }

    /**
     * 删除未发送的定时任务
     */
    @RequestMapping(value = SmsLogDefine.DELETE)
    @RequiresPermissions(SmsLogDefine.PERMISSIONS_MODIFY)
    public ModelAndView timemessageDelete(HttpServletRequest request, SmsLogBean form) {
        LogUtil.startLog(SmsLogController.class.toString(), SmsLogDefine.TIME_INIT);
        ModelAndView modeAndView = new ModelAndView(SmsLogDefine.TIME_MESSAGE_LIST_VIEW);
        if (form.getId() != null) {
            logService.updateSmsOnTime(form.getId(), 3);
        }
        this.timecreatePage(request, modeAndView, form);
        LogUtil.endLog(SmsLogController.class.toString(), SmsLogDefine.TIME_INIT);
        return modeAndView;
    }

}
