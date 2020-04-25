package com.hyjf.admin.manager.user.certificateauthority;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.CertificateAuthority;
import com.hyjf.mybatis.model.auto.CertificateAuthorityExample;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 法大大电子签章CA认证记录
 *
 * @author liuyang
 */
@Service("adminFddCertificateAuthorityServiceImpl")

public class FddCertificateAuthorityServiceImpl extends BaseServiceImpl implements FddCertificateAuthorityService {
    /**
     * CA认证记录件数
     *
     * @param form
     * @return
     */
    @Override
    public Integer countCertificateAuthorityList(FddCertificateAuthorityBean form) {
        CertificateAuthorityExample example = getCertificateAuthorityExample(form);
        return this.certificateAuthorityMapper.countByExample(example);
    }

    private CertificateAuthorityExample getCertificateAuthorityExample(FddCertificateAuthorityBean form) {
        CertificateAuthorityExample example = getCondition(form);
        return example;
    }

    /**
     * CA认证记录列表
     * @param form
     * @param limitStart
     * @param limitEnd
     * @return
     */
    @Override
    public List<CertificateAuthority> getCertificateAuthorityList(FddCertificateAuthorityBean form, int limitStart, int limitEnd) {
        CertificateAuthorityExample example = getCondition(form);
        if (limitStart >= 0) {
            example.setLimitStart(limitStart);
            example.setLimitEnd(limitEnd);
        }
        example.setOrderByClause("`create_time` Desc");
        return this.certificateAuthorityMapper.selectByExample(example);
    }

    private CertificateAuthorityExample getCondition(FddCertificateAuthorityBean form) {
        CertificateAuthorityExample example = new CertificateAuthorityExample();
        CertificateAuthorityExample.Criteria cra = example.createCriteria();
        // 用户名不为空
        if (StringUtils.isNotBlank(form.getUserNameSrch())) {
            cra.andUserNameEqualTo(form.getUserNameSrch());
        }
        // 用户手机号
        if (StringUtils.isNotBlank(form.getMobileSrch())) {
            cra.andMobileEqualTo(form.getMobileSrch());
        }
        // 姓名
        if (StringUtils.isNotBlank(form.getTrueNameSrch())) {
            cra.andTrueNameEqualTo(form.getTrueNameSrch());
        }
        // 用户类型
        if (StringUtils.isNotBlank(form.getIdTypeSrch())) {
            cra.andIdTypeEqualTo(Integer.parseInt(form.getIdTypeSrch()));
        }
        // 客户编号
        if (StringUtils.isNotBlank(form.getCustomerIdSrch())) {
            cra.andCustomerIdLike("%"+form.getCustomerIdSrch()+"%");
        }
        // 检索条件转账时间开始
        if (StringUtils.isNotBlank(form.getStartTimeSrch())) {
            cra.andCreateTimeGreaterThanOrEqualTo(GetDate.dateString2Timestamp(form.getStartTimeSrch() + " 00:00:00"));
        }
        // 检索条件转账时间结束
        if (StringUtils.isNotBlank(form.getEndTimeSrch())) {
            cra.andCreateTimeLessThanOrEqualTo(GetDate.dateString2Timestamp(form.getEndTimeSrch() + " 23:59:59"));
        }
        cra.andCodeEqualTo("1000");
        return example;
    }
}
