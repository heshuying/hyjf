/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.bank.service.borrow.nifa.contractessence;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.bank.service.fdd.fddgeneratecontract.FddGenerateContractConstant;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author PC-LIUSHOUYI
 * @version NifaContractEssenceServiceImpl, v0.1 2018/7/9 14:31
 */
@Service
public class NifaContractEssenceServiceImpl extends BaseServiceImpl implements NifaContractEssenceService {

    Logger _log = LoggerFactory.getLogger(NifaContractEssenceServiceImpl.class);

    private String thisMessName = "【生成合同要素信息】";

    /**
     * 18位社会信用代码
     */
    private String comSocialCreditCode = PropUtils.getSystem("hyjf.com.social.credit.code");

    @Override
    public boolean insertContractEssence(String borrowNid, String ordid, BorrowTender borrowTender) {

        NifaContractEssenceExample nifaContractEssenceExample = new NifaContractEssenceExample();
        nifaContractEssenceExample.createCriteria().andContractNoEqualTo(ordid);
        List<NifaContractEssence> nifaContractEssenceList = this.nifaContractEssenceMapper.selectByExample(nifaContractEssenceExample);
        // 已经生成数据的订单不再重复生成
        if (null != nifaContractEssenceList && nifaContractEssenceList.size() > 0) {
            _log.info(thisMessName + "相同合同编号数据已导入合同要素信息表，借款编号:" + borrowNid + "订单号：" + ordid);
            return true;
        }

        // 获取借款详情
        Borrow borrow = this.getBorrowByNid(borrowNid);

        // 出借人信息
        Integer userId = borrowTender.getUserId();
        UsersInfo lenderInfo = this.getUsersInfoByUserId(userId);

        // 获取最新合同模板
        FddTemplet fddTemplet = this.selectFddTemplet();
        if (null == fddTemplet) {
            _log.error(thisMessName + "无法匹配模板----借款编号：" + borrowNid);
            return false;
        }

        // 根据合同编号获取合同模版约定条款
        NifaContractTemplate nifaContractTemplate = this.selectNifaContractTemplateByTemplateNid(fddTemplet.getTempletId());
        if (null == nifaContractTemplate) {
            _log.error(thisMessName + "未获取到出借订单相关合同模板信息，借款编号:" + borrowNid + "订单号：" + ordid);
            return false;
        }

        // 获取最新互金字段定义
        NifaFieldDefinition nifaFieldDefinition = this.selectNifaFieldDefinition();
        if (null == nifaFieldDefinition) {
            _log.error(thisMessName + "未获取到互金字段定义信息，借款编号:" + borrowNid + "订单号：" + ordid);
            return false;
        }

        // 获取还款计算公式
        BorrowStyleWithBLOBs borrowStyleWithBLOBs = this.selectBorrowStyle(borrow.getBorrowStyle());
        if (null == borrowStyleWithBLOBs) {
            _log.error(thisMessName + "未获取到该还款方式对应的还款公式，借款编号:" + borrowNid + "订单号：" + ordid);
            return false;
        }

        // 获取公司信息
        SiteSetting siteSetting = this.selectSiteSetting();
        if (null == siteSetting) {
            _log.error(thisMessName + "未获取到获取公司名称，借款编号:" + borrowNid + "订单号：" + ordid);
            return false;
        }

        // 合同要素信息处理
        NifaContractEssence nifaContractEssence = new NifaContractEssence();
        // 统一社会信用代码
        nifaContractEssence.setPlatformNo(comSocialCreditCode);
        // 从业机构名称
        nifaContractEssence.setPlatformName(siteSetting.getCompany());
        // 项目编号
        nifaContractEssence.setProjectNo(borrow.getBorrowNid());
        // 合同名称
        nifaContractEssence.setContractName(FddGenerateContractConstant.CONTRACT_DOC_TITLE);
        // 合同编号
        nifaContractEssence.setContractNo(ordid);
        // 合同签署日
        nifaContractEssence.setContractTime(GetDate.timestamptoStrYYYYMMDD(GetDate.getNowTime10()));
        // 出借人类型 01 个人
        nifaContractEssence.setInvestorType(1);
        // 出借人证件类型
        nifaContractEssence.setInvestorCertType("0");
        // 出借人证件号码
        nifaContractEssence.setInvestorCertNo(lenderInfo.getIdcard());
        // 出借人姓名
        nifaContractEssence.setInvestorName(lenderInfo.getTruename());

        // 出借人暂无机构
        // 出借人统一社会信用代码
        nifaContractEssence.setInvestorNacaoNo("");
        // 出借人组织机构代码
        nifaContractEssence.setInvestorOrgcodeNo("");
        // 出借人名称
        nifaContractEssence.setInvestorCompany("");

        // 借款人信息1：公司 2：个人
        if ("1".equals(borrow.getCompanyOrPersonal())) {
            BorrowUsers borrowUsers = this.selectBorrowUsersByBorrowNid(borrow.getBorrowNid());
            if (null == borrowUsers) {
                _log.error(thisMessName + "未获取到借款人信息，订单号：" + borrowTender.getNid());
                return false;
            }
            // 借款人类型
            nifaContractEssence.setBorrowerType(2);
            // 合同签署方
            nifaContractEssence.setContractSigner("甲方：" + lenderInfo.getTruename() + "，乙方：" + borrowUsers.getUsername());
            // 借款人统一社会信用代码
            if (null != borrowUsers.getSocialCreditCode()) {
                nifaContractEssence.setBorrowerNacaoNo(borrowUsers.getSocialCreditCode());
            } else {
                // 借款人组织机构代码
                nifaContractEssence.setBorrowerOrgcodeNo(null != borrowUsers.getCorporateCode() ? borrowUsers.getCorporateCode() : "");
            }
            // 借款人名称
            nifaContractEssence.setBorrowerCompany(borrowUsers.getUsername());
            // 通知与送达
            nifaContractEssence.setNoticeAddress(borrowUsers.getRegistrationAddress());
        }
        if ("2".equals(borrow.getCompanyOrPersonal())) {
            BorrowManinfo borrowManinfo = this.selectBorrowMainfo(borrow.getBorrowNid());
            if (null == borrowManinfo) {
                _log.error(thisMessName + "未获取到借款公司信息，订单号：" + borrowTender.getNid());
                return false;
            }
            // 借款人证件类型 0:居民身份证
            nifaContractEssence.setBorrowerCertType("0");
            // 借款人类型
            nifaContractEssence.setBorrowerType(1);
            // 合同签署方
            nifaContractEssence.setContractSigner("甲方：" + lenderInfo.getTruename() + "，乙方：" + borrowManinfo.getName());
            // 借款人证件号码
            nifaContractEssence.setBorrowerCertNo(borrowManinfo.getCardNo());
            // 借款人姓名
            nifaContractEssence.setBorrowerName(borrowManinfo.getName());
            // 借款人地址
            nifaContractEssence.setBorrowerAddress(borrowManinfo.getAddress());
            // 通知与送达
            nifaContractEssence.setNoticeAddress(borrowManinfo.getAddress());
        }

        // 借款金额
        nifaContractEssence.setInvestAmount(borrowTender.getAccount().toString());
        // 年化利率（居间服务协议统一取标的的年华利率）
        nifaContractEssence.setBorrowRate(borrow.getBorrowApr().divide(new BigDecimal("100")).toString());
        // 借款用途
        nifaContractEssence.setBorrowUse(borrow.getFinancePurpose());
        // 借款用途限制
        nifaContractEssence.setBorrowUseLimit(nifaFieldDefinition.getBorrowingRestrictions());
        // 借款放款日 最后一笔放款时间
        nifaContractEssence.setLoanDate(GetDate.timestamptoStrYYYYMMDD(GetDate.getNowTime10()));
        // 借款放款日判断依据
        nifaContractEssence.setLoanDateBasis(nifaFieldDefinition.getJudgmentsBased());
        // 起息日 放款日开始计息
        nifaContractEssence.setStartDate(GetDate.timestamptoStrYYYYMMDD(GetDate.getNowTime10()));
        // 是否按月还款
        boolean isMonth = false;
        // 到期日
        String lasterDay = "";
        // 还款方式 去还款表取数据
        if ("month".equals(borrow.getBorrowStyle())) {
            isMonth = true;
            nifaContractEssence.setRepayType(10);
        } else if ("end".equals(borrow.getBorrowStyle())) {
            isMonth = false;
            nifaContractEssence.setRepayType(25);
        } else if ("endmonth".equals(borrow.getBorrowStyle())) {
            isMonth = true;
            nifaContractEssence.setRepayType(9);
        } else if ("endday".equals(borrow.getBorrowStyle())) {
            isMonth = false;
            nifaContractEssence.setRepayType(25);
        } else if ("principal".equals(borrow.getBorrowStyle())) {
            isMonth = true;
            nifaContractEssence.setRepayType(11);
        }

        // 还款计划 json串 查询还款计划表
        JSONArray jsonArray = new JSONArray();
        if (isMonth) {
            // 还款期数
            nifaContractEssence.setRepayNum(borrow.getBorrowPeriod());
            // 根据借款编号获取还款计划
            List<BorrowRecoverPlan> borrowRecoverPlanList = this.selectBorrowRecoverPlanList(borrow.getBorrowNid(), userId, ordid);
            if (null == borrowRecoverPlanList) {
                _log.error(thisMessName + "未获取到借款人分期回款信息，订单号：" + borrowTender.getNid() + "借款编号：" + borrow.getBorrowNid() + "借款人id：" + userId);
                return false;
            }
            // 最后一期还款日
            lasterDay = borrowRecoverPlanList.get(borrowRecoverPlanList.size() - 1).getRecoverTime();
            // json数据生成
            for (BorrowRecoverPlan borrowRecoverPlan : borrowRecoverPlanList) {
                Map<String, String> map = new HashMap<String, String>();
                if (Validator.isNumber(lasterDay)) {
                    try {
                        map.put("date", GetDate.timestamptoStrYYYYMMDD(Integer.parseInt(borrowRecoverPlan.getRecoverTime())));
                    } catch (Exception e) {
                        _log.error(thisMessName + "还款日格式化失败，borrowNid:" + borrowNid + " 还款日期：" + borrowRecoverPlan.getRecoverTime());
                        e.printStackTrace();
                        return false;
                    }
                } else {
                    _log.error(thisMessName + "还款日格式化失败，borrowNid:" + borrowNid + " 还款日期：" + borrowRecoverPlan.getRecoverTime());
                    return false;
                }
                map.put("principal", borrowRecoverPlan.getRecoverCapital().toString());
                map.put("interest", borrowRecoverPlan.getRecoverInterest().toString());
                jsonArray.add(map);
            }
        } else {
            // 还款期数（到期还本还息的只有1期）
            nifaContractEssence.setRepayNum(1);
            // 根据借款编号获取还款计划
            BorrowRecover borrowRecover = this.selectBorrowRecover(borrow.getBorrowNid(), userId, ordid);
            if (null == borrowRecover) {
                _log.error(thisMessName + "未获取到回款计划信息，借款编号:" + borrowNid + "订单号：" + ordid + "借款编号：" + borrow.getBorrowNid() + "借款人id：" + userId);
                return false;
            }
            // 最后还款日
            lasterDay = borrowRecover.getRecoverTime();
            // json数据生成
            Map<String, String> map = new HashMap<String, String>();
            if (Validator.isNumber(lasterDay)) {
                try {
                    map.put("date", GetDate.timestamptoStrYYYYMMDD(Integer.parseInt(borrowRecover.getRecoverTime())));
                } catch (Exception e) {
                    _log.error(thisMessName + "还款日格式化失败，borrowNid:" + borrowNid + " 还款日期：" + borrowRecover.getRecoverTime());
                    e.printStackTrace();
                    return false;
                }
            } else {
                _log.error(thisMessName + "还款日格式化失败，borrowNid:" + borrowNid + " 还款日期：" + borrowRecover.getRecoverTime());
                return false;
            }
            map.put("principal", borrowRecover.getRecoverCapital().toString());
            map.put("interest", borrowRecover.getRecoverInterest().toString());
            jsonArray.add(map);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("plan", jsonArray);
        nifaContractEssence.setRepayPlan(jsonObject.toString());

        // 到期日设定
        if (Validator.isNumber(lasterDay)) {
            try {
                nifaContractEssence.setExpiryDate(GetDate.timestamptoStrYYYYMMDD(Integer.parseInt(lasterDay)));
            } catch (Exception e) {
                _log.error(thisMessName + "最后一期还款日格式化失败，borrowNid:" + borrowNid);
                e.printStackTrace();
                return false;
            }
        } else {
            _log.error(thisMessName + "最后一期还款日格式化失败，borrowNid:" + borrowNid);
            return false;
        }
        // 还款方式含义及计算公式
        nifaContractEssence.setRepayFormula(borrowStyleWithBLOBs.getContents());

        // 还款日判断依据
        nifaContractEssence.setRepayDateRule(nifaFieldDefinition.getRepayDateRule());
        // 逾期还款定义
        nifaContractEssence.setOverdueRepayDef(nifaFieldDefinition.getOverdueDefinition());
        // 逾期还款责任
        nifaContractEssence.setOverdueRepayResp(nifaFieldDefinition.getOverdueResponsibility());
        // 逾期还款流程
        nifaContractEssence.setOverdueRepayProc(nifaFieldDefinition.getOverdueProcess());
        // 合同生效日
        nifaContractEssence.setContractEffectiveDate(GetDate.timestamptoStrYYYYMMDD(GetDate.getNowTime10()));
        // 合同模板编号
        nifaContractEssence.setContractTemplateNo(comSocialCreditCode + fddTemplet.getTempletId());
        // 数据创建更新人id
        nifaContractEssence.setCreateUserId(0);
        nifaContractEssence.setUpdateUserId(0);
        // 插入数据库
        boolean result = this.nifaContractEssenceMapper.insert(nifaContractEssence) > 0 ? true : false;
        return result;
    }
    // del by liushouyi nifa2 20181128 tenderListByBorrowNid
    /**
     * 根据借款编号和出借人id获取回款信息
     *
     * @param borrowNid
     * @param userId
     * @return
     */
    private BorrowRecover selectBorrowRecover(String borrowNid, Integer userId, String ordid) {
        BorrowRecoverExample example = new BorrowRecoverExample();
        example.createCriteria().andBorrowNidEqualTo(borrowNid).andUserIdEqualTo(userId).andNidEqualTo(ordid);
        List<BorrowRecover> borrowRecoverList = this.borrowRecoverMapper.selectByExample(example);
        if (null != borrowRecoverList && borrowRecoverList.size() > 0) {
            return borrowRecoverList.get(0);
        }
        return null;
    }

    /**
     * 根据借款编号和借款人id获取回款信息
     *
     * @param borrowNid
     * @param userId
     * @return
     */
    private List<BorrowRecoverPlan> selectBorrowRecoverPlanList(String borrowNid, Integer userId, String ordid) {
        BorrowRecoverPlanExample example = new BorrowRecoverPlanExample();
        example.createCriteria().andUserIdEqualTo(userId).andBorrowNidEqualTo(borrowNid).andNidEqualTo(ordid);
        example.setOrderByClause("recover_period asc");
        List<BorrowRecoverPlan> borrowRecoverPlanList = this.borrowRecoverPlanMapper.selectByExample(example);
        if (null != borrowRecoverPlanList && borrowRecoverPlanList.size() > 0) {
            return borrowRecoverPlanList;
        }
        return null;
    }

    /**
     * 根据订单编号查询生成合同信息
     *
     * @param tenderAgreementID
     * @return
     */
    private TenderAgreement selectTenderAgreementByNid(String tenderAgreementID) {
        TenderAgreement tenderAgreement = this.tenderAgreementMapper.selectByPrimaryKey(Integer.valueOf(tenderAgreementID));
        return tenderAgreement;
    }

    /**
     * 根据模板编号获取合同模版约定条款
     *
     * @param templateNid
     * @return
     */
    private NifaContractTemplate selectNifaContractTemplateByTemplateNid(String templateNid) {
        NifaContractTemplateExample nifaContractTemplateExample = new NifaContractTemplateExample();
        NifaContractTemplateExample.Criteria criteria = nifaContractTemplateExample.createCriteria();
        criteria.andTempletNidEqualTo(templateNid);
        List<NifaContractTemplate> nifaContractTemplateList = this.nifaContractTemplateMapper.selectByExample(nifaContractTemplateExample);
        if (null != nifaContractTemplateList && nifaContractTemplateList.size() > 0) {
            return nifaContractTemplateList.get(0);
        }
        return null;
    }

    /**
     * 获取最近互金字段定义
     *
     * @return
     */
    private NifaFieldDefinition selectNifaFieldDefinition() {
        NifaFieldDefinitionExample nifaFieldDefinitionExample = new NifaFieldDefinitionExample();
        nifaFieldDefinitionExample.setOrderByClause("update_time desc");
        List<NifaFieldDefinition> nifaFieldDefinitionList = this.nifaFieldDefinitionMapper.selectByExample(nifaFieldDefinitionExample);
        if (null != nifaFieldDefinitionList && nifaFieldDefinitionList.size() > 0) {
            return nifaFieldDefinitionList.get(0);
        }
        return null;
    }

    /**
     * 根据还款方式获取还款计算公式
     *
     * @param borrowStyle
     * @return
     */
    private BorrowStyleWithBLOBs selectBorrowStyle(String borrowStyle) {
        BorrowStyleExample example = new BorrowStyleExample();
        BorrowStyleExample.Criteria cra = example.createCriteria();
        cra.andStatusEqualTo(CustomConstants.FLAG_STATUS_ENABLE);
        cra.andNidEqualTo(borrowStyle);
        List<BorrowStyleWithBLOBs> borrowStyleWithBLOBsList = borrowStyleMapper.selectByExampleWithBLOBs(example);
        if (null != borrowStyleWithBLOBsList && borrowStyleWithBLOBsList.size() > 0) {
            return borrowStyleWithBLOBsList.get(0);
        }
        return null;
    }

    /**
     * 获取公司名称
     *
     * @return
     */
    private SiteSetting selectSiteSetting() {
        SiteSettingExample example = new SiteSettingExample();
        List<SiteSetting> siteSettings = this.siteSettingMapper.selectByExample(example);
        if (null != siteSettings && siteSettings.size() > 0) {
            return siteSettings.get(0);
        }
        return null;
    }

    /**
     * 获取最新的合同模板
     *
     * @return
     */
    private FddTemplet selectFddTemplet() {
        FddTempletExample example = new FddTempletExample();
        FddTempletExample.Criteria criteria = example.createCriteria();
        criteria.andProtocolTypeEqualTo(FddGenerateContractConstant.PROTOCOL_TYPE_TENDER);
        criteria.andIsActiveEqualTo(1);
        criteria.andCaFlagEqualTo(1);
        List<FddTemplet> fddTemplets = this.fddTempletMapper.selectByExample(example);
        if (fddTemplets != null && fddTemplets.size() == 1) {
            return fddTemplets.get(0);
        }
        return null;
    }

    /**
     * 查询标的下投资信息
     *
     * @param borrowNid
     * @return
     */
    @Override
    public List<BorrowTender> tenderListByBorrowNid(String borrowNid) {
        BorrowTenderExample example = new BorrowTenderExample();
        example.createCriteria().andBorrowNidEqualTo(borrowNid).andStatusEqualTo(1);
        List<BorrowTender> borrowTenderList = this.borrowTenderMapper.selectByExample(example);
        if (null != borrowTenderList && borrowTenderList.size() > 0) {
            return borrowTenderList;
        }
        return null;
    }
}
