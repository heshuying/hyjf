package com.hyjf.api.anrong.util;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.anrong.server.AnRongDefine;
import com.hyjf.common.log.LogUtil;
import com.hyjf.mybatis.mapper.auto.MspAbnormalCreditDetailMapper;
import com.hyjf.mybatis.mapper.auto.MspAbnormalCreditMapper;
import com.hyjf.mybatis.mapper.auto.MspAnliInfosMapper;
import com.hyjf.mybatis.mapper.auto.MspApplyDetailsMapper;
import com.hyjf.mybatis.mapper.auto.MspBlackDataMapper;
import com.hyjf.mybatis.mapper.auto.MspDegreeResultMapper;
import com.hyjf.mybatis.mapper.auto.MspFqzMapper;
import com.hyjf.mybatis.mapper.auto.MspNormalCreditDetailMapper;
import com.hyjf.mybatis.mapper.auto.MspQueryDetailMapper;
import com.hyjf.mybatis.mapper.auto.MspShixinInfosMapper;
import com.hyjf.mybatis.mapper.auto.MspTitleMapper;
import com.hyjf.mybatis.mapper.auto.MspZhixingInfosMapper;
import com.hyjf.mybatis.model.auto.MspAbnormalCredit;
import com.hyjf.mybatis.model.auto.MspAbnormalCreditDetail;
import com.hyjf.mybatis.model.auto.MspAnliInfos;
import com.hyjf.mybatis.model.auto.MspApplyDetails;
import com.hyjf.mybatis.model.auto.MspBlackData;
import com.hyjf.mybatis.model.auto.MspDegreeResult;
import com.hyjf.mybatis.model.auto.MspFqz;
import com.hyjf.mybatis.model.auto.MspNormalCreditDetail;
import com.hyjf.mybatis.model.auto.MspQueryDetail;
import com.hyjf.mybatis.model.auto.MspShixinInfos;
import com.hyjf.mybatis.model.auto.MspTitle;
import com.hyjf.mybatis.model.auto.MspZhixingInfos;

/**
 * 
 * 安融返回值处理
 * @author sss
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年10月11日
 * @see 下午2:28:56
 */
@Component
public class DoResultService {

    @Autowired
    MspAbnormalCreditDetailMapper abnormalCreditDetailMapper;
    @Autowired
    MspAbnormalCreditMapper abnormalCreditMapper;
    @Autowired
    MspApplyDetailsMapper applyDetailsMapper;
    @Autowired
    MspBlackDataMapper blackDataMapper;
    @Autowired
    MspNormalCreditDetailMapper creditDetailMapper;
    @Autowired
    MspQueryDetailMapper queryDetailMapper;
    @Autowired
    MspTitleMapper mspTitleMapper;
    @Autowired
    MspFqzMapper fqzMapper;
    @Autowired
    MspAnliInfosMapper anliInfosMapper;
    @Autowired
    MspShixinInfosMapper infosMapper;
    @Autowired
    MspZhixingInfosMapper zhixingInfosMapper;
    @Autowired
    MspDegreeResultMapper degreeResultMapper;
    

    /**
     * 
     * 安融返回结果处理
     * @author sss
     * @param resultJson
     * @param appid 请求ID
     * @return
     */
    public void insertResult(JSONObject resultJson,String appid) {
        LogUtil.infoLog(DoResultService.class.getName(), "开始插入安融返回结果insertResult");
        // msp 报告
        JSONObject msp = resultJson.getJSONObject(AnRongDefine.RESULT_KEY_MSP);
        LogUtil.infoLog(DoResultService.class.getName(), "开始处理msp数据");
        if ((!msp.containsKey(AnRongDefine.RESULT_KEY_ERRORCODE))||msp.get(AnRongDefine.RESULT_KEY_ERRORCODE)==null) {
            LogUtil.infoLog(DoResultService.class.getName(), "msp有数据");
            // 有数据
            // 插入 abnormalCreditDetails
            if(validateIsNull(msp,AnRongDefine.RESULT_KEY_ABNORMALCREDITDETAILS)){
                JSONArray abnormalCreditDetails = msp.getJSONArray(AnRongDefine.RESULT_KEY_ABNORMALCREDITDETAILS);
                insertCreditDetails(abnormalCreditDetails,appid);
            }
            
            // 插入 applyDetails
            if(validateIsNull(msp,AnRongDefine.RESULT_KEY_APPLYDETAILS)){
                JSONArray applyDetails = msp.getJSONArray(AnRongDefine.RESULT_KEY_APPLYDETAILS);
                insertApplyDetails(applyDetails,appid);
            }
            
            // 插入 blackDatas
            if(validateIsNull(msp,AnRongDefine.RESULT_KEY_BLACKDATAS)){
                JSONArray blackDatas = msp.getJSONArray(AnRongDefine.RESULT_KEY_BLACKDATAS);
                inserTblackDatas(blackDatas,appid);
            }
            
            // 插入 normalCreditDetails
            if(validateIsNull(msp,AnRongDefine.RESULT_KEY_NORMALCREDITDETAILS)){
                JSONArray normalCreditDetails = msp.getJSONArray(AnRongDefine.RESULT_KEY_NORMALCREDITDETAILS);
                insertNormalCreditDetails(normalCreditDetails,appid);
            }
            
            // 插入queryDetails
            if(validateIsNull(msp,AnRongDefine.RESULT_KEY_QUERYDETAILS)){
                JSONArray queryDetails = msp.getJSONArray(AnRongDefine.RESULT_KEY_QUERYDETAILS);
                insertQueryDetails(queryDetails,appid);
            }
            
            // 插入titles
            if(validateIsNull(msp,AnRongDefine.RESULT_KEY_TITLE)){
                JSONObject titles = msp.getJSONObject(AnRongDefine.RESULT_KEY_TITLE);
                inserTtitles(titles,appid);
            }
        }
        LogUtil.infoLog(DoResultService.class.getName(), "msp数据处理完成");
        
        // fqz 报告
        LogUtil.infoLog(DoResultService.class.getName(), "开始处理fqz数据");
        JSONObject fqz = resultJson.getJSONObject(AnRongDefine.RESULT_KEY_FQZ);
        if ((!fqz.containsKey(AnRongDefine.RESULT_KEY_ERRORCODE))||fqz.get(AnRongDefine.RESULT_KEY_ERRORCODE)==null) {
            LogUtil.startLog(DoResultService.class.getName(), "fqz有数据");
            insertFqz(fqz,appid);
        }
        LogUtil.infoLog(DoResultService.class.getName(), "fqz数据处理完成");
    }

    // 插入fqz
    private void insertFqz(JSONObject fqz,String appid) {
        JSONObject fqzBeanJson = fqz.getJSONObject(AnRongDefine.RESULT_KEY_FQZBEAN);
        MspFqz fqzBean = JSONObject.parseObject(fqzBeanJson.toString(), MspFqz.class);
        fqzBean.setApplyId(appid);
        fqzMapper.insert(fqzBean);
        
        if(validateIsNull(fqzBeanJson,AnRongDefine.RESULT_KEY_DEGREERESULT)){
            JSONObject degreeResult = fqzBeanJson.getJSONObject(AnRongDefine.RESULT_KEY_DEGREERESULT);
            MspDegreeResult mspDegreeResult = JSONObject.parseObject(degreeResult.toString(), MspDegreeResult.class);
            mspDegreeResult.setApplyId(appid);
            degreeResultMapper.insert(mspDegreeResult);
        }
        
        // 插入 validSifa
        JSONObject validSifa = fqzBeanJson.getJSONObject(AnRongDefine.RESULT_KEY_VALIDSIFA);
        if(validateIsNull(validSifa,AnRongDefine.RESULT_KEY_ANLIINFOS)){
            JSONArray anliInfos = validSifa.getJSONArray(AnRongDefine.RESULT_KEY_ANLIINFOS);
            for (Object item : anliInfos) {
                JSONObject jso = (JSONObject) item;
                // 这里ID跟数据库里面的ID数据类型冲突  所以需要单独处理
                String id = jso.getString(AnRongDefine.STRING_ID);
                jso.remove(AnRongDefine.STRING_ID);
                MspAnliInfos anliInfo = JSONObject.parseObject(jso.toString(), MspAnliInfos.class);
                anliInfo.setAnliId(id);
                anliInfo.setApplyId(appid);
                anliInfosMapper.insert(anliInfo);
            }
        }
        if(validateIsNull(validSifa,AnRongDefine.RESULT_KEY_SHIXININFOS)){
            JSONArray shixinInfos = validSifa.getJSONArray(AnRongDefine.RESULT_KEY_SHIXININFOS);
            for (Object item : shixinInfos) {
                JSONObject jso = (JSONObject) item;
                MspShixinInfos shixinInfo = JSONObject.parseObject(jso.toString(), MspShixinInfos.class);
                shixinInfo.setApplyId(appid);
                infosMapper.insert(shixinInfo);
            }
        }
        if(validateIsNull(validSifa,AnRongDefine.RESULT_KEY_ZHIXINGINFOS)){
            JSONArray zhixingInfos = validSifa.getJSONArray(AnRongDefine.RESULT_KEY_ZHIXINGINFOS);
            for (Object item : zhixingInfos) {
                JSONObject jso = (JSONObject) item;
                MspZhixingInfos zhixingInfo = JSONObject.parseObject(jso.toString(), MspZhixingInfos.class);
                zhixingInfo.setApplyId(appid);
                zhixingInfosMapper.insert(zhixingInfo);
            }
        }
    }

    // 插入titles
    private void inserTtitles(JSONObject titles, String appid) {
        MspTitle ad = JSONObject.parseObject(titles.toString(), MspTitle.class);
        ad.setApplyId(appid);
        mspTitleMapper.insert(ad);
    }

    // 插入queryDetails
    private void insertQueryDetails(JSONArray queryDetails, String appid) {
        for (Object item : queryDetails) {
            JSONObject jso = (JSONObject) item;
            MspQueryDetail ad = JSONObject.parseObject(jso.toString(), MspQueryDetail.class);
            ad.setApplyId(appid);
            queryDetailMapper.insert(ad);
        }
    }

    // 插入 normalCreditDetails
    private void insertNormalCreditDetails(JSONArray normalCreditDetails, String appid) {
        for (Object item : normalCreditDetails) {
            JSONObject jso = (JSONObject) item;
            MspNormalCreditDetail ad = JSONObject.parseObject(jso.toString(), MspNormalCreditDetail.class);
            ad.setApplyId(appid);
            creditDetailMapper.insert(ad);
        }
    }

    // 插入 blackDatas
    private void inserTblackDatas(JSONArray blackDatas, String appid) {
        for (Object item : blackDatas) {
            JSONObject jso = (JSONObject) item;
            MspBlackData ad = JSONObject.parseObject(jso.toString(), MspBlackData.class);
            ad.setApplyId(appid);
            blackDataMapper.insert(ad);
        }
    }

    // msp 插入 applyDetails
    private void insertApplyDetails(JSONArray applyDetails, String appid) {
        for (Object item : applyDetails) {
            JSONObject jso = (JSONObject) item;
            MspApplyDetails ad = JSONObject.parseObject(jso.toString(), MspApplyDetails.class);
            ad.setApplyId(appid);
            applyDetailsMapper.insert(ad);
        }
    }

    // msp  abnormalCreditDetails
    private void insertCreditDetails(JSONArray abnormalCreditDetails,String appId) {
        // 需要插入两个表
        for (Object item : abnormalCreditDetails) {
            JSONObject jso = (JSONObject) item;
            MspAbnormalCredit abnormalCredit = JSONObject.parseObject(jso.toString(), MspAbnormalCredit.class);
            abnormalCredit.setApplyId(appId);
            abnormalCredit.setId(UUID.randomUUID().toString());
            
            if(jso.containsKey(AnRongDefine.RESULT_KEY_OVERDUES)){
                for (Object aDetail : jso.getJSONArray(AnRongDefine.RESULT_KEY_OVERDUES)) {
                    MspAbnormalCreditDetail detail = JSONObject.parseObject(aDetail.toString(), MspAbnormalCreditDetail.class);
                    detail.setAbcdId(abnormalCredit.getId());
                    abnormalCreditDetailMapper.insert(detail);
                }
            }
            abnormalCreditMapper.insert(abnormalCredit);
        }
    }
    
    // 验证是否为空
    private boolean validateIsNull(JSONObject validSifa,String key) {
        if(validSifa.containsKey(key) && validSifa.get(key)!=null && !"null".equals(validSifa.get(key))){
            return true;
        }
        return false;
    }

}
