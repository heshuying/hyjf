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

package com.hyjf.mqreceiver.hgdatareport;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.common.calculate.CalculatesUtil;
import com.hyjf.common.util.*;
import com.hyjf.mongo.hgdatareport.base.BaseHgDataReportEntity;
import com.hyjf.mongo.hgdatareport.dao.NifaBorrowInfoDao;
import com.hyjf.mongo.hgdatareport.dao.UserInfoSHA256Dao;
import com.hyjf.mongo.hgdatareport.entity.NifaBorrowInfoEntity;
import com.hyjf.mongo.hgdatareport.entity.UserInfoSHA256Entity;
import com.hyjf.mqreceiver.hgdatareport.bifa.BifaCommonConstants;
import com.hyjf.mqreceiver.hgdatareport.bifa.BifaReportResultBean;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.nifa.NifaTenderUserInfoCustomize;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liubin
 */
@Service
public class BaseHgDateReportServiceImpl extends BaseServiceImpl implements BaseHgDateReportService {


    Logger _log = LoggerFactory.getLogger(BaseHgDateReportServiceImpl.class);

    private String thisMessName = "合规数据上送共通方法";
    private String logHeader = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_BIFA + " " + thisMessName + "】";

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
    public static final String BIFA_PASS_WORD = PropUtils.getSystem("hyjf.bifa.pass.word");

    @Autowired
    NifaBorrowInfoDao nifaBorrowInfoDao;

    @Autowired
    UserInfoSHA256Dao userInfoSHA256Dao;

    /**
     * 平台编号
     */
    public static final String SOURCE_CODE = PropUtils.getSystem("hyjf.source.code");

    /**
     * 散标链接
     */
    public static final String SOURCE_PRODUCT_URL_BORROW = PropUtils.getSystem("hyjf.source.product.url.borrow");
    /**
     * 智投链接
     */
    public static final String SOURCE_PRODUCT_URL_HJHPLAN = PropUtils.getSystem("hyjf.source.product.url.hjhplan");
    /**
     * 散标转让产品链接
     */
    public static final String SOURCE_PRODUCT_URL_BORROW_CREDIT = PropUtils.getSystem("hyjf.source.product.url.borrow.credit");
    /**
     * 智投产品链接
     */
    public static final String SOURCE_PRODUCT_URL_HJH_CREDIT = PropUtils.getSystem("hyjf.source.product.url.hjh.credit");

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
        String[] params = {"arg0", "arg1", "arg2"};
        return WebServiceUtil.webService(BIFA_CRT_PATH, BIFA_END_POINT, BIFA_NAME_SPACE, methodName, params, new Object[]{BIFA_LOGIN_NAME, BIFA_PASS_WORD, encmsg});
    }

    /**
     * 根据借款主体名称获取CA认证编号
     *
     * @param name
     * @return
     */
    @Override
    public CertificateAuthority selectCAInfoByUsername(String name, String cardNo) {
        if (StringUtils.isBlank(cardNo)) {
            return null;
        }
        CertificateAuthorityExample example = new CertificateAuthorityExample();
        example.createCriteria().andTrueNameEqualTo(name).andIdNoEqualTo(cardNo);
        List<CertificateAuthority> certificateAuthoritieList = this.certificateAuthorityMapper.selectByExample(example);
        if (null != certificateAuthoritieList && certificateAuthoritieList.size() > 0) {
            return certificateAuthoritieList.get(0);
        }
        return null;
    }

    /**
     * 根据平台借款等级变换中互金借款等级
     *
     * @param borrowLevel
     * @return
     */
    @Override
    public Integer getBorrowLevel(String borrowLevel) {
        switch (borrowLevel) {
            case "BBB":
                return 0;
            case "A":
                return 1;
            case "AA-":
                return 2;
            case "AA":
                return 3;
            case "AA+":
                return 4;
            case "AAA":
                return 5;
            default:
                return -1;
        }
    }

    /**
     * 根据借款人获取借款人银行卡信息
     *
     * @param userId
     * @return
     */
    @Override
    public BankCard selectBankCardByUserId(Integer userId) {
        BankCardExample example = new BankCardExample();
        example.createCriteria().andUserIdEqualTo(userId);
        List<BankCard> bankCards = this.bankCardMapper.selectByExample(example);
        if (null != bankCards && bankCards.size() > 0) {
            return bankCards.get(0);
        }
        return null;
    }

    /**
     * 判断到期还款还是分期还款
     *
     * @param borrowStyle
     * @return
     */
    @Override
    public boolean isMonth(String borrowStyle) {
        // 是否按月还款
        boolean isMonth = false;
        // 还款方式 去还款表取数据
        if ("month".equals(borrowStyle)) {
            isMonth = true;
        } else if ("end".equals(borrowStyle)) {
            isMonth = false;
        } else if ("endmonth".equals(borrowStyle)) {
            isMonth = true;
        } else if ("endday".equals(borrowStyle)) {
            isMonth = false;
        } else if ("principal".equals(borrowStyle)) {
            isMonth = true;
        }
        return isMonth;
    }

    /**
     * 根据借款编号获取还款计划 到期还款
     *
     * @param borrowNid
     * @return
     */
    @Override
    public BorrowRepay selectBorrowRepayByBorrowNid(String borrowNid) {
        BorrowRepayExample example = new BorrowRepayExample();
        example.createCriteria().andBorrowNidEqualTo(borrowNid);
        List<BorrowRepay> borrowRepayList = this.borrowRepayMapper.selectByExample(example);
        if (null != borrowRepayList && borrowRepayList.size() > 0) {
            return borrowRepayList.get(0);
        }
        return null;
    }

    /**
     * 根据借款编号获取还款计划 分期还款
     *
     * @param borrowNid
     * @return
     */
    @Override
    public List<BorrowRepayPlan> selectBorrowRepayPlanByBorrowNid(String borrowNid) {
        BorrowRepayPlanExample example = new BorrowRepayPlanExample();
        example.createCriteria().andBorrowNidEqualTo(borrowNid);
        example.setOrderByClause("repay_period asc");
        List<BorrowRepayPlan> borrowRepayPlanList = this.borrowRepayPlanMapper.selectByExample(example);
        if (null != borrowRepayPlanList && borrowRepayPlanList.size() > 0) {
            return borrowRepayPlanList;
        }
        return null;
    }

    /**
     * 查询该借款数据是否上报完成
     *
     * @param borrowNid
     * @param message
     * @return
     */
    @Override
    public NifaBorrowInfoEntity selectNifaBorrowInfoByBorrowNid(String borrowNid, String message) {
        Query query = new Query();
        Criteria criteria = Criteria.where("projectNo").is(CustomConstants.COM_SOCIAL_CREDIT_CODE + "1" + borrowNid).and("message").is(message);
        query.addCriteria(criteria);
        NifaBorrowInfoEntity nifaBorrowInfoEntity = nifaBorrowInfoDao.findOne(query);
        if (null != nifaBorrowInfoEntity) {
            return nifaBorrowInfoEntity;
        }
        return null;
    }

    /**
     * 获取散标债转信息表
     *
     * @param creditNid
     * @return
     */
    @Override
    public BorrowCredit selectBorrowCreditByCreditNid(String creditNid) {
        BorrowCreditExample example = new BorrowCreditExample();
        example.createCriteria().andCreditNidEqualTo(Integer.parseInt(creditNid));
        List<BorrowCredit> borrowCreditList = this.borrowCreditMapper.selectByExample(example);
        if (null != borrowCreditList && borrowCreditList.size() > 0) {
            return borrowCreditList.get(0);
        }
        return null;
    }

    /**
     * 获取散标债转承接人承接信息
     *
     * @param creditNid
     * @return
     */
    @Override
    public List<CreditTender> selectCreditTenderByCreditNid(String creditNid) {
        CreditTenderExample example = new CreditTenderExample();
        example.createCriteria().andCreditNidEqualTo(creditNid);
        List<CreditTender> creditTenderList = this.creditTenderMapper.selectByExample(example);
        if (null != creditTenderList && creditTenderList.size() > 0) {
            return creditTenderList;
        }
        return null;
    }

    /**
     * 根据债转编号查询承接人承接记录
     *
     * @param creditNid
     * @return
     */
    @Override
    public List<HjhDebtCreditTender> selectHjhDebtCreditTenderByCreditNid(String creditNid) {
        HjhDebtCreditTenderExample example = new HjhDebtCreditTenderExample();
        example.createCriteria().andCreditNidEqualTo(creditNid);
        List<HjhDebtCreditTender> hjhDebtCreditTenderList = this.hjhDebtCreditTenderMapper.selectByExample(example);
        if (null != hjhDebtCreditTenderList && hjhDebtCreditTenderList.size() > 0) {
            return hjhDebtCreditTenderList;
        }
        return null;
    }

    /**
     * 根据债转编号获取债转信息
     *
     * @param creditNid
     * @return
     */
    @Override
    public HjhDebtCredit selectHjhDebtCreditByCreditNid(String creditNid) {
        HjhDebtCreditExample example = new HjhDebtCreditExample();
        example.createCriteria().andCreditNidEqualTo(creditNid);
        List<HjhDebtCredit> hjhDebtCreditList = this.hjhDebtCreditMapper.selectByExample(example);
        if (null != hjhDebtCreditList && hjhDebtCreditList.size() > 0) {
            return hjhDebtCreditList.get(0);
        }
        return null;
    }

    /**
     * 获取上一期订单的债转编号
     *
     * @param planOrderId
     * @return
     */
    @Override
    public HjhDebtCreditTender selectHjhDebtCreditTenderByPlanOrderId(String planOrderId) {
        HjhDebtCreditTenderExample example = new HjhDebtCreditTenderExample();
        example.createCriteria().andAssignPlanOrderIdEqualTo(planOrderId);
        List<HjhDebtCreditTender> hjhDebtCreditTenderList = this.hjhDebtCreditTenderMapper.selectByExample(example);
        if (null != hjhDebtCreditTenderList && hjhDebtCreditTenderList.size() > 0) {
            return hjhDebtCreditTenderList.get(0);
        }
        return null;
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
     * 上报数据
     *
     * @param
     * @return
     */
    @Override
    public <T extends BaseHgDataReportEntity> T reportData(String methodName, T data) {
        try {
            String encmsg = JSONObject.toJSONString(data);
            String result = this.webService(methodName, encmsg);
            BifaReportResultBean reportResult = JSONObject.parseObject(result, BifaReportResultBean.class);
            data.setErrCode(reportResult.getReCode());
            data.setErrDesc(reportResult.getMessage());

            if (BifaCommonConstants.SUCCESSCODE.equals(reportResult.getReCode())) {
                data.setReportStatus("1");
            } else if (BifaCommonConstants.REPORTEDCODE.equals(reportResult.getReCode())
                    && reportResult.getMessage().indexOf(BifaCommonConstants.EXIST) >= 0 ){
                //已经上报过的状态置为7
                data.setReportStatus("7");
            } else {
                data.setReportStatus("9");
            }

        } catch (Exception e) {
            data.setReportStatus("9");
            data.setErrCode(BifaCommonConstants.ERRCODE);
            data.setErrDesc(BifaCommonConstants.ERRDESC);
            _log.error(logHeader + "北互金上报数据失败！！！", e);
        } finally {
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
    public UserInfoSHA256Entity selectUserIdToSHA256(Integer userId, String trueName, String idCard) {
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
     * 根据借款编号查询该借款下所有投资人的相关信息
     *
     * @param borrowNid
     * @return
     */
    @Override
    public List<NifaTenderUserInfoCustomize> selectTenderUserInfoByBorrowNid(String borrowNid) {
        return this.nifaBorrowerInfoCustomizeMapper.selectTenderUserInfoByBorrowNid(borrowNid);
    }

    /**
     * 获取企业注册所属地区编号
     *
     * @param code
     * @return
     */
    @Override
    public String getBorrowUsersArea(String code) {
        if (StringUtils.isBlank(code)) {
            return "";
        }
        switch (code) {
            case "16592028-6":
                return "370800";
            case "16947057-7":
                return "370881";
            case "16624348-3":
                return "370828";
            case "86593626-2":
                return "370800";
            case "16592032-3":
                return "370800";
            case "86594449-X":
                return "370800";
            case "16601600-2":
                return "370800";
            case "137010370090131":
                return "370103";
            default:
                return code.substring(0, 6);
        }
    }
}
