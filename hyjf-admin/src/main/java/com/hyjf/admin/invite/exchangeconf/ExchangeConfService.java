package com.hyjf.admin.invite.exchangeconf;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.InvitePrizeConf;
import com.hyjf.mybatis.model.customize.admin.PrizeGetCustomize;

public interface ExchangeConfService extends BaseService{

    List<PrizeGetCustomize> getRecordList(ExchangeConfBean bean, int limitStart, int limitEnd);

    InvitePrizeConfCustom getPrizeByGroupCode(String groupCode);

    int insertRecord(InvitePrizeConf prize);

    String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception;

    int updatePrizeStatus(Map<String, Object> paraMap);

    int updatePrizeConfig(ExchangeConfBean prize);

}