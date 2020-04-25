package com.hyjf.admin.manager.statis;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import cn.jpush.api.utils.StringUtils;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.HjhAccountBalanceExample;
import com.hyjf.mybatis.model.customize.HjhAccountBalanceCustomize;
@Service
public class AccountBalanceServiceImpl extends BaseServiceImpl implements AccountBalanceService {

	/**
	 * 查询数量
	 * 
	 * @param customerCustomize
	 * @return public Integer getHjhAccountBalancecount(HjhAccountBalanceCustomize
	 *         entity) {
	 */
	@Override
	public Integer getHjhAccountBalancecount(HjhAccountBalanceCustomize hjhAccountBalanceCustomize) {
//
		HjhAccountBalanceExample example = new HjhAccountBalanceExample();
		HjhAccountBalanceExample.Criteria cra = example.createCriteria();
//		// 查询条件開始日期
//		if (StringUtils.isNotEmpty(hjhAccountBalanceCustomize.getAddTimeStart())) {
//			cra.andDateGreaterThanOrEqualTo(GetDate.stringToDate2(hjhAccountBalanceCustomize.getAddTimeStart()));
//		}
//		// 查询条件结束日期
//		if (StringUtils.isNotEmpty(hjhAccountBalanceCustomize.getAddTimeEnd())) {
//			cra.andDateLessThanOrEqualTo(GetDate.stringToDate2(hjhAccountBalanceCustomize.getAddTimeEnd()));
//		}
//		int i = hjhAccountBalanceMapper.countByExample(example);
//		return i;
		if (StringUtils.isNotEmpty(hjhAccountBalanceCustomize.getAddTimeStart())
				&& StringUtils.isNotEmpty(hjhAccountBalanceCustomize.getAddTimeEnd())) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date start = null;
			Date end = null;
			try {
				start = formatter.parse(hjhAccountBalanceCustomize.getAddTimeStart());
				end = formatter.parse(hjhAccountBalanceCustomize.getAddTimeEnd());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cra.andDateGreaterThanOrEqualTo(GetDate.stringToDate2(hjhAccountBalanceCustomize.getAddTimeStart()));
			cra.andDateLessThanOrEqualTo(GetDate.stringToDate2(hjhAccountBalanceCustomize.getAddTimeEnd()));
//			cra.andDateBetween(new java.sql.Date(start.getTime()), new java.sql.Date(end.getTime()));
		}
		// 排序
		Integer count = this.hjhAccountBalanceMapper.countByExample(example);
		return count;
	}

	/**
	 * 
	 * 查询List集合数据
	 * 
	 * 
	 */
	@Override
	public List<HjhAccountBalanceCustomize> getHjhAccountBalanceList(HjhAccountBalanceCustomize hjhAccountBalanceCustomize) {
			// 封装查询条件

//			HjhAccountBalanceExample example = new HjhAccountBalanceExample();
//			HjhAccountBalanceExample.Criteria cra = example.createCriteria();
//			// 查询条件開始日期
//			if (StringUtils.isNotEmpty(hjhAccountBalanceBean.getAddTimeStart())) {
//				cra.andDateGreaterThanOrEqualTo(GetDate.stringToDate2(hjhAccountBalanceBean.getAddTimeStart()));
//			}
//			// 查询条件结束日期
//			if (StringUtils.isNotEmpty(hjhAccountBalanceBean.getAddTimeEnd())) {
//				cra.andDateLessThanOrEqualTo(GetDate.stringToDate2(hjhAccountBalanceBean.getAddTimeEnd()));
//			}
//			// 分页条件
//			example.setLimitStart(hjhAccountBalanceBean.getLimitStart());
//			example.setLimitEnd(hjhAccountBalanceBean.getLimitEnd());
//			// 排序
//			example.setOrderByClause("`date` desc");
//			List<HjhAccountBalance> list = hjhAccountBalanceMapper.selectByExample(example);
//			return list;
		if (hjhAccountBalanceCustomize.getAddTimeStart() != null
				&& hjhAccountBalanceCustomize.getAddTimeEnd() != null) {
			Date date;
			try {
				return hjhAccountBalanceCustomizeMapper.getHjhAccountBalanceList(hjhAccountBalanceCustomize);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return hjhAccountBalanceCustomizeMapper.getHjhAccountBalanceList(hjhAccountBalanceCustomize);
}

	/**
	 * 
	 * 查询合计数据
	 */
	@Override
	public HjhAccountBalanceCustomize getHjhAccountBalanceSum(HjhAccountBalanceCustomize hjhAccountBalanceCustomize) {
		return hjhAccountBalanceCustomizeMapper.getHjhAccountBalanceSum(hjhAccountBalanceCustomize);
	}

	/**
	 * 按月查询数量
	 * 
	 * @param customerCustomize
	 * @return public Integer getHjhAccountBalancecount(HjhAccountBalanceCustomize
	 *         entity) {
	 */
	@Override
	public Integer getHjhAccountBalanceMonthCount(HjhAccountBalanceCustomize hjhAccountBalanceCustomize) {

		if (StringUtils.isNotEmpty(hjhAccountBalanceCustomize.getAddTimeStart())
				&& StringUtils.isNotEmpty(hjhAccountBalanceCustomize.getAddTimeEnd())) {
			
				String addTimeStart = hjhAccountBalanceCustomize.getAddTimeStart();
				String kai = "-01";
				String kaishi =addTimeStart+kai;
				hjhAccountBalanceCustomize.setAddTimeStart(kaishi);
				String addTimeEnd = hjhAccountBalanceCustomize.getAddTimeEnd();
				String mo = "-31";
				String mowei= addTimeEnd+mo;
				hjhAccountBalanceCustomize.setAddTimeEnd(mowei);
//			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
//			Date start = null;
//			Date end = null;
//			Date yuechu = null;
//			Date yuemo = null;
			try {
//				start = formatter.parse(hjhAccountBalanceCustomize.getAddTimeStart());
//				end = formatter.parse(hjhAccountBalanceCustomize.getAddTimeEnd());
//				yuechu = GetDate.getYUECHU(start);
//				yuemo = GetDate.getYUEMO(end);
//				HashMap<String, Date> Map = new HashMap<>();
//				Map.put("yuechu", yuechu);
//				Map.put("yuemo", yuemo);
				int count = hjhAccountBalanceCustomizeMapper.getHjhAccountBalanceMonthCountNew(hjhAccountBalanceCustomize);
				return count;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		// 排序
		Integer count = this.hjhAccountBalanceCustomizeMapper
				.getHjhAccountBalanceMonthCount(hjhAccountBalanceCustomize);
		return count;
	}

	/**
	 * 
	 * 按月查询List集合数据
	 * 
	 * 
	 */
	@Override
	public List<HjhAccountBalanceCustomize> getHjhAccountBalanceMonthList(
			HjhAccountBalanceCustomize hjhAccountBalanceCustomize) {
		if (StringUtils.isNotEmpty(hjhAccountBalanceCustomize.getAddTimeStart()) && StringUtils.isNotEmpty(hjhAccountBalanceCustomize.getAddTimeEnd())) {
			try {
//				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
//				Date start = formatter.parse(hjhAccountBalanceCustomize.getAddTimeStart());
//				Date end = formatter.parse(hjhAccountBalanceCustomize.getAddTimeEnd());
//				Date yuechu = GetDate.getYUECHU(start);
//				Date yuemo = GetDate.getYUEMO(end);
//				hjhAccountBalanceCustomize.setYuechu(yuechu);
//				hjhAccountBalanceCustomize.setYuemo(yuemo);
//				String addTimeStart = hjhAccountBalanceCustomize.getAddTimeStart();
//				String kai = "-1";
//				String kaishi =addTimeStart+kai;
//				hjhAccountBalanceCustomize.setAddTimeStart(kaishi);
//				String addTimeEnd = hjhAccountBalanceCustomize.getAddTimeEnd();
//				String mo = "-31";
//				String mowei= addTimeEnd+mo;
//				hjhAccountBalanceCustomize.setAddTimeEnd(mowei);
				List<HjhAccountBalanceCustomize> hjhAccountBalanceList = hjhAccountBalanceCustomizeMapper
						.getHjhAccountBalanceMonthListNew(hjhAccountBalanceCustomize);
				return hjhAccountBalanceList;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		List<HjhAccountBalanceCustomize> list = hjhAccountBalanceCustomizeMapper
				.getHjhAccountBalanceMonthList(hjhAccountBalanceCustomize);
		return list;
	}

	/**
	 * 
	 * 查询月合计数据
	 */
	@Override
	public HjhAccountBalanceCustomize getHjhAccountBalanceMonthSum(
			HjhAccountBalanceCustomize hjhAccountBalanceCustomize) {
		if (hjhAccountBalanceCustomize.getAddTimeStart() != null
				&& hjhAccountBalanceCustomize.getAddTimeEnd() != null) {
			try {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
				Date start = formatter.parse(hjhAccountBalanceCustomize.getAddTimeStart());
				Date end = formatter.parse(hjhAccountBalanceCustomize.getAddTimeEnd());
				Date firstDay = GetDate.getFirstDayOnMonth(start);
				Date lastDay = GetDate.getLastDayOnMonth(end);

				hjhAccountBalanceCustomize.setYuechu(firstDay);
				hjhAccountBalanceCustomize.setYuemo(lastDay);
				return hjhAccountBalanceCustomizeMapper.getHjhAccountBalanceSum(hjhAccountBalanceCustomize);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return hjhAccountBalanceCustomizeMapper.getHjhAccountBalanceSum(hjhAccountBalanceCustomize);
	}

}
