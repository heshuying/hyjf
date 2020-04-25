package com.hyjf.admin.finance.returncash;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.http.HtmlUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountFunds;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AccountWebList;
import com.hyjf.mybatis.model.auto.AccountWebListExample;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.SpreadsUsersExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.EmployeeCustomize;
import com.hyjf.mybatis.model.customize.ReturncashCustomize;
import com.hyjf.mybatis.model.customize.UserInfoCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;

@Service
public class ReturncashServiceImpl extends BaseServiceImpl implements ReturncashService {

    /** 待返金额>=1000 */
    private static final BigDecimal REC_MONEY = new BigDecimal(1000);
    /** 新出借金额>=997 */
    private static final BigDecimal IN_MONEY = new BigDecimal(997);
	@Autowired
    @Qualifier("smsProcesser")
    private MessageProcesser smsProcesser; 

    /**
     * 获取待返现列表数量
     *
     * @param form
     * @return
     */
    @Override
    public int getReturncashRecordCount(ReturncashBean form) {
        ReturncashCustomize returncashCustomize = new ReturncashCustomize();
        BeanUtils.copyProperties(form, returncashCustomize);
        // 待返金额>=1000
        returncashCustomize.setRecMoney(REC_MONEY);
        // 新出借金额>=997
        returncashCustomize.setInMoney(IN_MONEY);
        // 部门
        if (Validator.isNotNull(form.getCombotreeSrch())) {
            if (form.getCombotreeSrch().contains(StringPool.COMMA)) {
                String[] list = form.getCombotreeSrch().split(StringPool.COMMA);
                returncashCustomize.setCombotreeListSrch(list);
            } else {
                returncashCustomize.setCombotreeListSrch(new String[]{form.getCombotreeSrch()});
            }
        }
        return returncashCustomizeMapper.selectReturncashCount(returncashCustomize);
    }

    /**
     * 获取待返现列表
     *
     * @return
     */
    @Override
    public List<ReturncashCustomize> getReturncashRecordList(ReturncashBean form) {
        ReturncashCustomize returncashCustomize = new ReturncashCustomize();
        BeanUtils.copyProperties(form, returncashCustomize);
        // 待返金额>=1000
        returncashCustomize.setRecMoney(REC_MONEY);
        // 新出借金额>=997
        returncashCustomize.setInMoney(IN_MONEY);
        // 部门
        if (Validator.isNotNull(form.getCombotreeSrch())) {
            if (form.getCombotreeSrch().contains(StringPool.COMMA)) {
                String[] list = form.getCombotreeSrch().split(StringPool.COMMA);
                returncashCustomize.setCombotreeListSrch(list);
            } else {
                returncashCustomize.setCombotreeListSrch(new String[]{form.getCombotreeSrch()});
            }
        }

        return returncashCustomizeMapper.selectReturncashList(returncashCustomize);
    }

    /**
     * 获取已返现列表数量
     *
     * @param form
     * @return
     */
    @Override
    public int getReturnedcashRecordCount(ReturncashBean form) {
        ReturncashCustomize returncashCustomize = new ReturncashCustomize();
        BeanUtils.copyProperties(form, returncashCustomize);
        // 部门
        if (Validator.isNotNull(form.getCombotreeSrch())) {
            if (form.getCombotreeSrch().contains(StringPool.COMMA)) {
                String[] list = form.getCombotreeSrch().split(StringPool.COMMA);
                returncashCustomize.setCombotreeListSrch(list);
            } else {
                returncashCustomize.setCombotreeListSrch(new String[]{form.getCombotreeSrch()});
            }
        }
        return returncashCustomizeMapper.selectReturnedcashCount(returncashCustomize);
    }

    /**
     * 获取已返现列表
     *
     * @return
     */
    @Override
    public List<ReturncashCustomize> getReturnedcashRecordList(ReturncashBean form) {
        ReturncashCustomize returncashCustomize = new ReturncashCustomize();
        BeanUtils.copyProperties(form, returncashCustomize);
        // 部门
        if (Validator.isNotNull(form.getCombotreeSrch())) {
            if (form.getCombotreeSrch().contains(StringPool.COMMA)) {
                String[] list = form.getCombotreeSrch().split(StringPool.COMMA);
                returncashCustomize.setCombotreeListSrch(list);
            } else {
                returncashCustomize.setCombotreeListSrch(new String[]{form.getCombotreeSrch()});
            }
        }
        return returncashCustomizeMapper.selectReturnedcashList(returncashCustomize);
    }

    /**
     * 返现处理
     *
     * @param form
     * @return
     */
    public int insertReturncashRecord(ReturncashCustomize returncashCustomize, ChinapnrBean bean) {
        int ret = 0;

        // 功能描述
        String note = "充值手续费返还";
        // 增加时间
        Integer time = GetDate.getMyTimeInMillis();
        // 用户ID
        Integer userId = returncashCustomize.getUserId();
        // 操作者用户名
        String operator = ShiroUtil.getLoginUsername();

        // 更新账户信息
        AccountExample accountExample = new AccountExample();
        AccountExample.Criteria accountCriteria = accountExample.createCriteria();
        accountCriteria.andUserIdEqualTo(userId);
        Account account = accountMapper.selectByExample(accountExample).get(0);
        BigDecimal money = new BigDecimal(bean.getTransAmt());

        // 如果出借金额大于充值金额
        if (returncashCustomize.getInMoney().compareTo(returncashCustomize.getRecMoney()) >= 0) {
            account.setRecMoney(BigDecimal.ZERO);
            account.setInMoney(returncashCustomize.getInMoney().subtract(returncashCustomize.getRecMoney()));
            account.setFee(BigDecimal.ZERO);
        } else {
            // 如果出借金额小于充值金额
            account.setRecMoney(returncashCustomize.getRecMoney().subtract(returncashCustomize.getInMoney()));
            account.setInMoney(BigDecimal.ZERO);
            account.setFee(returncashCustomize.getFee().subtract(money));
        }
        account.setTotal(account.getTotal().add(money)); // 用户总资产=总资产+返现金额
        account.setBalance(account.getBalance().add(money)); // 用户资金总额=资金总额+返现金额
        account.setIncome(account.getIncome().add(money)); // 用户收入=收入+返现金额
        ret += this.accountMapper.updateByExampleSelective(account, accountExample);

        // 根据用户ID查询部门信息
        List<UserInfoCustomize> userInfoCustomizes = this.userInfoCustomizeMapper.queryDepartmentInfoByUserId(userId);
        UserInfoCustomize userInfoCustomize;
        if(userInfoCustomizes!=null && userInfoCustomizes.size()>0){
        	userInfoCustomize= userInfoCustomizes.get(0);
        }else{
        	userInfoCustomize= null;
//        	System.out.println("返现 -根据用户ID查询部门信息失败!");
        }
        
        // 写入发放记录表
        AccountFunds accountFunds = new AccountFunds();
        accountFunds.setUserId(userId);
        accountFunds.setMoney(money);
        accountFunds.setNote(note);
        accountFunds.setAddtime(time);
        accountFunds.setStatus(1);
        accountFunds.setOperator(operator);
        if (userInfoCustomize != null) {
            accountFunds.setRegionId(userInfoCustomize.getRegionId());
            accountFunds.setRegionName(userInfoCustomize.getRegionName());
            accountFunds.setBranchId(userInfoCustomize.getBranchId());
            accountFunds.setBranchName(userInfoCustomize.getBranchName());
            accountFunds.setDepartmentId(userInfoCustomize.getDepartmentId());
            accountFunds.setDepartmentName(HtmlUtil.unescape(userInfoCustomize.getDepartmentName()));
        }
        ret += this.accountFundsMapper.insertSelective(accountFunds);

        // 写入收支明细
        AccountList accountList = new AccountList();
        accountList.setNid(bean.getOrdId());
        accountList.setUserId(userId);
        accountList.setAmount(money);
        accountList.setType(1);
        accountList.setTrade("recharge_success");
        accountList.setTradeCode("balance");
        accountList.setTotal(account.getTotal());
        accountList.setBalance(account.getBalance());
        accountList.setFrost(account.getFrost());
        accountList.setAwait(account.getAwait());
        accountList.setRepay(account.getRepay());
        accountList.setRemark(note);
        accountList.setCreateTime(time);
        accountList.setOperator(operator);
        accountList.setIp(returncashCustomize.getIp());
        accountList.setIsUpdate(0);
        accountList.setBaseUpdate(0);
        accountList.setInterest(null);
        accountList.setWeb(2);
        ret += this.accountListMapper.insertSelective(accountList);

        // 插入网站收支明细记录
        AccountWebList accountWebList = new AccountWebList();
        accountWebList.setOrdid(accountList.getNid());// 订单号
        accountWebList.setUserId(accountList.getUserId()); // 出借者
        accountWebList.setAmount(accountList.getAmount()); // 管理费
        accountWebList.setType(CustomConstants.TYPE_OUT); // 类型1收入 2支出
        accountWebList.setTrade(CustomConstants.TRADE_RECHAFEE); // 充值返现
        accountWebList.setTradeType(CustomConstants.TRADE_RECHAFEE_NM); // 充值返现
        accountWebList.setRemark(CustomConstants.TRADE_RECHAFEE_REMARK); // 充值手续费返还
        accountWebList.setCreateTime(time);
        ret += insertAccountWebList(accountWebList);

        // 发送短信
        Map<String, String> params = new HashMap<String, String>();
        params.put("val_amount", money.toString());
		   SmsMessage smsMessage =
                   new SmsMessage(userId, params, null, null, MessageDefine.SMSSENDFORUSER, null,
                   		CustomConstants.PARAM_TPL_SDSXFFX, CustomConstants.CHANNEL_TYPE_NORMAL);
          smsProcesser.gather(smsMessage);

        return ret;
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
     * 插入网站收支记录
     *
     * @param nid
     * @return
     */
    private int insertAccountWebList(AccountWebList accountWebList) {
        if (countAccountWebList(accountWebList.getOrdid(), accountWebList.getTrade()) == 0) {
            // 设置部门名称
            setDepartments(accountWebList);
            // 插入
            return this.accountWebListMapper.insertSelective(accountWebList);
        }
        return 0;
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
}
