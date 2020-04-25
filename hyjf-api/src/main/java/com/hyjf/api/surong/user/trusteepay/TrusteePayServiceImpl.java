package com.hyjf.api.surong.user.trusteepay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BankOpenAccountExample;
import com.hyjf.mybatis.model.auto.BankSmsAuthCode;
import com.hyjf.mybatis.model.auto.BankSmsAuthCodeExample;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.ChinapnrExclusiveLogExample;
import com.hyjf.mybatis.model.auto.ChinapnrExclusiveLogWithBLOBs;
import com.hyjf.mybatis.model.auto.CorpOpenAccountRecord;
import com.hyjf.mybatis.model.auto.CorpOpenAccountRecordExample;
import com.hyjf.mybatis.model.auto.HjhPlanAsset;
import com.hyjf.mybatis.model.auto.HjhPlanAssetExample;
import com.hyjf.mybatis.model.auto.HjhUserAuth;
import com.hyjf.mybatis.model.auto.HjhUserAuthExample;
import com.hyjf.mybatis.model.auto.HjhUserAuthLog;
import com.hyjf.mybatis.model.auto.HjhUserAuthLogExample;
import com.hyjf.mybatis.model.auto.STZHWhiteList;
import com.hyjf.mybatis.model.auto.STZHWhiteListExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;

@Service
public class TrusteePayServiceImpl extends BaseServiceImpl implements TrusteePayService{

    @Autowired
    @Qualifier("myAmqpTemplate")
    private RabbitTemplate rabbitTemplate;

    Logger _log = LoggerFactory.getLogger("TrusteePayServer");

    /**
     * 根据电子账户号查询用户开户信息
     * 
     * @param accountId
     * @return
     */
    @Override
    public BankOpenAccount selectBankOpenAccountByAccountId(String accountId) {
        BankOpenAccountExample example = new BankOpenAccountExample();
        BankOpenAccountExample.Criteria cra = example.createCriteria();
        cra.andAccountEqualTo(accountId);
        List<BankOpenAccount> list = this.bankOpenAccountMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public String selectBankSmsLog(Integer userId, String srvTxCode) {
        BankSmsAuthCodeExample example = new BankSmsAuthCodeExample();
        example.createCriteria().andUserIdEqualTo(userId).andSrvTxCodeEqualTo(srvTxCode);
        List<BankSmsAuthCode> smsAuthCodeList = bankSmsAuthCodeMapper.selectByExample(example);
        if (smsAuthCodeList != null && smsAuthCodeList.size() == 1) {
            BankSmsAuthCode smsAuthCode = smsAuthCodeList.get(0);
            return smsAuthCode.getSmsSeq();
        }
        return null;
    }
    
    @Override
    public Map<String,Object> selectUserAuth(Integer userId){
        Map<String, Object> map = hjhPlanCustomizeMapper.selectUserAppointmentInfo(userId+"");
        return map;
    }

    @Override
    public boolean insertUserAuthInves(Integer userId, BankCallBean bean) {
        HjhUserAuth hjhUserAuth = new HjhUserAuth();
        Users users = this.getUsers(userId);
        hjhUserAuth.setUserId(userId);
        hjhUserAuth.setUserName(users.getUsername());
        hjhUserAuth.setCreateUser(userId);
        hjhUserAuth.setCreateTime(GetDate.getNowTime10());
        hjhUserAuth.setDelFlg(0);
        hjhUserAuth.setAutoOrderId(bean.getOrderId());
        hjhUserAuth.setAutoInvesStatus(Integer.parseInt(bean.getAutoBid()));
        hjhUserAuth.setAutoCreditStatus(Integer.parseInt(bean.getAutoTransfer()));
        hjhUserAuth.setAutoWithdrawStatus(Integer.parseInt(bean.getAgreeWithdraw()));
        hjhUserAuth.setAutoConsumeStatus(Integer.parseInt(bean.getDirectConsume()));
        hjhUserAuth.setAutoCreateTime(GetDate.getNowTime10());
        hjhUserAuth.setUpdateTime(GetDate.getNowTime10());
        hjhUserAuth.setUpdateUser(userId);
        boolean flag = hjhUserAuthMapper.insertSelective(hjhUserAuth) > 0 ? true : false;
        return flag;
    }
    
    @Override
    public boolean updateUserAuthInves(Integer userId, BankCallBean bean) {
        
        HjhUserAuth hjhUserAuth = new HjhUserAuth();
        HjhUserAuthExample example=new HjhUserAuthExample();
        example.createCriteria().andUserIdEqualTo(userId);
        Users users = this.getUsers(userId);
        hjhUserAuth.setUserId(userId);
        hjhUserAuth.setUserName(users.getUsername());
        hjhUserAuth.setCreateUser(userId);
        hjhUserAuth.setCreateTime(GetDate.getNowTime10());
        hjhUserAuth.setDelFlg(0);
        hjhUserAuth.setAutoOrderId(bean.getOrderId());
        hjhUserAuth.setAutoInvesStatus(Integer.parseInt(bean.getAutoBid()));
        hjhUserAuth.setAutoCreditStatus(Integer.parseInt(bean.getAutoTransfer()));
        hjhUserAuth.setAutoWithdrawStatus(Integer.parseInt(bean.getAgreeWithdraw()));
        hjhUserAuth.setAutoConsumeStatus(Integer.parseInt(bean.getDirectConsume()));
        hjhUserAuth.setAutoCreateTime(GetDate.getNowTime10());
        hjhUserAuth.setUpdateTime(GetDate.getNowTime10());
        hjhUserAuth.setUpdateUser(userId);
        
        boolean flag =  hjhUserAuthMapper.updateByExampleSelective(hjhUserAuth,example)  > 0 ? true : false;

        return flag;
    }

    @Override
    public boolean insertUserAuthLog(Integer userId,BankCallBean bean,String auth_type) {
        HjhUserAuthLog hjhUserAuthLog = new HjhUserAuthLog();
        Users users = this.getUsers(userId);
        hjhUserAuthLog.setUserId(userId);                     
        hjhUserAuthLog.setUserName(users.getUsername());                       
        hjhUserAuthLog.setOrderId(bean.getLogOrderId());                        
        hjhUserAuthLog.setOrderStatus(2);                        
        hjhUserAuthLog.setAuthType(Integer.parseInt(auth_type));                                         
        hjhUserAuthLog.setOperateEsb(4);                     
        hjhUserAuthLog.setAuthCreateTime(GetDate.getNowTime10());                        
        hjhUserAuthLog.setCreateTime(GetDate.getNowTime10());                     
        hjhUserAuthLog.setCreateUser(userId);                     
        hjhUserAuthLog.setUpdateTime(GetDate.getNowTime10());                     
        hjhUserAuthLog.setUpdateUser(userId);                     
        hjhUserAuthLog.setDelFlg(0);                     
        boolean flag = hjhUserAuthLogMapper.insertSelective(hjhUserAuthLog) > 0 ? true : false;
        return flag;
    }
    
    @Override
    public boolean updateUserAuthLog(Integer userId,String orderId){
        HjhUserAuthLog hjhUserAuthLog = new HjhUserAuthLog();
        HjhUserAuthLogExample example=new HjhUserAuthLogExample();
        example.createCriteria().andOrderIdEqualTo(orderId);
        hjhUserAuthLog.setOrderStatus(1);                        
        hjhUserAuthLog.setUpdateTime(GetDate.getNowTime10());                     
        hjhUserAuthLog.setUpdateUser(userId);                     
        hjhUserAuthLog.setDelFlg(0);   
        boolean flag = hjhUserAuthLogMapper.updateByExampleSelective(hjhUserAuthLog, example)>0?true : false;
        return flag;
    }

    @Override
    public Borrow selectBorrowByProductId(String productId) {
        BorrowExample example = new BorrowExample();
        BorrowExample.Criteria crt = example.createCriteria();
        crt.andBorrowNidEqualTo(productId);
        List<Borrow> borrows = this.borrowMapper.selectByExample(example);
        if (borrows != null && borrows.size() > 0) {
            return borrows.get(0);
        }
        return null;
    }

    @Override
    public Boolean updateTrusteePaySuccess(BankCallBean bean) {
        // 修改huiyingdai_borrow表状态  根据返回值productId对应表的id   status改为2
        // 修改hyjf_hjh_plan_asset status改为
        String nid = bean.getProductId();
        BorrowWithBLOBs borrow = new BorrowWithBLOBs();
        borrow.setBorrowNid(nid);
        borrow.setStatus(1);
        borrow.setTrusteePayTime(GetDate.getNowTime10());// 受托支付完成时间
        BorrowExample example = new BorrowExample();
        example.createCriteria().andBorrowNidEqualTo(nid).andStatusEqualTo(7);
        boolean flag = this.borrowMapper.updateByExampleSelective(borrow, example) > 0 ? true : false;
        if(flag){
            HjhPlanAsset hp = new HjhPlanAsset();
            hp.setStatus(5);
            hp.setBorrowNid(nid);
            HjhPlanAssetExample hpexp = new HjhPlanAssetExample();
            hpexp.createCriteria().andBorrowNidEqualTo(nid);
            flag = this.hjhPlanAssetMapper.updateByExampleSelective(hp, hpexp) > 0 ? true : false;

            if(flag){
                _log.info("受托支付开始推送消息到MQ,标的编号:【"+bean.getProductId()+"】");
                // 查询资产表
                HjhPlanAsset hjhHp = getHjhPlanAsset(nid);
                // 推送消息到mq
                try {
                    if(hjhHp!=null){
                        sendToMQ(hjhHp.getAssetId(),hjhHp.getInstCode(),RabbitMQConstants.ROUTINGKEY_BORROW_PREAUDIT);
                    }
                } catch (Exception e) {
                    _log.info("借款人受托支付申请出错,标的编号:【"+bean.getProductId()+"】错误原因："+e.getMessage());
                }
                _log.info("受托支付推送消息到MQ结束,标的编号:【"+bean.getProductId()+"】");
            }
        }
        return flag;
    }

    @Override
    public STZHWhiteList getSTZHWhiteList(String accountId, String receiptAccountId) {
        STZHWhiteListExample example = new STZHWhiteListExample();
        example.createCriteria().andStAccountidEqualTo(receiptAccountId).andAccountidEqualTo(accountId);
        List<STZHWhiteList> lists = this.sTZHWhiteListMapper.selectByExample(example);
        if (lists != null && lists.size() > 0) {
            return lists.get(0);
            
        }
        return null;
    }

    @Override
    public ChinapnrExclusiveLogWithBLOBs selectChinapnrExclusiveLogByOrderId(String logOrderId) {
        ChinapnrExclusiveLogExample example = new ChinapnrExclusiveLogExample();
        ChinapnrExclusiveLogExample.Criteria cra = example.createCriteria();
        cra.andOrdidEqualTo(logOrderId);
        List<ChinapnrExclusiveLogWithBLOBs> result = chinapnrExclusiveLogMapper.selectByExampleWithBLOBs(example);
        if (result != null && result.size() > 0) {
            return result.get(0);
        }
        return null;
    }

    @Override
    public STZHWhiteList getSTZHWhiteListByCode(String instCode, String receiptAccountId) {
        STZHWhiteListExample example = new STZHWhiteListExample();
        example.createCriteria().andStAccountidEqualTo(receiptAccountId).andInstcodeEqualTo(instCode);
        List<STZHWhiteList> lists = this.sTZHWhiteListMapper.selectByExample(example);
        if (lists != null && lists.size() > 0) {
            return lists.get(0);

        }
        return null;
    }

    // 推送消息到MQ
    private void sendToMQ(String assetId,String instCode,String routingKey){

        // 加入到消息队列
        Map<String, String> params = new HashMap<String, String>();
        params.put("mqMsgId", GetCode.getRandomCode(10));
        params.put("assetId", assetId);
        params.put("instCode", instCode);

        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, routingKey, JSONObject.toJSONString(params));

    }

    // 根据borrowid查询资产表
    private HjhPlanAsset getHjhPlanAsset(String nid) {
        HjhPlanAssetExample example = new HjhPlanAssetExample();
        example.createCriteria().andBorrowNidEqualTo(nid);
        List<HjhPlanAsset> lists = this.hjhPlanAssetMapper.selectByExample(example);
        if (lists != null && lists.size() > 0) {
            return lists.get(0);

        }
        return null;
    }

    @Override
    public CorpOpenAccountRecord getCorpOpenAccountRecord(Integer userId) {
        CorpOpenAccountRecordExample example = new CorpOpenAccountRecordExample();
        CorpOpenAccountRecordExample.Criteria cra = example.createCriteria();
        cra.andUserIdEqualTo(userId);
        cra.andIsBankEqualTo(1);//江西银行
        List<CorpOpenAccountRecord> list = this.corpOpenAccountRecordMapper.selectByExample(example);
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public BankCallBean queryTrusteePayState(String accountId, String productId, String userid) {
        BankCallBean selectbean = new BankCallBean();
        // 设置共通参数
        setCommonCall(selectbean);
        selectbean.setTxCode(BankCallConstant.TXCODE_TRUSTEE_PAY_QUERY);
        selectbean.setAccountId(accountId);// 电子账号
        selectbean.setProductId(productId); // 标的编号

        // 操作者ID
        selectbean.setLogUserId(userid);
        selectbean.setLogOrderId(GetOrderIdUtils.getOrderId2(Integer.parseInt(userid)));
        selectbean.setLogClient(0);
        selectbean.setLogRemark("受托支付申请查询");
        selectbean.setLogOrderDate(GetOrderIdUtils.getOrderDate());
        return selectbean;
    }

    private void setCommonCall(BankCallBean selectbean) {
        selectbean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        selectbean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
        selectbean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));// 银行代码
        selectbean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
        selectbean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
        selectbean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
        selectbean.setChannel(BankCallConstant.CHANNEL_PC); // 交易渠道
    }
}
