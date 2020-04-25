package com.hyjf.mqreceiver.hgdatareport.cert.creditor;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mqreceiver.hgdatareport.cert.BaseHgCertReportServiceImpl;
import com.hyjf.mqreceiver.hgdatareport.cert.status.CertBorrowStatusService;
import com.hyjf.mqreceiver.hgdatareport.cert.undertake.CertCreditInfoService;
import com.hyjf.mqreceiver.hgdatareport.common.CertCallConstant;
import com.hyjf.mqreceiver.hgdatareport.common.CertCallUtil;
import com.hyjf.mybatis.mapper.auto.BorrowRecoverMapper;
import com.hyjf.mybatis.mapper.auto.BorrowTenderMapper;
import com.hyjf.mybatis.model.auto.*;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author nxl
 */

@Service
public class CertTenderInfoServiceImpl extends BaseHgCertReportServiceImpl implements CertTenderInfoService {

    @Autowired
    BorrowTenderMapper borrowTenderMapper;
    @Autowired
    CertCreditInfoService certCreditInfoService;
    @Autowired
    CertBorrowStatusService certBorrowStatusService;

    @Autowired
    BorrowRecoverMapper borrowRecoverMapper;


    Logger logger = LoggerFactory.getLogger(CertTenderInfoServiceImpl.class);
    private String thisMessName = "债权信息信息推送";
    private String logHeader = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_CERT + " " + thisMessName + "】";

    /**
     * 获取标的的债权信息
     *
     * @param borrowNid
     * @return
     */
    @Override
    public JSONArray getBorrowTender(String borrowNid, JSONArray json,boolean isOld) {
        //JSONArray json = new JSONArray();
        //标的信息
        Borrow borrow = this.getBorrowByBorrowNid(borrowNid);
        //如果标的信息不为空,而且标的状态为放款中,即标的放款成功
        if (null != borrow && borrow.getStatus() == 4) {
            //原产品信息编号
            String sourceFinancingCode = "-1";
            BigDecimal sumRedPackage = new BigDecimal("0");
            BorrowTenderExample example = new BorrowTenderExample();
            BorrowTenderExample.Criteria cra = example.createCriteria();
            cra.andBorrowNidEqualTo(borrowNid);
            List<BorrowTender> borrowTenderList = borrowTenderMapper.selectByExample(example);
            if (null != borrowTenderList && borrowTenderList.size() > 0) {
                for (BorrowTender borrowTender : borrowTenderList) {
                    //投资人用户标示 Hash
                    String userIdcardHash = "";
                    //投资计息时间
                    String invTime = "";
                    //封闭截止时间
                    String lockTime = "";
                    Map<String, Object> param = new LinkedHashMap<String, Object>();
                    //投资预期年化收益率
                    String rate = CertCallUtil.convertLoanRate(borrow.getBorrowApr(), borrow.getBorrowPeriod(), borrow.getBorrowStyle());
                    //
                    //获取用户信息
                    UsersInfo usersInfo = this.getUsersInfoByUserId(borrowTender.getUserId());
                    try {
                        //投资人用户标示 Hash
                        userIdcardHash = tool.idCardHash(usersInfo.getIdcard());
                    } catch (Exception e) {
                        logger.error(logHeader + " 用户标示哈希出错！", e);
                    }
                    //
                    if (StringUtils.isNotBlank(borrowTender.getAccedeOrderId())) {
                        //计划
                        //根据智投编号查找智投信息
                        HjhAccede hjhAccede = certCreditInfoService.getHjhAccedeByOrderId(borrowTender.getAccedeOrderId());
                        if(null!=hjhAccede){
                            //投资计息时间
                            BorrowRecover borrowRecover = getRecoverDateByTenderNid(borrowTender.getNid(), borrowNid,borrowTender.getAccedeOrderId());
                            invTime = dateFormatTransformation(borrowRecover.getAddtime());
                            sumRedPackage = certCreditInfoService.getRedPackageSum(hjhAccede.getAccedeOrderId());
                            //封闭截止时间：智投报送计息日
                            lockTime = certCreditInfoService.dateFormatTransformation(hjhAccede.getAddTime().toString(), "Y");
                        }
                    } else {
                        //红包
                        sumRedPackage = certCreditInfoService.getRedPackageSum(borrowTender.getNid());
                        //投资计息时间
                        BorrowRecover borrowRecover = getRecoverDateByTenderNid(borrowTender.getNid(), borrowNid,null);
                        //计息时间
                        invTime = dateFormatTransformation(borrowRecover.getAddtime());
                        //封闭截止时间：散标报送计息日+30天
                        lockTime = getlockTime(invTime);
                    }
                    //截至日期

                    //接口版本号
                    param.put("version", CertCallConstant.CERT_CALL_VERSION);
                    //债权信息编号
                    param.put("finClaimId", borrowTender.getNid());
                    //平台编号
                    param.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
                    //原散标编号
                    param.put("sourceProductCode", borrowNid);
                    //原产品信息编号
                    param.put("sourceFinancingCode", sourceFinancingCode);
                    //投资人用户标示 Hash
                    param.put("userIdcardHash", userIdcardHash);
                    //投资 金额 (元)
                    param.put("invAmount", borrowTender.getAccount());
                    //投资预期年化收益率
                    param.put("invRate", rate);
                    //  投资计息时间
                    param.put("invTime", invTime);
                    //投资红包
                    param.put("redpackage", sumRedPackage.toString());
                    // 封闭截止时间
                    param.put("lockTime", lockTime);
                    //是否是历史数据
                    if(isOld){
                        BorrowRecover borrowRecover = certBorrowStatusService.selectBorrowRecover(borrowNid);
                        // groupByDate  旧数据上报排序 按月用
                        String groupByDateStr = dateFormatTransformation(borrowRecover.getAddtime());
                        String groupByDate= groupByDateStr.split("-")[0]+"-"+groupByDateStr.split("-")[1];
                        param.put("groupByDate",groupByDate);
                    }
                    json.add(param);
                }
            }
        } else {
          //  logger.info(logHeader + "标的编号为:" + borrowNid + ",标的信息为空或者标的为放款成功!");
        }
        return json;
    }

    /**
     * 日期转换,数据存的int10的时间戳
     *
     * @param repayTime
     * @return
     */
    private String dateFormatTransformation(String repayTime) {
        if (StringUtils.isNotBlank(repayTime)) {
            long intT = Long.parseLong(repayTime) * 1000;
            Date dateRapay = new Date(intT);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateStr = sdf.format(dateRapay);
            return dateStr;
        }
        return null;
    }

    /**
     * 获取放款信息
     *
     * @param tenderNid
     * @param borrowNid
     * @return
     */
    private BorrowRecover getRecoverDateByTenderNid(String tenderNid, String borrowNid,String accedeOrderId) {
        BorrowRecoverExample example = new BorrowRecoverExample();
        BorrowRecoverExample.Criteria cra = example.createCriteria();
        cra.andBorrowNidEqualTo(borrowNid);
        cra.andNidEqualTo(tenderNid);
        if(StringUtils.isNotBlank(accedeOrderId)){
            cra.andAccedeOrderIdEqualTo(accedeOrderId);
        }
        List<BorrowRecover> borrowRecoverList = borrowRecoverMapper.selectByExample(example);
        if (null != borrowRecoverList && borrowRecoverList.size() > 0) {
            return borrowRecoverList.get(0);
        }
        return null;
    }


    /**
     * 计息日+30天
     *
     * @param timeStr
     * @return
     */
    private String getlockTime(String timeStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String enddate = "";
        try {
            Date currdate = format.parse(timeStr);
            Calendar ca = Calendar.getInstance();
            ca.setTime(currdate);
            ca.add(Calendar.DATE, 30);// num为增加的天数，可以改变的
            currdate = ca.getTime();
            enddate = format.format(currdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return enddate;
    }


}
