package com.hyjf.admin.manager.borrow.borrowlog;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.BorrowLogCommonCustomize;
import com.hyjf.mybatis.model.customize.BorrowLogCustomize;

@Service
public class BorrowLogServiceImpl extends BaseServiceImpl implements BorrowLogService {

	/**
	 * COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public Long countBorrowLog(BorrowLogCommonCustomize borrowLogCustomize) {
		return this.borrowLogCustomizeMapper.countBorrowLog(borrowLogCustomize);
	}


	

	/**
	 * 列表导出
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public List<BorrowLogCustomize> exportBorrowLogList(BorrowLogCommonCustomize borrowLogCustomize) {
		return this.borrowLogCustomizeMapper.exportBorrowLogList(borrowLogCustomize);
	}


	/**
     * 列表
     * 
     * @return
     */
    public List<BorrowLogCustomize> selectBorrowLogList(BorrowLogCommonCustomize borrowLogCustomize) {
        
       return this.borrowLogCustomizeMapper.selectBorrowLogList(borrowLogCustomize);
    }



}
