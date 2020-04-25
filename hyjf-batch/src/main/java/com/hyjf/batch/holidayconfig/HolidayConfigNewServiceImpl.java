package com.hyjf.batch.holidayconfig;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hyjf.common.http.HttpDeal;
import com.hyjf.mybatis.mapper.customize.HolidaysConfigNewCustomizeMapper;
import com.hyjf.mybatis.model.auto.HolidaysConfigNew;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @author xiasq
 * @version HolidaysConfigNewServiceImpl, v0.1 2018/7/26 17:56
 */
@Service
public class HolidayConfigNewServiceImpl implements HolidayConfigNewService {
	private Logger logger = LoggerFactory.getLogger(HolidayConfigNewServiceImpl.class);

	@Autowired
	private HolidaysConfigNewCustomizeMapper holidaysConfigNewCustomizeMapper;

	private String url = "http://www.easybots.cn/api/holidayconfig.php?m=";

	/**
	 * 更新节假日配置 从第三方提供的api接口查询，更新本年度
	 *
	 * @return
	 * @throws ReturnMessageException
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateHolidaysConfig(int currentYear) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		String month = "";
		// 录入本年
		for (int index = 1; index <= 12; index++) {
			month = String.format("%02d", index);
			String date = currentYear + "-" + month;
			List<HolidaysConfigNew> list = holidaysConfigNewCustomizeMapper.selectByYearMonth(currentYear, index);
			List<HolidaysConfigNew> updateList = new ArrayList<>(list.size());
			// {"201801":{"01":"2","06":"2","07":"2","13":"1","14":"2","20":"2","21":"1","27":"2","28":"1"}}
			String result = HttpDeal.get(url + date);
			logger.info("{}月份节假日查询结果：{}", month, result);
			if (StringUtils.isEmpty(result)) {
				logger.error("{}月份节假日查询失败....", month);
				continue;
			}
			Map<String, Object> map = json2map(result);
			for (String ymStr : map.keySet()) {
				Map<String, Object> detailMap = (Map<String, Object>) map.get(ymStr);
				for (String dayStr : detailMap.keySet()) {
					int holidaysFlag = Integer.parseInt((String) detailMap.get(dayStr));
					for (HolidaysConfigNew holidaysConfigNew : list) {
						String apiQueryStr = date.concat("-").concat(dayStr);
						String sdfStr = sdf.format(holidaysConfigNew.getDayTime());
						if (apiQueryStr.equals(sdfStr)) {
							holidaysConfigNew.setHolidayFlag(holidaysFlag);
							updateList.add(holidaysConfigNew);
						}
					}
				}
			}
			if (CollectionUtils.isEmpty(updateList)) {
				logger.error("{}月份节假日更新失败....", month);
				continue;
			}
			holidaysConfigNewCustomizeMapper.batchUpdate(updateList);
		}

		return true;
	}

	/**
	 * 初始化本年度配置
	 *
	 * @param year
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void initCurrentYearConfig(int year) {
		List<HolidaysConfigNew> list = new ArrayList<>(366);
		HolidaysConfigNew holidaysConfigNew = null;
		Calendar c = Calendar.getInstance();
		for (int month = 0; month < 12; month++) {
			c.set(year, month, 1);
			int lastDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
			for (int day = 1; day <= lastDay; day++) {
				holidaysConfigNew = new HolidaysConfigNew();
				c.set(year, month, day);
				holidaysConfigNew.setDayTime(c.getTime());
				list.add(holidaysConfigNew);
			}
		}
		// 先删除在插入， 防止重复
		holidaysConfigNewCustomizeMapper.deleteByYear(year);
		holidaysConfigNewCustomizeMapper.batchInsert(list);
	}

	@Override
	public boolean hasHolidayConfig(int currentYear) {
		List<HolidaysConfigNew> list = holidaysConfigNewCustomizeMapper.selectByYearMonth(currentYear, 1);
		return CollectionUtils.isEmpty(list) ? false : true;
	}

	private Map<String, Object> json2map(String str_json) {
		Map<String, Object> res = null;
		try {
			Gson gson = new Gson();
			res = gson.fromJson(str_json, new TypeToken<Map<String, Object>>() {
			}.getType());
		} catch (JsonSyntaxException e) {
			logger.error("数据转换异常...", e);
		}
		return res;
	}
}
