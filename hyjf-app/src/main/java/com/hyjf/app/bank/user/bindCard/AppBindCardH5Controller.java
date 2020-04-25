package com.hyjf.app.bank.user.bindCard;

import com.hyjf.app.BaseController;
import com.hyjf.app.BaseResultBeanFrontEnd;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author fuqiang
 */
@Controller
public class AppBindCardH5Controller extends BaseController {

    /** THIS_CLASS */
    private static final String THIS_CLASS = AppBindCardH5Controller.class.getName();

    private final String TOKEN_ISINVALID_STATUS = "Token失效，请重新登录";

    @Autowired
    private AppBindCardService userBindCardService;

    /**
     * 获取绑卡数据
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = AppBindCardDefine.BINDCARD_ACTION, method = RequestMethod.GET)
    public BaseResultBeanFrontEnd getBindCardData(HttpServletRequest request){
        LogUtil.startLog(THIS_CLASS, AppBindCardDefine.BINDCARD_ACTION);

        BindCardResultBean resultBean = new BindCardResultBean();
//        resultBean.setStatus(BindCardBean.SUCCESS);
//        resultBean.setStatusDesc(BindCardBean.SUCCESS_MSG);

        // 唯一标识
        String sign = request.getParameter("sign");

//        Integer userId = Integer.valueOf(request.getParameter("userId"));

        Integer userId = null;
        try {
            userId = SecretUtil.getUserId(sign);
        }catch (Exception e){ // token失效
            resultBean.setStatus(BaseResultBeanFrontEnd.FAIL);
            resultBean.setStatusDesc(TOKEN_ISINVALID_STATUS);
            return resultBean;
        }
        if(userId == null){
            resultBean.setStatus(CustomConstants.APP_STATUS_FAIL);
            resultBean.setStatusDesc(TOKEN_ISINVALID_STATUS);
            return resultBean;
        }


        BindBeanDeatail beanDetail = new BindBeanDeatail();
        try {
            // 查询用户信息获取真实姓名和银行卡和手机号码
//            List<BankCard> bankCardList= userBindCardService.getAccountBankByUserId(String.valueOf(userId));
            Users users = userBindCardService.getUsers(userId);
            UsersInfo usersInfoByUserId = userBindCardService.getUsersInfoByUserId(userId);
            if (users != null) {
//                BankCard bankCard = bankCardList.get(0);
                beanDetail.setTelNo(users.getMobile());
            } else {
                resultBean.setStatus(BindCardResultBean.FAIL);
                resultBean.setStatusDesc(BindCardResultBean.FAIL_MSG);
            }

            if (usersInfoByUserId != null) {
                beanDetail.setUserName(usersInfoByUserId.getTruename());
                beanDetail.setUserCardId(usersInfoByUserId.getIdcard());
            } else {
                resultBean.setStatus(BindCardResultBean.FAIL);
                resultBean.setStatusDesc(BindCardResultBean.FAIL_MSG);
            }

        } catch (Exception e) {
            resultBean.setStatus(BindCardResultBean.FAIL);
            resultBean.setStatusDesc(BindCardResultBean.FAIL_MSG);
        }
        resultBean.setFormData(beanDetail);
        resultBean.setStatus(BindCardResultBean.SUCCESS);
        resultBean.setStatusDesc(BindCardBean.SUCCESS_MSG);

        LogUtil.endLog(THIS_CLASS, AppBindCardDefine.BINDCARD_ACTION);
        return resultBean;
    }

    /**
     * 用户绑卡
     * @param form
     * @return
     */
  /*  @RequestMapping(value = AppBindCardDefine.BINDCARD_ACTION, method = RequestMethod.POST)
    public BaseResultBeanFrontEnd bindCard(HttpServletRequest request, @ModelAttribute() BindCardBean form){

        LogUtil.startLog(THIS_CLASS, AppBindCardDefine.BINDCARD_ACTION);

        // 唯一标识
        String sign = request.getParameter("sign");
        Integer userId = SecretUtil.getUserId(sign);

        BaseResultBeanFrontEnd result = new BaseResultBeanFrontEnd();

        // 银行卡
        String bankId = form.getBankId();

        // 判断银行卡和userId非空
        if (Validator.isNull(bankId) || userId == 0 || userId == null) {
            result.setStatus(BaseResultBeanFrontEnd.FAIL);
            result.setStatusDesc(BaseResultBeanFrontEnd.FAIL_MSG);
            return result;
        }

        // 取得用户的客户号
        BankOpenAccount accountChinapnrTender = userBindCardService.getBankOpenAccount(userId);
        if (accountChinapnrTender == null || Validator.isNull(accountChinapnrTender.getAccount())) {
            result.setStatus(BaseResultBeanFrontEnd.FAIL);
            result.setStatusDesc(BaseResultBeanFrontEnd.FAIL_MSG);
            return result;
        }

        try {
            // 调用汇付接口(4.2.2 用户绑卡接口)
            BankCallBean bean = new BankCallBean();
            bean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));
            bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());
            bean.setLogUserId(String.valueOf(userId));
            bean.setLogRemark("用户绑卡");
            bean.setLogBankDetailUrl(BankCallConstant.BANK_URL_MOBILE);
            bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
            bean.setTxCode(BankCallMethodConstant.TXCODE_CARD_BIND);
            bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
            bean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
            bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号6位
            bean.setChannel(BankCallConstant.CHANNEL_APP);//
            bean.setAccountId(accountChinapnrTender.getAccount());// 存管平台分配的账号
            bean.setIdType(BankCallConstant.ID_TYPE_IDCARD);// 证件类型01身份证
            bean.setIdNo(form.getUserCardId());// 证件号
            bean.setName(form.getUserName());// 姓名
            bean.setMobile(form.getTelNo());// 手机号
            bean.setCardNo(form.getBankId());// 银行卡号
            bean.setRetUrl(PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() + AppBindCardDefine.REQUEST_MAPPING + AppBindCardDefine.RETURN_MAPPING);// 商户前台台应答地址(必须)
            bean.setNotifyUrl(PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() + AppBindCardDefine.REQUEST_MAPPING + AppBindCardDefine.NOTIFY_RETURN_MAPPING); // 商户后台应答地址(必须)
            bean.setUserIP(GetCilentIP.getIpAddr(request));// 客户IP
//		System.out.println("绑卡前台回调函数：\n" + bean.getRetUrl());
//		System.out.println("绑卡后台回调函数：\n" + bean.getNotifyUrl());
            LogAcqResBean logAcq = new LogAcqResBean();
            logAcq.setCardNo(form.getBankId());
            bean.setLogAcqResBean(logAcq);
        } catch (Exception e) {
            result.setStatus(BaseResultBeanFrontEnd.FAIL);
            result.setStatusDesc(BaseResultBeanFrontEnd.FAIL_MSG);
        }

        result.setStatus(BaseResultBeanFrontEnd.SUCCESS);
        result.setStatusDesc(BaseResultBeanFrontEnd.SUCCESS_MSG);

        LogUtil.endLog(THIS_CLASS, AppBindCardDefine.BINDCARD_ACTION);

        return result;
    }*/
}
