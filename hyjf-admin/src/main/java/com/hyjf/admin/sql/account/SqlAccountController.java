package com.hyjf.admin.sql.account;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.mybatis.model.customize.SqlAccountCustomize;

@Controller
@RequestMapping(SqlAccountDefine.REQUEST_MAPPING)
public class SqlAccountController {
	@Autowired
	private SqlAccountService sqlAccountService;
	
	@RequestMapping(SqlAccountDefine.LIST)
	public ModelAndView selectBorrowTender(HttpServletRequest request, HttpServletResponse response){
		ModelAndView modelAndView = new ModelAndView(SqlAccountDefine.LIST_PATH);
		
		List<SqlAccountCustomize> list = sqlAccountService.selectAccountToday();
		modelAndView.addObject("lists", list);
		
		return modelAndView;
		
	}

	
	
}
