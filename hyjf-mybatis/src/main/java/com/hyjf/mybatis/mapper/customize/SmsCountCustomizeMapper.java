/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mybatis.mapper.customize;

import com.hyjf.mybatis.model.auto.SmsLog;
import com.hyjf.mybatis.model.customize.SmsCountCustomize;
import com.hyjf.mybatis.model.customize.SmsLogCustomize;

import java.util.List;
import java.util.Map;

/**
 * @author DELL
 * @version SmsCountCustomizeMapper, v0.1 2018/5/15 16:35
 */
public interface SmsCountCustomizeMapper {

    public int querySmsCountTotal(SmsCountCustomize smsCountCustomize);

    //查询短信发送总条数
    public Integer querySmsCountNumberTotal(SmsCountCustomize smsCountCustomize);

    public List<SmsCountCustomize> querySms(SmsCountCustomize smsCountCustomize);

    /**
     * 查询短信统计，通过分公司显示
     * @param smsCountCustomize
     * @return
     */
    public List<SmsCountCustomize> querySmsCountList(SmsCountCustomize smsCountCustomize);

    /**
     * 批量插入
     * @param list
     * @return
     */
    public void insertBatch(List<SmsLogCustomize> list);

    /**
     * 通过手机号获取部门
     * @param mobile
     * @return
     */
    public Map<String,Object> getDeptByMobile(String mobile);

    public void insertSelective(SmsCountCustomize smsCountCustomize);

    public void updateByPrimaryKeySelective(SmsCountCustomize smsCountCustomize);

    //获得内部员工的ID和部门
    public List<SmsCountCustomize> getuserIdAnddepartmentName();
}
