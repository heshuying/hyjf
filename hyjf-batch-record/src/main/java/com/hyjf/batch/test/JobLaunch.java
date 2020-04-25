package com.hyjf.batch.test;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
  
/**
 * job任务
 * @author HP
 *
 */
public class JobLaunch {  
    
    /** 
     * @param args 
     */  
    public static void main(String[] args) {  
    	  ApplicationContext context = new ClassPathXmlApplicationContext("conf/spring-batch-account.xml");  
          JobLauncher launcher = (JobLauncher) context.getBean("jobLauncher");  
          Job job = (Job) context.getBean("csvJob");  
          try {  
              JobExecution result = launcher.run(job, new JobParameters());  
              System.out.println(result.toString());  
          } catch (Exception e) {  
              e.printStackTrace();  
          }  
    }  
}  