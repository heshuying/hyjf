package com.hyjf.wechat.service.myproject;

import com.hyjf.mybatis.model.customize.web.*;
import com.hyjf.wechat.base.BaseService;
import com.hyjf.wechat.controller.user.myAsset.QueryPlanedProjectVO;
import com.hyjf.wechat.controller.user.myAsset.QueryMyProjectVO;

import java.util.List;

/**
 * Created by cuigq on 2018/2/6.
 */
public interface MyProjectService extends BaseService {


    void selectCurrentHoldObligatoryRightList(String userId, int currentPage, int pageSize, QueryMyProjectVO vo);

    void selectRepaymentList(String userId, int currentPage, int pageSize, QueryMyProjectVO vo);

    void selectCreditRecordList(String userId, int currentPage, int pageSize, QueryMyProjectVO vo);

    void selectCurrentHoldPlanList(String userId, int currentPage, int pageSize, QueryMyProjectVO vo);

    void selectRepayMentPlanList(String userId, int currentPage, int pageSize, QueryMyProjectVO vo);
}
