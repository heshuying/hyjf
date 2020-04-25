/**
 * Description：用户预约管理service接口实现
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.web.user.appoint;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AccountLog;
import com.hyjf.mybatis.model.auto.AppointmentRecodLog;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowAppoint;
import com.hyjf.mybatis.model.auto.BorrowAppointExample;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.FreezeList;
import com.hyjf.mybatis.model.auto.FreezeListExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.web.WebBorrowAppointCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;
import com.hyjf.web.BaseServiceImpl;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

@Service
public class AppointServiceImpl extends BaseServiceImpl implements AppointService {

	
	@Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private TransactionDefinition transactionDefinition;
	/**
	 * 查询用户的预约总数
	 * @param params
	 * @return
	 * @author Administrator
	 */
		
	@Override
	public int countAppointRecordTotal(Map<String, Object> params) {
		return this.webBorrowAppointCustomizeMapper.countAppointRecordTotal(params);
			
	}

	/**
	 * 查询用户预约数量
	 * @param params
	 * @return
	 * @author Administrator
	 */
		
	@Override
	public List<WebBorrowAppointCustomize> selectAppointRecordList(Map<String, Object> params) {
		return this.webBorrowAppointCustomizeMapper.selectAppointRecordList(params);
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param orderId
	 * @param userId
	 * @return
	 * @author Administrator
	 */
		
	@Override
	public int checkAppointStatus(String orderId, Integer userId) {
		
		BorrowAppointExample example  = new BorrowAppointExample();
		BorrowAppointExample.Criteria crt = example.createCriteria();
		crt.andUserIdEqualTo(userId);
		crt.andOrderIdEqualTo(orderId);
		List<BorrowAppoint> borrowAppoints =  this.borrowAppointMapper.selectByExample(example);
		if(borrowAppoints!=null && borrowAppoints.size()>0){
			if(borrowAppoints.size()==1){
				BorrowAppoint borrowAppoint = borrowAppoints.get(0);
				String borrowNid = borrowAppoint.getBorrowNid();
				Borrow borrow = this.selectBorrowByNid(borrowNid);
				int ontime =borrow.getOntime();
				int nowTime = GetDate.getNowTime10();
				if(nowTime >= ontime){
					return 3;
				}else{
					return 1;
				}
			}else{
				return 2;
			}
		}else{
			return 0;	
		}
	}

	/**
	 * 根据项目编号查询相应的项目信息
	 * @param borrowNid
	 * @return
	 */
		
	private Borrow selectBorrowByNid(String borrowNid) {
		
		BorrowExample example = new BorrowExample();
		BorrowExample.Criteria crt = example.createCriteria();
		crt.andBorrowNidEqualTo(borrowNid);
		List<Borrow> borrows = this.borrowMapper.selectByExample(example);
		if(borrows!=null&&borrows.size()>0){
			return borrows.get(0);
		}
		return null;
			
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param orderId
	 * @param userId
	 * @return
	 * @author Administrator
	 * @throws Exception 
	 */
		
	@Override
	public boolean userCancelAppoint(String orderId, Integer userId) throws Exception {
		
		Date date = new Date();
		int nowTime = GetDate.getNowTime10();
		 // 手动控制事务
        TransactionStatus txStatus = null;
        try {
        	// 开启事务
        	txStatus = this.transactionManager.getTransaction(transactionDefinition);
        	BorrowAppointExample example  = new BorrowAppointExample();
    		BorrowAppointExample.Criteria crt = example.createCriteria();
    		crt.andUserIdEqualTo(userId);
    		crt.andOrderIdEqualTo(orderId);
    		List<BorrowAppoint> borrowAppoints =  this.borrowAppointMapper.selectByExample(example);
    		if(borrowAppoints!=null && borrowAppoints.size()>0){
    			BorrowAppoint borrowAppoint = borrowAppoints.get(0);
    			//项目标的
    			String borrowNid = borrowAppoint.getBorrowNid();
    			//预约金额
    			BigDecimal account = borrowAppoint.getAccount();
    			borrowAppoint.setAppointStatus(2);
    			borrowAppoint.setCancelTime(date);
    			borrowAppoint.setUpdateTime(date);
    			borrowAppoint.setAppointRemark("预约被取消!");
    			boolean appointFlag = this.borrowAppointMapper.updateByPrimaryKeySelective(borrowAppoint)>0?true:false;
    			if(appointFlag){
					FreezeListExample freezeExample = new FreezeListExample();
					FreezeListExample.Criteria freezeCrt = freezeExample.createCriteria();
					freezeCrt.andUserIdEqualTo(userId);
					freezeCrt.andBorrowNidEqualTo(borrowNid);
					freezeCrt.andOrdidEqualTo(orderId);
					boolean freezeFlag = this.freezeListMapper.deleteByExample(freezeExample) > 0 ? true : false;
					if (freezeFlag) {
						// 新出借金额汇总
						AccountExample accountExample = new AccountExample();
						AccountExample.Criteria criteria = accountExample.createCriteria();
						criteria.andUserIdEqualTo(userId);
						List<Account> list = accountMapper.selectByExample(accountExample);
						if (list != null && list.size() == 1) {
							// 总金额
							BigDecimal total = list.get(0).getTotal();
							// 冻结金额
							BigDecimal frost = list.get(0).getFrost();
							// 可用金额
							BigDecimal balance = list.get(0).getBalance();
							// 待收金额
							BigDecimal await = list.get(0).getAwait();
							// 待还金额
							BigDecimal repay = list.get(0).getRepay();
							// 更新用户账户余额表
							Account accountBean = new Account();
							accountBean.setUserId(userId);
							accountBean.setFrost(account);
							// 出借人可用余额
							accountBean.setBalance(account);
							// 出借人待收金额
							Boolean accountFlag = this.adminAccountCustomizeMapper.updateAppointAccount(accountBean) > 0 ? true: false;
							// 插入account_list表
							if (accountFlag) {
								System.out.println("用户:" + userId + "***********************************更新account，预约订单号：" + orderId);
								// 插入huiyingdai_account_list表
								AccountList accountList = new AccountList();
								// 生成规则BorrowNid_userid_期数
								accountList.setNid(borrowNid + "_" + orderId);
								// 借款人id
								accountList.setUserId(userId);
								// 操作金额
								accountList.setAmount(account);
								// 收支类型1收入2支出3冻结
								accountList.setType(1);
								// 交易类型
								accountList.setTrade("appoint_cancel");
								// 操作识别码
								accountList.setTradeCode("balance");
								// 资金总额
								accountList.setTotal(total);
								// 可用金额
								accountList.setBalance(balance.add(account));
								// 冻结金额
								accountList.setFrost(frost.subtract(account));
								// 待收金额
								accountList.setAwait(await);
								// 待还金额
								accountList.setRepay(repay);
								// 创建时间
								accountList.setCreateTime(nowTime);
								// 操作员
								accountList.setOperator(CustomConstants.OPERATOR_AUTO_REPAY);
								accountList.setRemark(borrowNid);
								accountList.setBaseUpdate(0);
								accountList.setWeb(0);
								boolean accountListFlag = accountListMapper.insertSelective(accountList) > 0 ? true : false;
								if (accountListFlag) {
									// 扣除可用金额
									AccountLog accountLog = new AccountLog();
									// 操作用户id
									accountLog.setUserId(userId);
									accountLog.setNid("appoint_cancel_" + userId + "_" + borrowNid + "_" + orderId);
									accountLog.setTotalOld(BigDecimal.ZERO);
									accountLog.setCode("borrow");
									accountLog.setCodeType("tender");
									accountLog.setCodeNid(orderId);
									accountLog.setBorrowNid(borrowNid);
									accountLog.setIncomeOld(BigDecimal.ZERO);
									accountLog.setIncomeNew(BigDecimal.ZERO);
									accountLog.setAccountWebStatus(0);
									accountLog.setAccountUserStatus(0);
									accountLog.setAccountType("");
									// 操作金额
									accountLog.setMoney(account);
									// 收入
									accountLog.setIncome(BigDecimal.ZERO);
									// 支出
									accountLog.setExpend(BigDecimal.ZERO);
									accountLog.setExpendNew(BigDecimal.ZERO);
									accountLog.setBalanceOld(BigDecimal.ZERO);
									accountLog.setBalanceNew(BigDecimal.ZERO);
									accountLog.setBalanceCash(BigDecimal.ZERO);
									accountLog.setBalanceCashNew(BigDecimal.ZERO);
									accountLog.setBalanceCashOld(BigDecimal.ZERO);
									accountLog.setExpendOld(BigDecimal.ZERO);
									// 可提现金额
									accountLog.setBalanceCash(BigDecimal.ZERO);
									// 不可提现金额
									accountLog.setBalanceFrost(frost.subtract(account).multiply(new BigDecimal(-1)));
									accountLog.setFrost(frost);// 冻结金额
									accountLog.setFrostOld(BigDecimal.ZERO);
									accountLog.setFrostNew(BigDecimal.ZERO);
									accountLog.setAwait(BigDecimal.ZERO);// 待收金额
									accountLog.setRepay(BigDecimal.ZERO);// 待还金额
									accountLog.setRepayOld(BigDecimal.ZERO);
									accountLog.setRepayNew(BigDecimal.ZERO);
									accountLog.setAwait(BigDecimal.ZERO);
									accountLog.setAwaitNew(BigDecimal.ZERO);
									accountLog.setAwaitOld(BigDecimal.ZERO);
									accountLog.setType("tender");// 类型
									accountLog.setToUserid(userId); // 付给谁
									accountLog.setRemark("预约[" + borrowNid + "]所冻结资金");// 备注
									accountLog.setAddtime(String.valueOf(GetDate.getNowTime10()));
									accountLog.setBalanceFrostNew(BigDecimal.ZERO);
									accountLog.setBalanceFrostOld(BigDecimal.ZERO);
									boolean accountLogFlag = this.accountLogMapper.insertSelective(accountLog) > 0 ? true : false;
									if (accountLogFlag) {
										System.out.println("用户:" + userId + "***********************************插入accountLog，预约订单号：" + orderId);
										Borrow borrow = selectBorrowByNid(borrowAppoint.getBorrowNid());
				    					if(Validator.isNotNull(borrow)){
				    						//项目id
				    						int borrowId = borrow.getId();
				    						// 更新borrow表
				    						Map<String, Object> borrowParams = new HashMap<String, Object>();
				    						borrowParams.put("appoint", borrowAppoint.getAccount());
				    						borrowParams.put("borrowId", borrowId);
				    						boolean updateBorrowAccountFlag = this.borrowCustomizeMapper.updateCancelOfBorrowAppoint(borrowParams)> 0 ? true : false;
				    						if(updateBorrowAccountFlag){
				    							//回滚redis
				    							boolean redisFlag = recoverRedis(borrowAppoint.getBorrowNid(), borrowAppoint.getUserId(), borrowAppoint.getAccount());
				    							if(redisFlag){
				    								Users user = this.usersMapper.selectByPrimaryKey(userId);
				    								int recordTotal = Validator.isNotNull(user.getRecodTotal())?user.getRecodTotal():0;
				    								AppointmentRecodLog appointRecordLog = new AppointmentRecodLog();
				    								appointRecordLog.setAddTime(date);
				    								appointRecordLog.setRecod(2);
				    								appointRecordLog.setRecodMoney(borrowAppoint.getAccount());
				    								appointRecordLog.setRecodNid(borrowAppoint.getBorrowNid());
				    								appointRecordLog.setApointOrderId(borrowAppoint.getOrderId());
				    								appointRecordLog.setRecodRemark("取消预约");
				    								appointRecordLog.setRecodTotal(recordTotal+2);
				    								appointRecordLog.setUserName(user.getUsername());
				    								appointRecordLog.setRecodType(0);
				    								appointRecordLog.setUserId(userId);
				    								boolean appointRecordLogFlag = this.appointmentRecodLogMapper.insertSelective(appointRecordLog)>0?true:false;
				    								if(appointRecordLogFlag){
				    									user.setRecodTotal(recordTotal+2);
				    									user.setRecodTime(date);
				    									boolean userFlag = this.usersMapper.updateByPrimaryKey(user)>0?true:false;
				    									if(userFlag){
				    										// 提交事务
				    	                                    this.transactionManager.commit(txStatus);
				    										return true;
				    									}else{
				    										throw new RuntimeException("更新users表失败！");
				    									}
				    								}else{
				    									throw new RuntimeException("插入appointRecordLogFlag表失败！");
				    								}
				    							}else{
				    								throw new RuntimeException("redis回滚失败！");
				    							}
				    						}else{
				    							throw new RuntimeException("标的信息borrow表更新失败！");
				    						}
				    					}else{
				    						throw new RuntimeException("标的信息borrow表查询失败！");
				    					}
									} else {
										throw new RuntimeException("accountLog表更新失败");
									}
								} else {
									throw new RuntimeException("用户交易明细表accountList插入失败");
								}
							} else {
								throw new RuntimeException("用户账户信息表account更新失败");
							}
						} else {
							throw new RuntimeException("用户账户信息表account查询失败");
						}
					}else{
						throw new RuntimeException("删除用户预约冻结表freezeList失败");
					}
    			}else{
    				throw new RuntimeException("更新borrowAppoint表失败！");
    			}
    		}else{
				throw new RuntimeException("用户预约表borrowAppoint查询失败！");
			}
        }catch(Exception e){
        	e.printStackTrace();
        	// 回滚事务
        	this.transactionManager.rollback(txStatus);
        }
		return false;
	}
	
	/**
	 * 
	 * @param borrowNid
	 * @param userId
	 * @param account
	 */
	private boolean recoverRedis(String borrowNid, Integer userId, BigDecimal account) {
		JedisPool pool = RedisUtils.getPool();
		Jedis jedis = pool.getResource();
		String balanceLast = RedisUtils.get(borrowNid+CustomConstants.APPOINT);
		if (StringUtils.isNotBlank(balanceLast)) {
			while ("OK".equals(jedis.watch(borrowNid+CustomConstants.APPOINT))) {
				balanceLast = RedisUtils.get(borrowNid+CustomConstants.APPOINT);
				if(StringUtils.isNotBlank(balanceLast)){
					BigDecimal recoverAccount = account.add(new BigDecimal(balanceLast));
					Transaction tx = jedis.multi();
					tx.set(borrowNid+CustomConstants.APPOINT, recoverAccount + "");
					List<Object> result = tx.exec();
					if (result == null || result.isEmpty()) {
						jedis.unwatch();
						return false;
					} else {
						System.out.println("预约取消，用户:" + userId + "*****from redis恢复redis：" + account);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 获取用户的冻结记录
	 * 
	 * @param userId
	 * @param borrowNid
	 * @param appointOrderId
	 * @return
	 * @author Administrator
	 */
	@Override
	public FreezeList getUserFreeze(Integer userId, String orderId) {
		FreezeListExample example = new FreezeListExample();
		FreezeListExample.Criteria crt = example.createCriteria();
		crt.andUserIdEqualTo(userId);
		crt.andOrdidEqualTo(orderId);
		List<FreezeList> freezeLists = this.freezeListMapper.selectByExample(example);
		if (freezeLists != null && freezeLists.size() == 1) {
			return freezeLists.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 交易状态查询(调用汇付天下接口)
	 *
	 * @return
	 */
	private ChinapnrBean queryTransStat(String ordId, String ordDate, String queryTransType) {
		String methodName = "queryTransStat";

		// 调用汇付接口(交易状态查询)
		ChinapnrBean bean = new ChinapnrBean();
		bean.setVersion(ChinaPnrConstant.VERSION_10); // 版本号(必须)
		bean.setCmdId(ChinaPnrConstant.CMDID_QUERYTRANSSTAT); // 消息类型(必须)
		bean.setOrdId(ordId); // 订单号(必须)
		bean.setOrdDate(ordDate); // 订单日期(必须)
		bean.setQueryTransType(queryTransType); // 交易查询类型
		// 写log用参数
		bean.setLogUserId(0);
		bean.setLogRemark("交易状态查询"); // 备注
		bean.setLogClient("0"); // PC
		// 调用汇付接口
		ChinapnrBean chinapnrBean = ChinapnrUtil.callApiBg(bean);
		if (chinapnrBean == null) {
			LogUtil.errorLog(this.getClass().getName(), methodName,
					new Exception("调用交易状态查询接口失败![参数：" + bean.getAllParams() + "]"));
			return null;
		}
		return chinapnrBean;
	}

	/**
	 * 资金（货款）解冻(调用汇付天下接口)
	 *
	 * @param trxId
	 * @return
	 * @throws Exception
	 */
	private ChinapnrBean usrUnFreeze(String trxId) throws Exception {

		String methodName = "usrUnFreeze";
		// 调用汇付接口
		ChinapnrBean bean = new ChinapnrBean();
		bean.setVersion(ChinaPnrConstant.VERSION_10); // 版本号(必须)
		bean.setCmdId(ChinaPnrConstant.CMDID_USR_UN_FREEZE); // 消息类型(必须)
		bean.setOrdId(GetOrderIdUtils.getOrderId2(0)); // 订单号(必须)
		bean.setOrdDate(GetOrderIdUtils.getOrderDate()); // 订单日期(必须)
		bean.setTrxId(trxId); // 本平台交易唯一标识(必须)
		bean.setBgRetUrl(ChinapnrUtil.getBgRetUrl()); // 商户后台应答地址(必须)
		// 写log用参数
		bean.setLogUserId(0);
		bean.setLogRemark("资金（货款）解冻"); // 备注
		bean.setLogClient("0"); // PC
		ChinapnrBean chinapnrBean = ChinapnrUtil.callApiBg(bean);
		if (Validator.isNull(chinapnrBean)) {
			LogUtil.errorLog(this.getClass().getName(), methodName,
					new Exception("调用解冻接口失败![参数：" + bean.getAllParams() + "]"));
			throw new Exception("调用交易查询接口(解冻)失败,[冻结标识：" + trxId + "]");
		} else if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(chinapnrBean.getRespCode())) {
			return chinapnrBean;
		} else {
			throw new Exception("调用交易查询接口(解冻)返回错误,[冻结标识：" + trxId + "]");
		}
	}


	/**
	 * 解冻
	 * @param userId
	 * @param orderId
	 * @param trxId
	 * @param orderDate
	 * @return
	 * @author Administrator
	 * @throws Exception 
	 */
		
	@Override
	public boolean unFreezeOrder(Integer userId, String orderId, String trxId, String orderDate) throws Exception {
		// 出借人的账户信息
		AccountChinapnr outCust = this.getAccountChinapnr(userId);
		if (outCust == null) {
			throw new Exception("出借人未开户。[出借人ID：" + userId + "]，" + "[出借订单号：" + orderId + "]");
		}
		// 调用交易查询接口(解冻)
		ChinapnrBean queryTransStatBean = queryTransStat(orderId, orderDate, "FREEZE");
		if (queryTransStatBean == null) {
			throw new Exception("调用交易查询接口(解冻)失败。" + ",[出借订单号：" + orderId + "]");
		} else {
			String queryRespCode = queryTransStatBean.getRespCode();
			System.out.print("出借失败交易接口查询接口返回码：" + queryRespCode);
			// 调用接口失败时(000以外)
			if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(queryRespCode)) {
				String message = queryTransStatBean == null ? "" : queryTransStatBean.getRespDesc();
				LogUtil.errorLog(this.getClass().getName(), "unFreezeOrder",
						"调用交易查询接口(解冻)失败。" + message + ",[出借订单号：" + orderId + "]", null);
				throw new Exception("调用交易查询接口(解冻)失败。" + queryRespCode + "：" + message + ",[出借订单号：" + orderId + "]");
			} else {
				// 汇付交易状态
				String transStat = queryTransStatBean == null ? "" : queryTransStatBean.getTransStat();
				// 冻结请求已经被解冻 U:已解冻 N:无需解冻，对于解冻交易
				if (!"U".equals(transStat) && !"N".equals(transStat)) {
					/** 解冻订单 */
					ChinapnrBean unFreezeBean = usrUnFreeze(trxId);
					String respCode = unFreezeBean == null ? "" : unFreezeBean.getRespCode();
					System.out.print("出借失败自动解冻接口返回码：" + respCode);
					// 调用接口失败时(000 或 107 以外)
					if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode)
							&& !ChinaPnrConstant.RESPCODE_REPEAT_DEAL.equals(respCode)) {
						String message = unFreezeBean == null ? "" : unFreezeBean.getRespDesc();
						message = "调用解冻接口失败。" + respCode + "：" + message + "，出借订单号[" + orderId + "]";
						LogUtil.errorLog(this.getClass().getName(), "unFreezeOrder", message, null);
						return false;
					} else {
						return true;
					}
				} else {
					return true;
				}
			}
		}
			
	}
}
