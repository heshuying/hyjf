package com.hyjf.app.bank.user.deletecard;

import java.util.List;

import com.hyjf.app.BaseDefine;
import com.hyjf.app.bank.user.bindCard.AppBindCardDefine;
import com.hyjf.app.bank.user.bindcardpage.BindCardPageDefine;
import com.hyjf.app.user.manage.AppUserDefine;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.app.BaseServiceImpl;
import com.hyjf.common.bank.LogAcqResBean;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.BankAccountLog;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankCardExample;
import com.hyjf.mybatis.model.auto.BankConfig;
import com.hyjf.mybatis.model.auto.BankConfigExample;
import com.hyjf.mybatis.model.auto.BankInterface;
import com.hyjf.mybatis.model.auto.BankInterfaceExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

/**
 * App解绑银行卡Service实现类
 * 
 * @author liuyang
 *
 */
@Service
public class AppDeleteCardServiceImpl extends BaseServiceImpl implements AppDeleteCardService {

	/**
	 * 用户删除银行卡后调用方法
	 *
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public synchronized boolean updateAfterDeleteCard(BankCallBean bean, Integer userId) throws Exception {
		int nowTime = GetDate.getNowTime10(); // 当前时间
		boolean ret = false;
		LogAcqResBean logAcqResBean = bean.getLogAcqResBean();
		BankCardExample bankCardExample = new BankCardExample();
		BankCardExample.Criteria aCriteria = bankCardExample.createCriteria();
		aCriteria.andUserIdEqualTo(userId);
		aCriteria.andCardNoEqualTo(logAcqResBean.getCardNo()); // 银行卡账号
		aCriteria.andIdEqualTo(logAcqResBean.getCardId());// 银行卡Id
		List<BankCard> accountBank = this.bankCardMapper.selectByExample(bankCardExample);
		boolean isDeleteFlag = this.bankCardMapper.deleteByExample(bankCardExample) > 0 ? true : false;
		if (!isDeleteFlag) {
			throw new Exception("删除银行卡失败,请联系客服人员!");
		}
		// 插入操作记录表
		BankAccountLog bankAccountLog = new BankAccountLog();
		bankAccountLog.setUserId(userId);
		bankAccountLog.setUserName(this.getUsers(userId).getUsername());
		bankAccountLog.setBankCode(String.valueOf(accountBank.get(0).getBankId()));
		bankAccountLog.setBankAccount(logAcqResBean.getCardNo());
		bankAccountLog.setBankName(accountBank.get(0).getBank());
		bankAccountLog.setCardType(0);// 卡类型 0普通提现卡1默认卡2快捷支付卡
		bankAccountLog.setOperationType(1);// 操作类型 0绑定 1删除
		bankAccountLog.setStatus(0);// 成功
		bankAccountLog.setCreateTime(nowTime);// 操作时间
		ret = this.bankAccountLogMapper.insertSelective(bankAccountLog) > 0 ? true : false;
		return ret;
	}

	/**
	 * 获取用户的身份证号
	 *
	 * @param userId
	 * @return 用户的身份证号
	 */
	public String getUserIdcard(Integer userId) {
		// 取得身份证号
		UsersInfoExample usersInfoExample = new UsersInfoExample();
		usersInfoExample.createCriteria().andUserIdEqualTo(userId);
		List<UsersInfo> listUsersInfo = this.usersInfoMapper.selectByExample(usersInfoExample);
		if (listUsersInfo != null && listUsersInfo.size() > 0) {
			return listUsersInfo.get(0).getIdcard();
		}
		return "";
	}

	/**
	 * 获取银行列表
	 * 
	 * @param string
	 * @return
	 * @author Administrator
	 */
	public BankConfig getBankcardConfig(String bankCode) {
		BankConfigExample example = new BankConfigExample();
		BankConfigExample.Criteria cra = example.createCriteria();
		if (StringUtils.isNotEmpty(bankCode)) {
			cra.andCodeEqualTo(bankCode);
		} else {
			return null;
		}
		List<BankConfig> banks = bankConfigMapper.selectByExample(example);
		if (banks != null && banks.size() > 0) {
			return banks.get(0);
		}
		return null;
	}

	@Override
	public BankCard getBankCardByCardNo(Integer userId, String cardNo) {
		BankCardExample example = new BankCardExample();
		BankCardExample.Criteria cra = example.createCriteria();
		cra.andUserIdEqualTo(userId);
		cra.andCardNoEqualTo(cardNo);
		List<BankCard> list = bankCardMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	/**
     * 判断江西银行绑卡使用新/旧接口
     * @param interfaceType
     * @author: pcc
     * @return 0旧接口/1新接口
     */
    @Override
    public Integer getBandCardBindUrlType(String interfaceType) {
        BankInterfaceExample bankInterfaceExample = new BankInterfaceExample();
        BankInterfaceExample.Criteria criteria= bankInterfaceExample.createCriteria();
        criteria.andInterfaceTypeEqualTo(interfaceType);
        criteria.andIsDeleteEqualTo(0);
        criteria.andIsUsableEqualTo(1);
        bankInterfaceExample.setLimitStart(0);
        bankInterfaceExample.setLimitEnd(1);
        bankInterfaceExample.setOrderByClause("`update_time` DESC");
        List<BankInterface> bankInterfaces = bankInterfaceMapper.selectByExample(bankInterfaceExample);
        if(bankInterfaces.size() > 0){
            //返回接口类型
            return bankInterfaces.get(0).getInterfaceStatus();
        }else {
            //默认绑卡旧接口
            return 0;
        }
    }

	/**
	 * 解绑银行卡后(异步回调删除)
	 * 合规四期(解卡页面调用)
	 *
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	@Override
	public synchronized boolean deleteBankCard(BankCallBean bean, Integer userId) throws Exception {
		int nowTime = GetDate.getNowTime10(); // 当前时间
		boolean ret = false;
		BankCardExample bankCardExample = new BankCardExample();
		BankCardExample.Criteria aCriteria = bankCardExample.createCriteria();
		aCriteria.andUserIdEqualTo(userId);
		List<BankCard> accountBank = this.bankCardMapper.selectByExample(bankCardExample);
		// 插入操作记录表
		BankAccountLog bankAccountLog = new BankAccountLog();
		bankAccountLog.setUserId(userId);
		bankAccountLog.setUserName(this.getUsers(userId).getUsername());
		bankAccountLog.setBankCode(String.valueOf(accountBank.get(0).getBankId()));
		bankAccountLog.setBankAccount(accountBank.get(0).getCardNo());
		bankAccountLog.setBankName(accountBank.get(0).getBank());
		//删除银行卡
		boolean isDeleteFlag = this.bankCardMapper.deleteByExample(bankCardExample) > 0 ? true : false;
		if (!isDeleteFlag) {
			throw new Exception("删除银行卡失败,请联系客服人员!");
		}

		bankAccountLog.setCardType(0);// 卡类型 0普通提现卡1默认卡2快捷支付卡
		bankAccountLog.setOperationType(1);// 操作类型 0绑定 1删除
		bankAccountLog.setStatus(0);// 成功
		bankAccountLog.setCreateTime(nowTime);// 操作时间
		ret = this.bankAccountLogMapper.insertSelective(bankAccountLog) > 0 ? true : false;
		return ret;
	}

	//获取绑卡页面 add by nxl

	/**
	 * 判断是否走新街口还是老接口
	 * 银行接口使用状态(0:老接口，1:新接口)
	 * @param userId
	 * @return
	 */
	@Override
	public String getResultUrl(String userId) {
		String returnUrl = "";
		String webhost = UploadFileUtils.getDoPath(PropUtils.getSystem("hyjf.web.host")) + BaseDefine.REQUEST_HOME.substring(1, AppUserDefine.REQUEST_HOME.length()) + "/";
		webhost = webhost.substring(0, webhost.length() - 1);// http://new.hyjf.com/hyjf-app/
		if (StringUtils.isNotBlank(userId)) {
			AccountChinapnrExample example = new AccountChinapnrExample();
			example.createCriteria().andUserIdEqualTo(Integer.parseInt(userId));
			List<AccountChinapnr> list = accountChinapnrMapper.selectByExample(example);
			// 江西银行绑卡接口修改 update by wj 2018-5-17 start
			Integer urlType = this.getBandCardBindUrlType("BIND_CARD");
			// 江西银行绑卡接口修改 update by wj 2018-5-17 end
			if (list != null && list.size() > 0) {
				// 绑卡url
//			result.setHuifuBindBankCardUrl(webhost + UserBindCardDefine.REQUEST_MAPPING + UserBindCardDefine.REQUEST_MAPPING);
				if (urlType == 1) {
					//绑卡接口类型为新接口
					return "1";
				} else {
					//绑卡接口类型为旧接口
					return "0";
				}
			} else {
				if (urlType == 1) {
					//绑卡接口类型为新接口
					return "1";
				} else {
					//绑卡接口类型为旧接口
					return "0";
				}
			}
		}
		return returnUrl;
	}

}
