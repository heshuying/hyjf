/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2017
 * Company: HYJF Corporation
 *
 * @author: lb
 * @version: 1.0
 * Created at: 2017年9月15日 上午9:43:49
 * Modification History:
 * Modified by :
 */

package com.hyjf.batch.hgdatareport;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.batch.hgdatareport.bifa.BifaCommonConstants;
import com.hyjf.batch.hgdatareport.bifa.BifaReportResultBean;
import com.hyjf.common.calculate.CalculatesUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.SHA256Util;
import com.hyjf.common.util.WebServiceUtil;
import com.hyjf.mongo.hgdatareport.base.BaseHgDataReportEntity;
import com.hyjf.mongo.hgdatareport.dao.UserInfoSHA256Dao;
import com.hyjf.mongo.hgdatareport.entity.UserInfoSHA256Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

/**
 * @author liubin
 */
@Service
public class BaseHgDateReportServiceImpl extends BaseServiceImpl implements BaseHgDateReportService {
    public Logger _log = LoggerFactory.getLogger(BaseHgDateReportServiceImpl.class);

    /**
     * 北互金协议地址
     **/
    public static final String BIFA_CRT_PATH = PropUtils.getSystem("hyjf.bifa.crt.path");
    /***
     * 北互金wsdl地址
     */
    public static final String BIFA_END_POINT = PropUtils.getSystem("hyjf.bifa.end.point");
    /**
     * 北互金namespace地址
     */
    public static final String BIFA_NAME_SPACE = PropUtils.getSystem("hyjf.bifa.name.space");
    /**
     * 北互金loginname
     */
    public static final String BIFA_LOGIN_NAME = PropUtils.getSystem("hyjf.bifa.login.name");
    /**
     * 北互金password
     */
    public static final String BIFA_PASS_WORD= PropUtils.getSystem("hyjf.bifa.pass.word");

    /**
     * 平台编号
     */
    public static final String SOURCE_CODE= PropUtils.getSystem("hyjf.source.code");


    @Autowired
    public UserInfoSHA256Dao userInfoSHA256Dao;
    /**
     * 北互金索引文件存放地址
     **/
    public static final String HYJF_BIFA_FILES_INDEXFILE = PropUtils.getSystem("hyjf.bifa.files.indexfile");

    /**
     * 北互金索引文件上报地址
     */
    public static final String HYJF_BIFA_INDEXDATA_REPORT_URL =PropUtils.getSystem("hyjf.bifa.indexdata.report.url");

    /**
     * 散标链接
     */
    public static final String SOURCE_PRODUCT_URL_BORROW = PropUtils.getSystem("hyjf.source.product.url.borrow");

    /**
     * 智投链接
     */
    public static final String SOURCE_PRODUCT_URL_HJHPLAN = PropUtils.getSystem("hyjf.source.product.url.hjhplan");


    /**
     * 调用webservice接口并返回数据
     *
     * @param methodName
     * @param encmsg
     * @return
     */
    @Override
    public String webService(String methodName, String encmsg) {
        // 北互金目前三个方法的参数统一
        String[] params = {"arg0","arg1","arg2"};
        return WebServiceUtil.webService(BIFA_CRT_PATH,BIFA_END_POINT,BIFA_NAME_SPACE,methodName,params,new Object[]{BIFA_LOGIN_NAME,BIFA_PASS_WORD,encmsg});
    }

    /**
     * 上报数据
     * @param data
     * @return
     */
    @Override
    public <T extends BaseHgDataReportEntity> T reportData(String methodName,T data) {
        try {
            String encmsg = JSONObject.toJSONString(data);
            String result = this.webService(methodName,encmsg);
            BifaReportResultBean reportResult= JSONObject.parseObject(result,BifaReportResultBean.class);
            data.setErrCode(reportResult.getReCode());
            data.setErrDesc(reportResult.getMessage());

            if (BifaCommonConstants.SUCCESSCODE.equals(reportResult.getReCode())){
                data.setReportStatus("1");
            }else if (BifaCommonConstants.REPORTEDCODE.equals(reportResult.getReCode())
                    && reportResult.getMessage().indexOf(BifaCommonConstants.EXIST) >= 0 ){
                //已经上报过的状态置为7
                data.setReportStatus("7");
            }else {
                data.setReportStatus("9");
            }

        } catch (Exception e) {
            e.printStackTrace();
            data.setReportStatus("9");
            data.setErrCode(BifaCommonConstants.ERRCODE);
            data.setErrDesc(BifaCommonConstants.ERRDESC);
        }finally {
            _log.info("北互金上报结果"+JSONObject.toJSONString(data));
            return data;
        }
    }


    /**
     * 获取用户索引信息
     *
     * @param userId
     * @param trueName
     * @param idCard
     * @return
     */
    @Override
    public UserInfoSHA256Entity selectUserIdToSHA256(Integer userId,String trueName,String idCard) {
        // 查询mongo是否已存该加密信息
        Query query = new Query();
        Criteria criteria = null;
        if (userId != null) {
            criteria = Criteria.where("userId").is(userId);
        } else {
            criteria = Criteria.where("trueName").is(trueName).and("idCard").is(idCard);
        }
        query.addCriteria(criteria);
        UserInfoSHA256Entity userInfoSHA256Entity = this.userInfoSHA256Dao.findOne(query);
        // 取到返回、未取到初始化后入库
        if (null == userInfoSHA256Entity) {
            // 未取到数据重新加密传入
            userInfoSHA256Entity = new UserInfoSHA256Entity();
            String input = trueName.concat(idCard);
            String output = SHA256Util.getSHA256(input);
            // 主键
            userInfoSHA256Entity.setUserId(userId);
            // 用户姓名
            userInfoSHA256Entity.setTrueName(trueName);
            // 用户身份证号
            userInfoSHA256Entity.setIdCard(idCard);
            // 已投资用户是否上报数据
            userInfoSHA256Entity.setIsLenderOneUp("0");
            // 开户未投资用户是否上报数据
            userInfoSHA256Entity.setIsLenderZeroUp("0");
            // 开户用户是否上报数据
            userInfoSHA256Entity.setIsOpenUp("0");
            // 密文
            userInfoSHA256Entity.setSha256(output);
            // 插入时间
            userInfoSHA256Entity.setCreateTime(GetDate.getDate());
            // mongo插入
            this.userInfoSHA256Dao.save(userInfoSHA256Entity);
        }
        return userInfoSHA256Entity;
    }

    /**
     * 借款人性别转换
     *
     * @param sex
     * @return
     */
    public String convertSex(Integer sex) {
        switch (sex) {
            case 0:
                return "未知";
            case 1:
                return "男";
            case 2:
                return "女";
            default:
                return "";
        }
    }


    /**
     * 期限类型(还款方式)转换
     *
     * @param borrowStyle
     * @return
     */
    public String convertTermType(String borrowStyle) {
        if (CalculatesUtil.STYLE_ENDDAY.equals(borrowStyle)) {
            return "天";
        } else {
            return "月";
        }
    }

    /**
     * 产品分类转换
     *
     * @param projectType
     * @return
     */
    public String convertProductMark(Integer projectType) {
        switch (projectType) {
            case 4:
                return "新手标";
            default:
                return "散标";
        }
    }

    public String convertPayType(String borrowStyle) {
        switch (borrowStyle) {
            case CalculatesUtil.STYLE_END:
                return "4";
            case CalculatesUtil.STYLE_ENDDAY:
                return "4";
            case CalculatesUtil.STYLE_MONTH:
                return "1";
            case CalculatesUtil.STYLE_PRINCIPAL:
                return "2";
            case CalculatesUtil.STYLE_ENDMONTH:
                return "3";
            default:
                return "5";
        }
    }

    /**
     * 房产抵押转换
     *
     * @param housesType
     * @return
     */
    public String convertHousesType(String housesType) {
        switch (housesType) {
            case "1":
                return "住宅";
            case "2":
                return "商业用房";
            case "3":
                return "商业用地";
            case "4":
                return "工业用房";
            case "5":
                return "工业用地";
            default:
                return "";
        }
    }

}
	