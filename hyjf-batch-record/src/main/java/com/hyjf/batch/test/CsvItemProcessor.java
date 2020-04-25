/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2017
 * Company: HYJF Corporation
 * @author: Michael
 * @version: 1.0
 * Created at: 2017年4月6日 下午4:36:17
 * Modification History:
 * Modified by : 
 */
	
package com.hyjf.batch.test;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;  
  

/**
 * @author Michael
 */
@Component("csvItemProcessor")  
public class CsvItemProcessor implements ItemProcessor<Student, Student> {  
  
    /** 
     * 对取到的数据进行简单的处理。 
     *  
     * @param student 
     *            处理前的数据。 
     * @return 处理后的数据。 
     * @exception Exception 
     *                处理是发生的任何异常。 
     */  
    @Override  
    public Student process(Student student) throws Exception {  
    	Student writeStudent = new Student();
        /* 合并ID和名字 */  
    	writeStudent.setUsername(student.getUserId() + "--" + student.getUsername());
        /* 姓名 */  
    	writeStudent.setRealname(student.getRealname() + "ssss");
        /* 分数加10 */  
    	writeStudent.setPhone(student.getPhone() + "1234"); 
        System.out.println(writeStudent.getUsername());
        /* 将处理后的结果传递给writer */  
        return writeStudent;  
    }  
}