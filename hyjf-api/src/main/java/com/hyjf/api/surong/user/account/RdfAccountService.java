package com.hyjf.api.surong.user.account;

import java.util.List;
import java.util.Map;

import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.auto.BankCard;

public interface RdfAccountService extends BaseService{
      public String getBalance(String mobile);
      
      public BankCard getBankCard(String mobile);
      
      public List<Map<String, String>> balanceSync(List<Integer> ids);
}
