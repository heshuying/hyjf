package com.hyjf.batch;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hyjf.batch.debtTransfer.DebtTransferBean;
import com.hyjf.batch.result.account.UserAccountResultBean;
import com.hyjf.batch.result.debtTransfer.DebtTransferResultBean;
import com.hyjf.batch.result.subjectTransfer.SubjectTransferResultBean;
import com.hyjf.batch.sigtranTransfer.SigtranTransferBean;
import com.hyjf.batch.subject.transfer.SubjectTransferBean;
import com.hyjf.batch.user.account.UserAccountBean;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.customize.CustomizeMapper;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BankOpenAccountExample;
import com.hyjf.mybatis.model.auto.BorrowRecover;
import com.hyjf.mybatis.model.auto.BorrowRecoverExample;
import com.hyjf.mybatis.model.auto.BorrowRecoverPlan;
import com.hyjf.mybatis.model.auto.BorrowRecoverPlanExample;
import com.hyjf.mybatis.model.auto.BorrowTender;
import com.hyjf.mybatis.model.auto.BorrowTenderExample;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.CreditRepay;
import com.hyjf.mybatis.model.auto.CreditRepayExample;
import com.hyjf.mybatis.model.auto.CreditTender;
import com.hyjf.mybatis.model.auto.CreditTenderExample;
import com.hyjf.mybatis.model.auto.DataErrorAccount;
import com.hyjf.mybatis.model.auto.DataErrorAccountExample;
import com.hyjf.mybatis.model.auto.DataErrorDebt;
import com.hyjf.mybatis.model.auto.DataErrorDebtExample;
import com.hyjf.mybatis.model.auto.DataErrorSubject;
import com.hyjf.mybatis.model.auto.DataErrorSubjectExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;

@Service
public class BaseServiceImpl extends CustomizeMapper implements BaseService {

	private static final Logger LOG = LoggerFactory.getLogger(BaseServiceImpl.class);
	
	@Override
	public Users getUsers(Integer userId) {
		return usersMapper.selectByPrimaryKey(userId);
	}

	/**
	 * 根据用户ID取得用户信息
	 *
	 * @param userId
	 * @return
	 */
	@Override
	public UsersInfo getUsersInfoByUserId(Integer userId) {
		if (userId != null) {
			UsersInfoExample example = new UsersInfoExample();
			example.createCriteria().andUserIdEqualTo(userId);
			List<UsersInfo> usersInfoList = this.usersInfoMapper.selectByExample(example);
			if (usersInfoList != null && usersInfoList.size() > 0) {
				return usersInfoList.get(0);
			}
		}
		return null;
	}

	/**
	 * 插入开户表
	 * 
	 * @param resultBean
	 * @return
	 * @author Michael
	 */

	@Override
	public boolean insertBankOpenAccount(UserAccountResultBean resultBean) {
		BankOpenAccount record = new BankOpenAccount();
		String userId = resultBean.getAppId();// 用户id
		// 用户ID为空
		if (StringUtils.isEmpty(userId)) {
			return false;
		}
		// 用户已开户
		if (isExistAccount(Integer.valueOf(userId))) {
			LOG.info("用户已开户抛出数据,用户id：" + Integer.valueOf(userId));
			return false;
		}
		// 用户信息
		Users user = this.getUsers(Integer.valueOf(userId));
		record.setAccount(resultBean.getAccountId());
		record.setUserId(user.getUserId());
		record.setUserName(user.getUsername());
		record.setCreateTime(GetDate.getDate());
		record.setCreateUserId(user.getUserId());
		record.setCreateUserName(user.getUsername());
		this.bankOpenAccountMapper.insertSelective(record);
		// 更新用户已开户
		user.setOpenAccount(1);// 已开户
		user.setBankOpenAccount(1);//银行开户
		user.setBankAccountEsb(Integer.valueOf(CustomConstants.CLIENT_PC));//PC开户
		this.usersMapper.updateByPrimaryKeySelective(user);
		return true;
	}

	/**
	 * 判断用户是否已经开户
	 * 
	 * @param userId
	 * @return
	 */
	public boolean isExistAccount(Integer userId) {
		BankOpenAccountExample example = new BankOpenAccountExample();
		BankOpenAccountExample.Criteria cra = example.createCriteria();
		cra.andUserIdEqualTo(userId);
		int count = this.bankOpenAccountMapper.countByExample(example);
		if (count > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 批量开户异常处理(平台过滤)
	 * 
	 * @param userAccountBean
	 * @param errorCode
	 *            1身份证号为空 2姓名为空 3手机号为空 4身份证15位 不符合规范 5用户未满18岁
	 * @author Michael
	 */
	@Override
	public void insertOpenAccountError(UserAccountBean userAccountBean, int errorCode) {
		// 判断用户是否存在异常
		if (isExistAccountError(Integer.valueOf(userAccountBean.getUserId()))) {
			return;
		}
		DataErrorAccount record = new DataErrorAccount();
		record.setUserId(Integer.valueOf(userAccountBean.getUserId()));
		record.setIdCard(userAccountBean.getIdCard());
		record.setName(userAccountBean.getName());
		record.setEmail(userAccountBean.getEmail());
		record.setMobile(userAccountBean.getMobile());
		record.setErrorType(1);// 平台过滤
		switch (errorCode) {
		case 1:
			record.setErrorDesc("身份证号为空 ");
			break;
		case 2:
			record.setErrorDesc("姓名为空 ");
			break;
		case 3:
			record.setErrorDesc("手机号为空");
			break;
		case 4:
			record.setErrorDesc("身份证15位 不符合规范");
			break;
		case 5:
			record.setErrorDesc("用户未满18岁 ");
			break;
		default:
			record.setErrorDesc("");
			break;
		}
		record.setCreateTime(GetDate.getDate());
		dataErrorAccountMapper.insertSelective(record);
	}

	/**
	 * 批量开户异常处理(接口返回过滤)
	 * 
	 * @param userAccountBean
	 */
	@Override
	public void insertOpenAccountResultError(UserAccountResultBean userAccountResultBean, String error) {
		// 判断用户是否存在异常
		if (isExistAccountError(Integer.valueOf(userAccountResultBean.getAppId()))) {
			LOG.info("已存在error表抛出数据，用户id：" + Integer.valueOf(userAccountResultBean.getAppId()));
			return;
		}
		DataErrorAccount record = new DataErrorAccount();
		record.setUserId(Integer.valueOf(userAccountResultBean.getAppId()));
		record.setIdCard(userAccountResultBean.getIdCard());
		record.setName(userAccountResultBean.getName());
		record.setMobile(userAccountResultBean.getMobile());
		record.setErrorType(2);// 接口返回过滤
		record.setResultFlag(userAccountResultBean.getFlag());
		record.setErrorCode(userAccountResultBean.getErrCode());
		switch (record.getErrorCode()) {
		case "000":
			error = "成功";
			break;
		case "101":
			error = "证件类型或证件编号非法";
			break;
		case "102":
			error = "姓名字段不能为空";
			break;
		case "103":
			error = "姓名字段非法";
			break;
		case "104":
			error = "性别字段不能为空";
			break;
		case "105":
			error = "性别字段非法";
			break;
		case "106":
			error = "手机号码不能为空或已被其他客户使用";
			break;
		case "107":
			error = "该手机号已被使用";
			break;
		case "108":
			error = "该客户未满18岁或重复开户";
			break;
		case "109":
			error = "请求方用户ID不能为空";
			break;
		default:
			break;
		}
		record.setAccountId(userAccountResultBean.getAccountId());
		record.setErrorDesc(error);
		record.setUpdateTime(GetDate.getDate());
		dataErrorAccountMapper.insertSelective(record);
	}

	/**
	 * 判断用户是否已存异常数据
	 * 
	 * @param userId
	 * @return
	 */
	public boolean isExistAccountError(Integer userId) {
		DataErrorAccountExample example = new DataErrorAccountExample();
		DataErrorAccountExample.Criteria cra = example.createCriteria();
		cra.andUserIdEqualTo(userId);
		int count = this.dataErrorAccountMapper.countByExample(example);
		if (count > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 批量标的迁移入库
	 * 
	 * @param SubjectTransferBean
	 * @author Libin
	 */
	@Override
	public void insertSubjectTransferData(SubjectTransferBean readBean) {
		// 判断标的是否已入库
		if (isExistSubjectTransferData(Integer.valueOf(readBean.getP2pProdId()))) {
			return;
		}
		DataErrorSubject record = new DataErrorSubject();
		record.setBorrowId(Integer.valueOf(readBean.getP2pProdId()));
		record.setBorrowNid(readBean.getBorrowNid());
		record.setBorrowDesc(readBean.getProdDesc());
		record.setBorrowAccountId(readBean.getBorrowerElecAcc());
		record.setAmount(readBean.getAmount());
		record.setPaymentType(readBean.getPaymentStyle());
		record.setLoanTerm(readBean.getPeriod());
		record.setBorrowApr(readBean.getExpectAnnualRate());
		record.setGuaranteeAccountId(readBean.getGuarantorElecAcc());
		record.setRaiseDate(readBean.getRaiseDate());
		record.setRaiseEndDate(readBean.getRaiseEndDate());
		record.setRespCode("");
		record.setErrorDesc("");
		record.setCreateTime(GetDate.getDate());
		dataErrorSubjectMapper.insertSelective(record);
	}

	/**
	 * 判断标的是否已入库
	 * 
	 * @param borrowId
	 * @return
	 */
	public boolean isExistSubjectTransferData(Integer borrowId) {
		DataErrorSubjectExample example = new DataErrorSubjectExample();
		DataErrorSubjectExample.Criteria cra = example.createCriteria();
		cra.andBorrowIdEqualTo(borrowId);
		int count = this.dataErrorSubjectMapper.countByExample(example);
		if (count > 0) {
			return true;
		}
		return false;
	}

	
	/**
	 * 更新标的迁移结果
	 * @param resultBean
	 * @param error
	 * @author Michael
	 */
		
	@Override
	public void updateSubjectTransferData(SubjectTransferResultBean resultBean) {
		DataErrorSubjectExample example = new DataErrorSubjectExample();
		DataErrorSubjectExample.Criteria cra = example.createCriteria();
		cra.andBorrowIdEqualTo(Integer.valueOf(resultBean.getBorrowId()));
		List<DataErrorSubject> list = this.dataErrorSubjectMapper.selectByExample(example);
		if(list != null && list.size() > 0 ){
			DataErrorSubject record = list.get(0);
			record.setRespCode(resultBean.getRespCode());
			//错误消息
			String errorMsg = "";
			switch (record.getRespCode()) {
			case "00":
				errorMsg ="成功";
				break;
			case "01":
				errorMsg ="银行号不允许为空";
				break;
			case "02":
				errorMsg ="批次号不允许为空";
				break;
			case "03":
				errorMsg ="标的编号不允许为空";
				break;
			case "04":
				errorMsg ="标的描述不允许为空";
				break;
			case "05":
				errorMsg ="借款人电子账号不允许为空";
				break;
			default:
				errorMsg ="返回码为："+record.getRespCode()+"请对照返回码表查看错误信息";
				break;
			}
			record.setErrorDesc(errorMsg);
			record.setUpdateTime(GetDate.getDate());
			//更新结果
			this.dataErrorSubjectMapper.updateByPrimaryKeySelective(record);
			
			if(record.getRespCode().equals("00")){
				BorrowWithBLOBs borrow = this.borrowMapper.selectByPrimaryKey(Integer.valueOf(resultBean.getBorrowId()));
				 if(borrow != null){
					 //银行募集开始时间
					 borrow.setBankRaiseStartDate(record.getRaiseDate());
					 //银行募集结束时间
					 borrow.setBankRaiseEndDate(record.getRaiseEndDate());
					 borrow.setBankRegistDays(5);
					 //银行项目期限
					 borrow.setBankBorrowDays(record.getLoanTerm());
					 //备案成功
					 borrow.setRegistStatus(2);
					 //备案时间
					 borrow.setRegistTime(GetDate.getDate());
					 //更新borrow表备案字段
					 this.borrowMapper.updateByPrimaryKeyWithBLOBs(borrow);
				 }
			}
		}
	}
	
	/**
	 * 债权转移请求异常处理(平台过滤)
	 * @param DebtTransferBean
	 * @param errorCode  
	 */
	@Override
	public void insertDebtTransferError(DebtTransferBean readBean) {
		//重复记录不要插入
		DataErrorDebtExample example = new DataErrorDebtExample();
		DataErrorDebtExample.Criteria cra = example.createCriteria();
		cra.andSerialNumEqualTo(readBean.getSerialNum());
		List<DataErrorDebt> list = this.dataErrorDebtMapper.selectByExample(example);
		if(list != null && list.size() > 0){
			return;
		}
		DataErrorDebt record = new DataErrorDebt();
		record.setBankId(readBean.getBankId());
		record.setBatchId(readBean.getBatchId());
		record.setDebtHolderAcc(readBean.getDebtHolderAcc());
		record.setProdIssuer(readBean.getProdIssuer());
		record.setBorrowId(Integer.valueOf(readBean.getProdNum()));
		record.setSerialNum(readBean.getSerialNum());
		record.setAmount(readBean.getAmount());
		record.setDebtObtDate(readBean.getDebtObtDate());
		record.setInterestDate(readBean.getIntStDate());
		record.setIntPayStyle(readBean.getIntStStyle()); 
		record.setIntPayDate(readBean.getIntPaydate());  
		record.setEndDate(readBean.getEndDate());
		record.setExpectAnualRate(readBean.getExpectAnualRate());
		record.setCurrType(readBean.getCurrType());
		record.setRevers(readBean.getBorrowNid());//标号后加存到保留域
		record.setCreateTime(GetDate.getDate());
		record.setOrderId(readBean.getOrderId());
		record.setTenderType(Integer.valueOf(readBean.getType()));
		
		//add new 4
		record.setBorrowUserId(readBean.getBorrowUserId());
		record.setTenUserId(readBean.getTenUserId());
		record.setInterestWait(readBean.getInterestWait());
		record.setInterestPaid(readBean.getInterestPaid());
		
		dataErrorDebtMapper.insertSelective(record);
		
	}


	/**
	 * 签约关系转移请求异常处理（平台过滤）
	 * @param SigtranTransferBean
	 * @param errorCode
	 * */
	public void insertSigtranTransferError(SigtranTransferBean sigtranTransferBean) {
		// 等待签约关系转移请求建表，生成后，获取实体，换成自己的，在插入表中
		
		/*DataErrortranSigtran record = new DataErrortranSigtran();
		dataErrorDebtMapper.insertSelective(record);*/
		
	}


	@Override
	public void updateDebtTransferData(DebtTransferResultBean resultBean) {
		DataErrorDebtExample example = new DataErrorDebtExample();
		DataErrorDebtExample.Criteria cra = example.createCriteria();
		cra.andSerialNumEqualTo(resultBean.getSerialNum());
		List<DataErrorDebt> list = this.dataErrorDebtMapper.selectByExample(example);
	
		if(list != null && list.size() > 0 ){
			DataErrorDebt record = list.get(0);
			
			//1.持卡人姓名
			record.setName(resultBean.getHolderName());
			//2.处理日期(暂时不存)
			
			//3.处理响应码
			record.setRetCode(resultBean.getRespCode());
			//4.申请授权码
			record.setAuthCode(resultBean.getAuthCode());
			//5.保留域
//			record.setRevers(resultBean.getRevers());
			//6.错误描述
			switch (record.getRetCode()) {
			case "00":
				record.setErrorDesc("成功");
				break;
			case "14":
				record.setErrorDesc("无效账号");
				break;
			case "93":
				record.setErrorDesc("姓名校验错误");
				break;
			case "99":
				record.setErrorDesc("其他错误原因");
				break;
			default:
				record.setErrorDesc("未知错误");
				break;
			}
			record.setUpdateTime(GetDate.getDate());
			//更新底子表
			this.dataErrorDebtMapper.updateByPrimaryKey(record);
			
			if(record.getRetCode().equals("00")){
				//from borrow_tender
				if(record.getTenderType() == 1){
					//更新borrowTender表
					BorrowTender borrowTender = getBorrowTenderByOrderId(record.getOrderId());
					if(borrowTender != null){
						borrowTender.setAuthCode(record.getAuthCode());
						this.borrowTenderMapper.updateByPrimaryKeySelective(borrowTender);
					}
					//更新borrowRecover表
					BorrowRecover borrowRecover = getBorrowRecoverByOrderId(record.getOrderId());
					if(borrowRecover != null){
						borrowRecover.setAuthCode(record.getAuthCode());
						this.borrowRecoverMapper.updateByPrimaryKeySelective(borrowRecover);
					}
					//更新borrowRecoverplan表
					BorrowRecoverPlanExample borrowRecoverPlanExample = new BorrowRecoverPlanExample();
					borrowRecoverPlanExample.createCriteria().andNidEqualTo(record.getOrderId());
					BorrowRecoverPlan plan = new BorrowRecoverPlan();
					plan.setAuthCode(record.getAuthCode());
					this.borrowRecoverPlanMapper.updateByExampleSelective(plan, borrowRecoverPlanExample);
					
				} //from credit_tender
				else if(record.getTenderType() == 2){
					//更新creditTender表
					CreditTender creditTender = getCreditTenderByOrderId(record.getOrderId());
					if(creditTender != null){
						creditTender.setAuthCode(record.getAuthCode());
						this.creditTenderMapper.updateByPrimaryKeySelective(creditTender);
					}
					//更新creditRepay表
					List<CreditRepay> creditRepayList = getCreditRepayByOrderId(record.getOrderId());
					if(creditRepayList != null){
						for(CreditRepay creditRepay : creditRepayList){
							creditRepay.setAuthCode(record.getAuthCode());
							this.creditRepayMapper.updateByPrimaryKeySelective(creditRepay);
						}
					}
				}
				/**
				 * 更新huiyingdai_account 表
				 * 借款人
				 * 出借人
				 */
				//银行待还本金
				BigDecimal borrowCapital = record.getAmount();
				//银行待还利息(待还减去已还)
				BigDecimal borrowInterest = record.getInterestWait().subtract(record.getInterestPaid());
				
				//借款人
				Account borrowAccount = getAccountByUserId(record.getBorrowUserId());
				if(borrowAccount != null){
					borrowAccount.setBankWaitCapital(borrowAccount.getBankWaitCapital().add(borrowCapital));//银行待还本金
					borrowAccount.setBankWaitInterest(borrowAccount.getBankWaitInterest().add(borrowInterest));//银行待还利息(待还减去已还)
					borrowAccount.setBankWaitRepay(borrowAccount.getBankWaitRepay().add(borrowCapital).add(borrowInterest));//待还总额
					this.accountMapper.updateByPrimaryKeySelective(borrowAccount);
				}
				//出借人
				Account tenderAccount = getAccountByUserId(record.getTenUserId());
				if(tenderAccount != null){
					//tenderAccount.setBankInvestSum(tenderAccount.getBankInvestSum().add(borrowCapital));//银行累计出借
					tenderAccount.setBankAwait(tenderAccount.getBankAwait().add(borrowCapital).add(borrowInterest)); //银行待收总额
					tenderAccount.setBankAwaitCapital(tenderAccount.getBankAwaitCapital().add(borrowCapital));//银行待收本金
					tenderAccount.setBankAwaitInterest(tenderAccount.getBankAwaitInterest().add(borrowInterest));//银行待收利息
					tenderAccount.setBankTotal(tenderAccount.getBankTotal().add(borrowCapital).add(borrowInterest)); //资产总额
					this.accountMapper.updateByPrimaryKeySelective(tenderAccount);
				}
			}
		}
	}
	
	/*
	 * 通过订单号获取债转信息
	 */
	public CreditTender getCreditTenderByOrderId(String orderId){
		if(StringUtils.isEmpty(orderId)){
			return null;
		}
		CreditTenderExample example = new CreditTenderExample();
		CreditTenderExample.Criteria cra = example.createCriteria();
		cra.andAssignNidEqualTo(orderId);
		List<CreditTender> list = this.creditTenderMapper.selectByExample(example);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	/*
	 * 通过订单号获取债转信息CreditRepay
	 */
	public List<CreditRepay> getCreditRepayByOrderId(String orderId){
		if(StringUtils.isEmpty(orderId)){
			return null;
		}
		CreditRepayExample example = new CreditRepayExample();
		CreditRepayExample.Criteria cra = example.createCriteria();
		cra.andAssignNidEqualTo(orderId);
		List<CreditRepay> list = this.creditRepayMapper.selectByExample(example);
		if(list != null && list.size() > 0){
			return list;
		}
		return null;
	}

	
	/*
	 * 通过订单号获取出借信息
	 */
	public BorrowTender getBorrowTenderByOrderId(String orderId){
		if(StringUtils.isEmpty(orderId)){
			return null;
		}
		BorrowTenderExample example = new BorrowTenderExample();
		BorrowTenderExample.Criteria cra = example.createCriteria();
		cra.andNidEqualTo(orderId);
		List<BorrowTender> list = this.borrowTenderMapper.selectByExample(example);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	/*
	 * 通过订单号获取还款信息
	 */
	public BorrowRecover getBorrowRecoverByOrderId(String orderId){
		if(StringUtils.isEmpty(orderId)){
			return null;
		}
		BorrowRecoverExample example = new BorrowRecoverExample();
		BorrowRecoverExample.Criteria cra = example.createCriteria();
		cra.andNidEqualTo(orderId);
		List<BorrowRecover> list = this.borrowRecoverMapper.selectByExample(example);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}

	
	
	
	/*
	 * 通过用户id获取资金信息
	 */
	public Account getAccountByUserId(Integer userId){
		AccountExample example = new AccountExample();
		AccountExample.Criteria cra = example.createCriteria();
		cra.andUserIdEqualTo(userId);
		List<Account> list = this.accountMapper.selectByExample(example);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}

}
