package com.hyjf.bank.service.fdd.fdduserinfochange;

import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.CertificateAuthority;
import com.hyjf.mybatis.model.auto.CertificateAuthorityExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.fadada.bean.DzqzCallBean;
import com.hyjf.pay.lib.fadada.util.DzqzCallUtil;
import com.hyjf.pay.lib.fadada.util.DzqzConstant;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 法大大客户信息修改Service实现类
 *
 * @author liuyang
 */
@Service
public class FddUserInfoChangeServiceImpl extends BaseServiceImpl implements FddUserInfoChangeService {

    Logger _log = LoggerFactory.getLogger(FddUserInfoChangeServiceImpl.class);

    /**
     * 请求法大大客户信息修改接口,更新用户CA认证相关信息
     *
     * @param userId
     */
    @Override
    public void updateUserCAInfo(Integer userId) throws Exception {
        // 根据用户ID获取用户信息
        Users users = this.getUsers(userId);
        // 获取用户CA认证相关信息
        CertificateAuthority certificateAuthority = this.selectCAInfoByUserId(userId);
        // 客户编号
        String customerId = certificateAuthority.getCustomerId();
        // 调用客户信息修改接口
        // 参数生成
        DzqzCallBean bean = new DzqzCallBean();
        bean.setUserId(userId);
        bean.setTxCode("infochange");
        bean.setApp_id(DzqzConstant.HYJF_FDD_APP_ID);
        bean.setV(DzqzConstant.HYJF_FDD_VERSION);
        bean.setTimestamp(GetDate.getDate("yyyyMMddHHmmss"));
        bean.setCustomer_id(customerId);// 客户编号
        bean.setEmail(StringUtils.isNotBlank(users.getEmail()) ? users.getEmail() : "");// 电子邮箱
        bean.setMobile(users.getMobile());// 手机号
        // 调用接口
        DzqzCallBean result = DzqzCallUtil.callApiBg(bean);
        if (result != null && "success".equals(result.getResult())) {
            // 如果返回成功
            certificateAuthority.setMobile(users.getMobile());// 手机号
            certificateAuthority.setEmail(StringUtils.isNotBlank(users.getEmail()) ? users.getEmail() : "");// 电子邮箱
            // 更新用户认证信息表
            boolean isUpdateFlag = this.certificateAuthorityMapper.updateByPrimaryKeySelective(certificateAuthority) > 0 ? true : false;
            if(!isUpdateFlag){
                throw new Exception("调用客户信息修改接口成功之后,更新用户认证信息表失败,用户ID:["+userId+"].");
            }
        }
    }
}
