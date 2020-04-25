/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.message.count;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.SmsCountCustomize;
import com.hyjf.mybatis.model.customize.SmsLogCustomize;
import com.hyjf.mybatis.model.customize.UserVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @author yinhui
 */
@Service
public class SmsCountServiceImpl extends BaseServiceImpl implements SmsCountService {

    @Override
    public Integer querySmsCountTotal(SmsCountCustomize smsCountCustomize) {

        return smsCountCustomizeMapper.querySmsCountTotal(smsCountCustomize);
    }

    @Override
    public Integer querySmsCountNumberTotal(SmsCountCustomize smsCountCustomize) {

        return smsCountCustomizeMapper.querySmsCountNumberTotal(smsCountCustomize);
    }

    @Override
    public List<SmsCountCustomize> querySms(SmsCountCustomize smsCountCustomize) {
        return smsCountCustomizeMapper.querySms(smsCountCustomize);
    }

    /**
     * 查询短信统计，通过分公司显示
     * @param smsCountCustomize
     * @return
     */
    public List<SmsCountCustomize> querySmsCountList(SmsCountCustomize smsCountCustomize){
        return smsCountCustomizeMapper.querySmsCountList(smsCountCustomize);
    }

    /**
     * 通过手机号码查询对应的分公司和分公司ID
     *
     * @param mobile
     * @return
     */
    @Override
    public Map<String, Object> getDeptByMobile(String mobile) {
        return smsCountCustomizeMapper.getDeptByMobile(mobile);
    }

    /**
     * 通过手机号码查询用户信息
     *
     * @param mobile
     * @return
     */
    @Override
    public List<UserVO> getUsersVo(List<String> mobile) {
        if (CollectionUtils.isEmpty(mobile)) {
            return null;
        }
        return usersCustomizeMapper.selectUserListByMobile(mobile);
    }


    //获得内部员工的ID和部门
    @Override
    public List<SmsCountCustomize> getuserIdAnddepartmentName() {
        List<SmsCountCustomize> list = smsCountCustomizeMapper.getuserIdAnddepartmentName();
        return list;
    }

    @Transactional
    @Override
    public void insertBatch(SmsLogCustomize smsLogCustomize) {
//        SmsCountCustomize smsCountCustomize = new SmsCountCustomize();
//        smsCountCustomize.setPost_time_begin(smsLogCustomize.getInitStar());
//        smsCountCustomize.setPost_time_end(smsLogCustomize.getInitEnd());
//        List<SmsLogCustomize> list = smslogCustomizeService.queryInitSmsCount(smsLogCustomize);
//        int count = smsCountCustomizeMapper.querySmsCountTotal(smsCountCustomize);
//        if (count < 1 && list.size() > 0) {
//            smsCountCustomizeMapper.insertBatch(list);
//        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertBatchSms(List<SmsLogCustomize> smsLogCustomize) {
        if (CollectionUtils.isEmpty(smsLogCustomize)) {
            return;
        }
        smsCountCustomizeMapper.insertBatch(smsLogCustomize);

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateByPrimaryKeySelective(List<SmsCountCustomize> updateSmsCount) {
        if (CollectionUtils.isEmpty(updateSmsCount)) {
            return;
        }
        for (SmsCountCustomize sms : updateSmsCount) {
            smsCountCustomizeMapper.updateByPrimaryKeySelective(sms);
        }
    }
}
