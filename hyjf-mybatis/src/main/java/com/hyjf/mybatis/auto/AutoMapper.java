package com.hyjf.mybatis.auto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hyjf.mybatis.mapper.auto.*;
import com.hyjf.mybatis.mapper.customize.UtmPlatCustomizeMapper;
import com.hyjf.mybatis.mapper.customize.act.ActdecCorpsCustomizeMapper;
import com.hyjf.mybatis.mapper.customize.admin.AdminAccountCustomizeQuiryMapper;
import com.hyjf.mybatis.mapper.customize.admin.BanksConfigCustomizeMapper;
import com.hyjf.mybatis.mapper.customize.admin.act.ActSigninCustomizeMapper;

@Service
public class AutoMapper {

    @Autowired
    protected AccountBankMapper accountBankMapper;

    @Autowired
    protected AccountChinapnrMapper accountChinapnrMapper;

    @Autowired
    protected ExceptionAccountMapper exceptionAccountMapper;

    @Autowired
    protected AccountFundsMapper accountFundsMapper;

    @Autowired
    protected AccountRechargeMapper accountRechargeMapper;

    @Autowired
    protected AccountMapper accountMapper;

    @Autowired
    protected AccountListMapper accountListMapper;

    @Autowired
    protected AccountTradeMapper accountTradeMapper;

    @Autowired
    protected ActivityListMapper activityListMapper;

    @Autowired
    protected AccountWebListMapper accountWebListMapper;

    @Autowired
    protected AccountwithdrawMapper accountwithdrawMapper;

    @Autowired
    protected AdsMapper adsMapper;

    @Autowired
    protected AdsTypeMapper adsTypeMapper;

    @Autowired
    protected AdminMapper adminMapper;

    @Autowired
    protected AdminRoleMapper adminRoleMapper;

    @Autowired
    protected AdminAndRoleMapper adminAndRoleMapper;

    @Autowired
    protected AdminRoleMenuPermissionsMapper adminRoleMenuPermissionsMapper;

    @Autowired
    protected AdminMenuPermissionsMapper adminMenuPermissionsMapper;

    @Autowired
    protected AdminMenuMapper adminMenuMapper;

    @Autowired
    protected AdminPermissionsMapper adminPermissionsMapper;

    @Autowired
    protected AreasMapper areasMapper;

    @Autowired
    protected AccountBorrowMapper accountBorrowMapper;

    @Autowired
    protected AccountLogMapper accountLogMapper;

    @Autowired
    protected BankConfigMapper bankConfigMapper;

    @Autowired
    protected BankWithdrawTypeMapper bankWithdrawTypeMapper;

    @Autowired
    protected BorrowCreditMapper borrowCreditMapper;

    @Autowired
    protected BorrowMapper borrowMapper;

    @Autowired
    protected BorrowBailMapper borrowBailMapper;

    @Autowired
    protected BorrowCarinfoMapper borrowCarinfoMapper;

    @Autowired
    protected BorrowConsumeMapper borrowConsumeMapper;

    @Autowired
    protected BorrowConfigMapper borrowConfigMapper;

    @Autowired
    protected BorrowCompanyAuthenMapper borrowCompanyAuthenMapper;

    @Autowired
    protected BorrowFinhxfmanChargeMapper borrowFinhxfmanChargeMapper;

    @Autowired
    protected BorrowHousesMapper borrowHousesMapper;

    @Autowired
    protected BorrowManinfoMapper borrowManinfoMapper;

    @Autowired
    protected BorrowUsersMapper borrowUsersMapper;

    @Autowired
    protected BorrowRecoverMapper borrowRecoverMapper;

    @Autowired
    protected BorrowRecoverPlanMapper borrowRecoverPlanMapper;

    @Autowired
    protected BorrowRepayMapper borrowRepayMapper;

    @Autowired
    protected BorrowRepayPlanMapper borrowRepayPlanMapper;

    @Autowired
    protected BorrowTenderMapper borrowTenderMapper;

    @Autowired
    protected BorrowTenderTmpMapper borrowTenderTmpMapper;

    @Autowired
    protected BorrowTenderTmpInfoMapper borrowTenderTmpInfoMapper;

    @Autowired
    protected BorrowProjectTypeMapper borrowProjectTypeMapper;

    @Autowired
    protected BorrowProjectRepayMapper borrowProjectRepayMapper;

    @Autowired
    protected BorrowSendTypeMapper borrowSendTypeMapper;

    @Autowired
    protected BorrowFinmanChargeMapper borrowFinmanChargeMapper;

    @Autowired
    protected BorrowFinserChargeMapper borrowFinserChargeMapper;

    @Autowired
    protected BorrowStyleMapper borrowStyleMapper;

    @Autowired
    protected BorrowApicronMapper borrowApicronMapper;

    @Autowired
    protected ConsumeMapper consumeMapper;

    @Autowired
    protected ConsumeListMapper consumeListMapper;

    @Autowired
    protected ContentQualifyMapper contentQualifyMapper;

    @Autowired
    protected ContentEnvironmentMapper contentEnvironmentMapper;

    @Autowired
    protected ContentArticleMapper contentArticleMapper;

    @Autowired
    protected ChinapnrLogMapper chinapnrLogMapper;

    @Autowired
    protected ChinapnrSendlogMapper chinapnrSendlogMapper;

    @Autowired
    protected ChinapnrExclusiveLogMapper chinapnrExclusiveLogMapper;

    @Autowired
    protected ConfigMapper configMapper;

    @Autowired
    protected CategoryMapper categoryMapper;

    @Autowired
    protected ContentHelpMapper contentHelpMapper;

    @Autowired
    protected DepartmentMapper departmentMapper;

    @Autowired
    protected EventsMapper eventsMapper;

    @Autowired
    protected FeeConfigMapper feeConfigMapper;

    @Autowired
    protected FreezeListMapper freezeListMapper;

    @Autowired
    protected HzrConfigMapper hzrConfigMapper;

    @Autowired
    protected JobsMapper jobsMapper;

    @Autowired
    protected LinksMapper linksMapper;

    @Autowired
    protected LoanMapper loanMapper;

    @Autowired
    protected ParamNameMapper paramNameMapper;

    @Autowired
    protected ProductMapper productMapper;

    @Autowired
    protected ProductChinapnrLogMapper productChinapnrLogMapper;

    @Autowired
    protected ProductChinapnrSendLogMapper productChinapnrSendLogMapper;

    @Autowired
    protected ProductErrorLogMapper productErrorLogMapper;

    @Autowired
    protected ProductInterestMapper productInterestMapper;

    @Autowired
    protected ProductInfoMapper productInfoMapper;

    @Autowired
    protected ProductListLogMapper productListLogMapper;

    @Autowired
    protected ProductListMapper productListMapper;

    @Autowired
    protected ProductRedeemFailMapper productRedeemFailMapper;

    @Autowired
    protected ProductRedeemListMapper productRedeemListMapper;

    @Autowired
    protected ProductRedeemMapper productRedeemMapper;

    @Autowired
    protected PushMoneyMapper pushMoneyMapper;

    @Autowired
    protected SmsConfigMapper smsConfigMapper;

    @Autowired
    protected SmsNoticeConfigMapper smsNoticeConfigMapper;

    @Autowired
    protected SmsCodeMapper smsCodeMapper;

    @Autowired
    protected SmsLogMapper smsLogMapper;

    @Autowired
    protected SmsTemplateMapper smsTemplateMapper;

    @Autowired
    protected SmsMailTemplateMapper smsMailTemplateMapper;

    @Autowired
    protected SiteSettingMapper siteSettingMapper;

    @Autowired
    protected SpreadsUsersMapper spreadsUsersMapper;

    @Autowired
    protected SpreadsUsersLogMapper spreadsUsersLogMapper;

    @Autowired
    protected SpreadsLogMapper spreadsLogMapper;

    @Autowired
    protected TeamMapper teamMapper;

    @Autowired
    protected TenderCommissionMapper tenderCommissionMapper;

    @Autowired
    protected UsersMapper usersMapper;

    @Autowired
    protected UsersInfoMapper usersInfoMapper;

    @Autowired
    protected UsersModifyLogMapper usersModifyLogMapper;

    @Autowired
    protected UsersContractMapper usersContractMapper;

    @Autowired
    protected UsersLogMapper usersLogMapper;

    @Autowired
    protected UsersPortraitMapper usersPortraitMapper;

    @Autowired
    protected UtmPlatMapper utmPlatMapper;

    @Autowired
    protected UtmMapper utmMapper;

    @Autowired
    protected UtmRegMapper utmRegMapper;

    @Autowired
    protected FreezeHistoryMapper freezeHistoryMapper;

    @Autowired
    protected TenderBackHistoryMapper tenderBackHistoryMapper;

    @Autowired
    protected AppBorrowImageMapper appBorrowImageMapper;

    @Autowired
    protected VersionMapper versionMapper;

    @Autowired
    protected MobileCodeMapper mobileCodeMapper;

    @Autowired
    protected UserCornerMapper userCornerMapper;

    @Autowired
    protected SubmissionsMapper submissionsMapper;

    @Autowired
    protected InviteMapper inviteMapper;

    @Autowired
    protected AppAccessStatisticsMapper appAccessStatisticsMapper;

    @Autowired
    protected AppChannelStatisticsMapper appChannelStatisticsMapper;

    @Autowired
    protected AppChannelStatisticsDetailMapper appChannelStatisticsDetailMapper;

    @Autowired
    protected ExtensionTempMapper extensionTempMapper;

    @Autowired
    protected UserEvalationResultMapper userEvalationResultMapper;

    @Autowired
    protected QuestionMapper questionMapper;

    @Autowired
    protected AnswerMapper answerMapper;

    @Autowired
    protected EvalationMapper evalationMapper;

    @Autowired
    protected UserEvalationMapper userEvalationMapper;

    @Autowired
    protected ConfigApplicantMapper configApplicantMapper;

    @Autowired
    protected CreditTenderLogMapper creditTenderLogMapper;

    @Autowired
    protected CreditTenderMapper creditTenderMapper;

    @Autowired
    protected CreditRepayMapper creditRepayMapper;

    @Autowired
    protected CreditRepayLogMapper creditRepayLogMapper;

    @Autowired
    protected HolidaysConfigMapper holidaysConfig;

    @Autowired
    protected WithdrawalsTimeConfigMapper withdrawalsTimeConfigMapper;

    @Autowired
    protected AccountAccurateMapper accountAccurateMapper;

    @Autowired
    protected UserEvalationBehaviorMapper userEvalationBehaviorMapper;

    @Autowired
    protected UserBindEmailLogMapper userBindEmailLogMapper;

    @Autowired
    protected ActivityF1Mapper activityF1Mapper;

    @Autowired
    protected UtmVisitMapper utmVisitMapper;

    @Autowired
    protected UsersChangeLogMapper usersChangeLogMapper;

    @Autowired
    protected BankRechargeLimitConfigMapper bankRechargeLimitConfigMapper;

    @Autowired
    protected UserTransferMapper userTransferMapper;

    @Autowired
    protected CouponConfigMapper couponConfigMapper;

    @Autowired
    protected CouponUserMapper couponUserMapper;

    @Autowired
    protected CouponRecoverMapper couponRecoverMapper;

    @Autowired
    protected CouponTenderMapper couponTenderMapper;

    @Autowired
    protected CouponRealTenderMapper couponRealTenderMapper;

    @Autowired
    protected CouponOperationHistoryMapper couponOperationHistoryMapper;

    @Autowired
    protected TransferExceptionLogMapper transferExceptionLogMapper;

    @Autowired
    protected SiteMsgConfigMapper siteMsgConfigMapper;

    @Autowired
    protected SiteMsgLogMapper siteMsgLogMapper;

    @Autowired
    protected BorrowTenderCpnMapper borrowTenderCpnMapper;

    @Autowired
    protected VipInfoMapper vipInfoMapper;

    @Autowired
    protected VipAuthMapper vipAuthMapper;

    @Autowired
    protected VipUserTenderMapper vipUserTenderMapper;

    @Autowired
    protected VipUserUpgradeMapper vipUserUpgradeMapper;

    @Autowired
    protected VipTransferLogMapper vipTransferLogMapper;

    @Autowired
    protected AccountDirectionalTransferMapper accountDirectionalTransferMapper;

    @Autowired
    protected DirectionalTransferAssociatedRecordsMapper directionalTransferAssociatedRecordsMapper;

    @Autowired
    protected DirectionalTransferAssociatedLogMapper directionalTransferAssociatedLogMapper;

    @Autowired
    protected CorpOpenAccountRecordMapper corpOpenAccountRecordMapper;

    @Autowired
    protected ActivityReturncashMapper activityReturncashMapper;

    @Autowired
    protected IdfaMapper idfaMapper;

    @Autowired
    protected PreRegistMapper preRegistMapper;

    @Autowired
    protected BorrowFinmanNewChargeMapper borrowFinmanNewChargeMapper;

    @Autowired
    protected PreRegistChannelExclusiveActivityMapper preRegistChannelExclusiveActivityMapper;

    @Autowired
    protected RechargeFeeReconciliationMapper rechargeFeeReconciliationMapper;

    @Autowired
    protected MerchantAccountMapper merchantAccountMapper;

    @Autowired
    protected CalculateInvestInterestMapper calculateInvestInterestMapper;

    @Autowired
    protected TenderMonthInfoMapper tenderMonthInfoMapper;

    @Autowired
    protected LandingPageMapper landingPageMapper;

    @Autowired
    protected MerchantTransferMapper merchantTransferMapper;

    @Autowired
    protected RechargeFeeBalanceStatisticsMapper rechargeFeeBalanceStatisticsMapper;

    @Autowired
    protected RechargeFeeStatisticsMapper rechargeFeeStatisticsMapper;

    @Autowired
    protected CouponRepayMonitorMapper couponRepayMonitorMapper;

    @Autowired
    protected PrizeListMapper prizeListMapper;

    @Autowired
    protected UserPrizeCodeMapper userPrizeCodeMapper;

    @Autowired
    protected I4Mapper i4Mapper;

    @Autowired
    protected AppointmentAuthLogMapper appointmentAuthLogMapper;

    @Autowired
    protected BorrowAppointMapper borrowAppointMapper;

    @Autowired
    protected AppointmentRecodLogMapper appointmentRecodLogMapper;

    @Autowired
    protected MessagePushTagMapper messagePushTagMapper;

    @Autowired
    protected MessagePushTemplateMapper messagePushTemplateMapper;

    @Autowired
    protected MessagePushMsgMapper messagePushMsgMapper;

    @Autowired
    protected MessagePushMsgHistoryMapper messagePushMsgHistoryMapper;

    @Autowired
    protected MessagePushPlatStaticsMapper messagePushPlatStaticsMapper;

    @Autowired
    protected MessagePushTemplateStaticsMapper messagePushTemplateStaticsMapper;

    @Autowired
    protected MessagePushUserReadMapper messagePushUserReadMapper;

    @Autowired
    protected InviteInfoMapper inviteInfoMapper;

    @Autowired
    protected InviteRecommendMapper inviteRecommendMapper;

    @Autowired
    protected InviteRecommendPrizeMapper inviteRecommendPrizeMapper;

    @Autowired
    protected InvitePrizeConfMapper invitePrizeConfMapper;

    @Autowired
    protected WhereaboutsPageConfigMapper whereaboutsPageConfigMapper;

    @Autowired
    protected WhereaboutsPagePictureMapper whereaboutsPagePictureMapper;

    @Autowired
    protected DebtPlanMapper debtPlanMapper;

    @Autowired
    protected DebtPlanConfigMapper debtPlanConfigMapper;

    @Autowired
    protected DebtPlanBorrowMapper debtPlanBorrowMapper;

    @Autowired
    protected DebtAccountListMapper debtAccountListMapper;

    @Autowired
    protected DebtCreditMapper debtCreditMapper;

    @Autowired
    protected DebtCreditRepayMapper debtCreditRepayMapper;

    @Autowired
    protected DebtCreditTenderMapper debtCreditTenderMapper;

    @Autowired
    protected DebtCreditTenderLogMapper debtCreditTenderLogMapper;

    @Autowired
    protected DebtInvestMapper debtInvestMapper;

    @Autowired
    protected DebtInvestLogMapper debtInvestLogMapper;

    @Autowired
    protected DebtFreezeMapper debtFreezeMapper;

    @Autowired
    protected DebtFreezeLogMapper debtFreezeLogMapper;

    @Autowired
    protected DebtLoanMapper debtLoanMapper;

    @Autowired
    protected DebtLoanDetailMapper debtLoanDetailMapper;

    @Autowired
    protected DebtPlanAccedeMapper debtPlanAccedeMapper;

    @Autowired
    protected DebtRepayMapper debtRepayMapper;

    @Autowired
    protected DebtRepayDetailMapper debtRepayDetailMapper;

    @Autowired
    protected DebtLoanLogMapper debtLoanLogMapper;

    @Autowired
    protected DebtBorrowMapper debtBorrowMapper;

    @Autowired
    protected DebtBailMapper debtBailMapper;

    @Autowired
    protected DebtCarInfoMapper debtCarInfoMapper;

    @Autowired
    protected DebtCompanyAuthenMapper debtCompanyAuthenMapper;

    @Autowired
    protected DebtCompanyInfoMapper debtCompanyInfoMapper;

    @Autowired
    protected DebtHouseInfoMapper debtHouseInfoMapper;

    @Autowired
    protected DebtUsersInfoMapper debtUsersInfoMapper;

    @Autowired
    protected DebtApicronMapper debtApicronMapper;

    @Autowired
    protected DebtDetailMapper debtDetailMapper;

    @Autowired
    protected BankAccountLogMapper bankAccountLogMapper;

    @Autowired
    protected DebtAccedeCommissionMapper debtAccedeCommissionMapper;

    @Autowired
    protected DebtCommissionConfigMapper debtCommissionConfigMapper;

    @Autowired
    protected StockInfoMapper stockInfoMapper;

    @Autowired
    protected ActivityBillionOneMapper activityBillionOneMapper;

    @Autowired
    protected ActivityBillionSecondMapper activityBillionSecondMapper;

    @Autowired
    protected ActivityBillionThirdMapper activityBillionThirdMapper;

    @Autowired
    protected ActivityBillionSecondTimeMapper activityBillionSecondTimeMapper;

    @Autowired
    protected ActivityBillionThirdConfigMapper activityBillionThirdConfigMapper;

    @Autowired
    protected DebtPlanInfoStaticMapper debtPlanInfoStaticMapper;

    @Autowired
    protected IncreaseInterestInvestMapper increaseInterestInvestMapper;

    @Autowired
    protected IncreaseInterestLoanMapper increaseInterestLoanMapper;

    @Autowired
    protected IncreaseInterestLoanDetailMapper increaseInterestLoanDetailMapper;

    @Autowired
    protected IncreaseInterestRepayMapper increaseInterestRepayMapper;

    @Autowired
    protected IncreaseInterestRepayDetailMapper increaseInterestRepayDetailMapper;

    @Autowired
    protected SmsOntimeMapper smsOntimeMapper;

    @Autowired
    protected NewyearCaisheCardUserMapper newyearCaisheCardUserMapper;

    @Autowired
    protected NewyearPrizeUserMapper newyearPrizeUserMapper;

    @Autowired
    protected NewyearPrizeConfigMapper newyearPrizeConfigMapper;

    @Autowired
    protected NewyearQuestionConfigMapper newyearQuestionConfigMapper;

    @Autowired
    protected NewyearQuestionUserMapper newyearQuestionUserMapper;

    @Autowired
    protected NewyearCaishenCardQuantityMapper newyearCaishenCardQuantityMapper;

    @Autowired
    protected NewyearSendPrizeMapper newyearSendPrizeMapper;

    @Autowired
    protected NewyearGetCardMapper newyearGetCardMapper;

    @Autowired
    protected ServerApplicationMapper serverApplicationMapper;

    @Autowired
    protected WkcdBorrowMapper wkcdBorrowMapper;

    @Autowired
    protected AdminUtmReadPermissionsMapper adminUtmReadPermissionsMapper;

    @Autowired
    protected StatisticsTzjMapper statisticsTzjMapper;

    @Autowired
    protected StatisticsTzjHourMapper statisticsTzjHourMapper;

    @Autowired
    protected StatisticsTzjUtmMapper statisticsTzjUtmMapper;

    @Autowired
    protected BankOpenAccountLogMapper bankOpenAccountLogMapper;

    @Autowired
    protected BankOpenAccountMapper bankOpenAccountMapper;

    @Autowired
    protected BankCardMapper bankCardMapper;

    @Autowired
    protected BankSmsAuthCodeMapper bankSmsAuthCodeMapper;

    @Autowired
    protected BankMerchantAccountMapper bankMerchantAccountMapper;

    @Autowired
    protected DataErrorAccountMapper dataErrorAccountMapper;

    @Autowired
    protected DataErrorSubjectMapper dataErrorSubjectMapper;

    @Autowired
    protected DataErrorDebtMapper dataErrorDebtMapper;

    @Autowired
    protected BankMerchantAccountListMapper bankMerchantAccountListMapper;

    @Autowired
    protected BankReturnCodeConfigMapper bankReturnCodeConfigMapper;

    @Autowired
    protected UserDeviceUniqueCodeMapper userDeviceUniqueCodeMapper;

    @Autowired
    protected IdCardToAreaMapper idCardToAreaMapper;

    @Autowired
    protected PcChannelStatisticsMapper pcChannelStatisticsMapper;

    @Autowired
    protected CardBinMapper cardBinMapper;

    @Autowired
    protected BanksConfigMapper banksConfigMapper;

    @Autowired
    protected BankRepayFreezeLogMapper bankRepayFreezeLogMapper;

    @Autowired
    protected BankMerchantAccountInfoMapper bankMerchantAccountInfoMapper;

    @Autowired
    protected ActivityInviteSevenMapper activityInviteSevenMapper;

    @Autowired
    protected HjhInstConfigMapper hjhInstConfigMapper;

    @Autowired
    protected HjhAssetBorrowTypeMapper hjhAssetBorrowTypeMapper;

    @Autowired
    protected HjhPlanMapper hjhPlanMapper;

    @Autowired
    protected HjhPlanAssetMapper hjhPlanAssetMapper;

    @Autowired
    protected HjhAccedeMapper hjhAccedeMapper;

    @Autowired
    protected HjhUserAuthMapper hjhUserAuthMapper;

    @Autowired
    protected HjhUserAuthLogMapper hjhUserAuthLogMapper;

    @Autowired
    protected HjhRepayMapper hjhRepayMapper;

    @Autowired
    protected HjhPlanBorrowTmpMapper hjhPlanBorrowTmpMapper;

    @Autowired
    protected HjhAssetTypeMapper hjhAssetTypeMapper;

    @Autowired
    protected BanksConfigCustomizeMapper banksConfigCustomizeMapper;

    @Autowired
    protected FeerateModifyLogMapper feerateModifyLogMapper;

    @Autowired
    protected CallcenterServiceUsersMapper callcenterServiceUsersMapper;

    @Autowired
    protected AdminAccountCustomizeQuiryMapper adminAccountCustomizeQuiryMapper;

    @Autowired
    protected ActQuestionsMapper actQuestionsMapper;

    @Autowired
    protected ActQuestionsAnswerlistMapper actQuestionsAnswerlistMapper;

    @Autowired
    protected ActSigninMapper actSigninMapper;
    @Autowired
    protected ActRewardListMapper actRewardListMapper;
    @Autowired
    protected ActSigninCustomizeMapper actSigninCustomizeMapper;
    @Autowired
    protected SubCommissionMapper subCommissionMapper;
    @Autowired
    protected BindUsersMapper bindUsersMapper;
    @Autowired
    protected MspApplyMapper mspApplyMapper;
    @Autowired
    protected MspRegionMapper mspRegionMapper;
    @Autowired
    protected MspFqzMapper mspFqzMapper;
    @Autowired
    protected MspAnliInfosMapper mspAnliInfosMapper;
    @Autowired
    protected MspShixinInfosMapper mspShixinInfosMapper;
    @Autowired
    protected MspZhixingInfosMapper mspZhixingInfosMapper;
    @Autowired
    protected MspTitleMapper mspTitleMapper;
    @Autowired
    protected MspNormalCreditDetailMapper mspNormalCreditDetailMapper;
    @Autowired
    protected MspApplyDetailsMapper mspApplyDetailsMapper;
    @Autowired
    protected MspQueryDetailMapper mspQueryDetailMapper;
    @Autowired
    protected MspBlackDataMapper mspBlackDataMapper;
    @Autowired
    protected MspAbnormalCreditDetailMapper mspAbnormalCreditDetailMapper;
    @Autowired
    protected MspAbnormalCreditMapper mspAbnormalCreditMapper;
    @Autowired
    protected MspConfigureMapper mspConfigureMapper;
    @Autowired
    protected MspConfigureMapper mspConfigureMapperAuto;

    @Autowired
    protected AleveLogMapper aleveLogMapper;
    @Autowired
    protected AleveErrowLogMapper aleveErrowLogMapper;
    @Autowired
    protected EveLogMapper eveLogMapper;
    @Autowired
    protected ActJanQuestionsMapper actJanQuestionsMapper;
    @Autowired
    protected ActJanAnswerListMapper actJanAnswerListMapper;
    @Autowired
    protected ActJanPrizeMapper actJanPrizeMapper;
    @Autowired
    protected ActJanPrizewinListMapper actJanPrizewinListMapper;
    @Autowired
    protected ActJanBargainMapper actJanBargainMapper;

    @Autowired
    protected  STZHWhiteListMapper sTZHWhiteListMapper;
    @Autowired
    protected  WeeklyReportMapper weeklyReportMapper ;
    @Autowired
    protected ActdecCorpsMapper actdecCorpsMapper;
    @Autowired
    protected ActdecWinningMapper actdecWinningMapper;
    @Autowired
    protected ActdecTenderBalloonMapper actdecTenderBalloonMapper;
    @Autowired
    protected ActdecCorpsCustomizeMapper actdecCorpsCustomizeMapper;

    @Autowired
    protected UtmPlatCustomizeMapper utmPlatCustomizeMapper;

    @Autowired
    protected HjhAllocationEngineMapper hjhAllocationEngineMapper;

    @Autowired
    protected HjhLabelMapper hjhLabelMapper;

    @Autowired
    protected HjhDebtCreditMapper hjhDebtCreditMapper;

    @Autowired
    protected HjhRegionMapper hjhRegionMapper;

    @Autowired
    protected HjhDebtDetailMapper hjhDebtDetailMapper;

    @Autowired
    protected BorrowLogMapper borrowLogMapper;

    @Autowired
    protected  HjhDebtCreditTenderMapper hjhDebtCreditTenderMapper;

    @Autowired
    protected HjhDebtCreditTenderLogMapper hjhDebtCreditTenderLogMapper;

    @Autowired
    protected HjhDebtCreditRepayMapper hjhDebtCreditRepayMapper;

    @Autowired
    protected PoundageLedgerMapper poundageLedgerMapper;

    @Autowired
    protected PoundageMapper poundageMapper;

    @Autowired
    protected PoundageDetailMapper poundageDetailMapper;

    @Autowired
    protected PoundageExceptionMapper poundageExceptionMapper;

    @Autowired
    protected ActdecSpringListMapper actdecSpringListMapper;

    @Autowired
    protected CertificateAuthorityMapper certificateAuthorityMapper;

    @Autowired
    protected TenderAgreementMapper tenderAgreementMapper;

    @Autowired
    protected FddTempletMapper fddTempletMapper;

    @Autowired
    protected ActdecListedFourMapper actdecListedFourMapper;
    @Autowired
    protected ActdecListedThreeMapper actdecListedThreeMapper;
    @Autowired
    protected ActdecListedOneMapper actdecListedOneMapper;
    @Autowired
    protected ActdecListedTwoMapper actdecListedTwoMapper;
    @Autowired
    protected BankCreditEndMapper bankCreditEndMapper;

    @Autowired
    protected LoanSubjectCertificateAuthorityMapper loanSubjectCertificateAuthorityMapper;

    @Autowired
    protected BorrowUserStatisticMapper borrowUserStatisticMapper;
    @Autowired
    protected ActdecFinancingMapper actdecFinancingMapper;

    @Autowired
    protected HjhPlanCapitalMapper hjhPlanCapitalMapper;
    @Autowired
    protected HjhAssetRiskInfoMapper hjhAssetRiskInfoMapper;
    @Autowired
    protected HjhAccountBalanceMapper hjhAccountBalanceMapper;
    @Autowired
    protected BankInterfaceMapper bankInterfaceMapper;

    @Autowired
    protected OperationReportMapper operationReportMapper;

    @Autowired
    protected OperationReportActivityMapper operationReportActivityMapper;
    @Autowired
    protected HalfYearOperationReportMapper halfYearOperationReportMapper;

    @Autowired
    protected MonthlyOperationReportMapper monthlyOperationReportMapper;

    @Autowired
    protected QuarterOperationReportMapper quarterOperationReportMapper;

    @Autowired
    protected TenthOperationReportMapper tenthOperationReportMapper;

    @Autowired
    protected UserOperationReportMapper userOperationReportMapper;
    @Autowired
    protected YearOperationReportMapper yearOperationReportMapper;
    @Autowired
    protected UnderLineRechargeMapper underLineRechargeMapper;
    @Autowired
    protected ApplyAgreementMapper applyAgreementMapper;

    @Autowired
    protected ProtocolVersionMapper protocolVersionMapper;
    @Autowired
    protected ProtocolTemplateMapper protocolTemplateMapper;
    @Autowired
    protected  ApplyAgreementInfoMapper applyAgreementInfoMapper;

    @Autowired
    protected ProtocolLogMapper protocolLogMapper;

    @Autowired
    protected NifaReportLogMapper nifaReportLogMapper;
    @Autowired
    protected HjhBailConfigMapper hjhBailConfigMapper;

    @Autowired
    protected NifaContractEssenceMapper nifaContractEssenceMapper;
    @Autowired
    protected HjhBailConfigInfoMapper hjhBailConfigInfoMapper;

    @Autowired
    protected HjhBailConfigLogMapper hjhBailConfigLogMapper;

    @Autowired
    protected BorrowDeleteMapper borrowDeleteMapper;

    @Autowired
    protected NifaContractStatusMapper nifaContractStatusMapper;

    @Autowired
    protected NifaContractTemplateMapper nifaContractTemplateMapper;

    @Autowired
    protected NifaFieldDefinitionMapper nifaFieldDefinitionMapper;

    @Autowired
    protected NifaRepayInfoMapper nifaRepayInfoMapper;

    @Autowired
    protected NifaReceivedPaymentsMapper nifaReceivedPaymentsMapper;

    @Autowired
    protected DebtConfigMapper debtConfigMapper;
    @Autowired
    protected AppPushManageMapper appPushManageMapper;

    @Autowired
    protected DebtConfigLogMapper debtConfigLogMapper;

    @Autowired
    protected HjhUserAuthConfigMapper hjhUserAuthConfigMapper;

    @Autowired
    protected HjhUserAuthConfigLogMapper hjhUserAuthConfigLogMapper;

    @Autowired
    protected ActivityMidauRecodMapper activityMidauRecodMapper;

    @Autowired
    protected ActivityMidauInfoMapper activityMidauInfoMapper;
    @Autowired
    protected BankRepayOrgFreezeLogMapper bankRepayOrgFreezeLogMapper;

    @Autowired
    protected TwoelevenRewardMapper twoelevenRewardMapper;

    //合规数据上报 CERT
    @Autowired
    protected CertErrLogMapper certErrLogMapper;
    @Autowired
    protected CertUserMapper certUserMapper;
    @Autowired
    protected CertLogMapper certLogMapper;
    @Autowired
    protected CertBorrowMapper certBorrowMapper;

    @Autowired
    protected InviterReturnDetailMapper inviterReturnDetailMapper;
    @Autowired
    protected PerformanceReturnDetailMapper performanceReturnDetailMapper;

}

