package com.hyjf.admin.exception.borrowexception;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.SessionUtils;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BorrowBail;
import com.hyjf.mybatis.model.auto.BorrowBailExample;
import com.hyjf.mybatis.model.auto.BorrowCarinfoExample;
import com.hyjf.mybatis.model.auto.BorrowCompanyAuthenExample;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.BorrowHousesExample;
import com.hyjf.mybatis.model.auto.BorrowManinfoExample;
import com.hyjf.mybatis.model.auto.BorrowTender;
import com.hyjf.mybatis.model.auto.BorrowTenderExample;
import com.hyjf.mybatis.model.auto.BorrowTenderTmpInfoExample;
import com.hyjf.mybatis.model.auto.BorrowUsersExample;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.FreezeListExample;
import com.hyjf.mybatis.model.customize.AdminSystem;
import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.BorrowCustomize;
import com.hyjf.mybatis.model.customize.BorrowExceptionDeleteBean;
import com.hyjf.mybatis.model.customize.BorrowExceptionDeleteSrchBean;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

@Service
public class BorrowExceptionServiceImpl extends BaseServiceImpl implements BorrowExceptionService {

	/**
	 * COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public Long countBorrow(BorrowCommonCustomize borrowCommonCustomize) {
		return this.adminBorrowExceptionMapper.countBorrow(borrowCommonCustomize);
	}

	/**
	 * 总额合计
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public BigDecimal sumAccount(BorrowCommonCustomize borrowCommonCustomize) {
		return this.adminBorrowExceptionMapper.sumAccount(borrowCommonCustomize);
	}

	/**
	 * 借款列表
	 * 
	 * @return
	 */
	public List<BorrowCustomize> selectBorrowList(BorrowCommonCustomize borrowCommonCustomize) {
		return this.adminBorrowExceptionMapper.selectBorrowList(borrowCommonCustomize);
	}

	/**
	 * 根据bnid获取borrow信息
	 * 
	 * @param nid
	 * @return
	 * @author zhuxiaodong
	 */

	@Override
	public List<BorrowCustomize> selectBorrowByNid(String nid) {
		// TODO Auto-generated method stub
		return this.adminBorrowExceptionMapper.selectBorrowByNid(nid);
	}

	/**
	 * 根据bnid删除borrow信息
	 * 
	 * @param nid
	 * @author zhuxiaodong
	 */

	@Override
	public void deleteBorrowByNid(String nid) {
		List<BorrowCustomize> recordList = this.adminBorrowExceptionMapper.selectBorrowByNid(nid);
		if (recordList != null && recordList.size() > 0) {
			BorrowExceptionDeleteBean bed = null;
			AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
			for (BorrowCustomize borrowCustomize : recordList) {
				bed = new BorrowExceptionDeleteBean();
				String bnid = borrowCustomize.getBorrowNid();
				// 根据标的号检索标的详情
				BorrowWithBLOBs borrow = null;
				BorrowExample borrowSearchExample = new BorrowExample();
				BorrowExample.Criteria cra = borrowSearchExample.createCriteria();
				cra.andBorrowNidEqualTo(nid);
				List<BorrowWithBLOBs> borrowList = this.borrowMapper.selectByExampleWithBLOBs(borrowSearchExample);
				if (borrowList != null && borrowList.size() > 0) {
					borrow = borrowList.get(0);
				}
				// 判断该项目编号有没有交过保证金
				BorrowBailExample exampleBail = new BorrowBailExample();
				BorrowBailExample.Criteria craBail = exampleBail.createCriteria();
				craBail.andBorrowNidEqualTo(bnid);
				List<BorrowBail> borrowBailList = this.borrowBailMapper.selectByExample(exampleBail);
				if (borrowBailList != null && borrowBailList.size() > 0) {
					bed.setBail_num(borrowBailList.get(0).getBailNum());
				}
				bed.setBorrow_nid(borrowCustomize.getBorrowNid());
				bed.setBorrow_name(borrowCustomize.getBorrowName());
				bed.setUsername(borrowCustomize.getUsername());
				bed.setAccount(borrowCustomize.getAccount());
				bed.setBorrow_account_yes(borrowCustomize.getBorrowAccountYes());
				bed.setBorrow_account_wait(borrowCustomize.getBorrowAccountWait());
				bed.setBorrow_account_scale(borrowCustomize.getBorrowAccountScale());
				bed.setBorrow_style(borrowCustomize.getBorrowStyle());
				bed.setBorrow_style_name(borrowCustomize.getBorrowStyleName());
				bed.setProject_type(Integer.parseInt(borrowCustomize.getProjectType()));
				bed.setProject_type_name(borrowCustomize.getProjectTypeName());
				bed.setBorrow_period(borrowCustomize.getBorrowPeriod());
				bed.setBorrow_apr(borrowCustomize.getBorrowApr());
				bed.setStatus(borrowCustomize.getStatus());
				bed.setAddtime(borrowCustomize.getAddtime());
				bed.setBorrow_full_time(borrowCustomize.getBorrowFullTime());
				bed.setRecover_last_time(borrowCustomize.getRecoverLastTime());
				bed.setOperater_uid(Integer.valueOf(adminSystem.getId()));
				bed.setOperater_user(adminSystem.getUsername());
				bed.setOperater_time(GetDate.getNowTime10());
				bed.setOperater_type(0);
				try {
					/* 首先先保存日志,然后删除borrow辅助信息,最后删除borrow信息 */
					this.adminBorrowExceptionMapper.insert(bed);
					// 1.删除个人信息
					BorrowManinfoExample borrowManinfoExample = new BorrowManinfoExample();
					BorrowManinfoExample.Criteria borrowManinfoCra = borrowManinfoExample.createCriteria();
					borrowManinfoCra.andBorrowNidEqualTo(bnid);
					this.borrowManinfoMapper.deleteByExample(borrowManinfoExample);
					// 2.删除公司信息
					BorrowUsersExample borrowUsersExample = new BorrowUsersExample();
					BorrowUsersExample.Criteria borrowUsersCra = borrowUsersExample.createCriteria();
					borrowUsersCra.andBorrowNidEqualTo(bnid);
					this.borrowUsersMapper.deleteByExample(borrowUsersExample);
					// 3.删除车辆信息
					BorrowCarinfoExample borrowCarinfoExample = new BorrowCarinfoExample();
					BorrowCarinfoExample.Criteria borrowCarinfoCra = borrowCarinfoExample.createCriteria();
					borrowCarinfoCra.andBorrowNidEqualTo(bnid);
					this.borrowCarinfoMapper.deleteByExample(borrowCarinfoExample);
					// 4.删除房产信息
					BorrowHousesExample borrowHousesExample = new BorrowHousesExample();
					BorrowHousesExample.Criteria borrowHousesCra = borrowHousesExample.createCriteria();
					borrowHousesCra.andBorrowNidEqualTo(bnid);
					this.borrowHousesMapper.deleteByExample(borrowHousesExample);
					// 5.删除认证信息
					BorrowCompanyAuthenExample borrowCompanyAuthenExample = new BorrowCompanyAuthenExample();
					BorrowCompanyAuthenExample.Criteria borrowCompanyAuthenCra = borrowCompanyAuthenExample.createCriteria();
					borrowCompanyAuthenCra.andBorrowNidEqualTo(bnid);
					this.borrowCompanyAuthenMapper.deleteByExample(borrowCompanyAuthenExample);
					// 6.删除保证金信息
					BorrowBailExample borrowBailExample = new BorrowBailExample();
					BorrowBailExample.Criteria borrowBailCra = borrowBailExample.createCriteria();
					borrowBailCra.andBorrowNidEqualTo(bnid);
					this.borrowBailMapper.deleteByExample(borrowBailExample);
					// 7.删除borrow数据
					BorrowExample borrowExample = new BorrowExample();
					BorrowExample.Criteria borrowCra = borrowExample.createCriteria();
					borrowCra.andBorrowNidEqualTo(bnid);
					this.borrowMapper.deleteByExample(borrowExample);
					// 删标后,重新设置redits
					Integer projectType = borrow.getProjectType();
					String redisBorrowPreNid =projectType == 15 ?  RedisUtils.get("xjdBorrowPreNid") : RedisUtils.get("borrowPreNid");
					String borrowPreNid = borrow == null ? "" : borrow.getBorrowPreNidNew();
					if (StringUtils.isNotEmpty(redisBorrowPreNid)) {
						if (Long.valueOf(redisBorrowPreNid) >= Long.valueOf(borrowPreNid)) {
							if(projectType == 15){
								RedisUtils.set("xjdBorrowPreNid", String.valueOf((Long.valueOf(redisBorrowPreNid) - 1)));
							} else{
								RedisUtils.set("borrowPreNid", String.valueOf((Long.valueOf(redisBorrowPreNid) - 1)));
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public Long countBorrowDelete(BorrowExceptionDeleteSrchBean form) {
		return this.adminBorrowExceptionMapper.countBorrowDelete(form);
	}

	@Override
	public List<BorrowExceptionDeleteBean> selectBorrowDeleteList(BorrowExceptionDeleteSrchBean form) {
		return this.adminBorrowExceptionMapper.selectBorrowDeleteList(form);
	}

	/**
	 * 标的撤销
	 */
	@Override
	public void updateBorrowByNid(String borrowNid) throws Exception {
		AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
		// 当前时间
		int nowTime = GetDate.getNowTime10();
		// 根据标的号检索是否有用户出借
		List<BorrowTender> tenderList = this.selectBorrowTenderByBorrowNid(borrowNid);
		// 如果redis存在,删除redis
		if (RedisUtils.exists(borrowNid)) {
			RedisUtils.del(borrowNid);
		}
		// 再去更改标的状态
		BorrowWithBLOBs borrow = this.selectBorrowByBorrowNid(borrowNid);
		if (borrow != null) {
			// 去更新borrow
			borrow.setStatus(0);
			borrow.setRegistStatus(3);
			boolean isBorrowUpdateFlag = this.borrowMapper.updateByPrimaryKey(borrow) > 0 ? true : false;
			if (!isBorrowUpdateFlag) {
				throw new Exception("标的状态更新失败!");
			}
		}
		if (tenderList != null && tenderList.size() > 0) {
			for (int i = 0; i < tenderList.size(); i++) {
				// 先调用江西银行的撤销出借接口
				BorrowTender borrowTender = tenderList.get(i);
				// 获取出借用户的开户信息
				BankOpenAccount tenderAccount = this.getBankOpenAccount(borrowTender.getUserId());
				// 获取用户出借的标的信息
				BorrowWithBLOBs borrowInfo = this.selectBorrowByBorrowNid(borrowNid);
				BankCallBean bean = new BankCallBean();
				bean.setVersion(BankCallConstant.VERSION_10);// 版本号
				bean.setTxCode(BankCallMethodConstant.TXCODE_BID_CANCEL);// 交易代码
				bean.setTxDate(GetOrderIdUtils.getTxDate()); // 交易日期
				bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
				bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
				bean.setChannel(BankCallConstant.CHANNEL_PC); // 交易渠道
				bean.setAccountId(tenderAccount.getAccount());// 电子账号
				bean.setOrderId(GetOrderIdUtils.getOrderId2(borrowTender.getUserId())); // 订单号
				bean.setTxAmount(String.valueOf(borrowTender.getAccount()));// 原标出借金额
				bean.setProductId(String.valueOf(borrowInfo.getBorrowNid()));// 原标的号
				bean.setOrgOrderId(borrowTender.getNid());// 原投标的订单号
				bean.setLogOrderId(GetOrderIdUtils.getOrderId2(borrowTender.getUserId()));// 订单号
				bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
				bean.setLogUserId(String.valueOf(borrowTender.getUserId()));
				bean.setLogUserName(tenderAccount.getUserName());
				bean.setLogClient(0);
				try {
					BankCallBean callBackBean = BankCallUtils.callApiBg(bean);
					if (callBackBean != null && BankCallStatusConstant.RESPCODE_SUCCESS.equals(callBackBean.getRetCode())) {
						// 要把用户的钱回滚,获取出借用户的账户信息
						Account tenderUserAccount = this.selectAccountByUserId(borrowTender.getUserId());
						tenderUserAccount.setBankBalance(tenderUserAccount.getBankTotal().add(borrowTender.getAccount()));
						tenderUserAccount.setBankFrost(tenderUserAccount.getBankFrost().subtract(borrowTender.getAccount()));
						BigDecimal version = tenderUserAccount.getVersion();
						tenderUserAccount.setVersion(version.add(BigDecimal.ONE));
						// 生成交易明细
						AccountList accountList = new AccountList();
						accountList.setNid(callBackBean.getLogOrderId());
						accountList.setUserId(borrowTender.getUserId());
						accountList.setAmount(borrowTender.getAccount());
						accountList.setTxDate(Integer.parseInt(bean.getTxDate()));// 交易日期
						accountList.setTxTime(Integer.parseInt(bean.getTxTime()));// 交易时间
						accountList.setSeqNo(bean.getSeqNo());// 交易流水号
						accountList.setBankSeqNo((bean.getTxDate() + bean.getTxTime() + bean.getSeqNo()));
						accountList.setType(1);
						accountList.setTrade("tender_over");// TODO
						accountList.setTradeCode("balance");// TODO
						accountList.setAccountId(callBackBean.getAccountId());
						accountList.setBankTotal(tenderUserAccount.getBankTotal()); // 银行总资产
						accountList.setBankBalance(tenderUserAccount.getBankBalance()); // 银行可用余额
						accountList.setBankFrost(tenderUserAccount.getBankFrost());// 银行冻结金额
						accountList.setBankWaitCapital(tenderUserAccount.getBankWaitCapital());// 银行待还本金
						accountList.setBankWaitInterest(tenderUserAccount.getBankWaitInterest());// 银行待还利息
						accountList.setBankAwaitCapital(tenderUserAccount.getBankAwaitCapital());// 银行待收本金
						accountList.setBankAwaitInterest(tenderUserAccount.getBankAwaitInterest());// 银行待收利息
						accountList.setBankAwait(tenderUserAccount.getBankAwait());// 银行待收总额
						accountList.setBankInterestSum(tenderUserAccount.getBankInterestSum()); // 银行累计收益
						accountList.setBankInvestSum(tenderUserAccount.getBankInvestSum());// 银行累计出借
						accountList.setBankWaitRepay(tenderUserAccount.getBankWaitRepay());// 银行待还金额
						accountList.setPlanBalance(tenderUserAccount.getPlanBalance());//汇计划账户可用余额
						accountList.setPlanFrost(tenderUserAccount.getPlanFrost());
						accountList.setTotal(tenderUserAccount.getTotal());
						accountList.setBalance(tenderUserAccount.getBalance());
						accountList.setFrost(tenderUserAccount.getFrost());
						accountList.setAwait(tenderUserAccount.getAwait());
						accountList.setRepay(tenderUserAccount.getRepay());
						accountList.setRemark("出借失败,资金回滚");
						accountList.setCreateTime(nowTime);
						accountList.setBaseUpdate(nowTime);
						accountList.setOperator(borrowTender.getUserId() + "");
						accountList.setIsUpdate(0);
						accountList.setBaseUpdate(0);
						accountList.setInterest(null);
						accountList.setWeb(0);
						accountList.setIsBank(1);// 是否是银行的交易记录 0:否 ,1:是
						// 插入交易明细
						boolean isAccountListUpdateFlag = this.accountListMapper.insertSelective(accountList) > 0 ? true : false;
						if (!isAccountListUpdateFlag) {
							throw new Exception("出借撤销后,插入交易明细失败");
						}
						//更新账户信息
						boolean istenderAccountFlag = this.accountMapper.updateByPrimaryKeySelective(tenderUserAccount) > 0 ? true : false;
						if (!istenderAccountFlag) {
							throw new Exception("出借撤销后,更新出借人的账户信息失败");
						}
						//删除冻结记录
						FreezeListExample freezeListExample = new FreezeListExample();
						freezeListExample.createCriteria().andOrdidEqualTo(borrowTender.getNid()).andUserIdEqualTo(borrowTender.getUserId());
						boolean freezeListFlag = this.freezeListMapper.deleteByExample(freezeListExample)>0?true:false;
						if (!freezeListFlag) {
							throw new Exception("出借撤销后,删除出借冻结信息失败");
						}
						//删除相应的出借信息表
						BorrowTenderTmpInfoExample tenderTmpInfoExample = new BorrowTenderTmpInfoExample();
						tenderTmpInfoExample.createCriteria().andOrdidEqualTo(borrowTender.getNid());
						boolean tenderTempInfoFlag = this.borrowTenderTmpInfoMapper.deleteByExample(tenderTmpInfoExample)>0?true:false;
						if (!tenderTempInfoFlag) {
							throw new Exception("出借撤销后,删除出借记录信息失败");
						}
						// 出借记录删除
						boolean isBorrowTenderFlag = this.borrowTenderMapper.deleteByPrimaryKey(borrowTender.getId()) > 0 ? true : false;
						if (!isBorrowTenderFlag) {
							throw new Exception("出借撤销后,删除出借记录失败");
						}
					}
				} catch (Exception e) {
					throw new Exception("调用江西银行出借撤销接口");
				}
			}
		}
		// 获取借款人在银行的电子账户
		BankOpenAccount borrowOpenAccount = this.getBankOpenAccount(borrow.getUserId());
		// 出借都撤销完了,然后去江西银行进行标的撤销
		BankCallBean borrowBean = new BankCallBean();
		borrowBean.setVersion(BankCallConstant.VERSION_10);// 版本号
		borrowBean.setTxCode(BankCallMethodConstant.TXCODE_DEBT_REGISTER_CANCEL);// 交易代码
		borrowBean.setTxDate(GetOrderIdUtils.getTxDate()); // 交易日期
		borrowBean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
		borrowBean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
		borrowBean.setChannel(BankCallConstant.CHANNEL_PC); // 交易渠道
		borrowBean.setAccountId(borrowOpenAccount.getAccount());// 电子账号
		borrowBean.setProductId(String.valueOf(borrow.getBorrowNid()));// 原标的号
		borrowBean.setRaiseDate(borrow.getBankRaiseStartDate());// 募集日
		borrowBean.setLogOrderId(GetOrderIdUtils.getOrderId2(borrow.getUserId()));// 订单号
		borrowBean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
		borrowBean.setLogUserId(String.valueOf(borrow.getUserId()));
		borrowBean.setLogUserName(borrowOpenAccount.getUserName());
		borrowBean.setLogClient(0);
		try {
			BankCallBean resultBean = BankCallUtils.callApiBg(borrowBean);
			// 标的撤销成功
			if (resultBean == null || !BankCallStatusConstant.RESPCODE_SUCCESS.equals(resultBean.getRetCode())) {
				throw new Exception("调用江西银行的标的撤销接口失败~!");
			}
			BorrowExceptionDeleteBean bed = new BorrowExceptionDeleteBean();
			String bnid = borrow.getBorrowNid();
			List<BorrowCustomize> recordList = this.adminBorrowExceptionMapper.selectBorrowByNid(bnid);
			BorrowCustomize borrowCustomize = null;
			if (recordList != null && recordList.size() > 0) {
				borrowCustomize = recordList.get(0);
			}
			// 判断该项目编号有没有交过保证金
			BorrowBailExample exampleBail = new BorrowBailExample();
			BorrowBailExample.Criteria craBail = exampleBail.createCriteria();
			craBail.andBorrowNidEqualTo(bnid);
			List<BorrowBail> borrowBailList = this.borrowBailMapper.selectByExample(exampleBail);
			if (borrowBailList != null && borrowBailList.size() > 0) {
				bed.setBail_num(borrowBailList.get(0).getBailNum());
			}
			bed.setBorrow_nid(borrowCustomize.getBorrowNid());
			bed.setBorrow_name(borrowCustomize.getBorrowName());
			bed.setUsername(borrowCustomize.getUsername());
			bed.setAccount(borrowCustomize.getAccount());
			bed.setBorrow_account_yes(borrowCustomize.getBorrowAccountYes());
			bed.setBorrow_account_wait(borrowCustomize.getBorrowAccountWait());
			bed.setBorrow_account_scale(borrowCustomize.getBorrowAccountScale());
			bed.setBorrow_style(borrowCustomize.getBorrowStyle());
			bed.setBorrow_style_name(borrowCustomize.getBorrowStyleName());
			bed.setProject_type(Integer.parseInt(borrowCustomize.getProjectType()));
			bed.setProject_type_name(borrowCustomize.getProjectTypeName());
			bed.setBorrow_period(borrowCustomize.getBorrowPeriod());
			bed.setBorrow_apr(borrowCustomize.getBorrowApr());
			bed.setStatus(borrowCustomize.getStatus());
			bed.setAddtime(borrowCustomize.getAddtime());
			bed.setBorrow_full_time(borrowCustomize.getBorrowFullTime());
			bed.setRecover_last_time(borrowCustomize.getRecoverLastTime());
			bed.setOperater_uid(Integer.valueOf(adminSystem.getId()));
			bed.setOperater_user(adminSystem.getUsername());
			bed.setOperater_time(GetDate.getNowTime10());
			bed.setOperater_type(1);
			boolean isInsertFlag = this.adminBorrowExceptionMapper.insert(bed) > 0 ? true : false;
			if (!isInsertFlag) {
				throw new Exception("插入标的删除日志表失败!");
			}
		} catch (Exception e) {
			throw new Exception("调用江西银行的标的撤销接口失败!");
		}

	}

	/**
	 * 根据标的号检索标的是否有出借
	 * 
	 * @param borrowNid
	 * @return
	 */
	private List<BorrowTender> selectBorrowTenderByBorrowNid(String borrowNid) {
		BorrowTenderExample tenderExample = new BorrowTenderExample();
		BorrowTenderExample.Criteria tenderCra = tenderExample.createCriteria();
		tenderCra.andBorrowNidEqualTo(borrowNid);
		return this.borrowTenderMapper.selectByExample(tenderExample);
	}

	/**
	 * 根据标的号
	 * 
	 * @param borrowNid
	 * @return
	 */
	private BorrowWithBLOBs selectBorrowByBorrowNid(String borrowNid) {
		BorrowExample example = new BorrowExample();
		BorrowExample.Criteria cra = example.createCriteria();
		cra.andBorrowNidEqualTo(borrowNid);
		List<BorrowWithBLOBs> result = this.borrowMapper.selectByExampleWithBLOBs(example);
		if (result != null && result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

	/**
	 * 根据用户Id获取用户
	 * 
	 * @param userId
	 * @return
	 */
	private Account selectAccountByUserId(Integer userId) {
		AccountExample example = new AccountExample();
		AccountExample.Criteria cra = example.createCriteria();
		cra.andUserIdEqualTo(userId);
		List<Account> result = this.accountMapper.selectByExample(example);
		if (result != null && result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
}
