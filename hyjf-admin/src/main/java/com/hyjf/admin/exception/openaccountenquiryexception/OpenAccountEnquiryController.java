package com.hyjf.admin.exception.openaccountenquiryexception;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.exception.openaccountexception.OpenAccountBean;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.bank.service.user.accountopenpage.UserOpenAccountPageService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.mapper.auto.*;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.admin.OpenAccountEnquiryBean;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @package com.hyjf.admin.maintenance.Admin
 * @author GOGTZ-T
 * @date 2017/08/25 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = OpenAccountEnquiryDefine.REQUEST_MAPPING)
public class OpenAccountEnquiryController extends BaseController {
    Logger _log = LoggerFactory.getLogger(OpenAccountEnquiryController.class);
    @Autowired
    private OpenAccountEnquiryService openAccountEnquiryService;

    @Autowired
    protected BankOpenAccountMapper bankOpenAccountMapper;
    @Autowired
    protected UsersMapper usersMapper;
    @Autowired
    protected BankCardMapper bankCardMapper;
    @Autowired
    protected AppChannelStatisticsDetailMapper appChannelStatisticsDetailMapper;
    @Autowired
    protected BankOpenAccountLogMapper bankOpenAccountLogMapper;
    @Autowired
    protected UsersInfoMapper usersInfoMapper;

    @Autowired
    private UserOpenAccountPageService accountPageService;

    /**
     * 账户设置画面初始化
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(OpenAccountEnquiryDefine.INIT_PATH)
    @RequiresPermissions(OpenAccountEnquiryDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, @ModelAttribute OpenAccountBean form) {
        LogUtil.startLog(OpenAccountEnquiryDefine.THIS_CLASS, OpenAccountEnquiryDefine.INIT_PATH);
        ModelAndView modelAndView = new ModelAndView(OpenAccountEnquiryDefine.UPDATE_ACCOUNTENQURIY_PATH);
        LogUtil.endLog(OpenAccountEnquiryDefine.THIS_CLASS, OpenAccountEnquiryDefine.INIT_PATH);
        return modelAndView;
    }
    /**
     * ajax用户按照手机号和身份证号查询开户掉单校验
     *
     * @param request
     *
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(OpenAccountEnquiryDefine.ERROR)
    public String openAccountEnquiryError(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(OpenAccountEnquiryDefine.THIS_CLASS, OpenAccountEnquiryDefine.UPDATE_PATH);
        JSONObject jsonString = new JSONObject();
        String userId = ShiroUtil.getLoginUserId();
        // 请求参数
        Map<String, String> requestMap = new HashMap<>();
        // 返回参数
        ModelAndView modelAndView = new ModelAndView(OpenAccountEnquiryDefine.UPDATE_ACCOUNTENQURIYINFO_PATH);
        String phone = request.getParameter("param").trim();
        String userName = request.getParameter("userName");
        // 按照手机号查询 2按照身份证查询
        String requestType = request.getParameter("requestType").trim();
        // 调用查询电子账户
        BankCallBean selectbean = new BankCallBean();
        selectbean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        selectbean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
        selectbean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
        selectbean.setTxDate(GetOrderIdUtils.getTxDate());
        selectbean.setTxTime(GetOrderIdUtils.getTxTime());
        selectbean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
        selectbean.setChannel("000002");
        // 操作者ID
        selectbean.setLogUserId(String.valueOf(userId));
        selectbean.setLogOrderId(GetOrderIdUtils.getOrderId2(Integer.parseInt(userId)));
        selectbean.setLogOrderDate(GetOrderIdUtils.getOrderDate());
        selectbean.setLogClient(0);
        // 返回参数
        BankCallBean retBean = null;
        // 设置本地的开户状态
        setDbOpenParam(jsonString, requestType, userName, phone);
        // 已经开户的就不查询了
        if ("0".equals(jsonString.get("isOpen"))) {
            if ("1".equals(requestType)) {
                // 根据手机号查询
                ValidatorFieldCheckUtil.validateMobile(modelAndView, "phone", phone, true);
                if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
                    jsonString.put("info", "输入号码验证失败");
                    return jsonString.toJSONString();
                }
                // 手机号码验证成功
                selectbean.setTxCode(BankCallConstant.TXCODE_ACCOUNT_QUERY_BY_MOBILE_PLUS);
                requestMap.put("phone", phone);
                OpenAccountEnquiryBean accountMap = this.openAccountEnquiryService.accountEnquiry(requestMap);
                if (accountMap == null) {
                    jsonString.put("info", "该用户不存在！");
                    return jsonString.toJSONString();
                }
                selectbean.setMobile(phone);
                // 首次查询上送空；翻页查询上送1； 翻页标志
                selectbean.setRtnInd("");
                // nxProduct翻页产品号 翻页控制使用；首次查询上送空；翻页查询时上送上页返回的最后一条记录的产品。
                selectbean.setNxProduct("");
                // 翻页控制使用；首次查询上送空；翻页查询时上送上页返回的最后一条记录的电子账号
                selectbean.setNxAccountId("");
                // 调用接口
                retBean = BankCallUtils.callApiBg(selectbean);
                if (retBean != null && BankCallStatusConstant.RESPCODE_SUCCESS.equals(retBean.getRetCode())) {
                    JSONArray jsa = JSONArray.parseArray(retBean.getSubPacks());
                    if (jsa != null && jsa.size() > 0) {
                        // 如果返回的结果大于0条
                        jsonString.put("status", "y");

                        JSONObject jso = new JSONObject();
                        jso = jsa.getJSONObject(0);
                        jsonString.put("accountId", jso.get("accountId"));
                        jsonString.put("regTimeEnd", jso.get("issDate"));
                        jsonString.put("idNo", jso.get("idNo"));
                        jsonString.put("name", jso.get("name"));
                        jsonString.put("addr", jso.get("addr"));
                        jsonString.put("username", accountMap.getUsername());
                        jsonString.put("roleId", jso.get("identity"));
                        return jsonString.toJSONString();
                    } else {
                        jsonString.put("info", "该用户无银行开户信息！");
                        return jsonString.toJSONString();
                    }
                } else {
                    jsonString.put("info", "该用户无银行开户信息！");
                    return jsonString.toJSONString();
                }
            } else if ("2".equals(requestType)) {
                // 根据身份证查询
                // 根据身份证查询时候必须验证用户名不能为空
                if (userName==null||"".equals(userName)) {
                    jsonString.put("info", "请输入用户名");
                    return jsonString.toJSONString();
                }
                Users user = getUsersByUserName(userName);
                if (user == null) {
                    jsonString.put("info", "未根据用户名查询到该用户");
                    return jsonString.toJSONString();
                }
                // 根据身份证查询
                selectbean.setTxCode(BankCallConstant.TXCODE_ACCOUNTID_QUERY);
                selectbean.setIdType("01");
                selectbean.setIdNo(phone);
                // 调用接口
                retBean = BankCallUtils.callApiBg(selectbean);
                if (retBean != null && BankCallStatusConstant.RESPCODE_SUCCESS.equals(retBean.getRetCode())) {
                    jsonString.put("status", "y");
                    jsonString.put("accountId", retBean.getAccountId());
                    jsonString.put("regTimeEnd", retBean.getOpenDate());
                    jsonString.put("username", userName);
                    jsonString.put("idNo", phone);
                    return jsonString.toJSONString();
                } else {
                    jsonString.put("info", "该用户无银行开户信息！");
                    return jsonString.toJSONString();
                }
            }
        }
        return jsonString.toJSONString();
    }

    // 查询本地开户状态
    private void setDbOpenParam(JSONObject result, String requestType, String userName, String phone) {
        Map<String, String> requestMap = new HashMap<>();
        if ("1".equals(requestType)) {
            // 手机号
            requestMap.put("phone", phone);
        } else if ("2".equals(requestType)) {
            // 身份证
            requestMap.put("username", userName);
        }
        OpenAccountEnquiryBean accountMap = this.openAccountEnquiryService.accountEnquiry(requestMap);
        if (accountMap != null) {
            // 已经开户了
            result.put("isOpen", accountMap.getAccountStatus());
            result.put("username", accountMap.getUsername());
            result.put("accountId", accountMap.getAccount());
            result.put("mobile", accountMap.getMobile());
            result.put("regTimeEnd", accountMap.getRegTimeEnd());
            result.put("roleId", accountMap.getRoleId());
            result.put("name", accountMap.getName());
            result.put("idNo", accountMap.getIdNo());
            result.put("userId", accountMap.getUserid());
        } else {
            // 没开户
            result.put("isOpen", "0");
        }
    }

    /**
     * 用户按照手机号和身份证号查询开户掉单后同步系统掉单信息，更改用户状态
     *
     * @param request
     *
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(OpenAccountEnquiryDefine.UPDATEACCOUNT_PATH)
    public JSONObject openAccountEnquiryUpdate(HttpServletRequest request, HttpServletResponse response) {
        String userid = request.getParameter("userid");
        String accountId = request.getParameter("accountId");
        String role = request.getParameter("roleId");
        if(role==null||role.equals("")){
            role = "1";
        }
        Integer roleId = Integer.parseInt(role);
        // 证件类型
        String idNo = request.getParameter("idNo").trim();
        String name = request.getParameter("name").trim();
        // 开户时间
        String regTimeEnd = request.getParameter("regTimeEnd").trim();

        if(userid!=null&&idNo!=null&&accountId!=null&&regTimeEnd!=null){
            try{
                // 查询银行是否已经设置了交易密码
                Integer isSetPassword = getIsSetPassword(accountId,Integer.parseInt(userid));
                openAccountEnquiryService.updateUserAccountData(userid,accountId,roleId,idNo,name,regTimeEnd,isSetPassword);
            }catch (Exception e){
                e.printStackTrace();
            }
            BankCallBean bean = new BankCallBean();
            bean.setAccountId(accountId);
            bean.setLogUserId(userid);
            Users users = getUsersByUserId(Integer.parseInt(userid));
            bean.setMobile(users.getMobile());
            // 检查是否需要保存银行卡
            BankCard card = accountPageService.getBankCardByUserId(userid);
            if(card==null){
                // 保存银行卡信息
                _log.info("开户掉单  同步银行卡信息 userid:{} ",userid );
                this.accountPageService.updateCardNoToBank(bean);
            }
        }
        JSONObject ret = new JSONObject();
        ret.put("status", "success");
        ret.put("result", "开户掉单同步成功");
        return ret;
    }

    /**
     * 查看是否设置交易密码
     * @param account
     * @param userId
     * @return
     */
    private Integer getIsSetPassword(String account,Integer userId) {
        // 调用查询电子账户密码是否设置
        BankCallBean selectbean = new BankCallBean();
        selectbean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        selectbean.setTxCode(BankCallConstant.TXCODE_PASSWORD_SET_QUERY);
        selectbean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
        selectbean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
        selectbean.setTxDate(GetOrderIdUtils.getTxDate());
        selectbean.setTxTime(GetOrderIdUtils.getTxTime());
        selectbean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
        selectbean.setChannel(BankCallConstant.CHANNEL_PC);
        // 电子账号
        selectbean.setAccountId(account);

        // 操作者ID
        selectbean.setLogUserId(userId+"");
        selectbean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));
        selectbean.setLogClient(0);
        // 返回参数
        BankCallBean retBean = BankCallUtils.callApiBg(selectbean);

        if(retBean==null){
            return 0;
        }
        if("1".equals(retBean.getPinFlag())){
            return 1;
        }
        return 0;
    }

    /**
     * 根据用户ID取得用户信息
     *
     * @param userId
     * @return
     */
    public Users getUsersByUserId(Integer userId) {
        if (userId != null) {
            UsersExample example = new UsersExample();
            example.createCriteria().andUserIdEqualTo(userId);
            List<Users> usersList = this.usersMapper.selectByExample(example);
            if (usersList != null && usersList.size() > 0) {
                return usersList.get(0);
            }
        }
        return null;
    }

    private Users getUsersByUserName(String userName) {
        if (userName != null) {
            UsersExample example = new UsersExample();
            example.createCriteria().andUsernameEqualTo(userName);
            List<Users> usersList = this.usersMapper.selectByExample(example);
            if (usersList != null && usersList.size() > 0) {
                return usersList.get(0);
            }
        }
        return null;
    }

    /**
     * 校验返回的电子账号是否已被使用
     *
     * @param account
     * @return
     */
    private boolean checkAccount(String account) {
        // 根据account查询用户是否开户
        BankOpenAccountExample example = new BankOpenAccountExample();
        example.createCriteria().andAccountEqualTo(account);
        List<BankOpenAccount> bankOpenList = this.bankOpenAccountMapper.selectByExample(example);
        if (bankOpenList != null && bankOpenList.size() > 0) {
            for (int i = 0; i < bankOpenList.size(); i++) {
                Integer userId = bankOpenList.get(i).getUserId();
                UsersExample userExample = new UsersExample();
                userExample.createCriteria().andUserIdEqualTo(userId);
                List<Users> user = this.usersMapper.selectByExample(userExample);
                if (user != null && user.size() > 0) {
                    for (int j = 0; j < user.size(); j++) {
                        Users info = user.get(j);
                        Integer bankOpenFlag = info.getBankOpenAccount();
                        if (bankOpenFlag == 1) {
                            return false;
                        }
                    }
                }
            }

        }
        return true;
    }
}
