/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mqreceiver.hgdatareport.nifa.tenderinfo;

import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.Validator;
import com.hyjf.mongo.hgdatareport.dao.NifaBorrowInfoDao;
import com.hyjf.mongo.hgdatareport.dao.NifaBorrowerInfoDao;
import com.hyjf.mongo.hgdatareport.dao.NifaTenderInfoDao;
import com.hyjf.mongo.hgdatareport.entity.NifaBorrowInfoEntity;
import com.hyjf.mongo.hgdatareport.entity.NifaBorrowerInfoEntity;
import com.hyjf.mongo.hgdatareport.entity.NifaTenderInfoEntity;
import com.hyjf.mqreceiver.hgdatareport.BaseHgDateReportServiceImpl;
import com.hyjf.mqreceiver.hgdatareport.nifa.repayinfos.NifaRepayInfosMessageHadnle;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.nifa.NifaTenderUserInfoCustomize;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

/**
 * @author PC-LIUSHOUYI
 * @version NifaBorrowLoanServiceImpl, v0.1 2018/11/27 10:58
 */
@Service
public class NifaTenderInfoServiceImpl extends BaseHgDateReportServiceImpl implements NifaTenderInfoService {

    Logger _log = LoggerFactory.getLogger(NifaTenderInfoServiceImpl.class);

    private String thisMessName = "互金标的相关信息上送";
    private String logHeader = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_NIFA + " " + thisMessName + "】";

    @Autowired
    NifaBorrowerInfoDao nifaBorrowerInfoDao;

    @Autowired
    NifaBorrowInfoDao nifaBorrowInfoDao;

    @Autowired
    NifaTenderInfoDao nifaTenderInfoDao;

    /**
     * 根据借款主体信息获取借款人报送信息
     *
     * @param msgBody
     * @param borrowNid
     * @param borrower
     * @return
     */
    @Override
    public NifaBorrowerInfoEntity selectNifaBorrowerInfo(String msgBody, String borrowNid, String borrower) {
        Query query = new Query();
        Criteria criteria = Criteria.where("projectNo").is(CustomConstants.COM_SOCIAL_CREDIT_CODE + "1" + borrowNid).and("message").is(msgBody).and("userId").is(borrower);
        query.addCriteria(criteria);
        NifaBorrowerInfoEntity nifaBorrowerInfoEntity = nifaBorrowerInfoDao.findOne(query);
        if (null != nifaBorrowerInfoEntity) {
            return nifaBorrowerInfoEntity;
        }
        return null;
    }

    /**
     * 生成标的信息
     *
     * @param historyData
     * @param borrow
     * @param borrowRepay
     * @param borrowRecoverList
     * @param recoverFee
     * @param lateCounts
     * @param nifaBorrowInfoEntity
     * @return
     */
    @Override
    public boolean selectDualNifaBorrowInfo(String historyData, Borrow borrow, BorrowRepay borrowRepay, List<BorrowRecover> borrowRecoverList, BigDecimal recoverFee, String lateCounts, NifaBorrowInfoEntity nifaBorrowInfoEntity) {
        nifaBorrowInfoEntity.setHistoryData(historyData);
        // 项目唯一编号 系统内唯一编号，是社会信用代码、平台序号和报数机构项目编号的组合，长度128位
        nifaBorrowInfoEntity.setProjectNo(CustomConstants.COM_SOCIAL_CREDIT_CODE + "1" + borrow.getBorrowNid());
        // 社会信用代码
        nifaBorrowInfoEntity.setSocialCreditCode(CustomConstants.COM_SOCIAL_CREDIT_CODE);
        // 平台序号
        nifaBorrowInfoEntity.setPlatformNo("1");
        // 项目编号 报数机构内部项目编号，长度64位
        nifaBorrowInfoEntity.setOrganizationNo(borrow.getBorrowNid());
        // 项目类型 01互联网债权类融资-个体直接借贷，02互联网债权类融资-互联网小额贷款，03其他债权类融资业务，长度2位
        nifaBorrowInfoEntity.setBorrowType("01");
        // 项目名称 项目名称不存在的取借款编号
        nifaBorrowInfoEntity.setBorrowName(StringUtils.isNotBlank(borrow.getProjectName()) ? borrow.getProjectName() : borrow.getBorrowNid());
        // 项目成立日期 借款人和出借人债权债务关系的成立日期，格式为YYYYMMDD
        if (null == borrow.getRecoverLastTime()) {
            _log.error(logHeader + "最终放款日为空！！borrowNid:{}", borrow.getBorrowNid());
            return false;
        }
        try {
            nifaBorrowInfoEntity.setRecoverTime(GetDate.timestamptoStrYYYYMMDD(borrow.getRecoverLastTime()).replaceAll("-", ""));
        } catch (Exception e) {
            e.printStackTrace();
            _log.error(logHeader + "最终放款日格式化失败！！borrowNid:{}", borrow.getBorrowNid());
            return false;
        }
        // 业务处理时间记录-最终放款日
//        nifaBorrowInfoEntity.setHistoryData(GetDate.timestamptoStrYYYYMMDD(borrow.getRecoverLastTime()));
        // 借款金额
        nifaBorrowInfoEntity.setAccount(borrow.getAccount() + "");
        // 借款币种 GB/T 12406-2008，表示资金和货币的代码，长度3位
        nifaBorrowInfoEntity.setCurrency("CNY");
        // 借款起息日 借款合同约定的起息日期，格式为YYYYMMDD 生产库：即收益起始日，为项目认购完成日。
        nifaBorrowInfoEntity.setBorrowInterestTime(nifaBorrowInfoEntity.getRecoverTime());
        // 借款到期日期 借款合同约定的到期日期，格式为YYYYMMDD
        if (null == borrow.getBorrowEndTime()) {
            _log.error(logHeader + "借款到期日为空！！borrowNid:{},到期日:{}", borrow.getBorrowNid(), borrow.getBorrowEndTime());
            return false;
        }
        if (!Validator.isNumber(borrow.getBorrowEndTime())) {
            _log.error(logHeader + "借款到期日含非数字！！borrowNid:{},到期日:{}", borrow.getBorrowNid(), borrow.getBorrowEndTime());
            return false;
        }
        Integer borrowEndTime = Integer.parseInt(borrow.getBorrowEndTime());
        try {
            nifaBorrowInfoEntity.setBorrowEndTime(GetDate.timestamptoStrYYYYMMDD(borrowEndTime).replaceAll("-", ""));
        } catch (Exception e) {
            e.printStackTrace();
            _log.error(logHeader + "最终放款日格式化失败！！borrowNid:{}", borrow.getBorrowNid());
            return false;
        }
        // 是否分期还款
        boolean isMonth = false;
        // 最后一笔还款日
        String lasterDay;
        Integer lasterRepayTime = 0;
        // 还款方式 去还款表取数据
        if (CustomConstants.BORROW_STYLE_MONTH.equals(borrow.getBorrowStyle())) {
            // 等额本息
            isMonth = true;
            // 还款方式 01按月付息到期还本、02按季付息到期还本、03按月等额本息还款、04按季等额本息还款、05到期一次性还本付息、06按月等额本金还款、07按季等额本金还款、08按月等本等息还款、09半年付息到期还本、10随时提前还款、99个性化还款方式，长度2位
            nifaBorrowInfoEntity.setBorrowStyle("03");
        } else if (CustomConstants.BORROW_STYLE_END.equals(borrow.getBorrowStyle())) {
            // 按月计息，到期还本还息
            isMonth = false;
            nifaBorrowInfoEntity.setBorrowStyle("05");
        } else if (CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrow.getBorrowStyle())) {
            // 先息后本
            isMonth = true;
            nifaBorrowInfoEntity.setBorrowStyle("01");
        } else if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrow.getBorrowStyle())) {
            // 按天计息，到期还本息
            isMonth = false;
            nifaBorrowInfoEntity.setBorrowStyle("05");
        } else if (CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrow.getBorrowStyle())) {
            // 等额本金
            isMonth = true;
            nifaBorrowInfoEntity.setBorrowStyle("06");
        }
        if (isMonth) {
            // 还款期数
            nifaBorrowInfoEntity.setRepayPeriod(borrow.getBorrowPeriod() + "");
            // 根据借款订单号获取还款记录
            List<BorrowRepayPlan> borrowRepayPlanList = this.selectBorrowRepayPlanByBorrowNid(borrow.getBorrowNid());
            if (null == borrowRepayPlanList || borrowRepayPlanList.size() <= 0) {
                _log.error(logHeader + "为获取到还款计划（到期还款）！！borrowNid:{}", borrow.getBorrowNid());
                return false;
            }
            // 约定还款计划 每期的还款日期、本金、利息列表，用冒号分隔，用分号分段,结尾不写分号
            String repayPlanListStr = "";
            for (BorrowRepayPlan borrowRepayPlan : borrowRepayPlanList) {
                Integer repayTime = 0;
                if (Validator.isNumber(borrowRepayPlan.getRepayTime())) {
                    repayTime = Integer.parseInt(borrowRepayPlan.getRepayTime());
                    // 字符串拼接
                    repayPlanListStr = repayPlanListStr.concat(GetDate.timestamptoStrYYYYMMDD(repayTime)).concat(":")
                            .concat(borrowRepayPlan.getRepayCapital() + "").concat(":")
                            .concat(borrowRepayPlan.getRepayInterest() + "").concat(";");
                } else {
                    _log.error(logHeader + "该期还款时间为非数字！！borrowNid:{}", borrow.getBorrowNid());
                    return false;
                }
            }
            nifaBorrowInfoEntity.setRepayPlan(repayPlanListStr.substring(0, repayPlanListStr.length() - 1));
            // 最后一期还款日
            lasterDay = borrowRepayPlanList.get(borrowRepayPlanList.size() - 1).getRepayTime();
            lasterRepayTime = Integer.parseInt(lasterDay);
        } else {
            // 还款期数
            nifaBorrowInfoEntity.setRepayPeriod("1");
            lasterDay = borrowRepay.getRepayTime();
            if (Validator.isNumber(lasterDay)) {
                lasterRepayTime = Integer.parseInt(lasterDay);
            } else {
                _log.error(logHeader + "该期还款时间为非数字！！borrowNid:{}", borrow.getBorrowNid());
                return false;
            }
            // 约定还款计划 每期的还款日期、本金、利息列表，用冒号分隔，用分号分段,结尾不写分号
            nifaBorrowInfoEntity.setRepayPlan(GetDate.timestamptoStrYYYYMMDD(lasterRepayTime) + ":" + borrowRepay.getRepayCapital() + ":" + borrowRepay.getRepayInterest());
        }
        // 借款期限 借款合同约定的期限，单位日，按年，月等计算的，需转化为日
        // 借款期限转化为日 还款日-放款日
//        Integer betweenDays = (lasterRepayTime - borrow.getRecoverLastTime()) / (3600 * 24);
        try {
            nifaBorrowInfoEntity.setBorrowDays("" + GetDate.daysBetween(GetDate.timestamptoStrYYYYMMDD(borrow.getRecoverLastTime()), GetDate.timestamptoStrYYYYMMDD(lasterRepayTime)));
        } catch (ParseException e) {
            e.printStackTrace();
            _log.error(logHeader + "借款期限计算失败！！borrowNid:{}", borrow.getBorrowNid());
            return false;
        }
        // 出借利率 合同约定的出借利率（年化，利率固定时填写），8(8)有效数字8位，保留小数点后8位
        nifaBorrowInfoEntity.setBorrowApr(borrow.getBorrowApr().divide(new BigDecimal(100), 8, BigDecimal.ROUND_DOWN).toPlainString());
        // 项目费用 平台向借款人收取的费用金额，15(4)有效数字15位，保留小数点后4位，以元为单位。(项目服务费+还款服务费)
        BigDecimal projectFee = recoverFee.add(borrowRepay.getRepayFee());
        nifaBorrowInfoEntity.setProjectFee(projectFee.toPlainString());
        // 项目费率 平台向借款人收取的费率（按年化计算），项目费用与借款金额的比率，8(8)有效数字8位，保留小数点后8位。
        BigDecimal tmpBD1 = BigDecimal.ZERO;
        BigDecimal tmpBD2 = borrow.getAccount().multiply(new BigDecimal(borrow.getBorrowPeriod()));
        // 被除数不能为0
        if (tmpBD2.compareTo(BigDecimal.ZERO) <= 0) {
            _log.error(logHeader + "项目费率计算失败！！Account：" + borrow.getAccount());
            return false;
        }
        if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrow.getBorrowStyle())) {
            // 按天 项目费用/借款金额/项目天数*365
            tmpBD1 = projectFee.multiply(new BigDecimal("365"));
        } else {
            // 按月 项目费用/借款金额/项目期限*12
            tmpBD1 = projectFee.multiply(new BigDecimal("12"));
        }
        nifaBorrowInfoEntity.setProjectFeeRate(tmpBD1.divide(tmpBD2, 8, BigDecimal.ROUND_DOWN).toPlainString());
        // 其他费用 除项目费用外的其他费用，15(4)有效数字15位，保留小数点后4位 报送0
        nifaBorrowInfoEntity.setOtherFee("0");
        // 还款保证措施 01保证金（风险准备金）、02有担保公司担保、03无担保公司担保、04保险、05信用、06回购、07第三方收购。可多选，用冒号分隔 均报送03
        nifaBorrowInfoEntity.setMeasures("03");
        // 担保方式 抵质押详情有内容报送02 03 其他报送01
        // 资产属性 1:抵押标 2:质押标 3:信用标 4:债权转让标 5:净值标
        if (borrow.getAssetAttributes() == 1) {
            nifaBorrowInfoEntity.setGuaranteeType("02");
        } else if (borrow.getAssetAttributes() == 2) {
            nifaBorrowInfoEntity.setGuaranteeType("03");
        } else {
            nifaBorrowInfoEntity.setGuaranteeType("01");
        }
        // 担保公司名称 不报送
        nifaBorrowInfoEntity.setGuaranteeCompany("");
        // 实际还款记录 项目刚成立尚无还款记录时，填写日期为项目成立日期，本金利息记为零 还款日期、本金、利息、还款来源列表
        nifaBorrowInfoEntity.setRepayDetials(GetDate.timestamptoStrYYYYMMDD(borrow.getRecoverLastTime()) + ":0:0:01");
        // 实际累计本金偿还额
        nifaBorrowInfoEntity.setRepayCaptialYes("0");
        // 实际累计利息偿还额
        nifaBorrowInfoEntity.setRepayInterestYes("0");
        BigDecimal repayCapitalWait = BigDecimal.ZERO;
        BigDecimal repayInterestWait = BigDecimal.ZERO;
        // 放款的时候一分都没还款呀
        for (BorrowRecover borrowRecover : borrowRecoverList) {
            repayCapitalWait = repayCapitalWait.add(borrowRecover.getRecoverCapital());
            repayInterestWait = repayInterestWait.add(borrowRecover.getRecoverInterest());
        }
        // repay表取不到 放到上面是否分期里处理 借款剩余本金余额
        nifaBorrowInfoEntity.setRepayCaptitalWait(repayCapitalWait + "");
        // 放到上面是否分期里处理 借款剩余应付利息
        nifaBorrowInfoEntity.setRepayInterestWait(repayInterestWait + "");
        // 是否支持转让 0否1是，长度1位
        nifaBorrowInfoEntity.setIsCredit("1");
        // 项目状态 01项目新成立、02还款中、03正常还款已结清、04提前还款已结清
        nifaBorrowInfoEntity.setReverifyStatus("01");
        // 逾期原因 如项目状态为05、06、07、08、11、12、13，则需报送该项目的逾期原因。长度512位
        nifaBorrowInfoEntity.setLateReason("");
        // 逾期次数 指已逾期的期数，如项目状态为05、06、07、08、11、12、13，则需报送该项目的逾期次数。
        nifaBorrowInfoEntity.setLateCounts("0");
        // 借款用途
        nifaBorrowInfoEntity.setFinancePurpose(borrow.getFinancePurpose());
        // 出借人个数
        Integer lenderCounts = borrowRecoverList.size();
        nifaBorrowInfoEntity.setLenderCounts(lenderCounts + "");
        return true;
    }

    /**
     * 保存借款详情
     *
     * @param nifaBorrowInfoEntity
     * @return
     */
    @Override
    public void insertNifaBorrowInfo(NifaBorrowInfoEntity nifaBorrowInfoEntity) {
        Query query = new Query();
        Criteria criteria = Criteria.where("projectNo").is(nifaBorrowInfoEntity.getProjectNo()).and("message").is(nifaBorrowInfoEntity.getMessage());
        query.addCriteria(criteria);
        List<NifaBorrowInfoEntity> borrowInfoEntityList = this.nifaBorrowInfoDao.find(query);
        if (null != borrowInfoEntityList && borrowInfoEntityList.size() > 0) {
            nifaBorrowInfoDao.deleteBatch(borrowInfoEntityList);
        }
        nifaBorrowInfoDao.save(nifaBorrowInfoEntity);
    }

    /**
     * 编辑借款公司信息
     *
     * @param borrowUsers
     * @param borrower
     * @param borrowLevelStr
     * @param bank
     * @param nifaBorrowerInfoEntity
     * @return
     */
    @Override
    public boolean selectDualNifaBorrowerUserInfo(BorrowUsers borrowUsers, String borrower, String borrowLevelStr, String bank, NifaBorrowerInfoEntity nifaBorrowerInfoEntity) {
        // 项目唯一编号 社会信用代码、平台序号和报数机构项目编号的组合
        nifaBorrowerInfoEntity.setProjectNo(CustomConstants.COM_SOCIAL_CREDIT_CODE + "1" + borrowUsers.getBorrowNid());
        // 借款人类型 01自然人 02法人
        nifaBorrowerInfoEntity.setBorrowerType("02");
        // 借款人ID CA认证编号
        nifaBorrowerInfoEntity.setUserId(borrower);
        // 证件类型 01身份证/ 01社会信用代码、02组织机构代码、03营业执照编号
        // 证件号码
        if (StringUtils.isNotBlank(borrowUsers.getSocialCreditCode())) {
            nifaBorrowerInfoEntity.setCardType("01");
            nifaBorrowerInfoEntity.setCardNo(borrowUsers.getSocialCreditCode());
            // 证件号码：没有组织机构代码和社会信用代码的就留空
        } else if (StringUtils.isNotBlank(borrowUsers.getCorporateCode())) {
            nifaBorrowerInfoEntity.setCardType("02");
            nifaBorrowerInfoEntity.setCardNo(borrowUsers.getCorporateCode());
        } else if (StringUtils.isNotBlank(borrowUsers.getRegistCode())) {
            nifaBorrowerInfoEntity.setCardNo(borrowUsers.getRegistCode());
            // 15位注册号报送03其他报送99
            if (borrowUsers.getRegistCode().length() == 15){
                nifaBorrowerInfoEntity.setCardType("03");
            } else {
                nifaBorrowerInfoEntity.setCardType("99");
            }
        }
        // 借款人性别 自然人填写
        nifaBorrowerInfoEntity.setSex("");
        // 借款人年平均收入 自然人填写
        nifaBorrowerInfoEntity.setAnnualIncome("");
        // 借款人主要收入来源 不报送
        nifaBorrowerInfoEntity.setMainIncome("");
        // 职业类型 借款人类型为自然人时填写，GB/T 6565-2015 职业分类与代码
        nifaBorrowerInfoEntity.setPosition("");
        // 所属地区 借款人类型为自然人时填写身份证前6位数字；借款人为法人时填写社会统一信用代码或营业执照上的行政区域，6位，如社会统一信用代码为91110107**********，填写110107。
        if (StringUtils.isNotBlank(borrowUsers.getSocialCreditCode())) {
            nifaBorrowerInfoEntity.setArea(borrowUsers.getSocialCreditCode().substring(2, 8));
        } else if (StringUtils.isNotBlank(borrowUsers.getRegistCode())) {
            nifaBorrowerInfoEntity.setArea(getBorrowUsersArea(borrowUsers.getRegistCode()));
        }
        // 实缴资本 不报送
        nifaBorrowerInfoEntity.setContributedCapital("");
        // 注册资本 借款人为法人填写，指人民币金额，以元为单位。15(4)有效数字15位，保留小数点后4位
        // 注册资本 为0 报0
        // 注册资本修改为仅能输入数字 单位为美元的不报送
        if (!"美元".equals(getParamName(CustomConstants.CURRENCY_STATUS, borrowUsers.getCurrencyName()))) {
            nifaBorrowerInfoEntity.setRegCaptial(null != borrowUsers.getRegCaptial() ? borrowUsers.getRegCaptial() : "0");
        }
        // 所属行业 不报送
        nifaBorrowerInfoEntity.setIndustry("");
        // 机构成立时间 借款人为法人填写，填写其机构的成立时间，格式为YYYYMMDD
        // 线上数据：2007年9月25日、2007年9月 三种数据存在 另数据需要补录修正数据为yyyy-mm-dd格式
        nifaBorrowerInfoEntity.setComRegTime(borrowUsers.getComRegTime().replace("-", ""));
        // 收款账户开户行银行名称 开户银行名称，长度80位
        nifaBorrowerInfoEntity.setBank(StringUtils.isNotBlank(bank) ? bank : "");
        // 收款账户开户行所在地区 不报送
        nifaBorrowerInfoEntity.setBankOpenArea("");
        // 借款人信用评级
        if (StringUtils.isBlank(borrowLevelStr)) {
            _log.error(logHeader + "借款人信用评级为空！！borrowNid:{}", borrowUsers.getBorrowNid());
            return false;
        }
        Integer borrowLevel = getBorrowLevel(borrowLevelStr);
        if (borrowLevel >= 0) {
            nifaBorrowerInfoEntity.setBorrowLevel(borrowLevel + "");
        }
        // 借款主体累计借款次数
        Integer count = this.nifaBorrowerInfoCustomizeMapper.selectBorrowUsersCount(borrowUsers.getUsername());
        nifaBorrowerInfoEntity.setBorrowCounts(count + "");
        return true;
    }

    /**
     * 编辑个人借款人信息
     *
     * @param borrowManinfo
     * @param borrower
     * @param borrowLevelStr
     * @param bank
     * @param nifaBorrowerInfoEntity
     * @return
     */
    @Override
    public boolean selectDualNifaBorrowerManInfo(BorrowManinfo borrowManinfo, String borrower, String borrowLevelStr, String bank, NifaBorrowerInfoEntity nifaBorrowerInfoEntity) {

        // 项目唯一编号 社会信用代码、平台序号和报数机构项目编号的组合
        nifaBorrowerInfoEntity.setProjectNo(CustomConstants.COM_SOCIAL_CREDIT_CODE + "1" + borrowManinfo.getBorrowNid());
        // 借款人类型 01自然人 02法人
        nifaBorrowerInfoEntity.setBorrowerType("01");
        // 借款人ID CA认证编号
        nifaBorrowerInfoEntity.setUserId(borrower);
        // 证件类型 01身份证/ 01社会信用代码、02组织机构代码、03营业执照编号
        nifaBorrowerInfoEntity.setCardType("01");
        // 证件号码
        nifaBorrowerInfoEntity.setCardNo(borrowManinfo.getCardNo());
        // 借款人性别 自然人填写
        nifaBorrowerInfoEntity.setSex(borrowManinfo.getSex() + "");
        // 借款人年平均收入 自然人填写
        nifaBorrowerInfoEntity.setAnnualIncome(borrowManinfo.getAnnualIncome());
        // 借款人主要收入来源 不报送
        nifaBorrowerInfoEntity.setMainIncome("");
        // 职业类型 借款人类型为自然人时填写，GB/T 6565-2015 职业分类与代码
        nifaBorrowerInfoEntity.setPosition(borrowManinfo.getPosition());
        // 所属地区 借款人类型为自然人时填写身份证前6位数字；借款人为法人时填写社会统一信用代码或营业执照上的行政区域，6位，如社会统一信用代码为91110107**********，填写110107。
        if (StringUtils.isNotBlank(borrowManinfo.getCardNo())) {
            nifaBorrowerInfoEntity.setArea(borrowManinfo.getCardNo().substring(0, 6));
        }
        // 实缴资本 不报送
        nifaBorrowerInfoEntity.setContributedCapital("");
        // 注册资本 借款人为法人填写，指人民币金额，以元为单位。15(4)有效数字15位，保留小数点后4位
        // 注册资本 为0 报0
        nifaBorrowerInfoEntity.setRegCaptial("");
        // 所属行业 不报送
        nifaBorrowerInfoEntity.setIndustry("");
        // 机构成立时间 借款人为法人填写，填写其机构的成立时间，格式为YYYYMMDD
        nifaBorrowerInfoEntity.setComRegTime("");
        // 收款账户开户行银行名称 开户银行名称，长度80位
        nifaBorrowerInfoEntity.setBank(StringUtils.isNotBlank(bank) ? bank : "");
        // 收款账户开户行所在地区 不报送
        nifaBorrowerInfoEntity.setBankOpenArea("");
        // 借款人信用评级
        if (StringUtils.isBlank(borrowLevelStr)) {
            _log.error(logHeader + "借款人信用评级为空！！borrowNid:{}", borrowManinfo.getBorrowNid());
            return false;
        }
        Integer borrowLevel = getBorrowLevel(borrowLevelStr);
        if (borrowLevel >= 0) {
            nifaBorrowerInfoEntity.setBorrowLevel(borrowLevel + "");
        }
        // 借款主体累计借款次数
        Integer count = this.nifaBorrowerInfoCustomizeMapper.selectBorrowManInfoCount(borrowManinfo.getName());
        nifaBorrowerInfoEntity.setBorrowCounts(count + "");
        return true;
    }

    /**
     * 保存借款人信息
     *
     * @param nifaBorrowerInfoEntity
     * @return
     */
    @Override
    public void insertNifaBorrowerUserInfo(NifaBorrowerInfoEntity nifaBorrowerInfoEntity) {
        Query query = new Query();
        Criteria criteria = Criteria.where("projectNo").is(nifaBorrowerInfoEntity.getProjectNo()).and("message").is(nifaBorrowerInfoEntity.getMessage()).and("userId").is(nifaBorrowerInfoEntity.getUserId());
        query.addCriteria(criteria);
        List<NifaBorrowerInfoEntity> nifaBorrowerInfoEntityList = this.nifaBorrowerInfoDao.find(query);
        if (null != nifaBorrowerInfoEntityList && nifaBorrowerInfoEntityList.size() > 0) {
            nifaBorrowerInfoDao.deleteBatch(nifaBorrowerInfoEntityList);
        }
        nifaBorrowerInfoDao.save(nifaBorrowerInfoEntity);
    }

    /**
     * 查询该标的下出借人信息
     *
     * @param msgBody
     * @param borrowNid
     * @param customerId
     * @return
     */
    @Override
    public NifaTenderInfoEntity selectNifaTenderInfoByBorrowNid(String msgBody, String borrowNid, String customerId) {
        Query query = new Query();
        Criteria criteria = Criteria.where("projectNo").is(CustomConstants.COM_SOCIAL_CREDIT_CODE + "1" + borrowNid).and("message").is(msgBody).and("lenderId").is(customerId);
        query.addCriteria(criteria);
        NifaTenderInfoEntity nifaTenderInfoEntity = nifaTenderInfoDao.findOne(query);
        if (null != nifaTenderInfoEntity) {
            return nifaTenderInfoEntity;
        }
        return null;
    }

    /**
     * 编辑投资人信息
     *
     * @param borrow
     * @param nifaTenderUserInfoCustomize
     * @param nifaTenderInfoEntity
     * @return
     */
    @Override
    public boolean selectDualNifaTenderInfo(Borrow borrow, NifaTenderUserInfoCustomize nifaTenderUserInfoCustomize, NifaTenderInfoEntity nifaTenderInfoEntity) {
        // 项目唯一编号 系统内唯一编号，是社会信用代码、平台序号和报数机构项目编号的组合，长度128位，与项目信息的项目唯一编号保持一致。
        nifaTenderInfoEntity.setProjectNo(CustomConstants.COM_SOCIAL_CREDIT_CODE + "1" + borrow.getBorrowNid());
        // 出借人类型 01自然人,02法人，长度2位
        nifaTenderInfoEntity.setLenderType("01");
        // 出借人ID 平台标识出借人的唯一ID号，长度64位
        nifaTenderInfoEntity.setLenderId(nifaTenderUserInfoCustomize.getUserId() + "");
        // 证件类型 出借人类型为自然人：01身份证 借款人类型为法人：01社会信用代码、02组织机构代码、03营业执照编号，长度2位
        nifaTenderInfoEntity.setCardType("01");
        // 证件号码
        nifaTenderInfoEntity.setIdCard(nifaTenderUserInfoCustomize.getIdcard());
        // 职业类型 不报送
        nifaTenderInfoEntity.setProfessionType("");
        // 所属地区 不报送
        nifaTenderInfoEntity.setArea("");
        // 所属行业 不报送
        nifaTenderInfoEntity.setIndustry("");
        // 出借金额
        nifaTenderInfoEntity.setAccount(nifaTenderUserInfoCustomize.getAccount() + "");
        // 出借状态
        // 01互联网债权类融资-个体直接借贷、
        // 02互联网金融产品及收益权转让融资-客户持有的债权受让、
        // 03互联网金融产品及收益权转让融资-客户持有的债权出让、
        // 04互联网金融产品及收益权转让融资-客户持有的债权部分出让长度2位。当发生出让时，出借状态的变更由协会系统自动处理完成，无需报数机构报送。
        nifaTenderInfoEntity.setLenderStatus("01");
        return true;
    }

    /**
     * 保存投资人信息
     *
     * @param nifaTenderInfoEntity
     */
    @Override
    public void insertNifaTenderInfo(NifaTenderInfoEntity nifaTenderInfoEntity) {
        nifaTenderInfoDao.save(nifaTenderInfoEntity);
    }

    /**
     * 获取汇付绑定的所属银行
     *
     * @param userId
     * @return
     */
    @Override
    public String selectBankFromAccountBank(Integer userId) {
        // 获取汇付绑定银行卡信息 没有返回默认值
        List<String> bankList = this.nifaBorrowerInfoCustomizeMapper.selectBankFromAccountBank(userId);
        if (null != bankList && bankList.size() >0 ){
            return bankList.get(0);
        }
        return "中国工商银行";
    }
}
