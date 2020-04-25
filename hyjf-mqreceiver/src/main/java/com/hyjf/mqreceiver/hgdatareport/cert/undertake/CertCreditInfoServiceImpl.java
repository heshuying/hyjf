package com.hyjf.mqreceiver.hgdatareport.cert.undertake;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mqreceiver.hgdatareport.cert.BaseHgCertReportServiceImpl;
import com.hyjf.mqreceiver.hgdatareport.common.CertCallConstant;
import com.hyjf.mqreceiver.hgdatareport.common.CertCallUtil;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.coupon.CouponTenderCustomize;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * @author nxl
 */

@Service
public class CertCreditInfoServiceImpl extends BaseHgCertReportServiceImpl implements CertCreditInfoService {

    Logger logger = LoggerFactory.getLogger(CertCreditInfoServiceImpl.class);
    private String thisMessName = "承接订单信息推送";
    private String logHeader = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_CERT + " " + thisMessName + "】";

    /**
     * 获取标的的还款信息
     *
     * @param creditTenderNid
     * @return
     */
    @Override
    public JSONArray getBorrowTender(String creditTenderNid, String flag) {
        JSONArray json = new JSONArray();
        if (flag.equals("1")) {
            //代表散标
            List<CreditTender> creditTenderList = getCreditTender(creditTenderNid);
            json = getBorrowCreditTenderInfo(creditTenderList,new JSONArray(),false);
        } else if (flag.equals("2")) {
            //智投服务
            List<HjhDebtCreditTender> hjhDebtCreditTenderList = getHjhDebetCerdeitByAssorderId(creditTenderNid);
            json = getHjhDebtCreditInfo(hjhDebtCreditTenderList,new JSONArray(),false);
        }
        return json;
    }

    /**
     * 日期转换,数据存的int10的时间戳
     *
     * @param repayTime
     * @return
     */
    @Override
    public String dateFormatTransformation(String repayTime, String flg) {
        if (StringUtils.isNotBlank(repayTime)) {
            long intT = Long.parseLong(repayTime) * 1000;
            Date dateRapay = new Date(intT);
            if (flg.equals("H")) {
                //代表获取有时分秒
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateStr = sdf.format(dateRapay);
                return dateStr;
            }
            if (flg.equals("Y")) {
                //代表只有年与日
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String dateStr = sdf.format(dateRapay);
                return dateStr;
            }

        }
        return null;
    }

    /**
     * 获取本金投资和优惠券投资关联表信息
     *
     * @param nid
     * @return
     */
    private CouponRealTender getCouponRealTenderInfoByTenderNid(String nid) {
        CouponRealTenderExample example = new CouponRealTenderExample();
        CouponRealTenderExample.Criteria cra = example.createCriteria();
        cra.andRealTenderIdEqualTo(nid);
        List<CouponRealTender> couponRealTenderList = couponRealTenderMapper.selectByExample(example);
        if (null != couponRealTenderList && couponRealTenderList.size() > 0) {
            return couponRealTenderList.get(0);
        }
        return null;
    }

    private BorrowCredit getBorrowCreditByTenderNid(int creditTenderNid) {
        BorrowCreditExample example = new BorrowCreditExample();
        BorrowCreditExample.Criteria cra = example.createCriteria();
        cra.andCreditNidEqualTo(creditTenderNid);
        List<BorrowCredit> borrowCreditList = borrowCreditMapper.selectByExample(example);
        if (null != borrowCreditList && borrowCreditList.size() > 0) {
            return borrowCreditList.get(0);
        }
        return null;
    }

    private List<CreditTender> getCreditTender(String assignNid) {
        CreditTenderExample example = new CreditTenderExample();
        CreditTenderExample.Criteria cra = example.createCriteria();
        // cra.andCreditNidEqualTo(assignNid);
        cra.andAssignNidEqualTo(assignNid);
        List<CreditTender> creditTenderList = creditTenderMapper.selectByExample(example);
        if (null != creditTenderList && creditTenderList.size() > 0) {
            return creditTenderList;
        }
        return null;
    }


    /**
     * 查询散标的承接信息
     *
     * @param creditTenderList
     * @return
     */
    @Override
    public JSONArray getBorrowCreditTenderInfo(List<CreditTender> creditTenderList,JSONArray json,boolean isOld) {
        if (null != creditTenderList && creditTenderList.size() > 0) {
            for (CreditTender creditTender : creditTenderList) {
                Map<String, Object> param = new LinkedHashMap<String, Object>();
                int intCredit = Integer.parseInt(creditTender.getCreditNid());
                //查找汇转让标的表
                BorrowCredit borrowCredit = getBorrowCreditByTenderNid(intCredit);
                BigDecimal creditD = borrowCredit.getCreditDiscount().divide(new BigDecimal("100"));
                //承接人用户标示 Hash
                String idHash = getUserIdcardHash(creditTender.getUserId());
                //6.承接浮动金额：散标转让算法 承接本金*折让率 智投的转让报送0  智投的转让报送0
                BigDecimal bigDecimalCredit = creditTender.getAssignCapital().multiply(creditD);
                bigDecimalCredit = bigDecimalCredit.setScale(2, BigDecimal.ROUND_DOWN);
                //7.承接预期年华收益率：原项目预期年化收益率  智投报送智投产品年化收益率
                Borrow borrow = this.getBorrowByBorrowNid(creditTender.getBidNid());
                // 投资年化收益率
                String rate = CertCallUtil.convertLoanRate(borrow.getBorrowApr(), borrow.getBorrowPeriod(), borrow.getBorrowStyle());
                //8.承接时间：系统记录的承接时间
                String tenderDate = dateFormatTransformation(creditTender.getAddTime(), "H");
                //9.投资红包：抵扣券报送 红包面值 加息券报送加息券到期收益  没使用券报送0
                //获取优惠券信息
                BigDecimal bigDecimalCouponQuota = getRedPackageSum(creditTender.getAssignNid());

                //接口版本号
                param.put("version", CertCallConstant.CERT_CALL_VERSION);
                //平台编号
                param.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
                //1.承接信息编号：承接订单号
                param.put("unFinClaimId", creditTender.getAssignNid());
                //2.转让编号：债转编号
                param.put("transferId", creditTender.getCreditNid());
                //3.债权信息编号：该转让对应的原始投资订单编号
                param.put("finClaimId", creditTender.getCreditTenderNid());
                //承接人用户标示 Hash
                param.put("userIdcardHash", idHash);
                //4.承接人投资资金额：承接本金金额
                param.put("takeAmount", creditTender.getAssignCapital().toString());
                //5.承接利息金额：承接人承接本金对应的待收利息金额
                param.put("takeInterest", creditTender.getAssignInterest().toString());
                //6.承接浮动金额：散标转让算法承接本金*折让率 智投的转让报送0  智投的转让报送0
                //承接本金*折让率  数值前加负
                param.put("floatMoney", "-" + bigDecimalCredit.toString());
                //7.承接预期年华收益率：原项目预期年化收益率  智投报送智投产品年化收益率
                param.put("takeRate", rate);
                //8.承接时间：系统记录的承接时间
                param.put("takeTime", tenderDate);
                //9.投资红包：抵扣券报送 红包面值 加息券报送加息券到期收益  没使用券报送0
                param.put("redpackage", bigDecimalCouponQuota.toString());
                //10.封闭截至时间：散标报送 到期日  智投报送承接日
                param.put("lockTime", dateFormatTransformation(borrow.getRepayLastTime(), "Y"));
                //是否是历史数据
                if(isOld){
                    //是否是历史数据
                    // groupByDate  旧数据上报排序 按月用
                    String groupByDate= tenderDate.split("-")[0]+"-"+tenderDate.split("-")[1];
                    param.put("groupByDate",groupByDate);
                }
                json.add(param);
            }
        }
        return json;
    }

    /**
     * 获取智投承接信息
     *
     * @param hjhDebtCreditTenderList
     * @return
     */
    @Override
    public JSONArray getHjhDebtCreditInfo(List<HjhDebtCreditTender> hjhDebtCreditTenderList,JSONArray json,boolean isOld) {
        if (null != hjhDebtCreditTenderList && hjhDebtCreditTenderList.size() > 0) {
            for (HjhDebtCreditTender hjhDebtCreditTender : hjhDebtCreditTenderList) {
                Map<String, Object> param = new LinkedHashMap<String, Object>();
                //查找计划信息
                HjhPlan hjhPlan = this.getHjhPlanInfoByPlanNid(hjhDebtCreditTender.getAssignPlanNid());
                String userIdcardHash = getUserIdcardHash(hjhDebtCreditTender.getUserId());
                //8.承接时间：系统记录的承接时间
                String takeTime = dateFormatTransformation(hjhDebtCreditTender.getCreateTime().toString(), "H");
                //9.投资红包：抵扣券报送 红包面值 加息券报送加息券到期收益  没使用券报送0
                BigDecimal bigDecimalCouponQuota = getRedPackageSum(hjhDebtCreditTender.getAssignPlanOrderId());
                //10.封闭截至时间：散标报送 到期日  智投报送承接日
                String lockTime = dateFormatTransformation(hjhDebtCreditTender.getCreateTime().toString(), "Y");

                //接口版本号
                param.put("version", CertCallConstant.CERT_CALL_VERSION);
                //平台编号
                param.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
                //1.承接信息编号：承接订单号
                param.put("unFinClaimId", hjhDebtCreditTender.getAssignOrderId());
                //2.转让编号：债转编号
                param.put("transferId", hjhDebtCreditTender.getCreditNid());
                //3.债权信息编号：该转让对应的原始投资订单编号
                param.put("finClaimId", hjhDebtCreditTender.getInvestOrderId());
                //承接人用户标示 Hash
                param.put("userIdcardHash", userIdcardHash);
                //4.承接人投资资金额：承接本金金额
                param.put("takeAmount", hjhDebtCreditTender.getAssignCapital());
                //5.承接利息金额：承接人承接本金对应的待收利息金额
                param.put("takeInterest", hjhDebtCreditTender.getAssignInterest());
                //6.承接浮动金额：散标转让算法承接本金*折让率 智投的转让报送0  智投的转让报送0
                param.put("floatMoney", "0");
                //7.承接预期年华收益率：原项目预期年化收益率  智投报送智投产品年化收益率
                String takeRate = CertCallUtil.convertLoanRate(hjhPlan.getExpectApr(),0,null);
                param.put("takeRate", takeRate);
                //8.承接时间：系统记录的承接时间
                param.put("takeTime", takeTime);
                //9.投资红包：抵扣券报送 红包面值 加息券报送加息券到期收益  没使用券报送0
                param.put("redpackage", bigDecimalCouponQuota.toString());
                //10.封闭截至时间：散标报送 到期日  智投报送承接日
                param.put("lockTime", lockTime);
                //是否是历史数据
                if(isOld){
                    //是否是历史数据
                    // groupByDate  旧数据上报排序 按月用
                    String groupByDate= takeTime.split("-")[0]+"-"+takeTime.split("-")[1];
                    param.put("groupByDate",groupByDate);
                }
                json.add(param);
            }

        }
        return json;
    }

    /**
     * 用户身份证号hash值
     *
     * @param userId
     * @return
     */
    private String getUserIdcardHash(int userId) {
        UsersInfo users = this.getUsersInfoByUserId(userId);
        String idHash = "";
        if (null != users) {
            try {
                idHash = tool.idCardHash(users.getIdcard());
            } catch (Exception e) {
                logger.error(logHeader + " 用户标示哈希出错！", e);
            }
        }
        return idHash;
    }

    private List<HjhDebtCreditTender> getHjhDebetCerdeitByAssorderId(String assignOrderId) {
        HjhDebtCreditTenderExample example = new HjhDebtCreditTenderExample();
        HjhDebtCreditTenderExample.Criteria cra = example.createCriteria();
        cra.andAssignOrderIdEqualTo(assignOrderId);
        List<HjhDebtCreditTender> creditTenderList = hjhDebtCreditTenderMapper.selectByExample(example);
        if (null != creditTenderList && creditTenderList.size() > 0) {
            return creditTenderList;
        }
        return null;
    }

    /**
     * 根据计划编号查找计划信息
     *
     * @param planNid
     * @return
     */
    private HjhPlan getHjhPlanInfoByPlanNid(String planNid) {
        HjhPlanExample example = new HjhPlanExample();
        HjhPlanExample.Criteria cra = example.createCriteria();
        cra.andPlanNidEqualTo(planNid);
        List<HjhPlan> creditTenderList = hjhPlanMapper.selectByExample(example);
        if (null != creditTenderList && creditTenderList.size() > 0) {
            return creditTenderList.get(0);
        }
        return null;
    }

    /**
     * 根据加入计划单号查找加入计划信息
     *
     * @param orderId
     * @return
     */
    @Override
    public HjhAccede getHjhAccedeByOrderId(String orderId) {
        HjhAccedeExample example = new HjhAccedeExample();
        HjhAccedeExample.Criteria cra = example.createCriteria();
        cra.andAccedeOrderIdEqualTo(orderId);
        List<HjhAccede> creditTenderList = hjhAccedeMapper.selectByExample(example);
        if (null != creditTenderList && creditTenderList.size() > 0) {
            return creditTenderList.get(0);
        }
        return null;
    }

    /**
     * 获取投资红包金额
     *
     * @param realTenderId
     * @return
     */
    @Override
    public BigDecimal getRedPackageSum(String realTenderId) {
        BigDecimal bigDecimalCouponQuota = new BigDecimal("0");
        //9.投资红包：抵扣券报送 红包面值 加息券报送加息券到期收益  没使用券报送0
        //获取优惠券信息
        CouponRealTender couponRealTender = getCouponRealTenderInfoByTenderNid(realTenderId);
        if (null != couponRealTender) {
            //代表使用了红包,各种优惠券等
            /*CouponTenderCustomize couponTenderCustomize = couponTenderCustomizeMapper.selectBorrowTenderCpnByOrderId(couponRealTender.getCouponTenderId());
            //coupon_type = 1 体验金,coupon_type = 2 加息券,hcc.coupon_type = 3 代金券 else 加息券
            if (couponTenderCustomize.getCouponType().equals("3")) {
                //代金券：代金券面值+利息
                if (StringUtils.isNotBlank(couponTenderCustomize.getCouponQuota())) {
                    bigDecimalCouponQuota = new BigDecimal(couponTenderCustomize.getCouponQuota());
                }
            }*/
            //优惠券利息
            BigDecimal bigSumRecover = new BigDecimal("0");
            String sumRecoverInst = couponTenderCustomizeMapper.sunRecoverInterest(couponRealTender.getCouponTenderId());
            if (StringUtils.isNotBlank(sumRecoverInst)) {
                bigSumRecover = new BigDecimal(sumRecoverInst);
            }
            bigDecimalCouponQuota = bigDecimalCouponQuota.add(bigSumRecover);
        }
        return bigDecimalCouponQuota;
    }
}
