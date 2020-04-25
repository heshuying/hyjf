package com.hyjf.admin.manager.activity.actnov2017.bargain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.hyjf.admin.BaseService;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.ActJanBargain;
import com.hyjf.mybatis.model.auto.ActJanBargainExample;
import com.hyjf.mybatis.model.auto.ActJanPrizewinList;
import com.hyjf.mybatis.model.auto.ActJanPrizewinListExample;

public interface ActBargainService extends BaseService {

	int countPrizeWinList();

	List<ActJanPrizewinList> selectPrizeWinList(int limitStart, int limitEnd);

	int countBargainList(ActBargainBean form);

	List<ActJanBargain> selectBargainList(ActBargainBean form, int limitStart, int limitEnd);


}
