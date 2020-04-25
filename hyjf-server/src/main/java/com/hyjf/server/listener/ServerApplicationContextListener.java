package com.hyjf.server.listener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.cache.SerializeUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.ServerApplication;

public class ServerApplicationContextListener implements ServletContextListener {
    
    //定义MySQL的数据库驱动程序  
    public String DBDRIVER = "com.mysql.jdbc.Driver";  
    //定义MySQL数据库的连接地址  
    public String DBURL = "";  
    //MySQL数据库的连接用户名和连接密码  
    public String DBUSER = "";  
    public String DBPASS = "";
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LogUtil.infoLog(this.getClass().getName(), "contextInitialized", "<----------- hyjf-server项目启动,开始容器装载-第三方接口数据装载进Redis ----------->");
        /*
        //读取配置问价内容
        Properties pro = new Properties();
        FileInputStream in = null;
        try {
            in = new FileInputStream("classpath:properties/jdbc.properties");
            pro.load(in);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        DBDRIVER = pro.getProperty("jdbc_driverClassName");
        DBURL = pro.getProperty("jdbc_url");
        DBUSER = pro.getProperty("jdbc_username");
        DBPASS = pro.getProperty("jdbc_password");
        */
        DBDRIVER = PropUtils.getJdbc("jdbc_driverClassName");
        DBURL = PropUtils.getJdbc("jdbc_url");
        DBUSER = PropUtils.getJdbc("jdbc_username");
        DBPASS = PropUtils.getJdbc("jdbc_password");
        
        List<ServerApplication> serverApplicationList = new ArrayList<ServerApplication>();
        
        //jdbc查询数据库
        Connection conn = null;   
        try{    
            Class.forName(DBDRIVER);   
        }
        catch(ClassNotFoundException e){    
            e.printStackTrace();   
        }   
        String sql = "SELECT id, appid, appkey, name, 'describe', version, releasetime, developer, company, mobile, phone, fax, email, address, type, status FROM hyjf_server_application";   
        try {    
            conn = DriverManager.getConnection(DBURL, DBUSER, DBPASS); 
            Statement stmt = conn.createStatement();    
            ResultSet rs = stmt.executeQuery(sql);    
            while(rs.next()){
                ServerApplication serverApplication = new ServerApplication();
                serverApplication.setId(rs.getInt("id"));
                serverApplication.setAppid(rs.getString("appid"));
                serverApplication.setAppkey(rs.getString("appkey"));
                serverApplication.setName(rs.getString("name"));
                serverApplication.setDescribe(rs.getString("describe"));
                serverApplication.setVersion(rs.getString("version"));
                serverApplication.setReleasetime(rs.getInt("releasetime"));
                serverApplication.setDeveloper(rs.getString("developer"));
                serverApplication.setCompany(rs.getString("company"));
                serverApplication.setMobile(rs.getInt("mobile"));
                serverApplication.setPhone(rs.getString("phone"));
                serverApplication.setFax(rs.getString("fax"));
                serverApplication.setEmail(rs.getString("email"));
                serverApplication.setAddress(rs.getString("address"));
                serverApplication.setType(rs.getString("type"));
                serverApplication.setStatus(rs.getInt("status"));
                serverApplicationList.add(serverApplication);
            }    
            conn.close();   
        } catch (SQLException e1) {    
            e1.printStackTrace();   
        }

        //处理所有第三方接口信息数据
        if(serverApplicationList!=null && serverApplicationList.size()>0){
            LogUtil.infoLog(this.getClass().getName(), "contextInitialized", "<----------- 共有"+serverApplicationList.size()+"条接口数据需要装载进Redis ----------->");
            for(ServerApplication serverApplication: serverApplicationList){
                RedisUtils.set(("Third-Party-Application:"+serverApplication.getAppid()).getBytes(), SerializeUtils.serialize(serverApplication));
            }
        }else{
            LogUtil.infoLog(this.getClass().getName(), "contextInitialized", "<----------- 没有第三方接口应用信息 ----------->");
        }
        LogUtil.infoLog(this.getClass().getName(), "contextInitialized", "<----------- 容器装载完成,服务器将完成后续的启动过程 ----------->");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // TODO Auto-generated method stub 
    }

}
