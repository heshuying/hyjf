/**
 * Description:用户出借实现类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 郭勇
 * @version: 1.0
 * Created at: 2015年12月4日 下午1:50:02
 * Modification History:
 * Modified by :
 */

package com.hyjf.web.user.transfer;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.AccountChinapnrExample;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AccountWebList;
import com.hyjf.mybatis.model.auto.AccountWebListExample;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.SpreadsUsersExample;
import com.hyjf.mybatis.model.auto.UserTransfer;
import com.hyjf.mybatis.model.auto.UserTransferExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.EmployeeCustomize;
import com.hyjf.web.BaseServiceImpl;

@Service
public class TransferServiceImpl extends BaseServiceImpl implements TransferService {

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Autowired
	private TransactionDefinition transactionDefinition;

	/**
	 * 获取用户的汇付信息
	 *
	 * @param userId
	 * @return 用户的汇付信息
	 */
	@Override
	public AccountChinapnr getAccountChinapnr(Integer userId) {
		AccountChinapnr accountChinapnr = null;
		if (userId == null) {
			return null;
		}
		AccountChinapnrExample example = new AccountChinapnrExample();
		AccountChinapnrExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		List<AccountChinapnr> list = accountChinapnrMapper.selectByExample(example);
		if (list != null && !list.isEmpty()) {
			accountChinapnr = list.get(0);
		}
		return accountChinapnr;
	}

	/**
	 * 校验债转参数
	 */
	@Override
	public UserTransfer checkTransferParam(String orderId, JSONObject ret) {

		int nowTime = GetDate.getNowTime10();
		if (StringUtils.isNotBlank(orderId)) {
			UserTransferExample example = new UserTransferExample();
			UserTransferExample.Criteria crt = example.createCriteria();
			crt.andOrderIdEqualTo(orderId);
			List<UserTransfer> userTransfers = this.userTransferMapper.selectByExample(example);
			if (userTransfers != null && userTransfers.size() == 1) {
				UserTransfer userTransfer = userTransfers.get(0);
				if (userTransfer.getStatus() == 0 || userTransfer.getStatus() == 1) {
					Date date = userTransfer.getCreateTime();
					int creatTime = (int) (GetDate.getMillis(date) / 1000);
					int time24 = creatTime + 24 * 60 * 60;
					if (creatTime <= nowTime && nowTime <= time24) {
						if (userTransfer.getOutUserId() != null) {
							int outUserId = userTransfer.getOutUserId();
							Users user = this.usersMapper.selectByPrimaryKey(outUserId);
							if (user == null) {
								ret.put("error", "1");
								ret.put("data", "未查询到相应的用户信息");
							} else {
								AccountChinapnrExample chinapnrExample = new AccountChinapnrExample();
								AccountChinapnrExample.Criteria chinapnrCrt = chinapnrExample.createCriteria();
								chinapnrCrt.andUserIdEqualTo(outUserId);
								List<AccountChinapnr> chinapnrs = this.accountChinapnrMapper
										.selectByExample(chinapnrExample);
								if (chinapnrs != null && chinapnrs.size() == 1) {
									AccountExample accountExample = new AccountExample();
									AccountExample.Criteria accountCrt = accountExample.createCriteria();
									accountCrt.andUserIdEqualTo(user.getUserId());
									List<Account> accounts = this.accountMapper.selectByExample(accountExample);
									if (accounts != null && accounts.size() == 1) {
										Account account = accounts.get(0);
										BigDecimal balance = account.getBalance();
										if (balance.compareTo(userTransfer.getTransferAmount()) == -1) {
											ret.put("error", "1");
											ret.put("data", "用户余额不足!");
										} else {
											ret.put("error", "0");
											ret.put("data", "校验通过");
										}
									} else {
										ret.put("error", "1");
										ret.put("data", "未查询到正确的余额信息!");
									}
								} else {
									ret.put("error", "1");
									ret.put("data", "用户未开户，无法转账!");
								}
							}
						} else {
							ret.put("error", "1");
							ret.put("data", "参数错误，请联系系统管理员！");
						}
					} else {
						userTransfer.setStatus(4);
						this.userTransferMapper.updateByPrimaryKey(userTransfer);
						ret.put("error", "1");
						ret.put("data", "此链接已经失效！");
					}
				} else if (userTransfer.getStatus() == 2) {
					ret.put("error", "1");
					ret.put("status", "2");
					ret.put("data", "此笔转账已经成功,请勿重复操作！");
				} else if (userTransfer.getStatus() == 3) {
					ret.put("error", "1");
					ret.put("data", "此笔转账已经失败,请勿重复操作！");
				} else if (userTransfer.getStatus() == 4) {
					ret.put("error", "1");
					ret.put("data", "此链接已经失效！");
				}
				return userTransfer;
			} else {
				ret.put("error", "1");
				ret.put("data", "链接错误,未查询到此笔转账！");
			}
		} else {
			ret.put("error", "1");
			ret.put("data", "链接错误,请检查后重试！");
		}
		return null;
	}

	/**
	 * 更新用户转账状态为初始
	 */
	@Override
	public boolean updateUserTransfer(UserTransfer userTransfer) {

		Date date = new Date();
		UserTransferExample transferExample = new UserTransferExample();
		UserTransferExample.Criteria crt = transferExample.createCriteria();
		if (userTransfer.getStatus() == 0) {
			crt.andCreateTimeEqualTo(userTransfer.getCreateTime());
		} else if (userTransfer.getStatus() == 1) {
			crt.andUpdateTimeEqualTo(userTransfer.getUpdateTime());
		}
		userTransfer.setUpdateTime(date);
		userTransfer.setTransferTime(date);
		userTransfer.setStatus(1);
		boolean flag = this.userTransferMapper.updateByPrimaryKeySelective(userTransfer) > 0 ? true : false;
		return flag;
	}

	/**
	 * @throws Exception
	 * 
	 */
	@Override
	public boolean updateAfterUserTansfer(UserTransfer userTransfer, String ip) throws Exception {

		// 处理转出账户
		TransactionStatus txStatus = null;
		// 系统时间
		int nowTime = GetDate.getNowTime10();
		Date date = new Date();
		// 转出账户
		int outUserId = userTransfer.getOutUserId();
		// 订单号
		String orderId = userTransfer.getOrderId();
		// 转账金额
		BigDecimal amout = userTransfer.getTransferAmount();
		// ================开始处理================
		try {
			// 开启事务
			txStatus = this.transactionManager.getTransaction(transactionDefinition);
			// 查询用户的交易信息
			AccountExample example = new AccountExample();
			AccountExample.Criteria criteria = example.createCriteria();
			criteria.andUserIdEqualTo(outUserId);
			List<Account> accountList = accountMapper.selectByExample(example);
			if (accountList != null && accountList.size() == 1) {
				Account account = accountList.get(0);
				// 1可用余额减去转账金额,资产总额减去转账金额
				Account accountNew = new Account();
				accountNew.setBalance(amout);
				accountNew.setTotal(amout);
				accountNew.setUserId(outUserId);
				boolean accountFlag = this.adminAccountCustomizeMapper.updateOfTransfer(accountNew) > 0 ? true : false;
				// 2转出账户资金明细添加一条收支记录，支出，用户转账
				if (accountFlag) {
					AccountList accountListNew = new AccountList();
					accountListNew.setAmount(amout);
					accountListNew.setAwait(new BigDecimal(0));
					accountListNew.setBalance(account.getBalance().subtract(amout));
					accountListNew.setBaseUpdate(0);
					accountListNew.setCreateTime(nowTime);
					accountListNew.setFrost(account.getFrost());
					accountListNew.setInterest(new BigDecimal(0));
					accountListNew.setIp(ip);
					accountListNew.setIsUpdate(0);
					accountListNew.setNid(orderId);
					accountListNew.setOperator(String.valueOf(outUserId));
					accountListNew.setRemark("用户转账");
					accountListNew.setRepay(new BigDecimal(0));
					accountListNew.setTotal(account.getTotal().subtract(amout));
					// 用户转账
					accountListNew.setTrade("user_transfer");
					accountListNew.setTradeCode("balance");
					// 收支类型1收入2支出3冻结
					accountListNew.setType(2);
					accountListNew.setUserId(outUserId);
					accountListNew.setWeb(0);
					System.out.println("用户转账:" + outUserId + "******预插入accountList：" + JSON.toJSONString(accountList));
					boolean accountListFlag = accountListMapper.insertSelective(accountListNew) > 0 ? true : false;
					if (accountListFlag) {
						// 3更新转账状态为成功
						UserTransferExample userTransferExample = new UserTransferExample();
						UserTransferExample.Criteria crt = userTransferExample.createCriteria();
						crt.andIdEqualTo(userTransfer.getId());
						crt.andUpdateTimeEqualTo(userTransfer.getUpdateTime());
						userTransfer.setStatus(2);
						userTransfer.setUpdateTime(date);
						boolean userTransferFlag = this.userTransferMapper.updateByExampleSelective(userTransfer,
								userTransferExample) > 0 ? true : false;
						if (userTransferFlag) {
							// 4网站收支增加一条收支记录，收入，用户转账
							// 插入网站收支明细记录
							AccountWebList accountWebList = new AccountWebList();
							// 订单号
							accountWebList.setOrdid(orderId);
							// 转账人
							accountWebList.setUserId(outUserId);
							// 转账金额
							accountWebList.setAmount(amout);
							// 类型1收入,2支出
							accountWebList.setType(CustomConstants.TYPE_IN);
							// 用户转账
							accountWebList.setTrade(CustomConstants.TRADE_TRANSFER);
							// 用户转账
							accountWebList.setTradeType(CustomConstants.TRADE_TRANSFER_NM);
							// 转账订单号
							accountWebList.setRemark(orderId);
							// 系统时间
							accountWebList.setCreateTime(nowTime);
							boolean accountWebFlag = insertAccountWebList(accountWebList) > 0 ? true : false;
							if (accountWebFlag) {
								// 提交事务
								this.transactionManager.commit(txStatus);
								return true;
							} else {
								throw new Exception(
										"网站收支记录(huiyingdai_account_web_list)更新失败！" + "[用户转账订单号：" + orderId + "]");
							}
						} else {
							throw new Exception(
									"用户转账:" + outUserId + "[用户转账订单号：" + orderId + "]" + "******更新用户userTransfer表失败");
						}
					} else {
						throw new Exception(
								"用户转账:" + outUserId + "[用户转账订单号：" + orderId + "]" + "******更新用户accountList表失败");
					}
				} else {
					throw new Exception("用户转账:" + outUserId + "[用户转账订单号：" + orderId + "]" + "******更新用户account表失败");
				}
			} else {
				throw new Exception("用户转账:" + outUserId + "[用户转账订单号：" + orderId + "]" + "******未查询到用户account表信息");
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 回滚事务
			transactionManager.rollback(txStatus);
			UserTransfer userTransferUpdate = this.searchUserTransfer(userTransfer.getId());
			if (userTransferUpdate.getStatus() == 1) {
				return false;
			} else if (userTransferUpdate.getStatus() == 2) {
				return true;
			} else if (userTransferUpdate.getStatus() == 3) {
				return false;
			}
		}
		return false;
	}

	@Override
	public boolean updateUserTransferFail(UserTransfer userTransfer, Date date) {
		// 3更新转账状态为失败
		UserTransferExample userTransferExample = new UserTransferExample();
		UserTransferExample.Criteria crt = userTransferExample.createCriteria();
		crt.andIdEqualTo(userTransfer.getId());
		crt.andUpdateTimeEqualTo(userTransfer.getUpdateTime());
		userTransfer.setStatus(3);
		userTransfer.setUpdateTime(date);
		boolean userTransferFlag = this.userTransferMapper.updateByExampleSelective(userTransfer,
				userTransferExample) > 0 ? true : false;
		if (userTransferFlag) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 根据主键查询用户转账记录
	 * 
	 * @param id
	 * @return
	 */
	private UserTransfer searchUserTransfer(int id) {
		UserTransfer userTransfer = this.userTransferMapper.selectByPrimaryKey(id);
		return userTransfer;
	}

	/**
	 * 插入网站收支记录
	 *
	 * @param nid
	 * @return
	 */
	private int insertAccountWebList(AccountWebList accountWebList) {
		if (countAccountWebList(accountWebList.getOrdid(), accountWebList.getTrade()) == 0) {
			// 设置部门信息
			setDepartments(accountWebList);
			// 插入
			return this.accountWebListMapper.insertSelective(accountWebList);
		}
		return 0;
	}

	/**
	 * 判断网站收支是否存在
	 *
	 * @param nid
	 * @return
	 */
	private int countAccountWebList(String nid, String trade) {
		AccountWebListExample example = new AccountWebListExample();
		example.createCriteria().andOrdidEqualTo(nid).andTradeEqualTo(trade);
		return this.accountWebListMapper.countByExample(example);
	}

	/**
	 * 设置部门名称
	 *
	 * @param accountWebList
	 */
	private void setDepartments(AccountWebList accountWebList) {
		if (accountWebList != null) {
			Integer userId = accountWebList.getUserId();
			UsersInfo usersInfo = getUsersInfoByUserId(userId);

			if (usersInfo != null) {

				Integer attribute = usersInfo.getAttribute();

				if (attribute != null) {
					// 查找用户的的推荐人
					Users users = getUsersByUserId(userId);

					Integer refUserId = users.getReferrer();
					SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
					SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
					spreadsUsersExampleCriteria.andUserIdEqualTo(userId);
					List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
					if (sList != null && !sList.isEmpty()) {
						refUserId = sList.get(0).getSpreadsUserid();
					}

					// 如果是线上员工或线下员工，推荐人的userId和username不插
					if (users != null && (attribute == 2 || attribute == 3)) {
						// 查找用户信息
						EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(userId);
						if (employeeCustomize != null) {
							accountWebList.setRegionName(employeeCustomize.getRegionName());
							accountWebList.setBranchName(employeeCustomize.getBranchName());
							accountWebList.setDepartmentName(employeeCustomize.getDepartmentName());
						}
					}
					// 如果是无主单，全插
					else if (users != null && (attribute == 1)) {
						// 查找用户推荐人
						EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(refUserId);
						if (employeeCustomize != null) {
							accountWebList.setRegionName(employeeCustomize.getRegionName());
							accountWebList.setBranchName(employeeCustomize.getBranchName());
							accountWebList.setDepartmentName(employeeCustomize.getDepartmentName());
						}
					}
					// 如果是有主单
					else if (users != null && (attribute == 0)) {
						// 查找用户推荐人
						EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(refUserId);
						if (employeeCustomize != null) {
							accountWebList.setRegionName(employeeCustomize.getRegionName());
							accountWebList.setBranchName(employeeCustomize.getBranchName());
							accountWebList.setDepartmentName(employeeCustomize.getDepartmentName());
						}
					}
				}
				accountWebList.setTruename(usersInfo.getTruename());
				accountWebList.setFlag(1);
			}
		}

	}

	/**
	 * 根据用户ID取得用户信息
	 *
	 * @param userId
	 * @return
	 */
	private Users getUsersByUserId(Integer userId) {
		if (userId != null) {
			UsersExample example = new UsersExample();
			example.createCriteria().andUserIdEqualTo(userId);
			List<Users> usersList = this.usersMapper.selectByExample(example);
			if (usersList != null && usersList.size() > 0) {
				return usersList.get(0);
			}
		}
		return null;
	}
	/**
	 * 查询相应的用户转账
	 * @param userTransfer
	 * @return
	 * @author Administrator
	 */
		
	@Override
	public UserTransfer selectUserTransfer(UserTransfer userTransfer) {
		return userTransferMapper.selectByPrimaryKey(userTransfer.getId());
			
	}
}