package com.hyjf.api.web;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.activity.actdec.listed2.ListedTwoActDefine;
import com.hyjf.activity.actdoubleeleven.bargain.BargainDoubleRequestBean;
import com.hyjf.activity.actdoubleeleven.bargain.BargainRequestBean;
import com.hyjf.activity.actdoubleeleven.bargain.PrizeBuyRequestBean;
import com.hyjf.activity.actdoubleeleven.bargain.SmsCodeRequestBean;
import com.hyjf.activity.actdoubleeleven.draw.ActDrawGuessRequestBean;
import com.hyjf.activity.actdoubleeleven.fightluck.FightLuckResultBean;
import com.hyjf.activity.actten.act2.ActQuestionTenRequestBean;
import com.hyjf.activity.list.ActivityListDataBean;
import com.hyjf.activity.mgm10.prizechange.PrizeChangeBean;
import com.hyjf.activity.mgm10.prizedraw.PrizeDrawBean;
import com.hyjf.activity.mgm10.recommend.RecommendBean;
import com.hyjf.activity.mgm10.regist.RegistRecommendBean;
import com.hyjf.activity.newyear.getcard.GetCardBean;
import com.hyjf.activity.newyear.lanternfestival.LanternFestivalBean;
import com.hyjf.activity.newyear.wealthcard.WealthCardBean;
import com.hyjf.api.aems.aemsunbindcardpage.AemsUnbindCardPageDefine;
import com.hyjf.api.aems.aemsunbindcardpage.AemsUnbindCardPageRequestBean;
import com.hyjf.api.aems.asset.AemsAssetSearchDefine;
import com.hyjf.api.aems.asset.AemsAssetStatusRequestBean;
import com.hyjf.api.aems.assetpush.AemsPushDefine;
import com.hyjf.api.aems.assetpush.AemsPushRequestBean;
import com.hyjf.api.aems.authstatus.AemsAuthStatusQueryDefine;
import com.hyjf.api.aems.authstatus.AemsAuthStatusQueryRequestBean;
import com.hyjf.api.aems.bindcardpage.AemsBindCardPageDefine;
import com.hyjf.api.aems.bindcardpage.AemsBindCardPageRequestBean;
import com.hyjf.api.aems.borrowdetail.AemsBorrowDetailDefine;
import com.hyjf.api.aems.borrowdetail.AemsBorrowDetailRequestBean;
import com.hyjf.api.aems.borrowlist.AemsBorrowListDefine;
import com.hyjf.api.aems.borrowlist.AemsBorrowListRequestBean;
import com.hyjf.api.aems.directrecharge.AemsUserDirectRechargeDefine;
import com.hyjf.api.aems.encryptpage.AemsBankOpenEncryptPageDefine;
import com.hyjf.api.aems.encryptpage.AemsBankOpenEncryptPageRequestBean;
import com.hyjf.api.aems.evaluation.AemsEvaluationDefine;
import com.hyjf.api.aems.evaluation.AemsEvaluationRequestBean;
import com.hyjf.api.aems.group.AemsOrganizationStructureBean;
import com.hyjf.api.aems.group.AemsOrganizationStructureDefine;
import com.hyjf.api.aems.invest.AemsInvestDefine;
import com.hyjf.api.aems.invest.AemsInvestListRequest;
import com.hyjf.api.aems.invest.AemsInvestRepayBean;
import com.hyjf.api.aems.invest.AemsRepayListRequest;
import com.hyjf.api.aems.mergeauth.AemsMergeAuthPagePlusDefine;
import com.hyjf.api.aems.mergeauth.AemsMergeAuthPagePlusRequestBean;
import com.hyjf.api.aems.register.AemsUserRegisterDefine;
import com.hyjf.api.aems.register.AemsUserRegisterRequestBean;
import com.hyjf.api.aems.repay.AemsRepayDefine;
import com.hyjf.api.aems.repay.AemsRepayParamBean;
import com.hyjf.api.aems.repayment.AemsBorrowRepaymentInfoBean;
import com.hyjf.api.aems.repayment.AemsBorrowRepaymentInfoDefine;
import com.hyjf.api.aems.repayplan.AemsBorrowRepayPlanDefine;
import com.hyjf.api.aems.repayplan.AemsBorrowRepayPlanRequestBean;
import com.hyjf.api.aems.synbalance.AemsSynBalanceDefine;
import com.hyjf.api.aems.synbalance.AemsSynBalanceRequestBean;
import com.hyjf.api.aems.syncuserinfo.AemsSyncUserInfoDefine;
import com.hyjf.api.aems.syncuserinfo.AemsSyncUserInfoRequest;
import com.hyjf.api.aems.tender.AemsAutoTenderDefine;
import com.hyjf.api.aems.tender.AemsAutoTenderRequestBean;
import com.hyjf.api.aems.tradelist.AemsTradeListBean;
import com.hyjf.api.aems.tradelist.AemsTradeListDefine;
import com.hyjf.api.aems.transpassword.AemsTransPasswordDefine;
import com.hyjf.api.aems.transpassword.AemsTransPasswordRequestBean;
import com.hyjf.api.aems.trusteePay.AemsTrusteePayDefine;
import com.hyjf.api.aems.trusteePay.AemsTrusteePayRequestBean;
import com.hyjf.api.aems.withdraw.AemsUserWithdrawDefine;
import com.hyjf.api.aems.withdraw.AemsUserWithdrawRequestBean;
import com.hyjf.api.server.asset.AssetStatusRequestBean;
import com.hyjf.api.server.assetpush.PushDefine;
import com.hyjf.api.server.assetpush.PushRequestBean;
import com.hyjf.api.server.assetpush.PushResultBean;
import com.hyjf.api.server.borrow.borrowlist.BorrowListDefine;
import com.hyjf.api.server.borrow.borrowlist.BorrowListRequestBean;
import com.hyjf.api.server.borrow.repayment.BorrowRepaymentInfoBean;
import com.hyjf.api.server.borrowDetail.BorrowDetailDefine;
import com.hyjf.api.server.borrowDetail.BorrowDetailRequestBean;
import com.hyjf.api.server.group.OrganizationStructureBean;
import com.hyjf.api.server.invest.InvestDefine;
import com.hyjf.api.server.invest.InvestListRequest;
import com.hyjf.api.server.invest.InvestRepayBean;
import com.hyjf.api.server.invest.RepayListRequest;
import com.hyjf.api.server.tender.AutoTenderDefine;
import com.hyjf.api.server.tender.AutoTenderRequestBean;
import com.hyjf.api.server.tradelist.TradeListBean;
import com.hyjf.api.server.tradelist.TradeListDefine;
import com.hyjf.api.server.user.accountopenpage.OpenAccountPageDefine;
import com.hyjf.api.server.user.accountopenpage.OpenAccountPageRequestBean;
import com.hyjf.api.server.user.auth.mergeauth.MergeAuthPagePlusRequestBean;
import com.hyjf.api.server.user.auth.paymentauth.PaymentAuthPagePlusRequestBean;
import com.hyjf.api.server.user.auth.repayauth.RepayAuthPlusRequestBean;
import com.hyjf.api.server.user.authquery.AutoStateQueryDefine;
import com.hyjf.api.server.user.authquery.AutoStateQueryRequestBean;
import com.hyjf.api.server.user.autoup.AutoPlusRequestBean;
import com.hyjf.api.server.user.bankcard.ThirdPartyBankCardRequestBean;
import com.hyjf.api.server.user.bindcardpage.BindCardPageRequestBean;
import com.hyjf.api.server.user.cashauth.CashAuthRequestBean;
import com.hyjf.api.server.user.directrecharge.UserDirectRechargeDefine;
import com.hyjf.api.server.user.directrecharge.UserDirectRechargeRequestBean;
import com.hyjf.api.server.user.encryptpage.BankOpenEncryptPageDefine;
import com.hyjf.api.server.user.encryptpage.BankOpenEncryptPageRequestBean;
import com.hyjf.api.server.user.evaluation.ThirdPartyEvaluationRequestBean;
import com.hyjf.api.server.user.nonwithdraw.NonCashWithdrawDefine;
import com.hyjf.api.server.user.nonwithdraw.NonCashWithdrawRequestBean;
import com.hyjf.api.server.user.nonwithdraw.NonCashWithdrawResultBean;
import com.hyjf.api.server.user.openaccount.UserOpenAccountRequestBean;
import com.hyjf.api.server.user.openaccountplus.OpenAccountPlusDefine;
import com.hyjf.api.server.user.openaccountplus.OpenAccountPlusRequest;
import com.hyjf.api.server.user.paymentauthpage.PaymentAuthPageRequestBean;
import com.hyjf.api.server.user.recharge.UserRechargeRequestBean;
import com.hyjf.api.server.user.register.UserRegisterRequestBean;
import com.hyjf.api.server.user.registeropenaccount.UserRegisterAndOpenAccountRequestBean;
import com.hyjf.api.server.user.repay.RepayParamBean;
import com.hyjf.api.server.user.repayauth.RepayAuthRequestBean;
import com.hyjf.api.server.user.synbalance.ThirdPartySynBalanceRequestBean;
import com.hyjf.api.server.user.syncuserinfo.SyncUserInfoRequest;
import com.hyjf.api.server.user.tenderauth.TenderAuthDefine;
import com.hyjf.api.server.user.tenderauth.TenderAuthRequestBean;
import com.hyjf.api.server.user.transpassword.ThirdPartyTransPasswordRequestBean;
import com.hyjf.api.server.user.trusteepay.TrusteePayRequestBean;
import com.hyjf.api.server.user.unbindcardpage.UnbindCardPageDefine;
import com.hyjf.api.server.user.unbindcardpage.UnbindCardPageRequestBean;
import com.hyjf.api.server.user.withdraw.UserWithdrawDefine;
import com.hyjf.api.server.user.withdraw.UserWithdrawRequestBean;
import com.hyjf.api.surong.borrow.repay.info.BorrowRepayInfoParamBean;
import com.hyjf.api.surong.borrow.status.BorrowInfoSynParamBean;
import com.hyjf.api.web.plan.PlanBean;
import com.hyjf.api.web.planaccount.PlanAccountBean;
import com.hyjf.base.bean.BaseBean;
import com.hyjf.base.bean.BaseDefine;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.result.ResultBean;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.CheckUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.coupon.loans.CouponLoansBean;
import com.hyjf.coupon.myaccount.MyAccountBean;
import com.hyjf.coupon.mycoupon.CouponBean;
import com.hyjf.coupon.repay.CouponRepayBean;
import com.hyjf.financialadvisor.FinancialAdvisorBean;
import com.hyjf.invest.InvestBean;
import com.hyjf.plan.coupon.PlanCouponBean;
import com.hyjf.user.synbalance.SynBalanceRequestBean;
import com.hyjf.vip.apply.ApplyBean;
import com.hyjf.vip.apply.ApplyDefine;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

/**
 * <p>
 * BaseController
 * </p>
 *
 * @author gogtz
 * @version 1.0.0
 */
public class BaseController extends MultiActionController {
    private Logger logger = LoggerFactory.getLogger(BaseController.class);
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new HtmlCleanEditor(true, true));
		binder.registerCustomEditor(Date.class, new DateEditor(true));
	}

	@ResponseBody
	@ExceptionHandler({ Exception.class })
	public ModelAndView exception(Exception e) {
		e.printStackTrace();
		ModelAndView mav = new ModelAndView("");
		mav.addObject("status", "1");
		mav.addObject("statusDesc", "请求发生异常");
		return mav;
	}

	/**
	 * 验证签名
	 *
	 * @param paramBean
	 * @return
	 */
	protected boolean checkSign(BaseBean paramBean, String methodName) {
		// Class<? extends BaseBean> c = paramBean.getClass();
		String sign = StringUtils.EMPTY;
		String accessKey = PropUtils.getSystem(ApplyDefine.AOP_INTERFACE_ACCESSKEY);
		if (BaseDefine.METHOD_APPLY_CHECK.equals(methodName)) {
			// 用户购买会员--校验接口
			ApplyBean reflectionBean = (ApplyBean) paramBean;
			Integer userId = reflectionBean.getUserId();
			Long timestamp = reflectionBean.getTimestamp();
			sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + userId + timestamp + accessKey));

		} else if (BaseDefine.METHOD_VIP_APPLY.equals(methodName)) {
			// 用户购买会员--购买
			ApplyBean reflectionBean = (ApplyBean) paramBean;
			Integer userId = reflectionBean.getUserId();
			String platform = reflectionBean.getPlatform();
			String callBackUrl = reflectionBean.getCallBackUrl();
			Long timestamp = reflectionBean.getTimestamp();
			sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + userId + platform + callBackUrl + timestamp + accessKey));
		}else if(BaseDefine.METHOD_USER_VIP_DETAIL_ACTIVE_INIT.equals(methodName)){
            Integer userId = paramBean.getUserId();
            Long timestamp = paramBean.getTimestamp();
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + userId + timestamp + accessKey));

        }else if(BaseDefine.METHOD_VIP_LEVEL_CAPTION_ACTIVE_INIT.equals(methodName)){
            Integer userId = paramBean.getUserId();
            Long timestamp = paramBean.getTimestamp();
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + userId + timestamp + accessKey));
        } else if (BaseDefine.METHOD_GET_INVEST_INFO_MAPPING.equals(methodName)) {
            //根据出借项目id获取出借信息
            InvestBean investBean = (InvestBean) paramBean;
            String borrowNid = investBean.getBorrowNid();
            String money = investBean.getMoney() == null ? "" : investBean.getMoney();
            String platform = investBean.getPlatform();
            String couponGrantId = investBean.getCouponGrantId() == null ? "" : investBean.getCouponGrantId();
            Integer userId = investBean.getUserId();
            Long timestamp = investBean.getTimestamp();
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + userId + timestamp + borrowNid +
                    money + platform + couponGrantId + accessKey));
        } else if (BaseDefine.METHOD_GET_PROJECT_AVAILABLE_USER_COUPON_ACTION.equals(methodName)) {
            //根据borrowNid和用户id获取用户可用优惠券和不可用优惠券列表
            InvestBean investBean = (InvestBean) paramBean;
            Integer userId = investBean.getUserId();
            Long timestamp = investBean.getTimestamp();
            String platform = investBean.getPlatform();
            String borrowNid = investBean.getBorrowNid();
            String money = investBean.getMoney() == null ? "" : investBean.getMoney();
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + userId + borrowNid +
                    money + platform + timestamp + accessKey));

        } else if (BaseDefine.METHOD_CHECK_PARAM_ACTION.equals(methodName)) {
            //出借校验
            InvestBean investBean = (InvestBean) paramBean;
            Integer userId = investBean.getUserId();
            Long timestamp = investBean.getTimestamp();
            String borrowNid = investBean.getBorrowNid();
            String platform = investBean.getPlatform();
            String couponGrantId = investBean.getCouponGrantId() == null ? "" : investBean.getCouponGrantId();
            String money = investBean.getMoney() == null ? "" : investBean.getMoney();
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + userId + borrowNid +
                    money + platform + couponGrantId + timestamp + accessKey));

        } else if (BaseDefine.METHOD_VALIDATE_COUPON_ACTION.equals(methodName)) {
            //优惠券出借校验
            InvestBean investBean = (InvestBean) paramBean;
            Integer userId = investBean.getUserId();
            Long timestamp = investBean.getTimestamp();
            String borrowNid = investBean.getBorrowNid();
            String platform = investBean.getPlatform();
            String couponGrantId = investBean.getCouponGrantId() == null ? "" : investBean.getCouponGrantId();
            String money = investBean.getMoney() == null ? "" : investBean.getMoney();
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + userId + borrowNid +
                    money + platform + couponGrantId + timestamp + accessKey));

        } else if (BaseDefine.METHOD_COUPON_TENDER_ACTION.equals(methodName)) {
            //体验金出借
            InvestBean investBean = (InvestBean) paramBean;
            String ip = investBean.getIp();
            String ordId = investBean.getOrdId() == null ? "" : investBean.getOrdId();
            int couponOldTime = investBean.getCouponOldTime();
            Integer userId = investBean.getUserId();
            Long timestamp = investBean.getTimestamp();
            String borrowNid = investBean.getBorrowNid();
            String platform = investBean.getPlatform();
            String couponGrantId = investBean.getCouponGrantId() == null ? "" : investBean.getCouponGrantId();
            String money = investBean.getMoney() == null ? "" : investBean.getMoney();
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + userId + borrowNid +
                    money + platform + couponGrantId + ip + ordId + couponOldTime + timestamp + accessKey));

        } else if (BaseDefine.METHOD_COUPON_USER_LIST.equals(methodName)) {
            //我的优惠券列表
            CouponBean couponBean = (CouponBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + couponBean.getUserId() + couponBean.getCouponStatus() + couponBean.getPage() + couponBean.getPageSize() + couponBean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_COUPON_USER_DETAIL.equals(methodName)) {
            //我的优惠券详情页
            CouponBean couponBean = (CouponBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + couponBean.getId() + couponBean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_MYACCOUNT_COUPONINFO.equals(methodName)) {
            //我的账户优惠券信息
            MyAccountBean accountBean = (MyAccountBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + accountBean.getUserId() + accountBean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_MYACCOUNT_VIPINFO.equals(methodName)) {
            //我的账户vip信息
            MyAccountBean accountBean = (MyAccountBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + accountBean.getUserId() + accountBean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_MYTENDER_INVEST_LIST.equals(methodName)
                || BaseDefine.METHOD_MYTENDER_REPAY_LIST.equals(methodName) || BaseDefine.METHOD_MYTENDER_REPAYED_LIST.equals(methodName)) {
            //我的出借列表
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + paramBean.getUserId() + paramBean.getPage() + paramBean.getPageSize() + paramBean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_COUPON_REPAY.equals(methodName)) {
            // 优惠券自动还款
            CouponRepayBean repayBean = (CouponRepayBean) paramBean;
            Long timestamp = repayBean.getTimestamp();
            String borrowNid = repayBean.getBorrowNid();
            Integer periodNow = repayBean.getPeriodNow();
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + borrowNid +
                    periodNow + timestamp + accessKey));
        } else if (BaseDefine.METHOD_INVITE_USER.equals(methodName)) {
            // 10月份MGM活动-注册开户送推荐星
            RegistRecommendBean registRecommendBean = (RegistRecommendBean) paramBean;
            Integer inviteUser = registRecommendBean.getInviteUser();
            Integer inviteByUser = registRecommendBean.getInviteByUser();
            Long timestamp = registRecommendBean.getTimestamp();
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + inviteUser + inviteByUser + timestamp + accessKey));
        } else if (BaseDefine.METHOD_PRIZE_CHANGE_LIST.equals(methodName)) {
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + paramBean.getUserId() + paramBean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_DO_PRIZE_CHANGE.equals(methodName)) {
            PrizeChangeBean bean = (PrizeChangeBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getUserId() + bean.getGroupCode() + bean.getChangeCount() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_PRIZE_CHANGE_CHECK.equals(methodName)) {
            PrizeChangeBean bean = (PrizeChangeBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getUserId() + bean.getGroupCode() + bean.getChangeCount() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_PRIZE_DRAW_LIST.equals(methodName)) {
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + paramBean.getUserId() + paramBean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_DO_PRIZE_DRAW.equals(methodName)) {
            PrizeDrawBean bean = (PrizeDrawBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getUserId() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_GET_USER_RECOMMEND_INFO.equals(methodName)) {
            RecommendBean bean = (RecommendBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getUserId() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_GET_USER_RECOMMEND_STAR_PRIZE_LIST.equals(methodName)) {
            RecommendBean bean = (RecommendBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getUserId() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_GET_USER_RECOMMEND_STAR_USED_PRIZE_LIST.equals(methodName)) {
            RecommendBean bean = (RecommendBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getUserId() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_GET_USER_FLAG.equals(methodName)) {
            RecommendBean bean = (RecommendBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getUserId() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_GET_ACTLIST.equals(methodName)) {
            ActivityListDataBean bean = (ActivityListDataBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_COUPON_LOANS.equals(methodName)) {
            // 优惠券自动放款
            CouponLoansBean bean = (CouponLoansBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getBorrowNid() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_VIP_VALUE.equals(methodName)) {
            // 出借放款更新V值
            CouponLoansBean bean = (CouponLoansBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getNid() + bean.getUserId() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_COUPON_REPAY_TYJ.equals(methodName)) {
            // 体验金按照收益期限还款
            CouponRepayBean bean = (CouponRepayBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_CARD_SEND.equals(methodName)) {
            WealthCardBean bean = (WealthCardBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getUserId() + bean.getPhoneNum() + bean.getCardIdentifier() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_PHONENUM_CHECK.equals(methodName)) {
            WealthCardBean bean = (WealthCardBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getPhoneNum() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_NEWYEAR_PRIZEDRAW.equals(methodName)) {
            WealthCardBean bean = (WealthCardBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getUserId() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_GET_USERCARD_COUNT.equals(methodName)) {
            WealthCardBean bean = (WealthCardBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getUserId() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_GET_PRESENT_RIDDLES.equals(methodName)) {
            LanternFestivalBean bean = (LanternFestivalBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_GET_TODAY_USER_ANSWER_FLAG.equals(methodName)) {
            LanternFestivalBean bean = (LanternFestivalBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + (bean.getUserId() == null ? "" : bean.getUserId()) + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_GET_USER_PRESENT_CUMULATIVE_COUPON.equals(methodName)) {
            LanternFestivalBean bean = (LanternFestivalBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + (bean.getUserId() == null ? "" : bean.getUserId()) + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_GET_USER_LANTERN_ILLUMINE_LIST.equals(methodName)) {
            LanternFestivalBean bean = (LanternFestivalBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + (bean.getUserId() == null ? "" : bean.getUserId()) + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_INSERT_USER_ANSWER_RECORD_INIT.equals(methodName)) {
            LanternFestivalBean bean = (LanternFestivalBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + (bean.getUserId() == null ? "" : bean.getUserId()) + (bean.getQuestionId() == null ? "" : bean.getQuestionId()) + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_UPDATE_USER_ANSWER_RECORD.equals(methodName)) {
            LanternFestivalBean bean = (LanternFestivalBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + (bean.getUserId() == null ? "" : bean.getUserId()) + (bean.getQuestionId() == null ? "" : bean.getQuestionId()) + bean.getUserAnswer() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_CHECK.equals(methodName)) {
            LanternFestivalBean bean = (LanternFestivalBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + (bean.getUserId() == null ? "" : bean.getUserId()) + (bean.getQuestionId() == null ? "" : bean.getQuestionId()) + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_NEWYEAR_TENDER.equals(methodName)) {
            // 出借送财神卡
            GetCardBean bean = (GetCardBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getTenderNid() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_NEWYEAR_SEND_CARD.equals(methodName)) {
            // 注册且开户发放财神卡
            GetCardBean bean = (GetCardBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_NEWYEAR_REGIST.equals(methodName)) {
            // 活动期内注册或邀请好友注册
            GetCardBean bean = (GetCardBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getUserId() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_COUPON_REPAY_HTJ.equals(methodName)) {
            // 汇添金优惠券自动还款
            CouponRepayBean repayBean = (CouponRepayBean) paramBean;
            Long timestamp = repayBean.getTimestamp();
            String planNid = repayBean.getPlanNid();
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + planNid + timestamp + accessKey));
        } else if (BaseDefine.METHOD_PLAN_GET_PROJECT_AVAILABLE_USER_COUPON_ACTION.equals(methodName)) {
            //根据borrowNid和用户id获取用户可用优惠券和不可用优惠券列表
            PlanCouponBean planCouponBean = (PlanCouponBean) paramBean;
            Integer userId = planCouponBean.getUserId();
            Long timestamp = planCouponBean.getTimestamp();
            String platform = planCouponBean.getPlatform();
            String planNid = planCouponBean.getPlanNid();
            String money = planCouponBean.getMoney() == null ? "" : planCouponBean.getMoney();
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + planNid + userId + money + platform + timestamp + accessKey));

        } else if (BaseDefine.METHOD_PLAN_GET_USER_COUPON_AVAILABLE_COUNT.equals(methodName)) {
            //根据borrowNid和用户id获取用户可用优惠券和不可用优惠券列表
            PlanCouponBean planCouponBean = (PlanCouponBean) paramBean;
            Integer userId = planCouponBean.getUserId();
            Long timestamp = planCouponBean.getTimestamp();
            String platform = planCouponBean.getPlatform();
            String planNid = planCouponBean.getPlanNid();
            String money = planCouponBean.getMoney() == null ? "" : planCouponBean.getMoney();
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + planNid + userId + money + platform + timestamp + accessKey));

        } else if (BaseDefine.METHOD_PLAN_GET_BEST_COUPON.equals(methodName)) {
            //根据borrowNid和用户id获取用户可用优惠券和不可用优惠券列表
            PlanCouponBean planCouponBean = (PlanCouponBean) paramBean;
            Integer userId = planCouponBean.getUserId();
            Long timestamp = planCouponBean.getTimestamp();
            String platform = planCouponBean.getPlatform();
            String planNid = planCouponBean.getPlanNid();
            String money = planCouponBean.getMoney() == null ? "" : planCouponBean.getMoney();
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + planNid + userId + money + platform + timestamp + accessKey));

        } else if (BaseDefine.METHOD_PLAN_GET_COUPON_INTEREST.equals(methodName)) {
            //根据borrowNid和用户id获取用户可用优惠券和不可用优惠券列表
            PlanCouponBean planCouponBean = (PlanCouponBean) paramBean;
            Long timestamp = planCouponBean.getTimestamp();
            String couponId = planCouponBean.getCouponGrantId();
            String planNid = planCouponBean.getPlanNid();
            String money = planCouponBean.getMoney() == null ? "" : planCouponBean.getMoney();
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + planNid + couponId + money + timestamp + accessKey));

        } else if (BaseDefine.METHOD_PLAN_COUNT_COUPON_USERS.equals(methodName)) {
            //根据borrowNid和用户id获取用户可用优惠券和不可用优惠券列表
            PlanCouponBean planCouponBean = (PlanCouponBean) paramBean;
            Long timestamp = planCouponBean.getTimestamp();
            String usedFlag = planCouponBean.getUsedFlag();
            Integer userId = planCouponBean.getUserId();
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + userId + usedFlag + timestamp + accessKey));

        } else if (BaseDefine.METHOD_PLAN_COUPON_TENDER_ACTION.equals(methodName)) {
            //根据borrowNid和用户id获取用户可用优惠券和不可用优惠券列表
            PlanCouponBean planCouponBean = (PlanCouponBean) paramBean;
            String ip = planCouponBean.getIp();
            String ordId = planCouponBean.getOrdId() == null ? "" : planCouponBean.getOrdId();
            int couponOldTime = planCouponBean.getCouponOldTime();
            Integer userId = planCouponBean.getUserId();
            Long timestamp = planCouponBean.getTimestamp();
            String planNid = planCouponBean.getPlanNid();
            String platform = planCouponBean.getPlatform();
            String couponGrantId = planCouponBean.getCouponGrantId() == null ? "" : planCouponBean.getCouponGrantId();
            String money = planCouponBean.getMoney() == null ? "" : planCouponBean.getMoney();
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + userId + planNid + money + platform
                    + couponGrantId + ip + ordId + couponOldTime + timestamp + accessKey));

        } else if (BaseDefine.METHOD_PLAN_COUPON_RECOVER.equals(methodName)) {
            //
            PlanCouponBean planCouponBean = (PlanCouponBean) paramBean;
            String planNid = planCouponBean.getPlanNid();
            Long timestamp = planCouponBean.getTimestamp();
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + planNid + timestamp + accessKey));

        } else if (BaseDefine.METHOD_SEARCH_PLAN_LIST.equals(methodName)) {
            PlanBean bean = (PlanBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_SEARCH_PLAN_DETAIL.equals(methodName)) {
            PlanBean bean = (PlanBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getPlanNid() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_SEARCH_PLAN_ACCEDE.equals(methodName)) {
            PlanBean bean = (PlanBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getPlanNid() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_SEARCH_PLAN_BORROW.equals(methodName)) {
            PlanBean bean = (PlanBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getPlanNid() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_GET_PLAN_INVEST_INFO.equals(methodName)) {
            PlanBean bean = (PlanBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getPlanNid() + bean.getUserId() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_GET_PLAN_INVEST_EARNINGS.equals(methodName)) {
            PlanBean bean = (PlanBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getPlanNid() + bean.getAccount() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_APPOINT_CHECK.equals(methodName)) {
            PlanBean bean = (PlanBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getPlanNid() + bean.getAccount() + bean.getUserId() + bean.getTimestamp()
                    + accessKey));
        } else if (BaseDefine.METHOD_JOIN_PLAN.equals(methodName)) {
            PlanBean bean = (PlanBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getPlanNid() + bean.getAccount() + bean.getUserId() + bean.getTimestamp()
                    + accessKey));
        } else if (BaseDefine.METHOD_APPOINTMENT.equals(methodName)) {
            PlanBean bean = (PlanBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getUserId() + bean.getAppointment() + bean.getCallback()
                    + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_SEARCH_USER_PROJECT_LIST.equals(methodName)) {
            PlanAccountBean bean = (PlanAccountBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getPlanStatus() + bean.getUserId() + bean.getTimestamp()
                    + accessKey));
        } else if (BaseDefine.METHOD_CHECK_APPOINTMENT.equals(methodName)) {
            PlanBean bean = (PlanBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getUserId() + bean.getAppointment() + bean.getTimestamp()
                    + accessKey));
        } else if (BaseDefine.METHOD_GET_USEREVALATIONRESULT_BY_USERID.equals(methodName)) {
            FinancialAdvisorBean bean = (FinancialAdvisorBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getUserId() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_GET_QUESTION_LIST.equals(methodName)) {
            FinancialAdvisorBean bean = (FinancialAdvisorBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getBehaviorId() + bean.getBehavior() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_USER_EVALATION_END.equals(methodName)) {
            FinancialAdvisorBean bean = (FinancialAdvisorBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getUserId() + bean.getBehaviorId() + bean.getUserAnswer() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_USER_EVALUATION_BEHAVIOR.equals(methodName)) {
            FinancialAdvisorBean bean = (FinancialAdvisorBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getBehaviorId() + bean.getBehavior() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_USER_EVALUATION_BEHAVIOR_STATUS.equals(methodName)) {
            FinancialAdvisorBean bean = (FinancialAdvisorBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getUserId() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_BORROW_STATUS_SYN.equals(methodName)) {
            BorrowInfoSynParamBean bean = (BorrowInfoSynParamBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getBorrowNid() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_BORROW_REPAY_CONFIRM.equals(methodName)) {
            BorrowRepayInfoParamBean bean = (BorrowRepayInfoParamBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getBorrowNid() + bean.getUsername() + bean.getMobile() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_BORROW_REPAY.equals(methodName)) {
            BorrowRepayInfoParamBean bean = (BorrowRepayInfoParamBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getBorrowNid() + bean.getUsername() + bean.getMobile() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_BORROW_REPAY_SEARCH.equals(methodName)) {
            BorrowRepayInfoParamBean bean = (BorrowRepayInfoParamBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getBorrowNid() + bean.getRepayPeriod() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_SYNBALANCE.equals(methodName)) {
            SynBalanceRequestBean bean = (SynBalanceRequestBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getUserId() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_SERVER_SYNBALANCE.equals(methodName)) {
            SynBalanceRequestBean bean = (SynBalanceRequestBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getBankAccount() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_REPAY.equals(methodName)) {
            RepayParamBean bean = (RepayParamBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getAccountId() + bean.getBorrowNid() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_SERVER_SET_PASSWORD.equals(methodName)) {
            ThirdPartyTransPasswordRequestBean bean = (ThirdPartyTransPasswordRequestBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getAccountId() + bean.getRetUrl() + bean.getChannel() + bean.getBgRetUrl() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_SERVER_RESET_PASSWORD.equals(methodName)) {
            ThirdPartyTransPasswordRequestBean bean = (ThirdPartyTransPasswordRequestBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getAccountId() + bean.getRetUrl() + bean.getChannel() + bean.getBgRetUrl() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_SERVER_BIND_CARD.equals(methodName)) {
            ThirdPartyBankCardRequestBean bean = (ThirdPartyBankCardRequestBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getAccountId() + bean.getCardNo() + bean.getChannel() + bean.getRetUrl() + bean.getBgRetUrl() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_SERVER_DELETE_CARD.equals(methodName)) {
            ThirdPartyBankCardRequestBean bean = (ThirdPartyBankCardRequestBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getAccountId() + bean.getCardNo() + bean.getChannel() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_SERVER_SEND_SMS.equals(methodName)) {
            UserOpenAccountRequestBean bean = (UserOpenAccountRequestBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getMobile() + bean.getChannel() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_SERVER_REGISTER.equals(methodName)) {
            UserRegisterRequestBean bean = (UserRegisterRequestBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getMobile() + bean.getInstCode() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_SERVER_OPEN_ACCOUNT.equals(methodName)) {
            UserOpenAccountRequestBean bean = (UserOpenAccountRequestBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getMobile() + bean.getTrueName() + bean.getIdNo() + bean.getCardNo() + bean.getSmsCode() + bean.getChannel()
                    + bean.getOrderId() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_SERVER_OPEN_ACCOUNT_SILENT.equals(methodName)) {
            UserOpenAccountRequestBean bean = (UserOpenAccountRequestBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getMobile() + bean.getTrueName() + bean.getIdNo() + bean.getCardNo() + bean.getChannel()
                    + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_SERVER_ASSET_STATUS.equals(methodName)) {
            AssetStatusRequestBean bean = (AssetStatusRequestBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getAssetId() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_SERVER_SEND_RECHARGE_SMS.equals(methodName)) {
            UserRechargeRequestBean bean = (UserRechargeRequestBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getMobile() + bean.getCardNo() + bean.getAccountId() + bean.getChannel() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_SERVER_RECHARGE.equals(methodName)) {
            UserRechargeRequestBean bean = (UserRechargeRequestBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getAccountId() + bean.getCardNo() + bean.getMobile() + bean.getAccount() + bean.getSmsCode() + bean.getChannel() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_REPAY_INFO.equals(methodName)) {
            RepayParamBean bean = (RepayParamBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getAccountId() + bean.getProductId() + bean.getRepayType() + bean.getTimestamp() + bean.getInstCode() + accessKey));
        } else if (BaseDefine.METHOD_REPAY_RESULT.equals(methodName)) {
            RepayParamBean bean = (RepayParamBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getAccountId() + bean.getBorrowNid() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_SERVER_NON_CASH_WITHDRAW.equals(methodName)) {
            NonCashWithdrawRequestBean bean = (NonCashWithdrawRequestBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getAccountId() + bean.getCardNo() + bean.getAccount() + bean.getPlatform() + bean.getChannel() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_SERVER_REGISTER_OPENACCOUNT.equals(methodName)) {
            UserRegisterAndOpenAccountRequestBean bean = (UserRegisterAndOpenAccountRequestBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getMobile() + bean.getTrueName() + bean.getIdCard() + bean.getCardNo() + bean.getInstCode() + bean.getChannel() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_SERVER_CASHWITHDRAWAL.equals(methodName)) {
            CashAuthRequestBean bean = (CashAuthRequestBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getChannel() + bean.getAccountId() + bean.getBitMap() + bean.getAutoBid() == null ? "" : bean.getAutoBid()
                    + bean.getAutoTransfer() == null ? "" : bean.getAutoTransfer() + bean.getDirectConsume() == null ? "" : bean.getDirectConsume() + bean.getAgreeWithdraw() == null ? "" : bean.getAgreeWithdraw() + bean.getNotifyUrl() + bean.getRetUrl() + bean.getTransactionUrl() + accessKey));
        } else if (PushDefine.PUSH_ACTION.equals(methodName)) {
            // 资产推送--校验接口
            PushRequestBean reflectionBean = (PushRequestBean) paramBean;
            String instCode = reflectionBean.getInstCode();
            int assetType = reflectionBean.getAssetType();

            Long timestamp = reflectionBean.getTimestamp();
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + timestamp + instCode + assetType + accessKey));

        } else if (NonCashWithdrawDefine.NON_CASH_WITHDRAW_ACTION.equals(methodName)) {
            // 免密提现--校验接口
            NonCashWithdrawRequestBean reflectionBean = (NonCashWithdrawRequestBean) paramBean;
            String accountId = reflectionBean.getAccount();
            String cardNo = reflectionBean.getCardNo();
            String account = reflectionBean.getAccount();
            String channel = reflectionBean.getChannel();
            Long timestamp = reflectionBean.getTimestamp();
            sign = accountId + cardNo + account + channel + timestamp;
        } else if (BaseDefine.GET_USER_QUESTION_ACTION.equals(methodName)) {
            // 十月份活动2 根据用户ID加载答题信息
            ActQuestionTenRequestBean bean = (ActQuestionTenRequestBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.ANSWER_CHECK_ACTION.equals(methodName)) {
            // 十月份活动2 用户答题
            ActQuestionTenRequestBean bean = (ActQuestionTenRequestBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getUserId() + bean.getAnswer() + bean.getQuestionId() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.GET_ACTIVITY_LIST.equals(methodName)) {
            // 十月份活动1 签到活动
            BaseBean bean = (BaseBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getUserId() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.JAN_GET_USER_QUESTION_ACTION.equals(methodName)) {
            // 双十一活动 我画你猜
            ActDrawGuessRequestBean bean = (ActDrawGuessRequestBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.JAN_ANSWER_CHECK_ACTION.equals(methodName)) {
            // 双十一活动 我画你猜 答题
            ActDrawGuessRequestBean bean = (ActDrawGuessRequestBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getUserId() + bean.getAnswer() + bean.getQuestionId() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.GRAB_COUPONS_ACTION.equals(methodName)) {
            // 双十一活动 拼手气抢优惠券接口
            FightLuckResultBean bean = (FightLuckResultBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getUserId() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_DO_BARGAIN.equals(methodName)) {
            // 双十一活动 砍价接口
            BargainRequestBean bean = (BargainRequestBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getWechatId() + bean.getWechatIdHelp() + String.valueOf(bean.getPrizeId()) + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_DO_BARGAIN_DOUBLE.equals(methodName)) {
            // 双十一活动 砍价翻倍接口
            BargainDoubleRequestBean bean = (BargainDoubleRequestBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + String.valueOf(bean.getIdBargain()) + String.valueOf(bean.getPrizeId()) + bean.getPhoneNum() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_DO_PRIZE_BUY.equals(methodName)) {
            // 双十一活动 奖品购买接口
            PrizeBuyRequestBean bean = (PrizeBuyRequestBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getWechatId() + bean.getPrizeId() + bean.getBookingMobile() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_DO_SMSCODE_SEND.equals(methodName)) {
            // 双十一活动 奖品购买接口
            SmsCodeRequestBean bean = (SmsCodeRequestBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getSmsCodeType() + bean.getPhoneNum() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.GET_WEEKLY_DATA.equals(methodName)) {
            // 周运营活动
            BaseBean bean = (BaseBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getUserId() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.GET_ACT_CORPS.equals(methodName)) {
            // 周运营活动
            BaseBean bean = (BaseBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.METHOD_DO_BALLOON_RECEIVE.equals(methodName)) {
            // 周运营活动
            BaseBean bean = (BaseBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getUserId() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.GET_ACT_2018SPRING.equals(methodName)) {
            // 周运营活动
            BaseBean bean = (BaseBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getUserId() + bean.getTimestamp() + accessKey));
        } else if (BaseDefine.GET_ACT_LIST.equals(methodName)) {
            // 周运营活动
            BaseBean bean = (BaseBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getTimestamp() + accessKey));
        } else if (ListedTwoActDefine.REQUEST_MAPPING.equals(methodName)) {
            // 上市活动2
            BaseBean bean = (BaseBean) paramBean;
            sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getTimestamp() + accessKey));
        }
        return StringUtils.equals(sign, paramBean.getChkValue()) ? true : false;
    }




    /**
     * AEMS验证外部请求签名
     * jijun 20180905
     * @param paramBean
     * @return
     */
    protected boolean AEMSVerifyRequestSign(BaseBean paramBean, String methodName) {

        String sign = StringUtils.EMPTY;

        // 机构编号必须参数
        String instCode = paramBean.getInstCode();
        if (StringUtils.isEmpty(instCode)) {
            return false;
        }

        if ((AemsAutoTenderDefine.REQUEST_MAPPING + AemsAutoTenderDefine.AUTOTENDER_ACTION).equals(methodName)) {
            //AEMS自动投资
            AemsAutoTenderRequestBean bean = (AemsAutoTenderRequestBean) paramBean;
            sign = bean.getInstCode() + bean.getAccountId() + bean.getBorrowNid() + bean.getTimestamp();
        } else if ((AemsUserRegisterDefine.REQUEST_MAPPING + AemsUserRegisterDefine.REGISTER_ACTION ).equals(methodName)) {
            // 用户注册
            AemsUserRegisterRequestBean bean = (AemsUserRegisterRequestBean) paramBean;
            sign = bean.getMobile() + bean.getInstCode() + bean.getTimestamp();
        } else if ((AemsTradeListDefine.REQUEST_MAPPING + AemsTradeListDefine.TRADELIST_ACTION).equals(methodName)) {
            //aems交易明细查询验签
            AemsTradeListBean bean = (AemsTradeListBean) paramBean;
            sign = bean.getInstCode() + bean.getTimestamp() + bean.getPhone() + bean.getAccountId();//暂定四个参数
        } else if ((AemsBorrowListDefine.REQUEST_MAPPING + AemsBorrowListDefine.BORROW_LIST_ACTION).equals(methodName)) {
            //aems标的列表查询
            AemsBorrowListRequestBean bean = (AemsBorrowListRequestBean) paramBean;
            sign = bean.getInstCode() + bean.getBorrowStatus() + bean.getTimestamp();
        } else if ((AemsBorrowDetailDefine.REQUEST_MAPPING + AemsBorrowDetailDefine.BORROW_DETAIL_ACTION).equals(methodName)) {
            //aems标的详情查询
            AemsBorrowDetailRequestBean bean = (AemsBorrowDetailRequestBean) paramBean;
            sign = bean.getInstCode() + bean.getBorrowNid() + bean.getTimestamp();
        } else if ((AemsInvestDefine.REQUEST_MAPPING + AemsInvestDefine.REPAY_LIST).equals(methodName)) {
            //aems回款记录
            AemsRepayListRequest bean = (AemsRepayListRequest) paramBean;
            sign = bean.getInstCode() + bean.getStartTime() + bean.getEndTime() + bean.getTimestamp();
        } else if ((AemsInvestDefine.REQUEST_MAPPING + AemsInvestDefine.INVEST_LIST).equals(methodName)) {
            //aems投资记录查询
            AemsInvestListRequest bean = (AemsInvestListRequest) paramBean;
            sign = bean.getInstCode() + bean.getStartTime() + bean.getEndTime() + bean.getTimestamp();
        } else if ((AemsInvestDefine.REQUEST_MAPPING + AemsInvestDefine.INVEST_REPAY_LIST).equals(methodName)) {
            //aems获取回款记录信息
            AemsInvestRepayBean bean = (AemsInvestRepayBean) paramBean;
            sign = bean.getInstCode() + bean.getTimestamp();
        } else if ((AemsTrusteePayDefine.REQUEST_MAPPING + AemsTrusteePayDefine.TRUSTEE_PAY_ACTION).equals(methodName)) {
            //aems借款人受托支付申请
            AemsTrusteePayRequestBean bean = (AemsTrusteePayRequestBean) paramBean;
            sign = bean.getChannel() + bean.getAccountId() + bean.getProductId() + bean.getIdType() + bean.getIdNo()
                    + bean.getReceiptAccountId() + bean.getForgotPwdUrl() + bean.getRetUrl() + bean.getNotifyUrl() + bean.getTimestamp();
        } else if ((AemsTrusteePayDefine.REQUEST_MAPPING + AemsTrusteePayDefine.TRUSTEEPAYQUERY_ACTION).equals(methodName)) {
            //aems借款人受托支付申请查询
            AemsTrusteePayRequestBean bean = (AemsTrusteePayRequestBean) paramBean;
            sign = bean.getChannel() + bean.getAccountId() + bean.getProductId() + bean.getTimestamp();
        } else if ((AemsAuthStatusQueryDefine.REQUEST_MAPPING + AemsAuthStatusQueryDefine.AUTH_STATUS_QUERY_ACTION).equals(methodName)) {
            // aems授权状态查询
            AemsAuthStatusQueryRequestBean bean = (AemsAuthStatusQueryRequestBean) paramBean;
            sign = bean.getInstCode() + bean.getAccountId() + bean.getTimestamp();
        } else if ((AemsPushDefine.REQUEST_MAPPING + AemsPushDefine.PUSH_ACTION).equals(methodName)) {
            //aems资产推送个人-校验接口
            AemsPushRequestBean bean = (AemsPushRequestBean) paramBean;
            sign = bean.getTimestamp() + bean.getInstCode() + bean.getAssetType();
        } else if ((AemsPushDefine.REQUEST_MAPPING + AemsPushDefine.PUSH_COMPANY_ACTION).equals(methodName)) {
            //aems资产推送公司-校验接口
            AemsPushRequestBean bean = (AemsPushRequestBean) paramBean;
            sign = bean.getTimestamp() + bean.getInstCode() + bean.getAssetType();
        } else if ((AemsOrganizationStructureDefine.REQUEST_MAPPING + AemsOrganizationStructureDefine.ORGANIZATION_LIST).equals(methodName)) {
            //aems获取集团组织架构信息
            AemsOrganizationStructureBean bean = (AemsOrganizationStructureBean) paramBean;
            sign = bean.getInstCode() + bean.getTimestamp();
        } else if ((AemsBindCardPageDefine.REQUEST_MAPPING + AemsBindCardPageDefine.BIND_CARD_PAGE).equals(methodName)) {
            //aems用户页面绑卡
            AemsBindCardPageRequestBean bean = (AemsBindCardPageRequestBean) paramBean;
            sign = bean.getInstCode() + bean.getAccountId() + bean.getTimestamp();
        }  else if ((AemsUnbindCardPageDefine.REQUEST_MAPPING+AemsUnbindCardPageDefine.DELETE_CARD).equals(methodName)) {
            //aems用户页面解绑卡
            AemsUnbindCardPageRequestBean bean = (AemsUnbindCardPageRequestBean) paramBean;
            sign = bean.getInstCode()+ bean.getAccountId() + bean.getMobile() + bean.getCardNo()+bean.getTimestamp();
        } else if ((AemsRepayDefine.REQUEST_MAPPING + AemsRepayDefine.REPAY_INFO_ACTION).equals(methodName)) {
            //aems获得标的还款计划
            AemsRepayParamBean bean = (AemsRepayParamBean) paramBean;
            sign = bean.getRepayType() + bean.getInstCode() + bean.getTimestamp();
            logger.info("-------------------aems获得标的还款计划sign:"+sign);
        } else if ((AemsUserDirectRechargeDefine.REQUEST_MAPPING + AemsUserDirectRechargeDefine.RECHARGE_ACTION).equals(methodName)) {
            //aems充值页面
            UserDirectRechargeRequestBean bean = (UserDirectRechargeRequestBean) paramBean;
            logger.info("-------------------aems充值bean:"+JSONObject.toJSONString(bean));
			sign = bean.getInstCode() + bean.getAccountId() + bean.getMobile() + bean.getIdNo() + bean.getCardNo()
					+ bean.getTxAmount() + bean.getName() + bean.getRetUrl() + bean.getBgRetUrl() + bean.getTimestamp();
            logger.info("-------------------aems充值sign:"+sign);
        } else if ((AemsTransPasswordDefine.REQUEST_MAPPING + BaseDefine.METHOD_AEMS_SET_PASSWORD).equals(methodName)) {
            //aems设置&重置交易密码
            AemsTransPasswordRequestBean bean = (AemsTransPasswordRequestBean) paramBean;
            sign = bean.getChannel() + bean.getAccountId() + bean.getInstCode() + bean.getTimestamp();
        } /*else if ((AemsTrusteePayDefine.REQUEST_MAPPING + AemsTrusteePayDefine.TRUSTEEPAYQUERY_ACTION).equals(methodName)) {
            //aems受托支付申请查询接口
            OrganizationStructureBean bean = (OrganizationStructureBean) paramBean;
            sign = bean.getInstCode() + bean.getTimestamp();
        }*/ else if ((AemsAssetSearchDefine.REQUEST_MAPPING+AemsAssetSearchDefine.STATUS_ACTION).equals(methodName)) {
            //aems资产查询接口
            AemsAssetStatusRequestBean bean = (AemsAssetStatusRequestBean) paramBean;
            sign = bean.getAssetId() + bean.getInstCode() + bean.getTimestamp();
        } else if ((AemsUserWithdrawDefine.REQUEST_MAPPING + AemsUserWithdrawDefine.WITHDRAW_ACTION).equals(methodName)) {
            //aems获取用户提现记录
            AemsUserWithdrawRequestBean bean = (AemsUserWithdrawRequestBean) paramBean;
            logger.info("-------------------aems提现bean:"+JSONObject.toJSONString(bean));
			sign = bean.getChannel() + bean.getAccountId() + bean.getAccount() + bean.getCardNo() + bean.getRetUrl()
					+ bean.getBgRetUrl() + bean.getTimestamp();
            logger.info("-------------------aems提现sign:"+sign);
        } else if ((AemsBorrowRepaymentInfoDefine.REQUEST_MAPPING + AemsBorrowRepaymentInfoDefine.BORROW_LIST_ACTION).equals(methodName)) {
            //aems获取还款明细查询
            AemsBorrowRepaymentInfoBean bean = (AemsBorrowRepaymentInfoBean) paramBean;
            sign = bean.getInstCode() + bean.getAssetId() + bean.getTimestamp();
        } else if ((AemsEvaluationDefine.REQUEST_MAPPING + AemsEvaluationDefine.SAVE_USER_EVALUATION_RESULTS).equals(methodName)) {
            //aems用户风险测评
            AemsEvaluationRequestBean bean = (AemsEvaluationRequestBean) paramBean;
            sign = bean.getInstCode() + bean.getTimestamp();
        } else if ((AemsSyncUserInfoDefine.REQUEST_MAPPING + AemsSyncUserInfoDefine.SYNCUSERINFO_ACTION).equals(methodName)) {
            //aems用户信息查询
            AemsSyncUserInfoRequest bean = (AemsSyncUserInfoRequest) paramBean;
            sign = bean.getInstCode() + bean.getTimestamp();
        } else if ((AemsMergeAuthPagePlusDefine.REQUEST_MAPPING + AemsMergeAuthPagePlusDefine.MERGE_AUTH_ACTION).equals(methodName)) {
            // 多合一授权
            AemsMergeAuthPagePlusRequestBean bean = (AemsMergeAuthPagePlusRequestBean) paramBean;
            sign = bean.getInstCode() + bean.getAccountId() + bean.getAuthType() + bean.getRetUrl() + bean.getNotifyUrl() + bean.getTimestamp();
        } else if ((AemsBankOpenEncryptPageDefine.REQUEST_MAPPING + AemsBankOpenEncryptPageDefine.OPEN_ACCOUNT_ACTION).equals(methodName)) {
            // 用户开户
            AemsBankOpenEncryptPageRequestBean bean = (AemsBankOpenEncryptPageRequestBean) paramBean;
            sign = bean.getInstCode() + bean.getMobile() + bean.getTrueName() + bean.getRetUrl() + bean.getBgRetUrl() + bean.getTimestamp();
        }else if ((AemsBorrowRepayPlanDefine.REQUEST_MAPPING + AemsBorrowRepayPlanDefine.GET_REPAY_PLAN_ACTION).equals(methodName)){
            // 还款计划
            AemsBorrowRepayPlanRequestBean bean = (AemsBorrowRepayPlanRequestBean) paramBean;
            sign =  bean.getRepayType() +  bean.getInstCode() + bean.getTimestamp();
        }else if((AemsBorrowRepayPlanDefine.REQUEST_MAPPING + AemsBorrowRepayPlanDefine.GET_REPAY_PLAN_DETAIL_ACTION).equals(methodName)){
            // 还款计划详情
            AemsBorrowRepayPlanRequestBean bean = (AemsBorrowRepayPlanRequestBean) paramBean;
            sign =  bean.getProductId() + bean.getRepayType() +  bean.getInstCode() + bean.getTimestamp();
        } else if ((AemsSynBalanceDefine.REQUEST_MAPPING + AemsSynBalanceDefine.SYNBALANCE_ACTION).equals(methodName)) {
            // 用户余额查询
            AemsSynBalanceRequestBean bean = (AemsSynBalanceRequestBean) paramBean;
            sign = bean.getAccountId() + bean.getTimestamp();
        }

        // TODO AEMS验签修改
        return ApiSignUtil.verifyByRSA("AEMS", paramBean.getChkValue(), sign);
    }


    /**
     * 验证外部请求签名
     *
     * @param paramBean
     * @return
     */
    protected boolean verifyRequestSign(BaseBean paramBean, String methodName) {

        String sign = StringUtils.EMPTY;

        // 机构编号必须参数
        String instCode = paramBean.getInstCode();
        if (StringUtils.isEmpty(instCode)) {
            return false;
        }

        if (BaseDefine.METHOD_SERVER_CASHWITHDRAWAL.equals(methodName)) {
            CashAuthRequestBean bean = (CashAuthRequestBean) paramBean;
            sign = bean.getAccountId() + (bean.getAcqRes() == null ? "" : bean.getAcqRes()) + (bean.getAgreeWithdraw() == null ? "" : bean.getAgreeWithdraw()) + (bean.getAutoBid() == null ? "" : bean.getAutoBid()) + (bean.getAutoTransfer() == null ? "" : bean.getAutoTransfer()) + bean.getBitMap()
                    + bean.getChannel() + (bean.getDirectConsume() == null ? "" : bean.getDirectConsume()) + bean.getNotifyUrl()
                    + bean.getRetUrl() + bean.getTransactionUrl();
        } else if ((TenderAuthDefine.REQUEST_MAPPING + TenderAuthDefine.TENDER_AUTH).equals(methodName)) {
            TenderAuthRequestBean bean = (TenderAuthRequestBean) paramBean;
            sign = bean.getAccountId() + (bean.getAcqRes() == null ? "" : bean.getAcqRes()) + (bean.getAgreeWithdraw() == null ? "" : bean.getAgreeWithdraw()) + (bean.getAutoBid() == null ? "" : bean.getAutoBid()) + (bean.getAutoTransfer() == null ? "" : bean.getAutoTransfer()) + bean.getBitMap()
                    + bean.getChannel() + (bean.getDirectConsume() == null ? "" : bean.getDirectConsume()) + bean.getNotifyUrl()
                    + bean.getRetUrl() + bean.getTransactionUrl();
        } else if (PushDefine.PUSH_ACTION.equals(methodName)) {
            // 资产推送--校验接口
            PushRequestBean reflectionBean = (PushRequestBean) paramBean;
            int assetType = reflectionBean.getAssetType();
            Long timestamp = reflectionBean.getTimestamp();
            sign = timestamp + instCode + assetType;
        } else if (BaseDefine.METHOD_SERVER_REGISTER.equals(methodName)) {
            // 用户注册
            UserRegisterRequestBean bean = (UserRegisterRequestBean) paramBean;
            sign = bean.getMobile() + bean.getInstCode() + bean.getTimestamp();
            //用户开户
        } else if (BaseDefine.METHOD_SERVER_OPEN_ACCOUNT.equals(methodName)) {
            UserOpenAccountRequestBean bean = (UserOpenAccountRequestBean) paramBean;
            sign = bean.getMobile() + bean.getTrueName() + bean.getIdNo() + bean.getCardNo() + bean.getSmsCode() + bean.getOrderId() + bean.getChannel() + bean.getTimestamp();
        } else if (NonCashWithdrawDefine.NON_CASH_WITHDRAW_ACTION.equals(methodName)) {
            // 免密提现  验签
            NonCashWithdrawRequestBean bean = (NonCashWithdrawRequestBean) paramBean;
            sign = bean.getAccountId() + bean.getCardNo() + bean.getAccount() + bean.getChannel() + bean.getTimestamp();
        } else if (BaseDefine.METHOD_REPAY_INFO.equals(methodName)) {//获取还款计划
            RepayParamBean bean = (RepayParamBean) paramBean;
            sign = bean.getAccountId() + bean.getProductId() + bean.getRepayType() + bean.getInstCode() + bean.getTimestamp();
        } else if (BaseDefine.METHOD_REPAY_RESULT.equals(methodName)) {
            RepayParamBean bean = (RepayParamBean) paramBean;
            sign = bean.getChannel() + bean.getAccountId() + bean.getBorrowNid() + bean.getInstCode() + bean.getTimestamp();
        } else if (BaseDefine.METHOD_SERVER_SET_PASSWORD.equals(methodName)) {
            //设置交易密码验签
            ThirdPartyTransPasswordRequestBean bean = (ThirdPartyTransPasswordRequestBean) paramBean;
            sign = bean.getChannel() + bean.getAccountId() + bean.getRetUrl() + bean.getBgRetUrl() + bean.getTimestamp();
        } else if (BaseDefine.METHOD_SERVER_RESET_PASSWORD.equals(methodName)) {
            //设置交易密码验签
            ThirdPartyTransPasswordRequestBean bean = (ThirdPartyTransPasswordRequestBean) paramBean;
            sign = bean.getChannel() + bean.getAccountId() + bean.getRetUrl() + bean.getBgRetUrl() + bean.getTimestamp();
        } else if (BaseDefine.METHOD_SERVER_SYNBALANCE.equals(methodName)) {
            //设用户余额查询
            ThirdPartySynBalanceRequestBean bean = (ThirdPartySynBalanceRequestBean) paramBean;
            sign = bean.getAccountId() + bean.getTimestamp();
        } else if (BaseDefine.METHOD_SERVER_BIND_CARD_SEND_PLUS_CODE.equals(methodName)) {
            //绑定银行卡发送短信验证码
            ThirdPartyBankCardRequestBean bean = (ThirdPartyBankCardRequestBean) paramBean;
            sign = bean.getInstCode() + bean.getAccountId() + bean.getMobile() + bean.getCardNo() + bean.getTimestamp();
        } else if (BaseDefine.METHOD_SERVER_BIND_CARD.equals(methodName)) {
            //绑定银行卡
            ThirdPartyBankCardRequestBean bean = (ThirdPartyBankCardRequestBean) paramBean;
            sign = bean.getInstCode() + bean.getAccountId() + bean.getLastSrvAuthCode() + bean.getCode() + bean.getMobile() + bean.getCardNo() + bean.getTimestamp();
        } else if (BaseDefine.METHOD_SERVER_DELETE_CARD.equals(methodName)) {
            //设用户余额查询
            ThirdPartyBankCardRequestBean bean = (ThirdPartyBankCardRequestBean) paramBean;
            sign = bean.getChannel() + bean.getAccountId() + bean.getTimestamp();
        } else if (BaseDefine.METHOD_SERVER_ASSET_STATUS.equals(methodName)) {
            AssetStatusRequestBean bean = (AssetStatusRequestBean) paramBean;
            sign = bean.getAssetId() + bean.getTimestamp();
        } else if (BaseDefine.METHOD_SERVER_WITHDRAW.equals(methodName)) {
            // 用户提现
            UserWithdrawRequestBean bean = (UserWithdrawRequestBean) paramBean;
            sign = bean.getChannel() + bean.getAccountId() + bean.getAccount() + bean.getCardNo() + bean.getRetUrl() + bean.getBgRetUrl() + bean.getTimestamp();
        } else if (BaseDefine.METHOD_SERVER_SEND_SMS.equals(methodName)) {
            UserOpenAccountRequestBean bean = (UserOpenAccountRequestBean) paramBean;
            sign = bean.getChannel() + bean.getMobile() + bean.getTimestamp();
        } else if (BorrowListDefine.BORROW_LIST_ACTION.equals(methodName)) {
            BorrowListRequestBean bean = (BorrowListRequestBean) paramBean;
            sign = bean.getInstCode() + bean.getBorrowStatus() + bean.getTimestamp();
        } else if (BaseDefine.METHOD_SERVER_RECHARGE.equals(methodName)) {
            // 短信充值
            UserRechargeRequestBean bean = (UserRechargeRequestBean) paramBean;
            sign = bean.getAccountId() + bean.getCardNo() + bean.getMobile() + bean.getAccount() + bean.getChannel() + bean.getSmsCode() + bean.getTimestamp();
        } else if (BaseDefine.METHOD_SERVER_SEND_RECHARGE_SMS.equals(methodName)) {
            // 短信充值发送验证码
            UserRechargeRequestBean bean = (UserRechargeRequestBean) paramBean;
            sign = bean.getChannel() + bean.getAccountId() + bean.getMobile() + bean.getCardNo() + bean.getTimestamp();
        } else if (BaseDefine.METHOD_SAVE_USER_EVALUATION_RESULTS.equals(methodName)) {
            // 第三方用户测评结果记录
            ThirdPartyEvaluationRequestBean bean = (ThirdPartyEvaluationRequestBean) paramBean;
            sign = bean.getInstCode() + bean.getMobile() + bean.getPlatform() + bean.getTimestamp();
        } else if (TradeListDefine.TRADELIST_ACTION.equals(methodName)) {
            //同步交易明细
            TradeListBean bean = (TradeListBean) paramBean;
            sign = bean.getInstCode() + bean.getTimestamp() + bean.getPhone() + bean.getAccountId();//暂定四个参数
        } else if ((AutoTenderDefine.REQUEST_MAPPING + AutoTenderDefine.AUTOTENDER_ACTION).equals(methodName)) {
            //自动出借
            AutoTenderRequestBean bean = (AutoTenderRequestBean) paramBean;
            sign = bean.getInstCode() + bean.getAccountId() + bean.getBorrowNid() + bean.getTimestamp();
        } else if (BaseDefine.METHOD_SERVER_TRUSTEE_PAY.equals(methodName)) {
            // 借款人受托支付申请
            TrusteePayRequestBean bean = (TrusteePayRequestBean) paramBean;
            sign = bean.getChannel() + bean.getAccountId() + bean.getProductId() + bean.getIdType() + bean.getIdNo()
                    + bean.getReceiptAccountId() + bean.getForgotPwdUrl() + bean.getRetUrl() + bean.getNotifyUrl()
                    + bean.getTimestamp();
        } else if (BaseDefine.METHOD_SERVER_TRUSTEE_PAY_QUERY.equals(methodName)) {
            // 借款人受托支付申请查询
            TrusteePayRequestBean bean = (TrusteePayRequestBean) paramBean;
            sign = bean.getChannel() + bean.getAccountId() + bean.getProductId() + bean.getTimestamp();
        } else if (BorrowDetailDefine.BORROW_DETAIL_ACTION.equals(methodName)){
            BorrowDetailRequestBean bean = (BorrowDetailRequestBean) paramBean;
            sign = bean.getInstCode() + bean.getBorrowNid() + bean.getTimestamp();
        } else if (OpenAccountPlusDefine.METHOD_SERVER_REGISTER.equals(methodName)){
            OpenAccountPlusRequest bean = (OpenAccountPlusRequest) paramBean;
            sign = bean.getMobile() + bean.getInstCode() + bean.getTimestamp();
        } else if (InvestDefine.INVEST_LIST.equals(methodName)){
            InvestListRequest bean = (InvestListRequest) paramBean;
            sign =  bean.getInstCode() + bean.getStartTime() + bean.getEndTime() + bean.getTimestamp();
        } else if (InvestDefine.REPAY_LIST.equals(methodName)) {
            RepayListRequest bean = (RepayListRequest) paramBean;
            sign =  bean.getInstCode() + bean.getStartTime() + bean.getEndTime() + bean.getTimestamp();
        } else if (BaseDefine.METHOD_BORROW_LIST_ACTION.equals(methodName)) {
            // 第三方还款明细查询
            BorrowRepaymentInfoBean bean = (BorrowRepaymentInfoBean) paramBean;
            sign = bean.getInstCode() + bean.getAssetId() + bean.getTimestamp();
        }else if (BaseDefine.METHOD_BORROW_AUTH_SEND_SMS.equals(methodName)) {
            // 自动出借 债转  短信验证码
            AutoPlusRequestBean bean = (AutoPlusRequestBean) paramBean;
            sign = bean.getInstCode() + bean.getAccountId() + bean.getMobile() + bean.getSendType() + bean.getTimestamp();
        }else if (BaseDefine.METHOD_BORROW_AUTH_INVES.equals(methodName)) {
            // 自动出借 增强
            AutoPlusRequestBean bean = (AutoPlusRequestBean) paramBean;
            sign = bean.getInstCode() + bean.getAccountId() + bean.getSmsCode() + bean.getTimestamp();
        }else if (BaseDefine.METHOD_BORROW_AUTH_CREDIT.equals(methodName)) {
            // 自动 债转  授权增强
            AutoPlusRequestBean bean = (AutoPlusRequestBean) paramBean;
            sign = bean.getInstCode() + bean.getAccountId() + bean.getSmsCode() + bean.getTimestamp();
        }else if (BaseDefine.METHOD_SERVER_SYNCUSERINFO.equals(methodName)) {
            //查询用户信息
            SyncUserInfoRequest bean = (SyncUserInfoRequest) paramBean;
            sign = bean.getInstCode() + bean.getTimestamp();
            logger.info("sign is :{}", sign);
        }else if (BaseDefine.ORGANIZATION_LIST.equals(methodName)) {
            // 获取集团组织架构信息
        	OrganizationStructureBean bean = (OrganizationStructureBean) paramBean;
            sign = bean.getInstCode() +  bean.getTimestamp();
        }else if (BaseDefine.EMPINFO_LIST.equals(methodName)) {
            // 获取员工信息
        	OrganizationStructureBean bean = (OrganizationStructureBean) paramBean;
            sign = bean.getInstCode() +  bean.getTimestamp();
        }else if (BaseDefine.INVEST_REPAY_LIST.equals(methodName)) {
            // 获取用户开户信息
        	InvestRepayBean bean = (InvestRepayBean) paramBean;
            sign = bean.getInstCode() +  bean.getTimestamp();
        }else if (BaseDefine.METHOD_PAYMENT_AUTH_PAGE.equals(methodName)) {
            // 缴费授权
            PaymentAuthPageRequestBean bean = (PaymentAuthPageRequestBean) paramBean;
            sign = bean.getInstCode() + bean.getAccountId()+bean.getRetUrl()+bean.getNotifyUrl() + bean.getTimestamp();
        }else if (BaseDefine.METHOD_PAYMENT_AUTH_PAGE_PLUS.equals(methodName)) {
            // 缴费授权(合规)
            PaymentAuthPagePlusRequestBean bean = (PaymentAuthPagePlusRequestBean) paramBean;
            sign = bean.getInstCode() + bean.getAccountId() + bean.getRetUrl() + bean.getNotifyUrl() + bean.getTimestamp();
        } else if (BaseDefine.METHOD_MERGE_AUTH_PAGE_PLUS.equals(methodName)) {
            // 第三方服务接口多合一授权合规(合规)
        	MergeAuthPagePlusRequestBean bean = (MergeAuthPagePlusRequestBean) paramBean;
            sign = bean.getInstCode() + bean.getAccountId()+bean.getAuthType()+bean.getRetUrl()+bean.getNotifyUrl() + bean.getTimestamp();
        }else if (BaseDefine.METHOD_REPAY_AUTH.equals(methodName)) {
            // 还款授权
            RepayAuthRequestBean bean = (RepayAuthRequestBean) paramBean;
            sign = bean.getInstCode() + bean.getAccountId()+bean.getRetUrl()+bean.getNotifyUrl() + bean.getTimestamp();
        }else if (BaseDefine.METHOD_REPAY_AUTH_PLUS.equals(methodName)) {
            // 还款授权(合规)
            RepayAuthPlusRequestBean bean = (RepayAuthPlusRequestBean) paramBean;
            sign = bean.getInstCode() + bean.getAccountId()+bean.getRetUrl()+bean.getNotifyUrl() + bean.getTimestamp();
        }
        else if ((UserDirectRechargeDefine.REQUEST_MAPPING+UserDirectRechargeDefine.RECHARGE_ACTION).equals(methodName)) {
            // 页面充值
            UserDirectRechargeRequestBean bean = (UserDirectRechargeRequestBean) paramBean;
            sign = bean.getInstCode() + bean.getAccountId() + bean.getMobile() + bean.getIdNo() + bean.getCardNo()
                    + bean.getTxAmount() + bean.getName() + bean.getRetUrl() + bean.getBgRetUrl() + bean.getTimestamp();
        }
        else if ((OpenAccountPageDefine.REQUEST_MAPPING+OpenAccountPageDefine.OPEN_ACCOUNT_ACTION).equals(methodName)) {
            // 页面开户
            OpenAccountPageRequestBean bean = (OpenAccountPageRequestBean) paramBean;
            sign = bean.getInstCode() + bean.getMobile() + bean.getIdNo() + bean.getTrueName()
                    + bean.getRetUrl() + bean.getBgRetUrl() + bean.getTimestamp();
        }
        else if ((AutoStateQueryDefine.REQUEST_MAPPING+AutoStateQueryDefine.AUTO_STATE_QUERY_ACTION).equals(methodName)) {
            // 授权状态查询
            AutoStateQueryRequestBean bean = (AutoStateQueryRequestBean) paramBean;
            sign = bean.getInstCode() +bean.getAccountId() + bean.getTimestamp();
        }
        else if ((UserWithdrawDefine.REQUEST_MAPPING+UserWithdrawDefine.GET_USER_WITHDRAW_RECORD_ACTION).equals(methodName)) {
            // 提现记录查询接口
            UserWithdrawRequestBean bean = (UserWithdrawRequestBean) paramBean;
            sign = bean.getInstCode() +bean.getAccountId()+bean.getLimitStart()+bean.getLimitEnd() + bean.getTimestamp();
        } else if (BaseDefine.METHOD_SERVER_BIND_CARD_PAGE.equals(methodName)) {
            // 页面绑卡
            BindCardPageRequestBean bean = (BindCardPageRequestBean) paramBean;
            sign = bean.getInstCode() + bean.getAccountId() + bean.getRetUrl() + bean.getForgotPwdUrl() + bean.getNotifyUrl() + bean.getTimestamp();
        }else if (PushDefine.PUSH_COMPANY_ACTION.equals(methodName)) {
            // 企业资产推送--校验接口
            PushRequestBean bean = (PushRequestBean) paramBean;
            Long timestamp = bean.getTimestamp();
            Integer assetType = bean.getAssetType();
            sign = timestamp + instCode + assetType;
        }else if ((BankOpenEncryptPageDefine.REQUEST_MAPPING + BankOpenEncryptPageDefine.OPEN_ACCOUNT_ACTION).equals(methodName)) {
            // 开户+设密
            BankOpenEncryptPageRequestBean bean = (BankOpenEncryptPageRequestBean) paramBean;
            sign = bean.getInstCode() + bean.getMobile() + bean.getTrueName()
            + bean.getRetUrl() + bean.getBgRetUrl() + bean.getTimestamp();
        }else if ((UnbindCardPageDefine.REQUEST_MAPPING + UnbindCardPageDefine.DELETE_CARD).equals(methodName)) {
            // 解卡(页面调用)合规
            UnbindCardPageRequestBean bean = (UnbindCardPageRequestBean) paramBean;
            sign = bean.getInstCode()+ bean.getAccountId() + bean.getMobile() + bean.getCardNo()+bean.getTimestamp();
        }

        return ApiSignUtil.verifyByRSA(instCode, paramBean.getChkValue(), sign);
	}

	/**
	 * 生成外部返回签名
	 *
	 * @param paramBean
	 * @return
	 */
	protected String genResponseSign(BaseResultBean paramBean, String methodName) {
		// Class<? extends BaseBean> c = paramBean.getClass();
		String sign = StringUtils.EMPTY;

		if (PushDefine.PUSH_ACTION.equals(methodName)) {
			// 资产推送--校验接口
			PushResultBean reflectionBean = (PushResultBean) paramBean;
			String status = reflectionBean.getStatus();
			sign = status;
		}else if(BaseDefine.METHOD_SERVER_CASHWITHDRAWAL.equals(methodName)){
		    // 免密消费授权--校验接口
            String status = paramBean.getStatus();
            String statusDesc = paramBean.getStatusDesc();
            sign = status + statusDesc;
		}else if(NonCashWithdrawDefine.NON_CASH_WITHDRAW_ACTION.equals(methodName)){
		    NonCashWithdrawResultBean reflectionBean = (NonCashWithdrawResultBean) paramBean;
		    // 免密提现--校验接口
            String status = reflectionBean.getStatus();
            String statusDesc = reflectionBean.getStatusDesc();

            String amt = reflectionBean.getAmt();
            String fee = reflectionBean.getFee();
            String arrivalAmount = reflectionBean.getArrivalAmount();
            if(Validator.isNull(amt)){
                sign = status + statusDesc;
            }else{
                sign = status + statusDesc +amt +fee +arrivalAmount ;
            }
		}

		return ApiSignUtil.encryptByRSA(sign);
	}

	/**
	 * 转换json对象
	 *
	 * @param obj
	 * @return
	 */
	public JSONObject convertJsonObject(Object obj) {
		return (JSONObject) JSONObject.toJSON(obj);
	}

	/**
	 *
	 * 特殊字符编码
	 *
	 * @author renxingchen
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public String strEncode(String str) {
		try {
			str = URLEncoder.encode(str, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 组成返回信息
	 *
	 * @param message
	 * @param status
	 * @return
	 */
	public JSONObject jsonMessage(String message, String status) {
		JSONObject jo = null;
		if (Validator.isNotNull(message)) {
			jo = new JSONObject();
			jo.put(CustomConstants.APP_STATUS_DESC, message);
			jo.put(CustomConstants.APP_STATUS, status);
		}
		return jo;
	}

	/**
	 * 接口规则（MD5）加密结果
	 * @param params
	 * @return
	 * @author liubin
	 */
	protected String getSign(Object...params) {
		String sign = StringUtils.EMPTY;
		String accessKey = PropUtils.getSystem(ApplyDefine.AOP_INTERFACE_ACCESSKEY);

		//字符串拼接（accessKey + param1 + param2 + ... + accessKey)）
		StringBuilder builder = new StringBuilder();
		builder.append(accessKey);
		for (Object param : params) {
			if (params != null) {
				builder.append(String.valueOf(param));
			}
		}
		builder.append(accessKey);

		sign = builder.toString();
		return StringUtils.lowerCase(MD5.toMD5Code(sign));
	}

	/**
	 * 验证签名
	 * @param chkValue 调用方传入签名
	 * @param params 调用方传入验签参数
	 * @return
	 * @author liubin
	 */
	protected boolean checkSign(String chkValue, Object...params) {
		String sign = getSign(params);
		return StringUtils.equals(sign, chkValue) ? true : false;
	}

	/**
	 * 传入参数类型错误异常处理
	 * @param request
	 * @param response
	 * @param ex
	 * @return
	 * @author liubin
	 */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ResultBean<?> bindExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        return new ResultBean<>(ResultBean.FAIL, CheckUtil.getErrorMessage("param.error"));
    }

	/**
	 * 传入JSON错误异常处理
	 * @param request
	 * @param response
	 * @param ex
	 * @return
	 * @author liubin
	 */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResultBean<?> httpMessageNotReadableExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) {
    	return new ResultBean<>(ResultBean.FAIL, CheckUtil.getErrorMessage("json.error"));
    }
}
