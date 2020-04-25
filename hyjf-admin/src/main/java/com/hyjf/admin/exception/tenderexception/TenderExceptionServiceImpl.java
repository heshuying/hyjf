package com.hyjf.admin.exception.tenderexception;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.BorrowTender;
import com.hyjf.mybatis.model.auto.BorrowTenderExample;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.FreezeList;
import com.hyjf.mybatis.model.auto.FreezeListExample;
import com.hyjf.mybatis.model.auto.TenderBackHistory;
import com.hyjf.mybatis.model.auto.UtmPlat;
import com.hyjf.mybatis.model.auto.UtmPlatExample;
import com.hyjf.mybatis.model.customize.admin.AdminTenderExceptionCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

@Service
public class TenderExceptionServiceImpl extends BaseServiceImpl implements TenderExceptionService {

	/**
	 * 明细列表
	 * 
	 * @param borrowCommonCustomize
	 * @return
	 * @author Administrator
	 */

	@Override
	public List<AdminTenderExceptionCustomize> selectTenderExceptionList(AdminTenderExceptionCustomize record) {
		return this.adminTenderExceptionCustomizeMapper.selectTenderExceptionList(record);
	}

	/**
	 * 明细记录 总数COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	@Override
	public Integer countTenderException(AdminTenderExceptionCustomize record) {
		return this.adminTenderExceptionCustomizeMapper.countTenderException(record);
	}

	/**
	 * 渠道下拉列表
	 * 
	 * @return
	 */
	@Override
	public List<UtmPlat> getUtmPlatList() {
		UtmPlatExample example = new UtmPlatExample();
		return this.utmPlatMapper.selectByExample(example);
	}

	/**
	 * 金额合计
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	@Override
	public String sumTenderExceptionAccount(AdminTenderExceptionCustomize record) {
		return this.adminTenderExceptionCustomizeMapper.sumTenderExceptionAccount(record);
	}

	/**
	 * 解冻订单号在本库中是否存在
	 * 
	 * @param trxId
	 * @return
	 */
	public boolean selsectNidIsExists(String nid) {
		FreezeListExample example = new FreezeListExample();
		FreezeListExample.Criteria cra = example.createCriteria();
		cra.andOrdidEqualTo(nid);

		List<FreezeList> freezeList = this.freezeListMapper.selectByExample(example);

		if (freezeList != null && freezeList.size() > 0) {
			return true;
		}

		return false;
	}

	/**
	 * 出借爆标处理
	 * 
	 * @param nid
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public String updateBackTender(TenderExceptionBean form) throws Exception {
		// 当前时间
		int nowTime = GetDate.getNowTime10();

		TenderBackHistory tenderBackHistory = new TenderBackHistory();
		tenderBackHistory.setType("0");
		tenderBackHistory.setNotes(form.getNotes());
		tenderBackHistory.setCreateUser(ShiroUtil.getLoginUsername());
		tenderBackHistory.setCreateTime(GetDate.getMyTimeInMillis());

		String borrowNid = "";
		String nid = form.getNid();

		FreezeListExample freezeListExample = new FreezeListExample();
		FreezeListExample.Criteria freezeListCra = freezeListExample.createCriteria();
		freezeListCra.andOrdidEqualTo(nid);

		List<FreezeList> freezeListList = this.freezeListMapper.selectByExample(freezeListExample);
		FreezeList freezeList = null;
		// 资金冻结表 删除
		if (freezeListList != null && freezeListList.size() > 0) {
			freezeList = freezeListList.get(0);
			borrowNid = freezeList.getBorrowNid();
			tenderBackHistory.setBorrowNid(borrowNid);
			tenderBackHistory.setTrxId(freezeList.getTrxid());
		}

		// 根据出借订单号查询出借记录表
		BorrowTenderExample borrowTenderExample = new BorrowTenderExample();
		BorrowTenderExample.Criteria borrowTenderCra = borrowTenderExample.createCriteria();
		borrowTenderCra.andBorrowNidEqualTo(borrowNid);
		borrowTenderCra.andNidEqualTo(nid);
		List<BorrowTender> tenderList = this.borrowTenderMapper.selectByExample(borrowTenderExample);
		if (tenderList != null && tenderList.size() > 0) {
			for (int i = 0; i < tenderList.size(); i++) {
				BorrowTender tender = tenderList.get(i);
				BankOpenAccount tenderAccount = this.getBankOpenAccount(tender.getUserId());
				BorrowWithBLOBs borrowInfo = this.selectBorrowInfoByBorrowId(borrowNid);
				// 调用投标撤销接口
				BankCallBean bean = new BankCallBean();
				bean.setVersion(BankCallConstant.VERSION_10);// 版本号
				bean.setTxCode(BankCallMethodConstant.TXCODE_BID_CANCEL);// 交易代码
				bean.setTxDate(GetOrderIdUtils.getTxDate()); // 交易日期
				bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
				bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
				bean.setChannel(BankCallConstant.CHANNEL_PC); // 交易渠道
				bean.setAccountId(tenderAccount.getAccount());// 电子账号
				bean.setOrderId(GetOrderIdUtils.getOrderId2(tender.getUserId())); // 订单号
				bean.setTxAmount(String.valueOf(tender.getAccount()));// 原标出借金额
				bean.setProductId(String.valueOf(borrowInfo.getId()));// 原标的号
				bean.setOrgOrderId(tender.getNid());// 原投标的订单号
				bean.setLogOrderId(GetOrderIdUtils.getOrderId2(tender.getUserId()));// 订单号
				bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
				bean.setLogUserId(String.valueOf(tender.getUserId()));
				bean.setLogUserName(tenderAccount.getUserName());
				bean.setLogClient(0);
				try {
					BankCallBean callBackBean = BankCallUtils.callApiBg(bean);
					if (callBackBean != null
							&& BankCallStatusConstant.RESPCODE_SUCCESS.equals(callBackBean.getRetCode())) {
						AccountExample example = new AccountExample();
						AccountExample.Criteria criteria = example.createCriteria();
						criteria.andUserIdEqualTo(tender.getUserId());
						List<Account> list = accountMapper.selectByExample(example);
						if (list != null && list.size() > 0) {
							Account accountBean = list.get(0);
							// frost减去account(用户出借金额)
							accountBean.setBankFrost(accountBean.getBankFrost().subtract(tender.getAccount()));
							// balance加上account
							accountBean.setBankBalance(accountBean.getBankBalance().add(tender.getAccount()));

							boolean isAccountUpdateFlag = accountMapper.updateByPrimaryKey(accountBean) > 0 ? true
									: false;
							if (!isAccountUpdateFlag) {
								throw new Exception("出借撤销后,更新出借人账户失败!");
							}

							// 生成交易明细
							AccountList accountList = new AccountList();
							accountList.setNid(callBackBean.getLogOrderId());
							accountList.setUserId(tender.getUserId());
							accountList.setAmount(tender.getAccount());
							accountList.setTxDate(Integer.parseInt(bean.getTxDate()));// 交易日期
							accountList.setTxTime(Integer.parseInt(bean.getTxTime()));// 交易时间
							accountList.setSeqNo(bean.getSeqNo());// 交易流水号
							accountList.setBankSeqNo((bean.getTxDate() + bean.getTxTime() + bean.getSeqNo()));
							accountList.setType(1);
							accountList.setTrade("unFrz");// 解冻
							accountList.setTradeCode("tender_back");// 出借撤销
							accountList.setAccountId(callBackBean.getAccountId());
							accountList.setBankTotal(accountBean.getBankTotal()); // 银行总资产
							accountList.setBankBalance(accountBean.getBankBalance()); // 银行可用余额
							accountList.setBankFrost(accountBean.getBankFrost());// 银行冻结金额
							accountList.setBankWaitCapital(accountBean.getBankWaitCapital());// 银行待还本金
							accountList.setBankWaitInterest(accountBean.getBankWaitInterest());// 银行待还利息
							accountList.setBankAwaitCapital(accountBean.getBankAwaitCapital());// 银行待收本金
							accountList.setBankAwaitInterest(accountBean.getBankAwaitInterest());// 银行待收利息
							accountList.setBankAwait(accountBean.getBankAwait());// 银行待收总额
							accountList.setBankInterestSum(accountBean.getBankInterestSum()); // 银行累计收益
							accountList.setBankInvestSum(accountBean.getBankInvestSum());// 银行累计出借
							accountList.setBankWaitRepay(accountBean.getBankWaitRepay());// 银行待还金额
							accountList.setPlanBalance(accountBean.getPlanBalance());//汇计划账户可用余额
							accountList.setPlanFrost(accountBean.getPlanFrost());
							accountList.setTotal(accountBean.getTotal());
							accountList.setBalance(accountBean.getBalance());
							accountList.setFrost(accountBean.getFrost());
							accountList.setAwait(accountBean.getAwait());
							accountList.setRepay(accountBean.getRepay());
							accountList.setRemark(borrowNid + "的出借被撤销");
							accountList.setCreateTime(nowTime);
							accountList.setBaseUpdate(nowTime);
							accountList.setOperator(tender.getUserId() + "");
							accountList.setIsUpdate(0);
							accountList.setBaseUpdate(0);
							accountList.setInterest(null);
							accountList.setWeb(0);
							accountList.setIsBank(1);// 是否是银行的交易记录 0:否 ,1:是

							accountListMapper.insertSelective(accountList);
						}
					} else {
						throw new Exception("调用江西银行标的撤销接口失败");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		// 需要删除的出借标的
		BorrowTenderExample borrowTenderDeleteExample = new BorrowTenderExample();
		BorrowTenderExample.Criteria borrowTenderDeleteCra = borrowTenderDeleteExample.createCriteria();
		borrowTenderDeleteCra.andBorrowNidEqualTo(borrowNid);
		borrowTenderDeleteCra.andNidEqualTo(nid);
		List<BorrowTender> borrowTenderDeleteList = this.borrowTenderMapper.selectByExample(borrowTenderDeleteExample);

		BorrowTender borrowTenderDeleteRecord = null;

		if (borrowTenderDeleteList != null && borrowTenderDeleteList.size() > 0) {
			borrowTenderDeleteRecord = borrowTenderDeleteList.get(0);
			tenderBackHistory.setUserName(borrowTenderDeleteRecord.getTenderUserName());
			tenderBackHistory.setAmount(borrowTenderDeleteRecord.getAccount().toString());
			tenderBackHistory.setOrdId(borrowTenderDeleteRecord.getNid());
		}

		// 所有的标的信息
		borrowTenderExample = new BorrowTenderExample();
		borrowTenderCra = borrowTenderExample.createCriteria();
		borrowTenderCra.andBorrowNidEqualTo(borrowNid);
		List<BorrowTender> borrowTenderList = this.borrowTenderMapper.selectByExample(borrowTenderExample);

		if (borrowTenderList != null && borrowTenderList.size() > 0) {

			// 标的出借总金额
			BigDecimal tenderAmount = BigDecimal.ZERO;

			// 服务费
			BigDecimal loanFree = BigDecimal.ZERO;

			for (int i = 0; i < borrowTenderList.size(); i++) {
				BorrowTender borrowTenderRecord = borrowTenderList.get(i);
				// 剩余标的的出借总额和服务费总额
				if (!nid.equals(borrowTenderRecord.getNid())) {
					// 出借总额
					tenderAmount = tenderAmount.add(borrowTenderRecord.getAccount());
					// 出借服务费
					loanFree = loanFree.add(borrowTenderRecord.getLoanFee());
				}
			}

			// 回滚borrow表的数据
			BorrowExample borrowExample = new BorrowExample();
			BorrowExample.Criteria borrowCriteria = borrowExample.createCriteria();
			borrowCriteria.andBorrowNidEqualTo(borrowNid);
			List<BorrowWithBLOBs> listBorrow = this.borrowMapper.selectByExampleWithBLOBs(borrowExample);
			if (listBorrow != null && listBorrow.size() > 0) {
				BorrowWithBLOBs borrow = listBorrow.get(0);

				// 出借表的出借总额
				borrow.setBorrowAccountYes(tenderAmount);
				// 借款表的借款总额 - 出借表的出借总额
				borrow.setBorrowAccountWait(borrow.getAccount().subtract(tenderAmount));
				// 服务费
				borrow.setBorrowService(loanFree.toString());
				// 借款进度
				BigDecimal scale = borrow.getBorrowAccountYes().divide(borrow.getAccount(), 8, BigDecimal.ROUND_DOWN)
						.multiply(new BigDecimal(100));
				borrow.setBorrowAccountScale(scale);
				// 出借次数减去1一次
				borrow.setTenderTimes(borrow.getTenderTimes() - 1);

				// 借款金额 大于 出借总额
				if (borrow.getAccount().compareTo(tenderAmount) > 0) {
					borrow.setBorrowFullStatus(0);// 满标状态
					borrow.setBorrowFullTime(0); // 满标时间
					RedisUtils.set(borrowNid, borrow.getBorrowAccountWait().toString());
				}
				// 借款金额 等于 出借总额满标
				if (borrow.getAccount().compareTo(tenderAmount) == 0) {
					borrow.setBorrowFullStatus(1);// 满标状态
					borrow.setBorrowFullTime(GetDate.getMyTimeInMillis()); // 满标时间
				}
				tenderBackHistory.setBorrowName(borrow.getName());
				tenderBackHistory.setAccount(borrow.getAccount().toString());
				tenderBackHistory.setAccountYes(borrow.getBorrowAccountYes().toString());

				boolean isBorrowUpdateFlag = this.borrowMapper.updateByPrimaryKeyWithBLOBs(borrow) > 0 ? true : false;
				if (!isBorrowUpdateFlag) {
					throw new Exception("投标撤销后,更新borrow表信息失败");
				}
			}
		}

		// 删除资金冻结表
		if (freezeList != null) {
			boolean isFreezeUpdateFlag = this.freezeListMapper.deleteByPrimaryKey(freezeList.getId()) > 0 ? true
					: false;
			if (!isFreezeUpdateFlag) {
				throw new Exception("投标撤消后,更新冻结记录表失败");
			}
		}
		// 删除该出借标的
		if (borrowTenderDeleteRecord != null) {
			boolean isTenderUpdateFlag = this.borrowTenderMapper.deleteByPrimaryKey(borrowTenderDeleteRecord.getId()) > 0 ? true
					: false;
			if (!isTenderUpdateFlag) {
				throw new Exception("投标撤销后,更新出借记录表失败~!");
			}
		}

		// 插入历史记录
		boolean isTenderBackFlag = this.tenderBackHistoryMapper.insertSelective(tenderBackHistory) > 0 ? true : false;
		if (!isTenderBackFlag) {
			throw new Exception("投标撤销后,插入出借删除记录表失败~!");
		}
		return "";
	}

	/**
	 * 根据项目编号检索标的信息
	 * 
	 * @param borrowNid
	 * @return
	 */
	private BorrowWithBLOBs selectBorrowInfoByBorrowId(String borrowNid) {
		BorrowExample example = new BorrowExample();
		BorrowExample.Criteria cra = example.createCriteria();
		cra.andBorrowNidEqualTo(borrowNid);
		List<BorrowWithBLOBs> list = this.borrowMapper.selectByExampleWithBLOBs(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

}
