package com.hyjf.admin.exception.increaseinterestexception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.BorrowApicron;
import com.hyjf.mybatis.model.auto.BorrowApicronExample;
import com.hyjf.mybatis.model.customize.admin.AdminIncreaseInterestExceptionCustomize;

/***
 * 融通宝加息异常处理Service实现类
 * 
 * @ClassName IncreaseInterestExceptionServiceImpl
 * @author liuyang
 * @date 2017年1月5日 下午5:38:59
 */
@Service
public class IncreaseInterestExceptionServiceImpl extends BaseServiceImpl implements IncreaseInterestExceptionService {

	/**
	 * 检索列表件数
	 * 
	 * @Title countRecordList
	 * @param form
	 * @return
	 */
	@Override
	public int countRecordList(IncreaseInterestExceptionBean form) {
		Map<String, Object> param = new HashMap<String, Object>();
		// 项目编号检索
		if (StringUtils.isNotEmpty(form.getBorrowNidSrch())) {
			param.put("borrowNidSrch", form.getBorrowNidSrch());
		}
		return adminIncreaseInterestExceptionCustomizeMapper.countRecordList(param);
	}

	/**
	 * 检索列表
	 * 
	 * @Title selectRecordList
	 * @param form
	 * @return
	 */
	@Override
	public List<AdminIncreaseInterestExceptionCustomize> selectRecordList(IncreaseInterestExceptionBean form, int limitStart, int limitEnd) {
		Map<String, Object> param = new HashMap<String, Object>();
		// 项目编号检索
		if (StringUtils.isNotEmpty(form.getBorrowNidSrch())) {
			param.put("borrowNidSrch", form.getBorrowNidSrch());
		}
		if (limitStart != -1) {
			param.put("limitStart", limitStart);
			param.put("limitEnd", limitEnd);
		}

		return adminIncreaseInterestExceptionCustomizeMapper.selectRecordList(param);
	}

	/**
	 * 更新borrowApr
	 * 
	 * @Title updateBorrowApr
	 * @param form
	 */
	@Override
	public void updateBorrowApr(IncreaseInterestExceptionBean form) {

		BorrowApicronExample example = new BorrowApicronExample();
		BorrowApicronExample.Criteria cra = example.createCriteria();
		// aprid
		if (StringUtils.isNotEmpty(form.getId())) {
			cra.andIdEqualTo(Integer.parseInt(form.getId()));
		}
		if (StringUtils.isNotEmpty(form.getBorrowNid())) {
			cra.andBorrowNidEqualTo(form.getBorrowNid());
		}
		List<BorrowApicron> list = borrowApicronMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			BorrowApicron api = list.get(0);
			api.setData("");
			api.setExtraYieldRepayStatus(0);
			this.borrowApicronMapper.updateByPrimaryKey(api);
		}
	}

}
