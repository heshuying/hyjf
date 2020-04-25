package com.hyjf.mqreceiver.hgdatareport.cert.transact;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.hyjf.common.util.GetDate;
import com.hyjf.mqreceiver.hgdatareport.cert.BaseHgCertReportServiceImpl;
import com.hyjf.mqreceiver.hgdatareport.common.CertCallConstant;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AccountListExample;
import com.hyjf.mybatis.model.auto.AccountRecharge;
import com.hyjf.mybatis.model.auto.AccountRechargeExample;
import com.hyjf.mybatis.model.auto.Accountwithdraw;
import com.hyjf.mybatis.model.auto.AccountwithdrawExample;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowApicron;
import com.hyjf.mybatis.model.auto.BorrowApicronExample;
import com.hyjf.mybatis.model.auto.BorrowManinfo;
import com.hyjf.mybatis.model.auto.BorrowRecover;
import com.hyjf.mybatis.model.auto.BorrowRecoverExample;
import com.hyjf.mybatis.model.auto.BorrowRecoverPlan;
import com.hyjf.mybatis.model.auto.BorrowRecoverPlanExample;
import com.hyjf.mybatis.model.auto.BorrowRepay;
import com.hyjf.mybatis.model.auto.BorrowRepayExample;
import com.hyjf.mybatis.model.auto.BorrowRepayPlan;
import com.hyjf.mybatis.model.auto.BorrowRepayPlanExample;
import com.hyjf.mybatis.model.auto.BorrowTenderCpn;
import com.hyjf.mybatis.model.auto.BorrowTenderCpnExample;
import com.hyjf.mybatis.model.auto.BorrowUsers;
import com.hyjf.mybatis.model.auto.CertUser;
import com.hyjf.mybatis.model.auto.CertUserExample;
import com.hyjf.mybatis.model.auto.CouponRealTender;
import com.hyjf.mybatis.model.auto.CouponRealTenderExample;
import com.hyjf.mybatis.model.auto.CouponRecover;
import com.hyjf.mybatis.model.auto.CouponRecoverExample;
import com.hyjf.mybatis.model.auto.CreditRepay;
import com.hyjf.mybatis.model.auto.CreditRepayExample;
import com.hyjf.mybatis.model.auto.CreditTender;
import com.hyjf.mybatis.model.auto.CreditTenderExample;
import com.hyjf.mybatis.model.auto.HjhDebtCreditRepay;
import com.hyjf.mybatis.model.auto.HjhDebtCreditRepayExample;
import com.hyjf.mybatis.model.auto.HjhDebtCreditTender;
import com.hyjf.mybatis.model.auto.HjhDebtCreditTenderExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.CertAccountListCustomize;
/**
 * @author pcc
 */

@Service
public class CertTransactServiceImpl extends BaseHgCertReportServiceImpl implements CertTransactService {
	Logger logger = LoggerFactory.getLogger(CertTransactServiceImpl.class);

	@Override
	public JSONArray createDate(String minId, String maxId) {
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		
		Map<String, Object> map =new HashMap<String, Object>();
		map.put("minId", minId);
		map.put("maxId", maxId);
		List<CertAccountListCustomize> accountLists=accountDetailCustomizeMapper.queryCertAccountList(map);
		try {
			for (CertAccountListCustomize accountList : accountLists) {
				
				 createParam(accountList,list);
			}
			if(list==null||list.size()==0){
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return JSONArray.parseArray(JSON.toJSONString(list));
	}

	private void createParam(CertAccountListCustomize accountList,List<Map<String, Object>> list) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> param1 = new HashMap<String, Object>();
		Map<String, Object> param2 = new HashMap<String, Object>();
		Borrow borrow=new Borrow();
		List<BorrowRecover> borrowRecovers=new ArrayList<BorrowRecover>();
		BorrowRecoverExample borrowRecoverExample=new BorrowRecoverExample();
		BorrowRecoverPlanExample borrowRecoverPlanExample=new BorrowRecoverPlanExample();
		List<BorrowRecoverPlan> borrowRecoverPlans=new ArrayList<BorrowRecoverPlan>();
		CouponRealTenderExample couponRealTenderExample=new CouponRealTenderExample();
		List<CouponRealTender> couponRealTenders=new ArrayList<CouponRealTender>();
		List<BorrowRepay> borrowRepays=new ArrayList<BorrowRepay>();
		BorrowRepayExample borrowRepayExample=new BorrowRepayExample();
		HjhDebtCreditTenderExample hjhDebtCreditTenderExample= new HjhDebtCreditTenderExample();
		List<HjhDebtCreditTender> hjhDebtCreditTenders=new ArrayList<HjhDebtCreditTender>();
		CreditTenderExample creditTenderExample=new CreditTenderExample();
		List<CreditTender> creditTenders=new ArrayList<CreditTender>();
		CertUserExample certUserExample=new CertUserExample();
		List<CertUser> certUser=new ArrayList<CertUser>();
		UsersInfo usersInfo=this.getUsersInfoByUserId(accountList.getUserId());
		String period="";
		String nid="";
		switch (accountList.getTrade()) {
		//提现  发送7提现  以及23提现手续费
		case "cash_success":
			if(accountList.getRoleId()!=1){
				return ;
			}
			AccountwithdrawExample accountwithdrawExample=new AccountwithdrawExample();
			accountwithdrawExample.createCriteria().andNidEqualTo(accountList.getNid());
			List<Accountwithdraw> accountwithdraws=accountwithdrawMapper.selectByExample(accountwithdrawExample);
			if(accountwithdraws==null||accountwithdraws.size()==0){
				return;
			}
			
			/******************发送7提现******************/
			//接口版本号
			param.put("version", CertCallConstant.CERT_CALL_VERSION);
			//交易流水时间
			param.put("transTime", GetDate.timestamptoStrYYYYMMDDHHMMSS(accountList.getCreateTime()+""));
			//平台交易流水号
			param.put("transId", accountList.getNid());
			//平台编号
			param.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
			//原散标编号
			param.put("sourceProductCode", "-1");
			//原散标名称
			param.put("sourceProductName", "-1");
			//交易类型
			param.put("transType", "7");
			//交易方式
			param.put("transPayment", "g");
			//交易类型描述
			param.put("transTypeDec", CertTradeTypeEnum.getName("7"));
			//交易金额
			param.put("transMoney", accountwithdraws.get(0).getCredited());
			//交易日期
			param.put("transDate", GetDate.times10toStrYYYYMMDD(accountList.getCreateTime()));
			//交易人员银行（或三方支付名称）
			param.put("transBank", "江西银行");
			//用户标示哈希
			param.put("userIdcardHash", tool.idCardHash(usersInfo.getIdcard()));
			//存管银行流水编号	
			param.put("bankTransId", accountList.getSeqNo());
			//原产品信息编号
			param.put("sourceFinancingCode", "-1");
			//原产品信息名称
			param.put("sourceFinancingName", "-1");
			//债权编号
			param.put("finClaimId", "-1");
			//转让项目编号
			param.put("transferId", "-1");
			//	还款计划编号
			param.put("replanId", "-1");
			list.add(param);
			/******************发送23提现手续费******************/
			//接口版本号
			param1.put("version", CertCallConstant.CERT_CALL_VERSION);
			//交易流水时间
			param1.put("transTime", GetDate.timestamptoStrYYYYMMDDHHMMSS(accountList.getCreateTime()+""));
			//	平台交易流水号
			param1.put("transId", accountList.getNid());
			//平台编号
			param1.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
			//原散标编号
			param1.put("sourceProductCode", "-1");
			//原散标名称
			param1.put("sourceProductName", "-1");
			//交易类型
			param1.put("transType", "23");
			//交易方式
			param1.put("transPayment", "g");
			//交易类型描述
			param1.put("transTypeDec", CertTradeTypeEnum.getName("23"));
			//交易金额
			param1.put("transMoney", accountwithdraws.get(0).getFee());
			//	交易日期
			param1.put("transDate", GetDate.times10toStrYYYYMMDD(accountList.getCreateTime()));
			//交易人员银行（或三方支付名称）
			param1.put("transBank", "江西银行");
			//用户标示哈希
			param1.put("userIdcardHash", tool.idCardHash(usersInfo.getIdcard()));
			//存管银行流水编号	
			param1.put("bankTransId", accountList.getSeqNo());
			//原产品信息编号
			param1.put("sourceFinancingCode", "-1");
			//原产品信息名称
			param1.put("sourceFinancingName", "-1");
			//债权编号
			param1.put("finClaimId", "-1");
			//转让项目编号
			param1.put("transferId", "-1");
			//	还款计划编号
			param1.put("replanId", "-1");
			list.add(param1);
			
			break;
		
		case "account_adjustment_down":
			if(accountList.getRoleId()!=1){
				return ;
			}
			/******************发送7提现******************/
			//接口版本号
			param.put("version", CertCallConstant.CERT_CALL_VERSION);
			//交易流水时间
			param.put("transTime", GetDate.timestamptoStrYYYYMMDDHHMMSS(accountList.getCreateTime()+""));
			//平台交易流水号
			if(accountList.getNid()==null||accountList.getNid().length()==0){
				//平台交易流水号
				param.put("transId", accountList.getId()+"");
			}else{
				//平台交易流水号
				param.put("transId", accountList.getNid());
			}
			//平台编号
			param.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
			//原散标编号
			param.put("sourceProductCode", "-1");
			//原散标名称
			param.put("sourceProductName", "-1");
			//交易类型
			param.put("transType", "7");
			//交易方式
			param.put("transPayment", "g");
			//交易类型描述
			param.put("transTypeDec", CertTradeTypeEnum.getName("7"));
			//交易金额
			param.put("transMoney", accountList.getAmount().toString());
			//交易日期
			param.put("transDate", GetDate.times10toStrYYYYMMDD(accountList.getCreateTime()));
			//交易人员银行（或三方支付名称）
			param.put("transBank", "江西银行");
			//用户标示哈希
			param.put("userIdcardHash", tool.idCardHash(usersInfo.getIdcard()));
			//存管银行流水编号	
			param.put("bankTransId", accountList.getSeqNo());
			//原产品信息编号
			param.put("sourceFinancingCode", "-1");
			//原产品信息名称
			param.put("sourceFinancingName", "-1");
			//债权编号
			param.put("finClaimId", "-1");
			//转让项目编号
			param.put("transferId", "-1");
			//	还款计划编号
			param.put("replanId", "-1");
			list.add(param);
			break;
		//充值        线下充值  发送6充值
		case "recharge":
		case "recharge_offline":
			
			if(accountList.getRoleId()!=1){
				return ;
			}
			AccountRechargeExample accountRechargeExample=new AccountRechargeExample();
			accountRechargeExample.createCriteria().andNidEqualTo(accountList.getNid());
			List<AccountRecharge> accountRecharges=accountRechargeMapper.selectByExample(accountRechargeExample);
			if(accountRecharges==null||accountRecharges.size()==0){
				return;
			}
			
			/******************发送6充值******************/
			//接口版本号
			param.put("version", CertCallConstant.CERT_CALL_VERSION);
			//交易流水时间
			param.put("transTime", GetDate.timestamptoStrYYYYMMDDHHMMSS(accountList.getCreateTime()+""));
			//	平台交易流水号
			param.put("transId", accountList.getNid());
			//平台编号
			param.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
			//原散标编号
			param.put("sourceProductCode", "-1");
			//原散标名称
			param.put("sourceProductName", "-1");
			//交易类型
			param.put("transType", "6");
			if("recharge".equals(accountList.getTrade())){
				//交易方式
				param.put("transPayment", "f");
			}else{
				//交易方式
				param.put("transPayment", "d");
			}
			
			//交易类型描述
			param.put("transTypeDec", CertTradeTypeEnum.getName("6"));
			//交易金额
			param.put("transMoney", accountRecharges.get(0).getMoney());
			//	交易日期
			param.put("transDate", GetDate.times10toStrYYYYMMDD(accountList.getCreateTime()));
			//交易人员银行（或三方支付名称）
			param.put("transBank", "江西银行");
			//用户标示哈希
			param.put("userIdcardHash", tool.idCardHash(usersInfo.getIdcard()));
			//存管银行流水编号	
			param.put("bankTransId", accountList.getSeqNo());
			//原产品信息编号
			param.put("sourceFinancingCode", "-1");
			//原产品信息名称
			param.put("sourceFinancingName", "-1");
			//债权编号
			param.put("finClaimId", "-1");
			//转让项目编号
			param.put("transferId", "-1");
			//	还款计划编号
			param.put("replanId", "-1");
			list.add(param);
			break;
		case "auto_reverse":
		case "account_adjustment_up":
			if(accountList.getRoleId()!=1){
				return ;
			}
			
			/******************发送6充值******************/
			//接口版本号
			param.put("version", CertCallConstant.CERT_CALL_VERSION);
			//交易流水时间
			param.put("transTime", GetDate.timestamptoStrYYYYMMDDHHMMSS(accountList.getCreateTime()+""));
			//	平台交易流水号
			if(accountList.getNid()==null||accountList.getNid().length()==0){
				//平台交易流水号
				param.put("transId", accountList.getId()+"");
			}else{
				//平台交易流水号
				param.put("transId", accountList.getNid());
			}
			//平台编号
			param.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
			//原散标编号
			param.put("sourceProductCode", "-1");
			//原散标名称
			param.put("sourceProductName", "-1");
			//交易类型
			param.put("transType", "6");
			//交易方式
			param.put("transPayment", "a");
			//交易类型描述
			param.put("transTypeDec", CertTradeTypeEnum.getName("6"));
			//交易金额
			param.put("transMoney", accountList.getAmount().toString());
			//	交易日期
			param.put("transDate", GetDate.times10toStrYYYYMMDD(accountList.getCreateTime()));
			//交易人员银行（或三方支付名称）
			param.put("transBank", "江西银行");
			//用户标示哈希
			param.put("userIdcardHash", tool.idCardHash(usersInfo.getIdcard()));
			//存管银行流水编号	
			param.put("bankTransId", accountList.getSeqNo());
			//原产品信息编号
			param.put("sourceFinancingCode", "-1");
			//原产品信息名称
			param.put("sourceFinancingName", "-1");
			//债权编号
			param.put("finClaimId", "-1");
			//转让项目编号
			param.put("transferId", "-1");
			//	还款计划编号
			param.put("replanId", "-1");
			list.add(param);
			break;
		//借款成功  发送1放款   发送5交易手续费（）
		case "borrow_success":
			borrow= this.getBorrowByBorrowNid(accountList.getRemark());
			certUserExample=new CertUserExample();
			certUserExample.createCriteria().
			andBorrowNidEqualTo(accountList.getRemark()).
			andUserIdEqualTo(borrow.getUserId());
			certUser=certUserMapper.selectByExample(certUserExample);
			if(certUser==null||certUser.size()==0){
				certUser=new ArrayList<CertUser>();
				CertUser cUser=new CertUser();
				if("1".equals(borrow.getCompanyOrPersonal())){
					// 公司
	                BorrowUsers borrowUsers = getBorrowUsers(borrow.getBorrowNid());
	                // 统一社会信用代码
	                String userIdcard = borrowUsers.getSocialCreditCode();
	                cUser.setUserIdCardHash(tool.idCardHash(userIdcard));
				}else{
					
					// 个人
	                BorrowManinfo borrowManinfo = getBorrowManInfo(borrow.getBorrowNid());
	                // 身份证号
	                String userIdcard = borrowManinfo.getCardNo();
					cUser.setUserIdCardHash(tool.idCardHash(userIdcard));
				}
				certUser.add(cUser);
			}
			/******************发送 发送1放款******************/
			//接口版本号
			param.put("version", CertCallConstant.CERT_CALL_VERSION);
			//交易流水时间
			param.put("transTime", GetDate.timestamptoStrYYYYMMDDHHMMSS(accountList.getCreateTime()+""));
			//	平台交易流水号
			param.put("transId", accountList.getNid());
			//平台编号
			param.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
			//原散标编号
			param.put("sourceProductCode", borrow.getBorrowNid());
			//原散标名称
			param.put("sourceProductName", borrow.getName());
			//交易类型
			param.put("transType", "1");
			//交易方式
			param.put("transPayment", "a");
			
			//交易类型描述
			param.put("transTypeDec", CertTradeTypeEnum.getName("1"));
			//交易金额
			param.put("transMoney", accountList.getAmount());
			//	交易日期
			param.put("transDate", GetDate.times10toStrYYYYMMDD(accountList.getCreateTime()));
			//交易人员银行（或三方支付名称）
			param.put("transBank", "江西银行");
			//用户标示哈希
			param.put("userIdcardHash", certUser.get(0).getUserIdCardHash());
			//存管银行流水编号	
			param.put("bankTransId", accountList.getSeqNo());
			//原产品信息编号
			param.put("sourceFinancingCode", "-1");
			//原产品信息名称
			param.put("sourceFinancingName", "-1");
			//债权编号
			param.put("finClaimId", "-1");
			//转让项目编号
			param.put("transferId", "-1");
			//	还款计划编号
			param.put("replanId", "-1");
			list.add(param);
			
			
			/****************** 发送5交易手续费******************/
			borrowRecoverExample=new BorrowRecoverExample();
			borrowRecoverExample.createCriteria().andBorrowNidEqualTo(borrow.getBorrowNid());
			borrowRecovers=borrowRecoverMapper.selectByExample(borrowRecoverExample);
			BigDecimal recoverFee=BigDecimal.ZERO;
			if(borrowRecovers==null||borrowRecovers.size()==0){
				return;
			}
			for (BorrowRecover borrowRecover : borrowRecovers) {
				recoverFee=recoverFee.add(borrowRecover.getRecoverServiceFee());
			}
			if(recoverFee.compareTo(BigDecimal.ZERO)==1){
				//接口版本号
				param1.put("version", CertCallConstant.CERT_CALL_VERSION);
				//交易流水时间
				param1.put("transTime", GetDate.timestamptoStrYYYYMMDDHHMMSS(accountList.getCreateTime()+""));
				//	平台交易流水号
				param1.put("transId", accountList.getNid());
				//平台编号
				param1.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
				//原散标编号
				param1.put("sourceProductCode", borrow.getBorrowNid());
				//原散标名称
				param1.put("sourceProductName", borrow.getName());
				//交易类型
				param1.put("transType", "5");
				//交易方式
				param1.put("transPayment", "a");
				
				//交易类型描述
				param1.put("transTypeDec", CertTradeTypeEnum.getName("5"));
				//交易金额
				param1.put("transMoney", recoverFee);
				//	交易日期
				param1.put("transDate", GetDate.times10toStrYYYYMMDD(accountList.getCreateTime()));
				//交易人员银行（或三方支付名称）
				param1.put("transBank", "江西银行");
				//用户标示哈希
				param1.put("userIdcardHash", certUser.get(0).getUserIdCardHash());
				//存管银行流水编号	
				param1.put("bankTransId", accountList.getSeqNo());
				//原产品信息编号
				param1.put("sourceFinancingCode", "-1");
				//原产品信息名称
				param1.put("sourceFinancingName", "-1");
				//债权编号
				param1.put("finClaimId", "-1");
				//转让项目编号
				param1.put("transferId", "-1");
				//	还款计划编号
				param1.put("replanId", "-1");
				list.add(param1);
			}
			
			AccountListExample accountListExample=new AccountListExample();
			List<String> tradeList=new ArrayList<String>();
			tradeList.add("hjh_tender_success");
			tradeList.add("tender_success");
			accountListExample.createCriteria().andTradeIn(tradeList).andRemarkEqualTo(borrow.getBorrowNid());
			List<AccountList> tenderList=accountListMapper.selectByExample(accountListExample);
			for (AccountList accountList2 : tenderList) {
				UsersInfo tenderUsersInfo=this.getUsersInfoByUserId(accountList2.getUserId());
				Map<String, Object> param3 = new HashMap<String, Object>();
				//接口版本号
				param3.put("version", CertCallConstant.CERT_CALL_VERSION);
				//交易流水时间
				param3.put("transTime", GetDate.timestamptoStrYYYYMMDDHHMMSS(accountList2.getCreateTime()+""));
				//	平台交易流水号
				param3.put("transId", accountList2.getNid());
				//平台编号
				param3.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
				//原散标编号
				param3.put("sourceProductCode", borrow.getBorrowNid());
				//原散标名称
				param3.put("sourceProductName", borrow.getName());
				//交易类型
				param3.put("transType", "2");
				//交易方式
				param3.put("transPayment", "a");
				//交易类型描述
				param3.put("transTypeDec", CertTradeTypeEnum.getName("2"));
				//交易金额
				param3.put("transMoney", accountList2.getAmount());
				//	交易日期
				param3.put("transDate", GetDate.times10toStrYYYYMMDD(accountList2.getCreateTime()));
				//交易人员银行（或三方支付名称）
				param3.put("transBank", "江西银行");
				//用户标示哈希
				param3.put("userIdcardHash", tool.idCardHash(tenderUsersInfo.getIdcard()));
				//存管银行流水编号	
				param3.put("bankTransId", accountList2.getSeqNo());
				//原产品信息编号
				param3.put("sourceFinancingCode", "-1");
				//原产品信息名称
				param3.put("sourceFinancingName", "-1");
				//债权编号
				param3.put("finClaimId", accountList2.getNid());
				//转让项目编号
				param3.put("transferId", "-1");
				//还款计划编号
				param3.put("replanId", "-1");
				list.add(param3);
			}
			
			break;
		//还款 发送18还款本金  19还款利息  5交易手续费（还款服务费）
		case "repay_success":
			//还款 发送18还款本金  19还款利息  5交易手续费（还款服务费）
			borrow= this.getBorrowByBorrowNid(accountList.getRemark());
			period = borrow.getBorrowNid();
			
			certUserExample = new CertUserExample();
			certUserExample.createCriteria().
			andBorrowNidEqualTo(accountList.getRemark()).
			andUserIdEqualTo(borrow.getUserId());
			certUser = certUserMapper.selectByExample(certUserExample);
			if(certUser==null||certUser.size()==0){
				certUser=new ArrayList<CertUser>();
				CertUser cUser=new CertUser();
				UsersInfo uInfo=this.getUsersInfoByUserId(borrow.getUserId());
				cUser.setUserIdCardHash(tool.idCardHash(uInfo.getIdcard()));
				certUser.add(cUser);
			}
			
			
			
			if("end".equals(borrow.getBorrowStyle())||"endday".equals(borrow.getBorrowStyle())){
				BigDecimal repayCapitalYes=BigDecimal.ZERO;
				BigDecimal repayInterestYes=BigDecimal.ZERO;
				BigDecimal repayFee=BigDecimal.ZERO;
				borrowRepayExample = new BorrowRepayExample();
				borrowRepayExample.createCriteria().andNidEqualTo(accountList.getNid()).andBorrowNidEqualTo(accountList.getRemark());
				borrowRepays = borrowRepayMapper.selectByExample(borrowRepayExample);
				repayCapitalYes=borrowRepays.get(0).getRepayCapitalYes();
				repayInterestYes=borrowRepays.get(0).getRepayInterestYes();
				repayFee=borrowRepays.get(0).getRepayFee();
				
				period=period+"-"+borrowRepays.get(0).getRepayPeriod();
				/****************** 发18还款本金5交易手续费******************/
				//接口版本号
				param.put("version", CertCallConstant.CERT_CALL_VERSION);
				//交易流水时间
				param.put("transTime", GetDate.timestamptoStrYYYYMMDDHHMMSS(accountList.getCreateTime()+""));
				//	平台交易流水号
				param.put("transId", accountList.getNid());
				//平台编号
				param.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
				//原散标编号
				param.put("sourceProductCode", borrow.getBorrowNid());
				//原散标名称
				param.put("sourceProductName", borrow.getName());
				//交易类型
				param.put("transType", "18");
				//交易方式
				param.put("transPayment", "a");
				//交易类型描述
				param.put("transTypeDec", CertTradeTypeEnum.getName("18"));
				//交易金额
				param.put("transMoney", repayCapitalYes);
				//	交易日期
				param.put("transDate", GetDate.times10toStrYYYYMMDD(accountList.getCreateTime()));
				//交易人员银行（或三方支付名称）
				param.put("transBank", "江西银行");
				//用户标示哈希
				param.put("userIdcardHash", certUser.get(0).getUserIdCardHash());
				//存管银行流水编号	
				param.put("bankTransId", accountList.getSeqNo());
				//原产品信息编号
				param.put("sourceFinancingCode", "-1");
				//原产品信息名称
				param.put("sourceFinancingName", "-1");
				//债权编号
				param.put("finClaimId", "-1");
				//转让项目编号
				param.put("transferId", "-1");
				//	还款计划编号
				param.put("replanId", period);
				list.add(param);
				
				/****************** 发送19还款利息******************/

				//接口版本号
				param1.put("version", CertCallConstant.CERT_CALL_VERSION);
				//交易流水时间
				param1.put("transTime", GetDate.timestamptoStrYYYYMMDDHHMMSS(accountList.getCreateTime()+""));
				//	平台交易流水号
				param1.put("transId", accountList.getNid());
				//平台编号
				param1.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
				//原散标编号
				param1.put("sourceProductCode", borrow.getBorrowNid());
				//原散标名称
				param1.put("sourceProductName", borrow.getName());
				//交易类型
				param1.put("transType", "19");
				//交易方式
				param1.put("transPayment", "a");
				//交易类型描述
				param1.put("transTypeDec", CertTradeTypeEnum.getName("19"));
				//交易金额
				param1.put("transMoney", repayInterestYes);
				//	交易日期
				param1.put("transDate", GetDate.times10toStrYYYYMMDD(accountList.getCreateTime()));
				//交易人员银行（或三方支付名称）
				param1.put("transBank", "江西银行");
				//用户标示哈希
				param1.put("userIdcardHash", certUser.get(0).getUserIdCardHash());
				//存管银行流水编号	
				param1.put("bankTransId", accountList.getSeqNo());
				//原产品信息编号
				param1.put("sourceFinancingCode", "-1");
				//原产品信息名称
				param1.put("sourceFinancingName", "-1");
				//债权编号
				param1.put("finClaimId", "-1");
				//转让项目编号
				param1.put("transferId", "-1");
				//	还款计划编号
				param1.put("replanId", period);
				list.add(param1);	
				
				/****************** 发送5交易手续费******************/
				if(repayFee.compareTo(BigDecimal.ZERO)==1){
					//接口版本号
					param2.put("version", CertCallConstant.CERT_CALL_VERSION);
					//交易流水时间
					param2.put("transTime", GetDate.timestamptoStrYYYYMMDDHHMMSS(accountList.getCreateTime()+""));
					//	平台交易流水号
					param2.put("transId", accountList.getNid());
					//平台编号
					param2.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
					//原散标编号
					param2.put("sourceProductCode", borrow.getBorrowNid());
					//原散标名称
					param2.put("sourceProductName", borrow.getName());
					//交易类型
					param2.put("transType", "5");
					//交易方式
					param2.put("transPayment", "a");
					//交易类型描述
					param2.put("transTypeDec", CertTradeTypeEnum.getName("5"));
					//交易金额
					param2.put("transMoney", repayFee);
					//	交易日期
					param2.put("transDate", GetDate.times10toStrYYYYMMDD(accountList.getCreateTime()));
					//交易人员银行（或三方支付名称）
					param2.put("transBank", "江西银行");
					//用户标示哈希
					param2.put("userIdcardHash", certUser.get(0).getUserIdCardHash());
					//存管银行流水编号	
					param2.put("bankTransId", accountList.getSeqNo());
					//原产品信息编号
					param2.put("sourceFinancingCode", "-1");
					//原产品信息名称
					param2.put("sourceFinancingName", "-1");
					//债权编号
					param2.put("finClaimId", "-1");
					//转让项目编号
					param2.put("transferId", "-1");
					//	还款计划编号
					param2.put("replanId", period);
					list.add(param2);
				}
			}else{
				
				BorrowRepayPlanExample borrowRepayPlanExample=new BorrowRepayPlanExample();
				borrowRepayPlanExample.createCriteria().andRepayYestimeBetween(accountList.getCreateTime()-60+"", accountList.getCreateTime()+"").
				andBorrowNidEqualTo(accountList.getRemark());
				List<BorrowRepayPlan> borrowRepayPlans=borrowRepayPlanMapper.selectByExample(borrowRepayPlanExample);
				for (BorrowRepayPlan borrowRepayPlan : borrowRepayPlans) {
					BigDecimal repayCapitalYes=BigDecimal.ZERO;
					BigDecimal repayInterestYes=BigDecimal.ZERO;
					BigDecimal repayFee=BigDecimal.ZERO;
					repayCapitalYes=repayCapitalYes.add(borrowRepayPlan.getRepayCapitalYes());
					repayInterestYes=repayInterestYes.add(borrowRepayPlan.getRepayInterestYes());
					repayFee=repayFee.add(borrowRepayPlan.getRepayFee());
					String replanId=period+"-"+borrowRepayPlan.getRepayPeriod();
					
					Map<String, Object> param18 = new HashMap<String, Object>();
					Map<String, Object> param19 = new HashMap<String, Object>();
					Map<String, Object> param5 = new HashMap<String, Object>();
					/****************** 发18还款本金5交易手续费******************/
					//接口版本号
					param18.put("version", CertCallConstant.CERT_CALL_VERSION);
					//交易流水时间
					param18.put("transTime", GetDate.timestamptoStrYYYYMMDDHHMMSS(accountList.getCreateTime()+""));
					//	平台交易流水号
					param18.put("transId", accountList.getNid());
					//平台编号
					param18.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
					//原散标编号
					param18.put("sourceProductCode", borrow.getBorrowNid());
					//原散标名称
					param18.put("sourceProductName", borrow.getName());
					//交易类型
					param18.put("transType", "18");
					//交易方式
					param18.put("transPayment", "a");
					//交易类型描述
					param18.put("transTypeDec", CertTradeTypeEnum.getName("18"));
					//交易金额
					param18.put("transMoney", repayCapitalYes);
					//	交易日期
					param18.put("transDate", GetDate.times10toStrYYYYMMDD(accountList.getCreateTime()));
					//交易人员银行（或三方支付名称）
					param18.put("transBank", "江西银行");
					//用户标示哈希
					param18.put("userIdcardHash", certUser.get(0).getUserIdCardHash());
					//存管银行流水编号	
					param18.put("bankTransId", accountList.getSeqNo());
					//原产品信息编号
					param18.put("sourceFinancingCode", "-1");
					//原产品信息名称
					param18.put("sourceFinancingName", "-1");
					//债权编号
					param18.put("finClaimId", "-1");
					//转让项目编号
					param18.put("transferId", "-1");
					//	还款计划编号
					param18.put("replanId", replanId);
					list.add(param18);
					
					/****************** 发送19还款利息******************/

					//接口版本号
					param19.put("version", CertCallConstant.CERT_CALL_VERSION);
					//交易流水时间
					param19.put("transTime", GetDate.timestamptoStrYYYYMMDDHHMMSS(accountList.getCreateTime()+""));
					//	平台交易流水号
					param19.put("transId", accountList.getNid());
					//平台编号
					param19.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
					//原散标编号
					param19.put("sourceProductCode", borrow.getBorrowNid());
					//原散标名称
					param19.put("sourceProductName", borrow.getName());
					//交易类型
					param19.put("transType", "19");
					//交易方式
					param19.put("transPayment", "a");
					//交易类型描述
					param19.put("transTypeDec", CertTradeTypeEnum.getName("19"));
					//交易金额
					param19.put("transMoney", repayInterestYes);
					//	交易日期
					param19.put("transDate", GetDate.times10toStrYYYYMMDD(accountList.getCreateTime()));
					//交易人员银行（或三方支付名称）
					param19.put("transBank", "江西银行");
					//用户标示哈希
					param19.put("userIdcardHash", certUser.get(0).getUserIdCardHash());
					//存管银行流水编号	
					param19.put("bankTransId", accountList.getSeqNo());
					//原产品信息编号
					param19.put("sourceFinancingCode", "-1");
					//原产品信息名称
					param19.put("sourceFinancingName", "-1");
					//债权编号
					param19.put("finClaimId", "-1");
					//转让项目编号
					param19.put("transferId", "-1");
					//	还款计划编号
					param19.put("replanId", replanId);
					list.add(param19);	
					
					/****************** 发送5交易手续费******************/
					if(repayFee.compareTo(BigDecimal.ZERO)==1){
						//接口版本号
						param5.put("version", CertCallConstant.CERT_CALL_VERSION);
						//交易流水时间
						param5.put("transTime", GetDate.timestamptoStrYYYYMMDDHHMMSS(accountList.getCreateTime()+""));
						//	平台交易流水号
						param5.put("transId", accountList.getNid());
						//平台编号
						param5.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
						//原散标编号
						param5.put("sourceProductCode", borrow.getBorrowNid());
						//原散标名称
						param5.put("sourceProductName", borrow.getName());
						//交易类型
						param5.put("transType", "5");
						//交易方式
						param5.put("transPayment", "a");
						//交易类型描述
						param5.put("transTypeDec", CertTradeTypeEnum.getName("5"));
						//交易金额
						param5.put("transMoney", repayFee);
						//	交易日期
						param5.put("transDate", GetDate.times10toStrYYYYMMDD(accountList.getCreateTime()));
						//交易人员银行（或三方支付名称）
						param5.put("transBank", "江西银行");
						//用户标示哈希
						param5.put("userIdcardHash", certUser.get(0).getUserIdCardHash());
						//存管银行流水编号	
						param5.put("bankTransId", accountList.getSeqNo());
						//原产品信息编号
						param5.put("sourceFinancingCode", "-1");
						//原产品信息名称
						param5.put("sourceFinancingName", "-1");
						//债权编号
						param5.put("finClaimId", "-1");
						//转让项目编号
						param5.put("transferId", "-1");
						//	还款计划编号
						param5.put("replanId", replanId);
						list.add(param5);
					}
				}
			}
			
			
			
			
			break;
		//优惠券回款 上送10红包
		case "experience_profit":
		case "increase_interest_profit":
		case "cash_coupon_profit":

			CouponRecoverExample couponRecoverExample=new CouponRecoverExample();
			couponRecoverExample.createCriteria().andTransferIdEqualTo(accountList.getNid());
			List<CouponRecover> couponRecovers=couponRecoverMapper.selectByExample(couponRecoverExample);
			if(couponRecovers==null||couponRecovers.size()==0){
				return;
			}
			CouponRecover couponRecover=couponRecovers.get(0);
			BorrowTenderCpnExample borrowTenderCpnExample=new BorrowTenderCpnExample();
			borrowTenderCpnExample.createCriteria().andNidEqualTo(couponRecover.getTenderId());
			List<BorrowTenderCpn> borrowTenderCpnList=borrowTenderCpnMapper.selectByExample(borrowTenderCpnExample);
			if(borrowTenderCpnList==null||borrowTenderCpnList.size()==0){
				return;
			}
			BorrowTenderCpn borrowTenderCpn=borrowTenderCpnList.get(0);
			borrow= this.getBorrowByBorrowNid(borrowTenderCpn.getBorrowNid());
			couponRealTenderExample.createCriteria().andCouponTenderIdEqualTo(couponRecover.getTenderId());
			couponRealTenders=couponRealTenderMapper.selectByExample(couponRealTenderExample);
			if(couponRealTenders==null||couponRealTenders.size()==0){
				return;
			}
			
			//接口版本号
			param.put("version", CertCallConstant.CERT_CALL_VERSION);
			//交易流水时间
			param.put("transTime", GetDate.timestamptoStrYYYYMMDDHHMMSS(accountList.getCreateTime()+""));
			//	平台交易流水号
			param.put("transId", accountList.getNid());
			//平台编号
			param.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
			//原散标编号
			param.put("sourceProductCode", borrow==null?"-1":borrow.getBorrowNid());
			//原散标名称
			param.put("sourceProductName", borrow==null?"-1":borrow.getName());
			//交易类型
			param.put("transType", "10");
			//交易方式
			param.put("transPayment", "a");
			//交易类型描述
			param.put("transTypeDec", CertTradeTypeEnum.getName("10"));
			//交易金额
			param.put("transMoney", accountList.getAmount());
			//	交易日期
			param.put("transDate", GetDate.times10toStrYYYYMMDD(accountList.getCreateTime()));
			//交易人员银行（或三方支付名称）
			param.put("transBank", "江西银行");
			//用户标示哈希
			param.put("userIdcardHash", tool.idCardHash(usersInfo.getIdcard()));
			//存管银行流水编号	
			param.put("bankTransId", accountList.getSeqNo());
			//原产品信息编号
			param.put("sourceFinancingCode", "-1");
			//原产品信息名称
			param.put("sourceFinancingName", "-1");
			if(couponRealTenders.get(0).getRealTenderId()!=null&&couponRealTenders.get(0).getRealTenderId().length()>0){
				//债权编号
				param.put("finClaimId", couponRealTenders.get(0).getRealTenderId());
			}else{
				//债权编号
				param.put("finClaimId", "-1");
				
			}
			
			//转让项目编号
			param.put("transferId", "-1");
			//	还款计划编号
			param.put("replanId", borrowTenderCpn.getBorrowNid()+"-"+couponRecover.getRecoverPeriod());
			list.add(param);
			break;
		//投资收到还款  发送8赎回本金 发送9赎回利息
		case "tender_recover_yes":
		case "hjh_repay_balance":
		case "hjh_repay_frost":
			borrow= this.getBorrowByBorrowNid(accountList.getRemark());
			
			BigDecimal interest=BigDecimal.ZERO;
			BigDecimal capital=BigDecimal.ZERO;
			period=borrow.getBorrowNid();
			nid="";
			if("end".equals(borrow.getBorrowStyle())||"endday".equals(borrow.getBorrowStyle())){
				borrowRecoverExample=new BorrowRecoverExample();
				borrowRecoverExample.createCriteria().andRepayOrdidEqualTo(accountList.getNid());
				borrowRecovers=borrowRecoverMapper.selectByExample(borrowRecoverExample);
				if(borrowRecovers==null||borrowRecovers.size()==0){
					return;
				}

				interest=borrowRecovers.get(0).getRecoverInterestYes();
				capital=borrowRecovers.get(0).getRecoverCapitalYes();
				BorrowRecover borrowRecover=borrowRecovers.get(0);
				if(borrow.getPlanNid()!=null&&borrow.getPlanNid().length()>0){
					//智投
					HjhDebtCreditRepayExample example=new HjhDebtCreditRepayExample();
					example.createCriteria().
					andInvestOrderIdEqualTo(borrowRecover.getNid()).
					andBorrowNidEqualTo(borrowRecover.getBorrowNid()).
					andAssignRepayPeriodEqualTo(borrowRecover.getRecoverPeriod());
					List<HjhDebtCreditRepay> hjhDebtCreditRepays=hjhDebtCreditRepayMapper.selectByExample(example);
					for (HjhDebtCreditRepay hjhDebtCreditRepay : hjhDebtCreditRepays) {
						interest=interest.subtract(hjhDebtCreditRepay.getRepayInterestYes());
						capital=capital.subtract(hjhDebtCreditRepay.getRepayCapitalYes());
					}
				}else{
					//散标
					CreditRepayExample example=new CreditRepayExample();
					example.createCriteria().
					andCreditTenderNidEqualTo(borrowRecover.getNid()).
					andBidNidEqualTo(borrowRecover.getBorrowNid()).
					andRecoverPeriodEqualTo(borrowRecover.getRecoverPeriod());
					List<CreditRepay> creditRepays=creditRepayMapper.selectByExample(example);
					for (CreditRepay creditRepay : creditRepays) {
						interest=interest.subtract(creditRepay.getAssignRepayInterest());
						capital=capital.subtract(creditRepay.getAssignRepayCapital());
					}
				}
				period=period+"-"+borrowRecover.getRecoverPeriod();
				nid=borrowRecover.getNid();
			}else{
				borrowRecoverPlanExample=new BorrowRecoverPlanExample();
				borrowRecoverPlanExample.createCriteria().andRepayOrderIdEqualTo(accountList.getNid());
				borrowRecoverPlans = borrowRecoverPlanMapper.selectByExample(borrowRecoverPlanExample);
				if(borrowRecoverPlans==null||borrowRecoverPlans.size()==0){
					return;
				}
				BorrowRecoverPlan borrowRecoverPlan=borrowRecoverPlans.get(0);
				interest=borrowRecoverPlan.getRecoverInterestYes();
				capital=borrowRecoverPlan.getRecoverCapitalYes();
				if(borrow.getPlanNid()!=null&&borrow.getPlanNid().length()>0){
					//智投
					HjhDebtCreditRepayExample example=new HjhDebtCreditRepayExample();
					example.createCriteria().
					andInvestOrderIdEqualTo(borrowRecoverPlan.getNid()).
					andBorrowNidEqualTo(borrowRecoverPlan.getBorrowNid()).
					andAssignRepayPeriodEqualTo(borrowRecoverPlan.getRecoverPeriod());
					List<HjhDebtCreditRepay> hjhDebtCreditRepays=hjhDebtCreditRepayMapper.selectByExample(example);
					for (HjhDebtCreditRepay hjhDebtCreditRepay : hjhDebtCreditRepays) {
						interest=interest.subtract(hjhDebtCreditRepay.getRepayInterestYes());
						capital=capital.subtract(hjhDebtCreditRepay.getRepayCapitalYes());
					}
				}else{
					//散标
					CreditRepayExample example=new CreditRepayExample();
					example.createCriteria().
					andCreditTenderNidEqualTo(borrowRecoverPlan.getNid()).
					andBidNidEqualTo(borrowRecoverPlan.getBorrowNid()).
					andRecoverPeriodEqualTo(borrowRecoverPlan.getRecoverPeriod());
					List<CreditRepay> creditRepays=creditRepayMapper.selectByExample(example);
					for (CreditRepay creditRepay : creditRepays) {
						interest=interest.subtract(creditRepay.getAssignRepayInterest());
						capital=capital.subtract(creditRepay.getAssignRepayCapital());
					}
				}
				
				
				
				period=period+"-"+borrowRecoverPlan.getRecoverPeriod();
				nid=borrowRecoverPlan.getNid();
			}
			
			/****************** 发送8赎回本金******************/
			//接口版本号
			param.put("version", CertCallConstant.CERT_CALL_VERSION);
			//交易流水时间
			param.put("transTime", GetDate.timestamptoStrYYYYMMDDHHMMSS(accountList.getCreateTime()+""));
			//	平台交易流水号
			param.put("transId", accountList.getNid());
			//平台编号
			param.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
			//原散标编号
			param.put("sourceProductCode", borrow.getBorrowNid());
			//原散标名称
			param.put("sourceProductName", borrow.getName());
			//交易类型
			param.put("transType", "8");
			//交易方式
			param.put("transPayment", "a");
			//交易类型描述
			param.put("transTypeDec", CertTradeTypeEnum.getName("8"));
			//交易金额
			param.put("transMoney", capital);
			//	交易日期
			param.put("transDate", GetDate.times10toStrYYYYMMDD(accountList.getCreateTime()));
			//交易人员银行（或三方支付名称）
			param.put("transBank", "江西银行");
			//用户标示哈希
			param.put("userIdcardHash", tool.idCardHash(usersInfo.getIdcard()));
			//存管银行流水编号	
			param.put("bankTransId", accountList.getSeqNo());
			//原产品信息编号
			param.put("sourceFinancingCode", "-1");
			//原产品信息名称
			param.put("sourceFinancingName", "-1");
			//债权编号
			param.put("finClaimId", nid);
			//转让项目编号
			param.put("transferId", "-1");
			//	还款计划编号
			param.put("replanId", period);
			list.add(param);
			
			
			/****************** 发送9赎回利息******************/
			//接口版本号
			param1.put("version", CertCallConstant.CERT_CALL_VERSION);
			//交易流水时间
			param1.put("transTime", GetDate.timestamptoStrYYYYMMDDHHMMSS(accountList.getCreateTime()+""));
			//	平台交易流水号
			param1.put("transId", accountList.getNid());
			//平台编号
			param1.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
			//原散标编号
			param1.put("sourceProductCode", borrow.getBorrowNid());
			//原散标名称
			param1.put("sourceProductName", borrow.getName());
			//交易类型
			param1.put("transType", "9");
			//交易方式
			param1.put("transPayment", "a");
			//交易类型描述
			param1.put("transTypeDec", CertTradeTypeEnum.getName("9"));
			//交易金额
			param1.put("transMoney", interest);
			//	交易日期
			param1.put("transDate", GetDate.times10toStrYYYYMMDD(accountList.getCreateTime()));
			//交易人员银行（或三方支付名称）
			param1.put("transBank", "江西银行");
			//用户标示哈希
			param1.put("userIdcardHash", tool.idCardHash(usersInfo.getIdcard()));
			//存管银行流水编号	
			param1.put("bankTransId", accountList.getSeqNo());
			//原产品信息编号
			param1.put("sourceFinancingCode", "-1");
			//原产品信息名称
			param1.put("sourceFinancingName", "-1");
			//债权编号
			param1.put("finClaimId", nid);
			//转让项目编号
			param1.put("transferId", "-1");
			//	还款计划编号
			param1.put("replanId", period);
			list.add(param1);
			break;
		case "credit_tender_recover_yes":
		case "credit_tender_recover_forst":
			borrow= this.getBorrowByBorrowNid(accountList.getRemark());
			BigDecimal creditInterest=BigDecimal.ZERO;
			BigDecimal creditCapital=BigDecimal.ZERO;
			period=borrow.getBorrowNid();
			nid="";
			String transferId="";
			if(borrow.getPlanNid()!=null&&borrow.getPlanNid().length()>0){
				//智投
				HjhDebtCreditRepayExample example=new HjhDebtCreditRepayExample();
				example.createCriteria().andCreditRepayOrderIdEqualTo(accountList.getNid());
				List<HjhDebtCreditRepay> hjhDebtCreditRepays=hjhDebtCreditRepayMapper.selectByExample(example);
				if(hjhDebtCreditRepays==null||hjhDebtCreditRepays.size()==0){
					return;
				}
				creditInterest=hjhDebtCreditRepays.get(0).getReceiveInterestYes();
				creditCapital=hjhDebtCreditRepays.get(0).getReceiveCapitalYes();
				period=period+"-"+hjhDebtCreditRepays.get(0).getRepayPeriod();
				nid=hjhDebtCreditRepays.get(0).getInvestOrderId();
				transferId=hjhDebtCreditRepays.get(0).getCreditNid()+"";
			}else{
				//散标
				CreditRepayExample example=new CreditRepayExample();
				example.createCriteria().andCreditRepayOrderIdEqualTo(accountList.getNid());
				List<CreditRepay> creditRepays=creditRepayMapper.selectByExample(example);
				if(creditRepays==null||creditRepays.size()==0){
					return;
				}
				creditInterest=creditRepays.get(0).getAssignRepayInterest();
				creditCapital=creditRepays.get(0).getAssignRepayCapital();
				period=period+"-"+creditRepays.get(0).getRecoverPeriod();
				nid=creditRepays.get(0).getCreditTenderNid();
				transferId=creditRepays.get(0).getCreditNid()+"";
			}
			
			/****************** 发送8赎回本金******************/
			//接口版本号
			param.put("version", CertCallConstant.CERT_CALL_VERSION);
			//交易流水时间
			param.put("transTime", GetDate.timestamptoStrYYYYMMDDHHMMSS(accountList.getCreateTime()+""));
			//	平台交易流水号
			param.put("transId", accountList.getNid());
			//平台编号
			param.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
			//原散标编号
			param.put("sourceProductCode", borrow.getBorrowNid());
			//原散标名称
			param.put("sourceProductName", borrow.getName());
			//交易类型
			param.put("transType", "8");
			//交易方式
			param.put("transPayment", "a");
			//交易类型描述
			param.put("transTypeDec", CertTradeTypeEnum.getName("8"));
			//交易金额
			param.put("transMoney", creditCapital);
			//	交易日期
			param.put("transDate", GetDate.times10toStrYYYYMMDD(accountList.getCreateTime()));
			//交易人员银行（或三方支付名称）
			param.put("transBank", "江西银行");
			//用户标示哈希
			param.put("userIdcardHash", tool.idCardHash(usersInfo.getIdcard()));
			//存管银行流水编号	
			param.put("bankTransId", accountList.getSeqNo());
			//原产品信息编号
			param.put("sourceFinancingCode", "-1");
			//原产品信息名称
			param.put("sourceFinancingName", "-1");
			//债权编号
			param.put("finClaimId", nid);
			//转让项目编号
			param.put("transferId", transferId);
			//	还款计划编号
			param.put("replanId", period);
			list.add(param);
			/****************** 发送9赎回利息******************/
			//接口版本号
			param1.put("version", CertCallConstant.CERT_CALL_VERSION);
			//交易流水时间
			param1.put("transTime", GetDate.timestamptoStrYYYYMMDDHHMMSS(accountList.getCreateTime()+""));
			//	平台交易流水号
			param1.put("transId", accountList.getNid());
			//平台编号
			param1.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
			//原散标编号
			param1.put("sourceProductCode", borrow.getBorrowNid());
			//原散标名称
			param1.put("sourceProductName", borrow.getName());
			//交易类型
			param1.put("transType", "9");
			//交易方式
			param1.put("transPayment", "a");
			//交易类型描述
			param1.put("transTypeDec", CertTradeTypeEnum.getName("9"));
			//交易金额
			param1.put("transMoney", creditInterest);
			//	交易日期
			param1.put("transDate", GetDate.times10toStrYYYYMMDD(accountList.getCreateTime()));
			//交易人员银行（或三方支付名称）
			param1.put("transBank", "江西银行");
			//用户标示哈希
			param1.put("userIdcardHash", tool.idCardHash(usersInfo.getIdcard()));
			//存管银行流水编号	
			param1.put("bankTransId", accountList.getSeqNo());
			//原产品信息编号
			param1.put("sourceFinancingCode", "-1");
			//原产品信息名称
			param1.put("sourceFinancingName", "-1");
			//债权编号
			param1.put("finClaimId", nid);
			//转让项目编号
			param1.put("transferId", transferId);
			//	还款计划编号
			param1.put("replanId", period);
			list.add(param1);
			
			break;
		//承接债权 散标债转发送17承接 
		case "creditassign":
			creditTenderExample=new CreditTenderExample();
			creditTenderExample.createCriteria().andAssignNidEqualTo(accountList.getNid());
			creditTenders=creditTenderMapper.selectByExample(creditTenderExample);
			if(creditTenders==null||creditTenders.size()==0){
				return;
			}
			borrow= this.getBorrowByBorrowNid(creditTenders.get(0).getBidNid());
			//接口版本号
			param.put("version", CertCallConstant.CERT_CALL_VERSION);
			//交易流水时间
			param.put("transTime", GetDate.timestamptoStrYYYYMMDDHHMMSS(accountList.getCreateTime()+""));
			//	平台交易流水号
			param.put("transId", accountList.getNid());
			//平台编号
			param.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
			//原散标编号
			param.put("sourceProductCode", borrow.getBorrowNid());
			//原散标名称
			param.put("sourceProductName", borrow.getName());
			//交易类型
			param.put("transType", "17");
			//交易方式
			param.put("transPayment", "a");
			//交易类型描述
			param.put("transTypeDec", CertTradeTypeEnum.getName("17"));
			//交易金额
			param.put("transMoney", accountList.getAmount());
			//	交易日期
			param.put("transDate", GetDate.times10toStrYYYYMMDD(accountList.getCreateTime()));
			//交易人员银行（或三方支付名称）
			param.put("transBank", "江西银行");
			//用户标示哈希
			param.put("userIdcardHash", tool.idCardHash(usersInfo.getIdcard()));
			//存管银行流水编号	
			param.put("bankTransId", accountList.getSeqNo());
			//原产品信息编号
			param.put("sourceFinancingCode", "-1");
			//原产品信息名称
			param.put("sourceFinancingName", "-1");
			//债权编号
			param.put("finClaimId", creditTenders.get(0).getCreditTenderNid());
			//转让项目编号
			param.put("transferId", creditTenders.get(0).getCreditNid());
			//	还款计划编号
			param.put("replanId", "-1");
			list.add(param);
			
			break;
		//自动承接债权 计划自动承接债转发送17承接 
		case "accede_assign":
			hjhDebtCreditTenderExample =new HjhDebtCreditTenderExample();
			hjhDebtCreditTenderExample.createCriteria().andAssignOrderIdEqualTo(accountList.getNid());
			hjhDebtCreditTenders=hjhDebtCreditTenderMapper.selectByExample(hjhDebtCreditTenderExample);
			if(hjhDebtCreditTenders==null||hjhDebtCreditTenders.size()==0){
				return;
			}
			borrow= this.getBorrowByBorrowNid(hjhDebtCreditTenders.get(0).getBorrowNid());
			//接口版本号
			param.put("version", CertCallConstant.CERT_CALL_VERSION);
			//交易流水时间
			param.put("transTime", GetDate.timestamptoStrYYYYMMDDHHMMSS(accountList.getCreateTime()+""));
			//	平台交易流水号
			param.put("transId", accountList.getNid());
			//平台编号
			param.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
			//原散标编号
			param.put("sourceProductCode", borrow.getBorrowNid());
			//原散标名称
			param.put("sourceProductName", borrow.getName());
			//交易类型
			param.put("transType", "17");
			//交易方式
			param.put("transPayment", "a");
			//交易类型描述
			param.put("transTypeDec", CertTradeTypeEnum.getName("17"));
			//交易金额
			param.put("transMoney", accountList.getAmount());
			//	交易日期
			param.put("transDate", GetDate.times10toStrYYYYMMDD(accountList.getCreateTime()));
			//交易人员银行（或三方支付名称）
			param.put("transBank", "江西银行");
			//用户标示哈希
			param.put("userIdcardHash", tool.idCardHash(usersInfo.getIdcard()));
			//存管银行流水编号	
			param.put("bankTransId", accountList.getSeqNo());
			//原产品信息编号
			param.put("sourceFinancingCode", "-1");
			//原产品信息名称
			param.put("sourceFinancingName", "-1");
			//债权编号
			param.put("finClaimId", hjhDebtCreditTenders.get(0).getInvestOrderId());
			//转让项目编号
			param.put("transferId", hjhDebtCreditTenders.get(0).getCreditNid());
			//	还款计划编号
			param.put("replanId", "-1");
			list.add(param);
			break;
		//智投清算（转让）  计划 发送11转让  发送5交易手续费（转让服务费）
		case "liquidates_sell":
			hjhDebtCreditTenderExample =new HjhDebtCreditTenderExample();
			hjhDebtCreditTenderExample.createCriteria().andAssignOrderIdEqualTo(accountList.getNid());
			hjhDebtCreditTenders=hjhDebtCreditTenderMapper.selectByExample(hjhDebtCreditTenderExample);
			if(hjhDebtCreditTenders==null||hjhDebtCreditTenders.size()==0){
				return;
			}
			borrow= this.getBorrowByBorrowNid(hjhDebtCreditTenders.get(0).getBorrowNid());
			//接口版本号
			param.put("version", CertCallConstant.CERT_CALL_VERSION);
			//交易流水时间
			param.put("transTime", GetDate.timestamptoStrYYYYMMDDHHMMSS(accountList.getCreateTime()+""));
			//	平台交易流水号
			param.put("transId", accountList.getNid());
			//平台编号
			param.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
			//原散标编号
			param.put("sourceProductCode", borrow.getBorrowNid());
			//原散标名称
			param.put("sourceProductName", borrow.getName());
			//交易类型
			param.put("transType", "11");
			//交易方式
			param.put("transPayment", "a");
			//交易类型描述
			param.put("transTypeDec", CertTradeTypeEnum.getName("11"));
			//交易金额
			param.put("transMoney", accountList.getAmount());
			//	交易日期
			param.put("transDate", GetDate.times10toStrYYYYMMDD(accountList.getCreateTime()));
			//交易人员银行（或三方支付名称）
			param.put("transBank", "江西银行");
			//用户标示哈希
			param.put("userIdcardHash", tool.idCardHash(usersInfo.getIdcard()));
			//存管银行流水编号	
			param.put("bankTransId", accountList.getSeqNo());
			//债权编号
			param.put("finClaimId", hjhDebtCreditTenders.get(0).getInvestOrderId());
			//转让项目编号
			param.put("transferId", hjhDebtCreditTenders.get(0).getCreditNid());
			//	还款计划编号
			param.put("replanId", "-1");
			list.add(param);
			
			
			if(hjhDebtCreditTenders.get(0).
					getAssignServiceFee().compareTo(BigDecimal.ZERO)==1){
				//接口版本号
				param1.put("version", CertCallConstant.CERT_CALL_VERSION);
				//交易流水时间
				param1.put("transTime", GetDate.timestamptoStrYYYYMMDDHHMMSS(accountList.getCreateTime()+""));
				//	平台交易流水号
				param1.put("transId", accountList.getNid());
				//平台编号
				param1.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
				//原散标编号
				param1.put("sourceProductCode", borrow.getBorrowNid());
				//原散标名称
				param1.put("sourceProductName", borrow.getName());
				//交易类型
				param1.put("transType", "5");
				//交易方式
				param1.put("transPayment", "a");
				//交易类型描述
				param1.put("transTypeDec", CertTradeTypeEnum.getName("5"));
				//交易金额
				param1.put("transMoney", hjhDebtCreditTenders.get(0).getAssignServiceFee());
				//	交易日期
				param1.put("transDate", GetDate.times10toStrYYYYMMDD(accountList.getCreateTime()));
				//交易人员银行（或三方支付名称）
				param1.put("transBank", "江西银行");
				//用户标示哈希
				param1.put("userIdcardHash", tool.idCardHash(usersInfo.getIdcard()));
				//存管银行流水编号	
				param1.put("bankTransId", accountList.getSeqNo());
				//原产品信息编号
				param1.put("sourceFinancingCode", "-1");
				//原产品信息名称
				param1.put("sourceFinancingName", "-1");
				//债权编号
				param1.put("finClaimId", hjhDebtCreditTenders.get(0).getInvestOrderId());
				//转让项目编号
				param1.put("transferId", hjhDebtCreditTenders.get(0).getCreditNid());
				//	还款计划编号
				param1.put("replanId", "-1");
				list.add(param1);
			}
			
			break;
		//出让债权 计划发送11转让  发送5交易手续费（转让服务费）
		case "creditsell":
			creditTenderExample=new CreditTenderExample();
			creditTenderExample.createCriteria().andAssignNidEqualTo(accountList.getNid());
			creditTenders=creditTenderMapper.selectByExample(creditTenderExample);
			if(creditTenders==null||creditTenders.size()==0){
				return;
			}
			/****************** 发送11转让 ******************/
			borrow= this.getBorrowByBorrowNid(creditTenders.get(0).getBidNid());
			//接口版本号
			param.put("version", CertCallConstant.CERT_CALL_VERSION);
			//交易流水时间
			param.put("transTime", GetDate.timestamptoStrYYYYMMDDHHMMSS(accountList.getCreateTime()+""));
			//	平台交易流水号
			param.put("transId", accountList.getNid());
			//平台编号
			param.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
			//原散标编号
			param.put("sourceProductCode", borrow.getBorrowNid());
			//原散标名称
			param.put("sourceProductName", borrow.getName());
			//交易类型
			param.put("transType", "11");
			//交易方式
			param.put("transPayment", "a");
			//交易类型描述
			param.put("transTypeDec", CertTradeTypeEnum.getName("11"));
			//交易金额
			param.put("transMoney", accountList.getAmount());
			//	交易日期
			param.put("transDate", GetDate.times10toStrYYYYMMDD(accountList.getCreateTime()));
			//交易人员银行（或三方支付名称）
			param.put("transBank", "江西银行");
			//用户标示哈希
			param.put("userIdcardHash", tool.idCardHash(usersInfo.getIdcard()));
			//存管银行流水编号	
			param.put("bankTransId", accountList.getSeqNo());
			//原产品信息编号
			param.put("sourceFinancingCode", "-1");
			//原产品信息名称
			param.put("sourceFinancingName", "-1");
			//债权编号
			param.put("finClaimId", creditTenders.get(0).getCreditTenderNid());
			//转让项目编号
			param.put("transferId", creditTenders.get(0).getCreditNid());
			//	还款计划编号
			param.put("replanId", "-1");
			list.add(param);
			
			/****************** 发送5交易手续费******************/
			if(creditTenders.get(0).getCreditFee().compareTo(BigDecimal.ZERO)==1){
				//接口版本号
				param1.put("version", CertCallConstant.CERT_CALL_VERSION);
				//交易流水时间
				param1.put("transTime", GetDate.timestamptoStrYYYYMMDDHHMMSS(accountList.getCreateTime()+""));
				//	平台交易流水号
				param1.put("transId", accountList.getNid());
				//平台编号
				param1.put("sourceCode", CertCallConstant.CERT_SOURCE_CODE);
				//原散标编号
				param1.put("sourceProductCode", borrow.getBorrowNid());
				//原散标名称
				param1.put("sourceProductName", borrow.getName());
				//交易类型
				param1.put("transType", "5");
				//交易方式
				param1.put("transPayment", "a");
				//交易类型描述
				param1.put("transTypeDec", CertTradeTypeEnum.getName("5"));
				//交易金额
				param1.put("transMoney", creditTenders.get(0).getCreditFee());
				//	交易日期
				param1.put("transDate", GetDate.times10toStrYYYYMMDD(accountList.getCreateTime()));
				//交易人员银行（或三方支付名称）
				param1.put("transBank", "江西银行");
				//用户标示哈希
				param1.put("userIdcardHash", tool.idCardHash(usersInfo.getIdcard()));
				//存管银行流水编号	
				param1.put("bankTransId", accountList.getSeqNo());
				//原产品信息编号
				param1.put("sourceFinancingCode", "-1");
				//原产品信息名称
				param1.put("sourceFinancingName", "-1");
				//债权编号
				param1.put("finClaimId", creditTenders.get(0).getCreditTenderNid());
				//转让项目编号
				param1.put("transferId", creditTenders.get(0).getCreditNid());
				//	还款计划编号
				param1.put("replanId", "-1");
				list.add(param1);
			}
			break;
		default:
			break;
		}
	}
}

