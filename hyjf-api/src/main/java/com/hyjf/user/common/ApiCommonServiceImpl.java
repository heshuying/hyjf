/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2017
 * Company: HYJF Corporation
 * @author: lb
 * @version: 1.0
 * Created at: 2017年9月15日 上午9:43:49
 * Modification History:
 * Modified by : 
 */
	
package com.hyjf.user.common;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.common.enums.utils.MsgEnum;
import com.hyjf.common.security.utils.SignUtil;
import com.hyjf.common.validator.CheckUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.user.ApiUserPostBean;
import com.hyjf.util.RSA_Hjs;

/**
 * @author liubin
 */

@Service("ApiCommonService")
public class ApiCommonServiceImpl extends BaseServiceImpl implements ApiCommonService {
	public static final String DEFAULT_ACCESSKEY = "aop.interface.accesskey";
	
	/**
	 * 传入信息验证
	 * @param bean
	 */
	@Override
	public void checkPostBean(ApiUserPostBean bean) {
    	//传入信息验证 
		//(适用 信息码和信息在枚举中)
		CheckUtil.check(Validator.isNotNull(bean.getBindUniqueIdScy()), "Object.required", "bindUniqueIdScy");
//		CheckUtil.check(Validator.isNotNull(bean.getTimestamp()), "Object.required", "Timestamp");
		// (适用 信息码和信息在枚举中)
		CheckUtil.check(Validator.isNotNull(bean.getTimestamp()), MsgEnum.ERR_OBJECT_REQUIRED, "Timestamp");
	}

	/**
	 * 验签
	 * @param bean
	 */
	@Override
	public void checkSign(ApiUserPostBean bean) {
    	CheckUtil.check(SignUtil.checkSignDefaultKey(bean.getChkValue(), bean.getBindUniqueIdScy(), bean.getTimestamp()), "sign");
	}

	/**
	 * 解密
	 * @param bean
	 * @return
	 */
	@Override
	public Long decrypt(ApiUserPostBean bean) {
		// RAC解密
		String str = decrypt(bean.getBindUniqueIdScy());
		// 解密结果数字验证
		CheckUtil.check(Validator.isDigit(str), "Object.digit", "bindUniqueIdScy");
		// 返回
		return Long.parseLong(str);	
	}

	public String decrypt(String str) {
		if (StringUtils.isEmpty(str)) {
			return null;
		}
		try {
		    return RSA_Hjs.decrypt(str,"utf-8");
        } catch (Exception e) {
            CheckUtil.check(false, "decrypt.error");
        }
		return null;
		
	}
	

    /**
     * 加密
     * @param bean
     * @return
     */
    @Override
    public String encode(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        try {
            return RSA_Hjs.encode(str);
        } catch (Exception e) {
            CheckUtil.check(false, "encode.error");
        }
        return null;
    }
	
	/**
	 * 根据绑定信息取得用户id
	 * @param bindUniqueId
	 * @return
	 */
	@Override
	public Integer getUserIdByBind(Long bindUniqueId, int bindPlatformId) {
		//检索条件
		return 1;
	}
}
	