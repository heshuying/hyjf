package com.hyjf.wechat.service.mytrade;

import com.hyjf.mybatis.model.customize.app.AppAccountTradeListCustomize;
import com.hyjf.mybatis.model.customize.app.AppTradeListCustomize;
import com.hyjf.wechat.base.BaseService;
import com.hyjf.wechat.controller.user.mytrade.QueryTradeListQO;

import java.util.List;

/**
 * Created by cuigq on 2018/2/8.
 */
public interface MyTradeService extends BaseService {

    /**
     * 交易明细条数
     * @param qo
     * @return
     */
    int countTradeList(QueryTradeListQO qo);

    /**
     * 交易明细列表
     * @param qo
     * @return
     */
    List<AppTradeListCustomize> queryTradeList(QueryTradeListQO qo);

    List<AppAccountTradeListCustomize> queryTradeTypes();
}
