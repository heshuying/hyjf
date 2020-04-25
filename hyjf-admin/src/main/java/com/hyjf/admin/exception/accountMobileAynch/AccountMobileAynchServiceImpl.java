package com.hyjf.admin.exception.accountMobileAynch;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.mapper.auto.AccountMobileAynchMapper;
import com.hyjf.mybatis.model.auto.AccountMobileAynch;
import com.hyjf.mybatis.model.auto.AccountMobileAynchExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 李晟
 * @version AccountMobileAynchServiceImpl, v0.1 2018/5/9 15:18
 */
@Service
public class AccountMobileAynchServiceImpl extends BaseServiceImpl implements AccountMobileAynchService  {

    @Autowired
    AccountMobileAynchMapper accountMobileAynchMapper;

    /**
     *查询表中的数据数目
     * @param form
     * @return
     */
    @Override
    public int countMobileAccountAynch(AccountMobileAynchBean form,int flag) {
        String account = form.getAccount();
        String newAccount = form.getNewAccount();
        String username = form.getUsername();
        String mobile = form.getMobile();
        String newMobile = form.getNewMobile();
        Integer status = form.getStatus();
        String accountId = form.getAccountId();
        AccountMobileAynchExample accountMobileAynchExample = new AccountMobileAynchExample();
        AccountMobileAynchExample.Criteria criteria = accountMobileAynchExample.createCriteria();
        criteria.andFlagEqualTo(flag);
        //电子账号
        if(StringUtils.isNotBlank(accountId)){
            criteria.andAccountidEqualTo(accountId);
        }
        // 银行卡号
        if (StringUtils.isNotEmpty(account)) {
            criteria.andAccountEqualTo(account);
        }
        //新银行卡号
        if (StringUtils.isNotEmpty(newAccount)) {
            criteria.andNewAccountEqualTo(newAccount);
        }
        // 用户名
        if (StringUtils.isNotEmpty(username)) {
            criteria.andUsernameEqualTo(username);
        }
        // 原手机号
        if (StringUtils.isNotEmpty(mobile)) {
            criteria.andMobileEqualTo(mobile);
        }
        //新手机号
        if (StringUtils.isNotEmpty(newMobile)) {
            criteria.andNewMobileEqualTo(newMobile);
        }
        //同步状态
        if (!StringUtils.equals("null",status+"")) {
            criteria.andStatusEqualTo(status);
        }
        accountMobileAynchExample.setOrderByClause("create_time desc");
        return accountMobileAynchMapper.countByExample(accountMobileAynchExample);
    }

    /**
     * 查询表中的数据
     * @param form
     * @param offset
     * @param limit
     * @return
     */
    @Override
    public List<AccountMobileAynch> searchAccountMobileAynch(AccountMobileAynchBean form, int offset, int limit,int flag) {

        String account = form.getAccount();
        String newAccount = form.getNewAccount();
        String username = form.getUsername();
        String mobile = form.getMobile();
        String newMobile = form.getNewMobile();
        Integer status = form.getStatus();
        String accountId = form.getAccountId();
        AccountMobileAynchExample accountMobileAynchExample = new AccountMobileAynchExample();
        AccountMobileAynchExample.Criteria criteria = accountMobileAynchExample.createCriteria();

        criteria.andFlagEqualTo(flag);

        //电子账号
        if(StringUtils.isNotBlank(accountId)){
            criteria.andAccountidEqualTo(accountId);
        }
        // 银行卡号
        if (StringUtils.isNotEmpty(account)) {
            criteria.andAccountEqualTo(account);
        }
        //新银行卡号
        if (StringUtils.isNotEmpty(newAccount)) {
            criteria.andNewAccountEqualTo(newAccount);
        }
        // 用户名
        if (StringUtils.isNotEmpty(username)) {
            criteria.andUsernameEqualTo(username);
        }
        // 原手机号
        if (StringUtils.isNotEmpty(mobile)) {
            criteria.andMobileEqualTo(mobile);
        }
        //新手机号
        if (StringUtils.isNotEmpty(newMobile)) {
            criteria.andNewMobileEqualTo(newMobile);
        }
        //同步状态
        if (!StringUtils.equals("null",status+"")) {
            criteria.andStatusEqualTo(status);
        }
        if (offset != -1) {
            accountMobileAynchExample.setLimitStart(offset);
            accountMobileAynchExample.setLimitEnd(limit);
        }
        accountMobileAynchExample.setOrderByClause("create_time desc");
        return accountMobileAynchMapper.selectByExample(accountMobileAynchExample);
    }

    /**
     * 添加一条同步信息数据
     * @param username
     * @return
     */
    @Override
    public Integer insertAccountMobileAynch(String username,String flag) {
        AccountMobileAynchExample accountMobileAynchExample = new AccountMobileAynchExample();
        AccountMobileAynchExample.Criteria criteria = accountMobileAynchExample.createCriteria();
        criteria.andUsernameEqualTo(username);
        criteria.andStatusEqualTo(0);
        if("1".equals(flag)){
            criteria.andFlagEqualTo(1);
            List<AccountMobileAynch> accountMobileAynches = accountMobileAynchMapper.selectByExample(accountMobileAynchExample);
            //只要当前用户有一个未同步的数据，就不允许新增
            if(accountMobileAynches!=null&&!accountMobileAynches.isEmpty()){
                return 1;
            }
            int i = accountMobileAynchMapper.insertMobile(username);
            if(i>0){
                return 0;
            }else{
                return 2;
            }
        }else{
            criteria.andFlagEqualTo(2);
            List<AccountMobileAynch> accountMobileAynches = accountMobileAynchMapper.selectByExample(accountMobileAynchExample);
            //只要当前用户有一个未同步的数据，就不允许新增
            if(accountMobileAynches!=null&&!accountMobileAynches.isEmpty()){
                return 1;
            }
            int i = accountMobileAynchMapper.insertAccount(username);
            if(i>0){
                return 0;
            }else{
                return 2;
            }
        }

    }

    public boolean deleteMessage(String id){
        int i = accountMobileAynchMapper.deleteByPrimaryKey(Integer.valueOf(id));
        if(i>0){
            return true;
        }else {
            return false;
        }
    }
}
