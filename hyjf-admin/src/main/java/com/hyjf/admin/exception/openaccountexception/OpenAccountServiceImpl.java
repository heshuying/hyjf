package com.hyjf.admin.exception.openaccountexception;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.mapper.auto.BankOpenAccountMapper;
import com.hyjf.mybatis.model.auto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.customize.admin.AdminOpenAccountExceptionCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;

@Service
public class OpenAccountServiceImpl extends BaseServiceImpl implements OpenAccountService {

	@Override
	public int countRecordTotal(Map<String, Object> accountUser) {
		int total = this.adminOpenAccountExceptionCustomizeMapper.countOpenAccountExceptionRecordTotal(accountUser);
		return total;
	}

	@Override
	public List<AdminOpenAccountExceptionCustomize> getRecordList(Map<String, Object> accountUser, int limitStart,
			int limitEnd) {

		if (limitStart == 0 || limitStart > 0) {
			accountUser.put("limitStart", limitStart);
		}
		if (limitEnd > 0) {
			accountUser.put("limitEnd", limitEnd);
		}
		List<AdminOpenAccountExceptionCustomize> list = this.adminOpenAccountExceptionCustomizeMapper
				.selectOpenAccountExceptionList(accountUser);
		return list;
	}

    @Override
    public int insertOpenAccountRecord(ChinapnrBean bean) {

        int nowTime = GetDate.getNowTime10(); // 当前时间
        int ret = 0;
        // 注册人ID
        Integer userId = bean.getLogUserId();
        // 身份证号
        String idCard = bean.getCertId();
        // 真实姓名
        String trueName = null;
        try {
            trueName = URLDecoder.decode(bean.getUsrName(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 性别
        int sexInt = Integer.parseInt(idCard.substring(16, 17));
        if (sexInt % 2 == 0) {
            sexInt = 2;
        } else {
            sexInt = 1;
        }
        // 出生日期
        String birthDay = idCard.substring(6, 14);
        // 判断是否存在
        UsersExample uexample = new UsersExample();
        UsersExample.Criteria crtu = uexample.createCriteria();
        crtu.andUserIdEqualTo(userId);
        List<Users> users = usersMapper.selectByExample(uexample);
        if (users != null && users.size() == 1) {
            // 更新用户表
            Users user = users.get(0);
            user.setOpenAccount(1); // 已经开户
            user.setMobile(bean.getUsrMp()); // 更新手机号
            user.setAccountEsb(0);
            user.setRechargeSms(0);
            user.setWithdrawSms(0);
            ret += usersMapper.updateByPrimaryKeySelective(user);

            // 更新用户详细信息表
            UsersInfoExample uiexample = new UsersInfoExample();
            UsersInfoExample.Criteria crtui = uiexample.createCriteria();
            crtui.andUserIdEqualTo(userId);
            List<UsersInfo> userInfos = usersInfoMapper.selectByExample(uiexample);
            if (userInfos != null && userInfos.size() == 1) {
                UsersInfo usersInfo = userInfos.get(0);
                usersInfo.setTruename(trueName);
                usersInfo.setIdcard(idCard);
                usersInfo.setSex(sexInt);
                usersInfo.setBirthday(birthDay);
                usersInfo.setTruenameIsapprove(1);
                ret += usersInfoMapper.updateByPrimaryKeySelective(usersInfo);
            }

            // 插入汇付关联表
//			AccountChinapnr accountChinapnr = new AccountChinapnr();
//			accountChinapnr.setUserId(userId);
//			accountChinapnr.setChinapnrUsrid(bean.getUsrId());
//			accountChinapnr.setChinapnrUsrcustid(Long.valueOf(bean.getUsrCustId()));
//			accountChinapnr.setAddtime(String.valueOf(nowTime));
//			accountChinapnr.setAddip(bean.getLogIp());
//			accountChinapnr.setIsok(1);
//			accountChinapnr.setEggsIsok(0);
//			ret += this.accountChinapnrMapper.insertSelective(accountChinapnr);

        }
        return ret;
    }
	
	
	@Override
	public int insertOpenAccountRecord(ChinapnrBean bean, String accountId) {

		int nowTime = GetDate.getNowTime10(); // 当前时间
		int ret = 0;
		// 注册人ID
		Integer userId = bean.getLogUserId();
		// 身份证号
		String idCard = bean.getCertId();
		// 真实姓名
		String trueName = null;
		try {
			trueName = URLDecoder.decode(bean.getUsrName(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// 性别
		int sexInt = Integer.parseInt(idCard.substring(16, 17));
		if (sexInt % 2 == 0) {
			sexInt = 2;
		} else {
			sexInt = 1;
		}
		// 出生日期
		String birthDay = idCard.substring(6, 14);
		// 判断是否存在
		UsersExample uexample = new UsersExample();
		UsersExample.Criteria crtu = uexample.createCriteria();
		crtu.andUserIdEqualTo(userId);
		List<Users> users = usersMapper.selectByExample(uexample);
		if (users != null && users.size() == 1) {
			// 更新用户表
			Users user = users.get(0);
			user.setOpenAccount(1); // 已经开户
			user.setMobile(bean.getUsrMp()); // 更新手机号
			user.setAccountEsb(0);
			user.setRechargeSms(0);
			user.setWithdrawSms(0);
			ret += usersMapper.updateByPrimaryKeySelective(user);

			// 更新用户详细信息表
			UsersInfoExample uiexample = new UsersInfoExample();
			UsersInfoExample.Criteria crtui = uiexample.createCriteria();
			crtui.andUserIdEqualTo(userId);
			List<UsersInfo> userInfos = usersInfoMapper.selectByExample(uiexample);
			if (userInfos != null && userInfos.size() == 1) {
				UsersInfo usersInfo = userInfos.get(0);
				usersInfo.setTruename(trueName);
				usersInfo.setIdcard(idCard);
				usersInfo.setSex(sexInt);
				usersInfo.setBirthday(birthDay);
				usersInfo.setTruenameIsapprove(1);
				ret += usersInfoMapper.updateByPrimaryKeySelective(usersInfo);
			}

			// 插入汇付关联表
//			AccountChinapnr accountChinapnr = new AccountChinapnr();
//			accountChinapnr.setUserId(userId);
//			accountChinapnr.setChinapnrUsrid(bean.getUsrId());
//			accountChinapnr.setChinapnrUsrcustid(Long.valueOf(bean.getUsrCustId()));
//			accountChinapnr.setAddtime(String.valueOf(nowTime));
//			accountChinapnr.setAddip(bean.getLogIp());
//			accountChinapnr.setIsok(1);
//			accountChinapnr.setEggsIsok(0);
//			ret += this.accountChinapnrMapper.insertSelective(accountChinapnr);

            BankOpenAccountExample example = new BankOpenAccountExample();
            BankOpenAccountExample.Criteria criteria = example.createCriteria();
            criteria.andUserIdEqualTo(userId);
            List<BankOpenAccount> bankOpenAccounts = bankOpenAccountMapper.selectByExample(example);

            BankOpenAccount bankOpenAccount = new BankOpenAccount();
            bankOpenAccount.setUserId(userId);
            bankOpenAccount.setUpdateTime(new Date());
            bankOpenAccount.setAccount(accountId);
            bankOpenAccount.setUserName(user.getUsername());
            bankOpenAccount.setCreateTime(new Date());
            bankOpenAccount.setCreateUserId(userId);
            bankOpenAccount.setCreateUserName(user.getUsername());
            if (bankOpenAccounts == null) {
                ret += bankOpenAccountMapper.insert(bankOpenAccount);
            } else {
                BankOpenAccountExample accountExample = new BankOpenAccountExample();
                BankOpenAccountExample.Criteria accountExampleCriteria = accountExample.createCriteria();
                accountExampleCriteria.andIdEqualTo(bankOpenAccounts.get(0).getId());
                ret += bankOpenAccountMapper.updateByPrimaryKey(bankOpenAccount);
            }

        }
		return ret;
	}

	@Override
	public Users getUserByUserId(String userId) {
		Users user = this.usersMapper.selectByPrimaryKey(Integer.parseInt(userId));
		return user;
	}

	@Override
	public AccountChinapnr getAccountChinapnr(String usrId) {
		AccountChinapnrExample example = new AccountChinapnrExample();
		example.createCriteria().andChinapnrUsridEqualTo(usrId);
		List<AccountChinapnr> accounts = accountChinapnrMapper.selectByExample(example);
		if (accounts != null && accounts.size() > 0) {
			return accounts.get(0);
		}
		return null;
	}

	@Override
	public List<ChinapnrLog> getOpenAccountLog(String userId) {
		
		ChinapnrLogExample example =new ChinapnrLogExample();
		ChinapnrLogExample.Criteria crt = example.createCriteria();
		crt.andUserIdEqualTo(Integer.parseInt(userId));
		crt.andRespCodeEqualTo("000");
		List<ChinapnrLog> chinapnrLogs= chinapnrLogMapper.selectByExampleWithBLOBs(example); 
		return chinapnrLogs;
	}

}
