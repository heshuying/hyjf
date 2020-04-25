package com.hyjf.api.surong.user.regist;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.web.BaseController;
import com.hyjf.common.http.HttpDeal;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;

@Controller("registerServer")
@RequestMapping(value = RegisterDefine.REQUEST_MAPPING)
public class RegisterServer extends BaseController {
    @Autowired  
    private RegisterService registerService;
    

    /**
     * 注册
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = RegisterDefine.REGIST_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public JSONObject registAction(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(RegisterServer.class.getName(), RegisterDefine.REGIST_ACTION);
        JSONObject ret = new JSONObject();
        


        /*if (!this.checkSign(investBean, RegisterDefine.REGIST_ACTION)) {
            ret.put("status", RegisterDefine.FAILED);
            ret.put("statusDesc", "验签失败！");
            return ret;
        }*/

        // 手机号
        String mobile = request.getParameter("mobile");
        // 登录密码
        String password = request.getParameter("password");
        // 检查参数正确性
        if (Validator.isNull(mobile) || Validator.isNull(password)) {
            ret.put("status", "1");
            ret.put("statusDesc", "请求参数非法");
            return ret;
        }

        // 业务逻辑
        try {
            if (Validator.isNull(mobile)) {
                ret.put("status", "1");
                ret.put("statusDesc", "手机号不能为空");
                return ret;
            }
            if (Validator.isNull(password)) {
                ret.put("status", "1");
                ret.put("statusDesc", "密码不能为空");
                return ret;
            }
            if (!Validator.isMobile(mobile)) {
                ret.put("status", "1");
                ret.put("statusDesc", "请输入您的真实手机号码");
                return ret;
            }
            if (password.length() < 6 || password.length() > 16) {
                ret.put("status", "1");
                ret.put("statusDesc", "密码长度6-16位");
                return ret;
            }
            if (Validator.isNumber(password.substring(0, 1))) {
                ret.put("status", "1");
                ret.put("statusDesc", "密码首位必须为字母");
                return ret;
            }
            boolean hasNumber = false;

            for (int i = 0; i < password.length(); i++) {
                if (Validator.isNumber(password.substring(i, i + 1))) {
                    hasNumber = true;
                    break;
                }
            }
            if (!hasNumber) {
                ret.put("status", "1");
                ret.put("statusDesc", "密码必须包含数字");
                return ret;
            }
            if (!Validator.isMobile(mobile)) {
                ret.put("status", "1");
                ret.put("statusDesc", "请输入您的真实手机号码");
                return ret;
            }
            {
                Users regUser = registerService.countUserByMobile(mobile);
                if (regUser != null) {
                    String statusDesc = "注册成功";
                    ret.put("status", "0");
                    ret.put("hyjfName", regUser.getUsername());
                    ret.put("userId", regUser.getUserId());
                    ret.put("statusDesc", statusDesc);
                    ret.put("chinapnrUsrid", "");
                    ret.put("bankcard", "");
                    ret.put("bank", "");
                    ret.put("username", "");
                    ret.put("idcard", "");
                    
                    //查询开户状态
                    List<BankOpenAccount> aclist = registerService.getAccountInfo(regUser.getUserId()); 
                    if(aclist!=null&&aclist.size()>0){
                        ret.put("chinapnrUsrid", aclist.get(0).getAccount());
                    }
                    //查询快捷卡
                    List<BankCard> ablist = registerService.getBankCard(regUser.getUserId());
                    if(ablist!=null&&ablist.size()>0){
                        ret.put("bankcard", ablist.get(0).getCardNo());
                        ret.put("bank", ablist.get(0).getBank());
                    }
                    //查询客户信息
                    List<UsersInfo> uslist = registerService.getUser(regUser.getUserId());
                    if(uslist!=null&&uslist.size()>0){
                        ret.put("username", uslist.get(0).getTruename());
                        ret.put("idcard", uslist.get(0).getIdcard());
                    }
                    registerService.updateUser(regUser.getUserId());
                    return ret;
                }
            }
            // 注册
            Users user = new Users();
            Map<String,Object> map = registerService.insertUserAction(mobile, password,CustomUtil.getIpAddr(request), request,user);

            if (Integer.parseInt(map.get("userId").toString()) != 0) {
                Users reUser = (Users)map.get("user");
                String statusDesc = "注册成功";
                ret.put("status", "0");
                ret.put("hyjfName", reUser.getUsername());
                ret.put("userId", map.get("userId").toString());
                ret.put("chinapnrUsrid", "");
                ret.put("bankcard", "");
                ret.put("bank", "");
                ret.put("username", "");
                ret.put("idcard", "");
                ret.put("statusDesc", statusDesc);
            } else {
                ret.put("status", "1");
                ret.put("statusDesc", "注册失败,参数异常");
            }
        } catch (Exception e) {
            ret.put("status", "1");
            ret.put("statusDesc", "注册发生错误,参数异常");
        }
        
        LogUtil.endLog(RegisterServer.class.getName(), RegisterDefine.REGIST_ACTION);
        return ret;
    }
    
    /**
     * 注册判定用户状态
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = RegisterDefine.CHECKMOBILE_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public JSONObject checkInfoAction(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(RegisterServer.class.getName(), RegisterDefine.CHECKMOBILE_ACTION);
        JSONObject ret = new JSONObject();

        // 手机号
        String mobile = request.getParameter("mobile");
        // 检查参数正确性
        if (Validator.isNull(mobile)) {
            ret.put("status", "1");
            ret.put("statusDesc", "请求参数非法");
            return ret;
        }

        // 业务逻辑
        try {
            if (Validator.isNull(mobile)) {
                ret.put("status", "1");
                ret.put("statusDesc", "手机号不能为空");
                return ret;
            }
            if (!Validator.isMobile(mobile)) {
                ret.put("status", "1");
                ret.put("statusDesc", "请输入您的真实手机号码");
                return ret;
            }
            if (!Validator.isMobile(mobile)) {
                ret.put("status", "1");
                ret.put("statusDesc", "请输入您的真实手机号码");
                return ret;
            }
            {
                Users regUser = registerService.countUserByMobile(mobile);
                if(regUser != null){
                    List<BankOpenAccount> aclist = registerService.getAccountInfo(regUser.getUserId()); 
                    if(aclist.size()>0){
                        ret.put("status", "2");
                        ret.put("statusDesc", "输入姓名身份证号");
                        return ret;
                    }else{
                        ret.put("status", "3");
                        ret.put("statusDesc", "联系客服");
                        return ret; 
                    }
                }else{
                    ret.put("status", "0");
                    ret.put("statusDesc", "正常注册");
                    return ret;
                }
            }
        } catch (Exception e) {
            ret.put("status", "1");
            ret.put("statusDesc", "发生错误,参数异常");
        }
        
        LogUtil.endLog(RegisterServer.class.getName(), RegisterDefine.CHECKMOBILE_ACTION);
        return ret;
    }
    
    
    /**
     * 注册判定用户状态
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = RegisterDefine.CHECKINFO_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public JSONObject checkInfo(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(RegisterServer.class.getName(), RegisterDefine.CHECKINFO_ACTION);
        JSONObject ret = new JSONObject();

        // 手机号
        String mobile = request.getParameter("mobile");
        // 真实姓名
        String name = request.getParameter("name");
        // 身份证号
        String idNo = request.getParameter("idNo");
        // 检查参数正确性
        if (Validator.isNull(mobile) || Validator.isNull(name) || Validator.isNull(idNo)) {
            ret.put("status", "1");
            ret.put("statusDesc", "请求参数非法");
            return ret;
        }

        // 业务逻辑
        try {
            if (Validator.isNull(mobile)) {
                ret.put("status", "1");
                ret.put("statusDesc", "手机号不能为空");
                return ret;
            }
            if (Validator.isNull(name)) {
                ret.put("status", "1");
                ret.put("statusDesc", "姓名不能为空");
                return ret;
            }
            if (Validator.isNull(idNo)) {
                ret.put("status", "1");
                ret.put("statusDesc", "身份证号不能为空");
                return ret;
            }
            if (!Validator.isMobile(mobile)) {
                ret.put("status", "1");
                ret.put("statusDesc", "请输入您的真实手机号码");
                return ret;
            }
            if (!Validator.isMobile(mobile)) {
                ret.put("status", "1");
                ret.put("statusDesc", "请输入您的真实手机号码");
                return ret;
            }
            {
                Users regUser = registerService.countUserByMobile(mobile);
                if(regUser != null){
                    List<UsersInfo> uslist = registerService.getUser(regUser.getUserId()); 
                    if(uslist!=null&&uslist.size()>0){
                        if(uslist.get(0).getIdcard().equals(idNo) && uslist.get(0).getTruename().equals(name)){
                            ret.put("status", "0");
                            ret.put("statusDesc", "正常注册");
                            return ret;  
                        }else{
                            ret.put("status", "1");
                            ret.put("statusDesc", "姓名或身份证号错误");
                            return ret;
                        }
                    }else{
                        ret.put("status", "1");
                        ret.put("statusDesc", "姓名或身份证号错误");
                        return ret;
                    }
                }else{
                    ret.put("status", "1");
                    ret.put("statusDesc", "姓名或身份证号错误");
                    return ret;
                }
            }
        } catch (Exception e) {
            ret.put("status", "1");
            ret.put("statusDesc", "发生错误,参数异常");
        }
        
        LogUtil.endLog(RegisterServer.class.getName(), RegisterDefine.CHECKINFO_ACTION);
        return ret;
    }
    


}
