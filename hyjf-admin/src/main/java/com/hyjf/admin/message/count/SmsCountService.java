/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.message.count;

import com.hyjf.mybatis.model.customize.SmsCountCustomize;
import com.hyjf.mybatis.model.customize.SmsLogCustomize;
import com.hyjf.mybatis.model.customize.UserVO;

import java.util.List;
import java.util.Map;

/**
 * @author DELL
 * @version SmsCountService, v0.1 2018/5/15 16:24
 */
public interface SmsCountService {

    public Integer querySmsCountTotal(SmsCountCustomize smsCountCustomize);

    public Integer querySmsCountNumberTotal(SmsCountCustomize smsCountCustomize);

    public List<SmsCountCustomize> querySms(SmsCountCustomize smsCountCustomize);

    /**
     * 通过手机号码查询对应的分公司和分公司ID
     * @param mobile
     * @return
     */
    public Map<String,Object> getDeptByMobile(String mobile);

    /**
     * 查询短信统计，通过分公司显示
     * @param smsCountCustomize
     * @return
     */
    public List<SmsCountCustomize> querySmsCountList(SmsCountCustomize smsCountCustomize);

    /**
     * 通过手机号码查询用户信息
     * @param mobile
     * @return
     */
    public List<UserVO> getUsersVo(List<String> mobile);

    //获得内部员工的ID和部门
    public List<SmsCountCustomize> getuserIdAnddepartmentName();

    public void insertBatch(SmsLogCustomize smsLogCustomize);

    public void insertBatchSms(List<SmsLogCustomize> smsLogCustomize);

    public void updateByPrimaryKeySelective(List<SmsCountCustomize> updateSmsCount);
}
