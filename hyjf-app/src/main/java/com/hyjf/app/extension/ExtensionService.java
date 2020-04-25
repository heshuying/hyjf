/*
 * Copyright(c) 2012-2016 JD Pharma.Ltd. All Rights Reserved.
 * 
 */
package com.hyjf.app.extension;

import java.util.List;

import com.hyjf.app.BaseService;
import com.hyjf.mybatis.model.auto.ExtensionTemp;

/**
 * 此处为类说明
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年3月16日
 * @see 上午11:20:17
 */
public interface ExtensionService extends BaseService {
    
    int insertExtensionInfo(ExtensionTemp extensionTemp);

    List<ExtensionTemp> queryExtensionInfo(Integer userId);

}
