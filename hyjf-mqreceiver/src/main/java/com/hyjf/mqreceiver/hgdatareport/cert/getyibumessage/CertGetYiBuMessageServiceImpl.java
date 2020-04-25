package com.hyjf.mqreceiver.hgdatareport.cert.getyibumessage;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.mongo.hgdatareport.entity.CertReportEntity;
import com.hyjf.mqreceiver.hgdatareport.cert.BaseHgCertReportServiceImpl;
import com.hyjf.mqreceiver.hgdatareport.common.CertCallConstant;
import com.hyjf.mqreceiver.hgdatareport.common.CertCallUtil;
import com.hyjf.mqreceiver.hgdatareport.common.CertSendUtils;
import com.hyjf.mybatis.model.auto.CertLog;
import com.hyjf.mybatis.model.auto.CertLogExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Description 合规数据上报 CERT 查询批次数据入库消息
 * @Author nxl
 * @Date 2018/12/25 17:10
 */
@Service
public class CertGetYiBuMessageServiceImpl extends BaseHgCertReportServiceImpl implements CertGetYiBuMessageService {
    Logger logger = LoggerFactory.getLogger(BaseHgCertReportServiceImpl.class);


    @Override
    public List<CertLog> getCertLog(){
        CertLogExample example = new CertLogExample();
        CertLogExample.Criteria creteria = example.createCriteria();
        // 查询上报结果成功
        creteria.andSendStatusEqualTo(1);
        // 查询结果为初始
        List<Integer> resultIn = new ArrayList<>();
        // 初始  等待处理   无响应
        resultIn.add(0);
        resultIn.add(2);
        resultIn.add(99);
        creteria.andQueryResultIn(resultIn);
        //一次查找500条数据
        example.setLimitStart(0);
        example.setLimitEnd(300);

        example.setOrderByClause(" id asc");
        List<CertLog> certLogList = certLogMapper.selectByExample(example);
        if (null != certLogList && certLogList.size() > 0) {
            return  certLogList;
        }
        return null;
    }
    @Override
    public CertReportEntity updateYiBuMessage(String batchNum,String certLogId,String infType){
        CertReportEntity entity = new CertReportEntity();
        entity = getCertReportEntity(batchNum,entity,infType);
        //拼接参数
        StringBuilder strUrl=new StringBuilder(entity.getUrl());
        strUrl.append("?apiKey=");
        strUrl.append(entity.getApiKey());
        strUrl.append("&nonce=");
        strUrl.append(entity.getNonce());
        strUrl.append("&sourceCode=");
        strUrl.append(entity.getSourceCode());
        strUrl.append("&timestamp=");
        strUrl.append(entity.getTimestamp()+"");
        strUrl.append("&version=");
        strUrl.append(entity.getVersion());
        strUrl.append("&infType=");
        strUrl.append(entity.getInfType());
        strUrl.append("&dataType=");
        strUrl.append(entity.getDataType());
        strUrl.append("&batchNum=");
        strUrl.append(entity.getBatchNum());
        //get 请求方式
        String rtnMsg = CertSendUtils.getReq(strUrl.toString());
        logger.info("[查询批次数据入库消息] 的返回结果为:"+rtnMsg);
        CertLog certLog =getCertLogById(certLogId);
        entity = setResult(entity,rtnMsg,certLog);
        return entity;
    }

    /**
     * 组装参数
     * @param batchNum
     * @param entity
     * @param infType
     * @return
     */
    private CertReportEntity getCertReportEntity(String batchNum,CertReportEntity entity,String infType){
        // nonce 随机数
        String nonce = Integer.toHexString(new Random().nextInt());
        entity.setNonce(nonce);
        // sourceCode  平台编号
        //平台编号
        entity.setSourceCode(CertCallConstant.CERT_SOURCE_CODE);
        //版本号
        String version = CertCallConstant.CERT_CALL_VERSION;
        entity.setVersion(version);
        // timestamp 时间戳(1490601378778)
        long timestamp = System.currentTimeMillis();
        entity.setTimestamp(String.valueOf(timestamp));
        // apiKey
        String apiKey = CertCallUtil.getApiKey(CertCallConstant.CERT_API_KEY, CertCallConstant.CERT_SOURCE_CODE, version, timestamp, nonce);
        entity.setApiKey(apiKey);
        // dataType 数据类型（0测试，1正式）
        // 判断是否测试环境
        if (CertCallConstant.CERT_IS_TEST == null || "true".equals(CertCallConstant.CERT_IS_TEST)) {
            // 测试数据
            entity.setDataType("0");
        }else{
            // 正式数据
            entity.setDataType("1");
        }
        // batchNum 批次号
        entity.setBatchNum(batchNum);
        // 请求地址
        // "https://api.ifcert.org.cn/balanceService/v15/yiBuMessage"
        String url =CertCallConstant.CERT_WEB_YIBU;
        logger.info("[查询批次数据入库消息] 请求地址是:"+url);
        entity.setUrl(url);
        entity.setInfType(infType);
        return entity;
    }

    /**
     * 组装参数
     * @param bean
     * @return
     */
    protected Map<String, String> getQueryBankParam(CertReportEntity bean){
        // 组装调用接口需要的参数对象
        Map<String, String> params = new HashMap<>(2);
        params.put("apiKey", bean.getApiKey());
        params.put("version", bean.getVersion());
        params.put("batchNum", bean.getBatchNum());
        params.put("sourceCode", bean.getSourceCode());
        params.put("dataType", bean.getDataType());
        params.put("timestamp", bean.getTimestamp());
        params.put("nonce", bean.getNonce());
        params.put("infType", bean.getInfType());
        return params;
    }

    private CertLog getCertLogById(String certLogId){
        //
        CertLog certLog = certLogMapper.selectByPrimaryKey(Integer.parseInt(certLogId));
        return certLog;
    }

    protected CertReportEntity setResult(CertReportEntity bean, String rtnMsg,CertLog certLog) {

        // 上报结果  0初始，1成功，9失败 99 无响应
        // 返回结果  例  {"resultMsg": {"code": "0000","message": "[调试]数据已成功上报，正在等待处理，请使用对账接口查看数据状态"}
        JSONObject resp = CertCallUtil.parseResultQuery(rtnMsg);
        String errorMsg = resp.getString("message");
        String code = resp.getString("code");
        if(null == rtnMsg||rtnMsg.equals("")){
            //请求失败  无响应
            certLog.setQueryResult(CertCallUtil.convertQueryResult(errorMsg));
            certLog.setQueryMsg("请求失败,无响应");
        }else{
            if(CertCallConstant.CERT_RESPONSE_SUCCESS.equals(code)){
                //代表入库成功
                certLog.setQueryResult(CertCallConstant.CERT_QUERY_RETURN_STATUS_SUCCESS);
                certLog.setQueryMsg(errorMsg);

            }else{
                // 请求失败
                certLog.setQueryResult(CertCallUtil.convertQueryResult(errorMsg));
                certLog.setQueryMsg(errorMsg);
            }

        }
        // 修改数据库
        certLogMapper.updateByPrimaryKeySelective(certLog);
        return bean;
    }

}

