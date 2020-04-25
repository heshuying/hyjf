package com.hyjf.batch.hjh.borrow.laterepay;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.cache.RedisConstants;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.common.validator.Validator;
import com.hyjf.mongo.hgdatareport.dao.NifaBorrowInfoDao;
import com.hyjf.mongo.hgdatareport.entity.NifaBorrowInfoEntity;
import com.hyjf.mybatis.model.auto.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 标的还款逾期短信提醒
 *
 * @author lsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年8月15日
 */
@Service
public class LateRepayServiceImpl extends BaseServiceImpl implements LateRepayService {

    Logger _log = LoggerFactory.getLogger(LateRepayServiceImpl.class);

    @Autowired
    @Qualifier("myAmqpTemplate")
    private RabbitTemplate rabbitTemplate;

    @Autowired
    NifaBorrowInfoDao nifaBorrowInfoDao;

    /**
     * 检索逾期的还款标的
     *
     * @return
     * @author lsy
     */
    @Override
    public List<BorrowRepay> selectOverdueBorrowList() {
        BorrowRepayExample example = new BorrowRepayExample();
        example.createCriteria().andRepayTimeLessThanOrEqualTo(String.valueOf(GetDate.getDayEnd10(GetDate.getTodayBeforeOrAfter(-1)))).andRepayTypeEqualTo("wait");
        List<BorrowRepay> borrowRepayList = borrowRepayMapper.selectByExample(example);

        if (borrowRepayList == null) {
            return new ArrayList<BorrowRepay>();
        }

        return borrowRepayList;
    }

    /**
     * 推送消息到MQ
     */
    @Override
    public void sendToMQ(BorrowRepay borrowRepay) {
        // 加入到消息队列
        Map<String, String> params = new HashMap<String, String>();
        params.put("mqMsgId", GetCode.getRandomCode(10));
        params.put("borrowNid", borrowRepay.getBorrowNid());
        params.put("nid", "");
        params.put("contractStatus", "2");

        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, RabbitMQConstants.ROUTINGKEY_NIFA_REPAY_LATE, JSONObject.toJSONString(params));
    }

    /**
     * 推送消息到MQ
     */
    @Override
    public void sendToMQCredit(String creditTenderNid) {
        // 加入到消息队列
        Map<String, String> params = new HashMap<String, String>();
        params.put("mqMsgId", GetCode.getRandomCode(10));
        params.put("borrowNid", "");
        params.put("nid", creditTenderNid);
        params.put("contractStatus", "6");
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, RabbitMQConstants.ROUTINGKEY_NIFA_REPAY_LATE, JSONObject.toJSONString(params));
    }

    @Override
    public List<BorrowRecover> selectBorrowRecoverCredit() {
        List<BorrowRecover> borrowRecoverList = this.nifaContractStatusCustomizeMapper.selectBorrowRecoverCredit();
        if (null != borrowRecoverList && borrowRecoverList.size() > 0) {
            return borrowRecoverList;
        }
        return null;
    }
    // add by liushouyi nifa2 20181203 start
    /**
     * 互金审计二期逾期标的处理
     *
     * @param borrowRepay
     * @return
     */
    @Override
    public NifaBorrowInfoEntity dualNifaBorrowInfo(BorrowRepay borrowRepay) {
        String borrowNid = borrowRepay.getBorrowNid();
        // 获取标的借款信息
        Borrow borrow = this.getBorrowByNid(borrowNid);
        if (null == borrow) {
            _log.error("互金审计二期逾期标的处理，未获取到标的信息，borrowNid:{}" + borrowNid);
            return null;
        }
        // 获取标的放款信息
        List<BorrowRecover> borrowRecoverList = this.selectBorrowRecoverListByBorrowNid(borrowNid);
        if (null == borrowRecoverList || borrowRecoverList.size() <= 0) {
            _log.error("互金审计二期逾期标的处理，未获取到标的放款信息，borrowNid:{}" + borrowNid);
            return null;
        }
        // 计算放款服务费
        BigDecimal recoverFee = BigDecimal.ZERO;
        for (BorrowRecover borrowRecover : borrowRecoverList){
            recoverFee.add(borrowRecover.getRecoverFee());
        }
        
        NifaBorrowInfoEntity nifaBorrowInfoEntity = new NifaBorrowInfoEntity();
        // 借款详情处理
        nifaBorrowInfoEntity.setMessage("borrowNid:" + borrowNid + "repayPeriod:" + borrowRepay.getRepayPeriod());
        // 报送状态初始化
        nifaBorrowInfoEntity.setReportStatus("0");
        // 项目唯一编号 系统内唯一编号，是社会信用代码、平台序号和报数机构项目编号的组合，长度128位
        nifaBorrowInfoEntity.setProjectNo(CustomConstants.COM_SOCIAL_CREDIT_CODE + "1" + borrow.getBorrowNid());
        // 社会信用代码
        nifaBorrowInfoEntity.setPlatformNo(CustomConstants.COM_SOCIAL_CREDIT_CODE);
        // 平台序号
        nifaBorrowInfoEntity.setPlatformNo("1");
        // 项目编号 报数机构内部项目编号，长度64位
        nifaBorrowInfoEntity.setOrganizationNo(borrow.getBorrowNid());
        // 项目类型 01互联网债权类融资-个体直接借贷，02互联网债权类融资-互联网小额贷款，03其他债权类融资业务，长度2位
        nifaBorrowInfoEntity.setBorrowType("01");
        // 项目名称
        nifaBorrowInfoEntity.setBorrowName(borrow.getProjectName());
        // 项目成立日期 借款人和出借人债权债务关系的成立日期，格式为YYYYMMDD
        if (null == borrow.getRecoverLastTime()) {
            _log.error("互金审计二期逾期标的处理，最终放款日为空！！borrowNid:{}", borrow.getBorrowNid());
            return null;
        }
        try {
            nifaBorrowInfoEntity.setRecoverTime(GetDate.timestamptoStrYYYYMMDD(borrow.getRecoverLastTime()).replaceAll("-", ""));
        } catch (Exception e) {
            e.printStackTrace();
            _log.error("互金审计二期逾期标的处理，最终放款日格式化失败！！borrowNid:{}", borrow.getBorrowNid());
            return null;
        }
        // 借款金额
        nifaBorrowInfoEntity.setAccount(borrow.getAccount() + "");
        // 借款币种 GB/T 12406-2008，表示资金和货币的代码，长度3位
        nifaBorrowInfoEntity.setCurrency("CNY");
        // 借款起息日 借款合同约定的起息日期，格式为YYYYMMDD 生产库：即收益起始日，为项目认购完成日。
        nifaBorrowInfoEntity.setBorrowInterestTime(nifaBorrowInfoEntity.getRecoverTime());
        // 借款到期日期 借款合同约定的到期日期，格式为YYYYMMDD
        if (null == borrow.getBorrowEndTime()) {
            _log.error("互金审计二期逾期标的处理，借款到期日为空！！borrowNid:{},到期日:{}", borrow.getBorrowNid(), borrow.getBorrowEndTime());
            return null;
        }
        if (!Validator.isNumber(borrow.getBorrowEndTime())) {
            _log.error("互金审计二期逾期标的处理，借款到期日含非数字！！borrowNid:{},到期日:{}", borrow.getBorrowNid(), borrow.getBorrowEndTime());
            return null;
        }
        Integer borrowEndTime = Integer.parseInt(borrow.getBorrowEndTime());
        try {
            nifaBorrowInfoEntity.setBorrowEndTime(GetDate.timestamptoStrYYYYMMDD(borrowEndTime).replaceAll("-", ""));
        } catch (Exception e) {
            e.printStackTrace();
            _log.error("互金审计二期逾期标的处理，最终放款日格式化失败！！borrowNid:{}", borrow.getBorrowNid());
            return null;
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
            // 根据借款订单号获取还款记录
            List<BorrowRepayPlan> borrowRepayPlanList = this.selectBorrowRepayPlanByBorrowNid(borrow.getBorrowNid());
            if (null == borrowRepayPlanList || borrowRepayPlanList.size() <= 0) {
                _log.error("互金审计二期逾期标的处理，为获取到还款计划（到期还款）！！borrowNid:{}", borrow.getBorrowNid());
                return null;
            }
            // 约定还款计划 每期的还款日期、本金、利息列表，用冒号分隔，用分号分段,结尾不写分号
            String repayPlanListStr = "";
            String repayDetilsStr = "";
            Integer laterCount = 0;
            for (BorrowRepayPlan borrowRepayPlan : borrowRepayPlanList) {
                Integer repayTime;
                if (Validator.isNumber(borrowRepayPlan.getRepayTime())) {
                    repayTime = Integer.parseInt(borrowRepayPlan.getRepayTime());
                    // 字符串拼接
                    repayPlanListStr = repayPlanListStr.concat(GetDate.timestamptoStrYYYYMMDD(repayTime)).concat(":")
                            .concat(borrowRepay.getRepayCapitalYes() + "").concat(":")
                            .concat(borrowRepay.getRepayInterest() + "").concat(";");
                    // 已还款的期数
                    if (borrowRepayPlan.getRepayPeriod() <= borrowRepay.getRepayPeriod()) {
                        if (borrowRepayPlanList.get(0).getAdvanceStatus() == 3){
                            // 统计当前标的逾期次数
                            laterCount = laterCount + 1;
                        }
                        repayDetilsStr = repayDetilsStr.concat(GetDate.timestamptoStrYYYYMMDD(repayTime)).concat(":")
                                .concat(borrowRepay.getRepayCapitalYes() + "").concat(":")
                                .concat(borrowRepay.getRepayInterestYes() + "").concat(":01;");
                    }
                } else {
                    _log.error("互金审计二期逾期标的处理，该期还款时间为非数字！！borrowNid:{}", borrow.getBorrowNid());
                    return null;
                }
            }
            nifaBorrowInfoEntity.setRepayPlan(repayPlanListStr.substring(0, repayPlanListStr.length() - 1));
            // 实际还款记录 项目刚成立尚无还款记录时，填写日期为项目成立日期，本金利息记为零 还款日期、本金、利息、还款来源列表
            nifaBorrowInfoEntity.setRepayDetials(repayDetilsStr.substring(0, repayPlanListStr.length() - 1));
            // 最后一期还款日
            lasterDay = borrowRepayPlanList.get(borrowRepayPlanList.size() - 1).getRepayTime();
            lasterRepayTime = Integer.parseInt(lasterDay);
            // 逾期次数 指已逾期的期数，如项目状态为05、06、07、08、11、12、13，则需报送该项目的逾期次数。
            nifaBorrowInfoEntity.setLateCounts(laterCount + "");
            // 项目状态 01项目新成立、02还款中、03正常还款已结清、04提前还款已结清
            if (borrowRepay.getRepayPeriod().equals(borrow.getBorrowPeriod())) {
                nifaBorrowInfoEntity.setReverifyStatus("03");
                // 逾期原因 如项目状态为05、06、07、08、11、12、13，则需报送该项目的逾期原因。长度512位
                nifaBorrowInfoEntity.setLateReason("");
            } else {
                nifaBorrowInfoEntity.setReverifyStatus("02");
                // 逾期原因 如项目状态为05、06、07、08、11、12、13，则需报送该项目的逾期原因。长度512位
                if (borrowRepayPlanList.get(0).getAdvanceStatus() == 3){
                    String laterRepayReason = RedisUtils.get(RedisConstants.LATER_REPAY_REASON);
                    nifaBorrowInfoEntity.setLateReason(laterRepayReason);
                }
            }
        } else {
            lasterDay = borrowRepay.getRepayTime();
            if (Validator.isNumber(lasterDay)) {
                lasterRepayTime = Integer.parseInt(lasterDay);
            } else {
                _log.error("互金审计二期逾期标的处理，该期还款时间为非数字！！borrowNid:{}", borrow.getBorrowNid());
                return null;
            }
            // 约定还款计划 每期的还款日期、本金、利息列表，用冒号分隔，用分号分段,结尾不写分号
            nifaBorrowInfoEntity.setRepayPlan(GetDate.timestamptoStrYYYYMMDD(lasterRepayTime) + ":" + borrowRepay.getRepayAccount() + ":" + borrowRepay.getRepayInterest());
            // 实际还款记录 项目刚成立尚无还款记录时，填写日期为项目成立日期，本金利息记为零 还款日期、本金、利息、还款来源列表
            nifaBorrowInfoEntity.setRepayDetials(GetDate.timestamptoStrYYYYMMDD(lasterRepayTime) + ":" + borrowRepay.getRepayCapitalYes() + "" + ":" + borrowRepay.getRepayInterestYes() + ":01");
            // 项目状态 01项目新成立、02还款中、03正常还款已结清、04提前还款已结清
            // 判断是否提前、逾期还款1：提前3：逾期
            if (borrowRepay.getAdvanceStatus() == 1) {
                nifaBorrowInfoEntity.setReverifyStatus("04");
                // 逾期原因 如项目状态为05、06、07、08、11、12、13，则需报送该项目的逾期原因。长度512位
                nifaBorrowInfoEntity.setLateReason("");
            } else if (borrowRepay.getAdvanceStatus() == 3) {
                nifaBorrowInfoEntity.setReverifyStatus("13");
                // 逾期原因 如项目状态为05、06、07、08、11、12、13，则需报送该项目的逾期原因。长度512位
                String laterRepayReason = RedisUtils.get(RedisConstants.LATER_REPAY_REASON);
                if (StringUtils.isNotBlank(laterRepayReason)){
                    nifaBorrowInfoEntity.setLateReason(laterRepayReason);
                }
            }
            // 逾期次数 指已逾期的期数，如项目状态为05、06、07、08、11、12、13，则需报送该项目的逾期次数。
            nifaBorrowInfoEntity.setLateCounts("0");
        }
        // 借款期限 借款合同约定的期限，单位日，按年，月等计算的，需转化为日
        // 借款期限转化为日 还款日-放款日
        Integer betweenDays = (lasterRepayTime - borrow.getRecoverLastTime()) / (3600 * 24);
        nifaBorrowInfoEntity.setBorrowDays(betweenDays + "");
        // 出借利率 合同约定的出借利率（年化，利率固定时填写），8(8)有效数字8位，保留小数点后8位
        nifaBorrowInfoEntity.setBorrowApr(borrow.getBorrowApr().divide(new BigDecimal(100), 8,BigDecimal.ROUND_HALF_UP) + "");
        // 项目费用 平台向借款人收取的费用金额，15(4)有效数字15位，保留小数点后4位，以元为单位。(项目服务费+还款服务费)
        BigDecimal projectFee = recoverFee.add(borrowRepay.getRepayFee());
        nifaBorrowInfoEntity.setProjectFee(projectFee + "");
        // 项目费率 平台向借款人收取的费率（按年化计算），项目费用与借款金额的比率，8(8)有效数字8位，保留小数点后8位。
        if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrow.getBorrowStyle())) {
            // 按天 项目费用/借款金额/项目天数*365
            nifaBorrowInfoEntity.setProjectFeeRate(projectFee.multiply(new BigDecimal("365")).divide(borrow.getAccount(), 8,BigDecimal.ROUND_HALF_UP) + "");
        } else {
            // 按月 项目费用/借款金额/项目期限*12
            nifaBorrowInfoEntity.setProjectFeeRate(projectFee.multiply(new BigDecimal("12")).divide(borrow.getAccount(), 8,BigDecimal.ROUND_HALF_UP) + "");
        }
        // 其他费用 除项目费用外的其他费用，15(4)有效数字15位，保留小数点后4位 报送0
        nifaBorrowInfoEntity.setOtherFee("0");
        // 还款保证措施 01保证金（风险准备金）、02有担保公司担保、03无担保公司担保、04保险、05信用、06回购、07第三方收购。可多选，用冒号分隔 均报送03
        nifaBorrowInfoEntity.setMeasures("03");
        // 还款期数
        nifaBorrowInfoEntity.setRepayPeriod(borrow.getBorrowPeriod() + "");
        // 抵质押详情有内容报送02 03 其他报送01
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
        // 实际累计本金偿还额
        nifaBorrowInfoEntity.setRepayCaptialYes(borrowRepay.getRepayCapitalYes() + "");
        // 实际累计利息偿还额
        nifaBorrowInfoEntity.setRepayInterestYes(borrowRepay.getRepayInterestYes() + "");
        // 借款剩余本金余额
        nifaBorrowInfoEntity.setRepayCaptitalWait(borrowRepay.getRepayCapitalWait() + "");
        // 借款剩余应付利息
        nifaBorrowInfoEntity.setRepayInterestWait(borrowRepay.getRepayInterestWait() + "");
        // 是否支持转让 0否1是，长度1位
        nifaBorrowInfoEntity.setIsCredit("1");
        // 借款用途
        nifaBorrowInfoEntity.setFinancePurpose(borrow.getFinancePurpose());
        // 出借人个数
        Integer lenderCounts = borrowRecoverList.size();
        nifaBorrowInfoEntity.setLenderCounts(lenderCounts + "");
        
        return nifaBorrowInfoEntity;
    }

    /**
     * 逾期上送数据保存
     *
     * @param nifaBorrowInfoEntity
     */
    @Override
    public void insertNifaBorrowInfo(NifaBorrowInfoEntity nifaBorrowInfoEntity) {
        nifaBorrowInfoDao.save(nifaBorrowInfoEntity);
    }

    /**
     * 根据借款编号获取放款信息
     *
     * @param borrowNid
     * @return
     */
    private List<BorrowRecover> selectBorrowRecoverListByBorrowNid(String borrowNid) {
        BorrowRecoverExample example = new BorrowRecoverExample();
        example.createCriteria().andBorrowNidEqualTo(borrowNid);
        List<BorrowRecover> borrowRecoverList = this.borrowRecoverMapper.selectByExample(example);
        if (null != borrowRecoverList && borrowRecoverList.size() > 0) {
            return borrowRecoverList;
        }
        return null;
    }

    /**
     * 根据借款编号获取还款计划 分期还款
     *
     * @param borrowNid
     * @return
     */
    private List<BorrowRepayPlan> selectBorrowRepayPlanByBorrowNid(String borrowNid) {
        BorrowRepayPlanExample example = new BorrowRepayPlanExample();
        example.createCriteria().andBorrowNidEqualTo(borrowNid);
        example.setOrderByClause("repay_period asc");
        List<BorrowRepayPlan> borrowRepayPlanList = this.borrowRepayPlanMapper.selectByExample(example);
        if (null != borrowRepayPlanList && borrowRepayPlanList.size() > 0) {
            return borrowRepayPlanList;
        }
        return null;
    }
    // add by liushouyi nifa2 20181203
}
