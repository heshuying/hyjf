package com.hyjf.batch.test;

import org.springframework.beans.factory.annotation.Autowired;

public class TestTask {

    @Autowired
    TestService testService;
    
    public void run() {
        System.out.println("test" + testService.insertTest());
        
    }
}
