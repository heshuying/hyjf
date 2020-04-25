package com.hyjf.admin.finance.associatedrecords;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.DirectionalTransferAssociatedRecords;
import com.hyjf.mybatis.model.auto.DirectionalTransferAssociatedRecordsExample;

@Service
public class AssociatedrecordsServiceImpl extends BaseServiceImpl implements AssociatedrecordsService {

    @Override
    public int countRecordTotal(AssociatedrecordsBean form) {
        DirectionalTransferAssociatedRecordsExample example = new DirectionalTransferAssociatedRecordsExample();
        DirectionalTransferAssociatedRecordsExample.Criteria cra = example.createCriteria();
        // 转出账户
        if (!StringUtils.isEmpty(form.getTurnOutUsername())) {
            cra.andTurnOutUsernameLike("%" + form.getTurnOutUsername() + "%");
        }
        // 转出账户手机
        if (!StringUtils.isEmpty(form.getTurnOutMobile())) {
            cra.andTurnOutMobileLike("%" + form.getTurnOutMobile() + "%");
        }
        // 关联状态
        if (!StringUtils.isEmpty(form.getStatusSearch())) {
            cra.andAssociatedStateEqualTo(Integer.parseInt(form.getStatusSearch()));
        }
        // 转入账户
        if (!StringUtils.isEmpty(form.getShiftToUsername())) {
            cra.andShiftToUsernameLike("%" + form.getShiftToUsername() + "%");
        }
        // 转入账户手机
        if (!StringUtils.isEmpty(form.getShiftToMobile())) {
            cra.andShiftToMobileLike("%" + form.getShiftToMobile() + "%");
        }
        // 关联时间开始
        if (!StringUtils.isEmpty(form.getStartDate())) {
            cra.andAssociatedTimeGreaterThanOrEqualTo(GetDate.stringToDate(form.getStartDate() + " 00:00:00"));
        }
        // 关联时间结束
        if (!StringUtils.isEmpty(form.getEndDate())) {
            cra.andAssociatedTimeLessThanOrEqualTo(GetDate.stringToDate(form.getEndDate() + " 23:59:59"));
        }

        return directionalTransferAssociatedRecordsMapper.countByExample(example);
    }

    @Override
    public List<DirectionalTransferAssociatedRecords> getRecordList(AssociatedrecordsBean form, int limitStart,
        int limitEnd) {
        DirectionalTransferAssociatedRecordsExample example = new DirectionalTransferAssociatedRecordsExample();
        DirectionalTransferAssociatedRecordsExample.Criteria cra = example.createCriteria();
        // 转出账户
        if (!StringUtils.isEmpty(form.getTurnOutUsername())) {
            cra.andTurnOutUsernameLike("%" + form.getTurnOutUsername() + "%");
        }
        // 转出账户手机
        if (!StringUtils.isEmpty(form.getTurnOutMobile())) {
            cra.andTurnOutMobileLike("%" + form.getTurnOutMobile() + "%");
        }
        // 关联状态
        if (!StringUtils.isEmpty(form.getStatusSearch())) {
            cra.andAssociatedStateEqualTo(Integer.parseInt(form.getStatusSearch()));
        }
        // 转入账户
        if (!StringUtils.isEmpty(form.getShiftToUsername())) {
            cra.andShiftToUsernameLike("%" + form.getShiftToUsername() + "%");
        }
        // 转入账户手机
        if (!StringUtils.isEmpty(form.getShiftToMobile())) {
            cra.andShiftToMobileLike("%" + form.getShiftToMobile() + "%");
        }
        // 关联时间开始
        if (!StringUtils.isEmpty(form.getStartDate())) {
            cra.andAssociatedTimeGreaterThanOrEqualTo(GetDate.stringToDate(form.getStartDate() + " 00:00:00"));
        }
        // 关联时间结束
        if (!StringUtils.isEmpty(form.getEndDate())) {
            cra.andAssociatedTimeLessThanOrEqualTo(GetDate.stringToDate(form.getEndDate() + " 23:59:59"));
        }
        if (limitStart != -1) {
            example.setLimitStart(limitStart);
            example.setLimitEnd(limitEnd);
        }
        // 关联记录
        example.setOrderByClause("`associated_time` desc");
        return directionalTransferAssociatedRecordsMapper.selectByExample(example);
    }

}
