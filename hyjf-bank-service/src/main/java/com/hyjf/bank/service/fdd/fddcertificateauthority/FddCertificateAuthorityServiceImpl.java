package com.hyjf.bank.service.fdd.fddcertificateauthority;

import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.pay.lib.fadada.bean.DzqzCallBean;
import com.hyjf.pay.lib.fadada.util.DzqzCallUtil;
import com.hyjf.pay.lib.fadada.util.DzqzConstant;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 法大大CA认证Service实现类
 *
 * @author liuyang
 */
@Service
public class FddCertificateAuthorityServiceImpl extends BaseServiceImpl implements FddCertificateAuthorityService {

    Logger _log = LoggerFactory.getLogger(FddCertificateAuthorityServiceImpl.class);

    /**
     * 调用法大大CA认证接口
     *
     * @param userId
     */
    @Override
    public void updateUserCAInfo(Integer userId) throws Exception {
        _log.info("CA认证用户ID:[" + userId + "].");
        // 当前时间
        Integer nowTime = GetDate.getNowTime10();
        // 根据用户ID获取用户信息
        Users users = this.getUsers(userId);
        if (users == null) {
            _log.info("根据用户ID获取用户信息失败,用户ID:[" + userId + "].");
            throw new Exception("根据用户ID获取用户信息失败,用户ID:[" + userId + "].");
        }
        // 根据用户ID获取用户详情信息
        UsersInfo usersInfo = this.getUsersInfoByUserId(userId);
        if (usersInfo == null) {
            _log.info("根据用户ID获取用户详情信息失败,用户ID:[" + userId + "].");
            throw new Exception("根据用户ID获取用户详情信息失败,用户ID:[" + userId + "].");
        }

        // 根据用户ID查询用户CA认证信息
        CertificateAuthority certificateAuthority = this.selectCAInfoByUserId(userId);
        // 用户类型 0普通用户 1企业用户
        Integer userType = users.getUserType();
        // 手机号
        String mobile = users.getMobile();
        // 邮箱
        String email = users.getEmail();
        // 普通用户CA认证
        if (userType == 0) {
            // 参数生成
            DzqzCallBean bean = new DzqzCallBean();
            bean.setUserId(userId);
            bean.setLogordid(GetOrderIdUtils.getOrderId0(userId));
            bean.setTxCode("syncPerson_auto");
            bean.setApp_id(DzqzConstant.HYJF_FDD_APP_ID);
            bean.setV(DzqzConstant.HYJF_FDD_VERSION);
            bean.setTimestamp(GetDate.getDate("yyyyMMddHHmmss"));
            bean.setCustomer_name(usersInfo.getTruename());// 客户姓名
            bean.setEmail(StringUtils.isNotBlank(users.getEmail()) ? users.getEmail() : "");// 电子邮箱
            bean.setIdent_type("0");// 证件类型
            bean.setIdCard(usersInfo.getIdcard());// 身份证号
            bean.setMobile(users.getMobile());// 手机号
            // 调用接口
            DzqzCallBean result = DzqzCallUtil.callApiBg(bean);
            if (result != null) {
                _log.info("CA认证成功:用户ID:[" + userId + "].");
                if ("success".equals(result.getResult())) {
                    users.setIsCaFlag(1);
                    boolean isUserUpdateFlag = this.usersMapper.updateByPrimaryKeySelective(users) > 0 ? true : false;
                    if (!isUserUpdateFlag) {
                        _log.info("CA认证成功后,更新用户表CA标识失败,用户ID:[" + userId + "].");
                        throw new Exception("CA认证成功后,更新用户表CA标识失败,用户ID:[" + userId + "].");
                    }
                }
                //  如果用户没有进行过CA认证
                if (certificateAuthority == null) {
                    CertificateAuthority newCertificateAuthority = new CertificateAuthority();
                    newCertificateAuthority.setUserId(userId);
                    newCertificateAuthority.setUserName(users.getUsername());
                    newCertificateAuthority.setMobile(mobile);
                    newCertificateAuthority.setTrueName(usersInfo.getTruename());
                    newCertificateAuthority.setIdType(0);
                    newCertificateAuthority.setIdNo(usersInfo.getIdcard());
                    newCertificateAuthority.setEmail(StringUtils.isBlank(users.getEmail()) ? "" : users.getEmail());
                    newCertificateAuthority.setStatus(StringUtils.isBlank(result.getResult()) ? "" : result.getResult());
                    newCertificateAuthority.setCode(StringUtils.isBlank(result.getCode()) ? "" : result.getCode());// 状态码
                    newCertificateAuthority.setCustomerId(StringUtils.isBlank(result.getCustomer_id()) ? "" : result.getCustomer_id());// 客户编号
                    newCertificateAuthority.setRemark(StringUtils.isBlank(result.getMsg()) ? "" : result.getMsg());// 描述
                    newCertificateAuthority.setCreateTime(nowTime);
                    newCertificateAuthority.setCreateUserId(userId);
                    newCertificateAuthority.setCreateUserName(users.getUsername());
                    newCertificateAuthority.setUpdateTime(nowTime);
                    newCertificateAuthority.setUpdateUserId(userId);
                    newCertificateAuthority.setUpdateUserName(users.getUsername());
                    boolean isInsertFlag = this.certificateAuthorityMapper.insertSelective(newCertificateAuthority) > 0 ? true : false;
                    if (!isInsertFlag) {
                        _log.info("CA认证成功后,插入用户CA认证记录表失败,用户ID:[" + userId + "].");
                        throw new Exception("CA认证成功后,插入用户CA认证记录表失败,用户ID:[" + userId + "].");
                    }
                } else {
                    // 如果用户已进行过CA认证,更新用户CA认证记录
                    certificateAuthority.setMobile(mobile);
                    certificateAuthority.setTrueName(usersInfo.getTruename());
                    certificateAuthority.setIdType(0);
                    certificateAuthority.setIdNo(usersInfo.getIdcard());
                    certificateAuthority.setEmail(StringUtils.isBlank(users.getEmail()) ? "" : users.getEmail());
                    certificateAuthority.setStatus(StringUtils.isBlank(result.getResult()) ? "" : result.getResult());
                    certificateAuthority.setCode(StringUtils.isBlank(result.getCode()) ? "" : result.getCode());// 状态码
                    certificateAuthority.setCustomerId(StringUtils.isBlank(result.getCustomer_id()) ? "" : result.getCustomer_id());// 客户编号
                    certificateAuthority.setRemark(StringUtils.isBlank(result.getMsg()) ? "" : result.getMsg());// 描述
                    certificateAuthority.setUpdateTime(nowTime);
                    certificateAuthority.setUpdateUserId(userId);
                    certificateAuthority.setUpdateUserName(users.getUsername());
                    boolean isUpdateFlag = this.certificateAuthorityMapper.updateByPrimaryKeySelective(certificateAuthority) > 0 ? true : false;
                    if (!isUpdateFlag) {
                        _log.info("CA认证成功后,更新用户CA认证记录表失败,用户ID:[" + userId + "].");
                        throw new Exception("CA认证成功后,更新用户CA认证记录表失败,用户ID:[" + userId + "].");
                    }
                }
            }
        } else {
            // 企业用户CA认证
            CorpOpenAccountRecord corpOpenAccountRecord = this.getCompanyInfoByUserId(userId);
            if (corpOpenAccountRecord == null) {
                _log.info("获取企业信息失败,企业用户ID:[" + userId + "].");
                throw new Exception("获取企业信息失败,企业用户ID:[" + userId + "].");
            }
            // 组织机构代码
            String busiCode = corpOpenAccountRecord.getBusiCode();
            // 调用法大大企业CA认证接口
            // 参数生成
            DzqzCallBean bean = new DzqzCallBean();
            bean.setUserId(userId);
            bean.setLogordid(GetOrderIdUtils.getOrderId0(userId));
            bean.setTxCode("syncCompany_auto");
            bean.setApp_id(DzqzConstant.HYJF_FDD_APP_ID);
            bean.setV(DzqzConstant.HYJF_FDD_VERSION);
            bean.setTimestamp(GetDate.getDate("yyyyMMddHHmmss"));
            bean.setCustomer_name(usersInfo.getTruename());// 客户姓名
            bean.setEmail(StringUtils.isNotBlank(users.getEmail()) ? users.getEmail() : "");// 电子邮箱
            bean.setIdCard(busiCode);// 组织机构代码
            bean.setMobile(users.getMobile());// 手机号
            // 调用接口
            DzqzCallBean result = DzqzCallUtil.callApiBg(bean);
            if (result != null) {
                // CA认证成功后,更新用户表
                if ("success".equals(result.getResult())) {
                    _log.info("CA认证成功:用户ID:[" + userId + "].");
                    users.setIsCaFlag(1);
                    boolean isUserUpdateFlag = this.usersMapper.updateByPrimaryKeySelective(users) > 0 ? true : false;
                    if (!isUserUpdateFlag) {
                        _log.info("CA认证成功后,更新用户表CA标识失败,用户ID:[" + userId + "].");
                        throw new Exception("CA认证成功后,更新用户表CA标识失败,用户ID:[" + userId + "].");
                    }
                }

                // 如果没有做过CA认证
                if (certificateAuthority == null) {
                    // 插入CA认证记录
                    CertificateAuthority newCertificateAuthority = new CertificateAuthority();
                    newCertificateAuthority.setUserId(userId);
                    newCertificateAuthority.setUserName(users.getUsername());
                    newCertificateAuthority.setMobile(mobile);
                    newCertificateAuthority.setTrueName(corpOpenAccountRecord.getBusiName());
                    newCertificateAuthority.setIdType(1);
                    newCertificateAuthority.setIdNo(corpOpenAccountRecord.getBusiCode());
                    newCertificateAuthority.setEmail(StringUtils.isBlank(users.getEmail()) ? "" : users.getEmail());
                    newCertificateAuthority.setStatus(StringUtils.isBlank(result.getResult()) ? "" : result.getResult());
                    newCertificateAuthority.setCode(StringUtils.isBlank(result.getCode()) ? "" : result.getCode());// 状态码
                    newCertificateAuthority.setCustomerId(StringUtils.isBlank(result.getCustomer_id()) ? "" : result.getCustomer_id());// 客户编号
                    newCertificateAuthority.setRemark(StringUtils.isBlank(result.getMsg()) ? "" : result.getMsg());// 描述
                    newCertificateAuthority.setCreateTime(nowTime);
                    newCertificateAuthority.setCreateUserId(userId);
                    newCertificateAuthority.setCreateUserName(users.getUsername());
                    newCertificateAuthority.setUpdateTime(nowTime);
                    newCertificateAuthority.setUpdateUserId(userId);
                    newCertificateAuthority.setUpdateUserName(users.getUsername());
                    boolean isInsertFlag = this.certificateAuthorityMapper.insertSelective(newCertificateAuthority) > 0 ? true : false;
                    if (!isInsertFlag) {
                        _log.info("CA认证成功后,插入用户CA认证记录表失败,用户ID:[" + userId + "].");
                        throw new Exception("CA认证成功后,插入用户CA认证记录表失败,用户ID:[" + userId + "].");
                    }
                } else {
                    // 如果已经做过CA认证,更新CA认证记录表
                    certificateAuthority.setMobile(mobile);
                    certificateAuthority.setTrueName(usersInfo.getTruename());
                    certificateAuthority.setIdType(1);
                    certificateAuthority.setIdNo(corpOpenAccountRecord.getBusiCode());
                    certificateAuthority.setEmail(StringUtils.isBlank(users.getEmail()) ? "" : users.getEmail());
                    certificateAuthority.setStatus(StringUtils.isBlank(result.getResult()) ? "" : result.getResult());
                    certificateAuthority.setCode(StringUtils.isBlank(result.getCode()) ? "" : result.getCode());// 状态码
                    certificateAuthority.setCustomerId(StringUtils.isBlank(result.getCustomer_id()) ? "" : result.getCustomer_id());// 客户编号
                    certificateAuthority.setRemark(StringUtils.isBlank(result.getMsg()) ? "" : result.getMsg());// 描述
                    certificateAuthority.setUpdateTime(nowTime);
                    certificateAuthority.setUpdateUserId(userId);
                    certificateAuthority.setUpdateUserName(users.getUsername());
                    boolean isUpdateFlag = this.certificateAuthorityMapper.updateByPrimaryKeySelective(certificateAuthority) > 0 ? true : false;
                    if (!isUpdateFlag) {
                        _log.info("CA认证成功后,更新用户CA认证记录表失败,用户ID:[" + userId + "].");
                        throw new Exception("CA认证成功后,更新用户CA认证记录表失败,用户ID:[" + userId + "].");
                    }
                }
            }
        }
    }


    /**
     * 企业用户获取企业相关信息
     *
     * @param userId
     * @return
     */
    private CorpOpenAccountRecord getCompanyInfoByUserId(Integer userId) {
        CorpOpenAccountRecordExample example = new CorpOpenAccountRecordExample();
        CorpOpenAccountRecordExample.Criteria cra = example.createCriteria();
        cra.andUserIdEqualTo(userId);
        List<CorpOpenAccountRecord> list = this.corpOpenAccountRecordMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
}
