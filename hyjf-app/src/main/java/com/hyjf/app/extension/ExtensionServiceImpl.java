/*
 * Copyright(c) 2012-2016 JD Pharma.Ltd. All Rights Reserved.
 */
package com.hyjf.app.extension;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.app.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.ExtensionTemp;
import com.hyjf.mybatis.model.auto.ExtensionTempExample;

/**
 * 此处为类说明
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年3月16日
 * @see 上午11:20:48
 */
@Service
public class ExtensionServiceImpl extends BaseServiceImpl implements ExtensionService {
    

    @Override
    public int insertExtensionInfo(ExtensionTemp extensionTemp) {
        return extensionTempMapper.insertSelective(extensionTemp);
    }

    @Override
    public List<ExtensionTemp> queryExtensionInfo(Integer userId) {
        ExtensionTempExample example = new ExtensionTempExample();
        example.createCriteria().andUserIdEqualTo(userId);
        return extensionTempMapper.selectByExample(example);
    }

}
