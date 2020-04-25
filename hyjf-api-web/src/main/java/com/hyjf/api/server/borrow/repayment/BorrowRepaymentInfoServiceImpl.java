package com.hyjf.api.server.borrow.repayment;


import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.api.web.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.apiweb.ApiBorrowRepaymentInfoCustomize;
import com.hyjf.mybatis.model.customize.apiweb.ApiBorrowRepaymentInfoCustomizeRe;

@Service
public class BorrowRepaymentInfoServiceImpl extends BaseServiceImpl implements BorrowRepaymentInfoService {

	/**
	 * 出借明细列表
	 * 
	 * @param borrowCommonCustomize
	 * @return
	 * @author Administrator
	 */

    @Override
    public List<ApiBorrowRepaymentInfoCustomizeRe> selectBorrowRepaymentInfoList(ApiBorrowRepaymentInfoCustomize bean) {

       
        return borrowRepaymentInfoCustomizeMapper.apiSearchBorrowRepaymentInfoList(bean);
    }


}
