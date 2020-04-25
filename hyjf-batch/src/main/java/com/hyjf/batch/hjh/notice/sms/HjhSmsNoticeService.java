package com.hyjf.batch.hjh.notice.sms;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.Borrow;

import java.util.List;

public interface HjhSmsNoticeService extends BaseService {

    List<Borrow> selectOverdueBorrowList();

    void sendSmsForManager(String borrowNid, Integer userId);
    
}
