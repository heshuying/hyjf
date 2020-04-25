package com.hyjf.batch.test;

import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.AdminExample;

@Service
public class TestServiceImpl extends BaseServiceImpl implements TestService {

    public long insertTest() {
        long cnt = adminMapper.countByExample(new AdminExample());

        // TODO Auto-generated method stub
        System.out.println("insertTest ..." + cnt);
        
        return cnt;
    }

}
