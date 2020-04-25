package com.hyjf.admin.manager.debt.debtborrowfirst;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.SessionUtils;
import com.hyjf.mybatis.model.auto.BorrowAppointExample;
import com.hyjf.mybatis.model.auto.DebtBail;
import com.hyjf.mybatis.model.auto.DebtBailExample;
import com.hyjf.mybatis.model.auto.DebtBorrowExample;
import com.hyjf.mybatis.model.auto.DebtBorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.AdminSystem;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowFirstCustomize;

@Service
public class DebtBorrowFirstServiceImpl extends BaseServiceImpl implements DebtBorrowFirstService {

	/**
	 * COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	@Override
	public Integer countBorrowFirst(DebtBorrowCommonCustomize debtBorrowCommonCustomize) {
		return this.debtBorrowFirstCustomizeMapper.countBorrowFirst(debtBorrowCommonCustomize);
	}

	/**
	 * 借款列表
	 * 
	 * @return
	 */
	@Override
	public List<DebtBorrowFirstCustomize> selectBorrowFirstList(DebtBorrowCommonCustomize debtBorrowCommonCustomize) {
		return this.debtBorrowFirstCustomizeMapper.selectBorrowFirstList(debtBorrowCommonCustomize);
	}

	/**
	 * 已缴保证金
	 * 
	 * @param record
	 */
	@Override
	public void borrowBailRecord(String borrowPreNid) {
		// 项目编号存在
		if (StringUtils.isNotEmpty(borrowPreNid)) {
			DebtBorrowExample example = new DebtBorrowExample();
			DebtBorrowExample.Criteria cra = example.createCriteria();
			cra.andBorrowPreNidEqualTo(Integer.valueOf(borrowPreNid));
			List<DebtBorrowWithBLOBs> debtBorrowList = this.debtBorrowMapper.selectByExampleWithBLOBs(example);
			if (debtBorrowList != null && debtBorrowList.size() > 0) {
				for (DebtBorrowWithBLOBs borrowWithBLOBs : debtBorrowList) {

					// 该项目编号没有交过保证金
					DebtBailExample exampleBail = new DebtBailExample();
					DebtBailExample.Criteria craBail = exampleBail.createCriteria();
					craBail.andBorrowNidEqualTo(borrowWithBLOBs.getBorrowNid());
					List<DebtBail> debtBailList = this.debtBailMapper.selectByExample(exampleBail);

					if (debtBailList == null || debtBailList.size() == 0) {
						AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
						DebtBail debtBail = new DebtBail();
						// 借款人的ID
						debtBail.setBorrowUid(borrowWithBLOBs.getUserId());
						// 操作人的ID
						debtBail.setOperaterUid(Integer.valueOf(adminSystem.getId()));
						// 项目编号
						debtBail.setBorrowNid(borrowWithBLOBs.getBorrowNid());
						// 保证金数值
						BigDecimal bailPercent = new BigDecimal(this.getBorrowConfig(CustomConstants.BORROW_BAIL_RATE));// 计算公式：保证金金额=借款金额×3％
						BigDecimal accountBail = (borrowWithBLOBs.getAccount()).multiply(bailPercent).setScale(2, BigDecimal.ROUND_DOWN);
						debtBail.setBailNum(accountBail);
						// 10位系统时间（到秒）
						debtBail.setUpdatetime(GetDate.getNowTime10());

						this.debtBailMapper.insertSelective(debtBail);
					}
				}
			}
		}
	}

	/**
	 * 已交保证金
	 * 
	 * @param borrow
	 * @return
	 * @author Administrator
	 */

	@Override
	public boolean getBorrowBail(String borrowNid) {
		DebtBailExample example = new DebtBailExample();
		DebtBailExample.Criteria cra = example.createCriteria();
		cra.andBorrowNidEqualTo(borrowNid);
		List<DebtBail> list = this.debtBailMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return true;
		}

		return false;
	}

	/**
	 * 更新
	 * 
	 * @param record
	 */
	@Override
	public void updateOntimeRecord(DebtBorrowFirstBean borrowBean) {
		// 插入时间
		long systemNowDateLong = new Date().getTime() / 1000;
		Date systemNowDate = GetDate.getDate(systemNowDateLong);

		String borrowNid = borrowBean.getBorrowNid();
		if (StringUtils.isNotEmpty(borrowNid)) {
			DebtBorrowExample debtBorrowExample = new DebtBorrowExample();
			DebtBorrowExample.Criteria borrowCra = debtBorrowExample.createCriteria();
			borrowCra.andBorrowNidEqualTo(borrowNid);

			List<DebtBorrowWithBLOBs> debtBorrowList = this.debtBorrowMapper.selectByExampleWithBLOBs(debtBorrowExample);

			if (debtBorrowList != null && debtBorrowList.size() == 1) {
				DebtBorrowWithBLOBs debtBorrow = debtBorrowList.get(0);
				// 剩余的金额
				debtBorrow.setBorrowAccountWait(debtBorrow.getAccount());
				int time = Integer.valueOf(String.valueOf(systemNowDateLong));
				// 当发标状态为立即发标时插入系统时间
				if (borrowBean.getVerifyStatus() != null && StringUtils.isNotEmpty(borrowBean.getVerifyStatus())) {
					// 发标方式为”暂不发标 3“或者”定时发标 1“时，项目状态变为”待发布“
					if (Integer.valueOf(borrowBean.getVerifyStatus()) == 1 || Integer.valueOf(borrowBean.getVerifyStatus()) == 3) {
						// 定时发标
						if (Integer.valueOf(borrowBean.getVerifyStatus()) == 1) {
							// 发标时间
							debtBorrow.setOntime(GetDate.strYYYYMMDDHHMMSS2Timestamp(borrowBean.getOntime()));
							debtBorrow.setBookingBeginTime(GetDate.strYYYYMMDDHHMMSS2Timestamp(borrowBean.getBookingBeginTime()));
							debtBorrow.setBookingEndTime(GetDate.strYYYYMMDDHHMMSS2Timestamp(borrowBean.getBookingEndTime()));
						} else if (Integer.valueOf(borrowBean.getVerifyStatus()) == 3) {
							// 发标时间
							debtBorrow.setOntime(0);
							debtBorrow.setBookingBeginTime(0);
							debtBorrow.setBookingEndTime(0);
						}
						// 状态
						debtBorrow.setStatus(0);
						// 初审状态
						debtBorrow.setVerifyStatus(Integer.valueOf(borrowBean.getVerifyStatus()));
					}
					// 发标方式为”立即发标 2“时，项目状态变为”出借中
					else if (Integer.valueOf(borrowBean.getVerifyStatus()) == 2) {
						// 是否可以进行借款
						debtBorrow.setBorrowStatus(1);
						// 初审时间
						debtBorrow.setVerifyTime(String.valueOf(GetDate.getNowTime10()));
						// 发标的状态
						debtBorrow.setVerifyStatus(Integer.valueOf(borrowBean.getVerifyStatus()));
						// 状态
						debtBorrow.setStatus(1);
						// 借款到期时间
						debtBorrow.setBorrowEndTime(String.valueOf(time + debtBorrow.getBorrowValidTime() * 86400));
						// borrowNid，借款的borrowNid,account借款总额
						RedisUtils.set(CustomConstants.DEBT_REDITS + borrowNid, debtBorrow.getAccount().toString());
					}

					// 更新时间
					debtBorrow.setUpdatetime(systemNowDate);

					this.debtBorrowMapper.updateByExampleSelective(debtBorrow, debtBorrowExample);
				}
			}
		}
	}

	/**
	 * 获取用户名
	 * 
	 * @param record
	 */
	@Override
	public String getUserName(Integer userId) {
		Users users = this.usersMapper.selectByPrimaryKey(userId);
		if (users != null && StringUtils.isNotEmpty(users.getUsername())) {
			return users.getUsername();
		} else {
			return "";
		}
	}

	@Override
	public String sumBorrowFirstAccount(DebtBorrowCommonCustomize debtBorrowCommonCustomize) {
		String sumAccount = this.debtBorrowFirstCustomizeMapper.sumBorrowFirstAccount(debtBorrowCommonCustomize);
		return sumAccount;
	}

	@Override
	public Boolean hasBookingRecord(String nid) {
		BorrowAppointExample example = new BorrowAppointExample();
		List<Integer> tempList = new ArrayList<Integer>();
		tempList.add(0);
		tempList.add(1);
		example.createCriteria().andBorrowNidEqualTo(nid).andAppointStatusIn(tempList);
		int countSize = this.borrowAppointMapper.countByExample(example);
		if (countSize != 0) {
			return true;
		} else {
			return false;
		}
	}

}
