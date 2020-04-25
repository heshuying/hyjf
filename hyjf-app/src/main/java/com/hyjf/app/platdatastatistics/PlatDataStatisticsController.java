package com.hyjf.app.platdatastatistics;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.mybatis.model.auto.CalculateInvestInterest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 平台数据统计
 *
 * @author liuyang
 */
@Controller
@RequestMapping(PlatDataStatisticsDefine.REQUEST_MAPPING)
public class PlatDataStatisticsController extends BaseController {

    @Autowired
    private PlatDataStatisticsService platDataStatisticsService;


    /**
     * 获取临时数据，之后正式接口上线将删除
     * 累计出借 + 累计收益
     * @return
     */
	@RequestMapping(PlatDataStatisticsDefine.GET_TEMP_DATA)
    @ResponseBody
	public JSONObject getTempData() {
		JSONObject result = new JSONObject();
		result.put("status", "000");
		result.put("statusDesc", "成功");

		DecimalFormat df = new DecimalFormat("#,##0");
		CalculateInvestInterest calculateInvestInterest = this.platDataStatisticsService
				.selectCalculateInvestInterest();
		if (calculateInvestInterest != null) {
			// 累计出借
			result.put("investTotal",
					df.format(calculateInvestInterest.getTenderSum().setScale(0, BigDecimal.ROUND_HALF_DOWN)));
			// 累计收益
			result.put("interestTotal",
					df.format(calculateInvestInterest.getInterestSum().setScale(0, BigDecimal.ROUND_HALF_DOWN)));
		}
		return result;
	}
}

