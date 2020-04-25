package com.hyjf.batch;

import java.io.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.lang.StringUtils;

import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.mybatis.customize.CustomizeMapper;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.AccountChinapnrExample;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BankOpenAccountExample;
import com.hyjf.mybatis.model.auto.BankReturnCodeConfig;
import com.hyjf.mybatis.model.auto.BankReturnCodeConfigExample;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.ChinapnrLog;
import com.hyjf.mybatis.model.auto.HjhPlanAsset;
import com.hyjf.mybatis.model.auto.HjhPlanAssetExample;
import com.hyjf.mybatis.model.auto.SmsCode;
import com.hyjf.mybatis.model.auto.SmsLogWithBLOBs;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.SpreadsUsersExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

public class BaseServiceImpl extends CustomizeMapper implements BaseService {


    /**
     * 添加短信记录表
     *
     * @param content
     *            短信内容
     * @param mobile
     *            手机号码
     * @param checkCode
     *            验证码(没有验证码，设为空)
     * @param remark
     *            发送备注，如 注册填写“注册”
     * @param status
     *            发送状态，第三方返回状态
     * @return
     */

    public boolean AddMessage(String content, String mobile, String checkCode, String remark, int status) {

        if (StringUtils.isNotEmpty(mobile) && StringUtils.isNotEmpty(content)) {
            // 带验证码
            if (checkCode != null) {
                SmsCode smsCode = new SmsCode();
                smsCode.setMobile(mobile);
                smsCode.setCheckfor(MD5.toMD5Code(mobile + checkCode));
                smsCode.setCheckcode(checkCode);
                smsCode.setPosttime(GetDate.getNowTime10());
                smsCode.setStatus(status);
                smsCodeMapper.insertSelective(smsCode);
            }

            // 插入短信记录
            SmsLogWithBLOBs smsLog = new SmsLogWithBLOBs();

            smsLog.setMobile(mobile);
            smsLog.setContent(content);
            smsLog.setPosttime(GetDate.getNowTime10());
            smsLog.setStatus(status);
            smsLog.setType(remark);
            smsLogMapper.insertSelective(smsLog);
            return true;
        }
        return false;
    }

    /**
     * 写入日志
     *
     * @return
     */
    public int insertChinapnrLog(ChinapnrLog chinapnrLog) {
        return chinapnrLogMapper.insertSelective(chinapnrLog);
    }

    /**
     * 获取用户在汇付天下的账号信息
     *
     * @return
     */
    public AccountChinapnr getChinapnrUserInfo(Integer userId) {
        if (userId != null) {
            AccountChinapnrExample example = new AccountChinapnrExample();
            AccountChinapnrExample.Criteria criteria = example.createCriteria();
            criteria.andUserIdEqualTo(userId);
            List<AccountChinapnr> list = this.accountChinapnrMapper.selectByExample(example);
            if (list != null && list.size() == 1) {
                return list.get(0);
            }
        }
        return null;
    }

    /**
     * 根据用户ID取得用户信息
     *
     * @param userId
     * @return
     */
    public Users getUsersByUserId(Integer userId) {
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
     * 根据用户ID取得用户信息
     *
     * @param userId
     * @return
     */
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
    
    /***
	 * 获取用户在银行的开户信息
	 */
	@Override
	public BankOpenAccount getBankOpenAccount(Integer userId) {
		BankOpenAccountExample accountExample = new BankOpenAccountExample();
		BankOpenAccountExample.Criteria crt = accountExample.createCriteria();
		crt.andUserIdEqualTo(userId);
		List<BankOpenAccount> bankAccounts = this.bankOpenAccountMapper.selectByExample(accountExample);
		if (bankAccounts != null && bankAccounts.size() == 1) {
			return bankAccounts.get(0);
		}
		return null;
	}
	
	@Override
	public BorrowWithBLOBs getBorrowByNid(String borrowNid) {
		BorrowExample example = new BorrowExample();
		BorrowExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		List<BorrowWithBLOBs> list = borrowMapper.selectByExampleWithBLOBs(example);
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}
	
	@Override
	public String getBankRetMsg(String retCode) {
		//如果错误码不为空
		if (StringUtils.isNotBlank(retCode)) {
			BankReturnCodeConfigExample example = new BankReturnCodeConfigExample();
			example.createCriteria().andRetCodeEqualTo(retCode);
			List<BankReturnCodeConfig> retCodes = this.bankReturnCodeConfigMapper.selectByExample(example);
			if (retCodes != null && retCodes.size() == 1) {
				String retMsg = retCodes.get(0).getErrorMsg();
				if (StringUtils.isNotBlank(retMsg)) {
					return retMsg;
				} else {
					return "请联系客服！";
				}
			} else {
				return "请联系客服！";
			}
		} else {
			return "操作失败！";
		}
	}
	
	/**
	 * 获取银行电子账户余额
	 * @param userId
	 * @param accountId
	 * @return
	 */
	@Override
	public BigDecimal getBankBalance(Integer userId, String accountId) {
		// 账户可用余额
		BigDecimal balance = BigDecimal.ZERO;
		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 版本号
		bean.setTxCode(BankCallMethodConstant.TXCODE_BALANCE_QUERY);// 交易代码
		bean.setTxDate(GetOrderIdUtils.getTxDate()); // 交易日期
		bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
		bean.setChannel(BankCallConstant.CHANNEL_PC); // 交易渠道
		bean.setAccountId(accountId);// 电子账号
		bean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));// 订单号
		bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
		bean.setLogUserId(String.valueOf(userId));
		bean.setLogRemark("电子账户余额查询");
		bean.setLogClient(0);// 平台
		try {
			BankCallBean resultBean = BankCallUtils.callApiBg(bean);
			if (resultBean != null && BankCallStatusConstant.RESPCODE_SUCCESS.equals(resultBean.getRetCode())) {
				balance = new BigDecimal(resultBean.getAvailBal().replace(",", ""));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return balance;
	}
	
	/**
	 * 根据用户ID取得用户的推荐人信息
	 * 
	 * @param userId
	 * @return
	 */
	@Override
	public SpreadsUsers getSpreadsUsersByUserId(Integer userId) {
		if (userId != null) {
			SpreadsUsersExample example = new SpreadsUsersExample();
			example.createCriteria().andUserIdEqualTo(userId);
			List<SpreadsUsers> list = this.spreadsUsersMapper.selectByExample(example);
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		}
		return null;
	}
	
	/**
	 * 根据活动id获取活动记录
	 */
	@Override
	public ActivityList getActivityListById(Integer activityId) {
		ActivityList activity = this.activityListMapper.selectByPrimaryKey(activityId);
		return activity;
	}
	
    /**
     * 根据借款编号查询资产信息 
     * 
     * @param borrowNid
     * @return
     * @author PC-LIUSHOUYI
     */
    @Override
    public HjhPlanAsset getHjhPlanAsset(String borrowNid) {
		HjhPlanAssetExample example = new HjhPlanAssetExample();
		HjhPlanAssetExample.Criteria crt = example.createCriteria();
		crt.andBorrowNidEqualTo(borrowNid);
        
        List<HjhPlanAsset> list = this.hjhPlanAssetMapper.selectByExample(example);
        
        if(list != null && list.size() > 0){
        	return list.get(0);
        } else {
        	return null;
        }
    }


	/**
	 * 压缩zip文件包
	 *
	 * @param sb
	 * @param zipName
	 * @return
	 */
	@Override
	public boolean writeZip(StringBuffer sb, String zipName) {
		try {
			String[] files = sb.toString().split(",");
			OutputStream os = new BufferedOutputStream(new FileOutputStream(zipName + ".zip"));
			ZipOutputStream zos = new ZipOutputStream(os);
			byte[] buf = new byte[8192];
			int len;
			for (int i = 0; i < files.length; i++) {
				File file = new File(files[i]);
				if (!file.isFile()) {
					continue;
				}
				ZipEntry ze = new ZipEntry(file.getName());
				zos.putNextEntry(ze);
				BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
				while ((len = bis.read(buf)) > 0) {
					zos.write(buf, 0, len);
				}
				zos.closeEntry();
			}
			zos.closeEntry();
			zos.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
