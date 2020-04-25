package com.hyjf.api.aems.repayment;


import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.apiweb.ApiBorrowRepaymentInfoCustomize;
import com.hyjf.mybatis.model.customize.apiweb.ApiBorrowRepaymentInfoCustomizeRe;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AemsBorrowRepaymentInfoServiceImpl extends BaseServiceImpl implements AemsBorrowRepaymentInfoService {

	/**
	 * 投资明细列表
	 * 
	 * @param bean
	 * @return
	 * @author Administrator
	 */

    @Override
    public List<ApiBorrowRepaymentInfoCustomizeRe> selectBorrowRepaymentInfoList(ApiBorrowRepaymentInfoCustomize bean) {

       
        return borrowRepaymentInfoCustomizeMapper.apiSearchBorrowRepaymentInfoList(bean);
    }


}
