package com.hyjf.mybatis.customize;

import javax.annotation.Resource;

import com.hyjf.mybatis.mapper.auto.*;
import com.hyjf.mybatis.mapper.customize.admin.*;
import com.hyjf.mybatis.mapper.customize.apiweb.aems.AemsBorrowRepayPlanCustomizeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hyjf.mybatis.auto.AutoMapper;
import com.hyjf.mybatis.mapper.customize.*;
import com.hyjf.mybatis.mapper.customize.act.ActCustomizeMapper;
import com.hyjf.mybatis.mapper.customize.act.ActNovBargainCustomizeMapper;
import com.hyjf.mybatis.mapper.customize.admin.*;
import com.hyjf.mybatis.mapper.customize.admin.act.ActTen2017CustomizeMapper;
import com.hyjf.mybatis.mapper.customize.admin.act.ActdecSpringListCustomizeMapper;
import com.hyjf.mybatis.mapper.customize.admin.htj.*;
import com.hyjf.mybatis.mapper.customize.admin.statistics.AdminTZJStatisticsDayReportCustomizeMapper;
import com.hyjf.mybatis.mapper.customize.admin.statistics.StatisticsTZJCustomizeMapper;
import com.hyjf.mybatis.mapper.customize.admin.statistics.StatisticsTZJUTMReportCustomizeMapper;
import com.hyjf.mybatis.mapper.customize.apiweb.*;
import com.hyjf.mybatis.mapper.customize.apiweb.plan.WeChatDebtPlanCustomizeMapper;
import com.hyjf.mybatis.mapper.customize.apiweb.plan.WeChatPlanAccountCustomizeMapper;
import com.hyjf.mybatis.mapper.customize.apiweb.wdzj.WDZJCustomizeMapper;
import com.hyjf.mybatis.mapper.customize.app.*;
import com.hyjf.mybatis.mapper.customize.batch.*;
import com.hyjf.mybatis.mapper.customize.bifa.CreditTenderCustomizeMapper;
import com.hyjf.mybatis.mapper.customize.bifa.HjhAccedeCustomizeMapper;
import com.hyjf.mybatis.mapper.customize.billion.BillionCustomizeMapper;
import com.hyjf.mybatis.mapper.customize.callcenter.*;
import com.hyjf.mybatis.mapper.customize.coupon.*;
import com.hyjf.mybatis.mapper.customize.datacenter.BorrowUserStatisticCustomizeMapper;
import com.hyjf.mybatis.mapper.customize.datacenter.DataCenterCouponCustomizeMapper;
import com.hyjf.mybatis.mapper.customize.message.coupon.SiteMsgLogCustomizeMapper;
import com.hyjf.mybatis.mapper.customize.nifa.*;
import com.hyjf.mybatis.mapper.customize.poundageledger.PoundageCustomizeMapper;
import com.hyjf.mybatis.mapper.customize.poundageledger.PoundageDetailCustomizeMapper;
import com.hyjf.mybatis.mapper.customize.poundageledger.PoundageExceptionCustomizeMapper;
import com.hyjf.mybatis.mapper.customize.poundageledger.PoundageLedgerCustomizeMapper;
import com.hyjf.mybatis.mapper.customize.recommend.RecommendCustomizeMapper;
import com.hyjf.mybatis.mapper.customize.report.ContentOperationReportCustomizeMapper;
import com.hyjf.mybatis.mapper.customize.report.ReportTenderCustomizeMapper;
import com.hyjf.mybatis.mapper.customize.statisticsway.StatisticsWayCustomizeMapper;
import com.hyjf.mybatis.mapper.customize.web.*;
import com.hyjf.mybatis.mapper.customize.web.hjh.HjhPlanCustomizeMapper;
import com.hyjf.mybatis.mapper.customize.web.hjhreinvestdebt.HjhReInvestDebtCustomizeMapper;
import com.hyjf.mybatis.mapper.customize.web.hjhreinvestdetail.HjhReInvestDetailCustomizeMapper;
import com.hyjf.mybatis.mapper.customize.web.htj.DebtPlanBorrowCustomizeMapper;
import com.hyjf.mybatis.mapper.customize.web.htj.DebtPlanCustomizeMapper;
import com.hyjf.mybatis.mapper.customize.wecat.WecatProjectListCustomizeMapper;
import com.hyjf.mybatis.mapper.customize.whereaboutspage.WhereaboutsPageCustomizeMapper;
import com.hyjf.mybatis.mapper.customize.worldcup.WorldCupActivityCustomizeMapper;
import com.hyjf.mybatis.mapper.customize.wrb.WrbQueryCustomizeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CustomizeMapper extends AutoMapper {

	@Autowired
	protected AdminRoleCustomizeMapper adminRoleCustomizeMapper;

	@Autowired
	protected AdminCustomizeMapper adminCustomizeMapper;

	@Autowired
	protected AdminSystemMapper adminSystemMapper;

	@Resource
	protected AdminAccountCustomizeMapper adminAccountCustomizeMapper;

	@Autowired
	protected AdminBankcardCustomizeMapper adminBankcardCustomizeMapper;

	@Resource
	protected AdminOpenAccountExceptionCustomizeMapper adminOpenAccountExceptionCustomizeMapper;

	@Resource
	protected AdminRegistCustomizeMapper adminRegistCustomizeMapper;

	@Autowired
	protected AdminRecoverExceptionCustomizeMapper adminRecoverExceptionCustomizeMapper;

	@Autowired
	protected AdminBankCardExceptionCustomizeMapper adminBankCardExceptionCustomizeMapper;

	@Autowired
	protected AdminRechargeExceptionCustomizeMapper adminRechargeExceptionCustomizeMapper;

	@Autowired
	protected AdminRechargeWarnExceptionCustomizeMapper adminRechargeWarnExceptionCustomizeMapper;

	@Autowired
	protected AdminRepayDelayCustomizeMapper adminRepayDelayCustomizeMapper;

	@Resource
	protected AdminUsersCustomizeMapper adminUsersCustomizeMapper;

	@Autowired
	protected AdminRedisProjectListCustomizeMapper redisProjectListCustomizeMapper;

	@Resource
	protected AdminUserEvalationResultCustomizeMapper adminUserEvalationResultCustomizeMapper;

	@Autowired
	protected AdminTenderExceptionCustomizeMapper adminTenderExceptionCustomizeMapper;

	@Autowired
	protected AdminBorrowExceptionMapper adminBorrowExceptionMapper;

	@Autowired
	protected AdminEmployeeLeaveCustomizeMapper employeeLeaveCustomizeMapper;

	@Autowired
	protected AdminEmployeeUserCustomizeMapper employeeUserCustomizeMapper;

	@Autowired
	protected AppChannelStatisticsCustomizeMapper appChannelStatisticsCustomizeMapper;

	@Autowired
	protected AppChannelStatisticsDetailCustomizeMapper appChannelStatisticsDetailCustomizeMapper;

	@Autowired
	protected AppAdsCustomizeMapper adsCustomizeMapper;

	@Autowired
	protected AppBorrowImageCustomizeMapper appBorrowImageCustomizeMapper;

	@Autowired
	protected AppProjectListCustomizeMapper appProjectListCustomizeMapper;

	@Autowired
	protected HjhLabelCustomizeMapper hjhLabelCustomizeMapper;

	@Autowired
	protected AppTradeDetailCustomizeMapper appTradeDetailCustomizeMapper;

	@Autowired
	protected AppUserInvestCustomizeMapper appUserInvestCustomizeMapper;

	@Autowired
	protected AppAccountTradeCustomizeMapper appAccountTradeCustomizeMapper;

	@Autowired
	protected ApiwebTenderInfoCustomizeMapper tenderInfoCustomizeMapper;

	@Resource
	protected WebUserInvestListCustomizeMapper webUserInvestListCustomizeMapper;

	@Resource
	protected WebUserTradeDetailCustomizeMapper webUserTradeDetailCustomizeMapper;

	@Resource
	protected WebUserRepayListCustomizeMapper webUserRepayListCustomizeMapper;

	@Autowired
	protected WebHomePageCustomizeMapper webHomePageCustomizeMapper;

	@Autowired
	protected WebHtlStatisticsCustomizeMapper webHtlStatisticsCustomizeMapper;

	@Autowired
	protected WebPandectCustomizeMapper webPandectCustomizeMapper;

	@Autowired
	protected WebProjectListCustomizeMapper webProjectListCustomizeMapper;

	@Autowired
	protected WecatProjectListCustomizeMapper wecatProjectListCustomizeMapper;

	@Autowired
	protected AreasCustomizeMapper areasCustomizeMapper;

	@Autowired
	protected AccountManageCustomizeMapper accountManageCustomizeMapper;

	@Autowired
	protected AccountDetailCustomizeMapper accountDetailCustomizeMapper;

	@Autowired
	protected ActivityListCustomizeMapper activityListCustomizeMapper;

	@Autowired
	protected BorrowCustomizeMapper borrowCustomizeMapper;

	@Autowired
	protected BorrowLogCustomizeMapper borrowLogCustomizeMapper;

	@Autowired
	protected BorrowRegistCustomizeMapper borrowRegistCustomizeMapper;

	@Autowired
	protected BorrowFirstCustomizeMapper borrowFirstCustomizeMapper;

	@Autowired
	protected BorrowFullCustomizeMapper borrowFullCustomizeMapper;

	@Autowired
	protected BorrowRepaymentCustomizeMapper borrowRepaymentCustomizeMapper;

	@Autowired
	protected BorrowRepaymentInfoCustomizeMapper borrowRepaymentInfoCustomizeMapper;

	@Autowired
	protected BorrowRepaymentDetailsCustomizeMapper  borrowRepaymentDetailsCustomizeMapper;

	@Autowired
	protected BorrowRepaymentInfoListCustomizeMapper borrowRepaymentInfoListCustomizeMapper;

	@Autowired
	protected BorrowRepaymentPlanCustomizeMapper borrowRepaymentPlanCustomizeMapper;

	@Autowired
	protected BorrowInvestCustomizeMapper borrowInvestCustomizeMapper;

	@Autowired
	protected BorrowRecoverCustomizeMapper borrowRecoverCustomizeMapper;

	@Autowired
	protected BorrowApplyCustomizeMapper borrowApplyCustomizeMapper;

	@Autowired
	protected ContentArticleCustomizeMapper contentArticleCustomizeMapper;

	@Autowired
	protected ContentHelpCustomizeMapper contenthelpCustomizeMapper;

	@Autowired
	protected ContentJobsCustomizeMapper contentJobsCustomizeMapper;

	@Autowired
	protected ContentQualifyCustomizeMapper contentQualifyCustomizeMapper;

	@Autowired
	protected ContentTeamCustomizeMapper contentTeamCustomizeMapper;

	@Autowired
	protected ContentEventsCustomizeMapper contentEventsCustomizeMapper;

	@Resource
	protected HtlUserInfoCustomizeMapper htlUserInfoCustomizeMapper;

	@Autowired
	protected HtlCommonCustomizeMapper htlCommonCustomizeMapper;

	@Autowired
	protected LinksCustomizeMapper linksCustomizeMapper;

	@Resource
	protected ProductRedeemCustomizeMapper productRedeemCustomizeMapper;

	@Autowired
	protected ProductIntoRecordCustomizeMapper productIntoRecordCustomizeMapper;

	@Autowired
	protected ProductInterestCustomizeMapper productInterestCustomizeMapper;

	@Autowired
	protected ProductRedeemDetailCustomizeMapper productRedeemDetailCustomizeMapper;

	@Autowired
	protected RechargeCustomizeMapper rechargeCustomizeMapper;

	@Autowired
	protected ReturncashCustomizeMapper returncashCustomizeMapper;

	@Autowired
	protected SmsCodeCustomizeMapper smsCodeCustomizeMapper;

	@Autowired
	protected SmsLogCustomizeMapper smsLogCustomizeMapper;

	@Autowired
	protected SmsCountCustomizeMapper smsCountCustomizeMapper;

	@Resource
	protected UsersCustomizeMapper usersCustomizeMapper;

	@Resource
	protected WithdrawCustomizeMapper withdrawCustomizeMapper;

	@Autowired
	protected WebCustomizeMapper webCustomizeMapper;

	@Autowired
	protected PushMoneyCustomizeMapper pushMoneyCustomizeMapper;

	@Autowired
	protected ProjectTypeCustomizeMapper projectTypeCustomizeMapper;

	@Autowired
	protected BorrowCreditCustomizeMapper borrowCreditCustomizeMapper;

	@Autowired
	protected OntimeTenderCustomizeMapper ontimeTenderCustomizeMapper;

	@Autowired
	protected EmployeeCustomizeMapper employeeCustomizeMapper;

	@Autowired
	protected ChannelCustomizeMapper channelCustomizeMapper;

	@Autowired
	protected ChannelCountCustomizeMapper channelCountCustomizeMapper;

	@Autowired
	protected KeyCountCustomizeMapper keyCountCustomizeMapper;

	@Autowired
	protected PlatformCountCustomizeMapper platformCountCustomizeMapper;

	@Autowired
	protected ProductStatisCustomizeMapper productStatisCustomizeMapper;

	@Autowired
	protected PlanStatisCustomizeMapper planStatisCustomizeMapper;

	@Autowired
	protected AccountChinapnrCustomizeMapper accountChinapnrCustomizeMapper;

	@Autowired
	protected OntimeUserEntryCustomizeMapper ontimeUserEntryCustomizeMapper;

	@Autowired
	protected OntimeUserLeaveCustomizeMapper ontimeUserLeaveCustomizeMapper;

	@Autowired
	protected WebListCustomizeMapper webListCustomizeMapper;

	@Autowired
	protected UserInfoCustomizeMapper userInfoCustomizeMapper;

	@Autowired
	protected RepayExceptionCustomizeMapper repayExceptionCustomizeMapper;

	@Autowired
	protected RepayExceptionInfoCustomizeMapper repayExceptionInfoCustomizeMapper;

	@Autowired
	protected ReportTenderCustomizeMapper reportTenderCustomizeMapper;

	@Autowired
	protected SubmissionsCustomizeMapper submissionsCustomizeMapper;

	@Autowired
	protected ChannelStatisticsDetailCustomizeMapper channelStatisticsDetailCustomizeMapper;

	@Autowired
	protected QuestionCustomizeMapper questionCustomizeMapper;

	@Autowired
	protected CreditRepayCustomizeMapper creditRepayCustomizeMapper;

	@Autowired
	protected TenderCreditCustomizeMapper tenderCreditCustomizeMapper;

	@Autowired
	protected WebInviteCustomizeMapper webInviteCustomizeMapper;

	@Autowired
	protected HelpCustomizeMapper helpCustomizeMapper;

	@Autowired
	protected AdminTransferCustomizeMapper adminTransferCustomizeMapper;

	@Autowired
	protected DataCustomizeMapper dataCustomizeMapper;

	@Autowired
	protected ChangeLogCustomizeMapper changeLogCustomizeMapper;

	@Autowired
	protected ActivityF1CustomizeMapper activityF1CustomizeMapper;

	@Autowired
	protected AdminBorrowCreditRepayCustomizeMapper adminBorrowCreditRepayCustomizeMapper;

	@Autowired
	protected CouponConfigCustomizeMapper couponConfigCustomizeMapper;

	@Autowired
	protected CouponUserCustomizeMapper couponUserCustomizeMapper;

	@Autowired
	protected CouponTenderCustomizeMapper couponTenderCustomizeMapper;

	@Autowired
	protected CouponBackMoneyCustomizeMapper couponBackMoneyCustomizeMapper;

	@Autowired
	protected SiteMsgLogCustomizeMapper siteMsgLogCustomizeMapper;

	@Autowired
	protected DataCenterCouponCustomizeMapper dataCenterCouponCustomizeMapper;

	@Autowired
	protected AdminTransferExceptionLogCustomizeMapper transferExceptionLogCustomizeMapper;

	@Autowired
	protected CouponRecoverCustomizeMapper couponRecoverCustomizeMapper;

	@Resource
	protected VIPManageCustomizeMapper vipManageCustomizeMapper;

	@Resource
	protected VIPDetailListCustomizeMapper vipDetailListCustomizeMapper;

	@Resource
	protected VIPUPGradeListCustomizeMapper vipUPGradeListCustomizeMapper;

	@Resource
	protected VipAuthCustomizeMapper vipAuthCustomizeMapper;

	@Resource
	protected CouponUserListCustomizeMapper couponUserListCustomizeMapper;

	@Autowired
	protected AdminPreRegistCustomizeMapper adminPreRegistCustomizeMapper;

	@Autowired
	protected BorrowFinmanNewChargeCustomizeMapper borrowFinmanNewChargeCustomizeMapper;

	@Autowired
	protected AdminPreRegistChannelExclusiveActivityCustomizeMapper adminPreRegistChannelExclusiveActivityCustomizeMapper;

	@Autowired
	protected AdminActivityReturncashCustomizeMapper adminActivityReturncashCustomizeMapper;

	@Autowired
	protected RechargeFeeCustomizeMapper rechargeFeeCustomizeMapper;

	@Autowired
	protected AdminMerchantAccountCustomizeMapper adminMerchantAccountCustomizeMapper;

	@Autowired
	protected AdminCouponRepayMonitorCustomizeMapper adminCouponRepayMonitorCustomizeMapper;

	@Autowired
	protected WebCalculateInvestInterestCustomizeMapper webCalculateInvestInterestCustomizeMapper;

	@Autowired
	protected AppUserPrizeCodeCustomizeMapper appUserPrizeCodeCustomizeMapper;

	@Autowired
	protected BorrowTenderInfoCustomizeMapper borrowTenderInfoCustomizeMapper;

	@Autowired
	protected AdminNewUserActivityCustomizeMapper adminNewUserActivityCustomizeMapper;

	@Autowired
	protected WebBorrowAppointCustomizeMapper webBorrowAppointCustomizeMapper;

	@Autowired
	protected AppointmentAuthLogCustomizeMapper appointmentAuthLogCustomizeMapper;

	@Autowired
	protected MsgPushCommonCustomizeMapper msgPushCommonCustomizeMapper;

	@Autowired
	protected InviteUserCustomizeMapper inviteUserCustomizeMapper;

	@Autowired
	protected GetRecommendCustomizeMapper getRecommendCustomizeMapper;

	@Autowired
	protected UsedRecommendCustomizeMapper usedRecommendCustomizeMapper;

	@Autowired
	protected PrizeGetCustomizeMapper prizeGetCustomizeMapper;

	@Autowired
	protected PrizeResetCustomizeMapper prizeResetCustomizeMapper;

	@Autowired
	protected Mgm10CustomizeMapper mgm10CustomizeMapper;

	@Autowired
	protected RecommendCustomizeMapper recommendCustomizeMapper;

	@Autowired
	protected PlanLockCustomizeMapper planLockCustomizeMapper;

	@Autowired
	protected PlanCustomizeMapper planCustomizeMapper;

	@Autowired
	protected WhereaboutsPageCustomizeMapper whereaboutsPageCustomizeMapper;

	@Autowired
	protected AppTenderCreditCustomizeMapper appTenderCreditCustomizeMapper;

	@Autowired
	protected AdminBorrowAppointCustomizeMapper adminBorrowAppointCustomizeMapper;

	@Autowired
	protected BillionSecondCustomizeMapper billionSecondCustomizeMapper;

	@Autowired
	protected FinserChargeCustomizeMapper finserChargeCustomizeMapper;

	@Autowired
	protected StockInfoCustomizeMapper stockInfoCustomizeMapper;

	@Autowired
	protected AdminStockInfoCustomizeMapper adminStockInfoCustomizeMapper;

	@Autowired
	protected BatchDebtPlanBorrowCustomizeMapper batchDebtPlanBorrowCustomizeMapper;

	@Autowired
	protected BatchDebtCreditCustomizeMapper batchDebtCreditCustomizeMapper;

	@Autowired
	protected AdminPlanAccedeDetailCustomizeMapper adminPlanAccedeDetailCustomizeMapper;

	@Autowired
	protected DebtBorrowCustomizeMapper debtBorrowCustomizeMapper;

	@Autowired
	protected DebtBorrowApplyCustomizeMapper debtBorrowApplyCustomizeMapper;

	@Autowired
	protected DebtBorrowFirstCustomizeMapper debtBorrowFirstCustomizeMapper;

	@Autowired
	protected DebtBorrowFullCustomizeMapper debtBorrowFullCustomizeMapper;

	@Autowired
	protected DebtBorrowInvestCustomizeMapper debtBorrowInvestCustomizeMapper;

	@Autowired
	protected DebtBorrowRecoverCustomizeMapper debtBorrowRecoverCustomizeMapper;

	@Autowired
	protected DebtPlanBorrowCustomizeMapper debtPlanBorrowCustomizeMapper;

	@Autowired
	protected DebtPlanCustomizeMapper debtPlanCustomizeMapper;

	@Autowired
	protected DebtBorrowRepaymentCustomizeMapper debtBorrowRepaymentCustomizeMapper;

	@Autowired
	protected DebtAdminRepayDelayCustomizeMapper debtAdminRepayDelayCustomizeMapper;

	@Autowired
	protected DebtBorrowRepaymentPlanCustomizeMapper debtBorrowRepaymentPlanCustomizeMapper;

	@Autowired
	protected DebtBorrowRepaymentInfoCustomizeMapper debtBorrowRepaymentInfoCustomizeMapper;

	@Autowired
	protected DebtBorrowRepaymentInfoListCustomizeMapper debtBorrowRepaymentInfoListCustomizeMapper;

	@Autowired
	protected DebtAccountDetailCustomizeMapper debtAccountDetailCustomizeMapper;

	@Autowired
	protected BatchDebtBorrowCustomizeMapper batchDebtBorrowCustomizeMapper;

	@Autowired
	protected BatchDebtBorrowFullCustomizeMapper batchDebtBorrowFullCustomizeMapper;

	@Autowired
	protected BatchDebtOntimeBorrowCustomizeMapper batchDebtOntimeBorrowCustomizeMapper;

	@Autowired
	protected PlanPushMoneyCustomizeMapper planPushMoneyCustomizeMapper;

	@Autowired
	protected BatchDebtPlanAccedeCustomizeMapper batchDebtPlanAccedeCustomizeMapper;

	@Autowired
	protected AdminPlanPushMoneyDetailCustomizeMapper adminPlanPushMoneyDetailCustomizeMapper;

	@Autowired
	protected DebtAdminBorrowExceptionMapper debtAdminBorrowExceptionMapper;

	@Autowired
	protected DebtCreditCustomizeMapper debtCreditCustomizeMapper;

	@Autowired
    protected HjhDebtCreditCustomizeMapper hjhdebtCreditCustomizeMapper;

	@Autowired
	protected DebtCreditTenderCustomizeMapper debtCreditTenderCustomizeMapper;

	@Autowired
	protected HjhDebtCreditTenderCustomizeMapper hjhdebtCreditTenderCustomizeMapper;

	@Autowired
	protected DebtAdminCreditCustomizeMapper debtAdminCreditCustomizeMapper;

	@Autowired
	protected DebtPlanAccedeCustomizeMapper debtPlanAccedeCustomizeMapper;

	@Autowired
	protected DebtDetailCustomizeMapper debtDetailCustomizeMapper;

	@Autowired
	protected AdminDebtPlanCustomizeMapper adminDebtPlanCustomizeMapper;

	@Autowired
	protected BatchDebtPlanCustomizeMapper batchDebtPlanCustomizeMapper;

	@Autowired
	protected BatchDebtCreditRepayCustomizeMapper batchDebtCreditRepayCustomizeMapper;

	@Autowired
	protected BatchDebtDetailCustomizeMapper batchDebtDetailCustomizeMapper;

	@Autowired
	protected BatchTyjRepayCustomizeMapper batchTyjRepayCustomizeMapper;

	@Autowired
	protected WkcdBorrowCustomizeMapper wkcdBorrowCustomizeMapper;

	@Autowired
	protected BatchAccountCustomizeMapper batchAccountCustomizeMapper;

	@Autowired
	protected AdminIncreaseInterestRepayCustomizeMapper adminIncreaseInterestRepayCustomizeMapper;

	@Autowired
	protected WeChatDebtPlanCustomizeMapper weChatDebtPlanCustomizeMapper;

	@Autowired
	protected WeChatPlanAccountCustomizeMapper weChatPlanAccountCustomizeMapper;

	@Autowired
	protected BorrowRepayCustomizeMapper borrowRepayCustomizeMapper;

	@Autowired
	protected AppChannelReconciliationCustomizeMapper appChannelReconciliationCustomizeMapper;

	@Autowired
	protected AdminUtmReadPermissionsCustomizeMapper adminUtmReadPermissionsCustomizeMapper;

	@Autowired
	protected AdminIncreaseInterestExceptionCustomizeMapper adminIncreaseInterestExceptionCustomizeMapper;

	@Autowired
	protected NaMiActivityCustomizeMapper naMiActivityCustomizeMapper;

	@Autowired
	protected Newyear2016CustomizeMapper newyear2016CustomizeMapper;

	@Autowired
	protected GetCardCustomizeMapper getCardCustomizeMapper;

	@Autowired
	protected IanternFestivalCustomizeMapper ianternFestivalCustomizeMapper;

	@Autowired
	protected BatchPcPromotionCustomizeMapper batchPcPromotionCustomizeMapper;

	@Autowired
	protected BatchChannelStatisticsOldCustomizeMapper batchChannelStatisticsOldCustomizeMapper;

	@Autowired
	protected PcChannelReconciliationCustomizeMapper pcChannelReconciliationCustomizeMapper;

	@Autowired
	protected BatchCouponTimeoutCustomizeMapper couponTimeoutCustomizeMapper;

	@Autowired
	protected AdminTZJStatisticsDayReportCustomizeMapper tzjStatisticsDayReportCustomizeMapper;

	@Autowired
	protected StatisticsTzjUtmCustomizeMapper statisticsTzjUtmCustomizeMapper;

	@Autowired
	protected StatisticsTZJCustomizeMapper statisticsTZJCustomizeMapper;

	@Autowired
	protected UserTenderDetailCustomizeMapper userTenderDetailCustomizeMapper;

	@Autowired
	protected StatisticsTZJUTMReportCustomizeMapper statisticsTZJUTMReportCustomizeMapper;

	@Autowired
	protected BankAccountManageCustomizeMapper bankAccountManageCustomizeMapper;

	@Autowired
	protected AdminBankAccountCheckCustomizeMapper adminBankAccountCheckCustomizeMapper;

	@Autowired
	protected AccountBankCustomizeMapper accountBankCustomizeMapper;

	@Autowired
	protected BatchBorrowTenderExceptionCustomizeMapper batchBorrowTenderExceptionCustomizeMapper;

	@Autowired
	protected BillionCustomizeMapper billionCustomizeMapper;

	@Autowired
	protected BatchCenterCustomizeMapper batchCenterCustomizeMapper;

	@Autowired
	protected BankMerchantAccountListCustomizeMapper bankMerchantAccountListCustomizeMapper;
	@Autowired
	protected AssetManageCustomizeMapper assetManageCustomizeMapper;

	@Autowired
	protected PcChannelStatisticsCustomizeMapper pcChannelStatisticsCustomizeMapper;

	@Autowired
	protected AdminBankDebtEndCustomizeMapper adminBankDebtEndCustomizeMapper;

	@Autowired
	protected BorrowUserCustomizeMapper borrowUserCustomizeMapper;

	@Autowired
	protected BankInvestSumCustomizeMapper bankInvestSumCustomizeMapper;

	@Autowired
	protected InterestSumCustomizeMapper interestSumCustomizeMapper;

	@Autowired
	protected OfflineRechargeCustomizeMapper offlineRechargeCustomizeMapper;

	@Autowired
	protected CouponAccountUpdateCustomizeMapper couponAccountUpdateCustomizeMapper;

	@Autowired
	protected AdminBorrowRegistExceptionMapper adminBorrowRegistExceptionMapper;

	@Autowired
	protected BindCardExceptionCustomizeMapper bindCardExceptionCustomizeMapper;

	@Autowired
	protected ActivityInviteSevenCustomizeMapper activityInviteSevenCustomizeMapper;

	@Autowired
	protected MobileSynchronizeCustomizeMapper mobileSynchronizeCustomizeMapper;

	@Autowired
	protected AdminBorrowTenderTmpMapper adminBorrowTenderTmpMapper;

	@Autowired
	protected AdminUserAuthCustomizeMapper adminUserAuthCustomizeMapper;

	@Autowired
	protected AssetListCustomizeMapper assetListCustomizeMapper;

	@Autowired
	protected AdminPlanAccedeListCustomizeMapper adminPlanAccedeListCustomizeMapper;

	@Autowired
	protected HjhPlanCustomizeMapper hjhPlanCustomizeMapper;

	@Autowired
	protected HjhAssetBorrowTypeCustomizeMapper hjhAssetBorrowTypeCustomizeMapper;

	@Autowired
	protected FeerateModifyLogMapper feerateModifyLogMapper;

	@Autowired
	protected FeerateModifyLogsMapper feerateModifyLogsMapper;
	/***************************************呼叫中心相关服务 start*******************************************/
	@Autowired
	protected CallCenterCouponUserCustomizeMapper callCenterCouponUserCustomizeMapper;

	@Autowired
	protected CallcenterAccountHuifuCustomizeMapper callcenterAccountHuifuCustomizeMapper;

	@Autowired
	protected CallcenterUserInfoCustomizeMapper callcenterUserInfoCustomizeMapper;

	@Autowired
	protected CallCenterAccountDetailCustomizeMapper callCenterAccountDetailCustomizeMapper;

	@Autowired
	protected CallcenterHztInvestCustomizeMapper callcenterHztInvestCustomizeMapper;

	@Autowired
	protected CallcenterHtjInvestCustomizeMapper callcenterHtjInvestCustomizeMapper;

	@Autowired
	protected CallcenterRechargeCustomizeMapper callcenterRechargeCustomizeMapper;

	@Autowired
	protected CallcenterWithdrawCustomizeMapper callcenterwithdrawCustomizeMapper;

	@Autowired
	protected CallCenterRepaymentDetailCustomizeMapper callCenterRepaymentDetailCustomizeMapper;

	@Autowired
	protected CallCenterBorrowCreditCustomizeMapper callCenterBorrowCreditCustomizeMapper;

	@Autowired
	protected CallCenterBankAccountManageCustomizeMapper callCenterBankAccountManageCustomizeMapper;

	@Autowired
	protected CallCenterAccountManageCustomizeMapper callCenteraccountManageCustomizeMapper;
	/***************************************呼叫中心相关服务 ent*********************************************/
	@Autowired
	protected ActTen2017CustomizeMapper actTen2017CustomizeMapper;

	@Autowired
	protected ApiProjectListCustomizeMapper apiProjectListCustomizeMapper;

	@Autowired
	protected MspConfigureCustomizeMapper mspConfigureMapper;

	/***************************************安融相关*********************************************/
	@Autowired
	protected AnrongSendLogMapper anrongSendLogMapper;

	@Autowired
	protected ActNovBargainCustomizeMapper actNovBargainCustomizeMapper;

	@Autowired
	protected  STZHWhiteListCustomizeMapper stzhWhiteListCustomizeMapper;

	@Autowired
	protected AutoReqRepayBorrowCustomizeMapper autoReqRepayBorrowCustomizeMapper;


	// 标的授权
	@Autowired
	protected  WebBorrowAuthCustomizeMapper borrowAuthCustomizeMapper;
	@Autowired
	protected HjhDebtDetailCustomizeMapper hjhDebtDetailCustomizeMapper;

	@Autowired
	protected BatchHjhAccedeCustomizeMapper batchHjhAccedeCustomizeMapper;

	@Autowired
	protected IncreaseInterestInvestCustomizeMapper increaseInterestInvestCustomizeMapper;

	@Autowired
	protected IncreaseInterestRepayCustomizeMapper increaseInterestRepayCustomizeMapper;

	@Autowired
	protected HjhPlanListCustomizeMapper hjhPlanListCustomizeMapper;

	@Autowired
	protected HjhRepayCustomizeMapper hjhRepayCustomizeMapper;


	/***************************************分账*********************************************/
	@Autowired
	protected  SubCommissionListConfigCustomizeMapper subCommissionListConfigCustomizeMapper;

	@Autowired
	protected BorrowDetailCustomizeMapper borrowDetailMapper;

	@Autowired
	protected ActCustomizeMapper actCustomizeMapper;
	
	@Autowired
	protected WDZJCustomizeMapper wdzjCustomizeMapper;

	@Autowired
	protected PoundageLedgerCustomizeMapper poundageLedgerCustomizeMapper;

	@Autowired
	protected PoundageCustomizeMapper poundageCustomizeMapper;

	@Autowired
	protected PoundageDetailCustomizeMapper poundageDetailCustomizeMapper;

	@Autowired
	protected PoundageExceptionCustomizeMapper poundageExceptionCustomizeMapper;


	@Autowired
	protected ActdecSpringListCustomizeMapper actdecSpringListCustomizeMapper;

	@Autowired
	protected ManualReverseCustomizeMapper manualReverseCustomizeMapper;

	@Autowired
	protected FddTempletCustomizeMapper fddTempletCustomizeMapper;

	@Autowired
	protected ListActServiceCustomizeMapper listActServiceCustomizeMapper;
	@Autowired
	protected ActdecListedTwoCustomizeMapper actdecListedTwoCustomizeMapper;

	@Autowired
	protected ActdecListedOneCustomizeMapper actdecListedOneCustomizeMapper;

	@Autowired
	protected ActdecListedThreeCustomizeMapper actdecListedThreeCustomizeMapper;

	@Autowired
	protected ActdecListedFourCustomizeMapper actdecListedFourCustomizeMapper;

	/***批次结束债权 **/
	@Autowired
	protected BankCreditEndMapper bankCreditEndMapper;

	@Autowired
	protected BorrowUserStatisticCustomizeMapper borrowUserStatisticCustomizeMapper;

	@Autowired
	protected WrbQueryCustomizeMapper wrbQueryCustomizeMapper;

	@Autowired
	protected HjhPlanCapitalCustomizeMapper hjhPlanCapitalCustomizeMapper;

	@Autowired
	protected HjhReInvestDetailCustomizeMapper hjhReInvestDetailCustomizeMapper;

    @Autowired
    protected HjhAccountBalanceCustomizeMapper hjhAccountBalanceCustomizeMapper;

    @Autowired
    protected HjhReInvestDebtCustomizeMapper hjhReInvestDebtCustomizeMapper;
	@Autowired
	protected ActdecFinancingCustomizeMapper actdecFinancingCustomizeMapper;

	@Autowired
	protected ContentOperationReportCustomizeMapper contentOperationReportCustomizeMapper;

	@Autowired
	protected AdminBankInterfaceCustomizeMapper adminBankInterfaceCustomizeMapper;

	@Autowired
	protected WorldCupActivityCustomizeMapper worldCupActivityCustomizeMapper;
	@Autowired
	protected StatisticsWayCustomizeMapper statisticsWayCustomizeMapper;

	@Autowired
	protected ApplyAgreementCustomizeMapper applyAgreementCustomizeMapper;

	@Autowired
	protected NifaContractEssenceCustomizeMapper nifaContractEssenceCustomizeMapper;

	@Autowired
	protected NifaContractTemplateCustomizeMapper nifaContractTemplateCustomizeMapper;

	@Autowired
	protected NifaRepayInfoCustomizeMapper nifaRepayInfoCustomizeMapper;

	@Autowired
	protected NifaContractStatusCustomizeMapper nifaContractStatusCustomizeMapper;

	@Autowired
	protected NifaReceivedPaymentsCustomizeMapper nifaReceivedPaymentsCustomizeMapper;

	@Autowired
	protected  NifaReportLogCustomizeMapper nifaReportLogCustomizeMapper;
	@Autowired
	protected QixiActivityCustomizeMapper qixiActivityCustomizeMapper;

	@Autowired
	protected HjhBailConfigCustomizeMapper hjhBailConfigCustomizeMapper;
	@Autowired
	protected HjhBailConfigLogCustomizeMapper hjhBailConfigLogCustomizeMapper;
	@Autowired
	protected AssetExceptionCustomizeMapper assetExceptionCustomizeMapper;

	@Autowired
	protected  UserDepartmentInfoCustomizeMapper userDepartmentInfoCustomizeMapper;

	@Autowired
	protected ActivityMidauInfoCustomizeMapper activityMidauInfoCustomizeMapper;

	@Autowired
	protected BankRepayFreezeOrgCustomizeMapper bankRepayFreezeOrgCustomizeMapper;

	@Autowired
	protected HjhUserAuthConfigCustomizeMapper hjhUserAuthConfigCustomizeMapper;


	@Autowired
	protected EvalationCustomizeMapper evalationCustomizeMapper;

	@Autowired
	protected UserEvalationResultCustomizeMapper userEvalationResultCustomizeMapper;

	@Autowired
	protected TwoelevenCustomizeMapper twoelevenCustomizeMapper;
	
	@Autowired
	protected CertUserTransactMapper certUserTransactMapper;

	/** 北互金相关 **/
	@Autowired
	protected CreditTenderCustomizeMapper creditTenderCustomizeMapper;
	@Autowired
	protected HjhAccedeCustomizeMapper hjhAccedeCustomizeMapper;

	@Autowired
	protected EvaluationConfigMapper evaluationConfigMapper;

	@Autowired
	protected EvaluationConfigLogMapper evaluationConfigLogMapper;
    @Autowired
    protected AemsBorrowRepayPlanCustomizeMapper aemsBorrowRepayPlanCustomizeMapper;

	@Autowired
	protected BorrowManinfoCustomizeMapper borrowManinfoCustomizeMapper;

	@Autowired
	protected NifaBorrowerInfoCustomizeMapper nifaBorrowerInfoCustomizeMapper;

	@Autowired
	protected NifaDualHistoryDataCustomizeMapper nifaDualHistoryDataCustomizeMapper;

	@Autowired
	protected BankCreditEndCustomizeMapper bankCreditEndCustomizeMapper;
}
