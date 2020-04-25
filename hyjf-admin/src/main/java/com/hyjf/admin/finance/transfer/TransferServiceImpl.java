package com.hyjf.admin.finance.transfer;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.finance.returncash.ReturncashDefine;
import com.hyjf.common.security.utils.ThreeDESUtils;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.AccountChinapnrExample;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.UserTransfer;
import com.hyjf.mybatis.model.auto.UserTransferExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;

@Service
public class TransferServiceImpl extends BaseServiceImpl implements TransferService {

	private static String KEY = PropUtils.getSystem("hyjf.transfer.3des.key").trim();

	/**
	 * 获取权限列表
	 * 
	 * @return
	 */
	public List<UserTransfer> getRecordList(TransferListBean form, int limitStart,int limitEnd) {
		UserTransferExample example = new UserTransferExample();
		UserTransferExample.Criteria cra = example.createCriteria();
		if(StringUtils.isNotEmpty(form.getOutUserNameSrch())){
			cra.andOutUserNameLike("%"+form.getOutUserNameSrch()+"%");
		}
		if(StringUtils.isNotEmpty(form.getReconciliationIdSrch())){
			cra.andReconciliationIdLike("%"+form.getReconciliationIdSrch()+"%");
		}
		if(StringUtils.isNotEmpty(form.getStatusSrch())){
			cra.andStatusEqualTo(Integer.valueOf(form.getStatusSrch()));
		}
		if(StringUtils.isNotEmpty(form.getOrderIdSrch())){
			cra.andOrderIdEqualTo(form.getOrderIdSrch());
		}
		if(StringUtils.isNotEmpty(form.getOpreateTimeStart())){
			cra.andCreateTimeGreaterThanOrEqualTo(GetDate.stringToDate(GetDate.getDayStart(form.getOpreateTimeStart())));
		}
		if(StringUtils.isNotEmpty(form.getOpreateTimeEnd())){
			cra.andCreateTimeLessThanOrEqualTo(GetDate.stringToDate(GetDate.getDayEnd(form.getOpreateTimeEnd())));
		}
		if(StringUtils.isNotEmpty(form.getTransferTimeStart())){
			cra.andTransferTimeGreaterThanOrEqualTo(GetDate.stringToDate(GetDate.getDayStart(form.getTransferTimeStart())));
		}
		if(StringUtils.isNotEmpty(form.getTransferTimeEnd())){
			cra.andTransferTimeLessThanOrEqualTo(GetDate.stringToDate(GetDate.getDayEnd(form.getTransferTimeEnd())));
		}
		if(StringUtils.isNotEmpty(form.getReconciliationId())){
			cra.andReconciliationIdEqualTo(form.getReconciliationId());
		}
		if(StringUtils.isNotEmpty(form.getTransferTypeSrch())){
			cra.andTransferTypeEqualTo(Integer.valueOf(form.getTransferTypeSrch()));
		}
		example.setOrderByClause("create_time desc");
		if (limitStart != -1) {
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}
		return userTransferMapper.selectByExample(example);
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * 
	 * @param userListCustomizeBean
	 * @return
	 * @author Administrator
	 */

	@Override
	public int countRecordTotal(TransferListBean form) {
		UserTransferExample example = new UserTransferExample();
		UserTransferExample.Criteria cra = example.createCriteria();
		if(StringUtils.isNotEmpty(form.getOutUserNameSrch())){
			cra.andOutUserNameLike("%"+form.getOutUserNameSrch()+"%");
		}
		if(StringUtils.isNotEmpty(form.getReconciliationIdSrch())){
			cra.andReconciliationIdLike("%"+form.getReconciliationIdSrch()+"%");
		}
		if(StringUtils.isNotEmpty(form.getStatusSrch())){
			cra.andStatusEqualTo(Integer.valueOf(form.getStatusSrch()));
		}
		if(StringUtils.isNotEmpty(form.getOrderIdSrch())){
			cra.andOrderIdEqualTo(form.getOrderIdSrch());
		}
		if(StringUtils.isNotEmpty(form.getOpreateTimeStart())){
			cra.andCreateTimeGreaterThanOrEqualTo(GetDate.stringToDate(GetDate.getDayStart(form.getOpreateTimeStart())));
		}
		if(StringUtils.isNotEmpty(form.getOpreateTimeEnd())){
			cra.andCreateTimeLessThanOrEqualTo(GetDate.stringToDate(GetDate.getDayEnd(form.getOpreateTimeEnd())));
		}
		if(StringUtils.isNotEmpty(form.getTransferTimeStart())){
			cra.andTransferTimeGreaterThanOrEqualTo(GetDate.stringToDate(GetDate.getDayStart(form.getTransferTimeStart())));
		}
		if(StringUtils.isNotEmpty(form.getTransferTimeEnd())){
			cra.andTransferTimeLessThanOrEqualTo(GetDate.stringToDate(GetDate.getDayEnd(form.getTransferTimeEnd())));
		}
		if(StringUtils.isNotEmpty(form.getReconciliationId())){
			cra.andReconciliationIdEqualTo(form.getReconciliationId());
		}
		if(StringUtils.isNotEmpty(form.getTransferTypeSrch())){
			cra.andTransferTypeEqualTo(Integer.valueOf(form.getTransferTypeSrch()));
		}

		example.setOrderByClause("create_time desc");
		return userTransferMapper.countByExample(example);
	}
	/**
	 * 导出数据
	 * @param form
	 * @return
	 * @author Michael
	 */
		
	@Override
	public List<UserTransfer> exportRecordList(TransferListBean form) {
		UserTransferExample example = new UserTransferExample();
		UserTransferExample.Criteria cra = example.createCriteria();
		if(StringUtils.isNotEmpty(form.getOutUserNameSrch())){
			cra.andOutUserNameLike("%"+form.getOutUserNameSrch()+"%");
		}
		if(StringUtils.isNotEmpty(form.getReconciliationIdSrch())){
			cra.andReconciliationIdLike("%"+form.getReconciliationIdSrch()+"%");
		}
		if(StringUtils.isNotEmpty(form.getStatusSrch())){
			cra.andStatusEqualTo(Integer.valueOf(form.getStatusSrch()));
		}
		if(StringUtils.isNotEmpty(form.getOrderIdSrch())){
			cra.andOrderIdEqualTo(form.getOrderIdSrch());
		}
		if(StringUtils.isNotEmpty(form.getOpreateTimeStart())){
			cra.andCreateTimeGreaterThanOrEqualTo(GetDate.stringToDate(GetDate.getDayStart(form.getOpreateTimeStart())));
		}
		if(StringUtils.isNotEmpty(form.getOpreateTimeEnd())){
			cra.andCreateTimeLessThanOrEqualTo(GetDate.stringToDate(GetDate.getDayEnd(form.getOpreateTimeEnd())));
		}
		if(StringUtils.isNotEmpty(form.getTransferTimeStart())){
			cra.andTransferTimeGreaterThanOrEqualTo(GetDate.stringToDate(GetDate.getDayStart(form.getTransferTimeStart())));
		}
		if(StringUtils.isNotEmpty(form.getTransferTimeEnd())){
			cra.andTransferTimeLessThanOrEqualTo(GetDate.stringToDate(GetDate.getDayEnd(form.getTransferTimeEnd())));
		}
		if(StringUtils.isNotEmpty(form.getReconciliationId())){
			cra.andReconciliationIdEqualTo(form.getReconciliationId());
		}
		if(StringUtils.isNotEmpty(form.getTransferTypeSrch())){
			cra.andTransferTypeEqualTo(Integer.valueOf(form.getTransferTypeSrch()));
		}
		example.setOrderByClause("create_time desc");
		return userTransferMapper.selectByExample(example);
	}

	
	
	@Override
	public void checkTransfer(String outUserName, JSONObject ret) {
		UsersExample userExample = new UsersExample();
		UsersExample.Criteria userCrt = userExample.createCriteria();
		userCrt.andUsernameEqualTo(outUserName);
		List<Users> users = this.usersMapper.selectByExample(userExample);
		if (users != null && users.size() == 1) {
			Users user = users.get(0);
			AccountChinapnrExample chinapnrExample = new AccountChinapnrExample();
			AccountChinapnrExample.Criteria chinapnrCrt = chinapnrExample.createCriteria();
			chinapnrCrt.andUserIdEqualTo(user.getUserId());
			List<AccountChinapnr> chinapnrs = this.accountChinapnrMapper.selectByExample(chinapnrExample);
			if (chinapnrs != null && chinapnrs.size() == 1) {
				AccountExample accountExample = new AccountExample();
				AccountExample.Criteria accountCrt = accountExample.createCriteria();
				accountCrt.andUserIdEqualTo(user.getUserId());
				List<Account> accounts = this.accountMapper.selectByExample(accountExample);
				if (accounts != null && accounts.size() == 1) {
					ret.put(ReturncashDefine.JSON_VALID_STATUS_KEY, ReturncashDefine.JSON_VALID_STATUS_OK);
				} else {
					ret.put(TransferDefine.JSON_VALID_INFO_KEY, "未查询到正确的余额信息!");
				}
			} else {
				ret.put(TransferDefine.JSON_VALID_INFO_KEY, "用户未开户，无法转账!");
			}
		} else {
			ret.put(TransferDefine.JSON_VALID_INFO_KEY, "未查询到正确的用户信息!");
		}
	}
	
	@Override
	public void searchBalance(String outUserName, JSONObject ret) {

		UsersExample userExample = new UsersExample();
		UsersExample.Criteria userCrt = userExample.createCriteria();
		userCrt.andUsernameEqualTo(outUserName);
		List<Users> users = this.usersMapper.selectByExample(userExample);
		if (users != null && users.size() == 1) {
			Users user = users.get(0);
			AccountChinapnrExample chinapnrExample = new AccountChinapnrExample();
			AccountChinapnrExample.Criteria chinapnrCrt = chinapnrExample.createCriteria();
			chinapnrCrt.andUserIdEqualTo(user.getUserId());
			List<AccountChinapnr> chinapnrs = this.accountChinapnrMapper.selectByExample(chinapnrExample);
			if (chinapnrs != null && chinapnrs.size() == 1) {
				AccountExample accountExample = new AccountExample();
				AccountExample.Criteria accountCrt = accountExample.createCriteria();
				accountCrt.andUserIdEqualTo(user.getUserId());
				List<Account> accounts = this.accountMapper.selectByExample(accountExample);
				if (accounts != null && accounts.size() == 1) {
					Account account = accounts.get(0);
					ret.put(ReturncashDefine.JSON_STATUS_KEY, ReturncashDefine.JSON_STATUS_OK);
					ret.put(ReturncashDefine.JSON_RESULT_KEY, account.getBalance().toString());
				} else {
					ret.put(TransferDefine.JSON_STATUS_KEY, TransferDefine.JSON_STATUS_NG);
					ret.put(TransferDefine.JSON_RESULT_KEY, "未查询到正确的余额信息!");
				}
			} else {
				ret.put(TransferDefine.JSON_STATUS_KEY, TransferDefine.JSON_STATUS_NG);
				ret.put(TransferDefine.JSON_RESULT_KEY, "用户未开户，无法转账!");
			}
		} else {
			ret.put(TransferDefine.JSON_STATUS_KEY, TransferDefine.JSON_STATUS_NG);
			ret.put(TransferDefine.JSON_RESULT_KEY, "未查询到正确的用户信息!");
		}
	}

	@Override
	public boolean insertTransfer(TransferCustomizeBean form) {

		Date nowTime = new Date();
		UserTransfer userTransfer = new UserTransfer();
		// 生成订单
		String orderId = GetOrderIdUtils.getOrderId2(Integer.valueOf(form.getOutUserId()));
		userTransfer.setOrderId(orderId);
		userTransfer.setOutUserId(form.getOutUserId());
		userTransfer.setOutUserName(form.getOutUserName());
		userTransfer.setTransferAmount(form.getTransferAmount());
		userTransfer.setTransferType(0);
		userTransfer.setStatus(0);
		userTransfer.setRemark(form.getRemark());
		userTransfer.setCreateUserId(form.getCreateUserId());
		userTransfer.setCreateTime(nowTime);
		userTransfer.setCreateUserName(form.getCreateUserName());
		// 转账url
		String transferUrl = "";
		try {
			transferUrl = TransferDefine.WEB_TRANSFER_URL + "?orderId="
					+ ThreeDESUtils.Encrypt3DES(KEY, userTransfer.getOrderId());
			userTransfer.setTransferUrl(transferUrl);
			boolean flag = this.userTransferMapper.insertSelective(userTransfer) > 0 ? true : false;
			if (flag) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void checkTransferParam(ModelAndView modelAndView, TransferCustomizeBean form) {

		if (StringUtils.isNotBlank(form.getOutUserName())) {
			UsersExample userExample = new UsersExample();
			UsersExample.Criteria userCrt = userExample.createCriteria();
			userCrt.andUsernameEqualTo(form.getOutUserName());
			List<Users> users = this.usersMapper.selectByExample(userExample);
			if (users != null && users.size() == 1) {
				Users user = users.get(0);
				form.setOutUserId(user.getUserId());
				AccountChinapnrExample chinapnrExample = new AccountChinapnrExample();
				AccountChinapnrExample.Criteria chinapnrCrt = chinapnrExample.createCriteria();
				chinapnrCrt.andUserIdEqualTo(user.getUserId());
				List<AccountChinapnr> chinapnrs = this.accountChinapnrMapper.selectByExample(chinapnrExample);
				if (chinapnrs != null && chinapnrs.size() == 1) {
					AccountExample accountExample = new AccountExample();
					AccountExample.Criteria accountCrt = accountExample.createCriteria();
					accountCrt.andUserIdEqualTo(user.getUserId());
					List<Account> accounts = this.accountMapper.selectByExample(accountExample);
					if (accounts != null && accounts.size() == 1) {
						
					} else {
						ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "outUsername",
								"transfer.outuserbalance", "未查询到正确的余额信息!");
					}
				} else {
					ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "outUsername", "transfer.outuseraccount",
							"用户未开户，无法转账!");
				}
			} else {
				ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "outUsername", "transfer.outusername",
						"未查询到正确的用户信息!");
			}
		} else {
			ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "outUsername", "transfer.outusername",
					"用户名不能为空!");
		}

	}

	@Override
	public UserTransfer searchUserTransferById(int id) {
		UserTransfer userTransfer = this.userTransferMapper.selectByPrimaryKey(id);
		return userTransfer;
	}

	@Override
	public Users searchUserByUserId(Integer userId) {
		Users user = this.usersMapper.selectByPrimaryKey(userId);
		return user;
	}


}
