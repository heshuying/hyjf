/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: Michael
 * @version: 1.0
 * Created at: 2015年12月25日 上午11:53:23
 * Modification History:
 * Modified by : 
 */

package com.hyjf.web.htl.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.hyjf.mybatis.model.auto.Product;
import com.hyjf.web.htl.invest.HtlCommonService;

//实现了接口ServletContextListener,也就是说他必须实现contextDestroyed, contextInitialized这两个方法

public class ProductInitListener implements ServletContextListener {
	private ServletContext context = null;

	public void contextInitialized(ServletContextEvent event) {
		context = event.getServletContext();
		// 初始化调用方法
		WebApplicationContext rwp = WebApplicationContextUtils
				.getRequiredWebApplicationContext(event.getServletContext());
		HtlCommonService myservice = (HtlCommonService) rwp.getBean("htlCommonService");
		Product product = myservice.getProduct();
		// 然后进行自己的处理, 想做什么都可以.
		if (product != null) {
			context.setAttribute("HtlProduct", product);
		}
		new ProductUtils(product);
	}

	public void contextDestroyed(ServletContextEvent event) {
	}

}