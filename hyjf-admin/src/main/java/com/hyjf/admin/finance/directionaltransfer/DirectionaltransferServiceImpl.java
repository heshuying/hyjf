package com.hyjf.admin.finance.directionaltransfer;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.AccountDirectionalTransfer;
import com.hyjf.mybatis.model.auto.AccountDirectionalTransferExample;

@Service
public class DirectionaltransferServiceImpl extends BaseServiceImpl implements DirectionaltransferService {

    @Override
    public int countRecordTotal(DirectionaltransferBean form) {
        AccountDirectionalTransferExample example = new AccountDirectionalTransferExample();
        AccountDirectionalTransferExample.Criteria cra = example.createCriteria();
        // 转出账户
        if (!StringUtils.isEmpty(form.getTurnOutUsername())) {
            cra.andTurnOutUsernameLike("%" + form.getTurnOutUsername() + "%");
        }
        // 转入账户
        if (!StringUtils.isEmpty(form.getShiftToUsername())) {
            cra.andShiftToUsernameLike("%" + form.getShiftToUsername() + "%");
        }
        // 转账状态
        if (!StringUtils.isEmpty(form.getStatusSearch())) {
            cra.andTransferAccountsStateEqualTo(Integer.parseInt(form.getStatusSearch()));
        }
        // 转账订单号
        if (!StringUtils.isEmpty(form.getOrderId())) {
            cra.andOrderIdLike("%" + form.getOrderId() + "%");
        }
        // 检索条件转账时间开始
        if (!StringUtils.isEmpty(form.getStartDate())) {
            cra.andTransferAccountsTimeGreaterThanOrEqualTo(GetDate.stringToDate(form.getStartDate() + " 00:00:00"));
        }
        // 检索条件转账时间结束
        if (!StringUtils.isEmpty(form.getEndDate())) {
            cra.andTransferAccountsTimeLessThanOrEqualTo(GetDate.stringToDate(form.getEndDate() + " 23:59:59"));
        }
        return accountDirectionalTransferMapper.countByExample(example);
    }

    @Override
    public List<AccountDirectionalTransfer> getRecordList(DirectionaltransferBean form, int limitStart, int limitEnd) {
        AccountDirectionalTransferExample example = new AccountDirectionalTransferExample();
        AccountDirectionalTransferExample.Criteria cra = example.createCriteria();
        // 转出账户
        if (!StringUtils.isEmpty(form.getTurnOutUsername())) {
            cra.andTurnOutUsernameLike("%" + form.getTurnOutUsername() + "%");
        }
        // 转入账户
        if (!StringUtils.isEmpty(form.getShiftToUsername())) {
            cra.andShiftToUsernameLike("%" + form.getShiftToUsername() + "%");
        }
        // 转账状态
        if (!StringUtils.isEmpty(form.getStatusSearch())) {
            cra.andTransferAccountsStateEqualTo(Integer.parseInt(form.getStatusSearch()));
        }
        // 转账订单号
        if (!StringUtils.isEmpty(form.getOrderId())) {
            cra.andOrderIdLike("%" + form.getOrderId() + "%");
        }
        // 检索条件转账时间开始
        if (!StringUtils.isEmpty(form.getStartDate())) {
            cra.andTransferAccountsTimeGreaterThanOrEqualTo(GetDate.stringToDate(form.getStartDate() + " 00:00:00"));
        }
        // 检索条件转账时间结束
        if (!StringUtils.isEmpty(form.getEndDate())) {
            cra.andTransferAccountsTimeLessThanOrEqualTo(GetDate.stringToDate(form.getEndDate() + " 23:59:59"));
        }
        if (limitStart != -1) {
            example.setLimitStart(limitStart);
            example.setLimitEnd(limitEnd);
        }
        // 转账时间
        example.setOrderByClause("`transfer_accounts_time` desc");
        return accountDirectionalTransferMapper.selectByExample(example);
    }

}
