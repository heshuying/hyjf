package com.hyjf.admin.submissions;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.SubmissionsWithBLOBs;
import com.hyjf.mybatis.model.customize.SubmissionsCustomize;

@Service
public class SubmissionsServiceImpl extends BaseServiceImpl implements SubmissionsService {

	
	/**
	 * 根据检索条件取得总数量 分页用
	 * @param msb
	 * @return
	 * @author Administrator
	 */
		
	@Override
	public int countRecordTotal(Map<String, Object> msb) {
		int count = this.submissionsCustomizeMapper.countRecordTotal(msb);
		return count;
			
	}

	/**
	 * 根据查询条件 取得数据
	 * @param msb
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 * @author Administrator
	 */
		
	@Override
	public List<SubmissionsCustomize> queryRecordList(Map<String, Object> msb, int limitStart, int limitEnd) {
		if (limitStart == 0 || limitStart > 0) {
			msb.put("limitStart", limitStart);
		}
		if (limitEnd > 0) {
			msb.put("limitEnd", limitEnd);
		}
		
		return submissionsCustomizeMapper.queryRecordList(msb);
			
	}

	/**
	 * 根据主键编号，取得相应的意见反馈信息
	 * @param id
	 * @return
	 * @author Administrator
	 */
		
	@Override
	public SubmissionsWithBLOBs querySelectedSubmissions(String id) {
		
		return submissionsMapper.selectByPrimaryKey(Integer.valueOf(id));
			
	}

	/**
	 * 更新意见反馈
	 * @param submissions
	 * @author Administrator
	 */
	@Override
	public void updateSubmissions(SubmissionsWithBLOBs submissions) {
		
		submissionsMapper.updateByPrimaryKeySelective(submissions);	
	}

	

}
