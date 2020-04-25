package com.hyjf.admin.invite.drawconf;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.InvitePrizeConf;
import com.hyjf.mybatis.model.customize.admin.PrizeGetCustomize;

public interface DrawConfService extends BaseService{

    List<PrizeGetCustomize> getRecordList(DrawConfBean bean, int limitStart, int limitEnd);

    int insertRecord(InvitePrizeConf prize);

    String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception;

    InvitePrizeConfCustom getPrizeByGroupCode(String groupCode);

    int updatePrizeConfig(DrawConfBean prize);

    int updatePrizeStatus(Map<String, Object> paraMap);

}