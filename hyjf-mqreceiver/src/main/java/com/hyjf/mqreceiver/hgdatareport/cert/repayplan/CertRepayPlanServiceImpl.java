package com.hyjf.mqreceiver.hgdatareport.cert.repayplan;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mqreceiver.hgdatareport.cert.BaseHgCertReportServiceImpl;
import com.hyjf.mqreceiver.hgdatareport.cert.olddata.undertake.CertOldCreditInfoServiceImpl;
import com.hyjf.mqreceiver.hgdatareport.cert.status.CertBorrowStatusService;
import com.hyjf.mqreceiver.hgdatareport.cert.userinfo.CertUserInfoService;
import com.hyjf.mqreceiver.hgdatareport.common.CertCallConstant;
import com.hyjf.mybatis.mapper.auto.BorrowRepayPlanMapper;
import com.hyjf.mybatis.model.auto.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * @author nxl
 */

@Service
public class CertRepayPlanServiceImpl extends BaseHgCertReportServiceImpl implements CertRepayPlanService {

    Logger logger = LoggerFactory.getLogger(CertOldCreditInfoServiceImpl.class);
    private String thisMessName = "还款计划信息推送";
    private String logHeader = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_CERT + " " + thisMessName + "】";

    @Autowired
    BorrowRepayPlanMapper borrowRepayPlanMapper;
    @Autowired
    private CertUserInfoService certUserInfoService;
    @Autowired
    private CertBorrowStatusService certBorrowStatusService;

    /**
     * 获取标的的还款信息
     *
     * @param borrowNid
     * @return
     */
    @Override
    public JSONArray getBorrowReyapPlan(String borrowNid, JSONArray json,boolean isOld) {
        //JSONArray json = new JSONArray();
        //根据标的编号查找标的信息
        try {
            //获取还款信息
            BorrowRepay repay = this.selectBorrowRepay(borrowNid);
            //标的信息
            Borrow borrow = this.getBorrowByBorrowNid(borrowNid);
            //借款人信息
            CertUser certUser = certUserInfoService.getCertUserByUserId(borrow.getUserId());
            String borrowStyle = borrow.getBorrowStyle();
            // 是否分期(true:分期, false:不分期)
            boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
                    || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
            String userIdcardHash = "";
            //如果借款人未上报,则从用户信息表中获取用户idCard
            if (null != certUser) {
                userIdcardHash = certUser.getUserIdCardHash();
            } else {
                UsersInfo usersInfo = this.getUsersInfoByUserId(borrow.getUserId());
                if (null != usersInfo) {
                    userIdcardHash = tool.idCardHash(usersInfo.getIdcard());
                }
            }
            //标的状态为还款中,代表放款成功
            if (borrow.getStatus() == 4) {
                if (isMonth) {
                    //分期的标的
                    //根据标的编号查找还款信息
                    BorrowRepayPlanExample example = new BorrowRepayPlanExample();
                    BorrowRepayPlanExample.Criteria cra = example.createCriteria();
                    cra.andBorrowNidEqualTo(borrowNid);
                    List<BorrowRepayPlan> listRepay = borrowRepayPlanMapper.selectByExample(example);
                    if (null != listRepay && listRepay.size() > 0) {
                        //循环取值
                        for (int i = 0; i < listRepay.size(); i++) {
                            BorrowRepayPlan borrowRepayPlan = listRepay.get(i);
                            Map<String, Object> param = new LinkedHashMap<String, Object>();
                            //接口版本号
                            param.put("version", CertCallConstant.CERT_CALL_VERSION);
                            //平台编号
                            param.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
                            //原散标编号
                            param.put("sourceProductCode", borrowNid);
                            //借款用户标示hash
                            param.put("userIdcardHash", userIdcardHash);
                            //总期数 分期还款的总期数
                            param.put("totalIssue", borrow.getBorrowPeriod().toString());
                            //当前期数  当期还款期数，每一期一条数据。
                            param.put("issue", borrowRepayPlan.getRepayPeriod().toString());
                            // 还款计划编号
                            // 还款计划编号是指每一个项目的每一次还款计划的唯一编号，平台内所有还款计划中编号唯一。如果没有则填写“散标编号+当前期数”
                            //还款计划编号：报送标的号+“-”+当前期数
                            param.put("replanId", borrowNid + "-" + borrowRepayPlan.getRepayPeriod());
                            //当期应还本金（元）
                            param.put("curFund", borrowRepayPlan.getRepayCapital());
                            //当期应还利息（元）
                            param.put("curInterest", borrowRepayPlan.getRepayInterest());
                            //当期应还款时间点
                            //当期应还时间点：报送当期应还日期23:59:59
                            param.put("repayTime", dateFormatTransformation(borrowRepayPlan.getRepayTime()) + " 23:59:59");
                            //是否是历史数据
                            if(isOld){
                                //是否是历史数据
                                // groupByDate  旧数据上报排序 按月用
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
                    Map<String, Object> param = new LinkedHashMap<String, Object>();
                    //不分期的标的
                    //接口版本号
                    param.put("version", CertCallConstant.CERT_CALL_VERSION);
                    //平台编号
                    param.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
                    //原散标编号
                    param.put("sourceProductCode", borrowNid);
                    //借款用户标示hash
                    param.put("userIdcardHash", userIdcardHash);
                    //总期数 分期还款的总期数
                    param.put("totalIssue", "1");
                    //当前期数  当期还款期数，每一期一条数据。
                    param.put("issue", "1");
                    // 还款计划编号
                    // 还款计划编号是指每一个项目的每一次还款计划的唯一编号，平台内所有还款计划中编号唯一。如果没有则填写“散标编号+当前期数”
                    //还款计划编号：报送标的号+“-”+当前期数
                    param.put("replanId", borrowNid + "-" + "1");
                    //当期应还本金（元）
                    param.put("curFund", borrow.getBorrowAccountYes());
                    //当期应还利息（元）
                    param.put("curInterest", borrow.getRepayAccountInterest());
                    //当期应还款时间点
                    //当期应还时间点：报送当期应还日期23:59:59
                    param.put("repayTime", dateFormatTransformation(repay.getRepayTime()) + " 23:59:59");
                    //是否是历史数据
                    if(isOld){
                        //是否是历史数据
                        // groupByDate  旧数据上报排序 按月用
                        BorrowRecover borrowRecover = certBorrowStatusService.selectBorrowRecover(borrowNid);
                        // groupByDate  旧数据上报排序 按月用
                        String groupByDateStr = dateFormatTransformation(borrowRecover.getAddtime());
                        String groupByDate= groupByDateStr.split("-")[0]+"-"+groupByDateStr.split("-")[1];
                        param.put("groupByDate",groupByDate);
                    }
                    json.add(param);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dateStr = sdf.format(dateRapay);
            return dateStr;
        }
        return null;
    }
}
