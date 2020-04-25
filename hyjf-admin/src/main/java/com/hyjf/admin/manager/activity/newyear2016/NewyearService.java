package com.hyjf.admin.manager.activity.newyear2016;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.admin.Newyear2016UserCardCustomize;
import com.hyjf.mybatis.model.customize.admin.Newyear2016UserPrizeCustomize;
import com.hyjf.mybatis.model.customize.admin.Newyear2016UserYuanXiaoCustomize;

public interface NewyearService extends BaseService {

    Integer selectUserPrizeRecordCount(UserPrizeListBean paramBean);

    List<Newyear2016UserPrizeCustomize> selectUserPrizeRecordList(UserPrizeListBean paramBean);
    
    Integer selectUserCardRecordCount(UserCardListBean paramBean);

    List<Newyear2016UserCardCustomize> selectUserCardRecordList(UserCardListBean paramBean);
    
    Integer selectUserYuanXiaoRecordCount(UserYuanXiaoListBean paramBean);

    List<Newyear2016UserYuanXiaoCustomize> selectUserYuanXiaoRecordList(UserYuanXiaoListBean paramBean);
    
}
