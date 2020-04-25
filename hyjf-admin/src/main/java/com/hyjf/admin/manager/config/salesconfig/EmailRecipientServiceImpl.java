package com.hyjf.admin.manager.config.salesconfig;

import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.mapper.customize.SellDailyDistributionCustomizeMapper;
import com.hyjf.mybatis.model.customize.SellDailyDistributionCustomize;
import com.hyjf.mybatis.model.customize.SellDailyDistributionCustomizeExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author lisheng
 * @version EmailRecipientServiceImpl, v0.1 2018/7/23 16:43
 */
@Service
public class EmailRecipientServiceImpl implements EmailRecipientService {
    @Autowired
    SellDailyDistributionCustomizeMapper sellDailyDistributionMapper;
    public static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 查询条数
     *
     * @param form
     * @return
     */
    @Override
    public int countRecordTotal(EmailRecipientBean form) {
        String businessName = form.getBusinessName();// 业务名称
        Integer status = form.getStatus();//状态
        String timeStartCreateSrch = form.getTimeStartCreateSrch();//创建开始时间
        String timeEndCreateSrch = form.getTimeEndCreateSrch();//创建结束时间
        String timeStartUpdateSrch = form.getTimeStartUpdateSrch();//更新开始时间
        String timeEndUpdateSrch = form.getTimeEndUpdateSrch();//更新结束时间

        SellDailyDistributionCustomizeExample example = new SellDailyDistributionCustomizeExample();
        SellDailyDistributionCustomizeExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(businessName)) {
            criteria.andBusinessNameEqualTo(businessName);
        }
        if (status != null) {
            criteria.andStatusEqualTo(status);
        }

        if (StringUtils.isNotBlank(timeStartCreateSrch) && StringUtils.isNotBlank(timeEndCreateSrch)) {
            criteria.andCreateTimeBetween(GetDate.str2Date(GetDate.getDayStart(timeStartCreateSrch),datetimeFormat), GetDate.str2Date(GetDate.getDayEnd(timeStartCreateSrch),datetimeFormat));
        }
        if (StringUtils.isNotBlank(timeStartUpdateSrch) && StringUtils.isNotBlank(timeEndUpdateSrch)) {
            criteria.andUpdateTimeBetween(GetDate.str2Date(GetDate.getDayStart(timeStartUpdateSrch),datetimeFormat), GetDate.str2Date(GetDate.getDayEnd(timeEndUpdateSrch),datetimeFormat));
        }

        return sellDailyDistributionMapper.countByExample(example);
    }

    /**
     * 查询符合条件记录
     *
     * @param form
     * @param offset
     * @param limit
     * @return
     */
    @Override
    public List<SellDailyDistributionCustomize> getRecordList(EmailRecipientBean form, Integer offset, Integer limit) {
        String businessName = form.getBusinessName();// 业务名称
        Integer status = form.getStatus();//状态
        String timeStartCreateSrch = form.getTimeStartCreateSrch();//创建开始时间
        String timeEndCreateSrch = form.getTimeEndCreateSrch();//创建结束时间
        String timeStartUpdateSrch = form.getTimeStartUpdateSrch();//更新开始时间
        String timeEndUpdateSrch = form.getTimeEndUpdateSrch();//更新结束时间

        SellDailyDistributionCustomizeExample example = new SellDailyDistributionCustomizeExample();
        SellDailyDistributionCustomizeExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(businessName)) {
            criteria.andBusinessNameEqualTo(businessName);
        }
        if (status != null) {
            criteria.andStatusEqualTo(status);
        }
        if (StringUtils.isNotBlank(timeStartCreateSrch) && StringUtils.isNotBlank(timeEndCreateSrch)) {
            criteria.andCreateTimeBetween(GetDate.str2Date(GetDate.getDayStart(timeStartCreateSrch),datetimeFormat), GetDate.str2Date(GetDate.getDayEnd(timeStartCreateSrch),datetimeFormat));
        }
        if (StringUtils.isNotBlank(timeStartUpdateSrch) && StringUtils.isNotBlank(timeEndUpdateSrch)) {
            criteria.andUpdateTimeBetween(GetDate.str2Date(GetDate.getDayStart(timeStartUpdateSrch),datetimeFormat), GetDate.str2Date(GetDate.getDayEnd(timeEndUpdateSrch),datetimeFormat));
        }
        if (offset != null && limit != null) {
            example.setLimitStart(offset);
            example.setLimitEnd(limit);
        }
        example.setOrderByClause("update_time desc");
        return sellDailyDistributionMapper.selectByExample(example);
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Override
    public SellDailyDistributionCustomize getRecordById(Integer id) {
        return sellDailyDistributionMapper.selectByPrimaryKey(id);
    }

    /**
     * 修改记录
     *
     * @param form
     * @return
     */
    @Override
    public boolean updateRecord(EmailRecipientBean form) {
        SellDailyDistributionCustomizeExample example = new SellDailyDistributionCustomizeExample();
        SellDailyDistributionCustomizeExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(form.getId());
        SellDailyDistributionCustomize SellDailyDistribution = new SellDailyDistributionCustomize();
        if (StringUtils.isNotBlank(form.getBusinessName())) {
            SellDailyDistribution.setBusinessName(form.getBusinessName());
        }
        if (StringUtils.isNotBlank(form.getTimePoint() + "")) {
            SellDailyDistribution.setTimePoint(form.getTimePoint());
        }
        if (StringUtils.isNotBlank(form.getTime() + "")) {
            SellDailyDistribution.setTime(form.getTime());
        }
        if (StringUtils.isNotBlank(form.getEmail() + "")) {
            SellDailyDistribution.setEmail(form.getEmail());
        }

        SellDailyDistribution.setUpdateName(form.getUpdateName());
        return sellDailyDistributionMapper.updateByExampleSelective(SellDailyDistribution, example) > 0 ? true : false;
    }

    /**
     * 禁用
     * @param form
     * @return
     */
    @Override
    public boolean updateForbidden(EmailRecipientBean form) {
        SellDailyDistributionCustomizeExample example = new SellDailyDistributionCustomizeExample();
        SellDailyDistributionCustomizeExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(form.getId());
        SellDailyDistributionCustomize SellDailyDistribution = new SellDailyDistributionCustomize();
        SellDailyDistribution.setStatus(form.getStatus());

        SellDailyDistribution.setUpdateName(form.getUpdateName());
        return sellDailyDistributionMapper.updateByExampleSelective(SellDailyDistribution, example) > 0 ? true : false;
    }

    /**
     * 添加信息
     * @param form
     * @return
     */
    @Override
    public boolean insertRecord(EmailRecipientBean form) {
        SellDailyDistributionCustomizeExample example = new SellDailyDistributionCustomizeExample();
        SellDailyDistributionCustomizeExample.Criteria criteria = example.createCriteria();
        SellDailyDistributionCustomize SellDailyDistribution = new SellDailyDistributionCustomize();
        if (StringUtils.isNotBlank(form.getBusinessName())) {
            SellDailyDistribution.setBusinessName(form.getBusinessName());
        }
        if (StringUtils.isNotBlank(form.getTimePoint() + "")) {
            SellDailyDistribution.setTimePoint(form.getTimePoint());
        }
        if (StringUtils.isNotBlank(form.getTime() + "")) {
            SellDailyDistribution.setTime(form.getTime());
        }
        if (StringUtils.isNotBlank(form.getEmail() + "")) {
            SellDailyDistribution.setEmail(form.getEmail());
        }
        SellDailyDistribution.setStatus(1);
        SellDailyDistribution.setCreateName(form.getCreateName());
        return sellDailyDistributionMapper.insert(SellDailyDistribution)> 0 ? true : false;
    }

    /**
     * 校验邮箱合法性
     * @param Email
     * @return
     */
    @Override
    public boolean checkEmail(String Email) {
        SellDailyDistributionCustomizeExample example = new SellDailyDistributionCustomizeExample();
        List<SellDailyDistributionCustomize> sell = sellDailyDistributionMapper.selectByExample(example);
        for (SellDailyDistributionCustomize sellDaily : sell) {
            String[] split = sellDaily.getEmail().split(";");
            for (String s : split) {
                if(StringUtils.equals(s,Email)){
                    return true;
                }
            }
        }
        return false;
    }
}
