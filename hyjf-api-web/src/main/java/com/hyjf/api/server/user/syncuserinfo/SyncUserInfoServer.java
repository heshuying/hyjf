package com.hyjf.api.server.user.syncuserinfo;

import cn.emay.slf4j.Logger;
import cn.emay.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.server.util.ErrorCodeConstant;
import com.hyjf.api.web.BaseController;
import com.hyjf.base.bean.BaseDefine;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 外部服务接口：同步用户信息
 *
 */
@Controller
@RequestMapping(value = SyncUserInfoDefine.REQUEST_MAPPING)
public class SyncUserInfoServer extends BaseController {

    Logger log = LoggerFactory.getLogger(SyncUserInfoServer.class);
    private static DecimalFormat DF_FOR_VIEW = new DecimalFormat("#,##0.00");
    @Autowired
    private SyncUserService syncUserService;

    @ResponseBody
    @RequestMapping(value = SyncUserInfoDefine.SYNCUSERINFO_ACTION)
    public SyncUserInfoResult syncUserInfo(@RequestBody SyncUserInfoRequest syncUserInfoRequest, HttpServletRequest request) {
        log.info("synUserInfoRequest is {}", JSONObject.toJSONString(syncUserInfoRequest));
        SyncUserInfoResult result = new SyncUserInfoResult();
        //机构编号
        String instCode = syncUserInfoRequest.getInstCode();
        //用户银行电子账号
        String accountIds = syncUserInfoRequest.getAccountIds();
        List<String> accountid = new ArrayList<>();
        String uId[] = accountIds.split(",");

        for (int i = 0; i < uId.length; i++) {
            if(!uId[i].isEmpty()){
                accountid.add(uId[i]);
            }
        }
        try {
            //验证请求参数
            if (Validator.isNull(instCode) || Validator.isNull(accountIds) ){
                result.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
                result.setStatusDesc("请求参数非法");
                return result;
            }
            //验签
            if (!this.verifyRequestSign(syncUserInfoRequest, BaseDefine.METHOD_SERVER_SYNCUSERINFO)) {
                log.info("----验签失败----");
                result.setStatusForResponse(ErrorCodeConstant.STATUS_CE000002);
                result.setStatusDesc("验签失败！");
                return result;
            }
            List<SyncUserInfoResult.AccountBean> list = new ArrayList<>();
            SyncUserInfoResult.AccountBean accountBean = null;
            //根据电子账户ID获取用户ID
            for (String accountId:accountid) {
                BankOpenAccount bankOpenAccount = this.syncUserService.getUserByAccountId(accountId);
                if (bankOpenAccount == null) {
                    result.setStatusForResponse(ErrorCodeConstant.STATUS_CE000004);
                    return result;
                }
                Integer userId = bankOpenAccount.getUserId();
                //根据用户ID获取用户账户信息
                Account account = syncUserService.getAccount(userId);
                if (account == null) {
                    result.setStatusForResponse(ErrorCodeConstant.STATUS_CE000010);
                    return result;
                }
                accountBean = new SyncUserInfoResult.AccountBean();
                this.copyProperties2Result(account, accountBean);

                accountBean.setAccountId(accountId);
                list.add(accountBean);
                result.setStatusForResponse(ErrorCodeConstant.SUCCESS);
            }
            result.setData(list);
            return result;
        }catch (Exception e){
            log.error("获取用户信息失败",e);
            result.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
            return result;
        }
    }
    private void copyProperties2Result(Account account, SyncUserInfoResult.AccountBean accountBean) {
        accountBean.setPlanAwaitAmount(DF_FOR_VIEW.format(account.getPlanAccountWait()));
        accountBean.setPlanAwaitCapital(DF_FOR_VIEW.format(account.getPlanCapitalWait()));
        accountBean.setPlanAwaitInterest(DF_FOR_VIEW.format(account.getPlanInterestWait()));
        accountBean.setBorrowAwaitAmount(DF_FOR_VIEW.format(account.getBankAwait()));
        accountBean.setBorrowAwaitCapital(DF_FOR_VIEW.format(account.getBankAwaitCapital()));
        accountBean.setBorrowAwaitInterest(DF_FOR_VIEW.format(account.getBankAwaitInterest()));
        accountBean.setBalanceAmount(DF_FOR_VIEW.format(account.getBankBalance()));
        accountBean.setFrozenAmount(DF_FOR_VIEW.format(account.getBankFrost()));
        accountBean.setTotalAmount(DF_FOR_VIEW.format(account.getBankTotal()));
        accountBean.setInvestSum(DF_FOR_VIEW.format(account.getBankInvestSum()));
        accountBean.setInterestSum(DF_FOR_VIEW.format(account.getBankInterestSum()));
    }
}
