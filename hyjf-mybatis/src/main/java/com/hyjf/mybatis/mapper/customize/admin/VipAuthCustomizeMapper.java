/**
 * Description:会员管理初始化列表查询
 * Copyright: (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 上午11:01:57
 * Modification History:
 * Modified by : 
 * */

package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;

import com.hyjf.mybatis.model.customize.admin.VipAuthCustomize;

public interface VipAuthCustomizeMapper {

    List<VipAuthCustomize> getRecordList(VipAuthCustomize vipAuthCustomize);

    VipAuthCustomize selectByPrimaryKey(Integer record);

}
