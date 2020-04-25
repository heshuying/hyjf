package com.hyjf.bank.service.fdd.fddgeneratecontract;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.bank.service.user.credit.CreditAssignedBean;
import com.hyjf.bank.service.user.repay.RepayService;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.web.TenderToCreditDetailCustomize;
import com.hyjf.mybatis.model.customize.web.hjh.UserHjhInvistDetailCustomize;
import com.hyjf.pay.lib.fadada.bean.DzqzCallBean;
import com.hyjf.pay.lib.fadada.util.DzqzCallUtil;
import com.hyjf.pay.lib.fadada.util.DzqzConstant;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FddGenerateContractServiceImpl extends BaseServiceImpl implements FddGenerateContractService {

    Logger log = LoggerFactory.getLogger(this.getClass());


    @Autowired
    @Qualifier("myAmqpTemplate")
    private RabbitTemplate rabbitTemplate;

    /**
     * 签署合同
     *
     * @param bean 入参
     */
    private void updateSignContract(FddGenerateContractBean bean) {

        //协议类型
        int transType = bean.getTransType();

        String txCode = DzqzConstant.FDD_EXTSIGN_AUTO;

        int tenderUserId = bean.getTenderUserId();
        String customerId = null;
        CertificateAuthorityExample example = new CertificateAuthorityExample();
        example.createCriteria().andUserIdEqualTo(tenderUserId);
        List<CertificateAuthority> authorityList = this.certificateAuthorityMapper.selectByExample(example);
        if (authorityList != null && authorityList.size() > 0) {
            CertificateAuthority authority = authorityList.get(0);
            customerId = authority.getCustomerId();
        }
        String payurl = PropUtils.getSystem(DzqzConstant.HYJF_PAY_FDD_NOTIFY_URL);
        String notifyUrl = payurl + DzqzConstant.REQUEST_MAPPING_CALLAPI_SIGNNODIFY + "?transType=" + transType;
        //出借人签署
        DzqzCallBean callBean = new DzqzCallBean();
        callBean.setContract_id(bean.getOrdid());
        callBean.setCustomer_id(customerId);
        callBean.setClient_role(FddGenerateContractConstant.FDD_CLIENT_ROLE_TENDER);
        callBean.setTxCode(txCode);
        callBean.setUserId(tenderUserId);
        callBean.setSign_keyword(FddGenerateContractConstant.FDD_SIGN_KEYWORK_TENDER);
        //计划加入协议
        if(FddGenerateContractConstant.PROTOCOL_TYPE_PLAN == transType){
            callBean.setDoc_title(FddGenerateContractConstant.CONTRACT_DOC_TITLE_PLAN);
        }else if(FddGenerateContractConstant.PROTOCOL_TYPE_TENDER == transType){//居间服务协议
            callBean.setDoc_title(FddGenerateContractConstant.CONTRACT_DOC_TITLE);
        }else if(FddGenerateContractConstant.PROTOCOL_TYPE_CREDIT == transType || FddGenerateContractConstant.FDD_TRANSTYPE_PLAN_CRIDET == transType
                || FddGenerateContractConstant.FDD_TRANSTYPE_apply_CRIDET == transType
                || FddGenerateContractConstant.FDD_TRANSTYPE_APPLY_PLAN_CRIDET == transType){//债转协议
            callBean.setDoc_title(FddGenerateContractConstant.CONTRACT_DOC_TITLE_CREDIT);
            callBean.setSign_keyword(FddGenerateContractConstant.FDD_SIGN_KEYWORK_BORROWER);
        }

        callBean.setKeywordStrategy("0");
        callBean.setNotify_url(notifyUrl);
        DzqzCallBean dzqzCallBean = DzqzCallUtil.callApiBg(callBean);
        String result = dzqzCallBean.getResult();
        String code = dzqzCallBean.getCode();

        if ("success".equals(result) && FddGenerateContractConstant.FDD_RETURN_CODE_1000.equals(code)) {
            if(FddGenerateContractConstant.PROTOCOL_TYPE_CREDIT == transType || FddGenerateContractConstant.FDD_TRANSTYPE_PLAN_CRIDET == transType
                    || FddGenerateContractConstant.FDD_TRANSTYPE_apply_CRIDET == transType
                    || FddGenerateContractConstant.FDD_TRANSTYPE_APPLY_PLAN_CRIDET == transType){//债权转让协议
                //出让人签章
                Integer creditUserID = bean.getCreditUserID();
                String creditCustomerID = getCustomerIDByUserID(creditUserID);
                callBean.setCustomer_id(creditCustomerID);
                callBean.setClient_role(FddGenerateContractConstant.FDD_CLIENT_ROLE_BORROWER);
                callBean.setSign_keyword(FddGenerateContractConstant.FDD_SIGN_KEYWORK_TENDER);
                callBean.setNotify_url(notifyUrl);
                DzqzCallUtil.callApiBg(callBean);
            }else{
                //接入平台签署
                String platFromCustomerId = PropUtils.getSystem(FddGenerateContractConstant.FDD_HYJF_CUSTOMERID);
                callBean.setCustomer_id(platFromCustomerId);
                callBean.setClient_role(FddGenerateContractConstant.FDD_CLIENT_ROLE_PLATFORM);
                if(FddGenerateContractConstant.PROTOCOL_TYPE_PLAN == transType){
                    callBean.setSign_keyword(FddGenerateContractConstant.FDD_SIGN_KEYWORK_BORROWER);
                }else{
                    callBean.setSign_keyword(FddGenerateContractConstant.FDD_SIGN_KEYWORK_PLATFORM);
                }
                callBean.setNotify_url(notifyUrl);
                DzqzCallBean resultBean = DzqzCallUtil.callApiBg(callBean);
                String result2 = resultBean.getResult();
                String code2 = resultBean.getCode();
                if ("success".equals(result2) && FddGenerateContractConstant.FDD_RETURN_CODE_1000.equals(code2)) {
                    if(FddGenerateContractConstant.PROTOCOL_TYPE_TENDER == transType){//居间服务协议
                        //借款人签署
                        callBean.setCustomer_id(bean.getBorrowerCustomerID());
                        callBean.setClient_role(FddGenerateContractConstant.FDD_CLIENT_ROLE_BORROWER);
                        callBean.setSign_keyword(FddGenerateContractConstant.FDD_SIGN_KEYWORK_BORROWER);
                        callBean.setNotify_url(notifyUrl);
                        DzqzCallUtil.callApiBg(callBean);
                    }
                }
            }
        }else{
            log.info("----------------签署异常，订单号：" + bean.getOrdid() + ",返回值：" + code + ",返回描述：" + dzqzCallBean.getMsg());
        }
        //签署结果由异步通知进行处理
    }

    @Autowired
    private RepayService repayService;

    @Override
    public void tenderGenerateContract(FddGenerateContractBean bean) throws Exception {
        log.info("--------------开始生成居间出借协议----订单号：" + bean.getOrdid());
        // 出借人用户ID
        Integer tenderUserId = bean.getTenderUserId();
        if (tenderUserId == null || tenderUserId == 0) {
            throw new Exception("出借人用户ID为空.");
        }
        //出借人代收利息
        BigDecimal tenderInterest = bean.getTenderInterest();
        if(tenderInterest == null){
            throw new Exception("出借人代收利息为空.");
        }
        // 出借订单号
        String tenderNid = bean.getOrdid();
        if (StringUtils.isBlank(tenderNid)) {
            throw new Exception("出借订单号为空.");
        }
        // 标的编号
        String borrowNid = bean.getBorrowNid();
        if (StringUtils.isBlank(borrowNid)) {
            throw new Exception("标的编号为空.");
        }
        // 借款详情
        Borrow borrow = this.getBorrowByNid(borrowNid);
        if (borrow == null) {
            throw new Exception("根据标的编号检索借款详情失败,借款编号:[" + borrowNid + "].");
        }

        String planNid = borrow.getPlanNid();

        // 借款方真实姓名或企业名称
        String borrowTrueName = null;
        // 借款方CA认证客户编号
        String borrowerCustomerID = null;
        //借款人证件号码
        String borrowIdCard = null;
        // del by liuyang 20180326 借款人信息 借款主体为准 modify by cwyang 20180320 散标情况下进行修改
        //modify by cwyang 20180718 增加散标进入计划的情况下借款人也按借款主体来取值
        //判断借款主体是否为企业
        boolean isCompany = "1".equals(borrow.getCompanyOrPersonal());
        Object borrowMain = getValueByBorrowUser(borrow,isCompany);
        //是否存在借款主体
        boolean isBorrowMain =true;
        if(borrowMain == null){//未查询到借款主体
            isBorrowMain = false;
        }
        if(!isBorrowMain){
            // 借款人用户ID
            Integer borrowUserId = borrow.getUserId();
            // 借款人用户信息
            Users borrowUser = this.getUsersByUserId(borrowUserId);
            if (borrowUser == null) {
                throw new Exception("根据借款人用户ID,查询借款人信息失败,借款人用户ID:[" + borrowUserId + "].");
            }

            // 借款人用户详情信息
            UsersInfo borrowUserInfo = this.getUsersInfoByUserId(borrowUserId);
            if (borrowUserInfo == null) {
                throw new Exception("根据借款人用户ID,查询借款人详情信息失败,借款人用户ID:[" + borrowUserId + "].");
            }

            // 如果是企业用户
            if (borrowUser.getUserType() == 1) {
                CorpOpenAccountRecordExample example = new CorpOpenAccountRecordExample();
                example.createCriteria().andUserIdEqualTo(borrowUserId);
                List<CorpOpenAccountRecord> corpOpenAccountRecords = this.corpOpenAccountRecordMapper.selectByExample(example);
                CorpOpenAccountRecord corpOpenAccountRecord = corpOpenAccountRecords.get(0);
                borrowTrueName = corpOpenAccountRecord.getBusiName();
                borrowIdCard = corpOpenAccountRecord.getBusiCode();
            }else{
                borrowTrueName = borrowUserInfo.getTruename();
                borrowIdCard = borrowUserInfo.getIdcard();
            }
            borrowerCustomerID = getCustomerIDByUserID(borrowUserId);
        }else{
            if(isCompany){
                // 借款主体为企业借款
                BorrowUsers  borrowUsers = (BorrowUsers) borrowMain;
                if (borrowUsers == null){
                    throw  new Exception("根据标的编号查询借款主体为企业借款的相关信息失败,标的编号:["+borrowNid+"]");
                }
                borrowTrueName =  borrowUsers.getUsername();
                borrowIdCard = borrowUsers.getSocialCreditCode();
                // 获取CA认证客户编号
                borrowerCustomerID = this.getCACustomerID(borrowUsers);
                if(StringUtils.isBlank(borrowerCustomerID)){
                    throw new Exception("企业借款获取CA认证客户编号失败,企业名称:["+borrowUsers.getUsername()+"],社会统一信用代码:["+borrowUsers.getSocialCreditCode()+"].");
                }
            }else{
                // 借款主体为个人借款
                BorrowManinfo borrowManinfo = (BorrowManinfo) borrowMain;
                if (borrowManinfo == null){
                    throw new Exception("借款主体为个人借款时,获取个人借款信息失败,标的编号:["+borrowNid+"].");
                }
                borrowTrueName = borrowManinfo.getName();
                borrowIdCard = borrowManinfo.getCardNo();
                // 获取CA认证客户编号
                borrowerCustomerID = this.getPersonCACustomerID(borrowManinfo);
                if (StringUtils.isBlank(borrowerCustomerID)){
                    throw new Exception("获取个人借款CA认证客户编号失败,姓名:["+borrowManinfo.getName()+"],身份证号:["+borrowManinfo.getCardNo()+"].");
                }
            }
        }
        // del by liuyang 20180326 借款人信息 借款主体为准 end
        String borrowStyle = borrow.getBorrowStyle();// 还款方式

        boolean isInstalment = false;//是否分期
        if(CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
                || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle)){
            isInstalment = true;
        }

        //月偿还本息
        String monthPayAccount = "";
        String lastPayAccount = "";
        // 出借人用户信息
        Users tenderUser = this.getUsersByUserId(tenderUserId);
        if (tenderUser == null) {
            throw new Exception("根据出借人用户ID,查询出借人信息失败,出借人用户ID:[" + tenderUserId + "].");
        }
        // 出借人用户详情信息
        UsersInfo tenderUsersInfo = this.getUsersInfoByUserId(tenderUserId);
        if (tenderUsersInfo == null) {
            throw new Exception("根据出借人用户ID,查询出借人详情信息失败,出借人用户ID:[" + tenderUserId + "].");
        }
        // 查询出借记录
        BorrowTender borrowTender = this.selectBorrowTender(tenderUserId, borrowNid, tenderNid);
        if (borrowTender == null) {
            throw new Exception("出借记录不存在,出借订单号:[" + tenderNid + "],出借人用户ID:[" + tenderUserId + "],标的编号:[" + borrowNid + "].");
        }
        //查询标的分期信息
        if(isInstalment){
            BorrowRecoverPlan planInfo = this.getborrowRecoverPlan(borrowNid,borrowTender.getNid(),1);
            if(planInfo == null){
                throw new Exception("分期还款记录不存在,投资订单号:[" + tenderNid + "],投资人用户ID:[" + tenderUserId + "],标的编号:[" + borrowNid + "].");
            }
            monthPayAccount = planInfo.getRecoverAccount().toString();
            BorrowRecoverPlan lastPlanInfo = this.getborrowRecoverPlan(borrowNid,borrowTender.getNid(),borrow.getBorrowPeriod());
            if(lastPlanInfo != null){
                lastPayAccount = lastPlanInfo.getRecoverAccount().toString();
            }
        }else{
            monthPayAccount = "-";
            lastPayAccount = borrowTender.getRecoverAccountAll().toString();
        }

        String tenderTrueName = null;
        String tenderIdCard = null;
        tenderTrueName = tenderUsersInfo.getTruename();
        tenderIdCard = tenderUsersInfo.getIdcard();
        if(tenderUser.getUserType() == 1){
            CorpOpenAccountRecord info = getCorpOpenAccountInfoByUserID(tenderUserId);
            tenderTrueName = info.getBusiName();
            tenderIdCard = info.getBusiCode();
        }

        /** 标的基本数据 */
        String borrowDate = "";

        Integer borrowPeriod = borrow.getBorrowPeriod();
        // 是否月标(true:月标, false:天标)
        boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
                || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle) || CustomConstants.BORROW_STYLE_END.equals(borrowStyle);
        if(isMonth){//月表
            borrowDate = borrowPeriod + "个月";
        }else{
            borrowDate = borrowPeriod + "天";
        }

        //中北互金修改借款人用途取值 add by yangchangwei 20181227
        String financePurpose;//借款用途
        financePurpose = this.getParamName(FddGenerateContractConstant.FINANCE_PURPOSE,borrow.getFinancePurpose());

        JSONObject paramter = new JSONObject();

        paramter.put("nid", borrowTender.getNid());//借款编号
        paramter.put("recoverTime", bean.getSignDate());//签署时间
        paramter.put("realName", tenderTrueName);//出借人真实姓名
        paramter.put("idCard", tenderIdCard);//证件号码
//        paramter.put("Username", tenderUser.getUsername());//出借人用户名
        paramter.put("borrowUsername", borrowTrueName);//借款人真实姓名
        paramter.put("BorrowidCard", borrowIdCard);//借款人证件号码
        paramter.put("borrowUse", financePurpose);//借款用途
        paramter.put("borrowValueDay", GetDate.date2Str(GetDate.getDate(borrow.getRecoverLastTime()), GetDate.date_sdf_wz));//放款时间
        paramter.put("borrowDueDay", GetDate.date2Str(GetDate.getDate(Integer.valueOf(borrow.getRepayLastTime())), GetDate.date_sdf_wz));//还款时间
        paramter.put("borrowLendingDay", GetDate.date2Str(GetDate.getDate(borrow.getRecoverLastTime()), GetDate.date_sdf_wz));//放款日
        paramter.put("monthPayAccount", monthPayAccount);//月偿还本息
        paramter.put("lastPayAccount", lastPayAccount);//最后偿还金额
        paramter.put("account", borrow.getAccount().toString());//借款金额
        paramter.put("borrowApr", borrow.getBorrowApr()+"%");//借款利率
        paramter.put("borrowPeriod", borrowDate);//借款期限
        paramter.put("borrowStyleName", this.getBorrowStyle(borrow.getBorrowStyle()));//还款方式
        paramter.put("userInvestAccount", borrowTender.getAccount().toString());//出借人出借金额
        paramter.put("ecoverAccountInterest", tenderInterest.toString());//借款人预期收益

        bean.setBorrowerCustomerID(borrowerCustomerID);
        bean.setContractName(FddGenerateContractConstant.CONTRACT_DOC_TITLE);
        bean.setTenderUserName(tenderUser.getUsername());

        //判断是否已生成合同
        boolean isSign = isCreatContract(borrowTender.getNid());
        log.info("-->isSign：{}", isSign);
        if (isSign){//单独走签署接口
            updateSignContract(bean);
        }else{
            String paramStr = paramter.toJSONString();
            FddTempletExample example = new FddTempletExample();
            FddTempletExample.Criteria criteria = example.createCriteria();
            criteria.andProtocolTypeEqualTo(FddGenerateContractConstant.PROTOCOL_TYPE_TENDER);
            criteria.andIsActiveEqualTo(1);
            criteria.andCaFlagEqualTo(1);
            List<FddTemplet> fddTemplets = this.fddTempletMapper.selectByExample(example);
            if (fddTemplets != null && fddTemplets.size() == 1) {
                FddTemplet fddTemplet = fddTemplets.get(0);
                String templetId = fddTemplet.getTempletId();
                DzqzCallBean callBean = new DzqzCallBean();
                callBean.setParameter_map(paramStr);
                callBean.setTemplate_id(templetId);
                callBean.setTxCode(DzqzConstant.FDD_GENERATECONTRACT);
                callBean.setContract_id(bean.getOrdid());
                callBean.setDoc_title(FddGenerateContractConstant.CONTRACT_DOC_TITLE);
                callBean.setDynamic_tables(null);
                callBean.setUserId(tenderUserId);
                callBean.setFont_size("12");
                callBean.setFont_type("1");
                DzqzCallBean dzqzCallBean = DzqzCallUtil.callApiBg(callBean);
                String result = dzqzCallBean.getResult();
                String code = dzqzCallBean.getCode();
                String msg = dzqzCallBean.getMsg();
                dzqzCallBean.setTemplate_id(templetId);
                if (("success".equals(result) && FddGenerateContractConstant.FDD_RETURN_CODE_1000.equals(code))
                        || ("对不起，合同编号重复！".equals(msg) && FddGenerateContractConstant.FDD_RETURN_CODE_2002.equals(code))) {
                    //协议生成成功，开始签署并进行脱敏处理
                    updateSaveDownUrl(dzqzCallBean, bean);
                    updateSignContract(bean);
                }else {
                    log.info("--------------开始生成居间出借协议返回错误，订单号：" + bean.getOrdid() + "错误码：" + code + ",错误描述：" + msg);
                }
            } else {
                log.info("--------------开始生成居间出借协议异常，无法匹配模板----订单号：" + bean.getOrdid());
            }
        }

    }

    public static void main(String[] args) {
        String s = GetDate.date2Str(GetDate.getDate(1543375207), GetDate.date_sdf_wz);
//        String repayLastTime = GetDate.date2Str(GetDate.getDate(Integer.valueOf(borrow.getRepayLastTime())), GetDate.date_sdf_wz);
        System.out.println(s);
    }

    /**
     * 获取标的的分期还款信息
     * @param borrowNid
     * @param nid
     * @param period
     * @return
     */
    private BorrowRecoverPlan getborrowRecoverPlan(String borrowNid, String nid, Integer period) {
        BorrowRecoverPlanExample example = new BorrowRecoverPlanExample();
        example.createCriteria().andBorrowNidEqualTo(borrowNid).andNidEqualTo(nid).andRecoverPeriodEqualTo(period);
        List<BorrowRecoverPlan> borrowRecoverPlans = this.borrowRecoverPlanMapper.selectByExample(example);
        if(borrowRecoverPlans != null && borrowRecoverPlans.size() > 0){
            return borrowRecoverPlans.get(0);
        }
        return null;
    }


    /**
     * 获取借款主体信息
     * @param borrow
     * @param isCompany
     * @return
     */
    private Object getValueByBorrowUser(Borrow borrow, boolean isCompany) {

        String borrowNid = borrow.getBorrowNid();
        if(isCompany){
            BorrowUsers  borrowUsers = this.getBorrowUsers(borrowNid);
            if (borrowUsers == null){
                return null;
            }
            return borrowUsers;
        }else{
            BorrowManinfo borrowManinfo = this.getBorrowManInfo(borrowNid);
            if (borrowManinfo == null){
                return null;
            }
            return borrowManinfo;
        }
    }


    /**
     * 获取企业用户信息
     * @param tenderUserId
     * @return
     */
    private CorpOpenAccountRecord getCorpOpenAccountInfoByUserID(Integer tenderUserId) {

        CorpOpenAccountRecordExample example = new CorpOpenAccountRecordExample();
        example.createCriteria().andUserIdEqualTo(tenderUserId);
        List<CorpOpenAccountRecord> openAccountRecords = this.corpOpenAccountRecordMapper.selectByExample(example);
        if(openAccountRecords != null && openAccountRecords.size() > 0){
            return openAccountRecords.get(0);
        }
        return null;
    }


    /**
     * 判断是否生成合同
     * @param nid
     * @return
     */
    private boolean isCreatContract(String nid) {

        TenderAgreementExample example = new TenderAgreementExample();
        example.createCriteria().andTenderNidEqualTo(nid);
        List<TenderAgreement> tenderAgreements = this.tenderAgreementMapper.selectByExample(example);
        if (tenderAgreements != null && tenderAgreements.size() > 0){
            return true;
        }
        return false;
    }


    /**
     * 存储下载地址和观看地址
     * @param dzqzCallBean 法大大返回结果
     *
     * @param bean         存储需要的参数
     */
    private void updateSaveDownUrl(DzqzCallBean dzqzCallBean, FddGenerateContractBean bean) {

        TenderAgreement info = new TenderAgreement();
        info.setUserId(bean.getTenderUserId());
        info.setUserName(bean.getTenderUserName());
        info.setBorrowNid(bean.getBorrowNid());
        info.setTenderType(bean.getTenderType());
        info.setTenderNid(bean.getOrdid());
        info.setStatus(1);//生成成功
        info.setContractNumber(bean.getOrdid());
        info.setTempletId(dzqzCallBean.getTemplate_id());
        info.setContractCreateTime(GetDate.getNowTime10());
        info.setContractName(bean.getContractName());
        info.setCreateTime(GetDate.getNowTime10());
        info.setCreateUserId(bean.getTenderUserId());
        info.setCreateUserName(bean.getTenderUserName());
        int flag = this.tenderAgreementMapper.insertSelective(info);
        if (flag == 0) {
            log.info("--------------存储居间协议失败-订单号：" + bean.getOrdid());
        }

    }


    public UserHjhInvistDetailCustomize selectUserHjhInvistDetail(Map<String, Object> params) {
        return hjhPlanCustomizeMapper.selectUserHjhInvistDetail(params);
    }

    @Override
    public void updateBorrowRecover(BorrowRecover borrowRecover) {
        this.borrowRecoverMapper.updateByPrimaryKeySelective(borrowRecover);
    }

    @Override
    public BorrowRecover selectBorrowRecoverByTenderNid(String tenderAgreementID) {
        TenderAgreement tenderAgreement = this.tenderAgreementMapper.selectByPrimaryKey(Integer.valueOf(tenderAgreementID));
        String tenderNid = tenderAgreement.getTenderNid();
        BorrowRecoverExample example = new BorrowRecoverExample();
        example.createCriteria().andNidEqualTo(tenderNid);
        List<BorrowRecover> borrowRecoverList = this.borrowRecoverMapper.selectByExample(example);
        if (borrowRecoverList!= null && borrowRecoverList.size() >0){
            return borrowRecoverList.get(0);
        }
        return null;
    }

    /**
     *
     * @param bean
     */
    @Override
    public void planJoinGenerateContract(FddGenerateContractBean bean) {
        log.info("--------------开始生成计划加入协议----计划加入订单号：" + bean.getOrdid());

        if(StringUtils.isBlank(bean.getOrdid())){
            throw new RuntimeException("-------参数ordid不得为空！------");
        }

        if(bean.getTenderUserId() == 0){
            throw new RuntimeException("-------参数TenderUserId不得为空！------");
        }

        if(StringUtils.isBlank(bean.getPlanStartDate())){
            throw new RuntimeException("-------参数PlanStartDate不得为空！------");
        }

        if(StringUtils.isBlank(bean.getPlanEndDate())){
            throw new RuntimeException("-------参数PlanEndDate不得为空！------");
        }

        UsersInfo userInfo=this.getUsersInfoByUserId(bean.getTenderUserId());
        Users users = this.getUsers(bean.getTenderUserId());

        String tenderTrueName = null;
        String tenderIdCard = null;
        tenderTrueName = userInfo.getTruename();
        tenderIdCard = userInfo.getIdcard();
        if(users.getUserType() == 1){
            CorpOpenAccountRecord info = getCorpOpenAccountInfoByUserID(bean.getTenderUserId());
            tenderTrueName = info.getBusiName();
            tenderIdCard = info.getBusiCode();
        }

        Map<String ,Object> params=new HashMap<String ,Object>();
        params.put("accedeOrderId", bean.getOrdid());
        params.put("userId", bean.getTenderUserId());
        UserHjhInvistDetailCustomize userHjhInvistDetailCustomize = this.selectUserHjhInvistDetail(params);
        bean.setOrdid(bean.getOrdid());
        bean.setTransType(FddGenerateContractConstant.PROTOCOL_TYPE_PLAN);
        bean.setTenderTrueName(tenderTrueName);
        bean.setIdCard(tenderIdCard);
        bean.setTenderUserId(bean.getTenderUserId());
        bean.setTenderUserName(users.getUsername());
        bean.setTenderAccountFMT(userHjhInvistDetailCustomize.getAccedeAccount());
        bean.setBorrowDate(userHjhInvistDetailCustomize.getPlanPeriod());//计划期限
        bean.setBorrowRate(userHjhInvistDetailCustomize.getPlanApr());//计划收益率
        bean.setSignDate(GetDate.getDataString(GetDate.date_sdf));//签署日期


        JSONObject paramter = new JSONObject();
        paramter.put("accedeOrderId", bean.getOrdid());//合同编号
        paramter.put("addTime",bean.getSignDate());//签署时间
        paramter.put("truename", bean.getTenderTrueName());//出借人真实姓名
        paramter.put("idcard",bean.getIdCard());//证件号码
//        paramter.put("username", bean.getTenderUserName());//出借人用户名
        paramter.put("accedeAccount", bean.getTenderAccountFMT());//出借人加入金额
        paramter.put("planPeriod",bean.getBorrowDate());//计划期限
        paramter.put("planApr", bean.getBorrowRate());//计划收益率
        paramter.put("countInterestTime",bean.getPlanStartDate());//计划生效日期
        paramter.put("quitTime",bean.getPlanEndDate());//计划到期日期
        //协议修改
        /*
        paramter.put("shouyifutou",FddGenerateContractConstant.SHOUYI_FUTOU);//收益处理方式
        paramter.put("shouldPayTotal", bean.getTenderInterestFmt());//计划本金及预期收益
        */

        String paramStr = paramter.toJSONString();
        bean.setContractName(FddGenerateContractConstant.CONTRACT_DOC_TITLE_PLAN);

        boolean isSign = isCreatContract(bean.getOrdid());
        if (isSign){//单独走签署接口
            updateSignContract(bean);
        }else {
            FddTempletExample example = new FddTempletExample();
            FddTempletExample.Criteria criteria = example.createCriteria();
            criteria.andProtocolTypeEqualTo(FddGenerateContractConstant.PROTOCOL_TYPE_PLAN);
            criteria.andIsActiveEqualTo(1);
            criteria.andCaFlagEqualTo(1);
            List<FddTemplet> fddTemplets = this.fddTempletMapper.selectByExample(example);
            if (fddTemplets != null && fddTemplets.size() == 1) {
                FddTemplet fddTemplet = fddTemplets.get(0);
                String templetId = fddTemplet.getTempletId();
                DzqzCallBean callBean = new DzqzCallBean();
                callBean.setParameter_map(paramStr);
                callBean.setTemplate_id(templetId);
                callBean.setTxCode(DzqzConstant.FDD_GENERATECONTRACT);
                callBean.setContract_id(bean.getOrdid());
                callBean.setDoc_title(FddGenerateContractConstant.CONTRACT_DOC_TITLE_PLAN);
                callBean.setDynamic_tables(null);
                callBean.setUserId(bean.getTenderUserId());
                callBean.setFont_size("12");
                callBean.setFont_type("1");
                DzqzCallBean dzqzCallBean = DzqzCallUtil.callApiBg(callBean);
                String result = dzqzCallBean.getResult();
                String code = dzqzCallBean.getCode();
                dzqzCallBean.setTemplate_id(templetId);
                String msg = dzqzCallBean.getMsg();
                if (("success".equals(result) && FddGenerateContractConstant.FDD_RETURN_CODE_1000.equals(code))
                        || ("对不起，合同编号重复！".equals(msg) && FddGenerateContractConstant.FDD_RETURN_CODE_2002.equals(code))) {
                    //协议生成成功，开始签署并进行脱敏处理
                    updateSaveDownUrl(dzqzCallBean, bean);
                    updateSignContract(bean);
                }else {
                    log.info("--------------开始生成居间出借协议返回错误，订单号：" + bean.getOrdid() + "错误码：" + code + ",错误描述：" + msg);
                }
            } else {
                log.info("--------------开始生成居间出借协议异常，无法匹配模板----订单号：" + bean.getOrdid());
            }
        }
    }



    @Override
    public void creditGenerateContract(FddGenerateContractBean bean) throws Exception {
        JSONObject paramter = new JSONObject();
        // 承接订单号
        String assignOrderId = bean.getAssignOrderId();
        if (StringUtils.isBlank(assignOrderId)) {
            log.info("债转承接订单号为空");
            throw new Exception("债转承接订单号为空");
        }
        // 承接人用户ID
        Integer tenderUserId = bean.getTenderUserId();
        if (tenderUserId == null || tenderUserId == 0) {
            throw new Exception("承接用户ID为空");
        }
        // 标的编号
        String borrowNid = bean.getBorrowNid();
        if (StringUtils.isBlank(borrowNid)) {
            throw new Exception("借款编号为空");
        }
        // 原始出借订单号
        String creditTenderNid = bean.getCreditTenderNid();
        if (StringUtils.isBlank(creditTenderNid)) {
            throw new Exception("原始出借订单号");
        }
        // 债转编号
        String creditNid = bean.getCreditNid();
        if (StringUtils.isBlank(creditNid)) {
            throw new Exception("债转编号为空");
        }

        // 获取债转出借信息
        CreditTenderExample creditTenderExample = new CreditTenderExample();
        CreditTenderExample.Criteria cra = creditTenderExample.createCriteria();
        cra.andAssignNidEqualTo(assignOrderId).andBidNidEqualTo(borrowNid).andCreditNidEqualTo(creditNid).andCreditTenderNidEqualTo(creditTenderNid);
        List<CreditTender> creditTenderList = this.creditTenderMapper.selectByExample(creditTenderExample);
        if (creditTenderList != null && creditTenderList.size() > 0) {

            CreditTender creditTender = creditTenderList.get(0);
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("creditNid", creditTender.getCreditNid());
            params.put("assignNid", creditTender.getAssignNid());
            List<TenderToCreditDetailCustomize> tenderToCreditDetailList = tenderCreditCustomizeMapper.selectWebCreditTenderDetailForContract(params);
            if (tenderToCreditDetailList != null && tenderToCreditDetailList.size() > 0) {
                if (tenderToCreditDetailList.get(0).getCreditRepayEndTime() != null) {
                    tenderToCreditDetailList.get(0).setCreditRepayEndTime(GetDate.getDateMyTimeInMillis(Integer.parseInt(tenderToCreditDetailList.get(0).getCreditRepayEndTime())));
                }
                if (tenderToCreditDetailList.get(0).getCreditTime() != null) {
                    try {
                        tenderToCreditDetailList.get(0).setCreditTime(GetDate.formatDate(GetDate.parseDate(tenderToCreditDetailList.get(0).getCreditTime(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }



                // 债转期限
                paramter.put("creditTerm", tenderToCreditDetailList.get(0).getCreditTerm());
                // 债转时间
                paramter.put("creditTime", tenderToCreditDetailList.get(0).getCreditTime());
                // 转让债权本金
                paramter.put("assignCapital", creditTender.getAssignCapital().toString());
                //转让价款
                paramter.put("assignPay", creditTender.getAssignPay().toString());
                //转让服务费
                paramter.put("coverCharge", creditTender.getCreditFee().toString());
                // 债转最后还款日
//                paramter.put("creditRepayEndTime", tenderToCreditDetailList.get(0).getCreditRepayEndTime());
                // 签署时间
                paramter.put("addTime", tenderToCreditDetailList.get(0).getSignTime());
            }


            // 获取借款标的信息
            Borrow borrow = this.getBorrowByNid(borrowNid);
            if (borrow == null) {
                log.info("根据标的编号获取标的信息为空,标的编号:" + borrowNid + "].");
                throw new Exception("根据标的编号获取标的信息为空,标的编号:" + borrowNid + "].");
            }

            // 借款人用户ID
            Integer borrowUserId = borrow.getUserId();

            // 获取债转信息
            BorrowCreditExample borrowCreditExample = new BorrowCreditExample();
            BorrowCreditExample.Criteria borrowCreditCra = borrowCreditExample.createCriteria();
            borrowCreditCra.andCreditNidEqualTo(Integer.parseInt(creditNid)).andBidNidEqualTo(borrowNid).andTenderNidEqualTo(creditTenderNid);
            List<BorrowCredit> borrowCredit = this.borrowCreditMapper.selectByExample(borrowCreditExample);

            if (borrowCredit == null || borrowCredit.size() != 1) {
                throw new Exception("根据债转编号查询债转信息失败,债转编号:[" + creditNid + "].");
            }
            // 出让人用户ID
            Integer creditUserId = creditTender.getCreditUserId();

            // 获取承接人平台信息
            Users tenderUser = this.getUsersByUserId(tenderUserId);
            if (tenderUser == null) {
                throw new Exception("根据用户ID查询用户信息失败, 承接人用户ID:[" + tenderUserId + "]");
            }
            // 获取承接人身份信息
            UsersInfo tenderUserInfo = this.getUsersInfoByUserId(tenderUserId);
            if (tenderUserInfo == null) {
                throw new Exception("根据用户ID查询用户详情信息失败, 承接人用户ID:[" + tenderUserId + "]");
            }
            // 获取融资方平台信息
            Users borrowUsers = this.getUsers(borrowUserId);
            if (borrowUsers == null) {
                throw new Exception("根据借款人用户ID,查询借款人用户信息失败,借款人用户ID:[" + borrowUserId + "].");
            }
            // 获取借款人信息
            UsersInfo borrowUsersInfo = this.getUsersInfoByUserId(borrowUserId);
            if (borrowUsersInfo == null) {
                throw new Exception("根据借款人用户ID,查询借款人用户详情信息失败,借款人用户ID:[" + borrowUserId + "].");
            }
            // 获取出让人平台信息
            Users creditUser = this.getUsers(creditUserId);
            if (creditUser == null) {
                throw new Exception("根据出让人用户ID,查询出让人用户信息失败,出让人用户ID:[" + creditUserId + "].");
            }
            // 获取债转人身份信息
            UsersInfo creditUsersInfo = this.getUsersInfoByUserId(creditUserId);
            if (creditUsersInfo == null) {
                throw new Exception("根据出让人用户ID,查询出让人用户信息详情失败,出让人用户ID:[" + creditUserId + "].");
            }

            if (borrow.getReverifyTime() != null) {
                borrow.setReverifyTime(GetDate.getDateMyTimeInMillis(Integer.parseInt(borrow.getReverifyTime())));
            }
            if (borrow.getRepayLastTime() != null) {
                borrow.setRepayLastTime(GetDate.getDateMyTimeInMillis(Integer.parseInt(borrow.getRepayLastTime())));
            }

            /** 标的基本数据 */
            String borrowStyle = borrow.getBorrowStyle();// 还款方式
            Integer borrowPeriod = borrow.getBorrowPeriod();
            String borrowDate = "";
            Object creditTerm = paramter.get("creditTerm");
            // 是否月标(true:月标, false:天标)
            boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
                    || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle) || CustomConstants.BORROW_STYLE_END.equals(borrowStyle);
            if(isMonth){//月表
                borrowDate = borrowPeriod + "个月";
            }else{
                borrowDate = borrowPeriod + "天";
            }
            creditTerm = creditTerm + "天";

            paramter.put("creditTerm",creditTerm);

            // 标的编号
            paramter.put("borrowNid", borrow.getBorrowNid());

            String repayLastTime = borrow.getRepayLastTime();
            //到期日
            paramter.put("borrowDueDay", repayLastTime);
            //编号
            paramter.put("NID", assignOrderId);
            //借款本金总额
            paramter.put("borrowAccount", borrow.getAccount().toString());
            // 借款利率
            paramter.put("borrowApr", borrow.getBorrowApr() + "%");
            // 还款方式
            paramter.put("borrowStyle", this.getBorrowStyle(borrow.getBorrowStyle()));
            // 借款期限
            paramter.put("borrowPeriod", borrowDate);

            // 出让人相关 start
            // 出让人用户真实姓名
            paramter.put("CreditTruename", creditUsersInfo.getTruename());
            // 出让人身份证号
            paramter.put("CreditIdcard", creditUsersInfo.getIdcard());
            // 出让人用户名
//            paramter.put("CreditUsername", creditUser.getUsername());
            // 出让人相关 end

            String tenderTrueName = null;
            String tenderIdCard = null;
            tenderTrueName = tenderUserInfo.getTruename();
            tenderIdCard = tenderUserInfo.getIdcard();
            if(tenderUser.getUserType() == 1){
                CorpOpenAccountRecord info = getCorpOpenAccountInfoByUserID(bean.getTenderUserId());
                tenderTrueName = info.getBusiName();
                tenderIdCard = info.getBusiCode();
            }

            // 承接人用户 start
            // 承接人真实姓名
            paramter.put("truename", tenderTrueName);
            // 承接人身份证号
            paramter.put("idcard", tenderIdCard);
            // 承接人用户名
//            paramter.put("username", tenderUser.getUsername());
            // 承接人用户 end

            // 借款人用户名
            paramter.put("BorrowUsername",borrowUsers.getUsername());



            String paramStr = paramter.toJSONString();

            bean.setTenderUserName(tenderUser.getUsername());
            bean.setOrdid(assignOrderId);
            bean.setTenderType(1);
            bean.setCreditUserID(creditUserId);
            bean.setContractName(FddGenerateContractConstant.CONTRACT_DOC_TITLE_CREDIT);

            boolean isSign = isCreatContract(assignOrderId);
            if (isSign){//单独走签署接口
                updateSignContract(bean);
            }else {
                FddTempletExample example = new FddTempletExample();
                FddTempletExample.Criteria criteria = example.createCriteria();
                criteria.andProtocolTypeEqualTo(FddGenerateContractConstant.PROTOCOL_TYPE_CREDIT);
                criteria.andIsActiveEqualTo(1);
                criteria.andCaFlagEqualTo(1);
                List<FddTemplet> fddTemplets = this.fddTempletMapper.selectByExample(example);
                if (fddTemplets != null && fddTemplets.size() == 1) {
                    FddTemplet fddTemplet = fddTemplets.get(0);
                    String templetId = fddTemplet.getTempletId();
                    DzqzCallBean callBean = new DzqzCallBean();
                    callBean.setParameter_map(paramStr);
                    callBean.setTemplate_id(templetId);
                    callBean.setTxCode(DzqzConstant.FDD_GENERATECONTRACT);
                    callBean.setContract_id(bean.getOrdid());
                    callBean.setDoc_title(FddGenerateContractConstant.CONTRACT_DOC_TITLE_CREDIT);
                    callBean.setDynamic_tables(null);
                    callBean.setUserId(tenderUserId);
                    callBean.setFont_size("12");
                    callBean.setFont_type("1");
                    DzqzCallBean dzqzCallBean = DzqzCallUtil.callApiBg(callBean);
                    String result = dzqzCallBean.getResult();
                    String code = dzqzCallBean.getCode();
                    dzqzCallBean.setTemplate_id(templetId);
                    String msg = dzqzCallBean.getMsg();
                    if (("success".equals(result) && FddGenerateContractConstant.FDD_RETURN_CODE_1000.equals(code))
                            || ("对不起，合同编号重复！".equals(msg) && FddGenerateContractConstant.FDD_RETURN_CODE_2002.equals(code))) {
                        //协议生成成功，开始签署并进行脱敏处理
                        updateSaveDownUrl(dzqzCallBean, bean);
                        updateSignContract(bean);
                    } else {
                        log.info("--------------开始生成债转出借协议返回错误，订单号：" + bean.getOrdid() + "错误码：" + code + ",错误描述：" + msg);
                    }
                } else {
                    log.info("--------------开始生成债转出借协议异常，无法匹配模板----订单号：" + bean.getOrdid());
                }
            }
        }

}
    /**
     * 垫付债转协议
     */
    @Override
    public void creditGenerateContractApply(FddGenerateContractBean bean) throws Exception {
        JSONObject paramter = new JSONObject();
        paramter = bean.getParamter();
            String paramStr = paramter.toJSONString();
            //垫付协议发大大Contract_id 
            String contract_id = bean.getTeString()+"-"+bean.getRepayPeriod()+"-"+bean.getOrdid();
            bean.setOrdid(contract_id);
            bean.setContractName(FddGenerateContractConstant.CONTRACT_DOC_TITLE_CREDIT);
            //bean.setTransType(3);
            boolean isSign = isCreatContract(contract_id);
            if (isSign){//单独走签署接口
                updateSignContract(bean);
            }else {
                FddTempletExample example = new FddTempletExample();
                FddTempletExample.Criteria criteria = example.createCriteria();
                criteria.andProtocolTypeEqualTo(FddGenerateContractConstant.PROTOCOL_TYPE_CREDIT);
                criteria.andIsActiveEqualTo(1);
                criteria.andCaFlagEqualTo(1);
                List<FddTemplet> fddTemplets = this.fddTempletMapper.selectByExample(example);
                if (fddTemplets != null && fddTemplets.size() >0) {
                    FddTemplet fddTemplet = fddTemplets.get(0);
                    String templetId = fddTemplet.getTempletId();
                    DzqzCallBean callBean = new DzqzCallBean();
                    callBean.setParameter_map(paramStr);
                    callBean.setTemplate_id(templetId);
                    callBean.setTxCode(DzqzConstant.FDD_GENERATECONTRACT);
                    callBean.setContract_id(bean.getOrdid());
                    callBean.setDoc_title(FddGenerateContractConstant.CONTRACT_DOC_TITLE_CREDIT);
                    callBean.setDynamic_tables(null);
                    callBean.setUserId(bean.getTenderUserId());
                    callBean.setFont_size("12");
                    callBean.setFont_type("1");
                    DzqzCallBean dzqzCallBean = DzqzCallUtil.callApiBg(callBean);
                    String result = dzqzCallBean.getResult();
                    String code = dzqzCallBean.getCode();
                    dzqzCallBean.setTemplate_id(templetId);
                    String msg = dzqzCallBean.getMsg();
                    if (("success".equals(result) && FddGenerateContractConstant.FDD_RETURN_CODE_1000.equals(code))
                            || ("对不起，合同编号重复！".equals(msg) && FddGenerateContractConstant.FDD_RETURN_CODE_2002.equals(code))) {
                        //协议生成成功，开始签署并进行脱敏处理
                        updateSaveDownUrl(dzqzCallBean, bean);
                        updateSignContract(bean);
                    } else {
                        log.info("--------------开始生成债转出借协议返回错误，订单号：" + bean.getOrdid() + "错误码：" + code + ",错误描述：" + msg);
                    }
                } else {
                    log.info("--------------开始生成债转出借协议异常，无法匹配模板----订单号：" + bean.getOrdid());
                }
            }
        }


    /**
     * 生成计划债转协议
     * @param bean
     */
    @Override
    public void planCreditGenerateContract(FddGenerateContractBean bean) {
        log.info("开始生成计划债转协议---------------------承接订单号:" + bean.getAssignNid());
        try {
            //计划债转是在事务内提交，可能获取不到数据，需要暂时休眠1秒，待数据提交
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Map<String, Object> resultMap = null;
        List<HjhDebtCreditTender> hjhCreditTenderList = this.repayService.selectHjhCreditTenderListByassignOrderId(bean.getAssignNid());//hyjf_hjh_debt_credit_tender
//        Integer tenderUserid = null;
        String signTime = null;
        JSONObject paramter = new JSONObject();
        if(hjhCreditTenderList!=null && hjhCreditTenderList.size()>0) {
            HjhDebtCreditTender hjhCreditTender = hjhCreditTenderList.get(0);
            paramter.put("assignCapital",hjhCreditTender.getAssignCapital().toString());
            paramter.put("assignPay",hjhCreditTender.getAssignPay().toString());
            //转让服务费
            paramter.put("coverCharge", hjhCreditTender.getAssignServiceFee().toString());
            //调用下载计划债转协议的方法
            CreditAssignedBean tenderCreditAssignedBean = new CreditAssignedBean();

            tenderCreditAssignedBean.setBidNid(hjhCreditTender.getBorrowNid());// 标号
            tenderCreditAssignedBean.setCreditNid(hjhCreditTender.getCreditNid());// 债转编号
            tenderCreditAssignedBean.setCreditTenderNid(hjhCreditTender.getInvestOrderId());//原始出借订单号
            tenderCreditAssignedBean.setAssignNid(hjhCreditTender.getAssignOrderId());//债转后的新的"出借"订单号
            tenderCreditAssignedBean.setCurrentUserId(hjhCreditTender.getUserId());//承接人id
//            tenderUserid = hjhCreditTender.getUserId();
            Integer createTime = hjhCreditTender.getCreateTime();
            signTime = GetDate.getDateMyTimeInMillisYYYYMMDD(createTime);
            // 模板参数对象(查新表)
            resultMap = this.selectHJHUserCreditContract(tenderCreditAssignedBean);
        }else{
            log.error("开始生成计划债转协议失败-----------汇计划债转出借表中无该记录----------承接订单号:" + bean.getAssignNid());
        }

        Borrow borrow = (Borrow) resultMap.get("borrow");
        UsersInfo creditUsersInfo = (UsersInfo) resultMap.get("usersInfoCredit");
        Users creditUser = (Users) resultMap.get("usersCredit");
        UsersInfo tenderUserInfo = (UsersInfo) resultMap.get("usersInfo");
        Users tenderUser = (Users) resultMap.get("users");
        HjhDebtCredit borrowCredit = (HjhDebtCredit) resultMap.get("borrowCredit");
        Users borrowUsers = getUsersByUserId(borrow.getUserId());

        /** 标的基本数据 */
        String borrowStyle = borrow.getBorrowStyle();// 还款方式
        Integer borrowPeriod = borrow.getBorrowPeriod();
        String borrowDate = "";

        String creditTerm = borrowCredit.getCreditTerm().toString();
        // 是否月标(true:月标, false:天标)
        boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
                || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle) || CustomConstants.BORROW_STYLE_END.equals(borrowStyle);
        if(isMonth){//月表
            borrowDate = borrowPeriod + "个月";
        }else{
            borrowDate = borrowPeriod + "天";
        }
        creditTerm = creditTerm + "天";
        //转让剩余期限
        paramter.put("creditTerm", creditTerm);
        //转让日期
        paramter.put("creditTime", GetDate.getDateMyTimeInMillisYYYYMMDD(borrowCredit.getCreateTime()));

        // 标的编号
        paramter.put("borrowNid", borrow.getBorrowNid());
        String repayLastTime = borrow.getRepayLastTime();
        //到期日
        paramter.put("borrowDueDay", repayLastTime);
        // 编号
        paramter.put("NID", bean.getAssignNid());
        //签署日期
        paramter.put("addTime", signTime);

        //借款本金总额
        paramter.put("borrowAccount", borrow.getAccount().toString());
        // 借款利率
        paramter.put("borrowApr", borrow.getBorrowApr() + "%");
        // 还款方式
        paramter.put("borrowStyle", this.getBorrowStyle(borrow.getBorrowStyle()));
        // 借款期限
        paramter.put("borrowPeriod", borrowDate);


        // 出让人相关 start
        // 出让人用户真实姓名
        paramter.put("CreditTruename", creditUsersInfo.getTruename());
        // 出让人身份证号
        paramter.put("CreditIdcard", creditUsersInfo.getIdcard());
        // 出让人用户名
//        paramter.put("CreditUsername", creditUser.getUsername());
        // 出让人相关 end

        String tenderTrueName = null;
        String tenderIdCard = null;
        tenderTrueName = tenderUserInfo.getTruename();
        tenderIdCard = tenderUserInfo.getIdcard();
        if(tenderUser.getUserType() == 1){
            CorpOpenAccountRecord info = getCorpOpenAccountInfoByUserID(bean.getTenderUserId());
            tenderTrueName = info.getBusiName();
            tenderIdCard = info.getBusiCode();
        }

        // 承接人用户 start
        // 承接人真实姓名
        paramter.put("truename", tenderUserInfo.getTruename());
        // 承接人身份证号
        paramter.put("idcard", tenderUserInfo.getIdcard());
        // 承接人用户名
//        paramter.put("username", tenderUser.getUsername());
        // 承接人用户 end

        // 借款人用户名
        paramter.put("BorrowUsername",borrowUsers.getUsername());

        String paramStr = paramter.toJSONString();

        bean.setTenderUserId(tenderUser.getUserId());
        bean.setTenderUserName(tenderUser.getUsername());
        bean.setOrdid(bean.getAssignNid());
        bean.setTenderType(1);
        bean.setCreditUserID(creditUser.getUserId());
        bean.setContractName(FddGenerateContractConstant.CONTRACT_DOC_TITLE_CREDIT);

        boolean isSign = isCreatContract(bean.getAssignNid());
        if (isSign){//单独走签署接口
            updateSignContract(bean);
        }else {

            FddTempletExample example = new FddTempletExample();
            FddTempletExample.Criteria criteria = example.createCriteria();
            criteria.andProtocolTypeEqualTo(FddGenerateContractConstant.PROTOCOL_TYPE_CREDIT);
            criteria.andIsActiveEqualTo(1);
            criteria.andCaFlagEqualTo(1);
            List<FddTemplet> fddTemplets = this.fddTempletMapper.selectByExample(example);
            if (fddTemplets != null && fddTemplets.size() == 1) {
                FddTemplet fddTemplet = fddTemplets.get(0);
                String templetId = fddTemplet.getTempletId();
                DzqzCallBean callBean = new DzqzCallBean();
                callBean.setParameter_map(paramStr);
                callBean.setTemplate_id(templetId);
                callBean.setTxCode(DzqzConstant.FDD_GENERATECONTRACT);
                callBean.setContract_id(bean.getOrdid());
                callBean.setDoc_title(FddGenerateContractConstant.CONTRACT_DOC_TITLE_CREDIT);
                callBean.setDynamic_tables(null);
                callBean.setFont_size("12");
                callBean.setFont_type("1");
                callBean.setUserId(tenderUser.getUserId());
                DzqzCallBean dzqzCallBean = DzqzCallUtil.callApiBg(callBean);
                String result = dzqzCallBean.getResult();
                String code = dzqzCallBean.getCode();
                dzqzCallBean.setTemplate_id(templetId);
                String msg = dzqzCallBean.getMsg();
                if (("success".equals(result) && FddGenerateContractConstant.FDD_RETURN_CODE_1000.equals(code))
                        || ("对不起，合同编号重复！".equals(msg) && FddGenerateContractConstant.FDD_RETURN_CODE_2002.equals(code))) {
                    //协议生成成功，开始签署并进行脱敏处理
                    updateSaveDownUrl(dzqzCallBean, bean);
                    updateSignContract(bean);
                } else {
                    log.info("--------------开始生成计划债转出借协议返回错误，订单号：" + bean.getAssignNid() + "错误码：" + code + ",错误描述：" + msg);
                }
            } else {
                log.info("--------------开始生成计划债转出借协议异常，无法匹配模板----订单号：" + bean.getAssignNid());
            }
        }
    }
    
    /**
     * 生成计划垫付债转协议
     * @param bean
     */
    @Override
    public void planCreditGenerateContractApply(FddGenerateContractBean bean) {
        log.info("开始生成生成计划垫付债转协议---------------------承接订单号:" + bean.getOrdid());
        try {
            //计划债转是在事务内提交，可能获取不到数据，需要暂时休眠1秒，待数据提交
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
       // bean.setTransType(3);
        //垫付协议发大大Contract_id 
        //GetDate.getNowTime10()測試
        String contract_id = bean.getTeString()+"-"+bean.getRepayPeriod()+"-"+bean.getOrdid();
        bean.setOrdid(contract_id);
        JSONObject paramter = new JSONObject();
        paramter = bean.getParamter();
        String paramStr = paramter.toJSONString();
        bean.setContractName(FddGenerateContractConstant.CONTRACT_DOC_TITLE_CREDIT);
        boolean isSign = isCreatContract(contract_id);
        if (isSign){//单独走签署接口
            updateSignContract(bean);
        }else {
            FddTempletExample example = new FddTempletExample();
            FddTempletExample.Criteria criteria = example.createCriteria();
            criteria.andProtocolTypeEqualTo(FddGenerateContractConstant.PROTOCOL_TYPE_CREDIT);
            criteria.andIsActiveEqualTo(1);
            criteria.andCaFlagEqualTo(1);
            List<FddTemplet> fddTemplets = this.fddTempletMapper.selectByExample(example);
            if (fddTemplets != null && fddTemplets.size() >0) {
                FddTemplet fddTemplet = fddTemplets.get(0);
                String templetId = fddTemplet.getTempletId();
                DzqzCallBean callBean = new DzqzCallBean();
                callBean.setParameter_map(paramStr);
                callBean.setTemplate_id(templetId);
                callBean.setTxCode(DzqzConstant.FDD_GENERATECONTRACT);
                callBean.setContract_id(bean.getOrdid());
                callBean.setDoc_title(FddGenerateContractConstant.CONTRACT_DOC_TITLE_CREDIT);
                callBean.setDynamic_tables(null);
                callBean.setFont_size("12");
                callBean.setFont_type("1");
                callBean.setUserId(bean.getTenderUserId());
                DzqzCallBean dzqzCallBean = DzqzCallUtil.callApiBg(callBean);
                String result = dzqzCallBean.getResult();
                String code = dzqzCallBean.getCode();
                dzqzCallBean.setTemplate_id(templetId);
                String msg = dzqzCallBean.getMsg();
                if (("success".equals(result) && FddGenerateContractConstant.FDD_RETURN_CODE_1000.equals(code))
                        || ("对不起，合同编号重复！".equals(msg) && FddGenerateContractConstant.FDD_RETURN_CODE_2002.equals(code))) {
                    //协议生成成功，开始签署并进行脱敏处理
                    updateSaveDownUrl(dzqzCallBean, bean);
                    updateSignContract(bean);
                } else {
                    log.info("--------------开始生成计划债转出借协议返回错误，订单号：" + bean.getAssignOrderId() + "错误码：" + code + ",错误描述：" + msg);
                }
            } else {
                log.info("--------------开始生成计划债转出借协议异常，无法匹配模板----订单号：" + bean.getAssignOrderId());
            }
        }
    }

    /**
     * 用户中心债转被出借的协议(汇计划)
     *
     * @return
     */
    private Map<String, Object> selectHJHUserCreditContract(CreditAssignedBean tenderCreditAssignedBean) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        // 获取债转出借信息
        HjhDebtCreditTenderExample creditTenderExample = new HjhDebtCreditTenderExample();
        HjhDebtCreditTenderExample.Criteria creditTenderCra = creditTenderExample.createCriteria();
        creditTenderCra.andBorrowNidEqualTo(tenderCreditAssignedBean.getBidNid());
        creditTenderCra.andCreditNidEqualTo(tenderCreditAssignedBean.getCreditNid());
        creditTenderCra.andInvestOrderIdEqualTo(tenderCreditAssignedBean.getCreditTenderNid());
        creditTenderCra.andAssignOrderIdEqualTo(tenderCreditAssignedBean.getAssignNid());

        // 当前用户的id
        Integer currentUserId = tenderCreditAssignedBean.getCurrentUserId();

        //查询 hyjf_hjh_debt_credit_tender 表
        List<HjhDebtCreditTender> creditTenderList = this.hjhDebtCreditTenderMapper.selectByExample(creditTenderExample);
        if (creditTenderList != null && creditTenderList.size() > 0) {
            HjhDebtCreditTender creditTender = creditTenderList.get(0);
            Map<String, Object> params = new HashMap<String, Object>();

            params.put("creditNid", creditTender.getCreditNid());//取得 hyjf_hjh_debt_credit_tender 表的债转编号
            params.put("assignOrderId", creditTender.getAssignOrderId());//取得 hyjf_hjh_debt_credit_tender 表的债转编号

            //查看债转详情
            List<TenderToCreditDetailCustomize> tenderToCreditDetailList = tenderCreditCustomizeMapper.selectHJHWebCreditTenderDetail(params);//查汇计划债转详情

            if (tenderToCreditDetailList != null && tenderToCreditDetailList.size() > 0) {
                if (tenderToCreditDetailList.get(0).getCreditRepayEndTime() != null) {
                    tenderToCreditDetailList.get(0).setCreditRepayEndTime(GetDate.getDateMyTimeInMillis(Integer.parseInt(tenderToCreditDetailList.get(0).getCreditRepayEndTime())));
                }
                if (tenderToCreditDetailList.get(0).getCreditTime() != null) {
                    try {
                        tenderToCreditDetailList.get(0).setCreditTime(GetDate.formatDate(GetDate.parseDate(tenderToCreditDetailList.get(0).getCreditTime(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                resultMap.put("tenderToCreditDetail", tenderToCreditDetailList.get(0));
            }


            // 获取借款标的信息
            BorrowExample borrowExample = new BorrowExample();
            BorrowExample.Criteria borrowCra = borrowExample.createCriteria();
            borrowCra.andBorrowNidEqualTo(creditTender.getBorrowNid());
            List<Borrow> borrow = this.borrowMapper.selectByExample(borrowExample);

            // 获取债转信息(新表)
            HjhDebtCreditExample borrowCreditExample = new HjhDebtCreditExample();
            HjhDebtCreditExample.Criteria borrowCreditCra = borrowCreditExample.createCriteria();
            borrowCreditCra.andBorrowNidEqualTo(tenderCreditAssignedBean.getBidNid());
            borrowCreditCra.andCreditNidEqualTo(tenderCreditAssignedBean.getCreditNid());
            borrowCreditCra.andInvestOrderIdEqualTo(tenderCreditAssignedBean.getCreditTenderNid());//??
            List<HjhDebtCredit> borrowCredit = this.hjhDebtCreditMapper.selectByExample(borrowCreditExample);


            // 获取承接人身份信息
            UsersInfoExample usersInfoExample = new UsersInfoExample();
            UsersInfoExample.Criteria usersInfoCra = usersInfoExample.createCriteria();
            usersInfoCra.andUserIdEqualTo(creditTender.getUserId());
            List<UsersInfo> usersInfo = this.usersInfoMapper.selectByExample(usersInfoExample);

            // 获取承接人平台信息
            UsersExample usersExample = new UsersExample();
            UsersExample.Criteria usersCra = usersExample.createCriteria();
            usersCra.andUserIdEqualTo(creditTender.getUserId());
            List<Users> users = this.usersMapper.selectByExample(usersExample);

            // 获取融资方平台信息
            UsersExample usersBorrowExample = new UsersExample();
            UsersExample.Criteria usersBorrowCra = usersBorrowExample.createCriteria();
            usersBorrowCra.andUserIdEqualTo(borrow.get(0).getUserId());
            List<Users> usersBorrow = this.usersMapper.selectByExample(usersBorrowExample);

            // 获取债转人身份信息
            UsersInfoExample usersInfoExampleCredit = new UsersInfoExample();
            UsersInfoExample.Criteria usersInfoCraCredit = usersInfoExampleCredit.createCriteria();
            usersInfoCraCredit.andUserIdEqualTo(creditTender.getCreditUserId());
            List<UsersInfo> usersInfoCredit = this.usersInfoMapper.selectByExample(usersInfoExampleCredit);

            // 获取债转人平台信息
            UsersExample usersExampleCredit = new UsersExample();
            UsersExample.Criteria usersCraCredit = usersExampleCredit.createCriteria();
            usersCraCredit.andUserIdEqualTo(creditTender.getCreditUserId());
            List<Users> usersCredit = this.usersMapper.selectByExample(usersExampleCredit);
            // 将int类型时间转成字符串
			/*creditTender.setCreateTime(Integer.valueOf(GetDate.times10toStrYYYYMMDD(creditTender.getCreateTime())));*/
            String createTime = GetDate.times10toStrYYYYMMDD(creditTender.getCreateTime());
            String addip = GetDate.getDateMyTimeInMillis(creditTender.getAssignRepayEndTime());// 借用ip字段存储最后还款时
            resultMap.put("createTime", createTime);//记得去JSP上替换 creditResult.data.creditTender.addip 字段，要新建一个JSP
            resultMap.put("addip", addip);//记得去JSP上替换 creditResult.data.creditTender.addip 字段，要新建一个JSP
            resultMap.put("creditTender", creditTender);

            if (borrow != null && borrow.size() > 0) {
                if (borrow.get(0).getReverifyTime() != null) {
                    borrow.get(0).setReverifyTime(GetDate.getDateMyTimeInMillis(Integer.parseInt(borrow.get(0).getReverifyTime())));
                }
                if (borrow.get(0).getRepayLastTime() != null) {
                    borrow.get(0).setRepayLastTime(GetDate.getDateMyTimeInMillis(Integer.parseInt(borrow.get(0).getRepayLastTime())));
                }
                resultMap.put("borrow", borrow.get(0));
                // 获取借款人信息
                UsersInfoExample usersInfoExampleBorrow = new UsersInfoExample();
                UsersInfoExample.Criteria usersInfoCraBorrow = usersInfoExampleBorrow.createCriteria();
                usersInfoCraBorrow.andUserIdEqualTo(borrow.get(0).getUserId());
                List<UsersInfo> usersInfoBorrow = this.usersInfoMapper.selectByExample(usersInfoExampleBorrow);
                if (usersInfoBorrow != null && usersInfoBorrow.size() > 0) {
                    resultMap.put("usersInfoBorrow", usersInfoBorrow.get(0));
                }
            }

            if (borrowCredit != null && borrowCredit.size() > 0) {
                resultMap.put("borrowCredit", borrowCredit.get(0));
            }


            if (usersInfo != null && usersInfo.size() > 0) {
                resultMap.put("usersInfo", usersInfo.get(0));
            }


            if (usersBorrow != null && usersBorrow.size() > 0) {

                resultMap.put("usersBorrow", usersBorrow.get(0));
            }

            if (users != null && users.size() > 0) {
                resultMap.put("users", users.get(0));
            }

            if (usersCredit != null && usersCredit.size() > 0) {
                resultMap.put("usersCredit", usersCredit.get(0));
            }

            if (usersInfoCredit != null && usersInfoCredit.size() > 0) {

                resultMap.put("usersInfoCredit", usersInfoCredit.get(0));
            }

            String phpWebHost = PropUtils.getSystem("hyjf.web.host.php");
            if (StringUtils.isNotEmpty(phpWebHost)) {
                resultMap.put("phpWebHost", phpWebHost);
            } else {
                resultMap.put("phpWebHost", "http://site.hyjf.com");
            }
        }
        return resultMap;
    }

    /**
     *  自动签署结果异步返回处理
     *
     * @param bean
     */
    @Override
    public void updateAutoSignData(DzqzCallBean bean) throws Exception{

        //协议类型
        Integer transType = bean.getTransType();

        //居间服务协议签署处理
        //合同编号
        String contract_id = bean.getContract_id();
        log.info("--------------------合同编号：" + contract_id + "，开始处理自动签署异步处理-------");
        //出借人
        Integer userId = null;

        String borrowNid = null;

        //机构编号
        String instCode = null;

        // 出让人用户ID
        Integer creditUserId = null;
        // 借款人CA认证客户编号
        String borrowerCustomerID = null;

        if(FddGenerateContractConstant.PROTOCOL_TYPE_TENDER == transType){//居间服务协议
            BorrowTenderExample example = new BorrowTenderExample();
            example.createCriteria().andNidEqualTo(contract_id);
            List<BorrowTender> tenderList = this.borrowTenderMapper.selectByExample(example);
            if (tenderList != null && tenderList.size() > 0) {
                BorrowTender borrowTender = tenderList.get(0);
                userId = borrowTender.getUserId(); //出借人
                borrowNid = borrowTender.getBorrowNid();//标的号
                BorrowWithBLOBs borrow = this.getBorrowByNid(borrowNid);
                instCode = borrow.getInstCode();//机构编号
                //判断是否为企业借款
                boolean result = isCompanyUser(borrow);
                Object borrowMain = getValueByBorrowUser(borrow,result);
                //是否存在借款主体
                boolean isBorrowMain =true;
                if(borrowMain == null){//未查询到借款主体
                    isBorrowMain = false;
                }
                if (result){
                    // 获取CA认证客户编号
                    if(!isBorrowMain){//没有借款主体
                        Integer borrowUserId = borrow.getUserId();
                        borrowerCustomerID = getCustomerIDByUserID(borrowUserId);
                        if(StringUtils.isBlank(borrowerCustomerID)){
                            throw new Exception("企业借款获取CA认证客户编号失败,用户ID" + borrowUserId);
                        }
                    }else{
                        // 借款主体为企业借款
                        BorrowUsers  borrowUsers = (BorrowUsers) borrowMain;
                        if (borrowUsers == null){
                            throw  new Exception("根据标的编号查询借款主体为企业借款的相关信息失败,标的编号:["+borrowNid+"]");
                        }
                        borrowerCustomerID = this.getCACustomerID(borrowUsers);
                        if(StringUtils.isBlank(borrowerCustomerID)){
                            throw new Exception("企业借款获取CA认证客户编号失败,企业名称:["+borrowUsers.getUsername()+"],社会统一信用代码:["+borrowUsers.getSocialCreditCode()+"].");
                        }
                    }

                }else{
                    if(!isBorrowMain){//没有借款主体
                        Integer borrowUserId = borrow.getUserId();
                        borrowerCustomerID = getCustomerIDByUserID(borrowUserId);
                        if(StringUtils.isBlank(borrowerCustomerID)){
                            throw new Exception("个人借款获取CA认证客户编号失败,用户ID" + borrowUserId);
                        }
                    }else {
                        // 借款主体为个人借款
                        BorrowManinfo borrowManinfo = (BorrowManinfo) borrowMain;
                        if (borrowManinfo == null) {
                            throw new Exception("借款主体为个人借款时,获取个人借款信息失败,标的编号:[" + borrowNid + "].");
                        }
                        // 获取CA认证客户编号
                        borrowerCustomerID = this.getPersonCACustomerID(borrowManinfo);
                        if (StringUtils.isBlank(borrowerCustomerID)) {
                            throw new Exception("获取个人借款CA认证客户编号失败,姓名:[" + borrowManinfo.getName() + "],身份证号:[" + borrowManinfo.getCardNo() + "].");
                        }
                    }
                }
            }
        }else if(FddGenerateContractConstant.PROTOCOL_TYPE_PLAN == transType){//计划加入协议
            HjhAccedeExample example = new HjhAccedeExample();
            example.createCriteria().andAccedeOrderIdEqualTo(contract_id);
            List<HjhAccede> hjhAccedes = this.hjhAccedeMapper.selectByExample(example);
            if(hjhAccedes != null && hjhAccedes.size() > 0){
                HjhAccede hjhAccede = hjhAccedes.get(0);
                userId = hjhAccede.getUserId();
                instCode = hjhAccede.getPlanNid();
            }
        }else if(FddGenerateContractConstant.PROTOCOL_TYPE_CREDIT == transType){//债转服务协议
            CreditTenderExample creditTenderExample = new CreditTenderExample();
            CreditTenderExample.Criteria cra = creditTenderExample.createCriteria();
            cra.andAssignNidEqualTo(contract_id);
            List<CreditTender> creditTenderList = this.creditTenderMapper.selectByExample(creditTenderExample);
            if (creditTenderList != null && creditTenderList.size() > 0) {
                CreditTender creditTender = creditTenderList.get(0);
                userId = creditTender.getUserId();// 承接人
                borrowNid = creditTender.getBidNid();// 原标的号
                BorrowWithBLOBs borrow = this.getBorrowByNid(borrowNid);
                instCode = borrow.getInstCode();// 机构编号
                creditUserId = creditTender.getCreditUserId();// 出让人
                borrowerCustomerID = getCustomerIDByUserID(creditUserId);
            }
        }else if(FddGenerateContractConstant.FDD_TRANSTYPE_PLAN_CRIDET == transType){// 计划债转服务协议
            List<HjhDebtCreditTender> hjhCreditTenderList = this.repayService.selectHjhCreditTenderListByassignOrderId(contract_id);//hyjf_hjh_debt_credit_tender
            if(hjhCreditTenderList!=null && hjhCreditTenderList.size()>0) {
                HjhDebtCreditTender hjhCreditTender = hjhCreditTenderList.get(0);
                userId = hjhCreditTender.getUserId();// 承接人
                borrowNid = hjhCreditTender.getBorrowNid();// 标的号
                BorrowWithBLOBs borrow = this.getBorrowByNid(borrowNid);
                instCode = borrow.getInstCode();// 机构编号
                creditUserId = hjhCreditTender.getCreditUserId();// 出让人
                borrowerCustomerID = getCustomerIDByUserID(creditUserId);
            }
        }else if(FddGenerateContractConstant.FDD_TRANSTYPE_apply_CRIDET == transType || //垫付债转服务协议
                FddGenerateContractConstant.FDD_TRANSTYPE_APPLY_PLAN_CRIDET == transType){// 垫付计划债转服务协议
            ApplyAgreementInfoExample applyAgreementInfoExample = new ApplyAgreementInfoExample();
            ApplyAgreementInfoExample.Criteria cra = applyAgreementInfoExample.createCriteria();
            cra.andContractIdEqualTo(contract_id);
            List<ApplyAgreementInfo> applyAgreementInfoList = this.applyAgreementInfoMapper.selectByExample(applyAgreementInfoExample);
            if (applyAgreementInfoList != null && applyAgreementInfoList.size() > 0) {
                ApplyAgreementInfo applyAgreementInfo = applyAgreementInfoList.get(0);
                userId = Integer.valueOf(applyAgreementInfo.getUserId());// 承接人
                borrowNid = applyAgreementInfo.getBorrowNid();// 原标的号
                BorrowWithBLOBs borrow = this.getBorrowByNid(borrowNid);
                instCode = borrow.getInstCode();// 机构编号
                creditUserId = Integer.valueOf(applyAgreementInfo.getCreditUserId());// 出让人
                borrowerCustomerID = getCustomerIDByUserID(creditUserId);
            }
        }
        //通过三方的客户编号查询法大大签署结果，如果都成功的话，开始更新下载地址，并重置状态
        String tenderCustomerID = getCustomerIDByUserID(userId);
        if (tenderCustomerID == null) {
            throw new RuntimeException("用户：" + userId + ",未进行CA认证，无法获取客户编号！");
        }


        //平台客户编号
        String platFromCustomerId = PropUtils.getSystem(FddGenerateContractConstant.FDD_HYJF_CUSTOMERID);

        //查询出借人/承接人是否为企业用户
        boolean isTenderCompany = false;
        Users users = this.getUsers(userId);
        Integer userType = users.getUserType();
        if (userType == 1){
            isTenderCompany = true;
        }

        //查询出让人是否为企业用户
        boolean isCreditCompany = false;
        if(creditUserId != null){
            Users creditUsers = this.getUsers(creditUserId);
            Integer creditUserType = creditUsers.getUserType();
            if (creditUserType == 1){
                isCreditCompany = true;
            }
        }
        //调用查询接口并保存返回数据
        //查询出借人签署结果
        DzqzCallBean callBean = querySignResult(contract_id, tenderCustomerID, userId);
        if (callBean != null) {
            String sign_status = callBean.getSign_status();
            if (!"1".equals(sign_status)) {
                log.info("--------------------合同编号：" + contract_id + "，甲方签署失败-------");
                return;
            }
            log.info("--------------------合同编号：" + contract_id + "，甲方签署完成-------");
        } else {
            throw new RuntimeException("用户：" + userId + ",查询签署结果异常！");
        }

        if(FddGenerateContractConstant.PROTOCOL_TYPE_TENDER == transType || FddGenerateContractConstant.PROTOCOL_TYPE_CREDIT == transType
                || FddGenerateContractConstant.FDD_TRANSTYPE_PLAN_CRIDET == transType
                || FddGenerateContractConstant.FDD_TRANSTYPE_apply_CRIDET == transType
                || FddGenerateContractConstant.FDD_TRANSTYPE_APPLY_PLAN_CRIDET == transType){

            // 查询借款人签署结果
            DzqzCallBean borrowCallBean = querySignResult(contract_id, borrowerCustomerID, 123);
            if (borrowCallBean != null) {
                String sign_status = borrowCallBean.getSign_status();
                if (!"1".equals(sign_status)) {
                    log.info("--------------------合同编号：" + contract_id + "，乙方签署失败-------");
                    return;
                }
                log.info("--------------------合同编号：" + contract_id + "，乙方签署完成-------");
            } else {
                throw new RuntimeException("查询签署结果异常！");
            }
        }

        if ( FddGenerateContractConstant.PROTOCOL_TYPE_CREDIT == transType
                || FddGenerateContractConstant.FDD_TRANSTYPE_PLAN_CRIDET == transType
                || FddGenerateContractConstant.FDD_TRANSTYPE_apply_CRIDET == transType
                || FddGenerateContractConstant.FDD_TRANSTYPE_APPLY_PLAN_CRIDET == transType){
            log.info("--------------------合同编号：" + contract_id + "，签署完成，开始更新-------");
            updateSaveSignInfo(callBean, contract_id, borrowNid, transType, instCode,isTenderCompany,isCreditCompany);
        }else{
            //查询平台签署结果
            DzqzCallBean platerCallBean = querySignResult(contract_id, platFromCustomerId, userId);
            if (platerCallBean != null) {
                String sign_status = platerCallBean.getSign_status();
                if (!"1".equals(sign_status)) {
                    log.info("--------------------合同编号：" + contract_id + "，平台签署失败-------");
                    return;
                } else {
                    log.info("--------------------合同编号：" + contract_id + "，签署完成，开始更新-------");
                    //三方均签署成功，更新数据库
                    updateSaveSignInfo(platerCallBean, contract_id, borrowNid, transType, instCode,isTenderCompany,false);
                }
                log.info("--------------------合同编号：" + contract_id + "，平台签署完成-------");
            } else {
                throw new RuntimeException("接入平台,查询签署结果异常！");
            }
        }


    }

    /**
     *  判断借款主体是否为企业借款
     * @param borrow
     * @return true 企业 false 个人
     */
    private boolean isCompanyUser(BorrowWithBLOBs borrow) {

        String companyOrPersonal = borrow.getCompanyOrPersonal();
        if("1".equals(companyOrPersonal)){
            return true;
        }

        return false;
    }


    /**
     * 保存签署成功后的数据
     * @param platerCallBean
     * @param contract_id
     * @param borrowNid
     * @param transType
     * @param instCode
     * @param isCreditCompany
     */
    private void updateSaveSignInfo(DzqzCallBean platerCallBean, String contract_id, String borrowNid, Integer transType, String instCode, boolean isTenderCompany, boolean isCreditCompany) {

        String download_url = platerCallBean.getDownload_url();
        String viewpdf_url = platerCallBean.getViewpdf_url();


        TenderAgreementExample example = new TenderAgreementExample();
        example.createCriteria().andTenderNidEqualTo(contract_id).andStatusNotEqualTo(2);
        List<TenderAgreement> agreements = this.tenderAgreementMapper.selectByExample(example);
        if (agreements != null && agreements.size() > 0) {
            TenderAgreement tenderAgreement = agreements.get(0);
            tenderAgreement.setContractSignTime(GetDate.getNowTime10());
            tenderAgreement.setDownloadUrl(download_url);
            tenderAgreement.setViewpdfUrl(viewpdf_url);
            tenderAgreement.setUpdateTime(GetDate.getNowTime10());
            tenderAgreement.setUpdateUserId(tenderAgreement.getCreateUserId());
            tenderAgreement.setUpdateUserName(tenderAgreement.getUserName());
            tenderAgreement.setStatus(2);//签署成功
            int update = this.tenderAgreementMapper.updateByPrimaryKey(tenderAgreement);
            if (update > 0) {
                String savePath = null;
                String path = "/pdf_tem/";
                String ftpPath = null;
                if (FddGenerateContractConstant.PROTOCOL_TYPE_TENDER == transType || FddGenerateContractConstant.PROTOCOL_TYPE_CREDIT == transType
                        || FddGenerateContractConstant.FDD_TRANSTYPE_PLAN_CRIDET == transType
                        || FddGenerateContractConstant.FDD_TRANSTYPE_apply_CRIDET == transType
                        || FddGenerateContractConstant.FDD_TRANSTYPE_APPLY_PLAN_CRIDET == transType){
                    savePath = path + "pdf/" + borrowNid ;
                    ftpPath = "PDF/" + instCode + "/" + borrowNid + "/" + contract_id + "/";
                }else if(FddGenerateContractConstant.PROTOCOL_TYPE_PLAN == transType){
                    savePath = path + "pdf/" + instCode ;
                    ftpPath = "PDF/" + instCode  + "/" + contract_id + "/";
                }
                //下载协议并脱敏
                FddDessenesitizationBean bean = new FddDessenesitizationBean();
                bean.setAgrementID(tenderAgreement.getId().toString());
                bean.setSavePath(savePath);
                bean.setTransType(transType.toString());
                bean.setFtpPath(ftpPath);
                bean.setDownloadUrl(download_url);
                bean.setOrdid(contract_id);
                bean.setTenderCompany(isTenderCompany);
                bean.setCreditCompany(isCreditCompany);
                rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_DOWNDESSENESITIZATION_CONTRACT, JSONObject.toJSONString(bean));
            }
        }
    }




    /**
     * 查询签署结果
     *  @param contract_id      合同编号
     * @param tenderCustomerID 客户编号
     * @param userId
     */
    private DzqzCallBean querySignResult(String contract_id, String tenderCustomerID, Integer userId) {

        DzqzCallBean bean = new DzqzCallBean();
        bean.setTxCode(DzqzConstant.FDD_QUERY_SIGNSTATUS);
        bean.setCustomer_id(tenderCustomerID);
        bean.setContract_id(contract_id);
        bean.setUserId(userId);
        DzqzCallBean callBean = DzqzCallUtil.callApiBg(bean);
        return callBean;
    }

    /**
     * 通过userID获得CA认证的客户ID
     *
     * @param userId
     * @return
     */
    public String getCustomerIDByUserID(Integer userId) {
        if(userId == null){
            return null;
        }
        CertificateAuthorityExample cerExample = new CertificateAuthorityExample();
        cerExample.createCriteria().andUserIdEqualTo(userId).andCodeEqualTo("1000");
        String customerID = null;
        List<CertificateAuthority> authorities = this.certificateAuthorityMapper.selectByExample(cerExample);
        if (authorities != null && authorities.size() > 0) {
            CertificateAuthority certificateAuthority = authorities.get(0);
            customerID = certificateAuthority.getCustomerId();
        }
        return customerID;
    }

    /**
     * 查询用户出借记录
     * @param tenderUserId
     * @param borrowNid
     * @param tenderNid
     * @return
     */
    private BorrowTender selectBorrowTender(Integer tenderUserId,String borrowNid,String tenderNid){
        BorrowTenderExample example = new BorrowTenderExample();
        BorrowTenderExample.Criteria cra = example.createCriteria();
        cra.andNidEqualTo(tenderNid);
        cra.andBorrowNidEqualTo(borrowNid);
        cra.andUserIdEqualTo(tenderUserId);
        List<BorrowTender> tenderList = this.borrowTenderMapper.selectByExample(example);
        if(tenderList != null && tenderList.size() > 0){
            return tenderList.get(0);
        }
        return null;
    }

    /**
     * 获取还款方式
     * @param borrowStyle
     * @return
     */
    private  String  getBorrowStyle(String borrowStyle){
        BorrowStyleExample example = new BorrowStyleExample();
        example.createCriteria().andNidEqualTo(borrowStyle);
        List<BorrowStyle> borrowStyles = this.borrowStyleMapper.selectByExample(example);

        if(borrowStyles != null && borrowStyles.size()>0){
            return borrowStyles.get(0).getName();
        }
        return "";
    }


    @Override
    public List<TenderAgreement> selectByExample(String tenderNid) {
        TenderAgreementExample example = new TenderAgreementExample();
        example.createCriteria().andTenderNidEqualTo(tenderNid);
        List<TenderAgreement> tenderAgreements= this.tenderAgreementMapper.selectByExample(example);

        if(tenderAgreements != null && tenderAgreements.size()>0){
            return tenderAgreements;
        }
        return null;
    }
    
    @Override
    public List<TenderAgreement> selectLikeByExample(String tenderNid, String nid) {
        TenderAgreementExample example = new TenderAgreementExample();
        TenderAgreementExample.Criteria cra = example.createCriteria();
        cra.andTenderNidLike(tenderNid);
        cra.andBorrowNidEqualTo(nid);
        List<TenderAgreement> tenderAgreements= this.tenderAgreementMapper.selectByExample(example);

        if(tenderAgreements != null && tenderAgreements.size()>0){
            return tenderAgreements;
        }
        return null;
    }

    /**
     * 更新脱敏后的图片地址
     * @param tenderAgreementID
     * @param iamgeurl
     * @param tmpdfPath
     */
    @Override
    public void updateTenderAgreementImageURL(String tenderAgreementID, String iamgeurl, String tmpdfPath) {

        TenderAgreement tenderAgreement = this.tenderAgreementMapper.selectByPrimaryKey(Integer.valueOf(tenderAgreementID));
        tenderAgreement.setImgUrl(iamgeurl);
        tenderAgreement.setPdfUrl(tmpdfPath);
        tenderAgreement.setStatus(3);//下载成功
        this.tenderAgreementMapper.updateByPrimaryKey(tenderAgreement);
    }

    /**
     * 获得合同签署信息
     * @param tenderAgreementID
     * @return
     */
    @Override
    public TenderAgreement getTenderAgrementInfo(String tenderAgreementID) {
        TenderAgreement tenderAgreement = this.tenderAgreementMapper.selectByPrimaryKey(Integer.valueOf(tenderAgreementID));
        return tenderAgreement;
    }

    /**
     * 根据加入订单号查询计划加入订单
     * @param tenderNid
     * @return
     */
    @Override
    public HjhAccede selectHjhAccede(String tenderNid) {
        HjhAccedeExample example = new HjhAccedeExample();
        HjhAccedeExample.Criteria cra = example.createCriteria();
        cra.andAccedeOrderIdEqualTo(tenderNid);
        List<HjhAccede> list = this.hjhAccedeMapper.selectByExample(example);
        if (list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }


    /**
     * 根据借款编号取借款主体为企业用户的信息
     * @param borrowNid
     * @return
     */
    @Override
    public BorrowUsers getBorrowUsers(String borrowNid) {
        BorrowUsersExample example = new BorrowUsersExample();
        BorrowUsersExample.Criteria cra = example.createCriteria();
        cra.andBorrowNidEqualTo(borrowNid);
        List<BorrowUsers> list = this.borrowUsersMapper.selectByExample(example);
        if(list!= null && list.size()>0){
            return list.get(0);
        }
        return null;
    }


    /**
     * 获取CA认证客户编号
     * @param borrowUsers
     * @return
     */
    private String getCACustomerID(BorrowUsers borrowUsers) {
        // 用户CA认证记录表
        CertificateAuthorityExample example = new CertificateAuthorityExample();
        CertificateAuthorityExample.Criteria cra = example.createCriteria();
        cra.andTrueNameEqualTo(borrowUsers.getUsername());
        cra.andIdNoEqualTo(borrowUsers.getSocialCreditCode());
        cra.andIdTypeEqualTo(1);
        List<CertificateAuthority> list = this.certificateAuthorityMapper.selectByExample(example);
        if(list != null && list.size()> 0){
            return list.get(0).getCustomerId();
        }

        // 借款主体CA认证记录表
        LoanSubjectCertificateAuthorityExample loanSubjectCertificateAuthorityExample = new LoanSubjectCertificateAuthorityExample();
        LoanSubjectCertificateAuthorityExample.Criteria  loanSubjectCra = loanSubjectCertificateAuthorityExample.createCriteria();
        loanSubjectCra.andNameEqualTo(borrowUsers.getUsername());
        loanSubjectCra.andIdTypeEqualTo(1);
        loanSubjectCra.andIdNoEqualTo(borrowUsers.getSocialCreditCode());
        List<LoanSubjectCertificateAuthority> resultList = this.loanSubjectCertificateAuthorityMapper.selectByExample(loanSubjectCertificateAuthorityExample);

        if (resultList!=null&&resultList.size()>0){
            return resultList.get(0).getCustomerId();
        }
        return null;
    }


    /**
     * 根据标的号检索借款主体个人借款信息
     * @param borrowNid
     * @return
     */
    @Override
    public BorrowManinfo getBorrowManInfo(String borrowNid) {
        BorrowManinfoExample example = new BorrowManinfoExample();
        BorrowManinfoExample.Criteria criteria = example.createCriteria();
        criteria.andBorrowNidEqualTo(borrowNid);
        List<BorrowManinfo> list = this.borrowManinfoMapper.selectByExample(example);

        if (list != null && list.size() > 0 ){
            return list.get(0);
        }
        return null;
    }

    /**
     * 获取借款主体为个人的CA认证客户编号
     * @param borrowManinfo
     * @return
     */
    private String getPersonCACustomerID(BorrowManinfo borrowManinfo) {
        // 用户CA认证记录表
        CertificateAuthorityExample example = new CertificateAuthorityExample();
        CertificateAuthorityExample.Criteria cra = example.createCriteria();
        cra.andTrueNameEqualTo(borrowManinfo.getName());
        cra.andIdNoEqualTo(borrowManinfo.getCardNo());
        cra.andIdTypeEqualTo(0);
        List<CertificateAuthority> list = this.certificateAuthorityMapper.selectByExample(example);
        if(list != null && list.size()> 0){
            return list.get(0).getCustomerId();
        }

        // 借款主体CA认证记录表
        LoanSubjectCertificateAuthorityExample loanSubjectCertificateAuthorityExample = new LoanSubjectCertificateAuthorityExample();
        LoanSubjectCertificateAuthorityExample.Criteria  loanSubjectCra = loanSubjectCertificateAuthorityExample.createCriteria();
        loanSubjectCra.andNameEqualTo(borrowManinfo.getName());
        loanSubjectCra.andIdTypeEqualTo(0);
        loanSubjectCra.andIdNoEqualTo(borrowManinfo.getCardNo());
        List<LoanSubjectCertificateAuthority> resultList = this.loanSubjectCertificateAuthorityMapper.selectByExample(loanSubjectCertificateAuthorityExample);

        if (resultList!=null&&resultList.size()>0){
            return resultList.get(0).getCustomerId();
        }
        return null;
    }

    /**
     * 签署成功后更新合同模板信息
     * @param contract_id
     * @return
     */
    @Override
    public void sendMQToNifa(String contract_id) {
        NifaContractEssenceExample nifaContractEssenceExample = new NifaContractEssenceExample();
        nifaContractEssenceExample.createCriteria().andContractNoEqualTo(contract_id);
        List<NifaContractEssence> nifaContractEssenceList = this.nifaContractEssenceMapper.selectByExample(nifaContractEssenceExample);
        // 已经生成数据的合同不再发送MQ
        if (null != nifaContractEssenceList && nifaContractEssenceList.size()>0) {
            return;
        }
        List<Integer> list = new ArrayList<>();
        // 签署成功
        list.add(2);
        // 下载成功
        list.add(3);
        TenderAgreementExample example = new TenderAgreementExample();
        example.createCriteria().andTenderNidEqualTo(contract_id).andStatusIn(list).andContractSignTimeGreaterThan(0);
        List<TenderAgreement> agreements = this.tenderAgreementMapper.selectByExample(example);
        if (agreements != null && agreements.size() > 0) {
            TenderAgreement tenderAgreement = agreements.get(0);
            FddDessenesitizationBean bean = new FddDessenesitizationBean();
            bean.setAgrementID(tenderAgreement.getId().toString());
            bean.setOrdid(contract_id);
            // 签署成功生成合同要素信息
            log.info("签署成功发送MQ消息生成合同要素信息：合同ID：" + tenderAgreement.getId() + ",订单编号：" + contract_id + ",合同状态：" + tenderAgreement.getStatus());
            rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_NIFA_CONTRACT_ESSENCE, JSONObject.toJSONString(bean));
        }
    }

}
