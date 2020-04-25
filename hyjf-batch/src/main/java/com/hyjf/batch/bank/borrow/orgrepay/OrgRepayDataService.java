package com.hyjf.batch.bank.borrow.orgrepay;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.Borrow;

import java.text.ParseException;
import java.util.List;

public interface OrgRepayDataService extends BaseService{

    public List<Borrow> getOrgRepayBorrowList();

    public void updateOrgRepayData(Borrow list) throws Exception;


}
