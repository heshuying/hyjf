package com.hyjf.admin.whereaboutspage;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.WhereaboutsPageConfig;
import com.hyjf.mybatis.model.customize.whereaboutspage.WhereaboutsPageConfigCustomize;

public interface WhereaboutsPageService extends BaseService {

    Integer countRecord(WhereaboutsPageConfigCustomize form);

    List<WhereaboutsPageConfigCustomize> getRecordList(WhereaboutsPageConfigCustomize form);

    void deleteRecord(String recordId);

    String checkUtmId(String utmId);

    String checkReferrer(String referrer);

    void insertAction(WhereaboutsPageBean form);

    String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception;

    void getWhereaboutsPageConfigById(WhereaboutsPageBean form);

    void updateAction(WhereaboutsPageBean form);

    int statusAction(Integer statusOn, Integer id);


}