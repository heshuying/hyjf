package com.hyjf.mqreceiver.hgdatareport.cert.exception;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mongo.hgdatareport.entity.CertReportEntity;
import com.hyjf.mqreceiver.hgdatareport.cert.BaseHgCertReportServiceImpl;
import com.hyjf.mqreceiver.hgdatareport.cert.userinfo.CertUserInfoMessageHadnle;
import com.hyjf.mqreceiver.hgdatareport.cert.userinfo.CertUserInfoService;
import com.hyjf.mqreceiver.hgdatareport.common.CertCallConstant;
import com.hyjf.mqreceiver.hgdatareport.common.CertCallUtil;
import com.hyjf.mqreceiver.hgdatareport.common.CertSendUtils;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.CertSendUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author sss
 */

@Service
public class CertSendErrorServiceImpl extends BaseHgCertReportServiceImpl implements CertSendErrorService {
    Logger logger = LoggerFactory.getLogger(CertUserInfoMessageHadnle.class);

    private String thisMessName = "上报失败异常处理";
    private String logHeader = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_CERT + " " + thisMessName + "】";

    @Autowired
    private CertUserInfoService certUserInfoService;

    /**
     * 重新上报  并修改数据库
     *
     * @param list
     */
    @Override
    public void insertData(JSONArray list) {
        for (int i = 0; i < list.size(); i++) {
            JSONObject item = list.getJSONObject(i);
            String logOrdId = item.getString("logOrdId");
            Integer count = item.getInteger("count");
            Query q1 = Query.query(Criteria.where("logOrdId").is(logOrdId));
            CertReportEntity entity = certReportDao.findOne(q1);
            if(entity == null ){
                logger.info(logHeader+"mongo查询无记录！订单号:{}",logOrdId);
                continue;
            }
            try {
                setCommonParam(entity);
            }catch (Exception e){
               logger.error(logHeader+"设置参数错误",e);
            }
            this.certReportDao.insert(entity);
            // 开始上报数据
            Map<String, String> params = getBankParam(entity);
            String rtnMsg = CertSendUtils.postRequest(entity.getUrl(), params);
            updateResult(rtnMsg,entity,count,logOrdId);
            list.set(i,entity);
        }
    }

    /**
     * 操作结果
     * @param rtnMsg
     */
    private void updateResult(String rtnMsg,CertReportEntity bean,Integer count,String oldLogOrdId) {
        // 上报结果  0初始，1成功，9失败 99 无响应
        // 返回结果  例  {"resultMsg": {"code": "0000","message": "[调试]数据已成功上报，正在等待处理，请使用对账接口查看数据状态"}
        bean =  super.setResult(bean,rtnMsg);

        // 如果成功的话 删除数据
        if(CertCallConstant.CERT_RETURN_STATUS_SUCCESS.equals(bean.getReportStatus())){
            CertErrLogExample example = new CertErrLogExample();
            CertErrLogExample.Criteria cra = example.createCriteria();
            cra.andLogOrdIdEqualTo(oldLogOrdId);
            certErrLogMapper.deleteByExample(example);

            // 如果是用户数据的话  需要插入ht_cert_user表
            if(CertCallConstant.CERT_INF_TYPE_USER_INFO.equals(bean.getInfType())){
                /*JSONArray users = bean.getDataList();
                List<CertUser> insertList = new ArrayList<>();
                for (int i=0;i<users.size();i++){
                    JSONObject item = users.getJSONObject(i);
                    String borrowNid = bean.getLogParm();
                    CertUser certUser = null;
                    if(StringUtils.isBlank(borrowNid)){
                        certUser = getCertUserByUserId(Integer.parseInt(bean.getUserId()));
                    }
                    if(certUser ==null ){
                        // 借款人了
                        if(borrowNid==null||"".equals(borrowNid)){
                           logger.info(logHeader+"mongo未查询到标的号!");
                           continue;
                        }
                        certUser = certUserInfoService.getCertUserByUserIdBorrowNid(Integer.parseInt(bean.getUserId()),borrowNid);
                    }

                    // 新增
                    if(certUser == null ){
                        CertSendUser user = certUserInfoService.getCertSendUserByUserId(Integer.parseInt(bean.getUserId()));
                        certUser = new CertUser();
                        certUser.setUserName(user.getUsername());
                        certUser.setLogOrdId(bean.getLogOrdId());
                        certUser.setBorrowNid(bean.getLogParm());
                        certUser.setUserId(Integer.parseInt(bean.getUserId()));
                        certUser.setUserIdCardHash((String)item.get("userIdcardHash"));
                        certUser.setHashValue(user.getUserIdcardValue());
                        insertList.add(certUser);
                    }
                }
                if(insertList.size()>0){
                    certUserInfoService.insertCertUserByList(insertList);
                }*/
            }

        }else{
            updateErrorLogCount(oldLogOrdId,count,bean.getRetMess());
        }

    }

    /**
     * 修改错误次数加1
     * @param logOrdId
     */
    private void updateErrorLogCount(String logOrdId,Integer count,String rtnMsg) {
        JSONObject resp = CertCallUtil.parseResult(rtnMsg);
        String retCode = resp.getString("code");
        String retMess = resp.getString("message");
        CertErrLog certErrLog = new CertErrLog();
        certErrLog.setSendCount(++count);
        certErrLog.setResultMsg(retMess);
        certErrLog.setResultCode(retCode);

        CertErrLogExample example = new CertErrLogExample();
        CertErrLogExample.Criteria cra = example.createCriteria();
        cra.andLogOrdIdEqualTo(logOrdId);
        certErrLogMapper.updateByExampleSelective(certErrLog,example);
    }
}
