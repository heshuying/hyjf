package com.hyjf.wechat.controller.user.mytrade;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hyjf.mybatis.model.customize.app.AppAccountTradeListCustomize;
import com.hyjf.mybatis.model.customize.app.AppTradeListCustomize;
import com.hyjf.wechat.annotation.SignValidate;
import com.hyjf.wechat.base.BaseController;
import com.hyjf.wechat.base.BaseResultBean;
import com.hyjf.wechat.base.SimpleResultBean;
import com.hyjf.wechat.service.mytrade.MyTradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by cuigq on 2018/2/8.
 */
@Controller
@RequestMapping(MyTradeDefine.REQUEST_MAPPING)
public class MyTradeController extends BaseController {

    @Autowired
    private MyTradeService myTradeService;

    @SignValidate
    @RequestMapping(value = MyTradeDefine.QUERY_TRADE_LIST)
    @ResponseBody
    public Map<String, Object> queryTradeList(HttpServletRequest request, QueryTradeListQO qo) {
        SimpleResultBean<Map<String, Object>> resultBean = new SimpleResultBean<>();

        Map<String, Object> mapVo = Maps.newHashMap();
        resultBean.setObject(mapVo);

        String userId = String.valueOf(requestUtil.getRequestUserId(request));

        Preconditions.checkArgument(!Strings.isNullOrEmpty(userId));
        qo.setUserId(userId);

        int totalCount = myTradeService.countTradeList(qo);
//        mapVo.put("totalCount", totalCount);

        List<AppTradeListCustomize> lstTrade = myTradeService.queryTradeList(qo);
        List<AppTradeListCustomize> lstTradeCopy = Lists.newArrayList();
        lstTradeCopy.addAll(lstTrade);
        int page = qo.getCurrentPage();
        int pageSize = qo.getPageSize();
        mapVo.put("lstTrade", lstTradeCopy);
        mapVo.put("currentPage", page);
        mapVo.put("pageSize", pageSize);
        mapVo.put("status", "000");
        mapVo.put("statusDesc", "成功");
        mapVo.put("endPage", page * pageSize >= totalCount ? "1" : "0");
        return mapVo;
    }

    @SignValidate
    @RequestMapping(value = MyTradeDefine.TRADE_TYPES_ACTION, method = RequestMethod.GET)
    @ResponseBody
    public BaseResultBean searchTradeTypes(HttpServletRequest request) {
        SimpleResultBean<List<AppAccountTradeListCustomize>> baseResultBean = new SimpleResultBean<>();
        baseResultBean.setObject(myTradeService.queryTradeTypes());
        return baseResultBean;
    }

}
